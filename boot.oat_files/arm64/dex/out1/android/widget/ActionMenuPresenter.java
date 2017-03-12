package android.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Settings$System;
import android.util.GeneralUtil;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.ActionProvider;
import android.view.ActionProvider.SubUiVisibilityListener;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnAttachStateChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewRootImpl;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ActionMenuView.ActionMenuChildView;
import com.android.internal.R;
import com.android.internal.view.ActionBarPolicy;
import com.android.internal.view.menu.ActionMenuItemView;
import com.android.internal.view.menu.ActionMenuItemView.PopupCallback;
import com.android.internal.view.menu.BaseMenuPresenter;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuItemImpl;
import com.android.internal.view.menu.MenuPopupHelper;
import com.android.internal.view.menu.MenuPresenter.Callback;
import com.android.internal.view.menu.MenuView;
import com.android.internal.view.menu.MenuView.ItemView;
import com.android.internal.view.menu.SubMenuBuilder;
import com.samsung.android.multiwindow.MultiWindowStyle;
import java.util.ArrayList;
import java.util.List;

public class ActionMenuPresenter extends BaseMenuPresenter implements SubUiVisibilityListener {
    private static final boolean ACTIONBAR_ANIMATIONS_ENABLED = false;
    private static final int ITEM_ANIMATION_DURATION = 150;
    private static final String TAG = "ActionMenuPresenter";
    private final SparseBooleanArray mActionButtonGroups = new SparseBooleanArray();
    private ActionButtonSubmenu mActionButtonPopup;
    private int mActionItemWidthLimit;
    private OnAttachStateChangeListener mAttachStateChangeListener = new OnAttachStateChangeListener() {
        public void onViewAttachedToWindow(View v) {
        }

        public void onViewDetachedFromWindow(View v) {
            ((View) ActionMenuPresenter.this.mMenuView).getViewTreeObserver().removeOnPreDrawListener(ActionMenuPresenter.this.mItemAnimationPreDrawListener);
            ActionMenuPresenter.this.mPreLayoutItems.clear();
            ActionMenuPresenter.this.mPostLayoutItems.clear();
        }
    };
    private boolean mExpandedActionViewsExclusive;
    private Handler mHandler = new Handler();
    private boolean mIsThemeDeviceDefaultFamily;
    private boolean mIsThemeDeviceDefaultLightFamily;
    private OnPreDrawListener mItemAnimationPreDrawListener = new OnPreDrawListener() {
        public boolean onPreDraw() {
            ActionMenuPresenter.this.computeMenuItemAnimationInfo(false);
            ((View) ActionMenuPresenter.this.mMenuView).getViewTreeObserver().removeOnPreDrawListener(this);
            ActionMenuPresenter.this.runItemAnimations();
            return true;
        }
    };
    private int mMaxItems;
    private boolean mMaxItemsSet;
    private int mMinCellSize;
    int mOpenSubMenuId;
    private View mOverflowButton;
    private OverflowPopup mOverflowPopup;
    private Drawable mPendingOverflowIcon;
    private boolean mPendingOverflowIconSet;
    private ActionMenuPopupCallback mPopupCallback;
    final PopupPresenterCallback mPopupPresenterCallback = new PopupPresenterCallback();
    private SparseArray<MenuItemLayoutInfo> mPostLayoutItems = new SparseArray();
    private OpenOverflowRunnable mPostedOpenRunnable;
    private SparseArray<MenuItemLayoutInfo> mPreLayoutItems = new SparseArray();
    private boolean mReserveOverflow;
    private boolean mReserveOverflowSet;
    private List<ItemAnimationInfo> mRunningItemAnimations = new ArrayList();
    private boolean mStrictWidthLimit;
    private boolean mUseTextItemMode;
    private int mWidthLimit;
    private boolean mWidthLimitSet;

    private class ActionButtonSubmenu extends MenuPopupHelper {
        private SubMenuBuilder mSubMenu;

        public ActionButtonSubmenu(Context context, SubMenuBuilder subMenu) {
            super(context, subMenu, null, false, R.attr.actionOverflowMenuStyle);
            this.mSubMenu = subMenu;
            if (!((MenuItemImpl) subMenu.getItem()).isActionButton()) {
                setAnchorView(ActionMenuPresenter.this.mOverflowButton == null ? (View) ActionMenuPresenter.this.mMenuView : ActionMenuPresenter.this.mOverflowButton);
            }
            if (ActionMenuPresenter.this.mIsThemeDeviceDefaultFamily) {
                setGravity(Gravity.END);
            }
            setCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
            boolean preserveIconSpacing = false;
            int count = subMenu.size();
            for (int i = 0; i < count; i++) {
                MenuItem childItem = subMenu.getItem(i);
                if (childItem.isVisible() && childItem.getIcon() != null) {
                    preserveIconSpacing = true;
                    break;
                }
            }
            setForceShowIcon(preserveIconSpacing);
        }

        public void onDismiss() {
            super.onDismiss();
            ActionMenuPresenter.this.mActionButtonPopup = null;
            ActionMenuPresenter.this.mOpenSubMenuId = 0;
        }
    }

    private class ActionMenuPopupCallback extends PopupCallback {
        private ActionMenuPopupCallback() {
        }

        public ListPopupWindow getPopup() {
            return ActionMenuPresenter.this.mActionButtonPopup != null ? ActionMenuPresenter.this.mActionButtonPopup.getPopup() : null;
        }
    }

    private static class ItemAnimationInfo {
        static final int FADE_IN = 1;
        static final int FADE_OUT = 2;
        static final int MOVE = 0;
        int animType;
        Animator animator;
        int id;
        MenuItemLayoutInfo menuItemLayoutInfo;

        ItemAnimationInfo(int id, MenuItemLayoutInfo info, Animator anim, int animType) {
            this.id = id;
            this.menuItemLayoutInfo = info;
            this.animator = anim;
            this.animType = animType;
        }
    }

    private static class MenuItemLayoutInfo {
        int left;
        int top;
        View view;

        MenuItemLayoutInfo(View view, boolean preLayout) {
            this.left = view.getLeft();
            this.top = view.getTop();
            if (preLayout) {
                this.left = (int) (((float) this.left) + view.getTranslationX());
                this.top = (int) (((float) this.top) + view.getTranslationY());
            }
            this.view = view;
        }
    }

    private class OpenOverflowRunnable implements Runnable {
        private OverflowPopup mPopup;

        public OpenOverflowRunnable(OverflowPopup popup) {
            this.mPopup = popup;
        }

        public void run() {
            if (ActionMenuPresenter.this.mMenu != null) {
                ActionMenuPresenter.this.mMenu.changeMenuMode();
            }
            View menuView = (View) ActionMenuPresenter.this.mMenuView;
            if (!(menuView == null || menuView.getWindowToken() == null || !this.mPopup.tryShow())) {
                ActionMenuPresenter.this.mOverflowPopup = this.mPopup;
            }
            ActionMenuPresenter.this.mPostedOpenRunnable = null;
        }
    }

    private class OverflowMenuButton extends ImageButton implements ActionMenuChildView {
        private final float[] mTempPts = new float[2];

        public OverflowMenuButton(Context context) {
            super(context, null, R.attr.actionOverflowButtonStyle);
            setClickable(true);
            setFocusable(true);
            setVisibility(0);
            setEnabled(true);
            if (getHoverUIFeatureLevel() >= 2 || ActionMenuPresenter.this.mIsThemeDeviceDefaultFamily) {
                setHoverPopupType(1);
                setLongClickable(true);
            }
        }

        public boolean performClick() {
            if (!super.performClick()) {
                playSoundEffect(0);
                ActionMenuPresenter.this.showOverflowMenu();
            }
            return true;
        }

        public boolean performLongClick() {
            if (super.performLongClick() || !ActionMenuPresenter.this.mIsThemeDeviceDefaultFamily) {
                return true;
            }
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
            if (getLayoutDirection() == 0) {
                referenceX = screenWidth - referenceX;
            }
            referenceX -= (int) (5.0f * density);
            if (ActionMenuPresenter.this.mIsThemeDeviceDefaultFamily) {
                cheatSheet = Toast.twMakeText(context, getContentDescription(), 0);
            } else {
                cheatSheet = Toast.makeText(context, getContentDescription(), 0);
            }
            if (screenPos[1] + height != contentFrame.bottom || height == contentFrame.height()) {
                cheatSheet.setGravity(8388661, referenceX, height + (screenPos[1] - statusBarHeight));
            } else {
                cheatSheet.setGravity(8388693, referenceX, height + (screenHeight - contentFrame.bottom));
            }
            MultiWindowStyle mwStyle = this.mContext.getAppMultiWindowStyle();
            if (mwStyle != null && mwStyle.isCascade()) {
                int gravity;
                int xOffset;
                int yOffset;
                if ((screenPos[1] + height) + ((int) (4.0f * density)) < screenHeight) {
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
            performHapticFeedback(0);
            return true;
        }

        public boolean needsDividerBefore() {
            return false;
        }

        public boolean needsDividerAfter() {
            return false;
        }

        public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfoInternal(info);
            info.setCanOpenPopup(true);
        }

        protected boolean setFrame(int l, int t, int r, int b) {
            boolean changed = super.setFrame(l, t, r, b);
            if (!ActionMenuPresenter.this.mIsThemeDeviceDefaultFamily) {
                Drawable d = getDrawable();
                Drawable bg = getBackground();
                if (!(d == null || bg == null)) {
                    int width = getWidth();
                    int height = getHeight();
                    int halfEdge = Math.max(width, height) / 2;
                    int centerX = (width + (getPaddingLeft() - getPaddingRight())) / 2;
                    int centerY = (height + (getPaddingTop() - getPaddingBottom())) / 2;
                    bg.setHotspotBounds(centerX - halfEdge, centerY - halfEdge, centerX + halfEdge, centerY + halfEdge);
                }
            }
            return changed;
        }
    }

    private class OverflowPopup extends MenuPopupHelper {
        public OverflowPopup(Context context, MenuBuilder menu, View anchorView, boolean overflowOnly) {
            super(context, menu, anchorView, overflowOnly, R.attr.actionOverflowMenuStyle);
            setGravity(Gravity.END);
            setCallback(ActionMenuPresenter.this.mPopupPresenterCallback);
        }

        public void onDismiss() {
            super.onDismiss();
            if (ActionMenuPresenter.this.mMenu != null) {
                if (ActionMenuPresenter.this.isSupportOverlapSubMenu()) {
                    ActionMenuPresenter.this.mMenu.close(false);
                } else {
                    ActionMenuPresenter.this.mMenu.close();
                }
            }
            ActionMenuPresenter.this.mOverflowPopup = null;
        }
    }

    public class OverflowTextMenuButton extends TextView implements ActionMenuChildView {
        private Drawable mArchivedBackground;
        private Drawable mArchivedShowButtonBackground;
        private float mCurrentFontScale = 1.0f;
        private float mDefaultTextSize;
        private float mMaxFontScale = 1.2f;

        public OverflowTextMenuButton(Context context) {
            super(context, null, R.attr.actionOverflowButtonStyle);
            TypedArray a = context.obtainStyledAttributes(null, R.styleable.Theme, 0, 0);
            setTextAppearance(context, a.getResourceId(187, 0));
            a.recycle();
            if (ActionMenuPresenter.this.mIsThemeDeviceDefaultFamily) {
                a = context.getTheme().obtainStyledAttributes(null, R.styleable.Theme, 0, 0);
                int actionMeneTextAppearnceId = a.getResourceId(187, 0);
                a.recycle();
                a = getContext().obtainStyledAttributes(actionMeneTextAppearnceId, R.styleable.TextAppearance);
                TypedValue value = a.peekValue(0);
                a.recycle();
                if (value != null) {
                    this.mDefaultTextSize = TypedValue.complexToFloat(value.data);
                    this.mCurrentFontScale = getContext().getResources().getConfiguration().fontScale;
                    if (this.mCurrentFontScale > this.mMaxFontScale) {
                        this.mCurrentFontScale = this.mMaxFontScale;
                    }
                    setTextSize(1, this.mDefaultTextSize * this.mCurrentFontScale);
                }
            }
            setClickable(true);
            setFocusable(true);
            setVisibility(0);
            setEnabled(true);
            setText(context.getResources().getString(R.string.more_item_label));
            this.mArchivedBackground = getBackground();
            this.mArchivedShowButtonBackground = context.getResources().getDrawable(R.drawable.tw_action_item_with_button_background_light);
            changeButtonBackground();
        }

        private void changeButtonBackground() {
            boolean show = true;
            if (ActionMenuPresenter.this.mIsThemeDeviceDefaultFamily) {
                if (Settings$System.getInt(getContext().getContentResolver(), Settings$System.SHOW_BUTTON_BACKGROUND, 0) != 1) {
                    show = false;
                }
                Drawable d = getBackground();
                if (d instanceof RippleDrawable) {
                    this.mArchivedBackground = d;
                } else {
                    this.mArchivedShowButtonBackground = d;
                }
                if (show) {
                    setBackground(this.mArchivedShowButtonBackground);
                } else {
                    setBackground(this.mArchivedBackground);
                }
            }
        }

        protected void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            if (!(!ActionMenuPresenter.this.mIsThemeDeviceDefaultFamily || newConfig == null || newConfig.fontScale == this.mCurrentFontScale)) {
                this.mCurrentFontScale = newConfig.fontScale;
                if (this.mCurrentFontScale > this.mMaxFontScale) {
                    this.mCurrentFontScale = this.mMaxFontScale;
                }
                setTextSize(1, this.mDefaultTextSize * this.mCurrentFontScale);
            }
            TypedArray a = getContext().obtainStyledAttributes(null, R.styleable.View, R.attr.actionOverflowButtonStyle, 0);
            setMinimumHeight(a.getDimensionPixelSize(37, -1));
            a.recycle();
            setText(this.mContext.getResources().getString(R.string.more_item_label));
            changeButtonBackground();
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            changeButtonBackground();
        }

        public boolean performClick() {
            if (!super.performClick()) {
                playSoundEffect(0);
                if (ActionMenuPresenter.this.mIsThemeDeviceDefaultFamily) {
                    ActionMenuPresenter.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            ActionMenuPresenter.this.showOverflowMenu();
                        }
                    }, 100);
                } else {
                    ActionMenuPresenter.this.showOverflowMenu();
                }
            }
            return true;
        }

        public boolean needsDividerBefore() {
            return false;
        }

        public boolean needsDividerAfter() {
            return false;
        }

        public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
            super.onInitializeAccessibilityNodeInfo(info);
            info.setClassName("android.widget.Button");
            info.setCanOpenPopup(true);
        }
    }

    private class PopupPresenterCallback implements Callback {
        private PopupPresenterCallback() {
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            if (subMenu == null) {
                return false;
            }
            ActionMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder) subMenu).getItem().getItemId();
            Callback cb = ActionMenuPresenter.this.getCallback();
            return cb != null ? cb.onOpenSubMenu(subMenu) : false;
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
            if (menu instanceof SubMenuBuilder) {
                ((SubMenuBuilder) menu).getRootMenu().close(false);
            }
            Callback cb = ActionMenuPresenter.this.getCallback();
            if (cb != null) {
                cb.onCloseMenu(menu, allMenusAreClosing);
            }
        }
    }

    private static class SavedState implements Parcelable {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        public int openSubMenuId;

        SavedState() {
        }

        SavedState(Parcel in) {
            this.openSubMenuId = in.readInt();
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.openSubMenuId);
        }
    }

    public ActionMenuPresenter(Context context) {
        boolean z;
        boolean z2 = true;
        super(context, R.layout.action_menu_layout, R.layout.action_menu_item_layout);
        TypedValue outValue = new TypedValue();
        TypedValue colorValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefaultDark, colorValue, true);
        this.mIsThemeDeviceDefaultFamily = outValue.data != 0;
        if (outValue.data == 0 || colorValue.data != 0) {
            z = false;
        } else {
            z = true;
        }
        this.mIsThemeDeviceDefaultLightFamily = z;
        if (!(this.mIsThemeDeviceDefaultFamily && context.getResources().getBoolean(R.bool.tw_action_bar_text_item_mode))) {
            z2 = false;
        }
        this.mUseTextItemMode = z2;
    }

    public void initForMenu(Context context, MenuBuilder menu) {
        super.initForMenu(context, menu);
        if (this.mIsThemeDeviceDefaultFamily) {
            super.setMenuLayoutRes(R.layout.tw_action_menu_layout, R.layout.tw_action_menu_item_layout);
        }
        Resources res = context.getResources();
        ActionBarPolicy abp = ActionBarPolicy.get(context);
        if (!this.mReserveOverflowSet) {
            this.mReserveOverflow = abp.showsOverflowMenuButton();
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = abp.getEmbeddedMenuWidthLimit();
            if (this.mIsThemeDeviceDefaultFamily) {
                this.mWidthLimit = (int) (((double) this.mContext.getResources().getDisplayMetrics().widthPixels) * 0.7d);
            }
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = abp.getMaxActionButtons();
        }
        int width = this.mWidthLimit;
        if (this.mReserveOverflow) {
            if (this.mOverflowButton == null) {
                if (this.mUseTextItemMode) {
                    this.mOverflowButton = new OverflowTextMenuButton(this.mSystemContext);
                } else {
                    this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
                    if (this.mPendingOverflowIconSet) {
                        ((OverflowMenuButton) this.mOverflowButton).setImageDrawable(this.mPendingOverflowIcon);
                        this.mPendingOverflowIcon = null;
                        this.mPendingOverflowIconSet = false;
                    }
                }
                int spec = MeasureSpec.makeMeasureSpec(0, 0);
                this.mOverflowButton.measure(spec, spec);
            }
            width -= this.mOverflowButton.getMeasuredWidth();
        } else {
            this.mOverflowButton = null;
        }
        this.mActionItemWidthLimit = width;
        this.mMinCellSize = (int) (56.0f * res.getDisplayMetrics().density);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.mOverflowButton != null) {
            TypedArray a = this.mContext.obtainStyledAttributes(null, R.styleable.View, R.attr.actionOverflowButtonStyle, 0);
            this.mOverflowButton.setContentDescription(a.getText(44));
            this.mOverflowButton.setMinimumHeight(a.getDimensionPixelSize(37, -1));
            a.recycle();
        }
        if (!this.mMaxItemsSet) {
            this.mMaxItems = this.mContext.getResources().getInteger(R.integer.max_action_buttons);
        }
        if (!this.mWidthLimitSet) {
            this.mWidthLimit = this.mContext.getResources().getDisplayMetrics().widthPixels / 2;
            if (this.mIsThemeDeviceDefaultFamily) {
                this.mWidthLimit = (int) (((double) this.mContext.getResources().getDisplayMetrics().widthPixels) * 0.7d);
            }
        }
        if (!this.mReserveOverflow || this.mOverflowButton == null) {
            this.mActionItemWidthLimit = this.mWidthLimit;
        } else {
            this.mActionItemWidthLimit = this.mWidthLimit - this.mOverflowButton.getMeasuredWidth();
        }
        if (this.mMenu != null) {
            this.mMenu.onItemsChanged(true);
        }
    }

    public void setWidthLimit(int width, boolean strict) {
        this.mWidthLimit = width;
        this.mStrictWidthLimit = strict;
        this.mWidthLimitSet = true;
    }

    public void setReserveOverflow(boolean reserveOverflow) {
        this.mReserveOverflow = reserveOverflow;
        this.mReserveOverflowSet = true;
    }

    public void setItemLimit(int itemCount) {
        this.mMaxItems = itemCount;
        this.mMaxItemsSet = true;
    }

    public void setExpandedActionViewsExclusive(boolean isExclusive) {
        this.mExpandedActionViewsExclusive = isExclusive;
    }

    public void setOverflowIcon(Drawable icon) {
        if (!this.mUseTextItemMode) {
            if (this.mOverflowButton != null) {
                ((OverflowMenuButton) this.mOverflowButton).setImageDrawable(icon);
                return;
            }
            this.mPendingOverflowIconSet = true;
            this.mPendingOverflowIcon = icon;
        }
    }

    public Drawable getOverflowIcon() {
        if (this.mUseTextItemMode) {
            return null;
        }
        if (this.mOverflowButton != null) {
            return ((OverflowMenuButton) this.mOverflowButton).getDrawable();
        }
        if (this.mPendingOverflowIconSet) {
            return this.mPendingOverflowIcon;
        }
        return null;
    }

    public MenuView getMenuView(ViewGroup root) {
        MenuView oldMenuView = this.mMenuView;
        MenuView result = super.getMenuView(root);
        if (oldMenuView != result) {
            ((ActionMenuView) result).setPresenter(this);
            if (oldMenuView != null) {
                ((View) oldMenuView).removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
            }
            ((View) result).addOnAttachStateChangeListener(this.mAttachStateChangeListener);
        }
        return result;
    }

    public View getItemView(MenuItemImpl item, View convertView, ViewGroup parent) {
        View actionView = item.getActionView();
        if (actionView == null || item.hasCollapsibleActionView()) {
            actionView = super.getItemView(item, convertView, parent);
        }
        actionView.setVisibility(item.isActionViewExpanded() ? 8 : 0);
        ActionMenuView menuParent = (ActionMenuView) parent;
        LayoutParams lp = actionView.getLayoutParams();
        if (!menuParent.checkLayoutParams(lp)) {
            actionView.setLayoutParams(menuParent.generateLayoutParams(lp));
        }
        return actionView;
    }

    public void bindItemView(MenuItemImpl item, ItemView itemView) {
        itemView.initialize(item, 0);
        ActionMenuItemView actionItemView = (ActionMenuItemView) itemView;
        actionItemView.setItemInvoker(this.mMenuView);
        if (this.mPopupCallback == null) {
            this.mPopupCallback = new ActionMenuPopupCallback();
        }
        actionItemView.setPopupCallback(this.mPopupCallback);
    }

    public boolean shouldIncludeItem(int childIndex, MenuItemImpl item) {
        return item.isActionButton();
    }

    private void computeMenuItemAnimationInfo(boolean preLayout) {
        ViewGroup menuView = this.mMenuView;
        int count = menuView.getChildCount();
        SparseArray items = preLayout ? this.mPreLayoutItems : this.mPostLayoutItems;
        for (int i = 0; i < count; i++) {
            View child = menuView.getChildAt(i);
            int id = child.getId();
            if (!(id <= 0 || child.getWidth() == 0 || child.getHeight() == 0)) {
                items.put(id, new MenuItemLayoutInfo(child, preLayout));
            }
        }
    }

    private void runItemAnimations() {
        int i;
        for (i = 0; i < this.mPreLayoutItems.size(); i++) {
            int j;
            ItemAnimationInfo oldInfo;
            ObjectAnimator anim;
            float oldAlpha;
            int id = this.mPreLayoutItems.keyAt(i);
            final MenuItemLayoutInfo menuItemLayoutInfoPre = (MenuItemLayoutInfo) this.mPreLayoutItems.get(id);
            int postLayoutIndex = this.mPostLayoutItems.indexOfKey(id);
            if (postLayoutIndex >= 0) {
                MenuItemLayoutInfo menuItemLayoutInfoPost = (MenuItemLayoutInfo) this.mPostLayoutItems.valueAt(postLayoutIndex);
                PropertyValuesHolder pvhX = null;
                PropertyValuesHolder pvhY = null;
                if (menuItemLayoutInfoPre.left != menuItemLayoutInfoPost.left) {
                    pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, new float[]{(float) (menuItemLayoutInfoPre.left - menuItemLayoutInfoPost.left), 0.0f});
                }
                if (menuItemLayoutInfoPre.top != menuItemLayoutInfoPost.top) {
                    pvhY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, new float[]{(float) (menuItemLayoutInfoPre.top - menuItemLayoutInfoPost.top), 0.0f});
                }
                if (!(pvhX == null && pvhY == null)) {
                    for (j = 0; j < this.mRunningItemAnimations.size(); j++) {
                        oldInfo = (ItemAnimationInfo) this.mRunningItemAnimations.get(j);
                        if (oldInfo.id == id && oldInfo.animType == 0) {
                            oldInfo.animator.cancel();
                        }
                    }
                    if (pvhX == null) {
                        anim = ObjectAnimator.ofPropertyValuesHolder(menuItemLayoutInfoPost.view, new PropertyValuesHolder[]{pvhY});
                    } else if (pvhY != null) {
                        anim = ObjectAnimator.ofPropertyValuesHolder(menuItemLayoutInfoPost.view, new PropertyValuesHolder[]{pvhX, pvhY});
                    } else {
                        anim = ObjectAnimator.ofPropertyValuesHolder(menuItemLayoutInfoPost.view, new PropertyValuesHolder[]{pvhX});
                    }
                    anim.setDuration(150);
                    anim.start();
                    this.mRunningItemAnimations.add(new ItemAnimationInfo(id, menuItemLayoutInfoPost, anim, 0));
                    anim.addListener(new AnimatorListenerAdapter() {
                        public void onAnimationEnd(Animator animation) {
                            for (int j = 0; j < ActionMenuPresenter.this.mRunningItemAnimations.size(); j++) {
                                if (((ItemAnimationInfo) ActionMenuPresenter.this.mRunningItemAnimations.get(j)).animator == animation) {
                                    ActionMenuPresenter.this.mRunningItemAnimations.remove(j);
                                    return;
                                }
                            }
                        }
                    });
                }
                this.mPostLayoutItems.remove(id);
            } else {
                oldAlpha = 1.0f;
                for (j = 0; j < this.mRunningItemAnimations.size(); j++) {
                    oldInfo = (ItemAnimationInfo) this.mRunningItemAnimations.get(j);
                    if (oldInfo.id == id && oldInfo.animType == 1) {
                        oldAlpha = oldInfo.menuItemLayoutInfo.view.getAlpha();
                        oldInfo.animator.cancel();
                    }
                }
                anim = ObjectAnimator.ofFloat(menuItemLayoutInfoPre.view, View.ALPHA, new float[]{oldAlpha, 0.0f});
                ((ViewGroup) this.mMenuView).getOverlay().add(menuItemLayoutInfoPre.view);
                anim.setDuration(150);
                anim.start();
                this.mRunningItemAnimations.add(new ItemAnimationInfo(id, menuItemLayoutInfoPre, anim, 2));
                anim.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        for (int j = 0; j < ActionMenuPresenter.this.mRunningItemAnimations.size(); j++) {
                            if (((ItemAnimationInfo) ActionMenuPresenter.this.mRunningItemAnimations.get(j)).animator == animation) {
                                ActionMenuPresenter.this.mRunningItemAnimations.remove(j);
                                break;
                            }
                        }
                        ((ViewGroup) ActionMenuPresenter.this.mMenuView).getOverlay().remove(menuItemLayoutInfoPre.view);
                    }
                });
            }
        }
        for (i = 0; i < this.mPostLayoutItems.size(); i++) {
            id = this.mPostLayoutItems.keyAt(i);
            postLayoutIndex = this.mPostLayoutItems.indexOfKey(id);
            if (postLayoutIndex >= 0) {
                MenuItemLayoutInfo menuItemLayoutInfo = (MenuItemLayoutInfo) this.mPostLayoutItems.valueAt(postLayoutIndex);
                oldAlpha = 0.0f;
                for (j = 0; j < this.mRunningItemAnimations.size(); j++) {
                    oldInfo = (ItemAnimationInfo) this.mRunningItemAnimations.get(j);
                    if (oldInfo.id == id && oldInfo.animType == 2) {
                        oldAlpha = oldInfo.menuItemLayoutInfo.view.getAlpha();
                        oldInfo.animator.cancel();
                    }
                }
                anim = ObjectAnimator.ofFloat(menuItemLayoutInfo.view, View.ALPHA, new float[]{oldAlpha, 1.0f});
                anim.start();
                anim.setDuration(150);
                this.mRunningItemAnimations.add(new ItemAnimationInfo(id, menuItemLayoutInfo, anim, 1));
                anim.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animation) {
                        for (int j = 0; j < ActionMenuPresenter.this.mRunningItemAnimations.size(); j++) {
                            if (((ItemAnimationInfo) ActionMenuPresenter.this.mRunningItemAnimations.get(j)).animator == animation) {
                                ActionMenuPresenter.this.mRunningItemAnimations.remove(j);
                                return;
                            }
                        }
                    }
                });
            }
        }
        this.mPreLayoutItems.clear();
        this.mPostLayoutItems.clear();
    }

    private void setupItemAnimations() {
        computeMenuItemAnimationInfo(true);
        ((View) this.mMenuView).getViewTreeObserver().addOnPreDrawListener(this.mItemAnimationPreDrawListener);
    }

    public void updateMenuView(boolean cleared) {
        int count;
        if (((ViewGroup) ((View) this.mMenuView).getParent()) != null) {
            super.updateMenuView(cleared);
            ((View) this.mMenuView).requestLayout();
        } else {
            super.updateMenuView(cleared);
            ((View) this.mMenuView).requestLayout();
        }
        if (this.mMenu != null) {
            ArrayList<MenuItemImpl> actionItems = this.mMenu.getActionItems();
            count = actionItems.size();
            for (int i = 0; i < count; i++) {
                ActionProvider provider = ((MenuItemImpl) actionItems.get(i)).getActionProvider();
                if (provider != null) {
                    provider.setSubUiVisibilityListener(this);
                }
            }
        }
        ArrayList<MenuItemImpl> nonActionItems = this.mMenu != null ? this.mMenu.getNonActionItems() : null;
        boolean hasOverflow = false;
        if (this.mReserveOverflow && nonActionItems != null) {
            count = nonActionItems.size();
            hasOverflow = count == 1 ? !((MenuItemImpl) nonActionItems.get(0)).isActionViewExpanded() : count > 0;
        }
        if (hasOverflow) {
            if (this.mOverflowButton == null) {
                if (this.mUseTextItemMode) {
                    this.mOverflowButton = new OverflowTextMenuButton(this.mSystemContext);
                } else {
                    this.mOverflowButton = new OverflowMenuButton(this.mSystemContext);
                }
            }
            ViewGroup parent = (ViewGroup) this.mOverflowButton.getParent();
            if (parent != this.mMenuView) {
                if (parent != null) {
                    parent.removeView(this.mOverflowButton);
                }
                ActionMenuView menuView = this.mMenuView;
                menuView.addView(this.mOverflowButton, menuView.generateOverflowButtonLayoutParams());
                ViewRootImpl viewRootImpl = menuView.getViewRootImpl();
                if (viewRootImpl != null) {
                    View view = viewRootImpl.getView();
                    if (view != null) {
                        this.mOverflowButton.setLayoutDirection(view.getLayoutDirection());
                    }
                }
            }
        } else if (this.mOverflowButton != null && this.mOverflowButton.getParent() == this.mMenuView) {
            ((ViewGroup) this.mMenuView).removeView(this.mOverflowButton);
            if (isOverflowMenuShowing()) {
                hideOverflowMenu();
            }
        }
        if ((this.mOverflowButton == null || this.mOverflowButton.getVisibility() != 0) && isOverflowMenuShowing()) {
            hideOverflowMenu();
        }
        ((ActionMenuView) this.mMenuView).setOverflowReserved(this.mReserveOverflow);
    }

    public boolean filterLeftoverView(ViewGroup parent, int childIndex) {
        if (parent.getChildAt(childIndex) == this.mOverflowButton) {
            return false;
        }
        return super.filterLeftoverView(parent, childIndex);
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
        if (subMenu == null || !subMenu.hasVisibleItems()) {
            return false;
        }
        SubMenuBuilder topSubMenu = subMenu;
        while (topSubMenu.getParentMenu() != this.mMenu) {
            topSubMenu = (SubMenuBuilder) topSubMenu.getParentMenu();
        }
        View anchor = findViewForItem(topSubMenu.getItem());
        if (anchor == null) {
            if (this.mOverflowButton == null) {
                return false;
            }
            anchor = this.mOverflowButton;
        }
        this.mOpenSubMenuId = subMenu.getItem().getItemId();
        this.mActionButtonPopup = new ActionButtonSubmenu(this.mContext, subMenu);
        this.mActionButtonPopup.setAnchorView(anchor);
        this.mActionButtonPopup.show();
        super.onSubMenuSelected(subMenu);
        return true;
    }

    public boolean twOnSubMenuSelected(SubMenuBuilder subMenu, View view) {
        if ((this.mActionButtonPopup != null && this.mActionButtonPopup.isShowing()) || subMenu == null || !subMenu.hasVisibleItems()) {
            return false;
        }
        boolean isActionSubMenu = true;
        SubMenuBuilder topSubMenu = subMenu;
        while (topSubMenu.getParentMenu() != this.mMenu) {
            topSubMenu = (SubMenuBuilder) topSubMenu.getParentMenu();
        }
        if (findViewForItem(topSubMenu.getItem()) == null) {
            isActionSubMenu = false;
            if (this.mOverflowButton == null) {
                return false;
            }
            if (null == null) {
                View anchor = this.mOverflowButton;
            }
        }
        if (!isSupportOverlapSubMenu() || isActionSubMenu) {
            return onSubMenuSelected(subMenu);
        }
        this.mOpenSubMenuId = subMenu.getItem().getItemId();
        this.mActionButtonPopup = new ActionButtonSubmenu(this.mContext, subMenu);
        this.mActionButtonPopup.setAnchorView(this.mOverflowButton);
        this.mActionButtonPopup.twTryShowAtLocation(view);
        super.onSubMenuSelected(subMenu);
        return true;
    }

    private boolean isSupportOverlapSubMenu() {
        return GeneralUtil.isTablet() && this.mIsThemeDeviceDefaultLightFamily;
    }

    private View findViewForItem(MenuItem item) {
        ViewGroup parent = this.mMenuView;
        if (parent == null) {
            return null;
        }
        int count = parent.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            if ((child instanceof ItemView) && ((ItemView) child).getItemData() == item) {
                return child;
            }
        }
        return null;
    }

    public boolean showOverflowMenu() {
        if (!this.mReserveOverflow || isOverflowMenuShowing() || this.mMenu == null || this.mMenuView == null || this.mPostedOpenRunnable != null || this.mMenu.getNonActionItems().isEmpty()) {
            return false;
        }
        this.mPostedOpenRunnable = new OpenOverflowRunnable(new OverflowPopup(this.mContext, this.mMenu, this.mOverflowButton, true));
        ((View) this.mMenuView).post(this.mPostedOpenRunnable);
        super.onSubMenuSelected(null);
        return true;
    }

    public boolean hideOverflowMenu() {
        if (this.mPostedOpenRunnable == null || this.mMenuView == null) {
            MenuPopupHelper popup = this.mOverflowPopup;
            if (popup == null) {
                return false;
            }
            popup.dismiss();
            return true;
        }
        ((View) this.mMenuView).removeCallbacks(this.mPostedOpenRunnable);
        this.mPostedOpenRunnable = null;
        return true;
    }

    public boolean dismissPopupMenus() {
        boolean result = hideOverflowMenu();
        if (result || isSupportOverlapSubMenu()) {
            return result | hideSubMenus();
        }
        return result;
    }

    public boolean hideSubMenus() {
        if (this.mActionButtonPopup == null) {
            return false;
        }
        this.mActionButtonPopup.dismiss();
        return true;
    }

    public boolean isOverflowMenuShowing() {
        return this.mOverflowPopup != null && this.mOverflowPopup.isShowing();
    }

    public boolean isOverflowMenuShowPending() {
        return this.mPostedOpenRunnable != null || isOverflowMenuShowing();
    }

    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    public boolean flagActionItems() {
        int i;
        ArrayList<MenuItemImpl> visibleItems = this.mMenu.getVisibleItems();
        int itemsSize = visibleItems.size();
        int maxActions = this.mMaxItems;
        int widthLimit = this.mActionItemWidthLimit;
        int querySpec = MeasureSpec.makeMeasureSpec(0, 0);
        ViewGroup parent = (ViewGroup) this.mMenuView;
        int requiredItems = 0;
        int requestedItems = 0;
        int firstActionWidth = 0;
        boolean hasOverflow = false;
        for (i = 0; i < itemsSize; i++) {
            MenuItemImpl item = (MenuItemImpl) visibleItems.get(i);
            if (item.requiresActionButton()) {
                requiredItems++;
            } else if (item.requestsActionButton()) {
                requestedItems++;
            } else {
                hasOverflow = true;
            }
            if (this.mExpandedActionViewsExclusive && item.isActionViewExpanded()) {
                maxActions = 0;
            }
        }
        if (this.mReserveOverflow && (hasOverflow || requiredItems + requestedItems > maxActions)) {
            maxActions--;
        }
        maxActions -= requiredItems;
        SparseBooleanArray seenGroups = this.mActionButtonGroups;
        seenGroups.clear();
        int cellSize = 0;
        int cellsRemaining = 0;
        if (this.mStrictWidthLimit) {
            cellsRemaining = widthLimit / this.mMinCellSize;
            cellSize = this.mMinCellSize + ((widthLimit % this.mMinCellSize) / cellsRemaining);
        }
        for (i = 0; i < itemsSize; i++) {
            item = (MenuItemImpl) visibleItems.get(i);
            View v;
            int measuredWidth;
            int groupId;
            if (item.requiresActionButton()) {
                v = getItemView(item, null, parent);
                if (this.mStrictWidthLimit) {
                    cellsRemaining -= ActionMenuView.measureChildForCells(v, cellSize, cellsRemaining, querySpec, 0);
                } else {
                    v.measure(querySpec, querySpec);
                }
                measuredWidth = v.getMeasuredWidth();
                widthLimit -= measuredWidth;
                if (firstActionWidth == 0) {
                    firstActionWidth = measuredWidth;
                }
                groupId = item.getGroupId();
                if (groupId != 0) {
                    seenGroups.put(groupId, true);
                }
                item.setIsActionButton(true);
            } else if (item.requestsActionButton()) {
                groupId = item.getGroupId();
                boolean inGroup = seenGroups.get(groupId);
                boolean isAction = (maxActions > 0 || inGroup) && widthLimit > 0 && (!this.mStrictWidthLimit || cellsRemaining > 0);
                if (isAction) {
                    v = getItemView(item, null, parent);
                    if (this.mStrictWidthLimit) {
                        int cells = ActionMenuView.measureChildForCells(v, cellSize, cellsRemaining, querySpec, 0);
                        cellsRemaining -= cells;
                        if (cells == 0) {
                            isAction = false;
                        }
                    } else {
                        v.measure(querySpec, querySpec);
                    }
                    measuredWidth = v.getMeasuredWidth();
                    widthLimit -= measuredWidth;
                    if (firstActionWidth == 0) {
                        firstActionWidth = measuredWidth;
                    }
                    if (this.mStrictWidthLimit) {
                        int i2;
                        if (widthLimit >= 0) {
                            i2 = 1;
                        } else {
                            i2 = 0;
                        }
                        isAction &= i2;
                    } else if (this.mIsThemeDeviceDefaultFamily) {
                        isAction &= widthLimit >= 0 ? 1 : 0;
                    } else {
                        isAction &= widthLimit + firstActionWidth > 0 ? 1 : 0;
                    }
                }
                if (isAction && groupId != 0) {
                    seenGroups.put(groupId, true);
                } else if (inGroup) {
                    seenGroups.put(groupId, false);
                    for (int j = 0; j < i; j++) {
                        MenuItemImpl areYouMyGroupie = (MenuItemImpl) visibleItems.get(j);
                        if (areYouMyGroupie.getGroupId() == groupId) {
                            if (areYouMyGroupie.isActionButton()) {
                                maxActions++;
                            }
                            areYouMyGroupie.setIsActionButton(false);
                        }
                    }
                }
                if (isAction) {
                    maxActions--;
                }
                item.setIsActionButton(isAction);
            } else {
                item.setIsActionButton(false);
            }
        }
        return true;
    }

    public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        dismissPopupMenus();
        super.onCloseMenu(menu, allMenusAreClosing);
    }

    public Parcelable onSaveInstanceState() {
        SavedState state = new SavedState();
        state.openSubMenuId = this.mOpenSubMenuId;
        return state;
    }

    public void onRestoreInstanceState(Parcelable state) {
        SavedState saved = (SavedState) state;
        if (saved.openSubMenuId > 0) {
            MenuItem item = this.mMenu.findItem(saved.openSubMenuId);
            if (item != null) {
                onSubMenuSelected((SubMenuBuilder) item.getSubMenu());
            }
        }
    }

    public void onSubUiVisibilityChanged(boolean isVisible) {
        if (isVisible) {
            super.onSubMenuSelected(null);
        } else {
            this.mMenu.close(false);
        }
    }

    public void setMenuView(ActionMenuView menuView) {
        if (this.mMenuView != null) {
            ((View) this.mMenuView).removeOnAttachStateChangeListener(this.mAttachStateChangeListener);
        }
        this.mMenuView = menuView;
        menuView.initialize(this.mMenu);
        menuView.addOnAttachStateChangeListener(this.mAttachStateChangeListener);
    }
}
