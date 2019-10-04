/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package org.bouncycastle.crypto.engines;

import org.bouncycastle.crypto.engines.VMPCEngine;

public class VMPCKSA3Engine
extends VMPCEngine {
    @Override
    public String getAlgorithmName() {
        return "VMPC-KSA3";
    }

    @Override
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
        for (int i5 = 0; i5 < 768; ++i5) {
            this.s = this.P[255 & this.s + this.P[i5 & 255] + arrby[i5 % arrby.length]];
            byte by = this.P[i5 & 255];
            this.P[i5 & 255] = this.P[255 & this.s];
            this.P[255 & this.s] = by;
        }
        this.n = 0;
    }
}

