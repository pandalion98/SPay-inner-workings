package com.kddi.android;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkUtils;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings.Secure;
import android.util.Log;
import com.samsung.android.smartface.SmartFaceManager;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public final class CpaManager {
    private static final int APN_INDEX = 2;
    public static int AUTHENTICATION_ERROR = -3;
    private static final int CARRIER_ENABLED_INDEX = 4;
    private static final String CHANGE_MODE_REQUEST_ACTION = "com.kddi.android.cpa.CHANGE_MODE_REQUEST_ACTION";
    public static final int CONNECTED = 2;
    public static final int CONNECTING = 1;
    public static final int DISCONNECTED = 4;
    public static final int DISCONNECTING = 3;
    private static final int ID_INDEX = 0;
    private static String MODE_CPA = "DEFAULT_ON_CPA";
    private static String MODE_DEFAULT = "DEFAULT";
    private static String MODE_NAVI = "NAVI";
    private static final int NAME_INDEX = 1;
    public static int PARAMETER_ERROR = -1;
    public static int RADIO_NOT_AVAILABLE = -2;
    public static int SUCCESS = 0;
    private static final String TAG = "CpaManager";
    private static final int TYPES_INDEX = 3;
    public static int UNKNOWN_ERROR = -4;
    private boolean DBG;
    Context mContext;
    private String mSelectedKey;
    private int mState;

    public static class ConnInfo {
        public InetAddress[] dnsAddress;
        public InetAddress localAddress;
    }

    public static class Settings {
        public String apn;
        public int authType;
        public String dns1;
        public String dns2;
        public String password;
        public String proxyHost;
        public String proxyPort;
        public String userId;
    }

    public CpaManager(Context context) throws RemoteException {
        this.DBG = !SystemProperties.getBoolean("ro.product_ship", true);
        this.mState = 1;
        if (this.DBG) {
            Log.d(TAG, "#########################");
        }
        Log.d(TAG, "CpaManager constructor! context = " + context);
        if (this.DBG) {
            Log.d(TAG, "#########################");
        }
        if (context == null) {
            throw new RemoteException("context is null");
        }
        this.mContext = context;
        if (!checkPermission()) {
            Log.e(TAG, "CpaManager permission err!!");
            throw new RemoteException("checkPermission() return false");
        }
    }

    public int changeMode(String mode, Settings settings) throws RemoteException, IllegalArgumentException {
        Log.d(TAG, "changeMode() mode=" + mode + " settings=" + settings);
        if (mode == null) {
            throw new IllegalArgumentException("mode is null!! ");
        } else if (mode.equals(MODE_NAVI) && (settings == null || settings.apn == null)) {
            throw new IllegalArgumentException("mode is navi but settings is null!! ");
        } else {
            try {
                Intent intent = new Intent();
                intent.setAction(CHANGE_MODE_REQUEST_ACTION);
                intent.putExtra("mode", mode);
                if (settings != null) {
                    intent.putExtra("settings.apn", settings.apn);
                    intent.putExtra("settings.userId", settings.userId);
                    intent.putExtra("settings.password", settings.password);
                    intent.putExtra("settings.authType", settings.authType);
                    intent.putExtra("settings.proxyHost", settings.proxyHost);
                    intent.putExtra("settings.proxyPort", settings.proxyPort);
                    intent.putExtra("settings.dns1", settings.dns1);
                    intent.putExtra("settings.dns2", settings.dns2);
                }
                if (this.DBG) {
                    Log.i(TAG, "Display for Broadcating Intent =" + intent);
                }
                this.mContext.sendBroadcast(intent);
                return SUCCESS;
            } catch (Exception e) {
                throw new RemoteException("error!! send intent for chang mode");
            }
        }
    }

    public synchronized int getConnStatus() throws RemoteException {
        int state = getState();
        Log.d(TAG, "getConnStatus() state = " + state);
        if (state == 1 || state == 2 || state == 3 || state == 4) {
        } else {
            throw new RemoteException("error!! get state for navi cpa");
        }
        return getState();
    }

    public synchronized ConnInfo getConnInfo() throws RemoteException {
        ConnInfo connInfo;
        if (this.DBG) {
            Log.d(TAG, "getConnInfo() start");
        }
        connInfo = new ConnInfo();
        connInfo.dnsAddress = new InetAddress[2];
        try {
            if (getState() == 2) {
                String localIP = getLocalIpAddress();
                String[] dns = getCurrentDns();
                if (this.DBG) {
                    Log.d(TAG, " getLocalIpAddress() = " + localIP);
                }
                if (this.DBG) {
                    Log.d(TAG, " getCurrentDns() dns1 = " + dns[0] + " dns[1] = " + dns[1]);
                }
                connInfo.localAddress = NetworkUtils.numericToInetAddress(localIP);
                connInfo.dnsAddress[0] = NetworkUtils.numericToInetAddress(dns[0]);
                connInfo.dnsAddress[1] = NetworkUtils.numericToInetAddress(dns[1]);
                if (this.DBG) {
                    Log.d(TAG, " getConnInfo() localAddress  = " + connInfo.localAddress);
                }
                if (this.DBG) {
                    Log.d(TAG, " getConnInfo() dns1 = " + connInfo.dnsAddress[0] + "  dns2 = " + connInfo.dnsAddress[1]);
                }
            } else {
                connInfo = null;
            }
        } catch (IllegalArgumentException e) {
            throw new RemoteException("error!! get localip dns address for navi cpa");
        } catch (Exception e2) {
            throw new RemoteException("error!! getConnInfo()");
        }
        return connInfo;
    }

    private boolean checkPermission() {
        if (this.mContext != null) {
            int permissionGrantStatus = this.mContext.checkCallingOrSelfPermission("com.kddi.android.permission.MANAGE_CPA");
            if (permissionGrantStatus == 0) {
                return true;
            }
            Log.e(TAG, "checkPermission() err!! permissionGrantStatus =" + permissionGrantStatus);
        } else {
            Log.e(TAG, "checkPermission() err!! mContext =" + this.mContext);
        }
        return false;
    }

    private int getState() {
        return Secure.getInt(this.mContext.getContentResolver(), "kddi_cpa_state", 0);
    }

    public static String getLocalIpAddress() throws RemoteException {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en != null && en.hasMoreElements()) {
                NetworkInterface interf = (NetworkInterface) en.nextElement();
                if (!(interf.getName().contains("pdp") || interf.getName().equals("lo"))) {
                    Enumeration<InetAddress> ips = interf.getInetAddresses();
                    while (ips.hasMoreElements()) {
                        InetAddress inetAddress = (InetAddress) ips.nextElement();
                        if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                    continue;
                }
            }
            Log.e(TAG, "getLocalIpAddress() == null");
            return null;
        } catch (SocketException ex) {
            Log.e(TAG, ex.toString());
            throw new RemoteException("error!! get local address");
        }
    }

    public synchronized String[] getCurrentDns() {
        String[] dns;
        dns = new String[2];
        if (SystemProperties.get("sys.cpa_kdd_hipri").equals(SmartFaceManager.TRUE)) {
            dns[0] = SystemProperties.get("net.rmnet0.dns1");
            dns[1] = SystemProperties.get("net.rmnet0.dns2");
            if (dns[0] == null || dns[0].length() <= 0 || dns[0].equals("undefined")) {
                dns[0] = null;
            }
            if (dns[1] == null || dns[1].length() <= 0 || dns[1].equals("undefined")) {
                dns[1] = null;
            }
        } else {
            dns[0] = SystemProperties.get("net.dns1");
            dns[1] = SystemProperties.get("net.dns2");
            if (dns[0] == null || dns[0].length() <= 0 || dns[0].equals("undefined")) {
                dns[0] = null;
            }
            if (dns[1] == null || dns[1].length() <= 0 || dns[1].equals("undefined")) {
                dns[1] = null;
            }
        }
        return dns;
    }
}
