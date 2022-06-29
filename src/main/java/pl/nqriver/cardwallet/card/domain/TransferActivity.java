package pl.nqriver.cardwallet.card.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

public class TransferActivity extends Activity {

    @Getter
    @NonNull
    private final LoyaltyCard.LoyaltyCardId targetCardId;

    @Getter
    @NonNull
    private final LoyaltyCard.LoyaltyCardId sourceCardId;

    @Builder
    public TransferActivity(@NonNull LocalDateTime timestamp,
                            LoyaltyCard.@NonNull LoyaltyCardId ownerCardId,
                            @NonNull Points points, LoyaltyCard.LoyaltyCardId targetCardId, LoyaltyCard.LoyaltyCardId sourceCardId) {
        super(timestamp, ownerCardId, points);
        this.targetCardId = targetCardId;
        this.sourceCardId = sourceCardId;
    }

    @Override
    public ActivityType getTypeOfActivity() {
        return this.getSourceCardId()
                .equals(this.getOwnerCardId()) ? ActivityType.OUTGOING : ActivityType.INCOMING;
    }
}