package pl.nqriver.cardwallet.card.application.ports.input.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import pl.nqriver.cardwallet.card.domain.Points;

import static pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

@Value
@AllArgsConstructor
public class TransferLoyaltyPointsCommand {

    private final LoyaltyCardId sourceCardId;
    private final LoyaltyCardId targetCardId;
    private final Points points;
}
