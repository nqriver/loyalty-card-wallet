package pl.nqriver.cardwallet.card.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

class LoyaltyCardTest {

    public static final long INITIAL_POINTS = 20L;

    @Test
    void shouldCalculateBalanceWithSimpleWithdrawalAndDeposit() {

        LoyaltyCard.LoyaltyCardId cardId = new LoyaltyCard.LoyaltyCardId(1L);
        LoyaltyCard loyaltyCard = getLoyaltyCardOfId(cardId);

        loyaltyCard.withdraw(Points.of(20L));
        loyaltyCard.deposit(Points.of(120L));

        Points balance = loyaltyCard.calculateBalance();
        assertThat(balance).isEqualTo(Points.of(120L));
    }

    @Test
    void shouldCalculateBalanceWithTransfers() {
        LoyaltyCard.LoyaltyCardId firstCardId = new LoyaltyCard.LoyaltyCardId(1L);
        LoyaltyCard.LoyaltyCardId secondCardId = new LoyaltyCard.LoyaltyCardId(2L);

        LoyaltyCard firstCard = getLoyaltyCardOfId(firstCardId);
        LoyaltyCard secondCard = getLoyaltyCardOfId(secondCardId);

        firstCard.deposit(Points.of(12L), secondCardId);
        secondCard.withdraw(Points.of(12L), firstCardId);

        firstCard.withdraw(Points.of(2L), secondCardId);
        secondCard.deposit(Points.of(2L), firstCardId);

        Points firstCardBalance = firstCard.calculateBalance();
        Points secondCardBalance = secondCard.calculateBalance();

        assertThat(firstCardBalance).isEqualTo(Points.of(30));
        assertThat(secondCardBalance).isEqualTo(Points.of(10));

    }

    @Test
    void shouldFailWithdrawal_whenThereAreNotEnoughPoints() {
        LoyaltyCard.LoyaltyCardId firstCardId = new LoyaltyCard.LoyaltyCardId(1L);

        LoyaltyCard card = getLoyaltyCardOfId(firstCardId);
        long exceedingAmount = INITIAL_POINTS + 20;

        boolean withdrawalSuccess = card.withdraw(Points.of(exceedingAmount));
        Points points = card.calculateBalance();

        assertThat(withdrawalSuccess).isFalse();
        assertThat(points).isEqualTo(Points.of(INITIAL_POINTS));
    }

    private LoyaltyCard getLoyaltyCardOfId(LoyaltyCard.LoyaltyCardId cardId) {
        LoyaltyCard loyaltyCard = LoyaltyCard.builder()
                .id(cardId)
                .baselinePoints(Points.of(INITIAL_POINTS))
                .activityWindow(new ActivityWindow(new ArrayList<>()))
                .build();
        return loyaltyCard;
    }
}