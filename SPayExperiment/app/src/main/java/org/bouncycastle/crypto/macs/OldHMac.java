/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;

public class OldHMac
implements Mac {
    private static final int BLOCK_LENGTH = 64;
    private static final byte IPAD = 54;
    private static final byte OPAD = 92;
    private Digest digest;
    private int digestSize;
    private byte[] inputPad = new byte[64];
    private byte[] outputPad = new byte[64];

    public OldHMac(Digest digest) {
        this.digest = digest;
        this.digestSize = digest.getDigestSize();
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        byte[] arrby2 = new byte[this.digestSize];
        this.digest.doFinal(arrby2, 0);
        this.digest.update(this.outputPad, 0, this.outputPad.length);
        this.digest.update(arrby2, 0, arrby2.length);
        int n3 = this.digest.doFinal(arrby, n2);
        this.reset();
        return n3;
    }

    @Override
    public String getAlgorithmName() {
        return this.digest.getAlgorithmName() + "/HMAC";
    }

    @Override
    public int getMacSize() {
        return this.digestSize;
    }

    public Digest getUnderlyingDigest() {
        return this.digest;
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        this.digest.reset();
        byte[] arrby = ((KeyParameter)cipherParameters).getKey();
        if (arrby.length > 64) {
            this.digest.update(arrby, 0, arrby.length);
            this.digest.doFinal(this.inputPad, 0);
            for (int i2 = this.digestSize; i2 < this.inputPad.length; ++i2) {
                this.inputPad[i2] = 0;
            }
        } else {
            System.arraycopy((Object)arrby, (int)0, (Object)this.inputPad, (int)0, (int)arrby.length);
            for (int i3 = arrby.length; i3 < this.inputPad.length; ++i3) {
                this.inputPad[i3] = 0;
            }
        }
        this.outputPad = new byte[this.inputPad.length];
        System.arraycopy((Object)this.inputPad, (int)0, (Object)this.outputPad, (int)0, (int)this.inputPad.length);
        for (int i4 = 0; i4 < this.inputPad.length; ++i4) {
            byte[] arrby2 = this.inputPad;
            arrby2[i4] = (byte)(54 ^ arrby2[i4]);
        }
        for (int i5 = 0; i5 < this.outputPad.length; ++i5) {
            byte[] arrby3 = this.outputPad;
            arrby3[i5] = (byte)(92 ^ arrby3[i5]);
        }
        this.digest.update(this.inputPad, 0, this.inputPad.length);
    }

    @Override
    public void reset() {
        this.digest.reset();
        this.digest.update(this.inputPad, 0, this.inputPad.length);
    }

    @Override
    public void update(byte by) {
        this.digest.update(by);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        this.digest.update(arrby, n2, n3);
    }
}

