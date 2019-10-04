/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 */
package com.samsung.sensorframework.sda.b.a;

import java.util.HashMap;

public abstract class a {
    protected HashMap<String, String> Ie = new HashMap();

    public String get(String string) {
        return (String)this.Ie.get((Object)string);
    }

    public void set(String string, String string2) {
        this.Ie.put((Object)string, (Object)string2);
    }
}

