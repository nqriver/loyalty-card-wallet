package pl.nqriver.cardwallet.card.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.CreateCardCommand;
import pl.nqriver.cardwallet.card.application.ports.input.CreateCardUseCase;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardPort;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateLoyaltyCardService implements CreateCardUseCase {

    private final LoyaltyCardProperties loyaltyCardProperties;
    private final LoyaltyCardPort loyaltyCardPort;
    

    @Override
    public LoyaltyCard setUpNewLoyaltyCard(CreateCardCommand command) {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = loyaltyCardProperties.getExpirationDate(createdAt);
        return loyaltyCardPort.createLoyaltyCard(command, createdAt, expiresAt);
    }
}
