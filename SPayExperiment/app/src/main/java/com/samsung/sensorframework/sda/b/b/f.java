/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.b.b;

import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a;

public class f
extends a {
    private int IT;
    private String data;
    private String number;

    public f(long l2, c c2) {
        super(l2, c2);
    }

    @Override
    public int getSensorType() {
        return 5006;
    }

    public void setData(String string) {
        this.data = string;
    }

    public void setEventType(int n2) {
        this.IT = n2;
    }

    public void setNumber(String string) {
        this.number = string;
    }
}

