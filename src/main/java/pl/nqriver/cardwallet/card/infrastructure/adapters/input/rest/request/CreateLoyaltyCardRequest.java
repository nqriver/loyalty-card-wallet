package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.request;

import javax.validation.constraints.Email;

public record CreateLoyaltyCardRequest(
        @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$") String email) {
}
