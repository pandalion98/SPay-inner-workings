/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 */
package org.bouncycastle.crypto.macs;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Mac;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class VMPCMac
implements Mac {
    private byte[] P = null;
    private byte[] T;
    private byte g;
    private byte n = 0;
    private byte s = 0;
    private byte[] workingIV;
    private byte[] workingKey;
    private byte x1;
    private byte x2;
    private byte x3;
    private byte x4;

    private void initKey(byte[] arrby, byte[] arrby2) {
        this.s = 0;
        this.P = new byte[256];
        for (int i2 = 0; i2 < 256; ++i2) {
            this.P[i2] = (byte)i2;
        }
        for (int i3 = 0; i3 < 768; ++i3) {
            this.s = this.P[255 & this.s + this.P[i3 & 255] + arrby[i3 % arrby.length]];
            byte by = this.P[i3 & 255];
            this.P[i3 & 255] = this.P[255 & this.s];
            this.P[255 & this.s] = by;
        }
        for (int i4 = 0; i4 < 768; ++i4) {
            this.s = this.P[255 & this.s + this.P[i4 & 255] + arrby2[i4 % arrby2.length]];
            byte by = this.P[i4 & 255];
            this.P[i4 & 255] = this.P[255 & this.s];
            this.P[255 & this.s] = by;
        }
        this.n = 0;
    }

    @Override
    public int doFinal(byte[] arrby, int n2) {
        for (int i2 = 1; i2 < 25; ++i2) {
            this.s = this.P[255 & this.s + this.P[255 & this.n]];
            this.x4 = this.P[255 & i2 + (this.x4 + this.x3)];
            this.x3 = this.P[255 & i2 + (this.x3 + this.x2)];
            this.x2 = this.P[255 & i2 + (this.x2 + this.x1)];
            this.x1 = this.P[255 & i2 + (this.x1 + this.s)];
            this.T[31 & this.g] = (byte)(this.T[31 & this.g] ^ this.x1);
            this.T[31 & 1 + this.g] = (byte)(this.T[31 & 1 + this.g] ^ this.x2);
            this.T[31 & 2 + this.g] = (byte)(this.T[31 & 2 + this.g] ^ this.x3);
            this.T[31 & 3 + this.g] = (byte)(this.T[31 & 3 + this.g] ^ this.x4);
            this.g = (byte)(31 & 4 + this.g);
            byte by = this.P[255 & this.n];
            this.P[255 & this.n] = this.P[255 & this.s];
            this.P[255 & this.s] = by;
            this.n = (byte)(255 & 1 + this.n);
        }
        for (int i3 = 0; i3 < 768; ++i3) {
            this.s = this.P[255 & this.s + this.P[i3 & 255] + this.T[i3 & 31]];
            byte by = this.P[i3 & 255];
            this.P[i3 & 255] = this.P[255 & this.s];
            this.P[255 & this.s] = by;
        }
        byte[] arrby2 = new byte[20];
        for (int i4 = 0; i4 < 20; ++i4) {
            this.s = this.P[255 & this.s + this.P[i4 & 255]];
            arrby2[i4] = this.P[255 & 1 + this.P[255 & this.P[255 & this.s]]];
            byte by = this.P[i4 & 255];
            this.P[i4 & 255] = this.P[255 & this.s];
            this.P[255 & this.s] = by;
        }
        System.arraycopy((Object)arrby2, (int)0, (Object)arrby, (int)n2, (int)arrby2.length);
        this.reset();
        return arrby2.length;
    }

    @Override
    public String getAlgorithmName() {
        return "VMPC-MAC";
    }

    @Override
    public int getMacSize() {
        return 20;
    }

    @Override
    public void init(CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("VMPC-MAC Init parameters must include an IV");
        }
        ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        KeyParameter keyParameter = (KeyParameter)parametersWithIV.getParameters();
        if (!(parametersWithIV.getParameters() instanceof KeyParameter)) {
            throw new IllegalArgumentException("VMPC-MAC Init parameters must include a key");
        }
        this.workingIV = parametersWithIV.getIV();
        if (this.workingIV == null || this.workingIV.length < 1 || this.workingIV.length > 768) {
            throw new IllegalArgumentException("VMPC-MAC requires 1 to 768 bytes of IV");
        }
        this.workingKey = keyParameter.getKey();
        this.reset();
    }

    @Override
    public void reset() {
        this.initKey(this.workingKey, this.workingIV);
        this.n = 0;
        this.x4 = 0;
        this.x3 = 0;
        this.x2 = 0;
        this.x1 = 0;
        this.g = 0;
        this.T = new byte[32];
        for (int i2 = 0; i2 < 32; ++i2) {
            this.T[i2] = 0;
        }
    }

    @Override
    public void update(byte by) {
        this.s = this.P[255 & this.s + this.P[255 & this.n]];
        byte by2 = (byte)(by ^ this.P[255 & 1 + this.P[255 & this.P[255 & this.s]]]);
        this.x4 = this.P[255 & this.x4 + this.x3];
        this.x3 = this.P[255 & this.x3 + this.x2];
        this.x2 = this.P[255 & this.x2 + this.x1];
        this.x1 = this.P[255 & by2 + (this.x1 + this.s)];
        this.T[31 & this.g] = (byte)(this.T[31 & this.g] ^ this.x1);
        this.T[31 & 1 + this.g] = (byte)(this.T[31 & 1 + this.g] ^ this.x2);
        this.T[31 & 2 + this.g] = (byte)(this.T[31 & 2 + this.g] ^ this.x3);
        this.T[31 & 3 + this.g] = (byte)(this.T[31 & 3 + this.g] ^ this.x4);
        this.g = (byte)(31 & 4 + this.g);
        byte by3 = this.P[255 & this.n];
        this.P[255 & this.n] = this.P[255 & this.s];
        this.P[255 & this.s] = by3;
        this.n = (byte)(255 & 1 + this.n);
    }

    @Override
    public void update(byte[] arrby, int n2, int n3) {
        if (n2 + n3 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            this.update(arrby[i2]);
        }
    }
}

