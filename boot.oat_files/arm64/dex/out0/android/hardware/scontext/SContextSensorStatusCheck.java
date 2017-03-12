package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextSensorStatusCheck extends SContextEventContext {
    public static final Creator<SContextSensorStatusCheck> CREATOR = new Creator<SContextSensorStatusCheck>() {
        public SContextSensorStatusCheck createFromParcel(Parcel in) {
            return new SContextSensorStatusCheck(in);
        }

        public SContextSensorStatusCheck[] newArray(int size) {
            return new SContextSensorStatusCheck[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextSensorStatusCheck() {
        this.mContext = new Bundle();
    }

    SContextSensorStatusCheck(Parcel src) {
        readFromParcel(src);
    }

    public int getXAxis() {
        return this.mContext.getInt("XAxis");
    }

    public int getYAxis() {
        return this.mContext.getInt("YAxis");
    }

    public int getZAxis() {
        return this.mContext.getInt("ZAxis");
    }

    public int getStatus() {
        return this.mContext.getInt("Status");
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
