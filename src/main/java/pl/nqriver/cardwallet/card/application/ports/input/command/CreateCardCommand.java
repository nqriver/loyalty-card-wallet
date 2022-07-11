package pl.nqriver.cardwallet.card.application.ports.input.command;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateCardCommand {
    private final String holderEmail;
}
