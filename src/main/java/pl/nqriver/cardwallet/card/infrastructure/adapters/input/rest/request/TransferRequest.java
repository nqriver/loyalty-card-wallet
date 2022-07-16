package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigInteger;


public record TransferRequest(
        @NotNull(message = "Id of source card of transaction cannot be null") Long sourceCardId,
        @NotNull(message = "Id of target card of transaction cannot be null") Long targetCardId,
        @Positive(message = "The amount of points to transfer must be positive") BigInteger points) {
}
