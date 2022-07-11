package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.mapper;

import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.domain.*;
import pl.nqriver.cardwallet.card.domain.Activity.ActivityId;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.ActivityEntity;

import java.util.List;

@Component
public class ActivityMapper {

    public ActivityWindow mapToActivityWindow(List<ActivityEntity> activities) {
        List<Activity> activitiesList = activities.stream()
                .map(this::mapActivityEntityToDomainObject)
                .toList();
        return ActivityWindow.of(
                activitiesList);

    }

    private Activity mapActivityEntityToDomainObject(ActivityEntity activityEntity) {
        return TransferActivity.builder()
                .id(ActivityId.of(activityEntity.getId()))
                .ownerCardId(
                        LoyaltyCardId.of(activityEntity.getOwnerLoyaltyCardId()))
                .sourceCardId(
                        LoyaltyCardId.of(activityEntity.getSourceLoyaltyCardId()))
                .targetCardId(
                        LoyaltyCardId.of(activityEntity.getTargetLoyaltyCardId()))
                .timestamp(activityEntity.getTimestamp())
                .points(Points.of(activityEntity.getPoints()))
                .build();
    }

    public ActivityEntity mapDomainObjectToActivityEntity(Activity activity) {
        return new ActivityEntity(
                activity.getId() == null ? null : activity.getId().getValue(),
                activity.getTimestamp(),
                activity.getOwnerCardId().getValue(),
                activity.getSourceCardId().getValue(),
                activity.getTargetCardId().getValue(),
                activity.getPoints().getAmount().longValue(),
                activity.getTypeOfActivity().equals(ActivityType.INCOMING)
        );
    }
}
