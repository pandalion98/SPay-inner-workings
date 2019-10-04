/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Byte
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Array
 */
package org.bouncycastle.crypto.engines;

import java.lang.reflect.Array;
import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

public class RijndaelEngine
implements BlockCipher {
    private static final int MAXKC = 64;
    private static final int MAXROUNDS = 14;
    private static final byte[] S;
    private static final byte[] Si;
    private static final byte[] aLogtable;
    private static final byte[] logtable;
    private static final int[] rcon;
    static byte[][] shifts0;
    static byte[][] shifts1;
    private long A0;
    private long A1;
    private long A2;
    private long A3;
    private int BC;
    private long BC_MASK;
    private int ROUNDS;
    private int blockBits;
    private boolean forEncryption;
    private byte[] shifts0SC;
    private byte[] shifts1SC;
    private long[][] workingKey;

    static {
        logtable = new byte[]{0, 0, 25, 1, 50, 2, 26, -58, 75, -57, 27, 104, 51, -18, -33, 3, 100, 4, -32, 14, 52, -115, -127, -17, 76, 113, 8, -56, -8, 105, 28, -63, 125, -62, 29, -75, -7, -71, 39, 106, 77, -28, -90, 114, -102, -55, 9, 120, 101, 47, -118, 5, 33, 15, -31, 36, 18, -16, -126, 69, 53, -109, -38, -114, -106, -113, -37, -67, 54, -48, -50, -108, 19, 92, -46, -15, 64, 70, -125, 56, 102, -35, -3, 48, -65, 6, -117, 98, -77, 37, -30, -104, 34, -120, -111, 16, 126, 110, 72, -61, -93, -74, 30, 66, 58, 107, 40, 84, -6, -123, 61, -70, 43, 121, 10, 21, -101, -97, 94, -54, 78, -44, -84, -27, -13, 115, -89, 87, -81, 88, -88, 80, -12, -22, -42, 116, 79, -82, -23, -43, -25, -26, -83, -24, 44, -41, 117, 122, -21, 22, 11, -11, 89, -53, 95, -80, -100, -87, 81, -96, 127, 12, -10, 111, 23, -60, 73, -20, -40, 67, 31, 45, -92, 118, 123, -73, -52, -69, 62, 90, -5, 96, -79, -122, 59, 82, -95, 108, -86, 85, 41, -99, -105, -78, -121, -112, 97, -66, -36, -4, -68, -107, -49, -51, 55, 63, 91, -47, 83, 57, -124, 60, 65, -94, 109, 71, 20, 42, -98, 93, 86, -14, -45, -85, 68, 17, -110, -39, 35, 32, 46, -119, -76, 124, -72, 38, 119, -103, -29, -91, 103, 74, -19, -34, -59, 49, -2, 24, 13, 99, -116, -128, -64, -9, 112, 7};
        aLogtable = new byte[]{0, 3, 5, 15, 17, 51, 85, -1, 26, 46, 114, -106, -95, -8, 19, 53, 95, -31, 56, 72, -40, 115, -107, -92, -9, 2, 6, 10, 30, 34, 102, -86, -27, 52, 92, -28, 55, 89, -21, 38, 106, -66, -39, 112, -112, -85, -26, 49, 83, -11, 4, 12, 20, 60, 68, -52, 79, -47, 104, -72, -45, 110, -78, -51, 76, -44, 103, -87, -32, 59, 77, -41, 98, -90, -15, 8, 24, 40, 120, -120, -125, -98, -71, -48, 107, -67, -36, 127, -127, -104, -77, -50, 73, -37, 118, -102, -75, -60, 87, -7, 16, 48, 80, -16, 11, 29, 39, 105, -69, -42, 97, -93, -2, 25, 43, 125, -121, -110, -83, -20, 47, 113, -109, -82, -23, 32, 96, -96, -5, 22, 58, 78, -46, 109, -73, -62, 93, -25, 50, 86, -6, 21, 63, 65, -61, 94, -30, 61, 71, -55, 64, -64, 91, -19, 44, 116, -100, -65, -38, 117, -97, -70, -43, 100, -84, -17, 42, 126, -126, -99, -68, -33, 122, -114, -119, -128, -101, -74, -63, 88, -24, 35, 101, -81, -22, 37, 111, -79, -56, 67, -59, 84, -4, 31, 33, 99, -91, -12, 7, 9, 27, 45, 119, -103, -80, -53, 70, -54, 69, -49, 74, -34, 121, -117, -122, -111, -88, -29, 62, 66, -58, 81, -13, 14, 18, 54, 90, -18, 41, 123, -115, -116, -113, -118, -123, -108, -89, -14, 13, 23, 57, 75, -35, 124, -124, -105, -94, -3, 28, 36, 108, -76, -57, 82, -10, 1, 3, 5, 15, 17, 51, 85, -1, 26, 46, 114, -106, -95, -8, 19, 53, 95, -31, 56, 72, -40, 115, -107, -92, -9, 2, 6, 10, 30, 34, 102, -86, -27, 52, 92, -28, 55, 89, -21, 38, 106, -66, -39, 112, -112, -85, -26, 49, 83, -11, 4, 12, 20, 60, 68, -52, 79, -47, 104, -72, -45, 110, -78, -51, 76, -44, 103, -87, -32, 59, 77, -41, 98, -90, -15, 8, 24, 40, 120, -120, -125, -98, -71, -48, 107, -67, -36, 127, -127, -104, -77, -50, 73, -37, 118, -102, -75, -60, 87, -7, 16, 48, 80, -16, 11, 29, 39, 105, -69, -42, 97, -93, -2, 25, 43, 125, -121, -110, -83, -20, 47, 113, -109, -82, -23, 32, 96, -96, -5, 22, 58, 78, -46, 109, -73, -62, 93, -25, 50, 86, -6, 21, 63, 65, -61, 94, -30, 61, 71, -55, 64, -64, 91, -19, 44, 116, -100, -65, -38, 117, -97, -70, -43, 100, -84, -17, 42, 126, -126, -99, -68, -33, 122, -114, -119, -128, -101, -74, -63, 88, -24, 35, 101, -81, -22, 37, 111, -79, -56, 67, -59, 84, -4, 31, 33, 99, -91, -12, 7, 9, 27, 45, 119, -103, -80, -53, 70, -54, 69, -49, 74, -34, 121, -117, -122, -111, -88, -29, 62, 66, -58, 81, -13, 14, 18, 54, 90, -18, 41, 123, -115, -116, -113, -118, -123, -108, -89, -14, 13, 23, 57, 75, -35, 124, -124, -105, -94, -3, 28, 36, 108, -76, -57, 82, -10, 1};
        S = new byte[]{99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22};
        Si = new byte[]{82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5, 124, -29, 57, -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53, 84, 123, -108, 50, -90, -62, 35, 61, -18, 76, -107, 11, 66, -6, -61, 78, 8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37, 114, -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110, 108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115, -99, -124, -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5, -72, -77, 69, 6, -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, -36, -22, -105, -14, -49, -50, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 28, 117, -33, 110, 71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62, 75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12, 31, -35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 89, 39, -128, -20, 95, 96, 81, 127, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17, -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97, 23, 43, 4, 126, -70, 119, -42, 38, -31, 105, 20, 99, 85, 33, 12, 125};
        rcon = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212, 179, 125, 250, 239, 197, 145};
        shifts0 = new byte[][]{{0, 8, 16, 24}, {0, 8, 16, 24}, {0, 8, 16, 24}, {0, 8, 16, 32}, {0, 8, 24, 32}};
        shifts1 = new byte[][]{{0, 24, 16, 8}, {0, 32, 24, 16}, {0, 40, 32, 24}, {0, 48, 40, 24}, {0, 56, 40, 32}};
    }

    public RijndaelEngine() {
        this(128);
    }

    /*
     * Enabled aggressive block sorting
     */
    public RijndaelEngine(int n2) {
        switch (n2) {
            default: {
                throw new IllegalArgumentException("unknown blocksize to Rijndael");
            }
            case 128: {
                this.BC = 32;
                this.BC_MASK = 0xFFFFFFFFL;
                this.shifts0SC = shifts0[0];
                this.shifts1SC = shifts1[0];
                break;
            }
            case 160: {
                this.BC = 40;
                this.BC_MASK = 0xFFFFFFFFFFL;
                this.shifts0SC = shifts0[1];
                this.shifts1SC = shifts1[1];
                break;
            }
            case 192: {
                this.BC = 48;
                this.BC_MASK = 0xFFFFFFFFFFFFL;
                this.shifts0SC = shifts0[2];
                this.shifts1SC = shifts1[2];
                break;
            }
            case 224: {
                this.BC = 56;
                this.BC_MASK = 0xFFFFFFFFFFFFFFL;
                this.shifts0SC = shifts0[3];
                this.shifts1SC = shifts1[3];
                break;
            }
            case 256: {
                this.BC = 64;
                this.BC_MASK = -1L;
                this.shifts0SC = shifts0[4];
                this.shifts1SC = shifts1[4];
            }
        }
        this.blockBits = n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void InvMixColumn() {
        long l2 = 0L;
        int n2 = 0;
        long l3 = l2;
        long l4 = l2;
        long l5 = l2;
        do {
            if (n2 >= this.BC) {
                this.A0 = l5;
                this.A1 = l4;
                this.A2 = l3;
                this.A3 = l2;
                return;
            }
            int n3 = (int)(255L & this.A0 >> n2);
            int n4 = (int)(255L & this.A1 >> n2);
            int n5 = (int)(255L & this.A2 >> n2);
            int n6 = (int)(255L & this.A3 >> n2);
            int n7 = n3 != 0 ? 255 & logtable[n3 & 255] : -1;
            int n8 = n4 != 0 ? 255 & logtable[n4 & 255] : -1;
            int n9 = n5 != 0 ? 255 & logtable[n5 & 255] : -1;
            int n10 = n6 != 0 ? 255 & logtable[n6 & 255] : -1;
            l5 |= (long)(255 & (this.mul0xe(n7) ^ this.mul0xb(n8) ^ this.mul0xd(n9) ^ this.mul0x9(n10))) << n2;
            l4 |= (long)(255 & (this.mul0xe(n8) ^ this.mul0xb(n9) ^ this.mul0xd(n10) ^ this.mul0x9(n7))) << n2;
            l3 |= (long)(255 & (this.mul0xe(n9) ^ this.mul0xb(n10) ^ this.mul0xd(n7) ^ this.mul0x9(n8))) << n2;
            l2 |= (long)(255 & (this.mul0xe(n10) ^ this.mul0xb(n7) ^ this.mul0xd(n8) ^ this.mul0x9(n9))) << n2;
            n2 += 8;
        } while (true);
    }

    private void KeyAddition(long[] arrl) {
        this.A0 ^= arrl[0];
        this.A1 ^= arrl[1];
        this.A2 ^= arrl[2];
        this.A3 ^= arrl[3];
    }

    private void MixColumn() {
        long l2 = 0L;
        long l3 = l2;
        long l4 = l2;
        long l5 = l2;
        for (int i2 = 0; i2 < this.BC; i2 += 8) {
            int n2 = (int)(255L & this.A0 >> i2);
            int n3 = (int)(255L & this.A1 >> i2);
            int n4 = (int)(255L & this.A2 >> i2);
            int n5 = (int)(255L & this.A3 >> i2);
            l5 |= (long)(255 & (n5 ^ (n4 ^ (this.mul0x2(n2) ^ this.mul0x3(n3))))) << i2;
            l4 |= (long)(255 & (n2 ^ (n5 ^ (this.mul0x2(n3) ^ this.mul0x3(n4))))) << i2;
            l3 |= (long)(255 & (n3 ^ (n2 ^ (this.mul0x2(n4) ^ this.mul0x3(n5))))) << i2;
            l2 |= (long)(255 & (n4 ^ (n3 ^ (this.mul0x2(n5) ^ this.mul0x3(n2))))) << i2;
        }
        this.A0 = l5;
        this.A1 = l4;
        this.A2 = l3;
        this.A3 = l2;
    }

    private void ShiftRow(byte[] arrby) {
        this.A1 = this.shift(this.A1, arrby[1]);
        this.A2 = this.shift(this.A2, arrby[2]);
        this.A3 = this.shift(this.A3, arrby[3]);
    }

    private void Substitution(byte[] arrby) {
        this.A0 = this.applyS(this.A0, arrby);
        this.A1 = this.applyS(this.A1, arrby);
        this.A2 = this.applyS(this.A2, arrby);
        this.A3 = this.applyS(this.A3, arrby);
    }

    private long applyS(long l2, byte[] arrby) {
        long l3 = 0L;
        for (int i2 = 0; i2 < this.BC; i2 += 8) {
            l3 |= (long)(255 & arrby[(int)(255L & l2 >> i2)]) << i2;
        }
        return l3;
    }

    private void decryptBlock(long[][] arrl) {
        this.KeyAddition(arrl[this.ROUNDS]);
        this.Substitution(Si);
        this.ShiftRow(this.shifts1SC);
        for (int i2 = -1 + this.ROUNDS; i2 > 0; --i2) {
            this.KeyAddition(arrl[i2]);
            this.InvMixColumn();
            this.Substitution(Si);
            this.ShiftRow(this.shifts1SC);
        }
        this.KeyAddition(arrl[0]);
    }

    private void encryptBlock(long[][] arrl) {
        this.KeyAddition(arrl[0]);
        for (int i2 = 1; i2 < this.ROUNDS; ++i2) {
            this.Substitution(S);
            this.ShiftRow(this.shifts0SC);
            this.MixColumn();
            this.KeyAddition(arrl[i2]);
        }
        this.Substitution(S);
        this.ShiftRow(this.shifts0SC);
        this.KeyAddition(arrl[this.ROUNDS]);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private long[][] generateWorkingKey(byte[] var1_1) {
        var2_2 = 8 * var1_1.length;
        var3_3 = new int[]{4, 64};
        var4_4 = (byte[][])Array.newInstance((Class)Byte.TYPE, (int[])var3_3);
        var5_5 = new int[]{15, 4};
        var6_6 = (long[][])Array.newInstance((Class)Long.TYPE, (int[])var5_5);
        switch (var2_2) {
            default: {
                throw new IllegalArgumentException("Key length not 128/160/192/224/256 bits.");
            }
            case 128: {
                var7_7 = 4;
                break;
            }
            case 160: {
                var7_7 = 5;
                break;
            }
            case 192: {
                var7_7 = 6;
                break;
            }
            case 224: {
                var7_7 = 7;
                break;
            }
            case 256: {
                var7_7 = 8;
            }
        }
        this.ROUNDS = var2_2 >= this.blockBits ? var7_7 + 6 : 6 + this.BC / 8;
        var8_8 = 0;
        for (var9_9 = 0; var9_9 < var1_1.length; ++var9_9) {
            var39_10 = var4_4[var9_9 % 4];
            var40_11 = var9_9 / 4;
            var41_12 = var8_8 + 1;
            var39_10[var40_11] = var1_1[var8_8];
            var8_8 = var41_12;
        }
        var10_13 = 0;
        for (var11_14 = 0; var10_13 < var7_7 && var11_14 < (1 + this.ROUNDS) * (this.BC / 8); ++var11_14) {
            for (var36_15 = 0; var36_15 < 4; ++var36_15) {
                var38_17 = var6_6[var11_14 / (this.BC / 8)];
                var38_17[var36_15] = var38_17[var36_15] | (long)(255 & var4_4[var36_15][var10_13]) << var11_14 * 8 % this.BC;
            }
            var37_16 = var10_13 + 1;
            var10_13 = var37_16;
        }
        var12_18 = var11_14;
        var13_19 = 0;
        ** GOTO lbl46
        {
            var12_18 = var23_29;
            var13_19 = var18_24;
lbl46: // 2 sources:
            if (var12_18 >= (1 + this.ROUNDS) * (this.BC / 8)) return var6_6;
            for (var14_20 = 0; var14_20 < 4; ++var14_20) {
                var35_41 = var4_4[var14_20];
                var35_41[0] = (byte)(var35_41[0] ^ RijndaelEngine.S[255 & var4_4[(var14_20 + 1) % 4][var7_7 - 1]]);
            }
            var15_21 = var4_4[0];
            var16_22 = var15_21[0];
            var17_23 = RijndaelEngine.rcon;
            var18_24 = var13_19 + 1;
            var15_21[0] = (byte)(var16_22 ^ var17_23[var13_19]);
            if (var7_7 <= 6) {
                for (var32_38 = 1; var32_38 < var7_7; ++var32_38) {
                    for (var33_39 = 0; var33_39 < 4; ++var33_39) {
                        var34_40 = var4_4[var33_39];
                        var34_40[var32_38] = (byte)(var34_40[var32_38] ^ var4_4[var33_39][var32_38 - 1]);
                    }
                }
            } else {
                for (var19_25 = 1; var19_25 < 4; ++var19_25) {
                    for (var30_36 = 0; var30_36 < 4; ++var30_36) {
                        var31_37 = var4_4[var30_36];
                        var31_37[var19_25] = (byte)(var31_37[var19_25] ^ var4_4[var30_36][var19_25 - 1]);
                    }
                }
                for (var20_26 = 0; var20_26 < 4; ++var20_26) {
                    var29_35 = var4_4[var20_26];
                    var29_35[4] = (byte)(var29_35[4] ^ RijndaelEngine.S[255 & var4_4[var20_26][3]]);
                }
                for (var21_27 = 5; var21_27 < var7_7; ++var21_27) {
                    for (var27_33 = 0; var27_33 < 4; ++var27_33) {
                        var28_34 = var4_4[var27_33];
                        var28_34[var21_27] = (byte)(var28_34[var21_27] ^ var4_4[var27_33][var21_27 - 1]);
                    }
                }
            }
            var22_28 = 0;
            var23_29 = var12_18;
            do {
                if (var22_28 >= var7_7 || var23_29 >= (1 + this.ROUNDS) * (this.BC / 8)) continue block10;
                for (var24_30 = 0; var24_30 < 4; ++var24_30) {
                    var26_32 = var6_6[var23_29 / (this.BC / 8)];
                    var26_32[var24_30] = var26_32[var24_30] | (long)(255 & var4_4[var24_30][var22_28]) << var23_29 * 8 % this.BC;
                }
                var25_31 = var22_28 + 1;
                ++var23_29;
                var22_28 = var25_31;
            } while (true);
        }
    }

    private byte mul0x2(int n2) {
        if (n2 != 0) {
            return aLogtable[25 + (255 & logtable[n2])];
        }
        return 0;
    }

    private byte mul0x3(int n2) {
        if (n2 != 0) {
            return aLogtable[1 + (255 & logtable[n2])];
        }
        return 0;
    }

    private byte mul0x9(int n2) {
        if (n2 >= 0) {
            return aLogtable[n2 + 199];
        }
        return 0;
    }

    private byte mul0xb(int n2) {
        if (n2 >= 0) {
            return aLogtable[n2 + 104];
        }
        return 0;
    }

    private byte mul0xd(int n2) {
        if (n2 >= 0) {
            return aLogtable[n2 + 238];
        }
        return 0;
    }

    private byte mul0xe(int n2) {
        if (n2 >= 0) {
            return aLogtable[n2 + 223];
        }
        return 0;
    }

    private void packBlock(byte[] arrby, int n2) {
        for (int i2 = 0; i2 != this.BC; i2 += 8) {
            int n3 = n2 + 1;
            arrby[n2] = (byte)(this.A0 >> i2);
            int n4 = n3 + 1;
            arrby[n3] = (byte)(this.A1 >> i2);
            int n5 = n4 + 1;
            arrby[n4] = (byte)(this.A2 >> i2);
            n2 = n5 + 1;
            arrby[n5] = (byte)(this.A3 >> i2);
        }
    }

    private long shift(long l2, int n2) {
        return (l2 >>> n2 | l2 << this.BC - n2) & this.BC_MASK;
    }

    private void unpackBlock(byte[] arrby, int n2) {
        int n3 = n2 + 1;
        this.A0 = 255 & arrby[n2];
        int n4 = n3 + 1;
        this.A1 = 255 & arrby[n3];
        int n5 = n4 + 1;
        this.A2 = 255 & arrby[n4];
        int n6 = n5 + 1;
        this.A3 = 255 & arrby[n5];
        for (int i2 = 8; i2 != this.BC; i2 += 8) {
            long l2 = this.A0;
            int n7 = n6 + 1;
            this.A0 = l2 | (long)(255 & arrby[n6]) << i2;
            long l3 = this.A1;
            int n8 = n7 + 1;
            this.A1 = l3 | (long)(255 & arrby[n7]) << i2;
            long l4 = this.A2;
            int n9 = n8 + 1;
            this.A2 = l4 | (long)(255 & arrby[n8]) << i2;
            long l5 = this.A3;
            n6 = n9 + 1;
            this.A3 = l5 | (long)(255 & arrby[n9]) << i2;
        }
    }

    @Override
    public String getAlgorithmName() {
        return "Rijndael";
    }

    @Override
    public int getBlockSize() {
        return this.BC / 2;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.workingKey = this.generateWorkingKey(((KeyParameter)cipherParameters).getKey());
            this.forEncryption = bl;
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to Rijndael init - " + cipherParameters.getClass().getName());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.workingKey == null) {
            throw new IllegalStateException("Rijndael engine not initialised");
        }
        if (n2 + this.BC / 2 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + this.BC / 2 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.forEncryption) {
            this.unpackBlock(arrby, n2);
            this.encryptBlock(this.workingKey);
            this.packBlock(arrby2, n3);
            do {
                return this.BC / 2;
                break;
            } while (true);
        }
        this.unpackBlock(arrby, n2);
        this.decryptBlock(this.workingKey);
        this.packBlock(arrby2, n3);
        return this.BC / 2;
    }

    @Override
    public void reset() {
    }
}

