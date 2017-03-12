package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.text.format.DateFormat;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.utils.PedoHistory;
import com.samsung.android.contextaware.manager.CaUserInfo;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.manager.ListenerListManager;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.fingerprint.FingerprintManager;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PedometerRunner extends LibTypeProvider {
    private final Lock _mutex = new ReentrantLock(true);
    private double accumulativeCalorie;
    private double accumulativeDistance;
    private long accumulativeRunDownStepCount;
    private long accumulativeRunStepCount;
    private long accumulativeRunUpStepCount;
    private long accumulativeTotalStepCount;
    private long accumulativeUpDownStepCount;
    private long accumulativeWalkDownStepCount;
    private long accumulativeWalkStepCount;
    private long accumulativeWalkUpStepCount;
    private final PedoHistory mPedoHistory = PedoHistory.getInstance();

    public PedometerRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
        CaLogger.info("PedometerRunner is created");
    }

    private void sendHistoryStepBuffer() {
        String[] names = getContextValueNames();
        long totalStepCountDiff = 0;
        long walkStepCountDiff = 0;
        long runStepCountDiff = 0;
        long walkUpStepCountDiff = 0;
        long walkDownStepCountDiff = 0;
        long runUpStepCountDiff = 0;
        long runDownStepCountDiff = 0;
        double distanceDiff = 0.0d;
        double calorieDiff = 0.0d;
        double speed = 0.0d;
        int loggingCount = this.mPedoHistory.getBufferSize();
        if (loggingCount == 0) {
            CaLogger.warning("History Data Buffer is null!!");
        }
        long[] timeStampArray = new long[loggingCount];
        double[] distanceDiffArray = new double[loggingCount];
        double[] calorieDiffArray = new double[loggingCount];
        double[] speedArray = new double[loggingCount];
        long[] walkStepCountDiffArray = new long[loggingCount];
        long[] runStepCountDiffArray = new long[loggingCount];
        long[] walkUpStepCountDiffArray = new long[loggingCount];
        long[] walkDownStepCountDiffArray = new long[loggingCount];
        long[] runUpStepCountDiffArray = new long[loggingCount];
        long[] runDownStepCountDiffArray = new long[loggingCount];
        long[] totalStepCountDiffArray = new long[loggingCount];
        for (int i = 0; i < loggingCount; i++) {
            timeStampArray[i] = this.mPedoHistory.getTimeStampSingle(i);
            totalStepCountDiffArray[i] = this.mPedoHistory.getTotalStepCountSingle(i);
            totalStepCountDiff += totalStepCountDiffArray[i];
            walkStepCountDiffArray[i] = this.mPedoHistory.getWalkStepCountSingle(i);
            walkStepCountDiff += walkStepCountDiffArray[i];
            runStepCountDiffArray[i] = this.mPedoHistory.getRunStepCountSingle(i);
            runStepCountDiff += runStepCountDiffArray[i];
            walkUpStepCountDiffArray[i] = this.mPedoHistory.getWalkUpStepCountSingle(i);
            walkUpStepCountDiff += walkUpStepCountDiffArray[i];
            runUpStepCountDiffArray[i] = this.mPedoHistory.getRunUpStepCountSingle(i);
            runUpStepCountDiff += runUpStepCountDiffArray[i];
            walkDownStepCountDiffArray[i] = this.mPedoHistory.getWalkDnStepCountSingle(i);
            walkDownStepCountDiff += walkDownStepCountDiffArray[i];
            runDownStepCountDiffArray[i] = this.mPedoHistory.getRunDnStepCountSingle(i);
            runDownStepCountDiff += runDownStepCountDiffArray[i];
            distanceDiffArray[i] = this.mPedoHistory.getDistanceArraySingle(i);
            distanceDiff += distanceDiffArray[i];
            calorieDiffArray[i] = this.mPedoHistory.getCalorieArraySingle(i);
            calorieDiff += calorieDiffArray[i];
            speedArray[i] = this.mPedoHistory.getSpeedArraySingle(i);
            speed += speedArray[i];
        }
        long upDownStepCountDiff = ((walkUpStepCountDiff + walkDownStepCountDiff) + runUpStepCountDiff) + runDownStepCountDiff;
        if (loggingCount > 0) {
            speed /= (double) loggingCount;
        } else {
            speed = 0.0d;
        }
        getContextBean().putContext(names[0], walkStepCountDiff);
        getContextBean().putContext(names[1], this.accumulativeWalkStepCount);
        getContextBean().putContext(names[2], runStepCountDiff);
        getContextBean().putContext(names[3], this.accumulativeRunStepCount);
        getContextBean().putContext(names[4], upDownStepCountDiff);
        getContextBean().putContext(names[5], this.accumulativeUpDownStepCount);
        getContextBean().putContext(names[6], totalStepCountDiff);
        getContextBean().putContext(names[7], this.accumulativeTotalStepCount);
        getContextBean().putContext(names[8], distanceDiff);
        getContextBean().putContext(names[9], this.accumulativeDistance);
        getContextBean().putContext(names[10], speed);
        getContextBean().putContext(names[11], -1);
        getContextBean().putContext(names[12], calorieDiff);
        getContextBean().putContext(names[13], this.accumulativeCalorie);
        getContextBean().putContext(names[14], 0.0d);
        getContextBean().putContext(names[15], walkUpStepCountDiff);
        getContextBean().putContext(names[16], this.accumulativeWalkUpStepCount);
        getContextBean().putContext(names[17], walkDownStepCountDiff);
        getContextBean().putContext(names[18], this.accumulativeWalkDownStepCount);
        getContextBean().putContext(names[19], runUpStepCountDiff);
        getContextBean().putContext(names[20], this.accumulativeRunUpStepCount);
        getContextBean().putContext(names[21], runDownStepCountDiff);
        getContextBean().putContext(names[22], this.accumulativeRunDownStepCount);
        getContextBean().putContext(names[23], loggingCount);
        getContextBean().putContext(names[24], timeStampArray);
        getContextBean().putContext(names[25], distanceDiffArray);
        getContextBean().putContext(names[26], calorieDiffArray);
        getContextBean().putContext(names[27], speedArray);
        getContextBean().putContext(names[28], walkStepCountDiffArray);
        getContextBean().putContext(names[29], runStepCountDiffArray);
        getContextBean().putContext(names[30], walkUpStepCountDiffArray);
        getContextBean().putContext(names[31], walkDownStepCountDiffArray);
        getContextBean().putContext(names[32], runUpStepCountDiffArray);
        getContextBean().putContext(names[33], runDownStepCountDiffArray);
        getContextBean().putContext(names[34], totalStepCountDiffArray);
        getContextBean().putContext(names[35], 1);
        super.notifyObserver();
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_PEDOMETER.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) 3;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) ((int) CaUserInfo.getInstance().getUserHeight()), (byte) ((int) CaUserInfo.getInstance().getUserWeight()), (byte) CaUserInfo.getInstance().getUserGender()};
    }

    public final String[] getContextValueNames() {
        return new String[]{"WalkStepCountDiff", "WalkStepCount", "RunStepCountDiff", "RunStepCount", "UpDownStepCountDiff", "UpDownStepCount", "TotalStepCountDiff", "TotalStepCount", "DistanceDiff", "Distance", "Speed", "StepStatus", "CalorieDiff", "Calorie", "WalkingFrequency", "WalkUpStepCountDiff", "WalkUpStepCount", "WalkDownStepCountDiff", "WalkDownStepCount", "RunUpStepCountDiff", "RunUpStepCount", "RunDownStepCountDiff", "RunDownStepCount", "LoggingCount", "TimeStampArray", "DistanceDiffArray", "CalorieDiffArray", "SpeedArray", "WalkStepCountDiffArray", "RunStepCountDiffArray", "WalkUpStepCountDiffArray", "WalkDownStepCountDiffArray", "RunUpStepCountDiffArray", "RunDownStepCountDiffArray", "TotalStepCountDiffArray", "PreviousStepBuffer"};
    }

    protected void display() {
        Bundle context = getContextBean().getContextBundleForDisplay();
        if (context != null && !context.isEmpty()) {
            CaLogger.debug("================= " + getContextType() + " =================");
            StringBuffer str = new StringBuffer();
            for (String i : context.keySet()) {
                if (i == null || i.isEmpty()) {
                    break;
                }
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < i.length(); j++) {
                    if (i.charAt(j) < DateFormat.AM_PM) {
                        sb.append(i.charAt(j));
                    }
                }
                str.append(sb + "=[" + getDisplayContents(context, i) + "], ");
            }
            if (str.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER) > 0) {
                str.delete(str.lastIndexOf(FingerprintManager.FINGER_PERMISSION_DELIMITER), str.length());
            }
            CaLogger.info(str.toString());
        }
    }

    public int parse(byte[] packet, int next) {
        Throwable th;
        int tmpNext = next;
        CaLogger.info("parse start:" + tmpNext);
        if ((packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        this._mutex.lock();
        String[] names = getContextValueNames();
        int tmpNext2 = tmpNext + 1;
        int data = packet[tmpNext];
        long totalStepCountDiff = 0;
        long walkStepCountDiff = 0;
        long runStepCountDiff = 0;
        long walkUpStepCountDiff = 0;
        long walkDownStepCountDiff = 0;
        long runUpStepCountDiff = 0;
        long runDownStepCountDiff = 0;
        double distanceDiff = 0.0d;
        double calorieDiff = 0.0d;
        double speed = 0.0d;
        Calendar calender = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
        int hour = calender.get(11);
        int minute = calender.get(12);
        long curUtcTime = (long) (((((hour * 3600) + (minute * 60)) + calender.get(13)) * 1000) + calender.get(14));
        long curTimeMillis = calender.getTimeInMillis();
        int i;
        if ((data & 128) == 0) {
            if ((packet.length - tmpNext2) - 14 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                this._mutex.unlock();
                tmpNext = tmpNext2;
                return -1;
            }
            data &= 127;
            super.getContextBean().putContext(names[0], (long) data);
            walkStepCountDiff = (long) data;
            this.accumulativeWalkStepCount += (long) data;
            super.getContextBean().putContext(names[1], this.accumulativeWalkStepCount);
            tmpNext = tmpNext2 + 1;
            data = packet[tmpNext2];
            super.getContextBean().putContext(names[2], (long) data);
            runStepCountDiff = (long) data;
            this.accumulativeRunStepCount += (long) data;
            super.getContextBean().putContext(names[3], this.accumulativeRunStepCount);
            tmpNext2 = tmpNext + 1;
            data = packet[tmpNext];
            super.getContextBean().putContext(names[4], (long) data);
            this.accumulativeUpDownStepCount += (long) data;
            super.getContextBean().putContext(names[5], this.accumulativeUpDownStepCount);
            tmpNext = tmpNext2 + 1;
            i = (packet[tmpNext2] & 255) << 8;
            tmpNext2 = tmpNext + 1;
            data = i + (packet[tmpNext] & 255);
            super.getContextBean().putContext(names[6], (long) data);
            totalStepCountDiff = (long) data;
            this.accumulativeTotalStepCount += (long) data;
            super.getContextBean().putContext(names[7], this.accumulativeTotalStepCount);
            tmpNext = tmpNext2 + 1;
            i = (packet[tmpNext2] & 255) << 8;
            tmpNext2 = tmpNext + 1;
            data = i + (packet[tmpNext] & 255);
            super.getContextBean().putContext(names[8], ((double) data) / 100.0d);
            distanceDiff = ((double) data) / 100.0d;
            this.accumulativeDistance += ((double) data) / 100.0d;
            super.getContextBean().putContext(names[9], this.accumulativeDistance);
            tmpNext = tmpNext2 + 1;
            speed = ((double) CaConvertUtil.getCompleteOfTwo(packet[tmpNext2])) / 10.0d;
            super.getContextBean().putContext(names[10], speed);
            tmpNext2 = tmpNext + 1;
            super.getContextBean().putContext(names[11], packet[tmpNext]);
            tmpNext = tmpNext2 + 1;
            calorieDiff = ((double) CaConvertUtil.getCompleteOfTwo(packet[tmpNext2])) / 100.0d;
            super.getContextBean().putContext(names[12], calorieDiff);
            this.accumulativeCalorie += calorieDiff;
            super.getContextBean().putContext(names[13], this.accumulativeCalorie);
            tmpNext2 = tmpNext + 1;
            super.getContextBean().putContext(names[14], ((double) packet[tmpNext]) / 10.0d);
            tmpNext = tmpNext2 + 1;
            data = packet[tmpNext2];
            super.getContextBean().putContext(names[15], (long) data);
            walkUpStepCountDiff = (long) data;
            this.accumulativeWalkUpStepCount += (long) data;
            super.getContextBean().putContext(names[16], this.accumulativeWalkUpStepCount);
            tmpNext2 = tmpNext + 1;
            data = packet[tmpNext];
            super.getContextBean().putContext(names[17], (long) data);
            walkDownStepCountDiff = (long) data;
            this.accumulativeWalkDownStepCount += (long) data;
            super.getContextBean().putContext(names[18], this.accumulativeWalkDownStepCount);
            tmpNext = tmpNext2 + 1;
            data = packet[tmpNext2];
            super.getContextBean().putContext(names[19], (long) data);
            runUpStepCountDiff = (long) data;
            this.accumulativeRunUpStepCount += (long) data;
            super.getContextBean().putContext(names[20], this.accumulativeRunUpStepCount);
            tmpNext2 = tmpNext + 1;
            data = packet[tmpNext];
            super.getContextBean().putContext(names[21], (long) data);
            runDownStepCountDiff = (long) data;
            this.accumulativeRunDownStepCount += (long) data;
            super.getContextBean().putContext(names[22], this.accumulativeRunDownStepCount);
            if (totalStepCountDiff > 0) {
                this.mPedoHistory.updateBufferIndex(1);
                this.mPedoHistory.putTotalStepInfoSingle(totalStepCountDiff);
                this.mPedoHistory.putWalkStepInfoSingle(walkStepCountDiff);
                this.mPedoHistory.putRunStepInfoSingle(runStepCountDiff);
                this.mPedoHistory.putWalkUpStepInfoSingle(walkUpStepCountDiff);
                this.mPedoHistory.putRunUpStepInfoSingle(runUpStepCountDiff);
                this.mPedoHistory.putWalkDnStepInfoSingle(walkDownStepCountDiff);
                this.mPedoHistory.putRunDnStepInfoSingle(runDownStepCountDiff);
                this.mPedoHistory.putDistanceInfoSingle(distanceDiff);
                this.mPedoHistory.putCalorieInfoSingle(calorieDiff);
                this.mPedoHistory.putSpeedInfoSingle(speed);
                this.mPedoHistory.setDataMode(1);
            }
            tmpNext = tmpNext2;
        } else if (((data & 192) >> 6) == 3) {
            if ((packet.length - tmpNext2) - 1 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                this._mutex.unlock();
                tmpNext = tmpNext2;
                return -1;
            }
            tmpNext = tmpNext2 + 1;
            int loggingCount = packet[tmpNext2];
            if (loggingCount <= 0) {
                CaLogger.error(SensorHubErrors.ERROR_LOGGING_PACKAGE_SIZE.getMessage());
                this._mutex.unlock();
                return -1;
            }
            long[] timeStampArray = new long[loggingCount];
            double[] distanceDiffArray = new double[loggingCount];
            double[] calorieDiffArray = new double[loggingCount];
            double[] speedArray = new double[loggingCount];
            long[] walkStepCountDiffArray = new long[loggingCount];
            long[] runStepCountDiffArray = new long[loggingCount];
            long[] walkUpStepCountDiffArray = new long[loggingCount];
            long[] walkDownStepCountDiffArray = new long[loggingCount];
            long[] runUpStepCountDiffArray = new long[loggingCount];
            long[] runDownStepCountDiffArray = new long[loggingCount];
            long[] totalStepCountDiffArray = new long[loggingCount];
            int i2 = 0;
            tmpNext2 = tmpNext;
            while (i2 < loggingCount) {
                try {
                    if ((packet.length - tmpNext2) - 20 < 0) {
                        CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                        this._mutex.unlock();
                        tmpNext = tmpNext2;
                        return -1;
                    }
                    tmpNext = tmpNext2 + 1;
                    i = (packet[tmpNext2] & 255) << 24;
                    tmpNext2 = tmpNext + 1;
                    i += (packet[tmpNext] & 255) << 16;
                    tmpNext = tmpNext2 + 1;
                    i += (packet[tmpNext2] & 255) << 8;
                    tmpNext2 = tmpNext + 1;
                    timeStampArray[i2] = CaTimeManager.getInstance().getTimeStampForUTC24(curUtcTime, curTimeMillis, (long) (i + (packet[tmpNext] & 255)));
                    tmpNext = tmpNext2 + 1;
                    i = (packet[tmpNext2] & 255) << 8;
                    tmpNext2 = tmpNext + 1;
                    distanceDiffArray[i2] = ((double) (i + (packet[tmpNext] & 255))) / 100.0d;
                    tmpNext = tmpNext2 + 1;
                    i = (packet[tmpNext2] & 255) << 8;
                    tmpNext2 = tmpNext + 1;
                    calorieDiffArray[i2] = ((double) (i + (packet[tmpNext] & 255))) / 100.0d;
                    tmpNext = tmpNext2 + 1;
                    walkStepCountDiffArray[i2] = (long) CaConvertUtil.getCompleteOfTwo(packet[tmpNext2]);
                    tmpNext2 = tmpNext + 1;
                    i = (packet[tmpNext] & 255) << 8;
                    tmpNext = tmpNext2 + 1;
                    runStepCountDiffArray[i2] = (long) (i + (packet[tmpNext2] & 255));
                    tmpNext2 = tmpNext + 1;
                    walkUpStepCountDiffArray[i2] = (long) CaConvertUtil.getCompleteOfTwo(packet[tmpNext]);
                    tmpNext = tmpNext2 + 1;
                    walkDownStepCountDiffArray[i2] = (long) CaConvertUtil.getCompleteOfTwo(packet[tmpNext2]);
                    tmpNext2 = tmpNext + 1;
                    i = (packet[tmpNext] & 255) << 8;
                    tmpNext = tmpNext2 + 1;
                    try {
                        runUpStepCountDiffArray[i2] = (long) (i + (packet[tmpNext2] & 255));
                        tmpNext2 = tmpNext + 1;
                        i = (packet[tmpNext] & 255) << 8;
                        tmpNext = tmpNext2 + 1;
                        runDownStepCountDiffArray[i2] = (long) (i + (packet[tmpNext2] & 255));
                        tmpNext2 = tmpNext + 1;
                        speedArray[i2] = ((double) CaConvertUtil.getCompleteOfTwo(packet[tmpNext])) / 10.0d;
                        tmpNext = tmpNext2 + 1;
                        i = (packet[tmpNext2] & 255) << 8;
                        tmpNext2 = tmpNext + 1;
                        totalStepCountDiffArray[i2] = (long) (i + (packet[tmpNext] & 255));
                        distanceDiff += distanceDiffArray[i2];
                        calorieDiff += calorieDiffArray[i2];
                        speed += speedArray[i2];
                        totalStepCountDiff += totalStepCountDiffArray[i2];
                        walkStepCountDiff += walkStepCountDiffArray[i2];
                        runStepCountDiff += runStepCountDiffArray[i2];
                        walkUpStepCountDiff += walkUpStepCountDiffArray[i2];
                        walkDownStepCountDiff += walkDownStepCountDiffArray[i2];
                        runUpStepCountDiff += runUpStepCountDiffArray[i2];
                        runDownStepCountDiff += runDownStepCountDiffArray[i2];
                        this.mPedoHistory.updateBufferIndex(2);
                        this.mPedoHistory.putTimestamp(timeStampArray[i2]);
                        this.mPedoHistory.putTotalStepInfo(totalStepCountDiffArray[i2]);
                        this.mPedoHistory.putWalkStepInfo(walkStepCountDiffArray[i2]);
                        this.mPedoHistory.putRunStepInfo(runStepCountDiffArray[i2]);
                        this.mPedoHistory.putWalkUpStepInfo(walkUpStepCountDiffArray[i2]);
                        this.mPedoHistory.putRunUpStepInfo(runUpStepCountDiffArray[i2]);
                        this.mPedoHistory.putWalkDnStepInfo(walkDownStepCountDiffArray[i2]);
                        this.mPedoHistory.putRunDnStepInfo(runDownStepCountDiffArray[i2]);
                        this.mPedoHistory.putDistanceInfo(distanceDiffArray[i2]);
                        this.mPedoHistory.putCalorieInfo(calorieDiffArray[i2]);
                        this.mPedoHistory.putSpeedInfo(speedArray[i2]);
                        this.mPedoHistory.setDataMode(2);
                        i2++;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    tmpNext = tmpNext2;
                }
            }
            long upDownStepCountDiff = ((walkUpStepCountDiff + walkDownStepCountDiff) + runUpStepCountDiff) + runDownStepCountDiff;
            speed /= (double) loggingCount;
            getContextBean().putContext(names[0], walkStepCountDiff);
            this.accumulativeWalkStepCount += walkStepCountDiff;
            getContextBean().putContext(names[1], this.accumulativeWalkStepCount);
            getContextBean().putContext(names[2], runStepCountDiff);
            this.accumulativeRunStepCount += runStepCountDiff;
            getContextBean().putContext(names[3], this.accumulativeRunStepCount);
            getContextBean().putContext(names[4], upDownStepCountDiff);
            this.accumulativeUpDownStepCount += upDownStepCountDiff;
            getContextBean().putContext(names[5], this.accumulativeUpDownStepCount);
            getContextBean().putContext(names[6], totalStepCountDiff);
            this.accumulativeTotalStepCount += totalStepCountDiff;
            getContextBean().putContext(names[7], this.accumulativeTotalStepCount);
            getContextBean().putContext(names[8], distanceDiff);
            this.accumulativeDistance += distanceDiff;
            getContextBean().putContext(names[9], this.accumulativeDistance);
            getContextBean().putContext(names[10], speed);
            getContextBean().putContext(names[11], -1);
            getContextBean().putContext(names[12], calorieDiff);
            this.accumulativeCalorie += calorieDiff;
            getContextBean().putContext(names[13], this.accumulativeCalorie);
            getContextBean().putContext(names[14], 0.0d);
            getContextBean().putContext(names[15], walkUpStepCountDiff);
            this.accumulativeWalkUpStepCount += walkUpStepCountDiff;
            getContextBean().putContext(names[16], this.accumulativeWalkUpStepCount);
            getContextBean().putContext(names[17], walkDownStepCountDiff);
            this.accumulativeWalkDownStepCount += walkDownStepCountDiff;
            getContextBean().putContext(names[18], this.accumulativeWalkDownStepCount);
            getContextBean().putContext(names[19], runUpStepCountDiff);
            this.accumulativeRunUpStepCount += runUpStepCountDiff;
            getContextBean().putContext(names[20], this.accumulativeRunUpStepCount);
            getContextBean().putContext(names[21], runDownStepCountDiff);
            this.accumulativeRunDownStepCount += runDownStepCountDiff;
            getContextBean().putContext(names[22], this.accumulativeRunDownStepCount);
            getContextBean().putContext(names[23], loggingCount);
            getContextBean().putContext(names[24], timeStampArray);
            getContextBean().putContext(names[25], distanceDiffArray);
            getContextBean().putContext(names[26], calorieDiffArray);
            getContextBean().putContext(names[27], speedArray);
            getContextBean().putContext(names[28], walkStepCountDiffArray);
            getContextBean().putContext(names[29], runStepCountDiffArray);
            getContextBean().putContext(names[30], walkUpStepCountDiffArray);
            getContextBean().putContext(names[31], walkDownStepCountDiffArray);
            getContextBean().putContext(names[32], runUpStepCountDiffArray);
            getContextBean().putContext(names[33], runDownStepCountDiffArray);
            getContextBean().putContext(names[34], totalStepCountDiffArray);
            tmpNext = tmpNext2;
        } else if ((packet.length - tmpNext2) - 32 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            this._mutex.unlock();
            tmpNext = tmpNext2;
            return -1;
        } else {
            tmpNext = tmpNext2 + 1;
            i = (packet[tmpNext2] & 255) << 16;
            tmpNext2 = tmpNext + 1;
            i += (packet[tmpNext] & 255) << 8;
            tmpNext = tmpNext2 + 1;
            data = i + (packet[tmpNext2] & 255);
            super.getContextBean().putContext(names[0], (long) data);
            walkStepCountDiff = (long) data;
            this.accumulativeWalkStepCount += (long) data;
            super.getContextBean().putContext(names[1], this.accumulativeWalkStepCount);
            tmpNext2 = tmpNext + 1;
            i = (packet[tmpNext] & 255) << 16;
            tmpNext = tmpNext2 + 1;
            i += (packet[tmpNext2] & 255) << 8;
            tmpNext2 = tmpNext + 1;
            data = i + (packet[tmpNext] & 255);
            super.getContextBean().putContext(names[2], (long) data);
            runStepCountDiff = (long) data;
            this.accumulativeRunStepCount += (long) data;
            super.getContextBean().putContext(names[3], this.accumulativeRunStepCount);
            tmpNext = tmpNext2 + 1;
            i = (packet[tmpNext2] & 255) << 16;
            tmpNext2 = tmpNext + 1;
            i += (packet[tmpNext] & 255) << 8;
            tmpNext = tmpNext2 + 1;
            data = i + (packet[tmpNext2] & 255);
            super.getContextBean().putContext(names[4], (long) data);
            this.accumulativeUpDownStepCount += (long) data;
            super.getContextBean().putContext(names[5], this.accumulativeUpDownStepCount);
            tmpNext2 = tmpNext + 1;
            i = (packet[tmpNext] & 255) << 16;
            tmpNext = tmpNext2 + 1;
            i += (packet[tmpNext2] & 255) << 8;
            tmpNext2 = tmpNext + 1;
            data = i + (packet[tmpNext] & 255);
            super.getContextBean().putContext(names[6], (long) data);
            totalStepCountDiff = (long) data;
            this.accumulativeTotalStepCount += (long) data;
            super.getContextBean().putContext(names[7], this.accumulativeTotalStepCount);
            tmpNext = tmpNext2 + 1;
            i = (packet[tmpNext2] & 255) << 16;
            tmpNext2 = tmpNext + 1;
            i += (packet[tmpNext] & 255) << 8;
            tmpNext = tmpNext2 + 1;
            data = i + (packet[tmpNext2] & 255);
            super.getContextBean().putContext(names[8], ((double) data) / 100.0d);
            distanceDiff = ((double) data) / 100.0d;
            this.accumulativeDistance += ((double) data) / 100.0d;
            super.getContextBean().putContext(names[9], this.accumulativeDistance);
            tmpNext2 = tmpNext + 1;
            speed = ((double) CaConvertUtil.getCompleteOfTwo(packet[tmpNext])) / 10.0d;
            super.getContextBean().putContext(names[10], speed);
            tmpNext = tmpNext2 + 1;
            super.getContextBean().putContext(names[11], packet[tmpNext2]);
            tmpNext2 = tmpNext + 1;
            i = (packet[tmpNext] & 255) << 8;
            tmpNext = tmpNext2 + 1;
            data = i + (packet[tmpNext2] & 255);
            super.getContextBean().putContext(names[12], ((double) data) / 10.0d);
            calorieDiff = ((double) data) / 10.0d;
            this.accumulativeCalorie += ((double) data) / 10.0d;
            super.getContextBean().putContext(names[13], this.accumulativeCalorie);
            tmpNext2 = tmpNext + 1;
            super.getContextBean().putContext(names[14], ((double) packet[tmpNext]) / 10.0d);
            tmpNext = tmpNext2 + 1;
            i = (packet[tmpNext2] & 255) << 16;
            tmpNext2 = tmpNext + 1;
            i += (packet[tmpNext] & 255) << 8;
            tmpNext = tmpNext2 + 1;
            data = i + (packet[tmpNext2] & 255);
            super.getContextBean().putContext(names[15], (long) data);
            walkUpStepCountDiff = (long) data;
            this.accumulativeWalkUpStepCount += (long) data;
            super.getContextBean().putContext(names[16], this.accumulativeWalkUpStepCount);
            tmpNext2 = tmpNext + 1;
            i = (packet[tmpNext] & 255) << 16;
            tmpNext = tmpNext2 + 1;
            i += (packet[tmpNext2] & 255) << 8;
            tmpNext2 = tmpNext + 1;
            data = i + (packet[tmpNext] & 255);
            super.getContextBean().putContext(names[17], (long) data);
            walkDownStepCountDiff = (long) data;
            this.accumulativeWalkDownStepCount += (long) data;
            super.getContextBean().putContext(names[18], this.accumulativeWalkDownStepCount);
            tmpNext = tmpNext2 + 1;
            i = (packet[tmpNext2] & 255) << 16;
            tmpNext2 = tmpNext + 1;
            i += (packet[tmpNext] & 255) << 8;
            tmpNext = tmpNext2 + 1;
            data = i + (packet[tmpNext2] & 255);
            super.getContextBean().putContext(names[19], (long) data);
            runUpStepCountDiff = (long) data;
            this.accumulativeRunUpStepCount += (long) data;
            super.getContextBean().putContext(names[20], this.accumulativeRunUpStepCount);
            tmpNext2 = tmpNext + 1;
            i = (packet[tmpNext] & 255) << 16;
            tmpNext = tmpNext2 + 1;
            i += (packet[tmpNext2] & 255) << 8;
            tmpNext2 = tmpNext + 1;
            data = i + (packet[tmpNext] & 255);
            super.getContextBean().putContext(names[21], (long) data);
            runDownStepCountDiff = (long) data;
            this.accumulativeRunDownStepCount += (long) data;
            super.getContextBean().putContext(names[22], this.accumulativeRunDownStepCount);
            if (totalStepCountDiff > 0) {
                this.mPedoHistory.updateBufferIndex(1);
                this.mPedoHistory.putTotalStepInfoSingle(totalStepCountDiff);
                this.mPedoHistory.putWalkStepInfoSingle(walkStepCountDiff);
                this.mPedoHistory.putRunStepInfoSingle(runStepCountDiff);
                this.mPedoHistory.putWalkUpStepInfoSingle(walkUpStepCountDiff);
                this.mPedoHistory.putRunUpStepInfoSingle(runUpStepCountDiff);
                this.mPedoHistory.putWalkDnStepInfoSingle(walkDownStepCountDiff);
                this.mPedoHistory.putRunDnStepInfoSingle(runDownStepCountDiff);
                this.mPedoHistory.putDistanceInfoSingle(distanceDiff);
                this.mPedoHistory.putCalorieInfoSingle(calorieDiff);
                this.mPedoHistory.putSpeedInfoSingle(speed);
                this.mPedoHistory.setDataMode(1);
            }
            tmpNext = tmpNext2;
        }
        super.notifyObserver();
        this._mutex.unlock();
        CaLogger.info("parse end:" + tmpNext);
        return tmpNext;
        this._mutex.unlock();
        throw th;
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
        this._mutex.lock();
        try {
            this.accumulativeWalkStepCount = 0;
            this.accumulativeWalkUpStepCount = 0;
            this.accumulativeWalkDownStepCount = 0;
            this.accumulativeRunStepCount = 0;
            this.accumulativeRunUpStepCount = 0;
            this.accumulativeRunDownStepCount = 0;
            this.accumulativeUpDownStepCount = 0;
            this.accumulativeTotalStepCount = 0;
            this.accumulativeDistance = 0.0d;
            this.accumulativeCalorie = 0.0d;
            this.mPedoHistory.erase();
        } finally {
            this._mutex.unlock();
        }
    }

    protected final Bundle getInitContextBundle() {
        String[] names = getContextValueNames();
        Bundle contextBundle = new Bundle();
        contextBundle.putLong(names[0], 0);
        contextBundle.putLong(names[1], 0);
        contextBundle.putLong(names[2], 0);
        contextBundle.putLong(names[3], 0);
        contextBundle.putLong(names[4], 0);
        contextBundle.putLong(names[5], 0);
        contextBundle.putLong(names[6], 0);
        contextBundle.putLong(names[7], 0);
        contextBundle.putDouble(names[8], 0.0d);
        contextBundle.putDouble(names[9], 0.0d);
        contextBundle.putDouble(names[10], 0.0d);
        contextBundle.putInt(names[11], -1);
        contextBundle.putDouble(names[12], 0.0d);
        contextBundle.putDouble(names[13], 0.0d);
        contextBundle.putDouble(names[14], 0.0d);
        contextBundle.putLong(names[15], 0);
        contextBundle.putLong(names[16], 0);
        contextBundle.putLong(names[17], 0);
        contextBundle.putLong(names[18], 0);
        contextBundle.putLong(names[19], 0);
        contextBundle.putLong(names[20], 0);
        contextBundle.putLong(names[21], 0);
        contextBundle.putLong(names[22], 0);
        return contextBundle;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        byte[] dataPacket = new byte[1];
        if (property == 5) {
            CaLogger.info("Height");
            CaUserInfo.getInstance().setUserHeight(((Double) ((ContextAwarePropertyBundle) value).getValue()).doubleValue());
            dataPacket[0] = (byte) ((int) CaUserInfo.getInstance().getUserHeight());
            return sendCommonValueToSensorHub((byte) 18, dataPacket);
        } else if (property == 4) {
            CaLogger.info("Weight");
            CaUserInfo.getInstance().setUserWeight(((Double) ((ContextAwarePropertyBundle) value).getValue()).doubleValue());
            dataPacket[0] = (byte) ((int) CaUserInfo.getInstance().getUserWeight());
            return sendCommonValueToSensorHub((byte) 19, dataPacket);
        } else if (property == 6) {
            CaLogger.info("Gender");
            CaUserInfo.getInstance().setUserGender(((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue());
            dataPacket[0] = (byte) CaUserInfo.getInstance().getUserGender();
            return sendCommonValueToSensorHub((byte) 20, dataPacket);
        } else if (property == 17) {
            int count = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Delivery Count = " + Integer.toString(count));
            return sendCommonValueToSensorHub((byte) 21, CaConvertUtil.intToByteArr(count, 1));
        } else if (property != 2) {
            return false;
        } else {
            CaLogger.info("History Data");
            this._mutex.lock();
            try {
                sendHistoryStepBuffer();
                return true;
            } finally {
                this._mutex.unlock();
            }
        }
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final void enable() {
        CaLogger.trace();
        super.enable();
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
    }

    protected final void notifyInitContext() {
        if (ListenerListManager.getInstance().getUsedTotalCount(getContextType()) == 1) {
            super.notifyInitContext();
        }
    }

    protected final ITimeChangeObserver getTimeChangeObserver() {
        return this;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }
}
