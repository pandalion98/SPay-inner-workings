package com.americanexpress.mobilepayments.hceclient.utils.common;

import com.americanexpress.mobilepayments.hceclient.exception.HCEClientException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EpochTimeConverter {
    private static final int _1000 = 1000;
    private static final SimpleDateFormat simpleDateFormat;
    private static final SimpleDateFormat simpleTimeFormat;

    static {
        simpleDateFormat = new SimpleDateFormat("yyMMdd");
        simpleTimeFormat = new SimpleDateFormat("HHmmss");
    }

    public static String getDateFromEpoch(long j) {
        return simpleDateFormat.format(new Date(1000 * j));
    }

    public static String getTimeFromEpoch(long j) {
        return simpleTimeFormat.format(new Date(1000 * j));
    }

    public static long getEpochFromDate(String str) {
        try {
            return simpleDateFormat.parse(str.trim()).getTime() / 1000;
        } catch (ParseException e) {
            throw new HCEClientException(e.getMessage());
        }
    }
}
