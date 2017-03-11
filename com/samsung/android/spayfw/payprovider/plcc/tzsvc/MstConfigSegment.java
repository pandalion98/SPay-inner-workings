package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

public class MstConfigSegment {
    public int leadingZeros;
    public boolean reverse;
    public int trackIndex;
    public int trailingZeros;

    public MstConfigSegment(int i, int i2, int i3, boolean z) {
        this.trackIndex = i;
        this.leadingZeros = i2;
        this.trailingZeros = i3;
        this.reverse = z;
    }
}
