package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextFlatMotion extends SContextEventContext {
    public static final Creator<SContextFlatMotion> CREATOR = new Creator<SContextFlatMotion>() {
        public SContextFlatMotion createFromParcel(Parcel in) {
            return new SContextFlatMotion(in);
        }

        public SContextFlatMotion[] newArray(int size) {
            return new SContextFlatMotion[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextFlatMotion() {
        this.mContext = new Bundle();
    }

    SContextFlatMotion(Parcel src) {
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
