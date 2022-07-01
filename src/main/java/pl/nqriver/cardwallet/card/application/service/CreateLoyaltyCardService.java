package pl.nqriver.cardwallet.card.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.CreateCardUseCase;
import pl.nqriver.cardwallet.card.application.ports.output.CreateLoyaltyCardPort;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;

@Component
@RequiredArgsConstructor
public class CreateLoyaltyCardService implements CreateCardUseCase {

    private final CreateLoyaltyCardPort loyaltyCardPort;


    @Override
    public LoyaltyCard setUpNewLoyaltyCard() {
        return loyaltyCardPort.createLoyaltyCard();
    }

}
