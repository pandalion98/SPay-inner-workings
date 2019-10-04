/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
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

public class AESLightEngine
implements BlockCipher {
    private static final int BLOCK_SIZE = 16;
    private static final byte[] S = new byte[]{99, 124, 119, 123, -14, 107, 111, -59, 48, 1, 103, 43, -2, -41, -85, 118, -54, -126, -55, 125, -6, 89, 71, -16, -83, -44, -94, -81, -100, -92, 114, -64, -73, -3, -109, 38, 54, 63, -9, -52, 52, -91, -27, -15, 113, -40, 49, 21, 4, -57, 35, -61, 24, -106, 5, -102, 7, 18, -128, -30, -21, 39, -78, 117, 9, -125, 44, 26, 27, 110, 90, -96, 82, 59, -42, -77, 41, -29, 47, -124, 83, -47, 0, -19, 32, -4, -79, 91, 106, -53, -66, 57, 74, 76, 88, -49, -48, -17, -86, -5, 67, 77, 51, -123, 69, -7, 2, 127, 80, 60, -97, -88, 81, -93, 64, -113, -110, -99, 56, -11, -68, -74, -38, 33, 16, -1, -13, -46, -51, 12, 19, -20, 95, -105, 68, 23, -60, -89, 126, 61, 100, 93, 25, 115, 96, -127, 79, -36, 34, 42, -112, -120, 70, -18, -72, 20, -34, 94, 11, -37, -32, 50, 58, 10, 73, 6, 36, 92, -62, -45, -84, 98, -111, -107, -28, 121, -25, -56, 55, 109, -115, -43, 78, -87, 108, 86, -12, -22, 101, 122, -82, 8, -70, 120, 37, 46, 28, -90, -76, -58, -24, -35, 116, 31, 75, -67, -117, -118, 112, 62, -75, 102, 72, 3, -10, 14, 97, 53, 87, -71, -122, -63, 29, -98, -31, -8, -104, 17, 105, -39, -114, -108, -101, 30, -121, -23, -50, 85, 40, -33, -116, -95, -119, 13, -65, -26, 66, 104, 65, -103, 45, 15, -80, 84, -69, 22};
    private static final byte[] Si = new byte[]{82, 9, 106, -43, 48, 54, -91, 56, -65, 64, -93, -98, -127, -13, -41, -5, 124, -29, 57, -126, -101, 47, -1, -121, 52, -114, 67, 68, -60, -34, -23, -53, 84, 123, -108, 50, -90, -62, 35, 61, -18, 76, -107, 11, 66, -6, -61, 78, 8, 46, -95, 102, 40, -39, 36, -78, 118, 91, -94, 73, 109, -117, -47, 37, 114, -8, -10, 100, -122, 104, -104, 22, -44, -92, 92, -52, 93, 101, -74, -110, 108, 112, 72, 80, -3, -19, -71, -38, 94, 21, 70, 87, -89, -115, -99, -124, -112, -40, -85, 0, -116, -68, -45, 10, -9, -28, 88, 5, -72, -77, 69, 6, -48, 44, 30, -113, -54, 63, 15, 2, -63, -81, -67, 3, 1, 19, -118, 107, 58, -111, 17, 65, 79, 103, -36, -22, -105, -14, -49, -50, -16, -76, -26, 115, -106, -84, 116, 34, -25, -83, 53, -123, -30, -7, 55, -24, 28, 117, -33, 110, 71, -15, 26, 113, 29, 41, -59, -119, 111, -73, 98, 14, -86, 24, -66, 27, -4, 86, 62, 75, -58, -46, 121, 32, -102, -37, -64, -2, 120, -51, 90, -12, 31, -35, -88, 51, -120, 7, -57, 49, -79, 18, 16, 89, 39, -128, -20, 95, 96, 81, 127, -87, 25, -75, 74, 13, 45, -27, 122, -97, -109, -55, -100, -17, -96, -32, 59, 77, -82, 42, -11, -80, -56, -21, -69, 60, -125, 83, -103, 97, 23, 43, 4, 126, -70, 119, -42, 38, -31, 105, 20, 99, 85, 33, 12, 125};
    private static final int m1 = -2139062144;
    private static final int m2 = 2139062143;
    private static final int m3 = 27;
    private static final int[] rcon = new int[]{1, 2, 4, 8, 16, 32, 64, 128, 27, 54, 108, 216, 171, 77, 154, 47, 94, 188, 99, 198, 151, 53, 106, 212, 179, 125, 250, 239, 197, 145};
    private int C0;
    private int C1;
    private int C2;
    private int C3;
    private int ROUNDS;
    private int[][] WorkingKey = null;
    private boolean forEncryption;

    private static int FFmulX(int n2) {
        return (2139062143 & n2) << 1 ^ 27 * ((-2139062144 & n2) >>> 7);
    }

    private void decryptBlock(int[][] arrn) {
        int n2 = this.C0 ^ arrn[this.ROUNDS][0];
        int n3 = this.C1 ^ arrn[this.ROUNDS][1];
        int n4 = this.C2 ^ arrn[this.ROUNDS][2];
        int n5 = -1 + this.ROUNDS;
        int n6 = this.C3 ^ arrn[this.ROUNDS][3];
        while (n5 > 1) {
            int n7 = AESLightEngine.inv_mcol(255 & Si[n2 & 255] ^ (255 & Si[255 & n6 >> 8]) << 8 ^ (255 & Si[255 & n4 >> 16]) << 16 ^ Si[255 & n3 >> 24] << 24) ^ arrn[n5][0];
            int n8 = AESLightEngine.inv_mcol(255 & Si[n3 & 255] ^ (255 & Si[255 & n2 >> 8]) << 8 ^ (255 & Si[255 & n6 >> 16]) << 16 ^ Si[255 & n4 >> 24] << 24) ^ arrn[n5][1];
            int n9 = AESLightEngine.inv_mcol(255 & Si[n4 & 255] ^ (255 & Si[255 & n3 >> 8]) << 8 ^ (255 & Si[255 & n2 >> 16]) << 16 ^ Si[255 & n6 >> 24] << 24) ^ arrn[n5][2];
            int n10 = AESLightEngine.inv_mcol(255 & Si[n6 & 255] ^ (255 & Si[255 & n4 >> 8]) << 8 ^ (255 & Si[255 & n3 >> 16]) << 16 ^ Si[255 & n2 >> 24] << 24);
            int n11 = n5 - 1;
            int n12 = n10 ^ arrn[n5][3];
            n2 = AESLightEngine.inv_mcol(255 & Si[n7 & 255] ^ (255 & Si[255 & n12 >> 8]) << 8 ^ (255 & Si[255 & n9 >> 16]) << 16 ^ Si[255 & n8 >> 24] << 24) ^ arrn[n11][0];
            n3 = AESLightEngine.inv_mcol(255 & Si[n8 & 255] ^ (255 & Si[255 & n7 >> 8]) << 8 ^ (255 & Si[255 & n12 >> 16]) << 16 ^ Si[255 & n9 >> 24] << 24) ^ arrn[n11][1];
            n4 = AESLightEngine.inv_mcol(255 & Si[n9 & 255] ^ (255 & Si[255 & n8 >> 8]) << 8 ^ (255 & Si[255 & n7 >> 16]) << 16 ^ Si[255 & n12 >> 24] << 24) ^ arrn[n11][2];
            int n13 = AESLightEngine.inv_mcol(255 & Si[n12 & 255] ^ (255 & Si[255 & n9 >> 8]) << 8 ^ (255 & Si[255 & n8 >> 16]) << 16 ^ Si[255 & n7 >> 24] << 24);
            n5 = n11 - 1;
            n6 = n13 ^ arrn[n11][3];
        }
        int n14 = AESLightEngine.inv_mcol(255 & Si[n2 & 255] ^ (255 & Si[255 & n6 >> 8]) << 8 ^ (255 & Si[255 & n4 >> 16]) << 16 ^ Si[255 & n3 >> 24] << 24) ^ arrn[n5][0];
        int n15 = AESLightEngine.inv_mcol(255 & Si[n3 & 255] ^ (255 & Si[255 & n2 >> 8]) << 8 ^ (255 & Si[255 & n6 >> 16]) << 16 ^ Si[255 & n4 >> 24] << 24) ^ arrn[n5][1];
        int n16 = AESLightEngine.inv_mcol(255 & Si[n4 & 255] ^ (255 & Si[255 & n3 >> 8]) << 8 ^ (255 & Si[255 & n2 >> 16]) << 16 ^ Si[255 & n6 >> 24] << 24) ^ arrn[n5][2];
        int n17 = AESLightEngine.inv_mcol(255 & Si[n6 & 255] ^ (255 & Si[255 & n4 >> 8]) << 8 ^ (255 & Si[255 & n3 >> 16]) << 16 ^ Si[255 & n2 >> 24] << 24) ^ arrn[n5][3];
        this.C0 = 255 & Si[n14 & 255] ^ (255 & Si[255 & n17 >> 8]) << 8 ^ (255 & Si[255 & n16 >> 16]) << 16 ^ Si[255 & n15 >> 24] << 24 ^ arrn[0][0];
        this.C1 = 255 & Si[n15 & 255] ^ (255 & Si[255 & n14 >> 8]) << 8 ^ (255 & Si[255 & n17 >> 16]) << 16 ^ Si[255 & n16 >> 24] << 24 ^ arrn[0][1];
        this.C2 = 255 & Si[n16 & 255] ^ (255 & Si[255 & n15 >> 8]) << 8 ^ (255 & Si[255 & n14 >> 16]) << 16 ^ Si[255 & n17 >> 24] << 24 ^ arrn[0][2];
        this.C3 = 255 & Si[n17 & 255] ^ (255 & Si[255 & n16 >> 8]) << 8 ^ (255 & Si[255 & n15 >> 16]) << 16 ^ Si[255 & n14 >> 24] << 24 ^ arrn[0][3];
    }

    private void encryptBlock(int[][] arrn) {
        int n2 = this.C0 ^ arrn[0][0];
        int n3 = this.C1 ^ arrn[0][1];
        int n4 = this.C2 ^ arrn[0][2];
        int n5 = this.C3 ^ arrn[0][3];
        int n6 = n2;
        int n7 = n3;
        int n8 = n4;
        int n9 = 1;
        while (n9 < -1 + this.ROUNDS) {
            int n10 = AESLightEngine.mcol(255 & S[n6 & 255] ^ (255 & S[255 & n7 >> 8]) << 8 ^ (255 & S[255 & n8 >> 16]) << 16 ^ S[255 & n5 >> 24] << 24) ^ arrn[n9][0];
            int n11 = AESLightEngine.mcol(255 & S[n7 & 255] ^ (255 & S[255 & n8 >> 8]) << 8 ^ (255 & S[255 & n5 >> 16]) << 16 ^ S[255 & n6 >> 24] << 24) ^ arrn[n9][1];
            int n12 = AESLightEngine.mcol(255 & S[n8 & 255] ^ (255 & S[255 & n5 >> 8]) << 8 ^ (255 & S[255 & n6 >> 16]) << 16 ^ S[255 & n7 >> 24] << 24) ^ arrn[n9][2];
            int n13 = AESLightEngine.mcol(255 & S[n5 & 255] ^ (255 & S[255 & n6 >> 8]) << 8 ^ (255 & S[255 & n7 >> 16]) << 16 ^ S[255 & n8 >> 24] << 24);
            int n14 = n9 + 1;
            int n15 = n13 ^ arrn[n9][3];
            n6 = AESLightEngine.mcol(255 & S[n10 & 255] ^ (255 & S[255 & n11 >> 8]) << 8 ^ (255 & S[255 & n12 >> 16]) << 16 ^ S[255 & n15 >> 24] << 24) ^ arrn[n14][0];
            n7 = AESLightEngine.mcol(255 & S[n11 & 255] ^ (255 & S[255 & n12 >> 8]) << 8 ^ (255 & S[255 & n15 >> 16]) << 16 ^ S[255 & n10 >> 24] << 24) ^ arrn[n14][1];
            n8 = AESLightEngine.mcol(255 & S[n12 & 255] ^ (255 & S[255 & n15 >> 8]) << 8 ^ (255 & S[255 & n10 >> 16]) << 16 ^ S[255 & n11 >> 24] << 24) ^ arrn[n14][2];
            int n16 = AESLightEngine.mcol(255 & S[n15 & 255] ^ (255 & S[255 & n10 >> 8]) << 8 ^ (255 & S[255 & n11 >> 16]) << 16 ^ S[255 & n12 >> 24] << 24);
            n9 = n14 + 1;
            n5 = n16 ^ arrn[n14][3];
        }
        int n17 = AESLightEngine.mcol(255 & S[n6 & 255] ^ (255 & S[255 & n7 >> 8]) << 8 ^ (255 & S[255 & n8 >> 16]) << 16 ^ S[255 & n5 >> 24] << 24) ^ arrn[n9][0];
        int n18 = AESLightEngine.mcol(255 & S[n7 & 255] ^ (255 & S[255 & n8 >> 8]) << 8 ^ (255 & S[255 & n5 >> 16]) << 16 ^ S[255 & n6 >> 24] << 24) ^ arrn[n9][1];
        int n19 = AESLightEngine.mcol(255 & S[n8 & 255] ^ (255 & S[255 & n5 >> 8]) << 8 ^ (255 & S[255 & n6 >> 16]) << 16 ^ S[255 & n7 >> 24] << 24) ^ arrn[n9][2];
        int n20 = AESLightEngine.mcol(255 & S[n5 & 255] ^ (255 & S[255 & n6 >> 8]) << 8 ^ (255 & S[255 & n7 >> 16]) << 16 ^ S[255 & n8 >> 24] << 24);
        int n21 = n9 + 1;
        int n22 = n20 ^ arrn[n9][3];
        this.C0 = 255 & S[n17 & 255] ^ (255 & S[255 & n18 >> 8]) << 8 ^ (255 & S[255 & n19 >> 16]) << 16 ^ S[255 & n22 >> 24] << 24 ^ arrn[n21][0];
        this.C1 = 255 & S[n18 & 255] ^ (255 & S[255 & n19 >> 8]) << 8 ^ (255 & S[255 & n22 >> 16]) << 16 ^ S[255 & n17 >> 24] << 24 ^ arrn[n21][1];
        this.C2 = 255 & S[n19 & 255] ^ (255 & S[255 & n22 >> 8]) << 8 ^ (255 & S[255 & n17 >> 16]) << 16 ^ S[255 & n18 >> 24] << 24 ^ arrn[n21][2];
        this.C3 = 255 & S[n22 & 255] ^ (255 & S[255 & n17 >> 8]) << 8 ^ (255 & S[255 & n18 >> 16]) << 16 ^ S[255 & n19 >> 24] << 24 ^ arrn[n21][3];
    }

    /*
     * Enabled aggressive block sorting
     */
    private int[][] generateWorkingKey(byte[] arrby, boolean bl) {
        int n2 = arrby.length / 4;
        if (n2 != 4 && n2 != 6 && n2 != 8 || n2 * 4 != arrby.length) {
            throw new IllegalArgumentException("Key length not 128/192/256 bits.");
        }
        this.ROUNDS = n2 + 6;
        int[] arrn = new int[]{1 + this.ROUNDS, 4};
        int[][] arrn2 = (int[][])Array.newInstance((Class)Integer.TYPE, (int[])arrn);
        int n3 = 0;
        for (int i2 = 0; i2 < arrby.length; i2 += 4, ++n3) {
            arrn2[n3 >> 2][n3 & 3] = 255 & arrby[i2] | (255 & arrby[i2 + 1]) << 8 | (255 & arrby[i2 + 2]) << 16 | arrby[i2 + 3] << 24;
        }
        int n4 = 1 + this.ROUNDS << 2;
        for (int i3 = n2; i3 < n4; ++i3) {
            int n5 = arrn2[i3 - 1 >> 2][3 & i3 - 1];
            if (i3 % n2 == 0) {
                n5 = AESLightEngine.subWord(AESLightEngine.shift(n5, 8)) ^ rcon[-1 + i3 / n2];
            } else if (n2 > 6 && i3 % n2 == 4) {
                n5 = AESLightEngine.subWord(n5);
            }
            arrn2[i3 >> 2][i3 & 3] = n5 ^ arrn2[i3 - n2 >> 2][3 & i3 - n2];
        }
        if (!bl) {
            for (int i4 = 1; i4 < this.ROUNDS; ++i4) {
                for (int i5 = 0; i5 < 4; ++i5) {
                    arrn2[i4][i5] = AESLightEngine.inv_mcol(arrn2[i4][i5]);
                }
            }
        }
        return arrn2;
    }

    private static int inv_mcol(int n2) {
        int n3 = AESLightEngine.FFmulX(n2);
        int n4 = AESLightEngine.FFmulX(n3);
        int n5 = AESLightEngine.FFmulX(n4);
        int n6 = n2 ^ n5;
        return n5 ^ (n3 ^ n4) ^ AESLightEngine.shift(n3 ^ n6, 8) ^ AESLightEngine.shift(n4 ^ n6, 16) ^ AESLightEngine.shift(n6, 24);
    }

    private static int mcol(int n2) {
        int n3 = AESLightEngine.FFmulX(n2);
        return n3 ^ AESLightEngine.shift(n2 ^ n3, 8) ^ AESLightEngine.shift(n2, 16) ^ AESLightEngine.shift(n2, 24);
    }

    private void packBlock(byte[] arrby, int n2) {
        int n3 = n2 + 1;
        arrby[n2] = (byte)this.C0;
        int n4 = n3 + 1;
        arrby[n3] = (byte)(this.C0 >> 8);
        int n5 = n4 + 1;
        arrby[n4] = (byte)(this.C0 >> 16);
        int n6 = n5 + 1;
        arrby[n5] = (byte)(this.C0 >> 24);
        int n7 = n6 + 1;
        arrby[n6] = (byte)this.C1;
        int n8 = n7 + 1;
        arrby[n7] = (byte)(this.C1 >> 8);
        int n9 = n8 + 1;
        arrby[n8] = (byte)(this.C1 >> 16);
        int n10 = n9 + 1;
        arrby[n9] = (byte)(this.C1 >> 24);
        int n11 = n10 + 1;
        arrby[n10] = (byte)this.C2;
        int n12 = n11 + 1;
        arrby[n11] = (byte)(this.C2 >> 8);
        int n13 = n12 + 1;
        arrby[n12] = (byte)(this.C2 >> 16);
        int n14 = n13 + 1;
        arrby[n13] = (byte)(this.C2 >> 24);
        int n15 = n14 + 1;
        arrby[n14] = (byte)this.C3;
        int n16 = n15 + 1;
        arrby[n15] = (byte)(this.C3 >> 8);
        int n17 = n16 + 1;
        arrby[n16] = (byte)(this.C3 >> 16);
        n17 + 1;
        arrby[n17] = (byte)(this.C3 >> 24);
    }

    private static int shift(int n2, int n3) {
        return n2 >>> n3 | n2 << -n3;
    }

    private static int subWord(int n2) {
        return 255 & S[n2 & 255] | (255 & S[255 & n2 >> 8]) << 8 | (255 & S[255 & n2 >> 16]) << 16 | S[255 & n2 >> 24] << 24;
    }

    private void unpackBlock(byte[] arrby, int n2) {
        int n3 = n2 + 1;
        int n4 = this.C0 = 255 & arrby[n2];
        int n5 = n3 + 1;
        int n6 = this.C0 = n4 | (255 & arrby[n3]) << 8;
        int n7 = n5 + 1;
        int n8 = this.C0 = n6 | (255 & arrby[n5]) << 16;
        int n9 = n7 + 1;
        this.C0 = n8 | arrby[n7] << 24;
        int n10 = n9 + 1;
        int n11 = this.C1 = 255 & arrby[n9];
        int n12 = n10 + 1;
        int n13 = this.C1 = n11 | (255 & arrby[n10]) << 8;
        int n14 = n12 + 1;
        int n15 = this.C1 = n13 | (255 & arrby[n12]) << 16;
        int n16 = n14 + 1;
        this.C1 = n15 | arrby[n14] << 24;
        int n17 = n16 + 1;
        int n18 = this.C2 = 255 & arrby[n16];
        int n19 = n17 + 1;
        int n20 = this.C2 = n18 | (255 & arrby[n17]) << 8;
        int n21 = n19 + 1;
        int n22 = this.C2 = n20 | (255 & arrby[n19]) << 16;
        int n23 = n21 + 1;
        this.C2 = n22 | arrby[n21] << 24;
        int n24 = n23 + 1;
        int n25 = this.C3 = 255 & arrby[n23];
        int n26 = n24 + 1;
        int n27 = this.C3 = n25 | (255 & arrby[n24]) << 8;
        int n28 = n26 + 1;
        int n29 = this.C3 = n27 | (255 & arrby[n26]) << 16;
        n28 + 1;
        this.C3 = n29 | arrby[n28] << 24;
    }

    @Override
    public String getAlgorithmName() {
        return "AES";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.WorkingKey = this.generateWorkingKey(((KeyParameter)cipherParameters).getKey(), bl);
            this.forEncryption = bl;
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to AES init - " + cipherParameters.getClass().getName());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.WorkingKey == null) {
            throw new IllegalStateException("AES engine not initialised");
        }
        if (n2 + 16 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 16 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        if (this.forEncryption) {
            this.unpackBlock(arrby, n2);
            this.encryptBlock(this.WorkingKey);
            this.packBlock(arrby2, n3);
            do {
                return 16;
                break;
            } while (true);
        }
        this.unpackBlock(arrby, n2);
        this.decryptBlock(this.WorkingKey);
        this.packBlock(arrby2, n3);
        return 16;
    }

    @Override
    public void reset() {
    }
}

