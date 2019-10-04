/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.ArithmeticException
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.lang.System
 *  java.math.BigInteger
 *  java.util.Random
 */
package org.bouncycastle.pqc.math.linearalgebra;

import java.math.BigInteger;
import java.util.Random;
import org.bouncycastle.pqc.math.linearalgebra.IntUtils;
import org.bouncycastle.pqc.math.linearalgebra.IntegerFunctions;

public class GF2Polynomial {
    private static final int[] bitMask;
    private static final boolean[] parity;
    private static Random rand;
    private static final int[] reverseRightMask;
    private static final short[] squaringTable;
    private int blocks;
    private int len;
    private int[] value;

    static {
        rand = new Random();
        parity = new boolean[]{false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false, false, true, true, false, true, false, false, true, false, true, true, false, true, false, false, true, true, false, false, true, false, true, true, false};
        squaringTable = new short[]{0, 1, 4, 5, 16, 17, 20, 21, 64, 65, 68, 69, 80, 81, 84, 85, 256, 257, 260, 261, 272, 273, 276, 277, 320, 321, 324, 325, 336, 337, 340, 341, 1024, 1025, 1028, 1029, 1040, 1041, 1044, 1045, 1088, 1089, 1092, 1093, 1104, 1105, 1108, 1109, 1280, 1281, 1284, 1285, 1296, 1297, 1300, 1301, 1344, 1345, 1348, 1349, 1360, 1361, 1364, 1365, 4096, 4097, 4100, 4101, 4112, 4113, 4116, 4117, 4160, 4161, 4164, 4165, 4176, 4177, 4180, 4181, 4352, 4353, 4356, 4357, 4368, 4369, 4372, 4373, 4416, 4417, 4420, 4421, 4432, 4433, 4436, 4437, 5120, 5121, 5124, 5125, 5136, 5137, 5140, 5141, 5184, 5185, 5188, 5189, 5200, 5201, 5204, 5205, 5376, 5377, 5380, 5381, 5392, 5393, 5396, 5397, 5440, 5441, 5444, 5445, 5456, 5457, 5460, 5461, 16384, 16385, 16388, 16389, 16400, 16401, 16404, 16405, 16448, 16449, 16452, 16453, 16464, 16465, 16468, 16469, 16640, 16641, 16644, 16645, 16656, 16657, 16660, 16661, 16704, 16705, 16708, 16709, 16720, 16721, 16724, 16725, 17408, 17409, 17412, 17413, 17424, 17425, 17428, 17429, 17472, 17473, 17476, 17477, 17488, 17489, 17492, 17493, 17664, 17665, 17668, 17669, 17680, 17681, 17684, 17685, 17728, 17729, 17732, 17733, 17744, 17745, 17748, 17749, 20480, 20481, 20484, 20485, 20496, 20497, 20500, 20501, 20544, 20545, 20548, 20549, 20560, 20561, 20564, 20565, 20736, 20737, 20740, 20741, 20752, 20753, 20756, 20757, 20800, 20801, 20804, 20805, 20816, 20817, 20820, 20821, 21504, 21505, 21508, 21509, 21520, 21521, 21524, 21525, 21568, 21569, 21572, 21573, 21584, 21585, 21588, 21589, 21760, 21761, 21764, 21765, 21776, 21777, 21780, 21781, 21824, 21825, 21828, 21829, 21840, 21841, 21844, 21845};
        bitMask = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, Integer.MIN_VALUE, 0};
        reverseRightMask = new int[]{0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071, 262143, 524287, 1048575, 2097151, 4194303, 8388607, 16777215, 33554431, 67108863, 134217727, 268435455, 536870911, 1073741823, Integer.MAX_VALUE, -1};
    }

    public GF2Polynomial(int n) {
        if (n < 1) {
            n = 1;
        }
        this.blocks = 1 + (n - 1 >> 5);
        this.value = new int[this.blocks];
        this.len = n;
    }

    public GF2Polynomial(int n, String string) {
        if (n < 1) {
            n = 1;
        }
        this.blocks = 1 + (n - 1 >> 5);
        this.value = new int[this.blocks];
        this.len = n;
        if (string.equalsIgnoreCase("ZERO")) {
            this.assignZero();
            return;
        }
        if (string.equalsIgnoreCase("ONE")) {
            this.assignOne();
            return;
        }
        if (string.equalsIgnoreCase("RANDOM")) {
            this.randomize();
            return;
        }
        if (string.equalsIgnoreCase("X")) {
            this.assignX();
            return;
        }
        if (string.equalsIgnoreCase("ALL")) {
            this.assignAll();
            return;
        }
        throw new IllegalArgumentException("Error: GF2Polynomial was called using " + string + " as value!");
    }

    /*
     * Enabled aggressive block sorting
     */
    public GF2Polynomial(int n, BigInteger bigInteger) {
        byte[] arrby;
        if (n < 1) {
            n = 1;
        }
        this.blocks = 1 + (n - 1 >> 5);
        this.value = new int[this.blocks];
        this.len = n;
        byte[] arrby2 = bigInteger.toByteArray();
        if (arrby2[0] == 0) {
            byte[] arrby3 = new byte[-1 + arrby2.length];
            System.arraycopy((Object)arrby2, (int)1, (Object)arrby3, (int)0, (int)arrby3.length);
            arrby = arrby3;
        } else {
            arrby = arrby2;
        }
        int n2 = 3 & arrby.length;
        int n3 = 1 + (-1 + arrby.length >> 2);
        for (int i = 0; i < n2; ++i) {
            int[] arrn = this.value;
            int n4 = n3 - 1;
            arrn[n4] = arrn[n4] | (255 & arrby[i]) << (n2 - 1 - i << 3);
        }
        for (int i = 0; i <= -4 + arrby.length >> 2; ++i) {
            int n5 = -1 + arrby.length - (i << 2);
            this.value[i] = 255 & arrby[n5];
            int[] arrn = this.value;
            arrn[i] = arrn[i] | 65280 & arrby[n5 - 1] << 8;
            int[] arrn2 = this.value;
            arrn2[i] = arrn2[i] | 16711680 & arrby[n5 - 2] << 16;
            int[] arrn3 = this.value;
            arrn3[i] = arrn3[i] | -16777216 & arrby[n5 - 3] << 24;
        }
        if ((31 & this.len) != 0) {
            int[] arrn = this.value;
            int n6 = -1 + this.blocks;
            arrn[n6] = arrn[n6] & reverseRightMask[31 & this.len];
        }
        this.reduceN();
    }

    public GF2Polynomial(int n, Random random) {
        if (n < 1) {
            n = 1;
        }
        this.blocks = 1 + (n - 1 >> 5);
        this.value = new int[this.blocks];
        this.len = n;
        this.randomize(random);
    }

    public GF2Polynomial(int n, byte[] arrby) {
        if (n < 1) {
            n = 1;
        }
        this.blocks = 1 + (n - 1 >> 5);
        this.value = new int[this.blocks];
        this.len = n;
        int n2 = Math.min((int)(1 + (-1 + arrby.length >> 2)), (int)this.blocks);
        for (int i = 0; i < n2 - 1; ++i) {
            int n3 = -1 + (arrby.length - (i << 2));
            this.value[i] = 255 & arrby[n3];
            int[] arrn = this.value;
            arrn[i] = arrn[i] | 65280 & arrby[n3 - 1] << 8;
            int[] arrn2 = this.value;
            arrn2[i] = arrn2[i] | 16711680 & arrby[n3 - 2] << 16;
            int[] arrn3 = this.value;
            arrn3[i] = arrn3[i] | -16777216 & arrby[n3 - 3] << 24;
        }
        int n4 = n2 - 1;
        int n5 = -1 + (arrby.length - (n4 << 2));
        this.value[n4] = 255 & arrby[n5];
        if (n5 > 0) {
            int[] arrn = this.value;
            arrn[n4] = arrn[n4] | 65280 & arrby[n5 - 1] << 8;
        }
        if (n5 > 1) {
            int[] arrn = this.value;
            arrn[n4] = arrn[n4] | 16711680 & arrby[n5 - 2] << 16;
        }
        if (n5 > 2) {
            int[] arrn = this.value;
            arrn[n4] = arrn[n4] | -16777216 & arrby[n5 - 3] << 24;
        }
        this.zeroUnusedBits();
        this.reduceN();
    }

    public GF2Polynomial(int n, int[] arrn) {
        if (n < 1) {
            n = 1;
        }
        this.blocks = 1 + (n - 1 >> 5);
        this.value = new int[this.blocks];
        this.len = n;
        int n2 = Math.min((int)this.blocks, (int)arrn.length);
        System.arraycopy((Object)arrn, (int)0, (Object)this.value, (int)0, (int)n2);
        this.zeroUnusedBits();
    }

    public GF2Polynomial(GF2Polynomial gF2Polynomial) {
        this.len = gF2Polynomial.len;
        this.blocks = gF2Polynomial.blocks;
        this.value = IntUtils.clone(gF2Polynomial.value);
    }

    private void doShiftBlocksLeft(int n) {
        if (this.blocks <= this.value.length) {
            for (int i = -1 + this.blocks; i >= n; --i) {
                this.value[i] = this.value[i - n];
            }
            for (int i = 0; i < n; ++i) {
                this.value[i] = 0;
            }
        } else {
            int[] arrn = new int[this.blocks];
            System.arraycopy((Object)this.value, (int)0, (Object)arrn, (int)n, (int)(this.blocks - n));
            this.value = null;
            this.value = arrn;
        }
    }

    private GF2Polynomial karaMult(GF2Polynomial gF2Polynomial) {
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.len << 1);
        if (this.len <= 32) {
            gF2Polynomial2.value = GF2Polynomial.mult32(this.value[0], gF2Polynomial.value[0]);
            return gF2Polynomial2;
        }
        if (this.len <= 64) {
            gF2Polynomial2.value = GF2Polynomial.mult64(this.value, gF2Polynomial.value);
            return gF2Polynomial2;
        }
        if (this.len <= 128) {
            gF2Polynomial2.value = GF2Polynomial.mult128(this.value, gF2Polynomial.value);
            return gF2Polynomial2;
        }
        if (this.len <= 256) {
            gF2Polynomial2.value = GF2Polynomial.mult256(this.value, gF2Polynomial.value);
            return gF2Polynomial2;
        }
        if (this.len <= 512) {
            gF2Polynomial2.value = GF2Polynomial.mult512(this.value, gF2Polynomial.value);
            return gF2Polynomial2;
        }
        int n = IntegerFunctions.floorLog(-1 + this.len);
        int n2 = bitMask[n];
        GF2Polynomial gF2Polynomial3 = this.lower(1 + (n2 - 1 >> 5));
        GF2Polynomial gF2Polynomial4 = this.upper(1 + (n2 - 1 >> 5));
        GF2Polynomial gF2Polynomial5 = gF2Polynomial.lower(1 + (n2 - 1 >> 5));
        GF2Polynomial gF2Polynomial6 = gF2Polynomial.upper(1 + (n2 - 1 >> 5));
        GF2Polynomial gF2Polynomial7 = gF2Polynomial4.karaMult(gF2Polynomial6);
        GF2Polynomial gF2Polynomial8 = gF2Polynomial3.karaMult(gF2Polynomial5);
        gF2Polynomial3.addToThis(gF2Polynomial4);
        gF2Polynomial5.addToThis(gF2Polynomial6);
        GF2Polynomial gF2Polynomial9 = gF2Polynomial3.karaMult(gF2Polynomial5);
        gF2Polynomial2.shiftLeftAddThis(gF2Polynomial7, n2 << 1);
        gF2Polynomial2.shiftLeftAddThis(gF2Polynomial7, n2);
        gF2Polynomial2.shiftLeftAddThis(gF2Polynomial9, n2);
        gF2Polynomial2.shiftLeftAddThis(gF2Polynomial8, n2);
        gF2Polynomial2.addToThis(gF2Polynomial8);
        return gF2Polynomial2;
    }

    private GF2Polynomial lower(int n) {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(n << 5);
        System.arraycopy((Object)this.value, (int)0, (Object)gF2Polynomial.value, (int)0, (int)Math.min((int)n, (int)this.blocks));
        return gF2Polynomial;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static int[] mult128(int[] arrn, int[] arrn2) {
        int[] arrn3 = new int[8];
        int[] arrn4 = new int[2];
        System.arraycopy((Object)arrn, (int)0, (Object)arrn4, (int)0, (int)Math.min((int)2, (int)arrn.length));
        int[] arrn5 = new int[2];
        if (arrn.length > 2) {
            System.arraycopy((Object)arrn, (int)2, (Object)arrn5, (int)0, (int)Math.min((int)2, (int)(-2 + arrn.length)));
        }
        int[] arrn6 = new int[2];
        System.arraycopy((Object)arrn2, (int)0, (Object)arrn6, (int)0, (int)Math.min((int)2, (int)arrn2.length));
        int[] arrn7 = new int[2];
        if (arrn2.length > 2) {
            System.arraycopy((Object)arrn2, (int)2, (Object)arrn7, (int)0, (int)Math.min((int)2, (int)(-2 + arrn2.length)));
        }
        if (arrn5[1] == 0 && arrn7[1] == 0) {
            if (arrn5[0] != 0 || arrn7[0] != 0) {
                int[] arrn8 = GF2Polynomial.mult32(arrn5[0], arrn7[0]);
                arrn3[5] = arrn3[5] ^ arrn8[1];
                arrn3[4] = arrn3[4] ^ arrn8[0];
                arrn3[3] = arrn3[3] ^ arrn8[1];
                arrn3[2] = arrn3[2] ^ arrn8[0];
            }
        } else {
            int[] arrn9 = GF2Polynomial.mult64(arrn5, arrn7);
            arrn3[7] = arrn3[7] ^ arrn9[3];
            arrn3[6] = arrn3[6] ^ arrn9[2];
            arrn3[5] = arrn3[5] ^ (arrn9[1] ^ arrn9[3]);
            arrn3[4] = arrn3[4] ^ (arrn9[0] ^ arrn9[2]);
            arrn3[3] = arrn3[3] ^ arrn9[1];
            arrn3[2] = arrn3[2] ^ arrn9[0];
        }
        arrn5[0] = arrn5[0] ^ arrn4[0];
        arrn5[1] = arrn5[1] ^ arrn4[1];
        arrn7[0] = arrn7[0] ^ arrn6[0];
        arrn7[1] = arrn7[1] ^ arrn6[1];
        if (arrn5[1] == 0 && arrn7[1] == 0) {
            int[] arrn10 = GF2Polynomial.mult32(arrn5[0], arrn7[0]);
            arrn3[3] = arrn3[3] ^ arrn10[1];
            arrn3[2] = arrn3[2] ^ arrn10[0];
        } else {
            int[] arrn11 = GF2Polynomial.mult64(arrn5, arrn7);
            arrn3[5] = arrn3[5] ^ arrn11[3];
            arrn3[4] = arrn3[4] ^ arrn11[2];
            arrn3[3] = arrn3[3] ^ arrn11[1];
            arrn3[2] = arrn3[2] ^ arrn11[0];
        }
        if (arrn4[1] == 0 && arrn6[1] == 0) {
            int[] arrn12 = GF2Polynomial.mult32(arrn4[0], arrn6[0]);
            arrn3[3] = arrn3[3] ^ arrn12[1];
            arrn3[2] = arrn3[2] ^ arrn12[0];
            arrn3[1] = arrn3[1] ^ arrn12[1];
            arrn3[0] = arrn3[0] ^ arrn12[0];
            return arrn3;
        }
        int[] arrn13 = GF2Polynomial.mult64(arrn4, arrn6);
        arrn3[5] = arrn3[5] ^ arrn13[3];
        arrn3[4] = arrn3[4] ^ arrn13[2];
        arrn3[3] = arrn3[3] ^ (arrn13[1] ^ arrn13[3]);
        arrn3[2] = arrn3[2] ^ (arrn13[0] ^ arrn13[2]);
        arrn3[1] = arrn3[1] ^ arrn13[1];
        arrn3[0] = arrn3[0] ^ arrn13[0];
        return arrn3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static int[] mult256(int[] arrn, int[] arrn2) {
        int[] arrn3 = new int[16];
        int[] arrn4 = new int[4];
        System.arraycopy((Object)arrn, (int)0, (Object)arrn4, (int)0, (int)Math.min((int)4, (int)arrn.length));
        int[] arrn5 = new int[4];
        if (arrn.length > 4) {
            System.arraycopy((Object)arrn, (int)4, (Object)arrn5, (int)0, (int)Math.min((int)4, (int)(-4 + arrn.length)));
        }
        int[] arrn6 = new int[4];
        System.arraycopy((Object)arrn2, (int)0, (Object)arrn6, (int)0, (int)Math.min((int)4, (int)arrn2.length));
        int[] arrn7 = new int[4];
        if (arrn2.length > 4) {
            System.arraycopy((Object)arrn2, (int)4, (Object)arrn7, (int)0, (int)Math.min((int)4, (int)(-4 + arrn2.length)));
        }
        if (arrn5[3] == 0 && arrn5[2] == 0 && arrn7[3] == 0 && arrn7[2] == 0) {
            if (arrn5[1] == 0 && arrn7[1] == 0) {
                if (arrn5[0] != 0 || arrn7[0] != 0) {
                    int[] arrn8 = GF2Polynomial.mult32(arrn5[0], arrn7[0]);
                    arrn3[9] = arrn3[9] ^ arrn8[1];
                    arrn3[8] = arrn3[8] ^ arrn8[0];
                    arrn3[5] = arrn3[5] ^ arrn8[1];
                    arrn3[4] = arrn3[4] ^ arrn8[0];
                }
            } else {
                int[] arrn9 = GF2Polynomial.mult64(arrn5, arrn7);
                arrn3[11] = arrn3[11] ^ arrn9[3];
                arrn3[10] = arrn3[10] ^ arrn9[2];
                arrn3[9] = arrn3[9] ^ arrn9[1];
                arrn3[8] = arrn3[8] ^ arrn9[0];
                arrn3[7] = arrn3[7] ^ arrn9[3];
                arrn3[6] = arrn3[6] ^ arrn9[2];
                arrn3[5] = arrn3[5] ^ arrn9[1];
                arrn3[4] = arrn3[4] ^ arrn9[0];
            }
        } else {
            int[] arrn10 = GF2Polynomial.mult128(arrn5, arrn7);
            arrn3[15] = arrn3[15] ^ arrn10[7];
            arrn3[14] = arrn3[14] ^ arrn10[6];
            arrn3[13] = arrn3[13] ^ arrn10[5];
            arrn3[12] = arrn3[12] ^ arrn10[4];
            arrn3[11] = arrn3[11] ^ (arrn10[3] ^ arrn10[7]);
            arrn3[10] = arrn3[10] ^ (arrn10[2] ^ arrn10[6]);
            arrn3[9] = arrn3[9] ^ (arrn10[1] ^ arrn10[5]);
            arrn3[8] = arrn3[8] ^ (arrn10[0] ^ arrn10[4]);
            arrn3[7] = arrn3[7] ^ arrn10[3];
            arrn3[6] = arrn3[6] ^ arrn10[2];
            arrn3[5] = arrn3[5] ^ arrn10[1];
            arrn3[4] = arrn3[4] ^ arrn10[0];
        }
        arrn5[0] = arrn5[0] ^ arrn4[0];
        arrn5[1] = arrn5[1] ^ arrn4[1];
        arrn5[2] = arrn5[2] ^ arrn4[2];
        arrn5[3] = arrn5[3] ^ arrn4[3];
        arrn7[0] = arrn7[0] ^ arrn6[0];
        arrn7[1] = arrn7[1] ^ arrn6[1];
        arrn7[2] = arrn7[2] ^ arrn6[2];
        arrn7[3] = arrn7[3] ^ arrn6[3];
        int[] arrn11 = GF2Polynomial.mult128(arrn5, arrn7);
        arrn3[11] = arrn3[11] ^ arrn11[7];
        arrn3[10] = arrn3[10] ^ arrn11[6];
        arrn3[9] = arrn3[9] ^ arrn11[5];
        arrn3[8] = arrn3[8] ^ arrn11[4];
        arrn3[7] = arrn3[7] ^ arrn11[3];
        arrn3[6] = arrn3[6] ^ arrn11[2];
        arrn3[5] = arrn3[5] ^ arrn11[1];
        arrn3[4] = arrn3[4] ^ arrn11[0];
        int[] arrn12 = GF2Polynomial.mult128(arrn4, arrn6);
        arrn3[11] = arrn3[11] ^ arrn12[7];
        arrn3[10] = arrn3[10] ^ arrn12[6];
        arrn3[9] = arrn3[9] ^ arrn12[5];
        arrn3[8] = arrn3[8] ^ arrn12[4];
        arrn3[7] = arrn3[7] ^ (arrn12[3] ^ arrn12[7]);
        arrn3[6] = arrn3[6] ^ (arrn12[2] ^ arrn12[6]);
        arrn3[5] = arrn3[5] ^ (arrn12[1] ^ arrn12[5]);
        arrn3[4] = arrn3[4] ^ (arrn12[0] ^ arrn12[4]);
        arrn3[3] = arrn3[3] ^ arrn12[3];
        arrn3[2] = arrn3[2] ^ arrn12[2];
        arrn3[1] = arrn3[1] ^ arrn12[1];
        arrn3[0] = arrn3[0] ^ arrn12[0];
        return arrn3;
    }

    private static int[] mult32(int n, int n2) {
        int[] arrn = new int[2];
        if (n == 0 || n2 == 0) {
            return arrn;
        }
        long l = 0xFFFFFFFFL & (long)n2;
        long l2 = 0L;
        for (int i = 1; i <= 32; ++i) {
            if ((n & bitMask[i - 1]) != 0) {
                l2 ^= l;
            }
            l <<= 1;
        }
        arrn[1] = (int)(l2 >>> 32);
        arrn[0] = (int)(l2 & 0xFFFFFFFFL);
        return arrn;
    }

    private static int[] mult512(int[] arrn, int[] arrn2) {
        int[] arrn3 = new int[32];
        int[] arrn4 = new int[8];
        System.arraycopy((Object)arrn, (int)0, (Object)arrn4, (int)0, (int)Math.min((int)8, (int)arrn.length));
        int[] arrn5 = new int[8];
        if (arrn.length > 8) {
            System.arraycopy((Object)arrn, (int)8, (Object)arrn5, (int)0, (int)Math.min((int)8, (int)(-8 + arrn.length)));
        }
        int[] arrn6 = new int[8];
        System.arraycopy((Object)arrn2, (int)0, (Object)arrn6, (int)0, (int)Math.min((int)8, (int)arrn2.length));
        int[] arrn7 = new int[8];
        if (arrn2.length > 8) {
            System.arraycopy((Object)arrn2, (int)8, (Object)arrn7, (int)0, (int)Math.min((int)8, (int)(-8 + arrn2.length)));
        }
        int[] arrn8 = GF2Polynomial.mult256(arrn5, arrn7);
        arrn3[31] = arrn3[31] ^ arrn8[15];
        arrn3[30] = arrn3[30] ^ arrn8[14];
        arrn3[29] = arrn3[29] ^ arrn8[13];
        arrn3[28] = arrn3[28] ^ arrn8[12];
        arrn3[27] = arrn3[27] ^ arrn8[11];
        arrn3[26] = arrn3[26] ^ arrn8[10];
        arrn3[25] = arrn3[25] ^ arrn8[9];
        arrn3[24] = arrn3[24] ^ arrn8[8];
        arrn3[23] = arrn3[23] ^ (arrn8[7] ^ arrn8[15]);
        arrn3[22] = arrn3[22] ^ (arrn8[6] ^ arrn8[14]);
        arrn3[21] = arrn3[21] ^ (arrn8[5] ^ arrn8[13]);
        arrn3[20] = arrn3[20] ^ (arrn8[4] ^ arrn8[12]);
        arrn3[19] = arrn3[19] ^ (arrn8[3] ^ arrn8[11]);
        arrn3[18] = arrn3[18] ^ (arrn8[2] ^ arrn8[10]);
        arrn3[17] = arrn3[17] ^ (arrn8[1] ^ arrn8[9]);
        arrn3[16] = arrn3[16] ^ (arrn8[0] ^ arrn8[8]);
        arrn3[15] = arrn3[15] ^ arrn8[7];
        arrn3[14] = arrn3[14] ^ arrn8[6];
        arrn3[13] = arrn3[13] ^ arrn8[5];
        arrn3[12] = arrn3[12] ^ arrn8[4];
        arrn3[11] = arrn3[11] ^ arrn8[3];
        arrn3[10] = arrn3[10] ^ arrn8[2];
        arrn3[9] = arrn3[9] ^ arrn8[1];
        arrn3[8] = arrn3[8] ^ arrn8[0];
        arrn5[0] = arrn5[0] ^ arrn4[0];
        arrn5[1] = arrn5[1] ^ arrn4[1];
        arrn5[2] = arrn5[2] ^ arrn4[2];
        arrn5[3] = arrn5[3] ^ arrn4[3];
        arrn5[4] = arrn5[4] ^ arrn4[4];
        arrn5[5] = arrn5[5] ^ arrn4[5];
        arrn5[6] = arrn5[6] ^ arrn4[6];
        arrn5[7] = arrn5[7] ^ arrn4[7];
        arrn7[0] = arrn7[0] ^ arrn6[0];
        arrn7[1] = arrn7[1] ^ arrn6[1];
        arrn7[2] = arrn7[2] ^ arrn6[2];
        arrn7[3] = arrn7[3] ^ arrn6[3];
        arrn7[4] = arrn7[4] ^ arrn6[4];
        arrn7[5] = arrn7[5] ^ arrn6[5];
        arrn7[6] = arrn7[6] ^ arrn6[6];
        arrn7[7] = arrn7[7] ^ arrn6[7];
        int[] arrn9 = GF2Polynomial.mult256(arrn5, arrn7);
        arrn3[23] = arrn3[23] ^ arrn9[15];
        arrn3[22] = arrn3[22] ^ arrn9[14];
        arrn3[21] = arrn3[21] ^ arrn9[13];
        arrn3[20] = arrn3[20] ^ arrn9[12];
        arrn3[19] = arrn3[19] ^ arrn9[11];
        arrn3[18] = arrn3[18] ^ arrn9[10];
        arrn3[17] = arrn3[17] ^ arrn9[9];
        arrn3[16] = arrn3[16] ^ arrn9[8];
        arrn3[15] = arrn3[15] ^ arrn9[7];
        arrn3[14] = arrn3[14] ^ arrn9[6];
        arrn3[13] = arrn3[13] ^ arrn9[5];
        arrn3[12] = arrn3[12] ^ arrn9[4];
        arrn3[11] = arrn3[11] ^ arrn9[3];
        arrn3[10] = arrn3[10] ^ arrn9[2];
        arrn3[9] = arrn3[9] ^ arrn9[1];
        arrn3[8] = arrn3[8] ^ arrn9[0];
        int[] arrn10 = GF2Polynomial.mult256(arrn4, arrn6);
        arrn3[23] = arrn3[23] ^ arrn10[15];
        arrn3[22] = arrn3[22] ^ arrn10[14];
        arrn3[21] = arrn3[21] ^ arrn10[13];
        arrn3[20] = arrn3[20] ^ arrn10[12];
        arrn3[19] = arrn3[19] ^ arrn10[11];
        arrn3[18] = arrn3[18] ^ arrn10[10];
        arrn3[17] = arrn3[17] ^ arrn10[9];
        arrn3[16] = arrn3[16] ^ arrn10[8];
        arrn3[15] = arrn3[15] ^ (arrn10[7] ^ arrn10[15]);
        arrn3[14] = arrn3[14] ^ (arrn10[6] ^ arrn10[14]);
        arrn3[13] = arrn3[13] ^ (arrn10[5] ^ arrn10[13]);
        arrn3[12] = arrn3[12] ^ (arrn10[4] ^ arrn10[12]);
        arrn3[11] = arrn3[11] ^ (arrn10[3] ^ arrn10[11]);
        arrn3[10] = arrn3[10] ^ (arrn10[2] ^ arrn10[10]);
        arrn3[9] = arrn3[9] ^ (arrn10[1] ^ arrn10[9]);
        arrn3[8] = arrn3[8] ^ (arrn10[0] ^ arrn10[8]);
        arrn3[7] = arrn3[7] ^ arrn10[7];
        arrn3[6] = arrn3[6] ^ arrn10[6];
        arrn3[5] = arrn3[5] ^ arrn10[5];
        arrn3[4] = arrn3[4] ^ arrn10[4];
        arrn3[3] = arrn3[3] ^ arrn10[3];
        arrn3[2] = arrn3[2] ^ arrn10[2];
        arrn3[1] = arrn3[1] ^ arrn10[1];
        arrn3[0] = arrn3[0] ^ arrn10[0];
        return arrn3;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static int[] mult64(int[] arrn, int[] arrn2) {
        int[] arrn3 = new int[4];
        int n = arrn[0];
        int n2 = arrn.length > 1 ? arrn[1] : 0;
        int n3 = arrn2[0];
        int n4 = arrn2.length > 1 ? arrn2[1] : 0;
        if (n2 != 0 || n4 != 0) {
            int[] arrn4 = GF2Polynomial.mult32(n2, n4);
            arrn3[3] = arrn3[3] ^ arrn4[1];
            arrn3[2] = arrn3[2] ^ (arrn4[0] ^ arrn4[1]);
            arrn3[1] = arrn3[1] ^ arrn4[0];
        }
        int[] arrn5 = GF2Polynomial.mult32(n2 ^ n, n4 ^ n3);
        arrn3[2] = arrn3[2] ^ arrn5[1];
        arrn3[1] = arrn3[1] ^ arrn5[0];
        int[] arrn6 = GF2Polynomial.mult32(n, n3);
        arrn3[2] = arrn3[2] ^ arrn6[1];
        arrn3[1] = arrn3[1] ^ (arrn6[0] ^ arrn6[1]);
        arrn3[0] = arrn3[0] ^ arrn6[0];
        return arrn3;
    }

    private GF2Polynomial upper(int n) {
        int n2 = Math.min((int)n, (int)(this.blocks - n));
        GF2Polynomial gF2Polynomial = new GF2Polynomial(n2 << 5);
        if (this.blocks >= n) {
            System.arraycopy((Object)this.value, (int)n, (Object)gF2Polynomial.value, (int)0, (int)n2);
        }
        return gF2Polynomial;
    }

    private void zeroUnusedBits() {
        if ((31 & this.len) != 0) {
            int[] arrn = this.value;
            int n = -1 + this.blocks;
            arrn[n] = arrn[n] & reverseRightMask[31 & this.len];
        }
    }

    public GF2Polynomial add(GF2Polynomial gF2Polynomial) {
        return this.xor(gF2Polynomial);
    }

    public void addToThis(GF2Polynomial gF2Polynomial) {
        this.expandN(gF2Polynomial.len);
        this.xorThisBy(gF2Polynomial);
    }

    public void assignAll() {
        for (int i = 0; i < this.blocks; ++i) {
            this.value[i] = -1;
        }
        this.zeroUnusedBits();
    }

    public void assignOne() {
        for (int i = 1; i < this.blocks; ++i) {
            this.value[i] = 0;
        }
        this.value[0] = 1;
    }

    public void assignX() {
        for (int i = 1; i < this.blocks; ++i) {
            this.value[i] = 0;
        }
        this.value[0] = 2;
    }

    public void assignZero() {
        for (int i = 0; i < this.blocks; ++i) {
            this.value[i] = 0;
        }
    }

    public Object clone() {
        return new GF2Polynomial(this);
    }

    public GF2Polynomial[] divide(GF2Polynomial gF2Polynomial) {
        GF2Polynomial[] arrgF2Polynomial = new GF2Polynomial[2];
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.len);
        GF2Polynomial gF2Polynomial3 = new GF2Polynomial(this);
        GF2Polynomial gF2Polynomial4 = new GF2Polynomial(gF2Polynomial);
        if (gF2Polynomial4.isZero()) {
            throw new RuntimeException();
        }
        gF2Polynomial3.reduceN();
        gF2Polynomial4.reduceN();
        if (gF2Polynomial3.len < gF2Polynomial4.len) {
            arrgF2Polynomial[0] = new GF2Polynomial(0);
            arrgF2Polynomial[1] = gF2Polynomial3;
            return arrgF2Polynomial;
        }
        int n = gF2Polynomial3.len - gF2Polynomial4.len;
        gF2Polynomial2.expandN(n + 1);
        while (n >= 0) {
            gF2Polynomial3.subtractFromThis(gF2Polynomial4.shiftLeft(n));
            gF2Polynomial3.reduceN();
            gF2Polynomial2.xorBit(n);
            n = gF2Polynomial3.len - gF2Polynomial4.len;
        }
        arrgF2Polynomial[0] = gF2Polynomial2;
        arrgF2Polynomial[1] = gF2Polynomial3;
        return arrgF2Polynomial;
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean equals(Object object) {
        if (object != null && object instanceof GF2Polynomial) {
            GF2Polynomial gF2Polynomial = (GF2Polynomial)object;
            if (this.len == gF2Polynomial.len) {
                int n = 0;
                do {
                    if (n >= this.blocks) {
                        return true;
                    }
                    if (this.value[n] != gF2Polynomial.value[n]) break;
                    ++n;
                } while (true);
            }
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void expandN(int n) {
        int n2;
        block7 : {
            block6 : {
                if (this.len >= n) break block6;
                this.len = n;
                n2 = 1 + (n - 1 >>> 5);
                if (this.blocks < n2) break block7;
            }
            return;
        }
        if (this.value.length < n2) {
            int[] arrn = new int[n2];
            System.arraycopy((Object)this.value, (int)0, (Object)arrn, (int)0, (int)this.blocks);
            this.blocks = n2;
            this.value = null;
            this.value = arrn;
            return;
        }
        int n3 = this.blocks;
        do {
            if (n3 >= n2) {
                this.blocks = n2;
                return;
            }
            this.value[n3] = 0;
            ++n3;
        } while (true);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public GF2Polynomial gcd(GF2Polynomial gF2Polynomial) {
        if (this.isZero() && gF2Polynomial.isZero()) {
            throw new ArithmeticException("Both operands of gcd equal zero.");
        }
        if (this.isZero()) {
            return new GF2Polynomial(gF2Polynomial);
        }
        if (gF2Polynomial.isZero()) {
            return new GF2Polynomial(this);
        }
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this);
        GF2Polynomial gF2Polynomial3 = new GF2Polynomial(gF2Polynomial);
        while (!gF2Polynomial3.isZero()) {
            GF2Polynomial gF2Polynomial4 = gF2Polynomial2.remainder(gF2Polynomial3);
            gF2Polynomial2 = gF2Polynomial3;
            gF2Polynomial3 = gF2Polynomial4;
        }
        return gF2Polynomial2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int getBit(int n) {
        if (n < 0) {
            throw new RuntimeException();
        }
        return n <= -1 + this.len && (this.value[n >>> 5] & bitMask[n & 31]) != 0;
    }

    public int getLength() {
        return this.len;
    }

    public int hashCode() {
        return this.len + this.value.hashCode();
    }

    public GF2Polynomial increase() {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(this);
        gF2Polynomial.increaseThis();
        return gF2Polynomial;
    }

    public void increaseThis() {
        this.xorBit(0);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean isIrreducible() {
        if (!this.isZero()) {
            GF2Polynomial gF2Polynomial = new GF2Polynomial(this);
            gF2Polynomial.reduceN();
            int n = -1 + gF2Polynomial.len;
            GF2Polynomial gF2Polynomial2 = new GF2Polynomial(gF2Polynomial.len, "X");
            int n2 = 1;
            do {
                if (n2 > n >> 1) {
                    return true;
                }
                gF2Polynomial2.squareThisPreCalc();
                GF2Polynomial gF2Polynomial3 = (gF2Polynomial2 = gF2Polynomial2.remainder(gF2Polynomial)).add(new GF2Polynomial(32, "X"));
                if (gF2Polynomial3.isZero() || !gF2Polynomial.gcd(gF2Polynomial3).isOne()) break;
                ++n2;
            } while (true);
        }
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isOne() {
        int n;
        for (int i = n = 1; i < this.blocks; i += 1) {
            if (this.value[i] == 0) continue;
            return (boolean)0;
        }
        if (this.value[0] == n) return (boolean)n;
        return false;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isZero() {
        if (this.len == 0) {
            return true;
        }
        int n = 0;
        while (n < this.blocks) {
            int n2 = this.value[n];
            boolean bl = false;
            if (n2 != 0) return bl;
            ++n;
        }
        return true;
    }

    public GF2Polynomial multiply(GF2Polynomial gF2Polynomial) {
        int n = Math.max((int)this.len, (int)gF2Polynomial.len);
        this.expandN(n);
        gF2Polynomial.expandN(n);
        return this.karaMult(gF2Polynomial);
    }

    public GF2Polynomial multiplyClassic(GF2Polynomial gF2Polynomial) {
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(Math.max((int)this.len, (int)gF2Polynomial.len) << 1);
        GF2Polynomial[] arrgF2Polynomial = new GF2Polynomial[32];
        arrgF2Polynomial[0] = new GF2Polynomial(this);
        for (int i = 1; i <= 31; ++i) {
            arrgF2Polynomial[i] = arrgF2Polynomial[i - 1].shiftLeft();
        }
        for (int i = 0; i < gF2Polynomial.blocks; ++i) {
            for (int j = 0; j <= 31; ++j) {
                if ((gF2Polynomial.value[i] & bitMask[j]) == 0) continue;
                gF2Polynomial2.xorThisBy(arrgF2Polynomial[j]);
            }
            for (int j = 0; j <= 31; ++j) {
                arrgF2Polynomial[j].shiftBlocksLeft();
            }
        }
        return gF2Polynomial2;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public GF2Polynomial quotient(GF2Polynomial gF2Polynomial) {
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this.len);
        GF2Polynomial gF2Polynomial3 = new GF2Polynomial(this);
        GF2Polynomial gF2Polynomial4 = new GF2Polynomial(gF2Polynomial);
        if (gF2Polynomial4.isZero()) {
            throw new RuntimeException();
        }
        gF2Polynomial3.reduceN();
        gF2Polynomial4.reduceN();
        if (gF2Polynomial3.len < gF2Polynomial4.len) {
            return new GF2Polynomial(0);
        }
        int n = gF2Polynomial3.len - gF2Polynomial4.len;
        gF2Polynomial2.expandN(n + 1);
        while (n >= 0) {
            gF2Polynomial3.subtractFromThis(gF2Polynomial4.shiftLeft(n));
            gF2Polynomial3.reduceN();
            gF2Polynomial2.xorBit(n);
            n = gF2Polynomial3.len - gF2Polynomial4.len;
        }
        return gF2Polynomial2;
    }

    public void randomize() {
        for (int i = 0; i < this.blocks; ++i) {
            this.value[i] = rand.nextInt();
        }
        this.zeroUnusedBits();
    }

    public void randomize(Random random) {
        for (int i = 0; i < this.blocks; ++i) {
            this.value[i] = random.nextInt();
        }
        this.zeroUnusedBits();
    }

    public void reduceN() {
        int n;
        for (n = -1 + this.blocks; this.value[n] == 0 && n > 0; --n) {
        }
        int n2 = this.value[n];
        int n3 = 0;
        while (n2 != 0) {
            n2 >>>= 1;
            ++n3;
        }
        this.len = n3 + (n << 5);
        this.blocks = n + 1;
    }

    void reducePentanomial(int n, int[] arrn) {
        int n2 = n >>> 5;
        int n3 = 32 - (n & 31);
        int n4 = n - arrn[0] >>> 5;
        int n5 = 32 - (31 & n - arrn[0]);
        int n6 = n - arrn[1] >>> 5;
        int n7 = 32 - (31 & n - arrn[1]);
        int n8 = n - arrn[2] >>> 5;
        int n9 = 32 - (31 & n - arrn[2]);
        for (int i = -2 + (n << 1) >>> 5; i > n2; --i) {
            long l = 0xFFFFFFFFL & (long)this.value[i];
            int[] arrn2 = this.value;
            int n10 = -1 + (i - n2);
            arrn2[n10] = arrn2[n10] ^ (int)(l << n3);
            int[] arrn3 = this.value;
            int n11 = i - n2;
            arrn3[n11] = (int)((long)arrn3[n11] ^ l >>> 32 - n3);
            int[] arrn4 = this.value;
            int n12 = -1 + (i - n4);
            arrn4[n12] = arrn4[n12] ^ (int)(l << n5);
            int[] arrn5 = this.value;
            int n13 = i - n4;
            arrn5[n13] = (int)((long)arrn5[n13] ^ l >>> 32 - n5);
            int[] arrn6 = this.value;
            int n14 = -1 + (i - n6);
            arrn6[n14] = arrn6[n14] ^ (int)(l << n7);
            int[] arrn7 = this.value;
            int n15 = i - n6;
            arrn7[n15] = (int)((long)arrn7[n15] ^ l >>> 32 - n7);
            int[] arrn8 = this.value;
            int n16 = -1 + (i - n8);
            arrn8[n16] = arrn8[n16] ^ (int)(l << n9);
            int[] arrn9 = this.value;
            int n17 = i - n8;
            arrn9[n17] = (int)((long)arrn9[n17] ^ l >>> 32 - n9);
            this.value[i] = 0;
        }
        long l = 0xFFFFFFFFL & (long)this.value[n2] & 0xFFFFFFFFL << (n & 31);
        int[] arrn10 = this.value;
        arrn10[0] = (int)((long)arrn10[0] ^ l >>> 32 - n3);
        if (-1 + (n2 - n4) >= 0) {
            int[] arrn11 = this.value;
            int n18 = -1 + (n2 - n4);
            arrn11[n18] = arrn11[n18] ^ (int)(l << n5);
        }
        int[] arrn12 = this.value;
        int n19 = n2 - n4;
        arrn12[n19] = (int)((long)arrn12[n19] ^ l >>> 32 - n5);
        if (-1 + (n2 - n6) >= 0) {
            int[] arrn13 = this.value;
            int n20 = -1 + (n2 - n6);
            arrn13[n20] = arrn13[n20] ^ (int)(l << n7);
        }
        int[] arrn14 = this.value;
        int n21 = n2 - n6;
        arrn14[n21] = (int)((long)arrn14[n21] ^ l >>> 32 - n7);
        if (-1 + (n2 - n8) >= 0) {
            int[] arrn15 = this.value;
            int n22 = -1 + (n2 - n8);
            arrn15[n22] = arrn15[n22] ^ (int)(l << n9);
        }
        int[] arrn16 = this.value;
        int n23 = n2 - n8;
        arrn16[n23] = (int)((long)arrn16[n23] ^ l >>> 32 - n9);
        int[] arrn17 = this.value;
        arrn17[n2] = arrn17[n2] & reverseRightMask[n & 31];
        this.blocks = 1 + (n - 1 >>> 5);
        this.len = n;
    }

    void reduceTrinomial(int n, int n2) {
        int n3 = n >>> 5;
        int n4 = 32 - (n & 31);
        int n5 = n - n2 >>> 5;
        int n6 = 32 - (31 & n - n2);
        for (int i = -2 + (n << 1) >>> 5; i > n3; --i) {
            long l = 0xFFFFFFFFL & (long)this.value[i];
            int[] arrn = this.value;
            int n7 = -1 + (i - n3);
            arrn[n7] = arrn[n7] ^ (int)(l << n4);
            int[] arrn2 = this.value;
            int n8 = i - n3;
            arrn2[n8] = (int)((long)arrn2[n8] ^ l >>> 32 - n4);
            int[] arrn3 = this.value;
            int n9 = -1 + (i - n5);
            arrn3[n9] = arrn3[n9] ^ (int)(l << n6);
            int[] arrn4 = this.value;
            int n10 = i - n5;
            arrn4[n10] = (int)((long)arrn4[n10] ^ l >>> 32 - n6);
            this.value[i] = 0;
        }
        long l = 0xFFFFFFFFL & (long)this.value[n3] & 0xFFFFFFFFL << (n & 31);
        int[] arrn = this.value;
        arrn[0] = (int)((long)arrn[0] ^ l >>> 32 - n4);
        if (-1 + (n3 - n5) >= 0) {
            int[] arrn5 = this.value;
            int n11 = -1 + (n3 - n5);
            arrn5[n11] = arrn5[n11] ^ (int)(l << n6);
        }
        int[] arrn6 = this.value;
        int n12 = n3 - n5;
        arrn6[n12] = (int)((long)arrn6[n12] ^ l >>> 32 - n6);
        int[] arrn7 = this.value;
        arrn7[n3] = arrn7[n3] & reverseRightMask[n & 31];
        this.blocks = 1 + (n - 1 >>> 5);
        this.len = n;
    }

    /*
     * Enabled aggressive block sorting
     */
    public GF2Polynomial remainder(GF2Polynomial gF2Polynomial) {
        GF2Polynomial gF2Polynomial2 = new GF2Polynomial(this);
        GF2Polynomial gF2Polynomial3 = new GF2Polynomial(gF2Polynomial);
        if (gF2Polynomial3.isZero()) {
            throw new RuntimeException();
        }
        gF2Polynomial2.reduceN();
        gF2Polynomial3.reduceN();
        if (gF2Polynomial2.len >= gF2Polynomial3.len) {
            int n = gF2Polynomial2.len - gF2Polynomial3.len;
            while (n >= 0) {
                gF2Polynomial2.subtractFromThis(gF2Polynomial3.shiftLeft(n));
                gF2Polynomial2.reduceN();
                n = gF2Polynomial2.len - gF2Polynomial3.len;
            }
        }
        return gF2Polynomial2;
    }

    public void resetBit(int n) {
        if (n < 0) {
            throw new RuntimeException();
        }
        if (n > -1 + this.len) {
            return;
        }
        int[] arrn = this.value;
        int n2 = n >>> 5;
        arrn[n2] = arrn[n2] & (-1 ^ bitMask[n & 31]);
    }

    public void setBit(int n) {
        if (n < 0 || n > -1 + this.len) {
            throw new RuntimeException();
        }
        int[] arrn = this.value;
        int n2 = n >>> 5;
        arrn[n2] = arrn[n2] | bitMask[n & 31];
    }

    void shiftBlocksLeft() {
        this.blocks = 1 + this.blocks;
        this.len = 32 + this.len;
        if (this.blocks <= this.value.length) {
            for (int i = -1 + this.blocks; i >= 1; --i) {
                this.value[i] = this.value[i - 1];
            }
            this.value[0] = 0;
            return;
        }
        int[] arrn = new int[this.blocks];
        System.arraycopy((Object)this.value, (int)0, (Object)arrn, (int)1, (int)(-1 + this.blocks));
        this.value = null;
        this.value = arrn;
    }

    public GF2Polynomial shiftLeft() {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(1 + this.len, this.value);
        for (int i = -1 + gF2Polynomial.blocks; i >= 1; --i) {
            int[] arrn = gF2Polynomial.value;
            arrn[i] = arrn[i] << 1;
            int[] arrn2 = gF2Polynomial.value;
            arrn2[i] = arrn2[i] | gF2Polynomial.value[i - 1] >>> 31;
        }
        int[] arrn = gF2Polynomial.value;
        arrn[0] = arrn[0] << 1;
        return gF2Polynomial;
    }

    public GF2Polynomial shiftLeft(int n) {
        int n2;
        GF2Polynomial gF2Polynomial = new GF2Polynomial(n + this.len, this.value);
        if (n >= 32) {
            gF2Polynomial.doShiftBlocksLeft(n >>> 5);
        }
        if ((n2 = n & 31) != 0) {
            for (int i = -1 + gF2Polynomial.blocks; i >= 1; --i) {
                int[] arrn = gF2Polynomial.value;
                arrn[i] = arrn[i] << n2;
                int[] arrn2 = gF2Polynomial.value;
                arrn2[i] = arrn2[i] | gF2Polynomial.value[i - 1] >>> 32 - n2;
            }
            int[] arrn = gF2Polynomial.value;
            arrn[0] = arrn[0] << n2;
        }
        return gF2Polynomial;
    }

    /*
     * Enabled aggressive block sorting
     */
    public void shiftLeftAddThis(GF2Polynomial gF2Polynomial, int n) {
        if (n == 0) {
            this.addToThis(gF2Polynomial);
            return;
        } else {
            this.expandN(n + gF2Polynomial.len);
            int n2 = n >>> 5;
            for (int i = -1 + gF2Polynomial.blocks; i >= 0; --i) {
                if (1 + (i + n2) < this.blocks && (n & 31) != 0) {
                    int[] arrn = this.value;
                    int n3 = 1 + (i + n2);
                    arrn[n3] = arrn[n3] ^ gF2Polynomial.value[i] >>> 32 - (n & 31);
                }
                int[] arrn = this.value;
                int n4 = i + n2;
                arrn[n4] = arrn[n4] ^ gF2Polynomial.value[i] << (n & 31);
            }
        }
    }

    public void shiftLeftThis() {
        if ((31 & this.len) == 0) {
            this.len = 1 + this.len;
            this.blocks = 1 + this.blocks;
            if (this.blocks > this.value.length) {
                int[] arrn = new int[this.blocks];
                System.arraycopy((Object)this.value, (int)0, (Object)arrn, (int)0, (int)this.value.length);
                this.value = null;
                this.value = arrn;
            }
            for (int i = -1 + this.blocks; i >= 1; --i) {
                int[] arrn = this.value;
                arrn[i] = arrn[i] | this.value[i - 1] >>> 31;
                int[] arrn2 = this.value;
                int n = i - 1;
                arrn2[n] = arrn2[n] << 1;
            }
        } else {
            this.len = 1 + this.len;
            for (int i = -1 + this.blocks; i >= 1; --i) {
                int[] arrn = this.value;
                arrn[i] = arrn[i] << 1;
                int[] arrn3 = this.value;
                arrn3[i] = arrn3[i] | this.value[i - 1] >>> 31;
            }
            int[] arrn = this.value;
            arrn[0] = arrn[0] << 1;
        }
    }

    public GF2Polynomial shiftRight() {
        GF2Polynomial gF2Polynomial = new GF2Polynomial(-1 + this.len);
        System.arraycopy((Object)this.value, (int)0, (Object)gF2Polynomial.value, (int)0, (int)gF2Polynomial.blocks);
        for (int i = 0; i <= -2 + gF2Polynomial.blocks; ++i) {
            int[] arrn = gF2Polynomial.value;
            arrn[i] = arrn[i] >>> 1;
            int[] arrn2 = gF2Polynomial.value;
            arrn2[i] = arrn2[i] | gF2Polynomial.value[i + 1] << 31;
        }
        int[] arrn = gF2Polynomial.value;
        int n = -1 + gF2Polynomial.blocks;
        arrn[n] = arrn[n] >>> 1;
        if (gF2Polynomial.blocks < this.blocks) {
            int[] arrn3 = gF2Polynomial.value;
            int n2 = -1 + gF2Polynomial.blocks;
            arrn3[n2] = arrn3[n2] | this.value[gF2Polynomial.blocks] << 31;
        }
        return gF2Polynomial;
    }

    public void shiftRightThis() {
        this.len = -1 + this.len;
        this.blocks = 1 + (-1 + this.len >>> 5);
        for (int i = 0; i <= -2 + this.blocks; ++i) {
            int[] arrn = this.value;
            arrn[i] = arrn[i] >>> 1;
            int[] arrn2 = this.value;
            arrn2[i] = arrn2[i] | this.value[i + 1] << 31;
        }
        int[] arrn = this.value;
        int n = -1 + this.blocks;
        arrn[n] = arrn[n] >>> 1;
        if ((31 & this.len) == 0) {
            int[] arrn3 = this.value;
            int n2 = -1 + this.blocks;
            arrn3[n2] = arrn3[n2] | this.value[this.blocks] << 31;
        }
    }

    public void squareThisBitwise() {
        if (this.isZero()) {
            return;
        }
        int[] arrn = new int[this.blocks << 1];
        for (int i = -1 + this.blocks; i >= 0; --i) {
            int n = this.value[i];
            int n2 = 1;
            for (int j = 0; j < 16; ++j) {
                if ((n & 1) != 0) {
                    int n3 = i << 1;
                    arrn[n3] = n2 | arrn[n3];
                }
                if ((65536 & n) != 0) {
                    int n4 = 1 + (i << 1);
                    arrn[n4] = n2 | arrn[n4];
                }
                n2 <<= 2;
                n >>>= 1;
            }
        }
        this.value = null;
        this.value = arrn;
        this.blocks = arrn.length;
        this.len = -1 + (this.len << 1);
    }

    public void squareThisPreCalc() {
        if (this.isZero()) {
            return;
        }
        if (this.value.length >= this.blocks << 1) {
            for (int i = -1 + this.blocks; i >= 0; --i) {
                this.value[1 + (i << 1)] = squaringTable[(16711680 & this.value[i]) >>> 16] | squaringTable[(-16777216 & this.value[i]) >>> 24] << 16;
                this.value[i << 1] = squaringTable[255 & this.value[i]] | squaringTable[(65280 & this.value[i]) >>> 8] << 16;
            }
            this.blocks <<= 1;
            this.len = -1 + (this.len << 1);
            return;
        }
        int[] arrn = new int[this.blocks << 1];
        for (int i = 0; i < this.blocks; ++i) {
            arrn[i << 1] = squaringTable[255 & this.value[i]] | squaringTable[(65280 & this.value[i]) >>> 8] << 16;
            arrn[1 + (i << 1)] = squaringTable[(16711680 & this.value[i]) >>> 16] | squaringTable[(-16777216 & this.value[i]) >>> 24] << 16;
        }
        this.value = null;
        this.value = arrn;
        this.blocks <<= 1;
        this.len = -1 + (this.len << 1);
    }

    public GF2Polynomial subtract(GF2Polynomial gF2Polynomial) {
        return this.xor(gF2Polynomial);
    }

    public void subtractFromThis(GF2Polynomial gF2Polynomial) {
        this.expandN(gF2Polynomial.len);
        this.xorThisBy(gF2Polynomial);
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean testBit(int n) {
        if (n < 0) {
            throw new RuntimeException();
        }
        return n <= -1 + this.len && (this.value[n >>> 5] & bitMask[n & 31]) != 0;
    }

    public byte[] toByteArray() {
        int n = 1 + (-1 + this.len >> 3);
        int n2 = n & 3;
        byte[] arrby = new byte[n];
        int n3 = 0;
        do {
            int n4 = n >> 2;
            if (n3 >= n4) break;
            int n5 = -1 + (n - (n3 << 2));
            arrby[n5] = (byte)(255 & this.value[n3]);
            arrby[n5 - 1] = (byte)((65280 & this.value[n3]) >>> 8);
            arrby[n5 - 2] = (byte)((16711680 & this.value[n3]) >>> 16);
            arrby[n5 - 3] = (byte)((-16777216 & this.value[n3]) >>> 24);
            ++n3;
        } while (true);
        for (int i = 0; i < n2; ++i) {
            int n6 = -1 + (n2 - i) << 3;
            arrby[i] = (byte)((this.value[-1 + this.blocks] & 255 << n6) >>> n6);
        }
        return arrby;
    }

    public BigInteger toFlexiBigInt() {
        if (this.len == 0 || this.isZero()) {
            return new BigInteger(0, new byte[0]);
        }
        return new BigInteger(1, this.toByteArray());
    }

    public int[] toIntegerArray() {
        int[] arrn = new int[this.blocks];
        System.arraycopy((Object)this.value, (int)0, (Object)arrn, (int)0, (int)this.blocks);
        return arrn;
    }

    public String toString(int n) {
        String string;
        char[] arrc = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        String[] arrstring = new String[]{"0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111"};
        String string2 = new String();
        if (n == 16) {
            int n2 = -1 + this.blocks;
            string = string2;
            for (int i = n2; i >= 0; --i) {
                String string3 = string + arrc[15 & this.value[i] >>> 28];
                String string4 = string3 + arrc[15 & this.value[i] >>> 24];
                String string5 = string4 + arrc[15 & this.value[i] >>> 20];
                String string6 = string5 + arrc[15 & this.value[i] >>> 16];
                String string7 = string6 + arrc[15 & this.value[i] >>> 12];
                String string8 = string7 + arrc[15 & this.value[i] >>> 8];
                String string9 = string8 + arrc[15 & this.value[i] >>> 4];
                String string10 = string9 + arrc[15 & this.value[i]];
                String string11 = string10 + " ";
                string = string11;
            }
        } else {
            int n3 = -1 + this.blocks;
            string = string2;
            for (int i = n3; i >= 0; --i) {
                String string12 = string + arrstring[15 & this.value[i] >>> 28];
                String string13 = string12 + arrstring[15 & this.value[i] >>> 24];
                String string14 = string13 + arrstring[15 & this.value[i] >>> 20];
                String string15 = string14 + arrstring[15 & this.value[i] >>> 16];
                String string16 = string15 + arrstring[15 & this.value[i] >>> 12];
                String string17 = string16 + arrstring[15 & this.value[i] >>> 8];
                String string18 = string17 + arrstring[15 & this.value[i] >>> 4];
                String string19 = string18 + arrstring[15 & this.value[i]];
                String string20 = string19 + " ";
                string = string20;
            }
        }
        return string;
    }

    public boolean vectorMult(GF2Polynomial gF2Polynomial) {
        boolean bl = false;
        if (this.len != gF2Polynomial.len) {
            throw new RuntimeException();
        }
        for (int i = 0; i < this.blocks; ++i) {
            int n = this.value[i] & gF2Polynomial.value[i];
            bl = bl ^ parity[n & 255] ^ parity[255 & n >>> 8] ^ parity[255 & n >>> 16] ^ parity[255 & n >>> 24];
        }
        return bl;
    }

    public GF2Polynomial xor(GF2Polynomial gF2Polynomial) {
        int n;
        GF2Polynomial gF2Polynomial2;
        int n2 = Math.min((int)this.blocks, (int)gF2Polynomial.blocks);
        if (this.len >= gF2Polynomial.len) {
            gF2Polynomial2 = new GF2Polynomial(this);
            for (n = 0; n < n2; ++n) {
                int[] arrn = gF2Polynomial2.value;
                arrn[n] = arrn[n] ^ gF2Polynomial.value[n];
            }
        } else {
            gF2Polynomial2 = new GF2Polynomial(gF2Polynomial);
            while (n < n2) {
                int[] arrn = gF2Polynomial2.value;
                arrn[n] = arrn[n] ^ this.value[n];
                ++n;
            }
        }
        gF2Polynomial2.zeroUnusedBits();
        return gF2Polynomial2;
    }

    public void xorBit(int n) {
        if (n < 0 || n > -1 + this.len) {
            throw new RuntimeException();
        }
        int[] arrn = this.value;
        int n2 = n >>> 5;
        arrn[n2] = arrn[n2] ^ bitMask[n & 31];
    }

    public void xorThisBy(GF2Polynomial gF2Polynomial) {
        for (int i = 0; i < Math.min((int)this.blocks, (int)gF2Polynomial.blocks); ++i) {
            int[] arrn = this.value;
            arrn[i] = arrn[i] ^ gF2Polynomial.value[i];
        }
        this.zeroUnusedBits();
    }
}

