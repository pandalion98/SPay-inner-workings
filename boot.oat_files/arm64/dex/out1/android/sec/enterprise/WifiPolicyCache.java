package android.sec.enterprise;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.UserInfo;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.os.UserManager;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.ims.ImsConferenceState;
import com.android.internal.R;
import com.android.internal.telephony.PhoneConstants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WifiPolicyCache {
    public static final String ACTION_WIFI_POLICY_STATE_CHANGED = "edm.intent.action.internal.WIFI_STATE_CHANGED";
    public static final String EXTRA_USER_ID = "android.app.enterprise.extra.EXTRA_USER_ID";
    public static final String EXTRA_WIFI_TYPE_CHANGED = "android.app.enterprise.extra.WIFI_TYPE_CHANGED";
    public static final int NO_USER = -1;
    public static final String TYPE_ALLOW_AUTOMATIC_CONNECTION = "ALLOW_AUTOMATIC_CONNECTION";
    public static final String TYPE_ALLOW_STATE_CHANGE = "ALLOW_STATE_CHANGES";
    public static final String TYPE_ALLOW_USER_CHANGES = "ALLOW_USER_CHANGES";
    public static final String TYPE_ALLOW_USER_PROFILES = "ALLOW_USER_PROFILES";
    public static final int TYPE_BLACK_SSID_LIST = 0;
    public static final String TYPE_BLOCKED_NETWORKS = "BLOCKED_NETWORKS";
    public static final String TYPE_ENTERPRISE_SSIDS = "ENTERPRISE_SSIDS";
    public static final String TYPE_MINIMUM_SECURITY_LEVEL = "MINIMUM_SECURITY_LEVEL";
    public static final String TYPE_PROMPT_CREDENTIALS_ENABLED = "PROMPT_CREDENTIALS_ENABLED";
    public static final String TYPE_WHITE_BLACK_SSID_LIST = "WHITE_BLACK_SSID_LIST";
    public static final int TYPE_WHITE_SSID_LIST = 1;
    public static final String TYPE_WIFI_ALLOWED = "WIFI_ALLOWED";
    private static final Object mSync = new Object();
    private static WifiPolicyCache sInstance = null;
    private boolean mAllowAutomaticConnections = true;
    private boolean mAllowStateChange = true;
    private boolean mAllowUserChanges = true;
    private HashMap<Integer, Boolean> mAllowUserProfiles = new HashMap();
    private List<String> mBlackListedSSIDs = new ArrayList();
    private List<String> mBlockedSsids = new ArrayList();
    private Context mContext;
    private List<String> mEnterpriseSsids = new ArrayList();
    private int mMinimumSecurityLevel = 0;
    private boolean mPromptCredentialsEnabled = true;
    private UserManager mUserManager;
    private List<String> mWhiteListedSSIDs = new ArrayList();
    private boolean mWifiAllowed = true;
    private WifiPolicy mWifiPolicy = EnterpriseDeviceManager.getInstance().getWifiPolicy();

    public static WifiPolicyCache getInstance(Context context) {
        WifiPolicyCache wifiPolicyCache;
        synchronized (mSync) {
            if (sInstance == null) {
                sInstance = new WifiPolicyCache(context);
            }
            wifiPolicyCache = sInstance;
        }
        return wifiPolicyCache;
    }

    private WifiPolicyCache(Context context) {
        this.mContext = context;
        this.mUserManager = (UserManager) this.mContext.getSystemService(ImsConferenceState.USER);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_WIFI_POLICY_STATE_CHANGED);
        context.registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(WifiPolicyCache.ACTION_WIFI_POLICY_STATE_CHANGED)) {
                    Bundle extras = intent.getExtras();
                    if (extras != null) {
                        WifiPolicyCache.this.readVariables(extras.getString(WifiPolicyCache.EXTRA_WIFI_TYPE_CHANGED), extras.getInt(WifiPolicyCache.EXTRA_USER_ID));
                        return;
                    }
                    WifiPolicyCache.this.readVariables(null, -1);
                }
            }
        }, intentFilter);
        readVariables(null, -1);
    }

    private synchronized void readVariables(String type, int userId) {
        if (type == null) {
            this.mEnterpriseSsids = this.mWifiPolicy.getNetworkSSIDList();
            this.mBlockedSsids = this.mWifiPolicy.getBlockedNetworks();
            this.mAllowUserChanges = this.mWifiPolicy.getAllowUserPolicyChanges();
            this.mWifiAllowed = this.mWifiPolicy.isWifiStateChangeAllowed();
            this.mPromptCredentialsEnabled = this.mWifiPolicy.getPromptCredentialsEnabled();
            this.mMinimumSecurityLevel = this.mWifiPolicy.getMinimumRequiredSecurity();
            this.mBlackListedSSIDs = this.mWifiPolicy.getWifiSsidRestrictionList(0);
            this.mWhiteListedSSIDs = this.mWifiPolicy.getWifiSsidRestrictionList(1);
            this.mAllowAutomaticConnections = this.mWifiPolicy.getAutomaticConnectionToWifi();
            List<UserInfo> usersList = this.mUserManager.getUsers();
            this.mAllowUserProfiles.clear();
            for (UserInfo i : usersList) {
                userId = i.getUserHandle().getIdentifier();
                this.mAllowUserProfiles.put(Integer.valueOf(userId), Boolean.valueOf(this.mWifiPolicy.getAllowUserProfiles(false, userId)));
            }
            this.mAllowStateChange = this.mWifiPolicy.isWifiStateChangeAllowed();
        } else if (type.equals(TYPE_ENTERPRISE_SSIDS)) {
            this.mEnterpriseSsids = this.mWifiPolicy.getNetworkSSIDList();
        } else if (type.equals(TYPE_BLOCKED_NETWORKS)) {
            this.mBlockedSsids = this.mWifiPolicy.getBlockedNetworks();
        } else if (type.equals(TYPE_ALLOW_USER_CHANGES)) {
            this.mAllowUserChanges = this.mWifiPolicy.getAllowUserPolicyChanges();
        } else if (type.equals(TYPE_ALLOW_USER_PROFILES)) {
            this.mAllowUserProfiles.put(Integer.valueOf(userId), Boolean.valueOf(this.mWifiPolicy.getAllowUserProfiles(false, userId)));
        } else if (type.equals(TYPE_WIFI_ALLOWED)) {
            this.mWifiAllowed = this.mWifiPolicy.isWifiStateChangeAllowed();
        } else if (type.equals(TYPE_PROMPT_CREDENTIALS_ENABLED)) {
            this.mPromptCredentialsEnabled = this.mWifiPolicy.getPromptCredentialsEnabled();
        } else if (type.equals(TYPE_MINIMUM_SECURITY_LEVEL)) {
            this.mMinimumSecurityLevel = this.mWifiPolicy.getMinimumRequiredSecurity();
        } else if (type.equals(TYPE_WHITE_BLACK_SSID_LIST)) {
            this.mBlackListedSSIDs = this.mWifiPolicy.getWifiSsidRestrictionList(0);
            this.mWhiteListedSSIDs = this.mWifiPolicy.getWifiSsidRestrictionList(1);
        } else if (TYPE_ALLOW_AUTOMATIC_CONNECTION.equals(type)) {
            this.mAllowAutomaticConnections = this.mWifiPolicy.getAutomaticConnectionToWifi();
        } else if (type.equals(TYPE_ALLOW_STATE_CHANGE)) {
            this.mAllowStateChange = this.mWifiPolicy.isWifiStateChangeAllowed();
        }
    }

    public synchronized boolean isEnterpriseNetwork(String ssid) {
        boolean contains;
        if (ssid != null) {
            contains = this.mEnterpriseSsids.contains(removeDoubleQuotes(ssid));
        } else {
            contains = false;
        }
        return contains;
    }

    public synchronized boolean getAllowUserChanges() {
        return this.mAllowUserChanges;
    }

    public synchronized boolean getPromptCredentialsEnabled() {
        return this.mPromptCredentialsEnabled;
    }

    public synchronized boolean getAllowUserProfiles() {
        boolean booleanValue;
        int userId = ActivityManager.getCurrentUser();
        if (this.mAllowUserProfiles.containsKey(Integer.valueOf(userId))) {
            booleanValue = ((Boolean) this.mAllowUserProfiles.get(Integer.valueOf(userId))).booleanValue();
        } else {
            booleanValue = true;
        }
        return booleanValue;
    }

    public synchronized boolean isNetworkAllowed(WifiConfiguration config, boolean showMsg) {
        boolean z = true;
        synchronized (this) {
            if (config != null) {
                if (this.mBlockedSsids.contains(removeDoubleQuotes(config.SSID))) {
                    if (showMsg) {
                        showMessage(R.string.dpm_wifi_blocked_network);
                    }
                    z = false;
                } else if (WifiPolicy.getSecurityLevel(WifiPolicy.getLinkSecurity(config)) < WifiPolicy.getSecurityLevel(this.mMinimumSecurityLevel)) {
                    if (showMsg) {
                        showMessage(R.string.dpm_wifi_misc_network_insecure);
                    }
                    z = false;
                } else if (!this.mWhiteListedSSIDs.contains(removeDoubleQuotes(config.SSID)) && (this.mBlackListedSSIDs.contains(removeDoubleQuotes(config.SSID)) || this.mBlackListedSSIDs.contains(PhoneConstants.APN_TYPE_ALL))) {
                    if (showMsg) {
                        showMessage(R.string.dpm_wifi_blocked_network);
                    }
                    z = false;
                }
            }
        }
        return z;
    }

    public synchronized boolean isWifiAllowed(boolean showMsg) {
        return this.mWifiAllowed;
    }

    public synchronized boolean isWifiStateChangeAllowed(boolean showMsg) {
        return this.mAllowStateChange;
    }

    private String removeDoubleQuotes(String string) {
        if (string == null) {
            return null;
        }
        int length = string.length();
        if (length > 1 && string.charAt(0) == '\"' && string.charAt(length - 1) == '\"') {
            return string.substring(1, length - 1);
        }
        return string;
    }

    public WifiConfiguration updateAllowedFields(WifiConfiguration config, WifiConfiguration edmConfig, int newConfigSec) {
        int edmConfigSec = WifiPolicy.getLinkSecurity(edmConfig);
        if (newConfigSec != edmConfigSec || newConfigSec == 0) {
            return null;
        }
        switch (edmConfigSec) {
            case 1:
                int index = config.wepTxKeyIndex;
                edmConfig.wepTxKeyIndex = index;
                edmConfig.wepKeys[index] = config.wepKeys[index];
                return edmConfig;
            case 2:
            case 8:
                if (config.preSharedKey == null || PhoneConstants.APN_TYPE_ALL.equals(config.preSharedKey)) {
                    return edmConfig;
                }
                edmConfig.preSharedKey = config.preSharedKey;
                return edmConfig;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
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
            case 30:
                edmConfig.enterpriseConfig.setIdentity(config.enterpriseConfig.getIdentity());
                edmConfig.enterpriseConfig.setAnonymousIdentity(config.enterpriseConfig.getAnonymousIdentity());
                String password = config.enterpriseConfig.getPassword();
                if (TextUtils.isEmpty(password)) {
                    return edmConfig;
                }
                edmConfig.enterpriseConfig.setPassword(password);
                return edmConfig;
            default:
                return null;
        }
    }

    public boolean getAutomaticConnectionToWifi() {
        return this.mAllowAutomaticConnections;
    }

    private void showMessage(int resId) {
        Toast.makeText(this.mContext, resId, 0).show();
    }
}
