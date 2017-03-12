package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@Deprecated
public class SContextWristUpMotion extends SContextEventContext {
    public static final Creator<SContextWristUpMotion> CREATOR = new Creator<SContextWristUpMotion>() {
        public SContextWristUpMotion createFromParcel(Parcel in) {
            return new SContextWristUpMotion(in);
        }

        public SContextWristUpMotion[] newArray(int size) {
            return new SContextWristUpMotion[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextWristUpMotion() {
        this.mContext = new Bundle();
    }

    SContextWristUpMotion(Parcel src) {
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
