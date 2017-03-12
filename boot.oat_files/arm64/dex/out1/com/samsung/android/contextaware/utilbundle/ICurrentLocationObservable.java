package com.samsung.android.contextaware.utilbundle;

import com.samsung.android.contextaware.manager.ICurrrentLocationObserver;

public interface ICurrentLocationObservable {
    void notifyCurrentLocationObserver(long j, long j2, double d, double d2, double d3);

    void registerCurrentLocationObserver(ICurrrentLocationObserver iCurrrentLocationObserver);

    void unregisterCurrentLocationObserver();
}
