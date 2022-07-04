package pl.nqriver.cardwallet.card.application.ports.output;

import pl.nqriver.cardwallet.card.domain.LoyaltyCard;

public interface LoyaltyCardPort {

    LoyaltyCard loadLoyaltyCard(LoyaltyCard.LoyaltyCardId id);

    LoyaltyCard createLoyaltyCard();

}
