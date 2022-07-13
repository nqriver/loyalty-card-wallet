package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.command.CreateCardCommand;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardActivitiesPort;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardPort;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.ActivityEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.LoyaltyCardEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.exception.ResourceNotFoundException;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.mapper.ActivityMapper;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.mapper.LoyaltyCardMapper;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.repository.ActivityRepository;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.repository.LoyaltyCardRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
@Slf4j
class LoyaltyCardPersistenceAdapter implements LoyaltyCardPort, LoyaltyCardActivitiesPort {

    private static final String CARD_NOT_FOUND_FORMAT = "Loyalty card of id: %d cannot be found";

    private final LoyaltyCardRepository loyaltyCardRepository;
    private final ActivityRepository activityRepository;
    private final LoyaltyCardMapper loyaltyCardMapper;
    private final ActivityMapper activityMapper;

    @Override
    public LoyaltyCard loadLoyaltyCardWithAllActivities(LoyaltyCard.LoyaltyCardId id) {
        Long loyaltyCardId = id.getValue();
        log.debug("Persistence adapter: Fetching loyalty card with id: {}", loyaltyCardId);
        LoyaltyCardEntity loyaltyCardEntity =
                loyaltyCardRepository.findById(loyaltyCardId)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(CARD_NOT_FOUND_FORMAT, loyaltyCardId)));
        log.debug("Persistence adapter: Fetching all activities of loyalty card with id: {}", loyaltyCardId);
        List<ActivityEntity> activities = activityRepository.findByOwner(loyaltyCardId);

        return loyaltyCardMapper.mapToDomainWithAllActivities(loyaltyCardEntity, activities);
    }

    @Override
    public LoyaltyCard loadLoyaltyCardWithActivitiesOfPeriod(LoyaltyCard.LoyaltyCardId id,
                                                             LocalDateTime since, LocalDateTime until) {
        Long loyaltyCardId = id.getValue();
        LoyaltyCardEntity loyaltyCardEntity =
                loyaltyCardRepository.findById(loyaltyCardId)
                        .orElseThrow(() -> new ResourceNotFoundException(String.format(CARD_NOT_FOUND_FORMAT, loyaltyCardId)));

        log.debug("Persistence adapter: Fetching activities between dates {} - {} of loyalty card with id: {}",
                since, until, loyaltyCardId);
        List<ActivityEntity> activities = activityRepository.findByOwnerBetweenDates(loyaltyCardId, since, until);

        log.debug("Persistence adapter: Getting deposit and withdrawal balance between dates {} - {} of loyalty card with id: {}",
                since, until, loyaltyCardId);

        Long depositedPointsOutOfWindow = getValueElseZero
                (activityRepository.getPointsDepositBalanceExcludingPeriodOf(loyaltyCardId, since, until));
        Long withdrawnPointsOutOfWindow = getValueElseZero
                (activityRepository.getPointsWithdrawalBalanceExcludingPeriodOf(loyaltyCardId, since, until));

        return loyaltyCardMapper.mapToDomain(loyaltyCardEntity,
                activities,
                withdrawnPointsOutOfWindow,
                depositedPointsOutOfWindow);
    }


    @Override
    public LoyaltyCard loadLoyaltyCardWithoutActivities(LoyaltyCard.LoyaltyCardId id) {
        Long cardId = id.getValue();
        log.debug("Persistence adapter: Fetching loyalty card with id: {}", cardId);
        LoyaltyCardEntity entity = loyaltyCardRepository.findById(cardId).orElseThrow(
                () -> new ResourceNotFoundException(String.format(CARD_NOT_FOUND_FORMAT, cardId)));

        log.debug("Persistence adapter: Getting total deposit and withdrawal balance of loyalty card with id: {}", cardId);
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
        log.info("Persistence Adapter: Updating activities of loyalty card with id: {}", loyaltyCard.getId().get());

    }

    @Override
    public LoyaltyCard createLoyaltyCard(CreateCardCommand command,
                                         LocalDateTime createdAt,
                                         LocalDateTime expiresAt) {
        LoyaltyCardEntity entity = loyaltyCardMapper.mapToEntity(command, createdAt, expiresAt);
        LoyaltyCardEntity savedEntity = loyaltyCardRepository.save(entity);
        log.info("Persistence Adapter: Loyalty card saved by id: {}", savedEntity.getId());
        return loyaltyCardMapper.mapToDomain(savedEntity,
                Collections.emptyList(),
                0L, 0L);
    }

    @Override
    public void updateExpirationDate(LoyaltyCard.LoyaltyCardId id, LocalDateTime newExpirationDate) {
        Long idValue = id.getValue();

        LoyaltyCardEntity loyaltyCard = loyaltyCardRepository.findById(idValue)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(CARD_NOT_FOUND_FORMAT, idValue)));
        loyaltyCard.setExpiresAt(newExpirationDate);
        log.info("Persistence Adapter: Loyalty card of id {} updated with expiration date of{}", loyaltyCard.getId(), loyaltyCard.getExpiresAt());
    }

}
