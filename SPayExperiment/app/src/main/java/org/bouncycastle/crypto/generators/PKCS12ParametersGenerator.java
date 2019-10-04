/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.ExtendedDigest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class PKCS12ParametersGenerator
extends PBEParametersGenerator {
    public static final int IV_MATERIAL = 2;
    public static final int KEY_MATERIAL = 1;
    public static final int MAC_MATERIAL = 3;
    private Digest digest;
    private int u;
    private int v;

    public PKCS12ParametersGenerator(Digest digest) {
        this.digest = digest;
        if (digest instanceof ExtendedDigest) {
            this.u = digest.getDigestSize();
            this.v = ((ExtendedDigest)digest).getByteLength();
            return;
        }
        throw new IllegalArgumentException("Digest " + digest.getAlgorithmName() + " unsupported");
    }

    private void adjust(byte[] arrby, int n2, byte[] arrby2) {
        int n3 = 1 + ((255 & arrby2[-1 + arrby2.length]) + (255 & arrby[-1 + (n2 + arrby2.length)]));
        arrby[-1 + (n2 + arrby2.length)] = (byte)n3;
        int n4 = n3 >>> 8;
        for (int i2 = -2 + arrby2.length; i2 >= 0; --i2) {
            int n5 = n4 + ((255 & arrby2[i2]) + (255 & arrby[n2 + i2]));
            arrby[n2 + i2] = (byte)n5;
            n4 = n5 >>> 8;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private byte[] generateDerivedKey(int n2, int n3) {
        byte[] arrby;
        byte[] arrby2;
        byte[] arrby3 = new byte[this.v];
        byte[] arrby4 = new byte[n3];
        for (int i2 = 0; i2 != arrby3.length; ++i2) {
            arrby3[i2] = (byte)n2;
        }
        if (this.salt != null && this.salt.length != 0) {
            byte[] arrby5 = new byte[this.v * ((-1 + (this.salt.length + this.v)) / this.v)];
            for (int i3 = 0; i3 != arrby5.length; ++i3) {
                arrby5[i3] = this.salt[i3 % this.salt.length];
            }
            arrby2 = arrby5;
        } else {
            arrby2 = new byte[]{};
        }
        if (this.password != null && this.password.length != 0) {
            byte[] arrby6 = new byte[this.v * ((-1 + (this.password.length + this.v)) / this.v)];
            for (int i4 = 0; i4 != arrby6.length; ++i4) {
                arrby6[i4] = this.password[i4 % this.password.length];
            }
            arrby = arrby6;
        } else {
            arrby = new byte[]{};
        }
        byte[] arrby7 = new byte[arrby2.length + arrby.length];
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby7, (int)0, (int)arrby2.length);
        System.arraycopy((Object)arrby, (int)0, (Object)arrby7, (int)arrby2.length, (int)arrby.length);
        byte[] arrby8 = new byte[this.v];
        int n4 = (-1 + (n3 + this.u)) / this.u;
        byte[] arrby9 = new byte[this.u];
        int n5 = 1;
        while (n5 <= n4) {
            this.digest.update(arrby3, 0, arrby3.length);
            this.digest.update(arrby7, 0, arrby7.length);
            this.digest.doFinal(arrby9, 0);
            for (int i5 = 1; i5 < this.iterationCount; ++i5) {
                this.digest.update(arrby9, 0, arrby9.length);
                this.digest.doFinal(arrby9, 0);
            }
            for (int i6 = 0; i6 != arrby8.length; ++i6) {
                arrby8[i6] = arrby9[i6 % arrby9.length];
            }
            for (int i7 = 0; i7 != arrby7.length / this.v; ++i7) {
                this.adjust(arrby7, i7 * this.v, arrby8);
            }
            if (n5 == n4) {
                System.arraycopy((Object)arrby9, (int)0, (Object)arrby4, (int)((n5 - 1) * this.u), (int)(arrby4.length - (n5 - 1) * this.u));
            } else {
                System.arraycopy((Object)arrby9, (int)0, (Object)arrby4, (int)((n5 - 1) * this.u), (int)arrby9.length);
            }
            ++n5;
        }
        return arrby4;
    }

    @Override
    public CipherParameters generateDerivedMacParameters(int n2) {
        int n3 = n2 / 8;
        return new KeyParameter(this.generateDerivedKey(3, n3), 0, n3);
    }

    @Override
    public CipherParameters generateDerivedParameters(int n2) {
        int n3 = n2 / 8;
        return new KeyParameter(this.generateDerivedKey(1, n3), 0, n3);
    }

    @Override
    public CipherParameters generateDerivedParameters(int n2, int n3) {
        int n4 = n2 / 8;
        int n5 = n3 / 8;
        byte[] arrby = this.generateDerivedKey(1, n4);
        byte[] arrby2 = this.generateDerivedKey(2, n5);
        return new ParametersWithIV(new KeyParameter(arrby, 0, n4), arrby2, 0, n5);
    }
}

