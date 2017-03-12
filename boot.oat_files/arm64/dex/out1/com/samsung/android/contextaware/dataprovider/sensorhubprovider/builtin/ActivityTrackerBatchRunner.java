package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.utils.ActivityHistory;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.handler.builtin.ActivityTrackerProvider;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ActivityTrackerBatchRunner extends ActivityTrackerProvider {
    private static long CHECK_PERIOD = 20000;
    private static int DEFAULT_ACTIVITY_RECORDING_PERIOD = 1800000;
    private static final int DEFAULT_BATCHING_PERIOD = 1200;
    private static final int MSG_TIMER_EXPIRED = 65261;
    private static final int mBatchingPeriod = 1200;
    private final ActivityHistory mActivityHistory = ActivityHistory.getInstance();
    private Handler mHandler;
    private boolean mHistoryDataReq = false;
    private final ArrayList<ActivityInfo> mListActivityInfo = new ArrayList();
    private final Lock mMutex = new ReentrantLock(true);

    private static class ActivityInfo {
        int accuracy;
        int activityType;
        long duration;
        long timestamp;

        private ActivityInfo() {
        }
    }

    public ActivityTrackerBatchRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
        createHandler(looper);
    }

    private void sendHistoryData() {
        CaLogger.warning("start");
        String[] names = getContextValueNames();
        int loggingCount = this.mActivityHistory.getBufferSize();
        if (loggingCount == 0) {
            CaLogger.error("History Data Buffer is null!!");
        }
        int[] activityType = new int[loggingCount];
        int[] accuracy = new int[loggingCount];
        long[] duration = new long[loggingCount];
        long[] timestamp = new long[loggingCount];
        for (int i = 0; i < loggingCount; i++) {
            activityType[i] = this.mActivityHistory.getActivityType(i);
            accuracy[i] = this.mActivityHistory.getActivityAccuracy(i);
            duration[i] = this.mActivityHistory.getActivityDuration(i);
            timestamp[i] = this.mActivityHistory.getActivityTimestamp(i);
        }
        getContextBean().putContext(names[0], (short) getModeType());
        getContextBean().putContext(names[2], loggingCount);
        getContextBean().putContext(names[3], activityType);
        getContextBean().putContext(names[4], accuracy);
        getContextBean().putContext(names[5], duration);
        getContextBean().putContext(names[6], getMostActivity());
        getContextBean().putContext(names[7], timestamp);
        getContextBean().putContext(names[8], 1);
        notifyObserver();
        CaLogger.warning("end");
    }

    protected final int getBatchingPeriod() {
        return 1200;
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_ACTIVITY_TRACKER_BATCH.getCode();
    }

    protected final byte getModeType() {
        return (byte) 2;
    }

    public String[] getContextValueNames() {
        return new String[]{"OperationMode", "TimeStamp", "Count", "ActivityType", "Accuracy", "Duration", "MostActivity", "TimeStampArray", "HistoryMode"};
    }

    public final <E> boolean setPropertyValue(int property, E e) {
        if (property != 3) {
            return false;
        }
        CaLogger.info("History Data");
        sendCmdToSensorHub(ISensorHubCmdProtocol.INST_LIB_GETVALUE, getInstLibType(), new byte[]{(byte) 2, (byte) 0});
        this.mHandler.sendEmptyMessageDelayed(MSG_TIMER_EXPIRED, 500);
        this.mHistoryDataReq = true;
        return true;
    }

    public int parse(byte[] packet, int next) {
        int tmpNext = next;
        String[] names = getContextValueNames();
        CaLogger.warning("parse start");
        if ((packet.length - tmpNext) - 4 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        timeTemp = new byte[4];
        int tmpNext2 = tmpNext + 1;
        timeTemp[0] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        timeTemp[1] = packet[tmpNext2];
        tmpNext2 = tmpNext + 1;
        timeTemp[2] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        timeTemp[3] = packet[tmpNext2];
        long timeStamp = CaTimeManager.getInstance().getTimeStampForUTC(byteArrayToLong(timeTemp));
        if ((packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        tmpNext2 = tmpNext + 1;
        int totalCnt = packet[tmpNext];
        if (totalCnt <= 0) {
            CaLogger.error(SensorHubErrors.ERROR_BATCH_DATA_COUNT.getMessage());
            tmpNext = tmpNext2;
            return -1;
        }
        int[] activityType = new int[totalCnt];
        int[] accuracy = new int[totalCnt];
        long[] duration = new long[totalCnt];
        int i = 0;
        while (i < totalCnt) {
            if ((packet.length - tmpNext2) - 5 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                tmpNext = tmpNext2;
                return -1;
            }
            tmpNext = tmpNext2 + 1;
            activityType[i] = packet[tmpNext2];
            tmpNext2 = tmpNext + 1;
            accuracy[i] = packet[tmpNext];
            tmpNext = tmpNext2 + 1;
            tmpNext2 = tmpNext + 1;
            tmpNext = tmpNext2 + 1;
            duration[i] = (long) ((((packet[tmpNext2] & 255) << 16) + ((packet[tmpNext] & 255) << 8)) + (packet[tmpNext2] & 255));
            i++;
            tmpNext2 = tmpNext;
        }
        this.mMutex.lock();
        try {
            updateActivityInfo(timeStamp, activityType, duration, accuracy, totalCnt);
            this.mActivityHistory.putActivityData(timeStamp, activityType, accuracy, duration);
            getContextBean().putContext(names[0], (short) getModeType());
            getContextBean().putContext(names[1], timeStamp);
            getContextBean().putContext(names[2], totalCnt);
            getContextBean().putContext(names[3], activityType);
            getContextBean().putContext(names[4], accuracy);
            getContextBean().putContext(names[5], duration);
            getContextBean().putContext(names[6], getMostActivity());
            notifyObserver();
            if (this.mHistoryDataReq) {
                this.mHandler.removeMessages(MSG_TIMER_EXPIRED);
                this.mHistoryDataReq = false;
                sendHistoryData();
            }
            this.mMutex.unlock();
            CaLogger.warning("parse end");
            tmpNext = tmpNext2;
            return tmpNext2;
        } catch (Throwable th) {
            this.mMutex.unlock();
        }
    }

    private int getMostActivity() {
        int size = this.mListActivityInfo.size();
        CaLogger.info("size:" + size);
        if (size <= 0) {
            return 1;
        }
        if (((ActivityInfo) this.mListActivityInfo.get(size - 1)).duration > CHECK_PERIOD) {
            return ((ActivityInfo) this.mListActivityInfo.get(size - 1)).activityType;
        }
        long sumDuration = 0;
        int i = size - 1;
        float maxWeight = 0.0f;
        int k = 1;
        int actIndex = size - 1;
        while (sumDuration <= 30000 && i >= 0) {
            if (((ActivityInfo) this.mListActivityInfo.get(i)).duration < 3000) {
                i--;
            } else {
                float weight = ((float) ((ActivityInfo) this.mListActivityInfo.get(i)).duration) / ((float) k);
                if (maxWeight < weight) {
                    maxWeight = weight;
                    actIndex = i;
                    k++;
                }
                sumDuration += ((ActivityInfo) this.mListActivityInfo.get(i)).duration;
                i--;
            }
        }
        return ((ActivityInfo) this.mListActivityInfo.get(actIndex)).activityType;
    }

    private void updateActivityInfo(long timeStamp, int[] activityType, long[] duration, int[] accuracy, int totalCnt) {
        int i = 0;
        while (i < totalCnt) {
            if (!(activityType[i] == 0 || accuracy[i] == 0)) {
                int size = this.mListActivityInfo.size();
                if (size == 0 || activityType[i] != ((ActivityInfo) this.mListActivityInfo.get(size - 1)).activityType) {
                    ActivityInfo info = new ActivityInfo();
                    info.activityType = activityType[i];
                    if (info.activityType == 4) {
                        info.activityType = 1;
                    }
                    info.duration = duration[i];
                    info.accuracy = accuracy[i];
                    info.timestamp = timeStamp;
                    timeStamp += duration[i];
                    this.mListActivityInfo.add(info);
                } else {
                    ActivityInfo activityInfo = (ActivityInfo) this.mListActivityInfo.get(size - 1);
                    activityInfo.duration += duration[i];
                }
            }
            i++;
        }
        long startTime = System.currentTimeMillis() - ((long) DEFAULT_ACTIVITY_RECORDING_PERIOD);
        i = 0;
        while (i < this.mListActivityInfo.size() && ((ActivityInfo) this.mListActivityInfo.get(i)).timestamp < startTime) {
            i++;
        }
        for (int j = 0; j < i; j++) {
            this.mListActivityInfo.remove(0);
        }
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    public final void disable() {
        CaLogger.trace();
        this.mHistoryDataReq = false;
        this.mHandler.removeMessages(MSG_TIMER_EXPIRED);
        super.disable();
    }

    public final void clear() {
        CaLogger.trace();
        this.mMutex.lock();
        try {
            this.mListActivityInfo.clear();
            this.mActivityHistory.erase();
            super.clear();
        } finally {
            this.mMutex.unlock();
        }
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    protected final ITimeChangeObserver getTimeChangeObserver() {
        return this;
    }

    public final Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    private void createHandler(Looper looper) {
        this.mHandler = new Handler(looper) {
            public void handleMessage(Message msg) {
                if (msg.what == ActivityTrackerBatchRunner.MSG_TIMER_EXPIRED) {
                    ActivityTrackerBatchRunner.this.mHistoryDataReq = false;
                    ActivityTrackerBatchRunner.this.mMutex.lock();
                    try {
                        ActivityTrackerBatchRunner.this.sendHistoryData();
                    } finally {
                        ActivityTrackerBatchRunner.this.mMutex.unlock();
                    }
                }
            }
        };
    }
}
