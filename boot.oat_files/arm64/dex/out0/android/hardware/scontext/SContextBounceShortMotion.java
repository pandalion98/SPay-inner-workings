package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextBounceShortMotion extends SContextEventContext {
    public static final Creator<SContextBounceShortMotion> CREATOR = new Creator<SContextBounceShortMotion>() {
        public SContextBounceShortMotion createFromParcel(Parcel in) {
            return new SContextBounceShortMotion(in);
        }

        public SContextBounceShortMotion[] newArray(int size) {
            return new SContextBounceShortMotion[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextBounceShortMotion() {
        this.mContext = new Bundle();
    }

    SContextBounceShortMotion(Parcel src) {
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
