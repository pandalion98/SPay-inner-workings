package com.absolute.android.utils;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.provider.Settings$System;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.PhoneConstants;
import com.samsung.android.smartface.SmartFaceManager;
import java.lang.reflect.Method;
import java.util.UUID;

public class DeviceUtil {
    private static final String a = "APS";
    private static final boolean b = false;
    private static final String c = "000000000000000";
    private static final String d = "Android";
    private static final Object e = new Object();
    private static String f = null;
    private static Class g;
    private static Method h;

    public static String getDeviceId(Context context) {
        synchronized (e) {
            if (f == null) {
                String substring;
                String substring2;
                String manufacturer = getManufacturer();
                if (manufacturer.length() > 6) {
                    substring = manufacturer.substring(0, 6);
                } else {
                    substring = manufacturer;
                }
                manufacturer = getModel();
                if (manufacturer.length() > 20) {
                    substring2 = manufacturer.substring(0, 20);
                } else {
                    substring2 = manufacturer;
                }
                if (SmartFaceManager.PAGE_BOTTOM.equals(getSystemProperty("ro.kernel.qemu"))) {
                    manufacturer = getTelephonyId(context);
                    if (manufacturer == null) {
                        manufacturer = UUID.randomUUID().toString();
                    }
                    if (manufacturer.length() > 32) {
                        manufacturer = manufacturer.substring(0, 32);
                    }
                    f = substring + "~" + substring2 + "~" + manufacturer;
                } else {
                    manufacturer = getSerialNumber();
                    if (manufacturer.length() > 32) {
                        manufacturer = manufacturer.substring(0, 32);
                    }
                    f = substring + "~" + substring2 + "~" + manufacturer;
                }
            }
            if (f == null) {
                f = c;
            }
        }
        return f;
    }

    public static String getTelephonyId(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(PhoneConstants.PHONE_KEY);
            if (telephonyManager != null) {
                return telephonyManager.getDeviceId();
            }
        } catch (Throwable e) {
            Log.w("APS", "getTelephonyId: exception caught invoking TelephonyManager.getDeviceId(). Exception: " + ExceptionUtil.getExceptionMessage(e));
        }
        return null;
    }

    public static String getMacAddress(Context context) {
        try {
            return ((WifiManager) context.getSystemService(Settings$System.RADIO_WIFI)).getConnectionInfo().getMacAddress();
        } catch (Throwable e) {
            Log.w("APS", "getMacAddress: exception caught getting wifi MAC address. Exception: " + ExceptionUtil.getExceptionMessage(e));
            return null;
        }
    }

    public static String getManufacturer() {
        return getSystemProperty("ro.product.manufacturer");
    }

    public static String getModel() {
        return getSystemProperty("ro.product.model");
    }

    public static String getSerialNumber() {
        return getSystemProperty("ro.serialno");
    }

    public static String getHardwareName() {
        return getSystemProperty("ro.hardware");
    }

    public static String getHardwareRevision() {
        return getSystemProperty("ro.revision");
    }

    public static String getPlatform() {
        return d;
    }

    public static String getOSVersion() {
        return VERSION.RELEASE;
    }

    public static String getSystemProperty(String str) {
        String str2;
        String str3 = "";
        synchronized (e) {
            if (h == null) {
                try {
                    h = Class.forName("android.os.SystemProperties").getMethod("get", new Class[]{String.class});
                } catch (Throwable e) {
                    Log.w("APS", "getSystemProperty: exception getting android.os.SystemProperties.get() method. Exception: " + ExceptionUtil.getExceptionMessage(e));
                }
            }
            if (h != null) {
                try {
                    str2 = (String) h.invoke(g, new Object[]{str});
                } catch (Throwable e2) {
                    Log.w("APS", "getSystemProperty: exception getting invoking android.os.SystemProperties.get() method for propertyName: " + str + ". Exception: " + ExceptionUtil.getExceptionMessage(e2));
                }
            }
            str2 = str3;
        }
        return str2;
    }
}
