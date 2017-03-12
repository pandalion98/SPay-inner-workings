package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextAirMotion extends SContextEventContext {
    public static final Creator<SContextAirMotion> CREATOR = new Creator<SContextAirMotion>() {
        public SContextAirMotion createFromParcel(Parcel in) {
            return new SContextAirMotion(in);
        }

        public SContextAirMotion[] newArray(int size) {
            return new SContextAirMotion[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextAirMotion() {
        this.mContext = new Bundle();
    }

    SContextAirMotion(Parcel src) {
        readFromParcel(src);
    }

    public int getDirection() {
        return this.mContext.getInt("Direction");
    }

    public int getAngle() {
        return this.mContext.getInt("Angle");
    }

    public int getSpeed() {
        return this.mContext.getInt("Speed");
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
