/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 */
package com.samsung.contextservice.b;

import ch.hsr.geohash.GeoHash;
import java.util.ArrayList;

public class d {
    public static double a(double d2) {
        return 111132.92 - 559.82 * Math.cos((double)(3.141592653589793 * (2.0 * d2) / 180.0)) + 1.175 * Math.cos((double)(3.141592653589793 * (4.0 * d2) / 180.0));
    }

    public static double b(double d2) {
        return 111412.84 * Math.cos((double)(d2 * 3.141592653589793 / 180.0)) - 93.5 * Math.cos((double)(3.141592653589793 * (3.0 * d2) / 180.0));
    }

    public static double b(double d2, double d3) {
        return d3 / d.a(d2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static ArrayList<String> bQ(String string) {
        ArrayList arrayList;
        block11 : {
            block10 : {
                if (string == null || string.length() < 3) break block10;
                if (string.length() >= 6) {
                    string = string.substring(0, 6);
                }
                arrayList = new ArrayList();
                switch (string.length()) {
                    default: {
                        break;
                    }
                    case 6: {
                        arrayList.add((Object)string.substring(0, 6));
                    }
                    case 5: {
                        arrayList.add((Object)string.substring(0, 5));
                    }
                    case 4: {
                        arrayList.add((Object)string.substring(0, 4));
                    }
                    case 3: {
                        arrayList.add((Object)string.substring(0, 3));
                    }
                }
                if (arrayList.size() > 0) break block11;
            }
            return null;
        }
        return arrayList;
    }

    public static double c(double d2, double d3) {
        return d3 / d.b(d2);
    }

    public static double[] c(double d2) {
        if (d2 <= 500.0) {
            return new double[]{0.0, 500.0};
        }
        if (500.0 < d2 && d2 <= 80467.0) {
            return new double[]{500.0, 80467.0};
        }
        if (80467.0 < d2 && d2 <= 321869.0) {
            return new double[]{80467.0, 321869.0};
        }
        return new double[]{321869.0, 2.147483647E9};
    }

    public static String d(double d2, double d3) {
        return GeoHash.a(d2, d3, 12).c();
    }
}

