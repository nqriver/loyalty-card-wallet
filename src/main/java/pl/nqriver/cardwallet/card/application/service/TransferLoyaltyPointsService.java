package pl.nqriver.cardwallet.card.application.service;

import lombok.RequiredArgsConstructor;
import pl.nqriver.cardwallet.card.application.ports.input.TransferLoyaltyPointsCommand;
import pl.nqriver.cardwallet.card.application.ports.input.TransferLoyaltyPointsUseCase;
import pl.nqriver.cardwallet.card.application.ports.output.LoadLoyaltyCardPort;
import pl.nqriver.cardwallet.card.application.ports.output.UpdateLoyaltyCardStatePort;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.domain.Points;

import javax.transaction.Transactional;

@RequiredArgsConstructor
public class TransferLoyaltyPointsService implements TransferLoyaltyPointsUseCase {

    private final LoadLoyaltyCardPort loadLoyaltyCardPort;
    private final UpdateLoyaltyCardStatePort updateLoyaltyCardStatePort;


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
        if (!sourceCard.withdraw(transferPoints, targetCardId)) {
            throw new InsufficientCardBalanceException();
        }
        targetCard.deposit(transferPoints, sourceCardId);

        updateLoyaltyCardStatePort.updateActivities(sourceCard);
        updateLoyaltyCardStatePort.updateActivities(targetCard);
    }
}
