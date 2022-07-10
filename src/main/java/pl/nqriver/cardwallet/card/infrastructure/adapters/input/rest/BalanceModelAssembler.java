package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.BalanceResponse;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class BalanceModelAssembler implements RepresentationModelAssembler<BalanceResponse, EntityModel<BalanceResponse>> {

    @Override
    public EntityModel<BalanceResponse> toModel(BalanceResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(LoyaltyCardController.class).getGeneralInfo(response.getLoyaltyCardId())).withRel("generalInfo"),
                linkTo(methodOn(LoyaltyCardController.class).getBalance(response.getLoyaltyCardId())).withSelfRel(),
                linkTo(methodOn(ActivityWindowViewController.class).getActivities(response.getLoyaltyCardId(),
                        Optional.empty(),
                        Optional.of(LocalDateTime.now()))).withRel("activities"));
    }
}
