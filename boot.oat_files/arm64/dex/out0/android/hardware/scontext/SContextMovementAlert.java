package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@Deprecated
public class SContextMovementAlert extends SContextEventContext {
    public static final Creator<SContextMovementAlert> CREATOR = new Creator<SContextMovementAlert>() {
        public SContextMovementAlert createFromParcel(Parcel in) {
            return new SContextMovementAlert(in);
        }

        public SContextMovementAlert[] newArray(int size) {
            return new SContextMovementAlert[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextMovementAlert() {
        this.mContext = new Bundle();
    }

    SContextMovementAlert(Parcel src) {
        readFromParcel(src);
    }

    public int getAction() {
        return this.mContext.getInt("Action");
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mContext);
    }

    private void readFromParcel(Parcel src) {
        this.mContext = src.readBundle();
    }
}
