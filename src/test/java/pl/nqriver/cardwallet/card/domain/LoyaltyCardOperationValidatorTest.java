package pl.nqriver.cardwallet.card.domain;

import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static pl.nqriver.cardwallet.card.domain.LoyaltyCardOperationValidator.*;

class LoyaltyCardOperationValidatorTest {

    @Test
    void shouldFailValidationWhenCardValidityHasExpired() {
        // given
        LoyaltyCard card = givenLoyaltyCardOfId(LoyaltyCardId.of(1L));
        givenExpiredCard(card);
        givenCardWithSufficientBalance(card);

        // when
        OperationValidationResult result = isAccountBalanceSufficient(Points.of(1))
                .and(isCardNotExpired())
                .apply(card);

        //then
        assertThat(result).isEqualTo(OperationValidationResult.CARD_EXPIRED);
    }

    @Test
    void shouldFailValidationWhenCardBalanceIsNotSufficient() {

        // given
        LoyaltyCard card = givenLoyaltyCardOfId(LoyaltyCardId.of(1L));
        givenValidCard(card);
        givenCardWithInsufficientBalance(card);

        // when
        OperationValidationResult result = isAccountBalanceSufficient(Points.of(1))
                .and(isCardNotExpired())
                .apply(card);

        //then
        assertThat(result).isEqualTo(OperationValidationResult.INSUFFICIENT_ACCOUNT_BALANCE);
    }

    @Test
    void shouldSucceedWhenCardIsValidAndHasEnoughBalance() {

        // given
        LoyaltyCard card = givenLoyaltyCardOfId(LoyaltyCardId.of(1L));
        givenValidCard(card);
        givenCardWithSufficientBalance(card);

        // when
        OperationValidationResult result = isAccountBalanceSufficient(Points.of(1))
                .and(isCardNotExpired())
                .apply(card);

        //then
        assertThat(result).isEqualTo(OperationValidationResult.SUCCESS);
    }

    private void givenExpiredCard(LoyaltyCard loyaltyCard) {
        BDDMockito.given(loyaltyCard.isValid())
                .willReturn(false);
    }

    private void givenValidCard(LoyaltyCard loyaltyCard) {
        BDDMockito.given(loyaltyCard.isValid())
                .willReturn(true);
    }

    private void givenCardWithSufficientBalance(LoyaltyCard loyaltyCard) {
        BDDMockito.given(loyaltyCard.canBeWithdrawn(any(Points.class)))
                .willReturn(true);
    }

    private void givenCardWithInsufficientBalance(LoyaltyCard loyaltyCard) {
        BDDMockito.given(loyaltyCard.canBeWithdrawn(any(Points.class)))
                .willReturn(false);
    }

    private LoyaltyCard givenLoyaltyCardOfId(LoyaltyCardId cardId) {
        LoyaltyCard card = Mockito.mock(LoyaltyCard.class);
        BDDMockito.given(card.getId())
                .willReturn(Optional.of(cardId));
        return card;
    }
}