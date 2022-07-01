package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistance.entity;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "loyalty_cards")
@Getter
public class LoyaltyCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

}
