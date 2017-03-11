package com.samsung.sensorframework.sda.p033b.p034a;

import com.samsung.sensorframework.sda.p030a.SensorConfig;
import java.util.HashMap;

/* renamed from: com.samsung.sensorframework.sda.b.a.k */
public class ContactsContentListData extends AbstractContentReaderListData {
    protected HashMap<String, ContactDetails> Il;

    public ContactsContentListData(long j, SensorConfig sensorConfig) {
        super(j, sensorConfig);
    }

    public void m1518a(HashMap<String, ContactDetails> hashMap) {
        this.Il = hashMap;
    }

    public int getSensorType() {
        return 5016;
    }
}
