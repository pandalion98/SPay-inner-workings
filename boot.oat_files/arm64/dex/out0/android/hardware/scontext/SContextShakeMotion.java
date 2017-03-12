package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextShakeMotion extends SContextEventContext {
    public static final Creator<SContextShakeMotion> CREATOR = new Creator<SContextShakeMotion>() {
        public SContextShakeMotion createFromParcel(Parcel in) {
            return new SContextShakeMotion(in);
        }

        public SContextShakeMotion[] newArray(int size) {
            return new SContextShakeMotion[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextShakeMotion() {
        this.mContext = new Bundle();
    }

    SContextShakeMotion(Parcel src) {
        readFromParcel(src);
    }

    public int getShakeStatus() {
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
