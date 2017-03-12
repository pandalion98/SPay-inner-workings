package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextExercise extends SContextEventContext {
    public static final Creator<SContextExercise> CREATOR = new Creator<SContextExercise>() {
        public SContextExercise createFromParcel(Parcel in) {
            return new SContextExercise(in);
        }

        public SContextExercise[] newArray(int size) {
            return new SContextExercise[size];
        }
    };
    private Bundle mContext;
    private int mMode;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextExercise() {
        this.mContext = new Bundle();
        this.mMode = 0;
    }

    SContextExercise(Parcel src) {
        readFromParcel(src);
    }

    public int getMode() {
        return this.mContext.getInt("Mode");
    }

    public int getLoggingSize() {
        if (this.mMode == 0) {
            return this.mContext.getInt("DataCount");
        }
        return 0;
    }

    public long[] getTimeStamp() {
        if (this.mMode == 0) {
            return this.mContext.getLongArray("TimeStampArray");
        }
        return null;
    }

    public double[] getLatitude() {
        if (this.mMode == 0) {
            return this.mContext.getDoubleArray("LatitudeArray");
        }
        return null;
    }

    public double[] getLongitude() {
        if (this.mMode == 0) {
            return this.mContext.getDoubleArray("LongitudeArray");
        }
        return null;
    }

    public float[] getAltitude() {
        if (this.mMode == 0) {
            return this.mContext.getFloatArray("AltitudeArray");
        }
        return null;
    }

    public float[] getPressure() {
        if (this.mMode == 0) {
            return this.mContext.getFloatArray("PressureArray");
        }
        return null;
    }

    public float[] getSpeed() {
        if (this.mMode == 0) {
            return this.mContext.getFloatArray("SpeedArray");
        }
        return null;
    }

    public double[] getPedoDistance() {
        if (this.mMode == 0) {
            return this.mContext.getDoubleArray("PedoDistanceDiffArray");
        }
        return null;
    }

    public double[] getPedoSpeed() {
        if (this.mMode == 0) {
            return this.mContext.getDoubleArray("PedoSpeedArray");
        }
        return null;
    }

    public long[] getStepCount() {
        if (this.mMode == 0) {
            return this.mContext.getLongArray("StepCountDiffArray");
        }
        return null;
    }

    public int getStatus() {
        if (this.mMode == 1) {
            return this.mContext.getInt("GpsStatus");
        }
        return 0;
    }

    void setValues(Bundle context) {
        this.mContext = context;
        this.mMode = context.getInt("Mode");
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
