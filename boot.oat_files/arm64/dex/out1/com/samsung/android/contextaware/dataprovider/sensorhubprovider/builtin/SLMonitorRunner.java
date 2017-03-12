package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin.utils.SLMHistory;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaCurrentUtcTimeManager;
import com.samsung.android.contextaware.utilbundle.CaTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.fingerprint.FingerprintManager;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SLMonitorRunner extends LibTypeProvider {
    private static final int DEFAULT_POWER_STEP_START_DURATION = 300;
    private static final int DEFAULT_STEP_LEVEL_TYPE = 4;
    private final Lock _mutex = new ReentrantLock(true);
    private int mDuration = 300;
    private final SLMHistory mSLMHistory = SLMHistory.getInstance();
    private int mStepLevelType = 4;

    private enum ContextValIndex {
        DataType(0),
        Timestamp(1),
        DataCount(2),
        DataBundle(3),
        StepType(4),
        StepCount(5),
        Distance(6),
        Calorie(7),
        Duration(8),
        ActiveTime(9),
        TimeStampArray(10),
        HistoryMode(11);
        
        private int val;

        private ContextValIndex(int v) {
            this.val = v;
        }
    }

    public SLMonitorRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    private void sendHistorySLMBuffer() {
        CaLogger.warning("sendSLMHistoryData");
        String[] names = getContextValueNames();
        int loggingCount = this.mSLMHistory.getBufferSize();
        if (loggingCount == 0) {
            CaLogger.error("History Data Buffer is null!");
        }
        long[] timeStampArray = new long[loggingCount];
        int[] stepType = new int[loggingCount];
        int[] stepCount = new int[loggingCount];
        double[] distance = new double[loggingCount];
        double[] calorie = new double[loggingCount];
        int[] duration = new int[loggingCount];
        Bundle dataBundle = new Bundle();
        for (int i = 0; i < loggingCount; i++) {
            timeStampArray[i] = this.mSLMHistory.getTimeStampSingle(i);
            stepType[i] = this.mSLMHistory.getmStepTypeArraySingle(i);
            stepCount[i] = this.mSLMHistory.getmStepCountArraySingle(i);
            distance[i] = this.mSLMHistory.getmDistanceArraySingle(i);
            calorie[i] = this.mSLMHistory.getmCalorieArraySingle(i);
            duration[i] = this.mSLMHistory.getmDurationArraySingle(i);
        }
        dataBundle.putIntArray(names[ContextValIndex.StepType.val], stepType);
        dataBundle.putIntArray(names[ContextValIndex.StepCount.val], stepCount);
        dataBundle.putDoubleArray(names[ContextValIndex.Distance.val], distance);
        dataBundle.putDoubleArray(names[ContextValIndex.Calorie.val], calorie);
        dataBundle.putIntArray(names[ContextValIndex.Duration.val], duration);
        getContextBean().putContext(names[ContextValIndex.DataCount.val], loggingCount);
        getContextBean().putContext(names[ContextValIndex.TimeStampArray.val], timeStampArray);
        getContextBean().putContext(names[ContextValIndex.DataBundle.val], dataBundle);
        getContextBean().putContext(names[ContextValIndex.DataType.val], 1);
        getContextBean().putContext(names[ContextValIndex.HistoryMode.val], 1);
        Bundle logBundle = new Bundle();
        logBundle.putInt(names[ContextValIndex.DataCount.val], loggingCount);
        logBundle.putLongArray(names[ContextValIndex.TimeStampArray.val], timeStampArray);
        logBundle.putBundle(names[ContextValIndex.DataBundle.val], dataBundle);
        logBundle.putInt(names[ContextValIndex.DataType.val], 1);
        logBundle.putInt(names[ContextValIndex.HistoryMode.val], 1);
        display(logBundle);
        super.notifyObserver();
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_SL_MONITOR.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_STEP_LEVEL_MONITOR_SERVICE;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        packet = new byte[8];
        byte[] startDuration = CaConvertUtil.intToByteArr(this.mDuration, 2);
        packet[2] = startDuration[0];
        packet[3] = startDuration[1];
        packet[4] = (byte) 0;
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        packet[5] = CaConvertUtil.intToByteArr(utcTime[0], 1)[0];
        packet[6] = CaConvertUtil.intToByteArr(utcTime[1], 1)[0];
        packet[7] = CaConvertUtil.intToByteArr(utcTime[2], 1)[0];
        return packet;
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        return new byte[]{(byte) 0};
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property == 58) {
            this.mStepLevelType = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Step Level Type = " + Integer.toString(this.mStepLevelType));
            return true;
        } else if (property == 59) {
            this.mDuration = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Duration = " + Integer.toString(this.mDuration));
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_STEP_LEVEL_MONITOR_SERVICE, (byte) 4, CaConvertUtil.intToByteArr(this.mDuration, 2));
            return true;
        } else if (property != 4) {
            return false;
        } else {
            CaLogger.info("History Data");
            this._mutex.lock();
            try {
                sendHistorySLMBuffer();
                return true;
            } finally {
                this._mutex.unlock();
            }
        }
    }

    public final int parse(byte[] packet, int next) {
        Exception e;
        int tmpNext = next;
        String[] names = getContextValueNames();
        CaLogger.info("parse:" + next);
        if ((packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        int dataType = packet[tmpNext];
        if (dataType != 1) {
            CaLogger.error(SensorHubErrors.ERROR_TYPE_VALUE.getMessage());
            tmpNext = tmpNext2;
        } else if ((packet.length - tmpNext2) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            tmpNext = tmpNext2;
            return -1;
        } else {
            tmpNext = tmpNext2 + 1;
            int dataSize = packet[tmpNext2];
            CaLogger.debug("dataSize:" + dataSize);
            if (dataSize <= 0) {
                CaLogger.error(SensorHubErrors.ERROR_DATA_FIELD_PARSING.getMessage());
                return -1;
            } else if (packet.length - tmpNext < (dataSize * 12) + 4) {
                CaLogger.debug("packet len:" + packet.length + " tmpNext:" + tmpNext);
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            } else {
                byte[] bArr = new byte[8];
                bArr[0] = (byte) 0;
                bArr[1] = (byte) 0;
                bArr[2] = (byte) 0;
                bArr[3] = (byte) 0;
                tmpNext2 = tmpNext + 1;
                bArr[4] = packet[tmpNext];
                tmpNext = tmpNext2 + 1;
                bArr[5] = packet[tmpNext2];
                tmpNext2 = tmpNext + 1;
                bArr[6] = packet[tmpNext];
                tmpNext = tmpNext2 + 1;
                bArr[7] = packet[tmpNext2];
                long timestamp = ByteBuffer.wrap(bArr).getLong();
                Calendar calender = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
                int hour = calender.get(11);
                int minute = calender.get(12);
                timestamp = CaTimeManager.getInstance().getTimeStampForUTC24((long) (((((hour * 3600) + (minute * 60)) + calender.get(13)) * 1000) + calender.get(14)), calender.getTimeInMillis(), timestamp);
                int[] stepType = new int[dataSize];
                int[] stepCount = new int[dataSize];
                double[] distance = new double[dataSize];
                double[] calorie = new double[dataSize];
                int[] duration = new int[dataSize];
                long[] tempTimeStamp = new long[dataSize];
                tempTimeStamp[0] = timestamp;
                Bundle dataBundle = new Bundle();
                int i = 0;
                tmpNext2 = tmpNext;
                while (i < dataSize) {
                    tmpNext = tmpNext2 + 1;
                    stepType[i] = packet[tmpNext2];
                    bArr = new byte[4];
                    bArr[0] = (byte) 0;
                    bArr[1] = (byte) 0;
                    tmpNext2 = tmpNext + 1;
                    bArr[2] = packet[tmpNext];
                    tmpNext = tmpNext2 + 1;
                    try {
                        bArr[3] = packet[tmpNext2];
                        stepCount[i] = ByteBuffer.wrap(bArr).getInt();
                        bArr = new byte[4];
                        bArr[0] = (byte) 0;
                        tmpNext2 = tmpNext + 1;
                        bArr[1] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr[2] = packet[tmpNext2];
                        tmpNext2 = tmpNext + 1;
                        bArr[3] = packet[tmpNext];
                        distance[i] = ((double) ByteBuffer.wrap(bArr).getInt()) / 100.0d;
                        bArr = new byte[4];
                        bArr[0] = (byte) 0;
                        bArr[1] = (byte) 0;
                        tmpNext = tmpNext2 + 1;
                        bArr[2] = packet[tmpNext2];
                        tmpNext2 = tmpNext + 1;
                        bArr[3] = packet[tmpNext];
                        calorie[i] = ((double) ByteBuffer.wrap(bArr).getInt()) / 100.0d;
                        bArr = new byte[4];
                        tmpNext = tmpNext2 + 1;
                        bArr[0] = packet[tmpNext2];
                        tmpNext2 = tmpNext + 1;
                        bArr[1] = packet[tmpNext];
                        tmpNext = tmpNext2 + 1;
                        bArr[2] = packet[tmpNext2];
                        tmpNext2 = tmpNext + 1;
                        bArr[3] = packet[tmpNext];
                        duration[i] = ByteBuffer.wrap(bArr).getInt();
                        if (stepType[i] < 1 || stepType[i] > 5) {
                            throw new Exception("Invalid stepType : " + stepType[i]);
                        }
                        try {
                            this._mutex.lock();
                            this.mSLMHistory.putSLMData(tempTimeStamp[i], stepType[i], stepCount[i], distance[i], calorie[i], duration[i]);
                            this._mutex.unlock();
                            if (i < dataSize - 1) {
                                tempTimeStamp[i + 1] = tempTimeStamp[i] + ((long) duration[i]);
                            }
                            i++;
                        } catch (Exception e2) {
                            e = e2;
                            tmpNext = tmpNext2;
                        } catch (Throwable th) {
                            this._mutex.unlock();
                        }
                    } catch (Exception e3) {
                        e = e3;
                    }
                }
                dataBundle.putIntArray(names[ContextValIndex.StepType.val], stepType);
                dataBundle.putIntArray(names[ContextValIndex.StepCount.val], stepCount);
                dataBundle.putDoubleArray(names[ContextValIndex.Distance.val], distance);
                dataBundle.putDoubleArray(names[ContextValIndex.Calorie.val], calorie);
                dataBundle.putIntArray(names[ContextValIndex.Duration.val], duration);
                getContextBean().putContext(names[ContextValIndex.DataCount.val], dataSize);
                getContextBean().putContext(names[ContextValIndex.Timestamp.val], timestamp);
                getContextBean().putContext(names[ContextValIndex.DataBundle.val], dataBundle);
                getContextBean().putContext(names[ContextValIndex.DataType.val], dataType);
                Bundle logBundle = new Bundle();
                logBundle.putInt(names[ContextValIndex.DataCount.val], dataSize);
                logBundle.putLong(names[ContextValIndex.Timestamp.val], timestamp);
                logBundle.putBundle(names[ContextValIndex.DataBundle.val], dataBundle);
                logBundle.putInt(names[ContextValIndex.DataType.val], dataType);
                display(logBundle);
                super.notifyObserver();
                tmpNext = tmpNext2;
            }
        }
        return tmpNext;
        CaLogger.error("SLMonitor Runner Exception : " + e.getMessage().toString());
        return tmpNext;
    }

    public final String[] getContextValueNames() {
        return new String[]{"DataType", "TimeStamp", "DataCount", "DataBundle", "StepTypeArray", "StepCountArray", "DistanceArray", "CalorieArray", "DurationArray", "ActiveTimeDuration", "TimeStampArray", "HistoryMode"};
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

    public final void clear() {
        CaLogger.trace();
        this._mutex.lock();
        try {
            this.mSLMHistory.erase();
            super.clear();
        } finally {
            this._mutex.unlock();
        }
    }

    protected final ITimeChangeObserver getTimeChangeObserver() {
        return this;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    protected void display() {
    }

    private void display(Bundle context) {
        if (context != null && !context.isEmpty()) {
            CaLogger.debug("================= " + getContextType() + " =================");
            StringBuffer str = new StringBuffer();
            int dataType = context.getInt("DataType");
            if (dataType == 1) {
                int i;
                int count = context.getInt("DataCount");
                int historyType = context.getInt("HistoryMode", 0);
                str.append("DT=[" + dataType + "], ");
                str.append("DC=[" + count + "], ");
                if (historyType == 1) {
                    long[] timestampArray = context.getLongArray("TimeStampArray");
                    str.append("TS=[");
                    for (i = 0; i < count; i++) {
                        str.append(timestampArray[i]);
                        if (i != count - 1) {
                            str.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                        }
                    }
                    str.append("], ");
                } else {
                    str.append("TS=[" + context.getLong("TimeStamp") + "], ");
                }
                Bundle dataBundle = context.getBundle("DataBundle");
                if (dataBundle != null) {
                    int[] stepType = dataBundle.getIntArray("StepTypeArray");
                    int[] stepCount = dataBundle.getIntArray("StepCountArray");
                    double[] distance = dataBundle.getDoubleArray("DistanceArray");
                    double[] calorie = dataBundle.getDoubleArray("CalorieArray");
                    int[] duration = dataBundle.getIntArray("DurationArray");
                    if (stepType != null && stepCount != null && distance != null && calorie != null && duration != null) {
                        str.append("ST=[");
                        for (i = 0; i < count; i++) {
                            if (stepType[i] == 5) {
                                str.append("IN");
                            }
                            if (stepType[i] == 4) {
                                str.append("PO");
                            }
                            if (stepType[i] == 3) {
                                str.append("NO");
                            }
                            if (stepType[i] == 2) {
                                str.append("SE");
                            }
                            if (stepType[i] == 1) {
                                str.append("ST");
                            }
                            if (i != count - 1) {
                                str.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                            }
                        }
                        str.append("], ");
                        str.append("SC=[");
                        for (i = 0; i < count; i++) {
                            str.append(stepCount[i]);
                            if (i != count - 1) {
                                str.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                            }
                        }
                        str.append("], ");
                        str.append("DI=[");
                        for (i = 0; i < count; i++) {
                            str.append(distance[i]);
                            if (i != count - 1) {
                                str.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                            }
                        }
                        str.append("], ");
                        str.append("CA=[");
                        for (i = 0; i < count; i++) {
                            str.append(calorie[i]);
                            if (i != count - 1) {
                                str.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                            }
                        }
                        str.append("], ");
                        str.append("DU=[");
                        for (i = 0; i < count; i++) {
                            str.append(duration[i]);
                            if (i != count - 1) {
                                str.append(FingerprintManager.FINGER_PERMISSION_DELIMITER);
                            }
                        }
                        str.append("]");
                        if (historyType == 1) {
                            str.append(", HistoryMode=[" + historyType + "], ");
                        }
                    } else {
                        return;
                    }
                }
                return;
            } else if (dataType == 2) {
                int activeTime = context.getInt("ActiveTimeDuration");
                str.append("DT=[" + dataType + "], ");
                str.append("DU=[" + activeTime + "]");
            }
            CaLogger.info(str.toString());
        }
    }
}
