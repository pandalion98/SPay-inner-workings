package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextStepLevelMonitor extends SContextEventContext {
    public static final Creator<SContextStepLevelMonitor> CREATOR = new Creator<SContextStepLevelMonitor>() {
        public SContextStepLevelMonitor createFromParcel(Parcel in) {
            return new SContextStepLevelMonitor(in);
        }

        public SContextStepLevelMonitor[] newArray(int size) {
            return new SContextStepLevelMonitor[size];
        }
    };
    private Bundle mContext;
    private Bundle mInfo;
    private int mMode;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextStepLevelMonitor() {
        this.mContext = new Bundle();
        this.mInfo = new Bundle();
        this.mMode = 0;
    }

    SContextStepLevelMonitor(Parcel src) {
        readFromParcel(src);
    }

    public int getCount() {
        return this.mContext.getInt("DataCount");
    }

    public long[] getTimeStamp() {
        if (this.mMode == 0) {
            int size = this.mContext.getInt("DataCount");
            int[] duration = this.mInfo.getIntArray("DurationArray");
            long[] timestamp = new long[size];
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    timestamp[i] = this.mContext.getLong("TimeStamp");
                } else {
                    timestamp[i] = timestamp[i - 1] + ((long) duration[i - 1]);
                }
            }
            return timestamp;
        } else if (this.mMode == 1) {
            return this.mContext.getLongArray("TimeStampArray");
        } else {
            return null;
        }
    }

    public int[] getDuration() {
        return this.mInfo.getIntArray("DurationArray");
    }

    public int[] getStepLevel() {
        return this.mInfo.getIntArray("StepTypeArray");
    }

    public int[] getStepCount() {
        return this.mInfo.getIntArray("StepCountArray");
    }

    public double[] getDistance() {
        return this.mInfo.getDoubleArray("DistanceArray");
    }

    public double[] getCalorie() {
        return this.mInfo.getDoubleArray("CalorieArray");
    }

    public int getMode() {
        return this.mInfo.getInt("Mode");
    }

    public void setValues(Bundle context) {
        this.mContext = context;
        this.mInfo = context.getBundle("DataBundle");
        this.mMode = context.getInt("Mode");
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mContext);
        dest.writeBundle(this.mInfo);
        dest.writeInt(this.mMode);
    }

    private void readFromParcel(Parcel src) {
        this.mContext = src.readBundle();
        this.mInfo = src.readBundle();
        this.mMode = src.readInt();
    }
}
