package pl.nqriver.cardwallet.card.application.service;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * Configuration properties of card validity period for card creation use cases.
 */


@RequiredArgsConstructor
public class LoyaltyCardProperties {

    private final Long cardValidityPeriodInYears;

    public LocalDateTime calculateExpirationDate(LocalDateTime creationDate) {
        return creationDate.plusYears(this.cardValidityPeriodInYears);
    }
}
