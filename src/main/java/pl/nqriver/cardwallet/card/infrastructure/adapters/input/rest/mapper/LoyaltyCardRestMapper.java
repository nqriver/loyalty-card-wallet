package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import pl.nqriver.cardwallet.card.application.ports.input.CreateCardCommand;
import pl.nqriver.cardwallet.card.domain.Balance;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.Points;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.request.CreateLoyaltyCardRequest;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.BalanceDetailsResponse;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.response.LoyaltyCardResponse;

import java.math.BigInteger;

@Mapper(componentModel = "spring")
public interface LoyaltyCardRestMapper {

    @Mappings({
            @Mapping(source = "withdrawalBalance", target = "withdrawalBalance", qualifiedByName = "pointsToValue"),
            @Mapping(source = "overallBalance", target = "overallBalance", qualifiedByName = "pointsToValue"),
            @Mapping(source = "depositBalance", target = "depositBalance", qualifiedByName = "pointsToValue")
    })
    BalanceDetailsResponse toBalanceResponse(Balance balance);

    @Mappings({
            @Mapping(source = "baselinePoints", target = "baselinePoints", qualifiedByName = "toPoints")
    })
    CreateCardCommand toCreateCardCommand(CreateLoyaltyCardRequest request);

    @Named("toPoints")
    default Points bigIntegerToPoints(BigInteger points) {
        return Points.of(points);
    }

    @Named("pointsToValue")
    default BigInteger pointsToBigInteger(Points points) {
        return points.getAmount();
    }

    @Mappings({
            @Mapping(target = "id", expression = "java(loyaltyCard.getId().get().getValue())"),
            @Mapping(target = "holderEmail", expression = "java(loyaltyCard.getHolderEmail())"),
    })
    LoyaltyCardResponse toLoyaltyCardResponse(LoyaltyCard loyaltyCard);

}
