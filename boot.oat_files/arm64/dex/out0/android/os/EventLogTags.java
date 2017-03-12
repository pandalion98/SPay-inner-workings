package android.os;

import android.util.EventLog;

public class EventLogTags {
    public static final int LOOPER_SLOW_LAP_TIME = 73100;

    private EventLogTags() {
    }

    public static void writeLooperSlowLapTime(String package_, int time) {
        EventLog.writeEvent(LOOPER_SLOW_LAP_TIME, new Object[]{package_, Integer.valueOf(time)});
    }
}
