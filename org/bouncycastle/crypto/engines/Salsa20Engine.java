package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.MaxBytesExceededException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.SkippingStreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.util.Pack;
import org.bouncycastle.util.Strings;

public class Salsa20Engine implements SkippingStreamCipher {
    public static final int DEFAULT_ROUNDS = 20;
    private static final int STATE_SIZE = 16;
    protected static final byte[] sigma;
    protected static final byte[] tau;
    private int cW0;
    private int cW1;
    private int cW2;
    protected int[] engineState;
    private int index;
    private boolean initialised;
    private byte[] keyStream;
    protected int rounds;
    protected int[] f150x;

    static {
        sigma = Strings.toByteArray("expand 32-byte k");
        tau = Strings.toByteArray("expand 16-byte k");
    }

    public Salsa20Engine() {
        this(DEFAULT_ROUNDS);
    }

    public Salsa20Engine(int i) {
        this.index = 0;
        this.engineState = new int[STATE_SIZE];
        this.f150x = new int[STATE_SIZE];
        this.keyStream = new byte[64];
        this.initialised = false;
        if (i <= 0 || (i & 1) != 0) {
            throw new IllegalArgumentException("'rounds' must be a positive, even number");
        }
        this.rounds = i;
    }

    private boolean limitExceeded() {
        int i = this.cW0 + 1;
        this.cW0 = i;
        if (i != 0) {
            return false;
        }
        i = this.cW1 + 1;
        this.cW1 = i;
        if (i != 0) {
            return false;
        }
        i = this.cW2 + 1;
        this.cW2 = i;
        return (i & 32) != 0;
    }

    private boolean limitExceeded(int i) {
        this.cW0 += i;
        if (this.cW0 >= i || this.cW0 < 0) {
            return false;
        }
        int i2 = this.cW1 + 1;
        this.cW1 = i2;
        if (i2 != 0) {
            return false;
        }
        i2 = this.cW2 + 1;
        this.cW2 = i2;
        return (i2 & 32) != 0;
    }

    private void resetLimitCounter() {
        this.cW0 = 0;
        this.cW1 = 0;
        this.cW2 = 0;
    }

    protected static int rotl(int i, int i2) {
        return (i << i2) | (i >>> (-i2));
    }

    public static void salsaCore(int i, int[] iArr, int[] iArr2) {
        if (iArr.length != STATE_SIZE) {
            throw new IllegalArgumentException();
        } else if (iArr2.length != STATE_SIZE) {
            throw new IllegalArgumentException();
        } else if (i % 2 != 0) {
            throw new IllegalArgumentException("Number of rounds must be even");
        } else {
            int i2 = iArr[0];
            int i3 = iArr[1];
            int i4 = iArr[2];
            int i5 = iArr[3];
            int i6 = iArr[4];
            int i7 = iArr[5];
            int i8 = iArr[6];
            int i9 = iArr[7];
            int i10 = iArr[8];
            int i11 = iArr[9];
            int i12 = iArr[10];
            int i13 = iArr[11];
            int i14 = iArr[12];
            int i15 = iArr[13];
            int i16 = iArr[14];
            int i17 = iArr[15];
            while (i > 0) {
                i6 ^= rotl(i2 + i14, 7);
                i10 ^= rotl(i6 + i2, 9);
                i14 ^= rotl(i10 + i6, 13);
                i2 ^= rotl(i14 + i10, 18);
                i11 ^= rotl(i7 + i3, 7);
                i15 ^= rotl(i11 + i7, 9);
                i3 ^= rotl(i15 + i11, 13);
                i7 ^= rotl(i3 + i15, 18);
                i16 ^= rotl(i12 + i8, 7);
                i4 ^= rotl(i16 + i12, 9);
                i8 ^= rotl(i4 + i16, 13);
                i12 ^= rotl(i8 + i4, 18);
                i5 ^= rotl(i17 + i13, 7);
                i9 ^= rotl(i5 + i17, 9);
                i13 ^= rotl(i9 + i5, 13);
                i17 ^= rotl(i13 + i9, 18);
                i3 ^= rotl(i2 + i5, 7);
                i4 ^= rotl(i3 + i2, 9);
                i5 ^= rotl(i4 + i3, 13);
                i2 ^= rotl(i5 + i4, 18);
                i8 ^= rotl(i7 + i6, 7);
                i9 ^= rotl(i8 + i7, 9);
                i6 ^= rotl(i9 + i8, 13);
                i7 ^= rotl(i6 + i9, 18);
                i13 ^= rotl(i12 + i11, 7);
                i10 ^= rotl(i13 + i12, 9);
                i11 ^= rotl(i10 + i13, 13);
                i12 ^= rotl(i11 + i10, 18);
                i14 ^= rotl(i17 + i16, 7);
                i15 ^= rotl(i14 + i17, 9);
                i16 ^= rotl(i15 + i14, 13);
                i17 ^= rotl(i16 + i15, 18);
                i -= 2;
            }
            iArr2[0] = i2 + iArr[0];
            iArr2[1] = i3 + iArr[1];
            iArr2[2] = i4 + iArr[2];
            iArr2[3] = i5 + iArr[3];
            iArr2[4] = i6 + iArr[4];
            iArr2[5] = i7 + iArr[5];
            iArr2[6] = i8 + iArr[6];
            iArr2[7] = i9 + iArr[7];
            iArr2[8] = i10 + iArr[8];
            iArr2[9] = i11 + iArr[9];
            iArr2[10] = i12 + iArr[10];
            iArr2[11] = i13 + iArr[11];
            iArr2[12] = i14 + iArr[12];
            iArr2[13] = i15 + iArr[13];
            iArr2[14] = i16 + iArr[14];
            iArr2[15] = i17 + iArr[15];
        }
    }

    protected void advanceCounter() {
        int[] iArr = this.engineState;
        int i = iArr[8] + 1;
        iArr[8] = i;
        if (i == 0) {
            iArr = this.engineState;
            iArr[9] = iArr[9] + 1;
        }
    }

    protected void advanceCounter(long j) {
        int[] iArr;
        int i = (int) (j >>> 32);
        int i2 = (int) j;
        if (i > 0) {
            iArr = this.engineState;
            iArr[9] = i + iArr[9];
        }
        i = this.engineState[8];
        iArr = this.engineState;
        iArr[8] = i2 + iArr[8];
        if (i != 0 && this.engineState[8] < i) {
            int[] iArr2 = this.engineState;
            iArr2[9] = iArr2[9] + 1;
        }
    }

    protected void generateKeyStream(byte[] bArr) {
        salsaCore(this.rounds, this.engineState, this.f150x);
        Pack.intToLittleEndian(this.f150x, bArr, 0);
    }

    public String getAlgorithmName() {
        String str = "Salsa20";
        return this.rounds != DEFAULT_ROUNDS ? str + "/" + this.rounds : str;
    }

    protected long getCounter() {
        return (((long) this.engineState[9]) << 32) | (((long) this.engineState[8]) & 4294967295L);
    }

    protected int getNonceSize() {
        return 8;
    }

    public long getPosition() {
        return (getCounter() * 64) + ((long) this.index);
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        if (cipherParameters instanceof ParametersWithIV) {
            ParametersWithIV parametersWithIV = (ParametersWithIV) cipherParameters;
            byte[] iv = parametersWithIV.getIV();
            if (iv == null || iv.length != getNonceSize()) {
                throw new IllegalArgumentException(getAlgorithmName() + " requires exactly " + getNonceSize() + " bytes of IV");
            }
            CipherParameters parameters = parametersWithIV.getParameters();
            if (parameters == null) {
                if (this.initialised) {
                    setKey(null, iv);
                } else {
                    throw new IllegalStateException(getAlgorithmName() + " KeyParameter can not be null for first initialisation");
                }
            } else if (parameters instanceof KeyParameter) {
                setKey(((KeyParameter) parameters).getKey(), iv);
            } else {
                throw new IllegalArgumentException(getAlgorithmName() + " Init parameters must contain a KeyParameter (or null for re-init)");
            }
            reset();
            this.initialised = true;
            return;
        }
        throw new IllegalArgumentException(getAlgorithmName() + " Init parameters must include an IV");
    }

    public int processBytes(byte[] bArr, int i, int i2, byte[] bArr2, int i3) {
        if (!this.initialised) {
            throw new IllegalStateException(getAlgorithmName() + " not initialised");
        } else if (i + i2 > bArr.length) {
            throw new DataLengthException("input buffer too short");
        } else if (i3 + i2 > bArr2.length) {
            throw new OutputLengthException("output buffer too short");
        } else if (limitExceeded(i2)) {
            throw new MaxBytesExceededException("2^70 byte limit per IV would be exceeded; Change IV");
        } else {
            for (int i4 = 0; i4 < i2; i4++) {
                bArr2[i4 + i3] = (byte) (this.keyStream[this.index] ^ bArr[i4 + i]);
                this.index = (this.index + 1) & 63;
                if (this.index == 0) {
                    advanceCounter();
                    generateKeyStream(this.keyStream);
                }
            }
            return i2;
        }
    }

    public void reset() {
        this.index = 0;
        resetLimitCounter();
        resetCounter();
        generateKeyStream(this.keyStream);
    }

    protected void resetCounter() {
        int[] iArr = this.engineState;
        this.engineState[9] = 0;
        iArr[8] = 0;
    }

    protected void retreatCounter() {
        if (this.engineState[8] == 0 && this.engineState[9] == 0) {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
        int[] iArr = this.engineState;
        int i = iArr[8] - 1;
        iArr[8] = i;
        if (i == -1) {
            iArr = this.engineState;
            iArr[9] = iArr[9] - 1;
        }
    }

    protected void retreatCounter(long j) {
        int i = (int) (j >>> 32);
        int i2 = (int) j;
        if (i != 0) {
            if ((((long) this.engineState[9]) & 4294967295L) >= (((long) i) & 4294967295L)) {
                int[] iArr = this.engineState;
                iArr[9] = iArr[9] - i;
            } else {
                throw new IllegalStateException("attempt to reduce counter past zero.");
            }
        }
        int[] iArr2;
        if ((((long) this.engineState[8]) & 4294967295L) >= (((long) i2) & 4294967295L)) {
            iArr2 = this.engineState;
            iArr2[8] = iArr2[8] - i2;
        } else if (this.engineState[9] != 0) {
            iArr2 = this.engineState;
            iArr2[9] = iArr2[9] - 1;
            iArr2 = this.engineState;
            iArr2[8] = iArr2[8] - i2;
        } else {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
    }

    public byte returnByte(byte b) {
        if (limitExceeded()) {
            throw new MaxBytesExceededException("2^70 byte limit per IV; Change IV");
        }
        byte b2 = (byte) (this.keyStream[this.index] ^ b);
        this.index = (this.index + 1) & 63;
        if (this.index == 0) {
            advanceCounter();
            generateKeyStream(this.keyStream);
        }
        return b2;
    }

    public long seekTo(long j) {
        reset();
        return skip(j);
    }

    protected void setKey(byte[] bArr, byte[] bArr2) {
        int i = STATE_SIZE;
        if (bArr != null) {
            if (bArr.length == STATE_SIZE || bArr.length == 32) {
                byte[] bArr3;
                this.engineState[1] = Pack.littleEndianToInt(bArr, 0);
                this.engineState[2] = Pack.littleEndianToInt(bArr, 4);
                this.engineState[3] = Pack.littleEndianToInt(bArr, 8);
                this.engineState[4] = Pack.littleEndianToInt(bArr, 12);
                if (bArr.length == 32) {
                    bArr3 = sigma;
                } else {
                    bArr3 = tau;
                    i = 0;
                }
                this.engineState[11] = Pack.littleEndianToInt(bArr, i);
                this.engineState[12] = Pack.littleEndianToInt(bArr, i + 4);
                this.engineState[13] = Pack.littleEndianToInt(bArr, i + 8);
                this.engineState[14] = Pack.littleEndianToInt(bArr, i + 12);
                this.engineState[0] = Pack.littleEndianToInt(bArr3, 0);
                this.engineState[5] = Pack.littleEndianToInt(bArr3, 4);
                this.engineState[10] = Pack.littleEndianToInt(bArr3, 8);
                this.engineState[15] = Pack.littleEndianToInt(bArr3, 12);
            } else {
                throw new IllegalArgumentException(getAlgorithmName() + " requires 128 bit or 256 bit key");
            }
        }
        this.engineState[6] = Pack.littleEndianToInt(bArr2, 0);
        this.engineState[7] = Pack.littleEndianToInt(bArr2, 4);
    }

    public long skip(long j) {
        long j2 = 0;
        long j3;
        if (j >= 0) {
            if (j >= 64) {
                j3 = j / 64;
                advanceCounter(j3);
                j3 = j - (j3 * 64);
            } else {
                j3 = j;
            }
            int i = this.index;
            this.index = (((int) j3) + this.index) & 63;
            if (this.index < i) {
                advanceCounter();
            }
        } else {
            j3 = -j;
            if (j3 >= 64) {
                long j4 = j3 / 64;
                retreatCounter(j4);
                j3 -= j4 * 64;
            }
            while (j2 < j3) {
                if (this.index == 0) {
                    retreatCounter();
                }
                this.index = (this.index - 1) & 63;
                j2++;
            }
        }
        generateKeyStream(this.keyStream);
        return j;
    }
}
