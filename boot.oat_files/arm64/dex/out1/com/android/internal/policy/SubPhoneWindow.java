package com.android.internal.policy;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManagerGlobal;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemVibrator;
import android.util.Log;
import android.view.Display;
import android.view.DisplayInfo;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.R;
import com.android.internal.os.SMProviderContract;
import com.android.internal.policy.multiwindow.Border;
import com.android.internal.policy.multiwindow.EdgeInspector;
import com.samsung.android.multiwindow.MultiWindowFacade;
import com.samsung.android.multiwindow.MultiWindowStyle;
import com.samsung.android.multiwindow.ui.GuideView;

public class SubPhoneWindow extends PhoneWindow {
    static final boolean DEBUG = false;
    static final boolean SAFE_DEBUG = true;
    static final String TAG = "SubPhoneWindow";
    private Activity mActivity = null;
    private Border mBorder;
    private Context mContext;
    private boolean mHasStackFocus = false;
    private boolean mIsBorder = false;
    private byte[] mIvt = new byte[]{(byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 18, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 48, (byte) 0, (byte) 0, (byte) 10, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 32, (byte) 0, (byte) 0, (byte) 0, Byte.MAX_VALUE, (byte) 0, (byte) 0, (byte) -94};
    private Rect mMinStackBoundForLand = new Rect();
    private Rect mMinStackBoundForPort = new Rect();
    private final MultiWindowFacade mMultiWindowFacade;
    private MultiWindowStyle mMultiWindowStyle;
    private PowerManager mPowerManager;
    private float mScaleX;
    private float mScaleY;
    private Rect mStackBound;
    private int mStatusBarHeight;
    private SubPhoneWindowListener mSubPhoneWindowListener;
    private IBinder mToken = null;
    private ColorDrawable mTrasnparentColor = new ColorDrawable(0);
    private SystemVibrator mVibrator;

    private static class ResizeHelper {
        private static final String TAG = "ResizeHelper";
        private final boolean DEBUG = true;
        private int mBeginX;
        private int mBeginY;
        private Rect mContentsBounds = new Rect();
        private Context mContext;
        private float mFixedRatio;
        private GuideView mGuideView;
        private int mHoverIcon = -1;
        private Listener mListener;
        private int mMaxHeight;
        private int mMaxWidth;
        private int mMinHeight;
        private int mMinWidth;
        private boolean mMoving = false;
        private Rect mPaddingRect = new Rect();
        private Rect mResizablePadding = new Rect();
        private Rect mResizeBounds = new Rect();
        private EdgeInspector mTouchEdgeInspector = new EdgeInspector(null, null, true);

        public interface Listener {
            void onResizeFinish(Rect rect);
        }

        public ResizeHelper(Context context, Rect contentsBoudns, Rect resizeBounds, Rect paddingRect, Listener l, int minWidth, int minHeight, int maxWidth, int maxHeight) {
            this.mContext = context;
            this.mListener = l;
            int resizableSize = (int) this.mContext.getResources().getDimension(R.dimen.multiwindow_floating_resize_area);
            this.mResizablePadding.set(resizableSize, resizableSize, resizableSize, resizableSize);
            set(contentsBoudns, resizeBounds, paddingRect, minWidth, minHeight, maxWidth, maxHeight);
        }

        public void set(Rect contentsBoudns, Rect resizeBounds, Rect paddingRect, int minWidth, int minHeight, int maxWidth, int maxHeight) {
            if (contentsBoudns == null || resizeBounds == null) {
                Log.e(TAG, "The parameter is null.resizeBounds =" + resizeBounds + ",contentsBoudns=" + contentsBoudns);
                throw new IllegalArgumentException("The parameter was wrong.");
            }
            Log.d(TAG, "resizeBounds =" + resizeBounds + ",contentsBoudns=" + contentsBoudns);
            this.mContentsBounds.set(contentsBoudns);
            this.mResizeBounds.set(resizeBounds);
            if (paddingRect != null) {
                this.mPaddingRect.set(paddingRect);
            } else {
                this.mPaddingRect.setEmpty();
            }
            this.mMinWidth = minWidth;
            this.mMinHeight = minHeight;
            this.mMaxWidth = maxWidth;
            this.mMaxHeight = maxHeight;
            this.mFixedRatio = ((float) this.mContentsBounds.width()) / ((float) this.mContentsBounds.height());
        }

        public boolean isMoving() {
            return this.mMoving;
        }

        public boolean begin(MotionEvent ev) {
            this.mTouchEdgeInspector.clear();
            this.mTouchEdgeInspector.set(this.mResizeBounds, this.mResizablePadding);
            switch (this.mContext.getResources().getConfiguration().orientation) {
                case 1:
                    this.mTouchEdgeInspector.setFilter(11);
                    break;
                case 2:
                    this.mTouchEdgeInspector.setFilter(14);
                    break;
            }
            this.mBeginX = (int) ev.getRawXForScaledWindow();
            this.mBeginY = (int) ev.getRawYForScaledWindow();
            this.mTouchEdgeInspector.check(this.mBeginX, this.mBeginY);
            this.mMoving = this.mTouchEdgeInspector.isCorner();
            if (this.mMoving) {
                Log.d(TAG, "Start to resize.");
                initGuideView();
            } else {
                Log.d(TAG, "Resizing is failed. mResizeBounds=" + this.mResizeBounds + ",mResizablePadding=" + this.mResizablePadding + ",mBeginX=" + this.mBeginX + ",mBeginY" + this.mBeginY);
            }
            return this.mMoving;
        }

        public boolean move(MotionEvent ev) {
            if (this.mMoving) {
                boolean lastBoundIsMax;
                int dx = 0;
                if (this.mTouchEdgeInspector.isEdge(5)) {
                    dx = this.mBeginX - ((int) ev.getRawXForScaledWindow());
                } else if (this.mTouchEdgeInspector.isEdge(9)) {
                    dx = ((int) ev.getRawXForScaledWindow()) - this.mBeginX;
                } else if (this.mTouchEdgeInspector.isEdge(6)) {
                    dx = this.mBeginX - ((int) ev.getRawXForScaledWindow());
                } else if (this.mTouchEdgeInspector.isEdge(10)) {
                    dx = ((int) ev.getRawXForScaledWindow()) - this.mBeginX;
                }
                int width = this.mContentsBounds.width() + dx;
                if (this.mContentsBounds.width() == this.mMaxWidth || this.mContentsBounds.height() == this.mMaxHeight) {
                    lastBoundIsMax = true;
                } else {
                    lastBoundIsMax = false;
                }
                int height = Math.round(((float) width) / this.mFixedRatio);
                if (width < this.mMinWidth) {
                    width = this.mMinWidth;
                    height = Math.round(((float) width) / this.mFixedRatio);
                    this.mGuideView.setGuideState(1);
                } else if (width <= this.mMaxWidth) {
                    this.mGuideView.setGuideState(0);
                } else if (lastBoundIsMax) {
                    width = DisplayManagerGlobal.getInstance().getDisplayInfo(0).appWidth;
                    height = DisplayManagerGlobal.getInstance().getDisplayInfo(0).appHeight;
                    this.mGuideView.setGuideState(2);
                } else {
                    width = this.mMaxWidth;
                    height = Math.round(((float) width) / this.mFixedRatio);
                }
                if (width == DisplayManagerGlobal.getInstance().getDisplayInfo(0).appWidth || height == DisplayManagerGlobal.getInstance().getDisplayInfo(0).appHeight) {
                    this.mGuideView.show(0, 0, width, height);
                } else if (this.mTouchEdgeInspector.isEdge(5)) {
                    this.mGuideView.show(this.mContentsBounds.right - width, this.mContentsBounds.bottom - height, width, height);
                } else if (this.mTouchEdgeInspector.isEdge(9)) {
                    this.mGuideView.show(this.mContentsBounds.left, this.mContentsBounds.bottom - height, width, height);
                } else if (this.mTouchEdgeInspector.isEdge(6)) {
                    this.mGuideView.show(this.mContentsBounds.right - width, this.mContentsBounds.top, width, height);
                } else if (this.mTouchEdgeInspector.isEdge(10)) {
                    this.mGuideView.show(this.mContentsBounds.left, this.mContentsBounds.top, width, height);
                }
                return true;
            } else if (!this.mTouchEdgeInspector.isCandidate()) {
                return false;
            } else {
                this.mBeginX = (int) ev.getRawXForScaledWindow();
                this.mBeginY = (int) ev.getRawYForScaledWindow();
                this.mTouchEdgeInspector.check(this.mBeginX, this.mBeginY);
                boolean isCorner = this.mTouchEdgeInspector.isCorner();
                this.mMoving = isCorner;
                if (!isCorner) {
                    return false;
                }
                Log.d("TAG", "Start to resize.");
                initGuideView();
                return false;
            }
        }

        public boolean finish(MotionEvent ev) {
            if (this.mGuideView != null) {
                this.mGuideView.dismiss();
            }
            if (this.mMoving) {
                if (this.mGuideView != null) {
                    Log.d(TAG, "finish resizing. The bounds is " + this.mGuideView.getLastBounds());
                }
                if (this.mGuideView != null) {
                    this.mListener.onResizeFinish(this.mGuideView.getLastBounds());
                    this.mGuideView = null;
                }
                return true;
            }
            this.mGuideView = null;
            return false;
        }

        public boolean dispathcHoverEvent(MotionEvent ev) {
            int i = 9;
            int i2 = 8;
            boolean isMouse = false;
            int i3 = 1;
            if (ev.getToolType(0) == 3) {
                isMouse = true;
            }
            boolean isConsume = false;
            switch (ev.getAction()) {
                case 7:
                    break;
                case 9:
                    this.mTouchEdgeInspector.clear();
                    this.mTouchEdgeInspector.set(this.mResizeBounds, this.mResizablePadding);
                    switch (this.mContext.getResources().getConfiguration().orientation) {
                        case 1:
                            this.mTouchEdgeInspector.setFilter(11);
                            break;
                        case 2:
                            this.mTouchEdgeInspector.setFilter(14);
                            break;
                    }
                    break;
                case 10:
                    if (isResizeIcon(isMouse)) {
                        if (isMouse) {
                            i3 = 101;
                        }
                        this.mHoverIcon = i3;
                        isConsume = true;
                        break;
                    }
                    break;
            }
            this.mTouchEdgeInspector.clear();
            this.mTouchEdgeInspector.check((int) ev.getRawXForScaledWindow(), (int) ev.getRawYForScaledWindow());
            if (!this.mTouchEdgeInspector.isCorner() || isInputMethodShown()) {
                if (isResizeIcon(isMouse)) {
                    if (isMouse) {
                        i3 = 101;
                    }
                    this.mHoverIcon = i3;
                    isConsume = true;
                }
                if (isConsume) {
                    try {
                        if (this.mHoverIcon > 0) {
                            if (isMouse) {
                                PointerIcon.setHoveringSpenIcon(this.mHoverIcon, -1);
                            } else {
                                PointerIcon.setMouseIcon(this.mHoverIcon, -1);
                            }
                        }
                    } catch (RemoteException e) {
                        Log.e(TAG, "Failed to change Pen Point to HOVERING / " + isMouse);
                    }
                }
                return isConsume;
            }
            if (this.mTouchEdgeInspector.isEdge(6)) {
                this.mHoverIcon = isMouse ? 109 : 9;
                isConsume = true;
            } else if (this.mTouchEdgeInspector.isEdge(10)) {
                if (isMouse) {
                    i3 = 108;
                } else {
                    i3 = 8;
                }
                this.mHoverIcon = i3;
                isConsume = true;
            } else if (this.mTouchEdgeInspector.isEdge(5)) {
                if (isMouse) {
                    i2 = 108;
                }
                this.mHoverIcon = i2;
                isConsume = true;
            } else {
                if (isMouse) {
                    i = 109;
                }
                this.mHoverIcon = i;
                isConsume = true;
            }
            if (isConsume) {
                if (this.mHoverIcon > 0) {
                    if (isMouse) {
                        PointerIcon.setHoveringSpenIcon(this.mHoverIcon, -1);
                    } else {
                        PointerIcon.setMouseIcon(this.mHoverIcon, -1);
                    }
                }
            }
            return isConsume;
        }

        private boolean isResizeIcon(boolean isMouse) {
            if ((isMouse && (this.mHoverIcon == 108 || this.mHoverIcon == 109)) || this.mHoverIcon == 8 || this.mHoverIcon == 9) {
                return true;
            }
            return false;
        }

        private boolean isInputMethodShown() {
            InputMethodManager imm = InputMethodManager.peekInstance();
            if (imm != null) {
                return imm.isInputMethodShown();
            }
            return false;
        }

        private void initGuideView() {
            this.mGuideView = new GuideView(this.mContext, null, LayoutParams.TYPE_MULTI_WINDOW_GUIDE_VIEW);
            this.mGuideView.init();
            this.mGuideView.setGuidePadding(this.mPaddingRect.left, this.mPaddingRect.top, this.mPaddingRect.right, this.mPaddingRect.bottom);
            this.mGuideView.show(this.mContentsBounds.left, this.mContentsBounds.top, this.mContentsBounds.right - this.mContentsBounds.left, this.mContentsBounds.bottom - this.mContentsBounds.top);
        }
    }

    private final class SubPhoneDecorView extends DecorView {
        private ResizeHelper mResizeHelper;

        public SubPhoneDecorView(Context context, int featureId) {
            super(context, featureId);
        }

        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (SubPhoneWindow.this.mMultiWindowStyle.isCascade() && SubPhoneWindow.this.mMultiWindowStyle.isEnabled(2048)) {
                switch (ev.getAction()) {
                    case 0:
                        initResizeHelper();
                        if (this.mResizeHelper.begin(ev)) {
                            SubPhoneWindow.this.forceHideInputMethod();
                            return true;
                        }
                        break;
                    case 1:
                    case 3:
                        this.mResizeHelper.finish(ev);
                        break;
                    case 2:
                        if (this.mResizeHelper == null) {
                            initResizeHelper();
                        }
                        if (this.mResizeHelper.move(ev)) {
                            return true;
                        }
                        break;
                }
            }
            return super.dispatchTouchEvent(ev);
        }

        public void draw(Canvas canvas) {
            super.draw(canvas);
            drawBorder(canvas);
        }

        protected void dispatchDraw(Canvas canvas) {
            super.dispatchDraw(canvas);
            drawBorder(canvas);
        }

        protected boolean dispatchHoverEvent(MotionEvent event) {
            if (SubPhoneWindow.this.mMultiWindowStyle.isCascade() && SubPhoneWindow.this.mMultiWindowStyle.isEnabled(2048)) {
                if (this.mResizeHelper == null || event.getActionMasked() == 9) {
                    initResizeHelper();
                }
                if (this.mResizeHelper.dispathcHoverEvent(event)) {
                    return true;
                }
            }
            return super.dispatchHoverEvent(event);
        }

        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
        }

        public void setBackgroundDrawable(Drawable d) {
            super.setBackgroundDrawable(d);
        }

        private void drawBorder(Canvas canvas) {
            if (SubPhoneWindow.this.mIsBorder) {
                LayoutParams attrs = SubPhoneWindow.this.getAttributes();
                if (!SubPhoneWindow.this.mMultiWindowStyle.isEnabled(4) && attrs.width == -1 && attrs.height == -1) {
                    switch (getResources().getConfiguration().orientation) {
                        case 1:
                            SubPhoneWindow.this.mBorder.setDrawOption(14);
                            break;
                        case 2:
                            SubPhoneWindow.this.mBorder.setDrawOption(11);
                            break;
                    }
                    SubPhoneWindow.this.mBorder.drawFocusBorder(canvas, getMeasuredWidth(), getMeasuredHeight());
                    return;
                }
                SubPhoneWindow.this.mIsBorder = false;
            }
        }

        private void initResizeHelper() {
            Rect stackBounds = SubPhoneWindow.this.getStackBoxBounds();
            if (stackBounds != null) {
                int minWidth;
                int minHeight;
                float maxSizeRatio = ((float) this.mContext.getResources().getInteger(R.integer.multiwindow_floating_maximum_size_ratio)) / 1000.0f;
                if (this.mContext.getResources().getConfiguration().orientation == 1) {
                    minWidth = SubPhoneWindow.this.mMinStackBoundForPort.width();
                    minHeight = SubPhoneWindow.this.mMinStackBoundForPort.height();
                } else {
                    minWidth = SubPhoneWindow.this.mMinStackBoundForLand.width();
                    minHeight = SubPhoneWindow.this.mMinStackBoundForLand.height();
                }
                int screenWidth = DisplayManagerGlobal.getInstance().getDisplayInfo(0).appWidth;
                int screenHeight = DisplayManagerGlobal.getInstance().getDisplayInfo(0).appHeight;
                int maxWidth = (int) (((float) screenWidth) * maxSizeRatio);
                int maxHeight = (int) (((float) screenHeight) * maxSizeRatio);
                float hScale = ((float) stackBounds.width()) / ((float) screenWidth);
                float vScale = ((float) stackBounds.height()) / ((float) screenHeight);
                Rect resizeBounds = new Rect(stackBounds);
                DisplayInfo displayInfo = DisplayManagerGlobal.getInstance().getDisplayInfo(0, SubPhoneWindow.this.mToken);
                if (displayInfo == null) {
                    Log.e(SubPhoneWindow.TAG, "Could not get display information from display manager.");
                    return;
                }
                float test2 = ((float) (displayInfo.logicalWidth - displayInfo.appWidth)) * hScale;
                int absWidth = Math.round((((float) (displayInfo.logicalWidth - displayInfo.appWidth)) * hScale) + 0.5f);
                int absHeight = Math.round((((float) (displayInfo.logicalHeight - displayInfo.appHeight)) * vScale) + 0.5f);
                switch (displayInfo.rotation) {
                    case 0:
                    case 2:
                        resizeBounds.right += absWidth;
                        break;
                    default:
                        resizeBounds.bottom += absHeight;
                        break;
                }
                Rect paddingRect = new Rect(0, 0, absWidth, absHeight);
                if (this.mResizeHelper == null) {
                    this.mResizeHelper = new ResizeHelper(getContext(), stackBounds, resizeBounds, paddingRect, new Listener() {
                        public void onResizeFinish(Rect rect) {
                            if (rect.width() == DisplayManagerGlobal.getInstance().getDisplayInfo(0).appWidth || rect.height() == DisplayManagerGlobal.getInstance().getDisplayInfo(0).appHeight) {
                                MultiWindowStyle tempStyle = new MultiWindowStyle(SubPhoneWindow.this.mMultiWindowStyle);
                                tempStyle.setType(0);
                                SubPhoneWindow.this.mMultiWindowFacade.setMultiWindowStyle(SubPhoneWindow.this.mToken, tempStyle);
                                return;
                            }
                            SubPhoneWindow.this.mMultiWindowFacade.setStackBound(SubPhoneWindow.this.mToken, rect);
                        }
                    }, minWidth, minHeight, maxWidth, maxHeight);
                } else {
                    this.mResizeHelper.set(stackBounds, resizeBounds, paddingRect, minWidth, minHeight, maxWidth, maxHeight);
                }
            }
        }
    }

    private static class SubPhoneWindowListener implements OnTouchListener, OnClickListener, OnHoverListener {
        SubPhoneWindowListener() {
        }

        public void onClick(View v) {
        }

        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case 0:
                case 1:
                case 2:
                case 3:
                    return true;
                default:
                    return false;
            }
        }

        public boolean onHover(View v, MotionEvent event) {
            return false;
        }
    }

    public SubPhoneWindow(Context context) {
        super(context);
        this.mContext = context;
        if (this.mContext instanceof Activity) {
            this.mActivity = (Activity) this.mContext;
        }
        this.mVibrator = (SystemVibrator) this.mContext.getSystemService("vibrator");
        this.mPowerManager = (PowerManager) this.mContext.getSystemService(SMProviderContract.KEY_POWER);
        this.mMultiWindowFacade = (MultiWindowFacade) this.mContext.getSystemService("multiwindow_facade");
        this.mStatusBarHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
        if (this.mActivity != null) {
            this.mMultiWindowStyle = new MultiWindowStyle(this.mActivity.getMultiWindowStyle());
            this.mToken = this.mActivity.getActivityToken();
        } else {
            this.mMultiWindowStyle = new MultiWindowStyle();
        }
        if (this.mMultiWindowStyle.isCascade() && this.mMultiWindowStyle.isEnabled(2048)) {
            this.mIsBorder = true;
        }
        Point displaySize = new Point();
        Display display = DisplayManagerGlobal.getInstance().getRealDisplay(0);
        if (display != null) {
            display.getSize(displaySize);
        }
        int floatingMinimumSizeRatioPercentage = this.mContext.getResources().getInteger(R.integer.multiwindow_floating_minimum_size_ratio);
        Rect minSize = new Rect(0, 0, (int) (((float) (displaySize.x * floatingMinimumSizeRatioPercentage)) / 1000.0f), (int) (((float) (displaySize.y * floatingMinimumSizeRatioPercentage)) / 1000.0f));
        if (this.mContext.getResources().getConfiguration().orientation == 1) {
            this.mMinStackBoundForPort.set(minSize);
            this.mMinStackBoundForLand.set(minSize.left, minSize.top, minSize.left + minSize.height(), minSize.top + minSize.width());
            return;
        }
        this.mMinStackBoundForPort.set(minSize.left, minSize.top, minSize.left + minSize.height(), minSize.top + minSize.width());
        this.mMinStackBoundForLand.set(minSize);
    }

    protected DecorView generateDecor() {
        if (this.mBorder == null) {
            WindowManager wm = (WindowManager) this.mContext.getSystemService("window");
            if (wm != null) {
                this.mBorder = new Border(this.mContext, wm);
            }
        }
        return new SubPhoneDecorView(this.mContext, -1);
    }

    private Rect getStackBoxBounds() {
        return this.mMultiWindowFacade.getStackBound(this.mToken);
    }

    public void onMultiWindowStyleChanged(MultiWindowStyle style, int notifyReason) {
        super.onMultiWindowStyleChanged(style, notifyReason);
        boolean doInvalidate = false;
        if (this.mMultiWindowStyle.getType() != style.getType()) {
            doInvalidate = true;
        }
        this.mMultiWindowStyle.setTo(style);
        if (isCascadeScaledWindow()) {
            this.mIsBorder = true;
        } else {
            this.mIsBorder = false;
        }
        if (doInvalidate && peekDecorView() != null) {
            peekDecorView().invalidate();
        }
    }

    public void changeStackFocus(boolean focus) {
        setStackFocus(focus);
    }

    private boolean isCascadeScaledWindow() {
        return this.mMultiWindowStyle.isCascade() && this.mMultiWindowStyle.isEnabled(2048) && !this.mMultiWindowStyle.isEnabled(4);
    }

    private void setStackFocus(boolean focus) {
        if (this.mHasStackFocus != focus) {
            this.mHasStackFocus = focus;
            if (this.mBorder != null) {
                this.mBorder.setFocus(this.mHasStackFocus);
                if (peekDecorView() != null) {
                    peekDecorView().requestLayout();
                    peekDecorView().invalidate();
                }
            }
        }
    }

    private boolean forceHideInputMethod() {
        InputMethodManager imm = InputMethodManager.peekInstance();
        if (imm != null) {
            return imm.forceHideSoftInput();
        }
        return false;
    }
}
