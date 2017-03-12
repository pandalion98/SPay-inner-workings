package org.bouncycastle.crypto.tls;

import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class UseSRTPData {
    protected byte[] mki;
    protected int[] protectionProfiles;

    public UseSRTPData(int[] iArr, byte[] bArr) {
        if (iArr == null || iArr.length < 1 || iArr.length >= X509KeyUsage.decipherOnly) {
            throw new IllegalArgumentException("'protectionProfiles' must have length from 1 to (2^15 - 1)");
        }
        if (bArr == null) {
            bArr = TlsUtils.EMPTY_BYTES;
        } else if (bArr.length > GF2Field.MASK) {
            throw new IllegalArgumentException("'mki' cannot be longer than 255 bytes");
        }
        this.protectionProfiles = iArr;
        this.mki = bArr;
    }

    public byte[] getMki() {
        return this.mki;
    }

    public int[] getProtectionProfiles() {
        return this.protectionProfiles;
    }
}
