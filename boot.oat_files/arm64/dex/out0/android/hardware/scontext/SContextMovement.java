package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextMovement extends SContextEventContext {
    public static final Creator<SContextMovement> CREATOR = new Creator<SContextMovement>() {
        public SContextMovement createFromParcel(Parcel in) {
            return new SContextMovement(in);
        }

        public SContextMovement[] newArray(int size) {
            return new SContextMovement[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextMovement() {
        this.mContext = new Bundle();
    }

    SContextMovement(Parcel src) {
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
