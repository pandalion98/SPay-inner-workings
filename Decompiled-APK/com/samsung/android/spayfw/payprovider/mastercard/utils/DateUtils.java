package com.samsung.android.spayfw.payprovider.mastercard.utils;

import android.text.TextUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {
    private static String[] FORMATS_ENDING_WITH_Z;
    private static String[] FORMATS_WITH_TIME_ZONE;

    static {
        FORMATS_WITH_TIME_ZONE = new String[]{"yyyy-MM-dd'T'HH:mm:ssZ", "yyyy-MM-dd'T'HH:mm:ss.SSSZ"};
        FORMATS_ENDING_WITH_Z = new String[]{"yyyy-MM-dd'T'HH:mm:ss'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"};
    }

    public static Date iso8601ToDate(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String[] strArr;
        if (str.endsWith("Z")) {
            strArr = FORMATS_ENDING_WITH_Z;
        } else {
            strArr = FORMATS_WITH_TIME_ZONE;
        }
        return org.apache.http.impl.cookie.DateUtils.parseDate(str, strArr);
    }

    public static String convertToIso8601Basic(Date date, boolean z) {
        if (date == null) {
            return null;
        }
        DateFormat simpleDateFormat = new SimpleDateFormat(z ? FORMATS_ENDING_WITH_Z[1] : FORMATS_ENDING_WITH_Z[0], Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(date);
    }
}
