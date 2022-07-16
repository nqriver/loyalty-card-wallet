package pl.nqriver.cardwallet.card.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.query.GetCardActivityWindowQuery;
import pl.nqriver.cardwallet.card.application.ports.input.query.GetCardBalanceQuery;
import pl.nqriver.cardwallet.card.application.ports.input.query.GetCardGeneralInfoQuery;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardPort;
import pl.nqriver.cardwallet.card.domain.Activity;
import pl.nqriver.cardwallet.card.domain.ActivityType;
import pl.nqriver.cardwallet.card.domain.Balance;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class LoyaltyCardQueryService implements GetCardBalanceQuery,
        GetCardActivityWindowQuery,
        GetCardGeneralInfoQuery {

    private final LoyaltyCardPort loyaltyCardPort;


    @Override
    public Balance getCardBalance(LoyaltyCard.LoyaltyCardId id) {
        return loyaltyCardPort.loadLoyaltyCardWithoutActivities(id)
                .calculateBalance();
    }

    @Override
    public List<Activity> getActivitiesOfCardForPeriod(LoyaltyCard.LoyaltyCardId id,
                                                       LocalDateTime since,
                                                       LocalDateTime until) {
        return loyaltyCardPort
                .loadLoyaltyCardWithActivitiesOfPeriod(id, since, until)
                .getActivityWindow()
                .getActivities();
    }

    @Override
    public List<Activity> getIncomingActivitiesOfCardForPeriod(LoyaltyCard.LoyaltyCardId id, LocalDateTime start, LocalDateTime end) {
        return this.getActivitiesOfCardForPeriod(id, start, end)
                .stream()
                .filter(activity -> ActivityType.INCOMING.equals(activity.getTypeOfActivity()))
                .toList();
    }

    @Override
    public List<Activity> getOutgoingActivitiesOfCardForPeriod(LoyaltyCard.LoyaltyCardId id, LocalDateTime start, LocalDateTime end) {
        return this.getActivitiesOfCardForPeriod(id, start, end)
                .stream()
                .filter(activity -> ActivityType.OUTGOING.equals(activity.getTypeOfActivity()))
                .toList();
    }

    @Override
    public LoyaltyCard getCardInfo(LoyaltyCard.LoyaltyCardId id) {
        return loyaltyCardPort.loadLoyaltyCardWithoutActivities(id);
    }
}

