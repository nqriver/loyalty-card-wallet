package pl.nqriver.cardwallet.card.application.ports.output;

import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

public interface LoadLoyaltyCardPort {
    LoyaltyCard loadLoyaltyCard(LoyaltyCardId id);
}
