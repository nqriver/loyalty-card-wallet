package pl.nqriver.cardwallet.card.domain;

import lombok.Builder;
import lombok.NonNull;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

import java.time.LocalDateTime;

/**
 * Withdrawal activity. Meets the condition of equality between owner card id and source card id.
 * Target card id is set to null
 */
public class WithdrawalActivity extends Activity {

    @Builder
    public WithdrawalActivity(
            ActivityId id,
            @NonNull LocalDateTime timestamp,
            @NonNull LoyaltyCardId ownerCardId,
            @NonNull Points points) {
        super(id, timestamp, ownerCardId, null, ownerCardId, points);
    }

    @Override
    public ActivityType getTypeOfActivity() {
        return ActivityType.OUTGOING;
    }
}
