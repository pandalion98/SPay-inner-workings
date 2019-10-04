/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.text.ParseException
 *  java.text.SimpleDateFormat
 *  java.util.Date
 */
package com.americanexpress.mobilepayments.hceclient.utils.common;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EpochTimeConverter {
    private static final int _1000 = 1000;
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
    private static final SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HHmmss");

    public static String getDateFromEpoch(long l2) {
        return simpleDateFormat.format(new Date(1000L * l2));
    }

    public static long getEpochFromDate(String string) {
        try {
            long l2 = simpleDateFormat.parse(string.trim()).getTime() / 1000L;
            return l2;
        }
        catch (ParseException parseException) {
            throw new HCEClientException(parseException.getMessage());
        }
    }

    public static String getTimeFromEpoch(long l2) {
        return simpleTimeFormat.format(new Date(1000L * l2));
    }
}

