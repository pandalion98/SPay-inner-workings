package com.samsung.android.contextaware.manager;

import com.samsung.android.contextaware.utilbundle.CaTimeOutCheckManager;

public interface IContextTimeOutCheck {
    Thread getHandler();

    CaTimeOutCheckManager getService();

    boolean isTimeOut();

    void run();

    void setTimeOutOccurence(boolean z);
}
