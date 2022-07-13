package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.repository;

import org.hibernate.Session;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.LoyaltyCardEntity;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * This test relays on pre-inserted test data which can be found under resources/db/test-data/insert-test-data.sql
 */
@AutoConfigureTestEntityManager
class LoyaltyCardRepositoryTest extends AbstractPersistenceIntegrationTest{

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @Transactional
    void shouldFetchLoyaltyCardByNaturalId() {
        String naturalIdEmail = "firstemail@email.com";

        LoyaltyCardEntity loadedLoyaltyCard = entityManager.getEntityManager().unwrap(Session.class)
                .bySimpleNaturalId(LoyaltyCardEntity.class)
                .load(naturalIdEmail);
        assertThat(loadedLoyaltyCard.getId()).isEqualTo(1L);
        assertThat(loadedLoyaltyCard.getHolderEmail()).isEqualTo(naturalIdEmail);
    }
}