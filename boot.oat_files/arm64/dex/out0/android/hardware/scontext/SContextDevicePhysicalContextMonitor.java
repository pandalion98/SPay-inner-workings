package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextDevicePhysicalContextMonitor extends SContextEventContext {
    public static final Creator<SContextDevicePhysicalContextMonitor> CREATOR = new Creator<SContextDevicePhysicalContextMonitor>() {
        public SContextDevicePhysicalContextMonitor createFromParcel(Parcel in) {
            return new SContextDevicePhysicalContextMonitor(in);
        }

        public SContextDevicePhysicalContextMonitor[] newArray(int size) {
            return new SContextDevicePhysicalContextMonitor[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextDevicePhysicalContextMonitor() {
        this.mContext = new Bundle();
    }

    SContextDevicePhysicalContextMonitor(Parcel src) {
        readFromParcel(src);
    }

    public int getAODStatus() {
        return this.mContext.getInt("AODStatus");
    }

    public int getAODReason() {
        return this.mContext.getInt("AODReason");
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
