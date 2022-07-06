package pl.nqriver.cardwallet.card.application.ports.input;

import pl.nqriver.cardwallet.card.domain.Balance;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.domain.Points;

public interface GetCardBalanceQuery {
    Balance getCardBalance(LoyaltyCardId id);

    Balance getCardBalanceDetails(LoyaltyCardId id);
}
