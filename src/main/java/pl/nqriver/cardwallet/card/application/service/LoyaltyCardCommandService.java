package pl.nqriver.cardwallet.card.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.command.CreateCardCommand;
import pl.nqriver.cardwallet.card.application.ports.input.command.CreateCardUseCase;
import pl.nqriver.cardwallet.card.application.ports.input.command.ExtendExpirationDateUseCase;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardPort;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LoyaltyCardCommandService implements CreateCardUseCase, ExtendExpirationDateUseCase {

    private final LoyaltyCardProperties loyaltyCardProperties;
    private final LoyaltyCardPort loyaltyCardPort;
    

    @Override
    public LoyaltyCard setUpNewLoyaltyCard(CreateCardCommand command) {
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = loyaltyCardProperties.calculateExpirationDate(createdAt);
        return loyaltyCardPort.createLoyaltyCard(command, createdAt, expiresAt);
    }

    @Override
    public void extendExpirationDate(LoyaltyCard.LoyaltyCardId id) {
        LocalDateTime newExpirationDate = loyaltyCardProperties.calculateExpirationDate(LocalDateTime.now());
        loyaltyCardPort.updateExpirationDate(id, newExpirationDate);
    }
}
