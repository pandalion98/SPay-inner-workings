package com.samsung.sensorframework.sdi.p050g;

import com.samsung.android.spayfw.p002b.Log;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

/* renamed from: com.samsung.sensorframework.sdi.g.a */
public class SFUtils {
    public static int aA(int i) {
        return (int) (Math.random() * ((double) i));
    }

    public static String ih() {
        return new SimpleDateFormat("MMM-dd-yyyy", Locale.US).format(Long.valueOf(System.currentTimeMillis()));
    }

    public static boolean m1708f(int i, int i2) {
        Log.m285d("SFUtils", "isCurrentTimeInTimeRange(), startHourInclusive: " + i + " endHourExclusive: " + i2);
        int i3 = Calendar.getInstance().get(11);
        if (i3 < i || i3 >= i2) {
            Log.m285d("SFUtils", "isCurrentTimeInTimeRange() returning false");
            return false;
        }
        Log.m285d("SFUtils", "isCurrentTimeInTimeRange() returning true");
        return true;
    }

    public static String m1707a(Collection<String> collection) {
        StringBuilder stringBuilder = new StringBuilder();
        if (collection != null && collection.size() > 0) {
            for (String str : collection) {
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(", ");
                }
                if (str != null) {
                    stringBuilder.append(str);
                } else {
                    stringBuilder.append("null");
                }
            }
        }
        if (stringBuilder.length() == 0) {
            stringBuilder.append("<empty>");
        }
        return stringBuilder.toString();
    }
}
