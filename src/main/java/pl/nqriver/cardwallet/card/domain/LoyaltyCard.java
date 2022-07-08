package pl.nqriver.cardwallet.card.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static pl.nqriver.cardwallet.card.domain.LoyaltyCardOperationValidator.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class LoyaltyCard {

    private final LoyaltyCardId id;

    private final Points baselinePoints;

    private final ActivityWindow activityWindow;

    private final Holder holder;

    @Getter
    private final LocalDateTime createdAt;

    @Getter
    private final LocalDateTime expiresAt;

    /**
     * Static factory method to create a {@link LoyaltyCard} entity with an id.
     */
    public static LoyaltyCard withId(LoyaltyCardId id,
                                     Points baselinePoints,
                                     ActivityWindow window,
                                     Holder holder,
                                     LocalDateTime createdAt,
                                     LocalDateTime expiresAt
    ) {
        return new LoyaltyCard(id, baselinePoints, window, holder, createdAt, expiresAt);
    }

    /**
     * Static factory method to create a {@link LoyaltyCard} entity without an id. Used when entity is not yet persisted
     */
    public static LoyaltyCard withoutId(Points baselinePoints,
                                        ActivityWindow window,
                                        Holder holder,
                                        LocalDateTime createdAt,
                                        LocalDateTime expiresAt) {
        return new LoyaltyCard(null, baselinePoints, window, holder, createdAt, expiresAt);
    }

    public Optional<LoyaltyCardId> getId() {
        return Optional.ofNullable(this.id);
    }


    public Points calculateBalance() {
        return Points.add(activityWindow.calculateBalance(), baselinePoints);
    }


    public boolean withdraw(Points points) {

        if (!canBeWithdrawn(points)) {
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

    public OperationValidationResult transferOut(Points points, LoyaltyCardId targetCardId) {
        OperationValidationResult operationResult =
                isAccountBalanceSufficient(points)
                        .and(isCardNotExpired())
                        .apply(this);

        if (operationResult.otherThan(OperationValidationResult.SUCCESS)) {
            return operationResult;
        }

        Activity withdrawal = TransferActivity.builder()
                .sourceCardId(this.id)
                .ownerCardId(this.id)
                .targetCardId(targetCardId)
                .timestamp(LocalDateTime.now())
                .points(points)
                .build();

        activityWindow.addActivity(withdrawal);
        return operationResult;
    }

    public OperationValidationResult deposit(Points points) {
        OperationValidationResult operationResult = isCardNotExpired().apply(this);

        if (operationResult.otherThan(OperationValidationResult.SUCCESS)) {
            return operationResult;
        }

        Activity deposit = DepositActivity.builder()
                .timestamp(LocalDateTime.now())
                .ownerCardId(this.id)
                .points(points)
                .build();

        this.activityWindow.addActivity(deposit);
        return operationResult;
    }

    public OperationValidationResult transferIn(Points points, LoyaltyCardId sourceCardId) {
        OperationValidationResult result =
                isCardNotExpired().apply(this);

        if (result.otherThan(OperationValidationResult.SUCCESS)) {
            return result;
        }

        Activity deposit = TransferActivity.builder()
                .sourceCardId(sourceCardId)
                .ownerCardId(this.id)
                .targetCardId(this.id)
                .timestamp(LocalDateTime.now())
                .points(points)
                .build();

        this.activityWindow.addActivity(deposit);
        return result;
    }

    public boolean canBeWithdrawn(Points points) {
        return this.calculateBalance().isGreaterThanOrEqualTo(points);
    }

    public boolean isValid() {
        return this.expiresAt.isAfter(LocalDateTime.now());
    }

    public ActivityWindow getActivityWindow() {
        return activityWindow;
    }


    public String getHolderEmail() {
        return holder.getEmail();
    }

    @Value
    public static class LoyaltyCardId {
        private final Long value;
        public static LoyaltyCardId of(Long value) {
            return new LoyaltyCardId(value);
        }
    }
}
