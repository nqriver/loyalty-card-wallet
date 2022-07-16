package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response;

import java.math.BigInteger;
import java.time.LocalDateTime;


public record ActivityResponse(Long id,
                               LocalDateTime timestamp,
                               Long ownerCardId,
                               Long targetCardId,
                               Long sourceCardId,
                               BigInteger points,
                               String type) {

}
