/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.security.PrivateKey
 *  java.security.PublicKey
 *  java.security.spec.KeySpec
 */
package org.bouncycastle.jce.spec;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import org.bouncycastle.jce.interfaces.IESKey;

public class IEKeySpec
implements KeySpec,
IESKey {
    private PrivateKey privKey;
    private PublicKey pubKey;

    public IEKeySpec(PrivateKey privateKey, PublicKey publicKey) {
        this.privKey = privateKey;
        this.pubKey = publicKey;
    }

    public String getAlgorithm() {
        return "IES";
    }

    public byte[] getEncoded() {
        return null;
    }

    public String getFormat() {
        return null;
    }

    @Override
    public PrivateKey getPrivate() {
        return this.privKey;
    }

    @Override
    public PublicKey getPublic() {
        return this.pubKey;
    }
}

