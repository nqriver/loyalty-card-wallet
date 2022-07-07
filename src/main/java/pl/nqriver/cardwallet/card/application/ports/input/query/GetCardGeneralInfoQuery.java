package pl.nqriver.cardwallet.card.application.ports.input.query;

import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

public interface GetCardGeneralInfoQuery {
    LoyaltyCard getCardInfo(LoyaltyCardId id);
}
