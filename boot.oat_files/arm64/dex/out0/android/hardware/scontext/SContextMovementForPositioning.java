package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@Deprecated
public class SContextMovementForPositioning extends SContextEventContext {
    public static final Creator<SContextMovementForPositioning> CREATOR = new Creator<SContextMovementForPositioning>() {
        public SContextMovementForPositioning createFromParcel(Parcel in) {
            return new SContextMovementForPositioning(in);
        }

        public SContextMovementForPositioning[] newArray(int size) {
            return new SContextMovementForPositioning[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextMovementForPositioning() {
        this.mContext = new Bundle();
    }

    SContextMovementForPositioning(Parcel src) {
        readFromParcel(src);
    }

    public int getAlert() {
        return this.mContext.getInt("Alert");
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
