package org.bouncycastle.crypto.tls;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.engines.ChaChaEngine;
import org.bouncycastle.crypto.generators.Poly1305KeyGenerator;
import org.bouncycastle.crypto.macs.Poly1305;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class Chacha20Poly1305 implements TlsCipher {
    protected TlsContext context;
    protected ChaChaEngine decryptCipher;
    protected ChaChaEngine encryptCipher;

    public Chacha20Poly1305(TlsContext tlsContext) {
        if (TlsUtils.isTLSv12(tlsContext)) {
            this.context = tlsContext;
            byte[] calculateKeyBlock = TlsUtils.calculateKeyBlock(tlsContext, 64);
            CipherParameters keyParameter = new KeyParameter(calculateKeyBlock, 0, 32);
            CipherParameters keyParameter2 = new KeyParameter(calculateKeyBlock, 32, 32);
            this.encryptCipher = new ChaChaEngine(20);
            this.decryptCipher = new ChaChaEngine(20);
            if (!tlsContext.isServer()) {
                CipherParameters cipherParameters = keyParameter2;
                keyParameter2 = keyParameter;
                keyParameter = cipherParameters;
            }
            calculateKeyBlock = new byte[8];
            this.encryptCipher.init(true, new ParametersWithIV(keyParameter2, calculateKeyBlock));
            this.decryptCipher.init(false, new ParametersWithIV(keyParameter, calculateKeyBlock));
            return;
        }
        throw new TlsFatalAlert((short) 80);
    }

    protected byte[] calculateRecordMAC(KeyParameter keyParameter, byte[] bArr, byte[] bArr2, int i, int i2) {
        Mac poly1305 = new Poly1305();
        poly1305.init(keyParameter);
        updateRecordMAC(poly1305, bArr, 0, bArr.length);
        updateRecordMAC(poly1305, bArr2, i, i2);
        byte[] bArr3 = new byte[poly1305.getMacSize()];
        poly1305.doFinal(bArr3, 0);
        return bArr3;
    }

    public byte[] decodeCiphertext(long j, short s, byte[] bArr, int i, int i2) {
        if (getPlaintextLimit(i2) < 0) {
            throw new TlsFatalAlert((short) 50);
        }
        int i3 = i2 - 16;
        if (Arrays.constantTimeAreEqual(calculateRecordMAC(initRecordMAC(this.decryptCipher, false, j), getAdditionalData(j, s, i3), bArr, i, i3), Arrays.copyOfRange(bArr, i + i3, i + i2))) {
            byte[] bArr2 = new byte[i3];
            this.decryptCipher.processBytes(bArr, i, i3, bArr2, 0);
            return bArr2;
        }
        throw new TlsFatalAlert((short) 20);
    }

    public byte[] encodePlaintext(long j, short s, byte[] bArr, int i, int i2) {
        int i3 = i2 + 16;
        KeyParameter initRecordMAC = initRecordMAC(this.encryptCipher, true, j);
        Object obj = new byte[i3];
        this.encryptCipher.processBytes(bArr, i, i2, obj, 0);
        Object calculateRecordMAC = calculateRecordMAC(initRecordMAC, getAdditionalData(j, s, i2), obj, 0, i2);
        System.arraycopy(calculateRecordMAC, 0, obj, i2, calculateRecordMAC.length);
        return obj;
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
        return i - 16;
    }

    protected KeyParameter initRecordMAC(ChaChaEngine chaChaEngine, boolean z, long j) {
        byte[] bArr = new byte[8];
        TlsUtils.writeUint64(j, bArr, 0);
        chaChaEngine.init(z, new ParametersWithIV(null, bArr));
        Object obj = new byte[64];
        chaChaEngine.processBytes(obj, 0, obj.length, obj, 0);
        System.arraycopy(obj, 0, obj, 32, 16);
        KeyParameter keyParameter = new KeyParameter(obj, 16, 32);
        Poly1305KeyGenerator.clamp(keyParameter.getKey());
        return keyParameter;
    }

    protected void updateRecordMAC(Mac mac, byte[] bArr, int i, int i2) {
        mac.update(bArr, i, i2);
        byte[] longToLittleEndian = Pack.longToLittleEndian(((long) i2) & 4294967295L);
        mac.update(longToLittleEndian, 0, longToLittleEndian.length);
    }
}
