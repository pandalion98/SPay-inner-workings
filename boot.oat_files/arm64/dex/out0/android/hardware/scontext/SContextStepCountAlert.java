package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextStepCountAlert extends SContextEventContext {
    public static final Creator<SContextStepCountAlert> CREATOR = new Creator<SContextStepCountAlert>() {
        public SContextStepCountAlert createFromParcel(Parcel in) {
            return new SContextStepCountAlert(in);
        }

        public SContextStepCountAlert[] newArray(int size) {
            return new SContextStepCountAlert[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextStepCountAlert() {
        this.mContext = new Bundle();
    }

    SContextStepCountAlert(Parcel src) {
        readFromParcel(src);
    }

    public int getAlert() {
        if (this.mContext.getInt("Action") == 1) {
            return 1;
        }
        return 0;
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
