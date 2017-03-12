package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.CaCurrentUtcTimeManager;
import com.samsung.android.contextaware.utilbundle.ITimeChangeObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.nio.ByteBuffer;

public class ActiveTimeRunner extends LibTypeProvider {

    private enum ContextValIndex {
        DataType(0),
        ActiveTime(1);
        
        private int val;

        private ContextValIndex(int v) {
            this.val = v;
        }
    }

    public ActiveTimeRunner(int version, Context context, Looper looper, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_ACTIVE_TIME.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_STEP_LEVEL_MONITOR_SERVICE;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        packet = new byte[8];
        int[] utcTime = CaCurrentUtcTimeManager.getInstance().getUtcTime();
        packet[5] = CaConvertUtil.intToByteArr(utcTime[0], 1)[0];
        packet[6] = CaConvertUtil.intToByteArr(utcTime[1], 1)[0];
        packet[7] = CaConvertUtil.intToByteArr(utcTime[2], 1)[0];
        return packet;
    }

    protected final byte[] getDataPacketToUnregisterLib() {
        return new byte[]{(byte) 2};
    }

    public final <E> boolean setPropertyValue(int property, E e) {
        return true;
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        String[] names = getContextValueNames();
        CaLogger.info("parse:" + next);
        if ((packet.length - tmpNext) - 1 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        int tmpNext2 = tmpNext + 1;
        int dataType = packet[tmpNext];
        if (dataType != 2) {
            CaLogger.error(SensorHubErrors.ERROR_TYPE_VALUE.getMessage());
            tmpNext = tmpNext2;
        } else if (packet.length - tmpNext2 < 4) {
            CaLogger.debug("packet len:" + packet.length + " tmpNext:" + tmpNext2);
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            tmpNext = tmpNext2;
            return -1;
        } else {
            r5 = new byte[4];
            tmpNext = tmpNext2 + 1;
            r5[0] = packet[tmpNext2];
            tmpNext2 = tmpNext + 1;
            r5[1] = packet[tmpNext];
            tmpNext = tmpNext2 + 1;
            r5[2] = packet[tmpNext2];
            tmpNext2 = tmpNext + 1;
            r5[3] = packet[tmpNext];
            getContextBean().putContext(names[ContextValIndex.ActiveTime.val], ByteBuffer.wrap(r5).getInt());
            getContextBean().putContext(names[ContextValIndex.DataType.val], dataType);
            super.notifyObserver();
            tmpNext = tmpNext2;
        }
        return tmpNext;
    }

    public final String[] getContextValueNames() {
        return new String[]{"DataType", "ActiveTimeDuration"};
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
