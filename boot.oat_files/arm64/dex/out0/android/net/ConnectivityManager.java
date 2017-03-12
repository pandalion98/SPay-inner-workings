package android.net;

import android.Manifest.permission;
import android.app.ActivityThread;
import android.app.PendingIntent;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.INetworkActivityListener;
import android.os.INetworkManagementService;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.ParcelFileDescriptor;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.Settings;
import android.provider.Settings.System;
import android.telephony.SubscriptionManager;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephony.Stub;
import com.android.internal.util.Preconditions;
import com.samsung.android.telephony.MultiSimManager;
import com.sec.android.app.CscFeature;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import libcore.net.event.NetworkEventDispatcher;

public class ConnectivityManager {
    @Deprecated
    public static final String ACTION_BACKGROUND_DATA_SETTING_CHANGED = "android.net.conn.BACKGROUND_DATA_SETTING_CHANGED";
    public static final String ACTION_CAPTIVE_PORTAL_SIGN_IN = "android.net.conn.CAPTIVE_PORTAL";
    public static final String ACTION_CAPTIVE_PORTAL_TEST_COMPLETED = "android.net.conn.CAPTIVE_PORTAL_TEST_COMPLETED";
    public static final String ACTION_DATA_ACTIVITY_CHANGE = "android.net.conn.DATA_ACTIVITY_CHANGE";
    public static final String ACTION_PROMPT_UNVALIDATED = "android.net.conn.PROMPT_UNVALIDATED";
    public static final String ACTION_TETHER_STATE_CHANGED = "android.net.conn.TETHER_STATE_CHANGED";
    private static final int BASE = 524288;
    public static final int CALLBACK_AVAILABLE = 524290;
    public static final int CALLBACK_CAP_CHANGED = 524294;
    public static final int CALLBACK_EXIT = 524297;
    public static final int CALLBACK_IP_CHANGED = 524295;
    public static final int CALLBACK_LOSING = 524291;
    public static final int CALLBACK_LOST = 524292;
    public static final int CALLBACK_PRECHECK = 524289;
    public static final int CALLBACK_RELEASED = 524296;
    public static final int CALLBACK_RESUMED = 524300;
    public static final int CALLBACK_SUSPENDED = 524299;
    public static final int CALLBACK_UNAVAIL = 524293;
    public static final String CONNECTIVITY_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    public static final String CONNECTIVITY_ACTION_SUPL = "android.net.conn.CONNECTIVITY_CHANGE_SUPL";
    public static final int CONNECTIVITY_CHANGE_DELAY_DEFAULT = 500;
    @Deprecated
    public static final int DEFAULT_NETWORK_PREFERENCE = 1;
    private static final int EXPIRE_LEGACY_REQUEST = 524298;
    public static final String EXTRA_ACTIVE_TETHER = "activeArray";
    public static final String EXTRA_AVAILABLE_TETHER = "availableArray";
    public static final String EXTRA_CAPTIVE_PORTAL = "android.net.extra.CAPTIVE_PORTAL";
    public static final String EXTRA_CAPTIVE_PORTAL_SMART_DETECTION = "android.net.extra.CAPTIVE_PORTAL_SMART_DETECTION";
    public static final String EXTRA_DEVICE_TYPE = "deviceType";
    public static final String EXTRA_ERRORED_TETHER = "erroredArray";
    public static final String EXTRA_EXTRA_INFO = "extraInfo";
    public static final String EXTRA_INET_CONDITION = "inetCondition";
    public static final String EXTRA_IS_ACTIVE = "isActive";
    public static final String EXTRA_IS_CAPTIVE_PORTAL = "captivePortal";
    public static final String EXTRA_IS_FAILOVER = "isFailover";
    public static final String EXTRA_NETWORK = "android.net.extra.NETWORK";
    @Deprecated
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String EXTRA_NETWORK_REQUEST = "android.net.extra.NETWORK_REQUEST";
    public static final String EXTRA_NETWORK_TYPE = "networkType";
    public static final String EXTRA_NO_CONNECTIVITY = "noConnectivity";
    public static final String EXTRA_OTHER_NETWORK_INFO = "otherNetwork";
    public static final String EXTRA_REALTIME_NS = "tsNanos";
    public static final String EXTRA_REASON = "reason";
    public static final String INET_CONDITION_ACTION = "android.net.conn.INET_CONDITION_ACTION";
    private static final int LISTEN = 1;
    public static final int MAX_NETWORK_REQUEST_TIMEOUT_MS = 6000000;
    public static final int MAX_NETWORK_TYPE = 17;
    public static final int MAX_NETWORK_TYPE_EX = 29;
    public static final int MAX_RADIO_TYPE = 17;
    public static final int NETID_UNSET = 0;
    private static final int REQUEST = 2;
    public static final int REQUEST_ID_UNSET = 0;
    private static final String TAG = "ConnectivityManager";
    public static final int TETHER_ERROR_DISABLE_NAT_ERROR = 9;
    public static final int TETHER_ERROR_ENABLE_NAT_ERROR = 8;
    public static final int TETHER_ERROR_IFACE_CFG_ERROR = 10;
    public static final int TETHER_ERROR_MASTER_ERROR = 5;
    public static final int TETHER_ERROR_NO_ERROR = 0;
    public static final int TETHER_ERROR_SECURITY_POLICY = 20;
    public static final int TETHER_ERROR_SERVICE_UNAVAIL = 2;
    public static final int TETHER_ERROR_TETHER_IFACE_ERROR = 6;
    public static final int TETHER_ERROR_UNAVAIL_IFACE = 4;
    public static final int TETHER_ERROR_UNKNOWN_IFACE = 1;
    public static final int TETHER_ERROR_UNSUPPORTED = 3;
    public static final int TETHER_ERROR_UNTETHER_IFACE_ERROR = 7;
    public static final int TYPE_BLUETOOTH = 7;
    public static final int TYPE_DUMMY = 8;
    public static final int TYPE_ETHERNET = 9;
    public static final int TYPE_MOBILE = 0;
    public static final int TYPE_MOBILE_BIP = 23;
    public static final int TYPE_MOBILE_CAS = 24;
    public static final int TYPE_MOBILE_CBS = 12;
    public static final int TYPE_MOBILE_CMDM = 20;
    public static final int TYPE_MOBILE_CMMAIL = 21;
    public static final int TYPE_MOBILE_DM = 25;
    public static final int TYPE_MOBILE_DUN = 4;
    public static final int TYPE_MOBILE_EMERGENCY = 15;
    public static final int TYPE_MOBILE_ENT1 = 28;
    public static final int TYPE_MOBILE_ENT2 = 29;
    public static final int TYPE_MOBILE_FOTA = 10;
    public static final int TYPE_MOBILE_HIPRI = 5;
    public static final int TYPE_MOBILE_IA = 14;
    public static final int TYPE_MOBILE_IMS = 11;
    public static final int TYPE_MOBILE_MMS = 2;
    public static final int TYPE_MOBILE_MMS2 = 26;
    public static final int TYPE_MOBILE_SUPL = 3;
    public static final int TYPE_MOBILE_WAP = 22;
    public static final int TYPE_MOBILE_XCAP = 27;
    public static final int TYPE_NONE = -1;
    public static final int TYPE_PROXY = 16;
    public static final int TYPE_VPN = 17;
    public static final int TYPE_WIFI = 1;
    public static final int TYPE_WIFI_P2P = 13;
    public static final int TYPE_WIMAX = 6;
    static CallbackHandler sCallbackHandler = null;
    static final AtomicInteger sCallbackRefCount = new AtomicInteger(0);
    private static ConnectivityManager sInstance;
    private static HashMap<NetworkCapabilities, LegacyRequest> sLegacyRequests = new HashMap();
    static final HashMap<NetworkRequest, NetworkCallback> sNetworkCallback = new HashMap();
    private final Context mContext;
    private INetworkManagementService mNMService;
    private final ArrayMap<OnNetworkActiveListener, INetworkActivityListener> mNetworkActivityListeners = new ArrayMap();
    private final IConnectivityManager mService;

    private class CallbackHandler extends Handler {
        private static final boolean DBG = false;
        private static final String TAG = "ConnectivityManager.CallbackHandler";
        private final HashMap<NetworkRequest, NetworkCallback> mCallbackMap;
        private final ConnectivityManager mCm;
        private final AtomicInteger mRefCount;

        CallbackHandler(Looper looper, HashMap<NetworkRequest, NetworkCallback> callbackMap, AtomicInteger refCount, ConnectivityManager cm) {
            super(looper);
            this.mCallbackMap = callbackMap;
            this.mRefCount = refCount;
            this.mCm = cm;
        }

        public void handleMessage(Message message) {
            NetworkRequest request = (NetworkRequest) getObject(message, NetworkRequest.class);
            Network network = (Network) getObject(message, Network.class);
            NetworkCallback callback;
            switch (message.what) {
                case ConnectivityManager.CALLBACK_PRECHECK /*524289*/:
                    callback = getCallback(request, "PRECHECK");
                    if (callback != null) {
                        callback.onPreCheck(network);
                        return;
                    }
                    return;
                case ConnectivityManager.CALLBACK_AVAILABLE /*524290*/:
                    callback = getCallback(request, "AVAILABLE");
                    if (callback != null) {
                        callback.onAvailable(network);
                        return;
                    }
                    return;
                case ConnectivityManager.CALLBACK_LOSING /*524291*/:
                    callback = getCallback(request, "LOSING");
                    if (callback != null) {
                        callback.onLosing(network, message.arg1);
                        return;
                    }
                    return;
                case ConnectivityManager.CALLBACK_LOST /*524292*/:
                    callback = getCallback(request, "LOST");
                    if (callback != null) {
                        callback.onLost(network);
                        return;
                    }
                    return;
                case ConnectivityManager.CALLBACK_UNAVAIL /*524293*/:
                    callback = getCallback(request, "UNAVAIL");
                    if (callback != null) {
                        callback.onUnavailable();
                        return;
                    }
                    return;
                case ConnectivityManager.CALLBACK_CAP_CHANGED /*524294*/:
                    callback = getCallback(request, "CAP_CHANGED");
                    if (callback != null) {
                        callback.onCapabilitiesChanged(network, (NetworkCapabilities) getObject(message, NetworkCapabilities.class));
                        return;
                    }
                    return;
                case ConnectivityManager.CALLBACK_IP_CHANGED /*524295*/:
                    callback = getCallback(request, "IP_CHANGED");
                    if (callback != null) {
                        callback.onLinkPropertiesChanged(network, (LinkProperties) getObject(message, LinkProperties.class));
                        return;
                    }
                    return;
                case ConnectivityManager.CALLBACK_RELEASED /*524296*/:
                    synchronized (this.mCallbackMap) {
                        callback = (NetworkCallback) this.mCallbackMap.remove(request);
                    }
                    if (callback != null) {
                        synchronized (this.mRefCount) {
                            if (this.mRefCount.decrementAndGet() == 0) {
                                getLooper().quit();
                            }
                        }
                        return;
                    }
                    Log.e(TAG, "callback not found for RELEASED message");
                    return;
                case ConnectivityManager.CALLBACK_EXIT /*524297*/:
                    Log.d(TAG, "Listener quitting");
                    getLooper().quit();
                    return;
                case ConnectivityManager.EXPIRE_LEGACY_REQUEST /*524298*/:
                    ConnectivityManager.this.expireRequest((NetworkCapabilities) message.obj, message.arg1);
                    return;
                case ConnectivityManager.CALLBACK_SUSPENDED /*524299*/:
                    callback = getCallback(request, "SUSPENDED");
                    if (callback != null) {
                        callback.onNetworkSuspended(network);
                        return;
                    }
                    return;
                case ConnectivityManager.CALLBACK_RESUMED /*524300*/:
                    callback = getCallback(request, "RESUMED");
                    if (callback != null) {
                        callback.onNetworkResumed(network);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        private Object getObject(Message msg, Class c) {
            return msg.getData().getParcelable(c.getSimpleName());
        }

        private NetworkCallback getCallback(NetworkRequest req, String name) {
            NetworkCallback callback;
            synchronized (this.mCallbackMap) {
                callback = (NetworkCallback) this.mCallbackMap.get(req);
            }
            if (callback == null) {
                Log.e(TAG, "callback not found for " + name + " message");
            }
            return callback;
        }
    }

    public static class NetworkCallback {
        private NetworkRequest networkRequest;

        public void onPreCheck(Network network) {
        }

        public void onAvailable(Network network) {
        }

        public void onLosing(Network network, int maxMsToLive) {
        }

        public void onLost(Network network) {
        }

        public void onUnavailable() {
        }

        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        }

        public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        }

        public void onNetworkSuspended(Network network) {
        }

        public void onNetworkResumed(Network network) {
        }
    }

    private static class LegacyRequest {
        Network currentNetwork;
        int delay;
        int expireSequenceNumber;
        NetworkCallback networkCallback;
        NetworkCapabilities networkCapabilities;
        NetworkRequest networkRequest;

        private LegacyRequest() {
            this.delay = -1;
            this.networkCallback = new NetworkCallback() {
                public void onAvailable(Network network) {
                    LegacyRequest.this.currentNetwork = network;
                    Log.d(ConnectivityManager.TAG, "startUsingNetworkFeature got Network:" + network);
                    ConnectivityManager.setProcessDefaultNetworkForHostResolution(network);
                }

                public void onLost(Network network) {
                    if (network.equals(LegacyRequest.this.currentNetwork)) {
                        LegacyRequest.this.clearDnsBinding();
                    }
                    Log.d(ConnectivityManager.TAG, "startUsingNetworkFeature lost Network:" + network);
                }
            };
        }

        private void clearDnsBinding() {
            if (this.currentNetwork != null) {
                this.currentNetwork = null;
                ConnectivityManager.setProcessDefaultNetworkForHostResolution(null);
            }
        }
    }

    public interface OnNetworkActiveListener {
        void onNetworkActive();
    }

    public class PacketKeepalive {
        public static final int BINDER_DIED = -10;
        public static final int ERROR_HARDWARE_ERROR = -31;
        public static final int ERROR_HARDWARE_UNSUPPORTED = -30;
        public static final int ERROR_INVALID_INTERVAL = -24;
        public static final int ERROR_INVALID_IP_ADDRESS = -21;
        public static final int ERROR_INVALID_LENGTH = -23;
        public static final int ERROR_INVALID_NETWORK = -20;
        public static final int ERROR_INVALID_PORT = -22;
        public static final int NATT_PORT = 4500;
        public static final int NO_KEEPALIVE = -1;
        public static final int SUCCESS = 0;
        private static final String TAG = "PacketKeepalive";
        private final PacketKeepaliveCallback mCallback;
        private final Looper mLooper;
        private final Messenger mMessenger;
        private final Network mNetwork;
        private volatile Integer mSlot;

        void stopLooper() {
            this.mLooper.quit();
        }

        public void stop() {
            try {
                ConnectivityManager.this.mService.stopKeepalive(this.mNetwork, this.mSlot.intValue());
            } catch (RemoteException e) {
                Log.e(TAG, "Error stopping packet keepalive: ", e);
                stopLooper();
            }
        }

        private PacketKeepalive(Network network, PacketKeepaliveCallback callback) {
            Preconditions.checkNotNull(network, "network cannot be null");
            Preconditions.checkNotNull(callback, "callback cannot be null");
            this.mNetwork = network;
            this.mCallback = callback;
            HandlerThread thread = new HandlerThread(TAG);
            thread.start();
            this.mLooper = thread.getLooper();
            this.mMessenger = new Messenger(new Handler(this.mLooper, ConnectivityManager.this) {
                public void handleMessage(Message message) {
                    switch (message.what) {
                        case NetworkAgent.EVENT_PACKET_KEEPALIVE /*528397*/:
                            int error = message.arg2;
                            if (error == 0) {
                                try {
                                    if (PacketKeepalive.this.mSlot == null) {
                                        PacketKeepalive.this.mSlot = Integer.valueOf(message.arg1);
                                        PacketKeepalive.this.mCallback.onStarted();
                                        return;
                                    }
                                    PacketKeepalive.this.mSlot = null;
                                    PacketKeepalive.this.stopLooper();
                                    PacketKeepalive.this.mCallback.onStopped();
                                    return;
                                } catch (Exception e) {
                                    Log.e(PacketKeepalive.TAG, "Exception in keepalive callback(" + error + ")", e);
                                    return;
                                }
                            }
                            PacketKeepalive.this.stopLooper();
                            PacketKeepalive.this.mCallback.onError(error);
                            return;
                        default:
                            Log.e(PacketKeepalive.TAG, "Unhandled message " + Integer.toHexString(message.what));
                            return;
                    }
                }
            });
        }
    }

    public static class PacketKeepaliveCallback {
        public void onStarted() {
        }

        public void onStopped() {
        }

        public void onError(int error) {
        }
    }

    public static boolean isNetworkTypeValid(int networkType) {
        return (networkType >= 0 && networkType <= 17) || (networkType >= 20 && networkType <= 29);
    }

    public static String getNetworkTypeName(int type) {
        switch (type) {
            case 0:
                return "MOBILE";
            case 1:
                return "WIFI";
            case 2:
                return "MOBILE_MMS";
            case 3:
                return "MOBILE_SUPL";
            case 4:
                return "MOBILE_DUN";
            case 5:
                return "MOBILE_HIPRI";
            case 6:
                return "WIMAX";
            case 7:
                return "BLUETOOTH";
            case 8:
                return "DUMMY";
            case 9:
                return "ETHERNET";
            case 10:
                return "MOBILE_FOTA";
            case 11:
                return "MOBILE_IMS";
            case 12:
                return "MOBILE_CBS";
            case 13:
                return "WIFI_P2P";
            case 14:
                return "MOBILE_IA";
            case 15:
                return "MOBILE_EMERGENCY";
            case 16:
                return "PROXY";
            case 17:
                return "VPN";
            case 20:
                return "MOBILE_CMDM";
            case 21:
                return "MOBILE_CMMAIL";
            case 22:
                return "MOBILE_WAP";
            case 23:
                return "MOBILE_BIP";
            case 24:
                return "MOBILE_CAS";
            case 25:
                return "MOBILE_DM";
            case 26:
                return "MOBILE_MMS2";
            case 27:
                return "MOBILE_XCAP";
            case 28:
                return "MOBILE_ENT1";
            case 29:
                return "MOBILE_ENT2";
            default:
                return Integer.toString(type);
        }
    }

    public static boolean isNetworkTypeMobile(int networkType) {
        switch (networkType) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 10:
            case 11:
            case 12:
            case 14:
            case 15:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
                return true;
            default:
                return false;
        }
    }

    public static boolean isNetworkTypeWifi(int networkType) {
        switch (networkType) {
            case 1:
            case 13:
                return true;
            default:
                return false;
        }
    }

    public void setNetworkPreference(int preference) {
    }

    public int getNetworkPreference() {
        return -1;
    }

    public NetworkInfo getActiveNetworkInfo() {
        try {
            return this.mService.getActiveNetworkInfo();
        } catch (RemoteException e) {
            return null;
        }
    }

    public Network getActiveNetwork() {
        try {
            return this.mService.getActiveNetwork();
        } catch (RemoteException e) {
            return null;
        }
    }

    public NetworkInfo getActiveNetworkInfoForUid(int uid) {
        try {
            return this.mService.getActiveNetworkInfoForUid(uid);
        } catch (RemoteException e) {
            return null;
        }
    }

    public NetworkInfo getNetworkInfo(int networkType) {
        try {
            return this.mService.getNetworkInfo(networkType);
        } catch (RemoteException e) {
            return null;
        }
    }

    public NetworkInfo getNetworkInfo(Network network) {
        try {
            return this.mService.getNetworkInfoForNetwork(network);
        } catch (RemoteException e) {
            return null;
        }
    }

    public NetworkInfo[] getAllNetworkInfo() {
        try {
            return this.mService.getAllNetworkInfo();
        } catch (RemoteException e) {
            return null;
        }
    }

    public NetworkInfo[] getAllNetworkInfoEx() {
        try {
            return this.mService.getAllNetworkInfoEx();
        } catch (RemoteException e) {
            return null;
        }
    }

    public Network getNetworkForType(int networkType) {
        try {
            return this.mService.getNetworkForType(networkType);
        } catch (RemoteException e) {
            return null;
        }
    }

    public Network[] getAllNetworks() {
        try {
            return this.mService.getAllNetworks();
        } catch (RemoteException e) {
            return null;
        }
    }

    public NetworkCapabilities[] getDefaultNetworkCapabilitiesForUser(int userId) {
        try {
            return this.mService.getDefaultNetworkCapabilitiesForUser(userId);
        } catch (RemoteException e) {
            return null;
        }
    }

    public LinkProperties getActiveLinkProperties() {
        try {
            return this.mService.getActiveLinkProperties();
        } catch (RemoteException e) {
            return null;
        }
    }

    public LinkProperties getLinkProperties(int networkType) {
        try {
            return this.mService.getLinkPropertiesForType(networkType);
        } catch (RemoteException e) {
            return null;
        }
    }

    public LinkProperties getLinkProperties(Network network) {
        try {
            return this.mService.getLinkProperties(network);
        } catch (RemoteException e) {
            return null;
        }
    }

    public NetworkCapabilities getNetworkCapabilities(Network network) {
        try {
            return this.mService.getNetworkCapabilities(network);
        } catch (RemoteException e) {
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int startUsingNetworkFeature(int r18, java.lang.String r19) {
        /*
        r17 = this;
        r13 = "ConnectivityManager";
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "CallingUid : ";
        r14 = r14.append(r15);
        r15 = android.os.Binder.getCallingUid();
        r14 = r14.append(r15);
        r15 = ", CallingPid : ";
        r14 = r14.append(r15);
        r15 = android.os.Binder.getCallingPid();
        r14 = r14.append(r15);
        r14 = r14.toString();
        android.util.Log.d(r13, r14);
        r17.checkLegacyRoutingApiAccess();
        r9 = r17.networkCapabilitiesForFeature(r18, r19);
        if (r9 != 0) goto L_0x005b;
    L_0x0033:
        r13 = "ConnectivityManager";
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "Can't satisfy startUsingNetworkFeature for ";
        r14 = r14.append(r15);
        r0 = r18;
        r14 = r14.append(r0);
        r15 = ", ";
        r14 = r14.append(r15);
        r0 = r19;
        r14 = r14.append(r0);
        r14 = r14.toString();
        android.util.Log.d(r13, r14);
        r13 = 3;
    L_0x005a:
        return r13;
    L_0x005b:
        r13 = "LGT";
        r14 = "EUR";
        r13 = r13.equals(r14);
        if (r13 == 0) goto L_0x00b1;
    L_0x0065:
        if (r18 != 0) goto L_0x008b;
    L_0x0067:
        r13 = "enableMMS";
        r0 = r19;
        r13 = android.text.TextUtils.equals(r0, r13);
        if (r13 != 0) goto L_0x008b;
    L_0x0071:
        r13 = "enableIMS";
        r0 = r19;
        r13 = android.text.TextUtils.equals(r0, r13);
        if (r13 != 0) goto L_0x008b;
    L_0x007b:
        r13 = r17.getMobileDataEnabled();
        if (r13 != 0) goto L_0x008b;
    L_0x0081:
        r13 = "ConnectivityManager";
        r14 = "requested special network with data disabled - rejected";
        android.util.Log.d(r13, r14);
        r13 = 2;
        goto L_0x005a;
    L_0x008b:
        if (r18 != 0) goto L_0x00b1;
    L_0x008d:
        r13 = "enableSUPL";
        r0 = r19;
        r13 = android.text.TextUtils.equals(r0, r13);
        if (r13 == 0) goto L_0x00b1;
    L_0x0097:
        r13 = "8";
        r14 = "ril.ActivePDPRejectCause";
        r15 = "0";
        r14 = android.os.SystemProperties.get(r14, r15);
        r13 = r13.equals(r14);
        if (r13 == 0) goto L_0x00b1;
    L_0x00a8:
        r13 = "ConnectivityManager";
        r14 = "enableSUPL type  NOT available - rejectCause 8";
        android.util.Log.d(r13, r14);
        r13 = 2;
        goto L_0x005a;
    L_0x00b1:
        r13 = com.sec.android.app.CscFeature.getInstance();
        r14 = "CscFeature_RIL_ConfigNetworkTypeCapability";
        r3 = r13.getString(r14);
        if (r3 == 0) goto L_0x00fc;
    L_0x00bd:
        r13 = "VZW-";
        r13 = r3.startsWith(r13);
        if (r13 == 0) goto L_0x00fc;
    L_0x00c5:
        r8 = 1;
        r13 = "phone";
        r13 = android.os.ServiceManager.getService(r13);	 Catch:{ RemoteException -> 0x00e8, NullPointerException -> 0x00f2 }
        r13 = com.android.internal.telephony.ITelephony.Stub.asInterface(r13);	 Catch:{ RemoteException -> 0x00e8, NullPointerException -> 0x00f2 }
        r8 = r13.needsOtaServiceProvisioning();	 Catch:{ RemoteException -> 0x00e8, NullPointerException -> 0x00f2 }
    L_0x00d5:
        r13 = 5;
        r13 = r9.hasCapability(r13);
        if (r13 == 0) goto L_0x00fc;
    L_0x00dc:
        if (r8 == 0) goto L_0x00fc;
    L_0x00de:
        r13 = "ConnectivityManager";
        r14 = "Device is not activated yet";
        android.util.Log.d(r13, r14);
        r13 = 3;
        goto L_0x005a;
    L_0x00e8:
        r5 = move-exception;
        r13 = "ConnectivityManager";
        r14 = "needsOtaServiceProvisioning returned RemoteException";
        android.util.Log.e(r13, r14);
        goto L_0x00d5;
    L_0x00f2:
        r5 = move-exception;
        r13 = "ConnectivityManager";
        r14 = "needsOtaServiceProvisioning has NullPointerException";
        android.util.Log.e(r13, r14);
        goto L_0x00d5;
    L_0x00fc:
        if (r3 == 0) goto L_0x0192;
    L_0x00fe:
        r13 = "VZW-";
        r13 = r3.startsWith(r13);
        if (r13 == 0) goto L_0x0192;
    L_0x0106:
        r13 = "enableFOTA";
        r0 = r19;
        r13 = android.text.TextUtils.equals(r0, r13);
        if (r13 != 0) goto L_0x012e;
    L_0x0110:
        r13 = "enableMMS";
        r0 = r19;
        r13 = android.text.TextUtils.equals(r0, r13);
        if (r13 != 0) goto L_0x012e;
    L_0x011a:
        r13 = "enableCBS";
        r0 = r19;
        r13 = android.text.TextUtils.equals(r0, r13);
        if (r13 != 0) goto L_0x012e;
    L_0x0124:
        r13 = "enable800APN";
        r0 = r19;
        r13 = android.text.TextUtils.equals(r0, r13);
        if (r13 == 0) goto L_0x0192;
    L_0x012e:
        r1 = r9.firstNetCapToApnType();
        r13 = "ConnectivityManager";
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "feature: ";
        r14 = r14.append(r15);
        r0 = r19;
        r14 = r14.append(r0);
        r15 = " apnType: ";
        r14 = r14.append(r15);
        r14 = r14.append(r1);
        r14 = r14.toString();
        android.util.Log.d(r13, r14);
        r13 = "phone";
        r2 = android.os.ServiceManager.getService(r13);
        if (r2 == 0) goto L_0x0192;
    L_0x015f:
        r6 = com.android.internal.telephony.ITelephony.Stub.asInterface(r2);	 Catch:{ RemoteException -> 0x018a, NullPointerException -> 0x021e }
        r13 = r6.isApnTypeAvailable(r1);	 Catch:{ RemoteException -> 0x018a, NullPointerException -> 0x021e }
        if (r13 != 0) goto L_0x0192;
    L_0x0169:
        r13 = "ConnectivityManager";
        r14 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x018a, NullPointerException -> 0x021e }
        r14.<init>();	 Catch:{ RemoteException -> 0x018a, NullPointerException -> 0x021e }
        r15 = "APN type (";
        r14 = r14.append(r15);	 Catch:{ RemoteException -> 0x018a, NullPointerException -> 0x021e }
        r14 = r14.append(r1);	 Catch:{ RemoteException -> 0x018a, NullPointerException -> 0x021e }
        r15 = ") NOT available";
        r14 = r14.append(r15);	 Catch:{ RemoteException -> 0x018a, NullPointerException -> 0x021e }
        r14 = r14.toString();	 Catch:{ RemoteException -> 0x018a, NullPointerException -> 0x021e }
        android.util.Log.e(r13, r14);	 Catch:{ RemoteException -> 0x018a, NullPointerException -> 0x021e }
        r13 = 2;
        goto L_0x005a;
    L_0x018a:
        r4 = move-exception;
        r13 = "ConnectivityManager";
        r14 = "RemoteException isApnTypeAvailable()";
        android.util.Log.e(r13, r14, r4);
    L_0x0192:
        r13 = com.sec.android.app.CscFeature.getInstance();
        r14 = "CscFeature_RIL_SupportImsService";
        r13 = r13.getEnableStatus(r14);
        if (r13 == 0) goto L_0x0230;
    L_0x019e:
        r13 = "enableIMS";
        r0 = r19;
        r13 = android.text.TextUtils.equals(r0, r13);
        if (r13 != 0) goto L_0x01b2;
    L_0x01a8:
        r13 = "enableXCAP";
        r0 = r19;
        r13 = android.text.TextUtils.equals(r0, r13);
        if (r13 == 0) goto L_0x0230;
    L_0x01b2:
        r1 = r9.firstNetCapToApnType();
        r13 = "ConnectivityManager";
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "VoLTE : feature: ";
        r14 = r14.append(r15);
        r0 = r19;
        r14 = r14.append(r0);
        r15 = " apnType: ";
        r14 = r14.append(r15);
        r14 = r14.append(r1);
        r14 = r14.toString();
        android.util.Log.d(r13, r14);
        r13 = "phone";
        r2 = android.os.ServiceManager.getService(r13);
        if (r2 == 0) goto L_0x0230;
    L_0x01e3:
        r6 = com.android.internal.telephony.ITelephony.Stub.asInterface(r2);	 Catch:{ RemoteException -> 0x0228, NullPointerException -> 0x026b }
        r13 = 3;
        r12 = com.samsung.android.telephony.MultiSimManager.getDefaultSubscriptionId(r13);	 Catch:{ RemoteException -> 0x0228, NullPointerException -> 0x026b }
        r13 = android.telephony.SubscriptionManager.isValidSubscriptionId(r12);	 Catch:{ RemoteException -> 0x0228, NullPointerException -> 0x026b }
        if (r13 != 0) goto L_0x01f7;
    L_0x01f2:
        r13 = 0;
        r12 = com.samsung.android.telephony.MultiSimManager.getDefaultSubscriptionId(r13);	 Catch:{ RemoteException -> 0x0228, NullPointerException -> 0x026b }
    L_0x01f7:
        r13 = r6.isApnTypeAvailableUsingSubId(r1, r12);	 Catch:{ RemoteException -> 0x0228, NullPointerException -> 0x026b }
        if (r13 != 0) goto L_0x0230;
    L_0x01fd:
        r13 = "ConnectivityManager";
        r14 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0228, NullPointerException -> 0x026b }
        r14.<init>();	 Catch:{ RemoteException -> 0x0228, NullPointerException -> 0x026b }
        r15 = "APN type (";
        r14 = r14.append(r15);	 Catch:{ RemoteException -> 0x0228, NullPointerException -> 0x026b }
        r14 = r14.append(r1);	 Catch:{ RemoteException -> 0x0228, NullPointerException -> 0x026b }
        r15 = ") NOT available";
        r14 = r14.append(r15);	 Catch:{ RemoteException -> 0x0228, NullPointerException -> 0x026b }
        r14 = r14.toString();	 Catch:{ RemoteException -> 0x0228, NullPointerException -> 0x026b }
        android.util.Log.e(r13, r14);	 Catch:{ RemoteException -> 0x0228, NullPointerException -> 0x026b }
        r13 = 2;
        goto L_0x005a;
    L_0x021e:
        r4 = move-exception;
        r13 = "ConnectivityManager";
        r14 = "NullPointerException isApnTypeAvailable()";
        android.util.Log.e(r13, r14, r4);
        goto L_0x0192;
    L_0x0228:
        r4 = move-exception;
        r13 = "ConnectivityManager";
        r14 = "RemoteException isApnTypeAvailable()";
        android.util.Log.e(r13, r14, r4);
    L_0x0230:
        r11 = 0;
        r14 = sLegacyRequests;
        monitor-enter(r14);
        r13 = sLegacyRequests;	 Catch:{ all -> 0x0268 }
        r7 = r13.get(r9);	 Catch:{ all -> 0x0268 }
        r7 = (android.net.ConnectivityManager.LegacyRequest) r7;	 Catch:{ all -> 0x0268 }
        if (r7 == 0) goto L_0x0278;
    L_0x023e:
        r13 = "ConnectivityManager";
        r15 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0268 }
        r15.<init>();	 Catch:{ all -> 0x0268 }
        r16 = "renewing startUsingNetworkFeature request ";
        r15 = r15.append(r16);	 Catch:{ all -> 0x0268 }
        r0 = r7.networkRequest;	 Catch:{ all -> 0x0268 }
        r16 = r0;
        r15 = r15.append(r16);	 Catch:{ all -> 0x0268 }
        r15 = r15.toString();	 Catch:{ all -> 0x0268 }
        android.util.Log.d(r13, r15);	 Catch:{ all -> 0x0268 }
        r0 = r17;
        r0.renewRequestLocked(r7);	 Catch:{ all -> 0x0268 }
        r13 = r7.currentNetwork;	 Catch:{ all -> 0x0268 }
        if (r13 == 0) goto L_0x0274;
    L_0x0264:
        r13 = 0;
        monitor-exit(r14);	 Catch:{ all -> 0x0268 }
        goto L_0x005a;
    L_0x0268:
        r13 = move-exception;
        monitor-exit(r14);	 Catch:{ all -> 0x0268 }
        throw r13;
    L_0x026b:
        r4 = move-exception;
        r13 = "ConnectivityManager";
        r14 = "NullPointerException isApnTypeAvailable()";
        android.util.Log.e(r13, r14, r4);
        goto L_0x0230;
    L_0x0274:
        r13 = 1;
        monitor-exit(r14);	 Catch:{ all -> 0x0268 }
        goto L_0x005a;
    L_0x0278:
        r0 = r17;
        r11 = r0.requestNetworkForFeatureLocked(r9);	 Catch:{ all -> 0x0268 }
        monitor-exit(r14);	 Catch:{ all -> 0x0268 }
        if (r11 == 0) goto L_0x02d1;
    L_0x0281:
        r13 = "ConnectivityManager";
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "starting startUsingNetworkFeature for request ";
        r14 = r14.append(r15);
        r14 = r14.append(r11);
        r14 = r14.toString();
        android.util.Log.d(r13, r14);
        r0 = r17;
        r13 = r0.mService;	 Catch:{ Exception -> 0x02b5 }
        r0 = r17;
        r14 = r0.legacyTypeForNetworkCapabilities(r9);	 Catch:{ Exception -> 0x02b5 }
        r10 = r13.getNetworkForType(r14);	 Catch:{ Exception -> 0x02b5 }
        if (r10 == 0) goto L_0x02ce;
    L_0x02aa:
        r13 = "ConnectivityManager";
        r14 = "return APN ALREADY ACTIVE";
        android.util.Log.d(r13, r14);	 Catch:{ Exception -> 0x02b5 }
        r13 = 0;
        goto L_0x005a;
    L_0x02b5:
        r4 = move-exception;
        r13 = "ConnectivityManager";
        r14 = new java.lang.StringBuilder;
        r14.<init>();
        r15 = "getNetworkForType ";
        r14 = r14.append(r15);
        r14 = r14.append(r4);
        r14 = r14.toString();
        android.util.Log.d(r13, r14);
    L_0x02ce:
        r13 = 1;
        goto L_0x005a;
    L_0x02d1:
        r13 = "ConnectivityManager";
        r14 = " request Failed";
        android.util.Log.d(r13, r14);
        r13 = 3;
        goto L_0x005a;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.ConnectivityManager.startUsingNetworkFeature(int, java.lang.String):int");
    }

    public int stopUsingNetworkFeature(int networkType, String feature) {
        checkLegacyRoutingApiAccess();
        NetworkCapabilities netCap = networkCapabilitiesForFeature(networkType, feature);
        if (netCap == null) {
            Log.d(TAG, "Can't satisfy stopUsingNetworkFeature for " + networkType + ", " + feature);
            return -1;
        }
        synchronized (sLegacyRequests) {
            LegacyRequest l = (LegacyRequest) sLegacyRequests.get(netCap);
            if (l != null) {
                Log.v(TAG, "replace netCap from sLegacyRequests");
                netCap = l.networkCapabilities;
            } else {
                Log.v(TAG, "not found netCap at sLegacyRequests");
            }
        }
        if (removeRequestForFeature(netCap)) {
            Log.d(TAG, "stopUsingNetworkFeature for " + networkType + ", " + feature);
        }
        return 1;
    }

    private NetworkCapabilities networkCapabilitiesForFeature(int networkType, String feature) {
        NetworkCapabilities netCap;
        if (networkType == 0) {
            int cap;
            if ("enableMMS".equals(feature)) {
                cap = 0;
            } else if ("enableXCAP".equals(feature)) {
                cap = 9;
            } else if ("enableMMS2".equals(feature)) {
                cap = 20;
            } else if ("enableSUPL".equals(feature)) {
                cap = 1;
            } else if ("enableDUN".equals(feature) || "enableDUNAlways".equals(feature)) {
                cap = 2;
            } else if ("enableHIPRI".equals(feature)) {
                cap = 12;
            } else if ("enableFOTA".equals(feature)) {
                cap = 3;
            } else if ("enableIMS".equals(feature)) {
                cap = 4;
            } else if ("enableCBS".equals(feature)) {
                cap = 5;
            } else if ("enableBIP".equals(feature)) {
                cap = 21;
            } else if ("enableEIMS".equals(feature)) {
                cap = 10;
            } else if (!"enable800APN".equals(feature)) {
                return null;
            } else {
                String configNetworkTypeCapability = CscFeature.getInstance().getString("CscFeature_RIL_ConfigNetworkTypeCapability");
                if (configNetworkTypeCapability != null && configNetworkTypeCapability.startsWith("VZW-")) {
                    try {
                        this.mService.enforceVzw800ApnPermission(Binder.getCallingUid());
                    } catch (RemoteException e) {
                        return null;
                    }
                }
                cap = 18;
            }
            netCap = new NetworkCapabilities();
            netCap.addTransportType(0).addCapability(cap);
            netCap.maybeMarkCapabilitiesRestricted();
            return netCap;
        } else if (networkType != 1 || !"p2p".equals(feature)) {
            return null;
        } else {
            netCap = new NetworkCapabilities();
            netCap.addTransportType(1);
            netCap.addCapability(6);
            netCap.maybeMarkCapabilitiesRestricted();
            return netCap;
        }
    }

    private int inferLegacyTypeForNetworkCapabilities(NetworkCapabilities netCap) {
        if (netCap == null) {
            return -1;
        }
        if (!netCap.hasTransport(0)) {
            return -1;
        }
        String type = null;
        int result = -1;
        if (netCap.hasCapability(5)) {
            type = "enableCBS";
            result = 12;
        }
        if (netCap.hasCapability(9)) {
            type = "enableXCAP";
            result = 27;
        }
        if (netCap.hasCapability(4)) {
            type = "enableIMS";
            result = 11;
        }
        if (netCap.hasCapability(3)) {
            type = "enableFOTA";
            result = 10;
        }
        if (netCap.hasCapability(2)) {
            type = "enableDUN";
            result = 4;
        }
        if (netCap.hasCapability(1)) {
            type = "enableSUPL";
            result = 3;
        }
        if (netCap.hasCapability(0)) {
            type = "enableMMS";
            result = 2;
        }
        if (netCap.hasCapability(20)) {
            type = "enableMMS2";
            result = 26;
        }
        if (netCap.hasCapability(12)) {
            type = "enableHIPRI";
            result = 5;
        }
        if (netCap.hasCapability(21)) {
            type = "enableBIP";
            result = 23;
        }
        if (netCap.hasCapability(10)) {
            type = "enableEIMS";
            result = 15;
        }
        if (type != null) {
            NetworkCapabilities testCap = networkCapabilitiesForFeature(0, type);
            if (testCap.equalsNetCapabilities(netCap) && testCap.equalsTransportTypes(netCap)) {
                return result;
            }
        }
        return -1;
    }

    private int legacyTypeForNetworkCapabilities(NetworkCapabilities netCap) {
        if (netCap == null) {
            return -1;
        }
        if (netCap.hasCapability(5)) {
            return 12;
        }
        if (netCap.hasCapability(9)) {
            return 27;
        }
        if (netCap.hasCapability(4)) {
            return 11;
        }
        if (netCap.hasCapability(3)) {
            return 10;
        }
        if (netCap.hasCapability(2)) {
            return 4;
        }
        if (netCap.hasCapability(1)) {
            return 3;
        }
        if (netCap.hasCapability(0)) {
            return 2;
        }
        if (netCap.hasCapability(20)) {
            return 26;
        }
        if (netCap.hasCapability(12)) {
            return 5;
        }
        if (netCap.hasCapability(6)) {
            return 13;
        }
        if (netCap.hasCapability(21)) {
            return 23;
        }
        if (netCap.hasCapability(10)) {
            return 15;
        }
        return -1;
    }

    private NetworkRequest findRequestForFeature(NetworkCapabilities netCap) {
        synchronized (sLegacyRequests) {
            LegacyRequest l = (LegacyRequest) sLegacyRequests.get(netCap);
            if (l != null) {
                NetworkRequest networkRequest = l.networkRequest;
                return networkRequest;
            }
            return null;
        }
    }

    private void renewRequestLocked(LegacyRequest l) {
        l.expireSequenceNumber++;
        Log.d(TAG, "renewing request to seqNum " + l.expireSequenceNumber);
        sendExpireMsgForFeature(l.networkCapabilities, l.expireSequenceNumber, l.delay);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void expireRequest(android.net.NetworkCapabilities r7, int r8) {
        /*
        r6 = this;
        r1 = -1;
        r3 = sLegacyRequests;
        monitor-enter(r3);
        r2 = sLegacyRequests;	 Catch:{ all -> 0x0057 }
        r0 = r2.get(r7);	 Catch:{ all -> 0x0057 }
        r0 = (android.net.ConnectivityManager.LegacyRequest) r0;	 Catch:{ all -> 0x0057 }
        if (r0 != 0) goto L_0x0010;
    L_0x000e:
        monitor-exit(r3);	 Catch:{ all -> 0x0057 }
    L_0x000f:
        return;
    L_0x0010:
        r2 = "ConnectivityManager";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0057 }
        r4.<init>();	 Catch:{ all -> 0x0057 }
        r5 = "expireRequest NetworkRequest: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0057 }
        r5 = r0.networkRequest;	 Catch:{ all -> 0x0057 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0057 }
        r4 = r4.toString();	 Catch:{ all -> 0x0057 }
        android.util.Log.d(r2, r4);	 Catch:{ all -> 0x0057 }
        r1 = r0.expireSequenceNumber;	 Catch:{ all -> 0x0057 }
        r2 = r0.expireSequenceNumber;	 Catch:{ all -> 0x0057 }
        if (r2 != r8) goto L_0x0033;
    L_0x0030:
        r6.removeRequestForFeature(r7);	 Catch:{ all -> 0x0057 }
    L_0x0033:
        monitor-exit(r3);	 Catch:{ all -> 0x0057 }
        r2 = "ConnectivityManager";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "expireRequest with ";
        r3 = r3.append(r4);
        r3 = r3.append(r1);
        r4 = ", ";
        r3 = r3.append(r4);
        r3 = r3.append(r8);
        r3 = r3.toString();
        android.util.Log.d(r2, r3);
        goto L_0x000f;
    L_0x0057:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0057 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.ConnectivityManager.expireRequest(android.net.NetworkCapabilities, int):void");
    }

    private NetworkRequest requestNetworkForFeatureLocked(NetworkCapabilities netCap) {
        int delay = -1;
        int type = legacyTypeForNetworkCapabilities(netCap);
        try {
            delay = this.mService.getRestoreDefaultNetworkDelay(type);
        } catch (RemoteException e) {
        }
        LegacyRequest l = new LegacyRequest();
        l.networkCapabilities = netCap;
        l.delay = delay;
        l.expireSequenceNumber = 0;
        l.networkRequest = sendRequestForNetwork(netCap, l.networkCallback, 0, 2, type);
        if (l.networkRequest == null) {
            return null;
        }
        sLegacyRequests.put(netCap, l);
        sendExpireMsgForFeature(netCap, l.expireSequenceNumber, delay);
        return l.networkRequest;
    }

    private void sendExpireMsgForFeature(NetworkCapabilities netCap, int seqNum, int delay) {
        if (delay >= 0) {
            Log.d(TAG, "sending expire msg with seqNum " + seqNum + " and delay " + delay);
            sCallbackHandler.sendMessageDelayed(sCallbackHandler.obtainMessage(EXPIRE_LEGACY_REQUEST, seqNum, 0, netCap), (long) delay);
        }
    }

    private boolean removeRequestForFeature(NetworkCapabilities netCap) {
        synchronized (sLegacyRequests) {
            LegacyRequest l = (LegacyRequest) sLegacyRequests.remove(netCap);
        }
        if (l == null) {
            return false;
        }
        unregisterNetworkCallback(l.networkCallback);
        l.clearDnsBinding();
        return true;
    }

    public PacketKeepalive startNattKeepalive(Network network, int intervalSeconds, PacketKeepaliveCallback callback, InetAddress srcAddr, int srcPort, InetAddress dstAddr) {
        PacketKeepalive k = new PacketKeepalive(network, callback);
        try {
            this.mService.startNattKeepalive(network, intervalSeconds, k.mMessenger, new Binder(), srcAddr.getHostAddress(), srcPort, dstAddr.getHostAddress());
            return k;
        } catch (RemoteException e) {
            Log.e(TAG, "Error starting packet keepalive: ", e);
            k.stopLooper();
            return null;
        }
    }

    public boolean requestRouteToHost(int networkType, int hostAddress) {
        return requestRouteToHostAddress(networkType, NetworkUtils.intToInetAddress(hostAddress));
    }

    public boolean requestRouteToHostAddress(int networkType, InetAddress hostAddress) {
        checkLegacyRoutingApiAccess();
        try {
            return this.mService.requestRouteToHostAddress(networkType, hostAddress.getAddress());
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean removeRouteToHostAddress(int networkType, InetAddress hostAddress) {
        try {
            return this.mService.removeRouteToHostAddress(networkType, hostAddress.getAddress());
        } catch (RemoteException e) {
            return false;
        }
    }

    @Deprecated
    public boolean getBackgroundDataSetting() {
        return true;
    }

    @Deprecated
    public void setBackgroundDataSetting(boolean allowBackgroundData) {
    }

    public NetworkQuotaInfo getActiveNetworkQuotaInfo() {
        try {
            return this.mService.getActiveNetworkQuotaInfo();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setMobileDataEnabled(boolean enabled) {
        IBinder b = ServiceManager.getService("phone");
        if (b != null) {
            try {
                ITelephony it = Stub.asInterface(b);
                int subId = MultiSimManager.getDefaultSubscriptionId(3);
                if (!SubscriptionManager.isValidSubscriptionId(subId)) {
                    subId = MultiSimManager.getDefaultSubscriptionId(0);
                }
                Log.d(TAG, "setMobileDataEnabled()+ subId=" + subId);
                it.setDataEnabled(subId, enabled);
            } catch (RemoteException e) {
            }
        }
    }

    public boolean getMobileDataEnabled() {
        IBinder b = ServiceManager.getService("phone");
        if (b != null) {
            try {
                ITelephony it = Stub.asInterface(b);
                int subId = MultiSimManager.getDefaultSubscriptionId(3);
                if (!SubscriptionManager.isValidSubscriptionId(subId)) {
                    subId = MultiSimManager.getDefaultSubscriptionId(0);
                }
                Log.d(TAG, "getMobileDataEnabled()+ subId=" + subId);
                boolean retVal = it.getDataEnabled(subId);
                Log.d(TAG, "getMobileDataEnabled()- subId=" + subId + " retVal=" + retVal);
                return retVal;
            } catch (RemoteException e) {
            }
        }
        Log.d(TAG, "getMobileDataEnabled()- remote exception retVal=false");
        return false;
    }

    private INetworkManagementService getNetworkManagementService() {
        INetworkManagementService iNetworkManagementService;
        synchronized (this) {
            if (this.mNMService != null) {
                iNetworkManagementService = this.mNMService;
            } else {
                this.mNMService = INetworkManagementService.Stub.asInterface(ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE));
                iNetworkManagementService = this.mNMService;
            }
        }
        return iNetworkManagementService;
    }

    public void addDefaultNetworkActiveListener(final OnNetworkActiveListener l) {
        INetworkActivityListener rl = new INetworkActivityListener.Stub() {
            public void onNetworkActive() throws RemoteException {
                l.onNetworkActive();
            }
        };
        try {
            getNetworkManagementService().registerNetworkActivityListener(rl);
            this.mNetworkActivityListeners.put(l, rl);
        } catch (RemoteException e) {
        }
    }

    public void removeDefaultNetworkActiveListener(OnNetworkActiveListener l) {
        INetworkActivityListener rl = (INetworkActivityListener) this.mNetworkActivityListeners.get(l);
        if (rl == null) {
            throw new IllegalArgumentException("Listener not registered: " + l);
        }
        try {
            getNetworkManagementService().unregisterNetworkActivityListener(rl);
        } catch (RemoteException e) {
        }
    }

    public boolean isDefaultNetworkActive() {
        try {
            return getNetworkManagementService().isNetworkActive();
        } catch (RemoteException e) {
            return false;
        }
    }

    public ConnectivityManager(Context context, IConnectivityManager service) {
        this.mContext = (Context) Preconditions.checkNotNull(context, "missing context");
        this.mService = (IConnectivityManager) Preconditions.checkNotNull(service, "missing IConnectivityManager");
        sInstance = this;
    }

    public static ConnectivityManager from(Context context) {
        return (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static final void enforceChangePermission(Context context) {
        int uid = UserHandle.getAppId(Binder.getCallingUid());
        Log.d(TAG, "enforceChangePermission: calling uid (" + uid + ")");
        Settings.checkAndNoteChangeNetworkStateOperation(context, uid, Settings.getPackageNameForUid(context, uid), uid != 1000);
    }

    public static final void enforceTetherChangePermission(Context context) {
        WifiManager mWifiManager = (WifiManager) context.getSystemService("wifi");
        Message msg = new Message();
        msg.what = 28;
        int mRvfMode = mWifiManager.callSECApi(msg);
        Log.e(TAG, "ConnectivityManager mRvfMode: " + mRvfMode);
        if (context.getResources().getStringArray(17235989).length == 2 && mRvfMode == 0) {
            context.enforceCallingOrSelfPermission(permission.CONNECTIVITY_INTERNAL, "ConnectivityService");
            return;
        }
        int uid = UserHandle.getAppId(Binder.getCallingUid());
        Log.d(TAG, "enforceTetherChangePermission: calling uid (" + uid + ")");
        Settings.checkAndNoteWriteSettingsOperation(context, uid, Settings.getPackageNameForUid(context, uid), uid != 1000);
    }

    static ConnectivityManager getInstanceOrNull() {
        return sInstance;
    }

    private static ConnectivityManager getInstance() {
        if (getInstanceOrNull() != null) {
            return getInstanceOrNull();
        }
        throw new IllegalStateException("No ConnectivityManager yet constructed");
    }

    public String[] getTetherableIfaces() {
        try {
            return this.mService.getTetherableIfaces();
        } catch (RemoteException e) {
            return new String[0];
        }
    }

    public String[] getTetheredIfaces() {
        try {
            return this.mService.getTetheredIfaces();
        } catch (RemoteException e) {
            return new String[0];
        }
    }

    public String[] getTetheringErroredIfaces() {
        try {
            return this.mService.getTetheringErroredIfaces();
        } catch (RemoteException e) {
            return new String[0];
        }
    }

    public String[] getTetheredDhcpRanges() {
        try {
            return this.mService.getTetheredDhcpRanges();
        } catch (RemoteException e) {
            return new String[0];
        }
    }

    public int tether(String iface) {
        try {
            return this.mService.tether(iface);
        } catch (RemoteException e) {
            return 2;
        }
    }

    public int untether(String iface) {
        try {
            return this.mService.untether(iface);
        } catch (RemoteException e) {
            return 2;
        }
    }

    public boolean isTetheringSupported() {
        try {
            return this.mService.isTetheringSupported();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isSplitBillingEnabled() {
        try {
            return this.mService.isSplitBillingEnabled();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isEntApnEnabled(int connectionType) {
        try {
            return this.mService.isEntApnEnabled(connectionType);
        } catch (RemoteException e) {
            return false;
        }
    }

    public int[] getUidsForApnType(String apn) {
        try {
            return this.mService.getUidsForApnType(apn);
        } catch (RemoteException e) {
            return null;
        }
    }

    public int[] getUsersForEnterpriseNetwork(int networkType) {
        try {
            return this.mService.getUsersForEnterpriseNetwork(networkType);
        } catch (RemoteException e) {
            return null;
        }
    }

    public String[] getTetherableUsbRegexs() {
        try {
            return this.mService.getTetherableUsbRegexs();
        } catch (RemoteException e) {
            return new String[0];
        }
    }

    public String[] getTetherableWifiRegexs() {
        try {
            return this.mService.getTetherableWifiRegexs();
        } catch (RemoteException e) {
            return new String[0];
        }
    }

    public String[] getTetherableBluetoothRegexs() {
        try {
            return this.mService.getTetherableBluetoothRegexs();
        } catch (RemoteException e) {
            return new String[0];
        }
    }

    public int setUsbTethering(boolean enable) {
        try {
            return this.mService.setUsbTethering(enable);
        } catch (RemoteException e) {
            return 2;
        }
    }

    public int setNcmTethering(boolean enable) {
        try {
            return this.mService.setNcmTethering(enable);
        } catch (RemoteException e) {
            return 2;
        }
    }

    public int getLastTetherError(String iface) {
        try {
            return this.mService.getLastTetherError(iface);
        } catch (RemoteException e) {
            return 2;
        }
    }

    public void reportInetCondition(int networkType, int percentage) {
        try {
            this.mService.reportInetCondition(networkType, percentage);
        } catch (RemoteException e) {
        }
    }

    public void reportBadNetwork(Network network) {
        try {
            this.mService.reportNetworkConnectivity(network, true);
            this.mService.reportNetworkConnectivity(network, false);
        } catch (RemoteException e) {
        }
    }

    public void reportNetworkConnectivity(Network network, boolean hasConnectivity) {
        try {
            this.mService.reportNetworkConnectivity(network, hasConnectivity);
        } catch (RemoteException e) {
        }
    }

    public void setGlobalProxy(ProxyInfo p) {
        try {
            this.mService.setGlobalProxy(p);
        } catch (RemoteException e) {
        }
    }

    public ProxyInfo getGlobalProxy() {
        try {
            return this.mService.getGlobalProxy();
        } catch (RemoteException e) {
            return null;
        }
    }

    public ProxyInfo getProxyForNetwork(Network network) {
        try {
            return this.mService.getProxyForNetwork(network);
        } catch (RemoteException e) {
            return null;
        }
    }

    public ProxyInfo getDefaultProxy() {
        return getProxyForNetwork(getBoundNetworkForProcess());
    }

    public boolean isNetworkSupported(int networkType) {
        try {
            return this.mService.isNetworkSupported(networkType);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isActiveNetworkMetered() {
        try {
            return this.mService.isActiveNetworkMetered();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isMobilePolicyDataEnable() {
        try {
            return this.mService.isMobilePolicyDataEnable();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean updateLockdownVpn() {
        try {
            return this.mService.updateLockdownVpn();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean prepareVpn(String oldPackage, String newPackage, int userId) {
        try {
            return this.mService.prepareVpn(oldPackage, newPackage, userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public int checkMobileProvisioning(int suggestedTimeOutMs) {
        int timeOutMs = -1;
        try {
            timeOutMs = this.mService.checkMobileProvisioning(suggestedTimeOutMs);
        } catch (RemoteException e) {
        }
        return timeOutMs;
    }

    public String getMobileProvisioningUrl() {
        try {
            return this.mService.getMobileProvisioningUrl();
        } catch (RemoteException e) {
            return null;
        }
    }

    public DhcpServerConfiguration getDhcpServerConfiguration() {
        try {
            return this.mService.getDhcpServerConfiguration();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean saveDhcpServerConfiguration(DhcpServerConfiguration serverConfig) {
        try {
            this.mService.saveDhcpServerConfiguration(serverConfig);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setProvisioningNotificationVisible(boolean visible, int networkType, String action) {
        try {
            this.mService.setProvisioningNotificationVisible(visible, networkType, action);
        } catch (RemoteException e) {
        }
    }

    public void setAirplaneMode(boolean enable) {
        try {
            this.mService.setAirplaneMode(enable);
        } catch (RemoteException e) {
        }
    }

    public void registerNetworkFactory(Messenger messenger, String name) {
        try {
            this.mService.registerNetworkFactory(messenger, name);
        } catch (RemoteException e) {
        }
    }

    public void unregisterNetworkFactory(Messenger messenger) {
        try {
            this.mService.unregisterNetworkFactory(messenger);
        } catch (RemoteException e) {
        }
    }

    public int registerNetworkAgent(Messenger messenger, NetworkInfo ni, LinkProperties lp, NetworkCapabilities nc, int score, NetworkMisc misc) {
        try {
            return this.mService.registerNetworkAgent(messenger, ni, lp, nc, score, misc);
        } catch (RemoteException e) {
            return 0;
        }
    }

    private void incCallbackHandlerRefCount() {
        synchronized (sCallbackRefCount) {
            if (sCallbackRefCount.incrementAndGet() == 1) {
                HandlerThread callbackThread = new HandlerThread(TAG);
                callbackThread.start();
                sCallbackHandler = new CallbackHandler(callbackThread.getLooper(), sNetworkCallback, sCallbackRefCount, this);
            }
        }
    }

    private void decCallbackHandlerRefCount() {
        synchronized (sCallbackRefCount) {
            if (sCallbackRefCount.decrementAndGet() == 0) {
                sCallbackHandler.obtainMessage(CALLBACK_EXIT).sendToTarget();
                sCallbackHandler = null;
            }
        }
    }

    private NetworkRequest sendRequestForNetwork(NetworkCapabilities need, NetworkCallback networkCallback, int timeoutSec, int action, int legacyType) {
        if (networkCallback == null) {
            throw new IllegalArgumentException("null NetworkCallback");
        } else if (need == null) {
            throw new IllegalArgumentException("null NetworkCapabilities");
        } else {
            try {
                Log.d(TAG, "requestNetwork; getAppId(CallingUid) : " + UserHandle.getAppId(Binder.getCallingUid()) + ", CallingPid : " + Binder.getCallingPid());
                if (CscFeature.getInstance().getString("CscFeature_Common_ConfigLocalSecurityPolicy").equals("ChinaNalSecurity")) {
                    Log.d(TAG, "ChinaNalSecurity is true.");
                    if (need.hasCapability(0)) {
                        String callingPackage = ActivityThread.currentPackageName();
                        int callerId = UserHandle.getAppId(Binder.getCallingUid());
                        String blackUIDs = System.getString(this.mContext.getContentResolver(), "send_mms_block_list");
                        if (blackUIDs != null) {
                            Log.d(TAG, "blackUIDs is " + blackUIDs);
                            for (String blackUID : blackUIDs.split(" ")) {
                                Log.d(TAG, "blackUID is " + blackUID);
                                if (blackUID.contains(String.valueOf(callerId))) {
                                    Log.d(TAG, "ChinaNalSecurity; OP_SEND_MMS  is denied.Throw IOexception Cancel to connect MMS connection. Caller is " + callingPackage);
                                    return networkCallback.networkRequest;
                                }
                            }
                        } else {
                            Log.d(TAG, "blackUIDs is null. It didn't check block list.");
                        }
                    }
                }
                incCallbackHandlerRefCount();
                synchronized (sNetworkCallback) {
                    if (action == 1) {
                        networkCallback.networkRequest = this.mService.listenForNetwork(need, new Messenger(sCallbackHandler), new Binder());
                    } else {
                        networkCallback.networkRequest = this.mService.requestNetwork(need, new Messenger(sCallbackHandler), timeoutSec, new Binder(), legacyType);
                    }
                    if (networkCallback.networkRequest != null) {
                        sNetworkCallback.put(networkCallback.networkRequest, networkCallback);
                    }
                }
            } catch (RemoteException e) {
            }
            if (networkCallback.networkRequest == null) {
                decCallbackHandlerRefCount();
            }
            return networkCallback.networkRequest;
        }
    }

    public void requestNetwork(NetworkRequest request, NetworkCallback networkCallback) {
        sendRequestForNetwork(request.networkCapabilities, networkCallback, 0, 2, inferLegacyTypeForNetworkCapabilities(request.networkCapabilities));
    }

    public void requestNetwork(NetworkRequest request, NetworkCallback networkCallback, int timeoutMs) {
        sendRequestForNetwork(request.networkCapabilities, networkCallback, timeoutMs, 2, inferLegacyTypeForNetworkCapabilities(request.networkCapabilities));
    }

    public void requestNetwork(NetworkRequest request, PendingIntent operation) {
        checkPendingIntent(operation);
        try {
            this.mService.pendingRequestForNetwork(request.networkCapabilities, operation);
        } catch (RemoteException e) {
        }
    }

    public void releaseNetworkRequest(PendingIntent operation) {
        checkPendingIntent(operation);
        try {
            Log.d(TAG, "releaseNetwork; CallingUid : " + Binder.getCallingUid() + ", CallingPid : " + Binder.getCallingPid());
            this.mService.releasePendingNetworkRequest(operation);
        } catch (RemoteException e) {
        }
    }

    private void checkPendingIntent(PendingIntent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("PendingIntent cannot be null.");
        }
    }

    public void registerNetworkCallback(NetworkRequest request, NetworkCallback networkCallback) {
        sendRequestForNetwork(request.networkCapabilities, networkCallback, 0, 1, -1);
    }

    public void registerNetworkCallback(NetworkRequest request, PendingIntent operation) {
        checkPendingIntent(operation);
        try {
            this.mService.pendingListenForNetwork(request.networkCapabilities, operation);
        } catch (RemoteException e) {
        }
    }

    public boolean requestBandwidthUpdate(Network network) {
        try {
            return this.mService.requestBandwidthUpdate(network);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void unregisterNetworkCallback(NetworkCallback networkCallback) {
        if (networkCallback == null || networkCallback.networkRequest == null || networkCallback.networkRequest.requestId == 0) {
            throw new IllegalArgumentException("Invalid NetworkCallback");
        }
        try {
            Log.d(TAG, "unregisterNetworkCallback; CallingUid : " + Binder.getCallingUid() + ", CallingPid : " + Binder.getCallingPid());
            this.mService.releaseNetworkRequest(networkCallback.networkRequest);
        } catch (RemoteException e) {
        }
    }

    public void unregisterNetworkCallback(PendingIntent operation) {
        releaseNetworkRequest(operation);
    }

    public void setAcceptUnvalidated(Network network, boolean accept, boolean always) {
        try {
            this.mService.setAcceptUnvalidated(network, accept, always);
        } catch (RemoteException e) {
        }
    }

    public void factoryReset() {
        try {
            this.mService.factoryReset();
        } catch (RemoteException e) {
        }
    }

    public boolean bindProcessToNetwork(Network network) {
        return setProcessDefaultNetwork(network);
    }

    public static boolean setProcessDefaultNetwork(Network network) {
        int netId = network == null ? 0 : network.netId;
        if (netId == NetworkUtils.getBoundNetworkForProcess()) {
            return true;
        }
        if (!NetworkUtils.bindProcessToNetwork(netId)) {
            return false;
        }
        try {
            Proxy.setHttpProxySystemProperty(getInstance().getDefaultProxy());
        } catch (SecurityException e) {
            Log.e(TAG, "Can't set proxy properties", e);
        }
        InetAddress.clearDnsCache();
        NetworkEventDispatcher.getInstance().onNetworkConfigurationChanged();
        return true;
    }

    public Network getBoundNetworkForProcess() {
        return getProcessDefaultNetwork();
    }

    public static Network getProcessDefaultNetwork() {
        int netId = NetworkUtils.getBoundNetworkForProcess();
        if (netId == 0) {
            return null;
        }
        return new Network(netId);
    }

    private void unsupportedStartingFrom(int version) {
        if (Process.myUid() != 1000 && this.mContext.getApplicationInfo().targetSdkVersion >= version) {
            throw new UnsupportedOperationException("This method is not supported in target SDK version " + version + " and above");
        }
    }

    private void checkLegacyRoutingApiAccess() {
        if (this.mContext.checkCallingOrSelfPermission("com.android.permission.INJECT_OMADM_SETTINGS") != 0) {
            unsupportedStartingFrom(23);
        }
    }

    public static boolean setProcessDefaultNetworkForHostResolution(Network network) {
        return NetworkUtils.bindProcessToNetworkForHostResolution(network == null ? 0 : network.netId);
    }

    public void tagSocket(ParcelFileDescriptor pfd, int uid, int tag) {
        try {
            Log.d(TAG, "in connectivity manager pfd " + pfd + " uid" + uid + " tag" + tag);
            this.mService.tagSocket(pfd, uid, tag);
        } catch (RemoteException e) {
            Log.d(TAG, "Exception while Tag Socket");
        }
    }

    public void setRoamingReduction(int uid, int policy) {
        try {
            this.mService.setRoamingReduction(uid, policy);
        } catch (RemoteException e) {
        }
    }

    public int getRoamingReduction(int uid) {
        int policy = 0;
        try {
            policy = this.mService.getRoamingReduction(uid);
        } catch (RemoteException e) {
        }
        return policy;
    }
}
