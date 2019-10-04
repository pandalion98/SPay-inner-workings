/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.params.KeyParameter;

public class SerpentEngine
implements BlockCipher {
    private static final int BLOCK_SIZE = 16;
    static final int PHI = -1640531527;
    static final int ROUNDS = 32;
    private int X0;
    private int X1;
    private int X2;
    private int X3;
    private boolean encrypting;
    private int[] wKey;

    private void LT() {
        int n2 = this.rotateLeft(this.X0, 13);
        int n3 = this.rotateLeft(this.X2, 3);
        int n4 = n3 ^ (n2 ^ this.X1);
        int n5 = n3 ^ this.X3 ^ n2 << 3;
        this.X1 = this.rotateLeft(n4, 1);
        this.X3 = this.rotateLeft(n5, 7);
        this.X0 = this.rotateLeft(n2 ^ this.X1 ^ this.X3, 5);
        this.X2 = this.rotateLeft(n3 ^ this.X3 ^ this.X1 << 7, 22);
    }

    private int bytesToWord(byte[] arrby, int n2) {
        return (255 & arrby[n2]) << 24 | (255 & arrby[n2 + 1]) << 16 | (255 & arrby[n2 + 2]) << 8 | 255 & arrby[n2 + 3];
    }

    private void decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        this.X3 = this.wKey[131] ^ this.bytesToWord(arrby, n2);
        this.X2 = this.wKey[130] ^ this.bytesToWord(arrby, n2 + 4);
        this.X1 = this.wKey[129] ^ this.bytesToWord(arrby, n2 + 8);
        this.X0 = this.wKey[128] ^ this.bytesToWord(arrby, n2 + 12);
        this.ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[124];
        this.X1 ^= this.wKey[125];
        this.X2 ^= this.wKey[126];
        this.X3 ^= this.wKey[127];
        this.inverseLT();
        this.ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[120];
        this.X1 ^= this.wKey[121];
        this.X2 ^= this.wKey[122];
        this.X3 ^= this.wKey[123];
        this.inverseLT();
        this.ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[116];
        this.X1 ^= this.wKey[117];
        this.X2 ^= this.wKey[118];
        this.X3 ^= this.wKey[119];
        this.inverseLT();
        this.ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[112];
        this.X1 ^= this.wKey[113];
        this.X2 ^= this.wKey[114];
        this.X3 ^= this.wKey[115];
        this.inverseLT();
        this.ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[108];
        this.X1 ^= this.wKey[109];
        this.X2 ^= this.wKey[110];
        this.X3 ^= this.wKey[111];
        this.inverseLT();
        this.ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[104];
        this.X1 ^= this.wKey[105];
        this.X2 ^= this.wKey[106];
        this.X3 ^= this.wKey[107];
        this.inverseLT();
        this.ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[100];
        this.X1 ^= this.wKey[101];
        this.X2 ^= this.wKey[102];
        this.X3 ^= this.wKey[103];
        this.inverseLT();
        this.ib0(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[96];
        this.X1 ^= this.wKey[97];
        this.X2 ^= this.wKey[98];
        this.X3 ^= this.wKey[99];
        this.inverseLT();
        this.ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[92];
        this.X1 ^= this.wKey[93];
        this.X2 ^= this.wKey[94];
        this.X3 ^= this.wKey[95];
        this.inverseLT();
        this.ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[88];
        this.X1 ^= this.wKey[89];
        this.X2 ^= this.wKey[90];
        this.X3 ^= this.wKey[91];
        this.inverseLT();
        this.ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[84];
        this.X1 ^= this.wKey[85];
        this.X2 ^= this.wKey[86];
        this.X3 ^= this.wKey[87];
        this.inverseLT();
        this.ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[80];
        this.X1 ^= this.wKey[81];
        this.X2 ^= this.wKey[82];
        this.X3 ^= this.wKey[83];
        this.inverseLT();
        this.ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[76];
        this.X1 ^= this.wKey[77];
        this.X2 ^= this.wKey[78];
        this.X3 ^= this.wKey[79];
        this.inverseLT();
        this.ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[72];
        this.X1 ^= this.wKey[73];
        this.X2 ^= this.wKey[74];
        this.X3 ^= this.wKey[75];
        this.inverseLT();
        this.ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[68];
        this.X1 ^= this.wKey[69];
        this.X2 ^= this.wKey[70];
        this.X3 ^= this.wKey[71];
        this.inverseLT();
        this.ib0(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[64];
        this.X1 ^= this.wKey[65];
        this.X2 ^= this.wKey[66];
        this.X3 ^= this.wKey[67];
        this.inverseLT();
        this.ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[60];
        this.X1 ^= this.wKey[61];
        this.X2 ^= this.wKey[62];
        this.X3 ^= this.wKey[63];
        this.inverseLT();
        this.ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[56];
        this.X1 ^= this.wKey[57];
        this.X2 ^= this.wKey[58];
        this.X3 ^= this.wKey[59];
        this.inverseLT();
        this.ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[52];
        this.X1 ^= this.wKey[53];
        this.X2 ^= this.wKey[54];
        this.X3 ^= this.wKey[55];
        this.inverseLT();
        this.ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[48];
        this.X1 ^= this.wKey[49];
        this.X2 ^= this.wKey[50];
        this.X3 ^= this.wKey[51];
        this.inverseLT();
        this.ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[44];
        this.X1 ^= this.wKey[45];
        this.X2 ^= this.wKey[46];
        this.X3 ^= this.wKey[47];
        this.inverseLT();
        this.ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[40];
        this.X1 ^= this.wKey[41];
        this.X2 ^= this.wKey[42];
        this.X3 ^= this.wKey[43];
        this.inverseLT();
        this.ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[36];
        this.X1 ^= this.wKey[37];
        this.X2 ^= this.wKey[38];
        this.X3 ^= this.wKey[39];
        this.inverseLT();
        this.ib0(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[32];
        this.X1 ^= this.wKey[33];
        this.X2 ^= this.wKey[34];
        this.X3 ^= this.wKey[35];
        this.inverseLT();
        this.ib7(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[28];
        this.X1 ^= this.wKey[29];
        this.X2 ^= this.wKey[30];
        this.X3 ^= this.wKey[31];
        this.inverseLT();
        this.ib6(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[24];
        this.X1 ^= this.wKey[25];
        this.X2 ^= this.wKey[26];
        this.X3 ^= this.wKey[27];
        this.inverseLT();
        this.ib5(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[20];
        this.X1 ^= this.wKey[21];
        this.X2 ^= this.wKey[22];
        this.X3 ^= this.wKey[23];
        this.inverseLT();
        this.ib4(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[16];
        this.X1 ^= this.wKey[17];
        this.X2 ^= this.wKey[18];
        this.X3 ^= this.wKey[19];
        this.inverseLT();
        this.ib3(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[12];
        this.X1 ^= this.wKey[13];
        this.X2 ^= this.wKey[14];
        this.X3 ^= this.wKey[15];
        this.inverseLT();
        this.ib2(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[8];
        this.X1 ^= this.wKey[9];
        this.X2 ^= this.wKey[10];
        this.X3 ^= this.wKey[11];
        this.inverseLT();
        this.ib1(this.X0, this.X1, this.X2, this.X3);
        this.X0 ^= this.wKey[4];
        this.X1 ^= this.wKey[5];
        this.X2 ^= this.wKey[6];
        this.X3 ^= this.wKey[7];
        this.inverseLT();
        this.ib0(this.X0, this.X1, this.X2, this.X3);
        this.wordToBytes(this.X3 ^ this.wKey[3], arrby2, n3);
        this.wordToBytes(this.X2 ^ this.wKey[2], arrby2, n3 + 4);
        this.wordToBytes(this.X1 ^ this.wKey[1], arrby2, n3 + 8);
        this.wordToBytes(this.X0 ^ this.wKey[0], arrby2, n3 + 12);
    }

    private void encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        this.X3 = this.bytesToWord(arrby, n2);
        this.X2 = this.bytesToWord(arrby, n2 + 4);
        this.X1 = this.bytesToWord(arrby, n2 + 8);
        this.X0 = this.bytesToWord(arrby, n2 + 12);
        this.sb0(this.wKey[0] ^ this.X0, this.wKey[1] ^ this.X1, this.wKey[2] ^ this.X2, this.wKey[3] ^ this.X3);
        this.LT();
        this.sb1(this.wKey[4] ^ this.X0, this.wKey[5] ^ this.X1, this.wKey[6] ^ this.X2, this.wKey[7] ^ this.X3);
        this.LT();
        this.sb2(this.wKey[8] ^ this.X0, this.wKey[9] ^ this.X1, this.wKey[10] ^ this.X2, this.wKey[11] ^ this.X3);
        this.LT();
        this.sb3(this.wKey[12] ^ this.X0, this.wKey[13] ^ this.X1, this.wKey[14] ^ this.X2, this.wKey[15] ^ this.X3);
        this.LT();
        this.sb4(this.wKey[16] ^ this.X0, this.wKey[17] ^ this.X1, this.wKey[18] ^ this.X2, this.wKey[19] ^ this.X3);
        this.LT();
        this.sb5(this.wKey[20] ^ this.X0, this.wKey[21] ^ this.X1, this.wKey[22] ^ this.X2, this.wKey[23] ^ this.X3);
        this.LT();
        this.sb6(this.wKey[24] ^ this.X0, this.wKey[25] ^ this.X1, this.wKey[26] ^ this.X2, this.wKey[27] ^ this.X3);
        this.LT();
        this.sb7(this.wKey[28] ^ this.X0, this.wKey[29] ^ this.X1, this.wKey[30] ^ this.X2, this.wKey[31] ^ this.X3);
        this.LT();
        this.sb0(this.wKey[32] ^ this.X0, this.wKey[33] ^ this.X1, this.wKey[34] ^ this.X2, this.wKey[35] ^ this.X3);
        this.LT();
        this.sb1(this.wKey[36] ^ this.X0, this.wKey[37] ^ this.X1, this.wKey[38] ^ this.X2, this.wKey[39] ^ this.X3);
        this.LT();
        this.sb2(this.wKey[40] ^ this.X0, this.wKey[41] ^ this.X1, this.wKey[42] ^ this.X2, this.wKey[43] ^ this.X3);
        this.LT();
        this.sb3(this.wKey[44] ^ this.X0, this.wKey[45] ^ this.X1, this.wKey[46] ^ this.X2, this.wKey[47] ^ this.X3);
        this.LT();
        this.sb4(this.wKey[48] ^ this.X0, this.wKey[49] ^ this.X1, this.wKey[50] ^ this.X2, this.wKey[51] ^ this.X3);
        this.LT();
        this.sb5(this.wKey[52] ^ this.X0, this.wKey[53] ^ this.X1, this.wKey[54] ^ this.X2, this.wKey[55] ^ this.X3);
        this.LT();
        this.sb6(this.wKey[56] ^ this.X0, this.wKey[57] ^ this.X1, this.wKey[58] ^ this.X2, this.wKey[59] ^ this.X3);
        this.LT();
        this.sb7(this.wKey[60] ^ this.X0, this.wKey[61] ^ this.X1, this.wKey[62] ^ this.X2, this.wKey[63] ^ this.X3);
        this.LT();
        this.sb0(this.wKey[64] ^ this.X0, this.wKey[65] ^ this.X1, this.wKey[66] ^ this.X2, this.wKey[67] ^ this.X3);
        this.LT();
        this.sb1(this.wKey[68] ^ this.X0, this.wKey[69] ^ this.X1, this.wKey[70] ^ this.X2, this.wKey[71] ^ this.X3);
        this.LT();
        this.sb2(this.wKey[72] ^ this.X0, this.wKey[73] ^ this.X1, this.wKey[74] ^ this.X2, this.wKey[75] ^ this.X3);
        this.LT();
        this.sb3(this.wKey[76] ^ this.X0, this.wKey[77] ^ this.X1, this.wKey[78] ^ this.X2, this.wKey[79] ^ this.X3);
        this.LT();
        this.sb4(this.wKey[80] ^ this.X0, this.wKey[81] ^ this.X1, this.wKey[82] ^ this.X2, this.wKey[83] ^ this.X3);
        this.LT();
        this.sb5(this.wKey[84] ^ this.X0, this.wKey[85] ^ this.X1, this.wKey[86] ^ this.X2, this.wKey[87] ^ this.X3);
        this.LT();
        this.sb6(this.wKey[88] ^ this.X0, this.wKey[89] ^ this.X1, this.wKey[90] ^ this.X2, this.wKey[91] ^ this.X3);
        this.LT();
        this.sb7(this.wKey[92] ^ this.X0, this.wKey[93] ^ this.X1, this.wKey[94] ^ this.X2, this.wKey[95] ^ this.X3);
        this.LT();
        this.sb0(this.wKey[96] ^ this.X0, this.wKey[97] ^ this.X1, this.wKey[98] ^ this.X2, this.wKey[99] ^ this.X3);
        this.LT();
        this.sb1(this.wKey[100] ^ this.X0, this.wKey[101] ^ this.X1, this.wKey[102] ^ this.X2, this.wKey[103] ^ this.X3);
        this.LT();
        this.sb2(this.wKey[104] ^ this.X0, this.wKey[105] ^ this.X1, this.wKey[106] ^ this.X2, this.wKey[107] ^ this.X3);
        this.LT();
        this.sb3(this.wKey[108] ^ this.X0, this.wKey[109] ^ this.X1, this.wKey[110] ^ this.X2, this.wKey[111] ^ this.X3);
        this.LT();
        this.sb4(this.wKey[112] ^ this.X0, this.wKey[113] ^ this.X1, this.wKey[114] ^ this.X2, this.wKey[115] ^ this.X3);
        this.LT();
        this.sb5(this.wKey[116] ^ this.X0, this.wKey[117] ^ this.X1, this.wKey[118] ^ this.X2, this.wKey[119] ^ this.X3);
        this.LT();
        this.sb6(this.wKey[120] ^ this.X0, this.wKey[121] ^ this.X1, this.wKey[122] ^ this.X2, this.wKey[123] ^ this.X3);
        this.LT();
        this.sb7(this.wKey[124] ^ this.X0, this.wKey[125] ^ this.X1, this.wKey[126] ^ this.X2, this.wKey[127] ^ this.X3);
        this.wordToBytes(this.wKey[131] ^ this.X3, arrby2, n3);
        this.wordToBytes(this.wKey[130] ^ this.X2, arrby2, n3 + 4);
        this.wordToBytes(this.wKey[129] ^ this.X1, arrby2, n3 + 8);
        this.wordToBytes(this.wKey[128] ^ this.X0, arrby2, n3 + 12);
    }

    private void ib0(int n2, int n3, int n4, int n5) {
        int n6 = ~n2;
        int n7 = n2 ^ n3;
        int n8 = n5 ^ (n6 | n7);
        int n9 = n4 ^ n8;
        this.X2 = n7 ^ n9;
        int n10 = n6 ^ n7 & n5;
        this.X1 = n8 ^ n10 & this.X2;
        this.X3 = n2 & n8 ^ (n9 | this.X1);
        this.X0 = this.X3 ^ (n10 ^ n9);
    }

    private void ib1(int n2, int n3, int n4, int n5) {
        int n6 = n3 ^ n5;
        int n7 = n2 ^ n3 & n6;
        int n8 = n6 ^ n7;
        this.X3 = n4 ^ n8;
        int n9 = n3 ^ n6 & n7;
        this.X1 = n7 ^ (n9 | this.X3);
        int n10 = -1 ^ this.X1;
        int n11 = n9 ^ this.X3;
        this.X0 = n10 ^ n11;
        this.X2 = n8 ^ (n11 | n10);
    }

    private void ib2(int n2, int n3, int n4, int n5) {
        int n6 = n3 ^ n5;
        int n7 = ~n6;
        int n8 = n2 ^ n4;
        int n9 = n4 ^ n6;
        this.X0 = n8 ^ n3 & n9;
        this.X3 = n6 ^ (n8 | n5 ^ (n7 | n2));
        int n10 = ~n9;
        int n11 = this.X0 | this.X3;
        this.X1 = n10 ^ n11;
        this.X2 = n10 & n5 ^ (n11 ^ n8);
    }

    private void ib3(int n2, int n3, int n4, int n5) {
        int n6 = n2 | n3;
        int n7 = n3 ^ n4;
        int n8 = n2 ^ n3 & n7;
        int n9 = n4 ^ n8;
        int n10 = n5 | n8;
        this.X0 = n7 ^ n10;
        int n11 = n5 ^ (n7 | n10);
        this.X2 = n9 ^ n11;
        int n12 = n6 ^ n11;
        this.X3 = n8 ^ n12 & this.X0;
        this.X1 = this.X3 ^ (n12 ^ this.X0);
    }

    private void ib4(int n2, int n3, int n4, int n5) {
        int n6 = n3 ^ n2 & (n4 | n5);
        int n7 = n4 ^ n2 & n6;
        this.X1 = n5 ^ n7;
        int n8 = ~n2;
        this.X3 = n6 ^ n7 & this.X1;
        int n9 = n5 ^ (n8 | this.X1);
        this.X0 = n9 ^ this.X3;
        this.X2 = n6 & n9 ^ (n8 ^ this.X1);
    }

    private void ib5(int n2, int n3, int n4, int n5) {
        int n6 = ~n4;
        int n7 = n5 ^ n3 & n6;
        int n8 = n2 & n7;
        this.X3 = n8 ^ (n3 ^ n6);
        int n9 = n3 | this.X3;
        this.X1 = n7 ^ n2 & n9;
        int n10 = n2 | n5;
        this.X0 = n10 ^ (n6 ^ n9);
        this.X2 = n3 & n10 ^ (n8 | n2 ^ n4);
    }

    private void ib6(int n2, int n3, int n4, int n5) {
        int n6 = ~n2;
        int n7 = n2 ^ n3;
        int n8 = n4 ^ n7;
        int n9 = n5 ^ (n4 | n6);
        this.X1 = n8 ^ n9;
        int n10 = n7 ^ n8 & n9;
        this.X3 = n9 ^ (n3 | n10);
        int n11 = n3 | this.X3;
        this.X0 = n10 ^ n11;
        this.X2 = n6 & n5 ^ (n8 ^ n11);
    }

    private void ib7(int n2, int n3, int n4, int n5) {
        int n6 = n4 | n2 & n3;
        int n7 = n5 & (n2 | n3);
        this.X3 = n6 ^ n7;
        int n8 = ~n5;
        int n9 = n7 ^ n3;
        this.X1 = n2 ^ (n9 | n8 ^ this.X3);
        this.X0 = n9 ^ n4 ^ (n5 | this.X1);
        this.X2 = n6 ^ this.X1 ^ (this.X0 ^ n2 & this.X3);
    }

    private void inverseLT() {
        int n2 = this.rotateRight(this.X2, 22) ^ this.X3 ^ this.X1 << 7;
        int n3 = this.rotateRight(this.X0, 5) ^ this.X1 ^ this.X3;
        int n4 = this.rotateRight(this.X3, 7);
        int n5 = this.rotateRight(this.X1, 1);
        this.X3 = n4 ^ n2 ^ n3 << 3;
        this.X1 = n2 ^ (n5 ^ n3);
        this.X2 = this.rotateRight(n2, 3);
        this.X0 = this.rotateRight(n3, 13);
    }

    private int[] makeWorkingKey(byte[] arrby) {
        int[] arrn;
        int n2;
        int[] arrn2 = new int[16];
        int n3 = 0;
        for (n2 = -4 + arrby.length; n2 > 0; n2 -= 4) {
            int n4 = n3 + 1;
            arrn2[n3] = this.bytesToWord(arrby, n2);
            n3 = n4;
        }
        if (n2 == 0) {
            int n5 = n3 + 1;
            arrn2[n3] = this.bytesToWord(arrby, 0);
            if (n5 < 8) {
                arrn2[n5] = 1;
            }
            arrn = new int[132];
            for (int i2 = 8; i2 < 16; ++i2) {
                arrn2[i2] = this.rotateLeft(-1640531527 ^ (arrn2[i2 - 8] ^ arrn2[i2 - 5] ^ arrn2[i2 - 3] ^ arrn2[i2 - 1]) ^ i2 - 8, 11);
            }
        } else {
            throw new IllegalArgumentException("key must be a multiple of 4 bytes");
        }
        System.arraycopy((Object)arrn2, (int)8, (Object)arrn, (int)0, (int)8);
        for (int i3 = 8; i3 < 132; ++i3) {
            arrn[i3] = this.rotateLeft(i3 ^ (-1640531527 ^ (arrn[i3 - 8] ^ arrn[i3 - 5] ^ arrn[i3 - 3] ^ arrn[i3 - 1])), 11);
        }
        this.sb3(arrn[0], arrn[1], arrn[2], arrn[3]);
        arrn[0] = this.X0;
        arrn[1] = this.X1;
        arrn[2] = this.X2;
        arrn[3] = this.X3;
        this.sb2(arrn[4], arrn[5], arrn[6], arrn[7]);
        arrn[4] = this.X0;
        arrn[5] = this.X1;
        arrn[6] = this.X2;
        arrn[7] = this.X3;
        this.sb1(arrn[8], arrn[9], arrn[10], arrn[11]);
        arrn[8] = this.X0;
        arrn[9] = this.X1;
        arrn[10] = this.X2;
        arrn[11] = this.X3;
        this.sb0(arrn[12], arrn[13], arrn[14], arrn[15]);
        arrn[12] = this.X0;
        arrn[13] = this.X1;
        arrn[14] = this.X2;
        arrn[15] = this.X3;
        this.sb7(arrn[16], arrn[17], arrn[18], arrn[19]);
        arrn[16] = this.X0;
        arrn[17] = this.X1;
        arrn[18] = this.X2;
        arrn[19] = this.X3;
        this.sb6(arrn[20], arrn[21], arrn[22], arrn[23]);
        arrn[20] = this.X0;
        arrn[21] = this.X1;
        arrn[22] = this.X2;
        arrn[23] = this.X3;
        this.sb5(arrn[24], arrn[25], arrn[26], arrn[27]);
        arrn[24] = this.X0;
        arrn[25] = this.X1;
        arrn[26] = this.X2;
        arrn[27] = this.X3;
        this.sb4(arrn[28], arrn[29], arrn[30], arrn[31]);
        arrn[28] = this.X0;
        arrn[29] = this.X1;
        arrn[30] = this.X2;
        arrn[31] = this.X3;
        this.sb3(arrn[32], arrn[33], arrn[34], arrn[35]);
        arrn[32] = this.X0;
        arrn[33] = this.X1;
        arrn[34] = this.X2;
        arrn[35] = this.X3;
        this.sb2(arrn[36], arrn[37], arrn[38], arrn[39]);
        arrn[36] = this.X0;
        arrn[37] = this.X1;
        arrn[38] = this.X2;
        arrn[39] = this.X3;
        this.sb1(arrn[40], arrn[41], arrn[42], arrn[43]);
        arrn[40] = this.X0;
        arrn[41] = this.X1;
        arrn[42] = this.X2;
        arrn[43] = this.X3;
        this.sb0(arrn[44], arrn[45], arrn[46], arrn[47]);
        arrn[44] = this.X0;
        arrn[45] = this.X1;
        arrn[46] = this.X2;
        arrn[47] = this.X3;
        this.sb7(arrn[48], arrn[49], arrn[50], arrn[51]);
        arrn[48] = this.X0;
        arrn[49] = this.X1;
        arrn[50] = this.X2;
        arrn[51] = this.X3;
        this.sb6(arrn[52], arrn[53], arrn[54], arrn[55]);
        arrn[52] = this.X0;
        arrn[53] = this.X1;
        arrn[54] = this.X2;
        arrn[55] = this.X3;
        this.sb5(arrn[56], arrn[57], arrn[58], arrn[59]);
        arrn[56] = this.X0;
        arrn[57] = this.X1;
        arrn[58] = this.X2;
        arrn[59] = this.X3;
        this.sb4(arrn[60], arrn[61], arrn[62], arrn[63]);
        arrn[60] = this.X0;
        arrn[61] = this.X1;
        arrn[62] = this.X2;
        arrn[63] = this.X3;
        this.sb3(arrn[64], arrn[65], arrn[66], arrn[67]);
        arrn[64] = this.X0;
        arrn[65] = this.X1;
        arrn[66] = this.X2;
        arrn[67] = this.X3;
        this.sb2(arrn[68], arrn[69], arrn[70], arrn[71]);
        arrn[68] = this.X0;
        arrn[69] = this.X1;
        arrn[70] = this.X2;
        arrn[71] = this.X3;
        this.sb1(arrn[72], arrn[73], arrn[74], arrn[75]);
        arrn[72] = this.X0;
        arrn[73] = this.X1;
        arrn[74] = this.X2;
        arrn[75] = this.X3;
        this.sb0(arrn[76], arrn[77], arrn[78], arrn[79]);
        arrn[76] = this.X0;
        arrn[77] = this.X1;
        arrn[78] = this.X2;
        arrn[79] = this.X3;
        this.sb7(arrn[80], arrn[81], arrn[82], arrn[83]);
        arrn[80] = this.X0;
        arrn[81] = this.X1;
        arrn[82] = this.X2;
        arrn[83] = this.X3;
        this.sb6(arrn[84], arrn[85], arrn[86], arrn[87]);
        arrn[84] = this.X0;
        arrn[85] = this.X1;
        arrn[86] = this.X2;
        arrn[87] = this.X3;
        this.sb5(arrn[88], arrn[89], arrn[90], arrn[91]);
        arrn[88] = this.X0;
        arrn[89] = this.X1;
        arrn[90] = this.X2;
        arrn[91] = this.X3;
        this.sb4(arrn[92], arrn[93], arrn[94], arrn[95]);
        arrn[92] = this.X0;
        arrn[93] = this.X1;
        arrn[94] = this.X2;
        arrn[95] = this.X3;
        this.sb3(arrn[96], arrn[97], arrn[98], arrn[99]);
        arrn[96] = this.X0;
        arrn[97] = this.X1;
        arrn[98] = this.X2;
        arrn[99] = this.X3;
        this.sb2(arrn[100], arrn[101], arrn[102], arrn[103]);
        arrn[100] = this.X0;
        arrn[101] = this.X1;
        arrn[102] = this.X2;
        arrn[103] = this.X3;
        this.sb1(arrn[104], arrn[105], arrn[106], arrn[107]);
        arrn[104] = this.X0;
        arrn[105] = this.X1;
        arrn[106] = this.X2;
        arrn[107] = this.X3;
        this.sb0(arrn[108], arrn[109], arrn[110], arrn[111]);
        arrn[108] = this.X0;
        arrn[109] = this.X1;
        arrn[110] = this.X2;
        arrn[111] = this.X3;
        this.sb7(arrn[112], arrn[113], arrn[114], arrn[115]);
        arrn[112] = this.X0;
        arrn[113] = this.X1;
        arrn[114] = this.X2;
        arrn[115] = this.X3;
        this.sb6(arrn[116], arrn[117], arrn[118], arrn[119]);
        arrn[116] = this.X0;
        arrn[117] = this.X1;
        arrn[118] = this.X2;
        arrn[119] = this.X3;
        this.sb5(arrn[120], arrn[121], arrn[122], arrn[123]);
        arrn[120] = this.X0;
        arrn[121] = this.X1;
        arrn[122] = this.X2;
        arrn[123] = this.X3;
        this.sb4(arrn[124], arrn[125], arrn[126], arrn[127]);
        arrn[124] = this.X0;
        arrn[125] = this.X1;
        arrn[126] = this.X2;
        arrn[127] = this.X3;
        this.sb3(arrn[128], arrn[129], arrn[130], arrn[131]);
        arrn[128] = this.X0;
        arrn[129] = this.X1;
        arrn[130] = this.X2;
        arrn[131] = this.X3;
        return arrn;
    }

    private int rotateLeft(int n2, int n3) {
        return n2 << n3 | n2 >>> -n3;
    }

    private int rotateRight(int n2, int n3) {
        return n2 >>> n3 | n2 << -n3;
    }

    private void sb0(int n2, int n3, int n4, int n5) {
        int n6 = n2 ^ n5;
        int n7 = n4 ^ n6;
        int n8 = n3 ^ n7;
        this.X3 = n8 ^ n2 & n5;
        int n9 = n2 ^ n6 & n3;
        this.X2 = n8 ^ (n4 | n9);
        int n10 = this.X3 & (n7 ^ n9);
        this.X1 = n10 ^ ~n7;
        this.X0 = n10 ^ ~n9;
    }

    private void sb1(int n2, int n3, int n4, int n5) {
        int n6 = n3 ^ ~n2;
        int n7 = n4 ^ (n2 | n6);
        this.X2 = n5 ^ n7;
        int n8 = n3 ^ (n5 | n6);
        int n9 = n6 ^ this.X2;
        this.X3 = n9 ^ n7 & n8;
        int n10 = n8 ^ n7;
        this.X1 = n10 ^ this.X3;
        this.X0 = n7 ^ n9 & n10;
    }

    private void sb2(int n2, int n3, int n4, int n5) {
        int n6 = ~n2;
        int n7 = n3 ^ n5;
        this.X0 = n7 ^ n4 & n6;
        int n8 = n4 ^ n6;
        int n9 = n3 & (n4 ^ this.X0);
        this.X3 = n8 ^ n9;
        this.X2 = n2 ^ (n9 | n5) & (n8 | this.X0);
        this.X1 = n7 ^ this.X3 ^ (this.X2 ^ (n6 | n5));
    }

    private void sb3(int n2, int n3, int n4, int n5) {
        int n6 = n2 ^ n3;
        int n7 = n2 & n4;
        int n8 = n2 | n5;
        int n9 = n4 ^ n5;
        int n10 = n7 | n6 & n8;
        this.X2 = n9 ^ n10;
        int n11 = n10 ^ (n8 ^ n3);
        this.X0 = n6 ^ n9 & n11;
        int n12 = this.X2 & this.X0;
        this.X1 = n11 ^ n12;
        this.X3 = (n3 | n5) ^ (n12 ^ n9);
    }

    private void sb4(int n2, int n3, int n4, int n5) {
        int n6 = n2 ^ n5;
        int n7 = n4 ^ n5 & n6;
        int n8 = n3 | n7;
        this.X3 = n6 ^ n8;
        int n9 = ~n3;
        this.X0 = n7 ^ (n6 | n9);
        int n10 = n2 & this.X0;
        int n11 = n6 ^ n9;
        this.X2 = n10 ^ n8 & n11;
        this.X1 = n7 ^ n2 ^ n11 & this.X2;
    }

    private void sb5(int n2, int n3, int n4, int n5) {
        int n6 = ~n2;
        int n7 = n2 ^ n3;
        int n8 = n2 ^ n5;
        this.X0 = n4 ^ n6 ^ (n7 | n8);
        int n9 = n5 & this.X0;
        this.X1 = n9 ^ (n7 ^ this.X0);
        int n10 = n6 | this.X0;
        int n11 = n7 | n9;
        int n12 = n10 ^ n8;
        this.X2 = n11 ^ n12;
        this.X3 = n3 ^ n9 ^ n12 & this.X1;
    }

    private void sb6(int n2, int n3, int n4, int n5) {
        int n6 = ~n2;
        int n7 = n2 ^ n5;
        int n8 = n3 ^ n7;
        int n9 = n4 ^ (n6 | n7);
        this.X1 = n3 ^ n9;
        int n10 = n5 ^ (n7 | this.X1);
        this.X2 = n8 ^ n9 & n10;
        int n11 = n10 ^ n9;
        this.X0 = n11 ^ this.X2;
        this.X3 = ~n9 ^ n11 & n8;
    }

    private void sb7(int n2, int n3, int n4, int n5) {
        int n6 = n3 ^ n4;
        int n7 = n5 ^ n4 & n6;
        int n8 = n2 ^ n7;
        this.X1 = n3 ^ n8 & (n5 | n6);
        int n9 = n7 | this.X1;
        this.X3 = n6 ^ n2 & n8;
        int n10 = n8 ^ n9;
        this.X2 = n7 ^ n10 & this.X3;
        this.X0 = ~n10 ^ this.X3 & this.X2;
    }

    private void wordToBytes(int n2, byte[] arrby, int n3) {
        arrby[n3 + 3] = (byte)n2;
        arrby[n3 + 2] = (byte)(n2 >>> 8);
        arrby[n3 + 1] = (byte)(n2 >>> 16);
        arrby[n3] = (byte)(n2 >>> 24);
    }

    @Override
    public String getAlgorithmName() {
        return "Serpent";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.encrypting = bl;
            this.wKey = this.makeWorkingKey(((KeyParameter)cipherParameters).getKey());
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to Serpent init - " + cipherParameters.getClass().getName());
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public final int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.wKey == null) {
            throw new IllegalStateException("Serpent not initialised");
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
    }
}

