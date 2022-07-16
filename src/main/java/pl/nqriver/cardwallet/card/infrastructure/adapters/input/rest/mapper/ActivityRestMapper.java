package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import pl.nqriver.cardwallet.card.domain.Activity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.ActivityResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class ActivityRestMapper {

    public ActivityResponse toActivityResponse(Activity activity) {
        return new ActivityResponse(
                activity.getId().getValue(),
                activity.getTimestamp(),
                activity.getOwnerCardId().getValue(),
                activity.getTargetCardId().getValue(),
                activity.getSourceCardId().getValue(),
                activity.getPoints().getAmount(),
                activity.getTypeOfActivity().name()
        );
    }

    public abstract List<ActivityResponse> toActivityResponseList(List<Activity> activityList);
}
