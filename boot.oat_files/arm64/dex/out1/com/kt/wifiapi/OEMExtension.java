package com.kt.wifiapi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiSsid;
import android.os.Bundle;
import android.os.Debug;
import android.os.Message;
import android.provider.Settings$System;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.InputDevice;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class OEMExtension {
    private static final String BLE_STR = "ble=";
    private static final String BSSID_STR = "bssid=";
    static final boolean DBG;
    private static final String DELIMITER_STR = "====";
    private static final String END_STR = "####";
    public static final int FEATURE_KT_WIFIAPI_OEM_DISCONNECTION_PRIORITY = 2;
    public static final int FEATURE_KT_WIFIAPI_OEM_EAP_AKA_NOTIFICATON = 16;
    public static final int FEATURE_KT_WIFIAPI_OEM_EAP_NOTIFICATON = 8;
    public static final int FEATURE_KT_WIFIAPI_OEM_ERROR_NOTIFICATON = 4;
    public static final int FEATURE_KT_WIFIAPI_OEM_MANUAL_CONNECTION = 1;
    public static final int FEATURE_KT_WIFIAPI_OEM_SCAN_RESULT_EXTENSION = 32;
    private static final String FLAGS_STR = "flags=";
    private static final String FREQ_STR = "freq=";
    private static final String ID_STR = "id=";
    private static final String LEVEL_STR = "level=";
    private static final int SCAN_RESULT_CACHE_SIZE = 80;
    private static final String SSID_STR = "ssid=";
    private static final String TAG = "OEMExtension";
    private static final String VSI_STR = "vsi=";
    public static final String WIFI_SKIP_AUTO_CONNECTION = "wifi_skip_auto_conn";
    private static OEMExtension instance = null;
    private static boolean isFirstGetManualConnection = true;
    private static Context mContext;
    private static boolean mManualConnection = false;
    private static final Pattern scanResultPattern = Pattern.compile("\t+");
    private List<GWSScanResult> mGWSScanResults;
    private final LruCache<String, GWSScanResult> mScanResultCache = new LruCache(80);
    private WifiManager mWifiManager = ((WifiManager) mContext.getSystemService(Settings$System.RADIO_WIFI));

    static {
        boolean z;
        if (Debug.isProductShip() == 1) {
            z = false;
        } else {
            z = true;
        }
        DBG = z;
    }

    private OEMExtension(Context context) {
        mContext = context;
    }

    public static OEMExtension getInstance(Context context) {
        if (DBG) {
            Log.d(TAG, "OEMExtension::getInstance() " + instance);
        }
        if (instance == null) {
            instance = new OEMExtension(context);
        }
        return instance;
    }

    public int getFeature() {
        return 63;
    }

    public boolean setManualConnection(boolean value) {
        Message msg = new Message();
        if (DBG) {
            Log.d(TAG, "setManualConnection() " + value);
        }
        msg.what = 32;
        Bundle args = new Bundle();
        args.putBoolean("enable", value);
        msg.obj = args;
        int result = this.mWifiManager.callSECApi(msg);
        if (result == 0) {
            mManualConnection = value;
            return true;
        }
        if (DBG) {
            Log.e(TAG, "set callSECApi(SKIP_AUTO_CONNECTION) fail(" + result + ")");
        }
        return false;
    }

    public boolean getManualConnection() {
        if (isFirstGetManualConnection) {
            Message msg = new Message();
            msg.what = 32;
            int result = this.mWifiManager.callSECApi(msg);
            if (result == 1) {
                mManualConnection = true;
            } else if (result == 0) {
                mManualConnection = false;
            } else if (!DBG) {
                return false;
            } else {
                Log.e(TAG, "get callSECApi(SKIP_AUTO_CONNECTION) fail(" + result + ")");
                return false;
            }
            isFirstGetManualConnection = false;
        }
        boolean ret = mManualConnection;
        if (DBG) {
            Log.d(TAG, "getManualConnection() " + ret);
        }
        return ret;
    }

    public boolean setDisconnectionPriority(int value) {
        Message msg = new Message();
        if (DBG) {
            Log.d(TAG, "setDisconnectionPriority() : value(" + value + ")");
        }
        msg.what = 84;
        Bundle args = new Bundle();
        args.putInt("priority", value);
        msg.obj = args;
        int result = this.mWifiManager.callSECApi(msg);
        if (result == 0) {
            return true;
        }
        if (DBG) {
            Log.e(TAG, "set callSECApi(setDisconnectionPriority) fail(" + result + ")");
        }
        return false;
    }

    public int getDisconnectionPriority() {
        Message msg = new Message();
        msg.what = 84;
        Bundle args = new Bundle();
        args.putInt("priority", -1);
        msg.obj = args;
        int result = this.mWifiManager.callSECApi(msg);
        if (DBG) {
            Log.d(TAG, "getDisconnectionPriority() " + result);
        }
        return result;
    }

    public List<GWSScanResult> getGWSScanResultsEx() {
        Log.e(TAG, "getGWSScanResultsEx()");
        Message msg = new Message();
        msg.what = 33;
        return setScanResultsEx(this.mWifiManager.callSECStringApi(msg));
    }

    public List<GWSScanResult> setScanResultsEx(String scanResults) {
        if (scanResults == null || TextUtils.isEmpty(scanResults)) {
            return null;
        }
        List<GWSScanResult> gwsScanList = new ArrayList();
        String[] lines = scanResults.split("\n");
        String bssid = "";
        WifiSsid wifiSsid = null;
        int level = 0;
        int freq = 0;
        String flags = "";
        String BSSLoadElement = "null";
        String vendorSpecificOUI = "null";
        String vendorSpecificContents = "null";
        int bssidStrLen = BSSID_STR.length();
        int flagLen = FLAGS_STR.length();
        int bleLen = BLE_STR.length();
        int vsiLen = VSI_STR.length();
        for (String line : lines) {
            if (line.startsWith(BSSID_STR)) {
                bssid = new String(line.getBytes(), bssidStrLen, line.length() - bssidStrLen);
            } else if (line.startsWith(FREQ_STR)) {
                try {
                    freq = Integer.parseInt(line.substring(FREQ_STR.length()));
                } catch (NumberFormatException e) {
                    freq = 0;
                }
            } else if (line.startsWith(LEVEL_STR)) {
                try {
                    level = Integer.parseInt(line.substring(LEVEL_STR.length()));
                    if (level > 0) {
                        level += InputDevice.SOURCE_ANY;
                    }
                } catch (NumberFormatException e2) {
                    level = 0;
                }
            } else if (line.startsWith(FLAGS_STR)) {
                flags = new String(line.getBytes(), flagLen, line.length() - flagLen);
            } else if (line.startsWith(SSID_STR)) {
                wifiSsid = WifiSsid.createFromAsciiEncoded(line.substring(SSID_STR.length()));
            } else if (line.startsWith(BLE_STR)) {
                BSSLoadElement = new String(line.getBytes(), bleLen, line.length() - bleLen);
            } else if (line.startsWith(VSI_STR)) {
                vendorSpecificOUI = new String(line.getBytes(), vsiLen, 6);
                vendorSpecificContents = new String(line.getBytes(), vsiLen + 6, (line.length() - vsiLen) - 6);
            } else if (line.startsWith(DELIMITER_STR) || line.startsWith(END_STR)) {
                if (bssid != null) {
                    String ssid = wifiSsid != null ? wifiSsid.toString() : "<unknown ssid>";
                    String key = bssid + ssid;
                    GWSScanResult scanResult = (GWSScanResult) this.mScanResultCache.get(key);
                    if (scanResult != null) {
                        scanResult.level = level;
                        scanResult.SSID = wifiSsid != null ? wifiSsid.toString() : "<unknown ssid>";
                        scanResult.capabilities = flags;
                        scanResult.frequency = freq;
                        scanResult.BSSLoadElement = BSSLoadElement;
                        scanResult.vendorSpecificOUI = vendorSpecificOUI;
                        scanResult.vendorSpecificContents = vendorSpecificContents;
                        gwsScanList.add(scanResult);
                    } else if (ssid.trim().length() > 0) {
                        scanResult = new GWSScanResult(ssid, bssid, flags, level, freq, BSSLoadElement, vendorSpecificOUI, vendorSpecificContents);
                        this.mScanResultCache.put(key, scanResult);
                        gwsScanList.add(scanResult);
                    }
                }
                bssid = null;
                level = 0;
                freq = 0;
                flags = "";
                wifiSsid = null;
                BSSLoadElement = "null";
                vendorSpecificOUI = "null";
                vendorSpecificContents = "null";
            }
        }
        return gwsScanList;
    }

    public int setInternetCheckOption(int id, int value) {
        Message msg = new Message();
        if (DBG) {
            Log.d(TAG, "setInternetCheckOption() Network ID(" + id + ") value(" + value + ")");
        }
        msg.what = 35;
        msg.arg1 = id;
        msg.arg2 = value;
        return this.mWifiManager.callSECApi(msg);
    }

    public int getInternetCheckOption(int id) {
        Message msg = new Message();
        if (DBG) {
            Log.d(TAG, "getInternetCheckOption() Network ID(" + id + ")");
        }
        msg.what = 34;
        msg.arg1 = id;
        return this.mWifiManager.callSECApi(msg);
    }

    public boolean isReconnectEnabled(int networkId) {
        boolean ret;
        Message msg = new Message();
        msg.what = 83;
        Bundle args = new Bundle();
        args.putInt("netId", networkId);
        args.putInt("autojoin", -1);
        msg.obj = args;
        if (DBG) {
            Log.d(TAG, "isReconnectEnabled() : networkId(" + networkId + ")");
        }
        int result = this.mWifiManager.callSECApi(msg);
        if (result == 1) {
            ret = true;
        } else if (result == 0) {
            ret = false;
        } else {
            if (DBG) {
                Log.e(TAG, "get callSECApi(isReconnectEnabled) fail(" + result + ")");
            }
            return false;
        }
        if (DBG) {
            Log.d(TAG, "isReconnectEnabled() " + ret);
        }
        return ret;
    }

    public boolean setReconnectEnabled(int networkId, boolean reconnect) {
        int value;
        Message msg = new Message();
        if (DBG) {
            Log.d(TAG, "setReconnectEnabled() Network ID(" + networkId + ") value(" + reconnect + ")");
        }
        msg.what = 83;
        Bundle args = new Bundle();
        args.putInt("netId", networkId);
        if (reconnect) {
            value = 1;
        } else {
            value = 0;
        }
        args.putInt("autojoin", value);
        msg.obj = args;
        int result = this.mWifiManager.callSECApi(msg);
        this.mWifiManager.saveConfiguration();
        if (result == 0) {
            return true;
        }
        if (DBG) {
            Log.e(TAG, "set callSECApi(setReconnectEnabled) fail(" + result + ")");
        }
        return false;
    }
}
