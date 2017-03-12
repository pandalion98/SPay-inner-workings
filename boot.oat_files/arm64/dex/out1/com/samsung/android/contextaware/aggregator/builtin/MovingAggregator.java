package com.samsung.android.contextaware.aggregator.builtin;

import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.aggregator.Aggregator;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.concurrent.CopyOnWriteArrayList;

public class MovingAggregator extends Aggregator {
    public static final int PEDESTRIAN_MOVE = 0;
    public static final int PEDESTRIAN_STOP = 1;
    public static final int UNKNOWN = -1;
    public static final int VEHICLE_MOVE = 2;
    public static final int VEHICLE_STOP = 3;
    private int mOldMode;
    private int mOldMove;
    private int mOldTransMethod;

    public MovingAggregator(int version, CopyOnWriteArrayList<ContextComponent> collectionList, ISensorHubResetObservable observable) {
        super(version, null, null, collectionList, observable);
    }

    protected final void initializeAggregator() {
        LocationAggregator locatoinAggregator = (LocationAggregator) getSubCollectionObj(ContextType.AGGREGATOR_LOCATION.getCode());
        if (locatoinAggregator != null) {
            locatoinAggregator.setPropertyValue(1, Integer.valueOf(10));
        }
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
        this.mOldMove = -1;
        this.mOldTransMethod = -1;
        this.mOldMode = -1;
        String[] names = getContextValueNames();
        super.getContextBean().putContext(names[0], this.mOldMove);
        super.getContextBean().putContext(names[1], this.mOldTransMethod);
        super.getContextBean().putContext(names[2], this.mOldMode);
        super.notifyObserver();
    }

    private void updatePedestrianStatus(Bundle context) {
        int move;
        int transMethod;
        switch (context.getInt("PedestrianStatus")) {
            case -1:
                move = -1;
                transMethod = -1;
                break;
            case 0:
                move = 1;
                transMethod = 0;
                break;
            case 1:
                move = 0;
                transMethod = 0;
                break;
            case 2:
                move = 1;
                transMethod = 1;
                break;
            case 3:
                move = 0;
                transMethod = 1;
                break;
            default:
                move = -1;
                transMethod = -1;
                break;
        }
        notifyMovingContext(move, transMethod, updateMovingMode(transMethod));
    }

    private void updatePedometerData(Bundle context) {
        this.mOldMode = context.getInt("StepStatus");
        notifyMovingContext(this.mOldMove, this.mOldTransMethod, updateMovingMode(this.mOldTransMethod));
    }

    private int updateMovingMode(int transMethod) {
        switch (this.mOldMode) {
            case -1:
            case 0:
                if (transMethod == 1) {
                    return 6;
                }
                return this.mOldMode;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return this.mOldMode;
            default:
                return -1;
        }
    }

    private void notifyMovingContext(int move, int transMethod, int mode) {
        String[] names = getContextValueNames();
        super.getContextBean().putContext(names[0], move);
        super.getContextBean().putContext(names[1], transMethod);
        super.getContextBean().putContext(names[2], mode);
        super.notifyObserver();
        this.mOldMove = move;
        this.mOldTransMethod = transMethod;
        this.mOldMode = mode;
    }

    public final String getContextType() {
        return ContextType.AGGREGATOR_MOVING.getCode();
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final String[] getContextValueNames() {
        return new String[]{"move", "transMethod", "mode"};
    }

    public final void updateContext(String type, Bundle context) {
        if (type.equals(ContextType.AGGREGATOR_LOCATION.getCode())) {
            updatePedestrianStatus(context);
        } else if (type.equals(ContextType.SENSORHUB_RUNNER_PEDOMETER.getCode())) {
            updatePedometerData(context);
        }
    }

    public final void enable() {
        CaLogger.trace();
    }

    public final void disable() {
        CaLogger.trace();
    }
}
