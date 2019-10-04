/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.bouncycastle.math.ec.ECPoint
 */
package org.bouncycastle.crypto.ec;

import org.bouncycastle.math.ec.ECPoint;

public class ECPair {
    private final ECPoint x;
    private final ECPoint y;

    public ECPair(ECPoint eCPoint, ECPoint eCPoint2) {
        this.x = eCPoint;
        this.y = eCPoint2;
    }

    public boolean equals(Object object) {
        if (object instanceof ECPair) {
            return this.equals((ECPair)object);
        }
        return false;
    }

    public boolean equals(ECPair eCPair) {
        return eCPair.getX().equals(this.getX()) && eCPair.getY().equals(this.getY());
    }

    public ECPoint getX() {
        return this.x;
    }

    public ECPoint getY() {
        return this.y;
    }

    public int hashCode() {
        return this.x.hashCode() + 37 * this.y.hashCode();
    }
}

