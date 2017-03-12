package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.utils;

import android.text.format.DateUtils;
import com.samsung.android.contextaware.utilbundle.CaTimeChangeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class PedoHistory implements ITimeChangeObserver {
    public static final int DATA_MODE_BATCH = 2;
    public static final int DATA_MODE_NORMAL = 1;
    private static final int LOG_BUFFER_SIZE = 1440;
    private static volatile PedoHistory mPedoHistory;
    private double mAccumulatedCalorie;
    private double mAccumulatedDistance;
    private long mAccumulatedRunDnStep;
    private long mAccumulatedRunStep;
    private long mAccumulatedRunUpStep;
    private long mAccumulatedTotalStep;
    private long mAccumulatedWalkDnStep;
    private long mAccumulatedWalkStep;
    private long mAccumulatedWalkUpStep;
    private double mAverageSpeed;
    private final double[] mCalorieArray = new double[LOG_BUFFER_SIZE];
    private final double[] mDistanceArray = new double[LOG_BUFFER_SIZE];
    private int mHistoryArrayIndex;
    private int mHistoryArraySize;
    private int mLastDataMode;
    private long mLastSavingTimestamp;
    private double mNumAccumulatedData;
    private final long[] mRunDnStepCountArray = new long[LOG_BUFFER_SIZE];
    private final long[] mRunStepCountArray = new long[LOG_BUFFER_SIZE];
    private final long[] mRunUpStepCountArray = new long[LOG_BUFFER_SIZE];
    private final double[] mSpeedArray = new double[LOG_BUFFER_SIZE];
    private final long[] mTimeStampArray = new long[LOG_BUFFER_SIZE];
    private final long[] mTotalStepCountArray = new long[LOG_BUFFER_SIZE];
    private final long[] mWalkDnStepCountArray = new long[LOG_BUFFER_SIZE];
    private final long[] mWalkStepCountArray = new long[LOG_BUFFER_SIZE];
    private final long[] mWalkUpStepCountArray = new long[LOG_BUFFER_SIZE];

    public static PedoHistory getInstance() {
        if (mPedoHistory == null) {
            synchronized (PedoHistory.class) {
                if (mPedoHistory == null) {
                    mPedoHistory = new PedoHistory();
                }
            }
        }
        return mPedoHistory;
    }

    private PedoHistory() {
        initialize();
        CaTimeChangeManager.getInstance().registerObserver(this);
    }

    private void initialize() {
        this.mHistoryArrayIndex = -1;
        this.mHistoryArraySize = 0;
        resetAccumulatedStepInfo();
        this.mLastSavingTimestamp = 0;
        this.mLastDataMode = 1;
    }

    public void erase() {
        initialize();
        CaLogger.warning("erased");
    }

    private long getLastSavingTimestamp() {
        return this.mLastSavingTimestamp;
    }

    public void putTimestamp(long curTimeStamp) {
        this.mLastSavingTimestamp = curTimeStamp;
        this.mTimeStampArray[this.mHistoryArrayIndex] = curTimeStamp;
    }

    public void putStepInfoSingle(long scTotal, long scWalk, long scRun, long scWalkUp, long scRunUp, long scWalkDn, long scRunDn, double distance, double calorie, double speed) {
        this.mAccumulatedTotalStep += scTotal;
        this.mAccumulatedWalkStep += scWalk;
        this.mAccumulatedRunStep += scRun;
        this.mAccumulatedWalkUpStep += scWalkUp;
        this.mAccumulatedRunUpStep += scRunUp;
        this.mAccumulatedWalkDnStep += scWalkDn;
        this.mAccumulatedRunDnStep += scRunDn;
        this.mAccumulatedDistance += distance;
        this.mAccumulatedCalorie += calorie;
        this.mAverageSpeed = ((this.mAverageSpeed * this.mNumAccumulatedData) + speed) / (this.mNumAccumulatedData + 1.0d);
        this.mNumAccumulatedData += 1.0d;
        this.mTotalStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedTotalStep;
        this.mWalkStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedWalkStep;
        this.mRunStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedRunStep;
        this.mWalkUpStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedWalkUpStep;
        this.mRunUpStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedRunUpStep;
        this.mWalkDnStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedWalkDnStep;
        this.mRunDnStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedRunDnStep;
        this.mDistanceArray[this.mHistoryArrayIndex] = this.mAccumulatedDistance;
        this.mCalorieArray[this.mHistoryArrayIndex] = this.mAccumulatedCalorie;
        this.mSpeedArray[this.mHistoryArrayIndex] = this.mAverageSpeed;
        this.mLastDataMode = 1;
    }

    public void putTotalStepInfoSingle(long scTotal) {
        this.mAccumulatedTotalStep += scTotal;
        this.mTotalStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedTotalStep;
    }

    public void putWalkStepInfoSingle(long scWalk) {
        this.mAccumulatedWalkStep += scWalk;
        this.mWalkStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedWalkStep;
    }

    public void putRunStepInfoSingle(long scRun) {
        this.mAccumulatedRunStep += scRun;
        this.mRunStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedRunStep;
    }

    public void putWalkUpStepInfoSingle(long scWalkUp) {
        this.mAccumulatedWalkUpStep += scWalkUp;
        this.mWalkUpStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedWalkUpStep;
    }

    public void putRunUpStepInfoSingle(long scRunUp) {
        this.mAccumulatedRunUpStep += scRunUp;
        this.mRunUpStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedRunUpStep;
    }

    public void putWalkDnStepInfoSingle(long scWalkDn) {
        this.mAccumulatedWalkDnStep += scWalkDn;
        this.mWalkDnStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedWalkDnStep;
    }

    public void putRunDnStepInfoSingle(long scRunDn) {
        this.mAccumulatedRunDnStep += scRunDn;
        this.mRunDnStepCountArray[this.mHistoryArrayIndex] = this.mAccumulatedRunDnStep;
    }

    public void putDistanceInfoSingle(double distance) {
        this.mAccumulatedDistance += distance;
        this.mDistanceArray[this.mHistoryArrayIndex] = this.mAccumulatedDistance;
    }

    public void putCalorieInfoSingle(double calorie) {
        this.mAccumulatedCalorie += calorie;
        this.mCalorieArray[this.mHistoryArrayIndex] = this.mAccumulatedCalorie;
    }

    public void putSpeedInfoSingle(double speed) {
        this.mAverageSpeed = ((this.mAverageSpeed * this.mNumAccumulatedData) + speed) / (this.mNumAccumulatedData + 1.0d);
        this.mNumAccumulatedData += 1.0d;
        this.mSpeedArray[this.mHistoryArrayIndex] = this.mAverageSpeed;
    }

    private void resetAccumulatedStepInfo() {
        this.mAccumulatedTotalStep = 0;
        this.mAccumulatedWalkStep = 0;
        this.mAccumulatedRunStep = 0;
        this.mAccumulatedWalkUpStep = 0;
        this.mAccumulatedRunUpStep = 0;
        this.mAccumulatedWalkDnStep = 0;
        this.mAccumulatedRunDnStep = 0;
        this.mAccumulatedDistance = 0.0d;
        this.mAccumulatedCalorie = 0.0d;
        this.mAverageSpeed = 0.0d;
        this.mNumAccumulatedData = 0.0d;
    }

    public void putStepInfo(long scTotal, long scWalk, long scRun, long scWalkUp, long scRunUp, long scWalkDn, long scRunDn, double distance, double calorie, double speed) {
        this.mTotalStepCountArray[this.mHistoryArrayIndex] = scTotal;
        this.mWalkStepCountArray[this.mHistoryArrayIndex] = scWalk;
        this.mRunStepCountArray[this.mHistoryArrayIndex] = scRun;
        this.mWalkUpStepCountArray[this.mHistoryArrayIndex] = scWalkUp;
        this.mRunUpStepCountArray[this.mHistoryArrayIndex] = scRunUp;
        this.mWalkDnStepCountArray[this.mHistoryArrayIndex] = scWalkDn;
        this.mRunDnStepCountArray[this.mHistoryArrayIndex] = scRunDn;
        this.mDistanceArray[this.mHistoryArrayIndex] = distance;
        this.mCalorieArray[this.mHistoryArrayIndex] = calorie;
        this.mSpeedArray[this.mHistoryArrayIndex] = speed;
        this.mLastDataMode = 2;
    }

    public void setDataMode(int dataMode) {
        this.mLastDataMode = dataMode;
    }

    public void putTotalStepInfo(long scTotal) {
        this.mTotalStepCountArray[this.mHistoryArrayIndex] = scTotal;
    }

    public void putWalkStepInfo(long scWalk) {
        this.mWalkStepCountArray[this.mHistoryArrayIndex] = scWalk;
    }

    public void putRunStepInfo(long scRun) {
        this.mRunStepCountArray[this.mHistoryArrayIndex] = scRun;
    }

    public void putWalkUpStepInfo(long scWalkUp) {
        this.mWalkUpStepCountArray[this.mHistoryArrayIndex] = scWalkUp;
    }

    public void putRunUpStepInfo(long scRunUp) {
        this.mRunUpStepCountArray[this.mHistoryArrayIndex] = scRunUp;
    }

    public void putWalkDnStepInfo(long scWalkDn) {
        this.mWalkDnStepCountArray[this.mHistoryArrayIndex] = scWalkDn;
    }

    public void putRunDnStepInfo(long scRunDn) {
        this.mRunDnStepCountArray[this.mHistoryArrayIndex] = scRunDn;
    }

    public void putDistanceInfo(double distance) {
        this.mDistanceArray[this.mHistoryArrayIndex] = distance;
    }

    public void putCalorieInfo(double calorie) {
        this.mCalorieArray[this.mHistoryArrayIndex] = calorie;
    }

    public void putSpeedInfo(double speed) {
        this.mSpeedArray[this.mHistoryArrayIndex] = speed;
    }

    private void increaseBufferIndex() {
        this.mHistoryArrayIndex++;
        if (this.mHistoryArrayIndex == LOG_BUFFER_SIZE) {
            this.mHistoryArrayIndex = 0;
        }
        if (this.mHistoryArraySize < LOG_BUFFER_SIZE) {
            this.mHistoryArraySize++;
        }
    }

    public void updateBufferIndex(int dataMode) {
        if (dataMode == 2) {
            increaseBufferIndex();
        } else if (dataMode == 1) {
            Calendar calender = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
            long curTimeMillis = calender.getTimeInMillis();
            int minute = calender.get(12);
            Calendar lastUpdatedTime = Calendar.getInstance();
            lastUpdatedTime.setTimeInMillis(getLastSavingTimestamp());
            int lastUpdatedMinute = lastUpdatedTime.get(12);
            long diffTimeStamp = curTimeMillis - getLastSavingTimestamp();
            if (getLastCallBackMode() == 2) {
                increaseBufferIndex();
                resetAccumulatedStepInfo();
                putTimestamp(curTimeMillis);
            } else if (minute != lastUpdatedMinute || diffTimeStamp > DateUtils.MINUTE_IN_MILLIS || getLatestBufferIndex() < 0) {
                increaseBufferIndex();
                resetAccumulatedStepInfo();
                putTimestamp(curTimeMillis);
            }
        }
    }

    public int getMaxBufferSize() {
        return LOG_BUFFER_SIZE;
    }

    private int getLatestBufferIndex() {
        return this.mHistoryArrayIndex;
    }

    public int getBufferSize() {
        return this.mHistoryArraySize;
    }

    public int getLastCallBackMode() {
        return this.mLastDataMode;
    }

    public long getTimeStampSingle(int index) {
        int modifiedIndex;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            modifiedIndex = (this.mHistoryArrayIndex + 1) + index;
            if (modifiedIndex >= LOG_BUFFER_SIZE) {
                modifiedIndex -= 1440;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mTimeStampArray[modifiedIndex];
    }

    public long getTotalStepCountSingle(int index) {
        int modifiedIndex;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            modifiedIndex = (this.mHistoryArrayIndex + 1) + index;
            if (modifiedIndex >= LOG_BUFFER_SIZE) {
                modifiedIndex -= 1440;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mTotalStepCountArray[modifiedIndex];
    }

    public long getWalkStepCountSingle(int index) {
        int modifiedIndex;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            modifiedIndex = (this.mHistoryArrayIndex + 1) + index;
            if (modifiedIndex >= LOG_BUFFER_SIZE) {
                modifiedIndex -= 1440;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mWalkStepCountArray[modifiedIndex];
    }

    public long getRunStepCountSingle(int index) {
        int modifiedIndex;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            modifiedIndex = (this.mHistoryArrayIndex + 1) + index;
            if (modifiedIndex >= LOG_BUFFER_SIZE) {
                modifiedIndex -= 1440;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mRunStepCountArray[modifiedIndex];
    }

    public long getWalkUpStepCountSingle(int index) {
        int modifiedIndex;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            modifiedIndex = (this.mHistoryArrayIndex + 1) + index;
            if (modifiedIndex >= LOG_BUFFER_SIZE) {
                modifiedIndex -= 1440;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mWalkUpStepCountArray[modifiedIndex];
    }

    public long getRunUpStepCountSingle(int index) {
        int modifiedIndex;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            modifiedIndex = (this.mHistoryArrayIndex + 1) + index;
            if (modifiedIndex >= LOG_BUFFER_SIZE) {
                modifiedIndex -= 1440;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mRunUpStepCountArray[modifiedIndex];
    }

    public long getWalkDnStepCountSingle(int index) {
        int modifiedIndex;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            modifiedIndex = (this.mHistoryArrayIndex + 1) + index;
            if (modifiedIndex >= LOG_BUFFER_SIZE) {
                modifiedIndex -= 1440;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mWalkDnStepCountArray[modifiedIndex];
    }

    public long getRunDnStepCountSingle(int index) {
        int modifiedIndex;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            modifiedIndex = (this.mHistoryArrayIndex + 1) + index;
            if (modifiedIndex >= LOG_BUFFER_SIZE) {
                modifiedIndex -= 1440;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mRunDnStepCountArray[modifiedIndex];
    }

    public double getDistanceArraySingle(int index) {
        int modifiedIndex;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            modifiedIndex = (this.mHistoryArrayIndex + 1) + index;
            if (modifiedIndex >= LOG_BUFFER_SIZE) {
                modifiedIndex -= 1440;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mDistanceArray[modifiedIndex];
    }

    public double getCalorieArraySingle(int index) {
        int modifiedIndex;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            modifiedIndex = (this.mHistoryArrayIndex + 1) + index;
            if (modifiedIndex >= LOG_BUFFER_SIZE) {
                modifiedIndex -= 1440;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mCalorieArray[modifiedIndex];
    }

    public double getSpeedArraySingle(int index) {
        int modifiedIndex;
        if (this.mHistoryArraySize >= LOG_BUFFER_SIZE) {
            modifiedIndex = (this.mHistoryArrayIndex + 1) + index;
            if (modifiedIndex >= LOG_BUFFER_SIZE) {
                modifiedIndex -= 1440;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mSpeedArray[modifiedIndex];
    }

    public void onTimeChanged() {
        erase();
    }
}
