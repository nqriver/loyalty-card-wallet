package pl.nqriver.cardwallet.card.domain;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;

public class TransferOutActivity extends Activity {

    @Builder
    protected TransferOutActivity(
                                  LoyaltyCard.@NonNull LoyaltyCardId ownerCardId,
                                  LoyaltyCard.@NonNull LoyaltyCardId sourceCardId,
                                  LoyaltyCard.@NonNull LoyaltyCardId targetCardId,
                                  @NonNull LocalDateTime timestamp,
                                  @NonNull Points points) {

        super(ActivityType.OUTGOING,
                ownerCardId,
                sourceCardId,
                targetCardId,
                timestamp, points);
    }
}
