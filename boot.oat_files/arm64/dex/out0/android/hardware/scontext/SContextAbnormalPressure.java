package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextAbnormalPressure extends SContextEventContext {
    public static final Creator<SContextAbnormalPressure> CREATOR = new Creator<SContextAbnormalPressure>() {
        public SContextAbnormalPressure createFromParcel(Parcel in) {
            return new SContextAbnormalPressure(in);
        }

        public SContextAbnormalPressure[] newArray(int size) {
            return new SContextAbnormalPressure[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextAbnormalPressure() {
        this.mContext = new Bundle();
    }

    SContextAbnormalPressure(Parcel src) {
        readFromParcel(src);
    }

    public float getPressure() {
        return this.mContext.getFloat("barometer");
    }

    public float getAccX() {
        return this.mContext.getFloat("xaxis");
    }

    public float getAccY() {
        return this.mContext.getFloat("yaxis");
    }

    public float getAccZ() {
        return this.mContext.getFloat("zaxis");
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
