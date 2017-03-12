package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.util.Arrays;

public class TlsNullCipher implements TlsCipher {
    protected TlsContext context;
    protected TlsMac readMac;
    protected TlsMac writeMac;

    public TlsNullCipher(TlsContext tlsContext) {
        this.context = tlsContext;
        this.writeMac = null;
        this.readMac = null;
    }

    public TlsNullCipher(TlsContext tlsContext, Digest digest, Digest digest2) {
        TlsMac tlsMac = null;
        int i = 1;
        int i2 = digest == null ? 1 : 0;
        if (digest2 != null) {
            i = 0;
        }
        if (i2 != i) {
            throw new TlsFatalAlert((short) 80);
        }
        this.context = tlsContext;
        if (digest != null) {
            int digestSize = digest.getDigestSize() + digest2.getDigestSize();
            byte[] calculateKeyBlock = TlsUtils.calculateKeyBlock(tlsContext, digestSize);
            tlsMac = new TlsMac(tlsContext, digest, calculateKeyBlock, 0, digest.getDigestSize());
            int digestSize2 = 0 + digest.getDigestSize();
            TlsMac tlsMac2 = new TlsMac(tlsContext, digest2, calculateKeyBlock, digestSize2, digest2.getDigestSize());
            if (digest2.getDigestSize() + digestSize2 != digestSize) {
                throw new TlsFatalAlert((short) 80);
            }
        }
        tlsMac2 = null;
        if (tlsContext.isServer()) {
            this.writeMac = tlsMac2;
            this.readMac = tlsMac;
            return;
        }
        this.writeMac = tlsMac;
        this.readMac = tlsMac2;
    }

    public byte[] decodeCiphertext(long j, short s, byte[] bArr, int i, int i2) {
        if (this.readMac == null) {
            return Arrays.copyOfRange(bArr, i, i + i2);
        }
        int size = this.readMac.getSize();
        if (i2 < size) {
            throw new TlsFatalAlert((short) 50);
        }
        int i3 = i2 - size;
        if (Arrays.constantTimeAreEqual(Arrays.copyOfRange(bArr, i + i3, i + i2), this.readMac.calculateMac(j, s, bArr, i, i3))) {
            return Arrays.copyOfRange(bArr, i, i + i3);
        }
        throw new TlsFatalAlert((short) 20);
    }

    public byte[] encodePlaintext(long j, short s, byte[] bArr, int i, int i2) {
        if (this.writeMac == null) {
            return Arrays.copyOfRange(bArr, i, i + i2);
        }
        Object calculateMac = this.writeMac.calculateMac(j, s, bArr, i, i2);
        Object obj = new byte[(calculateMac.length + i2)];
        System.arraycopy(bArr, i, obj, 0, i2);
        System.arraycopy(calculateMac, 0, obj, i2, calculateMac.length);
        return obj;
    }

    public int getPlaintextLimit(int i) {
        return this.writeMac != null ? i - this.writeMac.getSize() : i;
    }
}
