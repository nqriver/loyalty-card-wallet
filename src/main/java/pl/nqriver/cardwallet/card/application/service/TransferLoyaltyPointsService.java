package pl.nqriver.cardwallet.card.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.command.TransferLoyaltyPointsCommand;
import pl.nqriver.cardwallet.card.application.ports.input.TransferLoyaltyPointsUseCase;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardPort;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardActivitiesPort;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.domain.Points;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Component
public class TransferLoyaltyPointsService implements TransferLoyaltyPointsUseCase {

    private final LoyaltyCardPort loadLoyaltyCardPort;
    private final LoyaltyCardActivitiesPort loyaltyCardActivitiesPort;


    @Transactional
    @Override
    public void transferLoyaltyPoints(TransferLoyaltyPointsCommand command) {
        LoyaltyCard sourceCard = loadLoyaltyCardPort.loadLoyaltyCard(
                command.getSourceCardId());
        LoyaltyCard targetCard = loadLoyaltyCardPort.loadLoyaltyCard(
                command.getTargetCardId());

        LoyaltyCardId sourceCardId = sourceCard.getId().orElseThrow(() -> new IllegalStateException("Source card ID is empty"));
        LoyaltyCardId targetCardId = targetCard.getId().orElseThrow(() -> new IllegalStateException("Target card ID is empty"));

        Points transferPoints = command.getPoints();

        boolean withdrawalSuccess = sourceCard.withdraw(transferPoints, targetCardId);
        if (!withdrawalSuccess) {
            throw new InsufficientCardBalanceException();
        }
        targetCard.deposit(transferPoints, sourceCardId);

        loyaltyCardActivitiesPort.updateActivities(sourceCard);
        loyaltyCardActivitiesPort.updateActivities(targetCard);
    }
}
