package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextCallMotion extends SContextEventContext {
    public static final Creator<SContextCallMotion> CREATOR = new Creator<SContextCallMotion>() {
        public SContextCallMotion createFromParcel(Parcel in) {
            return new SContextCallMotion(in);
        }

        public SContextCallMotion[] newArray(int size) {
            return new SContextCallMotion[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextCallMotion() {
        this.mContext = new Bundle();
    }

    SContextCallMotion(Parcel src) {
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
