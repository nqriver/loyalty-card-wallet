package pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.nqriver.cardwallet.card.application.ports.input.command.TransferLoyaltyPointsCommand;
import pl.nqriver.cardwallet.card.application.ports.input.command.TransferLoyaltyPointsUseCase;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.mapper.LoyaltyCardRestMapper;
import pl.nqriver.cardwallet.card.infrastructure.adapters.input.rest.request.TransferRequest;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Validated
public class TransferPointsController {

    private final TransferLoyaltyPointsUseCase transferLoyaltyPointsUseCase;
    private final LoyaltyCardRestMapper restMapper;

    @PostMapping("/cards/send")
    ResponseEntity<?> transferPoints(@RequestBody @Valid TransferRequest transferRequest) {
        TransferLoyaltyPointsCommand command = restMapper.toTransferCommand(transferRequest);
        transferLoyaltyPointsUseCase.transferLoyaltyPoints(command);
        return ResponseEntity.noContent().build();
    }
}
