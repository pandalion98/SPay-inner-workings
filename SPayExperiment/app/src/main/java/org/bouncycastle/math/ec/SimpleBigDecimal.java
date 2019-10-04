/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuffer
 *  java.math.BigInteger
 */
package org.bouncycastle.math.ec;

import java.math.BigInteger;
import org.bouncycastle.math.ec.ECConstants;

class SimpleBigDecimal {
    private static final long serialVersionUID = 1L;
    private final BigInteger bigInt;
    private final int scale;

    public SimpleBigDecimal(BigInteger bigInteger, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("scale may not be negative");
        }
        this.bigInt = bigInteger;
        this.scale = n;
    }

    private void checkScale(SimpleBigDecimal simpleBigDecimal) {
        if (this.scale != simpleBigDecimal.scale) {
            throw new IllegalArgumentException("Only SimpleBigDecimal of same scale allowed in arithmetic operations");
        }
    }

    public static SimpleBigDecimal getInstance(BigInteger bigInteger, int n) {
        return new SimpleBigDecimal(bigInteger.shiftLeft(n), n);
    }

    public SimpleBigDecimal add(BigInteger bigInteger) {
        return new SimpleBigDecimal(this.bigInt.add(bigInteger.shiftLeft(this.scale)), this.scale);
    }

    public SimpleBigDecimal add(SimpleBigDecimal simpleBigDecimal) {
        this.checkScale(simpleBigDecimal);
        return new SimpleBigDecimal(this.bigInt.add(simpleBigDecimal.bigInt), this.scale);
    }

    public SimpleBigDecimal adjustScale(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("scale may not be negative");
        }
        if (n == this.scale) {
            return this;
        }
        return new SimpleBigDecimal(this.bigInt.shiftLeft(n - this.scale), n);
    }

    public int compareTo(BigInteger bigInteger) {
        return this.bigInt.compareTo(bigInteger.shiftLeft(this.scale));
    }

    public int compareTo(SimpleBigDecimal simpleBigDecimal) {
        this.checkScale(simpleBigDecimal);
        return this.bigInt.compareTo(simpleBigDecimal.bigInt);
    }

    public SimpleBigDecimal divide(BigInteger bigInteger) {
        return new SimpleBigDecimal(this.bigInt.divide(bigInteger), this.scale);
    }

    public SimpleBigDecimal divide(SimpleBigDecimal simpleBigDecimal) {
        this.checkScale(simpleBigDecimal);
        return new SimpleBigDecimal(this.bigInt.shiftLeft(this.scale).divide(simpleBigDecimal.bigInt), this.scale);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        block5 : {
            block4 : {
                if (this == object) break block4;
                if (!(object instanceof SimpleBigDecimal)) {
                    return false;
                }
                SimpleBigDecimal simpleBigDecimal = (SimpleBigDecimal)object;
                if (!this.bigInt.equals((Object)simpleBigDecimal.bigInt) || this.scale != simpleBigDecimal.scale) break block5;
            }
            return true;
        }
        return false;
    }

    public BigInteger floor() {
        return this.bigInt.shiftRight(this.scale);
    }

    public int getScale() {
        return this.scale;
    }

    public int hashCode() {
        return this.bigInt.hashCode() ^ this.scale;
    }

    public int intValue() {
        return this.floor().intValue();
    }

    public long longValue() {
        return this.floor().longValue();
    }

    public SimpleBigDecimal multiply(BigInteger bigInteger) {
        return new SimpleBigDecimal(this.bigInt.multiply(bigInteger), this.scale);
    }

    public SimpleBigDecimal multiply(SimpleBigDecimal simpleBigDecimal) {
        this.checkScale(simpleBigDecimal);
        return new SimpleBigDecimal(this.bigInt.multiply(simpleBigDecimal.bigInt), this.scale + this.scale);
    }

    public SimpleBigDecimal negate() {
        return new SimpleBigDecimal(this.bigInt.negate(), this.scale);
    }

    public BigInteger round() {
        return this.add(new SimpleBigDecimal(ECConstants.ONE, 1).adjustScale(this.scale)).floor();
    }

    public SimpleBigDecimal shiftLeft(int n) {
        return new SimpleBigDecimal(this.bigInt.shiftLeft(n), this.scale);
    }

    public SimpleBigDecimal subtract(BigInteger bigInteger) {
        return new SimpleBigDecimal(this.bigInt.subtract(bigInteger.shiftLeft(this.scale)), this.scale);
    }

    public SimpleBigDecimal subtract(SimpleBigDecimal simpleBigDecimal) {
        return this.add(simpleBigDecimal.negate());
    }

    public String toString() {
        if (this.scale == 0) {
            return this.bigInt.toString();
        }
        BigInteger bigInteger = this.floor();
        BigInteger bigInteger2 = this.bigInt.subtract(bigInteger.shiftLeft(this.scale));
        if (this.bigInt.signum() == -1) {
            bigInteger2 = ECConstants.ONE.shiftLeft(this.scale).subtract(bigInteger2);
        }
        if (bigInteger.signum() == -1 && !bigInteger2.equals((Object)ECConstants.ZERO)) {
            bigInteger = bigInteger.add(ECConstants.ONE);
        }
        String string = bigInteger.toString();
        char[] arrc = new char[this.scale];
        String string2 = bigInteger2.toString(2);
        int n = string2.length();
        int n2 = this.scale - n;
        for (int i = 0; i < n2; ++i) {
            arrc[i] = 48;
        }
        for (int i = 0; i < n; ++i) {
            arrc[n2 + i] = string2.charAt(i);
        }
        String string3 = new String(arrc);
        StringBuffer stringBuffer = new StringBuffer(string);
        stringBuffer.append(".");
        stringBuffer.append(string3);
        return stringBuffer.toString();
    }
}

