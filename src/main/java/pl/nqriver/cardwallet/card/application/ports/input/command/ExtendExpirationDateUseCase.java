package pl.nqriver.cardwallet.card.application.ports.input.command;

import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

public interface ExtendExpirationDateUseCase {
    void extendExpirationDate(LoyaltyCardId id);
}
