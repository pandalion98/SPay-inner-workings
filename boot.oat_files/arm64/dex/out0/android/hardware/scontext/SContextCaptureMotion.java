package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@Deprecated
public class SContextCaptureMotion extends SContextEventContext {
    public static final Creator<SContextCaptureMotion> CREATOR = new Creator<SContextCaptureMotion>() {
        public SContextCaptureMotion createFromParcel(Parcel in) {
            return new SContextCaptureMotion(in);
        }

        public SContextCaptureMotion[] newArray(int size) {
            return new SContextCaptureMotion[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextCaptureMotion() {
        this.mContext = new Bundle();
    }

    SContextCaptureMotion(Parcel src) {
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
