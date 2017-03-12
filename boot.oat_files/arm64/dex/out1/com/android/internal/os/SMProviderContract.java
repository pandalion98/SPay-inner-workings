package com.android.internal.os;

import android.net.Uri;

public final class SMProviderContract {
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final Uri CONTENT_URI_ALL = Uri.parse("content://com.sec.smartmanager.provider/*");
    static final Uri CONTENT_URI_BATTEY_DELTA = Uri.parse("content://com.sec.smartmanager.provider/Battery_delta");
    static final Uri CONTENT_URI_PCONSUMING_PACKAGE = Uri.parse("content://com.sec.smartmanager.provider/power_consuming_packages");
    public static final String KEY_BATTERY_DELTA = "batterydelta";
    public static final String KEY_BATTERY_PERC = "batterypercent";
    public static final String KEY_LCD = "lcd_condition";
    public static final String KEY_NETWORK_USAGE = "network_usage";
    public static final String KEY_OFFPOWER = "offpower";
    public static final String KEY_PACKAGE_NAME = "packageName";
    public static final String KEY_POWER = "power";
    public static final String KEY_SCREEN_USAGE = "screen_usage";
    public static final String KEY_TIME = "time";
    public static final String KEY_TOTALPOWER = "totalpower";
    public static final String KEY_USAGE_TIME = "usage_time";
    public static final String PROVIDER_NAME = "com.sec.smartmanager.provider";
    public static final String URL = "content://com.sec.smartmanager.provider";
}
