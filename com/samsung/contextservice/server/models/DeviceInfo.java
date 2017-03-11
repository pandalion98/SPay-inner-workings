package com.samsung.contextservice.server.models;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.os.Build;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.util.Base64;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.samsung.android.spayfw.appinterface.CardIssuerApp;
import com.samsung.android.spayfw.p001a.MultiSimManagerAdapter;
import com.samsung.android.spayfw.p008e.p010b.Platformutils;
import com.samsung.android.spayfw.remoteservice.p022e.SslUtils;
import com.samsung.contextservice.p029b.CSlog;
import java.util.Locale;
import java.util.TimeZone;

public class DeviceInfo extends Id {
    private static final String TAG = "DeviceInfo";
    private String androidId;
    private String imei;
    private Locale locale;
    private String mac;
    private String name;
    private String serial;
    private Timezone timezone;

    private static final class Timezone {
        String code;
        String setter;

        private Timezone() {
        }
    }

    public static final String getDeviceId(Context context) {
        return Base64.encodeToString(SslUtils.m1191N(context).getBytes(), 10).replace('=', LLVARUtil.EMPTY_STRING);
    }

    public static final String getDeviceSerialNumber() {
        return Build.SERIAL;
    }

    public static final String getDeviceImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager.getDeviceId() == null) {
            return null;
        }
        String deviceId = telephonyManager.getDeviceId();
        if (deviceId == null || deviceId.length() >= 15 || Platformutils.fT()) {
            return deviceId;
        }
        try {
            return MultiSimManagerAdapter.getImei(0);
        } catch (Throwable th) {
            th.printStackTrace();
            return deviceId;
        }
    }

    public static final String getMsisdn(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (!(telephonyManager == null || telephonyManager.getSimSerialNumber() == null || telephonyManager.getSimSerialNumber().isEmpty())) {
            String line1Number = telephonyManager.getLine1Number();
            if (line1Number != null && line1Number.length() > 3) {
                return line1Number;
            }
        }
        return null;
    }

    public static final String getDeviceBluetoothMac() {
        return BluetoothAdapter.getDefaultAdapter().getAddress();
    }

    public static final String getAndroidId(Context context) {
        return Secure.getString(context.getContentResolver(), "android_id");
    }

    public static final String getSimSerialNumber(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getSimSerialNumber();
    }

    public static final String getNetworkOperatorName(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getNetworkOperatorName();
    }

    public static final boolean getAutoTimeZone(Context context) {
        try {
            if (Global.getInt(context.getContentResolver(), "auto_time_zone") == 1) {
                return true;
            }
            return false;
        } catch (Throwable e) {
            CSlog.m1406c(TAG, e.getMessage(), e);
            return false;
        }
    }

    public static final DeviceInfo getDefaultDeviceInfo(Context context) {
        DeviceInfo deviceInfo = new DeviceInfo(getDeviceId(context));
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        deviceInfo.imei = getDeviceImei(context);
        Locale locale = Locale.getDefault();
        deviceInfo.locale = new Locale(locale.getLanguage(), locale.getCountry());
        deviceInfo.name = System.getString(context.getContentResolver(), "device_name");
        if (deviceInfo.name == null || deviceInfo.name.isEmpty()) {
            deviceInfo.name = Global.getString(context.getContentResolver(), "device_name");
        }
        if (deviceInfo.name == null || deviceInfo.name.isEmpty()) {
            deviceInfo.name = CardIssuerApp.STORE_SAMSUNG;
        }
        deviceInfo.androidId = getAndroidId(context);
        deviceInfo.serial = Build.SERIAL;
        deviceInfo.mac = BluetoothAdapter.getDefaultAdapter().getAddress();
        Timezone timezone = new Timezone();
        timezone.code = TimeZone.getDefault().getID();
        timezone.setter = getAutoTimeZone(context) ? "NETWORK" : "USER";
        deviceInfo.timezone = timezone;
        return deviceInfo;
    }

    private DeviceInfo(String str) {
        super(str);
    }

    public String getSerial() {
        return this.serial;
    }

    public String getImei() {
        return this.imei;
    }

    public String getBtMac() {
        return this.mac;
    }

    public String getAndroidId() {
        return this.androidId;
    }

    public String toString() {
        return getId();
    }
}
