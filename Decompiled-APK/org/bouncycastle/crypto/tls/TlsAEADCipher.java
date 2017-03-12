package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.Arrays;

public class TlsAEADCipher implements TlsCipher {
    protected TlsContext context;
    protected AEADBlockCipher decryptCipher;
    protected byte[] decryptImplicitNonce;
    protected AEADBlockCipher encryptCipher;
    protected byte[] encryptImplicitNonce;
    protected int macSize;
    protected int nonce_explicit_length;

    public TlsAEADCipher(TlsContext tlsContext, AEADBlockCipher aEADBlockCipher, AEADBlockCipher aEADBlockCipher2, int i, int i2) {
        if (TlsUtils.isTLSv12(tlsContext)) {
            this.context = tlsContext;
            this.macSize = i2;
            this.nonce_explicit_length = 8;
            int i3 = (i * 2) + 8;
            byte[] calculateKeyBlock = TlsUtils.calculateKeyBlock(tlsContext, i3);
            KeyParameter keyParameter = new KeyParameter(calculateKeyBlock, 0, i);
            int i4 = 0 + i;
            KeyParameter keyParameter2 = new KeyParameter(calculateKeyBlock, i4, i);
            i4 += i;
            byte[] copyOfRange = Arrays.copyOfRange(calculateKeyBlock, i4, i4 + 4);
            i4 += 4;
            calculateKeyBlock = Arrays.copyOfRange(calculateKeyBlock, i4, i4 + 4);
            if (i4 + 4 != i3) {
                throw new TlsFatalAlert((short) 80);
            }
            if (tlsContext.isServer()) {
                this.encryptCipher = aEADBlockCipher2;
                this.decryptCipher = aEADBlockCipher;
                this.encryptImplicitNonce = calculateKeyBlock;
                this.decryptImplicitNonce = copyOfRange;
            } else {
                this.encryptCipher = aEADBlockCipher;
                this.decryptCipher = aEADBlockCipher2;
                this.encryptImplicitNonce = copyOfRange;
                this.decryptImplicitNonce = calculateKeyBlock;
                KeyParameter keyParameter3 = keyParameter2;
                keyParameter2 = keyParameter;
                keyParameter = keyParameter3;
            }
            byte[] bArr = new byte[(4 + this.nonce_explicit_length)];
            this.encryptCipher.init(true, new AEADParameters(keyParameter2, i2 * 8, bArr));
            this.decryptCipher.init(false, new AEADParameters(keyParameter, i2 * 8, bArr));
            return;
        }
        throw new TlsFatalAlert((short) 80);
    }

    public byte[] decodeCiphertext(long j, short s, byte[] bArr, int i, int i2) {
        if (getPlaintextLimit(i2) < 0) {
            throw new TlsFatalAlert((short) 50);
        }
        Object obj = new byte[(this.decryptImplicitNonce.length + this.nonce_explicit_length)];
        System.arraycopy(this.decryptImplicitNonce, 0, obj, 0, this.decryptImplicitNonce.length);
        System.arraycopy(bArr, i, obj, this.decryptImplicitNonce.length, this.nonce_explicit_length);
        int i3 = i + this.nonce_explicit_length;
        int i4 = i2 - this.nonce_explicit_length;
        int outputSize = this.decryptCipher.getOutputSize(i4);
        byte[] bArr2 = new byte[outputSize];
        try {
            this.decryptCipher.init(false, new AEADParameters(null, this.macSize * 8, obj, getAdditionalData(j, s, outputSize)));
            int processBytes = this.decryptCipher.processBytes(bArr, i3, i4, bArr2, 0) + 0;
            if (processBytes + this.decryptCipher.doFinal(bArr2, processBytes) == bArr2.length) {
                return bArr2;
            }
            throw new TlsFatalAlert((short) 80);
        } catch (Throwable e) {
            throw new TlsFatalAlert((short) 20, e);
        }
    }

    public byte[] encodePlaintext(long j, short s, byte[] bArr, int i, int i2) {
        Object obj = new byte[(this.encryptImplicitNonce.length + this.nonce_explicit_length)];
        System.arraycopy(this.encryptImplicitNonce, 0, obj, 0, this.encryptImplicitNonce.length);
        TlsUtils.writeUint64(j, obj, this.encryptImplicitNonce.length);
        Object obj2 = new byte[(this.encryptCipher.getOutputSize(i2) + this.nonce_explicit_length)];
        System.arraycopy(obj, this.encryptImplicitNonce.length, obj2, 0, this.nonce_explicit_length);
        int i3 = this.nonce_explicit_length;
        try {
            this.encryptCipher.init(true, new AEADParameters(null, this.macSize * 8, obj, getAdditionalData(j, s, i2)));
            int processBytes = this.encryptCipher.processBytes(bArr, i, i2, obj2, i3) + i3;
            if (processBytes + this.encryptCipher.doFinal(obj2, processBytes) == obj2.length) {
                return obj2;
            }
            throw new TlsFatalAlert((short) 80);
        } catch (Throwable e) {
            throw new TlsFatalAlert((short) 80, e);
        }
    }

    protected byte[] getAdditionalData(long j, short s, int i) {
        byte[] bArr = new byte[13];
        TlsUtils.writeUint64(j, bArr, 0);
        TlsUtils.writeUint8(s, bArr, 8);
        TlsUtils.writeVersion(this.context.getServerVersion(), bArr, 9);
        TlsUtils.writeUint16(i, bArr, 11);
        return bArr;
    }

    public int getPlaintextLimit(int i) {
        return (i - this.macSize) - this.nonce_explicit_length;
    }
}
