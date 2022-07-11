package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "loyalty_cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoyaltyCardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "holder_email")
    @NaturalId
    private String holderEmail;

    @Column(name = "expiration_time")
    private LocalDateTime expiresAt;

    @Column(name = "creation_time")
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoyaltyCardEntity that = (LoyaltyCardEntity) o;
        return Objects.equals(holderEmail, that.holderEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(holderEmail);
    }
}
