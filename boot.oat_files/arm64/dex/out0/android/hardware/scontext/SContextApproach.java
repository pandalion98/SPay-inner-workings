package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextApproach extends SContextEventContext {
    public static final Creator<SContextApproach> CREATOR = new Creator<SContextApproach>() {
        public SContextApproach createFromParcel(Parcel in) {
            return new SContextApproach(in);
        }

        public SContextApproach[] newArray(int size) {
            return new SContextApproach[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextApproach() {
        this.mContext = new Bundle();
    }

    SContextApproach(Parcel src) {
        readFromParcel(src);
    }

    public int getApproach() {
        return this.mContext.getInt("Proximity");
    }

    public int getUserID() {
        return this.mContext.getInt("UserID");
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
