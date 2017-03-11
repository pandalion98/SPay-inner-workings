package org.bouncycastle.crypto.generators;

import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.DerivationParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.DigestDerivationFunction;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.ISO18033KDFParameters;
import org.bouncycastle.crypto.params.KDFParameters;
import org.bouncycastle.util.Pack;

public class BaseKDFBytesGenerator implements DigestDerivationFunction {
    private int counterStart;
    private Digest digest;
    private byte[] iv;
    private byte[] shared;

    protected BaseKDFBytesGenerator(int i, Digest digest) {
        this.counterStart = i;
        this.digest = digest;
    }

    public int generateBytes(byte[] bArr, int i, int i2) {
        if (bArr.length - i2 < i) {
            throw new DataLengthException("output buffer too small");
        }
        long j = (long) i2;
        int digestSize = this.digest.getDigestSize();
        if (j > 8589934591L) {
            throw new IllegalArgumentException("Output length too large");
        }
        int i3 = (int) (((((long) digestSize) + j) - 1) / ((long) digestSize));
        Object obj = new byte[this.digest.getDigestSize()];
        byte[] bArr2 = new byte[4];
        Pack.intToBigEndian(this.counterStart, bArr2, 0);
        int i4 = this.counterStart & -256;
        int i5 = i;
        int i6 = i2;
        for (int i7 = 0; i7 < i3; i7++) {
            this.digest.update(this.shared, 0, this.shared.length);
            this.digest.update(bArr2, 0, bArr2.length);
            if (this.iv != null) {
                this.digest.update(this.iv, 0, this.iv.length);
            }
            this.digest.doFinal(obj, 0);
            if (i6 > digestSize) {
                System.arraycopy(obj, 0, bArr, i5, digestSize);
                i5 += digestSize;
                i6 -= digestSize;
            } else {
                System.arraycopy(obj, 0, bArr, i5, i6);
            }
            byte b = (byte) (bArr2[3] + 1);
            bArr2[3] = b;
            if (b == null) {
                i4 += SkeinMac.SKEIN_256;
                Pack.intToBigEndian(i4, bArr2, 0);
            }
        }
        this.digest.reset();
        return (int) j;
    }

    public Digest getDigest() {
        return this.digest;
    }

    public void init(DerivationParameters derivationParameters) {
        if (derivationParameters instanceof KDFParameters) {
            KDFParameters kDFParameters = (KDFParameters) derivationParameters;
            this.shared = kDFParameters.getSharedSecret();
            this.iv = kDFParameters.getIV();
        } else if (derivationParameters instanceof ISO18033KDFParameters) {
            this.shared = ((ISO18033KDFParameters) derivationParameters).getSeed();
            this.iv = null;
        } else {
            throw new IllegalArgumentException("KDF parameters required for KDF2Generator");
        }
    }
}
