/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Character
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.util.Calendar
 *  java.util.TimeZone
 */
package com.samsung.android.spayfw.payprovider.mastercard.utils;

import android.content.Context;
import com.mastercard.mobile_api.bytes.ByteArray;
import com.mastercard.mobile_api.utils.Utils;
import com.samsung.android.spayfw.utils.h;
import java.util.Calendar;
import java.util.TimeZone;

public class McUtils {
    public static final int LENGTH_ZERO = 0;
    public static final String TAG_PAY = "mcpce_";
    public static final String TAG_TDS = "mctds_";

    public static String byteArrayToHex(byte[] arrby) {
        StringBuilder stringBuilder = new StringBuilder(2 * arrby.length);
        for (byte by : arrby) {
            Object[] arrobject = new Object[]{by & 255};
            stringBuilder.append(String.format((String)"%02x", (Object[])arrobject));
        }
        return stringBuilder.toString();
    }

    public static String byteToHex(byte by) {
        StringBuilder stringBuilder = new StringBuilder();
        Object[] arrobject = new Object[]{by & 255};
        stringBuilder.append(String.format((String)"%02x", (Object[])arrobject));
        return stringBuilder.toString();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean contains(ByteArray[] arrbyteArray, ByteArray byteArray) {
        if (arrbyteArray == null || arrbyteArray.length < 1 || byteArray == null) {
            return false;
        }
        int n2 = arrbyteArray.length;
        int n3 = 0;
        while (n3 < n2) {
            ByteArray byteArray2 = arrbyteArray[n3];
            if (byteArray2 != null && byteArray2.isEqual(byteArray)) {
                return true;
            }
            ++n3;
        }
        return false;
    }

    public static byte[] convertStirngToByteArray(String string) {
        int n2 = string.length();
        byte[] arrby = new byte[n2 / 2];
        for (int i2 = 0; i2 < n2; i2 += 2) {
            arrby[i2 / 2] = (byte)((Character.digit((char)string.charAt(i2), (int)16) << 4) + Character.digit((char)string.charAt(i2 + 1), (int)16));
        }
        return arrby;
    }

    public static String copySrcToDes(int n2, byte[] arrby, int n3) {
        byte[] arrby2 = new byte[n2];
        System.arraycopy((Object)arrby, (int)n3, (Object)arrby2, (int)0, (int)n2);
        return McUtils.byteArrayToHex(arrby2);
    }

    public static String getRealTime(Context context) {
        Class<McUtils> class_ = McUtils.class;
        synchronized (McUtils.class) {
            String string = String.valueOf((long)h.am(context));
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return string;
        }
    }

    public static String getUtcTimeInMilliseconds() {
        return String.valueOf((long)Calendar.getInstance((TimeZone)TimeZone.getTimeZone((String)"UTC")).getTimeInMillis());
    }

    public static boolean isStringValid(String string, int n2, int n3) {
        return string != null && !string.isEmpty() && n2 < string.length() && n3 > string.length();
    }

    public static String shortToHex(short s2) {
        byte[] arrby = new byte[]{(byte)(255 & s2 >> 8), (byte)(s2 & 255)};
        return McUtils.byteArrayToHex(arrby);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean superior(ByteArray byteArray, ByteArray byteArray2) {
        boolean bl = false;
        if (byteArray2 == null) return bl;
        bl = false;
        if (byteArray == null) return bl;
        int n2 = byteArray.getLength();
        int n3 = 0;
        do {
            int n4;
            bl = false;
            if (n3 >= n2) return bl;
            int n5 = 255 & byteArray.getByte(n3);
            if (n5 != (n4 = 255 & byteArray2.getByte(n3))) {
                bl = false;
                if (n5 <= n4) return bl;
                return true;
            }
            ++n3;
        } while (true);
    }

    public static int unsignedByteToInt(byte by) {
        return by & 255;
    }

    public static void wipe(ByteArray[] arrbyteArray) {
        if (arrbyteArray != null) {
            for (ByteArray byteArray : arrbyteArray) {
                if (byteArray == null) continue;
                Utils.clearByteArray(byteArray);
            }
        }
    }
}

