package org.codeaurora;

import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;

public class Performance {
    public static final int ALL_CPUS_PC_DIS = 257;
    public static final int CPU0_FREQ_NONTURBO_MAX = 522;
    public static final int CPU0_FREQ_TURBO_MAX = 766;
    public static final int CPU0_MAX_FREQ_NONTURBO_MAX = 5386;
    public static final int CPU1_FREQ_NONTURBO_MAX = 778;
    public static final int CPU1_FREQ_TURBO_MAX = 1022;
    public static final int CPU1_MAX_FREQ_NONTURBO_MAX = 5642;
    public static final int CPU2_FREQ_NONTURBO_MAX = 1034;
    public static final int CPU2_FREQ_TURBO_MAX = 1278;
    public static final int CPU2_MAX_FREQ_NONTURBO_MAX = 5898;
    public static final int CPU3_FREQ_NONTURBO_MAX = 1290;
    public static final int CPU3_FREQ_TURBO_MAX = 1534;
    public static final int CPU3_MAX_FREQ_NONTURBO_MAX = 6154;
    public static final int CPUS_ON_2 = 1794;
    public static final int CPUS_ON_3 = 1795;
    public static final int CPUS_ON_LIMIT_1 = 2302;
    public static final int CPUS_ON_LIMIT_2 = 2301;
    public static final int CPUS_ON_LIMIT_3 = 2300;
    public static final int CPUS_ON_MAX = 1796;
    public static final int REQUEST_FAILED = -1;
    public static final int REQUEST_SUCCEEDED = 0;
    public static final int SCHED_MIGRATE_COST = 16129;
    public static final int SCHED_PREFER_IDLE = 15873;
    private static final String TAG = "Perf";
    private static boolean isFlingEnabled = false;
    private static TouchInfo mTouchInfo = null;
    private static VelocityTracker mVelocityTracker = null;
    private int handle = 0;
    private int mDivFact = 6;
    private int mHDragPix = 12;
    private int mMaxVelocity = 24000;
    private int mMinVelocity = 150;
    private int mWDragPix = 12;

    class TouchInfo {
        private int mCurX = 0;
        private int mCurY = 0;
        private int mMinDragH = 0;
        private int mMinDragW = 0;
        private int mStartX = 0;
        private int mStartY = 0;

        TouchInfo() {
        }

        private void reset() {
            this.mCurY = 0;
            this.mCurX = 0;
            this.mStartY = 0;
            this.mStartX = 0;
            this.mMinDragW = 0;
            this.mMinDragH = 0;
            Performance.isFlingEnabled = false;
        }

        private void setXY(int dx, int dy) {
            this.mCurX = dx;
            this.mCurY = dy;
        }

        private void setDragWH(int dw, int dh) {
            this.mMinDragW = dw;
            this.mMinDragH = dh;
        }

        private void setStartXY(int dx, int dy) {
            this.mCurX = dx;
            this.mStartX = dx;
            this.mCurY = dy;
            this.mStartY = dy;
        }
    }

    private native int native_cpu_setoptions(int i, int i2);

    private native void native_deinit();

    private native int native_perf_lock_acq(int i, int i2, int[] iArr);

    private native int native_perf_lock_rel(int i);

    public int perfLockAcquire(int duration, int... list) {
        this.handle = native_perf_lock_acq(this.handle, duration, list);
        if (this.handle == 0) {
            return -1;
        }
        return 0;
    }

    public int perfLockAcquireTouch(MotionEvent ev, DisplayMetrics metrics, int duration, int... list) {
        int actionMasked = ev.getActionMasked();
        int pointerIndex = ev.getActionIndex();
        int pointerId = ev.getPointerId(pointerIndex);
        int dx = (int) ((((float) ((int) ev.getX(pointerIndex))) * 1.0f) / metrics.density);
        int dy = (int) ((((float) ((int) ev.getY(pointerIndex))) * 1.0f) / metrics.density);
        boolean isBoostRequired = false;
        int xdiff;
        int ydiff;
        switch (actionMasked) {
            case 0:
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(ev);
                }
                if (mTouchInfo == null) {
                    mTouchInfo = new TouchInfo();
                }
                if (mTouchInfo != null) {
                    mTouchInfo.reset();
                    mTouchInfo.setStartXY(dx, dy);
                    mTouchInfo.setDragWH((int) ((((float) this.mWDragPix) * 1.0f) / metrics.density), (int) ((((float) this.mHDragPix) * 1.0f) / metrics.density));
                    break;
                }
                break;
            case 1:
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(ev);
                    mVelocityTracker.computeCurrentVelocity(1000, (float) this.mMaxVelocity);
                    int initialVelocity = Math.abs((int) mVelocityTracker.getYVelocity(pointerId));
                    if (initialVelocity > this.mMinVelocity) {
                        duration = (int) (((float) duration) * ((1.0f * ((float) initialVelocity)) / (1.0f * ((float) this.mMinVelocity))));
                        isBoostRequired = true;
                        break;
                    }
                }
                if (mTouchInfo != null) {
                    xdiff = Math.abs(dx - mTouchInfo.mCurX);
                    ydiff = Math.abs(dy - mTouchInfo.mCurY);
                    if (xdiff > mTouchInfo.mMinDragW || ydiff > mTouchInfo.mMinDragH) {
                        isBoostRequired = true;
                        break;
                    }
                }
                break;
            case 2:
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(ev);
                }
                if (mTouchInfo != null) {
                    xdiff = Math.abs(dx - mTouchInfo.mCurX);
                    ydiff = Math.abs(dy - mTouchInfo.mCurY);
                    mTouchInfo.setXY(dx, dy);
                    if (xdiff > mTouchInfo.mMinDragW || ydiff > mTouchInfo.mMinDragH) {
                        isBoostRequired = true;
                        break;
                    }
                }
                break;
            case 3:
                if (mTouchInfo != null) {
                    mTouchInfo.reset();
                    break;
                }
                break;
        }
        if (!isBoostRequired) {
            return -1;
        }
        this.handle = native_perf_lock_acq(this.handle, duration, list);
        if (this.handle != 0) {
            return 0;
        }
        return -1;
    }

    public int perfLockRelease() {
        return native_perf_lock_rel(this.handle);
    }

    protected void finalize() {
        native_deinit();
    }
}
