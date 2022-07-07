package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.nqriver.cardwallet.card.application.ports.input.command.TransferLoyaltyPointsCommand;
import pl.nqriver.cardwallet.card.application.ports.input.command.TransferLoyaltyPointsUseCase;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.domain.Points;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@RestController
@RequiredArgsConstructor
public class TransferPointsController {

    private final TransferLoyaltyPointsUseCase transferLoyaltyPointsUseCase;

    @PostMapping("/cards/send/{sourceCardId}/{targetCardId}/{points}")
    ResponseEntity<?> transferPoints(
            @PathVariable("sourceCardId") @NotNull Long sourceCardId,
            @PathVariable("targetCardId") @NotNull Long targetCardId,
            @PathVariable("points") @Positive Long points
    ) {

        TransferLoyaltyPointsCommand command = TransferLoyaltyPointsCommand.builder()
                .sourceCardId(LoyaltyCardId.of(sourceCardId))
                .targetCardId(LoyaltyCardId.of(targetCardId))
                .points(Points.of(points))
                .build();
        transferLoyaltyPointsUseCase.transferLoyaltyPoints(command);
        return ResponseEntity.noContent().build();
    }
}
