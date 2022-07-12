package pl.nqriver.cardwallet.card.application.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import pl.nqriver.cardwallet.card.application.ports.input.command.TransferLoyaltyPointsCommand;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardPort;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardActivitiesPort;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.domain.LoyaltyCardOperationValidator.OperationValidationResult;
import pl.nqriver.cardwallet.card.domain.Points;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.*;


class TransferLoyaltyPointsServiceTest {

    public static final Points POINTS = Points.of(1);

    private final LoyaltyCardPort loadLoyaltyCardPort = Mockito.mock(LoyaltyCardPort.class);

    private final LoyaltyCardActivitiesPort loyaltyCardActivitiesPort = Mockito.mock(LoyaltyCardActivitiesPort.class);

    private final TransferLoyaltyPointsService transferLoyaltyPointsService
            = new TransferLoyaltyPointsService(loadLoyaltyCardPort, loyaltyCardActivitiesPort);

    @Test
    void transferFails() {
        // given
        var firstCardId = givenLoyaltyCardIdOf(1L);
        var secondCardId = givenLoyaltyCardIdOf(2L);

        LoyaltyCard firstLoyaltyCard = givenLoyaltyCardOfId(firstCardId);
        LoyaltyCard secondLoyaltyCard = givenLoyaltyCardOfId(secondCardId);

        givenWithdrawalFail(firstLoyaltyCard);

        TransferLoyaltyPointsCommand command = new TransferLoyaltyPointsCommand(firstCardId, secondCardId, POINTS);

        // when
        assertThatCode(() -> transferLoyaltyPointsService.transferLoyaltyPoints(command))
                .isInstanceOf(OperationTerminatedWithFailureException.class);

        // then
        then(secondLoyaltyCard).should(times(0)).transferIn(any(), eq(firstCardId));

    }

    @Test
    void transferSucceeds() {
        // given
        var firstCardId = givenLoyaltyCardIdOf(1L);
        var secondCardId = givenLoyaltyCardIdOf(2L);

        LoyaltyCard firstLoyaltyCard = givenLoyaltyCardOfId(firstCardId);
        LoyaltyCard secondLoyaltyCard = givenLoyaltyCardOfId(secondCardId);

        givenWithdrawalSuccess(firstLoyaltyCard);
        givenDepositSuccess(secondLoyaltyCard);

        TransferLoyaltyPointsCommand command = new TransferLoyaltyPointsCommand(firstCardId, secondCardId, POINTS);

        // when
        assertThatCode(() -> transferLoyaltyPointsService.transferLoyaltyPoints(command))
                .doesNotThrowAnyException();

        // then
        then(secondLoyaltyCard).should().transferIn(eq(POINTS), eq(firstCardId));
        then(firstLoyaltyCard).should().transferOut(eq(POINTS), eq(secondCardId));
        thenExpectCardActivitiesBeingUpdated(firstCardId, secondCardId);

    }

    private void givenWithdrawalFail(LoyaltyCard loyaltyCard) {
        BDDMockito.given(loyaltyCard.transferOut(any(Points.class), any(LoyaltyCardId.class)))
                .willReturn(OperationValidationResult.CARD_EXPIRED);
    }

    private void givenWithdrawalSuccess(LoyaltyCard loyaltyCard) {
        BDDMockito.given(loyaltyCard.transferOut(any(Points.class), any(LoyaltyCardId.class)))
                .willReturn(OperationValidationResult.SUCCESS);
    }

    private void givenDepositSuccess(LoyaltyCard loyaltyCard) {
        BDDMockito.given(loyaltyCard.transferIn(any(Points.class), any(LoyaltyCardId.class)))
                .willReturn(OperationValidationResult.SUCCESS);
    }

    private void givenDepositFail(LoyaltyCard loyaltyCard) {
        BDDMockito.given(loyaltyCard.transferIn(any(Points.class), any(LoyaltyCardId.class)))
                .willReturn(OperationValidationResult.CARD_EXPIRED);
    }

    private LoyaltyCard givenLoyaltyCardOfId(LoyaltyCardId cardId) {
        LoyaltyCard card = Mockito.mock(LoyaltyCard.class);

        BDDMockito.given(card.getId())
                .willReturn(Optional.of(cardId));

        BDDMockito.given(loadLoyaltyCardPort.loadLoyaltyCardWithoutActivities(eq(cardId)))
                .willReturn(card);
        BDDMockito.given(loadLoyaltyCardPort.loadLoyaltyCardWithAllActivities(eq(cardId)))
                .willReturn(card);

        BDDMockito.given(loadLoyaltyCardPort.loadLoyaltyCardWithActivitiesOfPeriod(eq(cardId), any(), any()))
                .willReturn(card);

        return card;
    }

    private void thenExpectCardActivitiesBeingUpdated(LoyaltyCardId... loyaltyCardIds) {
        ArgumentCaptor<LoyaltyCard> cardArgumentCaptor = ArgumentCaptor.forClass(LoyaltyCard.class);
        then(loyaltyCardActivitiesPort).should(times(loyaltyCardIds.length))
                .updateActivities(cardArgumentCaptor.capture());

        List<LoyaltyCardId> updatedCardsIds = cardArgumentCaptor.getAllValues()
                .stream()
                .map(LoyaltyCard::getId)
                .map(Optional::get)
                .toList();

        assertThat(updatedCardsIds).containsAll(
                asList(loyaltyCardIds));
    }

    private LoyaltyCardId givenLoyaltyCardIdOf(long id) {
        return new LoyaltyCardId(id);
    }
}