package pl.nqriver.cardwallet.card.application.ports.input;

import pl.nqriver.cardwallet.card.domain.LoyaltyCard;

public interface CreateCardUseCase {
    LoyaltyCard setUpNewLoyaltyCard(CreateCardCommand command);
}
