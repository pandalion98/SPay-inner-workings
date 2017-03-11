package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.TweakableBlockCipherParameters;

public class ThreefishEngine implements BlockCipher {
    public static final int BLOCKSIZE_1024 = 1024;
    public static final int BLOCKSIZE_256 = 256;
    public static final int BLOCKSIZE_512 = 512;
    private static final long C_240 = 2004413935125273122L;
    private static final int MAX_ROUNDS = 80;
    private static int[] MOD17 = null;
    private static int[] MOD3 = null;
    private static int[] MOD5 = null;
    private static int[] MOD9 = null;
    private static final int ROUNDS_1024 = 80;
    private static final int ROUNDS_256 = 72;
    private static final int ROUNDS_512 = 72;
    private static final int TWEAK_SIZE_BYTES = 16;
    private static final int TWEAK_SIZE_WORDS = 2;
    private int blocksizeBytes;
    private int blocksizeWords;
    private ThreefishCipher cipher;
    private long[] currentBlock;
    private boolean forEncryption;
    private long[] kw;
    private long[] f167t;

    private static abstract class ThreefishCipher {
        protected final long[] kw;
        protected final long[] f166t;

        protected ThreefishCipher(long[] jArr, long[] jArr2) {
            this.kw = jArr;
            this.f166t = jArr2;
        }

        abstract void decryptBlock(long[] jArr, long[] jArr2);

        abstract void encryptBlock(long[] jArr, long[] jArr2);
    }

    private static final class Threefish1024Cipher extends ThreefishCipher {
        private static final int ROTATION_0_0 = 24;
        private static final int ROTATION_0_1 = 13;
        private static final int ROTATION_0_2 = 8;
        private static final int ROTATION_0_3 = 47;
        private static final int ROTATION_0_4 = 8;
        private static final int ROTATION_0_5 = 17;
        private static final int ROTATION_0_6 = 22;
        private static final int ROTATION_0_7 = 37;
        private static final int ROTATION_1_0 = 38;
        private static final int ROTATION_1_1 = 19;
        private static final int ROTATION_1_2 = 10;
        private static final int ROTATION_1_3 = 55;
        private static final int ROTATION_1_4 = 49;
        private static final int ROTATION_1_5 = 18;
        private static final int ROTATION_1_6 = 23;
        private static final int ROTATION_1_7 = 52;
        private static final int ROTATION_2_0 = 33;
        private static final int ROTATION_2_1 = 4;
        private static final int ROTATION_2_2 = 51;
        private static final int ROTATION_2_3 = 13;
        private static final int ROTATION_2_4 = 34;
        private static final int ROTATION_2_5 = 41;
        private static final int ROTATION_2_6 = 59;
        private static final int ROTATION_2_7 = 17;
        private static final int ROTATION_3_0 = 5;
        private static final int ROTATION_3_1 = 20;
        private static final int ROTATION_3_2 = 48;
        private static final int ROTATION_3_3 = 41;
        private static final int ROTATION_3_4 = 47;
        private static final int ROTATION_3_5 = 28;
        private static final int ROTATION_3_6 = 16;
        private static final int ROTATION_3_7 = 25;
        private static final int ROTATION_4_0 = 41;
        private static final int ROTATION_4_1 = 9;
        private static final int ROTATION_4_2 = 37;
        private static final int ROTATION_4_3 = 31;
        private static final int ROTATION_4_4 = 12;
        private static final int ROTATION_4_5 = 47;
        private static final int ROTATION_4_6 = 44;
        private static final int ROTATION_4_7 = 30;
        private static final int ROTATION_5_0 = 16;
        private static final int ROTATION_5_1 = 34;
        private static final int ROTATION_5_2 = 56;
        private static final int ROTATION_5_3 = 51;
        private static final int ROTATION_5_4 = 4;
        private static final int ROTATION_5_5 = 53;
        private static final int ROTATION_5_6 = 42;
        private static final int ROTATION_5_7 = 41;
        private static final int ROTATION_6_0 = 31;
        private static final int ROTATION_6_1 = 44;
        private static final int ROTATION_6_2 = 47;
        private static final int ROTATION_6_3 = 46;
        private static final int ROTATION_6_4 = 19;
        private static final int ROTATION_6_5 = 42;
        private static final int ROTATION_6_6 = 44;
        private static final int ROTATION_6_7 = 25;
        private static final int ROTATION_7_0 = 9;
        private static final int ROTATION_7_1 = 48;
        private static final int ROTATION_7_2 = 35;
        private static final int ROTATION_7_3 = 52;
        private static final int ROTATION_7_4 = 23;
        private static final int ROTATION_7_5 = 31;
        private static final int ROTATION_7_6 = 37;
        private static final int ROTATION_7_7 = 20;

        public Threefish1024Cipher(long[] jArr, long[] jArr2) {
            super(jArr, jArr2);
        }

        void decryptBlock(long[] jArr, long[] jArr2) {
            long[] jArr3 = this.kw;
            long[] jArr4 = this.t;
            int[] access$300 = ThreefishEngine.MOD17;
            int[] access$100 = ThreefishEngine.MOD3;
            if (jArr3.length != ROTATION_2_0) {
                throw new IllegalArgumentException();
            } else if (jArr4.length != ROTATION_3_0) {
                throw new IllegalArgumentException();
            } else {
                long j = jArr[0];
                long j2 = jArr[1];
                long j3 = jArr[ThreefishEngine.TWEAK_SIZE_WORDS];
                long j4 = jArr[3];
                long j5 = jArr[ROTATION_5_4];
                long j6 = jArr[ROTATION_3_0];
                long j7 = jArr[6];
                long j8 = jArr[7];
                long j9 = jArr[ROTATION_0_4];
                long j10 = jArr[ROTATION_7_0];
                long j11 = jArr[ROTATION_1_2];
                long j12 = jArr[11];
                long j13 = jArr[ROTATION_4_4];
                long j14 = jArr[ROTATION_2_3];
                long j15 = jArr[14];
                long j16 = jArr[15];
                for (int i = ROTATION_6_4; i >= 1; i -= 2) {
                    int i2 = access$300[i];
                    int i3 = access$100[i];
                    j -= jArr3[i2 + 1];
                    j2 -= jArr3[i2 + ThreefishEngine.TWEAK_SIZE_WORDS];
                    j3 -= jArr3[i2 + 3];
                    j4 -= jArr3[i2 + ROTATION_5_4];
                    j5 -= jArr3[i2 + ROTATION_3_0];
                    j6 -= jArr3[i2 + 6];
                    j7 -= jArr3[i2 + 7];
                    j8 -= jArr3[i2 + ROTATION_0_4];
                    j9 -= jArr3[i2 + ROTATION_7_0];
                    j10 -= jArr3[i2 + ROTATION_1_2];
                    j11 -= jArr3[i2 + 11];
                    j12 -= jArr3[i2 + ROTATION_4_4];
                    j13 -= jArr3[i2 + ROTATION_2_3];
                    j14 -= jArr3[i2 + 14] + jArr4[i3 + 1];
                    j15 -= jArr3[i2 + 15] + jArr4[i3 + ThreefishEngine.TWEAK_SIZE_WORDS];
                    j16 = ThreefishEngine.xorRotr(j16 - ((jArr3[i2 + ROTATION_5_0] + ((long) i)) + 1), ROTATION_7_0, j);
                    j -= j16;
                    j12 = ThreefishEngine.xorRotr(j12, ROTATION_7_1, j3);
                    j3 -= j12;
                    j14 = ThreefishEngine.xorRotr(j14, ROTATION_7_2, j7);
                    j7 -= j14;
                    j10 = ThreefishEngine.xorRotr(j10, ROTATION_7_3, j5);
                    j5 -= j10;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_7_4, j15);
                    j15 -= j2;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_7_5, j9);
                    j9 -= j6;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_7_6, j11);
                    j11 -= j4;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_7_7, j13);
                    j13 -= j8;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_7_5, j);
                    j -= j8;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_6_6, j3);
                    j3 -= j6;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_6_2, j5);
                    j5 -= j4;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_6_3, j7);
                    j7 -= j2;
                    j16 = ThreefishEngine.xorRotr(j16, ROTATION_6_4, j13);
                    j13 -= j16;
                    j14 = ThreefishEngine.xorRotr(j14, ROTATION_6_5, j15);
                    j15 -= j14;
                    j12 = ThreefishEngine.xorRotr(j12, ROTATION_6_6, j9);
                    j9 -= j12;
                    j10 = ThreefishEngine.xorRotr(j10, ROTATION_6_7, j11);
                    j11 -= j10;
                    j10 = ThreefishEngine.xorRotr(j10, ROTATION_5_0, j);
                    j -= j10;
                    j14 = ThreefishEngine.xorRotr(j14, ROTATION_5_1, j3);
                    j3 -= j14;
                    j12 = ThreefishEngine.xorRotr(j12, ROTATION_5_2, j7);
                    j7 -= j12;
                    j16 = ThreefishEngine.xorRotr(j16, ROTATION_5_3, j5);
                    j5 -= j16;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_5_4, j11);
                    j11 -= j8;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_5_5, j13);
                    j13 -= j4;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_6_5, j15);
                    j15 -= j6;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_5_7, j9);
                    j9 -= j2;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_5_7, j);
                    j -= j2;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_7_0, j3);
                    j3 -= j4;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_7_6, j5);
                    j5 -= j6;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_7_5, j7);
                    j7 -= j8;
                    j10 = ThreefishEngine.xorRotr(j10, ROTATION_4_4, j9);
                    j9 -= j10;
                    j12 = ThreefishEngine.xorRotr(j12, ROTATION_6_2, j11);
                    j11 -= j12;
                    j14 = ThreefishEngine.xorRotr(j14, ROTATION_6_6, j13);
                    j13 -= j14;
                    j16 = ThreefishEngine.xorRotr(j16, ROTATION_4_7, j15);
                    j -= jArr3[i2];
                    j2 -= jArr3[i2 + 1];
                    j3 -= jArr3[i2 + ThreefishEngine.TWEAK_SIZE_WORDS];
                    j4 -= jArr3[i2 + 3];
                    j5 -= jArr3[i2 + ROTATION_5_4];
                    j6 -= jArr3[i2 + ROTATION_3_0];
                    j7 -= jArr3[i2 + 6];
                    j8 -= jArr3[i2 + 7];
                    j9 -= jArr3[i2 + ROTATION_0_4];
                    j10 -= jArr3[i2 + ROTATION_7_0];
                    j11 -= jArr3[i2 + ROTATION_1_2];
                    j12 -= jArr3[i2 + 11];
                    j13 -= jArr3[i2 + ROTATION_4_4];
                    j14 -= jArr3[i2 + ROTATION_2_3] + jArr4[i3];
                    j15 = (j15 - j16) - (jArr4[i3 + 1] + jArr3[i2 + 14]);
                    j16 = ThreefishEngine.xorRotr(j16 - (jArr3[i2 + 15] + ((long) i)), ROTATION_3_0, j);
                    j -= j16;
                    j12 = ThreefishEngine.xorRotr(j12, ROTATION_7_7, j3);
                    j3 -= j12;
                    j14 = ThreefishEngine.xorRotr(j14, ROTATION_7_1, j7);
                    j7 -= j14;
                    j10 = ThreefishEngine.xorRotr(j10, ROTATION_5_7, j5);
                    j5 -= j10;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_6_2, j15);
                    j15 -= j2;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_3_5, j9);
                    j9 -= j6;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_5_0, j11);
                    j11 -= j4;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_6_7, j13);
                    j13 -= j8;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_2_0, j);
                    j -= j8;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_5_4, j3);
                    j3 -= j6;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_5_3, j5);
                    j5 -= j4;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_2_3, j7);
                    j7 -= j2;
                    j16 = ThreefishEngine.xorRotr(j16, ROTATION_5_1, j13);
                    j13 -= j16;
                    j14 = ThreefishEngine.xorRotr(j14, ROTATION_5_7, j15);
                    j15 -= j14;
                    j12 = ThreefishEngine.xorRotr(j12, ROTATION_2_6, j9);
                    j9 -= j12;
                    j10 = ThreefishEngine.xorRotr(j10, ROTATION_2_7, j11);
                    j11 -= j10;
                    j10 = ThreefishEngine.xorRotr(j10, ROTATION_1_0, j);
                    j -= j10;
                    j14 = ThreefishEngine.xorRotr(j14, ROTATION_6_4, j3);
                    j3 -= j14;
                    j12 = ThreefishEngine.xorRotr(j12, ROTATION_1_2, j7);
                    j7 -= j12;
                    j16 = ThreefishEngine.xorRotr(j16, ROTATION_1_3, j5);
                    j5 -= j16;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_1_4, j11);
                    j11 -= j8;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_1_5, j13);
                    j13 -= j4;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_7_4, j15);
                    j15 -= j6;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_7_3, j9);
                    j9 -= j2;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_0_0, j);
                    j -= j2;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_2_3, j3);
                    j3 -= j4;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_0_4, j5);
                    j5 -= j6;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_6_2, j7);
                    j7 -= j8;
                    j10 = ThreefishEngine.xorRotr(j10, ROTATION_0_4, j9);
                    j9 -= j10;
                    j12 = ThreefishEngine.xorRotr(j12, ROTATION_2_7, j11);
                    j11 -= j12;
                    j14 = ThreefishEngine.xorRotr(j14, ROTATION_0_6, j13);
                    j13 -= j14;
                    j16 = ThreefishEngine.xorRotr(j16, ROTATION_7_6, j15);
                    j15 -= j16;
                }
                j -= jArr3[0];
                j2 -= jArr3[1];
                j3 -= jArr3[ThreefishEngine.TWEAK_SIZE_WORDS];
                j4 -= jArr3[3];
                j5 -= jArr3[ROTATION_5_4];
                j6 -= jArr3[ROTATION_3_0];
                j7 -= jArr3[6];
                j8 -= jArr3[7];
                j9 -= jArr3[ROTATION_0_4];
                j10 -= jArr3[ROTATION_7_0];
                j11 -= jArr3[ROTATION_1_2];
                j12 -= jArr3[11];
                j13 -= jArr3[ROTATION_4_4];
                j14 -= jArr3[ROTATION_2_3] + jArr4[0];
                j15 -= jArr4[1] + jArr3[14];
                long j17 = j16 - jArr3[15];
                jArr2[0] = j;
                jArr2[1] = j2;
                jArr2[ThreefishEngine.TWEAK_SIZE_WORDS] = j3;
                jArr2[3] = j4;
                jArr2[ROTATION_5_4] = j5;
                jArr2[ROTATION_3_0] = j6;
                jArr2[6] = j7;
                jArr2[7] = j8;
                jArr2[ROTATION_0_4] = j9;
                jArr2[ROTATION_7_0] = j10;
                jArr2[ROTATION_1_2] = j11;
                jArr2[11] = j12;
                jArr2[ROTATION_4_4] = j13;
                jArr2[ROTATION_2_3] = j14;
                jArr2[14] = j15;
                jArr2[15] = j17;
            }
        }

        void encryptBlock(long[] jArr, long[] jArr2) {
            long[] jArr3 = this.kw;
            long[] jArr4 = this.t;
            int[] access$300 = ThreefishEngine.MOD17;
            int[] access$100 = ThreefishEngine.MOD3;
            if (jArr3.length != ROTATION_2_0) {
                throw new IllegalArgumentException();
            } else if (jArr4.length != ROTATION_3_0) {
                throw new IllegalArgumentException();
            } else {
                long j = jArr[0];
                long j2 = jArr[1];
                long j3 = jArr[ThreefishEngine.TWEAK_SIZE_WORDS];
                long j4 = jArr[3];
                long j5 = jArr[ROTATION_5_4];
                long j6 = jArr[ROTATION_3_0];
                long j7 = jArr[6];
                long j8 = jArr[7];
                long j9 = jArr[ROTATION_0_4];
                long j10 = jArr[ROTATION_7_0];
                long j11 = jArr[ROTATION_1_2];
                long j12 = jArr[11];
                long j13 = jArr[ROTATION_4_4];
                long j14 = jArr[ROTATION_2_3];
                long j15 = jArr[14];
                long j16 = j + jArr3[0];
                long j17 = j2 + jArr3[1];
                long j18 = j3 + jArr3[ThreefishEngine.TWEAK_SIZE_WORDS];
                long j19 = j4 + jArr3[3];
                long j20 = j5 + jArr3[ROTATION_5_4];
                long j21 = j6 + jArr3[ROTATION_3_0];
                long j22 = j7 + jArr3[6];
                long j23 = j8 + jArr3[7];
                j8 = j9 + jArr3[ROTATION_0_4];
                j7 = j10 + jArr3[ROTATION_7_0];
                j6 = j11 + jArr3[ROTATION_1_2];
                j5 = j12 + jArr3[11];
                j4 = j13 + jArr3[ROTATION_4_4];
                j3 = j14 + (jArr3[ROTATION_2_3] + jArr4[0]);
                j2 = j15 + (jArr3[14] + jArr4[1]);
                j = jArr3[15] + jArr[15];
                for (int i = 1; i < ROTATION_7_7; i += ThreefishEngine.TWEAK_SIZE_WORDS) {
                    int i2 = access$300[i];
                    int i3 = access$100[i];
                    j16 += j17;
                    j17 = ThreefishEngine.rotlXor(j17, ROTATION_0_0, j16);
                    j18 += j19;
                    j19 = ThreefishEngine.rotlXor(j19, ROTATION_2_3, j18);
                    j20 += j21;
                    j21 = ThreefishEngine.rotlXor(j21, ROTATION_0_4, j20);
                    j22 += j23;
                    j23 = ThreefishEngine.rotlXor(j23, ROTATION_6_2, j22);
                    j8 += j7;
                    j7 = ThreefishEngine.rotlXor(j7, ROTATION_0_4, j8);
                    j6 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_2_7, j6);
                    j4 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_0_6, j4);
                    j2 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_7_6, j2);
                    j16 += j7;
                    j7 = ThreefishEngine.rotlXor(j7, ROTATION_1_0, j16);
                    j18 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_6_4, j18);
                    j22 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_1_2, j22);
                    j20 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_1_3, j20);
                    j6 += j23;
                    j23 = ThreefishEngine.rotlXor(j23, ROTATION_1_4, j6);
                    j4 += j19;
                    j19 = ThreefishEngine.rotlXor(j19, ROTATION_1_5, j4);
                    j2 += j21;
                    j21 = ThreefishEngine.rotlXor(j21, ROTATION_7_4, j2);
                    j8 += j17;
                    j17 = ThreefishEngine.rotlXor(j17, ROTATION_7_3, j8);
                    j16 += j23;
                    j23 = ThreefishEngine.rotlXor(j23, ROTATION_2_0, j16);
                    j18 += j21;
                    j21 = ThreefishEngine.rotlXor(j21, ROTATION_5_4, j18);
                    j20 += j19;
                    j19 = ThreefishEngine.rotlXor(j19, ROTATION_5_3, j20);
                    j22 += j17;
                    j17 = ThreefishEngine.rotlXor(j17, ROTATION_2_3, j22);
                    j4 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_5_1, j4);
                    j2 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_5_7, j2);
                    j8 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_2_6, j8);
                    j6 += j7;
                    j7 = ThreefishEngine.rotlXor(j7, ROTATION_2_7, j6);
                    j16 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_3_0, j16);
                    j18 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_7_7, j18);
                    j22 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_7_1, j22);
                    j20 += j7;
                    j7 = ThreefishEngine.rotlXor(j7, ROTATION_5_7, j20);
                    j2 += j17;
                    j17 = ThreefishEngine.rotlXor(j17, ROTATION_6_2, j2);
                    j8 += j21;
                    j21 = ThreefishEngine.rotlXor(j21, ROTATION_3_5, j8);
                    j6 += j19;
                    j19 = ThreefishEngine.rotlXor(j19, ROTATION_5_0, j6);
                    j4 += j23;
                    j17 += jArr3[i2 + 1];
                    j18 += jArr3[i2 + ThreefishEngine.TWEAK_SIZE_WORDS];
                    j19 += jArr3[i2 + 3];
                    j20 += jArr3[i2 + ROTATION_5_4];
                    j21 += jArr3[i2 + ROTATION_3_0];
                    j22 += jArr3[i2 + 6];
                    j23 = ThreefishEngine.rotlXor(j23, ROTATION_6_7, j4) + jArr3[i2 + 7];
                    j8 += jArr3[i2 + ROTATION_0_4];
                    j7 += jArr3[i2 + ROTATION_7_0];
                    j6 += jArr3[i2 + ROTATION_1_2];
                    j5 += jArr3[i2 + 11];
                    j4 += jArr3[i2 + ROTATION_4_4];
                    j3 += jArr3[i2 + ROTATION_2_3] + jArr4[i3];
                    j2 += jArr3[i2 + 14] + jArr4[i3 + 1];
                    j += jArr3[i2 + 15] + ((long) i);
                    j16 = (j16 + jArr3[i2]) + j17;
                    j17 = ThreefishEngine.rotlXor(j17, ROTATION_5_7, j16);
                    j18 += j19;
                    j19 = ThreefishEngine.rotlXor(j19, ROTATION_7_0, j18);
                    j20 += j21;
                    j21 = ThreefishEngine.rotlXor(j21, ROTATION_7_6, j20);
                    j22 += j23;
                    j23 = ThreefishEngine.rotlXor(j23, ROTATION_7_5, j22);
                    j8 += j7;
                    j7 = ThreefishEngine.rotlXor(j7, ROTATION_4_4, j8);
                    j6 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_6_2, j6);
                    j4 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_6_6, j4);
                    j2 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_4_7, j2);
                    j16 += j7;
                    j7 = ThreefishEngine.rotlXor(j7, ROTATION_5_0, j16);
                    j18 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_5_1, j18);
                    j22 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_5_2, j22);
                    j20 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_5_3, j20);
                    j6 += j23;
                    j23 = ThreefishEngine.rotlXor(j23, ROTATION_5_4, j6);
                    j4 += j19;
                    j19 = ThreefishEngine.rotlXor(j19, ROTATION_5_5, j4);
                    j2 += j21;
                    j21 = ThreefishEngine.rotlXor(j21, ROTATION_6_5, j2);
                    j8 += j17;
                    j17 = ThreefishEngine.rotlXor(j17, ROTATION_5_7, j8);
                    j16 += j23;
                    j23 = ThreefishEngine.rotlXor(j23, ROTATION_7_5, j16);
                    j18 += j21;
                    j21 = ThreefishEngine.rotlXor(j21, ROTATION_6_6, j18);
                    j20 += j19;
                    j19 = ThreefishEngine.rotlXor(j19, ROTATION_6_2, j20);
                    j22 += j17;
                    j17 = ThreefishEngine.rotlXor(j17, ROTATION_6_3, j22);
                    j4 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_6_4, j4);
                    j2 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_6_5, j2);
                    j8 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_6_6, j8);
                    j6 += j7;
                    j7 = ThreefishEngine.rotlXor(j7, ROTATION_6_7, j6);
                    j16 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_7_0, j16);
                    j18 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_7_1, j18);
                    j22 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_7_2, j22);
                    j20 += j7;
                    j7 = ThreefishEngine.rotlXor(j7, ROTATION_7_3, j20);
                    j2 += j17;
                    j17 = ThreefishEngine.rotlXor(j17, ROTATION_7_4, j2);
                    j8 += j21;
                    j21 = ThreefishEngine.rotlXor(j21, ROTATION_7_5, j8);
                    j6 += j19;
                    j19 = ThreefishEngine.rotlXor(j19, ROTATION_7_6, j6);
                    j4 += j23;
                    j16 += jArr3[i2 + 1];
                    j17 += jArr3[i2 + ThreefishEngine.TWEAK_SIZE_WORDS];
                    j18 += jArr3[i2 + 3];
                    j19 += jArr3[i2 + ROTATION_5_4];
                    j20 += jArr3[i2 + ROTATION_3_0];
                    j21 += jArr3[i2 + 6];
                    j22 += jArr3[i2 + 7];
                    j23 = ThreefishEngine.rotlXor(j23, ROTATION_7_7, j4) + jArr3[i2 + ROTATION_0_4];
                    j8 += jArr3[i2 + ROTATION_7_0];
                    j7 += jArr3[i2 + ROTATION_1_2];
                    j6 += jArr3[i2 + 11];
                    j5 += jArr3[i2 + ROTATION_4_4];
                    j4 += jArr3[i2 + ROTATION_2_3];
                    j3 += jArr3[i2 + 14] + jArr4[i3 + 1];
                    j2 += jArr4[i3 + ThreefishEngine.TWEAK_SIZE_WORDS] + jArr3[i2 + 15];
                    j += (jArr3[i2 + ROTATION_5_0] + ((long) i)) + 1;
                }
                jArr2[0] = j16;
                jArr2[1] = j17;
                jArr2[ThreefishEngine.TWEAK_SIZE_WORDS] = j18;
                jArr2[3] = j19;
                jArr2[ROTATION_5_4] = j20;
                jArr2[ROTATION_3_0] = j21;
                jArr2[6] = j22;
                jArr2[7] = j23;
                jArr2[ROTATION_0_4] = j8;
                jArr2[ROTATION_7_0] = j7;
                jArr2[ROTATION_1_2] = j6;
                jArr2[11] = j5;
                jArr2[ROTATION_4_4] = j4;
                jArr2[ROTATION_2_3] = j3;
                jArr2[14] = j2;
                jArr2[15] = j;
            }
        }
    }

    private static final class Threefish256Cipher extends ThreefishCipher {
        private static final int ROTATION_0_0 = 14;
        private static final int ROTATION_0_1 = 16;
        private static final int ROTATION_1_0 = 52;
        private static final int ROTATION_1_1 = 57;
        private static final int ROTATION_2_0 = 23;
        private static final int ROTATION_2_1 = 40;
        private static final int ROTATION_3_0 = 5;
        private static final int ROTATION_3_1 = 37;
        private static final int ROTATION_4_0 = 25;
        private static final int ROTATION_4_1 = 33;
        private static final int ROTATION_5_0 = 46;
        private static final int ROTATION_5_1 = 12;
        private static final int ROTATION_6_0 = 58;
        private static final int ROTATION_6_1 = 22;
        private static final int ROTATION_7_0 = 32;
        private static final int ROTATION_7_1 = 32;

        public Threefish256Cipher(long[] jArr, long[] jArr2) {
            super(jArr, jArr2);
        }

        void decryptBlock(long[] jArr, long[] jArr2) {
            long[] jArr3 = this.kw;
            long[] jArr4 = this.t;
            int[] access$000 = ThreefishEngine.MOD5;
            int[] access$100 = ThreefishEngine.MOD3;
            if (jArr3.length != 9) {
                throw new IllegalArgumentException();
            } else if (jArr4.length != ROTATION_3_0) {
                throw new IllegalArgumentException();
            } else {
                long j = jArr[0];
                long j2 = jArr[1];
                long j3 = jArr[ThreefishEngine.TWEAK_SIZE_WORDS];
                long j4 = jArr[3];
                for (int i = 17; i >= 1; i -= 2) {
                    int i2 = access$000[i];
                    int i3 = access$100[i];
                    j -= jArr3[i2 + 1];
                    j2 -= jArr3[i2 + ThreefishEngine.TWEAK_SIZE_WORDS] + jArr4[i3 + 1];
                    j3 -= jArr3[i2 + 3] + jArr4[i3 + ThreefishEngine.TWEAK_SIZE_WORDS];
                    j4 = ThreefishEngine.xorRotr(j4 - ((jArr3[i2 + 4] + ((long) i)) + 1), ROTATION_7_1, j);
                    j -= j4;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_7_1, j3);
                    j3 -= j2;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_6_0, j);
                    j -= j2;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_6_1, j3);
                    j3 -= j4;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_5_0, j);
                    j -= j4;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_5_1, j3);
                    j3 -= j2;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_4_0, j);
                    j -= j2;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_4_1, j3);
                    j -= jArr3[i2];
                    j2 -= jArr3[i2 + 1] + jArr4[i3];
                    j3 = (j3 - j4) - (jArr4[i3 + 1] + jArr3[i2 + ThreefishEngine.TWEAK_SIZE_WORDS]);
                    j4 = ThreefishEngine.xorRotr(j4 - (jArr3[i2 + 3] + ((long) i)), ROTATION_3_0, j);
                    j -= j4;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_3_1, j3);
                    j3 -= j2;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_2_0, j);
                    j -= j2;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_2_1, j3);
                    j3 -= j4;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_1_0, j);
                    j -= j4;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_1_1, j3);
                    j3 -= j2;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_0_0, j);
                    j -= j2;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_0_1, j3);
                    j3 -= j4;
                }
                j -= jArr3[0];
                j2 -= jArr3[1] + jArr4[0];
                j3 -= jArr4[1] + jArr3[ThreefishEngine.TWEAK_SIZE_WORDS];
                long j5 = j4 - jArr3[3];
                jArr2[0] = j;
                jArr2[1] = j2;
                jArr2[ThreefishEngine.TWEAK_SIZE_WORDS] = j3;
                jArr2[3] = j5;
            }
        }

        void encryptBlock(long[] jArr, long[] jArr2) {
            long[] jArr3 = this.kw;
            long[] jArr4 = this.t;
            int[] access$000 = ThreefishEngine.MOD5;
            int[] access$100 = ThreefishEngine.MOD3;
            if (jArr3.length != 9) {
                throw new IllegalArgumentException();
            } else if (jArr4.length != ROTATION_3_0) {
                throw new IllegalArgumentException();
            } else {
                long j = jArr[0];
                long j2 = jArr[1];
                long j3 = jArr[ThreefishEngine.TWEAK_SIZE_WORDS];
                long j4 = j + jArr3[0];
                long j5 = j2 + (jArr3[1] + jArr4[0]);
                j2 = j3 + (jArr3[ThreefishEngine.TWEAK_SIZE_WORDS] + jArr4[1]);
                j = jArr3[3] + jArr[3];
                for (int i = 1; i < 18; i += ThreefishEngine.TWEAK_SIZE_WORDS) {
                    int i2 = access$000[i];
                    int i3 = access$100[i];
                    j4 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_0_0, j4);
                    j2 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_0_1, j2);
                    j4 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_1_0, j4);
                    j2 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_1_1, j2);
                    j4 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_2_0, j4);
                    j2 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_2_1, j2);
                    j4 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_3_0, j4);
                    j2 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_3_1, j2) + (jArr3[i2 + 1] + jArr4[i3]);
                    j2 += jArr3[i2 + ThreefishEngine.TWEAK_SIZE_WORDS] + jArr4[i3 + 1];
                    j += jArr3[i2 + 3] + ((long) i);
                    j4 = (j4 + jArr3[i2]) + j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_4_0, j4);
                    j2 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_4_1, j2);
                    j4 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_5_0, j4);
                    j2 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_5_1, j2);
                    j4 += j5;
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_6_0, j4);
                    j2 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_6_1, j2);
                    j4 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_7_1, j4);
                    j2 += j5;
                    j4 += jArr3[i2 + 1];
                    j5 = ThreefishEngine.rotlXor(j5, ROTATION_7_1, j2) + (jArr3[i2 + ThreefishEngine.TWEAK_SIZE_WORDS] + jArr4[i3 + 1]);
                    j2 += jArr4[i3 + ThreefishEngine.TWEAK_SIZE_WORDS] + jArr3[i2 + 3];
                    j += (jArr3[i2 + 4] + ((long) i)) + 1;
                }
                jArr2[0] = j4;
                jArr2[1] = j5;
                jArr2[ThreefishEngine.TWEAK_SIZE_WORDS] = j2;
                jArr2[3] = j;
            }
        }
    }

    private static final class Threefish512Cipher extends ThreefishCipher {
        private static final int ROTATION_0_0 = 46;
        private static final int ROTATION_0_1 = 36;
        private static final int ROTATION_0_2 = 19;
        private static final int ROTATION_0_3 = 37;
        private static final int ROTATION_1_0 = 33;
        private static final int ROTATION_1_1 = 27;
        private static final int ROTATION_1_2 = 14;
        private static final int ROTATION_1_3 = 42;
        private static final int ROTATION_2_0 = 17;
        private static final int ROTATION_2_1 = 49;
        private static final int ROTATION_2_2 = 36;
        private static final int ROTATION_2_3 = 39;
        private static final int ROTATION_3_0 = 44;
        private static final int ROTATION_3_1 = 9;
        private static final int ROTATION_3_2 = 54;
        private static final int ROTATION_3_3 = 56;
        private static final int ROTATION_4_0 = 39;
        private static final int ROTATION_4_1 = 30;
        private static final int ROTATION_4_2 = 34;
        private static final int ROTATION_4_3 = 24;
        private static final int ROTATION_5_0 = 13;
        private static final int ROTATION_5_1 = 50;
        private static final int ROTATION_5_2 = 10;
        private static final int ROTATION_5_3 = 17;
        private static final int ROTATION_6_0 = 25;
        private static final int ROTATION_6_1 = 29;
        private static final int ROTATION_6_2 = 39;
        private static final int ROTATION_6_3 = 43;
        private static final int ROTATION_7_0 = 8;
        private static final int ROTATION_7_1 = 35;
        private static final int ROTATION_7_2 = 56;
        private static final int ROTATION_7_3 = 22;

        protected Threefish512Cipher(long[] jArr, long[] jArr2) {
            super(jArr, jArr2);
        }

        public void decryptBlock(long[] jArr, long[] jArr2) {
            long[] jArr3 = this.kw;
            long[] jArr4 = this.t;
            int[] access$200 = ThreefishEngine.MOD9;
            int[] access$100 = ThreefishEngine.MOD3;
            if (jArr3.length != ROTATION_5_3) {
                throw new IllegalArgumentException();
            } else if (jArr4.length != 5) {
                throw new IllegalArgumentException();
            } else {
                long j = jArr[0];
                long j2 = jArr[1];
                long j3 = jArr[ThreefishEngine.TWEAK_SIZE_WORDS];
                long j4 = jArr[3];
                long j5 = jArr[4];
                long j6 = jArr[5];
                long j7 = jArr[6];
                long j8 = jArr[7];
                for (int i = ROTATION_5_3; i >= 1; i -= 2) {
                    int i2 = access$200[i];
                    int i3 = access$100[i];
                    j -= jArr3[i2 + 1];
                    j3 -= jArr3[i2 + 3];
                    j4 -= jArr3[i2 + 4];
                    j5 -= jArr3[i2 + 5];
                    j6 -= jArr3[i2 + 6] + jArr4[i3 + 1];
                    j7 -= jArr3[i2 + 7] + jArr4[i3 + ThreefishEngine.TWEAK_SIZE_WORDS];
                    j8 -= (jArr3[i2 + ROTATION_7_0] + ((long) i)) + 1;
                    j2 = ThreefishEngine.xorRotr(j2 - jArr3[i2 + ThreefishEngine.TWEAK_SIZE_WORDS], ROTATION_7_0, j7);
                    j7 -= j2;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_7_1, j);
                    j -= j8;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_7_2, j3);
                    j3 -= j6;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_7_3, j5);
                    j5 -= j4;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_6_0, j5);
                    j5 -= j2;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_6_1, j7);
                    j7 -= j4;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_6_2, j);
                    j -= j6;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_6_3, j3);
                    j3 -= j8;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_5_0, j3);
                    j3 -= j2;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_5_1, j5);
                    j5 -= j8;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_5_2, j7);
                    j7 -= j6;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_5_3, j);
                    j -= j4;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_6_2, j);
                    j -= j2;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_4_1, j3);
                    j3 -= j4;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_4_2, j5);
                    j5 -= j6;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_4_3, j7);
                    j -= jArr3[i2];
                    j2 -= jArr3[i2 + 1];
                    j3 -= jArr3[i2 + ThreefishEngine.TWEAK_SIZE_WORDS];
                    j4 -= jArr3[i2 + 3];
                    j5 -= jArr3[i2 + 4];
                    j6 -= jArr3[i2 + 5] + jArr4[i3];
                    j7 = (j7 - j8) - (jArr4[i3 + 1] + jArr3[i2 + 6]);
                    j8 -= jArr3[i2 + 7] + ((long) i);
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_3_0, j7);
                    j7 -= j2;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_3_1, j);
                    j -= j8;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_3_2, j3);
                    j3 -= j6;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_7_2, j5);
                    j5 -= j4;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_5_3, j5);
                    j5 -= j2;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_2_1, j7);
                    j7 -= j4;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_2_2, j);
                    j -= j6;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_6_2, j3);
                    j3 -= j8;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_1_0, j3);
                    j3 -= j2;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_1_1, j5);
                    j5 -= j8;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_1_2, j7);
                    j7 -= j6;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_1_3, j);
                    j -= j4;
                    j2 = ThreefishEngine.xorRotr(j2, ROTATION_0_0, j);
                    j -= j2;
                    j4 = ThreefishEngine.xorRotr(j4, ROTATION_2_2, j3);
                    j3 -= j4;
                    j6 = ThreefishEngine.xorRotr(j6, ROTATION_0_2, j5);
                    j5 -= j6;
                    j8 = ThreefishEngine.xorRotr(j8, ROTATION_0_3, j7);
                    j7 -= j8;
                }
                j -= jArr3[0];
                j2 -= jArr3[1];
                j3 -= jArr3[ThreefishEngine.TWEAK_SIZE_WORDS];
                j4 -= jArr3[3];
                j5 -= jArr3[4];
                j6 -= jArr3[5] + jArr4[0];
                j7 -= jArr4[1] + jArr3[6];
                long j9 = j8 - jArr3[7];
                jArr2[0] = j;
                jArr2[1] = j2;
                jArr2[ThreefishEngine.TWEAK_SIZE_WORDS] = j3;
                jArr2[3] = j4;
                jArr2[4] = j5;
                jArr2[5] = j6;
                jArr2[6] = j7;
                jArr2[7] = j9;
            }
        }

        public void encryptBlock(long[] jArr, long[] jArr2) {
            long[] jArr3 = this.kw;
            long[] jArr4 = this.t;
            int[] access$200 = ThreefishEngine.MOD9;
            int[] access$100 = ThreefishEngine.MOD3;
            if (jArr3.length != ROTATION_5_3) {
                throw new IllegalArgumentException();
            } else if (jArr4.length != 5) {
                throw new IllegalArgumentException();
            } else {
                long j = jArr[0];
                long j2 = jArr[1];
                long j3 = jArr[ThreefishEngine.TWEAK_SIZE_WORDS];
                long j4 = jArr[3];
                long j5 = jArr[4];
                long j6 = jArr[5];
                long j7 = jArr[6];
                long j8 = j + jArr3[0];
                long j9 = j2 + jArr3[1];
                long j10 = j3 + jArr3[ThreefishEngine.TWEAK_SIZE_WORDS];
                long j11 = j4 + jArr3[3];
                j4 = j5 + jArr3[4];
                j3 = j6 + (jArr3[5] + jArr4[0]);
                j2 = j7 + (jArr3[6] + jArr4[1]);
                j = jArr3[7] + jArr[7];
                for (int i = 1; i < 18; i += ThreefishEngine.TWEAK_SIZE_WORDS) {
                    int i2 = access$200[i];
                    int i3 = access$100[i];
                    j8 += j9;
                    j9 = ThreefishEngine.rotlXor(j9, ROTATION_0_0, j8);
                    j10 += j11;
                    j11 = ThreefishEngine.rotlXor(j11, ROTATION_2_2, j10);
                    j4 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_0_2, j4);
                    j2 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_0_3, j2);
                    j10 += j9;
                    j9 = ThreefishEngine.rotlXor(j9, ROTATION_1_0, j10);
                    j4 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_1_1, j4);
                    j2 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_1_2, j2);
                    j8 += j11;
                    j11 = ThreefishEngine.rotlXor(j11, ROTATION_1_3, j8);
                    j4 += j9;
                    j9 = ThreefishEngine.rotlXor(j9, ROTATION_5_3, j4);
                    j2 += j11;
                    j11 = ThreefishEngine.rotlXor(j11, ROTATION_2_1, j2);
                    j8 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_2_2, j8);
                    j10 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_6_2, j10);
                    j2 += j9;
                    j9 = ThreefishEngine.rotlXor(j9, ROTATION_3_0, j2);
                    j8 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_3_1, j8);
                    j10 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_3_2, j10);
                    j4 += j11;
                    j9 += jArr3[i2 + 1];
                    j10 += jArr3[i2 + ThreefishEngine.TWEAK_SIZE_WORDS];
                    j11 = ThreefishEngine.rotlXor(j11, ROTATION_7_2, j4) + jArr3[i2 + 3];
                    j4 += jArr3[i2 + 4];
                    j3 += jArr3[i2 + 5] + jArr4[i3];
                    j2 += jArr3[i2 + 6] + jArr4[i3 + 1];
                    j += jArr3[i2 + 7] + ((long) i);
                    j8 = (j8 + jArr3[i2]) + j9;
                    j9 = ThreefishEngine.rotlXor(j9, ROTATION_6_2, j8);
                    j10 += j11;
                    j11 = ThreefishEngine.rotlXor(j11, ROTATION_4_1, j10);
                    j4 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_4_2, j4);
                    j2 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_4_3, j2);
                    j10 += j9;
                    j9 = ThreefishEngine.rotlXor(j9, ROTATION_5_0, j10);
                    j4 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_5_1, j4);
                    j2 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_5_2, j2);
                    j8 += j11;
                    j11 = ThreefishEngine.rotlXor(j11, ROTATION_5_3, j8);
                    j4 += j9;
                    j9 = ThreefishEngine.rotlXor(j9, ROTATION_6_0, j4);
                    j2 += j11;
                    j11 = ThreefishEngine.rotlXor(j11, ROTATION_6_1, j2);
                    j8 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_6_2, j8);
                    j10 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_6_3, j10);
                    j2 += j9;
                    j9 = ThreefishEngine.rotlXor(j9, ROTATION_7_0, j2);
                    j8 += j;
                    j = ThreefishEngine.rotlXor(j, ROTATION_7_1, j8);
                    j10 += j3;
                    j3 = ThreefishEngine.rotlXor(j3, ROTATION_7_2, j10);
                    j4 += j11;
                    j8 += jArr3[i2 + 1];
                    j9 += jArr3[i2 + ThreefishEngine.TWEAK_SIZE_WORDS];
                    j10 += jArr3[i2 + 3];
                    j11 = ThreefishEngine.rotlXor(j11, ROTATION_7_3, j4) + jArr3[i2 + 4];
                    j4 += jArr3[i2 + 5];
                    j3 += jArr3[i2 + 6] + jArr4[i3 + 1];
                    j2 += jArr4[i3 + ThreefishEngine.TWEAK_SIZE_WORDS] + jArr3[i2 + 7];
                    j += (jArr3[i2 + ROTATION_7_0] + ((long) i)) + 1;
                }
                jArr2[0] = j8;
                jArr2[1] = j9;
                jArr2[ThreefishEngine.TWEAK_SIZE_WORDS] = j10;
                jArr2[3] = j11;
                jArr2[4] = j4;
                jArr2[5] = j3;
                jArr2[6] = j2;
                jArr2[7] = j;
            }
        }
    }

    static {
        MOD9 = new int[ROUNDS_1024];
        MOD17 = new int[MOD9.length];
        MOD5 = new int[MOD9.length];
        MOD3 = new int[MOD9.length];
        for (int i = 0; i < MOD9.length; i++) {
            MOD17[i] = i % 17;
            MOD9[i] = i % 9;
            MOD5[i] = i % 5;
            MOD3[i] = i % 3;
        }
    }

    public ThreefishEngine(int i) {
        this.f167t = new long[5];
        this.blocksizeBytes = i / 8;
        this.blocksizeWords = this.blocksizeBytes / 8;
        this.currentBlock = new long[this.blocksizeWords];
        this.kw = new long[((this.blocksizeWords * TWEAK_SIZE_WORDS) + 1)];
        switch (i) {
            case BLOCKSIZE_256 /*256*/:
                this.cipher = new Threefish256Cipher(this.kw, this.f167t);
            case BLOCKSIZE_512 /*512*/:
                this.cipher = new Threefish512Cipher(this.kw, this.f167t);
            case BLOCKSIZE_1024 /*1024*/:
                this.cipher = new Threefish1024Cipher(this.kw, this.f167t);
            default:
                throw new IllegalArgumentException("Invalid blocksize - Threefish is defined with block size of 256, 512, or 1024 bits");
        }
    }

    public static long bytesToWord(byte[] bArr, int i) {
        if (i + 8 > bArr.length) {
            throw new IllegalArgumentException();
        }
        int i2 = i + 1;
        int i3 = i2 + 1;
        i2 = i3 + 1;
        i3 = i2 + 1;
        i2 = i3 + 1;
        i3 = i2 + 1;
        i2 = i3 + 1;
        long j = ((((((((long) bArr[i]) & 255) | ((((long) bArr[i2]) & 255) << 8)) | ((((long) bArr[i3]) & 255) << TWEAK_SIZE_BYTES)) | ((((long) bArr[i2]) & 255) << 24)) | ((((long) bArr[i3]) & 255) << 32)) | ((((long) bArr[i2]) & 255) << 40)) | ((((long) bArr[i3]) & 255) << 48);
        i3 = i2 + 1;
        return ((((long) bArr[i2]) & 255) << 56) | j;
    }

    static long rotlXor(long j, int i, long j2) {
        return ((j << i) | (j >>> (-i))) ^ j2;
    }

    private void setKey(long[] jArr) {
        if (jArr.length != this.blocksizeWords) {
            throw new IllegalArgumentException("Threefish key must be same size as block (" + this.blocksizeWords + " words)");
        }
        long j = C_240;
        for (int i = 0; i < this.blocksizeWords; i++) {
            this.kw[i] = jArr[i];
            j ^= this.kw[i];
        }
        this.kw[this.blocksizeWords] = j;
        System.arraycopy(this.kw, 0, this.kw, this.blocksizeWords + 1, this.blocksizeWords);
    }

    private void setTweak(long[] jArr) {
        if (jArr.length != TWEAK_SIZE_WORDS) {
            throw new IllegalArgumentException("Tweak must be 2 words.");
        }
        this.f167t[0] = jArr[0];
        this.f167t[1] = jArr[1];
        this.f167t[TWEAK_SIZE_WORDS] = this.f167t[0] ^ this.f167t[1];
        this.f167t[3] = this.f167t[0];
        this.f167t[4] = this.f167t[1];
    }

    public static void wordToBytes(long j, byte[] bArr, int i) {
        if (i + 8 > bArr.length) {
            throw new IllegalArgumentException();
        }
        int i2 = i + 1;
        bArr[i] = (byte) ((int) j);
        int i3 = i2 + 1;
        bArr[i2] = (byte) ((int) (j >> 8));
        i2 = i3 + 1;
        bArr[i3] = (byte) ((int) (j >> TWEAK_SIZE_BYTES));
        i3 = i2 + 1;
        bArr[i2] = (byte) ((int) (j >> 24));
        i2 = i3 + 1;
        bArr[i3] = (byte) ((int) (j >> 32));
        i3 = i2 + 1;
        bArr[i2] = (byte) ((int) (j >> 40));
        i2 = i3 + 1;
        bArr[i3] = (byte) ((int) (j >> 48));
        i3 = i2 + 1;
        bArr[i2] = (byte) ((int) (j >> 56));
    }

    static long xorRotr(long j, int i, long j2) {
        long j3 = j ^ j2;
        return (j3 << (-i)) | (j3 >>> i);
    }

    public String getAlgorithmName() {
        return "Threefish-" + (this.blocksizeBytes * 8);
    }

    public int getBlockSize() {
        return this.blocksizeBytes;
    }

    public void init(boolean z, CipherParameters cipherParameters) {
        byte[] tweak;
        byte[] bArr;
        long[] jArr;
        long[] jArr2;
        if (cipherParameters instanceof TweakableBlockCipherParameters) {
            TweakableBlockCipherParameters tweakableBlockCipherParameters = (TweakableBlockCipherParameters) cipherParameters;
            byte[] key = tweakableBlockCipherParameters.getKey().getKey();
            tweak = tweakableBlockCipherParameters.getTweak();
            bArr = key;
        } else if (cipherParameters instanceof KeyParameter) {
            tweak = null;
            bArr = ((KeyParameter) cipherParameters).getKey();
        } else {
            throw new IllegalArgumentException("Invalid parameter passed to Threefish init - " + cipherParameters.getClass().getName());
        }
        if (bArr == null) {
            jArr = null;
        } else if (bArr.length != this.blocksizeBytes) {
            throw new IllegalArgumentException("Threefish key must be same size as block (" + this.blocksizeBytes + " bytes)");
        } else {
            jArr = new long[this.blocksizeWords];
            for (int i = 0; i < jArr.length; i++) {
                jArr[i] = bytesToWord(bArr, i * 8);
            }
        }
        if (tweak == null) {
            jArr2 = null;
        } else if (tweak.length != TWEAK_SIZE_BYTES) {
            throw new IllegalArgumentException("Threefish tweak must be 16 bytes");
        } else {
            jArr2 = new long[TWEAK_SIZE_WORDS];
            jArr2[0] = bytesToWord(tweak, 0);
            jArr2[1] = bytesToWord(tweak, 8);
        }
        init(z, jArr, jArr2);
    }

    public void init(boolean z, long[] jArr, long[] jArr2) {
        this.forEncryption = z;
        if (jArr != null) {
            setKey(jArr);
        }
        if (jArr2 != null) {
            setTweak(jArr2);
        }
    }

    public int processBlock(byte[] bArr, int i, byte[] bArr2, int i2) {
        int i3 = 0;
        if (this.blocksizeBytes + i2 > bArr2.length) {
            throw new DataLengthException("Output buffer too short");
        } else if (this.blocksizeBytes + i > bArr.length) {
            throw new DataLengthException("Input buffer too short");
        } else {
            for (int i4 = 0; i4 < this.blocksizeBytes; i4 += 8) {
                this.currentBlock[i4 >> 3] = bytesToWord(bArr, i + i4);
            }
            processBlock(this.currentBlock, this.currentBlock);
            while (i3 < this.blocksizeBytes) {
                wordToBytes(this.currentBlock[i3 >> 3], bArr2, i2 + i3);
                i3 += 8;
            }
            return this.blocksizeBytes;
        }
    }

    public int processBlock(long[] jArr, long[] jArr2) {
        if (this.kw[this.blocksizeWords] == 0) {
            throw new IllegalStateException("Threefish engine not initialised");
        } else if (jArr.length != this.blocksizeWords) {
            throw new DataLengthException("Input buffer too short");
        } else if (jArr2.length != this.blocksizeWords) {
            throw new DataLengthException("Output buffer too short");
        } else {
            if (this.forEncryption) {
                this.cipher.encryptBlock(jArr, jArr2);
            } else {
                this.cipher.decryptBlock(jArr, jArr2);
            }
            return this.blocksizeWords;
        }
    }

    public void reset() {
    }
}
