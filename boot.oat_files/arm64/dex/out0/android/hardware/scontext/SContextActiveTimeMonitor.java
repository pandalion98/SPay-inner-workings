package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextActiveTimeMonitor extends SContextEventContext {
    public static final Creator<SContextActiveTimeMonitor> CREATOR = new Creator<SContextActiveTimeMonitor>() {
        public SContextActiveTimeMonitor createFromParcel(Parcel in) {
            return new SContextActiveTimeMonitor(in);
        }

        public SContextActiveTimeMonitor[] newArray(int size) {
            return new SContextActiveTimeMonitor[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextActiveTimeMonitor() {
        this.mContext = new Bundle();
    }

    SContextActiveTimeMonitor(Parcel src) {
        readFromParcel(src);
    }

    public int getDuration() {
        return this.mContext.getInt("ActiveTimeDuration");
    }

    void setValues(Bundle context) {
        this.mContext = context;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mContext);
    }

    private void readFromParcel(Parcel src) {
        this.mContext = src.readBundle();
    }
}
