/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.bouncycastle.util.Strings
 */
package org.bouncycastle.crypto;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.util.Strings;

public abstract class PBEParametersGenerator {
    protected int iterationCount;
    protected byte[] password;
    protected byte[] salt;

    protected PBEParametersGenerator() {
    }

    public static byte[] PKCS12PasswordToBytes(char[] arrc) {
        if (arrc != null && arrc.length > 0) {
            byte[] arrby = new byte[2 * (1 + arrc.length)];
            for (int i2 = 0; i2 != arrc.length; ++i2) {
                arrby[i2 * 2] = (byte)(arrc[i2] >>> 8);
                arrby[1 + i2 * 2] = (byte)arrc[i2];
            }
            return arrby;
        }
        return new byte[0];
    }

    public static byte[] PKCS5PasswordToBytes(char[] arrc) {
        if (arrc != null) {
            byte[] arrby = new byte[arrc.length];
            for (int i2 = 0; i2 != arrby.length; ++i2) {
                arrby[i2] = (byte)arrc[i2];
            }
            return arrby;
        }
        return new byte[0];
    }

    public static byte[] PKCS5PasswordToUTF8Bytes(char[] arrc) {
        if (arrc != null) {
            return Strings.toUTF8ByteArray((char[])arrc);
        }
        return new byte[0];
    }

    public abstract CipherParameters generateDerivedMacParameters(int var1);

    public abstract CipherParameters generateDerivedParameters(int var1);

    public abstract CipherParameters generateDerivedParameters(int var1, int var2);

    public int getIterationCount() {
        return this.iterationCount;
    }

    public byte[] getPassword() {
        return this.password;
    }

    public byte[] getSalt() {
        return this.salt;
    }

    public void init(byte[] arrby, byte[] arrby2, int n2) {
        this.password = arrby;
        this.salt = arrby2;
        this.iterationCount = n2;
    }
}

