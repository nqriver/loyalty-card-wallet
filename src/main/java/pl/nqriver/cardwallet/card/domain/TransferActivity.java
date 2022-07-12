package pl.nqriver.cardwallet.card.domain;

import lombok.Builder;
import lombok.NonNull;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

import java.time.LocalDateTime;

/**
 * Transfer activity. Owner card id could be either source card id or target card id depending on the transfer flow.
 *
 **/

public class TransferActivity extends Activity {

    @Builder
    public TransferActivity(
                            ActivityId id,
                            @NonNull LocalDateTime timestamp,
                            @NonNull LoyaltyCardId ownerCardId,
                            @NonNull Points points,
                            @NonNull LoyaltyCardId targetCardId,
                            @NonNull LoyaltyCardId sourceCardId) {
        super(id, timestamp, ownerCardId, targetCardId, sourceCardId, points);
    }

    @Override
    public ActivityType getTypeOfActivity() {
        return !this.getTargetCardId().equals(this.getOwnerCardId()) && this.getSourceCardId().equals(this.getOwnerCardId()) ?
                ActivityType.OUTGOING : ActivityType.INCOMING;
    }
}