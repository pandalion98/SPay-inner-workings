package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextCallPose extends SContextEventContext {
    public static final Creator<SContextCallPose> CREATOR = new Creator<SContextCallPose>() {
        public SContextCallPose createFromParcel(Parcel in) {
            return new SContextCallPose(in);
        }

        public SContextCallPose[] newArray(int size) {
            return new SContextCallPose[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextCallPose() {
        this.mContext = new Bundle();
    }

    SContextCallPose(Parcel src) {
        readFromParcel(src);
    }

    public int getPose() {
        return this.mContext.getInt("Pose");
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
