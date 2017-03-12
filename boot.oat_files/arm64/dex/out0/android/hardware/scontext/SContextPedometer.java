package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextPedometer extends SContextEventContext {
    public static final Creator<SContextPedometer> CREATOR = new Creator<SContextPedometer>() {
        public SContextPedometer createFromParcel(Parcel in) {
            return new SContextPedometer(in);
        }

        public SContextPedometer[] newArray(int size) {
            return new SContextPedometer[size];
        }
    };
    private Bundle mContext;
    private int mMode;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextPedometer() {
        this.mContext = new Bundle();
        this.mMode = 0;
    }

    SContextPedometer(Parcel src) {
        readFromParcel(src);
    }

    public int getMode() {
        return this.mMode;
    }

    public int getStepStatus() {
        return this.mContext.getInt("StepStatus");
    }

    public double getSpeed() {
        return this.mContext.getDouble("Speed");
    }

    public double getCalorie() {
        return this.mContext.getDouble("CumulativeCalorie");
    }

    public double getDistance() {
        return this.mContext.getDouble("CumulativeDistance");
    }

    public long getTotalStepCount() {
        return this.mContext.getLong("CumulativeTotalStepCount");
    }

    public long getWalkStepCount() {
        return this.mContext.getLong("CumulativeWalkFlatStepCount");
    }

    public long getWalkUpStepCount() {
        return this.mContext.getLong("CumulativeWalkUpStepCount");
    }

    public long getWalkDownStepCount() {
        return this.mContext.getLong("CumulativeWalkDownStepCount");
    }

    public long getRunStepCount() {
        return this.mContext.getLong("CumulativeRunFlatStepCount");
    }

    public long getRunUpStepCount() {
        return this.mContext.getLong("CumulativeRunUpStepCount");
    }

    public long getRunDownStepCount() {
        return this.mContext.getLong("CumulativeRunDownStepCount");
    }

    @Deprecated
    public long getUpDownStepCount() {
        return this.mContext.getLong("UpDownStepCount");
    }

    public double getCalorieDiff() {
        return this.mContext.getDouble("CalorieDiff");
    }

    public double getDistanceDiff() {
        return this.mContext.getDouble("DistanceDiff");
    }

    public long getTotalStepCountDiff() {
        return this.mContext.getLong("TotalStepCountDiff");
    }

    public long getWalkStepCountDiff() {
        return this.mContext.getLong("WalkStepCountDiff");
    }

    public long getWalkUpStepCountDiff() {
        return this.mContext.getLong("WalkUpStepCountDiff");
    }

    public long getWalkDownStepCountDiff() {
        return this.mContext.getLong("WalkDownStepCountDiff");
    }

    public long getRunStepCountDiff() {
        return this.mContext.getLong("RunStepCountDiff");
    }

    public long getRunUpStepCountDiff() {
        return this.mContext.getLong("RunUpStepCountDiff");
    }

    public long getRunDownStepCountDiff() {
        return this.mContext.getLong("RunDownStepCountDiff");
    }

    @Deprecated
    public long getUpDownStepCountDiff() {
        return this.mContext.getLong("UpDownStepCountDiff");
    }

    public double getWalkingFrequency() {
        return this.mContext.getDouble("WalkingFrequency");
    }

    @Deprecated
    public double getCumulativeDistance() {
        return this.mContext.getDouble("CumulativeDistance");
    }

    @Deprecated
    public double getCumulativeCalorie() {
        return this.mContext.getDouble("CumulativeCalorie");
    }

    @Deprecated
    public long getCumulativeTotalStepCount() {
        return this.mContext.getLong("CumulativeTotalStepCount");
    }

    @Deprecated
    public long getCumulativeWalkStepCount() {
        return this.mContext.getLong("CumulativeWalkFlatStepCount");
    }

    @Deprecated
    public long getCumulativeWalkUpStepCount() {
        return this.mContext.getLong("CumulativeWalkUpStepCount");
    }

    @Deprecated
    public long getCumulativeWalkDownStepCount() {
        return this.mContext.getLong("CumulativeWalkDownStepCount");
    }

    @Deprecated
    public long getCumulativeRunStepCount() {
        return this.mContext.getLong("CumulativeRunFlatStepCount");
    }

    @Deprecated
    public long getCumulativeRunUpStepCount() {
        return this.mContext.getLong("CumulativeRunUpStepCount");
    }

    @Deprecated
    public long getCumulativeRunDownStepCount() {
        return this.mContext.getLong("CumulativeRunDownStepCount");
    }

    public double[] getSpeedArray() {
        if (this.mMode == 1 || this.mMode == 2) {
            return this.mContext.getDoubleArray("SpeedArray");
        }
        return null;
    }

    public double[] getCalorieDiffArray() {
        if (this.mMode == 1 || this.mMode == 2) {
            return this.mContext.getDoubleArray("CalorieDiffArray");
        }
        return null;
    }

    public double[] getDistanceDiffArray() {
        if (this.mMode == 1 || this.mMode == 2) {
            return this.mContext.getDoubleArray("DistanceDiffArray");
        }
        return null;
    }

    public long[] getTotalStepCountDiffArray() {
        if (this.mMode == 1 || this.mMode == 2) {
            return this.mContext.getLongArray("TotalStepCountDiffArray");
        }
        return null;
    }

    public long[] getWalkStepCountDiffArray() {
        if (this.mMode == 1 || this.mMode == 2) {
            return this.mContext.getLongArray("WalkStepCountDiffArray");
        }
        return null;
    }

    public long[] getWalkUpStepCountDiffArray() {
        if (this.mMode == 1 || this.mMode == 2) {
            return this.mContext.getLongArray("WalkUpStepCountDiffArray");
        }
        return null;
    }

    public long[] getWalkDownStepCountDiffArray() {
        if (this.mMode == 1 || this.mMode == 2) {
            return this.mContext.getLongArray("WalkDownStepCountDiffArray");
        }
        return null;
    }

    public long[] getRunStepCountDiffArray() {
        if (this.mMode == 1 || this.mMode == 2) {
            return this.mContext.getLongArray("RunStepCountDiffArray");
        }
        return null;
    }

    public long[] getRunUpStepCountDiffArray() {
        if (this.mMode == 1 || this.mMode == 2) {
            return this.mContext.getLongArray("RunUpStepCountDiffArray");
        }
        return null;
    }

    public long[] getRunDownStepCountDiffArray() {
        if (this.mMode == 1 || this.mMode == 2) {
            return this.mContext.getLongArray("RunDownStepCountDiffArray");
        }
        return null;
    }

    public long[] getTimeStampArray() {
        if (this.mMode == 1 || this.mMode == 2) {
            return this.mContext.getLongArray("TimeStampArray");
        }
        return null;
    }

    public int getArraySize() {
        if (this.mMode == 1 || this.mMode == 2) {
            return this.mContext.getInt("LoggingCount");
        }
        return 0;
    }

    void setValues(Bundle context) {
        this.mContext = context;
        this.mMode = this.mContext.getInt("Mode");
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mContext);
        dest.writeInt(this.mMode);
    }

    private void readFromParcel(Parcel src) {
        this.mContext = src.readBundle();
        this.mMode = src.readInt();
    }
}
