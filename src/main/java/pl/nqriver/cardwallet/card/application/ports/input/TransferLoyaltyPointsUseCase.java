package pl.nqriver.cardwallet.card.application.ports.input;

import pl.nqriver.cardwallet.card.application.ports.input.command.TransferLoyaltyPointsCommand;

public interface TransferLoyaltyPointsUseCase {

    void transferLoyaltyPoints(TransferLoyaltyPointsCommand command);

}
