/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.math.BigInteger
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.math.BigInteger;

public interface GFElement {
    public GFElement add(GFElement var1);

    public void addToThis(GFElement var1);

    public Object clone();

    public boolean equals(Object var1);

    public int hashCode();

    public GFElement invert();

    public boolean isOne();

    public boolean isZero();

    public GFElement multiply(GFElement var1);

    public void multiplyThisBy(GFElement var1);

    public GFElement subtract(GFElement var1);

    public void subtractFromThis(GFElement var1);

    public byte[] toByteArray();

    public BigInteger toFlexiBigInt();

    public String toString();

    public String toString(int var1);
}

