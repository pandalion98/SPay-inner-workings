package com.samsung.sensorframework.sda.p030a;

import java.util.HashMap;

/* renamed from: com.samsung.sensorframework.sda.a.a */
public class AbstractConfig {
    protected final HashMap<String, Object> HV;

    public AbstractConfig() {
        this.HV = new HashMap();
    }

    public void setParameter(String str, Object obj) {
        this.HV.put(str, obj);
    }

    public Object getParameter(String str) {
        if (this.HV.containsKey(str)) {
            return this.HV.get(str);
        }
        return null;
    }

    public boolean bR(String str) {
        if (this.HV.containsKey(str)) {
            return true;
        }
        return false;
    }
}
