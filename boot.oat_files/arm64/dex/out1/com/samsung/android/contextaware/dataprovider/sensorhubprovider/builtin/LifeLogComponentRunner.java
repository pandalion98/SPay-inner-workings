package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ICurrentPositionRequestObserver;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.IPassiveCurrrentPositionObserver;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin.CurrentPositionRequestRunner;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin.CurrentPositionRequestRunner.Position;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin.ICurrentPositionRequest;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.request.builtin.ISensorHubRequestParser;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaCurrentUtcTimeManager;
import com.samsung.android.contextaware.utilbundle.CaPassiveCurrentPositionManager;
import com.samsung.android.contextaware.utilbundle.CaTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Calendar;
import java.util.SimpleTimeZone;

public class LifeLogComponentRunner extends LibTypeProvider implements ICurrentPositionRequestObserver, IPassiveCurrrentPositionObserver {
    private static final int DEFAULT_BATCHING_PERIOD = 65535;
    private static final int DEFAULT_STOP_PERIOD = 300;
    private static final int DEFAULT_WAIT_PERIOD = 1500;
    private final ICurrentPositionRequest mCurrentPositionRequest;
    protected final CaPassiveCurrentPositionManager mPassiveCurrentPosition;
    private int mStopPeriod = 300;
    private int mWaitPeriod = DEFAULT_WAIT_PERIOD;

    public LifeLogComponentRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
        this.mPassiveCurrentPosition = new CaPassiveCurrentPositionManager(context, looper, this);
        this.mCurrentPositionRequest = new CurrentPositionRequestRunner(getContext(), getLooper());
        this.mCurrentPositionRequest.registerObserver(this);
        addRequestParser((ISensorHubRequestParser) this.mCurrentPositionRequest);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_LIFE_LOG_COMPONENT_SERVICE;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        packet = new byte[9];
        byte[] data = CaConvertUtil.intToByteArr(this.mStopPeriod, 2);
        packet[0] = data[0];
        packet[1] = data[1];
        data = CaConvertUtil.intToByteArr(this.mWaitPeriod, 2);
        packet[2] = data[0];
        packet[3] = data[1];
        data = CaConvertUtil.intToByteArr(65535, 2);
        packet[4] = data[0];
        packet[5] = data[1];
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        packet[6] = CaConvertUtil.intToByteArr(utcTime[0], 1)[0];
        packet[7] = CaConvertUtil.intToByteArr(utcTime[1], 1)[0];
        packet[8] = CaConvertUtil.intToByteArr(utcTime[2], 1)[0];
        return packet;
    }

    public final String[] getContextValueNames() {
        return new String[]{"StayingAreaCount", "StayingAreaTimeStamp", "StayingAreaLatitude", "StayingAreaLongitude", "StayingAreaAltitude", "StayingAreaTimeDuration", "StayingAreaRadius", "StayingAreaStatus", "MovingCount", "MovingTimeStamp", "MovingActivity", "MovingAccuracy", "MovingTimeDuration"};
    }

    public int parse(byte[] packet, int next) {
        int tmpNext = next;
        Calendar calender = Calendar.getInstance(new SimpleTimeZone(0, "GMT"));
        int hour = calender.get(11);
        int minute = calender.get(12);
        long curUtcTime = (long) (((((hour * 3600) + (minute * 60)) + calender.get(13)) * 1000) + calender.get(14));
        long curTimeMillis = calender.getTimeInMillis();
        CaLogger.info("parse start:" + tmpNext);
        tmpNext = parseForStayingArea(packet, tmpNext, curUtcTime, curTimeMillis);
        if (tmpNext <= 0) {
            return tmpNext;
        }
        tmpNext = parseForMoving(packet, tmpNext, curUtcTime, curTimeMillis);
        if (tmpNext <= 0) {
            return tmpNext;
        }
        super.notifyObserver();
        CaLogger.info("parse end:" + tmpNext);
        return tmpNext;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property == 28) {
            int stopPeriod = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("StopPeriod = " + Integer.toString(stopPeriod));
            this.mStopPeriod = stopPeriod;
            return sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_LIFE_LOG_COMPONENT_SERVICE, (byte) 1, CaConvertUtil.intToByteArr(stopPeriod, 2));
        } else if (property == 29) {
            int waitPeriod = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("WaitPeriod = " + Integer.toString(waitPeriod));
            this.mWaitPeriod = waitPeriod;
            return sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_LIFE_LOG_COMPONENT_SERVICE, (byte) 2, CaConvertUtil.intToByteArr(waitPeriod, 2));
        } else if (property == 30) {
            int stayingRadius = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("StayingRadius = " + Integer.toString(stayingRadius));
            return sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_LIFE_LOG_COMPONENT_SERVICE, (byte) 3, CaConvertUtil.intToByteArr(stayingRadius, 2));
        } else if (property != 31) {
            return false;
        } else {
            int stayingAreaRadius = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("StayingAreaRadius = " + Integer.toString(stayingAreaRadius));
            return sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_LIFE_LOG_COMPONENT_SERVICE, (byte) 4, CaConvertUtil.intToByteArr(stayingAreaRadius, 2));
        }
    }

    protected void display() {
    }

    private boolean checkStayingAreaPacket(byte[] packet, int next) {
        return next + 23 <= packet.length;
    }

    private boolean checkMovingPacket(byte[] packet, int next) {
        return next + 5 <= packet.length;
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    private int parseForStayingArea(byte[] packet, int next, long curUtcTime, long curTimeMillis) {
        int tmpNext = next;
        if ((packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        int stayingAreaCount = packet[tmpNext];
        if (stayingAreaCount <= 0) {
            tmpNext = tmpNext2;
            return tmpNext2;
        }
        long[] stayingAreaTimeStamp = new long[stayingAreaCount];
        double[] stayingAreaLatitude = new double[stayingAreaCount];
        double[] stayingAreaLongitude = new double[stayingAreaCount];
        double[] stayingAreaAltitude = new double[stayingAreaCount];
        int[] stayingAreaTimeDuration = new int[stayingAreaCount];
        int[] stayingAreaRadius = new int[stayingAreaCount];
        int[] stayingAreaStatus = new int[stayingAreaCount];
        int i = 0;
        while (i < stayingAreaCount) {
            if (checkStayingAreaPacket(packet, tmpNext2) || i < stayingAreaCount - 1) {
                tmpNext = tmpNext2 + 1;
                tmpNext2 = tmpNext + 1;
                tmpNext = tmpNext2 + 1;
                tmpNext2 = tmpNext + 1;
                stayingAreaTimeStamp[i] = CaTimeManager.getInstance().getTimeStampForUTC(curUtcTime, curTimeMillis, (long) (((((packet[tmpNext2] & 255) << 24) + ((packet[tmpNext] & 255) << 16)) + ((packet[tmpNext2] & 255) << 8)) + (packet[tmpNext] & 255)));
                tmpNext = tmpNext2 + 1;
                tmpNext2 = tmpNext + 1;
                tmpNext = tmpNext2 + 1;
                tmpNext2 = tmpNext + 1;
                stayingAreaLatitude[i] = ((double) (((((packet[tmpNext2] & 255) << 24) + ((packet[tmpNext] & 255) << 16)) + ((packet[tmpNext2] & 255) << 8)) + (packet[tmpNext] & 255))) / 1000000.0d;
                tmpNext = tmpNext2 + 1;
                tmpNext2 = tmpNext + 1;
                tmpNext = tmpNext2 + 1;
                tmpNext2 = tmpNext + 1;
                stayingAreaLongitude[i] = ((double) (((((packet[tmpNext2] & 255) << 24) + ((packet[tmpNext] & 255) << 16)) + ((packet[tmpNext2] & 255) << 8)) + (packet[tmpNext] & 255))) / 1000000.0d;
                tmpNext = tmpNext2 + 1;
                tmpNext2 = tmpNext + 1;
                tmpNext = tmpNext2 + 1;
                tmpNext2 = tmpNext + 1;
                stayingAreaAltitude[i] = ((double) (((((packet[tmpNext2] & 255) << 24) + ((packet[tmpNext] & 255) << 16)) + ((packet[tmpNext2] & 255) << 8)) + (packet[tmpNext] & 255))) / 1000.0d;
                tmpNext = tmpNext2 + 1;
                tmpNext2 = tmpNext + 1;
                tmpNext = tmpNext2 + 1;
                tmpNext2 = tmpNext + 1;
                stayingAreaTimeDuration[i] = ((((packet[tmpNext2] & 255) << 24) + ((packet[tmpNext] & 255) << 16)) + ((packet[tmpNext2] & 255) << 8)) + (packet[tmpNext] & 255);
                tmpNext = tmpNext2 + 1;
                tmpNext2 = tmpNext + 1;
                stayingAreaRadius[i] = ((packet[tmpNext2] & 255) << 8) + (packet[tmpNext] & 255);
                tmpNext = tmpNext2 + 1;
                stayingAreaStatus[i] = packet[tmpNext2];
                i++;
                tmpNext2 = tmpNext;
            } else {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                tmpNext = tmpNext2;
                return -1;
            }
        }
        String[] names = getContextValueNames();
        getContextBean().putContext(names[0], stayingAreaCount);
        getContextBean().putContext(names[1], stayingAreaTimeStamp);
        getContextBean().putContext(names[2], stayingAreaLatitude);
        getContextBean().putContext(names[3], stayingAreaLongitude);
        getContextBean().putContext(names[4], stayingAreaAltitude);
        getContextBean().putContext(names[5], stayingAreaTimeDuration);
        getContextBean().putContext(names[6], stayingAreaRadius);
        getContextBean().putContext(names[7], stayingAreaStatus);
        tmpNext = tmpNext2;
        return tmpNext2;
    }

    private int parseForMoving(byte[] packet, int next, long curUtcTime, long curTimeMillis) {
        int tmpNext = next;
        if ((packet.length - tmpNext) - 4 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        tmpNext = tmpNext2 + 1;
        tmpNext2 = tmpNext + 1;
        tmpNext = tmpNext2 + 1;
        long movingTimeStamp = CaTimeManager.getInstance().getTimeStampForUTC(curUtcTime, curTimeMillis, (long) (((((packet[tmpNext] & 255) << 24) + ((packet[tmpNext2] & 255) << 16)) + ((packet[tmpNext] & 255) << 8)) + (packet[tmpNext2] & 255)));
        if ((packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        tmpNext2 = tmpNext + 1;
        int movingCount = packet[tmpNext];
        if (movingCount <= 0) {
            tmpNext = tmpNext2;
            return tmpNext2;
        }
        int[] movingActivity = new int[movingCount];
        int[] movingAccuracy = new int[movingCount];
        int[] movingTimeDuration = new int[movingCount];
        int i = 0;
        while (i < movingCount) {
            if (!checkMovingPacket(packet, tmpNext2) || i > movingCount - 1) {
                CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
                tmpNext = tmpNext2;
                return -1;
            }
            tmpNext = tmpNext2 + 1;
            movingActivity[i] = packet[tmpNext2];
            tmpNext2 = tmpNext + 1;
            movingAccuracy[i] = packet[tmpNext];
            tmpNext = tmpNext2 + 1;
            tmpNext2 = tmpNext + 1;
            tmpNext = tmpNext2 + 1;
            movingTimeDuration[i] = (((packet[tmpNext2] & 255) << 16) + ((packet[tmpNext] & 255) << 8)) + (packet[tmpNext2] & 255);
            i++;
            tmpNext2 = tmpNext;
        }
        String[] names = getContextValueNames();
        getContextBean().putContext(names[8], movingCount);
        getContextBean().putContext(names[9], movingTimeStamp);
        getContextBean().putContext(names[10], movingActivity);
        getContextBean().putContext(names[11], movingAccuracy);
        getContextBean().putContext(names[12], movingTimeDuration);
        tmpNext = tmpNext2;
        return tmpNext2;
    }

    public final void enable() {
        CaLogger.trace();
        this.mPassiveCurrentPosition.enable();
        super.enable();
    }

    public final void disable() {
        CaLogger.trace();
        this.mPassiveCurrentPosition.disable();
        super.disable();
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    protected final ITimeChangeObserver getTimeChangeObserver() {
        return this;
    }

    public final void updatePosition(Position position) {
        if (!isDisable()) {
            if (position == null) {
                CaLogger.error(SensorHubErrors.getMessage(SensorHubErrors.ERROR_CURRENT_POSITION_NULL_EXCEPTION.getCode()));
                return;
            }
            Position pos = position;
            int latitude = (int) (pos.getLatitude() * 1000000.0d);
            int longitude = (int) (pos.getLongitude() * 1000000.0d);
            int altitude = (int) (pos.getAltitude() * 1000.0d);
            int accuracy = (int) pos.getAccuracy();
            int[] utcTime = pos.getUtcTime();
            int satelliteCount = pos.getSatelliteCount();
            int speed = (int) (pos.getSpeed() * 100.0f);
            int distance = (int) (pos.getDistance() * 1000.0d);
            int type = pos.getType();
            byte[] dataPacket = new byte[22];
            System.arraycopy(CaConvertUtil.intToByteArr(latitude, 4), 0, dataPacket, 0, 4);
            int size = 0 + 4;
            System.arraycopy(CaConvertUtil.intToByteArr(longitude, 4), 0, dataPacket, size, 4);
            size += 4;
            System.arraycopy(CaConvertUtil.intToByteArr(altitude, 4), 0, dataPacket, size, 4);
            size += 4;
            System.arraycopy(CaConvertUtil.intToByteArr(accuracy, 1), 0, dataPacket, size, 1);
            size++;
            System.arraycopy(CaConvertUtil.intToByteArr(utcTime[0], 1), 0, dataPacket, size, 1);
            size++;
            System.arraycopy(CaConvertUtil.intToByteArr(utcTime[1], 1), 0, dataPacket, size, 1);
            size++;
            System.arraycopy(CaConvertUtil.intToByteArr(utcTime[2], 1), 0, dataPacket, size, 1);
            size++;
            System.arraycopy(CaConvertUtil.intToByteArr(satelliteCount, 1), 0, dataPacket, size, 1);
            size++;
            System.arraycopy(CaConvertUtil.intToByteArr(speed, 2), 0, dataPacket, size, 2);
            size += 2;
            System.arraycopy(CaConvertUtil.intToByteArr(distance, 2), 0, dataPacket, size, 2);
            System.arraycopy(CaConvertUtil.intToByteArr(type, 1), 0, dataPacket, size + 2, 1);
            sendCommonValueToSensorHub((byte) 22, dataPacket);
        }
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }

    public void updatePassiveCurrentPosition(int type, int[] utcTime, double latitude, double longitude, double altitude, double distance, float speed, float accuracy, int satelliteCount) {
        if (!isDisable()) {
            CaLogger.debug("send the passive current position to SensorHub");
            int result = CaPassiveCurrentPositionManager.sendPositionToSensorHub(type, utcTime, latitude, longitude, altitude, distance, speed, accuracy, satelliteCount);
            if (result != SensorHubErrors.SUCCESS.getCode()) {
                CaLogger.error(SensorHubErrors.getMessage(result));
            }
        }
    }
}
