package android.app.usage;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class UsageStats implements Parcelable {
    public static final Creator<UsageStats> CREATOR = new Creator<UsageStats>() {
        public UsageStats createFromParcel(Parcel in) {
            UsageStats stats = new UsageStats();
            stats.mPackageName = in.readString();
            stats.mBeginTimeStamp = in.readLong();
            stats.mEndTimeStamp = in.readLong();
            stats.mLastTimeUsed = in.readLong();
            stats.mTotalTimeInForeground = in.readLong();
            stats.mLaunchCount = in.readInt();
            stats.mLastEvent = in.readInt();
            stats.mBeginIdleTime = in.readLong();
            stats.mLastTimeSystemUsed = in.readLong();
            return stats;
        }

        public UsageStats[] newArray(int size) {
            return new UsageStats[size];
        }
    };
    public long mBeginIdleTime;
    public long mBeginTimeStamp;
    public long mEndTimeStamp;
    public int mLastEvent;
    public long mLastTimeSystemUsed;
    public long mLastTimeUsed;
    public int mLaunchCount;
    public String mPackageName;
    public long mTotalTimeInForeground;

    public UsageStats(UsageStats stats) {
        this.mPackageName = stats.mPackageName;
        this.mBeginTimeStamp = stats.mBeginTimeStamp;
        this.mEndTimeStamp = stats.mEndTimeStamp;
        this.mLastTimeUsed = stats.mLastTimeUsed;
        this.mTotalTimeInForeground = stats.mTotalTimeInForeground;
        this.mLaunchCount = stats.mLaunchCount;
        this.mLastEvent = stats.mLastEvent;
        this.mBeginIdleTime = stats.mBeginIdleTime;
        this.mLastTimeSystemUsed = stats.mLastTimeSystemUsed;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public long getFirstTimeStamp() {
        return this.mBeginTimeStamp;
    }

    public long getLastTimeStamp() {
        return this.mEndTimeStamp;
    }

    public long getLastTimeUsed() {
        return this.mLastTimeUsed;
    }

    public long getLastTimeSystemUsed() {
        return this.mLastTimeSystemUsed;
    }

    public long getBeginIdleTime() {
        return this.mBeginIdleTime;
    }

    public long getTotalTimeInForeground() {
        return this.mTotalTimeInForeground;
    }

    public void add(UsageStats right) {
        if (this.mPackageName.equals(right.mPackageName)) {
            if (right.mEndTimeStamp > this.mEndTimeStamp) {
                this.mLastEvent = right.mLastEvent;
                this.mEndTimeStamp = right.mEndTimeStamp;
                this.mLastTimeUsed = right.mLastTimeUsed;
                this.mBeginIdleTime = right.mBeginIdleTime;
                this.mLastTimeSystemUsed = right.mLastTimeSystemUsed;
            }
            this.mBeginTimeStamp = Math.min(this.mBeginTimeStamp, right.mBeginTimeStamp);
            this.mTotalTimeInForeground += right.mTotalTimeInForeground;
            this.mLaunchCount += right.mLaunchCount;
            return;
        }
        throw new IllegalArgumentException("Can't merge UsageStats for package '" + this.mPackageName + "' with UsageStats for package '" + right.mPackageName + "'.");
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPackageName);
        dest.writeLong(this.mBeginTimeStamp);
        dest.writeLong(this.mEndTimeStamp);
        dest.writeLong(this.mLastTimeUsed);
        dest.writeLong(this.mTotalTimeInForeground);
        dest.writeInt(this.mLaunchCount);
        dest.writeInt(this.mLastEvent);
        dest.writeLong(this.mBeginIdleTime);
        dest.writeLong(this.mLastTimeSystemUsed);
    }
}
