package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response;

import lombok.Data;

@Data
public class BalanceDetailsResponse {
    Long depositBalance;
    Long withdrawalBalance;
    Long overallBalance;
}
