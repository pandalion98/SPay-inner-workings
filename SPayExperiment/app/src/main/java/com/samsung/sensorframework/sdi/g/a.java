/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.text.SimpleDateFormat
 *  java.util.Calendar
 *  java.util.Collection
 *  java.util.Locale
 */
package com.samsung.sensorframework.sdi.g;

import com.samsung.android.spayfw.b.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

public class a {
    public static String a(Collection<String> collection) {
        StringBuilder stringBuilder = new StringBuilder();
        if (collection != null && collection.size() > 0) {
            for (String string : collection) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(", ");
                }
                if (string != null) {
                    stringBuilder.append(string);
                    continue;
                }
                stringBuilder.append("null");
            }
        }
        if (stringBuilder.length() == 0) {
            stringBuilder.append("<empty>");
        }
        return stringBuilder.toString();
    }

    public static int aA(int n2) {
        return (int)(Math.random() * (double)n2);
    }

    public static boolean f(int n2, int n3) {
        Log.d("SFUtils", "isCurrentTimeInTimeRange(), startHourInclusive: " + n2 + " endHourExclusive: " + n3);
        int n4 = Calendar.getInstance().get(11);
        if (n4 >= n2 && n4 < n3) {
            Log.d("SFUtils", "isCurrentTimeInTimeRange() returning true");
            return true;
        }
        Log.d("SFUtils", "isCurrentTimeInTimeRange() returning false");
        return false;
    }

    public static String ih() {
        return new SimpleDateFormat("MMM-dd-yyyy", Locale.US).format((Object)System.currentTimeMillis());
    }
}

