package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nqriver.cardwallet.card.application.ports.input.CreateCardUseCase;
import pl.nqriver.cardwallet.card.application.ports.input.GetCardBalanceQuery;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

@RestController
@RequiredArgsConstructor
public class LoyaltyCardController {

    private final GetCardBalanceQuery getCardBalanceQuery;
    private final CreateCardUseCase createCardUseCase;

    @GetMapping("card/{id}/balance")
    ResponseEntity<Object> getPointsBalance(@PathVariable("id") Long loyaltyCardId) {
        return ResponseEntity.ok(getCardBalanceQuery.getCardBalance(new LoyaltyCardId(loyaltyCardId)));
    }

    @PostMapping("card")
    ResponseEntity<Object> setUpLoyaltyCard() {
        return ResponseEntity.status(HttpStatus.CREATED).body(createCardUseCase.setUpNewLoyaltyCard());
    }

}
