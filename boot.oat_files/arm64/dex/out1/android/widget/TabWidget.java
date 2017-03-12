package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Settings$System;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.accessibility.AccessibilityEvent;
import android.widget.LinearLayout.LayoutParams;
import com.android.internal.R;

public class TabWidget extends LinearLayout implements OnFocusChangeListener {
    private final Rect mBounds;
    private boolean mChangedTheme;
    private float mDefaultTextSize;
    private boolean mDrawBottomStrips;
    private int[] mImposedTabWidths;
    private int mImposedTabsHeight;
    private boolean mIsThemeDeviceDefaultFamily;
    private Drawable mLeftStrip;
    private float mMaxFontScale;
    private Drawable mRightStrip;
    private int mSelectedTab;
    private OnTabSelectionChanged mSelectionChangedListener;
    private boolean mStripMoved;

    interface OnTabSelectionChanged {
        void onTabSelectionChanged(int i, boolean z);
    }

    private class TabClickListener implements OnClickListener {
        private final int mTabIndex;

        private TabClickListener(int tabIndex) {
            this.mTabIndex = tabIndex;
        }

        public void onClick(View v) {
            TabWidget.this.mSelectionChangedListener.onTabSelectionChanged(this.mTabIndex, true);
        }
    }

    public TabWidget(Context context) {
        this(context, null);
    }

    public TabWidget(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.tabWidgetStyle);
    }

    public TabWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TabWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        boolean z;
        boolean z2 = true;
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mSelectedTab = -1;
        this.mDrawBottomStrips = true;
        this.mBounds = new Rect();
        this.mImposedTabsHeight = -1;
        this.mMaxFontScale = 1.2f;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabWidget, defStyleAttr, defStyleRes);
        setStripEnabled(a.getBoolean(3, true));
        setLeftStripDrawable(a.getDrawable(1));
        setRightStripDrawable(a.getDrawable(2));
        a.recycle();
        initTabWidget();
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
        if (outValue.data != 0) {
            z = true;
        } else {
            z = false;
        }
        this.mIsThemeDeviceDefaultFamily = z;
        String themeName = Settings$System.getString(context.getContentResolver(), "current_sec_active_themepackage");
        if (!(this.mIsThemeDeviceDefaultFamily && themeName != null && context.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_560)) {
            z2 = false;
        }
        this.mChangedTheme = z2;
        if (this.mIsThemeDeviceDefaultFamily) {
            a = context.getTheme().obtainStyledAttributes(null, R.styleable.Theme, 0, 0);
            int tabTextAppearnceId = a.getResourceId(143, 0);
            a.recycle();
            a = getContext().obtainStyledAttributes(tabTextAppearnceId, R.styleable.TextAppearance);
            outValue = a.peekValue(0);
            a.recycle();
            this.mDefaultTextSize = TypedValue.complexToFloat(outValue.data);
        }
    }

    protected void twUpdateBackgroundBounds() {
        Drawable background = getBackground();
        if (background != null) {
            View target = (View) getParent();
            if (!this.mChangedTheme || target == null || !(background instanceof BitmapDrawable) || background.getIntrinsicWidth() >= target.getWidth()) {
                super.twUpdateBackgroundBounds();
                return;
            }
            background.setBounds(0, 0, target.getRight(), (int) ((((float) target.getWidth()) / ((float) background.getIntrinsicWidth())) * ((float) background.getIntrinsicHeight())));
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mChangedTheme) {
            twUpdateBackgroundBounds();
        }
        if (this.mIsThemeDeviceDefaultFamily) {
            float fontScale = getContext().getResources().getConfiguration().fontScale;
            if (fontScale > this.mMaxFontScale) {
                fontScale = this.mMaxFontScale;
            }
            int count = getTabCount();
            for (int i = 0; i < count; i++) {
                View child = getChildTabViewAt(i);
                if (child != null) {
                    View vTextView = child.findViewById(R.id.title);
                    if (vTextView != null && (vTextView instanceof TextView)) {
                        ((TextView) vTextView).setTextSize(1, this.mDefaultTextSize * fontScale);
                    }
                }
            }
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.mStripMoved = true;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    protected int getChildDrawingOrder(int childCount, int i) {
        if (this.mSelectedTab == -1) {
            return i;
        }
        if (i == childCount - 1) {
            return this.mSelectedTab;
        }
        if (i >= this.mSelectedTab) {
            return i + 1;
        }
        return i;
    }

    private void initTabWidget() {
        setChildrenDrawingOrderEnabled(true);
        Context context = this.mContext;
        if (context.getApplicationInfo().targetSdkVersion <= 4) {
            if (this.mLeftStrip == null) {
                this.mLeftStrip = context.getDrawable(R.drawable.tab_bottom_left_v4);
            }
            if (this.mRightStrip == null) {
                this.mRightStrip = context.getDrawable(R.drawable.tab_bottom_right_v4);
            }
        } else {
            if (this.mLeftStrip == null) {
                this.mLeftStrip = context.getDrawable(R.drawable.tab_bottom_left);
            }
            if (this.mRightStrip == null) {
                this.mRightStrip = context.getDrawable(R.drawable.tab_bottom_right);
            }
        }
        setFocusable(true);
        setOnFocusChangeListener(this);
    }

    void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        if (!isMeasureWithLargestChildEnabled() && this.mImposedTabsHeight >= 0) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(this.mImposedTabWidths[childIndex] + totalWidth, 1073741824);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(this.mImposedTabsHeight, 1073741824);
        }
        super.measureChildBeforeLayout(child, childIndex, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
    }

    void measureHorizontal(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == 0) {
            super.measureHorizontal(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int unspecifiedWidth = MeasureSpec.makeSafeMeasureSpec(width, 0);
        this.mImposedTabsHeight = -1;
        super.measureHorizontal(unspecifiedWidth, heightMeasureSpec);
        int extraWidth = getMeasuredWidth() - width;
        if (extraWidth > 0) {
            int i;
            int count = getChildCount();
            int childCount = 0;
            for (i = 0; i < count; i++) {
                if (getChildAt(i).getVisibility() != 8) {
                    childCount++;
                }
            }
            if (childCount > 0) {
                if (this.mImposedTabWidths == null || this.mImposedTabWidths.length != count) {
                    this.mImposedTabWidths = new int[count];
                }
                for (i = 0; i < count; i++) {
                    View child = getChildAt(i);
                    if (child.getVisibility() != 8) {
                        int childWidth = child.getMeasuredWidth();
                        int newWidth = Math.max(0, childWidth - (extraWidth / childCount));
                        this.mImposedTabWidths[i] = newWidth;
                        extraWidth -= childWidth - newWidth;
                        childCount--;
                        this.mImposedTabsHeight = Math.max(this.mImposedTabsHeight, child.getMeasuredHeight());
                    }
                }
            }
        }
        super.measureHorizontal(widthMeasureSpec, heightMeasureSpec);
    }

    public View getChildTabViewAt(int index) {
        return getChildAt(index);
    }

    public int getTabCount() {
        return getChildCount();
    }

    public void setDividerDrawable(Drawable drawable) {
        super.setDividerDrawable(drawable);
    }

    public void setDividerDrawable(int resId) {
        setDividerDrawable(this.mContext.getDrawable(resId));
    }

    public void setLeftStripDrawable(Drawable drawable) {
        this.mLeftStrip = drawable;
        requestLayout();
        invalidate();
    }

    public void setLeftStripDrawable(int resId) {
        setLeftStripDrawable(this.mContext.getDrawable(resId));
    }

    public void setRightStripDrawable(Drawable drawable) {
        this.mRightStrip = drawable;
        requestLayout();
        invalidate();
    }

    public void setRightStripDrawable(int resId) {
        setRightStripDrawable(this.mContext.getDrawable(resId));
    }

    public void setStripEnabled(boolean stripEnabled) {
        this.mDrawBottomStrips = stripEnabled;
        invalidate();
    }

    public boolean isStripEnabled() {
        return this.mDrawBottomStrips;
    }

    public void childDrawableStateChanged(View child) {
        if (this.mSelectedTab != -1 && getTabCount() > 0 && child == getChildTabViewAt(this.mSelectedTab)) {
            invalidate();
        }
        super.childDrawableStateChanged(child);
    }

    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (getTabCount() != 0 && this.mDrawBottomStrips) {
            View selectedChild = getChildTabViewAt(this.mSelectedTab);
            Drawable leftStrip = this.mLeftStrip;
            Drawable rightStrip = this.mRightStrip;
            leftStrip.setState(selectedChild.getDrawableState());
            rightStrip.setState(selectedChild.getDrawableState());
            if (this.mStripMoved) {
                Rect bounds = this.mBounds;
                bounds.left = selectedChild.getLeft();
                bounds.right = selectedChild.getRight();
                int myHeight = getHeight();
                leftStrip.setBounds(Math.min(0, bounds.left - leftStrip.getIntrinsicWidth()), myHeight - leftStrip.getIntrinsicHeight(), bounds.left, myHeight);
                rightStrip.setBounds(bounds.right, myHeight - rightStrip.getIntrinsicHeight(), Math.max(getWidth(), bounds.right + rightStrip.getIntrinsicWidth()), myHeight);
                this.mStripMoved = false;
            }
            leftStrip.draw(canvas);
            rightStrip.draw(canvas);
        }
    }

    public void setCurrentTab(int index) {
        if (index >= 0 && index < getTabCount() && index != this.mSelectedTab) {
            if (this.mSelectedTab != -1) {
                getChildTabViewAt(this.mSelectedTab).setSelected(false);
            }
            this.mSelectedTab = index;
            getChildTabViewAt(this.mSelectedTab).setSelected(true);
            this.mStripMoved = true;
            if (isShown()) {
                sendAccessibilityEvent(4);
            }
        }
    }

    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        if (this.mSelectedTab != -1) {
            View tabView = getChildTabViewAt(this.mSelectedTab);
            if (tabView != null && tabView.getVisibility() == 0) {
                return tabView.dispatchPopulateAccessibilityEvent(event);
            }
        }
        return false;
    }

    public CharSequence getAccessibilityClassName() {
        return TabWidget.class.getName();
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setItemCount(getTabCount());
        event.setCurrentItemIndex(this.mSelectedTab);
    }

    public void sendAccessibilityEventUncheckedInternal(AccessibilityEvent event) {
        if (event.getEventType() == 8 && isFocused()) {
            event.recycle();
        } else {
            super.sendAccessibilityEventUncheckedInternal(event);
        }
    }

    public void focusCurrentTab(int index) {
        int oldTab = this.mSelectedTab;
        setCurrentTab(index);
        if (oldTab != index) {
            getChildTabViewAt(index).requestFocus();
        }
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        int count = getTabCount();
        for (int i = 0; i < count; i++) {
            getChildTabViewAt(i).setEnabled(enabled);
        }
    }

    public void addView(View child) {
        if (child.getLayoutParams() == null) {
            LayoutParams lp = new LayoutParams(0, -1, 1.0f);
            lp.setMargins(0, 0, 0, 0);
            child.setLayoutParams(lp);
        }
        child.setFocusable(true);
        child.setClickable(true);
        super.addView(child);
        child.setOnClickListener(new TabClickListener(getTabCount() - 1));
        child.setOnFocusChangeListener(this);
    }

    public void removeAllViews() {
        super.removeAllViews();
        this.mSelectedTab = -1;
    }

    void setTabSelectionListener(OnTabSelectionChanged listener) {
        this.mSelectionChangedListener = listener;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (v == this && hasFocus && getTabCount() > 0 && this.mSelectedTab != -1) {
            getChildTabViewAt(this.mSelectedTab).requestFocus();
        } else if (hasFocus) {
            int numTabs = getTabCount();
            for (int i = 0; i < numTabs; i++) {
                if (getChildTabViewAt(i) == v) {
                    setCurrentTab(i);
                    this.mSelectionChangedListener.onTabSelectionChanged(i, false);
                    if (isShown()) {
                        sendAccessibilityEvent(8);
                        return;
                    }
                    return;
                }
            }
        }
    }
}
