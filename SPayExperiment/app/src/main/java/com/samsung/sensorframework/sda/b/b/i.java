/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 */
package com.samsung.sensorframework.sda.b.b;

import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a;
import java.util.HashMap;

public class i
extends a {
    private int IY;
    private int IZ;
    private String Ja;
    private String Jb;
    private String Jc;
    private final HashMap<String, Integer> Jd = new HashMap();
    private String address;

    public i(long l2, c c2) {
        super(l2, c2);
    }

    public void addCategory(String string) {
        Integer n2 = (Integer)this.Jd.get((Object)string);
        if (n2 == null) {
            n2 = 0;
        }
        this.Jd.put((Object)string, (Object)(1 + n2));
    }

    public void al(int n2) {
        this.IY = n2;
    }

    public void am(int n2) {
        this.IZ = n2;
    }

    public void bZ(String string) {
        this.Jb = string;
    }

    public void ca(String string) {
        this.Jc = string;
    }

    public void cb(String string) {
        this.Ja = string;
    }

    @Override
    public int getSensorType() {
        return 5009;
    }

    public void setAddress(String string) {
        this.address = string;
    }
}

