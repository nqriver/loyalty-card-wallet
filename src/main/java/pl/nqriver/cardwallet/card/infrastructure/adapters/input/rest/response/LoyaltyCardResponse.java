package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoyaltyCardResponse {
    private Long id;
    private String holderEmail;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt;
}
