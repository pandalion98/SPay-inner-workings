package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextMainScreenDetection extends SContextEventContext {
    public static final Creator<SContextMainScreenDetection> CREATOR = new Creator<SContextMainScreenDetection>() {
        public SContextMainScreenDetection createFromParcel(Parcel in) {
            return new SContextMainScreenDetection(in);
        }

        public SContextMainScreenDetection[] newArray(int size) {
            return new SContextMainScreenDetection[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextMainScreenDetection() {
        this.mContext = new Bundle();
    }

    SContextMainScreenDetection(Parcel src) {
        readFromParcel(src);
    }

    public int getScreenStatus() {
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
