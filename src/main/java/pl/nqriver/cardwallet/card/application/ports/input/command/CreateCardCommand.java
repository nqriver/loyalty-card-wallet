package pl.nqriver.cardwallet.card.application.ports.input.command;

import lombok.Builder;
import lombok.Value;
import pl.nqriver.cardwallet.card.domain.Points;

import java.time.LocalDateTime;

@Value
@Builder
public class CreateCardCommand {
    private final String holderEmail;
    private final Points baselinePoints;
}
