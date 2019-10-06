package com.goodix.cap.fingerprint.sec.p001mt;

import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/* renamed from: com.goodix.cap.fingerprint.sec.mt.Common */
public class Common {
    public static final int DISABLE_DEVICE_FLAG = 0;
    public static final int ENABLE_DEVICE_FLAG = 1;
    public static final String ENABLE_MT_FLAG = "enable_mt_flag";
    public static final int MT_TEST_GROUP_ID = 10000;
    public static final int TEST_FINGER_INDEX = 1;
    public static final String TEST_USER_ID = "Test";

    public static void LOG_D(String tag, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append("GF_MT_TEST_");
        sb.append(tag);
        Log.d(sb.toString(), msg);
    }

    public static String longToString(long currentTime, String formatType) {
        return dateToString(longToDate(currentTime, formatType), formatType);
    }

    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }

    public static Date longToDate(long currentTime, String formatType) {
        return stringToDate(dateToString(new Date(currentTime), formatType), formatType);
    }

    public static Date stringToDate(String strTime, String formatType) {
        try {
            return new SimpleDateFormat(formatType).parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
