package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextDualDisplayAngle extends SContextEventContext {
    public static final Creator<SContextDualDisplayAngle> CREATOR = new Creator<SContextDualDisplayAngle>() {
        public SContextDualDisplayAngle createFromParcel(Parcel in) {
            return new SContextDualDisplayAngle(in);
        }

        public SContextDualDisplayAngle[] newArray(int size) {
            return new SContextDualDisplayAngle[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    public SContextDualDisplayAngle() {
        this.mContext = new Bundle();
    }

    public SContextDualDisplayAngle(Parcel src) {
        readFromParcel(src);
    }

    public short getDualAngle() {
        return this.mContext.getShort("Angle");
    }

    public short getType() {
        return this.mContext.getShort("Type");
    }

    public short getIntensity() {
        return this.mContext.getShort("Intensity");
    }

    public void setValues(Bundle context) {
        this.mContext = context;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mContext);
    }

    private void readFromParcel(Parcel src) {
        this.mContext = src.readBundle();
    }
}
