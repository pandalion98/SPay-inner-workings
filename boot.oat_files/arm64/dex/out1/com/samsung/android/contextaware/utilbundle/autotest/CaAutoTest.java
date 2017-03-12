package com.samsung.android.contextaware.utilbundle.autotest;

abstract class CaAutoTest implements ICaAutoTest {
    private int mDelayTime;
    private boolean mStopFlag = false;

    public abstract void run();

    protected CaAutoTest(int delayTime) {
        setDelayTime(delayTime);
    }

    protected final void setDelayTime(int time) {
        this.mDelayTime = time;
    }

    protected final int getDelayTime() {
        return this.mDelayTime;
    }

    public final void setStopFlag(boolean flag) {
        this.mStopFlag = flag;
    }

    public final void stopAutoTest() {
        this.mStopFlag = true;
    }

    protected final boolean isStopTest() {
        return this.mStopFlag;
    }
}
