package pl.nqriver.cardwallet.card.domain;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;

public class DepositActivity extends Activity {


    @Builder
    public DepositActivity(ActivityId id,
                           @NonNull LocalDateTime timestamp,
                           LoyaltyCard.@NonNull LoyaltyCardId ownerCardId,
                           @NonNull Points points) {
        super(id, timestamp, ownerCardId, ownerCardId, null, points);
    }

    @Override
    public ActivityType getTypeOfActivity() {
        return ActivityType.INCOMING;
    }
}
