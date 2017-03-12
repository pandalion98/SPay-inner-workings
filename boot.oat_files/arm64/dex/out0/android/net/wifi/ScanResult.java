package android.net.wifi;

import android.media.MediaPlayer;
import android.net.ProxyInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ScanResult implements Parcelable {
    public static final int AUTHENTICATION_ERROR = 128;
    public static final int AUTO_JOIN_DISABLED = 32;
    public static final int AUTO_ROAM_DISABLED = 16;
    public static final int CHANNEL_WIDTH_160MHZ = 3;
    public static final int CHANNEL_WIDTH_20MHZ = 0;
    public static final int CHANNEL_WIDTH_40MHZ = 1;
    public static final int CHANNEL_WIDTH_80MHZ = 2;
    public static final int CHANNEL_WIDTH_80MHZ_PLUS_MHZ = 4;
    public static final Creator<ScanResult> CREATOR = new Creator<ScanResult>() {
        public ScanResult createFromParcel(Parcel in) {
            WifiSsid wifiSsid = null;
            if (in.readInt() == 1) {
                wifiSsid = (WifiSsid) WifiSsid.CREATOR.createFromParcel(in);
            }
            ScanResult sr = new ScanResult(wifiSsid, in.readString(), in.readString(), in.readString(), in.readString(), in.readInt(), in.readInt(), in.readLong(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), false);
            sr.seen = in.readLong();
            sr.autoJoinStatus = in.readInt();
            sr.untrusted = in.readInt() != 0;
            sr.numConnection = in.readInt();
            sr.numUsage = in.readInt();
            sr.numIpConfigFailures = in.readInt();
            sr.isAutoJoinCandidate = in.readInt();
            sr.venueName = in.readString();
            sr.operatorFriendlyName = in.readString();
            sr.flags = in.readLong();
            sr.is5GHzVsi = in.readInt() != 0;
            sr.ChannelMode = in.readString();
            sr.BSSLoadElement = in.readString();
            sr.SamsungVsie = in.readString();
            int n = in.readInt();
            if (n != 0) {
                sr.informationElements = new InformationElement[n];
                for (int i = 0; i < n; i++) {
                    sr.informationElements[i] = new InformationElement();
                    sr.informationElements[i].id = in.readInt();
                    sr.informationElements[i].bytes = new byte[in.readInt()];
                    in.readByteArray(sr.informationElements[i].bytes);
                }
            }
            return sr;
        }

        public ScanResult[] newArray(int size) {
            return new ScanResult[size];
        }
    };
    public static final int ENABLED = 0;
    public static final long FLAG_80211mc_RESPONDER = 2;
    public static final long FLAG_PASSPOINT_NETWORK = 1;
    public static final int UNSPECIFIED = -1;
    public String BSSID;
    public String BSSLoadElement;
    public String ChannelMode;
    public String HESSID;
    public String SSID;
    public String SamsungVsie;
    public int autoJoinStatus;
    public long blackListTimestamp;
    public byte[] bytes;
    public String capabilities;
    public int centerFreq0;
    public int centerFreq1;
    public int channelWidth;
    public int distanceCm;
    public int distanceSdCm;
    public long flags;
    public int frequency;
    public InformationElement[] informationElements;
    public boolean is5GHzVsi;
    public boolean is80211McRTTResponder;
    public int isAutoJoinCandidate;
    public int level;
    public int numConnection;
    public int numIpConfigFailures;
    public int numUsage;
    public CharSequence operatorFriendlyName;
    public long seen;
    public long timestamp;
    public boolean untrusted;
    public CharSequence venueName;
    public WifiSsid wifiSsid;

    public static class InformationElement {
        public byte[] bytes;
        public int id;

        public InformationElement(InformationElement rhs) {
            this.id = rhs.id;
            this.bytes = (byte[]) rhs.bytes.clone();
        }
    }

    public void averageRssi(int previousRssi, long previousSeen, int maxAge) {
        if (this.seen == 0) {
            this.seen = System.currentTimeMillis();
        }
        long age = this.seen - previousSeen;
        if (previousSeen > 0 && age > 0 && age < ((long) (maxAge / 2))) {
            double alpha = 0.5d - (((double) age) / ((double) maxAge));
            this.level = (int) ((((double) this.level) * (1.0d - alpha)) + (((double) previousRssi) * alpha));
        }
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
        this.autoJoinStatus = status;
    }

    public void setFlag(long flag) {
        this.flags |= flag;
    }

    public void clearFlag(long flag) {
        this.flags &= -1 ^ flag;
    }

    public boolean is80211mcResponder() {
        return (this.flags & 2) != 0;
    }

    public boolean isPasspointNetwork() {
        return (this.flags & 1) != 0;
    }

    public boolean is24GHz() {
        return is24GHz(this.frequency);
    }

    public static boolean is24GHz(int freq) {
        return freq > 2400 && freq < MediaPlayer.KEY_PARAMETER_WFD_TCP_DISABLE;
    }

    public boolean is5GHz() {
        return is5GHz(this.frequency);
    }

    public static boolean is5GHz(int freq) {
        return freq > 4900 && freq < 5900;
    }

    public ScanResult(WifiSsid wifiSsid, String BSSID, String caps, int level, int frequency, long tsf) {
        this.wifiSsid = wifiSsid;
        this.SSID = wifiSsid != null ? wifiSsid.toString() : WifiSsid.NONE;
        this.BSSID = BSSID;
        this.HESSID = null;
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.timestamp = tsf;
        this.distanceCm = -1;
        this.distanceSdCm = -1;
        this.channelWidth = -1;
        this.centerFreq0 = -1;
        this.centerFreq1 = -1;
        this.flags = 0;
    }

    public ScanResult(WifiSsid wifiSsid, String BSSID, String caps, int level, int frequency, long tsf, int distCm, int distSdCm) {
        this.wifiSsid = wifiSsid;
        this.SSID = wifiSsid != null ? wifiSsid.toString() : WifiSsid.NONE;
        this.BSSID = BSSID;
        this.HESSID = null;
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.timestamp = tsf;
        this.distanceCm = distCm;
        this.distanceSdCm = distSdCm;
        this.channelWidth = -1;
        this.centerFreq0 = -1;
        this.centerFreq1 = -1;
        this.flags = 0;
    }

    public ScanResult(String Ssid, String BSSID, String caps, int level, int frequency, long tsf, int distCm, int distSdCm, int channelWidth, int centerFreq0, int centerFreq1, boolean is80211McRTTResponder) {
        this.SSID = Ssid;
        this.BSSID = BSSID;
        this.HESSID = null;
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.timestamp = tsf;
        this.distanceCm = distCm;
        this.distanceSdCm = distSdCm;
        this.channelWidth = channelWidth;
        this.centerFreq0 = centerFreq0;
        this.centerFreq1 = centerFreq1;
        if (is80211McRTTResponder) {
            this.flags = 2;
        } else {
            this.flags = 0;
        }
    }

    public ScanResult(WifiSsid wifiSsid, String Ssid, String BSSID, String HESSID, String caps, int level, int frequency, long tsf, int distCm, int distSdCm, int channelWidth, int centerFreq0, int centerFreq1, boolean is80211McRTTResponder) {
        this(Ssid, BSSID, caps, level, frequency, tsf, distCm, distSdCm, channelWidth, centerFreq0, centerFreq1, is80211McRTTResponder);
        this.wifiSsid = wifiSsid;
        this.HESSID = HESSID;
    }

    public ScanResult(WifiSsid wifiSsid, String Ssid, String BSSID, String caps, int level, int frequency, long tsf, int distCm, int distSdCm, int channelWidth, int centerFreq0, int centerFreq1, boolean is80211McRTTResponder) {
        this(Ssid, BSSID, caps, level, frequency, tsf, distCm, distSdCm, channelWidth, centerFreq0, centerFreq1, is80211McRTTResponder);
        this.wifiSsid = wifiSsid;
    }

    public ScanResult(ScanResult source) {
        if (source != null) {
            this.wifiSsid = source.wifiSsid;
            this.SSID = source.SSID;
            this.BSSID = source.BSSID;
            this.HESSID = source.HESSID;
            this.capabilities = source.capabilities;
            this.level = source.level;
            this.frequency = source.frequency;
            this.channelWidth = source.channelWidth;
            this.centerFreq0 = source.centerFreq0;
            this.centerFreq1 = source.centerFreq1;
            this.timestamp = source.timestamp;
            this.distanceCm = source.distanceCm;
            this.distanceSdCm = source.distanceSdCm;
            this.seen = source.seen;
            this.autoJoinStatus = source.autoJoinStatus;
            this.untrusted = source.untrusted;
            this.numConnection = source.numConnection;
            this.numUsage = source.numUsage;
            this.numIpConfigFailures = source.numIpConfigFailures;
            this.isAutoJoinCandidate = source.isAutoJoinCandidate;
            this.venueName = source.venueName;
            this.operatorFriendlyName = source.operatorFriendlyName;
            this.flags = source.flags;
            this.is5GHzVsi = source.is5GHzVsi;
            this.ChannelMode = source.ChannelMode;
            this.BSSLoadElement = source.BSSLoadElement;
            this.SamsungVsie = source.SamsungVsie;
        }
    }

    public String toString() {
        String str;
        StringBuffer sb = new StringBuffer();
        String none = "<none>";
        StringBuffer append = sb.append("SSID: ").append(this.wifiSsid == null ? WifiSsid.NONE : this.wifiSsid).append(", BSSID: ");
        if (this.BSSID == null) {
            str = none;
        } else {
            str = this.BSSID;
        }
        append = append.append(str).append(", HESSID: ");
        if (this.HESSID == null) {
            str = none;
        } else {
            str = this.HESSID;
        }
        StringBuffer append2 = append.append(str).append(", capabilities: ");
        if (this.capabilities != null) {
            none = this.capabilities;
        }
        append2.append(none).append(", level: ").append(this.level).append(", frequency: ").append(this.frequency).append(", timestamp: ").append(this.timestamp);
        sb.append(", distance: ").append(this.distanceCm != -1 ? Integer.valueOf(this.distanceCm) : "?").append("(cm)");
        sb.append(", distanceSd: ").append(this.distanceSdCm != -1 ? Integer.valueOf(this.distanceSdCm) : "?").append("(cm)");
        sb.append(", passpoint: ");
        sb.append((this.flags & 1) != 0 ? "yes" : "no");
        if (this.autoJoinStatus != 0) {
            sb.append(", status: ").append(this.autoJoinStatus);
        }
        sb.append(", ChannelBandwidth: ").append(this.channelWidth);
        sb.append(", centerFreq0: ").append(this.centerFreq0);
        sb.append(", centerFreq1: ").append(this.centerFreq1);
        sb.append(", 80211mcResponder: ");
        sb.append((this.flags & 2) != 0 ? "is supported" : "is not supported");
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i = 1;
        if (this.wifiSsid != null) {
            dest.writeInt(1);
            this.wifiSsid.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        dest.writeString(this.SSID);
        dest.writeString(this.BSSID);
        dest.writeString(this.HESSID);
        dest.writeString(this.capabilities);
        dest.writeInt(this.level);
        dest.writeInt(this.frequency);
        dest.writeLong(this.timestamp);
        dest.writeInt(this.distanceCm);
        dest.writeInt(this.distanceSdCm);
        dest.writeInt(this.channelWidth);
        dest.writeInt(this.centerFreq0);
        dest.writeInt(this.centerFreq1);
        dest.writeLong(this.seen);
        dest.writeInt(this.autoJoinStatus);
        dest.writeInt(this.untrusted ? 1 : 0);
        dest.writeInt(this.numConnection);
        dest.writeInt(this.numUsage);
        dest.writeInt(this.numIpConfigFailures);
        dest.writeInt(this.isAutoJoinCandidate);
        dest.writeString(this.venueName != null ? this.venueName.toString() : ProxyInfo.LOCAL_EXCL_LIST);
        dest.writeString(this.operatorFriendlyName != null ? this.operatorFriendlyName.toString() : ProxyInfo.LOCAL_EXCL_LIST);
        dest.writeLong(this.flags);
        if (!this.is5GHzVsi) {
            i = 0;
        }
        dest.writeInt(i);
        dest.writeString(this.ChannelMode != null ? this.ChannelMode.toString() : ProxyInfo.LOCAL_EXCL_LIST);
        dest.writeString(this.BSSLoadElement != null ? this.BSSLoadElement.toString() : ProxyInfo.LOCAL_EXCL_LIST);
        dest.writeString(this.SamsungVsie != null ? this.SamsungVsie.toString() : ProxyInfo.LOCAL_EXCL_LIST);
        if (this.informationElements != null) {
            dest.writeInt(this.informationElements.length);
            for (int i2 = 0; i2 < this.informationElements.length; i2++) {
                dest.writeInt(this.informationElements[i2].id);
                dest.writeInt(this.informationElements[i2].bytes.length);
                dest.writeByteArray(this.informationElements[i2].bytes);
            }
            return;
        }
        dest.writeInt(0);
    }
}
