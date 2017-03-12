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

public class MovementForPositioningRunner extends LibTypeProvider {
    private static final double DEFAULT_MOVE_DISTANCE = 100.0d;
    private static final int DEFAULT_MOVE_DURATION = 20;
    private static final int DEFAULT_MOVE_MIN_DURATION = 5;
    private static final int DEFAULT_NOMOVE_DURATION = 60;
    private double mMoveDistanceThrs = DEFAULT_MOVE_DISTANCE;
    private int mMoveDurationThrs = 20;
    private int mMoveMinDurationThrs = 5;
    private int mNoMoveDurationThrs = 60;

    public MovementForPositioningRunner(int version, Context context, ISensorHubResetObservable observable) {
        super(version, context, null, observable);
    }

    public final String getContextType() {
        return ContextType.SENSORHUB_RUNNER_MOVEMENT_FOR_POSITIONING.getCode();
    }

    protected final byte getInstLibType() {
        return (byte) 9;
    }

    protected final byte[] getDataPacketToRegisterLib() {
        byte[] packet = new byte[7];
        byte[] data = CaConvertUtil.intToByteArr(this.mNoMoveDurationThrs, 2);
        packet[0] = data[0];
        packet[1] = data[1];
        data = CaConvertUtil.intToByteArr(this.mMoveDurationThrs, 2);
        packet[2] = data[0];
        packet[3] = data[1];
        data = CaConvertUtil.intToByteArr(this.mMoveMinDurationThrs, 2);
        packet[4] = data[0];
        packet[5] = data[1];
        packet[6] = (byte) ((int) this.mMoveDistanceThrs);
        return packet;
    }

    public final String[] getContextValueNames() {
        return new String[]{"Alert"};
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        if (property == 9) {
            this.mNoMoveDurationThrs = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Enter Threshold = " + Integer.toString(this.mNoMoveDurationThrs));
            return true;
        } else if (property == 10) {
            this.mMoveDurationThrs = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Exit Distance Threshold = " + Integer.toString(this.mMoveDurationThrs));
            return true;
        } else if (property == 11) {
            this.mMoveMinDurationThrs = ((Integer) ((ContextAwarePropertyBundle) value).getValue()).intValue();
            CaLogger.info("Exit Time Threshold = " + Integer.toString(this.mMoveMinDurationThrs));
            return true;
        } else if (property != 12) {
            return false;
        } else {
            this.mMoveDistanceThrs = ((Double) ((ContextAwarePropertyBundle) value).getValue()).doubleValue();
            CaLogger.info("Movement Threshold = " + Double.toString(this.mMoveDistanceThrs));
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
