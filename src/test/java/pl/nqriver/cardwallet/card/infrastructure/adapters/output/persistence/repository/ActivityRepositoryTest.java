package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;


class ActivityRepositoryTest extends AbstractPersistenceIntegrationTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private LoyaltyCardRepository loyaltyCardRepository;

    @Test
    void shouldRetrieveSavedData() {
        assertThat(activityRepository.findAll()).hasSize(4);
        assertThat(loyaltyCardRepository.findAll()).hasSize(6);
    }
}