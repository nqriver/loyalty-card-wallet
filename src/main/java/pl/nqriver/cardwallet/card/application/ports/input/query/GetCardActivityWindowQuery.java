package pl.nqriver.cardwallet.card.application.ports.input.query;

import pl.nqriver.cardwallet.card.domain.Activity;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;

import java.time.LocalDateTime;
import java.util.List;

public interface GetCardActivityWindowQuery {

    List<Activity> getActivitiesOfCardForPeriod(LoyaltyCard.LoyaltyCardId id,
                                                LocalDateTime start, LocalDateTime end);

}
