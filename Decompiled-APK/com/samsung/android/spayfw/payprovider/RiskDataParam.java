package com.samsung.android.spayfw.payprovider;

import java.io.Serializable;

public class RiskDataParam implements Serializable {
    private String key;
    private Object value;

    public RiskDataParam(String str, Object obj) {
        this.key = str;
        this.value = obj;
    }

    public String getKey() {
        return this.key;
    }

    public Object getValue() {
        return this.value;
    }
}
