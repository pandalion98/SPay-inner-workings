/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class Grain128Engine
implements StreamCipher {
    private static final int STATE_SIZE = 4;
    private int index = 4;
    private boolean initialised = false;
    private int[] lfsr;
    private int[] nfsr;
    private byte[] out;
    private int output;
    private byte[] workingIV;
    private byte[] workingKey;

    private byte getKeyStream() {
        if (this.index > 3) {
            this.oneRound();
            this.index = 0;
        }
        byte[] arrby = this.out;
        int n2 = this.index;
        this.index = n2 + 1;
        return arrby[n2];
    }

    private int getOutput() {
        int n2 = this.nfsr[0] >>> 2 | this.nfsr[1] << 30;
        int n3 = this.nfsr[0] >>> 12 | this.nfsr[1] << 20;
        int n4 = this.nfsr[0] >>> 15 | this.nfsr[1] << 17;
        int n5 = this.nfsr[1] >>> 4 | this.nfsr[2] << 28;
        int n6 = this.nfsr[1] >>> 13 | this.nfsr[2] << 19;
        int n7 = this.nfsr[2];
        int n8 = this.nfsr[2] >>> 9 | this.nfsr[3] << 23;
        int n9 = this.nfsr[2] >>> 25 | this.nfsr[3] << 7;
        int n10 = this.nfsr[2] >>> 31 | this.nfsr[3] << 1;
        int n11 = this.lfsr[0] >>> 8 | this.lfsr[1] << 24;
        int n12 = this.lfsr[0] >>> 13 | this.lfsr[1] << 19;
        int n13 = this.lfsr[0] >>> 20 | this.lfsr[1] << 12;
        int n14 = this.lfsr[1] >>> 10 | this.lfsr[2] << 22;
        int n15 = this.lfsr[1] >>> 28 | this.lfsr[2] << 4;
        int n16 = this.lfsr[2] >>> 15 | this.lfsr[3] << 17;
        int n17 = this.lfsr[2] >>> 29 | this.lfsr[3] << 3;
        int n18 = this.lfsr[2] >>> 31 | this.lfsr[3] << 1;
        return n9 ^ (n8 ^ (n7 ^ (n6 ^ (n5 ^ (n4 ^ (n2 ^ (n17 ^ (n11 & n3 ^ n12 & n13 ^ n10 & n14 ^ n15 & n16 ^ n18 & (n3 & n10)))))))));
    }

    private int getOutputLFSR() {
        int n2 = this.lfsr[0];
        int n3 = this.lfsr[0] >>> 7 | this.lfsr[1] << 25;
        int n4 = this.lfsr[1] >>> 6 | this.lfsr[2] << 26;
        int n5 = this.lfsr[2] >>> 6 | this.lfsr[3] << 26;
        int n6 = this.lfsr[2] >>> 17 | this.lfsr[3] << 15;
        return this.lfsr[3] ^ (n6 ^ (n5 ^ (n4 ^ (n2 ^ n3))));
    }

    private int getOutputNFSR() {
        int n2 = this.nfsr[0];
        int n3 = this.nfsr[0] >>> 3 | this.nfsr[1] << 29;
        int n4 = this.nfsr[0] >>> 11 | this.nfsr[1] << 21;
        int n5 = this.nfsr[0] >>> 13 | this.nfsr[1] << 19;
        int n6 = this.nfsr[0] >>> 17 | this.nfsr[1] << 15;
        int n7 = this.nfsr[0] >>> 18 | this.nfsr[1] << 14;
        int n8 = this.nfsr[0] >>> 26 | this.nfsr[1] << 6;
        int n9 = this.nfsr[0] >>> 27 | this.nfsr[1] << 5;
        int n10 = this.nfsr[1] >>> 8 | this.nfsr[2] << 24;
        int n11 = this.nfsr[1] >>> 16 | this.nfsr[2] << 16;
        int n12 = this.nfsr[1] >>> 24 | this.nfsr[2] << 8;
        int n13 = this.nfsr[1] >>> 27 | this.nfsr[2] << 5;
        int n14 = this.nfsr[1] >>> 29 | this.nfsr[2] << 3;
        int n15 = this.nfsr[2] >>> 1 | this.nfsr[3] << 31;
        int n16 = this.nfsr[2] >>> 3 | this.nfsr[3] << 29;
        int n17 = this.nfsr[2] >>> 4 | this.nfsr[3] << 28;
        int n18 = this.nfsr[2] >>> 20 | this.nfsr[3] << 12;
        int n19 = this.nfsr[2] >>> 27 | this.nfsr[3] << 5;
        return this.nfsr[3] ^ (n19 ^ (n12 ^ (n2 ^ n8))) ^ n3 & n16 ^ n4 & n5 ^ n6 & n7 ^ n9 & n13 ^ n10 & n11 ^ n14 & n15 ^ n17 & n18;
    }

    private void initGrain() {
        for (int i2 = 0; i2 < 8; ++i2) {
            this.output = this.getOutput();
            this.nfsr = this.shift(this.nfsr, this.getOutputNFSR() ^ this.lfsr[0] ^ this.output);
            this.lfsr = this.shift(this.lfsr, this.getOutputLFSR() ^ this.output);
        }
        this.initialised = true;
    }

    private void oneRound() {
        this.output = this.getOutput();
        this.out[0] = (byte)this.output;
        this.out[1] = (byte)(this.output >> 8);
        this.out[2] = (byte)(this.output >> 16);
        this.out[3] = (byte)(this.output >> 24);
        this.nfsr = this.shift(this.nfsr, this.getOutputNFSR() ^ this.lfsr[0]);
        this.lfsr = this.shift(this.lfsr, this.getOutputLFSR());
    }

    private void setKey(byte[] arrby, byte[] arrby2) {
        arrby2[12] = -1;
        arrby2[13] = -1;
        arrby2[14] = -1;
        arrby2[15] = -1;
        this.workingKey = arrby;
        this.workingIV = arrby2;
        int n2 = 0;
        for (int i2 = 0; i2 < this.nfsr.length; ++i2) {
            this.nfsr[i2] = this.workingKey[n2 + 3] << 24 | 16711680 & this.workingKey[n2 + 2] << 16 | 65280 & this.workingKey[n2 + 1] << 8 | 255 & this.workingKey[n2];
            this.lfsr[i2] = this.workingIV[n2 + 3] << 24 | 16711680 & this.workingIV[n2 + 2] << 16 | 65280 & this.workingIV[n2 + 1] << 8 | 255 & this.workingIV[n2];
            n2 += 4;
        }
    }

    private int[] shift(int[] arrn, int n2) {
        arrn[0] = arrn[1];
        arrn[1] = arrn[2];
        arrn[2] = arrn[3];
        arrn[3] = n2;
        return arrn;
    }

    @Override
    public String getAlgorithmName() {
        return "Grain-128";
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("Grain-128 Init parameters must include an IV");
        }
        ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        byte[] arrby = parametersWithIV.getIV();
        if (arrby == null || arrby.length != 12) {
            throw new IllegalArgumentException("Grain-128  requires exactly 12 bytes of IV");
        }
        if (!(parametersWithIV.getParameters() instanceof KeyParameter)) {
            throw new IllegalArgumentException("Grain-128 Init parameters must include a key");
        }
        KeyParameter keyParameter = (KeyParameter)parametersWithIV.getParameters();
        this.workingIV = new byte[keyParameter.getKey().length];
        this.workingKey = new byte[keyParameter.getKey().length];
        this.lfsr = new int[4];
        this.nfsr = new int[4];
        this.out = new byte[4];
        System.arraycopy((Object)arrby, (int)0, (Object)this.workingIV, (int)0, (int)arrby.length);
        System.arraycopy((Object)keyParameter.getKey(), (int)0, (Object)this.workingKey, (int)0, (int)keyParameter.getKey().length);
        this.reset();
    }

    @Override
    public int processBytes(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        if (!this.initialised) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        if (n2 + n3 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n4 + n3 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            arrby2[n4 + i2] = (byte)(arrby[n2 + i2] ^ this.getKeyStream());
        }
        return n3;
    }

    @Override
    public void reset() {
        this.index = 4;
        this.setKey(this.workingKey, this.workingIV);
        this.initGrain();
    }

    @Override
    public byte returnByte(byte by) {
        if (!this.initialised) {
            throw new IllegalStateException(this.getAlgorithmName() + " not initialised");
        }
        return (byte)(by ^ this.getKeyStream());
    }
}

