/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.String
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.engines.Salsa20Engine;
import org.bouncycastle.util.Pack;

public class ChaChaEngine
extends Salsa20Engine {
    public ChaChaEngine() {
    }

    public ChaChaEngine(int n2) {
        super(n2);
    }

    public static void chachaCore(int n2, int[] arrn, int[] arrn2) {
        if (arrn.length != 16) {
            throw new IllegalArgumentException();
        }
        if (arrn2.length != 16) {
            throw new IllegalArgumentException();
        }
        if (n2 % 2 != 0) {
            throw new IllegalArgumentException("Number of rounds must be even");
        }
        int n3 = arrn[0];
        int n4 = arrn[1];
        int n5 = arrn[2];
        int n6 = arrn[3];
        int n7 = arrn[4];
        int n8 = arrn[5];
        int n9 = arrn[6];
        int n10 = arrn[7];
        int n11 = arrn[8];
        int n12 = arrn[9];
        int n13 = arrn[10];
        int n14 = arrn[11];
        int n15 = arrn[12];
        int n16 = arrn[13];
        int n17 = arrn[14];
        int n18 = arrn[15];
        while (n2 > 0) {
            int n19 = n3 + n7;
            int n20 = ChaChaEngine.rotl(n15 ^ n19, 16);
            int n21 = n11 + n20;
            int n22 = ChaChaEngine.rotl(n7 ^ n21, 12);
            int n23 = n19 + n22;
            int n24 = ChaChaEngine.rotl(n20 ^ n23, 8);
            int n25 = n21 + n24;
            int n26 = ChaChaEngine.rotl(n22 ^ n25, 7);
            int n27 = n4 + n8;
            int n28 = ChaChaEngine.rotl(n16 ^ n27, 16);
            int n29 = n12 + n28;
            int n30 = ChaChaEngine.rotl(n8 ^ n29, 12);
            int n31 = n27 + n30;
            int n32 = ChaChaEngine.rotl(n28 ^ n31, 8);
            int n33 = n29 + n32;
            int n34 = ChaChaEngine.rotl(n30 ^ n33, 7);
            int n35 = n5 + n9;
            int n36 = ChaChaEngine.rotl(n17 ^ n35, 16);
            int n37 = n13 + n36;
            int n38 = ChaChaEngine.rotl(n9 ^ n37, 12);
            int n39 = n35 + n38;
            int n40 = ChaChaEngine.rotl(n36 ^ n39, 8);
            int n41 = n37 + n40;
            int n42 = ChaChaEngine.rotl(n38 ^ n41, 7);
            int n43 = n6 + n10;
            int n44 = ChaChaEngine.rotl(n18 ^ n43, 16);
            int n45 = n14 + n44;
            int n46 = ChaChaEngine.rotl(n10 ^ n45, 12);
            int n47 = n43 + n46;
            int n48 = ChaChaEngine.rotl(n44 ^ n47, 8);
            int n49 = n45 + n48;
            int n50 = ChaChaEngine.rotl(n46 ^ n49, 7);
            int n51 = n23 + n34;
            int n52 = ChaChaEngine.rotl(n48 ^ n51, 16);
            int n53 = n41 + n52;
            int n54 = ChaChaEngine.rotl(n34 ^ n53, 12);
            n3 = n51 + n54;
            n18 = ChaChaEngine.rotl(n52 ^ n3, 8);
            n13 = n53 + n18;
            n8 = ChaChaEngine.rotl(n54 ^ n13, 7);
            int n55 = n31 + n42;
            int n56 = ChaChaEngine.rotl(n24 ^ n55, 16);
            int n57 = n49 + n56;
            int n58 = ChaChaEngine.rotl(n42 ^ n57, 12);
            n4 = n55 + n58;
            n15 = ChaChaEngine.rotl(n56 ^ n4, 8);
            n14 = n57 + n15;
            n9 = ChaChaEngine.rotl(n58 ^ n14, 7);
            int n59 = n39 + n50;
            int n60 = ChaChaEngine.rotl(n32 ^ n59, 16);
            int n61 = n25 + n60;
            int n62 = ChaChaEngine.rotl(n50 ^ n61, 12);
            n5 = n59 + n62;
            n16 = ChaChaEngine.rotl(n60 ^ n5, 8);
            n11 = n61 + n16;
            n10 = ChaChaEngine.rotl(n62 ^ n11, 7);
            int n63 = n47 + n26;
            int n64 = ChaChaEngine.rotl(n40 ^ n63, 16);
            int n65 = n33 + n64;
            int n66 = ChaChaEngine.rotl(n26 ^ n65, 12);
            n6 = n63 + n66;
            n17 = ChaChaEngine.rotl(n64 ^ n6, 8);
            n12 = n65 + n17;
            n7 = ChaChaEngine.rotl(n66 ^ n12, 7);
            n2 -= 2;
        }
        arrn2[0] = n3 + arrn[0];
        arrn2[1] = n4 + arrn[1];
        arrn2[2] = n5 + arrn[2];
        arrn2[3] = n6 + arrn[3];
        arrn2[4] = n7 + arrn[4];
        arrn2[5] = n8 + arrn[5];
        arrn2[6] = n9 + arrn[6];
        arrn2[7] = n10 + arrn[7];
        arrn2[8] = n11 + arrn[8];
        arrn2[9] = n12 + arrn[9];
        arrn2[10] = n13 + arrn[10];
        arrn2[11] = n14 + arrn[11];
        arrn2[12] = n15 + arrn[12];
        arrn2[13] = n16 + arrn[13];
        arrn2[14] = n17 + arrn[14];
        arrn2[15] = n18 + arrn[15];
    }

    @Override
    protected void advanceCounter() {
        int n2;
        int[] arrn = this.engineState;
        arrn[12] = n2 = 1 + arrn[12];
        if (n2 == 0) {
            int[] arrn2 = this.engineState;
            arrn2[13] = 1 + arrn2[13];
        }
    }

    @Override
    protected void advanceCounter(long l2) {
        int n2 = (int)(l2 >>> 32);
        int n3 = (int)l2;
        if (n2 > 0) {
            int[] arrn = this.engineState;
            arrn[13] = n2 + arrn[13];
        }
        int n4 = this.engineState[12];
        int[] arrn = this.engineState;
        arrn[12] = n3 + arrn[12];
        if (n4 != 0 && this.engineState[12] < n4) {
            int[] arrn2 = this.engineState;
            arrn2[13] = 1 + arrn2[13];
        }
    }

    @Override
    protected void generateKeyStream(byte[] arrby) {
        ChaChaEngine.chachaCore(this.rounds, this.engineState, this.x);
        Pack.intToLittleEndian((int[])this.x, (byte[])arrby, (int)0);
    }

    @Override
    public String getAlgorithmName() {
        return "ChaCha" + this.rounds;
    }

    @Override
    protected long getCounter() {
        return (long)this.engineState[13] << 32 | 0xFFFFFFFFL & (long)this.engineState[12];
    }

    @Override
    protected void resetCounter() {
        int[] arrn = this.engineState;
        this.engineState[13] = 0;
        arrn[12] = 0;
    }

    @Override
    protected void retreatCounter() {
        int n2;
        if (this.engineState[12] == 0 && this.engineState[13] == 0) {
            throw new IllegalStateException("attempt to reduce counter past zero.");
        }
        int[] arrn = this.engineState;
        arrn[12] = n2 = -1 + arrn[12];
        if (n2 == -1) {
            int[] arrn2 = this.engineState;
            arrn2[13] = -1 + arrn2[13];
        }
    }

    @Override
    protected void retreatCounter(long l2) {
        int n2 = (int)(l2 >>> 32);
        int n3 = (int)l2;
        if (n2 != 0) {
            if ((0xFFFFFFFFL & (long)this.engineState[13]) < (0xFFFFFFFFL & (long)n2)) {
                throw new IllegalStateException("attempt to reduce counter past zero.");
            }
            int[] arrn = this.engineState;
            arrn[13] = arrn[13] - n2;
        }
        if ((0xFFFFFFFFL & (long)this.engineState[12]) >= (0xFFFFFFFFL & (long)n3)) {
            int[] arrn = this.engineState;
            arrn[12] = arrn[12] - n3;
            return;
        }
        if (this.engineState[13] != 0) {
            int[] arrn = this.engineState;
            arrn[13] = -1 + arrn[13];
            int[] arrn2 = this.engineState;
            arrn2[12] = arrn2[12] - n3;
            return;
        }
        throw new IllegalStateException("attempt to reduce counter past zero.");
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected void setKey(byte[] arrby, byte[] arrby2) {
        int n2 = 16;
        if (arrby != null) {
            byte[] arrby3;
            if (arrby.length != n2 && arrby.length != 32) {
                throw new IllegalArgumentException(this.getAlgorithmName() + " requires 128 bit or 256 bit key");
            }
            this.engineState[4] = Pack.littleEndianToInt((byte[])arrby, (int)0);
            this.engineState[5] = Pack.littleEndianToInt((byte[])arrby, (int)4);
            this.engineState[6] = Pack.littleEndianToInt((byte[])arrby, (int)8);
            this.engineState[7] = Pack.littleEndianToInt((byte[])arrby, (int)12);
            if (arrby.length == 32) {
                arrby3 = sigma;
            } else {
                arrby3 = tau;
                n2 = 0;
            }
            this.engineState[8] = Pack.littleEndianToInt((byte[])arrby, (int)n2);
            this.engineState[9] = Pack.littleEndianToInt((byte[])arrby, (int)(n2 + 4));
            this.engineState[10] = Pack.littleEndianToInt((byte[])arrby, (int)(n2 + 8));
            this.engineState[11] = Pack.littleEndianToInt((byte[])arrby, (int)(n2 + 12));
            this.engineState[0] = Pack.littleEndianToInt((byte[])arrby3, (int)0);
            this.engineState[1] = Pack.littleEndianToInt((byte[])arrby3, (int)4);
            this.engineState[2] = Pack.littleEndianToInt((byte[])arrby3, (int)8);
            this.engineState[3] = Pack.littleEndianToInt((byte[])arrby3, (int)12);
        }
        this.engineState[14] = Pack.littleEndianToInt((byte[])arrby2, (int)0);
        this.engineState[15] = Pack.littleEndianToInt((byte[])arrby2, (int)4);
    }
}

