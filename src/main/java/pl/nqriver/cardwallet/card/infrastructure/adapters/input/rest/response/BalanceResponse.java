package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class BalanceResponse {
    private final Long loyaltyCardId;
    private final Long totalBalance;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long depositBalance;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Long withdrawalBalance;
}
