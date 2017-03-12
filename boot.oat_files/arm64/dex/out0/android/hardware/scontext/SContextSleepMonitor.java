package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@Deprecated
public class SContextSleepMonitor extends SContextEventContext {
    public static final Creator<SContextSleepMonitor> CREATOR = new Creator<SContextSleepMonitor>() {
        public SContextSleepMonitor createFromParcel(Parcel in) {
            return new SContextSleepMonitor(in);
        }

        public SContextSleepMonitor[] newArray(int size) {
            return new SContextSleepMonitor[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextSleepMonitor() {
        this.mContext = new Bundle();
    }

    SContextSleepMonitor(Parcel src) {
        readFromParcel(src);
    }

    public int[] getStatus() {
        return this.mContext.getIntArray("SleepStatus");
    }

    public float[] getPIM() {
        return this.mContext.getFloatArray("PIM");
    }

    public int[] getZCM() {
        return this.mContext.getIntArray("ZCM");
    }

    public int[] getStage() {
        return this.mContext.getIntArray("Stage");
    }

    public int[] getWrist() {
        return this.mContext.getIntArray("Wrist");
    }

    public int[] getFlag() {
        return this.mContext.getIntArray("Flag");
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
