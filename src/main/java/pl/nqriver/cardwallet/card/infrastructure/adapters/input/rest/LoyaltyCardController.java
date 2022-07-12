package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.hateoas.assembler.BalanceModelAssembler;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.hateoas.assembler.LoyaltyCardModelAssembler;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.request.CreateLoyaltyCardRequest;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.BalanceResponse;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.LoyaltyCardResponse;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(LoyaltyCardController.SERVICE_PATH)
@RequiredArgsConstructor
@Api("Loyalty card services")
public class LoyaltyCardController {

    public static final String SERVICE_PATH = "cards";

    private final LoyaltyCardFacade facade;
    private final LoyaltyCardModelAssembler cardModelAssembler;
    private final BalanceModelAssembler balanceModelAssembler;

    @ApiOperation(value = "Retrieves the current points balance of a loyalty card")
    @GetMapping("/{id}/balance")
    public ResponseEntity<EntityModel<BalanceResponse>> getBalance(@PathVariable("id") Long loyaltyCardId) {
        return ResponseEntity.ok(balanceModelAssembler.toModel(facade.getBalance(loyaltyCardId)));
    }

    @ApiOperation(value = "Creates the new loyalty card with provided unique email")
    @PostMapping("")
    public ResponseEntity<EntityModel<LoyaltyCardResponse>> setUpLoyaltyCard(
            @RequestBody @Valid final CreateLoyaltyCardRequest request) {

        LoyaltyCardResponse loyaltyCardResponse = facade.create(request);
        return ResponseEntity
                .created(
                    linkTo(methodOn(LoyaltyCardController.class).getGeneralInfo(loyaltyCardResponse.getId())).toUri())
                .body(cardModelAssembler.toModel(loyaltyCardResponse));
    }

    @ApiOperation(value = "Extends the expiration date of the card by the service-specific time")
    @PutMapping("/{id}/validity")
    public ResponseEntity<?> extendExpirationDate(@PathVariable("id") Long loyaltyCardId) {
        facade.extendExpirationDate(loyaltyCardId);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Retrieves general information about the card and provides balance without loading activities")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<LoyaltyCardResponse>> getGeneralInfo(@PathVariable("id") Long loyaltyCardId) {
        LoyaltyCardResponse loyaltyCardResponse = facade.getGeneralInfo(loyaltyCardId);
        return ResponseEntity.ok(cardModelAssembler.toModel(loyaltyCardResponse));
    }
}
