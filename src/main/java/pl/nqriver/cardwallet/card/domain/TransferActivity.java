package pl.nqriver.cardwallet.card.domain;

import lombok.NonNull;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

import java.time.LocalDateTime;

/**
 * Transfer activity. Owner card id could be either source card id or target card id depending on the transfer flow.
 *
 **/

public final class TransferActivity extends Activity {

    public TransferActivity(
            ActivityId id,
            @NonNull LocalDateTime timestamp,
            @NonNull Points points,
            @NonNull LoyaltyCardId ownerCardId,
            @NonNull LoyaltyCardId targetCardId,
            @NonNull LoyaltyCardId sourceCardId) {
        super(id, timestamp, ownerCardId, targetCardId, sourceCardId, points);
    }

    static TransferActivity newTransferOut(LocalDateTime timestamp,
                                           Points points,
                                           LoyaltyCardId targetCardId,
                                           LoyaltyCardId sourceCardId) {
        return new TransferActivity(null, timestamp, points, sourceCardId, targetCardId, sourceCardId);
    }

    static TransferActivity newTransferIn(LocalDateTime timestamp,
                                          Points points,
                                          LoyaltyCardId targetCardId,
                                          LoyaltyCardId sourceCardId) {
        return new TransferActivity(null, timestamp, points, targetCardId, targetCardId, sourceCardId);
    }

    public static TransferActivity of(ActivityId id,
                                      LocalDateTime timestamp,
                                      Points points,
                                      LoyaltyCardId ownerCardId,
                                      LoyaltyCardId targetCardId,
                                      LoyaltyCardId sourceCardId) {
        return new TransferActivity(id, timestamp, points, ownerCardId, targetCardId, sourceCardId);
    }

    @Override
    public ActivityType getTypeOfActivity() {
        return !this.getTargetCardId().equals(this.getOwnerCardId()) && this.getSourceCardId().equals(this.getOwnerCardId()) ?
                ActivityType.OUTGOING : ActivityType.INCOMING;
    }
}