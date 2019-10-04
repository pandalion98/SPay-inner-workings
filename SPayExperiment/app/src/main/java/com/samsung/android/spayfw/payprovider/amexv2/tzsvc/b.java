/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.amexv2.tzsvc;

import android.content.Context;
import android.content.SharedPreferences;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.c;

public class b {
    private static String rN = "PersistentContext";
    private SharedPreferences pp;
    c rM;

    public b(c c2) {
        this.rM = c2;
        this.pp = this.rM.getContext().getSharedPreferences("AmexTA", 0);
    }

    public void aC(String string) {
        b b2 = this;
        synchronized (b2) {
            this.pp.edit().remove(string + rN).apply();
            return;
        }
    }

    public String aD(String string) {
        return this.pp.getString(string + rN, null);
    }

    public void p(String string, String string2) {
        this.pp.edit().putString(string + rN, string2).apply();
    }

    public void q(String string, String string2) {
        b b2 = this;
        synchronized (b2) {
            String string3 = this.aD(string);
            this.aC(string);
            this.p(string2, string3);
            return;
        }
    }
}

