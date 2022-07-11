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
        return DepositActivity.builder()
                .id(ActivityId.of(activityEntity.getId()))
                .ownerCardId(LoyaltyCardId.of(activityEntity.getOwnerLoyaltyCardId()))
                .points(Points.of(activityEntity.getPoints()))
                .timestamp(activityEntity.getTimestamp())
                .build();
    }

    private TransferActivity getTransferActivityInstance(ActivityEntity activityEntity) {
        return TransferActivity.builder()
                .id(ActivityId.of(activityEntity.getId()))
                .ownerCardId(
                        LoyaltyCardId.of(activityEntity.getOwnerLoyaltyCardId()))
                .sourceCardId(
                        LoyaltyCardId.of(activityEntity.getSourceLoyaltyCardId()))
                .targetCardId(
                        LoyaltyCardId.of(activityEntity.getTargetLoyaltyCardId()))
                .timestamp(activityEntity.getTimestamp())
                .points(Points.of(activityEntity.getPoints()))
                .build();
    }

    private WithdrawalActivity getWithdrawalActivityInstance(ActivityEntity activityEntity) {
        return WithdrawalActivity.builder()
                .id(ActivityId.of(activityEntity.getId()))
                .ownerCardId(LoyaltyCardId.of(activityEntity.getOwnerLoyaltyCardId()))
                .points(Points.of(activityEntity.getPoints()))
                .timestamp(activityEntity.getTimestamp())
                .build();
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
