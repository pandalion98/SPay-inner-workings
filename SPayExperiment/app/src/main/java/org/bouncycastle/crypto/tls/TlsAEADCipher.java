/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.System
 *  java.lang.Throwable
 *  org.bouncycastle.util.Arrays
 */
package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.tls.ProtocolVersion;
import org.bouncycastle.crypto.tls.TlsCipher;
import org.bouncycastle.crypto.tls.TlsContext;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.crypto.tls.TlsUtils;
import org.bouncycastle.util.Arrays;

public class TlsAEADCipher
implements TlsCipher {
    protected TlsContext context;
    protected AEADBlockCipher decryptCipher;
    protected byte[] decryptImplicitNonce;
    protected AEADBlockCipher encryptCipher;
    protected byte[] encryptImplicitNonce;
    protected int macSize;
    protected int nonce_explicit_length;

    /*
     * Enabled aggressive block sorting
     */
    public TlsAEADCipher(TlsContext tlsContext, AEADBlockCipher aEADBlockCipher, AEADBlockCipher aEADBlockCipher2, int n2, int n3) {
        if (!TlsUtils.isTLSv12(tlsContext)) {
            throw new TlsFatalAlert(80);
        }
        this.context = tlsContext;
        this.macSize = n3;
        this.nonce_explicit_length = 8;
        int n4 = 8 + n2 * 2;
        byte[] arrby = TlsUtils.calculateKeyBlock(tlsContext, n4);
        KeyParameter keyParameter = new KeyParameter(arrby, 0, n2);
        int n5 = 0 + n2;
        KeyParameter keyParameter2 = new KeyParameter(arrby, n5, n2);
        int n6 = n5 + n2;
        byte[] arrby2 = Arrays.copyOfRange((byte[])arrby, (int)n6, (int)(n6 + 4));
        int n7 = n6 + 4;
        byte[] arrby3 = Arrays.copyOfRange((byte[])arrby, (int)n7, (int)(n7 + 4));
        if (n7 + 4 != n4) {
            throw new TlsFatalAlert(80);
        }
        if (tlsContext.isServer()) {
            this.encryptCipher = aEADBlockCipher2;
            this.decryptCipher = aEADBlockCipher;
            this.encryptImplicitNonce = arrby3;
            this.decryptImplicitNonce = arrby2;
        } else {
            this.encryptCipher = aEADBlockCipher;
            this.decryptCipher = aEADBlockCipher2;
            this.encryptImplicitNonce = arrby2;
            this.decryptImplicitNonce = arrby3;
            KeyParameter keyParameter3 = keyParameter2;
            keyParameter2 = keyParameter;
            keyParameter = keyParameter3;
        }
        byte[] arrby4 = new byte[4 + this.nonce_explicit_length];
        this.encryptCipher.init(true, new AEADParameters(keyParameter2, n3 * 8, arrby4));
        this.decryptCipher.init(false, new AEADParameters(keyParameter, n3 * 8, arrby4));
    }

    @Override
    public byte[] decodeCiphertext(long l2, short s2, byte[] arrby, int n2, int n3) {
        byte[] arrby2;
        int n4;
        int n5;
        if (this.getPlaintextLimit(n3) < 0) {
            throw new TlsFatalAlert(50);
        }
        byte[] arrby3 = new byte[this.decryptImplicitNonce.length + this.nonce_explicit_length];
        System.arraycopy((Object)this.decryptImplicitNonce, (int)0, (Object)arrby3, (int)0, (int)this.decryptImplicitNonce.length);
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby3, (int)this.decryptImplicitNonce.length, (int)this.nonce_explicit_length);
        int n6 = n2 + this.nonce_explicit_length;
        int n7 = n3 - this.nonce_explicit_length;
        int n8 = this.decryptCipher.getOutputSize(n7);
        arrby2 = new byte[n8];
        byte[] arrby4 = this.getAdditionalData(l2, s2, n8);
        AEADParameters aEADParameters = new AEADParameters(null, 8 * this.macSize, arrby3, arrby4);
        try {
            this.decryptCipher.init(false, aEADParameters);
            n4 = 0 + this.decryptCipher.processBytes(arrby, n6, n7, arrby2, 0);
            n5 = this.decryptCipher.doFinal(arrby2, n4);
        }
        catch (Exception exception) {
            throw new TlsFatalAlert(20, exception);
        }
        if (n4 + n5 != arrby2.length) {
            throw new TlsFatalAlert(80);
        }
        return arrby2;
    }

    @Override
    public byte[] encodePlaintext(long l2, short s2, byte[] arrby, int n2, int n3) {
        byte[] arrby2;
        int n4;
        int n5;
        byte[] arrby3 = new byte[this.encryptImplicitNonce.length + this.nonce_explicit_length];
        System.arraycopy((Object)this.encryptImplicitNonce, (int)0, (Object)arrby3, (int)0, (int)this.encryptImplicitNonce.length);
        TlsUtils.writeUint64(l2, arrby3, this.encryptImplicitNonce.length);
        arrby2 = new byte[this.encryptCipher.getOutputSize(n3) + this.nonce_explicit_length];
        System.arraycopy((Object)arrby3, (int)this.encryptImplicitNonce.length, (Object)arrby2, (int)0, (int)this.nonce_explicit_length);
        int n6 = this.nonce_explicit_length;
        byte[] arrby4 = this.getAdditionalData(l2, s2, n3);
        AEADParameters aEADParameters = new AEADParameters(null, 8 * this.macSize, arrby3, arrby4);
        try {
            this.encryptCipher.init(true, aEADParameters);
            n5 = n6 + this.encryptCipher.processBytes(arrby, n2, n3, arrby2, n6);
            n4 = this.encryptCipher.doFinal(arrby2, n5);
        }
        catch (Exception exception) {
            throw new TlsFatalAlert(80, exception);
        }
        if (n5 + n4 != arrby2.length) {
            throw new TlsFatalAlert(80);
        }
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
        return n2 - this.macSize - this.nonce_explicit_length;
    }
}

