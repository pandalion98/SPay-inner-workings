/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.HKDFParameters;
import org.bouncycastle.crypto.params.KeyParameter;

public class HKDFBytesGenerator
implements DerivationFunction {
    private byte[] currentT;
    private int generatedBytes;
    private HMac hMacHash;
    private int hashLen;
    private byte[] info;

    public HKDFBytesGenerator(Digest digest) {
        this.hMacHash = new HMac(digest);
        this.hashLen = digest.getDigestSize();
    }

    private void expandNext() {
        int n2 = 1 + this.generatedBytes / this.hashLen;
        if (n2 >= 256) {
            throw new DataLengthException("HKDF cannot generate more than 255 blocks of HashLen size");
        }
        if (this.generatedBytes != 0) {
            this.hMacHash.update(this.currentT, 0, this.hashLen);
        }
        this.hMacHash.update(this.info, 0, this.info.length);
        this.hMacHash.update((byte)n2);
        this.hMacHash.doFinal(this.currentT, 0);
    }

    /*
     * Enabled aggressive block sorting
     */
    private KeyParameter extract(byte[] arrby, byte[] arrby2) {
        this.hMacHash.init(new KeyParameter(arrby2));
        if (arrby == null) {
            this.hMacHash.init(new KeyParameter(new byte[this.hashLen]));
        } else {
            this.hMacHash.init(new KeyParameter(arrby));
        }
        this.hMacHash.update(arrby2, 0, arrby2.length);
        byte[] arrby3 = new byte[this.hashLen];
        this.hMacHash.doFinal(arrby3, 0);
        return new KeyParameter(arrby3);
    }

    @Override
    public int generateBytes(byte[] arrby, int n2, int n3) {
        if (n3 + this.generatedBytes > 255 * this.hashLen) {
            throw new DataLengthException("HKDF may only be used for 255 * HashLen bytes of output");
        }
        if (this.generatedBytes % this.hashLen == 0) {
            this.expandNext();
        }
        int n4 = this.generatedBytes % this.hashLen;
        int n5 = Math.min((int)(this.hashLen - this.generatedBytes % this.hashLen), (int)n3);
        System.arraycopy((Object)this.currentT, (int)n4, (Object)arrby, (int)n2, (int)n5);
        this.generatedBytes = n5 + this.generatedBytes;
        int n6 = n3 - n5;
        int n7 = n5 + n2;
        while (n6 > 0) {
            this.expandNext();
            int n8 = Math.min((int)this.hashLen, (int)n6);
            System.arraycopy((Object)this.currentT, (int)0, (Object)arrby, (int)n7, (int)n8);
            this.generatedBytes = n8 + this.generatedBytes;
            n6 -= n8;
            n7 += n8;
        }
        return n3;
    }

    public Digest getDigest() {
        return this.hMacHash.getUnderlyingDigest();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(DerivationParameters derivationParameters) {
        if (!(derivationParameters instanceof HKDFParameters)) {
            throw new IllegalArgumentException("HKDF parameters required for HKDFBytesGenerator");
        }
        HKDFParameters hKDFParameters = (HKDFParameters)derivationParameters;
        if (hKDFParameters.skipExtract()) {
            this.hMacHash.init(new KeyParameter(hKDFParameters.getIKM()));
        } else {
            this.hMacHash.init(this.extract(hKDFParameters.getSalt(), hKDFParameters.getIKM()));
        }
        this.info = hKDFParameters.getInfo();
        this.generatedBytes = 0;
        this.currentT = new byte[this.hashLen];
    }
}

