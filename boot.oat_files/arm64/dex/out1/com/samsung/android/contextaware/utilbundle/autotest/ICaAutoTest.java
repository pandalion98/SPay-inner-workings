package com.samsung.android.contextaware.utilbundle.autotest;

interface ICaAutoTest extends Runnable {
    void setStopFlag(boolean z);

    void stopAutoTest();
}
