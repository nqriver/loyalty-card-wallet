package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import pl.nqriver.cardwallet.card.application.ports.input.command.CreateCardCommand;
import pl.nqriver.cardwallet.card.application.ports.input.command.TransferLoyaltyPointsCommand;
import pl.nqriver.cardwallet.card.domain.Balance;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;
import pl.nqriver.cardwallet.card.domain.Points;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.request.CreateLoyaltyCardRequest;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.request.TransferRequest;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.BalanceResponse;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.LoyaltyCardResponse;

import java.math.BigInteger;

@Mapper(componentModel = "spring")
public interface LoyaltyCardRestMapper {

    @Mappings({
            @Mapping(source = "cardId", target = "loyaltyCardId"),
            @Mapping(source = "balance.withdrawalBalance", target = "withdrawalBalance", qualifiedByName = "pointsToValue"),
            @Mapping(source = "balance.totalBalance", target = "totalBalance", qualifiedByName = "pointsToValue"),
            @Mapping(source = "balance.depositBalance", target = "depositBalance", qualifiedByName = "pointsToValue")
    })
    BalanceResponse toBalanceResponse(Balance balance, Long cardId);


    @Mappings({
            @Mapping(target = "id", expression = "java(loyaltyCard.getId().get().getValue())"),
            @Mapping(target = "holderEmail", expression = "java(loyaltyCard.getHolderEmail())"),
    })
    LoyaltyCardResponse toLoyaltyCardResponse(LoyaltyCard loyaltyCard);

    @Mappings({
            @Mapping(source = "baselinePoints", target = "baselinePoints", qualifiedByName = "toPoints")
    })
    CreateCardCommand toCreateCardCommand(CreateLoyaltyCardRequest request);


    @Mappings({
        @Mapping(source = "sourceCardId", target = "sourceCardId", qualifiedByName = "longToCardId"),
        @Mapping(source = "targetCardId", target = "targetCardId", qualifiedByName = "longToCardId"),
        @Mapping(source = "points", target = "points", qualifiedByName = "longToPoints")
    })
    TransferLoyaltyPointsCommand toTransferCommand(TransferRequest request);

    @Named("toPoints")
    default Points bigIntegerToPoints(BigInteger points) {
        return Points.of(points);
    }

    @Named("longToPoints")
    default Points bigIntegerToPoints(Long points) {
        return Points.of(points);
    }

    @Named("pointsToValue")
    default BigInteger pointsToBigInteger(Points points) {
        return points.getAmount();
    }

    @Named("cardIdToLong")
    default Long cardIdToLong(LoyaltyCardId id) {
        return id.getValue();
    }

    @Named("longToCardId")
    default LoyaltyCardId longToCardId(Long id) {
        return LoyaltyCardId.of(id);
    }
}
