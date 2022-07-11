package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.hateoas.assembler;

import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.ActivitiesQueryController;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.LoyaltyCardController;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.ActivityResponse;

import java.util.Optional;
import java.util.function.Supplier;
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
        return EntityModel.of(activity,
                WebMvcLinkBuilder.linkTo(methodOn(LoyaltyCardController.class).getBalance(activity.getOwnerCardId())).withRel("balance"),
                WebMvcLinkBuilder.linkTo(methodOn(ActivitiesQueryController.class)
                        .getActivities(activity.getOwnerCardId(),
                                Optional.of(activity.getTimestamp().minusYears(1)),
                                Optional.of(activity.getTimestamp()))).withSelfRel())

                        .addIf(activity.getType().equalsIgnoreCase("incoming"), () ->
                                linkTo(methodOn(ActivitiesQueryController.class)
                                        .getIncomings(activity.getOwnerCardId(),
                                                Optional.of(activity.getTimestamp().minusYears(1)),
                                                Optional.of(activity.getTimestamp())))
                                        .withRel("all incomings"))
                        .addIf(activity.getType().equalsIgnoreCase("outgoing"), () ->
                                linkTo(methodOn(ActivitiesQueryController.class)
                                        .getOutgoings(activity.getOwnerCardId(),
                                                Optional.of(activity.getTimestamp().minusYears(1)),
                                                Optional.of(activity.getTimestamp())))
                                        .withRel("all outgoings"));
    }
}
