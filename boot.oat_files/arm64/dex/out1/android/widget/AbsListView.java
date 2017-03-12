package android.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
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
import android.preference.PreferenceGroupAdapter;
import android.provider.Settings$System;
import android.text.Editable;
import android.text.MultiSelection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
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
import android.view.ViewHierarchyEncoder;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.CorrectionInfo;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Filter.FilterListener;
import android.widget.RemoteViews.OnClickHandler;
import android.widget.RemoteViewsAdapter.RemoteAdapterConnectionCallback;
import com.android.internal.R;
import com.samsung.android.feature.FloatingFeature;
import com.samsung.android.motion.MotionRecognitionManager;
import com.samsung.android.smartface.SmartFaceManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class AbsListView extends AdapterView<ListAdapter> implements TextWatcher, OnGlobalLayoutListener, FilterListener, OnTouchModeChangeListener, RemoteAdapterConnectionCallback {
    private static final int CHECK_POSITION_SEARCH_DISTANCE = 20;
    public static final int CHOICE_MODE_MULTIPLE = 2;
    public static final int CHOICE_MODE_MULTIPLE_MODAL = 3;
    public static final int CHOICE_MODE_NONE = 0;
    public static final int CHOICE_MODE_SINGLE = 1;
    private static final int DRAGSCROLL_WORKING_ZONE_DP = 25;
    private static final int HOVERSCROLL_DOWN = 2;
    private static final int HOVERSCROLL_HEIGHT_BOTTOM_DP = 25;
    private static final int HOVERSCROLL_HEIGHT_TOP_DP = 25;
    private static final float HOVERSCROLL_SPEED_FASTER = 3000.0f;
    private static final int HOVERSCROLL_UP = 1;
    private static final int INVALID_POINTER = -1;
    static final int LAYOUT_FORCE_BOTTOM = 3;
    static final int LAYOUT_FORCE_TOP = 1;
    static final int LAYOUT_MOVE_SELECTION = 6;
    static final int LAYOUT_NORMAL = 0;
    static final int LAYOUT_SET_SELECTION = 2;
    static final int LAYOUT_SPECIFIC = 4;
    static final int LAYOUT_SYNC = 5;
    private static final int MSG_HOVERSCROLL_MOVE = 1;
    private static final int MSG_HOVERSCROLL_MOVE_FASTER = 2;
    private static final int MSG_HOVERSCROLL_MOVE_TO_END = 3;
    private static final int MSG_QC_HIDE = 0;
    static final int OVERSCROLL_LIMIT_DIVISOR = 3;
    private static final boolean PROFILE_FLINGING = false;
    private static final boolean PROFILE_SCROLLING = false;
    private static final int QC_BOTTOM = 4;
    private static final int QC_LEFT = 1;
    private static final int QC_RIGHT = 3;
    private static final int QC_STATE_NONE = 0;
    private static final int QC_STATE_PRESSED = 2;
    private static final int QC_STATE_SHOWN = 1;
    private static final int QC_TOP = 2;
    private static final String TAG = "AbsListView";
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
    private static int mTwScrollAmount = 500;
    static final Interpolator sLinearInterpolator = new LinearInterpolator();
    protected boolean AIR_VIEW_WINSET;
    private int HOVERSCROLL_DELAY;
    private float HOVERSCROLL_SPEED;
    private int QC_ICON_HIDE_DELAY;
    private final int SWITCH_CONTROL_FLING;
    private final int SWITCH_CONTROL_SCROLL_DURATION_DEFAULT;
    private final int SWITCH_CONTROL_SCROLL_DURATION_GAP;
    private final int SWITCH_CONTROL_SCROLL_MAX_DURATION;
    private final int SWITCH_CONTROL_SCROLL_MIN_DURATION;
    private boolean USE_SET_INTEGRATOR_HAPTIC;
    private ListItemAccessibilityDelegate mAccessibilityDelegate;
    private int mActivePointerId;
    ListAdapter mAdapter;
    boolean mAdapterHasStableIds;
    private boolean mAlwaysDisableHoverHighlight;
    private int mAutoscrollDuration;
    private int mCacheColorHint;
    boolean mCachingActive;
    boolean mCachingStarted;
    SparseBooleanArray mCheckStates;
    LongSparseArray<Integer> mCheckedIdStates;
    int mCheckedItemCount;
    boolean mChnagedAdapter;
    ActionMode mChoiceActionMode;
    int mChoiceMode;
    private Runnable mClearScrollingCache;
    private ContextMenuInfo mContextMenuInfo;
    private int mCurrentKeyCode;
    private boolean mDVFSLockAcquired;
    AdapterDataSetObserver mDataSetObserver;
    private InputConnection mDefInputConnection;
    private boolean mDeferNotifyDataSetChanged;
    private int mDeltamoveX;
    private int mDeltamoveY;
    private float mDensityScale;
    private int mDirection;
    private int mDragScrollWorkingZonePx;
    boolean mDrawSelectorOnTop;
    private EdgeEffect mEdgeGlowBottom;
    private EdgeEffect mEdgeGlowTop;
    private int mExtraPaddingInBottomHoverArea;
    private int mExtraPaddingInTopHoverArea;
    private FastScroller mFastScroll;
    boolean mFastScrollAlwaysVisible;
    boolean mFastScrollEnabled;
    private int mFastScrollStyle;
    private boolean mFiltered;
    private int mFirstPositionDistanceGuess;
    private int mFirstPressedPoint;
    private boolean mFlingProfilingStarted;
    private FlingRunnable mFlingRunnable;
    private Span mFlingStrictSpan;
    private boolean mForceTranscriptScroll;
    private boolean mForcedClick;
    private boolean mGlobalLayoutListenerAddedFilter;
    private boolean mHapticOverScroll;
    private HapticPreDrawListener mHapticPreDrawListener;
    private boolean mHasWindowFocusForMotion;
    public boolean mHoverAreaEnter;
    private int mHoverBottomAreaHeight;
    private HoverScrollHandler mHoverHandler;
    private int mHoverPosition;
    private long mHoverRecognitionCurrentTime;
    private long mHoverRecognitionDurationTime;
    private long mHoverRecognitionStartTime;
    private int mHoverScrollDirection;
    private boolean mHoverScrollEnable;
    private int mHoverScrollSpeed;
    private long mHoverScrollStartTime;
    private int mHoverScrollStateForListener;
    private long mHoverScrollTimeInterval;
    private int mHoverTopAreaHeight;
    private boolean mHoveredOnEllipsizedText;
    boolean mInitAbsListView;
    private boolean mIsChildViewEnabled;
    private boolean mIsCloseChildSetted;
    private boolean mIsCtrlkeyPressed;
    private boolean mIsDetaching;
    private boolean mIsDragBlockEnabled;
    private boolean mIsDragScrolled;
    private boolean mIsEnabledPaddingInHoverScroll;
    private boolean mIsFirstPenClick;
    private boolean mIsHoverOverscrolled;
    private boolean mIsHoveredByMouse;
    private boolean mIsMovedbeforeUP;
    private boolean mIsMultiFocusEnabled;
    private boolean mIsNeedPenSelectIconSet;
    private boolean mIsNeedPenSelection;
    private boolean mIsPenHovered;
    private boolean mIsPenPressed;
    private boolean mIsPenSelectPointerSetted;
    private boolean mIsQCBtnFadeInSet;
    private boolean mIsQCBtnFadeOutSet;
    private boolean mIsQCShown;
    final boolean[] mIsScrap;
    private boolean mIsSendHoverScrollState;
    private boolean mIsShiftkeyPressed;
    private boolean mIsTextSelectionStarted;
    private boolean mIsTwOnClickEnabled;
    private boolean mIsfirstMoveEvent;
    boolean mJumpAtFirst;
    private int mLastAccessibilityScrollEventFromIndex;
    private int mLastAccessibilityScrollEventToIndex;
    private int mLastHandledItemCount;
    private int mLastPosition;
    private int mLastPositionDistanceGuess;
    private int mLastScrollState;
    private int mLastTouchMode;
    int mLastY;
    int mLayoutMode;
    Rect mListPadding;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    int mMotionCorrection;
    private boolean mMotionEnable;
    int mMotionPosition;
    private MotionRecognitionManager mMotionRecognitionManager;
    int mMotionViewNewTop;
    int mMotionViewOriginalTop;
    int mMotionX;
    int mMotionY;
    MultiChoiceModeWrapper mMultiChoiceModeCallback;
    private Drawable mMultiFocusImage;
    public boolean mMultiSelectionStart;
    private boolean mNeedsHoverScroll;
    private int mNestedYOffset;
    private boolean mNewTextViewHoverState;
    private int mOldAdapterItemCount;
    private int mOldHoverScrollDirection;
    private int mOldKeyCode;
    private boolean mOldTextViewHoverState;
    private OnFluidScrollEffectListener mOnFluidScrollEffectListener;
    private OnScrollListener mOnScrollListener;
    int mOverflingDistance;
    int mOverscrollDistance;
    int mOverscrollMax;
    private final Thread mOwnerThread;
    private long mPenDragScrollTimeInterval;
    private CheckForDoublePenClick mPendingCheckForDoublePenClick;
    private CheckForKeyLongPress mPendingCheckForKeyLongPress;
    private CheckForLongPress mPendingCheckForLongPress;
    private CheckForTap mPendingCheckForTap;
    private SavedState mPendingSync;
    private PerformClick mPerformClick;
    private int mPointerCount;
    PopupWindow mPopup;
    private boolean mPopupHidden;
    Runnable mPositionScrollAfterLayout;
    AbsPositionScroller mPositionScroller;
    private boolean mPreviousTextViewScroll;
    private InputConnectionWrapper mPublicInputConnection;
    private Drawable mQCBtnDrawable;
    private ValueAnimator mQCBtnFadeInAnimator;
    private final Runnable mQCBtnFadeInRunnable;
    private ValueAnimator mQCBtnFadeOutAnimator;
    private final Runnable mQCBtnFadeOutRunnable;
    private Drawable mQCBtnPressedDrawable;
    private int mQCLocation;
    private Rect mQCRect;
    private int mQCScrollDirection;
    private int mQCScrollFrom;
    private int mQCScrollNext;
    private Runnable mQCScrollRunnable;
    private int mQCScrollTo;
    private int mQCScrollingCount;
    private int mQCstate;
    final RecycleBin mRecycler;
    private RemoteViewsAdapter mRemoteAdapter;
    int mResurrectToPosition;
    private final int[] mScrollConsumed;
    View mScrollDown;
    private DecelerateInterpolator mScrollInterpolator;
    private final int[] mScrollOffset;
    private boolean mScrollProfilingStarted;
    private Span mScrollStrictSpan;
    View mScrollUp;
    boolean mScrollingCacheEnabled;
    private int mSecondPressedPoint;
    int mSelectedTop;
    int mSelectionBottomPadding;
    int mSelectionLeftPadding;
    int mSelectionRightPadding;
    int mSelectionTopPadding;
    Drawable mSelector;
    int mSelectorPosition;
    Rect mSelectorRect;
    private int[] mSelectorState;
    private boolean mSmoothScrollbarEnabled;
    boolean mStackFromBottom;
    EditText mTextFilter;
    private boolean mTextFilterEnabled;
    private final float[] mTmpPoint;
    private Rect mTouchFrame;
    int mTouchMode;
    private Runnable mTouchModeReset;
    private int mTouchSlop;
    private int mTouchdownX;
    private int mTouchdownY;
    private int mTranscriptMode;
    private View mTwCloseChildByBottom;
    private View mTwCloseChildByTop;
    private int mTwCloseChildPositionByBottom;
    private int mTwCloseChildPositionByTop;
    public int mTwCurrentFocusPosition;
    private boolean mTwCustomMultiChoiceMode;
    private int mTwDistanceFromCloseChildBottom;
    private int mTwDistanceFromCloseChildTop;
    private int mTwDistanceFromTrackedChildTop;
    private int mTwDragBlockBottom;
    private Drawable mTwDragBlockImage;
    private int mTwDragBlockLeft;
    private Rect mTwDragBlockRect;
    private int mTwDragBlockRight;
    private int mTwDragBlockTop;
    private int mTwDragEndX;
    private int mTwDragEndY;
    private ArrayList<Integer> mTwDragSelectedItemArray;
    private int mTwDragSelectedItemSize;
    private int mTwDragSelectedViewPosition;
    private int mTwDragStartX;
    private int mTwDragStartY;
    private FluidScroller mTwFluidScroll;
    boolean mTwFluidScrollEnabled;
    private ArrayList<Integer> mTwPressItemListArray;
    private TwSmoothScrollByMove mTwSmoothScrollByMove;
    private View mTwTrackedChild;
    private int mTwTrackedChildPosition;
    private LinkedList<Integer> mTwTwScrollRemains;
    private float mVelocityScale;
    private VelocityTracker mVelocityTracker;
    int mWidthMeasureSpec;
    boolean mWindowFocusChanged;

    static abstract class AbsPositionScroller {
        public abstract void start(int i);

        public abstract void start(int i, int i2);

        public abstract void startWithOffset(int i, int i2);

        public abstract void startWithOffset(int i, int i2, int i3);

        public abstract void stop();

        AbsPositionScroller() {
        }
    }

    class AdapterDataSetObserver extends AdapterDataSetObserver {
        AdapterDataSetObserver() {
            super();
        }

        public void onChanged() {
            super.onChanged();
            if (AbsListView.this.mFastScroll != null) {
                AbsListView.this.mFastScroll.onSectionsChanged();
            } else if (AbsListView.this.mTwFluidScroll != null) {
                AbsListView.this.mTwFluidScroll.onSectionsChanged();
            }
        }

        public void onInvalidated() {
            super.onInvalidated();
            if (AbsListView.this.mFastScroll != null) {
                AbsListView.this.mFastScroll.onSectionsChanged();
            } else if (AbsListView.this.mTwFluidScroll != null) {
                AbsListView.this.mTwFluidScroll.onSectionsChanged();
            }
        }
    }

    private final class CheckForDoublePenClick implements Runnable {
        int x;
        int y;

        private CheckForDoublePenClick() {
        }

        public void run() {
            boolean isNeedActionMode = false;
            if (AbsListView.this.mIsFirstPenClick && AbsListView.this.mAdapter != null) {
                if (AbsListView.this.mTwDragSelectedItemSize != 0) {
                    if (AbsListView.this.mCheckStates != null && (AbsListView.this.mChoiceMode == 2 || AbsListView.this.mChoiceMode == 3)) {
                        Iterator i$ = AbsListView.this.mTwDragSelectedItemArray.iterator();
                        while (i$.hasNext()) {
                            if (AbsListView.this.mAdapter.isEnabled(((Integer) i$.next()).intValue())) {
                                isNeedActionMode = true;
                            }
                        }
                        if (AbsListView.this.mChoiceMode == 3 && AbsListView.this.mChoiceActionMode == null && isNeedActionMode) {
                            AbsListView.this.mChoiceActionMode = AbsListView.this.startActionMode(AbsListView.this.mMultiChoiceModeCallback);
                        }
                        if (AbsListView.this.mIsTwOnClickEnabled) {
                            i$ = AbsListView.this.mTwDragSelectedItemArray.iterator();
                            while (i$.hasNext()) {
                                Integer dragSelectedViewPosition = (Integer) i$.next();
                                if (AbsListView.this.mAdapter.isEnabled(dragSelectedViewPosition.intValue())) {
                                    AbsListView.this.performItemClick(null, dragSelectedViewPosition.intValue(), AbsListView.this.getItemIdAtPosition(dragSelectedViewPosition.intValue()));
                                }
                            }
                        }
                    }
                    AbsListView.this.totwNotifyMultiSelectedStop(this.x, this.y);
                }
                AbsListView.this.mTwDragSelectedItemArray.clear();
                AbsListView.this.mTwDragSelectedItemSize = 0;
            }
            AbsListView.this.mIsFirstPenClick = false;
        }
    }

    private class WindowRunnnable {
        private int mOriginalAttachCount;

        private WindowRunnnable() {
        }

        public void rememberWindowAttachCount() {
            this.mOriginalAttachCount = AbsListView.this.getWindowAttachCount();
        }

        public boolean sameWindow() {
            return AbsListView.this.getWindowAttachCount() == this.mOriginalAttachCount;
        }
    }

    private class CheckForKeyLongPress extends WindowRunnnable implements Runnable {
        private CheckForKeyLongPress() {
            super();
        }

        public void run() {
            if (AbsListView.this.isPressed() && AbsListView.this.mSelectedPosition >= 0) {
                View v = AbsListView.this.getChildAt(AbsListView.this.mSelectedPosition - AbsListView.this.mFirstPosition);
                if (AbsListView.this.mDataChanged) {
                    AbsListView.this.setPressed(false);
                    if (v != null) {
                        v.setPressed(false);
                        return;
                    }
                    return;
                }
                boolean handled = false;
                if (sameWindow()) {
                    handled = AbsListView.this.performLongPress(v, AbsListView.this.mSelectedPosition, AbsListView.this.mSelectedRowId);
                }
                if (handled) {
                    AbsListView.this.setPressed(false);
                    if (v != null) {
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
            View child = AbsListView.this.getChildAt(AbsListView.this.mMotionPosition - AbsListView.this.mFirstPosition);
            if (child != null) {
                int longPressPosition = AbsListView.this.mMotionPosition;
                long longPressId = AbsListView.this.mAdapter.getItemId(AbsListView.this.mMotionPosition);
                boolean handled = false;
                if (sameWindow() && !AbsListView.this.mDataChanged) {
                    handled = AbsListView.this.performLongPress(child, longPressPosition, longPressId);
                }
                if (handled) {
                    AbsListView.this.mTouchMode = -1;
                    AbsListView.this.setPressed(false);
                    child.setPressed(false);
                    return;
                }
                AbsListView.this.mTouchMode = 2;
            }
        }
    }

    private final class CheckForTap implements Runnable {
        float x;
        float y;

        private CheckForTap() {
        }

        public void run() {
            if (AbsListView.this.mTouchMode == 0) {
                AbsListView.this.mTouchMode = 1;
                View child = AbsListView.this.getChildAt(AbsListView.this.mMotionPosition - AbsListView.this.mFirstPosition);
                if (child != null && !child.hasFocusable() && AbsListView.this.getAdapter() != null && AbsListView.this.mMotionPosition >= 0 && ((ListAdapter) AbsListView.this.getAdapter()).isEnabled(AbsListView.this.mMotionPosition)) {
                    AbsListView.this.mLayoutMode = 0;
                    if (AbsListView.this.mDataChanged) {
                        AbsListView.this.mTouchMode = 2;
                        return;
                    }
                    float[] point = AbsListView.this.mTmpPoint;
                    point[0] = this.x;
                    point[1] = this.y;
                    AbsListView.this.transformPointToViewLocal(point, child);
                    child.drawableHotspotChanged(point[0], point[1]);
                    child.setPressed(true);
                    AbsListView.this.setPressed(true);
                    AbsListView.this.layoutChildren();
                    AbsListView.this.positionSelector(AbsListView.this.mMotionPosition, child);
                    AbsListView.this.refreshDrawableState();
                    int longPressTimeout = ViewConfiguration.getLongPressTimeout();
                    boolean longClickable = AbsListView.this.isLongClickable();
                    if (AbsListView.this.mSelector != null) {
                        Drawable d = AbsListView.this.mSelector.getCurrent();
                        if (d != null && (d instanceof TransitionDrawable)) {
                            if (longClickable) {
                                ((TransitionDrawable) d).startTransition(longPressTimeout);
                            } else {
                                ((TransitionDrawable) d).resetTransition();
                            }
                        }
                        AbsListView.this.mSelector.setHotspot(this.x, this.y);
                    }
                    if (longClickable) {
                        if (AbsListView.this.mPendingCheckForLongPress == null) {
                            AbsListView.this.mPendingCheckForLongPress = new CheckForLongPress();
                        }
                        AbsListView.this.mPendingCheckForLongPress.rememberWindowAttachCount();
                        AbsListView.this.postDelayed(AbsListView.this.mPendingCheckForLongPress, (long) longPressTimeout);
                        return;
                    }
                    AbsListView.this.mTouchMode = 2;
                }
            }
        }
    }

    private class FlingRunnable implements Runnable {
        private static final int FLYWHEEL_TIMEOUT = 40;
        private final Runnable mCheckFlywheel = new Runnable() {
            public void run() {
                int activeId = AbsListView.this.mActivePointerId;
                VelocityTracker vt = AbsListView.this.mVelocityTracker;
                OverScroller scroller = FlingRunnable.this.mScroller;
                if (vt != null && activeId != -1) {
                    vt.computeCurrentVelocity(1000, (float) AbsListView.this.mMaximumVelocity);
                    float yvel = -vt.getYVelocity(activeId);
                    if (Math.abs(yvel) < ((float) AbsListView.this.mMinimumVelocity) || !scroller.isScrollingInDirection(0.0f, yvel)) {
                        FlingRunnable.this.endFling();
                        AbsListView.this.mTouchMode = 3;
                        AbsListView.this.reportScrollStateChange(1);
                        return;
                    }
                    AbsListView.this.postDelayed(this, 40);
                }
            }
        };
        private int mLastFlingY;
        private final OverScroller mScroller;

        FlingRunnable() {
            this.mScroller = new OverScroller(AbsListView.this.getContext());
        }

        void start(int initialVelocity) {
            int initialY;
            if (initialVelocity < 0) {
                initialY = Integer.MAX_VALUE;
            } else {
                initialY = 0;
            }
            this.mLastFlingY = initialY;
            this.mScroller.setInterpolator(null);
            this.mScroller.fling(0, initialY, 0, initialVelocity, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE);
            AbsListView.this.mTouchMode = 4;
            AbsListView.this.invalidate();
            AbsListView.this.postOnAnimation(this);
            if (AbsListView.this.mFlingStrictSpan == null) {
                AbsListView.this.mFlingStrictSpan = StrictMode.enterCriticalSpan("AbsListView-fling");
            }
        }

        void start(int initialVelocity, boolean accDisabled) {
            int initialY;
            if (initialVelocity < 0) {
                initialY = Integer.MAX_VALUE;
            } else {
                initialY = 0;
            }
            this.mLastFlingY = initialY;
            this.mScroller.setInterpolator(null);
            this.mScroller.fling(0, initialY, 0, initialVelocity, 0, Integer.MAX_VALUE, 0, Integer.MAX_VALUE, accDisabled);
            AbsListView.this.mTouchMode = 4;
            AbsListView.this.invalidate();
            AbsListView.this.postOnAnimation(this);
            if (AbsListView.this.mFlingStrictSpan == null) {
                AbsListView.this.mFlingStrictSpan = StrictMode.enterCriticalSpan("AbsListView-fling");
            }
        }

        void startSpringback() {
            if (this.mScroller.springBack(0, AbsListView.this.mScrollY, 0, 0, 0, 0)) {
                AbsListView.this.mTouchMode = 6;
                AbsListView.this.invalidate();
                AbsListView.this.postOnAnimation(this);
                return;
            }
            AbsListView.this.mTouchMode = -1;
            AbsListView.this.reportScrollStateChange(0);
        }

        void startOverfling(int initialVelocity) {
            this.mScroller.setInterpolator(null);
            this.mScroller.fling(0, AbsListView.this.mScrollY, 0, initialVelocity, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, AbsListView.this.getHeight());
            AbsListView.this.mTouchMode = 6;
            AbsListView.this.invalidate();
            AbsListView.this.postOnAnimation(this);
        }

        void edgeReached(int delta) {
            this.mScroller.notifyVerticalEdgeReached(AbsListView.this.mScrollY, 0, AbsListView.this.mOverflingDistance);
            int overscrollMode = AbsListView.this.getOverScrollMode();
            if (overscrollMode == 0 || (overscrollMode == 1 && !AbsListView.this.contentFits())) {
                AbsListView.this.mTouchMode = 6;
                int vel = (int) this.mScroller.getCurrVelocity();
                if (delta > 0) {
                    AbsListView.this.mEdgeGlowTop.onAbsorb(vel);
                } else {
                    AbsListView.this.mEdgeGlowBottom.onAbsorb(vel);
                }
            } else {
                AbsListView.this.mTouchMode = -1;
                if (AbsListView.this.mPositionScroller != null) {
                    AbsListView.this.mPositionScroller.stop();
                }
            }
            AbsListView.this.invalidate();
            AbsListView.this.postOnAnimation(this);
        }

        void startScroll(int distance, int duration, boolean linear) {
            int initialY;
            if (distance < 0) {
                initialY = Integer.MAX_VALUE;
            } else {
                initialY = 0;
            }
            this.mLastFlingY = initialY;
            this.mScroller.setInterpolator(linear ? AbsListView.sLinearInterpolator : null);
            this.mScroller.startScroll(0, initialY, 0, distance, duration);
            AbsListView.this.mTouchMode = 4;
            AbsListView.this.postOnAnimation(this);
        }

        void endFling() {
            AbsListView.this.mTouchMode = -1;
            AbsListView.this.removeCallbacks(this);
            AbsListView.this.removeCallbacks(this.mCheckFlywheel);
            AbsListView.this.reportScrollStateChange(0);
            AbsListView.this.clearScrollingCache();
            this.mScroller.abortAnimation();
            if (AbsListView.this.mFlingStrictSpan != null) {
                AbsListView.this.mFlingStrictSpan.finish();
                AbsListView.this.mFlingStrictSpan = null;
            }
        }

        void flywheelTouch() {
            AbsListView.this.postDelayed(this.mCheckFlywheel, 40);
        }

        public void run() {
            OverScroller scroller;
            switch (AbsListView.this.mTouchMode) {
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
                        int scrollY = AbsListView.this.mScrollY;
                        int currY = scroller.getCurrY();
                        if (AbsListView.this.overScrollBy(0, currY - scrollY, 0, scrollY, 0, 0, 0, AbsListView.this.mOverflingDistance, false)) {
                            boolean crossDown = scrollY <= 0 && currY > 0;
                            boolean crossUp = scrollY >= 0 && currY < 0;
                            if (crossDown || crossUp) {
                                int velocity = (int) scroller.getCurrVelocity();
                                if (crossUp) {
                                    int i = -velocity;
                                    return;
                                }
                                return;
                            }
                            startSpringback();
                            return;
                        }
                        AbsListView.this.invalidate();
                        AbsListView.this.postOnAnimation(this);
                        return;
                    }
                    endFling();
                    return;
                default:
                    endFling();
                    return;
            }
            if (AbsListView.this.mDataChanged) {
                AbsListView.this.layoutChildren();
            }
            if (AbsListView.this.mItemCount == 0 || AbsListView.this.getChildCount() == 0) {
                endFling();
                return;
            }
            scroller = this.mScroller;
            boolean more = scroller.computeScrollOffset();
            int y = scroller.getCurrY();
            int delta = this.mLastFlingY - y;
            if (delta > 0) {
                AbsListView.this.mMotionPosition = AbsListView.this.mFirstPosition;
                AbsListView.this.mMotionViewOriginalTop = AbsListView.this.getChildAt(0).getTop();
                delta = Math.min(((AbsListView.this.getHeight() - AbsListView.this.mPaddingBottom) - AbsListView.this.mPaddingTop) - 1, delta);
            } else {
                int offsetToLast = AbsListView.this.getChildCount() - 1;
                AbsListView.this.mMotionPosition = AbsListView.this.mFirstPosition + offsetToLast;
                AbsListView.this.mMotionViewOriginalTop = AbsListView.this.getChildAt(offsetToLast).getTop();
                delta = Math.max(-(((AbsListView.this.getHeight() - AbsListView.this.mPaddingBottom) - AbsListView.this.mPaddingTop) - 1), delta);
            }
            View motionView = AbsListView.this.getChildAt(AbsListView.this.mMotionPosition - AbsListView.this.mFirstPosition);
            int oldTop = 0;
            if (motionView != null) {
                oldTop = motionView.getTop();
            }
            boolean atEdge = AbsListView.this.trackMotionScroll(delta, delta);
            boolean atEnd = atEdge && delta != 0;
            if (atEnd) {
                if (motionView != null) {
                    AbsListView.this.overScrollBy(0, -(delta - (motionView.getTop() - oldTop)), 0, AbsListView.this.mScrollY, 0, 0, 0, AbsListView.this.mOverflingDistance, false);
                }
                if (more) {
                    edgeReached(delta);
                }
            } else if (!more || atEnd) {
                endFling();
            } else {
                if (atEdge) {
                    AbsListView.this.invalidate();
                }
                this.mLastFlingY = y;
                AbsListView.this.postOnAnimation(this);
            }
        }
    }

    private static class HoverScrollHandler extends Handler {
        private final WeakReference<AbsListView> mListView;

        HoverScrollHandler(AbsListView sv) {
            this.mListView = new WeakReference(sv);
        }

        public void handleMessage(Message msg) {
            AbsListView sv = (AbsListView) this.mListView.get();
            if (sv != null) {
                sv.handleMessage(msg);
            }
        }
    }

    private class InputConnectionWrapper implements InputConnection {
        private final EditorInfo mOutAttrs;
        private InputConnection mTarget;

        public InputConnectionWrapper(EditorInfo outAttrs) {
            this.mOutAttrs = outAttrs;
        }

        private InputConnection getTarget() {
            if (this.mTarget == null) {
                this.mTarget = AbsListView.this.getTextFilterInput().onCreateInputConnection(this.mOutAttrs);
            }
            return this.mTarget;
        }

        public boolean reportFullscreenMode(boolean enabled) {
            return AbsListView.this.mDefInputConnection.reportFullscreenMode(enabled);
        }

        public boolean performEditorAction(int editorAction) {
            if (editorAction != 6) {
                return false;
            }
            InputMethodManager imm = (InputMethodManager) AbsListView.this.getContext().getSystemService("input_method");
            if (imm != null) {
                imm.hideSoftInputFromWindow(AbsListView.this.getWindowToken(), 0);
            }
            return true;
        }

        public boolean sendKeyEvent(KeyEvent event) {
            return AbsListView.this.mDefInputConnection.sendKeyEvent(event);
        }

        public CharSequence getTextBeforeCursor(int n, int flags) {
            if (this.mTarget == null) {
                return "";
            }
            return this.mTarget.getTextBeforeCursor(n, flags);
        }

        public CharSequence getTextAfterCursor(int n, int flags) {
            if (this.mTarget == null) {
                return "";
            }
            return this.mTarget.getTextAfterCursor(n, flags);
        }

        public CharSequence getSelectedText(int flags) {
            if (this.mTarget == null) {
                return "";
            }
            return this.mTarget.getSelectedText(flags);
        }

        public int getCursorCapsMode(int reqModes) {
            if (this.mTarget == null) {
                return 16384;
            }
            return this.mTarget.getCursorCapsMode(reqModes);
        }

        public ExtractedText getExtractedText(ExtractedTextRequest request, int flags) {
            return getTarget().getExtractedText(request, flags);
        }

        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            return getTarget().deleteSurroundingText(beforeLength, afterLength);
        }

        public boolean setComposingText(CharSequence text, int newCursorPosition) {
            return getTarget().setComposingText(text, newCursorPosition);
        }

        public boolean setComposingRegion(int start, int end) {
            return getTarget().setComposingRegion(start, end);
        }

        public boolean finishComposingText() {
            return this.mTarget == null || this.mTarget.finishComposingText();
        }

        public boolean commitText(CharSequence text, int newCursorPosition) {
            return getTarget().commitText(text, newCursorPosition);
        }

        public boolean commitCompletion(CompletionInfo text) {
            return getTarget().commitCompletion(text);
        }

        public boolean commitCorrection(CorrectionInfo correctionInfo) {
            return getTarget().commitCorrection(correctionInfo);
        }

        public boolean setSelection(int start, int end) {
            return getTarget().setSelection(start, end);
        }

        public boolean performContextMenuAction(int id) {
            return getTarget().performContextMenuAction(id);
        }

        public boolean beginBatchEdit() {
            return getTarget().beginBatchEdit();
        }

        public boolean endBatchEdit() {
            return getTarget().endBatchEdit();
        }

        public boolean clearMetaKeyStates(int states) {
            return getTarget().clearMetaKeyStates(states);
        }

        public boolean performPrivateCommand(String action, Bundle data) {
            return getTarget().performPrivateCommand(action, data);
        }

        public boolean requestCursorUpdates(int cursorUpdateMode) {
            return getTarget().requestCursorUpdates(cursorUpdateMode);
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        @ExportedProperty(category = "list")
        boolean forceAdd;
        boolean isEnabled;
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

        protected void encodeProperties(ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("list:viewType", this.viewType);
            encoder.addProperty("list:recycledHeaderFooter", this.recycledHeaderFooter);
            encoder.addProperty("list:forceAdd", this.forceAdd);
            encoder.addProperty("list:isEnabled", this.isEnabled);
        }
    }

    class ListItemAccessibilityDelegate extends AccessibilityDelegate {
        ListItemAccessibilityDelegate() {
        }

        public void onInitializeAccessibilityNodeInfo(View host, AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(host, info);
            AbsListView.this.onInitializeAccessibilityNodeInfoForItem(host, AbsListView.this.getPositionForView(host), info);
        }

        public boolean performAccessibilityAction(View host, int action, Bundle arguments) {
            if (super.performAccessibilityAction(host, action, arguments)) {
                return true;
            }
            int position = AbsListView.this.getPositionForView(host);
            if (position == -1 || AbsListView.this.mAdapter == null) {
                return false;
            }
            if (position >= AbsListView.this.mAdapter.getCount()) {
                return false;
            }
            android.view.ViewGroup.LayoutParams lp = host.getLayoutParams();
            boolean isItemEnabled;
            if (lp instanceof LayoutParams) {
                isItemEnabled = ((LayoutParams) lp).isEnabled;
            } else {
                isItemEnabled = false;
            }
            if (!AbsListView.this.isEnabled() || !isItemEnabled) {
                return false;
            }
            switch (action) {
                case 4:
                    if (AbsListView.this.getSelectedItemPosition() == position) {
                        return false;
                    }
                    AbsListView.this.setSelection(position);
                    return true;
                case 8:
                    if (AbsListView.this.getSelectedItemPosition() != position) {
                        return false;
                    }
                    AbsListView.this.setSelection(-1);
                    return true;
                case 16:
                    if (!AbsListView.this.isClickable()) {
                        return false;
                    }
                    return AbsListView.this.performItemClick(host, position, AbsListView.this.getItemIdAtPosition(position));
                case 32:
                    if (!AbsListView.this.isLongClickable()) {
                        return false;
                    }
                    return AbsListView.this.performLongPress(host, position, AbsListView.this.getItemIdAtPosition(position));
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
            AbsListView.this.setLongClickable(false);
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
            AbsListView.this.mChoiceActionMode = null;
            AbsListView.this.clearChoices();
            AbsListView.this.mDataChanged = true;
            AbsListView.this.rememberSyncState();
            AbsListView.this.requestLayout();
            AbsListView.this.setLongClickable(true);
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            this.mWrapped.onItemCheckedStateChanged(mode, position, id, checked);
            if (AbsListView.this.getCheckedItemCount() == 0 && !AbsListView.this.mTwCustomMultiChoiceMode) {
                mode.finish();
            }
        }
    }

    public interface OnFluidScrollEffectListener {
        public static final int EFFECT_STATE_CLOSE = 0;
        public static final int EFFECT_STATE_OPEN = 1;

        void onEffectStateChanged(int i, float f);
    }

    public interface OnScrollListener {
        public static final int SCROLL_STATE_FLING = 2;
        public static final int SCROLL_STATE_IDLE = 0;
        public static final int SCROLL_STATE_TOUCH_SCROLL = 1;

        void onScroll(AbsListView absListView, int i, int i2, int i3);

        void onScrollStateChanged(AbsListView absListView, int i);
    }

    private class PerformClick extends WindowRunnnable implements Runnable {
        int mClickMotionPosition;

        private PerformClick() {
            super();
        }

        public void run() {
            if (AbsListView.this.mForcedClick || !AbsListView.this.mDataChanged) {
                ListAdapter adapter = AbsListView.this.mAdapter;
                int motionPosition = this.mClickMotionPosition;
                if (adapter != null && AbsListView.this.mItemCount > 0 && motionPosition != -1 && motionPosition < adapter.getCount() && sameWindow()) {
                    View view = AbsListView.this.getChildAt(motionPosition - AbsListView.this.mFirstPosition);
                    if (view != null) {
                        try {
                            AbsListView.this.performItemClick(view, motionPosition, adapter.getItemId(motionPosition));
                            if (AbsListView.this.mIsShiftkeyPressed || AbsListView.this.mIsCtrlkeyPressed) {
                                boolean handledNotifykeyPress = AbsListView.this.twNotifyKeyPressState(view, motionPosition, adapter.getItemId(motionPosition));
                            }
                            if ((!AbsListView.this.mIsShiftkeyPressed && !AbsListView.this.mIsCtrlkeyPressed) || AbsListView.this.mAdapter == null) {
                                return;
                            }
                            if (AbsListView.this.mIsCtrlkeyPressed) {
                                AbsListView.this.addToPressItemListArray(motionPosition, -1);
                            } else if (AbsListView.this.mIsShiftkeyPressed) {
                                AbsListView.this.resetPressItemListArray();
                                if (AbsListView.this.mFirstPressedPoint == -1) {
                                    AbsListView.this.addToPressItemListArray(motionPosition, -1);
                                    AbsListView.this.mFirstPressedPoint = motionPosition;
                                    return;
                                }
                                AbsListView.this.addToPressItemListArray(AbsListView.this.mFirstPressedPoint, motionPosition);
                            }
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    class PositionScroller extends AbsPositionScroller implements Runnable {
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
        private int mOffsetFromTop;
        private int mScrollDuration;
        private int mTargetPos;

        PositionScroller() {
            this.mExtraScroll = ViewConfiguration.get(AbsListView.this.mContext).getScaledFadingEdgeLength();
        }

        public void start(final int position) {
            stop();
            if (AbsListView.this.mDataChanged) {
                AbsListView.this.mPositionScrollAfterLayout = new Runnable() {
                    public void run() {
                        PositionScroller.this.start(position);
                    }
                };
                return;
            }
            int childCount = AbsListView.this.getChildCount();
            if (childCount != 0) {
                int viewTravelCount;
                int firstPos = AbsListView.this.mFirstPosition;
                int lastPos = (firstPos + childCount) - 1;
                int clampedPosition = Math.max(0, Math.min(AbsListView.this.getCount() - 1, position));
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
                AbsListView.this.postOnAnimation(this);
            }
        }

        public void start(final int position, final int boundPosition) {
            stop();
            if (boundPosition == -1) {
                start(position);
            } else if (AbsListView.this.mDataChanged) {
                AbsListView.this.mPositionScrollAfterLayout = new Runnable() {
                    public void run() {
                        PositionScroller.this.start(position, boundPosition);
                    }
                };
            } else {
                int childCount = AbsListView.this.getChildCount();
                if (childCount != 0) {
                    int viewTravelCount;
                    int firstPos = AbsListView.this.mFirstPosition;
                    int lastPos = (firstPos + childCount) - 1;
                    int clampedPosition = Math.max(0, Math.min(AbsListView.this.getCount() - 1, position));
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
                    AbsListView.this.postOnAnimation(this);
                }
            }
        }

        public void startWithOffset(int position, int offset) {
            startWithOffset(position, offset, 200);
        }

        public void startWithOffset(final int position, int offset, final int duration) {
            stop();
            if (AbsListView.this.mDataChanged) {
                final int postOffset = offset;
                AbsListView.this.mPositionScrollAfterLayout = new Runnable() {
                    public void run() {
                        PositionScroller.this.startWithOffset(position, postOffset, duration);
                    }
                };
                return;
            }
            int childCount = AbsListView.this.getChildCount();
            if (childCount != 0) {
                int viewTravelCount;
                offset += AbsListView.this.getPaddingTop();
                this.mTargetPos = Math.max(0, Math.min(AbsListView.this.getCount() - 1, position));
                this.mOffsetFromTop = offset;
                this.mBoundPos = -1;
                this.mLastSeenPos = -1;
                this.mMode = 5;
                int firstPos = AbsListView.this.mFirstPosition;
                int lastPos = (firstPos + childCount) - 1;
                if (this.mTargetPos < firstPos) {
                    viewTravelCount = firstPos - this.mTargetPos;
                } else if (this.mTargetPos > lastPos) {
                    viewTravelCount = this.mTargetPos - lastPos;
                } else {
                    AbsListView.this.smoothScrollBy(AbsListView.this.getChildAt(this.mTargetPos - firstPos).getTop() - offset, duration, true);
                    return;
                }
                float screenTravelCount = ((float) viewTravelCount) / ((float) childCount);
                if (screenTravelCount >= 1.0f) {
                    duration = (int) (((float) duration) / screenTravelCount);
                }
                this.mScrollDuration = duration;
                this.mLastSeenPos = -1;
                AbsListView.this.postOnAnimation(this);
            }
        }

        private void scrollToVisible(int targetPos, int boundPos, int duration) {
            int firstPos = AbsListView.this.mFirstPosition;
            int lastPos = (firstPos + AbsListView.this.getChildCount()) - 1;
            int paddedTop = AbsListView.this.mListPadding.top;
            int paddedBottom = AbsListView.this.getHeight() - AbsListView.this.mListPadding.bottom;
            if (targetPos < firstPos || targetPos > lastPos) {
                Log.w(AbsListView.TAG, "scrollToVisible called with targetPos " + targetPos + " not visible [" + firstPos + ", " + lastPos + "]");
            }
            if (boundPos < firstPos || boundPos > lastPos) {
                boundPos = -1;
            }
            View targetChild = AbsListView.this.getChildAt(targetPos - firstPos);
            int targetTop = targetChild.getTop();
            int targetBottom = targetChild.getBottom();
            int scrollBy = 0;
            if (targetBottom > paddedBottom) {
                scrollBy = targetBottom - paddedBottom;
            }
            if (targetTop < paddedTop) {
                scrollBy = targetTop - paddedTop;
            }
            if (scrollBy != 0) {
                if (boundPos >= 0) {
                    View boundChild = AbsListView.this.getChildAt(boundPos - firstPos);
                    int boundTop = boundChild.getTop();
                    int boundBottom = boundChild.getBottom();
                    int absScroll = Math.abs(scrollBy);
                    if (scrollBy < 0 && boundBottom + absScroll > paddedBottom) {
                        scrollBy = Math.max(0, boundBottom - paddedBottom);
                    } else if (scrollBy > 0 && boundTop - absScroll < paddedTop) {
                        scrollBy = Math.min(0, boundTop - paddedTop);
                    }
                }
                AbsListView.this.smoothScrollBy(scrollBy, duration);
            }
        }

        public void stop() {
            AbsListView.this.removeCallbacks(this);
        }

        public void run() {
            int listHeight = AbsListView.this.getHeight();
            int firstPos = AbsListView.this.mFirstPosition;
            int lastViewIndex;
            int lastPos;
            View lastView;
            int lastViewHeight;
            int lastViewPixelsShowing;
            int extraScroll;
            int childCount;
            switch (this.mMode) {
                case 1:
                    lastViewIndex = AbsListView.this.getChildCount() - 1;
                    lastPos = firstPos + lastViewIndex;
                    if (lastViewIndex < 0) {
                        return;
                    }
                    if (lastPos == this.mLastSeenPos) {
                        AbsListView.this.postOnAnimation(this);
                        return;
                    }
                    lastView = AbsListView.this.getChildAt(lastViewIndex);
                    lastViewHeight = lastView.getHeight();
                    lastViewPixelsShowing = listHeight - lastView.getTop();
                    if (lastPos < AbsListView.this.mItemCount - 1) {
                        extraScroll = Math.max(AbsListView.this.mListPadding.bottom, this.mExtraScroll);
                    } else {
                        extraScroll = AbsListView.this.mListPadding.bottom;
                    }
                    int scrollBy = (lastViewHeight - lastViewPixelsShowing) + extraScroll;
                    AbsListView.this.smoothScrollBy(scrollBy, this.mScrollDuration, true);
                    this.mLastSeenPos = lastPos;
                    if (lastPos < this.mTargetPos) {
                        AbsListView.this.postOnAnimation(this);
                        return;
                    }
                    return;
                case 2:
                    if (firstPos == this.mLastSeenPos) {
                        AbsListView.this.postOnAnimation(this);
                        return;
                    }
                    View firstView = AbsListView.this.getChildAt(0);
                    if (firstView != null) {
                        int firstViewTop = firstView.getTop();
                        if (firstPos > 0) {
                            extraScroll = Math.max(this.mExtraScroll, AbsListView.this.mListPadding.top);
                        } else {
                            extraScroll = AbsListView.this.mListPadding.top;
                        }
                        AbsListView.this.smoothScrollBy(firstViewTop - extraScroll, this.mScrollDuration, true);
                        this.mLastSeenPos = firstPos;
                        if (firstPos > this.mTargetPos) {
                            AbsListView.this.postOnAnimation(this);
                            return;
                        }
                        return;
                    }
                    return;
                case 3:
                    childCount = AbsListView.this.getChildCount();
                    if (firstPos != this.mBoundPos && childCount > 1 && firstPos + childCount < AbsListView.this.mItemCount) {
                        int nextPos = firstPos + 1;
                        if (nextPos == this.mLastSeenPos) {
                            AbsListView.this.postOnAnimation(this);
                            return;
                        }
                        View nextView = AbsListView.this.getChildAt(1);
                        int nextViewHeight = nextView.getHeight();
                        int nextViewTop = nextView.getTop();
                        extraScroll = Math.max(AbsListView.this.mListPadding.bottom, this.mExtraScroll);
                        if (nextPos < this.mBoundPos) {
                            AbsListView.this.smoothScrollBy(Math.max(0, (nextViewHeight + nextViewTop) - extraScroll), this.mScrollDuration, true);
                            this.mLastSeenPos = nextPos;
                            AbsListView.this.postOnAnimation(this);
                            return;
                        } else if (nextViewTop > extraScroll) {
                            AbsListView.this.smoothScrollBy(nextViewTop - extraScroll, this.mScrollDuration, true);
                            return;
                        } else {
                            return;
                        }
                    }
                    return;
                case 4:
                    lastViewIndex = AbsListView.this.getChildCount() - 2;
                    if (lastViewIndex >= 0) {
                        lastPos = firstPos + lastViewIndex;
                        if (lastPos == this.mLastSeenPos) {
                            AbsListView.this.postOnAnimation(this);
                            return;
                        }
                        lastView = AbsListView.this.getChildAt(lastViewIndex);
                        lastViewHeight = lastView.getHeight();
                        int lastViewTop = lastView.getTop();
                        lastViewPixelsShowing = listHeight - lastViewTop;
                        extraScroll = Math.max(AbsListView.this.mListPadding.top, this.mExtraScroll);
                        this.mLastSeenPos = lastPos;
                        if (lastPos > this.mBoundPos) {
                            AbsListView.this.smoothScrollBy(-(lastViewPixelsShowing - extraScroll), this.mScrollDuration, true);
                            AbsListView.this.postOnAnimation(this);
                            return;
                        }
                        int bottom = listHeight - extraScroll;
                        int lastViewBottom = lastViewTop + lastViewHeight;
                        if (bottom > lastViewBottom) {
                            AbsListView.this.smoothScrollBy(-(bottom - lastViewBottom), this.mScrollDuration, true);
                            return;
                        }
                        return;
                    }
                    return;
                case 5:
                    if (this.mLastSeenPos == firstPos) {
                        AbsListView.this.postOnAnimation(this);
                        return;
                    }
                    this.mLastSeenPos = firstPos;
                    childCount = AbsListView.this.getChildCount();
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
                        AbsListView.this.smoothScrollBy((int) (((float) (-AbsListView.this.getHeight())) * modifier), (int) (((float) this.mScrollDuration) * modifier), true);
                        AbsListView.this.postOnAnimation(this);
                        return;
                    } else if (position > lastPos) {
                        AbsListView.this.smoothScrollBy((int) (((float) AbsListView.this.getHeight()) * modifier), (int) (((float) this.mScrollDuration) * modifier), true);
                        AbsListView.this.postOnAnimation(this);
                        return;
                    } else {
                        int distance = AbsListView.this.getChildAt(position - firstPos).getTop() - this.mOffsetFromTop;
                        AbsListView.this.smoothScrollBy(distance, (int) (((float) this.mScrollDuration) * (((float) Math.abs(distance)) / ((float) AbsListView.this.getHeight()))), true);
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
            if (this.mViewTypeCount == 1) {
                clearScrap(this.mCurrentScrap);
            } else {
                int typeCount = this.mViewTypeCount;
                for (int i = 0; i < typeCount; i++) {
                    clearScrap(this.mScrapViews[i]);
                }
            }
            clearTransientStateViews();
        }

        void fillActiveViews(int childCount, int firstActivePosition) {
            if (this.mActiveViews.length < childCount) {
                this.mActiveViews = new View[childCount];
            }
            this.mFirstActivePosition = firstActivePosition;
            View[] activeViews = this.mActiveViews;
            for (int i = 0; i < childCount; i++) {
                View child = AbsListView.this.getChildAt(i);
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (!(lp == null || lp.viewType == -2)) {
                    activeViews[i] = child;
                    lp.scrappedFromPosition = firstActivePosition + i;
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
            if (AbsListView.this.mAdapter == null || !AbsListView.this.mAdapterHasStableIds || this.mTransientStateViewsById == null) {
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
            long id = AbsListView.this.mAdapter.getItemId(position);
            result = (View) this.mTransientStateViewsById.get(id);
            this.mTransientStateViewsById.remove(id);
            return result;
        }

        void clearTransientStateViews() {
            int N;
            int i;
            SparseArray<View> viewsByPos = this.mTransientStateViews;
            if (viewsByPos != null) {
                N = viewsByPos.size();
                for (i = 0; i < N; i++) {
                    removeDetachedView((View) viewsByPos.valueAt(i), false);
                }
                viewsByPos.clear();
            }
            LongSparseArray<View> viewsById = this.mTransientStateViewsById;
            if (viewsById != null) {
                N = viewsById.size();
                for (i = 0; i < N; i++) {
                    removeDetachedView((View) viewsById.valueAt(i), false);
                }
                viewsById.clear();
            }
        }

        View getScrapView(int position) {
            int whichScrap = AbsListView.this.mAdapter.getItemViewType(position);
            if (whichScrap < 0) {
                return null;
            }
            if (this.mViewTypeCount == 1) {
                return retrieveFromScrap(this.mCurrentScrap, position);
            }
            if (whichScrap < this.mScrapViews.length) {
                return retrieveFromScrap(this.mScrapViews[whichScrap], position);
            }
            return null;
        }

        void addScrapView(View scrap, int position) {
            LayoutParams lp = (LayoutParams) scrap.getLayoutParams();
            if (lp != null) {
                lp.scrappedFromPosition = position;
                int viewType = lp.viewType;
                if (shouldRecycleViewType(viewType)) {
                    scrap.dispatchStartTemporaryDetach();
                    AbsListView.this.notifyViewAccessibilityStateChangedIfNeeded(1);
                    if (!scrap.hasTransientState()) {
                        if (this.mViewTypeCount == 1) {
                            this.mCurrentScrap.add(scrap);
                        } else if (!this.mScrapViews[viewType].contains(scrap)) {
                            this.mScrapViews[viewType].add(scrap);
                        }
                        if (this.mRecyclerListener != null) {
                            this.mRecyclerListener.onMovedToScrapHeap(scrap);
                        }
                    } else if (AbsListView.this.mAdapter != null && AbsListView.this.mAdapterHasStableIds) {
                        if (this.mTransientStateViewsById == null) {
                            this.mTransientStateViewsById = new LongSparseArray();
                        }
                        this.mTransientStateViewsById.put(lp.itemId, scrap);
                    } else if (AbsListView.this.mDataChanged) {
                        getSkippedScrap().add(scrap);
                    } else {
                        if (this.mTransientStateViews == null) {
                            this.mTransientStateViews = new SparseArray();
                        }
                        this.mTransientStateViews.put(position, scrap);
                    }
                } else if (viewType != -2) {
                    getSkippedScrap().add(scrap);
                }
            }
        }

        private ArrayList<View> getSkippedScrap() {
            if (this.mSkippedScrap == null) {
                this.mSkippedScrap = new ArrayList();
            }
            return this.mSkippedScrap;
        }

        void removeSkippedScrap() {
            if (this.mSkippedScrap != null) {
                int count = this.mSkippedScrap.size();
                for (int i = 0; i < count; i++) {
                    removeDetachedView((View) this.mSkippedScrap.get(i), false);
                }
                this.mSkippedScrap.clear();
            }
        }

        void scrapActiveViews() {
            boolean hasListener;
            boolean multipleScraps;
            View[] activeViews = this.mActiveViews;
            if (this.mRecyclerListener != null) {
                hasListener = true;
            } else {
                hasListener = false;
            }
            if (this.mViewTypeCount > 1) {
                multipleScraps = true;
            } else {
                multipleScraps = false;
            }
            ArrayList<View> scrapViews = this.mCurrentScrap;
            for (int i = activeViews.length - 1; i >= 0; i--) {
                View victim = activeViews[i];
                if (victim != null) {
                    LayoutParams lp = (LayoutParams) victim.getLayoutParams();
                    int whichScrap = lp.viewType;
                    activeViews[i] = null;
                    if (victim.hasTransientState()) {
                        victim.dispatchStartTemporaryDetach();
                        if (AbsListView.this.mAdapter != null && AbsListView.this.mAdapterHasStableIds) {
                            if (this.mTransientStateViewsById == null) {
                                this.mTransientStateViewsById = new LongSparseArray();
                            }
                            this.mTransientStateViewsById.put(AbsListView.this.mAdapter.getItemId(this.mFirstActivePosition + i), victim);
                        } else if (!AbsListView.this.mDataChanged) {
                            if (this.mTransientStateViews == null) {
                                this.mTransientStateViews = new SparseArray();
                            }
                            this.mTransientStateViews.put(this.mFirstActivePosition + i, victim);
                        } else if (whichScrap != -2) {
                            removeDetachedView(victim, false);
                        }
                    } else if (shouldRecycleViewType(whichScrap)) {
                        if (multipleScraps) {
                            scrapViews = this.mScrapViews[whichScrap];
                        }
                        victim.dispatchStartTemporaryDetach();
                        lp.scrappedFromPosition = this.mFirstActivePosition + i;
                        scrapViews.add(victim);
                        if (hasListener) {
                            this.mRecyclerListener.onMovedToScrapHeap(victim);
                        }
                    } else if (whichScrap != -2) {
                        removeDetachedView(victim, false);
                    }
                }
            }
            pruneScrapViews();
        }

        private void pruneScrapViews() {
            int i;
            View v;
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
                    removeDetachedView((View) scrapPile.remove(size2), false);
                    j++;
                    size2 = size;
                }
            }
            SparseArray<View> transViewsByPos = this.mTransientStateViews;
            if (transViewsByPos != null) {
                i = 0;
                while (i < transViewsByPos.size()) {
                    v = (View) transViewsByPos.valueAt(i);
                    if (!v.hasTransientState()) {
                        removeDetachedView(v, false);
                        transViewsByPos.removeAt(i);
                        i--;
                    }
                    i++;
                }
            }
            LongSparseArray<View> transViewsById = this.mTransientStateViewsById;
            if (transViewsById != null) {
                i = 0;
                while (i < transViewsById.size()) {
                    v = (View) transViewsById.valueAt(i);
                    if (!v.hasTransientState()) {
                        removeDetachedView(v, false);
                        transViewsById.removeAt(i);
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

        private View retrieveFromScrap(ArrayList<View> scrapViews, int position) {
            int size = scrapViews.size();
            if (size <= 0) {
                return null;
            }
            View scrap;
            for (int i = 0; i < size; i++) {
                LayoutParams params = (LayoutParams) ((View) scrapViews.get(i)).getLayoutParams();
                if (AbsListView.this.mAdapterHasStableIds) {
                    if (AbsListView.this.mAdapter.getItemId(position) == params.itemId) {
                        return (View) scrapViews.remove(i);
                    }
                } else if (params.scrappedFromPosition == position) {
                    scrap = (View) scrapViews.remove(i);
                    clearAccessibilityFromScrap(scrap);
                    return scrap;
                }
            }
            scrap = (View) scrapViews.remove(size - 1);
            clearAccessibilityFromScrap(scrap);
            return scrap;
        }

        private void clearScrap(ArrayList<View> scrap) {
            int scrapCount = scrap.size();
            for (int j = 0; j < scrapCount; j++) {
                removeDetachedView((View) scrap.remove((scrapCount - 1) - j), false);
            }
        }

        private void clearAccessibilityFromScrap(View view) {
            view.clearAccessibilityFocus();
            view.setAccessibilityDelegate(null);
        }

        private void removeDetachedView(View child, boolean animate) {
            child.setAccessibilityDelegate(null);
            AbsListView.this.removeDetachedView(child, animate);
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
        int height;
        boolean inActionMode;
        int position;
        long selectedId;
        int viewTop;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.selectedId = in.readLong();
            this.firstId = in.readLong();
            this.viewTop = in.readInt();
            this.position = in.readInt();
            this.height = in.readInt();
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
            out.writeInt(this.viewTop);
            out.writeInt(this.position);
            out.writeInt(this.height);
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
            return "AbsListView.SavedState{" + Integer.toHexString(System.identityHashCode(this)) + " selectedId=" + this.selectedId + " firstId=" + this.firstId + " viewTop=" + this.viewTop + " position=" + this.position + " height=" + this.height + " filter=" + this.filter + " checkState=" + this.checkState + "}";
        }
    }

    public interface SelectionBoundsAdjuster {
        void adjustListItemSelectionBounds(Rect rect);
    }

    private class TwSmoothScrollByMove implements Runnable {
        private TwSmoothScrollByMove() {
        }

        public void run() {
            if (AbsListView.this.mFlingRunnable.mScroller.isFinished()) {
                if (!AbsListView.this.mTwTwScrollRemains.isEmpty()) {
                    AbsListView.this.smoothScrollBy(((Integer) AbsListView.this.mTwTwScrollRemains.poll()).intValue(), 0, false);
                } else {
                    return;
                }
            }
            AbsListView.this.post(this);
        }
    }

    abstract void fillGap(boolean z);

    abstract int findMotionRow(int i);

    abstract void setSelectionInt(int i);

    private void releaseAllBoosters() {
        if (this.mDVFSLockAcquired) {
            DVFSHelper.onScrollEvent(false);
            this.mDVFSLockAcquired = false;
        }
    }

    public void registerIRMotion() {
    }

    public void unregisterIRMotion() {
    }

    public void setAirScrollEnable(boolean enabled) {
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

    public AbsListView(Context context) {
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
        this.mWidthMeasureSpec = 0;
        this.mTouchMode = -1;
        this.mSelectedTop = 0;
        this.mSmoothScrollbarEnabled = true;
        this.mResurrectToPosition = -1;
        this.mContextMenuInfo = null;
        this.mLastTouchMode = -1;
        this.mScrollProfilingStarted = false;
        this.mFlingProfilingStarted = false;
        this.mScrollStrictSpan = null;
        this.mFlingStrictSpan = null;
        this.mIsFirstPenClick = false;
        this.mIsMovedbeforeUP = false;
        this.mLastScrollState = 0;
        this.mVelocityScale = 1.0f;
        this.mIsScrap = new boolean[1];
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mTmpPoint = new float[2];
        this.mNestedYOffset = 0;
        this.mActivePointerId = -1;
        this.mPointerCount = 0;
        this.mHapticOverScroll = false;
        this.mDirection = 0;
        this.mHoverTopAreaHeight = 0;
        this.mHoverBottomAreaHeight = 0;
        this.mHoverRecognitionDurationTime = 0;
        this.mHoverRecognitionCurrentTime = 0;
        this.mHoverRecognitionStartTime = 0;
        this.mHoverScrollTimeInterval = 300;
        this.mPenDragScrollTimeInterval = 500;
        this.mHoverScrollStartTime = 0;
        this.mHoverScrollDirection = -1;
        this.mIsHoverOverscrolled = false;
        this.mHoverScrollEnable = true;
        this.mHoverAreaEnter = false;
        this.mIsSendHoverScrollState = false;
        this.HOVERSCROLL_SPEED = 800.0f;
        this.HOVERSCROLL_DELAY = 0;
        this.QC_ICON_HIDE_DELAY = 30;
        this.mNeedsHoverScroll = false;
        this.mHoverScrollStateForListener = 0;
        this.mIsEnabledPaddingInHoverScroll = false;
        this.mExtraPaddingInTopHoverArea = 0;
        this.mExtraPaddingInBottomHoverArea = 0;
        this.mChnagedAdapter = false;
        this.mWindowFocusChanged = false;
        this.mInitAbsListView = false;
        this.mJumpAtFirst = false;
        this.mTwCustomMultiChoiceMode = false;
        this.mIsCtrlkeyPressed = false;
        this.mIsShiftkeyPressed = false;
        this.mIsPenHovered = false;
        this.mIsPenPressed = false;
        this.mIsfirstMoveEvent = true;
        this.mIsMultiFocusEnabled = false;
        this.mFirstPressedPoint = -1;
        this.mSecondPressedPoint = -1;
        this.mOldAdapterItemCount = 0;
        this.mOldKeyCode = 0;
        this.mCurrentKeyCode = 0;
        this.mTwCurrentFocusPosition = -1;
        this.mMultiSelectionStart = false;
        this.mIsTextSelectionStarted = false;
        this.mIsNeedPenSelection = false;
        this.mTwDragSelectedItemSize = 0;
        this.mTwDragSelectedViewPosition = -1;
        this.mIsPenSelectPointerSetted = false;
        this.mIsNeedPenSelectIconSet = false;
        this.mOldTextViewHoverState = false;
        this.mNewTextViewHoverState = false;
        this.mPreviousTextViewScroll = false;
        this.mIsDragBlockEnabled = false;
        this.mTwDragStartX = 0;
        this.mTwDragStartY = 0;
        this.mTwDragEndX = 0;
        this.mTwDragEndY = 0;
        this.mTwDragBlockLeft = 0;
        this.mTwDragBlockTop = 0;
        this.mTwDragBlockRight = 0;
        this.mTwDragBlockBottom = 0;
        this.mTwTrackedChild = null;
        this.mTwTrackedChildPosition = -1;
        this.mTwDistanceFromTrackedChildTop = 0;
        this.mIsCloseChildSetted = false;
        this.mOldHoverScrollDirection = -1;
        this.mTwCloseChildByTop = null;
        this.mTwCloseChildPositionByTop = -1;
        this.mTwDistanceFromCloseChildTop = 0;
        this.mTwCloseChildByBottom = null;
        this.mTwCloseChildPositionByBottom = -1;
        this.mTwDistanceFromCloseChildBottom = 0;
        this.mTwDragBlockRect = new Rect();
        this.mIsTwOnClickEnabled = true;
        this.SWITCH_CONTROL_FLING = 4000;
        this.SWITCH_CONTROL_SCROLL_DURATION_DEFAULT = 320;
        this.SWITCH_CONTROL_SCROLL_MIN_DURATION = 80;
        this.SWITCH_CONTROL_SCROLL_MAX_DURATION = DisplayMetrics.DENSITY_560;
        this.SWITCH_CONTROL_SCROLL_DURATION_GAP = 80;
        this.mDVFSLockAcquired = false;
        this.AIR_VIEW_WINSET = false;
        this.mForcedClick = false;
        this.mDragScrollWorkingZonePx = 0;
        this.mIsDragScrolled = false;
        this.mHoverPosition = -1;
        this.mHoveredOnEllipsizedText = false;
        this.mIsHoveredByMouse = false;
        this.mAlwaysDisableHoverHighlight = false;
        this.mTwSmoothScrollByMove = null;
        this.mTwTwScrollRemains = null;
        this.mHoverScrollSpeed = 0;
        this.mIsQCBtnFadeInSet = true;
        this.mIsQCBtnFadeOutSet = true;
        this.mQCBtnFadeInRunnable = new Runnable() {
            public void run() {
                AbsListView.this.playQCBtnFadeIn();
            }
        };
        this.mQCBtnFadeOutRunnable = new Runnable() {
            public void run() {
                AbsListView.this.playQCBtnFadeOut();
            }
        };
        this.mQCLocation = -1;
        this.mQCstate = 0;
        this.mIsQCShown = false;
        this.mQCScrollingCount = 1;
        this.mQCScrollRunnable = new Runnable() {
            public void run() {
                if (Math.abs(AbsListView.this.mQCScrollTo - AbsListView.this.mQCScrollNext) < 30) {
                    AbsListView.this.smoothScrollToPosition(AbsListView.this.mQCScrollTo);
                    return;
                }
                int movingDistance = (int) (((float) Math.abs(AbsListView.this.mQCScrollTo - AbsListView.this.mQCScrollFrom)) * AbsListView.this.mScrollInterpolator.getInterpolation(((float) AbsListView.this.mQCScrollingCount) / 5.0f));
                AbsListView.this.mQCScrollingCount = AbsListView.this.mQCScrollingCount + 1;
                if (AbsListView.this.mQCScrollDirection == 2) {
                    AbsListView.this.mQCScrollNext = movingDistance;
                    AbsListView.this.setSelection(AbsListView.this.mQCScrollNext);
                } else {
                    AbsListView.this.mQCScrollNext = AbsListView.this.mQCScrollFrom + movingDistance;
                    AbsListView.this.setSelection(AbsListView.this.mQCScrollNext);
                }
                AbsListView.this.postDelayed(AbsListView.this.mQCScrollRunnable, 40);
            }
        };
        initAbsListView();
        this.mOwnerThread = Thread.currentThread();
        setVerticalScrollBarEnabled(true);
        TypedArray a = context.obtainStyledAttributes(R.styleable.View);
        initializeScrollbarsInternal(a);
        a.recycle();
        if (this.mMotionRecognitionManager == null) {
            Log.d(TAG, "Get MotionRecognitionManager");
            this.mMotionRecognitionManager = (MotionRecognitionManager) context.getSystemService("motion_recognition");
        }
    }

    public AbsListView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.absListViewStyle);
    }

    public AbsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AbsListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        this.mWidthMeasureSpec = 0;
        this.mTouchMode = -1;
        this.mSelectedTop = 0;
        this.mSmoothScrollbarEnabled = true;
        this.mResurrectToPosition = -1;
        this.mContextMenuInfo = null;
        this.mLastTouchMode = -1;
        this.mScrollProfilingStarted = false;
        this.mFlingProfilingStarted = false;
        this.mScrollStrictSpan = null;
        this.mFlingStrictSpan = null;
        this.mIsFirstPenClick = false;
        this.mIsMovedbeforeUP = false;
        this.mLastScrollState = 0;
        this.mVelocityScale = 1.0f;
        this.mIsScrap = new boolean[1];
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mTmpPoint = new float[2];
        this.mNestedYOffset = 0;
        this.mActivePointerId = -1;
        this.mPointerCount = 0;
        this.mHapticOverScroll = false;
        this.mDirection = 0;
        this.mHoverTopAreaHeight = 0;
        this.mHoverBottomAreaHeight = 0;
        this.mHoverRecognitionDurationTime = 0;
        this.mHoverRecognitionCurrentTime = 0;
        this.mHoverRecognitionStartTime = 0;
        this.mHoverScrollTimeInterval = 300;
        this.mPenDragScrollTimeInterval = 500;
        this.mHoverScrollStartTime = 0;
        this.mHoverScrollDirection = -1;
        this.mIsHoverOverscrolled = false;
        this.mHoverScrollEnable = true;
        this.mHoverAreaEnter = false;
        this.mIsSendHoverScrollState = false;
        this.HOVERSCROLL_SPEED = 800.0f;
        this.HOVERSCROLL_DELAY = 0;
        this.QC_ICON_HIDE_DELAY = 30;
        this.mNeedsHoverScroll = false;
        this.mHoverScrollStateForListener = 0;
        this.mIsEnabledPaddingInHoverScroll = false;
        this.mExtraPaddingInTopHoverArea = 0;
        this.mExtraPaddingInBottomHoverArea = 0;
        this.mChnagedAdapter = false;
        this.mWindowFocusChanged = false;
        this.mInitAbsListView = false;
        this.mJumpAtFirst = false;
        this.mTwCustomMultiChoiceMode = false;
        this.mIsCtrlkeyPressed = false;
        this.mIsShiftkeyPressed = false;
        this.mIsPenHovered = false;
        this.mIsPenPressed = false;
        this.mIsfirstMoveEvent = true;
        this.mIsMultiFocusEnabled = false;
        this.mFirstPressedPoint = -1;
        this.mSecondPressedPoint = -1;
        this.mOldAdapterItemCount = 0;
        this.mOldKeyCode = 0;
        this.mCurrentKeyCode = 0;
        this.mTwCurrentFocusPosition = -1;
        this.mMultiSelectionStart = false;
        this.mIsTextSelectionStarted = false;
        this.mIsNeedPenSelection = false;
        this.mTwDragSelectedItemSize = 0;
        this.mTwDragSelectedViewPosition = -1;
        this.mIsPenSelectPointerSetted = false;
        this.mIsNeedPenSelectIconSet = false;
        this.mOldTextViewHoverState = false;
        this.mNewTextViewHoverState = false;
        this.mPreviousTextViewScroll = false;
        this.mIsDragBlockEnabled = false;
        this.mTwDragStartX = 0;
        this.mTwDragStartY = 0;
        this.mTwDragEndX = 0;
        this.mTwDragEndY = 0;
        this.mTwDragBlockLeft = 0;
        this.mTwDragBlockTop = 0;
        this.mTwDragBlockRight = 0;
        this.mTwDragBlockBottom = 0;
        this.mTwTrackedChild = null;
        this.mTwTrackedChildPosition = -1;
        this.mTwDistanceFromTrackedChildTop = 0;
        this.mIsCloseChildSetted = false;
        this.mOldHoverScrollDirection = -1;
        this.mTwCloseChildByTop = null;
        this.mTwCloseChildPositionByTop = -1;
        this.mTwDistanceFromCloseChildTop = 0;
        this.mTwCloseChildByBottom = null;
        this.mTwCloseChildPositionByBottom = -1;
        this.mTwDistanceFromCloseChildBottom = 0;
        this.mTwDragBlockRect = new Rect();
        this.mIsTwOnClickEnabled = true;
        this.SWITCH_CONTROL_FLING = 4000;
        this.SWITCH_CONTROL_SCROLL_DURATION_DEFAULT = 320;
        this.SWITCH_CONTROL_SCROLL_MIN_DURATION = 80;
        this.SWITCH_CONTROL_SCROLL_MAX_DURATION = DisplayMetrics.DENSITY_560;
        this.SWITCH_CONTROL_SCROLL_DURATION_GAP = 80;
        this.mDVFSLockAcquired = false;
        this.AIR_VIEW_WINSET = false;
        this.mForcedClick = false;
        this.mDragScrollWorkingZonePx = 0;
        this.mIsDragScrolled = false;
        this.mHoverPosition = -1;
        this.mHoveredOnEllipsizedText = false;
        this.mIsHoveredByMouse = false;
        this.mAlwaysDisableHoverHighlight = false;
        this.mTwSmoothScrollByMove = null;
        this.mTwTwScrollRemains = null;
        this.mHoverScrollSpeed = 0;
        this.mIsQCBtnFadeInSet = true;
        this.mIsQCBtnFadeOutSet = true;
        this.mQCBtnFadeInRunnable = /* anonymous class already generated */;
        this.mQCBtnFadeOutRunnable = /* anonymous class already generated */;
        this.mQCLocation = -1;
        this.mQCstate = 0;
        this.mIsQCShown = false;
        this.mQCScrollingCount = 1;
        this.mQCScrollRunnable = /* anonymous class already generated */;
        initAbsListView();
        this.mOwnerThread = Thread.currentThread();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AbsListView, defStyleAttr, defStyleRes);
        Drawable selector = a.getDrawable(0);
        if (selector != null) {
            setSelector(selector);
        }
        this.mDrawSelectorOnTop = a.getBoolean(1, false);
        setStackFromBottom(a.getBoolean(2, false));
        setScrollingCacheEnabled(a.getBoolean(3, true));
        setTextFilterEnabled(a.getBoolean(4, false));
        setTranscriptMode(a.getInt(5, 0));
        setCacheColorHint(a.getColor(6, 0));
        setFastScrollEnabled(a.getBoolean(8, false));
        setFastScrollStyle(a.getResourceId(11, 0));
        setSmoothScrollbarEnabled(a.getBoolean(9, true));
        setChoiceMode(a.getInt(7, 0));
        setFastScrollAlwaysVisible(a.getBoolean(10, false));
        a.recycle();
        if (this.mMotionRecognitionManager == null) {
            Log.d(TAG, "Get MotionRecognitionManager");
            this.mMotionRecognitionManager = (MotionRecognitionManager) context.getSystemService("motion_recognition");
        }
    }

    private void initAbsListView() {
        boolean z = false;
        this.mInitAbsListView = true;
        setClickable(true);
        setFocusableInTouchMode(true);
        setWillNotDraw(false);
        setAlwaysDrawnWithCacheEnabled(false);
        setScrollingCacheEnabled(true);
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
        TypedValue value = new TypedValue();
        if (this.mContext != null) {
            if (this.mContext.getTheme().resolveAttribute(R.attr.twListMultiSelectBackground, value, true)) {
                this.mMultiFocusImage = this.mContext.getResources().getDrawable(value.resourceId);
            }
            if (this.mContext.getTheme().resolveAttribute(R.attr.twDragBlockImage, value, true)) {
                this.mTwDragBlockImage = this.mContext.getResources().getDrawable(value.resourceId);
            }
        }
    }

    public void setOverScrollMode(int mode) {
        if (mode == 2) {
            this.mEdgeGlowTop = null;
            this.mEdgeGlowBottom = null;
        } else if (this.mEdgeGlowTop == null) {
            Context context = getContext();
            this.mEdgeGlowTop = new EdgeEffect(context);
            this.mEdgeGlowBottom = new EdgeEffect(context);
        }
        super.setOverScrollMode(mode);
    }

    public void setAdapter(ListAdapter adapter) {
        if (adapter != null) {
            this.mAdapterHasStableIds = this.mAdapter.hasStableIds();
            if (this.mChoiceMode != 0 && this.mAdapterHasStableIds && this.mCheckedIdStates == null) {
                this.mCheckedIdStates = new LongSparseArray();
            }
            this.mChnagedAdapter = true;
        }
        if (this.mCheckStates != null) {
            this.mCheckStates.clear();
        }
        if (this.mCheckedIdStates != null) {
            this.mCheckedIdStates.clear();
        }
        if (this.mIsMultiFocusEnabled && this.mAdapter != null) {
            this.mTwPressItemListArray = new ArrayList();
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
                    throw new IllegalStateException("AbsListView: attempted to start selection mode for CHOICE_MODE_MULTIPLE_MODAL but no choice mode callback was supplied. Call setMultiChoiceModeListener to set a callback.");
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
        if (!this.mIsShiftkeyPressed) {
            return false;
        }
        return super.twNotifyKeyPress(view, position, id, this.mIsShiftkeyPressed);
    }

    private boolean twNotifyMultiSelectState(View view, int position, long id) {
        return super.twNotifyMultiSelectedState(view, position, id, this.mIsShiftkeyPressed, this.mIsCtrlkeyPressed, this.mIsPenPressed);
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
        if (this.mChoiceMode == 2) {
            this.mIsDragBlockEnabled = true;
        } else if (this.mChoiceMode == 3) {
            this.mIsDragBlockEnabled = true;
        } else if (this.mChoiceMode == 0 || this.mChoiceMode == 1) {
            this.mIsDragBlockEnabled = false;
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
        if (getChildAt(0).getTop() < this.mListPadding.top || getChildAt(childCount - 1).getBottom() > getHeight() - this.mListPadding.bottom) {
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
                        AbsListView.this.setFastScrollerEnabledUiThread(enabled);
                    }
                });
            }
        }
    }

    private void setFastScrollerEnabledUiThread(boolean enabled) {
        if (this.mFastScroll != null) {
            this.mFastScroll.setEnabled(enabled);
        } else if (enabled) {
            this.mFastScroll = new FastScroller(this, this.mFastScrollStyle);
            this.mFastScroll.setEnabled(true);
        }
        resolvePadding();
        if (this.mFastScroll != null) {
            this.mFastScroll.updateLayout();
        }
    }

    public void setFastScrollStyle(int styleResId) {
        if (this.mFastScroll == null) {
            this.mFastScrollStyle = styleResId;
        } else {
            this.mFastScroll.setStyle(styleResId);
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
                        AbsListView.this.setFastScrollerAlwaysVisibleUiThread(alwaysShow);
                    }
                });
            }
        }
    }

    private void setFastScrollerAlwaysVisibleUiThread(boolean alwaysShow) {
        if (this.mFastScroll != null) {
            this.mFastScroll.setAlwaysShow(alwaysShow);
        }
    }

    private boolean isOwnerThread() {
        return this.mOwnerThread == Thread.currentThread();
    }

    @ExportedProperty
    public boolean isFastScrollAlwaysVisible() {
        if (this.mFastScroll == null) {
            if (this.mFastScrollEnabled && this.mFastScrollAlwaysVisible) {
                return true;
            }
            return false;
        } else if (this.mFastScroll.isEnabled() && this.mFastScroll.isAlwaysShowEnabled()) {
            return true;
        } else {
            return false;
        }
    }

    public int getVerticalScrollbarWidth() {
        if (this.mFastScroll != null && this.mFastScroll.isEnabled()) {
            return Math.max(super.getVerticalScrollbarWidth(), this.mFastScroll.getWidth());
        }
        if (this.mTwFluidScroll == null || !this.mTwFluidScroll.isEnabled()) {
            return super.getVerticalScrollbarWidth();
        }
        return Math.max(super.getVerticalScrollbarWidth(), this.mTwFluidScroll.getWidth());
    }

    @ExportedProperty
    public boolean isFastScrollEnabled() {
        if (this.mFastScroll == null) {
            return this.mFastScrollEnabled;
        }
        return this.mFastScroll.isEnabled();
    }

    public void setVerticalScrollbarPosition(int position) {
        super.setVerticalScrollbarPosition(position);
        if (this.mFastScroll != null) {
            this.mFastScroll.setScrollbarPosition(position);
        } else if (this.mTwFluidScroll != null) {
            this.mTwFluidScroll.setScrollbarPosition(position);
        }
    }

    public void setScrollBarStyle(int style) {
        super.setScrollBarStyle(style);
        if (this.mFastScroll != null) {
            this.mFastScroll.setScrollBarStyle(style);
        }
    }

    protected boolean isVerticalScrollBarHidden() {
        return isFastScrollEnabled() || twIsFluidScrollEnabled();
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
        if (this.mFastScroll != null) {
            this.mFastScroll.onScroll(this.mFirstPosition, getChildCount(), this.mItemCount);
        } else if (this.mTwFluidScroll != null) {
            this.mTwFluidScroll.onScroll(this.mFirstPosition, getChildCount(), this.mItemCount);
        }
        if (this.mOnScrollListener != null) {
            this.mOnScrollListener.onScroll(this, this.mFirstPosition, getChildCount(), this.mItemCount);
        }
        onScrollChanged(0, 0, 0, 0);
    }

    public void sendAccessibilityEventInternal(int eventType) {
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
        super.sendAccessibilityEventInternal(eventType);
    }

    public CharSequence getAccessibilityClassName() {
        return AbsListView.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (isEnabled()) {
            if (canScrollUp()) {
                info.addAction(AccessibilityAction.ACTION_SCROLL_BACKWARD);
                info.addAction(AccessibilityAction.ACTION_SCROLL_UP);
                info.setScrollable(true);
            }
            if (canScrollDown()) {
                info.addAction(AccessibilityAction.ACTION_SCROLL_FORWARD);
                info.addAction(AccessibilityAction.ACTION_SCROLL_DOWN);
                info.setScrollable(true);
            }
        }
    }

    int getSelectionModeForAccessibility() {
        switch (getChoiceMode()) {
            case 1:
                return 1;
            case 2:
            case 3:
                return 2;
            default:
                return 0;
        }
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        switch (action) {
            case 4096:
            case R.id.accessibilityActionScrollDown /*16908346*/:
                if (!isEnabled() || (getLastVisiblePosition() >= getCount() - 1 && (getLastVisiblePosition() != getCount() - 1 || !canScrollDown()))) {
                    return false;
                }
                smoothScrollBy((getHeight() - this.mListPadding.top) - this.mListPadding.bottom, 200);
                return true;
            case 8192:
            case R.id.accessibilityActionScrollUp /*16908344*/:
                if (!isEnabled() || this.mFirstPosition <= 0) {
                    return false;
                }
                smoothScrollBy(-((getHeight() - this.mListPadding.top) - this.mListPadding.bottom), 200);
                return true;
            case 4194304:
                Log.d(TAG, "case ACTION_AUTOSCROLL_ON, canScrollDown = " + canScrollDown());
                if (!canScrollDown()) {
                    return false;
                }
                this.mAutoscrollDuration = 320;
                autoScrollWithDuration(this.mAutoscrollDuration);
                return true;
            case 8388608:
                Log.d(TAG, "ACTION_AUTOSCROLL_OFF");
                smoothScrollBy(0, 0);
                if (this.mPositionScroller == null) {
                    return true;
                }
                this.mPositionScroller.stop();
                return true;
            case 16777216:
                Log.d(TAG, "case ACTION_AUTOSCROLL_UP  canScrollUp:" + canScrollUp());
                if (!canScrollUp()) {
                    return false;
                }
                fling(-4000);
                return true;
            case 33554432:
                Log.d(TAG, "case ACTION_AUTOSCROLL_DOWN  canScrollDown():" + canScrollDown());
                if (!canScrollDown()) {
                    return false;
                }
                fling(4000);
                return true;
            case 67108864:
                Log.d(TAG, "ACTION_AUTOSCROLL_TOP");
                if (!canScrollUp()) {
                    return false;
                }
                smoothScrollToPositionFromTop(0, 0, 0);
                return true;
            case 268435456:
                Log.d(TAG, "ACTION_AUTOSCROLL_SPEED_UP, current duration = " + this.mAutoscrollDuration);
                if (!canScrollDown()) {
                    return false;
                }
                if (this.mAutoscrollDuration > 80) {
                    this.mAutoscrollDuration -= 80;
                }
                autoScrollWithDuration(this.mAutoscrollDuration);
                return true;
            case 536870912:
                Log.d(TAG, "ACTION_AUTOSCROLL_SPEED_DOWN, current duration = " + this.mAutoscrollDuration);
                if (!canScrollDown()) {
                    return false;
                }
                if (this.mAutoscrollDuration < DisplayMetrics.DENSITY_560) {
                    this.mAutoscrollDuration += 80;
                }
                autoScrollWithDuration(this.mAutoscrollDuration);
                return true;
            default:
                return false;
        }
    }

    private void autoScrollWithDuration(int duration) {
        int firstPosition = getFirstVisiblePosition();
        View mFirst = getChildAt(firstPosition);
        View mLast = getChildAt(getLastVisiblePosition());
        int height = 0;
        int mCount = getCount();
        Log.d(TAG, "autoScrollWithDuration:CASE  mFirst " + mFirst + "mCount is" + mCount);
        if (mFirst != null) {
            height = mFirst.getHeight();
        }
        if (mLast != null) {
            height += mLast.getHeight();
        }
        Log.d(TAG, "autoScrollWithDuration:CASEE  height > 0 && getAdapter()!= null && mCount > 0 ");
        int tempdur = duration * (mCount - firstPosition);
        Log.d(TAG, "autoScrollWithDuration(), duration = " + tempdur);
        smoothScrollToPositionFromTop(mCount - 1, ((firstPosition * height) / 2) * -1, tempdur);
    }

    public View findViewByAccessibilityIdTraversal(int accessibilityId) {
        return accessibilityId == getAccessibilityViewId() ? this : super.findViewByAccessibilityIdTraversal(accessibilityId);
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
        setSelector(getContext().getDrawable(R.drawable.list_selector_background));
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
            ss.viewTop = this.mPendingSync.viewTop;
            ss.position = this.mPendingSync.position;
            ss.height = this.mPendingSync.height;
            ss.filter = this.mPendingSync.filter;
            ss.inActionMode = this.mPendingSync.inActionMode;
            ss.checkedItemCount = this.mPendingSync.checkedItemCount;
            ss.checkState = this.mPendingSync.checkState;
            ss.checkIdState = this.mPendingSync.checkIdState;
        } else {
            boolean haveChildren = getChildCount() > 0 && this.mItemCount > 0;
            long selectedId = getSelectedItemId();
            ss.selectedId = selectedId;
            ss.height = getHeight();
            if (selectedId >= 0) {
                ss.viewTop = this.mSelectedTop;
                ss.position = getSelectedItemPosition();
                ss.firstId = -1;
            } else if (!haveChildren || this.mFirstPosition <= 0) {
                ss.viewTop = 0;
                ss.firstId = -1;
                ss.position = 0;
            } else {
                ss.viewTop = getChildAt(0).getTop();
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
        this.mSyncHeight = (long) ss.height;
        if (ss.selectedId >= 0) {
            this.mNeedSync = true;
            this.mPendingSync = ss;
            this.mSyncRowId = ss.selectedId;
            this.mSyncPosition = ss.position;
            this.mSpecificTop = ss.viewTop;
            this.mSyncMode = 0;
        } else if (ss.firstId >= 0) {
            setSelectedPositionInt(-1);
            setNextSelectedPositionInt(-1);
            this.mSelectorPosition = -1;
            this.mNeedSync = true;
            this.mPendingSync = ss;
            this.mSyncRowId = ss.firstId;
            this.mSyncPosition = ss.position;
            this.mSpecificTop = ss.viewTop;
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
        this.mSelectedTop = 0;
        this.mSelectorPosition = -1;
        this.mSelectorRect.setEmpty();
        invalidate();
    }

    protected int computeVerticalScrollExtent() {
        int count = getChildCount();
        if (count <= 0) {
            return 0;
        }
        if (!this.mSmoothScrollbarEnabled) {
            return 1;
        }
        int extent = count * 100;
        View view = getChildAt(0);
        int top = view.getTop();
        int height = view.getHeight();
        if (height > 0) {
            extent += (top * 100) / height;
        }
        view = getChildAt(count - 1);
        int bottom = view.getBottom();
        height = view.getHeight();
        if (height > 0) {
            return extent - (((bottom - getHeight()) * 100) / height);
        }
        return extent;
    }

    protected int computeVerticalScrollOffset() {
        int firstPosition = this.mFirstPosition;
        int childCount = getChildCount();
        if (firstPosition < 0 || childCount <= 0) {
            return 0;
        }
        if (this.mSmoothScrollbarEnabled) {
            View view = getChildAt(0);
            int top = view.getTop();
            int height = view.getHeight();
            if (height > 0) {
                return Math.max(((firstPosition * 100) - ((top * 100) / height)) + ((int) (((((float) this.mScrollY) / ((float) getHeight())) * ((float) this.mItemCount)) * 100.0f)), 0);
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

    protected int computeVerticalScrollRange() {
        if (!this.mSmoothScrollbarEnabled) {
            return this.mItemCount;
        }
        int result = Math.max(this.mItemCount * 100, 0);
        if (this.mScrollY != 0) {
            return result + Math.abs((int) (((((float) this.mScrollY) / ((float) getHeight())) * ((float) this.mItemCount)) * 100.0f));
        }
        return result;
    }

    protected float getTopFadingEdgeStrength() {
        int count = getChildCount();
        float fadeEdge = super.getTopFadingEdgeStrength();
        if (count == 0) {
            return fadeEdge;
        }
        if (this.mFirstPosition > 0) {
            return 1.0f;
        }
        int top = getChildAt(0).getTop();
        return top < this.mPaddingTop ? ((float) (-(top - this.mPaddingTop))) / ((float) getVerticalFadingEdgeLength()) : fadeEdge;
    }

    protected float getBottomFadingEdgeStrength() {
        int count = getChildCount();
        float fadeEdge = super.getBottomFadingEdgeStrength();
        if (count == 0) {
            return fadeEdge;
        }
        if ((this.mFirstPosition + count) - 1 < this.mItemCount - 1) {
            return 1.0f;
        }
        int bottom = getChildAt(count - 1).getBottom();
        int height = getHeight();
        return bottom > height - this.mPaddingBottom ? ((float) ((bottom - height) + this.mPaddingBottom)) / ((float) getVerticalFadingEdgeLength()) : fadeEdge;
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
            int listBottom = getHeight() - getPaddingBottom();
            View lastChild = getChildAt(childCount - 1);
            int lastBottom;
            if (lastChild != null) {
                lastBottom = lastChild.getBottom();
            } else {
                lastBottom = listBottom;
            }
            if (this.mFirstPosition + childCount < this.mLastHandledItemCount || lastBottom > listBottom) {
                z = false;
            }
            this.mForceTranscriptScroll = z;
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mInLayout = true;
        int childCount = getChildCount();
        if (changed) {
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).forceLayout();
            }
            this.mRecycler.markChildrenDirty();
        }
        layoutChildren();
        this.mInLayout = false;
        this.mOverscrollMax = (b - t) / 3;
        if (this.mFastScroll != null) {
            this.mFastScroll.onItemCountChanged(getChildCount(), this.mItemCount);
        } else if (this.mTwFluidScroll != null) {
            this.mTwFluidScroll.onItemCountChanged(getChildCount(), this.mItemCount);
        }
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

    View getAccessibilityFocusedChild(View focusedView) {
        View viewParent = focusedView.getParent();
        while ((viewParent instanceof View) && viewParent != this) {
            focusedView = viewParent;
            viewParent = viewParent.getParent();
        }
        if (viewParent instanceof View) {
            return focusedView;
        }
        return null;
    }

    void updateScrollIndicators() {
        int i = 0;
        if (this.mScrollUp != null) {
            this.mScrollUp.setVisibility(canScrollUp() ? 0 : 4);
        }
        if (this.mScrollDown != null) {
            View view = this.mScrollDown;
            if (!canScrollDown()) {
                i = 4;
            }
            view.setVisibility(i);
        }
    }

    private boolean canScrollUp() {
        boolean canScrollUp;
        if (this.mFirstPosition > 0) {
            canScrollUp = true;
        } else {
            canScrollUp = false;
        }
        if (canScrollUp || getChildCount() <= 0) {
            return canScrollUp;
        }
        if (getChildAt(0).getTop() < this.mListPadding.top) {
            return true;
        }
        return false;
    }

    private boolean canScrollDown() {
        boolean canScrollDown;
        int count = getChildCount();
        if (this.mFirstPosition + count < this.mItemCount) {
            canScrollDown = true;
        } else {
            canScrollDown = false;
        }
        if (canScrollDown || count <= 0) {
            return canScrollDown;
        }
        if (getChildAt(count - 1).getBottom() > this.mBottom - this.mListPadding.bottom) {
            return true;
        }
        return false;
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
        Trace.traceBegin(8, "obtainView");
        isScrap[0] = false;
        int viewType = this.mAdapter.getItemViewType(position);
        View transientView = this.mRecycler.getTransientStateView(position);
        if (transientView != null) {
            if (((LayoutParams) transientView.getLayoutParams()).viewType == viewType) {
                View updatedView = this.mAdapter.getView(position, transientView, this);
                if (updatedView != transientView) {
                    setItemViewLayoutParams(updatedView, position);
                    this.mRecycler.addScrapView(updatedView, position);
                }
            }
            isScrap[0] = true;
            transientView.dispatchFinishTemporaryDetach();
            return transientView;
        }
        View scrapView = null;
        if (viewType != -2) {
            scrapView = this.mRecycler.getScrapView(position);
        } else {
            Log.d(TAG, "viewType is heaer or footer");
        }
        View child = this.mAdapter.getView(position, scrapView, this);
        if (scrapView != null) {
            if (child != scrapView) {
                this.mRecycler.addScrapView(scrapView, position);
            } else {
                isScrap[0] = true;
                child.dispatchFinishTemporaryDetach();
            }
        }
        if (child == null) {
            Log.d(TAG, " try again to check child on obtainview");
            child = this.mAdapter.getView(position, null, this);
            if (child == null) {
                Log.d(TAG, " child is null again");
                Log.d(TAG, " position = " + position);
                Log.d(TAG, " mAdapter =" + this.mAdapter);
                Log.d(TAG, " getChildCount = " + getChildCount());
                Log.d(TAG, " mAdapter.getCount = " + this.mAdapter.getCount());
                return null;
            }
        }
        if (this.mCacheColorHint != 0) {
            child.setDrawingCacheBackgroundColor(this.mCacheColorHint);
        }
        if (child.getImportantForAccessibility() == 0) {
            child.setImportantForAccessibility(1);
        }
        setItemViewLayoutParams(child, position);
        if (this.mAccessibilityDelegate == null) {
            this.mAccessibilityDelegate = new ListItemAccessibilityDelegate();
        }
        if (child.getAccessibilityDelegate() == null) {
            child.setAccessibilityDelegate(this.mAccessibilityDelegate);
        }
        Trace.traceEnd(8);
        return child;
    }

    private void setItemViewLayoutParams(View child, int position) {
        android.view.ViewGroup.LayoutParams lp;
        android.view.ViewGroup.LayoutParams vlp = child.getLayoutParams();
        if (vlp == null) {
            lp = (LayoutParams) generateDefaultLayoutParams();
        } else if (checkLayoutParams(vlp)) {
            lp = (LayoutParams) vlp;
        } else {
            lp = (LayoutParams) generateLayoutParams(vlp);
        }
        if (this.mAdapterHasStableIds) {
            lp.itemId = this.mAdapter.getItemId(position);
        }
        lp.viewType = this.mAdapter.getItemViewType(position);
        lp.isEnabled = this.mAdapter.isEnabled(position);
        if (lp != vlp) {
            child.setLayoutParams(lp);
        }
    }

    public void onInitializeAccessibilityNodeInfoForItem(View view, int position, AccessibilityNodeInfo info) {
        if (position != -1) {
            android.view.ViewGroup.LayoutParams lp = view.getLayoutParams();
            boolean isItemEnabled;
            if (lp instanceof LayoutParams) {
                isItemEnabled = ((LayoutParams) lp).isEnabled;
            } else {
                isItemEnabled = false;
            }
            if (isEnabled() && isItemEnabled) {
                if (position == getSelectedItemPosition()) {
                    info.setSelected(true);
                    info.addAction(AccessibilityAction.ACTION_CLEAR_SELECTION);
                } else {
                    info.addAction(AccessibilityAction.ACTION_SELECT);
                }
                if (isClickable()) {
                    info.addAction(AccessibilityAction.ACTION_CLICK);
                    info.setClickable(true);
                }
                if (isLongClickable()) {
                    info.addAction(AccessibilityAction.ACTION_LONG_CLICK);
                    info.setLongClickable(true);
                    return;
                }
                return;
            }
            info.setEnabled(false);
        }
    }

    void positionSelectorLikeTouch(int position, View sel, float x, float y) {
        positionSelector(position, sel, true, x, y);
    }

    void positionSelectorLikeFocus(int position, View sel) {
        if (this.mSelector == null || this.mSelectorPosition == position || position == -1) {
            positionSelector(position, sel);
            return;
        }
        Rect bounds = this.mSelectorRect;
        positionSelector(position, sel, true, bounds.exactCenterX(), bounds.exactCenterY());
    }

    void positionSelector(int position, View sel) {
        positionSelector(position, sel, false, android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE, android.view.WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE);
    }

    private void positionSelector(int position, View sel, boolean manageHotspot, float x, float y) {
        boolean positionChanged = position != this.mSelectorPosition;
        if (position != -1) {
            this.mSelectorPosition = position;
        }
        Rect selectorRect = this.mSelectorRect;
        selectorRect.set(sel.getLeft(), sel.getTop(), sel.getRight(), sel.getBottom());
        if (sel instanceof SelectionBoundsAdjuster) {
            ((SelectionBoundsAdjuster) sel).adjustListItemSelectionBounds(selectorRect);
        }
        selectorRect.left -= this.mSelectionLeftPadding;
        selectorRect.top -= this.mSelectionTopPadding;
        selectorRect.right += this.mSelectionRightPadding;
        selectorRect.bottom += this.mSelectionBottomPadding - sel.mTwExtraPaddingBottomForPreference;
        Drawable selector = this.mSelector;
        if (selector != null) {
            if (positionChanged) {
                selector.setVisible(false, false);
                selector.setState(StateSet.NOTHING);
            }
            selector.setBounds(selectorRect);
            if (positionChanged) {
                if (getVisibility() == 0) {
                    selector.setVisible(true, false);
                }
                updateSelectorState();
            }
            if (manageHotspot) {
                selector.setHotspot(x, y);
            }
        }
        boolean isChildViewEnabled = this.mIsChildViewEnabled;
        if (sel.isEnabled() != isChildViewEnabled) {
            this.mIsChildViewEnabled = !isChildViewEnabled;
            if (getSelectedItemPosition() != -1) {
                refreshDrawableState();
            }
        }
    }

    protected void dispatchDraw(Canvas canvas) {
        int saveCount = 0;
        int trackChildTop = 0;
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
        if (!this.mIsDragBlockEnabled) {
            return;
        }
        if (this.mTwDragBlockLeft != 0 || this.mTwDragBlockTop != 0) {
            int firstChildPosition = getFirstVisiblePosition();
            int lastChildPosition = getLastVisiblePosition();
            if (this.mTwTrackedChildPosition >= firstChildPosition && this.mTwTrackedChildPosition <= lastChildPosition) {
                this.mTwTrackedChild = getChildAt(this.mTwTrackedChildPosition - getFirstVisiblePosition());
                if (this.mTwTrackedChild != null) {
                    trackChildTop = this.mTwTrackedChild.getTop();
                }
                this.mTwDragStartY = this.mTwDistanceFromTrackedChildTop + trackChildTop;
            }
            this.mTwDragBlockTop = this.mTwDragStartY < this.mTwDragEndY ? this.mTwDragStartY : this.mTwDragEndY;
            this.mTwDragBlockBottom = this.mTwDragEndY > this.mTwDragStartY ? this.mTwDragEndY : this.mTwDragStartY;
            this.mTwDragBlockRect.set(this.mTwDragBlockLeft, this.mTwDragBlockTop, this.mTwDragBlockRight, this.mTwDragBlockBottom);
            this.mTwDragBlockImage.setBounds(this.mTwDragBlockRect);
            this.mTwDragBlockImage.draw(canvas);
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
        if (this.mFastScroll != null) {
            this.mFastScroll.onSizeChanged(w, h, oldw, oldh);
        } else if (this.mTwFluidScroll != null) {
            this.mTwFluidScroll.onSizeChanged(w, h, oldw, oldh);
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
        return (isFocused() && !isInTouchMode()) || (touchModeDrawsInPressedState() && isPressed());
    }

    private void drawSelector(Canvas canvas) {
        Rect tempSelectorRect = new Rect();
        if (!this.mSelectorRect.isEmpty()) {
            Drawable selector = this.mSelector;
            selector.setBounds(this.mSelectorRect);
            selector.draw(canvas);
        }
        if (this.mIsMultiFocusEnabled) {
            Iterator i$ = this.mTwPressItemListArray.iterator();
            while (i$.hasNext()) {
                View selectedChild = getChildAt(((Integer) i$.next()).intValue() - this.mFirstPosition);
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
        setSelector(getContext().getDrawable(resID));
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
                Rect bounds = this.mSelectorRect;
                float x = bounds.exactCenterX();
                float y = bounds.exactCenterY();
                boolean longClickable = isLongClickable();
                Drawable d = selector.getCurrent();
                if (d != null && (d instanceof TransitionDrawable)) {
                    if (longClickable) {
                        ((TransitionDrawable) d).startTransition(ViewConfiguration.getLongPressTimeout());
                    } else {
                        ((TransitionDrawable) d).resetTransition();
                    }
                }
                this.mSelector.setHotspot(x, y);
                if (longClickable && !this.mDataChanged) {
                    if (this.mPendingCheckForKeyLongPress == null) {
                        this.mPendingCheckForKeyLongPress = new CheckForKeyLongPress();
                    } else {
                        removeCallbacks(this.mPendingCheckForKeyLongPress);
                    }
                    this.mPendingCheckForKeyLongPress.rememberWindowAttachCount();
                    postDelayed(this.mPendingCheckForKeyLongPress, (long) ViewConfiguration.getLongPressTimeout());
                }
            }
        }
    }

    public void setScrollIndicators(View up, View down) {
        this.mScrollUp = up;
        this.mScrollDown = down;
    }

    void updateSelectorState() {
        if (this.mSelector == null) {
            return;
        }
        if (!shouldShowSelector()) {
            this.mSelector.setState(StateSet.NOTHING);
        } else if (!isHovered() || this.mIsHoveredByMouse || this.mSelectorPosition < this.mFirstPosition) {
            this.mSelector.setState(getDrawableStateForSelector());
        } else {
            View child = getChildAt(this.mSelectorPosition - this.mFirstPosition);
            boolean ellipsized = findEllipsizedTextView(child);
            if (this.mIsPenHovered || (!this.mAlwaysDisableHoverHighlight && ellipsized && (child == null || child.isEnabled()))) {
                this.mSelector.setState(getDrawableStateForSelector());
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

    private int[] getDrawableStateForSelector() {
        if (this.mIsChildViewEnabled) {
            return super.getDrawableState();
        }
        int enabledState = ENABLED_STATE_SET[0];
        int[] state = onCreateDrawableState(1);
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
        if (isLayoutRtl() && this.mFastScroll != null) {
            this.mFastScroll.setScrollbarPosition(getVerticalScrollbarPosition());
        } else if (isLayoutRtl() && this.mTwFluidScroll != null) {
            this.mTwFluidScroll.setScrollbarPosition(getVerticalScrollbarPosition());
        }
        if (this.mAdapter instanceof PreferenceGroupAdapter) {
            int extraPaddingTop = ((PreferenceGroupAdapter) this.mAdapter).mTwNeedPaddingTop;
            if (this.mPaddingTop == 0 && extraPaddingTop > 0) {
                this.mPaddingTop = extraPaddingTop;
            }
        }
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mIsDetaching = true;
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
        if (this.mQCstate != 0) {
            this.mQCstate = 0;
        }
        releaseAllBoosters();
        this.mIsDetaching = false;
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        int touchMode;
        super.onWindowFocusChanged(hasWindowFocus);
        this.mHasWindowFocusForMotion = hasWindowFocus;
        this.mWindowFocusChanged = hasWindowFocus;
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
            if (this.mQCstate != 0) {
                this.mQCstate = 0;
            }
        }
        this.mLastTouchMode = touchMode;
        if (!hasWindowFocus) {
            releaseAllBoosters();
        }
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        if (this.mFastScroll != null) {
            this.mFastScroll.setScrollbarPosition(getVerticalScrollbarPosition());
        } else if (this.mTwFluidScroll != null) {
            this.mTwFluidScroll.setScrollbarPosition(getVerticalScrollbarPosition());
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
        if (this.mPendingCheckForDoublePenClick != null) {
            removeCallbacks(this.mPendingCheckForDoublePenClick);
        }
    }

    public void setForcedClick(boolean force) {
        this.mForcedClick = force;
    }

    private boolean performStylusButtonPressAction(MotionEvent ev) {
        if (this.mChoiceMode != 3 || this.mChoiceActionMode != null) {
            return false;
        }
        View child = getChildAt(this.mMotionPosition - this.mFirstPosition);
        if (child == null || !performLongPress(child, this.mMotionPosition, this.mAdapter.getItemId(this.mMotionPosition))) {
            return false;
        }
        this.mTouchMode = -1;
        setPressed(false);
        child.setPressed(false);
        return true;
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
                if (this.mChoiceMode == 3) {
                    this.mIsDragBlockEnabled = true;
                }
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
            case 31:
                if (this.mIsCtrlkeyPressed) {
                    resetPressItemListArray();
                    break;
                }
                break;
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
                            resetPressItemListArray();
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

    public void dispatchDrawableHotspotChanged(float x, float y) {
    }

    public int pointToPosition(int x, int y) {
        int dividerHeight;
        boolean drawDividers = false;
        Rect frame = this.mTouchFrame;
        if (frame == null) {
            this.mTouchFrame = new Rect();
            frame = this.mTouchFrame;
        }
        if (this instanceof ListView) {
            dividerHeight = ((ListView) this).mDividerHeight;
        } else {
            dividerHeight = 0;
        }
        if (dividerHeight > 0 && ((ListView) this).mDivider != null) {
            drawDividers = true;
        }
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (child.getVisibility() == 0) {
                child.getHitRect(frame);
                if (drawDividers) {
                    frame.bottom += dividerHeight;
                }
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

    private void totwNotifyMultiSelectedStop(int x, int y) {
        super.twNotifyMultiSelectedStop(x, y);
    }

    private boolean startScrollIfNeeded(int x, int y, MotionEvent vtev) {
        boolean overscroll;
        int deltaY = y - this.mMotionY;
        int distance = Math.abs(deltaY);
        if (this.mScrollY != 0) {
            overscroll = true;
        } else {
            overscroll = false;
        }
        if ((!overscroll && distance <= this.mTouchSlop) || (getNestedScrollAxes() & 2) != 0) {
            return false;
        }
        createScrollingCache();
        if (overscroll) {
            this.mTouchMode = 5;
            this.mMotionCorrection = 0;
        } else {
            this.mTouchMode = 3;
            this.mMotionCorrection = deltaY > 0 ? this.mTouchSlop : -this.mTouchSlop;
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
        int incrementalDeltaY;
        int rawDeltaY = y - this.mMotionY;
        int scrollOffsetCorrection = 0;
        int scrollConsumedCorrection = 0;
        if (this.mLastY == Integer.MIN_VALUE) {
            rawDeltaY -= this.mMotionCorrection;
        }
        if (dispatchNestedPreScroll(0, this.mLastY != Integer.MIN_VALUE ? this.mLastY - y : -rawDeltaY, this.mScrollConsumed, this.mScrollOffset)) {
            rawDeltaY += this.mScrollConsumed[1];
            scrollOffsetCorrection = -this.mScrollOffset[1];
            scrollConsumedCorrection = this.mScrollConsumed[1];
            if (vtev != null) {
                vtev.offsetLocation(0.0f, (float) this.mScrollOffset[1]);
                this.mNestedYOffset += this.mScrollOffset[1];
            }
        }
        int deltaY = rawDeltaY;
        if (this.mLastY != Integer.MIN_VALUE) {
            incrementalDeltaY = (y - this.mLastY) + scrollConsumedCorrection;
        } else {
            incrementalDeltaY = deltaY;
        }
        int lastYCorrection = 0;
        View motionView;
        int overscrollMode;
        if (this.mTouchMode == 3) {
            if (this.mScrollStrictSpan == null) {
                this.mScrollStrictSpan = StrictMode.enterCriticalSpan("AbsListView-scroll");
            }
            if (y != this.mLastY) {
                int motionIndex;
                if ((this.mGroupFlags & 524288) == 0 && Math.abs(rawDeltaY) > this.mTouchSlop) {
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
                int motionViewPrevTop = 0;
                motionView = getChildAt(motionIndex);
                if (motionView != null) {
                    motionViewPrevTop = motionView.getTop();
                }
                boolean atEdge = false;
                if (incrementalDeltaY != 0) {
                    atEdge = trackMotionScroll(deltaY, incrementalDeltaY);
                }
                motionView = getChildAt(motionIndex);
                if (motionView != null) {
                    int motionViewRealTop = motionView.getTop();
                    if (atEdge) {
                        int overscroll = (-incrementalDeltaY) - (motionViewRealTop - motionViewPrevTop);
                        if (dispatchNestedScroll(0, overscroll - incrementalDeltaY, 0, overscroll, this.mScrollOffset)) {
                            lastYCorrection = 0 - this.mScrollOffset[1];
                            if (vtev != null) {
                                vtev.offsetLocation(0.0f, (float) this.mScrollOffset[1]);
                                this.mNestedYOffset += this.mScrollOffset[1];
                            }
                        } else {
                            boolean atOverscrollEdge = overScrollBy(0, overscroll, 0, this.mScrollY, 0, 0, 0, this.mOverscrollDistance, true);
                            if (atOverscrollEdge && this.mVelocityTracker != null) {
                                this.mVelocityTracker.clear();
                            }
                            overscrollMode = getOverScrollMode();
                            if (overscrollMode == 0 || (overscrollMode == 1 && !contentFits())) {
                                if (!atOverscrollEdge) {
                                    this.mDirection = 0;
                                    this.mTouchMode = 5;
                                }
                                if (incrementalDeltaY > 0) {
                                    this.mEdgeGlowTop.onPull(((float) (-overscroll)) / ((float) getHeight()), ((float) x) / ((float) getWidth()));
                                    if (!this.mEdgeGlowBottom.isFinished()) {
                                        this.mEdgeGlowBottom.onRelease();
                                    }
                                    invalidateTopGlow();
                                } else if (incrementalDeltaY < 0) {
                                    this.mEdgeGlowBottom.onPull(((float) overscroll) / ((float) getHeight()), 1.0f - (((float) x) / ((float) getWidth())));
                                    if (!this.mEdgeGlowTop.isFinished()) {
                                        this.mEdgeGlowTop.onRelease();
                                    }
                                    invalidateBottomGlow();
                                }
                            }
                        }
                    }
                    this.mMotionY = (y + lastYCorrection) + scrollOffsetCorrection;
                }
                this.mLastY = (y + lastYCorrection) + scrollOffsetCorrection;
            }
        } else if (this.mTouchMode == 5 && y != this.mLastY) {
            int oldScroll = this.mScrollY;
            int newScroll = oldScroll - incrementalDeltaY;
            int newDirection = y > this.mLastY ? 1 : -1;
            if (this.mDirection == 0) {
                this.mDirection = newDirection;
            }
            int overScrollDistance = -incrementalDeltaY;
            if ((newScroll >= 0 || oldScroll < 0) && (newScroll <= 0 || oldScroll > 0)) {
                incrementalDeltaY = 0;
            } else {
                overScrollDistance = -oldScroll;
                incrementalDeltaY += overScrollDistance;
            }
            if (overScrollDistance != 0) {
                overScrollBy(0, overScrollDistance, 0, this.mScrollY, 0, 0, 0, this.mOverscrollDistance, true);
                overscrollMode = getOverScrollMode();
                if (overscrollMode == 0 || (overscrollMode == 1 && !contentFits())) {
                    if (rawDeltaY > 0) {
                        this.mEdgeGlowTop.onPull(((float) overScrollDistance) / ((float) getHeight()), ((float) x) / ((float) getWidth()));
                        if (!this.mEdgeGlowBottom.isFinished()) {
                            this.mEdgeGlowBottom.onRelease();
                        }
                        invalidateTopGlow();
                    } else if (rawDeltaY < 0) {
                        this.mEdgeGlowBottom.onPull(((float) overScrollDistance) / ((float) getHeight()), 1.0f - (((float) x) / ((float) getWidth())));
                        if (!this.mEdgeGlowTop.isFinished()) {
                            this.mEdgeGlowTop.onRelease();
                        }
                        invalidateBottomGlow();
                    }
                }
            }
            if (incrementalDeltaY != 0) {
                if (this.mScrollY != 0) {
                    this.mScrollY = 0;
                    invalidateParentIfNeeded();
                }
                trackMotionScroll(incrementalDeltaY, incrementalDeltaY);
                this.mTouchMode = 3;
                int motionPosition = findClosestMotionRow(y);
                this.mMotionCorrection = 0;
                motionView = getChildAt(motionPosition - this.mFirstPosition);
                this.mMotionViewOriginalTop = motionView != null ? motionView.getTop() : 0;
                this.mMotionY = y + scrollOffsetCorrection;
                this.mMotionPosition = motionPosition;
            }
            this.mLastY = (y + 0) + scrollOffsetCorrection;
            this.mDirection = newDirection;
        }
    }

    private void invalidateTopGlow() {
        int left = 0;
        if (this.mEdgeGlowTop != null) {
            int top;
            boolean clipToPadding = getClipToPadding();
            if (clipToPadding) {
                top = this.mPaddingTop;
            } else {
                top = 0;
            }
            if (clipToPadding) {
                left = this.mPaddingLeft;
            }
            invalidate(left, top, clipToPadding ? getWidth() - this.mPaddingRight : getWidth(), this.mEdgeGlowTop.getMaxHeight() + top);
        }
    }

    private void invalidateBottomGlow() {
        if (this.mEdgeGlowBottom != null) {
            boolean clipToPadding = getClipToPadding();
            int bottom = clipToPadding ? getHeight() - this.mPaddingBottom : getHeight();
            invalidate(clipToPadding ? this.mPaddingLeft : 0, bottom - this.mEdgeGlowBottom.getMaxHeight(), clipToPadding ? getWidth() - this.mPaddingRight : getWidth(), bottom);
        }
    }

    public void onTouchModeChanged(boolean isInTouchMode) {
        if (isInTouchMode) {
            hideSelector();
            if (getHeight() > 0 && getChildCount() > 0) {
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
            if (this.mScrollY != 0) {
                this.mScrollY = 0;
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
        if (Stub.asInterface(ServiceManager.getService("window")) != null) {
        }
        return isLockState || !true;
    }

    public void setHoverScrollMode(boolean flag) {
        if (flag) {
            this.mHoverScrollEnable = true;
        } else {
            this.mHoverScrollEnable = false;
        }
    }

    public void setHoverScrollSpeed(int hoverspeed) {
        this.HOVERSCROLL_SPEED = (float) (hoverspeed + 23);
    }

    public void setHoverScrollDelay(int hoverdelay) {
        this.HOVERSCROLL_DELAY = hoverdelay;
    }

    public void setEnablePaddingInHoverScroll(boolean enable) {
        this.mIsEnabledPaddingInHoverScroll = enable;
    }

    public void addExtraPaddingInTopHoverArea(int extraSpace) {
        this.mExtraPaddingInTopHoverArea = (int) (TypedValue.applyDimension(1, (float) extraSpace, this.mContext.getResources().getDisplayMetrics()) + 0.5f);
    }

    public void addExtraPaddingInBottomHoverArea(int extraSpace) {
        this.mExtraPaddingInBottomHoverArea = (int) (TypedValue.applyDimension(1, (float) extraSpace, this.mContext.getResources().getDisplayMetrics()) + 0.5f);
    }

    protected boolean dispatchHoverEvent(MotionEvent ev) {
        int action = ev.getAction();
        onHoverDrawableState(ev);
        this.mNewTextViewHoverState = MultiSelection.isTextViewHovered();
        if (!this.mNewTextViewHoverState && this.mOldTextViewHoverState && this.mIsDragBlockEnabled && (ev.getButtonState() == 32 || ev.getButtonState() == 2)) {
            this.mIsNeedPenSelectIconSet = true;
        } else {
            this.mIsNeedPenSelectIconSet = false;
        }
        this.mOldTextViewHoverState = this.mNewTextViewHoverState;
        if (action == 9) {
            int toolType = ev.getToolType(0);
            this.mNeedsHoverScroll = true;
            if (!(isHoveringUIEnabled() && this.mHoverScrollEnable)) {
                this.mNeedsHoverScroll = false;
            }
            if (this.mNeedsHoverScroll && toolType == 2) {
                boolean isHoveringOn = Settings$System.getInt(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING, 0) == 1;
                boolean isHoverListScrollOn = Settings$System.getInt(this.mContext.getContentResolver(), Settings$System.PEN_HOVERING_LIST_SCROLL, 0) == 1;
                boolean isCarModeOn = Settings$System.getIntForUser(this.mContext.getContentResolver(), "car_mode_on", 0, -3) == 1;
                if (!isHoveringOn || isCarModeOn) {
                    this.mNeedsHoverScroll = false;
                }
                if (isHoveringOn && this.mIsDragBlockEnabled && !this.mIsPenSelectPointerSetted && toolType == 2 && (ev.getButtonState() == 32 || ev.getButtonState() == 2)) {
                    showPointerIcon(21);
                    this.mIsPenSelectPointerSetted = true;
                }
                if (!isHoverListScrollOn) {
                    this.mNeedsHoverScroll = false;
                }
            }
            if (this.mNeedsHoverScroll && toolType == 3) {
                this.mNeedsHoverScroll = false;
            }
        } else if (action == 7) {
            if ((this.mIsDragBlockEnabled && !this.mIsPenSelectPointerSetted && ev.getToolType(0) == 2 && (ev.getButtonState() == 32 || ev.getButtonState() == 2)) || this.mIsNeedPenSelectIconSet) {
                showPointerIcon(21);
                this.mIsPenSelectPointerSetted = true;
            } else if (this.mIsDragBlockEnabled && this.mIsPenSelectPointerSetted && ev.getButtonState() != 32 && ev.getButtonState() != 2) {
                showPointerIcon(1);
                this.mIsPenSelectPointerSetted = false;
            }
        } else if (action == 10 && this.mIsPenSelectPointerSetted) {
            showPointerIcon(1);
            this.mIsPenSelectPointerSetted = false;
        }
        if (!this.mNeedsHoverScroll) {
            return super.dispatchHoverEvent(ev);
        }
        int contentTop;
        int contentBottom;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int childCount = getChildCount();
        int count = getChildCount();
        if (this.mHoverHandler == null) {
            this.mHoverHandler = new HoverScrollHandler(this);
        }
        if (this.mHoverTopAreaHeight <= 0 || this.mHoverBottomAreaHeight <= 0) {
            this.mHoverTopAreaHeight = (int) (TypedValue.applyDimension(1, 25.0f, this.mContext.getResources().getDisplayMetrics()) + 0.5f);
            this.mHoverBottomAreaHeight = (int) (TypedValue.applyDimension(1, 25.0f, this.mContext.getResources().getDisplayMetrics()) + 0.5f);
        }
        if (this.mIsEnabledPaddingInHoverScroll) {
            contentTop = this.mListPadding.top;
            contentBottom = getHeight() - this.mListPadding.bottom;
        } else {
            contentTop = this.mExtraPaddingInTopHoverArea;
            contentBottom = getHeight() - this.mExtraPaddingInBottomHoverArea;
        }
        boolean canScrollDown = this.mFirstPosition + count < this.mItemCount;
        if (!canScrollDown && count > 0) {
            View child = getChildAt(count - 1);
            canScrollDown = child.getBottom() > this.mBottom - this.mListPadding.bottom || child.getBottom() > getHeight() - this.mListPadding.bottom;
        }
        boolean canScrollUp = this.mFirstPosition > 0;
        if (!canScrollUp && getChildCount() > 0) {
            canScrollUp = getChildAt(0).getTop() < this.mListPadding.top;
        }
        boolean isPossibleTooltype = ev.getToolType(0) == 2;
        if ((y <= this.mHoverTopAreaHeight + contentTop || y >= contentBottom - this.mHoverBottomAreaHeight) && x > 0 && x <= getRight() && ((canScrollUp || canScrollDown) && ((y < contentTop || y > this.mHoverTopAreaHeight + contentTop || canScrollUp || !this.mIsHoverOverscrolled) && ((y < contentBottom - this.mHoverBottomAreaHeight || y > contentBottom || canScrollDown || !this.mIsHoverOverscrolled) && !((isPossibleTooltype && (ev.getButtonState() == 32 || ev.getButtonState() == 2)) || !isPossibleTooltype || isLockScreenMode()))))) {
            if (!this.mHoverAreaEnter) {
                this.mHoverScrollStartTime = System.currentTimeMillis();
            }
            switch (action) {
                case 7:
                    if (this.mHoverAreaEnter) {
                        if (y >= contentTop && y <= this.mHoverTopAreaHeight + contentTop) {
                            if (!this.mHoverHandler.hasMessages(1)) {
                                this.mHoverRecognitionStartTime = System.currentTimeMillis();
                                if (!this.mIsHoverOverscrolled || this.mHoverScrollDirection == 1) {
                                    showPointerIcon(11);
                                }
                                this.mHoverScrollDirection = 2;
                                this.mHoverHandler.sendEmptyMessage(1);
                                if (isQCSupported() && this.mQCstate == 0 && canScrollUp() && canScrollVertically(-1)) {
                                    setupQuickController(2);
                                    this.mQCstate = 1;
                                    break;
                                }
                            }
                        } else if (y >= contentBottom - this.mHoverBottomAreaHeight && y <= contentBottom) {
                            if (!this.mHoverHandler.hasMessages(1)) {
                                this.mHoverRecognitionStartTime = System.currentTimeMillis();
                                if (!this.mIsHoverOverscrolled || this.mHoverScrollDirection == 2) {
                                    showPointerIcon(15);
                                }
                                this.mHoverScrollDirection = 1;
                                this.mHoverHandler.sendEmptyMessage(1);
                                if (isQCSupported() && this.mQCstate == 0 && canScrollDown() && canScrollVertically(1)) {
                                    setupQuickController(4);
                                    this.mQCstate = 1;
                                    break;
                                }
                            }
                        } else {
                            if (this.mHoverHandler.hasMessages(1)) {
                                this.mHoverHandler.removeMessages(1);
                            }
                            showPointerIcon(1);
                            this.mHoverRecognitionStartTime = 0;
                            this.mHoverScrollStartTime = 0;
                            this.mIsHoverOverscrolled = false;
                            this.mHoverAreaEnter = false;
                            this.mIsSendHoverScrollState = false;
                            break;
                        }
                    }
                    this.mHoverAreaEnter = true;
                    ev.setAction(10);
                    return super.dispatchHoverEvent(ev);
                    break;
                case 9:
                    this.mHoverAreaEnter = true;
                    if (y >= contentTop && y <= this.mHoverTopAreaHeight + contentTop) {
                        if (!this.mHoverHandler.hasMessages(1)) {
                            this.mHoverRecognitionStartTime = System.currentTimeMillis();
                            showPointerIcon(11);
                            this.mHoverScrollDirection = 2;
                            this.mHoverHandler.sendEmptyMessage(1);
                            if (isQCSupported() && canScrollUp() && canScrollVertically(-1)) {
                                setupQuickController(2);
                                this.mQCstate = 1;
                                break;
                            }
                        }
                    } else if (y >= contentBottom - this.mHoverBottomAreaHeight && y <= contentBottom && !this.mHoverHandler.hasMessages(1)) {
                        this.mHoverRecognitionStartTime = System.currentTimeMillis();
                        showPointerIcon(15);
                        this.mHoverScrollDirection = 1;
                        this.mHoverHandler.sendEmptyMessage(1);
                        if (isQCSupported() && canScrollDown() && canScrollVertically(1)) {
                            setupQuickController(4);
                            this.mQCstate = 1;
                            break;
                        }
                    }
                    break;
                case 10:
                    if (this.mHoverHandler.hasMessages(1)) {
                        this.mHoverHandler.removeMessages(1);
                    }
                    showPointerIcon(1);
                    this.mHoverRecognitionStartTime = 0;
                    this.mHoverScrollStartTime = 0;
                    this.mIsHoverOverscrolled = false;
                    this.mHoverAreaEnter = false;
                    this.mIsSendHoverScrollState = false;
                    if (this.mQCstate == 1) {
                        this.mHoverHandler.sendEmptyMessageDelayed(0, (long) this.QC_ICON_HIDE_DELAY);
                    }
                    if (this.mHoverScrollStateForListener != 0) {
                        this.mHoverScrollStateForListener = 0;
                        if (!(this.mOnScrollListener == null || this.mTouchMode == 4)) {
                            this.mOnScrollListener.onScrollStateChanged(this, 0);
                        }
                    }
                    return super.dispatchHoverEvent(ev);
            }
            return true;
        }
        if (this.mHoverHandler.hasMessages(1)) {
            this.mHoverHandler.removeMessages(1);
            showPointerIcon(1);
        }
        if (this.mHoverHandler.hasMessages(2)) {
            this.mHoverHandler.removeMessages(2);
        }
        if ((y > this.mHoverTopAreaHeight + contentTop && y < contentBottom - this.mHoverBottomAreaHeight) || x <= 0 || x > getRight()) {
            this.mIsHoverOverscrolled = false;
        }
        if (this.mHoverAreaEnter || this.mHoverScrollStartTime != 0) {
            showPointerIcon(1);
        }
        this.mHoverRecognitionStartTime = 0;
        this.mHoverScrollStartTime = 0;
        this.mHoverAreaEnter = false;
        this.mIsSendHoverScrollState = false;
        if (action == 10 && this.mHoverScrollStateForListener != 0) {
            this.mHoverScrollStateForListener = 0;
            if (!(this.mOnScrollListener == null || this.mTouchMode == 4)) {
                this.mOnScrollListener.onScrollStateChanged(this, 0);
            }
        }
        if (this.mQCstate != 0) {
            this.mQCstate = 0;
            postInvalidateOnAnimation();
        }
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
        int contentBottom = 0;
        int count = getChildCount();
        if (childCount != 0) {
            contentBottom = getHeight();
        }
        boolean canScrollDown = this.mFirstPosition + count < this.mItemCount;
        if (!canScrollDown && count > 0) {
            View child = getChildAt(count - 1);
            canScrollDown = child.getBottom() > this.mBottom - this.mListPadding.bottom || child.getBottom() > getHeight() - this.mListPadding.bottom;
        }
        boolean canScrollUp = this.mFirstPosition > 0;
        if (!canScrollUp && getChildCount() > 0) {
            canScrollUp = getChildAt(0).getTop() < this.mListPadding.top;
        }
        if ((y <= this.mDragScrollWorkingZonePx || y >= contentBottom - this.mDragScrollWorkingZonePx) && x > 0 && x <= getRight() && (canScrollUp || canScrollDown)) {
            if (this.mHoverHandler == null) {
                this.mHoverHandler = new HoverScrollHandler(this);
            }
            if (!this.mHoverAreaEnter) {
                this.mHoverScrollStartTime = System.currentTimeMillis();
            }
            switch (action) {
                case 2:
                    if (!this.mHoverAreaEnter) {
                        this.mHoverAreaEnter = true;
                    }
                    if (y >= 0 && y <= this.mDragScrollWorkingZonePx) {
                        if (!this.mHoverHandler.hasMessages(1)) {
                            this.mIsDragScrolled = true;
                            this.mHoverRecognitionStartTime = System.currentTimeMillis();
                            this.mHoverScrollDirection = 2;
                            this.mHoverHandler.sendEmptyMessage(1);
                            break;
                        }
                    } else if (y >= contentBottom - this.mDragScrollWorkingZonePx && y <= contentBottom && !this.mHoverHandler.hasMessages(1)) {
                        this.mIsDragScrolled = true;
                        this.mHoverRecognitionStartTime = System.currentTimeMillis();
                        this.mHoverScrollDirection = 1;
                        this.mHoverHandler.sendEmptyMessage(1);
                        break;
                    }
                    break;
                case 3:
                    if (this.mIsDragScrolled) {
                        this.mIsDragScrolled = false;
                        resetDragableChildren(ev);
                        break;
                    }
                    break;
                case 4:
                case 6:
                    break;
                case 5:
                    this.mHoverAreaEnter = true;
                    if (y >= 0 && y <= this.mDragScrollWorkingZonePx) {
                        if (!this.mHoverHandler.hasMessages(1)) {
                            this.mIsDragScrolled = true;
                            this.mHoverRecognitionStartTime = System.currentTimeMillis();
                            this.mHoverScrollDirection = 2;
                            this.mHoverHandler.sendEmptyMessage(1);
                            break;
                        }
                    } else if (y >= contentBottom - this.mDragScrollWorkingZonePx && y <= contentBottom && !this.mHoverHandler.hasMessages(1)) {
                        this.mIsDragScrolled = true;
                        this.mHoverRecognitionStartTime = System.currentTimeMillis();
                        this.mHoverScrollDirection = 1;
                        this.mHoverHandler.sendEmptyMessage(1);
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
            resetDragableChildren(ev);
        }
        return super.dispatchDragEvent(ev);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        int contentTop;
        int contentBottom;
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        int action = ev.getAction();
        if (this.mTwDragSelectedItemArray == null) {
            this.mTwDragSelectedItemArray = new ArrayList();
        }
        if (this.mHoverHandler == null) {
            this.mHoverHandler = new HoverScrollHandler(this);
        }
        this.mIsTextSelectionStarted = MultiSelection.getIsMultiSelectingText();
        boolean needToScroll = MultiSelection.isNeedToScroll();
        if (action == 211) {
            this.mIsNeedPenSelection = true;
            this.mTouchdownX = x;
            this.mTouchdownY = y;
            if (Settings$System.getIntForUser(this.mContext.getContentResolver(), "car_mode_on", 0, -3) == 1) {
                this.mIsNeedPenSelection = false;
            }
        }
        if (this.mIsTextSelectionStarted) {
            this.mIsNeedPenSelection = false;
        }
        if (this.mHoverTopAreaHeight <= 0 || this.mHoverBottomAreaHeight <= 0) {
            this.mHoverTopAreaHeight = (int) (TypedValue.applyDimension(1, 25.0f, this.mContext.getResources().getDisplayMetrics()) + 0.5f);
            this.mHoverBottomAreaHeight = (int) (TypedValue.applyDimension(1, 25.0f, this.mContext.getResources().getDisplayMetrics()) + 0.5f);
        }
        if (this.mIsEnabledPaddingInHoverScroll) {
            contentTop = this.mListPadding.top;
            contentBottom = getHeight() - this.mListPadding.bottom;
        } else {
            contentTop = 0;
            contentBottom = getHeight();
        }
        if (this.mIsEnabledPaddingInHoverScroll && ((y < contentTop || y > contentBottom) && ev.getAction() != 1 && ev.getAction() != 212)) {
            return true;
        }
        switch (action) {
            case 0:
                if (this.mWindowFocusChanged) {
                    this.mWindowFocusChanged = false;
                }
                if (this.mQCstate == 1) {
                    if (!this.mIsQCShown || !this.mQCRect.contains(x, y)) {
                        this.mQCstate = 0;
                        break;
                    }
                    if (this.mHoverHandler.hasMessages(1)) {
                        this.mHoverHandler.removeMessages(1);
                    }
                    if (this.mHoverHandler.hasMessages(0)) {
                        this.mHoverHandler.removeMessages(0);
                    }
                    this.mHoverHandler.sendEmptyMessage(2);
                    this.mQCstate = 2;
                    postInvalidateOnAnimation();
                    return true;
                }
                break;
            case 1:
            case 3:
                if (this.mQCstate == 2) {
                    if (this.mHoverHandler.hasMessages(1)) {
                        this.mHoverHandler.removeMessages(1);
                    }
                    if (this.mHoverHandler.hasMessages(2)) {
                        this.mHoverHandler.removeMessages(2);
                    }
                    this.mIsHoverOverscrolled = false;
                    this.mQCstate = 0;
                    postInvalidateOnAnimation();
                    return true;
                }
                break;
            case 2:
                if (this.mQCstate == 2) {
                    return true;
                }
                break;
            case 212:
                break;
            case 213:
                this.mDeltamoveX = x - this.mTouchdownX;
                this.mDeltamoveY = y - this.mTouchdownY;
                if ((this.mDeltamoveX * this.mDeltamoveX) + (this.mDeltamoveY * this.mDeltamoveY) > this.mTouchSlop * this.mTouchSlop) {
                    this.mIsMovedbeforeUP = true;
                }
                if (this.mIsNeedPenSelection) {
                    int i;
                    View child;
                    int childTop;
                    int childBottom;
                    int i2;
                    int count = getChildCount();
                    if (this.mIsfirstMoveEvent) {
                        this.mTwDragStartX = x;
                        this.mTwDragStartY = y;
                        super.twNotifyMultiSelectedStart(x, y);
                        this.mIsPenPressed = true;
                        this.mTwTrackedChildPosition = pointToPosition(x, y);
                        if (this.mTwTrackedChildPosition == -1) {
                            int oldDistanceFromTop = 0;
                            int oldDistanceFromBottom = 0;
                            for (i = count - 1; i >= 0; i--) {
                                child = getChildAt(i);
                                if (child != null) {
                                    childTop = child.getTop();
                                    childBottom = child.getBottom();
                                    if (this.mTwDragStartY < childTop || this.mTwDragStartY > childBottom) {
                                        int newDistanceFromTop = Math.abs(this.mTwDragStartY - childTop);
                                        int newDistanceFromBottom = Math.abs(this.mTwDragStartY - childBottom);
                                        if (i == count - 1) {
                                            this.mTwCloseChildPositionByTop = (count - 1) + getFirstVisiblePosition();
                                            this.mTwCloseChildPositionByBottom = (count - 1) + getFirstVisiblePosition();
                                            oldDistanceFromTop = newDistanceFromTop;
                                            oldDistanceFromBottom = newDistanceFromBottom;
                                        } else {
                                            if (newDistanceFromTop <= oldDistanceFromTop) {
                                                this.mTwCloseChildPositionByTop = getFirstVisiblePosition() + i;
                                                oldDistanceFromTop = newDistanceFromTop;
                                            }
                                            if (newDistanceFromBottom <= oldDistanceFromBottom) {
                                                this.mTwCloseChildPositionByBottom = getFirstVisiblePosition() + i;
                                                oldDistanceFromBottom = newDistanceFromBottom;
                                            }
                                        }
                                    } else {
                                        this.mTwTrackedChild = child;
                                        this.mTwTrackedChildPosition = getFirstVisiblePosition() + i;
                                        if (this.mTwTrackedChild == null) {
                                            this.mTwCloseChildByTop = getChildAt(this.mTwCloseChildPositionByTop - getFirstVisiblePosition());
                                            if (this.mTwCloseChildByTop != null) {
                                                this.mTwDistanceFromCloseChildTop = this.mTwDragStartY - this.mTwCloseChildByTop.getTop();
                                            }
                                            this.mTwCloseChildByBottom = getChildAt(this.mTwCloseChildPositionByBottom - getFirstVisiblePosition());
                                            if (this.mTwCloseChildByBottom != null) {
                                                this.mTwDistanceFromCloseChildBottom = this.mTwDragStartY - this.mTwCloseChildByBottom.getTop();
                                            }
                                        }
                                    }
                                }
                            }
                            if (this.mTwTrackedChild == null) {
                                this.mTwCloseChildByTop = getChildAt(this.mTwCloseChildPositionByTop - getFirstVisiblePosition());
                                if (this.mTwCloseChildByTop != null) {
                                    this.mTwDistanceFromCloseChildTop = this.mTwDragStartY - this.mTwCloseChildByTop.getTop();
                                }
                                this.mTwCloseChildByBottom = getChildAt(this.mTwCloseChildPositionByBottom - getFirstVisiblePosition());
                                if (this.mTwCloseChildByBottom != null) {
                                    this.mTwDistanceFromCloseChildBottom = this.mTwDragStartY - this.mTwCloseChildByBottom.getTop();
                                }
                            }
                        } else {
                            this.mTwTrackedChild = getChildAt(this.mTwTrackedChildPosition - getFirstVisiblePosition());
                        }
                        if (this.mTwTrackedChild != null) {
                            this.mTwDistanceFromTrackedChildTop = this.mTwDragStartY - this.mTwTrackedChild.getTop();
                        }
                        this.mIsfirstMoveEvent = false;
                    }
                    if (this.mTwDragStartX == 0 && this.mTwDragStartY == 0) {
                        this.mTwDragStartX = x;
                        this.mTwDragStartY = y;
                        super.twNotifyMultiSelectedStart(x, y);
                        this.mIsPenPressed = true;
                    }
                    this.mTwDragEndX = x;
                    this.mTwDragEndY = y;
                    if (this.mTwDragEndY < 0) {
                        this.mTwDragEndY = 0;
                    } else if (this.mTwDragEndY > contentBottom) {
                        this.mTwDragEndY = contentBottom;
                    }
                    this.mTwDragSelectedViewPosition = pointToPosition(x, y);
                    this.mTwDragBlockLeft = this.mTwDragStartX < this.mTwDragEndX ? this.mTwDragStartX : this.mTwDragEndX;
                    this.mTwDragBlockTop = this.mTwDragStartY < this.mTwDragEndY ? this.mTwDragStartY : this.mTwDragEndY;
                    this.mTwDragBlockRight = this.mTwDragEndX > this.mTwDragStartX ? this.mTwDragEndX : this.mTwDragStartX;
                    if (this.mTwDragEndY > this.mTwDragStartY) {
                        i2 = this.mTwDragEndY;
                    } else {
                        i2 = this.mTwDragStartY;
                    }
                    this.mTwDragBlockBottom = i2;
                    for (i = 0; i < count; i++) {
                        child = getChildAt(i);
                        if (child != null) {
                            int childLeft = child.getLeft();
                            childTop = child.getTop();
                            int childRight = child.getRight();
                            childBottom = child.getBottom();
                            if (child.getVisibility() == 0) {
                                if ((this.mTwDragBlockLeft <= childLeft || this.mTwDragBlockTop <= childTop || this.mTwDragBlockRight >= childRight || this.mTwDragBlockBottom >= childBottom) && (((this.mTwDragBlockLeft <= childLeft || this.mTwDragBlockRight >= childRight) && ((this.mTwDragBlockLeft >= childLeft || this.mTwDragBlockRight <= childLeft) && (this.mTwDragBlockLeft >= childRight || this.mTwDragBlockRight <= childRight))) || ((this.mTwDragBlockTop < childTop || this.mTwDragBlockBottom > childBottom) && ((this.mTwDragBlockTop > childTop || this.mTwDragBlockBottom <= childTop) && (this.mTwDragBlockTop >= childBottom || this.mTwDragBlockBottom < childBottom))))) {
                                    this.mTwDragSelectedViewPosition = pointToPosition(childLeft + 1, childTop + 1);
                                    if (this.mTwDragSelectedViewPosition != -1 && this.mAdapter.isEnabled(this.mTwDragSelectedViewPosition) && this.mTwDragSelectedItemArray.contains(Integer.valueOf(this.mTwDragSelectedViewPosition))) {
                                        this.mTwDragSelectedItemArray.remove(new Integer(this.mTwDragSelectedViewPosition));
                                        addToPressItemListArray(this.mTwDragSelectedViewPosition, -1);
                                        twNotifyMultiSelectState(child, this.mTwDragSelectedViewPosition, getItemIdAtPosition(this.mTwDragSelectedViewPosition));
                                    }
                                } else {
                                    this.mTwDragSelectedViewPosition = pointToPosition(childLeft + 1, childTop + 1);
                                    if (!(this.mTwDragSelectedViewPosition == -1 || !this.mAdapter.isEnabled(this.mTwDragSelectedViewPosition) || this.mTwDragSelectedItemArray.contains(Integer.valueOf(this.mTwDragSelectedViewPosition)))) {
                                        this.mTwDragSelectedItemArray.add(Integer.valueOf(this.mTwDragSelectedViewPosition));
                                        addToPressItemListArray(this.mTwDragSelectedViewPosition, -1);
                                        twNotifyMultiSelectState(child, this.mTwDragSelectedViewPosition, getItemIdAtPosition(this.mTwDragSelectedViewPosition));
                                    }
                                }
                            }
                        }
                    }
                    needToScroll = true;
                }
                if (needToScroll) {
                    if (y >= contentTop + 0 && y <= this.mHoverTopAreaHeight + contentTop) {
                        if (!this.mHoverAreaEnter) {
                            this.mHoverAreaEnter = true;
                            this.mHoverScrollStartTime = System.currentTimeMillis();
                            if (this.mOnScrollListener != null) {
                                this.mOnScrollListener.onScrollStateChanged(this, 1);
                            }
                        }
                        if (!this.mHoverHandler.hasMessages(1)) {
                            this.mHoverRecognitionStartTime = System.currentTimeMillis();
                            this.mHoverScrollDirection = 2;
                            this.mHoverHandler.sendEmptyMessage(1);
                        }
                    } else if (y < contentBottom - this.mHoverBottomAreaHeight || y > contentBottom) {
                        if (this.mHoverAreaEnter && this.mOnScrollListener != null) {
                            this.mOnScrollListener.onScrollStateChanged(this, 0);
                        }
                        this.mHoverScrollStartTime = 0;
                        this.mHoverRecognitionStartTime = 0;
                        this.mHoverAreaEnter = false;
                        if (this.mHoverHandler.hasMessages(1)) {
                            this.mHoverHandler.removeMessages(1);
                        }
                        this.mIsHoverOverscrolled = false;
                    } else {
                        if (!this.mHoverAreaEnter) {
                            this.mHoverAreaEnter = true;
                            this.mHoverScrollStartTime = System.currentTimeMillis();
                            if (this.mOnScrollListener != null) {
                                this.mOnScrollListener.onScrollStateChanged(this, 1);
                            }
                        }
                        if (!this.mHoverHandler.hasMessages(1)) {
                            this.mHoverRecognitionStartTime = System.currentTimeMillis();
                            this.mHoverScrollDirection = 1;
                            this.mHoverHandler.sendEmptyMessage(1);
                        }
                    }
                    if (this.mIsDragBlockEnabled) {
                        invalidate();
                    }
                } else if (this.mPreviousTextViewScroll && this.mHoverHandler.hasMessages(1)) {
                    this.mHoverHandler.removeMessages(1);
                }
                this.mPreviousTextViewScroll = needToScroll;
                break;
        }
        if (!this.mIsTextSelectionStarted) {
            if (action == 212) {
                this.mIsFirstPenClick = !this.mIsFirstPenClick;
            }
            if (this.mHoverAreaEnter && this.mOnScrollListener != null) {
                this.mOnScrollListener.onScrollStateChanged(this, 0);
            }
            this.mHoverRecognitionStartTime = 0;
            this.mHoverScrollStartTime = 0;
            this.mHoverAreaEnter = false;
            this.mTwDragSelectedItemSize = this.mTwDragSelectedItemArray.size();
            if (this.mPendingCheckForDoublePenClick == null) {
                this.mPendingCheckForDoublePenClick = new CheckForDoublePenClick();
            }
            this.mPendingCheckForDoublePenClick.x = x;
            this.mPendingCheckForDoublePenClick.y = y;
            if (!this.mIsFirstPenClick) {
                removeCallbacks(this.mPendingCheckForDoublePenClick);
                this.mTwDragSelectedItemArray.clear();
                this.mTwDragSelectedItemSize = 0;
            } else if (this.mIsMovedbeforeUP) {
                post(this.mPendingCheckForDoublePenClick);
            } else {
                postDelayed(this.mPendingCheckForDoublePenClick, (long) ViewConfiguration.getDoubleTapTimeout());
            }
        }
        this.mIsPenPressed = false;
        this.mIsfirstMoveEvent = true;
        this.mTwDragSelectedViewPosition = -1;
        this.mTwDragStartX = 0;
        this.mTwDragStartY = 0;
        this.mTwDragEndX = 0;
        this.mTwDragEndY = 0;
        this.mTwDragBlockLeft = 0;
        this.mTwDragBlockTop = 0;
        this.mTwDragBlockRight = 0;
        this.mTwDragBlockBottom = 0;
        this.mTwTrackedChild = null;
        this.mTwDistanceFromTrackedChildTop = 0;
        this.mIsCloseChildSetted = false;
        this.mOldHoverScrollDirection = -1;
        this.mTwCloseChildByTop = null;
        this.mTwCloseChildPositionByTop = -1;
        this.mTwDistanceFromCloseChildTop = 0;
        this.mTwCloseChildByBottom = null;
        this.mTwCloseChildPositionByBottom = -1;
        this.mTwDistanceFromCloseChildBottom = 0;
        if (this.mIsDragBlockEnabled) {
            invalidate();
        }
        if (this.mHoverHandler.hasMessages(1)) {
            this.mHoverHandler.removeMessages(1);
        }
        this.mIsMovedbeforeUP = false;
        return super.dispatchTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean z = false;
        if (isEnabled()) {
            if (this.mPositionScroller != null) {
                this.mPositionScroller.stop();
            }
            if (this.mIsDetaching || !isAttachedToWindow()) {
                return false;
            }
            startNestedScroll(2);
            if (this.mFastScroll != null && this.mFastScroll.onTouchEvent(ev)) {
                return true;
            }
            if (this.mTwFluidScroll != null) {
                boolean intercepted = this.mTwFluidScroll.onTouchEvent(ev);
                if (this.mOnFluidScrollEffectListener != null) {
                    this.mOnFluidScrollEffectListener.onEffectStateChanged(this.mTwFluidScroll.getEffectState(), this.mTwFluidScroll.getScrollY());
                }
                if (intercepted) {
                    return true;
                }
            }
            initVelocityTrackerIfNotExists();
            MotionEvent vtev = MotionEvent.obtain(ev);
            int actionMasked = ev.getActionMasked();
            if (actionMasked == 0) {
                this.mNestedYOffset = 0;
            }
            vtev.offsetLocation(0.0f, (float) this.mNestedYOffset);
            int x;
            int y;
            int motionPosition;
            switch (actionMasked) {
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
                    y = (int) ev.getY(index);
                    this.mMotionCorrection = 0;
                    this.mActivePointerId = id;
                    this.mMotionX = x;
                    this.mMotionY = y;
                    motionPosition = pointToPosition(x, y);
                    if (motionPosition >= 0) {
                        this.mMotionViewOriginalTop = getChildAt(motionPosition - this.mFirstPosition).getTop();
                        this.mMotionPosition = motionPosition;
                    }
                    this.mLastY = y;
                    this.mPointerCount++;
                    break;
                case 6:
                    onSecondaryPointerUp(ev);
                    x = this.mMotionX;
                    y = this.mMotionY;
                    motionPosition = pointToPosition(x, y);
                    if (motionPosition >= 0) {
                        this.mMotionViewOriginalTop = getChildAt(motionPosition - this.mFirstPosition).getTop();
                        this.mMotionPosition = motionPosition;
                    }
                    this.mLastY = y;
                    this.mPointerCount--;
                    break;
            }
            if (this.mVelocityTracker != null) {
                this.mVelocityTracker.addMovement(vtev);
            }
            vtev.recycle();
            return true;
        }
        if (isClickable() || isLongClickable()) {
            z = true;
        }
        return z;
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
            this.mLastY = this.mMotionY;
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
                this.mMotionViewOriginalTop = getChildAt(motionPosition - this.mFirstPosition).getTop();
            }
            this.mMotionX = x;
            this.mMotionY = y;
            this.mMotionPosition = motionPosition;
            this.mLastY = Integer.MIN_VALUE;
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
        if (this.mDataChanged) {
            layoutChildren();
        }
        int y = (int) ev.getY(pointerIndex);
        switch (this.mTouchMode) {
            case 0:
            case 1:
            case 2:
                if (!startScrollIfNeeded((int) ev.getX(pointerIndex), y, vtev)) {
                    View motionView = getChildAt(this.mMotionPosition - this.mFirstPosition);
                    float x = ev.getX(pointerIndex);
                    if (!pointInView(x, (float) y, (float) this.mTouchSlop)) {
                        setPressed(false);
                        if (motionView != null) {
                            motionView.setPressed(false);
                        }
                        removeCallbacks(this.mTouchMode == 0 ? this.mPendingCheckForTap : this.mPendingCheckForLongPress);
                        this.mTouchMode = 2;
                        updateSelectorState();
                        return;
                    } else if (motionView != null) {
                        float[] point = this.mTmpPoint;
                        point[0] = x;
                        point[1] = (float) y;
                        transformPointToViewLocal(point, motionView);
                        motionView.drawableHotspotChanged(point[0], point[1]);
                        return;
                    } else {
                        return;
                    }
                }
                return;
            case 3:
            case 5:
                scrollIfNeeded((int) ev.getX(pointerIndex), y, vtev);
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
                                    AbsListView.this.mTouchModeReset = null;
                                    AbsListView.this.mTouchMode = -1;
                                    child.setPressed(false);
                                    AbsListView.this.setPressed(false);
                                    if (AbsListView.this.mForcedClick || !(AbsListView.this.mDataChanged || AbsListView.this.mIsDetaching || !AbsListView.this.isAttachedToWindow())) {
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
                int firstChildTop = getChildAt(0).getTop();
                int lastChildBottom = getChildAt(childCount - 1).getBottom();
                int contentTop = this.mListPadding.top;
                int contentBottom = getHeight() - this.mListPadding.bottom;
                if (this.mFirstPosition == 0 && firstChildTop >= contentTop && this.mFirstPosition + childCount < this.mItemCount && lastChildBottom <= getHeight() - contentBottom) {
                    this.mTouchMode = -1;
                    reportScrollStateChange(0);
                    break;
                }
                velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                initialVelocity = (int) (velocityTracker.getYVelocity(this.mActivePointerId) * this.mVelocityScale);
                if (Math.abs(initialVelocity) > this.mMinimumVelocity && ((this.mFirstPosition != 0 || firstChildTop != contentTop - this.mOverscrollDistance) && (this.mFirstPosition + childCount != this.mItemCount || lastChildBottom != this.mOverscrollDistance + contentBottom))) {
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
                if (this.mFlingRunnable == null) {
                    this.mFlingRunnable = new FlingRunnable();
                }
                velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                initialVelocity = (int) velocityTracker.getYVelocity(this.mActivePointerId);
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
        if (this.mEdgeGlowTop != null) {
            this.mEdgeGlowTop.onRelease();
            this.mEdgeGlowBottom.onRelease();
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
        if (this.mEdgeGlowTop != null) {
            this.mEdgeGlowTop.onRelease();
            this.mEdgeGlowBottom.onRelease();
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
            this.mIsPenHovered = true;
        } else if (action == 10) {
            this.mIsPenHovered = false;
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
                if (!this.mIsPenHovered) {
                    this.mSelectorRect.setEmpty();
                }
            } else if (!isMultiWindows()) {
                if (this.mIsPenHovered || !isInDialog()) {
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
                                this.mSelectorPosition = -1;
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
        if (this.mScrollY != scrollY) {
            onScrollChanged(this.mScrollX, scrollY, this.mScrollX, this.mScrollY);
            this.mScrollY = scrollY;
            invalidateParentIfNeeded();
            awakenScrollBars();
        }
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if ((event.getSource() & 2) != 0) {
            switch (event.getAction()) {
                case 8:
                    if (this.mTouchMode == -1) {
                        float vscroll = event.getAxisValue(9);
                        if (vscroll != 0.0f) {
                            int delta = (int) (getVerticalScrollFactor() * vscroll);
                            if (!trackMotionScroll(delta, delta)) {
                                return true;
                            }
                        }
                    }
                    break;
                case 11:
                    int actionButton = event.getActionButton();
                    if ((actionButton == 32 || actionButton == 2) && ((this.mTouchMode == 0 || this.mTouchMode == 1) && performStylusButtonPressAction(event))) {
                        removeCallbacks(this.mPendingCheckForLongPress);
                        removeCallbacks(this.mPendingCheckForTap);
                        break;
                    }
            }
        }
        return super.onGenericMotionEvent(event);
    }

    public void fling(int velocityY) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        }
        reportScrollStateChange(2);
        this.mFlingRunnable.start(velocityY);
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & 2) != 0;
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(2);
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        int oldTop;
        View motionView = getChildAt(getChildCount() / 2);
        if (motionView != null) {
            oldTop = motionView.getTop();
        } else {
            oldTop = 0;
        }
        if (motionView == null || trackMotionScroll(-dyUnconsumed, -dyUnconsumed)) {
            int myUnconsumed = dyUnconsumed;
            int myConsumed = 0;
            if (motionView != null) {
                myConsumed = motionView.getTop() - oldTop;
                myUnconsumed -= myConsumed;
            }
            dispatchNestedScroll(0, myConsumed, 0, myUnconsumed, null);
        }
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        int childCount = getChildCount();
        if (consumed || childCount <= 0 || !canScrollList((int) velocityY) || Math.abs(velocityY) <= ((float) this.mMinimumVelocity)) {
            return dispatchNestedFling(velocityX, velocityY, consumed);
        }
        reportScrollStateChange(2);
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        }
        if (!dispatchNestedPreFling(0.0f, velocityY)) {
            this.mFlingRunnable.start((int) velocityY);
        }
        return true;
    }

    public void draw(Canvas canvas) {
        int i = 0;
        super.draw(canvas);
        if (this.mEdgeGlowTop != null) {
            int width;
            int height;
            int translateX;
            int translateY;
            int restoreCount;
            int scrollY = this.mScrollY;
            boolean clipToPadding = getClipToPadding();
            if (clipToPadding) {
                width = (getWidth() - this.mPaddingLeft) - this.mPaddingRight;
                height = (getHeight() - this.mPaddingTop) - this.mPaddingBottom;
                translateX = this.mPaddingLeft;
                translateY = this.mPaddingTop;
            } else {
                width = getWidth();
                height = getHeight();
                translateX = 0;
                translateY = 0;
            }
            if (!this.mEdgeGlowTop.isFinished()) {
                restoreCount = canvas.save();
                canvas.clipRect(translateX, translateY, translateX + width, this.mEdgeGlowTop.getMaxHeight() + translateY);
                canvas.translate((float) translateX, (float) (Math.min(0, this.mFirstPositionDistanceGuess + scrollY) + translateY));
                this.mEdgeGlowTop.setSize(width, height);
                if (this.mEdgeGlowTop.draw(canvas)) {
                    invalidateTopGlow();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mEdgeGlowBottom.isFinished()) {
                restoreCount = canvas.save();
                canvas.clipRect(translateX, (translateY + height) - this.mEdgeGlowBottom.getMaxHeight(), translateX + width, translateY + height);
                int edgeX = (-width) + translateX;
                int max = Math.max(getHeight(), this.mLastPositionDistanceGuess + scrollY);
                if (clipToPadding) {
                    i = this.mPaddingBottom;
                }
                canvas.translate((float) edgeX, (float) (max - i));
                canvas.rotate(180.0f, (float) width, 0.0f);
                this.mEdgeGlowBottom.setSize(width, height);
                if (this.mEdgeGlowBottom.draw(canvas)) {
                    invalidateBottomGlow();
                }
                canvas.restoreToCount(restoreCount);
            }
        }
        drawQuickController(canvas);
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
        if (this.mFastScroll != null && this.mFastScroll.onInterceptHoverEvent(event)) {
            return true;
        }
        if (this.mTwFluidScroll == null || !this.mTwFluidScroll.onInterceptHoverEvent(event)) {
            return super.onInterceptHoverEvent(event);
        }
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int actionMasked = ev.getActionMasked();
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        if (this.mIsDetaching || !isAttachedToWindow()) {
            return false;
        }
        if (this.mFastScroll != null && this.mFastScroll.onInterceptTouchEvent(ev)) {
            return true;
        }
        if (this.mTwFluidScroll != null && this.mTwFluidScroll.onInterceptTouchEvent(ev)) {
            return true;
        }
        int y;
        switch (actionMasked) {
            case 0:
                int touchMode = this.mTouchMode;
                if (touchMode == 6 || touchMode == 5) {
                    this.mMotionCorrection = 0;
                    return true;
                }
                int x = (int) ev.getX();
                y = (int) ev.getY();
                this.mActivePointerId = ev.getPointerId(0);
                int motionPosition = findMotionRow(y);
                if (touchMode != 4 && motionPosition >= 0) {
                    this.mMotionViewOriginalTop = getChildAt(motionPosition - this.mFirstPosition).getTop();
                    this.mMotionX = x;
                    this.mMotionY = y;
                    this.mMotionPosition = motionPosition;
                    this.mTouchMode = 0;
                    clearScrollingCache();
                }
                this.mLastY = Integer.MIN_VALUE;
                initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(ev);
                this.mNestedYOffset = 0;
                startNestedScroll(2);
                if (touchMode == 4) {
                    return true;
                }
                break;
            case 1:
            case 3:
                this.mTouchMode = -1;
                this.mActivePointerId = -1;
                recycleVelocityTracker();
                reportScrollStateChange(0);
                stopNestedScroll();
                break;
            case 2:
                switch (this.mTouchMode) {
                    case 0:
                        int pointerIndex = ev.findPointerIndex(this.mActivePointerId);
                        if (pointerIndex == -1) {
                            pointerIndex = 0;
                            this.mActivePointerId = ev.getPointerId(0);
                        }
                        y = (int) ev.getY(pointerIndex);
                        initVelocityTrackerIfNotExists();
                        this.mVelocityTracker.addMovement(ev);
                        if (startScrollIfNeeded((int) ev.getX(pointerIndex), y, null)) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }
            case 6:
                onSecondaryPointerUp(ev);
                break;
        }
        return false;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & 65280) >> 8;
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mMotionX = (int) ev.getX(newPointerIndex);
            this.mMotionY = (int) ev.getY(newPointerIndex);
            this.mMotionCorrection = 0;
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            this.mLastY = this.mMotionY;
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
            if (this.mDVFSLockAcquired && this.mQCstate == 2) {
                DVFSHelper.onScrollEvent(false);
                this.mDVFSLockAcquired = false;
            }
            if (!(this.mQCstate == 2 || this.mTwScrollingByScrollbar)) {
                if (newState != 0 && this.mLastScrollState == 0) {
                    DVFSHelper.onScrollEvent(true);
                    this.mDVFSLockAcquired = true;
                }
                if (newState == 0 && this.mLastScrollState != 0 && this.mDVFSLockAcquired) {
                    DVFSHelper.onScrollEvent(false);
                    this.mDVFSLockAcquired = false;
                }
            }
            this.mLastScrollState = newState;
            if (this.mOnScrollListener != null) {
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

    AbsPositionScroller createPositionScroller() {
        return new PositionScroller();
    }

    public void smoothScrollToPosition(int position) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = createPositionScroller();
        }
        this.mPositionScroller.start(position);
    }

    public void smoothScrollToPositionFromTop(int position, int offset, int duration) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = createPositionScroller();
        }
        this.mPositionScroller.startWithOffset(position, offset, duration);
    }

    public void smoothScrollToPositionFromTop(int position, int offset) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = createPositionScroller();
        }
        this.mPositionScroller.startWithOffset(position, offset);
    }

    public void smoothScrollToPosition(int position, int boundPosition) {
        if (this.mPositionScroller == null) {
            this.mPositionScroller = createPositionScroller();
        }
        this.mPositionScroller.start(position, boundPosition);
    }

    public void smoothScrollBy(int distance, int duration) {
        smoothScrollBy(distance, duration, false);
    }

    protected boolean isTwShowingScrollbar() {
        return (!super.isTwShowingScrollbar() || this.mFastScrollEnabled || this.mTwFluidScrollEnabled) ? false : true;
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
            mTwScrollAmount = (int) (150.0f * this.mDensityScale);
        }
        boolean isEmpty = this.mTwTwScrollRemains.isEmpty();
        if (Math.abs(distance) > mTwScrollAmount) {
            if (distance > 0) {
                while (distance > mTwScrollAmount) {
                    this.mTwTwScrollRemains.offer(Integer.valueOf(mTwScrollAmount));
                    distance -= mTwScrollAmount;
                }
            } else {
                while (distance < (-mTwScrollAmount)) {
                    this.mTwTwScrollRemains.offer(Integer.valueOf(-mTwScrollAmount));
                    distance += mTwScrollAmount;
                }
            }
        }
        this.mTwTwScrollRemains.offer(Integer.valueOf(distance));
        if (isEmpty) {
            post(this.mTwSmoothScrollByMove);
        }
    }

    void smoothScrollBy(int distance, int duration, boolean linear) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        }
        int firstPos = this.mFirstPosition;
        int childCount = getChildCount();
        int lastPos = firstPos + childCount;
        int topLimit = getPaddingTop();
        int bottomLimit = getHeight() - getPaddingBottom();
        if (distance == 0 || this.mItemCount == 0 || childCount == 0 || ((firstPos == 0 && getChildAt(0).getTop() == topLimit && distance < 0) || (lastPos == this.mItemCount && getChildAt(childCount - 1).getBottom() == bottomLimit && distance > 0))) {
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
                        if (AbsListView.this.mCachingStarted) {
                            AbsListView absListView = AbsListView.this;
                            AbsListView.this.mCachingActive = false;
                            absListView.mCachingStarted = false;
                            AbsListView.this.setChildrenDrawnWithCacheEnabled(false);
                            if ((AbsListView.this.mPersistentDrawingCache & 2) == 0) {
                                AbsListView.this.setChildrenDrawingCacheEnabled(false);
                            }
                            if (!AbsListView.this.isAlwaysDrawnWithCacheEnabled()) {
                                AbsListView.this.invalidate();
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

    boolean trackMotionScroll(int deltaY, int incrementalDeltaY) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return true;
        }
        int firstTop = getChildAt(0).getTop();
        int lastBottom = getChildAt(childCount - 1).getBottom();
        Rect listPadding = this.mListPadding;
        int effectivePaddingTop = 0;
        int effectivePaddingBottom = 0;
        if ((this.mGroupFlags & 34) == 34) {
            effectivePaddingTop = listPadding.top;
            effectivePaddingBottom = listPadding.bottom;
        }
        int spaceAbove = effectivePaddingTop - firstTop;
        int spaceBelow = lastBottom - (getHeight() - effectivePaddingBottom);
        int height = (getHeight() - this.mPaddingBottom) - this.mPaddingTop;
        if (deltaY < 0) {
            deltaY = Math.max(-(height - 1), deltaY);
        } else {
            deltaY = Math.min(height - 1, deltaY);
        }
        if (incrementalDeltaY < 0) {
            incrementalDeltaY = Math.max(-(height - 1), incrementalDeltaY);
        } else {
            incrementalDeltaY = Math.min(height - 1, incrementalDeltaY);
        }
        int firstPosition = this.mFirstPosition;
        if (firstPosition == 0) {
            this.mFirstPositionDistanceGuess = firstTop - listPadding.top;
        } else {
            this.mFirstPositionDistanceGuess += incrementalDeltaY;
        }
        if (firstPosition + childCount == this.mItemCount) {
            this.mLastPositionDistanceGuess = listPadding.bottom + lastBottom;
        } else {
            this.mLastPositionDistanceGuess += incrementalDeltaY;
        }
        boolean cannotScrollDown = firstPosition == 0 && firstTop >= listPadding.top && incrementalDeltaY >= 0;
        boolean cannotScrollUp = firstPosition + childCount == this.mItemCount && lastBottom <= getHeight() - listPadding.bottom && incrementalDeltaY <= 0;
        if (!cannotScrollDown && !cannotScrollUp) {
            boolean down = incrementalDeltaY < 0;
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
            if (!down) {
                int bottom = getHeight() - incrementalDeltaY;
                if ((this.mGroupFlags & 34) == 34) {
                    bottom -= listPadding.bottom;
                }
                for (i = childCount - 1; i >= 0; i--) {
                    child = getChildAt(i);
                    if (child.getTop() <= bottom) {
                        break;
                    }
                    start = i;
                    count++;
                    position = firstPosition + i;
                    child.clearAccessibilityFocus();
                    if (position >= headerViewsCount && position < footerViewsStart) {
                        this.mRecycler.addScrapView(child, position);
                    }
                }
            } else {
                int top = -incrementalDeltaY;
                if ((this.mGroupFlags & 34) == 34) {
                    top += listPadding.top;
                }
                for (i = 0; i < childCount; i++) {
                    child = getChildAt(i);
                    if (child.getBottom() >= top) {
                        break;
                    }
                    count++;
                    position = firstPosition + i;
                    child.clearAccessibilityFocus();
                    if (position >= headerViewsCount && position < footerViewsStart) {
                        this.mRecycler.addScrapView(child, position);
                    }
                }
            }
            this.mMotionViewNewTop = this.mMotionViewOriginalTop + deltaY;
            this.mBlockLayoutRequests = true;
            if (count > 0) {
                detachViewsFromParent(start, count);
                this.mRecycler.removeSkippedScrap();
            }
            if (!awakenScrollBars()) {
                invalidate();
            }
            offsetChildrenTopAndBottom(incrementalDeltaY);
            if (down) {
                this.mFirstPosition += count;
            }
            int absIncrementalDeltaY = Math.abs(incrementalDeltaY);
            if (spaceAbove < absIncrementalDeltaY || spaceBelow < absIncrementalDeltaY) {
                fillGap(down);
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
        } else if (incrementalDeltaY != 0) {
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
            this.mSelectedTop = 0;
            this.mSelectorPosition = -1;
        }
    }

    int reconcileSelectedPosition() {
        int position = this.mSelectedPosition;
        if (position < 0) {
            position = this.mResurrectToPosition;
        }
        return Math.min(Math.max(0, position), this.mItemCount - 1);
    }

    int findClosestMotionRow(int y) {
        int childCount = getChildCount();
        if (childCount == 0) {
            return -1;
        }
        int motionRow = findMotionRow(y);
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
        int selectedTop = 0;
        int childrenTop = this.mListPadding.top;
        int childrenBottom = (this.mBottom - this.mTop) - this.mListPadding.bottom;
        int firstPosition = this.mFirstPosition;
        int toPosition = this.mResurrectToPosition;
        boolean down = true;
        if (toPosition >= firstPosition && toPosition < firstPosition + childCount) {
            selectedPos = toPosition;
            View selected = getChildAt(selectedPos - this.mFirstPosition);
            selectedTop = selected.getTop();
            int selectedBottom = selected.getBottom();
            if (selectedTop < childrenTop) {
                selectedTop = childrenTop + getVerticalFadingEdgeLength();
            } else if (selectedBottom > childrenBottom) {
                selectedTop = (childrenBottom - selected.getMeasuredHeight()) - getVerticalFadingEdgeLength();
            }
        } else if (toPosition < firstPosition) {
            selectedPos = firstPosition;
            for (i = 0; i < childCount; i++) {
                top = getChildAt(i).getTop();
                if (i == 0) {
                    selectedTop = top;
                    if (firstPosition > 0 || top < childrenTop) {
                        childrenTop += getVerticalFadingEdgeLength();
                    }
                }
                if (top >= childrenTop) {
                    selectedPos = firstPosition + i;
                    selectedTop = top;
                    break;
                }
            }
        } else {
            int itemCount = this.mItemCount;
            down = false;
            selectedPos = (firstPosition + childCount) - 1;
            for (i = childCount - 1; i >= 0; i--) {
                View v = getChildAt(i);
                top = v.getTop();
                int bottom = v.getBottom();
                if (i == childCount - 1) {
                    selectedTop = top;
                    if (firstPosition + childCount < itemCount || bottom > childrenBottom) {
                        childrenBottom -= getVerticalFadingEdgeLength();
                    }
                }
                if (bottom <= childrenBottom) {
                    selectedPos = firstPosition + i;
                    selectedTop = top;
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
        this.mSpecificTop = selectedTop;
        selectedPos = lookForSelectablePosition(selectedPos, down);
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
            this.mTwPressItemListArray = new ArrayList();
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
                    int listBottom = getHeight() - getPaddingBottom();
                    View lastChild = getChildAt(childCount - 1);
                    int lastBottom;
                    if (lastChild != null) {
                        lastBottom = lastChild.getBottom();
                    } else {
                        lastBottom = listBottom;
                    }
                    if (this.mFirstPosition + childCount < lastHandledItemCount || lastBottom > listBottom) {
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
                            if (this.mSyncHeight == ((long) getHeight())) {
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
        if (this.mPublicInputConnection == null) {
            this.mDefInputConnection = new BaseInputConnection((View) this, false);
            this.mPublicInputConnection = new InputConnectionWrapper(outAttrs);
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
        if (this.mEdgeGlowTop != null) {
            this.mEdgeGlowTop.finish();
            this.mEdgeGlowBottom.finish();
        }
    }

    public void setRemoteViewsAdapter(Intent intent) {
        if (this.mRemoteAdapter == null || !new FilterComparison(intent).equals(new FilterComparison(this.mRemoteAdapter.getRemoteViewsServiceIntent()))) {
            this.mDeferNotifyDataSetChanged = false;
            this.mRemoteAdapter = new RemoteViewsAdapter(getContext(), intent, this);
            if (this.mRemoteAdapter.isDataReady()) {
                setAdapter(this.mRemoteAdapter);
                Log.d(TAG, "setRemoteAdapter #1 getCount()=" + this.mRemoteAdapter.getCount());
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
            Log.d(TAG, "setRemoteAdapter() #2 getCount()=" + this.mRemoteAdapter.getCount());
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

    int getHeightForPosition(int position) {
        int firstVisiblePosition = getFirstVisiblePosition();
        int childCount = getChildCount();
        int index = position - firstVisiblePosition;
        if (index >= 0 && index < childCount) {
            return getChildAt(index).getHeight();
        }
        View view = obtainView(position, this.mIsScrap);
        view.measure(this.mWidthMeasureSpec, 0);
        int height = view.getMeasuredHeight();
        this.mRecycler.addScrapView(view, position);
        return height;
    }

    public void setSelectionFromTop(int position, int y) {
        if (this.mAdapter != null) {
            if (isInTouchMode()) {
                this.mResurrectToPosition = position;
            } else {
                position = lookForSelectablePosition(position, true);
                if (position >= 0) {
                    setNextSelectedPositionInt(position);
                }
            }
            if (position >= 0) {
                this.mLayoutMode = 4;
                this.mSpecificTop = this.mListPadding.top + y;
                if (this.mNeedSync) {
                    this.mSyncPosition = position;
                    this.mSyncRowId = this.mAdapter.getItemId(position);
                }
                if (this.mPositionScroller != null) {
                    this.mPositionScroller.stop();
                }
                requestLayout();
            }
        }
    }

    protected void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("drawing:cacheColorHint", getCacheColorHint());
        encoder.addProperty("list:fastScrollEnabled", isFastScrollEnabled());
        encoder.addProperty("list:scrollingCacheEnabled", isScrollingCacheEnabled());
        encoder.addProperty("list:smoothScrollbarEnabled", isSmoothScrollbarEnabled());
        encoder.addProperty("list:stackFromBottom", isStackFromBottom());
        encoder.addProperty("list:textFilterEnabled", isTextFilterEnabled());
        View selectedView = getSelectedView();
        if (selectedView != null) {
            encoder.addPropertyKey("selectedView");
            selectedView.encode(encoder);
        }
    }

    private void registerMotionListener() {
    }

    private void unregisterMotionListener() {
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        this.mHoverPosition = -1;
        if (visibility != 0) {
            releaseAllBoosters();
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
    }

    private void handleMessage(Message msg) {
        boolean canScrollDown;
        View child;
        boolean canScrollUp;
        int overscrollMode;
        boolean canOverscroll;
        switch (msg.what) {
            case 0:
                this.mQCstate = 0;
                postInvalidateOnAnimation();
                return;
            case 1:
                this.mHoverRecognitionCurrentTime = System.currentTimeMillis();
                this.mHoverRecognitionDurationTime = (this.mHoverRecognitionCurrentTime - this.mHoverRecognitionStartTime) / 1000;
                if (this.mIsPenHovered && this.mHoverRecognitionCurrentTime - this.mHoverScrollStartTime < this.mHoverScrollTimeInterval) {
                    return;
                }
                if (!this.mIsPenPressed || this.mHoverRecognitionCurrentTime - this.mHoverScrollStartTime >= this.mPenDragScrollTimeInterval) {
                    int offset;
                    if (this.mIsPenHovered && !this.mIsSendHoverScrollState) {
                        if (this.mOnScrollListener != null) {
                            this.mHoverScrollStateForListener = 1;
                            this.mOnScrollListener.onScrollStateChanged(this, 1);
                        }
                        this.mIsSendHoverScrollState = true;
                    }
                    int count = getChildCount();
                    canScrollDown = this.mFirstPosition + count < this.mItemCount;
                    if (!canScrollDown && count > 0) {
                        child = getChildAt(count - 1);
                        canScrollDown = child.getBottom() > this.mBottom - this.mListPadding.bottom || child.getBottom() > getHeight() - this.mListPadding.bottom;
                    }
                    canScrollUp = this.mFirstPosition > 0;
                    if (!canScrollUp && getChildCount() > 0) {
                        canScrollUp = getChildAt(0).getTop() < this.mListPadding.top;
                    }
                    this.mHoverScrollSpeed = (int) (TypedValue.applyDimension(1, this.HOVERSCROLL_SPEED, this.mContext.getResources().getDisplayMetrics()) + 0.5f);
                    if (this.mHoverRecognitionDurationTime > 2 && this.mHoverRecognitionDurationTime < 4) {
                        this.mHoverScrollSpeed += (int) (((double) this.mHoverScrollSpeed) * 0.1d);
                    } else if (this.mHoverRecognitionDurationTime >= 4 && this.mHoverRecognitionDurationTime < 5) {
                        this.mHoverScrollSpeed += (int) (((double) this.mHoverScrollSpeed) * 0.2d);
                    } else if (this.mHoverRecognitionDurationTime >= 5) {
                        this.mHoverScrollSpeed += (int) (((double) this.mHoverScrollSpeed) * 0.3d);
                    }
                    if (this.mHoverScrollDirection == 2) {
                        offset = this.mHoverScrollSpeed * -1;
                        if ((this.mTwTrackedChild == null && this.mTwCloseChildByBottom != null) || (this.mOldHoverScrollDirection != this.mHoverScrollDirection && this.mIsCloseChildSetted)) {
                            this.mTwTrackedChild = this.mTwCloseChildByBottom;
                            this.mTwDistanceFromTrackedChildTop = this.mTwDistanceFromCloseChildBottom;
                            this.mTwTrackedChildPosition = this.mTwCloseChildPositionByBottom;
                            this.mOldHoverScrollDirection = this.mHoverScrollDirection;
                            this.mIsCloseChildSetted = true;
                        }
                    } else {
                        offset = this.mHoverScrollSpeed * 1;
                        if ((this.mTwTrackedChild == null && this.mTwCloseChildByTop != null) || (this.mOldHoverScrollDirection != this.mHoverScrollDirection && this.mIsCloseChildSetted)) {
                            this.mTwTrackedChild = this.mTwCloseChildByTop;
                            this.mTwDistanceFromTrackedChildTop = this.mTwDistanceFromCloseChildTop;
                            this.mTwTrackedChildPosition = this.mTwCloseChildPositionByTop;
                            this.mOldHoverScrollDirection = this.mHoverScrollDirection;
                            this.mIsCloseChildSetted = true;
                        }
                    }
                    if (getChildAt(getChildCount() - 1) == null) {
                        return;
                    }
                    if ((offset >= 0 || !canScrollUp) && (offset <= 0 || !canScrollDown)) {
                        overscrollMode = getOverScrollMode();
                        canOverscroll = overscrollMode == 0 || (overscrollMode == 1 && !contentFits());
                        if (canOverscroll && !this.mIsHoverOverscrolled) {
                            if (this.mHoverScrollDirection == 2) {
                                this.mEdgeGlowTop.setSize(getWidth(), getHeight());
                                this.mEdgeGlowTop.onPull(0.4f);
                                if (!this.mEdgeGlowBottom.isFinished()) {
                                    this.mEdgeGlowBottom.onRelease();
                                }
                            } else if (this.mHoverScrollDirection == 1) {
                                this.mEdgeGlowBottom.setSize(getWidth(), getHeight());
                                this.mEdgeGlowBottom.onPull(0.4f);
                                if (!this.mEdgeGlowTop.isFinished()) {
                                    this.mEdgeGlowTop.onRelease();
                                }
                            }
                            if (!(this.mEdgeGlowTop == null || (this.mEdgeGlowTop.isFinished() && this.mEdgeGlowBottom.isFinished()))) {
                                invalidate();
                            }
                            this.mIsHoverOverscrolled = true;
                        }
                        if (!canOverscroll && !this.mIsHoverOverscrolled) {
                            this.mIsHoverOverscrolled = true;
                            return;
                        }
                        return;
                    }
                    if (this.mFlingRunnable == null) {
                        this.mFlingRunnable = new FlingRunnable();
                    }
                    this.mFlingRunnable.start(offset, true);
                    reportScrollStateChange(2);
                    this.mHoverHandler.sendEmptyMessageDelayed(1, (long) this.HOVERSCROLL_DELAY);
                    return;
                }
                return;
            case 2:
                int childCount = getChildCount();
                if (getChildAt(childCount - 1) != null) {
                    canScrollDown = this.mFirstPosition + childCount < this.mItemCount;
                    if (!canScrollDown && childCount > 0) {
                        child = getChildAt(childCount - 1);
                        canScrollDown = child.getBottom() > this.mBottom - this.mListPadding.bottom || child.getBottom() > getHeight() - this.mListPadding.bottom;
                    }
                    canScrollUp = this.mFirstPosition > 0;
                    if (!canScrollUp && getChildCount() > 0) {
                        canScrollUp = getChildAt(0).getTop() < this.mListPadding.top;
                    }
                    this.mHoverScrollSpeed = (int) (TypedValue.applyDimension(1, HOVERSCROLL_SPEED_FASTER, this.mContext.getResources().getDisplayMetrics()) + 0.5f);
                    int distanceToMove = this.mQCLocation == 2 ? -this.mHoverScrollSpeed : this.mHoverScrollSpeed;
                    if ((distanceToMove >= 0 || !canScrollUp) && (distanceToMove <= 0 || !canScrollDown)) {
                        overscrollMode = getOverScrollMode();
                        canOverscroll = overscrollMode == 0 || (overscrollMode == 1 && !contentFits());
                        if (canOverscroll && !this.mIsHoverOverscrolled) {
                            if (this.mQCLocation == 2) {
                                this.mEdgeGlowTop.setSize(getWidth(), getHeight());
                                this.mEdgeGlowTop.onPull(0.4f);
                                if (!this.mEdgeGlowBottom.isFinished()) {
                                    this.mEdgeGlowBottom.onRelease();
                                }
                            } else if (this.mQCLocation == 4) {
                                this.mEdgeGlowBottom.setSize(getWidth(), getHeight());
                                this.mEdgeGlowBottom.onPull(0.4f);
                                if (!this.mEdgeGlowTop.isFinished()) {
                                    this.mEdgeGlowTop.onRelease();
                                }
                            }
                            if (!(this.mEdgeGlowTop == null || (this.mEdgeGlowTop.isFinished() && this.mEdgeGlowBottom.isFinished()))) {
                                invalidate();
                            }
                            this.mIsHoverOverscrolled = true;
                        }
                        if (!canOverscroll && !this.mIsHoverOverscrolled) {
                            this.mIsHoverOverscrolled = true;
                            return;
                        }
                        return;
                    }
                    if (this.mFlingRunnable == null) {
                        this.mFlingRunnable = new FlingRunnable();
                    }
                    this.mFlingRunnable.start(distanceToMove, true);
                    reportScrollStateChange(2);
                    this.mHoverHandler.sendEmptyMessageDelayed(2, (long) this.HOVERSCROLL_DELAY);
                    return;
                }
                return;
            case 3:
                if (this.mQCLocation == 2) {
                    doScrollToTopEnd();
                    return;
                } else if (this.mQCLocation == 4) {
                    doScrollToBottomEnd();
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    private boolean showPointerIcon(int iconId) {
        try {
            PointerIcon.setHoveringSpenIcon(iconId, -1);
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to change PointerIcon to " + iconId);
            return false;
        }
    }

    private static void log(String log) {
        Log.d(TAG, log);
    }

    public void setEnableMultiFocus(boolean enable) {
        this.mIsMultiFocusEnabled = enable;
    }

    public void setEnableDragBlock(boolean enable) {
        this.mIsDragBlockEnabled = enable;
    }

    public boolean isMultiFocusEnabled() {
        return this.mIsMultiFocusEnabled;
    }

    public void setEnableOnclickInMultiSelectMode(boolean enable) {
        this.mIsTwOnClickEnabled = enable;
    }

    private void addToPressItemListArray(int firstpoint, int secondpoint) {
        if (this.mIsMultiFocusEnabled) {
            if (secondpoint == -1) {
                if (this.mTwPressItemListArray.contains(Integer.valueOf(firstpoint))) {
                    this.mTwPressItemListArray.remove(new Integer(firstpoint));
                } else {
                    this.mTwPressItemListArray.add(Integer.valueOf(firstpoint));
                }
            } else if (firstpoint < secondpoint) {
                checkCount = (secondpoint - firstpoint) + 1;
                for (i = 0; i < checkCount; i++) {
                    if (this.mTwPressItemListArray.contains(Integer.valueOf(firstpoint))) {
                        this.mTwPressItemListArray.remove(new Integer(firstpoint));
                    } else {
                        this.mTwPressItemListArray.add(Integer.valueOf(firstpoint));
                    }
                    firstpoint++;
                }
            } else if (firstpoint > secondpoint) {
                checkCount = (firstpoint - secondpoint) + 1;
                for (i = 0; i < checkCount; i++) {
                    if (this.mTwPressItemListArray.contains(Integer.valueOf(firstpoint))) {
                        this.mTwPressItemListArray.remove(new Integer(firstpoint));
                    } else {
                        this.mTwPressItemListArray.add(Integer.valueOf(firstpoint));
                    }
                    firstpoint--;
                }
            } else if (this.mTwPressItemListArray.contains(Integer.valueOf(firstpoint))) {
                this.mTwPressItemListArray.remove(new Integer(firstpoint));
            } else {
                this.mTwPressItemListArray.add(Integer.valueOf(firstpoint));
            }
            invalidate();
        }
    }

    public void resetPressItemListArray() {
        if (this.mAdapter != null) {
            int checkCount = this.mAdapter.getCount();
            if (this.mTwPressItemListArray != null) {
                this.mTwPressItemListArray.clear();
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

    private boolean isQCSupported() {
        if (sSpenUspLevel >= 3) {
            return true;
        }
        return false;
    }

    private void setupQuickController(int where) {
        int paddingTop;
        int paddingBottom;
        int w = getWidth();
        int h = getHeight();
        int centerX = this.mPaddingLeft + (((w - this.mPaddingLeft) - this.mPaddingRight) / 2);
        int oldLocation = this.mQCLocation;
        this.mQCLocation = where;
        boolean checkBoundary = true;
        String basePkgName = this.mContext.getBasePackageName();
        if (basePkgName != null && (basePkgName.contains("cocktailbarservice") || basePkgName.contains("com.sec.android.app.launcher"))) {
            checkBoundary = false;
        }
        if (checkBoundary) {
            int overlappedW;
            int i = 2;
            int[] locOnScr = new int[]{0, 0};
            getLocationOnScreen(locOnScr);
            DisplayMetrics dm = getResources().getDisplayMetrics();
            if (locOnScr[0] < 0) {
                overlappedW = -locOnScr[0];
                if (overlappedW > this.mPaddingLeft) {
                    centerX += (overlappedW - this.mPaddingLeft) / 2;
                }
            }
            if (locOnScr[0] + w > dm.widthPixels) {
                overlappedW = (locOnScr[0] + w) - dm.widthPixels;
                if (overlappedW > this.mPaddingRight) {
                    centerX -= (overlappedW - this.mPaddingRight) / 2;
                }
            }
        }
        if (this.mIsEnabledPaddingInHoverScroll) {
            paddingTop = this.mListPadding.top;
            paddingBottom = this.mListPadding.bottom;
        } else {
            paddingTop = this.mExtraPaddingInTopHoverArea;
            paddingBottom = this.mExtraPaddingInBottomHoverArea;
        }
        int btnW;
        switch (where) {
            case 1:
                this.mQCRect = new Rect(0, 0, 0, 0);
                break;
            case 2:
                if (this.mQCLocation != oldLocation) {
                    this.mQCBtnDrawable = getResources().getDrawable(R.drawable.list_menu_controller_up);
                    this.mQCBtnPressedDrawable = getResources().getDrawable(R.drawable.list_menu_controller_up_pressed);
                }
                btnW = this.mContext.getResources().getDimensionPixelSize(R.dimen.quickcontroller_size);
                this.mQCRect = new Rect(centerX - (btnW / 2), paddingTop + 0, (btnW / 2) + centerX, this.mContext.getResources().getDimensionPixelSize(R.dimen.quickcontroller_size) + paddingTop);
                break;
            case 3:
                this.mQCRect = new Rect(0, 0, 0, 0);
                break;
            case 4:
                if (this.mQCLocation != oldLocation) {
                    this.mQCBtnDrawable = getResources().getDrawable(R.drawable.list_menu_controller_down);
                    this.mQCBtnPressedDrawable = getResources().getDrawable(R.drawable.list_menu_controller_down_pressed);
                }
                btnW = this.mContext.getResources().getDimensionPixelSize(R.dimen.quickcontroller_size);
                this.mQCRect = new Rect(centerX - (btnW / 2), (h - this.mContext.getResources().getDimensionPixelSize(R.dimen.quickcontroller_size)) - paddingBottom, (btnW / 2) + centerX, h - paddingBottom);
                break;
        }
        this.mQCBtnDrawable.setBounds(this.mQCRect);
        this.mQCBtnPressedDrawable.setBounds(this.mQCRect);
        int[] iArr = new int[2];
        this.mQCBtnFadeInAnimator = ValueAnimator.ofInt(new int[]{0, 255});
        this.mQCBtnFadeInAnimator.setDuration(150);
        this.mQCBtnFadeInAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    AbsListView.this.mQCBtnDrawable.setAlpha(((Integer) animation.getAnimatedValue()).intValue());
                } catch (Exception e) {
                }
            }
        });
        iArr = new int[2];
        this.mQCBtnFadeOutAnimator = ValueAnimator.ofInt(new int[]{0, 255});
        this.mQCBtnFadeOutAnimator.setDuration(150);
        this.mQCBtnFadeOutAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                try {
                    AbsListView.this.mQCBtnDrawable.setAlpha(((Integer) animation.getAnimatedValue()).intValue());
                    AbsListView.this.invalidate();
                } catch (Exception e) {
                }
            }
        });
        this.mQCBtnFadeOutAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                try {
                    AbsListView.this.mIsQCShown = false;
                } catch (Exception e) {
                }
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationCancel(Animator animation) {
            }
        });
    }

    private void drawQuickController(Canvas canvas) {
        int scrollY;
        int restoreCount;
        if (this.mQCstate == 1 || this.mQCstate == 2) {
            scrollY = this.mScrollY;
            restoreCount = canvas.save();
            canvas.translate(0.0f, (float) scrollY);
            if (this.mQCstate == 1) {
                if (this.mIsQCBtnFadeInSet) {
                    this.mQCBtnDrawable.setAlpha(0);
                    post(this.mQCBtnFadeInRunnable);
                    this.mIsQCBtnFadeInSet = false;
                }
                this.mQCBtnDrawable.draw(canvas);
            } else if (this.mQCstate == 2) {
                this.mQCBtnPressedDrawable.draw(canvas);
            }
            canvas.restoreToCount(restoreCount);
            this.mIsQCShown = true;
            this.mIsQCBtnFadeOutSet = true;
        } else if (this.mIsQCShown) {
            scrollY = this.mScrollY;
            restoreCount = canvas.save();
            canvas.translate(0.0f, (float) scrollY);
            if (this.mIsQCBtnFadeOutSet) {
                post(this.mQCBtnFadeOutRunnable);
                this.mIsQCBtnFadeOutSet = false;
            }
            this.mQCBtnDrawable.draw(canvas);
            canvas.restoreToCount(restoreCount);
            this.mIsQCBtnFadeInSet = true;
        }
    }

    private void playQCBtnFadeIn() {
        if (!this.mQCBtnFadeInAnimator.isRunning()) {
            this.mQCBtnFadeInAnimator.setIntValues(new int[]{0, 255});
            this.mQCBtnFadeInAnimator.start();
        }
    }

    private void playQCBtnFadeOut() {
        if (!this.mQCBtnFadeOutAnimator.isRunning()) {
            this.mQCBtnFadeOutAnimator.setIntValues(new int[]{this.mQCBtnDrawable.getAlpha(), 0});
            this.mQCBtnFadeOutAnimator.start();
        }
    }

    private void doScrollToTopEnd() {
        this.mQCScrollDirection = 2;
        this.mQCScrollFrom = getFirstVisiblePosition();
        this.mQCScrollTo = 0;
        this.mQCScrollNext = getFirstVisiblePosition();
        this.mQCScrollingCount = 1;
        if (this.mScrollInterpolator == null) {
            this.mScrollInterpolator = new DecelerateInterpolator();
        }
        post(this.mQCScrollRunnable);
    }

    private void doScrollToBottomEnd() {
        this.mQCScrollDirection = 4;
        this.mQCScrollFrom = getFirstVisiblePosition();
        this.mQCScrollTo = ((ListAdapter) getAdapter()).getCount();
        this.mQCScrollNext = getFirstVisiblePosition();
        this.mQCScrollingCount = 1;
        if (this.mScrollInterpolator == null) {
            this.mScrollInterpolator = new DecelerateInterpolator();
        }
        post(this.mQCScrollRunnable);
    }

    public void setOnFluidScrollEffectListener(OnFluidScrollEffectListener l) {
        this.mOnFluidScrollEffectListener = l;
    }

    public void twSetFluidScrollEnabled(final boolean enabled) {
        if (this.mTwFluidScrollEnabled != enabled) {
            this.mTwFluidScrollEnabled = enabled;
            if (isOwnerThread()) {
                twSetFluidScrollerEnabledUiThread(enabled);
            } else {
                post(new Runnable() {
                    public void run() {
                        AbsListView.this.twSetFluidScrollerEnabledUiThread(enabled);
                    }
                });
            }
        }
    }

    private void twSetFluidScrollerEnabledUiThread(boolean enabled) {
        if (this.mTwFluidScroll != null) {
            this.mTwFluidScroll.setEnabled(enabled);
        } else if (enabled) {
            this.mTwFluidScroll = new FluidScroller(this, this.mFastScrollStyle);
            this.mTwFluidScroll.setEnabled(true);
        }
        resolvePadding();
        if (this.mTwFluidScroll != null) {
            this.mTwFluidScroll.updateLayout();
        }
    }

    private void twSetFluidScrollStyle(int styleResId) {
        if (this.mTwFluidScroll == null) {
            this.mFastScrollStyle = styleResId;
        } else {
            this.mTwFluidScroll.setStyle(styleResId);
        }
    }

    public boolean twIsFluidScrollEnabled() {
        if (this.mTwFluidScroll == null) {
            return this.mTwFluidScrollEnabled;
        }
        return this.mTwFluidScroll.isEnabled();
    }

    public void twSetSmoothScrollEnable(boolean enable) {
        if (this.mFlingRunnable == null) {
            this.mFlingRunnable = new FlingRunnable();
        }
        this.mFlingRunnable.mScroller.twSetSmoothScrollEnable(enable);
    }
}
