package com.samsung.android.spayfw.remoteservice.tokenrequester.models;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Base64;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.samsung.android.spayfw.appinterface.CardIssuerApp;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.p001a.MultiSimManagerAdapter;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.p008e.SystemPropertiesWrapper;
import com.samsung.android.spayfw.p008e.p010b.Platformutils;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.p022e.SslUtils;
import com.samsung.android.spayfw.utils.GoogleApiHelper;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DeviceInfo extends Id {
    private static final int BUILD_VERSION_CODE_M = 23;
    private static final String DEVICE_TYPE_PHONE = "PHONE";
    private static final String DEVICE_TYPE_TABLET = "TABLET";
    private static final int FASTEST_LOCATION_UPDATE_TIME_INTERVAL = 5000;
    private static final int LOCATION_UPDATE_TIME_INTERVAL = 5000;
    private static final int MAX_WIFI_SCANS = 50;
    private static String TAG = null;
    private static final int WIFI_UPDATE_TIME_INTERVAL = 10000;
    private static Handler googleLocHandler;
    private static HandlerThread googleLocThread;
    private static FusedLocationProviderApi mFusedLocationProviderApi;
    private static GoogleApiClient mGoogleApiClient;
    private static Location mLocation;
    private static LocationListener mLocationListener;
    private static LocationRequest mLocationRequest;
    private static BroadcastReceiver mWifiScanReceiver;
    private static Location sLocation;
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
    private int score;
    private String serial;
    private SimInfo sim;
    private StorageInfo storage;
    private Timezone timezone;
    private String type;

    /* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo.1 */
    static class C05801 extends Handler {
        C05801(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            Log.m285d(DeviceInfo.TAG, "googleLocHandler handle message");
            synchronized (DeviceInfo.class) {
                if (DeviceInfo.mFusedLocationProviderApi == null) {
                    Log.m287i(DeviceInfo.TAG, "mFusedLocationProviderApi is null");
                } else if (DeviceInfo.mGoogleApiClient == null) {
                    Log.m285d(DeviceInfo.TAG, "mGoogleApiClient is null");
                } else {
                    try {
                        DeviceInfo.mFusedLocationProviderApi.requestLocationUpdates(DeviceInfo.mGoogleApiClient, DeviceInfo.mLocationRequest, DeviceInfo.mLocationListener);
                    } catch (Throwable e) {
                        Log.m284c(DeviceInfo.TAG, e.getMessage(), e);
                    }
                }
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo.2 */
    static class C05812 implements android.location.LocationListener {
        final /* synthetic */ LocationManager val$lm;

        C05812(LocationManager locationManager) {
            this.val$lm = locationManager;
        }

        public void onLocationChanged(Location location) {
            Log.m285d(DeviceInfo.TAG, "onLocationChanged");
            synchronized (DeviceInfo.class) {
                DeviceInfo.sLocation = new Location(location);
                this.val$lm.removeUpdates(this);
                Looper.myLooper().quitSafely();
            }
        }

        public void onProviderDisabled(String str) {
            Log.m285d(DeviceInfo.TAG, "onProviderDisabled - " + str);
        }

        public void onProviderEnabled(String str) {
            Log.m285d(DeviceInfo.TAG, "onProviderEnabled - " + str);
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
            Log.m285d(DeviceInfo.TAG, "onStatusChanged");
        }
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo.3 */
    static class C05823 extends Handler {
        final /* synthetic */ android.location.LocationListener val$listener;
        final /* synthetic */ LocationManager val$lm;

        C05823(Looper looper, LocationManager locationManager, android.location.LocationListener locationListener) {
            this.val$lm = locationManager;
            this.val$listener = locationListener;
            super(looper);
        }

        public void handleMessage(Message message) {
            this.val$lm.requestLocationUpdates("gps", 0, 0.0f, this.val$listener);
            this.val$lm.requestLocationUpdates("network", 0, 0.0f, this.val$listener);
            this.val$lm.requestLocationUpdates("passive", 0, 0.0f, this.val$listener);
        }
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo.4 */
    static class C05844 extends BroadcastReceiver {

        /* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo.4.1 */
        class C05831 implements Runnable {
            final /* synthetic */ Context val$context;

            C05831(Context context) {
                this.val$context = context;
            }

            public void run() {
                Log.m285d(DeviceInfo.TAG, "Run Wifi Scan Again");
                if (this.val$context != null) {
                    WifiManager wifiManager = (WifiManager) this.val$context.getSystemService("wifi");
                    if (wifiManager != null) {
                        wifiManager.startScan();
                    }
                }
            }
        }

        C05844() {
        }

        public void onReceive(Context context, Intent intent) {
            Log.m285d(DeviceInfo.TAG, "Intent : " + intent.getAction());
            PaymentFrameworkApp.az().postDelayed(new C05831(context), 10000);
        }
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo.5 */
    static class C05855 implements Comparator<Wifi> {
        C05855() {
        }

        public int compare(Wifi wifi, Wifi wifi2) {
            double parseDouble = Double.parseDouble(wifi.getDistance());
            double parseDouble2 = Double.parseDouble(wifi2.getDistance());
            if (parseDouble > parseDouble2) {
                return 1;
            }
            return parseDouble == parseDouble2 ? 0 : -1;
        }
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo.6 */
    static class C05866 implements android.location.LocationListener {
        final /* synthetic */ ConditionVariable val$cv;
        final /* synthetic */ Location val$result;

        C05866(Location location, ConditionVariable conditionVariable) {
            this.val$result = location;
            this.val$cv = conditionVariable;
        }

        public void onLocationChanged(Location location) {
            Log.m285d(DeviceInfo.TAG, "onLocationChanged");
            DeviceInfo.sLocation = new Location(location);
            this.val$result.setLatitude(location.getLatitude());
            this.val$result.setLongitude(location.getLongitude());
            this.val$result.setProvider(location.getProvider());
            this.val$cv.open();
        }

        public void onProviderDisabled(String str) {
            Log.m285d(DeviceInfo.TAG, "onProviderDisabled - " + str);
        }

        public void onProviderEnabled(String str) {
            Log.m285d(DeviceInfo.TAG, "onProviderEnabled - " + str);
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
            Log.m285d(DeviceInfo.TAG, "onStatusChanged");
        }
    }

    /* renamed from: com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo.7 */
    static class C05877 extends Handler {
        final /* synthetic */ android.location.LocationListener val$listener;
        final /* synthetic */ LocationManager val$lm;

        C05877(Looper looper, LocationManager locationManager, android.location.LocationListener locationListener) {
            this.val$lm = locationManager;
            this.val$listener = locationListener;
            super(looper);
        }

        public void handleMessage(Message message) {
            this.val$lm.requestLocationUpdates("gps", 0, 0.0f, this.val$listener);
            this.val$lm.requestLocationUpdates("network", 0, 0.0f, this.val$listener);
            this.val$lm.requestLocationUpdates("passive", 0, 0.0f, this.val$listener);
        }
    }

    private static final class Timezone {
        String code;
        String setter;

        private Timezone() {
        }
    }

    private static class googleLocationListener implements LocationListener {
        private googleLocationListener() {
        }

        public void onLocationChanged(Location location) {
            try {
                Log.m285d(DeviceInfo.TAG, "Location Listener - on location changed");
                synchronized (DeviceInfo.class) {
                    DeviceInfo.mLocation = new Location(location);
                    if (GoogleApiHelper.fH() != null && DeviceInfo.mGoogleApiClient == null) {
                        DeviceInfo.mGoogleApiClient = GoogleApiHelper.fH().fI();
                    }
                    if (DeviceInfo.mFusedLocationProviderApi == null || DeviceInfo.mGoogleApiClient == null) {
                        Log.m287i(DeviceInfo.TAG, "mFusedLocationProviderApi is null");
                    } else {
                        Log.m285d(DeviceInfo.TAG, "remove location updates");
                        DeviceInfo.mFusedLocationProviderApi.removeLocationUpdates(DeviceInfo.mGoogleApiClient, DeviceInfo.mLocationListener);
                    }
                }
            } catch (Exception e) {
                Log.m285d(DeviceInfo.TAG, "Exception in googleLocationListener");
            }
        }
    }

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

    public static final String getDeviceId(Context context) {
        return Base64.encodeToString(SslUtils.m1191N(context).getBytes(), 10).replace('=', LLVARUtil.EMPTY_STRING);
    }

    public static final String getDeviceSerialNumber() {
        return Build.SERIAL;
    }

    public static final String getDeviceImei(Context context) {
        String deviceId = ((TelephonyManager) context.getSystemService("phone")).getDeviceId();
        if (!(deviceId == null || deviceId.length() >= 15 || Platformutils.fT())) {
            try {
                deviceId = MultiSimManagerAdapter.getImei(0);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        return deviceId;
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
            Log.m284c(TAG, e.getMessage(), e);
            return false;
        }
    }

    public static final String getLocalIpAddress() {
        try {
            Enumeration networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                Enumeration inetAddresses = ((NetworkInterface) networkInterfaces.nextElement()).getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            Log.m286e("IP Address", e.toString());
        }
        return null;
    }

    public static final String getDeviceCountry() {
        return Utils.fP();
    }

    public static final DeviceInfo getDefaultDeviceInfo(Context context) {
        DeviceInfo deviceInfo = new DeviceInfo(getDeviceId(context));
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        deviceInfo.imei = getDeviceImei(context);
        deviceInfo.ip = getLocalIpAddress();
        Locale locale = Locale.getDefault();
        deviceInfo.locale = new Locale(locale.getLanguage(), locale.getCountry());
        deviceInfo.locale.setVariant(SystemPropertiesWrapper.get("persist.sys.localevar", null));
        deviceInfo.manufacturer = Build.MANUFACTURER;
        deviceInfo.model = Build.MODEL;
        deviceInfo.brand = Build.BRAND;
        deviceInfo.product = Build.PRODUCT;
        deviceInfo.name = System.getString(context.getContentResolver(), "device_name");
        if (deviceInfo.name == null || deviceInfo.name.isEmpty()) {
            deviceInfo.name = Global.getString(context.getContentResolver(), "device_name");
        }
        if (deviceInfo.name == null || deviceInfo.name.isEmpty()) {
            deviceInfo.name = CardIssuerApp.STORE_SAMSUNG;
        }
        deviceInfo.type = getDeviceScreenType(context);
        if (telephonyManager != null) {
            String line1Number = telephonyManager.getLine1Number();
            if (!(telephonyManager.getSimSerialNumber() == null || telephonyManager.getSimSerialNumber().isEmpty())) {
                deviceInfo.sim = new SimInfo(telephonyManager.getSimSerialNumber());
                deviceInfo.sim.setKey(telephonyManager.getSubscriberId());
                if (line1Number != null && line1Number.length() > 3) {
                    deviceInfo.msisdn = line1Number;
                }
            }
            String networkOperator = telephonyManager.getNetworkOperator();
            if (!(networkOperator == null || networkOperator.isEmpty())) {
                deviceInfo.network = new NetworkInfo(Utils.aj(context) ? "WIFI" : "MOBILE", telephonyManager.getNetworkOperatorName(), getMcc(networkOperator), getMnc(networkOperator));
            }
        }
        deviceInfo.androidId = getAndroidId(context);
        deviceInfo.os = new OsInfo(deviceInfo.androidId, VERSION.RELEASE, SystemPropertiesWrapper.get("ro.build.PDA"));
        deviceInfo.push = new PushProviders();
        deviceInfo.storage = new StorageInfo();
        deviceInfo.storage.setId(getDeviceId(context));
        Location lastKnownLocation = getLastKnownLocation(context);
        if (lastKnownLocation != null) {
            String str = "NETWORK";
            if (lastKnownLocation.getProvider() == null) {
                Log.m286e(TAG, "Unknown Location Provider : " + lastKnownLocation.getProvider());
            } else if (lastKnownLocation.getProvider().equals("gps")) {
                str = "GPS";
            } else if (lastKnownLocation.getProvider().equals("network")) {
                str = Utils.aj(context) ? "WIFI" : "NETWORK";
            } else {
                Log.m286e(TAG, "Unknown Location Provider : " + lastKnownLocation.getProvider());
            }
            deviceInfo.location = new Location(lastKnownLocation.getLatitude() + BuildConfig.FLAVOR, lastKnownLocation.getLongitude() + BuildConfig.FLAVOR, TimeZone.getDefault().getID(), str, lastKnownLocation.getAltitude() + BuildConfig.FLAVOR);
        }
        deviceInfo.serial = Build.SERIAL;
        deviceInfo.mac = BluetoothAdapter.getDefaultAdapter().getAddress();
        Timezone timezone = new Timezone();
        timezone.code = TimeZone.getDefault().getID();
        timezone.setter = getAutoTimeZone(context) ? "NETWORK" : "USER";
        deviceInfo.timezone = timezone;
        return deviceInfo;
    }

    public static synchronized void startGoogleLocationScan(Context context) {
        synchronized (DeviceInfo.class) {
            Log.m285d(TAG, "start Google Location Scan");
            try {
                if (isLocationEnabled(context)) {
                    if (GoogleApiHelper.fH() != null) {
                        mGoogleApiClient = GoogleApiHelper.fH().fI();
                    }
                    if (mGoogleApiClient == null) {
                        Log.m287i(TAG, "mGoogleApiClient is null");
                    } else {
                        Log.m285d(TAG, "mGoogleApiClient is not null");
                        setupLocationUpdateParameters();
                        if (googleLocThread == null) {
                            Log.m285d(TAG, "googleLocThread null");
                            googleLocThread = new HandlerThread("GetGoogleLocThread");
                            googleLocThread.start();
                        }
                        if (googleLocHandler == null) {
                            Log.m285d(TAG, "googleLocHandler null");
                            googleLocHandler = new C05801(googleLocThread.getLooper());
                        }
                        if (googleLocHandler != null) {
                            googleLocHandler.sendEmptyMessage(0);
                        }
                    }
                } else {
                    Log.m287i(TAG, "Location settings off");
                }
            } catch (Throwable e) {
                Log.m284c(TAG, e.getMessage(), e);
            }
        }
    }

    public static synchronized Location getGoogleLocation() {
        Location location;
        synchronized (DeviceInfo.class) {
            location = null;
            try {
                if (GoogleApiHelper.fH() != null && mGoogleApiClient == null) {
                    mGoogleApiClient = GoogleApiHelper.fH().fI();
                }
                if (mGoogleApiClient == null) {
                    Log.m287i(TAG, "mGoogleApiClient is null");
                } else {
                    Log.m285d(TAG, "mGoogleApiClient is not null");
                    location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                }
            } catch (Throwable e) {
                Log.m284c(TAG, e.getMessage(), e);
            }
        }
        return location;
    }

    public static boolean isLocationEnabled(Context context) {
        boolean z = true;
        if (context == null) {
            return false;
        }
        try {
            if (VERSION.SDK_INT >= 19) {
                if (Secure.getInt(context.getContentResolver(), "location_mode") == 0) {
                    z = false;
                }
            } else if (TextUtils.isEmpty(Secure.getString(context.getContentResolver(), "location_providers_allowed"))) {
                z = false;
            }
        } catch (Exception e) {
            Log.m286e(TAG, "Exception in isLocationEnabled");
            z = false;
        }
        return z;
    }

    public static synchronized Location getLastKnownLocation(Context context) {
        Location location;
        synchronized (DeviceInfo.class) {
            Log.m285d(TAG, "getLastKnownLocation: " + sLocation);
            location = sLocation;
        }
        return location;
    }

    public static void cacheLocation(Context context) {
        int i;
        Log.m285d(TAG, "cacheLocation");
        LocationManager locationManager = (LocationManager) context.getSystemService("location");
        try {
            i = Secure.getInt(context.getContentResolver(), "location_mode");
        } catch (Throwable e) {
            Log.m284c(TAG, e.getMessage(), e);
            i = 0;
        }
        Log.m285d(TAG, "Current Location Mode : " + i);
        if (i != 0) {
            android.location.LocationListener c05812 = new C05812(locationManager);
            HandlerThread handlerThread = new HandlerThread("GetLocThread");
            handlerThread.start();
            new C05823(handlerThread.getLooper(), locationManager, c05812).sendEmptyMessage(0);
        }
    }

    public static synchronized void startWifiScans(Context context) {
        synchronized (DeviceInfo.class) {
            Log.m285d(TAG, "startWifiScans");
            if (context == null) {
                Log.m285d(TAG, "context is null");
            } else {
                WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
                if (mWifiScanReceiver == null) {
                    Log.m285d(TAG, "Register New Wifi Scan Receiver");
                    mWifiScanReceiver = new C05844();
                    context.registerReceiver(mWifiScanReceiver, new IntentFilter("android.net.wifi.SCAN_RESULTS"));
                }
                if (wifiManager != null) {
                    wifiManager.startScan();
                }
            }
        }
    }

    public static synchronized void stopWifiScans(Context context) {
        synchronized (DeviceInfo.class) {
            Log.m285d(TAG, "stopWifiScans");
            if (mWifiScanReceiver != null) {
                Log.m285d(TAG, "Unregister Wifi Scan Receiver");
                context.unregisterReceiver(mWifiScanReceiver);
                mWifiScanReceiver = null;
            }
        }
    }

    public static synchronized ArrayList<Wifi> getWifiDetails(Context context) {
        ArrayList<Wifi> arrayList;
        synchronized (DeviceInfo.class) {
            if (context == null) {
                Log.m287i(TAG, "context is null");
                arrayList = null;
            } else {
                Wifi wifi;
                Object arrayList2 = new ArrayList();
                WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
                List scanResults;
                if (wifiManager != null) {
                    scanResults = wifiManager.getScanResults();
                } else {
                    scanResults = null;
                }
                if (r2 != null && r2.size() > 0) {
                    for (ScanResult scanResult : r2) {
                        if (scanResult != null) {
                            if (scanResult.level >= 0) {
                                Log.m285d(TAG, "Invalid RSSI");
                            } else {
                                wifi = new Wifi(scanResult.BSSID, scanResult.SSID, String.valueOf(scanResult.level), String.valueOf((SystemClock.elapsedRealtimeNanos() / 1000) - scanResult.timestamp), String.valueOf(scanResult.frequency), String.valueOf(Math.pow(10.0d, ((27.55d - (20.0d * Math.log10((double) scanResult.frequency))) + ((double) Math.abs(scanResult.level))) / 20.0d)));
                                if (VERSION.SDK_INT >= BUILD_VERSION_CODE_M) {
                                    wifi.setCenterFreq0(String.valueOf(scanResult.centerFreq0));
                                    wifi.setCenterFreq1(String.valueOf(scanResult.centerFreq1));
                                    wifi.setChannelWidth(String.valueOf(scanResult.channelWidth));
                                    if (scanResult.operatorFriendlyName != null) {
                                        wifi.setOperatorFriendlyName(scanResult.operatorFriendlyName.toString());
                                    }
                                    if (scanResult.venueName != null) {
                                        wifi.setVenueName(scanResult.venueName.toString());
                                    }
                                }
                                arrayList2.add(wifi);
                            }
                        }
                    }
                    Collections.sort(arrayList2, new C05855());
                }
                int i = 0;
                ArrayList<Wifi> arrayList3 = new ArrayList(MAX_WIFI_SCANS);
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    int i2;
                    wifi = (Wifi) it.next();
                    if (i < MAX_WIFI_SCANS) {
                        arrayList3.add(wifi);
                        i2 = i + 1;
                    } else {
                        i2 = i;
                    }
                    i = i2;
                }
                arrayList = arrayList3;
            }
        }
        return arrayList;
    }

    public static synchronized Location getLocation(Context context) {
        Location location;
        Location location2 = null;
        synchronized (DeviceInfo.class) {
            int i;
            Log.m285d(TAG, "getLocation");
            ConditionVariable conditionVariable = new ConditionVariable();
            LocationManager locationManager = (LocationManager) context.getSystemService("location");
            Location location3 = new Location("N/A");
            android.location.LocationListener c05866 = new C05866(location3, conditionVariable);
            HandlerThread handlerThread = new HandlerThread("GetLocThread");
            handlerThread.start();
            Handler c05877 = new C05877(handlerThread.getLooper(), locationManager, c05866);
            try {
                i = Secure.getInt(context.getContentResolver(), "location_mode");
            } catch (Throwable e) {
                Log.m284c(TAG, e.getMessage(), e);
                i = 0;
            }
            Log.m285d(TAG, "Current Location Mode : " + i);
            if (i == 0) {
                location = null;
            } else {
                c05877.sendEmptyMessage(0);
                if (conditionVariable.block(10000)) {
                    location2 = location3;
                }
                locationManager.removeUpdates(c05866);
                handlerThread.getLooper().quitSafely();
                location = location2;
            }
        }
        return location;
    }

    public static boolean isVpnConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        Network[] allNetworks = connectivityManager.getAllNetworks();
        if (allNetworks == null) {
            Log.m287i(TAG, "Networks are NULL");
            return false;
        }
        Log.m287i(TAG, "Network count: " + allNetworks.length);
        for (int i = 0; i < allNetworks.length; i++) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(allNetworks[i]);
            Log.m285d(TAG, "Network " + i + ": " + allNetworks[i].toString());
            if (networkCapabilities != null) {
                Log.m285d(TAG, "VPN transport is: " + networkCapabilities.hasTransport(4));
                Log.m285d(TAG, "NOT_VPN capability is: " + networkCapabilities.hasCapability(15));
                if (networkCapabilities.hasTransport(4)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isProxyEnabled(Context context) {
        String string = Global.getString(context.getContentResolver(), "http_proxy");
        Log.m287i(TAG, "proxy: " + string);
        if (string == null || string.isEmpty()) {
            return false;
        }
        return true;
    }

    public static String getMcc(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            String networkOperator = telephonyManager.getNetworkOperator();
            if (!(networkOperator == null || networkOperator.isEmpty())) {
                return getMcc(networkOperator);
            }
        }
        return BuildConfig.FLAVOR;
    }

    public static String getMnc(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService("phone");
        if (telephonyManager != null) {
            String networkOperator = telephonyManager.getNetworkOperator();
            if (!(networkOperator == null || networkOperator.isEmpty())) {
                return getMnc(networkOperator);
            }
        }
        return BuildConfig.FLAVOR;
    }

    private static final String getDeviceScreenType(Context context) {
        if (context.getResources().getConfiguration().smallestScreenWidthDp >= 600) {
            return DEVICE_TYPE_TABLET;
        }
        return DEVICE_TYPE_PHONE;
    }

    private static final String getMcc(String str) {
        String str2 = BuildConfig.FLAVOR;
        if (str == null || str.length() < 3) {
            return str2;
        }
        return str.substring(0, 3);
    }

    private static final String getMnc(String str) {
        String str2 = BuildConfig.FLAVOR;
        if (str == null || str.length() <= 3) {
            return str2;
        }
        return str.substring(3);
    }

    private static void setupLocationUpdateParameters() {
        Log.m285d(TAG, "Setting up location update parameters");
        try {
            if (mLocationRequest == null) {
                mLocationRequest = LocationRequest.create();
            }
            if (mLocationRequest != null) {
                mLocationRequest.setPriority(100);
                mLocationRequest.setInterval(5000);
                mLocationRequest.setFastestInterval(5000);
            }
            if (mFusedLocationProviderApi == null) {
                mFusedLocationProviderApi = LocationServices.FusedLocationApi;
            }
            if (mLocationListener == null) {
                mLocationListener = new googleLocationListener();
            }
        } catch (Exception e) {
            Log.m285d(TAG, "Exception in setupLocationUpdateParameters");
        }
    }

    private DeviceInfo(String str) {
        super(str);
        this.score = 3;
    }

    public void setCertificates(CertificateInfo[] certificateInfoArr) {
        this.certificates = certificateInfoArr;
    }

    public void setGcmId(String str) {
        this.push.setGcm(new Id(str));
    }

    public void setSppId(String str) {
        this.push.setSpp(new Id(str));
    }

    public void setParentId(String str) {
        if (str != null && !str.isEmpty()) {
            this.parent = new Id(str);
        }
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

    public SimInfo getSimInfo() {
        return this.sim;
    }
}
