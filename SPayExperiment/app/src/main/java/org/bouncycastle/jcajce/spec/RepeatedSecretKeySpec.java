/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  javax.crypto.SecretKey
 */
package org.bouncycastle.jcajce.spec;

import javax.crypto.SecretKey;

public class RepeatedSecretKeySpec
implements SecretKey {
    private String algorithm;

    public RepeatedSecretKeySpec(String string) {
        this.algorithm = string;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public byte[] getEncoded() {
        return null;
    }

    public String getFormat() {
        return null;
    }
}

