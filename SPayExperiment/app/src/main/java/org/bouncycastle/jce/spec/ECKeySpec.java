/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.spec.KeySpec
 */
package org.bouncycastle.jce.spec;

import java.security.spec.KeySpec;
import org.bouncycastle.jce.spec.ECParameterSpec;

public class ECKeySpec
implements KeySpec {
    private ECParameterSpec spec;

    protected ECKeySpec(ECParameterSpec eCParameterSpec) {
        this.spec = eCParameterSpec;
    }

    public ECParameterSpec getParams() {
        return this.spec;
    }
}

