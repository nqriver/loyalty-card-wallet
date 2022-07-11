package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.LoyaltyCardEntity;

@Repository
public interface LoyaltyCardRepository extends JpaRepository<LoyaltyCardEntity, Long> {
}
