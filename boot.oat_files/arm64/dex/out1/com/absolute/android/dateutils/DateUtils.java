package com.absolute.android.dateutils;

import com.android.internal.content.NativeLibraryHelper;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {
    private static final String a(int i, int i2) {
        int i3 = 0;
        int abs = Math.abs(i);
        if (i == 0) {
            abs = 1;
        } else {
            int i4 = abs;
            abs = 0;
            while (i4 > 0) {
                i4 /= 10;
                abs++;
            }
        }
        StringBuffer stringBuffer = new StringBuffer(i2);
        if (abs < i2) {
            while (i3 < i2 - abs) {
                stringBuffer.append('0');
                i3++;
            }
        }
        stringBuffer.append(i);
        return stringBuffer.toString();
    }

    public static Calendar getCurrentTimeUTC() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT-00:00"));
    }

    public static String encodeDateAsUTC(Calendar calendar) {
        calendar.setTimeZone(TimeZone.getTimeZone("GMT-00:00"));
        StringBuffer stringBuffer = new StringBuffer(24);
        stringBuffer.append(a(calendar.get(1), 4));
        stringBuffer.append(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
        stringBuffer.append(a(calendar.get(2) + 1, 2));
        stringBuffer.append(NativeLibraryHelper.CLEAR_ABI_OVERRIDE);
        stringBuffer.append(a(calendar.get(5), 2));
        stringBuffer.append("T");
        stringBuffer.append(a(calendar.get(11), 2));
        stringBuffer.append(":");
        stringBuffer.append(a(calendar.get(12), 2));
        stringBuffer.append(":");
        stringBuffer.append(a(calendar.get(13), 2));
        stringBuffer.append("Z");
        return stringBuffer.toString();
    }

    public static String encodeDateAsLocalTime(Calendar calendar) {
        StringBuffer stringBuffer = new StringBuffer(25);
        Calendar instance = Calendar.getInstance();
        instance.setTime(calendar.getTime());
        int i = instance.get(10);
        if (i == 0) {
            i = 12;
        }
        stringBuffer.append(a(i, 2));
        stringBuffer.append(":");
        stringBuffer.append(a(instance.get(12), 2));
        stringBuffer.append(":");
        stringBuffer.append(a(instance.get(13), 2));
        stringBuffer.append(" ");
        if (instance.get(9) == 0) {
            stringBuffer.append("AM ");
        } else {
            stringBuffer.append("PM ");
        }
        stringBuffer.append(a(instance.get(1), 4));
        stringBuffer.append("/");
        stringBuffer.append(a(instance.get(2) + 1, 2));
        stringBuffer.append("/");
        stringBuffer.append(a(instance.get(5), 2));
        return stringBuffer.toString();
    }

    public static Calendar decodeUTCDateAsCalendar(String str) {
        int i;
        int i2;
        long j;
        int i3 = -1;
        int i4 = 0;
        if (str.charAt(0) == '-') {
            i = 1;
        } else {
            i = 0;
        }
        String substring = str.substring(i, i + 4);
        i += 5;
        String substring2 = str.substring(i, i + 2);
        i += 3;
        String substring3 = str.substring(i, i + 2);
        i += 3;
        String substring4 = str.substring(i, i + 2);
        i += 3;
        String substring5 = str.substring(i, i + 2);
        i += 3;
        String substring6 = str.substring(i, i + 2);
        i += 2;
        if (str.indexOf(".") != -1) {
            i++;
            String str2 = "0123456789";
            while (i < str.length() && str2.indexOf(str.charAt(i)) != -1) {
                i++;
            }
        }
        if (i >= str.length() || str.charAt(i) == 'Z') {
            i = 0;
            i2 = 1;
            i3 = 0;
        } else {
            char charAt = str.charAt(i);
            if (charAt == '-') {
                i3 = 1;
            } else if (charAt != '+') {
                i3 = 0;
            }
            if (i3 != 0) {
                i2 = Integer.parseInt(str.substring(i + 1, i + 3));
                i4 = Integer.parseInt(str.substring(i + 4, i + 6));
                i = i2;
                i2 = i3;
                i3 = 1;
            } else {
                i = 0;
                i2 = i3;
                i3 = 1;
            }
        }
        Calendar instance = Calendar.getInstance(TimeZone.getTimeZone("GMT-00:00"));
        instance.set(1, Integer.parseInt(substring));
        instance.set(2, Integer.parseInt(substring2) - 1);
        instance.set(5, Integer.parseInt(substring3));
        instance.set(11, Integer.parseInt(substring4));
        instance.set(12, Integer.parseInt(substring5));
        instance.set(13, Integer.parseInt(substring6));
        long time = instance.getTime().getTime();
        if (i3 != 0) {
            j = ((long) (((((i * 60) * 60) * 1000) + ((i4 * 60) * 1000)) * i2)) + time;
        } else {
            j = time;
        }
        instance.setTime(new Date(j));
        return instance;
    }
}
