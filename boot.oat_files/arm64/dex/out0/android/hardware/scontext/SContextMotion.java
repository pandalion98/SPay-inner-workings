package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@Deprecated
public class SContextMotion extends SContextEventContext {
    public static final Creator<SContextMotion> CREATOR = new Creator<SContextMotion>() {
        public SContextMotion createFromParcel(Parcel in) {
            return new SContextMotion(in);
        }

        public SContextMotion[] newArray(int size) {
            return new SContextMotion[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextMotion() {
        this.mContext = new Bundle();
    }

    SContextMotion(Parcel src) {
        readFromParcel(src);
    }

    public int getType() {
        return this.mContext.getInt("Type");
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
