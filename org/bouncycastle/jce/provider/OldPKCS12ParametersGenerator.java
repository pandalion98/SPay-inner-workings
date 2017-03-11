package org.bouncycastle.jce.provider;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.PBEParametersGenerator;
import org.bouncycastle.crypto.digests.MD5Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

class OldPKCS12ParametersGenerator extends PBEParametersGenerator {
    public static final int IV_MATERIAL = 2;
    public static final int KEY_MATERIAL = 1;
    public static final int MAC_MATERIAL = 3;
    private Digest digest;
    private int f298u;
    private int f299v;

    public OldPKCS12ParametersGenerator(Digest digest) {
        this.digest = digest;
        if (digest instanceof MD5Digest) {
            this.f298u = 16;
            this.f299v = 64;
        } else if (digest instanceof SHA1Digest) {
            this.f298u = 20;
            this.f299v = 64;
        } else if (digest instanceof RIPEMD160Digest) {
            this.f298u = 20;
            this.f299v = 64;
        } else {
            throw new IllegalArgumentException("Digest " + digest.getAlgorithmName() + " unsupported");
        }
    }

    private void adjust(byte[] bArr, int i, byte[] bArr2) {
        int i2 = ((bArr2[bArr2.length - 1] & GF2Field.MASK) + (bArr[(bArr2.length + i) - 1] & GF2Field.MASK)) + KEY_MATERIAL;
        bArr[(bArr2.length + i) - 1] = (byte) i2;
        int i3 = i2 >>> 8;
        for (i2 = bArr2.length - 2; i2 >= 0; i2--) {
            i3 += (bArr2[i2] & GF2Field.MASK) + (bArr[i + i2] & GF2Field.MASK);
            bArr[i + i2] = (byte) i3;
            i3 >>>= 8;
        }
    }

    private byte[] generateDerivedKey(int i, int i2) {
        int i3;
        Object obj;
        Object obj2;
        Object obj3;
        int i4;
        byte[] bArr = new byte[this.f299v];
        Object obj4 = new byte[i2];
        for (i3 = 0; i3 != bArr.length; i3 += KEY_MATERIAL) {
            bArr[i3] = (byte) i;
        }
        if (this.salt == null || this.salt.length == 0) {
            obj = new byte[0];
        } else {
            obj2 = new byte[(this.f299v * (((this.salt.length + this.f299v) - 1) / this.f299v))];
            for (i3 = 0; i3 != obj2.length; i3 += KEY_MATERIAL) {
                obj2[i3] = this.salt[i3 % this.salt.length];
            }
            obj = obj2;
        }
        if (this.password == null || this.password.length == 0) {
            obj2 = new byte[0];
        } else {
            obj3 = new byte[(this.f299v * (((this.password.length + this.f299v) - 1) / this.f299v))];
            for (i4 = 0; i4 != obj3.length; i4 += KEY_MATERIAL) {
                obj3[i4] = this.password[i4 % this.password.length];
            }
            obj2 = obj3;
        }
        obj3 = new byte[(obj.length + obj2.length)];
        System.arraycopy(obj, 0, obj3, 0, obj.length);
        System.arraycopy(obj2, 0, obj3, obj.length, obj2.length);
        byte[] bArr2 = new byte[this.f299v];
        int i5 = ((this.f298u + i2) - 1) / this.f298u;
        for (i4 = KEY_MATERIAL; i4 <= i5; i4 += KEY_MATERIAL) {
            Object obj5 = new byte[this.f298u];
            this.digest.update(bArr, 0, bArr.length);
            this.digest.update(obj3, 0, obj3.length);
            this.digest.doFinal(obj5, 0);
            for (i3 = KEY_MATERIAL; i3 != this.iterationCount; i3 += KEY_MATERIAL) {
                this.digest.update(obj5, 0, obj5.length);
                this.digest.doFinal(obj5, 0);
            }
            for (i3 = 0; i3 != bArr2.length; i3 += KEY_MATERIAL) {
                bArr2[i4] = obj5[i3 % obj5.length];
            }
            for (i3 = 0; i3 != obj3.length / this.f299v; i3 += KEY_MATERIAL) {
                adjust(obj3, this.f299v * i3, bArr2);
            }
            if (i4 == i5) {
                System.arraycopy(obj5, 0, obj4, (i4 - 1) * this.f298u, obj4.length - ((i4 - 1) * this.f298u));
            } else {
                System.arraycopy(obj5, 0, obj4, (i4 - 1) * this.f298u, obj5.length);
            }
        }
        return obj4;
    }

    public CipherParameters generateDerivedMacParameters(int i) {
        int i2 = i / 8;
        return new KeyParameter(generateDerivedKey(MAC_MATERIAL, i2), 0, i2);
    }

    public CipherParameters generateDerivedParameters(int i) {
        int i2 = i / 8;
        return new KeyParameter(generateDerivedKey(KEY_MATERIAL, i2), 0, i2);
    }

    public CipherParameters generateDerivedParameters(int i, int i2) {
        int i3 = i / 8;
        int i4 = i2 / 8;
        byte[] generateDerivedKey = generateDerivedKey(KEY_MATERIAL, i3);
        return new ParametersWithIV(new KeyParameter(generateDerivedKey, 0, i3), generateDerivedKey(IV_MATERIAL, i4), 0, i4);
    }
}
