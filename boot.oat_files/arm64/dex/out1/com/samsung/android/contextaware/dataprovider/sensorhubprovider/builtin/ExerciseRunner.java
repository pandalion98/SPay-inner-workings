package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import com.samsung.android.smartclip.SmartClipMetaTagType;
import java.nio.ByteBuffer;

public class ExerciseRunner extends LibTypeProvider {
    private static final byte DATA_TYPE_BATCH = (byte) 0;
    private static final byte DATA_TYPE_GPS_STATUS = (byte) 1;
    private Context mContext = null;
    private boolean mLastGpsEnabled = false;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public final void onReceive(Context context, Intent intent) {
            int i = 1;
            if (intent.getAction().equals("android.location.PROVIDERS_CHANGED")) {
                boolean nowGpsEnabled = ExerciseRunner.this.isGpsEnabled();
                if (nowGpsEnabled != ExerciseRunner.this.mLastGpsEnabled) {
                    ExerciseRunner exerciseRunner = ExerciseRunner.this;
                    byte[] bArr = new byte[1];
                    if (!nowGpsEnabled) {
                        i = 0;
                    }
                    bArr[0] = (byte) i;
                    exerciseRunner.sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_EXERCISE_SERVICE, (byte) 37, bArr);
                    ExerciseRunner.this.mLastGpsEnabled = nowGpsEnabled;
                }
            }
        }
    };
    private int mSensorType = 0;
    private double mTotalPedoDistance = 0.0d;
    private long mTotalStepCount = 0;
    private long startTimeStamp = 0;

    private enum ContextName {
        TimeStamp((byte) 0),
        DataCount((byte) 1),
        Latitude((byte) 2),
        Longitude((byte) 3),
        Altitude((byte) 4),
        Pressure((byte) 5),
        StepCountDiff((byte) 6),
        PedoDistanceDiff((byte) 7),
        PedoSpeed((byte) 8),
        Speed((byte) 9),
        GpsStatus((byte) 10);
        
        private byte val;

        private ContextName(byte v) {
            this.val = v;
        }
    }

    public ExerciseRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
        this.mContext = context;
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_EXERCISE.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_EXERCISE_SERVICE;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return CaConvertUtil.intToByteArr(this.mSensorType, 1);
    }

    public final String[] getContextValueNames() {
        return new String[]{"TimeStampArray", "DataCount", "LatitudeArray", "LongitudeArray", "AltitudeArray", "PressureArray", "StepCountDiffArray", "PedoDistanceDiffArray", "PedoSpeedArray", "SpeedArray", "GpsStatus", "TotalStepCount", "TotalPedoDistance"};
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property == 63) {
            int dataType = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Exercise data type = " + Integer.toString(dataType));
            this.mSensorType |= dataType;
            byte[] bArr = new byte[1];
            bArr[0] = (byte) (isGpsEnabled() ? 1 : 0);
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_EXERCISE_SERVICE, (byte) 37, bArr);
            return true;
        } else if (property != 0) {
            return false;
        } else {
            sendCmdToSensorHub(ISensorHubCmdProtocol.INST_LIB_GETVALUE, getInstLibType(), new byte[]{(byte) 1, (byte) 0});
            return true;
        }
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        String[] names = getContextValueNames();
        if ((packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        int dataType = packet[tmpNext];
        if (dataType == 0) {
            if ((packet.length - tmpNext2) - 6 < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                tmpNext = tmpNext2;
                return -1;
            }
            r31 = new byte[8];
            tmpNext = tmpNext2 + 1;
            r31[4] = packet[tmpNext2];
            tmpNext2 = tmpNext + 1;
            r31[5] = packet[tmpNext];
            tmpNext = tmpNext2 + 1;
            r31[6] = packet[tmpNext2];
            tmpNext2 = tmpNext + 1;
            r31[7] = packet[tmpNext];
            long baseTimeStamp = ByteBuffer.wrap(r31).getLong() + (this.startTimeStamp + 1000);
            r31 = new byte[4];
            tmpNext = tmpNext2 + 1;
            r31[2] = packet[tmpNext2];
            tmpNext2 = tmpNext + 1;
            r31[3] = packet[tmpNext];
            int dataSize = ByteBuffer.wrap(r31).getInt();
            if (dataSize <= 0) {
                CaLogger.error(SensorHubErrors.ERROR_DATA_FIELD_PARSING.getMessage());
                tmpNext = tmpNext2;
                return -1;
            } else if ((packet.length - tmpNext2) - (dataSize * 21) < 0) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                tmpNext = tmpNext2;
                return -1;
            } else {
                long[] timeStamp = new long[dataSize];
                double[] latitude = new double[dataSize];
                double[] longitude = new double[dataSize];
                float[] altitude = new float[dataSize];
                float[] pressure = new float[dataSize];
                long[] stepCountDiff = new long[dataSize];
                double[] pedoDistanceDiff = new double[dataSize];
                double[] pedoSpeed = new double[dataSize];
                float[] speed = new float[dataSize];
                int i = 0;
                while (i < dataSize) {
                    long lat;
                    long lon;
                    timeStamp[i] = ((long) (i * 1000)) + baseTimeStamp;
                    tmpNext = tmpNext2 + 1;
                    byte byte1 = packet[tmpNext2];
                    tmpNext2 = tmpNext + 1;
                    byte byte2 = packet[tmpNext];
                    tmpNext = tmpNext2 + 1;
                    byte byte3 = packet[tmpNext2];
                    tmpNext2 = tmpNext + 1;
                    byte byte4 = packet[tmpNext];
                    tmpNext = tmpNext2 + 1;
                    byte midByte = packet[tmpNext2];
                    if ((byte1 & 128) == 128) {
                        lat = ByteBuffer.wrap(new byte[]{(byte) -1, (byte) -1, (byte) -1, byte1, byte2, byte3, byte4, midByte}).getLong() >> 4;
                    } else {
                        lat = ByteBuffer.wrap(new byte[]{(byte) 0, (byte) 0, (byte) 0, byte1, byte2, byte3, byte4, midByte}).getLong() >> 4;
                    }
                    latitude[i] = ((double) lat) / 1.0E8d;
                    tmpNext2 = tmpNext + 1;
                    byte1 = packet[tmpNext];
                    tmpNext = tmpNext2 + 1;
                    byte2 = packet[tmpNext2];
                    tmpNext2 = tmpNext + 1;
                    byte3 = packet[tmpNext];
                    tmpNext = tmpNext2 + 1;
                    byte4 = packet[tmpNext2];
                    if ((midByte & 8) == 8) {
                        lon = ByteBuffer.wrap(new byte[]{(byte) -1, (byte) -1, (byte) -1, (byte) ((midByte & 15) | 240), byte1, byte2, byte3, byte4}).getLong();
                    } else {
                        lon = ByteBuffer.wrap(new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) (midByte & 15), byte1, byte2, byte3, byte4}).getLong();
                    }
                    longitude[i] = ((double) lon) / 1.0E8d;
                    r31 = new byte[4];
                    r31[0] = (byte) 0;
                    tmpNext2 = tmpNext + 1;
                    r31[1] = packet[tmpNext];
                    tmpNext = tmpNext2 + 1;
                    r31[2] = packet[tmpNext2];
                    tmpNext2 = tmpNext + 1;
                    r31[3] = packet[tmpNext];
                    altitude[i] = (float) (((double) ByteBuffer.wrap(r31).getInt()) / 100.0d);
                    r31 = new byte[4];
                    r31[0] = (byte) 0;
                    tmpNext = tmpNext2 + 1;
                    r31[1] = packet[tmpNext2];
                    tmpNext2 = tmpNext + 1;
                    r31[2] = packet[tmpNext];
                    tmpNext = tmpNext2 + 1;
                    r31[3] = packet[tmpNext2];
                    pressure[i] = (float) (((double) ByteBuffer.wrap(r31).getInt()) / 1000.0d);
                    tmpNext2 = tmpNext + 1;
                    this.mTotalStepCount += (long) packet[tmpNext];
                    stepCountDiff[i] = this.mTotalStepCount;
                    double d = this.mTotalPedoDistance;
                    r31 = new byte[4];
                    tmpNext = tmpNext2 + 1;
                    r31[2] = packet[tmpNext2];
                    tmpNext2 = tmpNext + 1;
                    r31[3] = packet[tmpNext];
                    this.mTotalPedoDistance = d + (((double) ByteBuffer.wrap(r31).getInt()) / 100.0d);
                    pedoDistanceDiff[i] = this.mTotalPedoDistance;
                    tmpNext = tmpNext2 + 1;
                    byte1 = packet[tmpNext2];
                    tmpNext2 = tmpNext + 1;
                    pedoSpeed[i] = ((double) (ByteBuffer.wrap(new byte[]{(byte) 0, (byte) 0, byte1, packet[tmpNext]}).getInt() >> 4)) / 100.0d;
                    r31 = new byte[4];
                    r31[0] = (byte) 0;
                    r31[1] = (byte) 0;
                    r31[2] = (byte) (midByte & 15);
                    tmpNext = tmpNext2 + 1;
                    r31[3] = packet[tmpNext2];
                    speed[i] = (float) (((double) ByteBuffer.wrap(r31).getInt()) / 100.0d);
                    i++;
                    tmpNext2 = tmpNext;
                }
                super.getContextBean().putContext(names[ContextName.TimeStamp.val], timeStamp);
                super.getContextBean().putContext(names[ContextName.DataCount.val], dataSize);
                super.getContextBean().putContext(names[ContextName.Latitude.val], latitude);
                super.getContextBean().putContext(names[ContextName.Longitude.val], longitude);
                super.getContextBean().putContext(names[ContextName.Altitude.val], altitude);
                super.getContextBean().putContext(names[ContextName.Pressure.val], pressure);
                super.getContextBean().putContext(names[ContextName.StepCountDiff.val], stepCountDiff);
                super.getContextBean().putContext(names[ContextName.PedoDistanceDiff.val], pedoDistanceDiff);
                super.getContextBean().putContext(names[ContextName.PedoSpeed.val], pedoSpeed);
                super.getContextBean().putContext(names[ContextName.Speed.val], speed);
                super.notifyObserver();
                tmpNext = tmpNext2;
            }
        } else if (dataType != 1) {
            CaLogger.error(SensorHubErrors.ERROR_DATA_FIELD_PARSING.getMessage());
            tmpNext = tmpNext2;
            return -1;
        } else if ((packet.length - tmpNext2) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            tmpNext = tmpNext2;
            return -1;
        } else {
            tmpNext = tmpNext2 + 1;
            super.getContextBean().putContext(names[ContextName.GpsStatus.val], (short) packet[tmpNext2]);
            super.notifyObserver();
        }
        return tmpNext;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final void enable() {
        CaLogger.trace();
        this.mLastGpsEnabled = isGpsEnabled();
        this.mContext.registerReceiver(this.mReceiver, new IntentFilter("android.location.PROVIDERS_CHANGED"));
        this.startTimeStamp = System.currentTimeMillis();
        super.enable();
    }

    public final void disable() {
        CaLogger.trace();
        this.mContext.unregisterReceiver(this.mReceiver);
        super.disable();
    }

    public final void clear() {
        CaLogger.trace();
        this.mTotalStepCount = 0;
        this.mTotalPedoDistance = 0.0d;
        super.clear();
    }

    protected final ITimeChangeObserver getTimeChangeObserver() {
        return this;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    private boolean isGpsEnabled() {
        LocationManager manager = (LocationManager) this.mContext.getSystemService(SmartClipMetaTagType.LOCATION);
        if (manager != null) {
            return manager.isProviderEnabled("gps");
        }
        return false;
    }
}
