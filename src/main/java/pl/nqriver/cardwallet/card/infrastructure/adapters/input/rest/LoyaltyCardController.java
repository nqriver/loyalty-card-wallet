package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.nqriver.cardwallet.card.domain.Points;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.request.CreateLoyaltyCardRequest;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.BalanceDetailsResponse;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.LoyaltyCardResponse;

@RestController
@RequestMapping("cards")
@RequiredArgsConstructor
public class LoyaltyCardController {

    private final LoyaltyCardFacade facade;

    @GetMapping("/{id}/balance")
    ResponseEntity<Points> getPointsBalance(@PathVariable("id") Long loyaltyCardId) {
        return ResponseEntity.ok(facade.getBalance(loyaltyCardId));
    }

    @GetMapping("/{id}/balance/details")
    ResponseEntity<BalanceDetailsResponse> getPointsBalanceDetails(@PathVariable("id") Long loyaltyCardId) {
        return ResponseEntity.ok(facade.getBalanceDetails(loyaltyCardId));
    }

    @PostMapping("")
    ResponseEntity<LoyaltyCardResponse> setUpLoyaltyCard(@RequestBody final CreateLoyaltyCardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(facade.create(request));
    }

    @GetMapping("/{id}")
    ResponseEntity<LoyaltyCardResponse> getGeneralInfo(@PathVariable("id") Long loyaltyCardId) {
        return ResponseEntity.ok(facade.getGeneralInfo(loyaltyCardId));
    }
}
