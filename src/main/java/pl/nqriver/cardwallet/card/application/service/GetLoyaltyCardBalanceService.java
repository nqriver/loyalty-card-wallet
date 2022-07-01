package pl.nqriver.cardwallet.card.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.GetCardBalanceQuery;
import pl.nqriver.cardwallet.card.application.ports.output.LoadLoyaltyCardPort;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.Points;

@RequiredArgsConstructor
@Component
public class GetLoyaltyCardBalanceService implements GetCardBalanceQuery {

    private final LoadLoyaltyCardPort loadLoyaltyCardPort;


    @Override
    public Points getCardBalance(LoyaltyCard.LoyaltyCardId id) {
        return loadLoyaltyCardPort.loadLoyaltyCard(id)
                .calculateBalance();
    }
}

