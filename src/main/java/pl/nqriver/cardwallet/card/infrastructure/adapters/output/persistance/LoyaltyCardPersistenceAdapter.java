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

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
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
    public LoyaltyCard loadLoyaltyCard(LoyaltyCard.LoyaltyCardId id) {
        Long loyaltyCardId = id.getValue();
        LoyaltyCardEntity loyaltyCardEntity =
                loyaltyCardRepository.findById(loyaltyCardId)
                        .orElseThrow(() -> new ResourceNotFoundException("Cannot find resource of id" + loyaltyCardId));

        List<ActivityEntity> activities = activityRepository.findByOwner(loyaltyCardId);

        Long pointsDepositBalance = getValueElseZero
                (activityRepository.getPointsDepositBalance(loyaltyCardId));
        Long pointsWithdrawalBalance = getValueElseZero(
                activityRepository.getPointsWithdrawalBalance(loyaltyCardId));

        return loyaltyCardMapper.mapToDomainObjectWithActivities(loyaltyCardEntity,
                activities,
                pointsWithdrawalBalance,
                pointsDepositBalance);
    }

    @Override
    public LoyaltyCard loadLoyaltyCardGeneralInfo(LoyaltyCard.LoyaltyCardId id) {
        Long cardId = id.getValue();
        LoyaltyCardEntity entity = loyaltyCardRepository.findById(cardId).orElseThrow(
                () -> new ResourceNotFoundException(String.format("%s %d %s", "Resource of id:", cardId, "cannot be found")));
        return loyaltyCardMapper.mapToSimplifiedDomainObject(entity);
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
        LoyaltyCardEntity entity = new LoyaltyCardEntity();
        entity.setCreatedAt(createdAt);
        entity.setExpiresAt(expiresAt);
        entity.setHolderEmail(command.getHolderEmail());
        return loyaltyCardMapper.mapToSimplifiedDomainObject(
                loyaltyCardRepository.save(entity));
    }

}
