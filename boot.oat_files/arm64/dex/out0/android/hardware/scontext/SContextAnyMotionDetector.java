package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextAnyMotionDetector extends SContextEventContext {
    public static final Creator<SContextAnyMotionDetector> CREATOR = new Creator<SContextAnyMotionDetector>() {
        public SContextAnyMotionDetector createFromParcel(Parcel in) {
            return new SContextAnyMotionDetector(in);
        }

        public SContextAnyMotionDetector[] newArray(int size) {
            return new SContextAnyMotionDetector[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextAnyMotionDetector() {
        this.mContext = new Bundle();
    }

    SContextAnyMotionDetector(Parcel src) {
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
