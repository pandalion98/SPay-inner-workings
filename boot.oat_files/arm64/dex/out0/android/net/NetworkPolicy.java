package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.util.Objects;

public class NetworkPolicy implements Parcelable, Comparable<NetworkPolicy> {
    public static final Creator<NetworkPolicy> CREATOR = new Creator<NetworkPolicy>() {
        public NetworkPolicy createFromParcel(Parcel in) {
            return new NetworkPolicy(in);
        }

        public NetworkPolicy[] newArray(int size) {
            return new NetworkPolicy[size];
        }
    };
    public static final int CYCLE_NONE = -1;
    private static final long DEFAULT_MTU = 1500;
    public static final long LIMIT_DISABLED = -1;
    public static final long SNOOZE_NEVER = -1;
    public static final long WARNING_DISABLED = -1;
    public int cycleDay;
    public String cycleTimezone;
    public boolean inferred;
    public long lastLimitSnooze;
    public long lastWarningSnooze;
    public long limitBytes;
    public boolean metered;
    public NetworkTemplate template;
    public long warningBytes;

    @Deprecated
    public NetworkPolicy(NetworkTemplate template, int cycleDay, String cycleTimezone, long warningBytes, long limitBytes, boolean metered) {
        this(template, cycleDay, cycleTimezone, warningBytes, limitBytes, -1, -1, metered, false);
    }

    public NetworkPolicy(NetworkTemplate template, int cycleDay, String cycleTimezone, long warningBytes, long limitBytes, long lastWarningSnooze, long lastLimitSnooze, boolean metered, boolean inferred) {
        this.template = (NetworkTemplate) Preconditions.checkNotNull(template, "missing NetworkTemplate");
        this.cycleDay = cycleDay;
        this.cycleTimezone = (String) Preconditions.checkNotNull(cycleTimezone, "missing cycleTimezone");
        this.warningBytes = warningBytes;
        this.limitBytes = limitBytes;
        this.lastWarningSnooze = lastWarningSnooze;
        this.lastLimitSnooze = lastLimitSnooze;
        this.metered = metered;
        this.inferred = inferred;
    }

    public NetworkPolicy(Parcel in) {
        boolean z;
        boolean z2 = true;
        this.template = (NetworkTemplate) in.readParcelable(null);
        this.cycleDay = in.readInt();
        this.cycleTimezone = in.readString();
        this.warningBytes = in.readLong();
        this.limitBytes = in.readLong();
        this.lastWarningSnooze = in.readLong();
        this.lastLimitSnooze = in.readLong();
        if (in.readInt() != 0) {
            z = true;
        } else {
            z = false;
        }
        this.metered = z;
        if (in.readInt() == 0) {
            z2 = false;
        }
        this.inferred = z2;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        int i2 = 1;
        dest.writeParcelable(this.template, flags);
        dest.writeInt(this.cycleDay);
        dest.writeString(this.cycleTimezone);
        dest.writeLong(this.warningBytes);
        dest.writeLong(this.limitBytes);
        dest.writeLong(this.lastWarningSnooze);
        dest.writeLong(this.lastLimitSnooze);
        if (this.metered) {
            i = 1;
        } else {
            i = 0;
        }
        dest.writeInt(i);
        if (!this.inferred) {
            i2 = 0;
        }
        dest.writeInt(i2);
    }

    public int describeContents() {
        return 0;
    }

    public boolean isOverWarning(long totalBytes) {
        return this.warningBytes != -1 && totalBytes >= this.warningBytes;
    }

    public boolean isOverLimit(long totalBytes) {
        return this.limitBytes != -1 && totalBytes + 3000 >= this.limitBytes;
    }

    public void clearSnooze() {
        this.lastWarningSnooze = -1;
        this.lastLimitSnooze = -1;
    }

    public boolean hasCycle() {
        return this.cycleDay != -1;
    }

    public int compareTo(NetworkPolicy another) {
        if (another == null || another.limitBytes == -1) {
            return -1;
        }
        if (this.limitBytes == -1 || another.limitBytes < this.limitBytes) {
            return 1;
        }
        return 0;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.template, Integer.valueOf(this.cycleDay), this.cycleTimezone, Long.valueOf(this.warningBytes), Long.valueOf(this.limitBytes), Long.valueOf(this.lastWarningSnooze), Long.valueOf(this.lastLimitSnooze), Boolean.valueOf(this.metered), Boolean.valueOf(this.inferred)});
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NetworkPolicy)) {
            return false;
        }
        NetworkPolicy other = (NetworkPolicy) obj;
        if (this.cycleDay == other.cycleDay && this.warningBytes == other.warningBytes && this.limitBytes == other.limitBytes && this.lastWarningSnooze == other.lastWarningSnooze && this.lastLimitSnooze == other.lastLimitSnooze && this.metered == other.metered && this.inferred == other.inferred && Objects.equals(this.cycleTimezone, other.cycleTimezone) && Objects.equals(this.template, other.template)) {
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("NetworkPolicy");
        builder.append("[").append(this.template).append("]:");
        builder.append(" cycleDay=").append(this.cycleDay);
        builder.append(", cycleTimezone=").append(this.cycleTimezone);
        builder.append(", warningBytes=").append(this.warningBytes);
        builder.append(", limitBytes=").append(this.limitBytes);
        builder.append(", lastWarningSnooze=").append(this.lastWarningSnooze);
        builder.append(", lastLimitSnooze=").append(this.lastLimitSnooze);
        builder.append(", metered=").append(this.metered);
        builder.append(", inferred=").append(this.inferred);
        return builder.toString();
    }
}
