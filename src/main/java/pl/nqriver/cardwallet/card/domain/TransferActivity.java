package pl.nqriver.cardwallet.card.domain;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;

public class TransferActivity extends Activity {

    @Builder
    protected TransferActivity(
            @NonNull ActivityType activityType,
            LoyaltyCard.@NonNull LoyaltyCardId ownerCardId,
            LoyaltyCard.@NonNull LoyaltyCardId sourceCardId,
            LoyaltyCard.@NonNull LoyaltyCardId targetCardId,
            @NonNull LocalDateTime timestamp,
            @NonNull Points points) {

        super(activityType,
                ownerCardId,
                sourceCardId,
                targetCardId,
                timestamp,
                points);
    }
}
