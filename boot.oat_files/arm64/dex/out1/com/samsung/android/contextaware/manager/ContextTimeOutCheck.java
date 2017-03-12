package com.samsung.android.contextaware.manager;

import com.samsung.android.contextaware.utilbundle.CaTimeOutCheckManager;
import com.samsung.android.contextaware.utilbundle.ITimeOutCheckObserver;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

class ContextTimeOutCheck implements IContextTimeOutCheck {
    protected static final int DEFAULT_TIME_OUT = 5;
    private ITimeOutCheckObserver mObserver;
    private CaTimeOutCheckManager mTimeOutCheckManager;
    private Thread mTimeOutHandler;
    private boolean mTimeOutOccurrence;

    protected ContextTimeOutCheck(ITimeOutCheckObserver observer) {
        this.mObserver = observer;
    }

    public final void run() {
        clearTimeOutCheckService();
        if (this.mObserver == null) {
            CaLogger.error("TimeOutCheckObserver is null");
            return;
        }
        this.mTimeOutCheckManager = new CaTimeOutCheckManager(this.mObserver, 5);
        this.mTimeOutHandler = new Thread(this.mTimeOutCheckManager);
        this.mTimeOutHandler.start();
    }

    protected void clearTimeOutCheckService() {
        if (this.mTimeOutHandler != null) {
            this.mTimeOutHandler.interrupt();
            this.mTimeOutCheckManager = null;
            this.mTimeOutHandler = null;
            CaLogger.trace();
        }
    }

    public final Thread getHandler() {
        return this.mTimeOutHandler;
    }

    public final CaTimeOutCheckManager getService() {
        return this.mTimeOutCheckManager;
    }

    public final void setTimeOutOccurence(boolean timeOut) {
        this.mTimeOutOccurrence = timeOut;
    }

    public final boolean isTimeOut() {
        return this.mTimeOutOccurrence;
    }
}
