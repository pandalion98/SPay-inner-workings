/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.jce.spec;

import java.security.spec.AlgorithmParameterSpec;

public class ElGamalGenParameterSpec
implements AlgorithmParameterSpec {
    private int primeSize;

    public ElGamalGenParameterSpec(int n) {
        this.primeSize = n;
    }

    public int getPrimeSize() {
        return this.primeSize;
    }
}

