package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "activities")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityEntity {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @Column(name = "owner_card_id")
    private Long ownerLoyaltyCardId;

    @Column(name = "source_card_id")
    private Long sourceLoyaltyCardId;

    @Column(name = "target_card_id")
    private Long targetLoyaltyCardId;

    @Column(name = "points")
    private Long points;

    @Column(name = "incoming")
    private boolean isIncoming;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityEntity that = (ActivityEntity) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, ownerLoyaltyCardId, sourceLoyaltyCardId, targetLoyaltyCardId);
    }
}

