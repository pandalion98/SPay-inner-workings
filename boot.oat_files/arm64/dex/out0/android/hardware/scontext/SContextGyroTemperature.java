package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextGyroTemperature extends SContextEventContext {
    public static final Creator<SContextGyroTemperature> CREATOR = new Creator<SContextGyroTemperature>() {
        public SContextGyroTemperature createFromParcel(Parcel in) {
            return new SContextGyroTemperature(in);
        }

        public SContextGyroTemperature[] newArray(int size) {
            return new SContextGyroTemperature[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextGyroTemperature() {
        this.mContext = new Bundle();
    }

    SContextGyroTemperature(Parcel src) {
        readFromParcel(src);
    }

    public double getGyroTemperature() {
        return this.mContext.getDouble("GyroTemperature");
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
