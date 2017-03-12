package com.samsung.android.contextaware.utilbundle.autotest;

import com.samsung.android.contextaware.utilbundle.logger.CaLogger;

abstract class CmdProcessStressTest extends CaAutoTest {
    private boolean mChange = true;

    protected abstract void clear();

    protected abstract int getType();

    protected abstract void registerListener();

    protected abstract void unregisterListener();

    protected CmdProcessStressTest(int delayTime) {
        super(delayTime);
    }

    public final void run() {
        do {
            try {
                Thread.sleep((long) getDelayTime());
                if (this.mChange) {
                    this.mChange = false;
                    registerListener();
                } else {
                    this.mChange = true;
                    unregisterListener();
                }
            } catch (InterruptedException e) {
                CaLogger.exception(e);
                return;
            }
        } while (!super.isStopTest());
        clear();
    }
}
