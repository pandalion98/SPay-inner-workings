package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.utils;

import com.samsung.android.contextaware.utilbundle.CaTimeChangeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class ActivityHistory implements ITimeChangeObserver {
    private static final int MAX_BUFFER_SIZE = 1440;
    private static volatile ActivityHistory mActivityHistory;
    private int mBufferIndex;
    private int mBufferSize;
    private final HistoryData[] mHistoryData = new HistoryData[MAX_BUFFER_SIZE];

    private static class HistoryData {
        private int accuracy;
        private int activityType;
        private long duration;
        private long timeStamp;

        private HistoryData() {
            this.timeStamp = 0;
            this.activityType = 0;
            this.accuracy = 0;
            this.duration = 0;
        }
    }

    public static ActivityHistory getInstance() {
        if (mActivityHistory == null) {
            synchronized (ActivityHistory.class) {
                if (mActivityHistory == null) {
                    mActivityHistory = new ActivityHistory();
                }
            }
        }
        return mActivityHistory;
    }

    private ActivityHistory() {
        for (int i = 0; i < MAX_BUFFER_SIZE; i++) {
            this.mHistoryData[i] = new HistoryData();
        }
        initialize();
        CaTimeChangeManager.getInstance().registerObserver(this);
    }

    private void initialize() {
        this.mBufferIndex = -1;
        this.mBufferSize = 0;
    }

    public void putActivityData(long timestamp, int[] activityType, int[] accuracy, long[] duration) {
        for (int i = 0; i < activityType.length; i++) {
            if (duration[i] != 0) {
                this.mBufferIndex = (this.mBufferIndex + 1) % MAX_BUFFER_SIZE;
                if (this.mBufferSize < MAX_BUFFER_SIZE) {
                    this.mBufferSize++;
                }
                this.mHistoryData[this.mBufferIndex].timeStamp = timestamp;
                timestamp += duration[i];
                this.mHistoryData[this.mBufferIndex].activityType = activityType[i];
                this.mHistoryData[this.mBufferIndex].accuracy = accuracy[i];
                this.mHistoryData[this.mBufferIndex].duration = duration[i];
            }
        }
    }

    public long getActivityTimestamp(int index) {
        if (this.mBufferSize >= MAX_BUFFER_SIZE) {
            return this.mHistoryData[((this.mBufferIndex + 1) + index) % MAX_BUFFER_SIZE].timeStamp;
        }
        return this.mHistoryData[index].timeStamp;
    }

    public int getActivityType(int index) {
        if (this.mBufferSize >= MAX_BUFFER_SIZE) {
            return this.mHistoryData[((this.mBufferIndex + 1) + index) % MAX_BUFFER_SIZE].activityType;
        }
        return this.mHistoryData[index].activityType;
    }

    public int getActivityAccuracy(int index) {
        if (this.mBufferSize >= MAX_BUFFER_SIZE) {
            return this.mHistoryData[((this.mBufferIndex + 1) + index) % MAX_BUFFER_SIZE].accuracy;
        }
        return this.mHistoryData[index].accuracy;
    }

    public long getActivityDuration(int index) {
        if (this.mBufferSize >= MAX_BUFFER_SIZE) {
            return this.mHistoryData[((this.mBufferIndex + 1) + index) % MAX_BUFFER_SIZE].duration;
        }
        return this.mHistoryData[index].duration;
    }

    public int getBufferSize() {
        return this.mBufferSize;
    }

    public void erase() {
        initialize();
        CaLogger.warning("erased");
    }

    public void onTimeChanged() {
        erase();
    }
}
