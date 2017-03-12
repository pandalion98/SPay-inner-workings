package com.samsung.android.contextaware.aggregator.builtin;

import android.os.Bundle;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.aggregator.Aggregator;
import com.samsung.android.contextaware.aggregator.AggregatorErrors;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.IApPowerObserver;
import com.samsung.android.contextaware.manager.ISensorHubResetObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.concurrent.CopyOnWriteArrayList;

public class LifeLogAggregator extends Aggregator {
    public LifeLogAggregator(int version, CopyOnWriteArrayList<ContextComponent> collectionList, ISensorHubResetObservable observable) {
        super(version, null, null, collectionList, observable);
    }

    public final synchronized void updateContext(String type, Bundle context) {
        if (type != null) {
            if (!type.isEmpty()) {
                if (context == null) {
                    CaLogger.error(AggregatorErrors.getMessage(AggregatorErrors.ERROR_UPDATED_CONTEXT_NULL_EXCEPTION.getCode()));
                } else {
                    CaLogger.info("updateContext:" + type);
                    String[] names;
                    ContextComponent obj;
                    String[] keys;
                    if (type.equals(ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT.getCode())) {
                        if (context.getInt("StayingAreaCount") > 0) {
                            names = getContextValueNames();
                            getContextBean().putContext(names[0], 1);
                            getContextBean().putContext(names[1], context);
                            obj = getSubCollectionObj(ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT.getCode());
                            if (obj == null) {
                                CaLogger.error("Sub-collection object is null");
                            } else {
                                keys = obj.getContextValueNames();
                                getContextBean().putContextForDisplay(keys[0], context.getInt(keys[0]));
                                getContextBean().putContextForDisplay(keys[1], context.getLongArray(keys[1]));
                                getContextBean().putContextForDisplay(keys[2], context.getDoubleArray(keys[2]));
                                getContextBean().putContextForDisplay(keys[3], context.getDoubleArray(keys[3]));
                                getContextBean().putContextForDisplay(keys[4], context.getDoubleArray(keys[4]));
                                getContextBean().putContextForDisplay(keys[5], context.getIntArray(keys[5]));
                                getContextBean().putContextForDisplay(keys[6], context.getIntArray(keys[6]));
                                getContextBean().putContextForDisplay(keys[7], context.getIntArray(keys[7]));
                                notifyObserver();
                            }
                        }
                        if (context.getInt("MovingCount") > 0) {
                            names = getContextValueNames();
                            getContextBean().putContext(names[0], 2);
                            getContextBean().putContext(names[1], context);
                            obj = getSubCollectionObj(ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT.getCode());
                            if (obj == null) {
                                CaLogger.error("Sub-collection object is null");
                            } else {
                                keys = obj.getContextValueNames();
                                getContextBean().putContextForDisplay(keys[8], context.getInt(keys[8]));
                                getContextBean().putContextForDisplay(keys[9], context.getLong(keys[9]));
                                getContextBean().putContextForDisplay(keys[10], context.getIntArray(keys[10]));
                                getContextBean().putContextForDisplay(keys[11], context.getIntArray(keys[11]));
                                getContextBean().putContextForDisplay(keys[12], context.getIntArray(keys[12]));
                                notifyObserver();
                            }
                        }
                    } else if (type.equals(ContextType.AGGREGATOR_LPP.getCode())) {
                        names = getContextValueNames();
                        getContextBean().putContext(names[0], 3);
                        getContextBean().putContext(names[1], context);
                        obj = getSubCollectionObj(ContextType.AGGREGATOR_LPP.getCode());
                        if (obj == null) {
                            CaLogger.error("Sub-collection object is null");
                        } else {
                            keys = obj.getContextValueNames();
                            getContextBean().putContextForDisplay(keys[0], context.getInt(keys[0]));
                            getContextBean().putContextForDisplay(keys[1], context.getLongArray(keys[1]));
                            getContextBean().putContextForDisplay(keys[2], context.getDoubleArray(keys[2]));
                            getContextBean().putContextForDisplay(keys[3], context.getDoubleArray(keys[3]));
                            getContextBean().putContextForDisplay(keys[4], context.getDoubleArray(keys[4]));
                            notifyObserver();
                        }
                    }
                }
            }
        }
        CaLogger.error(AggregatorErrors.getMessage(AggregatorErrors.ERROR_UPDATED_CONTEXT_TYPE_FAULT.getCode()));
    }

    public final void enable() {
        CaLogger.trace();
    }

    public final void disable() {
        CaLogger.trace();
    }

    public final void clear() {
        CaLogger.trace();
        super.clear();
    }

    protected void clearAccordingToRequest() {
        CaLogger.trace();
        super.clearAccordingToRequest();
    }

    public final String[] getContextValueNames() {
        return new String[]{"LoggingType", "LoggingBundle"};
    }

    public final String getContextType() {
        return ContextType.AGGREGATOR_LIFE_LOG.getCode();
    }

    protected final IApPowerObserver getPowerObserver() {
        return this;
    }

    protected final ISensorHubResetObserver getPowerResetObserver() {
        return this;
    }

    public final <E> boolean setPropertyValue(int property, E value) {
        String subCollectorName = null;
        if (property == 28 || property == 29 || property == 30 || property == 31) {
            subCollectorName = ContextType.SENSORHUB_RUNNER_LIFE_LOG_COMPONENT.getCode();
        } else if (property == 32) {
            subCollectorName = ContextType.AGGREGATOR_LPP.getCode();
        }
        if (subCollectorName == null || subCollectorName.isEmpty()) {
            return false;
        }
        ContextComponent sub = getSubCollectionObj(subCollectorName);
        if (sub != null) {
            return sub.setPropertyValue(property, value);
        }
        CaLogger.error(AggregatorErrors.getMessage(AggregatorErrors.ERROR_SUB_COLLECTOR_NULL_EXCEPTION.getCode()));
        return false;
    }

    protected void display() {
    }

    public Bundle getFaultDetectionResult() {
        CaLogger.debug(Boolean.toString(checkFaultDetectionResult()));
        return super.getFaultDetectionResult();
    }
}
