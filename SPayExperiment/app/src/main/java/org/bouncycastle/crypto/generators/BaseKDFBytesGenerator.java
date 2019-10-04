/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.DigestDerivationFunction;
import org.bouncycastle.crypto.params.ISO18033KDFParameters;
import org.bouncycastle.crypto.params.KDFParameters;
import org.bouncycastle.util.Pack;

public class BaseKDFBytesGenerator
implements DigestDerivationFunction {
    private int counterStart;
    private Digest digest;
    private byte[] iv;
    private byte[] shared;

    protected BaseKDFBytesGenerator(int n2, Digest digest) {
        this.counterStart = n2;
        this.digest = digest;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int generateBytes(byte[] arrby, int n2, int n3) {
        if (arrby.length - n3 < n2) {
            throw new DataLengthException("output buffer too small");
        }
        long l2 = n3;
        int n4 = this.digest.getDigestSize();
        if (l2 > 0x1FFFFFFFFL) {
            throw new IllegalArgumentException("Output length too large");
        }
        int n5 = (int)((l2 + (long)n4 - 1L) / (long)n4);
        byte[] arrby2 = new byte[this.digest.getDigestSize()];
        byte[] arrby3 = new byte[4];
        Pack.intToBigEndian((int)this.counterStart, (byte[])arrby3, (int)0);
        int n6 = -256 & this.counterStart;
        int n7 = 0;
        int n8 = n2;
        int n9 = n3;
        do {
            byte by;
            if (n7 >= n5) {
                this.digest.reset();
                return (int)l2;
            }
            this.digest.update(this.shared, 0, this.shared.length);
            this.digest.update(arrby3, 0, arrby3.length);
            if (this.iv != null) {
                this.digest.update(this.iv, 0, this.iv.length);
            }
            this.digest.doFinal(arrby2, 0);
            if (n9 > n4) {
                System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n8, (int)n4);
                n8 += n4;
                n9 -= n4;
            } else {
                System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n8, (int)n9);
            }
            arrby3[3] = by = (byte)(1 + arrby3[3]);
            if (by == 0) {
                Pack.intToBigEndian((int)(n6 += 256), (byte[])arrby3, (int)0);
            }
            ++n7;
        } while (true);
    }

    @Override
    public Digest getDigest() {
        return this.digest;
    }

    @Override
    public void init(DerivationParameters derivationParameters) {
        if (derivationParameters instanceof KDFParameters) {
            KDFParameters kDFParameters = (KDFParameters)derivationParameters;
            this.shared = kDFParameters.getSharedSecret();
            this.iv = kDFParameters.getIV();
            return;
        }
        if (derivationParameters instanceof ISO18033KDFParameters) {
            this.shared = ((ISO18033KDFParameters)derivationParameters).getSeed();
            this.iv = null;
            return;
        }
        throw new IllegalArgumentException("KDF parameters required for KDF2Generator");
    }
}

