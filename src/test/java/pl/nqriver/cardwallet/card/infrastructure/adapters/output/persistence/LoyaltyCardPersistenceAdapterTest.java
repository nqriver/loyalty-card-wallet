package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nqriver.cardwallet.card.domain.*;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.ActivityEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.repository.ActivityRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class LoyaltyCardPersistenceAdapterTest extends AbstractPersistenceIntegrationTest {

    @Autowired
    private LoyaltyCardPersistenceAdapter adapter;

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    void shouldLoadLoyaltyCardWithAllActivities() {
        // given
        Long idOfCardUnderTest = 1L;

        Long expectedTotalIncomingBalance = 170L;
        Long expectedTotalOutgoingBalance = 30L;

        // when
        LoyaltyCard loyaltyCard = adapter.loadLoyaltyCardWithAllActivities(LoyaltyCardId.of(idOfCardUnderTest));


        // then
        assertThat(loyaltyCard.getId()).isEqualTo(Optional.of(LoyaltyCardId.of(idOfCardUnderTest)));
        assertThat(loyaltyCard.getActivityWindow().getActivities())
                .hasSize(5);

        assertThat(loyaltyCard.getBalance()).isEqualTo(
                Balance.of(Points.of(expectedTotalOutgoingBalance),
                        Points.of(expectedTotalIncomingBalance)));
    }

    @Test
    void shouldLoadLoyaltyCardWithActivitiesOfGivenPeriod() {
        // given
        Long idOfCardUnderTest = 1L;
        LocalDateTime since = LocalDateTime.now().minusYears(2);
        LocalDateTime until = LocalDateTime.now().plusYears(2);

        Long expectedTotalIncomingBalance = 170L;
        Long expectedTotalOutgoingBalance = 30L;


        // when
        LoyaltyCard loyaltyCard = adapter
                .loadLoyaltyCardWithActivitiesOfPeriod(LoyaltyCardId.of(idOfCardUnderTest), since, until);


        // then
        assertThat(loyaltyCard.getId()).isEqualTo(Optional.of(LoyaltyCardId.of(idOfCardUnderTest)));

        assertThat(loyaltyCard.getActivityWindow().calculateBalance()).isEqualTo(Points.of(120L));

        assertThat(loyaltyCard.getBalance()).isEqualTo(
                Balance.of(Points.of(expectedTotalOutgoingBalance),
                        Points.of(expectedTotalIncomingBalance)));

        assertThat(loyaltyCard.getActivityWindow().getActivities())
                .hasSize(2);
    }

    @Test
    void shouldLoadLoyaltyCardWithoutActivities() {
        // given
        Long idOfCardUnderTest = 1L;

        Long expectedTotalIncomingBalance = 170L;
        Long expectedTotalOutgoingBalance = 30L;

        // when
        LoyaltyCard loyaltyCard = adapter.loadLoyaltyCardWithoutActivities(LoyaltyCardId.of(idOfCardUnderTest));


        // then
        assertThat(loyaltyCard.getId()).isEqualTo(Optional.of(LoyaltyCardId.of(idOfCardUnderTest)));
        assertThat(loyaltyCard.getActivityWindow().getActivities())
                .isEmpty();

        assertThat(loyaltyCard.getBalance()).isEqualTo(
                Balance.of(Points.of(expectedTotalOutgoingBalance),
                        Points.of(expectedTotalIncomingBalance)));
    }

    @Test
    void shouldUpdateActivities() {

        // given
        Long idOfCardUnderTest = 1L;
        List<ActivityEntity> activitiesOfCardBeforeUpdate = activityRepository.findByOwner(idOfCardUnderTest);

        WithdrawalActivity newActivity = WithdrawalActivity.builder()
                .id(null)
                .timestamp(LocalDateTime.now())
                .points(Points.of(20L))
                .ownerCardId(LoyaltyCardId.of(idOfCardUnderTest))
                .build();

        LoyaltyCard loyaltyCardUnderTest = LoyaltyCard.withId(
                LoyaltyCardId.of(idOfCardUnderTest),
                Balance.of(Points.of(0L), Points.of(0L)),
                ActivityWindow.of(
                        List.of(newActivity)
                ),
                Holder.of(""),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        // when
        adapter.updateActivities(loyaltyCardUnderTest);

        // then


        List<ActivityEntity> activitiesOfCardAfterUpdate = activityRepository.findByOwner(idOfCardUnderTest);
        assertThat(activitiesOfCardAfterUpdate.size()).isEqualTo(activitiesOfCardBeforeUpdate.size() + 1);

        var addedActivity = new ArrayList<>(CollectionUtils.removeAll(activitiesOfCardAfterUpdate, activitiesOfCardBeforeUpdate))
                .get(0);

        assertThat(addedActivity.getPoints())
                .isEqualTo(newActivity.getPoints().getAmount().longValue());
        assertThat(addedActivity.getSourceLoyaltyCardId())
                .isEqualTo(newActivity.getSourceCardId().getValue().longValue());

    }

    @Test
    void createLoyaltyCard() {
    }

    @Test
    void updateExpirationDate() {
    }
}