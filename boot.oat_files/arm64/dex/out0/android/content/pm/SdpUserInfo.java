package android.content.pm;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import android.util.Slog;

public class SdpUserInfo implements Parcelable {
    public static final Creator<SdpUserInfo> CREATOR = new Creator<SdpUserInfo>() {
        public SdpUserInfo createFromParcel(Parcel source) {
            return new SdpUserInfo(source);
        }

        public SdpUserInfo[] newArray(int size) {
            return new SdpUserInfo[size];
        }
    };
    private static final String TAG = "SdpUserInfo";
    public int flags;
    public int id;
    public boolean isMigrating;
    public int prevStatus;
    public int status;
    public int version;

    public SdpUserInfo(int id, int flags) {
        this.flags = 0;
        this.version = -1;
        this.prevStatus = 0;
        this.status = 0;
        this.isMigrating = false;
        this.id = id;
        this.flags = flags;
        this.prevStatus = -1;
        this.status = -1;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeInt(this.id);
        dest.writeInt(this.flags);
        dest.writeInt(this.version);
        dest.writeInt(this.prevStatus);
        dest.writeInt(this.status);
        dest.writeInt(this.isMigrating ? 1 : 0);
    }

    public void dump() {
        Log.d(TAG, "SDP INFO DUMP : id = " + this.id + "; flags = " + this.flags + "; version = " + this.version + "; prevStatus = " + this.prevStatus + "; status = " + this.status);
        Slog.e(TAG, "\n");
    }

    private SdpUserInfo(Parcel source) {
        boolean z = false;
        this.flags = 0;
        this.version = -1;
        this.prevStatus = 0;
        this.status = 0;
        this.isMigrating = false;
        this.id = source.readInt();
        this.flags = source.readInt();
        this.version = source.readInt();
        this.prevStatus = source.readInt();
        this.status = source.readInt();
        if (source.readInt() != 0) {
            z = true;
        }
        this.isMigrating = z;
    }
}
