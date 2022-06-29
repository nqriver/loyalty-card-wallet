package pl.nqriver.cardwallet.card.domain;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;

class LoyaltyCardTest {

    public static final Points INITIAL_POINTS = Points.of(20L);

    @Test
    void shouldCalculateBalanceWithSimpleWithdrawalAndDeposit() {

        LoyaltyCard.LoyaltyCardId cardId = givenLoyaltyCardIdOf(1L);
        LoyaltyCard loyaltyCard = givenLoyaltyCardOfId(cardId);

        loyaltyCard.withdraw(Points.of(20L));
        loyaltyCard.deposit(Points.of(120L));

        Points balance = loyaltyCard.calculateBalance();
        assertThat(balance).isEqualTo(Points.of(120L));
    }

    @Test
    void shouldCalculateBalanceWithTransfers() {
        LoyaltyCard.LoyaltyCardId firstCardId = givenLoyaltyCardIdOf(1L);
        LoyaltyCard.LoyaltyCardId secondCardId = givenLoyaltyCardIdOf(2L);

        LoyaltyCard firstCard = givenLoyaltyCardOfId(firstCardId);
        LoyaltyCard secondCard = givenLoyaltyCardOfId(secondCardId);

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
    void shouldFailTransferOut_whenThereAreNotEnoughPoints() {
        LoyaltyCard.LoyaltyCardId firstCardId = givenLoyaltyCardIdOf(1L);
        LoyaltyCard.LoyaltyCardId secondCardId = givenLoyaltyCardIdOf(2L);
        LoyaltyCard firstCard = givenLoyaltyCardOfId(firstCardId);

        final Points EXCEEDING_POINTS = INITIAL_POINTS.plus(Points.of(20L));

        boolean withdrawalSuccess = firstCard.withdraw(EXCEEDING_POINTS, secondCardId);

        Points points = firstCard.calculateBalance();

        assertThat(withdrawalSuccess).isFalse();
        assertThat(points).isEqualTo(INITIAL_POINTS);
    }

    @Test
    void shouldFailWithdrawal_whenThereAreNotEnoughPoints() {
        LoyaltyCard.LoyaltyCardId firstCardId = givenLoyaltyCardIdOf(1L);
        LoyaltyCard card = givenLoyaltyCardOfId(firstCardId);
        Points exceedingPoints = INITIAL_POINTS.plus(Points.of(20L));

        boolean withdrawalSuccess = card.withdraw(exceedingPoints);
        Points points = card.calculateBalance();

        assertThat(withdrawalSuccess).isFalse();
        assertThat(points).isEqualTo(INITIAL_POINTS);

    }


    private LoyaltyCard.LoyaltyCardId givenLoyaltyCardIdOf(long l) {
        return new LoyaltyCard.LoyaltyCardId(l);
    }

    private LoyaltyCard givenLoyaltyCardOfId(LoyaltyCard.LoyaltyCardId cardId) {
        LoyaltyCard loyaltyCard = LoyaltyCard.withId(
                cardId,
                INITIAL_POINTS,
                new ActivityWindow(new ArrayList<>())
        );
        return loyaltyCard;
    }
}