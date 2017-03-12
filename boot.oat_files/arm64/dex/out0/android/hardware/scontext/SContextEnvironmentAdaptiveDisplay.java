package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextEnvironmentAdaptiveDisplay extends SContextEventContext {
    public static final Creator<SContextEnvironmentAdaptiveDisplay> CREATOR = new Creator<SContextEnvironmentAdaptiveDisplay>() {
        public SContextEnvironmentAdaptiveDisplay createFromParcel(Parcel in) {
            return new SContextEnvironmentAdaptiveDisplay(in);
        }

        public SContextEnvironmentAdaptiveDisplay[] newArray(int size) {
            return new SContextEnvironmentAdaptiveDisplay[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextEnvironmentAdaptiveDisplay() {
        this.mContext = new Bundle();
    }

    SContextEnvironmentAdaptiveDisplay(Parcel src) {
        readFromParcel(src);
    }

    public float getRed() {
        return this.mContext.getFloat("R");
    }

    public float getGreen() {
        return this.mContext.getFloat("G");
    }

    public float getBlue() {
        return this.mContext.getFloat("B");
    }

    public long getLux() {
        return this.mContext.getLong("Lux");
    }

    public int getCCT() {
        return this.mContext.getInt("CCT");
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
