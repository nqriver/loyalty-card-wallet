package pl.nqriver.cardwallet.card.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.GetCardActivityWindowQuery;
import pl.nqriver.cardwallet.card.application.ports.input.GetCardBalanceQuery;
import pl.nqriver.cardwallet.card.application.ports.output.LoadLoyaltyCardPort;
import pl.nqriver.cardwallet.card.domain.Activity;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.Points;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Component
public class GetLoyaltyCardBalanceService
        implements GetCardBalanceQuery, GetCardActivityWindowQuery {

    private final LoadLoyaltyCardPort loadLoyaltyCardPort;


    @Override
    public Points getCardBalance(LoyaltyCard.LoyaltyCardId id) {
        return loadLoyaltyCardPort.loadLoyaltyCard(id)
                .calculateBalance();
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
}

