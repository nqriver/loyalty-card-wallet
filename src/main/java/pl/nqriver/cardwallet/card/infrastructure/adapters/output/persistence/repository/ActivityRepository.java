package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.ActivityEntity;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity.LoyaltyCardEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {

    List<ActivityEntity> findByOwnerLoyaltyCardAndTimestampIsBetween(LoyaltyCardEntity ownerCard,
                                                                       LocalDateTime since,
                                                                       LocalDateTime until);

    List<ActivityEntity> findByOwnerLoyaltyCard(LoyaltyCardEntity ownerCardId);


    default List<ActivityEntity> findByOwnerBetweenDates(
            LoyaltyCardEntity ownerCard,
            LocalDateTime since,
            LocalDateTime until
    ) {
        return findByOwnerLoyaltyCardAndTimestampIsBetween(ownerCard, since, until);
    }


    default List<ActivityEntity> findAllByOwner(LoyaltyCardEntity ownerCardId) {
        return findByOwnerLoyaltyCard(ownerCardId);
    }


    @Query(
            "select sum(ae.points) from ActivityEntity ae " +
                    "where ae.ownerLoyaltyCard = :ownerCard " +
                    "and ae.isIncoming = false " +
                    "and ae.timestamp not between :since and :until"
    )
    Long getPointsWithdrawalBalanceExcludingPeriodOf(
            @Param("ownerCard") LoyaltyCardEntity ownerCard,
            @Param("since") LocalDateTime since,
            @Param("until") LocalDateTime until
    );



    @Query(
            "select sum(ae.points) from ActivityEntity ae " +
                    "where ae.ownerLoyaltyCard = :ownerCard " +
                    "and ae.isIncoming = true " +
                    "and ae.timestamp not between :since and :until"
    )
    Long getPointsDepositBalanceExcludingPeriodOf(
            @Param("ownerCard") LoyaltyCardEntity ownerCard,
            @Param("since") LocalDateTime since,
            @Param("until") LocalDateTime until
    );



    @Query(
            "select sum(ae.points) from ActivityEntity ae " +
                    "where ae.ownerLoyaltyCard = :ownerCard " +
                    "and ae.isIncoming = false "
    )
    Long getPointsWithdrawalBalance(
            @Param("ownerCard") LoyaltyCardEntity ownerCard
    );



    @Query(
            "select sum(ae.points) from ActivityEntity ae " +
                    "where ae.ownerLoyaltyCard = :ownerCard " +
                    "and ae.isIncoming = true "
    )
    Long getPointsDepositBalance(
            @Param("ownerCard") LoyaltyCardEntity ownerCard
    );
}
