package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigInteger;

@Data
public class TransferRequest {

    @NotNull(message = "Id of source card of transaction cannot be null")
    private Long sourceCardId;

    @NotNull(message = "Id of target card of transaction cannot be null")
    private Long targetCardId;

    @Positive(message = "The amount of points to transfer must be positive")
    private BigInteger points;
}
