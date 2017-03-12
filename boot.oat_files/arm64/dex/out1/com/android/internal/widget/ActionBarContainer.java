package com.android.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Settings$System;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.android.internal.R;

public class ActionBarContainer extends FrameLayout {
    protected static boolean mIsThemeDeviceDefaultFamily;
    private View mActionBarView;
    private View mActionContextView;
    private Drawable mBackground;
    private int mHeight;
    private boolean mIgnoreConfigurationUpdate;
    private boolean mIsSplit;
    private boolean mIsStacked;
    private boolean mIsTabOnTop;
    private boolean mIsTransitioning;
    private Drawable mSplitBackground;
    private Drawable mStackedBackground;
    private View mTabContainer;
    private boolean mWasChangedTheme;

    private class ActionBarBackgroundDrawable extends Drawable {
        private ActionBarBackgroundDrawable() {
        }

        public void draw(Canvas canvas) {
            if (!ActionBarContainer.this.mIsSplit) {
                if (ActionBarContainer.this.mBackground != null) {
                    ActionBarContainer.this.mBackground.draw(canvas);
                }
                if (ActionBarContainer.this.mStackedBackground != null && ActionBarContainer.this.mIsStacked) {
                    ActionBarContainer.this.mStackedBackground.draw(canvas);
                }
            } else if (ActionBarContainer.this.mSplitBackground != null) {
                ActionBarContainer.this.mSplitBackground.draw(canvas);
            }
        }

        public void getOutline(Outline outline) {
            if (ActionBarContainer.this.mIsSplit) {
                if (ActionBarContainer.this.mSplitBackground != null) {
                    ActionBarContainer.this.mSplitBackground.getOutline(outline);
                }
            } else if (ActionBarContainer.this.mBackground != null) {
                ActionBarContainer.this.mBackground.getOutline(outline);
            }
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        public int getOpacity() {
            if (ActionBarContainer.this.mIsSplit) {
                if (ActionBarContainer.this.mSplitBackground != null && ActionBarContainer.this.mSplitBackground.getOpacity() == -1) {
                    return -1;
                }
            } else if (ActionBarContainer.this.mIsStacked && (ActionBarContainer.this.mStackedBackground == null || ActionBarContainer.this.mStackedBackground.getOpacity() != -1)) {
                return 0;
            } else {
                if (!(ActionBarContainer.isCollapsed(ActionBarContainer.this.mActionBarView) || ActionBarContainer.this.mBackground == null || ActionBarContainer.this.mBackground.getOpacity() != -1)) {
                    return -1;
                }
            }
            return 0;
        }
    }

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    public ActionBarContainer(Context context, AttributeSet attrs) {
        boolean z = true;
        super(context, attrs);
        this.mIsTabOnTop = false;
        setBackground(new ActionBarBackgroundDrawable());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionBar);
        this.mBackground = a.getDrawable(2);
        this.mStackedBackground = a.getDrawable(18);
        this.mHeight = a.getDimensionPixelSize(4, -1);
        if (getId() == R.id.split_action_bar) {
            this.mIsSplit = true;
            this.mSplitBackground = a.getDrawable(19);
        }
        a.recycle();
        boolean z2 = this.mIsSplit ? this.mSplitBackground == null : this.mBackground == null && this.mStackedBackground == null;
        setWillNotDraw(z2);
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
        if (outValue.data != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        mIsThemeDeviceDefaultFamily = z2;
        String packageName = Settings$System.getString(context.getContentResolver(), "current_sec_active_themepackage");
        if (!(mIsThemeDeviceDefaultFamily && packageName != null && context.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_560)) {
            z = false;
        }
        this.mWasChangedTheme = z;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        TypedArray a = getContext().obtainStyledAttributes(null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        if (mIsThemeDeviceDefaultFamily && !this.mIgnoreConfigurationUpdate) {
            this.mBackground = a.getDrawable(2);
            if (!(this.mActionBarView == null || this.mBackground == null)) {
                int height = this.mActionBarView.getBottom();
                if (this.mWasChangedTheme && (this.mBackground instanceof BitmapDrawable) && this.mBackground.getIntrinsicWidth() < this.mActionBarView.getWidth()) {
                    setElevation(0.0f);
                    height = (int) ((((float) this.mActionBarView.getWidth()) / ((float) this.mBackground.getIntrinsicWidth())) * ((float) this.mBackground.getIntrinsicHeight()));
                }
                this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), height);
            }
            this.mStackedBackground = a.getDrawable(18);
        }
        this.mHeight = a.getDimensionPixelSize(4, -1);
        a.recycle();
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = findViewById(R.id.action_bar);
        this.mActionContextView = findViewById(R.id.action_context_bar);
    }

    public void setPrimaryBackground(Drawable bg) {
        boolean z = true;
        this.mIgnoreConfigurationUpdate = true;
        if (this.mBackground != null) {
            this.mBackground.setCallback(null);
            unscheduleDrawable(this.mBackground);
        }
        this.mBackground = bg;
        if (bg != null) {
            bg.setCallback(this);
            if (this.mActionBarView != null) {
                int height = this.mActionBarView.getBottom();
                if (this.mWasChangedTheme && (this.mBackground instanceof BitmapDrawable) && this.mBackground.getIntrinsicWidth() < this.mActionBarView.getWidth()) {
                    setElevation(0.0f);
                    height = (int) ((((float) this.mActionBarView.getWidth()) / ((float) this.mBackground.getIntrinsicWidth())) * ((float) this.mBackground.getIntrinsicHeight()));
                }
                this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), height);
            }
        }
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                z = false;
            }
        } else if (!(this.mBackground == null && this.mStackedBackground == null)) {
            z = false;
        }
        setWillNotDraw(z);
        invalidate();
        invalidateOutline();
    }

    public void setStackedBackground(Drawable bg) {
        boolean z = true;
        if (this.mStackedBackground != null) {
            this.mStackedBackground.setCallback(null);
            unscheduleDrawable(this.mStackedBackground);
        }
        this.mStackedBackground = bg;
        if (bg != null) {
            bg.setCallback(this);
            if (this.mIsStacked && this.mStackedBackground != null) {
                this.mStackedBackground.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
            }
        }
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                z = false;
            }
        } else if (!(this.mBackground == null && this.mStackedBackground == null)) {
            z = false;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setSplitBackground(Drawable bg) {
        boolean z = true;
        if (this.mSplitBackground != null) {
            this.mSplitBackground.setCallback(null);
            unscheduleDrawable(this.mSplitBackground);
        }
        this.mSplitBackground = bg;
        if (bg != null) {
            bg.setCallback(this);
            if (this.mIsSplit && this.mSplitBackground != null) {
                this.mSplitBackground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            }
        }
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                z = false;
            }
        } else if (!(this.mBackground == null && this.mStackedBackground == null)) {
            z = false;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setVisibility(int visibility) {
        boolean isVisible;
        super.setVisibility(visibility);
        if (visibility == 0) {
            isVisible = true;
        } else {
            isVisible = false;
        }
        if (this.mBackground != null) {
            this.mBackground.setVisible(isVisible, false);
        }
        if (this.mStackedBackground != null) {
            this.mStackedBackground.setVisible(isVisible, false);
        }
        if (this.mSplitBackground != null) {
            this.mSplitBackground.setVisible(isVisible, false);
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        return (who == this.mBackground && !this.mIsSplit) || ((who == this.mStackedBackground && this.mIsStacked) || ((who == this.mSplitBackground && this.mIsSplit) || super.verifyDrawable(who)));
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mBackground != null && this.mBackground.isStateful()) {
            this.mBackground.setState(getDrawableState());
        }
        if (this.mStackedBackground != null && this.mStackedBackground.isStateful()) {
            this.mStackedBackground.setState(getDrawableState());
        }
        if (this.mSplitBackground != null && this.mSplitBackground.isStateful()) {
            this.mSplitBackground.setState(getDrawableState());
        }
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mBackground != null) {
            this.mBackground.jumpToCurrentState();
        }
        if (this.mStackedBackground != null) {
            this.mStackedBackground.jumpToCurrentState();
        }
        if (this.mSplitBackground != null) {
            this.mSplitBackground.jumpToCurrentState();
        }
    }

    public void onResolveDrawables(int layoutDirection) {
        super.onResolveDrawables(layoutDirection);
        if (this.mBackground != null) {
            this.mBackground.setLayoutDirection(layoutDirection);
        }
        if (this.mStackedBackground != null) {
            this.mStackedBackground.setLayoutDirection(layoutDirection);
        }
        if (this.mSplitBackground != null) {
            this.mSplitBackground.setLayoutDirection(layoutDirection);
        }
    }

    public void setTransitioning(boolean isTransitioning) {
        this.mIsTransitioning = isTransitioning;
        setDescendantFocusability(isTransitioning ? 393216 : 262144);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.mIsTransitioning || super.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return true;
    }

    public boolean onHoverEvent(MotionEvent ev) {
        super.onHoverEvent(ev);
        return true;
    }

    public void setTabContainer(ScrollingTabContainerView tabView) {
        if (this.mTabContainer != null) {
            removeView(this.mTabContainer);
        }
        this.mTabContainer = tabView;
        if (tabView != null) {
            addView(tabView);
            LayoutParams lp = tabView.getLayoutParams();
            lp.width = -1;
            lp.height = -2;
            tabView.setAllowCollapse(false);
        }
    }

    public View getTabContainer() {
        return this.mTabContainer;
    }

    public ActionMode startActionModeForChild(View child, Callback callback, int type) {
        if (type != 0) {
            return super.startActionModeForChild(child, callback, type);
        }
        return null;
    }

    private static boolean isCollapsed(View view) {
        return view == null || view.getVisibility() == 8 || view.getMeasuredHeight() == 0;
    }

    private int getMeasuredHeightWithMargins(View view) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
        return (view.getMeasuredHeight() + lp.topMargin) + lp.bottomMargin;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mActionBarView == null && MeasureSpec.getMode(heightMeasureSpec) == Integer.MIN_VALUE && this.mHeight >= 0) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(this.mHeight, MeasureSpec.getSize(heightMeasureSpec)), Integer.MIN_VALUE);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mActionBarView != null && this.mTabContainer != null && this.mTabContainer.getVisibility() != 8) {
            int nonTabMaxHeight = 0;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = getChildAt(i);
                if (child != this.mTabContainer) {
                    nonTabMaxHeight = Math.max(nonTabMaxHeight, isCollapsed(child) ? 0 : getMeasuredHeightWithMargins(child));
                }
            }
            setMeasuredDimension(getMeasuredWidth(), Math.min(getMeasuredHeightWithMargins(this.mTabContainer) + nonTabMaxHeight, MeasureSpec.getMode(heightMeasureSpec) == Integer.MIN_VALUE ? MeasureSpec.getSize(heightMeasureSpec) : Integer.MAX_VALUE));
        }
    }

    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View tabContainer = this.mTabContainer;
        boolean hasTabs = (tabContainer == null || tabContainer.getVisibility() == 8) ? false : true;
        if (!(tabContainer == null || tabContainer.getVisibility() == 8)) {
            int containerHeight = getMeasuredHeight();
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) tabContainer.getLayoutParams();
            int tabHeight = tabContainer.getMeasuredHeight();
            if (this.mIsTabOnTop) {
                int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    if (!(child == this.mTabContainer || isCollapsed(this.mActionBarView))) {
                        child.offsetTopAndBottom(tabHeight);
                    }
                }
                this.mTabContainer.layout(l, 0, r, tabHeight);
            } else {
                tabContainer.layout(l, (containerHeight - tabHeight) - lp.bottomMargin, r, containerHeight - lp.bottomMargin);
            }
        }
        boolean needsInvalidate = false;
        if (!this.mIsSplit) {
            if (this.mBackground != null) {
                if (this.mActionBarView.getVisibility() == 0) {
                    int height = this.mActionBarView.getBottom();
                    if (this.mWasChangedTheme && (this.mBackground instanceof BitmapDrawable) && this.mBackground.getIntrinsicWidth() < this.mActionBarView.getWidth()) {
                        setElevation(0.0f);
                        height = (int) ((((float) this.mActionBarView.getWidth()) / ((float) this.mBackground.getIntrinsicWidth())) * ((float) this.mBackground.getIntrinsicHeight()));
                    }
                    this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), height);
                } else if (this.mActionContextView == null || this.mActionContextView.getVisibility() != 0) {
                    this.mBackground.setBounds(0, 0, 0, 0);
                } else {
                    this.mBackground.setBounds(this.mActionContextView.getLeft(), this.mActionContextView.getTop(), this.mActionContextView.getRight(), this.mActionContextView.getBottom());
                }
                needsInvalidate = true;
            }
            this.mIsStacked = hasTabs;
            if (hasTabs && this.mStackedBackground != null) {
                this.mStackedBackground.setBounds(tabContainer.getLeft(), tabContainer.getTop(), tabContainer.getRight(), tabContainer.getBottom());
                needsInvalidate = true;
            }
        } else if (this.mSplitBackground != null) {
            this.mSplitBackground.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
            needsInvalidate = true;
        }
        if (needsInvalidate) {
            invalidate();
        }
    }

    public int getContainerHeight() {
        return this.mHeight;
    }

    public void twPutTabsOnTop(boolean isTabOnTop) {
        this.mIsTabOnTop = isTabOnTop;
    }
}
