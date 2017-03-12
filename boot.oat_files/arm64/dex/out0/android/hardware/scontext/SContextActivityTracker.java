package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextActivityTracker extends SContextEventContext {
    public static final Creator<SContextActivityTracker> CREATOR = new Creator<SContextActivityTracker>() {
        public SContextActivityTracker createFromParcel(Parcel in) {
            return new SContextActivityTracker(in);
        }

        public SContextActivityTracker[] newArray(int size) {
            return new SContextActivityTracker[size];
        }
    };
    private Bundle mContext;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextActivityTracker() {
        this.mContext = new Bundle();
    }

    SContextActivityTracker(Parcel src) {
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
