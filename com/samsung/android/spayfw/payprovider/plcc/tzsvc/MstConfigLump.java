package com.samsung.android.spayfw.payprovider.plcc.tzsvc;

import java.util.ArrayList;
import java.util.List;

public class MstConfigLump {
    public int baudRateInUs;
    public int delayInMs;
    public List<MstConfigSegment> segments;

    public MstConfigLump(int i, int i2) {
        this.segments = new ArrayList();
        this.baudRateInUs = i;
        this.delayInMs = i2;
    }

    public boolean addSegment(MstConfigSegment mstConfigSegment) {
        return this.segments.add(mstConfigSegment);
    }
}
