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

public class IDEAEngine
implements BlockCipher {
    private static final int BASE = 65537;
    protected static final int BLOCK_SIZE = 8;
    private static final int MASK = 65535;
    private int[] workingKey = null;

    private int bytesToWord(byte[] arrby, int n2) {
        return (65280 & arrby[n2] << 8) + (255 & arrby[n2 + 1]);
    }

    /*
     * Enabled aggressive block sorting
     */
    private int[] expandKey(byte[] arrby) {
        int[] arrn = new int[52];
        if (arrby.length < 16) {
            byte[] arrby2 = new byte[16];
            System.arraycopy((Object)arrby, (int)0, (Object)arrby2, (int)(arrby2.length - arrby.length), (int)arrby.length);
            arrby = arrby2;
        }
        for (int i2 = 0; i2 < 8; ++i2) {
            arrn[i2] = this.bytesToWord(arrby, i2 * 2);
        }
        int n2 = 8;
        while (n2 < 52) {
            arrn[n2] = (n2 & 7) < 6 ? 65535 & ((127 & arrn[n2 - 7]) << 9 | arrn[n2 - 6] >> 7) : ((n2 & 7) == 6 ? 65535 & ((127 & arrn[n2 - 7]) << 9 | arrn[n2 - 14] >> 7) : 65535 & ((127 & arrn[n2 - 15]) << 9 | arrn[n2 - 14] >> 7));
            ++n2;
        }
        return arrn;
    }

    private int[] generateWorkingKey(boolean bl, byte[] arrby) {
        if (bl) {
            return this.expandKey(arrby);
        }
        return this.invertKey(this.expandKey(arrby));
    }

    private void ideaFunc(int[] arrn, byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = this.bytesToWord(arrby, n2);
        int n5 = this.bytesToWord(arrby, n2 + 2);
        int n6 = this.bytesToWord(arrby, n2 + 4);
        int n7 = this.bytesToWord(arrby, n2 + 6);
        int n8 = n4;
        int n9 = n5;
        int n10 = n6;
        int n11 = n7;
        int n12 = 0;
        for (int i2 = 0; i2 < 8; ++i2) {
            int n13 = n12 + 1;
            int n14 = this.mul(n8, arrn[n12]);
            int n15 = n13 + 1;
            int n16 = 65535 & n9 + arrn[n13];
            int n17 = n15 + 1;
            int n18 = 65535 & n10 + arrn[n15];
            int n19 = n17 + 1;
            int n20 = this.mul(n11, arrn[n17]);
            int n21 = n18 ^ n14;
            int n22 = n16 ^ n20;
            int n23 = n19 + 1;
            int n24 = this.mul(n21, arrn[n19]);
            int n25 = 65535 & n22 + n24;
            n12 = n23 + 1;
            int n26 = this.mul(n25, arrn[n23]);
            int n27 = 65535 & n24 + n26;
            n8 = n14 ^ n26;
            n11 = n20 ^ n27;
            n9 = n26 ^ n18;
            n10 = n27 ^ n16;
        }
        int n28 = n12 + 1;
        this.wordToBytes(this.mul(n8, arrn[n12]), arrby2, n3);
        int n29 = n28 + 1;
        this.wordToBytes(n10 + arrn[n28], arrby2, n3 + 2);
        int n30 = n29 + 1;
        this.wordToBytes(n9 + arrn[n29], arrby2, n3 + 4);
        this.wordToBytes(this.mul(n11, arrn[n30]), arrby2, n3 + 6);
    }

    private int[] invertKey(int[] arrn) {
        int n2;
        int[] arrn2 = new int[52];
        int n3 = this.mulInv(arrn[0]);
        int n4 = this.addInv(arrn[n2]);
        int n5 = this.addInv(arrn[2]);
        int n6 = 4;
        arrn2[51] = this.mulInv(arrn[3]);
        arrn2[50] = n5;
        arrn2[49] = n4;
        int n7 = 48;
        arrn2[n7] = n3;
        for (n2 = 1; n2 < 8; ++n2) {
            int n8 = n6 + 1;
            int n9 = arrn[n6];
            int n10 = n8 + 1;
            int n11 = arrn[n8];
            int n12 = n7 - 1;
            arrn2[n12] = n11;
            int n13 = n12 - 1;
            arrn2[n13] = n9;
            int n14 = n10 + 1;
            int n15 = this.mulInv(arrn[n10]);
            int n16 = n14 + 1;
            int n17 = this.addInv(arrn[n14]);
            int n18 = n16 + 1;
            int n19 = this.addInv(arrn[n16]);
            n6 = n18 + 1;
            int n20 = this.mulInv(arrn[n18]);
            int n21 = n13 - 1;
            arrn2[n21] = n20;
            int n22 = n21 - 1;
            arrn2[n22] = n17;
            int n23 = n22 - 1;
            arrn2[n23] = n19;
            n7 = n23 - 1;
            arrn2[n7] = n15;
        }
        int n24 = n6 + 1;
        int n25 = arrn[n6];
        int n26 = n24 + 1;
        int n27 = arrn[n24];
        int n28 = n7 - 1;
        arrn2[n28] = n27;
        int n29 = n28 - 1;
        arrn2[n29] = n25;
        int n30 = n26 + 1;
        int n31 = this.mulInv(arrn[n26]);
        int n32 = n30 + 1;
        int n33 = this.addInv(arrn[n30]);
        int n34 = n32 + 1;
        int n35 = this.addInv(arrn[n32]);
        int n36 = this.mulInv(arrn[n34]);
        int n37 = n29 - 1;
        arrn2[n37] = n36;
        int n38 = n37 - 1;
        arrn2[n38] = n35;
        int n39 = n38 - 1;
        arrn2[n39] = n33;
        arrn2[n39 - 1] = n31;
        return arrn2;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int mul(int n2, int n3) {
        int n4;
        if (n2 == 0) {
            n4 = 65537 - n3;
            return n4 & 65535;
        }
        if (n3 == 0) {
            n4 = 65537 - n2;
            return n4 & 65535;
        }
        int n5 = n2 * n3;
        int n6 = n5 & 65535;
        int n7 = n5 >>> 16;
        int n8 = n6 - n7;
        int n9 = n6 < n7 ? 1 : 0;
        n4 = n9 + n8;
        return n4 & 65535;
    }

    /*
     * Enabled aggressive block sorting
     */
    private int mulInv(int n2) {
        if (n2 >= 2) {
            int n3 = 65537 / n2;
            int n4 = 65537 % n2;
            int n5 = n2;
            n2 = 1;
            do {
                if (n4 == 1) {
                    return 65535 & 1 - n3;
                }
                int n6 = n5 / n4;
                n2 = 65535 & n2 + n6 * n3;
                if ((n5 %= n4) == 1) break;
                int n7 = n4 / n5;
                n4 %= n5;
                n3 = 65535 & n3 + n7 * n2;
            } while (true);
        }
        return n2;
    }

    private void wordToBytes(int n2, byte[] arrby, int n3) {
        arrby[n3] = (byte)(n2 >>> 8);
        arrby[n3 + 1] = (byte)n2;
    }

    int addInv(int n2) {
        return 65535 & 0 - n2;
    }

    @Override
    public String getAlgorithmName() {
        return "IDEA";
    }

    @Override
    public int getBlockSize() {
        return 8;
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (cipherParameters instanceof KeyParameter) {
            this.workingKey = this.generateWorkingKey(bl, ((KeyParameter)cipherParameters).getKey());
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to IDEA init - " + cipherParameters.getClass().getName());
    }

    @Override
    public int processBlock(byte[] arrby, int n2, byte[] arrby2, int n3) {
        if (this.workingKey == null) {
            throw new IllegalStateException("IDEA engine not initialised");
        }
        if (n2 + 8 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n3 + 8 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        this.ideaFunc(this.workingKey, arrby, n2, arrby2, n3);
        return 8;
    }

    @Override
    public void reset() {
    }
}

