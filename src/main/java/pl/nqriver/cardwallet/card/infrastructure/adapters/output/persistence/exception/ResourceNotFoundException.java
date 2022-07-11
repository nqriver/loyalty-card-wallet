package pl.nqriver.cardwallet.card.infrastructure.adapters.output.persistence.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
