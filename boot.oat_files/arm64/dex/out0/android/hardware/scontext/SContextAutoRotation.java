package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextAutoRotation extends SContextEventContext {
    public static final Creator<SContextAutoRotation> CREATOR = new Creator<SContextAutoRotation>() {
        public SContextAutoRotation createFromParcel(Parcel in) {
            return new SContextAutoRotation(in);
        }

        public SContextAutoRotation[] newArray(int size) {
            return new SContextAutoRotation[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextAutoRotation() {
        this.mContext = new Bundle();
    }

    SContextAutoRotation(Parcel src) {
        readFromParcel(src);
    }

    public int getAngle() {
        return this.mContext.getInt("Angle");
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
