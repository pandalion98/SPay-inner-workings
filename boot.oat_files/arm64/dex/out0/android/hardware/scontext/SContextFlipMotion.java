package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextFlipMotion extends SContextEventContext {
    public static final Creator<SContextFlipMotion> CREATOR = new Creator<SContextFlipMotion>() {
        public SContextFlipMotion createFromParcel(Parcel in) {
            return new SContextFlipMotion(in);
        }

        public SContextFlipMotion[] newArray(int size) {
            return new SContextFlipMotion[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextFlipMotion() {
        this.mContext = new Bundle();
    }

    SContextFlipMotion(Parcel src) {
        readFromParcel(src);
    }

    public int getStatus() {
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
