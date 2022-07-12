package pl.nqriver.cardwallet.card.domain;

import lombok.Builder;
import lombok.NonNull;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

import java.time.LocalDateTime;

/**
 * Deposit activity from outer source. Meets the condition of equality between owner card id and target card id.
 * Source card id is set to null.
 */

public class DepositActivity extends Activity {


    @Builder
    public DepositActivity(
            ActivityId id,
            @NonNull LocalDateTime timestamp,
            @NonNull LoyaltyCardId ownerCardId,
            @NonNull Points points) {

        super(id, timestamp, ownerCardId, ownerCardId, null, points);
    }

    @Override
    public ActivityType getTypeOfActivity() {
        return ActivityType.INCOMING;
    }
}
