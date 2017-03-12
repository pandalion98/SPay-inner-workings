package com.android.internal.view.menu;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.provider.Settings$System;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ActionMenuView.ActionMenuChildView;
import android.widget.ListPopupWindow;
import android.widget.ListPopupWindow.ForwardingListener;
import android.widget.TextView;
import android.widget.Toast;
import com.android.internal.R;
import com.android.internal.view.menu.MenuBuilder.ItemInvoker;
import com.android.internal.view.menu.MenuView.ItemView;
import com.samsung.android.multiwindow.MultiWindowStyle;

public class ActionMenuItemView extends TextView implements ItemView, OnClickListener, OnLongClickListener, ActionMenuChildView {
    private static final int MAX_ICON_SIZE = 32;
    private static final String TAG = "ActionMenuItemView";
    private static final int TEMP_TOAST_HEIGHT = 40;
    private Activity mActivity;
    private boolean mAllowTextWithIcon;
    private Drawable mArchivedBackground;
    private float mDefaultTextSize;
    private boolean mExpandedFormat;
    private ForwardingListener mForwardingListener;
    private Handler mHandler;
    private boolean mHasSPenFeature;
    private Drawable mIcon;
    private boolean mIsMultiWindow;
    private boolean mIsThemeDeviceDefaultFamily;
    private MenuItemImpl mItemData;
    private ItemInvoker mItemInvoker;
    private float mMaxFontScale;
    private int mMaxIconSize;
    private int mMinWidth;
    private PopupCallback mPopupCallback;
    private int mSavedPaddingLeft;
    private CharSequence mTitle;

    public static abstract class PopupCallback {
        public abstract ListPopupWindow getPopup();
    }

    private class ActionMenuItemForwardingListener extends ForwardingListener {
        public ActionMenuItemForwardingListener() {
            super(ActionMenuItemView.this);
        }

        public ListPopupWindow getPopup() {
            if (ActionMenuItemView.this.mPopupCallback != null) {
                return ActionMenuItemView.this.mPopupCallback.getPopup();
            }
            return null;
        }

        protected boolean onForwardingStarted() {
            if (ActionMenuItemView.this.mItemInvoker == null || !ActionMenuItemView.this.mItemInvoker.invokeItem(ActionMenuItemView.this.mItemData)) {
                return false;
            }
            ListPopupWindow popup = getPopup();
            if (popup == null || !popup.isShowing()) {
                return false;
            }
            return true;
        }

        protected boolean onForwardingStopped() {
            ListPopupWindow popup = getPopup();
            if (popup == null) {
                return false;
            }
            popup.dismiss();
            return true;
        }
    }

    public ActionMenuItemView(Context context) {
        this(context, null);
    }

    public ActionMenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionMenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ActionMenuItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        boolean z = true;
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mHasSPenFeature = false;
        this.mHandler = new Handler();
        this.mDefaultTextSize = 0.0f;
        this.mMaxFontScale = 1.2f;
        this.mIsMultiWindow = false;
        Resources res = context.getResources();
        this.mAllowTextWithIcon = res.getBoolean(R.bool.config_allowActionMenuItemTextWithIcon);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionMenuItemView, defStyleAttr, defStyleRes);
        this.mMinWidth = a.getDimensionPixelSize(0, 0);
        a.recycle();
        this.mMaxIconSize = (int) ((LppLocationManager.PASSIVE_LOC_ACC_VALIDITY * res.getDisplayMetrics().density) + 0.5f);
        setOnClickListener(this);
        setOnLongClickListener(this);
        this.mSavedPaddingLeft = -1;
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
        if (outValue.data == 0) {
            z = false;
        }
        this.mIsThemeDeviceDefaultFamily = z;
        if (this.mIsThemeDeviceDefaultFamily) {
            a = context.getTheme().obtainStyledAttributes(null, R.styleable.Theme, 0, 0);
            int actionMeneTextAppearnceId = a.getResourceId(187, 0);
            a.recycle();
            a = getContext().obtainStyledAttributes(actionMeneTextAppearnceId, R.styleable.TextAppearance);
            outValue = a.peekValue(0);
            a.recycle();
            if (outValue != null) {
                this.mDefaultTextSize = TypedValue.complexToFloat(outValue.data);
            }
        }
        this.mHasSPenFeature = context.getPackageManager().hasSystemFeature("com.sec.feature.spen_usp");
        this.mArchivedBackground = getBackground();
        if (this.mIsThemeDeviceDefaultFamily && (context instanceof Activity)) {
            this.mActivity = (Activity) context;
        } else {
            this.mActivity = null;
        }
    }

    private void changeButtonBackground() {
        boolean show = true;
        if (this.mIsThemeDeviceDefaultFamily) {
            if (Settings$System.getInt(getContext().getContentResolver(), Settings$System.SHOW_BUTTON_BACKGROUND, 0) != 1) {
                show = false;
            }
            if (show && hasText()) {
                setBackgroundResource(R.drawable.tw_action_item_with_button_background_light);
            } else {
                setBackground(this.mArchivedBackground);
            }
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        TypedArray a = getContext().obtainStyledAttributes(null, R.styleable.View, R.attr.actionButtonStyle, 0);
        setMinHeight(a.getDimensionPixelSize(37, -1));
        a.recycle();
        this.mAllowTextWithIcon = getContext().getResources().getBoolean(R.bool.config_allowActionMenuItemTextWithIcon);
        updateTextButtonVisibility();
    }

    public void setPadding(int l, int t, int r, int b) {
        this.mSavedPaddingLeft = l;
        super.setPadding(l, t, r, b);
    }

    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public void initialize(MenuItemImpl itemData, int menuType) {
        this.mItemData = itemData;
        setIcon(itemData.getIcon());
        setTitle(itemData.getTitleForItemView(this));
        setId(itemData.getItemId());
        setVisibility(itemData.isVisible() ? 0 : 8);
        setEnabled(itemData.isEnabled());
        if (itemData.hasSubMenu() && !this.mIsThemeDeviceDefaultFamily && this.mForwardingListener == null) {
            this.mForwardingListener = new ActionMenuItemForwardingListener();
        }
    }

    public boolean onTouchEvent(MotionEvent e) {
        if (this.mItemData.hasSubMenu() && this.mForwardingListener != null && this.mForwardingListener.onTouch(this, e)) {
            return true;
        }
        return super.onTouchEvent(e);
    }

    public void onClick(View v) {
        if (this.mIsThemeDeviceDefaultFamily) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    if ((ActionMenuItemView.this.mActivity == null || !ActionMenuItemView.this.mActivity.isFinishing()) && ActionMenuItemView.this.mItemInvoker != null) {
                        ActionMenuItemView.this.mItemInvoker.invokeItem(ActionMenuItemView.this.mItemData);
                    }
                }
            }, 50);
        } else if (this.mItemInvoker != null) {
            this.mItemInvoker.invokeItem(this.mItemData);
        }
    }

    public void setItemInvoker(ItemInvoker invoker) {
        this.mItemInvoker = invoker;
    }

    public void setPopupCallback(PopupCallback popupCallback) {
        this.mPopupCallback = popupCallback;
    }

    public boolean prefersCondensedTitle() {
        return true;
    }

    public void setCheckable(boolean checkable) {
    }

    public void setChecked(boolean checked) {
    }

    public void setExpandedFormat(boolean expandedFormat) {
        if (this.mExpandedFormat != expandedFormat) {
            this.mExpandedFormat = expandedFormat;
            if (this.mItemData != null) {
                this.mItemData.actionFormatChanged();
            }
        }
    }

    private void updateTextButtonVisibility() {
        boolean visible;
        int i;
        if (TextUtils.isEmpty(this.mTitle)) {
            visible = false;
        } else {
            visible = true;
        }
        if (this.mIcon == null || (this.mItemData.showsTextAsAction() && ((this.mAllowTextWithIcon || this.mExpandedFormat) && !this.mIsMultiWindow))) {
            i = 1;
        } else {
            i = 0;
        }
        visible &= i;
        if (this.mIsThemeDeviceDefaultFamily && this.mDefaultTextSize > 0.0f) {
            float fontScale = getContext().getResources().getConfiguration().fontScale;
            if (fontScale > this.mMaxFontScale) {
                fontScale = this.mMaxFontScale;
            }
            setTextSize(1, this.mDefaultTextSize * fontScale);
        }
        setText(visible ? this.mTitle : null);
        if (getHoverUIFeatureLevel() < 2 && !this.mIsThemeDeviceDefaultFamily) {
            return;
        }
        if (visible) {
            setHoverPopupType(0);
        } else {
            setHoverPopupType(1);
        }
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
        if (icon != null) {
            float scale;
            int width = icon.getIntrinsicWidth();
            int height = icon.getIntrinsicHeight();
            if (width > this.mMaxIconSize) {
                scale = ((float) this.mMaxIconSize) / ((float) width);
                width = this.mMaxIconSize;
                height = (int) (((float) height) * scale);
            }
            if (height > this.mMaxIconSize) {
                scale = ((float) this.mMaxIconSize) / ((float) height);
                height = this.mMaxIconSize;
                width = (int) (((float) width) * scale);
            }
            icon.setBounds(0, 0, width, height);
        }
        setCompoundDrawables(icon, null, null, null);
        updateTextButtonVisibility();
    }

    public boolean hasText() {
        return !TextUtils.isEmpty(getText());
    }

    public void setShortcut(boolean showShortcut, char shortcutKey) {
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title;
        setContentDescription(this.mTitle);
        updateTextButtonVisibility();
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName("android.widget.Button");
    }

    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        onPopulateAccessibilityEvent(event);
        return true;
    }

    public void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        CharSequence cdesc = getContentDescription();
        if (!TextUtils.isEmpty(cdesc)) {
            event.getText().add(cdesc);
        }
    }

    public boolean dispatchHoverEvent(MotionEvent event) {
        if (getAirButton() == null) {
            return onHoverEvent(event);
        }
        return super.dispatchHoverEvent(event);
    }

    public void setHovered(boolean hovered) {
        if (!hasText()) {
            super.setHovered(hovered);
        }
    }

    public boolean showsIcon() {
        return true;
    }

    public boolean needsDividerBefore() {
        return hasText() && this.mItemData.getIcon() == null;
    }

    public boolean needsDividerAfter() {
        return hasText();
    }

    public boolean onLongClick(View v) {
        if (hasText()) {
            return false;
        }
        int horizontalGravity;
        Toast cheatSheet;
        int[] screenPos = new int[2];
        Rect contentFrame = new Rect();
        getLocationOnScreen(screenPos);
        getWindowVisibleContentFrame(contentFrame);
        Context context = getContext();
        int statusBarHeight = context.getResources().getDimensionPixelSize(R.dimen.status_bar_height);
        int width = getWidth();
        int height = getHeight();
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        float density = context.getResources().getDisplayMetrics().density;
        int referenceX = screenPos[0] + (width / 2);
        if (v.getLayoutDirection() == 0) {
            referenceX = screenWidth - referenceX;
            horizontalGravity = 5;
        } else {
            horizontalGravity = 3;
        }
        referenceX -= (int) (5.0f * density);
        if (this.mIsThemeDeviceDefaultFamily) {
            cheatSheet = Toast.twMakeText(context, v.getContentDescription(), 0);
        } else {
            cheatSheet = Toast.makeText(context, v.getContentDescription(), 0);
        }
        if (screenPos[1] + height != contentFrame.bottom || height == contentFrame.height()) {
            cheatSheet.setGravity(horizontalGravity | 48, referenceX, height + (screenPos[1] - statusBarHeight));
        } else {
            cheatSheet.setGravity(horizontalGravity | 80, referenceX, height + (screenHeight - contentFrame.bottom));
        }
        MultiWindowStyle mwStyle = this.mContext.getAppMultiWindowStyle();
        if (mwStyle != null && mwStyle.isCascade()) {
            int gravity;
            int xOffset;
            int yOffset;
            if ((screenPos[1] + height) + ((int) (40.0f * density)) < screenHeight) {
                gravity = 53;
                xOffset = (screenWidth - screenPos[0]) - (width / 2);
                yOffset = screenPos[1] + height;
            } else {
                gravity = 85;
                xOffset = (screenWidth - screenPos[0]) - (width / 2);
                yOffset = height;
            }
            cheatSheet.setGravity(gravity, xOffset, yOffset);
        }
        cheatSheet.show();
        return true;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean oldIsMultiWindow = this.mIsMultiWindow;
        MultiWindowStyle multindowStyle = null;
        if (this.mContext instanceof Activity) {
            multindowStyle = ((Activity) this.mContext).getMultiWindowStyle();
        } else if (this.mContext instanceof ContextWrapper) {
            multindowStyle = this.mContext.getAppMultiWindowStyle();
        }
        boolean z = multindowStyle != null && multindowStyle.getType() == 1;
        this.mIsMultiWindow = z;
        if (this.mIsMultiWindow != oldIsMultiWindow) {
            updateTextButtonVisibility();
        }
        boolean textVisible = hasText();
        if (textVisible && this.mSavedPaddingLeft >= 0) {
            super.setPadding(this.mSavedPaddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int oldMeasuredWidth = getMeasuredWidth();
        int targetWidth = widthMode == Integer.MIN_VALUE ? Math.min(widthSize, this.mMinWidth) : this.mMinWidth;
        if (widthMode != 1073741824 && this.mMinWidth > 0 && oldMeasuredWidth < targetWidth) {
            super.onMeasure(MeasureSpec.makeMeasureSpec(targetWidth, 1073741824), heightMeasureSpec);
        }
        if (!(textVisible || this.mIcon == null)) {
            super.setPadding((getMeasuredWidth() - this.mIcon.getBounds().width()) / 2, getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }
        changeButtonBackground();
    }
}
