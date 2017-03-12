package android.view;

import android.util.EventLog;

public class EventLogTags {
    public static final int CHOREOGRAPHER_FRAME_SKIP = 73200;

    private EventLogTags() {
    }

    public static void writeChoreographerFrameSkip(int pid, int time) {
        EventLog.writeEvent((int) CHOREOGRAPHER_FRAME_SKIP, Integer.valueOf(pid), Integer.valueOf(time));
    }
}
