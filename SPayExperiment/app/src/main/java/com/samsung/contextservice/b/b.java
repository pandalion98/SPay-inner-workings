/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintWriter
 *  java.io.StringWriter
 *  java.io.Writer
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.samsung.contextservice.b;

import com.samsung.android.spayfw.b.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public final class b {
    private static volatile boolean HJ = c.HK;

    private static void a(int n2, String string, String string2) {
        if (b.c(string, n2)) {
            b.b(n2, string, string2);
        }
    }

    private static void a(int n2, String string, Throwable throwable, String string2) {
        if (b.c(string, n2)) {
            b.b(n2, string, throwable, string2);
        }
    }

    private static void a(StringWriter stringWriter, Throwable throwable) {
        throwable.printStackTrace(new PrintWriter((Writer)stringWriter));
        stringWriter.flush();
    }

    public static void a(String string, String string2, Throwable throwable) {
        b.a(6, string, throwable, string2);
    }

    private static void b(int n2, String string, String string2) {
        String string3 = "CTX_" + string;
        switch (n2) {
            default: {
                return;
            }
            case 2: {
                Log.e(string3, string2);
                return;
            }
            case 3: {
                Log.w(string3, string2);
                return;
            }
            case 4: {
                Log.v(string3, string2);
                return;
            }
            case 5: {
                Log.i(string3, string2);
                return;
            }
            case 6: 
        }
        Log.d(string3, string2);
    }

    private static void b(int n2, String string, Throwable throwable, String string2) {
        StringWriter stringWriter = new StringWriter();
        stringWriter.append((CharSequence)string2);
        stringWriter.append('\n');
        b.a(stringWriter, throwable);
        b.b(n2, string, stringWriter.toString());
    }

    public static void c(String string, String string2, Throwable throwable) {
        b.a(2, string, throwable, string2);
    }

    private static boolean c(String string, int n2) {
        boolean bl;
        block6 : {
            block5 : {
                if (string.length() > 28 - "CTX_".length()) {
                    if (!HJ) {
                        Log.d("CTX_CS", string);
                    }
                    string.substring(0, 28 - "CTX_".length());
                }
                if (n2 == 5 || n2 == 2) break block5;
                boolean bl2 = HJ;
                bl = false;
                if (bl2) break block6;
            }
            bl = true;
        }
        return bl;
    }

    public static void d(String string, String string2) {
        b.a(6, string, string2);
    }

    public static void e(String string, String string2) {
        b.a(2, string, string2);
    }

    public static void i(String string, String string2) {
        b.a(5, string, string2);
    }
}

