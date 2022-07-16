package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.hateoas.assembler;

import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.ActivitiesQueryController;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.LoyaltyCardController;
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
                WebMvcLinkBuilder.linkTo(methodOn(LoyaltyCardController.class).getGeneralInfo(response.id())).withRel("generalInfo"),
                linkTo(methodOn(LoyaltyCardController.class).getBalance(response.id())).withRel("balance"),
                WebMvcLinkBuilder.linkTo(methodOn(ActivitiesQueryController.class).getActivities(response.id(),
                        Optional.of(response.createdAt()),
                        Optional.of(LocalDateTime.now()))).withRel("activities")
        );
    }
}
