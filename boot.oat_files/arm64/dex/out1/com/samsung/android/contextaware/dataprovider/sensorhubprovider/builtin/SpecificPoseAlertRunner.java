package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubCmdProtocol;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class SpecificPoseAlertRunner extends LibTypeProvider {
    private static final int DEFAULT_MAXIMUM_ANGLE = 90;
    private static final int DEFAULT_MINIMUM_ANGLE = -90;
    private static final int DEFAULT_MOVING_THRS = 1;
    private static final int DEFAULT_RETENTION_TIME = 1;
    private int mMaximumAngle = 90;
    private int mMinimumAngle = DEFAULT_MINIMUM_ANGLE;
    private int mMovingThrs = 1;
    private int mRetentionTime = 1;

    public SpecificPoseAlertRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_SPECIFIC_POSE_ALERT.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_SPECIFIC_POSE_ALERT_SERVICE;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        packet = new byte[6];
        byte[] data = CaConvertUtil.intToByteArr(this.mRetentionTime, 2);
        byte[] thrs = CaConvertUtil.intToByteArr(this.mMovingThrs, 2);
        packet[0] = data[0];
        packet[1] = data[1];
        packet[2] = (byte) this.mMinimumAngle;
        packet[3] = (byte) this.mMaximumAngle;
        packet[4] = thrs[0];
        packet[5] = thrs[1];
        return packet;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property == 33) {
            this.mRetentionTime = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Retention Time = " + Integer.toString(this.mRetentionTime));
            return true;
        } else if (property == 34) {
            this.mMinimumAngle = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Minimum Angle = " + Integer.toString(this.mMinimumAngle));
            return true;
        } else if (property == 35) {
            this.mMaximumAngle = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Maximum Angle = " + Integer.toString(this.mMaximumAngle));
            return true;
        } else if (property != 36) {
            return false;
        } else {
            this.mMovingThrs = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Moving Thrs = " + Integer.toString(this.mMovingThrs));
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
