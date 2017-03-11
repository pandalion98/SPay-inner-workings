package android.support.v4.widget;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.VelocityTrackerCompat;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import com.google.android.gms.location.LocationStatusCodes;
import java.util.Arrays;
import org.bouncycastle.math.ec.ECCurve;

public class ViewDragHelper {
    private static final int BASE_SETTLE_DURATION = 256;
    public static final int DIRECTION_ALL = 3;
    public static final int DIRECTION_HORIZONTAL = 1;
    public static final int DIRECTION_VERTICAL = 2;
    public static final int EDGE_ALL = 15;
    public static final int EDGE_BOTTOM = 8;
    public static final int EDGE_LEFT = 1;
    public static final int EDGE_RIGHT = 2;
    private static final int EDGE_SIZE = 20;
    public static final int EDGE_TOP = 4;
    public static final int INVALID_POINTER = -1;
    private static final int MAX_SETTLE_DURATION = 600;
    public static final int STATE_DRAGGING = 1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_SETTLING = 2;
    private static final String TAG = "ViewDragHelper";
    private static final Interpolator sInterpolator;
    private int mActivePointerId;
    private final Callback mCallback;
    private View mCapturedView;
    private int mDragState;
    private int[] mEdgeDragsInProgress;
    private int[] mEdgeDragsLocked;
    private int mEdgeSize;
    private int[] mInitialEdgesTouched;
    private float[] mInitialMotionX;
    private float[] mInitialMotionY;
    private float[] mLastMotionX;
    private float[] mLastMotionY;
    private float mMaxVelocity;
    private float mMinVelocity;
    private final ViewGroup mParentView;
    private int mPointersDown;
    private boolean mReleaseInProgress;
    private ScrollerCompat mScroller;
    private final Runnable mSetIdleRunnable;
    private int mTouchSlop;
    private int mTrackingEdges;
    private VelocityTracker mVelocityTracker;

    public static abstract class Callback {
        public abstract boolean tryCaptureView(View view, int i);

        public void onViewDragStateChanged(int i) {
        }

        public void onViewPositionChanged(View view, int i, int i2, int i3, int i4) {
        }

        public void onViewCaptured(View view, int i) {
        }

        public void onViewReleased(View view, float f, float f2) {
        }

        public void onEdgeTouched(int i, int i2) {
        }

        public boolean onEdgeLock(int i) {
            return false;
        }

        public void onEdgeDragStarted(int i, int i2) {
        }

        public int getOrderedChildIndex(int i) {
            return i;
        }

        public int getViewHorizontalDragRange(View view) {
            return ViewDragHelper.STATE_IDLE;
        }

        public int getViewVerticalDragRange(View view) {
            return ViewDragHelper.STATE_IDLE;
        }

        public int clampViewPositionHorizontal(View view, int i, int i2) {
            return ViewDragHelper.STATE_IDLE;
        }

        public int clampViewPositionVertical(View view, int i, int i2) {
            return ViewDragHelper.STATE_IDLE;
        }
    }

    /* renamed from: android.support.v4.widget.ViewDragHelper.1 */
    static class C00691 implements Interpolator {
        C00691() {
        }

        public float getInterpolation(float f) {
            float f2 = f - 1.0f;
            return (f2 * (((f2 * f2) * f2) * f2)) + 1.0f;
        }
    }

    /* renamed from: android.support.v4.widget.ViewDragHelper.2 */
    class C00702 implements Runnable {
        C00702() {
        }

        public void run() {
            ViewDragHelper.this.setDragState(ViewDragHelper.STATE_IDLE);
        }
    }

    static {
        sInterpolator = new C00691();
    }

    public static ViewDragHelper create(ViewGroup viewGroup, Callback callback) {
        return new ViewDragHelper(viewGroup.getContext(), viewGroup, callback);
    }

    public static ViewDragHelper create(ViewGroup viewGroup, float f, Callback callback) {
        ViewDragHelper create = create(viewGroup, callback);
        create.mTouchSlop = (int) (((float) create.mTouchSlop) * (1.0f / f));
        return create;
    }

    private ViewDragHelper(Context context, ViewGroup viewGroup, Callback callback) {
        this.mActivePointerId = INVALID_POINTER;
        this.mSetIdleRunnable = new C00702();
        if (viewGroup == null) {
            throw new IllegalArgumentException("Parent view may not be null");
        } else if (callback == null) {
            throw new IllegalArgumentException("Callback may not be null");
        } else {
            this.mParentView = viewGroup;
            this.mCallback = callback;
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            this.mEdgeSize = (int) ((context.getResources().getDisplayMetrics().density * 20.0f) + 0.5f);
            this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
            this.mMaxVelocity = (float) viewConfiguration.getScaledMaximumFlingVelocity();
            this.mMinVelocity = (float) viewConfiguration.getScaledMinimumFlingVelocity();
            this.mScroller = ScrollerCompat.create(context, sInterpolator);
        }
    }

    public void setMinVelocity(float f) {
        this.mMinVelocity = f;
    }

    public float getMinVelocity() {
        return this.mMinVelocity;
    }

    public int getViewDragState() {
        return this.mDragState;
    }

    public void setEdgeTrackingEnabled(int i) {
        this.mTrackingEdges = i;
    }

    public int getEdgeSize() {
        return this.mEdgeSize;
    }

    public void captureChildView(View view, int i) {
        if (view.getParent() != this.mParentView) {
            throw new IllegalArgumentException("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (" + this.mParentView + ")");
        }
        this.mCapturedView = view;
        this.mActivePointerId = i;
        this.mCallback.onViewCaptured(view, i);
        setDragState(STATE_DRAGGING);
    }

    public View getCapturedView() {
        return this.mCapturedView;
    }

    public int getActivePointerId() {
        return this.mActivePointerId;
    }

    public int getTouchSlop() {
        return this.mTouchSlop;
    }

    public void cancel() {
        this.mActivePointerId = INVALID_POINTER;
        clearMotionHistory();
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void abort() {
        cancel();
        if (this.mDragState == STATE_SETTLING) {
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            this.mScroller.abortAnimation();
            int currX2 = this.mScroller.getCurrX();
            int currY2 = this.mScroller.getCurrY();
            this.mCallback.onViewPositionChanged(this.mCapturedView, currX2, currY2, currX2 - currX, currY2 - currY);
        }
        setDragState(STATE_IDLE);
    }

    public boolean smoothSlideViewTo(View view, int i, int i2) {
        this.mCapturedView = view;
        this.mActivePointerId = INVALID_POINTER;
        return forceSettleCapturedViewAt(i, i2, STATE_IDLE, STATE_IDLE);
    }

    public boolean settleCapturedViewAt(int i, int i2) {
        if (this.mReleaseInProgress) {
            return forceSettleCapturedViewAt(i, i2, (int) VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int) VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId));
        }
        throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
    }

    private boolean forceSettleCapturedViewAt(int i, int i2, int i3, int i4) {
        int left = this.mCapturedView.getLeft();
        int top = this.mCapturedView.getTop();
        int i5 = i - left;
        int i6 = i2 - top;
        if (i5 == 0 && i6 == 0) {
            this.mScroller.abortAnimation();
            setDragState(STATE_IDLE);
            return false;
        }
        this.mScroller.startScroll(left, top, i5, i6, computeSettleDuration(this.mCapturedView, i5, i6, i3, i4));
        setDragState(STATE_SETTLING);
        return true;
    }

    private int computeSettleDuration(View view, int i, int i2, int i3, int i4) {
        int clampMag = clampMag(i3, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        int clampMag2 = clampMag(i4, (int) this.mMinVelocity, (int) this.mMaxVelocity);
        int abs = Math.abs(i);
        int abs2 = Math.abs(i2);
        int abs3 = Math.abs(clampMag);
        int abs4 = Math.abs(clampMag2);
        int i5 = abs3 + abs4;
        int i6 = abs + abs2;
        return (int) (((clampMag2 != 0 ? ((float) abs4) / ((float) i5) : ((float) abs2) / ((float) i6)) * ((float) computeAxisDuration(i2, clampMag2, this.mCallback.getViewVerticalDragRange(view)))) + ((clampMag != 0 ? ((float) abs3) / ((float) i5) : ((float) abs) / ((float) i6)) * ((float) computeAxisDuration(i, clampMag, this.mCallback.getViewHorizontalDragRange(view)))));
    }

    private int computeAxisDuration(int i, int i2, int i3) {
        if (i == 0) {
            return STATE_IDLE;
        }
        int width = this.mParentView.getWidth();
        int i4 = width / STATE_SETTLING;
        float distanceInfluenceForSnapDuration = (distanceInfluenceForSnapDuration(Math.min(1.0f, ((float) Math.abs(i)) / ((float) width))) * ((float) i4)) + ((float) i4);
        i4 = Math.abs(i2);
        if (i4 > 0) {
            width = Math.round(Math.abs(distanceInfluenceForSnapDuration / ((float) i4)) * 1000.0f) * EDGE_TOP;
        } else {
            width = (int) (((((float) Math.abs(i)) / ((float) i3)) + 1.0f) * 256.0f);
        }
        return Math.min(width, MAX_SETTLE_DURATION);
    }

    private int clampMag(int i, int i2, int i3) {
        int abs = Math.abs(i);
        if (abs < i2) {
            return STATE_IDLE;
        }
        if (abs <= i3) {
            return i;
        }
        if (i <= 0) {
            return -i3;
        }
        return i3;
    }

    private float clampMag(float f, float f2, float f3) {
        float abs = Math.abs(f);
        if (abs < f2) {
            return 0.0f;
        }
        if (abs <= f3) {
            return f;
        }
        if (f <= 0.0f) {
            return -f3;
        }
        return f3;
    }

    private float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
    }

    public void flingCapturedView(int i, int i2, int i3, int i4) {
        if (this.mReleaseInProgress) {
            this.mScroller.fling(this.mCapturedView.getLeft(), this.mCapturedView.getTop(), (int) VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), (int) VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), i, i3, i2, i4);
            setDragState(STATE_SETTLING);
            return;
        }
        throw new IllegalStateException("Cannot flingCapturedView outside of a call to Callback#onViewReleased");
    }

    public boolean continueSettling(boolean z) {
        if (this.mDragState == STATE_SETTLING) {
            boolean isFinished;
            boolean computeScrollOffset = this.mScroller.computeScrollOffset();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            int left = currX - this.mCapturedView.getLeft();
            int top = currY - this.mCapturedView.getTop();
            if (left != 0) {
                this.mCapturedView.offsetLeftAndRight(left);
            }
            if (top != 0) {
                this.mCapturedView.offsetTopAndBottom(top);
            }
            if (!(left == 0 && top == 0)) {
                this.mCallback.onViewPositionChanged(this.mCapturedView, currX, currY, left, top);
            }
            if (computeScrollOffset && currX == this.mScroller.getFinalX() && currY == this.mScroller.getFinalY()) {
                this.mScroller.abortAnimation();
                isFinished = this.mScroller.isFinished();
            } else {
                isFinished = computeScrollOffset;
            }
            if (!isFinished) {
                if (z) {
                    this.mParentView.post(this.mSetIdleRunnable);
                } else {
                    setDragState(STATE_IDLE);
                }
            }
        }
        return this.mDragState == STATE_SETTLING;
    }

    private void dispatchViewReleased(float f, float f2) {
        this.mReleaseInProgress = true;
        this.mCallback.onViewReleased(this.mCapturedView, f, f2);
        this.mReleaseInProgress = false;
        if (this.mDragState == STATE_DRAGGING) {
            setDragState(STATE_IDLE);
        }
    }

    private void clearMotionHistory() {
        if (this.mInitialMotionX != null) {
            Arrays.fill(this.mInitialMotionX, 0.0f);
            Arrays.fill(this.mInitialMotionY, 0.0f);
            Arrays.fill(this.mLastMotionX, 0.0f);
            Arrays.fill(this.mLastMotionY, 0.0f);
            Arrays.fill(this.mInitialEdgesTouched, STATE_IDLE);
            Arrays.fill(this.mEdgeDragsInProgress, STATE_IDLE);
            Arrays.fill(this.mEdgeDragsLocked, STATE_IDLE);
            this.mPointersDown = STATE_IDLE;
        }
    }

    private void clearMotionHistory(int i) {
        if (this.mInitialMotionX != null) {
            this.mInitialMotionX[i] = 0.0f;
            this.mInitialMotionY[i] = 0.0f;
            this.mLastMotionX[i] = 0.0f;
            this.mLastMotionY[i] = 0.0f;
            this.mInitialEdgesTouched[i] = STATE_IDLE;
            this.mEdgeDragsInProgress[i] = STATE_IDLE;
            this.mEdgeDragsLocked[i] = STATE_IDLE;
            this.mPointersDown &= (STATE_DRAGGING << i) ^ INVALID_POINTER;
        }
    }

    private void ensureMotionHistorySizeForId(int i) {
        if (this.mInitialMotionX == null || this.mInitialMotionX.length <= i) {
            Object obj = new float[(i + STATE_DRAGGING)];
            Object obj2 = new float[(i + STATE_DRAGGING)];
            Object obj3 = new float[(i + STATE_DRAGGING)];
            Object obj4 = new float[(i + STATE_DRAGGING)];
            Object obj5 = new int[(i + STATE_DRAGGING)];
            Object obj6 = new int[(i + STATE_DRAGGING)];
            Object obj7 = new int[(i + STATE_DRAGGING)];
            if (this.mInitialMotionX != null) {
                System.arraycopy(this.mInitialMotionX, STATE_IDLE, obj, STATE_IDLE, this.mInitialMotionX.length);
                System.arraycopy(this.mInitialMotionY, STATE_IDLE, obj2, STATE_IDLE, this.mInitialMotionY.length);
                System.arraycopy(this.mLastMotionX, STATE_IDLE, obj3, STATE_IDLE, this.mLastMotionX.length);
                System.arraycopy(this.mLastMotionY, STATE_IDLE, obj4, STATE_IDLE, this.mLastMotionY.length);
                System.arraycopy(this.mInitialEdgesTouched, STATE_IDLE, obj5, STATE_IDLE, this.mInitialEdgesTouched.length);
                System.arraycopy(this.mEdgeDragsInProgress, STATE_IDLE, obj6, STATE_IDLE, this.mEdgeDragsInProgress.length);
                System.arraycopy(this.mEdgeDragsLocked, STATE_IDLE, obj7, STATE_IDLE, this.mEdgeDragsLocked.length);
            }
            this.mInitialMotionX = obj;
            this.mInitialMotionY = obj2;
            this.mLastMotionX = obj3;
            this.mLastMotionY = obj4;
            this.mInitialEdgesTouched = obj5;
            this.mEdgeDragsInProgress = obj6;
            this.mEdgeDragsLocked = obj7;
        }
    }

    private void saveInitialMotion(float f, float f2, int i) {
        ensureMotionHistorySizeForId(i);
        float[] fArr = this.mInitialMotionX;
        this.mLastMotionX[i] = f;
        fArr[i] = f;
        fArr = this.mInitialMotionY;
        this.mLastMotionY[i] = f2;
        fArr[i] = f2;
        this.mInitialEdgesTouched[i] = getEdgesTouched((int) f, (int) f2);
        this.mPointersDown |= STATE_DRAGGING << i;
    }

    private void saveLastMotion(MotionEvent motionEvent) {
        int pointerCount = MotionEventCompat.getPointerCount(motionEvent);
        for (int i = STATE_IDLE; i < pointerCount; i += STATE_DRAGGING) {
            int pointerId = MotionEventCompat.getPointerId(motionEvent, i);
            float x = MotionEventCompat.getX(motionEvent, i);
            float y = MotionEventCompat.getY(motionEvent, i);
            this.mLastMotionX[pointerId] = x;
            this.mLastMotionY[pointerId] = y;
        }
    }

    public boolean isPointerDown(int i) {
        return (this.mPointersDown & (STATE_DRAGGING << i)) != 0;
    }

    void setDragState(int i) {
        if (this.mDragState != i) {
            this.mDragState = i;
            this.mCallback.onViewDragStateChanged(i);
            if (i == 0) {
                this.mCapturedView = null;
            }
        }
    }

    boolean tryCaptureViewForDrag(View view, int i) {
        if (view == this.mCapturedView && this.mActivePointerId == i) {
            return true;
        }
        if (view == null || !this.mCallback.tryCaptureView(view, i)) {
            return false;
        }
        this.mActivePointerId = i;
        captureChildView(view, i);
        return true;
    }

    protected boolean canScroll(View view, boolean z, int i, int i2, int i3, int i4) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int scrollX = view.getScrollX();
            int scrollY = view.getScrollY();
            for (int childCount = viewGroup.getChildCount() + INVALID_POINTER; childCount >= 0; childCount += INVALID_POINTER) {
                View childAt = viewGroup.getChildAt(childCount);
                if (i3 + scrollX >= childAt.getLeft() && i3 + scrollX < childAt.getRight() && i4 + scrollY >= childAt.getTop() && i4 + scrollY < childAt.getBottom()) {
                    if (canScroll(childAt, true, i, i2, (i3 + scrollX) - childAt.getLeft(), (i4 + scrollY) - childAt.getTop())) {
                        return true;
                    }
                }
            }
        }
        return z && (ViewCompat.canScrollHorizontally(view, -i) || ViewCompat.canScrollVertically(view, -i2));
    }

    public boolean shouldInterceptTouchEvent(MotionEvent motionEvent) {
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (actionMasked == 0) {
            cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        float y;
        int pointerId;
        switch (actionMasked) {
            case STATE_IDLE /*0*/:
                float x = motionEvent.getX();
                y = motionEvent.getY();
                pointerId = MotionEventCompat.getPointerId(motionEvent, STATE_IDLE);
                saveInitialMotion(x, y, pointerId);
                View findTopChildUnder = findTopChildUnder((int) x, (int) y);
                if (findTopChildUnder == this.mCapturedView && this.mDragState == STATE_SETTLING) {
                    tryCaptureViewForDrag(findTopChildUnder, pointerId);
                }
                actionMasked = this.mInitialEdgesTouched[pointerId];
                if ((this.mTrackingEdges & actionMasked) != 0) {
                    this.mCallback.onEdgeTouched(actionMasked & this.mTrackingEdges, pointerId);
                    break;
                }
                break;
            case STATE_DRAGGING /*1*/:
            case DIRECTION_ALL /*3*/:
                cancel();
                break;
            case STATE_SETTLING /*2*/:
                actionIndex = MotionEventCompat.getPointerCount(motionEvent);
                actionMasked = STATE_IDLE;
                while (actionMasked < actionIndex) {
                    pointerId = MotionEventCompat.getPointerId(motionEvent, actionMasked);
                    float x2 = MotionEventCompat.getX(motionEvent, actionMasked);
                    float y2 = MotionEventCompat.getY(motionEvent, actionMasked);
                    float f = x2 - this.mInitialMotionX[pointerId];
                    float f2 = y2 - this.mInitialMotionY[pointerId];
                    reportNewEdgeDrags(f, f2, pointerId);
                    if (this.mDragState != STATE_DRAGGING) {
                        View findTopChildUnder2 = findTopChildUnder((int) x2, (int) y2);
                        if (findTopChildUnder2 == null || !checkTouchSlop(findTopChildUnder2, f, f2) || !tryCaptureViewForDrag(findTopChildUnder2, pointerId)) {
                            actionMasked += STATE_DRAGGING;
                        }
                    }
                    saveLastMotion(motionEvent);
                    break;
                }
                saveLastMotion(motionEvent);
                break;
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                actionMasked = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                float x3 = MotionEventCompat.getX(motionEvent, actionIndex);
                y = MotionEventCompat.getY(motionEvent, actionIndex);
                saveInitialMotion(x3, y, actionMasked);
                if (this.mDragState != 0) {
                    if (this.mDragState == STATE_SETTLING) {
                        View findTopChildUnder3 = findTopChildUnder((int) x3, (int) y);
                        if (findTopChildUnder3 == this.mCapturedView) {
                            tryCaptureViewForDrag(findTopChildUnder3, actionMasked);
                            break;
                        }
                    }
                }
                actionIndex = this.mInitialEdgesTouched[actionMasked];
                if ((this.mTrackingEdges & actionIndex) != 0) {
                    this.mCallback.onEdgeTouched(actionIndex & this.mTrackingEdges, actionMasked);
                    break;
                }
                break;
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                clearMotionHistory(MotionEventCompat.getPointerId(motionEvent, actionIndex));
                break;
        }
        if (this.mDragState == STATE_DRAGGING) {
            return true;
        }
        return false;
    }

    public void processTouchEvent(MotionEvent motionEvent) {
        int i = STATE_IDLE;
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (actionMasked == 0) {
            cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        float x;
        float y;
        View findTopChildUnder;
        int i2;
        switch (actionMasked) {
            case STATE_IDLE /*0*/:
                x = motionEvent.getX();
                y = motionEvent.getY();
                i = MotionEventCompat.getPointerId(motionEvent, STATE_IDLE);
                findTopChildUnder = findTopChildUnder((int) x, (int) y);
                saveInitialMotion(x, y, i);
                tryCaptureViewForDrag(findTopChildUnder, i);
                i2 = this.mInitialEdgesTouched[i];
                if ((this.mTrackingEdges & i2) != 0) {
                    this.mCallback.onEdgeTouched(i2 & this.mTrackingEdges, i);
                }
            case STATE_DRAGGING /*1*/:
                if (this.mDragState == STATE_DRAGGING) {
                    releaseViewForPointerUp();
                }
                cancel();
            case STATE_SETTLING /*2*/:
                if (this.mDragState == STATE_DRAGGING) {
                    i = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
                    x = MotionEventCompat.getX(motionEvent, i);
                    i2 = (int) (x - this.mLastMotionX[this.mActivePointerId]);
                    i = (int) (MotionEventCompat.getY(motionEvent, i) - this.mLastMotionY[this.mActivePointerId]);
                    dragTo(this.mCapturedView.getLeft() + i2, this.mCapturedView.getTop() + i, i2, i);
                    saveLastMotion(motionEvent);
                    return;
                }
                i2 = MotionEventCompat.getPointerCount(motionEvent);
                while (i < i2) {
                    actionMasked = MotionEventCompat.getPointerId(motionEvent, i);
                    float x2 = MotionEventCompat.getX(motionEvent, i);
                    float y2 = MotionEventCompat.getY(motionEvent, i);
                    float f = x2 - this.mInitialMotionX[actionMasked];
                    float f2 = y2 - this.mInitialMotionY[actionMasked];
                    reportNewEdgeDrags(f, f2, actionMasked);
                    if (this.mDragState != STATE_DRAGGING) {
                        findTopChildUnder = findTopChildUnder((int) x2, (int) y2);
                        if (!checkTouchSlop(findTopChildUnder, f, f2) || !tryCaptureViewForDrag(findTopChildUnder, actionMasked)) {
                            i += STATE_DRAGGING;
                        }
                    }
                    saveLastMotion(motionEvent);
                }
                saveLastMotion(motionEvent);
            case DIRECTION_ALL /*3*/:
                if (this.mDragState == STATE_DRAGGING) {
                    dispatchViewReleased(0.0f, 0.0f);
                }
                cancel();
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                i = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                x = MotionEventCompat.getX(motionEvent, actionIndex);
                y = MotionEventCompat.getY(motionEvent, actionIndex);
                saveInitialMotion(x, y, i);
                if (this.mDragState == 0) {
                    tryCaptureViewForDrag(findTopChildUnder((int) x, (int) y), i);
                    i2 = this.mInitialEdgesTouched[i];
                    if ((this.mTrackingEdges & i2) != 0) {
                        this.mCallback.onEdgeTouched(i2 & this.mTrackingEdges, i);
                    }
                } else if (isCapturedViewUnder((int) x, (int) y)) {
                    tryCaptureViewForDrag(this.mCapturedView, i);
                }
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                actionMasked = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                if (this.mDragState == STATE_DRAGGING && actionMasked == this.mActivePointerId) {
                    actionIndex = MotionEventCompat.getPointerCount(motionEvent);
                    while (i < actionIndex) {
                        int pointerId = MotionEventCompat.getPointerId(motionEvent, i);
                        if (pointerId != this.mActivePointerId) {
                            if (findTopChildUnder((int) MotionEventCompat.getX(motionEvent, i), (int) MotionEventCompat.getY(motionEvent, i)) == this.mCapturedView && tryCaptureViewForDrag(this.mCapturedView, pointerId)) {
                                i = this.mActivePointerId;
                                if (i == INVALID_POINTER) {
                                    releaseViewForPointerUp();
                                }
                            }
                        }
                        i += STATE_DRAGGING;
                    }
                    i = INVALID_POINTER;
                    if (i == INVALID_POINTER) {
                        releaseViewForPointerUp();
                    }
                }
                clearMotionHistory(actionMasked);
            default:
        }
    }

    private void reportNewEdgeDrags(float f, float f2, int i) {
        int i2 = STATE_DRAGGING;
        if (!checkNewEdgeDrag(f, f2, i, STATE_DRAGGING)) {
            i2 = STATE_IDLE;
        }
        if (checkNewEdgeDrag(f2, f, i, EDGE_TOP)) {
            i2 |= EDGE_TOP;
        }
        if (checkNewEdgeDrag(f, f2, i, STATE_SETTLING)) {
            i2 |= STATE_SETTLING;
        }
        if (checkNewEdgeDrag(f2, f, i, EDGE_BOTTOM)) {
            i2 |= EDGE_BOTTOM;
        }
        if (i2 != 0) {
            int[] iArr = this.mEdgeDragsInProgress;
            iArr[i] = iArr[i] | i2;
            this.mCallback.onEdgeDragStarted(i2, i);
        }
    }

    private boolean checkNewEdgeDrag(float f, float f2, int i, int i2) {
        float abs = Math.abs(f);
        float abs2 = Math.abs(f2);
        if ((this.mInitialEdgesTouched[i] & i2) != i2 || (this.mTrackingEdges & i2) == 0 || (this.mEdgeDragsLocked[i] & i2) == i2 || (this.mEdgeDragsInProgress[i] & i2) == i2) {
            return false;
        }
        if (abs <= ((float) this.mTouchSlop) && abs2 <= ((float) this.mTouchSlop)) {
            return false;
        }
        if (abs < abs2 * 0.5f && this.mCallback.onEdgeLock(i2)) {
            int[] iArr = this.mEdgeDragsLocked;
            iArr[i] = iArr[i] | i2;
            return false;
        } else if ((this.mEdgeDragsInProgress[i] & i2) != 0 || abs <= ((float) this.mTouchSlop)) {
            return false;
        } else {
            return true;
        }
    }

    private boolean checkTouchSlop(View view, float f, float f2) {
        if (view == null) {
            return false;
        }
        boolean z;
        boolean z2;
        if (this.mCallback.getViewHorizontalDragRange(view) > 0) {
            z = true;
        } else {
            z = STATE_IDLE;
        }
        if (this.mCallback.getViewVerticalDragRange(view) > 0) {
            z2 = true;
        } else {
            z2 = STATE_IDLE;
        }
        if (z && z2) {
            if ((f * f) + (f2 * f2) <= ((float) (this.mTouchSlop * this.mTouchSlop))) {
                return false;
            }
            return true;
        } else if (z) {
            if (Math.abs(f) <= ((float) this.mTouchSlop)) {
                return false;
            }
            return true;
        } else if (!z2) {
            return false;
        } else {
            if (Math.abs(f2) <= ((float) this.mTouchSlop)) {
                return false;
            }
            return true;
        }
    }

    public boolean checkTouchSlop(int i) {
        int length = this.mInitialMotionX.length;
        for (int i2 = STATE_IDLE; i2 < length; i2 += STATE_DRAGGING) {
            if (checkTouchSlop(i, i2)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkTouchSlop(int i, int i2) {
        if (!isPointerDown(i2)) {
            return false;
        }
        boolean z;
        boolean z2 = (i & STATE_DRAGGING) == STATE_DRAGGING ? true : STATE_IDLE;
        if ((i & STATE_SETTLING) == STATE_SETTLING) {
            z = true;
        } else {
            z = STATE_IDLE;
        }
        float f = this.mLastMotionX[i2] - this.mInitialMotionX[i2];
        float f2 = this.mLastMotionY[i2] - this.mInitialMotionY[i2];
        if (z2 && z) {
            if ((f * f) + (f2 * f2) <= ((float) (this.mTouchSlop * this.mTouchSlop))) {
                return false;
            }
            return true;
        } else if (z2) {
            if (Math.abs(f) <= ((float) this.mTouchSlop)) {
                return false;
            }
            return true;
        } else if (!z) {
            return false;
        } else {
            if (Math.abs(f2) <= ((float) this.mTouchSlop)) {
                return false;
            }
            return true;
        }
    }

    public boolean isEdgeTouched(int i) {
        int length = this.mInitialEdgesTouched.length;
        for (int i2 = STATE_IDLE; i2 < length; i2 += STATE_DRAGGING) {
            if (isEdgeTouched(i, i2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEdgeTouched(int i, int i2) {
        return isPointerDown(i2) && (this.mInitialEdgesTouched[i2] & i) != 0;
    }

    private void releaseViewForPointerUp() {
        this.mVelocityTracker.computeCurrentVelocity(LocationStatusCodes.GEOFENCE_NOT_AVAILABLE, this.mMaxVelocity);
        dispatchViewReleased(clampMag(VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity), clampMag(VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, this.mActivePointerId), this.mMinVelocity, this.mMaxVelocity));
    }

    private void dragTo(int i, int i2, int i3, int i4) {
        int clampViewPositionHorizontal;
        int clampViewPositionVertical;
        int left = this.mCapturedView.getLeft();
        int top = this.mCapturedView.getTop();
        if (i3 != 0) {
            clampViewPositionHorizontal = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, i, i3);
            this.mCapturedView.offsetLeftAndRight(clampViewPositionHorizontal - left);
        } else {
            clampViewPositionHorizontal = i;
        }
        if (i4 != 0) {
            clampViewPositionVertical = this.mCallback.clampViewPositionVertical(this.mCapturedView, i2, i4);
            this.mCapturedView.offsetTopAndBottom(clampViewPositionVertical - top);
        } else {
            clampViewPositionVertical = i2;
        }
        if (i3 != 0 || i4 != 0) {
            this.mCallback.onViewPositionChanged(this.mCapturedView, clampViewPositionHorizontal, clampViewPositionVertical, clampViewPositionHorizontal - left, clampViewPositionVertical - top);
        }
    }

    public boolean isCapturedViewUnder(int i, int i2) {
        return isViewUnder(this.mCapturedView, i, i2);
    }

    public boolean isViewUnder(View view, int i, int i2) {
        if (view != null && i >= view.getLeft() && i < view.getRight() && i2 >= view.getTop() && i2 < view.getBottom()) {
            return true;
        }
        return false;
    }

    public View findTopChildUnder(int i, int i2) {
        for (int childCount = this.mParentView.getChildCount() + INVALID_POINTER; childCount >= 0; childCount += INVALID_POINTER) {
            View childAt = this.mParentView.getChildAt(this.mCallback.getOrderedChildIndex(childCount));
            if (i >= childAt.getLeft() && i < childAt.getRight() && i2 >= childAt.getTop() && i2 < childAt.getBottom()) {
                return childAt;
            }
        }
        return null;
    }

    private int getEdgesTouched(int i, int i2) {
        int i3 = STATE_IDLE;
        if (i < this.mParentView.getLeft() + this.mEdgeSize) {
            i3 = STATE_DRAGGING;
        }
        if (i2 < this.mParentView.getTop() + this.mEdgeSize) {
            i3 |= EDGE_TOP;
        }
        if (i > this.mParentView.getRight() - this.mEdgeSize) {
            i3 |= STATE_SETTLING;
        }
        if (i2 > this.mParentView.getBottom() - this.mEdgeSize) {
            return i3 | EDGE_BOTTOM;
        }
        return i3;
    }
}
