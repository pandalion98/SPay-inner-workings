/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 *  java.security.spec.InvalidKeySpecException
 *  java.security.spec.KeySpec
 *  javax.crypto.SecretKey
 *  javax.crypto.spec.PBEKeySpec
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.jcajce.provider.symmetric.util.BCPBEKey;
import org.bouncycastle.jcajce.provider.symmetric.util.BaseSecretKeyFactory;
import org.bouncycastle.jcajce.provider.symmetric.util.PBE;

public class PBESecretKeyFactory
extends BaseSecretKeyFactory
implements PBE {
    private int digest;
    private boolean forCipher;
    private int ivSize;
    private int keySize;
    private int scheme;

    public PBESecretKeyFactory(String string, ASN1ObjectIdentifier aSN1ObjectIdentifier, boolean bl, int n, int n2, int n3, int n4) {
        super(string, aSN1ObjectIdentifier);
        this.forCipher = bl;
        this.scheme = n;
        this.digest = n2;
        this.keySize = n3;
        this.ivSize = n4;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    protected SecretKey engineGenerateSecret(KeySpec keySpec) {
        CipherParameters cipherParameters;
        if (!(keySpec instanceof PBEKeySpec)) throw new InvalidKeySpecException("Invalid KeySpec");
        PBEKeySpec pBEKeySpec = (PBEKeySpec)keySpec;
        if (pBEKeySpec.getSalt() == null) {
            return new BCPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, pBEKeySpec, null);
        }
        if (this.forCipher) {
            cipherParameters = PBE.Util.makePBEParameters(pBEKeySpec, this.scheme, this.digest, this.keySize, this.ivSize);
            do {
                return new BCPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, pBEKeySpec, cipherParameters);
                break;
            } while (true);
        }
        cipherParameters = PBE.Util.makePBEMacParameters(pBEKeySpec, this.scheme, this.digest, this.keySize);
        return new BCPBEKey(this.algName, this.algOid, this.scheme, this.digest, this.keySize, this.ivSize, pBEKeySpec, cipherParameters);
    }
}

