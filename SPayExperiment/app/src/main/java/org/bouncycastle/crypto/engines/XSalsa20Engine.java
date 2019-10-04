/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.String
 *  org.bouncycastle.util.Pack
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.engines.Salsa20Engine;
import org.bouncycastle.util.Pack;

public class XSalsa20Engine
extends Salsa20Engine {
    @Override
    public String getAlgorithmName() {
        return "XSalsa20";
    }

    @Override
    protected int getNonceSize() {
        return 24;
    }

    @Override
    protected void setKey(byte[] arrby, byte[] arrby2) {
        if (arrby == null) {
            throw new IllegalArgumentException(this.getAlgorithmName() + " doesn't support re-init with null key");
        }
        if (arrby.length != 32) {
            throw new IllegalArgumentException(this.getAlgorithmName() + " requires a 256 bit key");
        }
        super.setKey(arrby, arrby2);
        this.engineState[8] = Pack.littleEndianToInt((byte[])arrby2, (int)8);
        this.engineState[9] = Pack.littleEndianToInt((byte[])arrby2, (int)12);
        int[] arrn = new int[this.engineState.length];
        XSalsa20Engine.salsaCore(20, this.engineState, arrn);
        this.engineState[1] = arrn[0] - this.engineState[0];
        this.engineState[2] = arrn[5] - this.engineState[5];
        this.engineState[3] = arrn[10] - this.engineState[10];
        this.engineState[4] = arrn[15] - this.engineState[15];
        this.engineState[11] = arrn[6] - this.engineState[6];
        this.engineState[12] = arrn[7] - this.engineState[7];
        this.engineState[13] = arrn[8] - this.engineState[8];
        this.engineState[14] = arrn[9] - this.engineState[9];
        this.engineState[6] = Pack.littleEndianToInt((byte[])arrby2, (int)16);
        this.engineState[7] = Pack.littleEndianToInt((byte[])arrby2, (int)20);
    }
}

