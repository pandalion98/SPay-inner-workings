package com.samsung.android.contextaware.manager;

import android.os.Bundle;
import com.samsung.android.contextaware.manager.fault.ICmdProcessResultObserver;

public interface ICmdProcessResultObservable {
    void notifyCmdProcessResultObserver(String str, Bundle bundle);

    void registerCmdProcessResultObserver(ICmdProcessResultObserver iCmdProcessResultObserver);

    void unregisterCmdProcessResultObserver(ICmdProcessResultObserver iCmdProcessResultObserver);
}
