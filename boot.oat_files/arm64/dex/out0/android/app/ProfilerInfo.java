package android.app;

import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ProfilerInfo implements Parcelable {
    public static final Creator<ProfilerInfo> CREATOR = new Creator<ProfilerInfo>() {
        public ProfilerInfo createFromParcel(Parcel in) {
            return new ProfilerInfo(in);
        }

        public ProfilerInfo[] newArray(int size) {
            return new ProfilerInfo[size];
        }
    };
    public final boolean autoStopProfiler;
    public ParcelFileDescriptor profileFd;
    public final String profileFile;
    public final int samplingInterval;

    public ProfilerInfo(String filename, ParcelFileDescriptor fd, int interval, boolean autoStop) {
        this.profileFile = filename;
        this.profileFd = fd;
        this.samplingInterval = interval;
        this.autoStopProfiler = autoStop;
    }

    public int describeContents() {
        if (this.profileFd != null) {
            return this.profileFd.describeContents();
        }
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        int i = 1;
        out.writeString(this.profileFile);
        if (this.profileFd != null) {
            out.writeInt(1);
            this.profileFd.writeToParcel(out, flags);
        } else {
            out.writeInt(0);
        }
        out.writeInt(this.samplingInterval);
        if (!this.autoStopProfiler) {
            i = 0;
        }
        out.writeInt(i);
    }

    private ProfilerInfo(Parcel in) {
        this.profileFile = in.readString();
        this.profileFd = in.readInt() != 0 ? (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(in) : null;
        this.samplingInterval = in.readInt();
        this.autoStopProfiler = in.readInt() != 0;
    }
}
