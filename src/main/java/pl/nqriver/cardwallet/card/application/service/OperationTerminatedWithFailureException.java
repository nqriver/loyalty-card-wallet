package pl.nqriver.cardwallet.card.application.service;

public class OperationTerminatedWithFailureException extends RuntimeException {
    public OperationTerminatedWithFailureException(String message) {
        super(message);
    }
}
