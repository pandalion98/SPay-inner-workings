/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.OutputLengthException;
import org.bouncycastle.crypto.StreamCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class VMPCEngine
implements StreamCipher {
    protected byte[] P = null;
    protected byte n = 0;
    protected byte s = 0;
    protected byte[] workingIV;
    protected byte[] workingKey;

    @Override
    public String getAlgorithmName() {
        return "VMPC";
    }

    @Override
    public void init(boolean bl, CipherParameters cipherParameters) {
        if (!(cipherParameters instanceof ParametersWithIV)) {
            throw new IllegalArgumentException("VMPC init parameters must include an IV");
        }
        ParametersWithIV parametersWithIV = (ParametersWithIV)cipherParameters;
        if (!(parametersWithIV.getParameters() instanceof KeyParameter)) {
            throw new IllegalArgumentException("VMPC init parameters must include a key");
        }
        KeyParameter keyParameter = (KeyParameter)parametersWithIV.getParameters();
        this.workingIV = parametersWithIV.getIV();
        if (this.workingIV == null || this.workingIV.length < 1 || this.workingIV.length > 768) {
            throw new IllegalArgumentException("VMPC requires 1 to 768 bytes of IV");
        }
        this.workingKey = keyParameter.getKey();
        this.initKey(this.workingKey, this.workingIV);
    }

    protected void initKey(byte[] arrby, byte[] arrby2) {
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
    public int processBytes(byte[] arrby, int n2, int n3, byte[] arrby2, int n4) {
        if (n2 + n3 > arrby.length) {
            throw new DataLengthException("input buffer too short");
        }
        if (n4 + n3 > arrby2.length) {
            throw new OutputLengthException("output buffer too short");
        }
        for (int i2 = 0; i2 < n3; ++i2) {
            this.s = this.P[255 & this.s + this.P[255 & this.n]];
            byte by = this.P[255 & 1 + this.P[255 & this.P[255 & this.s]]];
            byte by2 = this.P[255 & this.n];
            this.P[255 & this.n] = this.P[255 & this.s];
            this.P[255 & this.s] = by2;
            this.n = (byte)(255 & 1 + this.n);
            arrby2[i2 + n4] = (byte)(by ^ arrby[i2 + n2]);
        }
        return n3;
    }

    @Override
    public void reset() {
        this.initKey(this.workingKey, this.workingIV);
    }

    @Override
    public byte returnByte(byte by) {
        this.s = this.P[255 & this.s + this.P[255 & this.n]];
        byte by2 = this.P[255 & 1 + this.P[255 & this.P[255 & this.s]]];
        byte by3 = this.P[255 & this.n];
        this.P[255 & this.n] = this.P[255 & this.s];
        this.P[255 & this.s] = by3;
        this.n = (byte)(255 & 1 + this.n);
        return (byte)(by2 ^ by);
    }
}

