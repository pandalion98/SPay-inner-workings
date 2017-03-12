package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextInactiveTimer extends SContextEventContext {
    public static final Creator<SContextInactiveTimer> CREATOR = new Creator<SContextInactiveTimer>() {
        public SContextInactiveTimer createFromParcel(Parcel in) {
            return new SContextInactiveTimer(in);
        }

        public SContextInactiveTimer[] newArray(int size) {
            return new SContextInactiveTimer[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextInactiveTimer() {
        this.mContext = new Bundle();
    }

    SContextInactiveTimer(Parcel src) {
        readFromParcel(src);
    }

    public int getDuration() {
        return this.mContext.getInt("InactiveTimeDuration");
    }

    public int getStatus() {
        return this.mContext.getInt("InactiveStatus");
    }

    public boolean isTimeOutExpired() {
        return this.mContext.getBoolean("IsTimeOut");
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
