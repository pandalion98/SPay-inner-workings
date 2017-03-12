package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextActivityNotificationEx extends SContextEventContext {
    public static final Creator<SContextActivityNotificationEx> CREATOR = new Creator<SContextActivityNotificationEx>() {
        public SContextActivityNotificationEx createFromParcel(Parcel in) {
            return new SContextActivityNotificationEx(in);
        }

        public SContextActivityNotificationEx[] newArray(int size) {
            return new SContextActivityNotificationEx[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextActivityNotificationEx() {
        this.mContext = new Bundle();
    }

    SContextActivityNotificationEx(Parcel src) {
        readFromParcel(src);
    }

    public long getTimeStamp() {
        return this.mContext.getLong("TimeStamp");
    }

    public int getStatus() {
        return this.mContext.getInt("ActivityType");
    }

    public int getAccuracy() {
        return this.mContext.getInt("Accuracy");
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
