package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

@Deprecated
public class SContextCurrentStatusForPositioning extends SContextEventContext {
    public static final Creator<SContextCurrentStatusForPositioning> CREATOR = new Creator<SContextCurrentStatusForPositioning>() {
        public SContextCurrentStatusForPositioning createFromParcel(Parcel in) {
            return new SContextCurrentStatusForPositioning(in);
        }

        public SContextCurrentStatusForPositioning[] newArray(int size) {
            return new SContextCurrentStatusForPositioning[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextCurrentStatusForPositioning() {
        this.mContext = new Bundle();
    }

    SContextCurrentStatusForPositioning(Parcel src) {
        readFromParcel(src);
    }

    public int getStatus() {
        return this.mContext.getInt("Status");
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mContext);
    }

    private void readFromParcel(Parcel src) {
        this.mContext = src.readBundle();
    }
}
