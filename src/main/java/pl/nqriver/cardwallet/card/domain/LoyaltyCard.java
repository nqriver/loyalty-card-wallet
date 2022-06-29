package pl.nqriver.cardwallet.card.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class LoyaltyCard {

    private final LoyaltyCardId id;
    private final Points baselinePoints;
    private final ActivityWindow activityWindow;

    /**
     * Static factory method to create a {@link LoyaltyCard} entity with an id.
     */
    public static LoyaltyCard withId(LoyaltyCardId id, Points baselinePoints, ActivityWindow window) {
        return new LoyaltyCard(id, baselinePoints, window);
    }

    /**
     * Static factory method to create a {@link LoyaltyCard} entity without an id. Used when entity is not yet persisted
     */
    public static LoyaltyCard withoutId(Points baselinePoints, ActivityWindow window) {
        return new LoyaltyCard(null, baselinePoints, window);
    }

    Optional<LoyaltyCardId> getId() {
        return Optional.ofNullable(this.id);
    }


    public Points calculateBalance() {
        return Points.add(activityWindow.calculateBalance(this.id), baselinePoints);
    }


    public boolean withdraw(Points points) {
        if (!mayBeWithdrawn(points)) {
            return false;
        }
        Activity withdrawal = WithdrawalActivity.builder()
                .timestamp(LocalDateTime.now())
                .ownerCardId(this.id)
                .points(points)
                .build();

        activityWindow.addActivity(withdrawal);
        return true;
    }

    public boolean withdraw(Points points, LoyaltyCardId targetCardId) {
        if (!mayBeWithdrawn(points)) {
            return false;
        }

        Activity withdrawal = TransferActivity.builder()
                .sourceCardId(this.id)
                .ownerCardId(this.id)
                .targetCardId(targetCardId)
                .timestamp(LocalDateTime.now())
                .points(points)
                .build();

        activityWindow.addActivity(withdrawal);
        return true;
    }

    public boolean deposit(Points points) {
        Activity deposit = DepositActivity.builder()
                .timestamp(LocalDateTime.now())
                .ownerCardId(this.id)
                .points(points)
                .build();

        this.activityWindow.addActivity(deposit);
        return true;
    }

    public boolean deposit(Points points, LoyaltyCardId sourceCardId) {
        Activity deposit = TransferActivity.builder()
                .sourceCardId(sourceCardId)
                .ownerCardId(this.id)
                .targetCardId(this.id)
                .timestamp(LocalDateTime.now())
                .points(points)
                .build();

        this.activityWindow.addActivity(deposit);
        return true;
    }

    private boolean mayBeWithdrawn(Points points) {
        return this.calculateBalance().isGreaterThanOrEqualTo(points);
    }

    @Value
    public static class LoyaltyCardId {
        Long value;
    }
}
