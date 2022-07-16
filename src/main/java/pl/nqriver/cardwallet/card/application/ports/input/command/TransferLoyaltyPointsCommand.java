package pl.nqriver.cardwallet.card.application.ports.input.command;

import pl.nqriver.cardwallet.card.domain.Points;

import static pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

public record TransferLoyaltyPointsCommand(LoyaltyCardId sourceCardId,
                                           LoyaltyCardId targetCardId,
                                           Points points) {

}
