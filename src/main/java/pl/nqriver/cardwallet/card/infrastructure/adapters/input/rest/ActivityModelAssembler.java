package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.ActivityResponse;

import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ActivityModelAssembler implements RepresentationModelAssembler<ActivityResponse, EntityModel<ActivityResponse>> {

    @Override
    public @NonNull CollectionModel<EntityModel<ActivityResponse>> toCollectionModel(Iterable<? extends ActivityResponse> entities) {
        return CollectionModel.of(
                StreamSupport.stream(entities.spliterator(), false)
                        .map(this::toModel)
                        .toList());
    }

    @Override
    public @NonNull EntityModel<ActivityResponse> toModel(@NonNull ActivityResponse activity) {
        EntityModel<ActivityResponse> activityModel = EntityModel.of(activity,
                linkTo(methodOn(LoyaltyCardController.class).getBalance(activity.getOwnerCardId())).withRel("balance"));

        if (activity.getType().equalsIgnoreCase("incoming")) {
            activityModel.add(linkTo(methodOn(ActivitiesQueryController.class)
                    .getActivities(activity.getOwnerCardId(),
                            Optional.of(activity.getTimestamp().minusYears(1)),
                            Optional.of(activity.getTimestamp())))
                    .withRel("all incoming"));
        }
        return activityModel;
    }
}
