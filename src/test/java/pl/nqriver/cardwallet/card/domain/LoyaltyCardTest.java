package pl.nqriver.cardwallet.card.domain;

import org.junit.jupiter.api.Test;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.domain.LoyaltyCardOperationValidator.OperationValidationResult;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LoyaltyCardTest {

    public static final Balance BASELINE_BALANCE = Balance.of(Points.of(0L), Points.of(20L));

    @Test
    void shouldCalculateBalance() {
        // given
        LoyaltyCardId idOfCardUnderTest = LoyaltyCardId.of(1L);
        ActivityWindow window = givenActivityWindowForOwnerCardOfId(idOfCardUnderTest);
        LoyaltyCard cardUnderTest = givenLoyaltyCardOfActivityWindow(idOfCardUnderTest, window);


        // when
        Balance balance = cardUnderTest.calculateBalance();
        Points totalBalance = cardUnderTest.calculateTotalBalance();

        // then
        assertThat(balance).isEqualTo(Balance.of(Points.of(10), Points.of(40)));
        assertThat(totalBalance).isEqualTo(Points.of(30));
    }


    @Test
    void shouldSucceedDeposit_whenCardIsValid() {
        // given
        LoyaltyCardId cardId = LoyaltyCardId.of(1L);
        LoyaltyCard loyaltyCard = givenDefaultLoyaltyCardOfId(cardId);

        // when

        OperationValidationResult depositResult = loyaltyCard.deposit(Points.of(120L));
        Points balance = loyaltyCard.calculateTotalBalance();

        // then
        Points expectedTotalBalanceAfterOperations = Points.of(140L);

        assertThat(depositResult).isEqualTo(OperationValidationResult.SUCCESS);
        assertThat(balance).isEqualTo(expectedTotalBalanceAfterOperations);
    }

    @Test
    void shouldSucceedWithdrawal_whenCardIsValidAndHaveSufficientBalance() {
        // given
        LoyaltyCardId cardId = LoyaltyCardId.of(1L);
        LoyaltyCard loyaltyCard = givenDefaultLoyaltyCardOfId(cardId);

        // when
        OperationValidationResult withdrawalResult = loyaltyCard.withdraw(Points.of(10L));
        Points balance = loyaltyCard.calculateTotalBalance();

        // then
        Points expectedTotalBalanceAfterOperations = Points.of(10L);

        assertThat(withdrawalResult).isEqualTo(OperationValidationResult.SUCCESS);
        assertThat(balance).isEqualTo(expectedTotalBalanceAfterOperations);
    }


    @Test
    void shouldSucceedTransferOut_whenCardIsValidAndHaveSufficientBalance() {
        // given
        LoyaltyCardId cardId = LoyaltyCardId.of(1L);
        LoyaltyCard loyaltyCard = givenDefaultLoyaltyCardOfId(cardId);

        // when
        OperationValidationResult transferOutResult = loyaltyCard.transferOut(Points.of(20L), LoyaltyCardId.of(2L));
        Points balance = loyaltyCard.calculateTotalBalance();

        // then
        Points expectedTotalBalanceAfterOperations = Points.of(0L);

        assertThat(transferOutResult).isEqualTo(OperationValidationResult.SUCCESS);
        assertThat(balance).isEqualTo(expectedTotalBalanceAfterOperations);
    }


    @Test
    void shouldSucceedTransferIn_whenCardIsValid() {
        // given
        LoyaltyCardId cardId = LoyaltyCardId.of(1L);
        LoyaltyCard loyaltyCard = givenDefaultLoyaltyCardOfId(cardId);

        // when
        OperationValidationResult transferOutResult = loyaltyCard.transferIn(Points.of(20L), LoyaltyCardId.of(2L));
        Points balance = loyaltyCard.calculateTotalBalance();

        // then
        Points expectedTotalBalanceAfterOperations = Points.of(40L);

        assertThat(transferOutResult).isEqualTo(OperationValidationResult.SUCCESS);
        assertThat(balance).isEqualTo(expectedTotalBalanceAfterOperations);
    }



    @Test
    void shouldFailTransferOut_whenCardBalanceIsInsufficient() {
        // given
        LoyaltyCardId firstCardId = LoyaltyCardId.of(1L);
        LoyaltyCardId secondCardId = LoyaltyCardId.of(2L);
        LoyaltyCard firstCard = givenDefaultLoyaltyCardOfId(firstCardId);

        final Points EXCEEDING_POINTS = BASELINE_BALANCE.getTotalBalance().plus(Points.of(1L));

        // when
        var withdrawalResult = firstCard.transferOut(EXCEEDING_POINTS, secondCardId);
        Balance points = firstCard.calculateBalance();

        // then
        assertThat(withdrawalResult).isEqualTo(OperationValidationResult.INSUFFICIENT_ACCOUNT_BALANCE);
        assertThat(points).isEqualTo(BASELINE_BALANCE);
    }

    @Test
    void shouldFailTransferOut_whenCardIsExpired() {
        // given
        LoyaltyCard cardUnderTest = givenExpiredLoyaltyCardOfId(LoyaltyCardId.of(1L));
        final Points transferPoints = BASELINE_BALANCE.getTotalBalance();
        LoyaltyCardId targetCardId = LoyaltyCardId.of(2L);

        // when
        var withdrawalResult = cardUnderTest.transferOut(transferPoints, targetCardId);
        Balance points = cardUnderTest.calculateBalance();

        // then
        assertThat(withdrawalResult).isEqualTo(OperationValidationResult.CARD_EXPIRED);
        assertThat(points).isEqualTo(BASELINE_BALANCE);
    }

    @Test
    void shouldFailWithdrawal_whenCardBalanceIsInsufficient() {
        // given
        LoyaltyCardId firstCardId = LoyaltyCardId.of(1L);
        LoyaltyCard card = givenDefaultLoyaltyCardOfId(firstCardId);
        Points exceedingPoints = BASELINE_BALANCE.getTotalBalance().plus(Points.of(20L));

        // when
        OperationValidationResult withdrawalSuccess = card.withdraw(exceedingPoints);
        Points points = card.calculateTotalBalance();

        // then
        assertThat(withdrawalSuccess).isNotEqualTo(OperationValidationResult.SUCCESS);
        assertThat(points).isEqualTo(BASELINE_BALANCE.getTotalBalance());

    }

    @Test
    void shouldFailWithdrawal_whenCardIsExpired() {
        // given
        LoyaltyCard cardUnderTest = givenExpiredLoyaltyCardOfId(LoyaltyCardId.of(1L));
        Points transferPoints = BASELINE_BALANCE.getTotalBalance();

        // when
        OperationValidationResult withdrawalSuccess = cardUnderTest.withdraw(transferPoints);
        Points points = cardUnderTest.calculateTotalBalance();

        // then
        assertThat(withdrawalSuccess).isEqualTo(OperationValidationResult.CARD_EXPIRED);
        assertThat(points).isEqualTo(BASELINE_BALANCE.getTotalBalance());
    }

    private LoyaltyCard givenDefaultLoyaltyCardOfId(LoyaltyCardId cardId) {
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

    private LoyaltyCard givenLoyaltyCardOfActivityWindow(LoyaltyCardId cardId, ActivityWindow window) {
        LoyaltyCard loyaltyCard = LoyaltyCard.withId(
                cardId,
                BASELINE_BALANCE,
                window,
                Holder.of("someholderemail@test.com"),
                LocalDateTime.now(),
                LocalDateTime.now().plusYears(2)
        );
        return loyaltyCard;
    }

    private LoyaltyCard givenExpiredLoyaltyCardOfId(LoyaltyCardId cardId) {
        LoyaltyCard loyaltyCard = LoyaltyCard.withId(
                cardId,
                BASELINE_BALANCE,
                ActivityWindow.emptyWindow(),
                Holder.of("someholderemail@test.com"),
                LocalDateTime.now().minusYears(3),
                LocalDateTime.now().minusNanos(1)
        );
        return loyaltyCard;
    }

    private ActivityWindow givenActivityWindowForOwnerCardOfId(LoyaltyCardId owner) {
        ActivityWindow window = ActivityWindow.of(
                List.of(
                        WithdrawalActivity.builder()
                                .id(Activity.ActivityId.of(1L))
                                .ownerCardId(owner)
                                .points(Points.of(10L))
                                .timestamp(LocalDateTime.now())
                                .build(),

                        TransferActivity.builder()
                                .id(Activity.ActivityId.of(2L))
                                .sourceCardId(LoyaltyCardId.of(2L))
                                .targetCardId(LoyaltyCardId.of(1L))
                                .ownerCardId(owner)
                                .points(Points.of(20L))
                                .timestamp(LocalDateTime.now())
                                .build())
        );
        return window;
    }
}