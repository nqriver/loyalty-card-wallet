package pl.nqriver.cardwallet.card.domain;

import lombok.Builder;
import lombok.NonNull;

import java.time.LocalDateTime;

public class TransferActivity extends Activity {

    @Builder
    public TransferActivity(ActivityId id,
                            @NonNull LocalDateTime timestamp,
                            LoyaltyCard.@NonNull LoyaltyCardId ownerCardId,
                            @NonNull Points points, LoyaltyCard.LoyaltyCardId targetCardId, LoyaltyCard.LoyaltyCardId sourceCardId) {
        super(id, timestamp, ownerCardId, targetCardId, sourceCardId, points);
    }

    @Override
    public ActivityType getTypeOfActivity() {
        return !this.getTargetCardId().equals(this.getOwnerCardId()) && this.getSourceCardId().equals(this.getOwnerCardId()) ?
                ActivityType.OUTGOING : ActivityType.INCOMING;
    }
}