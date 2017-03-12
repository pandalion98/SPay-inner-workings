package android.hardware.scontext;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable.Creator;

public class SContextActivityLocationLogging extends SContextEventContext {
    public static final Creator<SContextActivityLocationLogging> CREATOR = new Creator<SContextActivityLocationLogging>() {
        public SContextActivityLocationLogging createFromParcel(Parcel in) {
            return new SContextActivityLocationLogging(in);
        }

        public SContextActivityLocationLogging[] newArray(int size) {
            return new SContextActivityLocationLogging[size];
        }
    };
    private Bundle mContext;
    private Bundle mInfo;
    private int mType;

    public /* bridge */ /* synthetic */ int describeContents() {
        return super.describeContents();
    }

    SContextActivityLocationLogging() {
        this.mContext = new Bundle();
        this.mInfo = new Bundle();
    }

    SContextActivityLocationLogging(Parcel src) {
        readFromParcel(src);
    }

    public int getType() {
        return this.mType;
    }

    public int getLoggingSize() {
        if (this.mType == 1) {
            return this.mInfo.getInt("StayingAreaCount");
        }
        if (this.mType == 2) {
            return this.mInfo.getInt("MovingCount");
        }
        if (this.mType == 3) {
            return this.mInfo.getInt("TrajectoryCount");
        }
        return 0;
    }

    public long[] getTimestamp() {
        if (this.mType == 1) {
            return this.mInfo.getLongArray("StayingAreaTimeStamp");
        }
        if (this.mType == 2) {
            int[] duration = this.mInfo.getIntArray("MovingTimeDuration");
            long[] timestamp = new long[duration.length];
            for (int i = 0; i < duration.length; i++) {
                if (i == 0) {
                    timestamp[i] = this.mInfo.getLong("MovingTimeStamp");
                } else {
                    timestamp[i] = timestamp[i - 1] + ((long) duration[i - 1]);
                }
            }
            return timestamp;
        } else if (this.mType == 3) {
            return this.mInfo.getLongArray("TrajectoryTimeStamp");
        } else {
            return null;
        }
    }

    public double[] getLatitude() {
        if (this.mType == 1) {
            return this.mInfo.getDoubleArray("StayingAreaLatitude");
        }
        if (this.mType == 3) {
            return this.mInfo.getDoubleArray("TrajectoryLatitude");
        }
        return null;
    }

    public double[] getLongitude() {
        if (this.mType == 1) {
            return this.mInfo.getDoubleArray("StayingAreaLongitude");
        }
        if (this.mType == 3) {
            return this.mInfo.getDoubleArray("TrajectoryLongitude");
        }
        return null;
    }

    public double[] getAltitude() {
        if (this.mType == 1) {
            return this.mInfo.getDoubleArray("StayingAreaAltitude");
        }
        if (this.mType == 3) {
            return this.mInfo.getDoubleArray("TrajectoryAltitude");
        }
        return null;
    }

    public int[] getStayingTimeDuration() {
        return this.mInfo.getIntArray("StayingAreaTimeDuration");
    }

    public int[] getStayingAreaRadius() {
        return this.mInfo.getIntArray("StayingAreaRadius");
    }

    public int[] getStayingAreaStatus() {
        return this.mInfo.getIntArray("StayingAreaStatus");
    }

    void setValues(Bundle context) {
        this.mContext = context;
        this.mInfo = context.getBundle("LoggingBundle");
        this.mType = this.mContext.getInt("LoggingType");
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeBundle(this.mContext);
        dest.writeBundle(this.mInfo);
        dest.writeInt(this.mType);
    }

    private void readFromParcel(Parcel src) {
        this.mContext = src.readBundle();
        this.mInfo = src.readBundle();
        this.mType = src.readInt();
    }
}
