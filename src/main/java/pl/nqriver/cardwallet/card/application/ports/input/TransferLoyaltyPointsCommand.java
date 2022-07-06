package pl.nqriver.cardwallet.card.application.ports.input;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import pl.nqriver.cardwallet.card.domain.Points;

import static pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

@Value
@Builder
@AllArgsConstructor
public class TransferLoyaltyPointsCommand {

    private final LoyaltyCardId sourceCardId;
    private final LoyaltyCardId targetCardId;
    private final Points points;
}
