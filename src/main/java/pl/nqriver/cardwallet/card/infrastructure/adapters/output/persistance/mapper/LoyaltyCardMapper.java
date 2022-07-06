package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.domain.ActivityWindow;
import pl.nqriver.cardwallet.card.domain.Holder;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.domain.Points;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.entity.ActivityEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.entity.LoyaltyCardEntity;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LoyaltyCardMapper {

    private final ActivityMapper activityMapper;

    public LoyaltyCard mapToDomainObjectWithActivities(
            LoyaltyCardEntity loyaltyCardEntity,
            List<ActivityEntity> activities,
            Long withdrawalBalance,
            Long depositBalance
    ) {
        Points baselinePoints = Points.subtract(Points.of(depositBalance), Points.of(withdrawalBalance));
        return LoyaltyCard.withId(
                new LoyaltyCardId(loyaltyCardEntity.getId()),
                baselinePoints,
                activityMapper.mapToActivityWindow(activities),
                Holder.of(loyaltyCardEntity.getHolderEmail()),
                loyaltyCardEntity.getCreatedAt(),
                loyaltyCardEntity.getExpiresAt()
        );
    }

    public LoyaltyCard mapToSimplifiedDomainObject(LoyaltyCardEntity loyaltyCardEntity) {
        Points baselinePoints = Points.of(0);
        return LoyaltyCard.withId(
                LoyaltyCardId.of(loyaltyCardEntity.getId()),
                baselinePoints,
                ActivityWindow.emptyWindow(),
                Holder.of(loyaltyCardEntity.getHolderEmail()),
                loyaltyCardEntity.getCreatedAt(),
                loyaltyCardEntity.getExpiresAt()
        );
    }

}
