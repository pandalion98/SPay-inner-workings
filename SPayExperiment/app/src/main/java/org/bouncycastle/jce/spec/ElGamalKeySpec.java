/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.spec.KeySpec
 */
package org.bouncycastle.jce.spec;

import java.security.spec.KeySpec;
import org.bouncycastle.jce.spec.ElGamalParameterSpec;

public class ElGamalKeySpec
implements KeySpec {
    private ElGamalParameterSpec spec;

    public ElGamalKeySpec(ElGamalParameterSpec elGamalParameterSpec) {
        this.spec = elGamalParameterSpec;
    }

    public ElGamalParameterSpec getParams() {
        return this.spec;
    }
}

