package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.Wrapper;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.Pack;

public class RFC5649WrapEngine implements Wrapper {
    private BlockCipher engine;
    private byte[] extractedAIV;
    private boolean forWrapping;
    private byte[] highOrderIV;
    private KeyParameter param;
    private byte[] preIV;

    public RFC5649WrapEngine(BlockCipher blockCipher) {
        this.highOrderIV = new byte[]{(byte) -90, (byte) 89, (byte) 89, (byte) -90};
        this.preIV = this.highOrderIV;
        this.extractedAIV = null;
        this.engine = blockCipher;
    }

    private byte[] padPlaintext(byte[] bArr) {
        int length = bArr.length;
        int i = (8 - (length % 8)) % 8;
        Object obj = new byte[(length + i)];
        System.arraycopy(bArr, 0, obj, 0, length);
        if (i != 0) {
            System.arraycopy(new byte[i], 0, obj, length, i);
        }
        return obj;
    }

    private byte[] rfc3394UnwrapNoIvCheck(byte[] bArr, int i, int i2) {
        byte[] bArr2 = new byte[8];
        Object obj = new byte[(i2 - bArr2.length)];
        Object obj2 = new byte[bArr2.length];
        Object obj3 = new byte[(bArr2.length + 8)];
        System.arraycopy(bArr, i, obj2, 0, bArr2.length);
        System.arraycopy(bArr, bArr2.length + i, obj, 0, i2 - bArr2.length);
        this.engine.init(false, this.param);
        int i3 = (i2 / 8) - 1;
        for (int i4 = 5; i4 >= 0; i4--) {
            for (int i5 = i3; i5 >= 1; i5--) {
                System.arraycopy(obj2, 0, obj3, 0, bArr2.length);
                System.arraycopy(obj, (i5 - 1) * 8, obj3, bArr2.length, 8);
                int i6 = (i3 * i4) + i5;
                int i7 = 1;
                while (i6 != 0) {
                    int length = bArr2.length - i7;
                    obj3[length] = (byte) (((byte) i6) ^ obj3[length]);
                    i6 >>>= 8;
                    i7++;
                }
                this.engine.processBlock(obj3, 0, obj3, 0);
                System.arraycopy(obj3, 0, obj2, 0, 8);
                System.arraycopy(obj3, 8, obj, (i5 - 1) * 8, 8);
            }
        }
        this.extractedAIV = obj2;
        return obj;
    }

    public String getAlgorithmName() {
        return this.engine.getAlgorithmName();
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        this.forWrapping = z;
        CipherParameters parameters = cipherParameters instanceof ParametersWithRandom ? ((ParametersWithRandom) cipherParameters).getParameters() : cipherParameters;
        if (parameters instanceof KeyParameter) {
            this.param = (KeyParameter) parameters;
        } else if (parameters instanceof ParametersWithIV) {
            this.preIV = ((ParametersWithIV) parameters).getIV();
            this.param = (KeyParameter) ((ParametersWithIV) parameters).getParameters();
            if (this.preIV.length != 4) {
                throw new IllegalArgumentException("IV length not equal to 4");
            }
        }
    }

    public byte[] unwrap(byte[] bArr, int i, int i2) {
        boolean z = true;
        if (this.forWrapping) {
            throw new IllegalStateException("not set for unwrapping");
        }
        int i3 = i2 / 8;
        if (i3 * 8 != i2) {
            throw new InvalidCipherTextException("unwrap data must be a multiple of 8 bytes");
        } else if (i3 == 1) {
            throw new InvalidCipherTextException("unwrap data must be at least 16 bytes");
        } else {
            Object obj;
            int length;
            boolean z2;
            Object obj2 = new byte[i2];
            System.arraycopy(bArr, i, obj2, 0, i2);
            Object obj3 = new byte[i2];
            if (i3 == 2) {
                this.engine.init(false, this.param);
                i3 = 0;
                while (i3 < obj2.length) {
                    this.engine.processBlock(obj2, i3, obj3, i3);
                    i3 += this.engine.getBlockSize();
                }
                this.extractedAIV = new byte[8];
                System.arraycopy(obj3, 0, this.extractedAIV, 0, this.extractedAIV.length);
                obj = new byte[(obj3.length - this.extractedAIV.length)];
                System.arraycopy(obj3, this.extractedAIV.length, obj, 0, obj.length);
            } else {
                obj = rfc3394UnwrapNoIvCheck(bArr, i, i2);
            }
            obj2 = new byte[4];
            obj3 = new byte[4];
            System.arraycopy(this.extractedAIV, 0, obj2, 0, obj2.length);
            System.arraycopy(this.extractedAIV, obj2.length, obj3, 0, obj3.length);
            int bigEndianToInt = Pack.bigEndianToInt(obj3, 0);
            if (!Arrays.constantTimeAreEqual(obj2, this.preIV)) {
                z = false;
            }
            int length2 = obj.length;
            if (bigEndianToInt <= length2 - 8) {
                z = false;
            }
            if (bigEndianToInt > length2) {
                z = false;
            }
            length2 -= bigEndianToInt;
            if (length2 >= obj.length) {
                length = obj.length;
                z2 = false;
            } else {
                int i4 = length2;
                z2 = z;
                length = i4;
            }
            byte[] bArr2 = new byte[length];
            Object obj4 = new byte[length];
            System.arraycopy(obj, obj.length - length, obj4, 0, length);
            if (!Arrays.constantTimeAreEqual(obj4, bArr2)) {
                z2 = false;
            }
            if (z2) {
                Object obj5 = new byte[bigEndianToInt];
                System.arraycopy(obj, 0, obj5, 0, obj5.length);
                return obj5;
            }
            throw new InvalidCipherTextException("checksum failed");
        }
    }

    public byte[] wrap(byte[] bArr, int i, int i2) {
        int i3 = 0;
        if (this.forWrapping) {
            Object obj = new byte[8];
            Object intToBigEndian = Pack.intToBigEndian(i2);
            System.arraycopy(this.preIV, 0, obj, 0, this.preIV.length);
            System.arraycopy(intToBigEndian, 0, obj, this.preIV.length, intToBigEndian.length);
            intToBigEndian = new byte[i2];
            System.arraycopy(bArr, i, intToBigEndian, 0, i2);
            Object padPlaintext = padPlaintext(intToBigEndian);
            if (padPlaintext.length == 8) {
                intToBigEndian = new byte[(padPlaintext.length + obj.length)];
                System.arraycopy(obj, 0, intToBigEndian, 0, obj.length);
                System.arraycopy(padPlaintext, 0, intToBigEndian, obj.length, padPlaintext.length);
                this.engine.init(true, this.param);
                while (i3 < intToBigEndian.length) {
                    this.engine.processBlock(intToBigEndian, i3, intToBigEndian, i3);
                    i3 += this.engine.getBlockSize();
                }
                return intToBigEndian;
            }
            Wrapper rFC3394WrapEngine = new RFC3394WrapEngine(this.engine);
            rFC3394WrapEngine.init(true, new ParametersWithIV(this.param, obj));
            return rFC3394WrapEngine.wrap(padPlaintext, i, padPlaintext.length);
        }
        throw new IllegalStateException("not set for wrapping");
    }
}
