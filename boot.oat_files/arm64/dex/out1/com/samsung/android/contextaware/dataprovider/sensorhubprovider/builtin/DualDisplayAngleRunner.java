package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.SensorHubErrors;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class DualDisplayAngleRunner extends LibTypeProvider {
    private int mOffAngle = 0;
    private int mOnAngle = 0;

    public DualDisplayAngleRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_DUAL_DISPLAY_ANGLE.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_DUAL_DISPLAY_ANGLE_SERVICE;
    }

    public final String[] getContextValueNames() {
        return new String[]{"Angle", "Type", "Intensity"};
    }

    public final int parse(byte[] packet, int next) {
        int tmpNext = next;
        if ((packet.length - tmpNext) - 2 < 0) {
            CaLogger.error(SensorHubErrors.ERROR_PACKET_LOST.getMessage());
            return -1;
        }
        String[] names = getContextValueNames();
        int tmpNext2 = tmpNext + 1;
        tmpNext = tmpNext2 + 1;
        short angle = (short) ((packet[tmpNext] & 255) + ((packet[tmpNext2] & 255) << 8));
        tmpNext2 = tmpNext + 1;
        tmpNext = tmpNext2 + 1;
        short type = (short) ((packet[tmpNext] & 255) + ((packet[tmpNext2] & 255) << 8));
        tmpNext2 = tmpNext + 1;
        tmpNext = tmpNext2 + 1;
        short intensity = (short) ((packet[tmpNext] & 255) + ((packet[tmpNext2] & 255) << 8));
        super.getContextBean().putContext(names[0], angle);
        super.getContextBean().putContext(names[1], type);
        super.getContextBean().putContext(names[2], intensity);
        super.notifyObserver();
        return tmpNext;
    }

    protected byte[] getDataPacketToRegisterLib() {
        packet = new byte[4];
        byte[] onAngle = CaConvertUtil.intToByteArr(this.mOnAngle, 2);
        byte[] offAngle = CaConvertUtil.intToByteArr(this.mOffAngle, 2);
        packet[0] = onAngle[0];
        packet[1] = onAngle[1];
        packet[2] = offAngle[0];
        packet[3] = offAngle[1];
        return packet;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property == 76) {
            int onAngle = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("onAngle = " + Integer.toString(onAngle));
            this.mOnAngle = onAngle;
            return true;
        } else if (property != 77) {
            return false;
        } else {
            int offAngle = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("offAngle = " + Integer.toString(offAngle));
            this.mOffAngle = offAngle;
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

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }
}
