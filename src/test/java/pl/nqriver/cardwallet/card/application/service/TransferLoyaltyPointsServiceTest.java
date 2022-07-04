package pl.nqriver.cardwallet.card.application.service;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import pl.nqriver.cardwallet.card.application.ports.input.TransferLoyaltyPointsCommand;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardPort;
import pl.nqriver.cardwallet.card.application.ports.output.LoyaltyCardActivitiesPort;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
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
        var firstCardId = givenLoyaltyCardIdOf(1L);
        var secondCardId = givenLoyaltyCardIdOf(2L);

        LoyaltyCard firstLoyaltyCard = givenLoyaltyCardOfId(firstCardId);
        LoyaltyCard secondLoyaltyCard = givenLoyaltyCardOfId(secondCardId);

        givenWithdrawalFail(firstLoyaltyCard);

        TransferLoyaltyPointsCommand command = new TransferLoyaltyPointsCommand(firstCardId, secondCardId, POINTS);

        assertThatCode(() -> transferLoyaltyPointsService.transferLoyaltyPoints(command))
                .isInstanceOf(InsufficientCardBalanceException.class);

        then(secondLoyaltyCard).should(times(0)).deposit(any(), eq(firstCardId));

    }

    @Test
    void transferSucceeds() {
        var firstCardId = givenLoyaltyCardIdOf(1L);
        var secondCardId = givenLoyaltyCardIdOf(2L);

        LoyaltyCard firstLoyaltyCard = givenLoyaltyCardOfId(firstCardId);
        LoyaltyCard secondLoyaltyCard = givenLoyaltyCardOfId(secondCardId);

        givenWithdrawalSuccess(firstLoyaltyCard);

        TransferLoyaltyPointsCommand command = new TransferLoyaltyPointsCommand(firstCardId, secondCardId, POINTS);

        assertThatCode(() -> transferLoyaltyPointsService.transferLoyaltyPoints(command))
                .doesNotThrowAnyException();

        then(secondLoyaltyCard).should(times(1)).deposit(any(), eq(firstCardId));
        then(secondLoyaltyCard).should().deposit(eq(POINTS), eq(firstCardId));
        thenExpectCardActivitiesBeingUpdated(firstCardId, secondCardId);

    }

    private void givenWithdrawalFail(LoyaltyCard loyaltyCard) {
        BDDMockito.given(loyaltyCard.withdraw(any(Points.class), any(LoyaltyCardId.class)))
                .willReturn(false);
    }

    private void givenWithdrawalSuccess(LoyaltyCard loyaltyCard) {
        BDDMockito.given(loyaltyCard.withdraw(any(Points.class), any(LoyaltyCardId.class)))
                .willReturn(true);
    }

    private LoyaltyCard givenLoyaltyCardOfId(LoyaltyCardId cardId) {
        LoyaltyCard card = Mockito.mock(LoyaltyCard.class);

        BDDMockito.given(card.getId())
                .willReturn(Optional.of(cardId));

        BDDMockito.given(loadLoyaltyCardPort.loadLoyaltyCard(eq(cardId)))
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