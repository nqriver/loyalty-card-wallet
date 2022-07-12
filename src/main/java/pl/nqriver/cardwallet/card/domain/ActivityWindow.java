package pl.nqriver.cardwallet.card.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A window of loyalty card activities.
 */
public class ActivityWindow {

    private final List<Activity> activities;


    /**
     * Calculates total balance of this activity window
     * @return total balance
     */
    public Points calculateBalance() {
        Points incomeBalance = calculateIncomingBalance();
        Points outgoingBalance = calculateOutgoingBalance();
        return Points.add(incomeBalance, outgoingBalance.negate());
    }

    /**
     * Calculates balance of all outgoing activities within this activity window
     * @return withdrawal balance
     */
    public Points calculateOutgoingBalance() {
        return getBalanceOfActivityType(ActivityType.OUTGOING);
    }


    /**
     * Calculates balance of all incoming activities within this activity window
     * @return deposit balance
     */
    public Points calculateIncomingBalance() {
        return getBalanceOfActivityType(ActivityType.INCOMING);
    }

    private Points getBalanceOfActivityType(ActivityType type) {
        return activities.stream()
                .filter(e -> type.equals(e.getTypeOfActivity()))
                .map(Activity::getPoints)
                .reduce(Points.ZERO, Points::add);
    }

    /**
     * Retrieves the timestamp of first activity within this activity window
     * @return
     */
    public LocalDateTime getTimestampOfFirstActivity() {
        return activities.stream()
                .min(Comparator.comparing(Activity::getTimestamp))
                .orElseThrow(IllegalStateException::new)
                .getTimestamp();
    }

    /**
     * Retrieves the timestamp of last activity within this activity window
     * @return
     */
    public LocalDateTime getTimestampOfLastActivity() {
        return activities.stream()
                .max(Comparator.comparing(Activity::getTimestamp))
                .orElseThrow(IllegalStateException::new)
                .getTimestamp();

    }

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    private ActivityWindow(List<Activity> activities) {
        this.activities = new ArrayList<>(activities);
    }

    public static ActivityWindow emptyWindow() {
        return new ActivityWindow(Collections.emptyList());
    }

    public static ActivityWindow of(List<Activity> activities) {
        return new ActivityWindow(activities);
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }
}
