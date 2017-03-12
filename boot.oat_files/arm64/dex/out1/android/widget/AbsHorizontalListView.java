package android.widget;

import android.app.KeyguardManager;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.FilterComparison;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.DVFSHelper;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.StrictMode.Span;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.Settings$System;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.DragEvent;
import android.view.HapticFeedbackConstants;
import android.view.HapticPreDrawListener;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.View.BaseSavedState;
import android.view.ViewConfiguration;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewDebug.IntToString;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Filter.FilterListener;
import android.widget.RemoteViews.OnClickHandler;
import android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback;
import com.android.internal.R;
import com.samsung.android.feature.FloatingFeature;
import com.samsung.android.motion.MREvent;
import com.samsung.android.motion.MRListener;
import com.samsung.android.motion.MotionRecognitionManager;
import com.samsung.android.smartface.SmartFaceManager;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class AbsHorizontalListView extends AdapterView<ListAdapter> implements TextWatcher, OnGlobalLayoutListener, FilterListener, OnTouchModeChangeListener, RemoteAdapterConnectionCallback {
    private static final int CHECK_POSITION_SEARCH_DISTANCE = 20;
    public static final int CHOICE_MODE_MULTIPLE = 2;
    public static final int CHOICE_MODE_MULTIPLE_MODAL = 3;
    public static final int CHOICE_MODE_NONE = 0;
    public static final int CHOICE_MODE_SINGLE = 1;
    private static final int DRAGSCROLL_WORKING_ZONE_DP = 25;
    private static final int HOVERSCROLL_LEFT = 1;
    private static final int HOVERSCROLL_MOVE = 1;
    private static final int HOVERSCROLL_RIGHT = 2;
    private static final int INVALID_POINTER = -1;
    static final int LAYOUT_FORCE_BOTTOM = 3;
    static final int LAYOUT_FORCE_TOP = 1;
    static final int LAYOUT_MOVE_SELECTION = 6;
    static final int LAYOUT_NORMAL = 0;
    static final int LAYOUT_SET_SELECTION = 2;
    static final int LAYOUT_SPECIFIC = 4;
    static final int LAYOUT_SYNC = 5;
    static final int OVERSCROLL_LIMIT_DIVISOR = 3;
    private static final boolean PROFILE_FLINGING = false;
    private static final boolean PROFILE_SCROLLING = false;
    private static final String TAG = "AbsHorizontalListView";
    static final int TOUCH_MODE_DONE_WAITING = 2;
    static final int TOUCH_MODE_DOWN = 0;
    static final int TOUCH_MODE_FLING = 4;
    private static final int TOUCH_MODE_OFF = 1;
    private static final int TOUCH_MODE_ON = 0;
    static final int TOUCH_MODE_OVERFLING = 6;
    static final int TOUCH_MODE_OVERSCROLL = 5;
    static final int TOUCH_MODE_REST = -1;
    static final int TOUCH_MODE_SCROLL = 3;
    static final int TOUCH_MODE_TAP = 1;
    private static final int TOUCH_MODE_UNKNOWN = -1;
    public static final int TRANSCRIPT_MODE_ALWAYS_SCROLL = 2;
    public static final int TRANSCRIPT_MODE_DISABLED = 0;
    public static final int TRANSCRIPT_MODE_NORMAL = 1;
    static final Interpolator sLinearInterpolator = new LinearInterpolator();
    protected boolean AIR_VIEW_WINSET;
    private int HOVERSCROLL_DELAY;
    private int HOVERSCROLL_SPEED;
    private boolean USE_SET_INTEGRATOR_HAPTIC;
    private boolean isHoveringUIEnabled;
    private ListItemAccessibilityDelegate mAccessibilityDelegate;
    private int mActivePointerId;
    ListAdapter mAdapter;
    boolean mAdapterHasStableIds;
    private boolean mAlwaysDisableHoverHighlight;
    private AudioManager mAudioManager;
    private int mCacheColorHint;
    boolean mCachingActive;
    boolean mCachingStarted;
    SparseBooleanArray mCheckStates;
    LongSparseArray<Integer> mCheckedIdStates;
    int mCheckedItemCount;
    ActionMode mChoiceActionMode;
    int mChoiceMode;
    private Runnable mClearScrollingCache;
    private ContextMenuInfo mContextMenuInfo;
    public int mCurrentKeyCode;
    private DVFSHelper mDVFSHelper;
    private DVFSHelper mDVFSHelperCore;
    private boolean mDVFSLockAcquired;
    AdapterDataSetObserver mDataSetObserver;
    private InputConnection mDefInputConnection;
    private boolean mDeferNotifyDataSetChanged;
    private float mDensityScale;
    private int mDirection;
    private int mDragScrollWorkingZonePx;
    boolean mDrawSelectorOnTop;
    private EdgeEffect mEdgeGlowLeft;
    private EdgeEffect mEdgeGlowRight;
    boolean mFastScrollAlwaysVisible;
    boolean mFastScrollEnabled;
    private HorizontalFastScroller mFastScroller;
    private boolean mFiltered;
    private int mFirstPositionDistanceGuess;
    public int mFirstPressedPoint;
    private boolean mFlingProfilingStarted;
    private FlingRunnable mFlingRunnable;
    private Span mFlingStrictSpan;
    private boolean mForceTranscriptScroll;
    private boolean mForcedClick;
    private boolean mGlobalLayoutListenerAddedFilter;
    private int mGlowPaddingBottom;
    private int mGlowPaddingTop;
    private boolean mHapticOverScroll;
    private HapticPreDrawListener mHapticPreDrawListener;
    private boolean mHasWindowFocusForMotion;
    int mHeightMeasureSpec;
    public boolean mHoverAreaEnter;
    private HoverScrollHandler mHoverHandler;
    private int mHoverLeftAreaWidth;
    private int mHoverLeftAreaWidth_DP;
    private int mHoverPosition;
    private int mHoverRecognitionCurrentTime;
    private int mHoverRecognitionDurationTime;
    private int mHoverRecognitionStartTime;
    private int mHoverRightAreaWidth;
    private int mHoverRightAreaWidth_DP;
    private int mHoverScrollDirection;
    private boolean mHoverScrollEnable;
    private int mHoverScrollSpeed;
    private int mHoverScrollStartTime;
    private int mHoverScrollTimeInterval;
    private boolean mHoveredOnEllipsizedText;
    private boolean mIsChildViewEnabled;
    private boolean mIsCtrlkeyPressed;
    private boolean mIsDragScrolled;
    private boolean mIsHoverOverscrolled;
    private boolean mIsHoveredByMouse;
    private boolean mIsMultiFocusEnabled;
    private boolean mIsPnePressed;
    final boolean[] mIsScrap;
    private boolean mIsShiftkeyPressed;
    private int mLastAccessibilityScrollEventFromIndex;
    private int mLastAccessibilityScrollEventToIndex;
    private int mLastHandledItemCount;
    private int mLastPosition;
    private int mLastPositionDistanceGuess;
    private int mLastScrollState;
    private int mLastTouchMode;
    int mLastX;
    int mLayoutMode;
    Rect mListPadding;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    int mMotionCorrection;
    private boolean mMotionEnable;
    private MRListener mMotionListener;
    int mMotionPosition;
    private MotionRecognitionManager mMotionRecognitionManager;
    int mMotionViewNewLeft;
    int mMotionViewOriginalLeft;
    int mMotionX;
    int mMotionY;
    MultiChoiceModeWrapper mMultiChoiceModeCallback;
    Drawable mMultiFocusImage;
    public int mOldAdapterItemCount;
    public int mOldKeyCode;
    private OnScrollListener mOnScrollListener;
    int mOverflingDistance;
    int mOverscrollDistance;
    int mOverscrollMax;
    private final Thread mOwnerThread;
    private CheckForKeyLongPress mPendingCheckForKeyLongPress;
    private CheckForLongPress mPendingCheckForLongPress;
    private CheckForTap mPendingCheckForTap;
    private SavedState mPendingSync;
    private PerformClick mPerformClick;
    private int mPointerCount;
    PopupWindow mPopup;
    private boolean mPopupHidden;
    Runnable mPositionScrollAfterLayout;
    PositionScroller mPositionScroller;
    private InputConnectionWrapper mPublicInputConnection;
    final RecycleBin mRecycler;
    private RemoteViewsAdapter mRemoteAdapter;
    int mResurrectToPosition;
    View mScrollLeft;
    private boolean mScrollProfilingStarted;
    View mScrollRight;
    private Span mScrollStrictSpan;
    boolean mScrollingCacheEnabled;
    public int mSecondPressedPoint;
    int mSelectedLeft;
    int mSelectionBottomPadding;
    int mSelectionLeftPadding;
    int mSelectionRightPadding;
    int mSelectionTopPadding;
    Drawable mSelector;
    int mSelectorPosition;
    Rect mSelectorRect;
    private boolean mSmoothScrollbarEnabled;
    boolean mStackFromBottom;
    EditText mTextFilter;
    private boolean mTextFilterEnabled;
    private Rect mTouchFrame;
    int mTouchMode;
    private Runnable mTouchModeReset;
    private int mTouchSlop;
    private int mTranscriptMode;
    public int mTwCurrentFocusPosition;
    private boolean mTwCustomMultiChoiceMode;
    public int[] mTwPressItemListArray;
    public int mTwPressItemListIndex;
    private final int mTwScrollAmount;
    private TwSmoothScrollByMove mTwSmoothScrollByMove;
    private LinkedList<Integer> mTwTwScrollRemains;
    private float mVelocityScale;
    private VelocityTracker mVelocityTracker;

    class AdapterDataSetObserver extends AdapterDataSetObserver {
        AdapterDataSetObserver() {
            super();
        }

        public void onChanged() {
            super.onChanged();
            if (AbsHorizontalListView.this.mFastScroller != null) {
                AbsHorizontalListView.this.mFastScroller.onSectionsChanged();
            }
        }

        public void onInvalidated() {
            super.onInvalidated();
            if (AbsHorizontalListView.this.mFastScroller != null) {
                AbsHorizontalListView.this.mFastScroller.onSectionsChanged();
            }
        }
    }

    private class WindowRunnnable {
        private int mOriginalAttachCount;

        private WindowRunnnable() {
        }

        public void rememberWindowAttachCount() {
            this.mOriginalAttachCount = AbsHorizontalListView.this.getWindowAttachCount();
        }

        public boolean sameWindow() {
            return AbsHorizontalListView.this.getWindowAttachCount() == this.mOriginalAttachCount;
        }
    }

    private class CheckForKeyLongPress extends WindowRunnnable implements Runnable {
        private CheckForKeyLongPress() {
            super();
        }

        public void run() {
            if (AbsHorizontalListView.this.isPressed() && AbsHorizontalListView.this.mSelectedPosition >= 0) {
                View v = AbsHorizontalListView.this.getChildAt(AbsHorizontalListView.this.mSelectedPosition - AbsHorizontalListView.this.mFirstPosition);
                if (v != null) {
                    if (AbsHorizontalListView.this.mDataChanged) {
                        AbsHorizontalListView.this.setPressed(false);
                        if (v != null) {
                            v.setPressed(false);
                            return;
                        }
                        return;
                    }
                    boolean handled = false;
                    if (sameWindow()) {
                        handled = AbsHorizontalListView.this.performLongPress(v, AbsHorizontalListView.this.mSelectedPosition, AbsHorizontalListView.this.mSelectedRowId);
                    }
                    if (handled) {
                        AbsHorizontalListView.this.setPressed(false);
                        v.setPressed(false);
                    }
                }
            }
        }
    }

    private class CheckForLongPress extends WindowRunnnable implements Runnable {
        private CheckForLongPress() {
            super();
        }

        public void run() {
            View child = AbsHorizontalListView.this.getChildAt(AbsHorizontalListView.this.mMotionPosition - AbsHorizontalListView.this.mFirstPosition);
            if (child != null) {
                int longPressPosition = AbsHorizontalListView.this.mMotionPosition;
                long longPressId = AbsHorizontalListView.this.mAdapter.getItemId(AbsHorizontalListView.this.mMotionPosition);
                boolean handled = false;
                if (sameWindow() && !AbsHorizontalListView.this.mDataChanged) {
                    handled = AbsHorizontalListView.this.performLongPress(child, longPressPosition, longPressId);
                }
                if (handled) {
                    AbsHorizontalListView.this.mTouchMode = -1;
                    AbsHorizontalListView.this.setPressed(false);
                    child.setPressed(false);
                    return;
                }
                AbsHorizontalListView.this.mTouchMode = 2;
            }
        }
    }

    private final class CheckForTap implements Runnable {
        float x;
        float y;

        private CheckForTap() {
        }

        public void run() {
            if (AbsHorizontalListView.this.mTouchMode == 0) {
                AbsHorizontalListView.this.mTouchMode = 1;
                View child = AbsHorizontalListView.this.getChildAt(AbsHorizontalListView.this.mMotionPosition - AbsHorizontalListView.this.mFirstPosition);
                if (child != null) {
                    AbsHorizontalListView.this.mIsChildViewEnabled = child.isEnabled();
                }
                if (child != null && !child.hasFocusable()) {
                    AbsHorizontalListView.this.mLayoutMode = 0;
                    if (AbsHorizontalListView.this.mDataChanged) {
                        AbsHorizontalListView.this.mTouchMode = 2;
                        return;
                    }
                    child.setPressed(true);
                    AbsHorizontalListView.this.setPressed(true);
                    AbsHorizontalListView.this.layoutChildren();
                    AbsHorizontalListView.this.positionSelector(AbsHorizontalListView.this.mMotionPosition, child);
                    AbsHorizontalListView.this.refreshDrawableState();
                    int longPressTimeout = ViewConfiguration.getLongPressTimeout();
                    boolean longClickable = AbsHorizontalListView.this.isLongClickable();
                    if (AbsHorizontalListView.this.mSelector != null) {
                        Drawable d = AbsHorizontalListView.this.mSelector.getCurrent();
                        if (d != null && (d instanceof TransitionDrawable)) {
                            if (longClickable) {
                                ((TransitionDrawable) d).startTransition(longPressTimeout);
                            } else {
                                ((TransitionDrawable) d).resetTransition();
                            }
                        }
                        AbsHorizontalListView.this.mSelector.setHotspot(this.x, this.y);
                    }
                    if (longClickable) {
                        if (AbsHorizontalListView.this.mPendingCheckForLongPress == null) {
                            AbsHorizontalListView.this.mPendingCheckForLongPress = new CheckForLongPress();
                        }
                        AbsHorizontalListView.this.mPendingCheckForLongPress.rememberWindowAttachCount();
                        AbsHorizontalListView.this.postDelayed(AbsHorizontalListView.this.mPendingCheckForLongPress, (long) longPressTimeout);
                        return;
                    }
                    AbsHorizontalListView.this.mTouchMode = 2;
                }
            }
        }
    }

    private class FlingRunnable implements Runnable {
        private static final int FLYWHEEL_TIMEOUT = 40;
        private final Runnable mCheckFlywheel = new Runnable() {
            public void run() {
                int activeId = AbsHorizontalListView.this.mActivePointerId;
                VelocityTracker vt = AbsHorizontalListView.this.mVelocityTracker;
                OverScroller scroller = FlingRunnable.this.mScroller;
                if (vt != null && activeId != -1) {
                    vt.computeCurrentVelocity(1000, (float) AbsHorizontalListView.this.mMaximumVelocity);
                    float xvel = -vt.getXVelocity(activeId);
                    if (Math.abs(xvel) < ((float) AbsHorizontalListView.this.mMinimumVelocity) || !scroller.isScrollingInDirection(xvel, 0.0f)) {
                        FlingRunnable.this.endFling();
                        AbsHorizontalListView.this.mTouchMode = 3;
                        AbsHorizontalListView.this.reportScrollStateChange(1);
                        return;
                    }
                    AbsHorizontalListView.this.postDelayed(this, 40);
                }
            }
        };
        private int mLastFlingX;
        private final OverScroller mScroller;

        FlingRunnable() {
            this.mScroller = new OverScroller(AbsHorizontalListView.this.getContext());
        }

        void start(int initialVelocity) {
            int initialX;
            if (initialVelocity < 0) {
                initialX = Integer.MAX_VALUE;
            } else {
                initialX = 0;
            }
            this.mLastFlingX = initialX;
            this.mScroller.setInterpolator(null);
            this.mScroller.fling(initialX, 0, initialVelocity, 0, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            AbsHorizontalListView.this.mTouchMode = 4;
            AbsHorizontalListView.this.postOnAnimation(this);
            if (AbsHorizontalListView.this.mFlingStrictSpan == null) {
                AbsHorizontalListView.this.mFlingStrictSpan = StrictMode.enterCriticalSpan("AbsHorizontalListView-fling");
            }
        }

        void startSpringback() {
            if (this.mScroller.springBack(AbsHorizontalListView.this.mScrollX, 0, 0, 0, 0, 0)) {
                AbsHorizontalListView.this.mTouchMode = 6;
                AbsHorizontalListView.this.invalidate();
                AbsHorizontalListView.this.postOnAnimation(this);
                return;
            }
            AbsHorizontalListView.this.mTouchMode = -1;
            AbsHorizontalListView.this.reportScrollStateChange(0);
        }

        void startOverfling(int initialVelocity) {
            this.mScroller.setInterpolator(null);
            this.mScroller.fling(AbsHorizontalListView.this.mScrollX, 0, initialVelocity, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0, AbsHorizontalListView.this.getWidth(), 0);
            AbsHorizontalListView.this.mTouchMode = 6;
            AbsHorizontalListView.this.invalidate();
            AbsHorizontalListView.this.postOnAnimation(this);
        }

        void edgeReached(int delta) {
            this.mScroller.notifyHorizontalEdgeReached(AbsHorizontalListView.this.mScrollX, 0, AbsHorizontalListView.this.mOverflingDistance);
            int overscrollMode = AbsHorizontalListView.this.getOverScrollMode();
            if (overscrollMode == 0 || (overscrollMode == 1 && !AbsHorizontalListView.this.contentFits())) {
                AbsHorizontalListView.this.mTouchMode = 6;
                int vel = (int) this.mScroller.getCurrVelocity();
                if (delta > 0) {
                    AbsHorizontalListView.this.mEdgeGlowLeft.onAbsorb(vel);
                } else {
                    AbsHorizontalListView.this.mEdgeGlowRight.onAbsorb(vel);
                }
            } else {
                AbsHorizontalListView.this.mTouchMode = -1;
                if (AbsHorizontalListView.this.mPositionScroller != null) {
                    AbsHorizontalListView.this.mPositionScroller.stop();
                }
            }
            AbsHorizontalListView.this.invalidate();
            AbsHorizontalListView.this.postOnAnimation(this);
        }

        void startScroll(int distance, int duration, boolean linear) {
            int initialX;
            if (distance < 0) {
                initialX = Integer.MAX_VALUE;
            } else {
                initialX = 0;
            }
            this.mLastFlingX = initialX;
            this.mScroller.setInterpolator(linear ? AbsHorizontalListView.sLinearInterpolator : null);
            this.mScroller.startScroll(initialX, 0, distance, 0, duration);
            AbsHorizontalListView.this.mTouchMode = 4;
            AbsHorizontalListView.this.postOnAnimation(this);
        }

        void endFling() {
            AbsHorizontalListView.this.mTouchMode = -1;
            AbsHorizontalListView.this.removeCallbacks(this);
            AbsHorizontalListView.this.removeCallbacks(this.mCheckFlywheel);
            AbsHorizontalListView.this.reportScrollStateChange(0);
            AbsHorizontalListView.this.clearScrollingCache();
            this.mScroller.abortAnimation();
            if (AbsHorizontalListView.this.mFlingStrictSpan != null) {
                AbsHorizontalListView.this.mFlingStrictSpan.finish();
                AbsHorizontalListView.this.mFlingStrictSpan = null;
            }
        }

        void flywheelTouch() {
            AbsHorizontalListView.this.postDelayed(this.mCheckFlywheel, 40);
        }

        public void run() {
            OverScroller scroller;
            switch (AbsHorizontalListView.this.mTouchMode) {
                case 3:
                    if (this.mScroller.isFinished()) {
                        return;
                    }
                    break;
                case 4:
                    break;
                case 6:
                    scroller = this.mScroller;
                    if (scroller.computeScrollOffset()) {
                        int scrollX = AbsHorizontalListView.this.mScrollX;
                        int currX = scroller.getCurrX();
                        if (AbsHorizontalListView.this.overScrollBy(currX - scrollX, 0, scrollX, 0, 0, 0, AbsHorizontalListView.this.mOverflingDistance, 0, false)) {
                            boolean crossRight = scrollX <= 0 && currX > 0;
                            boolean crossLeft = scrollX >= 0 && currX < 0;
                            if (crossRight || crossLeft) {
                                int velocity = (int) scroller.getCurrVelocity();
                                if (crossLeft) {
                                    velocity = -velocity;
                                }
                                scroller.abortAnimation();
                                start(velocity);
                                return;
                            }
                            startSpringback();
                            return;
                        }
                        AbsHorizontalListView.this.invalidate();
                        AbsHorizontalListView.this.postOnAnimation(this);
                        return;
                    }
                    endFling();
                    return;
                default:
                    endFling();
                    return;
            }
            if (AbsHorizontalListView.this.mDataChanged) {
                AbsHorizontalListView.this.layoutChildren();
            }
            if (AbsHorizontalListView.this.mItemCount == 0 || AbsHorizontalListView.this.getChildCount() == 0) {
                endFling();
                return;
            }
            scroller = this.mScroller;
            boolean more = scroller.computeScrollOffset();
            int x = scroller.getCurrX();
            int delta = this.mLastFlingX - x;
            if (delta > 0) {
                AbsHorizontalListView.this.mMotionPosition = AbsHorizontalListView.this.mFirstPosition;
                AbsHorizontalListView.this.mMotionViewOriginalLeft = AbsHorizontalListView.this.getChildAt(0).getLeft();
                delta = Math.min(((AbsHorizontalListView.this.getWidth() - AbsHorizontalListView.this.mPaddingRight) - AbsHorizontalListView.this.mPaddingLeft) - 1, delta);
            } else {
                int offsetToLast = AbsHorizontalListView.this.getChildCount() - 1;
                AbsHorizontalListView.this.mMotionPosition = AbsHorizontalListView.this.mFirstPosition + offsetToLast;
                AbsHorizontalListView.this.mMotionViewOriginalLeft = AbsHorizontalListView.this.getChildAt(offsetToLast).getLeft();
                delta = Math.max(-(((AbsHorizontalListView.this.getWidth() - AbsHorizontalListView.this.mPaddingRight) - AbsHorizontalListView.this.mPaddingLeft) - 1), delta);
            }
            View motionView = AbsHorizontalListView.this.getChildAt(AbsHorizontalListView.this.mMotionPosition - AbsHorizontalListView.this.mFirstPosition);
            int oldLeft = 0;
            if (motionView != null) {
                oldLeft = motionView.getLeft();
            }
            boolean atEdge = AbsHorizontalListView.this.trackMotionScroll(delta, delta);
            boolean atEnd = atEdge && delta != 0;
            if (atEnd) {
                if (motionView != null) {
                    AbsHorizontalListView.this.overScrollBy(-(delta - (motionView.getLeft() - oldLeft)), 0, AbsHorizontalListView.this.mScrollX, 0, 0, 0, AbsHorizontalListView.this.mOverflingDistance, 0, false);
                }
                if (more) {
                    edgeReached(delta);
                }
            } else if (!more || atEnd) {
                endFling();
            } else {
                if (atEdge) {
                    AbsHorizontalListView.this.invalidate();
                }
                this.mLastFlingX = x;
                AbsHorizontalListView.this.postOnAnimation(this);
            }
        }
    }

    private class HoverScrollHandler extends Handler {
        private HoverScrollHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    AbsHorizontalListView.this.mHoverRecognitionCurrentTime = (int) System.currentTimeMillis();
                    AbsHorizontalListView.this.mHoverRecognitionDurationTime = (AbsHorizontalListView.this.mHoverRecognitionCurrentTime - AbsHorizontalListView.this.mHoverRecognitionStartTime) / 1000;
                    if (AbsHorizontalListView.this.mHoverRecognitionCurrentTime - AbsHorizontalListView.this.mHoverScrollStartTime >= AbsHorizontalListView.this.mHoverScrollTimeInterval) {
                        int offset;
                        int count = AbsHorizontalListView.this.getChildCount();
                        boolean canScrollRight = AbsHorizontalListView.this.mFirstPosition + count < AbsHorizontalListView.this.mItemCount;
                        if (!canScrollRight && count > 0) {
                            View child = AbsHorizontalListView.this.getChildAt(count - 1);
                            canScrollRight = child.getRight() > AbsHorizontalListView.this.mRight - AbsHorizontalListView.this.mListPadding.right || child.getRight() > AbsHorizontalListView.this.getWidth() - AbsHorizontalListView.this.mListPadding.right;
                        }
                        boolean canScrollLeft = AbsHorizontalListView.this.mFirstPosition > 0;
                        if (!canScrollLeft && AbsHorizontalListView.this.getChildCount() > 0) {
                            canScrollLeft = AbsHorizontalListView.this.getChildAt(0).getLeft() < AbsHorizontalListView.this.mListPadding.left;
                        }
                        if (AbsHorizontalListView.this.mHoverRecognitionDurationTime > 2 && AbsHorizontalListView.this.mHoverRecognitionDurationTime < 4) {
                            AbsHorizontalListView.this.mHoverScrollSpeed = AbsHorizontalListView.this.HOVERSCROLL_SPEED + 2;
                        } else if (AbsHorizontalListView.this.mHoverRecognitionDurationTime >= 4 && AbsHorizontalListView.this.mHoverRecognitionDurationTime < 5) {
                            AbsHorizontalListView.this.mHoverScrollSpeed = AbsHorizontalListView.this.HOVERSCROLL_SPEED + 4;
                        } else if (AbsHorizontalListView.this.mHoverRecognitionDurationTime >= 5) {
                            AbsHorizontalListView.this.mHoverScrollSpeed = AbsHorizontalListView.this.HOVERSCROLL_SPEED + 6;
                        } else {
                            AbsHorizontalListView.this.mHoverScrollSpeed = AbsHorizontalListView.this.HOVERSCROLL_SPEED;
                        }
                        if (AbsHorizontalListView.this.mHoverScrollDirection == 2) {
                            offset = AbsHorizontalListView.this.mHoverScrollSpeed * -1;
                        } else {
                            offset = AbsHorizontalListView.this.mHoverScrollSpeed * 1;
                        }
                        if (AbsHorizontalListView.this.getResources().getDisplayMetrics().heightPixels >= 1080) {
                            offset *= 2;
                        }
                        if (AbsHorizontalListView.this.getChildAt(AbsHorizontalListView.this.getChildCount() - 1) == null) {
                            return;
                        }
                        if ((offset >= 0 || !canScrollLeft) && (offset <= 0 || !canScrollRight)) {
                            int overscrollMode = AbsHorizontalListView.this.getOverScrollMode();
                            boolean canOverscroll = overscrollMode == 0 || (overscrollMode == 1 && !AbsHorizontalListView.this.contentFits());
                            if (canOverscroll && !AbsHorizontalListView.this.mIsHoverOverscrolled) {
                                if (AbsHorizontalListView.this.mHoverScrollDirection == 2) {
                                    AbsHorizontalListView.this.mEdgeGlowLeft.onPull(0.4f);
                                    if (!AbsHorizontalListView.this.mEdgeGlowRight.isFinished()) {
                                        AbsHorizontalListView.this.mEdgeGlowRight.onRelease();
                                    }
                                } else if (AbsHorizontalListView.this.mHoverScrollDirection == 1) {
                                    AbsHorizontalListView.this.mEdgeGlowRight.onPull(0.4f);
                                    if (!AbsHorizontalListView.this.mEdgeGlowLeft.isFinished()) {
                                        AbsHorizontalListView.this.mEdgeGlowLeft.onRelease();
                                    }
                                }
                                if (!(AbsHorizontalListView.this.mEdgeGlowLeft == null || (AbsHorizontalListView.this.mEdgeGlowLeft.isFinished() && AbsHorizontalListView.this.mEdgeGlowRight.isFinished()))) {
                                    AbsHorizontalListView.this.invalidate();
                                }
                                AbsHorizontalListView.this.mIsHoverOverscrolled = true;
                            }
                            if (!canOverscroll && !AbsHorizontalListView.this.mIsHoverOverscrolled) {
                                AbsHorizontalListView.this.mIsHoverOverscrolled = true;
                                return;
                            }
                            return;
                        }
                        AbsHorizontalListView.this.smoothScrollBy(offset, 0);
                        AbsHorizontalListView.this.mHoverHandler.sendEmptyMessageDelayed(1, (long) AbsHorizontalListView.this.HOVERSCROLL_DELAY);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        @ExportedProperty(category = "list")
        boolean forceAdd;
        long itemId = -1;
        @ExportedProperty(category = "list")
        boolean recycledHeaderFooter;
        int scrappedFromPosition;
        @ExportedProperty(category = "list", mapping = {@IntToString(from = -1, to = "ITEM_VIEW_TYPE_IGNORE"), @IntToString(from = -2, to = "ITEM_VIEW_TYPE_HEADER_OR_FOOTER")})
        int viewType;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(int w, int h, int viewType) {
            super(w, h);
            this.viewType = viewType;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    class ListItemAccessibilityDelegate extends AccessibilityDelegate {
        ListItemAccessibilityDelegate() {
        }

        public AccessibilityNodeInfo createAccessibilityNodeInfo(View host) {
            if (AbsHorizontalListView.this.mDataChanged) {
                return null;
            }
            return super.createAccessibilityNodeInfo(host);
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            AbsHorizontalListView.this.onInitializeAccessibilityNodeInfoForItem(host, AbsHorizontalListView.this.getPositionForView(host), info);
        }

        public boolean performAccessibilityAction(View host, int action, Bundle arguments) {
            if (super.performAccessibilityAction(host, action, arguments)) {
                return true;
            }
            int position = AbsHorizontalListView.this.getPositionForView(host);
            ListAdapter adapter = (ListAdapter) AbsHorizontalListView.this.getAdapter();
            if (position == -1 || adapter == null) {
                return false;
            }
            if (!AbsHorizontalListView.this.isEnabled() || !adapter.isEnabled(position)) {
                return false;
            }
            long id = AbsHorizontalListView.this.getItemIdAtPosition(position);
            switch (action) {
                case 4:
                    if (AbsHorizontalListView.this.getSelectedItemPosition() == position) {
                        return false;
                    }
                    AbsHorizontalListView.this.setSelection(position);
                    return true;
                case 8:
                    if (AbsHorizontalListView.this.getSelectedItemPosition() != position) {
                        return false;
                    }
                    AbsHorizontalListView.this.setSelection(-1);
                    return true;
                case 16:
                    return AbsHorizontalListView.this.isClickable() ? AbsHorizontalListView.this.performItemClick(host, position, id) : false;
                case 32:
                    return AbsHorizontalListView.this.isLongClickable() ? AbsHorizontalListView.this.performLongPress(host, position, id) : false;
                default:
                    return false;
            }
        }
    }

    public interface MultiChoiceModeListener extends Callback {
        void onItemCheckedStateChanged(ActionMode actionMode, int i, long j, boolean z);
    }

    class MultiChoiceModeWrapper implements MultiChoiceModeListener {
        private MultiChoiceModeListener mWrapped;

        MultiChoiceModeWrapper() {
        }

        public void setWrapped(MultiChoiceModeListener wrapped) {
            this.mWrapped = wrapped;
        }

        public boolean hasWrappedCallback() {
            return this.mWrapped != null;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            if (!this.mWrapped.onCreateActionMode(mode, menu)) {
                return false;
            }
            AbsHorizontalListView.this.setLongClickable(false);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(mode, menu);
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return this.mWrapped.onActionItemClicked(mode, item);
        }

        public void onDestroyActionMode(ActionMode mode) {
            this.mWrapped.onDestroyActionMode(mode);
            AbsHorizontalListView.this.mChoiceActionMode = null;
            AbsHorizontalListView.this.clearChoices();
            AbsHorizontalListView.this.mDataChanged = true;
            AbsHorizontalListView.this.rememberSyncState();
            AbsHorizontalListView.this.requestLayout();
            AbsHorizontalListView.this.setLongClickable(true);
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            this.mWrapped.onItemCheckedStateChanged(mode, position, id, checked);
            if (AbsHorizontalListView.this.getCheckedItemCount() == 0 && !AbsHorizontalListView.this.mTwCustomMultiChoiceMode) {
                mode.finish();
            }
        }
    }

    public interface OnScrollListener {
        public static final int SCROLL_STATE_FLING = 2;
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

        void onScroll(AbsHorizontalListView absHorizontalListView, int i, int i2, int i3);

        void onScrollStateChanged(AbsHorizontalListView absHorizontalListView, int i);
    }

    private class PerformClick extends WindowRunnnable implements Runnable {
        int mClickMotionPosition;

        private PerformClick() {
            super();
        }

        public void run() {
            if (AbsHorizontalListView.this.mForcedClick || !AbsHorizontalListView.this.mDataChanged) {
                ListAdapter adapter = AbsHorizontalListView.this.mAdapter;
                int motionPosition = this.mClickMotionPosition;
                if (adapter != null && AbsHorizontalListView.this.mItemCount > 0 && motionPosition != -1 && motionPosition < adapter.getCount() && sameWindow()) {
                    View view = AbsHorizontalListView.this.getChildAt(motionPosition - AbsHorizontalListView.this.mFirstPosition);
                    if (view != null) {
                        try {
                            AbsHorizontalListView.this.performItemClick(view, motionPosition, adapter.getItemId(motionPosition));
                            if (AbsHorizontalListView.this.mIsShiftkeyPressed || AbsHorizontalListView.this.mIsCtrlkeyPressed) {
                                boolean handledNotifykeyPress = AbsHorizontalListView.this.twNotifyKeyPressState(view, motionPosition, adapter.getItemId(motionPosition));
                            }
                            if ((!AbsHorizontalListView.this.mIsShiftkeyPressed && !AbsHorizontalListView.this.mIsCtrlkeyPressed) || AbsHorizontalListView.this.mTwPressItemListIndex >= AbsHorizontalListView.this.mAdapter.getCount()) {
                                return;
                            }
                            if (AbsHorizontalListView.this.mIsCtrlkeyPressed) {
                                AbsHorizontalListView.this.addToPressItemListArray(motionPosition, -1);
                            } else if (AbsHorizontalListView.this.mIsShiftkeyPressed) {
                                AbsHorizontalListView.this.resetPressItemListArray();
                                if (AbsHorizontalListView.this.mFirstPressedPoint == -1) {
                                    AbsHorizontalListView.this.addToPressItemListArray(motionPosition, -1);
                                    AbsHorizontalListView.this.mFirstPressedPoint = motionPosition;
                                    return;
                                }
                                AbsHorizontalListView.this.addToPressItemListArray(AbsHorizontalListView.this.mFirstPressedPoint, motionPosition);
                            }
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    class PositionScroller implements Runnable {
        private static final int MOVE_DOWN_BOUND = 3;
        private static final int MOVE_DOWN_POS = 1;
        private static final int MOVE_OFFSET = 5;
        private static final int MOVE_UP_BOUND = 4;
        private static final int MOVE_UP_POS = 2;
        private static final int SCROLL_DURATION = 200;
        private int mBoundPos;
        private final int mExtraScroll;
        private int mLastSeenPos;
        private int mMode;
        private int mOffsetFromLeft;
        private int mScrollDuration;
        private int mTargetPos;

        PositionScroller() {
            this.mExtraScroll = ViewConfiguration.get(AbsHorizontalListView.this.mContext).getScaledFadingEdgeLength();
        }

        void start(final int position) {
            stop();
            if (AbsHorizontalListView.this.mDataChanged) {
                AbsHorizontalListView.this.mPositionScrollAfterLayout = new Runnable() {
                    public void run() {
                        PositionScroller.this.start(position);
                    }
                };
                return;
            }
            int childCount = AbsHorizontalListView.this.getChildCount();
            if (childCount != 0) {
                int viewTravelCount;
                int firstPos = AbsHorizontalListView.this.mFirstPosition;
                int lastPos = (firstPos + childCount) - 1;
                int clampedPosition = Math.max(0, Math.min(AbsHorizontalListView.this.getCount() - 1, position));
                if (clampedPosition < firstPos) {
                    viewTravelCount = (firstPos - clampedPosition) + 1;
                    this.mMode = 2;
                } else if (clampedPosition > lastPos) {
                    viewTravelCount = (clampedPosition - lastPos) + 1;
                    this.mMode = 1;
                } else {
                    scrollToVisible(clampedPosition, -1, 200);
                    return;
                }
                if (viewTravelCount > 0) {
                    this.mScrollDuration = 200 / viewTravelCount;
                } else {
                    this.mScrollDuration = 200;
                }
                this.mTargetPos = clampedPosition;
                this.mBoundPos = -1;
                this.mLastSeenPos = -1;
                AbsHorizontalListView.this.postOnAnimation(this);
            }
        }

        void start(final int position, final int boundPosition) {
            stop();
            if (boundPosition == -1) {
                start(position);
            } else if (AbsHorizontalListView.this.mDataChanged) {
                AbsHorizontalListView.this.mPositionScrollAfterLayout = new Runnable() {
                    public void run() {
                        PositionScroller.this.start(position, boundPosition);
                    }
                };
            } else {
                int childCount = AbsHorizontalListView.this.getChildCount();
                if (childCount != 0) {
                    int viewTravelCount;
                    int firstPos = AbsHorizontalListView.this.mFirstPosition;
                    int lastPos = (firstPos + childCount) - 1;
                    int clampedPosition = Math.max(0, Math.min(AbsHorizontalListView.this.getCount() - 1, position));
                    int posTravel;
                    int boundTravel;
                    if (clampedPosition < firstPos) {
                        int boundPosFromLast = lastPos - boundPosition;
                        if (boundPosFromLast >= 1) {
                            posTravel = (firstPos - clampedPosition) + 1;
                            boundTravel = boundPosFromLast - 1;
                            if (boundTravel < posTravel) {
                                viewTravelCount = boundTravel;
                                this.mMode = 4;
                            } else {
                                viewTravelCount = posTravel;
                                this.mMode = 2;
                            }
                        } else {
                            return;
                        }
                    } else if (clampedPosition > lastPos) {
                        int boundPosFromFirst = boundPosition - firstPos;
                        if (boundPosFromFirst >= 1) {
                            posTravel = (clampedPosition - lastPos) + 1;
                            boundTravel = boundPosFromFirst - 1;
                            if (boundTravel < posTravel) {
                                viewTravelCount = boundTravel;
                                this.mMode = 3;
                            } else {
                                viewTravelCount = posTravel;
                                this.mMode = 1;
                            }
                        } else {
                            return;
                        }
                    } else {
                        scrollToVisible(clampedPosition, boundPosition, 200);
                        return;
                    }
                    if (viewTravelCount > 0) {
                        this.mScrollDuration = 200 / viewTravelCount;
                    } else {
                        this.mScrollDuration = 200;
                    }
                    this.mTargetPos = clampedPosition;
                    this.mBoundPos = boundPosition;
                    this.mLastSeenPos = -1;
                    AbsHorizontalListView.this.postOnAnimation(this);
                }
            }
        }

        void startWithOffset(int position, int offset) {
            startWithOffset(position, offset, 200);
        }

        void startWithOffset(final int position, int offset, final int duration) {
            stop();
            if (AbsHorizontalListView.this.mDataChanged) {
                final int postOffset = offset;
                AbsHorizontalListView.this.mPositionScrollAfterLayout = new Runnable() {
                    public void run() {
                        PositionScroller.this.startWithOffset(position, postOffset, duration);
                    }
                };
                return;
            }
            int childCount = AbsHorizontalListView.this.getChildCount();
            if (childCount != 0) {
                int viewTravelCount;
                offset += AbsHorizontalListView.this.getPaddingLeft();
                this.mTargetPos = Math.max(0, Math.min(AbsHorizontalListView.this.getCount() - 1, position));
                this.mOffsetFromLeft = offset;
                this.mBoundPos = -1;
                this.mLastSeenPos = -1;
                this.mMode = 5;
                int firstPos = AbsHorizontalListView.this.mFirstPosition;
                int lastPos = (firstPos + childCount) - 1;
                if (this.mTargetPos < firstPos) {
                    viewTravelCount = firstPos - this.mTargetPos;
                } else if (this.mTargetPos > lastPos) {
                    viewTravelCount = this.mTargetPos - lastPos;
                } else {
                    AbsHorizontalListView.this.smoothScrollBy(AbsHorizontalListView.this.getChildAt(this.mTargetPos - firstPos).getLeft() - offset, duration, true);
                    return;
                }
                float screenTravelCount = ((float) viewTravelCount) / ((float) childCount);
                if (screenTravelCount >= 1.0f) {
                    duration = (int) (((float) duration) / screenTravelCount);
                }
                this.mScrollDuration = duration;
                this.mLastSeenPos = -1;
                AbsHorizontalListView.this.postOnAnimation(this);
            }
        }

        void scrollToVisible(int targetPos, int boundPos, int duration) {
            int firstPos = AbsHorizontalListView.this.mFirstPosition;
            int lastPos = (firstPos + AbsHorizontalListView.this.getChildCount()) - 1;
            int paddedLeft = AbsHorizontalListView.this.mListPadding.left;
            int paddedRight = AbsHorizontalListView.this.getWidth() - AbsHorizontalListView.this.mListPadding.right;
            if (targetPos < firstPos || targetPos > lastPos) {
                Log.w(AbsHorizontalListView.TAG, "scrollToVisible called with targetPos " + targetPos + " not visible [" + firstPos + ", " + lastPos + "]");
            }
            if (boundPos < firstPos || boundPos > lastPos) {
                boundPos = -1;
            }
            View targetChild = AbsHorizontalListView.this.getChildAt(targetPos - firstPos);
            int targetLeft = targetChild.getLeft();
            int targetRight = targetChild.getRight();
            int scrollBy = 0;
            if (targetRight > paddedRight) {
                scrollBy = targetRight - paddedRight;
            }
            if (targetLeft < paddedLeft) {
                scrollBy = targetLeft - paddedLeft;
            }
            if (scrollBy != 0) {
                if (boundPos >= 0) {
                    View boundChild = AbsHorizontalListView.this.getChildAt(boundPos - firstPos);
                    int boundLeft = boundChild.getLeft();
                    int boundRight = boundChild.getRight();
                    int absScroll = Math.abs(scrollBy);
                    if (scrollBy < 0 && boundRight + absScroll > paddedRight) {
                        scrollBy = Math.max(0, boundRight - paddedRight);
                    } else if (scrollBy > 0 && boundLeft - absScroll < paddedLeft) {
                        scrollBy = Math.min(0, boundLeft - paddedLeft);
                    }
                }
                AbsHorizontalListView.this.smoothScrollBy(scrollBy, duration);
            }
        }

        void stop() {
            AbsHorizontalListView.this.removeCallbacks(this);
        }

        public void run() {
            int listWidth = AbsHorizontalListView.this.getWidth();
            int firstPos = AbsHorizontalListView.this.mFirstPosition;
            int lastViewIndex;
            int lastPos;
            View lastView;
            int lastViewWidth;
            int lastViewPixelsShowing;
            int extraScroll;
            int childCount;
            switch (this.mMode) {
                case 1:
                    lastViewIndex = AbsHorizontalListView.this.getChildCount() - 1;
                    lastPos = firstPos + lastViewIndex;
                    if (lastViewIndex < 0) {
                        return;
                    }
                    if (lastPos == this.mLastSeenPos) {
                        AbsHorizontalListView.this.postOnAnimation(this);
                        return;
                    }
                    lastView = AbsHorizontalListView.this.getChildAt(lastViewIndex);
                    lastViewWidth = lastView.getWidth();
                    lastViewPixelsShowing = listWidth - lastView.getLeft();
                    if (lastPos < AbsHorizontalListView.this.mItemCount - 1) {
                        extraScroll = Math.max(AbsHorizontalListView.this.mListPadding.right, this.mExtraScroll);
                    } else {
                        extraScroll = AbsHorizontalListView.this.mListPadding.right;
                    }
                    int scrollBy = (lastViewWidth - lastViewPixelsShowing) + extraScroll;
                    AbsHorizontalListView.this.smoothScrollBy(scrollBy, this.mScrollDuration, true);
                    this.mLastSeenPos = lastPos;
                    if (lastPos < this.mTargetPos) {
                        AbsHorizontalListView.this.postOnAnimation(this);
                        return;
                    }
                    return;
                case 2:
                    if (firstPos == this.mLastSeenPos) {
                        AbsHorizontalListView.this.postOnAnimation(this);
                        return;
                    }
                    View firstView = AbsHorizontalListView.this.getChildAt(0);
                    if (firstView != null) {
                        int firstViewLeft = firstView.getLeft();
                        if (firstPos > 0) {
                            extraScroll = Math.max(this.mExtraScroll, AbsHorizontalListView.this.mListPadding.left);
                        } else {
                            extraScroll = AbsHorizontalListView.this.mListPadding.left;
                        }
                        AbsHorizontalListView.this.smoothScrollBy(firstViewLeft - extraScroll, this.mScrollDuration, true);
                        this.mLastSeenPos = firstPos;
                        if (firstPos > this.mTargetPos) {
                            AbsHorizontalListView.this.postOnAnimation(this);
                            return;
                        }
                        return;
                    }
                    return;
                case 3:
                    childCount = AbsHorizontalListView.this.getChildCount();
                    if (firstPos != this.mBoundPos && childCount > 1 && firstPos + childCount < AbsHorizontalListView.this.mItemCount) {
                        int nextPos = firstPos + 1;
                        if (nextPos == this.mLastSeenPos) {
                            AbsHorizontalListView.this.postOnAnimation(this);
                            return;
                        }
                        View nextView = AbsHorizontalListView.this.getChildAt(1);
                        int nextViewWidth = nextView.getWidth();
                        int nextViewLeft = nextView.getLeft();
                        extraScroll = Math.max(AbsHorizontalListView.this.mListPadding.right, this.mExtraScroll);
                        if (nextPos < this.mBoundPos) {
                            AbsHorizontalListView.this.smoothScrollBy(Math.max(0, (nextViewWidth + nextViewLeft) - extraScroll), this.mScrollDuration, true);
                            this.mLastSeenPos = nextPos;
                            AbsHorizontalListView.this.postOnAnimation(this);
                            return;
                        } else if (nextViewLeft > extraScroll) {
                            AbsHorizontalListView.this.smoothScrollBy(nextViewLeft - extraScroll, this.mScrollDuration, true);
                            return;
                        } else {
                            return;
                        }
                    }
                    return;
                case 4:
                    lastViewIndex = AbsHorizontalListView.this.getChildCount() - 2;
                    if (lastViewIndex >= 0) {
                        lastPos = firstPos + lastViewIndex;
                        if (lastPos == this.mLastSeenPos) {
                            AbsHorizontalListView.this.postOnAnimation(this);
                            return;
                        }
                        lastView = AbsHorizontalListView.this.getChildAt(lastViewIndex);
                        lastViewWidth = lastView.getWidth();
                        int lastViewLeft = lastView.getLeft();
                        lastViewPixelsShowing = listWidth - lastViewLeft;
                        extraScroll = Math.max(AbsHorizontalListView.this.mListPadding.left, this.mExtraScroll);
                        this.mLastSeenPos = lastPos;
                        if (lastPos > this.mBoundPos) {
                            AbsHorizontalListView.this.smoothScrollBy(-(lastViewPixelsShowing - extraScroll), this.mScrollDuration, true);
                            AbsHorizontalListView.this.postOnAnimation(this);
                            return;
                        }
                        int right = listWidth - extraScroll;
                        int lastViewRight = lastViewLeft + lastViewWidth;
                        if (right > lastViewRight) {
                            AbsHorizontalListView.this.smoothScrollBy(-(right - lastViewRight), this.mScrollDuration, true);
                            return;
                        }
                        return;
                    }
                    return;
                case 5:
                    if (this.mLastSeenPos == firstPos) {
                        AbsHorizontalListView.this.postOnAnimation(this);
                        return;
                    }
                    this.mLastSeenPos = firstPos;
                    childCount = AbsHorizontalListView.this.getChildCount();
                    int position = this.mTargetPos;
                    lastPos = (firstPos + childCount) - 1;
                    int viewTravelCount = 0;
                    if (position < firstPos) {
                        viewTravelCount = (firstPos - position) + 1;
                    } else if (position > lastPos) {
                        viewTravelCount = position - lastPos;
                    }
                    float modifier = Math.min(Math.abs(((float) viewTravelCount) / ((float) childCount)), 1.0f);
                    if (position < firstPos) {
                        AbsHorizontalListView.this.smoothScrollBy((int) (((float) (-AbsHorizontalListView.this.getWidth())) * modifier), (int) (((float) this.mScrollDuration) * modifier), true);
                        AbsHorizontalListView.this.postOnAnimation(this);
                        return;
                    } else if (position > lastPos) {
                        AbsHorizontalListView.this.smoothScrollBy((int) (((float) AbsHorizontalListView.this.getWidth()) * modifier), (int) (((float) this.mScrollDuration) * modifier), true);
                        AbsHorizontalListView.this.postOnAnimation(this);
                        return;
                    } else {
                        int distance = AbsHorizontalListView.this.getChildAt(position - firstPos).getLeft() - this.mOffsetFromLeft;
                        AbsHorizontalListView.this.smoothScrollBy(distance, (int) (((float) this.mScrollDuration) * (((float) Math.abs(distance)) / ((float) AbsHorizontalListView.this.getWidth()))), true);
                        return;
                    }
                default:
                    return;
            }
        }
    }

    class RecycleBin {
        private View[] mActiveViews = new View[0];
        private ArrayList<View> mCurrentScrap;
        private int mFirstActivePosition;
        private RecyclerListener mRecyclerListener;
        private ArrayList<View>[] mScrapViews;
        private ArrayList<View> mSkippedScrap;
        private SparseArray<View> mTransientStateViews;
        private LongSparseArray<View> mTransientStateViewsById;
        private int mViewTypeCount;

        RecycleBin() {
        }

        public void setViewTypeCount(int viewTypeCount) {
            if (viewTypeCount < 1) {
                throw new IllegalArgumentException("Can't have a viewTypeCount < 1");
            }
            ArrayList<View>[] scrapViews = new ArrayList[viewTypeCount];
            for (int i = 0; i < viewTypeCount; i++) {
                scrapViews[i] = new ArrayList();
            }
            this.mViewTypeCount = viewTypeCount;
            this.mCurrentScrap = scrapViews[0];
            this.mScrapViews = scrapViews;
        }

        public void markChildrenDirty() {
            int i;
            int count;
            ArrayList<View> scrap;
            int scrapCount;
            if (this.mViewTypeCount == 1) {
                scrap = this.mCurrentScrap;
                scrapCount = scrap.size();
                for (i = 0; i < scrapCount; i++) {
                    ((View) scrap.get(i)).forceLayout();
                }
            } else {
                int typeCount = this.mViewTypeCount;
                for (i = 0; i < typeCount; i++) {
                    scrap = this.mScrapViews[i];
                    scrapCount = scrap.size();
                    for (int j = 0; j < scrapCount; j++) {
                        ((View) scrap.get(j)).forceLayout();
                    }
                }
            }
            if (this.mTransientStateViews != null) {
                count = this.mTransientStateViews.size();
                for (i = 0; i < count; i++) {
                    ((View) this.mTransientStateViews.valueAt(i)).forceLayout();
                }
            }
            if (this.mTransientStateViewsById != null) {
                count = this.mTransientStateViewsById.size();
                for (i = 0; i < count; i++) {
                    ((View) this.mTransientStateViewsById.valueAt(i)).forceLayout();
                }
            }
        }

        public boolean shouldRecycleViewType(int viewType) {
            return viewType >= 0;
        }

        void clear() {
            ArrayList<View> scrap;
            int scrapCount;
            int i;
            if (this.mViewTypeCount == 1) {
                scrap = this.mCurrentScrap;
                scrapCount = scrap.size();
                for (i = 0; i < scrapCount; i++) {
                    AbsHorizontalListView.this.removeDetachedView((View) scrap.remove((scrapCount - 1) - i), false);
                }
            } else {
                int typeCount = this.mViewTypeCount;
                for (i = 0; i < typeCount; i++) {
                    scrap = this.mScrapViews[i];
                    scrapCount = scrap.size();
                    for (int j = 0; j < scrapCount; j++) {
                        AbsHorizontalListView.this.removeDetachedView((View) scrap.remove((scrapCount - 1) - j), false);
                    }
                }
            }
            if (this.mTransientStateViews != null) {
                this.mTransientStateViews.clear();
            }
            if (this.mTransientStateViewsById != null) {
                this.mTransientStateViewsById.clear();
            }
        }

        void fillActiveViews(int childCount, int firstActivePosition) {
            if (this.mActiveViews.length < childCount) {
                this.mActiveViews = new View[childCount];
            }
            this.mFirstActivePosition = firstActivePosition;
            View[] activeViews = this.mActiveViews;
            for (int i = 0; i < childCount; i++) {
                View child = AbsHorizontalListView.this.getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (!(lp == null || lp.viewType == -2)) {
                    activeViews[i] = child;
                }
            }
        }

        View getActiveView(int position) {
            int index = position - this.mFirstActivePosition;
            View[] activeViews = this.mActiveViews;
            if (index < 0 || index >= activeViews.length) {
                return null;
            }
            View match = activeViews[index];
            activeViews[index] = null;
            return match;
        }

        View getTransientStateView(int position) {
            if (AbsHorizontalListView.this.mAdapter == null || !AbsHorizontalListView.this.mAdapterHasStableIds || this.mTransientStateViewsById == null) {
                if (this.mTransientStateViews != null) {
                    int index = this.mTransientStateViews.indexOfKey(position);
                    if (index >= 0) {
                        View result = (View) this.mTransientStateViews.valueAt(index);
                        this.mTransientStateViews.removeAt(index);
                        return result;
                    }
                }
                return null;
            }
            long id = AbsHorizontalListView.this.mAdapter.getItemId(position);
            result = (View) this.mTransientStateViewsById.get(id);
            this.mTransientStateViewsById.remove(id);
            return result;
        }

        void clearTransientStateViews() {
            if (this.mTransientStateViews != null) {
                this.mTransientStateViews.clear();
            }
            if (this.mTransientStateViewsById != null) {
                this.mTransientStateViewsById.clear();
            }
        }

        View getScrapView(int position) {
            if (this.mViewTypeCount == 1) {
                return AbsHorizontalListView.retrieveFromScrap(this.mCurrentScrap, position);
            }
            int whichScrap = AbsHorizontalListView.this.mAdapter.getItemViewType(position);
            if (whichScrap < 0 || whichScrap >= this.mScrapViews.length) {
                return null;
            }
            return AbsHorizontalListView.retrieveFromScrap(this.mScrapViews[whichScrap], position);
        }

        void addScrapView(View scrap, int position) {
            LayoutParams lp = (LayoutParams) scrap.getLayoutParams();
            if (lp != null) {
                lp.scrappedFromPosition = position;
                int viewType = lp.viewType;
                if (shouldRecycleViewType(viewType)) {
                    scrap.dispatchStartTemporaryDetach();
                    AbsHorizontalListView.this.notifyViewAccessibilityStateChangedIfNeeded(1);
                    if (!scrap.hasTransientState()) {
                        if (this.mViewTypeCount == 1) {
                            this.mCurrentScrap.add(scrap);
                        } else {
                            this.mScrapViews[viewType].add(scrap);
                        }
                        if (scrap.isAccessibilityFocused()) {
                            scrap.clearAccessibilityFocus();
                        }
                        scrap.setAccessibilityDelegate(null);
                        if (this.mRecyclerListener != null) {
                            this.mRecyclerListener.onMovedToScrapHeap(scrap);
                        }
                    } else if (AbsHorizontalListView.this.mAdapter != null && AbsHorizontalListView.this.mAdapterHasStableIds) {
                        if (this.mTransientStateViewsById == null) {
                            this.mTransientStateViewsById = new LongSparseArray();
                        }
                        this.mTransientStateViewsById.put(lp.itemId, scrap);
                    } else if (AbsHorizontalListView.this.mDataChanged) {
                        if (this.mSkippedScrap == null) {
                            this.mSkippedScrap = new ArrayList();
                        }
                        this.mSkippedScrap.add(scrap);
                    } else {
                        if (this.mTransientStateViews == null) {
                            this.mTransientStateViews = new SparseArray();
                        }
                        this.mTransientStateViews.put(position, scrap);
                    }
                }
            }
        }

        void removeSkippedScrap() {
            if (this.mSkippedScrap != null) {
                int count = this.mSkippedScrap.size();
                for (int i = 0; i < count; i++) {
                    AbsHorizontalListView.this.removeDetachedView((View) this.mSkippedScrap.get(i), false);
                }
                this.mSkippedScrap.clear();
            }
        }

        void scrapActiveViews() {
            View[] activeViews = this.mActiveViews;
            boolean hasListener = this.mRecyclerListener != null;
            boolean multipleScraps = this.mViewTypeCount > 1;
            ArrayList<View> scrapViews = this.mCurrentScrap;
            for (int i = activeViews.length - 1; i >= 0; i--) {
                View victim = activeViews[i];
                if (victim != null) {
                    LayoutParams lp = (LayoutParams) victim.getLayoutParams();
                    int whichScrap = lp.viewType;
                    activeViews[i] = null;
                    boolean scrapHasTransientState = victim.hasTransientState();
                    if (!shouldRecycleViewType(whichScrap) || scrapHasTransientState) {
                        if (whichScrap != -2 && scrapHasTransientState) {
                            AbsHorizontalListView.this.removeDetachedView(victim, false);
                        }
                        if (scrapHasTransientState) {
                            if (AbsHorizontalListView.this.mAdapter == null || !AbsHorizontalListView.this.mAdapterHasStableIds) {
                                if (this.mTransientStateViews == null) {
                                    this.mTransientStateViews = new SparseArray();
                                }
                                this.mTransientStateViews.put(this.mFirstActivePosition + i, victim);
                            } else {
                                if (this.mTransientStateViewsById == null) {
                                    this.mTransientStateViewsById = new LongSparseArray();
                                }
                                this.mTransientStateViewsById.put(AbsHorizontalListView.this.mAdapter.getItemId(this.mFirstActivePosition + i), victim);
                            }
                        }
                    } else {
                        if (multipleScraps) {
                            scrapViews = this.mScrapViews[whichScrap];
                        }
                        victim.dispatchStartTemporaryDetach();
                        lp.scrappedFromPosition = this.mFirstActivePosition + i;
                        scrapViews.add(victim);
                        victim.setAccessibilityDelegate(null);
                        if (hasListener) {
                            this.mRecyclerListener.onMovedToScrapHeap(victim);
                        }
                    }
                }
            }
            pruneScrapViews();
        }

        private void pruneScrapViews() {
            int i;
            int maxViews = this.mActiveViews.length;
            int viewTypeCount = this.mViewTypeCount;
            ArrayList<View>[] scrapViews = this.mScrapViews;
            for (i = 0; i < viewTypeCount; i++) {
                ArrayList<View> scrapPile = scrapViews[i];
                int size = scrapPile.size();
                int extras = size - maxViews;
                int j = 0;
                int size2 = size - 1;
                while (j < extras) {
                    size = size2 - 1;
                    AbsHorizontalListView.this.removeDetachedView((View) scrapPile.remove(size2), false);
                    j++;
                    size2 = size;
                }
            }
            if (this.mTransientStateViews != null) {
                i = 0;
                while (i < this.mTransientStateViews.size()) {
                    if (!((View) this.mTransientStateViews.valueAt(i)).hasTransientState()) {
                        this.mTransientStateViews.removeAt(i);
                        i--;
                    }
                    i++;
                }
            }
            if (this.mTransientStateViewsById != null) {
                i = 0;
                while (i < this.mTransientStateViewsById.size()) {
                    if (!((View) this.mTransientStateViewsById.valueAt(i)).hasTransientState()) {
                        this.mTransientStateViewsById.removeAt(i);
                        i--;
                    }
                    i++;
                }
            }
        }

        void reclaimScrapViews(List<View> views) {
            if (this.mViewTypeCount == 1) {
                views.addAll(this.mCurrentScrap);
                return;
            }
            int viewTypeCount = this.mViewTypeCount;
            ArrayList<View>[] scrapViews = this.mScrapViews;
            for (int i = 0; i < viewTypeCount; i++) {
                views.addAll(scrapViews[i]);
            }
        }

        void setCacheColorHint(int color) {
            int i;
            ArrayList<View> scrap;
            int scrapCount;
            if (this.mViewTypeCount == 1) {
                scrap = this.mCurrentScrap;
                scrapCount = scrap.size();
                for (i = 0; i < scrapCount; i++) {
                    ((View) scrap.get(i)).setDrawingCacheBackgroundColor(color);
                }
            } else {
                int typeCount = this.mViewTypeCount;
                for (i = 0; i < typeCount; i++) {
                    scrap = this.mScrapViews[i];
                    scrapCount = scrap.size();
                    for (int j = 0; j < scrapCount; j++) {
                        ((View) scrap.get(j)).setDrawingCacheBackgroundColor(color);
                    }
                }
            }
            for (View victim : this.mActiveViews) {
                if (victim != null) {
                    victim.setDrawingCacheBackgroundColor(color);
                }
            }
        }
    }

    public interface RecyclerListener {
        void onMovedToScrapHeap(View view);
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        LongSparseArray<Integer> checkIdState;
        SparseBooleanArray checkState;
        int checkedItemCount;
        String filter;
        long firstId;
        boolean inActionMode;
        int position;
        long selectedId;
        int viewLeft;
        int width;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.selectedId = in.readLong();
            this.firstId = in.readLong();
            this.viewLeft = in.readInt();
            this.position = in.readInt();
            this.width = in.readInt();
            this.filter = in.readString();
            this.inActionMode = in.readByte() != (byte) 0;
            this.checkedItemCount = in.readInt();
            this.checkState = in.readSparseBooleanArray();
            int N = in.readInt();
            if (N > 0) {
                this.checkIdState = new LongSparseArray();
                for (int i = 0; i < N; i++) {
                    this.checkIdState.put(in.readLong(), Integer.valueOf(in.readInt()));
                }
            }
        }

        public void writeToParcel(Parcel out, int flags) {
            int i;
            int N;
            super.writeToParcel(out, flags);
            out.writeLong(this.selectedId);
            out.writeLong(this.firstId);
            out.writeInt(this.viewLeft);
            out.writeInt(this.position);
            out.writeInt(this.width);
            out.writeString(this.filter);
            if (this.inActionMode) {
                i = 1;
            } else {
                i = 0;
            }
            out.writeByte((byte) i);
            out.writeInt(this.checkedItemCount);
            out.writeSparseBooleanArray(this.checkState);
            if (this.checkIdState != null) {
                N = this.checkIdState.size();
            } else {
                N = 0;
            }
            out.writeInt(N);
            for (int i2 = 0; i2 < N; i2++) {
                out.writeLong(this.checkIdState.keyAt(i2));
                out.writeInt(((Integer) this.checkIdState.valueAt(i2)).intValue());
            }
        }

        public String toString() {
            return "AbsHorizontalListView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " selectedId=" + this.selectedId + " firstId=" + this.firstId + " viewLeft=" + this.viewLeft + " position=" + this.position + " width=" + this.width + " filter=" + this.filter + " checkState=" + this.checkState + "}";
        }
    }

    public interface SelectionBoundsAdjuster {
        void adjustListItemSelectionBounds(Rect rect);
    }

    private class TwSmoothScrollByMove implements Runnable {
        private TwSmoothScrollByMove() {
        }

        public void run() {
            if (AbsHorizontalListView.this.mFlingRunnable.mScroller.isFinished()) {
                synchronized (AbsHorizontalListView.this.mTwTwScrollRemains) {
                    if (AbsHorizontalListView.this.mTwTwScrollRemains.isEmpty()) {
                        return;
                    } else {
                        AbsHorizontalListView.this.smoothScrollBy(((Integer) AbsHorizontalListView.this.mTwTwScrollRemains.poll()).intValue(), 0, false);
                    }
                }
            }
            AbsHorizontalListView.this.post(this);
        }
    }

    abstract void fillGap(boolean z);

    abstract int findMotionRow(int i);

    abstract void setSelectionInt(int i);

    private void releaseAllBoosters() {
        if (this.mDVFSLockAcquired) {
            if (this.mDVFSHelper != null) {
                this.mDVFSHelper.release();
            }
            if (this.mDVFSHelperCore != null) {
                this.mDVFSHelperCore.release();
            }
            this.mDVFSLockAcquired = false;
        }
    }

    public void registerDoubleTapMotion() {
        registerMotionListener();
        setMotionEvent(true);
    }

    public void unregisterDoubleTapMotion() {
        unregisterMotionListener();
        setMotionEvent(false);
    }

    public void setMotionEvent(boolean enabled) {
        this.mMotionEnable = enabled;
    }

    public int getTouchSlop() {
        return this.mTouchSlop;
    }

    public void setTouchSlop(int value) {
        this.mTouchSlop = value;
    }

    public void updateCustomEdgeGlow(Drawable edgeeffectCustomEdge, Drawable edgeeffectCustomGlow) {
    }

    public AbsHorizontalListView(Context context) {
        super(context);
        this.mMotionEnable = false;
        this.mHasWindowFocusForMotion = false;
        this.mMotionRecognitionManager = null;
        this.USE_SET_INTEGRATOR_HAPTIC = FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_FRAMEWORK_ENABLE_INTEGRATOR_HAPTIC");
        this.mChoiceMode = 0;
        this.mLayoutMode = 0;
        this.mDeferNotifyDataSetChanged = false;
        this.mDrawSelectorOnTop = false;
        this.mSelectorPosition = -1;
        this.mSelectorRect = new Rect();
        this.mRecycler = new RecycleBin();
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mListPadding = new Rect();
        this.mHeightMeasureSpec = 0;
        this.mTouchMode = -1;
        this.mSelectedLeft = 0;
        this.mSmoothScrollbarEnabled = true;
        this.mResurrectToPosition = -1;
        this.mContextMenuInfo = null;
        this.mLastTouchMode = -1;
        this.mScrollProfilingStarted = false;
        this.mFlingProfilingStarted = false;
        this.mScrollStrictSpan = null;
        this.mFlingStrictSpan = null;
        this.mLastScrollState = 0;
        this.mVelocityScale = 1.0f;
        this.mIsScrap = new boolean[1];
        this.mActivePointerId = -1;
        this.mPointerCount = 0;
        this.mHapticOverScroll = false;
        this.mDirection = 0;
        this.mHoverLeftAreaWidth_DP = 15;
        this.mHoverRightAreaWidth_DP = 25;
        this.mHoverLeftAreaWidth = 0;
        this.mHoverRightAreaWidth = 0;
        this.mHoverRecognitionDurationTime = 0;
        this.mHoverRecognitionCurrentTime = 0;
        this.mHoverRecognitionStartTime = 0;
        this.mHoverScrollTimeInterval = 300;
        this.mHoverScrollStartTime = 0;
        this.mHoverScrollDirection = -1;
        this.mIsHoverOverscrolled = false;
        this.mHoverScrollEnable = true;
        this.mHoverAreaEnter = false;
        this.HOVERSCROLL_SPEED = 10;
        this.HOVERSCROLL_DELAY = 0;
        this.isHoveringUIEnabled = false;
        this.mTwCustomMultiChoiceMode = false;
        this.mIsCtrlkeyPressed = false;
        this.mIsShiftkeyPressed = false;
        this.mIsMultiFocusEnabled = false;
        this.mTwPressItemListIndex = 0;
        this.mFirstPressedPoint = -1;
        this.mSecondPressedPoint = -1;
        this.mOldAdapterItemCount = 0;
        this.mOldKeyCode = 0;
        this.mCurrentKeyCode = 0;
        this.mTwCurrentFocusPosition = -1;
        this.mDVFSHelper = null;
        this.mDVFSHelperCore = null;
        this.mDVFSLockAcquired = false;
        this.AIR_VIEW_WINSET = false;
        this.mForcedClick = false;
        this.mDragScrollWorkingZonePx = 0;
        this.mIsDragScrolled = false;
        this.mHoverPosition = -1;
        this.mHoveredOnEllipsizedText = false;
        this.mIsHoveredByMouse = false;
        this.mAlwaysDisableHoverHighlight = false;
        this.mIsPnePressed = false;
        this.mTwSmoothScrollByMove = null;
        this.mTwTwScrollRemains = null;
        this.mTwScrollAmount = 500;
        this.mMotionListener = new MRListener() {
            public void onMotionListener(MREvent motionEvent) {
                if (AbsHorizontalListView.this.mMotionEnable && AbsHorizontalListView.this.mHasWindowFocusForMotion) {
                    switch (motionEvent.getMotion()) {
                        case 1:
                            AbsHorizontalListView.log("[Motion-DoubleTap]");
                            if (AbsHorizontalListView.this.getLastVisiblePosition() > AbsHorizontalListView.this.getChildCount() * 2) {
                                AbsHorizontalListView.this.setSelection(AbsHorizontalListView.this.getChildCount() + 5);
                            }
                            AbsHorizontalListView.this.smoothScrollToPosition(0);
                            return;
                        default:
                            return;
                    }
                }
            }
        };
        this.mHoverScrollSpeed = 0;
        initAbsListView();
        this.mOwnerThread = Thread.currentThread();
        setHorizontalScrollBarEnabled(true);
        TypedArray a = context.obtainStyledAttributes(R.styleable.View);
        initializeScrollbars(a);
        a.recycle();
        if (this.mMotionRecognitionManager == null) {
            Log.d(TAG, "Get MotionRecognitionManager");
            this.mMotionRecognitionManager = (MotionRecognitionManager) context.getSystemService("motion_recognition");
        }
        this.isHoveringUIEnabled = getContext().getPackageManager().hasSystemFeature("com.sec.feature.hovering_ui");
    }

    public AbsHorizontalListView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.absListViewStyle);
    }

    public AbsHorizontalListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mMotionEnable = false;
        this.mHasWindowFocusForMotion = false;
        this.mMotionRecognitionManager = null;
        this.USE_SET_INTEGRATOR_HAPTIC = FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_FRAMEWORK_ENABLE_INTEGRATOR_HAPTIC");
        this.mChoiceMode = 0;
        this.mLayoutMode = 0;
        this.mDeferNotifyDataSetChanged = false;
        this.mDrawSelectorOnTop = false;
        this.mSelectorPosition = -1;
        this.mSelectorRect = new Rect();
        this.mRecycler = new RecycleBin();
        this.mSelectionLeftPadding = 0;
        this.mSelectionTopPadding = 0;
        this.mSelectionRightPadding = 0;
        this.mSelectionBottomPadding = 0;
        this.mListPadding = new Rect();
        this.mHeightMeasureSpec = 0;
        this.mTouchMode = -1;
        this.mSelectedLeft = 0;
        this.mSmoothScrollbarEnabled = true;
        this.mResurrectToPosition = -1;
        this.mContextMenuInfo = null;
        this.mLastTouchMode = -1;
        this.mScrollProfilingStarted = false;
        this.mFlingProfilingStarted = false;
        this.mScrollStrictSpan = null;
        this.mFlingStrictSpan = null;
        this.mLastScrollState = 0;
        this.mVelocityScale = 1.0f;
        this.mIsScrap = new boolean[1];
        this.mActivePointerId = -1;
        this.mPointerCount = 0;
        this.mHapticOverScroll = false;
        this.mDirection = 0;
        this.mHoverLeftAreaWidth_DP = 15;
        this.mHoverRightAreaWidth_DP = 25;
        this.mHoverLeftAreaWidth = 0;
        this.mHoverRightAreaWidth = 0;
        this.mHoverRecognitionDurationTime = 0;
        this.mHoverRecognitionCurrentTime = 0;
        this.mHoverRecognitionStartTime = 0;
        this.mHoverScrollTimeInterval = 300;
        this.mHoverScrollStartTime = 0;
        this.mHoverScrollDirection = -1;
        this.mIsHoverOverscrolled = false;
        this.mHoverScrollEnable = true;
        this.mHoverAreaEnter = false;
        this.HOVERSCROLL_SPEED = 10;
        this.HOVERSCROLL_DELAY = 0;
        this.isHoveringUIEnabled = false;
        this.mTwCustomMultiChoiceMode = false;
        this.mIsCtrlkeyPressed = false;
        this.mIsShiftkeyPressed = false;
        this.mIsMultiFocusEnabled = false;
        this.mTwPressItemListIndex = 0;
        this.mFirstPressedPoint = -1;
        this.mSecondPressedPoint = -1;
        this.mOldAdapterItemCount = 0;
        this.mOldKeyCode = 0;
        this.mCurrentKeyCode = 0;
        this.mTwCurrentFocusPosition = -1;
        this.mDVFSHelper = null;
        this.mDVFSHelperCore = null;
        this.mDVFSLockAcquired = false;
        this.AIR_VIEW_WINSET = false;
        this.mForcedClick = false;
        this.mDragScrollWorkingZonePx = 0;
        this.mIsDragScrolled = false;
        this.mHoverPosition = -1;
        this.mHoveredOnEllipsizedText = false;
        this.mIsHoveredByMouse = false;
        this.mAlwaysDisableHoverHighlight = false;
        this.mIsPnePressed = false;
        this.mTwSmoothScrollByMove = null;
        this.mTwTwScrollRemains = null;
        this.mTwScrollAmount = 500;
        this.mMotionListener = /* anonymous class already generated */;
        this.mHoverScrollSpeed = 0;
        initAbsListView();
        this.mOwnerThread = Thread.currentThread();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsListView, defStyle, 0);
        Drawable d = a.getDrawable(0);
        if (d != null) {
            setSelector(d);
        }
        this.mDrawSelectorOnTop = a.getBoolean(1, false);
        setStackFromBottom(a.getBoolean(2, false));
        setScrollingCacheEnabled(a.getBoolean(3, true));
        setTextFilterEnabled(a.getBoolean(4, false));
        setTranscriptMode(a.getInt(5, 0));
        setCacheColorHint(a.getColor(6, 0));
        setFastScrollEnabled(a.getBoolean(8, false));
        setSmoothScrollbarEnabled(a.getBoolean(9, true));
        setChoiceMode(a.getInt(7, 0));
        setFastScrollAlwaysVisible(a.getBoolean(10, false));
        a.recycle();
        if (this.mMotionRecognitionManager == null) {
            Log.d(TAG, "Get MotionRecognitionManager");
            this.mMotionRecognitionManager = (MotionRecognitionManager) context.getSystemService("motion_recognition");
        }
        this.isHoveringUIEnabled = getContext().getPackageManager().hasSystemFeature("com.sec.feature.hovering_ui");
    }

    private void initAbsListView() {
        boolean z = false;
        setClickable(true);
        setFocusableInTouchMode(true);
        setWillNotDraw(false);
        setAlwaysDrawnWithCacheEnabled(false);
        setScrollingCacheEnabled(true);
        twEnableHorizontalScrollbar();
        if (this.mContext != null) {
            ViewConfiguration configuration = ViewConfiguration.get(this.mContext);
            this.mTouchSlop = configuration.getScaledTouchSlop();
            this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
            this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
            this.mOverscrollDistance = configuration.getScaledOverscrollDistance();
            this.mOverflingDistance = configuration.getScaledOverflingDistance();
        }
        if (getContext() != null) {
            this.mDensityScale = getContext().getResources().getDisplayMetrics().density;
        }
        this.mHapticPreDrawListener = new HapticPreDrawListener();
        if (this.mContext != null) {
            if (Settings$System.getInt(this.mContext.getContentResolver(), Settings$System.FINGER_AIR_VIEW, 0) == 1) {
                z = true;
            }
            this.AIR_VIEW_WINSET = z;
        }
        if (this.mContext != null) {
            this.mDVFSHelper = new DVFSHelper(this.mContext, "LIST_SCROLL_BOOSTER", 12, 0);
            this.mDVFSHelper.addExtraOptionsByDefaultPolicy("ListView_scroll");
            if (DVFSHelper.LIST_SCROLL_BOOSTER_CORE_NUM != 0) {
                this.mDVFSHelperCore = new DVFSHelper(this.mContext, "LIST_SCROLL_BOOSTER_CORE", 14, 0);
                this.mDVFSHelperCore.addExtraOption("CORE_NUM", (long) DVFSHelper.LIST_SCROLL_BOOSTER_CORE_NUM);
            }
        }
        if (getContext() != null) {
            this.mAudioManager = (AudioManager) getContext().getSystemService("audio");
        }
        TypedValue value = new TypedValue();
        if (this.mContext != null && this.mContext.getTheme().resolveAttribute(R.attr.twListMultiSelectBackground, value, true)) {
            this.mMultiFocusImage = this.mContext.getResources().getDrawable(value.resourceId);
        }
    }

    public void setOverScrollMode(int mode) {
        if (mode == 2) {
            this.mEdgeGlowLeft = null;
            this.mEdgeGlowRight = null;
        } else if (this.mEdgeGlowLeft == null) {
            Context context = getContext();
            this.mEdgeGlowLeft = new EdgeEffect(context);
            this.mEdgeGlowRight = new EdgeEffect(context);
        }
        super.setOverScrollMode(mode);
    }

    public void setAdapter(ListAdapter adapter) {
        if (adapter != null) {
            this.mAdapterHasStableIds = this.mAdapter.hasStableIds();
            if (this.mChoiceMode != 0 && this.mAdapterHasStableIds && this.mCheckedIdStates == null) {
                this.mCheckedIdStates = new LongSparseArray();
            }
        }
        if (this.mCheckStates != null) {
            this.mCheckStates.clear();
        }
        if (this.mCheckedIdStates != null) {
            this.mCheckedIdStates.clear();
        }
        if (this.mIsMultiFocusEnabled && this.mAdapter != null) {
            this.mTwPressItemListArray = new int[this.mAdapter.getCount()];
            resetPressItemListArray();
            this.mOldAdapterItemCount = this.mAdapter.getCount();
        }
    }

    public int getCheckedItemCount() {
        return this.mCheckedItemCount;
    }

    public boolean isItemChecked(int position) {
        if (this.mChoiceMode == 0 || this.mCheckStates == null) {
            return false;
        }
        return this.mCheckStates.get(position);
    }

    public int getCheckedItemPosition() {
        if (this.mChoiceMode == 1 && this.mCheckStates != null && this.mCheckStates.size() == 1) {
            return this.mCheckStates.keyAt(0);
        }
        return -1;
    }

    public SparseBooleanArray getCheckedItemPositions() {
        if (this.mChoiceMode != 0) {
            return this.mCheckStates;
        }
        return null;
    }

    public long[] getCheckedItemIds() {
        if (this.mChoiceMode == 0 || this.mCheckedIdStates == null || this.mAdapter == null) {
            return new long[0];
        }
        LongSparseArray<Integer> idStates = this.mCheckedIdStates;
        int count = idStates.size();
        long[] ids = new long[count];
        for (int i = 0; i < count; i++) {
            ids[i] = idStates.keyAt(i);
        }
        return ids;
    }

    public void clearChoices() {
        if (this.mCheckStates != null) {
            this.mCheckStates.clear();
        }
        if (this.mCheckedIdStates != null) {
            this.mCheckedIdStates.clear();
        }
        this.mCheckedItemCount = 0;
    }

    public void setItemChecked(int position, boolean value) {
        if (this.mChoiceMode != 0) {
            if (value && this.mChoiceMode == 3 && this.mChoiceActionMode == null) {
                if (this.mMultiChoiceModeCallback == null || !this.mMultiChoiceModeCallback.hasWrappedCallback()) {
                    throw new IllegalStateException("AbsHorizontalListView: attempted to start selection mode for CHOICE_MODE_MULTIPLE_MODAL but no choice mode callback was supplied. Call setMultiChoiceModeListener to set a callback.");
                }
                this.mChoiceActionMode = startActionMode(this.mMultiChoiceModeCallback);
            }
            if (this.mChoiceMode == 2 || this.mChoiceMode == 3) {
                boolean oldValue = this.mCheckStates.get(position);
                this.mCheckStates.put(position, value);
                if (this.mCheckedIdStates != null && this.mAdapter.hasStableIds()) {
                    if (value) {
                        this.mCheckedIdStates.put(this.mAdapter.getItemId(position), Integer.valueOf(position));
                    } else {
                        this.mCheckedIdStates.delete(this.mAdapter.getItemId(position));
                    }
                }
                if (oldValue != value) {
                    if (value) {
                        this.mCheckedItemCount++;
                    } else {
                        this.mCheckedItemCount--;
                    }
                }
                if (this.mChoiceActionMode != null) {
                    this.mMultiChoiceModeCallback.onItemCheckedStateChanged(this.mChoiceActionMode, position, this.mAdapter.getItemId(position), value);
                }
            } else {
                boolean updateIds;
                if (this.mCheckedIdStates == null || !this.mAdapter.hasStableIds()) {
                    updateIds = false;
                } else {
                    updateIds = true;
                }
                if (value || isItemChecked(position)) {
                    this.mCheckStates.clear();
                    if (updateIds) {
                        this.mCheckedIdStates.clear();
                    }
                }
                if (value) {
                    this.mCheckStates.put(position, true);
                    if (updateIds) {
                        this.mCheckedIdStates.put(this.mAdapter.getItemId(position), Integer.valueOf(position));
                    }
                    this.mCheckedItemCount = 1;
                } else if (this.mCheckStates.size() == 0 || !this.mCheckStates.valueAt(0)) {
                    this.mCheckedItemCount = 0;
                }
            }
            if (!this.mInLayout && !this.mBlockLayoutRequests) {
                if (!this.mForcedClick) {
                    this.mDataChanged = true;
                }
                rememberSyncState();
                requestLayout();
            }
        }
    }

    public boolean performItemClick(View view, int position, long id) {
        boolean handled = false;
        boolean dispatchItemClick = true;
        if (this.mChoiceMode != 0) {
            handled = true;
            boolean checkedStateChanged = false;
            boolean checked;
            if (this.mCheckStates != null && (this.mChoiceMode == 2 || (this.mChoiceMode == 3 && this.mChoiceActionMode != null))) {
                if (this.mCheckStates.get(position, false)) {
                    checked = false;
                } else {
                    checked = true;
                }
                this.mCheckStates.put(position, checked);
                if (this.mCheckedIdStates != null && this.mAdapter.hasStableIds()) {
                    if (checked) {
                        this.mCheckedIdStates.put(this.mAdapter.getItemId(position), Integer.valueOf(position));
                    } else {
                        this.mCheckedIdStates.delete(this.mAdapter.getItemId(position));
                    }
                }
                if (checked) {
                    this.mCheckedItemCount++;
                } else {
                    this.mCheckedItemCount--;
                }
                if (this.mChoiceActionMode != null) {
                    this.mMultiChoiceModeCallback.onItemCheckedStateChanged(this.mChoiceActionMode, position, id, checked);
                    dispatchItemClick = false;
                }
                checkedStateChanged = true;
            } else if (this.mCheckStates != null && this.mChoiceMode == 1) {
                if (this.mCheckStates.get(position, false)) {
                    checked = false;
                } else {
                    checked = true;
                }
                if (checked) {
                    this.mCheckStates.clear();
                    this.mCheckStates.put(position, true);
                    if (this.mCheckedIdStates != null && this.mAdapter.hasStableIds()) {
                        this.mCheckedIdStates.clear();
                        this.mCheckedIdStates.put(this.mAdapter.getItemId(position), Integer.valueOf(position));
                    }
                    this.mCheckedItemCount = 1;
                } else if (this.mCheckStates.size() == 0 || !this.mCheckStates.valueAt(0)) {
                    this.mCheckedItemCount = 0;
                }
                checkedStateChanged = true;
            }
            if (checkedStateChanged) {
                updateOnScreenCheckedViews();
            }
        }
        if (dispatchItemClick) {
            return handled | super.performItemClick(view, position, id);
        }
        return handled;
    }

    public boolean twNotifyKeyPressState(View view, int position, long id) {
        return false;
    }

    private void updateOnScreenCheckedViews() {
        int firstPos = this.mFirstPosition;
        int count = getChildCount();
        boolean useActivated = getContext().getApplicationInfo().targetSdkVersion >= 11;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int position = firstPos + i;
            if (child instanceof Checkable) {
                ((Checkable) child).setChecked(this.mCheckStates.get(position));
            } else if (useActivated) {
                child.setActivated(this.mCheckStates.get(position));
            }
        }
    }

    public int getChoiceMode() {
        return this.mChoiceMode;
    }

    public void setChoiceMode(int choiceMode) {
        this.mChoiceMode = choiceMode;
        if (this.mChoiceActionMode != null) {
            this.mChoiceActionMode.finish();
            this.mChoiceActionMode = null;
        }
        if (this.mChoiceMode != 0) {
            if (this.mCheckStates == null) {
                this.mCheckStates = new SparseBooleanArray(0);
            }
            if (this.mCheckedIdStates == null && this.mAdapter != null && this.mAdapter.hasStableIds()) {
                this.mCheckedIdStates = new LongSparseArray(0);
            }
            if (this.mChoiceMode == 3) {
                clearChoices();
                setLongClickable(true);
            }
        }
    }

    public void setMultiChoiceModeListener(MultiChoiceModeListener listener) {
        if (this.mMultiChoiceModeCallback == null) {
            this.mMultiChoiceModeCallback = new MultiChoiceModeWrapper();
        }
        this.mMultiChoiceModeCallback.setWrapped(listener);
    }

    public void twShowMultiChoiceMode(boolean enabled) {
        if (enabled) {
            if (this.mChoiceMode == 3 && this.mMultiChoiceModeCallback != null) {
                this.mChoiceActionMode = startActionMode(this.mMultiChoiceModeCallback);
            }
        } else if (this.mChoiceActionMode != null) {
            this.mChoiceActionMode.finish();
            this.mChoiceActionMode = null;
        }
    }

    public void twSetCustomMultiChoiceMode(boolean enable) {
        this.mTwCustomMultiChoiceMode = enable;
    }

    public boolean twIsSetCustomMultiChoiceMode() {
        return this.mTwCustomMultiChoiceMode;
    }

    private boolean contentFits() {
        int childCount = getChildCount();
        if (childCount == 0) {
            return true;
        }
        if (childCount != this.mItemCount) {
            return false;
        }
        if (getChildAt(0).getLeft() < this.mListPadding.left || getChildAt(childCount - 1).getRight() > getWidth() - this.mListPadding.right) {
            return false;
        }
        return true;
    }

    public void setFastScrollEnabled(final boolean enabled) {
        if (this.mFastScrollEnabled != enabled) {
            this.mFastScrollEnabled = enabled;
            if (isOwnerThread()) {
                setFastScrollerEnabledUiThread(enabled);
            } else {
                post(new Runnable() {
                    public void run() {
                        AbsHorizontalListView.this.setFastScrollerEnabledUiThread(enabled);
                    }
                });
            }
        }
    }

    private void setFastScrollerEnabledUiThread(boolean enabled) {
        if (this.mFastScroller != null) {
            this.mFastScroller.setEnabled(enabled);
        } else if (enabled) {
            this.mFastScroller = new HorizontalFastScroller(this);
            this.mFastScroller.setEnabled(true);
        }
        resolvePadding();
        if (this.mFastScroller != null) {
            this.mFastScroller.updateLayout();
        }
    }

    public void setFastScrollAlwaysVisible(final boolean alwaysShow) {
        if (this.mFastScrollAlwaysVisible != alwaysShow) {
            if (alwaysShow && !this.mFastScrollEnabled) {
                setFastScrollEnabled(true);
            }
            this.mFastScrollAlwaysVisible = alwaysShow;
            if (isOwnerThread()) {
                setFastScrollerAlwaysVisibleUiThread(alwaysShow);
            } else {
                post(new Runnable() {
                    public void run() {
                        AbsHorizontalListView.this.setFastScrollerAlwaysVisibleUiThread(alwaysShow);
                    }
                });
            }
        }
    }

    private void setFastScrollerAlwaysVisibleUiThread(boolean alwaysShow) {
        if (this.mFastScroller != null) {
            this.mFastScroller.setAlwaysShow(alwaysShow);
        }
    }

    private boolean isOwnerThread() {
        return this.mOwnerThread == Thread.currentThread();
    }

    public boolean isFastScrollAlwaysVisible() {
        if (this.mFastScroller == null) {
            if (this.mFastScrollEnabled && this.mFastScrollAlwaysVisible) {
                return true;
            }
            return false;
        } else if (this.mFastScroller.isEnabled() && this.mFastScroller.isAlwaysShowEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    public int getHorizontalScrollbarHeight() {
        if (this.mFastScroller == null || !this.mFastScroller.isEnabled()) {
            return super.getHorizontalScrollbarHeight();
        }
        return Math.max(super.getHorizontalScrollbarHeight(), this.mFastScroller.getHeight());
    }

    @ExportedProperty
    public boolean isFastScrollEnabled() {
        if (this.mFastScroller == null) {
            return this.mFastScrollEnabled;
        }
        return this.mFastScroller.isEnabled();
    }

    public void setTwHorizontalScrollbarPosition(int position) {
        super.setTwHorizontalScrollbarPosition(position);
        if (this.mFastScroller != null) {
            this.mFastScroller.setScrollbarPosition(position);
        }
    }

    public void setScrollBarStyle(int style) {
        super.setScrollBarStyle(style);
        if (this.mFastScroller != null) {
            this.mFastScroller.setScrollBarStyle(style);
        }
    }

    protected boolean isTwHorizontalScrollBarHidden() {
        return isFastScrollEnabled();
    }

    public void setSmoothScrollbarEnabled(boolean enabled) {
        this.mSmoothScrollbarEnabled = enabled;
    }

    public boolean isMultiWindows() {
        return SmartFaceManager.PAGE_BOTTOM.equals(SystemProperties.get("sys.multiwindow.running"));
    }

    @ExportedProperty
    public boolean isSmoothScrollbarEnabled() {
        return this.mSmoothScrollbarEnabled;
    }

    public void setOnScrollListener(OnScrollListener l) {
        this.mOnScrollListener = l;
        invokeOnItemScrollListener();
    }

    void invokeOnItemScrollListener() {
        if (this.mFastScroller != null) {
            this.mFastScroller.onScroll(this.mFirstPosition, getChildCount(), this.mItemCount);
        }
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScroll(this, this.mFirstPosition, getChildCount(), this.mItemCount);
        }
        onScrollChanged(0, 0, 0, 0);
    }

    public void sendAccessibilityEvent(int eventType) {
        if (eventType == 4096) {
            int firstVisiblePosition = getFirstVisiblePosition();
            int lastVisiblePosition = getLastVisiblePosition();
            if (this.mLastAccessibilityScrollEventFromIndex != firstVisiblePosition || this.mLastAccessibilityScrollEventToIndex != lastVisiblePosition) {
                this.mLastAccessibilityScrollEventFromIndex = firstVisiblePosition;
                this.mLastAccessibilityScrollEventToIndex = lastVisiblePosition;
            } else {
                return;
            }
        }
        super.sendAccessibilityEvent(eventType);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(AbsHorizontalListView.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(AbsHorizontalListView.class.getName());
        if (isEnabled()) {
            if (getFirstVisiblePosition() > 0) {
                info.addAction(8192);
                info.setScrollable(true);
            }
            if (getLastVisiblePosition() < getCount() - 1) {
                info.addAction(4096);
                info.setScrollable(true);
            }
        }
    }

    public boolean performAccessibilityAction(int action, Bundle arguments) {
        if (super.performAccessibilityAction(action, arguments)) {
            return true;
        }
        switch (action) {
            case 4096:
                if (!isEnabled() || getLastVisiblePosition() >= getCount() - 1) {
                    return false;
                }
                smoothScrollBy((getWidth() - this.mListPadding.left) - this.mListPadding.right, 200);
                return true;
            case 8192:
                if (!isEnabled() || this.mFirstPosition <= 0) {
                    return false;
                }
                smoothScrollBy(-((getWidth() - this.mListPadding.left) - this.mListPadding.right), 200);
                return true;
            default:
                return false;
        }
    }

    public View findViewByAccessibilityIdTraversal(int accessibilityId) {
        if (accessibilityId == getAccessibilityViewId()) {
            return this;
        }
        if (this.mDataChanged) {
            return null;
        }
        return super.findViewByAccessibilityIdTraversal(accessibilityId);
    }

    @ExportedProperty
    public boolean isScrollingCacheEnabled() {
        return this.mScrollingCacheEnabled;
    }

    public void setScrollingCacheEnabled(boolean enabled) {
        if (this.mScrollingCacheEnabled && !enabled) {
            clearScrollingCache();
        }
        this.mScrollingCacheEnabled = enabled;
    }

    public void setTextFilterEnabled(boolean textFilterEnabled) {
        this.mTextFilterEnabled = textFilterEnabled;
    }

    @ExportedProperty
    public boolean isTextFilterEnabled() {
        return this.mTextFilterEnabled;
    }

    public void getFocusedRect(Rect r) {
        View view = getSelectedView();
        if (view == null || view.getParent() != this) {
            super.getFocusedRect(r);
            return;
        }
        view.getFocusedRect(r);
        offsetDescendantRectToMyCoords(view, r);
    }

    private void useDefaultSelector() {
        setSelector(getResources().getDrawable(R.drawable.list_selector_background));
    }

    @ExportedProperty
    public boolean isStackFromBottom() {
        return this.mStackFromBottom;
    }

    public void setStackFromBottom(boolean stackFromBottom) {
        if (this.mStackFromBottom != stackFromBottom) {
            this.mStackFromBottom = stackFromBottom;
            requestLayoutIfNecessary();
        }
    }

    void requestLayoutIfNecessary() {
        if (getChildCount() > 0) {
            resetList();
            requestLayout();
            invalidate();
        }
    }

    public Parcelable onSaveInstanceState() {
        dismissPopup();
        SavedState ss = new SavedState(super.onSaveInstanceState());
        if (this.mPendingSync != null) {
            ss.selectedId = this.mPendingSync.selectedId;
            ss.firstId = this.mPendingSync.firstId;
            ss.viewLeft = this.mPendingSync.viewLeft;
            ss.position = this.mPendingSync.position;
            ss.width = this.mPendingSync.width;
            ss.filter = this.mPendingSync.filter;
            ss.inActionMode = this.mPendingSync.inActionMode;
            ss.checkedItemCount = this.mPendingSync.checkedItemCount;
            ss.checkState = this.mPendingSync.checkState;
            ss.checkIdState = this.mPendingSync.checkIdState;
        } else {
            boolean haveChildren = getChildCount() > 0 && this.mItemCount > 0;
            long selectedId = getSelectedItemId();
            ss.selectedId = selectedId;
            ss.width = getWidth();
            if (selectedId >= 0) {
                ss.viewLeft = this.mSelectedLeft;
                ss.position = getSelectedItemPosition();
                ss.firstId = -1;
            } else if (!haveChildren || this.mFirstPosition <= 0) {
                ss.viewLeft = 0;
                ss.firstId = -1;
                ss.position = 0;
            } else {
                ss.viewLeft = getChildAt(0).getLeft();
                int firstPos = this.mFirstPosition;
                if (firstPos >= this.mItemCount) {
                    firstPos = this.mItemCount - 1;
                }
                ss.position = firstPos;
                ss.firstId = this.mAdapter.getItemId(firstPos);
            }
            ss.filter = null;
            if (this.mFiltered) {
                EditText textFilter = this.mTextFilter;
                if (textFilter != null) {
                    Editable filterText = textFilter.getText();
                    if (filterText != null) {
                        ss.filter = filterText.toString();
                    }
                }
            }
            boolean z = this.mChoiceMode == 3 && this.mChoiceActionMode != null;
            ss.inActionMode = z;
            if (this.mCheckStates != null) {
                ss.checkState = this.mCheckStates.clone();
            }
            if (this.mCheckedIdStates != null) {
                LongSparseArray<Integer> idState = new LongSparseArray();
                int count = this.mCheckedIdStates.size();
                for (int i = 0; i < count; i++) {
                    idState.put(this.mCheckedIdStates.keyAt(i), this.mCheckedIdStates.valueAt(i));
                }
                ss.checkIdState = idState;
            }
            ss.checkedItemCount = this.mCheckedItemCount;
            if (this.mRemoteAdapter != null) {
                this.mRemoteAdapter.saveRemoteViewsCache();
            }
        }
        return ss;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mDataChanged = true;
        this.mSyncHeight = (long) ss.width;
        if (ss.selectedId >= 0) {
            this.mNeedSync = true;
            this.mPendingSync = ss;
            this.mSyncRowId = ss.selectedId;
            this.mSyncPosition = ss.position;
            this.mSpecificTop = ss.viewLeft;
            this.mSyncMode = 0;
        } else if (ss.firstId >= 0) {
            setSelectedPositionInt(-1);
            setNextSelectedPositionInt(-1);
            this.mSelectorPosition = -1;
            this.mNeedSync = true;
            this.mPendingSync = ss;
            this.mSyncRowId = ss.firstId;
            this.mSyncPosition = ss.position;
            this.mSpecificTop = ss.viewLeft;
            this.mSyncMode = 1;
        }
        setFilterText(ss.filter);
        if (ss.checkState != null) {
            this.mCheckStates = ss.checkState;
        }
        if (ss.checkIdState != null) {
            this.mCheckedIdStates = ss.checkIdState;
        }
        this.mCheckedItemCount = ss.checkedItemCount;
        if (ss.inActionMode && this.mChoiceMode == 3 && this.mMultiChoiceModeCallback != null) {
            this.mChoiceActionMode = startActionMode(this.mMultiChoiceModeCallback);
        }
        requestLayout();
    }

    private boolean acceptFilter() {
        return this.mTextFilterEnabled && (getAdapter() instanceof Filterable) && ((Filterable) getAdapter()).getFilter() != null;
    }

    public void setFilterText(String filterText) {
        if (this.mTextFilterEnabled && !TextUtils.isEmpty(filterText)) {
            createTextFilter(false);
            this.mTextFilter.setText((CharSequence) filterText);
            this.mTextFilter.setSelection(filterText.length());
            if (this.mAdapter instanceof Filterable) {
                if (this.mPopup == null) {
                    ((Filterable) this.mAdapter).getFilter().filter(filterText);
                }
                this.mFiltered = true;
                this.mDataSetObserver.clearSavedState();
            }
        }
    }

    public CharSequence getTextFilter() {
        if (!this.mTextFilterEnabled || this.mTextFilter == null) {
            return null;
        }
        return this.mTextFilter.getText();
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus && this.mSelectedPosition < 0 && !isInTouchMode()) {
            if (!(isAttachedToWindow() || this.mAdapter == null)) {
                this.mDataChanged = true;
                this.mOldItemCount = this.mItemCount;
                this.mItemCount = this.mAdapter.getCount();
            }
            resurrectSelection();
        }
        if (isAttachedToWindow() && gainFocus && this.mSelectedPosition < 0) {
            if (!gainFocus) {
                releaseAllBoosters();
            }
        } else if (!gainFocus) {
            releaseAllBoosters();
        }
    }

    public void requestLayout() {
        if (!this.mBlockLayoutRequests && !this.mInLayout) {
            super.requestLayout();
        }
    }

    void resetList() {
        removeAllViewsInLayout();
        this.mFirstPosition = 0;
        if (this.USE_SET_INTEGRATOR_HAPTIC) {
            this.mLastPosition = -1;
        }
        this.mDataChanged = false;
        this.mPositionScrollAfterLayout = null;
        this.mNeedSync = false;
        this.mPendingSync = null;
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        setSelectedPositionInt(-1);
        setNextSelectedPositionInt(-1);
        this.mSelectedLeft = 0;
        this.mSelectorPosition = -1;
        this.mSelectorRect.setEmpty();
        invalidate();
    }

    protected int computeHorizontalScrollExtent() {
        int count = getChildCount();
        if (count <= 0) {
            return 0;
        }
        if (!this.mSmoothScrollbarEnabled) {
            return 1;
        }
        int extent = count * 100;
        View view = getChildAt(0);
        int left = view.getLeft();
        int width = view.getWidth();
        if (width > 0) {
            extent += (left * 100) / width;
        }
        view = getChildAt(count - 1);
        int right = view.getRight();
        width = view.getWidth();
        if (width > 0) {
            return extent - (((right - getWidth()) * 100) / width);
        }
        return extent;
    }

    protected int computeHorizontalScrollOffset() {
        int firstPosition = this.mFirstPosition;
        int childCount = getChildCount();
        if (firstPosition < 0 || childCount <= 0) {
            return 0;
        }
        if (this.mSmoothScrollbarEnabled) {
            View view = getChildAt(0);
            int left = view.getLeft();
            int width = view.getWidth();
            if (width > 0) {
                return Math.max(((firstPosition * 100) - ((left * 100) / width)) + ((int) (((((float) this.mScrollX) / ((float) getWidth())) * ((float) this.mItemCount)) * 100.0f)), 0);
            }
            return 0;
        }
        int index;
        int count = this.mItemCount;
        if (firstPosition == 0) {
            index = 0;
        } else if (firstPosition + childCount == count) {
            index = count;
        } else {
            index = firstPosition + (childCount / 2);
        }
        return (int) (((float) firstPosition) + (((float) childCount) * (((float) index) / ((float) count))));
    }

    protected int computeHorizontalScrollRange() {
        if (!this.mSmoothScrollbarEnabled) {
            return this.mItemCount;
        }
        int result = Math.max(this.mItemCount * 100, 0);
        if (this.mScrollX != 0) {
            return result + Math.abs((int) (((((float) this.mScrollX) / ((float) getWidth())) * ((float) this.mItemCount)) * 100.0f));
        }
        return result;
    }

    protected float getLeftFadingEdgeStrength() {
        int count = getChildCount();
        float fadeEdge = super.getLeftFadingEdgeStrength();
        if (count == 0) {
            return fadeEdge;
        }
        if (this.mFirstPosition > 0) {
            return 1.0f;
        }
        int left = getChildAt(0).getLeft();
        return left < this.mPaddingLeft ? ((float) (-(left - this.mPaddingLeft))) / ((float) getHorizontalFadingEdgeLength()) : fadeEdge;
    }

    protected float getRightFadingEdgeStrength() {
        int count = getChildCount();
        float fadeEdge = super.getRightFadingEdgeStrength();
        if (count == 0) {
            return fadeEdge;
        }
        if ((this.mFirstPosition + count) - 1 < this.mItemCount - 1) {
            return 1.0f;
        }
        int right = getChildAt(count - 1).getRight();
        int width = getWidth();
        return right > width - this.mPaddingRight ? ((float) ((right - width) + this.mPaddingRight)) / ((float) getHorizontalFadingEdgeLength()) : fadeEdge;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean z = true;
        if (this.mSelector == null) {
            useDefaultSelector();
        }
        Rect listPadding = this.mListPadding;
        listPadding.left = this.mSelectionLeftPadding + this.mPaddingLeft;
        listPadding.top = this.mSelectionTopPadding + this.mPaddingTop;
        listPadding.right = this.mSelectionRightPadding + this.mPaddingRight;
        listPadding.bottom = this.mSelectionBottomPadding + this.mPaddingBottom;
        if (this.mTranscriptMode == 1) {
            int childCount = getChildCount();
            int listRight = getWidth() - getPaddingRight();
            View lastChild = getChildAt(childCount - 1);
            int lastRight;
            if (lastChild != null) {
                lastRight = lastChild.getRight();
            } else {
                lastRight = listRight;
            }
            if (this.mFirstPosition + childCount < this.mLastHandledItemCount || lastRight > listRight) {
                z = false;
            }
            this.mForceTranscriptScroll = z;
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mInLayout = true;
        if (changed) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).forceLayout();
            }
            this.mRecycler.markChildrenDirty();
        }
        if (this.mFastScroller != null && (this.mItemCount != this.mOldItemCount || this.mDataChanged)) {
            this.mFastScroller.onItemCountChanged(this.mItemCount);
        }
        layoutChildren();
        this.mInLayout = false;
        this.mOverscrollMax = (r - l) / 3;
    }

    protected boolean setFrame(int left, int top, int right, int bottom) {
        boolean changed = super.setFrame(left, top, right, bottom);
        if (changed) {
            boolean visible = getWindowVisibility() == 0;
            if (this.mFiltered && visible && this.mPopup != null && this.mPopup.isShowing()) {
                positionPopup();
            }
        }
        return changed;
    }

    protected void layoutChildren() {
    }

    void updateScrollIndicators() {
        int i = 0;
        if (this.mScrollLeft != null) {
            int i2;
            boolean canScrollLeft = this.mFirstPosition > 0;
            if (!canScrollLeft && getChildCount() > 0) {
                canScrollLeft = getChildAt(0).getLeft() < this.mListPadding.left;
            }
            View view = this.mScrollLeft;
            if (canScrollLeft) {
                i2 = 0;
            } else {
                i2 = 4;
            }
            view.setVisibility(i2);
        }
        if (this.mScrollRight != null) {
            boolean canScrollRight;
            int count = getChildCount();
            if (this.mFirstPosition + count < this.mItemCount) {
                canScrollRight = true;
            } else {
                canScrollRight = false;
            }
            if (!canScrollRight && count > 0) {
                if (getChildAt(count - 1).getRight() > this.mRight - this.mListPadding.right) {
                    canScrollRight = true;
                } else {
                    canScrollRight = false;
                }
            }
            View view2 = this.mScrollRight;
            if (!canScrollRight) {
                i = 4;
            }
            view2.setVisibility(i);
        }
    }

    @ExportedProperty
    public View getSelectedView() {
        if (this.mItemCount <= 0 || this.mSelectedPosition < 0) {
            return null;
        }
        return getChildAt(this.mSelectedPosition - this.mFirstPosition);
    }

    public int getListPaddingTop() {
        return this.mListPadding.top;
    }

    public int getListPaddingBottom() {
        return this.mListPadding.bottom;
    }

    public int getListPaddingLeft() {
        return this.mListPadding.left;
    }

    public int getListPaddingRight() {
        return this.mListPadding.right;
    }

    View obtainView(int position, boolean[] isScrap) {
        View child;
        Trace.traceBegin(8, "obtainView");
        isScrap[0] = false;
        View scrapView = this.mRecycler.getTransientStateView(position);
        if (scrapView == null) {
            scrapView = this.mRecycler.getScrapView(position);
        }
        if (scrapView != null) {
            child = this.mAdapter.getView(position, scrapView, this);
            if (child.getImportantForAccessibility() == 0) {
                child.setImportantForAccessibility(1);
            }
            if (child != scrapView) {
                this.mRecycler.addScrapView(scrapView, position);
                if (this.mCacheColorHint != 0) {
                    child.setDrawingCacheBackgroundColor(this.mCacheColorHint);
                }
            } else {
                isScrap[0] = true;
                if (child.isAccessibilityFocused()) {
                    child.clearAccessibilityFocus();
                }
                child.dispatchFinishTemporaryDetach();
            }
        } else {
            child = this.mAdapter.getView(position, null, this);
            if (child.getImportantForAccessibility() == 0) {
                child.setImportantForAccessibility(1);
            }
            if (this.mCacheColorHint != 0) {
                child.setDrawingCacheBackgroundColor(this.mCacheColorHint);
            }
        }
        if (this.mAdapterHasStableIds) {
            LayoutParams lp;
            android.view.ViewGroup.LayoutParams vlp = child.getLayoutParams();
            if (vlp == null) {
                lp = (LayoutParams) generateDefaultLayoutParams();
            } else if (checkLayoutParams(vlp)) {
                lp = (LayoutParams) vlp;
            } else {
                lp = (LayoutParams) generateLayoutParams(vlp);
            }
            lp.itemId = this.mAdapter.getItemId(position);
            child.setLayoutParams(lp);
        }
        if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            if (this.mAccessibilityDelegate == null) {
                this.mAccessibilityDelegate = new ListItemAccessibilityDelegate();
            }
            if (child.getAccessibilityDelegate() == null) {
                child.setAccessibilityDelegate(this.mAccessibilityDelegate);
            }
        }
        Trace.traceEnd(8);
        return child;
    }

    public void onInitializeAccessibilityNodeInfoForItem(View view, int position, AccessibilityNodeInfo info) {
        ListAdapter adapter = (ListAdapter) getAdapter();
        if (position != -1 && adapter != null) {
            if (isEnabled() && adapter.isEnabled(position)) {
                if (position == getSelectedItemPosition()) {
                    info.setSelected(true);
                    info.addAction(8);
                } else {
                    info.addAction(4);
                }
                if (isClickable()) {
                    info.addAction(16);
                    info.setClickable(true);
                }
                if (isLongClickable()) {
                    info.addAction(32);
                    info.setLongClickable(true);
                    return;
                }
                return;
            }
            info.setEnabled(false);
        }
    }

    void positionSelector(int position, View sel) {
        if (position != -1) {
            this.mSelectorPosition = position;
        }
        Rect selectorRect = this.mSelectorRect;
        selectorRect.set(sel.getLeft(), sel.getTop(), sel.getRight(), sel.getBottom());
        if (sel instanceof SelectionBoundsAdjuster) {
            ((SelectionBoundsAdjuster) sel).adjustListItemSelectionBounds(selectorRect);
        }
        positionSelector(selectorRect.left, selectorRect.top, selectorRect.right, selectorRect.bottom);
        boolean isChildViewEnabled = this.mIsChildViewEnabled;
        if (sel.isEnabled() != isChildViewEnabled) {
            this.mIsChildViewEnabled = !isChildViewEnabled;
            if (getSelectedItemPosition() != -1) {
                refreshDrawableState();
            }
        }
    }

    private void positionSelector(int l, int t, int r, int b) {
        this.mSelectorRect.set(l - this.mSelectionLeftPadding, t - this.mSelectionTopPadding, this.mSelectionRightPadding + r, this.mSelectionBottomPadding + b);
    }

    protected void dispatchDraw(Canvas canvas) {
        int saveCount = 0;
        boolean clipToPadding = (this.mGroupFlags & 34) == 34;
        if (clipToPadding) {
            saveCount = canvas.save();
            int scrollX = this.mScrollX;
            int scrollY = this.mScrollY;
            canvas.clipRect(this.mPaddingLeft + scrollX, this.mPaddingTop + scrollY, ((this.mRight + scrollX) - this.mLeft) - this.mPaddingRight, ((this.mBottom + scrollY) - this.mTop) - this.mPaddingBottom);
            this.mGroupFlags &= -35;
        }
        boolean drawSelectorOnTop = this.mDrawSelectorOnTop;
        if (!drawSelectorOnTop) {
            drawSelector(canvas);
        }
        super.dispatchDraw(canvas);
        if (drawSelectorOnTop) {
            drawSelector(canvas);
        }
        if (clipToPadding) {
            canvas.restoreToCount(saveCount);
            this.mGroupFlags |= 34;
        }
    }

    protected boolean isPaddingOffsetRequired() {
        return (this.mGroupFlags & 34) != 34;
    }

    protected int getLeftPaddingOffset() {
        return (this.mGroupFlags & 34) == 34 ? 0 : -this.mPaddingLeft;
    }

    protected int getTopPaddingOffset() {
        return (this.mGroupFlags & 34) == 34 ? 0 : -this.mPaddingTop;
    }

    protected int getRightPaddingOffset() {
        return (this.mGroupFlags & 34) == 34 ? 0 : this.mPaddingRight;
    }

    protected int getBottomPaddingOffset() {
        return (this.mGroupFlags & 34) == 34 ? 0 : this.mPaddingBottom;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (getChildCount() > 0) {
            this.mDataChanged = true;
            rememberSyncState();
        }
        if (this.mFastScroller != null) {
            this.mFastScroller.onSizeChanged(w, h, oldw, oldh);
        }
    }

    boolean touchModeDrawsInPressedState() {
        switch (this.mTouchMode) {
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }

    boolean shouldShowSelector() {
        boolean isFingerAirView;
        if (Settings$System.getInt(this.mContext.getContentResolver(), Settings$System.FINGER_AIR_VIEW, 0) == 1) {
            isFingerAirView = true;
        } else {
            isFingerAirView = false;
        }
        boolean isFingerAirViewPreview;
        if (Settings$System.getInt(this.mContext.getContentResolver(), Settings$System.FINGER_AIR_VIEW_INFORMATION_PREVIEW, 0) == 1) {
            isFingerAirViewPreview = true;
        } else {
            isFingerAirViewPreview = false;
        }
        if ((!hasFocus() || isInTouchMode()) && ((!touchModeDrawsInPressedState() || !isPressed()) && (!isFingerAirView || !isFingerAirViewPreview || !isHovered() || !isFingerHovered()))) {
            return false;
        }
        return true;
    }

    boolean shouldShowSelectorDefault() {
        return !isInTouchMode() || (touchModeDrawsInPressedState() && isPressed());
    }

    private void drawSelector(Canvas canvas) {
        Rect tempSelectorRect = new Rect();
        if (!this.mSelectorRect.isEmpty()) {
            Drawable selector = this.mSelector;
            selector.setBounds(this.mSelectorRect);
            selector.draw(canvas);
        }
        if (this.mIsMultiFocusEnabled) {
            for (int i = 0; i < this.mTwPressItemListIndex; i++) {
                View selectedChild = getChildAt(this.mTwPressItemListArray[i] - this.mFirstPosition);
                if (selectedChild != null) {
                    tempSelectorRect.set(selectedChild.getLeft(), selectedChild.getTop(), selectedChild.getRight(), selectedChild.getBottom());
                    this.mMultiFocusImage.setBounds(tempSelectorRect);
                    this.mMultiFocusImage.draw(canvas);
                }
            }
        }
    }

    public void setDrawSelectorOnTop(boolean onTop) {
        this.mDrawSelectorOnTop = onTop;
    }

    public void setSelector(int resID) {
        setSelector(getResources().getDrawable(resID));
    }

    public void setSelector(Drawable sel) {
        if (this.mSelector != null) {
            this.mSelector.setCallback(null);
            unscheduleDrawable(this.mSelector);
        }
        this.mSelector = sel;
        Rect padding = new Rect();
        sel.getPadding(padding);
        this.mSelectionLeftPadding = padding.left;
        this.mSelectionTopPadding = padding.top;
        this.mSelectionRightPadding = padding.right;
        this.mSelectionBottomPadding = padding.bottom;
        sel.setCallback(this);
        updateSelectorState();
    }

    public Drawable getSelector() {
        return this.mSelector;
    }

    void keyPressed() {
        if (isEnabled() && isClickable()) {
            Drawable selector = this.mSelector;
            Rect selectorRect = this.mSelectorRect;
            if (selector == null) {
                return;
            }
            if ((isFocused() || touchModeDrawsInPressedState()) && !selectorRect.isEmpty()) {
                View v = getChildAt(this.mSelectedPosition - this.mFirstPosition);
                if (v != null) {
                    if (!v.hasFocusable()) {
                        v.setPressed(true);
                    } else {
                        return;
                    }
                }
                setPressed(true);
                boolean longClickable = isLongClickable();
                Drawable d = selector.getCurrent();
                if (d != null && (d instanceof TransitionDrawable)) {
                    if (longClickable) {
                        ((TransitionDrawable) d).startTransition(ViewConfiguration.getLongPressTimeout());
                    } else {
                        ((TransitionDrawable) d).resetTransition();
                    }
                }
                if (longClickable && !this.mDataChanged) {
                    if (this.mPendingCheckForKeyLongPress == null) {
                        this.mPendingCheckForKeyLongPress = new CheckForKeyLongPress();
                    }
                    this.mPendingCheckForKeyLongPress.rememberWindowAttachCount();
                    postDelayed(this.mPendingCheckForKeyLongPress, (long) ViewConfiguration.getLongPressTimeout());
                }
            }
        }
    }

    public void setScrollIndicators(View left, View right) {
        this.mScrollLeft = left;
        this.mScrollRight = right;
    }

    void updateSelectorState() {
        if (this.mSelector == null) {
            return;
        }
        if (!shouldShowSelector()) {
            this.mSelector.setState(StateSet.NOTHING);
        } else if (!isHovered() || this.mIsHoveredByMouse || this.mSelectorPosition < this.mFirstPosition) {
            this.mSelector.setState(getDrawableState());
        } else {
            View child = getChildAt(this.mSelectorPosition - this.mFirstPosition);
            boolean ellipsized = findEllipsizedTextView(child);
            if (this.mIsPnePressed || (!this.mAlwaysDisableHoverHighlight && ellipsized && (child == null || child.isEnabled()))) {
                this.mSelector.setState(getDrawableState());
                return;
            }
            this.mSelector.setState(StateSet.NOTHING);
            this.mSelectorRect.setEmpty();
        }
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        updateSelectorState();
    }

    protected int[] onCreateDrawableState(int extraSpace) {
        if (this.mIsChildViewEnabled) {
            return super.onCreateDrawableState(extraSpace);
        }
        int enabledState = ENABLED_STATE_SET[0];
        int[] state = super.onCreateDrawableState(extraSpace + 1);
        int enabledPos = -1;
        for (int i = state.length - 1; i >= 0; i--) {
            if (state[i] == enabledState) {
                enabledPos = i;
                break;
            }
        }
        if (enabledPos < 0) {
            return state;
        }
        System.arraycopy(state, enabledPos + 1, state, enabledPos, (state.length - enabledPos) - 1);
        return state;
    }

    public boolean verifyDrawable(Drawable dr) {
        return this.mSelector == dr || super.verifyDrawable(dr);
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mSelector != null) {
            this.mSelector.jumpToCurrentState();
        }
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewTreeObserver treeObserver = getViewTreeObserver();
        treeObserver.addOnTouchModeChangeListener(this);
        if (this.USE_SET_INTEGRATOR_HAPTIC) {
            treeObserver.addOnPreDrawListener(this.mHapticPreDrawListener);
        }
        if (!(!this.mTextFilterEnabled || this.mPopup == null || this.mGlobalLayoutListenerAddedFilter)) {
            treeObserver.addOnGlobalLayoutListener(this);
        }
        if (this.mAdapter != null && this.mDataSetObserver == null) {
            this.mDataSetObserver = new AdapterDataSetObserver();
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            this.mDataChanged = true;
            this.mOldItemCount = this.mItemCount;
            this.mItemCount = this.mAdapter.getCount();
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "onDetachedFromWindow");
        dismissPopup();
        this.mRecycler.clear();
        ViewTreeObserver treeObserver = getViewTreeObserver();
        treeObserver.removeOnTouchModeChangeListener(this);
        if (this.mTextFilterEnabled && this.mPopup != null) {
            treeObserver.removeOnGlobalLayoutListener(this);
            this.mGlobalLayoutListenerAddedFilter = false;
        }
        if (!(this.mAdapter == null || this.mDataSetObserver == null)) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
            this.mDataSetObserver = null;
        }
        if (this.mScrollStrictSpan != null) {
            this.mScrollStrictSpan.finish();
            this.mScrollStrictSpan = null;
        }
        if (this.mFlingStrictSpan != null) {
            this.mFlingStrictSpan.finish();
            this.mFlingStrictSpan = null;
        }
        if (this.mFlingRunnable != null) {
            removeCallbacks(this.mFlingRunnable);
        }
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        if (this.mClearScrollingCache != null) {
            removeCallbacks(this.mClearScrollingCache);
        }
        if (this.mPerformClick != null) {
            removeCallbacks(this.mPerformClick);
        }
        if (this.mTouchModeReset != null) {
            removeCallbacks(this.mTouchModeReset);
            this.mTouchModeReset.run();
        }
        if (this.mTouchMode != -1) {
            this.mTouchMode = -1;
        }
        if (this.USE_SET_INTEGRATOR_HAPTIC) {
            treeObserver.removeOnPreDrawListener(this.mHapticPreDrawListener);
        }
        releaseAllBoosters();
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        int touchMode;
        super.onWindowFocusChanged(hasWindowFocus);
        this.mHasWindowFocusForMotion = hasWindowFocus;
        if (isInTouchMode()) {
            touchMode = 0;
        } else {
            touchMode = 1;
        }
        if (hasWindowFocus) {
            if (this.mFiltered && !this.mPopupHidden) {
                showPopup();
            }
            if (!(touchMode == this.mLastTouchMode || this.mLastTouchMode == -1)) {
                if (touchMode == 1) {
                    resurrectSelection();
                } else {
                    hideSelector();
                    this.mLayoutMode = 0;
                    layoutChildren();
                }
            }
        } else {
            setChildrenDrawingCacheEnabled(false);
            if (this.mFlingRunnable != null) {
                removeCallbacks(this.mFlingRunnable);
                this.mFlingRunnable.endFling();
                if (this.mPositionScroller != null) {
                    this.mPositionScroller.stop();
                }
                if (this.mScrollY != 0) {
                    this.mScrollY = 0;
                    invalidateParentCaches();
                    finishGlows();
                    invalidate();
                }
            }
            dismissPopup();
            if (touchMode == 1) {
                this.mResurrectToPosition = this.mSelectedPosition;
            }
        }
        this.mLastTouchMode = touchMode;
        if (!hasWindowFocus) {
            releaseAllBoosters();
        }
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        if (this.mFastScroller != null) {
            this.mFastScroller.setScrollbarPosition(getTwHorizontalScrollbarPosition());
        }
    }

    ContextMenuInfo createContextMenuInfo(View view, int position, long id) {
        return new AdapterContextMenuInfo(view, position, id);
    }

    public void onCancelPendingInputEvents() {
        super.onCancelPendingInputEvents();
        if (this.mPerformClick != null) {
            removeCallbacks(this.mPerformClick);
        }
        if (this.mPendingCheckForTap != null) {
            removeCallbacks(this.mPendingCheckForTap);
        }
        if (this.mPendingCheckForLongPress != null) {
            removeCallbacks(this.mPendingCheckForLongPress);
        }
        if (this.mPendingCheckForKeyLongPress != null) {
            removeCallbacks(this.mPendingCheckForKeyLongPress);
        }
    }

    public void setForcedClick(boolean force) {
        this.mForcedClick = force;
    }

    boolean performLongPress(View child, int longPressPosition, long longPressId) {
        boolean z = true;
        if (this.mChoiceMode != 3) {
            z = false;
            if (this.mOnItemLongClickListener != null) {
                z = this.mOnItemLongClickListener.onItemLongClick(this, child, longPressPosition, longPressId);
            }
            if (!z) {
                this.mContextMenuInfo = createContextMenuInfo(child, longPressPosition, longPressId);
                z = super.showContextMenuForChild(this);
            }
            if (z) {
                performHapticFeedback(HapticFeedbackConstants.VIBE_COMMON_A);
            }
        } else if (this.mChoiceActionMode == null) {
            ActionMode startActionMode = startActionMode(this.mMultiChoiceModeCallback);
            this.mChoiceActionMode = startActionMode;
            if (startActionMode != null) {
                setItemChecked(longPressPosition, true);
                performHapticFeedback(HapticFeedbackConstants.VIBE_COMMON_A);
            }
        }
        return z;
    }

    protected ContextMenuInfo getContextMenuInfo() {
        return this.mContextMenuInfo;
    }

    public boolean showContextMenu(float x, float y, int metaState) {
        int position = pointToPosition((int) x, (int) y);
        if (position != -1) {
            long id = this.mAdapter.getItemId(position);
            View child = getChildAt(position - this.mFirstPosition);
            if (child != null) {
                this.mContextMenuInfo = createContextMenuInfo(child, position, id);
                return super.showContextMenuForChild(this);
            }
        }
        this.mContextMenuInfo = null;
        return super.showContextMenu(x, y, metaState);
    }

    public boolean showContextMenuForChild(View originalView) {
        int longPressPosition = getPositionForView(originalView);
        if (longPressPosition < 0) {
            return false;
        }
        long longPressId = this.mAdapter.getItemId(longPressPosition);
        boolean handled = false;
        if (this.mOnItemLongClickListener != null) {
            handled = this.mOnItemLongClickListener.onItemLongClick(this, originalView, longPressPosition, longPressId);
        }
        if (handled) {
            return handled;
        }
        this.mContextMenuInfo = createContextMenuInfo(getChildAt(longPressPosition - this.mFirstPosition), longPressPosition, longPressId);
        return super.showContextMenuForChild(originalView);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 59:
            case 60:
                this.mIsShiftkeyPressed = true;
                break;
            case 113:
            case 114:
                this.mIsCtrlkeyPressed = true;
                break;
        }
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        View selectedview = getChildAt(this.mSelectedPosition - this.mFirstPosition);
        if (KeyEvent.isConfirmKey(keyCode)) {
            if (!isEnabled()) {
                return true;
            }
            if (isClickable() && isPressed() && this.mSelectedPosition >= 0 && this.mAdapter != null && this.mSelectedPosition < this.mAdapter.getCount()) {
                View view = getChildAt(this.mSelectedPosition - this.mFirstPosition);
                if (view != null) {
                    performItemClick(view, this.mSelectedPosition, this.mSelectedRowId);
                    view.setPressed(false);
                }
                setPressed(false);
                return true;
            }
        }
        switch (keyCode) {
            case 19:
            case 20:
            case 21:
            case 22:
                if (this.mIsShiftkeyPressed) {
                    if (this.mOldKeyCode == 0) {
                        this.mOldKeyCode = keyCode;
                    } else {
                        this.mCurrentKeyCode = keyCode;
                    }
                }
                if (isClickable() && this.mSelectedPosition >= 0 && this.mAdapter != null && this.mSelectedPosition < this.mAdapter.getCount()) {
                    View currentview = getChildAt(this.mTwCurrentFocusPosition);
                    if (this.mIsShiftkeyPressed && selectedview != null) {
                        if (this.mCurrentKeyCode == 0) {
                            twNotifyKeyPressState(currentview, this.mTwCurrentFocusPosition, this.mSelectedRowId);
                            twNotifyKeyPressState(selectedview, this.mSelectedPosition, this.mSelectedRowId);
                            addToPressItemListArray(this.mTwCurrentFocusPosition, this.mSelectedPosition);
                            this.mFirstPressedPoint = this.mTwCurrentFocusPosition;
                        } else {
                            resetPressItemListArray();
                            twNotifyKeyPressState(selectedview, this.mSelectedPosition, this.mSelectedRowId);
                            addToPressItemListArray(this.mFirstPressedPoint, this.mSelectedPosition);
                        }
                    }
                    if (this.mCurrentKeyCode != 0) {
                        this.mOldKeyCode = this.mCurrentKeyCode;
                        break;
                    }
                }
                break;
            case 31:
                if (this.mIsCtrlkeyPressed) {
                    resetPressItemListArray();
                    break;
                }
                break;
            case 59:
            case 60:
                this.mIsShiftkeyPressed = false;
                this.mOldKeyCode = 0;
                this.mCurrentKeyCode = 0;
                this.mFirstPressedPoint = -1;
                this.mSecondPressedPoint = -1;
                break;
            case 113:
            case 114:
                this.mIsCtrlkeyPressed = false;
                break;
        }
        return super.onKeyUp(keyCode, event);
    }

    protected void dispatchSetPressed(boolean pressed) {
    }

    public int pointToPosition(int x, int y) {
        Rect frame = this.mTouchFrame;
        if (frame == null) {
            this.mTouchFrame = new Rect();
            frame = this.mTouchFrame;
        }
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return this.mFirstPosition + i;
                }
            }
        }
        return -1;
    }

    public long pointToRowId(int x, int y) {
        int position = pointToPosition(x, y);
        if (position >= 0) {
            return this.mAdapter.getItemId(position);
        }
        return Long.MIN_VALUE;
    }

    private boolean startScrollIfNeeded(int x, int y, MotionEvent vtev) {
        boolean overscroll;
        int deltaX = x - this.mMotionX;
        int distance = Math.abs(deltaX);
        if (this.mScrollX != 0) {
            overscroll = true;
        } else {
            overscroll = false;
        }
        if (!overscroll && distance <= this.mTouchSlop) {
            return false;
        }
        createScrollingCache();
        if (overscroll) {
            this.mTouchMode = 5;
            this.mMotionCorrection = 0;
        } else {
            this.mTouchMode = 3;
            this.mMotionCorrection = deltaX > 0 ? this.mTouchSlop : -this.mTouchSlop;
        }
        removeCallbacks(this.mPendingCheckForLongPress);
        setPressed(false);
        View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
        if (motionView != null) {
            motionView.setPressed(false);
        }
        if (this.mPointerCount > 1) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).setPressed(false);
            }
        }
        reportScrollStateChange(1);
        ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(true);
        }
        scrollIfNeeded(x, y, vtev);
        return true;
    }

    private void scrollIfNeeded(int x, int y, MotionEvent vtev) {
        int incrementalDeltaX;
        int rawDeltaX = x - this.mMotionX;
        int deltaX = rawDeltaX - this.mMotionCorrection;
        if (this.mLastX != Integer.MIN_VALUE) {
            incrementalDeltaX = x - this.mLastX;
        } else {
            incrementalDeltaX = deltaX;
        }
        View motionView;
        int overscrollMode;
        if (this.mTouchMode == 3) {
            if (this.mScrollStrictSpan == null) {
                this.mScrollStrictSpan = StrictMode.enterCriticalSpan("AbsHorizontalListView-scroll");
            }
            if (x != this.mLastX) {
                int motionIndex;
                if ((this.mGroupFlags & 524288) == 0 && Math.abs(rawDeltaX) > this.mTouchSlop) {
                    ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                }
                if (this.mMotionPosition >= 0) {
                    motionIndex = this.mMotionPosition - this.mFirstPosition;
                } else {
                    motionIndex = getChildCount() / 2;
                }
                int motionViewPrevLeft = 0;
                motionView = getChildAt(motionIndex);
                if (motionView != null) {
                    motionViewPrevLeft = motionView.getLeft();
                }
                boolean atEdge = false;
                if (incrementalDeltaX != 0) {
                    atEdge = trackMotionScroll(deltaX, incrementalDeltaX);
                }
                motionView = getChildAt(motionIndex);
                if (motionView != null) {
                    int motionViewRealLeft = motionView.getLeft();
                    if (atEdge) {
                        int overscroll = (-incrementalDeltaX) - (motionViewRealLeft - motionViewPrevLeft);
                        overScrollBy(overscroll, 0, this.mScrollX, 0, 0, 0, this.mOverscrollDistance, 0, true);
                        if (Math.abs(this.mOverscrollDistance) == Math.abs(this.mScrollX) && this.mVelocityTracker != null) {
                            this.mVelocityTracker.clear();
                        }
                        overscrollMode = getOverScrollMode();
                        if (overscrollMode == 0 || (overscrollMode == 1 && !contentFits())) {
                            this.mDirection = 0;
                            int oldTouchMode = this.mTouchMode;
                            this.mTouchMode = 5;
                            if (rawDeltaX > 0) {
                                this.mEdgeGlowLeft.onPull(((float) (-overscroll)) / ((float) getWidth()), 1.0f - (((float) y) / ((float) getHeight())));
                                if (!this.mEdgeGlowRight.isFinished()) {
                                    this.mEdgeGlowRight.onRelease();
                                }
                                invalidate(0, 0, this.mEdgeGlowLeft.getMaxHeight() + getPaddingLeft(), getHeight());
                            } else if (rawDeltaX < 0) {
                                this.mEdgeGlowRight.onPull(((float) overscroll) / ((float) getWidth()), ((float) y) / ((float) getHeight()));
                                if (!this.mEdgeGlowLeft.isFinished()) {
                                    this.mEdgeGlowLeft.onRelease();
                                }
                                invalidate((getWidth() - getPaddingRight()) - this.mEdgeGlowRight.getMaxHeight(), 0, getWidth(), getHeight());
                            }
                        }
                    }
                    this.mMotionX = x;
                }
                this.mLastX = x;
            }
        } else if (this.mTouchMode == 5 && x != this.mLastX) {
            int oldScroll = this.mScrollX;
            int newScroll = oldScroll - incrementalDeltaX;
            int newDirection = x > this.mLastX ? 1 : -1;
            if (this.mDirection == 0) {
                this.mDirection = newDirection;
            }
            int overScrollDistance = -incrementalDeltaX;
            if ((newScroll >= 0 || oldScroll < 0) && (newScroll <= 0 || oldScroll > 0)) {
                incrementalDeltaX = 0;
            } else {
                overScrollDistance = -oldScroll;
                incrementalDeltaX += overScrollDistance;
            }
            if (overScrollDistance != 0) {
                overScrollBy(overScrollDistance, 0, this.mScrollX, 0, 0, 0, this.mOverscrollDistance, 0, true);
                overscrollMode = getOverScrollMode();
                if (overscrollMode == 0 || (overscrollMode == 1 && !contentFits())) {
                    if (rawDeltaX > 0) {
                        this.mEdgeGlowLeft.onPull(((float) overScrollDistance) / ((float) getWidth()), 1.0f - (((float) y) / ((float) getHeight())));
                        if (!this.mEdgeGlowRight.isFinished()) {
                            this.mEdgeGlowRight.onRelease();
                        }
                        invalidate(0, 0, this.mEdgeGlowLeft.getMaxHeight() + getPaddingLeft(), getHeight());
                    } else if (rawDeltaX < 0) {
                        this.mEdgeGlowRight.onPull(((float) overScrollDistance) / ((float) getWidth()), (float) (y / getHeight()));
                        if (!this.mEdgeGlowLeft.isFinished()) {
                            this.mEdgeGlowLeft.onRelease();
                        }
                        invalidate((getWidth() - getPaddingRight()) - this.mEdgeGlowRight.getMaxHeight(), 0, getWidth(), getHeight());
                    }
                }
            }
            if (incrementalDeltaX != 0) {
                if (this.mScrollX != 0) {
                    this.mScrollX = 0;
                    invalidateParentIfNeeded();
                }
                trackMotionScroll(incrementalDeltaX, incrementalDeltaX);
                this.mTouchMode = 3;
                int motionPosition = findClosestMotionRow(x);
                this.mMotionCorrection = 0;
                motionView = getChildAt(motionPosition - this.mFirstPosition);
                this.mMotionViewOriginalLeft = motionView != null ? motionView.getLeft() : 0;
                this.mMotionX = x;
                this.mMotionPosition = motionPosition;
            }
            this.mLastX = x;
            this.mDirection = newDirection;
        }
    }

    public void onTouchModeChanged(boolean isInTouchMode) {
        if (isInTouchMode) {
            hideSelector();
            if (getWidth() > 0 && getChildCount() > 0) {
                layoutChildren();
            }
            updateSelectorState();
            return;
        }
        int touchMode = this.mTouchMode;
        if (touchMode == 5 || touchMode == 6) {
            if (this.mFlingRunnable != null) {
                this.mFlingRunnable.endFling();
            }
            if (this.mPositionScroller != null) {
                this.mPositionScroller.stop();
            }
            if (this.mScrollX != 0) {
                this.mScrollX = 0;
                invalidateParentCaches();
                finishGlows();
                invalidate();
            }
        }
    }

    public boolean isLockScreenMode() {
        Context context = this.mContext;
        Context context2 = this.mContext;
        boolean isLockState = ((KeyguardManager) context.getSystemService("keyguard")).inKeyguardRestrictedInputMode();
        context = this.mContext;
        IWindowManager windowManager = Stub.asInterface(ServiceManager.getService("window"));
        return isLockState && true;
    }

    public void setHoverScrollMode(boolean flag) {
        if (flag) {
            this.mHoverScrollEnable = true;
        } else {
            this.mHoverScrollEnable = false;
        }
    }

    public void setHoverScrollSpeed(int hoverspeed) {
        this.HOVERSCROLL_SPEED = hoverspeed;
    }

    public void setHoverScrollDelay(int hoverdelay) {
        this.HOVERSCROLL_DELAY = hoverdelay;
    }

    protected boolean dispatchHoverEvent(MotionEvent ev) {
        boolean isHoveringOn = Settings$System.getInt(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING, 0) == 1;
        boolean isHoverListScrollOn = Settings$System.getInt(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING_LIST_SCROLL, 0) == 1;
        if (this.mHoverHandler == null) {
            this.mHoverHandler = new HoverScrollHandler();
        }
        onHoverDrawableState(ev);
        if (!this.isHoveringUIEnabled || !this.mHoverScrollEnable || ((ev.getToolType(0) == 2 && (!isHoveringOn || !isHoverListScrollOn)) || ev.getToolType(0) == 3)) {
            return super.dispatchHoverEvent(ev);
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int childCount = getChildCount();
        int contentRight = 0;
        int count = getChildCount();
        if (this.mHoverLeftAreaWidth <= 0 || this.mHoverRightAreaWidth <= 0) {
            this.mHoverLeftAreaWidth = (int) (TypedValue.applyDimension(1, (float) this.mHoverLeftAreaWidth_DP, this.mContext.getResources().getDisplayMetrics()) + 0.5f);
            this.mHoverRightAreaWidth = (int) (TypedValue.applyDimension(1, (float) this.mHoverRightAreaWidth_DP, this.mContext.getResources().getDisplayMetrics()) + 0.5f);
        }
        if (childCount != 0) {
            contentRight = getWidth();
        }
        boolean canScrollRight = this.mFirstPosition + count < this.mItemCount;
        if (!canScrollRight && count > 0) {
            View child = getChildAt(count - 1);
            canScrollRight = child.getRight() > this.mRight - this.mListPadding.right || child.getWidth() > getWidth() - this.mListPadding.right;
        }
        boolean canScrollLeft = this.mFirstPosition > 0;
        if (!canScrollLeft && getChildCount() > 0) {
            canScrollLeft = getChildAt(0).getLeft() < this.mListPadding.left;
        }
        boolean isPossibleTooltype = ev.getToolType(0) == 2;
        if ((x <= this.mHoverLeftAreaWidth || x >= contentRight - this.mHoverRightAreaWidth) && y > 0 && y <= getBottom() && ((canScrollLeft || canScrollRight) && !((isPossibleTooltype && ev.getButtonState() == 32) || !isPossibleTooltype || isLockScreenMode()))) {
            if (!this.mHoverAreaEnter) {
                this.mHoverScrollStartTime = (int) System.currentTimeMillis();
                if (this.mOnScrollListener != null) {
                    this.mOnScrollListener.onScrollStateChanged(this, 1);
                }
            }
            Message msg;
            switch (ev.getAction()) {
                case 7:
                    if (this.mHoverAreaEnter) {
                        if (x >= 0 && x <= this.mHoverLeftAreaWidth) {
                            if (!this.mHoverHandler.hasMessages(1)) {
                                this.mHoverRecognitionStartTime = (int) System.currentTimeMillis();
                                if (!this.mIsHoverOverscrolled || this.mHoverScrollDirection == 1) {
                                    try {
                                        PointerIcon.setHoveringSpenIcon(17, -1);
                                    } catch (RemoteException e) {
                                        Log.e(TAG, "Failed to change Pen Point to HOVERING_SCROLL_LEFT");
                                    }
                                }
                                msg = Message.obtain();
                                msg.what = 1;
                                this.mHoverScrollDirection = 2;
                                this.mHoverHandler.sendMessage(msg);
                                break;
                            }
                        } else if (x >= contentRight - this.mHoverRightAreaWidth && x <= contentRight && !this.mHoverHandler.hasMessages(1)) {
                            this.mHoverRecognitionStartTime = (int) System.currentTimeMillis();
                            if (!this.mIsHoverOverscrolled || this.mHoverScrollDirection == 2) {
                                try {
                                    PointerIcon.setHoveringSpenIcon(13, -1);
                                } catch (RemoteException e2) {
                                    Log.e(TAG, "Failed to change Pen Point to HOVERING_SCROLL_RIGHT");
                                }
                            }
                            msg = Message.obtain();
                            msg.what = 1;
                            this.mHoverScrollDirection = 1;
                            this.mHoverHandler.sendMessage(msg);
                            break;
                        }
                    }
                    this.mHoverAreaEnter = true;
                    ev.setAction(10);
                    return super.dispatchHoverEvent(ev);
                    break;
                case 9:
                    this.mHoverAreaEnter = true;
                    if (x >= 0 && x <= this.mHoverLeftAreaWidth) {
                        if (!this.mHoverHandler.hasMessages(1)) {
                            this.mHoverRecognitionStartTime = (int) System.currentTimeMillis();
                            try {
                                PointerIcon.setHoveringSpenIcon(17, -1);
                            } catch (RemoteException e3) {
                                Log.e(TAG, "Failed to change Pen Point to HOVERING_SCROLL_LEFT");
                            }
                            msg = Message.obtain();
                            msg.what = 1;
                            this.mHoverScrollDirection = 2;
                            this.mHoverHandler.sendMessage(msg);
                            break;
                        }
                    } else if (x >= contentRight - this.mHoverRightAreaWidth && x <= contentRight && !this.mHoverHandler.hasMessages(1)) {
                        this.mHoverRecognitionStartTime = (int) System.currentTimeMillis();
                        try {
                            PointerIcon.setHoveringSpenIcon(13, -1);
                        } catch (RemoteException e4) {
                            Log.e(TAG, "Failed to change Pen Point to HOVERING_SCROLL_RIGHT");
                        }
                        msg = Message.obtain();
                        msg.what = 1;
                        this.mHoverScrollDirection = 1;
                        this.mHoverHandler.sendMessage(msg);
                        break;
                    }
                    break;
                case 10:
                    if (this.mHoverHandler.hasMessages(1)) {
                        this.mHoverHandler.removeMessages(1);
                    }
                    try {
                        PointerIcon.setHoveringSpenIcon(1, -1);
                    } catch (RemoteException e5) {
                        Log.e(TAG, "Failed to change Pen Point to HOVERING_SPENICON_DEFAULT");
                    }
                    this.mHoverRecognitionStartTime = 0;
                    this.mHoverScrollStartTime = 0;
                    this.mIsHoverOverscrolled = false;
                    this.mHoverAreaEnter = false;
                    if (this.mOnScrollListener != null) {
                        this.mOnScrollListener.onScrollStateChanged(this, 0);
                    }
                    return super.dispatchHoverEvent(ev);
            }
            return true;
        }
        if (this.mHoverHandler.hasMessages(1)) {
            this.mHoverHandler.removeMessages(1);
            try {
                PointerIcon.setHoveringSpenIcon(1, -1);
            } catch (RemoteException e6) {
                Log.e(TAG, "Failed to change Pen Point to HOVERING_SPENICON_DEFAULT");
            }
        }
        if (this.mIsHoverOverscrolled || this.mHoverScrollStartTime != 0) {
            this.mIsHoverOverscrolled = false;
            try {
                PointerIcon.setHoveringSpenIcon(1, -1);
            } catch (RemoteException e7) {
                Log.e(TAG, "Failed to change Pen Point to HOVERING_SPENICON_DEFAULT");
            }
        }
        this.mHoverRecognitionStartTime = 0;
        this.mHoverScrollStartTime = 0;
        this.mHoverAreaEnter = false;
        return super.dispatchHoverEvent(ev);
    }

    public boolean dispatchDragEvent(DragEvent ev) {
        int action = ev.getAction();
        ClipDescription cd = ev.getClipDescription();
        if (cd == null || !"cropUri".equals(cd.getLabel())) {
            return super.dispatchDragEvent(ev);
        }
        if (action == 1) {
            if (this.mDragScrollWorkingZonePx <= 0) {
                this.mDragScrollWorkingZonePx = (int) (TypedValue.applyDimension(1, 25.0f, this.mContext.getResources().getDisplayMetrics()) + 0.5f);
            }
            if (super.dispatchDragEvent(ev)) {
            }
            return true;
        }
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int childCount = getChildCount();
        int contentRight = 0;
        int count = getChildCount();
        if (childCount != 0) {
            contentRight = getWidth();
        }
        boolean canScrollRight = this.mFirstPosition + count < this.mItemCount;
        if (!canScrollRight && count > 0) {
            View child = getChildAt(count - 1);
            canScrollRight = child.getRight() > this.mRight - this.mListPadding.right || child.getRight() > getWidth() - this.mListPadding.right;
        }
        boolean canScrollLeft = this.mFirstPosition > 0;
        if (!canScrollLeft && getChildCount() > 0) {
            canScrollLeft = getChildAt(0).getLeft() < this.mListPadding.left;
        }
        if ((x <= this.mDragScrollWorkingZonePx || x >= contentRight - this.mDragScrollWorkingZonePx) && y > 0 && y <= getBottom() && (canScrollLeft || canScrollRight)) {
            if (this.mHoverHandler == null) {
                this.mHoverHandler = new HoverScrollHandler();
            }
            if (!this.mHoverAreaEnter) {
                this.mHoverScrollStartTime = (int) System.currentTimeMillis();
            }
            Message msg;
            switch (action) {
                case 2:
                    if (!this.mHoverAreaEnter) {
                        this.mHoverAreaEnter = true;
                    }
                    if (x >= 0 && x <= this.mDragScrollWorkingZonePx) {
                        if (!this.mHoverHandler.hasMessages(1)) {
                            this.mIsDragScrolled = true;
                            this.mHoverRecognitionStartTime = (int) System.currentTimeMillis();
                            msg = Message.obtain();
                            msg.what = 1;
                            this.mHoverScrollDirection = 2;
                            this.mHoverHandler.sendMessage(msg);
                            break;
                        }
                    } else if (x >= contentRight - this.mDragScrollWorkingZonePx && x <= contentRight && !this.mHoverHandler.hasMessages(1)) {
                        this.mIsDragScrolled = true;
                        this.mHoverRecognitionStartTime = (int) System.currentTimeMillis();
                        msg = Message.obtain();
                        msg.what = 1;
                        this.mHoverScrollDirection = 1;
                        this.mHoverHandler.sendMessage(msg);
                        break;
                    }
                    break;
                case 3:
                    if (this.mIsDragScrolled) {
                        this.mIsDragScrolled = false;
                        break;
                    }
                    break;
                case 4:
                case 6:
                    break;
                case 5:
                    this.mHoverAreaEnter = true;
                    if (x >= 0 && x <= this.mDragScrollWorkingZonePx) {
                        if (!this.mHoverHandler.hasMessages(1)) {
                            this.mIsDragScrolled = true;
                            this.mHoverRecognitionStartTime = (int) System.currentTimeMillis();
                            msg = Message.obtain();
                            msg.what = 1;
                            this.mHoverScrollDirection = 2;
                            this.mHoverHandler.sendMessage(msg);
                            break;
                        }
                    } else if (x >= contentRight - this.mDragScrollWorkingZonePx && x <= contentRight && !this.mHoverHandler.hasMessages(1)) {
                        this.mIsDragScrolled = true;
                        this.mHoverRecognitionStartTime = (int) System.currentTimeMillis();
                        msg = Message.obtain();
                        msg.what = 1;
                        this.mHoverScrollDirection = 1;
                        this.mHoverHandler.sendMessage(msg);
                        break;
                    }
                    break;
            }
            if (this.mHoverHandler.hasMessages(1)) {
                this.mHoverHandler.removeMessages(1);
            }
            this.mIsDragScrolled = false;
            this.mHoverRecognitionStartTime = 0;
            this.mHoverScrollStartTime = 0;
            this.mIsHoverOverscrolled = false;
            this.mHoverAreaEnter = false;
            return super.dispatchDragEvent(ev);
        }
        if (this.mHoverHandler != null && this.mHoverHandler.hasMessages(1)) {
            this.mHoverHandler.removeMessages(1);
        }
        if (this.mIsHoverOverscrolled || this.mHoverScrollStartTime != 0) {
            this.mIsHoverOverscrolled = false;
        }
        this.mHoverRecognitionStartTime = 0;
        this.mHoverScrollStartTime = 0;
        this.mHoverAreaEnter = false;
        if (action == 2 && this.mIsDragScrolled) {
            this.mIsDragScrolled = false;
        }
        return super.dispatchDragEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        if (this.mMotionEnable && this.mMotionListener != null) {
            this.mMotionRecognitionManager.setMotionAngle(this.mMotionListener, 1);
        }
        if (isEnabled()) {
            if (this.mPositionScroller != null) {
                this.mPositionScroller.stop();
            }
            if (!isAttachedToWindow()) {
                return false;
            }
            if (this.mFastScroller != null && this.mFastScroller.onTouchEvent(ev)) {
                return true;
            }
            MotionEvent vtev = MotionEvent.obtain(ev);
            initVelocityTrackerIfNotExists();
            this.mVelocityTracker.addMovement(ev);
            int x;
            int motionPosition;
            switch (ev.getActionMasked()) {
                case 0:
                    onTouchDown(ev);
                    break;
                case 1:
                    onTouchUp(ev);
                    break;
                case 2:
                    onTouchMove(ev, vtev);
                    break;
                case 3:
                    onTouchCancel();
                    break;
                case 5:
                    int index = ev.getActionIndex();
                    int id = ev.getPointerId(index);
                    x = (int) ev.getX(index);
                    int y = (int) ev.getY(index);
                    this.mMotionCorrection = 0;
                    this.mActivePointerId = id;
                    this.mMotionX = x;
                    this.mMotionY = y;
                    motionPosition = pointToPosition(x, y);
                    if (motionPosition >= 0) {
                        this.mMotionViewOriginalLeft = getChildAt(motionPosition - this.mFirstPosition).getLeft();
                        this.mMotionPosition = motionPosition;
                    }
                    this.mLastX = x;
                    this.mPointerCount++;
                    break;
                case 6:
                    onSecondaryPointerUp(ev);
                    x = this.mMotionX;
                    motionPosition = pointToPosition(x, this.mMotionY);
                    if (motionPosition >= 0) {
                        this.mMotionViewOriginalLeft = getChildAt(motionPosition - this.mFirstPosition).getLeft();
                        this.mMotionPosition = motionPosition;
                    }
                    this.mLastX = x;
                    this.mPointerCount--;
                    break;
            }
            vtev.recycle();
            return true;
        } else if (isClickable() || isLongClickable()) {
            return true;
        } else {
            return false;
        }
    }

    private void onTouchDown(MotionEvent ev) {
        this.mActivePointerId = ev.getPointerId(0);
        this.mPointerCount++;
        if (this.mTouchMode == 6) {
            this.mFlingRunnable.endFling();
            if (this.mPositionScroller != null) {
                this.mPositionScroller.stop();
            }
            this.mTouchMode = 5;
            this.mMotionX = (int) ev.getX();
            this.mMotionY = (int) ev.getY();
            this.mLastX = this.mMotionX;
            this.mMotionCorrection = 0;
            this.mDirection = 0;
        } else {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            int motionPosition = pointToPosition(x, y);
            if (!this.mDataChanged) {
                if (this.mTouchMode == 4) {
                    createScrollingCache();
                    this.mTouchMode = 3;
                    this.mMotionCorrection = 0;
                    motionPosition = findMotionRow(y);
                    this.mFlingRunnable.flywheelTouch();
                } else if (motionPosition >= 0 && ((ListAdapter) getAdapter()).isEnabled(motionPosition)) {
                    this.mTouchMode = 0;
                    if (this.mPendingCheckForTap == null) {
                        this.mPendingCheckForTap = new CheckForTap();
                    }
                    this.mPendingCheckForTap.x = ev.getX();
                    this.mPendingCheckForTap.y = ev.getY();
                    postDelayed(this.mPendingCheckForTap, (long) ViewConfiguration.getTapTimeout());
                }
            }
            if (motionPosition >= 0) {
                this.mMotionViewOriginalLeft = getChildAt(motionPosition - this.mFirstPosition).getLeft();
            }
            this.mMotionX = x;
            this.mMotionY = y;
            this.mMotionPosition = motionPosition;
            this.mLastX = Integer.MIN_VALUE;
        }
        if (this.mTouchMode == 0 && this.mMotionPosition != -1 && performButtonActionOnTouchDown(ev)) {
            removeCallbacks(this.mPendingCheckForTap);
        }
    }

    private void onTouchMove(MotionEvent ev, MotionEvent vtev) {
        int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
        if (pointerIndex == -1) {
            pointerIndex = 0;
            this.mActivePointerId = ev.getPointerId(0);
        }
        int x = (int) ev.getX(pointerIndex);
        if (this.mDataChanged) {
            layoutChildren();
        }
        switch (this.mTouchMode) {
            case 0:
            case 1:
            case 2:
                if (!startScrollIfNeeded(x, (int) ev.getY(pointerIndex), vtev)) {
                    if (!pointInView((float) x, ev.getY(pointerIndex), (float) this.mTouchSlop)) {
                        setPressed(false);
                        View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
                        if (motionView != null) {
                            motionView.setPressed(false);
                        }
                        removeCallbacks(this.mTouchMode == 0 ? this.mPendingCheckForTap : this.mPendingCheckForLongPress);
                        this.mTouchMode = 2;
                        updateSelectorState();
                        return;
                    }
                    return;
                }
                return;
            case 3:
            case 5:
                scrollIfNeeded(x, (int) ev.getY(pointerIndex), vtev);
                return;
            default:
                return;
        }
    }

    private void onTouchUp(MotionEvent ev) {
        VelocityTracker velocityTracker;
        int initialVelocity;
        switch (this.mTouchMode) {
            case 0:
            case 1:
            case 2:
                int motionPosition = this.mMotionPosition;
                final View child = getChildAt(motionPosition - this.mFirstPosition);
                if (child != null) {
                    if (this.mTouchMode != 0) {
                        child.setPressed(false);
                    }
                    float x = ev.getX();
                    boolean inList = x > ((float) this.mListPadding.left) && x < ((float) (getWidth() - this.mListPadding.right));
                    if (inList && !child.hasFocusable()) {
                        if (this.mPerformClick == null) {
                            this.mPerformClick = new PerformClick();
                        }
                        final PerformClick performClick = this.mPerformClick;
                        performClick.mClickMotionPosition = motionPosition;
                        performClick.rememberWindowAttachCount();
                        this.mResurrectToPosition = motionPosition;
                        if (this.mTouchMode == 0 || this.mTouchMode == 1) {
                            CheckForTap checkForTap;
                            if (this.mTouchMode == 0) {
                                checkForTap = this.mPendingCheckForTap;
                            } else {
                                Object obj = this.mPendingCheckForLongPress;
                            }
                            removeCallbacks(checkForTap);
                            this.mLayoutMode = 0;
                            if (this.mDataChanged || !this.mAdapter.isEnabled(motionPosition)) {
                                this.mTouchMode = -1;
                                updateSelectorState();
                                if (this.mForcedClick && this.mAdapter.isEnabled(motionPosition)) {
                                    performClick.run();
                                    return;
                                }
                                return;
                            }
                            this.mTouchMode = 1;
                            setSelectedPositionInt(this.mMotionPosition);
                            layoutChildren();
                            child.setPressed(true);
                            positionSelector(this.mMotionPosition, child);
                            setPressed(true);
                            if (this.mSelector != null) {
                                Drawable d = this.mSelector.getCurrent();
                                if (d != null && (d instanceof TransitionDrawable)) {
                                    ((TransitionDrawable) d).resetTransition();
                                }
                                this.mSelector.setHotspot(x, ev.getY());
                            }
                            if (this.mTouchModeReset != null) {
                                removeCallbacks(this.mTouchModeReset);
                            }
                            this.mTouchModeReset = new Runnable() {
                                public void run() {
                                    AbsHorizontalListView.this.mTouchModeReset = null;
                                    AbsHorizontalListView.this.mTouchMode = -1;
                                    child.setPressed(false);
                                    AbsHorizontalListView.this.setPressed(false);
                                    if (AbsHorizontalListView.this.mForcedClick || (!AbsHorizontalListView.this.mDataChanged && AbsHorizontalListView.this.isAttachedToWindow())) {
                                        performClick.run();
                                    }
                                }
                            };
                            postDelayed(this.mTouchModeReset, (long) ViewConfiguration.getPressedStateDuration());
                            return;
                        } else if ((this.mForcedClick || !this.mDataChanged) && this.mAdapter.isEnabled(motionPosition)) {
                            performClick.run();
                        }
                    }
                }
                this.mTouchMode = -1;
                updateSelectorState();
                break;
            case 3:
                int childCount = getChildCount();
                if (childCount <= 0) {
                    this.mTouchMode = -1;
                    reportScrollStateChange(0);
                    break;
                }
                int firstChildLeft = getChildAt(0).getLeft();
                int lastChildRight = getChildAt(childCount - 1).getRight();
                int contentLeft = this.mListPadding.left;
                int contentRight = getWidth() - this.mListPadding.right;
                if (this.mFirstPosition == 0 && firstChildLeft >= contentLeft && this.mFirstPosition + childCount < this.mItemCount && lastChildRight <= getWidth() - contentRight) {
                    this.mTouchMode = -1;
                    reportScrollStateChange(0);
                    break;
                }
                velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                initialVelocity = (int) (velocityTracker.getXVelocity(this.mActivePointerId) * this.mVelocityScale);
                if (Math.abs(initialVelocity) > this.mMinimumVelocity && ((this.mFirstPosition != 0 || firstChildLeft != contentLeft - this.mOverscrollDistance) && (this.mFirstPosition + childCount != this.mItemCount || lastChildRight != this.mOverscrollDistance + contentRight))) {
                    if (this.mFlingRunnable == null) {
                        this.mFlingRunnable = new FlingRunnable();
                    }
                    reportScrollStateChange(2);
                    this.mFlingRunnable.start(-initialVelocity);
                    break;
                }
                this.mTouchMode = -1;
                reportScrollStateChange(0);
                if (this.mFlingRunnable != null) {
                    this.mFlingRunnable.endFling();
                }
                if (this.mPositionScroller != null) {
                    this.mPositionScroller.stop();
                    break;
                }
                break;
            case 5:
                if (this.USE_SET_INTEGRATOR_HAPTIC) {
                    this.mHapticOverScroll = false;
                }
                if (this.mFlingRunnable == null) {
                    this.mFlingRunnable = new FlingRunnable();
                }
                velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                initialVelocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
                reportScrollStateChange(2);
                if (Math.abs(initialVelocity) <= this.mMinimumVelocity) {
                    this.mFlingRunnable.startSpringback();
                    break;
                } else {
                    this.mFlingRunnable.startOverfling(-initialVelocity);
                    break;
                }
        }
        setPressed(false);
        if (this.mEdgeGlowLeft != null) {
            this.mEdgeGlowLeft.onRelease();
            this.mEdgeGlowRight.onRelease();
        }
        invalidate();
        removeCallbacks(this.mPendingCheckForLongPress);
        recycleVelocityTracker();
        this.mActivePointerId = -1;
        this.mPointerCount = 0;
        if (this.mScrollStrictSpan != null) {
            this.mScrollStrictSpan.finish();
            this.mScrollStrictSpan = null;
        }
    }

    private void onTouchCancel() {
        switch (this.mTouchMode) {
            case 5:
                if (this.mFlingRunnable == null) {
                    this.mFlingRunnable = new FlingRunnable();
                }
                this.mFlingRunnable.startSpringback();
                break;
            case 6:
                break;
            default:
                this.mTouchMode = -1;
                setPressed(false);
                View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
                if (motionView != null) {
                    motionView.setPressed(false);
                }
                clearScrollingCache();
                removeCallbacks(this.mPendingCheckForLongPress);
                recycleVelocityTracker();
                break;
        }
        if (this.mEdgeGlowLeft != null) {
            this.mEdgeGlowLeft.onRelease();
            this.mEdgeGlowRight.onRelease();
        }
        this.mActivePointerId = -1;
        this.mPointerCount = 0;
    }

    public void setAlwaysDisableHoverHighlight(boolean disabled) {
        this.mAlwaysDisableHoverHighlight = disabled;
    }

    private void onHoverDrawableState(MotionEvent event) {
        int action = event.getAction();
        int toolType = event.getToolType(0);
        if ((action == 7 || action == 9) && toolType == 2) {
            this.mIsPnePressed = true;
        } else if (action == 10) {
            this.mIsPnePressed = false;
        }
        if (toolType != 1) {
            boolean z;
            if (toolType == 3) {
                z = true;
            } else {
                z = false;
            }
            this.mIsHoveredByMouse = z;
            return;
        }
        this.mIsHoveredByMouse = false;
        if (!this.mAlwaysDisableHoverHighlight && this.mSelector != null && this.mSelector.isStateful() && !this.mHoverAreaEnter) {
            if (action == 9) {
                if (!this.mIsPnePressed) {
                    this.mSelectorRect.setEmpty();
                }
            } else if (!isMultiWindows()) {
                if (this.mIsPnePressed || !isInDialog()) {
                    boolean isFingerAirView = Settings$System.getInt(this.mContext.getContentResolver(), Settings$System.FINGER_AIR_VIEW, 0) == 1;
                    boolean isFingerAirViewPreview = Settings$System.getInt(this.mContext.getContentResolver(), Settings$System.FINGER_AIR_VIEW_INFORMATION_PREVIEW, 0) == 1;
                    if (isFingerAirView && isFingerAirViewPreview) {
                        try {
                            if (!isHovered()) {
                                if (toolType == 1) {
                                    setFingerHovered(true);
                                }
                                setHovered(true);
                            }
                            int newHoverPosition = pointToPosition((int) event.getX(), (int) event.getY());
                            boolean bChanged = this.mHoverPosition != newHoverPosition;
                            boolean shouldShowSelector = shouldShowSelectorDefault();
                            if (newHoverPosition < 0) {
                                if (!shouldShowSelector) {
                                    this.mSelectorRect.setEmpty();
                                }
                                if (this.mHoveredOnEllipsizedText) {
                                    this.mSelector.setState(StateSet.NOTHING);
                                    postInvalidateOnAnimation();
                                    this.mHoveredOnEllipsizedText = false;
                                }
                                this.mHoverPosition = -1;
                                return;
                            }
                            this.mHoverPosition = newHoverPosition;
                            View child = getChildAt(this.mHoverPosition - this.mFirstPosition);
                            boolean foundEllipsizedTextView = findEllipsizedTextView(child);
                            boolean isSetFingerHovedInAppWidget = findSetFingerHovedInAppWidget(child);
                            if (child.isEnabled() && foundEllipsizedTextView && isSetFingerHovedInAppWidget) {
                                positionSelector(this.mHoverPosition, child);
                                this.mHoveredOnEllipsizedText = true;
                            } else if (!shouldShowSelector) {
                                this.mSelectorRect.setEmpty();
                            }
                            if (bChanged && this.mHoveredOnEllipsizedText) {
                                refreshDrawableState();
                                postInvalidateOnAnimation();
                            }
                            if (action == 10 && !shouldShowSelector) {
                                this.mHoveredOnEllipsizedText = false;
                                this.mHoverPosition = -1;
                                this.mSelector.setState(StateSet.NOTHING);
                                this.mSelectorRect.setEmpty();
                                postInvalidateOnAnimation();
                                return;
                            }
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    return;
                }
                this.mSelector.setState(StateSet.NOTHING);
                this.mSelectorRect.setEmpty();
            }
        }
    }

    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (this.mScrollX != scrollX) {
            onScrollChanged(scrollX, this.mScrollY, this.mScrollX, this.mScrollY);
            this.mScrollX = scrollX;
            invalidateParentIfNeeded();
            awakenScrollBars();
        }
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & 2) != 0) {
            switch (event.getAction()) {
                case 8:
                    if (this.mTouchMode == -1) {
                        float hscroll = event.getAxisValue(10);
                        if (hscroll != 0.0f) {
                            int delta = (int) (getHorizontalScrollFactor() * hscroll);
                            if (!trackMotionScroll(delta, delta)) {
                                return true;
                            }
                        }
                    }
                    break;
            }
        }
        return super.onGenericMotionEvent(event);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (this.mEdgeGlowLeft != null) {
            int restoreCount;
            int topPadding;
            int height;
            int edgeY;
            int edgeX;
            int scrollX = this.mScrollX;
            if (!this.mEdgeGlowLeft.isFinished()) {
                restoreCount = canvas.save();
                topPadding = this.mListPadding.top + this.mGlowPaddingTop;
                height = (getHeight() - topPadding) - (this.mListPadding.bottom + this.mGlowPaddingBottom);
                edgeY = (-height) + topPadding;
                edgeX = Math.min(0, this.mFirstPositionDistanceGuess + scrollX);
                canvas.rotate(270.0f);
                canvas.translate((float) edgeY, (float) edgeX);
                this.mEdgeGlowLeft.setSize(height, getWidth());
                if (this.mEdgeGlowLeft.draw(canvas)) {
                    canvas.restoreToCount(restoreCount);
                } else {
                    canvas.restoreToCount(restoreCount);
                }
            }
            if (!this.mEdgeGlowRight.isFinished()) {
                restoreCount = canvas.save();
                topPadding = this.mListPadding.top + this.mGlowPaddingTop;
                height = (getHeight() - topPadding) - (this.mListPadding.bottom + this.mGlowPaddingBottom);
                int width = getWidth();
                edgeY = -topPadding;
                edgeX = -Math.max(width, this.mLastPositionDistanceGuess + scrollX);
                canvas.rotate(90.0f);
                canvas.translate((float) edgeY, (float) edgeX);
                this.mEdgeGlowRight.setSize(height, width);
                if (this.mEdgeGlowRight.draw(canvas)) {
                    canvas.restoreToCount(restoreCount);
                } else {
                    canvas.restoreToCount(restoreCount);
                }
            }
        }
    }

    public void setOverScrollEffectPadding(int topPadding, int bottomPadding) {
        this.mGlowPaddingTop = topPadding;
        this.mGlowPaddingBottom = bottomPadding;
    }

    private void initOrResetVelocityTracker() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            this.mVelocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public boolean onInterceptHoverEvent(MotionEvent event) {
        if (this.mFastScroller == null || !this.mFastScroller.onInterceptHoverEvent(event)) {
            return super.onInterceptHoverEvent(event);
        }
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        if (!isAttachedToWindow()) {
            return false;
        }
        if (this.mFastScroller != null && this.mFastScroller.onInterceptTouchEvent(ev)) {
            return true;
        }
        int x;
        switch (action & 255) {
            case 0:
                int touchMode = this.mTouchMode;
                if (touchMode == 6 || touchMode == 5) {
                    this.mMotionCorrection = 0;
                    return true;
                }
                x = (int) ev.getX();
                int y = (int) ev.getY();
                this.mActivePointerId = ev.getPointerId(0);
                int motionPosition = findMotionRow(x);
                if (touchMode != 4 && motionPosition >= 0) {
                    this.mMotionViewOriginalLeft = getChildAt(motionPosition - this.mFirstPosition).getLeft();
                    this.mMotionX = x;
                    this.mMotionY = y;
                    this.mMotionPosition = motionPosition;
                    this.mTouchMode = 0;
                    clearScrollingCache();
                }
                this.mLastX = Integer.MIN_VALUE;
                initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(ev);
                if (touchMode == 4) {
                    return true;
                }
                return false;
            case 1:
            case 3:
                this.mTouchMode = -1;
                this.mActivePointerId = -1;
                recycleVelocityTracker();
                reportScrollStateChange(0);
                return false;
            case 2:
                switch (this.mTouchMode) {
                    case 0:
                        int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                        if (pointerIndex == -1) {
                            pointerIndex = 0;
                            this.mActivePointerId = ev.getPointerId(0);
                        }
                        x = (int) ev.getX(pointerIndex);
                        initVelocityTrackerIfNotExists();
                        this.mVelocityTracker.addMovement(ev);
                        if (startScrollIfNeeded(x, (int) ev.getY(pointerIndex), null)) {
                            return true;
                        }
                        return false;
                    default:
                        return false;
                }
            case 6:
                onSecondaryPointerUp(ev);
                return false;
            default:
                return false;
        }
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & 65280) >> 8;
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mMotionX = (int) ev.getX(newPointerIndex);
            this.mMotionY = (int) ev.getY(newPointerIndex);
            this.mMotionCorrection = 0;
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            this.mLastX = this.mMotionX;
        }
    }

    public void addTouchables(ArrayList<View> views) {
        int count = getChildCount();
        int firstPosition = this.mFirstPosition;
        ListAdapter adapter = this.mAdapter;
        if (adapter != null) {
            for (int i = 0; i < count; i++) {
                View child = getChildAt(i);
                if (adapter.isEnabled(firstPosition + i)) {
                    views.add(child);
                }
                child.addTouchables(views);
            }
        }
    }

    void reportScrollStateChange(int newState) {
        if (newState != this.mLastScrollState) {
            this.mLastScrollState = newState;
            if (this.mOnScrollListener != null && !this.mHoverAreaEnter) {
                this.mOnScrollListener.onScrollStateChanged(this, newState);
            }
        }
    }

    public void setFriction(float friction) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        }
        this.mFlingRunnable.mScroller.setFriction(friction);
    }

    public void setVelocityScale(float scale) {
        this.mVelocityScale = scale;
    }

    public void smoothScrollToPosition(int position) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = new PositionScroller();
        }
        this.mPositionScroller.start(position);
    }

    public void smoothScrollToPositionFromTop(int position, int offset, int duration) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = new PositionScroller();
        }
        this.mPositionScroller.startWithOffset(position, offset, duration);
    }

    public void smoothScrollToPositionFromTop(int position, int offset) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = new PositionScroller();
        }
        this.mPositionScroller.startWithOffset(position, offset);
    }

    public void smoothScrollToPosition(int position, int boundPosition) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = new PositionScroller();
        }
        this.mPositionScroller.start(position, boundPosition);
    }

    public void smoothScrollBy(int distance, int duration) {
        smoothScrollBy(distance, duration, false);
    }

    protected boolean isTwShowingScrollbar() {
        return super.isTwShowingScrollbar() && !this.mFastScrollEnabled;
    }

    protected int twGetItemCount() {
        Adapter adapter = getAdapter();
        return adapter == null ? 0 : adapter.getCount();
    }

    protected boolean isTwUsingAdapterView() {
        return true;
    }

    public void twSetSelection(int position) {
        setSelection(position);
    }

    public void twSmoothScrollBy(int distance) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        }
        if (this.mTwTwScrollRemains == null) {
            this.mTwTwScrollRemains = new LinkedList();
            this.mTwSmoothScrollByMove = new TwSmoothScrollByMove();
        }
        synchronized (this.mTwTwScrollRemains) {
            boolean isEmpty = this.mTwTwScrollRemains.isEmpty();
            if (Math.abs(distance) > 500) {
                if (distance > 0) {
                    while (distance > 500) {
                        this.mTwTwScrollRemains.offer(Integer.valueOf(500));
                        distance -= 500;
                    }
                } else {
                    while (distance < -500) {
                        this.mTwTwScrollRemains.offer(Integer.valueOf(-500));
                        distance += 500;
                    }
                }
            }
            this.mTwTwScrollRemains.offer(Integer.valueOf(distance));
            if (isEmpty) {
                post(this.mTwSmoothScrollByMove);
            }
        }
    }

    void smoothScrollBy(int distance, int duration, boolean linear) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        }
        int firstPos = this.mFirstPosition;
        int childCount = getChildCount();
        int lastPos = firstPos + childCount;
        int leftLimit = getPaddingLeft();
        int rightLimit = getWidth() - getPaddingRight();
        if (distance == 0 || this.mItemCount == 0 || childCount == 0 || ((firstPos == 0 && getChildAt(0).getLeft() == leftLimit && distance < 0) || (lastPos == this.mItemCount && getChildAt(childCount - 1).getRight() == rightLimit && distance > 0))) {
            this.mFlingRunnable.endFling();
            if (this.mPositionScroller != null) {
                this.mPositionScroller.stop();
                return;
            }
            return;
        }
        reportScrollStateChange(2);
        this.mFlingRunnable.startScroll(distance, duration, linear);
    }

    void smoothScrollByOffset(int position) {
        int index = -1;
        if (position < 0) {
            index = getFirstVisiblePosition();
        } else if (position > 0) {
            index = getLastVisiblePosition();
        }
        if (index > -1) {
            View child = getChildAt(index - getFirstVisiblePosition());
            if (child != null) {
                Rect visibleRect = new Rect();
                if (child.getGlobalVisibleRect(visibleRect)) {
                    float visibleArea = ((float) (visibleRect.width() * visibleRect.height())) / ((float) (child.getWidth() * child.getHeight()));
                    if (position < 0 && visibleArea < 0.75f) {
                        index++;
                    } else if (position > 0 && visibleArea < 0.75f) {
                        index--;
                    }
                }
                smoothScrollToPosition(Math.max(0, Math.min(getCount(), index + position)));
            }
        }
    }

    private void createScrollingCache() {
        if (this.mScrollingCacheEnabled && !this.mCachingStarted && !isHardwareAccelerated()) {
            setChildrenDrawnWithCacheEnabled(true);
            setChildrenDrawingCacheEnabled(true);
            this.mCachingActive = true;
            this.mCachingStarted = true;
        }
    }

    private void clearScrollingCache() {
        if (!isHardwareAccelerated()) {
            if (this.mClearScrollingCache == null) {
                this.mClearScrollingCache = new Runnable() {
                    public void run() {
                        if (AbsHorizontalListView.this.mCachingStarted) {
                            AbsHorizontalListView absHorizontalListView = AbsHorizontalListView.this;
                            AbsHorizontalListView.this.mCachingActive = false;
                            absHorizontalListView.mCachingStarted = false;
                            AbsHorizontalListView.this.setChildrenDrawnWithCacheEnabled(false);
                            if ((AbsHorizontalListView.this.mPersistentDrawingCache & 2) == 0) {
                                AbsHorizontalListView.this.setChildrenDrawingCacheEnabled(false);
                            }
                            if (!AbsHorizontalListView.this.isAlwaysDrawnWithCacheEnabled()) {
                                AbsHorizontalListView.this.invalidate();
                            }
                        }
                    }
                };
            }
            post(this.mClearScrollingCache);
        }
    }

    public void scrollListBy(int y) {
        trackMotionScroll(-y, -y);
    }

    public boolean canScrollList(int direction) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return false;
        }
        int firstPosition = this.mFirstPosition;
        Rect listPadding = this.mListPadding;
        if (direction > 0) {
            int lastBottom = getChildAt(childCount - 1).getBottom();
            if (firstPosition + childCount < this.mItemCount || lastBottom > getHeight() - listPadding.bottom) {
                return true;
            }
            return false;
        }
        int firstTop = getChildAt(0).getTop();
        if (firstPosition > 0 || firstTop < listPadding.top) {
            return true;
        }
        return false;
    }

    boolean trackMotionScroll(int deltaX, int incrementalDeltaX) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return true;
        }
        int firstLeft = getChildAt(0).getLeft();
        int lastRight = getChildAt(childCount - 1).getRight();
        Rect listPadding = this.mListPadding;
        int effectivePaddingLeft = 0;
        int effectivePaddingRight = 0;
        if ((this.mGroupFlags & 34) == 34) {
            effectivePaddingLeft = listPadding.left;
            effectivePaddingRight = listPadding.right;
        }
        int spaceAbove = effectivePaddingLeft - firstLeft;
        int spaceBelow = lastRight - (getWidth() - effectivePaddingRight);
        int width = (getWidth() - this.mPaddingRight) - this.mPaddingLeft;
        if (deltaX < 0) {
            deltaX = Math.max(-(width - 1), deltaX);
        } else {
            deltaX = Math.min(width - 1, deltaX);
        }
        if (incrementalDeltaX < 0) {
            incrementalDeltaX = Math.max(-(width - 1), incrementalDeltaX);
        } else {
            incrementalDeltaX = Math.min(width - 1, incrementalDeltaX);
        }
        int firstPosition = this.mFirstPosition;
        if (firstPosition == 0) {
            this.mFirstPositionDistanceGuess = firstLeft - listPadding.left;
        } else {
            this.mFirstPositionDistanceGuess += incrementalDeltaX;
        }
        if (firstPosition + childCount == this.mItemCount) {
            this.mLastPositionDistanceGuess = listPadding.right + lastRight;
        } else {
            this.mLastPositionDistanceGuess += incrementalDeltaX;
        }
        boolean cannotScrollRight = firstPosition == 0 && firstLeft >= listPadding.left && incrementalDeltaX >= 0;
        boolean cannotScrollLeft = firstPosition + childCount == this.mItemCount && lastRight <= getWidth() - listPadding.right && incrementalDeltaX <= 0;
        if (!cannotScrollRight && !cannotScrollLeft) {
            boolean rightSide = incrementalDeltaX < 0;
            boolean inTouchMode = isInTouchMode();
            if (inTouchMode) {
                hideSelector();
            }
            int headerViewsCount = getHeaderViewsCount();
            int footerViewsStart = this.mItemCount - getFooterViewsCount();
            int start = 0;
            int count = 0;
            int i;
            View child;
            int position;
            if (!rightSide) {
                int right = getWidth() - incrementalDeltaX;
                if ((this.mGroupFlags & 34) == 34) {
                    right -= listPadding.right;
                }
                for (i = childCount - 1; i >= 0; i--) {
                    child = getChildAt(i);
                    if (child.getLeft() <= right) {
                        break;
                    }
                    start = i;
                    count++;
                    position = firstPosition + i;
                    if (position >= headerViewsCount && position < footerViewsStart) {
                        if (child.isAccessibilityFocused()) {
                            child.clearAccessibilityFocus();
                        }
                        if (this.USE_SET_INTEGRATOR_HAPTIC && this.mLastPosition != position) {
                            this.mLastPosition = position;
                        }
                        this.mRecycler.addScrapView(child, position);
                    }
                }
            } else {
                int left = -incrementalDeltaX;
                if ((this.mGroupFlags & 34) == 34) {
                    left += listPadding.left;
                }
                for (i = 0; i < childCount; i++) {
                    child = getChildAt(i);
                    if (child.getRight() >= left) {
                        break;
                    }
                    count++;
                    position = firstPosition + i;
                    if (position >= headerViewsCount && position < footerViewsStart) {
                        if (child.isAccessibilityFocused()) {
                            child.clearAccessibilityFocus();
                        }
                        if (this.USE_SET_INTEGRATOR_HAPTIC && this.mLastPosition != position) {
                            this.mLastPosition = position;
                        }
                        this.mRecycler.addScrapView(child, position);
                    }
                }
            }
            this.mMotionViewNewLeft = this.mMotionViewOriginalLeft + deltaX;
            this.mBlockLayoutRequests = true;
            if (count > 0) {
                detachViewsFromParent(start, count);
                this.mRecycler.removeSkippedScrap();
            }
            if (!awakenScrollBars()) {
                invalidate();
            }
            twOffsetChildrenLeftAndRight(incrementalDeltaX);
            if (rightSide) {
                this.mFirstPosition += count;
            }
            int absIncrementalDeltaX = Math.abs(incrementalDeltaX);
            if (spaceAbove < absIncrementalDeltaX || spaceBelow < absIncrementalDeltaX) {
                fillGap(rightSide);
            }
            int childIndex;
            if (!inTouchMode && this.mSelectedPosition != -1) {
                childIndex = this.mSelectedPosition - this.mFirstPosition;
                if (childIndex >= 0 && childIndex < getChildCount()) {
                    positionSelector(this.mSelectedPosition, getChildAt(childIndex));
                }
            } else if (this.mSelectorPosition != -1) {
                childIndex = this.mSelectorPosition - this.mFirstPosition;
                if (childIndex >= 0 && childIndex < getChildCount()) {
                    positionSelector(-1, getChildAt(childIndex));
                }
            } else {
                this.mSelectorRect.setEmpty();
            }
            this.mBlockLayoutRequests = false;
            invokeOnItemScrollListener();
            return false;
        } else if (incrementalDeltaX != 0) {
            return true;
        } else {
            return false;
        }
    }

    int getHeaderViewsCount() {
        return 0;
    }

    int getFooterViewsCount() {
        return 0;
    }

    void hideSelector() {
        if (this.mSelectedPosition != -1) {
            if (this.mLayoutMode != 4) {
                this.mResurrectToPosition = this.mSelectedPosition;
            }
            if (this.mNextSelectedPosition >= 0 && this.mNextSelectedPosition != this.mSelectedPosition) {
                this.mResurrectToPosition = this.mNextSelectedPosition;
            }
            setSelectedPositionInt(-1);
            setNextSelectedPositionInt(-1);
            this.mSelectedLeft = 0;
        }
    }

    int reconcileSelectedPosition() {
        int position = this.mSelectedPosition;
        if (position < 0) {
            position = this.mResurrectToPosition;
        }
        return Math.min(Math.max(0, position), this.mItemCount - 1);
    }

    int findClosestMotionRow(int x) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return -1;
        }
        int motionRow = findMotionRow(x);
        return motionRow == -1 ? (this.mFirstPosition + childCount) - 1 : motionRow;
    }

    public void invalidateViews() {
        this.mDataChanged = true;
        rememberSyncState();
        requestLayout();
        invalidate();
    }

    boolean resurrectSelectionIfNeeded() {
        if (this.mSelectedPosition >= 0 || !resurrectSelection()) {
            return false;
        }
        updateSelectorState();
        return true;
    }

    boolean resurrectSelection() {
        int childCount = getChildCount();
        if (childCount <= 0) {
            return false;
        }
        int selectedPos;
        int selectedLeft = 0;
        int childrenLeft = this.mListPadding.left;
        int childrenRight = (this.mRight - this.mLeft) - this.mListPadding.right;
        int firstPosition = this.mFirstPosition;
        int toPosition = this.mResurrectToPosition;
        boolean rightSide = true;
        if (toPosition >= firstPosition && toPosition < firstPosition + childCount) {
            selectedPos = toPosition;
            View selected = getChildAt(selectedPos - this.mFirstPosition);
            selectedLeft = selected.getLeft();
            int selectedRight = selected.getRight();
            if (selectedLeft < childrenLeft) {
                selectedLeft = childrenLeft + getHorizontalFadingEdgeLength();
            } else if (selectedRight > childrenRight) {
                selectedLeft = (childrenRight - selected.getMeasuredWidth()) - getHorizontalFadingEdgeLength();
            }
        } else if (toPosition < firstPosition) {
            selectedPos = firstPosition;
            for (i = 0; i < childCount; i++) {
                left = getChildAt(i).getLeft();
                if (i == 0) {
                    selectedLeft = left;
                    if (firstPosition > 0 || left < childrenLeft) {
                        childrenLeft += getHorizontalFadingEdgeLength();
                    }
                }
                if (left >= childrenLeft) {
                    selectedPos = firstPosition + i;
                    selectedLeft = left;
                    break;
                }
            }
        } else {
            int itemCount = this.mItemCount;
            rightSide = false;
            selectedPos = (firstPosition + childCount) - 1;
            for (i = childCount - 1; i >= 0; i--) {
                View v = getChildAt(i);
                left = v.getLeft();
                int right = v.getRight();
                if (i == childCount - 1) {
                    selectedLeft = left;
                    if (firstPosition + childCount < itemCount || right > childrenRight) {
                        childrenRight -= getHorizontalFadingEdgeLength();
                    }
                }
                if (right <= childrenRight) {
                    selectedPos = firstPosition + i;
                    selectedLeft = left;
                    break;
                }
            }
        }
        this.mResurrectToPosition = -1;
        removeCallbacks(this.mFlingRunnable);
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        this.mTouchMode = -1;
        clearScrollingCache();
        this.mSpecificTop = selectedLeft;
        selectedPos = lookForSelectablePosition(selectedPos, rightSide);
        if (selectedPos < firstPosition || selectedPos > getLastVisiblePosition()) {
            selectedPos = -1;
        } else {
            this.mLayoutMode = 4;
            updateSelectorState();
            setSelectionInt(selectedPos);
            invokeOnItemScrollListener();
        }
        reportScrollStateChange(0);
        if (selectedPos >= 0) {
            return true;
        }
        return false;
    }

    void confirmCheckedPositionsById() {
        this.mCheckStates.clear();
        boolean checkedCountChanged = false;
        int checkedIndex = 0;
        while (checkedIndex < this.mCheckedIdStates.size()) {
            long id = this.mCheckedIdStates.keyAt(checkedIndex);
            int lastPos = ((Integer) this.mCheckedIdStates.valueAt(checkedIndex)).intValue();
            if (id != this.mAdapter.getItemId(lastPos)) {
                int start = Math.max(0, lastPos - 20);
                int end = Math.min(lastPos + 20, this.mItemCount);
                boolean found = false;
                for (int searchPos = start; searchPos < end; searchPos++) {
                    if (id == this.mAdapter.getItemId(searchPos)) {
                        found = true;
                        this.mCheckStates.put(searchPos, true);
                        this.mCheckedIdStates.setValueAt(checkedIndex, Integer.valueOf(searchPos));
                        break;
                    }
                }
                if (!found) {
                    this.mCheckedIdStates.delete(id);
                    checkedIndex--;
                    this.mCheckedItemCount--;
                    checkedCountChanged = true;
                    if (!(this.mChoiceActionMode == null || this.mMultiChoiceModeCallback == null)) {
                        this.mMultiChoiceModeCallback.onItemCheckedStateChanged(this.mChoiceActionMode, lastPos, id, false);
                    }
                }
            } else {
                this.mCheckStates.put(lastPos, true);
            }
            checkedIndex++;
        }
        if (checkedCountChanged && this.mChoiceActionMode != null) {
            this.mChoiceActionMode.invalidate();
        }
    }

    protected void handleDataChanged() {
        int i = 3;
        int count = this.mItemCount;
        int lastHandledItemCount = this.mLastHandledItemCount;
        this.mLastHandledItemCount = this.mItemCount;
        if (!(!this.mIsMultiFocusEnabled || this.mAdapter == null || this.mItemCount == this.mOldAdapterItemCount)) {
            this.mTwPressItemListArray = new int[this.mItemCount];
            resetPressItemListArray();
            this.mOldAdapterItemCount = this.mItemCount;
        }
        if (!(this.mChoiceMode == 0 || this.mAdapter == null || !this.mAdapter.hasStableIds())) {
            confirmCheckedPositionsById();
        }
        this.mRecycler.clearTransientStateViews();
        if (count > 0) {
            int newPos;
            if (this.mNeedSync) {
                this.mNeedSync = false;
                this.mPendingSync = null;
                if (this.mTranscriptMode == 2) {
                    this.mLayoutMode = 3;
                    return;
                }
                if (this.mTranscriptMode == 1) {
                    if (this.mForceTranscriptScroll) {
                        this.mForceTranscriptScroll = false;
                        this.mLayoutMode = 3;
                        return;
                    }
                    int childCount = getChildCount();
                    int listRight = getWidth() - getPaddingRight();
                    View lastChild = getChildAt(childCount - 1);
                    int lastRight;
                    if (lastChild != null) {
                        lastRight = lastChild.getRight();
                    } else {
                        lastRight = listRight;
                    }
                    if (this.mFirstPosition + childCount < lastHandledItemCount || lastRight > listRight) {
                        awakenScrollBars();
                    } else {
                        this.mLayoutMode = 3;
                        return;
                    }
                }
                switch (this.mSyncMode) {
                    case 0:
                        if (isInTouchMode()) {
                            this.mLayoutMode = 5;
                            this.mSyncPosition = Math.min(Math.max(0, this.mSyncPosition), count - 1);
                            return;
                        }
                        newPos = findSyncPosition();
                        if (newPos >= 0 && lookForSelectablePosition(newPos, true) == newPos) {
                            this.mSyncPosition = newPos;
                            if (this.mSyncHeight == ((long) getWidth())) {
                                this.mLayoutMode = 5;
                            } else {
                                this.mLayoutMode = 2;
                            }
                            setNextSelectedPositionInt(newPos);
                            return;
                        }
                    case 1:
                        this.mLayoutMode = 5;
                        this.mSyncPosition = Math.min(Math.max(0, this.mSyncPosition), count - 1);
                        return;
                }
            }
            if (!isInTouchMode()) {
                newPos = getSelectedItemPosition();
                if (newPos >= count) {
                    newPos = count - 1;
                }
                if (newPos < 0) {
                    newPos = 0;
                }
                int selectablePos = lookForSelectablePosition(newPos, true);
                if (selectablePos >= 0) {
                    setNextSelectedPositionInt(selectablePos);
                    return;
                }
                selectablePos = lookForSelectablePosition(newPos, false);
                if (selectablePos >= 0) {
                    setNextSelectedPositionInt(selectablePos);
                    return;
                }
            } else if (this.mResurrectToPosition >= 0) {
                return;
            }
        }
        if (!this.mStackFromBottom) {
            i = 1;
        }
        this.mLayoutMode = i;
        this.mSelectedPosition = -1;
        this.mSelectedRowId = Long.MIN_VALUE;
        this.mNextSelectedPosition = -1;
        this.mNextSelectedRowId = Long.MIN_VALUE;
        this.mNeedSync = false;
        this.mPendingSync = null;
        this.mSelectorPosition = -1;
        checkSelectionChanged();
    }

    protected void onDisplayHint(int hint) {
        super.onDisplayHint(hint);
        switch (hint) {
            case 0:
                if (!(!this.mFiltered || this.mPopup == null || this.mPopup.isShowing())) {
                    showPopup();
                    break;
                }
            case 4:
                if (this.mPopup != null && this.mPopup.isShowing()) {
                    dismissPopup();
                    break;
                }
        }
        this.mPopupHidden = hint == 4;
    }

    private void dismissPopup() {
        if (this.mPopup != null) {
            this.mPopup.dismiss();
        }
    }

    private void showPopup() {
        if (getWindowVisibility() == 0) {
            createTextFilter(true);
            positionPopup();
            checkFocus();
        }
    }

    private void positionPopup() {
        int screenHeight = getResources().getDisplayMetrics().heightPixels;
        int[] xy = new int[2];
        getLocationOnScreen(xy);
        int bottomGap = ((screenHeight - xy[1]) - getHeight()) + ((int) (this.mDensityScale * 20.0f));
        if (this.mPopup.isShowing()) {
            this.mPopup.update(xy[0], bottomGap, -1, -1);
        } else {
            this.mPopup.showAtLocation((View) this, 81, xy[0], bottomGap);
        }
    }

    static int getDistance(Rect source, Rect dest, int direction) {
        int sX;
        int sY;
        int dX;
        int dY;
        switch (direction) {
            case 1:
            case 2:
                sX = source.right + (source.width() / 2);
                sY = source.top + (source.height() / 2);
                dX = dest.left + (dest.width() / 2);
                dY = dest.top + (dest.height() / 2);
                break;
            case 17:
                sX = source.left;
                sY = source.top + (source.height() / 2);
                dX = dest.right;
                dY = dest.top + (dest.height() / 2);
                break;
            case 33:
                sX = source.left + (source.width() / 2);
                sY = source.top;
                dX = dest.left + (dest.width() / 2);
                dY = dest.bottom;
                break;
            case 66:
                sX = source.right;
                sY = source.top + (source.height() / 2);
                dX = dest.left;
                dY = dest.top + (dest.height() / 2);
                break;
            case 130:
                sX = source.left + (source.width() / 2);
                sY = source.bottom;
                dX = dest.left + (dest.width() / 2);
                dY = dest.top;
                break;
            default:
                throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT, FOCUS_FORWARD, FOCUS_BACKWARD}.");
        }
        int deltaX = dX - sX;
        int deltaY = dY - sY;
        return (deltaY * deltaY) + (deltaX * deltaX);
    }

    protected boolean isInFilterMode() {
        return this.mFiltered;
    }

    boolean sendToTextFilter(int keyCode, int count, KeyEvent event) {
        if (!acceptFilter()) {
            return false;
        }
        boolean handled = false;
        boolean okToSend = true;
        switch (keyCode) {
            case 4:
                if (this.mFiltered && this.mPopup != null && this.mPopup.isShowing()) {
                    if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                        DispatcherState state = getKeyDispatcherState();
                        if (state != null) {
                            state.startTracking(event, this);
                        }
                        handled = true;
                    } else if (event.getAction() == 1 && event.isTracking() && !event.isCanceled()) {
                        handled = true;
                        this.mTextFilter.setText((CharSequence) "");
                    }
                }
                okToSend = false;
                break;
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 66:
                okToSend = false;
                break;
            case 62:
                okToSend = this.mFiltered;
                break;
        }
        if (!okToSend) {
            return handled;
        }
        createTextFilter(true);
        KeyEvent forwardEvent = event;
        if (forwardEvent.getRepeatCount() > 0) {
            forwardEvent = KeyEvent.changeTimeRepeat(event, event.getEventTime(), 0);
        }
        switch (event.getAction()) {
            case 0:
                handled = this.mTextFilter.onKeyDown(keyCode, forwardEvent);
                if (keyCode == 59 || keyCode == 60) {
                    this.mIsShiftkeyPressed = true;
                    return handled;
                } else if (keyCode != 113 && keyCode != 114) {
                    return handled;
                } else {
                    this.mIsCtrlkeyPressed = true;
                    return handled;
                }
            case 1:
                handled = this.mTextFilter.onKeyUp(keyCode, forwardEvent);
                if (keyCode == 59 || keyCode == 60) {
                    this.mIsShiftkeyPressed = false;
                    this.mOldKeyCode = 0;
                    this.mCurrentKeyCode = 0;
                    this.mFirstPressedPoint = -1;
                    this.mSecondPressedPoint = -1;
                    return handled;
                } else if (keyCode != 113 && keyCode != 114) {
                    return handled;
                } else {
                    this.mIsCtrlkeyPressed = false;
                    return handled;
                }
            case 2:
                return this.mTextFilter.onKeyMultiple(keyCode, count, event);
            default:
                return handled;
        }
    }

    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        if (!isTextFilterEnabled()) {
            return null;
        }
        createTextFilter(false);
        if (this.mPublicInputConnection == null) {
            this.mDefInputConnection = new BaseInputConnection((View) this, false);
            this.mPublicInputConnection = new InputConnectionWrapper(this.mTextFilter.onCreateInputConnection(outAttrs), true) {
                public boolean reportFullscreenMode(boolean enabled) {
                    return AbsHorizontalListView.this.mDefInputConnection.reportFullscreenMode(enabled);
                }

                public boolean performEditorAction(int editorAction) {
                    if (editorAction != 6) {
                        return false;
                    }
                    InputMethodManager imm = (InputMethodManager) AbsHorizontalListView.this.getContext().getSystemService("input_method");
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(AbsHorizontalListView.this.getWindowToken(), 0);
                    }
                    return true;
                }

                public boolean sendKeyEvent(KeyEvent event) {
                    return AbsHorizontalListView.this.mDefInputConnection.sendKeyEvent(event);
                }
            };
        }
        outAttrs.inputType = 177;
        outAttrs.imeOptions = 6;
        return this.mPublicInputConnection;
    }

    public boolean checkInputConnectionProxy(View view) {
        return view == this.mTextFilter;
    }

    private void createTextFilter(boolean animateEntrance) {
        if (this.mPopup == null) {
            PopupWindow p = new PopupWindow(getContext());
            p.setFocusable(false);
            p.setTouchable(false);
            p.setInputMethodMode(2);
            p.setContentView(getTextFilterInput());
            p.setWidth(-2);
            p.setHeight(-2);
            p.setBackgroundDrawable(null);
            this.mPopup = p;
            getViewTreeObserver().addOnGlobalLayoutListener(this);
            this.mGlobalLayoutListenerAddedFilter = true;
        }
        if (animateEntrance) {
            this.mPopup.setAnimationStyle(R.style.Animation_TypingFilter);
        } else {
            this.mPopup.setAnimationStyle(R.style.Animation_TypingFilterRestore);
        }
    }

    private EditText getTextFilterInput() {
        if (this.mTextFilter == null) {
            this.mTextFilter = (EditText) LayoutInflater.from(getContext()).inflate((int) R.layout.typing_filter, null);
            this.mTextFilter.setRawInputType(177);
            this.mTextFilter.setImeOptions(268435456);
            this.mTextFilter.addTextChangedListener(this);
        }
        return this.mTextFilter;
    }

    public void clearTextFilter() {
        if (this.mFiltered) {
            getTextFilterInput().setText((CharSequence) "");
            this.mFiltered = false;
            if (this.mPopup != null && this.mPopup.isShowing()) {
                dismissPopup();
            }
        }
    }

    public boolean hasTextFilter() {
        return this.mFiltered;
    }

    public void onGlobalLayout() {
        if (isShown()) {
            if (this.mFiltered && this.mPopup != null && !this.mPopup.isShowing() && !this.mPopupHidden) {
                showPopup();
            }
        } else if (this.mPopup != null && this.mPopup.isShowing()) {
            dismissPopup();
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (isTextFilterEnabled()) {
            createTextFilter(true);
            int length = s.length();
            boolean showing = this.mPopup.isShowing();
            if (!showing && length > 0) {
                showPopup();
                this.mFiltered = true;
            } else if (showing && length == 0) {
                dismissPopup();
                this.mFiltered = false;
            }
            if (this.mAdapter instanceof Filterable) {
                Filter f = ((Filterable) this.mAdapter).getFilter();
                if (f != null) {
                    f.filter(s, this);
                    return;
                }
                throw new IllegalStateException("You cannot call onTextChanged with a non filterable adapter");
            }
        }
    }

    public void afterTextChanged(Editable s) {
    }

    public void onFilterComplete(int count) {
        if (this.mSelectedPosition < 0 && count > 0) {
            this.mResurrectToPosition = -1;
            resurrectSelection();
        }
    }

    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2, 0);
    }

    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void setTranscriptMode(int mode) {
        this.mTranscriptMode = mode;
    }

    public int getTranscriptMode() {
        return this.mTranscriptMode;
    }

    public int getSolidColor() {
        return this.mCacheColorHint;
    }

    public void setCacheColorHint(int color) {
        if (color != this.mCacheColorHint) {
            this.mCacheColorHint = color;
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                getChildAt(i).setDrawingCacheBackgroundColor(color);
            }
            this.mRecycler.setCacheColorHint(color);
        }
    }

    @ExportedProperty(category = "drawing")
    public int getCacheColorHint() {
        return this.mCacheColorHint;
    }

    public void reclaimViews(List<View> views) {
        int childCount = getChildCount();
        RecyclerListener listener = this.mRecycler.mRecyclerListener;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp != null && this.mRecycler.shouldRecycleViewType(lp.viewType)) {
                views.add(child);
                child.setAccessibilityDelegate(null);
                if (listener != null) {
                    listener.onMovedToScrapHeap(child);
                }
            }
        }
        this.mRecycler.reclaimScrapViews(views);
        removeAllViewsInLayout();
    }

    private void finishGlows() {
        if (this.mEdgeGlowLeft != null) {
            this.mEdgeGlowLeft.finish();
            this.mEdgeGlowRight.finish();
        }
    }

    public void setRemoteViewsAdapter(Intent intent) {
        if (this.mRemoteAdapter == null || !new FilterComparison(intent).equals(new FilterComparison(this.mRemoteAdapter.getRemoteViewsServiceIntent()))) {
            this.mDeferNotifyDataSetChanged = false;
            this.mRemoteAdapter = new RemoteViewsAdapter(getContext(), intent, this);
            if (this.mRemoteAdapter.isDataReady()) {
                setAdapter(this.mRemoteAdapter);
            }
        }
    }

    public void setRemoteViewsOnClickHandler(OnClickHandler handler) {
        if (this.mRemoteAdapter != null) {
            this.mRemoteAdapter.setRemoteViewsOnClickHandler(handler);
        }
    }

    public void deferNotifyDataSetChanged() {
        this.mDeferNotifyDataSetChanged = true;
    }

    public boolean onRemoteAdapterConnected() {
        if (this.mRemoteAdapter != this.mAdapter) {
            setAdapter(this.mRemoteAdapter);
            if (!this.mDeferNotifyDataSetChanged) {
                return false;
            }
            this.mRemoteAdapter.notifyDataSetChanged();
            this.mDeferNotifyDataSetChanged = false;
            return false;
        } else if (this.mRemoteAdapter == null) {
            return false;
        } else {
            this.mRemoteAdapter.superNotifyDataSetChanged();
            return true;
        }
    }

    public void onRemoteAdapterDisconnected() {
    }

    void setVisibleRangeHint(int start, int end) {
        if (this.mRemoteAdapter != null) {
            this.mRemoteAdapter.setVisibleRangeHint(start, end);
        }
    }

    public void setRecyclerListener(RecyclerListener listener) {
        this.mRecycler.mRecyclerListener = listener;
    }

    static View retrieveFromScrap(ArrayList<View> scrapViews, int position) {
        int size = scrapViews.size();
        if (size <= 0) {
            return null;
        }
        for (int i = 0; i < size; i++) {
            View view = (View) scrapViews.get(i);
            if (((LayoutParams) view.getLayoutParams()).scrappedFromPosition == position) {
                scrapViews.remove(i);
                return view;
            }
        }
        return (View) scrapViews.remove(size - 1);
    }

    private void registerMotionListener() {
        log("[registerDoubleTapMotionListener]");
        if (this.mMotionRecognitionManager != null) {
            this.mMotionRecognitionManager.registerListenerEvent(this.mMotionListener, 8);
        }
    }

    private void unregisterMotionListener() {
        log("[unregisterDoubleTapMotionListener]");
        if (this.mMotionRecognitionManager != null) {
            this.mMotionRecognitionManager.unregisterListener(this.mMotionListener);
        }
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        this.mHoverPosition = -1;
        if (visibility != 0) {
            releaseAllBoosters();
        }
    }

    protected void finalize() throws Throwable {
        try {
            unregisterMotionListener();
        } finally {
            super.finalize();
        }
    }

    private static void log(String log) {
        Log.d(TAG, log);
    }

    public void setEnableMultiFocus(boolean enable) {
        this.mIsMultiFocusEnabled = enable;
    }

    public boolean isMultiFocusEnabled() {
        return this.mIsMultiFocusEnabled;
    }

    public void addToPressItemListArray(int firstpoint, int secondpoint) {
        boolean isSameValueExist = false;
        if (this.mIsMultiFocusEnabled) {
            int i;
            int j;
            if (secondpoint == -1) {
                for (i = 0; i < this.mTwPressItemListIndex; i++) {
                    if (this.mTwPressItemListArray[i] == firstpoint) {
                        isSameValueExist = true;
                        for (j = i; j < this.mTwPressItemListIndex - 1; j++) {
                            this.mTwPressItemListArray[j] = this.mTwPressItemListArray[j + 1];
                        }
                        this.mTwPressItemListIndex--;
                    }
                }
                if (!isSameValueExist) {
                    this.mTwPressItemListArray[this.mTwPressItemListIndex] = firstpoint;
                    this.mTwPressItemListIndex++;
                }
            } else if (firstpoint < secondpoint) {
                checkCount = (secondpoint - firstpoint) + 1;
                for (i = 0; i < checkCount; i++) {
                    isSameValueExist = false;
                    for (j = 0; j < this.mTwPressItemListIndex; j++) {
                        if (this.mTwPressItemListArray[j] == firstpoint) {
                            isSameValueExist = true;
                        }
                    }
                    if (!isSameValueExist) {
                        this.mTwPressItemListArray[this.mTwPressItemListIndex] = firstpoint;
                        this.mTwPressItemListIndex++;
                    }
                    firstpoint++;
                }
            } else if (firstpoint > secondpoint) {
                checkCount = (firstpoint - secondpoint) + 1;
                for (i = 0; i < checkCount; i++) {
                    isSameValueExist = false;
                    for (j = 0; j < this.mTwPressItemListIndex; j++) {
                        if (this.mTwPressItemListArray[j] == firstpoint) {
                            isSameValueExist = true;
                        }
                    }
                    if (!isSameValueExist) {
                        this.mTwPressItemListArray[this.mTwPressItemListIndex] = firstpoint;
                        this.mTwPressItemListIndex++;
                    }
                    firstpoint--;
                }
            } else {
                for (i = 0; i < this.mTwPressItemListIndex; i++) {
                    if (this.mTwPressItemListArray[i] == firstpoint) {
                        isSameValueExist = true;
                    }
                }
                if (!isSameValueExist) {
                    this.mTwPressItemListArray[this.mTwPressItemListIndex] = firstpoint;
                    this.mTwPressItemListIndex++;
                }
            }
            invalidate();
        }
    }

    public void resetPressItemListArray() {
        if (this.mAdapter != null) {
            int checkCount = this.mAdapter.getCount();
            if (this.mTwPressItemListArray != null) {
                for (int i = 0; i < checkCount; i++) {
                    this.mTwPressItemListArray[i] = -1;
                }
                this.mTwPressItemListIndex = 0;
                invalidate();
            }
        }
    }

    public void setMultiFocusListItem(int startitem, int enditem) {
        if (this.mTwPressItemListArray != null) {
            resetPressItemListArray();
            addToPressItemListArray(startitem, enditem);
        }
    }
}
