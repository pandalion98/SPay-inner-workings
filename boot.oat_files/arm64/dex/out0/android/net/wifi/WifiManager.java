package android.net.wifi;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.admin.DevicePolicyManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.AudioParameter;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.net.NetworkRequest.Builder;
import android.net.ProxyInfo;
import android.net.wifi.hs20.WifiHs20OsuProvider;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.WorkSource;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.AsyncChannel;
import com.sec.android.app.CscFeature;
import java.io.File;
import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class WifiManager {
    public static final String ACTION_FRAME_VS_WES_RECEIVED_ACTION = "android.net.wifi.ACTION_FRAME_VS_WES_RECEIVED_ACTION";
    public static final String ACTION_PICK_WIFI_NETWORK = "android.net.wifi.PICK_WIFI_NETWORK";
    public static final String ACTION_REQUEST_SCAN_ALWAYS_AVAILABLE = "android.net.wifi.action.REQUEST_SCAN_ALWAYS_AVAILABLE";
    public static final String ACTION_SEC_NETWORK_STATE_CHANGED = "com.samsung.android.net.wifi.SEC_NETWORK_STATE_CHANGED";
    public static final String ACTION_SEC_NOTIFICATION_CANCEL = "com.samsung.android.net.wifi.SEC_NOTIFICATION_CANCEL";
    public static final String ACTION_SEC_PICK_WIFI_NETWORK = "com.samsung.android.net.wifi.SEC_PICK_WIFI_NETWORK";
    public static final String ACTION_SEC_PICK_WIFI_NETWORK_WITH_DIALOG = "com.samsung.android.net.wifi.SEC_PICK_WIFI_NETWORK_WITH_DIALOG";
    public static final String ACTION_SEC_PICK_WIFI_NETWORK_WITH_ON = "com.samsung.android.net.wifi.SEC_PICK_WIFI_NETWORK_WITH_ON";
    public static final String ACTION_SEC_VZW_NOTIFICATION_CANCEL = "android.net.wifi.SEC_VZW_NOTIFICATION_CANCEL";
    public static final String ACTION_SEC_VZW_NOTIFICATION_CONNECT = "android.net.wifi.SEC_VZW_NOTIFICATION_CONNECT";
    public static final int AP_OPEN_TYPE_CAPTIVE = 3;
    public static final int AP_OPEN_TYPE_INTERNET = 2;
    public static final int AP_OPEN_TYPE_NONE = 0;
    public static final int AP_OPEN_TYPE_NOT_ABLE_TO_CONNECT = 4;
    public static final int AP_OPEN_TYPE_NO_INTERNET = 1;
    private static final int BASE = 151552;
    public static final String BATCHED_SCAN_RESULTS_AVAILABLE_ACTION = "android.net.wifi.BATCHED_RESULTS";
    public static final int BUSY = 2;
    public static final int CANCEL_WPS = 151566;
    public static final int CANCEL_WPS_FAILED = 151567;
    public static final int CANCEL_WPS_SUCCEDED = 151568;
    public static final String CAPTIVE_PORTAL_AUTHENTICATED = "com.samsung.android.net.wifi.CAPTIVE_PORTAL_AUTHENTICATED";
    public static final String CAPTIVE_PORTAL_DETECTED = "com.samsung.android.net.wifi.CAPTIVE_PORTAL_DETECTED";
    public static final int CHANGE_REASON_ADDED = 0;
    public static final int CHANGE_REASON_CONFIG_CHANGE = 2;
    public static final int CHANGE_REASON_REMOVED = 1;
    public static final String CONFIGOPBRANDINGFORMOBILEAP = WifiApCust.mMHSCustomer;
    public static final String CONFIGURED_NETWORKS_CHANGED_ACTION = "android.net.wifi.CONFIGURED_NETWORKS_CHANGE";
    public static final int CONNECT_MODE_INTRANET_ONLY = 1;
    public static final int CONNECT_MODE_NORMAL = 0;
    public static final int CONNECT_NETWORK = 151553;
    public static final int CONNECT_NETWORK_FAILED = 151554;
    public static final int CONNECT_NETWORK_SUCCEEDED = 151555;
    public static final int DATA_ACTIVITY_IN = 1;
    public static final int DATA_ACTIVITY_INOUT = 3;
    public static final int DATA_ACTIVITY_NONE = 0;
    public static final int DATA_ACTIVITY_NOTIFICATION = 1;
    public static final int DATA_ACTIVITY_OUT = 2;
    private static final boolean DBG = (Debug.isProductShip() != 1);
    private static final boolean DBGMHS;
    public static final boolean DEFAULT_POOR_NETWORK_AVOIDANCE_ENABLED = false;
    public static final int DHCP_FAILURE_ERROR = 2;
    public static final int DISABLE_NETWORK = 151569;
    public static final int DISABLE_NETWORK_FAILED = 151570;
    public static final int DISABLE_NETWORK_SUCCEEDED = 151571;
    public static final int DRIVER_ERROR = 10;
    public static final int ERROR = 0;
    public static final String ERROR_ACTION = "android.net.wifi.ERROR";
    public static final int ERROR_AUTHENTICATING = 1;
    public static final String EXTRA_ACTION_FRAME_VS_WES_DATA = "ActionFrameVSWESData";
    public static final String EXTRA_BSSID = "bssid";
    public static final String EXTRA_CALLED_DIALOG = "called_dialog";
    public static final String EXTRA_CHANGE_REASON = "changeReason";
    public static final String EXTRA_ERROR_CODE = "errorCode";
    public static final String EXTRA_HS20_STATE = "hs20_state";
    public static final String EXTRA_LINK_PROPERTIES = "linkProperties";
    public static final String EXTRA_MULTIPLE_NETWORKS_CHANGED = "multipleChanges";
    public static final String EXTRA_NETWORK_CAPABILITIES = "networkCapabilities";
    public static final String EXTRA_NETWORK_INFO = "networkInfo";
    public static final String EXTRA_NETWORK_RECONNECT = "network_reconnect";
    public static final String EXTRA_NEW_RSSI = "newRssi";
    public static final String EXTRA_NEW_STATE = "newState";
    public static final String EXTRA_OXYGEN_CONNECT_MODE = "connectMode";
    public static final String EXTRA_PREVIOUS_WIFI_AP_STATE = "previous_wifi_state";
    public static final String EXTRA_PREVIOUS_WIFI_STATE = "previous_wifi_state";
    public static final String EXTRA_RESULTS_UPDATED = "resultsUpdated";
    public static final String EXTRA_SCAN_AVAILABLE = "scan_enabled";
    public static final String EXTRA_SUPPLICANT_CONNECTED = "connected";
    public static final String EXTRA_SUPPLICANT_ERROR = "supplicantError";
    public static final String EXTRA_WIFI_AP_FAILURE_REASON = "wifi_ap_error_code";
    public static final String EXTRA_WIFI_AP_INTEFACE_STATE = "interface_state";
    public static final String EXTRA_WIFI_AP_STATE = "wifi_state";
    public static final String EXTRA_WIFI_CONFIGURATION = "wifiConfiguration";
    public static final String EXTRA_WIFI_CREDENTIAL_EVENT_TYPE = "et";
    public static final String EXTRA_WIFI_CREDENTIAL_SSID = "ssid";
    public static final String EXTRA_WIFI_INFO = "wifiInfo";
    public static final String EXTRA_WIFI_STATE = "wifi_state";
    public static final int FORGET_NETWORK = 151556;
    public static final int FORGET_NETWORK_FAILED = 151557;
    public static final int FORGET_NETWORK_SUCCEEDED = 151558;
    public static final String FREE_WIFI_SCAN_OPEN_AP_CHECK_RESULT = "android.net.wifi.OPEN_AP_CHECK_RESULT";
    public static final String FREE_WIFI_SCAN_OPEN_NETWORK_STATUS = "free_wifi_scan_open_network_status";
    public static final String HS20_DISABLED_COMPLETE_BY_CREDERROR_ACTION = "android.net.wifi.HS20_DISABLED_COMPLETE_BY_CREDERROR";
    public static final String HS20_EXPIRY_TIME_FOUND_ACTION = "android.net.wifi.HS20_EXPIRY_TIME_FOUND";
    public static final String HS20_PROVIDER_LIST_UPDATED = "android.net.wifi.HS20_PROVIDER_LIST_UPDATED";
    public static final int HS20_STATE_DISABLED = 20;
    public static final int HS20_STATE_ENABLED = 21;
    public static final int HS20_STATE_UNKNOWN = 22;
    public static final String HS20_WNM_DEAUTH_IMMINENT = "android.net.wifi.HS20_WNM_DEAUTH_IMMINENT";
    public static final int INVALID_ARGS = 8;
    public static final int INVALID_CRED_ID = -1;
    private static final int INVALID_KEY = 0;
    public static final int IN_PROGRESS = 1;
    public static final String KIES_VIA_WIFI_RUN_KIES = "android.net.wifi.RUN_KIES";
    public static final String KIES_VIA_WIFI_SET_HOME_AP = "android.net.wifi.SET_HOME_AP";
    public static final String LINK_CONFIGURATION_CHANGED_ACTION = "android.net.wifi.LINK_CONFIGURATION_CHANGED";
    private static final int MAX_ACTIVE_LOCKS = 50;
    private static final int MAX_RSSI = -55;
    private static final int MIN_RSSI = -100;
    public static final String NETWORK_IDS_CHANGED_ACTION = "android.net.wifi.NETWORK_IDS_CHANGED";
    public static final String NETWORK_OXYGEN_STATE_CHANGED_ACTION = "com.samsung.android.net.wifi.NETWORK_OXYGEN_STATE_CHANGE";
    public static final String NETWORK_STATE_CHANGED_ACTION = "android.net.wifi.STATE_CHANGE";
    public static final int NOTIFICATION_FOR_CAPTIVE_PORTAL_DISABLED = 13;
    public static final int NOTIFICATION_FOR_CAPTIVE_PORTAL_LOGIN = 12;
    public static final int NOTIFICATION_FOR_MALICIOUS_HOTSPOT_DETECTION = 17;
    public static final int NOTIFICATION_FOR_SCC_DIFF = 6;
    public static final int NOTIFICATION_FOR_SHARE_PROFILE_NETWORK_EXPIRED = 15;
    public static final int NOTIFICATION_FOR_SHARE_PROFILE_RECEPTION = 14;
    public static final int NOTIFICATION_FOR_SHARE_PROFILE_TRANSMISSION = 16;
    public static final int NOTIFICATION_FOR_SNS_EXCEPTION = 11;
    public static final int NOTIFICATION_FOR_SNS_POOR_CONNECTION = 9;
    public static final int NOTIFICATION_FOR_WWSM_PATCHER = 18;
    public static final int NOT_AUTHORIZED = 9;
    public static final int OLSRD_ERROR = 20;
    public static final String OXYGEN_SCAN_RESULTS_AVAILABLE_ACTION = "com.samsung.android.net.wifi.OXYGEN_SCAN_RESULTS";
    public static final String RSSI_CHANGED_ACTION = "android.net.wifi.RSSI_CHANGED";
    public static final int RSSI_LEVELS = 5;
    public static final int RSSI_PKTCNT_FETCH = 151572;
    public static final int RSSI_PKTCNT_FETCH_FAILED = 151574;
    public static final int RSSI_PKTCNT_FETCH_SUCCEEDED = 151573;
    public static final int SAP_START_FAILURE_GENERAL = 0;
    public static final int SAP_START_FAILURE_NO_CHANNEL = 1;
    public static final int SAVE_NETWORK = 151559;
    public static final int SAVE_NETWORK_FAILED = 151560;
    public static final int SAVE_NETWORK_SUCCEEDED = 151561;
    public static final String SCAN_RESULTS_AVAILABLE_ACTION = "android.net.wifi.SCAN_RESULTS";
    public static final int SEC_CMD_GET_PASSPOINT_STATE = 255;
    public static final int SEC_COMMAND_ID_ADD_CRED = 210;
    public static final int SEC_COMMAND_ID_ADD_HISTORICAL_DUMPLOG = 78;
    public static final int SEC_COMMAND_ID_ANS_EXCEPTION_ANSWER = 201;
    public static final int SEC_COMMAND_ID_AUTO_CONNECT = 1;
    public static final int SEC_COMMAND_ID_BACKUP_RESTORE = 61;
    public static final int SEC_COMMAND_ID_CHECK_SUPPORT_80211AC = 73;
    public static final int SEC_COMMAND_ID_CONTROL_SENSOR_MONITOR = 79;
    public static final int SEC_COMMAND_ID_DELAY_DISCONNECT_TRANSITION = 81;
    public static final int SEC_COMMAND_ID_DELETE_RAW_WIFI_PROFILE = 260;
    public static final int SEC_COMMAND_ID_DISABLE_NETWORK_WITH_REASON = 203;
    public static final int SEC_COMMAND_ID_DNS_RECHECK = 25;
    public static final int SEC_COMMAND_ID_FETCH_ANQP = 254;
    public static final int SEC_COMMAND_ID_GET_BAND = 162;
    public static final int SEC_COMMAND_ID_GET_CHAMELEON_ENABLED = 20;
    public static final int SEC_COMMAND_ID_GET_CHAMELEON_MAXUSER = 22;
    public static final int SEC_COMMAND_ID_GET_CHAMELEON_TETHEREDDATA = 21;
    public static final int SEC_COMMAND_ID_GET_COUNTRY_REV = 160;
    public static final int SEC_COMMAND_ID_GET_DFS_SCAN_MODE = 164;
    public static final int SEC_COMMAND_ID_GET_DHCP_RENEW_AFTER_ROAMING_MODE = 180;
    public static final int SEC_COMMAND_ID_GET_FWS_OPEN_AP_CHECK = 220;
    public static final int SEC_COMMAND_ID_GET_INTERNET_CHECK_OPTION = 34;
    public static final int SEC_COMMAND_ID_GET_PREFER_BAND = 30;
    public static final int SEC_COMMAND_ID_GET_RECONNECT = 200;
    public static final int SEC_COMMAND_ID_GET_ROAM_DELTA = 102;
    public static final int SEC_COMMAND_ID_GET_ROAM_SCAN_CHANNELS = 108;
    public static final int SEC_COMMAND_ID_GET_ROAM_SCAN_CONTROL = 106;
    public static final int SEC_COMMAND_ID_GET_ROAM_SCAN_PERIOD = 104;
    public static final int SEC_COMMAND_ID_GET_ROAM_TRIGGER = 100;
    public static final int SEC_COMMAND_ID_GET_SCAN_CHANNEL_TIME = 130;
    public static final int SEC_COMMAND_ID_GET_SCAN_CONTROLLER_SETTINGS = 223;
    public static final int SEC_COMMAND_ID_GET_SCAN_HOME_AWAY_TIME = 134;
    public static final int SEC_COMMAND_ID_GET_SCAN_HOME_TIME = 132;
    public static final int SEC_COMMAND_ID_GET_SCAN_NPROBES = 136;
    public static final int SEC_COMMAND_ID_GET_SCAN_RESULTS_EX = 33;
    public static final int SEC_COMMAND_ID_GET_SPECIAL_SSID = 258;
    public static final int SEC_COMMAND_ID_GET_WES_MODE = 170;
    public static final int SEC_COMMAND_ID_GET_WIFIAP_RVFMODE = 28;
    public static final int SEC_COMMAND_ID_GET_WIFIAP_STANUM = 3;
    public static final int SEC_COMMAND_ID_GET_WIFIAP_TEST_MODE = 191;
    public static final int SEC_COMMAND_ID_GET_WIFI_PASSPHRASE = 39;
    public static final int SEC_COMMAND_ID_HS20_CLEAR_BLACKLIST = 45;
    public static final int SEC_COMMAND_ID_HS20_ENABLE = 41;
    public static final int SEC_COMMAND_ID_HS20_FETCH_ANQP = 43;
    public static final int SEC_COMMAND_ID_HS20_INTERWORKING_SELECT = 42;
    public static final int SEC_COMMAND_ID_INIT = 0;
    public static final int SEC_COMMAND_ID_INIT_SCAN_INTERVAL = 2;
    public static final int SEC_COMMAND_ID_IS_IBSS_SUPPORTED = 62;
    public static final int SEC_COMMAND_ID_IW_ADD_NETWORK = 253;
    public static final int SEC_COMMAND_ID_KT_DISCONNECTION_PRIORITY = 84;
    public static final int SEC_COMMAND_ID_LIST_CRED = 207;
    public static final int SEC_COMMAND_ID_LOAD_CRED = 44;
    public static final int SEC_COMMAND_ID_LOGGING = 77;
    public static final int SEC_COMMAND_ID_LTECOEX = 63;
    public static final int SEC_COMMAND_ID_PARTIAL_SCAN = 36;
    public static final int SEC_COMMAND_ID_PRE_SCAN = 38;
    public static final int SEC_COMMAND_ID_PROFILE_BACKUP_RESTORE = 205;
    public static final int SEC_COMMAND_ID_READ_WHITELIST = 5;
    public static final int SEC_COMMAND_ID_REASSOC = 151;
    public static final int SEC_COMMAND_ID_RELOAD_HIDDEN_AP_LIST = 240;
    public static final int SEC_COMMAND_ID_RELOAD_SIM_STATE = 46;
    public static final int SEC_COMMAND_ID_REMOVED_CRED = 206;
    public static final int SEC_COMMAND_ID_REMOVE_CRED_ALL = 208;
    public static final int SEC_COMMAND_ID_RESET_CONFIGURATION = 242;
    public static final int SEC_COMMAND_ID_RESET_WIFIAP = 197;
    public static final int SEC_COMMAND_ID_RMC_ENABLE = 50;
    public static final int SEC_COMMAND_ID_RMC_TXRATE = 51;
    public static final int SEC_COMMAND_ID_SEC_RECONNECT_ENABLED = 83;
    public static final int SEC_COMMAND_ID_SEND_ACTION_FRAME = 150;
    public static final int SEC_COMMAND_ID_SEND_SELECTED_WIFI_PROFILE = 204;
    public static final int SEC_COMMAND_ID_SETTINGS_SOFT_RESET = 193;
    public static final int SEC_COMMAND_ID_SETWMMPS = 72;
    public static final int SEC_COMMAND_ID_SET_ALLOW_TO_CONNECT = 37;
    public static final int SEC_COMMAND_ID_SET_AMPDU_MPDU = 17;
    public static final int SEC_COMMAND_ID_SET_BAND = 163;
    public static final int SEC_COMMAND_ID_SET_CAPTIVE_PORTAL_LOGIN_URL = 257;
    public static final int SEC_COMMAND_ID_SET_CAPTIVE_PORTAL_VAR = 256;
    public static final int SEC_COMMAND_ID_SET_COUNTRYCODE = 55;
    public static final int SEC_COMMAND_ID_SET_COUNTRY_REV = 161;
    public static final int SEC_COMMAND_ID_SET_CRED = 209;
    public static final int SEC_COMMAND_ID_SET_DFS_SCAN_MODE = 165;
    public static final int SEC_COMMAND_ID_SET_DHCP_RENEW_AFTER_ROAMING_MODE = 181;
    public static final int SEC_COMMAND_ID_SET_FWS_OPEN_AP_CHECK = 221;
    public static final int SEC_COMMAND_ID_SET_IBSS_AMPDU = 52;
    public static final int SEC_COMMAND_ID_SET_IBSS_ANTENNA_MODE = 53;
    public static final int SEC_COMMAND_ID_SET_IMS_RSSI_POLL_STATE = 24;
    public static final int SEC_COMMAND_ID_SET_INTERNET_CHECK_OPTION = 35;
    public static final int SEC_COMMAND_ID_SET_MINIMIZE_RETRY = 19;
    public static final int SEC_COMMAND_ID_SET_PREFER_BAND = 31;
    public static final int SEC_COMMAND_ID_SET_ROAM_DELTA = 103;
    public static final int SEC_COMMAND_ID_SET_ROAM_SCAN_CHANNELS = 109;
    public static final int SEC_COMMAND_ID_SET_ROAM_SCAN_CONTROL = 107;
    public static final int SEC_COMMAND_ID_SET_ROAM_SCAN_PERIOD = 105;
    public static final int SEC_COMMAND_ID_SET_ROAM_TRIGGER = 101;
    public static final int SEC_COMMAND_ID_SET_RPS_MODE = 75;
    public static final int SEC_COMMAND_ID_SET_SCAN_CHANNEL_TIME = 131;
    public static final int SEC_COMMAND_ID_SET_SCAN_CONTROLLER_SETTINGS = 222;
    public static final int SEC_COMMAND_ID_SET_SCAN_HOME_AWAY_TIME = 135;
    public static final int SEC_COMMAND_ID_SET_SCAN_HOME_TIME = 133;
    public static final int SEC_COMMAND_ID_SET_SCAN_NPROBES = 137;
    public static final int SEC_COMMAND_ID_SET_SEC_WHITELIST = 82;
    public static final int SEC_COMMAND_ID_SET_SKIP_SCAN_ASSOC_LOCK = 76;
    public static final int SEC_COMMAND_ID_SET_SPECIAL_SSID = 259;
    public static final int SEC_COMMAND_ID_SET_WES_MODE = 171;
    public static final int SEC_COMMAND_ID_SET_WIFIAP_DISASSOC_STA = 4;
    public static final int SEC_COMMAND_ID_SET_WIFIAP_MACADDR_ACL = 194;
    public static final int SEC_COMMAND_ID_SET_WIFIAP_MAXCLIENT = 14;
    public static final int SEC_COMMAND_ID_SET_WIFIAP_RVFMODE = 27;
    public static final int SEC_COMMAND_ID_SET_WIFIAP_TEST_MODE = 192;
    public static final int SEC_COMMAND_ID_SET_WIFIAP_TX_POWER = 195;
    public static final int SEC_COMMAND_ID_SET_WIFIAP_WPS_PBC = 190;
    public static final int SEC_COMMAND_ID_SET_WIFI_ENABLED_WITH_P2P = 26;
    public static final int SEC_COMMAND_ID_SET_WIFI_ENABLED_WITH_RIL = 40;
    public static final int SEC_COMMAND_ID_SET_WIFI_SCAN_WITH_P2P = 74;
    public static final int SEC_COMMAND_ID_SHOW_APLIST_EVENT = 16;
    public static final int SEC_COMMAND_ID_SHUTDOWN_WIFI = 230;
    public static final int SEC_COMMAND_ID_SKIP_AUTO_CONNECTION = 32;
    public static final int SEC_COMMAND_ID_STOP_PERIODIC_SCAN = 18;
    public static final int SEC_COMMAND_ID_UNLOCK_MOBILITY_NETWORK = 29;
    public static final int SEC_COMMAND_ID_UPDATE_AUTOJOIN = 71;
    public static final int SEC_COMMAND_ID_UPDATE_SMART_NETWORK_SWITCH_PARAMETERS = 241;
    public static final int SEC_COMMAND_ID_VERSION = 0;
    public static final int SEC_COMMAND_ID_WIFI_CONNECTION_TYPE = 15;
    public static final int SEC_COMMAND_ID_WLAN_ENABLE_WARNING = 70;
    public static final int SEC_COMMAND_ID_WPS_AP_METHOD = 202;
    public static final int START_WPS = 151562;
    public static final int START_WPS_SUCCEEDED = 151563;
    public static final String SUPPLICANT_CONNECTION_CHANGE_ACTION = "android.net.wifi.supplicant.CONNECTION_CHANGE";
    public static final String SUPPLICANT_STATE_CHANGED_ACTION = "android.net.wifi.supplicant.STATE_CHANGE";
    public static final String SUPPLICANT_WAPI_EVENT = "android.net.wifi.supplicant.WAPI_EVENT";
    private static final String TAG = "WifiManager";
    public static final int VENDORS_NOTIFICATION_ID = 3012971;
    public static final int WAPI_EVENT_AUTH_FAIL_CODE = 16;
    public static final int WAPI_EVENT_CERT_FAIL_CODE = 17;
    public static final String WIFI_AP_DRIVER_STATE_HANGED = "com.samsung.android.net.wifi.WIFI_AP_DRIVER_STATE_HANGED";
    public static final int WIFI_AP_INTERFACE_STATE_CREATED = 1;
    public static final int WIFI_AP_INTERFACE_STATE_DELETED = 2;
    public static final int WIFI_AP_INTERFACE_STATE_ERRORED = 3;
    public static final String WIFI_AP_SET_INTEFACE = "com.samsung.android.net.wifi.softap_interface";
    public static final String WIFI_AP_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    public static final int WIFI_AP_STATE_DISABLED = 11;
    public static final int WIFI_AP_STATE_DISABLING = 10;
    public static final int WIFI_AP_STATE_ENABLED = 13;
    public static final int WIFI_AP_STATE_ENABLING = 12;
    public static final int WIFI_AP_STATE_FAILED = 14;
    public static final String WIFI_AP_STA_ASSOCIATED_ACTION = "android.net.wifi.WIFI_AP_STA_ASSOCIATED_ACTION";
    public static final String WIFI_AP_STA_ASSOC_LIST_AVAILABLE_ACTION = "android.net.wifi.WIFI_AP_STA_ASSOC_LIST_AVAILABLE_ACTION";
    public static final String WIFI_AP_STA_STATUS_CHANGED_ACTION = "com.samsung.android.net.wifi.WIFI_AP_STA_STATUS_CHANGED";
    public static final String WIFI_AP_WPS_STATE_ACTION = "android.net.wifi.WIFI_AP_WPS_STATE_ACTION";
    public static final String WIFI_CREDENTIAL_CHANGED_ACTION = "android.net.wifi.WIFI_CREDENTIAL_CHANGED";
    public static final int WIFI_CREDENTIAL_FORGOT = 1;
    public static final int WIFI_CREDENTIAL_SAVED = 0;
    public static final String WIFI_DIALOG_CANCEL_ACTION = "com.samsung.android.net.wifi.WIFI_DIALOG_CANCEL_ACTION";
    public static final int WIFI_DIALOG_ENABLING_HOTSPOT = 2;
    public static final int WIFI_DIALOG_ENABLING_WIFI = 1;
    public static final String WIFI_ENABLED_FROM_MHS = "com.samsung.android.net.wifi.WIFI_MHS";
    public static final int WIFI_FEATURE_ADDITIONAL_STA = 2048;
    public static final int WIFI_FEATURE_AP_STA = 32768;
    public static final int WIFI_FEATURE_BATCH_SCAN = 512;
    public static final int WIFI_FEATURE_D2AP_RTT = 256;
    public static final int WIFI_FEATURE_D2D_RTT = 128;
    public static final int WIFI_FEATURE_EPR = 16384;
    public static final int WIFI_FEATURE_HAL_EPNO = 262144;
    public static final int WIFI_FEATURE_INFRA = 1;
    public static final int WIFI_FEATURE_INFRA_5G = 2;
    public static final int WIFI_FEATURE_LINK_LAYER_STATS = 65536;
    public static final int WIFI_FEATURE_LOGGER = 131072;
    public static final int WIFI_FEATURE_MOBILE_HOTSPOT = 16;
    public static final int WIFI_FEATURE_NAN = 64;
    public static final int WIFI_FEATURE_P2P = 8;
    public static final int WIFI_FEATURE_PASSPOINT = 4;
    public static final int WIFI_FEATURE_PNO = 1024;
    public static final int WIFI_FEATURE_SCANNER = 32;
    public static final int WIFI_FEATURE_TDLS = 4096;
    public static final int WIFI_FEATURE_TDLS_OFFCHANNEL = 8192;
    public static final int WIFI_FREQUENCY_BAND_2GHZ = 2;
    public static final int WIFI_FREQUENCY_BAND_5GHZ = 1;
    public static final int WIFI_FREQUENCY_BAND_AUTO = 0;
    public static final int WIFI_LINK_STATUS_AC = 1;
    public static final int WIFI_LINK_STATUS_AC_MIMO = 3;
    public static final int WIFI_LINK_STATUS_MIMO = 2;
    public static final int WIFI_LINK_STATUS_NOT_AC_MIMO = 0;
    public static final int WIFI_MODE_FULL = 1;
    public static final int WIFI_MODE_FULL_HIGH_PERF = 3;
    public static final int WIFI_MODE_SCAN_ONLY = 2;
    public static final String WIFI_OXYGEN_STATE_CHANGED_ACTION = "com.samsung.android.net.wifi.WIFI_OXYGEN_STATE_CHANGE";
    public static final String WIFI_P2P_DISABLED = "com.samsung.android.net.wifi.p2p_disabled";
    public static final String WIFI_SCAN_AVAILABLE = "wifi_scan_available";
    public static final String WIFI_SHOW_NOTIFICATION_MESSAGE = "com.samsung.android.net.wifi.SHOW_NOTI_MESSAGE";
    public static final String WIFI_STATE_CHANGED_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";
    public static final int WIFI_STATE_DISABLED = 1;
    public static final int WIFI_STATE_DISABLING = 0;
    public static final int WIFI_STATE_ENABLED = 3;
    public static final int WIFI_STATE_ENABLING = 2;
    public static final int WIFI_STATE_UNKNOWN = 4;
    public static final int WPS_AUTH_FAILURE = 6;
    public static final int WPS_COMPLETED = 151565;
    public static final int WPS_FAILED = 151564;
    public static final int WPS_OVERLAP_ERROR = 3;
    public static final int WPS_TIMED_OUT = 7;
    public static final int WPS_TKIP_ONLY_PROHIBITED = 5;
    public static final int WPS_WEP_PROHIBITED = 4;
    private static final int mVersionOfCallSECApi = 1;
    private static AsyncChannel sAsyncChannel;
    private static ConnectivityManager sCM;
    private static CountDownLatch sConnected;
    private static HandlerThread sHandlerThread;
    private static int sListenerKey = 1;
    private static final SparseArray sListenerMap = new SparseArray();
    private static final Object sListenerMapLock = new Object();
    private static int sThreadRefCount;
    private static final Object sThreadRefLock = new Object();
    private int mActiveLockCount;
    private Context mContext;
    @GuardedBy("sCM")
    private PinningNetworkCallback mNetworkCallback;
    IWifiManager mService;
    private final int mTargetSdkVersion;

    public interface ActionListener {
        void onFailure(int i);

        void onSuccess();
    }

    public class MulticastLock {
        private final IBinder mBinder;
        private boolean mHeld;
        private int mRefCount;
        private boolean mRefCounted;
        private String mTag;

        private MulticastLock(String tag) {
            this.mTag = tag;
            this.mBinder = new Binder();
            this.mRefCount = 0;
            this.mRefCounted = true;
            this.mHeld = false;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void acquire() {
            /*
            r4 = this;
            r1 = r4.mBinder;
            monitor-enter(r1);
            r0 = r4.mRefCounted;	 Catch:{ all -> 0x004b }
            if (r0 == 0) goto L_0x003f;
        L_0x0007:
            r0 = r4.mRefCount;	 Catch:{ all -> 0x004b }
            r0 = r0 + 1;
            r4.mRefCount = r0;	 Catch:{ all -> 0x004b }
            if (r0 <= 0) goto L_0x003d;
        L_0x000f:
            r0 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x0039 }
            r0 = r0.mService;	 Catch:{ RemoteException -> 0x0039 }
            r2 = r4.mBinder;	 Catch:{ RemoteException -> 0x0039 }
            r3 = r4.mTag;	 Catch:{ RemoteException -> 0x0039 }
            r0.acquireMulticastLock(r2, r3);	 Catch:{ RemoteException -> 0x0039 }
            r2 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x0039 }
            monitor-enter(r2);	 Catch:{ RemoteException -> 0x0039 }
            r0 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x0036 }
            r0 = r0.mActiveLockCount;	 Catch:{ all -> 0x0036 }
            r3 = 50;
            if (r0 < r3) goto L_0x0044;
        L_0x0027:
            r0 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x0036 }
            r0 = r0.mService;	 Catch:{ all -> 0x0036 }
            r0.releaseMulticastLock();	 Catch:{ all -> 0x0036 }
            r0 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x0036 }
            r3 = "Exceeded maximum number of wifi locks";
            r0.<init>(r3);	 Catch:{ all -> 0x0036 }
            throw r0;	 Catch:{ all -> 0x0036 }
        L_0x0036:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x0036 }
            throw r0;	 Catch:{ RemoteException -> 0x0039 }
        L_0x0039:
            r0 = move-exception;
        L_0x003a:
            r0 = 1;
            r4.mHeld = r0;	 Catch:{ all -> 0x004b }
        L_0x003d:
            monitor-exit(r1);	 Catch:{ all -> 0x004b }
            return;
        L_0x003f:
            r0 = r4.mHeld;	 Catch:{ all -> 0x004b }
            if (r0 != 0) goto L_0x003d;
        L_0x0043:
            goto L_0x000f;
        L_0x0044:
            r0 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x0036 }
            r0.mActiveLockCount = r0.mActiveLockCount + 1;	 Catch:{ all -> 0x0036 }
            monitor-exit(r2);	 Catch:{ all -> 0x0036 }
            goto L_0x003a;
        L_0x004b:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x004b }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager.MulticastLock.acquire():void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void release() {
            /*
            r4 = this;
            r1 = r4.mBinder;
            monitor-enter(r1);
            r0 = r4.mRefCounted;	 Catch:{ all -> 0x005f }
            if (r0 == 0) goto L_0x0018;
        L_0x0007:
            r0 = r4.mRefCount;	 Catch:{ all -> 0x005f }
            r0 = r0 + -1;
            r4.mRefCount = r0;	 Catch:{ all -> 0x005f }
            if (r0 < 0) goto L_0x0018;
        L_0x000f:
            r2 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x005f }
            monitor-enter(r2);	 Catch:{ all -> 0x005f }
            r0 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x0062 }
            r0.mActiveLockCount = r0.mActiveLockCount - 1;	 Catch:{ all -> 0x0062 }
            monitor-exit(r2);	 Catch:{ all -> 0x0062 }
        L_0x0018:
            r0 = r4.mRefCounted;	 Catch:{ all -> 0x005f }
            if (r0 == 0) goto L_0x0065;
        L_0x001c:
            r0 = r4.mRefCount;	 Catch:{ all -> 0x005f }
            if (r0 != 0) goto L_0x0040;
        L_0x0020:
            r0 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x006f }
            r0 = r0.mService;	 Catch:{ RemoteException -> 0x006f }
            r0.releaseMulticastLock();	 Catch:{ RemoteException -> 0x006f }
            r2 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x006f }
            monitor-enter(r2);	 Catch:{ RemoteException -> 0x006f }
            r0 = r4.mRefCounted;	 Catch:{ all -> 0x006c }
            if (r0 != 0) goto L_0x003c;
        L_0x002e:
            r3 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x006c }
            r0 = r4.mRefCount;	 Catch:{ all -> 0x006c }
            if (r0 <= 0) goto L_0x006a;
        L_0x0034:
            r0 = r4.mRefCount;	 Catch:{ all -> 0x006c }
        L_0x0036:
            android.net.wifi.WifiManager.access$820(r3, r0);	 Catch:{ all -> 0x006c }
            r0 = 0;
            r4.mRefCount = r0;	 Catch:{ all -> 0x006c }
        L_0x003c:
            monitor-exit(r2);	 Catch:{ all -> 0x006c }
        L_0x003d:
            r0 = 0;
            r4.mHeld = r0;	 Catch:{ all -> 0x005f }
        L_0x0040:
            r0 = r4.mRefCount;	 Catch:{ all -> 0x005f }
            if (r0 >= 0) goto L_0x0071;
        L_0x0044:
            r0 = new java.lang.RuntimeException;	 Catch:{ all -> 0x005f }
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005f }
            r2.<init>();	 Catch:{ all -> 0x005f }
            r3 = "MulticastLock under-locked ";
            r2 = r2.append(r3);	 Catch:{ all -> 0x005f }
            r3 = r4.mTag;	 Catch:{ all -> 0x005f }
            r2 = r2.append(r3);	 Catch:{ all -> 0x005f }
            r2 = r2.toString();	 Catch:{ all -> 0x005f }
            r0.<init>(r2);	 Catch:{ all -> 0x005f }
            throw r0;	 Catch:{ all -> 0x005f }
        L_0x005f:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x005f }
            throw r0;
        L_0x0062:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x0062 }
            throw r0;	 Catch:{ all -> 0x005f }
        L_0x0065:
            r0 = r4.mHeld;	 Catch:{ all -> 0x005f }
            if (r0 == 0) goto L_0x0040;
        L_0x0069:
            goto L_0x0020;
        L_0x006a:
            r0 = 1;
            goto L_0x0036;
        L_0x006c:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x006c }
            throw r0;	 Catch:{ RemoteException -> 0x006f }
        L_0x006f:
            r0 = move-exception;
            goto L_0x003d;
        L_0x0071:
            monitor-exit(r1);	 Catch:{ all -> 0x005f }
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager.MulticastLock.release():void");
        }

        public void setReferenceCounted(boolean refCounted) {
            this.mRefCounted = refCounted;
        }

        public boolean isHeld() {
            boolean z;
            synchronized (this.mBinder) {
                z = this.mHeld;
            }
            return z;
        }

        public String toString() {
            String str;
            synchronized (this.mBinder) {
                String s3;
                String s1 = Integer.toHexString(System.identityHashCode(this));
                String s2 = this.mHeld ? "held; " : ProxyInfo.LOCAL_EXCL_LIST;
                if (this.mRefCounted) {
                    s3 = "refcounted: refcount = " + this.mRefCount;
                } else {
                    s3 = "not refcounted";
                }
                str = "MulticastLock{ " + s1 + "; " + s2 + s3 + " }";
            }
            return str;
        }

        protected void finalize() throws Throwable {
            super.finalize();
            setReferenceCounted(false);
            release();
        }
    }

    private class PinningNetworkCallback extends NetworkCallback {
        private Network mPinnedNetwork;

        private PinningNetworkCallback() {
        }

        public void onPreCheck(Network network) {
            WifiManager.sCM;
            if (ConnectivityManager.getProcessDefaultNetwork() == null && this.mPinnedNetwork == null) {
                WifiManager.sCM;
                ConnectivityManager.setProcessDefaultNetwork(network);
                this.mPinnedNetwork = network;
                Log.d(WifiManager.TAG, "Wifi alternate reality enabled on network " + network);
            }
        }

        public void onLost(Network network) {
            if (network.equals(this.mPinnedNetwork)) {
                WifiManager.sCM;
                if (network.equals(ConnectivityManager.getProcessDefaultNetwork())) {
                    WifiManager.sCM;
                    ConnectivityManager.setProcessDefaultNetwork(null);
                    Log.d(WifiManager.TAG, "Wifi alternate reality disabled on network " + network);
                    this.mPinnedNetwork = null;
                    WifiManager.this.unregisterPinningNetworkCallback();
                }
            }
        }
    }

    private static class ServiceHandler extends Handler {
        ServiceHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            Object listener = WifiManager.removeListener(message.arg2);
            switch (message.what) {
                case DevicePolicyManager.PASSWORD_QUALITY_FINGERPRINT_OLD /*69632*/:
                    if (message.arg1 == 0) {
                        WifiManager.sAsyncChannel.sendMessage(69633);
                    } else {
                        Log.e(WifiManager.TAG, "Failed to set up channel connection");
                        WifiManager.sAsyncChannel = null;
                    }
                    WifiManager.sConnected.countDown();
                    return;
                case 69636:
                    Log.e(WifiManager.TAG, "Channel connection lost");
                    WifiManager.sAsyncChannel = null;
                    getLooper().quit();
                    return;
                case WifiManager.CONNECT_NETWORK_FAILED /*151554*/:
                case WifiManager.FORGET_NETWORK_FAILED /*151557*/:
                case WifiManager.SAVE_NETWORK_FAILED /*151560*/:
                case WifiManager.DISABLE_NETWORK_FAILED /*151570*/:
                    if (listener != null) {
                        ((ActionListener) listener).onFailure(message.arg1);
                        return;
                    }
                    return;
                case WifiManager.CONNECT_NETWORK_SUCCEEDED /*151555*/:
                case WifiManager.FORGET_NETWORK_SUCCEEDED /*151558*/:
                case WifiManager.SAVE_NETWORK_SUCCEEDED /*151561*/:
                case WifiManager.DISABLE_NETWORK_SUCCEEDED /*151571*/:
                    if (listener != null) {
                        ((ActionListener) listener).onSuccess();
                        return;
                    }
                    return;
                case WifiManager.START_WPS_SUCCEEDED /*151563*/:
                    if (listener != null) {
                        ((WpsCallback) listener).onStarted(message.obj.pin);
                        synchronized (WifiManager.sListenerMapLock) {
                            WifiManager.sListenerMap.put(message.arg2, listener);
                        }
                        return;
                    }
                    return;
                case WifiManager.WPS_FAILED /*151564*/:
                    if (listener != null) {
                        ((WpsCallback) listener).onFailed(message.arg1);
                        return;
                    }
                    return;
                case WifiManager.WPS_COMPLETED /*151565*/:
                    if (listener != null) {
                        ((WpsCallback) listener).onSucceeded();
                        return;
                    }
                    return;
                case WifiManager.CANCEL_WPS_FAILED /*151567*/:
                    if (listener != null) {
                        ((WpsCallback) listener).onFailed(message.arg1);
                        return;
                    }
                    return;
                case WifiManager.CANCEL_WPS_SUCCEDED /*151568*/:
                    if (listener != null) {
                        ((WpsCallback) listener).onSucceeded();
                        return;
                    }
                    return;
                case WifiManager.RSSI_PKTCNT_FETCH_SUCCEEDED /*151573*/:
                    if (listener != null) {
                        RssiPacketCountInfo info = message.obj;
                        if (info != null) {
                            ((TxPacketCountListener) listener).onSuccess(info.txgood + info.txbad);
                            return;
                        } else {
                            ((TxPacketCountListener) listener).onFailure(0);
                            return;
                        }
                    }
                    return;
                case WifiManager.RSSI_PKTCNT_FETCH_FAILED /*151574*/:
                    if (listener != null) {
                        ((TxPacketCountListener) listener).onFailure(message.arg1);
                        return;
                    }
                    return;
                case PppoeManager.CMD_START_PPPOE_FAILED /*458754*/:
                case PppoeManager.CMD_STOP_PPPOE_FAILED /*458757*/:
                    if (listener != null) {
                        ((ActionListener) listener).onFailure(message.arg1);
                        return;
                    }
                    return;
                case PppoeManager.CMD_START_PPPOE_SUCCEDED /*458755*/:
                case PppoeManager.CMD_STOP_PPPOE_SUCCEDED /*458758*/:
                    if (listener != null) {
                        ((ActionListener) listener).onSuccess();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public interface TxPacketCountListener {
        void onFailure(int i);

        void onSuccess(int i);
    }

    public class WifiLock {
        private final IBinder mBinder;
        private boolean mHeld;
        int mLockType;
        private int mRefCount;
        private boolean mRefCounted;
        private String mTag;
        private WorkSource mWorkSource;

        private WifiLock(int lockType, String tag) {
            this.mTag = tag;
            this.mLockType = lockType;
            this.mBinder = new Binder();
            this.mRefCount = 0;
            this.mRefCounted = true;
            this.mHeld = false;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void acquire() {
            /*
            r6 = this;
            r1 = r6.mBinder;
            monitor-enter(r1);
            r0 = r6.mRefCounted;	 Catch:{ all -> 0x0051 }
            if (r0 == 0) goto L_0x0045;
        L_0x0007:
            r0 = r6.mRefCount;	 Catch:{ all -> 0x0051 }
            r0 = r0 + 1;
            r6.mRefCount = r0;	 Catch:{ all -> 0x0051 }
            if (r0 <= 0) goto L_0x0043;
        L_0x000f:
            r0 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x003f }
            r0 = r0.mService;	 Catch:{ RemoteException -> 0x003f }
            r2 = r6.mBinder;	 Catch:{ RemoteException -> 0x003f }
            r3 = r6.mLockType;	 Catch:{ RemoteException -> 0x003f }
            r4 = r6.mTag;	 Catch:{ RemoteException -> 0x003f }
            r5 = r6.mWorkSource;	 Catch:{ RemoteException -> 0x003f }
            r0.acquireWifiLock(r2, r3, r4, r5);	 Catch:{ RemoteException -> 0x003f }
            r2 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x003f }
            monitor-enter(r2);	 Catch:{ RemoteException -> 0x003f }
            r0 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x003c }
            r0 = r0.mActiveLockCount;	 Catch:{ all -> 0x003c }
            r3 = 50;
            if (r0 < r3) goto L_0x004a;
        L_0x002b:
            r0 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x003c }
            r0 = r0.mService;	 Catch:{ all -> 0x003c }
            r3 = r6.mBinder;	 Catch:{ all -> 0x003c }
            r0.releaseWifiLock(r3);	 Catch:{ all -> 0x003c }
            r0 = new java.lang.UnsupportedOperationException;	 Catch:{ all -> 0x003c }
            r3 = "Exceeded maximum number of wifi locks";
            r0.<init>(r3);	 Catch:{ all -> 0x003c }
            throw r0;	 Catch:{ all -> 0x003c }
        L_0x003c:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x003c }
            throw r0;	 Catch:{ RemoteException -> 0x003f }
        L_0x003f:
            r0 = move-exception;
        L_0x0040:
            r0 = 1;
            r6.mHeld = r0;	 Catch:{ all -> 0x0051 }
        L_0x0043:
            monitor-exit(r1);	 Catch:{ all -> 0x0051 }
            return;
        L_0x0045:
            r0 = r6.mHeld;	 Catch:{ all -> 0x0051 }
            if (r0 != 0) goto L_0x0043;
        L_0x0049:
            goto L_0x000f;
        L_0x004a:
            r0 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x003c }
            r0.mActiveLockCount = r0.mActiveLockCount + 1;	 Catch:{ all -> 0x003c }
            monitor-exit(r2);	 Catch:{ all -> 0x003c }
            goto L_0x0040;
        L_0x0051:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0051 }
            throw r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager.WifiLock.acquire():void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void release() {
            /*
            r4 = this;
            r1 = r4.mBinder;
            monitor-enter(r1);
            r0 = r4.mRefCounted;	 Catch:{ all -> 0x0061 }
            if (r0 == 0) goto L_0x0018;
        L_0x0007:
            r0 = r4.mRefCount;	 Catch:{ all -> 0x0061 }
            r0 = r0 + -1;
            r4.mRefCount = r0;	 Catch:{ all -> 0x0061 }
            if (r0 < 0) goto L_0x0018;
        L_0x000f:
            r2 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x0061 }
            monitor-enter(r2);	 Catch:{ all -> 0x0061 }
            r0 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x0064 }
            r0.mActiveLockCount = r0.mActiveLockCount - 1;	 Catch:{ all -> 0x0064 }
            monitor-exit(r2);	 Catch:{ all -> 0x0064 }
        L_0x0018:
            r0 = r4.mRefCounted;	 Catch:{ all -> 0x0061 }
            if (r0 == 0) goto L_0x0067;
        L_0x001c:
            r0 = r4.mRefCount;	 Catch:{ all -> 0x0061 }
            if (r0 != 0) goto L_0x0042;
        L_0x0020:
            r0 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x0071 }
            r0 = r0.mService;	 Catch:{ RemoteException -> 0x0071 }
            r2 = r4.mBinder;	 Catch:{ RemoteException -> 0x0071 }
            r0.releaseWifiLock(r2);	 Catch:{ RemoteException -> 0x0071 }
            r2 = android.net.wifi.WifiManager.this;	 Catch:{ RemoteException -> 0x0071 }
            monitor-enter(r2);	 Catch:{ RemoteException -> 0x0071 }
            r0 = r4.mRefCounted;	 Catch:{ all -> 0x006e }
            if (r0 != 0) goto L_0x003e;
        L_0x0030:
            r3 = android.net.wifi.WifiManager.this;	 Catch:{ all -> 0x006e }
            r0 = r4.mRefCount;	 Catch:{ all -> 0x006e }
            if (r0 <= 0) goto L_0x006c;
        L_0x0036:
            r0 = r4.mRefCount;	 Catch:{ all -> 0x006e }
        L_0x0038:
            android.net.wifi.WifiManager.access$820(r3, r0);	 Catch:{ all -> 0x006e }
            r0 = 0;
            r4.mRefCount = r0;	 Catch:{ all -> 0x006e }
        L_0x003e:
            monitor-exit(r2);	 Catch:{ all -> 0x006e }
        L_0x003f:
            r0 = 0;
            r4.mHeld = r0;	 Catch:{ all -> 0x0061 }
        L_0x0042:
            r0 = r4.mRefCount;	 Catch:{ all -> 0x0061 }
            if (r0 >= 0) goto L_0x0073;
        L_0x0046:
            r0 = new java.lang.RuntimeException;	 Catch:{ all -> 0x0061 }
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0061 }
            r2.<init>();	 Catch:{ all -> 0x0061 }
            r3 = "WifiLock under-locked ";
            r2 = r2.append(r3);	 Catch:{ all -> 0x0061 }
            r3 = r4.mTag;	 Catch:{ all -> 0x0061 }
            r2 = r2.append(r3);	 Catch:{ all -> 0x0061 }
            r2 = r2.toString();	 Catch:{ all -> 0x0061 }
            r0.<init>(r2);	 Catch:{ all -> 0x0061 }
            throw r0;	 Catch:{ all -> 0x0061 }
        L_0x0061:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0061 }
            throw r0;
        L_0x0064:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x0064 }
            throw r0;	 Catch:{ all -> 0x0061 }
        L_0x0067:
            r0 = r4.mHeld;	 Catch:{ all -> 0x0061 }
            if (r0 == 0) goto L_0x0042;
        L_0x006b:
            goto L_0x0020;
        L_0x006c:
            r0 = 1;
            goto L_0x0038;
        L_0x006e:
            r0 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x006e }
            throw r0;	 Catch:{ RemoteException -> 0x0071 }
        L_0x0071:
            r0 = move-exception;
            goto L_0x003f;
        L_0x0073:
            monitor-exit(r1);	 Catch:{ all -> 0x0061 }
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager.WifiLock.release():void");
        }

        public void setReferenceCounted(boolean refCounted) {
            this.mRefCounted = refCounted;
        }

        public boolean isHeld() {
            boolean z;
            synchronized (this.mBinder) {
                z = this.mHeld;
            }
            return z;
        }

        public void setWorkSource(WorkSource ws) {
            synchronized (this.mBinder) {
                if (ws != null) {
                    if (ws.size() == 0) {
                        ws = null;
                    }
                }
                boolean changed = true;
                if (ws == null) {
                    this.mWorkSource = null;
                } else {
                    ws.clearNames();
                    if (this.mWorkSource == null) {
                        changed = this.mWorkSource != null;
                        this.mWorkSource = new WorkSource(ws);
                    } else {
                        changed = this.mWorkSource.diff(ws);
                        if (changed) {
                            this.mWorkSource.set(ws);
                        }
                    }
                }
                if (changed && this.mHeld) {
                    try {
                        WifiManager.this.mService.updateWifiLockWorkSource(this.mBinder, this.mWorkSource);
                    } catch (RemoteException e) {
                    }
                }
            }
        }

        public String toString() {
            String str;
            synchronized (this.mBinder) {
                String s3;
                String s1 = Integer.toHexString(System.identityHashCode(this));
                String s2 = this.mHeld ? "held; " : ProxyInfo.LOCAL_EXCL_LIST;
                if (this.mRefCounted) {
                    s3 = "refcounted: refcount = " + this.mRefCount;
                } else {
                    s3 = "not refcounted";
                }
                str = "WifiLock{ " + s1 + "; " + s2 + s3 + " }";
            }
            return str;
        }

        protected void finalize() throws Throwable {
            super.finalize();
            synchronized (this.mBinder) {
                if (this.mHeld) {
                    try {
                        WifiManager.this.mService.releaseWifiLock(this.mBinder);
                        synchronized (WifiManager.this) {
                            WifiManager.access$820(WifiManager.this, this.mRefCount > 0 ? this.mRefCount : 1);
                        }
                    } catch (RemoteException e) {
                    }
                }
            }
        }
    }

    public static abstract class WpsCallback {
        public abstract void onFailed(int i);

        public abstract void onStarted(String str);

        public abstract void onSucceeded();
    }

    static /* synthetic */ int access$820(WifiManager x0, int x1) {
        int i = x0.mActiveLockCount - x1;
        x0.mActiveLockCount = i;
        return i;
    }

    static {
        boolean z = false;
        if ("eng".equals(Build.TYPE) || Debug.isProductShip() != 1) {
            z = true;
        }
        DBGMHS = z;
        WifiApCust.getInstance();
    }

    public WifiManager(Context context, IWifiManager service) {
        this.mContext = context;
        this.mService = service;
        this.mTargetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        init();
    }

    public List<WifiConfiguration> getConfiguredNetworks() {
        try {
            return this.mService.getConfiguredNetworks();
        } catch (RemoteException e) {
            Log.w(TAG, "Caught RemoteException trying to get configured networks: " + e);
            return null;
        }
    }

    public WifiConfiguration getSpecificNetwork(int netID) {
        try {
            return this.mService.getSpecificNetwork(netID);
        } catch (RemoteException e) {
            return null;
        }
    }

    public List<WifiConfiguration> getPrivilegedConfiguredNetworks() {
        try {
            return this.mService.getPrivilegedConfiguredNetworks();
        } catch (RemoteException e) {
            return null;
        }
    }

    public WifiConnectionStatistics getConnectionStatistics() {
        try {
            return this.mService.getConnectionStatistics();
        } catch (RemoteException e) {
            return null;
        }
    }

    public WifiConfiguration getMatchingWifiConfig(ScanResult scanResult) {
        try {
            return this.mService.getMatchingWifiConfig(scanResult);
        } catch (RemoteException e) {
            return null;
        }
    }

    public int addNetwork(WifiConfiguration config) {
        if (config == null) {
            return -1;
        }
        config.networkId = -1;
        return addOrUpdateNetwork(config);
    }

    public int updateNetwork(WifiConfiguration config) {
        if (config == null || config.networkId < 0) {
            return -1;
        }
        return addOrUpdateNetwork(config);
    }

    private int addOrUpdateNetwork(WifiConfiguration config) {
        try {
            return this.mService.addOrUpdateNetwork(config);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean removeNetwork(int netId) {
        try {
            return this.mService.removeNetwork(netId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean enableNetwork(int netId, boolean disableOthers) {
        boolean success;
        boolean pin = disableOthers && this.mTargetSdkVersion < 21;
        if (pin) {
            registerPinningNetworkCallback();
        }
        try {
            success = this.mService.enableNetwork(netId, disableOthers);
        } catch (RemoteException e) {
            success = false;
        }
        if (pin && !success) {
            unregisterPinningNetworkCallback();
        }
        return success;
    }

    public boolean disableNetwork(int netId) {
        try {
            return this.mService.disableNetwork(netId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean disconnect() {
        try {
            this.mService.disconnect();
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean reconnect() {
        try {
            this.mService.reconnect();
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean reassociate() {
        try {
            this.mService.reassociate();
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean pingSupplicant() {
        boolean z = false;
        if (this.mService != null) {
            try {
                z = this.mService.pingSupplicant();
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public List<WifiChannel> getChannelList() {
        try {
            return this.mService.getChannelList();
        } catch (RemoteException e) {
            return null;
        }
    }

    private int getSupportedFeatures() {
        try {
            return this.mService.getSupportedFeatures();
        } catch (RemoteException e) {
            return 0;
        }
    }

    private boolean isFeatureSupported(int feature) {
        return (getSupportedFeatures() & feature) == feature;
    }

    public boolean is5GHzBandSupported() {
        return isFeatureSupported(2);
    }

    public boolean isPasspointSupported() {
        return isFeatureSupported(4);
    }

    public boolean isP2pSupported() {
        return isFeatureSupported(8);
    }

    public boolean isPortableHotspotSupported() {
        return isFeatureSupported(16);
    }

    public boolean isWifiScannerSupported() {
        return isFeatureSupported(32);
    }

    public boolean isNanSupported() {
        return isFeatureSupported(64);
    }

    public boolean isDeviceToDeviceRttSupported() {
        return false;
    }

    public boolean isDeviceToApRttSupported() {
        return false;
    }

    public boolean isPreferredNetworkOffloadSupported() {
        return isFeatureSupported(1024);
    }

    public boolean isAdditionalStaSupported() {
        return isFeatureSupported(2048);
    }

    public boolean isTdlsSupported() {
        return isFeatureSupported(4096);
    }

    public boolean isOffChannelTdlsSupported() {
        return isFeatureSupported(8192);
    }

    public boolean isEnhancedPowerReportingSupported() {
        return isFeatureSupported(65536);
    }

    public WifiActivityEnergyInfo getControllerActivityEnergyInfo(int updateType) {
        if (this.mService == null) {
            return null;
        }
        try {
            if (!isEnhancedPowerReportingSupported()) {
                return null;
            }
            synchronized (this) {
                WifiActivityEnergyInfo record = this.mService.reportActivityInfo();
                if (record == null || !record.isValid()) {
                    return null;
                }
                return record;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "getControllerActivityEnergyInfo: " + e);
            return null;
        }
    }

    public boolean startScan() {
        try {
            this.mService.startScan(null, null);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean startScan(WorkSource workSource) {
        try {
            this.mService.startScan(null, workSource);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean startLocationRestrictedScan(WorkSource workSource) {
        try {
            this.mService.startLocationRestrictedScan(workSource);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean TstartScan(int r15) {
        /*
        r14 = this;
        r10 = 1;
        r11 = 0;
        r9 = r14.mService;	 Catch:{ RemoteException -> 0x0043 }
        r0 = r9.getChannelList();	 Catch:{ RemoteException -> 0x0043 }
        r3 = new java.util.ArrayList;	 Catch:{ RemoteException -> 0x0043 }
        r3.<init>();	 Catch:{ RemoteException -> 0x0043 }
        if (r0 == 0) goto L_0x006d;
    L_0x000f:
        r9 = r0.size();	 Catch:{ RemoteException -> 0x0043 }
        if (r9 <= 0) goto L_0x006d;
    L_0x0015:
        switch(r15) {
            case 0: goto L_0x001a;
            case 1: goto L_0x0023;
            case 2: goto L_0x004d;
            default: goto L_0x0018;
        };	 Catch:{ RemoteException -> 0x0043 }
    L_0x0018:
        r9 = r11;
    L_0x0019:
        return r9;
    L_0x001a:
        r9 = r14.mService;	 Catch:{ RemoteException -> 0x0043 }
        r12 = 0;
        r13 = 0;
        r9.startScan(r12, r13);	 Catch:{ RemoteException -> 0x0043 }
        r9 = r10;
        goto L_0x0019;
    L_0x0023:
        r6 = r0.iterator();	 Catch:{ RemoteException -> 0x0043 }
    L_0x0027:
        r9 = r6.hasNext();	 Catch:{ RemoteException -> 0x0043 }
        if (r9 == 0) goto L_0x006f;
    L_0x002d:
        r2 = r6.next();	 Catch:{ RemoteException -> 0x0043 }
        r2 = (android.net.wifi.WifiChannel) r2;	 Catch:{ RemoteException -> 0x0043 }
        r9 = r2.freqMHz;	 Catch:{ RemoteException -> 0x0043 }
        r12 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;
        if (r9 <= r12) goto L_0x0027;
    L_0x0039:
        r9 = r2.freqMHz;	 Catch:{ RemoteException -> 0x0043 }
        r9 = java.lang.Integer.valueOf(r9);	 Catch:{ RemoteException -> 0x0043 }
        r3.add(r9);	 Catch:{ RemoteException -> 0x0043 }
        goto L_0x0027;
    L_0x0043:
        r4 = move-exception;
        r9 = "WifiManager";
        r10 = "TstartScan() failed!";
        android.util.Log.i(r9, r10);
        r9 = r11;
        goto L_0x0019;
    L_0x004d:
        r6 = r0.iterator();	 Catch:{ RemoteException -> 0x0043 }
    L_0x0051:
        r9 = r6.hasNext();	 Catch:{ RemoteException -> 0x0043 }
        if (r9 == 0) goto L_0x006f;
    L_0x0057:
        r2 = r6.next();	 Catch:{ RemoteException -> 0x0043 }
        r2 = (android.net.wifi.WifiChannel) r2;	 Catch:{ RemoteException -> 0x0043 }
        r9 = r2.freqMHz;	 Catch:{ RemoteException -> 0x0043 }
        r12 = 3000; // 0xbb8 float:4.204E-42 double:1.482E-320;
        if (r9 >= r12) goto L_0x0051;
    L_0x0063:
        r9 = r2.freqMHz;	 Catch:{ RemoteException -> 0x0043 }
        r9 = java.lang.Integer.valueOf(r9);	 Catch:{ RemoteException -> 0x0043 }
        r3.add(r9);	 Catch:{ RemoteException -> 0x0043 }
        goto L_0x0051;
    L_0x006d:
        r9 = r11;
        goto L_0x0019;
    L_0x006f:
        r9 = r3.size();	 Catch:{ RemoteException -> 0x0043 }
        if (r9 == 0) goto L_0x00d6;
    L_0x0075:
        r9 = r3.size();	 Catch:{ RemoteException -> 0x0043 }
        r8 = new int[r9];	 Catch:{ RemoteException -> 0x0043 }
        r5 = 0;
    L_0x007c:
        r9 = r3.size();	 Catch:{ RemoteException -> 0x0043 }
        if (r5 >= r9) goto L_0x0091;
    L_0x0082:
        r9 = r3.get(r5);	 Catch:{ RemoteException -> 0x0043 }
        r9 = (java.lang.Integer) r9;	 Catch:{ RemoteException -> 0x0043 }
        r9 = r9.intValue();	 Catch:{ RemoteException -> 0x0043 }
        r8[r5] = r9;	 Catch:{ RemoteException -> 0x0043 }
        r5 = r5 + 1;
        goto L_0x007c;
    L_0x0091:
        r9 = "WifiManager";
        r12 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0043 }
        r12.<init>();	 Catch:{ RemoteException -> 0x0043 }
        r13 = "TstartScan()";
        r12 = r12.append(r13);	 Catch:{ RemoteException -> 0x0043 }
        r13 = java.util.Arrays.toString(r8);	 Catch:{ RemoteException -> 0x0043 }
        r12 = r12.append(r13);	 Catch:{ RemoteException -> 0x0043 }
        r12 = r12.toString();	 Catch:{ RemoteException -> 0x0043 }
        android.util.Log.i(r9, r12);	 Catch:{ RemoteException -> 0x0043 }
        r7 = new android.os.Message;	 Catch:{ RemoteException -> 0x00ca }
        r7.<init>();	 Catch:{ RemoteException -> 0x00ca }
        r9 = 36;
        r7.what = r9;	 Catch:{ RemoteException -> 0x00ca }
        r1 = new android.os.Bundle;	 Catch:{ RemoteException -> 0x00ca }
        r1.<init>();	 Catch:{ RemoteException -> 0x00ca }
        r9 = "channel";
        r1.putIntArray(r9, r8);	 Catch:{ RemoteException -> 0x00ca }
        r7.obj = r1;	 Catch:{ RemoteException -> 0x00ca }
        r9 = r14.mService;	 Catch:{ RemoteException -> 0x00ca }
        r9.callSECApi(r7);	 Catch:{ RemoteException -> 0x00ca }
        r9 = r10;
        goto L_0x0019;
    L_0x00ca:
        r4 = move-exception;
        r9 = "WifiManager";
        r10 = "mService.startScan() failed!";
        android.util.Log.i(r9, r10);	 Catch:{ RemoteException -> 0x0043 }
        r9 = r11;
        goto L_0x0019;
    L_0x00d6:
        r9 = r11;
        goto L_0x0019;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.net.wifi.WifiManager.TstartScan(int):boolean");
    }

    public boolean startCustomizedScan(ScanSettings requested) {
        try {
            this.mService.startScan(requested, null);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean startCustomizedScan(ScanSettings requested, WorkSource workSource) {
        try {
            this.mService.startScan(requested, workSource);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean requestBatchedScan(BatchedScanSettings requested) {
        try {
            return this.mService.requestBatchedScan(requested, new Binder(), null);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean requestBatchedScan(BatchedScanSettings requested, WorkSource workSource) {
        try {
            return this.mService.requestBatchedScan(requested, new Binder(), workSource);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isBatchedScanSupported() {
        try {
            return this.mService.isBatchedScanSupported();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void stopBatchedScan(BatchedScanSettings requested) {
        try {
            this.mService.stopBatchedScan(requested);
        } catch (RemoteException e) {
        }
    }

    public List<BatchedScanResult> getBatchedScanResults() {
        try {
            return this.mService.getBatchedScanResults(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            return null;
        }
    }

    public void pollBatchedScan() {
        try {
            this.mService.pollBatchedScan();
        } catch (RemoteException e) {
        }
    }

    public String getWpsNfcConfigurationToken(int netId) {
        try {
            return this.mService.getWpsNfcConfigurationToken(netId);
        } catch (RemoteException e) {
            return null;
        }
    }

    public WifiInfo getConnectionInfo() {
        try {
            return this.mService.getConnectionInfo();
        } catch (RemoteException e) {
            return null;
        }
    }

    public List<ScanResult> getScanResults() {
        try {
            return this.mService.getScanResults(this.mContext.getOpPackageName());
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean isScanAlwaysAvailable() {
        try {
            return this.mService.isScanAlwaysAvailable();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isHs20OsuProviderAvailable() {
        try {
            return this.mService.isHs20OsuProviderAvailable();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean fetchHs20OsuProviders() {
        try {
            return this.mService.fetchHs20OsuProviders();
        } catch (RemoteException e) {
            return false;
        }
    }

    public List<WifiHs20OsuProvider> getHs20OsuProviders() {
        try {
            return this.mService.getHs20OsuProviders();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean saveConfiguration() {
        try {
            return this.mService.saveConfiguration();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setCountryCode(String country, boolean persist) {
        try {
            this.mService.setCountryCode(country, persist);
        } catch (RemoteException e) {
        }
    }

    public String getCountryCode() {
        try {
            return this.mService.getCountryCode();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setFrequencyBand(int band, boolean persist) {
        try {
            this.mService.setFrequencyBand(band, persist);
        } catch (RemoteException e) {
        }
    }

    public int getFrequencyBand() {
        try {
            return this.mService.getFrequencyBand();
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean isDualBandSupported() {
        try {
            return this.mService.isDualBandSupported();
        } catch (RemoteException e) {
            return false;
        }
    }

    public DhcpInfo getDhcpInfo() {
        try {
            return this.mService.getDhcpInfo();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean setWifiEnabled(boolean enabled) {
        boolean z = false;
        String packageName = this.mContext.getPackageName();
        if (DBG) {
            Log.i(TAG, "setWifiEnabled: " + enabled + "packageName : " + packageName);
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
        }
        if (enabled && isAllowWifiWarning() && ((getWifiApState() == 11 || getWifiApState() == 10) && getWifiState() != 3 && getWifiState() != 2 && !isUserAction())) {
            String label = (String) this.mContext.getPackageManager().getApplicationLabel(this.mContext.getApplicationInfo());
            if (DBG) {
                Log.i(TAG, "label :" + label);
            }
            Intent wifiEnableWarningIntent = new Intent();
            wifiEnableWarningIntent.setClassName("com.android.settings", "com.android.settings.wifi.WifiWarningDialog");
            wifiEnableWarningIntent.setFlags(343932928);
            wifiEnableWarningIntent.putExtra("dialog_type", "wlan_enable_warning");
            wifiEnableWarningIntent.putExtra("dialog_name", label);
            Log.i(TAG, "WifiManager.setWifiEnabled Prepare to stat activity: WifiWarningDialog");
            int userId = UserHandle.myUserId();
            try {
                UserHandle userHandle;
                Context context = this.mContext;
                if (packageName.equals("com.android.systemui")) {
                    userHandle = UserHandle.CURRENT;
                } else {
                    userHandle = new UserHandle(userId);
                }
                context.startActivityAsUser(wifiEnableWarningIntent, userHandle);
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "ActivityNotFoundException occured. WifiManager.startActivityAsUser()");
                e.printStackTrace();
            }
        } else {
            try {
                Message msg = new Message();
                msg.what = 78;
                Bundle args = new Bundle();
                args.putString("extra_log", new String("WifiManager.setWifiEnabled(" + enabled + ") : " + packageName + "\n"));
                msg.obj = args;
                callSECApi(msg);
                z = this.mService.setWifiEnabled(enabled);
            } catch (RemoteException e2) {
            }
        }
        return z;
    }

    public int getWifiState() {
        try {
            return this.mService.getWifiEnabledState();
        } catch (RemoteException e) {
            return 4;
        }
    }

    public boolean isWifiEnabled() {
        return getWifiState() == 3;
    }

    public void getTxPacketCount(TxPacketCountListener listener) {
        validateChannel();
        sAsyncChannel.sendMessage(RSSI_PKTCNT_FETCH, 0, putListener(listener));
    }

    public static int calculateSignalLevel(int rssi, int numLevels) {
        if (numLevels == 5) {
            if (rssi <= -89) {
                return 0;
            }
            if (rssi > -89 && rssi <= -83) {
                return 1;
            }
            if (rssi > -83 && rssi <= -75) {
                return 2;
            }
            if (rssi <= -75 || rssi > -64) {
                return 4;
            }
            return 3;
        } else if (rssi <= -100) {
            return 0;
        } else {
            if (rssi >= MAX_RSSI) {
                return numLevels - 1;
            }
            return (int) ((((float) (rssi + 100)) * ((float) (numLevels - 1))) / 45.0f);
        }
    }

    public static int compareSignalLevel(int rssiA, int rssiB) {
        return rssiA - rssiB;
    }

    public boolean setWifiApEnabled(WifiConfiguration wifiConfig, boolean enabled) {
        Log.i(TAG, "setWifiApEnabled : " + enabled + "packageName : " + this.mContext.getPackageName());
        if (DBGMHS) {
            Log.e(TAG, Log.getStackTraceString(new Throwable()));
        }
        try {
            Message msg = new Message();
            msg.what = 78;
            Bundle args = new Bundle();
            if (DBGMHS) {
                StackTraceElement callerElement = new Exception().getStackTrace()[1];
                args.putString("extra_log", String.format("%s: %s%n", new Object[]{DateFormat.format("yy/MM/dd kk:mm:ss", System.currentTimeMillis()), "setWifiApEnabled(" + enabled + ") : " + packageName + "[" + callerElement.getFileName() + ":" + callerElement.getMethodName() + "():" + callerElement.getLineNumber() + "]"}));
            } else {
                args.putString("extra_log", String.format("%s: %s%n", new Object[]{DateFormat.format("yy/MM/dd kk:mm:ss", System.currentTimeMillis()), "setWifiApEnabled(" + enabled + ") : " + packageName + "\n"}));
            }
            msg.obj = args;
            callSECApi(msg);
            this.mService.setWifiApEnabled(wifiConfig, enabled);
            if (enabled && "CMCC".equals(CONFIGOPBRANDINGFORMOBILEAP) && isUserAction()) {
                Context context = this.mContext;
                Context context2 = this.mContext;
                ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                boolean isMobileDataEnabled = mConnectivityManager.getMobileDataEnabled();
                NetworkInfo activeNetwork = mConnectivityManager.getActiveNetworkInfo();
                if (isMobileDataEnabled) {
                    if (activeNetwork == null) {
                        isMobileDataEnabled = false;
                    } else if (activeNetwork.getType() != 0) {
                        isMobileDataEnabled = false;
                    }
                }
                if (!(isMobileDataEnabled || activeNetwork == null || activeNetwork.getType() != 7)) {
                    isMobileDataEnabled = true;
                }
                Log.i(TAG, "CMCC isMobileDataEnabled : " + isMobileDataEnabled + " wifi:" + isWifiEnabled() + " wifisharing:" + isWifiSharingEnabled());
                if (isWifiSharingEnabled()) {
                    if (!(isMobileDataEnabled || isWifiEnabled())) {
                        Toast.makeText(this.mContext, this.mContext.getString(17041386), 1).show();
                    }
                } else if (!isMobileDataEnabled) {
                    Toast.makeText(this.mContext, this.mContext.getString(17041386), 1).show();
                }
            }
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isUserAction() {
        for (RunningAppProcessInfo info : ((ActivityManager) this.mContext.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses()) {
            if (info.pid == Binder.getCallingPid()) {
                String pName = info.processName;
                Log.d(TAG, "processName = " + pName);
                if ("com.android.systemui".equals(pName.toLowerCase()) || "com.android.settings".equals(pName.toLowerCase()) || "com.sec.android.easysettings".equals(pName.toLowerCase()) || "com.sec.android.emergencymode.service".equals(pName.toLowerCase()) || "com.sec.NetworkPowerSaving".equals(pName) || "com.android.nfc".equals(pName) || "com.samsung.android.app.sreminder".equals(pName.toLowerCase()) || ("com.sec.knox.kccagent".equals(info.processName.toLowerCase()) && !isCustomizedByKccAgent())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCustomizedByKccAgent() {
        if (new File("/data/data/com.sec.knox.kccagent/shared_prefs/customized.xml").exists()) {
            return true;
        }
        return false;
    }

    public int getWifiApState() {
        try {
            return this.mService.getWifiApEnabledState();
        } catch (RemoteException e) {
            return 14;
        }
    }

    public boolean isWifiApEnabled() {
        return getWifiApState() == 13;
    }

    public WifiConfiguration getWifiApConfiguration() {
        try {
            return this.mService.getWifiApConfiguration();
        } catch (RemoteException e) {
            return null;
        }
    }

    public WifiConfiguration buildWifiConfig(String uriString, String mimeType, byte[] data) {
        try {
            return this.mService.buildWifiConfig(uriString, mimeType, data);
        } catch (RemoteException e) {
            Log.w(TAG, "Caught RemoteException trying to build wifi config: " + e);
            return null;
        }
    }

    public boolean setWifiApConfiguration(WifiConfiguration wifiConfig) {
        try {
            this.mService.setWifiApConfiguration(wifiConfig);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setWifiApConfigurationToDefault() {
        try {
            this.mService.setWifiApConfigurationToDefault();
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public String getWifiApStaList() {
        try {
            return this.mService.getWifiApStaList();
        } catch (RemoteException e) {
            return null;
        }
    }

    public String getWifiApChameleonSsid() {
        try {
            return this.mService.getWifiApChameleonSsid();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean setWifiApTimeOut(int sec) {
        try {
            this.mService.setWifiApTimeOut(sec);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getWifiApTimeOut() {
        try {
            return this.mService.getWifiApTimeOut();
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int getWifiApConfigTxPower() {
        try {
            return this.mService.getWifiApConfigTxPower();
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean setWifiApConfigTxPower(int txPowerMode) {
        try {
            this.mService.setWifiApConfigTxPower(txPowerMode);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isWifiApDbContain(String mac) {
        try {
            return this.mService.isWifiApDbContain(mac);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isWifiApListContain(String mac) {
        try {
            return this.mService.isWifiApListContain(mac);
        } catch (RemoteException e) {
            return false;
        }
    }

    public String getWifiApLimitDataFromDb(String mac) {
        try {
            return this.mService.getWifiApLimitDataFromDb(mac);
        } catch (RemoteException e) {
            return null;
        }
    }

    public String getWifiApRemainDataFromDb(String mac) {
        try {
            return this.mService.getWifiApRemainDataFromDb(mac);
        } catch (RemoteException e) {
            return null;
        }
    }

    public void addWifiApData(String mac, String ip, String limit) {
        try {
            this.mService.addWifiApData(mac, ip, limit);
        } catch (RemoteException e) {
        }
    }

    public boolean isWifiApConcurrentSupported() {
        return false;
    }

    public String getWifiApInterfaceName() {
        return "wlan0";
    }

    public boolean startWifi() {
        try {
            this.mService.startWifi();
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean stopWifi() {
        try {
            this.mService.stopWifi();
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean addToBlacklist(String bssid) {
        try {
            this.mService.addToBlacklist(bssid);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean clearBlacklist() {
        try {
            this.mService.clearBlacklist();
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean addToSBlacklist(String bssid) {
        try {
            this.mService.addToSBlacklist(bssid);
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean clearSBlacklist() {
        try {
            this.mService.clearSBlacklist();
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    public String getSBlacklist() {
        try {
            return this.mService.getSBlacklist();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setTdlsEnabled(InetAddress remoteIPAddress, boolean enable) {
        try {
            this.mService.enableTdls(remoteIPAddress.getHostAddress(), enable);
        } catch (RemoteException e) {
        }
    }

    public void setTdlsEnabledWithMacAddress(String remoteMacAddress, boolean enable) {
        try {
            this.mService.enableTdlsWithMacAddress(remoteMacAddress, enable);
        } catch (RemoteException e) {
        }
    }

    public void startMediaShare(String remoteMacAddress, boolean enable) {
        if (enable) {
            try {
                this.mService.enableTdlsWithMacAddress("!" + remoteMacAddress, false);
            } catch (RemoteException e) {
                return;
            }
        }
        this.mService.enableTdlsWithMacAddress(remoteMacAddress, enable);
    }

    private static int putListener(Object listener) {
        if (listener == null) {
            return 0;
        }
        int key;
        synchronized (sListenerMapLock) {
            do {
                key = sListenerKey;
                sListenerKey = key + 1;
            } while (key == 0);
            sListenerMap.put(key, listener);
        }
        return key;
    }

    private static Object removeListener(int key) {
        if (key == 0) {
            return null;
        }
        Object listener;
        synchronized (sListenerMapLock) {
            listener = sListenerMap.get(key);
            sListenerMap.remove(key);
        }
        return listener;
    }

    private void init() {
        synchronized (sThreadRefLock) {
            int i = sThreadRefCount + 1;
            sThreadRefCount = i;
            if (i == 1) {
                Messenger messenger = getWifiServiceMessenger();
                if (messenger == null) {
                    sAsyncChannel = null;
                    return;
                }
                sHandlerThread = new HandlerThread(TAG);
                sAsyncChannel = new AsyncChannel();
                sConnected = new CountDownLatch(1);
                sHandlerThread.start();
                sAsyncChannel.connect(this.mContext, new ServiceHandler(sHandlerThread.getLooper()), messenger);
                try {
                    sConnected.await();
                } catch (InterruptedException e) {
                    Log.e(TAG, "interrupted wait at init");
                }
            }
        }
    }

    private void validateChannel() {
        if (sAsyncChannel == null) {
            throw new IllegalStateException("No permission to access and change wifi or a bad initialization");
        }
    }

    private void initConnectivityManager() {
        if (sCM == null) {
            sCM = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (sCM == null) {
                throw new IllegalStateException("Bad luck, ConnectivityService not started.");
            }
        }
    }

    private void registerPinningNetworkCallback() {
        initConnectivityManager();
        synchronized (sCM) {
            if (this.mNetworkCallback == null) {
                NetworkRequest request = new Builder().addTransportType(1).removeCapability(12).build();
                this.mNetworkCallback = new PinningNetworkCallback();
                try {
                    sCM.registerNetworkCallback(request, this.mNetworkCallback);
                } catch (SecurityException e) {
                    Log.d(TAG, "Failed to register network callback", e);
                }
            }
        }
    }

    private void unregisterPinningNetworkCallback() {
        initConnectivityManager();
        synchronized (sCM) {
            if (this.mNetworkCallback != null) {
                try {
                    sCM.unregisterNetworkCallback(this.mNetworkCallback);
                } catch (SecurityException e) {
                    Log.d(TAG, "Failed to unregister network callback", e);
                }
                this.mNetworkCallback = null;
            }
        }
    }

    public void connect(WifiConfiguration config, ActionListener listener) {
        if (config == null) {
            throw new IllegalArgumentException("config cannot be null");
        }
        validateChannel();
        sAsyncChannel.sendMessage(CONNECT_NETWORK, -1, putListener(listener), config);
    }

    public void connect(int networkId, ActionListener listener) {
        if (networkId < 0) {
            throw new IllegalArgumentException("Network id cannot be negative");
        }
        validateChannel();
        sAsyncChannel.sendMessage(CONNECT_NETWORK, networkId, putListener(listener));
    }

    public void save(WifiConfiguration config, ActionListener listener) {
        if (config == null) {
            throw new IllegalArgumentException("config cannot be null");
        }
        validateChannel();
        sAsyncChannel.sendMessage(SAVE_NETWORK, 0, putListener(listener), config);
    }

    public void forget(int netId, ActionListener listener) {
        if (netId < 0) {
            throw new IllegalArgumentException("Network id cannot be negative");
        }
        validateChannel();
        sAsyncChannel.sendMessage(FORGET_NETWORK, netId, putListener(listener));
    }

    public void disable(int netId, ActionListener listener) {
        if (netId < 0) {
            throw new IllegalArgumentException("Network id cannot be negative");
        }
        validateChannel();
        sAsyncChannel.sendMessage(DISABLE_NETWORK, netId, putListener(listener));
    }

    public void disableEphemeralNetwork(String SSID) {
        if (SSID == null) {
            throw new IllegalArgumentException("SSID cannot be null");
        }
        try {
            this.mService.disableEphemeralNetwork(SSID);
        } catch (RemoteException e) {
        }
    }

    public void startWps(WpsInfo config, WpsCallback listener) {
        if (config == null) {
            throw new IllegalArgumentException("config cannot be null");
        }
        validateChannel();
        sAsyncChannel.sendMessage(START_WPS, 0, putListener(listener), config);
    }

    public void cancelWps(WpsCallback listener) {
        validateChannel();
        sAsyncChannel.sendMessage(CANCEL_WPS, 0, putListener(listener));
    }

    public Messenger getWifiServiceMessenger() {
        Messenger messenger = null;
        try {
            messenger = this.mService.getWifiServiceMessenger();
        } catch (RemoteException e) {
        } catch (SecurityException e2) {
        }
        return messenger;
    }

    public String getConfigFile() {
        try {
            return this.mService.getConfigFile();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean setRoamTrigger(int roamTrigger) {
        try {
            return this.mService.setRoamTrigger(roamTrigger);
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getRoamTrigger() {
        try {
            return this.mService.getRoamTrigger();
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean setRoamDelta(int roamDelta) {
        try {
            return this.mService.setRoamDelta(roamDelta);
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getRoamDelta() {
        try {
            return this.mService.getRoamDelta();
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean setRoamScanPeriod(int roamScanPeriod) {
        try {
            return this.mService.setRoamScanPeriod(roamScanPeriod);
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getRoamScanPeriod() {
        try {
            return this.mService.getRoamScanPeriod();
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean setFullRoamScanPeriod(int fullRoamScanPeriod) {
        try {
            return this.mService.setFullRoamScanPeriod(fullRoamScanPeriod);
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getFullRoamScanPeriod() {
        try {
            return this.mService.getFullRoamScanPeriod();
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean setRoamBand(int roamBand) {
        try {
            return this.mService.setRoamBand(roamBand);
        } catch (RemoteException e) {
            return false;
        }
    }

    public int getRoamBand() {
        try {
            return this.mService.getRoamBand();
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean setCountryRev(String countryRev) {
        try {
            return this.mService.setCountryRev(countryRev);
        } catch (RemoteException e) {
            return false;
        }
    }

    public String getCountryRev() {
        try {
            return this.mService.getCountryRev();
        } catch (RemoteException e) {
            return null;
        }
    }

    public int getLinkStatus() {
        try {
            return this.mService.getLinkStatus();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public boolean setWifiIBSSEnabled(boolean enabled) {
        String packageName = this.mContext.getPackageName();
        try {
            Log.i(TAG, "setWifiIBSSEnabled : " + enabled);
            if (DBG) {
                Log.e(TAG, Log.getStackTraceString(new Throwable()));
            }
            Message msg = new Message();
            msg.what = 78;
            Bundle args = new Bundle();
            args.putString("extra_log", new String("WifiManager.setWifiIBSSEnabled(" + enabled + ") : " + packageName + "\n"));
            msg.obj = args;
            callSECApi(msg);
            return this.mService.setWifiIBSSEnabled(enabled);
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException occured. WifiManager.setWifiIBSSEnabled()");
            return false;
        }
    }

    public int getWifiIBSSState() {
        try {
            return this.mService.getWifiIBSSEnabledState();
        } catch (RemoteException e) {
            return 4;
        }
    }

    public boolean isWifiIBSSEnabled() {
        return getWifiIBSSState() == 3;
    }

    public List<WifiChannel> getSupportedChannels() {
        try {
            return this.mService.getChannelList();
        } catch (RemoteException e) {
            return null;
        }
    }

    public NetworkInfo getNetworkInfo() {
        try {
            return this.mService.getNetworkInfo();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean setIsFmcNetwork(boolean enable) {
        try {
            return this.mService.setIsFmcNetwork(enable);
        } catch (Exception e) {
            return false;
        }
    }

    public int callSECApi(Message msg) {
        if (msg.what == 0) {
            return 1;
        }
        try {
            return this.mService.callSECApi(msg);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public String callSECStringApi(Message msg) {
        try {
            return this.mService.callSECStringApi(msg);
        } catch (RemoteException e) {
            return null;
        }
    }

    public WifiLock createWifiLock(int lockType, String tag) {
        return new WifiLock(lockType, tag);
    }

    public WifiLock createWifiLock(String tag) {
        return new WifiLock(1, tag);
    }

    public MulticastLock createMulticastLock(String tag) {
        return new MulticastLock(tag);
    }

    public boolean isMulticastEnabled() {
        try {
            return this.mService.isMulticastEnabled();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean initializeMulticastFiltering() {
        try {
            this.mService.initializeMulticastFiltering();
            return true;
        } catch (RemoteException e) {
            return false;
        }
    }

    protected void finalize() throws Throwable {
        try {
            synchronized (sThreadRefLock) {
                int i = sThreadRefCount - 1;
                sThreadRefCount = i;
                if (i == 0 && sAsyncChannel != null) {
                    sAsyncChannel.disconnect();
                }
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public void startPPPOE(PPPOEConfig config) {
        try {
            Log.d("pppoe", "WifiManager: startPPPOE");
            this.mService.startPPPOE(config);
        } catch (RemoteException e) {
            Log.d("pppoe", "RemoteException");
        }
    }

    public void stopPPPOE() {
        try {
            Log.d("pppoe", "WifiManager: stopPPPOE");
            this.mService.stopPPPOE();
        } catch (RemoteException e) {
            Log.d("pppoe", "RemoteException");
        }
    }

    public PPPOEInfo getPPPOEInfo() {
        try {
            Log.d("pppoe", "WifiManager: getPPPOEInfo");
            return this.mService.getPPPOEInfo();
        } catch (RemoteException e) {
            return null;
        }
    }

    public void enableVerboseLogging(int verbose) {
        try {
            this.mService.enableVerboseLogging(verbose);
        } catch (Exception e) {
            Log.e(TAG, "enableVerboseLogging " + e.toString());
        }
    }

    public int getVerboseLoggingLevel() {
        try {
            return this.mService.getVerboseLoggingLevel();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public void enableAggressiveHandover(int enabled) {
        try {
            this.mService.enableAggressiveHandover(enabled);
        } catch (RemoteException e) {
        }
    }

    public int getAggressiveHandover() {
        try {
            return this.mService.getAggressiveHandover();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public void setAllowScansWithTraffic(int enabled) {
        try {
            this.mService.setAllowScansWithTraffic(enabled);
        } catch (RemoteException e) {
        }
    }

    public int getAllowScansWithTraffic() {
        try {
            return this.mService.getAllowScansWithTraffic();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public void factoryReset() {
        try {
            this.mService.factoryReset();
        } catch (RemoteException e) {
        }
    }

    public Network getCurrentNetwork() {
        try {
            return this.mService.getCurrentNetwork();
        } catch (RemoteException e) {
            return null;
        }
    }

    public boolean enableAutoJoinWhenAssociated(boolean enabled) {
        try {
            return this.mService.enableAutoJoinWhenAssociated(enabled);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean getEnableAutoJoinWhenAssociated() {
        try {
            return this.mService.getEnableAutoJoinWhenAssociated();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setHalBasedAutojoinOffload(int enabled) {
        try {
            this.mService.setHalBasedAutojoinOffload(enabled);
        } catch (RemoteException e) {
        }
    }

    public int getHalBasedAutojoinOffload() {
        try {
            return this.mService.getHalBasedAutojoinOffload();
        } catch (RemoteException e) {
            return 0;
        }
    }

    public int isReconnecting() {
        Message msg = new Message();
        msg.what = 200;
        try {
            return this.mService.callSECApi(msg);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean isAllowWifiWarning() {
        boolean isCscWifiEnableWarning;
        String ChinaNalSecurityType = CscFeature.getInstance().getString("CscFeature_Common_ConfigLocalSecurityPolicy");
        if (ChinaNalSecurityType.isEmpty() || !"ChinaNalSecurity".equals(ChinaNalSecurityType) || ProxyInfo.LOCAL_EXCL_LIST.equals("CHINA_UP")) {
            isCscWifiEnableWarning = false;
        } else {
            isCscWifiEnableWarning = true;
        }
        Log.i(TAG, "isAllowWifiWarning() -> isCscWifiEnableWarning : " + isCscWifiEnableWarning + " ChinaNalSecurityType : " + ChinaNalSecurityType);
        try {
            boolean isAllowPopup = this.mService.checkWarningPopup();
            if (isCscWifiEnableWarning && isAllowPopup) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException occured. WifiManager.isAllowWifiWarning()");
            return isCscWifiEnableWarning;
        }
    }

    public static boolean showWebBrowserAtCaptivePortal() {
        String mSalesCode = SystemProperties.get("ro.csc.sales_code");
        String salesCodeList = "TTR MAX TRG COA COS DDE DTM CRO DHR DNL TNL DPL TPL MBM TMH TMS TMT TMZ";
        if (mSalesCode != null) {
            String[] arr$ = salesCodeList.split(" ");
            int len$ = arr$.length;
            int i$ = 0;
            while (i$ < len$) {
                String Code = arr$[i$];
                if (Code == null || !Code.equals(mSalesCode)) {
                    i$++;
                } else {
                    if (DBG) {
                        Log.e(TAG, "SalesCode is " + mSalesCode);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isWifiSharingEnabled() {
        try {
            return this.mService.isWifiSharingEnabled();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean enableWifiSharing(boolean enable) {
        try {
            return this.mService.enableWifiSharing(enable);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean setProvisionSuccess(boolean set) {
        try {
            return this.mService.setProvisionSuccess(set);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean getProvisionSuccess() {
        try {
            return this.mService.getProvisionSuccess();
        } catch (RemoteException e) {
            return false;
        }
    }

    public static boolean suppressBrowserOnCaptivePortal() {
        String wifiSetupRunning = SystemProperties.get("persist.sys.vzw_wifi_running");
        if ("VZW".equals(CONFIGOPBRANDINGFORMOBILEAP) && wifiSetupRunning != null && AudioParameter.AUDIO_PARAMETER_VALUE_true.equals(wifiSetupRunning)) {
            return true;
        }
        return false;
    }

    public boolean isDetectedAsMaliciousHotspot(String bssid) {
        boolean z = false;
        if (!(bssid == null || bssid.equals(ProxyInfo.LOCAL_EXCL_LIST))) {
            try {
                z = this.mService.isDetectedAsMaliciousHotspot(bssid);
            } catch (RemoteException e) {
            }
        }
        return z;
    }

    public boolean isPasspointDefaultOn() {
        try {
            return this.mService.isPasspointDefaultOn();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isPasspointMenuVisible() {
        try {
            return this.mService.isPasspointMenuVisible();
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean isCaptivePortalException() {
        Log.i(TAG, "isCaptivePortalException");
        try {
            return this.mService.isCaptivePortalException();
        } catch (RemoteException e) {
            return false;
        }
    }

    public String getWifiVername() {
        Log.i(TAG, "getWifiVername");
        try {
            return this.mService.getWifiVerName();
        } catch (RemoteException e) {
            return "NG";
        }
    }
}
