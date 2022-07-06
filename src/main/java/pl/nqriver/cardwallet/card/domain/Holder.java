package pl.nqriver.cardwallet.card.domain;

import lombok.Value;

@Value
public class Holder {
    private final String email;

    public static Holder of(String email) {
        return new Holder(email);
    }
}
