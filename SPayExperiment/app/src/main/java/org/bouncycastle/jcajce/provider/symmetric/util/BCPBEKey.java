/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  javax.crypto.interfaces.PBEKey
 *  javax.crypto.spec.PBEKeySpec
 */
package org.bouncycastle.jcajce.provider.symmetric.util;

import javax.crypto.interfaces.PBEKey;
import javax.crypto.spec.PBEKeySpec;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class BCPBEKey
implements PBEKey {
    String algorithm;
    int digest;
    int ivSize;
    int keySize;
    ASN1ObjectIdentifier oid;
    CipherParameters param;
    PBEKeySpec pbeKeySpec;
    boolean tryWrong = false;
    int type;

    public BCPBEKey(String string, ASN1ObjectIdentifier aSN1ObjectIdentifier, int n, int n2, int n3, int n4, PBEKeySpec pBEKeySpec, CipherParameters cipherParameters) {
        this.algorithm = string;
        this.oid = aSN1ObjectIdentifier;
        this.type = n;
        this.digest = n2;
        this.keySize = n3;
        this.ivSize = n4;
        this.pbeKeySpec = pBEKeySpec;
        this.param = cipherParameters;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    int getDigest() {
        return this.digest;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public byte[] getEncoded() {
        if (this.param != null) {
            KeyParameter keyParameter;
            if (this.param instanceof ParametersWithIV) {
                keyParameter = (KeyParameter)((ParametersWithIV)this.param).getParameters();
                do {
                    return keyParameter.getKey();
                    break;
                } while (true);
            }
            keyParameter = (KeyParameter)this.param;
            return keyParameter.getKey();
        }
        if (this.type == 2) {
            return PBEParametersGenerator.PKCS12PasswordToBytes(this.pbeKeySpec.getPassword());
        }
        if (this.type != 5) return PBEParametersGenerator.PKCS5PasswordToBytes(this.pbeKeySpec.getPassword());
        return PBEParametersGenerator.PKCS5PasswordToUTF8Bytes(this.pbeKeySpec.getPassword());
    }

    public String getFormat() {
        return "RAW";
    }

    public int getIterationCount() {
        return this.pbeKeySpec.getIterationCount();
    }

    public int getIvSize() {
        return this.ivSize;
    }

    int getKeySize() {
        return this.keySize;
    }

    public ASN1ObjectIdentifier getOID() {
        return this.oid;
    }

    public CipherParameters getParam() {
        return this.param;
    }

    public char[] getPassword() {
        return this.pbeKeySpec.getPassword();
    }

    public byte[] getSalt() {
        return this.pbeKeySpec.getSalt();
    }

    int getType() {
        return this.type;
    }

    public void setTryWrongPKCS12Zero(boolean bl) {
        this.tryWrong = bl;
    }

    boolean shouldTryWrongPKCS12() {
        return this.tryWrong;
    }
}

