package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextActivityBatch extends SContextEventContext {
    public static final Creator<SContextActivityBatch> CREATOR = new Creator<SContextActivityBatch>() {
        public SContextActivityBatch createFromParcel(Parcel in) {
            return new SContextActivityBatch(in);
        }

        public SContextActivityBatch[] newArray(int size) {
            return new SContextActivityBatch[size];
        }
    };
    private Bundle mContext;
    private int mMode;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextActivityBatch() {
        this.mContext = new Bundle();
        this.mMode = 0;
    }

    SContextActivityBatch(Parcel src) {
        readFromParcel(src);
    }

    public long[] getTimeStamp() {
        if (this.mMode == 0) {
            int size = this.mContext.getInt("Count");
            long[] duration = this.mContext.getLongArray("Duration");
            long[] timestamp = new long[size];
            for (int i = 0; i < size; i++) {
                if (i == 0) {
                    timestamp[i] = this.mContext.getLong("TimeStamp");
                } else {
                    timestamp[i] = timestamp[i - 1] + duration[i - 1];
                }
            }
            return timestamp;
        } else if (this.mMode == 1) {
            return this.mContext.getLongArray("TimeStampArray");
        } else {
            return null;
        }
    }

    public int[] getStatus() {
        return this.mContext.getIntArray("ActivityType");
    }

    public int getMostActivity() {
        return this.mContext.getInt("MostActivity");
    }

    public int[] getAccuracy() {
        return this.mContext.getIntArray("Accuracy");
    }

    public int getMode() {
        return this.mMode;
    }

    void setValues(Bundle context) {
        this.mContext = context;
        this.mMode = this.mContext.getInt("Mode");
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mContext);
        dest.writeInt(this.mMode);
    }

    private void readFromParcel(Parcel src) {
        this.mContext = src.readBundle();
        this.mMode = src.readInt();
    }
}
