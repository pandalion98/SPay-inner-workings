package com.samsung.android.contextaware.utilbundle;

import com.samsung.android.contextaware.manager.IApPowerObserver;

public interface IApPowerObservable {
    public static final byte AP_NONE = (byte) 0;
    public static final byte AP_SLEEP = (byte) -46;
    public static final byte AP_WAKEUP = (byte) -47;
    public static final byte POWER_CONNECTED = (byte) -42;
    public static final byte POWER_DISCONNECTED = (byte) -41;

    void notifyApPowerObserver(int i, long j);

    void registerApPowerObserver(IApPowerObserver iApPowerObserver);

    void unregisterApPowerObserver(IApPowerObserver iApPowerObserver);
}
