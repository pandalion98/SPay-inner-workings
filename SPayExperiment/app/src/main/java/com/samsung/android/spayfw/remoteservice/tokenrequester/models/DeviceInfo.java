/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.bluetooth.BluetoothAdapter
 *  android.content.BroadcastReceiver
 *  android.content.ContentResolver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.location.Location
 *  android.location.LocationListener
 *  android.location.LocationManager
 *  android.net.ConnectivityManager
 *  android.net.Network
 *  android.net.NetworkCapabilities
 *  android.net.wifi.ScanResult
 *  android.net.wifi.WifiManager
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Bundle
 *  android.os.ConditionVariable
 *  android.os.Handler
 *  android.os.HandlerThread
 *  android.os.Looper
 *  android.os.Message
 *  android.os.SystemClock
 *  android.provider.Settings
 *  android.provider.Settings$Global
 *  android.provider.Settings$Secure
 *  android.provider.Settings$SettingNotFoundException
 *  android.provider.Settings$System
 *  android.telephony.TelephonyManager
 *  android.text.TextUtils
 *  android.util.Base64
 *  com.google.android.gms.common.api.GoogleApiClient
 *  com.google.android.gms.common.api.PendingResult
 *  com.google.android.gms.location.FusedLocationProviderApi
 *  com.google.android.gms.location.LocationListener
 *  com.google.android.gms.location.LocationRequest
 *  com.google.android.gms.location.LocationServices
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Double
 *  java.lang.Exception
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.Throwable
 *  java.net.InetAddress
 *  java.net.NetworkInterface
 *  java.util.ArrayList
 *  java.util.Collections
 *  java.util.Comparator
 *  java.util.Enumeration
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Locale
 *  java.util.TimeZone
 */
package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.samsung.android.spayfw.a.a;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.e.d;
import com.samsung.android.spayfw.remoteservice.e.c;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.h;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class DeviceInfo
extends Id {
    private static final int BUILD_VERSION_CODE_M = 23;
    private static final String DEVICE_TYPE_PHONE = "PHONE";
    private static final String DEVICE_TYPE_TABLET = "TABLET";
    private static final int FASTEST_LOCATION_UPDATE_TIME_INTERVAL = 5000;
    private static final int LOCATION_UPDATE_TIME_INTERVAL = 5000;
    private static final int MAX_WIFI_SCANS = 50;
    private static String TAG;
    private static final int WIFI_UPDATE_TIME_INTERVAL = 10000;
    private static Handler googleLocHandler;
    private static HandlerThread googleLocThread;
    private static FusedLocationProviderApi mFusedLocationProviderApi;
    private static GoogleApiClient mGoogleApiClient;
    private static android.location.Location mLocation;
    private static com.google.android.gms.location.LocationListener mLocationListener;
    private static LocationRequest mLocationRequest;
    private static BroadcastReceiver mWifiScanReceiver;
    private static android.location.Location sLocation;
    private String androidId;
    private String brand;
    private CertificateInfo[] certificates;
    private String imei;
    private String ip;
    private Locale locale;
    private Location location;
    private String mac;
    private String manufacturer;
    private String model;
    private String msisdn;
    private String name;
    private NetworkInfo network;
    private OsInfo os;
    private Id parent;
    private String product;
    private PushProviders push;
    private int score = 3;
    private String serial;
    private SimInfo sim;
    private StorageInfo storage;
    private Timezone timezone;
    private String type;

    static {
        mLocationListener = null;
        mFusedLocationProviderApi = null;
        mLocationRequest = null;
        mLocation = null;
        mGoogleApiClient = null;
        googleLocThread = null;
        googleLocHandler = null;
        mWifiScanReceiver = null;
        TAG = "DeviceInfo";
    }

    private DeviceInfo(String string) {
        super(string);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void cacheLocation(Context context) {
        int n2;
        Log.d(TAG, "cacheLocation");
        final LocationManager locationManager = (LocationManager)context.getSystemService("location");
        try {
            int n3;
            n2 = n3 = Settings.Secure.getInt((ContentResolver)context.getContentResolver(), (String)"location_mode");
        }
        catch (Settings.SettingNotFoundException settingNotFoundException) {
            Log.c(TAG, settingNotFoundException.getMessage(), settingNotFoundException);
            n2 = 0;
        }
        Log.d(TAG, "Current Location Mode : " + n2);
        if (n2 == 0) {
            return;
        }
        final LocationListener locationListener = new LocationListener(){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            public void onLocationChanged(android.location.Location location) {
                Log.d(TAG, "onLocationChanged");
                Class<DeviceInfo> class_ = DeviceInfo.class;
                synchronized (DeviceInfo.class) {
                    sLocation = new android.location.Location(location);
                    locationManager.removeUpdates((LocationListener)this);
                    Looper.myLooper().quitSafely();
                    // ** MonitorExit[var4_2] (shouldn't be in output)
                    return;
                }
            }

            public void onProviderDisabled(String string) {
                Log.d(TAG, "onProviderDisabled - " + string);
            }

            public void onProviderEnabled(String string) {
                Log.d(TAG, "onProviderEnabled - " + string);
            }

            public void onStatusChanged(String string, int n2, Bundle bundle) {
                Log.d(TAG, "onStatusChanged");
            }
        };
        HandlerThread handlerThread = new HandlerThread("GetLocThread");
        handlerThread.start();
        new Handler(handlerThread.getLooper()){

            public void handleMessage(Message message) {
                locationManager.requestLocationUpdates("gps", 0L, 0.0f, locationListener);
                locationManager.requestLocationUpdates("network", 0L, 0.0f, locationListener);
                locationManager.requestLocationUpdates("passive", 0L, 0.0f, locationListener);
            }
        }.sendEmptyMessage(0);
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
            Log.c(TAG, settingNotFoundException.getMessage(), settingNotFoundException);
            return false;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static final DeviceInfo getDefaultDeviceInfo(Context context) {
        DeviceInfo deviceInfo = new DeviceInfo(DeviceInfo.getDeviceId(context));
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
        deviceInfo.imei = DeviceInfo.getDeviceImei(context);
        deviceInfo.ip = DeviceInfo.getLocalIpAddress();
        java.util.Locale locale = java.util.Locale.getDefault();
        deviceInfo.locale = new Locale(locale.getLanguage(), locale.getCountry());
        deviceInfo.locale.setVariant(d.get("persist.sys.localevar", null));
        deviceInfo.manufacturer = Build.MANUFACTURER;
        deviceInfo.model = Build.MODEL;
        deviceInfo.brand = Build.BRAND;
        deviceInfo.product = Build.PRODUCT;
        deviceInfo.name = Settings.System.getString((ContentResolver)context.getContentResolver(), (String)"device_name");
        if (deviceInfo.name == null || deviceInfo.name.isEmpty()) {
            deviceInfo.name = Settings.Global.getString((ContentResolver)context.getContentResolver(), (String)"device_name");
        }
        if (deviceInfo.name == null || deviceInfo.name.isEmpty()) {
            deviceInfo.name = "SAMSUNG";
        }
        deviceInfo.type = DeviceInfo.getDeviceScreenType(context);
        if (telephonyManager != null) {
            String string;
            String string2 = telephonyManager.getLine1Number();
            if (telephonyManager.getSimSerialNumber() != null && !telephonyManager.getSimSerialNumber().isEmpty()) {
                deviceInfo.sim = new SimInfo(telephonyManager.getSimSerialNumber());
                deviceInfo.sim.setKey(telephonyManager.getSubscriberId());
                if (string2 != null && string2.length() > 3) {
                    deviceInfo.msisdn = string2;
                }
            }
            if ((string = telephonyManager.getNetworkOperator()) != null && !string.isEmpty()) {
                String string3 = h.aj(context) ? "WIFI" : "MOBILE";
                deviceInfo.network = new NetworkInfo(string3, telephonyManager.getNetworkOperatorName(), DeviceInfo.getMcc(string), DeviceInfo.getMnc(string));
            }
        }
        deviceInfo.androidId = DeviceInfo.getAndroidId(context);
        deviceInfo.os = new OsInfo(deviceInfo.androidId, Build.VERSION.RELEASE, d.get("ro.build.PDA"));
        deviceInfo.push = new PushProviders();
        deviceInfo.storage = new StorageInfo();
        deviceInfo.storage.setId(DeviceInfo.getDeviceId(context));
        android.location.Location location = DeviceInfo.getLastKnownLocation(context);
        if (location != null) {
            String string = "NETWORK";
            if (location.getProvider() != null) {
                if (location.getProvider().equals((Object)"gps")) {
                    string = "GPS";
                } else if (location.getProvider().equals((Object)"network")) {
                    String string4 = h.aj(context) ? "WIFI" : "NETWORK";
                    string = string4;
                } else {
                    Log.e(TAG, "Unknown Location Provider : " + location.getProvider());
                }
            } else {
                Log.e(TAG, "Unknown Location Provider : " + location.getProvider());
            }
            deviceInfo.location = new Location(location.getLatitude() + "", location.getLongitude() + "", TimeZone.getDefault().getID(), string, location.getAltitude() + "");
        }
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

    public static final String getDeviceCountry() {
        return h.fP();
    }

    public static final String getDeviceId(Context context) {
        return Base64.encodeToString((byte[])c.N(context).getBytes(), (int)10).replace('=', '0');
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static final String getDeviceImei(Context context) {
        String string = ((TelephonyManager)context.getSystemService("phone")).getDeviceId();
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

    private static final String getDeviceScreenType(Context context) {
        if (context.getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            return DEVICE_TYPE_TABLET;
        }
        return DEVICE_TYPE_PHONE;
    }

    public static final String getDeviceSerialNumber() {
        return Build.SERIAL;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static android.location.Location getGoogleLocation() {
        Class<DeviceInfo> class_ = DeviceInfo.class;
        synchronized (DeviceInfo.class) {
            block7 : {
                android.location.Location location = null;
                if (com.samsung.android.spayfw.utils.d.fH() != null && mGoogleApiClient == null) {
                    mGoogleApiClient = com.samsung.android.spayfw.utils.d.fH().fI();
                }
                if (mGoogleApiClient != null) break block7;
                Log.i(TAG, "mGoogleApiClient is null");
                // ** MonitorExit[var4] (shouldn't be in output)
                return location;
            }
            try {
                Log.d(TAG, "mGoogleApiClient is not null");
                android.location.Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                return location;
            }
            catch (Exception exception) {
                Log.c(TAG, exception.getMessage(), exception);
                return null;
            }
        }
    }

    public static android.location.Location getLastKnownLocation(Context context) {
        Class<DeviceInfo> class_ = DeviceInfo.class;
        synchronized (DeviceInfo.class) {
            Log.d(TAG, "getLastKnownLocation: " + (Object)sLocation);
            android.location.Location location = sLocation;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return location;
        }
    }

    public static final String getLocalIpAddress() {
        try {
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                Enumeration enumeration2 = ((NetworkInterface)enumeration.nextElement()).getInetAddresses();
                while (enumeration2.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress)enumeration2.nextElement();
                    if (inetAddress.isLoopbackAddress() || inetAddress.isLinkLocalAddress()) continue;
                    String string = inetAddress.getHostAddress();
                    return string;
                }
            }
        }
        catch (Exception exception) {
            Log.e("IP Address", exception.toString());
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static android.location.Location getLocation(Context context) {
        Class<DeviceInfo> class_ = DeviceInfo.class;
        synchronized (DeviceInfo.class) {
            int n2;
            Log.d(TAG, "getLocation");
            final ConditionVariable conditionVariable = new ConditionVariable();
            final LocationManager locationManager = (LocationManager)context.getSystemService("location");
            final android.location.Location location = new android.location.Location("N/A");
            final LocationListener locationListener = new LocationListener(){

                public void onLocationChanged(android.location.Location location2) {
                    Log.d(TAG, "onLocationChanged");
                    sLocation = new android.location.Location(location2);
                    location.setLatitude(location2.getLatitude());
                    location.setLongitude(location2.getLongitude());
                    location.setProvider(location2.getProvider());
                    conditionVariable.open();
                }

                public void onProviderDisabled(String string) {
                    Log.d(TAG, "onProviderDisabled - " + string);
                }

                public void onProviderEnabled(String string) {
                    Log.d(TAG, "onProviderEnabled - " + string);
                }

                public void onStatusChanged(String string, int n2, Bundle bundle) {
                    Log.d(TAG, "onStatusChanged");
                }
            };
            HandlerThread handlerThread = new HandlerThread("GetLocThread");
            handlerThread.start();
            Handler handler = new Handler(handlerThread.getLooper()){

                public void handleMessage(Message message) {
                    locationManager.requestLocationUpdates("gps", 0L, 0.0f, locationListener);
                    locationManager.requestLocationUpdates("network", 0L, 0.0f, locationListener);
                    locationManager.requestLocationUpdates("passive", 0L, 0.0f, locationListener);
                }
            };
            try {
                int n3;
                n2 = n3 = Settings.Secure.getInt((ContentResolver)context.getContentResolver(), (String)"location_mode");
            }
            catch (Settings.SettingNotFoundException settingNotFoundException) {
                Log.c(TAG, settingNotFoundException.getMessage(), settingNotFoundException);
                n2 = 0;
            }
            Log.d(TAG, "Current Location Mode : " + n2);
            if (n2 == 0) {
                return null;
            }
            handler.sendEmptyMessage(0);
            boolean bl = conditionVariable.block(10000L);
            android.location.Location location2 = null;
            if (bl) {
                location2 = location;
            }
            locationManager.removeUpdates(locationListener);
            handlerThread.getLooper().quitSafely();
            return location2;
        }
    }

    public static String getMcc(Context context) {
        String string;
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
        if (telephonyManager != null && (string = telephonyManager.getNetworkOperator()) != null && !string.isEmpty()) {
            return DeviceInfo.getMcc(string);
        }
        return "";
    }

    private static final String getMcc(String string) {
        String string2 = "";
        if (string != null && string.length() >= 3) {
            string2 = string.substring(0, 3);
        }
        return string2;
    }

    public static String getMnc(Context context) {
        String string;
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService("phone");
        if (telephonyManager != null && (string = telephonyManager.getNetworkOperator()) != null && !string.isEmpty()) {
            return DeviceInfo.getMnc(string);
        }
        return "";
    }

    private static final String getMnc(String string) {
        String string2 = "";
        if (string != null && string.length() > 3) {
            string2 = string.substring(3);
        }
        return string2;
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

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static ArrayList<Wifi> getWifiDetails(Context context) {
        Class<DeviceInfo> class_ = DeviceInfo.class;
        synchronized (DeviceInfo.class) {
            if (context == null) {
                Log.i(TAG, "context is null");
                return null;
            }
            ArrayList arrayList = new ArrayList();
            WifiManager wifiManager = (WifiManager)context.getSystemService("wifi");
            List list = wifiManager != null ? wifiManager.getScanResults() : null;
            if (list != null && list.size() > 0) {
                for (ScanResult scanResult : list) {
                    if (scanResult == null) continue;
                    if (scanResult.level >= 0) {
                        Log.d(TAG, "Invalid RSSI");
                        continue;
                    }
                    String string = String.valueOf((double)Math.pow((double)10.0, (double)((27.55 - 20.0 * Math.log10((double)scanResult.frequency) + (double)Math.abs((int)scanResult.level)) / 20.0)));
                    Wifi wifi = new Wifi(scanResult.BSSID, scanResult.SSID, String.valueOf((int)scanResult.level), String.valueOf((long)(SystemClock.elapsedRealtimeNanos() / 1000L - scanResult.timestamp)), String.valueOf((int)scanResult.frequency), string);
                    if (Build.VERSION.SDK_INT >= 23) {
                        wifi.setCenterFreq0(String.valueOf((int)scanResult.centerFreq0));
                        wifi.setCenterFreq1(String.valueOf((int)scanResult.centerFreq1));
                        wifi.setChannelWidth(String.valueOf((int)scanResult.channelWidth));
                        if (scanResult.operatorFriendlyName != null) {
                            wifi.setOperatorFriendlyName(scanResult.operatorFriendlyName.toString());
                        }
                        if (scanResult.venueName != null) {
                            wifi.setVenueName(scanResult.venueName.toString());
                        }
                    }
                    arrayList.add((Object)wifi);
                }
                Collections.sort((List)arrayList, (Comparator)new Comparator<Wifi>(){

                    public int compare(Wifi wifi, Wifi wifi2) {
                        double d2;
                        double d3 = Double.parseDouble((String)wifi.getDistance());
                        if (d3 > (d2 = Double.parseDouble((String)wifi2.getDistance()))) {
                            return 1;
                        }
                        if (d3 == d2) {
                            return 0;
                        }
                        return -1;
                    }
                });
            }
            int n2 = 0;
            ArrayList arrayList2 = new ArrayList(50);
            Iterator iterator = arrayList.iterator();
            while (iterator.hasNext()) {
                int n3;
                Wifi wifi = (Wifi)iterator.next();
                if (n2 < 50) {
                    arrayList2.add((Object)wifi);
                    n3 = n2 + 1;
                } else {
                    n3 = n2;
                }
                n2 = n3;
            }
            return arrayList2;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static boolean isLocationEnabled(Context context) {
        boolean bl = true;
        if (context == null) {
            return false;
        }
        try {
            if (Build.VERSION.SDK_INT >= 19) {
                if (Settings.Secure.getInt((ContentResolver)context.getContentResolver(), (String)"location_mode") == 0) return false;
                return bl;
            }
            boolean bl2 = TextUtils.isEmpty((CharSequence)Settings.Secure.getString((ContentResolver)context.getContentResolver(), (String)"location_providers_allowed"));
            if (!bl2) return bl;
            return false;
        }
        catch (Exception exception) {
            Log.e(TAG, "Exception in isLocationEnabled");
            return false;
        }
    }

    public static boolean isProxyEnabled(Context context) {
        String string = Settings.Global.getString((ContentResolver)context.getContentResolver(), (String)"http_proxy");
        Log.i(TAG, "proxy: " + string);
        return string != null && !string.isEmpty();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isVpnConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        Network[] arrnetwork = connectivityManager.getAllNetworks();
        if (arrnetwork == null) {
            Log.i(TAG, "Networks are NULL");
            return false;
        }
        Log.i(TAG, "Network count: " + arrnetwork.length);
        int n2 = 0;
        while (n2 < arrnetwork.length) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(arrnetwork[n2]);
            Log.d(TAG, "Network " + n2 + ": " + arrnetwork[n2].toString());
            if (networkCapabilities != null) {
                Log.d(TAG, "VPN transport is: " + networkCapabilities.hasTransport(4));
                Log.d(TAG, "NOT_VPN capability is: " + networkCapabilities.hasCapability(15));
                if (networkCapabilities.hasTransport(4)) {
                    return true;
                }
            }
            ++n2;
        }
        return false;
    }

    private static void setupLocationUpdateParameters() {
        Log.d(TAG, "Setting up location update parameters");
        try {
            if (mLocationRequest == null) {
                mLocationRequest = LocationRequest.create();
            }
            if (mLocationRequest != null) {
                mLocationRequest.setPriority(100);
                mLocationRequest.setInterval(5000L);
                mLocationRequest.setFastestInterval(5000L);
            }
            if (mFusedLocationProviderApi == null) {
                mFusedLocationProviderApi = LocationServices.FusedLocationApi;
            }
            if (mLocationListener == null) {
                mLocationListener = new googleLocationListener();
            }
            return;
        }
        catch (Exception exception) {
            Log.d(TAG, "Exception in setupLocationUpdateParameters");
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void startGoogleLocationScan(Context context) {
        Class<DeviceInfo> class_ = DeviceInfo.class;
        synchronized (DeviceInfo.class) {
            Log.d(TAG, "start Google Location Scan");
            try {
                if (!DeviceInfo.isLocationEnabled(context)) {
                    Log.i(TAG, "Location settings off");
                } else {
                    if (com.samsung.android.spayfw.utils.d.fH() != null) {
                        mGoogleApiClient = com.samsung.android.spayfw.utils.d.fH().fI();
                    }
                    if (mGoogleApiClient == null) {
                        Log.i(TAG, "mGoogleApiClient is null");
                    }
                    Log.d(TAG, "mGoogleApiClient is not null");
                    DeviceInfo.setupLocationUpdateParameters();
                    if (googleLocThread == null) {
                        Log.d(TAG, "googleLocThread null");
                        googleLocThread = new HandlerThread("GetGoogleLocThread");
                        googleLocThread.start();
                    }
                    if (googleLocHandler == null) {
                        Log.d(TAG, "googleLocHandler null");
                        googleLocHandler = new Handler(googleLocThread.getLooper()){

                            /*
                             * Enabled aggressive block sorting
                             * Enabled unnecessary exception pruning
                             * Enabled aggressive exception aggregation
                             */
                            public void handleMessage(Message message) {
                                Log.d(TAG, "googleLocHandler handle message");
                                Class<DeviceInfo> class_ = DeviceInfo.class;
                                synchronized (DeviceInfo.class) {
                                    if (mFusedLocationProviderApi != null) {
                                        if (mGoogleApiClient == null) {
                                            Log.d(TAG, "mGoogleApiClient is null");
                                        } else {
                                            try {
                                                mFusedLocationProviderApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, mLocationListener);
                                            }
                                            catch (Exception exception) {
                                                Log.c(TAG, exception.getMessage(), exception);
                                            }
                                        }
                                    } else {
                                        Log.i(TAG, "mFusedLocationProviderApi is null");
                                    }
                                    // ** MonitorExit[var5_2] (shouldn't be in output)
                                    return;
                                }
                            }
                        };
                    }
                    if (googleLocHandler != null) {
                        googleLocHandler.sendEmptyMessage(0);
                    }
                }
            }
            catch (Exception exception) {
                Log.c(TAG, exception.getMessage(), exception);
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void startWifiScans(Context context) {
        Class<DeviceInfo> class_ = DeviceInfo.class;
        synchronized (DeviceInfo.class) {
            Log.d(TAG, "startWifiScans");
            if (context == null) {
                Log.d(TAG, "context is null");
            } else {
                WifiManager wifiManager = (WifiManager)context.getSystemService("wifi");
                if (mWifiScanReceiver == null) {
                    Log.d(TAG, "Register New Wifi Scan Receiver");
                    mWifiScanReceiver = new BroadcastReceiver(){

                        public void onReceive(final Context context, Intent intent) {
                            Log.d(TAG, "Intent : " + intent.getAction());
                            PaymentFrameworkApp.az().postDelayed(new Runnable(){

                                public void run() {
                                    WifiManager wifiManager;
                                    Log.d(TAG, "Run Wifi Scan Again");
                                    if (context != null && (wifiManager = (WifiManager)context.getSystemService("wifi")) != null) {
                                        wifiManager.startScan();
                                    }
                                }
                            }, 10000L);
                        }

                    };
                    context.registerReceiver(mWifiScanReceiver, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
                }
                if (wifiManager != null) {
                    wifiManager.startScan();
                }
            }
            // ** MonitorExit[var5_1] (shouldn't be in output)
            return;
        }
    }

    public static void stopWifiScans(Context context) {
        Class<DeviceInfo> class_ = DeviceInfo.class;
        synchronized (DeviceInfo.class) {
            Log.d(TAG, "stopWifiScans");
            if (mWifiScanReceiver != null) {
                Log.d(TAG, "Unregister Wifi Scan Receiver");
                context.unregisterReceiver(mWifiScanReceiver);
                mWifiScanReceiver = null;
            }
            // ** MonitorExit[var2_1] (shouldn't be in output)
            return;
        }
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

    public SimInfo getSimInfo() {
        return this.sim;
    }

    public void setCertificates(CertificateInfo[] arrcertificateInfo) {
        this.certificates = arrcertificateInfo;
    }

    public void setGcmId(String string) {
        this.push.setGcm(new Id(string));
    }

    public void setParentId(String string) {
        if (string != null && !string.isEmpty()) {
            this.parent = new Id(string);
        }
    }

    public void setSppId(String string) {
        this.push.setSpp(new Id(string));
    }

    private static final class Timezone {
        String code;
        String setter;

        private Timezone() {
        }
    }

    private static class googleLocationListener
    implements com.google.android.gms.location.LocationListener {
        private googleLocationListener() {
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        public void onLocationChanged(android.location.Location location) {
            try {
                Log.d(TAG, "Location Listener - on location changed");
                Class<DeviceInfo> class_ = DeviceInfo.class;
                // MONITORENTER : com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo.class
            }
            catch (Exception exception) {
                Log.d(TAG, "Exception in googleLocationListener");
                return;
            }
            mLocation = new android.location.Location(location);
            if (com.samsung.android.spayfw.utils.d.fH() != null && mGoogleApiClient == null) {
                mGoogleApiClient = com.samsung.android.spayfw.utils.d.fH().fI();
            }
            if (mFusedLocationProviderApi != null && mGoogleApiClient != null) {
                Log.d(TAG, "remove location updates");
                mFusedLocationProviderApi.removeLocationUpdates(mGoogleApiClient, mLocationListener);
                // MONITOREXIT : class_
                return;
            }
            Log.i(TAG, "mFusedLocationProviderApi is null");
        }
    }

}

