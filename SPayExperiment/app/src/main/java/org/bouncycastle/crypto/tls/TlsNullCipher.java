/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsMac;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;

public class TlsNullCipher
implements TlsCipher {
    protected TlsContext context;
    protected TlsMac readMac;
    protected TlsMac writeMac;

    public TlsNullCipher(TlsContext tlsContext) {
        this.context = tlsContext;
        this.writeMac = null;
        this.readMac = null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public TlsNullCipher(TlsContext tlsContext, Digest digest, Digest digest2) {
        TlsMac tlsMac;
        TlsMac tlsMac2 = null;
        boolean bl = true;
        boolean bl2 = digest == null ? bl : false;
        if (digest2 != null) {
            bl = false;
        }
        if (bl2 != bl) {
            throw new TlsFatalAlert(80);
        }
        this.context = tlsContext;
        if (digest != null) {
            int n2 = digest.getDigestSize() + digest2.getDigestSize();
            byte[] arrby = TlsUtils.calculateKeyBlock(tlsContext, n2);
            tlsMac2 = new TlsMac(tlsContext, digest, arrby, 0, digest.getDigestSize());
            int n3 = 0 + digest.getDigestSize();
            tlsMac = new TlsMac(tlsContext, digest2, arrby, n3, digest2.getDigestSize());
            if (n3 + digest2.getDigestSize() != n2) {
                throw new TlsFatalAlert(80);
            }
        } else {
            tlsMac = null;
        }
        if (tlsContext.isServer()) {
            this.writeMac = tlsMac;
            this.readMac = tlsMac2;
            return;
        }
        this.writeMac = tlsMac2;
        this.readMac = tlsMac;
    }

    @Override
    public byte[] decodeCiphertext(long l2, short s2, byte[] arrby, int n2, int n3) {
        if (this.readMac == null) {
            return Arrays.copyOfRange((byte[])arrby, (int)n2, (int)(n2 + n3));
        }
        int n4 = this.readMac.getSize();
        if (n3 < n4) {
            throw new TlsFatalAlert(50);
        }
        int n5 = n3 - n4;
        if (!Arrays.constantTimeAreEqual((byte[])Arrays.copyOfRange((byte[])arrby, (int)(n2 + n5), (int)(n2 + n3)), (byte[])this.readMac.calculateMac(l2, s2, arrby, n2, n5))) {
            throw new TlsFatalAlert(20);
        }
        return Arrays.copyOfRange((byte[])arrby, (int)n2, (int)(n2 + n5));
    }

    @Override
    public byte[] encodePlaintext(long l2, short s2, byte[] arrby, int n2, int n3) {
        if (this.writeMac == null) {
            return Arrays.copyOfRange((byte[])arrby, (int)n2, (int)(n2 + n3));
        }
        byte[] arrby2 = this.writeMac.calculateMac(l2, s2, arrby, n2, n3);
        byte[] arrby3 = new byte[n3 + arrby2.length];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby3, (int)0, (int)n3);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby3, (int)n3, (int)arrby2.length);
        return arrby3;
    }

    @Override
    public int getPlaintextLimit(int n2) {
        if (this.writeMac != null) {
            n2 -= this.writeMac.getSize();
        }
        return n2;
    }
}

