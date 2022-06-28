package pl.nqriver.cardwallet.card.domain;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;

public class WithdrawalActivity extends Activity {

    @Builder
    protected WithdrawalActivity(
                                 LoyaltyCard.@NonNull LoyaltyCardId sourceCardId,
                                 @NonNull LocalDateTime timestamp,
                                 @NonNull Points points) {
        super(ActivityType.OUTGOING,
                sourceCardId,
                sourceCardId,
                null,
                timestamp, points
        );
    }
}
