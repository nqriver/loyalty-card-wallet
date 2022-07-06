package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.request.CreateLoyaltyCardRequest;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.BalanceResponse;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.LoyaltyCardResponse;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("cards")
@RequiredArgsConstructor
public class LoyaltyCardController {

    private final LoyaltyCardFacade facade;
    private final LoyaltyCardModelAssembler cardModelAssembler;
    private final BalanceModelAssembler balanceModelAssembler;

    @GetMapping("/{id}/balance")
    ResponseEntity<EntityModel<BalanceResponse>> getPointsBalance(@PathVariable("id") Long loyaltyCardId) {
        return ResponseEntity.ok(balanceModelAssembler.toModel(facade.getBalance(loyaltyCardId)));
    }

    @GetMapping("/{id}/balance/details")
    ResponseEntity<EntityModel<BalanceResponse>> getPointsBalanceDetails(@PathVariable("id") Long loyaltyCardId) {
        return ResponseEntity.ok(balanceModelAssembler.toModel(facade.getBalanceDetails(loyaltyCardId)));
    }

    @PostMapping("")
    ResponseEntity<EntityModel<LoyaltyCardResponse>> setUpLoyaltyCard(@RequestBody final CreateLoyaltyCardRequest request) {
        LoyaltyCardResponse loyaltyCardResponse = facade.create(request);
        return ResponseEntity
                .created(
                    linkTo(methodOn(LoyaltyCardController.class).getGeneralInfo(loyaltyCardResponse.getId())).toUri())
                .body(cardModelAssembler.toModel(loyaltyCardResponse));
    }

    @GetMapping("/{id}")
    ResponseEntity<EntityModel<LoyaltyCardResponse>> getGeneralInfo(@PathVariable("id") Long loyaltyCardId) {
        LoyaltyCardResponse loyaltyCardResponse = facade.getGeneralInfo(loyaltyCardId);
        return ResponseEntity.ok(cardModelAssembler.toModel(loyaltyCardResponse));
    }
}
