package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.utils;

import com.samsung.android.contextaware.utilbundle.CaTimeChangeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;

public class SLMHistory implements ITimeChangeObserver {
    private static final int MAX_BUFFER_SIZE = 15;
    private static volatile SLMHistory mSLMHistory;
    private double mAccumulatedCalorie;
    private double mAccumulatedDistance;
    private int mAccumulatedDuration;
    private int mAccumulatedStepCount;
    private int mBufferIndex;
    private int mBufferSize;
    private double[] mCalorieArray = new double[15];
    private double[] mDistanceArray = new double[15];
    private int[] mDurationArray = new int[15];
    private int mLastStepType;
    private int[] mStepCountArray = new int[15];
    private int[] mStepTypeArray = new int[15];
    private long[] mTimeStampArray = new long[15];

    public static SLMHistory getInstance() {
        if (mSLMHistory == null) {
            synchronized (SLMHistory.class) {
                if (mSLMHistory == null) {
                    mSLMHistory = new SLMHistory();
                }
            }
        }
        return mSLMHistory;
    }

    private SLMHistory() {
        initialize();
        CaTimeChangeManager.getInstance().registerObserver(this);
    }

    private void initialize() {
        this.mBufferIndex = -1;
        this.mBufferSize = 0;
        this.mLastStepType = 0;
        resetAccumulatedStepInfo();
    }

    public void erase() {
        initialize();
    }

    public void resetAccumulatedStepInfo() {
        this.mAccumulatedStepCount = 0;
        this.mAccumulatedDistance = 0.0d;
        this.mAccumulatedCalorie = 0.0d;
        this.mAccumulatedDuration = 0;
    }

    public void putSLMData(long timeStamp, int stepType, int stepCount, double distance, double calorie, int duration) {
        if (this.mLastStepType != stepType) {
            this.mBufferIndex = (this.mBufferIndex + 1) % 15;
            if (this.mBufferSize < 15) {
                this.mBufferSize++;
            }
            resetAccumulatedStepInfo();
            this.mTimeStampArray[this.mBufferIndex] = timeStamp;
        }
        int[] iArr = this.mStepTypeArray;
        int i = this.mBufferIndex;
        this.mLastStepType = stepType;
        iArr[i] = stepType;
        this.mAccumulatedStepCount += stepCount;
        this.mStepCountArray[this.mBufferIndex] = this.mAccumulatedStepCount;
        this.mAccumulatedDistance += distance;
        this.mDistanceArray[this.mBufferIndex] = this.mAccumulatedDistance;
        this.mAccumulatedCalorie += calorie;
        this.mCalorieArray[this.mBufferIndex] = this.mAccumulatedCalorie;
        this.mAccumulatedDuration += duration;
        this.mDurationArray[this.mBufferIndex] = this.mAccumulatedDuration;
    }

    public int getMaxBufferSize() {
        return 15;
    }

    public int getLastStepType() {
        return this.mLastStepType;
    }

    public int getLatestBufferIndex() {
        return this.mBufferIndex;
    }

    public int getBufferSize() {
        return this.mBufferSize;
    }

    public long getTimeStampSingle(int index) {
        int modifiedIndex;
        if (this.mBufferSize >= 15) {
            modifiedIndex = (this.mBufferIndex + 1) + index;
            if (modifiedIndex >= 15) {
                modifiedIndex -= 15;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mTimeStampArray[modifiedIndex];
    }

    public int getmStepTypeArraySingle(int index) {
        int modifiedIndex;
        if (this.mBufferSize >= 15) {
            modifiedIndex = (this.mBufferIndex + 1) + index;
            if (modifiedIndex >= 15) {
                modifiedIndex -= 15;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mStepTypeArray[modifiedIndex];
    }

    public int getmStepCountArraySingle(int index) {
        int modifiedIndex;
        if (this.mBufferSize >= 15) {
            modifiedIndex = (this.mBufferIndex + 1) + index;
            if (modifiedIndex >= 15) {
                modifiedIndex -= 15;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mStepCountArray[modifiedIndex];
    }

    public int getmDurationArraySingle(int index) {
        int modifiedIndex;
        if (this.mBufferSize >= 15) {
            modifiedIndex = (this.mBufferIndex + 1) + index;
            if (modifiedIndex >= 15) {
                modifiedIndex -= 15;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mDurationArray[modifiedIndex];
    }

    public double getmDistanceArraySingle(int index) {
        int modifiedIndex;
        if (this.mBufferSize >= 15) {
            modifiedIndex = (this.mBufferIndex + 1) + index;
            if (modifiedIndex >= 15) {
                modifiedIndex -= 15;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mDistanceArray[modifiedIndex];
    }

    public double getmCalorieArraySingle(int index) {
        int modifiedIndex;
        if (this.mBufferSize >= 15) {
            modifiedIndex = (this.mBufferIndex + 1) + index;
            if (modifiedIndex >= 15) {
                modifiedIndex -= 15;
            }
        } else {
            modifiedIndex = index;
        }
        return this.mCalorieArray[modifiedIndex];
    }

    public void onTimeChanged() {
        erase();
    }
}
