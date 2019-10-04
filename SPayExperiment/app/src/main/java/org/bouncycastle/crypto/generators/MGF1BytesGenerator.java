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

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.MGFParameters;

public class MGF1BytesGenerator
implements DerivationFunction {
    private Digest digest;
    private int hLen;
    private byte[] seed;

    public MGF1BytesGenerator(Digest digest) {
        this.digest = digest;
        this.hLen = digest.getDigestSize();
    }

    private void ItoOSP(int n2, byte[] arrby) {
        arrby[0] = (byte)(n2 >>> 24);
        arrby[1] = (byte)(n2 >>> 16);
        arrby[2] = (byte)(n2 >>> 8);
        arrby[3] = (byte)(n2 >>> 0);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int generateBytes(byte[] arrby, int n2, int n3) {
        int n4;
        if (arrby.length - n3 < n2) {
            throw new DataLengthException("output buffer too small");
        }
        byte[] arrby2 = new byte[this.hLen];
        byte[] arrby3 = new byte[4];
        this.digest.reset();
        if (n3 > this.hLen) {
            n4 = 0;
            do {
                this.ItoOSP(n4, arrby3);
                this.digest.update(this.seed, 0, this.seed.length);
                this.digest.update(arrby3, 0, arrby3.length);
                this.digest.doFinal(arrby2, 0);
                System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)(n2 + n4 * this.hLen), (int)this.hLen);
            } while (++n4 < n3 / this.hLen);
        } else {
            n4 = 0;
        }
        if (n4 * this.hLen < n3) {
            this.ItoOSP(n4, arrby3);
            this.digest.update(this.seed, 0, this.seed.length);
            this.digest.update(arrby3, 0, arrby3.length);
            this.digest.doFinal(arrby2, 0);
            System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)(n2 + n4 * this.hLen), (int)(n3 - n4 * this.hLen));
        }
        return n3;
    }

    public Digest getDigest() {
        return this.digest;
    }

    @Override
    public void init(DerivationParameters derivationParameters) {
        if (!(derivationParameters instanceof MGFParameters)) {
            throw new IllegalArgumentException("MGF parameters required for MGF1Generator");
        }
        this.seed = ((MGFParameters)derivationParameters).getSeed();
    }
}

