package android.app;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.WorkSource;
import android.text.TextUtils;
import java.io.IOException;
import libcore.util.ZoneInfoDB;

public class AlarmManager {
    public static final String ACTION_NEXT_ALARM_CLOCK_CHANGED = "android.app.action.NEXT_ALARM_CLOCK_CHANGED";
    public static final int ELAPSED_REALTIME = 3;
    public static final int ELAPSED_REALTIME_WAKEUP = 2;
    public static final int FLAG_ALLOW_WHILE_IDLE = 4;
    public static final int FLAG_ALLOW_WHILE_IDLE_UNRESTRICTED = 8;
    public static final int FLAG_IDLE_UNTIL = 16;
    public static final int FLAG_STANDALONE = 1;
    public static final int FLAG_WAKE_FROM_IDLE = 2;
    public static final long INTERVAL_DAY = 86400000;
    public static final long INTERVAL_FIFTEEN_MINUTES = 900000;
    public static final long INTERVAL_HALF_DAY = 43200000;
    public static final long INTERVAL_HALF_HOUR = 1800000;
    public static final long INTERVAL_HOUR = 3600000;
    public static final int RTC = 1;
    public static final int RTC_POWEROFF_WAKEUP = 5;
    public static final int RTC_WAKEUP = 0;
    public static final long WINDOW_EXACT = 0;
    public static final long WINDOW_HEURISTIC = -1;
    private final boolean mAlwaysExact;
    private final IAlarmManager mService;
    private final int mTargetSdkVersion;

    public static final class AlarmClockInfo implements Parcelable {
        public static final Creator<AlarmClockInfo> CREATOR = new Creator<AlarmClockInfo>() {
            public AlarmClockInfo createFromParcel(Parcel in) {
                return new AlarmClockInfo(in);
            }

            public AlarmClockInfo[] newArray(int size) {
                return new AlarmClockInfo[size];
            }
        };
        private final PendingIntent mShowIntent;
        private final long mTriggerTime;

        public AlarmClockInfo(long triggerTime, PendingIntent showIntent) {
            this.mTriggerTime = triggerTime;
            this.mShowIntent = showIntent;
        }

        AlarmClockInfo(Parcel in) {
            this.mTriggerTime = in.readLong();
            this.mShowIntent = (PendingIntent) in.readParcelable(PendingIntent.class.getClassLoader());
        }

        public long getTriggerTime() {
            return this.mTriggerTime;
        }

        public PendingIntent getShowIntent() {
            return this.mShowIntent;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.mTriggerTime);
            dest.writeParcelable(this.mShowIntent, flags);
        }
    }

    AlarmManager(IAlarmManager service, Context ctx) {
        this.mService = service;
        this.mTargetSdkVersion = ctx.getApplicationInfo().targetSdkVersion;
        this.mAlwaysExact = this.mTargetSdkVersion < 19;
    }

    private long legacyExactLength() {
        return this.mAlwaysExact ? 0 : -1;
    }

    public void set(int type, long triggerAtMillis, PendingIntent operation) {
        setImpl(type, triggerAtMillis, legacyExactLength(), 0, 0, operation, null, null);
    }

    public void setAutoPowerUp(String time) {
        try {
            this.mService.setAutoPowerUp(time);
        } catch (RemoteException e) {
        }
    }

    public void setRepeating(int type, long triggerAtMillis, long intervalMillis, PendingIntent operation) {
        setImpl(type, triggerAtMillis, legacyExactLength(), intervalMillis, 0, operation, null, null);
    }

    public void setWindow(int type, long windowStartMillis, long windowLengthMillis, PendingIntent operation) {
        setImpl(type, windowStartMillis, windowLengthMillis, 0, 0, operation, null, null);
    }

    public void setExact(int type, long triggerAtMillis, PendingIntent operation) {
        setImpl(type, triggerAtMillis, 0, 0, 0, operation, null, null);
    }

    public void setIdleUntil(int type, long triggerAtMillis, PendingIntent operation) {
        setImpl(type, triggerAtMillis, 0, 0, 16, operation, null, null);
    }

    public void setAlarmClock(AlarmClockInfo info, PendingIntent operation) {
        setImpl(0, info.getTriggerTime(), 0, 0, 0, operation, null, info);
    }

    public void set(int type, long triggerAtMillis, long windowMillis, long intervalMillis, PendingIntent operation, WorkSource workSource) {
        setImpl(type, triggerAtMillis, windowMillis, intervalMillis, 0, operation, workSource, null);
    }

    private void setImpl(int type, long triggerAtMillis, long windowMillis, long intervalMillis, int flags, PendingIntent operation, WorkSource workSource, AlarmClockInfo alarmClock) {
        if (triggerAtMillis < 0) {
            triggerAtMillis = 0;
        }
        try {
            this.mService.set(type, triggerAtMillis, windowMillis, intervalMillis, flags, operation, workSource, alarmClock);
        } catch (RemoteException e) {
        }
    }

    public void setInexactRepeating(int type, long triggerAtMillis, long intervalMillis, PendingIntent operation) {
        setImpl(type, triggerAtMillis, -1, intervalMillis, 0, operation, null, null);
    }

    public void setAndAllowWhileIdle(int type, long triggerAtMillis, PendingIntent operation) {
        setImpl(type, triggerAtMillis, -1, 0, 4, operation, null, null);
    }

    public void setExactAndAllowWhileIdle(int type, long triggerAtMillis, PendingIntent operation) {
        setImpl(type, triggerAtMillis, 0, 0, 4, operation, null, null);
    }

    public void cancel(PendingIntent operation) {
        try {
            this.mService.remove(operation);
        } catch (RemoteException e) {
        }
    }

    public void setTime(long millis) {
        try {
            this.mService.setTime(millis);
        } catch (RemoteException e) {
        }
    }

    public void setTimeZone(String timeZone) {
        if (!TextUtils.isEmpty(timeZone)) {
            if (this.mTargetSdkVersion >= 23) {
                boolean hasTimeZone = false;
                try {
                    hasTimeZone = ZoneInfoDB.getInstance().hasTimeZone(timeZone);
                } catch (IOException e) {
                }
                if (!hasTimeZone) {
                    throw new IllegalArgumentException("Timezone: " + timeZone + " is not an Olson ID");
                }
            }
            try {
                this.mService.setTimeZone(timeZone);
            } catch (RemoteException e2) {
            }
        }
    }

    public long getNextWakeFromIdleTime() {
        try {
            return this.mService.getNextWakeFromIdleTime();
        } catch (RemoteException e) {
            return Long.MAX_VALUE;
        }
    }

    public AlarmClockInfo getNextAlarmClock() {
        return getNextAlarmClock(UserHandle.myUserId());
    }

    public AlarmClockInfo getNextAlarmClock(int userId) {
        try {
            return this.mService.getNextAlarmClock(userId);
        } catch (RemoteException e) {
            return null;
        }
    }
}
