package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;


public record BalanceResponse(Long loyaltyCardId, Long totalBalance,
                              @JsonInclude(JsonInclude.Include.NON_NULL) Long depositBalance,
                              @JsonInclude(JsonInclude.Include.NON_NULL) Long withdrawalBalance) {
}
