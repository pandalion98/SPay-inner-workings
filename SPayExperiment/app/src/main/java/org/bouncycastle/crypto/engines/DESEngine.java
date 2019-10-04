/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

public class DESEngine
implements BlockCipher {
    protected static final int BLOCK_SIZE = 8;
    private static final int[] SP1;
    private static final int[] SP2;
    private static final int[] SP3;
    private static final int[] SP4;
    private static final int[] SP5;
    private static final int[] SP6;
    private static final int[] SP7;
    private static final int[] SP8;
    private static final int[] bigbyte;
    private static final short[] bytebit;
    private static final byte[] pc1;
    private static final byte[] pc2;
    private static final byte[] totrot;
    private int[] workingKey = null;

    static {
        bytebit = new short[]{128, 64, 32, 16, 8, 4, 2, 1};
        bigbyte = new int[]{8388608, 4194304, 2097152, 1048576, 524288, 262144, 131072, 65536, 32768, 16384, 8192, 4096, 2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1};
        pc1 = new byte[]{56, 48, 40, 32, 24, 16, 8, 0, 57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 60, 52, 44, 36, 28, 20, 12, 4, 27, 19, 11, 3};
        totrot = new byte[]{1, 2, 4, 6, 8, 10, 12, 14, 15, 17, 19, 21, 23, 25, 27, 28};
        pc2 = new byte[]{13, 16, 10, 23, 0, 4, 2, 27, 14, 5, 20, 9, 22, 18, 11, 3, 25, 7, 15, 6, 26, 19, 12, 1, 40, 51, 30, 36, 46, 54, 29, 39, 50, 44, 32, 47, 43, 48, 38, 55, 33, 52, 45, 41, 49, 35, 28, 31};
        SP1 = new int[]{16843776, 0, 65536, 16843780, 16842756, 66564, 4, 65536, 1024, 16843776, 16843780, 1024, 16778244, 16842756, 16777216, 4, 1028, 16778240, 16778240, 66560, 66560, 16842752, 16842752, 16778244, 65540, 16777220, 16777220, 65540, 0, 1028, 66564, 16777216, 65536, 16843780, 4, 16842752, 16843776, 16777216, 16777216, 1024, 16842756, 65536, 66560, 16777220, 1024, 4, 16778244, 66564, 16843780, 65540, 16842752, 16778244, 16777220, 1028, 66564, 16843776, 1028, 16778240, 16778240, 0, 65540, 66560, 0, 16842756};
        SP2 = new int[]{-2146402272, -2147450880, 32768, 1081376, 1048576, 32, -2146435040, -2147450848, -2147483616, -2146402272, -2146402304, Integer.MIN_VALUE, -2147450880, 1048576, 32, -2146435040, 1081344, 1048608, -2147450848, 0, Integer.MIN_VALUE, 32768, 1081376, -2146435072, 1048608, -2147483616, 0, 1081344, 32800, -2146402304, -2146435072, 32800, 0, 1081376, -2146435040, 1048576, -2147450848, -2146435072, -2146402304, 32768, -2146435072, -2147450880, 32, -2146402272, 1081376, 32, 32768, Integer.MIN_VALUE, 32800, -2146402304, 1048576, -2147483616, 1048608, -2147450848, -2147483616, 1048608, 1081344, 0, -2147450880, 32800, Integer.MIN_VALUE, -2146435040, -2146402272, 1081344};
        SP3 = new int[]{520, 134349312, 0, 134348808, 134218240, 0, 131592, 134218240, 131080, 134217736, 134217736, 131072, 134349320, 131080, 134348800, 520, 134217728, 8, 134349312, 512, 131584, 134348800, 134348808, 131592, 134218248, 131584, 131072, 134218248, 8, 134349320, 512, 134217728, 134349312, 134217728, 131080, 520, 131072, 134349312, 134218240, 0, 512, 131080, 134349320, 134218240, 134217736, 512, 0, 134348808, 134218248, 131072, 134217728, 134349320, 8, 131592, 131584, 134217736, 134348800, 134218248, 520, 134348800, 131592, 8, 134348808, 131584};
        SP4 = new int[]{8396801, 8321, 8321, 128, 8396928, 8388737, 8388609, 8193, 0, 8396800, 8396800, 8396929, 129, 0, 8388736, 8388609, 1, 8192, 8388608, 8396801, 128, 8388608, 8193, 8320, 8388737, 1, 8320, 8388736, 8192, 8396928, 8396929, 129, 8388736, 8388609, 8396800, 8396929, 129, 0, 0, 8396800, 8320, 8388736, 8388737, 1, 8396801, 8321, 8321, 128, 8396929, 129, 1, 8192, 8388609, 8193, 8396928, 8388737, 8193, 8320, 8388608, 8396801, 128, 8388608, 8192, 8396928};
        SP5 = new int[]{256, 34078976, 34078720, 1107296512, 524288, 256, 1073741824, 34078720, 1074266368, 524288, 33554688, 1074266368, 1107296512, 1107820544, 524544, 1073741824, 33554432, 1074266112, 1074266112, 0, 1073742080, 1107820800, 1107820800, 33554688, 1107820544, 1073742080, 0, 1107296256, 34078976, 33554432, 1107296256, 524544, 524288, 1107296512, 256, 33554432, 1073741824, 34078720, 1107296512, 1074266368, 33554688, 1073741824, 1107820544, 34078976, 1074266368, 256, 33554432, 1107820544, 1107820800, 524544, 1107296256, 1107820800, 34078720, 0, 1074266112, 1107296256, 524544, 33554688, 1073742080, 524288, 0, 1074266112, 34078976, 1073742080};
        SP6 = new int[]{536870928, 541065216, 16384, 541081616, 541065216, 16, 541081616, 4194304, 536887296, 4210704, 4194304, 536870928, 4194320, 536887296, 536870912, 16400, 0, 4194320, 536887312, 16384, 4210688, 536887312, 16, 541065232, 541065232, 0, 4210704, 541081600, 16400, 4210688, 541081600, 536870912, 536887296, 16, 541065232, 4210688, 541081616, 4194304, 16400, 536870928, 4194304, 536887296, 536870912, 16400, 536870928, 541081616, 4210688, 541065216, 4210704, 541081600, 0, 541065232, 16, 16384, 541065216, 4210704, 16384, 4194320, 536887312, 0, 541081600, 536870912, 4194320, 536887312};
        SP7 = new int[]{2097152, 69206018, 67110914, 0, 2048, 67110914, 2099202, 69208064, 69208066, 2097152, 0, 67108866, 2, 67108864, 69206018, 2050, 67110912, 2099202, 2097154, 67110912, 67108866, 69206016, 69208064, 2097154, 69206016, 2048, 2050, 69208066, 2099200, 2, 67108864, 2099200, 67108864, 2099200, 2097152, 67110914, 67110914, 69206018, 69206018, 2, 2097154, 67108864, 67110912, 2097152, 69208064, 2050, 2099202, 69208064, 2050, 67108866, 69208066, 69206016, 2099200, 0, 2, 69208066, 0, 2099202, 69206016, 2048, 67108866, 67110912, 2048, 2097154};
        SP8 = new int[]{268439616, 4096, 262144, 268701760, 268435456, 268439616, 64, 268435456, 262208, 268697600, 268701760, 266240, 268701696, 266304, 4096, 64, 268697600, 268435520, 268439552, 4160, 266240, 262208, 268697664, 268701696, 4160, 0, 0, 268697664, 268435520, 268439552, 266304, 262144, 266304, 262144, 268701696, 4096, 64, 268697664, 4096, 266304, 268439552, 64, 268435520, 268697600, 268697664, 268435456, 262144, 268439616, 0, 268701760, 262208, 268435520, 268697600, 268439552, 268439616, 0, 268701760, 266240, 266240, 4160, 4160, 262208, 268435456, 268701696};
    }

    protected void desFunc(int[] arrn, byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = (255 & arrby[n2 + 0]) << 24 | (255 & arrby[n2 + 1]) << 16 | (255 & arrby[n2 + 2]) << 8 | 255 & arrby[n2 + 3];
        int n5 = (255 & arrby[n2 + 4]) << 24 | (255 & arrby[n2 + 5]) << 16 | (255 & arrby[n2 + 6]) << 8 | 255 & arrby[n2 + 7];
        int n6 = 252645135 & (n5 ^ n4 >>> 4);
        int n7 = n5 ^ n6;
        int n8 = n4 ^ n6 << 4;
        int n9 = 65535 & (n7 ^ n8 >>> 16);
        int n10 = n7 ^ n9;
        int n11 = n8 ^ n9 << 16;
        int n12 = 858993459 & (n11 ^ n10 >>> 2);
        int n13 = n11 ^ n12;
        int n14 = n10 ^ n12 << 2;
        int n15 = 16711935 & (n13 ^ n14 >>> 8);
        int n16 = n13 ^ n15;
        int n17 = n14 ^ n15 << 8;
        int n18 = -1 & (n17 << 1 | 1 & n17 >>> 31);
        int n19 = -1431655766 & (n16 ^ n18);
        int n20 = n16 ^ n19;
        int n21 = n19 ^ n18;
        int n22 = -1 & (n20 << 1 | 1 & n20 >>> 31);
        for (int i2 = 0; i2 < 8; ++i2) {
            int n23 = (n21 << 28 | n21 >>> 4) ^ arrn[0 + i2 * 4];
            int n24 = SP7[n23 & 63] | SP5[63 & n23 >>> 8] | SP3[63 & n23 >>> 16] | SP1[63 & n23 >>> 24];
            int n25 = n21 ^ arrn[1 + i2 * 4];
            int n26 = ((n22 ^= n24 | SP8[n25 & 63] | SP6[63 & n25 >>> 8] | SP4[63 & n25 >>> 16] | SP2[63 & n25 >>> 24]) << 28 | n22 >>> 4) ^ arrn[2 + i2 * 4];
            int n27 = SP7[n26 & 63] | SP5[63 & n26 >>> 8] | SP3[63 & n26 >>> 16] | SP1[63 & n26 >>> 24];
            int n28 = n22 ^ arrn[3 + i2 * 4];
            n21 ^= n27 | SP8[n28 & 63] | SP6[63 & n28 >>> 8] | SP4[63 & n28 >>> 16] | SP2[63 & n28 >>> 24];
        }
        int n29 = n21 << 31 | n21 >>> 1;
        int n30 = -1431655766 & (n22 ^ n29);
        int n31 = n22 ^ n30;
        int n32 = n29 ^ n30;
        int n33 = n31 << 31 | n31 >>> 1;
        int n34 = 16711935 & (n32 ^ n33 >>> 8);
        int n35 = n32 ^ n34;
        int n36 = n33 ^ n34 << 8;
        int n37 = 858993459 & (n35 ^ n36 >>> 2);
        int n38 = n35 ^ n37;
        int n39 = n36 ^ n37 << 2;
        int n40 = 65535 & (n39 ^ n38 >>> 16);
        int n41 = n39 ^ n40;
        int n42 = n38 ^ n40 << 16;
        int n43 = 252645135 & (n41 ^ n42 >>> 4);
        int n44 = n41 ^ n43;
        int n45 = n42 ^ n43 << 4;
        arrby2[n3 + 0] = (byte)(255 & n45 >>> 24);
        arrby2[n3 + 1] = (byte)(255 & n45 >>> 16);
        arrby2[n3 + 2] = (byte)(255 & n45 >>> 8);
        arrby2[n3 + 3] = (byte)(n45 & 255);
        arrby2[n3 + 4] = (byte)(255 & n44 >>> 24);
        arrby2[n3 + 5] = (byte)(255 & n44 >>> 16);
        arrby2[n3 + 6] = (byte)(255 & n44 >>> 8);
        arrby2[n3 + 7] = (byte)(n44 & 255);
    }

    /*
     * Enabled aggressive block sorting
     */
    protected int[] generateWorkingKey(boolean bl, byte[] arrby) {
        int[] arrn = new int[32];
        boolean[] arrbl = new boolean[56];
        boolean[] arrbl2 = new boolean[56];
        for (int i2 = 0; i2 < 56; ++i2) {
            byte by = pc1[i2];
            boolean bl2 = (arrby[by >>> 3] & bytebit[by & 7]) != 0;
            arrbl[i2] = bl2;
        }
        int n2 = 0;
        do {
            int n3;
            int n4;
            if (n2 < 16) {
                n3 = bl ? n2 << 1 : 15 - n2 << 1;
                n4 = n3 + 1;
                arrn[n4] = 0;
                arrn[n3] = 0;
            } else {
                int n5 = 0;
                do {
                    if (n5 == 32) {
                        return arrn;
                    }
                    int n6 = arrn[n5];
                    int n7 = arrn[n5 + 1];
                    arrn[n5] = (16515072 & n6) << 6 | (n6 & 4032) << 10 | (16515072 & n7) >>> 10 | (n7 & 4032) >>> 6;
                    arrn[n5 + 1] = (258048 & n6) << 12 | (n6 & 63) << 16 | (258048 & n7) >>> 4 | n7 & 63;
                    n5 += 2;
                } while (true);
            }
            for (int i3 = 0; i3 < 28; ++i3) {
                int n8 = i3 + totrot[n2];
                arrbl2[i3] = n8 < 28 ? arrbl[n8] : arrbl[n8 - 28];
            }
            for (int i4 = 28; i4 < 56; ++i4) {
                int n9 = i4 + totrot[n2];
                arrbl2[i4] = n9 < 56 ? arrbl[n9] : arrbl[n9 - 28];
            }
            for (int i5 = 0; i5 < 24; ++i5) {
                if (arrbl2[pc2[i5]]) {
                    arrn[n3] = arrn[n3] | bigbyte[i5];
                }
                if (!arrbl2[pc2[i5 + 24]]) continue;
                arrn[n4] = arrn[n4] | bigbyte[i5];
            }
            ++n2;
        } while (true);
    }

    @Override
    public String getAlgorithmName() {
        return "DES";
    }

    @Override
    public int getBlockSize() {
        return 8;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            if (((KeyParameter)cipherParameters).getKey().length > 8) {
                throw new IllegalArgumentException("DES key too long - should be 8 bytes");
            }
            this.workingKey = this.generateWorkingKey(bl, ((KeyParameter)cipherParameters).getKey());
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to DES init - " + cipherParameters.getClass().getName());
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.workingKey == null) {
            throw new IllegalStateException("DES engine not initialised");
        }
        if (n2 + 8 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 8 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        this.desFunc(this.workingKey, arrby, n2, arrby2, n3);
        return 8;
    }

    @Override
    public void reset() {
    }
}

