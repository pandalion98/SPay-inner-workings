package com.android.internal.os;

import android.bluetooth.BluetoothActivityEnergyInfo;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkStats;
import android.net.NetworkStats.Entry;
import android.net.wifi.WifiActivityEnergyInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryStats;
import android.os.BatteryStats.DailyItem;
import android.os.BatteryStats.HistoryEventTracker;
import android.os.BatteryStats.HistoryItem;
import android.os.BatteryStats.HistoryPrinter;
import android.os.BatteryStats.HistoryStepDetails;
import android.os.BatteryStats.HistoryTag;
import android.os.BatteryStats.LevelStepTracker;
import android.os.BatteryStats.LongCounter;
import android.os.BatteryStats.PackageChange;
import android.os.BatteryStats.Uid.Pid;
import android.os.BatteryStats.Uid.Proc.ExcessivePower;
import android.os.Build;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.ParcelFormatException;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.WorkSource;
import android.telephony.DataConnectionRealTimeInfo;
import android.telephony.SignalStrength;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.LogWriter;
import android.util.MutableInt;
import android.util.Printer;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.util.TimeUtils;
import android.view.SurfaceControl;
import android.view.View;
import com.android.internal.logging.EventLogTags;
import com.android.internal.net.NetworkStatsFactory;
import com.android.internal.os.KernelUidCpuTimeReader.Callback;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.FastXmlSerializer;
import com.android.internal.util.JournaledFile;
import com.android.internal.util.XmlUtils;
import com.android.server.NetworkManagementSocketTagger;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import libcore.util.EmptyArray;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

public final class BatteryStatsImpl extends BatteryStats {
    static final int BATTERY_DELTA_LEVEL_FLAG = 1;
    public static final int BATTERY_PLUGGED_NONE = 0;
    public static final Creator<BatteryStatsImpl> CREATOR = new Creator<BatteryStatsImpl>() {
        public BatteryStatsImpl createFromParcel(Parcel in) {
            return new BatteryStatsImpl(in);
        }

        public BatteryStatsImpl[] newArray(int size) {
            return new BatteryStatsImpl[size];
        }
    };
    private static final boolean DEBUG = false;
    public static final boolean DEBUG_ENERGY = false;
    private static final boolean DEBUG_ENERGY_CPU = false;
    private static final boolean DEBUG_HISTORY = false;
    static final long DELAY_UPDATE_WAKELOCKS = 5000;
    static final int DELTA_BATTERY_LEVEL_FLAG = 524288;
    static final int DELTA_EVENT_FLAG = 8388608;
    static final int DELTA_STATE2_FLAG = 2097152;
    static final int DELTA_STATE_FLAG = 1048576;
    static final int DELTA_STATE_MASK = -16777216;
    static final int DELTA_TIME_ABS = 524285;
    static final int DELTA_TIME_INT = 524286;
    static final int DELTA_TIME_LONG = 524287;
    static final int DELTA_TIME_MASK = 524287;
    static final int DELTA_WAKELOCK_FLAG = 4194304;
    private static final int MAGIC = -1166707595;
    static final int MAX_DAILY_ITEMS = 10;
    static final int MAX_HISTORY_BUFFER = 262144;
    private static final int MAX_HISTORY_ITEMS = 2000;
    static final int MAX_LEVEL_STEPS = 200;
    static final int MAX_MAX_HISTORY_BUFFER = 327680;
    private static final int MAX_MAX_HISTORY_ITEMS = 3000;
    private static final int MAX_WAKELOCKS_PER_UID = 100;
    static final int MSG_REPORT_CHARGING = 3;
    static final int MSG_REPORT_POWER_CHANGE = 2;
    static final int MSG_UPDATE_WAKELOCKS = 1;
    private static final int NETWORK_STATS_DELTA = 2;
    private static final int NETWORK_STATS_LAST = 0;
    private static final int NETWORK_STATS_NEXT = 1;
    static final int STATE_BATTERY_HEALTH_MASK = 7;
    static final int STATE_BATTERY_HEALTH_SHIFT = 26;
    static final int STATE_BATTERY_PLUG_MASK = 3;
    static final int STATE_BATTERY_PLUG_SHIFT = 24;
    static final int STATE_BATTERY_STATUS_MASK = 7;
    static final int STATE_BATTERY_STATUS_SHIFT = 29;
    private static final String TAG = "BatteryStatsImpl";
    private static final boolean USE_OLD_HISTORY = false;
    private static final int VERSION = 132;
    private final String CHARGE_TIME_PATH;
    final HistoryEventTracker mActiveEvents;
    int mActiveHistoryStates;
    int mActiveHistoryStates2;
    int mAudioOnNesting;
    StopwatchTimer mAudioOnTimer;
    final ArrayList<StopwatchTimer> mAudioTurnedOnTimers;
    final LongSamplingCounter[] mBluetoothActivityCounters;
    private BatteryCallback mCallback;
    int mCameraOnNesting;
    StopwatchTimer mCameraOnTimer;
    final ArrayList<StopwatchTimer> mCameraTurnedOnTimers;
    int mChangedStates;
    int mChangedStates2;
    final LevelStepTracker mChargeStepTracker;
    boolean mCharging;
    public final AtomicFile mCheckinFile;
    final HistoryStepDetails mCurHistoryStepDetails;
    long mCurStepCpuSystemTime;
    long mCurStepCpuUserTime;
    int mCurStepMode;
    long mCurStepStatIOWaitTime;
    long mCurStepStatIdleTime;
    long mCurStepStatIrqTime;
    long mCurStepStatSoftIrqTime;
    long mCurStepStatSystemTime;
    long mCurStepStatUserTime;
    int mCurrentBatteryLevel;
    final LevelStepTracker mDailyChargeStepTracker;
    final LevelStepTracker mDailyDischargeStepTracker;
    public final AtomicFile mDailyFile;
    final ArrayList<DailyItem> mDailyItems;
    ArrayList<PackageChange> mDailyPackageChanges;
    long mDailyStartTime;
    boolean mDeviceIdleModeEnabled;
    StopwatchTimer mDeviceIdleModeEnabledTimer;
    boolean mDeviceIdling;
    StopwatchTimer mDeviceIdlingTimer;
    int mDischargeAmountScreenOff;
    int mDischargeAmountScreenOffSinceCharge;
    int mDischargeAmountScreenOn;
    int mDischargeAmountScreenOnSinceCharge;
    int mDischargeCurrentLevel;
    int mDischargePlugLevel;
    int mDischargeScreenOffUnplugLevel;
    int mDischargeScreenOnUnplugLevel;
    int mDischargeStartLevel;
    final LevelStepTracker mDischargeStepTracker;
    int mDischargeUnplugLevel;
    boolean mDistributeWakelockCpu;
    final ArrayList<StopwatchTimer> mDrawTimers;
    String mEndPlatformVersion;
    private final ExternalStatsSync mExternalSync;
    private boolean mFeatureComputeChargeTimeModel;
    private final JournaledFile mFile;
    int mFlashlightOnNesting;
    StopwatchTimer mFlashlightOnTimer;
    final ArrayList<StopwatchTimer> mFlashlightTurnedOnTimers;
    final ArrayList<StopwatchTimer> mFullTimers;
    final ArrayList<StopwatchTimer> mFullWifiLockTimers;
    boolean mGlobalWifiRunning;
    StopwatchTimer mGlobalWifiRunningTimer;
    int mGpsNesting;
    public final MyHandler mHandler;
    private boolean mHasBluetoothEnergyReporting;
    private boolean mHasWifiEnergyReporting;
    boolean mHaveBatteryLevel;
    int mHighDischargeAmountSinceCharge;
    HistoryItem mHistory;
    final HistoryItem mHistoryAddTmp;
    long mHistoryBaseTime;
    final Parcel mHistoryBuffer;
    int mHistoryBufferLastPos;
    HistoryItem mHistoryCache;
    final HistoryItem mHistoryCur;
    HistoryItem mHistoryEnd;
    private HistoryItem mHistoryIterator;
    HistoryItem mHistoryLastEnd;
    final HistoryItem mHistoryLastLastWritten;
    final HistoryItem mHistoryLastWritten;
    boolean mHistoryOverflow;
    final HistoryItem mHistoryReadTmp;
    final HashMap<HistoryTag, Integer> mHistoryTagPool;
    int mInitStepMode;
    private String mInitialAcquireWakeName;
    private int mInitialAcquireWakeUid;
    boolean mInteractive;
    StopwatchTimer mInteractiveTimer;
    final SparseIntArray mIsolatedUids;
    private boolean mIteratingHistory;
    private KernelCpuSpeedReader[] mKernelCpuSpeedReaders;
    private final KernelUidCpuTimeReader mKernelUidCpuTimeReader;
    private final KernelWakelockReader mKernelWakelockReader;
    private final HashMap<String, SamplingTimer> mKernelWakelockStats;
    int mLastChargeStepLevel;
    int mLastChargingStateLevel;
    int mLastDischargeStepLevel;
    long mLastHistoryElapsedRealtime;
    HistoryStepDetails mLastHistoryStepDetails;
    byte mLastHistoryStepLevel;
    final ArrayList<StopwatchTimer> mLastPartialTimers;
    long mLastStepCpuSystemTime;
    long mLastStepCpuUserTime;
    long mLastStepStatIOWaitTime;
    long mLastStepStatIdleTime;
    long mLastStepStatIrqTime;
    long mLastStepStatSoftIrqTime;
    long mLastStepStatSystemTime;
    long mLastStepStatUserTime;
    String mLastWakeupReason;
    long mLastWakeupUptimeMs;
    long mLastWriteTime;
    private int mLoadedNumConnectivityChange;
    int mLowDischargeAmountSinceCharge;
    int mMaxChargeStepLevel;
    int mMinDischargeStepLevel;
    private String[] mMobileIfaces;
    private final NetworkStats[] mMobileNetworkStats;
    LongSamplingCounter mMobileRadioActiveAdjustedTime;
    StopwatchTimer mMobileRadioActivePerAppTimer;
    long mMobileRadioActiveStartTime;
    StopwatchTimer mMobileRadioActiveTimer;
    LongSamplingCounter mMobileRadioActiveUnknownCount;
    LongSamplingCounter mMobileRadioActiveUnknownTime;
    int mMobileRadioPowerState;
    int mModStepMode;
    final LongSamplingCounter[] mNetworkByteActivityCounters;
    final LongSamplingCounter[] mNetworkPacketActivityCounters;
    private final NetworkStatsFactory mNetworkStatsFactory;
    int mNextHistoryTagIdx;
    long mNextMaxDailyDeadline;
    long mNextMinDailyDeadline;
    boolean mNoAutoReset;
    private int mNumConnectivityChange;
    int mNumHistoryItems;
    int mNumHistoryTagChars;
    boolean mOnBattery;
    boolean mOnBatteryInternal;
    final TimeBase mOnBatteryScreenOffTimeBase;
    final TimeBase mOnBatteryTimeBase;
    final ArrayList<StopwatchTimer> mPartialTimers;
    Parcel mPendingWrite;
    int mPhoneDataConnectionType;
    final StopwatchTimer[] mPhoneDataConnectionsTimer;
    boolean mPhoneOn;
    StopwatchTimer mPhoneOnTimer;
    private int mPhoneServiceState;
    private int mPhoneServiceStateRaw;
    StopwatchTimer mPhoneSignalScanningTimer;
    int mPhoneSignalStrengthBin;
    int mPhoneSignalStrengthBinRaw;
    final StopwatchTimer[] mPhoneSignalStrengthsTimer;
    private int mPhoneSimStateRaw;
    private PowerProfile mPowerProfile;
    boolean mPowerSaveModeEnabled;
    StopwatchTimer mPowerSaveModeEnabledTimer;
    int mReadHistoryChars;
    final HistoryStepDetails mReadHistoryStepDetails;
    String[] mReadHistoryStrings;
    int[] mReadHistoryUids;
    private boolean mReadOverflow;
    long mRealtime;
    long mRealtimeStart;
    public boolean mRecordAllHistory;
    boolean mRecordingHistory;
    int mScreenBrightnessBin;
    final StopwatchTimer[] mScreenBrightnessTimer;
    StopwatchTimer mScreenOnTimer;
    int mScreenState;
    int mSensorNesting;
    final SparseArray<ArrayList<StopwatchTimer>> mSensorTimers;
    boolean mShuttingDown;
    long mStartClockTime;
    int mStartCount;
    String mStartPlatformVersion;
    long mTempTotalCpuSystemTimeUs;
    long mTempTotalCpuUserTimeUs;
    final HistoryStepDetails mTmpHistoryStepDetails;
    private final Entry mTmpNetworkStatsEntry;
    private final KernelWakelockStats mTmpWakelockStats;
    long mTrackRunningHistoryElapsedRealtime;
    long mTrackRunningHistoryUptime;
    final SparseArray<Uid> mUidStats;
    private int mUnpluggedNumConnectivityChange;
    long mUptime;
    long mUptimeStart;
    int mVideoOnNesting;
    StopwatchTimer mVideoOnTimer;
    final ArrayList<StopwatchTimer> mVideoTurnedOnTimers;
    boolean mWakeLockImportant;
    int mWakeLockNesting;
    private final HashMap<String, SamplingTimer> mWakeupReasonStats;
    final LongSamplingCounter[] mWifiActivityCounters;
    final SparseArray<ArrayList<StopwatchTimer>> mWifiBatchedScanTimers;
    int mWifiFullLockNesting;
    private String[] mWifiIfaces;
    int mWifiMulticastNesting;
    final ArrayList<StopwatchTimer> mWifiMulticastTimers;
    private final NetworkStats[] mWifiNetworkStats;
    boolean mWifiOn;
    StopwatchTimer mWifiOnTimer;
    int mWifiRadioPowerState;
    final ArrayList<StopwatchTimer> mWifiRunningTimers;
    int mWifiScanNesting;
    final ArrayList<StopwatchTimer> mWifiScanTimers;
    int mWifiSignalStrengthBin;
    final StopwatchTimer[] mWifiSignalStrengthsTimer;
    int mWifiState;
    final StopwatchTimer[] mWifiStateTimer;
    int mWifiSupplState;
    final StopwatchTimer[] mWifiSupplStateTimer;
    final ArrayList<StopwatchTimer> mWindowTimers;
    final ReentrantLock mWriteLock;

    public interface TimeBaseObs {
        void onTimeStarted(long j, long j2, long j3);

        void onTimeStopped(long j, long j2, long j3);
    }

    public static abstract class Timer extends android.os.BatteryStats.Timer implements TimeBaseObs {
        int mCount;
        int mLastCount;
        long mLastTime;
        int mLoadedCount;
        long mLoadedTime;
        final TimeBase mTimeBase;
        long mTimeBeforeMark;
        long mTotalTime;
        final int mType;
        int mUnpluggedCount;
        long mUnpluggedTime;

        protected abstract int computeCurrentCountLocked();

        protected abstract long computeRunTimeLocked(long j);

        Timer(int type, TimeBase timeBase, Parcel in) {
            this.mType = type;
            this.mTimeBase = timeBase;
            this.mCount = in.readInt();
            this.mLoadedCount = in.readInt();
            this.mLastCount = 0;
            this.mUnpluggedCount = in.readInt();
            this.mTotalTime = in.readLong();
            this.mLoadedTime = in.readLong();
            this.mLastTime = 0;
            this.mUnpluggedTime = in.readLong();
            this.mTimeBeforeMark = in.readLong();
            timeBase.add(this);
        }

        Timer(int type, TimeBase timeBase) {
            this.mType = type;
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        boolean reset(boolean detachIfReset) {
            this.mTimeBeforeMark = 0;
            this.mLastTime = 0;
            this.mLoadedTime = 0;
            this.mTotalTime = 0;
            this.mLastCount = 0;
            this.mLoadedCount = 0;
            this.mCount = 0;
            if (detachIfReset) {
                detach();
            }
            return true;
        }

        void detach() {
            this.mTimeBase.remove(this);
        }

        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            out.writeInt(this.mCount);
            out.writeInt(this.mLoadedCount);
            out.writeInt(this.mUnpluggedCount);
            out.writeLong(computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs)));
            out.writeLong(this.mLoadedTime);
            out.writeLong(this.mUnpluggedTime);
            out.writeLong(this.mTimeBeforeMark);
        }

        public void onTimeStarted(long elapsedRealtime, long timeBaseUptime, long baseRealtime) {
            this.mUnpluggedTime = computeRunTimeLocked(baseRealtime);
            this.mUnpluggedCount = this.mCount;
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            this.mTotalTime = computeRunTimeLocked(baseRealtime);
            this.mCount = computeCurrentCountLocked();
        }

        public static void writeTimerToParcel(Parcel out, Timer timer, long elapsedRealtimeUs) {
            if (timer == null) {
                out.writeInt(0);
                return;
            }
            out.writeInt(1);
            timer.writeToParcel(out, elapsedRealtimeUs);
        }

        public long getTotalTimeLocked(long elapsedRealtimeUs, int which) {
            long val = computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs));
            if (which == 2) {
                return val - this.mUnpluggedTime;
            }
            if (which != 0) {
                return val - this.mLoadedTime;
            }
            return val;
        }

        public int getCountLocked(int which) {
            int val = computeCurrentCountLocked();
            if (which == 2) {
                return val - this.mUnpluggedCount;
            }
            if (which != 0) {
                return val - this.mLoadedCount;
            }
            return val;
        }

        public long getTimeSinceMarkLocked(long elapsedRealtimeUs) {
            return computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs)) - this.mTimeBeforeMark;
        }

        public void logState(Printer pw, String prefix) {
            pw.println(prefix + "mCount=" + this.mCount + " mLoadedCount=" + this.mLoadedCount + " mLastCount=" + this.mLastCount + " mUnpluggedCount=" + this.mUnpluggedCount);
            pw.println(prefix + "mTotalTime=" + this.mTotalTime + " mLoadedTime=" + this.mLoadedTime);
            pw.println(prefix + "mLastTime=" + this.mLastTime + " mUnpluggedTime=" + this.mUnpluggedTime);
        }

        void writeSummaryFromParcelLocked(Parcel out, long elapsedRealtimeUs) {
            out.writeLong(computeRunTimeLocked(this.mTimeBase.getRealtime(elapsedRealtimeUs)));
            out.writeInt(this.mCount);
        }

        void readSummaryFromParcelLocked(Parcel in) {
            long readLong = in.readLong();
            this.mLoadedTime = readLong;
            this.mTotalTime = readLong;
            this.mLastTime = 0;
            this.mUnpluggedTime = this.mTotalTime;
            int readInt = in.readInt();
            this.mLoadedCount = readInt;
            this.mCount = readInt;
            this.mLastCount = 0;
            this.mUnpluggedCount = this.mCount;
            this.mTimeBeforeMark = this.mTotalTime;
        }
    }

    public static final class BatchTimer extends Timer {
        boolean mInDischarge;
        long mLastAddedDuration;
        long mLastAddedTime;
        final Uid mUid;

        BatchTimer(Uid uid, int type, TimeBase timeBase, Parcel in) {
            super(type, timeBase, in);
            this.mUid = uid;
            this.mLastAddedTime = in.readLong();
            this.mLastAddedDuration = in.readLong();
            this.mInDischarge = timeBase.isRunning();
        }

        BatchTimer(Uid uid, int type, TimeBase timeBase) {
            super(type, timeBase);
            this.mUid = uid;
            this.mInDischarge = timeBase.isRunning();
        }

        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeLong(this.mLastAddedTime);
            out.writeLong(this.mLastAddedDuration);
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            recomputeLastDuration(SystemClock.elapsedRealtime() * 1000, false);
            this.mInDischarge = false;
            super.onTimeStopped(elapsedRealtime, baseUptime, baseRealtime);
        }

        public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
            recomputeLastDuration(elapsedRealtime, false);
            this.mInDischarge = true;
            if (this.mLastAddedTime == elapsedRealtime) {
                this.mTotalTime += this.mLastAddedDuration;
            }
            super.onTimeStarted(elapsedRealtime, baseUptime, baseRealtime);
        }

        public void logState(Printer pw, String prefix) {
            super.logState(pw, prefix);
            pw.println(prefix + "mLastAddedTime=" + this.mLastAddedTime + " mLastAddedDuration=" + this.mLastAddedDuration);
        }

        private long computeOverage(long curTime) {
            if (this.mLastAddedTime > 0) {
                return (this.mLastTime + this.mLastAddedDuration) - curTime;
            }
            return 0;
        }

        private void recomputeLastDuration(long curTime, boolean abort) {
            long overage = computeOverage(curTime);
            if (overage > 0) {
                if (this.mInDischarge) {
                    this.mTotalTime -= overage;
                }
                if (abort) {
                    this.mLastAddedTime = 0;
                    return;
                }
                this.mLastAddedTime = curTime;
                this.mLastAddedDuration -= overage;
            }
        }

        public void addDuration(BatteryStatsImpl stats, long durationMillis) {
            long now = SystemClock.elapsedRealtime() * 1000;
            recomputeLastDuration(now, true);
            this.mLastAddedTime = now;
            this.mLastAddedDuration = durationMillis * 1000;
            if (this.mInDischarge) {
                this.mTotalTime += this.mLastAddedDuration;
                this.mCount++;
            }
        }

        public void abortLastDuration(BatteryStatsImpl stats) {
            recomputeLastDuration(SystemClock.elapsedRealtime() * 1000, true);
        }

        protected int computeCurrentCountLocked() {
            return this.mCount;
        }

        protected long computeRunTimeLocked(long curBatteryRealtime) {
            long overage = computeOverage(SystemClock.elapsedRealtime() * 1000);
            if (overage <= 0) {
                return this.mTotalTime;
            }
            this.mTotalTime = overage;
            return overage;
        }

        boolean reset(boolean detachIfReset) {
            boolean stillActive;
            boolean z;
            long now = SystemClock.elapsedRealtime() * 1000;
            recomputeLastDuration(now, true);
            if (this.mLastAddedTime == now) {
                stillActive = true;
            } else {
                stillActive = false;
            }
            if (stillActive || !detachIfReset) {
                z = false;
            } else {
                z = true;
            }
            super.reset(z);
            if (stillActive) {
                return false;
            }
            return true;
        }
    }

    public interface BatteryCallback {
        void batteryNeedsCpuUpdate();

        void batteryPowerChanged(boolean z);

        void batterySendBroadcast(Intent intent);
    }

    public static class Counter extends android.os.BatteryStats.Counter implements TimeBaseObs {
        final AtomicInteger mCount = new AtomicInteger();
        int mLastCount;
        int mLoadedCount;
        int mPluggedCount;
        final TimeBase mTimeBase;
        int mUnpluggedCount;

        Counter(TimeBase timeBase, Parcel in) {
            this.mTimeBase = timeBase;
            this.mPluggedCount = in.readInt();
            this.mCount.set(this.mPluggedCount);
            this.mLoadedCount = in.readInt();
            this.mLastCount = 0;
            this.mUnpluggedCount = in.readInt();
            timeBase.add(this);
        }

        Counter(TimeBase timeBase) {
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        public void writeToParcel(Parcel out) {
            out.writeInt(this.mCount.get());
            out.writeInt(this.mLoadedCount);
            out.writeInt(this.mUnpluggedCount);
        }

        public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
            this.mUnpluggedCount = this.mPluggedCount;
            this.mCount.set(this.mPluggedCount);
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            this.mPluggedCount = this.mCount.get();
        }

        public static void writeCounterToParcel(Parcel out, Counter counter) {
            if (counter == null) {
                out.writeInt(0);
                return;
            }
            out.writeInt(1);
            counter.writeToParcel(out);
        }

        public int getCountLocked(int which) {
            int val = this.mCount.get();
            if (which == 2) {
                return val - this.mUnpluggedCount;
            }
            if (which != 0) {
                return val - this.mLoadedCount;
            }
            return val;
        }

        public void logState(Printer pw, String prefix) {
            pw.println(prefix + "mCount=" + this.mCount.get() + " mLoadedCount=" + this.mLoadedCount + " mLastCount=" + this.mLastCount + " mUnpluggedCount=" + this.mUnpluggedCount + " mPluggedCount=" + this.mPluggedCount);
        }

        void stepAtomic() {
            this.mCount.incrementAndGet();
        }

        void reset(boolean detachIfReset) {
            this.mCount.set(0);
            this.mUnpluggedCount = 0;
            this.mPluggedCount = 0;
            this.mLastCount = 0;
            this.mLoadedCount = 0;
            if (detachIfReset) {
                detach();
            }
        }

        void detach() {
            this.mTimeBase.remove(this);
        }

        void writeSummaryFromParcelLocked(Parcel out) {
            out.writeInt(this.mCount.get());
        }

        void readSummaryFromParcelLocked(Parcel in) {
            this.mLoadedCount = in.readInt();
            this.mCount.set(this.mLoadedCount);
            this.mLastCount = 0;
            int i = this.mLoadedCount;
            this.mPluggedCount = i;
            this.mUnpluggedCount = i;
        }
    }

    public interface ExternalStatsSync {
        void scheduleCpuSyncDueToRemovedUid(int i);

        void scheduleSync(String str);

        void scheduleWifiSync(String str);
    }

    public static class LongSamplingCounter extends LongCounter implements TimeBaseObs {
        long mCount;
        long mLastCount;
        long mLoadedCount;
        long mPluggedCount;
        final TimeBase mTimeBase;
        long mUnpluggedCount;

        LongSamplingCounter(TimeBase timeBase, Parcel in) {
            this.mTimeBase = timeBase;
            this.mPluggedCount = in.readLong();
            this.mCount = this.mPluggedCount;
            this.mLoadedCount = in.readLong();
            this.mLastCount = 0;
            this.mUnpluggedCount = in.readLong();
            timeBase.add(this);
        }

        LongSamplingCounter(TimeBase timeBase) {
            this.mTimeBase = timeBase;
            timeBase.add(this);
        }

        public void writeToParcel(Parcel out) {
            out.writeLong(this.mCount);
            out.writeLong(this.mLoadedCount);
            out.writeLong(this.mUnpluggedCount);
        }

        public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
            this.mUnpluggedCount = this.mPluggedCount;
            this.mCount = this.mPluggedCount;
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            this.mPluggedCount = this.mCount;
        }

        public long getCountLocked(int which) {
            long val = this.mCount;
            if (which == 2) {
                return val - this.mUnpluggedCount;
            }
            if (which != 0) {
                return val - this.mLoadedCount;
            }
            return val;
        }

        public void logState(Printer pw, String prefix) {
            pw.println(prefix + "mCount=" + this.mCount + " mLoadedCount=" + this.mLoadedCount + " mLastCount=" + this.mLastCount + " mUnpluggedCount=" + this.mUnpluggedCount + " mPluggedCount=" + this.mPluggedCount);
        }

        void addCountLocked(long count) {
            this.mCount += count;
        }

        void reset(boolean detachIfReset) {
            this.mCount = 0;
            this.mUnpluggedCount = 0;
            this.mPluggedCount = 0;
            this.mLastCount = 0;
            this.mLoadedCount = 0;
            if (detachIfReset) {
                detach();
            }
        }

        void detach() {
            this.mTimeBase.remove(this);
        }

        void writeSummaryFromParcelLocked(Parcel out) {
            out.writeLong(this.mCount);
        }

        void readSummaryFromParcelLocked(Parcel in) {
            this.mLoadedCount = in.readLong();
            this.mCount = this.mLoadedCount;
            this.mLastCount = 0;
            long j = this.mLoadedCount;
            this.mPluggedCount = j;
            this.mUnpluggedCount = j;
        }
    }

    final class MyHandler extends Handler {
        public MyHandler(Looper looper) {
            super(looper, null, true);
        }

        public void handleMessage(Message msg) {
            BatteryCallback cb = BatteryStatsImpl.this.mCallback;
            switch (msg.what) {
                case 1:
                    synchronized (BatteryStatsImpl.this) {
                        BatteryStatsImpl.this.updateCpuTimeLocked();
                    }
                    if (cb != null) {
                        cb.batteryNeedsCpuUpdate();
                        return;
                    }
                    return;
                case 2:
                    if (cb != null) {
                        cb.batteryPowerChanged(msg.arg1 != 0);
                        return;
                    }
                    return;
                case 3:
                    if (cb != null) {
                        String action;
                        synchronized (BatteryStatsImpl.this) {
                            action = BatteryStatsImpl.this.mCharging ? "android.os.action.CHARGING" : "android.os.action.DISCHARGING";
                        }
                        Intent intent = new Intent(action);
                        intent.addFlags(67108864);
                        cb.batterySendBroadcast(intent);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public abstract class OverflowArrayMap<T> {
        private static final String OVERFLOW_NAME = "*overflow*";
        ArrayMap<String, MutableInt> mActiveOverflow;
        T mCurOverflow;
        final ArrayMap<String, T> mMap = new ArrayMap();

        public abstract T instantiateObject();

        public ArrayMap<String, T> getMap() {
            return this.mMap;
        }

        public void clear() {
            this.mMap.clear();
            this.mCurOverflow = null;
            this.mActiveOverflow = null;
        }

        public void add(String name, T obj) {
            this.mMap.put(name, obj);
            if (OVERFLOW_NAME.equals(name)) {
                this.mCurOverflow = obj;
            }
        }

        public void cleanup() {
            if (this.mActiveOverflow != null && this.mActiveOverflow.size() == 0) {
                this.mActiveOverflow = null;
            }
            if (this.mActiveOverflow == null) {
                if (this.mMap.containsKey(OVERFLOW_NAME)) {
                    Slog.wtf(BatteryStatsImpl.TAG, "Cleaning up with no active overflow, but have overflow entry " + this.mMap.get(OVERFLOW_NAME));
                    this.mMap.remove(OVERFLOW_NAME);
                }
                this.mCurOverflow = null;
            } else if (this.mCurOverflow == null || !this.mMap.containsKey(OVERFLOW_NAME)) {
                Slog.wtf(BatteryStatsImpl.TAG, "Cleaning up with active overflow, but no overflow entry: cur=" + this.mCurOverflow + " map=" + this.mMap.get(OVERFLOW_NAME));
            }
        }

        public T startObject(String name) {
            T obj = this.mMap.get(name);
            if (obj != null) {
                return obj;
            }
            if (this.mActiveOverflow != null) {
                MutableInt over = (MutableInt) this.mActiveOverflow.get(name);
                if (over != null) {
                    obj = this.mCurOverflow;
                    if (obj == null) {
                        Slog.wtf(BatteryStatsImpl.TAG, "Have active overflow " + name + " but null overflow");
                        obj = instantiateObject();
                        this.mCurOverflow = obj;
                        this.mMap.put(OVERFLOW_NAME, obj);
                    }
                    over.value++;
                    return obj;
                }
            }
            if (this.mMap.size() >= 100) {
                obj = this.mCurOverflow;
                if (obj == null) {
                    obj = instantiateObject();
                    this.mCurOverflow = obj;
                    this.mMap.put(OVERFLOW_NAME, obj);
                }
                if (this.mActiveOverflow == null) {
                    this.mActiveOverflow = new ArrayMap();
                }
                this.mActiveOverflow.put(name, new MutableInt(1));
                return obj;
            }
            obj = instantiateObject();
            this.mMap.put(name, obj);
            return obj;
        }

        public T stopObject(String name) {
            T obj = this.mMap.get(name);
            if (obj != null) {
                return obj;
            }
            if (this.mActiveOverflow != null) {
                MutableInt over = (MutableInt) this.mActiveOverflow.get(name);
                if (over != null) {
                    obj = this.mCurOverflow;
                    if (obj != null) {
                        over.value--;
                        if (over.value <= 0) {
                            this.mActiveOverflow.remove(name);
                        }
                        return obj;
                    }
                }
            }
            Slog.wtf(BatteryStatsImpl.TAG, "Unable to find object for " + name + " mapsize=" + this.mMap.size() + " activeoverflow=" + this.mActiveOverflow + " curoverflow=" + this.mCurOverflow);
            return null;
        }
    }

    public static final class SamplingTimer extends Timer {
        int mCurrentReportedCount;
        long mCurrentReportedTotalTime;
        boolean mTimeBaseRunning;
        boolean mTrackingReportedValues;
        int mUnpluggedReportedCount;
        long mUnpluggedReportedTotalTime;
        int mUpdateVersion;

        SamplingTimer(TimeBase timeBase, Parcel in) {
            boolean z = true;
            super(0, timeBase, in);
            this.mCurrentReportedCount = in.readInt();
            this.mUnpluggedReportedCount = in.readInt();
            this.mCurrentReportedTotalTime = in.readLong();
            this.mUnpluggedReportedTotalTime = in.readLong();
            if (in.readInt() != 1) {
                z = false;
            }
            this.mTrackingReportedValues = z;
            this.mTimeBaseRunning = timeBase.isRunning();
        }

        SamplingTimer(TimeBase timeBase, boolean trackReportedValues) {
            super(0, timeBase);
            this.mTrackingReportedValues = trackReportedValues;
            this.mTimeBaseRunning = timeBase.isRunning();
        }

        public void setStale() {
            this.mTrackingReportedValues = false;
            this.mUnpluggedReportedTotalTime = 0;
            this.mUnpluggedReportedCount = 0;
        }

        public void setUpdateVersion(int version) {
            this.mUpdateVersion = version;
        }

        public int getUpdateVersion() {
            return this.mUpdateVersion;
        }

        public void updateCurrentReportedCount(int count) {
            if (this.mTimeBaseRunning && this.mUnpluggedReportedCount == 0) {
                this.mUnpluggedReportedCount = count;
                this.mTrackingReportedValues = true;
            }
            this.mCurrentReportedCount = count;
        }

        public void addCurrentReportedCount(int delta) {
            updateCurrentReportedCount(this.mCurrentReportedCount + delta);
        }

        public void updateCurrentReportedTotalTime(long totalTime) {
            if (this.mTimeBaseRunning && this.mUnpluggedReportedTotalTime == 0) {
                this.mUnpluggedReportedTotalTime = totalTime;
                this.mTrackingReportedValues = true;
            }
            this.mCurrentReportedTotalTime = totalTime;
        }

        public void addCurrentReportedTotalTime(long delta) {
            updateCurrentReportedTotalTime(this.mCurrentReportedTotalTime + delta);
        }

        public void onTimeStarted(long elapsedRealtime, long baseUptime, long baseRealtime) {
            super.onTimeStarted(elapsedRealtime, baseUptime, baseRealtime);
            if (this.mTrackingReportedValues) {
                this.mUnpluggedReportedTotalTime = this.mCurrentReportedTotalTime;
                this.mUnpluggedReportedCount = this.mCurrentReportedCount;
            }
            this.mTimeBaseRunning = true;
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            super.onTimeStopped(elapsedRealtime, baseUptime, baseRealtime);
            this.mTimeBaseRunning = false;
        }

        public void logState(Printer pw, String prefix) {
            super.logState(pw, prefix);
            pw.println(prefix + "mCurrentReportedCount=" + this.mCurrentReportedCount + " mUnpluggedReportedCount=" + this.mUnpluggedReportedCount + " mCurrentReportedTotalTime=" + this.mCurrentReportedTotalTime + " mUnpluggedReportedTotalTime=" + this.mUnpluggedReportedTotalTime);
        }

        protected long computeRunTimeLocked(long curBatteryRealtime) {
            long j = this.mTotalTime;
            long j2 = (this.mTimeBaseRunning && this.mTrackingReportedValues) ? this.mCurrentReportedTotalTime - this.mUnpluggedReportedTotalTime : 0;
            return j2 + j;
        }

        protected int computeCurrentCountLocked() {
            int i = this.mCount;
            int i2 = (this.mTimeBaseRunning && this.mTrackingReportedValues) ? this.mCurrentReportedCount - this.mUnpluggedReportedCount : 0;
            return i2 + i;
        }

        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeInt(this.mCurrentReportedCount);
            out.writeInt(this.mUnpluggedReportedCount);
            out.writeLong(this.mCurrentReportedTotalTime);
            out.writeLong(this.mUnpluggedReportedTotalTime);
            out.writeInt(this.mTrackingReportedValues ? 1 : 0);
        }

        boolean reset(boolean detachIfReset) {
            super.reset(detachIfReset);
            setStale();
            return true;
        }

        void writeSummaryFromParcelLocked(Parcel out, long batteryRealtime) {
            super.writeSummaryFromParcelLocked(out, batteryRealtime);
            out.writeLong(this.mCurrentReportedTotalTime);
            out.writeInt(this.mCurrentReportedCount);
            out.writeInt(this.mTrackingReportedValues ? 1 : 0);
        }

        void readSummaryFromParcelLocked(Parcel in) {
            boolean z = true;
            super.readSummaryFromParcelLocked(in);
            long readLong = in.readLong();
            this.mCurrentReportedTotalTime = readLong;
            this.mUnpluggedReportedTotalTime = readLong;
            int readInt = in.readInt();
            this.mCurrentReportedCount = readInt;
            this.mUnpluggedReportedCount = readInt;
            if (in.readInt() != 1) {
                z = false;
            }
            this.mTrackingReportedValues = z;
        }
    }

    public static final class StopwatchTimer extends Timer {
        long mAcquireTime;
        boolean mInList;
        int mNesting;
        long mTimeout;
        final ArrayList<StopwatchTimer> mTimerPool;
        final Uid mUid;
        long mUpdateTime;

        StopwatchTimer(Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase, Parcel in) {
            super(type, timeBase, in);
            this.mUid = uid;
            this.mTimerPool = timerPool;
            this.mUpdateTime = in.readLong();
        }

        StopwatchTimer(Uid uid, int type, ArrayList<StopwatchTimer> timerPool, TimeBase timeBase) {
            super(type, timeBase);
            this.mUid = uid;
            this.mTimerPool = timerPool;
        }

        void setTimeout(long timeout) {
            this.mTimeout = timeout;
        }

        public void writeToParcel(Parcel out, long elapsedRealtimeUs) {
            super.writeToParcel(out, elapsedRealtimeUs);
            out.writeLong(this.mUpdateTime);
        }

        public void onTimeStopped(long elapsedRealtime, long baseUptime, long baseRealtime) {
            if (this.mNesting > 0) {
                super.onTimeStopped(elapsedRealtime, baseUptime, baseRealtime);
                this.mUpdateTime = baseRealtime;
            }
        }

        public void logState(Printer pw, String prefix) {
            super.logState(pw, prefix);
            pw.println(prefix + "mNesting=" + this.mNesting + " mUpdateTime=" + this.mUpdateTime + " mAcquireTime=" + this.mAcquireTime);
        }

        void startRunningLocked(long elapsedRealtimeMs) {
            int i = this.mNesting;
            this.mNesting = i + 1;
            if (i == 0) {
                long batteryRealtime = this.mTimeBase.getRealtime(1000 * elapsedRealtimeMs);
                this.mUpdateTime = batteryRealtime;
                if (this.mTimerPool != null) {
                    refreshTimersLocked(batteryRealtime, this.mTimerPool, null);
                    this.mTimerPool.add(this);
                }
                this.mCount++;
                this.mAcquireTime = this.mTotalTime;
            }
        }

        boolean isRunningLocked() {
            return this.mNesting > 0;
        }

        void stopRunningLocked(long elapsedRealtimeMs) {
            if (this.mNesting != 0) {
                int i = this.mNesting - 1;
                this.mNesting = i;
                if (i == 0) {
                    long batteryRealtime = this.mTimeBase.getRealtime(1000 * elapsedRealtimeMs);
                    if (this.mTimerPool != null) {
                        refreshTimersLocked(batteryRealtime, this.mTimerPool, null);
                        this.mTimerPool.remove(this);
                    } else {
                        this.mNesting = 1;
                        this.mTotalTime = computeRunTimeLocked(batteryRealtime);
                        this.mNesting = 0;
                    }
                    if (this.mTotalTime == this.mAcquireTime) {
                        this.mCount--;
                    }
                }
            }
        }

        void stopAllRunningLocked(long elapsedRealtimeMs) {
            if (this.mNesting > 0) {
                this.mNesting = 1;
                stopRunningLocked(elapsedRealtimeMs);
            }
        }

        private static long refreshTimersLocked(long batteryRealtime, ArrayList<StopwatchTimer> pool, StopwatchTimer self) {
            long selfTime = 0;
            int N = pool.size();
            for (int i = N - 1; i >= 0; i--) {
                StopwatchTimer t = (StopwatchTimer) pool.get(i);
                long heldTime = batteryRealtime - t.mUpdateTime;
                if (heldTime > 0) {
                    long myTime = heldTime / ((long) N);
                    if (t == self) {
                        selfTime = myTime;
                    }
                    t.mTotalTime += myTime;
                }
                t.mUpdateTime = batteryRealtime;
            }
            return selfTime;
        }

        protected long computeRunTimeLocked(long curBatteryRealtime) {
            int i = 1;
            if (this.mTimeout > 0 && curBatteryRealtime > this.mUpdateTime + this.mTimeout) {
                curBatteryRealtime = this.mUpdateTime + this.mTimeout;
            }
            if (this.mTimerPool != null && this.mTimerPool.size() > 0) {
                i = this.mTimerPool.size();
            }
            long poolsize = (long) i;
            if (this.mType == -400) {
                Slog.d(BatteryStatsImpl.TAG, "poolsize= " + poolsize + ", curBatteryRealtime= " + curBatteryRealtime + ", mUpdateTime= " + this.mUpdateTime + ", mTimeout= " + this.mTimeout + ", mNesting= " + this.mNesting + ", mTotalTime= " + this.mTotalTime);
            }
            if (curBatteryRealtime - this.mUpdateTime <= 0) {
                return this.mTotalTime;
            }
            return (this.mNesting > 0 ? (curBatteryRealtime - this.mUpdateTime) / poolsize : 0) + this.mTotalTime;
        }

        protected int computeCurrentCountLocked() {
            return this.mCount;
        }

        boolean reset(boolean detachIfReset) {
            boolean canDetach;
            boolean z = true;
            if (this.mNesting <= 0) {
                canDetach = true;
            } else {
                canDetach = false;
            }
            if (!(canDetach && detachIfReset)) {
                z = false;
            }
            super.reset(z);
            if (this.mNesting > 0) {
                this.mUpdateTime = this.mTimeBase.getRealtime(SystemClock.elapsedRealtime() * 1000);
            }
            this.mAcquireTime = this.mTotalTime;
            return canDetach;
        }

        void detach() {
            super.detach();
            if (this.mTimerPool != null) {
                this.mTimerPool.remove(this);
            }
        }

        void readSummaryFromParcelLocked(Parcel in) {
            super.readSummaryFromParcelLocked(in);
            this.mNesting = 0;
        }

        public void setMark(long elapsedRealtimeMs) {
            long batteryRealtime = this.mTimeBase.getRealtime(1000 * elapsedRealtimeMs);
            if (this.mNesting > 0) {
                if (this.mTimerPool != null) {
                    refreshTimersLocked(batteryRealtime, this.mTimerPool, this);
                } else {
                    this.mTotalTime += batteryRealtime - this.mUpdateTime;
                    this.mUpdateTime = batteryRealtime;
                }
            }
            this.mTimeBeforeMark = this.mTotalTime;
        }
    }

    static class TimeBase {
        private final ArrayList<TimeBaseObs> mObservers = new ArrayList();
        private long mPastRealtime;
        private long mPastUptime;
        private long mRealtime;
        private long mRealtimeStart;
        private boolean mRunning;
        private long mUnpluggedRealtime;
        private long mUnpluggedUptime;
        private long mUptime;
        private long mUptimeStart;

        TimeBase() {
        }

        public void dump(PrintWriter pw, String prefix) {
            StringBuilder sb = new StringBuilder(128);
            pw.print(prefix);
            pw.print("mRunning=");
            pw.println(this.mRunning);
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mUptime=");
            BatteryStats.formatTimeMs(sb, this.mUptime / 1000);
            pw.println(sb.toString());
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mRealtime=");
            BatteryStats.formatTimeMs(sb, this.mRealtime / 1000);
            pw.println(sb.toString());
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mPastUptime=");
            BatteryStats.formatTimeMs(sb, this.mPastUptime / 1000);
            sb.append("mUptimeStart=");
            BatteryStats.formatTimeMs(sb, this.mUptimeStart / 1000);
            sb.append("mUnpluggedUptime=");
            BatteryStats.formatTimeMs(sb, this.mUnpluggedUptime / 1000);
            pw.println(sb.toString());
            sb.setLength(0);
            sb.append(prefix);
            sb.append("mPastRealtime=");
            BatteryStats.formatTimeMs(sb, this.mPastRealtime / 1000);
            sb.append("mRealtimeStart=");
            BatteryStats.formatTimeMs(sb, this.mRealtimeStart / 1000);
            sb.append("mUnpluggedRealtime=");
            BatteryStats.formatTimeMs(sb, this.mUnpluggedRealtime / 1000);
            pw.println(sb.toString());
        }

        public void add(TimeBaseObs observer) {
            this.mObservers.add(observer);
        }

        public void remove(TimeBaseObs observer) {
            if (!this.mObservers.remove(observer)) {
                Slog.wtf(BatteryStatsImpl.TAG, "Removed unknown observer: " + observer);
            }
        }

        public void init(long uptime, long realtime) {
            this.mRealtime = 0;
            this.mUptime = 0;
            this.mPastUptime = 0;
            this.mPastRealtime = 0;
            this.mUptimeStart = uptime;
            this.mRealtimeStart = realtime;
            this.mUnpluggedUptime = getUptime(this.mUptimeStart);
            this.mUnpluggedRealtime = getRealtime(this.mRealtimeStart);
        }

        public void reset(long uptime, long realtime) {
            if (this.mRunning) {
                this.mUptimeStart = uptime;
                this.mRealtimeStart = realtime;
                this.mUnpluggedUptime = getUptime(uptime);
                this.mUnpluggedRealtime = getRealtime(realtime);
                return;
            }
            this.mPastUptime = 0;
            this.mPastRealtime = 0;
        }

        public long computeUptime(long curTime, int which) {
            switch (which) {
                case 0:
                    return this.mUptime + getUptime(curTime);
                case 1:
                    return getUptime(curTime);
                case 2:
                    return getUptime(curTime) - this.mUnpluggedUptime;
                default:
                    return 0;
            }
        }

        public long computeRealtime(long curTime, int which) {
            switch (which) {
                case 0:
                    return this.mRealtime + getRealtime(curTime);
                case 1:
                    return getRealtime(curTime);
                case 2:
                    return getRealtime(curTime) - this.mUnpluggedRealtime;
                default:
                    return 0;
            }
        }

        public long getUptime(long curTime) {
            long time = this.mPastUptime;
            if (this.mRunning) {
                return time + (curTime - this.mUptimeStart);
            }
            return time;
        }

        public long getRealtime(long curTime) {
            long time = this.mPastRealtime;
            if (this.mRunning) {
                return time + (curTime - this.mRealtimeStart);
            }
            return time;
        }

        public long getUptimeStart() {
            return this.mUptimeStart;
        }

        public long getRealtimeStart() {
            return this.mRealtimeStart;
        }

        public boolean isRunning() {
            return this.mRunning;
        }

        public boolean setRunning(boolean running, long uptime, long realtime) {
            if (this.mRunning == running) {
                return false;
            }
            this.mRunning = running;
            long batteryUptime;
            long batteryRealtime;
            int i;
            if (running) {
                this.mUptimeStart = uptime;
                this.mRealtimeStart = realtime;
                batteryUptime = getUptime(uptime);
                this.mUnpluggedUptime = batteryUptime;
                batteryRealtime = getRealtime(realtime);
                this.mUnpluggedRealtime = batteryRealtime;
                for (i = this.mObservers.size() - 1; i >= 0; i--) {
                    ((TimeBaseObs) this.mObservers.get(i)).onTimeStarted(realtime, batteryUptime, batteryRealtime);
                }
            } else {
                this.mPastUptime += uptime - this.mUptimeStart;
                this.mPastRealtime += realtime - this.mRealtimeStart;
                batteryUptime = getUptime(uptime);
                batteryRealtime = getRealtime(realtime);
                for (i = this.mObservers.size() - 1; i >= 0; i--) {
                    ((TimeBaseObs) this.mObservers.get(i)).onTimeStopped(realtime, batteryUptime, batteryRealtime);
                }
            }
            return true;
        }

        public void readSummaryFromParcel(Parcel in) {
            this.mUptime = in.readLong();
            this.mRealtime = in.readLong();
        }

        public void writeSummaryToParcel(Parcel out, long uptime, long realtime) {
            out.writeLong(computeUptime(uptime, 0));
            out.writeLong(computeRealtime(realtime, 0));
        }

        public void readFromParcel(Parcel in) {
            this.mRunning = false;
            this.mUptime = in.readLong();
            this.mPastUptime = in.readLong();
            this.mUptimeStart = in.readLong();
            this.mRealtime = in.readLong();
            this.mPastRealtime = in.readLong();
            this.mRealtimeStart = in.readLong();
            this.mUnpluggedUptime = in.readLong();
            this.mUnpluggedRealtime = in.readLong();
        }

        public void writeToParcel(Parcel out, long uptime, long realtime) {
            long runningUptime = getUptime(uptime);
            long runningRealtime = getRealtime(realtime);
            out.writeLong(this.mUptime);
            out.writeLong(runningUptime);
            out.writeLong(this.mUptimeStart);
            out.writeLong(this.mRealtime);
            out.writeLong(runningRealtime);
            out.writeLong(this.mRealtimeStart);
            out.writeLong(this.mUnpluggedUptime);
            out.writeLong(this.mUnpluggedRealtime);
        }
    }

    public final class Uid extends android.os.BatteryStats.Uid {
        static final int NO_BATCHED_SCAN_STARTED = -1;
        static final int PROCESS_STATE_NONE = 3;
        StopwatchTimer mAudioTurnedOnTimer;
        LongSamplingCounter[] mBluetoothControllerTime = new LongSamplingCounter[4];
        StopwatchTimer mCameraTurnedOnTimer;
        LongSamplingCounter[][] mCpuClusterSpeed;
        LongSamplingCounter mCpuPower = new LongSamplingCounter(BatteryStatsImpl.this.mOnBatteryTimeBase);
        long mCurStepSystemTime;
        long mCurStepUserTime;
        StopwatchTimer mFlashlightTurnedOnTimer;
        StopwatchTimer mForegroundActivityTimer;
        boolean mFullWifiLockOut;
        StopwatchTimer mFullWifiLockTimer;
        final OverflowArrayMap<StopwatchTimer> mJobStats = new OverflowArrayMap<StopwatchTimer>() {
            {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                com.android.internal.os.BatteryStatsImpl.Uid.this = r2;
                r0 = com.android.internal.os.BatteryStatsImpl.this;
                r1.<init>();
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.3.<init>(com.android.internal.os.BatteryStatsImpl$Uid):void");
            }

            public com.android.internal.os.BatteryStatsImpl.StopwatchTimer instantiateObject() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r5 = this;
                r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
                r1 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r2 = 14;
                r3 = 0;
                r4 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r4 = com.android.internal.os.BatteryStatsImpl.this;
                r4 = r4.mOnBatteryTimeBase;
                r0.<init>(r1, r2, r3, r4);
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.3.instantiateObject():com.android.internal.os.BatteryStatsImpl$StopwatchTimer");
            }
        };
        long mLastStepSystemTime;
        long mLastStepUserTime;
        LongSamplingCounter mMobileRadioActiveCount;
        LongSamplingCounter mMobileRadioActiveTime;
        LongSamplingCounter[] mNetworkByteActivityCounters;
        LongSamplingCounter[] mNetworkPacketActivityCounters;
        final ArrayMap<String, Pkg> mPackageStats = new ArrayMap();
        final SparseArray<Pid> mPids = new SparseArray();
        int mProcessState = 3;
        StopwatchTimer[] mProcessStateTimer;
        final ArrayMap<String, Proc> mProcessStats = new ArrayMap();
        final SparseArray<Sensor> mSensorStats = new SparseArray();
        final OverflowArrayMap<StopwatchTimer> mSyncStats = new OverflowArrayMap<StopwatchTimer>() {
            {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                com.android.internal.os.BatteryStatsImpl.Uid.this = r2;
                r0 = com.android.internal.os.BatteryStatsImpl.this;
                r1.<init>();
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.2.<init>(com.android.internal.os.BatteryStatsImpl$Uid):void");
            }

            public com.android.internal.os.BatteryStatsImpl.StopwatchTimer instantiateObject() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r5 = this;
                r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
                r1 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r2 = 13;
                r3 = 0;
                r4 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r4 = com.android.internal.os.BatteryStatsImpl.this;
                r4 = r4.mOnBatteryTimeBase;
                r0.<init>(r1, r2, r3, r4);
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.2.instantiateObject():com.android.internal.os.BatteryStatsImpl$StopwatchTimer");
            }
        };
        LongSamplingCounter mSystemCpuTime = new LongSamplingCounter(BatteryStatsImpl.this.mOnBatteryTimeBase);
        final int mUid;
        Counter[] mUserActivityCounters;
        LongSamplingCounter mUserCpuTime = new LongSamplingCounter(BatteryStatsImpl.this.mOnBatteryTimeBase);
        BatchTimer mVibratorOnTimer;
        StopwatchTimer mVideoTurnedOnTimer;
        final OverflowArrayMap<Wakelock> mWakelockStats = new OverflowArrayMap<Wakelock>() {
            {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                com.android.internal.os.BatteryStatsImpl.Uid.this = r2;
                r0 = com.android.internal.os.BatteryStatsImpl.this;
                r1.<init>();
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.1.<init>(com.android.internal.os.BatteryStatsImpl$Uid):void");
            }

            public com.android.internal.os.BatteryStatsImpl.Uid.Wakelock instantiateObject() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r2 = this;
                r0 = new com.android.internal.os.BatteryStatsImpl$Uid$Wakelock;
                r1 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r0.<init>();
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.1.instantiateObject():com.android.internal.os.BatteryStatsImpl$Uid$Wakelock");
            }
        };
        int mWifiBatchedScanBinStarted = -1;
        StopwatchTimer[] mWifiBatchedScanTimer;
        LongSamplingCounter[] mWifiControllerTime = new LongSamplingCounter[4];
        boolean mWifiMulticastEnabled;
        StopwatchTimer mWifiMulticastTimer;
        boolean mWifiRunning;
        StopwatchTimer mWifiRunningTimer;
        boolean mWifiScanStarted;
        StopwatchTimer mWifiScanTimer;

        public final class Pkg extends android.os.BatteryStats.Uid.Pkg implements TimeBaseObs {
            final ArrayMap<String, Serv> mServiceStats = new ArrayMap();
            ArrayMap<String, Counter> mWakeupAlarms = new ArrayMap();

            public final class Serv extends android.os.BatteryStats.Uid.Pkg.Serv implements TimeBaseObs {
                int mLastLaunches;
                long mLastStartTime;
                int mLastStarts;
                boolean mLaunched;
                long mLaunchedSince;
                long mLaunchedTime;
                int mLaunches;
                int mLoadedLaunches;
                long mLoadedStartTime;
                int mLoadedStarts;
                boolean mRunning;
                long mRunningSince;
                long mStartTime;
                int mStarts;
                int mUnpluggedLaunches;
                long mUnpluggedStartTime;
                int mUnpluggedStarts;

                Serv(com.android.internal.os.BatteryStatsImpl.Uid.Pkg r2) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r1 = this;
                    com.android.internal.os.BatteryStatsImpl.Uid.Pkg.this = r2;
                    r1.<init>(r2);
                    r0 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                    r0 = com.android.internal.os.BatteryStatsImpl.this;
                    r0 = r0.mOnBatteryTimeBase;
                    r0.add(r1);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.<init>(com.android.internal.os.BatteryStatsImpl$Uid$Pkg):void");
                }

                public void onTimeStarted(long r4, long r6, long r8) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r3 = this;
                    r0 = r3.getStartTimeToNowLocked(r6);
                    r3.mUnpluggedStartTime = r0;
                    r0 = r3.mStarts;
                    r3.mUnpluggedStarts = r0;
                    r0 = r3.mLaunches;
                    r3.mUnpluggedLaunches = r0;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.onTimeStarted(long, long, long):void");
                }

                public void onTimeStopped(long r1, long r3, long r5) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r0 = this;
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.onTimeStopped(long, long, long):void");
                }

                void detach() {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r1 = this;
                    r0 = com.android.internal.os.BatteryStatsImpl.Uid.Pkg.this;
                    r0 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                    r0 = com.android.internal.os.BatteryStatsImpl.this;
                    r0 = r0.mOnBatteryTimeBase;
                    r0.remove(r1);
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.detach():void");
                }

                void readFromParcelLocked(android.os.Parcel r7) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r6 = this;
                    r1 = 1;
                    r2 = 0;
                    r4 = r7.readLong();
                    r6.mStartTime = r4;
                    r4 = r7.readLong();
                    r6.mRunningSince = r4;
                    r0 = r7.readInt();
                    if (r0 == 0) goto L_0x0064;
                L_0x0014:
                    r0 = r1;
                L_0x0015:
                    r6.mRunning = r0;
                    r0 = r7.readInt();
                    r6.mStarts = r0;
                    r4 = r7.readLong();
                    r6.mLaunchedTime = r4;
                    r4 = r7.readLong();
                    r6.mLaunchedSince = r4;
                    r0 = r7.readInt();
                    if (r0 == 0) goto L_0x0066;
                L_0x002f:
                    r6.mLaunched = r1;
                    r0 = r7.readInt();
                    r6.mLaunches = r0;
                    r0 = r7.readLong();
                    r6.mLoadedStartTime = r0;
                    r0 = r7.readInt();
                    r6.mLoadedStarts = r0;
                    r0 = r7.readInt();
                    r6.mLoadedLaunches = r0;
                    r0 = 0;
                    r6.mLastStartTime = r0;
                    r6.mLastStarts = r2;
                    r6.mLastLaunches = r2;
                    r0 = r7.readLong();
                    r6.mUnpluggedStartTime = r0;
                    r0 = r7.readInt();
                    r6.mUnpluggedStarts = r0;
                    r0 = r7.readInt();
                    r6.mUnpluggedLaunches = r0;
                    return;
                L_0x0064:
                    r0 = r2;
                    goto L_0x0015;
                L_0x0066:
                    r1 = r2;
                    goto L_0x002f;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.readFromParcelLocked(android.os.Parcel):void");
                }

                void writeToParcelLocked(android.os.Parcel r7) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r6 = this;
                    r1 = 1;
                    r2 = 0;
                    r4 = r6.mStartTime;
                    r7.writeLong(r4);
                    r4 = r6.mRunningSince;
                    r7.writeLong(r4);
                    r0 = r6.mRunning;
                    if (r0 == 0) goto L_0x004e;
                L_0x0010:
                    r0 = r1;
                L_0x0011:
                    r7.writeInt(r0);
                    r0 = r6.mStarts;
                    r7.writeInt(r0);
                    r4 = r6.mLaunchedTime;
                    r7.writeLong(r4);
                    r4 = r6.mLaunchedSince;
                    r7.writeLong(r4);
                    r0 = r6.mLaunched;
                    if (r0 == 0) goto L_0x0050;
                L_0x0027:
                    r7.writeInt(r1);
                    r0 = r6.mLaunches;
                    r7.writeInt(r0);
                    r0 = r6.mLoadedStartTime;
                    r7.writeLong(r0);
                    r0 = r6.mLoadedStarts;
                    r7.writeInt(r0);
                    r0 = r6.mLoadedLaunches;
                    r7.writeInt(r0);
                    r0 = r6.mUnpluggedStartTime;
                    r7.writeLong(r0);
                    r0 = r6.mUnpluggedStarts;
                    r7.writeInt(r0);
                    r0 = r6.mUnpluggedLaunches;
                    r7.writeInt(r0);
                    return;
                L_0x004e:
                    r0 = r2;
                    goto L_0x0011;
                L_0x0050:
                    r1 = r2;
                    goto L_0x0027;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.writeToParcelLocked(android.os.Parcel):void");
                }

                long getLaunchTimeToNowLocked(long r6) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r5 = this;
                    r0 = r5.mLaunched;
                    if (r0 != 0) goto L_0x0007;
                L_0x0004:
                    r0 = r5.mLaunchedTime;
                L_0x0006:
                    return r0;
                L_0x0007:
                    r0 = r5.mLaunchedTime;
                    r0 = r0 + r6;
                    r2 = r5.mLaunchedSince;
                    r0 = r0 - r2;
                    goto L_0x0006;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.getLaunchTimeToNowLocked(long):long");
                }

                long getStartTimeToNowLocked(long r6) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r5 = this;
                    r0 = r5.mRunning;
                    if (r0 != 0) goto L_0x0007;
                L_0x0004:
                    r0 = r5.mStartTime;
                L_0x0006:
                    return r0;
                L_0x0007:
                    r0 = r5.mStartTime;
                    r0 = r0 + r6;
                    r2 = r5.mRunningSince;
                    r0 = r0 - r2;
                    goto L_0x0006;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.getStartTimeToNowLocked(long):long");
                }

                public void startLaunchedLocked() {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r2 = this;
                    r0 = r2.mLaunched;
                    if (r0 != 0) goto L_0x0019;
                L_0x0004:
                    r0 = r2.mLaunches;
                    r0 = r0 + 1;
                    r2.mLaunches = r0;
                    r0 = com.android.internal.os.BatteryStatsImpl.Uid.Pkg.this;
                    r0 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                    r0 = com.android.internal.os.BatteryStatsImpl.this;
                    r0 = r0.getBatteryUptimeLocked();
                    r2.mLaunchedSince = r0;
                    r0 = 1;
                    r2.mLaunched = r0;
                L_0x0019:
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.startLaunchedLocked():void");
                }

                public void stopLaunchedLocked() {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r6 = this;
                    r2 = r6.mLaunched;
                    if (r2 == 0) goto L_0x0020;
                L_0x0004:
                    r2 = com.android.internal.os.BatteryStatsImpl.Uid.Pkg.this;
                    r2 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                    r2 = com.android.internal.os.BatteryStatsImpl.this;
                    r2 = r2.getBatteryUptimeLocked();
                    r4 = r6.mLaunchedSince;
                    r0 = r2 - r4;
                    r2 = 0;
                    r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
                    if (r2 <= 0) goto L_0x0021;
                L_0x0018:
                    r2 = r6.mLaunchedTime;
                    r2 = r2 + r0;
                    r6.mLaunchedTime = r2;
                L_0x001d:
                    r2 = 0;
                    r6.mLaunched = r2;
                L_0x0020:
                    return;
                L_0x0021:
                    r2 = r6.mLaunches;
                    r2 = r2 + -1;
                    r6.mLaunches = r2;
                    goto L_0x001d;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.stopLaunchedLocked():void");
                }

                public void startRunningLocked() {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r2 = this;
                    r0 = r2.mRunning;
                    if (r0 != 0) goto L_0x0019;
                L_0x0004:
                    r0 = r2.mStarts;
                    r0 = r0 + 1;
                    r2.mStarts = r0;
                    r0 = com.android.internal.os.BatteryStatsImpl.Uid.Pkg.this;
                    r0 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                    r0 = com.android.internal.os.BatteryStatsImpl.this;
                    r0 = r0.getBatteryUptimeLocked();
                    r2.mRunningSince = r0;
                    r0 = 1;
                    r2.mRunning = r0;
                L_0x0019:
                    return;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.startRunningLocked():void");
                }

                public void stopRunningLocked() {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r6 = this;
                    r2 = r6.mRunning;
                    if (r2 == 0) goto L_0x0020;
                L_0x0004:
                    r2 = com.android.internal.os.BatteryStatsImpl.Uid.Pkg.this;
                    r2 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                    r2 = com.android.internal.os.BatteryStatsImpl.this;
                    r2 = r2.getBatteryUptimeLocked();
                    r4 = r6.mRunningSince;
                    r0 = r2 - r4;
                    r2 = 0;
                    r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
                    if (r2 <= 0) goto L_0x0021;
                L_0x0018:
                    r2 = r6.mStartTime;
                    r2 = r2 + r0;
                    r6.mStartTime = r2;
                L_0x001d:
                    r2 = 0;
                    r6.mRunning = r2;
                L_0x0020:
                    return;
                L_0x0021:
                    r2 = r6.mStarts;
                    r2 = r2 + -1;
                    r6.mStarts = r2;
                    goto L_0x001d;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.stopRunningLocked():void");
                }

                public com.android.internal.os.BatteryStatsImpl getBatteryStats() {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r1 = this;
                    r0 = com.android.internal.os.BatteryStatsImpl.Uid.Pkg.this;
                    r0 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                    r0 = com.android.internal.os.BatteryStatsImpl.this;
                    return r0;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.getBatteryStats():com.android.internal.os.BatteryStatsImpl");
                }

                public int getLaunches(int r3) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r2 = this;
                    r0 = r2.mLaunches;
                    r1 = 1;
                    if (r3 != r1) goto L_0x0009;
                L_0x0005:
                    r1 = r2.mLoadedLaunches;
                    r0 = r0 - r1;
                L_0x0008:
                    return r0;
                L_0x0009:
                    r1 = 2;
                    if (r3 != r1) goto L_0x0008;
                L_0x000c:
                    r1 = r2.mUnpluggedLaunches;
                    r0 = r0 - r1;
                    goto L_0x0008;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.getLaunches(int):int");
                }

                public long getStartTime(long r6, int r8) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r5 = this;
                    r0 = r5.getStartTimeToNowLocked(r6);
                    r2 = 1;
                    if (r8 != r2) goto L_0x000b;
                L_0x0007:
                    r2 = r5.mLoadedStartTime;
                    r0 = r0 - r2;
                L_0x000a:
                    return r0;
                L_0x000b:
                    r2 = 2;
                    if (r8 != r2) goto L_0x000a;
                L_0x000e:
                    r2 = r5.mUnpluggedStartTime;
                    r0 = r0 - r2;
                    goto L_0x000a;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.getStartTime(long, int):long");
                }

                public int getStarts(int r3) {
                    /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                    /*
                    r2 = this;
                    r0 = r2.mStarts;
                    r1 = 1;
                    if (r3 != r1) goto L_0x0009;
                L_0x0005:
                    r1 = r2.mLoadedStarts;
                    r0 = r0 - r1;
                L_0x0008:
                    return r0;
                L_0x0009:
                    r1 = 2;
                    if (r3 != r1) goto L_0x0008;
                L_0x000c:
                    r1 = r2.mUnpluggedStarts;
                    r0 = r0 - r1;
                    goto L_0x0008;
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv.getStarts(int):int");
                }
            }

            Pkg(com.android.internal.os.BatteryStatsImpl.Uid r2) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                com.android.internal.os.BatteryStatsImpl.Uid.this = r2;
                r1.<init>();
                r0 = new android.util.ArrayMap;
                r0.<init>();
                r1.mWakeupAlarms = r0;
                r0 = new android.util.ArrayMap;
                r0.<init>();
                r1.mServiceStats = r0;
                r0 = com.android.internal.os.BatteryStatsImpl.this;
                r0 = r0.mOnBatteryScreenOffTimeBase;
                r0.add(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.<init>(com.android.internal.os.BatteryStatsImpl$Uid):void");
            }

            public void onTimeStarted(long r1, long r3, long r5) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r0 = this;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.onTimeStarted(long, long, long):void");
            }

            public void onTimeStopped(long r1, long r3, long r5) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r0 = this;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.onTimeStopped(long, long, long):void");
            }

            void detach() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r0 = com.android.internal.os.BatteryStatsImpl.this;
                r0 = r0.mOnBatteryScreenOffTimeBase;
                r0.remove(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.detach():void");
            }

            void readFromParcelLocked(android.os.Parcel r11) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r10 = this;
                r3 = r11.readInt();
                r7 = r10.mWakeupAlarms;
                r7.clear();
                r0 = 0;
            L_0x000a:
                if (r0 >= r3) goto L_0x0023;
            L_0x000c:
                r6 = r11.readString();
                r7 = r10.mWakeupAlarms;
                r8 = new com.android.internal.os.BatteryStatsImpl$Counter;
                r9 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r9 = com.android.internal.os.BatteryStatsImpl.this;
                r9 = r9.mOnBatteryTimeBase;
                r8.<init>(r9, r11);
                r7.put(r6, r8);
                r0 = r0 + 1;
                goto L_0x000a;
            L_0x0023:
                r2 = r11.readInt();
                r7 = r10.mServiceStats;
                r7.clear();
                r1 = 0;
            L_0x002d:
                if (r1 >= r2) goto L_0x0043;
            L_0x002f:
                r5 = r11.readString();
                r4 = new com.android.internal.os.BatteryStatsImpl$Uid$Pkg$Serv;
                r4.<init>();
                r7 = r10.mServiceStats;
                r7.put(r5, r4);
                r4.readFromParcelLocked(r11);
                r1 = r1 + 1;
                goto L_0x002d;
            L_0x0043:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.readFromParcelLocked(android.os.Parcel):void");
            }

            void writeToParcelLocked(android.os.Parcel r6) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r5 = this;
                r4 = r5.mWakeupAlarms;
                r2 = r4.size();
                r6.writeInt(r2);
                r1 = 0;
            L_0x000a:
                if (r1 >= r2) goto L_0x0025;
            L_0x000c:
                r4 = r5.mWakeupAlarms;
                r4 = r4.keyAt(r1);
                r4 = (java.lang.String) r4;
                r6.writeString(r4);
                r4 = r5.mWakeupAlarms;
                r4 = r4.valueAt(r1);
                r4 = (com.android.internal.os.BatteryStatsImpl.Counter) r4;
                r4.writeToParcel(r6);
                r1 = r1 + 1;
                goto L_0x000a;
            L_0x0025:
                r4 = r5.mServiceStats;
                r0 = r4.size();
                r6.writeInt(r0);
                r1 = 0;
            L_0x002f:
                if (r1 >= r0) goto L_0x004a;
            L_0x0031:
                r4 = r5.mServiceStats;
                r4 = r4.keyAt(r1);
                r4 = (java.lang.String) r4;
                r6.writeString(r4);
                r4 = r5.mServiceStats;
                r3 = r4.valueAt(r1);
                r3 = (com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv) r3;
                r3.writeToParcelLocked(r6);
                r1 = r1 + 1;
                goto L_0x002f;
            L_0x004a:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.writeToParcelLocked(android.os.Parcel):void");
            }

            public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Counter> getWakeupAlarmStats() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = r1.mWakeupAlarms;
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.getWakeupAlarmStats():android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats$Counter>");
            }

            public void noteWakeupAlarmLocked(java.lang.String r3) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r2 = this;
                r1 = r2.mWakeupAlarms;
                r0 = r1.get(r3);
                r0 = (com.android.internal.os.BatteryStatsImpl.Counter) r0;
                if (r0 != 0) goto L_0x001a;
            L_0x000a:
                r0 = new com.android.internal.os.BatteryStatsImpl$Counter;
                r1 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r1 = com.android.internal.os.BatteryStatsImpl.this;
                r1 = r1.mOnBatteryTimeBase;
                r0.<init>(r1);
                r1 = r2.mWakeupAlarms;
                r1.put(r3, r0);
            L_0x001a:
                r0.stepAtomic();
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.noteWakeupAlarmLocked(java.lang.String):void");
            }

            public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Uid.Pkg.Serv> getServiceStats() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = r1.mServiceStats;
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.getServiceStats():android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats$Uid$Pkg$Serv>");
            }

            final com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv newServiceStatsLocked() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = new com.android.internal.os.BatteryStatsImpl$Uid$Pkg$Serv;
                r0.<init>();
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Pkg.newServiceStatsLocked():com.android.internal.os.BatteryStatsImpl$Uid$Pkg$Serv");
            }
        }

        public final class Proc extends android.os.BatteryStats.Uid.Proc implements TimeBaseObs {
            boolean mActive = true;
            ArrayList<ExcessivePower> mExcessivePower;
            long mForegroundTime;
            long mLoadedForegroundTime;
            int mLoadedNumAnrs;
            int mLoadedNumCrashes;
            int mLoadedStarts;
            long mLoadedSystemTime;
            long mLoadedUserTime;
            final String mName;
            int mNumAnrs;
            int mNumCrashes;
            int mProcessState = 3;
            int mStarts;
            long mSystemTime;
            long mUnpluggedForegroundTime;
            int mUnpluggedNumAnrs;
            int mUnpluggedNumCrashes;
            int mUnpluggedStarts;
            long mUnpluggedSystemTime;
            long mUnpluggedUserTime;
            long mUserTime;

            Proc(com.android.internal.os.BatteryStatsImpl.Uid r2, java.lang.String r3) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                com.android.internal.os.BatteryStatsImpl.Uid.this = r2;
                r1.<init>();
                r0 = 1;
                r1.mActive = r0;
                r0 = 3;
                r1.mProcessState = r0;
                r1.mName = r3;
                r0 = com.android.internal.os.BatteryStatsImpl.this;
                r0 = r0.mOnBatteryTimeBase;
                r0.add(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.<init>(com.android.internal.os.BatteryStatsImpl$Uid, java.lang.String):void");
            }

            public void onTimeStarted(long r3, long r5, long r7) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r2 = this;
                r0 = r2.mUserTime;
                r2.mUnpluggedUserTime = r0;
                r0 = r2.mSystemTime;
                r2.mUnpluggedSystemTime = r0;
                r0 = r2.mForegroundTime;
                r2.mUnpluggedForegroundTime = r0;
                r0 = r2.mStarts;
                r2.mUnpluggedStarts = r0;
                r0 = r2.mNumCrashes;
                r2.mUnpluggedNumCrashes = r0;
                r0 = r2.mNumAnrs;
                r2.mUnpluggedNumAnrs = r0;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.onTimeStarted(long, long, long):void");
            }

            public void onTimeStopped(long r1, long r3, long r5) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r0 = this;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.onTimeStopped(long, long, long):void");
            }

            void reset() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r4 = this;
                r2 = 0;
                r0 = 0;
                r4.mForegroundTime = r2;
                r4.mSystemTime = r2;
                r4.mUserTime = r2;
                r4.mNumAnrs = r0;
                r4.mNumCrashes = r0;
                r4.mStarts = r0;
                r4.mLoadedForegroundTime = r2;
                r4.mLoadedSystemTime = r2;
                r4.mLoadedUserTime = r2;
                r4.mLoadedNumAnrs = r0;
                r4.mLoadedNumCrashes = r0;
                r4.mLoadedStarts = r0;
                r4.mUnpluggedForegroundTime = r2;
                r4.mUnpluggedSystemTime = r2;
                r4.mUnpluggedUserTime = r2;
                r4.mUnpluggedNumAnrs = r0;
                r4.mUnpluggedNumCrashes = r0;
                r4.mUnpluggedStarts = r0;
                r0 = 0;
                r4.mExcessivePower = r0;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.reset():void");
            }

            void detach() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = 0;
                r1.mActive = r0;
                r0 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r0 = com.android.internal.os.BatteryStatsImpl.this;
                r0 = r0.mOnBatteryTimeBase;
                r0.remove(r1);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.detach():void");
            }

            public int countExcessivePowers() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = r1.mExcessivePower;
                if (r0 == 0) goto L_0x000b;
            L_0x0004:
                r0 = r1.mExcessivePower;
                r0 = r0.size();
            L_0x000a:
                return r0;
            L_0x000b:
                r0 = 0;
                goto L_0x000a;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.countExcessivePowers():int");
            }

            public android.os.BatteryStats.Uid.Proc.ExcessivePower getExcessivePower(int r2) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = r1.mExcessivePower;
                if (r0 == 0) goto L_0x000d;
            L_0x0004:
                r0 = r1.mExcessivePower;
                r0 = r0.get(r2);
                r0 = (android.os.BatteryStats.Uid.Proc.ExcessivePower) r0;
            L_0x000c:
                return r0;
            L_0x000d:
                r0 = 0;
                goto L_0x000c;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.getExcessivePower(int):android.os.BatteryStats$Uid$Proc$ExcessivePower");
            }

            public void addExcessiveWake(long r4, long r6) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r3 = this;
                r1 = r3.mExcessivePower;
                if (r1 != 0) goto L_0x000b;
            L_0x0004:
                r1 = new java.util.ArrayList;
                r1.<init>();
                r3.mExcessivePower = r1;
            L_0x000b:
                r0 = new android.os.BatteryStats$Uid$Proc$ExcessivePower;
                r0.<init>();
                r1 = 1;
                r0.type = r1;
                r0.overTime = r4;
                r0.usedTime = r6;
                r1 = r3.mExcessivePower;
                r1.add(r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.addExcessiveWake(long, long):void");
            }

            public void addExcessiveCpu(long r4, long r6) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r3 = this;
                r1 = r3.mExcessivePower;
                if (r1 != 0) goto L_0x000b;
            L_0x0004:
                r1 = new java.util.ArrayList;
                r1.<init>();
                r3.mExcessivePower = r1;
            L_0x000b:
                r0 = new android.os.BatteryStats$Uid$Proc$ExcessivePower;
                r0.<init>();
                r1 = 2;
                r0.type = r1;
                r0.overTime = r4;
                r0.usedTime = r6;
                r1 = r3.mExcessivePower;
                r1.add(r0);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.addExcessiveCpu(long, long):void");
            }

            void writeExcessivePowerToParcelLocked(android.os.Parcel r7) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r6 = this;
                r3 = r6.mExcessivePower;
                if (r3 != 0) goto L_0x0009;
            L_0x0004:
                r3 = 0;
                r7.writeInt(r3);
            L_0x0008:
                return;
            L_0x0009:
                r3 = r6.mExcessivePower;
                r0 = r3.size();
                r7.writeInt(r0);
                r2 = 0;
            L_0x0013:
                if (r2 >= r0) goto L_0x0008;
            L_0x0015:
                r3 = r6.mExcessivePower;
                r1 = r3.get(r2);
                r1 = (android.os.BatteryStats.Uid.Proc.ExcessivePower) r1;
                r3 = r1.type;
                r7.writeInt(r3);
                r4 = r1.overTime;
                r7.writeLong(r4);
                r4 = r1.usedTime;
                r7.writeLong(r4);
                r2 = r2 + 1;
                goto L_0x0013;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.writeExcessivePowerToParcelLocked(android.os.Parcel):void");
            }

            void readExcessivePowerFromParcelLocked(android.os.Parcel r7) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r6 = this;
                r0 = r7.readInt();
                if (r0 != 0) goto L_0x000a;
            L_0x0006:
                r3 = 0;
                r6.mExcessivePower = r3;
            L_0x0009:
                return;
            L_0x000a:
                r3 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
                if (r0 <= r3) goto L_0x0027;
            L_0x000e:
                r3 = new android.os.ParcelFormatException;
                r4 = new java.lang.StringBuilder;
                r4.<init>();
                r5 = "File corrupt: too many excessive power entries ";
                r4 = r4.append(r5);
                r4 = r4.append(r0);
                r4 = r4.toString();
                r3.<init>(r4);
                throw r3;
            L_0x0027:
                r3 = new java.util.ArrayList;
                r3.<init>();
                r6.mExcessivePower = r3;
                r2 = 0;
            L_0x002f:
                if (r2 >= r0) goto L_0x0009;
            L_0x0031:
                r1 = new android.os.BatteryStats$Uid$Proc$ExcessivePower;
                r1.<init>();
                r3 = r7.readInt();
                r1.type = r3;
                r4 = r7.readLong();
                r1.overTime = r4;
                r4 = r7.readLong();
                r1.usedTime = r4;
                r3 = r6.mExcessivePower;
                r3.add(r1);
                r2 = r2 + 1;
                goto L_0x002f;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.readExcessivePowerFromParcelLocked(android.os.Parcel):void");
            }

            void writeToParcelLocked(android.os.Parcel r3) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r2 = this;
                r0 = r2.mUserTime;
                r3.writeLong(r0);
                r0 = r2.mSystemTime;
                r3.writeLong(r0);
                r0 = r2.mForegroundTime;
                r3.writeLong(r0);
                r0 = r2.mStarts;
                r3.writeInt(r0);
                r0 = r2.mNumCrashes;
                r3.writeInt(r0);
                r0 = r2.mNumAnrs;
                r3.writeInt(r0);
                r0 = r2.mLoadedUserTime;
                r3.writeLong(r0);
                r0 = r2.mLoadedSystemTime;
                r3.writeLong(r0);
                r0 = r2.mLoadedForegroundTime;
                r3.writeLong(r0);
                r0 = r2.mLoadedStarts;
                r3.writeInt(r0);
                r0 = r2.mLoadedNumCrashes;
                r3.writeInt(r0);
                r0 = r2.mLoadedNumAnrs;
                r3.writeInt(r0);
                r0 = r2.mUnpluggedUserTime;
                r3.writeLong(r0);
                r0 = r2.mUnpluggedSystemTime;
                r3.writeLong(r0);
                r0 = r2.mUnpluggedForegroundTime;
                r3.writeLong(r0);
                r0 = r2.mUnpluggedStarts;
                r3.writeInt(r0);
                r0 = r2.mUnpluggedNumCrashes;
                r3.writeInt(r0);
                r0 = r2.mUnpluggedNumAnrs;
                r3.writeInt(r0);
                r2.writeExcessivePowerToParcelLocked(r3);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.writeToParcelLocked(android.os.Parcel):void");
            }

            void readFromParcelLocked(android.os.Parcel r3) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r2 = this;
                r0 = r3.readLong();
                r2.mUserTime = r0;
                r0 = r3.readLong();
                r2.mSystemTime = r0;
                r0 = r3.readLong();
                r2.mForegroundTime = r0;
                r0 = r3.readInt();
                r2.mStarts = r0;
                r0 = r3.readInt();
                r2.mNumCrashes = r0;
                r0 = r3.readInt();
                r2.mNumAnrs = r0;
                r0 = r3.readLong();
                r2.mLoadedUserTime = r0;
                r0 = r3.readLong();
                r2.mLoadedSystemTime = r0;
                r0 = r3.readLong();
                r2.mLoadedForegroundTime = r0;
                r0 = r3.readInt();
                r2.mLoadedStarts = r0;
                r0 = r3.readInt();
                r2.mLoadedNumCrashes = r0;
                r0 = r3.readInt();
                r2.mLoadedNumAnrs = r0;
                r0 = r3.readLong();
                r2.mUnpluggedUserTime = r0;
                r0 = r3.readLong();
                r2.mUnpluggedSystemTime = r0;
                r0 = r3.readLong();
                r2.mUnpluggedForegroundTime = r0;
                r0 = r3.readInt();
                r2.mUnpluggedStarts = r0;
                r0 = r3.readInt();
                r2.mUnpluggedNumCrashes = r0;
                r0 = r3.readInt();
                r2.mUnpluggedNumAnrs = r0;
                r2.readExcessivePowerFromParcelLocked(r3);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.readFromParcelLocked(android.os.Parcel):void");
            }

            public void addCpuTimeLocked(int r5, int r6) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r4 = this;
                r0 = r4.mUserTime;
                r2 = (long) r5;
                r0 = r0 + r2;
                r4.mUserTime = r0;
                r0 = r4.mSystemTime;
                r2 = (long) r6;
                r0 = r0 + r2;
                r4.mSystemTime = r0;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.addCpuTimeLocked(int, int):void");
            }

            public void addForegroundTimeLocked(long r4) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r3 = this;
                r0 = r3.mForegroundTime;
                r0 = r0 + r4;
                r3.mForegroundTime = r0;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.addForegroundTimeLocked(long):void");
            }

            public void incStartsLocked() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = r1.mStarts;
                r0 = r0 + 1;
                r1.mStarts = r0;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.incStartsLocked():void");
            }

            public void incNumCrashesLocked() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = r1.mNumCrashes;
                r0 = r0 + 1;
                r1.mNumCrashes = r0;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.incNumCrashesLocked():void");
            }

            public void incNumAnrsLocked() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = r1.mNumAnrs;
                r0 = r0 + 1;
                r1.mNumAnrs = r0;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.incNumAnrsLocked():void");
            }

            public boolean isActive() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = r1.mActive;
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.isActive():boolean");
            }

            public long getUserTime(int r5) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r4 = this;
                r0 = r4.mUserTime;
                r2 = 1;
                if (r5 != r2) goto L_0x0009;
            L_0x0005:
                r2 = r4.mLoadedUserTime;
                r0 = r0 - r2;
            L_0x0008:
                return r0;
            L_0x0009:
                r2 = 2;
                if (r5 != r2) goto L_0x0008;
            L_0x000c:
                r2 = r4.mUnpluggedUserTime;
                r0 = r0 - r2;
                goto L_0x0008;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.getUserTime(int):long");
            }

            public long getSystemTime(int r5) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r4 = this;
                r0 = r4.mSystemTime;
                r2 = 1;
                if (r5 != r2) goto L_0x0009;
            L_0x0005:
                r2 = r4.mLoadedSystemTime;
                r0 = r0 - r2;
            L_0x0008:
                return r0;
            L_0x0009:
                r2 = 2;
                if (r5 != r2) goto L_0x0008;
            L_0x000c:
                r2 = r4.mUnpluggedSystemTime;
                r0 = r0 - r2;
                goto L_0x0008;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.getSystemTime(int):long");
            }

            public long getForegroundTime(int r5) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r4 = this;
                r0 = r4.mForegroundTime;
                r2 = 1;
                if (r5 != r2) goto L_0x0009;
            L_0x0005:
                r2 = r4.mLoadedForegroundTime;
                r0 = r0 - r2;
            L_0x0008:
                return r0;
            L_0x0009:
                r2 = 2;
                if (r5 != r2) goto L_0x0008;
            L_0x000c:
                r2 = r4.mUnpluggedForegroundTime;
                r0 = r0 - r2;
                goto L_0x0008;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.getForegroundTime(int):long");
            }

            public int getStarts(int r3) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r2 = this;
                r0 = r2.mStarts;
                r1 = 1;
                if (r3 != r1) goto L_0x0009;
            L_0x0005:
                r1 = r2.mLoadedStarts;
                r0 = r0 - r1;
            L_0x0008:
                return r0;
            L_0x0009:
                r1 = 2;
                if (r3 != r1) goto L_0x0008;
            L_0x000c:
                r1 = r2.mUnpluggedStarts;
                r0 = r0 - r1;
                goto L_0x0008;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.getStarts(int):int");
            }

            public int getNumCrashes(int r3) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r2 = this;
                r0 = r2.mNumCrashes;
                r1 = 1;
                if (r3 != r1) goto L_0x0009;
            L_0x0005:
                r1 = r2.mLoadedNumCrashes;
                r0 = r0 - r1;
            L_0x0008:
                return r0;
            L_0x0009:
                r1 = 2;
                if (r3 != r1) goto L_0x0008;
            L_0x000c:
                r1 = r2.mUnpluggedNumCrashes;
                r0 = r0 - r1;
                goto L_0x0008;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.getNumCrashes(int):int");
            }

            public int getNumAnrs(int r3) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r2 = this;
                r0 = r2.mNumAnrs;
                r1 = 1;
                if (r3 != r1) goto L_0x0009;
            L_0x0005:
                r1 = r2.mLoadedNumAnrs;
                r0 = r0 - r1;
            L_0x0008:
                return r0;
            L_0x0009:
                r1 = 2;
                if (r3 != r1) goto L_0x0008;
            L_0x000c:
                r1 = r2.mUnpluggedNumAnrs;
                r0 = r0 - r1;
                goto L_0x0008;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Proc.getNumAnrs(int):int");
            }
        }

        public final class Sensor extends android.os.BatteryStats.Uid.Sensor {
            final int mHandle;
            StopwatchTimer mTimer;

            public Sensor(com.android.internal.os.BatteryStatsImpl.Uid r1, int r2) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r0 = this;
                com.android.internal.os.BatteryStatsImpl.Uid.this = r1;
                r0.<init>();
                r0.mHandle = r2;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Sensor.<init>(com.android.internal.os.BatteryStatsImpl$Uid, int):void");
            }

            private com.android.internal.os.BatteryStatsImpl.StopwatchTimer readTimerFromParcel(com.android.internal.os.BatteryStatsImpl.TimeBase r7, android.os.Parcel r8) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r6 = this;
                r0 = r8.readInt();
                if (r0 != 0) goto L_0x0008;
            L_0x0006:
                r0 = 0;
            L_0x0007:
                return r0;
            L_0x0008:
                r0 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r0 = com.android.internal.os.BatteryStatsImpl.this;
                r0 = r0.mSensorTimers;
                r1 = r6.mHandle;
                r3 = r0.get(r1);
                r3 = (java.util.ArrayList) r3;
                if (r3 != 0) goto L_0x0028;
            L_0x0018:
                r3 = new java.util.ArrayList;
                r3.<init>();
                r0 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r0 = com.android.internal.os.BatteryStatsImpl.this;
                r0 = r0.mSensorTimers;
                r1 = r6.mHandle;
                r0.put(r1, r3);
            L_0x0028:
                r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
                r1 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r2 = 0;
                r4 = r7;
                r5 = r8;
                r0.<init>(r1, r2, r3, r4, r5);
                goto L_0x0007;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Sensor.readTimerFromParcel(com.android.internal.os.BatteryStatsImpl$TimeBase, android.os.Parcel):com.android.internal.os.BatteryStatsImpl$StopwatchTimer");
            }

            boolean reset() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r2 = this;
                r0 = 1;
                r1 = r2.mTimer;
                r1 = r1.reset(r0);
                if (r1 == 0) goto L_0x000d;
            L_0x0009:
                r1 = 0;
                r2.mTimer = r1;
            L_0x000c:
                return r0;
            L_0x000d:
                r0 = 0;
                goto L_0x000c;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Sensor.reset():boolean");
            }

            void readFromParcelLocked(com.android.internal.os.BatteryStatsImpl.TimeBase r2, android.os.Parcel r3) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = r1.readTimerFromParcel(r2, r3);
                r1.mTimer = r0;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Sensor.readFromParcelLocked(com.android.internal.os.BatteryStatsImpl$TimeBase, android.os.Parcel):void");
            }

            void writeToParcelLocked(android.os.Parcel r3, long r4) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r2 = this;
                r0 = r2.mTimer;
                com.android.internal.os.BatteryStatsImpl.Timer.writeTimerToParcel(r3, r0, r4);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Sensor.writeToParcelLocked(android.os.Parcel, long):void");
            }

            public com.android.internal.os.BatteryStatsImpl.Timer getSensorTime() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = r1.mTimer;
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Sensor.getSensorTime():com.android.internal.os.BatteryStatsImpl$Timer");
            }

            public int getHandle() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r1 = this;
                r0 = r1.mHandle;
                return r0;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Sensor.getHandle():int");
            }
        }

        public final class Wakelock extends android.os.BatteryStats.Uid.Wakelock {
            StopwatchTimer mTimerDraw;
            StopwatchTimer mTimerFull;
            StopwatchTimer mTimerPartial;
            StopwatchTimer mTimerWindow;

            private com.android.internal.os.BatteryStatsImpl.StopwatchTimer readTimerFromParcel(int r7, java.util.ArrayList<com.android.internal.os.BatteryStatsImpl.StopwatchTimer> r8, com.android.internal.os.BatteryStatsImpl.TimeBase r9, android.os.Parcel r10) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r6 = this;
                r0 = r10.readInt();
                if (r0 != 0) goto L_0x0008;
            L_0x0006:
                r0 = 0;
            L_0x0007:
                return r0;
            L_0x0008:
                r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
                r1 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r2 = r7;
                r3 = r8;
                r4 = r9;
                r5 = r10;
                r0.<init>(r1, r2, r3, r4, r5);
                goto L_0x0007;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Wakelock.readTimerFromParcel(int, java.util.ArrayList, com.android.internal.os.BatteryStatsImpl$TimeBase, android.os.Parcel):com.android.internal.os.BatteryStatsImpl$StopwatchTimer");
            }

            boolean reset() {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r5 = this;
                r4 = 0;
                r2 = 1;
                r3 = 0;
                r0 = 0;
                r1 = r5.mTimerFull;
                if (r1 == 0) goto L_0x0012;
            L_0x0008:
                r1 = r5.mTimerFull;
                r1 = r1.reset(r3);
                if (r1 != 0) goto L_0x006d;
            L_0x0010:
                r1 = r2;
            L_0x0011:
                r0 = r0 | r1;
            L_0x0012:
                r1 = r5.mTimerPartial;
                if (r1 == 0) goto L_0x0020;
            L_0x0016:
                r1 = r5.mTimerPartial;
                r1 = r1.reset(r3);
                if (r1 != 0) goto L_0x006f;
            L_0x001e:
                r1 = r2;
            L_0x001f:
                r0 = r0 | r1;
            L_0x0020:
                r1 = r5.mTimerWindow;
                if (r1 == 0) goto L_0x002e;
            L_0x0024:
                r1 = r5.mTimerWindow;
                r1 = r1.reset(r3);
                if (r1 != 0) goto L_0x0071;
            L_0x002c:
                r1 = r2;
            L_0x002d:
                r0 = r0 | r1;
            L_0x002e:
                r1 = r5.mTimerDraw;
                if (r1 == 0) goto L_0x003c;
            L_0x0032:
                r1 = r5.mTimerDraw;
                r1 = r1.reset(r3);
                if (r1 != 0) goto L_0x0073;
            L_0x003a:
                r1 = r2;
            L_0x003b:
                r0 = r0 | r1;
            L_0x003c:
                if (r0 != 0) goto L_0x006a;
            L_0x003e:
                r1 = r5.mTimerFull;
                if (r1 == 0) goto L_0x0049;
            L_0x0042:
                r1 = r5.mTimerFull;
                r1.detach();
                r5.mTimerFull = r4;
            L_0x0049:
                r1 = r5.mTimerPartial;
                if (r1 == 0) goto L_0x0054;
            L_0x004d:
                r1 = r5.mTimerPartial;
                r1.detach();
                r5.mTimerPartial = r4;
            L_0x0054:
                r1 = r5.mTimerWindow;
                if (r1 == 0) goto L_0x005f;
            L_0x0058:
                r1 = r5.mTimerWindow;
                r1.detach();
                r5.mTimerWindow = r4;
            L_0x005f:
                r1 = r5.mTimerDraw;
                if (r1 == 0) goto L_0x006a;
            L_0x0063:
                r1 = r5.mTimerDraw;
                r1.detach();
                r5.mTimerDraw = r4;
            L_0x006a:
                if (r0 != 0) goto L_0x0075;
            L_0x006c:
                return r2;
            L_0x006d:
                r1 = r3;
                goto L_0x0011;
            L_0x006f:
                r1 = r3;
                goto L_0x001f;
            L_0x0071:
                r1 = r3;
                goto L_0x002d;
            L_0x0073:
                r1 = r3;
                goto L_0x003b;
            L_0x0075:
                r2 = r3;
                goto L_0x006c;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Wakelock.reset():boolean");
            }

            void readFromParcelLocked(com.android.internal.os.BatteryStatsImpl.TimeBase r3, com.android.internal.os.BatteryStatsImpl.TimeBase r4, android.os.Parcel r5) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r2 = this;
                r0 = 0;
                r1 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r1 = com.android.internal.os.BatteryStatsImpl.this;
                r1 = r1.mPartialTimers;
                r0 = r2.readTimerFromParcel(r0, r1, r4, r5);
                r2.mTimerPartial = r0;
                r0 = 1;
                r1 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r1 = com.android.internal.os.BatteryStatsImpl.this;
                r1 = r1.mFullTimers;
                r0 = r2.readTimerFromParcel(r0, r1, r3, r5);
                r2.mTimerFull = r0;
                r0 = 2;
                r1 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r1 = com.android.internal.os.BatteryStatsImpl.this;
                r1 = r1.mWindowTimers;
                r0 = r2.readTimerFromParcel(r0, r1, r3, r5);
                r2.mTimerWindow = r0;
                r0 = 18;
                r1 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r1 = com.android.internal.os.BatteryStatsImpl.this;
                r1 = r1.mDrawTimers;
                r0 = r2.readTimerFromParcel(r0, r1, r3, r5);
                r2.mTimerDraw = r0;
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Wakelock.readFromParcelLocked(com.android.internal.os.BatteryStatsImpl$TimeBase, com.android.internal.os.BatteryStatsImpl$TimeBase, android.os.Parcel):void");
            }

            void writeToParcelLocked(android.os.Parcel r3, long r4) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r2 = this;
                r0 = r2.mTimerPartial;
                com.android.internal.os.BatteryStatsImpl.Timer.writeTimerToParcel(r3, r0, r4);
                r0 = r2.mTimerFull;
                com.android.internal.os.BatteryStatsImpl.Timer.writeTimerToParcel(r3, r0, r4);
                r0 = r2.mTimerWindow;
                com.android.internal.os.BatteryStatsImpl.Timer.writeTimerToParcel(r3, r0, r4);
                r0 = r2.mTimerDraw;
                com.android.internal.os.BatteryStatsImpl.Timer.writeTimerToParcel(r3, r0, r4);
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Wakelock.writeToParcelLocked(android.os.Parcel, long):void");
            }

            public com.android.internal.os.BatteryStatsImpl.Timer getWakeTime(int r4) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r3 = this;
                switch(r4) {
                    case 0: goto L_0x0020;
                    case 1: goto L_0x001d;
                    case 2: goto L_0x0023;
                    case 18: goto L_0x0026;
                    default: goto L_0x0003;
                };
            L_0x0003:
                r0 = new java.lang.IllegalArgumentException;
                r1 = new java.lang.StringBuilder;
                r1.<init>();
                r2 = "type = ";
                r1 = r1.append(r2);
                r1 = r1.append(r4);
                r1 = r1.toString();
                r0.<init>(r1);
                throw r0;
            L_0x001d:
                r0 = r3.mTimerFull;
            L_0x001f:
                return r0;
            L_0x0020:
                r0 = r3.mTimerPartial;
                goto L_0x001f;
            L_0x0023:
                r0 = r3.mTimerWindow;
                goto L_0x001f;
            L_0x0026:
                r0 = r3.mTimerDraw;
                goto L_0x001f;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Wakelock.getWakeTime(int):com.android.internal.os.BatteryStatsImpl$Timer");
            }

            public com.android.internal.os.BatteryStatsImpl.StopwatchTimer getStopwatchTimer(int r7) {
                /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
                /*
                r6 = this;
                switch(r7) {
                    case 0: goto L_0x001d;
                    case 1: goto L_0x0039;
                    case 2: goto L_0x0055;
                    case 18: goto L_0x0071;
                    default: goto L_0x0003;
                };
            L_0x0003:
                r2 = new java.lang.IllegalArgumentException;
                r3 = new java.lang.StringBuilder;
                r3.<init>();
                r4 = "type=";
                r3 = r3.append(r4);
                r3 = r3.append(r7);
                r3 = r3.toString();
                r2.<init>(r3);
                throw r2;
            L_0x001d:
                r0 = r6.mTimerPartial;
                if (r0 != 0) goto L_0x0037;
            L_0x0021:
                r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
                r2 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r3 = 0;
                r4 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r4 = com.android.internal.os.BatteryStatsImpl.this;
                r4 = r4.mPartialTimers;
                r5 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r5 = com.android.internal.os.BatteryStatsImpl.this;
                r5 = r5.mOnBatteryScreenOffTimeBase;
                r0.<init>(r2, r3, r4, r5);
                r6.mTimerPartial = r0;
            L_0x0037:
                r1 = r0;
            L_0x0038:
                return r1;
            L_0x0039:
                r0 = r6.mTimerFull;
                if (r0 != 0) goto L_0x0053;
            L_0x003d:
                r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
                r2 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r3 = 1;
                r4 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r4 = com.android.internal.os.BatteryStatsImpl.this;
                r4 = r4.mFullTimers;
                r5 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r5 = com.android.internal.os.BatteryStatsImpl.this;
                r5 = r5.mOnBatteryTimeBase;
                r0.<init>(r2, r3, r4, r5);
                r6.mTimerFull = r0;
            L_0x0053:
                r1 = r0;
                goto L_0x0038;
            L_0x0055:
                r0 = r6.mTimerWindow;
                if (r0 != 0) goto L_0x006f;
            L_0x0059:
                r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
                r2 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r3 = 2;
                r4 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r4 = com.android.internal.os.BatteryStatsImpl.this;
                r4 = r4.mWindowTimers;
                r5 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r5 = com.android.internal.os.BatteryStatsImpl.this;
                r5 = r5.mOnBatteryTimeBase;
                r0.<init>(r2, r3, r4, r5);
                r6.mTimerWindow = r0;
            L_0x006f:
                r1 = r0;
                goto L_0x0038;
            L_0x0071:
                r0 = r6.mTimerDraw;
                if (r0 != 0) goto L_0x008c;
            L_0x0075:
                r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
                r2 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r3 = 18;
                r4 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r4 = com.android.internal.os.BatteryStatsImpl.this;
                r4 = r4.mDrawTimers;
                r5 = com.android.internal.os.BatteryStatsImpl.Uid.this;
                r5 = com.android.internal.os.BatteryStatsImpl.this;
                r5 = r5.mOnBatteryTimeBase;
                r0.<init>(r2, r3, r4, r5);
                r6.mTimerDraw = r0;
            L_0x008c:
                r1 = r0;
                goto L_0x0038;
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.Wakelock.getStopwatchTimer(int):com.android.internal.os.BatteryStatsImpl$StopwatchTimer");
            }
        }

        public Uid(com.android.internal.os.BatteryStatsImpl r7, int r8) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r6 = this;
            r5 = 5;
            r4 = 3;
            r3 = 4;
            com.android.internal.os.BatteryStatsImpl.this = r7;
            r6.<init>();
            r0 = -1;
            r6.mWifiBatchedScanBinStarted = r0;
            r6.mProcessState = r4;
            r0 = new com.android.internal.os.BatteryStatsImpl.LongSamplingCounter[r3];
            r6.mWifiControllerTime = r0;
            r0 = new com.android.internal.os.BatteryStatsImpl.LongSamplingCounter[r3];
            r6.mBluetoothControllerTime = r0;
            r0 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r1 = com.android.internal.os.BatteryStatsImpl.this;
            r1 = r1.mOnBatteryTimeBase;
            r0.<init>(r1);
            r6.mUserCpuTime = r0;
            r0 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r1 = com.android.internal.os.BatteryStatsImpl.this;
            r1 = r1.mOnBatteryTimeBase;
            r0.<init>(r1);
            r6.mSystemCpuTime = r0;
            r0 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r1 = com.android.internal.os.BatteryStatsImpl.this;
            r1 = r1.mOnBatteryTimeBase;
            r0.<init>(r1);
            r6.mCpuPower = r0;
            r0 = new com.android.internal.os.BatteryStatsImpl$Uid$1;
            r0.<init>();
            r6.mWakelockStats = r0;
            r0 = new com.android.internal.os.BatteryStatsImpl$Uid$2;
            r0.<init>();
            r6.mSyncStats = r0;
            r0 = new com.android.internal.os.BatteryStatsImpl$Uid$3;
            r0.<init>();
            r6.mJobStats = r0;
            r0 = new android.util.SparseArray;
            r0.<init>();
            r6.mSensorStats = r0;
            r0 = new android.util.ArrayMap;
            r0.<init>();
            r6.mProcessStats = r0;
            r0 = new android.util.ArrayMap;
            r0.<init>();
            r6.mPackageStats = r0;
            r0 = new android.util.SparseArray;
            r0.<init>();
            r6.mPids = r0;
            r6.mUid = r8;
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = r7.mWifiRunningTimers;
            r2 = r7.mOnBatteryTimeBase;
            r0.<init>(r6, r3, r1, r2);
            r6.mWifiRunningTimer = r0;
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = r7.mFullWifiLockTimers;
            r2 = r7.mOnBatteryTimeBase;
            r0.<init>(r6, r5, r1, r2);
            r6.mFullWifiLockTimer = r0;
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = 6;
            r2 = r7.mWifiScanTimers;
            r3 = r7.mOnBatteryTimeBase;
            r0.<init>(r6, r1, r2, r3);
            r6.mWifiScanTimer = r0;
            r0 = new com.android.internal.os.BatteryStatsImpl.StopwatchTimer[r5];
            r6.mWifiBatchedScanTimer = r0;
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = 7;
            r2 = r7.mWifiMulticastTimers;
            r3 = r7.mOnBatteryTimeBase;
            r0.<init>(r6, r1, r2, r3);
            r6.mWifiMulticastTimer = r0;
            r0 = new com.android.internal.os.BatteryStatsImpl.StopwatchTimer[r4];
            r6.mProcessStateTimer = r0;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.<init>(com.android.internal.os.BatteryStatsImpl, int):void");
        }

        public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Uid.Wakelock> getWakelockStats() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mWakelockStats;
            r0 = r0.getMap();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getWakelockStats():android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats$Uid$Wakelock>");
        }

        public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Timer> getSyncStats() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mSyncStats;
            r0 = r0.getMap();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getSyncStats():android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats$Timer>");
        }

        public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Timer> getJobStats() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mJobStats;
            r0 = r0.getMap();
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getJobStats():android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats$Timer>");
        }

        public android.util.SparseArray<? extends android.os.BatteryStats.Uid.Sensor> getSensorStats() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mSensorStats;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getSensorStats():android.util.SparseArray<? extends android.os.BatteryStats$Uid$Sensor>");
        }

        public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Uid.Proc> getProcessStats() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mProcessStats;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getProcessStats():android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats$Uid$Proc>");
        }

        public android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats.Uid.Pkg> getPackageStats() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mPackageStats;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getPackageStats():android.util.ArrayMap<java.lang.String, ? extends android.os.BatteryStats$Uid$Pkg>");
        }

        public int getUid() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mUid;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getUid():int");
        }

        public void noteWifiRunningLocked(long r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r5 = this;
            r0 = r5.mWifiRunning;
            if (r0 != 0) goto L_0x0020;
        L_0x0004:
            r0 = 1;
            r5.mWifiRunning = r0;
            r0 = r5.mWifiRunningTimer;
            if (r0 != 0) goto L_0x001b;
        L_0x000b:
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = 4;
            r2 = com.android.internal.os.BatteryStatsImpl.this;
            r2 = r2.mWifiRunningTimers;
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mOnBatteryTimeBase;
            r0.<init>(r5, r1, r2, r3);
            r5.mWifiRunningTimer = r0;
        L_0x001b:
            r0 = r5.mWifiRunningTimer;
            r0.startRunningLocked(r6);
        L_0x0020:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteWifiRunningLocked(long):void");
        }

        public void noteWifiStoppedLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mWifiRunning;
            if (r0 == 0) goto L_0x000c;
        L_0x0004:
            r0 = 0;
            r1.mWifiRunning = r0;
            r0 = r1.mWifiRunningTimer;
            r0.stopRunningLocked(r2);
        L_0x000c:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteWifiStoppedLocked(long):void");
        }

        public void noteFullWifiLockAcquiredLocked(long r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r5 = this;
            r0 = r5.mFullWifiLockOut;
            if (r0 != 0) goto L_0x0020;
        L_0x0004:
            r0 = 1;
            r5.mFullWifiLockOut = r0;
            r0 = r5.mFullWifiLockTimer;
            if (r0 != 0) goto L_0x001b;
        L_0x000b:
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = 5;
            r2 = com.android.internal.os.BatteryStatsImpl.this;
            r2 = r2.mFullWifiLockTimers;
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mOnBatteryTimeBase;
            r0.<init>(r5, r1, r2, r3);
            r5.mFullWifiLockTimer = r0;
        L_0x001b:
            r0 = r5.mFullWifiLockTimer;
            r0.startRunningLocked(r6);
        L_0x0020:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteFullWifiLockAcquiredLocked(long):void");
        }

        public void noteFullWifiLockReleasedLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mFullWifiLockOut;
            if (r0 == 0) goto L_0x000c;
        L_0x0004:
            r0 = 0;
            r1.mFullWifiLockOut = r0;
            r0 = r1.mFullWifiLockTimer;
            r0.stopRunningLocked(r2);
        L_0x000c:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteFullWifiLockReleasedLocked(long):void");
        }

        public void noteWifiScanStartedLocked(long r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r5 = this;
            r0 = r5.mWifiScanStarted;
            if (r0 != 0) goto L_0x0020;
        L_0x0004:
            r0 = 1;
            r5.mWifiScanStarted = r0;
            r0 = r5.mWifiScanTimer;
            if (r0 != 0) goto L_0x001b;
        L_0x000b:
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = 6;
            r2 = com.android.internal.os.BatteryStatsImpl.this;
            r2 = r2.mWifiScanTimers;
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mOnBatteryTimeBase;
            r0.<init>(r5, r1, r2, r3);
            r5.mWifiScanTimer = r0;
        L_0x001b:
            r0 = r5.mWifiScanTimer;
            r0.startRunningLocked(r6);
        L_0x0020:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteWifiScanStartedLocked(long):void");
        }

        public void noteWifiScanStoppedLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mWifiScanStarted;
            if (r0 == 0) goto L_0x000c;
        L_0x0004:
            r0 = 0;
            r1.mWifiScanStarted = r0;
            r0 = r1.mWifiScanTimer;
            r0.stopRunningLocked(r2);
        L_0x000c:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteWifiScanStoppedLocked(long):void");
        }

        public void noteWifiBatchedScanStartedLocked(int r5, long r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            r0 = 0;
        L_0x0001:
            r1 = 8;
            if (r5 <= r1) goto L_0x000d;
        L_0x0005:
            r1 = 4;
            if (r0 >= r1) goto L_0x000d;
        L_0x0008:
            r5 = r5 >> 3;
            r0 = r0 + 1;
            goto L_0x0001;
        L_0x000d:
            r1 = r4.mWifiBatchedScanBinStarted;
            if (r1 != r0) goto L_0x0012;
        L_0x0011:
            return;
        L_0x0012:
            r1 = r4.mWifiBatchedScanBinStarted;
            r2 = -1;
            if (r1 == r2) goto L_0x0020;
        L_0x0017:
            r1 = r4.mWifiBatchedScanTimer;
            r2 = r4.mWifiBatchedScanBinStarted;
            r1 = r1[r2];
            r1.stopRunningLocked(r6);
        L_0x0020:
            r4.mWifiBatchedScanBinStarted = r0;
            r1 = r4.mWifiBatchedScanTimer;
            r1 = r1[r0];
            if (r1 != 0) goto L_0x002c;
        L_0x0028:
            r1 = 0;
            r4.makeWifiBatchedScanBin(r0, r1);
        L_0x002c:
            r1 = r4.mWifiBatchedScanTimer;
            r1 = r1[r0];
            r1.startRunningLocked(r6);
            goto L_0x0011;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteWifiBatchedScanStartedLocked(int, long):void");
        }

        public void noteWifiBatchedScanStoppedLocked(long r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r3 = this;
            r2 = -1;
            r0 = r3.mWifiBatchedScanBinStarted;
            if (r0 == r2) goto L_0x0010;
        L_0x0005:
            r0 = r3.mWifiBatchedScanTimer;
            r1 = r3.mWifiBatchedScanBinStarted;
            r0 = r0[r1];
            r0.stopRunningLocked(r4);
            r3.mWifiBatchedScanBinStarted = r2;
        L_0x0010:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteWifiBatchedScanStoppedLocked(long):void");
        }

        public void noteWifiMulticastEnabledLocked(long r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r5 = this;
            r0 = r5.mWifiMulticastEnabled;
            if (r0 != 0) goto L_0x0020;
        L_0x0004:
            r0 = 1;
            r5.mWifiMulticastEnabled = r0;
            r0 = r5.mWifiMulticastTimer;
            if (r0 != 0) goto L_0x001b;
        L_0x000b:
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = 7;
            r2 = com.android.internal.os.BatteryStatsImpl.this;
            r2 = r2.mWifiMulticastTimers;
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mOnBatteryTimeBase;
            r0.<init>(r5, r1, r2, r3);
            r5.mWifiMulticastTimer = r0;
        L_0x001b:
            r0 = r5.mWifiMulticastTimer;
            r0.startRunningLocked(r6);
        L_0x0020:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteWifiMulticastEnabledLocked(long):void");
        }

        public void noteWifiMulticastDisabledLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mWifiMulticastEnabled;
            if (r0 == 0) goto L_0x000c;
        L_0x0004:
            r0 = 0;
            r1.mWifiMulticastEnabled = r0;
            r0 = r1.mWifiMulticastTimer;
            r0.stopRunningLocked(r2);
        L_0x000c:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteWifiMulticastDisabledLocked(long):void");
        }

        public void noteWifiControllerActivityLocked(int r5, long r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            r0 = r4.mWifiControllerTime;
            r0 = r0[r5];
            if (r0 != 0) goto L_0x0013;
        L_0x0006:
            r0 = r4.mWifiControllerTime;
            r1 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r2 = com.android.internal.os.BatteryStatsImpl.this;
            r2 = r2.mOnBatteryTimeBase;
            r1.<init>(r2);
            r0[r5] = r1;
        L_0x0013:
            r0 = r4.mWifiControllerTime;
            r0 = r0[r5];
            r0.addCountLocked(r6);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteWifiControllerActivityLocked(int, long):void");
        }

        public com.android.internal.os.BatteryStatsImpl.StopwatchTimer createAudioTurnedOnTimerLocked() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            r0 = r4.mAudioTurnedOnTimer;
            if (r0 != 0) goto L_0x0015;
        L_0x0004:
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = 15;
            r2 = com.android.internal.os.BatteryStatsImpl.this;
            r2 = r2.mAudioTurnedOnTimers;
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mOnBatteryTimeBase;
            r0.<init>(r4, r1, r2, r3);
            r4.mAudioTurnedOnTimer = r0;
        L_0x0015:
            r0 = r4.mAudioTurnedOnTimer;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.createAudioTurnedOnTimerLocked():com.android.internal.os.BatteryStatsImpl$StopwatchTimer");
        }

        public void noteAudioTurnedOnLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.createAudioTurnedOnTimerLocked();
            r0.startRunningLocked(r2);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteAudioTurnedOnLocked(long):void");
        }

        public void noteAudioTurnedOffLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mAudioTurnedOnTimer;
            if (r0 == 0) goto L_0x0009;
        L_0x0004:
            r0 = r1.mAudioTurnedOnTimer;
            r0.stopRunningLocked(r2);
        L_0x0009:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteAudioTurnedOffLocked(long):void");
        }

        public void noteResetAudioLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mAudioTurnedOnTimer;
            if (r0 == 0) goto L_0x0009;
        L_0x0004:
            r0 = r1.mAudioTurnedOnTimer;
            r0.stopAllRunningLocked(r2);
        L_0x0009:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteResetAudioLocked(long):void");
        }

        public com.android.internal.os.BatteryStatsImpl.StopwatchTimer createVideoTurnedOnTimerLocked() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            r0 = r4.mVideoTurnedOnTimer;
            if (r0 != 0) goto L_0x0015;
        L_0x0004:
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = 8;
            r2 = com.android.internal.os.BatteryStatsImpl.this;
            r2 = r2.mVideoTurnedOnTimers;
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mOnBatteryTimeBase;
            r0.<init>(r4, r1, r2, r3);
            r4.mVideoTurnedOnTimer = r0;
        L_0x0015:
            r0 = r4.mVideoTurnedOnTimer;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.createVideoTurnedOnTimerLocked():com.android.internal.os.BatteryStatsImpl$StopwatchTimer");
        }

        public void noteVideoTurnedOnLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.createVideoTurnedOnTimerLocked();
            r0.startRunningLocked(r2);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteVideoTurnedOnLocked(long):void");
        }

        public void noteVideoTurnedOffLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mVideoTurnedOnTimer;
            if (r0 == 0) goto L_0x0009;
        L_0x0004:
            r0 = r1.mVideoTurnedOnTimer;
            r0.stopRunningLocked(r2);
        L_0x0009:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteVideoTurnedOffLocked(long):void");
        }

        public void noteResetVideoLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mVideoTurnedOnTimer;
            if (r0 == 0) goto L_0x0009;
        L_0x0004:
            r0 = r1.mVideoTurnedOnTimer;
            r0.stopAllRunningLocked(r2);
        L_0x0009:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteResetVideoLocked(long):void");
        }

        public com.android.internal.os.BatteryStatsImpl.StopwatchTimer createFlashlightTurnedOnTimerLocked() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            r0 = r4.mFlashlightTurnedOnTimer;
            if (r0 != 0) goto L_0x0015;
        L_0x0004:
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = 16;
            r2 = com.android.internal.os.BatteryStatsImpl.this;
            r2 = r2.mFlashlightTurnedOnTimers;
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mOnBatteryTimeBase;
            r0.<init>(r4, r1, r2, r3);
            r4.mFlashlightTurnedOnTimer = r0;
        L_0x0015:
            r0 = r4.mFlashlightTurnedOnTimer;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.createFlashlightTurnedOnTimerLocked():com.android.internal.os.BatteryStatsImpl$StopwatchTimer");
        }

        public void noteFlashlightTurnedOnLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.createFlashlightTurnedOnTimerLocked();
            r0.startRunningLocked(r2);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteFlashlightTurnedOnLocked(long):void");
        }

        public void noteFlashlightTurnedOffLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mFlashlightTurnedOnTimer;
            if (r0 == 0) goto L_0x0009;
        L_0x0004:
            r0 = r1.mFlashlightTurnedOnTimer;
            r0.stopRunningLocked(r2);
        L_0x0009:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteFlashlightTurnedOffLocked(long):void");
        }

        public void noteResetFlashlightLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mFlashlightTurnedOnTimer;
            if (r0 == 0) goto L_0x0009;
        L_0x0004:
            r0 = r1.mFlashlightTurnedOnTimer;
            r0.stopAllRunningLocked(r2);
        L_0x0009:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteResetFlashlightLocked(long):void");
        }

        public com.android.internal.os.BatteryStatsImpl.StopwatchTimer createCameraTurnedOnTimerLocked() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            r0 = r4.mCameraTurnedOnTimer;
            if (r0 != 0) goto L_0x0015;
        L_0x0004:
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = 17;
            r2 = com.android.internal.os.BatteryStatsImpl.this;
            r2 = r2.mCameraTurnedOnTimers;
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mOnBatteryTimeBase;
            r0.<init>(r4, r1, r2, r3);
            r4.mCameraTurnedOnTimer = r0;
        L_0x0015:
            r0 = r4.mCameraTurnedOnTimer;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.createCameraTurnedOnTimerLocked():com.android.internal.os.BatteryStatsImpl$StopwatchTimer");
        }

        public void noteCameraTurnedOnLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.createCameraTurnedOnTimerLocked();
            r0.startRunningLocked(r2);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteCameraTurnedOnLocked(long):void");
        }

        public void noteCameraTurnedOffLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mCameraTurnedOnTimer;
            if (r0 == 0) goto L_0x0009;
        L_0x0004:
            r0 = r1.mCameraTurnedOnTimer;
            r0.stopRunningLocked(r2);
        L_0x0009:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteCameraTurnedOffLocked(long):void");
        }

        public void noteResetCameraLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mCameraTurnedOnTimer;
            if (r0 == 0) goto L_0x0009;
        L_0x0004:
            r0 = r1.mCameraTurnedOnTimer;
            r0.stopAllRunningLocked(r2);
        L_0x0009:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteResetCameraLocked(long):void");
        }

        public com.android.internal.os.BatteryStatsImpl.StopwatchTimer createForegroundActivityTimerLocked() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            r0 = r4.mForegroundActivityTimer;
            if (r0 != 0) goto L_0x0012;
        L_0x0004:
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = 10;
            r2 = 0;
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mOnBatteryTimeBase;
            r0.<init>(r4, r1, r2, r3);
            r4.mForegroundActivityTimer = r0;
        L_0x0012:
            r0 = r4.mForegroundActivityTimer;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.createForegroundActivityTimerLocked():com.android.internal.os.BatteryStatsImpl$StopwatchTimer");
        }

        public void noteActivityResumedLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.createForegroundActivityTimerLocked();
            r0.startRunningLocked(r2);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteActivityResumedLocked(long):void");
        }

        public void noteActivityPausedLocked(long r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mForegroundActivityTimer;
            if (r0 == 0) goto L_0x0009;
        L_0x0004:
            r0 = r1.mForegroundActivityTimer;
            r0.stopRunningLocked(r2);
        L_0x0009:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteActivityPausedLocked(long):void");
        }

        void updateUidProcessStateLocked(int r5, long r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            r2 = 3;
            r0 = r4.mProcessState;
            if (r0 != r5) goto L_0x0006;
        L_0x0005:
            return;
        L_0x0006:
            r0 = r4.mProcessState;
            if (r0 == r2) goto L_0x0013;
        L_0x000a:
            r0 = r4.mProcessStateTimer;
            r1 = r4.mProcessState;
            r0 = r0[r1];
            r0.stopRunningLocked(r6);
        L_0x0013:
            r4.mProcessState = r5;
            if (r5 == r2) goto L_0x0005;
        L_0x0017:
            r0 = r4.mProcessStateTimer;
            r0 = r0[r5];
            if (r0 != 0) goto L_0x0021;
        L_0x001d:
            r0 = 0;
            r4.makeProcessState(r5, r0);
        L_0x0021:
            r0 = r4.mProcessStateTimer;
            r0 = r0[r5];
            r0.startRunningLocked(r6);
            goto L_0x0005;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.updateUidProcessStateLocked(int, long):void");
        }

        public com.android.internal.os.BatteryStatsImpl.BatchTimer createVibratorOnTimerLocked() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r3 = this;
            r0 = r3.mVibratorOnTimer;
            if (r0 != 0) goto L_0x0011;
        L_0x0004:
            r0 = new com.android.internal.os.BatteryStatsImpl$BatchTimer;
            r1 = 9;
            r2 = com.android.internal.os.BatteryStatsImpl.this;
            r2 = r2.mOnBatteryTimeBase;
            r0.<init>(r3, r1, r2);
            r3.mVibratorOnTimer = r0;
        L_0x0011:
            r0 = r3.mVibratorOnTimer;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.createVibratorOnTimerLocked():com.android.internal.os.BatteryStatsImpl$BatchTimer");
        }

        public void noteVibratorOnLocked(long r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r3 = this;
            r0 = r3.createVibratorOnTimerLocked();
            r1 = com.android.internal.os.BatteryStatsImpl.this;
            r0.addDuration(r1, r4);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteVibratorOnLocked(long):void");
        }

        public void noteVibratorOffLocked() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r0 = r2.mVibratorOnTimer;
            if (r0 == 0) goto L_0x000b;
        L_0x0004:
            r0 = r2.mVibratorOnTimer;
            r1 = com.android.internal.os.BatteryStatsImpl.this;
            r0.abortLastDuration(r1);
        L_0x000b:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteVibratorOffLocked():void");
        }

        public long getWifiRunningTime(long r4, int r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r3 = this;
            r0 = r3.mWifiRunningTimer;
            if (r0 != 0) goto L_0x0007;
        L_0x0004:
            r0 = 0;
        L_0x0006:
            return r0;
        L_0x0007:
            r0 = r3.mWifiRunningTimer;
            r0 = r0.getTotalTimeLocked(r4, r6);
            goto L_0x0006;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getWifiRunningTime(long, int):long");
        }

        public long getFullWifiLockTime(long r4, int r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r3 = this;
            r0 = r3.mFullWifiLockTimer;
            if (r0 != 0) goto L_0x0007;
        L_0x0004:
            r0 = 0;
        L_0x0006:
            return r0;
        L_0x0007:
            r0 = r3.mFullWifiLockTimer;
            r0 = r0.getTotalTimeLocked(r4, r6);
            goto L_0x0006;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getFullWifiLockTime(long, int):long");
        }

        public long getWifiScanTime(long r4, int r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r3 = this;
            r0 = r3.mWifiScanTimer;
            if (r0 != 0) goto L_0x0007;
        L_0x0004:
            r0 = 0;
        L_0x0006:
            return r0;
        L_0x0007:
            r0 = r3.mWifiScanTimer;
            r0 = r0.getTotalTimeLocked(r4, r6);
            goto L_0x0006;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getWifiScanTime(long, int):long");
        }

        public int getWifiScanCount(int r2) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mWifiScanTimer;
            if (r0 != 0) goto L_0x0006;
        L_0x0004:
            r0 = 0;
        L_0x0005:
            return r0;
        L_0x0006:
            r0 = r1.mWifiScanTimer;
            r0 = r0.getCountLocked(r2);
            goto L_0x0005;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getWifiScanCount(int):int");
        }

        public long getWifiBatchedScanTime(int r5, long r6, int r8) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            r0 = 0;
            if (r5 < 0) goto L_0x0007;
        L_0x0004:
            r2 = 5;
            if (r5 < r2) goto L_0x0008;
        L_0x0007:
            return r0;
        L_0x0008:
            r2 = r4.mWifiBatchedScanTimer;
            r2 = r2[r5];
            if (r2 == 0) goto L_0x0007;
        L_0x000e:
            r0 = r4.mWifiBatchedScanTimer;
            r0 = r0[r5];
            r0 = r0.getTotalTimeLocked(r6, r8);
            goto L_0x0007;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getWifiBatchedScanTime(int, long, int):long");
        }

        public int getWifiBatchedScanCount(int r3, int r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r0 = 0;
            if (r3 < 0) goto L_0x0006;
        L_0x0003:
            r1 = 5;
            if (r3 < r1) goto L_0x0007;
        L_0x0006:
            return r0;
        L_0x0007:
            r1 = r2.mWifiBatchedScanTimer;
            r1 = r1[r3];
            if (r1 == 0) goto L_0x0006;
        L_0x000d:
            r0 = r2.mWifiBatchedScanTimer;
            r0 = r0[r3];
            r0 = r0.getCountLocked(r4);
            goto L_0x0006;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getWifiBatchedScanCount(int, int):int");
        }

        public long getWifiMulticastTime(long r4, int r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r3 = this;
            r0 = r3.mWifiMulticastTimer;
            if (r0 != 0) goto L_0x0007;
        L_0x0004:
            r0 = 0;
        L_0x0006:
            return r0;
        L_0x0007:
            r0 = r3.mWifiMulticastTimer;
            r0 = r0.getTotalTimeLocked(r4, r6);
            goto L_0x0006;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getWifiMulticastTime(long, int):long");
        }

        public com.android.internal.os.BatteryStatsImpl.Timer getAudioTurnedOnTimer() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mAudioTurnedOnTimer;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getAudioTurnedOnTimer():com.android.internal.os.BatteryStatsImpl$Timer");
        }

        public com.android.internal.os.BatteryStatsImpl.Timer getVideoTurnedOnTimer() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mVideoTurnedOnTimer;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getVideoTurnedOnTimer():com.android.internal.os.BatteryStatsImpl$Timer");
        }

        public com.android.internal.os.BatteryStatsImpl.Timer getFlashlightTurnedOnTimer() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mFlashlightTurnedOnTimer;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getFlashlightTurnedOnTimer():com.android.internal.os.BatteryStatsImpl$Timer");
        }

        public com.android.internal.os.BatteryStatsImpl.Timer getCameraTurnedOnTimer() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mCameraTurnedOnTimer;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getCameraTurnedOnTimer():com.android.internal.os.BatteryStatsImpl$Timer");
        }

        public com.android.internal.os.BatteryStatsImpl.Timer getForegroundActivityTimer() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mForegroundActivityTimer;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getForegroundActivityTimer():com.android.internal.os.BatteryStatsImpl$Timer");
        }

        void makeProcessState(int r8, android.os.Parcel r9) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r7 = this;
            r3 = 0;
            r2 = 12;
            if (r8 < 0) goto L_0x0008;
        L_0x0005:
            r0 = 3;
            if (r8 < r0) goto L_0x0009;
        L_0x0008:
            return;
        L_0x0009:
            if (r9 != 0) goto L_0x0019;
        L_0x000b:
            r0 = r7.mProcessStateTimer;
            r1 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r4 = com.android.internal.os.BatteryStatsImpl.this;
            r4 = r4.mOnBatteryTimeBase;
            r1.<init>(r7, r2, r3, r4);
            r0[r8] = r1;
            goto L_0x0008;
        L_0x0019:
            r6 = r7.mProcessStateTimer;
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = com.android.internal.os.BatteryStatsImpl.this;
            r4 = r1.mOnBatteryTimeBase;
            r1 = r7;
            r5 = r9;
            r0.<init>(r1, r2, r3, r4, r5);
            r6[r8] = r0;
            goto L_0x0008;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.makeProcessState(int, android.os.Parcel):void");
        }

        public long getProcessStateTime(int r5, long r6, int r8) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            r0 = 0;
            if (r5 < 0) goto L_0x0007;
        L_0x0004:
            r2 = 3;
            if (r5 < r2) goto L_0x0008;
        L_0x0007:
            return r0;
        L_0x0008:
            r2 = r4.mProcessStateTimer;
            r2 = r2[r5];
            if (r2 == 0) goto L_0x0007;
        L_0x000e:
            r0 = r4.mProcessStateTimer;
            r0 = r0[r5];
            r0 = r0.getTotalTimeLocked(r6, r8);
            goto L_0x0007;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getProcessStateTime(int, long, int):long");
        }

        public com.android.internal.os.BatteryStatsImpl.Timer getVibratorOnTimer() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mVibratorOnTimer;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getVibratorOnTimer():com.android.internal.os.BatteryStatsImpl$Timer");
        }

        public void noteUserActivityLocked(int r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r3 = this;
            r0 = r3.mUserActivityCounters;
            if (r0 != 0) goto L_0x0007;
        L_0x0004:
            r3.initUserActivityLocked();
        L_0x0007:
            if (r4 < 0) goto L_0x0014;
        L_0x0009:
            r0 = 3;
            if (r4 >= r0) goto L_0x0014;
        L_0x000c:
            r0 = r3.mUserActivityCounters;
            r0 = r0[r4];
            r0.stepAtomic();
        L_0x0013:
            return;
        L_0x0014:
            r0 = "BatteryStatsImpl";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "Unknown user activity type ";
            r1 = r1.append(r2);
            r1 = r1.append(r4);
            r2 = " was specified.";
            r1 = r1.append(r2);
            r1 = r1.toString();
            r2 = new java.lang.Throwable;
            r2.<init>();
            android.util.Slog.w(r0, r1, r2);
            goto L_0x0013;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteUserActivityLocked(int):void");
        }

        public boolean hasUserActivity() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mUserActivityCounters;
            if (r0 == 0) goto L_0x0006;
        L_0x0004:
            r0 = 1;
        L_0x0005:
            return r0;
        L_0x0006:
            r0 = 0;
            goto L_0x0005;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.hasUserActivity():boolean");
        }

        public int getUserActivityCount(int r2, int r3) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mUserActivityCounters;
            if (r0 != 0) goto L_0x0006;
        L_0x0004:
            r0 = 0;
        L_0x0005:
            return r0;
        L_0x0006:
            r0 = r1.mUserActivityCounters;
            r0 = r0[r2];
            r0 = r0.getCountLocked(r3);
            goto L_0x0005;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getUserActivityCount(int, int):int");
        }

        void makeWifiBatchedScanBin(int r8, android.os.Parcel r9) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r7 = this;
            r2 = 11;
            if (r8 < 0) goto L_0x0007;
        L_0x0004:
            r0 = 5;
            if (r8 < r0) goto L_0x0008;
        L_0x0007:
            return;
        L_0x0008:
            r0 = com.android.internal.os.BatteryStatsImpl.this;
            r0 = r0.mWifiBatchedScanTimers;
            r3 = r0.get(r8);
            r3 = (java.util.ArrayList) r3;
            if (r3 != 0) goto L_0x0020;
        L_0x0014:
            r3 = new java.util.ArrayList;
            r3.<init>();
            r0 = com.android.internal.os.BatteryStatsImpl.this;
            r0 = r0.mWifiBatchedScanTimers;
            r0.put(r8, r3);
        L_0x0020:
            if (r9 != 0) goto L_0x0030;
        L_0x0022:
            r0 = r7.mWifiBatchedScanTimer;
            r1 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r4 = com.android.internal.os.BatteryStatsImpl.this;
            r4 = r4.mOnBatteryTimeBase;
            r1.<init>(r7, r2, r3, r4);
            r0[r8] = r1;
            goto L_0x0007;
        L_0x0030:
            r6 = r7.mWifiBatchedScanTimer;
            r0 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r1 = com.android.internal.os.BatteryStatsImpl.this;
            r4 = r1.mOnBatteryTimeBase;
            r1 = r7;
            r5 = r9;
            r0.<init>(r1, r2, r3, r4, r5);
            r6[r8] = r0;
            goto L_0x0007;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.makeWifiBatchedScanBin(int, android.os.Parcel):void");
        }

        void initUserActivityLocked() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r5 = this;
            r4 = 3;
            r1 = new com.android.internal.os.BatteryStatsImpl.Counter[r4];
            r5.mUserActivityCounters = r1;
            r0 = 0;
        L_0x0006:
            if (r0 >= r4) goto L_0x0018;
        L_0x0008:
            r1 = r5.mUserActivityCounters;
            r2 = new com.android.internal.os.BatteryStatsImpl$Counter;
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mOnBatteryTimeBase;
            r2.<init>(r3);
            r1[r0] = r2;
            r0 = r0 + 1;
            goto L_0x0006;
        L_0x0018:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.initUserActivityLocked():void");
        }

        void noteNetworkActivityLocked(int r5, long r6, long r8) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            r0 = r4.mNetworkByteActivityCounters;
            if (r0 != 0) goto L_0x0007;
        L_0x0004:
            r4.initNetworkActivityLocked();
        L_0x0007:
            if (r5 < 0) goto L_0x001b;
        L_0x0009:
            r0 = 4;
            if (r5 >= r0) goto L_0x001b;
        L_0x000c:
            r0 = r4.mNetworkByteActivityCounters;
            r0 = r0[r5];
            r0.addCountLocked(r6);
            r0 = r4.mNetworkPacketActivityCounters;
            r0 = r0[r5];
            r0.addCountLocked(r8);
        L_0x001a:
            return;
        L_0x001b:
            r0 = "BatteryStatsImpl";
            r1 = new java.lang.StringBuilder;
            r1.<init>();
            r2 = "Unknown network activity type ";
            r1 = r1.append(r2);
            r1 = r1.append(r5);
            r2 = " was specified.";
            r1 = r1.append(r2);
            r1 = r1.toString();
            r2 = new java.lang.Throwable;
            r2.<init>();
            android.util.Slog.w(r0, r1, r2);
            goto L_0x001a;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteNetworkActivityLocked(int, long, long):void");
        }

        void noteMobileRadioActiveTimeLocked(long r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r5 = this;
            r0 = r5.mNetworkByteActivityCounters;
            if (r0 != 0) goto L_0x0007;
        L_0x0004:
            r5.initNetworkActivityLocked();
        L_0x0007:
            r0 = r5.mMobileRadioActiveTime;
            r0.addCountLocked(r6);
            r0 = r5.mMobileRadioActiveCount;
            r2 = 1;
            r0.addCountLocked(r2);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteMobileRadioActiveTimeLocked(long):void");
        }

        public boolean hasNetworkActivity() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mNetworkByteActivityCounters;
            if (r0 == 0) goto L_0x0006;
        L_0x0004:
            r0 = 1;
        L_0x0005:
            return r0;
        L_0x0006:
            r0 = 0;
            goto L_0x0005;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.hasNetworkActivity():boolean");
        }

        public long getNetworkActivityBytes(int r3, int r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r0 = r2.mNetworkByteActivityCounters;
            if (r0 == 0) goto L_0x0014;
        L_0x0004:
            if (r3 < 0) goto L_0x0014;
        L_0x0006:
            r0 = r2.mNetworkByteActivityCounters;
            r0 = r0.length;
            if (r3 >= r0) goto L_0x0014;
        L_0x000b:
            r0 = r2.mNetworkByteActivityCounters;
            r0 = r0[r3];
            r0 = r0.getCountLocked(r4);
        L_0x0013:
            return r0;
        L_0x0014:
            r0 = 0;
            goto L_0x0013;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getNetworkActivityBytes(int, int):long");
        }

        public long getNetworkActivityPackets(int r3, int r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r0 = r2.mNetworkPacketActivityCounters;
            if (r0 == 0) goto L_0x0014;
        L_0x0004:
            if (r3 < 0) goto L_0x0014;
        L_0x0006:
            r0 = r2.mNetworkPacketActivityCounters;
            r0 = r0.length;
            if (r3 >= r0) goto L_0x0014;
        L_0x000b:
            r0 = r2.mNetworkPacketActivityCounters;
            r0 = r0[r3];
            r0 = r0.getCountLocked(r4);
        L_0x0013:
            return r0;
        L_0x0014:
            r0 = 0;
            goto L_0x0013;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getNetworkActivityPackets(int, int):long");
        }

        public long getMobileRadioActiveTime(int r3) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r0 = r2.mMobileRadioActiveTime;
            if (r0 == 0) goto L_0x000b;
        L_0x0004:
            r0 = r2.mMobileRadioActiveTime;
            r0 = r0.getCountLocked(r3);
        L_0x000a:
            return r0;
        L_0x000b:
            r0 = 0;
            goto L_0x000a;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getMobileRadioActiveTime(int):long");
        }

        public int getMobileRadioActiveCount(int r3) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r0 = r2.mMobileRadioActiveCount;
            if (r0 == 0) goto L_0x000c;
        L_0x0004:
            r0 = r2.mMobileRadioActiveCount;
            r0 = r0.getCountLocked(r3);
            r0 = (int) r0;
        L_0x000b:
            return r0;
        L_0x000c:
            r0 = 0;
            goto L_0x000b;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getMobileRadioActiveCount(int):int");
        }

        public long getUserCpuTimeUs(int r3) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r0 = r2.mUserCpuTime;
            r0 = r0.getCountLocked(r3);
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getUserCpuTimeUs(int):long");
        }

        public long getSystemCpuTimeUs(int r3) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r0 = r2.mSystemCpuTime;
            r0 = r0.getCountLocked(r3);
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getSystemCpuTimeUs(int):long");
        }

        public long getCpuPowerMaUs(int r3) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r0 = r2.mCpuPower;
            r0 = r0.getCountLocked(r3);
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getCpuPowerMaUs(int):long");
        }

        public long getTimeAtCpuSpeed(int r5, int r6, int r7) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            r2 = r4.mCpuClusterSpeed;
            if (r2 == 0) goto L_0x001f;
        L_0x0004:
            if (r5 < 0) goto L_0x001f;
        L_0x0006:
            r2 = r4.mCpuClusterSpeed;
            r2 = r2.length;
            if (r5 >= r2) goto L_0x001f;
        L_0x000b:
            r2 = r4.mCpuClusterSpeed;
            r1 = r2[r5];
            if (r1 == 0) goto L_0x001f;
        L_0x0011:
            if (r6 < 0) goto L_0x001f;
        L_0x0013:
            r2 = r1.length;
            if (r6 >= r2) goto L_0x001f;
        L_0x0016:
            r0 = r1[r6];
            if (r0 == 0) goto L_0x001f;
        L_0x001a:
            r2 = r0.getCountLocked(r7);
        L_0x001e:
            return r2;
        L_0x001f:
            r2 = 0;
            goto L_0x001e;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getTimeAtCpuSpeed(int, int, int):long");
        }

        public long getWifiControllerActivity(int r3, int r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            if (r3 < 0) goto L_0x0014;
        L_0x0002:
            r0 = 4;
            if (r3 >= r0) goto L_0x0014;
        L_0x0005:
            r0 = r2.mWifiControllerTime;
            r0 = r0[r3];
            if (r0 == 0) goto L_0x0014;
        L_0x000b:
            r0 = r2.mWifiControllerTime;
            r0 = r0[r3];
            r0 = r0.getCountLocked(r4);
        L_0x0013:
            return r0;
        L_0x0014:
            r0 = 0;
            goto L_0x0013;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getWifiControllerActivity(int, int):long");
        }

        void initNetworkActivityLocked() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r5 = this;
            r4 = 4;
            r1 = new com.android.internal.os.BatteryStatsImpl.LongSamplingCounter[r4];
            r5.mNetworkByteActivityCounters = r1;
            r1 = new com.android.internal.os.BatteryStatsImpl.LongSamplingCounter[r4];
            r5.mNetworkPacketActivityCounters = r1;
            r0 = 0;
        L_0x000a:
            if (r0 >= r4) goto L_0x0029;
        L_0x000c:
            r1 = r5.mNetworkByteActivityCounters;
            r2 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mOnBatteryTimeBase;
            r2.<init>(r3);
            r1[r0] = r2;
            r1 = r5.mNetworkPacketActivityCounters;
            r2 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mOnBatteryTimeBase;
            r2.<init>(r3);
            r1[r0] = r2;
            r0 = r0 + 1;
            goto L_0x000a;
        L_0x0029:
            r1 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r2 = com.android.internal.os.BatteryStatsImpl.this;
            r2 = r2.mOnBatteryTimeBase;
            r1.<init>(r2);
            r5.mMobileRadioActiveTime = r1;
            r1 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r2 = com.android.internal.os.BatteryStatsImpl.this;
            r2 = r2.mOnBatteryTimeBase;
            r1.<init>(r2);
            r5.mMobileRadioActiveCount = r1;
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.initNetworkActivityLocked():void");
        }

        boolean reset() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r36 = this;
            r4 = 0;
            r0 = r36;
            r0 = r0.mWifiRunningTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x0023;
        L_0x0009:
            r0 = r36;
            r0 = r0.mWifiRunningTimer;
            r34 = r0;
            r35 = 0;
            r34 = r34.reset(r35);
            if (r34 != 0) goto L_0x0097;
        L_0x0017:
            r34 = 1;
        L_0x0019:
            r4 = r4 | r34;
            r0 = r36;
            r0 = r0.mWifiRunning;
            r34 = r0;
            r4 = r4 | r34;
        L_0x0023:
            r0 = r36;
            r0 = r0.mFullWifiLockTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x0045;
        L_0x002b:
            r0 = r36;
            r0 = r0.mFullWifiLockTimer;
            r34 = r0;
            r35 = 0;
            r34 = r34.reset(r35);
            if (r34 != 0) goto L_0x009a;
        L_0x0039:
            r34 = 1;
        L_0x003b:
            r4 = r4 | r34;
            r0 = r36;
            r0 = r0.mFullWifiLockOut;
            r34 = r0;
            r4 = r4 | r34;
        L_0x0045:
            r0 = r36;
            r0 = r0.mWifiScanTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x0067;
        L_0x004d:
            r0 = r36;
            r0 = r0.mWifiScanTimer;
            r34 = r0;
            r35 = 0;
            r34 = r34.reset(r35);
            if (r34 != 0) goto L_0x009d;
        L_0x005b:
            r34 = 1;
        L_0x005d:
            r4 = r4 | r34;
            r0 = r36;
            r0 = r0.mWifiScanStarted;
            r34 = r0;
            r4 = r4 | r34;
        L_0x0067:
            r0 = r36;
            r0 = r0.mWifiBatchedScanTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x00b5;
        L_0x006f:
            r9 = 0;
        L_0x0070:
            r34 = 5;
            r0 = r34;
            if (r9 >= r0) goto L_0x00a3;
        L_0x0076:
            r0 = r36;
            r0 = r0.mWifiBatchedScanTimer;
            r34 = r0;
            r34 = r34[r9];
            if (r34 == 0) goto L_0x0094;
        L_0x0080:
            r0 = r36;
            r0 = r0.mWifiBatchedScanTimer;
            r34 = r0;
            r34 = r34[r9];
            r35 = 0;
            r34 = r34.reset(r35);
            if (r34 != 0) goto L_0x00a0;
        L_0x0090:
            r34 = 1;
        L_0x0092:
            r4 = r4 | r34;
        L_0x0094:
            r9 = r9 + 1;
            goto L_0x0070;
        L_0x0097:
            r34 = 0;
            goto L_0x0019;
        L_0x009a:
            r34 = 0;
            goto L_0x003b;
        L_0x009d:
            r34 = 0;
            goto L_0x005d;
        L_0x00a0:
            r34 = 0;
            goto L_0x0092;
        L_0x00a3:
            r0 = r36;
            r0 = r0.mWifiBatchedScanBinStarted;
            r34 = r0;
            r35 = -1;
            r0 = r34;
            r1 = r35;
            if (r0 == r1) goto L_0x0189;
        L_0x00b1:
            r34 = 1;
        L_0x00b3:
            r4 = r4 | r34;
        L_0x00b5:
            r0 = r36;
            r0 = r0.mWifiMulticastTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x00d7;
        L_0x00bd:
            r0 = r36;
            r0 = r0.mWifiMulticastTimer;
            r34 = r0;
            r35 = 0;
            r34 = r34.reset(r35);
            if (r34 != 0) goto L_0x018d;
        L_0x00cb:
            r34 = 1;
        L_0x00cd:
            r4 = r4 | r34;
            r0 = r36;
            r0 = r0.mWifiMulticastEnabled;
            r34 = r0;
            r4 = r4 | r34;
        L_0x00d7:
            r0 = r36;
            r0 = r0.mAudioTurnedOnTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x00f1;
        L_0x00df:
            r0 = r36;
            r0 = r0.mAudioTurnedOnTimer;
            r34 = r0;
            r35 = 0;
            r34 = r34.reset(r35);
            if (r34 != 0) goto L_0x0191;
        L_0x00ed:
            r34 = 1;
        L_0x00ef:
            r4 = r4 | r34;
        L_0x00f1:
            r0 = r36;
            r0 = r0.mVideoTurnedOnTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x010b;
        L_0x00f9:
            r0 = r36;
            r0 = r0.mVideoTurnedOnTimer;
            r34 = r0;
            r35 = 0;
            r34 = r34.reset(r35);
            if (r34 != 0) goto L_0x0195;
        L_0x0107:
            r34 = 1;
        L_0x0109:
            r4 = r4 | r34;
        L_0x010b:
            r0 = r36;
            r0 = r0.mFlashlightTurnedOnTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x0125;
        L_0x0113:
            r0 = r36;
            r0 = r0.mFlashlightTurnedOnTimer;
            r34 = r0;
            r35 = 0;
            r34 = r34.reset(r35);
            if (r34 != 0) goto L_0x0199;
        L_0x0121:
            r34 = 1;
        L_0x0123:
            r4 = r4 | r34;
        L_0x0125:
            r0 = r36;
            r0 = r0.mCameraTurnedOnTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x013f;
        L_0x012d:
            r0 = r36;
            r0 = r0.mCameraTurnedOnTimer;
            r34 = r0;
            r35 = 0;
            r34 = r34.reset(r35);
            if (r34 != 0) goto L_0x019c;
        L_0x013b:
            r34 = 1;
        L_0x013d:
            r4 = r4 | r34;
        L_0x013f:
            r0 = r36;
            r0 = r0.mForegroundActivityTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x0159;
        L_0x0147:
            r0 = r36;
            r0 = r0.mForegroundActivityTimer;
            r34 = r0;
            r35 = 0;
            r34 = r34.reset(r35);
            if (r34 != 0) goto L_0x019f;
        L_0x0155:
            r34 = 1;
        L_0x0157:
            r4 = r4 | r34;
        L_0x0159:
            r0 = r36;
            r0 = r0.mProcessStateTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x01b7;
        L_0x0161:
            r9 = 0;
        L_0x0162:
            r34 = 3;
            r0 = r34;
            if (r9 >= r0) goto L_0x01a5;
        L_0x0168:
            r0 = r36;
            r0 = r0.mProcessStateTimer;
            r34 = r0;
            r34 = r34[r9];
            if (r34 == 0) goto L_0x0186;
        L_0x0172:
            r0 = r36;
            r0 = r0.mProcessStateTimer;
            r34 = r0;
            r34 = r34[r9];
            r35 = 0;
            r34 = r34.reset(r35);
            if (r34 != 0) goto L_0x01a2;
        L_0x0182:
            r34 = 1;
        L_0x0184:
            r4 = r4 | r34;
        L_0x0186:
            r9 = r9 + 1;
            goto L_0x0162;
        L_0x0189:
            r34 = 0;
            goto L_0x00b3;
        L_0x018d:
            r34 = 0;
            goto L_0x00cd;
        L_0x0191:
            r34 = 0;
            goto L_0x00ef;
        L_0x0195:
            r34 = 0;
            goto L_0x0109;
        L_0x0199:
            r34 = 0;
            goto L_0x0123;
        L_0x019c:
            r34 = 0;
            goto L_0x013d;
        L_0x019f:
            r34 = 0;
            goto L_0x0157;
        L_0x01a2:
            r34 = 0;
            goto L_0x0184;
        L_0x01a5:
            r0 = r36;
            r0 = r0.mProcessState;
            r34 = r0;
            r35 = 3;
            r0 = r34;
            r1 = r35;
            if (r0 == r1) goto L_0x01fd;
        L_0x01b3:
            r34 = 1;
        L_0x01b5:
            r4 = r4 | r34;
        L_0x01b7:
            r0 = r36;
            r0 = r0.mVibratorOnTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x01de;
        L_0x01bf:
            r0 = r36;
            r0 = r0.mVibratorOnTimer;
            r34 = r0;
            r35 = 0;
            r34 = r34.reset(r35);
            if (r34 == 0) goto L_0x0200;
        L_0x01cd:
            r0 = r36;
            r0 = r0.mVibratorOnTimer;
            r34 = r0;
            r34.detach();
            r34 = 0;
            r0 = r34;
            r1 = r36;
            r1.mVibratorOnTimer = r0;
        L_0x01de:
            r0 = r36;
            r0 = r0.mUserActivityCounters;
            r34 = r0;
            if (r34 == 0) goto L_0x0202;
        L_0x01e6:
            r9 = 0;
        L_0x01e7:
            r34 = 3;
            r0 = r34;
            if (r9 >= r0) goto L_0x0202;
        L_0x01ed:
            r0 = r36;
            r0 = r0.mUserActivityCounters;
            r34 = r0;
            r34 = r34[r9];
            r35 = 0;
            r34.reset(r35);
            r9 = r9 + 1;
            goto L_0x01e7;
        L_0x01fd:
            r34 = 0;
            goto L_0x01b5;
        L_0x0200:
            r4 = 1;
            goto L_0x01de;
        L_0x0202:
            r0 = r36;
            r0 = r0.mNetworkByteActivityCounters;
            r34 = r0;
            if (r34 == 0) goto L_0x0244;
        L_0x020a:
            r9 = 0;
        L_0x020b:
            r34 = 4;
            r0 = r34;
            if (r9 >= r0) goto L_0x022e;
        L_0x0211:
            r0 = r36;
            r0 = r0.mNetworkByteActivityCounters;
            r34 = r0;
            r34 = r34[r9];
            r35 = 0;
            r34.reset(r35);
            r0 = r36;
            r0 = r0.mNetworkPacketActivityCounters;
            r34 = r0;
            r34 = r34[r9];
            r35 = 0;
            r34.reset(r35);
            r9 = r9 + 1;
            goto L_0x020b;
        L_0x022e:
            r0 = r36;
            r0 = r0.mMobileRadioActiveTime;
            r34 = r0;
            r35 = 0;
            r34.reset(r35);
            r0 = r36;
            r0 = r0.mMobileRadioActiveCount;
            r34 = r0;
            r35 = 0;
            r34.reset(r35);
        L_0x0244:
            r9 = 0;
        L_0x0245:
            r34 = 4;
            r0 = r34;
            if (r9 >= r0) goto L_0x027c;
        L_0x024b:
            r0 = r36;
            r0 = r0.mWifiControllerTime;
            r34 = r0;
            r34 = r34[r9];
            if (r34 == 0) goto L_0x0262;
        L_0x0255:
            r0 = r36;
            r0 = r0.mWifiControllerTime;
            r34 = r0;
            r34 = r34[r9];
            r35 = 0;
            r34.reset(r35);
        L_0x0262:
            r0 = r36;
            r0 = r0.mBluetoothControllerTime;
            r34 = r0;
            r34 = r34[r9];
            if (r34 == 0) goto L_0x0279;
        L_0x026c:
            r0 = r36;
            r0 = r0.mBluetoothControllerTime;
            r34 = r0;
            r34 = r34[r9];
            r35 = 0;
            r34.reset(r35);
        L_0x0279:
            r9 = r9 + 1;
            goto L_0x0245;
        L_0x027c:
            r0 = r36;
            r0 = r0.mUserCpuTime;
            r34 = r0;
            r35 = 0;
            r34.reset(r35);
            r0 = r36;
            r0 = r0.mSystemCpuTime;
            r34 = r0;
            r35 = 0;
            r34.reset(r35);
            r0 = r36;
            r0 = r0.mCpuPower;
            r34 = r0;
            r35 = 0;
            r34.reset(r35);
            r0 = r36;
            r0 = r0.mCpuClusterSpeed;
            r34 = r0;
            if (r34 == 0) goto L_0x02d4;
        L_0x02a5:
            r0 = r36;
            r5 = r0.mCpuClusterSpeed;
            r0 = r5.length;
            r20 = r0;
            r10 = 0;
            r11 = r10;
        L_0x02ae:
            r0 = r20;
            if (r11 >= r0) goto L_0x02d4;
        L_0x02b2:
            r29 = r5[r11];
            if (r29 == 0) goto L_0x02d0;
        L_0x02b6:
            r6 = r29;
            r0 = r6.length;
            r21 = r0;
            r10 = 0;
        L_0x02bc:
            r0 = r21;
            if (r10 >= r0) goto L_0x02d0;
        L_0x02c0:
            r28 = r6[r10];
            if (r28 == 0) goto L_0x02cd;
        L_0x02c4:
            r34 = 0;
            r0 = r28;
            r1 = r34;
            r0.reset(r1);
        L_0x02cd:
            r10 = r10 + 1;
            goto L_0x02bc;
        L_0x02d0:
            r10 = r11 + 1;
            r11 = r10;
            goto L_0x02ae;
        L_0x02d4:
            r0 = r36;
            r0 = r0.mWakelockStats;
            r34 = r0;
            r32 = r34.getMap();
            r34 = r32.size();
            r18 = r34 + -1;
        L_0x02e4:
            if (r18 < 0) goto L_0x0302;
        L_0x02e6:
            r0 = r32;
            r1 = r18;
            r33 = r0.valueAt(r1);
            r33 = (com.android.internal.os.BatteryStatsImpl.Uid.Wakelock) r33;
            r34 = r33.reset();
            if (r34 == 0) goto L_0x0300;
        L_0x02f6:
            r0 = r32;
            r1 = r18;
            r0.removeAt(r1);
        L_0x02fd:
            r18 = r18 + -1;
            goto L_0x02e4;
        L_0x0300:
            r4 = 1;
            goto L_0x02fd;
        L_0x0302:
            r0 = r36;
            r0 = r0.mWakelockStats;
            r34 = r0;
            r34.cleanup();
            r0 = r36;
            r0 = r0.mSyncStats;
            r34 = r0;
            r30 = r34.getMap();
            r34 = r30.size();
            r14 = r34 + -1;
        L_0x031b:
            if (r14 < 0) goto L_0x033e;
        L_0x031d:
            r0 = r30;
            r31 = r0.valueAt(r14);
            r31 = (com.android.internal.os.BatteryStatsImpl.StopwatchTimer) r31;
            r34 = 0;
            r0 = r31;
            r1 = r34;
            r34 = r0.reset(r1);
            if (r34 == 0) goto L_0x033c;
        L_0x0331:
            r0 = r30;
            r0.removeAt(r14);
            r31.detach();
        L_0x0339:
            r14 = r14 + -1;
            goto L_0x031b;
        L_0x033c:
            r4 = 1;
            goto L_0x0339;
        L_0x033e:
            r0 = r36;
            r0 = r0.mSyncStats;
            r34 = r0;
            r34.cleanup();
            r0 = r36;
            r0 = r0.mJobStats;
            r34 = r0;
            r19 = r34.getMap();
            r34 = r19.size();
            r12 = r34 + -1;
        L_0x0357:
            if (r12 < 0) goto L_0x037a;
        L_0x0359:
            r0 = r19;
            r31 = r0.valueAt(r12);
            r31 = (com.android.internal.os.BatteryStatsImpl.StopwatchTimer) r31;
            r34 = 0;
            r0 = r31;
            r1 = r34;
            r34 = r0.reset(r1);
            if (r34 == 0) goto L_0x0378;
        L_0x036d:
            r0 = r19;
            r0.removeAt(r12);
            r31.detach();
        L_0x0375:
            r12 = r12 + -1;
            goto L_0x0357;
        L_0x0378:
            r4 = 1;
            goto L_0x0375;
        L_0x037a:
            r0 = r36;
            r0 = r0.mJobStats;
            r34 = r0;
            r34.cleanup();
            r0 = r36;
            r0 = r0.mSensorStats;
            r34 = r0;
            r34 = r34.size();
            r15 = r34 + -1;
        L_0x038f:
            if (r15 < 0) goto L_0x03b5;
        L_0x0391:
            r0 = r36;
            r0 = r0.mSensorStats;
            r34 = r0;
            r0 = r34;
            r26 = r0.valueAt(r15);
            r26 = (com.android.internal.os.BatteryStatsImpl.Uid.Sensor) r26;
            r34 = r26.reset();
            if (r34 == 0) goto L_0x03b3;
        L_0x03a5:
            r0 = r36;
            r0 = r0.mSensorStats;
            r34 = r0;
            r0 = r34;
            r0.removeAt(r15);
        L_0x03b0:
            r15 = r15 + -1;
            goto L_0x038f;
        L_0x03b3:
            r4 = 1;
            goto L_0x03b0;
        L_0x03b5:
            r0 = r36;
            r0 = r0.mProcessStats;
            r34 = r0;
            r34 = r34.size();
            r13 = r34 + -1;
        L_0x03c1:
            if (r13 < 0) goto L_0x03f5;
        L_0x03c3:
            r0 = r36;
            r0 = r0.mProcessStats;
            r34 = r0;
            r0 = r34;
            r25 = r0.valueAt(r13);
            r25 = (com.android.internal.os.BatteryStatsImpl.Uid.Proc) r25;
            r0 = r25;
            r0 = r0.mProcessState;
            r34 = r0;
            r35 = 3;
            r0 = r34;
            r1 = r35;
            if (r0 != r1) goto L_0x03f0;
        L_0x03df:
            r25.detach();
            r0 = r36;
            r0 = r0.mProcessStats;
            r34 = r0;
            r0 = r34;
            r0.removeAt(r13);
        L_0x03ed:
            r13 = r13 + -1;
            goto L_0x03c1;
        L_0x03f0:
            r25.reset();
            r4 = 1;
            goto L_0x03ed;
        L_0x03f5:
            r0 = r36;
            r0 = r0.mPids;
            r34 = r0;
            r34 = r34.size();
            if (r34 <= 0) goto L_0x0435;
        L_0x0401:
            r0 = r36;
            r0 = r0.mPids;
            r34 = r0;
            r34 = r34.size();
            r9 = r34 + -1;
        L_0x040d:
            if (r9 < 0) goto L_0x0435;
        L_0x040f:
            r0 = r36;
            r0 = r0.mPids;
            r34 = r0;
            r0 = r34;
            r23 = r0.valueAt(r9);
            r23 = (android.os.BatteryStats.Uid.Pid) r23;
            r0 = r23;
            r0 = r0.mWakeNesting;
            r34 = r0;
            if (r34 <= 0) goto L_0x0429;
        L_0x0425:
            r4 = 1;
        L_0x0426:
            r9 = r9 + -1;
            goto L_0x040d;
        L_0x0429:
            r0 = r36;
            r0 = r0.mPids;
            r34 = r0;
            r0 = r34;
            r0.removeAt(r9);
            goto L_0x0426;
        L_0x0435:
            r0 = r36;
            r0 = r0.mPackageStats;
            r34 = r0;
            r34 = r34.size();
            if (r34 <= 0) goto L_0x049d;
        L_0x0441:
            r0 = r36;
            r0 = r0.mPackageStats;
            r34 = r0;
            r34 = r34.entrySet();
            r16 = r34.iterator();
        L_0x044f:
            r34 = r16.hasNext();
            if (r34 == 0) goto L_0x0494;
        L_0x0455:
            r24 = r16.next();
            r24 = (java.util.Map.Entry) r24;
            r22 = r24.getValue();
            r22 = (com.android.internal.os.BatteryStatsImpl.Uid.Pkg) r22;
            r22.detach();
            r0 = r22;
            r0 = r0.mServiceStats;
            r34 = r0;
            r34 = r34.size();
            if (r34 <= 0) goto L_0x044f;
        L_0x0470:
            r0 = r22;
            r0 = r0.mServiceStats;
            r34 = r0;
            r34 = r34.entrySet();
            r17 = r34.iterator();
        L_0x047e:
            r34 = r17.hasNext();
            if (r34 == 0) goto L_0x044f;
        L_0x0484:
            r27 = r17.next();
            r27 = (java.util.Map.Entry) r27;
            r34 = r27.getValue();
            r34 = (com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv) r34;
            r34.detach();
            goto L_0x047e;
        L_0x0494:
            r0 = r36;
            r0 = r0.mPackageStats;
            r34 = r0;
            r34.clear();
        L_0x049d:
            r34 = 0;
            r0 = r34;
            r2 = r36;
            r2.mLastStepSystemTime = r0;
            r0 = r34;
            r2 = r36;
            r2.mLastStepUserTime = r0;
            r34 = 0;
            r0 = r34;
            r2 = r36;
            r2.mCurStepSystemTime = r0;
            r0 = r34;
            r2 = r36;
            r2.mCurStepUserTime = r0;
            if (r4 != 0) goto L_0x0668;
        L_0x04bb:
            r0 = r36;
            r0 = r0.mWifiRunningTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x04cc;
        L_0x04c3:
            r0 = r36;
            r0 = r0.mWifiRunningTimer;
            r34 = r0;
            r34.detach();
        L_0x04cc:
            r0 = r36;
            r0 = r0.mFullWifiLockTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x04dd;
        L_0x04d4:
            r0 = r36;
            r0 = r0.mFullWifiLockTimer;
            r34 = r0;
            r34.detach();
        L_0x04dd:
            r0 = r36;
            r0 = r0.mWifiScanTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x04ee;
        L_0x04e5:
            r0 = r36;
            r0 = r0.mWifiScanTimer;
            r34 = r0;
            r34.detach();
        L_0x04ee:
            r9 = 0;
        L_0x04ef:
            r34 = 5;
            r0 = r34;
            if (r9 >= r0) goto L_0x050d;
        L_0x04f5:
            r0 = r36;
            r0 = r0.mWifiBatchedScanTimer;
            r34 = r0;
            r34 = r34[r9];
            if (r34 == 0) goto L_0x050a;
        L_0x04ff:
            r0 = r36;
            r0 = r0.mWifiBatchedScanTimer;
            r34 = r0;
            r34 = r34[r9];
            r34.detach();
        L_0x050a:
            r9 = r9 + 1;
            goto L_0x04ef;
        L_0x050d:
            r0 = r36;
            r0 = r0.mWifiMulticastTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x051e;
        L_0x0515:
            r0 = r36;
            r0 = r0.mWifiMulticastTimer;
            r34 = r0;
            r34.detach();
        L_0x051e:
            r0 = r36;
            r0 = r0.mAudioTurnedOnTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x0537;
        L_0x0526:
            r0 = r36;
            r0 = r0.mAudioTurnedOnTimer;
            r34 = r0;
            r34.detach();
            r34 = 0;
            r0 = r34;
            r1 = r36;
            r1.mAudioTurnedOnTimer = r0;
        L_0x0537:
            r0 = r36;
            r0 = r0.mVideoTurnedOnTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x0550;
        L_0x053f:
            r0 = r36;
            r0 = r0.mVideoTurnedOnTimer;
            r34 = r0;
            r34.detach();
            r34 = 0;
            r0 = r34;
            r1 = r36;
            r1.mVideoTurnedOnTimer = r0;
        L_0x0550:
            r0 = r36;
            r0 = r0.mFlashlightTurnedOnTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x0569;
        L_0x0558:
            r0 = r36;
            r0 = r0.mFlashlightTurnedOnTimer;
            r34 = r0;
            r34.detach();
            r34 = 0;
            r0 = r34;
            r1 = r36;
            r1.mFlashlightTurnedOnTimer = r0;
        L_0x0569:
            r0 = r36;
            r0 = r0.mCameraTurnedOnTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x0582;
        L_0x0571:
            r0 = r36;
            r0 = r0.mCameraTurnedOnTimer;
            r34 = r0;
            r34.detach();
            r34 = 0;
            r0 = r34;
            r1 = r36;
            r1.mCameraTurnedOnTimer = r0;
        L_0x0582:
            r0 = r36;
            r0 = r0.mForegroundActivityTimer;
            r34 = r0;
            if (r34 == 0) goto L_0x059b;
        L_0x058a:
            r0 = r36;
            r0 = r0.mForegroundActivityTimer;
            r34 = r0;
            r34.detach();
            r34 = 0;
            r0 = r34;
            r1 = r36;
            r1.mForegroundActivityTimer = r0;
        L_0x059b:
            r0 = r36;
            r0 = r0.mUserActivityCounters;
            r34 = r0;
            if (r34 == 0) goto L_0x05b8;
        L_0x05a3:
            r9 = 0;
        L_0x05a4:
            r34 = 3;
            r0 = r34;
            if (r9 >= r0) goto L_0x05b8;
        L_0x05aa:
            r0 = r36;
            r0 = r0.mUserActivityCounters;
            r34 = r0;
            r34 = r34[r9];
            r34.detach();
            r9 = r9 + 1;
            goto L_0x05a4;
        L_0x05b8:
            r0 = r36;
            r0 = r0.mNetworkByteActivityCounters;
            r34 = r0;
            if (r34 == 0) goto L_0x05e0;
        L_0x05c0:
            r9 = 0;
        L_0x05c1:
            r34 = 4;
            r0 = r34;
            if (r9 >= r0) goto L_0x05e0;
        L_0x05c7:
            r0 = r36;
            r0 = r0.mNetworkByteActivityCounters;
            r34 = r0;
            r34 = r34[r9];
            r34.detach();
            r0 = r36;
            r0 = r0.mNetworkPacketActivityCounters;
            r34 = r0;
            r34 = r34[r9];
            r34.detach();
            r9 = r9 + 1;
            goto L_0x05c1;
        L_0x05e0:
            r9 = 0;
        L_0x05e1:
            r34 = 4;
            r0 = r34;
            if (r9 >= r0) goto L_0x0614;
        L_0x05e7:
            r0 = r36;
            r0 = r0.mWifiControllerTime;
            r34 = r0;
            r34 = r34[r9];
            if (r34 == 0) goto L_0x05fc;
        L_0x05f1:
            r0 = r36;
            r0 = r0.mWifiControllerTime;
            r34 = r0;
            r34 = r34[r9];
            r34.detach();
        L_0x05fc:
            r0 = r36;
            r0 = r0.mBluetoothControllerTime;
            r34 = r0;
            r34 = r34[r9];
            if (r34 == 0) goto L_0x0611;
        L_0x0606:
            r0 = r36;
            r0 = r0.mBluetoothControllerTime;
            r34 = r0;
            r34 = r34[r9];
            r34.detach();
        L_0x0611:
            r9 = r9 + 1;
            goto L_0x05e1;
        L_0x0614:
            r0 = r36;
            r0 = r0.mPids;
            r34 = r0;
            r34.clear();
            r0 = r36;
            r0 = r0.mUserCpuTime;
            r34 = r0;
            r34.detach();
            r0 = r36;
            r0 = r0.mSystemCpuTime;
            r34 = r0;
            r34.detach();
            r0 = r36;
            r0 = r0.mCpuPower;
            r34 = r0;
            r34.detach();
            r0 = r36;
            r0 = r0.mCpuClusterSpeed;
            r34 = r0;
            if (r34 == 0) goto L_0x0668;
        L_0x0640:
            r0 = r36;
            r5 = r0.mCpuClusterSpeed;
            r0 = r5.length;
            r20 = r0;
            r10 = 0;
            r11 = r10;
        L_0x0649:
            r0 = r20;
            if (r11 >= r0) goto L_0x0668;
        L_0x064d:
            r8 = r5[r11];
            if (r8 == 0) goto L_0x0664;
        L_0x0651:
            r6 = r8;
            r0 = r6.length;
            r21 = r0;
            r10 = 0;
        L_0x0656:
            r0 = r21;
            if (r10 >= r0) goto L_0x0664;
        L_0x065a:
            r7 = r6[r10];
            if (r7 == 0) goto L_0x0661;
        L_0x065e:
            r7.detach();
        L_0x0661:
            r10 = r10 + 1;
            goto L_0x0656;
        L_0x0664:
            r10 = r11 + 1;
            r11 = r10;
            goto L_0x0649;
        L_0x0668:
            if (r4 != 0) goto L_0x066d;
        L_0x066a:
            r34 = 1;
        L_0x066c:
            return r34;
        L_0x066d:
            r34 = 0;
            goto L_0x066c;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.reset():boolean");
        }

        void writeToParcelLocked(android.os.Parcel r35, long r36) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r34 = this;
            r0 = r34;
            r0 = r0.mWakelockStats;
            r32 = r0;
            r30 = r32.getMap();
            r8 = r30.size();
            r0 = r35;
            r0.writeInt(r8);
            r20 = 0;
        L_0x0015:
            r0 = r20;
            if (r0 >= r8) goto L_0x0040;
        L_0x0019:
            r0 = r30;
            r1 = r20;
            r32 = r0.keyAt(r1);
            r32 = (java.lang.String) r32;
            r0 = r35;
            r1 = r32;
            r0.writeString(r1);
            r0 = r30;
            r1 = r20;
            r31 = r0.valueAt(r1);
            r31 = (com.android.internal.os.BatteryStatsImpl.Uid.Wakelock) r31;
            r0 = r31;
            r1 = r35;
            r2 = r36;
            r0.writeToParcelLocked(r1, r2);
            r20 = r20 + 1;
            goto L_0x0015;
        L_0x0040:
            r0 = r34;
            r0 = r0.mSyncStats;
            r32 = r0;
            r28 = r32.getMap();
            r6 = r28.size();
            r0 = r35;
            r0.writeInt(r6);
            r18 = 0;
        L_0x0055:
            r0 = r18;
            if (r0 >= r6) goto L_0x0080;
        L_0x0059:
            r0 = r28;
            r1 = r18;
            r32 = r0.keyAt(r1);
            r32 = (java.lang.String) r32;
            r0 = r35;
            r1 = r32;
            r0.writeString(r1);
            r0 = r28;
            r1 = r18;
            r29 = r0.valueAt(r1);
            r29 = (com.android.internal.os.BatteryStatsImpl.StopwatchTimer) r29;
            r0 = r35;
            r1 = r29;
            r2 = r36;
            com.android.internal.os.BatteryStatsImpl.Timer.writeTimerToParcel(r0, r1, r2);
            r18 = r18 + 1;
            goto L_0x0055;
        L_0x0080:
            r0 = r34;
            r0 = r0.mJobStats;
            r32 = r0;
            r21 = r32.getMap();
            r4 = r21.size();
            r0 = r35;
            r0.writeInt(r4);
            r16 = 0;
        L_0x0095:
            r0 = r16;
            if (r0 >= r4) goto L_0x00c0;
        L_0x0099:
            r0 = r21;
            r1 = r16;
            r32 = r0.keyAt(r1);
            r32 = (java.lang.String) r32;
            r0 = r35;
            r1 = r32;
            r0.writeString(r1);
            r0 = r21;
            r1 = r16;
            r29 = r0.valueAt(r1);
            r29 = (com.android.internal.os.BatteryStatsImpl.StopwatchTimer) r29;
            r0 = r35;
            r1 = r29;
            r2 = r36;
            com.android.internal.os.BatteryStatsImpl.Timer.writeTimerToParcel(r0, r1, r2);
            r16 = r16 + 1;
            goto L_0x0095;
        L_0x00c0:
            r0 = r34;
            r0 = r0.mSensorStats;
            r32 = r0;
            r7 = r32.size();
            r0 = r35;
            r0.writeInt(r7);
            r19 = 0;
        L_0x00d1:
            r0 = r19;
            if (r0 >= r7) goto L_0x0106;
        L_0x00d5:
            r0 = r34;
            r0 = r0.mSensorStats;
            r32 = r0;
            r0 = r32;
            r1 = r19;
            r32 = r0.keyAt(r1);
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mSensorStats;
            r32 = r0;
            r0 = r32;
            r1 = r19;
            r27 = r0.valueAt(r1);
            r27 = (com.android.internal.os.BatteryStatsImpl.Uid.Sensor) r27;
            r0 = r27;
            r1 = r35;
            r2 = r36;
            r0.writeToParcelLocked(r1, r2);
            r19 = r19 + 1;
            goto L_0x00d1;
        L_0x0106:
            r0 = r34;
            r0 = r0.mProcessStats;
            r32 = r0;
            r5 = r32.size();
            r0 = r35;
            r0.writeInt(r5);
            r17 = 0;
        L_0x0117:
            r0 = r17;
            if (r0 >= r5) goto L_0x014c;
        L_0x011b:
            r0 = r34;
            r0 = r0.mProcessStats;
            r32 = r0;
            r0 = r32;
            r1 = r17;
            r32 = r0.keyAt(r1);
            r32 = (java.lang.String) r32;
            r0 = r35;
            r1 = r32;
            r0.writeString(r1);
            r0 = r34;
            r0 = r0.mProcessStats;
            r32 = r0;
            r0 = r32;
            r1 = r17;
            r26 = r0.valueAt(r1);
            r26 = (com.android.internal.os.BatteryStatsImpl.Uid.Proc) r26;
            r0 = r26;
            r1 = r35;
            r0.writeToParcelLocked(r1);
            r17 = r17 + 1;
            goto L_0x0117;
        L_0x014c:
            r0 = r34;
            r0 = r0.mPackageStats;
            r32 = r0;
            r32 = r32.size();
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mPackageStats;
            r32 = r0;
            r32 = r32.entrySet();
            r14 = r32.iterator();
        L_0x016b:
            r32 = r14.hasNext();
            if (r32 == 0) goto L_0x0192;
        L_0x0171:
            r25 = r14.next();
            r25 = (java.util.Map.Entry) r25;
            r32 = r25.getKey();
            r32 = (java.lang.String) r32;
            r0 = r35;
            r1 = r32;
            r0.writeString(r1);
            r24 = r25.getValue();
            r24 = (com.android.internal.os.BatteryStatsImpl.Uid.Pkg) r24;
            r0 = r24;
            r1 = r35;
            r0.writeToParcelLocked(r1);
            goto L_0x016b;
        L_0x0192:
            r0 = r34;
            r0 = r0.mWifiRunningTimer;
            r32 = r0;
            if (r32 == 0) goto L_0x0220;
        L_0x019a:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mWifiRunningTimer;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r2 = r36;
            r0.writeToParcel(r1, r2);
        L_0x01b2:
            r0 = r34;
            r0 = r0.mFullWifiLockTimer;
            r32 = r0;
            if (r32 == 0) goto L_0x022a;
        L_0x01ba:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mFullWifiLockTimer;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r2 = r36;
            r0.writeToParcel(r1, r2);
        L_0x01d2:
            r0 = r34;
            r0 = r0.mWifiScanTimer;
            r32 = r0;
            if (r32 == 0) goto L_0x0234;
        L_0x01da:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mWifiScanTimer;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r2 = r36;
            r0.writeToParcel(r1, r2);
        L_0x01f2:
            r13 = 0;
        L_0x01f3:
            r32 = 5;
            r0 = r32;
            if (r13 >= r0) goto L_0x0248;
        L_0x01f9:
            r0 = r34;
            r0 = r0.mWifiBatchedScanTimer;
            r32 = r0;
            r32 = r32[r13];
            if (r32 == 0) goto L_0x023e;
        L_0x0203:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mWifiBatchedScanTimer;
            r32 = r0;
            r32 = r32[r13];
            r0 = r32;
            r1 = r35;
            r2 = r36;
            r0.writeToParcel(r1, r2);
        L_0x021d:
            r13 = r13 + 1;
            goto L_0x01f3;
        L_0x0220:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x01b2;
        L_0x022a:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x01d2;
        L_0x0234:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x01f2;
        L_0x023e:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x021d;
        L_0x0248:
            r0 = r34;
            r0 = r0.mWifiMulticastTimer;
            r32 = r0;
            if (r32 == 0) goto L_0x0336;
        L_0x0250:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mWifiMulticastTimer;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r2 = r36;
            r0.writeToParcel(r1, r2);
        L_0x0268:
            r0 = r34;
            r0 = r0.mAudioTurnedOnTimer;
            r32 = r0;
            if (r32 == 0) goto L_0x0341;
        L_0x0270:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mAudioTurnedOnTimer;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r2 = r36;
            r0.writeToParcel(r1, r2);
        L_0x0288:
            r0 = r34;
            r0 = r0.mVideoTurnedOnTimer;
            r32 = r0;
            if (r32 == 0) goto L_0x034c;
        L_0x0290:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mVideoTurnedOnTimer;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r2 = r36;
            r0.writeToParcel(r1, r2);
        L_0x02a8:
            r0 = r34;
            r0 = r0.mFlashlightTurnedOnTimer;
            r32 = r0;
            if (r32 == 0) goto L_0x0357;
        L_0x02b0:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mFlashlightTurnedOnTimer;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r2 = r36;
            r0.writeToParcel(r1, r2);
        L_0x02c8:
            r0 = r34;
            r0 = r0.mCameraTurnedOnTimer;
            r32 = r0;
            if (r32 == 0) goto L_0x0362;
        L_0x02d0:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mCameraTurnedOnTimer;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r2 = r36;
            r0.writeToParcel(r1, r2);
        L_0x02e8:
            r0 = r34;
            r0 = r0.mForegroundActivityTimer;
            r32 = r0;
            if (r32 == 0) goto L_0x036d;
        L_0x02f0:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mForegroundActivityTimer;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r2 = r36;
            r0.writeToParcel(r1, r2);
        L_0x0308:
            r13 = 0;
        L_0x0309:
            r32 = 3;
            r0 = r32;
            if (r13 >= r0) goto L_0x0381;
        L_0x030f:
            r0 = r34;
            r0 = r0.mProcessStateTimer;
            r32 = r0;
            r32 = r32[r13];
            if (r32 == 0) goto L_0x0377;
        L_0x0319:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mProcessStateTimer;
            r32 = r0;
            r32 = r32[r13];
            r0 = r32;
            r1 = r35;
            r2 = r36;
            r0.writeToParcel(r1, r2);
        L_0x0333:
            r13 = r13 + 1;
            goto L_0x0309;
        L_0x0336:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x0268;
        L_0x0341:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x0288;
        L_0x034c:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x02a8;
        L_0x0357:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x02c8;
        L_0x0362:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x02e8;
        L_0x036d:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x0308;
        L_0x0377:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x0333;
        L_0x0381:
            r0 = r34;
            r0 = r0.mVibratorOnTimer;
            r32 = r0;
            if (r32 == 0) goto L_0x03cb;
        L_0x0389:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mVibratorOnTimer;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r2 = r36;
            r0.writeToParcel(r1, r2);
        L_0x03a1:
            r0 = r34;
            r0 = r0.mUserActivityCounters;
            r32 = r0;
            if (r32 == 0) goto L_0x03d5;
        L_0x03a9:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r13 = 0;
        L_0x03b3:
            r32 = 3;
            r0 = r32;
            if (r13 >= r0) goto L_0x03de;
        L_0x03b9:
            r0 = r34;
            r0 = r0.mUserActivityCounters;
            r32 = r0;
            r32 = r32[r13];
            r0 = r32;
            r1 = r35;
            r0.writeToParcel(r1);
            r13 = r13 + 1;
            goto L_0x03b3;
        L_0x03cb:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x03a1;
        L_0x03d5:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
        L_0x03de:
            r0 = r34;
            r0 = r0.mNetworkByteActivityCounters;
            r32 = r0;
            if (r32 == 0) goto L_0x045d;
        L_0x03e6:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r13 = 0;
        L_0x03f0:
            r32 = 4;
            r0 = r32;
            if (r13 >= r0) goto L_0x0417;
        L_0x03f6:
            r0 = r34;
            r0 = r0.mNetworkByteActivityCounters;
            r32 = r0;
            r32 = r32[r13];
            r0 = r32;
            r1 = r35;
            r0.writeToParcel(r1);
            r0 = r34;
            r0 = r0.mNetworkPacketActivityCounters;
            r32 = r0;
            r32 = r32[r13];
            r0 = r32;
            r1 = r35;
            r0.writeToParcel(r1);
            r13 = r13 + 1;
            goto L_0x03f0;
        L_0x0417:
            r0 = r34;
            r0 = r0.mMobileRadioActiveTime;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r0.writeToParcel(r1);
            r0 = r34;
            r0 = r0.mMobileRadioActiveCount;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r0.writeToParcel(r1);
        L_0x0431:
            r13 = 0;
        L_0x0432:
            r32 = 4;
            r0 = r32;
            if (r13 >= r0) goto L_0x0471;
        L_0x0438:
            r0 = r34;
            r0 = r0.mWifiControllerTime;
            r32 = r0;
            r32 = r32[r13];
            if (r32 == 0) goto L_0x0467;
        L_0x0442:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mWifiControllerTime;
            r32 = r0;
            r32 = r32[r13];
            r0 = r32;
            r1 = r35;
            r0.writeToParcel(r1);
        L_0x045a:
            r13 = r13 + 1;
            goto L_0x0432;
        L_0x045d:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x0431;
        L_0x0467:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x045a;
        L_0x0471:
            r13 = 0;
        L_0x0472:
            r32 = 4;
            r0 = r32;
            if (r13 >= r0) goto L_0x04a7;
        L_0x0478:
            r0 = r34;
            r0 = r0.mBluetoothControllerTime;
            r32 = r0;
            r32 = r32[r13];
            if (r32 == 0) goto L_0x049d;
        L_0x0482:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mBluetoothControllerTime;
            r32 = r0;
            r32 = r32[r13];
            r0 = r32;
            r1 = r35;
            r0.writeToParcel(r1);
        L_0x049a:
            r13 = r13 + 1;
            goto L_0x0472;
        L_0x049d:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x049a;
        L_0x04a7:
            r0 = r34;
            r0 = r0.mUserCpuTime;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r0.writeToParcel(r1);
            r0 = r34;
            r0 = r0.mSystemCpuTime;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r0.writeToParcel(r1);
            r0 = r34;
            r0 = r0.mCpuPower;
            r32 = r0;
            r0 = r32;
            r1 = r35;
            r0.writeToParcel(r1);
            r0 = r34;
            r0 = r0.mCpuClusterSpeed;
            r32 = r0;
            if (r32 == 0) goto L_0x054a;
        L_0x04d6:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r0 = r0.mCpuClusterSpeed;
            r32 = r0;
            r0 = r32;
            r0 = r0.length;
            r32 = r0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r34;
            r9 = r0.mCpuClusterSpeed;
            r0 = r9.length;
            r22 = r0;
            r14 = 0;
            r15 = r14;
        L_0x04fa:
            r0 = r22;
            if (r15 >= r0) goto L_0x0553;
        L_0x04fe:
            r12 = r9[r15];
            if (r12 == 0) goto L_0x053d;
        L_0x0502:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r12.length;
            r32 = r0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r10 = r12;
            r0 = r10.length;
            r23 = r0;
            r14 = 0;
        L_0x051a:
            r0 = r23;
            if (r14 >= r0) goto L_0x0546;
        L_0x051e:
            r11 = r10[r14];
            if (r11 == 0) goto L_0x0533;
        L_0x0522:
            r32 = 1;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            r0 = r35;
            r11.writeToParcel(r0);
        L_0x0530:
            r14 = r14 + 1;
            goto L_0x051a;
        L_0x0533:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
            goto L_0x0530;
        L_0x053d:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
        L_0x0546:
            r14 = r15 + 1;
            r15 = r14;
            goto L_0x04fa;
        L_0x054a:
            r32 = 0;
            r0 = r35;
            r1 = r32;
            r0.writeInt(r1);
        L_0x0553:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.writeToParcelLocked(android.os.Parcel, long):void");
        }

        void readFromParcelLocked(com.android.internal.os.BatteryStatsImpl.TimeBase r37, com.android.internal.os.BatteryStatsImpl.TimeBase r38, android.os.Parcel r39) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r36 = this;
            r24 = r39.readInt();
            r0 = r36;
            r4 = r0.mWakelockStats;
            r4.clear();
            r13 = 0;
        L_0x000c:
            r0 = r24;
            if (r13 >= r0) goto L_0x0036;
        L_0x0010:
            r34 = r39.readString();
            r33 = new com.android.internal.os.BatteryStatsImpl$Uid$Wakelock;
            r0 = r33;
            r1 = r36;
            r0.<init>();
            r0 = r33;
            r1 = r37;
            r2 = r38;
            r3 = r39;
            r0.readFromParcelLocked(r1, r2, r3);
            r0 = r36;
            r4 = r0.mWakelockStats;
            r0 = r34;
            r1 = r33;
            r4.add(r0, r1);
            r13 = r13 + 1;
            goto L_0x000c;
        L_0x0036:
            r23 = r39.readInt();
            r0 = r36;
            r4 = r0.mSyncStats;
            r4.clear();
            r13 = 0;
        L_0x0042:
            r0 = r23;
            if (r13 >= r0) goto L_0x006e;
        L_0x0046:
            r32 = r39.readString();
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x006b;
        L_0x0050:
            r0 = r36;
            r0 = r0.mSyncStats;
            r35 = r0;
            r4 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r6 = 13;
            r7 = 0;
            r5 = r36;
            r8 = r37;
            r9 = r39;
            r4.<init>(r5, r6, r7, r8, r9);
            r0 = r35;
            r1 = r32;
            r0.add(r1, r4);
        L_0x006b:
            r13 = r13 + 1;
            goto L_0x0042;
        L_0x006e:
            r18 = r39.readInt();
            r0 = r36;
            r4 = r0.mJobStats;
            r4.clear();
            r13 = 0;
        L_0x007a:
            r0 = r18;
            if (r13 >= r0) goto L_0x00a4;
        L_0x007e:
            r14 = r39.readString();
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x00a1;
        L_0x0088:
            r0 = r36;
            r0 = r0.mJobStats;
            r35 = r0;
            r4 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r6 = 14;
            r7 = 0;
            r5 = r36;
            r8 = r37;
            r9 = r39;
            r4.<init>(r5, r6, r7, r8, r9);
            r0 = r35;
            r0.add(r14, r4);
        L_0x00a1:
            r13 = r13 + 1;
            goto L_0x007a;
        L_0x00a4:
            r21 = r39.readInt();
            r0 = r36;
            r4 = r0.mSensorStats;
            r4.clear();
            r15 = 0;
        L_0x00b0:
            r0 = r21;
            if (r15 >= r0) goto L_0x00de;
        L_0x00b4:
            r30 = r39.readInt();
            r29 = new com.android.internal.os.BatteryStatsImpl$Uid$Sensor;
            r0 = r29;
            r1 = r36;
            r2 = r30;
            r0.<init>(r2);
            r0 = r36;
            r4 = com.android.internal.os.BatteryStatsImpl.this;
            r4 = r4.mOnBatteryTimeBase;
            r0 = r29;
            r1 = r39;
            r0.readFromParcelLocked(r4, r1);
            r0 = r36;
            r4 = r0.mSensorStats;
            r0 = r30;
            r1 = r29;
            r4.put(r0, r1);
            r15 = r15 + 1;
            goto L_0x00b0;
        L_0x00de:
            r20 = r39.readInt();
            r0 = r36;
            r4 = r0.mProcessStats;
            r4.clear();
            r15 = 0;
        L_0x00ea:
            r0 = r20;
            if (r15 >= r0) goto L_0x0112;
        L_0x00ee:
            r28 = r39.readString();
            r27 = new com.android.internal.os.BatteryStatsImpl$Uid$Proc;
            r0 = r27;
            r1 = r36;
            r2 = r28;
            r0.<init>(r2);
            r0 = r27;
            r1 = r39;
            r0.readFromParcelLocked(r1);
            r0 = r36;
            r4 = r0.mProcessStats;
            r0 = r28;
            r1 = r27;
            r4.put(r0, r1);
            r15 = r15 + 1;
            goto L_0x00ea;
        L_0x0112:
            r19 = r39.readInt();
            r0 = r36;
            r4 = r0.mPackageStats;
            r4.clear();
            r16 = 0;
        L_0x011f:
            r0 = r16;
            r1 = r19;
            if (r0 >= r1) goto L_0x0147;
        L_0x0125:
            r25 = r39.readString();
            r26 = new com.android.internal.os.BatteryStatsImpl$Uid$Pkg;
            r0 = r26;
            r1 = r36;
            r0.<init>();
            r0 = r26;
            r1 = r39;
            r0.readFromParcelLocked(r1);
            r0 = r36;
            r4 = r0.mPackageStats;
            r0 = r25;
            r1 = r26;
            r4.put(r0, r1);
            r16 = r16 + 1;
            goto L_0x011f;
        L_0x0147:
            r4 = 0;
            r0 = r36;
            r0.mWifiRunning = r4;
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x01cf;
        L_0x0152:
            r4 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r6 = 4;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r7 = r5.mWifiRunningTimers;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r8 = r5.mOnBatteryTimeBase;
            r5 = r36;
            r9 = r39;
            r4.<init>(r5, r6, r7, r8, r9);
            r0 = r36;
            r0.mWifiRunningTimer = r4;
        L_0x016c:
            r4 = 0;
            r0 = r36;
            r0.mFullWifiLockOut = r4;
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x01d5;
        L_0x0177:
            r4 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r6 = 5;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r7 = r5.mFullWifiLockTimers;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r8 = r5.mOnBatteryTimeBase;
            r5 = r36;
            r9 = r39;
            r4.<init>(r5, r6, r7, r8, r9);
            r0 = r36;
            r0.mFullWifiLockTimer = r4;
        L_0x0191:
            r4 = 0;
            r0 = r36;
            r0.mWifiScanStarted = r4;
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x01db;
        L_0x019c:
            r4 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r6 = 6;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r7 = r5.mWifiScanTimers;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r8 = r5.mOnBatteryTimeBase;
            r5 = r36;
            r9 = r39;
            r4.<init>(r5, r6, r7, r8, r9);
            r0 = r36;
            r0.mWifiScanTimer = r4;
        L_0x01b6:
            r4 = -1;
            r0 = r36;
            r0.mWifiBatchedScanBinStarted = r4;
            r12 = 0;
        L_0x01bc:
            r4 = 5;
            if (r12 >= r4) goto L_0x01e9;
        L_0x01bf:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x01e1;
        L_0x01c5:
            r0 = r36;
            r1 = r39;
            r0.makeWifiBatchedScanBin(r12, r1);
        L_0x01cc:
            r12 = r12 + 1;
            goto L_0x01bc;
        L_0x01cf:
            r4 = 0;
            r0 = r36;
            r0.mWifiRunningTimer = r4;
            goto L_0x016c;
        L_0x01d5:
            r4 = 0;
            r0 = r36;
            r0.mFullWifiLockTimer = r4;
            goto L_0x0191;
        L_0x01db:
            r4 = 0;
            r0 = r36;
            r0.mWifiScanTimer = r4;
            goto L_0x01b6;
        L_0x01e1:
            r0 = r36;
            r4 = r0.mWifiBatchedScanTimer;
            r5 = 0;
            r4[r12] = r5;
            goto L_0x01cc;
        L_0x01e9:
            r4 = 0;
            r0 = r36;
            r0.mWifiMulticastEnabled = r4;
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x02c7;
        L_0x01f4:
            r4 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r6 = 7;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r7 = r5.mWifiMulticastTimers;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r8 = r5.mOnBatteryTimeBase;
            r5 = r36;
            r9 = r39;
            r4.<init>(r5, r6, r7, r8, r9);
            r0 = r36;
            r0.mWifiMulticastTimer = r4;
        L_0x020e:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x02ce;
        L_0x0214:
            r4 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r6 = 15;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r7 = r5.mAudioTurnedOnTimers;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r8 = r5.mOnBatteryTimeBase;
            r5 = r36;
            r9 = r39;
            r4.<init>(r5, r6, r7, r8, r9);
            r0 = r36;
            r0.mAudioTurnedOnTimer = r4;
        L_0x022f:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x02d5;
        L_0x0235:
            r4 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r6 = 8;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r7 = r5.mVideoTurnedOnTimers;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r8 = r5.mOnBatteryTimeBase;
            r5 = r36;
            r9 = r39;
            r4.<init>(r5, r6, r7, r8, r9);
            r0 = r36;
            r0.mVideoTurnedOnTimer = r4;
        L_0x0250:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x02dc;
        L_0x0256:
            r4 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r6 = 16;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r7 = r5.mFlashlightTurnedOnTimers;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r8 = r5.mOnBatteryTimeBase;
            r5 = r36;
            r9 = r39;
            r4.<init>(r5, r6, r7, r8, r9);
            r0 = r36;
            r0.mFlashlightTurnedOnTimer = r4;
        L_0x0271:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x02e2;
        L_0x0277:
            r4 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r6 = 17;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r7 = r5.mCameraTurnedOnTimers;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r8 = r5.mOnBatteryTimeBase;
            r5 = r36;
            r9 = r39;
            r4.<init>(r5, r6, r7, r8, r9);
            r0 = r36;
            r0.mCameraTurnedOnTimer = r4;
        L_0x0292:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x02e8;
        L_0x0298:
            r4 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r6 = 10;
            r7 = 0;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r8 = r5.mOnBatteryTimeBase;
            r5 = r36;
            r9 = r39;
            r4.<init>(r5, r6, r7, r8, r9);
            r0 = r36;
            r0.mForegroundActivityTimer = r4;
        L_0x02ae:
            r4 = 3;
            r0 = r36;
            r0.mProcessState = r4;
            r12 = 0;
        L_0x02b4:
            r4 = 3;
            if (r12 >= r4) goto L_0x02f6;
        L_0x02b7:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x02ee;
        L_0x02bd:
            r0 = r36;
            r1 = r39;
            r0.makeProcessState(r12, r1);
        L_0x02c4:
            r12 = r12 + 1;
            goto L_0x02b4;
        L_0x02c7:
            r4 = 0;
            r0 = r36;
            r0.mWifiMulticastTimer = r4;
            goto L_0x020e;
        L_0x02ce:
            r4 = 0;
            r0 = r36;
            r0.mAudioTurnedOnTimer = r4;
            goto L_0x022f;
        L_0x02d5:
            r4 = 0;
            r0 = r36;
            r0.mVideoTurnedOnTimer = r4;
            goto L_0x0250;
        L_0x02dc:
            r4 = 0;
            r0 = r36;
            r0.mFlashlightTurnedOnTimer = r4;
            goto L_0x0271;
        L_0x02e2:
            r4 = 0;
            r0 = r36;
            r0.mCameraTurnedOnTimer = r4;
            goto L_0x0292;
        L_0x02e8:
            r4 = 0;
            r0 = r36;
            r0.mForegroundActivityTimer = r4;
            goto L_0x02ae;
        L_0x02ee:
            r0 = r36;
            r4 = r0.mProcessStateTimer;
            r5 = 0;
            r4[r12] = r5;
            goto L_0x02c4;
        L_0x02f6:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x0338;
        L_0x02fc:
            r4 = new com.android.internal.os.BatteryStatsImpl$BatchTimer;
            r5 = 9;
            r0 = r36;
            r6 = com.android.internal.os.BatteryStatsImpl.this;
            r6 = r6.mOnBatteryTimeBase;
            r0 = r36;
            r1 = r39;
            r4.<init>(r0, r5, r6, r1);
            r0 = r36;
            r0.mVibratorOnTimer = r4;
        L_0x0311:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x033e;
        L_0x0317:
            r4 = 3;
            r4 = new com.android.internal.os.BatteryStatsImpl.Counter[r4];
            r0 = r36;
            r0.mUserActivityCounters = r4;
            r12 = 0;
        L_0x031f:
            r4 = 3;
            if (r12 >= r4) goto L_0x0343;
        L_0x0322:
            r0 = r36;
            r4 = r0.mUserActivityCounters;
            r5 = new com.android.internal.os.BatteryStatsImpl$Counter;
            r0 = r36;
            r6 = com.android.internal.os.BatteryStatsImpl.this;
            r6 = r6.mOnBatteryTimeBase;
            r0 = r39;
            r5.<init>(r6, r0);
            r4[r12] = r5;
            r12 = r12 + 1;
            goto L_0x031f;
        L_0x0338:
            r4 = 0;
            r0 = r36;
            r0.mVibratorOnTimer = r4;
            goto L_0x0311;
        L_0x033e:
            r4 = 0;
            r0 = r36;
            r0.mUserActivityCounters = r4;
        L_0x0343:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x03c6;
        L_0x0349:
            r4 = 4;
            r4 = new com.android.internal.os.BatteryStatsImpl.LongSamplingCounter[r4];
            r0 = r36;
            r0.mNetworkByteActivityCounters = r4;
            r4 = 4;
            r4 = new com.android.internal.os.BatteryStatsImpl.LongSamplingCounter[r4];
            r0 = r36;
            r0.mNetworkPacketActivityCounters = r4;
            r12 = 0;
        L_0x0358:
            r4 = 4;
            if (r12 >= r4) goto L_0x0384;
        L_0x035b:
            r0 = r36;
            r4 = r0.mNetworkByteActivityCounters;
            r5 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r0 = r36;
            r6 = com.android.internal.os.BatteryStatsImpl.this;
            r6 = r6.mOnBatteryTimeBase;
            r0 = r39;
            r5.<init>(r6, r0);
            r4[r12] = r5;
            r0 = r36;
            r4 = r0.mNetworkPacketActivityCounters;
            r5 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r0 = r36;
            r6 = com.android.internal.os.BatteryStatsImpl.this;
            r6 = r6.mOnBatteryTimeBase;
            r0 = r39;
            r5.<init>(r6, r0);
            r4[r12] = r5;
            r12 = r12 + 1;
            goto L_0x0358;
        L_0x0384:
            r4 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r5 = r5.mOnBatteryTimeBase;
            r0 = r39;
            r4.<init>(r5, r0);
            r0 = r36;
            r0.mMobileRadioActiveTime = r4;
            r4 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r5 = r5.mOnBatteryTimeBase;
            r0 = r39;
            r4.<init>(r5, r0);
            r0 = r36;
            r0.mMobileRadioActiveCount = r4;
        L_0x03a6:
            r12 = 0;
        L_0x03a7:
            r4 = 4;
            if (r12 >= r4) goto L_0x03d9;
        L_0x03aa:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x03d1;
        L_0x03b0:
            r0 = r36;
            r4 = r0.mWifiControllerTime;
            r5 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r0 = r36;
            r6 = com.android.internal.os.BatteryStatsImpl.this;
            r6 = r6.mOnBatteryTimeBase;
            r0 = r39;
            r5.<init>(r6, r0);
            r4[r12] = r5;
        L_0x03c3:
            r12 = r12 + 1;
            goto L_0x03a7;
        L_0x03c6:
            r4 = 0;
            r0 = r36;
            r0.mNetworkByteActivityCounters = r4;
            r4 = 0;
            r0 = r36;
            r0.mNetworkPacketActivityCounters = r4;
            goto L_0x03a6;
        L_0x03d1:
            r0 = r36;
            r4 = r0.mWifiControllerTime;
            r5 = 0;
            r4[r12] = r5;
            goto L_0x03c3;
        L_0x03d9:
            r12 = 0;
        L_0x03da:
            r4 = 4;
            if (r12 >= r4) goto L_0x0401;
        L_0x03dd:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x03f9;
        L_0x03e3:
            r0 = r36;
            r4 = r0.mBluetoothControllerTime;
            r5 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r0 = r36;
            r6 = com.android.internal.os.BatteryStatsImpl.this;
            r6 = r6.mOnBatteryTimeBase;
            r0 = r39;
            r5.<init>(r6, r0);
            r4[r12] = r5;
        L_0x03f6:
            r12 = r12 + 1;
            goto L_0x03da;
        L_0x03f9:
            r0 = r36;
            r4 = r0.mBluetoothControllerTime;
            r5 = 0;
            r4[r12] = r5;
            goto L_0x03f6;
        L_0x0401:
            r4 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r5 = r5.mOnBatteryTimeBase;
            r0 = r39;
            r4.<init>(r5, r0);
            r0 = r36;
            r0.mUserCpuTime = r4;
            r4 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r5 = r5.mOnBatteryTimeBase;
            r0 = r39;
            r4.<init>(r5, r0);
            r0 = r36;
            r0.mSystemCpuTime = r4;
            r4 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r5 = r5.mOnBatteryTimeBase;
            r0 = r39;
            r4.<init>(r5, r0);
            r0 = r36;
            r0.mCpuPower = r4;
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x04cd;
        L_0x043a:
            r17 = r39.readInt();
            r0 = r36;
            r4 = com.android.internal.os.BatteryStatsImpl.this;
            r4 = r4.mPowerProfile;
            if (r4 == 0) goto L_0x0460;
        L_0x0448:
            r0 = r36;
            r4 = com.android.internal.os.BatteryStatsImpl.this;
            r4 = r4.mPowerProfile;
            r4 = r4.getNumCpuClusters();
            r0 = r17;
            if (r4 == r0) goto L_0x0460;
        L_0x0458:
            r4 = new android.os.ParcelFormatException;
            r5 = "Incompatible number of cpu clusters";
            r4.<init>(r5);
            throw r4;
        L_0x0460:
            r0 = r17;
            r4 = new com.android.internal.os.BatteryStatsImpl.LongSamplingCounter[r0][];
            r0 = r36;
            r0.mCpuClusterSpeed = r4;
            r10 = 0;
        L_0x0469:
            r0 = r17;
            if (r10 >= r0) goto L_0x04d4;
        L_0x046d:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x04c3;
        L_0x0473:
            r22 = r39.readInt();
            r0 = r36;
            r4 = com.android.internal.os.BatteryStatsImpl.this;
            r4 = r4.mPowerProfile;
            if (r4 == 0) goto L_0x0499;
        L_0x0481:
            r0 = r36;
            r4 = com.android.internal.os.BatteryStatsImpl.this;
            r4 = r4.mPowerProfile;
            r4 = r4.getNumSpeedStepsInCpuCluster(r10);
            r0 = r22;
            if (r4 == r0) goto L_0x0499;
        L_0x0491:
            r4 = new android.os.ParcelFormatException;
            r5 = "Incompatible number of cpu speeds";
            r4.<init>(r5);
            throw r4;
        L_0x0499:
            r0 = r22;
            r11 = new com.android.internal.os.BatteryStatsImpl.LongSamplingCounter[r0];
            r0 = r36;
            r4 = r0.mCpuClusterSpeed;
            r4[r10] = r11;
            r31 = 0;
        L_0x04a5:
            r0 = r31;
            r1 = r22;
            if (r0 >= r1) goto L_0x04ca;
        L_0x04ab:
            r4 = r39.readInt();
            if (r4 == 0) goto L_0x04c0;
        L_0x04b1:
            r4 = new com.android.internal.os.BatteryStatsImpl$LongSamplingCounter;
            r0 = r36;
            r5 = com.android.internal.os.BatteryStatsImpl.this;
            r5 = r5.mOnBatteryTimeBase;
            r0 = r39;
            r4.<init>(r5, r0);
            r11[r31] = r4;
        L_0x04c0:
            r31 = r31 + 1;
            goto L_0x04a5;
        L_0x04c3:
            r0 = r36;
            r4 = r0.mCpuClusterSpeed;
            r5 = 0;
            r4[r10] = r5;
        L_0x04ca:
            r10 = r10 + 1;
            goto L_0x0469;
        L_0x04cd:
            r4 = 0;
            r4 = (com.android.internal.os.BatteryStatsImpl.LongSamplingCounter[][]) r4;
            r0 = r36;
            r0.mCpuClusterSpeed = r4;
        L_0x04d4:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.readFromParcelLocked(com.android.internal.os.BatteryStatsImpl$TimeBase, com.android.internal.os.BatteryStatsImpl$TimeBase, android.os.Parcel):void");
        }

        public com.android.internal.os.BatteryStatsImpl.Uid.Proc getProcessStatsLocked(java.lang.String r3) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r1 = r2.mProcessStats;
            r0 = r1.get(r3);
            r0 = (com.android.internal.os.BatteryStatsImpl.Uid.Proc) r0;
            if (r0 != 0) goto L_0x0014;
        L_0x000a:
            r0 = new com.android.internal.os.BatteryStatsImpl$Uid$Proc;
            r0.<init>(r3);
            r1 = r2.mProcessStats;
            r1.put(r3, r0);
        L_0x0014:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getProcessStatsLocked(java.lang.String):com.android.internal.os.BatteryStatsImpl$Uid$Proc");
        }

        public void updateProcessStateLocked(java.lang.String r4, int r5, long r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r3 = this;
            r1 = 6;
            if (r5 > r1) goto L_0x0008;
        L_0x0003:
            r0 = 0;
        L_0x0004:
            r3.updateRealProcessStateLocked(r4, r0, r6);
            return;
        L_0x0008:
            r1 = 11;
            if (r5 > r1) goto L_0x000e;
        L_0x000c:
            r0 = 1;
            goto L_0x0004;
        L_0x000e:
            r0 = 2;
            goto L_0x0004;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.updateProcessStateLocked(java.lang.String, int, long):void");
        }

        public void updateRealProcessStateLocked(java.lang.String r8, int r9, long r10) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r7 = this;
            r0 = 1;
            r4 = 0;
            r2 = r7.getProcessStatsLocked(r8);
            r5 = r2.mProcessState;
            if (r5 == r9) goto L_0x0040;
        L_0x000a:
            r5 = r2.mProcessState;
            if (r9 >= r5) goto L_0x0034;
        L_0x000e:
            r5 = r7.mProcessState;
            if (r5 <= r9) goto L_0x0032;
        L_0x0012:
            r2.mProcessState = r9;
            if (r0 == 0) goto L_0x0040;
        L_0x0016:
            r3 = 3;
            r4 = r7.mProcessStats;
            r4 = r4.size();
            r1 = r4 + -1;
        L_0x001f:
            if (r1 < 0) goto L_0x003d;
        L_0x0021:
            r4 = r7.mProcessStats;
            r2 = r4.valueAt(r1);
            r2 = (com.android.internal.os.BatteryStatsImpl.Uid.Proc) r2;
            r4 = r2.mProcessState;
            if (r4 >= r3) goto L_0x002f;
        L_0x002d:
            r3 = r2.mProcessState;
        L_0x002f:
            r1 = r1 + -1;
            goto L_0x001f;
        L_0x0032:
            r0 = r4;
            goto L_0x0012;
        L_0x0034:
            r5 = r7.mProcessState;
            r6 = r2.mProcessState;
            if (r5 != r6) goto L_0x003b;
        L_0x003a:
            goto L_0x0012;
        L_0x003b:
            r0 = r4;
            goto L_0x003a;
        L_0x003d:
            r7.updateUidProcessStateLocked(r3, r10);
        L_0x0040:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.updateRealProcessStateLocked(java.lang.String, int, long):void");
        }

        public android.util.SparseArray<? extends android.os.BatteryStats.Uid.Pid> getPidStats() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = r1.mPids;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getPidStats():android.util.SparseArray<? extends android.os.BatteryStats$Uid$Pid>");
        }

        public android.os.BatteryStats.Uid.Pid getPidStatsLocked(int r3) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r1 = r2.mPids;
            r0 = r1.get(r3);
            r0 = (android.os.BatteryStats.Uid.Pid) r0;
            if (r0 != 0) goto L_0x0014;
        L_0x000a:
            r0 = new android.os.BatteryStats$Uid$Pid;
            r0.<init>(r2);
            r1 = r2.mPids;
            r1.put(r3, r0);
        L_0x0014:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getPidStatsLocked(int):android.os.BatteryStats$Uid$Pid");
        }

        public com.android.internal.os.BatteryStatsImpl.Uid.Pkg getPackageStatsLocked(java.lang.String r3) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r1 = r2.mPackageStats;
            r0 = r1.get(r3);
            r0 = (com.android.internal.os.BatteryStatsImpl.Uid.Pkg) r0;
            if (r0 != 0) goto L_0x0014;
        L_0x000a:
            r0 = new com.android.internal.os.BatteryStatsImpl$Uid$Pkg;
            r0.<init>();
            r1 = r2.mPackageStats;
            r1.put(r3, r0);
        L_0x0014:
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getPackageStatsLocked(java.lang.String):com.android.internal.os.BatteryStatsImpl$Uid$Pkg");
        }

        public com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv getServiceStatsLocked(java.lang.String r4, java.lang.String r5) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r3 = this;
            r0 = r3.getPackageStatsLocked(r4);
            r2 = r0.mServiceStats;
            r1 = r2.get(r5);
            r1 = (com.android.internal.os.BatteryStatsImpl.Uid.Pkg.Serv) r1;
            if (r1 != 0) goto L_0x0017;
        L_0x000e:
            r1 = r0.newServiceStatsLocked();
            r2 = r0.mServiceStats;
            r2.put(r5, r1);
        L_0x0017:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getServiceStatsLocked(java.lang.String, java.lang.String):com.android.internal.os.BatteryStatsImpl$Uid$Pkg$Serv");
        }

        public void readSyncSummaryFromParcelLocked(java.lang.String r3, android.os.Parcel r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r1 = r2.mSyncStats;
            r0 = r1.instantiateObject();
            r0 = (com.android.internal.os.BatteryStatsImpl.StopwatchTimer) r0;
            r0.readSummaryFromParcelLocked(r4);
            r1 = r2.mSyncStats;
            r1.add(r3, r0);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.readSyncSummaryFromParcelLocked(java.lang.String, android.os.Parcel):void");
        }

        public void readJobSummaryFromParcelLocked(java.lang.String r3, android.os.Parcel r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r1 = r2.mJobStats;
            r0 = r1.instantiateObject();
            r0 = (com.android.internal.os.BatteryStatsImpl.StopwatchTimer) r0;
            r0.readSummaryFromParcelLocked(r4);
            r1 = r2.mJobStats;
            r1.add(r3, r0);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.readJobSummaryFromParcelLocked(java.lang.String, android.os.Parcel):void");
        }

        public void readWakeSummaryFromParcelLocked(java.lang.String r3, android.os.Parcel r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r0 = new com.android.internal.os.BatteryStatsImpl$Uid$Wakelock;
            r0.<init>();
            r1 = r2.mWakelockStats;
            r1.add(r3, r0);
            r1 = r4.readInt();
            if (r1 == 0) goto L_0x0018;
        L_0x0010:
            r1 = 1;
            r1 = r0.getStopwatchTimer(r1);
            r1.readSummaryFromParcelLocked(r4);
        L_0x0018:
            r1 = r4.readInt();
            if (r1 == 0) goto L_0x0026;
        L_0x001e:
            r1 = 0;
            r1 = r0.getStopwatchTimer(r1);
            r1.readSummaryFromParcelLocked(r4);
        L_0x0026:
            r1 = r4.readInt();
            if (r1 == 0) goto L_0x0034;
        L_0x002c:
            r1 = 2;
            r1 = r0.getStopwatchTimer(r1);
            r1.readSummaryFromParcelLocked(r4);
        L_0x0034:
            r1 = r4.readInt();
            if (r1 == 0) goto L_0x0043;
        L_0x003a:
            r1 = 18;
            r1 = r0.getStopwatchTimer(r1);
            r1.readSummaryFromParcelLocked(r4);
        L_0x0043:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.readWakeSummaryFromParcelLocked(java.lang.String, android.os.Parcel):void");
        }

        public com.android.internal.os.BatteryStatsImpl.StopwatchTimer getSensorTimerLocked(int r6, boolean r7) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r5 = this;
            r3 = r5.mSensorStats;
            r0 = r3.get(r6);
            r0 = (com.android.internal.os.BatteryStatsImpl.Uid.Sensor) r0;
            if (r0 != 0) goto L_0x0018;
        L_0x000a:
            if (r7 != 0) goto L_0x000e;
        L_0x000c:
            r1 = 0;
        L_0x000d:
            return r1;
        L_0x000e:
            r0 = new com.android.internal.os.BatteryStatsImpl$Uid$Sensor;
            r0.<init>(r6);
            r3 = r5.mSensorStats;
            r3.put(r6, r0);
        L_0x0018:
            r1 = r0.mTimer;
            if (r1 != 0) goto L_0x000d;
        L_0x001c:
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mSensorTimers;
            r2 = r3.get(r6);
            r2 = (java.util.ArrayList) r2;
            if (r2 != 0) goto L_0x0034;
        L_0x0028:
            r2 = new java.util.ArrayList;
            r2.<init>();
            r3 = com.android.internal.os.BatteryStatsImpl.this;
            r3 = r3.mSensorTimers;
            r3.put(r6, r2);
        L_0x0034:
            r1 = new com.android.internal.os.BatteryStatsImpl$StopwatchTimer;
            r3 = 3;
            r4 = com.android.internal.os.BatteryStatsImpl.this;
            r4 = r4.mOnBatteryTimeBase;
            r1.<init>(r5, r3, r2, r4);
            r0.mTimer = r1;
            goto L_0x000d;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getSensorTimerLocked(int, boolean):com.android.internal.os.BatteryStatsImpl$StopwatchTimer");
        }

        public void noteStartSyncLocked(java.lang.String r3, long r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r1 = r2.mSyncStats;
            r0 = r1.startObject(r3);
            r0 = (com.android.internal.os.BatteryStatsImpl.StopwatchTimer) r0;
            if (r0 == 0) goto L_0x000d;
        L_0x000a:
            r0.startRunningLocked(r4);
        L_0x000d:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteStartSyncLocked(java.lang.String, long):void");
        }

        public void noteStopSyncLocked(java.lang.String r3, long r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r1 = r2.mSyncStats;
            r0 = r1.stopObject(r3);
            r0 = (com.android.internal.os.BatteryStatsImpl.StopwatchTimer) r0;
            if (r0 == 0) goto L_0x000d;
        L_0x000a:
            r0.stopRunningLocked(r4);
        L_0x000d:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteStopSyncLocked(java.lang.String, long):void");
        }

        public void noteStartJobLocked(java.lang.String r3, long r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r1 = r2.mJobStats;
            r0 = r1.startObject(r3);
            r0 = (com.android.internal.os.BatteryStatsImpl.StopwatchTimer) r0;
            if (r0 == 0) goto L_0x000d;
        L_0x000a:
            r0.startRunningLocked(r4);
        L_0x000d:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteStartJobLocked(java.lang.String, long):void");
        }

        public void noteStopJobLocked(java.lang.String r3, long r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r1 = r2.mJobStats;
            r0 = r1.stopObject(r3);
            r0 = (com.android.internal.os.BatteryStatsImpl.StopwatchTimer) r0;
            if (r0 == 0) goto L_0x000d;
        L_0x000a:
            r0.stopRunningLocked(r4);
        L_0x000d:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteStopJobLocked(java.lang.String, long):void");
        }

        public void noteStartWakeLocked(int r5, java.lang.String r6, int r7, long r8) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r4 = this;
            r2 = r4.mWakelockStats;
            r1 = r2.startObject(r6);
            r1 = (com.android.internal.os.BatteryStatsImpl.Uid.Wakelock) r1;
            if (r1 == 0) goto L_0x0011;
        L_0x000a:
            r2 = r1.getStopwatchTimer(r7);
            r2.startRunningLocked(r8);
        L_0x0011:
            if (r5 < 0) goto L_0x0023;
        L_0x0013:
            if (r7 != 0) goto L_0x0023;
        L_0x0015:
            r0 = r4.getPidStatsLocked(r5);
            r2 = r0.mWakeNesting;
            r3 = r2 + 1;
            r0.mWakeNesting = r3;
            if (r2 != 0) goto L_0x0023;
        L_0x0021:
            r0.mWakeStartMs = r8;
        L_0x0023:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteStartWakeLocked(int, java.lang.String, int, long):void");
        }

        public void noteStopWakeLocked(int r7, java.lang.String r8, int r9, long r10) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r6 = this;
            r2 = r6.mWakelockStats;
            r1 = r2.stopObject(r8);
            r1 = (com.android.internal.os.BatteryStatsImpl.Uid.Wakelock) r1;
            if (r1 == 0) goto L_0x0011;
        L_0x000a:
            r2 = r1.getStopwatchTimer(r9);
            r2.stopRunningLocked(r10);
        L_0x0011:
            if (r7 < 0) goto L_0x0039;
        L_0x0013:
            if (r9 != 0) goto L_0x0039;
        L_0x0015:
            r2 = r6.mPids;
            r0 = r2.get(r7);
            r0 = (android.os.BatteryStats.Uid.Pid) r0;
            if (r0 == 0) goto L_0x0039;
        L_0x001f:
            r2 = r0.mWakeNesting;
            if (r2 <= 0) goto L_0x0039;
        L_0x0023:
            r2 = r0.mWakeNesting;
            r3 = r2 + -1;
            r0.mWakeNesting = r3;
            r3 = 1;
            if (r2 != r3) goto L_0x0039;
        L_0x002c:
            r2 = r0.mWakeSumMs;
            r4 = r0.mWakeStartMs;
            r4 = r10 - r4;
            r2 = r2 + r4;
            r0.mWakeSumMs = r2;
            r2 = 0;
            r0.mWakeStartMs = r2;
        L_0x0039:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteStopWakeLocked(int, java.lang.String, int, long):void");
        }

        public void reportExcessiveWakeLocked(java.lang.String r3, long r4, long r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r0 = r2.getProcessStatsLocked(r3);
            if (r0 == 0) goto L_0x0009;
        L_0x0006:
            r0.addExcessiveWake(r4, r6);
        L_0x0009:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.reportExcessiveWakeLocked(java.lang.String, long, long):void");
        }

        public void reportExcessiveCpuLocked(java.lang.String r3, long r4, long r6) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r0 = r2.getProcessStatsLocked(r3);
            if (r0 == 0) goto L_0x0009;
        L_0x0006:
            r0.addExcessiveCpu(r4, r6);
        L_0x0009:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.reportExcessiveCpuLocked(java.lang.String, long, long):void");
        }

        public void noteStartSensor(int r3, long r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r1 = 1;
            r0 = r2.getSensorTimerLocked(r3, r1);
            if (r0 == 0) goto L_0x000a;
        L_0x0007:
            r0.startRunningLocked(r4);
        L_0x000a:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteStartSensor(int, long):void");
        }

        public void noteStopSensor(int r3, long r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r2 = this;
            r1 = 0;
            r0 = r2.getSensorTimerLocked(r3, r1);
            if (r0 == 0) goto L_0x000a;
        L_0x0007:
            r0.stopRunningLocked(r4);
        L_0x000a:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteStopSensor(int, long):void");
        }

        public void noteStartGps(long r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r3 = this;
            r1 = -10000; // 0xffffffffffffd8f0 float:NaN double:NaN;
            r2 = 1;
            r0 = r3.getSensorTimerLocked(r1, r2);
            if (r0 == 0) goto L_0x000c;
        L_0x0009:
            r0.startRunningLocked(r4);
        L_0x000c:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteStartGps(long):void");
        }

        public void noteStopGps(long r4) {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r3 = this;
            r1 = -10000; // 0xffffffffffffd8f0 float:NaN double:NaN;
            r2 = 0;
            r0 = r3.getSensorTimerLocked(r1, r2);
            if (r0 == 0) goto L_0x000c;
        L_0x0009:
            r0.stopRunningLocked(r4);
        L_0x000c:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.noteStopGps(long):void");
        }

        public com.android.internal.os.BatteryStatsImpl getBatteryStats() {
            /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: SSA rename variables already executed
	at jadx.core.dex.visitors.ssa.SSATransform.renameVariables(SSATransform.java:120)
	at jadx.core.dex.visitors.ssa.SSATransform.process(SSATransform.java:52)
	at jadx.core.dex.visitors.ssa.SSATransform.visit(SSATransform.java:42)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
            /*
            r1 = this;
            r0 = com.android.internal.os.BatteryStatsImpl.this;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.Uid.getBatteryStats():com.android.internal.os.BatteryStatsImpl");
        }
    }

    public Map<String, ? extends Timer> getKernelWakelockStats() {
        return this.mKernelWakelockStats;
    }

    public Map<String, ? extends Timer> getWakeupReasonStats() {
        return this.mWakeupReasonStats;
    }

    public BatteryStatsImpl() {
        this.mKernelWakelockReader = new KernelWakelockReader();
        this.mTmpWakelockStats = new KernelWakelockStats();
        this.mKernelUidCpuTimeReader = new KernelUidCpuTimeReader();
        this.mIsolatedUids = new SparseIntArray();
        this.mUidStats = new SparseArray();
        this.mPartialTimers = new ArrayList();
        this.mFullTimers = new ArrayList();
        this.mWindowTimers = new ArrayList();
        this.mDrawTimers = new ArrayList();
        this.mSensorTimers = new SparseArray();
        this.mWifiRunningTimers = new ArrayList();
        this.mFullWifiLockTimers = new ArrayList();
        this.mWifiMulticastTimers = new ArrayList();
        this.mWifiScanTimers = new ArrayList();
        this.mWifiBatchedScanTimers = new SparseArray();
        this.mAudioTurnedOnTimers = new ArrayList();
        this.mVideoTurnedOnTimers = new ArrayList();
        this.mFlashlightTurnedOnTimers = new ArrayList();
        this.mCameraTurnedOnTimers = new ArrayList();
        this.mLastPartialTimers = new ArrayList();
        this.mOnBatteryTimeBase = new TimeBase();
        this.mOnBatteryScreenOffTimeBase = new TimeBase();
        this.mActiveEvents = new HistoryEventTracker();
        this.mHaveBatteryLevel = false;
        this.mRecordingHistory = false;
        this.mHistoryBuffer = Parcel.obtain();
        this.mHistoryLastWritten = new HistoryItem();
        this.mHistoryLastLastWritten = new HistoryItem();
        this.mHistoryReadTmp = new HistoryItem();
        this.mHistoryAddTmp = new HistoryItem();
        this.mHistoryTagPool = new HashMap();
        this.mNextHistoryTagIdx = 0;
        this.mNumHistoryTagChars = 0;
        this.mHistoryBufferLastPos = -1;
        this.mHistoryOverflow = false;
        this.mActiveHistoryStates = -1;
        this.mActiveHistoryStates2 = -1;
        this.mLastHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryUptime = 0;
        this.mHistoryCur = new HistoryItem();
        this.mLastHistoryStepDetails = null;
        this.mLastHistoryStepLevel = (byte) 0;
        this.mCurHistoryStepDetails = new HistoryStepDetails();
        this.mReadHistoryStepDetails = new HistoryStepDetails();
        this.mTmpHistoryStepDetails = new HistoryStepDetails();
        this.mScreenState = 0;
        this.mScreenBrightnessBin = -1;
        this.mScreenBrightnessTimer = new StopwatchTimer[5];
        this.mPhoneSignalStrengthBin = -1;
        this.mPhoneSignalStrengthBinRaw = -1;
        this.mPhoneSignalStrengthsTimer = new StopwatchTimer[SignalStrength.NUM_SIGNAL_STRENGTH_BINS];
        this.mPhoneDataConnectionType = -1;
        this.mPhoneDataConnectionsTimer = new StopwatchTimer[17];
        this.mNetworkByteActivityCounters = new LongSamplingCounter[4];
        this.mNetworkPacketActivityCounters = new LongSamplingCounter[4];
        this.mBluetoothActivityCounters = new LongSamplingCounter[4];
        this.mWifiActivityCounters = new LongSamplingCounter[4];
        this.mWifiState = -1;
        this.mWifiStateTimer = new StopwatchTimer[8];
        this.mWifiSupplState = -1;
        this.mWifiSupplStateTimer = new StopwatchTimer[13];
        this.mWifiSignalStrengthBin = -1;
        this.mWifiSignalStrengthsTimer = new StopwatchTimer[5];
        this.mMobileRadioPowerState = DataConnectionRealTimeInfo.DC_POWER_STATE_LOW;
        this.mWifiRadioPowerState = DataConnectionRealTimeInfo.DC_POWER_STATE_LOW;
        this.mCharging = true;
        this.mInitStepMode = 0;
        this.mCurStepMode = 0;
        this.mModStepMode = 0;
        this.mDischargeStepTracker = new LevelStepTracker(200);
        this.mDailyDischargeStepTracker = new LevelStepTracker(400);
        this.mChargeStepTracker = new LevelStepTracker(200);
        this.mDailyChargeStepTracker = new LevelStepTracker(400);
        this.mDailyStartTime = 0;
        this.mNextMinDailyDeadline = 0;
        this.mNextMaxDailyDeadline = 0;
        this.mDailyItems = new ArrayList();
        this.mLastWriteTime = 0;
        this.mPhoneServiceState = -1;
        this.mPhoneServiceStateRaw = -1;
        this.mPhoneSimStateRaw = -1;
        this.mTmpNetworkStatsEntry = new Entry();
        this.mHasWifiEnergyReporting = false;
        this.mHasBluetoothEnergyReporting = false;
        this.mKernelWakelockStats = new HashMap();
        this.mLastWakeupReason = null;
        this.mLastWakeupUptimeMs = 0;
        this.mWakeupReasonStats = new HashMap();
        this.mChangedStates = 0;
        this.mChangedStates2 = 0;
        this.mInitialAcquireWakeUid = -1;
        this.mWifiFullLockNesting = 0;
        this.mWifiScanNesting = 0;
        this.mWifiMulticastNesting = 0;
        this.mMobileIfaces = EmptyArray.STRING;
        this.mWifiIfaces = EmptyArray.STRING;
        this.mNetworkStatsFactory = new NetworkStatsFactory();
        this.mMobileNetworkStats = new NetworkStats[]{new NetworkStats(SystemClock.elapsedRealtime(), 50), new NetworkStats(SystemClock.elapsedRealtime(), 50), new NetworkStats(SystemClock.elapsedRealtime(), 50)};
        this.mWifiNetworkStats = new NetworkStats[]{new NetworkStats(SystemClock.elapsedRealtime(), 50), new NetworkStats(SystemClock.elapsedRealtime(), 50), new NetworkStats(SystemClock.elapsedRealtime(), 50)};
        this.mFeatureComputeChargeTimeModel = true;
        this.CHARGE_TIME_PATH = "/sys/class/power_supply/battery/time_to_full_now";
        this.mPendingWrite = null;
        this.mWriteLock = new ReentrantLock();
        this.mFile = null;
        this.mCheckinFile = null;
        this.mDailyFile = null;
        this.mHandler = null;
        this.mExternalSync = null;
        clearHistoryLocked();
    }

    public SamplingTimer getWakeupReasonTimerLocked(String name) {
        SamplingTimer timer = (SamplingTimer) this.mWakeupReasonStats.get(name);
        if (timer != null) {
            return timer;
        }
        timer = new SamplingTimer(this.mOnBatteryTimeBase, true);
        this.mWakeupReasonStats.put(name, timer);
        return timer;
    }

    public SamplingTimer getKernelWakelockTimerLocked(String name) {
        SamplingTimer kwlt = (SamplingTimer) this.mKernelWakelockStats.get(name);
        if (kwlt != null) {
            return kwlt;
        }
        kwlt = new SamplingTimer(this.mOnBatteryScreenOffTimeBase, true);
        this.mKernelWakelockStats.put(name, kwlt);
        return kwlt;
    }

    private int writeHistoryTag(HistoryTag tag) {
        Integer idxObj = (Integer) this.mHistoryTagPool.get(tag);
        if (idxObj != null) {
            return idxObj.intValue();
        }
        int idx = this.mNextHistoryTagIdx;
        HistoryTag key = new HistoryTag();
        key.setTo(tag);
        tag.poolIdx = idx;
        this.mHistoryTagPool.put(key, Integer.valueOf(idx));
        this.mNextHistoryTagIdx++;
        this.mNumHistoryTagChars += key.string.length() + 1;
        return idx;
    }

    private void readHistoryTag(int index, HistoryTag tag) {
        tag.string = this.mReadHistoryStrings[index];
        tag.uid = this.mReadHistoryUids[index];
        tag.poolIdx = index;
    }

    public void writeHistoryDelta(Parcel dest, HistoryItem cur, HistoryItem last) {
        if (last == null || cur.cmd != (byte) 0) {
            dest.writeInt(DELTA_TIME_ABS);
            cur.writeToParcel(dest, 0);
            return;
        }
        int deltaTimeToken;
        long deltaTime = cur.time - last.time;
        int lastBatteryLevelInt = buildBatteryLevelInt(last);
        int lastStateInt = buildStateInt(last);
        if (deltaTime < 0 || deltaTime > 2147483647L) {
            deltaTimeToken = EventLogTags.SYSUI_VIEW_VISIBILITY;
        } else if (deltaTime >= 524285) {
            deltaTimeToken = DELTA_TIME_INT;
        } else {
            deltaTimeToken = (int) deltaTime;
        }
        int firstToken = deltaTimeToken | (cur.states & -16777216);
        int includeStepDetails = this.mLastHistoryStepLevel > cur.batteryLevel ? 1 : 0;
        boolean computeStepDetails = includeStepDetails != 0 || this.mLastHistoryStepDetails == null;
        int batteryLevelInt = buildBatteryLevelInt(cur) | includeStepDetails;
        boolean batteryLevelIntChanged = batteryLevelInt != lastBatteryLevelInt;
        if (batteryLevelIntChanged) {
            firstToken |= 524288;
        }
        int stateInt = buildStateInt(cur);
        boolean stateIntChanged = stateInt != lastStateInt;
        if (stateIntChanged) {
            firstToken |= 1048576;
        }
        boolean state2IntChanged = cur.states2 != last.states2;
        if (state2IntChanged) {
            firstToken |= 2097152;
        }
        if (!(cur.wakelockTag == null && cur.wakeReasonTag == null)) {
            firstToken |= 4194304;
        }
        if (cur.eventCode != 0) {
            firstToken |= 8388608;
        }
        dest.writeInt(firstToken);
        if (deltaTimeToken >= DELTA_TIME_INT) {
            if (deltaTimeToken == DELTA_TIME_INT) {
                dest.writeInt((int) deltaTime);
            } else {
                dest.writeLong(deltaTime);
            }
        }
        if (batteryLevelIntChanged) {
            dest.writeInt(batteryLevelInt);
        }
        if (stateIntChanged) {
            dest.writeInt(stateInt);
        }
        if (state2IntChanged) {
            dest.writeInt(cur.states2);
        }
        if (!(cur.wakelockTag == null && cur.wakeReasonTag == null)) {
            int wakeLockIndex;
            int wakeReasonIndex;
            if (cur.wakelockTag != null) {
                wakeLockIndex = writeHistoryTag(cur.wakelockTag);
            } else {
                wakeLockIndex = 65535;
            }
            if (cur.wakeReasonTag != null) {
                wakeReasonIndex = writeHistoryTag(cur.wakeReasonTag);
            } else {
                wakeReasonIndex = 65535;
            }
            dest.writeInt((wakeReasonIndex << 16) | wakeLockIndex);
        }
        if (cur.eventCode != 0) {
            dest.writeInt((cur.eventCode & 65535) | (writeHistoryTag(cur.eventTag) << 16));
        }
        if (computeStepDetails) {
            computeHistoryStepDetails(this.mCurHistoryStepDetails, this.mLastHistoryStepDetails);
            if (includeStepDetails != 0) {
                this.mCurHistoryStepDetails.writeToParcel(dest);
            }
            cur.stepDetails = this.mCurHistoryStepDetails;
            this.mLastHistoryStepDetails = this.mCurHistoryStepDetails;
        } else {
            cur.stepDetails = null;
        }
        if (this.mLastHistoryStepLevel < cur.batteryLevel) {
            this.mLastHistoryStepDetails = null;
        }
        this.mLastHistoryStepLevel = cur.batteryLevel;
    }

    private int buildBatteryLevelInt(HistoryItem h) {
        return (((h.batteryLevel << 25) & -33554432) | ((h.batteryTemperature << 14) & 33521664)) | ((h.batteryVoltage << 1) & 32767);
    }

    private int buildStateInt(HistoryItem h) {
        int plugType = 0;
        if ((h.batteryPlugType & 1) != 0) {
            plugType = 1;
        } else if ((h.batteryPlugType & 2) != 0) {
            plugType = 2;
        } else if ((h.batteryPlugType & 4) != 0) {
            plugType = 3;
        }
        return ((((h.batteryStatus & 7) << 29) | ((h.batteryHealth & 7) << 26)) | ((plugType & 3) << 24)) | (h.states & 16777215);
    }

    private void computeHistoryStepDetails(HistoryStepDetails out, HistoryStepDetails last) {
        HistoryStepDetails tmp;
        if (last != null) {
            tmp = this.mTmpHistoryStepDetails;
        } else {
            tmp = out;
        }
        requestImmediateCpuUpdate();
        int NU;
        int i;
        if (last == null) {
            NU = this.mUidStats.size();
            for (i = 0; i < NU; i++) {
                Uid uid = (Uid) this.mUidStats.valueAt(i);
                uid.mLastStepUserTime = uid.mCurStepUserTime;
                uid.mLastStepSystemTime = uid.mCurStepSystemTime;
            }
            this.mLastStepCpuUserTime = this.mCurStepCpuUserTime;
            this.mLastStepCpuSystemTime = this.mCurStepCpuSystemTime;
            this.mLastStepStatUserTime = this.mCurStepStatUserTime;
            this.mLastStepStatSystemTime = this.mCurStepStatSystemTime;
            this.mLastStepStatIOWaitTime = this.mCurStepStatIOWaitTime;
            this.mLastStepStatIrqTime = this.mCurStepStatIrqTime;
            this.mLastStepStatSoftIrqTime = this.mCurStepStatSoftIrqTime;
            this.mLastStepStatIdleTime = this.mCurStepStatIdleTime;
            tmp.clear();
            return;
        }
        out.userTime = (int) (this.mCurStepCpuUserTime - this.mLastStepCpuUserTime);
        out.systemTime = (int) (this.mCurStepCpuSystemTime - this.mLastStepCpuSystemTime);
        out.statUserTime = (int) (this.mCurStepStatUserTime - this.mLastStepStatUserTime);
        out.statSystemTime = (int) (this.mCurStepStatSystemTime - this.mLastStepStatSystemTime);
        out.statIOWaitTime = (int) (this.mCurStepStatIOWaitTime - this.mLastStepStatIOWaitTime);
        out.statIrqTime = (int) (this.mCurStepStatIrqTime - this.mLastStepStatIrqTime);
        out.statSoftIrqTime = (int) (this.mCurStepStatSoftIrqTime - this.mLastStepStatSoftIrqTime);
        out.statIdlTime = (int) (this.mCurStepStatIdleTime - this.mLastStepStatIdleTime);
        out.appCpuUid3 = -1;
        out.appCpuUid2 = -1;
        out.appCpuUid1 = -1;
        out.appCpuUTime3 = 0;
        out.appCpuUTime2 = 0;
        out.appCpuUTime1 = 0;
        out.appCpuSTime3 = 0;
        out.appCpuSTime2 = 0;
        out.appCpuSTime1 = 0;
        NU = this.mUidStats.size();
        for (i = 0; i < NU; i++) {
            uid = (Uid) this.mUidStats.valueAt(i);
            int totalUTime = (int) (uid.mCurStepUserTime - uid.mLastStepUserTime);
            int totalSTime = (int) (uid.mCurStepSystemTime - uid.mLastStepSystemTime);
            int totalTime = totalUTime + totalSTime;
            uid.mLastStepUserTime = uid.mCurStepUserTime;
            uid.mLastStepSystemTime = uid.mCurStepSystemTime;
            if (totalTime > out.appCpuUTime3 + out.appCpuSTime3) {
                if (totalTime <= out.appCpuUTime2 + out.appCpuSTime2) {
                    out.appCpuUid3 = uid.mUid;
                    out.appCpuUTime3 = totalUTime;
                    out.appCpuSTime3 = totalSTime;
                } else {
                    out.appCpuUid3 = out.appCpuUid2;
                    out.appCpuUTime3 = out.appCpuUTime2;
                    out.appCpuSTime3 = out.appCpuSTime2;
                    if (totalTime <= out.appCpuUTime1 + out.appCpuSTime1) {
                        out.appCpuUid2 = uid.mUid;
                        out.appCpuUTime2 = totalUTime;
                        out.appCpuSTime2 = totalSTime;
                    } else {
                        out.appCpuUid2 = out.appCpuUid1;
                        out.appCpuUTime2 = out.appCpuUTime1;
                        out.appCpuSTime2 = out.appCpuSTime1;
                        out.appCpuUid1 = uid.mUid;
                        out.appCpuUTime1 = totalUTime;
                        out.appCpuSTime1 = totalSTime;
                    }
                }
            }
        }
        this.mLastStepCpuUserTime = this.mCurStepCpuUserTime;
        this.mLastStepCpuSystemTime = this.mCurStepCpuSystemTime;
        this.mLastStepStatUserTime = this.mCurStepStatUserTime;
        this.mLastStepStatSystemTime = this.mCurStepStatSystemTime;
        this.mLastStepStatIOWaitTime = this.mCurStepStatIOWaitTime;
        this.mLastStepStatIrqTime = this.mCurStepStatIrqTime;
        this.mLastStepStatSoftIrqTime = this.mCurStepStatSoftIrqTime;
        this.mLastStepStatIdleTime = this.mCurStepStatIdleTime;
    }

    public void readHistoryDelta(Parcel src, HistoryItem cur) {
        int batteryLevelInt;
        int firstToken = src.readInt();
        int deltaTimeToken = firstToken & EventLogTags.SYSUI_VIEW_VISIBILITY;
        cur.cmd = (byte) 0;
        cur.numReadInts = 1;
        if (deltaTimeToken < DELTA_TIME_ABS) {
            cur.time += (long) deltaTimeToken;
        } else if (deltaTimeToken == DELTA_TIME_ABS) {
            cur.time = src.readLong();
            cur.numReadInts += 2;
            cur.readFromParcel(src);
            return;
        } else if (deltaTimeToken == DELTA_TIME_INT) {
            cur.time += (long) src.readInt();
            cur.numReadInts++;
        } else {
            cur.time += src.readLong();
            cur.numReadInts += 2;
        }
        if ((524288 & firstToken) != 0) {
            batteryLevelInt = src.readInt();
            cur.batteryLevel = (byte) ((batteryLevelInt >> 25) & 127);
            cur.batteryTemperature = (short) ((batteryLevelInt << 7) >> 21);
            cur.batteryVoltage = (char) ((batteryLevelInt >> 1) & View.PUBLIC_STATUS_BAR_VISIBILITY_MASK);
            cur.numReadInts++;
        } else {
            batteryLevelInt = 0;
        }
        if ((1048576 & firstToken) != 0) {
            int stateInt = src.readInt();
            cur.states = (-16777216 & firstToken) | (16777215 & stateInt);
            cur.batteryStatus = (byte) ((stateInt >> 29) & 7);
            cur.batteryHealth = (byte) ((stateInt >> 26) & 7);
            cur.batteryPlugType = (byte) ((stateInt >> 24) & 3);
            switch (cur.batteryPlugType) {
                case (byte) 1:
                    cur.batteryPlugType = (byte) 1;
                    break;
                case (byte) 2:
                    cur.batteryPlugType = (byte) 2;
                    break;
                case (byte) 3:
                    cur.batteryPlugType = (byte) 4;
                    break;
            }
            cur.numReadInts++;
        } else {
            cur.states = (-16777216 & firstToken) | (cur.states & 16777215);
        }
        if ((2097152 & firstToken) != 0) {
            cur.states2 = src.readInt();
        }
        if ((4194304 & firstToken) != 0) {
            int indexes = src.readInt();
            int wakeLockIndex = indexes & 65535;
            int wakeReasonIndex = (indexes >> 16) & 65535;
            if (wakeLockIndex != 65535) {
                cur.wakelockTag = cur.localWakelockTag;
                readHistoryTag(wakeLockIndex, cur.wakelockTag);
            } else {
                cur.wakelockTag = null;
            }
            if (wakeReasonIndex != 65535) {
                cur.wakeReasonTag = cur.localWakeReasonTag;
                readHistoryTag(wakeReasonIndex, cur.wakeReasonTag);
            } else {
                cur.wakeReasonTag = null;
            }
            cur.numReadInts++;
        } else {
            cur.wakelockTag = null;
            cur.wakeReasonTag = null;
        }
        if ((8388608 & firstToken) != 0) {
            cur.eventTag = cur.localEventTag;
            int codeAndIndex = src.readInt();
            cur.eventCode = 65535 & codeAndIndex;
            readHistoryTag((codeAndIndex >> 16) & 65535, cur.eventTag);
            cur.numReadInts++;
        } else {
            cur.eventCode = 0;
        }
        if ((batteryLevelInt & 1) != 0) {
            cur.stepDetails = this.mReadHistoryStepDetails;
            cur.stepDetails.readFromParcel(src);
            return;
        }
        cur.stepDetails = null;
    }

    public void commitCurrentHistoryBatchLocked() {
        this.mHistoryLastWritten.cmd = (byte) -1;
    }

    void addHistoryBufferLocked(long elapsedRealtimeMs, long uptimeMs, HistoryItem cur) {
        if (this.mHaveBatteryLevel && this.mRecordingHistory) {
            long timeDiff = (this.mHistoryBaseTime + elapsedRealtimeMs) - this.mHistoryLastWritten.time;
            int diffStates = this.mHistoryLastWritten.states ^ (cur.states & this.mActiveHistoryStates);
            int diffStates2 = this.mHistoryLastWritten.states2 ^ (cur.states2 & this.mActiveHistoryStates2);
            int lastDiffStates = this.mHistoryLastWritten.states ^ this.mHistoryLastLastWritten.states;
            int lastDiffStates2 = this.mHistoryLastWritten.states2 ^ this.mHistoryLastLastWritten.states2;
            if (this.mHistoryBufferLastPos >= 0 && this.mHistoryLastWritten.cmd == (byte) 0 && timeDiff < 1000 && (diffStates & lastDiffStates) == 0 && (diffStates2 & lastDiffStates2) == 0 && ((this.mHistoryLastWritten.wakelockTag == null || cur.wakelockTag == null) && ((this.mHistoryLastWritten.wakeReasonTag == null || cur.wakeReasonTag == null) && this.mHistoryLastWritten.stepDetails == null && ((this.mHistoryLastWritten.eventCode == 0 || cur.eventCode == 0) && this.mHistoryLastWritten.batteryLevel == cur.batteryLevel && this.mHistoryLastWritten.batteryStatus == cur.batteryStatus && this.mHistoryLastWritten.batteryHealth == cur.batteryHealth && this.mHistoryLastWritten.batteryPlugType == cur.batteryPlugType && this.mHistoryLastWritten.batteryTemperature == cur.batteryTemperature && this.mHistoryLastWritten.batteryVoltage == cur.batteryVoltage)))) {
                this.mHistoryBuffer.setDataSize(this.mHistoryBufferLastPos);
                this.mHistoryBuffer.setDataPosition(this.mHistoryBufferLastPos);
                this.mHistoryBufferLastPos = -1;
                elapsedRealtimeMs = this.mHistoryLastWritten.time - this.mHistoryBaseTime;
                if (this.mHistoryLastWritten.wakelockTag != null) {
                    cur.wakelockTag = cur.localWakelockTag;
                    cur.wakelockTag.setTo(this.mHistoryLastWritten.wakelockTag);
                }
                if (this.mHistoryLastWritten.wakeReasonTag != null) {
                    cur.wakeReasonTag = cur.localWakeReasonTag;
                    cur.wakeReasonTag.setTo(this.mHistoryLastWritten.wakeReasonTag);
                }
                if (this.mHistoryLastWritten.eventCode != 0) {
                    cur.eventCode = this.mHistoryLastWritten.eventCode;
                    cur.eventTag = cur.localEventTag;
                    cur.eventTag.setTo(this.mHistoryLastWritten.eventTag);
                }
                this.mHistoryLastWritten.setTo(this.mHistoryLastLastWritten);
            }
            int dataSize = this.mHistoryBuffer.dataSize();
            if (dataSize < 262144) {
                if (dataSize == 0) {
                    cur.currentTime = System.currentTimeMillis();
                    addHistoryBufferLocked(elapsedRealtimeMs, uptimeMs, (byte) 7, cur);
                }
                addHistoryBufferLocked(elapsedRealtimeMs, uptimeMs, (byte) 0, cur);
            } else if (this.mHistoryOverflow) {
                int old;
                boolean writeAnyway = false;
                int curStates = (cur.states & -1638400) & this.mActiveHistoryStates;
                if (this.mHistoryLastWritten.states != curStates) {
                    old = this.mActiveHistoryStates;
                    this.mActiveHistoryStates &= 1638399 | curStates;
                    writeAnyway = false | (old != this.mActiveHistoryStates ? 1 : 0);
                }
                int curStates2 = (cur.states2 & 1753153536) & this.mActiveHistoryStates2;
                if (this.mHistoryLastWritten.states2 != curStates2) {
                    old = this.mActiveHistoryStates2;
                    this.mActiveHistoryStates2 &= -1753153537 | curStates2;
                    writeAnyway |= old != this.mActiveHistoryStates2 ? 1 : 0;
                }
                if (writeAnyway || this.mHistoryLastWritten.batteryLevel != cur.batteryLevel || (dataSize < 327680 && ((this.mHistoryLastWritten.states ^ cur.states) & 1572864) != 0 && ((this.mHistoryLastWritten.states2 ^ cur.states2) & -1753219072) != 0)) {
                    addHistoryBufferLocked(elapsedRealtimeMs, uptimeMs, (byte) 0, cur);
                }
            } else {
                this.mHistoryOverflow = true;
                addHistoryBufferLocked(elapsedRealtimeMs, uptimeMs, (byte) 0, cur);
                addHistoryBufferLocked(elapsedRealtimeMs, uptimeMs, (byte) 6, cur);
            }
        }
    }

    private void addHistoryBufferLocked(long elapsedRealtimeMs, long uptimeMs, byte cmd, HistoryItem cur) {
        if (this.mIteratingHistory) {
            throw new IllegalStateException("Can't do this while iterating history!");
        }
        this.mHistoryBufferLastPos = this.mHistoryBuffer.dataPosition();
        this.mHistoryLastLastWritten.setTo(this.mHistoryLastWritten);
        this.mHistoryLastWritten.setTo(this.mHistoryBaseTime + elapsedRealtimeMs, cmd, cur);
        HistoryItem historyItem = this.mHistoryLastWritten;
        historyItem.states &= this.mActiveHistoryStates;
        historyItem = this.mHistoryLastWritten;
        historyItem.states2 &= this.mActiveHistoryStates2;
        writeHistoryDelta(this.mHistoryBuffer, this.mHistoryLastWritten, this.mHistoryLastLastWritten);
        this.mLastHistoryElapsedRealtime = elapsedRealtimeMs;
        cur.wakelockTag = null;
        cur.wakeReasonTag = null;
        cur.eventCode = 0;
        cur.eventTag = null;
    }

    void addHistoryRecordLocked(long elapsedRealtimeMs, long uptimeMs) {
        HistoryItem historyItem;
        if (this.mTrackRunningHistoryElapsedRealtime != 0) {
            long diffElapsed = elapsedRealtimeMs - this.mTrackRunningHistoryElapsedRealtime;
            long diffUptime = uptimeMs - this.mTrackRunningHistoryUptime;
            if (diffUptime < diffElapsed - 20) {
                long wakeElapsedTime = elapsedRealtimeMs - (diffElapsed - diffUptime);
                this.mHistoryAddTmp.setTo(this.mHistoryLastWritten);
                this.mHistoryAddTmp.wakelockTag = null;
                this.mHistoryAddTmp.wakeReasonTag = null;
                this.mHistoryAddTmp.eventCode = 0;
                historyItem = this.mHistoryAddTmp;
                historyItem.states &= Integer.MAX_VALUE;
                addHistoryRecordInnerLocked(wakeElapsedTime, uptimeMs, this.mHistoryAddTmp);
            }
        }
        historyItem = this.mHistoryCur;
        historyItem.states |= Integer.MIN_VALUE;
        this.mTrackRunningHistoryElapsedRealtime = elapsedRealtimeMs;
        this.mTrackRunningHistoryUptime = uptimeMs;
        addHistoryRecordInnerLocked(elapsedRealtimeMs, uptimeMs, this.mHistoryCur);
    }

    void addHistoryRecordInnerLocked(long elapsedRealtimeMs, long uptimeMs, HistoryItem cur) {
        addHistoryBufferLocked(elapsedRealtimeMs, uptimeMs, cur);
    }

    public void addHistoryEventLocked(long elapsedRealtimeMs, long uptimeMs, int code, String name, int uid) {
        this.mHistoryCur.eventCode = code;
        this.mHistoryCur.eventTag = this.mHistoryCur.localEventTag;
        this.mHistoryCur.eventTag.string = name;
        this.mHistoryCur.eventTag.uid = uid;
        addHistoryRecordLocked(elapsedRealtimeMs, uptimeMs);
    }

    void addHistoryRecordLocked(long elapsedRealtimeMs, long uptimeMs, byte cmd, HistoryItem cur) {
        HistoryItem rec = this.mHistoryCache;
        if (rec != null) {
            this.mHistoryCache = rec.next;
        } else {
            rec = new HistoryItem();
        }
        rec.setTo(this.mHistoryBaseTime + elapsedRealtimeMs, cmd, cur);
        addHistoryRecordLocked(rec);
    }

    void addHistoryRecordLocked(HistoryItem rec) {
        this.mNumHistoryItems++;
        rec.next = null;
        this.mHistoryLastEnd = this.mHistoryEnd;
        if (this.mHistoryEnd != null) {
            this.mHistoryEnd.next = rec;
            this.mHistoryEnd = rec;
            return;
        }
        this.mHistoryEnd = rec;
        this.mHistory = rec;
    }

    void clearHistoryLocked() {
        this.mHistoryBaseTime = 0;
        this.mLastHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryUptime = 0;
        this.mHistoryBuffer.setDataSize(0);
        this.mHistoryBuffer.setDataPosition(0);
        this.mHistoryBuffer.setDataCapacity(131072);
        this.mHistoryLastLastWritten.clear();
        this.mHistoryLastWritten.clear();
        this.mHistoryTagPool.clear();
        this.mNextHistoryTagIdx = 0;
        this.mNumHistoryTagChars = 0;
        this.mHistoryBufferLastPos = -1;
        this.mHistoryOverflow = false;
        this.mActiveHistoryStates = -1;
        this.mActiveHistoryStates2 = -1;
    }

    public void updateTimeBasesLocked(boolean unplugged, boolean screenOff, long uptime, long realtime) {
        this.mOnBatteryTimeBase.setRunning(unplugged, uptime, realtime);
        boolean unpluggedScreenOff = unplugged && screenOff;
        if (unpluggedScreenOff != this.mOnBatteryScreenOffTimeBase.isRunning()) {
            updateKernelWakelocksLocked();
            updateCpuTimeLocked();
            this.mOnBatteryScreenOffTimeBase.setRunning(unpluggedScreenOff, uptime, realtime);
        }
    }

    public void addIsolatedUidLocked(int isolatedUid, int appUid) {
        this.mIsolatedUids.put(isolatedUid, appUid);
    }

    public void scheduleRemoveIsolatedUidLocked(int isolatedUid, int appUid) {
        if (this.mIsolatedUids.get(isolatedUid, -1) == appUid && this.mExternalSync != null) {
            this.mExternalSync.scheduleCpuSyncDueToRemovedUid(isolatedUid);
        }
    }

    public void removeIsolatedUidLocked(int isolatedUid) {
        this.mIsolatedUids.delete(isolatedUid);
        this.mKernelUidCpuTimeReader.removeUid(isolatedUid);
    }

    public int mapUid(int uid) {
        int isolated = this.mIsolatedUids.get(uid, -1);
        return isolated > 0 ? isolated : uid;
    }

    public void noteEventLocked(int code, String name, int uid) {
        uid = mapUid(uid);
        if (this.mActiveEvents.updateState(code, name, uid, 0)) {
            addHistoryEventLocked(SystemClock.elapsedRealtime(), SystemClock.uptimeMillis(), code, name, uid);
        }
    }

    boolean ensureStartClockTime(long currentTime) {
        if (currentTime <= 31536000000L || this.mStartClockTime >= currentTime - 31536000000L) {
            return false;
        }
        this.mStartClockTime = currentTime - (SystemClock.elapsedRealtime() - (this.mRealtimeStart / 1000));
        return true;
    }

    public void noteCurrentTimeChangedLocked() {
        long currentTime = System.currentTimeMillis();
        recordCurrentTimeChangeLocked(currentTime, SystemClock.elapsedRealtime(), SystemClock.uptimeMillis());
        ensureStartClockTime(currentTime);
    }

    public void noteProcessStartLocked(String name, int uid) {
        uid = mapUid(uid);
        if (isOnBattery()) {
            getUidStatsLocked(uid).getProcessStatsLocked(name).incStartsLocked();
        }
        if (this.mActiveEvents.updateState(32769, name, uid, 0) && this.mRecordAllHistory) {
            addHistoryEventLocked(SystemClock.elapsedRealtime(), SystemClock.uptimeMillis(), 32769, name, uid);
        }
    }

    public void noteProcessCrashLocked(String name, int uid) {
        uid = mapUid(uid);
        if (isOnBattery()) {
            getUidStatsLocked(uid).getProcessStatsLocked(name).incNumCrashesLocked();
        }
    }

    public void noteProcessAnrLocked(String name, int uid) {
        uid = mapUid(uid);
        if (isOnBattery()) {
            getUidStatsLocked(uid).getProcessStatsLocked(name).incNumAnrsLocked();
        }
    }

    public void noteProcessStateLocked(String name, int uid, int state) {
        uid = mapUid(uid);
        getUidStatsLocked(uid).updateProcessStateLocked(name, state, SystemClock.elapsedRealtime());
    }

    public void noteProcessFinishLocked(String name, int uid) {
        uid = mapUid(uid);
        if (this.mActiveEvents.updateState(16385, name, uid, 0)) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            getUidStatsLocked(uid).updateProcessStateLocked(name, 3, elapsedRealtime);
            if (this.mRecordAllHistory) {
                addHistoryEventLocked(elapsedRealtime, uptime, 16385, name, uid);
            }
        }
    }

    public void noteSyncStartLocked(String name, int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        getUidStatsLocked(uid).noteStartSyncLocked(name, elapsedRealtime);
        if (this.mActiveEvents.updateState(32772, name, uid, 0)) {
            addHistoryEventLocked(elapsedRealtime, uptime, 32772, name, uid);
        }
    }

    public void noteSyncFinishLocked(String name, int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        getUidStatsLocked(uid).noteStopSyncLocked(name, elapsedRealtime);
        if (this.mActiveEvents.updateState(16388, name, uid, 0)) {
            addHistoryEventLocked(elapsedRealtime, uptime, 16388, name, uid);
        }
    }

    public void noteJobStartLocked(String name, int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        getUidStatsLocked(uid).noteStartJobLocked(name, elapsedRealtime);
        if (this.mActiveEvents.updateState(32774, name, uid, 0)) {
            addHistoryEventLocked(elapsedRealtime, uptime, 32774, name, uid);
        }
    }

    public void noteJobFinishLocked(String name, int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        getUidStatsLocked(uid).noteStopJobLocked(name, elapsedRealtime);
        if (this.mActiveEvents.updateState(16390, name, uid, 0)) {
            addHistoryEventLocked(elapsedRealtime, uptime, 16390, name, uid);
        }
    }

    public void noteAlarmStartLocked(String name, int uid) {
        if (this.mRecordAllHistory) {
            uid = mapUid(uid);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            if (this.mActiveEvents.updateState(32781, name, uid, 0)) {
                addHistoryEventLocked(elapsedRealtime, uptime, 32781, name, uid);
            }
        }
    }

    public void noteAlarmFinishLocked(String name, int uid) {
        if (this.mRecordAllHistory) {
            uid = mapUid(uid);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            if (this.mActiveEvents.updateState(16397, name, uid, 0)) {
                addHistoryEventLocked(elapsedRealtime, uptime, 16397, name, uid);
            }
        }
    }

    private void requestWakelockCpuUpdate() {
        if (!this.mHandler.hasMessages(1)) {
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1), 5000);
        }
    }

    private void requestImmediateCpuUpdate() {
        this.mHandler.removeMessages(1);
        this.mHandler.sendEmptyMessage(1);
    }

    public void setRecordAllHistoryLocked(boolean enabled) {
        this.mRecordAllHistory = enabled;
        HashMap<String, SparseIntArray> active;
        long mSecRealtime;
        long mSecUptime;
        SparseIntArray uids;
        int j;
        if (enabled) {
            active = this.mActiveEvents.getStateForEvent(1);
            if (active != null) {
                mSecRealtime = SystemClock.elapsedRealtime();
                mSecUptime = SystemClock.uptimeMillis();
                for (Map.Entry<String, SparseIntArray> ent : active.entrySet()) {
                    uids = (SparseIntArray) ent.getValue();
                    for (j = 0; j < uids.size(); j++) {
                        addHistoryEventLocked(mSecRealtime, mSecUptime, 32769, (String) ent.getKey(), uids.keyAt(j));
                    }
                }
                return;
            }
            return;
        }
        this.mActiveEvents.removeEvents(5);
        this.mActiveEvents.removeEvents(13);
        active = this.mActiveEvents.getStateForEvent(1);
        if (active != null) {
            mSecRealtime = SystemClock.elapsedRealtime();
            mSecUptime = SystemClock.uptimeMillis();
            for (Map.Entry<String, SparseIntArray> ent2 : active.entrySet()) {
                uids = (SparseIntArray) ent2.getValue();
                for (j = 0; j < uids.size(); j++) {
                    addHistoryEventLocked(mSecRealtime, mSecUptime, 16385, (String) ent2.getKey(), uids.keyAt(j));
                }
            }
        }
    }

    public void setNoAutoReset(boolean enabled) {
        Slog.d(TAG, "setNoAutoReset is called. enabled = " + enabled);
        this.mNoAutoReset = enabled;
    }

    public void noteStartWakeLocked(int uid, int pid, String name, String historyName, int type, boolean unimportantForLogging, long elapsedRealtime, long uptime) {
        uid = mapUid(uid);
        if (type == 0) {
            aggregateLastWakeupUptimeLocked(uptime);
            if (historyName == null) {
                historyName = name;
            }
            if (this.mRecordAllHistory && this.mActiveEvents.updateState(32773, historyName, uid, 0)) {
                addHistoryEventLocked(elapsedRealtime, uptime, 32773, historyName, uid);
            }
            HistoryTag historyTag;
            if (this.mWakeLockNesting == 0) {
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states |= 1073741824;
                this.mHistoryCur.wakelockTag = this.mHistoryCur.localWakelockTag;
                historyTag = this.mHistoryCur.wakelockTag;
                this.mInitialAcquireWakeName = historyName;
                historyTag.string = historyName;
                historyTag = this.mHistoryCur.wakelockTag;
                this.mInitialAcquireWakeUid = uid;
                historyTag.uid = uid;
                this.mWakeLockImportant = !unimportantForLogging;
                addHistoryRecordLocked(elapsedRealtime, uptime);
            } else if (!(this.mWakeLockImportant || unimportantForLogging || this.mHistoryLastWritten.cmd != (byte) 0)) {
                if (this.mHistoryLastWritten.wakelockTag != null) {
                    this.mHistoryLastWritten.wakelockTag = null;
                    this.mHistoryCur.wakelockTag = this.mHistoryCur.localWakelockTag;
                    historyTag = this.mHistoryCur.wakelockTag;
                    this.mInitialAcquireWakeName = historyName;
                    historyTag.string = historyName;
                    historyTag = this.mHistoryCur.wakelockTag;
                    this.mInitialAcquireWakeUid = uid;
                    historyTag.uid = uid;
                    addHistoryRecordLocked(elapsedRealtime, uptime);
                }
                this.mWakeLockImportant = true;
            }
            this.mWakeLockNesting++;
        }
        if (uid >= 0) {
            if (this.mOnBatteryScreenOffTimeBase.isRunning()) {
                requestWakelockCpuUpdate();
            }
            getUidStatsLocked(uid).noteStartWakeLocked(pid, name, type, elapsedRealtime);
        }
    }

    public void noteStopWakeLocked(int uid, int pid, String name, String historyName, int type, long elapsedRealtime, long uptime) {
        uid = mapUid(uid);
        if (type == 0) {
            this.mWakeLockNesting--;
            if (this.mRecordAllHistory) {
                if (historyName == null) {
                    historyName = name;
                }
                if (this.mActiveEvents.updateState(16389, historyName, uid, 0)) {
                    addHistoryEventLocked(elapsedRealtime, uptime, 16389, historyName, uid);
                }
            }
            if (this.mWakeLockNesting == 0) {
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states &= -1073741825;
                this.mInitialAcquireWakeName = null;
                this.mInitialAcquireWakeUid = -1;
                addHistoryRecordLocked(elapsedRealtime, uptime);
            }
        }
        if (uid >= 0) {
            if (this.mOnBatteryScreenOffTimeBase.isRunning()) {
                requestWakelockCpuUpdate();
            }
            getUidStatsLocked(uid).noteStopWakeLocked(pid, name, type, elapsedRealtime);
        }
    }

    public void noteStartWakeFromSourceLocked(WorkSource ws, int pid, String name, String historyName, int type, boolean unimportantForLogging) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteStartWakeLocked(ws.get(i), pid, name, historyName, type, unimportantForLogging, elapsedRealtime, uptime);
        }
    }

    public void noteChangeWakelockFromSourceLocked(WorkSource ws, int pid, String name, String historyName, int type, WorkSource newWs, int newPid, String newName, String newHistoryName, int newType, boolean newUnimportantForLogging) {
        int i;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        int NN = newWs.size();
        for (i = 0; i < NN; i++) {
            noteStartWakeLocked(newWs.get(i), newPid, newName, newHistoryName, newType, newUnimportantForLogging, elapsedRealtime, uptime);
        }
        int NO = ws.size();
        for (i = 0; i < NO; i++) {
            noteStopWakeLocked(ws.get(i), pid, name, historyName, type, elapsedRealtime, uptime);
        }
    }

    public void noteStopWakeFromSourceLocked(WorkSource ws, int pid, String name, String historyName, int type) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteStopWakeLocked(ws.get(i), pid, name, historyName, type, elapsedRealtime, uptime);
        }
    }

    void aggregateLastWakeupUptimeLocked(long uptimeMs) {
        if (this.mLastWakeupReason != null) {
            long deltaUptime = uptimeMs - this.mLastWakeupUptimeMs;
            SamplingTimer timer = getWakeupReasonTimerLocked(this.mLastWakeupReason);
            timer.addCurrentReportedCount(1);
            timer.addCurrentReportedTotalTime(1000 * deltaUptime);
            this.mLastWakeupReason = null;
        }
    }

    public void noteWakeupReasonLocked(String reason) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        aggregateLastWakeupUptimeLocked(uptime);
        this.mHistoryCur.wakeReasonTag = this.mHistoryCur.localWakeReasonTag;
        this.mHistoryCur.wakeReasonTag.string = reason;
        this.mHistoryCur.wakeReasonTag.uid = 0;
        this.mLastWakeupReason = reason;
        this.mLastWakeupUptimeMs = uptime;
        addHistoryRecordLocked(elapsedRealtime, uptime);
    }

    public boolean startAddingCpuLocked() {
        this.mHandler.removeMessages(1);
        return this.mOnBatteryInternal;
    }

    public void finishAddingCpuLocked(int totalUTime, int totalSTime, int statUserTime, int statSystemTime, int statIOWaitTime, int statIrqTime, int statSoftIrqTime, int statIdleTime) {
        this.mCurStepCpuUserTime += (long) totalUTime;
        this.mCurStepCpuSystemTime += (long) totalSTime;
        this.mCurStepStatUserTime += (long) statUserTime;
        this.mCurStepStatSystemTime += (long) statSystemTime;
        this.mCurStepStatIOWaitTime += (long) statIOWaitTime;
        this.mCurStepStatIrqTime += (long) statIrqTime;
        this.mCurStepStatSoftIrqTime += (long) statSoftIrqTime;
        this.mCurStepStatIdleTime += (long) statIdleTime;
    }

    public void noteProcessDiedLocked(int uid, int pid) {
        Uid u = (Uid) this.mUidStats.get(mapUid(uid));
        if (u != null) {
            u.mPids.remove(pid);
        }
    }

    public long getProcessWakeTime(int uid, int pid, long realtime) {
        long j = 0;
        Uid u = (Uid) this.mUidStats.get(mapUid(uid));
        if (u == null) {
            return 0;
        }
        Pid p = (Pid) u.mPids.get(pid);
        if (p == null) {
            return 0;
        }
        long j2 = p.mWakeSumMs;
        if (p.mWakeNesting > 0) {
            j = realtime - p.mWakeStartMs;
        }
        return j + j2;
    }

    public void reportExcessiveWakeLocked(int uid, String proc, long overTime, long usedTime) {
        Uid u = (Uid) this.mUidStats.get(mapUid(uid));
        if (u != null) {
            u.reportExcessiveWakeLocked(proc, overTime, usedTime);
        }
    }

    public void reportExcessiveCpuLocked(int uid, String proc, long overTime, long usedTime) {
        Uid u = (Uid) this.mUidStats.get(mapUid(uid));
        if (u != null) {
            u.reportExcessiveCpuLocked(proc, overTime, usedTime);
        }
    }

    public void noteStartSensorLocked(int uid, int sensor) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        if (this.mSensorNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states |= 8388608;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        this.mSensorNesting++;
        getUidStatsLocked(uid).noteStartSensor(sensor, elapsedRealtime);
    }

    public void noteStopSensorLocked(int uid, int sensor) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        this.mSensorNesting--;
        if (this.mSensorNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states &= -8388609;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        getUidStatsLocked(uid).noteStopSensor(sensor, elapsedRealtime);
    }

    public void noteStartGpsLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        if (this.mGpsNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states |= 536870912;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        this.mGpsNesting++;
        getUidStatsLocked(uid).noteStartGps(elapsedRealtime);
    }

    public void noteStopGpsLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        this.mGpsNesting--;
        if (this.mGpsNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states &= -536870913;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        getUidStatsLocked(uid).noteStopGps(elapsedRealtime);
    }

    public void noteScreenStateLocked(int state) {
        if (this.mScreenState != state) {
            recordDailyStatsIfNeededLocked(true);
            int oldState = this.mScreenState;
            this.mScreenState = state;
            if (state != 0) {
                int stepState = state - 1;
                if (stepState < 4) {
                    this.mModStepMode |= (this.mCurStepMode & 3) ^ stepState;
                    this.mCurStepMode = (this.mCurStepMode & -4) | stepState;
                } else {
                    Slog.wtf(TAG, "Unexpected screen state: " + state);
                }
            }
            long elapsedRealtime;
            long uptime;
            HistoryItem historyItem;
            if (state == 2) {
                elapsedRealtime = SystemClock.elapsedRealtime();
                uptime = SystemClock.uptimeMillis();
                historyItem = this.mHistoryCur;
                historyItem.states |= 1048576;
                addHistoryRecordLocked(elapsedRealtime, uptime);
                this.mScreenOnTimer.startRunningLocked(elapsedRealtime);
                if (this.mScreenBrightnessBin >= 0) {
                    this.mScreenBrightnessTimer[this.mScreenBrightnessBin].startRunningLocked(elapsedRealtime);
                }
                updateTimeBasesLocked(this.mOnBatteryTimeBase.isRunning(), false, SystemClock.uptimeMillis() * 1000, 1000 * elapsedRealtime);
                noteStartWakeLocked(-1, -1, "screen", null, 0, false, elapsedRealtime, uptime);
                if (this.mOnBatteryInternal) {
                    updateDischargeScreenLevelsLocked(false, true);
                }
            } else if (oldState == 2) {
                elapsedRealtime = SystemClock.elapsedRealtime();
                uptime = SystemClock.uptimeMillis();
                historyItem = this.mHistoryCur;
                historyItem.states &= -1048577;
                addHistoryRecordLocked(elapsedRealtime, uptime);
                this.mScreenOnTimer.stopRunningLocked(elapsedRealtime);
                if (this.mScreenBrightnessBin >= 0) {
                    this.mScreenBrightnessTimer[this.mScreenBrightnessBin].stopRunningLocked(elapsedRealtime);
                }
                noteStopWakeLocked(-1, -1, "screen", "screen", 0, elapsedRealtime, uptime);
                updateTimeBasesLocked(this.mOnBatteryTimeBase.isRunning(), true, SystemClock.uptimeMillis() * 1000, 1000 * elapsedRealtime);
                if (this.mOnBatteryInternal) {
                    updateDischargeScreenLevelsLocked(true, false);
                }
            }
        }
    }

    public void noteScreenBrightnessLocked(int brightness) {
        int bin = brightness / 51;
        if (bin < 0) {
            bin = 0;
        } else if (bin >= 5) {
            bin = 4;
        }
        if (this.mScreenBrightnessBin != bin) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            this.mHistoryCur.states = (this.mHistoryCur.states & -8) | (bin << 0);
            addHistoryRecordLocked(elapsedRealtime, uptime);
            if (this.mScreenState == 2) {
                if (this.mScreenBrightnessBin >= 0) {
                    this.mScreenBrightnessTimer[this.mScreenBrightnessBin].stopRunningLocked(elapsedRealtime);
                }
                this.mScreenBrightnessTimer[bin].startRunningLocked(elapsedRealtime);
            }
            this.mScreenBrightnessBin = bin;
        }
    }

    public void noteUserActivityLocked(int uid, int event) {
        if (this.mOnBatteryInternal) {
            getUidStatsLocked(mapUid(uid)).noteUserActivityLocked(event);
        }
    }

    public void noteWakeUpLocked(String reason, int reasonUid) {
        addHistoryEventLocked(SystemClock.elapsedRealtime(), SystemClock.uptimeMillis(), 18, reason, reasonUid);
    }

    public void noteInteractiveLocked(boolean interactive) {
        if (this.mInteractive != interactive) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            this.mInteractive = interactive;
            if (interactive) {
                this.mInteractiveTimer.startRunningLocked(elapsedRealtime);
            } else {
                this.mInteractiveTimer.stopRunningLocked(elapsedRealtime);
            }
        }
    }

    public void noteConnectivityChangedLocked(int type, String extra) {
        addHistoryEventLocked(SystemClock.elapsedRealtime(), SystemClock.uptimeMillis(), 9, extra, type);
        this.mNumConnectivityChange++;
    }

    public void noteMobileRadioPowerState(int powerState, long timestampNs) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        if (this.mMobileRadioPowerState != powerState) {
            long realElapsedRealtimeMs;
            boolean active = powerState == DataConnectionRealTimeInfo.DC_POWER_STATE_MEDIUM || powerState == DataConnectionRealTimeInfo.DC_POWER_STATE_HIGH;
            HistoryItem historyItem;
            if (active) {
                realElapsedRealtimeMs = elapsedRealtime;
                this.mMobileRadioActiveStartTime = elapsedRealtime;
                historyItem = this.mHistoryCur;
                historyItem.states |= 33554432;
            } else {
                realElapsedRealtimeMs = timestampNs / TimeUtils.NANOS_PER_MS;
                long lastUpdateTimeMs = this.mMobileRadioActiveStartTime;
                if (realElapsedRealtimeMs < lastUpdateTimeMs) {
                    Slog.wtf(TAG, "Data connection inactive timestamp " + realElapsedRealtimeMs + " is before start time " + lastUpdateTimeMs);
                    realElapsedRealtimeMs = elapsedRealtime;
                } else if (realElapsedRealtimeMs < elapsedRealtime) {
                    this.mMobileRadioActiveAdjustedTime.addCountLocked(elapsedRealtime - realElapsedRealtimeMs);
                }
                historyItem = this.mHistoryCur;
                historyItem.states &= -33554433;
            }
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mMobileRadioPowerState = powerState;
            if (active) {
                this.mMobileRadioActiveTimer.startRunningLocked(elapsedRealtime);
                this.mMobileRadioActivePerAppTimer.startRunningLocked(elapsedRealtime);
                return;
            }
            this.mMobileRadioActiveTimer.stopRunningLocked(realElapsedRealtimeMs);
            updateMobileRadioStateLocked(realElapsedRealtimeMs);
            this.mMobileRadioActivePerAppTimer.stopRunningLocked(realElapsedRealtimeMs);
        }
    }

    public void notePowerSaveMode(boolean enabled) {
        if (this.mPowerSaveModeEnabled != enabled) {
            int stepState = enabled ? 4 : 0;
            this.mModStepMode |= (this.mCurStepMode & 4) ^ stepState;
            this.mCurStepMode = (this.mCurStepMode & -5) | stepState;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            this.mPowerSaveModeEnabled = enabled;
            HistoryItem historyItem;
            if (enabled) {
                historyItem = this.mHistoryCur;
                historyItem.states2 |= Integer.MIN_VALUE;
                this.mPowerSaveModeEnabledTimer.startRunningLocked(elapsedRealtime);
            } else {
                historyItem = this.mHistoryCur;
                historyItem.states2 &= Integer.MAX_VALUE;
                this.mPowerSaveModeEnabledTimer.stopRunningLocked(elapsedRealtime);
            }
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
    }

    public void noteDeviceIdleModeLocked(boolean enabled, String activeReason, int activeUid) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        boolean nowIdling = enabled;
        if (this.mDeviceIdling && !enabled && activeReason == null) {
            nowIdling = true;
        }
        if (this.mDeviceIdling != nowIdling) {
            this.mDeviceIdling = nowIdling;
            int stepState = nowIdling ? 8 : 0;
            this.mModStepMode |= (this.mCurStepMode & 8) ^ stepState;
            this.mCurStepMode = (this.mCurStepMode & -9) | stepState;
            if (enabled) {
                this.mDeviceIdlingTimer.startRunningLocked(elapsedRealtime);
            } else {
                this.mDeviceIdlingTimer.stopRunningLocked(elapsedRealtime);
            }
        }
        if (this.mDeviceIdleModeEnabled != enabled) {
            this.mDeviceIdleModeEnabled = enabled;
            addHistoryEventLocked(elapsedRealtime, uptime, 10, activeReason != null ? activeReason : "", activeUid);
            HistoryItem historyItem;
            if (enabled) {
                historyItem = this.mHistoryCur;
                historyItem.states2 |= 67108864;
                this.mDeviceIdleModeEnabledTimer.startRunningLocked(elapsedRealtime);
            } else {
                historyItem = this.mHistoryCur;
                historyItem.states2 &= -67108865;
                this.mDeviceIdleModeEnabledTimer.stopRunningLocked(elapsedRealtime);
            }
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
    }

    public void notePackageInstalledLocked(String pkgName, int versionCode) {
        addHistoryEventLocked(SystemClock.elapsedRealtime(), SystemClock.uptimeMillis(), 11, pkgName, versionCode);
        PackageChange pc = new PackageChange();
        pc.mPackageName = pkgName;
        pc.mUpdate = true;
        pc.mVersionCode = versionCode;
        addPackageChange(pc);
    }

    public void notePackageUninstalledLocked(String pkgName) {
        addHistoryEventLocked(SystemClock.elapsedRealtime(), SystemClock.uptimeMillis(), 12, pkgName, 0);
        PackageChange pc = new PackageChange();
        pc.mPackageName = pkgName;
        pc.mUpdate = true;
        addPackageChange(pc);
    }

    private void addPackageChange(PackageChange pc) {
        if (this.mDailyPackageChanges == null) {
            this.mDailyPackageChanges = new ArrayList();
        }
        this.mDailyPackageChanges.add(pc);
    }

    public void notePhoneOnLocked() {
        if (!this.mPhoneOn) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 |= 16777216;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mPhoneOn = true;
            this.mPhoneOnTimer.startRunningLocked(elapsedRealtime);
        }
    }

    public void notePhoneOffLocked() {
        if (this.mPhoneOn) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -16777217;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mPhoneOn = false;
            this.mPhoneOnTimer.stopRunningLocked(elapsedRealtime);
        }
    }

    void stopAllPhoneSignalStrengthTimersLocked(int except) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        for (int i = 0; i < SignalStrength.NUM_SIGNAL_STRENGTH_BINS; i++) {
            if (i != except) {
                while (this.mPhoneSignalStrengthsTimer[i].isRunningLocked()) {
                    this.mPhoneSignalStrengthsTimer[i].stopRunningLocked(elapsedRealtime);
                }
            }
        }
    }

    private int fixPhoneServiceState(int state, int signalBin) {
        if (this.mPhoneSimStateRaw == 1 && state == 1 && signalBin > 0) {
            return 0;
        }
        return state;
    }

    private void updateAllPhoneStateLocked(int state, int simState, int strengthBin) {
        boolean scanning = false;
        boolean newHistory = false;
        this.mPhoneServiceStateRaw = state;
        this.mPhoneSimStateRaw = simState;
        this.mPhoneSignalStrengthBinRaw = strengthBin;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        if (simState == 1 && state == 1 && strengthBin > 0) {
            state = 0;
        }
        if (state == 3) {
            strengthBin = -1;
        } else if (state != 0 && state == 1) {
            scanning = true;
            strengthBin = 0;
            if (!this.mPhoneSignalScanningTimer.isRunningLocked()) {
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states |= 2097152;
                newHistory = true;
                this.mPhoneSignalScanningTimer.startRunningLocked(elapsedRealtime);
            }
        }
        if (!scanning && this.mPhoneSignalScanningTimer.isRunningLocked()) {
            historyItem = this.mHistoryCur;
            historyItem.states &= -2097153;
            newHistory = true;
            this.mPhoneSignalScanningTimer.stopRunningLocked(elapsedRealtime);
        }
        if (this.mPhoneServiceState != state) {
            this.mHistoryCur.states = (this.mHistoryCur.states & -449) | (state << 6);
            newHistory = true;
            this.mPhoneServiceState = state;
        }
        if (this.mPhoneSignalStrengthBin != strengthBin) {
            if (this.mPhoneSignalStrengthBin >= 0) {
                this.mPhoneSignalStrengthsTimer[this.mPhoneSignalStrengthBin].stopRunningLocked(elapsedRealtime);
            }
            if (strengthBin >= 0) {
                if (!this.mPhoneSignalStrengthsTimer[strengthBin].isRunningLocked()) {
                    this.mPhoneSignalStrengthsTimer[strengthBin].startRunningLocked(elapsedRealtime);
                }
                this.mHistoryCur.states = (this.mHistoryCur.states & -57) | (strengthBin << 3);
                newHistory = true;
            } else {
                stopAllPhoneSignalStrengthTimersLocked(-1);
            }
            this.mPhoneSignalStrengthBin = strengthBin;
        }
        if (newHistory) {
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
    }

    public void notePhoneStateLocked(int state, int simState) {
        updateAllPhoneStateLocked(state, simState, this.mPhoneSignalStrengthBinRaw);
    }

    public void notePhoneSignalStrengthLocked(SignalStrength signalStrength) {
        updateAllPhoneStateLocked(this.mPhoneServiceStateRaw, this.mPhoneSimStateRaw, signalStrength.getLevel());
    }

    public void notePhoneDataConnectionStateLocked(int dataType, boolean hasData) {
        int bin = 0;
        if (hasData) {
            switch (dataType) {
                case 1:
                    bin = 1;
                    break;
                case 2:
                    bin = 2;
                    break;
                case 3:
                    bin = 3;
                    break;
                case 4:
                    bin = 4;
                    break;
                case 5:
                    bin = 5;
                    break;
                case 6:
                    bin = 6;
                    break;
                case 7:
                    bin = 7;
                    break;
                case 8:
                    bin = 8;
                    break;
                case 9:
                    bin = 9;
                    break;
                case 10:
                    bin = 10;
                    break;
                case 11:
                    bin = 11;
                    break;
                case 12:
                    bin = 12;
                    break;
                case 13:
                    bin = 13;
                    break;
                case 14:
                    bin = 14;
                    break;
                case 15:
                    bin = 15;
                    break;
                default:
                    bin = 16;
                    break;
            }
        }
        if (this.mPhoneDataConnectionType != bin) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            this.mHistoryCur.states = (this.mHistoryCur.states & -15873) | (bin << 9);
            addHistoryRecordLocked(elapsedRealtime, uptime);
            if (this.mPhoneDataConnectionType >= 0) {
                this.mPhoneDataConnectionsTimer[this.mPhoneDataConnectionType].stopRunningLocked(elapsedRealtime);
            }
            this.mPhoneDataConnectionType = bin;
            this.mPhoneDataConnectionsTimer[bin].startRunningLocked(elapsedRealtime);
        }
    }

    public void noteWifiOnLocked() {
        if (!this.mWifiOn) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 |= 268435456;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mWifiOn = true;
            this.mWifiOnTimer.startRunningLocked(elapsedRealtime);
            scheduleSyncExternalWifiStatsLocked("wifi-off");
        }
    }

    public void noteWifiOffLocked() {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        if (this.mWifiOn) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -268435457;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mWifiOn = false;
            this.mWifiOnTimer.stopRunningLocked(elapsedRealtime);
            scheduleSyncExternalWifiStatsLocked("wifi-on");
        }
    }

    public void noteAudioOnLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        if (this.mAudioOnNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states |= 4194304;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mAudioOnTimer.startRunningLocked(elapsedRealtime);
        }
        this.mAudioOnNesting++;
        getUidStatsLocked(uid).noteAudioTurnedOnLocked(elapsedRealtime);
    }

    public void noteAudioOffLocked(int uid) {
        if (this.mAudioOnNesting != 0) {
            uid = mapUid(uid);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            int i = this.mAudioOnNesting - 1;
            this.mAudioOnNesting = i;
            if (i == 0) {
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states &= -4194305;
                addHistoryRecordLocked(elapsedRealtime, uptime);
                this.mAudioOnTimer.stopRunningLocked(elapsedRealtime);
            }
            getUidStatsLocked(uid).noteAudioTurnedOffLocked(elapsedRealtime);
        }
    }

    public void noteVideoOnLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        if (this.mVideoOnNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 |= 1073741824;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mVideoOnTimer.startRunningLocked(elapsedRealtime);
        }
        this.mVideoOnNesting++;
        getUidStatsLocked(uid).noteVideoTurnedOnLocked(elapsedRealtime);
    }

    public void noteVideoOffLocked(int uid) {
        if (this.mVideoOnNesting != 0) {
            uid = mapUid(uid);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            int i = this.mVideoOnNesting - 1;
            this.mVideoOnNesting = i;
            if (i == 0) {
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states2 &= -1073741825;
                addHistoryRecordLocked(elapsedRealtime, uptime);
                this.mVideoOnTimer.stopRunningLocked(elapsedRealtime);
            }
            getUidStatsLocked(uid).noteVideoTurnedOffLocked(elapsedRealtime);
        }
    }

    public void noteResetAudioLocked() {
        if (this.mAudioOnNesting > 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            this.mAudioOnNesting = 0;
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states &= -4194305;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mAudioOnTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                ((Uid) this.mUidStats.valueAt(i)).noteResetAudioLocked(elapsedRealtime);
            }
        }
    }

    public void noteResetVideoLocked() {
        if (this.mVideoOnNesting > 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            this.mAudioOnNesting = 0;
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -1073741825;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mVideoOnTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                ((Uid) this.mUidStats.valueAt(i)).noteResetVideoLocked(elapsedRealtime);
            }
        }
    }

    public void noteActivityResumedLocked(int uid) {
        getUidStatsLocked(mapUid(uid)).noteActivityResumedLocked(SystemClock.elapsedRealtime());
    }

    public void noteActivityPausedLocked(int uid) {
        getUidStatsLocked(mapUid(uid)).noteActivityPausedLocked(SystemClock.elapsedRealtime());
    }

    public void noteVibratorOnLocked(int uid, long durationMillis) {
        getUidStatsLocked(mapUid(uid)).noteVibratorOnLocked(durationMillis);
    }

    public void noteVibratorOffLocked(int uid) {
        getUidStatsLocked(mapUid(uid)).noteVibratorOffLocked();
    }

    public void noteFlashlightOnLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        int i = this.mFlashlightOnNesting;
        this.mFlashlightOnNesting = i + 1;
        if (i == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 |= 134217728;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mFlashlightOnTimer.startRunningLocked(elapsedRealtime);
        }
        getUidStatsLocked(uid).noteFlashlightTurnedOnLocked(elapsedRealtime);
    }

    public void noteFlashlightOffLocked(int uid) {
        if (this.mFlashlightOnNesting != 0) {
            uid = mapUid(uid);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            int i = this.mFlashlightOnNesting - 1;
            this.mFlashlightOnNesting = i;
            if (i == 0) {
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states2 &= -134217729;
                addHistoryRecordLocked(elapsedRealtime, uptime);
                this.mFlashlightOnTimer.stopRunningLocked(elapsedRealtime);
            }
            getUidStatsLocked(uid).noteFlashlightTurnedOffLocked(elapsedRealtime);
        }
    }

    public void noteCameraOnLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        int i = this.mCameraOnNesting;
        this.mCameraOnNesting = i + 1;
        if (i == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 |= 4194304;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mCameraOnTimer.startRunningLocked(elapsedRealtime);
        }
        getUidStatsLocked(uid).noteCameraTurnedOnLocked(elapsedRealtime);
    }

    public void noteCameraOffLocked(int uid) {
        if (this.mCameraOnNesting != 0) {
            uid = mapUid(uid);
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            int i = this.mCameraOnNesting - 1;
            this.mCameraOnNesting = i;
            if (i == 0) {
                HistoryItem historyItem = this.mHistoryCur;
                historyItem.states2 &= -4194305;
                addHistoryRecordLocked(elapsedRealtime, uptime);
                this.mCameraOnTimer.stopRunningLocked(elapsedRealtime);
            }
            getUidStatsLocked(uid).noteCameraTurnedOffLocked(elapsedRealtime);
        }
    }

    public void noteResetCameraLocked() {
        if (this.mCameraOnNesting > 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            this.mCameraOnNesting = 0;
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -4194305;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mCameraOnTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                ((Uid) this.mUidStats.valueAt(i)).noteResetCameraLocked(elapsedRealtime);
            }
        }
    }

    public void noteResetFlashlightLocked() {
        if (this.mFlashlightOnNesting > 0) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            this.mFlashlightOnNesting = 0;
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -134217729;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mFlashlightOnTimer.stopAllRunningLocked(elapsedRealtime);
            for (int i = 0; i < this.mUidStats.size(); i++) {
                ((Uid) this.mUidStats.valueAt(i)).noteResetFlashlightLocked(elapsedRealtime);
            }
        }
    }

    public void noteWifiRadioPowerState(int powerState, long timestampNs) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        if (this.mWifiRadioPowerState != powerState) {
            boolean active = powerState == DataConnectionRealTimeInfo.DC_POWER_STATE_MEDIUM || powerState == DataConnectionRealTimeInfo.DC_POWER_STATE_HIGH;
            HistoryItem historyItem;
            if (active) {
                historyItem = this.mHistoryCur;
                historyItem.states |= 67108864;
            } else {
                historyItem = this.mHistoryCur;
                historyItem.states &= -67108865;
            }
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mWifiRadioPowerState = powerState;
        }
    }

    public void noteWifiRunningLocked(WorkSource ws) {
        if (this.mGlobalWifiRunning) {
            Log.w(TAG, "noteWifiRunningLocked -- called while WIFI running");
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        HistoryItem historyItem = this.mHistoryCur;
        historyItem.states2 |= 536870912;
        addHistoryRecordLocked(elapsedRealtime, uptime);
        this.mGlobalWifiRunning = true;
        this.mGlobalWifiRunningTimer.startRunningLocked(elapsedRealtime);
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            getUidStatsLocked(mapUid(ws.get(i))).noteWifiRunningLocked(elapsedRealtime);
        }
        scheduleSyncExternalWifiStatsLocked("wifi-running");
    }

    public void noteWifiRunningChangedLocked(WorkSource oldWs, WorkSource newWs) {
        if (this.mGlobalWifiRunning) {
            int i;
            long elapsedRealtime = SystemClock.elapsedRealtime();
            int N = oldWs.size();
            for (i = 0; i < N; i++) {
                getUidStatsLocked(mapUid(oldWs.get(i))).noteWifiStoppedLocked(elapsedRealtime);
            }
            N = newWs.size();
            for (i = 0; i < N; i++) {
                getUidStatsLocked(mapUid(newWs.get(i))).noteWifiRunningLocked(elapsedRealtime);
            }
            return;
        }
        Log.w(TAG, "noteWifiRunningChangedLocked -- called while WIFI not running");
    }

    public void noteWifiStoppedLocked(WorkSource ws) {
        if (this.mGlobalWifiRunning) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states2 &= -536870913;
            addHistoryRecordLocked(elapsedRealtime, uptime);
            this.mGlobalWifiRunning = false;
            this.mGlobalWifiRunningTimer.stopRunningLocked(elapsedRealtime);
            int N = ws.size();
            for (int i = 0; i < N; i++) {
                getUidStatsLocked(mapUid(ws.get(i))).noteWifiStoppedLocked(elapsedRealtime);
            }
            scheduleSyncExternalWifiStatsLocked("wifi-stopped");
            return;
        }
        Log.w(TAG, "noteWifiStoppedLocked -- called while WIFI not running");
    }

    public void noteWifiStateLocked(int wifiState, String accessPoint) {
        if (this.mWifiState != wifiState) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            if (this.mWifiState >= 0) {
                this.mWifiStateTimer[this.mWifiState].stopRunningLocked(elapsedRealtime);
            }
            this.mWifiState = wifiState;
            this.mWifiStateTimer[wifiState].startRunningLocked(elapsedRealtime);
            scheduleSyncExternalWifiStatsLocked("wifi-state");
        }
    }

    public void noteWifiSupplicantStateChangedLocked(int supplState, boolean failedAuth) {
        if (this.mWifiSupplState != supplState) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            if (this.mWifiSupplState >= 0) {
                this.mWifiSupplStateTimer[this.mWifiSupplState].stopRunningLocked(elapsedRealtime);
            }
            this.mWifiSupplState = supplState;
            this.mWifiSupplStateTimer[supplState].startRunningLocked(elapsedRealtime);
            this.mHistoryCur.states2 = (this.mHistoryCur.states2 & -16) | (supplState << 0);
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
    }

    void stopAllWifiSignalStrengthTimersLocked(int except) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        for (int i = 0; i < 5; i++) {
            if (i != except) {
                while (this.mWifiSignalStrengthsTimer[i].isRunningLocked()) {
                    this.mWifiSignalStrengthsTimer[i].stopRunningLocked(elapsedRealtime);
                }
            }
        }
    }

    public void noteWifiRssiChangedLocked(int newRssi) {
        int strengthBin = WifiManager.calculateSignalLevel(newRssi, 5);
        if (this.mWifiSignalStrengthBin != strengthBin) {
            long elapsedRealtime = SystemClock.elapsedRealtime();
            long uptime = SystemClock.uptimeMillis();
            if (this.mWifiSignalStrengthBin >= 0) {
                this.mWifiSignalStrengthsTimer[this.mWifiSignalStrengthBin].stopRunningLocked(elapsedRealtime);
            }
            if (strengthBin >= 0) {
                if (!this.mWifiSignalStrengthsTimer[strengthBin].isRunningLocked()) {
                    this.mWifiSignalStrengthsTimer[strengthBin].startRunningLocked(elapsedRealtime);
                }
                this.mHistoryCur.states2 = (this.mHistoryCur.states2 & -113) | (strengthBin << 4);
                addHistoryRecordLocked(elapsedRealtime, uptime);
            } else {
                stopAllWifiSignalStrengthTimersLocked(-1);
            }
            this.mWifiSignalStrengthBin = strengthBin;
        }
    }

    public void noteFullWifiLockAcquiredLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        if (this.mWifiFullLockNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states |= 268435456;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        this.mWifiFullLockNesting++;
        getUidStatsLocked(uid).noteFullWifiLockAcquiredLocked(elapsedRealtime);
    }

    public void noteFullWifiLockReleasedLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        this.mWifiFullLockNesting--;
        if (this.mWifiFullLockNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states &= -268435457;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        getUidStatsLocked(uid).noteFullWifiLockReleasedLocked(elapsedRealtime);
    }

    public void noteWifiScanStartedLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        if (this.mWifiScanNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states |= 134217728;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        this.mWifiScanNesting++;
        getUidStatsLocked(uid).noteWifiScanStartedLocked(elapsedRealtime);
    }

    public void noteWifiScanStoppedLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        this.mWifiScanNesting--;
        if (this.mWifiScanNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states &= -134217729;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        getUidStatsLocked(uid).noteWifiScanStoppedLocked(elapsedRealtime);
    }

    public void noteWifiBatchedScanStartedLocked(int uid, int csph) {
        uid = mapUid(uid);
        getUidStatsLocked(uid).noteWifiBatchedScanStartedLocked(csph, SystemClock.elapsedRealtime());
    }

    public void noteWifiBatchedScanStoppedLocked(int uid) {
        uid = mapUid(uid);
        getUidStatsLocked(uid).noteWifiBatchedScanStoppedLocked(SystemClock.elapsedRealtime());
    }

    public void noteWifiMulticastEnabledLocked(int uid) {
        Slog.e(TAG, "Wifi multicast lock enabled by UID  = " + uid);
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        if (this.mWifiMulticastNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states |= 65536;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        this.mWifiMulticastNesting++;
        getUidStatsLocked(uid).noteWifiMulticastEnabledLocked(elapsedRealtime);
    }

    public void noteWifiMulticastDisabledLocked(int uid) {
        uid = mapUid(uid);
        long elapsedRealtime = SystemClock.elapsedRealtime();
        long uptime = SystemClock.uptimeMillis();
        this.mWifiMulticastNesting--;
        if (this.mWifiMulticastNesting == 0) {
            HistoryItem historyItem = this.mHistoryCur;
            historyItem.states &= -65537;
            addHistoryRecordLocked(elapsedRealtime, uptime);
        }
        getUidStatsLocked(uid).noteWifiMulticastDisabledLocked(elapsedRealtime);
    }

    public void noteFullWifiLockAcquiredFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteFullWifiLockAcquiredLocked(ws.get(i));
        }
    }

    public void noteFullWifiLockReleasedFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteFullWifiLockReleasedLocked(ws.get(i));
        }
    }

    public void noteWifiScanStartedFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiScanStartedLocked(ws.get(i));
        }
    }

    public void noteWifiScanStoppedFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiScanStoppedLocked(ws.get(i));
        }
    }

    public void noteWifiBatchedScanStartedFromSourceLocked(WorkSource ws, int csph) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiBatchedScanStartedLocked(ws.get(i), csph);
        }
    }

    public void noteWifiBatchedScanStoppedFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiBatchedScanStoppedLocked(ws.get(i));
        }
    }

    public void noteWifiMulticastEnabledFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiMulticastEnabledLocked(ws.get(i));
        }
    }

    public void noteWifiMulticastDisabledFromSourceLocked(WorkSource ws) {
        int N = ws.size();
        for (int i = 0; i < N; i++) {
            noteWifiMulticastDisabledLocked(ws.get(i));
        }
    }

    private static String[] includeInStringArray(String[] array, String str) {
        if (ArrayUtils.indexOf(array, str) >= 0) {
            return array;
        }
        String[] newArray = new String[(array.length + 1)];
        System.arraycopy(array, 0, newArray, 0, array.length);
        newArray[array.length] = str;
        return newArray;
    }

    private static String[] excludeFromStringArray(String[] array, String str) {
        int index = ArrayUtils.indexOf(array, str);
        if (index < 0) {
            return array;
        }
        String[] newArray = new String[(array.length - 1)];
        if (index > 0) {
            System.arraycopy(array, 0, newArray, 0, index);
        }
        if (index >= array.length - 1) {
            return newArray;
        }
        System.arraycopy(array, index + 1, newArray, index, (array.length - index) - 1);
        return newArray;
    }

    public void noteNetworkInterfaceTypeLocked(String iface, int networkType) {
        if (!TextUtils.isEmpty(iface)) {
            if (ConnectivityManager.isNetworkTypeMobile(networkType)) {
                this.mMobileIfaces = includeInStringArray(this.mMobileIfaces, iface);
            } else {
                this.mMobileIfaces = excludeFromStringArray(this.mMobileIfaces, iface);
            }
            if (ConnectivityManager.isNetworkTypeWifi(networkType)) {
                this.mWifiIfaces = includeInStringArray(this.mWifiIfaces, iface);
            } else {
                this.mWifiIfaces = excludeFromStringArray(this.mWifiIfaces, iface);
            }
        }
    }

    public void noteNetworkStatsEnabledLocked() {
        updateMobileRadioStateLocked(SystemClock.elapsedRealtime());
        updateWifiStateLocked(null);
    }

    public long getScreenOnTime(long elapsedRealtimeUs, int which) {
        return this.mScreenOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getScreenOnCount(int which) {
        return this.mScreenOnTimer.getCountLocked(which);
    }

    public long getScreenBrightnessTime(int brightnessBin, long elapsedRealtimeUs, int which) {
        return this.mScreenBrightnessTimer[brightnessBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getInteractiveTime(long elapsedRealtimeUs, int which) {
        return this.mInteractiveTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getPowerSaveModeEnabledTime(long elapsedRealtimeUs, int which) {
        return this.mPowerSaveModeEnabledTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getPowerSaveModeEnabledCount(int which) {
        return this.mPowerSaveModeEnabledTimer.getCountLocked(which);
    }

    public long getDeviceIdleModeEnabledTime(long elapsedRealtimeUs, int which) {
        return this.mDeviceIdleModeEnabledTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getDeviceIdleModeEnabledCount(int which) {
        return this.mDeviceIdleModeEnabledTimer.getCountLocked(which);
    }

    public long getDeviceIdlingTime(long elapsedRealtimeUs, int which) {
        return this.mDeviceIdlingTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getDeviceIdlingCount(int which) {
        return this.mDeviceIdlingTimer.getCountLocked(which);
    }

    public int getNumConnectivityChange(int which) {
        int val = this.mNumConnectivityChange;
        if (which == 1) {
            return val - this.mLoadedNumConnectivityChange;
        }
        if (which == 2) {
            return val - this.mUnpluggedNumConnectivityChange;
        }
        return val;
    }

    public long getPhoneOnTime(long elapsedRealtimeUs, int which) {
        return this.mPhoneOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getPhoneOnCount(int which) {
        return this.mPhoneOnTimer.getCountLocked(which);
    }

    public long getPhoneSignalStrengthTime(int strengthBin, long elapsedRealtimeUs, int which) {
        return this.mPhoneSignalStrengthsTimer[strengthBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getPhoneSignalScanningTime(long elapsedRealtimeUs, int which) {
        return this.mPhoneSignalScanningTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getPhoneSignalStrengthCount(int strengthBin, int which) {
        return this.mPhoneSignalStrengthsTimer[strengthBin].getCountLocked(which);
    }

    public long getPhoneDataConnectionTime(int dataType, long elapsedRealtimeUs, int which) {
        return this.mPhoneDataConnectionsTimer[dataType].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getPhoneDataConnectionCount(int dataType, int which) {
        return this.mPhoneDataConnectionsTimer[dataType].getCountLocked(which);
    }

    public long getMobileRadioActiveTime(long elapsedRealtimeUs, int which) {
        return this.mMobileRadioActiveTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getMobileRadioActiveCount(int which) {
        return this.mMobileRadioActiveTimer.getCountLocked(which);
    }

    public long getMobileRadioActiveAdjustedTime(int which) {
        return this.mMobileRadioActiveAdjustedTime.getCountLocked(which);
    }

    public long getMobileRadioActiveUnknownTime(int which) {
        return this.mMobileRadioActiveUnknownTime.getCountLocked(which);
    }

    public int getMobileRadioActiveUnknownCount(int which) {
        return (int) this.mMobileRadioActiveUnknownCount.getCountLocked(which);
    }

    public long getWifiOnTime(long elapsedRealtimeUs, int which) {
        return this.mWifiOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getGlobalWifiRunningTime(long elapsedRealtimeUs, int which) {
        return this.mGlobalWifiRunningTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getWifiStateTime(int wifiState, long elapsedRealtimeUs, int which) {
        return this.mWifiStateTimer[wifiState].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getWifiStateCount(int wifiState, int which) {
        return this.mWifiStateTimer[wifiState].getCountLocked(which);
    }

    public long getWifiSupplStateTime(int state, long elapsedRealtimeUs, int which) {
        return this.mWifiSupplStateTimer[state].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getWifiSupplStateCount(int state, int which) {
        return this.mWifiSupplStateTimer[state].getCountLocked(which);
    }

    public long getWifiSignalStrengthTime(int strengthBin, long elapsedRealtimeUs, int which) {
        return this.mWifiSignalStrengthsTimer[strengthBin].getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public int getWifiSignalStrengthCount(int strengthBin, int which) {
        return this.mWifiSignalStrengthsTimer[strengthBin].getCountLocked(which);
    }

    public boolean hasBluetoothActivityReporting() {
        return this.mHasBluetoothEnergyReporting;
    }

    public long getBluetoothControllerActivity(int type, int which) {
        if (type < 0 || type >= this.mBluetoothActivityCounters.length) {
            return 0;
        }
        return this.mBluetoothActivityCounters[type].getCountLocked(which);
    }

    public boolean hasWifiActivityReporting() {
        return this.mHasWifiEnergyReporting;
    }

    public long getWifiControllerActivity(int type, int which) {
        if (type < 0 || type >= this.mWifiActivityCounters.length) {
            return 0;
        }
        return this.mWifiActivityCounters[type].getCountLocked(which);
    }

    public long getFlashlightOnTime(long elapsedRealtimeUs, int which) {
        return this.mFlashlightOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getFlashlightOnCount(int which) {
        return (long) this.mFlashlightOnTimer.getCountLocked(which);
    }

    public long getCameraOnTime(long elapsedRealtimeUs, int which) {
        return this.mCameraOnTimer.getTotalTimeLocked(elapsedRealtimeUs, which);
    }

    public long getNetworkActivityBytes(int type, int which) {
        if (this.mNetworkByteActivityCounters == null || type < 0 || type >= this.mNetworkByteActivityCounters.length) {
            return 0;
        }
        return this.mNetworkByteActivityCounters[type].getCountLocked(which);
    }

    public long getNetworkActivityPackets(int type, int which) {
        if (this.mNetworkPacketActivityCounters == null || type < 0 || type >= this.mNetworkPacketActivityCounters.length) {
            return 0;
        }
        return this.mNetworkPacketActivityCounters[type].getCountLocked(which);
    }

    public long getStartClockTime() {
        long currentTime = System.currentTimeMillis();
        if (ensureStartClockTime(currentTime)) {
            recordCurrentTimeChangeLocked(currentTime, SystemClock.elapsedRealtime(), SystemClock.uptimeMillis());
        }
        return this.mStartClockTime;
    }

    public String getStartPlatformVersion() {
        return this.mStartPlatformVersion;
    }

    public String getEndPlatformVersion() {
        return this.mEndPlatformVersion;
    }

    public int getParcelVersion() {
        return 132;
    }

    public boolean getIsOnBattery() {
        return this.mOnBattery;
    }

    public SparseArray<? extends android.os.BatteryStats.Uid> getUidStats() {
        return this.mUidStats;
    }

    public BatteryStatsImpl(File systemDir, Handler handler, ExternalStatsSync externalSync) {
        int i;
        this.mKernelWakelockReader = new KernelWakelockReader();
        this.mTmpWakelockStats = new KernelWakelockStats();
        this.mKernelUidCpuTimeReader = new KernelUidCpuTimeReader();
        this.mIsolatedUids = new SparseIntArray();
        this.mUidStats = new SparseArray();
        this.mPartialTimers = new ArrayList();
        this.mFullTimers = new ArrayList();
        this.mWindowTimers = new ArrayList();
        this.mDrawTimers = new ArrayList();
        this.mSensorTimers = new SparseArray();
        this.mWifiRunningTimers = new ArrayList();
        this.mFullWifiLockTimers = new ArrayList();
        this.mWifiMulticastTimers = new ArrayList();
        this.mWifiScanTimers = new ArrayList();
        this.mWifiBatchedScanTimers = new SparseArray();
        this.mAudioTurnedOnTimers = new ArrayList();
        this.mVideoTurnedOnTimers = new ArrayList();
        this.mFlashlightTurnedOnTimers = new ArrayList();
        this.mCameraTurnedOnTimers = new ArrayList();
        this.mLastPartialTimers = new ArrayList();
        this.mOnBatteryTimeBase = new TimeBase();
        this.mOnBatteryScreenOffTimeBase = new TimeBase();
        this.mActiveEvents = new HistoryEventTracker();
        this.mHaveBatteryLevel = false;
        this.mRecordingHistory = false;
        this.mHistoryBuffer = Parcel.obtain();
        this.mHistoryLastWritten = new HistoryItem();
        this.mHistoryLastLastWritten = new HistoryItem();
        this.mHistoryReadTmp = new HistoryItem();
        this.mHistoryAddTmp = new HistoryItem();
        this.mHistoryTagPool = new HashMap();
        this.mNextHistoryTagIdx = 0;
        this.mNumHistoryTagChars = 0;
        this.mHistoryBufferLastPos = -1;
        this.mHistoryOverflow = false;
        this.mActiveHistoryStates = -1;
        this.mActiveHistoryStates2 = -1;
        this.mLastHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryUptime = 0;
        this.mHistoryCur = new HistoryItem();
        this.mLastHistoryStepDetails = null;
        this.mLastHistoryStepLevel = (byte) 0;
        this.mCurHistoryStepDetails = new HistoryStepDetails();
        this.mReadHistoryStepDetails = new HistoryStepDetails();
        this.mTmpHistoryStepDetails = new HistoryStepDetails();
        this.mScreenState = 0;
        this.mScreenBrightnessBin = -1;
        this.mScreenBrightnessTimer = new StopwatchTimer[5];
        this.mPhoneSignalStrengthBin = -1;
        this.mPhoneSignalStrengthBinRaw = -1;
        this.mPhoneSignalStrengthsTimer = new StopwatchTimer[SignalStrength.NUM_SIGNAL_STRENGTH_BINS];
        this.mPhoneDataConnectionType = -1;
        this.mPhoneDataConnectionsTimer = new StopwatchTimer[17];
        this.mNetworkByteActivityCounters = new LongSamplingCounter[4];
        this.mNetworkPacketActivityCounters = new LongSamplingCounter[4];
        this.mBluetoothActivityCounters = new LongSamplingCounter[4];
        this.mWifiActivityCounters = new LongSamplingCounter[4];
        this.mWifiState = -1;
        this.mWifiStateTimer = new StopwatchTimer[8];
        this.mWifiSupplState = -1;
        this.mWifiSupplStateTimer = new StopwatchTimer[13];
        this.mWifiSignalStrengthBin = -1;
        this.mWifiSignalStrengthsTimer = new StopwatchTimer[5];
        this.mMobileRadioPowerState = DataConnectionRealTimeInfo.DC_POWER_STATE_LOW;
        this.mWifiRadioPowerState = DataConnectionRealTimeInfo.DC_POWER_STATE_LOW;
        this.mCharging = true;
        this.mInitStepMode = 0;
        this.mCurStepMode = 0;
        this.mModStepMode = 0;
        this.mDischargeStepTracker = new LevelStepTracker(200);
        this.mDailyDischargeStepTracker = new LevelStepTracker(400);
        this.mChargeStepTracker = new LevelStepTracker(200);
        this.mDailyChargeStepTracker = new LevelStepTracker(400);
        this.mDailyStartTime = 0;
        this.mNextMinDailyDeadline = 0;
        this.mNextMaxDailyDeadline = 0;
        this.mDailyItems = new ArrayList();
        this.mLastWriteTime = 0;
        this.mPhoneServiceState = -1;
        this.mPhoneServiceStateRaw = -1;
        this.mPhoneSimStateRaw = -1;
        this.mTmpNetworkStatsEntry = new Entry();
        this.mHasWifiEnergyReporting = false;
        this.mHasBluetoothEnergyReporting = false;
        this.mKernelWakelockStats = new HashMap();
        this.mLastWakeupReason = null;
        this.mLastWakeupUptimeMs = 0;
        this.mWakeupReasonStats = new HashMap();
        this.mChangedStates = 0;
        this.mChangedStates2 = 0;
        this.mInitialAcquireWakeUid = -1;
        this.mWifiFullLockNesting = 0;
        this.mWifiScanNesting = 0;
        this.mWifiMulticastNesting = 0;
        this.mMobileIfaces = EmptyArray.STRING;
        this.mWifiIfaces = EmptyArray.STRING;
        this.mNetworkStatsFactory = new NetworkStatsFactory();
        this.mMobileNetworkStats = new NetworkStats[]{new NetworkStats(SystemClock.elapsedRealtime(), 50), new NetworkStats(SystemClock.elapsedRealtime(), 50), new NetworkStats(SystemClock.elapsedRealtime(), 50)};
        this.mWifiNetworkStats = new NetworkStats[]{new NetworkStats(SystemClock.elapsedRealtime(), 50), new NetworkStats(SystemClock.elapsedRealtime(), 50), new NetworkStats(SystemClock.elapsedRealtime(), 50)};
        this.mFeatureComputeChargeTimeModel = true;
        this.CHARGE_TIME_PATH = "/sys/class/power_supply/battery/time_to_full_now";
        this.mPendingWrite = null;
        this.mWriteLock = new ReentrantLock();
        if (systemDir != null) {
            this.mFile = new JournaledFile(new File(systemDir, "batterystats.bin"), new File(systemDir, "batterystats.bin.tmp"));
        } else {
            this.mFile = null;
        }
        this.mCheckinFile = new AtomicFile(new File(systemDir, "batterystats-checkin.bin"));
        this.mDailyFile = new AtomicFile(new File(systemDir, "batterystats-daily.xml"));
        this.mExternalSync = externalSync;
        this.mHandler = new MyHandler(handler.getLooper());
        this.mStartCount++;
        this.mScreenOnTimer = new StopwatchTimer(null, -1, null, this.mOnBatteryTimeBase);
        for (i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i] = new StopwatchTimer(null, -100 - i, null, this.mOnBatteryTimeBase);
        }
        this.mInteractiveTimer = new StopwatchTimer(null, -10, null, this.mOnBatteryTimeBase);
        this.mPowerSaveModeEnabledTimer = new StopwatchTimer(null, -2, null, this.mOnBatteryTimeBase);
        this.mDeviceIdleModeEnabledTimer = new StopwatchTimer(null, -11, null, this.mOnBatteryTimeBase);
        this.mDeviceIdlingTimer = new StopwatchTimer(null, -12, null, this.mOnBatteryTimeBase);
        this.mPhoneOnTimer = new StopwatchTimer(null, -3, null, this.mOnBatteryTimeBase);
        for (i = 0; i < SignalStrength.NUM_SIGNAL_STRENGTH_BINS; i++) {
            this.mPhoneSignalStrengthsTimer[i] = new StopwatchTimer(null, -200 - i, null, this.mOnBatteryTimeBase);
        }
        this.mPhoneSignalScanningTimer = new StopwatchTimer(null, -199, null, this.mOnBatteryTimeBase);
        for (i = 0; i < 17; i++) {
            this.mPhoneDataConnectionsTimer[i] = new StopwatchTimer(null, -300 - i, null, this.mOnBatteryTimeBase);
        }
        for (i = 0; i < 4; i++) {
            this.mNetworkByteActivityCounters[i] = new LongSamplingCounter(this.mOnBatteryTimeBase);
            this.mNetworkPacketActivityCounters[i] = new LongSamplingCounter(this.mOnBatteryTimeBase);
        }
        for (i = 0; i < 4; i++) {
            this.mBluetoothActivityCounters[i] = new LongSamplingCounter(this.mOnBatteryTimeBase);
            this.mWifiActivityCounters[i] = new LongSamplingCounter(this.mOnBatteryTimeBase);
        }
        this.mMobileRadioActiveTimer = new StopwatchTimer(null, -400, null, this.mOnBatteryTimeBase);
        this.mMobileRadioActivePerAppTimer = new StopwatchTimer(null, -401, null, this.mOnBatteryTimeBase);
        this.mMobileRadioActiveAdjustedTime = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mMobileRadioActiveUnknownTime = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mMobileRadioActiveUnknownCount = new LongSamplingCounter(this.mOnBatteryTimeBase);
        this.mWifiOnTimer = new StopwatchTimer(null, -4, null, this.mOnBatteryTimeBase);
        this.mGlobalWifiRunningTimer = new StopwatchTimer(null, -5, null, this.mOnBatteryTimeBase);
        for (i = 0; i < 8; i++) {
            this.mWifiStateTimer[i] = new StopwatchTimer(null, -600 - i, null, this.mOnBatteryTimeBase);
        }
        for (i = 0; i < 13; i++) {
            this.mWifiSupplStateTimer[i] = new StopwatchTimer(null, -700 - i, null, this.mOnBatteryTimeBase);
        }
        for (i = 0; i < 5; i++) {
            this.mWifiSignalStrengthsTimer[i] = new StopwatchTimer(null, -800 - i, null, this.mOnBatteryTimeBase);
        }
        this.mAudioOnTimer = new StopwatchTimer(null, -7, null, this.mOnBatteryTimeBase);
        this.mVideoOnTimer = new StopwatchTimer(null, -8, null, this.mOnBatteryTimeBase);
        this.mFlashlightOnTimer = new StopwatchTimer(null, -9, null, this.mOnBatteryTimeBase);
        this.mCameraOnTimer = new StopwatchTimer(null, -13, null, this.mOnBatteryTimeBase);
        this.mOnBatteryInternal = false;
        this.mOnBattery = false;
        initTimes(SystemClock.uptimeMillis() * 1000, SystemClock.elapsedRealtime() * 1000);
        String str = Build.ID;
        this.mEndPlatformVersion = str;
        this.mStartPlatformVersion = str;
        this.mDischargeStartLevel = 0;
        this.mDischargeUnplugLevel = 0;
        this.mDischargePlugLevel = -1;
        this.mDischargeCurrentLevel = 0;
        this.mCurrentBatteryLevel = 0;
        initDischarge();
        clearHistoryLocked();
        updateDailyDeadlineLocked();
    }

    public BatteryStatsImpl(Parcel p) {
        this.mKernelWakelockReader = new KernelWakelockReader();
        this.mTmpWakelockStats = new KernelWakelockStats();
        this.mKernelUidCpuTimeReader = new KernelUidCpuTimeReader();
        this.mIsolatedUids = new SparseIntArray();
        this.mUidStats = new SparseArray();
        this.mPartialTimers = new ArrayList();
        this.mFullTimers = new ArrayList();
        this.mWindowTimers = new ArrayList();
        this.mDrawTimers = new ArrayList();
        this.mSensorTimers = new SparseArray();
        this.mWifiRunningTimers = new ArrayList();
        this.mFullWifiLockTimers = new ArrayList();
        this.mWifiMulticastTimers = new ArrayList();
        this.mWifiScanTimers = new ArrayList();
        this.mWifiBatchedScanTimers = new SparseArray();
        this.mAudioTurnedOnTimers = new ArrayList();
        this.mVideoTurnedOnTimers = new ArrayList();
        this.mFlashlightTurnedOnTimers = new ArrayList();
        this.mCameraTurnedOnTimers = new ArrayList();
        this.mLastPartialTimers = new ArrayList();
        this.mOnBatteryTimeBase = new TimeBase();
        this.mOnBatteryScreenOffTimeBase = new TimeBase();
        this.mActiveEvents = new HistoryEventTracker();
        this.mHaveBatteryLevel = false;
        this.mRecordingHistory = false;
        this.mHistoryBuffer = Parcel.obtain();
        this.mHistoryLastWritten = new HistoryItem();
        this.mHistoryLastLastWritten = new HistoryItem();
        this.mHistoryReadTmp = new HistoryItem();
        this.mHistoryAddTmp = new HistoryItem();
        this.mHistoryTagPool = new HashMap();
        this.mNextHistoryTagIdx = 0;
        this.mNumHistoryTagChars = 0;
        this.mHistoryBufferLastPos = -1;
        this.mHistoryOverflow = false;
        this.mActiveHistoryStates = -1;
        this.mActiveHistoryStates2 = -1;
        this.mLastHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryElapsedRealtime = 0;
        this.mTrackRunningHistoryUptime = 0;
        this.mHistoryCur = new HistoryItem();
        this.mLastHistoryStepDetails = null;
        this.mLastHistoryStepLevel = (byte) 0;
        this.mCurHistoryStepDetails = new HistoryStepDetails();
        this.mReadHistoryStepDetails = new HistoryStepDetails();
        this.mTmpHistoryStepDetails = new HistoryStepDetails();
        this.mScreenState = 0;
        this.mScreenBrightnessBin = -1;
        this.mScreenBrightnessTimer = new StopwatchTimer[5];
        this.mPhoneSignalStrengthBin = -1;
        this.mPhoneSignalStrengthBinRaw = -1;
        this.mPhoneSignalStrengthsTimer = new StopwatchTimer[SignalStrength.NUM_SIGNAL_STRENGTH_BINS];
        this.mPhoneDataConnectionType = -1;
        this.mPhoneDataConnectionsTimer = new StopwatchTimer[17];
        this.mNetworkByteActivityCounters = new LongSamplingCounter[4];
        this.mNetworkPacketActivityCounters = new LongSamplingCounter[4];
        this.mBluetoothActivityCounters = new LongSamplingCounter[4];
        this.mWifiActivityCounters = new LongSamplingCounter[4];
        this.mWifiState = -1;
        this.mWifiStateTimer = new StopwatchTimer[8];
        this.mWifiSupplState = -1;
        this.mWifiSupplStateTimer = new StopwatchTimer[13];
        this.mWifiSignalStrengthBin = -1;
        this.mWifiSignalStrengthsTimer = new StopwatchTimer[5];
        this.mMobileRadioPowerState = DataConnectionRealTimeInfo.DC_POWER_STATE_LOW;
        this.mWifiRadioPowerState = DataConnectionRealTimeInfo.DC_POWER_STATE_LOW;
        this.mCharging = true;
        this.mInitStepMode = 0;
        this.mCurStepMode = 0;
        this.mModStepMode = 0;
        this.mDischargeStepTracker = new LevelStepTracker(200);
        this.mDailyDischargeStepTracker = new LevelStepTracker(400);
        this.mChargeStepTracker = new LevelStepTracker(200);
        this.mDailyChargeStepTracker = new LevelStepTracker(400);
        this.mDailyStartTime = 0;
        this.mNextMinDailyDeadline = 0;
        this.mNextMaxDailyDeadline = 0;
        this.mDailyItems = new ArrayList();
        this.mLastWriteTime = 0;
        this.mPhoneServiceState = -1;
        this.mPhoneServiceStateRaw = -1;
        this.mPhoneSimStateRaw = -1;
        this.mTmpNetworkStatsEntry = new Entry();
        this.mHasWifiEnergyReporting = false;
        this.mHasBluetoothEnergyReporting = false;
        this.mKernelWakelockStats = new HashMap();
        this.mLastWakeupReason = null;
        this.mLastWakeupUptimeMs = 0;
        this.mWakeupReasonStats = new HashMap();
        this.mChangedStates = 0;
        this.mChangedStates2 = 0;
        this.mInitialAcquireWakeUid = -1;
        this.mWifiFullLockNesting = 0;
        this.mWifiScanNesting = 0;
        this.mWifiMulticastNesting = 0;
        this.mMobileIfaces = EmptyArray.STRING;
        this.mWifiIfaces = EmptyArray.STRING;
        this.mNetworkStatsFactory = new NetworkStatsFactory();
        this.mMobileNetworkStats = new NetworkStats[]{new NetworkStats(SystemClock.elapsedRealtime(), 50), new NetworkStats(SystemClock.elapsedRealtime(), 50), new NetworkStats(SystemClock.elapsedRealtime(), 50)};
        this.mWifiNetworkStats = new NetworkStats[]{new NetworkStats(SystemClock.elapsedRealtime(), 50), new NetworkStats(SystemClock.elapsedRealtime(), 50), new NetworkStats(SystemClock.elapsedRealtime(), 50)};
        this.mFeatureComputeChargeTimeModel = true;
        this.CHARGE_TIME_PATH = "/sys/class/power_supply/battery/time_to_full_now";
        this.mPendingWrite = null;
        this.mWriteLock = new ReentrantLock();
        this.mFile = null;
        this.mCheckinFile = null;
        this.mDailyFile = null;
        this.mHandler = null;
        this.mExternalSync = null;
        clearHistoryLocked();
        readFromParcel(p);
    }

    public void setPowerProfile(PowerProfile profile) {
        synchronized (this) {
            this.mPowerProfile = profile;
            int numClusters = this.mPowerProfile.getNumCpuClusters();
            this.mKernelCpuSpeedReaders = new KernelCpuSpeedReader[numClusters];
            int firstCpuOfCluster = 0;
            for (int i = 0; i < numClusters; i++) {
                this.mKernelCpuSpeedReaders[i] = new KernelCpuSpeedReader(firstCpuOfCluster, this.mPowerProfile.getNumSpeedStepsInCpuCluster(i));
                firstCpuOfCluster += this.mPowerProfile.getNumCoresInCpuCluster(i);
            }
        }
    }

    public void setCallback(BatteryCallback cb) {
        this.mCallback = cb;
    }

    public void setRadioScanningTimeout(long timeout) {
        if (this.mPhoneSignalScanningTimer != null) {
            this.mPhoneSignalScanningTimer.setTimeout(timeout);
        }
    }

    public void updateDailyDeadlineLocked() {
        long currentTime = System.currentTimeMillis();
        this.mDailyStartTime = currentTime;
        Calendar calDeadline = Calendar.getInstance();
        calDeadline.setTimeInMillis(currentTime);
        calDeadline.set(6, calDeadline.get(6) + 1);
        calDeadline.set(14, 0);
        calDeadline.set(13, 0);
        calDeadline.set(12, 0);
        calDeadline.set(11, 1);
        this.mNextMinDailyDeadline = calDeadline.getTimeInMillis();
        calDeadline.set(11, 3);
        this.mNextMaxDailyDeadline = calDeadline.getTimeInMillis();
    }

    public void recordDailyStatsIfNeededLocked(boolean settled) {
        long currentTime = System.currentTimeMillis();
        if (currentTime >= this.mNextMaxDailyDeadline) {
            recordDailyStatsLocked();
        } else if (settled && currentTime >= this.mNextMinDailyDeadline) {
            recordDailyStatsLocked();
        } else if (currentTime < this.mDailyStartTime - DateUtils.DAY_IN_MILLIS) {
            recordDailyStatsLocked();
        }
    }

    public void recordDailyStatsLocked() {
        DailyItem item = new DailyItem();
        item.mStartTime = this.mDailyStartTime;
        item.mEndTime = System.currentTimeMillis();
        boolean hasData = false;
        if (this.mDailyDischargeStepTracker.mNumStepDurations > 0) {
            hasData = true;
            item.mDischargeSteps = new LevelStepTracker(this.mDailyDischargeStepTracker.mNumStepDurations, this.mDailyDischargeStepTracker.mStepDurations);
        }
        if (this.mDailyChargeStepTracker.mNumStepDurations > 0) {
            hasData = true;
            item.mChargeSteps = new LevelStepTracker(this.mDailyChargeStepTracker.mNumStepDurations, this.mDailyChargeStepTracker.mStepDurations);
        }
        if (this.mDailyPackageChanges != null) {
            hasData = true;
            item.mPackageChanges = this.mDailyPackageChanges;
            this.mDailyPackageChanges = null;
        }
        this.mDailyDischargeStepTracker.init();
        this.mDailyChargeStepTracker.init();
        updateDailyDeadlineLocked();
        if (hasData) {
            this.mDailyItems.add(item);
            while (this.mDailyItems.size() > 10) {
                this.mDailyItems.remove(0);
            }
            final ByteArrayOutputStream memStream = new ByteArrayOutputStream();
            try {
                XmlSerializer out = new FastXmlSerializer();
                out.setOutput(memStream, StandardCharsets.UTF_8.name());
                writeDailyItemsLocked(out);
                BackgroundThread.getHandler().post(new Runnable() {
                    public void run() {
                        synchronized (BatteryStatsImpl.this.mCheckinFile) {
                            FileOutputStream stream = null;
                            try {
                                stream = BatteryStatsImpl.this.mDailyFile.startWrite();
                                memStream.writeTo(stream);
                                stream.flush();
                                FileUtils.sync(stream);
                                stream.close();
                                BatteryStatsImpl.this.mDailyFile.finishWrite(stream);
                            } catch (IOException e) {
                                Slog.w("BatteryStats", "Error writing battery daily items", e);
                                BatteryStatsImpl.this.mDailyFile.failWrite(stream);
                            }
                        }
                    }
                });
            } catch (IOException e) {
            }
        }
    }

    private void writeDailyItemsLocked(XmlSerializer out) throws IOException {
        StringBuilder sb = new StringBuilder(64);
        out.startDocument(null, Boolean.valueOf(true));
        out.startTag(null, "daily-items");
        for (int i = 0; i < this.mDailyItems.size(); i++) {
            DailyItem dit = (DailyItem) this.mDailyItems.get(i);
            out.startTag(null, "item");
            out.attribute(null, "start", Long.toString(dit.mStartTime));
            out.attribute(null, "end", Long.toString(dit.mEndTime));
            writeDailyLevelSteps(out, "dis", dit.mDischargeSteps, sb);
            writeDailyLevelSteps(out, "chg", dit.mChargeSteps, sb);
            if (dit.mPackageChanges != null) {
                for (int j = 0; j < dit.mPackageChanges.size(); j++) {
                    PackageChange pc = (PackageChange) dit.mPackageChanges.get(j);
                    if (pc.mUpdate) {
                        out.startTag(null, "upd");
                        out.attribute(null, "pkg", pc.mPackageName);
                        out.attribute(null, "ver", Integer.toString(pc.mVersionCode));
                        out.endTag(null, "upd");
                    } else {
                        out.startTag(null, "rem");
                        out.attribute(null, "pkg", pc.mPackageName);
                        out.endTag(null, "rem");
                    }
                }
            }
            out.endTag(null, "item");
        }
        out.endTag(null, "daily-items");
        out.endDocument();
    }

    private void writeDailyLevelSteps(XmlSerializer out, String tag, LevelStepTracker steps, StringBuilder tmpBuilder) throws IOException {
        if (steps != null) {
            out.startTag(null, tag);
            out.attribute(null, "n", Integer.toString(steps.mNumStepDurations));
            for (int i = 0; i < steps.mNumStepDurations; i++) {
                out.startTag(null, "s");
                tmpBuilder.setLength(0);
                steps.encodeEntryAt(i, tmpBuilder);
                out.attribute(null, "v", tmpBuilder.toString());
                out.endTag(null, "s");
            }
            out.endTag(null, tag);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readDailyStatsLocked() {
        /*
        r6 = this;
        r3 = "BatteryStatsImpl";
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Reading daily items from ";
        r4 = r4.append(r5);
        r5 = r6.mDailyFile;
        r5 = r5.getBaseFile();
        r4 = r4.append(r5);
        r4 = r4.toString();
        android.util.Slog.d(r3, r4);
        r3 = r6.mDailyItems;
        r3.clear();
        r3 = r6.mDailyFile;	 Catch:{ FileNotFoundException -> 0x003d }
        r2 = r3.openRead();	 Catch:{ FileNotFoundException -> 0x003d }
        r1 = android.util.Xml.newPullParser();	 Catch:{ XmlPullParserException -> 0x003f, all -> 0x0046 }
        r3 = java.nio.charset.StandardCharsets.UTF_8;	 Catch:{ XmlPullParserException -> 0x003f, all -> 0x0046 }
        r3 = r3.name();	 Catch:{ XmlPullParserException -> 0x003f, all -> 0x0046 }
        r1.setInput(r2, r3);	 Catch:{ XmlPullParserException -> 0x003f, all -> 0x0046 }
        r6.readDailyItemsLocked(r1);	 Catch:{ XmlPullParserException -> 0x003f, all -> 0x0046 }
        r2.close();	 Catch:{ IOException -> 0x004b }
    L_0x003c:
        return;
    L_0x003d:
        r0 = move-exception;
        goto L_0x003c;
    L_0x003f:
        r3 = move-exception;
        r2.close();	 Catch:{ IOException -> 0x0044 }
        goto L_0x003c;
    L_0x0044:
        r3 = move-exception;
        goto L_0x003c;
    L_0x0046:
        r3 = move-exception;
        r2.close();	 Catch:{ IOException -> 0x004d }
    L_0x004a:
        throw r3;
    L_0x004b:
        r3 = move-exception;
        goto L_0x003c;
    L_0x004d:
        r4 = move-exception;
        goto L_0x004a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.readDailyStatsLocked():void");
    }

    private void readDailyItemsLocked(XmlPullParser parser) {
        int type;
        do {
            try {
                type = parser.next();
                if (type == 2) {
                    break;
                }
            } catch (IllegalStateException e) {
                Slog.w(TAG, "Failed parsing daily " + e);
                return;
            } catch (NullPointerException e2) {
                Slog.w(TAG, "Failed parsing daily " + e2);
                return;
            } catch (NumberFormatException e3) {
                Slog.w(TAG, "Failed parsing daily " + e3);
                return;
            } catch (XmlPullParserException e4) {
                Slog.w(TAG, "Failed parsing daily " + e4);
                return;
            } catch (IOException e5) {
                Slog.w(TAG, "Failed parsing daily " + e5);
                return;
            } catch (IndexOutOfBoundsException e6) {
                Slog.w(TAG, "Failed parsing daily " + e6);
                return;
            }
        } while (type != 1);
        if (type != 2) {
            throw new IllegalStateException("no start tag found");
        }
        int outerDepth = parser.getDepth();
        while (true) {
            type = parser.next();
            if (type == 1) {
                return;
            }
            if (type == 3 && parser.getDepth() <= outerDepth) {
                return;
            }
            if (!(type == 3 || type == 4)) {
                if (parser.getName().equals("item")) {
                    readDailyItemTagLocked(parser);
                } else {
                    Slog.w(TAG, "Unknown element under <daily-items>: " + parser.getName());
                    XmlUtils.skipCurrentTag(parser);
                }
            }
        }
    }

    void readDailyItemTagLocked(XmlPullParser parser) throws NumberFormatException, XmlPullParserException, IOException {
        DailyItem dit = new DailyItem();
        String attr = parser.getAttributeValue(null, "start");
        if (attr != null) {
            dit.mStartTime = Long.parseLong(attr);
        }
        attr = parser.getAttributeValue(null, "end");
        if (attr != null) {
            dit.mEndTime = Long.parseLong(attr);
        }
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                this.mDailyItems.add(dit);
            } else if (!(type == 3 || type == 4)) {
                String tagName = parser.getName();
                if (tagName.equals("dis")) {
                    readDailyItemTagDetailsLocked(parser, dit, false, "dis");
                } else if (tagName.equals("chg")) {
                    readDailyItemTagDetailsLocked(parser, dit, true, "chg");
                } else if (tagName.equals("upd")) {
                    if (dit.mPackageChanges == null) {
                        dit.mPackageChanges = new ArrayList();
                    }
                    pc = new PackageChange();
                    pc.mUpdate = true;
                    pc.mPackageName = parser.getAttributeValue(null, "pkg");
                    String verStr = parser.getAttributeValue(null, "ver");
                    pc.mVersionCode = verStr != null ? Integer.parseInt(verStr) : 0;
                    dit.mPackageChanges.add(pc);
                    XmlUtils.skipCurrentTag(parser);
                } else if (tagName.equals("rem")) {
                    if (dit.mPackageChanges == null) {
                        dit.mPackageChanges = new ArrayList();
                    }
                    pc = new PackageChange();
                    pc.mUpdate = false;
                    pc.mPackageName = parser.getAttributeValue(null, "pkg");
                    dit.mPackageChanges.add(pc);
                    XmlUtils.skipCurrentTag(parser);
                } else {
                    Slog.w(TAG, "Unknown element under <item>: " + parser.getName());
                    XmlUtils.skipCurrentTag(parser);
                }
            }
        }
        this.mDailyItems.add(dit);
    }

    void readDailyItemTagDetailsLocked(XmlPullParser parser, DailyItem dit, boolean isCharge, String tag) throws NumberFormatException, XmlPullParserException, IOException {
        String numAttr = parser.getAttributeValue(null, "n");
        if (numAttr == null) {
            Slog.w(TAG, "Missing 'n' attribute at " + parser.getPositionDescription());
            XmlUtils.skipCurrentTag(parser);
            return;
        }
        int num = Integer.parseInt(numAttr);
        LevelStepTracker steps = new LevelStepTracker(num);
        if (isCharge) {
            dit.mChargeSteps = steps;
        } else {
            dit.mDischargeSteps = steps;
        }
        int i = 0;
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1 || (type == 3 && parser.getDepth() <= outerDepth)) {
                steps.mNumStepDurations = i;
            } else if (!(type == 3 || type == 4)) {
                if (!"s".equals(parser.getName())) {
                    Slog.w(TAG, "Unknown element under <" + tag + ">: " + parser.getName());
                    XmlUtils.skipCurrentTag(parser);
                } else if (i < num) {
                    String valueAttr = parser.getAttributeValue(null, "v");
                    if (valueAttr != null) {
                        steps.decodeEntryAt(i, valueAttr);
                        i++;
                    }
                }
            }
        }
        steps.mNumStepDurations = i;
    }

    public DailyItem getDailyItemLocked(int daysAgo) {
        int index = (this.mDailyItems.size() - 1) - daysAgo;
        return index >= 0 ? (DailyItem) this.mDailyItems.get(index) : null;
    }

    public long getCurrentDailyStartTime() {
        return this.mDailyStartTime;
    }

    public long getNextMinDailyDeadline() {
        return this.mNextMinDailyDeadline;
    }

    public long getNextMaxDailyDeadline() {
        return this.mNextMaxDailyDeadline;
    }

    public boolean startIteratingOldHistoryLocked() {
        HistoryItem historyItem = this.mHistory;
        this.mHistoryIterator = historyItem;
        if (historyItem == null) {
            return false;
        }
        this.mHistoryBuffer.setDataPosition(0);
        this.mHistoryReadTmp.clear();
        this.mReadOverflow = false;
        this.mIteratingHistory = true;
        return true;
    }

    public boolean getNextOldHistoryLocked(HistoryItem out) {
        boolean end;
        if (this.mHistoryBuffer.dataPosition() >= this.mHistoryBuffer.dataSize()) {
            end = true;
        } else {
            end = false;
        }
        if (!end) {
            int i;
            readHistoryDelta(this.mHistoryBuffer, this.mHistoryReadTmp);
            boolean z = this.mReadOverflow;
            if (this.mHistoryReadTmp.cmd == (byte) 6) {
                i = 1;
            } else {
                i = 0;
            }
            this.mReadOverflow = i | z;
        }
        HistoryItem cur = this.mHistoryIterator;
        if (cur != null) {
            out.setTo(cur);
            this.mHistoryIterator = cur.next;
            if (!this.mReadOverflow) {
                if (end) {
                    Slog.w(TAG, "New history ends before old history!");
                } else if (!out.same(this.mHistoryReadTmp)) {
                    PrintWriter pw = new FastPrintWriter(new LogWriter(5, TAG));
                    pw.println("Histories differ!");
                    pw.println("Old history:");
                    new HistoryPrinter().printNextItem(pw, out, 0, false, true);
                    pw.println("New history:");
                    new HistoryPrinter().printNextItem(pw, this.mHistoryReadTmp, 0, false, true);
                    pw.flush();
                }
            }
            return true;
        } else if (this.mReadOverflow || end) {
            return false;
        } else {
            Slog.w(TAG, "Old history ends before new history!");
            return false;
        }
    }

    public void finishIteratingOldHistoryLocked() {
        this.mIteratingHistory = false;
        this.mHistoryBuffer.setDataPosition(this.mHistoryBuffer.dataSize());
        this.mHistoryIterator = null;
    }

    public int getHistoryTotalSize() {
        return 262144;
    }

    public int getHistoryUsedSize() {
        return this.mHistoryBuffer.dataSize();
    }

    public boolean startIteratingHistoryLocked() {
        if (this.mHistoryBuffer.dataSize() <= 0) {
            return false;
        }
        this.mHistoryBuffer.setDataPosition(0);
        this.mReadOverflow = false;
        this.mIteratingHistory = true;
        this.mReadHistoryStrings = new String[this.mHistoryTagPool.size()];
        this.mReadHistoryUids = new int[this.mHistoryTagPool.size()];
        this.mReadHistoryChars = 0;
        for (Map.Entry<HistoryTag, Integer> ent : this.mHistoryTagPool.entrySet()) {
            HistoryTag tag = (HistoryTag) ent.getKey();
            int idx = ((Integer) ent.getValue()).intValue();
            this.mReadHistoryStrings[idx] = tag.string;
            this.mReadHistoryUids[idx] = tag.uid;
            this.mReadHistoryChars += tag.string.length() + 1;
        }
        return true;
    }

    public int getHistoryStringPoolSize() {
        return this.mReadHistoryStrings.length;
    }

    public int getHistoryStringPoolBytes() {
        return (this.mReadHistoryStrings.length * 12) + (this.mReadHistoryChars * 2);
    }

    public String getHistoryTagPoolString(int index) {
        return this.mReadHistoryStrings[index];
    }

    public int getHistoryTagPoolUid(int index) {
        return this.mReadHistoryUids[index];
    }

    public boolean getNextHistoryLocked(HistoryItem out) {
        boolean end;
        int pos = this.mHistoryBuffer.dataPosition();
        if (pos == 0) {
            out.clear();
        }
        if (pos >= this.mHistoryBuffer.dataSize()) {
            end = true;
        } else {
            end = false;
        }
        if (end) {
            return false;
        }
        long lastRealtime = out.time;
        long lastWalltime = out.currentTime;
        readHistoryDelta(this.mHistoryBuffer, out);
        if (!(out.cmd == (byte) 5 || out.cmd == (byte) 7 || lastWalltime == 0)) {
            out.currentTime = (out.time - lastRealtime) + lastWalltime;
        }
        return true;
    }

    public void finishIteratingHistoryLocked() {
        this.mIteratingHistory = false;
        this.mHistoryBuffer.setDataPosition(this.mHistoryBuffer.dataSize());
        this.mReadHistoryStrings = null;
    }

    public long getHistoryBaseTime() {
        return this.mHistoryBaseTime;
    }

    public int getStartCount() {
        return this.mStartCount;
    }

    public boolean isOnBattery() {
        return this.mOnBattery;
    }

    public boolean isCharging() {
        return this.mCharging;
    }

    public boolean isScreenOn() {
        return this.mScreenState == 2;
    }

    void initTimes(long uptime, long realtime) {
        this.mStartClockTime = System.currentTimeMillis();
        this.mOnBatteryTimeBase.init(uptime, realtime);
        this.mOnBatteryScreenOffTimeBase.init(uptime, realtime);
        this.mRealtime = 0;
        this.mUptime = 0;
        this.mRealtimeStart = realtime;
        this.mUptimeStart = uptime;
    }

    void initDischarge() {
        this.mLowDischargeAmountSinceCharge = 0;
        this.mHighDischargeAmountSinceCharge = 0;
        this.mDischargeAmountScreenOn = 0;
        this.mDischargeAmountScreenOnSinceCharge = 0;
        this.mDischargeAmountScreenOff = 0;
        this.mDischargeAmountScreenOffSinceCharge = 0;
        this.mDischargeStepTracker.init();
        this.mChargeStepTracker.init();
    }

    public void resetAllStatsCmdLocked() {
        resetAllStatsLocked();
        long mSecUptime = SystemClock.uptimeMillis();
        long uptime = mSecUptime * 1000;
        long mSecRealtime = SystemClock.elapsedRealtime();
        long realtime = mSecRealtime * 1000;
        this.mDischargeStartLevel = this.mHistoryCur.batteryLevel;
        pullPendingStateUpdatesLocked();
        addHistoryRecordLocked(mSecRealtime, mSecUptime);
        byte b = this.mHistoryCur.batteryLevel;
        this.mCurrentBatteryLevel = b;
        this.mDischargePlugLevel = b;
        this.mDischargeUnplugLevel = b;
        this.mDischargeCurrentLevel = b;
        this.mOnBatteryTimeBase.reset(uptime, realtime);
        this.mOnBatteryScreenOffTimeBase.reset(uptime, realtime);
        if ((this.mHistoryCur.states & 524288) == 0) {
            if (this.mScreenState == 2) {
                this.mDischargeScreenOnUnplugLevel = this.mHistoryCur.batteryLevel;
                this.mDischargeScreenOffUnplugLevel = 0;
            } else {
                this.mDischargeScreenOnUnplugLevel = 0;
                this.mDischargeScreenOffUnplugLevel = this.mHistoryCur.batteryLevel;
            }
            this.mDischargeAmountScreenOn = 0;
            this.mDischargeAmountScreenOff = 0;
        }
        initActiveHistoryEventsLocked(mSecRealtime, mSecUptime);
    }

    private void resetAllStatsLocked() {
        int i;
        this.mStartCount = 0;
        initTimes(SystemClock.uptimeMillis() * 1000, SystemClock.elapsedRealtime() * 1000);
        this.mScreenOnTimer.reset(false);
        for (i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i].reset(false);
        }
        this.mInteractiveTimer.reset(false);
        this.mPowerSaveModeEnabledTimer.reset(false);
        this.mDeviceIdleModeEnabledTimer.reset(false);
        this.mDeviceIdlingTimer.reset(false);
        this.mPhoneOnTimer.reset(false);
        this.mAudioOnTimer.reset(false);
        this.mVideoOnTimer.reset(false);
        this.mFlashlightOnTimer.reset(false);
        this.mCameraOnTimer.reset(false);
        for (i = 0; i < SignalStrength.NUM_SIGNAL_STRENGTH_BINS; i++) {
            this.mPhoneSignalStrengthsTimer[i].reset(false);
        }
        this.mPhoneSignalScanningTimer.reset(false);
        for (i = 0; i < 17; i++) {
            this.mPhoneDataConnectionsTimer[i].reset(false);
        }
        for (i = 0; i < 4; i++) {
            this.mNetworkByteActivityCounters[i].reset(false);
            this.mNetworkPacketActivityCounters[i].reset(false);
        }
        this.mMobileRadioActiveTimer.reset(false);
        this.mMobileRadioActivePerAppTimer.reset(false);
        this.mMobileRadioActiveAdjustedTime.reset(false);
        this.mMobileRadioActiveUnknownTime.reset(false);
        this.mMobileRadioActiveUnknownCount.reset(false);
        this.mWifiOnTimer.reset(false);
        this.mGlobalWifiRunningTimer.reset(false);
        for (i = 0; i < 8; i++) {
            this.mWifiStateTimer[i].reset(false);
        }
        for (i = 0; i < 13; i++) {
            this.mWifiSupplStateTimer[i].reset(false);
        }
        for (i = 0; i < 5; i++) {
            this.mWifiSignalStrengthsTimer[i].reset(false);
        }
        for (i = 0; i < 4; i++) {
            this.mBluetoothActivityCounters[i].reset(false);
            this.mWifiActivityCounters[i].reset(false);
        }
        this.mUnpluggedNumConnectivityChange = 0;
        this.mLoadedNumConnectivityChange = 0;
        this.mNumConnectivityChange = 0;
        i = 0;
        while (i < this.mUidStats.size()) {
            if (((Uid) this.mUidStats.valueAt(i)).reset()) {
                this.mUidStats.remove(this.mUidStats.keyAt(i));
                i--;
            }
            i++;
        }
        if (this.mKernelWakelockStats.size() > 0) {
            for (SamplingTimer timer : this.mKernelWakelockStats.values()) {
                this.mOnBatteryScreenOffTimeBase.remove(timer);
            }
            this.mKernelWakelockStats.clear();
        }
        if (this.mWakeupReasonStats.size() > 0) {
            for (SamplingTimer timer2 : this.mWakeupReasonStats.values()) {
                this.mOnBatteryTimeBase.remove(timer2);
            }
            this.mWakeupReasonStats.clear();
        }
        this.mLastHistoryStepDetails = null;
        this.mLastStepCpuSystemTime = 0;
        this.mLastStepCpuUserTime = 0;
        this.mCurStepCpuSystemTime = 0;
        this.mCurStepCpuUserTime = 0;
        this.mCurStepCpuUserTime = 0;
        this.mLastStepCpuUserTime = 0;
        this.mCurStepCpuSystemTime = 0;
        this.mLastStepCpuSystemTime = 0;
        this.mCurStepStatUserTime = 0;
        this.mLastStepStatUserTime = 0;
        this.mCurStepStatSystemTime = 0;
        this.mLastStepStatSystemTime = 0;
        this.mCurStepStatIOWaitTime = 0;
        this.mLastStepStatIOWaitTime = 0;
        this.mCurStepStatIrqTime = 0;
        this.mLastStepStatIrqTime = 0;
        this.mCurStepStatSoftIrqTime = 0;
        this.mLastStepStatSoftIrqTime = 0;
        this.mCurStepStatIdleTime = 0;
        this.mLastStepStatIdleTime = 0;
        initDischarge();
        clearHistoryLocked();
    }

    private void initActiveHistoryEventsLocked(long elapsedRealtimeMs, long uptimeMs) {
        int i = 0;
        while (i < 19) {
            if (this.mRecordAllHistory || i != 1) {
                HashMap<String, SparseIntArray> active = this.mActiveEvents.getStateForEvent(i);
                if (active != null) {
                    for (Map.Entry<String, SparseIntArray> ent : active.entrySet()) {
                        SparseIntArray uids = (SparseIntArray) ent.getValue();
                        for (int j = 0; j < uids.size(); j++) {
                            addHistoryEventLocked(elapsedRealtimeMs, uptimeMs, i, (String) ent.getKey(), uids.keyAt(j));
                        }
                    }
                }
            }
            i++;
        }
    }

    void updateDischargeScreenLevelsLocked(boolean oldScreenOn, boolean newScreenOn) {
        int diff;
        if (oldScreenOn) {
            diff = this.mDischargeScreenOnUnplugLevel - this.mDischargeCurrentLevel;
            if (diff > 0) {
                this.mDischargeAmountScreenOn += diff;
                this.mDischargeAmountScreenOnSinceCharge += diff;
            }
        } else {
            diff = this.mDischargeScreenOffUnplugLevel - this.mDischargeCurrentLevel;
            if (diff > 0) {
                this.mDischargeAmountScreenOff += diff;
                this.mDischargeAmountScreenOffSinceCharge += diff;
            }
        }
        if (newScreenOn) {
            this.mDischargeScreenOnUnplugLevel = this.mDischargeCurrentLevel;
            this.mDischargeScreenOffUnplugLevel = 0;
            return;
        }
        this.mDischargeScreenOnUnplugLevel = 0;
        this.mDischargeScreenOffUnplugLevel = this.mDischargeCurrentLevel;
    }

    public void pullPendingStateUpdatesLocked() {
        if (this.mOnBatteryInternal) {
            boolean screenOn = this.mScreenState == 2;
            updateDischargeScreenLevelsLocked(screenOn, screenOn);
        }
    }

    private NetworkStats getNetworkStatsDeltaLocked(String[] ifaces, NetworkStats[] networkStatsBuffer) throws IOException {
        if (!SystemProperties.getBoolean(NetworkManagementSocketTagger.PROP_QTAGUID_ENABLED, false)) {
            return null;
        }
        NetworkStats stats = this.mNetworkStatsFactory.readNetworkStatsDetail(-1, ifaces, 0, networkStatsBuffer[1]);
        networkStatsBuffer[2] = NetworkStats.subtract(stats, networkStatsBuffer[0], null, null, networkStatsBuffer[2]);
        networkStatsBuffer[1] = networkStatsBuffer[0];
        networkStatsBuffer[0] = stats;
        return networkStatsBuffer[2];
    }

    public void updateWifiStateLocked(WifiActivityEnergyInfo info) {
        long elapsedRealtimeMs = SystemClock.elapsedRealtime();
        NetworkStats delta = null;
        try {
            if (!ArrayUtils.isEmpty(this.mWifiIfaces)) {
                delta = getNetworkStatsDeltaLocked(this.mWifiIfaces, this.mWifiNetworkStats);
            }
            if (this.mOnBatteryInternal) {
                int i;
                SparseLongArray rxPackets = new SparseLongArray();
                SparseLongArray txPackets = new SparseLongArray();
                long totalTxPackets = 0;
                long totalRxPackets = 0;
                if (delta != null) {
                    int size = delta.size();
                    for (i = 0; i < size; i++) {
                        Entry entry = delta.getValues(i, this.mTmpNetworkStatsEntry);
                        if (!(entry.rxBytes == 0 || entry.txBytes == 0)) {
                            Uid u = getUidStatsLocked(mapUid(entry.uid));
                            u.noteNetworkActivityLocked(2, entry.rxBytes, entry.rxPackets);
                            u.noteNetworkActivityLocked(3, entry.txBytes, entry.txPackets);
                            rxPackets.put(u.getUid(), entry.rxPackets);
                            txPackets.put(u.getUid(), entry.txPackets);
                            totalRxPackets += entry.rxPackets;
                            totalTxPackets += entry.txPackets;
                            this.mNetworkByteActivityCounters[2].addCountLocked(entry.rxBytes);
                            this.mNetworkByteActivityCounters[3].addCountLocked(entry.txBytes);
                            this.mNetworkPacketActivityCounters[2].addCountLocked(entry.rxPackets);
                            this.mNetworkPacketActivityCounters[3].addCountLocked(entry.txPackets);
                        }
                    }
                }
                if (info != null) {
                    Uid uid;
                    this.mHasWifiEnergyReporting = true;
                    long txTimeMs = info.getControllerTxTimeMillis();
                    long rxTimeMs = info.getControllerRxTimeMillis();
                    long idleTimeMs = info.getControllerIdleTimeMillis();
                    long totalTimeMs = (txTimeMs + rxTimeMs) + idleTimeMs;
                    long leftOverRxTimeMs = rxTimeMs;
                    long leftOverTxTimeMs = txTimeMs;
                    long totalWifiLockTimeMs = 0;
                    long totalScanTimeMs = 0;
                    int uidStatsSize = this.mUidStats.size();
                    for (i = 0; i < uidStatsSize; i++) {
                        uid = (Uid) this.mUidStats.valueAt(i);
                        totalScanTimeMs += uid.mWifiScanTimer.getTimeSinceMarkLocked(1000 * elapsedRealtimeMs) / 1000;
                        totalWifiLockTimeMs += uid.mFullWifiLockTimer.getTimeSinceMarkLocked(1000 * elapsedRealtimeMs) / 1000;
                    }
                    for (i = 0; i < uidStatsSize; i++) {
                        uid = (Uid) this.mUidStats.valueAt(i);
                        long scanTimeSinceMarkMs = uid.mWifiScanTimer.getTimeSinceMarkLocked(1000 * elapsedRealtimeMs) / 1000;
                        if (scanTimeSinceMarkMs > 0) {
                            uid.mWifiScanTimer.setMark(elapsedRealtimeMs);
                            long scanRxTimeSinceMarkMs = scanTimeSinceMarkMs;
                            long scanTxTimeSinceMarkMs = scanTimeSinceMarkMs;
                            if (totalScanTimeMs > rxTimeMs) {
                                scanRxTimeSinceMarkMs = (rxTimeMs * scanRxTimeSinceMarkMs) / totalScanTimeMs;
                            }
                            if (totalScanTimeMs > txTimeMs) {
                                scanTxTimeSinceMarkMs = (txTimeMs * scanTxTimeSinceMarkMs) / totalScanTimeMs;
                            }
                            uid.noteWifiControllerActivityLocked(1, scanRxTimeSinceMarkMs);
                            uid.noteWifiControllerActivityLocked(2, scanTxTimeSinceMarkMs);
                            leftOverRxTimeMs -= scanRxTimeSinceMarkMs;
                            leftOverTxTimeMs -= scanTxTimeSinceMarkMs;
                        }
                        long wifiLockTimeSinceMarkMs = uid.mFullWifiLockTimer.getTimeSinceMarkLocked(1000 * elapsedRealtimeMs) / 1000;
                        if (wifiLockTimeSinceMarkMs > 0) {
                            uid.mFullWifiLockTimer.setMark(elapsedRealtimeMs);
                            uid.noteWifiControllerActivityLocked(0, (wifiLockTimeSinceMarkMs * idleTimeMs) / totalWifiLockTimeMs);
                        }
                    }
                    for (i = 0; i < txPackets.size(); i++) {
                        getUidStatsLocked(txPackets.keyAt(i)).noteWifiControllerActivityLocked(2, (txPackets.valueAt(i) * leftOverTxTimeMs) / totalTxPackets);
                    }
                    for (i = 0; i < rxPackets.size(); i++) {
                        getUidStatsLocked(rxPackets.keyAt(i)).noteWifiControllerActivityLocked(1, (rxPackets.valueAt(i) * leftOverRxTimeMs) / totalRxPackets);
                    }
                    this.mWifiActivityCounters[1].addCountLocked(info.getControllerRxTimeMillis());
                    this.mWifiActivityCounters[2].addCountLocked(info.getControllerTxTimeMillis());
                    this.mWifiActivityCounters[0].addCountLocked(info.getControllerIdleTimeMillis());
                    double opVolt = this.mPowerProfile.getAveragePower(PowerProfile.POWER_WIFI_CONTROLLER_OPERATING_VOLTAGE) / 1000.0d;
                    if (opVolt != 0.0d) {
                        this.mWifiActivityCounters[3].addCountLocked((long) (((double) info.getControllerEnergyUsed()) / opVolt));
                    }
                }
            }
        } catch (IOException e) {
            Slog.wtf(TAG, "Failed to get wifi network stats", e);
        }
    }

    public void updateMobileRadioStateLocked(long elapsedRealtimeMs) {
        NetworkStats delta = null;
        try {
            if (!ArrayUtils.isEmpty(this.mMobileIfaces)) {
                delta = getNetworkStatsDeltaLocked(this.mMobileIfaces, this.mMobileNetworkStats);
            }
            if (delta != null && this.mOnBatteryInternal) {
                long radioTime = this.mMobileRadioActivePerAppTimer.getTimeSinceMarkLocked(1000 * elapsedRealtimeMs);
                this.mMobileRadioActivePerAppTimer.setMark(elapsedRealtimeMs);
                long totalPackets = delta.getTotalPackets();
                int size = delta.size();
                for (int i = 0; i < size; i++) {
                    Entry entry = delta.getValues(i, this.mTmpNetworkStatsEntry);
                    if (!(entry.rxBytes == 0 || entry.txBytes == 0)) {
                        Uid u = getUidStatsLocked(mapUid(entry.uid));
                        u.noteNetworkActivityLocked(0, entry.rxBytes, entry.rxPackets);
                        u.noteNetworkActivityLocked(1, entry.txBytes, entry.txPackets);
                        if (radioTime > 0) {
                            long appPackets = entry.rxPackets + entry.txPackets;
                            long appRadioTime = (radioTime * appPackets) / totalPackets;
                            u.noteMobileRadioActiveTimeLocked(appRadioTime);
                            radioTime -= appRadioTime;
                            totalPackets -= appPackets;
                        }
                        this.mNetworkByteActivityCounters[0].addCountLocked(entry.rxBytes);
                        this.mNetworkByteActivityCounters[1].addCountLocked(entry.txBytes);
                        this.mNetworkPacketActivityCounters[0].addCountLocked(entry.rxPackets);
                        this.mNetworkPacketActivityCounters[1].addCountLocked(entry.txPackets);
                    }
                }
                if (radioTime > 0) {
                    this.mMobileRadioActiveUnknownTime.addCountLocked(radioTime);
                    this.mMobileRadioActiveUnknownCount.addCountLocked(1);
                }
            }
        } catch (IOException e) {
            Slog.wtf(TAG, "Failed to get mobile network stats", e);
        }
    }

    public void updateBluetoothStateLocked(BluetoothActivityEnergyInfo info) {
        if (info != null && this.mOnBatteryInternal) {
            this.mHasBluetoothEnergyReporting = true;
            this.mBluetoothActivityCounters[1].addCountLocked(info.getControllerRxTimeMillis());
            this.mBluetoothActivityCounters[2].addCountLocked(info.getControllerTxTimeMillis());
            this.mBluetoothActivityCounters[0].addCountLocked(info.getControllerIdleTimeMillis());
            double opVolt = this.mPowerProfile.getAveragePower(PowerProfile.POWER_BLUETOOTH_CONTROLLER_OPERATING_VOLTAGE) / 1000.0d;
            if (opVolt != 0.0d) {
                this.mBluetoothActivityCounters[3].addCountLocked((long) (((double) info.getControllerEnergyUsed()) / opVolt));
            }
        }
    }

    public void updateKernelWakelocksLocked() {
        KernelWakelockStats wakelockStats = this.mKernelWakelockReader.readKernelWakelockStats(this.mTmpWakelockStats);
        if (wakelockStats == null) {
            Slog.w(TAG, "Couldn't get kernel wake lock stats");
            return;
        }
        boolean seenNonZeroTime = false;
        for (Map.Entry<String, KernelWakelockStats.Entry> ent : wakelockStats.entrySet()) {
            String name = (String) ent.getKey();
            KernelWakelockStats.Entry kws = (KernelWakelockStats.Entry) ent.getValue();
            SamplingTimer kwlt = (SamplingTimer) this.mKernelWakelockStats.get(name);
            if (kwlt == null) {
                kwlt = new SamplingTimer(this.mOnBatteryScreenOffTimeBase, true);
                this.mKernelWakelockStats.put(name, kwlt);
            }
            kwlt.updateCurrentReportedCount(kws.mCount);
            kwlt.updateCurrentReportedTotalTime(kws.mTotalTime);
            kwlt.setUpdateVersion(kws.mVersion);
            if (kws.mVersion != wakelockStats.kernelWakelockVersion) {
                seenNonZeroTime |= kws.mTotalTime > 0 ? 1 : 0;
            }
        }
        int numWakelocksSetStale = 0;
        if (wakelockStats.size() != this.mKernelWakelockStats.size()) {
            for (Map.Entry<String, SamplingTimer> ent2 : this.mKernelWakelockStats.entrySet()) {
                SamplingTimer st = (SamplingTimer) ent2.getValue();
                if (st.getUpdateVersion() != wakelockStats.kernelWakelockVersion) {
                    st.setStale();
                    numWakelocksSetStale++;
                }
            }
        }
        if (!seenNonZeroTime) {
            Slog.wtf(TAG, "All kernel wakelocks had time of zero");
        }
        if (numWakelocksSetStale == this.mKernelWakelockStats.size()) {
            Slog.wtf(TAG, "All kernel wakelocks were set stale. new version=" + wakelockStats.kernelWakelockVersion);
        }
    }

    public void updateCpuTimeLocked() {
        if (this.mPowerProfile != null) {
            int i;
            StopwatchTimer timer;
            AnonymousClass2 anonymousClass2;
            final long[][] clusterSpeeds = new long[this.mKernelCpuSpeedReaders.length][];
            for (int cluster = 0; cluster < this.mKernelCpuSpeedReaders.length; cluster++) {
                clusterSpeeds[cluster] = this.mKernelCpuSpeedReaders[cluster].readDelta();
            }
            int numWakelocks = 0;
            int numPartialTimers = this.mPartialTimers.size();
            if (this.mOnBatteryScreenOffTimeBase.isRunning()) {
                for (i = 0; i < numPartialTimers; i++) {
                    timer = (StopwatchTimer) this.mPartialTimers.get(i);
                    if (!(!timer.mInList || timer.mUid == null || timer.mUid.mUid == 1000)) {
                        numWakelocks++;
                    }
                }
            }
            final int numWakelocksF = numWakelocks;
            this.mTempTotalCpuUserTimeUs = 0;
            this.mTempTotalCpuSystemTimeUs = 0;
            long startTimeMs = SystemClock.elapsedRealtime();
            KernelUidCpuTimeReader kernelUidCpuTimeReader = this.mKernelUidCpuTimeReader;
            if (this.mOnBatteryInternal) {
                AnonymousClass2 anonymousClass22 = new Callback() {
                    public void onUidCpuTime(int uid, long userTimeUs, long systemTimeUs, long powerMaUs) {
                        Uid u = BatteryStatsImpl.this.getUidStatsLocked(BatteryStatsImpl.this.mapUid(uid));
                        BatteryStatsImpl batteryStatsImpl = BatteryStatsImpl.this;
                        batteryStatsImpl.mTempTotalCpuUserTimeUs += userTimeUs;
                        batteryStatsImpl = BatteryStatsImpl.this;
                        batteryStatsImpl.mTempTotalCpuSystemTimeUs += systemTimeUs;
                        StringBuilder sb = null;
                        if (numWakelocksF > 0) {
                            userTimeUs = (50 * userTimeUs) / 100;
                            systemTimeUs = (50 * systemTimeUs) / 100;
                        }
                        if (sb != null) {
                            sb.append("  adding to uid=").append(u.mUid).append(": u=");
                            TimeUtils.formatDuration(userTimeUs / 1000, sb);
                            sb.append(" s=");
                            TimeUtils.formatDuration(systemTimeUs / 1000, sb);
                            sb.append(" p=").append(powerMaUs / 1000).append("mAms");
                            Slog.d(BatteryStatsImpl.TAG, sb.toString());
                        }
                        u.mUserCpuTime.addCountLocked(userTimeUs);
                        u.mSystemCpuTime.addCountLocked(systemTimeUs);
                        u.mCpuPower.addCountLocked(powerMaUs);
                        int numClusters = BatteryStatsImpl.this.mPowerProfile.getNumCpuClusters();
                        if (u.mCpuClusterSpeed == null || u.mCpuClusterSpeed.length != numClusters) {
                            u.mCpuClusterSpeed = new LongSamplingCounter[numClusters][];
                        }
                        int cluster = 0;
                        while (cluster < clusterSpeeds.length) {
                            int speedsInCluster = BatteryStatsImpl.this.mPowerProfile.getNumSpeedStepsInCpuCluster(cluster);
                            if (u.mCpuClusterSpeed[cluster] == null || speedsInCluster != u.mCpuClusterSpeed[cluster].length) {
                                u.mCpuClusterSpeed[cluster] = new LongSamplingCounter[speedsInCluster];
                            }
                            LongSamplingCounter[] cpuSpeeds = u.mCpuClusterSpeed[cluster];
                            for (int speed = 0; speed < clusterSpeeds[cluster].length; speed++) {
                                if (cpuSpeeds[speed] == null) {
                                    cpuSpeeds[speed] = new LongSamplingCounter(BatteryStatsImpl.this.mOnBatteryTimeBase);
                                }
                                cpuSpeeds[speed].addCountLocked(clusterSpeeds[cluster][speed]);
                            }
                            cluster++;
                        }
                    }
                };
            } else {
                anonymousClass2 = null;
            }
            kernelUidCpuTimeReader.readDelta(anonymousClass2);
            if (this.mOnBatteryInternal && numWakelocks > 0) {
                this.mTempTotalCpuUserTimeUs = (this.mTempTotalCpuUserTimeUs * 50) / 100;
                this.mTempTotalCpuSystemTimeUs = (this.mTempTotalCpuSystemTimeUs * 50) / 100;
                for (i = 0; i < numPartialTimers; i++) {
                    timer = (StopwatchTimer) this.mPartialTimers.get(i);
                    if (!(!timer.mInList || timer.mUid == null || timer.mUid.mUid == 1000)) {
                        int userTimeUs = (int) (this.mTempTotalCpuUserTimeUs / ((long) numWakelocks));
                        int systemTimeUs = (int) (this.mTempTotalCpuSystemTimeUs / ((long) numWakelocks));
                        timer.mUid.mUserCpuTime.addCountLocked((long) userTimeUs);
                        timer.mUid.mSystemCpuTime.addCountLocked((long) systemTimeUs);
                        timer.mUid.getProcessStatsLocked("*wakelock*").addCpuTimeLocked(userTimeUs / 1000, systemTimeUs / 1000);
                        this.mTempTotalCpuUserTimeUs -= (long) userTimeUs;
                        this.mTempTotalCpuSystemTimeUs -= (long) systemTimeUs;
                        numWakelocks--;
                    }
                }
                if (this.mTempTotalCpuUserTimeUs > 0 || this.mTempTotalCpuSystemTimeUs > 0) {
                    Uid u = getUidStatsLocked(1000);
                    u.mUserCpuTime.addCountLocked(this.mTempTotalCpuUserTimeUs);
                    u.mSystemCpuTime.addCountLocked(this.mTempTotalCpuSystemTimeUs);
                    u.getProcessStatsLocked("*lost*").addCpuTimeLocked(((int) this.mTempTotalCpuUserTimeUs) / 1000, ((int) this.mTempTotalCpuSystemTimeUs) / 1000);
                }
            }
            if (ArrayUtils.referenceEquals(this.mPartialTimers, this.mLastPartialTimers)) {
                for (i = 0; i < numPartialTimers; i++) {
                    ((StopwatchTimer) this.mPartialTimers.get(i)).mInList = true;
                }
                return;
            }
            int numLastPartialTimers = this.mLastPartialTimers.size();
            for (i = 0; i < numLastPartialTimers; i++) {
                ((StopwatchTimer) this.mLastPartialTimers.get(i)).mInList = false;
            }
            this.mLastPartialTimers.clear();
            for (i = 0; i < numPartialTimers; i++) {
                timer = (StopwatchTimer) this.mPartialTimers.get(i);
                timer.mInList = true;
                this.mLastPartialTimers.add(timer);
            }
        }
    }

    boolean setChargingLocked(boolean charging) {
        if (this.mCharging == charging) {
            return false;
        }
        this.mCharging = charging;
        HistoryItem historyItem;
        if (charging) {
            historyItem = this.mHistoryCur;
            historyItem.states2 |= 33554432;
        } else {
            historyItem = this.mHistoryCur;
            historyItem.states2 &= -33554433;
        }
        this.mHandler.sendEmptyMessage(3);
        return true;
    }

    void setOnBatteryLocked(long mSecRealtime, long mSecUptime, boolean onBattery, int oldStatus, int level) {
        boolean doWrite = false;
        Message m = this.mHandler.obtainMessage(2);
        m.arg1 = onBattery ? 1 : 0;
        this.mHandler.sendMessage(m);
        long uptime = mSecUptime * 1000;
        long realtime = mSecRealtime * 1000;
        boolean screenOn = this.mScreenState == 2;
        HistoryItem historyItem;
        if (onBattery) {
            boolean z;
            boolean reset = false;
            if (!this.mNoAutoReset && (oldStatus == 5 || level >= 90 || ((this.mDischargeCurrentLevel < 20 && level >= 80) || (getHighDischargeAmountSinceCharge() >= 200 && this.mHistoryBuffer.dataSize() >= 262144)))) {
                Slog.i(TAG, "Resetting battery stats: level=" + level + " status=" + oldStatus + " dischargeLevel=" + this.mDischargeCurrentLevel + " lowAmount=" + getLowDischargeAmountSinceCharge() + " highAmount=" + getHighDischargeAmountSinceCharge());
                if (getLowDischargeAmountSinceCharge() >= 20) {
                    Parcel parcel = Parcel.obtain();
                    writeSummaryToParcel(parcel, true);
                    final Parcel parcel2 = parcel;
                    BackgroundThread.getHandler().post(new Runnable() {
                        public void run() {
                            synchronized (BatteryStatsImpl.this.mCheckinFile) {
                                FileOutputStream stream = null;
                                try {
                                    stream = BatteryStatsImpl.this.mCheckinFile.startWrite();
                                    stream.write(parcel2.marshall());
                                    stream.flush();
                                    FileUtils.sync(stream);
                                    stream.close();
                                    BatteryStatsImpl.this.mCheckinFile.finishWrite(stream);
                                    parcel2.recycle();
                                } catch (IOException e) {
                                    Slog.w("BatteryStats", "Error writing checkin battery statistics", e);
                                    BatteryStatsImpl.this.mCheckinFile.failWrite(stream);
                                    parcel2.recycle();
                                } catch (Throwable th) {
                                    parcel2.recycle();
                                }
                            }
                        }
                    });
                }
                doWrite = true;
                resetAllStatsLocked();
                this.mDischargeStartLevel = level;
                reset = true;
                this.mDischargeStepTracker.init();
            }
            if (this.mCharging) {
                setChargingLocked(false);
            }
            this.mLastChargingStateLevel = level;
            this.mOnBatteryInternal = true;
            this.mOnBattery = true;
            this.mLastDischargeStepLevel = level;
            this.mMinDischargeStepLevel = level;
            this.mDischargeStepTracker.clearTime();
            this.mDailyDischargeStepTracker.clearTime();
            this.mInitStepMode = this.mCurStepMode;
            this.mModStepMode = 0;
            pullPendingStateUpdatesLocked();
            this.mHistoryCur.batteryLevel = (byte) level;
            historyItem = this.mHistoryCur;
            historyItem.states &= -524289;
            if (reset) {
                this.mRecordingHistory = true;
                startRecordingHistory(mSecRealtime, mSecUptime, reset);
            }
            addHistoryRecordLocked(mSecRealtime, mSecUptime);
            this.mDischargeUnplugLevel = level;
            this.mDischargeCurrentLevel = level;
            if (screenOn) {
                this.mDischargeScreenOnUnplugLevel = level;
                this.mDischargeScreenOffUnplugLevel = 0;
            } else {
                this.mDischargeScreenOnUnplugLevel = 0;
                this.mDischargeScreenOffUnplugLevel = level;
            }
            this.mDischargeAmountScreenOn = 0;
            this.mDischargeAmountScreenOff = 0;
            if (screenOn) {
                z = false;
            } else {
                z = true;
            }
            updateTimeBasesLocked(true, z, uptime, realtime);
        } else {
            this.mLastChargingStateLevel = level;
            this.mOnBatteryInternal = false;
            this.mOnBattery = false;
            pullPendingStateUpdatesLocked();
            this.mHistoryCur.batteryLevel = (byte) level;
            historyItem = this.mHistoryCur;
            historyItem.states |= 524288;
            addHistoryRecordLocked(mSecRealtime, mSecUptime);
            this.mDischargePlugLevel = level;
            this.mDischargeCurrentLevel = level;
            if (level < this.mDischargeUnplugLevel) {
                this.mLowDischargeAmountSinceCharge += (this.mDischargeUnplugLevel - level) - 1;
                this.mHighDischargeAmountSinceCharge += this.mDischargeUnplugLevel - level;
            }
            updateDischargeScreenLevelsLocked(screenOn, screenOn);
            updateTimeBasesLocked(false, !screenOn, uptime, realtime);
            this.mChargeStepTracker.init();
            this.mLastChargeStepLevel = level;
            this.mMaxChargeStepLevel = level;
            this.mInitStepMode = this.mCurStepMode;
            this.mModStepMode = 0;
        }
        if ((doWrite || this.mLastWriteTime + DateUtils.MINUTE_IN_MILLIS < mSecRealtime) && this.mFile != null) {
            writeAsyncLocked();
        }
    }

    private void startRecordingHistory(long elapsedRealtimeMs, long uptimeMs, boolean reset) {
        this.mRecordingHistory = true;
        this.mHistoryCur.currentTime = System.currentTimeMillis();
        addHistoryBufferLocked(elapsedRealtimeMs, uptimeMs, reset ? (byte) 7 : (byte) 5, this.mHistoryCur);
        this.mHistoryCur.currentTime = 0;
        if (reset) {
            initActiveHistoryEventsLocked(elapsedRealtimeMs, uptimeMs);
        }
    }

    private void recordCurrentTimeChangeLocked(long currentTime, long elapsedRealtimeMs, long uptimeMs) {
        if (this.mRecordingHistory) {
            this.mHistoryCur.currentTime = currentTime;
            addHistoryBufferLocked(elapsedRealtimeMs, uptimeMs, (byte) 5, this.mHistoryCur);
            this.mHistoryCur.currentTime = 0;
        }
    }

    private void recordShutdownLocked(long elapsedRealtimeMs, long uptimeMs) {
        if (this.mRecordingHistory) {
            this.mHistoryCur.currentTime = System.currentTimeMillis();
            addHistoryBufferLocked(elapsedRealtimeMs, uptimeMs, (byte) 8, this.mHistoryCur);
            this.mHistoryCur.currentTime = 0;
        }
    }

    private void scheduleSyncExternalStatsLocked(String reason) {
        if (this.mExternalSync != null) {
            this.mExternalSync.scheduleSync(reason);
        }
    }

    private void scheduleSyncExternalWifiStatsLocked(String reason) {
        if (this.mExternalSync != null) {
            this.mExternalSync.scheduleWifiSync(reason);
        }
    }

    public void setBatteryStateLocked(int status, int health, int plugType, int level, int temp, int volt) {
        boolean onBattery = plugType == 0;
        long uptime = SystemClock.uptimeMillis();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (!this.mHaveBatteryLevel) {
            HistoryItem historyItem;
            this.mHaveBatteryLevel = true;
            if (onBattery == this.mOnBattery) {
                if (onBattery) {
                    historyItem = this.mHistoryCur;
                    historyItem.states &= -524289;
                } else {
                    historyItem = this.mHistoryCur;
                    historyItem.states |= 524288;
                }
            }
            historyItem = this.mHistoryCur;
            historyItem.states2 |= 33554432;
            this.mHistoryCur.batteryStatus = (byte) status;
            this.mHistoryCur.batteryLevel = (byte) level;
            this.mLastDischargeStepLevel = level;
            this.mLastChargeStepLevel = level;
            this.mMinDischargeStepLevel = level;
            this.mMaxChargeStepLevel = level;
            this.mLastChargingStateLevel = level;
        } else if (!(this.mCurrentBatteryLevel == level && this.mOnBattery == onBattery)) {
            boolean z = level >= 100 && onBattery;
            recordDailyStatsIfNeededLocked(z);
        }
        int oldStatus = this.mHistoryCur.batteryStatus;
        if (onBattery) {
            this.mDischargeCurrentLevel = level;
            if (!this.mRecordingHistory) {
                this.mRecordingHistory = true;
                startRecordingHistory(elapsedRealtime, uptime, true);
            }
        } else if (level < 96 && !this.mRecordingHistory) {
            this.mRecordingHistory = true;
            startRecordingHistory(elapsedRealtime, uptime, true);
        }
        this.mCurrentBatteryLevel = level;
        if (this.mDischargePlugLevel < 0) {
            this.mDischargePlugLevel = level;
        }
        if (onBattery != this.mOnBattery) {
            this.mHistoryCur.batteryLevel = (byte) level;
            this.mHistoryCur.batteryStatus = (byte) status;
            this.mHistoryCur.batteryHealth = (byte) health;
            this.mHistoryCur.batteryPlugType = (byte) plugType;
            this.mHistoryCur.batteryTemperature = (short) temp;
            this.mHistoryCur.batteryVoltage = (char) volt;
            setOnBatteryLocked(elapsedRealtime, uptime, onBattery, oldStatus, level);
        } else {
            boolean changed = false;
            if (this.mHistoryCur.batteryLevel != level) {
                this.mHistoryCur.batteryLevel = (byte) level;
                changed = true;
                scheduleSyncExternalStatsLocked("battery-level");
            }
            if (this.mHistoryCur.batteryStatus != status) {
                this.mHistoryCur.batteryStatus = (byte) status;
                changed = true;
            }
            if (this.mHistoryCur.batteryHealth != health) {
                this.mHistoryCur.batteryHealth = (byte) health;
                changed = true;
            }
            if (this.mHistoryCur.batteryPlugType != plugType) {
                this.mHistoryCur.batteryPlugType = (byte) plugType;
                changed = true;
            }
            if (temp >= this.mHistoryCur.batteryTemperature + 10 || temp <= this.mHistoryCur.batteryTemperature - 10) {
                this.mHistoryCur.batteryTemperature = (short) temp;
                changed = true;
            }
            if (volt > this.mHistoryCur.batteryVoltage + 20 || volt < this.mHistoryCur.batteryVoltage - 20) {
                this.mHistoryCur.batteryVoltage = (char) volt;
                changed = true;
            }
            long modeBits = ((((long) this.mInitStepMode) << 48) | (((long) this.mModStepMode) << 56)) | (((long) (level & 255)) << 40);
            if (onBattery) {
                changed |= setChargingLocked(false);
                if (this.mLastDischargeStepLevel != level && this.mMinDischargeStepLevel > level) {
                    this.mDischargeStepTracker.addLevelSteps(this.mLastDischargeStepLevel - level, modeBits, elapsedRealtime);
                    this.mDailyDischargeStepTracker.addLevelSteps(this.mLastDischargeStepLevel - level, modeBits, elapsedRealtime);
                    this.mLastDischargeStepLevel = level;
                    this.mMinDischargeStepLevel = level;
                    this.mInitStepMode = this.mCurStepMode;
                    this.mModStepMode = 0;
                }
            } else {
                if (level >= 90) {
                    changed |= setChargingLocked(true);
                    this.mLastChargeStepLevel = level;
                }
                if (this.mCharging) {
                    if (this.mLastChargeStepLevel > level) {
                        changed |= setChargingLocked(false);
                        this.mLastChargeStepLevel = level;
                    }
                } else if (this.mLastChargeStepLevel < level) {
                    changed |= setChargingLocked(true);
                    this.mLastChargeStepLevel = level;
                }
                if (this.mLastChargeStepLevel != level && this.mMaxChargeStepLevel < level) {
                    this.mChargeStepTracker.addLevelSteps(level - this.mLastChargeStepLevel, modeBits, elapsedRealtime);
                    this.mDailyChargeStepTracker.addLevelSteps(level - this.mLastChargeStepLevel, modeBits, elapsedRealtime);
                    this.mLastChargeStepLevel = level;
                    this.mMaxChargeStepLevel = level;
                    this.mInitStepMode = this.mCurStepMode;
                    this.mModStepMode = 0;
                }
            }
            if (changed) {
                addHistoryRecordLocked(elapsedRealtime, uptime);
            }
        }
        if (!onBattery && status == 5) {
            this.mRecordingHistory = false;
        }
    }

    public long getAwakeTimeBattery() {
        return computeBatteryUptime(getBatteryUptimeLocked(), 1);
    }

    public long getAwakeTimePlugged() {
        return (SystemClock.uptimeMillis() * 1000) - getAwakeTimeBattery();
    }

    public long computeUptime(long curTime, int which) {
        switch (which) {
            case 0:
                return this.mUptime + (curTime - this.mUptimeStart);
            case 1:
                return curTime - this.mUptimeStart;
            case 2:
                return curTime - this.mOnBatteryTimeBase.getUptimeStart();
            default:
                return 0;
        }
    }

    public long computeRealtime(long curTime, int which) {
        switch (which) {
            case 0:
                return this.mRealtime + (curTime - this.mRealtimeStart);
            case 1:
                return curTime - this.mRealtimeStart;
            case 2:
                return curTime - this.mOnBatteryTimeBase.getRealtimeStart();
            default:
                return 0;
        }
    }

    public long computeBatteryUptime(long curTime, int which) {
        return this.mOnBatteryTimeBase.computeUptime(curTime, which);
    }

    public long computeBatteryRealtime(long curTime, int which) {
        return this.mOnBatteryTimeBase.computeRealtime(curTime, which);
    }

    public long computeBatteryScreenOffUptime(long curTime, int which) {
        return this.mOnBatteryScreenOffTimeBase.computeUptime(curTime, which);
    }

    public long computeBatteryScreenOffRealtime(long curTime, int which) {
        return this.mOnBatteryScreenOffTimeBase.computeRealtime(curTime, which);
    }

    private long computeTimePerLevel(long[] steps, int numSteps) {
        if (numSteps <= 0) {
            return -1;
        }
        long total = 0;
        for (int i = 0; i < numSteps; i++) {
            total += steps[i] & 1099511627775L;
        }
        return total / ((long) numSteps);
    }

    public long computeBatteryTimeRemaining(long curTime) {
        if (!this.mOnBattery || this.mDischargeStepTracker.mNumStepDurations < 1) {
            return -1;
        }
        long msPerLevel = this.mDischargeStepTracker.computeTimePerLevel();
        if (msPerLevel > 0) {
            return (((long) this.mCurrentBatteryLevel) * msPerLevel) * 1000;
        }
        return -1;
    }

    public LevelStepTracker getDischargeLevelStepTracker() {
        return this.mDischargeStepTracker;
    }

    public LevelStepTracker getDailyDischargeLevelStepTracker() {
        return this.mDailyDischargeStepTracker;
    }

    private long readOneLine(String filepath) {
        Throwable th;
        long time = -1;
        BufferedReader buf = null;
        if (this.mFeatureComputeChargeTimeModel) {
            try {
                BufferedReader buf2 = new BufferedReader(new FileReader(filepath), 10);
                if (buf2 != null) {
                    try {
                        String str = buf2.readLine();
                        if (str != null) {
                            time = Long.parseLong(str, 10);
                        }
                    } catch (IOException e) {
                        buf = buf2;
                        try {
                            this.mFeatureComputeChargeTimeModel = false;
                            if (buf != null) {
                                try {
                                    buf.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            return (time * 1000) * 1000;
                        } catch (Throwable th2) {
                            th = th2;
                            if (buf != null) {
                                try {
                                    buf.close();
                                } catch (IOException e22) {
                                    e22.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        buf = buf2;
                        if (buf != null) {
                            buf.close();
                        }
                        throw th;
                    }
                }
                if (buf2 != null) {
                    try {
                        buf2.close();
                        buf = buf2;
                    } catch (IOException e222) {
                        e222.printStackTrace();
                        buf = buf2;
                    }
                }
            } catch (IOException e3) {
                this.mFeatureComputeChargeTimeModel = false;
                if (buf != null) {
                    buf.close();
                }
                return (time * 1000) * 1000;
            }
            return (time * 1000) * 1000;
        }
        Slog.d(TAG, "This device is not supported own charge time.");
        return -1;
    }

    public long computeChargeTimeRemaining(long curTime) {
        long chargeTime = readOneLine("/sys/class/power_supply/battery/time_to_full_now");
        if (this.mFeatureComputeChargeTimeModel) {
            return chargeTime < 0 ? -1 : chargeTime;
        } else {
            if (this.mOnBattery) {
                return -1;
            }
            if (this.mChargeStepTracker.mNumStepDurations < 1) {
                return -1;
            }
            long msPerLevel = this.mChargeStepTracker.computeTimePerLevel();
            if (msPerLevel <= 0) {
                return -1;
            }
            return (((long) (100 - this.mCurrentBatteryLevel)) * msPerLevel) * 1000;
        }
    }

    public LevelStepTracker getChargeLevelStepTracker() {
        return this.mChargeStepTracker;
    }

    public LevelStepTracker getDailyChargeLevelStepTracker() {
        return this.mDailyChargeStepTracker;
    }

    public ArrayList<PackageChange> getDailyPackageChanges() {
        return this.mDailyPackageChanges;
    }

    long getBatteryUptimeLocked() {
        return this.mOnBatteryTimeBase.getUptime(SystemClock.uptimeMillis() * 1000);
    }

    public long getBatteryUptime(long curTime) {
        return this.mOnBatteryTimeBase.getUptime(curTime);
    }

    public long getBatteryRealtime(long curTime) {
        return this.mOnBatteryTimeBase.getRealtime(curTime);
    }

    public int getDischargeStartLevel() {
        int dischargeStartLevelLocked;
        synchronized (this) {
            dischargeStartLevelLocked = getDischargeStartLevelLocked();
        }
        return dischargeStartLevelLocked;
    }

    public int getDischargeStartLevelLocked() {
        return this.mDischargeUnplugLevel;
    }

    public int getDischargeCurrentLevel() {
        int dischargeCurrentLevelLocked;
        synchronized (this) {
            dischargeCurrentLevelLocked = getDischargeCurrentLevelLocked();
        }
        return dischargeCurrentLevelLocked;
    }

    public int getDischargeCurrentLevelLocked() {
        return this.mDischargeCurrentLevel;
    }

    public int getLowDischargeAmountSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mLowDischargeAmountSinceCharge;
            if (this.mOnBattery && this.mDischargeCurrentLevel < this.mDischargeUnplugLevel) {
                val += (this.mDischargeUnplugLevel - this.mDischargeCurrentLevel) - 1;
            }
        }
        return val;
    }

    public int getHighDischargeAmountSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mHighDischargeAmountSinceCharge;
            if (this.mOnBattery && this.mDischargeCurrentLevel < this.mDischargeUnplugLevel) {
                val += this.mDischargeUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    public int getDischargeAmount(int which) {
        int dischargeAmount = which == 0 ? getHighDischargeAmountSinceCharge() : getDischargeStartLevel() - getDischargeCurrentLevel();
        if (dischargeAmount < 0) {
            return 0;
        }
        return dischargeAmount;
    }

    public int getDischargeAmountScreenOn() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenOn;
            if (this.mOnBattery && this.mScreenState == 2 && this.mDischargeCurrentLevel < this.mDischargeScreenOnUnplugLevel) {
                val += this.mDischargeScreenOnUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    public int getDischargeAmountScreenOnSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenOnSinceCharge;
            if (this.mOnBattery && this.mScreenState == 2 && this.mDischargeCurrentLevel < this.mDischargeScreenOnUnplugLevel) {
                val += this.mDischargeScreenOnUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    public int getDischargeAmountScreenOff() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenOff;
            if (this.mOnBattery && this.mScreenState != 2 && this.mDischargeCurrentLevel < this.mDischargeScreenOffUnplugLevel) {
                val += this.mDischargeScreenOffUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    public int getDischargeAmountScreenOffSinceCharge() {
        int val;
        synchronized (this) {
            val = this.mDischargeAmountScreenOffSinceCharge;
            if (this.mOnBattery && this.mScreenState != 2 && this.mDischargeCurrentLevel < this.mDischargeScreenOffUnplugLevel) {
                val += this.mDischargeScreenOffUnplugLevel - this.mDischargeCurrentLevel;
            }
        }
        return val;
    }

    public Uid getUidStatsLocked(int uid) {
        Uid u = (Uid) this.mUidStats.get(uid);
        if (u != null) {
            return u;
        }
        u = new Uid(uid);
        this.mUidStats.put(uid, u);
        return u;
    }

    public void removeUidStatsLocked(int uid) {
        this.mKernelUidCpuTimeReader.removeUid(uid);
        this.mUidStats.remove(uid);
    }

    public Proc getProcessStatsLocked(int uid, String name) {
        return getUidStatsLocked(mapUid(uid)).getProcessStatsLocked(name);
    }

    public Pkg getPackageStatsLocked(int uid, String pkg) {
        return getUidStatsLocked(mapUid(uid)).getPackageStatsLocked(pkg);
    }

    public Serv getServiceStatsLocked(int uid, String pkg, String name) {
        return getUidStatsLocked(mapUid(uid)).getServiceStatsLocked(pkg, name);
    }

    public void shutdownLocked() {
        recordShutdownLocked(SystemClock.elapsedRealtime(), SystemClock.uptimeMillis());
        writeSyncLocked();
        this.mShuttingDown = true;
    }

    public void writeAsyncLocked() {
        writeLocked(false);
    }

    public void writeSyncLocked() {
        writeLocked(true);
    }

    void writeLocked(boolean sync) {
        if (this.mFile == null) {
            Slog.w("BatteryStats", "writeLocked: no file associated with this instance");
        } else if (!this.mShuttingDown) {
            Parcel out = Parcel.obtain();
            writeSummaryToParcel(out, true);
            this.mLastWriteTime = SystemClock.elapsedRealtime();
            if (this.mPendingWrite != null) {
                this.mPendingWrite.recycle();
            }
            this.mPendingWrite = out;
            if (sync) {
                commitPendingDataToDisk();
            } else {
                BackgroundThread.getHandler().post(new Runnable() {
                    public void run() {
                        BatteryStatsImpl.this.commitPendingDataToDisk();
                    }
                });
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void commitPendingDataToDisk() {
        /*
        r5 = this;
        monitor-enter(r5);
        r1 = r5.mPendingWrite;	 Catch:{ all -> 0x0039 }
        r3 = 0;
        r5.mPendingWrite = r3;	 Catch:{ all -> 0x0039 }
        if (r1 != 0) goto L_0x000a;
    L_0x0008:
        monitor-exit(r5);	 Catch:{ all -> 0x0039 }
    L_0x0009:
        return;
    L_0x000a:
        r3 = r5.mWriteLock;	 Catch:{ all -> 0x0039 }
        r3.lock();	 Catch:{ all -> 0x0039 }
        monitor-exit(r5);	 Catch:{ all -> 0x0039 }
        r2 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x003c }
        r3 = r5.mFile;	 Catch:{ IOException -> 0x003c }
        r3 = r3.chooseForWrite();	 Catch:{ IOException -> 0x003c }
        r2.<init>(r3);	 Catch:{ IOException -> 0x003c }
        r3 = r1.marshall();	 Catch:{ IOException -> 0x003c }
        r2.write(r3);	 Catch:{ IOException -> 0x003c }
        r2.flush();	 Catch:{ IOException -> 0x003c }
        android.os.FileUtils.sync(r2);	 Catch:{ IOException -> 0x003c }
        r2.close();	 Catch:{ IOException -> 0x003c }
        r3 = r5.mFile;	 Catch:{ IOException -> 0x003c }
        r3.commit();	 Catch:{ IOException -> 0x003c }
        r1.recycle();
        r3 = r5.mWriteLock;
        r3.unlock();
        goto L_0x0009;
    L_0x0039:
        r3 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0039 }
        throw r3;
    L_0x003c:
        r0 = move-exception;
        r3 = "BatteryStats";
        r4 = "Error writing battery statistics";
        android.util.Slog.w(r3, r4, r0);	 Catch:{ all -> 0x0052 }
        r3 = r5.mFile;	 Catch:{ all -> 0x0052 }
        r3.rollback();	 Catch:{ all -> 0x0052 }
        r1.recycle();
        r3 = r5.mWriteLock;
        r3.unlock();
        goto L_0x0009;
    L_0x0052:
        r3 = move-exception;
        r1.recycle();
        r4 = r5.mWriteLock;
        r4.unlock();
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BatteryStatsImpl.commitPendingDataToDisk():void");
    }

    public void readLocked() {
        if (this.mDailyFile != null) {
            readDailyStatsLocked();
        }
        if (this.mFile == null) {
            Slog.w("BatteryStats", "readLocked: no file associated with this instance");
            return;
        }
        this.mUidStats.clear();
        try {
            File file = this.mFile.chooseForRead();
            if (file.exists()) {
                FileInputStream stream = new FileInputStream(file);
                byte[] raw = BatteryStatsHelper.readFully(stream);
                Parcel in = Parcel.obtain();
                in.unmarshall(raw, 0, raw.length);
                in.setDataPosition(0);
                stream.close();
                readSummaryFromParcel(in);
                this.mEndPlatformVersion = Build.ID;
                if (this.mHistoryBuffer.dataPosition() > 0) {
                    this.mRecordingHistory = true;
                    long elapsedRealtime = SystemClock.elapsedRealtime();
                    long uptime = SystemClock.uptimeMillis();
                    addHistoryBufferLocked(elapsedRealtime, uptime, (byte) 4, this.mHistoryCur);
                    startRecordingHistory(elapsedRealtime, uptime, false);
                }
                recordDailyStatsIfNeededLocked(false);
            }
        } catch (Exception e) {
            Slog.e("BatteryStats", "Error reading battery statistics", e);
            resetAllStatsLocked();
        }
    }

    public int describeContents() {
        return 0;
    }

    void readHistory(Parcel in, boolean andOldHistory) throws ParcelFormatException {
        long historyBaseTime = in.readLong();
        this.mHistoryBuffer.setDataSize(0);
        this.mHistoryBuffer.setDataPosition(0);
        this.mHistoryTagPool.clear();
        this.mNextHistoryTagIdx = 0;
        this.mNumHistoryTagChars = 0;
        int numTags = in.readInt();
        for (int i = 0; i < numTags; i++) {
            int idx = in.readInt();
            String str = in.readString();
            if (str == null) {
                throw new ParcelFormatException("null history tag string");
            }
            int uid = in.readInt();
            HistoryTag tag = new HistoryTag();
            tag.string = str;
            tag.uid = uid;
            tag.poolIdx = idx;
            this.mHistoryTagPool.put(tag, Integer.valueOf(idx));
            if (idx >= this.mNextHistoryTagIdx) {
                this.mNextHistoryTagIdx = idx + 1;
            }
            this.mNumHistoryTagChars += tag.string.length() + 1;
        }
        int bufSize = in.readInt();
        int curPos = in.dataPosition();
        if (bufSize >= SurfaceControl.FX_SURFACE_MASK) {
            throw new ParcelFormatException("File corrupt: history data buffer too large " + bufSize);
        } else if ((bufSize & -4) != bufSize) {
            throw new ParcelFormatException("File corrupt: history data buffer not aligned " + bufSize);
        } else {
            this.mHistoryBuffer.appendFrom(in, curPos, bufSize);
            in.setDataPosition(curPos + bufSize);
            if (andOldHistory) {
                readOldHistory(in);
            }
            this.mHistoryBaseTime = historyBaseTime;
            if (this.mHistoryBaseTime > 0) {
                this.mHistoryBaseTime = (this.mHistoryBaseTime - SystemClock.elapsedRealtime()) + 1;
            }
        }
    }

    void readOldHistory(Parcel in) {
    }

    void writeHistory(Parcel out, boolean inclData, boolean andOldHistory) {
        out.writeLong(this.mHistoryBaseTime + this.mLastHistoryElapsedRealtime);
        if (inclData) {
            out.writeInt(this.mHistoryTagPool.size());
            for (Map.Entry<HistoryTag, Integer> ent : this.mHistoryTagPool.entrySet()) {
                HistoryTag tag = (HistoryTag) ent.getKey();
                out.writeInt(((Integer) ent.getValue()).intValue());
                out.writeString(tag.string);
                out.writeInt(tag.uid);
            }
            out.writeInt(this.mHistoryBuffer.dataSize());
            out.appendFrom(this.mHistoryBuffer, 0, this.mHistoryBuffer.dataSize());
            if (andOldHistory) {
                writeOldHistory(out);
                return;
            }
            return;
        }
        out.writeInt(0);
        out.writeInt(0);
    }

    void writeOldHistory(Parcel out) {
    }

    public void readSummaryFromParcel(Parcel in) throws ParcelFormatException {
        int version = in.readInt();
        if (version != 132) {
            Slog.w("BatteryStats", "readFromParcel: version got " + version + ", expected " + 132 + "; erasing old stats");
            return;
        }
        int i;
        readHistory(in, true);
        this.mStartCount = in.readInt();
        this.mUptime = in.readLong();
        this.mRealtime = in.readLong();
        this.mStartClockTime = in.readLong();
        this.mStartPlatformVersion = in.readString();
        this.mEndPlatformVersion = in.readString();
        this.mOnBatteryTimeBase.readSummaryFromParcel(in);
        this.mOnBatteryScreenOffTimeBase.readSummaryFromParcel(in);
        this.mDischargeUnplugLevel = in.readInt();
        this.mDischargePlugLevel = in.readInt();
        this.mDischargeCurrentLevel = in.readInt();
        this.mCurrentBatteryLevel = in.readInt();
        this.mLowDischargeAmountSinceCharge = in.readInt();
        this.mHighDischargeAmountSinceCharge = in.readInt();
        this.mDischargeAmountScreenOnSinceCharge = in.readInt();
        this.mDischargeAmountScreenOffSinceCharge = in.readInt();
        this.mDischargeStepTracker.readFromParcel(in);
        this.mChargeStepTracker.readFromParcel(in);
        this.mDailyDischargeStepTracker.readFromParcel(in);
        this.mDailyChargeStepTracker.readFromParcel(in);
        int NPKG = in.readInt();
        if (NPKG > 0) {
            this.mDailyPackageChanges = new ArrayList(NPKG);
            while (NPKG > 0) {
                NPKG--;
                PackageChange pc = new PackageChange();
                pc.mPackageName = in.readString();
                pc.mUpdate = in.readInt() != 0;
                pc.mVersionCode = in.readInt();
                this.mDailyPackageChanges.add(pc);
            }
        } else {
            this.mDailyPackageChanges = null;
        }
        this.mDailyStartTime = in.readLong();
        this.mNextMinDailyDeadline = in.readLong();
        this.mNextMaxDailyDeadline = in.readLong();
        this.mStartCount++;
        this.mScreenState = 0;
        this.mScreenOnTimer.readSummaryFromParcelLocked(in);
        for (i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i].readSummaryFromParcelLocked(in);
        }
        this.mInteractive = false;
        this.mInteractiveTimer.readSummaryFromParcelLocked(in);
        this.mPhoneOn = false;
        this.mPowerSaveModeEnabledTimer.readSummaryFromParcelLocked(in);
        this.mDeviceIdleModeEnabledTimer.readSummaryFromParcelLocked(in);
        this.mDeviceIdlingTimer.readSummaryFromParcelLocked(in);
        this.mPhoneOnTimer.readSummaryFromParcelLocked(in);
        for (i = 0; i < SignalStrength.NUM_SIGNAL_STRENGTH_BINS; i++) {
            this.mPhoneSignalStrengthsTimer[i].readSummaryFromParcelLocked(in);
        }
        this.mPhoneSignalScanningTimer.readSummaryFromParcelLocked(in);
        for (i = 0; i < 17; i++) {
            this.mPhoneDataConnectionsTimer[i].readSummaryFromParcelLocked(in);
        }
        for (i = 0; i < 4; i++) {
            this.mNetworkByteActivityCounters[i].readSummaryFromParcelLocked(in);
            this.mNetworkPacketActivityCounters[i].readSummaryFromParcelLocked(in);
        }
        this.mMobileRadioPowerState = DataConnectionRealTimeInfo.DC_POWER_STATE_LOW;
        this.mMobileRadioActiveTimer.readSummaryFromParcelLocked(in);
        this.mMobileRadioActivePerAppTimer.readSummaryFromParcelLocked(in);
        this.mMobileRadioActiveAdjustedTime.readSummaryFromParcelLocked(in);
        this.mMobileRadioActiveUnknownTime.readSummaryFromParcelLocked(in);
        this.mMobileRadioActiveUnknownCount.readSummaryFromParcelLocked(in);
        this.mWifiRadioPowerState = DataConnectionRealTimeInfo.DC_POWER_STATE_LOW;
        this.mWifiOn = false;
        this.mWifiOnTimer.readSummaryFromParcelLocked(in);
        this.mGlobalWifiRunning = false;
        this.mGlobalWifiRunningTimer.readSummaryFromParcelLocked(in);
        for (i = 0; i < 8; i++) {
            this.mWifiStateTimer[i].readSummaryFromParcelLocked(in);
        }
        for (i = 0; i < 13; i++) {
            this.mWifiSupplStateTimer[i].readSummaryFromParcelLocked(in);
        }
        for (i = 0; i < 5; i++) {
            this.mWifiSignalStrengthsTimer[i].readSummaryFromParcelLocked(in);
        }
        for (i = 0; i < 4; i++) {
            this.mBluetoothActivityCounters[i].readSummaryFromParcelLocked(in);
        }
        for (i = 0; i < 4; i++) {
            this.mWifiActivityCounters[i].readSummaryFromParcelLocked(in);
        }
        int readInt = in.readInt();
        this.mLoadedNumConnectivityChange = readInt;
        this.mNumConnectivityChange = readInt;
        this.mFlashlightOnNesting = 0;
        this.mFlashlightOnTimer.readSummaryFromParcelLocked(in);
        this.mCameraOnNesting = 0;
        this.mCameraOnTimer.readSummaryFromParcelLocked(in);
        int NKW = in.readInt();
        if (NKW > 10000) {
            throw new ParcelFormatException("File corrupt: too many kernel wake locks " + NKW);
        }
        for (int ikw = 0; ikw < NKW; ikw++) {
            if (in.readInt() != 0) {
                getKernelWakelockTimerLocked(in.readString()).readSummaryFromParcelLocked(in);
            }
        }
        int NWR = in.readInt();
        if (NWR > 10000) {
            throw new ParcelFormatException("File corrupt: too many wakeup reasons " + NWR);
        }
        for (int iwr = 0; iwr < NWR; iwr++) {
            if (in.readInt() != 0) {
                getWakeupReasonTimerLocked(in.readString()).readSummaryFromParcelLocked(in);
            }
        }
        int NU = in.readInt();
        if (NU > 10000) {
            throw new ParcelFormatException("File corrupt: too many uids " + NU);
        }
        for (int iu = 0; iu < NU; iu++) {
            int uid = in.readInt();
            Uid uid2 = new Uid(uid);
            this.mUidStats.put(uid, uid2);
            uid2.mWifiRunning = false;
            if (in.readInt() != 0) {
                uid2.mWifiRunningTimer.readSummaryFromParcelLocked(in);
            }
            uid2.mFullWifiLockOut = false;
            if (in.readInt() != 0) {
                uid2.mFullWifiLockTimer.readSummaryFromParcelLocked(in);
            }
            uid2.mWifiScanStarted = false;
            if (in.readInt() != 0) {
                uid2.mWifiScanTimer.readSummaryFromParcelLocked(in);
            }
            uid2.mWifiBatchedScanBinStarted = -1;
            for (i = 0; i < 5; i++) {
                if (in.readInt() != 0) {
                    uid2.makeWifiBatchedScanBin(i, null);
                    uid2.mWifiBatchedScanTimer[i].readSummaryFromParcelLocked(in);
                }
            }
            uid2.mWifiMulticastEnabled = false;
            if (in.readInt() != 0) {
                uid2.mWifiMulticastTimer.readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                uid2.createAudioTurnedOnTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                uid2.createVideoTurnedOnTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                uid2.createFlashlightTurnedOnTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                uid2.createCameraTurnedOnTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                uid2.createForegroundActivityTimerLocked().readSummaryFromParcelLocked(in);
            }
            uid2.mProcessState = 3;
            for (i = 0; i < 3; i++) {
                if (in.readInt() != 0) {
                    uid2.makeProcessState(i, null);
                    uid2.mProcessStateTimer[i].readSummaryFromParcelLocked(in);
                }
            }
            if (in.readInt() != 0) {
                uid2.createVibratorOnTimerLocked().readSummaryFromParcelLocked(in);
            }
            if (in.readInt() != 0) {
                if (uid2.mUserActivityCounters == null) {
                    uid2.initUserActivityLocked();
                }
                for (i = 0; i < 3; i++) {
                    uid2.mUserActivityCounters[i].readSummaryFromParcelLocked(in);
                }
            }
            if (in.readInt() != 0) {
                if (uid2.mNetworkByteActivityCounters == null) {
                    uid2.initNetworkActivityLocked();
                }
                for (i = 0; i < 4; i++) {
                    uid2.mNetworkByteActivityCounters[i].readSummaryFromParcelLocked(in);
                    uid2.mNetworkPacketActivityCounters[i].readSummaryFromParcelLocked(in);
                }
                uid2.mMobileRadioActiveTime.readSummaryFromParcelLocked(in);
                uid2.mMobileRadioActiveCount.readSummaryFromParcelLocked(in);
            }
            uid2.mUserCpuTime.readSummaryFromParcelLocked(in);
            uid2.mSystemCpuTime.readSummaryFromParcelLocked(in);
            uid2.mCpuPower.readSummaryFromParcelLocked(in);
            if (in.readInt() != 0) {
                int numClusters = in.readInt();
                if (this.mPowerProfile == null || this.mPowerProfile.getNumCpuClusters() == numClusters) {
                    uid2.mCpuClusterSpeed = new LongSamplingCounter[numClusters][];
                    int cluster = 0;
                    while (cluster < numClusters) {
                        if (in.readInt() != 0) {
                            int NSB = in.readInt();
                            if (this.mPowerProfile == null || this.mPowerProfile.getNumSpeedStepsInCpuCluster(cluster) == NSB) {
                                uid2.mCpuClusterSpeed[cluster] = new LongSamplingCounter[NSB];
                                for (int speed = 0; speed < NSB; speed++) {
                                    if (in.readInt() != 0) {
                                        uid2.mCpuClusterSpeed[cluster][speed] = new LongSamplingCounter(this.mOnBatteryTimeBase);
                                        uid2.mCpuClusterSpeed[cluster][speed].readSummaryFromParcelLocked(in);
                                    }
                                }
                            } else {
                                throw new ParcelFormatException("File corrupt: too many speed bins " + NSB);
                            }
                        }
                        uid2.mCpuClusterSpeed[cluster] = null;
                        cluster++;
                    }
                } else {
                    throw new ParcelFormatException("Incompatible cpu cluster arrangement");
                }
            }
            uid2.mCpuClusterSpeed = (LongSamplingCounter[][]) null;
            int NW = in.readInt();
            if (NW > 100) {
                throw new ParcelFormatException("File corrupt: too many wake locks " + NW);
            }
            for (int iw = 0; iw < NW; iw++) {
                uid2.readWakeSummaryFromParcelLocked(in.readString(), in);
            }
            int NS = in.readInt();
            if (NS > 100) {
                throw new ParcelFormatException("File corrupt: too many syncs " + NS);
            }
            int is;
            for (is = 0; is < NS; is++) {
                uid2.readSyncSummaryFromParcelLocked(in.readString(), in);
            }
            int NJ = in.readInt();
            if (NJ > 100) {
                throw new ParcelFormatException("File corrupt: too many job timers " + NJ);
            }
            for (int ij = 0; ij < NJ; ij++) {
                uid2.readJobSummaryFromParcelLocked(in.readString(), in);
            }
            int NP = in.readInt();
            if (NP > 1000) {
                throw new ParcelFormatException("File corrupt: too many sensors " + NP);
            }
            for (is = 0; is < NP; is++) {
                int seNumber = in.readInt();
                if (in.readInt() != 0) {
                    uid2.getSensorTimerLocked(seNumber, true).readSummaryFromParcelLocked(in);
                }
            }
            NP = in.readInt();
            if (NP > 1000) {
                throw new ParcelFormatException("File corrupt: too many processes " + NP);
            }
            int ip;
            for (ip = 0; ip < NP; ip++) {
                Proc p = uid2.getProcessStatsLocked(in.readString());
                long readLong = in.readLong();
                p.mLoadedUserTime = readLong;
                p.mUserTime = readLong;
                readLong = in.readLong();
                p.mLoadedSystemTime = readLong;
                p.mSystemTime = readLong;
                readLong = in.readLong();
                p.mLoadedForegroundTime = readLong;
                p.mForegroundTime = readLong;
                readInt = in.readInt();
                p.mLoadedStarts = readInt;
                p.mStarts = readInt;
                readInt = in.readInt();
                p.mLoadedNumCrashes = readInt;
                p.mNumCrashes = readInt;
                readInt = in.readInt();
                p.mLoadedNumAnrs = readInt;
                p.mNumAnrs = readInt;
                p.readExcessivePowerFromParcelLocked(in);
            }
            NP = in.readInt();
            if (NP > 10000) {
                throw new ParcelFormatException("File corrupt: too many packages " + NP);
            }
            for (ip = 0; ip < NP; ip++) {
                String pkgName = in.readString();
                Pkg p2 = uid2.getPackageStatsLocked(pkgName);
                int NWA = in.readInt();
                if (NWA > 1000) {
                    throw new ParcelFormatException("File corrupt: too many wakeup alarms " + NWA);
                }
                p2.mWakeupAlarms.clear();
                for (int iwa = 0; iwa < NWA; iwa++) {
                    String tag = in.readString();
                    Counter c = new Counter(this.mOnBatteryTimeBase);
                    c.readSummaryFromParcelLocked(in);
                    p2.mWakeupAlarms.put(tag, c);
                }
                NS = in.readInt();
                if (NS > 1000) {
                    throw new ParcelFormatException("File corrupt: too many services " + NS);
                }
                for (is = 0; is < NS; is++) {
                    Serv s = uid2.getServiceStatsLocked(pkgName, in.readString());
                    readLong = in.readLong();
                    s.mLoadedStartTime = readLong;
                    s.mStartTime = readLong;
                    readInt = in.readInt();
                    s.mLoadedStarts = readInt;
                    s.mStarts = readInt;
                    readInt = in.readInt();
                    s.mLoadedLaunches = readInt;
                    s.mLaunches = readInt;
                }
            }
        }
    }

    public void writeSummaryToParcel(Parcel out, boolean inclHistory) {
        int i;
        pullPendingStateUpdatesLocked();
        long startClockTime = getStartClockTime();
        long NOW_SYS = SystemClock.uptimeMillis() * 1000;
        long NOWREAL_SYS = SystemClock.elapsedRealtime() * 1000;
        out.writeInt(132);
        writeHistory(out, inclHistory, true);
        out.writeInt(this.mStartCount);
        out.writeLong(computeUptime(NOW_SYS, 0));
        out.writeLong(computeRealtime(NOWREAL_SYS, 0));
        out.writeLong(startClockTime);
        out.writeString(this.mStartPlatformVersion);
        out.writeString(this.mEndPlatformVersion);
        this.mOnBatteryTimeBase.writeSummaryToParcel(out, NOW_SYS, NOWREAL_SYS);
        this.mOnBatteryScreenOffTimeBase.writeSummaryToParcel(out, NOW_SYS, NOWREAL_SYS);
        out.writeInt(this.mDischargeUnplugLevel);
        out.writeInt(this.mDischargePlugLevel);
        out.writeInt(this.mDischargeCurrentLevel);
        out.writeInt(this.mCurrentBatteryLevel);
        out.writeInt(getLowDischargeAmountSinceCharge());
        out.writeInt(getHighDischargeAmountSinceCharge());
        out.writeInt(getDischargeAmountScreenOnSinceCharge());
        out.writeInt(getDischargeAmountScreenOffSinceCharge());
        this.mDischargeStepTracker.writeToParcel(out);
        this.mChargeStepTracker.writeToParcel(out);
        this.mDailyDischargeStepTracker.writeToParcel(out);
        this.mDailyChargeStepTracker.writeToParcel(out);
        if (this.mDailyPackageChanges != null) {
            int NPKG = this.mDailyPackageChanges.size();
            out.writeInt(NPKG);
            for (i = 0; i < NPKG; i++) {
                PackageChange pc = (PackageChange) this.mDailyPackageChanges.get(i);
                out.writeString(pc.mPackageName);
                out.writeInt(pc.mUpdate ? 1 : 0);
                out.writeInt(pc.mVersionCode);
            }
        } else {
            out.writeInt(0);
        }
        out.writeLong(this.mDailyStartTime);
        out.writeLong(this.mNextMinDailyDeadline);
        out.writeLong(this.mNextMaxDailyDeadline);
        this.mScreenOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        for (i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        }
        this.mInteractiveTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        this.mPowerSaveModeEnabledTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        this.mDeviceIdleModeEnabledTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        this.mDeviceIdlingTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        this.mPhoneOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        for (i = 0; i < SignalStrength.NUM_SIGNAL_STRENGTH_BINS; i++) {
            this.mPhoneSignalStrengthsTimer[i].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        }
        this.mPhoneSignalScanningTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        for (i = 0; i < 17; i++) {
            this.mPhoneDataConnectionsTimer[i].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        }
        for (i = 0; i < 4; i++) {
            this.mNetworkByteActivityCounters[i].writeSummaryFromParcelLocked(out);
            this.mNetworkPacketActivityCounters[i].writeSummaryFromParcelLocked(out);
        }
        this.mMobileRadioActiveTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        this.mMobileRadioActivePerAppTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        this.mMobileRadioActiveAdjustedTime.writeSummaryFromParcelLocked(out);
        this.mMobileRadioActiveUnknownTime.writeSummaryFromParcelLocked(out);
        this.mMobileRadioActiveUnknownCount.writeSummaryFromParcelLocked(out);
        this.mWifiOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        this.mGlobalWifiRunningTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        for (i = 0; i < 8; i++) {
            this.mWifiStateTimer[i].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        }
        for (i = 0; i < 13; i++) {
            this.mWifiSupplStateTimer[i].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        }
        for (i = 0; i < 5; i++) {
            this.mWifiSignalStrengthsTimer[i].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        }
        for (i = 0; i < 4; i++) {
            this.mBluetoothActivityCounters[i].writeSummaryFromParcelLocked(out);
        }
        for (i = 0; i < 4; i++) {
            this.mWifiActivityCounters[i].writeSummaryFromParcelLocked(out);
        }
        out.writeInt(this.mNumConnectivityChange);
        this.mFlashlightOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        this.mCameraOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
        out.writeInt(this.mKernelWakelockStats.size());
        for (Map.Entry<String, SamplingTimer> ent : this.mKernelWakelockStats.entrySet()) {
            Timer kwlt = (Timer) ent.getValue();
            if (kwlt != null) {
                out.writeInt(1);
                out.writeString((String) ent.getKey());
                kwlt.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
        }
        out.writeInt(this.mWakeupReasonStats.size());
        for (Map.Entry<String, SamplingTimer> ent2 : this.mWakeupReasonStats.entrySet()) {
            SamplingTimer timer = (SamplingTimer) ent2.getValue();
            if (timer != null) {
                out.writeInt(1);
                out.writeString((String) ent2.getKey());
                timer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
        }
        int NU = this.mUidStats.size();
        out.writeInt(NU);
        for (int iu = 0; iu < NU; iu++) {
            int is;
            out.writeInt(this.mUidStats.keyAt(iu));
            Uid u = (Uid) this.mUidStats.valueAt(iu);
            if (u.mWifiRunningTimer != null) {
                out.writeInt(1);
                u.mWifiRunningTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
            if (u.mFullWifiLockTimer != null) {
                out.writeInt(1);
                u.mFullWifiLockTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
            if (u.mWifiScanTimer != null) {
                out.writeInt(1);
                u.mWifiScanTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
            for (i = 0; i < 5; i++) {
                if (u.mWifiBatchedScanTimer[i] != null) {
                    out.writeInt(1);
                    u.mWifiBatchedScanTimer[i].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(0);
                }
            }
            if (u.mWifiMulticastTimer != null) {
                out.writeInt(1);
                u.mWifiMulticastTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
            if (u.mAudioTurnedOnTimer != null) {
                out.writeInt(1);
                u.mAudioTurnedOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
            if (u.mVideoTurnedOnTimer != null) {
                out.writeInt(1);
                u.mVideoTurnedOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
            if (u.mFlashlightTurnedOnTimer != null) {
                out.writeInt(1);
                u.mFlashlightTurnedOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
            if (u.mCameraTurnedOnTimer != null) {
                out.writeInt(1);
                u.mCameraTurnedOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
            if (u.mForegroundActivityTimer != null) {
                out.writeInt(1);
                u.mForegroundActivityTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
            for (i = 0; i < 3; i++) {
                if (u.mProcessStateTimer[i] != null) {
                    out.writeInt(1);
                    u.mProcessStateTimer[i].writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(0);
                }
            }
            if (u.mVibratorOnTimer != null) {
                out.writeInt(1);
                u.mVibratorOnTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            } else {
                out.writeInt(0);
            }
            if (u.mUserActivityCounters == null) {
                out.writeInt(0);
            } else {
                out.writeInt(1);
                for (i = 0; i < 3; i++) {
                    u.mUserActivityCounters[i].writeSummaryFromParcelLocked(out);
                }
            }
            if (u.mNetworkByteActivityCounters == null) {
                out.writeInt(0);
            } else {
                out.writeInt(1);
                for (i = 0; i < 4; i++) {
                    u.mNetworkByteActivityCounters[i].writeSummaryFromParcelLocked(out);
                    u.mNetworkPacketActivityCounters[i].writeSummaryFromParcelLocked(out);
                }
                u.mMobileRadioActiveTime.writeSummaryFromParcelLocked(out);
                u.mMobileRadioActiveCount.writeSummaryFromParcelLocked(out);
            }
            u.mUserCpuTime.writeSummaryFromParcelLocked(out);
            u.mSystemCpuTime.writeSummaryFromParcelLocked(out);
            u.mCpuPower.writeSummaryFromParcelLocked(out);
            if (u.mCpuClusterSpeed != null) {
                out.writeInt(1);
                out.writeInt(u.mCpuClusterSpeed.length);
                for (LongSamplingCounter[] cpuSpeeds : u.mCpuClusterSpeed) {
                    if (cpuSpeeds != null) {
                        out.writeInt(1);
                        out.writeInt(cpuSpeeds.length);
                        for (LongSamplingCounter c : cpuSpeeds) {
                            if (c != null) {
                                out.writeInt(1);
                                c.writeSummaryFromParcelLocked(out);
                            } else {
                                out.writeInt(0);
                            }
                        }
                    } else {
                        out.writeInt(0);
                    }
                }
            } else {
                out.writeInt(0);
            }
            ArrayMap<String, Wakelock> wakeStats = u.mWakelockStats.getMap();
            int NW = wakeStats.size();
            out.writeInt(NW);
            for (int iw = 0; iw < NW; iw++) {
                out.writeString((String) wakeStats.keyAt(iw));
                Wakelock wl = (Wakelock) wakeStats.valueAt(iw);
                if (wl.mTimerFull != null) {
                    out.writeInt(1);
                    wl.mTimerFull.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(0);
                }
                if (wl.mTimerPartial != null) {
                    out.writeInt(1);
                    wl.mTimerPartial.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(0);
                }
                if (wl.mTimerWindow != null) {
                    out.writeInt(1);
                    wl.mTimerWindow.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(0);
                }
                if (wl.mTimerDraw != null) {
                    out.writeInt(1);
                    wl.mTimerDraw.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(0);
                }
            }
            ArrayMap<String, StopwatchTimer> syncStats = u.mSyncStats.getMap();
            int NS = syncStats.size();
            out.writeInt(NS);
            for (is = 0; is < NS; is++) {
                out.writeString((String) syncStats.keyAt(is));
                ((StopwatchTimer) syncStats.valueAt(is)).writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            }
            ArrayMap<String, StopwatchTimer> jobStats = u.mJobStats.getMap();
            int NJ = jobStats.size();
            out.writeInt(NJ);
            for (int ij = 0; ij < NJ; ij++) {
                out.writeString((String) jobStats.keyAt(ij));
                ((StopwatchTimer) jobStats.valueAt(ij)).writeSummaryFromParcelLocked(out, NOWREAL_SYS);
            }
            int NSE = u.mSensorStats.size();
            out.writeInt(NSE);
            for (int ise = 0; ise < NSE; ise++) {
                out.writeInt(u.mSensorStats.keyAt(ise));
                Sensor se = (Sensor) u.mSensorStats.valueAt(ise);
                if (se.mTimer != null) {
                    out.writeInt(1);
                    se.mTimer.writeSummaryFromParcelLocked(out, NOWREAL_SYS);
                } else {
                    out.writeInt(0);
                }
            }
            int NP = u.mProcessStats.size();
            out.writeInt(NP);
            for (int ip = 0; ip < NP; ip++) {
                try {
                    out.writeString((String) u.mProcessStats.keyAt(ip));
                    Proc ps = (Proc) u.mProcessStats.valueAt(ip);
                    out.writeLong(ps.mUserTime);
                    out.writeLong(ps.mSystemTime);
                    out.writeLong(ps.mForegroundTime);
                    out.writeInt(ps.mStarts);
                    out.writeInt(ps.mNumCrashes);
                    out.writeInt(ps.mNumAnrs);
                    ps.writeExcessivePowerToParcelLocked(out);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }
            }
            NP = u.mPackageStats.size();
            out.writeInt(NP);
            if (NP > 0) {
                for (Map.Entry<String, Pkg> ent3 : u.mPackageStats.entrySet()) {
                    out.writeString((String) ent3.getKey());
                    Pkg ps2 = (Pkg) ent3.getValue();
                    int NWA = ps2.mWakeupAlarms.size();
                    out.writeInt(NWA);
                    for (int iwa = 0; iwa < NWA; iwa++) {
                        out.writeString((String) ps2.mWakeupAlarms.keyAt(iwa));
                        ((Counter) ps2.mWakeupAlarms.valueAt(iwa)).writeSummaryFromParcelLocked(out);
                    }
                    NS = ps2.mServiceStats.size();
                    out.writeInt(NS);
                    for (is = 0; is < NS; is++) {
                        out.writeString((String) ps2.mServiceStats.keyAt(is));
                        Serv ss = (Serv) ps2.mServiceStats.valueAt(is);
                        out.writeLong(ss.getStartTimeToNowLocked(this.mOnBatteryTimeBase.getUptime(NOW_SYS)));
                        out.writeInt(ss.mStarts);
                        out.writeInt(ss.mLaunches);
                    }
                }
            }
        }
    }

    public void readFromParcel(Parcel in) {
        readFromParcelLocked(in);
    }

    void readFromParcelLocked(Parcel in) {
        int magic = in.readInt();
        if (magic != MAGIC) {
            throw new ParcelFormatException("Bad magic number: #" + Integer.toHexString(magic));
        }
        int i;
        readHistory(in, false);
        this.mStartCount = in.readInt();
        this.mStartClockTime = in.readLong();
        this.mStartPlatformVersion = in.readString();
        this.mEndPlatformVersion = in.readString();
        this.mUptime = in.readLong();
        this.mUptimeStart = in.readLong();
        this.mRealtime = in.readLong();
        this.mRealtimeStart = in.readLong();
        this.mOnBattery = in.readInt() != 0;
        this.mOnBatteryInternal = false;
        this.mOnBatteryTimeBase.readFromParcel(in);
        this.mOnBatteryScreenOffTimeBase.readFromParcel(in);
        this.mScreenState = 0;
        this.mScreenOnTimer = new StopwatchTimer(null, -1, null, this.mOnBatteryTimeBase, in);
        for (i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i] = new StopwatchTimer(null, -100 - i, null, this.mOnBatteryTimeBase, in);
        }
        this.mInteractive = false;
        this.mInteractiveTimer = new StopwatchTimer(null, -10, null, this.mOnBatteryTimeBase, in);
        this.mPhoneOn = false;
        this.mPowerSaveModeEnabledTimer = new StopwatchTimer(null, -2, null, this.mOnBatteryTimeBase, in);
        this.mDeviceIdleModeEnabledTimer = new StopwatchTimer(null, -11, null, this.mOnBatteryTimeBase, in);
        this.mDeviceIdlingTimer = new StopwatchTimer(null, -12, null, this.mOnBatteryTimeBase, in);
        this.mPhoneOnTimer = new StopwatchTimer(null, -3, null, this.mOnBatteryTimeBase, in);
        for (i = 0; i < SignalStrength.NUM_SIGNAL_STRENGTH_BINS; i++) {
            this.mPhoneSignalStrengthsTimer[i] = new StopwatchTimer(null, -200 - i, null, this.mOnBatteryTimeBase, in);
        }
        this.mPhoneSignalScanningTimer = new StopwatchTimer(null, -199, null, this.mOnBatteryTimeBase, in);
        for (i = 0; i < 17; i++) {
            this.mPhoneDataConnectionsTimer[i] = new StopwatchTimer(null, -300 - i, null, this.mOnBatteryTimeBase, in);
        }
        for (i = 0; i < 4; i++) {
            this.mNetworkByteActivityCounters[i] = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
            this.mNetworkPacketActivityCounters[i] = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
        }
        this.mMobileRadioPowerState = DataConnectionRealTimeInfo.DC_POWER_STATE_LOW;
        this.mMobileRadioActiveTimer = new StopwatchTimer(null, -400, null, this.mOnBatteryTimeBase, in);
        this.mMobileRadioActivePerAppTimer = new StopwatchTimer(null, -401, null, this.mOnBatteryTimeBase, in);
        this.mMobileRadioActiveAdjustedTime = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
        this.mMobileRadioActiveUnknownTime = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
        this.mMobileRadioActiveUnknownCount = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
        this.mWifiRadioPowerState = DataConnectionRealTimeInfo.DC_POWER_STATE_LOW;
        this.mWifiOn = false;
        this.mWifiOnTimer = new StopwatchTimer(null, -4, null, this.mOnBatteryTimeBase, in);
        this.mGlobalWifiRunning = false;
        this.mGlobalWifiRunningTimer = new StopwatchTimer(null, -5, null, this.mOnBatteryTimeBase, in);
        for (i = 0; i < 8; i++) {
            this.mWifiStateTimer[i] = new StopwatchTimer(null, -600 - i, null, this.mOnBatteryTimeBase, in);
        }
        for (i = 0; i < 13; i++) {
            this.mWifiSupplStateTimer[i] = new StopwatchTimer(null, -700 - i, null, this.mOnBatteryTimeBase, in);
        }
        for (i = 0; i < 5; i++) {
            this.mWifiSignalStrengthsTimer[i] = new StopwatchTimer(null, -800 - i, null, this.mOnBatteryTimeBase, in);
        }
        for (i = 0; i < 4; i++) {
            this.mBluetoothActivityCounters[i] = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
        }
        for (i = 0; i < 4; i++) {
            this.mWifiActivityCounters[i] = new LongSamplingCounter(this.mOnBatteryTimeBase, in);
        }
        this.mHasWifiEnergyReporting = in.readInt() != 0;
        this.mHasBluetoothEnergyReporting = in.readInt() != 0;
        this.mNumConnectivityChange = in.readInt();
        this.mLoadedNumConnectivityChange = in.readInt();
        this.mUnpluggedNumConnectivityChange = in.readInt();
        this.mAudioOnNesting = 0;
        this.mAudioOnTimer = new StopwatchTimer(null, -7, null, this.mOnBatteryTimeBase);
        this.mVideoOnNesting = 0;
        this.mVideoOnTimer = new StopwatchTimer(null, -8, null, this.mOnBatteryTimeBase);
        this.mFlashlightOnNesting = 0;
        this.mFlashlightOnTimer = new StopwatchTimer(null, -9, null, this.mOnBatteryTimeBase, in);
        this.mCameraOnNesting = 0;
        this.mCameraOnTimer = new StopwatchTimer(null, -13, null, this.mOnBatteryTimeBase, in);
        this.mDischargeUnplugLevel = in.readInt();
        this.mDischargePlugLevel = in.readInt();
        this.mDischargeCurrentLevel = in.readInt();
        this.mCurrentBatteryLevel = in.readInt();
        this.mLowDischargeAmountSinceCharge = in.readInt();
        this.mHighDischargeAmountSinceCharge = in.readInt();
        this.mDischargeAmountScreenOn = in.readInt();
        this.mDischargeAmountScreenOnSinceCharge = in.readInt();
        this.mDischargeAmountScreenOff = in.readInt();
        this.mDischargeAmountScreenOffSinceCharge = in.readInt();
        this.mDischargeStepTracker.readFromParcel(in);
        this.mChargeStepTracker.readFromParcel(in);
        this.mLastWriteTime = in.readLong();
        this.mKernelWakelockStats.clear();
        int NKW = in.readInt();
        for (int ikw = 0; ikw < NKW; ikw++) {
            if (in.readInt() != 0) {
                this.mKernelWakelockStats.put(in.readString(), new SamplingTimer(this.mOnBatteryScreenOffTimeBase, in));
            }
        }
        this.mWakeupReasonStats.clear();
        int NWR = in.readInt();
        for (int iwr = 0; iwr < NWR; iwr++) {
            if (in.readInt() != 0) {
                this.mWakeupReasonStats.put(in.readString(), new SamplingTimer(this.mOnBatteryTimeBase, in));
            }
        }
        this.mPartialTimers.clear();
        this.mFullTimers.clear();
        this.mWindowTimers.clear();
        this.mWifiRunningTimers.clear();
        this.mFullWifiLockTimers.clear();
        this.mWifiScanTimers.clear();
        this.mWifiBatchedScanTimers.clear();
        this.mWifiMulticastTimers.clear();
        this.mAudioTurnedOnTimers.clear();
        this.mVideoTurnedOnTimers.clear();
        this.mFlashlightTurnedOnTimers.clear();
        this.mCameraTurnedOnTimers.clear();
        int numUids = in.readInt();
        this.mUidStats.clear();
        for (i = 0; i < numUids; i++) {
            int uid = in.readInt();
            Uid uid2 = new Uid(uid);
            uid2.readFromParcelLocked(this.mOnBatteryTimeBase, this.mOnBatteryScreenOffTimeBase, in);
            this.mUidStats.append(uid, uid2);
        }
    }

    public void writeToParcel(Parcel out, int flags) {
        writeToParcelLocked(out, true, flags);
    }

    public void writeToParcelWithoutUids(Parcel out, int flags) {
        writeToParcelLocked(out, false, flags);
    }

    void writeToParcelLocked(Parcel out, boolean inclUids, int flags) {
        int i;
        pullPendingStateUpdatesLocked();
        long startClockTime = getStartClockTime();
        long uSecUptime = SystemClock.uptimeMillis() * 1000;
        long uSecRealtime = SystemClock.elapsedRealtime() * 1000;
        long batteryRealtime = this.mOnBatteryTimeBase.getRealtime(uSecRealtime);
        long batteryScreenOffRealtime = this.mOnBatteryScreenOffTimeBase.getRealtime(uSecRealtime);
        out.writeInt(MAGIC);
        writeHistory(out, true, false);
        out.writeInt(this.mStartCount);
        out.writeLong(startClockTime);
        out.writeString(this.mStartPlatformVersion);
        out.writeString(this.mEndPlatformVersion);
        out.writeLong(this.mUptime);
        out.writeLong(this.mUptimeStart);
        out.writeLong(this.mRealtime);
        out.writeLong(this.mRealtimeStart);
        out.writeInt(this.mOnBattery ? 1 : 0);
        this.mOnBatteryTimeBase.writeToParcel(out, uSecUptime, uSecRealtime);
        this.mOnBatteryScreenOffTimeBase.writeToParcel(out, uSecUptime, uSecRealtime);
        this.mScreenOnTimer.writeToParcel(out, uSecRealtime);
        for (i = 0; i < 5; i++) {
            this.mScreenBrightnessTimer[i].writeToParcel(out, uSecRealtime);
        }
        this.mInteractiveTimer.writeToParcel(out, uSecRealtime);
        this.mPowerSaveModeEnabledTimer.writeToParcel(out, uSecRealtime);
        this.mDeviceIdleModeEnabledTimer.writeToParcel(out, uSecRealtime);
        this.mDeviceIdlingTimer.writeToParcel(out, uSecRealtime);
        this.mPhoneOnTimer.writeToParcel(out, uSecRealtime);
        for (i = 0; i < SignalStrength.NUM_SIGNAL_STRENGTH_BINS; i++) {
            this.mPhoneSignalStrengthsTimer[i].writeToParcel(out, uSecRealtime);
        }
        this.mPhoneSignalScanningTimer.writeToParcel(out, uSecRealtime);
        for (i = 0; i < 17; i++) {
            this.mPhoneDataConnectionsTimer[i].writeToParcel(out, uSecRealtime);
        }
        for (i = 0; i < 4; i++) {
            this.mNetworkByteActivityCounters[i].writeToParcel(out);
            this.mNetworkPacketActivityCounters[i].writeToParcel(out);
        }
        this.mMobileRadioActiveTimer.writeToParcel(out, uSecRealtime);
        this.mMobileRadioActivePerAppTimer.writeToParcel(out, uSecRealtime);
        this.mMobileRadioActiveAdjustedTime.writeToParcel(out);
        this.mMobileRadioActiveUnknownTime.writeToParcel(out);
        this.mMobileRadioActiveUnknownCount.writeToParcel(out);
        this.mWifiOnTimer.writeToParcel(out, uSecRealtime);
        this.mGlobalWifiRunningTimer.writeToParcel(out, uSecRealtime);
        for (i = 0; i < 8; i++) {
            this.mWifiStateTimer[i].writeToParcel(out, uSecRealtime);
        }
        for (i = 0; i < 13; i++) {
            this.mWifiSupplStateTimer[i].writeToParcel(out, uSecRealtime);
        }
        for (i = 0; i < 5; i++) {
            this.mWifiSignalStrengthsTimer[i].writeToParcel(out, uSecRealtime);
        }
        for (i = 0; i < 4; i++) {
            this.mBluetoothActivityCounters[i].writeToParcel(out);
        }
        for (i = 0; i < 4; i++) {
            this.mWifiActivityCounters[i].writeToParcel(out);
        }
        out.writeInt(this.mHasWifiEnergyReporting ? 1 : 0);
        out.writeInt(this.mHasBluetoothEnergyReporting ? 1 : 0);
        out.writeInt(this.mNumConnectivityChange);
        out.writeInt(this.mLoadedNumConnectivityChange);
        out.writeInt(this.mUnpluggedNumConnectivityChange);
        this.mFlashlightOnTimer.writeToParcel(out, uSecRealtime);
        this.mCameraOnTimer.writeToParcel(out, uSecRealtime);
        out.writeInt(this.mDischargeUnplugLevel);
        out.writeInt(this.mDischargePlugLevel);
        out.writeInt(this.mDischargeCurrentLevel);
        out.writeInt(this.mCurrentBatteryLevel);
        out.writeInt(this.mLowDischargeAmountSinceCharge);
        out.writeInt(this.mHighDischargeAmountSinceCharge);
        out.writeInt(this.mDischargeAmountScreenOn);
        out.writeInt(this.mDischargeAmountScreenOnSinceCharge);
        out.writeInt(this.mDischargeAmountScreenOff);
        out.writeInt(this.mDischargeAmountScreenOffSinceCharge);
        this.mDischargeStepTracker.writeToParcel(out);
        this.mChargeStepTracker.writeToParcel(out);
        out.writeLong(this.mLastWriteTime);
        if (inclUids) {
            out.writeInt(this.mKernelWakelockStats.size());
            for (Map.Entry<String, SamplingTimer> ent : this.mKernelWakelockStats.entrySet()) {
                SamplingTimer kwlt = (SamplingTimer) ent.getValue();
                if (kwlt != null) {
                    out.writeInt(1);
                    out.writeString((String) ent.getKey());
                    kwlt.writeToParcel(out, uSecRealtime);
                } else {
                    out.writeInt(0);
                }
            }
            out.writeInt(this.mWakeupReasonStats.size());
            for (Map.Entry<String, SamplingTimer> ent2 : this.mWakeupReasonStats.entrySet()) {
                SamplingTimer timer = (SamplingTimer) ent2.getValue();
                if (timer != null) {
                    out.writeInt(1);
                    out.writeString((String) ent2.getKey());
                    timer.writeToParcel(out, uSecRealtime);
                } else {
                    out.writeInt(0);
                }
            }
        } else {
            out.writeInt(0);
        }
        if (inclUids) {
            int size = this.mUidStats.size();
            out.writeInt(size);
            for (i = 0; i < size; i++) {
                out.writeInt(this.mUidStats.keyAt(i));
                ((Uid) this.mUidStats.valueAt(i)).writeToParcelLocked(out, uSecRealtime);
            }
            return;
        }
        out.writeInt(0);
    }

    public void prepareForDumpLocked() {
        pullPendingStateUpdatesLocked();
        getStartClockTime();
    }

    public void dumpLocked(Context context, PrintWriter pw, int flags, int reqUid, long histStart) {
        super.dumpLocked(context, pw, flags, reqUid, histStart);
    }
}
