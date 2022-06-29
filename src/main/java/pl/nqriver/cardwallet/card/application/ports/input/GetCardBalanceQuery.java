package pl.nqriver.cardwallet.card.application.ports.input;

import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.domain.Points;

public interface GetCardBalanceQuery {
    Points getCardBalance(LoyaltyCardId id);
}
