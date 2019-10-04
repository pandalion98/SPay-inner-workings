/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  javax.crypto.SecretKey
 */
package org.bouncycastle.jcajce;

import javax.crypto.SecretKey;
import org.bouncycastle.crypto.PBEParametersGenerator;

public class PKCS12Key
implements SecretKey {
    private final char[] password;

    public PKCS12Key(char[] arrc) {
        this.password = new char[arrc.length];
        System.arraycopy((Object)arrc, (int)0, (Object)this.password, (int)0, (int)arrc.length);
    }

    public String getAlgorithm() {
        return "PKCS12";
    }

    public byte[] getEncoded() {
        return PBEParametersGenerator.PKCS12PasswordToBytes(this.password);
    }

    public String getFormat() {
        return "RAW";
    }

    public char[] getPassword() {
        return this.password;
    }
}

