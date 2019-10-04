/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.pqc.math.linearalgebra;

import org.bouncycastle.pqc.math.linearalgebra.Permutation;

public abstract class Vector {
    protected int length;

    public abstract Vector add(Vector var1);

    public abstract boolean equals(Object var1);

    public abstract byte[] getEncoded();

    public final int getLength() {
        return this.length;
    }

    public abstract int hashCode();

    public abstract boolean isZero();

    public abstract Vector multiply(Permutation var1);

    public abstract String toString();
}

