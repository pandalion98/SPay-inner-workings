package org.bouncycastle.crypto.ec;

import org.bouncycastle.math.ec.ECPoint;

public class ECPair {
    private final ECPoint f143x;
    private final ECPoint f144y;

    public ECPair(ECPoint eCPoint, ECPoint eCPoint2) {
        this.f143x = eCPoint;
        this.f144y = eCPoint2;
    }

    public boolean equals(Object obj) {
        return obj instanceof ECPair ? equals((ECPair) obj) : false;
    }

    public boolean equals(ECPair eCPair) {
        return eCPair.getX().equals(getX()) && eCPair.getY().equals(getY());
    }

    public ECPoint getX() {
        return this.f143x;
    }

    public ECPoint getY() {
        return this.f144y;
    }

    public int hashCode() {
        return this.f143x.hashCode() + (this.f144y.hashCode() * 37);
    }
}
