package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response;

import java.time.LocalDateTime;

public record LoyaltyCardResponse(Long id,
                                  String holderEmail,
                                  LocalDateTime expiresAt,
                                  LocalDateTime createdAt) {

}
