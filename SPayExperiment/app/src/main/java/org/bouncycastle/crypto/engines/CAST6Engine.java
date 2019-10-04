/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.engines.CAST5Engine;

public final class CAST6Engine
extends CAST5Engine {
    protected static final int BLOCK_SIZE = 16;
    protected static final int ROUNDS = 12;
    protected int[] _Km = new int[48];
    protected int[] _Kr = new int[48];
    protected int[] _Tm = new int[192];
    protected int[] _Tr = new int[192];
    private int[] _workingKey = new int[8];

    protected final void CAST_Decipher(int n2, int n3, int n4, int n5, int[] arrn) {
        int n6 = n5;
        int n7 = n4;
        int n8 = n3;
        int n9 = n2;
        for (int i2 = 0; i2 < 6; ++i2) {
            int n10 = 4 * (11 - i2);
            n6 ^= this.F1(n9 ^= this.F3(n8 ^= this.F2(n7 ^= this.F1(n6, this._Km[n10], this._Kr[n10]), this._Km[n10 + 1], this._Kr[n10 + 1]), this._Km[n10 + 2], this._Kr[n10 + 2]), this._Km[n10 + 3], this._Kr[n10 + 3]);
        }
        for (int i3 = 6; i3 < 12; ++i3) {
            int n11 = 4 * (11 - i3);
            n8 ^= this.F2(n7, this._Km[n11 + 1], this._Kr[n11 + 1]);
            n7 ^= this.F1(n6 ^= this.F1(n9 ^= this.F3(n8, this._Km[n11 + 2], this._Kr[n11 + 2]), this._Km[n11 + 3], this._Kr[n11 + 3]), this._Km[n11], this._Kr[n11]);
        }
        arrn[0] = n9;
        arrn[1] = n8;
        arrn[2] = n7;
        arrn[3] = n6;
    }

    protected final void CAST_Encipher(int n2, int n3, int n4, int n5, int[] arrn) {
        int n6 = n5;
        int n7 = n4;
        int n8 = n3;
        int n9 = n2;
        for (int i2 = 0; i2 < 6; ++i2) {
            int n10 = i2 * 4;
            n6 ^= this.F1(n9 ^= this.F3(n8 ^= this.F2(n7 ^= this.F1(n6, this._Km[n10], this._Kr[n10]), this._Km[n10 + 1], this._Kr[n10 + 1]), this._Km[n10 + 2], this._Kr[n10 + 2]), this._Km[n10 + 3], this._Kr[n10 + 3]);
        }
        for (int i3 = 6; i3 < 12; ++i3) {
            int n11 = i3 * 4;
            n8 ^= this.F2(n7, this._Km[n11 + 1], this._Kr[n11 + 1]);
            n7 ^= this.F1(n6 ^= this.F1(n9 ^= this.F3(n8, this._Km[n11 + 2], this._Kr[n11 + 2]), this._Km[n11 + 3], this._Kr[n11 + 3]), this._Km[n11], this._Kr[n11]);
        }
        arrn[0] = n9;
        arrn[1] = n8;
        arrn[2] = n7;
        arrn[3] = n6;
    }

    @Override
    protected int decryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int[] arrn = new int[4];
        this.CAST_Decipher(this.BytesTo32bits(arrby, n2), this.BytesTo32bits(arrby, n2 + 4), this.BytesTo32bits(arrby, n2 + 8), this.BytesTo32bits(arrby, n2 + 12), arrn);
        this.Bits32ToBytes(arrn[0], arrby2, n3);
        this.Bits32ToBytes(arrn[1], arrby2, n3 + 4);
        this.Bits32ToBytes(arrn[2], arrby2, n3 + 8);
        this.Bits32ToBytes(arrn[3], arrby2, n3 + 12);
        return 16;
    }

    @Override
    protected int encryptBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        int[] arrn = new int[4];
        this.CAST_Encipher(this.BytesTo32bits(arrby, n2), this.BytesTo32bits(arrby, n2 + 4), this.BytesTo32bits(arrby, n2 + 8), this.BytesTo32bits(arrby, n2 + 12), arrn);
        this.Bits32ToBytes(arrn[0], arrby2, n3);
        this.Bits32ToBytes(arrn[1], arrby2, n3 + 4);
        this.Bits32ToBytes(arrn[2], arrby2, n3 + 8);
        this.Bits32ToBytes(arrn[3], arrby2, n3 + 12);
        return 16;
    }

    @Override
    public String getAlgorithmName() {
        return "CAST6";
    }

    @Override
    public int getBlockSize() {
        return 16;
    }

    @Override
    public void reset() {
    }

    @Override
    protected void setKey(byte[] arrby) {
        int n2 = 1518500249;
        int n3 = 19;
        for (int i2 = 0; i2 < 24; ++i2) {
            int n4 = n2;
            int n5 = n3;
            for (int i3 = 0; i3 < 8; ++i3) {
                this._Tm[i3 + i2 * 8] = n4;
                n4 += 1859775393;
                this._Tr[i3 + i2 * 8] = n5;
                n5 = 31 & n5 + 17;
            }
            n3 = n5;
            n2 = n4;
        }
        byte[] arrby2 = new byte[64];
        System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)0, (int)arrby.length);
        for (int i4 = 0; i4 < 8; ++i4) {
            this._workingKey[i4] = this.BytesTo32bits(arrby2, i4 * 4);
        }
        for (int i5 = 0; i5 < 12; ++i5) {
            int n6 = 8 * (i5 * 2);
            int[] arrn = this._workingKey;
            arrn[6] = arrn[6] ^ this.F1(this._workingKey[7], this._Tm[n6], this._Tr[n6]);
            int[] arrn2 = this._workingKey;
            arrn2[5] = arrn2[5] ^ this.F2(this._workingKey[6], this._Tm[n6 + 1], this._Tr[n6 + 1]);
            int[] arrn3 = this._workingKey;
            arrn3[4] = arrn3[4] ^ this.F3(this._workingKey[5], this._Tm[n6 + 2], this._Tr[n6 + 2]);
            int[] arrn4 = this._workingKey;
            arrn4[3] = arrn4[3] ^ this.F1(this._workingKey[4], this._Tm[n6 + 3], this._Tr[n6 + 3]);
            int[] arrn5 = this._workingKey;
            arrn5[2] = arrn5[2] ^ this.F2(this._workingKey[3], this._Tm[n6 + 4], this._Tr[n6 + 4]);
            int[] arrn6 = this._workingKey;
            arrn6[1] = arrn6[1] ^ this.F3(this._workingKey[2], this._Tm[n6 + 5], this._Tr[n6 + 5]);
            int[] arrn7 = this._workingKey;
            arrn7[0] = arrn7[0] ^ this.F1(this._workingKey[1], this._Tm[n6 + 6], this._Tr[n6 + 6]);
            int[] arrn8 = this._workingKey;
            arrn8[7] = arrn8[7] ^ this.F2(this._workingKey[0], this._Tm[n6 + 7], this._Tr[n6 + 7]);
            int n7 = 8 * (1 + i5 * 2);
            int[] arrn9 = this._workingKey;
            arrn9[6] = arrn9[6] ^ this.F1(this._workingKey[7], this._Tm[n7], this._Tr[n7]);
            int[] arrn10 = this._workingKey;
            arrn10[5] = arrn10[5] ^ this.F2(this._workingKey[6], this._Tm[n7 + 1], this._Tr[n7 + 1]);
            int[] arrn11 = this._workingKey;
            arrn11[4] = arrn11[4] ^ this.F3(this._workingKey[5], this._Tm[n7 + 2], this._Tr[n7 + 2]);
            int[] arrn12 = this._workingKey;
            arrn12[3] = arrn12[3] ^ this.F1(this._workingKey[4], this._Tm[n7 + 3], this._Tr[n7 + 3]);
            int[] arrn13 = this._workingKey;
            arrn13[2] = arrn13[2] ^ this.F2(this._workingKey[3], this._Tm[n7 + 4], this._Tr[n7 + 4]);
            int[] arrn14 = this._workingKey;
            arrn14[1] = arrn14[1] ^ this.F3(this._workingKey[2], this._Tm[n7 + 5], this._Tr[n7 + 5]);
            int[] arrn15 = this._workingKey;
            arrn15[0] = arrn15[0] ^ this.F1(this._workingKey[1], this._Tm[n7 + 6], this._Tr[n7 + 6]);
            int[] arrn16 = this._workingKey;
            arrn16[7] = arrn16[7] ^ this.F2(this._workingKey[0], this._Tm[n7 + 7], this._Tr[n7 + 7]);
            this._Kr[i5 * 4] = 31 & this._workingKey[0];
            this._Kr[1 + i5 * 4] = 31 & this._workingKey[2];
            this._Kr[2 + i5 * 4] = 31 & this._workingKey[4];
            this._Kr[3 + i5 * 4] = 31 & this._workingKey[6];
            this._Km[i5 * 4] = this._workingKey[7];
            this._Km[1 + i5 * 4] = this._workingKey[5];
            this._Km[2 + i5 * 4] = this._workingKey[3];
            this._Km[3 + i5 * 4] = this._workingKey[1];
        }
    }
}

