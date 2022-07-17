package pl.nqriver.cardwallet.card.domain;

import lombok.NonNull;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

import java.time.LocalDateTime;

/**
 * Withdrawal activity. Meets the condition of equality between owner card id and source card id.
 * Target card id is set to null
 */
public final class WithdrawalActivity extends Activity {

    public WithdrawalActivity(
            ActivityId id,
            @NonNull LocalDateTime timestamp,
            @NonNull LoyaltyCardId ownerCardId,
            @NonNull Points points) {
        super(id, timestamp, ownerCardId, null, ownerCardId, points);
    }


    static WithdrawalActivity newWithdrawal(LocalDateTime timestamp,
                                            LoyaltyCardId ownerCardId,
                                            Points points) {
        return new WithdrawalActivity(null, timestamp, ownerCardId, points);
    }

    public static WithdrawalActivity of(ActivityId id,
                                        LocalDateTime timestamp,
                                        LoyaltyCardId ownerCardId,
                                        Points points) {
        return new WithdrawalActivity(id, timestamp, ownerCardId, points);
    }


    @Override
    public ActivityType getTypeOfActivity() {
        return ActivityType.OUTGOING;
    }
}
