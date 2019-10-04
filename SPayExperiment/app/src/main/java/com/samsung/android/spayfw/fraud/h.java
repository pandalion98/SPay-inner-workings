/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 */
package com.samsung.android.spayfw.fraud;

import com.samsung.android.spayfw.fraud.b.a;
import com.samsung.android.spayfw.fraud.c;
import com.samsung.android.spayfw.fraud.d;

public abstract class h {
    protected final float nA = 0.1f;
    protected final float nB = 0.0f;
    protected final float nC = 5.5E-4f;
    protected final float nD = 0.0055f;
    protected final float nE = 0.055f;
    protected final float nF = 1.0f;
    private String nv;
    private String nw;
    protected final float nx = 1.0E-4f;
    protected final float ny = 0.001f;
    protected final float nz = 0.01f;

    public h(c c2) {
        this.nv = c2.nh;
        this.nw = c2.ng;
    }

    /*
     * Enabled aggressive block sorting
     */
    public d bA() {
        d d2 = new d();
        Integer n2 = a.af(this.nw);
        if (n2 == null) {
            com.samsung.android.spayfw.utils.h.a((RuntimeException)new NullPointerException("No version number associated with model " + this.nw + ". Make sure a version number is set in FraudRiskModels."));
            return a.bJ().bA();
        }
        d2.nj = n2;
        d2.nh = this.nv;
        float f2 = this.by();
        d2.nl = this.bz();
        d2.nk = f2 < 1.0E-4f ? 5 : (f2 < 0.001f ? 4 : (f2 < 0.01f ? 3 : (f2 < 0.1f ? 2 : 1)));
        if (d2.nk != 1) return d2;
        d2.nk = 2;
        return d2;
    }

    protected abstract float by();

    protected abstract byte[] bz();
}

