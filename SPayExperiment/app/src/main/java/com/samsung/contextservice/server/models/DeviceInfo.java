/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.bluetooth.BluetoothAdapter
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.os.Build
 *  android.provider.Settings
 *  android.provider.Settings$Global
 *  android.provider.Settings$Secure
 *  android.provider.Settings$SettingNotFoundException
 *  android.provider.Settings$System
 *  android.telephony.TelephonyManager
 *  android.util.Base64
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.Locale
 *  java.util.TimeZone
 */
package com.samsung.contextservice.server.models;

import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import com.samsung.android.spayfw.a.a;
import com.samsung.android.spayfw.remoteservice.e.c;
import com.samsung.contextservice.b.b;
import com.samsung.contextservice.server.models.Id;
import java.util.Locale;
import java.util.TimeZone;

public class DeviceInfo
extends Id {
    private static final String TAG = "DeviceInfo";
    private String androidId;
    private String imei;
    private Locale locale;
    private String mac;
    private String name;
    private String serial;
    private Timezone timezone;

    private DeviceInfo(String string) {
        super(string);
    }

    public static final String getAndroidId(Context context) {
        return Settings.Secure.getString((ContentResolver)context.getContentResolver(), (String)"android_id");
    }

    public static final boolean getAutoTimeZone(Context context) {
        try {
            int n2 = Settings.Global.getInt((ContentResolver)context.getContentResolver(), (String)"auto_time_zone");
            return n2 == 1;
        }
        catch (Settings.SettingNotFoundException settingNotFoundException) {
            b.c(TAG, settingNotFoundException.getMessage(), settingNotFoundException);
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static final DeviceInfo getDefaultDeviceInfo(Context context) {
        DeviceInfo deviceInfo = new DeviceInfo(DeviceInfo.getDeviceId(context));
        (TelephonyManager)context.getSystemService("phone");
        deviceInfo.imei = DeviceInfo.getDeviceImei(context);
        Locale locale = Locale.getDefault();
        deviceInfo.locale = new Locale(locale.getLanguage(), locale.getCountry());
        deviceInfo.name = Settings.System.getString((ContentResolver)context.getContentResolver(), (String)"device_name");
        if (deviceInfo.name == null || deviceInfo.name.isEmpty()) {
            deviceInfo.name = Settings.Global.getString((ContentResolver)context.getContentResolver(), (String)"device_name");
        }
        if (deviceInfo.name == null || deviceInfo.name.isEmpty()) {
            deviceInfo.name = "SAMSUNG";
        }
        deviceInfo.androidId = DeviceInfo.getAndroidId(context);
        deviceInfo.serial = Build.SERIAL;
        deviceInfo.mac = BluetoothAdapter.getDefaultAdapter().getAddress();
        Timezone timezone = new Timezone();
        timezone.code = TimeZone.getDefault().getID();
        String string = DeviceInfo.getAutoTimeZone(context) ? "NETWORK" : "USER";
        timezone.setter = string;
        deviceInfo.timezone = timezone;
        return deviceInfo;
    }

    public static final String getDeviceBluetoothMac() {
        return BluetoothAdapter.getDefaultAdapter().getAddress();
    }

    public static final String getDeviceId(Context context) {
        return Base64.encodeToString((byte[])c.N(context).getBytes(), (int)10).replace('=', '0');
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final String getDeviceImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
        if (telephonyManager.getDeviceId() == null) return null;
        String string = telephonyManager.getDeviceId();
        if (string == null) return string;
        if (string.length() >= 15) return string;
        if (com.samsung.android.spayfw.e.b.a.fT()) return string;
        try {
            String string2 = a.getImei(0);
            return string2;
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            return string;
        }
    }

    public static final String getDeviceSerialNumber() {
        return Build.SERIAL;
    }

    public static final String getMsisdn(Context context) {
        String string;
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
        if (telephonyManager != null && telephonyManager.getSimSerialNumber() != null && !telephonyManager.getSimSerialNumber().isEmpty() && (string = telephonyManager.getLine1Number()) != null && string.length() > 3) {
            return string;
        }
        return null;
    }

    public static final String getNetworkOperatorName(Context context) {
        return ((TelephonyManager)context.getSystemService("phone")).getNetworkOperatorName();
    }

    public static final String getSimSerialNumber(Context context) {
        return ((TelephonyManager)context.getSystemService("phone")).getSimSerialNumber();
    }

    public String getAndroidId() {
        return this.androidId;
    }

    public String getBtMac() {
        return this.mac;
    }

    public String getImei() {
        return this.imei;
    }

    public String getSerial() {
        return this.serial;
    }

    public String toString() {
        return this.getId();
    }

    private static final class Timezone {
        String code;
        String setter;

        private Timezone() {
        }
    }

}

