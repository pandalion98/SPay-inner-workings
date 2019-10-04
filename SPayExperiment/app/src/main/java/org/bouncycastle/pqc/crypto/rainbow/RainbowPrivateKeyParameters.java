/*
 * Decompiled with CFR 0.0.
 */
package org.bouncycastle.pqc.crypto.rainbow;

import org.bouncycastle.pqc.crypto.rainbow.Layer;
import org.bouncycastle.pqc.crypto.rainbow.RainbowKeyParameters;

public class RainbowPrivateKeyParameters
extends RainbowKeyParameters {
    private short[][] A1inv;
    private short[][] A2inv;
    private short[] b1;
    private short[] b2;
    private Layer[] layers;
    private int[] vi;

    public RainbowPrivateKeyParameters(short[][] arrs, short[] arrs2, short[][] arrs3, short[] arrs4, int[] arrn, Layer[] arrlayer) {
        super(true, arrn[-1 + arrn.length] - arrn[0]);
        this.A1inv = arrs;
        this.b1 = arrs2;
        this.A2inv = arrs3;
        this.b2 = arrs4;
        this.vi = arrn;
        this.layers = arrlayer;
    }

    public short[] getB1() {
        return this.b1;
    }

    public short[] getB2() {
        return this.b2;
    }

    public short[][] getInvA1() {
        return this.A1inv;
    }

    public short[][] getInvA2() {
        return this.A2inv;
    }

    public Layer[] getLayers() {
        return this.layers;
    }

    public int[] getVi() {
        return this.vi;
    }
}

