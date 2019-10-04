/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.sensorframework.sda.c;

import android.content.Context;
import com.samsung.sensorframework.sda.SDAException;
import com.samsung.sensorframework.sda.c.a.c;
import com.samsung.sensorframework.sda.c.a.g;
import com.samsung.sensorframework.sda.c.a.j;
import com.samsung.sensorframework.sda.c.a.k;
import com.samsung.sensorframework.sda.c.a.l;
import com.samsung.sensorframework.sda.c.a.m;
import com.samsung.sensorframework.sda.c.a.n;
import com.samsung.sensorframework.sda.c.a.o;
import com.samsung.sensorframework.sda.c.b.b;
import com.samsung.sensorframework.sda.c.b.d;
import com.samsung.sensorframework.sda.c.b.e;
import com.samsung.sensorframework.sda.c.b.f;
import com.samsung.sensorframework.sda.c.b.h;
import com.samsung.sensorframework.sda.c.b.i;

public abstract class a {
    protected final boolean Je;
    protected final boolean Jf;
    protected final Context Jg;

    public a(Context context, boolean bl, boolean bl2) {
        this.Jg = context;
        this.Je = bl;
        this.Jf = bl2;
    }

    public static a a(Context context, int n2, boolean bl, boolean bl2) {
        if (!bl && !bl2) {
            throw new SDAException(8007, "No data (raw/processed) requested from the processor");
        }
        switch (n2) {
            default: {
                throw new SDAException(8001, "No processor defined for this sensor.");
            }
            case 5001: {
                return new com.samsung.sensorframework.sda.c.a.a(context, bl, bl2);
            }
            case 5003: {
                return new c(context, bl, bl2);
            }
            case 5004: {
                return new k(context, bl, bl2);
            }
            case 5005: {
                return new com.samsung.sensorframework.sda.c.a.b(context, bl, bl2);
            }
            case 5010: {
                return new o(context, bl, bl2);
            }
            case 5002: {
                return new com.samsung.sensorframework.sda.c.b.a(context, bl, bl2);
            }
            case 5011: {
                return new com.samsung.sensorframework.sda.c.b.c(context, bl, bl2);
            }
            case 5006: {
                return new f(context, bl, bl2);
            }
            case 5008: {
                return new i(context, bl, bl2);
            }
            case 5009: {
                return new h(context, bl, bl2);
            }
            case 5007: {
                return new com.samsung.sensorframework.sda.c.b.g(context, bl, bl2);
            }
            case 5014: {
                return new com.samsung.sensorframework.sda.c.a.e(context, bl, bl2);
            }
            case 5013: {
                return new l(context, bl, bl2);
            }
            case 5016: {
                return new g(context, bl, bl2);
            }
            case 5017: {
                return new e(context, bl, bl2);
            }
            case 5019: {
                return new j(context, bl, bl2);
            }
            case 5020: {
                return new n(context, bl, bl2);
            }
            case 5021: {
                return new com.samsung.sensorframework.sda.c.a.d(context, bl, bl2);
            }
            case 5022: {
                return new d(context, bl, bl2);
            }
            case 5024: {
                return new b(context, bl, bl2);
            }
            case 5025: {
                return new m(context, bl, bl2);
            }
            case 5026: 
            case 5027: 
            case 5028: 
            case 5029: 
            case 5030: 
            case 5031: 
            case 5032: 
            case 5033: {
                return new com.samsung.sensorframework.sda.c.a.i(context, bl, bl2);
            }
            case 5037: {
                return new com.samsung.sensorframework.sda.c.a.f(context, bl, bl2);
            }
            case 5038: 
        }
        return new k(context, bl, bl2);
    }
}

