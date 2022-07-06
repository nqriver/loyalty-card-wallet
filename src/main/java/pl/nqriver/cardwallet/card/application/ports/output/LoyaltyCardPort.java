package pl.nqriver.cardwallet.card.application.ports.output;

import pl.nqriver.cardwallet.card.application.ports.input.command.CreateCardCommand;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard;
import pl.nqriver.cardwallet.card.domain.LoyaltyCard.LoyaltyCardId;

import java.time.LocalDateTime;

public interface LoyaltyCardPort {

    LoyaltyCard loadLoyaltyCard(LoyaltyCardId id);

    LoyaltyCard loadLoyaltyCardGeneralInfo(LoyaltyCardId id);

    LoyaltyCard createLoyaltyCard(CreateCardCommand command, LocalDateTime createdAt, LocalDateTime expiresAt);

}
