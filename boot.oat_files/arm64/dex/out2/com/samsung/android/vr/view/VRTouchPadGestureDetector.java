package com.samsung.android.vr.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;

public class VRTouchPadGestureDetector {
    public static final int ACTION_SWIPE_BACKWARD = 0;
    public static final int ACTION_SWIPE_DOWN = 3;
    public static final int ACTION_SWIPE_FORWARD = 1;
    private static final int ACTION_SWIPE_INVALID = -1;
    public static final int ACTION_SWIPE_UP = 2;
    private static final int ACTION_TAP = 4;
    private static final int DEGREE_0 = 0;
    private static final int DEGREE_1 = 70;
    private static final int DEGREE_2 = 110;
    private static final int DEGREE_3 = 250;
    private static final int DEGREE_4 = 290;
    private static final int DEGREE_5 = 360;
    private static final int DOUBLE_TAP_SLOP = 100;
    private static final int DOUBLE_TAP_TIMEOUT = 300;
    private static final int LONGPRESS_TIMEOUT = 500;
    private static final int LONG_PRESS = 1;
    private static final int MAXIMUM_FLING_VELOCITY = 8000;
    private static final int MINIMUM_FLING_VELOCITY = 2000;
    static final String TAG = "VRTouchPadGestureDetector";
    private static final int TAP = 2;
    private static final int TAP_TIMEOUT = 180;
    private static final int TOUCH_XSLOP = 40;
    private static final int TOUCH_YSLOP = 20;
    private static final int VELOCITY_UNITS = 1000;
    private boolean mAlwaysInTapRegion;
    private MotionEvent mCurrentDownEvent;
    private boolean mDeferConfirmSingleTap;
    private OnTouchPadDoubleTapListener mDoubleTapListener;
    private int mDoubleTapSlopSquare;
    private float mDownFocusX;
    private float mDownFocusY;
    private final Handler mHandler;
    private boolean mInLongPress;
    private boolean mIsLongpressEnabled;
    private float mLastFocusX;
    private float mLastFocusY;
    private final OnTouchPadGestureListener mListener;
    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;
    private MotionEvent mPreviousUpEvent;
    private boolean mStillDown;
    private int mTouchXSlopSquare;
    private int mTouchYSlopSquare;
    private float mUpFocusX;
    private float mUpFocusY;
    private VelocityTracker mVelocityTracker;
    private int mVelocityUnits;

    public interface OnTouchPadDoubleTapListener {
        boolean onDoubleTap(MotionEvent motionEvent);

        boolean onSingleTapConfirmed(MotionEvent motionEvent);
    }

    public interface OnTouchPadGestureListener {
        boolean onLongPress(MotionEvent motionEvent);

        boolean onSingleTap(MotionEvent motionEvent);

        boolean onSwipe(MotionEvent motionEvent, int i, float f, float f2);
    }

    private class VRTouchPadGestureHandler extends Handler {
        public VRTouchPadGestureHandler(Handler handler) {
            super(handler.getLooper());
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    VRTouchPadGestureDetector.this.dispatchLongPress();
                    return;
                case 2:
                    if (VRTouchPadGestureDetector.this.mDoubleTapListener == null) {
                        return;
                    }
                    if (VRTouchPadGestureDetector.this.mStillDown) {
                        VRTouchPadGestureDetector.this.mDeferConfirmSingleTap = true;
                        return;
                    } else {
                        VRTouchPadGestureDetector.this.mDoubleTapListener.onSingleTapConfirmed(VRTouchPadGestureDetector.this.mCurrentDownEvent);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    public VRTouchPadGestureDetector(OnTouchPadGestureListener listener) {
        this(null, listener, null);
    }

    public VRTouchPadGestureDetector(Context context, OnTouchPadGestureListener listener) {
        this(context, listener, null);
    }

    public VRTouchPadGestureDetector(Context context, OnTouchPadGestureListener listener, Handler handler) {
        if (handler != null) {
            this.mHandler = new VRTouchPadGestureHandler(handler);
        } else {
            this.mHandler = new VRTouchPadGestureHandler();
        }
        this.mListener = listener;
        init(context);
    }

    private void init(Context context) {
        if (this.mListener == null) {
            throw new NullPointerException("OnTouchPadGestureListener must not be null");
        }
        this.mIsLongpressEnabled = true;
        this.mTouchXSlopSquare = 1600;
        this.mTouchYSlopSquare = 400;
        this.mDoubleTapSlopSquare = 10000;
        this.mMinimumFlingVelocity = MINIMUM_FLING_VELOCITY;
        this.mMaximumFlingVelocity = MAXIMUM_FLING_VELOCITY;
        this.mVelocityUnits = VELOCITY_UNITS;
    }

    public void setOnDoubleTapListener(OnTouchPadDoubleTapListener onDoubleTapListener) {
        this.mDoubleTapListener = onDoubleTapListener;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int div;
        int action = ev.getAction();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(ev);
        boolean pointerUp = (action & 255) == 6;
        int skipIndex = pointerUp ? ev.getActionIndex() : -1;
        float sumX = 0.0f;
        float sumY = 0.0f;
        int count = ev.getPointerCount();
        for (int i = 0; i < count; i++) {
            if (skipIndex != i) {
                sumX += ev.getX(i);
                sumY += ev.getY(i);
            }
        }
        if (pointerUp) {
            div = count - 1;
        } else {
            div = count;
        }
        float focusX = sumX / ((float) div);
        float focusY = sumY / ((float) div);
        boolean handled = false;
        switch (action & 255) {
            case 0:
                if (this.mDoubleTapListener != null) {
                    boolean hadTapMessage = this.mHandler.hasMessages(2);
                    if (hadTapMessage) {
                        this.mHandler.removeMessages(2);
                    }
                    if (!(this.mCurrentDownEvent == null || this.mPreviousUpEvent == null || !hadTapMessage)) {
                        if (isConsideredDoubleTap(this.mCurrentDownEvent, this.mPreviousUpEvent, ev)) {
                            handled = false | this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent);
                        }
                    }
                    this.mHandler.sendEmptyMessageDelayed(2, 300);
                }
                this.mLastFocusX = focusX;
                this.mDownFocusX = focusX;
                this.mLastFocusY = focusY;
                this.mDownFocusY = focusY;
                if (this.mCurrentDownEvent != null) {
                    this.mCurrentDownEvent.recycle();
                }
                this.mCurrentDownEvent = MotionEvent.obtain(ev);
                this.mAlwaysInTapRegion = true;
                this.mStillDown = true;
                this.mInLongPress = false;
                this.mDeferConfirmSingleTap = false;
                if (this.mIsLongpressEnabled) {
                    this.mHandler.removeMessages(1);
                    this.mHandler.sendEmptyMessageAtTime(1, (this.mCurrentDownEvent.getDownTime() + 180) + 500);
                    break;
                }
                break;
            case 1:
                this.mUpFocusX = focusX;
                this.mUpFocusY = focusY;
                this.mStillDown = false;
                MotionEvent currentUpEvent = MotionEvent.obtain(ev);
                if (this.mInLongPress) {
                    this.mHandler.removeMessages(2);
                    this.mInLongPress = false;
                } else if (this.mAlwaysInTapRegion) {
                    handled = this.mListener.onSingleTap(ev);
                    if (this.mDeferConfirmSingleTap && this.mDoubleTapListener != null) {
                        this.mDoubleTapListener.onSingleTapConfirmed(ev);
                    }
                } else {
                    VelocityTracker velocityTracker = this.mVelocityTracker;
                    int pointerId = ev.getPointerId(0);
                    velocityTracker.computeCurrentVelocity(this.mVelocityUnits, (float) this.mMaximumFlingVelocity);
                    float velocityX = velocityTracker.getXVelocity(pointerId);
                    float velocityY = velocityTracker.getYVelocity(pointerId);
                    Log.d(TAG, "velocityX:" + velocityX + " velocityY:" + velocityY);
                    int swipeActionType = processForSwipeAction(this.mDownFocusX, this.mDownFocusY, this.mUpFocusX, this.mUpFocusY);
                    if (swipeActionType != -1) {
                        this.mListener.onSwipe(ev, swipeActionType, velocityX, velocityY);
                    }
                    this.mHandler.removeMessages(2);
                }
                if (this.mPreviousUpEvent != null) {
                    this.mPreviousUpEvent.recycle();
                }
                this.mPreviousUpEvent = currentUpEvent;
                if (this.mVelocityTracker != null) {
                    this.mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                }
                this.mDeferConfirmSingleTap = false;
                this.mHandler.removeMessages(1);
                break;
            case 2:
                if (!this.mInLongPress) {
                    float scrollX = this.mLastFocusX - focusX;
                    float scrollY = this.mLastFocusY - focusY;
                    if (this.mAlwaysInTapRegion) {
                        int deltaX = (int) (focusX - this.mDownFocusX);
                        int deltaY = (int) (focusY - this.mDownFocusY);
                        int distanceY = deltaY * deltaY;
                        if (deltaX * deltaX > this.mTouchXSlopSquare || distanceY > this.mTouchYSlopSquare) {
                            this.mLastFocusX = focusX;
                            this.mLastFocusY = focusY;
                            this.mAlwaysInTapRegion = false;
                            this.mHandler.removeMessages(1);
                            break;
                        }
                    }
                }
                break;
            case 3:
                cancel();
                break;
        }
        return handled;
    }

    private void cancel() {
        this.mHandler.removeMessages(1);
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
        this.mAlwaysInTapRegion = false;
        this.mDeferConfirmSingleTap = false;
        this.mStillDown = false;
        if (this.mInLongPress) {
            this.mInLongPress = false;
        }
    }

    private void cancelTaps() {
        this.mHandler.removeMessages(2);
        this.mAlwaysInTapRegion = false;
        this.mDeferConfirmSingleTap = false;
        if (this.mInLongPress) {
            this.mInLongPress = false;
        }
    }

    private boolean isConsideredDoubleTap(MotionEvent firstDown, MotionEvent firstUp, MotionEvent secondDown) {
        boolean ret = false;
        if (secondDown.getEventTime() - firstDown.getEventTime() <= 300) {
            int deltaX = ((int) firstDown.getX()) - ((int) secondDown.getX());
            int deltaY = ((int) firstDown.getY()) - ((int) secondDown.getY());
            if ((deltaX * deltaX) + (deltaY * deltaY) < this.mDoubleTapSlopSquare) {
                ret = true;
            }
            if (!ret) {
                Log.w(TAG, "isConsideredDoubleTap distance[" + ((deltaX * deltaX) + (deltaY * deltaY)) + "," + this.mDoubleTapSlopSquare + "]");
            }
        }
        return ret;
    }

    private int processForSwipeAction(float downPositionX, float downPositionY, float upPositionX, float upPositionY) {
        float deltaX = upPositionX - downPositionX;
        float deltaY = upPositionY - downPositionY;
        Log.d(TAG, "processForSwipeAction deltaX:" + deltaX + " deltaY:" + deltaY);
        float angle = (float) Math.toDegrees(Math.atan2((double) deltaX, (double) deltaY));
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        Log.d(TAG, "processForSwipeAction angle:" + angle);
        if ((angle >= 0.0f && angle < 70.0f) || (angle >= 290.0f && angle < 360.0f)) {
            return 3;
        }
        if (angle >= 70.0f && angle < 110.0f) {
            return 0;
        }
        if (angle >= 110.0f && angle < 250.0f) {
            return 2;
        }
        if (angle < 250.0f || angle >= 290.0f) {
            return -1;
        }
        return 1;
    }

    private void dispatchLongPress() {
        this.mHandler.removeMessages(2);
        this.mDeferConfirmSingleTap = false;
        this.mInLongPress = true;
        this.mListener.onLongPress(this.mCurrentDownEvent);
    }
}
