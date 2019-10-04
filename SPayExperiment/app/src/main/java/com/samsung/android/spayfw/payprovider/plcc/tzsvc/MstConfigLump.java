/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

import com.samsung.android.spayfw.payprovider.plcc.tzsvc.MstConfigSegment;
import java.util.ArrayList;
import java.util.List;

public class MstConfigLump {
    public int baudRateInUs;
    public int delayInMs;
    public List<MstConfigSegment> segments = new ArrayList();

    public MstConfigLump(int n2, int n3) {
        this.baudRateInUs = n2;
        this.delayInMs = n3;
    }

    public boolean addSegment(MstConfigSegment mstConfigSegment) {
        return this.segments.add((Object)mstConfigSegment);
    }
}

