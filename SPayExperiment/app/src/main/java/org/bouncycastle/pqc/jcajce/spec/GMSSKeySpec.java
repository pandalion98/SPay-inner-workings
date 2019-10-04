/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.security.spec.KeySpec
 */
package org.bouncycastle.pqc.jcajce.spec;

import java.security.spec.KeySpec;
import org.bouncycastle.pqc.crypto.gmss.GMSSParameters;

public class GMSSKeySpec
implements KeySpec {
    private GMSSParameters gmssParameterSet;

    protected GMSSKeySpec(GMSSParameters gMSSParameters) {
        this.gmssParameterSet = gMSSParameters;
    }

    public GMSSParameters getParameters() {
        return this.gmssParameterSet;
    }
}

