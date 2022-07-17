package pl.nqriver.cardwallet.card.domain;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Optional;

import static pl.nqriver.cardwallet.card.domain.LoyaltyCardOperationValidator.*;

/**
 * A loyalty card that holds a certain amount of points and having properties such as
 * creation date, expiration date, {@link Holder}. The {@link LoyaltyCard} contains a window
 * of activity over a finite period of time and {@link Balance} instance which stores the balance of the
 * card excluding the loaded activity window.
 * The baseline balance in conjunction with activity window always provides a complete picture of the
 * current and valid state of the card. It means that the baseline balance summed with the activity window balance
 * always adds up to the current card balance regardless of the size of the activity window.
 */

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class LoyaltyCard {

    /**
     * Id of loyalty card
     */
    private final LoyaltyCardId id;

    /**
     * Baseline balance of card (Technically stores the total balance of the card minus the balance of the activity window)
     */
    private final Balance baselineBalance;

    /**
     * Window of this loyalty card activities of certain time period
     */
    private final ActivityWindow activityWindow;

    /**
     * Holder of this card
     */
    private final Holder holder;

    /**
     * This loyalty card creation date
     */
    @Getter
    private final LocalDateTime createdAt;

    /**
     * This loyalty card expiration date
     */
    @Getter
    private final LocalDateTime expiresAt;

    /**
     * Static factory method to create a {@link LoyaltyCard} entity with an id.
     */
    public static LoyaltyCard withId(LoyaltyCardId id,
                                     Balance baselineBalance,
                                     ActivityWindow window,
                                     Holder holder,
                                     LocalDateTime createdAt,
                                     LocalDateTime expiresAt
    ) {
        return new LoyaltyCard(id, baselineBalance, window, holder, createdAt, expiresAt);
    }

    /**
     * Static factory method to create a {@link LoyaltyCard} entity without an id. Used when entity is not yet persisted
     */
    public static LoyaltyCard withoutId(Balance baselineBalance,
                                        ActivityWindow window,
                                        Holder holder,
                                        LocalDateTime createdAt,
                                        LocalDateTime expiresAt) {
        return new LoyaltyCard(null, baselineBalance, window, holder, createdAt, expiresAt);
    }

    public Optional<LoyaltyCardId> getId() {
        return Optional.ofNullable(this.id);
    }


    /**
     * Calculates total balance of loyalty card. Returns a pure points value with no details about neither incomings or outgoings.
     *
     * @return overall balance of loyalty card
     */
    public Points calculateTotalBalance() {
        return this.calculateBalance().getTotalBalance();
    }


    /**
     * Provides meaningful balance details such as income balance, expense balance and total balance encapsulated in
     * one {@link Balance} instance
     *
     * @return instance of {@link Balance}
     */
    public Balance calculateBalance() {
        return this.baselineBalance.combine(activityWindow.calculateBalance());
    }

    /**
     * Tries to withdraw a certain amount of points from this loyalty card.
     * If operation succeeds, creates a new {@link WithdrawalActivity} and stores it in activity window.
     *
     * @return Enum type of {@link OperationValidationResult}.
     */
    public OperationValidationResult withdraw(Points points) {
        OperationValidationResult operationResult =
                isAccountBalanceSufficient(points)
                        .and(isCardNotExpired())
                        .apply(this);

        if (operationResult.otherThan(OperationValidationResult.SUCCESS)) {
            return operationResult;
        }
        Activity withdrawal = WithdrawalActivity.newWithdrawal(LocalDateTime.now(), this.id, points);
        activityWindow.addActivity(withdrawal);
        return operationResult;
    }


    /**
     * Tries to transfer out a certain amount of points from this loyalty card.
     * If operation succeeds, creates a new {@link TransferActivity} and stores it in activity window.
     * @param points amount of points to be transferred out
     * @param targetCardId id of target loyalty card
     * @return Enum type of {@link OperationValidationResult}
     */
    public OperationValidationResult transferOut(Points points, LoyaltyCardId targetCardId) {
        OperationValidationResult operationResult =
                isAccountBalanceSufficient(points)
                        .and(isCardNotExpired())
                        .apply(this);

        if (operationResult.otherThan(OperationValidationResult.SUCCESS)) {
            return operationResult;
        }

        Activity withdrawal = TransferActivity.newTransferOut(LocalDateTime.now(), points, targetCardId, this.id);
        activityWindow.addActivity(withdrawal);
        return operationResult;
    }

    /**
     * Tries to transfer out a certain amount of points from this loyalty card.
     * If operation succeeds, creates a new {@link DepositActivity} and stores it in activity window.
     * @param points amount of points to be deposited
     * @return Enum type of {@link OperationValidationResult}
     */
    public OperationValidationResult deposit(Points points) {
        OperationValidationResult operationResult = isCardNotExpired().apply(this);

        if (operationResult.otherThan(OperationValidationResult.SUCCESS)) {
            return operationResult;
        }

        Activity deposit = DepositActivity.newDeposit(LocalDateTime.now(), this.id, points);

        this.activityWindow.addActivity(deposit);
        return operationResult;
    }

    /**
     * Tries to transfer in a certain amount of points to this loyalty card.
     * If operation succeeds, creates a new {@link TransferActivity} and stores it in activity window.
     * @param points amount of points to be deposited
     * @param sourceCardId id of source card
     * @return Enum type of {@link OperationValidationResult}
     */
    public OperationValidationResult transferIn(Points points, LoyaltyCardId sourceCardId) {
        OperationValidationResult result =
                isCardNotExpired().apply(this);

        if (result.otherThan(OperationValidationResult.SUCCESS)) {
            return result;
        }

        Activity deposit = TransferActivity.newTransferIn(LocalDateTime.now(), points, this.id, sourceCardId);
        this.activityWindow.addActivity(deposit);
        return result;
    }

    /**
     * @return Window of stored activities of loyalty card
     */
    public ActivityWindow getActivityWindow() {
        return activityWindow;
    }


    /**
     * @return email of card holder
     */
    public String getHolderEmail() {
        return holder.getEmail();
    }

    boolean canBeWithdrawn(Points points) {
        return this.calculateTotalBalance().isGreaterThanOrEqualTo(points);
    }

    boolean isValid() {
        return this.expiresAt.isAfter(LocalDateTime.now());
    }

    @Value
    public static class LoyaltyCardId {
        private final Long value;
        public static @NonNull LoyaltyCardId of(Long value) {
            return new LoyaltyCardId(value);
        }
    }
}
