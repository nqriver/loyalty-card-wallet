package pl.nqriver.cardwallet.card.domain;

import lombok.Value;

/**
 * Complex representation of loyalty card balance
 */

@Value
public class Balance {

    private final Points totalBalance;

    private final Points withdrawalBalance;

    private final Points depositBalance;

    /**
     * Static factory method to create balance instance
     * Calculates total balance from incoming and outgoing points and returns instance
     *
     * @param withdrawn total outgoing points
     * @param deposited total incoming points
     * @return
     */
    public static Balance of(final Points withdrawn, final Points deposited) {
        Points totalPoints = Points.subtract(deposited, withdrawn);
        return new Balance(totalPoints, withdrawn, deposited);
    }


    /**
     * Combines this Balance instance with given balance by summing up incoming and outgoing balances
     *
     * @param other balance instance to combine with this instance.
     * @return new instance of combined balance
     */
    public Balance combine(Balance other) {
        Points combinedWithdrawalPoints = Points.add(this.withdrawalBalance, other.getWithdrawalBalance());
        Points combinedDepositPoints = Points.add(this.depositBalance, other.getDepositBalance());
        return Balance.of(combinedWithdrawalPoints, combinedDepositPoints);
    }
}
