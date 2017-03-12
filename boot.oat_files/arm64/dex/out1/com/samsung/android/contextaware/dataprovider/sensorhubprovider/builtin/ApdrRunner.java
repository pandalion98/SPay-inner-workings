package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
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
import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ApdrRunner extends LibTypeProvider {
    private static final int DEFAULT_WAKE_UP_STEP_COUNT = 500;
    private static final int DEFAULT_WAKE_UP_TIME_COUNT = 600;
    private static final int STEP_COUNT_HIGH = 50;
    private static final int STEP_COUNT_LOW = 500;
    private static final int STEP_COUNT_MEDIUM = 300;
    private static final int TIME_COUNT_HIGH = 60;
    private static final int TIME_COUNT_LOW = 600;
    private static final int TIME_COUNT_MEDIUM = 120;
    private static final int TIME_SYNC_TIMER = 7200;
    private int mLppResolution = 0;
    private ScheduledExecutorService mSyncSched = Executors.newSingleThreadScheduledExecutor();
    private final Time mSyncTime = new Time();
    private Runnable mSyncTimeTask = null;
    private int mWakeUpStepCount = 500;
    private int mWakeUpTimeCount = 600;

    public enum ContextValIndex {
        Alert(0),
        Count(1),
        Hour(2),
        Minute(3),
        Second(4),
        doe(5),
        TimeDifference(6),
        IncrementEast(7),
        IncrementNorth(8),
        ActivityType(9),
        StayingArea(10);
        
        private int val;

        private ContextValIndex(int v) {
            this.val = v;
        }

        public int index() {
            return this.val;
        }
    }

    public ApdrRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_APDR.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_APDR_SERVICE;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        return new byte[]{(byte) (this.mWakeUpStepCount / 5), (byte) (this.mWakeUpTimeCount / 5)};
    }

    public final Time getSyncTime() {
        return this.mSyncTime;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property != 32) {
            return false;
        }
        int resolution = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
        this.mLppResolution = resolution;
        setLppResolution(resolution);
        return true;
    }

    public final void sendSleepModeCmdToSensorHub(byte[] packet) {
        if (getInstLibType() >= (byte) 0 && packet != null && packet.length > 0) {
            byte[] dataPacket = new byte[(packet.length + 1)];
            byte[] mode = new byte[]{(byte) 1};
            System.arraycopy(mode, 0, dataPacket, 0, mode.length);
            System.arraycopy(packet, 0, dataPacket, mode.length, packet.length);
            super.sendCmdToSensorHub(ISensorHubCmdProtocol.INST_LIB_ADD, getInstLibType(), dataPacket);
        }
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        String[] names = getContextValueNames();
        Log.d("LppApdr", "parse:" + next);
        if ((packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        } else if (packet[tmpNext] == (byte) 2) {
            tmpNext++;
            super.getContextBean().putContext(names[ContextValIndex.StayingArea.val], packet[tmpNext]);
            super.notifyObserver();
            return tmpNext + 1;
        } else {
            int tmpNext2 = tmpNext + 1;
            if (packet[tmpNext] != (byte) 1) {
                CaLogger.error(SensorHubErrors.ERROR_DATA_FIELD_PARSING.getMessage());
                tmpNext = tmpNext2;
                return -1;
            }
            tmpNext = tmpNext2 + 1;
            int dataSize = packet[tmpNext2];
            CaLogger.debug("dataSize:" + dataSize);
            if (dataSize <= 0) {
                CaLogger.error(SensorHubErrors.ERROR_DATA_FIELD_PARSING.getMessage());
                return -1;
            } else if (packet.length - tmpNext < (dataSize * 5) + 4) {
                CaLogger.debug("packet len:" + packet.length + " tmpNext:" + tmpNext);
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                return -1;
            } else {
                tmpNext2 = tmpNext + 1;
                int hour = packet[tmpNext];
                tmpNext = tmpNext2 + 1;
                int minute = packet[tmpNext2];
                tmpNext2 = tmpNext + 1;
                int second = packet[tmpNext];
                tmpNext = tmpNext2 + 1;
                int doe = packet[tmpNext2];
                long[] timeDiff = new long[dataSize];
                int[] incEast = new int[dataSize];
                int[] incNorth = new int[dataSize];
                int[] activityType = new int[dataSize];
                int i = 0;
                tmpNext2 = tmpNext;
                while (i < dataSize) {
                    CaLogger.debug("packet length:" + packet.length + "  tmpNext:" + tmpNext2);
                    tmpNext = tmpNext2 + 1;
                    tmpNext2 = tmpNext + 1;
                    timeDiff[i] = (long) ((((packet[tmpNext2] & 255) << 8) + (packet[tmpNext] & 255)) * 100);
                    tmpNext = tmpNext2 + 1;
                    incEast[i] = packet[tmpNext2] * 10;
                    tmpNext2 = tmpNext + 1;
                    incNorth[i] = packet[tmpNext] * 10;
                    tmpNext = tmpNext2 + 1;
                    activityType[i] = packet[tmpNext2];
                    i++;
                    tmpNext2 = tmpNext;
                }
                super.getContextBean().putContext(names[ContextValIndex.Count.val], dataSize);
                super.getContextBean().putContext(names[ContextValIndex.Hour.val], hour);
                super.getContextBean().putContext(names[ContextValIndex.Minute.val], minute);
                super.getContextBean().putContext(names[ContextValIndex.Second.val], second);
                super.getContextBean().putContext(names[ContextValIndex.doe.val], doe);
                super.getContextBean().putContext(names[ContextValIndex.TimeDifference.val], timeDiff);
                super.getContextBean().putContext(names[ContextValIndex.IncrementEast.val], incEast);
                super.getContextBean().putContext(names[ContextValIndex.IncrementNorth.val], incNorth);
                super.getContextBean().putContext(names[ContextValIndex.ActivityType.val], activityType);
                super.notifyObserver();
                tmpNext = tmpNext2;
                return tmpNext2;
            }
        }
    }

    public final String[] getContextValueNames() {
        return new String[]{"Alert", "Count", "Hour", "Minute", "Second", "doe", "TimeDifference", "IncrementEast", "IncrementNorth", "ActivityType", "StayingArea"};
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
        this.mSyncTimeTask = new Runnable() {
            public void run() {
                ApdrRunner.this.sendCurTimeToSensorHub();
                ApdrRunner.this.mSyncSched = Executors.newSingleThreadScheduledExecutor();
                ApdrRunner.this.mSyncSched.schedule(ApdrRunner.this.mSyncTimeTask, 7200, TimeUnit.SECONDS);
            }
        };
    }

    public final void disable() {
        CaLogger.trace();
        super.disable();
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    protected final ITimeChangeObserver getTimeChangeObserver() {
        return this;
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    public final void setLppResolution(int res) {
        this.mLppResolution = res;
        if (res == 0) {
            this.mWakeUpStepCount = 500;
            this.mWakeUpTimeCount = 600;
        } else if (res == 1) {
            this.mWakeUpStepCount = 300;
            this.mWakeUpTimeCount = 120;
        } else if (res == 2) {
            this.mWakeUpStepCount = 50;
            this.mWakeUpTimeCount = 60;
        }
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 1, CaConvertUtil.intToByteArr(this.mWakeUpStepCount / 5, 1));
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 2, CaConvertUtil.intToByteArr(this.mWakeUpTimeCount / 5, 1));
    }

    public final void locationUpdate(Location loc) {
        int lat = (int) (loc.getLatitude() * 1000000.0d);
        int lon = (int) (loc.getLongitude() * 1000000.0d);
        int alt = (int) (loc.getAltitude() * 1000.0d);
        byte acc = (byte) ((int) loc.getAccuracy());
        Time t = new Time();
        t.set(loc.getTime());
        t.switchTimezone("GMT+00:00");
        Bundle bun = loc.getExtras();
        byte sat = (byte) 0;
        if (bun != null) {
            sat = (byte) bun.getInt("satellites");
            CaLogger.debug("satellites:" + sat);
        }
        int speed = (int) (loc.getSpeed() * 100.0f);
        byte type = (byte) 0;
        if (loc.getProvider() != null) {
            if (loc.getProvider().equals("gps")) {
                type = (byte) 1;
            } else if (loc.getProvider().equals("network")) {
                type = (byte) 2;
            } else if (loc.getProvider().equals("fused")) {
                type = (byte) 3;
            } else if (loc.getProvider().equals("GPS batch")) {
                type = (byte) 4;
            }
        }
        ByteBuffer data = ByteBuffer.allocate(22);
        data.put(CaConvertUtil.intToByteArr(lat, 4));
        data.put(CaConvertUtil.intToByteArr(lon, 4));
        data.put(CaConvertUtil.intToByteArr(alt, 4));
        data.put(acc);
        data.put((byte) t.hour);
        data.put((byte) t.minute);
        data.put((byte) t.second);
        data.put(sat);
        data.put(CaConvertUtil.intToByteArr(speed, 2));
        data.put(CaConvertUtil.intToByteArr(0, 2));
        data.put(type);
        Log.d("LPPApdrR", "loc time:" + loc.getTime());
        Log.d("LPPApdrR", "hr:" + t.hour + " min:" + t.minute + " sec:" + t.second);
        Log.d("LPPApdrR", "hr:" + ((byte) t.hour) + " min:" + ((byte) t.minute) + " sec:" + ((byte) t.second));
        sendCommonValueToSensorHub((byte) 22, data.array());
    }

    public final void setMagneticSensorOffset(int offsetX, int offsetY, int offsetZ) {
        byte[] data1 = CaConvertUtil.intToByteArr(offsetX, 2);
        byte[] data2 = CaConvertUtil.intToByteArr(offsetY, 2);
        byte[] data3 = CaConvertUtil.intToByteArr(offsetZ, 2);
        ByteBuffer data = ByteBuffer.allocate(6);
        data.put(data1);
        data.put(data2);
        data.put(data3);
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 3, data.array());
    }

    public final void gpsBatchStarted() {
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 5, new byte[]{(byte) 3});
    }

    public void gpsOnBatchStopped() {
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 5, new byte[]{(byte) 5});
    }

    public void gpsOffBatchStopped() {
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 5, new byte[]{(byte) 4});
    }

    public void gpsAvailable() {
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 5, new byte[]{(byte) 1});
    }

    public void gpsUnavailable() {
        sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_APDR_SERVICE, (byte) 5, new byte[]{(byte) 0});
    }

    private void sendCurTimeToSensorHub() {
        Time t = new Time();
        t.setToNow();
        t.switchTimezone("GMT+00:00");
        this.mSyncTime.set(t);
        byte[] data = new byte[]{(byte) 0, (byte) 0, (byte) 0};
        data[0] = (byte) t.hour;
        data[1] = (byte) t.minute;
        data[2] = (byte) t.second;
    }
}
