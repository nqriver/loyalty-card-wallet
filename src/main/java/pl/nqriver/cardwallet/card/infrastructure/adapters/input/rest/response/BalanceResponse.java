package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class BalanceResponse {
    private Long loyaltyCardId;
    private Long totalBalance;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long depositBalance;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long withdrawalBalance;
}
