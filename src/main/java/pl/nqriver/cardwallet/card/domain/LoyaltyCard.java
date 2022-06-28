package pl.nqriver.cardwallet.card.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LoyaltyCard {

    private final LoyaltyCardId id;
    private final Points baselinePoints;
    private final ActivityWindow activityWindow;


    public boolean withdraw(Points points) {
        if (!mayBeWithdrawn(points)) {
            return false;
        }
        Activity withdrawal = new WithdrawalActivity(
                this.id,
                LocalDateTime.now(),
                points
        );
        activityWindow.addActivity(withdrawal);
        return true;
    }

    public boolean withdraw(Points points, LoyaltyCardId sourceCardId) {
        if (!mayBeWithdrawn(points)) {
            return false;
        }

        Activity withdrawal = new TransferOutActivity(
                this.id,
                this.id,
                sourceCardId,
                LocalDateTime.now(),
                points
        );
        activityWindow.addActivity(withdrawal);
        return true;
    }

    private boolean mayBeWithdrawn(Points points) {
        return this.calculateBalance().isGreaterThanOrEqualTo(points);
    }

    public Points calculateBalance() {
        return Points.add(activityWindow.calculateBalance(this.id), baselinePoints);
    }

    public boolean deposit(Points points) {
        Activity deposit = new DepositActivity(
                this.id,
                LocalDateTime.now(),
                points
        );
        this.activityWindow.addActivity(deposit);
        return true;
    }

    public boolean deposit(Points points, LoyaltyCardId sourceCardId) {
        Activity deposit = new TransferActivity(
                ActivityType.INCOMING,
                this.id,
                sourceCardId,
                this.id,
                LocalDateTime.now(),
                points
        );
        this.activityWindow.addActivity(deposit);
        return true;
    }

    @Value
    public static class LoyaltyCardId {
        Long value;
    }

}
