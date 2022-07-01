package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.entity.LoyaltyCardEntity;

@Repository
public interface LoyaltyCardRepository extends JpaRepository<LoyaltyCardEntity, Long> {
}
