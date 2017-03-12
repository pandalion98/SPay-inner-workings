package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@Deprecated
public class SContextTemperatureAlert extends SContextEventContext {
    public static final Creator<SContextTemperatureAlert> CREATOR = new Creator<SContextTemperatureAlert>() {
        public SContextTemperatureAlert createFromParcel(Parcel in) {
            return new SContextTemperatureAlert(in);
        }

        public SContextTemperatureAlert[] newArray(int size) {
            return new SContextTemperatureAlert[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextTemperatureAlert() {
        this.mContext = new Bundle();
    }

    SContextTemperatureAlert(Parcel src) {
        readFromParcel(src);
    }

    public int getAction() {
        return this.mContext.getInt("Action");
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
