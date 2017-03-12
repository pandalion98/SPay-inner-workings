package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@Deprecated
public class SContextTestFlatMotion extends SContextEventContext {
    public static final Creator<SContextTestFlatMotion> CREATOR = new Creator<SContextTestFlatMotion>() {
        public SContextTestFlatMotion createFromParcel(Parcel in) {
            return new SContextTestFlatMotion(in);
        }

        public SContextTestFlatMotion[] newArray(int size) {
            return new SContextTestFlatMotion[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextTestFlatMotion() {
        this.mContext = new Bundle();
    }

    SContextTestFlatMotion(Parcel src) {
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
