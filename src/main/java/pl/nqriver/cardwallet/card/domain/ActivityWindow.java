package pl.nqriver.cardwallet.card.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ActivityWindow {

    private final List<Activity> activities;


    public Points calculateBalance() {
        Points incomeBalance = calculateDepositBalance();
        Points outgoingBalance = calculateWithdrawalBalance();
        return Points.add(incomeBalance, outgoingBalance.negate());
    }

    public Points calculateWithdrawalBalance() {
        return getBalanceOfActivityType(ActivityType.OUTGOING);
    }

    public Points calculateDepositBalance() {
        return getBalanceOfActivityType(ActivityType.INCOMING);
    }

    private Points getBalanceOfActivityType(ActivityType type) {
        return activities.stream()
                .filter(e -> type.equals(e.getTypeOfActivity()))
                .map(Activity::getPoints)
                .reduce(Points.ZERO, Points::add);
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
