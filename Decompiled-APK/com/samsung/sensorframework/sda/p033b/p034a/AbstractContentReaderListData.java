package com.samsung.sensorframework.sda.p033b.p034a;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import com.samsung.sensorframework.sda.p033b.SensorData;
import java.util.ArrayList;

/* renamed from: com.samsung.sensorframework.sda.b.a.b */
public abstract class AbstractContentReaderListData extends SensorData {
    protected final ArrayList<AbstractContentReaderEntry> If;

    public AbstractContentReaderListData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
        this.If = new ArrayList();
    }

    public void m1512a(AbstractContentReaderEntry abstractContentReaderEntry) {
        this.If.add(abstractContentReaderEntry);
    }

    public ArrayList<AbstractContentReaderEntry> gU() {
        return this.If;
    }
}
