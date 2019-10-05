/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.b.b;

import com.samsung.android.spayfw.b.Log;
import com.samsung.sensorframework.sda.b.a;

public class d
extends a {
    private static String TAG = "MusicPlayerStateData";
    private int IJ;
    private String IK;
    private String IL;
    private String IM;
    private String IN;
    private long IO;
    private long IP;
    private long IQ;
    private int state = 0;

    public d(long l2, com.samsung.sensorframework.sda.a.c c2) {
        super(l2, c2);
    }

    public void D(long l2) {
        this.IP = l2;
    }

    public void E(long l2) {
        this.IQ = l2;
    }

    public void ak(int n2) {
        this.IJ = n2;
    }

    public void bU(String string) {
        this.IK = string;
    }

    public void bV(String string) {
        this.IL = string;
    }

    public void bW(String string) {
        this.IM = string;
    }

    public void bX(String string) {
        this.IN = string;
    }

    @Override
    public int getSensorType() {
        return 5022;
    }

    public void setId(long l2) {
        this.IO = l2;
    }

    public void setState(int n2) {
        this.state = n2;
        String string = TAG;
        Object[] arrobject = new Object[]{this.state};
        Log.d(string, String.format((String)"state: %d", (Object[])arrobject));
    }
}

