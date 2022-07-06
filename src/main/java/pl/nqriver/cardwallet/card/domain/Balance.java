package pl.nqriver.cardwallet.card.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Getter
public class Balance {
    private final LoyaltyCardId loyaltyCardId;
    private final Points totalBalance;
    private final Points withdrawalBalance;
    private final Points depositBalance;

    public static Balance of(LoyaltyCard card) {
        return Balance.builder()
                .loyaltyCardId( card.getId().get())
                .totalBalance(card.calculateBalance())
                .depositBalance(card.getActivityWindow().calculateDepositBalance())
                .depositBalance(card.getActivityWindow().calculateWithdrawalBalance())
                .build();
    }

    public static Balance ofTotal(LoyaltyCard card) {
        return Balance.builder()
                .loyaltyCardId(card.getId().get())
                .totalBalance(card.calculateBalance())
                .build();
    }
}
