package pl.nqriver.cardwallet.card.domain;

import org.junit.jupiter.api.Test;
import pl.nqriver.cardwallet.card.domain.LoyaltyCardOperationValidator.OperationValidationResult;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LoyaltyCardTest {

    public static final Balance BASELINE_BALANCE = Balance.of(Points.of(0L), Points.of(20L));

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

        firstCard.transferIn(Points.of(12L), secondCardId);
        secondCard.transferOut(Points.of(12L), firstCardId);

        firstCard.transferOut(Points.of(2L), secondCardId);
        secondCard.transferIn(Points.of(2L), firstCardId);

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

        final Points EXCEEDING_POINTS = BASELINE_BALANCE.getTotalBalance().plus(Points.of(20L));

        var withdrawalResult = firstCard.transferOut(EXCEEDING_POINTS, secondCardId);

        Points points = firstCard.calculateBalance();

        assertThat(withdrawalResult).isNotEqualTo(OperationValidationResult.SUCCESS);
        assertThat(points).isEqualTo(BASELINE_BALANCE.getTotalBalance());
    }

    @Test
    void shouldFailWithdrawal_whenThereAreNotEnoughPoints() {
        LoyaltyCard.LoyaltyCardId firstCardId = givenLoyaltyCardIdOf(1L);
        LoyaltyCard card = givenLoyaltyCardOfId(firstCardId);
        Points exceedingPoints = BASELINE_BALANCE.getTotalBalance().plus(Points.of(20L));

        OperationValidationResult withdrawalSuccess = card.withdraw(exceedingPoints);
        Points points = card.calculateBalance();

        assertThat(withdrawalSuccess).isNotEqualTo(OperationValidationResult.SUCCESS);
        assertThat(points).isEqualTo(BASELINE_BALANCE.getTotalBalance());

    }


    private LoyaltyCard.LoyaltyCardId givenLoyaltyCardIdOf(long l) {
        return new LoyaltyCard.LoyaltyCardId(l);
    }

    private LoyaltyCard givenLoyaltyCardOfId(LoyaltyCard.LoyaltyCardId cardId) {
        LoyaltyCard loyaltyCard = LoyaltyCard.withId(
                cardId,
                BASELINE_BALANCE,
                ActivityWindow.emptyWindow(),
                Holder.of("someholderemail@test.com"),
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(2)
        );
        return loyaltyCard;
    }
}