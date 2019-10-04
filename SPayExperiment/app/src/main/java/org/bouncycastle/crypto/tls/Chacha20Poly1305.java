/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.System
 *  org.bouncycastle.util.Arrays
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.engines.ChaChaEngine;
import org.bouncycastle.crypto.generators.Poly1305KeyGenerator;
import org.bouncycastle.crypto.macs.Poly1305;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class Chacha20Poly1305
implements TlsCipher {
    protected TlsContext context;
    protected ChaChaEngine decryptCipher;
    protected ChaChaEngine encryptCipher;

    /*
     * Enabled aggressive block sorting
     */
    public Chacha20Poly1305(TlsContext tlsContext) {
        if (!TlsUtils.isTLSv12(tlsContext)) {
            throw new TlsFatalAlert(80);
        }
        this.context = tlsContext;
        byte[] arrby = TlsUtils.calculateKeyBlock(tlsContext, 64);
        KeyParameter keyParameter = new KeyParameter(arrby, 0, 32);
        KeyParameter keyParameter2 = new KeyParameter(arrby, 32, 32);
        this.encryptCipher = new ChaChaEngine(20);
        this.decryptCipher = new ChaChaEngine(20);
        if (!tlsContext.isServer()) {
            KeyParameter keyParameter3 = keyParameter2;
            keyParameter2 = keyParameter;
            keyParameter = keyParameter3;
        }
        byte[] arrby2 = new byte[8];
        this.encryptCipher.init(true, new ParametersWithIV(keyParameter2, arrby2));
        this.decryptCipher.init(false, new ParametersWithIV(keyParameter, arrby2));
    }

    protected byte[] calculateRecordMAC(KeyParameter keyParameter, byte[] arrby, byte[] arrby2, int n2, int n3) {
        Poly1305 poly1305 = new Poly1305();
        poly1305.init(keyParameter);
        this.updateRecordMAC(poly1305, arrby, 0, arrby.length);
        this.updateRecordMAC(poly1305, arrby2, n2, n3);
        byte[] arrby3 = new byte[poly1305.getMacSize()];
        poly1305.doFinal(arrby3, 0);
        return arrby3;
    }

    @Override
    public byte[] decodeCiphertext(long l2, short s2, byte[] arrby, int n2, int n3) {
        if (this.getPlaintextLimit(n3) < 0) {
            throw new TlsFatalAlert(50);
        }
        int n4 = n3 - 16;
        byte[] arrby2 = Arrays.copyOfRange((byte[])arrby, (int)(n2 + n4), (int)(n2 + n3));
        if (!Arrays.constantTimeAreEqual((byte[])this.calculateRecordMAC(this.initRecordMAC(this.decryptCipher, false, l2), this.getAdditionalData(l2, s2, n4), arrby, n2, n4), (byte[])arrby2)) {
            throw new TlsFatalAlert(20);
        }
        byte[] arrby3 = new byte[n4];
        this.decryptCipher.processBytes(arrby, n2, n4, arrby3, 0);
        return arrby3;
    }

    @Override
    public byte[] encodePlaintext(long l2, short s2, byte[] arrby, int n2, int n3) {
        int n4 = n3 + 16;
        KeyParameter keyParameter = this.initRecordMAC(this.encryptCipher, true, l2);
        byte[] arrby2 = new byte[n4];
        this.encryptCipher.processBytes(arrby, n2, n3, arrby2, 0);
        byte[] arrby3 = this.calculateRecordMAC(keyParameter, this.getAdditionalData(l2, s2, n3), arrby2, 0, n3);
        System.arraycopy((Object)arrby3, (int)0, (Object)arrby2, (int)n3, (int)arrby3.length);
        return arrby2;
    }

    protected byte[] getAdditionalData(long l2, short s2, int n2) {
        byte[] arrby = new byte[13];
        TlsUtils.writeUint64(l2, arrby, 0);
        TlsUtils.writeUint8(s2, arrby, 8);
        TlsUtils.writeVersion(this.context.getServerVersion(), arrby, 9);
        TlsUtils.writeUint16(n2, arrby, 11);
        return arrby;
    }

    @Override
    public int getPlaintextLimit(int n2) {
        return n2 - 16;
    }

    protected KeyParameter initRecordMAC(ChaChaEngine chaChaEngine, boolean bl, long l2) {
        byte[] arrby = new byte[8];
        TlsUtils.writeUint64(l2, arrby, 0);
        chaChaEngine.init(bl, new ParametersWithIV(null, arrby));
        byte[] arrby2 = new byte[64];
        chaChaEngine.processBytes(arrby2, 0, arrby2.length, arrby2, 0);
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby2, (int)32, (int)16);
        KeyParameter keyParameter = new KeyParameter(arrby2, 16, 32);
        Poly1305KeyGenerator.clamp(keyParameter.getKey());
        return keyParameter;
    }

    protected void updateRecordMAC(Mac mac, byte[] arrby, int n2, int n3) {
        mac.update(arrby, n2, n3);
        byte[] arrby2 = Pack.longToLittleEndian((long)(0xFFFFFFFFL & (long)n3));
        mac.update(arrby2, 0, arrby2.length);
    }
}

