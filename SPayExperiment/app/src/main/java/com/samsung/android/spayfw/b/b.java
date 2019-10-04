/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  java.lang.String
 */
package com.samsung.android.spayfw.b;

import android.util.Log;
import com.samsung.android.spayfw.b.d;

public class b
extends d {
    public b(String string) {
        super(string);
    }

    public int H(int n2) {
        if (n2 == 1) {
            n2 = 3;
        }
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void a(int n2, String string, String string2) {
        if (string2 == null || string == null || !this.isLoggable(n2)) {
            return;
        }
        int n3 = string2.length() / 2000;
        int n4 = 0;
        do {
            if (n4 >= n3) {
                Log.println((int)this.H(n2), (String)("SpayFw_" + string), (String)string2.substring(n3 * 2000, string2.length()));
                return;
            }
            Log.println((int)this.H(n2), (String)("SpayFw_" + string), (String)string2.substring(n4 * 2000, 2000 * (n4 + 1)));
            ++n4;
        } while (true);
    }
}

