/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.digests.LongDigest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.SSL3Mac;
import org.bouncycastle.crypto.tls.SecurityParameters;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;

public class TlsMac {
    protected TlsContext context;
    protected int digestBlockSize;
    protected int digestOverhead;
    protected Mac mac;
    protected int macLength;
    protected byte[] secret;

    /*
     * Enabled aggressive block sorting
     */
    public TlsMac(TlsContext tlsContext, Digest digest, byte[] arrby, int n2, int n3) {
        this.context = tlsContext;
        KeyParameter keyParameter = new KeyParameter(arrby, n2, n3);
        this.secret = Arrays.clone((byte[])keyParameter.getKey());
        if (digest instanceof LongDigest) {
            this.digestBlockSize = 128;
            this.digestOverhead = 16;
        } else {
            this.digestBlockSize = 64;
            this.digestOverhead = 8;
        }
        if (TlsUtils.isSSL(tlsContext)) {
            this.mac = new SSL3Mac(digest);
            if (digest.getDigestSize() == 20) {
                this.digestOverhead = 4;
            }
        } else {
            this.mac = new HMac(digest);
        }
        this.mac.init(keyParameter);
        this.macLength = this.mac.getMacSize();
        if (tlsContext.getSecurityParameters().truncatedHMac) {
            this.macLength = Math.min((int)this.macLength, (int)10);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[] calculateMac(long l2, short s2, byte[] arrby, int n2, int n3) {
        ProtocolVersion protocolVersion = this.context.getServerVersion();
        boolean bl = protocolVersion.isSSL();
        int n4 = bl ? 11 : 13;
        byte[] arrby2 = new byte[n4];
        TlsUtils.writeUint64(l2, arrby2, 0);
        TlsUtils.writeUint8(s2, arrby2, 8);
        if (!bl) {
            TlsUtils.writeVersion(protocolVersion, arrby2, 9);
        }
        TlsUtils.writeUint16(n3, arrby2, -2 + arrby2.length);
        this.mac.update(arrby2, 0, arrby2.length);
        this.mac.update(arrby, n2, n3);
        byte[] arrby3 = new byte[this.mac.getMacSize()];
        this.mac.doFinal(arrby3, 0);
        return this.truncate(arrby3);
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[] calculateMacConstantTime(long l2, short s2, byte[] arrby, int n2, int n3, int n4, byte[] arrby2) {
        byte[] arrby3 = this.calculateMac(l2, s2, arrby, n2, n3);
        int n5 = TlsUtils.isSSL(this.context) ? 11 : 13;
        int n6 = this.getDigestBlockCount(n5 + n4) - this.getDigestBlockCount(n5 + n3);
        do {
            if (--n6 < 0) {
                this.mac.update(arrby2[0]);
                this.mac.reset();
                return arrby3;
            }
            this.mac.update(arrby2, 0, this.digestBlockSize);
        } while (true);
    }

    protected int getDigestBlockCount(int n2) {
        return (n2 + this.digestOverhead) / this.digestBlockSize;
    }

    public byte[] getMACSecret() {
        return this.secret;
    }

    public int getSize() {
        return this.macLength;
    }

    protected byte[] truncate(byte[] arrby) {
        if (arrby.length <= this.macLength) {
            return arrby;
        }
        return Arrays.copyOf((byte[])arrby, (int)this.macLength);
    }
}

