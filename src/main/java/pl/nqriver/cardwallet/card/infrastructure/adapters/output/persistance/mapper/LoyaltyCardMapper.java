package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.domain.ActivityWindow;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.Points;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.entity.ActivityEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.entity.LoyaltyCardEntity;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LoyaltyCardMapper {

    private final ActivityMapper activityMapper;

    public LoyaltyCard mapToDomainObject(
            LoyaltyCardEntity loyaltyCardEntity,
            List<ActivityEntity> activities,
            Long withdrawalBalance,
            Long depositBalance
    ) {
//        Points baselinePoints = Points.subtract(Points.of(depositBalance), Points.of(withdrawalBalance));
        Points baselinePoints = Points.of(0);
        return LoyaltyCard.withId(
                new LoyaltyCard.LoyaltyCardId(loyaltyCardEntity.getId()),
                baselinePoints,
                activityMapper.mapToActivityWindow(activities)
        );
    }

    public LoyaltyCard mapToDomainObject(LoyaltyCardEntity loyaltyCardEntity) {
        Points baselinePoints = Points.of(0);
        return LoyaltyCard.withId(
                new LoyaltyCard.LoyaltyCardId(loyaltyCardEntity.getId()),
                baselinePoints,
                new ActivityWindow(new ArrayList<>())
        );
    }

}
