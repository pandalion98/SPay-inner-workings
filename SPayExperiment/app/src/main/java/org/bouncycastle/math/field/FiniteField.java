/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.math.BigInteger
 */
package org.bouncycastle.math.field;

import java.math.BigInteger;

public interface FiniteField {
    public BigInteger getCharacteristic();

    public int getDimension();
}

