package com.samsung.android.contextaware.utilbundle;

public interface ITimeOutCheckObserverable {
    void notifyTimeOut();

    void registerObserver(ITimeOutCheckObserver iTimeOutCheckObserver);

    void unregisterObserver();
}
