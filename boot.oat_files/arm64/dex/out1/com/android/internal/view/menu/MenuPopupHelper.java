package com.android.internal.view.menu;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcelable;
import android.util.GeneralUtil;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import com.android.internal.R;
import com.android.internal.view.menu.MenuPresenter.Callback;
import com.android.internal.view.menu.MenuView.ItemView;
import com.samsung.android.multiwindow.MultiWindowStyle;
import java.util.ArrayList;

public class MenuPopupHelper implements OnItemClickListener, OnKeyListener, OnGlobalLayoutListener, OnDismissListener, OnAttachStateChangeListener, MenuPresenter {
    static final int ITEM_LAYOUT = 17367225;
    static final int TW_CHECKED_ITEM_LAYOUT = 17367338;
    static final int TW_ITEM_LAYOUT = 17367339;
    private final MenuAdapter mAdapter;
    private View mAnchorView;
    private int mContentWidth;
    private final Context mContext;
    private int mDropDownGravity;
    boolean mForceShowIcon;
    private boolean mHasContentWidth;
    private final LayoutInflater mInflater;
    private boolean mIsParentThemeDeviceDefault;
    private boolean mIsParentThemeDeviceDefaultLight;
    private ViewGroup mMeasureParent;
    private final MenuBuilder mMenu;
    private final boolean mOverflowOnly;
    private ListPopupWindow mPopup;
    private final int mPopupMaxWidth;
    private final int mPopupStyleAttr;
    private final int mPopupStyleRes;
    public int mPopupWindowLayout;
    private View mPositionAnchorView;
    private Callback mPresenterCallback;
    private ViewTreeObserver mTreeObserver;

    private class MenuAdapter extends BaseAdapter {
        private MenuBuilder mAdapterMenu;
        private int mExpandedIndex = -1;

        public MenuAdapter(MenuBuilder menu) {
            this.mAdapterMenu = menu;
            findExpandedIndex();
        }

        public int getCount() {
            ArrayList<MenuItemImpl> items = MenuPopupHelper.this.mOverflowOnly ? this.mAdapterMenu.getNonActionItems() : this.mAdapterMenu.getVisibleItems();
            if (this.mExpandedIndex < 0) {
                return items.size();
            }
            return items.size() - 1;
        }

        public MenuItemImpl getItem(int position) {
            ArrayList<MenuItemImpl> items = MenuPopupHelper.this.mOverflowOnly ? this.mAdapterMenu.getNonActionItems() : this.mAdapterMenu.getVisibleItems();
            if (this.mExpandedIndex >= 0 && position >= this.mExpandedIndex) {
                position++;
            }
            return (MenuItemImpl) items.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                if (!MenuPopupHelper.this.twIsPopupMenuDisplay()) {
                    convertView = MenuPopupHelper.this.mInflater.inflate(17367225, parent, false);
                } else if (MenuPopupHelper.this.isSupportOverlapSubMenu() && getItem(position).isExclusiveCheckable()) {
                    convertView = MenuPopupHelper.this.mInflater.inflate(17367338, parent, false);
                } else {
                    convertView = MenuPopupHelper.this.mInflater.inflate(17367339, parent, false);
                }
            }
            ItemView itemView = (ItemView) convertView;
            if (MenuPopupHelper.this.mForceShowIcon) {
                ((ListMenuItemView) convertView).setForceShowIcon(true);
            }
            itemView.initialize(getItem(position), 0);
            return convertView;
        }

        void findExpandedIndex() {
            MenuItemImpl expandedItem = MenuPopupHelper.this.mMenu.getExpandedItem();
            if (expandedItem != null) {
                ArrayList<MenuItemImpl> items = MenuPopupHelper.this.mMenu.getNonActionItems();
                int count = items.size();
                for (int i = 0; i < count; i++) {
                    if (((MenuItemImpl) items.get(i)) == expandedItem) {
                        this.mExpandedIndex = i;
                        return;
                    }
                }
            }
            this.mExpandedIndex = -1;
        }

        public void notifyDataSetChanged() {
            findExpandedIndex();
            super.notifyDataSetChanged();
        }
    }

    public MenuPopupHelper(Context context, MenuBuilder menu) {
        this(context, menu, null, false, R.attr.popupMenuStyle, 0);
    }

    public MenuPopupHelper(Context context, MenuBuilder menu, View anchorView) {
        this(context, menu, anchorView, false, R.attr.popupMenuStyle, 0);
    }

    public MenuPopupHelper(Context context, MenuBuilder menu, View anchorView, boolean overflowOnly, int popupStyleAttr) {
        this(context, menu, anchorView, overflowOnly, popupStyleAttr, 0);
    }

    public MenuPopupHelper(Context context, MenuBuilder menu, View anchorView, boolean overflowOnly, int popupStyleAttr, int popupStyleRes) {
        boolean z;
        boolean z2 = true;
        this.mDropDownGravity = 0;
        this.mPositionAnchorView = null;
        this.mPopupWindowLayout = 0;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mMenu = menu;
        this.mAdapter = new MenuAdapter(this.mMenu);
        this.mOverflowOnly = overflowOnly;
        this.mPopupStyleAttr = popupStyleAttr;
        this.mPopupStyleRes = popupStyleRes;
        TypedValue outValue = new TypedValue();
        TypedValue colorValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, false);
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefaultDark, colorValue, true);
        if (outValue.data != 0) {
            z = true;
        } else {
            z = false;
        }
        this.mIsParentThemeDeviceDefault = z;
        if (outValue.data == 0 || colorValue.data != 0) {
            z2 = false;
        }
        this.mIsParentThemeDeviceDefaultLight = z2;
        Resources res = context.getResources();
        this.mPopupMaxWidth = Math.max(res.getDisplayMetrics().widthPixels / 2, res.getDimensionPixelSize(R.dimen.config_prefDialogWidth));
        this.mAnchorView = anchorView;
        menu.addMenuPresenter(this, context);
    }

    public void setAnchorView(View anchor) {
        this.mAnchorView = anchor;
    }

    public void setForceShowIcon(boolean forceShow) {
        this.mForceShowIcon = forceShow;
    }

    public void setGravity(int gravity) {
        this.mDropDownGravity = gravity;
    }

    public int getGravity() {
        return this.mDropDownGravity;
    }

    public void show() {
        if (!tryShow()) {
            throw new IllegalStateException("MenuPopupHelper cannot be used without an anchor");
        }
    }

    public ListPopupWindow getPopup() {
        return this.mPopup;
    }

    public boolean tryShow() {
        boolean addGlobalListener = false;
        if (isShowing()) {
            return true;
        }
        this.mPopup = new ListPopupWindow(this.mContext, null, this.mPopupStyleAttr, this.mPopupStyleRes);
        this.mPopup.setOnDismissListener(this);
        this.mPopup.setOnItemClickListener(this);
        this.mPopup.setAdapter(this.mAdapter);
        this.mPopup.setModal(true);
        View anchor = this.mAnchorView;
        if (anchor == null) {
            return false;
        }
        if (this.mTreeObserver == null) {
            addGlobalListener = true;
        }
        this.mTreeObserver = anchor.getViewTreeObserver();
        if (addGlobalListener) {
            this.mTreeObserver.addOnGlobalLayoutListener(this);
        }
        anchor.addOnAttachStateChangeListener(this);
        this.mPopup.setAnchorView(anchor);
        this.mPopup.setDropDownGravity(this.mDropDownGravity);
        if (this.mContext.getApplicationInfo().hasRtlSupport() && !anchor.isLayoutDirectionResolved()) {
            anchor.resolveLayoutDirection();
        }
        if (!this.mHasContentWidth) {
            this.mContentWidth = measureContentWidth();
            this.mHasContentWidth = true;
        }
        if (twIsPopupMenuDisplay()) {
            int mItemMaxCount;
            int mVisibleMaxHeight;
            boolean isMobileKeyboard = false;
            if (this.mContext.getResources().getConfiguration().mobileKeyboardCovered == 1) {
                isMobileKeyboard = true;
            }
            if (isMobileKeyboard) {
                mItemMaxCount = this.mContext.getResources().getInteger(R.integer.tw_menu_popup_mobile_keyboard_max_item_count);
                mVisibleMaxHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_menu_popup_mobile_keyboard_max_height);
            } else {
                mItemMaxCount = this.mContext.getResources().getInteger(R.integer.tw_menu_popup_max_item_count);
                mVisibleMaxHeight = this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_menu_popup_max_height);
            }
            if (this.mAdapter.getCount() > mItemMaxCount) {
                this.mPopup.setHeight(mVisibleMaxHeight);
            }
        }
        this.mPopup.setContentWidth(this.mContentWidth);
        this.mPopup.setInputMethodMode(2);
        if (this.mPopupWindowLayout != 0) {
            this.mPopup.setWindowLayoutType(this.mPopupWindowLayout);
        }
        this.mPopup.show();
        this.mPopup.getListView().setOnKeyListener(this);
        return true;
    }

    private int getScreenWidth() {
        if (this.mContext == null) {
            return 0;
        }
        return this.mContext.getResources().getDisplayMetrics().widthPixels;
    }

    private boolean isSupportOverlapSubMenu() {
        return GeneralUtil.isTablet() && this.mIsParentThemeDeviceDefaultLight;
    }

    public boolean twTryShowAtLocation(View view) {
        if (view == null) {
            return tryShow();
        }
        int positionX;
        int positionY;
        if (this.mPopup == null) {
            this.mPopup = new ListPopupWindow(this.mContext, null, this.mPopupStyleAttr);
            this.mPopup.setOnDismissListener(this);
            this.mPopup.setOnItemClickListener(this);
            this.mPopup.setAdapter(this.mAdapter);
            this.mPopup.setModal(true);
            View anchor = this.mAnchorView;
            if (anchor == null) {
                return false;
            }
            boolean addGlobalListener = this.mTreeObserver == null;
            this.mTreeObserver = anchor.getViewTreeObserver();
            if (addGlobalListener) {
                this.mTreeObserver.addOnGlobalLayoutListener(this);
            }
            anchor.addOnAttachStateChangeListener(this);
            this.mPopup.setAnchorView(view);
            this.mPopup.setDropDownGravity(8388661);
            if (this.mContext.getApplicationInfo().hasRtlSupport() && !anchor.isLayoutDirectionResolved()) {
                anchor.resolveLayoutDirection();
            }
            if (!this.mHasContentWidth) {
                this.mContentWidth = measureContentWidth();
                this.mHasContentWidth = true;
            }
            if (twIsPopupMenuDisplay() && this.mAdapter.getCount() > this.mContext.getResources().getInteger(R.integer.tw_menu_popup_max_item_count)) {
                this.mPopup.setHeight(this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_menu_popup_max_height));
            }
            this.mPopup.setContentWidth(this.mContentWidth);
            this.mPopup.setInputMethodMode(2);
            if (this.mPopupWindowLayout != 0) {
                this.mPopup.setWindowLayoutType(this.mPopupWindowLayout);
            }
        }
        Resources res = this.mContext.getResources();
        int hOffset = res.getDimensionPixelSize(R.dimen.tw_sub_menu_popup_horizontal_offset);
        int anchorVerticalOff = res.getDimensionPixelSize(R.dimen.tw_menu_popup_vertical_offset);
        int[] locationInWindow = new int[2];
        view.getLocationInWindow(locationInWindow);
        int[] locationOnScreen = new int[2];
        view.getLocationOnScreen(locationOnScreen);
        if (this.mContext.getApplicationInfo().hasRtlSupport() && this.mAnchorView.getLayoutDirection() == 1) {
            positionX = ((getScreenWidth() - locationInWindow[0]) - this.mContentWidth) - hOffset;
        } else {
            positionX = locationInWindow[0] + hOffset;
        }
        if (isSplitTypeMultiWindow()) {
            int[] rootLocation = new int[2];
            this.mAnchorView.getRootView().getLocationOnScreen(rootLocation);
            positionY = locationOnScreen[1] - rootLocation[1];
        } else {
            positionY = locationOnScreen[1];
        }
        this.mPopup.showAtLocation(this.mAnchorView.getWindowToken(), positionX, positionY, 8388661);
        this.mPositionAnchorView = view;
        this.mPopup.getListView().setOnKeyListener(this);
        return true;
    }

    private boolean isSplitTypeMultiWindow() {
        if (this.mContext != null) {
            MultiWindowStyle mwStyle = this.mContext.getAppMultiWindowStyle();
            if (mwStyle != null && mwStyle.isSplit()) {
                return true;
            }
        }
        return false;
    }

    private int getSubPopupGravity() {
        return 0;
    }

    private boolean twIsPopupMenuDisplay() {
        if (this.mIsParentThemeDeviceDefault) {
            return true;
        }
        return false;
    }

    public void dismiss() {
        if (isShowing()) {
            this.mPopup.dismiss();
        }
    }

    public void onDismiss() {
        this.mPopup = null;
        if (isSupportOverlapSubMenu()) {
            this.mMenu.close(false);
        } else {
            this.mMenu.close();
        }
        if (this.mTreeObserver != null) {
            if (!this.mTreeObserver.isAlive()) {
                this.mTreeObserver = this.mAnchorView.getViewTreeObserver();
            }
            this.mTreeObserver.removeGlobalOnLayoutListener(this);
            this.mTreeObserver = null;
        }
        this.mAnchorView.removeOnAttachStateChangeListener(this);
    }

    public boolean isShowing() {
        return this.mPopup != null && this.mPopup.isShowing();
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        MenuAdapter adapter = this.mAdapter;
        if (isSupportOverlapSubMenu()) {
            adapter.mAdapterMenu.twPerformItemAction(view, adapter.getItem(position), 0);
        } else {
            adapter.mAdapterMenu.performItemAction(adapter.getItem(position), 0);
        }
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() != 1 || keyCode != 82) {
            return false;
        }
        dismiss();
        return true;
    }

    private int measureContentWidth() {
        int maxWidth = 0;
        View itemView = null;
        int itemType = 0;
        ListAdapter adapter = this.mAdapter;
        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        int heightMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            int positionType = adapter.getItemViewType(i);
            if (positionType != itemType) {
                itemType = positionType;
                itemView = null;
            }
            if (this.mMeasureParent == null) {
                this.mMeasureParent = new FrameLayout(this.mContext);
            }
            itemView = adapter.getView(i, itemView, this.mMeasureParent);
            itemView.measure(widthMeasureSpec, heightMeasureSpec);
            int itemWidth = itemView.getMeasuredWidth();
            if (itemWidth >= this.mPopupMaxWidth) {
                return this.mPopupMaxWidth;
            }
            if (itemWidth > maxWidth) {
                maxWidth = itemWidth;
            }
        }
        return maxWidth;
    }

    public void onGlobalLayout() {
        if (isShowing()) {
            View anchor = this.mAnchorView;
            if (anchor == null || !anchor.isShown()) {
                dismiss();
            } else if (!isShowing()) {
            } else {
                if (!this.mIsParentThemeDeviceDefaultLight || this.mPositionAnchorView == null) {
                    this.mPopup.show();
                } else {
                    twTryShowAtLocation(this.mPositionAnchorView);
                }
            }
        }
    }

    public void onViewAttachedToWindow(View v) {
    }

    public void onViewDetachedFromWindow(View v) {
        if (this.mTreeObserver != null) {
            if (!this.mTreeObserver.isAlive()) {
                this.mTreeObserver = v.getViewTreeObserver();
            }
            this.mTreeObserver.removeGlobalOnLayoutListener(this);
        }
        v.removeOnAttachStateChangeListener(this);
    }

    public void initForMenu(Context context, MenuBuilder menu) {
    }

    public MenuView getMenuView(ViewGroup root) {
        throw new UnsupportedOperationException("MenuPopupHelpers manage their own views");
    }

    public void updateMenuView(boolean cleared) {
        this.mHasContentWidth = false;
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void setCallback(Callback cb) {
        this.mPresenterCallback = cb;
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
        if (subMenu.hasVisibleItems()) {
            MenuPopupHelper subPopup = new MenuPopupHelper(this.mContext, subMenu, this.mAnchorView);
            subPopup.setCallback(this.mPresenterCallback);
            boolean preserveIconSpacing = false;
            int count = subMenu.size();
            for (int i = 0; i < count; i++) {
                MenuItem childItem = subMenu.getItem(i);
                if (childItem.isVisible() && childItem.getIcon() != null) {
                    preserveIconSpacing = true;
                    break;
                }
            }
            subPopup.setForceShowIcon(preserveIconSpacing);
            if (!subPopup.tryShow()) {
                if (this.mPresenterCallback != null) {
                    this.mPresenterCallback.onOpenSubMenu(subMenu);
                }
                return true;
            }
        }
        return false;
    }

    public boolean twOnSubMenuSelected(SubMenuBuilder subMenu, View view) {
        if (!isSupportOverlapSubMenu()) {
            return onSubMenuSelected(subMenu);
        }
        if (subMenu.hasVisibleItems()) {
            MenuPopupHelper subPopup = new MenuPopupHelper(this.mContext, subMenu, this.mAnchorView);
            subPopup.setCallback(this.mPresenterCallback);
            boolean preserveIconSpacing = false;
            int count = subMenu.size();
            for (int i = 0; i < count; i++) {
                MenuItem childItem = subMenu.getItem(i);
                if (childItem.isVisible() && childItem.getIcon() != null) {
                    preserveIconSpacing = true;
                    break;
                }
            }
            subPopup.setForceShowIcon(preserveIconSpacing);
            if (!subPopup.twTryShowAtLocation(view)) {
                if (this.mPresenterCallback != null) {
                    this.mPresenterCallback.onOpenSubMenu(subMenu);
                }
                return true;
            }
        }
        return false;
    }

    public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        if (menu == this.mMenu) {
            dismiss();
            if (this.mPresenterCallback != null) {
                this.mPresenterCallback.onCloseMenu(menu, allMenusAreClosing);
            }
        }
    }

    public boolean flagActionItems() {
        return false;
    }

    public boolean expandItemActionView(MenuBuilder menu, MenuItemImpl item) {
        return false;
    }

    public boolean collapseItemActionView(MenuBuilder menu, MenuItemImpl item) {
        return false;
    }

    public int getId() {
        return 0;
    }

    public Parcelable onSaveInstanceState() {
        return null;
    }

    public void onRestoreInstanceState(Parcelable state) {
    }
}
