/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Boolean
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.a;

import com.samsung.sensorframework.sda.a.a;

public class b
extends a {
    private static final String HW = null;
    private static b HX;
    private static final Object lock;

    static {
        lock = new Object();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static b gO() {
        if (HX == null) {
            Object object;
            Object object2 = object = lock;
            synchronized (object2) {
                if (HX == null) {
                    HX = b.gP();
                }
            }
        }
        return HX;
    }

    private static b gP() {
        b b2 = new b();
        b2.setParameter("ENABLE_HASHING", true);
        b2.setParameter("INTENT_BROADCASTER_PERMISSION", HW);
        return b2;
    }

    public boolean gQ() {
        try {
            boolean bl = (Boolean)this.getParameter("ENABLE_HASHING");
            return bl;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return true;
        }
    }

    public String gR() {
        try {
            String string = (String)this.getParameter("INTENT_BROADCASTER_PERMISSION");
            return string;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return HW;
        }
    }
}

