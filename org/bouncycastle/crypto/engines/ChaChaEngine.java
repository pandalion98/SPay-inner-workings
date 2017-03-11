package org.bouncycastle.crypto.engines;

import org.bouncycastle.util.Pack;

public class ChaChaEngine extends Salsa20Engine {
    public ChaChaEngine(int i) {
        super(i);
    }

    public static void chachaCore(int i, int[] iArr, int[] iArr2) {
        if (iArr.length != 16) {
            throw new IllegalArgumentException();
        } else if (iArr2.length != 16) {
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
                i2 += i6;
                i14 = Salsa20Engine.rotl(i14 ^ i2, 16);
                i10 += i14;
                i6 = Salsa20Engine.rotl(i6 ^ i10, 12);
                i2 += i6;
                i14 = Salsa20Engine.rotl(i14 ^ i2, 8);
                i10 += i14;
                i6 = Salsa20Engine.rotl(i6 ^ i10, 7);
                i3 += i7;
                i15 = Salsa20Engine.rotl(i15 ^ i3, 16);
                i11 += i15;
                i7 = Salsa20Engine.rotl(i7 ^ i11, 12);
                i3 += i7;
                i15 = Salsa20Engine.rotl(i15 ^ i3, 8);
                i11 += i15;
                i7 = Salsa20Engine.rotl(i7 ^ i11, 7);
                i4 += i8;
                i16 = Salsa20Engine.rotl(i16 ^ i4, 16);
                i12 += i16;
                i8 = Salsa20Engine.rotl(i8 ^ i12, 12);
                i4 += i8;
                i16 = Salsa20Engine.rotl(i16 ^ i4, 8);
                i12 += i16;
                i8 = Salsa20Engine.rotl(i8 ^ i12, 7);
                i5 += i9;
                i17 = Salsa20Engine.rotl(i17 ^ i5, 16);
                i13 += i17;
                i9 = Salsa20Engine.rotl(i9 ^ i13, 12);
                i5 += i9;
                i17 = Salsa20Engine.rotl(i17 ^ i5, 8);
                i13 += i17;
                i9 = Salsa20Engine.rotl(i9 ^ i13, 7);
                i2 += i7;
                i17 = Salsa20Engine.rotl(i17 ^ i2, 16);
                i12 += i17;
                i7 = Salsa20Engine.rotl(i7 ^ i12, 12);
                i2 += i7;
                i17 = Salsa20Engine.rotl(i17 ^ i2, 8);
                i12 += i17;
                i7 = Salsa20Engine.rotl(i7 ^ i12, 7);
                i3 += i8;
                i14 = Salsa20Engine.rotl(i14 ^ i3, 16);
                i13 += i14;
                i8 = Salsa20Engine.rotl(i8 ^ i13, 12);
                i3 += i8;
                i14 = Salsa20Engine.rotl(i14 ^ i3, 8);
                i13 += i14;
                i8 = Salsa20Engine.rotl(i8 ^ i13, 7);
                i4 += i9;
                i15 = Salsa20Engine.rotl(i15 ^ i4, 16);
                i10 += i15;
                i9 = Salsa20Engine.rotl(i9 ^ i10, 12);
                i4 += i9;
                i15 = Salsa20Engine.rotl(i15 ^ i4, 8);
                i10 += i15;
                i9 = Salsa20Engine.rotl(i9 ^ i10, 7);
                i5 += i6;
                i16 = Salsa20Engine.rotl(i16 ^ i5, 16);
                i11 += i16;
                i6 = Salsa20Engine.rotl(i6 ^ i11, 12);
                i5 += i6;
                i16 = Salsa20Engine.rotl(i16 ^ i5, 8);
                i11 += i16;
                i6 = Salsa20Engine.rotl(i6 ^ i11, 7);
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
        int i = iArr[12] + 1;
        iArr[12] = i;
        if (i == 0) {
            iArr = this.engineState;
            iArr[13] = iArr[13] + 1;
        }
    }

    protected void advanceCounter(long j) {
        int[] iArr;
        int i = (int) (j >>> 32);
        int i2 = (int) j;
        if (i > 0) {
            iArr = this.engineState;
            iArr[13] = i + iArr[13];
        }
        i = this.engineState[12];
        iArr = this.engineState;
        iArr[12] = i2 + iArr[12];
        if (i != 0 && this.engineState[12] < i) {
            int[] iArr2 = this.engineState;
            iArr2[13] = iArr2[13] + 1;
        }
    }

    protected void generateKeyStream(byte[] bArr) {
        chachaCore(this.rounds, this.engineState, this.x);
        Pack.intToLittleEndian(this.x, bArr, 0);
    }

    public String getAlgorithmName() {
        return "ChaCha" + this.rounds;
    }

    protected long getCounter() {
        return (((long) this.engineState[13]) << 32) | (((long) this.engineState[12]) & 4294967295L);
    }

    protected void resetCounter() {
        int[] iArr = this.engineState;
        this.engineState[13] = 0;
        iArr[12] = 0;
    }

    protected void retreatCounter() {
        if (this.engineState[12] == 0 && this.engineState[13] == 0) {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
        int[] iArr = this.engineState;
        int i = iArr[12] - 1;
        iArr[12] = i;
        if (i == -1) {
            iArr = this.engineState;
            iArr[13] = iArr[13] - 1;
        }
    }

    protected void retreatCounter(long j) {
        int i = (int) (j >>> 32);
        int i2 = (int) j;
        if (i != 0) {
            if ((((long) this.engineState[13]) & 4294967295L) >= (((long) i) & 4294967295L)) {
                int[] iArr = this.engineState;
                iArr[13] = iArr[13] - i;
            } else {
                throw new IllegalStateException("attempt to reduce counter past zero.");
            }
        }
        int[] iArr2;
        if ((((long) this.engineState[12]) & 4294967295L) >= (((long) i2) & 4294967295L)) {
            iArr2 = this.engineState;
            iArr2[12] = iArr2[12] - i2;
        } else if (this.engineState[13] != 0) {
            iArr2 = this.engineState;
            iArr2[13] = iArr2[13] - 1;
            iArr2 = this.engineState;
            iArr2[12] = iArr2[12] - i2;
        } else {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
    }

    protected void setKey(byte[] bArr, byte[] bArr2) {
        int i = 16;
        if (bArr != null) {
            if (bArr.length == 16 || bArr.length == 32) {
                byte[] bArr3;
                this.engineState[4] = Pack.littleEndianToInt(bArr, 0);
                this.engineState[5] = Pack.littleEndianToInt(bArr, 4);
                this.engineState[6] = Pack.littleEndianToInt(bArr, 8);
                this.engineState[7] = Pack.littleEndianToInt(bArr, 12);
                if (bArr.length == 32) {
                    bArr3 = sigma;
                } else {
                    bArr3 = tau;
                    i = 0;
                }
                this.engineState[8] = Pack.littleEndianToInt(bArr, i);
                this.engineState[9] = Pack.littleEndianToInt(bArr, i + 4);
                this.engineState[10] = Pack.littleEndianToInt(bArr, i + 8);
                this.engineState[11] = Pack.littleEndianToInt(bArr, i + 12);
                this.engineState[0] = Pack.littleEndianToInt(bArr3, 0);
                this.engineState[1] = Pack.littleEndianToInt(bArr3, 4);
                this.engineState[2] = Pack.littleEndianToInt(bArr3, 8);
                this.engineState[3] = Pack.littleEndianToInt(bArr3, 12);
            } else {
                throw new IllegalArgumentException(getAlgorithmName() + " requires 128 bit or 256 bit key");
            }
        }
        this.engineState[14] = Pack.littleEndianToInt(bArr2, 0);
        this.engineState[15] = Pack.littleEndianToInt(bArr2, 4);
    }
}
