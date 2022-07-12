package pl.nqriver.cardwallet.card.domain;

import java.util.function.Function;

/**
 * Validator for loyalty card operations. It allows to combine validation criteria
 */
public interface LoyaltyCardOperationValidator
        extends Function<LoyaltyCard, LoyaltyCardOperationValidator.OperationValidationResult> {

    static LoyaltyCardOperationValidator isCardNotExpired() {
        return loyaltyCard -> loyaltyCard.isValid() ?
                OperationValidationResult.SUCCESS : OperationValidationResult.CARD_EXPIRED;
    }

    static LoyaltyCardOperationValidator isAccountBalanceSufficient(Points withdrawalPoints) {
        return loyaltyCard -> loyaltyCard.canBeWithdrawn(withdrawalPoints)
                ? OperationValidationResult.SUCCESS : OperationValidationResult.INSUFFICIENT_ACCOUNT_BALANCE ;
    }

    default LoyaltyCardOperationValidator and(LoyaltyCardOperationValidator other) {
        return loyaltyCard -> {
            OperationValidationResult result = this.apply(loyaltyCard);
            return result.equals(OperationValidationResult.SUCCESS) ? other.apply(loyaltyCard) : result;
        };
    }

    enum OperationValidationResult {
        SUCCESS("The operation was successful"),
        CARD_EXPIRED("Operation terminated with failure. The validity of card has expired"),
        INSUFFICIENT_ACCOUNT_BALANCE("Operation terminated with failure. Insufficient amount of points");

        private final String message;

        OperationValidationResult(final String message) {
            this.message = message;
        }


        public boolean otherThan(OperationValidationResult result) {
            return !this.equals(result);
        }

        public boolean isNotSuccess() {
            return this.otherThan(SUCCESS);
        }

        public String getMessage() {
            return this.message;
        }
    }
}
