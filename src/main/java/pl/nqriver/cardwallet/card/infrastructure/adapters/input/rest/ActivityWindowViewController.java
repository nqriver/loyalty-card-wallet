package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.nqriver.cardwallet.card.application.ports.input.query.GetCardActivityWindowQuery;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ActivityWindowViewController {

    private final GetCardActivityWindowQuery getCardActivityWindowQuery;

    @GetMapping("card/{id}/activities")
    ResponseEntity<Object> getActivities(
            @PathVariable("id") Long loyaltyCardId,
            @RequestParam(required = false) Optional<LocalDateTime> since,
            @RequestParam(required = false) Optional<LocalDateTime> until
    ) {
        var start = since.orElse(LocalDateTime.MIN);
        var end = until.orElse(LocalDateTime.now());

        return ResponseEntity.ok(getCardActivityWindowQuery.getActivitiesOfCardForPeriod(
                new LoyaltyCard.LoyaltyCardId(loyaltyCardId), start, end));
    }

}
