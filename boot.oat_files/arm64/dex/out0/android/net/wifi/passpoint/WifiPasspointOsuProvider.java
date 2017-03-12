package android.net.wifi.passpoint;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WifiPasspointOsuProvider implements Parcelable {
    public static final Creator<WifiPasspointOsuProvider> CREATOR = new Creator<WifiPasspointOsuProvider>() {
        public WifiPasspointOsuProvider createFromParcel(Parcel in) {
            WifiPasspointOsuProvider osu = new WifiPasspointOsuProvider();
            osu.ssid = in.readString();
            osu.friendlyName = in.readString();
            osu.serverUri = in.readString();
            osu.osuMethod = in.readInt();
            osu.iconWidth = in.readInt();
            osu.iconHeight = in.readInt();
            osu.iconType = in.readString();
            osu.iconFileName = in.readString();
            osu.osuNai = in.readString();
            osu.osuService = in.readString();
            return osu;
        }

        public WifiPasspointOsuProvider[] newArray(int size) {
            return new WifiPasspointOsuProvider[size];
        }
    };
    public static final int OSU_METHOD_OMADM = 0;
    public static final int OSU_METHOD_SOAP = 1;
    public static final int OSU_METHOD_UNKNOWN = -1;
    public String friendlyName;
    public Object icon;
    public String iconFileName;
    public int iconHeight;
    public String iconType;
    public int iconWidth;
    public int osuMethod = -1;
    public String osuNai;
    public String osuService;
    public String serverUri;
    public String ssid;

    public WifiPasspointOsuProvider(WifiPasspointOsuProvider source) {
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("SSID: ").append("<").append(this.ssid).append(">");
        if (this.friendlyName != null) {
            sb.append(" friendlyName: ").append("<").append(this.friendlyName).append(">");
        }
        if (this.serverUri != null) {
            sb.append(" serverUri: ").append("<").append(this.serverUri).append(">");
        }
        sb.append(" osuMethod: ").append("<").append(this.osuMethod).append(">");
        if (this.iconFileName != null) {
            sb.append(" icon: <").append(this.iconWidth).append("x").append(this.iconHeight).append(" ").append(this.iconType).append(" ").append(this.iconFileName).append(">");
        }
        if (this.osuNai != null) {
            sb.append(" osuNai: ").append("<").append(this.osuNai).append(">");
        }
        if (this.osuService != null) {
            sb.append(" osuService: ").append("<").append(this.osuService).append(">");
        }
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.ssid);
        out.writeString(this.friendlyName);
        out.writeString(this.serverUri);
        out.writeInt(this.osuMethod);
        out.writeInt(this.iconWidth);
        out.writeInt(this.iconHeight);
        out.writeString(this.iconType);
        out.writeString(this.iconFileName);
        out.writeString(this.osuNai);
        out.writeString(this.osuService);
    }
}
