package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.repository;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.AbstractPersistenceIntegrationTest;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.ActivityEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.LoyaltyCardEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test containers (Active profile is test)
 * This test relays on pre-inserted test data which can be found under resources/db/test-data/insert-test-data.sql
 */
class ActivityRepositoryTest extends AbstractPersistenceIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityRepositoryTest.class);

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private LoyaltyCardRepository loyaltyCardRepository;


    @Test
    void shouldFindActivitiesOfGivenPeriodByOwner() {
        // given
        LocalDateTime since = LocalDateTime.now().minusYears(2);
        LocalDateTime until = LocalDateTime.now().plusYears(6);

        Long idOfCardUnderTest = 1L;
        LoyaltyCardEntity cardUnderTest = loyaltyCardRepository.findById(idOfCardUnderTest).orElseThrow();

        // when
        List<ActivityEntity> retrievedActivities = activityRepository.findByOwnerBetweenDates(cardUnderTest, since, until);

        // then

        assertThat(retrievedActivities)
                .hasSize(5)
                .allSatisfy(e -> idOfCardUnderTest.equals(e.getOwnerLoyaltyCard().getId()))
                .allSatisfy(e -> e.getTimestamp().isBefore(until))
                .allSatisfy(e -> e.getTimestamp().isAfter(since));
    }

    @Test
    void shouldRetrieveSavedData() {
        activityRepository.findAll()
                .forEach(e -> LOGGER.debug("ActivityRepository: fetched entity {}", e.toString()));
        loyaltyCardRepository.findAll()
                .forEach(e -> LOGGER.debug("LoyaltyCardRepository: fetched entity {}", e.toString()));

        assertThat(activityRepository.findAll()).hasSize(10);
        assertThat(loyaltyCardRepository.findAll()).hasSize(6);
    }


    @Test
    void shouldRetrieveDepositBalanceOfLoyaltyCard() {
        // given
        Long idOfCardUnderTest = 1L;
        LoyaltyCardEntity cardUnderTest = loyaltyCardRepository.findById(idOfCardUnderTest).orElseThrow();

        // when
        Long pointsDepositBalance = activityRepository.getPointsDepositBalance(cardUnderTest);

        // then
        assertThat(pointsDepositBalance).isEqualTo(170L);
    }

    @Test
    void shouldRetrieveWithdrawalBalanceOfLoyaltyCard() {
        // given
        Long idOfCardUnderTest = 1L;
        LoyaltyCardEntity cardUnderTest = loyaltyCardRepository.findById(idOfCardUnderTest).orElseThrow();

        // when
        Long pointsDepositBalance = activityRepository.getPointsWithdrawalBalance(cardUnderTest);

        // then
        assertThat(pointsDepositBalance).isEqualTo(30L);
    }
}