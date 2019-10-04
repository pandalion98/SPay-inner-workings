/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

public class MstConfigSegment {
    public int leadingZeros;
    public boolean reverse;
    public int trackIndex;
    public int trailingZeros;

    public MstConfigSegment(int n2, int n3, int n4, boolean bl) {
        this.trackIndex = n2;
        this.leadingZeros = n3;
        this.trailingZeros = n4;
        this.reverse = bl;
    }
}

