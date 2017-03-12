package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextSpecificPoseAlert extends SContextEventContext {
    public static final Creator<SContextSpecificPoseAlert> CREATOR = new Creator<SContextSpecificPoseAlert>() {
        public SContextSpecificPoseAlert createFromParcel(Parcel in) {
            return new SContextSpecificPoseAlert(in);
        }

        public SContextSpecificPoseAlert[] newArray(int size) {
            return new SContextSpecificPoseAlert[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextSpecificPoseAlert() {
        this.mContext = new Bundle();
    }

    SContextSpecificPoseAlert(Parcel src) {
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
