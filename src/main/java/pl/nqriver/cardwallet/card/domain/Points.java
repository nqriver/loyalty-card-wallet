package pl.nqriver.cardwallet.card.domain;

import lombok.NonNull;
import lombok.Value;

import java.math.BigInteger;

@Value
public class Points {

    public static Points ZERO = Points.of(0L);

    @NonNull
    private final BigInteger amount;

    public Points(@NonNull BigInteger amount) {
        this.amount = amount;
    }

    public boolean isPositiveOrZero(){
        return this.amount.compareTo(BigInteger.ZERO) >= 0;
    }

    public boolean isNegative(){
        return this.amount.compareTo(BigInteger.ZERO) < 0;
    }

    public boolean isPositive(){
        return this.amount.compareTo(BigInteger.ZERO) > 0;
    }

    public boolean isGreaterThanOrEqualTo(Points points){
        return this.amount.compareTo(points.amount) >= 0;
    }

    public boolean isGreaterThan(Points points){
        return this.amount.compareTo(points.amount) >= 1;
    }

    public static Points of(long value) {
        return new Points(BigInteger.valueOf(value));
    }

    public static Points add(Points a, Points b) {
        return new Points(a.amount.add(b.amount));
    }

    public Points minus(Points points){
        return new Points(this.amount.subtract(points.amount));
    }

    public Points plus(Points points){
        return new Points(this.amount.add(points.amount));
    }

    public static Points subtract(Points a, Points b) {
        return new Points(a.amount.subtract(b.amount));
    }

    public Points negate(){
        return new Points(this.amount.negate());
    }
}
