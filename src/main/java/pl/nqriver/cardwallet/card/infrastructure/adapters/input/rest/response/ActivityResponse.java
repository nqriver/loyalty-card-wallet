package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Data
@Builder
public class ActivityResponse {

    private Long id;

    private final LocalDateTime timestamp;

    private final Long ownerCardId;

    private final Long targetCardId;

    private final Long sourceCardId;

    private final BigInteger points;

    private final String type;
}
