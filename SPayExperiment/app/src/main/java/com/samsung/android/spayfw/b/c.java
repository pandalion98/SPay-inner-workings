/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.PrintWriter
 *  java.io.StringWriter
 *  java.io.Writer
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.net.UnknownHostException
 *  java.util.ArrayList
 *  java.util.Iterator
 */
package com.samsung.android.spayfw.b;

import com.samsung.android.spayfw.b.d;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;

public final class c {
    private static ArrayList<d> oJ = new ArrayList();

    public static String I(int n2) {
        switch (n2) {
            default: {
                return "UNKNOWN";
            }
            case 1: {
                return "S";
            }
            case 3: {
                return "V";
            }
            case 2: {
                return "D";
            }
            case 4: {
                return "I";
            }
            case 5: {
                return "W";
            }
            case 6: 
        }
        return "E";
    }

    public static void a(String string, String string2, Throwable throwable) {
        Iterator iterator = oJ.iterator();
        while (iterator.hasNext()) {
            ((d)iterator.next()).a(6, string, string2 + '\n' + c.getStackTraceString(throwable));
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean a(d d2) {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            boolean bl = false;
            if (d2 == null) {
                // ** MonitorExit[var5_1] (shouldn't be in output)
                return bl;
            }
            boolean bl2 = oJ.contains((Object)d2);
            bl = false;
            if (bl2) return bl;
            boolean bl3 = oJ.add((Object)d2);
            return bl3;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static d an(String string) {
        Class<c> class_ = c.class;
        synchronized (c.class) {
            boolean bl;
            d d2;
            if (string == null) {
                return null;
            }
            Iterator iterator = oJ.iterator();
            do {
                if (!iterator.hasNext()) return null;
                d2 = (d)iterator.next();
            } while (!(bl = d2.oK.equals((Object)string)));
            // ** MonitorExit[var5_1] (shouldn't be in output)
            return d2;
        }
    }

    public static void b(String string, String string2, Throwable throwable) {
        Iterator iterator = oJ.iterator();
        while (iterator.hasNext()) {
            ((d)iterator.next()).a(6, string, string2 + '\n' + c.getStackTraceString(throwable));
        }
    }

    public static void c(String string, String string2, Throwable throwable) {
        Iterator iterator = oJ.iterator();
        while (iterator.hasNext()) {
            ((d)iterator.next()).a(6, string, string2 + '\n' + c.getStackTraceString(throwable));
        }
    }

    public static void d(String string, String string2) {
        Iterator iterator = oJ.iterator();
        while (iterator.hasNext()) {
            ((d)iterator.next()).a(2, string, string2);
        }
    }

    public static void e(String string, String string2) {
        Iterator iterator = oJ.iterator();
        while (iterator.hasNext()) {
            ((d)iterator.next()).a(6, string, string2);
        }
    }

    public static String getStackTraceString(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        for (Throwable throwable2 = throwable; throwable2 != null; throwable2 = throwable2.getCause()) {
            if (!(throwable2 instanceof UnknownHostException)) continue;
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter((Writer)stringWriter));
        return stringWriter.toString();
    }

    public static void i(String string, String string2) {
        Iterator iterator = oJ.iterator();
        while (iterator.hasNext()) {
            ((d)iterator.next()).a(4, string, string2);
        }
    }

    public static void m(String string, String string2) {
        Iterator iterator = oJ.iterator();
        while (iterator.hasNext()) {
            ((d)iterator.next()).a(1, string, string2);
        }
    }

    public static void v(String string, String string2) {
        Iterator iterator = oJ.iterator();
        while (iterator.hasNext()) {
            ((d)iterator.next()).a(3, string, string2);
        }
    }

    public static void w(String string, String string2) {
        Iterator iterator = oJ.iterator();
        while (iterator.hasNext()) {
            ((d)iterator.next()).a(5, string, string2);
        }
    }
}

