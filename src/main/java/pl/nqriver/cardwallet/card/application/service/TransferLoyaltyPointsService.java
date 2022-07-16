package pl.nqriver.cardwallet.card.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.command.TransferLoyaltyPointsCommand;
import pl.nqriver.cardwallet.card.application.ports.input.command.TransferLoyaltyPointsUseCase;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardPort;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardActivitiesPort;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.domain.LoyaltyCardOperationValidator.OperationValidationResult;
import pl.nqriver.cardwallet.card.domain.Points;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Component
@Slf4j
public class TransferLoyaltyPointsService implements TransferLoyaltyPointsUseCase {

    private final LoyaltyCardPort loadLoyaltyCardPort;
    private final LoyaltyCardActivitiesPort loyaltyCardActivitiesPort;


    @Transactional
    @Override
    public void transferLoyaltyPoints(TransferLoyaltyPointsCommand command) {
        log.info("Transfer Service: Transfer started");
        LoyaltyCard sourceCard = loadLoyaltyCardPort.loadLoyaltyCardWithoutActivities(
                command.sourceCardId());
        LoyaltyCard targetCard = loadLoyaltyCardPort.loadLoyaltyCardWithoutActivities(
                command.targetCardId());

        LoyaltyCardId sourceCardId = sourceCard.getId().orElseThrow(() -> new IllegalStateException("Source card ID is empty"));
        LoyaltyCardId targetCardId = targetCard.getId().orElseThrow(() -> new IllegalStateException("Target card ID is empty"));

        Points transferPoints = command.points();

        log.debug("Transfer Service: Withdrawing points from source loyalty card of id {}", command.sourceCardId());
        var withdrawalResult = sourceCard.transferOut(transferPoints, targetCardId);
        handleOperationResult(withdrawalResult);

        log.debug("Transfer Service: Depositing points to target loyalty card of id {}", command.targetCardId());
        var depositResult = targetCard.transferIn(transferPoints, sourceCardId);
        handleOperationResult(depositResult);

        loyaltyCardActivitiesPort.updateActivities(sourceCard);
        loyaltyCardActivitiesPort.updateActivities(targetCard);

        log.info("Transfer Service: Points of {} have been successfully transferred from loyalty card of id {}" +
                " to loyalty card of id {}", transferPoints, sourceCardId, targetCard);
    }

    private void handleOperationResult(OperationValidationResult operationValidationResult) {
        if (operationValidationResult.isNotSuccess()) {
            log.error("Transfer operation aborted. Cause: {}", operationValidationResult.getMessage());
            throw new OperationTerminatedWithFailureException(operationValidationResult.getMessage());
        }
    }
}
