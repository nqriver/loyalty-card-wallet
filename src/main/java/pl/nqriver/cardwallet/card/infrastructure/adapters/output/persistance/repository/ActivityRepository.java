package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.entity.ActivityEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {

    @Query(
            "select a from ActivityEntity a " +
                    "where a.ownerLoyaltyCardId = :ownerCardId " +
                    "and a.timestamp >= :since"
    )
    List<ActivityEntity> findByOwnerSince(
            @Param("ownerCardId") Long ownerCardId,
            @Param("since") LocalDateTime since
    );

    @Query(
            "select a from ActivityEntity a " +
                    "where a.ownerLoyaltyCardId = :ownerCardId"
    )
    List<ActivityEntity> findByOwner(
            @Param("ownerCardId") Long ownerCardId
    );


    @Query(
            "select sum(ae.points) from ActivityEntity ae " +
                    "where ae.ownerLoyaltyCardId = :loyaltyCardId " +
                    "and ae.isIncoming = true " +
                    "and ae.timestamp < :until"
    )
    Long getPointsDepositBalanceUntil(
            @Param("loyaltyCardId") Long loyaltyCardId,
            @Param("until") LocalDateTime until
    );

    @Query(
            "select sum(ae.points) from ActivityEntity ae " +
                    "where ae.ownerLoyaltyCardId = :loyaltyCardId " +
                    "and ae.isIncoming = false " +
                    "and ae.timestamp < :until"
    )
    Long getPointsWithdrawalBalanceUntil(
            @Param("loyaltyCardId") Long loyaltyCardId,
            @Param("until") LocalDateTime until
    );

    @Query(
            "select sum(ae.points) from ActivityEntity ae " +
                    "where ae.ownerLoyaltyCardId = :loyaltyCardId " +
                    "and ae.isIncoming = false "
    )
    Long getPointsWithdrawalBalance(
            @Param("loyaltyCardId") Long loyaltyCardId
    );

    @Query(
            "select sum(ae.points) from ActivityEntity ae " +
                    "where ae.ownerLoyaltyCardId = :loyaltyCardId " +
                    "and ae.isIncoming = true "
    )
    Long getPointsDepositBalance(
            @Param("loyaltyCardId") Long loyaltyCardId
    );
}
