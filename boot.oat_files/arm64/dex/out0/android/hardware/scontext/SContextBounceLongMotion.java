package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextBounceLongMotion extends SContextEventContext {
    public static final Creator<SContextBounceLongMotion> CREATOR = new Creator<SContextBounceLongMotion>() {
        public SContextBounceLongMotion createFromParcel(Parcel in) {
            return new SContextBounceLongMotion(in);
        }

        public SContextBounceLongMotion[] newArray(int size) {
            return new SContextBounceLongMotion[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextBounceLongMotion() {
        this.mContext = new Bundle();
    }

    SContextBounceLongMotion(Parcel src) {
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
