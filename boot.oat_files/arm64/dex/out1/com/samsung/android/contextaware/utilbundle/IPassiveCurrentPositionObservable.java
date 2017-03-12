package com.samsung.android.contextaware.utilbundle;

import com.samsung.android.contextaware.dataprovider.sensorhubprovider.IPassiveCurrrentPositionObserver;

public interface IPassiveCurrentPositionObservable {
    void notifyPassiveCurrentPositionObserver(PositionContextBean positionContextBean);

    void registerPassiveCurrentPositionObserver(IPassiveCurrrentPositionObserver iPassiveCurrrentPositionObserver);

    void unregisterPassiveCurrentPositionObserver();
}
