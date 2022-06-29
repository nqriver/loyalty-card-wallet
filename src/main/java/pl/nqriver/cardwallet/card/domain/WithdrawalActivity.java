package pl.nqriver.cardwallet.card.domain;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;

public class WithdrawalActivity extends Activity {

    @Builder
    public WithdrawalActivity(@NonNull LocalDateTime timestamp,
                              LoyaltyCard.@NonNull LoyaltyCardId ownerCardId,
                              @NonNull Points points) {
        super(timestamp, ownerCardId, points);
    }

    @Override
    protected ActivityType getTypeOfActivity() {
        return ActivityType.OUTGOING;
    }
}
