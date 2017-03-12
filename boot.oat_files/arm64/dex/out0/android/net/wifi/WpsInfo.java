package android.net.wifi;

import android.net.ProxyInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WpsInfo implements Parcelable {
    public static final Creator<WpsInfo> CREATOR = new Creator<WpsInfo>() {
        public WpsInfo createFromParcel(Parcel in) {
            WpsInfo config = new WpsInfo();
            config.setup = in.readInt();
            config.BSSID = in.readString();
            config.pin = in.readString();
            config.dev_nfc_hashkey = in.readString();
            config.dev_pw_id = in.readString();
            config.dev_pw = in.readString();
            return config;
        }

        public WpsInfo[] newArray(int size) {
            return new WpsInfo[size];
        }
    };
    public static final int DISPLAY = 1;
    public static final int INVALID = 4;
    public static final int KEYPAD = 2;
    public static final int LABEL = 3;
    public static final int NFC_INTERFACE = 6;
    public static final int PBC = 0;
    public static final int USERREJECT = 5;
    public String BSSID;
    public String dev_nfc_hashkey;
    public String dev_pw;
    public String dev_pw_id;
    public String pin;
    public int setup;

    public WpsInfo() {
        this.setup = 4;
        this.BSSID = null;
        this.pin = null;
        this.dev_nfc_hashkey = ProxyInfo.LOCAL_EXCL_LIST;
        this.dev_pw_id = ProxyInfo.LOCAL_EXCL_LIST;
        this.dev_pw = ProxyInfo.LOCAL_EXCL_LIST;
    }

    public String toString() {
        StringBuffer sbuf = new StringBuffer();
        sbuf.append(" setup: ").append(this.setup);
        sbuf.append('\n');
        sbuf.append(" BSSID: ").append(this.BSSID);
        sbuf.append('\n');
        sbuf.append(" pin: ").append(this.pin);
        sbuf.append('\n');
        sbuf.append(" hashkey: ").append(this.dev_nfc_hashkey);
        sbuf.append('\n');
        sbuf.append(" dev_pw_id: ").append(this.dev_pw_id);
        sbuf.append('\n');
        sbuf.append(" dev_pw: ").append(this.dev_pw);
        sbuf.append('\n');
        return sbuf.toString();
    }

    public int describeContents() {
        return 0;
    }

    public WpsInfo(WpsInfo source) {
        if (source != null) {
            this.setup = source.setup;
            this.BSSID = source.BSSID;
            this.pin = source.pin;
            this.dev_nfc_hashkey = source.dev_nfc_hashkey;
            this.dev_pw_id = source.dev_pw_id;
            this.dev_pw = source.dev_pw;
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.setup);
        dest.writeString(this.BSSID);
        dest.writeString(this.pin);
        dest.writeString(this.dev_nfc_hashkey);
        dest.writeString(this.dev_pw_id);
        dest.writeString(this.dev_pw);
    }
}
