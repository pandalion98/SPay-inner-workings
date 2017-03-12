package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextWirelessChargingDetection extends SContextEventContext {
    public static final Creator<SContextWirelessChargingDetection> CREATOR = new Creator<SContextWirelessChargingDetection>() {
        public SContextWirelessChargingDetection createFromParcel(Parcel in) {
            return new SContextWirelessChargingDetection(in);
        }

        public SContextWirelessChargingDetection[] newArray(int size) {
            return new SContextWirelessChargingDetection[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextWirelessChargingDetection() {
        this.mContext = new Bundle();
    }

    SContextWirelessChargingDetection(Parcel src) {
        readFromParcel(src);
    }

    public int getAction() {
        return this.mContext.getInt("Status");
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
