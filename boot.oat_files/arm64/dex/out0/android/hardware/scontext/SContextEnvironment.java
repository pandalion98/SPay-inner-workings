package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@Deprecated
public class SContextEnvironment extends SContextEventContext {
    public static final Creator<SContextEnvironment> CREATOR = new Creator<SContextEnvironment>() {
        public SContextEnvironment createFromParcel(Parcel in) {
            return new SContextEnvironment(in);
        }

        public SContextEnvironment[] newArray(int size) {
            return new SContextEnvironment[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextEnvironment() {
        this.mContext = new Bundle();
    }

    SContextEnvironment(Parcel src) {
        readFromParcel(src);
    }

    public int getSensorType() {
        return this.mContext.getInt("EnvSensorType");
    }

    public double[] getData(int index) {
        if (this.mContext.getInt("EnvSensorType") == 1) {
            return getTemperatureHumidityData(index);
        }
        return null;
    }

    private double[] getTemperatureHumidityData(int index) {
        if (index == 0) {
            return this.mContext.getDoubleArray("Temperature");
        }
        if (index == 1) {
            return this.mContext.getDoubleArray("Humidity");
        }
        return null;
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
