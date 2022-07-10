package pl.nqriver.cardwallet.card.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class Balance {

    private final Points totalBalance;

    private final Points withdrawalBalance;

    private final Points depositBalance;

    public static Balance of(final Points withdrawn, final Points deposited) {
        return Balance.builder()
                .totalBalance(Points.subtract(deposited, withdrawn))
                .withdrawalBalance(withdrawn)
                .depositBalance(deposited)
                .build();
    }
}
