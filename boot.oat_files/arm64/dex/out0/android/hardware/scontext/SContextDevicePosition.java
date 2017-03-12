package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextDevicePosition extends SContextEventContext {
    public static final Creator<SContextDevicePosition> CREATOR = new Creator<SContextDevicePosition>() {
        public SContextDevicePosition createFromParcel(Parcel in) {
            return new SContextDevicePosition(in);
        }

        public SContextDevicePosition[] newArray(int size) {
            return new SContextDevicePosition[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextDevicePosition() {
        this.mContext = new Bundle();
    }

    SContextDevicePosition(Parcel src) {
        readFromParcel(src);
    }

    public int getPosition() {
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
