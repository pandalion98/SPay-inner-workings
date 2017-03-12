package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
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
import com.samsung.android.contextaware.utilbundle.CaCurrentUtcTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.nio.ByteBuffer;

public class SLMonitorExtendedInterruptRunner extends LibTypeProvider {
    private static final int DEFAULT_DEVICE_TYPE = 1;
    private static final int DEFAULT_STEP_LEVEL_DURATION = 300;
    private static final int DEFAULT_STEP_TYPE = 1;
    private int mDeviceType = 1;
    private int mDuration = 300;
    private int mNotiCount = 2;

    private enum ContextValIndex {
        InactiveStatus(0),
        IsTimeOut(1),
        Duration(2);
        
        private int val;

        private ContextValIndex(int v) {
            this.val = v;
        }
    }

    public SLMonitorExtendedInterruptRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_SL_MONITOR_EXTENDED_INTERRUPT.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_STEP_LEVEL_MONITOR_SERVICE;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        byte[] packet = new byte[8];
        packet[0] = (byte) 3;
        if (this.mDeviceType == 1) {
            packet[1] = (byte) 5;
        } else if (this.mDeviceType == 2) {
            packet[1] = (byte) 2;
        }
        byte[] duration = CaConvertUtil.intToByteArr(this.mDuration, 2);
        packet[2] = duration[0];
        packet[3] = duration[1];
        packet[4] = CaConvertUtil.intToByteArr(this.mNotiCount, 1)[0];
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        packet[5] = CaConvertUtil.intToByteArr(utcTime[0], 1)[0];
        packet[6] = CaConvertUtil.intToByteArr(utcTime[1], 1)[0];
        packet[7] = CaConvertUtil.intToByteArr(utcTime[2], 1)[0];
        return packet;
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        return new byte[]{(byte) 3};
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        String[] names = getContextValueNames();
        CaLogger.info("parse:" + next);
        if (packet.length - tmpNext < 6) {
            CaLogger.debug("packet len:" + packet.length + " tmpNext:" + tmpNext);
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        boolean isTimeOut;
        int tmpNext2 = tmpNext + 1;
        int inactiveStatus = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        if (packet[tmpNext2] == (byte) 0) {
            isTimeOut = false;
        } else {
            isTimeOut = true;
        }
        r7 = new byte[4];
        tmpNext2 = tmpNext + 1;
        r7[0] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r7[1] = packet[tmpNext2];
        tmpNext2 = tmpNext + 1;
        r7[2] = packet[tmpNext];
        tmpNext = tmpNext2 + 1;
        r7[3] = packet[tmpNext2];
        int duration = ByteBuffer.wrap(r7).getInt();
        getContextBean().putContext(names[ContextValIndex.InactiveStatus.val], inactiveStatus);
        getContextBean().putContext(names[ContextValIndex.IsTimeOut.val], isTimeOut);
        getContextBean().putContext(names[ContextValIndex.Duration.val], duration);
        super.notifyObserver();
        return tmpNext;
    }

    public final String[] getContextValueNames() {
        return new String[]{"InactiveStatus", "IsTimeOut", "InactiveTimeDuration"};
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property == 59) {
            this.mDuration = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Duration = " + Integer.toString(this.mDuration));
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_STEP_LEVEL_MONITOR_SERVICE, (byte) 5, CaConvertUtil.intToByteArr(this.mDuration, 2));
            return true;
        } else if (property == 60) {
            this.mNotiCount = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Notification count = " + Integer.toString(this.mNotiCount));
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_STEP_LEVEL_MONITOR_SERVICE, (byte) 1, CaConvertUtil.intToByteArr(this.mNotiCount, 1));
            return true;
        } else if (property == 57) {
            this.mDeviceType = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Device type = " + Integer.toString(this.mDeviceType));
            return true;
        } else if (property == 55) {
            int start = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Notification start = " + Integer.toString(start));
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_STEP_LEVEL_MONITOR_SERVICE, (byte) 2, CaConvertUtil.intToByteArr(start, 2));
            return true;
        } else if (property != 56) {
            return false;
        } else {
            int end = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Notification end = " + Integer.toString(end));
            sendPropertyValueToSensorHub((byte) 23, ISensorHubCmdProtocol.TYPE_STEP_LEVEL_MONITOR_SERVICE, (byte) 3, CaConvertUtil.intToByteArr(end, 2));
            return true;
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
}
