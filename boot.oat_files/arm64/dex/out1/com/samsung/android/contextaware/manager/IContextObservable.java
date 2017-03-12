package com.samsung.android.contextaware.manager;

interface IContextObservable {
    void notifyObserver();

    void registerObserver(IContextObserver iContextObserver);

    void unregisterObserver(IContextObserver iContextObserver);
}
