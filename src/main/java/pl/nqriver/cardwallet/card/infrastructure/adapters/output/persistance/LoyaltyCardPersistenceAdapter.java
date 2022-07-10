package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.command.CreateCardCommand;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardActivitiesPort;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardPort;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.entity.ActivityEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.entity.LoyaltyCardEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.exception.ResourceNotFoundException;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.mapper.ActivityMapper;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.mapper.LoyaltyCardMapper;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.repository.ActivityRepository;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.repository.LoyaltyCardRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class LoyaltyCardPersistenceAdapter implements LoyaltyCardPort, LoyaltyCardActivitiesPort {

    private final LoyaltyCardRepository loyaltyCardRepository;
    private final ActivityRepository activityRepository;
    private final LoyaltyCardMapper loyaltyCardMapper;
    private final ActivityMapper activityMapper;

    @Override
    public LoyaltyCard loadLoyaltyCardWithAllActivities(LoyaltyCard.LoyaltyCardId id) {
        Long loyaltyCardId = id.getValue();
        LoyaltyCardEntity loyaltyCardEntity =
                loyaltyCardRepository.findById(loyaltyCardId)
                        .orElseThrow(() -> new ResourceNotFoundException("Cannot find resource of id" + loyaltyCardId));
        List<ActivityEntity> activities = activityRepository.findByOwner(loyaltyCardId);

        return loyaltyCardMapper.mapToDomainWithAllActivities(loyaltyCardEntity, activities);
    }

    @Override
    public LoyaltyCard loadLoyaltyCardWithActivitiesOfPeriod(LoyaltyCard.LoyaltyCardId id,
                                                             LocalDateTime since, LocalDateTime until) {
        Long loyaltyCardId = id.getValue();
        LoyaltyCardEntity loyaltyCardEntity =
                loyaltyCardRepository.findById(loyaltyCardId)
                        .orElseThrow(() -> new ResourceNotFoundException("Cannot find resource of id" + loyaltyCardId));

        List<ActivityEntity> activities = activityRepository.findByOwnerBetweenDates(loyaltyCardId, since, until);

        Long pointsDepositBalance = getValueElseZero
                (activityRepository.getPointsDepositBalanceUntil(loyaltyCardId, since));
        Long pointsWithdrawalBalance = getValueElseZero(
                activityRepository.getPointsWithdrawalBalanceUntil(loyaltyCardId, since));

        return loyaltyCardMapper.mapToDomain(loyaltyCardEntity,
                activities,
                pointsWithdrawalBalance,
                pointsDepositBalance);
    }


    @Override
    public LoyaltyCard loadLoyaltyCardWithoutActivities(LoyaltyCard.LoyaltyCardId id) {
        Long cardId = id.getValue();
        LoyaltyCardEntity entity = loyaltyCardRepository.findById(cardId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("%s %d %s", "Resource of id:", cardId, "cannot be found")));

        Long allDepositedPoints = getValueElseZero
                (activityRepository.getPointsDepositBalance(cardId));
        Long allWithdrawnPoints = getValueElseZero(
                activityRepository.getPointsWithdrawalBalance(cardId));

        return loyaltyCardMapper.mapToDomain(
                entity,
                Collections.emptyList(),
                allWithdrawnPoints,
                allDepositedPoints
        );
    }

    private Long getValueElseZero(Long pointsWithdrawalBalance) {
        return Objects.isNull(pointsWithdrawalBalance) ? 0L : pointsWithdrawalBalance;
    }

    @Override
    public void updateActivities(LoyaltyCard loyaltyCard) {

        loyaltyCard.getActivityWindow()
                .getActivities()
                .stream()
                .filter(activity -> Objects.isNull(activity.getId()))
                .map(activityMapper::mapDomainObjectToActivityEntity)
                .forEach(activityRepository::save);

    }

    @Override
    public LoyaltyCard createLoyaltyCard(CreateCardCommand command,
                                         LocalDateTime createdAt,
                                         LocalDateTime expiresAt) {
        LoyaltyCardEntity entity = loyaltyCardMapper.mapToEntity(command, createdAt, expiresAt);
        LoyaltyCardEntity savedEntity = loyaltyCardRepository.save(entity);
        return loyaltyCardMapper.mapToDomain(savedEntity,
                Collections.emptyList(),
                0L, 0L);
    }

    @Override
    public void updateExpirationDate(LoyaltyCard.LoyaltyCardId id, LocalDateTime newExpirationDate) {
        Long idValue = id.getValue();

        LoyaltyCardEntity loyaltyCard = loyaltyCardRepository.findById(idValue)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("%Loyalty card of id: %d cannot be found", idValue)));
        loyaltyCard.setExpiresAt(newExpirationDate);
    }

}
