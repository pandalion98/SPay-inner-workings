package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextPhoneStatusMonitor extends SContextEventContext {
    public static final Creator<SContextPhoneStatusMonitor> CREATOR = new Creator<SContextPhoneStatusMonitor>() {
        public SContextPhoneStatusMonitor createFromParcel(Parcel in) {
            return new SContextPhoneStatusMonitor(in);
        }

        public SContextPhoneStatusMonitor[] newArray(int size) {
            return new SContextPhoneStatusMonitor[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextPhoneStatusMonitor() {
        this.mContext = new Bundle();
    }

    SContextPhoneStatusMonitor(Parcel src) {
        readFromParcel(src);
    }

    public boolean isInSuroundingEnvironment() {
        return this.mContext.getBoolean("lcdOffRecommend");
    }

    public int getProximity() {
        return this.mContext.getInt("embower");
    }

    public int getLcdDirection() {
        return this.mContext.getInt("lcddirect");
    }

    public long getTimeStamp() {
        return this.mContext.getLong("timestamp");
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
