package pl.nqriver.cardwallet.card.domain;

import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityWindow {

    private final List<Activity> activities;


    public Points calculateBalance() {
        Points incomeBalance = activities.stream()
                .filter(e -> ActivityType.INCOMING.equals(e.getTypeOfActivity()))
                .map(Activity::getPoints)
                .reduce(Points.ZERO, Points::add);

        Points outgoingBalance = activities.stream()
                .filter(e -> ActivityType.OUTGOING.equals(e.getTypeOfActivity()))
                .map(Activity::getPoints)
                .reduce(Points.ZERO, Points::add);

        return Points.add(incomeBalance, outgoingBalance.negate());

    }

    public LocalDateTime getTimestampOfFirstActivity() {
        return activities.stream()
                .min(Comparator.comparing(Activity::getTimestamp))
                .orElseThrow(IllegalStateException::new)
                .getTimestamp();
    }

    public LocalDateTime getTimestampOfLastActivity() {
        return activities.stream()
                .max(Comparator.comparing(Activity::getTimestamp))
                .orElseThrow(IllegalStateException::new)
                .getTimestamp();

    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    public ActivityWindow(List<Activity> activities) {
        this.activities = activities;
    }

    public static ActivityWindow of(List<Activity> activities) {
        return new ActivityWindow(activities);
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }
}
