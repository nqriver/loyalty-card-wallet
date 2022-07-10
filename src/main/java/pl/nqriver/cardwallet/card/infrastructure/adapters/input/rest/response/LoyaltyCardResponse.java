package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoyaltyCardResponse {

    private final Long id;

    private final String holderEmail;

    private final LocalDateTime expiresAt;

    private final LocalDateTime createdAt;
}
