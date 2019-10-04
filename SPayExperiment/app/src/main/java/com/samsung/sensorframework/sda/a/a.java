/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 */
package com.samsung.sensorframework.sda.a;

import java.util.HashMap;

public class a {
    protected final HashMap<String, Object> HV = new HashMap();

    public boolean bR(String string) {
        return this.HV.containsKey((Object)string);
    }

    public Object getParameter(String string) {
        boolean bl = this.HV.containsKey((Object)string);
        Object object = null;
        if (bl) {
            object = this.HV.get((Object)string);
        }
        return object;
    }

    public void setParameter(String string, Object object) {
        this.HV.put((Object)string, object);
    }
}

