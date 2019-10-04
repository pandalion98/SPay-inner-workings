/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Cloneable
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Set
 */
package com.samsung.sensorframework.sda.a;

import com.samsung.sensorframework.sda.a.a;
import java.util.HashMap;
import java.util.Set;

public class c
extends a
implements Cloneable {
    public /* synthetic */ Object clone() {
        return this.gS();
    }

    public c gS() {
        c c2 = new c();
        for (String string : this.HV.keySet()) {
            c2.setParameter(string, this.HV.get((Object)string));
        }
        return c2;
    }
}

