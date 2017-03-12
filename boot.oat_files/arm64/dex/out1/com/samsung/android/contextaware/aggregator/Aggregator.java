package com.samsung.android.contextaware.aggregator;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.dataprovider.androidprovider.AndroidProvider;
import com.samsung.android.contextaware.dataprovider.sensorhubprovider.ISensorHubResetObservable;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.ContextAwareServiceErrors;
import com.samsung.android.contextaware.manager.ContextComponent;
import com.samsung.android.contextaware.manager.ContextProvider;
import com.samsung.android.contextaware.manager.IContextObserver;
import com.samsung.android.contextaware.manager.fault.ICmdProcessResultObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class Aggregator extends ContextProvider implements IContextObserver, ICmdProcessResultObserver {
    private boolean mAggregatorFaultDetectionResult;
    private final CopyOnWriteArrayList<ContextComponent> mSubCollectors;

    public abstract void updateContext(String str, Bundle bundle);

    protected Aggregator(int version, Context context, Looper looper, CopyOnWriteArrayList<ContextComponent> collectionList, ISensorHubResetObservable observable) {
        super(version, context, looper, observable);
        this.mSubCollectors = collectionList;
    }

    protected void initializeAggregator() {
    }

    protected void terminateAggregator() {
    }

    protected final void initialize() {
        initializeAggregator();
    }

    protected final void terminate() {
        unregisterObserver();
        terminateAggregator();
    }

    public final void start(Listener listener, int operation) {
        initializeFaultDetectionResult();
        registerObserver();
        if (super.isEnable()) {
            Iterator<ContextComponent> i = this.mSubCollectors.iterator();
            while (i.hasNext()) {
                ContextComponent next = (ContextComponent) i.next();
                if (next != null) {
                    next.start(listener, operation);
                }
            }
        }
        super.start(listener, operation);
    }

    public final void stop(Listener listener, int operation) {
        initializeFaultDetectionResult();
        if (super.isDisable()) {
            Iterator<ContextComponent> i = this.mSubCollectors.iterator();
            while (i.hasNext()) {
                ContextComponent next = (ContextComponent) i.next();
                if (next != null) {
                    next.stop(listener, operation);
                }
            }
        }
        super.stop(listener, operation);
    }

    protected final void clearExtension() {
        if (super.isRunning()) {
            Iterator<ContextComponent> i = this.mSubCollectors.iterator();
            while (i.hasNext()) {
                ContextComponent next = (ContextComponent) i.next();
                if (next != null) {
                    next.clear();
                }
            }
        }
    }

    protected final void registerObserver() {
        Iterator<ContextComponent> i = this.mSubCollectors.iterator();
        while (i.hasNext()) {
            ContextComponent next = (ContextComponent) i.next();
            if (next != null) {
                next.registerObserver(this);
                next.registerCmdProcessResultObserver(this);
            }
        }
    }

    protected final void unregisterObserver() {
        Iterator<ContextComponent> i = this.mSubCollectors.iterator();
        while (i.hasNext()) {
            ContextComponent next = (ContextComponent) i.next();
            if (next != null) {
                next.unregisterObserver(this);
                next.unregisterCmdProcessResultObserver(this);
            }
        }
    }

    public void pause() {
        Iterator<ContextComponent> i = this.mSubCollectors.iterator();
        while (i.hasNext()) {
            ContextComponent next = (ContextComponent) i.next();
            if (next != null) {
                next.pause();
            }
        }
        super.pause();
    }

    public void resume() {
        Iterator<ContextComponent> i = this.mSubCollectors.iterator();
        while (i.hasNext()) {
            ContextComponent next = (ContextComponent) i.next();
            if (next != null) {
                next.resume();
            }
        }
        super.resume();
    }

    protected final void initializeFaultDetectionResult() {
        this.mAggregatorFaultDetectionResult = true;
    }

    protected final void enableExtension() {
        if (super.isEnable()) {
            Iterator<ContextComponent> i = this.mSubCollectors.iterator();
            while (i.hasNext()) {
                ContextComponent next = (ContextComponent) i.next();
                if (next != null) {
                    CaLogger.trace();
                    next.enable();
                }
            }
        }
    }

    protected final void disableExtension() {
        if (super.isDisable()) {
            Iterator<ContextComponent> i = this.mSubCollectors.iterator();
            while (i.hasNext()) {
                ContextComponent next = (ContextComponent) i.next();
                if (next != null) {
                    next.disable();
                }
            }
        }
    }

    protected final void getContextInfo(Listener listener) {
        CaLogger.error(ContextAwareServiceErrors.ERROR_NOT_SUPPORT_CMD.getMessage());
    }

    protected final void notifyApStatus() {
        Iterator<ContextComponent> i = this.mSubCollectors.iterator();
        while (i.hasNext()) {
            ContextComponent next = (ContextComponent) i.next();
            if (next != null) {
                if (next instanceof AndroidProvider) {
                    ((AndroidProvider) next).updateAPStatus(super.getAPStatus());
                }
                next.updateApPowerStatusForPreparedCollection();
            }
        }
        super.setAPStatus(0);
    }

    protected final ContextComponent getSubCollectionObj(String collectionName) {
        Iterator<ContextComponent> i = this.mSubCollectors.iterator();
        while (i.hasNext()) {
            ContextComponent obj = (ContextComponent) i.next();
            if (obj != null && obj.getContextType().equals(collectionName)) {
                return obj;
            }
        }
        return null;
    }

    protected void updateApSleep() {
        notifyApStatus();
    }

    protected void updateApWakeup() {
        notifyApStatus();
    }

    public Bundle getFaultDetectionResult() {
        if (checkFaultDetectionResult()) {
            return super.getFaultDetectionResult(0, ContextAwareServiceErrors.SUCCESS.getMessage());
        }
        return super.getFaultDetectionResult(1, ContextAwareServiceErrors.ERROR_SUB_COLLECTION.getMessage());
    }

    protected final boolean checkFaultDetectionResult() {
        return this.mAggregatorFaultDetectionResult;
    }

    public final void updateCmdProcessResult(String type, Bundle context) {
        if (type.equals(ContextType.CMD_PROCESS_FAULT_DETECTION.getCode()) && context.getInt("CheckResult") != 0) {
            this.mAggregatorFaultDetectionResult = false;
            CaLogger.debug(getContextType() + " : SubCollection(" + ContextList.getInstance().getServiceCode(context.getInt("Service")) + ") process result is failed.");
        }
    }

    protected final CopyOnWriteArrayList<ContextComponent> getSubCollectors() {
        return this.mSubCollectors;
    }

    protected final void enableForStart(boolean restore) {
    }

    protected final void disableForStop(boolean restore) {
    }
}
