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

public final class TwofishEngine
implements BlockCipher {
    private static final int BLOCK_SIZE = 16;
    private static final int GF256_FDBK = 361;
    private static final int GF256_FDBK_2 = 180;
    private static final int GF256_FDBK_4 = 90;
    private static final int INPUT_WHITEN = 0;
    private static final int MAX_KEY_BITS = 256;
    private static final int MAX_ROUNDS = 16;
    private static final int OUTPUT_WHITEN = 4;
    private static final byte[][] P = new byte[][]{{-87, 103, -77, -24, 4, -3, -93, 118, -102, -110, -128, 120, -28, -35, -47, 56, 13, -58, 53, -104, 24, -9, -20, 108, 67, 117, 55, 38, -6, 19, -108, 72, -14, -48, -117, 48, -124, 84, -33, 35, 25, 91, 61, 89, -13, -82, -94, -126, 99, 1, -125, 46, -39, 81, -101, 124, -90, -21, -91, -66, 22, 12, -29, 97, -64, -116, 58, -11, 115, 44, 37, 11, -69, 78, -119, 107, 83, 106, -76, -15, -31, -26, -67, 69, -30, -12, -74, 102, -52, -107, 3, 86, -44, 28, 30, -41, -5, -61, -114, -75, -23, -49, -65, -70, -22, 119, 57, -81, 51, -55, 98, 113, -127, 121, 9, -83, 36, -51, -7, -40, -27, -59, -71, 77, 68, 8, -122, -25, -95, 29, -86, -19, 6, 112, -78, -46, 65, 123, -96, 17, 49, -62, 39, -112, 32, -10, 96, -1, -106, 92, -79, -85, -98, -100, 82, 27, 95, -109, 10, -17, -111, -123, 73, -18, 45, 79, -113, 59, 71, -121, 109, 70, -42, 62, 105, 100, 42, -50, -53, 47, -4, -105, 5, 122, -84, 127, -43, 26, 75, 14, -89, 90, 40, 20, 63, 41, -120, 60, 76, 2, -72, -38, -80, 23, 85, 31, -118, 125, 87, -57, -115, 116, -73, -60, -97, 114, 126, 21, 34, 18, 88, 7, -103, 52, 110, 80, -34, 104, 101, -68, -37, -8, -56, -88, 43, 64, -36, -2, 50, -92, -54, 16, 33, -16, -45, 93, 15, 0, 111, -99, 54, 66, 74, 94, -63, -32}, {117, -13, -58, -12, -37, 123, -5, -56, 74, -45, -26, 107, 69, 125, -24, 75, -42, 50, -40, -3, 55, 113, -15, -31, 48, 15, -8, 27, -121, -6, 6, 63, 94, -70, -82, 91, -118, 0, -68, -99, 109, -63, -79, 14, -128, 93, -46, -43, -96, -124, 7, 20, -75, -112, 44, -93, -78, 115, 76, 84, -110, 116, 54, 81, 56, -80, -67, 90, -4, 96, 98, -106, 108, 66, -9, 16, 124, 40, 39, -116, 19, -107, -100, -57, 36, 70, 59, 112, -54, -29, -123, -53, 17, -48, -109, -72, -90, -125, 32, -1, -97, 119, -61, -52, 3, 111, 8, -65, 64, -25, 43, -30, 121, 12, -86, -126, 65, 58, -22, -71, -28, -102, -92, -105, 126, -38, 122, 23, 102, -108, -95, 29, 61, -16, -34, -77, 11, 114, -89, 28, -17, -47, 83, 62, -113, 51, 38, 95, -20, 118, 42, 73, -127, -120, -18, 33, -60, 26, -21, -39, -59, 57, -103, -51, -83, 49, -117, 1, 24, 35, -35, 31, 78, 45, -7, 72, 79, -14, 101, -114, 120, 92, 88, 25, -115, -27, -104, 87, 103, 127, 5, 100, -81, 99, -74, -2, -11, -73, 60, -91, -50, -23, 104, 68, -32, 77, 67, 105, 41, 46, -84, 21, 89, -88, 10, -98, 110, 71, -33, 52, 53, 106, -49, -36, 34, -55, -64, -101, -119, -44, -19, -85, 18, -94, 13, 82, -69, 2, 47, -87, -41, 97, 30, -76, 80, 4, -10, -62, 22, 37, -122, 86, 85, 9, -66, -111}};
    private static final int P_00 = 1;
    private static final int P_01 = 0;
    private static final int P_02 = 0;
    private static final int P_03 = 1;
    private static final int P_04 = 1;
    private static final int P_10 = 0;
    private static final int P_11 = 0;
    private static final int P_12 = 1;
    private static final int P_13 = 1;
    private static final int P_14 = 0;
    private static final int P_20 = 1;
    private static final int P_21 = 1;
    private static final int P_22 = 0;
    private static final int P_23 = 0;
    private static final int P_24 = 0;
    private static final int P_30 = 0;
    private static final int P_31 = 1;
    private static final int P_32 = 1;
    private static final int P_33 = 0;
    private static final int P_34 = 1;
    private static final int ROUNDS = 16;
    private static final int ROUND_SUBKEYS = 8;
    private static final int RS_GF_FDBK = 333;
    private static final int SK_BUMP = 16843009;
    private static final int SK_ROTL = 9;
    private static final int SK_STEP = 33686018;
    private static final int TOTAL_SUBKEYS = 40;
    private boolean encrypting = false;
    private int[] gMDS0 = new int[256];
    private int[] gMDS1 = new int[256];
    private int[] gMDS2 = new int[256];
    private int[] gMDS3 = new int[256];
    private int[] gSBox;
    private int[] gSubKeys;
    private int k64Cnt = 0;
    private byte[] workingKey = null;

    public TwofishEngine() {
        int[] arrn = new int[2];
        int[] arrn2 = new int[2];
        int[] arrn3 = new int[2];
        for (int i2 = 0; i2 < 256; ++i2) {
            int n2;
            int n3;
            arrn[0] = n2 = 255 & P[0][i2];
            arrn2[0] = 255 & this.Mx_X(n2);
            arrn3[0] = 255 & this.Mx_Y(n2);
            arrn[1] = n3 = 255 & P[1][i2];
            arrn2[1] = 255 & this.Mx_X(n3);
            arrn3[1] = 255 & this.Mx_Y(n3);
            this.gMDS0[i2] = arrn[1] | arrn2[1] << 8 | arrn3[1] << 16 | arrn3[1] << 24;
            this.gMDS1[i2] = arrn3[0] | arrn3[0] << 8 | arrn2[0] << 16 | arrn[0] << 24;
            this.gMDS2[i2] = arrn2[1] | arrn3[1] << 8 | arrn[1] << 16 | arrn3[1] << 24;
            this.gMDS3[i2] = arrn2[0] | arrn[0] << 8 | arrn3[0] << 16 | arrn2[0] << 24;
        }
    }

    private void Bits32ToBytes(int n2, byte[] arrby, int n3) {
        arrby[n3] = (byte)n2;
        arrby[n3 + 1] = (byte)(n2 >> 8);
        arrby[n3 + 2] = (byte)(n2 >> 16);
        arrby[n3 + 3] = (byte)(n2 >> 24);
    }

    private int BytesTo32Bits(byte[] arrby, int n2) {
        return 255 & arrby[n2] | (255 & arrby[n2 + 1]) << 8 | (255 & arrby[n2 + 2]) << 16 | (255 & arrby[n2 + 3]) << 24;
    }

    private int F32(int n2, int[] arrn) {
        int n3 = this.b0(n2);
        int n4 = this.b1(n2);
        int n5 = this.b2(n2);
        int n6 = this.b3(n2);
        int n7 = arrn[0];
        int n8 = arrn[1];
        int n9 = arrn[2];
        int n10 = arrn[3];
        switch (3 & this.k64Cnt) {
            default: {
                return 0;
            }
            case 1: {
                return this.gMDS0[255 & P[0][n3] ^ this.b0(n7)] ^ this.gMDS1[255 & P[0][n4] ^ this.b1(n7)] ^ this.gMDS2[255 & P[1][n5] ^ this.b2(n7)] ^ this.gMDS3[255 & P[1][n6] ^ this.b3(n7)];
            }
            case 0: {
                n3 = 255 & P[1][n3] ^ this.b0(n10);
                n4 = 255 & P[0][n4] ^ this.b1(n10);
                n5 = 255 & P[0][n5] ^ this.b2(n10);
                n6 = 255 & P[1][n6] ^ this.b3(n10);
            }
            case 3: {
                n3 = 255 & P[1][n3] ^ this.b0(n9);
                n4 = 255 & P[1][n4] ^ this.b1(n9);
                n5 = 255 & P[0][n5] ^ this.b2(n9);
                n6 = 255 & P[0][n6] ^ this.b3(n9);
            }
            case 2: 
        }
        return this.gMDS0[255 & P[0][255 & P[0][n3] ^ this.b0(n8)] ^ this.b0(n7)] ^ this.gMDS1[255 & P[0][255 & P[1][n4] ^ this.b1(n8)] ^ this.b1(n7)] ^ this.gMDS2[255 & P[1][255 & P[0][n5] ^ this.b2(n8)] ^ this.b2(n7)] ^ this.gMDS3[255 & P[1][255 & P[1][n6] ^ this.b3(n8)] ^ this.b3(n7)];
    }

    private int Fe32_0(int n2) {
        return this.gSBox[0 + 2 * (n2 & 255)] ^ this.gSBox[1 + 2 * (255 & n2 >>> 8)] ^ this.gSBox[512 + 2 * (255 & n2 >>> 16)] ^ this.gSBox[513 + 2 * (255 & n2 >>> 24)];
    }

    private int Fe32_3(int n2) {
        return this.gSBox[0 + 2 * (255 & n2 >>> 24)] ^ this.gSBox[1 + 2 * (n2 & 255)] ^ this.gSBox[512 + 2 * (255 & n2 >>> 8)] ^ this.gSBox[513 + 2 * (255 & n2 >>> 16)];
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private int LFSR1(int n2) {
        int n3;
        int n4 = n2 >> 1;
        if ((n2 & 1) != 0) {
            n3 = 180;
            do {
                return n3 ^ n4;
                break;
            } while (true);
        }
        n3 = 0;
        return n3 ^ n4;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int LFSR2(int n2) {
        int n3 = n2 >> 2;
        int n4 = (n2 & 2) != 0 ? 180 : 0;
        int n5 = n4 ^ n3;
        int n6 = n2 & 1;
        int n7 = 0;
        if (n6 != 0) {
            n7 = 90;
        }
        return n7 ^ n5;
    }

    private int Mx_X(int n2) {
        return n2 ^ this.LFSR2(n2);
    }

    private int Mx_Y(int n2) {
        return n2 ^ this.LFSR1(n2) ^ this.LFSR2(n2);
    }

    private int RS_MDS_Encode(int n2, int n3) {
        int n4 = 0;
        for (int i2 = 0; i2 < 4; ++i2) {
            n3 = this.RS_rem(n3);
        }
        int n5 = n3 ^ n2;
        while (n4 < 4) {
            n5 = this.RS_rem(n5);
            ++n4;
        }
        return n5;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int RS_rem(int n2) {
        int n3 = 255 & n2 >>> 24;
        int n4 = n3 << 1;
        int n5 = (n3 & 128) != 0 ? 333 : 0;
        int n6 = 255 & (n5 ^ n4);
        int n7 = n3 >>> 1;
        int n8 = n3 & 1;
        int n9 = 0;
        if (n8 != 0) {
            n9 = 166;
        }
        int n10 = n6 ^ (n9 ^ n7);
        return n3 ^ (n2 << 8 ^ n10 << 24 ^ n6 << 16 ^ n10 << 8);
    }

    private int b0(int n2) {
        return n2 & 255;
    }

    private int b1(int n2) {
        return 255 & n2 >>> 8;
    }

    private int b2(int n2) {
        return 255 & n2 >>> 16;
    }

    private int b3(int n2) {
        return 255 & n2 >>> 24;
    }

    private void decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = this.BytesTo32Bits(arrby, n2) ^ this.gSubKeys[4];
        int n5 = this.BytesTo32Bits(arrby, n2 + 4) ^ this.gSubKeys[5];
        int n6 = this.BytesTo32Bits(arrby, n2 + 8) ^ this.gSubKeys[6];
        int n7 = this.BytesTo32Bits(arrby, n2 + 12) ^ this.gSubKeys[7];
        int n8 = 39;
        for (int i2 = 0; i2 < 16; i2 += 2) {
            int n9 = this.Fe32_0(n4);
            int n10 = this.Fe32_3(n5);
            int n11 = n9 + n10 * 2;
            int[] arrn = this.gSubKeys;
            int n12 = n8 - 1;
            int n13 = n7 ^ n11 + arrn[n8];
            int n14 = n6 << 1 | n6 >>> 31;
            int n15 = n9 + n10;
            int[] arrn2 = this.gSubKeys;
            int n16 = n12 - 1;
            n6 = n14 ^ n15 + arrn2[n12];
            n7 = n13 >>> 1 | n13 << 31;
            int n17 = this.Fe32_0(n6);
            int n18 = this.Fe32_3(n7);
            int n19 = n17 + n18 * 2;
            int[] arrn3 = this.gSubKeys;
            int n20 = n16 - 1;
            int n21 = n5 ^ n19 + arrn3[n16];
            int n22 = n4 << 1 | n4 >>> 31;
            int n23 = n18 + n17;
            int[] arrn4 = this.gSubKeys;
            n8 = n20 - 1;
            n4 = n22 ^ n23 + arrn4[n20];
            n5 = n21 >>> 1 | n21 << 31;
        }
        this.Bits32ToBytes(n6 ^ this.gSubKeys[0], arrby2, n3);
        this.Bits32ToBytes(n7 ^ this.gSubKeys[1], arrby2, n3 + 4);
        this.Bits32ToBytes(n4 ^ this.gSubKeys[2], arrby2, n3 + 8);
        this.Bits32ToBytes(n5 ^ this.gSubKeys[3], arrby2, n3 + 12);
    }

    private void encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = this.BytesTo32Bits(arrby, n2) ^ this.gSubKeys[0];
        int n5 = this.BytesTo32Bits(arrby, n2 + 4) ^ this.gSubKeys[1];
        int n6 = this.BytesTo32Bits(arrby, n2 + 8) ^ this.gSubKeys[2];
        int n7 = this.BytesTo32Bits(arrby, n2 + 12) ^ this.gSubKeys[3];
        int n8 = 8;
        for (int i2 = 0; i2 < 16; i2 += 2) {
            int n9 = this.Fe32_0(n4);
            int n10 = this.Fe32_3(n5);
            int n11 = n9 + n10;
            int[] arrn = this.gSubKeys;
            int n12 = n8 + 1;
            int n13 = n6 ^ n11 + arrn[n8];
            n6 = n13 >>> 1 | n13 << 31;
            int n14 = n7 << 1 | n7 >>> 31;
            int n15 = n9 + n10 * 2;
            int[] arrn2 = this.gSubKeys;
            int n16 = n12 + 1;
            n7 = n14 ^ n15 + arrn2[n12];
            int n17 = this.Fe32_0(n6);
            int n18 = this.Fe32_3(n7);
            int n19 = n17 + n18;
            int[] arrn3 = this.gSubKeys;
            int n20 = n16 + 1;
            int n21 = n4 ^ n19 + arrn3[n16];
            n4 = n21 >>> 1 | n21 << 31;
            int n22 = n5 << 1 | n5 >>> 31;
            int n23 = n17 + n18 * 2;
            int[] arrn4 = this.gSubKeys;
            n8 = n20 + 1;
            n5 = n22 ^ n23 + arrn4[n20];
        }
        this.Bits32ToBytes(n6 ^ this.gSubKeys[4], arrby2, n3);
        this.Bits32ToBytes(n7 ^ this.gSubKeys[5], arrby2, n3 + 4);
        this.Bits32ToBytes(n4 ^ this.gSubKeys[6], arrby2, n3 + 8);
        this.Bits32ToBytes(n5 ^ this.gSubKeys[7], arrby2, n3 + 12);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private void setKey(byte[] var1_1) {
        var2_2 = new int[4];
        var3_3 = new int[4];
        var4_4 = new int[4];
        this.gSubKeys = new int[40];
        if (this.k64Cnt < 1) {
            throw new IllegalArgumentException("Key size less than 64 bits");
        }
        if (this.k64Cnt > 4) {
            throw new IllegalArgumentException("Key size larger than 256 bits");
        }
        for (var5_5 = 0; var5_5 < this.k64Cnt; ++var5_5) {
            var26_6 = var5_5 * 8;
            var2_2[var5_5] = this.BytesTo32Bits(var1_1, var26_6);
            var3_3[var5_5] = this.BytesTo32Bits(var1_1, var26_6 + 4);
            var4_4[-1 + this.k64Cnt - var5_5] = this.RS_MDS_Encode(var2_2[var5_5], var3_3[var5_5]);
        }
        for (var6_7 = 0; var6_7 < 20; ++var6_7) {
            var20_8 = 33686018 * var6_7;
            var21_9 = this.F32(var20_8, var2_2);
            var22_10 = this.F32(var20_8 + 16843009, var3_3);
            var23_11 = var22_10 << 8 | var22_10 >>> 24;
            this.gSubKeys[var6_7 * 2] = var24_12 = var21_9 + var23_11;
            var25_13 = var23_11 + var24_12;
            this.gSubKeys[1 + var6_7 * 2] = var25_13 << 9 | var25_13 >>> 23;
        }
        var7_14 = var4_4[0];
        var8_15 = var4_4[1];
        var9_16 = var4_4[2];
        var10_17 = var4_4[3];
        this.gSBox = new int[1024];
        var11_18 = 0;
        while (var11_18 < 256) {
            switch (3 & this.k64Cnt) {
                case 1: {
                    this.gSBox[var11_18 * 2] = this.gMDS0[255 & TwofishEngine.P[0][var11_18] ^ this.b0(var7_14)];
                    this.gSBox[1 + var11_18 * 2] = this.gMDS1[255 & TwofishEngine.P[0][var11_18] ^ this.b1(var7_14)];
                    this.gSBox[512 + var11_18 * 2] = this.gMDS2[255 & TwofishEngine.P[1][var11_18] ^ this.b2(var7_14)];
                    this.gSBox[513 + var11_18 * 2] = this.gMDS3[255 & TwofishEngine.P[1][var11_18] ^ this.b3(var7_14)];
                    break;
                }
                case 0: {
                    var19_26 = 255 & TwofishEngine.P[1][var11_18] ^ this.b0(var10_17);
                    var18_25 = 255 & TwofishEngine.P[0][var11_18] ^ this.b1(var10_17);
                    var17_24 = 255 & TwofishEngine.P[0][var11_18] ^ this.b2(var10_17);
                    var16_23 = 255 & TwofishEngine.P[1][var11_18] ^ this.b3(var10_17);
                    ** GOTO lbl49
                }
                case 3: {
                    var16_23 = var11_18;
                    var17_24 = var11_18;
                    var18_25 = var11_18;
                    var19_26 = var11_18;
lbl49: // 2 sources:
                    var15_22 = 255 & TwofishEngine.P[1][var19_26] ^ this.b0(var9_16);
                    var14_21 = 255 & TwofishEngine.P[1][var18_25] ^ this.b1(var9_16);
                    var13_20 = 255 & TwofishEngine.P[0][var17_24] ^ this.b2(var9_16);
                    var12_19 = 255 & TwofishEngine.P[0][var16_23] ^ this.b3(var9_16);
                    ** GOTO lbl59
                }
                case 2: {
                    var12_19 = var11_18;
                    var13_20 = var11_18;
                    var14_21 = var11_18;
                    var15_22 = var11_18;
lbl59: // 2 sources:
                    this.gSBox[var11_18 * 2] = this.gMDS0[255 & TwofishEngine.P[0][255 & TwofishEngine.P[0][var15_22] ^ this.b0(var8_15)] ^ this.b0(var7_14)];
                    this.gSBox[1 + var11_18 * 2] = this.gMDS1[255 & TwofishEngine.P[0][255 & TwofishEngine.P[1][var14_21] ^ this.b1(var8_15)] ^ this.b1(var7_14)];
                    this.gSBox[512 + var11_18 * 2] = this.gMDS2[255 & TwofishEngine.P[1][255 & TwofishEngine.P[0][var13_20] ^ this.b2(var8_15)] ^ this.b2(var7_14)];
                    this.gSBox[513 + var11_18 * 2] = this.gMDS3[255 & TwofishEngine.P[1][255 & TwofishEngine.P[1][var12_19] ^ this.b3(var8_15)] ^ this.b3(var7_14)];
                }
            }
            ++var11_18;
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Twofish";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.encrypting = bl;
            this.workingKey = ((KeyParameter)cipherParameters).getKey();
            this.k64Cnt = this.workingKey.length / 8;
            this.setKey(this.workingKey);
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to Twofish init - " + cipherParameters.getClass().getName());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.workingKey == null) {
            throw new IllegalStateException("Twofish not initialised");
        }
        if (n2 + 16 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 16 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.encrypting) {
            this.encryptBlock(arrby, n2, arrby2, n3);
            do {
                return 16;
                break;
            } while (true);
        }
        this.decryptBlock(arrby, n2, arrby2, n3);
        return 16;
    }

    @Override
    public void reset() {
        if (this.workingKey != null) {
            this.setKey(this.workingKey);
        }
    }
}

