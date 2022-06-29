package pl.nqriver.cardwallet.card.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;

public abstract class Activity {

    private ActivityId id;

    @Getter
    @NonNull
    private final LocalDateTime timestamp;

    @Getter
    @NonNull
    private final LoyaltyCard.LoyaltyCardId ownerCardId;

    @Getter
    @NonNull
    private final Points points;

    protected abstract ActivityType getTypeOfActivity();

    public Activity(@NonNull LocalDateTime timestamp,
                    @NonNull LoyaltyCard.LoyaltyCardId ownerCardId,
                    @NonNull Points points) {
        this.id = null;
        this.timestamp = timestamp;
        this.ownerCardId = ownerCardId;
        this.points = points;
    }

    @Value
    public static class ActivityId {
        Long value;
    }
}
