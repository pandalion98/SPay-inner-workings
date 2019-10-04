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
import org.bouncycastle.crypto.params.RC2Parameters;

public class RC2Engine
implements BlockCipher {
    private static final int BLOCK_SIZE = 8;
    private static byte[] piTable = new byte[]{-39, 120, -7, -60, 25, -35, -75, -19, 40, -23, -3, 121, 74, -96, -40, -99, -58, 126, 55, -125, 43, 118, 83, -114, 98, 76, 100, -120, 68, -117, -5, -94, 23, -102, 89, -11, -121, -77, 79, 19, 97, 69, 109, -115, 9, -127, 125, 50, -67, -113, 64, -21, -122, -73, 123, 11, -16, -107, 33, 34, 92, 107, 78, -126, 84, -42, 101, -109, -50, 96, -78, 28, 115, 86, -64, 20, -89, -116, -15, -36, 18, 117, -54, 31, 59, -66, -28, -47, 66, 61, -44, 48, -93, 60, -74, 38, 111, -65, 14, -38, 70, 105, 7, 87, 39, -14, 29, -101, -68, -108, 67, 3, -8, 17, -57, -10, -112, -17, 62, -25, 6, -61, -43, 47, -56, 102, 30, -41, 8, -24, -22, -34, -128, 82, -18, -9, -124, -86, 114, -84, 53, 77, 106, 42, -106, 26, -46, 113, 90, 21, 73, 116, 75, -97, -48, 94, 4, 24, -92, -20, -62, -32, 65, 110, 15, 81, -53, -52, 36, -111, -81, 80, -95, -12, 112, 57, -103, 124, 58, -123, 35, -72, -76, 122, -4, 2, 54, 91, 37, 85, -105, 49, 45, 93, -6, -104, -29, -118, -110, -82, 5, -33, 41, 16, 103, 108, -70, -55, -45, 0, -26, -49, -31, -98, -88, 44, 99, 22, 1, 63, 88, -30, -119, -87, 13, 56, 52, 27, -85, 51, -1, -80, -69, 72, 12, 95, -71, -79, -51, 46, -59, -13, -37, 71, -27, -91, -100, 119, 10, -90, 32, 104, -2, 127, -63, -83};
    private boolean encrypting;
    private int[] workingKey;

    private void decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = ((255 & arrby[n2 + 7]) << 8) + (255 & arrby[n2 + 6]);
        int n5 = ((255 & arrby[n2 + 5]) << 8) + (255 & arrby[n2 + 4]);
        int n6 = ((255 & arrby[n2 + 3]) << 8) + (255 & arrby[n2 + 2]);
        int n7 = ((255 & arrby[n2 + 1]) << 8) + (255 & arrby[n2 + 0]);
        for (int i2 = 60; i2 >= 44; i2 -= 4) {
            n4 = this.rotateWordLeft(n4, 11) - ((n7 & ~n5) + (n6 & n5) + this.workingKey[i2 + 3]);
            n5 = this.rotateWordLeft(n5, 13) - ((n4 & ~n6) + (n7 & n6) + this.workingKey[i2 + 2]);
            n6 = this.rotateWordLeft(n6, 14) - ((n5 & ~n7) + (n4 & n7) + this.workingKey[i2 + 1]);
            n7 = this.rotateWordLeft(n7, 15) - ((n6 & ~n4) + (n5 & n4) + this.workingKey[i2]);
        }
        int n8 = n4 - this.workingKey[n5 & 63];
        int n9 = n5 - this.workingKey[n6 & 63];
        int n10 = n6 - this.workingKey[n7 & 63];
        int n11 = n7 - this.workingKey[n8 & 63];
        for (int i3 = 40; i3 >= 20; i3 -= 4) {
            n8 = this.rotateWordLeft(n8, 11) - ((n11 & ~n9) + (n10 & n9) + this.workingKey[i3 + 3]);
            n9 = this.rotateWordLeft(n9, 13) - ((n8 & ~n10) + (n11 & n10) + this.workingKey[i3 + 2]);
            n10 = this.rotateWordLeft(n10, 14) - ((n9 & ~n11) + (n8 & n11) + this.workingKey[i3 + 1]);
            n11 = this.rotateWordLeft(n11, 15) - ((n10 & ~n8) + (n9 & n8) + this.workingKey[i3]);
        }
        int n12 = n8 - this.workingKey[n9 & 63];
        int n13 = n9 - this.workingKey[n10 & 63];
        int n14 = n10 - this.workingKey[n11 & 63];
        int n15 = n11 - this.workingKey[n12 & 63];
        for (int i4 = 16; i4 >= 0; i4 -= 4) {
            n12 = this.rotateWordLeft(n12, 11) - ((n15 & ~n13) + (n14 & n13) + this.workingKey[i4 + 3]);
            n13 = this.rotateWordLeft(n13, 13) - ((n12 & ~n14) + (n15 & n14) + this.workingKey[i4 + 2]);
            n14 = this.rotateWordLeft(n14, 14) - ((n13 & ~n15) + (n12 & n15) + this.workingKey[i4 + 1]);
            n15 = this.rotateWordLeft(n15, 15) - ((n14 & ~n12) + (n13 & n12) + this.workingKey[i4]);
        }
        arrby2[n3 + 0] = (byte)n15;
        arrby2[n3 + 1] = (byte)(n15 >> 8);
        arrby2[n3 + 2] = (byte)n14;
        arrby2[n3 + 3] = (byte)(n14 >> 8);
        arrby2[n3 + 4] = (byte)n13;
        arrby2[n3 + 5] = (byte)(n13 >> 8);
        arrby2[n3 + 6] = (byte)n12;
        arrby2[n3 + 7] = (byte)(n12 >> 8);
    }

    private void encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = ((255 & arrby[n2 + 7]) << 8) + (255 & arrby[n2 + 6]);
        int n5 = ((255 & arrby[n2 + 5]) << 8) + (255 & arrby[n2 + 4]);
        int n6 = ((255 & arrby[n2 + 3]) << 8) + (255 & arrby[n2 + 2]);
        int n7 = ((255 & arrby[n2 + 1]) << 8) + (255 & arrby[n2 + 0]);
        for (int i2 = 0; i2 <= 16; i2 += 4) {
            n7 = this.rotateWordLeft(n7 + (n6 & ~n4) + (n5 & n4) + this.workingKey[i2], 1);
            n6 = this.rotateWordLeft(n6 + (n5 & ~n7) + (n4 & n7) + this.workingKey[i2 + 1], 2);
            n5 = this.rotateWordLeft(n5 + (n4 & ~n6) + (n7 & n6) + this.workingKey[i2 + 2], 3);
            n4 = this.rotateWordLeft(n4 + (n7 & ~n5) + (n6 & n5) + this.workingKey[i2 + 3], 5);
        }
        int n8 = n7 + this.workingKey[n4 & 63];
        int n9 = n6 + this.workingKey[n8 & 63];
        int n10 = n5 + this.workingKey[n9 & 63];
        int n11 = n4 + this.workingKey[n10 & 63];
        for (int i3 = 20; i3 <= 40; i3 += 4) {
            n8 = this.rotateWordLeft(n8 + (n9 & ~n11) + (n10 & n11) + this.workingKey[i3], 1);
            n9 = this.rotateWordLeft(n9 + (n10 & ~n8) + (n11 & n8) + this.workingKey[i3 + 1], 2);
            n10 = this.rotateWordLeft(n10 + (n11 & ~n9) + (n8 & n9) + this.workingKey[i3 + 2], 3);
            n11 = this.rotateWordLeft(n11 + (n8 & ~n10) + (n9 & n10) + this.workingKey[i3 + 3], 5);
        }
        int n12 = n8 + this.workingKey[n11 & 63];
        int n13 = n9 + this.workingKey[n12 & 63];
        int n14 = n10 + this.workingKey[n13 & 63];
        int n15 = n11 + this.workingKey[n14 & 63];
        for (int i4 = 44; i4 < 64; i4 += 4) {
            n12 = this.rotateWordLeft(n12 + (n13 & ~n15) + (n14 & n15) + this.workingKey[i4], 1);
            n13 = this.rotateWordLeft(n13 + (n14 & ~n12) + (n15 & n12) + this.workingKey[i4 + 1], 2);
            n14 = this.rotateWordLeft(n14 + (n15 & ~n13) + (n12 & n13) + this.workingKey[i4 + 2], 3);
            n15 = this.rotateWordLeft(n15 + (n12 & ~n14) + (n13 & n14) + this.workingKey[i4 + 3], 5);
        }
        arrby2[n3 + 0] = (byte)n12;
        arrby2[n3 + 1] = (byte)(n12 >> 8);
        arrby2[n3 + 2] = (byte)n13;
        arrby2[n3 + 3] = (byte)(n13 >> 8);
        arrby2[n3 + 4] = (byte)n14;
        arrby2[n3 + 5] = (byte)(n14 >> 8);
        arrby2[n3 + 6] = (byte)n15;
        arrby2[n3 + 7] = (byte)(n15 >> 8);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private int[] generateWorkingKey(byte[] var1_1, int var2_2) {
        var3_3 = 0;
        var4_4 = new int[128];
        for (var5_5 = 0; var5_5 != var1_1.length; ++var5_5) {
            var4_4[var5_5] = 255 & var1_1[var5_5];
        }
        var6_6 = var1_1.length;
        if (var6_6 >= 128) ** GOTO lbl19
        var11_7 = var4_4[var6_6 - 1];
        var12_8 = var6_6;
        var13_9 = var11_7;
        var14_10 = 0;
        do {
            block6 : {
                var15_11 = RC2Engine.piTable;
                var16_12 = var14_10 + 1;
                var13_9 = 255 & var15_11[255 & var13_9 + var4_4[var14_10]];
                var17_13 = var12_8 + 1;
                var4_4[var12_8] = var13_9;
                if (var17_13 < 128) break block6;
lbl19: // 2 sources:
                var7_14 = var2_2 + 7 >> 3;
                var4_4[128 - var7_14] = var8_15 = 255 & RC2Engine.piTable[var4_4[128 - var7_14] & 255 >> (7 & -var2_2)];
                for (var9_16 = -1 + (128 - var7_14); var9_16 >= 0; --var9_16) {
                    var4_4[var9_16] = var8_15 = 255 & RC2Engine.piTable[var8_15 ^ var4_4[var9_16 + var7_14]];
                }
                var10_17 = new int[64];
                while (var3_3 != var10_17.length) {
                    var10_17[var3_3] = var4_4[var3_3 * 2] + (var4_4[1 + var3_3 * 2] << 8);
                    ++var3_3;
                }
                return var10_17;
            }
            var12_8 = var17_13;
            var14_10 = var16_12;
        } while (true);
    }

    private int rotateWordLeft(int n2, int n3) {
        int n4 = 65535 & n2;
        return n4 << n3 | n4 >> 16 - n3;
    }

    @Override
    public String getAlgorithmName() {
        return "RC2";
    }

    @Override
    public int getBlockSize() {
        return 8;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        this.encrypting = bl;
        if (cipherParameters instanceof RC2Parameters) {
            RC2Parameters rC2Parameters = (RC2Parameters)cipherParameters;
            this.workingKey = this.generateWorkingKey(rC2Parameters.getKey(), rC2Parameters.getEffectiveKeyBits());
            return;
        }
        if (cipherParameters instanceof KeyParameter) {
            byte[] arrby = ((KeyParameter)cipherParameters).getKey();
            this.workingKey = this.generateWorkingKey(arrby, 8 * arrby.length);
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to RC2 init - " + cipherParameters.getClass().getName());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public final int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.workingKey == null) {
            throw new IllegalStateException("RC2 engine not initialised");
        }
        if (n2 + 8 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 8 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.encrypting) {
            this.encryptBlock(arrby, n2, arrby2, n3);
            do {
                return 8;
                break;
            } while (true);
        }
        this.decryptBlock(arrby, n2, arrby2, n3);
        return 8;
    }

    @Override
    public void reset() {
    }
}

