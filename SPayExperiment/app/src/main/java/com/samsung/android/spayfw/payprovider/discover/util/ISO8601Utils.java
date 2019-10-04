/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.text.ParseException
 *  java.text.SimpleDateFormat
 *  java.util.Date
 *  java.util.TimeZone
 */
package com.samsung.android.spayfw.payprovider.discover.util;

import com.samsung.android.spayfw.b.c;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ISO8601Utils {
    private static final SimpleDateFormat[] yZ;
    public static final TimeZone za;

    static {
        SimpleDateFormat[] arrsimpleDateFormat = new SimpleDateFormat[]{new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")};
        yZ = arrsimpleDateFormat;
        za = TimeZone.getTimeZone((String)"UTC");
        for (SimpleDateFormat simpleDateFormat : yZ) {
            simpleDateFormat.setLenient(true);
            simpleDateFormat.setTimeZone(za);
        }
    }

    public static String a(Format format, Format format2, String string) {
        if (format.ordinal() >= Format.zd.ordinal() || format2.ordinal() >= Format.zd.ordinal()) {
            c.e("ISO8601Utils", (Object)((Object)format) + " to " + (Object)((Object)format2) + " is not supported currently.");
            return null;
        }
        try {
            Date date = yZ[format.ordinal()].parse(string);
            String string2 = yZ[format2.ordinal()].format(date);
            return string2;
        }
        catch (ParseException parseException) {
            parseException.printStackTrace();
            return null;
        }
    }

    public static final class Format
    extends Enum<Format> {
        public static final /* enum */ Format zb = new Format();
        public static final /* enum */ Format zc = new Format();
        public static final /* enum */ Format zd = new Format();
        public static final /* enum */ Format ze = new Format();
        private static final /* synthetic */ Format[] zf;

        static {
            Format[] arrformat = new Format[]{zb, zc, zd, ze};
            zf = arrformat;
        }

        public static Format valueOf(String string) {
            return (Format)Enum.valueOf(Format.class, (String)string);
        }

        public static Format[] values() {
            return (Format[])zf.clone();
        }
    }

}

