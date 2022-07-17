package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import pl.nqriver.cardwallet.card.application.ports.input.command.CreateCardCommand;
import pl.nqriver.cardwallet.card.domain.*;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.ActivityEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.LoyaltyCardEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.repository.ActivityRepository;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.repository.LoyaltyCardRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;


/**
 * This test relays on pre-inserted test data which can be found under resources/db/test-data/insert-test-data.sql
 */
class LoyaltyCardPersistenceAdapterTest extends AbstractPersistenceIntegrationTest {

    @Autowired
    private LoyaltyCardPersistenceAdapter adapter;

    @Autowired
    private ActivityRepository activityRepository;


    @Autowired
    private LoyaltyCardRepository loyaltyCardRepository;

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

        assertThat(loyaltyCard.calculateBalance()).isEqualTo(
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

        assertThat(loyaltyCard.getActivityWindow().calculateBalance()).isEqualTo(Balance.of(Points.of(0), Points.of(120L)));

        assertThat(loyaltyCard.calculateBalance()).isEqualTo(
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

        assertThat(loyaltyCard.calculateBalance()).isEqualTo(
                Balance.of(Points.of(expectedTotalOutgoingBalance),
                        Points.of(expectedTotalIncomingBalance)));
    }

    @Test
    void shouldUpdateActivities() {

        // given
        Long idOfCardUnderTest = 1L;
        List<ActivityEntity> activitiesOfCardBeforeUpdate = activityRepository.findByOwner(idOfCardUnderTest);

        WithdrawalActivity newActivity = WithdrawalActivity
                .of(null, LocalDateTime.now(), LoyaltyCardId.of(idOfCardUnderTest), Points.of(20L));

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
    void shouldCreateLoyaltyCard() {
        // given
        String newCardHolder = "email7@email.email";
        CreateCardCommand command = new CreateCardCommand(newCardHolder);
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = LocalDateTime.now().plusMonths(10);

        // when
        LoyaltyCard createdLoyaltyCard = adapter.createLoyaltyCard(command, createdAt, expiresAt);

        // then

        assertThat(createdLoyaltyCard).isNotNull();
        assertThat(createdLoyaltyCard.getId()).isPresent();
        assertThat(createdLoyaltyCard.getCreatedAt()).isEqualTo(createdAt);
        assertThat(createdLoyaltyCard.getExpiresAt()).isEqualTo(expiresAt);
        assertThat(createdLoyaltyCard.calculateBalance()).isEqualTo(Balance.of(Points.of(0L), Points.of(0L)));
        assertThat(createdLoyaltyCard.getActivityWindow().getActivities()).isEmpty();
    }

    @Test
    void shouldNotCreateLoyaltyCardWithTakenEmail() {
        // given
        String alreadyRegisteredEmail = "firstemail@email.com";
        CreateCardCommand command = new CreateCardCommand(alreadyRegisteredEmail);
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime expiresAt = LocalDateTime.now().plusMonths(10);

        // when
        assertThatCode(() -> adapter.createLoyaltyCard(command, createdAt, expiresAt)).isInstanceOf(
                DataIntegrityViolationException.class);
    }

    @Test
    void shouldUpdateExpirationDate() {
        // given
        Long idOfCardUnderTest = 1L;
        LocalDateTime initialExpirationDate = loyaltyCardRepository.findById(idOfCardUnderTest).get().getExpiresAt();
        LocalDateTime newExpirationDate = initialExpirationDate.plusYears(10);

        // when
        adapter.updateExpirationDate(LoyaltyCardId.of(idOfCardUnderTest), newExpirationDate);
        LoyaltyCardEntity updatedActivity = loyaltyCardRepository.findById(idOfCardUnderTest).get();

        // then
        assertThat(updatedActivity.getId()).isEqualTo(idOfCardUnderTest);
        assertThat(updatedActivity.getExpiresAt()).isEqualTo(newExpirationDate);
    }
}