package pl.nqriver.cardwallet.card.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Balance {
    private final Points overallBalance;
    private final Points withdrawalBalance;
    private final Points depositBalance;

    public static Balance of(LoyaltyCard card) {
        return new Balance(
                card.getActivityWindow().calculateBalance(),
                card.getActivityWindow().calculateWithdrawalBalance(),
                card.getActivityWindow().calculateDepositBalance());
    }
}
