/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.asn1.x9;

import org.bouncycastle.asn1.x9.X9ECParameters;

public abstract class X9ECParametersHolder {
    private X9ECParameters params;

    protected abstract X9ECParameters createParameters();

    public X9ECParameters getParameters() {
        if (this.params == null) {
            this.params = this.createParameters();
        }
        return this.params;
    }
}

