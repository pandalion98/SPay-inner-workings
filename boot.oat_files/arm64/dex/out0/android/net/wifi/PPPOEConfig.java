package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PPPOEConfig implements Parcelable {
    public static final Creator<PPPOEConfig> CREATOR = new Creator<PPPOEConfig>() {
        public PPPOEConfig createFromParcel(Parcel in) {
            PPPOEConfig config = new PPPOEConfig();
            config.username = in.readString();
            config.password = in.readString();
            config.interf = in.readString();
            config.lcp_echo_interval = in.readInt();
            config.lcp_echo_failure = in.readInt();
            config.mtu = in.readInt();
            config.mru = in.readInt();
            config.timeout = in.readInt();
            config.MSS = in.readInt();
            return config;
        }

        public PPPOEConfig[] newArray(int size) {
            return new PPPOEConfig[size];
        }
    };
    private static final boolean DBG = true;
    private static final String TAG = "PPPOEConfig";
    public int MSS = 1412;
    public String interf = null;
    public int lcp_echo_failure = 3;
    public int lcp_echo_interval = 20;
    public int mru = 1492;
    public int mtu = 1492;
    public String password = null;
    public int timeout = 5;
    public String username = null;

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Username: ").append(this.username).append(", Password: ").append(this.password).append(", Interface: ").append(this.interf).append(", LCP echo interval: ").append(this.lcp_echo_interval).append(", LCP echo failure: ").append(this.lcp_echo_failure).append(", MTU: ").append(this.mtu).append(", MRU: ").append(this.mru).append(", Timeout: ").append(this.timeout).append(", MSS: ").append(this.MSS);
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.password);
        dest.writeString(this.interf);
        dest.writeInt(this.lcp_echo_interval);
        dest.writeInt(this.lcp_echo_failure);
        dest.writeInt(this.mtu);
        dest.writeInt(this.mru);
        dest.writeInt(this.timeout);
        dest.writeInt(this.MSS);
    }
}
