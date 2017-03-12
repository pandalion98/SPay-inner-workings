package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextPutDownMotion extends SContextEventContext {
    public static final Creator<SContextPutDownMotion> CREATOR = new Creator<SContextPutDownMotion>() {
        public SContextPutDownMotion createFromParcel(Parcel in) {
            return new SContextPutDownMotion(in);
        }

        public SContextPutDownMotion[] newArray(int size) {
            return new SContextPutDownMotion[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextPutDownMotion() {
        this.mContext = new Bundle();
    }

    SContextPutDownMotion(Parcel src) {
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
