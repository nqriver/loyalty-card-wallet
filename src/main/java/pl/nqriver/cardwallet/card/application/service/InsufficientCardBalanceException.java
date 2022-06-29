package pl.nqriver.cardwallet.card.application.service;

public class InsufficientCardBalanceException extends RuntimeException {
    public InsufficientCardBalanceException() {
        super("Restricted operation. Account balance is to low");
    }
}
