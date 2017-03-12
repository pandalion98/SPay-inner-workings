package android.view;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.CompatibilityInfo.Translator;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Region.Op;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceHolder.Callback2;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.WindowManager.LayoutParams;
import com.android.internal.view.BaseIWindow;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class SurfaceView extends View {
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_DCS = "eng".equals(Build.TYPE);
    private static final boolean DYNAMIC_COLOR_SCALING_ENABLED = true;
    static final int GET_NEW_SURFACE_MSG = 2;
    static final int KEEP_SCREEN_ON_MSG = 1;
    private static final String TAG = "SurfaceView";
    private static final String TAG_DCS = "SRIB_DCS:SurfaceView";
    static final int UPDATE_WINDOW_MSG = 3;
    final ArrayList<Callback> mCallbacks;
    final Configuration mConfiguration;
    final Rect mContentInsets;
    private int mCurrentOrientation;
    private final OnPreDrawListener mDrawListener;
    boolean mDrawingStopped;
    int mFormat;
    private boolean mGlobalListenersAdded;
    final Handler mHandler;
    boolean mHaveFrame;
    int mHeight;
    boolean mIsCreating;
    private boolean mIsDcsEnabledApp;
    private boolean mIsFixedOrientation;
    public int mIsSixteenBitApp;
    long mLastLockTime;
    int mLastSurfaceHeight;
    int mLastSurfaceWidth;
    final LayoutParams mLayout;
    int mLeft;
    final int[] mLocation;
    private boolean mNeedForceDrawAtSetFrame;
    final Surface mNewSurface;
    final Rect mOutsets;
    final Rect mOverscanInsets;
    boolean mReportDrawNeeded;
    int mRequestedFormat;
    int mRequestedHeight;
    boolean mRequestedVisible;
    int mRequestedWidth;
    private int mRequestedX;
    private int mRequestedY;
    private int mSVBufferCount;
    final OnScrollChangedListener mScrollChangedListener;
    IWindowSession mSession;
    final Rect mStableInsets;
    final Surface mSurface;
    boolean mSurfaceCreated;
    final Rect mSurfaceFrame;
    private final SurfaceHolder mSurfaceHolder;
    final ReentrantLock mSurfaceLock;
    int mTargetHeight;
    int mTargetWidth;
    int mTop;
    private Translator mTranslator;
    boolean mUpdateWindowNeeded;
    boolean mViewVisibility;
    boolean mVisible;
    final Rect mVisibleInsets;
    int mWidth;
    final Rect mWinFrame;
    MyWindow mWindow;
    int mWindowType;
    boolean mWindowVisibility;

    private static class MyWindow extends BaseIWindow {
        int mCurHeight = -1;
        int mCurWidth = -1;
        private final WeakReference<SurfaceView> mSurfaceView;

        public MyWindow(SurfaceView surfaceView) {
            this.mSurfaceView = new WeakReference(surfaceView);
        }

        public void resized(Rect frame, Rect overscanInsets, Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw, Configuration newConfig, Rect cocktailBarFrame) {
            SurfaceView surfaceView = (SurfaceView) this.mSurfaceView.get();
            if (surfaceView != null) {
                surfaceView.mSurfaceLock.lock();
                if (reportDraw) {
                    try {
                        surfaceView.mUpdateWindowNeeded = true;
                        surfaceView.mReportDrawNeeded = true;
                        surfaceView.mHandler.sendEmptyMessage(3);
                    } catch (Throwable th) {
                        surfaceView.mSurfaceLock.unlock();
                    }
                } else if (!(surfaceView.mWinFrame.width() == frame.width() && surfaceView.mWinFrame.height() == frame.height())) {
                    surfaceView.mUpdateWindowNeeded = true;
                    surfaceView.mHandler.sendEmptyMessage(3);
                }
                surfaceView.mSurfaceLock.unlock();
            }
        }

        public void dispatchAppVisibility(boolean visible) {
        }

        public void dispatchGetNewSurface() {
            SurfaceView surfaceView = (SurfaceView) this.mSurfaceView.get();
            if (surfaceView != null) {
                surfaceView.mHandler.sendMessage(surfaceView.mHandler.obtainMessage(2));
            }
        }

        public void windowFocusChanged(boolean hasFocus, boolean touchEnabled, boolean focusedAppChanged) {
            Log.w(SurfaceView.TAG, "Unexpected focus in surface: focus=" + hasFocus + ", touchEnabled=" + touchEnabled);
        }

        public void executeCommand(String command, String parameters, ParcelFileDescriptor out) {
        }
    }

    public SurfaceView(Context context) {
        super(context);
        this.mCallbacks = new ArrayList();
        this.mLocation = new int[2];
        this.mSurfaceLock = new ReentrantLock(true);
        this.mSurface = new Surface();
        this.mNewSurface = new Surface();
        this.mDrawingStopped = true;
        this.mLayout = new LayoutParams();
        this.mVisibleInsets = new Rect();
        this.mWinFrame = new Rect();
        this.mOverscanInsets = new Rect();
        this.mContentInsets = new Rect();
        this.mStableInsets = new Rect();
        this.mOutsets = new Rect();
        this.mConfiguration = new Configuration();
        this.mSVBufferCount = -1;
        this.mIsSixteenBitApp = 0;
        this.mIsDcsEnabledApp = false;
        this.mWindowType = 1001;
        this.mIsCreating = false;
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                boolean z = false;
                switch (msg.what) {
                    case 1:
                        SurfaceView surfaceView = SurfaceView.this;
                        if (msg.arg1 != 0) {
                            z = true;
                        }
                        surfaceView.setKeepScreenOn(z);
                        return;
                    case 2:
                        SurfaceView.this.handleGetNewSurface();
                        return;
                    case 3:
                        SurfaceView.this.updateWindow(false, false);
                        return;
                    default:
                        return;
                }
            }
        };
        this.mScrollChangedListener = new OnScrollChangedListener() {
            public void onScrollChanged() {
                SurfaceView.this.updateWindow(false, false);
            }
        };
        this.mRequestedVisible = false;
        this.mWindowVisibility = false;
        this.mViewVisibility = false;
        this.mRequestedWidth = -1;
        this.mRequestedHeight = -1;
        this.mTargetWidth = -1;
        this.mTargetHeight = -1;
        this.mRequestedFormat = 4;
        this.mHaveFrame = false;
        this.mSurfaceCreated = false;
        this.mLastLockTime = 0;
        this.mVisible = false;
        this.mLeft = -1;
        this.mTop = -1;
        this.mWidth = -1;
        this.mHeight = -1;
        this.mFormat = -1;
        this.mSurfaceFrame = new Rect();
        this.mLastSurfaceWidth = -1;
        this.mLastSurfaceHeight = -1;
        this.mDrawListener = new OnPreDrawListener() {
            public boolean onPreDraw() {
                boolean z;
                SurfaceView surfaceView = SurfaceView.this;
                if (SurfaceView.this.getWidth() <= 0 || SurfaceView.this.getHeight() <= 0) {
                    z = false;
                } else {
                    z = true;
                }
                surfaceView.mHaveFrame = z;
                SurfaceView.this.updateWindow(false, false);
                return true;
            }
        };
        this.mCurrentOrientation = -1;
        this.mNeedForceDrawAtSetFrame = false;
        this.mSurfaceHolder = new SurfaceHolder() {
            private static final String LOG_TAG = "SurfaceHolder";

            public boolean isCreating() {
                return SurfaceView.this.mIsCreating;
            }

            public void addCallback(Callback callback) {
                synchronized (SurfaceView.this.mCallbacks) {
                    if (!SurfaceView.this.mCallbacks.contains(callback)) {
                        SurfaceView.this.mCallbacks.add(callback);
                    }
                }
            }

            public void removeCallback(Callback callback) {
                synchronized (SurfaceView.this.mCallbacks) {
                    SurfaceView.this.mCallbacks.remove(callback);
                }
            }

            public void setFixedSize(int width, int height) {
                if (SurfaceView.this.mRequestedWidth != width || SurfaceView.this.mRequestedHeight != height) {
                    SurfaceView.this.mRequestedWidth = width;
                    SurfaceView.this.mRequestedHeight = height;
                    SurfaceView.this.requestLayout();
                }
            }

            public void setSizeFromLayout() {
                if (SurfaceView.this.mRequestedWidth != -1 || SurfaceView.this.mRequestedHeight != -1) {
                    SurfaceView surfaceView = SurfaceView.this;
                    SurfaceView.this.mRequestedHeight = -1;
                    surfaceView.mRequestedWidth = -1;
                    SurfaceView.this.requestLayout();
                }
            }

            public void setFormat(int format) {
                if (format == -1) {
                    format = 4;
                }
                SurfaceView.this.mRequestedFormat = format;
                if (SurfaceView.this.mIsDcsEnabledApp && SurfaceView.this.mFormat != -1) {
                    SurfaceView.this.mRequestedFormat = SurfaceView.this.mFormat;
                }
                if (SurfaceView.this.mWindow != null) {
                    SurfaceView.this.updateWindow(false, false);
                }
            }

            @Deprecated
            public void setType(int type) {
            }

            public void setKeepScreenOn(boolean screenOn) {
                int i = 1;
                Message msg = SurfaceView.this.mHandler.obtainMessage(1);
                if (!screenOn) {
                    i = 0;
                }
                msg.arg1 = i;
                SurfaceView.this.mHandler.sendMessage(msg);
            }

            public Canvas lockCanvas() {
                return internalLockCanvas(null);
            }

            public Canvas lockCanvas(Rect inOutDirty) {
                return internalLockCanvas(inOutDirty);
            }

            private final Canvas internalLockCanvas(Rect dirty) {
                SurfaceView.this.mSurfaceLock.lock();
                Canvas c = null;
                if (!(SurfaceView.this.mDrawingStopped || SurfaceView.this.mWindow == null)) {
                    try {
                        c = SurfaceView.this.mSurface.lockCanvas(dirty);
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "Exception locking surface", e);
                    }
                }
                if (c != null) {
                    SurfaceView.this.mLastLockTime = SystemClock.uptimeMillis();
                    return c;
                }
                long now = SystemClock.uptimeMillis();
                long nextTime = SurfaceView.this.mLastLockTime + 100;
                if (nextTime > now) {
                    try {
                        Thread.sleep(nextTime - now);
                    } catch (InterruptedException e2) {
                    }
                    now = SystemClock.uptimeMillis();
                }
                SurfaceView.this.mLastLockTime = now;
                SurfaceView.this.mSurfaceLock.unlock();
                return null;
            }

            public void unlockCanvasAndPost(Canvas canvas) {
                SurfaceView.this.mSurface.unlockCanvasAndPost(canvas);
                SurfaceView.this.mSurfaceLock.unlock();
            }

            public Surface getSurface() {
                return SurfaceView.this.mSurface;
            }

            public Rect getSurfaceFrame() {
                return SurfaceView.this.mSurfaceFrame;
            }
        };
        this.mIsFixedOrientation = false;
        this.mRequestedX = 0;
        this.mRequestedY = 0;
        init(context);
    }

    public SurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mCallbacks = new ArrayList();
        this.mLocation = new int[2];
        this.mSurfaceLock = new ReentrantLock(true);
        this.mSurface = new Surface();
        this.mNewSurface = new Surface();
        this.mDrawingStopped = true;
        this.mLayout = new LayoutParams();
        this.mVisibleInsets = new Rect();
        this.mWinFrame = new Rect();
        this.mOverscanInsets = new Rect();
        this.mContentInsets = new Rect();
        this.mStableInsets = new Rect();
        this.mOutsets = new Rect();
        this.mConfiguration = new Configuration();
        this.mSVBufferCount = -1;
        this.mIsSixteenBitApp = 0;
        this.mIsDcsEnabledApp = false;
        this.mWindowType = 1001;
        this.mIsCreating = false;
        this.mHandler = /* anonymous class already generated */;
        this.mScrollChangedListener = /* anonymous class already generated */;
        this.mRequestedVisible = false;
        this.mWindowVisibility = false;
        this.mViewVisibility = false;
        this.mRequestedWidth = -1;
        this.mRequestedHeight = -1;
        this.mTargetWidth = -1;
        this.mTargetHeight = -1;
        this.mRequestedFormat = 4;
        this.mHaveFrame = false;
        this.mSurfaceCreated = false;
        this.mLastLockTime = 0;
        this.mVisible = false;
        this.mLeft = -1;
        this.mTop = -1;
        this.mWidth = -1;
        this.mHeight = -1;
        this.mFormat = -1;
        this.mSurfaceFrame = new Rect();
        this.mLastSurfaceWidth = -1;
        this.mLastSurfaceHeight = -1;
        this.mDrawListener = /* anonymous class already generated */;
        this.mCurrentOrientation = -1;
        this.mNeedForceDrawAtSetFrame = false;
        this.mSurfaceHolder = /* anonymous class already generated */;
        this.mIsFixedOrientation = false;
        this.mRequestedX = 0;
        this.mRequestedY = 0;
        init(context);
    }

    public SurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mCallbacks = new ArrayList();
        this.mLocation = new int[2];
        this.mSurfaceLock = new ReentrantLock(true);
        this.mSurface = new Surface();
        this.mNewSurface = new Surface();
        this.mDrawingStopped = true;
        this.mLayout = new LayoutParams();
        this.mVisibleInsets = new Rect();
        this.mWinFrame = new Rect();
        this.mOverscanInsets = new Rect();
        this.mContentInsets = new Rect();
        this.mStableInsets = new Rect();
        this.mOutsets = new Rect();
        this.mConfiguration = new Configuration();
        this.mSVBufferCount = -1;
        this.mIsSixteenBitApp = 0;
        this.mIsDcsEnabledApp = false;
        this.mWindowType = 1001;
        this.mIsCreating = false;
        this.mHandler = /* anonymous class already generated */;
        this.mScrollChangedListener = /* anonymous class already generated */;
        this.mRequestedVisible = false;
        this.mWindowVisibility = false;
        this.mViewVisibility = false;
        this.mRequestedWidth = -1;
        this.mRequestedHeight = -1;
        this.mTargetWidth = -1;
        this.mTargetHeight = -1;
        this.mRequestedFormat = 4;
        this.mHaveFrame = false;
        this.mSurfaceCreated = false;
        this.mLastLockTime = 0;
        this.mVisible = false;
        this.mLeft = -1;
        this.mTop = -1;
        this.mWidth = -1;
        this.mHeight = -1;
        this.mFormat = -1;
        this.mSurfaceFrame = new Rect();
        this.mLastSurfaceWidth = -1;
        this.mLastSurfaceHeight = -1;
        this.mDrawListener = /* anonymous class already generated */;
        this.mCurrentOrientation = -1;
        this.mNeedForceDrawAtSetFrame = false;
        this.mSurfaceHolder = /* anonymous class already generated */;
        this.mIsFixedOrientation = false;
        this.mRequestedX = 0;
        this.mRequestedY = 0;
        init(context);
    }

    public SurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCallbacks = new ArrayList();
        this.mLocation = new int[2];
        this.mSurfaceLock = new ReentrantLock(true);
        this.mSurface = new Surface();
        this.mNewSurface = new Surface();
        this.mDrawingStopped = true;
        this.mLayout = new LayoutParams();
        this.mVisibleInsets = new Rect();
        this.mWinFrame = new Rect();
        this.mOverscanInsets = new Rect();
        this.mContentInsets = new Rect();
        this.mStableInsets = new Rect();
        this.mOutsets = new Rect();
        this.mConfiguration = new Configuration();
        this.mSVBufferCount = -1;
        this.mIsSixteenBitApp = 0;
        this.mIsDcsEnabledApp = false;
        this.mWindowType = 1001;
        this.mIsCreating = false;
        this.mHandler = /* anonymous class already generated */;
        this.mScrollChangedListener = /* anonymous class already generated */;
        this.mRequestedVisible = false;
        this.mWindowVisibility = false;
        this.mViewVisibility = false;
        this.mRequestedWidth = -1;
        this.mRequestedHeight = -1;
        this.mTargetWidth = -1;
        this.mTargetHeight = -1;
        this.mRequestedFormat = 4;
        this.mHaveFrame = false;
        this.mSurfaceCreated = false;
        this.mLastLockTime = 0;
        this.mVisible = false;
        this.mLeft = -1;
        this.mTop = -1;
        this.mWidth = -1;
        this.mHeight = -1;
        this.mFormat = -1;
        this.mSurfaceFrame = new Rect();
        this.mLastSurfaceWidth = -1;
        this.mLastSurfaceHeight = -1;
        this.mDrawListener = /* anonymous class already generated */;
        this.mCurrentOrientation = -1;
        this.mNeedForceDrawAtSetFrame = false;
        this.mSurfaceHolder = /* anonymous class already generated */;
        this.mIsFixedOrientation = false;
        this.mRequestedX = 0;
        this.mRequestedY = 0;
        init(context);
    }

    private void init() {
        setWillNotDraw(true);
    }

    private void init(Context ctx) {
        try {
            PackageInfo pi = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            this.mIsDcsEnabledApp = pi.isDcsEnabledApp;
            this.mFormat = pi.dcsFormat;
            if (this.mIsDcsEnabledApp && this.mFormat == 4) {
                if (DEBUG_DCS) {
                    Log.d(TAG_DCS, "Setting top Z-order for RGB565 for package: " + ctx.getPackageName() + "Format" + this.mFormat);
                }
                setZOrderOnTop(true);
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG_DCS, "SV init NameNotFoundException");
        }
        init();
    }

    public SurfaceHolder getHolder() {
        return this.mSurfaceHolder;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mParent.requestTransparentRegion(this);
        this.mSession = getWindowSession();
        this.mLayout.token = getWindowToken();
        this.mLayout.setTitle(TAG);
        this.mViewVisibility = getVisibility() == 0;
        if (!this.mGlobalListenersAdded) {
            ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnScrollChangedListener(this.mScrollChangedListener);
            observer.addOnPreDrawListener(this.mDrawListener);
            this.mGlobalListenersAdded = true;
        }
    }

    protected void onWindowVisibilityChanged(int visibility) {
        boolean z;
        boolean z2 = true;
        super.onWindowVisibilityChanged(visibility);
        if (visibility == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mWindowVisibility = z;
        if (!(this.mWindowVisibility && this.mViewVisibility)) {
            z2 = false;
        }
        this.mRequestedVisible = z2;
        updateWindow(false, false);
    }

    public void setVisibility(int visibility) {
        boolean z;
        boolean newRequestedVisible;
        super.setVisibility(visibility);
        if (visibility == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mViewVisibility = z;
        if (this.mWindowVisibility && this.mViewVisibility) {
            newRequestedVisible = true;
        } else {
            newRequestedVisible = false;
        }
        if (newRequestedVisible != this.mRequestedVisible) {
            requestLayout();
        }
        this.mRequestedVisible = newRequestedVisible;
        updateWindow(false, false);
    }

    protected void onDetachedFromWindow() {
        if (this.mGlobalListenersAdded) {
            ViewTreeObserver observer = getViewTreeObserver();
            observer.removeOnScrollChangedListener(this.mScrollChangedListener);
            observer.removeOnPreDrawListener(this.mDrawListener);
            this.mGlobalListenersAdded = false;
        }
        this.mRequestedVisible = false;
        updateWindow(false, false);
        this.mHaveFrame = false;
        if (this.mWindow != null) {
            try {
                this.mSession.remove(this.mWindow);
            } catch (RemoteException e) {
            }
            this.mWindow = null;
        }
        this.mSession = null;
        this.mLayout.token = null;
        super.onDetachedFromWindow();
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(this.mRequestedWidth >= 0 ? View.resolveSizeAndState(this.mRequestedWidth, widthMeasureSpec, 0) : View.getDefaultSize(0, widthMeasureSpec), this.mRequestedHeight >= 0 ? View.resolveSizeAndState(this.mRequestedHeight, heightMeasureSpec, 0) : View.getDefaultSize(0, heightMeasureSpec));
    }

    protected boolean setFrame(int left, int top, int right, int bottom) {
        boolean force;
        boolean result = super.setFrame(left, top, right, bottom);
        if (result && isFixedSize() && this.mNeedForceDrawAtSetFrame) {
            force = true;
        } else {
            force = false;
        }
        this.mNeedForceDrawAtSetFrame = false;
        updateWindow(force, false);
        return result;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.mCurrentOrientation != newConfig.orientation) {
            this.mNeedForceDrawAtSetFrame = true;
            this.mCurrentOrientation = newConfig.orientation;
        }
    }

    public boolean gatherTransparentRegion(Region region) {
        if (this.mWindowType == 1000) {
            return super.gatherTransparentRegion(region);
        }
        if (isMultiWindow()) {
            return super.gatherTransparentRegion(region);
        }
        boolean opaque = true;
        if ((this.mPrivateFlags & 128) == 0) {
            opaque = super.gatherTransparentRegion(region);
        } else if (region != null) {
            int w = getWidth();
            int h = getHeight();
            if (w > 0 && h > 0) {
                getLocationInWindow(this.mLocation);
                int l = this.mLocation[0];
                int t = this.mLocation[1];
                region.op(l, t, l + w, t + h, Op.UNION);
            }
        }
        if (PixelFormat.formatHasAlpha(this.mRequestedFormat)) {
            return false;
        }
        return opaque;
    }

    public void draw(Canvas canvas) {
        if (this.mWindowType != 1000 && (this.mPrivateFlags & 128) == 0) {
            canvas.drawColor(0, Mode.CLEAR);
        }
        super.draw(canvas);
    }

    protected void dispatchDraw(Canvas canvas) {
        if (this.mWindowType != 1000 && (this.mPrivateFlags & 128) == 128) {
            canvas.drawColor(0, Mode.CLEAR);
        }
        super.dispatchDraw(canvas);
    }

    public void setZOrderMediaOverlay(boolean isMediaOverlay) {
        this.mWindowType = isMediaOverlay ? 1004 : 1001;
    }

    public void setZOrderOnTop(boolean onTop) {
        if (onTop) {
            this.mWindowType = 1000;
            LayoutParams layoutParams = this.mLayout;
            layoutParams.flags |= 131072;
            return;
        }
        this.mWindowType = 1001;
        layoutParams = this.mLayout;
        layoutParams.flags &= -131073;
    }

    public void setBufferCount(int count) {
        this.mSVBufferCount = count;
    }

    public void setSecure(boolean isSecure) {
        if (isSecure) {
            LayoutParams layoutParams = this.mLayout;
            layoutParams.flags |= 8192;
            return;
        }
        layoutParams = this.mLayout;
        layoutParams.flags &= -8193;
    }

    public void setWindowType(int type) {
        this.mWindowType = type;
    }

    protected void updateWindow(boolean force, boolean redrawNeeded) {
        if (this.mHaveFrame) {
            ViewRootImpl viewRoot = getViewRootImpl();
            if (viewRoot != null) {
                this.mTranslator = viewRoot.mTranslator;
            }
            if (this.mTranslator != null) {
                this.mSurface.setCompatibilityTranslator(this.mTranslator);
            }
            int myWidth = this.mRequestedWidth;
            if (myWidth <= 0) {
                myWidth = getWidth();
            }
            int myHeight = this.mRequestedHeight;
            if (myHeight <= 0) {
                myHeight = getHeight();
            }
            getLocationInWindow(this.mLocation);
            boolean creating = this.mWindow == null;
            boolean formatChanged = this.mFormat != this.mRequestedFormat;
            boolean blockSizeChange = this.mHeight > myHeight && usingInputMethodInCascade();
            boolean sizeChanged = (this.mWidth == myWidth && (this.mHeight == myHeight || blockSizeChange)) ? false : true;
            boolean visibleChanged = this.mVisible != this.mRequestedVisible;
            boolean layoutSizeChanged = (getWidth() == this.mLayout.width && getHeight() == this.mLayout.height) ? false : true;
            if (force || creating || formatChanged || sizeChanged || visibleChanged || this.mLeft != this.mLocation[0] || this.mTop != this.mLocation[1] || this.mUpdateWindowNeeded || this.mReportDrawNeeded || redrawNeeded || layoutSizeChanged) {
                try {
                    boolean visible = this.mRequestedVisible;
                    this.mVisible = visible;
                    this.mLeft = this.mLocation[0];
                    this.mTop = this.mLocation[1];
                    this.mWidth = myWidth;
                    this.mHeight = myHeight;
                    this.mFormat = this.mRequestedFormat;
                    this.mLayout.x = this.mLeft;
                    this.mLayout.y = this.mTop;
                    if (this.mIsFixedOrientation) {
                        this.mLayout.x = this.mRequestedX;
                        this.mLayout.y = this.mRequestedY;
                        if (this.mTargetWidth <= 0 || this.mTargetHeight <= 0) {
                            this.mLayout.width = myWidth;
                            this.mLayout.height = myHeight;
                        } else {
                            this.mLayout.width = this.mTargetWidth;
                            this.mLayout.height = this.mTargetHeight;
                        }
                    } else {
                        this.mLayout.width = getWidth();
                        this.mLayout.height = getHeight();
                    }
                    if (this.mTranslator != null) {
                        this.mTranslator.translateLayoutParamsInAppWindowToScreen(this.mLayout);
                    }
                    this.mLayout.format = this.mRequestedFormat;
                    LayoutParams layoutParams = this.mLayout;
                    layoutParams.flags |= 16920;
                    if (!getContext().getResources().getCompatibilityInfo().supportsScreen()) {
                        layoutParams = this.mLayout;
                        layoutParams.privateFlags |= 128;
                    }
                    layoutParams = this.mLayout;
                    layoutParams.privateFlags |= 64;
                    if (this.mWindow == null) {
                        Display display = getDisplay();
                        this.mWindow = new MyWindow(this);
                        this.mLayout.type = this.mWindowType;
                        this.mLayout.gravity = 8388659;
                        this.mSession.addToDisplayWithoutInputChannel(this.mWindow, this.mWindow.mSeq, this.mLayout, this.mVisible ? 0 : 8, display.getDisplayId(), this.mContentInsets, this.mStableInsets);
                    }
                    this.mSurfaceLock.lock();
                    this.mUpdateWindowNeeded = false;
                    boolean reportDrawNeeded = this.mReportDrawNeeded;
                    this.mReportDrawNeeded = false;
                    this.mDrawingStopped = !visible;
                    int relayoutResult = this.mSession.relayout(this.mWindow, this.mWindow.mSeq, this.mLayout, this.mWidth, this.mHeight, visible ? 0 : 8, 2, this.mWinFrame, this.mOverscanInsets, this.mContentInsets, this.mVisibleInsets, this.mStableInsets, this.mOutsets, this.mConfiguration, this.mNewSurface, new PointF(), new Rect());
                    if ((relayoutResult & 2) != 0) {
                        reportDrawNeeded = true;
                    }
                    this.mSurfaceFrame.left = 0;
                    this.mSurfaceFrame.top = 0;
                    if (this.mTranslator == null) {
                        this.mSurfaceFrame.right = this.mWinFrame.width();
                        this.mSurfaceFrame.bottom = this.mWinFrame.height();
                    } else {
                        float appInvertedScale = this.mTranslator.applicationInvertedScale;
                        this.mSurfaceFrame.right = (int) ((((float) this.mWinFrame.width()) * appInvertedScale) + 0.5f);
                        this.mSurfaceFrame.bottom = (int) ((((float) this.mWinFrame.height()) * appInvertedScale) + 0.5f);
                    }
                    int surfaceWidth = this.mSurfaceFrame.right;
                    int surfaceHeight = this.mSurfaceFrame.bottom;
                    boolean realSizeChanged = (this.mLastSurfaceWidth == surfaceWidth && this.mLastSurfaceHeight == surfaceHeight) ? false : true;
                    this.mLastSurfaceWidth = surfaceWidth;
                    this.mLastSurfaceHeight = surfaceHeight;
                    this.mSurfaceLock.unlock();
                    redrawNeeded |= creating | reportDrawNeeded;
                    Callback[] callbacks = null;
                    boolean surfaceChanged = (relayoutResult & 4) != 0;
                    if (this.mSurfaceCreated && (surfaceChanged || (!visible && visibleChanged))) {
                        this.mSurfaceCreated = false;
                        if (this.mSurface.isValid()) {
                            callbacks = getSurfaceCallbacks();
                            for (Callback c : callbacks) {
                                c.surfaceDestroyed(this.mSurfaceHolder);
                            }
                        }
                    }
                    this.mSurface.transferFrom(this.mNewSurface);
                    if (visible && this.mSurface.isValid()) {
                        if (!this.mSurfaceCreated && (surfaceChanged || visibleChanged)) {
                            this.mSurfaceCreated = true;
                            this.mIsCreating = true;
                            if (callbacks == null) {
                                callbacks = getSurfaceCallbacks();
                            }
                            for (Callback c2 : callbacks) {
                                c2.surfaceCreated(this.mSurfaceHolder);
                            }
                            if (viewRoot != null) {
                                if (ViewRootImpl.sSVBufferCount != -1) {
                                    this.mSurface.setBufferCount(ViewRootImpl.sSVBufferCount);
                                } else if (this.mSVBufferCount != -1) {
                                    this.mSurface.setBufferCount(this.mSVBufferCount);
                                }
                            }
                        }
                        if (creating || formatChanged || sizeChanged || visibleChanged || realSizeChanged) {
                            if (callbacks == null) {
                                callbacks = getSurfaceCallbacks();
                            }
                            for (Callback c22 : callbacks) {
                                c22.surfaceChanged(this.mSurfaceHolder, this.mFormat, myWidth, myHeight);
                            }
                        }
                        if (redrawNeeded) {
                            if (callbacks == null) {
                                callbacks = getSurfaceCallbacks();
                            }
                            for (Callback c222 : callbacks) {
                                if (c222 instanceof Callback2) {
                                    ((Callback2) c222).surfaceRedrawNeeded(this.mSurfaceHolder);
                                }
                            }
                        }
                    }
                    this.mIsCreating = false;
                    if (redrawNeeded) {
                        this.mSession.finishDrawing(this.mWindow);
                    }
                    this.mSession.performDeferredDestroy(this.mWindow);
                } catch (RemoteException e) {
                } catch (Throwable th) {
                    this.mSurfaceLock.unlock();
                }
            }
        }
    }

    private Callback[] getSurfaceCallbacks() {
        Callback[] callbacks;
        synchronized (this.mCallbacks) {
            callbacks = new Callback[this.mCallbacks.size()];
            this.mCallbacks.toArray(callbacks);
        }
        return callbacks;
    }

    void handleGetNewSurface() {
        updateWindow(false, false);
    }

    public boolean isFixedSize() {
        return (this.mRequestedWidth == -1 && this.mRequestedHeight == -1) ? false : true;
    }

    public void setFixedOrientation(int samsungFlagFixedOrientation) {
        if (samsungFlagFixedOrientation != 0) {
            LayoutParams layoutParams = this.mLayout;
            layoutParams.samsungFlags |= samsungFlagFixedOrientation;
            this.mIsFixedOrientation = true;
            WindowManagerImpl wm = (WindowManagerImpl) this.mContext.getSystemService("window");
            DisplayInfo di = new DisplayInfo();
            wm.getDefaultDisplay().getDisplayInfo(di);
            if (samsungFlagFixedOrientation == 4) {
                this.mRequestedWidth = (int) MathUtils.max(di.appWidth, di.appHeight);
                this.mRequestedHeight = (int) MathUtils.min(di.appWidth, di.appHeight);
                return;
            } else if (samsungFlagFixedOrientation == 8) {
                this.mRequestedWidth = (int) MathUtils.min(di.appWidth, di.appHeight);
                this.mRequestedHeight = (int) MathUtils.max(di.appWidth, di.appHeight);
                return;
            } else {
                return;
            }
        }
        this.mRequestedX = 0;
        this.mRequestedY = 0;
        this.mRequestedWidth = -1;
        this.mRequestedHeight = -1;
        this.mIsFixedOrientation = false;
        layoutParams = this.mLayout;
        layoutParams.samsungFlags &= -13;
    }

    public void setFixedOrientation(int samsungFlagFixedOrientation, int x, int y, int width, int height) {
        setFixedOrientationWithScale(samsungFlagFixedOrientation, x, y, width, height, -1, -1);
    }

    public void setFixedOrientationWithScale(int samsungFlagFixedOrientation, int x, int y, int sourceWidth, int sourceHeight, int targetWidth, int targetHeight) {
        LayoutParams layoutParams = this.mLayout;
        layoutParams.samsungFlags |= samsungFlagFixedOrientation;
        this.mIsFixedOrientation = true;
        this.mRequestedX = x;
        this.mRequestedY = y;
        this.mRequestedWidth = sourceWidth;
        this.mRequestedHeight = sourceHeight;
        this.mTargetWidth = targetWidth;
        this.mTargetHeight = targetHeight;
    }

    public void addSamsungFlags(int flags) {
        setSamsungFlags(flags, flags);
    }

    public void clearSamsungFlags(int flags) {
        setSamsungFlags(0, flags);
    }

    private void setSamsungFlags(int flags, int mask) {
        this.mLayout.samsungFlags = (this.mLayout.samsungFlags & (mask ^ -1)) | (flags & mask);
    }
}
