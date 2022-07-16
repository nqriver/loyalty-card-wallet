package pl.nqriver.cardwallet.card.domain;

import lombok.*;

/**
 * Complex representation of loyalty card balance
 */

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class Balance {

    private final Points totalBalance;

    private final Points withdrawalBalance;

    private final Points depositBalance;

    /**
     * Static factory method to create balance instance
     * Calculates total balance from incoming and outgoing points and returns instance
     * @param withdrawn total outgoing points
     * @param deposited total incoming points
     * @return
     */
    public static Balance of(final Points withdrawn, final Points deposited) {
        return Balance.builder()
                .totalBalance(Points.subtract(deposited, withdrawn))
                .withdrawalBalance(withdrawn)
                .depositBalance(deposited)
                .build();
    }

    public Balance combine(Balance balance) {
        Points combinedWithdrawalPoints = Points.add(this.withdrawalBalance, balance.getWithdrawalBalance());
        Points combinedDepositPoints = Points.add(this.depositBalance, balance.getDepositBalance());
        return Balance.of(combinedWithdrawalPoints, combinedDepositPoints);
    }
}
