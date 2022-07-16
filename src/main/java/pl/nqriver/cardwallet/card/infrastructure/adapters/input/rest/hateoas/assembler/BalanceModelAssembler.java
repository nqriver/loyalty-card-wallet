package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.hateoas.assembler;

import lombok.NonNull;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.ActivitiesQueryController;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.LoyaltyCardController;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.BalanceResponse;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BalanceModelAssembler implements RepresentationModelAssembler<BalanceResponse, EntityModel<BalanceResponse>> {

    @Override
    public EntityModel<BalanceResponse> toModel(@NonNull BalanceResponse response) {
        return EntityModel.of(response,
                WebMvcLinkBuilder.linkTo(methodOn(LoyaltyCardController.class).getGeneralInfo(response.loyaltyCardId())).withRel("generalInfo"),
                linkTo(methodOn(LoyaltyCardController.class).getBalance(response.loyaltyCardId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(methodOn(ActivitiesQueryController.class).getActivities(response.loyaltyCardId(),
                        Optional.empty(),
                        Optional.of(LocalDateTime.now()))).withRel("activities"));
    }
}
