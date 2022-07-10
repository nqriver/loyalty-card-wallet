package pl.nqriver.cardwallet.card.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.query.GetCardActivityWindowQuery;
import pl.nqriver.cardwallet.card.application.ports.input.query.GetCardBalanceQuery;
import pl.nqriver.cardwallet.card.application.ports.input.query.GetCardGeneralInfoQuery;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardPort;
import pl.nqriver.cardwallet.card.domain.Activity;
import pl.nqriver.cardwallet.card.domain.Balance;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class LoyaltyCardQueriesService implements GetCardBalanceQuery,
        GetCardActivityWindowQuery,
        GetCardGeneralInfoQuery {

    private final LoyaltyCardPort loyaltyCardPort;


    @Override
    public Balance getCardBalance(LoyaltyCard.LoyaltyCardId id) {
        return loyaltyCardPort.loadLoyaltyCardWithoutActivities(id)
                .getBalance();
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
    public LoyaltyCard getCardInfo(LoyaltyCard.LoyaltyCardId id) {
        return loyaltyCardPort.loadLoyaltyCardWithoutActivities(id);
    }
}

