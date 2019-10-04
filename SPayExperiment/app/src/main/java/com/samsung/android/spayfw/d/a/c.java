/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.sqlite.SQLiteDatabase
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.d.a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.d.a.b;
import com.samsung.android.spayfw.d.a.d;
import com.samsung.android.spayfw.d.a.e;
import com.samsung.android.spayfw.d.a.f;
import com.samsung.android.spayfw.d.a.g;
import com.samsung.android.spayfw.d.a.h;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.interfacelibrary.db.a;

public class c
implements com.samsung.android.spayfw.interfacelibrary.db.b {
    private com.samsung.android.spayfw.interfacelibrary.db.b Br = null;

    private c(com.samsung.android.spayfw.interfacelibrary.db.b b2) {
        this.Br = b2;
    }

    public static com.samsung.android.spayfw.interfacelibrary.db.b a(Context context, String string, int n2, DBName dBName) {
        switch (1.Bs[dBName.ordinal()]) {
            default: {
                return null;
            }
            case 1: {
                return new c(b.c(context, string, n2));
            }
            case 2: {
                return new c(e.X(context));
            }
            case 3: {
                return new c(h.d(context, string, n2));
            }
            case 4: {
                return new c(f.Y(context));
            }
            case 5: {
                return new c(g.Z(context));
            }
            case 6: 
        }
        return new c(d.W(context));
    }

    @Override
    public void a(a a2) {
        this.Br.a(a2);
    }

    @Override
    public SQLiteDatabase getReadableDatabase(byte[] arrby) {
        return this.Br.getReadableDatabase(arrby);
    }

    @Override
    public SQLiteDatabase getWritableDatabase(byte[] arrby) {
        return this.Br.getWritableDatabase(arrby);
    }

}

