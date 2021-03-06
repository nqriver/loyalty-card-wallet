package pl.nqriver.cardwallet.card.application.ports.input.command;

import pl.nqriver.cardwallet.card.application.ports.input.command.CreateCardCommand;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;

public interface CreateCardUseCase {
    LoyaltyCard setUpNewLoyaltyCard(CreateCardCommand command);
}
