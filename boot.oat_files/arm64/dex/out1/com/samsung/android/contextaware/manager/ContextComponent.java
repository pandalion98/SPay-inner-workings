package com.samsung.android.contextaware.manager;

import android.os.Bundle;
import com.samsung.android.contextaware.manager.ContextAwareService.Listener;
import com.samsung.android.contextaware.manager.fault.ICmdProcessResultObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public abstract class ContextComponent implements IContextProvider, IContextObservable, ICmdProcessResultObservable {
    private final ContextBean mContextBean = new ContextBean();
    private final ContextObserverManager mContextObserver = new ContextObserverManager();

    public abstract ContextProvider getContextProvider();

    public abstract void start(Listener listener, int i);

    public abstract void stop(Listener listener, int i);

    protected ContextComponent() {
    }

    protected void initialize() {
    }

    protected void terminate() {
    }

    public void clear() {
        clearContextBean();
    }

    public void enable() {
    }

    public void disable() {
    }

    public String getContextType() {
        return "";
    }

    public final void registerObserver(IContextObserver observer) {
        this.mContextObserver.registerObserver(observer);
    }

    public final void unregisterObserver(IContextObserver observer) {
        this.mContextObserver.unregisterObserver(observer);
    }

    public final void notifyObserver() {
        if (checkNotifyCondition()) {
            notifyObserver(getContextType(), getContextBean().getContextBundle());
        }
    }

    private void notifyObserver(String type, Bundle context) {
        display();
        this.mContextObserver.notifyObserver(type, context);
        clearContextBean();
    }

    protected void enableForStart(int operation) {
    }

    protected void disableForStop(int operation) {
    }

    protected void registerApPowerObserver() {
    }

    protected void unregisterApPowerObserver() {
    }

    protected void notifyFaultDetectionResult() {
    }

    protected final int getUsedTotalCount() {
        return ListenerListManager.getInstance().getUsedTotalCount(getContextType());
    }

    protected final int getUsedServiceCount() {
        return ListenerListManager.getInstance().getUsedServiceCount(getContextType());
    }

    protected final int getUsedSubCollectionCount() {
        return ListenerListManager.getInstance().getUsedSubCollectionCount(getContextType());
    }

    public void pause() {
        disable();
    }

    public void resume() {
        enable();
    }

    protected void reset() {
    }

    protected void clearAccordingToRequest() {
        clear();
    }

    protected final boolean isRunning() {
        return getUsedTotalCount() >= 1;
    }

    protected Bundle getInitContextBundle() {
        return null;
    }

    protected boolean checkNotifyCondition() {
        return true;
    }

    protected void notifyInitContext() {
        Bundle contextBundle = getInitContextBundle();
        if (contextBundle != null) {
            notifyObserver(getContextType(), contextBundle);
        }
    }

    protected void getContextInfo(Listener listener) {
    }

    protected final <E> boolean setProperty(int property, E value) {
        if (value != null) {
            return setPropertyValue(property, value);
        }
        CaLogger.error("value is null");
        return false;
    }

    public <E> boolean setPropertyValue(int property, E e) {
        return true;
    }

    public void updateApPowerStatusForPreparedCollection() {
    }

    private void clearContextBean() {
        this.mContextBean.clearContextBean();
    }

    public final void registerCmdProcessResultObserver(ICmdProcessResultObserver observer) {
        this.mContextObserver.registerCmdProcessResultObserver(observer);
    }

    public final void unregisterCmdProcessResultObserver(ICmdProcessResultObserver observer) {
        this.mContextObserver.unregisterCmdProcessResultObserver(observer);
    }

    protected final ContextBean getContextBean() {
        return this.mContextBean;
    }

    public String[] getContextValueNames() {
        return new String[1];
    }

    public void notifyCmdProcessResultObserver(String type, Bundle context) {
        this.mContextObserver.notifyCmdProcessResultObserver(type, context);
    }

    protected void display() {
    }
}
