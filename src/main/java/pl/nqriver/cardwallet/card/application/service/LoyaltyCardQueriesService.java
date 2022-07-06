package pl.nqriver.cardwallet.card.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.GetCardActivityWindowQuery;
import pl.nqriver.cardwallet.card.application.ports.input.GetCardBalanceQuery;
import pl.nqriver.cardwallet.card.application.ports.input.GetCardGeneralInfoQuery;
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

    private final LoyaltyCardPort loadLoyaltyCardPort;


    @Override
    public Balance getCardBalance(LoyaltyCard.LoyaltyCardId id) {
        return Balance.ofTotal(loadLoyaltyCardPort.loadLoyaltyCard(id));
    }

    @Override
    public Balance getCardBalanceDetails(LoyaltyCard.LoyaltyCardId id) {
        return Balance.of(loadLoyaltyCardPort.loadLoyaltyCard(id));
    }


    @Override
    public List<Activity> getActivitiesOfCardForPeriod(LoyaltyCard.LoyaltyCardId id,
                                                       LocalDateTime start, LocalDateTime end) {
        return loadLoyaltyCardPort
                .loadLoyaltyCard(id)
                .getActivityWindow()
                .getActivities()
                .stream()
                .filter(e -> e.getTimestamp().isAfter(start) && e.getTimestamp().isBefore(end))
                .toList();
    }

    @Override
    public LoyaltyCard getCardInfo(LoyaltyCard.LoyaltyCardId id) {
        return loadLoyaltyCardPort.loadLoyaltyCardGeneralInfo(id);
    }
}

