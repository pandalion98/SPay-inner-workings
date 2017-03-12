package com.samsung.android.contextaware.manager;

import android.os.Bundle;
import com.samsung.android.contextaware.manager.fault.ICmdProcessResultObserver;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

class ContextObserverManager {
    private final CopyOnWriteArrayList<ICmdProcessResultObserver> mCmdProcessResultObservers = new CopyOnWriteArrayList();
    private final CopyOnWriteArrayList<IContextObserver> mObservers = new CopyOnWriteArrayList();

    ContextObserverManager() {
    }

    protected final void registerObserver(IContextObserver observer) {
        if (observer != null && !this.mObservers.contains(observer)) {
            this.mObservers.add(observer);
        }
    }

    protected final void unregisterObserver(IContextObserver observer) {
        if (observer != null && this.mObservers.contains(observer)) {
            this.mObservers.remove(observer);
        }
    }

    protected final void notifyObserver(String type, Bundle context) {
        Iterator<?> i = this.mObservers.iterator();
        while (i.hasNext()) {
            IContextObserver observer = (IContextObserver) i.next();
            if (observer != null) {
                observer.updateContext(type, context);
            }
        }
    }

    protected final void registerCmdProcessResultObserver(ICmdProcessResultObserver observer) {
        if (observer != null && !this.mCmdProcessResultObservers.contains(observer)) {
            this.mCmdProcessResultObservers.add(observer);
        }
    }

    protected final void unregisterCmdProcessResultObserver(ICmdProcessResultObserver observer) {
        if (observer != null && this.mCmdProcessResultObservers.contains(observer)) {
            this.mCmdProcessResultObservers.remove(observer);
        }
    }

    protected final void notifyCmdProcessResultObserver(String type, Bundle context) {
        Iterator<?> i = this.mCmdProcessResultObservers.iterator();
        while (i.hasNext()) {
            ICmdProcessResultObserver observer = (ICmdProcessResultObserver) i.next();
            if (observer != null) {
                observer.updateCmdProcessResult(type, context);
            }
        }
    }
}
