package com.android.internal.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.Settings$System;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ActionMenuPresenter;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.R;
import com.android.internal.view.menu.MenuBuilder;
import com.samsung.android.multiwindow.MultiWindowStyle;

public class ActionBarContextView extends AbsActionBarView {
    private static final String TAG = "ActionBarContextView";
    private boolean mChangedTheme;
    private View mClose;
    private int mCloseItemLayout;
    private View mCustomView;
    private float mMaxFontScale;
    private Drawable mSplitBackground;
    private CharSequence mSubtitle;
    private int mSubtitleStyleRes;
    private TextView mSubtitleView;
    private CharSequence mTitle;
    private LinearLayout mTitleLayout;
    private boolean mTitleOptional;
    private int mTitleStyleRes;
    private TextView mTitleView;

    public ActionBarContextView(Context context) {
        this(context, null);
    }

    public ActionBarContextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.actionModeStyle);
    }

    public ActionBarContextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ActionBarContextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        boolean z = true;
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mMaxFontScale = 1.2f;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionMode, defStyleAttr, defStyleRes);
        setBackground(a.getDrawable(0));
        this.mTitleStyleRes = a.getResourceId(2, 0);
        this.mSubtitleStyleRes = a.getResourceId(3, 0);
        this.mContentHeight = a.getLayoutDimension(1, 0);
        this.mSplitBackground = a.getDrawable(4);
        this.mCloseItemLayout = a.getResourceId(5, R.layout.action_mode_close_item);
        a.recycle();
        String packageName = Settings$System.getString(context.getContentResolver(), "current_sec_active_themepackage");
        if (!(mIsThemeDeviceDefaultFamily && packageName != null && context.getResources().getDisplayMetrics().densityDpi == DisplayMetrics.DENSITY_560)) {
            z = false;
        }
        this.mChangedTheme = z;
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mIsThemeDeviceDefaultFamily) {
            TypedArray a = this.mContext.obtainStyledAttributes(null, R.styleable.ActionMode, R.attr.actionModeStyle, 0);
            setBackground(a.getDrawable(0));
            a.recycle();
        }
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.hideOverflowMenu();
            this.mActionMenuPresenter.hideSubMenus();
        }
    }

    public void setSplitToolbar(boolean split) {
        if (this.mSplitActionBar != split) {
            if (this.mActionMenuPresenter != null) {
                LayoutParams layoutParams = new LayoutParams(-2, -1);
                ViewGroup oldParent;
                if (split) {
                    this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
                    this.mActionMenuPresenter.setItemLimit(Integer.MAX_VALUE);
                    layoutParams.width = -1;
                    layoutParams.height = this.mContentHeight;
                    this.mMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                    this.mMenuView.setBackground(this.mSplitBackground);
                    oldParent = (ViewGroup) this.mMenuView.getParent();
                    if (oldParent != null) {
                        oldParent.removeView(this.mMenuView);
                    }
                    this.mSplitView.addView(this.mMenuView, layoutParams);
                } else {
                    this.mMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                    this.mMenuView.setBackground(null);
                    oldParent = (ViewGroup) this.mMenuView.getParent();
                    if (oldParent != null) {
                        oldParent.removeView(this.mMenuView);
                    }
                    addView(this.mMenuView, layoutParams);
                }
            }
            super.setSplitToolbar(split);
        }
    }

    public void setContentHeight(int height) {
        this.mContentHeight = height;
    }

    public void setCustomView(View view) {
        if (this.mCustomView != null) {
            removeView(this.mCustomView);
        }
        this.mCustomView = view;
        if (!(view == null || this.mTitleLayout == null)) {
            removeView(this.mTitleLayout);
            this.mTitleLayout = null;
        }
        if (view != null) {
            addView(view);
        }
        requestLayout();
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title;
        initTitle();
    }

    public void setSubtitle(CharSequence subtitle) {
        this.mSubtitle = subtitle;
        initTitle();
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    private void initTitle() {
        boolean hasTitle;
        boolean hasSubtitle;
        int i;
        int i2 = 8;
        if (this.mTitleLayout == null) {
            LayoutInflater.from(getContext()).inflate((int) R.layout.action_bar_title_item, (ViewGroup) this);
            this.mTitleLayout = (LinearLayout) getChildAt(getChildCount() - 1);
            this.mTitleView = (TextView) this.mTitleLayout.findViewById(R.id.action_bar_title);
            this.mSubtitleView = (TextView) this.mTitleLayout.findViewById(R.id.action_bar_subtitle);
            if (this.mTitleStyleRes != 0) {
                this.mTitleView.setTextAppearance(this.mTitleStyleRes);
            }
            if (this.mSubtitleStyleRes != 0) {
                this.mSubtitleView.setTextAppearance(this.mSubtitleStyleRes);
            }
        }
        this.mTitleView.setText(this.mTitle);
        this.mSubtitleView.setText(this.mSubtitle);
        if (TextUtils.isEmpty(this.mTitle)) {
            hasTitle = false;
        } else {
            hasTitle = true;
        }
        if (TextUtils.isEmpty(this.mSubtitle)) {
            hasSubtitle = false;
        } else {
            hasSubtitle = true;
        }
        TextView textView = this.mSubtitleView;
        if (hasSubtitle) {
            i = 0;
        } else {
            i = 8;
        }
        textView.setVisibility(i);
        LinearLayout linearLayout = this.mTitleLayout;
        if (hasTitle || hasSubtitle) {
            i2 = 0;
        }
        linearLayout.setVisibility(i2);
        if (this.mTitleLayout.getParent() == null) {
            addView(this.mTitleLayout);
        }
    }

    public void initForMode(final ActionMode mode) {
        if (this.mClose == null) {
            this.mClose = LayoutInflater.from(this.mContext).inflate(this.mCloseItemLayout, (ViewGroup) this, false);
            addView(this.mClose);
        } else if (this.mClose.getParent() == null) {
            addView(this.mClose);
        }
        View closeButton = this.mClose.findViewById(R.id.action_mode_close_button);
        closeButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                mode.finish();
            }
        });
        closeButton.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View v) {
                Toast cheatSheet;
                int gravity;
                int[] screenPos = new int[2];
                Rect contentFrame = new Rect();
                ActionBarContextView.this.getLocationOnScreen(screenPos);
                ActionBarContextView.this.getWindowVisibleContentFrame(contentFrame);
                Context context = ActionBarContextView.this.getContext();
                int statusBarHeight = context.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
                int width = ActionBarContextView.this.getWidth();
                int height = ActionBarContextView.this.getHeight();
                int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
                int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
                float density = context.getResources().getDisplayMetrics().density;
                if (AbsActionBarView.mIsThemeDeviceDefaultFamily) {
                    cheatSheet = Toast.twMakeText(context, v.getContentDescription(), 0);
                } else {
                    cheatSheet = Toast.makeText(context, v.getContentDescription(), 0);
                }
                if (v.getLayoutDirection() == 1) {
                    gravity = 53;
                } else {
                    gravity = 51;
                }
                cheatSheet.setGravity(gravity, screenPos[0], height + (screenPos[1] - statusBarHeight));
                MultiWindowStyle mwStyle = ActionBarContextView.this.mContext.getAppMultiWindowStyle();
                if (mwStyle != null && mwStyle.isCascade()) {
                    int tempToastHeight = (int) (4.0f * density);
                    cheatSheet.setGravity(gravity, screenPos[0], screenPos[1] + height);
                }
                cheatSheet.show();
                return true;
            }
        });
        MenuBuilder menu = (MenuBuilder) mode.getMenu();
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.dismissPopupMenus();
        }
        this.mActionMenuPresenter = new ActionMenuPresenter(this.mContext);
        this.mActionMenuPresenter.setReserveOverflow(true);
        LayoutParams layoutParams = new LayoutParams(-2, -1);
        if (this.mSplitActionBar) {
            this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
            this.mActionMenuPresenter.setItemLimit(Integer.MAX_VALUE);
            layoutParams.width = -1;
            layoutParams.height = this.mContentHeight;
            menu.addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
            this.mMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
            this.mMenuView.setBackgroundDrawable(this.mSplitBackground);
            this.mSplitView.addView(this.mMenuView, layoutParams);
            return;
        }
        menu.addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
        this.mMenuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
        this.mMenuView.setBackground(null);
        addView(this.mMenuView, layoutParams);
    }

    public void closeMode() {
        if (this.mClose == null) {
            killMode();
        }
    }

    public void killMode() {
        removeAllViews();
        if (this.mSplitView != null) {
            this.mSplitView.removeView(this.mMenuView);
        }
        this.mCustomView = null;
        this.mMenuView = null;
    }

    public boolean showOverflowMenu() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.showOverflowMenu();
        }
        return false;
    }

    public boolean hideOverflowMenu() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.hideOverflowMenu();
        }
        return false;
    }

    public boolean isOverflowMenuShowing() {
        if (this.mActionMenuPresenter != null) {
            return this.mActionMenuPresenter.isOverflowMenuShowing();
        }
        return false;
    }

    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(-1, -2);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) != 1073741824) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used " + "with android:layout_width=\"match_parent\" (or fill_parent)");
        } else if (MeasureSpec.getMode(heightMeasureSpec) == 0) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used " + "with android:layout_height=\"wrap_content\"");
        } else {
            LayoutParams lp;
            int contentWidth = MeasureSpec.getSize(widthMeasureSpec);
            int maxHeight = this.mContentHeight > 0 ? this.mContentHeight : MeasureSpec.getSize(heightMeasureSpec);
            int verticalPadding = getPaddingTop() + getPaddingBottom();
            int availableWidth = (contentWidth - getPaddingLeft()) - getPaddingRight();
            int height = maxHeight - verticalPadding;
            int childSpecHeight = MeasureSpec.makeMeasureSpec(height, Integer.MIN_VALUE);
            if (!(this.mClose == null || this.mClose.getVisibility() == 8)) {
                MarginLayoutParams lp2 = (MarginLayoutParams) this.mClose.getLayoutParams();
                availableWidth = measureChildView(this.mClose, availableWidth, childSpecHeight, 0) - (lp2.leftMargin + lp2.rightMargin);
            }
            if (this.mMenuView != null && this.mMenuView.getParent() == this) {
                availableWidth = measureChildView(this.mMenuView, availableWidth, childSpecHeight, 0);
            }
            if (this.mTitleLayout != null && this.mCustomView == null) {
                if (mIsThemeDeviceDefaultFamily && this.mTitleView != null) {
                    TypedArray appearance = getContext().obtainStyledAttributes(this.mTitleStyleRes, R.styleable.TextAppearance);
                    TypedValue value = appearance.peekValue(0);
                    appearance.recycle();
                    float textSize = TypedValue.complexToFloat(value.data);
                    if (TextUtils.isEmpty(this.mSubtitle)) {
                        float fontScale = getContext().getResources().getConfiguration().fontScale;
                        if (fontScale > this.mMaxFontScale) {
                            fontScale = this.mMaxFontScale;
                        }
                        this.mTitleView.setTextSize(1, textSize * fontScale);
                    } else {
                        this.mTitleView.setTextSize(1, textSize);
                    }
                }
                if ((mIsThemeDeviceDefaultFamily && this.mClose == null) || (this.mClose != null && this.mClose.getVisibility() == 8)) {
                    if (this.mTitleView != null && this.mTitleView.getVisibility() == 0) {
                        lp = (LinearLayout.LayoutParams) this.mTitleView.getLayoutParams();
                        if (getLayoutDirection() == 0) {
                            lp.leftMargin = (int) getContext().getResources().getDimension(R.dimen.tw_toolbar_content_inset_start);
                        } else {
                            lp.rightMargin = (int) getContext().getResources().getDimension(R.dimen.tw_toolbar_content_inset_start);
                        }
                        this.mTitleView.setLayoutParams(lp);
                    }
                    if (this.mSubtitleView != null && this.mSubtitleView.getVisibility() == 0) {
                        lp = (LinearLayout.LayoutParams) this.mSubtitleView.getLayoutParams();
                        if (getLayoutDirection() == 0) {
                            lp.leftMargin = (int) getContext().getResources().getDimension(R.dimen.tw_toolbar_content_inset_start);
                        } else {
                            lp.rightMargin = (int) getContext().getResources().getDimension(R.dimen.tw_toolbar_content_inset_start);
                        }
                        this.mSubtitleView.setLayoutParams(lp);
                    }
                }
                if (this.mTitleOptional) {
                    this.mTitleLayout.measure(MeasureSpec.makeSafeMeasureSpec(contentWidth, 0), childSpecHeight);
                    int titleWidth = this.mTitleLayout.getMeasuredWidth();
                    boolean titleFits = titleWidth <= availableWidth;
                    if (titleFits) {
                        availableWidth -= titleWidth;
                    }
                    this.mTitleLayout.setVisibility(titleFits ? 0 : 8);
                } else {
                    availableWidth = measureChildView(this.mTitleLayout, availableWidth, childSpecHeight, 0);
                }
            }
            if (this.mCustomView != null) {
                int customWidth;
                int customHeight;
                lp = this.mCustomView.getLayoutParams();
                int customWidthMode = lp.width != -2 ? 1073741824 : Integer.MIN_VALUE;
                if (lp.width >= 0) {
                    customWidth = Math.min(lp.width, availableWidth);
                } else {
                    customWidth = availableWidth;
                }
                int customHeightMode = lp.height != -2 ? 1073741824 : Integer.MIN_VALUE;
                if (lp.height >= 0) {
                    customHeight = Math.min(lp.height, height);
                } else {
                    customHeight = height;
                }
                this.mCustomView.measure(MeasureSpec.makeMeasureSpec(customWidth, customWidthMode), MeasureSpec.makeMeasureSpec(customHeight, customHeightMode));
            }
            if (this.mContentHeight <= 0) {
                int measuredHeight = 0;
                int count = getChildCount();
                for (int i = 0; i < count; i++) {
                    int paddedViewHeight = getChildAt(i).getMeasuredHeight() + verticalPadding;
                    if (paddedViewHeight > measuredHeight) {
                        measuredHeight = paddedViewHeight;
                    }
                }
                setMeasuredDimension(contentWidth, measuredHeight);
            } else {
                setMeasuredDimension(contentWidth, maxHeight);
            }
            if (this.mChangedTheme) {
                twUpdateBackgroundBounds();
            }
        }
    }

    protected void twUpdateBackgroundBounds() {
        Drawable background = getBackground();
        if (background != null) {
            if (this.mChangedTheme && (background instanceof BitmapDrawable) && background.getIntrinsicWidth() < getWidth()) {
                background.setBounds(0, 0, getRight(), (int) ((((float) getWidth()) / ((float) background.getIntrinsicWidth())) * ((float) background.getIntrinsicHeight())));
                return;
            }
            super.twUpdateBackgroundBounds();
        }
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        boolean isLayoutRtl = isLayoutRtl();
        int x = isLayoutRtl ? (r - l) - getPaddingRight() : getPaddingLeft();
        int y = getPaddingTop();
        int contentHeight = ((b - t) - getPaddingTop()) - getPaddingBottom();
        if (!(this.mClose == null || this.mClose.getVisibility() == 8)) {
            MarginLayoutParams lp = (MarginLayoutParams) this.mClose.getLayoutParams();
            int startMargin = isLayoutRtl ? lp.rightMargin : lp.leftMargin;
            int endMargin = isLayoutRtl ? lp.leftMargin : lp.rightMargin;
            x = AbsActionBarView.next(x, startMargin, isLayoutRtl);
            x = AbsActionBarView.next(x + positionChild(this.mClose, x, y, contentHeight, isLayoutRtl), endMargin, isLayoutRtl);
        }
        if (!(this.mTitleLayout == null || this.mCustomView != null || this.mTitleLayout.getVisibility() == 8)) {
            x += positionChild(this.mTitleLayout, x, y, contentHeight, isLayoutRtl);
        }
        if (this.mCustomView != null) {
            x += positionChild(this.mCustomView, x, y, contentHeight, isLayoutRtl);
        }
        x = isLayoutRtl ? getPaddingLeft() : (r - l) - getPaddingRight();
        if (this.mMenuView != null) {
            x += positionChild(this.mMenuView, x, y, contentHeight, !isLayoutRtl);
        }
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        if (event.getEventType() == 32) {
            event.setSource(this);
            event.setClassName(getClass().getName());
            event.setPackageName(getContext().getPackageName());
            event.setContentDescription(this.mTitle);
            return;
        }
        super.onInitializeAccessibilityEventInternal(event);
    }

    public void setTitleOptional(boolean titleOptional) {
        if (titleOptional != this.mTitleOptional) {
            requestLayout();
        }
        this.mTitleOptional = titleOptional;
    }

    public boolean isTitleOptional() {
        return this.mTitleOptional;
    }
}
