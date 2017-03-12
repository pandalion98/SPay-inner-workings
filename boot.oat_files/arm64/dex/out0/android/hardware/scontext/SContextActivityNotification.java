package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextActivityNotification extends SContextEventContext {
    public static final Creator<SContextActivityNotification> CREATOR = new Creator<SContextActivityNotification>() {
        public SContextActivityNotification createFromParcel(Parcel in) {
            return new SContextActivityNotification(in);
        }

        public SContextActivityNotification[] newArray(int size) {
            return new SContextActivityNotification[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextActivityNotification() {
        this.mContext = new Bundle();
    }

    SContextActivityNotification(Parcel src) {
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
