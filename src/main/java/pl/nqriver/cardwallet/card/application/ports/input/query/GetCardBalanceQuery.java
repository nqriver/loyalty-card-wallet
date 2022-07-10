package pl.nqriver.cardwallet.card.application.ports.input.query;

import pl.nqriver.cardwallet.card.domain.Balance;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

public interface GetCardBalanceQuery {
    Balance getCardBalance(LoyaltyCardId id);
}
