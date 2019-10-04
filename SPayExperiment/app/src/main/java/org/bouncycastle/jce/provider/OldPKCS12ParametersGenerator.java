/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

class OldPKCS12ParametersGenerator
extends PBEParametersGenerator {
    public static final int IV_MATERIAL = 2;
    public static final int KEY_MATERIAL = 1;
    public static final int MAC_MATERIAL = 3;
    private Digest digest;
    private int u;
    private int v;

    public OldPKCS12ParametersGenerator(Digest digest) {
        this.digest = digest;
        if (digest instanceof MD5Digest) {
            this.u = 16;
            this.v = 64;
            return;
        }
        if (digest instanceof SHA1Digest) {
            this.u = 20;
            this.v = 64;
            return;
        }
        if (digest instanceof RIPEMD160Digest) {
            this.u = 20;
            this.v = 64;
            return;
        }
        throw new IllegalArgumentException("Digest " + digest.getAlgorithmName() + " unsupported");
    }

    private void adjust(byte[] arrby, int n, byte[] arrby2) {
        int n2 = 1 + ((255 & arrby2[-1 + arrby2.length]) + (255 & arrby[-1 + (n + arrby2.length)]));
        arrby[-1 + (n + arrby2.length)] = (byte)n2;
        int n3 = n2 >>> 8;
        for (int i = -2 + arrby2.length; i >= 0; --i) {
            int n4 = n3 + ((255 & arrby2[i]) + (255 & arrby[n + i]));
            arrby[n + i] = (byte)n4;
            n3 = n4 >>> 8;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] generateDerivedKey(int n, int n2) {
        byte[] arrby;
        byte[] arrby2;
        byte[] arrby3 = new byte[this.v];
        byte[] arrby4 = new byte[n2];
        for (int i = 0; i != arrby3.length; ++i) {
            arrby3[i] = (byte)n;
        }
        if (this.salt != null && this.salt.length != 0) {
            byte[] arrby5 = new byte[this.v * ((-1 + (this.salt.length + this.v)) / this.v)];
            for (int i = 0; i != arrby5.length; ++i) {
                arrby5[i] = this.salt[i % this.salt.length];
            }
            arrby2 = arrby5;
        } else {
            arrby2 = new byte[]{};
        }
        if (this.password != null && this.password.length != 0) {
            byte[] arrby6 = new byte[this.v * ((-1 + (this.password.length + this.v)) / this.v)];
            for (int i = 0; i != arrby6.length; ++i) {
                arrby6[i] = this.password[i % this.password.length];
            }
            arrby = arrby6;
        } else {
            arrby = new byte[]{};
        }
        byte[] arrby7 = new byte[arrby2.length + arrby.length];
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby7, (int)0, (int)arrby2.length);
        System.arraycopy((Object)arrby, (int)0, (Object)arrby7, (int)arrby2.length, (int)arrby.length);
        byte[] arrby8 = new byte[this.v];
        int n3 = (-1 + (n2 + this.u)) / this.u;
        int n4 = 1;
        while (n4 <= n3) {
            byte[] arrby9 = new byte[this.u];
            this.digest.update(arrby3, 0, arrby3.length);
            this.digest.update(arrby7, 0, arrby7.length);
            this.digest.doFinal(arrby9, 0);
            for (int i = 1; i != this.iterationCount; ++i) {
                this.digest.update(arrby9, 0, arrby9.length);
                this.digest.doFinal(arrby9, 0);
            }
            for (int i = 0; i != arrby8.length; ++i) {
                arrby8[n4] = arrby9[i % arrby9.length];
            }
            for (int i = 0; i != arrby7.length / this.v; ++i) {
                this.adjust(arrby7, i * this.v, arrby8);
            }
            if (n4 == n3) {
                System.arraycopy((Object)arrby9, (int)0, (Object)arrby4, (int)((n4 - 1) * this.u), (int)(arrby4.length - (n4 - 1) * this.u));
            } else {
                System.arraycopy((Object)arrby9, (int)0, (Object)arrby4, (int)((n4 - 1) * this.u), (int)arrby9.length);
            }
            ++n4;
        }
        return arrby4;
    }

    @Override
    public CipherParameters generateDerivedMacParameters(int n) {
        int n2 = n / 8;
        return new KeyParameter(this.generateDerivedKey(3, n2), 0, n2);
    }

    @Override
    public CipherParameters generateDerivedParameters(int n) {
        int n2 = n / 8;
        return new KeyParameter(this.generateDerivedKey(1, n2), 0, n2);
    }

    @Override
    public CipherParameters generateDerivedParameters(int n, int n2) {
        int n3 = n / 8;
        int n4 = n2 / 8;
        byte[] arrby = this.generateDerivedKey(1, n3);
        byte[] arrby2 = this.generateDerivedKey(2, n4);
        return new ParametersWithIV(new KeyParameter(arrby, 0, n3), arrby2, 0, n4);
    }
}

