package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.mapper;

import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.domain.*;
import pl.nqriver.cardwallet.card.domain.Activity.ActivityId;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.ActivityEntity;

@Component
public class ActivityFactory {

    public Activity getInstance(ActivityEntity activityEntity) {
        return switch (getTypeOfTransactionForActivityEntity(activityEntity)) {
            case TRANSFER -> getTransferActivityInstance(activityEntity);
            case WITHDRAWAL -> getWithdrawalActivityInstance(activityEntity);
            case DEPOSIT -> getDepositActivityInstance(activityEntity);
        };
    }

    private DepositActivity getDepositActivityInstance(ActivityEntity activityEntity) {
        return DepositActivity.of(
                ActivityId.of(activityEntity.getId()),
                activityEntity.getTimestamp(),
                LoyaltyCardId.of(activityEntity.getOwnerLoyaltyCard().getId()),
                Points.of(activityEntity.getPoints())
        );
    }

    private TransferActivity getTransferActivityInstance(ActivityEntity activityEntity) {
        return TransferActivity.of(
                ActivityId.of(activityEntity.getId()),
                activityEntity.getTimestamp(),
                Points.of(activityEntity.getPoints()),
                LoyaltyCardId.of(activityEntity.getOwnerLoyaltyCard().getId()),
                LoyaltyCardId.of(activityEntity.getTargetLoyaltyCardId()),
                LoyaltyCardId.of(activityEntity.getSourceLoyaltyCardId())
        );
    }

    private WithdrawalActivity getWithdrawalActivityInstance(ActivityEntity activityEntity) {
        return WithdrawalActivity.of(
                ActivityId.of(activityEntity.getId()),
                activityEntity.getTimestamp(),
                LoyaltyCardId.of(activityEntity.getOwnerLoyaltyCard().getId()),
                Points.of(activityEntity.getPoints())
        );
    }

    private TransactionType getTypeOfTransactionForActivityEntity(ActivityEntity activityEntity) {
        if (activityEntity.getSourceLoyaltyCardId() == null) {
            return TransactionType.DEPOSIT;
        } else if (activityEntity.getTargetLoyaltyCardId() == null) {
            return TransactionType.WITHDRAWAL;
        }
        return TransactionType.TRANSFER;
    }

    private enum TransactionType {
        TRANSFER,
        WITHDRAWAL,
        DEPOSIT
    }
}
