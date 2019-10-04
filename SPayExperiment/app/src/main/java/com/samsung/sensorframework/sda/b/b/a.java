/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.b.b;

import com.samsung.sensorframework.sda.a.c;

public class a
extends com.samsung.sensorframework.sda.b.a {
    private int Iw;
    private int Ix;
    private int Iy;
    private int Iz;
    private String action;
    private int level;
    private int scale;
    private int status;

    public a(long l2, c c2) {
        super(l2, c2);
    }

    public void ad(int n2) {
        this.level = n2;
    }

    public void ae(int n2) {
        this.Iy = n2;
    }

    public void af(int n2) {
        this.scale = n2;
    }

    public void ag(int n2) {
        this.Iw = n2;
    }

    public void ah(int n2) {
        this.Ix = n2;
    }

    public void ai(int n2) {
        this.Iz = n2;
    }

    public void bT(String string) {
        this.action = string;
    }

    public String getAction() {
        return this.action;
    }

    @Override
    public int getSensorType() {
        return 5002;
    }

    public void setStatus(int n2) {
        this.status = n2;
    }
}

