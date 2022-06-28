package pl.nqriver.cardwallet.card.domain;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;

public class DepositActivity extends Activity {

    @Builder
    protected DepositActivity(
            LoyaltyCard.@NonNull LoyaltyCardId ownerCardId,
            @NonNull LocalDateTime timestamp,
            @NonNull Points points) {

        super(ActivityType.INCOMING,
                ownerCardId,
                ownerCardId,
                null,
                timestamp,
                points);
    }
}
