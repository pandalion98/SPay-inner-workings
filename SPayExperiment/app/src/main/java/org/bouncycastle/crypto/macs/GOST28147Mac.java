/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithSBox;

public class GOST28147Mac
implements Mac {
    private byte[] S = new byte[]{9, 6, 3, 2, 8, 11, 1, 7, 10, 4, 14, 15, 12, 0, 13, 5, 3, 7, 14, 9, 8, 10, 15, 0, 5, 2, 6, 12, 11, 4, 13, 1, 14, 4, 6, 2, 11, 3, 13, 8, 12, 15, 5, 10, 0, 7, 1, 9, 14, 7, 10, 12, 13, 1, 3, 9, 0, 2, 11, 4, 15, 8, 5, 6, 11, 5, 1, 9, 8, 13, 15, 0, 14, 4, 2, 3, 12, 7, 10, 6, 3, 10, 13, 12, 1, 2, 0, 11, 7, 5, 9, 4, 8, 15, 14, 6, 1, 13, 2, 9, 7, 10, 6, 0, 8, 12, 4, 5, 15, 3, 11, 14, 11, 10, 15, 5, 0, 12, 14, 8, 6, 2, 3, 9, 1, 7, 13, 4};
    private int blockSize = 8;
    private byte[] buf = new byte[this.blockSize];
    private int bufOff = 0;
    private boolean firstStep = true;
    private byte[] mac = new byte[this.blockSize];
    private int macSize = 4;
    private int[] workingKey = null;

    private byte[] CM5func(byte[] arrby, int n2, byte[] arrby2) {
        byte[] arrby3 = new byte[arrby.length - n2];
        System.arraycopy((Object)arrby, (int)n2, (Object)arrby3, (int)0, (int)arrby2.length);
        for (int i2 = 0; i2 != arrby2.length; ++i2) {
            arrby3[i2] = (byte)(arrby3[i2] ^ arrby2[i2]);
        }
        return arrby3;
    }

    private int bytesToint(byte[] arrby, int n2) {
        return (-16777216 & arrby[n2 + 3] << 24) + (16711680 & arrby[n2 + 2] << 16) + (65280 & arrby[n2 + 1] << 8) + (255 & arrby[n2]);
    }

    private int[] generateWorkingKey(byte[] arrby) {
        if (arrby.length != 32) {
            throw new IllegalArgumentException("Key length invalid. Key needs to be 32 byte - 256 bit!!!");
        }
        int[] arrn = new int[8];
        for (int i2 = 0; i2 != 8; ++i2) {
            arrn[i2] = this.bytesToint(arrby, i2 * 4);
        }
        return arrn;
    }

    private void gost28147MacFunc(int[] arrn, byte[] arrby, int n2, byte[] arrby2, int n3) {
        int n4 = this.bytesToint(arrby, n2);
        int n5 = this.bytesToint(arrby, n2 + 4);
        for (int i2 = 0; i2 < 2; ++i2) {
            int n6 = n5;
            for (int i3 = 0; i3 < 8; ++i3) {
                int n7 = n6 ^ this.gost28147_mainStep(n4, arrn[i3]);
                int n8 = n4;
                n4 = n7;
                n6 = n8;
            }
            n5 = n6;
        }
        this.intTobytes(n4, arrby2, n3);
        this.intTobytes(n5, arrby2, n3 + 4);
    }

    private int gost28147_mainStep(int n2, int n3) {
        int n4 = n3 + n2;
        int n5 = (this.S[0 + (15 & n4 >> 0)] << 0) + (this.S[16 + (15 & n4 >> 4)] << 4) + (this.S[32 + (15 & n4 >> 8)] << 8) + (this.S[48 + (15 & n4 >> 12)] << 12) + (this.S[64 + (15 & n4 >> 16)] << 16) + (this.S[80 + (15 & n4 >> 20)] << 20) + (this.S[96 + (15 & n4 >> 24)] << 24) + (this.S[112 + (15 & n4 >> 28)] << 28);
        return n5 << 11 | n5 >>> 21;
    }

    private void intTobytes(int n2, byte[] arrby, int n3) {
        arrby[n3 + 3] = (byte)(n2 >>> 24);
        arrby[n3 + 2] = (byte)(n2 >>> 16);
        arrby[n3 + 1] = (byte)(n2 >>> 8);
        arrby[n3] = (byte)n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int doFinal(byte[] arrby, int n2) {
        while (this.bufOff < this.blockSize) {
            this.buf[this.bufOff] = 0;
            this.bufOff = 1 + this.bufOff;
        }
        byte[] arrby2 = new byte[this.buf.length];
        System.arraycopy((Object)this.buf, (int)0, (Object)arrby2, (int)0, (int)this.mac.length);
        if (this.firstStep) {
            this.firstStep = false;
        } else {
            arrby2 = this.CM5func(this.buf, 0, this.mac);
        }
        this.gost28147MacFunc(this.workingKey, arrby2, 0, this.mac, 0);
        System.arraycopy((Object)this.mac, (int)(this.mac.length / 2 - this.macSize), (Object)arrby, (int)n2, (int)this.macSize);
        this.reset();
        return this.macSize;
    }

    @Override
    public String getAlgorithmName() {
        return "GOST28147Mac";
    }

    @Override
    public int getMacSize() {
        return this.macSize;
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        this.reset();
        this.buf = new byte[this.blockSize];
        if (cipherParameters instanceof ParametersWithSBox) {
            ParametersWithSBox parametersWithSBox = (ParametersWithSBox)cipherParameters;
            System.arraycopy((Object)parametersWithSBox.getSBox(), (int)0, (Object)this.S, (int)0, (int)parametersWithSBox.getSBox().length);
            if (parametersWithSBox.getParameters() != null) {
                this.workingKey = this.generateWorkingKey(((KeyParameter)parametersWithSBox.getParameters()).getKey());
            }
            return;
        }
        if (cipherParameters instanceof KeyParameter) {
            this.workingKey = this.generateWorkingKey(((KeyParameter)cipherParameters).getKey());
            return;
        }
        throw new IllegalArgumentException("invalid parameter passed to GOST28147 init - " + cipherParameters.getClass().getName());
    }

    @Override
    public void reset() {
        for (int i2 = 0; i2 < this.buf.length; ++i2) {
            this.buf[i2] = 0;
        }
        this.bufOff = 0;
        this.firstStep = true;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void update(byte by) {
        if (this.bufOff == this.buf.length) {
            byte[] arrby = new byte[this.buf.length];
            System.arraycopy((Object)this.buf, (int)0, (Object)arrby, (int)0, (int)this.mac.length);
            if (this.firstStep) {
                this.firstStep = false;
            } else {
                arrby = this.CM5func(this.buf, 0, this.mac);
            }
            this.gost28147MacFunc(this.workingKey, arrby, 0, this.mac, 0);
            this.bufOff = 0;
        }
        byte[] arrby = this.buf;
        int n2 = this.bufOff;
        this.bufOff = n2 + 1;
        arrby[n2] = by;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void update(byte[] arrby, int n2, int n3) {
        int n4;
        int n5;
        if (n3 < 0) {
            throw new IllegalArgumentException("Can't have a negative input length!");
        }
        int n6 = this.blockSize - this.bufOff;
        if (n3 > n6) {
            System.arraycopy((Object)arrby, (int)n2, (Object)this.buf, (int)this.bufOff, (int)n6);
            byte[] arrby2 = new byte[this.buf.length];
            System.arraycopy((Object)this.buf, (int)0, (Object)arrby2, (int)0, (int)this.mac.length);
            if (this.firstStep) {
                this.firstStep = false;
            } else {
                arrby2 = this.CM5func(this.buf, 0, this.mac);
            }
            this.gost28147MacFunc(this.workingKey, arrby2, 0, this.mac, 0);
            this.bufOff = 0;
            int n7 = n3 - n6;
            int n8 = n2 + n6;
            n5 = n7;
            n4 = n8;
            while (n5 > this.blockSize) {
                byte[] arrby3 = this.CM5func(arrby, n4, this.mac);
                this.gost28147MacFunc(this.workingKey, arrby3, 0, this.mac, 0);
                int n9 = n5 - this.blockSize;
                int n10 = n4 + this.blockSize;
                n5 = n9;
                n4 = n10;
            }
        } else {
            n5 = n3;
            n4 = n2;
        }
        System.arraycopy((Object)arrby, (int)n4, (Object)this.buf, (int)this.bufOff, (int)n5);
        this.bufOff = n5 + this.bufOff;
    }
}

