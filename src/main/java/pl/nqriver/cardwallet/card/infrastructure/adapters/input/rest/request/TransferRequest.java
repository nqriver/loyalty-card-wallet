package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.request;

import lombok.Data;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.Points;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigInteger;

@Data
public class TransferRequest {

    @NotNull
    private final Long sourceCardId;

    @NotNull
    private final Long targetCardId;

    @Positive
    private final BigInteger points;
}
