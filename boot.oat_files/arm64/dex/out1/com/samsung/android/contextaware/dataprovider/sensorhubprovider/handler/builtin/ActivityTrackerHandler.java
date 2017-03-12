package com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin;

import android.content.Context;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubModeHandler;
import java.util.concurrent.CopyOnWriteArrayList;

public class ActivityTrackerHandler extends SensorHubModeHandler {
    private static volatile ActivityTrackerHandler instance;

    public static ActivityTrackerHandler getInstance() {
        if (instance == null) {
            synchronized (ActivityTrackerHandler.class) {
                if (instance == null) {
                    instance = new ActivityTrackerHandler();
                }
            }
        }
        return instance;
    }

    protected final void initialize(Context context, Looper looper) {
    }

    protected final CopyOnWriteArrayList<String> getModeList() {
        CopyOnWriteArrayList<String> list = new CopyOnWriteArrayList();
        list.add(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER.getCode());
        list.add(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_INTERRUPT.getCode());
        list.add(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_BATCH.getCode());
        list.add(ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_EXTANDED_INTERRUPT.getCode());
        return list;
    }

    protected final void enable() {
    }

    protected final void disable() {
        if (!isRunning()) {
        }
    }
}
