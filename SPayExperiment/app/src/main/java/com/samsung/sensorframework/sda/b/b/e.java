/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.b.b;

import com.samsung.sensorframework.sda.a.c;
import com.samsung.sensorframework.sda.b.a;

public class e
extends a {
    public String IR;
    public String packageName;

    public e(long l2, c c2) {
        super(l2, c2);
    }

    public void bY(String string) {
        this.IR = string;
    }

    @Override
    public int getSensorType() {
        return 5017;
    }

    public void setPackageName(String string) {
        this.packageName = string;
    }
}

