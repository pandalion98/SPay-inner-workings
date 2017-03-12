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
import com.samsung.android.smartface.SmartFaceManager;

public class FlatMotionForTableModeRunner extends LibTypeProvider {
    private static final int DEFAULT_DURATION = 500;
    private int mDuration = 500;

    public FlatMotionForTableModeRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public <E> boolean setPropertyValue(int property, E value) {
        boolean result = true;
        if (property == 61) {
            CaLogger.info("Duration = " + Integer.toString(this.mDuration) + SmartFaceManager.PAGE_TOP);
            int duration = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            if (this.mDuration <= 0) {
                CaLogger.warning("duration must be above 0.");
                return false;
            }
            CaLogger.info("Duration = " + Integer.toString(this.mDuration) + SmartFaceManager.PAGE_MIDDLE);
            this.mDuration = duration;
            CaLogger.info("Duration = " + Integer.toString(this.mDuration));
        } else {
            result = false;
            CaLogger.info("Duration = " + Integer.toString(this.mDuration) + SmartFaceManager.PAGE_BOTTOM);
        }
        CaLogger.info("Duration = " + Integer.toString(this.mDuration) + "2");
        return result;
    }

    protected byte[] getDataPacketToRegisterLib() {
        packet = new byte[2];
        byte[] duration = CaConvertUtil.intToByteArr(this.mDuration, 2);
        packet[0] = duration[0];
        packet[1] = duration[1];
        return packet;
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_FLAT_MOTION_FOR_TABLE_MODE.getCode();
    }

    protected final byte getInstLibType() {
        return ISensorHubCmdProtocol.TYPE_FLAT_MOTION_FOR_TABLE_MODE_SERVICE;
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
