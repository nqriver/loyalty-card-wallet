package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.domain.*;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.ActivityEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.LoyaltyCardEntity;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ActivityMapper {

    private final ActivityFactory activityFactory;


    public ActivityWindow mapToActivityWindow(List<ActivityEntity> activities) {
        List<Activity> activitiesList = activities.stream()
                .map(this::mapActivityEntityToDomainObject)
                .toList();
        return ActivityWindow.of(activitiesList);

    }

    private Activity mapActivityEntityToDomainObject(ActivityEntity activityEntity) {
        return activityFactory.getInstance(activityEntity);
    }
//
//    public ActivityEntity mapDomainObjectToActivityEntity(Activity activity) {
//        return new ActivityEntity(
//                activity.getId() == null ? null : activity.getId().getValue(),
//                activity.getTimestamp(),
//                cardMapper.mapToEntity()
//                activity.getSourceCardId() == null ? null : activity.getSourceCardId().getValue(),
//                activity.getTargetCardId() == null ? null : activity.getTargetCardId().getValue(),
//                activity.getPoints().getAmount().longValue(),
//                activity.getTypeOfActivity().equals(ActivityType.INCOMING)
//        );
//    }

    public ActivityEntity mapToEntityWithOwner(LoyaltyCardEntity ownerOfActivities, Activity activity) {
        return new ActivityEntity(
                activity.getId() == null ? null : activity.getId().getValue(),
                activity.getTimestamp(),
                ownerOfActivities,
                activity.getSourceCardId() == null ? null : activity.getSourceCardId().getValue(),
                activity.getTargetCardId() == null ? null : activity.getTargetCardId().getValue(),
                activity.getPoints().getAmount().longValue(),
                activity.getTypeOfActivity().equals(ActivityType.INCOMING));
    }
}
