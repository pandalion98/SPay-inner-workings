package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextAutoBrightness extends SContextEventContext {
    public static final Creator<SContextAutoBrightness> CREATOR = new Creator<SContextAutoBrightness>() {
        public SContextAutoBrightness createFromParcel(Parcel in) {
            return new SContextAutoBrightness(in);
        }

        public SContextAutoBrightness[] newArray(int size) {
            return new SContextAutoBrightness[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextAutoBrightness() {
        this.mContext = new Bundle();
    }

    SContextAutoBrightness(Parcel src) {
        readFromParcel(src);
    }

    public int getAmbientLux() {
        return this.mContext.getInt("AmbientLux");
    }

    public int getCandela() {
        return this.mContext.getInt("Candela");
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
