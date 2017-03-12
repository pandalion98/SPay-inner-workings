package com.samsung.android.contextaware.utilbundle;

import com.samsung.android.contextaware.manager.ICurrrentPositionObserver;

public interface ICurrentPositionObservable {
    void notifyCurrentPositionObserver();

    void registerCurrentPositionObserver(ICurrrentPositionObserver iCurrrentPositionObserver);

    void unregisterCurrentPositionObserver();
}
