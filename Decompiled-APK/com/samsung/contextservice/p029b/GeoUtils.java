package com.samsung.contextservice.p029b;

import ch.hsr.geohash.GeoHash;
import com.samsung.contextclient.data.Poi;

/* renamed from: com.samsung.contextservice.b.d */
public class GeoUtils {
    public static double m1411a(double d) {
        return (111132.92d - (559.82d * Math.cos(((2.0d * d) * 3.141592653589793d) / 180.0d))) + (1.175d * Math.cos(((4.0d * d) * 3.141592653589793d) / 180.0d));
    }

    public static double m1412b(double d) {
        return (111412.84d * Math.cos((d * 3.141592653589793d) / 180.0d)) - (93.5d * Math.cos(((3.0d * d) * 3.141592653589793d) / 180.0d));
    }

    public static double m1413b(double d, double d2) {
        return d2 / GeoUtils.m1411a(d);
    }

    public static double m1414c(double d, double d2) {
        return d2 / GeoUtils.m1412b(d);
    }

    public static String m1416d(double d, double d2) {
        return GeoHash.m2a(d, d2, 12).m6c();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.ArrayList<java.lang.String> bQ(java.lang.String r6) {
        /*
        r5 = 3;
        r0 = 0;
        r4 = 6;
        r3 = 0;
        if (r6 != 0) goto L_0x0007;
    L_0x0006:
        return r0;
    L_0x0007:
        r1 = r6.length();
        if (r1 < r5) goto L_0x0006;
    L_0x000d:
        r1 = r6.length();
        if (r1 < r4) goto L_0x0017;
    L_0x0013:
        r6 = r6.substring(r3, r4);
    L_0x0017:
        r1 = new java.util.ArrayList;
        r1.<init>();
        r2 = r6.length();
        switch(r2) {
            case 3: goto L_0x0042;
            case 4: goto L_0x003a;
            case 5: goto L_0x0032;
            case 6: goto L_0x002b;
            default: goto L_0x0023;
        };
    L_0x0023:
        r2 = r1.size();
        if (r2 <= 0) goto L_0x0006;
    L_0x0029:
        r0 = r1;
        goto L_0x0006;
    L_0x002b:
        r2 = r6.substring(r3, r4);
        r1.add(r2);
    L_0x0032:
        r2 = 5;
        r2 = r6.substring(r3, r2);
        r1.add(r2);
    L_0x003a:
        r2 = 4;
        r2 = r6.substring(r3, r2);
        r1.add(r2);
    L_0x0042:
        r2 = r6.substring(r3, r5);
        r1.add(r2);
        goto L_0x0023;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.contextservice.b.d.bQ(java.lang.String):java.util.ArrayList<java.lang.String>");
    }

    public static double[] m1415c(double d) {
        if (d <= Poi.RADIUS_SMALL) {
            return new double[]{0.0d, Poi.RADIUS_SMALL};
        }
        if (Poi.RADIUS_SMALL < d && d <= Poi.RADIUS_MEDIUM) {
            return new double[]{Poi.RADIUS_SMALL, Poi.RADIUS_MEDIUM};
        }
        if (Poi.RADIUS_MEDIUM >= d || d > Poi.RADIUS_LARGE) {
            return new double[]{Poi.RADIUS_LARGE, 2.147483647E9d};
        }
        return new double[]{Poi.RADIUS_MEDIUM, Poi.RADIUS_LARGE};
    }
}
