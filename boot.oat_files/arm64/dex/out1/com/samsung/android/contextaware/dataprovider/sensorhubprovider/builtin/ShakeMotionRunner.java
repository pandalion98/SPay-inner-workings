package com.samsung.android.contextaware.dataprovider.sensorhubprovider.builtin;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.LibTypeProvider;
import com.samsung.android.contextaware.manager.ContextAwarePropertyBundle;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.CaConvertUtil;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class ShakeMotionRunner extends LibTypeProvider {
    private static final int DEFAULT_SHAKE_DURATION = 800;
    private static final int DEFAULT_SHAKE_STRENGTH = 2;
    private int mShakeDuration = DEFAULT_SHAKE_DURATION;
    private int mShakeStrength = 2;

    public ShakeMotionRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_SHAKE_MOTION.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) 13;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        packet = new byte[3];
        byte[] data = CaConvertUtil.intToByteArr(this.mShakeDuration, 2);
        packet[1] = data[0];
        packet[2] = data[1];
        return packet;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        boolean result = true;
        if (property == 14) {
            int shakeStrength = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Strength = " + Integer.toString(this.mShakeStrength));
            if (shakeStrength <= 0 || shakeStrength > 5) {
                CaLogger.warning("range error of shake strength (range : 1~5)");
                return false;
            }
            this.mShakeStrength = shakeStrength;
        } else if (property == 15) {
            int shakeDuration = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Duration = " + Integer.toString(this.mShakeDuration));
            if (shakeDuration <= 0 || shakeDuration > 5000) {
                CaLogger.warning("range error of shake duration (range : 1~5000)");
                return false;
            }
            this.mShakeDuration = shakeDuration;
        } else {
            result = false;
        }
        return result;
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
