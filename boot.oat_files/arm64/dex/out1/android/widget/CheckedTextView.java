package android.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.SystemProperties;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewHierarchyEncoder;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.internal.R;

public class CheckedTextView extends TextView implements Checkable {
    private static final int[] CHECKED_STATE_SET = new int[]{R.attr.state_checked};
    private int mBasePadding;
    private Drawable mCheckMarkDrawable;
    private int mCheckMarkGravity;
    private int mCheckMarkResource;
    private ColorStateList mCheckMarkTintList;
    private Mode mCheckMarkTintMode;
    private int mCheckMarkWidth;
    private boolean mChecked;
    private boolean mHasCheckMarkTint;
    private boolean mHasCheckMarkTintMode;
    private boolean mIsDeviceDefault;
    private boolean mIsDeviceDefaultDark;
    private boolean mNeedRequestlayout;

    public CheckedTextView(Context context) {
        this(context, null);
    }

    public CheckedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.checkedTextViewStyle);
    }

    public CheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CheckedTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mCheckMarkTintList = null;
        this.mCheckMarkTintMode = null;
        this.mHasCheckMarkTint = false;
        this.mHasCheckMarkTintMode = false;
        this.mIsDeviceDefault = false;
        this.mIsDeviceDefaultDark = true;
        this.mCheckMarkGravity = Gravity.END;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CheckedTextView, defStyleAttr, defStyleRes);
        Drawable d = a.getDrawable(1);
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
        if (outValue.data != 0) {
            this.mIsDeviceDefault = true;
        }
        TypedValue outValueDarkTheme = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.parentIsDeviceDefaultDark, outValueDarkTheme, true);
        if (outValueDarkTheme.data == 0) {
            this.mIsDeviceDefaultDark = false;
        }
        String scafe = SystemProperties.get("ro.build.scafe");
        int mDrawablePadding = getContext().getResources().getDimensionPixelSize(R.dimen.tw_checkedtextview_padding);
        if (this.mIsDeviceDefault && !this.mIsDeviceDefaultDark && (scafe.equals("capuccino") || scafe.equals("latte") || View.TW_SCAFE_2016A)) {
            this.mCheckMarkGravity = a.getInt(4, Gravity.START);
            if (d != null) {
                setCompoundDrawablesRelativeWithIntrinsicBounds(d, null, null, null);
            }
            setCompoundDrawablePadding(mDrawablePadding);
        } else {
            if (d != null) {
                setCheckMarkDrawable(d);
            }
            this.mCheckMarkGravity = a.getInt(4, Gravity.END);
        }
        if (a.hasValue(3)) {
            this.mCheckMarkTintMode = Drawable.parseTintMode(a.getInt(3, -1), this.mCheckMarkTintMode);
            this.mHasCheckMarkTintMode = true;
        }
        if (a.hasValue(2)) {
            this.mCheckMarkTintList = a.getColorStateList(2);
            this.mHasCheckMarkTint = true;
        }
        setChecked(a.getBoolean(0, false));
        a.recycle();
        applyCheckMarkTint();
    }

    public void toggle() {
        setChecked(!this.mChecked);
    }

    @ExportedProperty
    public boolean isChecked() {
        return this.mChecked;
    }

    public void setChecked(boolean checked) {
        if (this.mChecked != checked) {
            this.mChecked = checked;
            refreshDrawableState();
            notifyViewAccessibilityStateChangedIfNeeded(0);
        }
    }

    public void setCheckMarkDrawable(int resId) {
        if (resId == 0 || resId != this.mCheckMarkResource) {
            this.mCheckMarkResource = resId;
            Drawable d = null;
            if (this.mCheckMarkResource != 0) {
                d = getContext().getDrawable(this.mCheckMarkResource);
            }
            setCheckMarkDrawable(d);
        }
    }

    public void setCheckMarkDrawable(Drawable d) {
        boolean z = true;
        if (this.mCheckMarkDrawable != null) {
            this.mCheckMarkDrawable.setCallback(null);
            unscheduleDrawable(this.mCheckMarkDrawable);
        }
        this.mNeedRequestlayout = d != this.mCheckMarkDrawable;
        if (d != null) {
            d.setCallback(this);
            if (getVisibility() != 0) {
                z = false;
            }
            d.setVisible(z, false);
            d.setState(CHECKED_STATE_SET);
            setMinHeight(d.getIntrinsicHeight());
            this.mCheckMarkWidth = d.getIntrinsicWidth();
            d.setState(getDrawableState());
            applyCheckMarkTint();
        } else {
            this.mCheckMarkWidth = 0;
        }
        this.mCheckMarkDrawable = d;
        resolvePadding();
    }

    public void setCheckMarkTintList(ColorStateList tint) {
        this.mCheckMarkTintList = tint;
        this.mHasCheckMarkTint = true;
        applyCheckMarkTint();
    }

    public ColorStateList getCheckMarkTintList() {
        return this.mCheckMarkTintList;
    }

    public void setCheckMarkTintMode(Mode tintMode) {
        this.mCheckMarkTintMode = tintMode;
        this.mHasCheckMarkTintMode = true;
        applyCheckMarkTint();
    }

    public Mode getCheckMarkTintMode() {
        return this.mCheckMarkTintMode;
    }

    private void applyCheckMarkTint() {
        if (this.mCheckMarkDrawable == null) {
            return;
        }
        if (this.mHasCheckMarkTint || this.mHasCheckMarkTintMode) {
            this.mCheckMarkDrawable = this.mCheckMarkDrawable.mutate();
            if (this.mHasCheckMarkTint) {
                this.mCheckMarkDrawable.setTintList(this.mCheckMarkTintList);
            }
            if (this.mHasCheckMarkTintMode) {
                this.mCheckMarkDrawable.setTintMode(this.mCheckMarkTintMode);
            }
            if (this.mCheckMarkDrawable.isStateful()) {
                this.mCheckMarkDrawable.setState(getDrawableState());
            }
        }
    }

    @RemotableViewMethod
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (this.mCheckMarkDrawable != null) {
            boolean z;
            Drawable drawable = this.mCheckMarkDrawable;
            if (visibility == 0) {
                z = true;
            } else {
                z = false;
            }
            drawable.setVisible(z, false);
        }
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (this.mCheckMarkDrawable != null) {
            this.mCheckMarkDrawable.jumpToCurrentState();
        }
    }

    protected boolean verifyDrawable(Drawable who) {
        return who == this.mCheckMarkDrawable || super.verifyDrawable(who);
    }

    public Drawable getCheckMarkDrawable() {
        return this.mCheckMarkDrawable;
    }

    protected void internalSetPadding(int left, int top, int right, int bottom) {
        super.internalSetPadding(left, top, right, bottom);
        setBasePadding(isCheckMarkAtStart());
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        updatePadding();
    }

    private void updatePadding() {
        int i = 1;
        resetPaddingToInitialValues();
        int newPadding = this.mCheckMarkDrawable != null ? this.mCheckMarkWidth + this.mBasePadding : this.mBasePadding;
        boolean z;
        if (isCheckMarkAtStart()) {
            z = this.mNeedRequestlayout;
            if (this.mPaddingLeft == newPadding) {
                i = 0;
            }
            this.mNeedRequestlayout = i | z;
            this.mPaddingLeft = newPadding;
        } else {
            z = this.mNeedRequestlayout;
            if (this.mPaddingRight == newPadding) {
                i = 0;
            }
            this.mNeedRequestlayout = i | z;
            this.mPaddingRight = newPadding;
        }
        if (this.mNeedRequestlayout) {
            requestLayout();
            this.mNeedRequestlayout = false;
        }
    }

    private void setBasePadding(boolean checkmarkAtStart) {
        if (checkmarkAtStart) {
            this.mBasePadding = this.mPaddingLeft;
        } else {
            this.mBasePadding = this.mPaddingRight;
        }
    }

    private boolean isCheckMarkAtStart() {
        return (Gravity.getAbsoluteGravity(this.mCheckMarkGravity, getLayoutDirection()) & 7) == 3;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable checkMarkDrawable = this.mCheckMarkDrawable;
        if (checkMarkDrawable != null) {
            int left;
            int right;
            int verticalGravity = getGravity() & 112;
            int height = checkMarkDrawable.getIntrinsicHeight();
            int y = 0;
            switch (verticalGravity) {
                case 16:
                    y = (getHeight() - height) / 2;
                    break;
                case 80:
                    y = getHeight() - height;
                    break;
            }
            boolean checkMarkAtStart = isCheckMarkAtStart();
            int width = getWidth();
            int top = y;
            int bottom = top + height;
            if (checkMarkAtStart) {
                left = this.mBasePadding;
                right = left + this.mCheckMarkWidth;
            } else {
                right = width - this.mBasePadding;
                left = right - this.mCheckMarkWidth;
            }
            if (isLayoutRtl()) {
                checkMarkDrawable.setBounds(this.mScrollX + left, top, this.mScrollX + right, bottom);
            } else {
                checkMarkDrawable.setBounds(left, top, right, bottom);
            }
            checkMarkDrawable.draw(canvas);
            Drawable background = getBackground();
            if (background != null) {
                background.setHotspotBounds(this.mScrollX + left, top, this.mScrollX + right, bottom);
            }
        }
    }

    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            View.mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (this.mCheckMarkDrawable != null) {
            this.mCheckMarkDrawable.setState(getDrawableState());
            invalidate();
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (this.mCheckMarkDrawable != null) {
            this.mCheckMarkDrawable.setHotspot(x, y);
        }
    }

    public CharSequence getAccessibilityClassName() {
        return CheckedTextView.class.getName();
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setChecked(this.mChecked);
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        info.setCheckable(true);
        info.setChecked(this.mChecked);
    }

    protected void encodeProperties(ViewHierarchyEncoder stream) {
        super.encodeProperties(stream);
        stream.addProperty("text:checked", isChecked());
    }
}
