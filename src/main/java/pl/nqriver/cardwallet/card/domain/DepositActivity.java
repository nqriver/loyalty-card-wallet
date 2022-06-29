package pl.nqriver.cardwallet.card.domain;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;

public class DepositActivity extends Activity {


    @Builder
    public DepositActivity(@NonNull LocalDateTime timestamp,
                           LoyaltyCard.@NonNull LoyaltyCardId ownerCardId,
                           @NonNull Points points) {
        super(timestamp, ownerCardId, points);
    }

    @Override
    protected ActivityType getTypeOfActivity() {
        return ActivityType.INCOMING;
    }
}
