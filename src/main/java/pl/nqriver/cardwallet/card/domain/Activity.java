package pl.nqriver.cardwallet.card.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

import java.time.LocalDateTime;


/**
 * Activity owned by {@link LoyaltyCard} between source and target {@link LoyaltyCard}s
 * The id of owner card must be equal to either target or source card id.
 */
public abstract class Activity {

    @Getter
    private ActivityId id;

    @Getter
    @NonNull
    private final LocalDateTime timestamp;

    @Getter
    @NonNull
    private final LoyaltyCardId ownerCardId;

    @Getter
    private final LoyaltyCardId targetCardId;

    @Getter
    private final LoyaltyCardId sourceCardId;

    @Getter
    @NonNull
    private final Points points;

    public abstract ActivityType getTypeOfActivity();

    protected Activity(
                    ActivityId id,
                    @NonNull LocalDateTime timestamp,
                    @NonNull LoyaltyCardId ownerCardId,
                    LoyaltyCardId targetCardId,
                    LoyaltyCardId sourceCardId,
                    @NonNull Points points) {
        this.id = id;
        this.targetCardId = targetCardId;
        this.sourceCardId = sourceCardId;
        this.timestamp = timestamp;
        this.ownerCardId = ownerCardId;
        this.points = points;
    }

    @Value
    public static class ActivityId {
        private final Long value;

        public static ActivityId of(Long value) {
            return new ActivityId(value);
        }
    }
}
