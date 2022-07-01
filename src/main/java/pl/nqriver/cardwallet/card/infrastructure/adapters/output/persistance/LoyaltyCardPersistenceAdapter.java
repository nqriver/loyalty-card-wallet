package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.output.CreateLoyaltyCardPort;
import pl.nqriver.cardwallet.card.application.ports.output.LoadLoyaltyCardPort;
import pl.nqriver.cardwallet.card.application.ports.output.UpdateLoyaltyCardStatePort;
import pl.nqriver.cardwallet.card.domain.Activity;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.Points;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.entity.ActivityEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.entity.LoyaltyCardEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.mapper.ActivityMapper;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.mapper.LoyaltyCardMapper;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.repository.ActivityRepository;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.repository.LoyaltyCardRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Component
public class LoyaltyCardPersistenceAdapter implements LoadLoyaltyCardPort, UpdateLoyaltyCardStatePort, CreateLoyaltyCardPort {

    private final LoyaltyCardRepository loyaltyCardRepository;
    private final ActivityRepository activityRepository;
    private final LoyaltyCardMapper loyaltyCardMapper;
    private final ActivityMapper activityMapper;

    @Override
    public LoyaltyCard loadLoyaltyCard(LoyaltyCard.LoyaltyCardId id) {
        Long loyaltyCardId = id.getValue();
        LoyaltyCardEntity loyaltyCardEntity =
                loyaltyCardRepository.findById(loyaltyCardId)
                        .orElseThrow(EntityNotFoundException::new);

        List<ActivityEntity> activities = activityRepository.findByOwner(loyaltyCardId);

        Long pointsDepositBalance = getValueElseZero
                (activityRepository.getPointsDepositBalance(loyaltyCardId));
        Long pointsWithdrawalBalance = getValueElseZero(
                activityRepository.getPointsWithdrawalBalance(loyaltyCardId));

        return loyaltyCardMapper.mapToDomainObject(loyaltyCardEntity,
                activities,
                pointsWithdrawalBalance,
                pointsDepositBalance);
    }

    private Long getValueElseZero(Long pointsWithdrawalBalance) {
        return Objects.isNull(pointsWithdrawalBalance) ? 0L : pointsWithdrawalBalance;
    }

    @Override
    public void updateActivities(LoyaltyCard loyaltyCard) {

        for (Activity activity : loyaltyCard.getActivityWindow()
                .getActivities()) {
            if (Objects.isNull(activity.getId())) {
                activityRepository.save(activityMapper.mapDomainObjectToActivityEntity(activity));
            }
        }

    }

    @Override
    public LoyaltyCard createLoyaltyCard() {
        return loyaltyCardMapper.mapToDomainObject(
                loyaltyCardRepository.save(new LoyaltyCardEntity()));
    }

}
