package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nqriver.cardwallet.card.application.ports.input.TransferLoyaltyPointsCommand;
import pl.nqriver.cardwallet.card.application.ports.input.TransferLoyaltyPointsUseCase;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.domain.Points;

@RestController
@RequiredArgsConstructor
public class TransferPointsController {

    private final TransferLoyaltyPointsUseCase transferLoyaltyPointsUseCase;

    @PostMapping("/cards/send/{sourceCardId}/{targetCardId}/{points}")
    void transferPoints(
            @PathVariable("sourceCardId") Long sourceCardId,
            @PathVariable("targetCardId") Long targetCardId,
            @PathVariable("points") Long points
    ) {

        TransferLoyaltyPointsCommand command = TransferLoyaltyPointsCommand.builder()
                .sourceCardId(LoyaltyCardId.of(sourceCardId))
                .targetCardId(LoyaltyCardId.of(targetCardId))
                .points(Points.of(points))
                .build();

        transferLoyaltyPointsUseCase.transferLoyaltyPoints(command);
    }
}
