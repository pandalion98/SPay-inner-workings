/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsMac;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;

public class TlsStreamCipher
implements TlsCipher {
    protected TlsContext context;
    protected StreamCipher decryptCipher;
    protected StreamCipher encryptCipher;
    protected TlsMac readMac;
    protected boolean usesNonce;
    protected TlsMac writeMac;

    /*
     * Enabled aggressive block sorting
     */
    public TlsStreamCipher(TlsContext tlsContext, StreamCipher streamCipher, StreamCipher streamCipher2, Digest digest, Digest digest2, int n2, boolean bl) {
        void var20_25;
        KeyParameter keyParameter;
        void var21_22;
        boolean bl2 = tlsContext.isServer();
        this.context = tlsContext;
        this.usesNonce = bl;
        this.encryptCipher = streamCipher;
        this.decryptCipher = streamCipher2;
        int n3 = n2 * 2 + digest.getDigestSize() + digest2.getDigestSize();
        byte[] arrby = TlsUtils.calculateKeyBlock(tlsContext, n3);
        TlsMac tlsMac = new TlsMac(tlsContext, digest, arrby, 0, digest.getDigestSize());
        int n4 = 0 + digest.getDigestSize();
        TlsMac tlsMac2 = new TlsMac(tlsContext, digest2, arrby, n4, digest2.getDigestSize());
        int n5 = n4 + digest2.getDigestSize();
        KeyParameter keyParameter2 = new KeyParameter(arrby, n5, n2);
        int n6 = n5 + n2;
        KeyParameter keyParameter3 = new KeyParameter(arrby, n6, n2);
        if (n6 + n2 != n3) {
            throw new TlsFatalAlert(80);
        }
        if (bl2) {
            this.writeMac = tlsMac2;
            this.readMac = tlsMac;
            this.encryptCipher = streamCipher2;
            this.decryptCipher = streamCipher;
            keyParameter = keyParameter3;
        } else {
            this.writeMac = tlsMac;
            this.readMac = tlsMac2;
            this.encryptCipher = streamCipher;
            this.decryptCipher = streamCipher2;
            keyParameter = keyParameter2;
            keyParameter2 = keyParameter3;
        }
        if (bl) {
            byte[] arrby2 = new byte[8];
            ParametersWithIV parametersWithIV = new ParametersWithIV(keyParameter, arrby2);
            ParametersWithIV parametersWithIV2 = new ParametersWithIV(keyParameter2, arrby2);
            ParametersWithIV parametersWithIV3 = parametersWithIV;
        } else {
            KeyParameter keyParameter4 = keyParameter2;
            KeyParameter keyParameter5 = keyParameter;
            KeyParameter keyParameter6 = keyParameter4;
        }
        this.encryptCipher.init(true, (CipherParameters)var20_25);
        this.decryptCipher.init(false, (CipherParameters)var21_22);
    }

    protected void checkMAC(long l2, short s2, byte[] arrby, int n2, int n3, byte[] arrby2, int n4, int n5) {
        if (!Arrays.constantTimeAreEqual((byte[])Arrays.copyOfRange((byte[])arrby, (int)n2, (int)n3), (byte[])this.readMac.calculateMac(l2, s2, arrby2, n4, n5))) {
            throw new TlsFatalAlert(20);
        }
    }

    @Override
    public byte[] decodeCiphertext(long l2, short s2, byte[] arrby, int n2, int n3) {
        int n4;
        if (this.usesNonce) {
            this.updateIV(this.decryptCipher, false, l2);
        }
        if (n3 < (n4 = this.readMac.getSize())) {
            throw new TlsFatalAlert(50);
        }
        int n5 = n3 - n4;
        byte[] arrby2 = new byte[n3];
        this.decryptCipher.processBytes(arrby, n2, n3, arrby2, 0);
        this.checkMAC(l2, s2, arrby2, n5, n3, arrby2, 0, n5);
        return Arrays.copyOfRange((byte[])arrby2, (int)0, (int)n5);
    }

    @Override
    public byte[] encodePlaintext(long l2, short s2, byte[] arrby, int n2, int n3) {
        if (this.usesNonce) {
            this.updateIV(this.encryptCipher, true, l2);
        }
        byte[] arrby2 = new byte[n3 + this.writeMac.getSize()];
        this.encryptCipher.processBytes(arrby, n2, n3, arrby2, 0);
        byte[] arrby3 = this.writeMac.calculateMac(l2, s2, arrby, n2, n3);
        this.encryptCipher.processBytes(arrby3, 0, arrby3.length, arrby2, n3);
        return arrby2;
    }

    @Override
    public int getPlaintextLimit(int n2) {
        return n2 - this.writeMac.getSize();
    }

    protected void updateIV(StreamCipher streamCipher, boolean bl, long l2) {
        byte[] arrby = new byte[8];
        TlsUtils.writeUint64(l2, arrby, 0);
        streamCipher.init(bl, new ParametersWithIV(null, arrby));
    }
}

