package com.samsung.sensorframework.sda.p033b.p034a;

import java.util.HashMap;

/* renamed from: com.samsung.sensorframework.sda.b.a.a */
public abstract class AbstractContentReaderEntry {
    protected HashMap<String, String> Ie;

    public AbstractContentReaderEntry() {
        this.Ie = new HashMap();
    }

    public void set(String str, String str2) {
        this.Ie.put(str, str2);
    }

    public String get(String str) {
        return (String) this.Ie.get(str);
    }
}
