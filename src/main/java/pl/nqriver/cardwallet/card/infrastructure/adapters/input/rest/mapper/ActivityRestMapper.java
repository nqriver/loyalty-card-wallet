package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import pl.nqriver.cardwallet.card.domain.Activity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.ActivityResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ActivityRestMapper {

    public ActivityResponse toActivityResponse(Activity activity) {
        return ActivityResponse.builder()
                .id(activity.getId().getValue())
                .ownerCardId(activity.getOwnerCardId().getValue())
                .sourceCardId(activity.getSourceCardId().getValue())
                .targetCardId(activity.getTargetCardId().getValue())
                .timestamp(activity.getTimestamp())
                .points(activity.getPoints().getAmount())
                .type(activity.getTypeOfActivity().name())
                .build();
    }

    public abstract List<ActivityResponse> toActivityResponseList(List<Activity> activityList);
}
