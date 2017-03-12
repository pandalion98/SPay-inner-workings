package com.android.internal.view.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.GeneralUtil;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.view.menu.MenuView.ItemView;

public class ListMenuItemView extends LinearLayout implements ItemView {
    private static final String TAG = "ListMenuItemView";
    private Drawable mBackground;
    private CheckBox mCheckBox;
    private boolean mForceShowIcon;
    private ImageView mIconView;
    private LayoutInflater mInflater;
    private boolean mIsCheckedTextColorMode;
    private boolean mIsDeviceDefaultLight;
    private MenuItemImpl mItemData;
    private int mMenuType;
    private boolean mPreserveIconSpacing;
    private RadioButton mRadioButton;
    private TextView mShortcutView;
    private int mTextAppearance;
    private Context mTextAppearanceContext;
    private TextView mTitleView;

    public ListMenuItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuView, defStyleAttr, defStyleRes);
        TypedValue outValue = new TypedValue();
        TypedValue colorValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, false);
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefaultDark, colorValue, true);
        if (outValue.data != 0 && colorValue.data == 0) {
            this.mIsDeviceDefaultLight = true;
        }
        this.mBackground = a.getDrawable(5);
        this.mTextAppearance = a.getResourceId(1, -1);
        this.mPreserveIconSpacing = a.getBoolean(7, false);
        this.mTextAppearanceContext = context;
        a.recycle();
    }

    public ListMenuItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ListMenuItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        setBackgroundDrawable(this.mBackground);
        this.mTitleView = (TextView) findViewById(R.id.title);
        if (this.mTextAppearance != -1) {
            this.mTitleView.setTextAppearance(this.mTextAppearanceContext, this.mTextAppearance);
        }
        this.mShortcutView = (TextView) findViewById(R.id.shortcut);
    }

    public void initialize(MenuItemImpl itemData, int menuType) {
        this.mItemData = itemData;
        this.mMenuType = menuType;
        int ts = 0;
        boolean z = GeneralUtil.isTablet() && this.mIsDeviceDefaultLight && this.mItemData.isExclusiveCheckable() && (this.mTitleView instanceof CheckedTextView);
        this.mIsCheckedTextColorMode = z;
        setVisibility(itemData.isVisible() ? 0 : 8);
        setTitle(itemData.getTitleForItemView(this));
        setCheckable(itemData.isCheckable());
        setShortcut(itemData.shouldShowShortcut(), itemData.getShortcut());
        setIcon(itemData.getIcon());
        setEnabled(itemData.isEnabled());
        this.mTitleView = (TextView) findViewById(R.id.title);
        this.mTitleView.setEnabled(itemData.isEnabled());
        if (this.mTextAppearance != -1) {
            TypedArray appearance = this.mTextAppearanceContext.obtainStyledAttributes(this.mTextAppearance, R.styleable.TextAppearance);
            ts = appearance.getDimensionPixelSize(0, 0);
            appearance.recycle();
        }
        if (ts != 0) {
            this.mTitleView.getPaint().setTextSize((float) ts);
        }
    }

    public void setForceShowIcon(boolean forceShow) {
        this.mForceShowIcon = forceShow;
        this.mPreserveIconSpacing = forceShow;
    }

    public void setTitle(CharSequence title) {
        if (title != null) {
            this.mTitleView.setText(title);
            if (this.mTitleView.getVisibility() != 0) {
                this.mTitleView.setVisibility(0);
            }
        } else if (this.mTitleView.getVisibility() != 8) {
            this.mTitleView.setVisibility(8);
        }
    }

    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public void setCheckable(boolean checkable) {
        int newVisibility = 0;
        if (checkable || this.mRadioButton != null || this.mCheckBox != null) {
            CompoundButton compoundButton;
            CompoundButton otherCompoundButton;
            if (this.mItemData.isExclusiveCheckable()) {
                if (this.mRadioButton == null) {
                    insertRadioButton();
                }
                compoundButton = this.mRadioButton;
                otherCompoundButton = this.mCheckBox;
            } else {
                if (this.mCheckBox == null) {
                    insertCheckBox();
                }
                compoundButton = this.mCheckBox;
                otherCompoundButton = this.mRadioButton;
            }
            if (checkable) {
                compoundButton.setChecked(this.mItemData.isChecked());
                if (!checkable) {
                    newVisibility = 8;
                }
                if (compoundButton.getVisibility() != newVisibility) {
                    compoundButton.setVisibility(newVisibility);
                }
                if (!(otherCompoundButton == null || otherCompoundButton.getVisibility() == 8)) {
                    otherCompoundButton.setVisibility(8);
                }
                if (this.mIsCheckedTextColorMode && this.mItemData.isCheckable()) {
                    ((CheckedTextView) this.mTitleView).setChecked(this.mItemData.isChecked());
                    compoundButton.setVisibility(8);
                    return;
                }
                return;
            }
            if (this.mCheckBox != null) {
                this.mCheckBox.setVisibility(8);
            }
            if (this.mRadioButton != null) {
                this.mRadioButton.setVisibility(8);
            }
            if (this.mTitleView instanceof CheckedTextView) {
                ((CheckedTextView) this.mTitleView).setChecked(false);
            }
        }
    }

    public void setChecked(boolean checked) {
        CompoundButton compoundButton;
        if (this.mItemData.isExclusiveCheckable()) {
            if (this.mRadioButton == null) {
                insertRadioButton();
            }
            compoundButton = this.mRadioButton;
        } else {
            if (this.mCheckBox == null) {
                insertCheckBox();
            }
            compoundButton = this.mCheckBox;
        }
        compoundButton.setChecked(checked);
        if (this.mIsCheckedTextColorMode && this.mItemData.isCheckable()) {
            ((CheckedTextView) this.mTitleView).setChecked(checked);
        }
    }

    public void setShortcut(boolean showShortcut, char shortcutKey) {
        int newVisibility = (showShortcut && this.mItemData.shouldShowShortcut()) ? 0 : 8;
        if (newVisibility == 0) {
            this.mShortcutView.setText(this.mItemData.getShortcutLabel());
        }
        if (this.mShortcutView.getVisibility() != newVisibility) {
            this.mShortcutView.setVisibility(newVisibility);
        }
    }

    public void setIcon(Drawable icon) {
        boolean showIcon;
        if (this.mItemData.shouldShowIcon() || this.mForceShowIcon) {
            showIcon = true;
        } else {
            showIcon = false;
        }
        if (!showIcon && !this.mPreserveIconSpacing) {
            return;
        }
        if (this.mIconView != null || icon != null || this.mPreserveIconSpacing) {
            if (this.mIconView == null) {
                insertIconView();
            }
            if (icon != null || this.mPreserveIconSpacing) {
                ImageView imageView = this.mIconView;
                if (!showIcon) {
                    icon = null;
                }
                imageView.setImageDrawable(icon);
                if (this.mIconView.getVisibility() != 0) {
                    this.mIconView.setVisibility(0);
                    return;
                }
                return;
            }
            this.mIconView.setVisibility(8);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mIconView != null && this.mPreserveIconSpacing) {
            LayoutParams lp = getLayoutParams();
            LinearLayout.LayoutParams iconLp = (LinearLayout.LayoutParams) this.mIconView.getLayoutParams();
            if (lp.height > 0 && iconLp.width <= 0) {
                iconLp.width = lp.height;
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void insertIconView() {
        this.mIconView = (ImageView) getInflater().inflate((int) R.layout.list_menu_item_icon, (ViewGroup) this, false);
        addView(this.mIconView, 0);
    }

    private void insertRadioButton() {
        this.mRadioButton = (RadioButton) getInflater().inflate((int) R.layout.list_menu_item_radio, (ViewGroup) this, false);
        addView(this.mRadioButton);
    }

    private void insertCheckBox() {
        this.mCheckBox = (CheckBox) getInflater().inflate((int) R.layout.list_menu_item_checkbox, (ViewGroup) this, false);
        if (this.mIsDeviceDefaultLight) {
            this.mCheckBox.setBackground(null);
        }
        addView(this.mCheckBox);
    }

    public boolean prefersCondensedTitle() {
        return false;
    }

    public boolean showsIcon() {
        return this.mForceShowIcon;
    }

    private LayoutInflater getInflater() {
        if (this.mInflater == null) {
            this.mInflater = LayoutInflater.from(this.mContext);
        }
        return this.mInflater;
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (this.mItemData != null && this.mItemData.hasSubMenu()) {
            info.setCanOpenPopup(true);
        }
    }
}
