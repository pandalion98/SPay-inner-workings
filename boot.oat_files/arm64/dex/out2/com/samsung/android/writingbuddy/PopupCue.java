package com.samsung.android.writingbuddy;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Slog;
import android.util.TypedValue;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;

public class PopupCue {
    private static final boolean DEBUG = "eng".equals(Build.TYPE);
    private static final boolean ENABLE_FLOATING_VISUAL_CUE_POSITION_X = false;
    private static final boolean ENABLE_FLOATING_VISUAL_CUE_POSITION_Y = false;
    private static final String TAG = "WritingBuddyPopupCue";
    public static final int TYPE_MULTILINE_EDITOR = 2;
    public static final int TYPE_NONFORM_VIEW = 3;
    public static final int TYPE_SINGLELINE_EDITOR = 1;
    private View mAnchorView;
    private Context mContext;
    private CueContainer mCueContainerView;
    private OnHoverListener mHoverListner;
    private IWindowManager mIWindowManager = null;
    private boolean mIsAirButtonClicked;
    private boolean mIsShowing;
    private int mPopupHeight;
    private int mPopupPosX;
    private int mPopupPosY;
    private int mPopupWidth;
    private int mPopupXfromAnchor;
    private int mPopupYfromAnchor;
    private OnTouchListener mTouchListner;
    private int mType;
    private WindowManager mWindowManager;
    private IBinder mWindowToken;

    private class CueContainer extends FrameLayout {
        private Context mContext;
        private View mHoverCue;
        private View mTouchCue;

        public CueContainer(Context context) {
            super(context);
            this.mContext = context;
            initLayout();
        }

        private void initLayout() {
            this.mTouchCue = new View(this.mContext);
            this.mTouchCue.setBackgroundResource(17304152);
            this.mHoverCue = new View(this.mContext);
            this.mHoverCue.setBackgroundResource(17304151);
            addView(this.mTouchCue);
            addView(this.mHoverCue);
            this.mHoverCue.setVisibility(0);
            this.mTouchCue.setVisibility(4);
        }

        public Drawable getHoverCueDrawable() {
            if (this.mHoverCue != null) {
                return this.mHoverCue.getBackground();
            }
            return this.mContext.getResources().getDrawable(17304151);
        }

        public void switchCueButton(boolean isTouched) {
            if (isTouched) {
                this.mHoverCue.setVisibility(4);
                this.mTouchCue.setVisibility(0);
                return;
            }
            this.mHoverCue.setVisibility(0);
            this.mTouchCue.setVisibility(4);
        }

        public boolean onInterceptHoverEvent(MotionEvent event) {
            if (event.getAction() == 7 && event.getButtonState() == 2) {
                PopupCue.this.mIsAirButtonClicked = true;
            }
            return true;
        }

        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return true;
        }
    }

    public PopupCue(View anchorView) {
        this.mAnchorView = anchorView;
        this.mContext = anchorView.getContext();
        initPopup();
    }

    private void initPopup() {
        this.mPopupWidth = 0;
        this.mPopupHeight = 0;
        this.mPopupPosX = 0;
        this.mPopupPosY = 0;
        this.mType = 3;
        this.mIsShowing = false;
        this.mWindowToken = null;
        this.mWindowManager = null;
        this.mCueContainerView = null;
        this.mTouchListner = null;
        this.mHoverListner = null;
    }

    private void createPopup() {
        if (this.mCueContainerView == null) {
            this.mCueContainerView = new CueContainer(this.mContext);
            if (this.mTouchListner != null) {
                this.mCueContainerView.setOnTouchListener(this.mTouchListner);
            }
            if (this.mHoverListner != null) {
                this.mCueContainerView.setOnHoverListener(this.mHoverListner);
            }
        }
    }

    private LayoutParams createPopupLayoutParam() {
        if (DEBUG) {
            Slog.d(TAG, "createPopupLayoutParam() x : " + this.mPopupPosX + " y :" + this.mPopupPosY + "  w : " + this.mPopupWidth + " h : " + this.mPopupHeight);
        }
        LayoutParams wlp = new LayoutParams();
        wlp.gravity = 51;
        wlp.width = this.mPopupWidth;
        wlp.height = this.mPopupHeight;
        wlp.x = this.mPopupPosX;
        wlp.y = this.mPopupPosY;
        wlp.token = this.mWindowToken;
        wlp.format = -3;
        wlp.setTitle("WritingBuddyCue : " + Integer.toHexString(hashCode()));
        wlp.type = 1000;
        wlp.flags |= 256;
        wlp.flags |= 8;
        wlp.flags |= 512;
        wlp.flags |= 16777216;
        wlp.windowAnimations = 16975099;
        return wlp;
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }

    public void show(int type, MotionEvent motionevent) {
        this.mType = type;
        this.mIsAirButtonClicked = false;
        if (!isShowing()) {
            createPopup();
            if (this.mWindowManager == null) {
                this.mWindowManager = (WindowManager) this.mContext.getSystemService("window");
            }
            computePosition(type, motionevent);
            this.mWindowManager.addView(this.mCueContainerView, createPopupLayoutParam());
            this.mIsShowing = true;
        }
    }

    public void switchCueButton(boolean isTouched) {
        this.mCueContainerView.switchCueButton(isTouched);
    }

    public void dismiss(boolean animation) {
        if (isShowing() && this.mCueContainerView != null) {
            ViewGroup.LayoutParams lp = this.mCueContainerView.getLayoutParams();
            if (lp instanceof LayoutParams) {
                int animationResID;
                LayoutParams wlp = (LayoutParams) lp;
                if (animation) {
                    animationResID = 16975099;
                } else {
                    animationResID = 0;
                }
                if (wlp.windowAnimations != animationResID) {
                    ((LayoutParams) lp).windowAnimations = animationResID;
                    this.mWindowManager.updateViewLayout(this.mCueContainerView, lp);
                }
            }
            this.mWindowManager.removeView(this.mCueContainerView);
        }
        this.mCueContainerView = null;
        this.mIsShowing = false;
        this.mIsAirButtonClicked = false;
    }

    private void computePosition(int type, MotionEvent motionevent) {
        int x;
        int y;
        int xFromAnchor;
        int yFromAnchor;
        int cueWidth = 0;
        int cueHeight = 0;
        Drawable d = this.mCueContainerView.getHoverCueDrawable();
        if (d != null) {
            cueWidth = d.getIntrinsicWidth();
            cueHeight = d.getIntrinsicHeight();
        }
        this.mWindowToken = this.mAnchorView.getApplicationWindowToken();
        Rect visibleScrRect = getVisibleRectOnScreen(this.mAnchorView);
        Rect visibleRect = new Rect(visibleScrRect);
        visibleRect.left = visibleScrRect.left;
        visibleRect.right = visibleScrRect.right;
        if (motionevent != null) {
            int mX = (int) motionevent.getX();
            int mY = (int) motionevent.getY();
        }
        if (this.mAnchorView instanceof EditText) {
            EditText targetView = (EditText) this.mAnchorView;
            Layout l = targetView.getLayout();
            int paddingStart = targetView.getCompoundPaddingStart();
            int paddingTop = targetView.getCompoundPaddingTop();
            int paddingBottom = targetView.getCompoundPaddingBottom();
            int offestFromCursor = (int) this.mContext.getResources().getDimension(17105690);
            int imagePaddingBottom = (int) this.mContext.getResources().getDimension(17105691);
            if (l == null || targetView.getBaseline() <= 0) {
                if (this.mAnchorView.getLayoutDirection() == 1) {
                    x = ((visibleRect.right - paddingStart) - cueWidth) - offestFromCursor;
                } else {
                    x = (visibleRect.left + paddingStart) + offestFromCursor;
                }
                y = (visibleRect.top + (paddingTop + ((visibleRect.height() - (paddingTop + paddingBottom)) / 2))) - (cueHeight - imagePaddingBottom);
            } else {
                y = (visibleRect.top + ((targetView.getBaseline() + l.getLineDescent(0)) - ((l.getLineBottom(0) - l.getLineTop(0)) / 2))) - (cueHeight - imagePaddingBottom);
                if (this.mAnchorView.getLayoutDirection() == 1) {
                    x = ((visibleRect.right - paddingStart) - cueWidth) - offestFromCursor;
                } else {
                    x = (visibleRect.left + paddingStart) + offestFromCursor;
                }
            }
            if (x + cueWidth > visibleRect.right) {
                x = visibleRect.right - cueWidth;
            }
            if (x < 0) {
                x = 0;
            }
            int statusBarHeight = getStatusBarHeight();
            if (y < statusBarHeight && isStatusBarShowing()) {
                y = statusBarHeight;
            }
            if (y < 0) {
                y = 0;
            }
            xFromAnchor = x - visibleRect.left;
            yFromAnchor = y - visibleRect.top;
        } else {
            x = visibleRect.left + ((int) this.mContext.getResources().getDimension(17105688));
            y = visibleRect.top + ((int) this.mContext.getResources().getDimension(17105689));
            if (x + cueWidth > visibleRect.right) {
                x = visibleRect.right - cueWidth;
            }
            if (y + cueHeight > visibleRect.bottom) {
                y = visibleRect.bottom - cueHeight;
            }
            if (x < visibleRect.left) {
                x = visibleRect.left;
            }
            if (y < visibleRect.top) {
                y = visibleRect.top;
            }
            xFromAnchor = x - visibleRect.left;
            yFromAnchor = y - visibleRect.top;
        }
        this.mPopupXfromAnchor = xFromAnchor;
        this.mPopupYfromAnchor = yFromAnchor;
        this.mPopupPosX = x;
        this.mPopupPosY = y;
        this.mPopupWidth = cueWidth;
        this.mPopupHeight = cueHeight;
        if (DEBUG) {
            Slog.d(TAG, "computePosition x : " + this.mPopupPosX + " y : " + this.mPopupPosY + " w : " + this.mPopupWidth + " h : " + this.mPopupHeight);
        }
    }

    public void updatePopupPosition(MotionEvent motionevent) {
        LayoutParams wlp = (LayoutParams) this.mCueContainerView.getLayoutParams();
        if (DEBUG) {
            Slog.d(TAG, "updatePopupPosition()");
        }
        computePosition(this.mType, null);
        wlp.x = this.mPopupPosX;
        wlp.y = this.mPopupPosY;
        wlp.width = this.mPopupWidth;
        wlp.height = this.mPopupHeight;
        this.mWindowManager.updateViewLayout(this.mCueContainerView, wlp);
    }

    public void setSize(int width, int height) {
        this.mPopupWidth = width;
        this.mPopupHeight = height;
    }

    public void setPosition(int x, int y) {
        this.mPopupPosX = x;
        this.mPopupPosY = y;
    }

    public void setWindowToken(IBinder token) {
        this.mWindowToken = token;
    }

    public void setOnTouchListener(OnTouchListener l) {
        this.mTouchListner = l;
        if (this.mCueContainerView != null) {
            this.mCueContainerView.setOnTouchListener(l);
        }
    }

    public void setOnHoverListener(OnHoverListener l) {
        this.mHoverListner = l;
        if (this.mCueContainerView != null) {
            this.mCueContainerView.setOnHoverListener(l);
        }
    }

    public boolean isAirButtonClicked() {
        return this.mIsAirButtonClicked;
    }

    public Rect getRectInAnchor() {
        Rect r = new Rect();
        r.left = this.mPopupXfromAnchor;
        r.top = this.mPopupYfromAnchor;
        r.right = r.left + this.mPopupWidth;
        r.bottom = r.top + this.mPopupHeight;
        return r;
    }

    public boolean isPointInPopup(float x, float y) {
        if (x < 0.0f || x > ((float) this.mPopupWidth) || y < 0.0f || y > ((float) this.mPopupHeight)) {
            return false;
        }
        return true;
    }

    private boolean pointInView(View v, float localX, float localY) {
        return localX >= 0.0f && localX < ((float) (v.getRight() - v.getLeft())) && localY >= 0.0f && localY < ((float) (v.getBottom() - v.getTop()));
    }

    private Rect getRectOnScreen(View view) {
        Rect r = new Rect(0, 0, 0, 0);
        if (view != null) {
            int[] locOnScr = new int[]{0, 0};
            view.getLocationOnScreen(locOnScr);
            r.set(locOnScr[0], locOnScr[1], locOnScr[0] + view.getWidth(), locOnScr[1] + view.getHeight());
        }
        return r;
    }

    private Rect getRectInWindow(View view) {
        Rect r = new Rect(0, 0, 0, 0);
        if (view != null) {
            int[] locInWindow = new int[]{0, 0};
            view.getLocationInWindow(locInWindow);
            r.set(locInWindow[0], locInWindow[1], locInWindow[0] + view.getWidth(), locInWindow[1] + view.getHeight());
        }
        return r;
    }

    private Rect getVisibleRectInWindow(View view) {
        Rect r = getRectInWindow(view);
        View v = view;
        ViewParent vp = view.getParent();
        int top = 0;
        int bottomDiff = 0;
        while (vp instanceof View) {
            View parent = (View) vp;
            top += (int) v.getY();
            if (parent.getScrollY() > 0) {
                if (parent.getScrollY() > top) {
                    r.top += parent.getScrollY() - top;
                    top = 0;
                } else {
                    top -= parent.getScrollY();
                }
            }
            int bottomPosY = (((int) v.getY()) + v.getHeight()) - parent.getScrollY();
            if (bottomPosY + bottomDiff < parent.getHeight()) {
                bottomDiff = -(parent.getHeight() - (bottomPosY + bottomDiff));
            } else {
                r.bottom -= (bottomPosY + bottomDiff) - parent.getHeight();
                bottomDiff = 0;
            }
            v = parent;
            vp = parent.getParent();
        }
        Slog.d(TAG, "getVisibleRectInWindow : " + r.toShortString());
        return r;
    }

    private Rect getVisibleRectOnScreen(View view) {
        Rect r = getRectOnScreen(view);
        View v = view;
        ViewParent vp = view.getParent();
        int top = 0;
        int bottomDiff = 0;
        while (vp instanceof View) {
            View parent = (View) vp;
            top += (int) v.getY();
            if (parent.getScrollY() > 0) {
                if (parent.getScrollY() > top) {
                    r.top += parent.getScrollY() - top;
                    top = 0;
                } else {
                    top -= parent.getScrollY();
                }
            }
            int bottomPosY = (((int) v.getY()) + v.getHeight()) - parent.getScrollY();
            if (bottomPosY + bottomDiff < parent.getHeight()) {
                bottomDiff = -(parent.getHeight() - (bottomPosY + bottomDiff));
            } else {
                r.bottom -= (bottomPosY + bottomDiff) - parent.getHeight();
                bottomDiff = 0;
            }
            v = parent;
            vp = parent.getParent();
        }
        Slog.d(TAG, "getVisibleRectOnScreen : " + r.toShortString());
        return r;
    }

    public boolean isStatusBarShowing() {
        try {
            if (getIWindowManager().isStatusBarVisible()) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Slog.d(TAG, "RemoteException - " + e);
            return false;
        }
    }

    public IWindowManager getIWindowManager() {
        if (this.mIWindowManager == null) {
            this.mIWindowManager = Stub.asInterface(ServiceManager.getService("window"));
        }
        return this.mIWindowManager;
    }

    private int getStatusBarHeight() {
        int height = 0;
        try {
            height = this.mContext.getResources().getDimensionPixelSize(17104919);
        } catch (NotFoundException e) {
            Slog.d(TAG, e.toString());
        }
        return height;
    }

    private int convertDPtoPX(float dp, DisplayMetrics displayMetrics) {
        DisplayMetrics dm = displayMetrics;
        if (dm == null) {
            dm = this.mAnchorView.getContext().getResources().getDisplayMetrics();
        }
        return (int) (TypedValue.applyDimension(1, dp, dm) + 0.5f);
    }
}
