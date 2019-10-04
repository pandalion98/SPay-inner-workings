/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.Serializable
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider;

import java.io.Serializable;

public class RiskDataParam
implements Serializable {
    private String key;
    private Object value;

    public RiskDataParam(String string, Object object) {
        this.key = string;
        this.value = object;
    }

    public String getKey() {
        return this.key;
    }

    public Object getValue() {
        return this.value;
    }
}

