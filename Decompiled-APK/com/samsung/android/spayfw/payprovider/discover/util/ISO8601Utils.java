package com.samsung.android.spayfw.payprovider.discover.util;

import com.samsung.android.spayfw.p002b.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class ISO8601Utils {
    private static final SimpleDateFormat[] yZ;
    public static final TimeZone za;

    public enum Format {
        UTCMS_WithZ,
        UTCS_WithZ,
        OffsetWithHoursOnly,
        OffsetWithHoursAndMins
    }

    static {
        int i = 0;
        yZ = new SimpleDateFormat[]{new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")};
        za = TimeZone.getTimeZone("UTC");
        SimpleDateFormat[] simpleDateFormatArr = yZ;
        int length = simpleDateFormatArr.length;
        while (i < length) {
            SimpleDateFormat simpleDateFormat = simpleDateFormatArr[i];
            simpleDateFormat.setLenient(true);
            simpleDateFormat.setTimeZone(za);
            i++;
        }
    }

    public static String m1055a(Format format, Format format2, String str) {
        String str2 = null;
        if (format.ordinal() >= Format.OffsetWithHoursOnly.ordinal() || format2.ordinal() >= Format.OffsetWithHoursOnly.ordinal()) {
            Log.m286e("ISO8601Utils", format + " to " + format2 + " is not supported currently.");
        } else {
            try {
                str2 = yZ[format2.ordinal()].format(yZ[format.ordinal()].parse(str));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return str2;
    }
}
