package com.samsung.android.multiwindow.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Slog;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.internal.R;
import com.samsung.android.multiwindow.MultiWindowFeatures;

public class GuideView extends FrameLayout {
    public static final int DOCKING = 2;
    public static final int MOVE = 3;
    public static final int NORMAL = 0;
    private static final String TAG = "GuideView";
    public static final int WARNING = 1;
    public static final int WARNING_NOT_SUPPORT = 4;
    private boolean mAttached;
    private View mBorderView;
    private float mDimAmount;
    Display mDisplay;
    private int mDockingHightlightColor;
    private ImageView mFakeHeaderView;
    private Drawable mGuideDrawable;
    private int mGuidePaddingBottom;
    private int mGuidePaddingLeft;
    private int mGuidePaddingRight;
    private int mGuidePaddingTop;
    private int mGuideState;
    private int mGuideWidth;
    private boolean mHeaderViewVisibility;
    private boolean mIsSupportWindowController;
    private Rect mLastRect;
    private int mMultiWindowFlags;
    private boolean mShowing;
    private IBinder mToken;
    private WindowManager mWindowManager;
    private int mWindowType;

    public GuideView(View parentView, int windowType) {
        this(parentView.getContext(), parentView.getWindowToken(), windowType);
    }

    public GuideView(Context context, IBinder token, int windowType) {
        this(context, token, windowType, null);
    }

    public GuideView(Context context, IBinder token, int windowType, Display display) {
        super(context);
        this.mAttached = false;
        this.mShowing = false;
        this.mGuideState = 0;
        this.mHeaderViewVisibility = false;
        this.mWindowType = LayoutParams.TYPE_MULTI_WINDOW_GUIDE_VIEW;
        this.mDimAmount = LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        this.mLastRect = new Rect();
        this.mIsSupportWindowController = false;
        this.mDockingHightlightColor = 0;
        this.mDisplay = null;
        this.mToken = token;
        this.mWindowType = windowType;
        this.mWindowManager = (WindowManager) getContext().getSystemService("window");
        this.mIsSupportWindowController = MultiWindowFeatures.isSupportStyleTransition(this.mContext);
        this.mDockingHightlightColor = this.mContext.getResources().getColor(R.color.multiwindow_docking_hightlight_color);
        this.mGuideWidth = this.mContext.getResources().getDimensionPixelSize(R.dimen.center_bar_guide_width);
        if (MultiWindowFeatures.isSupportOpenTheme(this.mContext)) {
            this.mDockingHightlightColor = (this.mDockingHightlightColor & 16777215) | -1728053248;
        }
    }

    public void dismiss() {
        synchronized (this) {
            if (this.mAttached) {
                this.mWindowManager.removeViewImmediate(this);
                removeAllViews();
                this.mAttached = false;
                this.mShowing = false;
                this.mGuideState = 0;
            }
        }
    }

    public boolean getGuideViewAttached() {
        return this.mAttached;
    }

    public void init() {
        if (!this.mAttached) {
            if (this.mBorderView == null) {
                this.mBorderView = new View(getContext());
            }
            if (this.mIsSupportWindowController && this.mFakeHeaderView == null) {
                this.mFakeHeaderView = new ImageView(getContext());
            }
            refreshBackground();
            Drawable bd = this.mBorderView.getBackground();
            addView(this.mBorderView, (ViewGroup.LayoutParams) new FrameLayout.LayoutParams(bd.getIntrinsicWidth(), bd.getIntrinsicHeight()));
            this.mBorderView.setVisibility(4);
            if (this.mIsSupportWindowController) {
                addView(this.mFakeHeaderView, (ViewGroup.LayoutParams) new FrameLayout.LayoutParams(this.mGuideWidth, this.mGuideWidth));
                setFakeHeaderVisibility(false);
            }
            resetResolvedLayoutDirection();
            setLayoutDirection(0);
            this.mWindowManager.addView(this, generateLayoutParam());
            this.mShowing = false;
            this.mAttached = true;
        }
    }

    public void show(int x, int y, int width, int height) {
        show(x, y, width, height, false);
    }

    public void show(int x, int y, int width, int height, boolean bDocking) {
        synchronized (this) {
            if (this.mBorderView == null || (this.mIsSupportWindowController && this.mFakeHeaderView == null)) {
                Slog.e(TAG, "mBorder or mFakeHeaderView is null");
                return;
            }
            FrameLayout.LayoutParams vlp = (FrameLayout.LayoutParams) this.mBorderView.getLayoutParams();
            int totalWidth = (this.mGuidePaddingLeft + width) + this.mGuidePaddingRight;
            int totalHeight = (this.mGuidePaddingTop + height) + this.mGuidePaddingBottom;
            vlp.leftMargin = x - this.mGuidePaddingLeft;
            vlp.topMargin = y - this.mGuidePaddingTop;
            vlp.width = totalWidth;
            vlp.height = totalHeight;
            if (bDocking) {
                if (this.mContext.getResources().getConfiguration().orientation == 1) {
                    vlp.width = -1;
                } else {
                    vlp.height = -1;
                }
            }
            if (this.mIsSupportWindowController) {
                FrameLayout.LayoutParams flp = (FrameLayout.LayoutParams) this.mFakeHeaderView.getLayoutParams();
                flp.leftMargin = ((width - flp.width) / 2) + x;
                flp.topMargin = y - (flp.height / 2);
                this.mFakeHeaderView.setLayoutParams(flp);
            }
            if (!this.mShowing) {
                this.mBorderView.setVisibility(0);
                if (this.mIsSupportWindowController) {
                    if (this.mGuideState != 3) {
                        setFakeHeaderVisibility(false);
                    } else {
                        setFakeHeaderVisibility(true);
                    }
                }
                this.mShowing = true;
            }
            this.mBorderView.requestLayout();
            this.mBorderView.invalidate();
            this.mLastRect.set(x, y, x + width, y + height);
        }
    }

    public void hide() {
        if (this.mShowing) {
            this.mShowing = false;
            this.mBorderView.setVisibility(4);
            if (this.mIsSupportWindowController) {
                setFakeHeaderVisibility(false);
            }
        }
    }

    public Rect getLastBounds() {
        return this.mLastRect;
    }

    public void setMultiWindowFlags(int multiWindowFlags) {
        this.mMultiWindowFlags = multiWindowFlags;
    }

    private LayoutParams generateLayoutParam() {
        LayoutParams lp = new LayoutParams();
        lp.setTitle("MultiWindow GuideView");
        lp.gravity = 8388659;
        lp.width = -1;
        lp.width = -1;
        lp.format = -2;
        lp.type = this.mWindowType;
        lp.token = this.mToken;
        lp.flags = 792;
        if (this.mDimAmount != LayoutParams.BRIGHTNESS_OVERRIDE_NONE) {
            lp.dimAmount = this.mDimAmount;
            lp.flags |= 2;
        }
        lp.privateFlags = this.mMultiWindowFlags;
        return lp;
    }

    public void setGuideState(int state) {
        if (this.mGuideState != state) {
            this.mGuideState = state;
            refreshBackground();
        }
    }

    public int getGuideState() {
        return this.mGuideState;
    }

    public void setFakeHeaderVisibility(boolean vis) {
        if (this.mIsSupportWindowController && this.mFakeHeaderView != null) {
            this.mHeaderViewVisibility = vis;
            if (vis) {
                this.mFakeHeaderView.setVisibility(0);
            } else {
                this.mFakeHeaderView.setVisibility(4);
            }
        }
    }

    public void setGuideBitmap(Bitmap bitmap) {
        this.mGuideDrawable = new BitmapDrawable(getContext().getResources(), bitmap);
        refreshBackground();
    }

    private void refreshBackground() {
        if (this.mBorderView != null) {
            if (!this.mIsSupportWindowController || this.mFakeHeaderView != null) {
                if (this.mGuideDrawable != null) {
                    this.mBorderView.setBackground(this.mGuideDrawable);
                    if (this.mIsSupportWindowController) {
                        this.mFakeHeaderView.setVisibility(4);
                        return;
                    }
                    return;
                }
                switch (this.mGuideState) {
                    case 0:
                        this.mBorderView.setBackgroundResource(R.drawable.mw_window_popup_bg_pressed_def);
                        if (this.mIsSupportWindowController) {
                            this.mFakeHeaderView.setImageResource(R.drawable.multi_window_drag_drop_handler_press);
                            this.mFakeHeaderView.setVisibility(4);
                            return;
                        }
                        return;
                    case 1:
                        this.mBorderView.setBackgroundResource(R.drawable.mw_window_popup_bg_out_def);
                        if (this.mIsSupportWindowController) {
                            this.mFakeHeaderView.setImageResource(R.drawable.multi_window_drag_drop_handler_minimum);
                            this.mFakeHeaderView.setVisibility(4);
                            return;
                        }
                        return;
                    case 2:
                        this.mBorderView.setBackgroundColor(this.mDockingHightlightColor);
                        if (this.mIsSupportWindowController) {
                            this.mFakeHeaderView.setImageResource(R.drawable.multi_window_header_move_guide);
                            this.mFakeHeaderView.setVisibility(4);
                            return;
                        }
                        return;
                    case 3:
                        this.mBorderView.setBackgroundResource(R.drawable.mw_window_popup_bg_pressed_def);
                        if (this.mIsSupportWindowController) {
                            this.mFakeHeaderView.setImageResource(R.drawable.multi_window_header_move_guide);
                            this.mFakeHeaderView.setVisibility(0);
                            return;
                        }
                        return;
                    case 4:
                        this.mBorderView.setBackgroundResource(R.drawable.mw_window_gesture_not_support);
                        if (this.mIsSupportWindowController) {
                            this.mFakeHeaderView.setImageResource(R.drawable.multi_window_drag_drop_handler_press);
                            this.mFakeHeaderView.setVisibility(4);
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public boolean isShowing() {
        return this.mShowing;
    }

    public void setDimAmount(float amount) {
        this.mDimAmount = amount;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        dismiss();
    }

    public void setGuidePadding(int left, int top, int right, int bottom) {
        synchronized (this) {
            this.mGuidePaddingLeft = left;
            this.mGuidePaddingTop = top;
            this.mGuidePaddingRight = right;
            this.mGuidePaddingBottom = bottom;
        }
    }
}
