/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Integer
 *  java.lang.Object
 *  java.lang.RuntimeException
 *  java.lang.String
 *  java.util.HashMap
 */
package com.samsung.android.spayfw.fraud.b;

import com.samsung.android.spayfw.fraud.b.d;
import com.samsung.android.spayfw.fraud.c;
import com.samsung.android.spayfw.fraud.h;
import java.util.HashMap;

public class a {
    private static final HashMap<String, Integer> om = new HashMap();

    static {
        om.put((Object)"simpleriskscore-v1", (Object)1);
        om.put((Object)"neuralnet-v1", (Object)2);
    }

    public static h a(c c2) {
        if (c2 == null) {
            return a.bJ();
        }
        if (com.samsung.android.spayfw.utils.h.DEBUG && "neuralnet-v1".equals((Object)c2.ng)) {
            return new com.samsung.android.spayfw.fraud.b.c(c2);
        }
        if ("simpleriskscore-v1".equals((Object)c2.ng)) {
            return new d(c2);
        }
        com.samsung.android.spayfw.utils.h.a((RuntimeException)new IllegalArgumentException("Illegal modelInfo.modelBase = " + c2.ng + ". Must be one of FraudRiskModels.MODEL_BASE_*."));
        return a.bJ();
    }

    public static Integer af(String string) {
        return (Integer)om.get((Object)string);
    }

    public static h bJ() {
        c c2 = new c();
        c2.ng = "simpleriskscore-v1";
        c2.nh = "simpleriskscore-default";
        return a.a(c2);
    }
}

