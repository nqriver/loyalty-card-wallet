package pl.nqriver.cardwallet.card.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

public abstract class Activity {

    private ActivityId id;

    @Getter
    @NonNull
    private final LocalDateTime timestamp;

    @Getter
    @NonNull
    private final ActivityType activityType;

    @Getter
    @NonNull
    private final LoyaltyCard.LoyaltyCardId ownerCardId;

    @Getter
    @NonNull
    private final LoyaltyCard.LoyaltyCardId sourceCardId;

    @Getter
    @NonNull
    private final LoyaltyCard.LoyaltyCardId targetCardId;

    @Getter
    @NonNull
    private final Points points;

    protected Activity(
            @NonNull ActivityType activityType,
            @NonNull LoyaltyCard.LoyaltyCardId ownerCardId,
            @NonNull LoyaltyCard.LoyaltyCardId sourceCardId,
            @Nullable LoyaltyCard.LoyaltyCardId targetCardId,
            @NonNull LocalDateTime timestamp,
            @NonNull Points points) {
        this.id = null;
        this.activityType = activityType;
        this.ownerCardId = ownerCardId;
        this.sourceCardId = sourceCardId;
        this.targetCardId = targetCardId;
        this.timestamp = timestamp;
        this.points = points;
    }


    @Value
    public static class ActivityId {
        Long value;
    }
}
