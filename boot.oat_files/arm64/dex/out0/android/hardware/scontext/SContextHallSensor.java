package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextHallSensor extends SContextEventContext {
    public static final Creator<SContextHallSensor> CREATOR = new Creator<SContextHallSensor>() {
        public SContextHallSensor createFromParcel(Parcel in) {
            return new SContextHallSensor(in);
        }

        public SContextHallSensor[] newArray(int size) {
            return new SContextHallSensor[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    public SContextHallSensor() {
        this.mContext = new Bundle();
    }

    public SContextHallSensor(Parcel src) {
        readFromParcel(src);
    }

    public short getAngle() {
        return this.mContext.getShort("Angle");
    }

    public short getType() {
        return this.mContext.getShort("Type");
    }

    public short getIntensity() {
        return this.mContext.getShort("Intensity");
    }

    public void setValues(Bundle context) {
        this.mContext = context;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mContext);
    }

    private void readFromParcel(Parcel src) {
        this.mContext = src.readBundle();
    }
}
