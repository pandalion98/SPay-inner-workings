package android.net.wifi;

import android.net.IpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.net.IpConfiguration.ProxySettings;
import android.net.ProxyInfo;
import android.net.StaticIpConfiguration;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.sec.android.app.CscFeature;
import java.util.BitSet;
import java.util.HashMap;

public class WifiConfiguration implements Parcelable {
    public static final int AUTO_JOIN_DELETED = 200;
    public static final int AUTO_JOIN_DISABLED_NO_CREDENTIALS = 160;
    public static final int AUTO_JOIN_DISABLED_ON_AUTH_FAILURE = 128;
    public static final int AUTO_JOIN_DISABLED_USER_ACTION = 161;
    public static final int AUTO_JOIN_ENABLED = 0;
    public static final int AUTO_JOIN_TEMPORARY_DISABLED = 1;
    public static final int AUTO_JOIN_TEMPORARY_DISABLED_AT_SUPPLICANT = 64;
    public static final int AUTO_JOIN_TEMPORARY_DISABLED_LINK_ERRORS = 32;
    public static int A_BAND_PREFERENCE_RSSI_THRESHOLD = -65;
    public static int BAD_RSSI_24 = -87;
    public static int BAD_RSSI_5 = -82;
    public static final Creator<WifiConfiguration> CREATOR = new Creator<WifiConfiguration>() {
        public WifiConfiguration createFromParcel(Parcel in) {
            int i;
            boolean z;
            boolean z2 = true;
            WifiConfiguration config = new WifiConfiguration();
            config.networkId = in.readInt();
            config.status = in.readInt();
            config.disableReason = in.readInt();
            config.SSID = in.readString();
            config.BSSID = in.readString();
            config.apBand = in.readInt();
            config.apChannel = in.readInt();
            config.autoJoinBSSID = in.readString();
            config.FQDN = in.readString();
            config.providerFriendlyName = in.readString();
            int numRoamingConsortiumIds = in.readInt();
            config.roamingConsortiumIds = new long[numRoamingConsortiumIds];
            for (i = 0; i < numRoamingConsortiumIds; i++) {
                config.roamingConsortiumIds[i] = in.readLong();
            }
            config.preSharedKey = in.readString();
            for (i = 0; i < config.wepKeys.length; i++) {
                config.wepKeys[i] = in.readString();
            }
            config.wepTxKeyIndex = in.readInt();
            config.priority = in.readInt();
            config.hiddenSSID = in.readInt() != 0;
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.requirePMF = z;
            config.updateIdentifier = in.readString();
            config.allowedKeyManagement = WifiConfiguration.readBitSet(in);
            config.allowedProtocols = WifiConfiguration.readBitSet(in);
            config.allowedAuthAlgorithms = WifiConfiguration.readBitSet(in);
            config.allowedPairwiseCiphers = WifiConfiguration.readBitSet(in);
            config.allowedGroupCiphers = WifiConfiguration.readBitSet(in);
            config.enterpriseConfig = (WifiEnterpriseConfig) in.readParcelable(null);
            config.mIpConfiguration = (IpConfiguration) in.readParcelable(null);
            config.dhcpServer = in.readString();
            config.defaultGwMacAddress = in.readString();
            config.autoJoinStatus = in.readInt();
            config.selfAdded = in.readInt() != 0;
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.didSelfAdd = z;
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.validatedInternetAccess = z;
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.ephemeral = z;
            config.creatorUid = in.readInt();
            config.lastConnectUid = in.readInt();
            config.lastUpdateUid = in.readInt();
            config.creatorName = in.readString();
            config.lastUpdateName = in.readString();
            config.blackListTimestamp = in.readLong();
            config.lastConnectionFailure = in.readLong();
            config.lastRoamingFailure = in.readLong();
            config.lastRoamingFailureReason = in.readInt();
            config.roamingFailureBlackListTimeMilli = in.readLong();
            config.numConnectionFailures = in.readInt();
            config.numIpConfigFailures = in.readInt();
            config.numAuthFailures = in.readInt();
            config.numScorerOverride = in.readInt();
            config.numScorerOverrideAndSwitchedNetwork = in.readInt();
            config.numAssociation = in.readInt();
            config.numUserTriggeredWifiDisableLowRSSI = in.readInt();
            config.numUserTriggeredWifiDisableBadRSSI = in.readInt();
            config.numUserTriggeredWifiDisableNotHighRSSI = in.readInt();
            config.numTicksAtLowRSSI = in.readInt();
            config.numTicksAtBadRSSI = in.readInt();
            config.numTicksAtNotHighRSSI = in.readInt();
            config.numUserTriggeredJoinAttempts = in.readInt();
            config.autoJoinUseAggressiveJoinAttemptThreshold = in.readInt();
            config.autoJoinBailedDueToLowRssi = in.readInt() != 0;
            config.userApproved = in.readInt();
            config.numNoInternetAccessReports = in.readInt();
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.noInternetAccessExpected = z;
            config.samsungSpecificFlags = WifiConfiguration.readBitSet(in);
            config.mode = OperationMode.valueOf(in.readString());
            config.frequency = in.readInt();
            config.channel = in.readInt();
            config.macaddrAcl = in.readInt();
            config.maxclient = in.readInt();
            config.vendorIE = in.readInt();
            config.apIsolate = in.readInt();
            config.wpsStatus = in.readInt();
            config.txPowerMode = in.readInt();
            if (config.allowedKeyManagement.get(5)) {
                config.wapiPskType = in.readInt();
            } else if (config.allowedKeyManagement.get(6)) {
                config.wapiAsCert = in.readString();
                config.wapiUserCert = in.readString();
                config.wapiCertIndex = in.readInt();
            }
            config.HESSID = in.readString();
            config.isHS20AP = in.readInt();
            config.isHS20Home = in.readInt();
            config.HS20CredId = in.readInt();
            config.HS20OperatorName = in.readString();
            config.HS20VenueName = in.readString();
            config.HS20OpURL = in.readString();
            config.recoverableRSSI = in.readInt();
            config.inRecoverArea = in.readInt() != 0;
            config.disabledTime = in.readLong();
            config.notInRangeTime = in.readLong();
            config.poorBSSID = in.readString();
            config.autojoin = in.readInt();
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.isCaptivePortal = z;
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.isAuthenticated = z;
            config.loginUrl = in.readString();
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.isUsableInternet = z;
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.skipInternetCheck = z;
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.isSharedAp = z;
            config.expiration = in.readString();
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            config.isVerifiedPassword = z;
            config.sim_num = in.readInt();
            if (in.readInt() == 0) {
                z2 = false;
            }
            config.isVendorSpecificSsid = z2;
            return config;
        }

        public WifiConfiguration[] newArray(int size) {
            return new WifiConfiguration[size];
        }
    };
    public static final int DISABLED_ASSOCIATION_REJECT = 4;
    public static final int DISABLED_AUTH_FAILURE = 3;
    public static final int DISABLED_BY_WIFI_MANAGER = 5;
    public static final int DISABLED_CAPTIVE_PORTAL = 13;
    public static final int DISABLED_DHCP_FAILURE = 2;
    public static final int DISABLED_DNS_FAILURE = 1;
    public static final int DISABLED_EAP_GENERAL_FAILURE = 9;
    public static final int DISABLED_NOT_SUBSCRIBED = 7;
    public static final int DISABLED_NO_INTERNET = 12;
    public static final int DISABLED_POOR_CONNECTIVITY = 11;
    public static final int DISABLED_SCC_DIFF_FREQ = 10;
    public static final int DISABLED_SPECIAL_SSID_FOR_MALICIOUS_HOTSPOT = 17;
    public static final int DISABLED_TEMPORARY_DENIED = 8;
    public static final int DISABLED_UNKNOWN_REASON = 0;
    public static final int DISABLED_UNSTABLE_AP = 16;
    public static final int DISABLE_NETWORK_BY_FWS = 15;
    public static final int DISABLE_NETWORK_BY_USER = 14;
    public static int GOOD_RSSI_24 = -65;
    public static int GOOD_RSSI_5 = -60;
    public static int G_BAND_PREFERENCE_RSSI_THRESHOLD = -75;
    public static int HOME_NETWORK_RSSI_BOOST = 5;
    public static final String HS20CREDID = "HS20CredId";
    public static final String HS20OPERATORNAME = "HS20OperatorName";
    public static final String HS20VENUENAME = "HS20VenueName";
    public static int INITIAL_AUTO_JOIN_ATTEMPT_MIN_24 = -80;
    public static int INITIAL_AUTO_JOIN_ATTEMPT_MIN_5 = -70;
    public static final int INVALID_NETWORK_ID = -1;
    public static int INVALID_RSSI = WifiInfo.INVALID_RSSI;
    public static final String ISHS20AP = "isHS20AP";
    public static final String ISHS20HOME = "isHS20Home";
    public static int LOW_RSSI_24 = -77;
    public static int LOW_RSSI_5 = -72;
    public static int MAX_INITIAL_AUTO_JOIN_RSSI_BOOST = 8;
    public static int ROAMING_FAILURE_AUTH_FAILURE = 2;
    public static int ROAMING_FAILURE_IP_CONFIG = 1;
    public static final String SIM_NUMBER = "sim_num";
    private static final String TAG = "WifiConfiguration";
    public static int UNBLACKLIST_THRESHOLD_24_HARD = -68;
    public static int UNBLACKLIST_THRESHOLD_24_SOFT = -77;
    public static int UNBLACKLIST_THRESHOLD_5_HARD = -56;
    public static int UNBLACKLIST_THRESHOLD_5_SOFT = -63;
    public static final int UNKNOWN_UID = -1;
    public static int UNWANTED_BLACKLIST_HARD_BUMP = 8;
    public static int UNWANTED_BLACKLIST_SOFT_BUMP = 4;
    public static int UNWANTED_BLACKLIST_SOFT_RSSI_24 = -80;
    public static int UNWANTED_BLACKLIST_SOFT_RSSI_5 = -70;
    public static final int USER_APPROVED = 1;
    public static final int USER_BANNED = 2;
    public static final int USER_PENDING = 3;
    public static final int USER_UNSPECIFIED = 0;
    public static final int WAPI_ASCII_PASSWORD = 0;
    public static final int WAPI_HEX_PASSWORD = 1;
    public static final String authenticatedVarName = "authenticated";
    public static final String autojoinVarName = "autojoin";
    public static final String bssidVarName = "bssid";
    public static final String captivePortalVarName = "captive_portal";
    public static final String expirationVarName = "expiration";
    public static final String frequencyVarName = "frequency";
    public static final String hessidVarName = "hessid";
    public static final String hiddenSSIDVarName = "scan_ssid";
    public static final String isUsableInternetVarName = "usable_internet";
    public static final String isVendorSpecSsidVarName = "vendor_spec_ssid";
    public static final String loginUrlVarName = "login_url";
    public static final String modeVarName = "mode";
    public static final String pmfVarName = "ieee80211w";
    public static final String priorityVarName = "priority";
    public static final String pskVarName = "psk";
    public static final String sharedApVarName = "shared";
    public static final String skipInternetCheckVarName = "skip_internet_check";
    public static final String ssidVarName = "ssid";
    public static final String updateIdentiferVarName = "update_identifier";
    public static final String verifiedPasswordVarName = "verified_password";
    public static final String wapiAsCertVarName = "wapi_as_cert";
    public static final String wapiCertIndexVarName = "cert_index";
    public static final String wapiPskTypeVarName = "psk_key_type";
    public static final String wapiUserCertVarName = "wapi_user_cert";
    public static final String[] wepKeyVarNames = new String[]{"wep_key0", "wep_key1", "wep_key2", "wep_key3"};
    public static final String wepTxKeyIdxVarName = "wep_tx_keyidx";
    public String BSSID;
    public String FQDN;
    public String HESSID;
    public int HS20CredId;
    public String HS20OpURL;
    public String HS20OperatorName;
    public String HS20VenueName;
    public String SSID;
    public BitSet allowedAuthAlgorithms;
    public BitSet allowedGroupCiphers;
    public BitSet allowedKeyManagement;
    public BitSet allowedPairwiseCiphers;
    public BitSet allowedProtocols;
    public int apBand;
    public int apChannel;
    public int apIsolate;
    public String autoJoinBSSID;
    public boolean autoJoinBailedDueToLowRssi;
    public int autoJoinStatus;
    public int autoJoinUseAggressiveJoinAttemptThreshold;
    public int autojoin;
    public long blackListTimestamp;
    public int channel;
    public HashMap<String, Integer> connectChoices;
    public String creationTime;
    public String creatorName;
    public int creatorUid;
    public String defaultGwMacAddress;
    public String dhcpServer;
    public boolean didSelfAdd;
    public boolean dirty;
    public int disableReason;
    public long disabledTime;
    public WifiEnterpriseConfig enterpriseConfig;
    public boolean ephemeral;
    public String expiration;
    public int frequency;
    public boolean hiddenSSID;
    public boolean inRecoverArea;
    public boolean isAuthenticated;
    public boolean isCaptivePortal;
    public int isHS20AP;
    public int isHS20Home;
    public boolean isSharedAp;
    public boolean isUsableInternet;
    public boolean isVendorSpecificSsid;
    public boolean isVerifiedPassword;
    public int lastConnectUid;
    public long lastConnected;
    public long lastConnectionFailure;
    public long lastDisconnected;
    public String lastFailure;
    public long lastRoamingFailure;
    public int lastRoamingFailureReason;
    public String lastUpdateName;
    public int lastUpdateUid;
    public HashMap<String, Integer> linkedConfigurations;
    public String loginUrl;
    String mCachedConfigKey;
    private IpConfiguration mIpConfiguration;
    public int macaddrAcl;
    public int maxclient;
    public OperationMode mode;
    public int networkId;
    public boolean noInternetAccessExpected;
    public long notInRangeTime;
    public int numAssociation;
    public int numAuthFailures;
    public int numConnectionFailures;
    public int numIpConfigFailures;
    public int numNoInternetAccessReports;
    public int numScorerOverride;
    public int numScorerOverrideAndSwitchedNetwork;
    public int numTicksAtBadRSSI;
    public int numTicksAtLowRSSI;
    public int numTicksAtNotHighRSSI;
    public int numUserTriggeredJoinAttempts;
    public int numUserTriggeredWifiDisableBadRSSI;
    public int numUserTriggeredWifiDisableLowRSSI;
    public int numUserTriggeredWifiDisableNotHighRSSI;
    public String peerWifiConfiguration;
    public String poorBSSID;
    public String preSharedKey;
    public int priority;
    public String providerFriendlyName;
    public int recoverableRSSI;
    public boolean requirePMF;
    public long[] roamingConsortiumIds;
    public long roamingFailureBlackListTimeMilli;
    public BitSet samsungSpecificFlags;
    public boolean selfAdded;
    public int sim_num;
    public boolean skipInternetCheck;
    public int status;
    public int txPowerMode;
    public String updateIdentifier;
    public String updateTime;
    public int userApproved;
    public boolean validatedInternetAccess;
    public int vendorIE;
    public Visibility visibility;
    public String wapiAsCert;
    public int wapiCertIndex;
    public int wapiPskType;
    public String wapiUserCert;
    public String[] wepKeys;
    public int wepTxKeyIndex;
    public int wpsStatus;

    public static class AuthAlgorithm {
        public static final int LEAP = 2;
        public static final int OPEN = 0;
        public static final int SHARED = 1;
        public static final String[] strings = new String[]{"OPEN", "SHARED", "LEAP"};
        public static final String varName = "auth_alg";

        private AuthAlgorithm() {
        }
    }

    public static class GroupCipher {
        public static final int CCMP = 3;
        public static final int TKIP = 2;
        public static final int WEP104 = 1;
        public static final int WEP40 = 0;
        public static final String[] strings = new String[]{"WEP40", "WEP104", "TKIP", "CCMP"};
        public static final String varName = "group";

        private GroupCipher() {
        }
    }

    public static class KeyMgmt {
        public static final int CCKM = 7;
        public static final int FT_EAP = 9;
        public static final int FT_PSK = 8;
        public static final int IEEE8021X = 3;
        public static final int NONE = 0;
        public static final int OSEN = 10;
        public static final int WAPI_CERT = 6;
        public static final int WAPI_PSK = 5;
        public static final int WPA2_PSK = 4;
        public static final int WPA_EAP = 2;
        public static final int WPA_PSK = 1;
        public static final String[] strings = new String[]{"NONE", "WPA_PSK", "WPA_EAP", "IEEE8021X", "WPA2_PSK", "WAPI_PSK", "WAPI_CERT", "CCKM", "FT_PSK", "FT_EAP", "OSEN"};
        public static final String varName = "key_mgmt";

        private KeyMgmt() {
        }
    }

    public enum OperationMode {
        INFRA,
        IBSS
    }

    public static class PairwiseCipher {
        public static final int CCMP = 2;
        public static final int NONE = 0;
        public static final int TKIP = 1;
        public static final String[] strings = new String[]{"NONE", "TKIP", "CCMP"};
        public static final String varName = "pairwise";

        private PairwiseCipher() {
        }
    }

    public static class Protocol {
        public static final int OSEN = 3;
        public static final int RSN = 1;
        public static final int WAPI = 2;
        public static final int WPA = 0;
        public static final String[] strings = new String[]{"WPA", "RSN", "WAPI", "OSEN"};
        public static final String varName = "proto";

        private Protocol() {
        }
    }

    public static class SamsungFlag {
        public static final int CLEAR_ALL = 4;
        public static final int SEC_INTRANET_AP = 3;
        public static final int SEC_MOBILE_AP = 1;
        public static final int SEC_OXYGEN_NETWORK = 2;
        public static final int SKIP_INTERNET_SERVICE_CHECK = 0;
        public static final String[] strings = new String[]{"ISC", "SECAP", "OXG", "INT", "CLR"};
        public static final String varName = "sec_flags";

        private SamsungFlag() {
        }
    }

    public static class Status {
        public static final int CURRENT = 0;
        public static final int DISABLED = 1;
        public static final int ENABLED = 2;
        public static final String[] strings = new String[]{"current", "disabled", "enabled"};

        private Status() {
        }
    }

    public static final class Visibility {
        public String BSSID24;
        public String BSSID5;
        public long age24;
        public long age5;
        public int bandPreferenceBoost;
        public int currentNetworkBoost;
        public int lastChoiceBoost;
        public String lastChoiceConfig;
        public int num24;
        public int num5;
        public int rssi24;
        public int rssi5;
        public int score;

        public Visibility() {
            this.rssi5 = WifiConfiguration.INVALID_RSSI;
            this.rssi24 = WifiConfiguration.INVALID_RSSI;
        }

        public Visibility(Visibility source) {
            this.rssi5 = source.rssi5;
            this.rssi24 = source.rssi24;
            this.age24 = source.age24;
            this.age5 = source.age5;
            this.num24 = source.num24;
            this.num5 = source.num5;
            this.BSSID5 = source.BSSID5;
            this.BSSID24 = source.BSSID24;
        }

        public String toString() {
            StringBuilder sbuf = new StringBuilder();
            sbuf.append("[");
            if (this.rssi24 > WifiConfiguration.INVALID_RSSI) {
                sbuf.append(Integer.toString(this.rssi24));
                sbuf.append(",");
                sbuf.append(Integer.toString(this.num24));
                if (this.BSSID24 != null) {
                    sbuf.append(",").append(this.BSSID24);
                }
            }
            sbuf.append("; ");
            if (this.rssi5 > WifiConfiguration.INVALID_RSSI) {
                sbuf.append(Integer.toString(this.rssi5));
                sbuf.append(",");
                sbuf.append(Integer.toString(this.num5));
                if (this.BSSID5 != null) {
                    sbuf.append(",").append(this.BSSID5);
                }
            }
            if (this.score != 0) {
                sbuf.append("; ").append(this.score);
                sbuf.append(", ").append(this.currentNetworkBoost);
                sbuf.append(", ").append(this.bandPreferenceBoost);
                if (this.lastChoiceConfig != null) {
                    sbuf.append(", ").append(this.lastChoiceBoost);
                    sbuf.append(", ").append(this.lastChoiceConfig);
                }
            }
            sbuf.append("]");
            return sbuf.toString();
        }
    }

    public void setVisibility(Visibility status) {
        this.visibility = status;
    }

    public boolean hasNoInternetAccess() {
        return this.numNoInternetAccessReports > 0 && !this.validatedInternetAccess;
    }

    public WifiConfiguration() {
        this.apBand = 0;
        this.apChannel = 0;
        this.userApproved = 0;
        this.roamingFailureBlackListTimeMilli = 1000;
        this.networkId = -1;
        this.SSID = null;
        this.BSSID = null;
        this.FQDN = null;
        this.roamingConsortiumIds = new long[0];
        this.priority = 0;
        this.hiddenSSID = false;
        this.disableReason = 0;
        this.allowedKeyManagement = new BitSet();
        this.allowedProtocols = new BitSet();
        this.allowedAuthAlgorithms = new BitSet();
        this.allowedPairwiseCiphers = new BitSet();
        this.allowedGroupCiphers = new BitSet();
        this.wepKeys = new String[4];
        for (int i = 0; i < this.wepKeys.length; i++) {
            this.wepKeys[i] = null;
        }
        this.enterpriseConfig = new WifiEnterpriseConfig();
        this.autoJoinStatus = 0;
        this.selfAdded = false;
        this.didSelfAdd = false;
        this.ephemeral = false;
        this.validatedInternetAccess = false;
        this.mIpConfiguration = new IpConfiguration();
        this.lastUpdateUid = -1;
        this.creatorUid = -1;
        this.samsungSpecificFlags = new BitSet();
        this.mode = OperationMode.INFRA;
        this.frequency = 0;
        this.channel = 0;
        this.macaddrAcl = 3;
        WifiApCust.getInstance();
        this.maxclient = WifiApCust.mDefaultMaxClientNum;
        this.vendorIE = 0;
        this.apIsolate = 0;
        this.wpsStatus = 1;
        this.txPowerMode = 0;
        this.wapiAsCert = null;
        this.wapiUserCert = null;
        this.wapiCertIndex = -1;
        this.wapiPskType = -1;
        this.HESSID = null;
        this.isHS20AP = 0;
        this.isHS20Home = 0;
        this.HS20CredId = -1;
        this.HS20OperatorName = null;
        this.HS20VenueName = null;
        this.HS20OpURL = null;
        this.recoverableRSSI = -200;
        this.inRecoverArea = false;
        this.disabledTime = 0;
        this.notInRangeTime = 0;
        this.poorBSSID = null;
        this.autojoin = 1;
        this.isCaptivePortal = false;
        this.isAuthenticated = false;
        this.loginUrl = null;
        this.isUsableInternet = false;
        this.skipInternetCheck = false;
        this.isSharedAp = false;
        this.expiration = null;
        this.isVerifiedPassword = false;
        this.sim_num = 0;
        this.isVendorSpecificSsid = false;
    }

    public boolean isPasspoint() {
        return (TextUtils.isEmpty(this.FQDN) || TextUtils.isEmpty(this.providerFriendlyName) || this.enterpriseConfig == null || this.enterpriseConfig.getEapMethod() == -1) ? false : true;
    }

    public boolean isLinked(WifiConfiguration config) {
        if (config.linkedConfigurations == null || this.linkedConfigurations == null || config.linkedConfigurations.get(configKey()) == null || this.linkedConfigurations.get(config.configKey()) == null) {
            return false;
        }
        return true;
    }

    public boolean isEnterprise() {
        return this.allowedKeyManagement.get(2) || this.allowedKeyManagement.get(3);
    }

    public void setAutoJoinStatus(int status) {
        if (status < 0) {
            status = 0;
        }
        if (status == 0) {
            this.blackListTimestamp = 0;
        } else if (status > this.autoJoinStatus) {
            this.blackListTimestamp = System.currentTimeMillis();
        }
        if (status != this.autoJoinStatus) {
            this.autoJoinStatus = status;
            this.dirty = true;
        }
    }

    public String toString() {
        long diff;
        StringBuilder sbuf = new StringBuilder();
        if (this.status == 0) {
            sbuf.append("* ");
        } else if (this.status == 1) {
            sbuf.append("- DSBLE ");
        }
        sbuf.append("ID: ").append(this.networkId).append(" SSID: ").append(this.SSID).append(" PROVIDER-NAME: ").append(this.providerFriendlyName).append(" BSSID: ").append(this.BSSID).append(" FQDN: ").append(this.FQDN).append(" PRIO: ").append(this.priority).append('\n');
        if (this.numConnectionFailures > 0) {
            sbuf.append(" numConnectFailures ").append(this.numConnectionFailures).append("\n");
        }
        if (this.numIpConfigFailures > 0) {
            sbuf.append(" numIpConfigFailures ").append(this.numIpConfigFailures).append("\n");
        }
        if (this.numAuthFailures > 0) {
            sbuf.append(" numAuthFailures ").append(this.numAuthFailures).append("\n");
        }
        if (this.autoJoinStatus > 0) {
            sbuf.append(" autoJoinStatus ").append(this.autoJoinStatus).append("\n");
        }
        if (this.disableReason > 0) {
            sbuf.append(" disableReason ").append(this.disableReason).append("\n");
        }
        if (this.numAssociation > 0) {
            sbuf.append(" numAssociation ").append(this.numAssociation).append("\n");
        }
        if (this.numNoInternetAccessReports > 0) {
            sbuf.append(" numNoInternetAccessReports ");
            sbuf.append(this.numNoInternetAccessReports).append("\n");
        }
        if (this.updateTime != null) {
            sbuf.append("update ").append(this.updateTime).append("\n");
        }
        if (this.creationTime != null) {
            sbuf.append("creation").append(this.creationTime).append("\n");
        }
        if (this.didSelfAdd) {
            sbuf.append(" didSelfAdd");
        }
        if (this.selfAdded) {
            sbuf.append(" selfAdded");
        }
        if (this.validatedInternetAccess) {
            sbuf.append(" validatedInternetAccess");
        }
        if (this.ephemeral) {
            sbuf.append(" ephemeral");
        }
        if (this.didSelfAdd || this.selfAdded || this.validatedInternetAccess || this.ephemeral) {
            sbuf.append("\n");
        }
        sbuf.append(" KeyMgmt:");
        for (int k = 0; k < this.allowedKeyManagement.size(); k++) {
            if (this.allowedKeyManagement.get(k)) {
                sbuf.append(" ");
                if (k < KeyMgmt.strings.length) {
                    sbuf.append(KeyMgmt.strings[k]);
                } else {
                    sbuf.append("??");
                }
            }
        }
        sbuf.append(" Protocols:");
        for (int p = 0; p < this.allowedProtocols.size(); p++) {
            if (this.allowedProtocols.get(p)) {
                sbuf.append(" ");
                if (p < Protocol.strings.length) {
                    sbuf.append(Protocol.strings[p]);
                } else {
                    sbuf.append("??");
                }
            }
        }
        sbuf.append('\n');
        sbuf.append(" AuthAlgorithms:");
        for (int a = 0; a < this.allowedAuthAlgorithms.size(); a++) {
            if (this.allowedAuthAlgorithms.get(a)) {
                sbuf.append(" ");
                if (a < AuthAlgorithm.strings.length) {
                    sbuf.append(AuthAlgorithm.strings[a]);
                } else {
                    sbuf.append("??");
                }
            }
        }
        sbuf.append('\n');
        sbuf.append(" PairwiseCiphers:");
        for (int pc = 0; pc < this.allowedPairwiseCiphers.size(); pc++) {
            if (this.allowedPairwiseCiphers.get(pc)) {
                sbuf.append(" ");
                if (pc < PairwiseCipher.strings.length) {
                    sbuf.append(PairwiseCipher.strings[pc]);
                } else {
                    sbuf.append("??");
                }
            }
        }
        sbuf.append('\n');
        sbuf.append(" GroupCiphers:");
        for (int gc = 0; gc < this.allowedGroupCiphers.size(); gc++) {
            if (this.allowedGroupCiphers.get(gc)) {
                sbuf.append(" ");
                if (gc < GroupCipher.strings.length) {
                    sbuf.append(GroupCipher.strings[gc]);
                } else {
                    sbuf.append("??");
                }
            }
        }
        sbuf.append('\n').append(" PSK: ");
        if (this.preSharedKey != null) {
            sbuf.append('*');
        }
        sbuf.append("\nEnterprise config:\n");
        sbuf.append(this.enterpriseConfig);
        sbuf.append("IP config:\n");
        sbuf.append(this.mIpConfiguration.toString());
        if (this.autoJoinBSSID != null) {
            sbuf.append(" autoJoinBSSID=" + this.autoJoinBSSID);
        }
        long now_ms = System.currentTimeMillis();
        if (this.blackListTimestamp != 0) {
            sbuf.append('\n');
            diff = now_ms - this.blackListTimestamp;
            if (diff <= 0) {
                sbuf.append(" blackListed since <incorrect>");
            } else {
                sbuf.append(" blackListed: ").append(Long.toString(diff / 1000)).append("sec ");
            }
        }
        if (this.creatorUid != 0) {
            sbuf.append(" cuid=" + Integer.toString(this.creatorUid));
        }
        if (this.creatorName != null) {
            sbuf.append(" cname=" + this.creatorName);
        }
        if (this.lastUpdateUid != 0) {
            sbuf.append(" luid=" + this.lastUpdateUid);
        }
        if (this.lastUpdateName != null) {
            sbuf.append(" lname=" + this.lastUpdateName);
        }
        sbuf.append(" lcuid=" + this.lastConnectUid);
        sbuf.append(" userApproved=" + userApprovedAsString(this.userApproved));
        sbuf.append(" noInternetAccessExpected=" + this.noInternetAccessExpected);
        sbuf.append(" ");
        if (this.lastConnected != 0) {
            sbuf.append('\n');
            diff = now_ms - this.lastConnected;
            if (diff <= 0) {
                sbuf.append("lastConnected since <incorrect>");
            } else {
                sbuf.append("lastConnected: ").append(Long.toString(diff / 1000)).append("sec ");
            }
        }
        if (this.lastConnectionFailure != 0) {
            sbuf.append('\n');
            diff = now_ms - this.lastConnectionFailure;
            if (diff <= 0) {
                sbuf.append("lastConnectionFailure since <incorrect> ");
            } else {
                sbuf.append("lastConnectionFailure: ").append(Long.toString(diff / 1000));
                sbuf.append("sec ");
            }
        }
        if (this.lastRoamingFailure != 0) {
            sbuf.append('\n');
            diff = now_ms - this.lastRoamingFailure;
            if (diff <= 0) {
                sbuf.append("lastRoamingFailure since <incorrect> ");
            } else {
                sbuf.append("lastRoamingFailure: ").append(Long.toString(diff / 1000));
                sbuf.append("sec ");
            }
        }
        sbuf.append("roamingFailureBlackListTimeMilli: ").append(Long.toString(this.roamingFailureBlackListTimeMilli));
        sbuf.append('\n');
        if (this.linkedConfigurations != null) {
            for (String key : this.linkedConfigurations.keySet()) {
                sbuf.append(" linked: ").append(key);
                sbuf.append('\n');
            }
        }
        if (this.connectChoices != null) {
            for (String key2 : this.connectChoices.keySet()) {
                Integer choice = (Integer) this.connectChoices.get(key2);
                if (choice != null) {
                    sbuf.append(" choice: ").append(key2);
                    sbuf.append(" = ").append(choice);
                    sbuf.append('\n');
                }
            }
        }
        sbuf.append("triggeredLow: ").append(this.numUserTriggeredWifiDisableLowRSSI);
        sbuf.append(" triggeredBad: ").append(this.numUserTriggeredWifiDisableBadRSSI);
        sbuf.append(" triggeredNotHigh: ").append(this.numUserTriggeredWifiDisableNotHighRSSI);
        sbuf.append('\n');
        sbuf.append("ticksLow: ").append(this.numTicksAtLowRSSI);
        sbuf.append(" ticksBad: ").append(this.numTicksAtBadRSSI);
        sbuf.append(" ticksNotHigh: ").append(this.numTicksAtNotHighRSSI);
        sbuf.append('\n');
        sbuf.append("triggeredJoin: ").append(this.numUserTriggeredJoinAttempts);
        sbuf.append('\n');
        sbuf.append("autoJoinBailedDueToLowRssi: ").append(this.autoJoinBailedDueToLowRssi);
        sbuf.append('\n');
        sbuf.append("autoJoinUseAggressiveJoinAttemptThreshold: ");
        sbuf.append(this.autoJoinUseAggressiveJoinAttemptThreshold);
        sbuf.append('\n');
        sbuf.append(" SIM NUMBER: ").append(this.sim_num);
        sbuf.append('\n');
        sbuf.append("samsungSpecificFlags:");
        for (int sf = 0; sf < this.samsungSpecificFlags.size(); sf++) {
            if (this.samsungSpecificFlags.get(sf)) {
                sbuf.append(" ");
                if (sf < SamsungFlag.strings.length) {
                    sbuf.append(SamsungFlag.strings[sf]);
                } else {
                    sbuf.append("??");
                }
            }
        }
        if (CscFeature.getInstance().getEnableStatus("CscFeature_Wifi_SupportWapi")) {
            if (this.wapiAsCert != null) {
                sbuf.append('\n');
                sbuf.append(" WapiAsCert: ").append(this.wapiAsCert);
            }
            if (this.wapiUserCert != null) {
                sbuf.append('\n');
                sbuf.append(" WapiUserCert: ").append(this.wapiUserCert);
            }
            if (this.wapiCertIndex != -1) {
                sbuf.append('\n');
                sbuf.append(" WapiCertIndex: ").append(this.wapiCertIndex);
            }
            if (this.wapiPskType != -1) {
                sbuf.append('\n');
                sbuf.append(" WapiPskType: ").append(this.wapiPskType);
            }
        }
        sbuf.append('\n');
        sbuf.append("recoverableRSSI: " + this.recoverableRSSI);
        sbuf.append('\n');
        sbuf.append("inRecoverArea: " + this.inRecoverArea);
        sbuf.append('\n');
        sbuf.append("disabledTime: " + this.disabledTime);
        sbuf.append('\n');
        sbuf.append("notInRangeTime: " + this.notInRangeTime);
        if (this.poorBSSID != null) {
            sbuf.append('\n');
            if (this.poorBSSID.length() < 5) {
                sbuf.append("poorBSSID: " + this.poorBSSID);
            }
        }
        sbuf.append('\n');
        sbuf.append(" HESSID: ").append(this.HESSID).append(" isHS20AP: ").append(this.isHS20AP).append(" HS20CredId: ").append(this.HS20CredId).append(" HS20OperatorName: ").append(this.HS20OperatorName).append(" HS20VenueName: ").append(this.HS20VenueName).append(" HS20OpURL: ").append(this.HS20OpURL);
        sbuf.append('\n');
        sbuf.append("mode:").append(this.mode.toString());
        sbuf.append('\n');
        sbuf.append("frequency:").append(this.frequency);
        sbuf.append('\n');
        sbuf.append("isCaptivePortal: " + this.isCaptivePortal);
        sbuf.append('\n');
        sbuf.append("isAuthenticated: " + this.isAuthenticated);
        sbuf.append('\n');
        sbuf.append("loginUrl: " + this.loginUrl);
        sbuf.append('\n');
        sbuf.append("isUsableInternet: " + this.isUsableInternet);
        sbuf.append('\n');
        sbuf.append("skipInternetCheck: " + this.skipInternetCheck);
        sbuf.append('\n');
        sbuf.append("isSharedAp: " + this.isSharedAp);
        sbuf.append('\n');
        sbuf.append("expiration: " + this.expiration);
        sbuf.append('\n');
        sbuf.append("isVerifiedPassword: " + this.isVerifiedPassword);
        sbuf.append('\n');
        sbuf.append("isVendorAp : " + this.isVendorSpecificSsid);
        sbuf.append('\n');
        sbuf.append("autojoin : " + this.autojoin);
        sbuf.append('\n');
        return sbuf.toString();
    }

    public String getPrintableSsid() {
        if (this.SSID == null) {
            return ProxyInfo.LOCAL_EXCL_LIST;
        }
        int length = this.SSID.length();
        if (length > 2 && this.SSID.charAt(0) == '\"' && this.SSID.charAt(length - 1) == '\"') {
            return this.SSID.substring(1, length - 1);
        }
        if (length > 3 && this.SSID.charAt(0) == 'P' && this.SSID.charAt(1) == '\"' && this.SSID.charAt(length - 1) == '\"') {
            return WifiSsid.createFromAsciiEncoded(this.SSID.substring(2, length - 1)).toString();
        }
        return this.SSID;
    }

    public static String userApprovedAsString(int userApproved) {
        switch (userApproved) {
            case 0:
                return "USER_UNSPECIFIED";
            case 1:
                return "USER_APPROVED";
            case 2:
                return "USER_BANNED";
            default:
                return "INVALID";
        }
    }

    public String getKeyIdForCredentials(WifiConfiguration current) {
        String keyMgmt = null;
        try {
            if (TextUtils.isEmpty(this.SSID)) {
                this.SSID = current.SSID;
            }
            if (this.allowedKeyManagement.cardinality() == 0) {
                this.allowedKeyManagement = current.allowedKeyManagement;
            }
            if (this.allowedKeyManagement.get(2)) {
                keyMgmt = KeyMgmt.strings[2];
            }
            if (this.allowedKeyManagement.get(3)) {
                keyMgmt = keyMgmt + KeyMgmt.strings[3];
            }
            if (this.allowedKeyManagement.get(7)) {
                keyMgmt = keyMgmt + KeyMgmt.strings[7];
            }
            if (this.allowedKeyManagement.get(9)) {
                keyMgmt = keyMgmt + KeyMgmt.strings[9];
            }
            if (TextUtils.isEmpty(keyMgmt)) {
                throw new IllegalStateException("Not an EAP network");
            }
            return trimStringForKeyId(this.SSID) + "_" + keyMgmt + "_" + trimStringForKeyId(this.enterpriseConfig.getKeyId(current != null ? current.enterpriseConfig : null));
        } catch (NullPointerException e) {
            throw new IllegalStateException("Invalid config details");
        }
    }

    private String trimStringForKeyId(String string) {
        return string.replace("\"", ProxyInfo.LOCAL_EXCL_LIST).replace(" ", ProxyInfo.LOCAL_EXCL_LIST);
    }

    private static BitSet readBitSet(Parcel src) {
        int cardinality = src.readInt();
        BitSet set = new BitSet();
        for (int i = 0; i < cardinality; i++) {
            set.set(src.readInt());
        }
        return set;
    }

    private static void writeBitSet(Parcel dest, BitSet set) {
        int nextSetBit = -1;
        dest.writeInt(set.cardinality());
        while (true) {
            nextSetBit = set.nextSetBit(nextSetBit + 1);
            if (nextSetBit != -1) {
                dest.writeInt(nextSetBit);
            } else {
                return;
            }
        }
    }

    public int getAuthType() {
        if (this.allowedKeyManagement.cardinality() > 1) {
            throw new IllegalStateException("More than one auth type set");
        } else if (this.allowedKeyManagement.get(1)) {
            return 1;
        } else {
            if (this.allowedKeyManagement.get(4)) {
                return 4;
            }
            if (this.allowedKeyManagement.get(2)) {
                return 2;
            }
            if (this.allowedKeyManagement.get(3)) {
                return 3;
            }
            if (this.allowedKeyManagement.get(7)) {
                return 7;
            }
            if (this.allowedKeyManagement.get(8)) {
                return 8;
            }
            if (this.allowedKeyManagement.get(9)) {
                return 9;
            }
            if (this.allowedKeyManagement.get(5)) {
                return 5;
            }
            return 0;
        }
    }

    public String configKey(boolean allowCached) {
        if (allowCached && this.mCachedConfigKey != null) {
            return this.mCachedConfigKey;
        }
        if (this.providerFriendlyName != null) {
            return this.FQDN + KeyMgmt.strings[2];
        }
        String key;
        if (this.allowedKeyManagement.get(1)) {
            key = this.SSID + KeyMgmt.strings[1];
        } else if (this.allowedKeyManagement.get(7)) {
            key = this.SSID + KeyMgmt.strings[7];
        } else if (this.allowedKeyManagement.get(8)) {
            key = this.SSID + KeyMgmt.strings[8];
        } else if (this.allowedKeyManagement.get(9)) {
            key = this.SSID + KeyMgmt.strings[9];
        } else if (this.allowedKeyManagement.get(2) || this.allowedKeyManagement.get(3)) {
            key = this.SSID + KeyMgmt.strings[2];
        } else if (this.wepKeys[0] != null) {
            key = this.SSID + "WEP";
        } else if (this.allowedKeyManagement.get(10)) {
            key = this.SSID + KeyMgmt.strings[10];
        } else {
            key = this.SSID + KeyMgmt.strings[0];
        }
        this.mCachedConfigKey = key;
        return key;
    }

    public String configKey() {
        return configKey(false);
    }

    public static String configKey(ScanResult result) {
        String key = "\"" + result.SSID + "\"";
        if (result.capabilities.contains("WEP")) {
            key = key + "-WEP";
        }
        if (result.capabilities.contains("PSK")) {
            key = key + "-" + KeyMgmt.strings[1];
        }
        if (result.capabilities.contains("EAP")) {
            return key + "-" + KeyMgmt.strings[2];
        }
        return key;
    }

    public IpConfiguration getIpConfiguration() {
        return this.mIpConfiguration;
    }

    public void setIpConfiguration(IpConfiguration ipConfiguration) {
        this.mIpConfiguration = ipConfiguration;
    }

    public StaticIpConfiguration getStaticIpConfiguration() {
        return this.mIpConfiguration.getStaticIpConfiguration();
    }

    public void setStaticIpConfiguration(StaticIpConfiguration staticIpConfiguration) {
        this.mIpConfiguration.setStaticIpConfiguration(staticIpConfiguration);
    }

    public IpAssignment getIpAssignment() {
        return this.mIpConfiguration.ipAssignment;
    }

    public void setIpAssignment(IpAssignment ipAssignment) {
        this.mIpConfiguration.ipAssignment = ipAssignment;
    }

    public ProxySettings getProxySettings() {
        return this.mIpConfiguration.proxySettings;
    }

    public void setProxySettings(ProxySettings proxySettings) {
        this.mIpConfiguration.proxySettings = proxySettings;
    }

    public ProxyInfo getHttpProxy() {
        return this.mIpConfiguration.httpProxy;
    }

    public void setHttpProxy(ProxyInfo httpProxy) {
        this.mIpConfiguration.httpProxy = httpProxy;
    }

    public void setProxy(ProxySettings settings, ProxyInfo proxy) {
        this.mIpConfiguration.proxySettings = settings;
        this.mIpConfiguration.httpProxy = proxy;
    }

    public int describeContents() {
        return 0;
    }

    public WifiConfiguration(WifiConfiguration source) {
        this.apBand = 0;
        this.apChannel = 0;
        this.userApproved = 0;
        this.roamingFailureBlackListTimeMilli = 1000;
        if (source != null) {
            this.networkId = source.networkId;
            this.status = source.status;
            this.disableReason = source.disableReason;
            this.disableReason = source.disableReason;
            this.SSID = source.SSID;
            this.BSSID = source.BSSID;
            this.FQDN = source.FQDN;
            this.roamingConsortiumIds = (long[]) source.roamingConsortiumIds.clone();
            this.providerFriendlyName = source.providerFriendlyName;
            this.preSharedKey = source.preSharedKey;
            this.apBand = source.apBand;
            this.apChannel = source.apChannel;
            this.wepKeys = new String[4];
            for (int i = 0; i < this.wepKeys.length; i++) {
                this.wepKeys[i] = source.wepKeys[i];
            }
            this.wepTxKeyIndex = source.wepTxKeyIndex;
            this.priority = source.priority;
            this.hiddenSSID = source.hiddenSSID;
            this.allowedKeyManagement = (BitSet) source.allowedKeyManagement.clone();
            this.allowedProtocols = (BitSet) source.allowedProtocols.clone();
            this.allowedAuthAlgorithms = (BitSet) source.allowedAuthAlgorithms.clone();
            this.allowedPairwiseCiphers = (BitSet) source.allowedPairwiseCiphers.clone();
            this.allowedGroupCiphers = (BitSet) source.allowedGroupCiphers.clone();
            this.enterpriseConfig = new WifiEnterpriseConfig(source.enterpriseConfig);
            this.defaultGwMacAddress = source.defaultGwMacAddress;
            this.mIpConfiguration = new IpConfiguration(source.mIpConfiguration);
            if (source.connectChoices != null && source.connectChoices.size() > 0) {
                this.connectChoices = new HashMap();
                this.connectChoices.putAll(source.connectChoices);
            }
            if (source.linkedConfigurations != null && source.linkedConfigurations.size() > 0) {
                this.linkedConfigurations = new HashMap();
                this.linkedConfigurations.putAll(source.linkedConfigurations);
            }
            this.mCachedConfigKey = null;
            this.autoJoinStatus = source.autoJoinStatus;
            this.selfAdded = source.selfAdded;
            this.validatedInternetAccess = source.validatedInternetAccess;
            this.ephemeral = source.ephemeral;
            if (source.visibility != null) {
                this.visibility = new Visibility(source.visibility);
            }
            this.lastFailure = source.lastFailure;
            this.didSelfAdd = source.didSelfAdd;
            this.lastConnectUid = source.lastConnectUid;
            this.lastUpdateUid = source.lastUpdateUid;
            this.creatorUid = source.creatorUid;
            this.creatorName = source.creatorName;
            this.lastUpdateName = source.lastUpdateName;
            this.peerWifiConfiguration = source.peerWifiConfiguration;
            this.blackListTimestamp = source.blackListTimestamp;
            this.lastConnected = source.lastConnected;
            this.lastDisconnected = source.lastDisconnected;
            this.lastConnectionFailure = source.lastConnectionFailure;
            this.lastRoamingFailure = source.lastRoamingFailure;
            this.lastRoamingFailureReason = source.lastRoamingFailureReason;
            this.roamingFailureBlackListTimeMilli = source.roamingFailureBlackListTimeMilli;
            this.numConnectionFailures = source.numConnectionFailures;
            this.numIpConfigFailures = source.numIpConfigFailures;
            this.numAuthFailures = source.numAuthFailures;
            this.numScorerOverride = source.numScorerOverride;
            this.numScorerOverrideAndSwitchedNetwork = source.numScorerOverrideAndSwitchedNetwork;
            this.numAssociation = source.numAssociation;
            this.numUserTriggeredWifiDisableLowRSSI = source.numUserTriggeredWifiDisableLowRSSI;
            this.numUserTriggeredWifiDisableBadRSSI = source.numUserTriggeredWifiDisableBadRSSI;
            this.numUserTriggeredWifiDisableNotHighRSSI = source.numUserTriggeredWifiDisableNotHighRSSI;
            this.numTicksAtLowRSSI = source.numTicksAtLowRSSI;
            this.numTicksAtBadRSSI = source.numTicksAtBadRSSI;
            this.numTicksAtNotHighRSSI = source.numTicksAtNotHighRSSI;
            this.numUserTriggeredJoinAttempts = source.numUserTriggeredJoinAttempts;
            this.autoJoinBSSID = source.autoJoinBSSID;
            this.autoJoinUseAggressiveJoinAttemptThreshold = source.autoJoinUseAggressiveJoinAttemptThreshold;
            this.autoJoinBailedDueToLowRssi = source.autoJoinBailedDueToLowRssi;
            this.dirty = source.dirty;
            this.userApproved = source.userApproved;
            this.numNoInternetAccessReports = source.numNoInternetAccessReports;
            this.noInternetAccessExpected = source.noInternetAccessExpected;
            this.creationTime = source.creationTime;
            this.updateTime = source.updateTime;
            this.samsungSpecificFlags = (BitSet) source.samsungSpecificFlags.clone();
            this.recoverableRSSI = source.recoverableRSSI;
            this.inRecoverArea = source.inRecoverArea;
            this.disabledTime = source.disabledTime;
            this.notInRangeTime = source.notInRangeTime;
            this.poorBSSID = source.poorBSSID;
            this.autojoin = source.autojoin;
            this.mode = source.mode;
            this.frequency = source.frequency;
            this.channel = source.channel;
            this.macaddrAcl = source.macaddrAcl;
            this.maxclient = source.maxclient;
            this.vendorIE = source.vendorIE;
            this.apIsolate = source.apIsolate;
            this.wpsStatus = source.wpsStatus;
            this.txPowerMode = source.txPowerMode;
            this.HESSID = source.HESSID;
            this.isHS20AP = source.isHS20AP;
            this.isHS20Home = source.isHS20Home;
            this.HS20CredId = source.HS20CredId;
            this.HS20OperatorName = source.HS20OperatorName;
            this.HS20VenueName = source.HS20VenueName;
            this.HS20OpURL = source.HS20OpURL;
            this.recoverableRSSI = source.recoverableRSSI;
            this.inRecoverArea = source.inRecoverArea;
            this.disabledTime = source.disabledTime;
            this.notInRangeTime = source.notInRangeTime;
            this.poorBSSID = source.poorBSSID;
            this.isCaptivePortal = source.isCaptivePortal;
            this.isAuthenticated = source.isAuthenticated;
            this.loginUrl = source.loginUrl;
            this.isUsableInternet = source.isUsableInternet;
            this.skipInternetCheck = source.skipInternetCheck;
            this.isSharedAp = source.isSharedAp;
            this.expiration = source.expiration;
            this.isVerifiedPassword = source.isVerifiedPassword;
            this.sim_num = 0;
            this.isVendorSpecificSsid = source.isVendorSpecificSsid;
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeInt(this.networkId);
        dest.writeInt(this.status);
        dest.writeInt(this.disableReason);
        dest.writeString(this.SSID);
        dest.writeString(this.BSSID);
        dest.writeInt(this.apBand);
        dest.writeInt(this.apChannel);
        dest.writeString(this.autoJoinBSSID);
        dest.writeString(this.FQDN);
        dest.writeString(this.providerFriendlyName);
        dest.writeInt(this.roamingConsortiumIds.length);
        for (long roamingConsortiumId : this.roamingConsortiumIds) {
            dest.writeLong(roamingConsortiumId);
        }
        dest.writeString(this.preSharedKey);
        for (String wepKey : this.wepKeys) {
            dest.writeString(wepKey);
        }
        dest.writeInt(this.wepTxKeyIndex);
        dest.writeInt(this.priority);
        dest.writeInt(this.hiddenSSID ? 1 : 0);
        if (this.requirePMF) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeString(this.updateIdentifier);
        writeBitSet(dest, this.allowedKeyManagement);
        writeBitSet(dest, this.allowedProtocols);
        writeBitSet(dest, this.allowedAuthAlgorithms);
        writeBitSet(dest, this.allowedPairwiseCiphers);
        writeBitSet(dest, this.allowedGroupCiphers);
        dest.writeParcelable(this.enterpriseConfig, flags);
        dest.writeParcelable(this.mIpConfiguration, flags);
        dest.writeString(this.dhcpServer);
        dest.writeString(this.defaultGwMacAddress);
        dest.writeInt(this.autoJoinStatus);
        dest.writeInt(this.selfAdded ? 1 : 0);
        if (this.didSelfAdd) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.validatedInternetAccess) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.ephemeral) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeInt(this.creatorUid);
        dest.writeInt(this.lastConnectUid);
        dest.writeInt(this.lastUpdateUid);
        dest.writeString(this.creatorName);
        dest.writeString(this.lastUpdateName);
        dest.writeLong(this.blackListTimestamp);
        dest.writeLong(this.lastConnectionFailure);
        dest.writeLong(this.lastRoamingFailure);
        dest.writeInt(this.lastRoamingFailureReason);
        dest.writeLong(this.roamingFailureBlackListTimeMilli);
        dest.writeInt(this.numConnectionFailures);
        dest.writeInt(this.numIpConfigFailures);
        dest.writeInt(this.numAuthFailures);
        dest.writeInt(this.numScorerOverride);
        dest.writeInt(this.numScorerOverrideAndSwitchedNetwork);
        dest.writeInt(this.numAssociation);
        dest.writeInt(this.numUserTriggeredWifiDisableLowRSSI);
        dest.writeInt(this.numUserTriggeredWifiDisableBadRSSI);
        dest.writeInt(this.numUserTriggeredWifiDisableNotHighRSSI);
        dest.writeInt(this.numTicksAtLowRSSI);
        dest.writeInt(this.numTicksAtBadRSSI);
        dest.writeInt(this.numTicksAtNotHighRSSI);
        dest.writeInt(this.numUserTriggeredJoinAttempts);
        dest.writeInt(this.autoJoinUseAggressiveJoinAttemptThreshold);
        dest.writeInt(this.autoJoinBailedDueToLowRssi ? 1 : 0);
        dest.writeInt(this.userApproved);
        dest.writeInt(this.numNoInternetAccessReports);
        if (this.noInternetAccessExpected) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        writeBitSet(dest, this.samsungSpecificFlags);
        dest.writeString(this.mode.name());
        dest.writeInt(this.frequency);
        dest.writeInt(this.channel);
        dest.writeInt(this.macaddrAcl);
        dest.writeInt(this.maxclient);
        dest.writeInt(this.vendorIE);
        dest.writeInt(this.apIsolate);
        dest.writeInt(this.wpsStatus);
        dest.writeInt(this.txPowerMode);
        if (this.allowedKeyManagement.get(5)) {
            dest.writeInt(this.wapiPskType);
        } else if (this.allowedKeyManagement.get(6)) {
            dest.writeString(this.wapiAsCert);
            dest.writeString(this.wapiUserCert);
            dest.writeInt(this.wapiCertIndex);
        }
        dest.writeString(this.HESSID);
        dest.writeInt(this.isHS20AP);
        dest.writeInt(this.isHS20Home);
        dest.writeInt(this.HS20CredId);
        dest.writeString(this.HS20OperatorName);
        dest.writeString(this.HS20VenueName);
        dest.writeString(this.HS20OpURL);
        dest.writeInt(this.recoverableRSSI);
        dest.writeInt(this.inRecoverArea ? 1 : 0);
        dest.writeLong(this.disabledTime);
        dest.writeLong(this.notInRangeTime);
        dest.writeString(this.poorBSSID);
        dest.writeInt(this.autojoin);
        if (this.isCaptivePortal) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isAuthenticated) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeString(this.loginUrl);
        if (this.isUsableInternet) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.skipInternetCheck) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isSharedAp) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeString(this.expiration);
        if (this.isVerifiedPassword) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeInt(this.sim_num);
        if (!this.isVendorSpecificSsid) {
            i2 = 0;
        }
        dest.writeInt(i2);
    }
}
