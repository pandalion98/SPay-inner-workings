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

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationFunction;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.params.KDFParameters;

public class BrokenKDF2BytesGenerator
implements DerivationFunction {
    private Digest digest;
    private byte[] iv;
    private byte[] shared;

    public BrokenKDF2BytesGenerator(Digest digest) {
        this.digest = digest;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int generateBytes(byte[] arrby, int n, int n2) {
        if (arrby.length - n2 < n) {
            throw new DataLengthException("output buffer too small");
        }
        long l = n2 * 8;
        if (l > 29L * (long)(8 * this.digest.getDigestSize())) {
            new IllegalArgumentException("Output length to large");
        }
        int n3 = (int)(l / (long)this.digest.getDigestSize());
        byte[] arrby2 = new byte[this.digest.getDigestSize()];
        int n4 = 1;
        int n5 = n;
        do {
            if (n4 > n3) {
                this.digest.reset();
                return n2;
            }
            this.digest.update(this.shared, 0, this.shared.length);
            this.digest.update((byte)(n4 & 255));
            this.digest.update((byte)(255 & n4 >> 8));
            this.digest.update((byte)(255 & n4 >> 16));
            this.digest.update((byte)(255 & n4 >> 24));
            this.digest.update(this.iv, 0, this.iv.length);
            this.digest.doFinal(arrby2, 0);
            if (n2 - n5 > arrby2.length) {
                System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n5, (int)arrby2.length);
                n5 += arrby2.length;
            } else {
                System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n5, (int)(n2 - n5));
            }
            ++n4;
        } while (true);
    }

    public Digest getDigest() {
        return this.digest;
    }

    @Override
    public void init(DerivationParameters derivationParameters) {
        if (!(derivationParameters instanceof KDFParameters)) {
            throw new IllegalArgumentException("KDF parameters required for KDF2Generator");
        }
        KDFParameters kDFParameters = (KDFParameters)derivationParameters;
        this.shared = kDFParameters.getSharedSecret();
        this.iv = kDFParameters.getIV();
    }
}

