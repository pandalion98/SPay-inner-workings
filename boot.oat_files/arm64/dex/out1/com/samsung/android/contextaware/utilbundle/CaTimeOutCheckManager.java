package com.samsung.android.contextaware.utilbundle;

import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

public class CaTimeOutCheckManager implements Runnable, ITimeOutCheckObserverable {
    private ITimeOutCheckObserver mObserver;
    private final int mTimeOut;

    public CaTimeOutCheckManager(ITimeOutCheckObserver observer, int time) {
        registerObserver(observer);
        this.mTimeOut = time;
    }

    public final void run() {
        int i = 0;
        while (i < this.mTimeOut * 10) {
            try {
                Thread.sleep(100);
                i++;
            } catch (InterruptedException e) {
                CaLogger.info("interruped");
                return;
            }
        }
        notifyTimeOut();
    }

    public final void registerObserver(ITimeOutCheckObserver observer) {
        this.mObserver = observer;
    }

    public final void unregisterObserver() {
        this.mObserver = null;
    }

    public final void notifyTimeOut() {
        if (this.mObserver != null) {
            this.mObserver.occurTimeOut();
        }
    }
}
