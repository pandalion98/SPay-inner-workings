/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package org.bouncycastle.crypto.params;

import org.bouncycastle.crypto.CipherParameters;

public class AsymmetricKeyParameter
implements CipherParameters {
    boolean privateKey;

    public AsymmetricKeyParameter(boolean bl) {
        this.privateKey = bl;
    }

    public boolean isPrivate() {
        return this.privateKey;
    }
}

