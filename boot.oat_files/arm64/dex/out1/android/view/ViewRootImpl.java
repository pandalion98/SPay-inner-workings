package android.view;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.LayoutTransition;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.ActivityManagerNative;
import android.content.ClipDescription;
import android.content.ComponentCallbacks;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.CompatibilityInfo;
import android.content.res.CompatibilityInfo.Translator;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.DVFSHelper;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.provider.Settings$System;
import android.provider.Settings.Secure;
import android.util.AndroidRuntimeException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Slog;
import android.util.TimeUtils;
import android.util.TypedValue;
import android.view.Choreographer.FrameCallback;
import android.view.InputDevice.MotionRange;
import android.view.InputQueue.Callback;
import android.view.KeyCharacterMap.FallbackAction;
import android.view.Surface.OutOfResourcesException;
import android.view.SurfaceHolder.Callback2;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.InternalInsetsInfo;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener;
import android.view.accessibility.AccessibilityManager.HighTextContrastChangeListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.accessibility.IAccessibilityInteractionConnection.Stub;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.interpolator.CircEaseInOut;
import android.view.animation.interpolator.SineEaseIn;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodManager.FinishedInputEventCallback;
import android.view.inputmethod.InputMethodManagerWrapper;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.R;
import com.android.internal.os.SomeArgs;
import com.android.internal.policy.PhoneFallbackEventHandler;
import com.android.internal.telephony.cat.CatEventDownload;
import com.android.internal.view.BaseSurfaceHolder;
import com.android.internal.view.RootViewSurfaceTaker;
import com.samsung.android.multiwindow.MultiWindowFacade;
import com.samsung.android.multiwindow.MultiWindowFeatures;
import com.samsung.android.multiwindow.MultiWindowStyle;
import com.samsung.android.smartclip.SmartClipDataCropperImpl;
import com.samsung.android.smartclip.SmartClipDataExtractionEvent;
import com.samsung.android.smartclip.SmartClipRemoteRequestDispatcher;
import com.samsung.android.smartclip.SmartClipRemoteRequestDispatcher.ViewRootImplGateway;
import com.samsung.android.smartclip.SmartClipRemoteRequestInfo;
import com.samsung.android.smartface.SmartFaceManager;
import com.samsung.android.telephony.MultiSimManager;
import com.samsung.android.toolbox.TwToolBoxManager;
import com.samsung.android.toolbox.TwToolBoxService;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;

public final class ViewRootImpl implements ViewParent, Callbacks, HardwareDrawCallbacks {
    private static final boolean DBG = false;
    private static final boolean DEBUG_CONFIGURATION = false;
    private static final boolean DEBUG_DIALOG = false;
    private static final boolean DEBUG_DRAW = false;
    private static final boolean DEBUG_FPS = false;
    private static final boolean DEBUG_IMF = false;
    private static final boolean DEBUG_INPUT_RESIZE = false;
    private static final boolean DEBUG_INPUT_STAGES = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final boolean DEBUG_ORIENTATION = false;
    private static final boolean DEBUG_TRACKBALL = false;
    private static final boolean DYNAMIC_COLOR_SCALING_ENABLED = true;
    private static final boolean LOCAL_LOGV = false;
    private static final int MAX_QUEUED_INPUT_EVENT_POOL_SIZE = 10;
    static final int MAX_TRACKBALL_DELAY = 250;
    private static final int MSG_ATTACHED_DISPLAY_CHANGED = 1000;
    private static final int MSG_CHECK_FOCUS = 13;
    private static final int MSG_CLEAR_ACCESSIBILITY_FOCUS_HOST = 21;
    private static final int MSG_CLOSE_SYSTEM_DIALOGS = 14;
    private static final int MSG_DIE = 3;
    private static final int MSG_DISPATCH_APP_VISIBILITY = 8;
    private static final int MSG_DISPATCH_COVER_STATE = 29;
    private static final int MSG_DISPATCH_DRAG_EVENT = 15;
    private static final int MSG_DISPATCH_DRAG_LOCATION_EVENT = 16;
    private static final int MSG_DISPATCH_GET_NEW_SURFACE = 9;
    private static final int MSG_DISPATCH_INPUT_EVENT = 7;
    private static final int MSG_DISPATCH_KEY_FROM_IME = 11;
    private static final int MSG_DISPATCH_MULTI_WINDOW_STATE_CHANGED = 31;
    private static final int MSG_DISPATCH_SURFACE_DESTROY_DEFERRED = 30;
    private static final int MSG_DISPATCH_SYSTEM_UI_VISIBILITY = 17;
    private static final int MSG_DISPATCH_WINDOW_ANIMATION_STARTED = 27;
    private static final int MSG_DISPATCH_WINDOW_ANIMATION_STOPPED = 26;
    private static final int MSG_DISPATCH_WINDOW_SHOWN = 25;
    private static final int MSG_FINISH_INPUT_CONNECTION = 12;
    private static final int MSG_INVALIDATE = 1;
    private static final int MSG_INVALIDATE_RECT = 2;
    private static final int MSG_INVALIDATE_WORLD = 22;
    private static final int MSG_PROCESS_INPUT_EVENTS = 19;
    private static final int MSG_RESIZED = 4;
    private static final int MSG_RESIZED_REPORT = 5;
    private static final int MSG_SYNTHESIZE_INPUT_EVENT = 24;
    private static final int MSG_UPDATE_CONFIGURATION = 18;
    private static final int MSG_WINDOW_FOCUS_CHANGED = 6;
    private static final int MSG_WINDOW_MOVED = 23;
    private static final String MULTI_WINDOW_DRAG_AND_DROP_IMAGE = "Multiwindow drag and drop image";
    private static final String MULTI_WINDOW_DRAG_AND_DROP_TEXT = "Multiwindow drag and drop text";
    public static final String PROPERTY_EMULATOR_WIN_OUTSET_BOTTOM_PX = "ro.emu.win_outset_bottom_px";
    private static final String PROPERTY_PROFILE_RENDERING = "viewroot.profile_rendering";
    private static final boolean SAFE_DEBUG;
    private static final String TAG = "ViewRootImpl";
    private static final boolean bDSSEnabled = true;
    static final boolean bFactoryBinary = SystemProperties.get("ro.factory.factory_binary").equals("factory");
    static final Interpolator mResizeInterpolator = new AccelerateDecelerateInterpolator();
    private static boolean mUseGestureDetectorTouchEventEx = true;
    static int sBufferCount = -1;
    static final ArrayList<ComponentCallbacks> sConfigCallbacks = new ArrayList();
    static double sDTSFactor = 1.0d;
    static int sDcsFormat = -1;
    static double sDssFactor = 1.0d;
    static boolean sFirstDrawComplete = false;
    static final ArrayList<Runnable> sFirstDrawHandlers = new ArrayList();
    static boolean sIsDcsEnabledApp = false;
    public static boolean sIsHighContrastTextEnabled = false;
    public static final boolean sIsNovelModel = Build.PRODUCT.startsWith("novel");
    static boolean sRendererDemoted = false;
    static final ThreadLocal<RunQueue> sRunQueues = new ThreadLocal();
    static int sSVBufferCount = -1;
    View mAccessibilityFocusedHost;
    AccessibilityNodeInfo mAccessibilityFocusedVirtualView;
    AccessibilityInteractionConnectionManager mAccessibilityInteractionConnectionManager;
    AccessibilityInteractionController mAccessibilityInteractionController;
    final AccessibilityManager mAccessibilityManager;
    boolean mAdded;
    boolean mAddedTouchMode;
    boolean mAppVisible = true;
    boolean mApplyInsetsRequested;
    final AttachInfo mAttachInfo;
    AudioManager mAudioManager;
    final String mBasePackageName;
    boolean mBlockResizeBuffer;
    Choreographer mChoreographer;
    int mClientWindowLayoutFlags;
    final Rect mCocktailBar;
    private CocktailGripDetector mCocktailGripDetector = null;
    final ConsumeBatchedInputImmediatelyRunnable mConsumeBatchedInputImmediatelyRunnable;
    boolean mConsumeBatchedInputImmediatelyScheduled;
    boolean mConsumeBatchedInputScheduled;
    final ConsumeBatchedInputRunnable mConsumedBatchedInputRunnable;
    private ValueAnimator mContentResizeAnimator;
    final ContentResolver mContentResolver;
    final Context mContext;
    int mCurScrollY;
    View mCurrentDragView;
    private View mCurrentWritingBuddyView;
    private final int mDensity;
    Rect mDirty;
    final Rect mDispatchContentInsets = new Rect();
    final Rect mDispatchStableInsets = new Rect();
    Display mDisplay;
    final DisplayAdjustments mDisplayAdjustments;
    private final DisplayListener mDisplayListener;
    final DisplayManager mDisplayManager;
    ClipDescription mDragDescription;
    final PointF mDragPoint = new PointF();
    boolean mDrawDuringWindowsAnimating;
    boolean mDrawingAllowed;
    private float mDssScale = 1.0f;
    FallbackEventHandler mFallbackEventHandler;
    boolean mFirst;
    InputStage mFirstInputStage;
    InputStage mFirstPostImeInputStage;
    private boolean mFocusDragStartWin = false;
    private boolean mForceDraw;
    private int mFpsNumFrames;
    private long mFpsPrevTime = -1;
    private long mFpsStartTime = -1;
    boolean mFullRedrawNeeded;
    final HCTRelayoutHandler mHCTRelayoutHandler;
    final ViewRootHandler mHandler;
    boolean mHandlingLayoutInLayoutRequest = false;
    int mHardwareXOffset;
    int mHardwareYOffset;
    boolean mHasHadWindowFocus;
    int mHeight;
    Interpolator mHideInterpolator;
    HighContrastTextManager mHighContrastTextManager;
    private boolean mInLayout = false;
    InputChannel mInputChannel;
    protected final InputEventConsistencyVerifier mInputEventConsistencyVerifier;
    WindowInputEventReceiver mInputEventReceiver;
    InputQueue mInputQueue;
    Callback mInputQueueCallback;
    final InvalidateOnAnimationRunnable mInvalidateOnAnimationRunnable;
    boolean mIsAnimating;
    boolean mIsCreating;
    boolean mIsDrawing;
    boolean mIsInTraversal;
    boolean mIsThisWindowDontNeedSurfaceBuffer;
    final Configuration mLastConfiguration = new Configuration();
    final InternalInsetsInfo mLastGivenInsets = new InternalInsetsInfo();
    boolean mLastInCompatMode = false;
    MultiWindowStyle mLastMeasuredMultiWindowStyle = null;
    boolean mLastOverscanRequested;
    MultiWindowStyle mLastPerformedMultiWindowStyle = new MultiWindowStyle();
    WeakReference<View> mLastScrolledFocus;
    int mLastSystemUiVisibility;
    final PointF mLastTouchPoint = new PointF();
    boolean mLastWasImTarget;
    private WindowInsets mLastWindowInsets;
    boolean mLayoutRequested;
    ArrayList<View> mLayoutRequesters = new ArrayList();
    volatile Object mLocalDragState;
    final WindowLeaked mLocation;
    private MotionEventMonitor mMotionEventMonitor;
    Rect mMultiWindowBoarderRect = new Rect();
    boolean mNewScaleFactorNeeded = false;
    boolean mNewSurfaceNeeded;
    private final int mNoncompatDensity;
    boolean mOrientationChanged = false;
    int mOrigWindowType = -1;
    boolean mPausedForTransition = false;
    final Configuration mPendingConfiguration = new Configuration();
    final Rect mPendingContentInsets = new Rect();
    int mPendingInputEventCount;
    QueuedInputEvent mPendingInputEventHead;
    String mPendingInputEventQueueLengthCounterName = "pq";
    QueuedInputEvent mPendingInputEventTail;
    final Rect mPendingOutsets = new Rect();
    final Rect mPendingOverscanInsets = new Rect();
    final Rect mPendingStableInsets = new Rect();
    private ArrayList<LayoutTransition> mPendingTransitions;
    final Rect mPendingVisibleInsets = new Rect();
    private Rect mPreContentInsets = new Rect();
    final Region mPreviousTransparentRegion;
    boolean mProcessInputEventsScheduled;
    private boolean mProfile;
    private boolean mProfileRendering;
    private QueuedInputEvent mQueuedInputEventPool;
    private int mQueuedInputEventPoolSize;
    private int mRemainingFrameCount;
    private boolean mRemoved;
    private FrameCallback mRenderProfiler;
    private boolean mRenderProfilingEnabled;
    boolean mReportNextDraw;
    int mReportedViewVisibility;
    int mResizeAlpha;
    private boolean mResizeAnimating = false;
    private DVFSHelper mResizeBooster = null;
    HardwareLayer mResizeBuffer;
    int mResizeBufferDuration;
    long mResizeBufferStartTime;
    final Paint mResizePaint;
    PointF mScaleFactor = new PointF(1.0f, 1.0f);
    boolean mScrollMayChange;
    int mScrollY;
    Scroller mScroller;
    SendWindowContentChangedAccessibilityEvent mSendWindowContentChangedAccessibilityEvent;
    int mSeq;
    Interpolator mShowInterpolator;
    private boolean mSkipPanScrollEnterAnimation;
    private boolean mSkipPanScrollExitAnimation;
    SmartClipRemoteRequestDispatcherProxy mSmartClipDispatcherProxy = null;
    int mSoftInputMode;
    boolean mStopped = false;
    final Surface mSurface = new Surface();
    BaseSurfaceHolder mSurfaceHolder;
    Callback2 mSurfaceHolderCallback;
    InputStage mSyntheticInputStage;
    final int mTargetSdkVersion;
    HashSet<View> mTempHashSet;
    final Rect mTempRect;
    final Thread mThread;
    final int[] mTmpLocation = new int[2];
    Matrix mTmpMotionEventMatrix = new Matrix(Matrix.IDENTITY_MATRIX);
    final TypedValue mTmpValue = new TypedValue();
    private TwToolBoxManager mToolBoxManager = null;
    Translator mTranslator;
    final Region mTransparentRegion;
    int mTraversalBarrier;
    final TraversalRunnable mTraversalRunnable;
    boolean mTraversalScheduled;
    boolean mTwDrawDuringWindowsAnimating = false;
    private boolean mTwToolBoxTracking;
    boolean mUnbufferedInputDispatch;
    public boolean mUseFloatingToolBox = false;
    View mView;
    final ViewConfiguration mViewConfiguration;
    private int mViewLayoutDirectionInitial;
    int mViewVisibility;
    final Rect mVisRect;
    int mWidth;
    boolean mWillDrawSoon;
    final Rect mWinFrame;
    final W mWindow;
    final LayoutParams mWindowAttributes = new LayoutParams();
    boolean mWindowAttributesChanged = false;
    int mWindowAttributesChangesFlag = 0;
    final IWindowSession mWindowSession;
    boolean mWindowsAnimating;
    boolean mWindowsExitAnimating;

    static final class AccessibilityInteractionConnection extends Stub {
        private final WeakReference<ViewRootImpl> mViewRootImpl;

        AccessibilityInteractionConnection(ViewRootImpl viewRootImpl) {
            this.mViewRootImpl = new WeakReference(viewRootImpl);
        }

        public void findAccessibilityNodeInfoByAccessibilityId(long accessibilityNodeId, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl == null || viewRootImpl.mView == null) {
                try {
                    callback.setFindAccessibilityNodeInfosResult(null, interactionId);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            viewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfoByAccessibilityIdClientThread(accessibilityNodeId, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
        }

        public void performAccessibilityAction(long accessibilityNodeId, int action, Bundle arguments, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl == null || viewRootImpl.mView == null) {
                try {
                    callback.setPerformAccessibilityActionResult(false, interactionId);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            viewRootImpl.getAccessibilityInteractionController().performAccessibilityActionClientThread(accessibilityNodeId, action, arguments, interactionId, callback, flags, interrogatingPid, interrogatingTid);
        }

        public void findAccessibilityNodeInfosByViewId(long accessibilityNodeId, String viewId, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl == null || viewRootImpl.mView == null) {
                try {
                    callback.setFindAccessibilityNodeInfoResult(null, interactionId);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            viewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfosByViewIdClientThread(accessibilityNodeId, viewId, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
        }

        public void findAccessibilityNodeInfosByText(long accessibilityNodeId, String text, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl == null || viewRootImpl.mView == null) {
                try {
                    callback.setFindAccessibilityNodeInfosResult(null, interactionId);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            viewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfosByTextClientThread(accessibilityNodeId, text, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
        }

        public void findFocus(long accessibilityNodeId, int focusType, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl == null || viewRootImpl.mView == null) {
                try {
                    callback.setFindAccessibilityNodeInfoResult(null, interactionId);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            viewRootImpl.getAccessibilityInteractionController().findFocusClientThread(accessibilityNodeId, focusType, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
        }

        public void focusSearch(long accessibilityNodeId, int direction, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl == null || viewRootImpl.mView == null) {
                try {
                    callback.setFindAccessibilityNodeInfoResult(null, interactionId);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            viewRootImpl.getAccessibilityInteractionController().focusSearchClientThread(accessibilityNodeId, direction, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
        }
    }

    final class AccessibilityInteractionConnectionManager implements AccessibilityStateChangeListener {
        AccessibilityInteractionConnectionManager() {
        }

        public void onAccessibilityStateChanged(boolean enabled) {
            if (enabled) {
                ensureConnection();
                if (ViewRootImpl.this.mAttachInfo.mHasWindowFocus && ViewRootImpl.this.mView != null) {
                    ViewRootImpl.this.mView.sendAccessibilityEvent(32);
                    View focusedView = ViewRootImpl.this.mView.findFocus();
                    if (focusedView != null && focusedView != ViewRootImpl.this.mView) {
                        focusedView.sendAccessibilityEvent(8);
                        return;
                    }
                    return;
                }
                return;
            }
            ensureNoConnection();
            ViewRootImpl.this.mHandler.obtainMessage(21).sendToTarget();
        }

        public void ensureConnection() {
            if (!(ViewRootImpl.this.mAttachInfo.mAccessibilityWindowId != Integer.MAX_VALUE)) {
                ViewRootImpl.this.mAttachInfo.mAccessibilityWindowId = ViewRootImpl.this.mAccessibilityManager.addAccessibilityInteractionConnection(ViewRootImpl.this.mWindow, new AccessibilityInteractionConnection(ViewRootImpl.this));
            }
        }

        public void ensureNoConnection() {
            if (ViewRootImpl.this.mAttachInfo.mAccessibilityWindowId != Integer.MAX_VALUE) {
                ViewRootImpl.this.mAttachInfo.mAccessibilityWindowId = Integer.MAX_VALUE;
                ViewRootImpl.this.mAccessibilityManager.removeAccessibilityInteractionConnection(ViewRootImpl.this.mWindow);
            }
        }
    }

    abstract class InputStage {
        protected static final int FINISH_HANDLED = 1;
        protected static final int FINISH_NOT_HANDLED = 2;
        protected static final int FORWARD = 0;
        private final InputStage mNext;

        public InputStage(InputStage next) {
            this.mNext = next;
        }

        public final void deliver(QueuedInputEvent q) {
            if ((q.mFlags & 4) != 0) {
                forward(q);
            } else if (shouldDropInputEvent(q)) {
                finish(q, false);
            } else {
                apply(q, onProcess(q));
            }
        }

        protected void finish(QueuedInputEvent q, boolean handled) {
            q.mFlags |= 4;
            if (handled) {
                q.mFlags |= 8;
            }
            forward(q);
        }

        protected void forward(QueuedInputEvent q) {
            onDeliverToNext(q);
        }

        protected void apply(QueuedInputEvent q, int result) {
            if (result == 0) {
                forward(q);
            } else if (result == 1) {
                finish(q, true);
            } else if (result == 2) {
                finish(q, false);
            } else {
                throw new IllegalArgumentException("Invalid result: " + result);
            }
        }

        protected int onProcess(QueuedInputEvent q) {
            return 0;
        }

        protected void onDeliverToNext(QueuedInputEvent q) {
            if (this.mNext != null) {
                this.mNext.deliver(q);
            } else {
                ViewRootImpl.this.finishInputEvent(q);
            }
        }

        protected boolean shouldDropInputEvent(QueuedInputEvent q) {
            if (ViewRootImpl.this.mView == null || !ViewRootImpl.this.mAdded) {
                Slog.w(ViewRootImpl.TAG, "Dropping event due to root view being removed: " + (ViewRootImpl.SAFE_DEBUG ? q.mEvent : ""));
                Slog.e(ViewRootImpl.TAG, "mStopped=" + ViewRootImpl.this.mStopped + " mHasWindowFocus=" + ViewRootImpl.this.mAttachInfo.mHasWindowFocus + " mPausedForTransition=" + ViewRootImpl.this.mPausedForTransition);
                return true;
            } else if (((ViewRootImpl.this.mAttachInfo.mHasWindowFocus && !ViewRootImpl.this.mStopped) || q.mEvent.isFromSource(2)) && (!ViewRootImpl.this.mPausedForTransition || isBack(q.mEvent))) {
                if (ViewRootImpl.this.mStopped || ViewRootImpl.this.mPausedForTransition) {
                    Log.e(ViewRootImpl.TAG, "InputEvent was not dropped but this window had been stopped.");
                    Slog.e(ViewRootImpl.TAG, "mStopped=" + ViewRootImpl.this.mStopped + " mHasWindowFocus=" + ViewRootImpl.this.mAttachInfo.mHasWindowFocus + " mPausedForTransition=" + ViewRootImpl.this.mPausedForTransition);
                }
                return false;
            } else if (ViewRootImpl.isTerminalInputEvent(q.mEvent)) {
                q.mEvent.cancel();
                Slog.w(ViewRootImpl.TAG, "Cancelling event due to no window focus: " + (ViewRootImpl.SAFE_DEBUG ? q.mEvent : ""));
                Slog.e(ViewRootImpl.TAG, "mStopped=" + ViewRootImpl.this.mStopped + " mHasWindowFocus=" + ViewRootImpl.this.mAttachInfo.mHasWindowFocus + " mPausedForTransition=" + ViewRootImpl.this.mPausedForTransition);
                return false;
            } else {
                Slog.w(ViewRootImpl.TAG, "Dropping event due to no window focus: " + (ViewRootImpl.SAFE_DEBUG ? q.mEvent : ""));
                Slog.e(ViewRootImpl.TAG, "mStopped=" + ViewRootImpl.this.mStopped + " mHasWindowFocus=" + ViewRootImpl.this.mAttachInfo.mHasWindowFocus + " mPausedForTransition=" + ViewRootImpl.this.mPausedForTransition);
                return true;
            }
        }

        void dump(String prefix, PrintWriter writer) {
            if (this.mNext != null) {
                this.mNext.dump(prefix, writer);
            }
        }

        private boolean isBack(InputEvent event) {
            if ((event instanceof KeyEvent) && ((KeyEvent) event).getKeyCode() == 4) {
                return true;
            }
            return false;
        }
    }

    abstract class AsyncInputStage extends InputStage {
        protected static final int DEFER = 3;
        private QueuedInputEvent mQueueHead;
        private int mQueueLength;
        private QueuedInputEvent mQueueTail;
        private final String mTraceCounter;

        public AsyncInputStage(InputStage next, String traceCounter) {
            super(next);
            this.mTraceCounter = traceCounter;
        }

        protected void defer(QueuedInputEvent q) {
            q.mFlags |= 2;
            enqueue(q);
        }

        protected void forward(QueuedInputEvent q) {
            q.mFlags &= -3;
            QueuedInputEvent curr = this.mQueueHead;
            if (curr == null) {
                super.forward(q);
                return;
            }
            int deviceId = q.mEvent.getDeviceId();
            QueuedInputEvent prev = null;
            boolean blocked = false;
            while (curr != null && curr != q) {
                if (!blocked && deviceId == curr.mEvent.getDeviceId()) {
                    blocked = true;
                }
                prev = curr;
                curr = curr.mNext;
            }
            if (!blocked) {
                if (curr != null) {
                    curr = curr.mNext;
                    dequeue(q, prev);
                }
                super.forward(q);
                while (curr != null) {
                    if (deviceId != curr.mEvent.getDeviceId()) {
                        prev = curr;
                        curr = curr.mNext;
                    } else if ((curr.mFlags & 2) == 0) {
                        QueuedInputEvent next = curr.mNext;
                        dequeue(curr, prev);
                        super.forward(curr);
                        curr = next;
                    } else {
                        return;
                    }
                }
            } else if (curr == null) {
                enqueue(q);
            }
        }

        protected void apply(QueuedInputEvent q, int result) {
            if (result == 3) {
                defer(q);
            } else {
                super.apply(q, result);
            }
        }

        private void enqueue(QueuedInputEvent q) {
            if (this.mQueueTail == null) {
                this.mQueueHead = q;
                this.mQueueTail = q;
            } else {
                this.mQueueTail.mNext = q;
                this.mQueueTail = q;
            }
            this.mQueueLength++;
            Trace.traceCounter(4, this.mTraceCounter, this.mQueueLength);
        }

        private void dequeue(QueuedInputEvent q, QueuedInputEvent prev) {
            if (prev == null) {
                this.mQueueHead = q.mNext;
            } else {
                prev.mNext = q.mNext;
            }
            if (this.mQueueTail == q) {
                this.mQueueTail = prev;
            }
            q.mNext = null;
            this.mQueueLength--;
            Trace.traceCounter(4, this.mTraceCounter, this.mQueueLength);
        }

        void dump(String prefix, PrintWriter writer) {
            writer.print(prefix);
            writer.print(getClass().getName());
            writer.print(": mQueueLength=");
            writer.println(this.mQueueLength);
            super.dump(prefix, writer);
        }
    }

    public static final class CalledFromWrongThreadException extends AndroidRuntimeException {
        public CalledFromWrongThreadException(String msg) {
            super(msg);
        }
    }

    final class CocktailGripDetector {
        private final int DEADZONE_THRESHOLD = 3;
        private final String TAG = "CocktailGripDetector";
        private int mDeadSize = 0;
        private Rect mDeadZone = new Rect();
        private boolean mbTouchBlock = false;

        public CocktailGripDetector() {
            this.mDeadSize = (int) (3.0f * ViewRootImpl.this.mContext.getResources().getDisplayMetrics().density);
        }

        public boolean checkGrip(MotionEvent event) {
            switch (event.getAction() & 255) {
                case 0:
                    if (ViewRootImpl.this.mWindowAttributes.type == 98) {
                        this.mbTouchBlock = false;
                        this.mDeadZone.setEmpty();
                        if (!ViewRootImpl.this.mWinFrame.isEmpty()) {
                            if (ViewRootImpl.this.mLastConfiguration.orientation == 1) {
                                if (ViewRootImpl.this.mWinFrame.left == 0) {
                                    this.mDeadZone.set(ViewRootImpl.this.mWinFrame);
                                    this.mDeadZone.right = this.mDeadSize;
                                } else {
                                    this.mDeadZone.set(ViewRootImpl.this.mWinFrame);
                                    this.mDeadZone.left = this.mDeadZone.right - this.mDeadSize;
                                }
                            } else if (ViewRootImpl.this.mWinFrame.top != 0) {
                                this.mDeadZone.set(ViewRootImpl.this.mWinFrame);
                                this.mDeadZone.top = this.mDeadZone.bottom - this.mDeadSize;
                            }
                        }
                        if (this.mDeadZone.contains((int) event.getRawXForScaledWindow(), (int) event.getRawYForScaledWindow())) {
                            Log.secW("CocktailGripDetector", "Touch Block : Dead Zone (DOWN) !!!");
                            this.mbTouchBlock = true;
                            return true;
                        }
                    }
                    break;
                case 1:
                    if (event.getToolType(event.getActionIndex()) == 1 && event.getAxisValue(49, event.getActionIndex()) > 0.0f) {
                        Log.secW("CocktailGripDetector", "Touch Block : Grip Flag !!!");
                        return true;
                    } else if (this.mbTouchBlock) {
                        this.mbTouchBlock = false;
                        Log.secW("CocktailGripDetector", "Touch Block : Dead Zone !!!");
                        return true;
                    }
                    break;
            }
            return false;
        }

        public boolean checkBlock() {
            return this.mbTouchBlock;
        }
    }

    final class ConsumeBatchedInputImmediatelyRunnable implements Runnable {
        ConsumeBatchedInputImmediatelyRunnable() {
        }

        public void run() {
            ViewRootImpl.this.doConsumeBatchedInput(-1);
        }
    }

    final class ConsumeBatchedInputRunnable implements Runnable {
        ConsumeBatchedInputRunnable() {
        }

        public void run() {
            ViewRootImpl.this.doConsumeBatchedInput(ViewRootImpl.this.mChoreographer.getFrameTimeNanos());
        }
    }

    private static final class CursorColor {
        private static final int BLUE = 1;
        private static final int GRAY = 6;
        private static final int GREEN = 2;
        private static final int ORANGE = 4;
        private static final int RED = 5;
        private static final int YELLOW = 3;

        private CursorColor() {
        }
    }

    final class EarlyPostImeInputStage extends InputStage {
        public EarlyPostImeInputStage(InputStage next) {
            super(next);
        }

        protected int onProcess(QueuedInputEvent q) {
            if (q.mEvent instanceof KeyEvent) {
                return processKeyEvent(q);
            }
            if ((q.mEvent.getSource() & 2) != 0) {
                return processPointerEvent(q);
            }
            return 0;
        }

        private int processKeyEvent(QueuedInputEvent q) {
            KeyEvent event = q.mEvent;
            if (ViewRootImpl.this.checkForLeavingTouchModeAndConsume(event)) {
                return 1;
            }
            ViewRootImpl.this.mFallbackEventHandler.preDispatchKeyEvent(event);
            return 0;
        }

        private int processPointerEvent(QueuedInputEvent q) {
            MotionEvent event = q.mEvent;
            if (ViewRootImpl.this.mTranslator != null) {
                ViewRootImpl.this.mTranslator.translateEventInScreenToAppWindow(event);
            }
            int action = event.getAction();
            if (action == 0 || action == 8) {
                ViewRootImpl.this.ensureTouchMode(true);
            }
            if (ViewRootImpl.this.mCurScrollY != 0) {
                event.offsetLocation(0.0f, (float) ViewRootImpl.this.mCurScrollY);
            }
            if (event.isTouchEvent()) {
                ViewRootImpl.this.mLastTouchPoint.x = event.getRawXForScaledWindow();
                ViewRootImpl.this.mLastTouchPoint.y = event.getRawYForScaledWindow();
            }
            return 0;
        }
    }

    private final class HCTRelayoutHandler extends Handler {
        public static final int MSG_NEED_TO_DO_RELAYOUT = 1;

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    ViewRootImpl.this.doRelayoutForHCT(true);
                    return;
                default:
                    return;
            }
        }
    }

    final class HighContrastTextManager implements HighTextContrastChangeListener {
        HighContrastTextManager() {
            ViewRootImpl.this.mAttachInfo.mHighContrastText = ViewRootImpl.this.mAccessibilityManager.isHighTextContrastEnabled();
            ViewRootImpl.sIsHighContrastTextEnabled = ViewRootImpl.this.mAttachInfo.mHighContrastText;
        }

        public void onHighTextContrastStateChanged(boolean enabled) {
            ViewRootImpl.this.mAttachInfo.mHighContrastText = enabled;
            ViewRootImpl.sIsHighContrastTextEnabled = enabled;
            ViewRootImpl.this.doRelayoutForHCT(false);
        }
    }

    final class ImeInputStage extends AsyncInputStage implements FinishedInputEventCallback {
        public ImeInputStage(InputStage next, String traceCounter) {
            super(next, traceCounter);
        }

        protected int onProcess(QueuedInputEvent q) {
            if (ViewRootImpl.this.mLastWasImTarget && !ViewRootImpl.this.isInLocalFocusMode()) {
                InputMethodManager imm = InputMethodManager.peekInstance();
                if (imm != null) {
                    int result = imm.dispatchInputEvent(q.mEvent, q, this, ViewRootImpl.this.mHandler);
                    if (result == 1) {
                        return 1;
                    }
                    if (result == 0) {
                        return 0;
                    }
                    return 3;
                }
            }
            return 0;
        }

        public void onFinishedInputEvent(Object token, boolean handled) {
            QueuedInputEvent q = (QueuedInputEvent) token;
            if (handled) {
                finish(q, true);
                Log.d(ViewRootImpl.TAG, "The input has been finished in ImeInputStage.");
                return;
            }
            forward(q);
        }
    }

    final class InvalidateOnAnimationRunnable implements Runnable {
        private boolean mPosted;
        private InvalidateInfo[] mTempViewRects;
        private View[] mTempViews;
        private final ArrayList<InvalidateInfo> mViewRects = new ArrayList();
        private final ArrayList<View> mViews = new ArrayList();

        InvalidateOnAnimationRunnable() {
        }

        public void addView(View view) {
            synchronized (this) {
                this.mViews.add(view);
                postIfNeededLocked();
            }
        }

        public void addViewRect(InvalidateInfo info) {
            synchronized (this) {
                this.mViewRects.add(info);
                postIfNeededLocked();
            }
        }

        public void removeView(View view) {
            synchronized (this) {
                this.mViews.remove(view);
                int i = this.mViewRects.size();
                while (true) {
                    int i2 = i - 1;
                    if (i <= 0) {
                        break;
                    }
                    InvalidateInfo info = (InvalidateInfo) this.mViewRects.get(i2);
                    if (info.target == view) {
                        this.mViewRects.remove(i2);
                        info.recycle();
                    }
                    i = i2;
                }
                if (this.mPosted && this.mViews.isEmpty() && this.mViewRects.isEmpty()) {
                    ViewRootImpl.this.mChoreographer.removeCallbacks(1, this, null);
                    this.mPosted = false;
                }
            }
        }

        public void run() {
            int i;
            synchronized (this) {
                this.mPosted = false;
                int viewCount = this.mViews.size();
                if (viewCount != 0) {
                    this.mTempViews = (View[]) this.mViews.toArray(this.mTempViews != null ? this.mTempViews : new View[viewCount]);
                    this.mViews.clear();
                }
                int viewRectCount = this.mViewRects.size();
                if (viewRectCount != 0) {
                    this.mTempViewRects = (InvalidateInfo[]) this.mViewRects.toArray(this.mTempViewRects != null ? this.mTempViewRects : new InvalidateInfo[viewRectCount]);
                    this.mViewRects.clear();
                }
            }
            for (i = 0; i < viewCount; i++) {
                this.mTempViews[i].invalidate();
                this.mTempViews[i] = null;
            }
            for (i = 0; i < viewRectCount; i++) {
                InvalidateInfo info = this.mTempViewRects[i];
                info.target.invalidate(info.left, info.top, info.right, info.bottom);
                info.recycle();
            }
        }

        private void postIfNeededLocked() {
            if (!this.mPosted) {
                ViewRootImpl.this.mChoreographer.postCallback(1, this, null);
                this.mPosted = true;
            }
        }
    }

    public static class MotionEventMonitor {
        private static boolean DEBUG = false;
        private static final String TAG = "MotionEventMonitor";
        private ArrayList<OnTouchListener> mListeners = new ArrayList();

        public interface OnTouchListener {
            void onTouch(MotionEvent motionEvent);
        }

        public void registerMotionEventMonitor(OnTouchListener listener) {
            if (this.mListeners.size() > 0) {
                Log.e(TAG, "registerMotionEventMonitor : Just one event listener is allowed");
                return;
            }
            this.mListeners.add(listener);
            if (DEBUG) {
                Log.d(TAG, "registerMotionEventMonitor : Listener count=" + this.mListeners.size());
            }
        }

        public void unregisterMotionEventMonitor(OnTouchListener listener) {
            this.mListeners.remove(listener);
            if (DEBUG) {
                Log.d(TAG, "unregisterMotionEventMonitor : Listener count=" + this.mListeners.size());
            }
        }

        public void dispatchInputEvent(InputEvent event) {
            if (this.mListeners.size() != 0) {
                if (event instanceof MotionEvent) {
                    MotionEvent motionEvent = (MotionEvent) event;
                    int action = motionEvent.getAction();
                    if (DEBUG) {
                        Log.d(TAG, "dispatchInputEvent : action=" + action);
                    }
                    switch (action) {
                        case 0:
                        case 1:
                        case 3:
                        case 7:
                        case 9:
                        case 10:
                            notifyTouchEvent(motionEvent);
                            return;
                        default:
                            return;
                    }
                } else if (DEBUG) {
                    Log.d(TAG, "dispatchInputEvent : The event is not instance of MotionEvent");
                }
            }
        }

        private void notifyTouchEvent(MotionEvent event) {
            int cnt = this.mListeners.size();
            Log.d(TAG, "notifyTouchEvent : Listener cnt=" + cnt);
            for (int i = 0; i < cnt; i++) {
                OnTouchListener listener = (OnTouchListener) this.mListeners.get(i);
                if (listener != null) {
                    listener.onTouch(event);
                }
            }
        }
    }

    final class NativePostImeInputStage extends AsyncInputStage implements InputQueue.FinishedInputEventCallback {
        public NativePostImeInputStage(InputStage next, String traceCounter) {
            super(next, traceCounter);
        }

        protected int onProcess(QueuedInputEvent q) {
            if (ViewRootImpl.this.mInputQueue == null) {
                return 0;
            }
            ViewRootImpl.this.mInputQueue.sendInputEvent(q.mEvent, q, false, this);
            return 3;
        }

        public void onFinishedInputEvent(Object token, boolean handled) {
            QueuedInputEvent q = (QueuedInputEvent) token;
            if (handled) {
                finish(q, true);
            } else {
                forward(q);
            }
        }
    }

    final class NativePreImeInputStage extends AsyncInputStage implements InputQueue.FinishedInputEventCallback {
        public NativePreImeInputStage(InputStage next, String traceCounter) {
            super(next, traceCounter);
        }

        protected int onProcess(QueuedInputEvent q) {
            if (ViewRootImpl.this.mInputQueue == null || !(q.mEvent instanceof KeyEvent)) {
                return 0;
            }
            ViewRootImpl.this.mInputQueue.sendInputEvent(q.mEvent, q, true, this);
            return 3;
        }

        public void onFinishedInputEvent(Object token, boolean handled) {
            QueuedInputEvent q = (QueuedInputEvent) token;
            if (handled) {
                finish(q, true);
            } else {
                forward(q);
            }
        }
    }

    private static final class QueuedInputEvent {
        public static final int FLAG_DEFERRED = 2;
        public static final int FLAG_DELIVER_POST_IME = 1;
        public static final int FLAG_FINISHED = 4;
        public static final int FLAG_FINISHED_HANDLED = 8;
        public static final int FLAG_RESYNTHESIZED = 16;
        public static final int FLAG_UNHANDLED = 32;
        public InputEvent mEvent;
        public int mFlags;
        public QueuedInputEvent mNext;
        public InputEventReceiver mReceiver;

        private QueuedInputEvent() {
        }

        public boolean shouldSkipIme() {
            if ((this.mFlags & 1) != 0) {
                return true;
            }
            if ((this.mEvent instanceof MotionEvent) && this.mEvent.isFromSource(2)) {
                return true;
            }
            return false;
        }

        public boolean shouldSendToSynthesizer() {
            if ((this.mFlags & 32) != 0) {
                return true;
            }
            return false;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("QueuedInputEvent{flags=");
            if (!flagToString("UNHANDLED", 32, flagToString("RESYNTHESIZED", 16, flagToString("FINISHED_HANDLED", 8, flagToString("FINISHED", 4, flagToString("DEFERRED", 2, flagToString("DELIVER_POST_IME", 1, false, sb), sb), sb), sb), sb), sb)) {
                sb.append(SmartFaceManager.PAGE_MIDDLE);
            }
            sb.append(", hasNextQueuedEvent=" + (this.mEvent != null ? SmartFaceManager.TRUE : SmartFaceManager.FALSE));
            sb.append(", hasInputEventReceiver=" + (this.mReceiver != null ? SmartFaceManager.TRUE : SmartFaceManager.FALSE));
            sb.append(", mEvent=" + this.mEvent + "}");
            return sb.toString();
        }

        private boolean flagToString(String name, int flag, boolean hasPrevious, StringBuilder sb) {
            if ((this.mFlags & flag) == 0) {
                return hasPrevious;
            }
            if (hasPrevious) {
                sb.append("|");
            }
            sb.append(name);
            return true;
        }
    }

    static final class RunQueue {
        private final ArrayList<HandlerAction> mActions = new ArrayList();

        private static class HandlerAction {
            Runnable action;
            long delay;

            private HandlerAction() {
            }

            public boolean equals(Object o) {
                if (this == o) {
                    return true;
                }
                if (o == null || getClass() != o.getClass()) {
                    return false;
                }
                HandlerAction that = (HandlerAction) o;
                if (this.action != null) {
                    if (this.action.equals(that.action)) {
                        return true;
                    }
                } else if (that.action == null) {
                    return true;
                }
                return false;
            }

            public int hashCode() {
                return ((this.action != null ? this.action.hashCode() : 0) * 31) + ((int) (this.delay ^ (this.delay >>> 32)));
            }
        }

        RunQueue() {
        }

        void post(Runnable action) {
            postDelayed(action, 0);
        }

        void postDelayed(Runnable action, long delayMillis) {
            HandlerAction handlerAction = new HandlerAction();
            handlerAction.action = action;
            handlerAction.delay = delayMillis;
            synchronized (this.mActions) {
                this.mActions.add(handlerAction);
            }
        }

        void removeCallbacks(Runnable action) {
            HandlerAction handlerAction = new HandlerAction();
            handlerAction.action = action;
            synchronized (this.mActions) {
                do {
                } while (this.mActions.remove(handlerAction));
            }
        }

        void executeActions(Handler handler) {
            synchronized (this.mActions) {
                ArrayList<HandlerAction> actions = this.mActions;
                int count = actions.size();
                for (int i = 0; i < count; i++) {
                    HandlerAction handlerAction = (HandlerAction) actions.get(i);
                    handler.postDelayed(handlerAction.action, handlerAction.delay);
                }
                actions.clear();
            }
        }
    }

    private class SendWindowContentChangedAccessibilityEvent implements Runnable {
        private int mChangeTypes;
        public long mLastEventTimeMillis;
        public View mSource;

        private SendWindowContentChangedAccessibilityEvent() {
            this.mChangeTypes = 0;
        }

        public void run() {
            if (AccessibilityManager.getInstance(ViewRootImpl.this.mContext).isEnabled()) {
                this.mLastEventTimeMillis = SystemClock.uptimeMillis();
                AccessibilityEvent event = AccessibilityEvent.obtain();
                event.setEventType(2048);
                event.setContentChangeTypes(this.mChangeTypes);
                this.mSource.sendAccessibilityEventUnchecked(event);
            } else {
                this.mLastEventTimeMillis = 0;
            }
            this.mSource.resetSubtreeAccessibilityStateChanged();
            this.mSource = null;
            this.mChangeTypes = 0;
        }

        public void runOrPost(View source, int changeType) {
            if (this.mSource != null) {
                View predecessor = ViewRootImpl.this.getCommonPredecessor(this.mSource, source);
                if (predecessor == null) {
                    predecessor = source;
                }
                this.mSource = predecessor;
                this.mChangeTypes |= changeType;
                return;
            }
            this.mSource = source;
            this.mChangeTypes = changeType;
            long timeSinceLastMillis = SystemClock.uptimeMillis() - this.mLastEventTimeMillis;
            long minEventIntevalMillis = ViewConfiguration.getSendRecurringAccessibilityEventsInterval();
            if (timeSinceLastMillis >= minEventIntevalMillis) {
                this.mSource.removeCallbacks(this);
                run();
                return;
            }
            this.mSource.postDelayed(this, minEventIntevalMillis - timeSinceLastMillis);
        }
    }

    final class SmartClipRemoteRequestDispatcherProxy {
        private boolean DEBUG = false;
        private final String TAG = "SmartClipRemoteRequestDispatcher_ViewRootImpl";
        private Context mContext;
        private SmartClipRemoteRequestDispatcher mDispatcher;
        private ViewRootImplGateway mGateway = new ViewRootImplGateway() {
            public void enqueueInputEvent(InputEvent event, InputEventReceiver receiver, int flags, boolean processImmediately) {
                ViewRootImpl.this.enqueueInputEvent(event, receiver, flags, processImmediately);
            }

            public PointF getScaleFactor() {
                return ViewRootImpl.this.mScaleFactor;
            }

            public View getRootView() {
                return ViewRootImpl.this.mView;
            }

            public ViewRootImpl getViewRootImpl() {
                return ViewRootImpl.this;
            }
        };

        public SmartClipRemoteRequestDispatcherProxy(Context context) {
            this.mContext = context;
            this.mDispatcher = new SmartClipRemoteRequestDispatcher(context, this.mGateway);
            this.DEBUG = this.mDispatcher.isDebugMode();
        }

        public void dispatchSmartClipRemoteRequest(final SmartClipRemoteRequestInfo request) {
            if (this.DEBUG) {
                Log.d("SmartClipRemoteRequestDispatcher_ViewRootImpl", "dispatchSmartClipRemoteRequest : req id=" + request.mRequestId + " type=" + request.mRequestType + " pid=" + request.mCallerPid + " uid=" + request.mCallerUid);
            }
            switch (request.mRequestType) {
                case 1:
                    this.mDispatcher.checkPermission(SmartClipRemoteRequestDispatcher.PERMISSION_EXTRACT_SMARTCLIP_DATA, request.mCallerPid, request.mCallerUid);
                    ViewRootImpl.this.mHandler.post(new Runnable() {
                        public void run() {
                            SmartClipRemoteRequestDispatcherProxy.this.dispatchSmartClipMetaDataExtraction(request);
                        }
                    });
                    return;
                default:
                    this.mDispatcher.dispatchSmartClipRemoteRequest(request);
                    return;
            }
        }

        private void dispatchSmartClipMetaDataExtraction(SmartClipRemoteRequestInfo request) {
            SmartClipDataExtractionEvent requestInfo = request.mRequestData;
            requestInfo.mRequestId = request.mRequestId;
            requestInfo.mTargetWindowLayer = request.mTargetWindowLayer;
            Rect winFrame = null;
            if (!(ViewRootImpl.this.mScaleFactor.x == 1.0f && ViewRootImpl.this.mScaleFactor.y == 1.0f)) {
                winFrame = ViewRootImpl.this.getMultiWindowStyle().getBounds();
                if (winFrame == null) {
                    winFrame = new Rect(ViewRootImpl.this.mWinFrame);
                }
                winFrame.offset(-ViewRootImpl.this.mCocktailBar.left, -ViewRootImpl.this.mCocktailBar.top);
                int cropRectWidth = requestInfo.mCropRect.width();
                int cropRectHeight = requestInfo.mCropRect.height();
                requestInfo.mCropRect.left = (int) ((((float) (requestInfo.mCropRect.left - winFrame.left)) * (1.0f / ViewRootImpl.this.mScaleFactor.x)) + 0.5f);
                requestInfo.mCropRect.top = (int) ((((float) (requestInfo.mCropRect.top - winFrame.top)) * (1.0f / ViewRootImpl.this.mScaleFactor.y)) + 0.5f);
                requestInfo.mCropRect.right = requestInfo.mCropRect.left + ((int) ((((float) cropRectWidth) * (1.0f / ViewRootImpl.this.mScaleFactor.x)) + 0.5f));
                requestInfo.mCropRect.bottom = requestInfo.mCropRect.top + ((int) ((((float) cropRectHeight) * (1.0f / ViewRootImpl.this.mScaleFactor.y)) + 0.5f));
            }
            if (ViewRootImpl.this.mView != null) {
                SmartClipDataCropperImpl cropper;
                if (ViewRootImpl.this.mScaleFactor.x == 1.0f && ViewRootImpl.this.mScaleFactor.y == 1.0f) {
                    cropper = new SmartClipDataCropperImpl(ViewRootImpl.this.mView.getContext(), requestInfo);
                } else {
                    int borderWidth = 0;
                    if (this.mContext != null && (ViewRootImpl.this.mWindowAttributes.privateFlags & 16) == 0) {
                        borderWidth = MultiWindowFeatures.isSupportSimplificationUI(this.mContext) ? this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_borderline_thickness) : (this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_floating_mBorderPaintInner) + this.mContext.getResources().getDimensionPixelSize(R.dimen.multiwindow_floating_mBorderPaintOuter)) + 1;
                    }
                    cropper = new SmartClipDataCropperImpl(ViewRootImpl.this.mView.getContext(), requestInfo, winFrame, new RectF(0.0f, 0.0f, ViewRootImpl.this.mScaleFactor.x, ViewRootImpl.this.mScaleFactor.y), borderWidth);
                }
                cropper.doExtractSmartClipData(ViewRootImpl.this.mView);
                return;
            }
            new SmartClipDataCropperImpl(this.mContext, requestInfo).sendExtractionResultToSmartClipService(null);
        }
    }

    final class SyntheticInputStage extends InputStage {
        private final SyntheticJoystickHandler mJoystick = new SyntheticJoystickHandler();
        private final SyntheticKeyboardHandler mKeyboard = new SyntheticKeyboardHandler();
        private final SyntheticTouchNavigationHandler mTouchNavigation = new SyntheticTouchNavigationHandler();
        private final SyntheticTrackballHandler mTrackball = new SyntheticTrackballHandler();

        public SyntheticInputStage() {
            super(null);
        }

        protected int onProcess(QueuedInputEvent q) {
            q.mFlags |= 16;
            if (q.mEvent instanceof MotionEvent) {
                MotionEvent event = q.mEvent;
                int source = event.getSource();
                if ((source & 4) != 0) {
                    this.mTrackball.process(event);
                    return 1;
                } else if ((source & 16) != 0) {
                    this.mJoystick.process(event);
                    return 1;
                } else if ((source & 2097152) == 2097152) {
                    this.mTouchNavigation.process(event);
                    return 1;
                }
            } else if ((q.mFlags & 32) != 0) {
                this.mKeyboard.process((KeyEvent) q.mEvent);
                return 1;
            }
            return 0;
        }

        protected void onDeliverToNext(QueuedInputEvent q) {
            if ((q.mFlags & 16) == 0 && (q.mEvent instanceof MotionEvent)) {
                MotionEvent event = q.mEvent;
                int source = event.getSource();
                if ((source & 4) != 0) {
                    this.mTrackball.cancel(event);
                } else if ((source & 16) != 0) {
                    this.mJoystick.cancel(event);
                } else if ((source & 2097152) == 2097152) {
                    this.mTouchNavigation.cancel(event);
                }
            }
            super.onDeliverToNext(q);
        }
    }

    final class SyntheticJoystickHandler extends Handler {
        private static final int MSG_ENQUEUE_X_AXIS_KEY_REPEAT = 1;
        private static final int MSG_ENQUEUE_Y_AXIS_KEY_REPEAT = 2;
        private static final String TAG = "SyntheticJoystickHandler";
        private int mLastXDirection;
        private int mLastXKeyCode;
        private int mLastYDirection;
        private int mLastYKeyCode;

        public SyntheticJoystickHandler() {
            super(true);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                case 2:
                    KeyEvent oldEvent = msg.obj;
                    KeyEvent e = KeyEvent.changeTimeRepeat(oldEvent, SystemClock.uptimeMillis(), oldEvent.getRepeatCount() + 1);
                    if (ViewRootImpl.this.mAttachInfo.mHasWindowFocus) {
                        ViewRootImpl.this.enqueueInputEvent(e);
                        Message m = obtainMessage(msg.what, e);
                        m.setAsynchronous(true);
                        sendMessageDelayed(m, (long) ViewConfiguration.getKeyRepeatDelay());
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        public void process(MotionEvent event) {
            switch (event.getActionMasked()) {
                case 2:
                    update(event, true);
                    return;
                case 3:
                    cancel(event);
                    return;
                default:
                    Log.w(TAG, "Unexpected action: " + event.getActionMasked());
                    return;
            }
        }

        private void cancel(MotionEvent event) {
            removeMessages(1);
            removeMessages(2);
            update(event, false);
        }

        private void update(MotionEvent event, boolean synthesizeNewKeys) {
            long time = event.getEventTime();
            int metaState = event.getMetaState();
            int deviceId = event.getDeviceId();
            int source = event.getSource();
            int xDirection = joystickAxisValueToDirection(event.getAxisValue(15));
            if (xDirection == 0) {
                xDirection = joystickAxisValueToDirection(event.getX());
            }
            int yDirection = joystickAxisValueToDirection(event.getAxisValue(16));
            if (yDirection == 0) {
                yDirection = joystickAxisValueToDirection(event.getY());
            }
            if (xDirection != this.mLastXDirection) {
                if (this.mLastXKeyCode != 0) {
                    removeMessages(1);
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(time, time, 1, this.mLastXKeyCode, 0, metaState, deviceId, 0, 1024, source));
                    this.mLastXKeyCode = 0;
                }
                this.mLastXDirection = xDirection;
                if (xDirection != 0 && synthesizeNewKeys) {
                    this.mLastXKeyCode = xDirection > 0 ? 22 : 21;
                    KeyEvent e = new KeyEvent(time, time, 0, this.mLastXKeyCode, 0, metaState, deviceId, 0, 1024, source);
                    ViewRootImpl.this.enqueueInputEvent(e);
                    Message m = obtainMessage(1, e);
                    m.setAsynchronous(true);
                    sendMessageDelayed(m, (long) ViewConfiguration.getKeyRepeatTimeout());
                }
            }
            if (yDirection != this.mLastYDirection) {
                if (this.mLastYKeyCode != 0) {
                    removeMessages(2);
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(time, time, 1, this.mLastYKeyCode, 0, metaState, deviceId, 0, 1024, source));
                    this.mLastYKeyCode = 0;
                }
                this.mLastYDirection = yDirection;
                if (yDirection != 0 && synthesizeNewKeys) {
                    this.mLastYKeyCode = yDirection > 0 ? 20 : 19;
                    e = new KeyEvent(time, time, 0, this.mLastYKeyCode, 0, metaState, deviceId, 0, 1024, source);
                    ViewRootImpl.this.enqueueInputEvent(e);
                    m = obtainMessage(2, e);
                    m.setAsynchronous(true);
                    sendMessageDelayed(m, (long) ViewConfiguration.getKeyRepeatTimeout());
                }
            }
        }

        private int joystickAxisValueToDirection(float value) {
            if (value >= 0.5f) {
                return 1;
            }
            if (value <= -0.5f) {
                return -1;
            }
            return 0;
        }
    }

    final class SyntheticKeyboardHandler {
        SyntheticKeyboardHandler() {
        }

        public void process(KeyEvent event) {
            if ((event.getFlags() & 1024) == 0) {
                FallbackAction fallbackAction = event.getKeyCharacterMap().getFallbackAction(event.getKeyCode(), event.getMetaState());
                if (fallbackAction != null) {
                    InputEvent fallbackEvent = KeyEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), fallbackAction.keyCode, event.getRepeatCount(), fallbackAction.metaState, event.getDeviceId(), event.getScanCode(), event.getFlags() | 1024, event.getDisplayId(), event.getSource(), null);
                    fallbackAction.recycle();
                    ViewRootImpl.this.enqueueInputEvent(fallbackEvent);
                }
            }
        }
    }

    final class SyntheticTouchNavigationHandler extends Handler {
        private static final float DEFAULT_HEIGHT_MILLIMETERS = 48.0f;
        private static final float DEFAULT_WIDTH_MILLIMETERS = 48.0f;
        private static final float FLING_TICK_DECAY = 0.8f;
        private static final boolean LOCAL_DEBUG = false;
        private static final String LOCAL_TAG = "SyntheticTouchNavigationHandler";
        private static final float MAX_FLING_VELOCITY_TICKS_PER_SECOND = 20.0f;
        private static final float MIN_FLING_VELOCITY_TICKS_PER_SECOND = 6.0f;
        private static final int TICK_DISTANCE_MILLIMETERS = 12;
        private float mAccumulatedX;
        private float mAccumulatedY;
        private int mActivePointerId = -1;
        private float mConfigMaxFlingVelocity;
        private float mConfigMinFlingVelocity;
        private float mConfigTickDistance;
        private boolean mConsumedMovement;
        private int mCurrentDeviceId = -1;
        private boolean mCurrentDeviceSupported;
        private int mCurrentSource;
        private final Runnable mFlingRunnable = new Runnable() {
            public void run() {
                long time = SystemClock.uptimeMillis();
                SyntheticTouchNavigationHandler.this.sendKeyDownOrRepeat(time, SyntheticTouchNavigationHandler.this.mPendingKeyCode, SyntheticTouchNavigationHandler.this.mPendingKeyMetaState);
                SyntheticTouchNavigationHandler.access$2532(SyntheticTouchNavigationHandler.this, 0.8f);
                if (!SyntheticTouchNavigationHandler.this.postFling(time)) {
                    SyntheticTouchNavigationHandler.this.mFlinging = false;
                    SyntheticTouchNavigationHandler.this.finishKeys(time);
                }
            }
        };
        private float mFlingVelocity;
        private boolean mFlinging;
        private float mLastX;
        private float mLastY;
        private int mPendingKeyCode = 0;
        private long mPendingKeyDownTime;
        private int mPendingKeyMetaState;
        private int mPendingKeyRepeatCount;
        private float mStartX;
        private float mStartY;
        private VelocityTracker mVelocityTracker;

        static /* synthetic */ float access$2532(SyntheticTouchNavigationHandler x0, float x1) {
            float f = x0.mFlingVelocity * x1;
            x0.mFlingVelocity = f;
            return f;
        }

        public SyntheticTouchNavigationHandler() {
            super(true);
        }

        public void process(MotionEvent event) {
            long time = event.getEventTime();
            int deviceId = event.getDeviceId();
            int source = event.getSource();
            if (!(this.mCurrentDeviceId == deviceId && this.mCurrentSource == source)) {
                finishKeys(time);
                finishTracking(time);
                this.mCurrentDeviceId = deviceId;
                this.mCurrentSource = source;
                this.mCurrentDeviceSupported = false;
                InputDevice device = event.getDevice();
                if (device != null) {
                    MotionRange xRange = device.getMotionRange(0);
                    MotionRange yRange = device.getMotionRange(1);
                    if (!(xRange == null || yRange == null)) {
                        this.mCurrentDeviceSupported = true;
                        float xRes = xRange.getResolution();
                        if (xRes <= 0.0f) {
                            xRes = xRange.getRange() / 48.0f;
                        }
                        float yRes = yRange.getResolution();
                        if (yRes <= 0.0f) {
                            yRes = yRange.getRange() / 48.0f;
                        }
                        this.mConfigTickDistance = 12.0f * ((xRes + yRes) * 0.5f);
                        this.mConfigMinFlingVelocity = MIN_FLING_VELOCITY_TICKS_PER_SECOND * this.mConfigTickDistance;
                        this.mConfigMaxFlingVelocity = MAX_FLING_VELOCITY_TICKS_PER_SECOND * this.mConfigTickDistance;
                    }
                }
            }
            if (this.mCurrentDeviceSupported) {
                int action = event.getActionMasked();
                switch (action) {
                    case 0:
                        boolean caughtFling = this.mFlinging;
                        finishKeys(time);
                        finishTracking(time);
                        this.mActivePointerId = event.getPointerId(0);
                        this.mVelocityTracker = VelocityTracker.obtain();
                        this.mVelocityTracker.addMovement(event);
                        this.mStartX = event.getX();
                        this.mStartY = event.getY();
                        this.mLastX = this.mStartX;
                        this.mLastY = this.mStartY;
                        this.mAccumulatedX = 0.0f;
                        this.mAccumulatedY = 0.0f;
                        this.mConsumedMovement = caughtFling;
                        return;
                    case 1:
                    case 2:
                        if (this.mActivePointerId >= 0) {
                            int index = event.findPointerIndex(this.mActivePointerId);
                            if (index < 0) {
                                finishKeys(time);
                                finishTracking(time);
                                return;
                            }
                            this.mVelocityTracker.addMovement(event);
                            float x = event.getX(index);
                            float y = event.getY(index);
                            this.mAccumulatedX += x - this.mLastX;
                            this.mAccumulatedY += y - this.mLastY;
                            this.mLastX = x;
                            this.mLastY = y;
                            consumeAccumulatedMovement(time, event.getMetaState());
                            if (action == 1) {
                                if (this.mConsumedMovement && this.mPendingKeyCode != 0) {
                                    this.mVelocityTracker.computeCurrentVelocity(1000, this.mConfigMaxFlingVelocity);
                                    if (!startFling(time, this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mVelocityTracker.getYVelocity(this.mActivePointerId))) {
                                        finishKeys(time);
                                    }
                                }
                                finishTracking(time);
                                return;
                            }
                            return;
                        }
                        return;
                    case 3:
                        finishKeys(time);
                        finishTracking(time);
                        return;
                    default:
                        return;
                }
            }
        }

        public void cancel(MotionEvent event) {
            if (this.mCurrentDeviceId == event.getDeviceId() && this.mCurrentSource == event.getSource()) {
                long time = event.getEventTime();
                finishKeys(time);
                finishTracking(time);
            }
        }

        private void finishKeys(long time) {
            cancelFling();
            sendKeyUp(time);
        }

        private void finishTracking(long time) {
            if (this.mActivePointerId >= 0) {
                this.mActivePointerId = -1;
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
            }
        }

        private void consumeAccumulatedMovement(long time, int metaState) {
            float absX = Math.abs(this.mAccumulatedX);
            float absY = Math.abs(this.mAccumulatedY);
            if (absX >= absY) {
                if (absX >= this.mConfigTickDistance) {
                    this.mAccumulatedX = consumeAccumulatedMovement(time, metaState, this.mAccumulatedX, 21, 22);
                    this.mAccumulatedY = 0.0f;
                    this.mConsumedMovement = true;
                }
            } else if (absY >= this.mConfigTickDistance) {
                this.mAccumulatedY = consumeAccumulatedMovement(time, metaState, this.mAccumulatedY, 19, 20);
                this.mAccumulatedX = 0.0f;
                this.mConsumedMovement = true;
            }
        }

        private float consumeAccumulatedMovement(long time, int metaState, float accumulator, int negativeKeyCode, int positiveKeyCode) {
            while (accumulator <= (-this.mConfigTickDistance)) {
                sendKeyDownOrRepeat(time, negativeKeyCode, metaState);
                accumulator += this.mConfigTickDistance;
            }
            while (accumulator >= this.mConfigTickDistance) {
                sendKeyDownOrRepeat(time, positiveKeyCode, metaState);
                accumulator -= this.mConfigTickDistance;
            }
            return accumulator;
        }

        private void sendKeyDownOrRepeat(long time, int keyCode, int metaState) {
            if (this.mPendingKeyCode != keyCode) {
                sendKeyUp(time);
                this.mPendingKeyDownTime = time;
                this.mPendingKeyCode = keyCode;
                this.mPendingKeyRepeatCount = 0;
            } else {
                this.mPendingKeyRepeatCount++;
            }
            this.mPendingKeyMetaState = metaState;
            ViewRootImpl.this.enqueueInputEvent(new KeyEvent(this.mPendingKeyDownTime, time, 0, this.mPendingKeyCode, this.mPendingKeyRepeatCount, this.mPendingKeyMetaState, this.mCurrentDeviceId, 1024, this.mCurrentSource));
        }

        private void sendKeyUp(long time) {
            if (this.mPendingKeyCode != 0) {
                ViewRootImpl.this.enqueueInputEvent(new KeyEvent(this.mPendingKeyDownTime, time, 1, this.mPendingKeyCode, 0, this.mPendingKeyMetaState, this.mCurrentDeviceId, 0, 1024, this.mCurrentSource));
                this.mPendingKeyCode = 0;
            }
        }

        private boolean startFling(long time, float vx, float vy) {
            switch (this.mPendingKeyCode) {
                case 19:
                    if ((-vy) >= this.mConfigMinFlingVelocity && Math.abs(vx) < this.mConfigMinFlingVelocity) {
                        this.mFlingVelocity = -vy;
                        break;
                    }
                    return false;
                case 20:
                    if (vy >= this.mConfigMinFlingVelocity && Math.abs(vx) < this.mConfigMinFlingVelocity) {
                        this.mFlingVelocity = vy;
                        break;
                    }
                    return false;
                    break;
                case 21:
                    if ((-vx) >= this.mConfigMinFlingVelocity && Math.abs(vy) < this.mConfigMinFlingVelocity) {
                        this.mFlingVelocity = -vx;
                        break;
                    }
                    return false;
                case 22:
                    if (vx >= this.mConfigMinFlingVelocity && Math.abs(vy) < this.mConfigMinFlingVelocity) {
                        this.mFlingVelocity = vx;
                        break;
                    }
                    return false;
            }
            this.mFlinging = postFling(time);
            return this.mFlinging;
        }

        private boolean postFling(long time) {
            if (this.mFlingVelocity < this.mConfigMinFlingVelocity) {
                return false;
            }
            postAtTime(this.mFlingRunnable, time + ((long) ((this.mConfigTickDistance / this.mFlingVelocity) * 1000.0f)));
            return true;
        }

        private void cancelFling() {
            if (this.mFlinging) {
                removeCallbacks(this.mFlingRunnable);
                this.mFlinging = false;
            }
        }
    }

    final class SyntheticTrackballHandler {
        private long mLastTime;
        private final TrackballAxis mX = new TrackballAxis();
        private final TrackballAxis mY = new TrackballAxis();

        SyntheticTrackballHandler() {
        }

        public void process(MotionEvent event) {
            long curTime = SystemClock.uptimeMillis();
            if (this.mLastTime + 250 < curTime) {
                this.mX.reset(0);
                this.mY.reset(0);
                this.mLastTime = curTime;
            }
            int action = event.getAction();
            int metaState = event.getMetaState();
            switch (action) {
                case 0:
                    this.mX.reset(2);
                    this.mY.reset(2);
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(curTime, curTime, 0, 23, 0, metaState, -1, 0, 1024, 257));
                    break;
                case 1:
                    this.mX.reset(2);
                    this.mY.reset(2);
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(curTime, curTime, 1, 23, 0, metaState, -1, 0, 1024, 257));
                    break;
            }
            float xOff = this.mX.collect(event.getX(), event.getEventTime(), "X");
            float yOff = this.mY.collect(event.getY(), event.getEventTime(), "Y");
            int keycode = 0;
            int movement = 0;
            float accel = 1.0f;
            if (xOff > yOff) {
                movement = this.mX.generate();
                if (movement != 0) {
                    keycode = movement > 0 ? 22 : 21;
                    accel = this.mX.acceleration;
                    this.mY.reset(2);
                }
            } else if (yOff > 0.0f) {
                movement = this.mY.generate();
                if (movement != 0) {
                    keycode = movement > 0 ? 20 : 19;
                    accel = this.mY.acceleration;
                    this.mX.reset(2);
                }
            }
            if (keycode != 0) {
                if (movement < 0) {
                    movement = -movement;
                }
                int accelMovement = (int) (((float) movement) * accel);
                if (accelMovement > movement) {
                    movement--;
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(curTime, curTime, 2, keycode, accelMovement - movement, metaState, -1, 0, 1024, 257));
                }
                while (movement > 0) {
                    movement--;
                    curTime = SystemClock.uptimeMillis();
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(curTime, curTime, 0, keycode, 0, metaState, -1, 0, 1024, 257));
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(curTime, curTime, 1, keycode, 0, metaState, -1, 0, 1024, 257));
                }
                this.mLastTime = curTime;
            }
        }

        public void cancel(MotionEvent event) {
            this.mLastTime = -2147483648L;
            if (ViewRootImpl.this.mView != null && ViewRootImpl.this.mAdded) {
                ViewRootImpl.this.ensureTouchMode(false);
            }
        }
    }

    static final class SystemUiVisibilityInfo {
        int globalVisibility;
        int localChanges;
        int localValue;
        int seq;

        SystemUiVisibilityInfo() {
        }
    }

    class TakenSurfaceHolder extends BaseSurfaceHolder {
        TakenSurfaceHolder() {
        }

        public boolean onAllowLockCanvas() {
            return ViewRootImpl.this.mDrawingAllowed;
        }

        public void onRelayoutContainer() {
        }

        public void setFormat(int format) {
            ((RootViewSurfaceTaker) ViewRootImpl.this.mView).setSurfaceFormat(format);
        }

        public void setType(int type) {
            ((RootViewSurfaceTaker) ViewRootImpl.this.mView).setSurfaceType(type);
        }

        public void onUpdateSurface() {
            throw new IllegalStateException("Shouldn't be here");
        }

        public boolean isCreating() {
            return ViewRootImpl.this.mIsCreating;
        }

        public void setFixedSize(int width, int height) {
            throw new UnsupportedOperationException("Currently only support sizing from layout");
        }

        public void setKeepScreenOn(boolean screenOn) {
            ((RootViewSurfaceTaker) ViewRootImpl.this.mView).setSurfaceKeepScreenOn(screenOn);
        }
    }

    static final class TrackballAxis {
        static final float ACCEL_MOVE_SCALING_FACTOR = 0.025f;
        static final long FAST_MOVE_TIME = 150;
        static final float FIRST_MOVEMENT_THRESHOLD = 0.5f;
        static final float MAX_ACCELERATION = 20.0f;
        static final float SECOND_CUMULATIVE_MOVEMENT_THRESHOLD = 2.0f;
        static final float SUBSEQUENT_INCREMENTAL_MOVEMENT_THRESHOLD = 1.0f;
        float acceleration = 1.0f;
        int dir;
        long lastMoveTime = 0;
        int nonAccelMovement;
        float position;
        int step;

        TrackballAxis() {
        }

        void reset(int _step) {
            this.position = 0.0f;
            this.acceleration = 1.0f;
            this.lastMoveTime = 0;
            this.step = _step;
            this.dir = 0;
        }

        float collect(float off, long time, String axis) {
            long normTime;
            if (off > 0.0f) {
                normTime = (long) (150.0f * off);
                if (this.dir < 0) {
                    this.position = 0.0f;
                    this.step = 0;
                    this.acceleration = 1.0f;
                    this.lastMoveTime = 0;
                }
                this.dir = 1;
            } else if (off < 0.0f) {
                normTime = (long) ((-off) * 150.0f);
                if (this.dir > 0) {
                    this.position = 0.0f;
                    this.step = 0;
                    this.acceleration = 1.0f;
                    this.lastMoveTime = 0;
                }
                this.dir = -1;
            } else {
                normTime = 0;
            }
            if (normTime > 0) {
                long delta = time - this.lastMoveTime;
                this.lastMoveTime = time;
                float acc = this.acceleration;
                float scale;
                if (delta < normTime) {
                    scale = ((float) (normTime - delta)) * ACCEL_MOVE_SCALING_FACTOR;
                    if (scale > 1.0f) {
                        acc *= scale;
                    }
                    if (acc >= MAX_ACCELERATION) {
                        acc = MAX_ACCELERATION;
                    }
                    this.acceleration = acc;
                } else {
                    scale = ((float) (delta - normTime)) * ACCEL_MOVE_SCALING_FACTOR;
                    if (scale > 1.0f) {
                        acc /= scale;
                    }
                    if (acc <= 1.0f) {
                        acc = 1.0f;
                    }
                    this.acceleration = acc;
                }
            }
            this.position += off;
            return Math.abs(this.position);
        }

        int generate() {
            int movement = 0;
            this.nonAccelMovement = 0;
            while (true) {
                int dir = this.position >= 0.0f ? 1 : -1;
                switch (this.step) {
                    case 0:
                        if (Math.abs(this.position) < FIRST_MOVEMENT_THRESHOLD) {
                            break;
                        }
                        movement += dir;
                        this.nonAccelMovement += dir;
                        this.step = 1;
                        continue;
                    case 1:
                        if (Math.abs(this.position) < SECOND_CUMULATIVE_MOVEMENT_THRESHOLD) {
                            break;
                        }
                        movement += dir;
                        this.nonAccelMovement += dir;
                        this.position -= ((float) dir) * SECOND_CUMULATIVE_MOVEMENT_THRESHOLD;
                        this.step = 2;
                        continue;
                    default:
                        if (Math.abs(this.position) < 1.0f) {
                            break;
                        }
                        movement += dir;
                        this.position -= ((float) dir) * 1.0f;
                        float acc = this.acceleration * 1.1f;
                        if (acc >= MAX_ACCELERATION) {
                            acc = this.acceleration;
                        }
                        this.acceleration = acc;
                        continue;
                }
                return movement;
            }
        }
    }

    final class TraversalRunnable implements Runnable {
        TraversalRunnable() {
        }

        public void run() {
            ViewRootImpl.this.doTraversal();
        }
    }

    final class ViewPostImeInputStage extends InputStage {
        public ViewPostImeInputStage(InputStage next) {
            super(next);
        }

        protected int onProcess(QueuedInputEvent q) {
            if (q.mEvent instanceof KeyEvent) {
                return processKeyEvent(q);
            }
            ViewRootImpl.this.handleDispatchWindowAnimationStopped();
            int source = q.mEvent.getSource();
            if ((source & 2) != 0) {
                return processPointerEvent(q);
            }
            if ((source & 4) != 0) {
                return processTrackballEvent(q);
            }
            return processGenericMotionEvent(q);
        }

        protected void onDeliverToNext(QueuedInputEvent q) {
            if (ViewRootImpl.this.mUnbufferedInputDispatch && (q.mEvent instanceof MotionEvent) && ((MotionEvent) q.mEvent).isTouchEvent() && ViewRootImpl.isTerminalInputEvent(q.mEvent)) {
                ViewRootImpl.this.mUnbufferedInputDispatch = false;
                ViewRootImpl.this.scheduleConsumeBatchedInput();
            }
            super.onDeliverToNext(q);
        }

        private int processKeyEvent(QueuedInputEvent q) {
            KeyEvent event = q.mEvent;
            if (event.getAction() != 1) {
                ViewRootImpl.this.handleDispatchWindowAnimationStopped();
            }
            Log.d(ViewRootImpl.TAG, "ViewPostImeInputStage processKey " + event.getAction());
            if (ViewRootImpl.this.mView.dispatchKeyEvent(event)) {
                return 1;
            }
            if (shouldDropInputEvent(q)) {
                return 2;
            }
            if (event.getAction() == 0 && event.isCtrlPressed() && event.getRepeatCount() == 0 && !KeyEvent.isModifierKey(event.getKeyCode())) {
                if (ViewRootImpl.this.mView.dispatchKeyShortcutEvent(event)) {
                    return 1;
                }
                if (shouldDropInputEvent(q)) {
                    return 2;
                }
            }
            if (ViewRootImpl.this.mFallbackEventHandler.dispatchKeyEvent(event)) {
                return 1;
            }
            if (shouldDropInputEvent(q)) {
                return 2;
            }
            if (event.getAction() == 0) {
                int direction = 0;
                switch (event.getKeyCode()) {
                    case 19:
                        if (event.hasNoModifiers()) {
                            direction = 33;
                            break;
                        }
                        break;
                    case 20:
                        if (event.hasNoModifiers()) {
                            direction = 130;
                            break;
                        }
                        break;
                    case 21:
                        if (event.hasNoModifiers()) {
                            direction = 17;
                            break;
                        }
                        break;
                    case 22:
                        if (event.hasNoModifiers()) {
                            direction = 66;
                            break;
                        }
                        break;
                    case 61:
                        if (!event.hasNoModifiers()) {
                            if (event.hasModifiers(1)) {
                                direction = 1;
                                break;
                            }
                        }
                        direction = 2;
                        break;
                        break;
                }
                if (direction != 0) {
                    View focused = ViewRootImpl.this.mView.findFocus();
                    View v;
                    if (focused != null) {
                        v = focused.focusSearch(direction);
                        if (!(v == null || v == focused)) {
                            focused.getFocusedRect(ViewRootImpl.this.mTempRect);
                            if (ViewRootImpl.this.mView instanceof ViewGroup) {
                                ((ViewGroup) ViewRootImpl.this.mView).offsetDescendantRectToMyCoords(focused, ViewRootImpl.this.mTempRect);
                                ((ViewGroup) ViewRootImpl.this.mView).offsetRectIntoDescendantCoords(v, ViewRootImpl.this.mTempRect);
                            }
                            if (v.requestFocus(direction, ViewRootImpl.this.mTempRect)) {
                                ViewRootImpl.this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
                                return 1;
                            }
                        }
                        if (ViewRootImpl.this.mView.dispatchUnhandledMove(focused, direction)) {
                            return 1;
                        }
                    }
                    v = ViewRootImpl.this.focusSearch(null, direction);
                    if (v != null && v.requestFocus(direction)) {
                        return 1;
                    }
                }
            }
            return 0;
        }

        private int processPointerEvent(QueuedInputEvent q) {
            MotionEvent event = q.mEvent;
            int action = event.getAction();
            if (TwToolBoxService.TOOLBOX_SUPPORT) {
                if (action == 0 && !ViewRootImpl.this.mUseFloatingToolBox) {
                    ViewRootImpl.this.twUpdateToolBox();
                }
                if (ViewRootImpl.this.mUseFloatingToolBox && ViewRootImpl.this.twProcessTwToolBox(event, action)) {
                    return 1;
                }
            }
            if (action == 0 || action == 1) {
                Log.d(ViewRootImpl.TAG, "ViewPostImeInputStage processPointer " + action);
            }
            if (ViewRootImpl.this.mMotionEventMonitor != null) {
                ViewRootImpl.this.mMotionEventMonitor.dispatchInputEvent(q.mEvent);
            }
            ViewRootImpl.this.mAttachInfo.mUnbufferedDispatchRequested = false;
            boolean handled = ViewRootImpl.this.mView.dispatchPointerEvent(event);
            if (ViewRootImpl.this.mAttachInfo.mUnbufferedDispatchRequested && !ViewRootImpl.this.mUnbufferedInputDispatch) {
                ViewRootImpl.this.mUnbufferedInputDispatch = true;
                if (ViewRootImpl.this.mConsumeBatchedInputScheduled) {
                    ViewRootImpl.this.scheduleConsumeBatchedInputImmediately();
                }
            }
            if (handled) {
                return 1;
            }
            return 0;
        }

        private int processTrackballEvent(QueuedInputEvent q) {
            if (ViewRootImpl.this.mView.dispatchTrackballEvent(q.mEvent)) {
                return 1;
            }
            return 0;
        }

        private int processGenericMotionEvent(QueuedInputEvent q) {
            if (ViewRootImpl.this.mView.dispatchGenericMotionEvent(q.mEvent)) {
                return 1;
            }
            return 0;
        }

        private boolean processWritingBuddyKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == 4 && ((event.getAction() == 0 || event.getAction() == 1) && ViewRootImpl.this.getCurrentWritingBuddyView() != null)) {
                View v = ViewRootImpl.this.getCurrentWritingBuddyView();
                if (v.getWritingBuddy(false) != null) {
                    if (v.getWritingBuddy(false).getImageModePenDrawing()) {
                        if (event.getAction() != 1) {
                            return true;
                        }
                        v.getWritingBuddy(false).showPopup();
                        return true;
                    } else if (event.getAction() != 1) {
                        return true;
                    } else {
                        v.getWritingBuddy(false).finish(false);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    final class ViewPreImeInputStage extends InputStage {
        private boolean mNeedBackKey = false;

        public ViewPreImeInputStage(InputStage next) {
            super(next);
        }

        protected int onProcess(QueuedInputEvent q) {
            if (q.mEvent instanceof KeyEvent) {
                return processKeyEvent(q);
            }
            return 0;
        }

        private int processKeyEvent(QueuedInputEvent q) {
            KeyEvent event = q.mEvent;
            if (ViewRootImpl.sIsNovelModel && !ViewRootImpl.bFactoryBinary && event.getKeyCode() == 67) {
                InputMethodManagerWrapper immWrapper = InputMethodManagerWrapper.getInstance();
                int action = event.getAction();
                if (action == 0 && event.getRepeatCount() == 0) {
                    InputConnection inputConnection = immWrapper == null ? null : immWrapper.getServedInputConnection();
                    if (inputConnection != null) {
                        CharSequence textBeforeCursor = inputConnection.getTextBeforeCursor(1, 0);
                        CharSequence textAfterCursor = inputConnection.getTextAfterCursor(1, 0);
                        if (immWrapper.getServedView() != ViewRootImpl.this.mView.findFocus()) {
                            this.mNeedBackKey = true;
                        } else if (textBeforeCursor.length() == 0 && textAfterCursor.length() == 0) {
                            boolean z;
                            if (inputConnection.getSelectedText(0) == null) {
                                z = true;
                            } else {
                                z = false;
                            }
                            this.mNeedBackKey = z;
                        } else {
                            this.mNeedBackKey = false;
                        }
                    } else {
                        this.mNeedBackKey = true;
                    }
                }
                if (this.mNeedBackKey) {
                    if (action != 1) {
                        return 1;
                    }
                    InputMethodManager imm = InputMethodManager.peekInstance();
                    if (imm == null || !imm.hideSoftInputFromWindow(ViewRootImpl.this.mView.getWindowToken(), 0)) {
                        injectBackKeyEvent();
                    }
                    this.mNeedBackKey = false;
                    return 1;
                }
            }
            if (ViewRootImpl.this.mView.dispatchKeyEventTextMultiSelection(event) || ViewRootImpl.this.mView.dispatchKeyEventPreIme(event)) {
                return 1;
            }
            return 0;
        }

        private void injectBackKeyEvent() {
            InputManager inputManager = InputManager.getInstance();
            if (inputManager != null) {
                long downTime = SystemClock.uptimeMillis();
                KeyEvent down = KeyEvent.obtain(downTime, downTime, 0, 4, 0, 0, -1, 0, 8, 257, null);
                inputManager.injectInputEvent(down, 0);
                down.recycle();
                KeyEvent up = KeyEvent.obtain(downTime, SystemClock.uptimeMillis(), 1, 4, 0, 0, -1, 0, 8, 257, null);
                inputManager.injectInputEvent(up, 0);
                up.recycle();
            }
        }
    }

    final class ViewRootHandler extends Handler {
        ViewRootHandler() {
        }

        public String getMessageName(Message message) {
            switch (message.what) {
                case 1:
                    return "MSG_INVALIDATE";
                case 2:
                    return "MSG_INVALIDATE_RECT";
                case 3:
                    return "MSG_DIE";
                case 4:
                    return "MSG_RESIZED";
                case 5:
                    return "MSG_RESIZED_REPORT";
                case 6:
                    return "MSG_WINDOW_FOCUS_CHANGED";
                case 7:
                    return "MSG_DISPATCH_INPUT_EVENT";
                case 8:
                    return "MSG_DISPATCH_APP_VISIBILITY";
                case 9:
                    return "MSG_DISPATCH_GET_NEW_SURFACE";
                case 11:
                    return "MSG_DISPATCH_KEY_FROM_IME";
                case 12:
                    return "MSG_FINISH_INPUT_CONNECTION";
                case 13:
                    return "MSG_CHECK_FOCUS";
                case 14:
                    return "MSG_CLOSE_SYSTEM_DIALOGS";
                case 15:
                    return "MSG_DISPATCH_DRAG_EVENT";
                case 16:
                    return "MSG_DISPATCH_DRAG_LOCATION_EVENT";
                case 17:
                    return "MSG_DISPATCH_SYSTEM_UI_VISIBILITY";
                case 18:
                    return "MSG_UPDATE_CONFIGURATION";
                case 19:
                    return "MSG_PROCESS_INPUT_EVENTS";
                case 21:
                    return "MSG_CLEAR_ACCESSIBILITY_FOCUS_HOST";
                case 23:
                    return "MSG_WINDOW_MOVED";
                case 24:
                    return "MSG_SYNTHESIZE_INPUT_EVENT";
                case 25:
                    return "MSG_DISPATCH_WINDOW_SHOWN";
                case 26:
                    return "MSG_DISPATCH_WINDOW_ANIMATION_STOPPED";
                case 27:
                    return "MSG_DISPATCH_WINDOW_ANIMATION_STARTED";
                case 29:
                    return "MSG_DISPATCH_COVER_STATE";
                case 30:
                    return "MSG_DISPATCH_SURFACE_DESTROY_DEFERRED";
                case 31:
                    return "MSG_DISPATCH_MULTI_WINDOW_STATE_CHANGED";
                case 1000:
                    return "MSG_ATTACHED_DISPLAY_CHANGED";
                default:
                    return super.getMessageName(message);
            }
        }

        public void handleMessage(Message msg) {
            SomeArgs args;
            Configuration config;
            InputMethodManager imm;
            switch (msg.what) {
                case 1:
                    ((View) msg.obj).invalidate();
                    return;
                case 2:
                    InvalidateInfo info = msg.obj;
                    info.target.invalidate(info.left, info.top, info.right, info.bottom);
                    info.recycle();
                    return;
                case 3:
                    ViewRootImpl.this.doDie();
                    return;
                case 4:
                    args = msg.obj;
                    if (ViewRootImpl.this.mWinFrame.equals(args.arg1) && ViewRootImpl.this.mPendingOverscanInsets.equals(args.arg5) && ViewRootImpl.this.mPendingContentInsets.equals(args.arg2) && ViewRootImpl.this.mPendingStableInsets.equals(args.arg6) && ViewRootImpl.this.mPendingVisibleInsets.equals(args.arg3) && ViewRootImpl.this.mPendingOutsets.equals(args.arg7) && args.arg4 == null) {
                        if (ViewRootImpl.SAFE_DEBUG) {
                            Log.d(ViewRootImpl.TAG, getMessageName(msg) + ": frame & inset does not changed");
                        }
                        if (ViewRootImpl.this.mContext != null) {
                            MultiWindowStyle multiWindowStyle = ViewRootImpl.this.getMultiWindowStyle();
                            if (ViewRootImpl.this.mLastMeasuredMultiWindowStyle == null || multiWindowStyle.getType() == ViewRootImpl.this.mLastMeasuredMultiWindowStyle.getType()) {
                                return;
                            }
                        }
                        return;
                    }
                case 5:
                    break;
                case 6:
                    if (ViewRootImpl.this.mAdded) {
                        boolean hasWindowFocus = msg.arg1 != 0;
                        if (hasWindowFocus != ViewRootImpl.this.mAttachInfo.mHasWindowFocus) {
                        }
                        ViewRootImpl.this.mAttachInfo.mHasWindowFocus = hasWindowFocus;
                        if (ViewRootImpl.SAFE_DEBUG) {
                            Log.d(ViewRootImpl.TAG, "MSG_WINDOW_FOCUS_CHANGED " + msg.arg1);
                        }
                        ViewRootImpl.this.profileRendering(hasWindowFocus);
                        if (hasWindowFocus) {
                            ViewRootImpl.this.ensureTouchModeLocally(msg.arg2 != 0);
                            if (TwToolBoxService.TOOLBOX_SUPPORT) {
                                ViewRootImpl.this.twUpdateToolBox();
                            }
                            if (ViewRootImpl.this.mAttachInfo.mHardwareRenderer != null && ViewRootImpl.this.mSurface.isValid()) {
                                ViewRootImpl.this.mFullRedrawNeeded = true;
                                try {
                                    LayoutParams lp = ViewRootImpl.this.mWindowAttributes;
                                    ViewRootImpl.this.mAttachInfo.mHardwareRenderer.initializeIfNeeded(ViewRootImpl.this.mWidth, ViewRootImpl.this.mHeight, ViewRootImpl.this.mAttachInfo, ViewRootImpl.this.mSurface, lp != null ? lp.surfaceInsets : null);
                                } catch (Throwable e) {
                                    Log.e(ViewRootImpl.TAG, "OutOfResourcesException locking surface", e);
                                    try {
                                        if (!ViewRootImpl.this.mWindowSession.outOfMemory(ViewRootImpl.this.mWindow)) {
                                            Slog.w(ViewRootImpl.TAG, "No processes killed for memory; killing self");
                                            Debug.saveDumpstate("-k -t -z -d -o /data/log/dumpstate_surfaceoom");
                                            Process.killProcess(Process.myPid());
                                        }
                                    } catch (RemoteException e2) {
                                    }
                                    sendMessageDelayed(obtainMessage(msg.what, msg.arg1, msg.arg2), 500);
                                    return;
                                }
                            }
                        }
                        if (TwToolBoxService.TOOLBOX_SUPPORT && ViewRootImpl.this.mUseFloatingToolBox && ViewRootImpl.this.mContext != null) {
                            int option = hasWindowFocus ? 8 : 16;
                            if (ViewRootImpl.this.mToolBoxManager == null) {
                                ViewRootImpl.this.mToolBoxManager = new TwToolBoxManager(ViewRootImpl.this.mContext);
                            }
                            ViewRootImpl.this.mToolBoxManager.sendMessage(ViewRootImpl.this.mContext.getPackageName(), 2, option);
                        }
                        ViewRootImpl.this.mLastWasImTarget = LayoutParams.mayUseInputMethod(ViewRootImpl.this.mWindowAttributes.flags);
                        imm = InputMethodManager.peekInstance();
                        if (!(imm == null || !ViewRootImpl.this.mLastWasImTarget || ViewRootImpl.this.isInLocalFocusMode())) {
                            imm.onPreWindowFocus(ViewRootImpl.this.mView, hasWindowFocus);
                        }
                        if (ViewRootImpl.this.mView != null) {
                            ViewRootImpl.this.mAttachInfo.mKeyDispatchState.reset();
                            ViewRootImpl.this.mView.dispatchWindowFocusChanged(hasWindowFocus);
                            ViewRootImpl.this.mAttachInfo.mTreeObserver.dispatchOnWindowFocusChange(hasWindowFocus);
                        }
                        if (hasWindowFocus) {
                            if (!(imm == null || !ViewRootImpl.this.mLastWasImTarget || ViewRootImpl.this.isInLocalFocusMode())) {
                                int mAdjustSoftInputMode = ViewRootImpl.this.mWindowAttributes.softInputMode;
                                if (((Boolean) msg.obj).booleanValue() && ViewRootImpl.this.mAppVisible && (mAdjustSoftInputMode & 6) != 0) {
                                    mAdjustSoftInputMode |= 256;
                                }
                                imm.onPostWindowFocus(ViewRootImpl.this.mView, ViewRootImpl.this.mView.findFocus(), mAdjustSoftInputMode, !ViewRootImpl.this.mHasHadWindowFocus, ViewRootImpl.this.mWindowAttributes.flags);
                            }
                            LayoutParams layoutParams = ViewRootImpl.this.mWindowAttributes;
                            layoutParams.softInputMode &= -257;
                            if (ViewRootImpl.this.mView != null) {
                                layoutParams = (LayoutParams) ViewRootImpl.this.mView.getLayoutParams();
                                layoutParams.softInputMode &= -257;
                            }
                            ViewRootImpl.this.mHasHadWindowFocus = true;
                        }
                        if (ViewRootImpl.this.mView != null && ViewRootImpl.this.mAccessibilityManager.isEnabled() && hasWindowFocus) {
                            ViewRootImpl.this.mView.sendAccessibilityEvent(32);
                        }
                    }
                    ViewRootImpl.this.sendUserActionEvent();
                    return;
                case 7:
                    args = (SomeArgs) msg.obj;
                    ViewRootImpl.this.enqueueInputEvent(args.arg1, args.arg2, 0, true);
                    args.recycle();
                    return;
                case 8:
                    ViewRootImpl.this.handleAppVisibility(msg.arg1 != 0);
                    return;
                case 9:
                    ViewRootImpl.this.handleGetNewSurface();
                    return;
                case 11:
                    InputEvent event = msg.obj;
                    if ((event.getFlags() & 8) != 0) {
                        event = KeyEvent.changeFlags(event, event.getFlags() & -9);
                    }
                    ViewRootImpl.this.enqueueInputEvent(event, null, 1, true);
                    return;
                case 12:
                    imm = InputMethodManager.peekInstance();
                    if (imm != null) {
                        imm.reportFinishInputConnection((InputConnection) msg.obj);
                        return;
                    }
                    return;
                case 13:
                    imm = InputMethodManager.peekInstance();
                    if (imm != null) {
                        imm.checkFocus();
                    }
                    ViewRootImpl.this.sendUserActionEvent();
                    return;
                case 14:
                    if (ViewRootImpl.this.mView != null) {
                        ViewRootImpl.this.mView.onCloseSystemDialogs((String) msg.obj);
                        return;
                    }
                    return;
                case 15:
                case 16:
                    DragEvent event2 = msg.obj;
                    event2.mLocalState = ViewRootImpl.this.mLocalDragState;
                    ViewRootImpl.this.handleDragEvent(event2);
                    return;
                case 17:
                    ViewRootImpl.this.handleDispatchSystemUiVisibilityChanged((SystemUiVisibilityInfo) msg.obj);
                    ViewRootImpl.this.sendUserActionEvent();
                    return;
                case 18:
                    config = (Configuration) msg.obj;
                    if (config.isOtherSeqNewer(ViewRootImpl.this.mLastConfiguration)) {
                        config = ViewRootImpl.this.mLastConfiguration;
                    }
                    ViewRootImpl.this.updateConfiguration(config, false);
                    return;
                case 19:
                    ViewRootImpl.this.mProcessInputEventsScheduled = false;
                    ViewRootImpl.this.doProcessInputEvents();
                    return;
                case 21:
                    ViewRootImpl.this.setAccessibilityFocus(null, null);
                    return;
                case 22:
                    if (ViewRootImpl.this.mView != null) {
                        ViewRootImpl.this.invalidateWorld(ViewRootImpl.this.mView);
                        return;
                    }
                    return;
                case 23:
                    if (ViewRootImpl.this.mAdded) {
                        int w = ViewRootImpl.this.mWinFrame.width();
                        int h = ViewRootImpl.this.mWinFrame.height();
                        int l = msg.arg1;
                        int t = msg.arg2;
                        ViewRootImpl.this.mWinFrame.left = l;
                        ViewRootImpl.this.mWinFrame.right = l + w;
                        ViewRootImpl.this.mWinFrame.top = t;
                        ViewRootImpl.this.mWinFrame.bottom = t + h;
                        if (ViewRootImpl.this.mView != null) {
                            ViewRootImpl.forceLayout(ViewRootImpl.this.mView);
                        }
                        ViewRootImpl.this.requestLayout();
                        return;
                    }
                    return;
                case 24:
                    ViewRootImpl.this.enqueueInputEvent((InputEvent) msg.obj, null, 32, true);
                    return;
                case 25:
                    ViewRootImpl.this.handleDispatchWindowShown();
                    return;
                case 26:
                    ViewRootImpl.this.handleDispatchWindowAnimationStopped();
                    return;
                case 27:
                    ViewRootImpl.this.handleDispatchWindowAnimationStarted(msg.arg1);
                    return;
                case 29:
                    ViewRootImpl.this.handleDispatchCoverStateChanged(msg.arg1 == 1);
                    return;
                case 30:
                    ViewRootImpl.this.handleDispatchSurfaceDestroyDeferred();
                    return;
                case 31:
                    ViewRootImpl.this.handleDispatchMultiWindowStateChanged(msg.arg1);
                    return;
                case 1000:
                    int displayId = msg.arg1;
                    return;
                default:
                    return;
            }
            if (ViewRootImpl.this.mAdded) {
                boolean needResizeAnimation = false;
                args = (SomeArgs) msg.obj;
                config = args.arg4;
                Log.d(ViewRootImpl.TAG, getMessageName(msg) + ": ci=" + args.arg2 + " vi=" + args.arg3 + " or=" + (config == null ? ViewRootImpl.this.mView.getResources().getConfiguration().orientation : config.orientation));
                if (config != null) {
                    ViewRootImpl.this.updateConfiguration(config, false);
                } else if (!(ViewRootImpl.this.mWindowAttributes == null || (ViewRootImpl.this.mWindowAttributes.samsungFlags & 16384) == 0 || (ViewRootImpl.this.mWindowAttributes.softInputMode & 16) == 0 || ViewRootImpl.this.mPendingContentInsets.equals((Rect) args.arg2) || !((Rect) args.arg2).equals((Rect) args.arg3))) {
                    if (ViewRootImpl.this.mContentResizeAnimator == null) {
                        ViewRootImpl.this.initContentResizeAnimator();
                    }
                    ViewRootImpl.this.acquireContentResizeAnimationBooster();
                    ViewRootImpl.this.mPreContentInsets.set(ViewRootImpl.this.mPendingContentInsets);
                    needResizeAnimation = true;
                }
                ViewRootImpl.this.mWinFrame.set((Rect) args.arg1);
                ViewRootImpl.this.mPendingOverscanInsets.set((Rect) args.arg5);
                ViewRootImpl.this.mPendingContentInsets.set((Rect) args.arg2);
                ViewRootImpl.this.mPendingStableInsets.set((Rect) args.arg6);
                ViewRootImpl.this.mPendingVisibleInsets.set((Rect) args.arg3);
                ViewRootImpl.this.mPendingOutsets.set((Rect) args.arg7);
                ViewRootImpl.this.mCocktailBar.set((Rect) args.arg8);
                args.recycle();
                if (msg.what == 5) {
                    ViewRootImpl.this.mReportNextDraw = true;
                }
                if (ViewRootImpl.this.mView != null) {
                    ViewRootImpl.forceLayout(ViewRootImpl.this.mView);
                }
                if (needResizeAnimation) {
                    ViewRootImpl.this.startContentResizeAnimation();
                }
                ViewRootImpl.this.requestLayout();
            }
        }
    }

    static class W extends IWindow.Stub {
        private final WeakReference<ViewRootImpl> mViewAncestor;
        private final IWindowSession mWindowSession;

        W(ViewRootImpl viewAncestor) {
            this.mViewAncestor = new WeakReference(viewAncestor);
            this.mWindowSession = viewAncestor.mWindowSession;
        }

        public void resized(Rect frame, Rect overscanInsets, Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw, Configuration newConfig, Rect cocktailBarFrame) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchResized(frame, overscanInsets, contentInsets, visibleInsets, stableInsets, outsets, reportDraw, newConfig, cocktailBarFrame);
            }
        }

        public void moved(int newX, int newY) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchMoved(newX, newY);
            }
        }

        public void dispatchAttachedDisplayChanged(int displayId) {
        }

        public void dispatchAppVisibility(boolean visible) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchAppVisibility(visible);
            }
        }

        public void dispatchGetNewSurface() {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchGetNewSurface();
            }
        }

        public void windowFocusChanged(boolean hasFocus, boolean inTouchMode, boolean focusedAppChanged) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.windowFocusChanged(hasFocus, inTouchMode, focusedAppChanged);
            }
        }

        private static int checkCallingPermission(String permission) {
            try {
                return ActivityManagerNative.getDefault().checkPermission(permission, Binder.getCallingPid(), Binder.getCallingUid());
            } catch (RemoteException e) {
                return -1;
            }
        }

        public void executeCommand(String command, String parameters, ParcelFileDescriptor out) {
            IOException e;
            Throwable th;
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                View view = viewAncestor.mView;
                if (view == null) {
                    return;
                }
                if (checkCallingPermission("android.permission.DUMP") != 0) {
                    throw new SecurityException("Insufficient permissions to invoke executeCommand() from pid=" + Binder.getCallingPid() + ", uid=" + Binder.getCallingUid());
                }
                OutputStream clientStream = null;
                try {
                    OutputStream clientStream2 = new AutoCloseOutputStream(out);
                    try {
                        ViewDebug.dispatchCommand(view, command, parameters, clientStream2);
                        if (clientStream2 != null) {
                            try {
                                clientStream2.close();
                            } catch (IOException e2) {
                                e2.printStackTrace();
                            }
                        }
                    } catch (IOException e3) {
                        e2 = e3;
                        clientStream = clientStream2;
                        try {
                            e2.printStackTrace();
                            if (clientStream != null) {
                                try {
                                    clientStream.close();
                                } catch (IOException e22) {
                                    e22.printStackTrace();
                                }
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            if (clientStream != null) {
                                try {
                                    clientStream.close();
                                } catch (IOException e222) {
                                    e222.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        clientStream = clientStream2;
                        if (clientStream != null) {
                            clientStream.close();
                        }
                        throw th;
                    }
                } catch (IOException e4) {
                    e222 = e4;
                    e222.printStackTrace();
                    if (clientStream != null) {
                        clientStream.close();
                    }
                }
            }
        }

        public void closeSystemDialogs(String reason) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchCloseSystemDialogs(reason);
            }
        }

        public void dispatchWallpaperOffsets(float x, float y, float xStep, float yStep, boolean sync) {
            if (sync) {
                try {
                    this.mWindowSession.wallpaperOffsetsComplete(asBinder());
                } catch (RemoteException e) {
                }
            }
        }

        public void dispatchWallpaperCommand(String action, int x, int y, int z, Bundle extras, boolean sync) {
            if (sync) {
                try {
                    this.mWindowSession.wallpaperCommandComplete(asBinder(), null);
                } catch (RemoteException e) {
                }
            }
        }

        public void dispatchDragEvent(DragEvent event) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchDragEvent(event);
            }
        }

        public void dispatchSystemUiVisibilityChanged(int seq, int globalVisibility, int localValue, int localChanges) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchSystemUiVisibilityChanged(seq, globalVisibility, localValue, localChanges);
            }
        }

        public void onAnimationStarted(int remainingFrameCount) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchWindowAnimationStarted(remainingFrameCount);
            }
        }

        public void onAnimationStopped() {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchWindowAnimationStopped();
            }
        }

        public void dispatchSmartClipRemoteRequest(SmartClipRemoteRequestInfo request) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchSmartClipRemoteRequest(request);
            }
        }

        public void dispatchCoverStateChanged(boolean isOpen) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchCoverStateChanged(isOpen);
            }
        }

        public void dispatchWindowShown() {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchWindowShown();
            }
        }

        public void onSurfaceDestroyDeferred() {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchSurfaceDestroyDeferred();
            }
        }

        public void dispatchMultiWindowStateChanged(int state) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchMultiWindowStateChanged(state);
            }
        }
    }

    final class WindowInputEventReceiver extends InputEventReceiver {
        public WindowInputEventReceiver(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper);
        }

        public void onInputEvent(InputEvent event) {
            String traceKey = null;
            if (ViewRootImpl.SAFE_DEBUG && (event instanceof MotionEvent)) {
                MotionEvent motionEvent = (MotionEvent) event;
                traceKey = String.format("onInputEvent(Action=%d, X=%d, Y=%d)", new Object[]{Integer.valueOf(motionEvent.getAction()), Integer.valueOf((int) motionEvent.getX()), Integer.valueOf((int) motionEvent.getY())});
                Trace.traceBegin(8, traceKey);
            }
            ViewRootImpl.this.enqueueInputEvent(event, this, 0, true);
            if (traceKey != null) {
                Trace.traceEnd(8);
            }
        }

        public void onBatchedInputEventPending() {
            if (ViewRootImpl.this.mUnbufferedInputDispatch) {
                super.onBatchedInputEventPending();
            } else {
                ViewRootImpl.this.scheduleConsumeBatchedInput();
            }
        }

        public void dispose() {
            ViewRootImpl.this.unscheduleConsumeBatchedInput();
            super.dispose();
        }
    }

    static {
        boolean z;
        if (Debug.isProductShip() == 1) {
            z = false;
        } else {
            z = true;
        }
        SAFE_DEBUG = z;
    }

    public ViewRootImpl(Context context, Display display) {
        InputEventConsistencyVerifier inputEventConsistencyVerifier;
        if (InputEventConsistencyVerifier.isInstrumentationEnabled()) {
            inputEventConsistencyVerifier = new InputEventConsistencyVerifier(this, 0);
        } else {
            inputEventConsistencyVerifier = null;
        }
        this.mInputEventConsistencyVerifier = inputEventConsistencyVerifier;
        this.mProfile = false;
        this.mDisplayListener = new DisplayListener() {
            public void onDisplayChanged(int displayId) {
                if (ViewRootImpl.this.mView != null && ViewRootImpl.this.mDisplay.getDisplayId() == displayId) {
                    int oldDisplayState = ViewRootImpl.this.mAttachInfo.mDisplayState;
                    int newDisplayState = ViewRootImpl.this.mDisplay.getState();
                    if (oldDisplayState != newDisplayState) {
                        ViewRootImpl.this.mAttachInfo.mDisplayState = newDisplayState;
                        ViewRootImpl.this.pokeDrawLockIfNeeded();
                        if (oldDisplayState != 0) {
                            int oldScreenState = toViewScreenState(oldDisplayState);
                            int newScreenState = toViewScreenState(newDisplayState);
                            if (oldScreenState != newScreenState) {
                                ViewRootImpl.this.mView.dispatchScreenStateChanged(newScreenState);
                            }
                            if (oldDisplayState == 1) {
                                ViewRootImpl.this.mFullRedrawNeeded = true;
                                ViewRootImpl.this.scheduleTraversals();
                            }
                        }
                    }
                }
            }

            public void onDisplayRemoved(int displayId) {
            }

            public void onDisplayAdded(int displayId) {
            }

            private int toViewScreenState(int displayState) {
                return displayState == 1 ? 0 : 1;
            }
        };
        this.mResizePaint = new Paint();
        this.mSkipPanScrollEnterAnimation = false;
        this.mSkipPanScrollExitAnimation = false;
        this.mTwToolBoxTracking = false;
        this.mHandler = new ViewRootHandler();
        this.mTraversalRunnable = new TraversalRunnable();
        this.mConsumedBatchedInputRunnable = new ConsumeBatchedInputRunnable();
        this.mConsumeBatchedInputImmediatelyRunnable = new ConsumeBatchedInputImmediatelyRunnable();
        this.mInvalidateOnAnimationRunnable = new InvalidateOnAnimationRunnable();
        this.mContext = context;
        this.mWindowSession = WindowManagerGlobal.getWindowSession();
        this.mDisplay = display;
        this.mBasePackageName = context.getBasePackageName();
        this.mContentResolver = context.getContentResolver();
        this.mDisplayAdjustments = display.getDisplayAdjustments();
        this.mThread = Thread.currentThread();
        this.mHCTRelayoutHandler = new HCTRelayoutHandler();
        this.mLocation = new WindowLeaked(null);
        this.mLocation.fillInStackTrace();
        this.mWidth = -1;
        this.mHeight = -1;
        this.mDirty = new Rect();
        this.mTempRect = new Rect();
        this.mVisRect = new Rect();
        this.mWinFrame = new Rect();
        this.mWindow = new W(this);
        this.mTargetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        this.mViewVisibility = 8;
        this.mReportedViewVisibility = 8;
        this.mTransparentRegion = new Region();
        this.mPreviousTransparentRegion = new Region();
        this.mFirst = true;
        this.mAdded = false;
        this.mAttachInfo = new AttachInfo(this.mWindowSession, this.mWindow, display, this, this.mHandler, this);
        this.mAccessibilityManager = AccessibilityManager.getInstance(context);
        this.mAccessibilityInteractionConnectionManager = new AccessibilityInteractionConnectionManager();
        this.mAccessibilityManager.addAccessibilityStateChangeListener(this.mAccessibilityInteractionConnectionManager);
        this.mHighContrastTextManager = new HighContrastTextManager();
        this.mAccessibilityManager.addHighTextContrastStateChangeListener(this.mHighContrastTextManager);
        this.mViewConfiguration = ViewConfiguration.get(context);
        this.mDensity = context.getResources().getDisplayMetrics().densityDpi;
        this.mNoncompatDensity = context.getResources().getDisplayMetrics().noncompatDensityDpi;
        this.mFallbackEventHandler = new PhoneFallbackEventHandler(context);
        this.mChoreographer = Choreographer.getInstance();
        this.mChoreographer.getRootContext(this.mContext);
        this.mDisplayManager = (DisplayManager) context.getSystemService("display");
        loadSystemProperties();
        this.mLastConfiguration.setTo(context.getResources().getConfiguration());
        this.mSmartClipDispatcherProxy = new SmartClipRemoteRequestDispatcherProxy(context);
        PackageManager pm = context.getPackageManager();
        if (pm != null && pm.getSystemFeatureLevel("com.sec.feature.spen_usp") >= 3) {
            this.mMotionEventMonitor = new MotionEventMonitor();
        }
        if (TwToolBoxService.TOOLBOX_SUPPORT) {
            twUpdateToolBox();
        }
        this.mCocktailBar = new Rect();
    }

    public static void addFirstDrawHandler(Runnable callback) {
        synchronized (sFirstDrawHandlers) {
            if (!sFirstDrawComplete) {
                sFirstDrawHandlers.add(callback);
            }
        }
    }

    public static void addConfigCallback(ComponentCallbacks callback) {
        synchronized (sConfigCallbacks) {
            sConfigCallbacks.add(callback);
        }
    }

    public void profile() {
        this.mProfile = true;
    }

    static boolean isInTouchMode() {
        IWindowSession windowSession = WindowManagerGlobal.peekWindowSession();
        if (windowSession != null) {
            try {
                return windowSession.getInTouchMode();
            } catch (RemoteException e) {
            }
        }
        return false;
    }

    private void checkDcs(PackageInfo pi) {
        sIsDcsEnabledApp = pi.isDcsEnabledApp;
        sDcsFormat = pi.dcsFormat;
    }

    private void checkDss(PackageInfo pi) {
        sDssFactor = pi.dssFactor;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setView(android.view.View r28, android.view.WindowManager.LayoutParams r29, android.view.View r30) {
        /*
        r27 = this;
        monitor-enter(r27);
        r0 = r27;
        r4 = r0.mView;	 Catch:{ all -> 0x036d }
        if (r4 != 0) goto L_0x0624;
    L_0x0007:
        r0 = r28;
        r1 = r27;
        r1.mView = r0;	 Catch:{ all -> 0x036d }
        r4 = "ViewRootImpl";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r5.<init>();	 Catch:{ all -> 0x036d }
        r6 = "#1 mView = ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r0 = r28;
        r5 = r5.append(r0);	 Catch:{ all -> 0x036d }
        r5 = r5.toString();	 Catch:{ all -> 0x036d }
        android.util.Log.d(r4, r5);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mContext;	 Catch:{ NameNotFoundException -> 0x0376 }
        r4 = r4.getPackageManager();	 Catch:{ NameNotFoundException -> 0x0376 }
        r0 = r27;
        r5 = r0.mBasePackageName;	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = 0;
        r21 = r4.getPackageInfo(r5, r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r0 = r21;
        r4 = r0.isDBQEnabledforSv;	 Catch:{ NameNotFoundException -> 0x0376 }
        if (r4 == 0) goto L_0x0370;
    L_0x003e:
        r0 = r21;
        r4 = r0.bufferCountInfo;	 Catch:{ NameNotFoundException -> 0x0376 }
    L_0x0042:
        sSVBufferCount = r4;	 Catch:{ NameNotFoundException -> 0x0376 }
        r4 = sBufferCount;	 Catch:{ NameNotFoundException -> 0x0376 }
        r5 = -1;
        if (r4 != r5) goto L_0x0055;
    L_0x0049:
        r0 = r21;
        r4 = r0.isDBQEnabledforAct;	 Catch:{ NameNotFoundException -> 0x0376 }
        if (r4 == 0) goto L_0x0373;
    L_0x004f:
        r0 = r21;
        r4 = r0.bufferCountInfo;	 Catch:{ NameNotFoundException -> 0x0376 }
    L_0x0053:
        sBufferCount = r4;	 Catch:{ NameNotFoundException -> 0x0376 }
    L_0x0055:
        r0 = r21;
        r4 = r0.dtsFactor;	 Catch:{ NameNotFoundException -> 0x0376 }
        sDTSFactor = r4;	 Catch:{ NameNotFoundException -> 0x0376 }
        r0 = r27;
        r1 = r21;
        r0.checkDcs(r1);	 Catch:{ NameNotFoundException -> 0x0376 }
        r0 = r27;
        r1 = r21;
        r0.checkDss(r1);	 Catch:{ NameNotFoundException -> 0x0376 }
        r4 = SAFE_DEBUG;	 Catch:{ NameNotFoundException -> 0x0376 }
        if (r4 == 0) goto L_0x00f3;
    L_0x006d:
        r4 = "ViewRootImpl";
        r5 = new java.lang.StringBuilder;	 Catch:{ NameNotFoundException -> 0x0376 }
        r5.<init>();	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = "Buffer Count from app info with  ::";
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = sBufferCount;	 Catch:{ NameNotFoundException -> 0x0376 }
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = " && ";
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = sSVBufferCount;	 Catch:{ NameNotFoundException -> 0x0376 }
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = " for :: ";
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r0 = r27;
        r6 = r0.mBasePackageName;	 Catch:{ NameNotFoundException -> 0x0376 }
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = " from View :: ";
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r0 = r27;
        r6 = r0.mView;	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = r6.getBufferCount();	 Catch:{ NameNotFoundException -> 0x0376 }
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = " DBQ Enabled ::";
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r0 = r21;
        r6 = r0.isDBQEnabledforAct;	 Catch:{ NameNotFoundException -> 0x0376 }
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = " ";
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r0 = r21;
        r6 = r0.isDBQEnabledforSv;	 Catch:{ NameNotFoundException -> 0x0376 }
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = " sIsDcsEnabledApp=";
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = sIsDcsEnabledApp;	 Catch:{ NameNotFoundException -> 0x0376 }
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = " sDcsFormat=";
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = sDcsFormat;	 Catch:{ NameNotFoundException -> 0x0376 }
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = "  sDssFactor=";
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r6 = sDssFactor;	 Catch:{ NameNotFoundException -> 0x0376 }
        r5 = r5.append(r6);	 Catch:{ NameNotFoundException -> 0x0376 }
        r5 = r5.toString();	 Catch:{ NameNotFoundException -> 0x0376 }
        android.util.Log.i(r4, r5);	 Catch:{ NameNotFoundException -> 0x0376 }
    L_0x00f3:
        r0 = r29;
        r4 = r0.samsungFlags;	 Catch:{ all -> 0x036d }
        r4 = r4 & 128;
        if (r4 == 0) goto L_0x0385;
    L_0x00fb:
        r0 = r29;
        r4 = r0.type;	 Catch:{ all -> 0x036d }
        r5 = 1;
        if (r4 != r5) goto L_0x0385;
    L_0x0102:
        r4 = 1;
    L_0x0103:
        r0 = r27;
        r0.mIsThisWindowDontNeedSurfaceBuffer = r4;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mIsThisWindowDontNeedSurfaceBuffer;	 Catch:{ all -> 0x036d }
        if (r4 == 0) goto L_0x013c;
    L_0x010d:
        r4 = SAFE_DEBUG;	 Catch:{ all -> 0x036d }
        if (r4 == 0) goto L_0x013c;
    L_0x0111:
        r4 = "ViewRootImpl";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r5.<init>();	 Catch:{ all -> 0x036d }
        r6 = "window=";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r6 = r29.getTitle();	 Catch:{ all -> 0x036d }
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r6 = ",  mIsThisWindowDontNeedSurfaceBuffer=";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r6 = r0.mIsThisWindowDontNeedSurfaceBuffer;	 Catch:{ all -> 0x036d }
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r5 = r5.toString();	 Catch:{ all -> 0x036d }
        android.util.Slog.d(r4, r5);	 Catch:{ all -> 0x036d }
    L_0x013c:
        r0 = r27;
        r4 = r0.mAttachInfo;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r5 = r0.mDisplay;	 Catch:{ all -> 0x036d }
        r5 = r5.getState();	 Catch:{ all -> 0x036d }
        r4.mDisplayState = r5;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mDisplayManager;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r5 = r0.mDisplayListener;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r6 = r0.mHandler;	 Catch:{ all -> 0x036d }
        r4.registerDisplayListener(r5, r6);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mView;	 Catch:{ all -> 0x036d }
        r4 = r4.getRawLayoutDirection();	 Catch:{ all -> 0x036d }
        r0 = r27;
        r0.mViewLayoutDirectionInitial = r4;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mFallbackEventHandler;	 Catch:{ all -> 0x036d }
        r0 = r28;
        r4.setView(r0);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mWindowAttributes;	 Catch:{ all -> 0x036d }
        r0 = r29;
        r4.copyFrom(r0);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mWindowAttributes;	 Catch:{ all -> 0x036d }
        r4 = r4.packageName;	 Catch:{ all -> 0x036d }
        if (r4 != 0) goto L_0x0189;
    L_0x017f:
        r0 = r27;
        r4 = r0.mWindowAttributes;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r5 = r0.mBasePackageName;	 Catch:{ all -> 0x036d }
        r4.packageName = r5;	 Catch:{ all -> 0x036d }
    L_0x0189:
        r0 = r27;
        r0 = r0.mWindowAttributes;	 Catch:{ all -> 0x036d }
        r29 = r0;
        r0 = r29;
        r4 = r0.flags;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r0.mClientWindowLayoutFlags = r4;	 Catch:{ all -> 0x036d }
        r4 = 0;
        r5 = 0;
        r0 = r27;
        r0.setAccessibilityFocus(r4, r5);	 Catch:{ all -> 0x036d }
        r0 = r28;
        r4 = r0 instanceof com.android.internal.view.RootViewSurfaceTaker;	 Catch:{ all -> 0x036d }
        if (r4 == 0) goto L_0x01ca;
    L_0x01a4:
        r0 = r28;
        r0 = (com.android.internal.view.RootViewSurfaceTaker) r0;	 Catch:{ all -> 0x036d }
        r4 = r0;
        r4 = r4.willYouTakeTheSurface();	 Catch:{ all -> 0x036d }
        r0 = r27;
        r0.mSurfaceHolderCallback = r4;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mSurfaceHolderCallback;	 Catch:{ all -> 0x036d }
        if (r4 == 0) goto L_0x01ca;
    L_0x01b7:
        r4 = new android.view.ViewRootImpl$TakenSurfaceHolder;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4.<init>();	 Catch:{ all -> 0x036d }
        r0 = r27;
        r0.mSurfaceHolder = r4;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mSurfaceHolder;	 Catch:{ all -> 0x036d }
        r5 = 0;
        r4.setFormat(r5);	 Catch:{ all -> 0x036d }
    L_0x01ca:
        r0 = r29;
        r4 = r0.hasManualSurfaceInsets;	 Catch:{ all -> 0x036d }
        if (r4 != 0) goto L_0x01ee;
    L_0x01d0:
        r4 = r28.getZ();	 Catch:{ all -> 0x036d }
        r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = r4 * r5;
        r4 = (double) r4;	 Catch:{ all -> 0x036d }
        r4 = java.lang.Math.ceil(r4);	 Catch:{ all -> 0x036d }
        r0 = (int) r4;	 Catch:{ all -> 0x036d }
        r24 = r0;
        r0 = r29;
        r4 = r0.surfaceInsets;	 Catch:{ all -> 0x036d }
        r0 = r24;
        r1 = r24;
        r2 = r24;
        r3 = r24;
        r4.set(r0, r1, r2, r3);	 Catch:{ all -> 0x036d }
    L_0x01ee:
        r0 = r27;
        r4 = r0.mDisplayAdjustments;	 Catch:{ all -> 0x036d }
        r14 = r4.getCompatibilityInfo();	 Catch:{ all -> 0x036d }
        r4 = r14.getTranslator();	 Catch:{ all -> 0x036d }
        r0 = r27;
        r0.mTranslator = r4;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mSurfaceHolder;	 Catch:{ all -> 0x036d }
        if (r4 != 0) goto L_0x020b;
    L_0x0204:
        r0 = r27;
        r1 = r29;
        r0.enableHardwareAcceleration(r1);	 Catch:{ all -> 0x036d }
    L_0x020b:
        r23 = 0;
        r0 = r27;
        r4 = r0.mTranslator;	 Catch:{ all -> 0x036d }
        if (r4 == 0) goto L_0x022c;
    L_0x0213:
        r0 = r27;
        r4 = r0.mSurface;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r5 = r0.mTranslator;	 Catch:{ all -> 0x036d }
        r4.setCompatibilityTranslator(r5);	 Catch:{ all -> 0x036d }
        r23 = 1;
        r29.backup();	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mTranslator;	 Catch:{ all -> 0x036d }
        r0 = r29;
        r4.translateWindowLayout(r0);	 Catch:{ all -> 0x036d }
    L_0x022c:
        r4 = r14.supportsScreen();	 Catch:{ all -> 0x036d }
        if (r4 != 0) goto L_0x0241;
    L_0x0232:
        r0 = r29;
        r4 = r0.privateFlags;	 Catch:{ all -> 0x036d }
        r4 = r4 | 128;
        r0 = r29;
        r0.privateFlags = r4;	 Catch:{ all -> 0x036d }
        r4 = 1;
        r0 = r27;
        r0.mLastInCompatMode = r4;	 Catch:{ all -> 0x036d }
    L_0x0241:
        r0 = r29;
        r4 = r0.softInputMode;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r0.mSoftInputMode = r4;	 Catch:{ all -> 0x036d }
        r4 = 1;
        r0 = r27;
        r0.mWindowAttributesChanged = r4;	 Catch:{ all -> 0x036d }
        r4 = -1;
        r0 = r27;
        r0.mWindowAttributesChangesFlag = r4;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mAttachInfo;	 Catch:{ all -> 0x036d }
        r0 = r28;
        r4.mRootView = r0;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r5 = r0.mAttachInfo;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mTranslator;	 Catch:{ all -> 0x036d }
        if (r4 == 0) goto L_0x0388;
    L_0x0265:
        r4 = 1;
    L_0x0266:
        r5.mScalingRequired = r4;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r5 = r0.mAttachInfo;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mTranslator;	 Catch:{ all -> 0x036d }
        if (r4 != 0) goto L_0x038b;
    L_0x0272:
        r4 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
    L_0x0274:
        r5.mApplicationScale = r4;	 Catch:{ all -> 0x036d }
        if (r30 == 0) goto L_0x0282;
    L_0x0278:
        r0 = r27;
        r4 = r0.mAttachInfo;	 Catch:{ all -> 0x036d }
        r5 = r30.getApplicationWindowToken();	 Catch:{ all -> 0x036d }
        r4.mPanelParentWindowToken = r5;	 Catch:{ all -> 0x036d }
    L_0x0282:
        r4 = 1;
        r0 = r27;
        r0.mAdded = r4;	 Catch:{ all -> 0x036d }
        r27.requestLayout();	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mWindowAttributes;	 Catch:{ all -> 0x036d }
        r4 = r4.inputFeatures;	 Catch:{ all -> 0x036d }
        r4 = r4 & 2;
        if (r4 != 0) goto L_0x0393;
    L_0x0294:
        r4 = new android.view.InputChannel;	 Catch:{ all -> 0x036d }
        r4.<init>();	 Catch:{ all -> 0x036d }
        r0 = r27;
        r0.mInputChannel = r4;	 Catch:{ all -> 0x036d }
    L_0x029d:
        r0 = r27;
        r4 = r0.mWindowAttributes;	 Catch:{ RemoteException -> 0x039c }
        r4 = r4.type;	 Catch:{ RemoteException -> 0x039c }
        r0 = r27;
        r0.mOrigWindowType = r4;	 Catch:{ RemoteException -> 0x039c }
        r0 = r27;
        r4 = r0.mAttachInfo;	 Catch:{ RemoteException -> 0x039c }
        r5 = 1;
        r4.mRecomputeGlobalAttributes = r5;	 Catch:{ RemoteException -> 0x039c }
        r27.collectViewAttributes();	 Catch:{ RemoteException -> 0x039c }
        r0 = r27;
        r4 = r0.mWindowSession;	 Catch:{ RemoteException -> 0x039c }
        r0 = r27;
        r5 = r0.mWindow;	 Catch:{ RemoteException -> 0x039c }
        r0 = r27;
        r6 = r0.mSeq;	 Catch:{ RemoteException -> 0x039c }
        r0 = r27;
        r7 = r0.mWindowAttributes;	 Catch:{ RemoteException -> 0x039c }
        r8 = r27.getHostVisibility();	 Catch:{ RemoteException -> 0x039c }
        r0 = r27;
        r9 = r0.mDisplay;	 Catch:{ RemoteException -> 0x039c }
        r9 = r9.getDisplayId();	 Catch:{ RemoteException -> 0x039c }
        r0 = r27;
        r10 = r0.mAttachInfo;	 Catch:{ RemoteException -> 0x039c }
        r10 = r10.mContentInsets;	 Catch:{ RemoteException -> 0x039c }
        r0 = r27;
        r11 = r0.mAttachInfo;	 Catch:{ RemoteException -> 0x039c }
        r11 = r11.mStableInsets;	 Catch:{ RemoteException -> 0x039c }
        r0 = r27;
        r12 = r0.mAttachInfo;	 Catch:{ RemoteException -> 0x039c }
        r12 = r12.mOutsets;	 Catch:{ RemoteException -> 0x039c }
        r0 = r27;
        r13 = r0.mInputChannel;	 Catch:{ RemoteException -> 0x039c }
        r22 = r4.addToDisplay(r5, r6, r7, r8, r9, r10, r11, r12, r13);	 Catch:{ RemoteException -> 0x039c }
        if (r23 == 0) goto L_0x02ec;
    L_0x02e9:
        r29.restore();	 Catch:{ all -> 0x036d }
    L_0x02ec:
        r0 = r27;
        r4 = r0.mTranslator;	 Catch:{ all -> 0x036d }
        if (r4 == 0) goto L_0x02ff;
    L_0x02f2:
        r0 = r27;
        r4 = r0.mTranslator;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r5 = r0.mAttachInfo;	 Catch:{ all -> 0x036d }
        r5 = r5.mContentInsets;	 Catch:{ all -> 0x036d }
        r4.translateRectInScreenToAppWindow(r5);	 Catch:{ all -> 0x036d }
    L_0x02ff:
        r0 = r27;
        r4 = r0.mPendingOverscanInsets;	 Catch:{ all -> 0x036d }
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r4.set(r5, r6, r7, r8);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mPendingContentInsets;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r5 = r0.mAttachInfo;	 Catch:{ all -> 0x036d }
        r5 = r5.mContentInsets;	 Catch:{ all -> 0x036d }
        r4.set(r5);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mPendingStableInsets;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r5 = r0.mAttachInfo;	 Catch:{ all -> 0x036d }
        r5 = r5.mStableInsets;	 Catch:{ all -> 0x036d }
        r4.set(r5);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mPendingVisibleInsets;	 Catch:{ all -> 0x036d }
        r5 = 0;
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r4.set(r5, r6, r7, r8);	 Catch:{ all -> 0x036d }
        if (r22 >= 0) goto L_0x04f9;
    L_0x0331:
        r0 = r27;
        r4 = r0.mAttachInfo;	 Catch:{ all -> 0x036d }
        r5 = 0;
        r4.mRootView = r5;	 Catch:{ all -> 0x036d }
        r4 = 0;
        r0 = r27;
        r0.mAdded = r4;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mFallbackEventHandler;	 Catch:{ all -> 0x036d }
        r5 = 0;
        r4.setView(r5);	 Catch:{ all -> 0x036d }
        r27.unscheduleTraversals();	 Catch:{ all -> 0x036d }
        r4 = 0;
        r5 = 0;
        r0 = r27;
        r0.setAccessibilityFocus(r4, r5);	 Catch:{ all -> 0x036d }
        switch(r22) {
            case -100: goto L_0x046b;
            case -10: goto L_0x04d6;
            case -9: goto L_0x04b3;
            case -8: goto L_0x0490;
            case -7: goto L_0x046d;
            case -6: goto L_0x0469;
            case -5: goto L_0x0446;
            case -4: goto L_0x0423;
            case -3: goto L_0x0400;
            case -2: goto L_0x03dd;
            case -1: goto L_0x03dd;
            default: goto L_0x0352;
        };	 Catch:{ all -> 0x036d }
    L_0x0352:
        r4 = new java.lang.RuntimeException;	 Catch:{ all -> 0x036d }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r5.<init>();	 Catch:{ all -> 0x036d }
        r6 = "Unable to add window -- unknown error code ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r0 = r22;
        r5 = r5.append(r0);	 Catch:{ all -> 0x036d }
        r5 = r5.toString();	 Catch:{ all -> 0x036d }
        r4.<init>(r5);	 Catch:{ all -> 0x036d }
        throw r4;	 Catch:{ all -> 0x036d }
    L_0x036d:
        r4 = move-exception;
        monitor-exit(r27);	 Catch:{ all -> 0x036d }
        throw r4;
    L_0x0370:
        r4 = -1;
        goto L_0x0042;
    L_0x0373:
        r4 = -1;
        goto L_0x0053;
    L_0x0376:
        r16 = move-exception;
        r4 = SAFE_DEBUG;	 Catch:{ all -> 0x036d }
        if (r4 == 0) goto L_0x00f3;
    L_0x037b:
        r4 = "SRIB_DBQ_SRIB_DCS_ViewRootImpl";
        r5 = "setView: Name not found exception caught";
        android.util.Log.d(r4, r5);	 Catch:{ all -> 0x036d }
        goto L_0x00f3;
    L_0x0385:
        r4 = 0;
        goto L_0x0103;
    L_0x0388:
        r4 = 0;
        goto L_0x0266;
    L_0x038b:
        r0 = r27;
        r4 = r0.mTranslator;	 Catch:{ all -> 0x036d }
        r4 = r4.applicationScale;	 Catch:{ all -> 0x036d }
        goto L_0x0274;
    L_0x0393:
        r4 = "ISSUE_DEBUG";
        r5 = "INPUT_FEATURE_NO_INPUT_CHANNEL";
        android.util.Log.d(r4, r5);	 Catch:{ all -> 0x036d }
        goto L_0x029d;
    L_0x039c:
        r16 = move-exception;
        r4 = 0;
        r0 = r27;
        r0.mAdded = r4;	 Catch:{ all -> 0x03d6 }
        r4 = 0;
        r0 = r27;
        r0.mView = r4;	 Catch:{ all -> 0x03d6 }
        r4 = "ViewRootImpl";
        r5 = "#2 mView = null";
        android.util.Log.d(r4, r5);	 Catch:{ all -> 0x03d6 }
        r0 = r27;
        r4 = r0.mAttachInfo;	 Catch:{ all -> 0x03d6 }
        r5 = 0;
        r4.mRootView = r5;	 Catch:{ all -> 0x03d6 }
        r4 = 0;
        r0 = r27;
        r0.mInputChannel = r4;	 Catch:{ all -> 0x03d6 }
        r0 = r27;
        r4 = r0.mFallbackEventHandler;	 Catch:{ all -> 0x03d6 }
        r5 = 0;
        r4.setView(r5);	 Catch:{ all -> 0x03d6 }
        r27.unscheduleTraversals();	 Catch:{ all -> 0x03d6 }
        r4 = 0;
        r5 = 0;
        r0 = r27;
        r0.setAccessibilityFocus(r4, r5);	 Catch:{ all -> 0x03d6 }
        r4 = new java.lang.RuntimeException;	 Catch:{ all -> 0x03d6 }
        r5 = "Adding window failed";
        r0 = r16;
        r4.<init>(r5, r0);	 Catch:{ all -> 0x03d6 }
        throw r4;	 Catch:{ all -> 0x03d6 }
    L_0x03d6:
        r4 = move-exception;
        if (r23 == 0) goto L_0x03dc;
    L_0x03d9:
        r29.restore();	 Catch:{ all -> 0x036d }
    L_0x03dc:
        throw r4;	 Catch:{ all -> 0x036d }
    L_0x03dd:
        r4 = new android.view.WindowManager$BadTokenException;	 Catch:{ all -> 0x036d }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r5.<init>();	 Catch:{ all -> 0x036d }
        r6 = "Unable to add window -- token ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r0 = r29;
        r6 = r0.token;	 Catch:{ all -> 0x036d }
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r6 = " is not valid; is your activity running?";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r5 = r5.toString();	 Catch:{ all -> 0x036d }
        r4.<init>(r5);	 Catch:{ all -> 0x036d }
        throw r4;	 Catch:{ all -> 0x036d }
    L_0x0400:
        r4 = new android.view.WindowManager$BadTokenException;	 Catch:{ all -> 0x036d }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r5.<init>();	 Catch:{ all -> 0x036d }
        r6 = "Unable to add window -- token ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r0 = r29;
        r6 = r0.token;	 Catch:{ all -> 0x036d }
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r6 = " is not for an application";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r5 = r5.toString();	 Catch:{ all -> 0x036d }
        r4.<init>(r5);	 Catch:{ all -> 0x036d }
        throw r4;	 Catch:{ all -> 0x036d }
    L_0x0423:
        r4 = new android.view.WindowManager$BadTokenException;	 Catch:{ all -> 0x036d }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r5.<init>();	 Catch:{ all -> 0x036d }
        r6 = "Unable to add window -- app for token ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r0 = r29;
        r6 = r0.token;	 Catch:{ all -> 0x036d }
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r6 = " is exiting";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r5 = r5.toString();	 Catch:{ all -> 0x036d }
        r4.<init>(r5);	 Catch:{ all -> 0x036d }
        throw r4;	 Catch:{ all -> 0x036d }
    L_0x0446:
        r4 = new android.view.WindowManager$BadTokenException;	 Catch:{ all -> 0x036d }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r5.<init>();	 Catch:{ all -> 0x036d }
        r6 = "Unable to add window -- window ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r6 = r0.mWindow;	 Catch:{ all -> 0x036d }
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r6 = " has already been added";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r5 = r5.toString();	 Catch:{ all -> 0x036d }
        r4.<init>(r5);	 Catch:{ all -> 0x036d }
        throw r4;	 Catch:{ all -> 0x036d }
    L_0x0469:
        monitor-exit(r27);	 Catch:{ all -> 0x036d }
    L_0x046a:
        return;
    L_0x046b:
        monitor-exit(r27);	 Catch:{ all -> 0x036d }
        goto L_0x046a;
    L_0x046d:
        r4 = new android.view.WindowManager$BadTokenException;	 Catch:{ all -> 0x036d }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r5.<init>();	 Catch:{ all -> 0x036d }
        r6 = "Unable to add window ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r6 = r0.mWindow;	 Catch:{ all -> 0x036d }
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r6 = " -- another window of this type already exists";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r5 = r5.toString();	 Catch:{ all -> 0x036d }
        r4.<init>(r5);	 Catch:{ all -> 0x036d }
        throw r4;	 Catch:{ all -> 0x036d }
    L_0x0490:
        r4 = new android.view.WindowManager$BadTokenException;	 Catch:{ all -> 0x036d }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r5.<init>();	 Catch:{ all -> 0x036d }
        r6 = "Unable to add window ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r6 = r0.mWindow;	 Catch:{ all -> 0x036d }
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r6 = " -- permission denied for this window type";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r5 = r5.toString();	 Catch:{ all -> 0x036d }
        r4.<init>(r5);	 Catch:{ all -> 0x036d }
        throw r4;	 Catch:{ all -> 0x036d }
    L_0x04b3:
        r4 = new android.view.WindowManager$InvalidDisplayException;	 Catch:{ all -> 0x036d }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r5.<init>();	 Catch:{ all -> 0x036d }
        r6 = "Unable to add window ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r6 = r0.mWindow;	 Catch:{ all -> 0x036d }
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r6 = " -- the specified display can not be found";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r5 = r5.toString();	 Catch:{ all -> 0x036d }
        r4.<init>(r5);	 Catch:{ all -> 0x036d }
        throw r4;	 Catch:{ all -> 0x036d }
    L_0x04d6:
        r4 = new android.view.WindowManager$InvalidDisplayException;	 Catch:{ all -> 0x036d }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r5.<init>();	 Catch:{ all -> 0x036d }
        r6 = "Unable to add window ";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r6 = r0.mWindow;	 Catch:{ all -> 0x036d }
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r6 = " -- the specified window type is not valid";
        r5 = r5.append(r6);	 Catch:{ all -> 0x036d }
        r5 = r5.toString();	 Catch:{ all -> 0x036d }
        r4.<init>(r5);	 Catch:{ all -> 0x036d }
        throw r4;	 Catch:{ all -> 0x036d }
    L_0x04f9:
        r0 = r28;
        r4 = r0 instanceof com.android.internal.view.RootViewSurfaceTaker;	 Catch:{ all -> 0x036d }
        if (r4 == 0) goto L_0x050c;
    L_0x04ff:
        r0 = r28;
        r0 = (com.android.internal.view.RootViewSurfaceTaker) r0;	 Catch:{ all -> 0x036d }
        r4 = r0;
        r4 = r4.willYouTakeTheInputQueue();	 Catch:{ all -> 0x036d }
        r0 = r27;
        r0.mInputQueueCallback = r4;	 Catch:{ all -> 0x036d }
    L_0x050c:
        r0 = r27;
        r4 = r0.mInputChannel;	 Catch:{ all -> 0x036d }
        if (r4 == 0) goto L_0x053f;
    L_0x0512:
        r0 = r27;
        r4 = r0.mInputQueueCallback;	 Catch:{ all -> 0x036d }
        if (r4 == 0) goto L_0x052c;
    L_0x0518:
        r4 = new android.view.InputQueue;	 Catch:{ all -> 0x036d }
        r4.<init>();	 Catch:{ all -> 0x036d }
        r0 = r27;
        r0.mInputQueue = r4;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mInputQueueCallback;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r5 = r0.mInputQueue;	 Catch:{ all -> 0x036d }
        r4.onInputQueueCreated(r5);	 Catch:{ all -> 0x036d }
    L_0x052c:
        r4 = new android.view.ViewRootImpl$WindowInputEventReceiver;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r5 = r0.mInputChannel;	 Catch:{ all -> 0x036d }
        r6 = android.os.Looper.myLooper();	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4.<init>(r5, r6);	 Catch:{ all -> 0x036d }
        r0 = r27;
        r0.mInputEventReceiver = r4;	 Catch:{ all -> 0x036d }
    L_0x053f:
        r0 = r28;
        r1 = r27;
        r0.assignParent(r1);	 Catch:{ all -> 0x036d }
        r4 = r22 & 1;
        if (r4 == 0) goto L_0x0627;
    L_0x054a:
        r4 = 1;
    L_0x054b:
        r0 = r27;
        r0.mAddedTouchMode = r4;	 Catch:{ all -> 0x036d }
        r4 = r22 & 2;
        if (r4 == 0) goto L_0x062a;
    L_0x0553:
        r4 = 1;
    L_0x0554:
        r0 = r27;
        r0.mAppVisible = r4;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mAccessibilityManager;	 Catch:{ all -> 0x036d }
        r4 = r4.isEnabled();	 Catch:{ all -> 0x036d }
        if (r4 == 0) goto L_0x0569;
    L_0x0562:
        r0 = r27;
        r4 = r0.mAccessibilityInteractionConnectionManager;	 Catch:{ all -> 0x036d }
        r4.ensureConnection();	 Catch:{ all -> 0x036d }
    L_0x0569:
        r4 = r28.getImportantForAccessibility();	 Catch:{ all -> 0x036d }
        if (r4 != 0) goto L_0x0575;
    L_0x056f:
        r4 = 1;
        r0 = r28;
        r0.setImportantForAccessibility(r4);	 Catch:{ all -> 0x036d }
    L_0x0575:
        r15 = r29.getTitle();	 Catch:{ all -> 0x036d }
        r4 = new android.view.ViewRootImpl$SyntheticInputStage;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4.<init>();	 Catch:{ all -> 0x036d }
        r0 = r27;
        r0.mSyntheticInputStage = r4;	 Catch:{ all -> 0x036d }
        r25 = new android.view.ViewRootImpl$ViewPostImeInputStage;	 Catch:{ all -> 0x036d }
        r0 = r27;
        r4 = r0.mSyntheticInputStage;	 Catch:{ all -> 0x036d }
        r0 = r25;
        r1 = r27;
        r0.<init>(r4);	 Catch:{ all -> 0x036d }
        r19 = new android.view.ViewRootImpl$NativePostImeInputStage;	 Catch:{ all -> 0x036d }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r4.<init>();	 Catch:{ all -> 0x036d }
        r5 = "aq:native-post-ime:";
        r4 = r4.append(r5);	 Catch:{ all -> 0x036d }
        r4 = r4.append(r15);	 Catch:{ all -> 0x036d }
        r4 = r4.toString();	 Catch:{ all -> 0x036d }
        r0 = r19;
        r1 = r27;
        r2 = r25;
        r0.<init>(r2, r4);	 Catch:{ all -> 0x036d }
        r17 = new android.view.ViewRootImpl$EarlyPostImeInputStage;	 Catch:{ all -> 0x036d }
        r0 = r17;
        r1 = r27;
        r2 = r19;
        r0.<init>(r2);	 Catch:{ all -> 0x036d }
        r18 = new android.view.ViewRootImpl$ImeInputStage;	 Catch:{ all -> 0x036d }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r4.<init>();	 Catch:{ all -> 0x036d }
        r5 = "aq:ime:";
        r4 = r4.append(r5);	 Catch:{ all -> 0x036d }
        r4 = r4.append(r15);	 Catch:{ all -> 0x036d }
        r4 = r4.toString();	 Catch:{ all -> 0x036d }
        r0 = r18;
        r1 = r27;
        r2 = r17;
        r0.<init>(r2, r4);	 Catch:{ all -> 0x036d }
        r26 = new android.view.ViewRootImpl$ViewPreImeInputStage;	 Catch:{ all -> 0x036d }
        r0 = r26;
        r1 = r27;
        r2 = r18;
        r0.<init>(r2);	 Catch:{ all -> 0x036d }
        r20 = new android.view.ViewRootImpl$NativePreImeInputStage;	 Catch:{ all -> 0x036d }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r4.<init>();	 Catch:{ all -> 0x036d }
        r5 = "aq:native-pre-ime:";
        r4 = r4.append(r5);	 Catch:{ all -> 0x036d }
        r4 = r4.append(r15);	 Catch:{ all -> 0x036d }
        r4 = r4.toString();	 Catch:{ all -> 0x036d }
        r0 = r20;
        r1 = r27;
        r2 = r26;
        r0.<init>(r2, r4);	 Catch:{ all -> 0x036d }
        r0 = r20;
        r1 = r27;
        r1.mFirstInputStage = r0;	 Catch:{ all -> 0x036d }
        r0 = r17;
        r1 = r27;
        r1.mFirstPostImeInputStage = r0;	 Catch:{ all -> 0x036d }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x036d }
        r4.<init>();	 Catch:{ all -> 0x036d }
        r5 = "aq:pending:";
        r4 = r4.append(r5);	 Catch:{ all -> 0x036d }
        r4 = r4.append(r15);	 Catch:{ all -> 0x036d }
        r4 = r4.toString();	 Catch:{ all -> 0x036d }
        r0 = r27;
        r0.mPendingInputEventQueueLengthCounterName = r4;	 Catch:{ all -> 0x036d }
    L_0x0624:
        monitor-exit(r27);	 Catch:{ all -> 0x036d }
        goto L_0x046a;
    L_0x0627:
        r4 = 0;
        goto L_0x054b;
    L_0x062a:
        r4 = 0;
        goto L_0x0554;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewRootImpl.setView(android.view.View, android.view.WindowManager$LayoutParams, android.view.View):void");
    }

    private boolean isInLocalFocusMode() {
        return (this.mWindowAttributes.flags & 268435456) != 0;
    }

    public int getWindowFlags() {
        return this.mWindowAttributes.flags;
    }

    public int getDisplayId() {
        return this.mDisplay.getDisplayId();
    }

    public CharSequence getTitle() {
        return this.mWindowAttributes.getTitle();
    }

    void destroyHardwareResources() {
        if (this.mAttachInfo.mHardwareRenderer != null) {
            this.mAttachInfo.mHardwareRenderer.destroyHardwareResources(this.mView);
            this.mAttachInfo.mHardwareRenderer.destroy();
        }
    }

    void resetSoftwareCaches(View view) {
        if (view != null) {
            view.destroyDrawingCache();
            if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    resetSoftwareCaches(group.getChildAt(i));
                }
            }
        }
    }

    private void setHCTLetterSpacing(View view, boolean mode) {
        if (view != null) {
            if (view instanceof TextView) {
                if (mode) {
                    ((TextView) view).setHCTLetterSpacing();
                } else {
                    ((TextView) view).resetHCTLetterSpacing();
                }
            } else if (view instanceof ViewGroup) {
                ViewGroup group = (ViewGroup) view;
                int count = group.getChildCount();
                for (int i = 0; i < count; i++) {
                    setHCTLetterSpacing(group.getChildAt(i), mode);
                }
            }
        }
    }

    public void detachFunctor(long functor) {
        this.mBlockResizeBuffer = true;
        if (this.mAttachInfo.mHardwareRenderer != null) {
            this.mAttachInfo.mHardwareRenderer.stopDrawing();
        }
    }

    public void invokeFunctor(long functor, boolean waitForCompletion) {
        ThreadedRenderer.invokeFunctor(functor, waitForCompletion);
    }

    public void registerAnimatingRenderNode(RenderNode animator) {
        if (this.mAttachInfo.mHardwareRenderer != null) {
            this.mAttachInfo.mHardwareRenderer.registerAnimatingRenderNode(animator);
            return;
        }
        if (this.mAttachInfo.mPendingAnimatingRenderNodes == null) {
            this.mAttachInfo.mPendingAnimatingRenderNodes = new ArrayList();
        }
        this.mAttachInfo.mPendingAnimatingRenderNodes.add(animator);
    }

    private void enableHardwareAcceleration(LayoutParams attrs) {
        this.mAttachInfo.mHardwareAccelerated = false;
        this.mAttachInfo.mHardwareAccelerationRequested = false;
        if ((!sIsDcsEnabledApp || ((attrs.type == 1 && attrs.gravity == 0) || sDcsFormat != 4)) && this.mTranslator == null) {
            boolean hardwareAccelerated;
            if ((attrs.flags & 16777216) != 0) {
                hardwareAccelerated = true;
            } else {
                hardwareAccelerated = false;
            }
            if (hardwareAccelerated && HardwareRenderer.isAvailable()) {
                boolean fakeHwAccelerated;
                if ((attrs.privateFlags & 1) != 0) {
                    fakeHwAccelerated = true;
                } else {
                    fakeHwAccelerated = false;
                }
                boolean forceHwAccelerated;
                if ((attrs.privateFlags & 2) != 0) {
                    forceHwAccelerated = true;
                } else {
                    forceHwAccelerated = false;
                }
                if (fakeHwAccelerated) {
                    this.mAttachInfo.mHardwareAccelerationRequested = true;
                } else if (!HardwareRenderer.sRendererDisabled || (HardwareRenderer.sSystemRendererDisabled && forceHwAccelerated)) {
                    boolean translucent;
                    if (this.mAttachInfo.mHardwareRenderer != null) {
                        this.mAttachInfo.mHardwareRenderer.destroy();
                    }
                    Rect insets = attrs.surfaceInsets;
                    boolean hasSurfaceInsets;
                    if (insets.left == 0 && insets.right == 0 && insets.top == 0 && insets.bottom == 0) {
                        hasSurfaceInsets = false;
                    } else {
                        hasSurfaceInsets = true;
                    }
                    if (attrs.format != -1 || hasSurfaceInsets) {
                        translucent = true;
                    } else {
                        translucent = false;
                    }
                    this.mAttachInfo.mHardwareRenderer = HardwareRenderer.create(this.mContext, translucent, sRendererDemoted);
                    if (this.mAttachInfo.mHardwareRenderer != null) {
                        this.mAttachInfo.mHardwareRenderer.setName(attrs.getTitle().toString());
                        AttachInfo attachInfo = this.mAttachInfo;
                        this.mAttachInfo.mHardwareAccelerationRequested = true;
                        attachInfo.mHardwareAccelerated = true;
                    }
                }
            }
        }
    }

    public View getView() {
        return this.mView;
    }

    final WindowLeaked getLocation() {
        return this.mLocation;
    }

    void setLayoutParams(LayoutParams attrs, boolean newView) {
        synchronized (this) {
            int oldInsetLeft = this.mWindowAttributes.surfaceInsets.left;
            int oldInsetTop = this.mWindowAttributes.surfaceInsets.top;
            int oldInsetRight = this.mWindowAttributes.surfaceInsets.right;
            int oldInsetBottom = this.mWindowAttributes.surfaceInsets.bottom;
            int oldSoftInputMode = this.mWindowAttributes.softInputMode;
            boolean oldHasManualSurfaceInsets = this.mWindowAttributes.hasManualSurfaceInsets;
            this.mClientWindowLayoutFlags = attrs.flags;
            int compatibleWindowFlag = this.mWindowAttributes.privateFlags & 128;
            attrs.systemUiVisibility = this.mWindowAttributes.systemUiVisibility;
            attrs.subtreeSystemUiVisibility = this.mWindowAttributes.subtreeSystemUiVisibility;
            this.mWindowAttributesChangesFlag = this.mWindowAttributes.copyFrom(attrs);
            if ((this.mWindowAttributesChangesFlag & 524288) != 0) {
                this.mAttachInfo.mRecomputeGlobalAttributes = true;
            }
            if (this.mWindowAttributes.packageName == null) {
                this.mWindowAttributes.packageName = this.mBasePackageName;
            }
            LayoutParams layoutParams = this.mWindowAttributes;
            layoutParams.privateFlags |= compatibleWindowFlag;
            this.mWindowAttributes.surfaceInsets.set(oldInsetLeft, oldInsetTop, oldInsetRight, oldInsetBottom);
            this.mWindowAttributes.hasManualSurfaceInsets = oldHasManualSurfaceInsets;
            applyKeepScreenOnFlag(this.mWindowAttributes);
            if (newView) {
                this.mSoftInputMode = attrs.softInputMode;
                requestLayout();
            }
            if ((attrs.softInputMode & 240) == 0) {
                this.mWindowAttributes.softInputMode = (this.mWindowAttributes.softInputMode & -241) | (oldSoftInputMode & 240);
            }
            this.mWindowAttributesChanged = true;
            scheduleTraversals();
        }
    }

    void handleAppVisibility(boolean visible) {
        if (this.mAppVisible != visible) {
            this.mAppVisible = visible;
            if ((this.mWindowAttributes.samsungFlags & 4096) != 0) {
                if (!this.mAppVisible) {
                    this.mWindowsExitAnimating = true;
                } else if (!this.mWindowsAnimating) {
                    this.mWindowsExitAnimating = false;
                }
            }
            scheduleTraversals();
            if (!this.mAppVisible) {
                WindowManagerGlobal.trimForeground();
            }
        }
    }

    void handleGetNewSurface() {
        this.mNewSurfaceNeeded = true;
        this.mFullRedrawNeeded = true;
        scheduleTraversals();
    }

    void pokeDrawLockIfNeeded() {
        int displayState = this.mAttachInfo.mDisplayState;
        if (this.mView == null || !this.mAdded || !this.mTraversalScheduled) {
            return;
        }
        if (displayState == 3 || displayState == 4) {
            try {
                this.mWindowSession.pokeDrawLock(this.mWindow);
            } catch (RemoteException e) {
            }
        }
    }

    public void requestFitSystemWindows() {
        checkThread();
        this.mApplyInsetsRequested = true;
        scheduleTraversals();
    }

    public void requestLayout() {
        if (this.mHandlingLayoutInLayoutRequest) {
            Log.i(TAG, "requestLayout is already in process");
            return;
        }
        checkThread();
        this.mLayoutRequested = true;
        scheduleTraversals();
    }

    public boolean isLayoutRequested() {
        return this.mLayoutRequested;
    }

    void invalidate() {
        this.mDirty.set(0, 0, this.mWidth, this.mHeight);
        if (!this.mWillDrawSoon) {
            scheduleTraversals();
        }
    }

    void invalidateWorld(View view) {
        view.invalidate();
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                invalidateWorld(parent.getChildAt(i));
            }
        }
    }

    public void invalidateChild(View child, Rect dirty) {
        invalidateChildInParent(null, dirty);
    }

    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        checkThread();
        if (dirty == null) {
            invalidate();
        } else if (!dirty.isEmpty() || this.mIsAnimating) {
            if (!(this.mCurScrollY == 0 && this.mTranslator == null)) {
                this.mTempRect.set(dirty);
                dirty = this.mTempRect;
                if (this.mCurScrollY != 0) {
                    dirty.offset(0, -this.mCurScrollY);
                }
                if (this.mTranslator != null) {
                    this.mTranslator.translateRectInAppWindowToScreen(dirty);
                }
                if (this.mAttachInfo.mScalingRequired) {
                    dirty.inset(-1, -1);
                }
            }
            invalidateRectOnScreen(dirty);
        }
        return null;
    }

    private void invalidateRectOnScreen(Rect dirty) {
        Rect localDirty = this.mDirty;
        if (!(localDirty.isEmpty() || localDirty.contains(dirty))) {
            this.mAttachInfo.mSetIgnoreDirtyState = true;
            this.mAttachInfo.mIgnoreDirtyState = true;
        }
        localDirty.union(dirty.left, dirty.top, dirty.right, dirty.bottom);
        float appScale = this.mAttachInfo.mApplicationScale;
        boolean intersected = localDirty.intersect(0, 0, (int) ((((float) this.mWidth) * appScale) + 0.5f), (int) ((((float) this.mHeight) * appScale) + 0.5f));
        if (!intersected) {
            localDirty.setEmpty();
        }
        if (!this.mWillDrawSoon) {
            if (intersected || this.mIsAnimating) {
                scheduleTraversals();
            }
        }
    }

    void setWindowStopped(boolean stopped) {
        if (this.mStopped != stopped) {
            this.mStopped = stopped;
            if (!this.mStopped) {
                scheduleTraversals();
            }
        }
    }

    public void setPausedForTransition(boolean paused) {
        this.mPausedForTransition = paused;
    }

    public boolean getStopped() {
        return this.mStopped;
    }

    public boolean getAppVisibility() {
        return this.mAppVisible;
    }

    public ViewParent getParent() {
        return null;
    }

    public boolean getChildVisibleRect(View child, Rect r, Point offset) {
        if (child == this.mView) {
            return r.intersect(0, 0, this.mWidth, this.mHeight);
        }
        throw new RuntimeException("child is not mine, honest!");
    }

    public void bringChildToFront(View child) {
    }

    int getHostVisibility() {
        int i = 8;
        try {
            if (this.mAppVisible) {
                i = this.mView.getVisibility();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return i;
    }

    void disposeResizeBuffer() {
        if (this.mResizeBuffer != null) {
            this.mResizeBuffer.destroy();
            this.mResizeBuffer = null;
        }
    }

    public void requestTransitionStart(LayoutTransition transition) {
        if (this.mPendingTransitions == null || !this.mPendingTransitions.contains(transition)) {
            if (this.mPendingTransitions == null) {
                this.mPendingTransitions = new ArrayList();
            }
            this.mPendingTransitions.add(transition);
        }
    }

    void notifyRendererOfFramePending() {
        if (this.mAttachInfo.mHardwareRenderer != null) {
            this.mAttachInfo.mHardwareRenderer.notifyFramePending();
        }
    }

    void scheduleTraversals() {
        if (!this.mTraversalScheduled) {
            this.mTraversalScheduled = true;
            this.mTraversalBarrier = this.mHandler.getLooper().getQueue().postSyncBarrier();
            this.mChoreographer.postCallback(2, this.mTraversalRunnable, null);
            if (!this.mUnbufferedInputDispatch) {
                scheduleConsumeBatchedInput();
            }
            notifyRendererOfFramePending();
            pokeDrawLockIfNeeded();
        }
    }

    void unscheduleTraversals() {
        if (this.mTraversalScheduled) {
            this.mTraversalScheduled = false;
            this.mHandler.getLooper().getQueue().removeSyncBarrier(this.mTraversalBarrier);
            this.mChoreographer.removeCallbacks(2, this.mTraversalRunnable, null);
        }
    }

    void doTraversal() {
        if (this.mTraversalScheduled) {
            this.mTraversalScheduled = false;
            this.mHandler.getLooper().getQueue().removeSyncBarrier(this.mTraversalBarrier);
            if (this.mProfile) {
                Debug.startMethodTracing("ViewAncestor");
            }
            performTraversals();
            if (this.mProfile) {
                Debug.stopMethodTracing();
                this.mProfile = false;
            }
        }
    }

    private void applyKeepScreenOnFlag(LayoutParams params) {
        if (this.mAttachInfo.mKeepScreenOn) {
            params.flags |= 128;
        } else {
            params.flags = (params.flags & -129) | (this.mClientWindowLayoutFlags & 128);
        }
    }

    private boolean collectViewAttributes() {
        if (!this.mAttachInfo.mRecomputeGlobalAttributes) {
            return false;
        }
        this.mAttachInfo.mRecomputeGlobalAttributes = false;
        boolean oldScreenOn = this.mAttachInfo.mKeepScreenOn;
        this.mAttachInfo.mKeepScreenOn = false;
        this.mAttachInfo.mSystemUiVisibility = 0;
        this.mAttachInfo.mHasSystemUiListeners = false;
        this.mView.dispatchCollectViewAttributes(this.mAttachInfo, 0);
        AttachInfo attachInfo = this.mAttachInfo;
        attachInfo.mSystemUiVisibility &= this.mAttachInfo.mDisabledSystemUiVisibility ^ -1;
        LayoutParams params = this.mWindowAttributes;
        attachInfo = this.mAttachInfo;
        attachInfo.mSystemUiVisibility |= getImpliedSystemUiVisibility(params);
        if (this.mAttachInfo.mKeepScreenOn == oldScreenOn && this.mAttachInfo.mSystemUiVisibility == params.subtreeSystemUiVisibility && this.mAttachInfo.mHasSystemUiListeners == params.hasSystemUiListeners) {
            return false;
        }
        applyKeepScreenOnFlag(params);
        params.subtreeSystemUiVisibility = this.mAttachInfo.mSystemUiVisibility;
        params.hasSystemUiListeners = this.mAttachInfo.mHasSystemUiListeners;
        this.mView.dispatchWindowSystemUiVisiblityChanged(this.mAttachInfo.mSystemUiVisibility);
        return true;
    }

    private int getImpliedSystemUiVisibility(LayoutParams params) {
        int vis = 0;
        if ((params.flags & 67108864) != 0) {
            vis = 0 | 1280;
        }
        if ((params.flags & 134217728) != 0) {
            return vis | 768;
        }
        return vis;
    }

    private boolean measureHierarchy(View host, LayoutParams lp, Resources res, int desiredWindowWidth, int desiredWindowHeight) {
        boolean goodMeasure = false;
        if (lp.width == -2) {
            DisplayMetrics packageMetrics = res.getDisplayMetrics();
            res.getValue(R.dimen.config_prefDialogWidth, this.mTmpValue, true);
            int baseSize = 0;
            if (this.mTmpValue.type == 5) {
                baseSize = (int) this.mTmpValue.getDimension(packageMetrics);
            }
            if (!(baseSize == 0 || desiredWindowWidth <= baseSize || lp.type == LayoutParams.TYPE_MINI_APP)) {
                int childWidthMeasureSpec = getRootMeasureSpec(baseSize, lp.width);
                int childHeightMeasureSpec = getRootMeasureSpec(desiredWindowHeight, lp.height);
                performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
                if ((host.getMeasuredWidthAndState() & 16777216) == 0) {
                    goodMeasure = true;
                } else {
                    performMeasure(getRootMeasureSpec((baseSize + desiredWindowWidth) / 2, lp.width), childHeightMeasureSpec);
                    if ((host.getMeasuredWidthAndState() & 16777216) == 0) {
                        goodMeasure = true;
                    }
                }
            }
        }
        if (goodMeasure) {
            return false;
        }
        performMeasure(getRootMeasureSpec(desiredWindowWidth, lp.width), getRootMeasureSpec(desiredWindowHeight, lp.height));
        if (this.mWidth == host.getMeasuredWidth() && this.mHeight == host.getMeasuredHeight()) {
            return false;
        }
        return true;
    }

    void transformMatrixToGlobal(Matrix m) {
        m.preTranslate((float) this.mAttachInfo.mWindowLeft, (float) this.mAttachInfo.mWindowTop);
    }

    void transformMatrixToLocal(Matrix m) {
        m.postTranslate((float) (-this.mAttachInfo.mWindowLeft), (float) (-this.mAttachInfo.mWindowTop));
    }

    WindowInsets getWindowInsets(boolean forceConstruct) {
        if (this.mLastWindowInsets == null || forceConstruct) {
            this.mDispatchContentInsets.set(this.mAttachInfo.mContentInsets);
            this.mDispatchStableInsets.set(this.mAttachInfo.mStableInsets);
            Rect contentInsets = this.mDispatchContentInsets;
            Rect stableInsets = this.mDispatchStableInsets;
            if (!(forceConstruct || (this.mPendingContentInsets.equals(contentInsets) && this.mPendingStableInsets.equals(stableInsets)))) {
                contentInsets = this.mPendingContentInsets;
                stableInsets = this.mPendingStableInsets;
            }
            Rect outsets = this.mAttachInfo.mOutsets;
            if (outsets.left > 0 || outsets.top > 0 || outsets.right > 0 || outsets.bottom > 0) {
                contentInsets = new Rect(contentInsets.left + outsets.left, contentInsets.top + outsets.top, contentInsets.right + outsets.right, contentInsets.bottom + outsets.bottom);
            }
            this.mLastWindowInsets = new WindowInsets(contentInsets, null, stableInsets, this.mContext.getResources().getConfiguration().isScreenRound());
        }
        return this.mLastWindowInsets;
    }

    void dispatchApplyInsets(View host) {
        host.dispatchApplyWindowInsets(getWindowInsets(true));
    }

    private void performTraversals() {
        View host = this.mView;
        if (host != null && this.mAdded) {
            Point size;
            int desiredWindowWidth;
            int desiredWindowHeight;
            DisplayMetrics packageMetrics;
            MultiWindowStyle multiWindowStyle;
            int i;
            this.mIsInTraversal = true;
            this.mWillDrawSoon = true;
            boolean windowSizeMayChange = false;
            boolean newSurface = false;
            boolean surfaceChanged = false;
            LayoutParams lp = this.mWindowAttributes;
            int viewVisibility = getHostVisibility();
            int reportViewVisibility = viewVisibility;
            if (!((lp.samsungFlags & 4096) == 0 || !this.mWindowsExitAnimating || this.mAppVisible)) {
                viewVisibility = 0;
            }
            boolean reportVisibilityChanged = this.mReportedViewVisibility != reportViewVisibility || this.mNewSurfaceNeeded;
            boolean viewVisibilityChanged = this.mViewVisibility != viewVisibility || this.mNewSurfaceNeeded;
            LayoutParams params = null;
            if (this.mWindowAttributesChanged) {
                this.mWindowAttributesChanged = false;
                surfaceChanged = true;
                params = lp;
            }
            if (this.mDisplayAdjustments.getCompatibilityInfo().supportsScreen() == this.mLastInCompatMode) {
                params = lp;
                this.mFullRedrawNeeded = true;
                this.mLayoutRequested = true;
                if (this.mLastInCompatMode) {
                    params.privateFlags &= -129;
                    this.mLastInCompatMode = false;
                } else {
                    params.privateFlags |= 128;
                    this.mLastInCompatMode = true;
                }
            }
            this.mWindowAttributesChangesFlag = 0;
            Rect frame = this.mWinFrame;
            if (this.mFirst) {
                this.mFullRedrawNeeded = true;
                this.mLayoutRequested = true;
                if (lp.type == LayoutParams.TYPE_STATUS_BAR_PANEL || lp.type == LayoutParams.TYPE_INPUT_METHOD) {
                    size = new Point();
                    this.mDisplay.getRealSize(size);
                    desiredWindowWidth = size.x;
                    desiredWindowHeight = size.y;
                } else {
                    packageMetrics = this.mView.getContext().getResources().getDisplayMetrics();
                    desiredWindowWidth = packageMetrics.widthPixels;
                    desiredWindowHeight = packageMetrics.heightPixels;
                    if (!(this.mContext == null || lp.type == 1003 || lp.type == 2)) {
                        multiWindowStyle = getMultiWindowStyle();
                        if (multiWindowStyle != null && multiWindowStyle.getType() == 1) {
                            MultiWindowFacade multiWindowFacade = (MultiWindowFacade) this.mContext.getSystemService("multiwindow_facade");
                            if (multiWindowFacade != null) {
                                Rect stackBounds = multiWindowFacade.getStackBound(this.mContext.getBaseActivityToken());
                                if (stackBounds != null) {
                                    desiredWindowWidth = stackBounds.width();
                                    desiredWindowHeight = stackBounds.height();
                                }
                            }
                        }
                    }
                }
                this.mAttachInfo.mUse32BitDrawingCache = true;
                this.mAttachInfo.mHasWindowFocus = false;
                this.mAttachInfo.mWindowVisibility = viewVisibility;
                this.mAttachInfo.mRecomputeGlobalAttributes = false;
                reportVisibilityChanged = false;
                viewVisibilityChanged = false;
                this.mLastSystemUiVisibility = this.mAttachInfo.mSystemUiVisibility;
                if (this.mViewLayoutDirectionInitial == 2) {
                    host.setLayoutDirection(this.mLastConfiguration.getLayoutDirection());
                }
                host.dispatchAttachedToWindow(this.mAttachInfo, 0);
                this.mAttachInfo.mTreeObserver.dispatchOnWindowAttachedChange(true);
                dispatchApplyInsets(host);
            } else {
                desiredWindowWidth = frame.width();
                desiredWindowHeight = frame.height();
                if (!(desiredWindowWidth == this.mWidth && desiredWindowHeight == this.mHeight)) {
                    this.mFullRedrawNeeded = true;
                    this.mLayoutRequested = true;
                    windowSizeMayChange = true;
                }
            }
            if (viewVisibilityChanged) {
                this.mAttachInfo.mWindowVisibility = viewVisibility;
                host.dispatchWindowVisibilityChanged(viewVisibility);
                if (viewVisibility != 0 || this.mNewSurfaceNeeded) {
                    destroyHardwareResources();
                }
                if (viewVisibility == 8) {
                    this.mHasHadWindowFocus = false;
                }
            }
            if (this.mAttachInfo.mWindowVisibility != 0) {
                host.clearAccessibilityFocus();
            }
            getRunQueue().executeActions(this.mAttachInfo.mHandler);
            boolean insetsChanged = false;
            if (!this.mLayoutRequested || (this.mStopped && !this.mReportNextDraw)) {
            }
            boolean sizeChangedInStopped = windowSizeMayChange && this.mStopped && this.mReportNextDraw;
            boolean imeStateChangedInStopped = this.mStopped && this.mLastWasImTarget && this.mFullRedrawNeeded;
            boolean forceRelayout = (this.mOrientationChanged || sizeChangedInStopped) && reportViewVisibility == 0 && (this.mWindowAttributes.type == 2 || this.mWindowAttributes.type == 1);
            boolean layoutRequested = this.mLayoutRequested && (!this.mStopped || forceRelayout);
            this.mOrientationChanged = false;
            if (layoutRequested) {
                Resources res = this.mView.getContext().getResources();
                if (this.mFirst) {
                    this.mAttachInfo.mInTouchMode = !this.mAddedTouchMode;
                    ensureTouchModeLocally(this.mAddedTouchMode);
                } else {
                    if (!this.mPendingOverscanInsets.equals(this.mAttachInfo.mOverscanInsets)) {
                        insetsChanged = true;
                    }
                    if (!this.mPendingContentInsets.equals(this.mAttachInfo.mContentInsets)) {
                        insetsChanged = true;
                    }
                    if (!this.mPendingStableInsets.equals(this.mAttachInfo.mStableInsets)) {
                        if (this.mWindowAttributes.type != 2 || (this.mWindowAttributes.flags & 2) == 0 || this.mView == null || this.mView.getZ() <= 0.0f) {
                            insetsChanged = true;
                        } else {
                            Log.d(TAG, "Ignore stableInsets changed, SI=" + this.mAttachInfo.mStableInsets.toShortString() + " PSI=" + this.mPendingStableInsets.toShortString());
                        }
                    }
                    if (!this.mPendingVisibleInsets.equals(this.mAttachInfo.mVisibleInsets)) {
                        this.mAttachInfo.mVisibleInsets.set(this.mPendingVisibleInsets);
                    }
                    if (!this.mPendingOutsets.equals(this.mAttachInfo.mOutsets)) {
                        insetsChanged = true;
                    }
                    if (lp.width == -2 || lp.height == -2) {
                        windowSizeMayChange = true;
                        if (lp.type == LayoutParams.TYPE_STATUS_BAR_PANEL || lp.type == LayoutParams.TYPE_INPUT_METHOD) {
                            size = new Point();
                            this.mDisplay.getRealSize(size);
                            desiredWindowWidth = size.x;
                            desiredWindowHeight = size.y;
                        } else {
                            packageMetrics = res.getDisplayMetrics();
                            desiredWindowWidth = packageMetrics.widthPixels;
                            desiredWindowHeight = packageMetrics.heightPixels;
                        }
                    }
                }
                windowSizeMayChange |= measureHierarchy(host, lp, res, desiredWindowWidth, desiredWindowHeight);
            }
            if (collectViewAttributes()) {
                params = lp;
            }
            if (this.mAttachInfo.mForceReportNewAttributes) {
                this.mAttachInfo.mForceReportNewAttributes = false;
                params = lp;
            }
            if (this.mFirst || this.mAttachInfo.mViewVisibilityChanged) {
                this.mAttachInfo.mViewVisibilityChanged = false;
                int resizeMode = this.mSoftInputMode & 240;
                if (resizeMode == 0) {
                    int N = this.mAttachInfo.mScrollContainers.size();
                    for (i = 0; i < N; i++) {
                        if (((View) this.mAttachInfo.mScrollContainers.get(i)).isShown()) {
                            resizeMode = 16;
                        }
                    }
                    if (resizeMode == 0) {
                        resizeMode = 32;
                    }
                    if ((lp.softInputMode & 240) != resizeMode) {
                        lp.softInputMode = (lp.softInputMode & -241) | resizeMode;
                        params = lp;
                    }
                }
            }
            if (params != null) {
                if (!((host.mPrivateFlags & 512) == 0 || PixelFormat.formatHasAlpha(params.format))) {
                    params.format = -3;
                }
                this.mAttachInfo.mOverscanRequested = (params.flags & 33554432) != 0;
            }
            if (this.mApplyInsetsRequested) {
                this.mApplyInsetsRequested = false;
                this.mLastOverscanRequested = this.mAttachInfo.mOverscanRequested;
                dispatchApplyInsets(host);
                if (this.mLayoutRequested) {
                    windowSizeMayChange |= measureHierarchy(host, lp, this.mView.getContext().getResources(), desiredWindowWidth, desiredWindowHeight);
                }
            }
            if (layoutRequested) {
                this.mLayoutRequested = false;
            }
            boolean windowShouldResize = layoutRequested && windowSizeMayChange && !(this.mWidth == host.getMeasuredWidth() && this.mHeight == host.getMeasuredHeight() && ((lp.width != -2 || frame.width() >= desiredWindowWidth || frame.width() == this.mWidth) && (lp.height != -2 || frame.height() >= desiredWindowHeight || frame.height() == this.mHeight)));
            boolean maximizing = false;
            if (this.mContext != null) {
                multiWindowStyle = getMultiWindowStyle();
                if (!(multiWindowStyle == null || this.mLastPerformedMultiWindowStyle.equals(multiWindowStyle))) {
                    if (multiWindowStyle.getType() != this.mLastPerformedMultiWindowStyle.getType()) {
                        windowShouldResize = true;
                    } else if (!multiWindowStyle.isEnabled(4) && this.mLastPerformedMultiWindowStyle.isEnabled(4)) {
                        windowShouldResize = true;
                        maximizing = true;
                    }
                    this.mLastPerformedMultiWindowStyle.setTo(multiWindowStyle);
                }
            }
            boolean computesInternalInsets = this.mAttachInfo.mTreeObserver.hasComputeInternalInsetsListeners() || this.mAttachInfo.mHasNonEmptyGivenInternalInsets;
            boolean insetsPending = false;
            int relayoutResult = 0;
            if (this.mFirst || windowShouldResize || insetsChanged || reportVisibilityChanged || params != null || forceRelayout || imeStateChangedInStopped || this.mNewScaleFactorNeeded) {
                if (reportViewVisibility == 0) {
                    insetsPending = computesInternalInsets && (this.mFirst || reportVisibilityChanged);
                }
                if (this.mSurfaceHolder != null) {
                    this.mSurfaceHolder.mSurfaceLock.lock();
                    this.mDrawingAllowed = true;
                }
                boolean hwInitialized = false;
                boolean z = false;
                boolean hadSurface = this.mSurface.isValid();
                try {
                    if (!this.mResizeAnimating) {
                        if (this.mAttachInfo.mHardwareRenderer != null) {
                            if (this.mAttachInfo.mHardwareRenderer.pauseSurface(this.mSurface)) {
                                this.mDirty.set(0, 0, this.mWidth, this.mHeight);
                            }
                            this.mChoreographer.mFrameInfo.addFlags(1);
                        }
                        relayoutResult = relayoutWindow(params, viewVisibility, insetsPending, reportViewVisibility);
                        if (this.mPendingConfiguration.seq != 0) {
                            updateConfiguration(this.mPendingConfiguration, !this.mFirst);
                            this.mPendingConfiguration.seq = 0;
                        }
                    }
                    int surfaceGenerationId = this.mSurface.getGenerationId();
                    boolean overscanInsetsChanged = !this.mPendingOverscanInsets.equals(this.mAttachInfo.mOverscanInsets);
                    z = !this.mPendingContentInsets.equals(this.mAttachInfo.mContentInsets);
                    boolean visibleInsetsChanged = !this.mPendingVisibleInsets.equals(this.mAttachInfo.mVisibleInsets);
                    boolean stableInsetsChanged = !this.mPendingStableInsets.equals(this.mAttachInfo.mStableInsets);
                    boolean outsetsChanged = !this.mPendingOutsets.equals(this.mAttachInfo.mOutsets);
                    if (z) {
                        if (!(this.mWidth <= 0 || this.mHeight <= 0 || lp == null || ((lp.systemUiVisibility | lp.subtreeSystemUiVisibility) & View.SYSTEM_UI_LAYOUT_FLAGS) != 0 || this.mSurface == null || !this.mSurface.isValid() || this.mAttachInfo.mTurnOffWindowResizeAnim || this.mAttachInfo.mHardwareRenderer == null || !this.mAttachInfo.mHardwareRenderer.isEnabled() || lp == null || PixelFormat.formatHasAlpha(lp.format) || this.mBlockResizeBuffer)) {
                            disposeResizeBuffer();
                        }
                        this.mAttachInfo.mContentInsets.set(this.mPendingContentInsets);
                    }
                    if (overscanInsetsChanged) {
                        this.mAttachInfo.mOverscanInsets.set(this.mPendingOverscanInsets);
                        z = true;
                    }
                    if (stableInsetsChanged) {
                        this.mAttachInfo.mStableInsets.set(this.mPendingStableInsets);
                        z = true;
                    }
                    if (z || this.mLastSystemUiVisibility != this.mAttachInfo.mSystemUiVisibility || this.mApplyInsetsRequested || this.mLastOverscanRequested != this.mAttachInfo.mOverscanRequested || outsetsChanged) {
                        this.mLastSystemUiVisibility = this.mAttachInfo.mSystemUiVisibility;
                        this.mLastOverscanRequested = this.mAttachInfo.mOverscanRequested;
                        this.mAttachInfo.mOutsets.set(this.mPendingOutsets);
                        this.mApplyInsetsRequested = false;
                        dispatchApplyInsets(host);
                    }
                    if (visibleInsetsChanged) {
                        this.mAttachInfo.mVisibleInsets.set(this.mPendingVisibleInsets);
                    }
                    SurfaceHolder.Callback[] callbacks;
                    HardwareRenderer hardwareRenderer;
                    int childWidthMeasureSpec;
                    int childHeightMeasureSpec;
                    int width;
                    int height;
                    boolean measureAgain;
                    if (hadSurface) {
                        if (!this.mSurface.isValid()) {
                            if (this.mLastScrolledFocus != null) {
                                this.mLastScrolledFocus.clear();
                            }
                            this.mCurScrollY = 0;
                            this.mScrollY = 0;
                            if (this.mView instanceof RootViewSurfaceTaker) {
                                ((RootViewSurfaceTaker) this.mView).onRootViewScrollYChanged(this.mCurScrollY);
                            }
                            if (this.mScroller != null) {
                                this.mScroller.abortAnimation();
                            }
                            disposeResizeBuffer();
                            if (this.mAttachInfo.mHardwareRenderer != null && this.mAttachInfo.mHardwareRenderer.isEnabled()) {
                                this.mAttachInfo.mHardwareRenderer.destroy();
                            }
                        } else if (!(surfaceGenerationId == this.mSurface.getGenerationId() || this.mSurfaceHolder != null || this.mAttachInfo.mHardwareRenderer == null)) {
                            this.mFullRedrawNeeded = true;
                            try {
                                this.mAttachInfo.mHardwareRenderer.updateSurface(this.mSurface);
                            } catch (OutOfResourcesException e) {
                                handleOutOfResourcesException(e);
                                return;
                            }
                        }
                        this.mAttachInfo.mWindowLeft = frame.left;
                        this.mAttachInfo.mWindowTop = frame.top;
                        this.mWidth = frame.width();
                        this.mHeight = frame.height();
                        if (this.mSurfaceHolder != null) {
                            if (this.mSurface.isValid()) {
                                this.mSurfaceHolder.mSurface = this.mSurface;
                            }
                            this.mSurfaceHolder.setSurfaceFrameSize(this.mWidth, this.mHeight);
                            this.mSurfaceHolder.mSurfaceLock.unlock();
                            if (this.mSurface.isValid()) {
                                if (!hadSurface) {
                                    this.mSurfaceHolder.ungetCallbacks();
                                    this.mIsCreating = true;
                                    this.mSurfaceHolderCallback.surfaceCreated(this.mSurfaceHolder);
                                    Log.d("ViewSystem", "ViewRootImpl >> surfaceCreated");
                                    callbacks = this.mSurfaceHolder.getCallbacks();
                                    if (callbacks != null) {
                                        for (SurfaceHolder.Callback c : callbacks) {
                                            c.surfaceCreated(this.mSurfaceHolder);
                                        }
                                    }
                                    surfaceChanged = true;
                                }
                                if (surfaceChanged) {
                                    this.mSurfaceHolderCallback.surfaceChanged(this.mSurfaceHolder, lp.format, this.mWidth, this.mHeight);
                                    Log.d("ViewSystem", String.format("ViewRootImpl >> surfaceChanged W=%d, H=%d)", new Object[]{Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight)}));
                                    callbacks = this.mSurfaceHolder.getCallbacks();
                                    if (callbacks != null) {
                                        for (SurfaceHolder.Callback c2 : callbacks) {
                                            c2.surfaceChanged(this.mSurfaceHolder, lp.format, this.mWidth, this.mHeight);
                                        }
                                    }
                                }
                                this.mIsCreating = false;
                            } else if (hadSurface) {
                                this.mSurfaceHolder.ungetCallbacks();
                                callbacks = this.mSurfaceHolder.getCallbacks();
                                this.mSurfaceHolderCallback.surfaceDestroyed(this.mSurfaceHolder);
                                Log.d("ViewSystem", "ViewRootImpl >> surfaceDestroyed");
                                if (callbacks != null) {
                                    for (SurfaceHolder.Callback c22 : callbacks) {
                                        c22.surfaceDestroyed(this.mSurfaceHolder);
                                    }
                                }
                                this.mSurfaceHolder.mSurfaceLock.lock();
                                this.mSurfaceHolder.mSurface = new Surface();
                            }
                        }
                        hardwareRenderer = this.mAttachInfo.mHardwareRenderer;
                        hardwareRenderer.setup(this.mWidth, this.mHeight, this.mAttachInfo, this.mWindowAttributes.surfaceInsets);
                        if (!hwInitialized) {
                            hardwareRenderer.invalidate(this.mSurface);
                            this.mFullRedrawNeeded = true;
                        }
                        if ((relayoutResult & 1) == 0) {
                        }
                        childWidthMeasureSpec = getRootMeasureSpec(this.mWidth, lp.width);
                        childHeightMeasureSpec = getRootMeasureSpec(this.mHeight, lp.height);
                        performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
                        width = host.getMeasuredWidth();
                        height = host.getMeasuredHeight();
                        measureAgain = false;
                        if (lp.horizontalWeight > 0.0f) {
                            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width + ((int) (((float) (this.mWidth - width)) * lp.horizontalWeight)), 1073741824);
                            measureAgain = true;
                        }
                        if (lp.verticalWeight > 0.0f) {
                            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height + ((int) (((float) (this.mHeight - height)) * lp.verticalWeight)), 1073741824);
                            measureAgain = true;
                        }
                        if (measureAgain) {
                            performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
                        }
                        layoutRequested = true;
                    } else {
                        if (this.mSurface.isValid()) {
                            newSurface = true;
                            this.mFullRedrawNeeded = true;
                            this.mPreviousTransparentRegion.setEmpty();
                            if (sBufferCount != -1) {
                                this.mSurface.setBufferCount(sBufferCount);
                            }
                            if (sDTSFactor != 1.0d) {
                                this.mSurface.setDTSFactor(sDTSFactor);
                            }
                            if (this.mAttachInfo.mHardwareRenderer != null) {
                                try {
                                    hwInitialized = this.mAttachInfo.mHardwareRenderer.initialize(this.mSurface, sIsDcsEnabledApp, sDcsFormat, sDssFactor);
                                    if (hwInitialized && (host.mPrivateFlags & 512) == 0 && !sRendererDemoted) {
                                        this.mSurface.allocateBuffers();
                                    }
                                } catch (OutOfResourcesException e2) {
                                    handleOutOfResourcesException(e2);
                                    return;
                                }
                            }
                        }
                        this.mAttachInfo.mWindowLeft = frame.left;
                        this.mAttachInfo.mWindowTop = frame.top;
                        if (!(this.mWidth == frame.width() && this.mHeight == frame.height())) {
                            this.mWidth = frame.width();
                            this.mHeight = frame.height();
                        }
                        if (this.mSurfaceHolder != null) {
                            if (this.mSurface.isValid()) {
                                this.mSurfaceHolder.mSurface = this.mSurface;
                            }
                            this.mSurfaceHolder.setSurfaceFrameSize(this.mWidth, this.mHeight);
                            this.mSurfaceHolder.mSurfaceLock.unlock();
                            if (this.mSurface.isValid()) {
                                if (hadSurface) {
                                    this.mSurfaceHolder.ungetCallbacks();
                                    this.mIsCreating = true;
                                    this.mSurfaceHolderCallback.surfaceCreated(this.mSurfaceHolder);
                                    Log.d("ViewSystem", "ViewRootImpl >> surfaceCreated");
                                    callbacks = this.mSurfaceHolder.getCallbacks();
                                    if (callbacks != null) {
                                        while (i$ < len$) {
                                            c22.surfaceCreated(this.mSurfaceHolder);
                                        }
                                    }
                                    surfaceChanged = true;
                                }
                                if (surfaceChanged) {
                                    this.mSurfaceHolderCallback.surfaceChanged(this.mSurfaceHolder, lp.format, this.mWidth, this.mHeight);
                                    Log.d("ViewSystem", String.format("ViewRootImpl >> surfaceChanged W=%d, H=%d)", new Object[]{Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight)}));
                                    callbacks = this.mSurfaceHolder.getCallbacks();
                                    if (callbacks != null) {
                                        while (i$ < len$) {
                                            c22.surfaceChanged(this.mSurfaceHolder, lp.format, this.mWidth, this.mHeight);
                                        }
                                    }
                                }
                                this.mIsCreating = false;
                            } else if (hadSurface) {
                                this.mSurfaceHolder.ungetCallbacks();
                                callbacks = this.mSurfaceHolder.getCallbacks();
                                this.mSurfaceHolderCallback.surfaceDestroyed(this.mSurfaceHolder);
                                Log.d("ViewSystem", "ViewRootImpl >> surfaceDestroyed");
                                if (callbacks != null) {
                                    while (i$ < len$) {
                                        c22.surfaceDestroyed(this.mSurfaceHolder);
                                    }
                                }
                                this.mSurfaceHolder.mSurfaceLock.lock();
                                try {
                                    this.mSurfaceHolder.mSurface = new Surface();
                                } finally {
                                    this.mSurfaceHolder.mSurfaceLock.unlock();
                                }
                            }
                        }
                        hardwareRenderer = this.mAttachInfo.mHardwareRenderer;
                        if (hardwareRenderer != null && hardwareRenderer.isEnabled() && (maximizing || hwInitialized || this.mWidth != hardwareRenderer.getWidth() || this.mHeight != hardwareRenderer.getHeight())) {
                            hardwareRenderer.setup(this.mWidth, this.mHeight, this.mAttachInfo, this.mWindowAttributes.surfaceInsets);
                            if (hwInitialized) {
                                hardwareRenderer.invalidate(this.mSurface);
                                this.mFullRedrawNeeded = true;
                            }
                        }
                        if (!this.mStopped || this.mReportNextDraw || forceRelayout) {
                            if (ensureTouchModeLocally((relayoutResult & 1) == 0) || this.mWidth != host.getMeasuredWidth() || this.mHeight != host.getMeasuredHeight() || contentInsetsChanged || this.mNewScaleFactorNeeded) {
                                childWidthMeasureSpec = getRootMeasureSpec(this.mWidth, lp.width);
                                childHeightMeasureSpec = getRootMeasureSpec(this.mHeight, lp.height);
                                performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
                                width = host.getMeasuredWidth();
                                height = host.getMeasuredHeight();
                                measureAgain = false;
                                if (lp.horizontalWeight > 0.0f) {
                                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width + ((int) (((float) (this.mWidth - width)) * lp.horizontalWeight)), 1073741824);
                                    measureAgain = true;
                                }
                                if (lp.verticalWeight > 0.0f) {
                                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height + ((int) (((float) (this.mHeight - height)) * lp.verticalWeight)), 1073741824);
                                    measureAgain = true;
                                }
                                if (measureAgain) {
                                    performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
                                }
                                layoutRequested = true;
                            }
                        }
                    }
                } catch (RemoteException e3) {
                }
            } else {
                boolean windowMoved = (this.mAttachInfo.mWindowLeft == frame.left && this.mAttachInfo.mWindowTop == frame.top) ? false : true;
                if (windowMoved) {
                    if (this.mTranslator != null) {
                        this.mTranslator.translateRectInScreenToAppWinFrame(frame);
                    }
                    this.mAttachInfo.mWindowLeft = frame.left;
                    this.mAttachInfo.mWindowTop = frame.top;
                    if (this.mAttachInfo.mHardwareRenderer != null) {
                        this.mAttachInfo.mHardwareRenderer.setLightCenter(this.mAttachInfo);
                    }
                }
            }
            boolean didLayout = layoutRequested && (!this.mStopped || this.mReportNextDraw || forceRelayout);
            boolean triggerGlobalLayoutListener = didLayout || this.mAttachInfo.mRecomputeGlobalAttributes;
            if (didLayout) {
                performLayout(lp, desiredWindowWidth, desiredWindowHeight);
                if ((host.mPrivateFlags & 512) != 0) {
                    host.getLocationInWindow(this.mTmpLocation);
                    this.mTransparentRegion.set(this.mTmpLocation[0], this.mTmpLocation[1], (this.mTmpLocation[0] + host.mRight) - host.mLeft, (this.mTmpLocation[1] + host.mBottom) - host.mTop);
                    host.gatherTransparentRegion(this.mTransparentRegion);
                    if (this.mWindowAttributes.surfaceInsets.left > 0 || this.mWindowAttributes.surfaceInsets.top > 0) {
                        this.mTransparentRegion.translate(this.mWindowAttributes.surfaceInsets.left, this.mWindowAttributes.surfaceInsets.top);
                    }
                    if (this.mTranslator != null) {
                        this.mTranslator.translateRegionInWindowToScreen(this.mTransparentRegion);
                    }
                    if (!this.mTransparentRegion.equals(this.mPreviousTransparentRegion)) {
                        this.mPreviousTransparentRegion.set(this.mTransparentRegion);
                        this.mFullRedrawNeeded = true;
                        try {
                            this.mWindowSession.setTransparentRegion(this.mWindow, this.mTransparentRegion);
                        } catch (RemoteException e4) {
                        }
                    }
                }
            }
            if (triggerGlobalLayoutListener) {
                this.mAttachInfo.mRecomputeGlobalAttributes = false;
                this.mAttachInfo.mTreeObserver.dispatchOnGlobalLayout();
            }
            if (computesInternalInsets) {
                InternalInsetsInfo insets = this.mAttachInfo.mGivenInternalInsets;
                insets.reset();
                this.mAttachInfo.mTreeObserver.dispatchOnComputeInternalInsets(insets);
                this.mAttachInfo.mHasNonEmptyGivenInternalInsets = !insets.isEmpty();
                if (insetsPending || !this.mLastGivenInsets.equals(insets)) {
                    Rect contentInsets;
                    Rect visibleInsets;
                    Region touchableRegion;
                    this.mLastGivenInsets.set(insets);
                    if (this.mTranslator != null) {
                        contentInsets = this.mTranslator.getTranslatedContentInsets(insets.contentInsets);
                        visibleInsets = this.mTranslator.getTranslatedVisibleInsets(insets.visibleInsets);
                        touchableRegion = this.mTranslator.getTranslatedTouchableArea(insets.touchableRegion);
                    } else {
                        contentInsets = insets.contentInsets;
                        visibleInsets = insets.visibleInsets;
                        touchableRegion = insets.touchableRegion;
                    }
                    if (!this.mResizeAnimating) {
                        try {
                            this.mWindowSession.setInsets(this.mWindow, insets.mTouchableInsets, contentInsets, visibleInsets, touchableRegion);
                        } catch (RemoteException e5) {
                        }
                    }
                }
            }
            boolean skipDraw = false;
            if (this.mFirst) {
                if (!(this.mView == null || this.mView.hasFocus())) {
                    this.mView.requestFocus(2);
                }
            } else if (this.mWindowsAnimating) {
                if (!(this.mRemainingFrameCount > 0 || this.mTwDrawDuringWindowsAnimating || this.mResizeAnimating)) {
                    skipDraw = true;
                }
                this.mRemainingFrameCount--;
            }
            this.mFirst = false;
            this.mWillDrawSoon = false;
            this.mNewSurfaceNeeded = false;
            this.mViewVisibility = viewVisibility;
            this.mReportedViewVisibility = reportViewVisibility;
            this.mNewScaleFactorNeeded = false;
            if (this.mAttachInfo.mHasWindowFocus && !isInLocalFocusMode()) {
                boolean imTarget = LayoutParams.mayUseInputMethod(this.mWindowAttributes.flags);
                if (imTarget != this.mLastWasImTarget) {
                    this.mLastWasImTarget = imTarget;
                    InputMethodManager imm = InputMethodManager.peekInstance();
                    if (imm != null && imTarget) {
                        imm.onPreWindowFocus(this.mView, true);
                        imm.onPostWindowFocus(this.mView, this.mView.findFocus(), this.mWindowAttributes.softInputMode, !this.mHasHadWindowFocus, this.mWindowAttributes.flags);
                    }
                }
            }
            if ((relayoutResult & 2) != 0) {
                this.mReportNextDraw = true;
            }
            boolean cancelDraw = this.mAttachInfo.mTreeObserver.dispatchOnPreDraw() || viewVisibility != 0;
            if (cancelDraw || newSurface) {
                if (viewVisibility == 0) {
                    scheduleTraversals();
                } else if (this.mPendingTransitions != null && this.mPendingTransitions.size() > 0) {
                    for (i = 0; i < this.mPendingTransitions.size(); i++) {
                        ((LayoutTransition) this.mPendingTransitions.get(i)).endChangingAnimations();
                    }
                    this.mPendingTransitions.clear();
                }
            } else if (!skipDraw || this.mReportNextDraw) {
                if (this.mPendingTransitions != null && this.mPendingTransitions.size() > 0) {
                    for (i = 0; i < this.mPendingTransitions.size(); i++) {
                        ((LayoutTransition) this.mPendingTransitions.get(i)).startChangingAnimations();
                    }
                    this.mPendingTransitions.clear();
                }
                performDraw();
            }
            this.mIsInTraversal = false;
        }
    }

    private void handleOutOfResourcesException(OutOfResourcesException e) {
        Log.e(TAG, "OutOfResourcesException initializing HW surface", e);
        try {
            if (!(this.mWindowSession.outOfMemory(this.mWindow) || Process.myUid() == 1000)) {
                Slog.w(TAG, "No processes killed for memory; killing self");
                Debug.saveDumpstate("-k -t -z -d -o /data/log/dumpstate_surfaceoom");
                Process.killProcess(Process.myPid());
            }
        } catch (RemoteException e2) {
        }
        this.mLayoutRequested = true;
    }

    private void performMeasure(int childWidthMeasureSpec, int childHeightMeasureSpec) {
        Trace.traceBegin(8, "measure");
        if (this.mContext != null && ((this.mWindowAttributes.type == 1 || this.mWindowAttributes.type == 2) && (this.mWindowAttributes.width > 0 || this.mWindowAttributes.height > 0))) {
            MultiWindowStyle multiWindowStyle = getMultiWindowStyle();
            this.mLastMeasuredMultiWindowStyle = multiWindowStyle;
            if (multiWindowStyle.getType() == 1) {
                int widthSpecMode = MeasureSpec.getMode(childWidthMeasureSpec);
                int widthSpecSize = MeasureSpec.getSize(childWidthMeasureSpec);
                int heightSpecMode = MeasureSpec.getMode(childHeightMeasureSpec);
                int heightSpecSize = MeasureSpec.getSize(childHeightMeasureSpec);
                MultiWindowFacade multiWindowFacade = (MultiWindowFacade) this.mContext.getSystemService("multiwindow_facade");
                if (multiWindowFacade != null) {
                    Rect currStackBound = multiWindowFacade.getStackBound(this.mContext.getBaseActivityToken());
                    if (currStackBound != null) {
                        if (this.mWindowAttributes.width > 0 && widthSpecSize > currStackBound.width()) {
                            childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(currStackBound.width(), 1073741824);
                        }
                        if (this.mWindowAttributes.height > 0 && heightSpecSize > currStackBound.height()) {
                            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(currStackBound.height(), 1073741824);
                        }
                    }
                }
            }
        }
        try {
            this.mView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        } finally {
            Trace.traceEnd(8);
        }
    }

    boolean isInLayout() {
        return this.mInLayout;
    }

    boolean requestLayoutDuringLayout(View view) {
        if (view.mParent == null || view.mAttachInfo == null) {
            return true;
        }
        if (!this.mLayoutRequesters.contains(view)) {
            this.mLayoutRequesters.add(view);
        }
        if (!this.mHandlingLayoutInLayoutRequest) {
            return true;
        }
        Log.i(TAG, "requestLayoutDuringLayout is already in process");
        return false;
    }

    private void performLayout(LayoutParams lp, int desiredWindowWidth, int desiredWindowHeight) {
        this.mLayoutRequested = false;
        this.mScrollMayChange = true;
        this.mInLayout = true;
        View host = this.mView;
        Trace.traceBegin(8, "layout");
        try {
            host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
            this.mInLayout = false;
            if (this.mLayoutRequesters.size() > 0) {
                ArrayList<View> validLayoutRequesters = getValidLayoutRequesters(this.mLayoutRequesters, false);
                if (validLayoutRequesters != null) {
                    this.mHandlingLayoutInLayoutRequest = true;
                    int numValidRequests = validLayoutRequesters.size();
                    for (int i = 0; i < numValidRequests; i++) {
                        View view = (View) validLayoutRequesters.get(i);
                        Log.w("View", "requestLayout() improperly called by " + view + " during layout: running second layout pass");
                        view.requestLayout();
                    }
                    measureHierarchy(host, lp, this.mView.getContext().getResources(), desiredWindowWidth, desiredWindowHeight);
                    this.mInLayout = true;
                    host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
                    this.mHandlingLayoutInLayoutRequest = false;
                    validLayoutRequesters = getValidLayoutRequesters(this.mLayoutRequesters, true);
                    if (validLayoutRequesters != null) {
                        final ArrayList<View> finalRequesters = validLayoutRequesters;
                        getRunQueue().post(new Runnable() {
                            public void run() {
                                int numValidRequests = finalRequesters.size();
                                for (int i = 0; i < numValidRequests; i++) {
                                    View view = (View) finalRequesters.get(i);
                                    Log.w("View", "requestLayout() improperly called by " + view + " during second layout pass: posting in next frame");
                                    view.requestLayout();
                                }
                            }
                        });
                    }
                }
            }
            Trace.traceEnd(8);
            this.mInLayout = false;
        } catch (Throwable th) {
            Trace.traceEnd(8);
        }
    }

    private ArrayList<View> getValidLayoutRequesters(ArrayList<View> layoutRequesters, boolean secondLayoutRequests) {
        int i;
        int numViewsRequestingLayout = layoutRequesters.size();
        ArrayList<View> validLayoutRequesters = null;
        for (i = 0; i < numViewsRequestingLayout; i++) {
            View view = (View) layoutRequesters.get(i);
            if (!(view == null || view.mAttachInfo == null || view.mParent == null || (!secondLayoutRequests && (view.mPrivateFlags & 4096) != 4096))) {
                boolean gone = false;
                View parent = view;
                while (parent != null) {
                    if ((parent.mViewFlags & 12) == 8) {
                        gone = true;
                        break;
                    } else if (parent.mParent instanceof View) {
                        parent = parent.mParent;
                    } else {
                        parent = null;
                    }
                }
                if (!gone) {
                    if (validLayoutRequesters == null) {
                        validLayoutRequesters = new ArrayList();
                    }
                    validLayoutRequesters.add(view);
                }
            }
        }
        if (!secondLayoutRequests) {
            for (i = 0; i < numViewsRequestingLayout; i++) {
                view = (View) layoutRequesters.get(i);
                while (view != null && (view.mPrivateFlags & 4096) != 0) {
                    view.mPrivateFlags &= -4097;
                    if (view.mParent instanceof View) {
                        view = view.mParent;
                    } else {
                        view = null;
                    }
                }
            }
        }
        layoutRequesters.clear();
        return validLayoutRequesters;
    }

    public void requestTransparentRegion(View child) {
        checkThread();
        if (this.mView == child) {
            View view = this.mView;
            view.mPrivateFlags |= 512;
            this.mWindowAttributesChanged = true;
            this.mWindowAttributesChangesFlag = 0;
            requestLayout();
        }
    }

    private static int getRootMeasureSpec(int windowSize, int rootDimension) {
        switch (rootDimension) {
            case -2:
                return MeasureSpec.makeMeasureSpec(windowSize, Integer.MIN_VALUE);
            case -1:
                return MeasureSpec.makeMeasureSpec(windowSize, 1073741824);
            default:
                return MeasureSpec.makeMeasureSpec(rootDimension, 1073741824);
        }
    }

    public void onHardwarePreDraw(DisplayListCanvas canvas) {
        canvas.translate((float) (-this.mHardwareXOffset), (float) (-this.mHardwareYOffset));
    }

    public void onHardwarePostDraw(DisplayListCanvas canvas) {
        if (this.mResizeBuffer != null) {
            this.mResizePaint.setAlpha(this.mResizeAlpha);
            canvas.drawHardwareLayer(this.mResizeBuffer, (float) this.mHardwareXOffset, (float) this.mHardwareYOffset, this.mResizePaint);
        }
        drawAccessibilityFocusedDrawableIfNeeded(canvas);
    }

    void outputDisplayList(View view) {
        view.mRenderNode.output();
    }

    private void profileRendering(boolean enabled) {
        if (this.mProfileRendering) {
            this.mRenderProfilingEnabled = enabled;
            if (this.mRenderProfiler != null) {
                this.mChoreographer.removeFrameCallback(this.mRenderProfiler);
            }
            if (this.mRenderProfilingEnabled) {
                if (this.mRenderProfiler == null) {
                    this.mRenderProfiler = new FrameCallback() {
                        public void doFrame(long frameTimeNanos) {
                            ViewRootImpl.this.mDirty.set(0, 0, ViewRootImpl.this.mWidth, ViewRootImpl.this.mHeight);
                            ViewRootImpl.this.scheduleTraversals();
                            if (ViewRootImpl.this.mRenderProfilingEnabled) {
                                ViewRootImpl.this.mChoreographer.postFrameCallback(ViewRootImpl.this.mRenderProfiler);
                            }
                        }
                    };
                }
                this.mChoreographer.postFrameCallback(this.mRenderProfiler);
                return;
            }
            this.mRenderProfiler = null;
        }
    }

    private void trackFPS() {
        long nowTime = System.currentTimeMillis();
        if (this.mFpsStartTime < 0) {
            this.mFpsPrevTime = nowTime;
            this.mFpsStartTime = nowTime;
            this.mFpsNumFrames = 0;
            return;
        }
        this.mFpsNumFrames++;
        String thisHash = Integer.toHexString(System.identityHashCode(this));
        long totalTime = nowTime - this.mFpsStartTime;
        Log.v(TAG, "0x" + thisHash + "\tFrame time:\t" + (nowTime - this.mFpsPrevTime));
        this.mFpsPrevTime = nowTime;
        if (totalTime > 1000) {
            Log.v(TAG, "0x" + thisHash + "\tFPS:\t" + ((((float) this.mFpsNumFrames) * 1000.0f) / ((float) totalTime)));
            this.mFpsStartTime = nowTime;
            this.mFpsNumFrames = 0;
        }
    }

    private void performDraw() {
        if (this.mAttachInfo.mDisplayState != 1 || this.mReportNextDraw || this.mForceDraw) {
            this.mForceDraw = false;
            boolean fullRedrawNeeded = this.mFullRedrawNeeded;
            this.mFullRedrawNeeded = false;
            this.mIsDrawing = true;
            Trace.traceBegin(8, "draw");
            try {
                if (!this.mIsThisWindowDontNeedSurfaceBuffer) {
                    draw(fullRedrawNeeded);
                } else if (SAFE_DEBUG) {
                    Slog.d(TAG, "window=" + this.mWindowAttributes.getTitle() + ",  mIsThisWindowDontNeedSurfaceBuffer=" + this.mIsThisWindowDontNeedSurfaceBuffer + ".    So, skip drawing.");
                }
                this.mIsDrawing = false;
                Trace.traceEnd(8);
                if (this.mAttachInfo.mPendingAnimatingRenderNodes != null) {
                    int count = this.mAttachInfo.mPendingAnimatingRenderNodes.size();
                    for (int i = 0; i < count; i++) {
                        ((RenderNode) this.mAttachInfo.mPendingAnimatingRenderNodes.get(i)).endAllAnimators();
                    }
                    this.mAttachInfo.mPendingAnimatingRenderNodes.clear();
                }
                if (this.mReportNextDraw) {
                    this.mReportNextDraw = false;
                    if (this.mAttachInfo.mHardwareRenderer != null) {
                        Trace.traceBegin(8, "fence");
                        this.mAttachInfo.mHardwareRenderer.fence();
                        Trace.traceEnd(8);
                    }
                    if (this.mSurfaceHolder != null && this.mSurface.isValid()) {
                        this.mSurfaceHolderCallback.surfaceRedrawNeeded(this.mSurfaceHolder);
                        SurfaceHolder.Callback[] callbacks = this.mSurfaceHolder.getCallbacks();
                        if (callbacks != null) {
                            for (SurfaceHolder.Callback c : callbacks) {
                                if (c instanceof Callback2) {
                                    ((Callback2) c).surfaceRedrawNeeded(this.mSurfaceHolder);
                                }
                            }
                        }
                    }
                    try {
                        this.mWindowSession.finishDrawing(this.mWindow);
                    } catch (RemoteException e) {
                    }
                }
            } catch (Throwable th) {
                this.mIsDrawing = false;
                Trace.traceEnd(8);
            }
        }
    }

    private void draw(boolean fullRedrawNeeded) {
        Surface surface = this.mSurface;
        if (surface != null && surface.isValid()) {
            int curScrollY;
            if (!sFirstDrawComplete) {
                synchronized (sFirstDrawHandlers) {
                    sFirstDrawComplete = true;
                    int count = sFirstDrawHandlers.size();
                    for (int i = 0; i < count; i++) {
                        this.mHandler.post((Runnable) sFirstDrawHandlers.get(i));
                    }
                }
            }
            scrollToRectOrFocus(null, false);
            if (this.mAttachInfo.mViewScrollChanged) {
                this.mAttachInfo.mViewScrollChanged = false;
                this.mAttachInfo.mTreeObserver.dispatchOnScrollChanged();
            }
            boolean animating = this.mScroller != null && this.mScroller.computeScrollOffset();
            if (animating) {
                curScrollY = this.mScroller.getCurrY();
            } else {
                curScrollY = this.mScrollY;
            }
            if (this.mCurScrollY != curScrollY) {
                this.mCurScrollY = curScrollY;
                fullRedrawNeeded = true;
                if (this.mView instanceof RootViewSurfaceTaker) {
                    ((RootViewSurfaceTaker) this.mView).onRootViewScrollYChanged(this.mCurScrollY);
                }
            }
            float appScale = this.mAttachInfo.mApplicationScale;
            boolean scalingRequired = this.mAttachInfo.mScalingRequired;
            int resizeAlpha = 0;
            if (this.mResizeBuffer != null) {
                long deltaTime = SystemClock.uptimeMillis() - this.mResizeBufferStartTime;
                if (deltaTime < ((long) this.mResizeBufferDuration)) {
                    animating = true;
                    resizeAlpha = 255 - ((int) (255.0f * mResizeInterpolator.getInterpolation(((float) deltaTime) / ((float) this.mResizeBufferDuration))));
                } else {
                    disposeResizeBuffer();
                }
            }
            Rect dirty = this.mDirty;
            if (this.mSurfaceHolder != null) {
                dirty.setEmpty();
                if (animating) {
                    if (this.mScroller != null) {
                        this.mScroller.abortAnimation();
                    }
                    disposeResizeBuffer();
                    return;
                }
                return;
            }
            if (fullRedrawNeeded) {
                this.mAttachInfo.mIgnoreDirtyState = true;
                dirty.set(0, 0, (int) ((((float) this.mWidth) * appScale) + 0.5f), (int) ((((float) this.mHeight) * appScale) + 0.5f));
            }
            this.mAttachInfo.mTreeObserver.dispatchOnDraw();
            int xOffset = 0;
            int yOffset = curScrollY;
            LayoutParams params = this.mWindowAttributes;
            Rect surfaceInsets = params != null ? params.surfaceInsets : null;
            if (surfaceInsets != null) {
                xOffset = 0 - surfaceInsets.left;
                yOffset -= surfaceInsets.top;
                if ((this.mWindowAttributes.flags & 8) == 0 || this.mWindowAttributes.token != null) {
                    dirty.offset(surfaceInsets.left, surfaceInsets.right);
                } else {
                    dirty.left -= surfaceInsets.left;
                    dirty.top -= surfaceInsets.top;
                    dirty.right += surfaceInsets.right;
                    dirty.bottom += surfaceInsets.bottom;
                }
            }
            boolean accessibilityFocusDirty = false;
            Drawable drawable = this.mAttachInfo.mAccessibilityFocusDrawable;
            if (drawable != null) {
                Rect bounds = this.mAttachInfo.mTmpInvalRect;
                if (!getAccessibilityFocusedRect(bounds)) {
                    bounds.setEmpty();
                }
                if (!bounds.equals(drawable.getBounds())) {
                    accessibilityFocusDirty = true;
                }
            }
            this.mAttachInfo.mDrawingTime = this.mChoreographer.getFrameTimeNanos() / TimeUtils.NANOS_PER_MS;
            if (!dirty.isEmpty() || this.mIsAnimating || accessibilityFocusDirty) {
                if (this.mAttachInfo.mHardwareRenderer != null && this.mAttachInfo.mHardwareRenderer.isEnabled()) {
                    boolean invalidateRoot = accessibilityFocusDirty;
                    this.mIsAnimating = false;
                    if (!(this.mHardwareYOffset == yOffset && this.mHardwareXOffset == xOffset)) {
                        this.mHardwareYOffset = yOffset;
                        this.mHardwareXOffset = xOffset;
                        invalidateRoot = true;
                    }
                    this.mResizeAlpha = resizeAlpha;
                    if (invalidateRoot) {
                        this.mAttachInfo.mHardwareRenderer.invalidateRoot();
                    }
                    dirty.setEmpty();
                    this.mBlockResizeBuffer = false;
                    this.mAttachInfo.mHardwareRenderer.draw(this.mView, this.mAttachInfo, this);
                } else if (this.mAttachInfo.mHardwareRenderer != null && !this.mAttachInfo.mHardwareRenderer.isEnabled() && this.mAttachInfo.mHardwareRenderer.isRequested()) {
                    try {
                        this.mAttachInfo.mHardwareRenderer.initializeIfNeeded(this.mWidth, this.mHeight, this.mAttachInfo, this.mSurface, surfaceInsets);
                        this.mFullRedrawNeeded = true;
                        scheduleTraversals();
                        return;
                    } catch (OutOfResourcesException e) {
                        handleOutOfResourcesException(e);
                        return;
                    }
                } else if (!drawSoftware(surface, this.mAttachInfo, xOffset, yOffset, scalingRequired, dirty)) {
                    return;
                }
            }
            if (animating) {
                this.mFullRedrawNeeded = true;
                scheduleTraversals();
            }
        } else if (surface == null) {
            Log.e("ViewSystem", "ViewRootImpl #1 Surface is null.");
        } else if (!surface.isValid()) {
            Log.e("ViewSystem", "ViewRootImpl #2 Surface is not valid.");
        }
    }

    private boolean drawSoftware(Surface surface, AttachInfo attachInfo, int xoff, int yoff, boolean scalingRequired, Rect dirty) {
        if (sRendererDemoted) {
            return false;
        }
        try {
            int left = dirty.left;
            int top = dirty.top;
            int right = dirty.right;
            int bottom = dirty.bottom;
            Canvas canvas = this.mSurface.lockCanvas(dirty);
            canvas.setHighContrastText(attachInfo.mHighContrastText);
            canvas.setDCSFormat(sDcsFormat);
            if (!(left == dirty.left && top == dirty.top && right == dirty.right && bottom == dirty.bottom)) {
                attachInfo.mIgnoreDirtyState = true;
            }
            canvas.setDensity(this.mDensity);
            try {
                if (!(canvas.isOpaque() && yoff == 0 && xoff == 0)) {
                    canvas.drawColor(0, Mode.CLEAR);
                }
                dirty.setEmpty();
                this.mIsAnimating = false;
                View view = this.mView;
                view.mPrivateFlags |= 32;
                canvas.translate((float) (-xoff), (float) (-yoff));
                if (this.mTranslator != null) {
                    this.mTranslator.translateCanvas(canvas);
                }
                canvas.setScreenDensity(scalingRequired ? this.mNoncompatDensity : 0);
                attachInfo.mSetIgnoreDirtyState = false;
                this.mView.draw(canvas);
                drawAccessibilityFocusedDrawableIfNeeded(canvas);
                if (!attachInfo.mSetIgnoreDirtyState) {
                    attachInfo.mIgnoreDirtyState = false;
                }
                try {
                    surface.unlockCanvasAndPost(canvas);
                    return true;
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "Could not unlock surface", e);
                    this.mLayoutRequested = true;
                    return false;
                }
            } catch (Throwable th) {
                try {
                    surface.unlockCanvasAndPost(canvas);
                } catch (IllegalArgumentException e2) {
                    Log.e(TAG, "Could not unlock surface", e2);
                    this.mLayoutRequested = true;
                    return false;
                }
            }
        } catch (OutOfResourcesException e3) {
            handleOutOfResourcesException(e3);
            return false;
        } catch (IllegalArgumentException e22) {
            Log.e(TAG, "Could not lock surface", e22);
            this.mLayoutRequested = true;
            return false;
        }
    }

    private void drawAccessibilityFocusedDrawableIfNeeded(Canvas canvas) {
        Rect bounds = this.mAttachInfo.mTmpInvalRect;
        if (getAccessibilityFocusedRect(bounds)) {
            Drawable drawable = getAccessibilityFocusedDrawable();
            if (drawable != null) {
                drawable.setBounds(bounds);
                drawable.draw(canvas);
            }
        } else if (this.mAttachInfo.mAccessibilityFocusDrawable != null) {
            this.mAttachInfo.mAccessibilityFocusDrawable.setBounds(0, 0, 0, 0);
        }
    }

    private boolean getAccessibilityFocusedRect(Rect bounds) {
        boolean z = true;
        AccessibilityManager manager = AccessibilityManager.getInstance(this.mContext);
        if (!manager.isEnabled()) {
            return false;
        }
        boolean universalSwitchEnabled;
        if (Secure.getInt(this.mContext.getContentResolver(), "universal_switch_enabled", 0) == 1) {
            universalSwitchEnabled = true;
        } else {
            universalSwitchEnabled = false;
        }
        if (!universalSwitchEnabled && !manager.isTouchExplorationEnabled()) {
            return false;
        }
        View host = this.mAccessibilityFocusedHost;
        if (host == null || host.mAttachInfo == null) {
            return false;
        }
        if (host.getAccessibilityNodeProvider() == null) {
            host.getBoundsOnScreen(bounds, true);
        } else if (this.mAccessibilityFocusedVirtualView == null) {
            return false;
        } else {
            this.mAccessibilityFocusedVirtualView.getBoundsInScreen(bounds);
        }
        AttachInfo attachInfo = this.mAttachInfo;
        bounds.offset(0, attachInfo.mViewRootImpl.mScrollY);
        bounds.offset(-attachInfo.mWindowLeft, -attachInfo.mWindowTop);
        if (!bounds.intersect(0, 0, attachInfo.mViewRootImpl.mWidth, attachInfo.mViewRootImpl.mHeight)) {
            bounds.setEmpty();
        }
        if (bounds.isEmpty()) {
            z = false;
        }
        return z;
    }

    private Drawable getAccessibilityFocusedDrawable() {
        GradientDrawable draw2;
        if (this.mAttachInfo.mAccessibilityFocusDrawable == null) {
            TypedValue value = new TypedValue();
            if (this.mView.mContext.getTheme().resolveAttribute(R.attr.accessibilityFocusedDrawable, value, true)) {
                this.mAttachInfo.mAccessibilityFocusDrawable = this.mView.mContext.getDrawable(value.resourceId);
            }
        }
        int px = (int) TypedValue.applyDimension(1, 2.0f, this.mContext.getResources().getDisplayMetrics());
        if (Secure.getInt(this.mContentResolver, "enabled_accessibility_s_talkback", 0) == 1) {
            draw2 = this.mAttachInfo.mAccessibilityFocusDrawable;
            draw2.setStroke((int) TypedValue.applyDimension(1, (float) Secure.getInt(this.mContentResolver, "accessibility_large_cursor", 2), this.mContext.getResources().getDisplayMetrics()), Secure.getInt(this.mContentResolver, "accessibility_cursor_color", this.mContext.getResources().getColor(R.color.tw_accessibility_focus_highlight)));
            this.mAttachInfo.mAccessibilityFocusDrawable = draw2;
        } else {
            draw2 = (GradientDrawable) this.mAttachInfo.mAccessibilityFocusDrawable;
            draw2.setStroke(px, this.mContext.getResources().getColor(R.color.tw_accessibility_focus_highlight));
            this.mAttachInfo.mAccessibilityFocusDrawable = draw2;
        }
        boolean universalSwitchEnabled = Secure.getInt(this.mContentResolver, "universal_switch_enabled", 0) == 1;
        if (SAFE_DEBUG) {
            Log.i(TAG, "universal Switch enabled:" + universalSwitchEnabled);
        }
        if (universalSwitchEnabled) {
            int color = this.mContext.getResources().getColor(R.color.universal_switch_row_scan_blue);
            int color2 = Secure.getInt(this.mContentResolver, "accessibility_universal_switch_cursor_color", 1);
            if (SAFE_DEBUG) {
                Log.i(TAG, "universal Switch current color:" + color2);
            }
            if (color2 == 1) {
                color = this.mContext.getResources().getColor(R.color.universal_switch_row_scan_blue);
            } else if (color2 == 2) {
                color = this.mContext.getResources().getColor(R.color.universal_switch_row_scan_green);
            } else if (color2 == 5) {
                color = this.mContext.getResources().getColor(R.color.universal_switch_row_scan_red);
            } else if (color2 == 6) {
                color = this.mContext.getResources().getColor(R.color.universal_switch_row_scan_gray);
            } else if (color2 == 4) {
                color = this.mContext.getResources().getColor(R.color.universal_switch_row_scan_orange);
            } else if (color2 == 3) {
                color = this.mContext.getResources().getColor(R.color.universal_switch_row_scan_yellow);
            } else {
                color = this.mContext.getResources().getColor(R.color.universal_switch_row_scan_blue);
            }
            draw2 = this.mAttachInfo.mAccessibilityFocusDrawable;
            draw2.setStroke((int) this.mContext.getResources().getDimension(R.dimen.universal_switch_stroke_width), color);
            this.mAttachInfo.mAccessibilityFocusDrawable = draw2;
            Log.i(TAG, "Changing accessibility focused drawable according to Universal Switch settings");
        }
        return this.mAttachInfo.mAccessibilityFocusDrawable;
    }

    public void setDrawDuringWindowsAnimating(boolean value) {
        this.mDrawDuringWindowsAnimating = value;
        if (value) {
            handleDispatchWindowAnimationStopped();
        }
    }

    boolean scrollToRectOrFocus(Rect rectangle, boolean immediate) {
        Rect ci = this.mAttachInfo.mContentInsets;
        Rect vi = this.mAttachInfo.mVisibleInsets;
        int scrollY = 0;
        boolean handled = false;
        if (vi.left > ci.left || vi.top > ci.top || vi.right > ci.right || vi.bottom > ci.bottom) {
            scrollY = this.mScrollY;
            View focus = this.mView.findFocus();
            if (!this.mAttachInfo.mHasWindowFocus && focus != null && (focus instanceof EditText)) {
                return false;
            }
            if (focus == null) {
                return false;
            }
            View lastScrolledFocus;
            if (this.mLastScrolledFocus != null) {
                lastScrolledFocus = (View) this.mLastScrolledFocus.get();
            } else {
                lastScrolledFocus = null;
            }
            if (focus != lastScrolledFocus) {
                rectangle = null;
            }
            if (!(focus == lastScrolledFocus && !this.mScrollMayChange && rectangle == null)) {
                this.mLastScrolledFocus = new WeakReference(focus);
                this.mScrollMayChange = false;
                scrollY = 0;
                if (focus.getGlobalVisibleRect(this.mVisRect, null)) {
                    if (rectangle == null) {
                        focus.getFocusedRect(this.mTempRect);
                        if (this.mView instanceof ViewGroup) {
                            try {
                                ((ViewGroup) this.mView).offsetDescendantRectToMyCoords(focus, this.mTempRect);
                            } catch (IllegalArgumentException ex) {
                                Log.e(TAG, "offsetDescendantRectToMyCoords() error occurred. focus=" + focus + " mTempRect=" + this.mTempRect.toShortString() + " " + ex);
                                ex.printStackTrace();
                            }
                        }
                    } else {
                        this.mTempRect.set(rectangle);
                    }
                    if (this.mTempRect.intersect(this.mVisRect)) {
                        if (this.mTempRect.height() <= (this.mView.getHeight() - vi.top) - vi.bottom) {
                            if (this.mTempRect.top - 0 < vi.top) {
                                scrollY = 0 - (vi.top - (this.mTempRect.top - 0));
                            } else if (this.mTempRect.bottom - 0 > this.mView.getHeight() - vi.bottom) {
                                scrollY = 0 + ((this.mTempRect.bottom - 0) - (this.mView.getHeight() - vi.bottom));
                            }
                        }
                        handled = true;
                    }
                }
            }
        }
        if (scrollY != this.mScrollY) {
            if (!immediate && this.mResizeBuffer == null) {
                if (this.mScroller == null) {
                    this.mScroller = new Scroller(this.mView.getContext());
                }
                this.mScroller.startScroll(0, this.mScrollY, 0, scrollY - this.mScrollY);
                if ((this.mSkipPanScrollEnterAnimation && scrollY != 0) || (this.mSkipPanScrollExitAnimation && scrollY == 0)) {
                    this.mScroller.abortAnimation();
                }
            } else if (this.mScroller != null) {
                this.mScroller.abortAnimation();
            }
            this.mScrollY = scrollY;
        }
        return handled;
    }

    public void setSkipPanScrollAnimation(boolean bSkipEnter, boolean bSkipExit) {
        this.mSkipPanScrollEnterAnimation = bSkipEnter;
        this.mSkipPanScrollExitAnimation = bSkipExit;
    }

    public View getAccessibilityFocusedHost() {
        return this.mAccessibilityFocusedHost;
    }

    public AccessibilityNodeInfo getAccessibilityFocusedVirtualView() {
        return this.mAccessibilityFocusedVirtualView;
    }

    void setAccessibilityFocus(View view, AccessibilityNodeInfo node) {
        if (this.mAccessibilityFocusedVirtualView != null) {
            AccessibilityNodeInfo focusNode = this.mAccessibilityFocusedVirtualView;
            View focusHost = this.mAccessibilityFocusedHost;
            this.mAccessibilityFocusedHost = null;
            this.mAccessibilityFocusedVirtualView = null;
            focusHost.clearAccessibilityFocusNoCallbacks();
            AccessibilityNodeProvider provider = focusHost.getAccessibilityNodeProvider();
            if (provider != null) {
                focusNode.getBoundsInParent(this.mTempRect);
                focusHost.invalidate(this.mTempRect);
                provider.performAction(AccessibilityNodeInfo.getVirtualDescendantId(focusNode.getSourceNodeId()), 128, null);
            }
            focusNode.recycle();
        }
        if (this.mAccessibilityFocusedHost != null) {
            this.mAccessibilityFocusedHost.clearAccessibilityFocusNoCallbacks();
        }
        this.mAccessibilityFocusedHost = view;
        this.mAccessibilityFocusedVirtualView = node;
        if (this.mAttachInfo.mHardwareRenderer != null) {
            this.mAttachInfo.mHardwareRenderer.invalidateRoot();
        }
    }

    public void requestChildFocus(View child, View focused) {
        checkThread();
        scheduleTraversals();
    }

    public void clearChildFocus(View child) {
        checkThread();
        scheduleTraversals();
    }

    public ViewParent getParentForAccessibility() {
        return null;
    }

    public void focusableViewAvailable(View v) {
        checkThread();
        if (this.mView == null) {
            return;
        }
        if (this.mView.hasFocus()) {
            View focused = this.mView.findFocus();
            if ((focused instanceof ViewGroup) && ((ViewGroup) focused).getDescendantFocusability() == 262144 && isViewDescendantOf(v, focused)) {
                v.requestFocus();
                return;
            }
            return;
        }
        v.requestFocus();
    }

    public void recomputeViewAttributes(View child) {
        checkThread();
        if (this.mView == child) {
            this.mAttachInfo.mRecomputeGlobalAttributes = true;
            if (!this.mWillDrawSoon) {
                scheduleTraversals();
            }
        }
    }

    void dispatchDetachedFromWindow() {
        if (!(this.mView == null || this.mView.mAttachInfo == null)) {
            this.mAttachInfo.mTreeObserver.dispatchOnWindowAttachedChange(false);
            this.mView.dispatchDetachedFromWindow();
            if (this.mToolBoxManager != null) {
                this.mToolBoxManager.sendMessage("", 3, 0);
            }
        }
        this.mAccessibilityInteractionConnectionManager.ensureNoConnection();
        this.mAccessibilityManager.removeAccessibilityStateChangeListener(this.mAccessibilityInteractionConnectionManager);
        this.mAccessibilityManager.removeHighTextContrastStateChangeListener(this.mHighContrastTextManager);
        removeSendWindowContentChangedCallback();
        destroyHardwareRenderer();
        setAccessibilityFocus(null, null);
        if (this.mView != null) {
            this.mView.assignParent(null);
            this.mView = null;
            Log.d(TAG, "#3 mView = null");
        }
        this.mAttachInfo.mRootView = null;
        this.mSurface.release();
        if (!(this.mInputQueueCallback == null || this.mInputQueue == null)) {
            this.mInputQueueCallback.onInputQueueDestroyed(this.mInputQueue);
            this.mInputQueue.dispose();
            this.mInputQueueCallback = null;
            this.mInputQueue = null;
        }
        if (this.mInputEventReceiver != null) {
            this.mInputEventReceiver.dispose();
            this.mInputEventReceiver = null;
        }
        try {
            if (this.mAdded) {
                this.mWindowSession.remove(this.mWindow);
            }
        } catch (RemoteException e) {
        }
        if (this.mInputChannel != null) {
            this.mInputChannel.dispose();
            this.mInputChannel = null;
        }
        this.mDisplayManager.unregisterDisplayListener(this.mDisplayListener);
        unscheduleTraversals();
    }

    void updateConfiguration(Configuration config, boolean force) {
        CompatibilityInfo ci = this.mDisplayAdjustments.getCompatibilityInfo();
        if (!ci.equals(CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO)) {
            Configuration config2 = new Configuration(config);
            ci.applyToConfiguration(this.mNoncompatDensity, config2);
            config = config2;
        }
        synchronized (sConfigCallbacks) {
            for (int i = sConfigCallbacks.size() - 1; i >= 0; i--) {
                ((ComponentCallbacks) sConfigCallbacks.get(i)).onConfigurationChanged(config);
            }
        }
        if (this.mView != null) {
            config = this.mView.getResources().getConfiguration();
            int diff = this.mLastConfiguration.diff(config);
            if ((diff & 128) != 0) {
                this.mOrientationChanged = true;
            }
            if (!((diff & 128) == 0 || (this.mWindowAttributes.multiWindowFlags & 16) == 0 || (this.mScaleFactor.x >= 1.0f && this.mScaleFactor.y >= 1.0f))) {
                this.mNewScaleFactorNeeded = true;
            }
            if (force || this.mLastConfiguration.diff(config) != 0) {
                int lastLayoutDirection = this.mLastConfiguration.getLayoutDirection();
                int currentLayoutDirection = config.getLayoutDirection();
                this.mLastConfiguration.setTo(config);
                if (lastLayoutDirection != currentLayoutDirection && this.mViewLayoutDirectionInitial == 2) {
                    this.mView.setLayoutDirection(currentLayoutDirection);
                }
                this.mView.dispatchConfigurationChanged(config);
                if (this.mLastConfiguration.mobileKeyboardCovered == 1 && this.mView != null && this.mAttachInfo.mHasWindowFocus) {
                    View focusView = this.mView.findFocus();
                    if (focusView != null) {
                        InputMethodManager imm = InputMethodManager.peekInstance();
                        if (imm != null && !imm.isInputMethodShown()) {
                            imm.focusIn(focusView);
                        }
                    }
                }
            }
        }
    }

    public static boolean isViewDescendantOf(View child, View parent) {
        if (child == parent) {
            return true;
        }
        ViewParent theParent = child.getParent();
        if ((theParent instanceof ViewGroup) && isViewDescendantOf((View) theParent, parent)) {
            return true;
        }
        return false;
    }

    private static void forceLayout(View view) {
        view.forceLayout();
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                forceLayout(group.getChildAt(i));
            }
        }
    }

    public boolean twProcessTwToolBox(MotionEvent event, int action) {
        if ((event.getFlags() & 536870912) != 0) {
            return false;
        }
        if (action == 0) {
            if (this.mToolBoxManager == null) {
                this.mToolBoxManager = new TwToolBoxManager(this.mContext);
            }
            this.mTwToolBoxTracking = this.mToolBoxManager.isContain((int) event.getRawX(), (int) event.getRawY());
        } else if (action == 1 || action == 3) {
            boolean tracking = this.mTwToolBoxTracking;
            this.mTwToolBoxTracking = false;
            return tracking;
        }
        return this.mTwToolBoxTracking;
    }

    public void twUpdateToolBox() {
        boolean z = true;
        if (Settings$System.getInt(this.mContentResolver, Settings$System.TOOLBOX_ONOFF, 0) != 1) {
            z = false;
        }
        this.mUseFloatingToolBox = z;
    }

    boolean ensureTouchMode(boolean inTouchMode) {
        if (this.mAttachInfo.mInTouchMode == inTouchMode) {
            return false;
        }
        try {
            if (!isInLocalFocusMode()) {
                this.mWindowSession.setInTouchMode(inTouchMode);
            }
            return ensureTouchModeLocally(inTouchMode);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean ensureTouchModeLocally(boolean inTouchMode) {
        if (this.mAttachInfo.mInTouchMode == inTouchMode || this.mResizeAnimating) {
            return false;
        }
        this.mAttachInfo.mInTouchMode = inTouchMode;
        this.mAttachInfo.mTreeObserver.dispatchOnTouchModeChanged(inTouchMode);
        return inTouchMode ? enterTouchMode() : leaveTouchMode();
    }

    private boolean enterTouchMode() {
        if (this.mView != null && this.mView.hasFocus()) {
            View focused = this.mView.findFocus();
            if (!(focused == null || focused.isFocusableInTouchMode())) {
                ViewGroup ancestorToTakeFocus = findAncestorToTakeFocusInTouchMode(focused);
                if (ancestorToTakeFocus != null) {
                    return ancestorToTakeFocus.requestFocus();
                }
                focused.clearFocusInternal(null, true, false);
                return true;
            }
        }
        return false;
    }

    private static ViewGroup findAncestorToTakeFocusInTouchMode(View focused) {
        ViewParent parent = focused.getParent();
        while (parent instanceof ViewGroup) {
            ViewGroup vgParent = (ViewGroup) parent;
            if (vgParent.getDescendantFocusability() == 262144 && vgParent.isFocusableInTouchMode()) {
                return vgParent;
            }
            if (vgParent.isRootNamespace()) {
                return null;
            }
            parent = vgParent.getParent();
        }
        return null;
    }

    private boolean leaveTouchMode() {
        if (this.mView == null) {
            return false;
        }
        if (this.mView.hasFocus()) {
            View focusedView = this.mView.findFocus();
            if (!((focusedView instanceof ViewGroup) && ((ViewGroup) focusedView).getDescendantFocusability() == 262144)) {
                return false;
            }
        }
        View focused = focusSearch(null, 130);
        if (focused != null) {
            return focused.requestFocus(130);
        }
        return false;
    }

    private boolean checkPalmRejection(MotionEvent event) {
        int SsumMajor = 0;
        boolean bPalm = false;
        int N = event.getPointerCount();
        int i = 0;
        while (i < N) {
            if (event.getPalm(i) == 1.0f || event.getPalm(i) == 3.0f) {
                bPalm = true;
            }
            SsumMajor = (int) event.getTouchMajor(i);
            i++;
        }
        if (event.getPalm() == -2.0f) {
            return false;
        }
        if (SsumMajor >= 100 || bPalm) {
            return true;
        }
        return false;
    }

    private boolean getPalmRejection(MotionEvent event) {
        int i;
        int[] Sxd = new int[20];
        int[] Syd = new int[20];
        int[] Major = new int[20];
        int[] Minor = new int[20];
        float SvarX = 0.0f;
        float SsumX = 0.0f;
        float SsumY = 0.0f;
        float SsumMajor = 0.0f;
        float SsumMinor = 0.0f;
        boolean bPalm = false;
        float TILT_TO_ZOOM_XVAR = 200.0f;
        int mScreenWidth = 0;
        int mScreenHeight = 0;
        int N = event.getPointerCount();
        try {
            if (this.mContext != null) {
                Display disp = ((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                disp.getMetrics(metrics);
                mScreenWidth = metrics.widthPixels;
                mScreenHeight = metrics.heightPixels;
            }
            if (mScreenHeight > mScreenWidth) {
                TILT_TO_ZOOM_XVAR = (float) mScreenWidth;
            } else {
                TILT_TO_ZOOM_XVAR = (float) mScreenHeight;
            }
        } catch (Exception e) {
            Log.d(TAG, "[Surface touch] Default Rotate = false");
        }
        for (i = 0; i < N; i++) {
            Sxd[i] = (int) event.getX(i);
            Syd[i] = (int) event.getY(i);
            Major[i] = (int) event.getTouchMajor(i);
            Minor[i] = (int) event.getTouchMinor(i);
        }
        for (i = 0; i < N; i++) {
            SsumX += (float) Sxd[i];
            SsumY += (float) Syd[i];
            SsumMajor += (float) Major[i];
            SsumMinor += (float) Minor[i];
        }
        float SmeanX = SsumX / ((float) N);
        float SmeanY = SsumY / ((float) N);
        float SsumEccen = SsumMajor / SsumMinor;
        i = 0;
        while (i < N) {
            SvarX += (float) Math.sqrt((double) ((((float) Sxd[i]) - SmeanX) * (((float) Sxd[i]) - SmeanX)));
            if (event.getPalm(i) == 1.0f || event.getPalm(i) == 3.0f) {
                bPalm = true;
            }
            i++;
        }
        SvarX /= (float) N;
        if (bPalm && event.getToolType(0) == 1 && event.getAction() != 1) {
            Log.d(TAG, "[ViewRootImpl] action cancel - 1, eccen:" + SsumEccen);
            return true;
        } else if (event.getToolType(0) != 1 || SsumMajor < 100.0f || SsumEccen <= 2.0f || SvarX >= TILT_TO_ZOOM_XVAR / ((float) (N + 4))) {
            return false;
        } else {
            Log.d(TAG, "[ViewRootImpl] action cancel - 2, Palm Sweep, SsumMajor:" + SsumMajor + " eccen:" + SsumEccen + " varX:" + SvarX);
            return true;
        }
    }

    private static boolean isNavigationKey(KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 61:
            case 62:
            case 66:
            case 92:
            case 93:
            case 122:
            case 123:
                return true;
            default:
                return false;
        }
    }

    private static boolean isTypingKey(KeyEvent keyEvent) {
        return keyEvent.getUnicodeChar() > 0;
    }

    private boolean checkForLeavingTouchModeAndConsume(KeyEvent event) {
        if (!this.mAttachInfo.mInTouchMode) {
            return false;
        }
        int action = event.getAction();
        if ((action != 0 && action != 2) || (event.getFlags() & 4) != 0) {
            return false;
        }
        if (isNavigationKey(event)) {
            return ensureTouchMode(false);
        }
        if (!isTypingKey(event)) {
            return false;
        }
        ensureTouchMode(false);
        return false;
    }

    void setLocalDragState(Object obj) {
        this.mLocalDragState = obj;
    }

    private void handleDragEvent(DragEvent event) {
        if (this.mView != null && this.mAdded) {
            int what = event.mAction;
            if (!(this.mScaleFactor.x == 1.0f && this.mScaleFactor.y == 1.0f)) {
                event.mX *= 1.0f / this.mScaleFactor.x;
                event.mY *= 1.0f / this.mScaleFactor.y;
            }
            if (what == 6) {
                this.mView.dispatchDragEvent(event);
            } else {
                if (what == 1) {
                    this.mCurrentDragView = null;
                    this.mDragDescription = event.mClipDescription;
                    if (!(this.mContext == null || !this.mView.hasWindowFocus() || getMultiWindowStyle().getType() == 0)) {
                        this.mFocusDragStartWin = true;
                    }
                } else {
                    event.mClipDescription = this.mDragDescription;
                }
                if (what == 2 || what == 3) {
                    this.mDragPoint.set(event.mX, event.mY);
                    if (this.mTranslator != null) {
                        this.mTranslator.translatePointInScreenToAppWindow(this.mDragPoint);
                    }
                    if (this.mCurScrollY != 0) {
                        this.mDragPoint.offset(0.0f, (float) this.mCurScrollY);
                    }
                    event.mX = this.mDragPoint.x;
                    event.mY = this.mDragPoint.y;
                }
                View prevDragView = this.mCurrentDragView;
                boolean result = this.mView.dispatchDragEvent(event);
                if (prevDragView != this.mCurrentDragView) {
                    if (prevDragView != null) {
                        try {
                            this.mWindowSession.dragRecipientExited(this.mWindow);
                        } catch (RemoteException e) {
                            Slog.e(TAG, "Unable to note drag target change");
                        }
                    }
                    if (this.mCurrentDragView != null) {
                        this.mWindowSession.dragRecipientEntered(this.mWindow);
                    }
                }
                if (what == 3) {
                    this.mDragDescription = null;
                    try {
                        Log.i(TAG, "Reporting drop result: " + result);
                        this.mWindowSession.reportDropResult(this.mWindow, result);
                    } catch (RemoteException e2) {
                        Log.e(TAG, "Unable to report drop result");
                    }
                    if (!(result || this.mFocusDragStartWin || event.getClipData() == null || event.getClipData().getItemCount() <= 0)) {
                        ClipDescription description = event.getClipData().getDescription();
                        if (!(description == null || description.getLabel() == null || (!description.getLabel().toString().equals(MULTI_WINDOW_DRAG_AND_DROP_IMAGE) && !description.getLabel().toString().equals(MULTI_WINDOW_DRAG_AND_DROP_TEXT)))) {
                            Context context = this.mContext;
                            if (!((context instanceof Activity) || context == null || !(context instanceof ContextWrapper))) {
                                context = ((ContextWrapper) context).getBaseContext();
                            }
                            if (context != null && (context instanceof Activity)) {
                                Toast toast = Toast.makeText(new ContextThemeWrapper(this.mContext, (int) R.style.Theme_DeviceDefault), this.mContext.getString(R.string.SS_NOT_SUPPORTED), 1);
                                ((TextView) toast.getView().findViewById(R.id.message)).setBackground(null);
                                toast.show();
                            }
                        }
                    }
                }
                if (what == 4) {
                    this.mFocusDragStartWin = false;
                    setLocalDragState(null);
                }
            }
        }
        event.recycle();
    }

    public void handleDispatchSystemUiVisibilityChanged(SystemUiVisibilityInfo args) {
        if (this.mSeq != args.seq) {
            this.mSeq = args.seq;
            this.mAttachInfo.mForceReportNewAttributes = true;
            scheduleTraversals();
        }
        if (this.mView != null) {
            if (args.localChanges != 0) {
                this.mView.updateLocalSystemUiVisibility(args.localValue, args.localChanges);
            }
            int visibility = args.globalVisibility & 7;
            if (visibility != this.mAttachInfo.mGlobalSystemUiVisibility) {
                this.mAttachInfo.mGlobalSystemUiVisibility = visibility;
                this.mView.dispatchSystemUiVisibilityChanged(visibility);
            }
        }
    }

    public void handleDispatchWindowAnimationStarted(int remainingFrameCount) {
        if (!this.mDrawDuringWindowsAnimating && remainingFrameCount != -1) {
            if (!(this.mAppVisible || (this.mWindowAttributes.samsungFlags & 4096) == 0)) {
                this.mWindowsExitAnimating = true;
            }
            this.mRemainingFrameCount = remainingFrameCount;
            this.mWindowsAnimating = true;
        }
    }

    public void handleDispatchWindowAnimationStopped() {
        if (this.mWindowsAnimating) {
            this.mWindowsAnimating = false;
            if (this.mWindowsExitAnimating && (this.mWindowAttributes.samsungFlags & 4096) != 0) {
                handleDispatchSurfaceDestroyDeferred();
            }
            if (!this.mDirty.isEmpty() || this.mIsAnimating || this.mFullRedrawNeeded || !(this.mScroller == null || this.mScrollY == this.mScroller.getCurrY())) {
                scheduleTraversals();
            }
        }
    }

    public void handleDispatchWindowShown() {
        this.mAttachInfo.mTreeObserver.dispatchOnWindowShown();
    }

    private void handleDispatchCoverStateChanged(boolean isOpen) {
        if (this.mView != null && this.mAttachInfo.mIsOpen != isOpen) {
            this.mAttachInfo.mIsOpen = isOpen;
            this.mView.dispatchCoverStateChanged(isOpen);
        }
    }

    private void handleDispatchSurfaceDestroyDeferred() {
        this.mWindowsExitAnimating = false;
        if ((!this.mAppVisible || this.mReportedViewVisibility == 8) && this.mView != null && (this.mWindowAttributes.samsungFlags & 4096) != 0) {
            int viewVisibility = getHostVisibility();
            if (!(!this.mAdded || this.mFirst || this.mViewVisibility == viewVisibility)) {
                View host = this.mView;
                this.mAttachInfo.mWindowVisibility = viewVisibility;
                host.dispatchWindowVisibilityChanged(viewVisibility);
                if (viewVisibility != 0) {
                    destroyHardwareResources();
                }
                if (viewVisibility == 8) {
                    this.mHasHadWindowFocus = false;
                    try {
                        this.mWindowSession.performDeferredDestroy(this.mWindow);
                    } catch (RemoteException e) {
                    }
                }
                this.mSurface.release();
            }
            this.mViewVisibility = viewVisibility;
        }
    }

    private void handleDispatchMultiWindowStateChanged(int state) {
        if (this.mView != null) {
            this.mView.dispatchMultiWindowStateChanged(state);
        }
    }

    public void getLastTouchPoint(Point outLocation) {
        outLocation.x = (int) this.mLastTouchPoint.x;
        outLocation.y = (int) this.mLastTouchPoint.y;
    }

    public void setDragFocus(View newDragTarget) {
        if (this.mCurrentDragView != newDragTarget) {
            this.mCurrentDragView = newDragTarget;
        }
    }

    private AudioManager getAudioManager() {
        if (this.mView == null) {
            throw new IllegalStateException("getAudioManager called when there is no mView");
        }
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) this.mView.getContext().getSystemService("audio");
        }
        return this.mAudioManager;
    }

    public AccessibilityInteractionController getAccessibilityInteractionController() {
        if (this.mView == null) {
            throw new IllegalStateException("getAccessibilityInteractionController called when there is no mView");
        }
        if (this.mAccessibilityInteractionController == null) {
            this.mAccessibilityInteractionController = new AccessibilityInteractionController(this);
        }
        return this.mAccessibilityInteractionController;
    }

    private int relayoutWindow(LayoutParams params, int internalViewVisibility, boolean insetsPending, int viewVisibility) throws RemoteException {
        int i;
        int i2;
        float appScale = this.mAttachInfo.mApplicationScale;
        boolean restore = false;
        if (!(params == null || this.mTranslator == null)) {
            restore = true;
            params.backup();
            this.mTranslator.translateWindowLayout(params);
        }
        if (params != null) {
            this.mPendingConfiguration.seq = 0;
        } else {
            this.mPendingConfiguration.seq = 0;
        }
        if (!(params == null || this.mOrigWindowType == params.type || this.mTargetSdkVersion >= 14)) {
            Slog.w(TAG, "Window type can not be changed after the window is added; ignoring change of " + this.mView);
            params.type = this.mOrigWindowType;
        }
        IWindowSession iWindowSession = this.mWindowSession;
        IWindow iWindow = this.mWindow;
        int i3 = this.mSeq;
        int measuredWidth = (int) ((((float) this.mView.getMeasuredWidth()) * appScale) + 0.5f);
        int measuredHeight = (int) ((((float) this.mView.getMeasuredHeight()) * appScale) + 0.5f);
        if (insetsPending) {
            i = 1;
        } else {
            i = 0;
        }
        if (internalViewVisibility != 0 || (this.mWindowAttributes.samsungFlags & 4096) == 0) {
            i2 = 0;
        } else {
            i2 = 2;
        }
        int relayoutResult = iWindowSession.relayout(iWindow, i3, params, measuredWidth, measuredHeight, viewVisibility, i | i2, this.mWinFrame, this.mPendingOverscanInsets, this.mPendingContentInsets, this.mPendingVisibleInsets, this.mPendingStableInsets, this.mPendingOutsets, this.mPendingConfiguration, this.mSurface, this.mScaleFactor, this.mCocktailBar);
        if (restore) {
            params.restore();
        }
        if (this.mTranslator != null) {
            this.mTranslator.translateRectInScreenToAppWinFrame(this.mWinFrame);
            this.mTranslator.translateRectInScreenToAppWindow(this.mPendingOverscanInsets);
            this.mTranslator.translateRectInScreenToAppWindow(this.mPendingContentInsets);
            this.mTranslator.translateRectInScreenToAppWindow(this.mPendingVisibleInsets);
            this.mTranslator.translateRectInScreenToAppWindow(this.mPendingStableInsets);
        }
        return relayoutResult;
    }

    public void playSoundEffect(int effectId) {
        checkThread();
        try {
            AudioManager audioManager = getAudioManager();
            switch (effectId) {
                case 0:
                    audioManager.playSoundEffect(0);
                    return;
                case 1:
                    audioManager.playSoundEffect(3);
                    return;
                case 2:
                    audioManager.playSoundEffect(1);
                    return;
                case 3:
                    audioManager.playSoundEffect(4);
                    return;
                case 4:
                    audioManager.playSoundEffect(2);
                    return;
                default:
                    throw new IllegalArgumentException("unknown effect id " + effectId + " not defined in " + SoundEffectConstants.class.getCanonicalName());
            }
        } catch (IllegalStateException e) {
            Log.e(TAG, "FATAL EXCEPTION when attempting to play sound effect: " + e);
            e.printStackTrace();
        }
        Log.e(TAG, "FATAL EXCEPTION when attempting to play sound effect: " + e);
        e.printStackTrace();
    }

    public boolean performHapticFeedback(int effectId, boolean always) {
        try {
            return this.mWindowSession.performHapticFeedback(this.mWindow, effectId, always);
        } catch (RemoteException e) {
            return false;
        }
    }

    public View focusSearch(View focused, int direction) {
        checkThread();
        if (this.mView instanceof ViewGroup) {
            return FocusFinder.getInstance().findNextFocus((ViewGroup) this.mView, focused, direction);
        }
        return null;
    }

    public void debug() {
        this.mView.debug();
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        String innerPrefix = prefix + "  ";
        writer.print(prefix);
        writer.println("ViewRoot:");
        writer.print(innerPrefix);
        writer.print("mAdded=");
        writer.print(this.mAdded);
        writer.print(" mRemoved=");
        writer.println(this.mRemoved);
        writer.print(innerPrefix);
        writer.print("mConsumeBatchedInputScheduled=");
        writer.println(this.mConsumeBatchedInputScheduled);
        writer.print(innerPrefix);
        writer.print("mConsumeBatchedInputImmediatelyScheduled=");
        writer.println(this.mConsumeBatchedInputImmediatelyScheduled);
        writer.print(innerPrefix);
        writer.print("mPendingInputEventCount=");
        writer.println(this.mPendingInputEventCount);
        writer.print(innerPrefix);
        writer.print("mProcessInputEventsScheduled=");
        writer.println(this.mProcessInputEventsScheduled);
        writer.print(innerPrefix);
        writer.print("mTraversalScheduled=");
        writer.print(this.mTraversalScheduled);
        if (this.mTraversalScheduled) {
            writer.print(" (barrier=");
            writer.print(this.mTraversalBarrier);
            writer.println(")");
        } else {
            writer.println();
        }
        this.mFirstInputStage.dump(innerPrefix, writer);
        this.mChoreographer.dump(prefix, writer);
        writer.print(prefix);
        writer.println("View Hierarchy:");
        dumpViewHierarchy(innerPrefix, writer, this.mView);
    }

    private void dumpViewHierarchy(String prefix, PrintWriter writer, View view) {
        writer.print(prefix);
        if (view == null) {
            writer.println("null");
            return;
        }
        writer.println(view.toString());
        if (view instanceof ViewGroup) {
            ViewGroup grp = (ViewGroup) view;
            int N = grp.getChildCount();
            if (N > 0) {
                prefix = prefix + "  ";
                for (int i = 0; i < N; i++) {
                    dumpViewHierarchy(prefix, writer, grp.getChildAt(i));
                }
            }
        }
    }

    public void dumpGfxInfo(int[] info) {
        info[1] = 0;
        info[0] = 0;
        if (this.mView != null) {
            getGfxInfo(this.mView, info);
        }
    }

    private static void getGfxInfo(View view, int[] info) {
        RenderNode renderNode = view.mRenderNode;
        info[0] = info[0] + 1;
        if (renderNode != null) {
            info[1] = info[1] + renderNode.getDebugSize();
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                getGfxInfo(group.getChildAt(i), info);
            }
        }
    }

    boolean die(boolean immediate) {
        if (!immediate || this.mIsInTraversal) {
            if (this.mIsDrawing) {
                Log.e(TAG, "Attempting to destroy the window while drawing!\n  window=" + this + ", title=" + this.mWindowAttributes.getTitle());
            } else {
                destroyHardwareRenderer();
            }
            this.mHandler.sendEmptyMessage(3);
            return true;
        }
        doDie();
        return false;
    }

    void doDie() {
        boolean reportViewVisibilityChanged = true;
        checkThread();
        synchronized (this) {
            if (this.mRemoved) {
                return;
            }
            handleDispatchSurfaceDestroyDeferred();
            this.mRemoved = true;
            dispatchDetachedFromWindow();
            if (this.mAdded && !this.mFirst) {
                destroyHardwareRenderer();
                if (this.mView != null) {
                    int viewVisibility = this.mView.getVisibility();
                    if (this.mReportedViewVisibility == viewVisibility) {
                        reportViewVisibilityChanged = false;
                    }
                    if (this.mWindowAttributesChanged || reportViewVisibilityChanged) {
                        try {
                            if ((relayoutWindow(this.mWindowAttributes, viewVisibility, false, viewVisibility) & 2) != 0) {
                                this.mWindowSession.finishDrawing(this.mWindow);
                            }
                        } catch (RemoteException e) {
                        }
                    }
                    this.mSurface.release();
                }
            }
            this.mAdded = false;
            this.mChoreographer.removeRootContext();
            WindowManagerGlobal.getInstance().doRemoveView(this);
        }
    }

    public void requestUpdateConfiguration(Configuration config) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(18, config));
    }

    public void loadSystemProperties() {
        this.mHandler.post(new Runnable() {
            public void run() {
                ViewRootImpl.this.mProfileRendering = SystemProperties.getBoolean(ViewRootImpl.PROPERTY_PROFILE_RENDERING, false);
                ViewRootImpl.this.profileRendering(ViewRootImpl.this.mAttachInfo.mHasWindowFocus);
                if (ViewRootImpl.this.mAttachInfo.mHardwareRenderer != null && ViewRootImpl.this.mAttachInfo.mHardwareRenderer.loadSystemProperties()) {
                    ViewRootImpl.this.invalidate();
                }
                boolean layout = SystemProperties.getBoolean(View.DEBUG_LAYOUT_PROPERTY, false);
                if (layout != ViewRootImpl.this.mAttachInfo.mDebugLayout) {
                    ViewRootImpl.this.mAttachInfo.mDebugLayout = layout;
                    if (!ViewRootImpl.this.mHandler.hasMessages(22)) {
                        ViewRootImpl.this.mHandler.sendEmptyMessageDelayed(22, 200);
                    }
                }
            }
        });
    }

    private void destroyHardwareRenderer() {
        HardwareRenderer hardwareRenderer = this.mAttachInfo.mHardwareRenderer;
        if (hardwareRenderer != null) {
            if (this.mView != null) {
                hardwareRenderer.destroyHardwareResources(this.mView);
            }
            hardwareRenderer.destroy();
            hardwareRenderer.setRequested(false);
            this.mAttachInfo.mHardwareRenderer = null;
            this.mAttachInfo.mHardwareAccelerated = false;
        }
    }

    public void dispatchFinishInputConnection(InputConnection connection) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(12, connection));
    }

    public void dispatchResized(Rect frame, Rect overscanInsets, Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw, Configuration newConfig, Rect cocktailBarFrame) {
        Message msg = this.mHandler.obtainMessage(reportDraw ? 5 : 4);
        if (this.mTranslator != null) {
            this.mTranslator.translateRectInScreenToAppWindow(frame);
            this.mTranslator.translateRectInScreenToAppWindow(overscanInsets);
            this.mTranslator.translateRectInScreenToAppWindow(contentInsets);
            this.mTranslator.translateRectInScreenToAppWindow(visibleInsets);
        }
        SomeArgs args = SomeArgs.obtain();
        boolean sameProcessCall = Binder.getCallingPid() == Process.myPid();
        if (sameProcessCall) {
            frame = new Rect(frame);
        }
        args.arg1 = frame;
        if (sameProcessCall) {
            contentInsets = new Rect(contentInsets);
        }
        args.arg2 = contentInsets;
        if (sameProcessCall) {
            visibleInsets = new Rect(visibleInsets);
        }
        args.arg3 = visibleInsets;
        if (sameProcessCall && newConfig != null) {
            newConfig = new Configuration(newConfig);
        }
        args.arg4 = newConfig;
        if (sameProcessCall) {
            overscanInsets = new Rect(overscanInsets);
        }
        args.arg5 = overscanInsets;
        if (sameProcessCall) {
            stableInsets = new Rect(stableInsets);
        }
        args.arg6 = stableInsets;
        if (sameProcessCall) {
            outsets = new Rect(outsets);
        }
        args.arg7 = outsets;
        if (sameProcessCall) {
            cocktailBarFrame = new Rect(cocktailBarFrame);
        }
        args.arg8 = cocktailBarFrame;
        msg.obj = args;
        this.mHandler.sendMessage(msg);
    }

    public void dispatchMoved(int newX, int newY) {
        if (this.mTranslator != null) {
            PointF point = new PointF((float) newX, (float) newY);
            this.mTranslator.translatePointInScreenToAppWindow(point);
            newX = (int) (((double) point.x) + 0.5d);
            newY = (int) (((double) point.y) + 0.5d);
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(23, newX, newY));
    }

    private QueuedInputEvent obtainQueuedInputEvent(InputEvent event, InputEventReceiver receiver, int flags) {
        QueuedInputEvent q = this.mQueuedInputEventPool;
        if (q != null) {
            this.mQueuedInputEventPoolSize--;
            this.mQueuedInputEventPool = q.mNext;
            q.mNext = null;
        } else {
            q = new QueuedInputEvent();
        }
        q.mEvent = event;
        q.mReceiver = receiver;
        q.mFlags = flags;
        return q;
    }

    private void recycleQueuedInputEvent(QueuedInputEvent q) {
        q.mEvent = null;
        q.mReceiver = null;
        if (this.mQueuedInputEventPoolSize < 10) {
            this.mQueuedInputEventPoolSize++;
            q.mNext = this.mQueuedInputEventPool;
            this.mQueuedInputEventPool = q;
        }
    }

    void enqueueInputEvent(InputEvent event) {
        enqueueInputEvent(event, null, 0, false);
    }

    void enqueueInputEvent(InputEvent event, InputEventReceiver receiver, int flags, boolean processImmediately) {
        adjustInputEventForCompatibility(event);
        QueuedInputEvent q = obtainQueuedInputEvent(event, receiver, flags);
        QueuedInputEvent last = this.mPendingInputEventTail;
        if (last == null) {
            this.mPendingInputEventHead = q;
            this.mPendingInputEventTail = q;
        } else {
            last.mNext = q;
            this.mPendingInputEventTail = q;
        }
        this.mPendingInputEventCount++;
        Trace.traceCounter(4, this.mPendingInputEventQueueLengthCounterName, this.mPendingInputEventCount);
        if (processImmediately) {
            doProcessInputEvents();
        } else {
            scheduleProcessInputEvents();
        }
    }

    private void scheduleProcessInputEvents() {
        if (!this.mProcessInputEventsScheduled) {
            this.mProcessInputEventsScheduled = true;
            Message msg = this.mHandler.obtainMessage(19);
            msg.setAsynchronous(true);
            this.mHandler.sendMessage(msg);
        }
    }

    void doProcessInputEvents() {
        while (this.mPendingInputEventHead != null) {
            QueuedInputEvent q = this.mPendingInputEventHead;
            this.mPendingInputEventHead = q.mNext;
            if (this.mPendingInputEventHead == null) {
                this.mPendingInputEventTail = null;
            }
            q.mNext = null;
            this.mPendingInputEventCount--;
            Trace.traceCounter(4, this.mPendingInputEventQueueLengthCounterName, this.mPendingInputEventCount);
            long eventTime = q.mEvent.getEventTimeNano();
            long oldestEventTime = eventTime;
            if (q.mEvent instanceof MotionEvent) {
                MotionEvent me = q.mEvent;
                if (me.getHistorySize() > 0) {
                    oldestEventTime = me.getHistoricalEventTimeNano(0);
                }
            }
            this.mChoreographer.mFrameInfo.updateInputEventTime(eventTime, oldestEventTime);
            deliverInputEvent(q);
        }
        if (this.mProcessInputEventsScheduled) {
            this.mProcessInputEventsScheduled = false;
            this.mHandler.removeMessages(19);
        }
    }

    private void deliverInputEvent(QueuedInputEvent q) {
        MotionEvent event;
        InputStage stage;
        Trace.asyncTraceBegin(8, "deliverInputEvent", q.mEvent.getSequenceNumber());
        if (q.mEvent instanceof MotionEvent) {
            event = q.mEvent;
            boolean isFloating = this.mWindowAttributes.width == -2 && this.mWindowAttributes.height == -2;
            boolean isMinimode = this.mWindowAttributes.type == LayoutParams.TYPE_MINI_APP || this.mWindowAttributes.type == LayoutParams.TYPE_MINI_APP_DIALOG;
            if (isMinimode || isFloating || this.mContext == null || !this.mContext.isTouchBlocked()) {
                float xScale = 1.0f;
                float yScale = 1.0f;
                Point stackOffset = null;
                if (!(this.mScaleFactor.x == 1.0f && this.mScaleFactor.y == 1.0f)) {
                    stackOffset = getStackPosition();
                    if (stackOffset != null) {
                        xScale = 1.0f / this.mScaleFactor.x;
                        yScale = 1.0f / this.mScaleFactor.y;
                        this.mTmpMotionEventMatrix.setScale(xScale, yScale);
                        event.transform(this.mTmpMotionEventMatrix);
                    }
                }
                event.setScale(xScale, yScale);
                event.setDssScale(this.mContext.getApplicationInfo().dssScale);
                if (stackOffset != null) {
                    MultiWindowStyle multiWindowStyle = getMultiWindowStyle();
                    boolean isCascade = multiWindowStyle.isCascade();
                    boolean isMinimized = multiWindowStyle.isEnabled(4);
                    if (!isCascade || isMinimized || this.mAttachInfo == null || this.mAttachInfo.mWindowTop >= stackOffset.y) {
                        event.setScaledWindowOffset(stackOffset.x, stackOffset.y);
                    } else {
                        event.setScaledWindowOffset(stackOffset.x, this.mAttachInfo.mWindowTop);
                    }
                }
            } else {
                finishInputEvent(q);
                return;
            }
        }
        if ((q.mEvent instanceof MotionEvent) && !bFactoryBinary) {
            event = (MotionEvent) q.mEvent;
            if (checkPalmRejection(event) && getPalmRejection(event)) {
                event.setAction(3);
            }
        }
        if (this.mInputEventConsistencyVerifier != null) {
            this.mInputEventConsistencyVerifier.onInputEvent(q.mEvent, 0);
        }
        if (q.shouldSendToSynthesizer()) {
            stage = this.mSyntheticInputStage;
        } else {
            stage = q.shouldSkipIme() ? this.mFirstPostImeInputStage : this.mFirstInputStage;
        }
        if (stage != null) {
            stage.deliver(q);
        } else {
            finishInputEvent(q);
        }
    }

    private void finishInputEvent(QueuedInputEvent q) {
        Trace.asyncTraceEnd(8, "deliverInputEvent", q.mEvent.getSequenceNumber());
        if (q.mReceiver != null) {
            q.mReceiver.finishInputEvent(q.mEvent, (q.mFlags & 8) != 0);
        } else {
            q.mEvent.recycleIfNeededAfterDispatch();
        }
        recycleQueuedInputEvent(q);
    }

    private void adjustInputEventForCompatibility(InputEvent e) {
        if (this.mTargetSdkVersion < 23 && (e instanceof MotionEvent)) {
            MotionEvent motion = (MotionEvent) e;
            int buttonState = motion.getButtonState();
            int compatButtonState = (buttonState & 96) >> 4;
            if (compatButtonState != 0) {
                motion.setButtonState(buttonState | compatButtonState);
            }
        }
    }

    static boolean isTerminalInputEvent(InputEvent event) {
        boolean z = false;
        if (!(event instanceof KeyEvent)) {
            int action = ((MotionEvent) event).getAction();
            if (action == 1 || action == 3 || action == 10) {
                z = true;
            }
            return z;
        } else if (((KeyEvent) event).getAction() == 1) {
            return true;
        } else {
            return false;
        }
    }

    void scheduleConsumeBatchedInput() {
        if (!this.mConsumeBatchedInputScheduled) {
            this.mConsumeBatchedInputScheduled = true;
            this.mChoreographer.postCallback(0, this.mConsumedBatchedInputRunnable, null);
        }
    }

    void unscheduleConsumeBatchedInput() {
        if (this.mConsumeBatchedInputScheduled) {
            this.mConsumeBatchedInputScheduled = false;
            this.mChoreographer.removeCallbacks(0, this.mConsumedBatchedInputRunnable, null);
        }
    }

    void scheduleConsumeBatchedInputImmediately() {
        if (!this.mConsumeBatchedInputImmediatelyScheduled) {
            unscheduleConsumeBatchedInput();
            this.mConsumeBatchedInputImmediatelyScheduled = true;
            this.mHandler.post(this.mConsumeBatchedInputImmediatelyRunnable);
        }
    }

    void doConsumeBatchedInput(long frameTimeNanos) {
        if (this.mConsumeBatchedInputScheduled) {
            this.mConsumeBatchedInputScheduled = false;
            if (!(this.mInputEventReceiver == null || !this.mInputEventReceiver.consumeBatchedInputEvents(frameTimeNanos) || frameTimeNanos == -1)) {
                scheduleConsumeBatchedInput();
            }
            doProcessInputEvents();
        }
    }

    public void dispatchInvalidateDelayed(View view, long delayMilliseconds) {
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, view), delayMilliseconds);
    }

    public void dispatchInvalidateRectDelayed(InvalidateInfo info, long delayMilliseconds) {
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(2, info), delayMilliseconds);
    }

    public void dispatchInvalidateOnAnimation(View view) {
        this.mInvalidateOnAnimationRunnable.addView(view);
    }

    public void dispatchInvalidateRectOnAnimation(InvalidateInfo info) {
        this.mInvalidateOnAnimationRunnable.addViewRect(info);
    }

    public void cancelInvalidate(View view) {
        this.mHandler.removeMessages(1, view);
        this.mHandler.removeMessages(2, view);
        this.mInvalidateOnAnimationRunnable.removeView(view);
    }

    public void dispatchInputEvent(InputEvent event) {
        dispatchInputEvent(event, null);
    }

    public void dispatchInputEvent(InputEvent event, InputEventReceiver receiver) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = event;
        args.arg2 = receiver;
        Message msg = this.mHandler.obtainMessage(7, args);
        msg.setAsynchronous(true);
        this.mHandler.sendMessage(msg);
    }

    public void synthesizeInputEvent(InputEvent event) {
        Message msg = this.mHandler.obtainMessage(24, event);
        msg.setAsynchronous(true);
        this.mHandler.sendMessage(msg);
    }

    public void dispatchKeyFromIme(KeyEvent event) {
        Message msg = this.mHandler.obtainMessage(11, event);
        msg.setAsynchronous(true);
        this.mHandler.sendMessage(msg);
    }

    public void dispatchUnhandledInputEvent(InputEvent event) {
        if (event instanceof MotionEvent) {
            event = MotionEvent.obtain((MotionEvent) event);
        }
        synthesizeInputEvent(event);
    }

    public void dispatchAppVisibility(boolean visible) {
        Message msg = this.mHandler.obtainMessage(8);
        msg.arg1 = visible ? 1 : 0;
        this.mHandler.sendMessage(msg);
    }

    public void dispatchGetNewSurface() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(9));
    }

    public void windowFocusChanged(boolean hasFocus, boolean inTouchMode) {
        windowFocusChanged(hasFocus, inTouchMode, false);
    }

    public void windowFocusChanged(boolean hasFocus, boolean inTouchMode, boolean focusedAppChanged) {
        int i = 1;
        Message msg = Message.obtain();
        msg.what = 6;
        msg.arg1 = hasFocus ? 1 : 0;
        if (!inTouchMode) {
            i = 0;
        }
        msg.arg2 = i;
        msg.obj = focusedAppChanged ? Boolean.TRUE : Boolean.FALSE;
        this.mHandler.sendMessage(msg);
    }

    public void dispatchWindowShown() {
        this.mHandler.sendEmptyMessage(25);
    }

    public void dispatchCloseSystemDialogs(String reason) {
        Message msg = Message.obtain();
        msg.what = 14;
        msg.obj = reason;
        this.mHandler.sendMessage(msg);
    }

    public void dispatchDragEvent(DragEvent event) {
        int what;
        if (event.getAction() == 2) {
            what = 16;
            this.mHandler.removeMessages(16);
        } else {
            what = 15;
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(what, event));
    }

    public void dispatchSystemUiVisibilityChanged(int seq, int globalVisibility, int localValue, int localChanges) {
        SystemUiVisibilityInfo args = new SystemUiVisibilityInfo();
        args.seq = seq;
        args.globalVisibility = globalVisibility;
        args.localValue = localValue;
        args.localChanges = localChanges;
        this.mHandler.sendMessage(this.mHandler.obtainMessage(17, args));
    }

    public void dispatchWindowAnimationStarted(int remainingFrameCount) {
        this.mHandler.obtainMessage(27, remainingFrameCount, 0).sendToTarget();
    }

    public void dispatchWindowAnimationStopped() {
        this.mHandler.sendEmptyMessage(26);
    }

    public void dispatchCheckFocus() {
        if (!this.mHandler.hasMessages(13)) {
            this.mHandler.sendEmptyMessage(13);
        }
    }

    public void dispatchSmartClipRemoteRequest(SmartClipRemoteRequestInfo request) {
        if (this.mSmartClipDispatcherProxy != null) {
            this.mSmartClipDispatcherProxy.dispatchSmartClipRemoteRequest(request);
        } else {
            Log.e(TAG, "dispatchSmartClipRemoteRequest : SmartClip dispatcher is null! req id=" + request.mRequestId + " type=" + request.mRequestType);
        }
    }

    private void dispatchCoverStateChanged(boolean isOpen) {
        Message msg = Message.obtain();
        msg.what = 29;
        msg.arg1 = isOpen ? 1 : 0;
        this.mHandler.sendMessage(msg);
    }

    private void dispatchSurfaceDestroyDeferred() {
        this.mHandler.sendEmptyMessage(30);
    }

    private void dispatchMultiWindowStateChanged(int state) {
        Message msg = Message.obtain();
        msg.what = 31;
        msg.arg1 = state;
        msg.arg2 = 0;
        this.mHandler.sendMessage(msg);
    }

    private void postSendWindowContentChangedCallback(View source, int changeType) {
        if (this.mSendWindowContentChangedAccessibilityEvent == null) {
            this.mSendWindowContentChangedAccessibilityEvent = new SendWindowContentChangedAccessibilityEvent();
        }
        this.mSendWindowContentChangedAccessibilityEvent.runOrPost(source, changeType);
    }

    private void removeSendWindowContentChangedCallback() {
        if (this.mSendWindowContentChangedAccessibilityEvent != null) {
            this.mHandler.removeCallbacks(this.mSendWindowContentChangedAccessibilityEvent);
        }
    }

    public boolean showContextMenuForChild(View originalView) {
        return false;
    }

    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback) {
        return null;
    }

    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback, int type) {
        return null;
    }

    public void createContextMenu(ContextMenu menu) {
    }

    public void childDrawableStateChanged(View child) {
    }

    public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        if (this.mView == null || this.mStopped || this.mPausedForTransition) {
            return false;
        }
        View source;
        switch (event.getEventType()) {
            case 2048:
                handleWindowContentChangedEvent(event);
                break;
            case 32768:
                long sourceNodeId = event.getSourceNodeId();
                source = this.mView.findViewByAccessibilityId(AccessibilityNodeInfo.getAccessibilityViewId(sourceNodeId));
                if (source != null) {
                    AccessibilityNodeProvider provider = source.getAccessibilityNodeProvider();
                    if (provider != null) {
                        AccessibilityNodeInfo node;
                        int virtualNodeId = AccessibilityNodeInfo.getVirtualDescendantId(sourceNodeId);
                        if (virtualNodeId == Integer.MAX_VALUE) {
                            node = provider.createAccessibilityNodeInfo(-1);
                        } else {
                            node = provider.createAccessibilityNodeInfo(virtualNodeId);
                        }
                        setAccessibilityFocus(source, node);
                        break;
                    }
                }
                break;
            case 65536:
                source = this.mView.findViewByAccessibilityId(AccessibilityNodeInfo.getAccessibilityViewId(event.getSourceNodeId()));
                if (!(source == null || source.getAccessibilityNodeProvider() == null)) {
                    setAccessibilityFocus(null, null);
                    break;
                }
        }
        this.mAccessibilityManager.sendAccessibilityEvent(event);
        return true;
    }

    private void handleWindowContentChangedEvent(AccessibilityEvent event) {
        View focusedHost = this.mAccessibilityFocusedHost;
        if (focusedHost != null && this.mAccessibilityFocusedVirtualView != null) {
            AccessibilityNodeProvider provider = focusedHost.getAccessibilityNodeProvider();
            if (provider == null) {
                this.mAccessibilityFocusedHost = null;
                this.mAccessibilityFocusedVirtualView = null;
                focusedHost.clearAccessibilityFocusNoCallbacks();
                return;
            }
            int changes = event.getContentChangeTypes();
            if ((changes & 1) != 0 || changes == 0) {
                int changedViewId = AccessibilityNodeInfo.getAccessibilityViewId(event.getSourceNodeId());
                boolean hostInSubtree = false;
                View root = this.mAccessibilityFocusedHost;
                while (root != null && !hostInSubtree) {
                    if (changedViewId == root.getAccessibilityViewId()) {
                        hostInSubtree = true;
                    } else {
                        ViewParent parent = root.getParent();
                        if (parent instanceof View) {
                            root = (View) parent;
                        } else {
                            root = null;
                        }
                    }
                }
                if (hostInSubtree) {
                    int focusedChildId = AccessibilityNodeInfo.getVirtualDescendantId(this.mAccessibilityFocusedVirtualView.getSourceNodeId());
                    if (focusedChildId == Integer.MAX_VALUE) {
                        focusedChildId = -1;
                    }
                    Rect oldBounds = this.mTempRect;
                    this.mAccessibilityFocusedVirtualView.getBoundsInScreen(oldBounds);
                    this.mAccessibilityFocusedVirtualView = provider.createAccessibilityNodeInfo(focusedChildId);
                    if (this.mAccessibilityFocusedVirtualView == null) {
                        this.mAccessibilityFocusedHost = null;
                        focusedHost.clearAccessibilityFocusNoCallbacks();
                        provider.performAction(focusedChildId, AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS.getId(), null);
                        invalidateRectOnScreen(oldBounds);
                        return;
                    }
                    Rect newBounds = this.mAccessibilityFocusedVirtualView.getBoundsInScreen();
                    if (!oldBounds.equals(newBounds)) {
                        oldBounds.union(newBounds);
                        invalidateRectOnScreen(oldBounds);
                    }
                }
            }
        }
    }

    public void notifySubtreeAccessibilityStateChanged(View child, View source, int changeType) {
        postSendWindowContentChangedCallback(source, changeType);
    }

    public boolean canResolveLayoutDirection() {
        return true;
    }

    public boolean isLayoutDirectionResolved() {
        return true;
    }

    public int getLayoutDirection() {
        return 0;
    }

    public boolean canResolveTextDirection() {
        return true;
    }

    public boolean isTextDirectionResolved() {
        return true;
    }

    public int getTextDirection() {
        return 1;
    }

    public boolean canResolveTextAlignment() {
        return true;
    }

    public boolean isTextAlignmentResolved() {
        return true;
    }

    public int getTextAlignment() {
        return 1;
    }

    private View getCommonPredecessor(View first, View second) {
        if (this.mTempHashSet == null) {
            this.mTempHashSet = new HashSet();
        }
        HashSet<View> seen = this.mTempHashSet;
        seen.clear();
        View firstCurrent = first;
        while (firstCurrent != null) {
            seen.add(firstCurrent);
            ViewParent firstCurrentParent = firstCurrent.mParent;
            if (firstCurrentParent instanceof View) {
                firstCurrent = (View) firstCurrentParent;
            } else {
                firstCurrent = null;
            }
        }
        View secondCurrent = second;
        while (secondCurrent != null) {
            if (seen.contains(secondCurrent)) {
                seen.clear();
                return secondCurrent;
            }
            ViewParent secondCurrentParent = secondCurrent.mParent;
            if (secondCurrentParent instanceof View) {
                secondCurrent = (View) secondCurrentParent;
            } else {
                secondCurrent = null;
            }
        }
        seen.clear();
        return null;
    }

    void checkThread() {
        if (this.mThread != Thread.currentThread()) {
            throw new CalledFromWrongThreadException("Only the original thread that created a view hierarchy can touch its views.");
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        boolean scrolled = scrollToRectOrFocus(rectangle, immediate);
        if (rectangle != null) {
            this.mTempRect.set(rectangle);
            this.mTempRect.offset(0, -this.mCurScrollY);
            this.mTempRect.offset(this.mAttachInfo.mWindowLeft, this.mAttachInfo.mWindowTop);
            try {
                this.mWindowSession.onRectangleOnScreenRequested(this.mWindow, this.mTempRect);
            } catch (RemoteException e) {
            }
        }
        return scrolled;
    }

    public void childHasTransientStateChanged(View child, boolean hasTransientState) {
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return false;
    }

    public void onStopNestedScroll(View target) {
    }

    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    public boolean onNestedPrePerformAccessibilityAction(View target, int action, Bundle args) {
        return false;
    }

    public void setReportNextDraw() {
        this.mReportNextDraw = true;
        invalidate();
    }

    void changeCanvasOpacity(boolean opaque) {
        Log.d(TAG, "changeCanvasOpacity: opaque=" + opaque);
        if (this.mAttachInfo.mHardwareRenderer != null) {
            this.mAttachInfo.mHardwareRenderer.setOpaque(opaque);
        }
    }

    static RunQueue getRunQueue() {
        RunQueue rq = (RunQueue) sRunQueues.get();
        if (rq != null) {
            return rq;
        }
        rq = new RunQueue();
        sRunQueues.set(rq);
        return rq;
    }

    public void dispatchAttachedDisplayChanged(int displayId) {
    }

    private void sendUserActionEvent() {
        if (this.mView == null) {
            Log.e(TAG, "sendUserActionEvent() mView == null");
        } else if (SmartFaceManager.PAGE_BOTTOM.equals(MultiSimManager.getTelephonyProperty("gsm.sim.userEvent", 0, SmartFaceManager.PAGE_MIDDLE)) || SmartFaceManager.PAGE_BOTTOM.equals(MultiSimManager.getTelephonyProperty("gsm.sim.userEvent", 1, SmartFaceManager.PAGE_MIDDLE))) {
            CatEventDownload catEventUserActivity = new CatEventDownload(4);
            Intent intent = new Intent(CatEventDownload.STK_EVENT_ACTION);
            intent.putExtra("STK EVENT", catEventUserActivity);
            this.mView.getContext().sendBroadcast(intent);
        }
    }

    private void doRelayoutForHCT(boolean fromHandler) {
        if (this.mThread == Thread.currentThread()) {
            destroyHardwareResources();
            resetSoftwareCaches(this.mView);
            setHCTLetterSpacing(this.mView, sIsHighContrastTextEnabled);
            invalidate();
            requestLayout();
            if (this.mView != null) {
                forceLayout(this.mView);
            }
        } else if (fromHandler) {
            Log.d(TAG, "Recursion detected");
        } else {
            this.mHCTRelayoutHandler.sendEmptyMessage(1);
        }
    }

    public MotionEventMonitor getMotionEventMonitor() {
        return this.mMotionEventMonitor;
    }

    public View getCurrentWritingBuddyView() {
        return this.mCurrentWritingBuddyView;
    }

    public void setCurrentWritingBuddyView(View view) {
        this.mCurrentWritingBuddyView = view;
    }

    public MultiWindowStyle getMultiWindowStyle() {
        if (this.mContext == null) {
            return MultiWindowStyle.sConstDefaultMultiWindowStyle;
        }
        if (this.mContext instanceof Activity) {
            return ((Activity) this.mContext).getMultiWindowStyle();
        }
        return this.mContext.getAppMultiWindowStyle();
    }

    private Point getStackPosition() {
        if (this.mContext == null) {
            return null;
        }
        Rect bound = getMultiWindowStyle().getBounds();
        if (bound != null) {
            return new Point(bound.left, bound.top);
        }
        return null;
    }

    public PointF getMultiWindowScale() {
        return this.mScaleFactor;
    }

    public void setMultiWindowScale(float hScale, float vScale) {
        if ((this.mDisplay == null || this.mDisplay.getDisplayId() == 0) && this.mWindowAttributes.type != LayoutParams.TYPE_MINI_APP && this.mWindowAttributes.type != LayoutParams.TYPE_MINI_APP_ON_KEYGUARD) {
            if (this.mScaleFactor.x != hScale || this.mScaleFactor.y != vScale) {
                MultiWindowStyle multiWindowStyle = getMultiWindowStyle();
                if (!multiWindowStyle.isEnabled(2048) || multiWindowStyle.isEnabled(4)) {
                    this.mScaleFactor.x = 1.0f;
                    this.mScaleFactor.y = 1.0f;
                    return;
                }
                this.mNewScaleFactorNeeded = true;
                this.mScaleFactor.x = hScale;
                this.mScaleFactor.y = vScale;
            }
        }
    }

    public void setTransparentRegion(Region region) {
        try {
            this.mWindowSession.setTransparentRegion(this.mWindow, region);
        } catch (RemoteException e) {
        }
    }

    public void requestOnStylusButtonEvent(MotionEvent event) {
        this.mAttachInfo.mTreeObserver.dispatchOnPenButtonEventListener(event, -1);
    }

    public void setUseGestureDetectorEx(boolean flag) {
        mUseGestureDetectorTouchEventEx = flag;
    }

    public Rect getCocktailBarRect() {
        return this.mCocktailBar;
    }

    public void setForcePerformDraw(boolean forceDraw) {
        Log.d(TAG, "setForcePerformDraw():" + forceDraw);
        this.mForceDraw = forceDraw;
    }

    private void initContentResizeAnimator() {
        this.mContentResizeAnimator = new ValueAnimator();
        this.mContentResizeAnimator.setDuration(300);
        this.mContentResizeAnimator.addListener(new AnimatorListener() {
            public void onAnimationStart(Animator animation) {
                ViewRootImpl.this.mResizeAnimating = true;
            }

            public void onAnimationRepeat(Animator animation) {
            }

            public void onAnimationEnd(Animator animation) {
                onEndAnimation();
            }

            public void onAnimationCancel(Animator animation) {
                onEndAnimation();
            }

            private void onEndAnimation() {
                ViewRootImpl.this.mResizeAnimating = false;
                if (ViewRootImpl.this.mView != null) {
                    ViewRootImpl.this.mView.dispatchSipResizeAnimationState(false);
                }
            }
        });
        this.mContentResizeAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                int bottom = ((Integer) animation.getAnimatedValue()).intValue();
                ViewRootImpl.this.mPendingContentInsets.bottom = bottom;
                ViewRootImpl.this.mPendingVisibleInsets.bottom = bottom;
                ViewRootImpl.this.requestLayout();
            }
        });
        this.mShowInterpolator = new CircEaseInOut();
        this.mHideInterpolator = new SineEaseIn();
        if (this.mResizeBooster == null) {
            this.mResizeBooster = new DVFSHelper(this.mContext, "ROTATION_BOOSTER", 12, 0);
        }
        if (this.mResizeBooster != null) {
            this.mResizeBooster.addExtraOptionsByDefaultPolicy("PhoneWindowManager_rotation");
        }
    }

    private void startContentResizeAnimation() {
        cancelContentResizeAnimation();
        int from = this.mPreContentInsets.bottom;
        int to = this.mPendingContentInsets.bottom;
        this.mPendingContentInsets.set(this.mPreContentInsets);
        this.mPendingVisibleInsets.set(this.mPreContentInsets);
        this.mContentResizeAnimator.setIntValues(new int[]{from, to});
        this.mResizeAnimating = true;
        if (from < to) {
            this.mContentResizeAnimator.setInterpolator(this.mShowInterpolator);
        } else {
            this.mContentResizeAnimator.setInterpolator(this.mHideInterpolator);
        }
        if (SAFE_DEBUG) {
            Slog.i(TAG, "\"" + this.mWindowAttributes.getTitle() + "\" called content resize anim, from:" + from + " to:" + to);
        }
        this.mContentResizeAnimator.start();
        if (this.mView != null) {
            this.mView.dispatchSipResizeAnimationState(true);
        }
    }

    private void cancelContentResizeAnimation() {
        if (this.mResizeAnimating) {
            this.mContentResizeAnimator.cancel();
        }
    }

    private void acquireContentResizeAnimationBooster() {
        if (this.mResizeBooster != null) {
            this.mResizeBooster.acquire(DVFSHelper.PWM_ROTATION_BOOST_TIMEOUT);
            this.mResizeBooster.onWindowRotationEvent(this.mContext, "ROTATION_BOOSTER");
        }
    }
}
