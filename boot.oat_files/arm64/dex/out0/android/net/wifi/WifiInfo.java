package android.net.wifi;

import android.net.NetworkInfo.DetailedState;
import android.net.NetworkUtils;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.EnumMap;
import java.util.Locale;

public class WifiInfo implements Parcelable {
    public static final Creator<WifiInfo> CREATOR = new Creator<WifiInfo>() {
        public WifiInfo createFromParcel(Parcel in) {
            boolean z;
            boolean z2 = true;
            WifiInfo info = new WifiInfo();
            info.setNetworkId(in.readInt());
            info.setRssi(in.readInt());
            info.setLinkSpeed(in.readInt());
            info.setFrequency(in.readInt());
            if (in.readByte() == (byte) 1) {
                try {
                    info.setInetAddress(InetAddress.getByAddress(in.createByteArray()));
                } catch (UnknownHostException e) {
                }
            }
            if (in.readInt() == 1) {
                info.mWifiSsid = (WifiSsid) WifiSsid.CREATOR.createFromParcel(in);
            }
            info.mBSSID = in.readString();
            info.mMacAddress = in.readString();
            info.mMeteredHint = in.readInt() != 0;
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            info.mEphemeral = z;
            info.score = in.readInt();
            info.txSuccessRate = in.readDouble();
            info.txRetriesRate = in.readDouble();
            info.txBadRate = in.readDouble();
            info.rxSuccessRate = in.readDouble();
            info.badRssiCount = in.readInt();
            info.lowRssiCount = in.readInt();
            info.mSupplicantState = (SupplicantState) SupplicantState.CREATOR.createFromParcel(in);
            if (in.readInt() == 1) {
                z = true;
            } else {
                z = false;
            }
            info.setSkipInternetCheck(z);
            if (in.readInt() == 1) {
                z = true;
            } else {
                z = false;
            }
            info.setCaptivePortal(z);
            if (in.readInt() == 1) {
                z = true;
            } else {
                z = false;
            }
            info.setAuthenticated(z);
            info.mLoginUrl = in.readString();
            if (in.readInt() == 1) {
                z = true;
            } else {
                z = false;
            }
            info.setDefaultAp(z);
            if (in.readInt() == 1) {
                z = true;
            } else {
                z = false;
            }
            info.setSharedAp(z);
            info.mExpiration = in.readString();
            if (in.readInt() == 1) {
                z = true;
            } else {
                z = false;
            }
            info.setVerifiedPassword(z);
            if (in.readInt() == 1) {
                z = true;
            } else {
                z = false;
            }
            info.setCheckVsieForSns(z);
            if (in.readInt() != 0) {
                z = true;
            } else {
                z = false;
            }
            info.isGigaAp = z;
            if (in.readInt() == 0) {
                z2 = false;
            }
            info.isVendorAp = z2;
            return info;
        }

        public WifiInfo[] newArray(int size) {
            return new WifiInfo[size];
        }
    };
    public static final String DEFAULT_MAC_ADDRESS = "02:00:00:00:00:00";
    public static final String FREQUENCY_UNITS = "MHz";
    public static final int INVALID_RSSI = -127;
    public static final String LINK_SPEED_UNITS = "Mbps";
    public static final int MAX_RSSI = 200;
    public static final int MIN_RSSI = -126;
    private static final String TAG = "WifiInfo";
    private static final String WIFI_MAC_FILE = "/efs/wifi/.mac.info";
    private static final EnumMap<SupplicantState, DetailedState> stateMap = new EnumMap(SupplicantState.class);
    public int badRssiCount;
    public boolean isGigaAp;
    public boolean isVendorAp;
    public int linkStuckCount;
    public int lowRssiCount;
    private boolean mAuthenticated;
    private String mBSSID;
    private boolean mCaptivePortal;
    private boolean mCheckVsieForSns;
    private boolean mDefaultAp;
    private boolean mEphemeral;
    private String mExpiration;
    private int mFrequency;
    private InetAddress mIpAddress;
    private int mLinkSpeed;
    private String mLoginUrl;
    private String mMacAddress;
    private boolean mMeteredHint;
    private int mNetworkId;
    private int mRssi;
    private boolean mSharedAp;
    private boolean mSkipInternetCheck;
    private SupplicantState mSupplicantState;
    private boolean mVerifiedPassword;
    private WifiSsid mWifiSsid;
    public long rxSuccess;
    public double rxSuccessRate;
    public int score;
    public long txBad;
    public double txBadRate;
    public long txRetries;
    public double txRetriesRate;
    public long txSuccess;
    public double txSuccessRate;

    static {
        stateMap.put(SupplicantState.DISCONNECTED, DetailedState.DISCONNECTED);
        stateMap.put(SupplicantState.INTERFACE_DISABLED, DetailedState.DISCONNECTED);
        stateMap.put(SupplicantState.INACTIVE, DetailedState.IDLE);
        stateMap.put(SupplicantState.SCANNING, DetailedState.SCANNING);
        stateMap.put(SupplicantState.AUTHENTICATING, DetailedState.CONNECTING);
        stateMap.put(SupplicantState.ASSOCIATING, DetailedState.CONNECTING);
        stateMap.put(SupplicantState.ASSOCIATED, DetailedState.CONNECTING);
        stateMap.put(SupplicantState.FOUR_WAY_HANDSHAKE, DetailedState.AUTHENTICATING);
        stateMap.put(SupplicantState.GROUP_HANDSHAKE, DetailedState.AUTHENTICATING);
        stateMap.put(SupplicantState.COMPLETED, DetailedState.OBTAINING_IPADDR);
        stateMap.put(SupplicantState.DORMANT, DetailedState.DISCONNECTED);
        stateMap.put(SupplicantState.UNINITIALIZED, DetailedState.IDLE);
        stateMap.put(SupplicantState.INVALID, DetailedState.FAILED);
    }

    public void updatePacketRates(WifiLinkLayerStats stats) {
        if (stats != null) {
            long txgood = ((stats.txmpdu_be + stats.txmpdu_bk) + stats.txmpdu_vi) + stats.txmpdu_vo;
            long txretries = ((stats.retries_be + stats.retries_bk) + stats.retries_vi) + stats.retries_vo;
            long rxgood = ((stats.rxmpdu_be + stats.rxmpdu_bk) + stats.rxmpdu_vi) + stats.rxmpdu_vo;
            long txbad = ((stats.lostmpdu_be + stats.lostmpdu_bk) + stats.lostmpdu_vi) + stats.lostmpdu_vo;
            if (this.txBad > txbad || this.txSuccess > txgood || this.rxSuccess > rxgood || this.txRetries > txretries) {
                this.txBadRate = 0.0d;
                this.txSuccessRate = 0.0d;
                this.rxSuccessRate = 0.0d;
                this.txRetriesRate = 0.0d;
            } else {
                this.txBadRate = (this.txBadRate * 0.5d) + (((double) (txbad - this.txBad)) * 0.5d);
                this.txSuccessRate = (this.txSuccessRate * 0.5d) + (((double) (txgood - this.txSuccess)) * 0.5d);
                this.rxSuccessRate = (this.rxSuccessRate * 0.5d) + (((double) (rxgood - this.rxSuccess)) * 0.5d);
                this.txRetriesRate = (this.txRetriesRate * 0.5d) + (((double) (txretries - this.txRetries)) * 0.5d);
            }
            this.txBad = txbad;
            this.txSuccess = txgood;
            this.rxSuccess = rxgood;
            this.txRetries = txretries;
            return;
        }
        this.txBad = 0;
        this.txSuccess = 0;
        this.rxSuccess = 0;
        this.txRetries = 0;
        this.txBadRate = 0.0d;
        this.txSuccessRate = 0.0d;
        this.rxSuccessRate = 0.0d;
        this.txRetriesRate = 0.0d;
    }

    public void updatePacketRates(long txPackets, long rxPackets) {
        this.txBad = 0;
        this.txRetries = 0;
        this.txBadRate = 0.0d;
        this.txRetriesRate = 0.0d;
        if (this.txSuccess > txPackets || this.rxSuccess > rxPackets) {
            this.txBadRate = 0.0d;
            this.txRetriesRate = 0.0d;
        } else {
            this.txSuccessRate = (this.txSuccessRate * 0.5d) + (((double) (txPackets - this.txSuccess)) * 0.5d);
            this.rxSuccessRate = (this.rxSuccessRate * 0.5d) + (((double) (rxPackets - this.rxSuccess)) * 0.5d);
        }
        this.txSuccess = txPackets;
        this.rxSuccess = rxPackets;
    }

    public WifiInfo() {
        this.mMacAddress = "02:00:00:00:00:00";
        this.mWifiSsid = null;
        this.mBSSID = null;
        this.mNetworkId = -1;
        this.mSupplicantState = SupplicantState.UNINITIALIZED;
        this.mRssi = INVALID_RSSI;
        this.mLinkSpeed = -1;
        this.mFrequency = -1;
        this.mSkipInternetCheck = false;
        this.mCaptivePortal = false;
        this.mAuthenticated = false;
        this.mLoginUrl = null;
        this.mDefaultAp = false;
        this.mSharedAp = false;
        this.mExpiration = null;
        this.mVerifiedPassword = false;
        this.mCheckVsieForSns = false;
        this.isGigaAp = false;
        this.isVendorAp = false;
    }

    public void reset() {
        setInetAddress(null);
        setBSSID(null);
        setSSID(null);
        setNetworkId(-1);
        setRssi(INVALID_RSSI);
        setLinkSpeed(-1);
        setFrequency(-1);
        setMeteredHint(false);
        setEphemeral(false);
        this.txBad = 0;
        this.txSuccess = 0;
        this.rxSuccess = 0;
        this.txRetries = 0;
        this.txBadRate = 0.0d;
        this.txSuccessRate = 0.0d;
        this.rxSuccessRate = 0.0d;
        this.txRetriesRate = 0.0d;
        this.lowRssiCount = 0;
        this.isGigaAp = false;
        this.isVendorAp = false;
        this.badRssiCount = 0;
        this.linkStuckCount = 0;
        this.score = 0;
    }

    public WifiInfo(WifiInfo source) {
        this.mMacAddress = "02:00:00:00:00:00";
        if (source != null) {
            this.mSupplicantState = source.mSupplicantState;
            this.mBSSID = source.mBSSID;
            this.mWifiSsid = source.mWifiSsid;
            this.mNetworkId = source.mNetworkId;
            this.mRssi = source.mRssi;
            this.mLinkSpeed = source.mLinkSpeed;
            this.mFrequency = source.mFrequency;
            this.mIpAddress = source.mIpAddress;
            this.mMacAddress = source.mMacAddress;
            this.mMeteredHint = source.mMeteredHint;
            this.mEphemeral = source.mEphemeral;
            this.txBad = source.txBad;
            this.txRetries = source.txRetries;
            this.txSuccess = source.txSuccess;
            this.rxSuccess = source.rxSuccess;
            this.txBadRate = source.txBadRate;
            this.txRetriesRate = source.txRetriesRate;
            this.txSuccessRate = source.txSuccessRate;
            this.rxSuccessRate = source.rxSuccessRate;
            this.score = source.score;
            this.badRssiCount = source.badRssiCount;
            this.lowRssiCount = source.lowRssiCount;
            this.linkStuckCount = source.linkStuckCount;
            this.mSkipInternetCheck = source.mSkipInternetCheck;
            this.mCaptivePortal = source.mCaptivePortal;
            this.mAuthenticated = source.mAuthenticated;
            this.mLoginUrl = source.mLoginUrl;
            this.mDefaultAp = source.mDefaultAp;
            this.mSharedAp = source.mSharedAp;
            this.mExpiration = source.mExpiration;
            this.mVerifiedPassword = source.mVerifiedPassword;
            this.mCheckVsieForSns = source.mCheckVsieForSns;
            this.isGigaAp = source.isGigaAp;
            this.isVendorAp = source.isVendorAp;
        }
    }

    public void setSSID(WifiSsid wifiSsid) {
        this.mWifiSsid = wifiSsid;
    }

    public String getSSID() {
        if (this.mWifiSsid == null) {
            return WifiSsid.NONE;
        }
        String unicode = this.mWifiSsid.toString();
        if (TextUtils.isEmpty(unicode)) {
            return this.mWifiSsid.getHexString();
        }
        return "\"" + unicode + "\"";
    }

    public WifiSsid getWifiSsid() {
        return this.mWifiSsid;
    }

    public void setBSSID(String BSSID) {
        this.mBSSID = BSSID;
    }

    public String getBSSID() {
        return this.mBSSID;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public void setRssi(int rssi) {
        if (rssi < INVALID_RSSI) {
            rssi = INVALID_RSSI;
        }
        if (rssi > 200) {
            rssi = 200;
        }
        this.mRssi = rssi;
    }

    public int getLinkSpeed() {
        return this.mLinkSpeed;
    }

    public void setLinkSpeed(int linkSpeed) {
        this.mLinkSpeed = linkSpeed;
    }

    public int getFrequency() {
        return this.mFrequency;
    }

    public void setFrequency(int frequency) {
        this.mFrequency = frequency;
    }

    public boolean is24GHz() {
        return ScanResult.is24GHz(this.mFrequency);
    }

    public boolean is5GHz() {
        return ScanResult.is5GHz(this.mFrequency);
    }

    public void setMacAddress(String macAddress) {
        this.mMacAddress = macAddress;
    }

    public String getMacAddress() {
        if (this.mMacAddress != null) {
            return this.mMacAddress.toUpperCase();
        }
        return this.mMacAddress;
    }

    public static String getMacAddressFromFile() {
        Throwable th;
        DataInputStream in = null;
        byte[] buffer = new byte[32];
        String str = null;
        try {
            DataInputStream in2 = new DataInputStream(new BufferedInputStream(new FileInputStream(WIFI_MAC_FILE)));
            try {
                if (in2.read(buffer) >= 17) {
                    String retValue = new String(buffer, 0, 17);
                    try {
                        str = retValue.toUpperCase();
                    } catch (IOException e) {
                        str = retValue;
                        in = in2;
                        try {
                            Log.e(TAG, "Failed to get MAC address from /efs/wifi/.mac.info");
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e2) {
                                    Log.e(TAG, "Failed to close .mac.info file");
                                }
                            }
                            return str;
                        } catch (Throwable th2) {
                            th = th2;
                            if (in != null) {
                                try {
                                    in.close();
                                } catch (IOException e3) {
                                    Log.e(TAG, "Failed to close .mac.info file");
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        str = retValue;
                        in = in2;
                        if (in != null) {
                            in.close();
                        }
                        throw th;
                    }
                }
                if (in2 != null) {
                    try {
                        in2.close();
                        in = in2;
                    } catch (IOException e4) {
                        Log.e(TAG, "Failed to close .mac.info file");
                        in = in2;
                    }
                }
            } catch (IOException e5) {
                in = in2;
                Log.e(TAG, "Failed to get MAC address from /efs/wifi/.mac.info");
                if (in != null) {
                    in.close();
                }
                return str;
            } catch (Throwable th4) {
                th = th4;
                in = in2;
                if (in != null) {
                    in.close();
                }
                throw th;
            }
        } catch (IOException e6) {
            Log.e(TAG, "Failed to get MAC address from /efs/wifi/.mac.info");
            if (in != null) {
                in.close();
            }
            return str;
        }
        return str;
    }

    public void setMeteredHint(boolean meteredHint) {
        this.mMeteredHint = meteredHint;
    }

    public boolean getMeteredHint() {
        return this.mMeteredHint;
    }

    public void setEphemeral(boolean ephemeral) {
        this.mEphemeral = ephemeral;
    }

    public boolean isEphemeral() {
        return this.mEphemeral;
    }

    public void setNetworkId(int id) {
        this.mNetworkId = id;
    }

    public int getNetworkId() {
        return this.mNetworkId;
    }

    public SupplicantState getSupplicantState() {
        return this.mSupplicantState;
    }

    public void setSupplicantState(SupplicantState state) {
        this.mSupplicantState = state;
    }

    public void setInetAddress(InetAddress address) {
        this.mIpAddress = address;
    }

    public int getIpAddress() {
        if (this.mIpAddress instanceof Inet4Address) {
            return NetworkUtils.inetAddressToInt((Inet4Address) this.mIpAddress);
        }
        return 0;
    }

    public boolean getHiddenSSID() {
        if (this.mWifiSsid == null) {
            return false;
        }
        return this.mWifiSsid.isHidden();
    }

    public static DetailedState getDetailedStateOf(SupplicantState suppState) {
        return (DetailedState) stateMap.get(suppState);
    }

    void setSupplicantState(String stateName) {
        this.mSupplicantState = valueOf(stateName);
    }

    static SupplicantState valueOf(String stateName) {
        if ("4WAY_HANDSHAKE".equalsIgnoreCase(stateName)) {
            return SupplicantState.FOUR_WAY_HANDSHAKE;
        }
        try {
            return SupplicantState.valueOf(stateName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            return SupplicantState.INVALID;
        }
    }

    public boolean getSkipInternetCheck() {
        return this.mSkipInternetCheck;
    }

    public void setSkipInternetCheck(boolean skipInternetCheck) {
        this.mSkipInternetCheck = skipInternetCheck;
    }

    public boolean isCaptivePortal() {
        return this.mCaptivePortal;
    }

    public boolean isAuthenticated() {
        return this.mAuthenticated;
    }

    public String getLoginUrl() {
        return this.mLoginUrl;
    }

    public void setCaptivePortal(boolean captivePortal) {
        this.mCaptivePortal = captivePortal;
    }

    public void setAuthenticated(boolean auth) {
        this.mAuthenticated = auth;
    }

    public void setLoginUrl(String url) {
        this.mLoginUrl = url;
    }

    public boolean getDefaultAp() {
        return this.mDefaultAp;
    }

    public void setDefaultAp(boolean defaultAp) {
        this.mDefaultAp = defaultAp;
    }

    public boolean isSharedAp() {
        return this.mSharedAp;
    }

    public void setSharedAp(boolean sharedAp) {
        this.mSharedAp = sharedAp;
    }

    public String getExpiration() {
        return this.mExpiration;
    }

    public void setExpiration(String expiration) {
        this.mExpiration = expiration;
    }

    public boolean isVerifiedPassword() {
        return this.mVerifiedPassword;
    }

    public void setVerifiedPassword(boolean verifiedPassword) {
        this.mVerifiedPassword = verifiedPassword;
    }

    public boolean getCheckVsieForSns() {
        return this.mCheckVsieForSns;
    }

    public void setCheckVsieForSns(boolean checkVsieForSns) {
        this.mCheckVsieForSns = checkVsieForSns;
    }

    public static String removeDoubleQuotes(String string) {
        if (string == null) {
            return null;
        }
        int length = string.length();
        if (length > 1 && string.charAt(0) == '\"' && string.charAt(length - 1) == '\"') {
            return string.substring(1, length - 1);
        }
        return string;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        String none = "<none>";
        StringBuffer append = sb.append("SSID: ").append(this.mWifiSsid == null ? WifiSsid.NONE : this.mWifiSsid).append(", BSSID: ").append(this.mBSSID == null ? none : this.mBSSID).append(", MAC: ").append(this.mMacAddress == null ? none : this.mMacAddress).append(", Supplicant state: ");
        if (this.mSupplicantState != null) {
            none = this.mSupplicantState;
        }
        append.append(none).append(", RSSI: ").append(this.mRssi).append(", Link speed: ").append(this.mLinkSpeed).append(LINK_SPEED_UNITS).append(", Frequency: ").append(this.mFrequency).append(FREQUENCY_UNITS).append(", Net ID: ").append(this.mNetworkId).append(", Metered hint: ").append(this.mMeteredHint).append(", score: ").append(Integer.toString(this.score));
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeInt(this.mNetworkId);
        dest.writeInt(this.mRssi);
        dest.writeInt(this.mLinkSpeed);
        dest.writeInt(this.mFrequency);
        if (this.mIpAddress != null) {
            dest.writeByte((byte) 1);
            dest.writeByteArray(this.mIpAddress.getAddress());
        } else {
            dest.writeByte((byte) 0);
        }
        if (this.mWifiSsid != null) {
            dest.writeInt(1);
            this.mWifiSsid.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        dest.writeString(this.mBSSID);
        dest.writeString(this.mMacAddress);
        if (this.mMeteredHint) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.mEphemeral) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeInt(this.score);
        dest.writeDouble(this.txSuccessRate);
        dest.writeDouble(this.txRetriesRate);
        dest.writeDouble(this.txBadRate);
        dest.writeDouble(this.rxSuccessRate);
        dest.writeInt(this.badRssiCount);
        dest.writeInt(this.lowRssiCount);
        this.mSupplicantState.writeToParcel(dest, flags);
        if (this.mSkipInternetCheck) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.mCaptivePortal) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.mAuthenticated) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeString(this.mLoginUrl);
        if (this.mDefaultAp) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.mSharedAp) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeString(this.mExpiration);
        if (this.mVerifiedPassword) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.mCheckVsieForSns) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (this.isGigaAp) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (!this.isVendorAp) {
            i2 = 0;
        }
        dest.writeInt(i2);
    }
}
