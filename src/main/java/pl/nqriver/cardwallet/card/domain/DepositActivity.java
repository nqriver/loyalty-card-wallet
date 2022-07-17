package pl.nqriver.cardwallet.card.domain;

import lombok.NonNull;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

import java.time.LocalDateTime;

/**
 * Deposit activity from outer source. Meets the condition of equality between owner card id and target card id.
 * Source card id is set to null.
 */

public final class DepositActivity extends Activity {


    public DepositActivity(
            ActivityId id,
            @NonNull LocalDateTime timestamp,
            @NonNull LoyaltyCardId ownerCardId,
            @NonNull Points points) {

        super(id, timestamp, ownerCardId, ownerCardId, null, points);
    }

    static DepositActivity newDeposit(LocalDateTime timestamp,
                                      LoyaltyCardId ownerCardId,
                                      Points points) {
        return new DepositActivity(null, timestamp, ownerCardId, points);
    }

    public static DepositActivity of(ActivityId id,
                                     LocalDateTime timestamp,
                                     LoyaltyCardId ownerCardId,
                                     Points points) {
        return new DepositActivity(id, timestamp, ownerCardId, points);
    }

    @Override
    public ActivityType getTypeOfActivity() {
        return ActivityType.INCOMING;
    }
}
