package pl.nqriver.cardwallet.card.domain;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;

public class WithdrawalActivity extends Activity {

    @Builder
    public WithdrawalActivity(
            ActivityId id,
            @NonNull LocalDateTime timestamp,
            LoyaltyCard.@NonNull LoyaltyCardId ownerCardId,
            @NonNull Points points) {
        super(id, timestamp, ownerCardId, null, ownerCardId, points);
    }

    @Override
    public ActivityType getTypeOfActivity() {
        return ActivityType.OUTGOING;
    }
}
