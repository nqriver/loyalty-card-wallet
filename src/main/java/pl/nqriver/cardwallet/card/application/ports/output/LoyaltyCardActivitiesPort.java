package pl.nqriver.cardwallet.card.application.ports.output;

import pl.nqriver.cardwallet.card.domain.LoyaltyCard;

public interface LoyaltyCardActivitiesPort {

    void updateActivities(LoyaltyCard loyaltyCard);
}
