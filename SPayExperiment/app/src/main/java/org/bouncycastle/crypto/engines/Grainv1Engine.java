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

public class Grainv1Engine
implements StreamCipher {
    private static final int STATE_SIZE = 5;
    private int index = 2;
    private boolean initialised = false;
    private int[] lfsr;
    private int[] nfsr;
    private byte[] out;
    private int output;
    private byte[] workingIV;
    private byte[] workingKey;

    private byte getKeyStream() {
        if (this.index > 1) {
            this.oneRound();
            this.index = 0;
        }
        byte[] arrby = this.out;
        int n2 = this.index;
        this.index = n2 + 1;
        return arrby[n2];
    }

    private int getOutput() {
        int n2 = this.nfsr[0] >>> 1 | this.nfsr[1] << 15;
        int n3 = this.nfsr[0] >>> 2 | this.nfsr[1] << 14;
        int n4 = this.nfsr[0] >>> 4 | this.nfsr[1] << 12;
        int n5 = this.nfsr[0] >>> 10 | this.nfsr[1] << 6;
        int n6 = this.nfsr[1] >>> 15 | this.nfsr[2] << 1;
        int n7 = this.nfsr[2] >>> 11 | this.nfsr[3] << 5;
        int n8 = this.nfsr[3] >>> 8 | this.nfsr[4] << 8;
        int n9 = this.nfsr[3] >>> 15 | this.nfsr[4] << 1;
        int n10 = this.lfsr[0] >>> 3 | this.lfsr[1] << 13;
        int n11 = this.lfsr[1] >>> 9 | this.lfsr[2] << 7;
        int n12 = this.lfsr[2] >>> 14 | this.lfsr[3] << 2;
        int n13 = this.lfsr[4];
        return 65535 & (n8 ^ (n7 ^ (n6 ^ (n5 ^ (n4 ^ (n3 ^ (n2 ^ (n11 ^ n9 ^ n10 & n13 ^ n12 & n13 ^ n13 & n9 ^ n12 & (n10 & n11) ^ n13 & (n10 & n12) ^ n9 & (n10 & n12) ^ n9 & (n11 & n12) ^ n9 & (n12 & n13)))))))));
    }

    private int getOutputLFSR() {
        int n2 = this.lfsr[0];
        int n3 = this.lfsr[0] >>> 13 | this.lfsr[1] << 3;
        int n4 = this.lfsr[1] >>> 7 | this.lfsr[2] << 9;
        int n5 = this.lfsr[2] >>> 6 | this.lfsr[3] << 10;
        int n6 = this.lfsr[3] >>> 3 | this.lfsr[4] << 13;
        return 65535 & ((this.lfsr[3] >>> 14 | this.lfsr[4] << 2) ^ (n6 ^ (n5 ^ (n4 ^ (n2 ^ n3)))));
    }

    private int getOutputNFSR() {
        int n2 = this.nfsr[0];
        int n3 = this.nfsr[0] >>> 9 | this.nfsr[1] << 7;
        int n4 = this.nfsr[0] >>> 14 | this.nfsr[1] << 2;
        int n5 = this.nfsr[0] >>> 15 | this.nfsr[1] << 1;
        int n6 = this.nfsr[1] >>> 5 | this.nfsr[2] << 11;
        int n7 = this.nfsr[1] >>> 12 | this.nfsr[2] << 4;
        int n8 = this.nfsr[2] >>> 1 | this.nfsr[3] << 15;
        int n9 = this.nfsr[2] >>> 5 | this.nfsr[3] << 11;
        int n10 = this.nfsr[2] >>> 13 | this.nfsr[3] << 3;
        int n11 = this.nfsr[3] >>> 4 | this.nfsr[4] << 12;
        int n12 = this.nfsr[3] >>> 12 | this.nfsr[4] << 4;
        int n13 = this.nfsr[3] >>> 14 | this.nfsr[4] << 2;
        int n14 = this.nfsr[3] >>> 15 | this.nfsr[4] << 1;
        return 65535 & (n2 ^ (n3 ^ (n4 ^ (n6 ^ (n7 ^ (n8 ^ (n9 ^ (n10 ^ (n11 ^ (n13 ^ n12))))))))) ^ n14 & n12 ^ n9 & n8 ^ n5 & n3 ^ n10 & (n12 & n11) ^ n6 & (n8 & n7) ^ n3 & (n7 & (n14 & n10)) ^ n8 & (n9 & (n12 & n11)) ^ n5 & (n6 & (n14 & n12)) ^ n9 & (n10 & (n11 & (n14 & n12))) ^ n3 & (n5 & (n6 & (n8 & n7))) ^ n6 & (n7 & (n8 & (n9 & (n11 & n10)))));
    }

    private void initGrain() {
        for (int i2 = 0; i2 < 10; ++i2) {
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
        this.nfsr = this.shift(this.nfsr, this.getOutputNFSR() ^ this.lfsr[0]);
        this.lfsr = this.shift(this.lfsr, this.getOutputLFSR());
    }

    private void setKey(byte[] arrby, byte[] arrby2) {
        arrby2[8] = -1;
        arrby2[9] = -1;
        this.workingKey = arrby;
        this.workingIV = arrby2;
        int n2 = 0;
        for (int i2 = 0; i2 < this.nfsr.length; ++i2) {
            this.nfsr[i2] = 65535 & (this.workingKey[n2 + 1] << 8 | 255 & this.workingKey[n2]);
            this.lfsr[i2] = 65535 & (this.workingIV[n2 + 1] << 8 | 255 & this.workingIV[n2]);
            n2 += 2;
        }
    }

    private int[] shift(int[] arrn, int n2) {
        arrn[0] = arrn[1];
        arrn[1] = arrn[2];
        arrn[2] = arrn[3];
        arrn[3] = arrn[4];
        arrn[4] = n2;
        return arrn;
    }

    @Override
    public String getAlgorithmName() {
        return "Grain v1";
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("Grain v1 Init parameters must include an IV");
        }
        ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        byte[] arrby = parametersWithIV.getIV();
        if (arrby == null || arrby.length != 8) {
            throw new IllegalArgumentException("Grain v1 requires exactly 8 bytes of IV");
        }
        if (!(parametersWithIV.getParameters() instanceof KeyParameter)) {
            throw new IllegalArgumentException("Grain v1 Init parameters must include a key");
        }
        KeyParameter keyParameter = (KeyParameter)parametersWithIV.getParameters();
        this.workingIV = new byte[keyParameter.getKey().length];
        this.workingKey = new byte[keyParameter.getKey().length];
        this.lfsr = new int[5];
        this.nfsr = new int[5];
        this.out = new byte[2];
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
        this.index = 2;
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

