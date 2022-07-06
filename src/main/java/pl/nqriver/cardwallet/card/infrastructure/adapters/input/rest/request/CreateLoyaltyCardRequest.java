package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.request;

import lombok.Value;

import javax.validation.constraints.Email;
import java.math.BigInteger;

@Value
public class CreateLoyaltyCardRequest {

    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private final String email;

    private final BigInteger baselinePoints;
}
