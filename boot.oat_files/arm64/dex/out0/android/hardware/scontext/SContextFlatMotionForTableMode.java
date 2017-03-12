package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextFlatMotionForTableMode extends SContextEventContext {
    public static final Creator<SContextFlatMotionForTableMode> CREATOR = new Creator<SContextFlatMotionForTableMode>() {
        public SContextFlatMotionForTableMode createFromParcel(Parcel in) {
            return new SContextFlatMotionForTableMode(in);
        }

        public SContextFlatMotionForTableMode[] newArray(int size) {
            return new SContextFlatMotionForTableMode[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextFlatMotionForTableMode() {
        this.mContext = new Bundle();
    }

    SContextFlatMotionForTableMode(Parcel src) {
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
