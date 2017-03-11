package org.bouncycastle.jcajce;

import javax.crypto.SecretKey;
import org.bouncycastle.crypto.PBEParametersGenerator;

public class PKCS12Key implements SecretKey {
    private final char[] password;

    public PKCS12Key(char[] cArr) {
        this.password = new char[cArr.length];
        System.arraycopy(cArr, 0, this.password, 0, cArr.length);
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
