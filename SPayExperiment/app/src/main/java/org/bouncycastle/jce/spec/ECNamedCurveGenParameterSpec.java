/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.spec.AlgorithmParameterSpec
 */
package org.bouncycastle.jce.spec;

import java.security.spec.AlgorithmParameterSpec;

public class ECNamedCurveGenParameterSpec
implements AlgorithmParameterSpec {
    private String name;

    public ECNamedCurveGenParameterSpec(String string) {
        this.name = string;
    }

    public String getName() {
        return this.name;
    }
}

