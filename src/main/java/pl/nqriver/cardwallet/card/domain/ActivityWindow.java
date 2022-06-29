package pl.nqriver.cardwallet.card.domain;

import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

import java.util.Collections;
import java.util.List;

public class ActivityWindow {

    private List<Activity> activities;


    public Points calculateBalance(LoyaltyCardId id) {
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

    public void addActivity(Activity activity) {
        this.activities.add(activity);
    }

    public ActivityWindow(List<Activity> activities) {
        this.activities = activities;
    }

    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }
}
