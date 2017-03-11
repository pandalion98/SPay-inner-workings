package org.bouncycastle.crypto.modes;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.SkippingStreamCipher;
import org.bouncycastle.crypto.StreamBlockCipher;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.util.Pack;

public class SICBlockCipher extends StreamBlockCipher implements SkippingStreamCipher {
    private byte[] IV;
    private final int blockSize;
    private int byteCount;
    private final BlockCipher cipher;
    private byte[] counter;
    private byte[] counterOut;

    public SICBlockCipher(BlockCipher blockCipher) {
        super(blockCipher);
        this.cipher = blockCipher;
        this.blockSize = this.cipher.getBlockSize();
        this.IV = new byte[this.blockSize];
        this.counter = new byte[this.blockSize];
        this.counterOut = new byte[this.blockSize];
        this.byteCount = 0;
    }

    private void adjustCounter(long j) {
        int i = 5;
        long j2 = 0;
        long j3;
        if (j >= 0) {
            long j4 = (((long) this.byteCount) + j) / ((long) this.blockSize);
            if (j4 > 255) {
                j2 = j4;
                while (i >= 1) {
                    j3 = 1 << (i * 8);
                    while (j2 >= j3) {
                        incrementCounterPow2(i);
                        j2 -= j3;
                    }
                    i--;
                }
                incrementCounter((int) j2);
            } else {
                incrementCounter((int) j4);
            }
            this.byteCount = (int) ((((long) this.byteCount) + j) - (((long) this.blockSize) * j4));
            return;
        }
        j3 = ((-j) - ((long) this.byteCount)) / ((long) this.blockSize);
        if (j3 > 255) {
            long j5 = j3;
            for (int i2 = 5; i2 >= 1; i2--) {
                long j6 = 1 << (i2 * 8);
                while (j5 > j6) {
                    decrementCounterPow2(i2);
                    j5 -= j6;
                }
            }
            while (j2 != j5) {
                decrementCounter();
                j2++;
            }
        } else {
            while (j2 != j3) {
                decrementCounter();
                j2++;
            }
        }
        int i3 = (int) ((((long) this.byteCount) + j) + (((long) this.blockSize) * j3));
        if (i3 >= 0) {
            this.byteCount = 0;
            return;
        }
        decrementCounter();
        this.byteCount = i3 + this.blockSize;
    }

    private void decrementCounter() {
        int i;
        if (this.counter[0] == null) {
            i = 0;
            for (int length = this.counter.length - 1; length > 0; length--) {
                if (this.counter[length] != null) {
                    i = 1;
                }
            }
            if (i == 0) {
                throw new IllegalStateException("attempt to reduce counter past zero.");
            }
        }
        i = this.counter.length - 1;
        while (i >= 0) {
            byte[] bArr = this.counter;
            byte b = (byte) (bArr[i] - 1);
            bArr[i] = b;
            if (b == -1) {
                i--;
            } else {
                return;
            }
        }
    }

    private void decrementCounterPow2(int i) {
        if (this.counter[i] == null) {
            Object obj = null;
            for (int length = this.counter.length - (i + 1); length > 0; length--) {
                if (this.counter[length] != null) {
                    obj = 1;
                }
            }
            if (obj == null) {
                throw new IllegalStateException("attempt to reduce counter past zero.");
            }
        }
        int length2 = this.counter.length - (i + 1);
        while (length2 >= 0) {
            byte[] bArr = this.counter;
            byte b = (byte) (bArr[length2] - 1);
            bArr[length2] = b;
            if (b == -1) {
                length2--;
            } else {
                return;
            }
        }
    }

    private void incrementCounter() {
        int length = this.counter.length - 1;
        while (length >= 0) {
            byte[] bArr = this.counter;
            byte b = (byte) (bArr[length] + 1);
            bArr[length] = b;
            if (b == null) {
                length--;
            } else {
                return;
            }
        }
    }

    private void incrementCounter(int i) {
        byte b = this.counter[this.counter.length - 1];
        byte[] bArr = this.counter;
        int length = this.counter.length - 1;
        bArr[length] = (byte) (bArr[length] + i);
        if (b != null && this.counter[this.counter.length - 1] < b) {
            incrementCounterPow2(1);
        }
    }

    private void incrementCounterPow2(int i) {
        int length = this.counter.length - (i + 1);
        while (length >= 0) {
            byte[] bArr = this.counter;
            byte b = (byte) (bArr[length] + 1);
            bArr[length] = b;
            if (b == null) {
                length--;
            } else {
                return;
            }
        }
    }

    protected byte calculateByte(byte b) {
        if (this.byteCount == 0) {
            this.cipher.processBlock(this.counter, 0, this.counterOut, 0);
            byte[] bArr = this.counterOut;
            int i = this.byteCount;
            this.byteCount = i + 1;
            return (byte) (bArr[i] ^ b);
        }
        bArr = this.counterOut;
        i = this.byteCount;
        this.byteCount = i + 1;
        byte b2 = (byte) (bArr[i] ^ b);
        if (this.byteCount != this.counter.length) {
            return b2;
        }
        this.byteCount = 0;
        incrementCounter();
        return b2;
    }

    public String getAlgorithmName() {
        return this.cipher.getAlgorithmName() + "/SIC";
    }

    public int getBlockSize() {
        return this.cipher.getBlockSize();
    }

    public long getPosition() {
        Object obj = new byte[this.IV.length];
        System.arraycopy(this.counter, 0, obj, 0, obj.length);
        for (int length = obj.length - 1; length >= 1; length--) {
            int i = (obj[length] & GF2Field.MASK) - (this.IV[length] & GF2Field.MASK);
            if (i < 0) {
                int i2 = length - 1;
                obj[i2] = (byte) (obj[i2] - 1);
                i += SkeinMac.SKEIN_256;
            }
            obj[length] = (byte) i;
        }
        return (Pack.bigEndianToLong(obj, obj.length - 8) * ((long) this.blockSize)) + ((long) this.byteCount);
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
            System.arraycopy(parametersWithIV.getIV(), 0, this.IV, 0, this.IV.length);
            if (parametersWithIV.getParameters() != null) {
                this.cipher.init(true, parametersWithIV.getParameters());
            }
            reset();
            return;
        }
        throw new IllegalArgumentException("SIC mode requires ParametersWithIV");
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        processBytes(bArr, i, this.blockSize, bArr2, i2);
        return this.blockSize;
    }

    public void reset() {
        System.arraycopy(this.IV, 0, this.counter, 0, this.counter.length);
        this.cipher.reset();
        this.byteCount = 0;
    }

    public long seekTo(long j) {
        reset();
        return skip(j);
    }

    public long skip(long j) {
        adjustCounter(j);
        this.cipher.processBlock(this.counter, 0, this.counterOut, 0);
        return j;
    }
}
