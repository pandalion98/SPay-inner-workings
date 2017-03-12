package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class PPPOEInfo implements Parcelable {
    public static final Creator<PPPOEInfo> CREATOR = new Creator<PPPOEInfo>() {
        public PPPOEInfo createFromParcel(Parcel in) {
            PPPOEInfo info = new PPPOEInfo();
            info.status = Status.values()[in.readInt()];
            info.online_time = in.readLong();
            return info;
        }

        public PPPOEInfo[] newArray(int size) {
            return new PPPOEInfo[size];
        }
    };
    private static final boolean DBG = true;
    private static final String TAG = "PPPOEInfo";
    public long online_time = 0;
    public Status status = Status.OFFLINE;

    public enum Status {
        OFFLINE,
        CONNECTING,
        ONLINE
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Status: ").append(this.status).append(", Online time: ").append(this.online_time);
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.status.ordinal());
        dest.writeLong(this.online_time);
    }
}
