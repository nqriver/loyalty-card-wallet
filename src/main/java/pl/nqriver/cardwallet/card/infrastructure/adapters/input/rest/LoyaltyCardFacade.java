package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.nqriver.cardwallet.card.application.ports.input.command.CreateCardUseCase;
import pl.nqriver.cardwallet.card.application.ports.input.command.ExtendExpirationDateUseCase;
import pl.nqriver.cardwallet.card.application.ports.input.query.GetCardBalanceQuery;
import pl.nqriver.cardwallet.card.application.ports.input.query.GetCardGeneralInfoQuery;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.mapper.LoyaltyCardRestMapper;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.request.CreateLoyaltyCardRequest;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.BalanceResponse;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.LoyaltyCardResponse;

import static pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId.of;

@RequiredArgsConstructor
@Component
class LoyaltyCardFacade {
    private final GetCardBalanceQuery getCardBalanceQuery;
    private final GetCardGeneralInfoQuery getCardGeneralInfoQuery;
    private final CreateCardUseCase createCardUseCase;
    private final ExtendExpirationDateUseCase extendExpirationDateUseCase;
    private final LoyaltyCardRestMapper mapper;

    LoyaltyCardResponse create(final CreateLoyaltyCardRequest request) {
        LoyaltyCard loyaltyCard = createCardUseCase.setUpNewLoyaltyCard(
                mapper.toCreateCardCommand(request)
        );
        return mapper.toLoyaltyCardResponse(loyaltyCard);
    }

    LoyaltyCardResponse getGeneralInfo(final Long loyaltyCardId) {
        return mapper.toLoyaltyCardResponse(getCardGeneralInfoQuery.getCardInfo(of(loyaltyCardId)));
    }

    BalanceResponse getBalance(final Long loyaltyCardId) {
        return mapper.toBalanceResponse(getCardBalanceQuery.getCardBalance(of(loyaltyCardId)), loyaltyCardId);
    }

    void extendExpirationDate(final Long id) {
        extendExpirationDateUseCase.extendExpirationDate(LoyaltyCard.LoyaltyCardId.of(id));
    }
}
