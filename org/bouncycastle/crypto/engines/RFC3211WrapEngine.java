package org.bouncycastle.crypto.engines;

import java.security.SecureRandom;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

public class RFC3211WrapEngine implements Wrapper {
    private CBCBlockCipher engine;
    private boolean forWrapping;
    private ParametersWithIV param;
    private SecureRandom rand;

    public RFC3211WrapEngine(BlockCipher blockCipher) {
        this.engine = new CBCBlockCipher(blockCipher);
    }

    public String getAlgorithmName() {
        return this.engine.getUnderlyingCipher().getAlgorithmName() + "/RFC3211Wrap";
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.forWrapping = z;
        if (cipherParameters instanceof ParametersWithRandom) {
            ParametersWithRandom parametersWithRandom = (ParametersWithRandom) cipherParameters;
            this.rand = parametersWithRandom.getRandom();
            this.param = (ParametersWithIV) parametersWithRandom.getParameters();
            return;
        }
        if (z) {
            this.rand = new SecureRandom();
        }
        this.param = (ParametersWithIV) cipherParameters;
    }

    public byte[] unwrap(byte[] bArr, int i, int i2) {
        int i3 = 0;
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        int blockSize = this.engine.getBlockSize();
        if (i2 < blockSize * 2) {
            throw new InvalidCipherTextException("input too short");
        }
        int i4;
        Object obj = new byte[i2];
        Object obj2 = new byte[blockSize];
        System.arraycopy(bArr, i, obj, 0, i2);
        System.arraycopy(bArr, i, obj2, 0, obj2.length);
        this.engine.init(false, new ParametersWithIV(this.param.getParameters(), obj2));
        for (i4 = blockSize; i4 < obj.length; i4 += blockSize) {
            this.engine.processBlock(obj, i4, obj, i4);
        }
        System.arraycopy(obj, obj.length - obj2.length, obj2, 0, obj2.length);
        this.engine.init(false, new ParametersWithIV(this.param.getParameters(), obj2));
        this.engine.processBlock(obj, 0, obj, 0);
        this.engine.init(false, this.param);
        for (i4 = 0; i4 < obj.length; i4 += blockSize) {
            this.engine.processBlock(obj, i4, obj, i4);
        }
        if ((obj[0] & GF2Field.MASK) > obj.length - 4) {
            throw new InvalidCipherTextException("wrapped key corrupted");
        }
        Object obj3 = new byte[(obj[0] & GF2Field.MASK)];
        System.arraycopy(obj, 4, obj3, 0, obj[0]);
        i4 = 0;
        while (i3 != 3) {
            i4 |= ((byte) (obj[i3 + 1] ^ -1)) ^ obj3[i3];
            i3++;
        }
        if (i4 == 0) {
            return obj3;
        }
        throw new InvalidCipherTextException("wrapped key fails checksum");
    }

    public byte[] wrap(byte[] bArr, int i, int i2) {
        int i3 = 0;
        if (this.forWrapping) {
            byte[] bArr2;
            int i4;
            this.engine.init(true, this.param);
            int blockSize = this.engine.getBlockSize();
            if (i2 + 4 < blockSize * 2) {
                bArr2 = new byte[(blockSize * 2)];
            } else {
                bArr2 = new byte[((i2 + 4) % blockSize == 0 ? i2 + 4 : (((i2 + 4) / blockSize) + 1) * blockSize)];
            }
            bArr2[0] = (byte) i2;
            bArr2[1] = (byte) (bArr[i] ^ -1);
            bArr2[2] = (byte) (bArr[i + 1] ^ -1);
            bArr2[3] = (byte) (bArr[i + 2] ^ -1);
            System.arraycopy(bArr, i, bArr2, 4, i2);
            for (i4 = i2 + 4; i4 < bArr2.length; i4++) {
                bArr2[i4] = (byte) this.rand.nextInt();
            }
            for (i4 = 0; i4 < bArr2.length; i4 += blockSize) {
                this.engine.processBlock(bArr2, i4, bArr2, i4);
            }
            while (i3 < bArr2.length) {
                this.engine.processBlock(bArr2, i3, bArr2, i3);
                i3 += blockSize;
            }
            return bArr2;
        }
        throw new IllegalStateException("not set for wrapping");
    }
}
