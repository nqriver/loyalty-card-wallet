package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.LoyaltyCardResponse;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class LoyaltyCardModelAssembler implements RepresentationModelAssembler<LoyaltyCardResponse, EntityModel<LoyaltyCardResponse>> {
    @Override
    public EntityModel<LoyaltyCardResponse> toModel(@NonNull LoyaltyCardResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(LoyaltyCardController.class).getGeneralInfo(response.getId())).withRel("generalInfo"),
                linkTo(methodOn(LoyaltyCardController.class).getBalance(response.getId())).withRel("balance"),
                linkTo(methodOn(ActivitiesQueryController.class).getActivities(response.getId(),
                        Optional.of(response.getCreatedAt()),
                        Optional.of(LocalDateTime.now()))).withRel("activities")
        );
    }
}
