/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.text.SimpleDateFormat
 *  java.util.Date
 *  java.util.Locale
 *  java.util.TimeZone
 *  org.apache.http.impl.cookie.DateUtils
 */
package com.samsung.android.spayfw.payprovider.mastercard.utils;

import android.text.TextUtils;
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

    /*
     * Enabled aggressive block sorting
     */
    public static String convertToIso8601Basic(Date date, boolean bl) {
        if (date == null) {
            return null;
        }
        String string = bl ? FORMATS_ENDING_WITH_Z[1] : FORMATS_ENDING_WITH_Z[0];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(string, Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone((String)"UTC"));
        return simpleDateFormat.format(date);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static Date iso8601ToDate(String string) {
        String[] arrstring;
        if (TextUtils.isEmpty((CharSequence)string)) {
            return null;
        }
        if (string.endsWith("Z")) {
            arrstring = FORMATS_ENDING_WITH_Z;
            do {
                return org.apache.http.impl.cookie.DateUtils.parseDate((String)string, (String[])arrstring);
                break;
            } while (true);
        }
        arrstring = FORMATS_WITH_TIME_ZONE;
        return org.apache.http.impl.cookie.DateUtils.parseDate((String)string, (String[])arrstring);
    }
}

