package com.android.internal.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.SystemProperties;
import android.text.TextUtils;
import android.util.Size;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.InternalInsetsInfo;
import android.view.ViewTreeObserver.OnComputeInternalInsetsListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.Transformation;
import android.view.animation.interpolator.SineInOut33;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class FloatingToolbar {
    private static final OnMenuItemClickListener NO_OP_MENUITEM_CLICK_LISTENER = new OnMenuItemClickListener() {
        public boolean onMenuItemClick(MenuItem item) {
            return false;
        }
    };
    private final Rect mContentRect;
    private final Context mContext;
    private Menu mMenu;
    private OnMenuItemClickListener mMenuItemClickListener;
    private final ComponentCallbacks mOrientationChangeHandler;
    private final FloatingToolbarPopup mPopup;
    private final Rect mPreviousContentRect;
    private List<Object> mShowingMenuItems;
    private int mSuggestedWidth;
    private boolean mUseNewSamsungToolbar;
    private boolean mUseSamsungToolbar;
    private boolean mWidthChanged;

    private static final class FloatingToolbarMainPanel {
        private int MAX_ITEMS_LANDSCAPE_LARGE_PHONE;
        private int MAX_ITEMS_LANDSCAPE_PHONE;
        private int MAX_ITEMS_LANDSCAPE_TABLET;
        private int MAX_ITEMS_PORTRAIT_LARGE_PHONE;
        private int MAX_ITEMS_PORTRAIT_PHONE;
        private int MAX_ITEMS_PORTRAIT_TABLET;
        private int MAX_ITEMS_VERTICAL;
        private boolean isClickedOption;
        ImageView mCloseButton;
        private final ViewGroup mContentView;
        private final Context mContext;
        private Drawable mDivider;
        private int mDividerPadding;
        private int mDividerWidth;
        private int mExtraItemWidth;
        private HorizontalScrollView mHorizontalScrollView;
        private boolean mIsTablet;
        private int mItemHeight;
        private int mItemWidth;
        private LinkedList<LinearLayout> mLinearLayoutArray;
        private final OnClickListener mMenuItemButtonOnClickListener;
        private OnMenuItemClickListener mOnMenuItemClickListener;
        TextView mOpenButton;
        private final Runnable mOpenOverflow;
        private View mOpenOverflowButton;
        int mOpenedPopupHeight;
        private int mOptionItemWidth;
        private int mPopupHeight;
        private int mPopupMaxHeight;
        private int mPopupMaxWidth;
        private int mPopupWidth;
        private ScrollView mScrollView;
        private ViewGroup mTwContentView;
        private boolean mUseNewSamsungToolbar;
        private boolean mUseSamsungToolbar;
        LinearLayout mVerticalLinearLayout;
        private final ViewFader viewFader;

        public FloatingToolbarMainPanel(Context context, Runnable openOverflow) {
            this(context, openOverflow, false);
        }

        public FloatingToolbarMainPanel(Context context, Runnable openOverflow, boolean useSamsungToolbar) {
            this.mLinearLayoutArray = new LinkedList();
            this.mMenuItemButtonOnClickListener = new OnClickListener() {
                public void onClick(View v) {
                    if ((v.getTag() instanceof MenuItem) && FloatingToolbarMainPanel.this.mOnMenuItemClickListener != null) {
                        FloatingToolbarMainPanel.this.mOnMenuItemClickListener.onMenuItemClick((MenuItem) v.getTag());
                    }
                }
            };
            this.mUseSamsungToolbar = false;
            this.mUseNewSamsungToolbar = false;
            this.mIsTablet = false;
            this.isClickedOption = true;
            this.mContext = (Context) Preconditions.checkNotNull(context);
            this.mContentView = new LinearLayout(context);
            this.viewFader = new ViewFader(this.mContentView);
            this.mOpenOverflow = (Runnable) Preconditions.checkNotNull(openOverflow);
            this.mUseSamsungToolbar = useSamsungToolbar;
            this.mUseNewSamsungToolbar = this.mContentView.getResources().getBoolean(R.bool.tw_edit_text_new_concept);
            if (this.mUseSamsungToolbar) {
                this.mIsTablet = isTablet();
                this.MAX_ITEMS_PORTRAIT_PHONE = this.mContentView.getResources().getInteger(R.integer.tw_text_edit_action_popup_max_items_portrait_phone);
                this.MAX_ITEMS_LANDSCAPE_PHONE = this.mContentView.getResources().getInteger(R.integer.tw_text_edit_action_popup_max_items_landscape_phone);
                this.MAX_ITEMS_PORTRAIT_TABLET = this.mContentView.getResources().getInteger(R.integer.tw_text_edit_action_popup_max_items_portrait_tablet);
                this.MAX_ITEMS_LANDSCAPE_TABLET = this.mContentView.getResources().getInteger(R.integer.tw_text_edit_action_popup_max_items_landscape_tablet);
                this.MAX_ITEMS_VERTICAL = this.mContentView.getResources().getInteger(R.integer.tw_text_edit_action_popup_max_items_vertical);
                this.mItemHeight = (int) this.mContext.getResources().getDimension(R.dimen.tw_text_edit_action_popup_item_height);
                this.mItemWidth = (int) this.mContext.getResources().getDimension(R.dimen.tw_text_edit_action_popup_item_width);
                this.mExtraItemWidth = (int) this.mContext.getResources().getDimension(R.dimen.tw_text_edit_action_popup_extra_item_width);
                this.mOptionItemWidth = (int) this.mContext.getResources().getDimension(R.dimen.tw_text_edit_action_popup_option_item_width);
                this.mDivider = this.mContext.getResources().getDrawable(R.drawable.tw_text_edit_action_popup_divider_mtrl);
                this.mDividerWidth = this.mDivider.getIntrinsicWidth();
                this.mDividerPadding = (int) this.mContext.getResources().getDimension(R.dimen.tw_text_edit_action_popup_divider_top_bottom_margin);
                this.mPopupMaxHeight = getPopupWidthLimit();
                LinearLayout linearLayout = new LinearLayout(context);
                linearLayout.setOrientation(0);
                linearLayout.setGravity(17);
                this.mTwContentView = linearLayout;
                this.mTwContentView.setLayoutParams(new LayoutParams(-2, -2));
                if (this.mUseNewSamsungToolbar) {
                    this.mScrollView = new ScrollView(this.mContext);
                    this.mScrollView.setLayoutParams(new LayoutParams(-2, -2));
                    this.mVerticalLinearLayout = new LinearLayout(this.mContext);
                    this.mVerticalLinearLayout.setOrientation(1);
                    this.mVerticalLinearLayout.setLayoutParams(new LayoutParams(-2, -2));
                    this.mVerticalLinearLayout.setVerticalScrollBarEnabled(false);
                    return;
                }
                ((LinearLayout) this.mTwContentView).setShowDividers(2);
                ((LinearLayout) this.mTwContentView).setDividerDrawable(this.mDivider);
                this.mHorizontalScrollView = new HorizontalScrollView(this.mContext);
                this.mHorizontalScrollView.setLayoutParams(new LayoutParams(-2, -2));
                this.mHorizontalScrollView.setHorizontalScrollBarEnabled(false);
            }
        }

        public List<MenuItem> layoutMenuItems(List<MenuItem> menuItems, int width) {
            return layoutMenuItems(menuItems, width, false);
        }

        public List<MenuItem> layoutMenuItems(List<MenuItem> menuItems, int width, boolean useSamsungToolbar) {
            Preconditions.checkNotNull(menuItems);
            int toolbarWidth = width - FloatingToolbar.getEstimatedOpenOverflowButtonWidth(this.mContext);
            int availableWidth = toolbarWidth;
            LinkedList<MenuItem> linkedList = new LinkedList(menuItems);
            this.mContentView.removeAllViews();
            if (useSamsungToolbar) {
                this.mTwContentView.removeAllViews();
                if (this.mUseNewSamsungToolbar) {
                    this.mLinearLayoutArray.clear();
                    this.mVerticalLinearLayout.removeAllViews();
                    this.mScrollView.removeAllViews();
                    this.mLinearLayoutArray.add((LinearLayout) this.mTwContentView);
                } else {
                    this.mHorizontalScrollView.removeAllViews();
                }
            }
            boolean isFirstItem = true;
            int mMenuSize = linkedList.size();
            int mMenuNum = 0;
            while (!linkedList.isEmpty()) {
                LayoutParams params;
                MenuItem menuItem = (MenuItem) linkedList.peek();
                if (isFirstItem && useSamsungToolbar && this.mUseNewSamsungToolbar) {
                    CharSequence moreString = this.mContentView.getContext().getString(R.string.more);
                    String closeString = this.mContentView.getContext().getString(R.string.close);
                    this.isClickedOption = true;
                    this.mOpenButton = (TextView) LayoutInflater.from(this.mContext).inflate((int) R.layout.tw_text_edit_action_popup_text, null);
                    this.mOpenButton.setText(moreString);
                    this.mOpenButton.setContentDescription(moreString);
                    this.mOpenButton.setCompoundDrawablesWithIntrinsicBounds(null, this.mContext.getResources().getDrawable(R.drawable.tw_text_edit_action_popup_more_mtrl), null, null);
                    this.mOpenButton.setHoverPopupType(0);
                    this.mOpenButton.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            if (FloatingToolbarMainPanel.this.isClickedOption) {
                                FloatingToolbarMainPanel.this.isClickedOption = false;
                                FloatingToolbarMainPanel.this.mOpenOverflow.run();
                            }
                        }
                    });
                    this.mOpenButton.setVisibility(8);
                    this.mCloseButton = (ImageView) LayoutInflater.from(this.mContext).inflate((int) R.layout.tw_text_edit_action_popup_option, null);
                    this.mCloseButton.setContentDescription(closeString);
                    this.mCloseButton.setHoverPopupType(0);
                    this.mCloseButton.setImageDrawable(menuItem.getIcon());
                    this.mCloseButton.setScaleType(ScaleType.CENTER);
                    this.mCloseButton.setAlpha(0.0f);
                    this.mCloseButton.setVisibility(8);
                    this.mCloseButton.setTag(menuItem);
                    this.mCloseButton.setOnClickListener(this.mMenuItemButtonOnClickListener);
                    linkedList.pop();
                    isFirstItem = false;
                } else {
                    View menuItemButton = FloatingToolbar.createMenuItemButton(this.mContext, menuItem, useSamsungToolbar);
                    if (useSamsungToolbar) {
                        if (this.mUseNewSamsungToolbar) {
                            mMenuNum++;
                            if (mMenuSize - 1 > getMenuLimit() && mMenuNum / (getMenuLimit() - 1) != 0 && mMenuNum % (getMenuLimit() - 1) == 1) {
                                this.mLinearLayoutArray.add(new LinearLayout(this.mContext));
                                int mCurLayoutIndex = this.mLinearLayoutArray.size() - 1;
                                ((LinearLayout) this.mLinearLayoutArray.get(mCurLayoutIndex)).setOrientation(0);
                                ((LinearLayout) this.mLinearLayoutArray.get(mCurLayoutIndex)).setLayoutParams(new LayoutParams(-2, -2));
                                this.mTwContentView = (ViewGroup) this.mLinearLayoutArray.get(mCurLayoutIndex);
                            }
                        }
                        setButtonTagAndClickListener(menuItemButton, menuItem);
                        this.mTwContentView.addView(menuItemButton);
                        params = menuItemButton.getLayoutParams();
                        params.width = this.mItemWidth;
                        params.height = this.mItemHeight;
                        menuItemButton.setLayoutParams(params);
                        linkedList.pop();
                    } else {
                        if (isFirstItem) {
                            menuItemButton.setPaddingRelative((int) (1.5d * ((double) menuItemButton.getPaddingStart())), menuItemButton.getPaddingTop(), menuItemButton.getPaddingEnd(), menuItemButton.getPaddingBottom());
                            isFirstItem = false;
                        }
                        if (linkedList.size() == 1) {
                            menuItemButton.setPaddingRelative(menuItemButton.getPaddingStart(), menuItemButton.getPaddingTop(), (int) (1.5d * ((double) menuItemButton.getPaddingEnd())), menuItemButton.getPaddingBottom());
                        }
                        menuItemButton.measure(0, 0);
                        int menuItemButtonWidth = Math.min(menuItemButton.getMeasuredWidth(), toolbarWidth);
                        if (menuItemButtonWidth <= availableWidth) {
                            setButtonTagAndClickListener(menuItemButton, menuItem);
                            this.mContentView.addView(menuItemButton);
                            params = menuItemButton.getLayoutParams();
                            params.width = menuItemButtonWidth;
                            menuItemButton.setLayoutParams(params);
                            availableWidth -= menuItemButtonWidth;
                            linkedList.pop();
                        } else {
                            if (this.mOpenOverflowButton == null) {
                                this.mOpenOverflowButton = LayoutInflater.from(this.mContext).inflate((int) R.layout.floating_popup_open_overflow_button, null);
                                this.mOpenOverflowButton.setOnClickListener(new OnClickListener() {
                                    public void onClick(View v) {
                                        if (FloatingToolbarMainPanel.this.mOpenOverflowButton != null) {
                                            FloatingToolbarMainPanel.this.mOpenOverflow.run();
                                        }
                                    }
                                });
                                this.mOpenOverflowButton.setHoverPopupType(0);
                            }
                            this.mContentView.addView(this.mOpenOverflowButton);
                            if (useSamsungToolbar) {
                                if (this.mUseNewSamsungToolbar) {
                                    this.mHorizontalScrollView.addView(this.mTwContentView);
                                    this.mContentView.addView(this.mHorizontalScrollView);
                                } else {
                                    this.mVerticalLinearLayout.addView((View) this.mLinearLayoutArray.get(0));
                                    this.mScrollView.addView(this.mVerticalLinearLayout);
                                    this.mContentView.addView(this.mScrollView);
                                    LayoutParams mScrollViewparams = this.mScrollView.getLayoutParams();
                                    mScrollViewparams.height = this.mItemHeight;
                                    this.mScrollView.setLayoutParams(mScrollViewparams);
                                    if (!(mMenuSize - 1 <= getMenuLimit() || this.mOpenButton == null || this.mCloseButton == null)) {
                                        this.mOpenButton.setVisibility(0);
                                        ((LinearLayout) this.mContentView).setShowDividers(2);
                                        ((LinearLayout) this.mContentView).setDividerDrawable(this.mDivider);
                                        ((LinearLayout) this.mContentView).setDividerPadding(this.mDividerPadding);
                                        this.mContentView.addView(this.mOpenButton);
                                        this.mContentView.addView(this.mCloseButton);
                                        params = this.mOpenButton.getLayoutParams();
                                        params.width = this.mOptionItemWidth;
                                        params.height = -1;
                                        this.mOpenButton.setLayoutParams(params);
                                        this.mCloseButton.setLayoutParams(params);
                                    }
                                }
                            }
                            return linkedList;
                        }
                    }
                }
            }
            if (useSamsungToolbar) {
                if (this.mUseNewSamsungToolbar) {
                    this.mHorizontalScrollView.addView(this.mTwContentView);
                    this.mContentView.addView(this.mHorizontalScrollView);
                } else {
                    this.mVerticalLinearLayout.addView((View) this.mLinearLayoutArray.get(0));
                    this.mScrollView.addView(this.mVerticalLinearLayout);
                    this.mContentView.addView(this.mScrollView);
                    LayoutParams mScrollViewparams2 = this.mScrollView.getLayoutParams();
                    mScrollViewparams2.height = this.mItemHeight;
                    this.mScrollView.setLayoutParams(mScrollViewparams2);
                    this.mOpenButton.setVisibility(0);
                    ((LinearLayout) this.mContentView).setShowDividers(2);
                    ((LinearLayout) this.mContentView).setDividerDrawable(this.mDivider);
                    ((LinearLayout) this.mContentView).setDividerPadding(this.mDividerPadding);
                    this.mContentView.addView(this.mOpenButton);
                    this.mContentView.addView(this.mCloseButton);
                    params = this.mOpenButton.getLayoutParams();
                    params.width = this.mOptionItemWidth;
                    params.height = -1;
                    this.mOpenButton.setLayoutParams(params);
                    this.mCloseButton.setLayoutParams(params);
                }
            }
            return linkedList;
        }

        public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
            this.mOnMenuItemClickListener = listener;
        }

        public View getView() {
            return this.mContentView;
        }

        public void fadeIn(boolean animate) {
            this.viewFader.fadeIn(animate);
        }

        public void fadeOut(boolean animate) {
            this.viewFader.fadeOut(animate);
        }

        private int getMenuLimit() {
            boolean isPortrait = true;
            if (this.mContext.getResources().getConfiguration().orientation != 1) {
                isPortrait = false;
            }
            return this.mIsTablet ? isPortrait ? this.MAX_ITEMS_PORTRAIT_TABLET : this.MAX_ITEMS_LANDSCAPE_TABLET : isPortrait ? this.MAX_ITEMS_PORTRAIT_PHONE : this.MAX_ITEMS_LANDSCAPE_PHONE;
        }

        private boolean isTablet() {
            if ("short".equals(SystemProperties.get("ro.build.scafe.size"))) {
                return false;
            }
            return true;
        }

        private int getMenuLimitVertical() {
            return this.MAX_ITEMS_VERTICAL;
        }

        private int getPopupWidthLimit() {
            int items = getMenuLimit();
            this.mPopupMaxWidth = (this.mItemWidth * items) + (this.mDividerWidth * items);
            return this.mPopupMaxWidth;
        }

        public Size measure() throws IllegalStateException {
            Preconditions.checkState(this.mContentView.getParent() == null);
            this.mContentView.measure(0, 0);
            if (this.mUseSamsungToolbar) {
                return new Size(Math.min(this.mContentView.getMeasuredWidth(), getPopupWidthLimit() + this.mExtraItemWidth), this.mContentView.getMeasuredHeight());
            }
            return new Size(this.mContentView.getMeasuredWidth(), this.mContentView.getMeasuredHeight());
        }

        public Size measureSamsung() throws IllegalStateException {
            this.mContentView.measure(0, 0);
            if (this.mUseSamsungToolbar) {
                return new Size(Math.min(this.mContentView.getMeasuredWidth(), getPopupWidthLimit() + this.mExtraItemWidth), this.mContentView.getMeasuredHeight());
            }
            return new Size(this.mContentView.getMeasuredWidth(), this.mContentView.getMeasuredHeight());
        }

        private void setButtonTagAndClickListener(View menuItemButton, MenuItem menuItem) {
            View button = menuItemButton;
            if (FloatingToolbar.isIconOnlyMenuItem(menuItem)) {
                button = menuItemButton.findViewById(R.id.floating_toolbar_menu_item_image_button);
            }
            button.setTag(menuItem);
            button.setOnClickListener(this.mMenuItemButtonOnClickListener);
        }

        public void setAllMenuInNewPopup() {
            int i;
            this.mVerticalLinearLayout.removeAllViews();
            for (i = 0; i < this.mLinearLayoutArray.size(); i++) {
                this.mVerticalLinearLayout.addView((View) this.mLinearLayoutArray.get(i));
            }
            for (i = 1; i < this.mVerticalLinearLayout.getChildCount(); i++) {
                this.mVerticalLinearLayout.getChildAt(i).setAlpha(0.0f);
            }
        }

        public int getNeedHeightNewPopup() {
            return Math.min(getPopupMaxHeight(), this.mItemHeight * this.mLinearLayoutArray.size());
        }

        public void setHeightNewPopup() {
            int height = this.mOpenedPopupHeight;
            LayoutParams params = this.mScrollView.getLayoutParams();
            params.height = height;
            this.mScrollView.setLayoutParams(params);
        }

        public int getPopupMaxHeight() {
            this.mPopupMaxHeight = this.mItemHeight * getMenuLimitVertical();
            return this.mPopupMaxHeight;
        }
    }

    private static final class FloatingToolbarOverflowPanel {
        private final View mBackButton;
        private final ViewGroup mBackButtonContainer;
        private final Runnable mCloseOverflow;
        private final LinearLayout mContentView;
        private final ListView mListView;
        private final TextView mListViewItemWidthCalculator;
        private OnMenuItemClickListener mOnMenuItemClickListener;
        private int mOverflowWidth;
        private int mSuggestedHeight;
        private final ViewFader mViewFader = new ViewFader(this.mContentView);

        public FloatingToolbarOverflowPanel(Context context, Runnable closeOverflow) {
            this.mCloseOverflow = (Runnable) Preconditions.checkNotNull(closeOverflow);
            this.mContentView = new LinearLayout(context);
            this.mContentView.setOrientation(1);
            this.mBackButton = LayoutInflater.from(context).inflate((int) R.layout.floating_popup_close_overflow_button, null);
            this.mBackButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    FloatingToolbarOverflowPanel.this.mCloseOverflow.run();
                }
            });
            this.mBackButton.setHoverPopupType(0);
            this.mBackButtonContainer = new LinearLayout(context);
            this.mBackButtonContainer.addView(this.mBackButton);
            this.mListView = createOverflowListView();
            this.mListView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    MenuItem menuItem = (MenuItem) FloatingToolbarOverflowPanel.this.mListView.getAdapter().getItem(position);
                    if (FloatingToolbarOverflowPanel.this.mOnMenuItemClickListener != null) {
                        FloatingToolbarOverflowPanel.this.mOnMenuItemClickListener.onMenuItemClick(menuItem);
                    }
                }
            });
            this.mContentView.addView(this.mListView);
            this.mContentView.addView(this.mBackButtonContainer);
            this.mListViewItemWidthCalculator = FloatingToolbar.createOverflowMenuItemButton(context);
            this.mListViewItemWidthCalculator.setLayoutParams(new LayoutParams(-1, -1));
        }

        public void setMenuItems(List<MenuItem> menuItems) {
            ArrayAdapter overflowListViewAdapter = (ArrayAdapter) this.mListView.getAdapter();
            overflowListViewAdapter.clear();
            overflowListViewAdapter.addAll((Collection) menuItems);
            setListViewHeight();
            setOverflowWidth();
        }

        public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
            this.mOnMenuItemClickListener = listener;
        }

        public void setOverflowDirection(int overflowDirection) {
            this.mContentView.removeView(this.mBackButtonContainer);
            this.mContentView.addView(this.mBackButtonContainer, overflowDirection == 0 ? 1 : 0);
        }

        public void setSuggestedHeight(int height) {
            this.mSuggestedHeight = height;
            setListViewHeight();
        }

        public int getMinimumHeight() {
            return this.mContentView.getContext().getResources().getDimensionPixelSize(R.dimen.floating_toolbar_minimum_overflow_height) + FloatingToolbar.getEstimatedToolbarHeight(this.mContentView.getContext());
        }

        public View getView() {
            return this.mContentView;
        }

        public void fadeIn(boolean animate) {
            this.mViewFader.fadeIn(animate);
        }

        public void fadeOut(boolean animate) {
            this.mViewFader.fadeOut(animate);
        }

        public Size measure() {
            boolean z;
            if (this.mContentView.getParent() == null) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkState(z);
            this.mContentView.measure(0, 0);
            return new Size(this.mContentView.getMeasuredWidth(), this.mContentView.getMeasuredHeight());
        }

        private void setListViewHeight() {
            int itemHeight = FloatingToolbar.getEstimatedToolbarHeight(this.mContentView.getContext());
            int height = this.mListView.getAdapter().getCount() * itemHeight;
            int maxHeight = this.mContentView.getContext().getResources().getDimensionPixelSize(R.dimen.floating_toolbar_maximum_overflow_height);
            int minHeight = this.mContentView.getContext().getResources().getDimensionPixelSize(R.dimen.floating_toolbar_minimum_overflow_height);
            int suggestedListViewHeight = (this.mSuggestedHeight - (this.mSuggestedHeight % itemHeight)) - itemHeight;
            LayoutParams params = this.mListView.getLayoutParams();
            if (suggestedListViewHeight <= 0) {
                params.height = Math.min(maxHeight, height);
            } else if (suggestedListViewHeight < minHeight) {
                params.height = minHeight;
            } else {
                params.height = Math.min(Math.min(suggestedListViewHeight, maxHeight), height);
            }
            this.mListView.setLayoutParams(params);
        }

        private void setOverflowWidth() {
            this.mOverflowWidth = 0;
            for (int i = 0; i < this.mListView.getAdapter().getCount(); i++) {
                MenuItem menuItem = (MenuItem) this.mListView.getAdapter().getItem(i);
                Preconditions.checkNotNull(menuItem);
                this.mListViewItemWidthCalculator.setText(menuItem.getTitle());
                this.mListViewItemWidthCalculator.measure(0, 0);
                this.mOverflowWidth = Math.max(this.mListViewItemWidthCalculator.getMeasuredWidth(), this.mOverflowWidth);
            }
        }

        private ListView createOverflowListView() {
            final Context context = this.mContentView.getContext();
            ListView overflowListView = new ListView(context);
            overflowListView.setLayoutParams(new LayoutParams(-1, -1));
            overflowListView.setDivider(null);
            overflowListView.setDividerHeight(0);
            overflowListView.setAdapter(new ArrayAdapter<MenuItem>(0, context) {
                public int getViewTypeCount() {
                    return 2;
                }

                public int getItemViewType(int position) {
                    if (FloatingToolbar.isIconOnlyMenuItem((MenuItem) getItem(position))) {
                        return 1;
                    }
                    return 0;
                }

                public View getView(int position, View convertView, ViewGroup parent) {
                    if (getItemViewType(position) == 1) {
                        return getIconOnlyView(position, convertView);
                    }
                    return getStringTitleView(position, convertView);
                }

                private View getStringTitleView(int position, View convertView) {
                    TextView menuButton;
                    if (convertView != null) {
                        menuButton = (TextView) convertView;
                    } else {
                        menuButton = FloatingToolbar.createOverflowMenuItemButton(context);
                    }
                    MenuItem menuItem = (MenuItem) getItem(position);
                    menuButton.setText(menuItem.getTitle());
                    menuButton.setContentDescription(menuItem.getTitle());
                    menuButton.setMinimumWidth(FloatingToolbarOverflowPanel.this.mOverflowWidth);
                    return menuButton;
                }

                private View getIconOnlyView(int position, View convertView) {
                    View menuButton;
                    if (convertView != null) {
                        menuButton = convertView;
                    } else {
                        menuButton = LayoutInflater.from(context).inflate((int) R.layout.floating_popup_overflow_image_list_item, null);
                    }
                    ((ImageView) menuButton.findViewById(R.id.floating_toolbar_menu_item_image_button)).setImageDrawable(((MenuItem) getItem(position)).getIcon());
                    menuButton.setMinimumWidth(FloatingToolbarOverflowPanel.this.mOverflowWidth);
                    return menuButton;
                }
            });
            return overflowListView;
        }
    }

    private static final class FloatingToolbarPopup {
        public static final int OVERFLOW_DIRECTION_DOWN = 1;
        public static final int OVERFLOW_DIRECTION_UP = 0;
        private final Runnable mCloseOverflow;
        private final AnimationSet mCloseOverflowAnimation;
        private AnimatorSet mCloseOverflowAnimatior;
        private final ViewGroup mContentContainer;
        private final Context mContext;
        private final Point mCoordsOnWindow;
        private final AnimatorSet mDismissAnimation;
        private boolean mDismissed;
        private boolean mHidden;
        private final AnimatorSet mHideAnimation;
        private final OnComputeInternalInsetsListener mInsetsComputer;
        private FloatingToolbarMainPanel mMainPanel;
        private final int mMarginHorizontal;
        private final int mMarginVertical;
        private final AnimationListener mOnOverflowClosed;
        private final AnimationListener mOnOverflowOpened;
        private final Runnable mOpenOverflow;
        private final AnimationSet mOpenOverflowAnimation;
        private AnimatorSet mOpenOverflowAnimatior1;
        private AnimatorSet mOpenOverflowAnimatior2;
        private final Runnable mOpenSamsungflow;
        private int mOrientation;
        private int mOverflowDirection;
        private FloatingToolbarOverflowPanel mOverflowPanel;
        private final View mParent;
        private int mPopupAboveMargin;
        private int mPopupBelowMargin;
        private int mPopupBgPaddingBottom;
        private int mPopupBgPaddingLeft;
        private int mPopupBgPaddingRight;
        private int mPopupBgPaddingTop;
        private int mPopupSideMargin;
        private final PopupWindow mPopupWindow;
        private final int[] mTmpCoords;
        private final Rect mTmpRect;
        private final Region mTouchableRegion;
        private boolean mUseNewSamsungToolbar;
        private boolean mUseSamsungToolbar;
        private final Rect mViewPortOnScreen;

        public FloatingToolbarPopup(Context context, View parent) {
            this(context, parent, false);
        }

        public FloatingToolbarPopup(Context context, View parent, boolean useSamsungToolbar) {
            this.mUseSamsungToolbar = false;
            this.mUseNewSamsungToolbar = false;
            this.mOnOverflowOpened = new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    FloatingToolbarPopup.this.setOverflowPanelAsContent();
                    FloatingToolbarPopup.this.mOverflowPanel.fadeIn(true);
                }

                public void onAnimationRepeat(Animation animation) {
                }
            };
            this.mOnOverflowClosed = new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    FloatingToolbarPopup.this.setMainPanelAsContent();
                    FloatingToolbarPopup.this.mMainPanel.fadeIn(true);
                }

                public void onAnimationRepeat(Animation animation) {
                }
            };
            this.mOpenOverflowAnimation = new AnimationSet(true);
            this.mCloseOverflowAnimation = new AnimationSet(true);
            this.mOpenOverflow = new Runnable() {
                public void run() {
                    FloatingToolbarPopup.this.openOverflow();
                }
            };
            this.mCloseOverflow = new Runnable() {
                public void run() {
                    FloatingToolbarPopup.this.closeOverflow();
                }
            };
            this.mOpenSamsungflow = new Runnable() {
                public void run() {
                    FloatingToolbarPopup.this.mMainPanel.setAllMenuInNewPopup();
                    FloatingToolbarPopup.this.mMainPanel.setHeightNewPopup();
                    FloatingToolbarPopup.this.openSamsungflow();
                }
            };
            this.mViewPortOnScreen = new Rect();
            this.mCoordsOnWindow = new Point();
            this.mTmpCoords = new int[2];
            this.mTmpRect = new Rect();
            this.mTouchableRegion = new Region();
            this.mInsetsComputer = new OnComputeInternalInsetsListener() {
                public void onComputeInternalInsets(InternalInsetsInfo info) {
                    info.contentInsets.setEmpty();
                    info.visibleInsets.setEmpty();
                    info.touchableRegion.set(FloatingToolbarPopup.this.mTouchableRegion);
                    info.setTouchableInsets(3);
                }
            };
            this.mDismissed = true;
            this.mParent = (View) Preconditions.checkNotNull(parent);
            this.mContext = (Context) Preconditions.checkNotNull(context);
            this.mContentContainer = FloatingToolbar.createContentContainer(context, useSamsungToolbar);
            this.mPopupWindow = FloatingToolbar.createPopupWindow(this.mContentContainer, useSamsungToolbar);
            this.mUseSamsungToolbar = useSamsungToolbar;
            this.mUseNewSamsungToolbar = parent.getResources().getBoolean(R.bool.tw_edit_text_new_concept);
            this.mDismissAnimation = FloatingToolbar.createExitAnimation(this.mContentContainer, 150, new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    FloatingToolbarPopup.this.mPopupWindow.dismiss();
                    FloatingToolbarPopup.this.mContentContainer.removeAllViews();
                }
            });
            this.mHideAnimation = FloatingToolbar.createExitAnimation(this.mContentContainer, 0, new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    FloatingToolbarPopup.this.mPopupWindow.dismiss();
                }
            });
            if (this.mUseSamsungToolbar) {
                this.mPopupWindow.setAnimationStyle(R.style.Animation_TextSelectActionPopup);
                Drawable bgRes = parent.getResources().getDrawable(R.drawable.tw_text_edit_action_popup_frame_mtrl);
                Rect bgResPadding = new Rect();
                if (bgResPadding != null) {
                    bgRes.getPadding(bgResPadding);
                    this.mPopupBgPaddingLeft = bgResPadding.left;
                    this.mPopupBgPaddingRight = bgResPadding.right;
                    this.mPopupBgPaddingTop = bgResPadding.top;
                    this.mPopupBgPaddingBottom = bgResPadding.bottom;
                } else {
                    this.mPopupBgPaddingLeft = 0;
                    this.mPopupBgPaddingRight = 0;
                    this.mPopupBgPaddingTop = 0;
                    this.mPopupBgPaddingBottom = 0;
                }
                this.mMarginHorizontal = 0;
                this.mMarginVertical = 0;
                this.mPopupAboveMargin = parent.getResources().getDimensionPixelSize(R.dimen.tw_text_edit_action_popup_top_margin);
                this.mPopupBelowMargin = parent.getResources().getDimensionPixelSize(R.dimen.tw_text_edit_action_popup_bottom_margin);
                this.mPopupSideMargin = parent.getResources().getDimensionPixelSize(R.dimen.tw_text_edit_action_popup_side_margin);
                return;
            }
            this.mMarginHorizontal = parent.getResources().getDimensionPixelSize(R.dimen.floating_toolbar_horizontal_margin);
            this.mMarginVertical = parent.getResources().getDimensionPixelSize(R.dimen.floating_toolbar_vertical_margin);
        }

        public void layoutMenuItems(List<MenuItem> menuItems, OnMenuItemClickListener menuItemClickListener, int suggestedWidth) {
            Preconditions.checkNotNull(menuItems);
            this.mContentContainer.removeAllViews();
            if (this.mMainPanel == null) {
                if (this.mUseSamsungToolbar) {
                    this.mMainPanel = new FloatingToolbarMainPanel(this.mContext, this.mOpenSamsungflow, this.mUseSamsungToolbar);
                } else {
                    this.mMainPanel = new FloatingToolbarMainPanel(this.mContext, this.mOpenOverflow);
                }
            }
            List<MenuItem> overflowMenuItems = this.mMainPanel.layoutMenuItems(menuItems, getToolbarWidth(suggestedWidth), this.mUseSamsungToolbar);
            this.mMainPanel.setOnMenuItemClickListener(menuItemClickListener);
            if (!overflowMenuItems.isEmpty()) {
                if (this.mOverflowPanel == null) {
                    this.mOverflowPanel = new FloatingToolbarOverflowPanel(this.mContext, this.mCloseOverflow);
                }
                this.mOverflowPanel.setMenuItems(overflowMenuItems);
                this.mOverflowPanel.setOnMenuItemClickListener(menuItemClickListener);
            }
            updatePopupSize();
        }

        public void show(Rect contentRectOnScreen) {
            Preconditions.checkNotNull(contentRectOnScreen);
            if (!isShowing()) {
                this.mHidden = false;
                this.mDismissed = false;
                cancelDismissAndHideAnimations();
                cancelOverflowAnimations();
                if (this.mContentContainer.getChildCount() == 0) {
                    setMainPanelAsContent();
                    if (this.mUseSamsungToolbar) {
                        this.mContentContainer.setBackgroundDrawable(this.mParent.getResources().getDrawable(R.drawable.tw_text_edit_action_popup_frame_mtrl));
                    } else {
                        this.mContentContainer.setAlpha(0.0f);
                    }
                }
                refreshCoordinatesAndOverflowDirection(contentRectOnScreen);
                preparePopupContent();
                this.mPopupWindow.showAtLocation(this.mParent, 0, this.mCoordsOnWindow.x, this.mCoordsOnWindow.y);
                setTouchableSurfaceInsetsComputer();
                runShowAnimation();
            }
        }

        public void dismiss() {
            if (!this.mDismissed) {
                this.mHidden = false;
                this.mDismissed = true;
                this.mHideAnimation.cancel();
                runDismissAnimation();
                setZeroTouchableSurface();
            }
        }

        public void hide() {
            if (isShowing()) {
                this.mHidden = true;
                runHideAnimation();
                setZeroTouchableSurface();
            }
        }

        public boolean isShowing() {
            return (this.mDismissed || this.mHidden) ? false : true;
        }

        public boolean isHidden() {
            return this.mHidden;
        }

        public void updateCoordinates(Rect contentRectOnScreen) {
            Preconditions.checkNotNull(contentRectOnScreen);
            if (isShowing() && this.mPopupWindow.isShowing()) {
                cancelOverflowAnimations();
                refreshCoordinatesAndOverflowDirection(contentRectOnScreen);
                preparePopupContent();
                this.mPopupWindow.update(this.mCoordsOnWindow.x, this.mCoordsOnWindow.y, getWidth(), getHeight());
            }
        }

        public int getWidth() {
            return this.mPopupWindow.getWidth();
        }

        public int getHeight() {
            return this.mPopupWindow.getHeight();
        }

        public Context getContext() {
            return this.mContext;
        }

        private void refreshCoordinatesAndOverflowDirection(Rect contentRectOnScreen) {
            int y;
            refreshViewPort();
            int x = Math.max(this.mPopupSideMargin, Math.min(contentRectOnScreen.centerX() - (getWidth() / 2), (this.mViewPortOnScreen.right - getWidth()) - this.mPopupSideMargin));
            int availableHeightAboveContent = contentRectOnScreen.top - this.mViewPortOnScreen.top;
            int availableHeightBelowContent = this.mViewPortOnScreen.bottom - contentRectOnScreen.bottom;
            if (this.mOverflowPanel != null) {
                int margin = this.mMarginVertical * 2;
                int minimumOverflowHeightWithMargin = this.mOverflowPanel.getMinimumHeight() + margin;
                int availableHeightThroughContentDown = (this.mViewPortOnScreen.bottom - contentRectOnScreen.top) + getToolbarHeightWithVerticalMargin();
                int availableHeightThroughContentUp = (contentRectOnScreen.bottom - this.mViewPortOnScreen.top) + getToolbarHeightWithVerticalMargin();
                if (availableHeightAboveContent >= minimumOverflowHeightWithMargin) {
                    updateOverflowHeight(availableHeightAboveContent - margin);
                    y = contentRectOnScreen.top - getHeight();
                    this.mOverflowDirection = 0;
                } else if (availableHeightAboveContent >= getToolbarHeightWithVerticalMargin() && availableHeightThroughContentDown >= minimumOverflowHeightWithMargin) {
                    updateOverflowHeight(availableHeightThroughContentDown - margin);
                    y = contentRectOnScreen.top - getToolbarHeightWithVerticalMargin();
                    this.mOverflowDirection = 1;
                } else if (availableHeightBelowContent >= minimumOverflowHeightWithMargin) {
                    updateOverflowHeight(availableHeightBelowContent - margin);
                    y = contentRectOnScreen.bottom;
                    this.mOverflowDirection = 1;
                } else if (availableHeightBelowContent < getToolbarHeightWithVerticalMargin() || this.mViewPortOnScreen.height() < minimumOverflowHeightWithMargin) {
                    updateOverflowHeight(this.mViewPortOnScreen.height() - margin);
                    y = this.mViewPortOnScreen.top;
                    this.mOverflowDirection = 1;
                } else {
                    updateOverflowHeight(availableHeightThroughContentUp - margin);
                    y = (contentRectOnScreen.bottom + getToolbarHeightWithVerticalMargin()) - getHeight();
                    this.mOverflowDirection = 0;
                }
                this.mOverflowPanel.setOverflowDirection(this.mOverflowDirection);
            } else if (this.mUseSamsungToolbar && this.mUseNewSamsungToolbar) {
                int mNeedHeight = this.mMainPanel.getNeedHeightNewPopup();
                updateNewPopup(mNeedHeight);
                if (availableHeightAboveContent >= ((this.mPopupBgPaddingTop + mNeedHeight) + this.mPopupBgPaddingBottom) + this.mPopupBelowMargin) {
                    y = (contentRectOnScreen.top - getHeight()) - this.mPopupBelowMargin;
                    this.mOverflowDirection = 0;
                } else if (availableHeightBelowContent >= ((this.mPopupBgPaddingTop + mNeedHeight) + this.mPopupBgPaddingBottom) + this.mPopupAboveMargin) {
                    y = contentRectOnScreen.bottom + this.mPopupAboveMargin;
                    this.mOverflowDirection = 1;
                } else {
                    updateNewPopup(Math.min(mNeedHeight, (this.mViewPortOnScreen.height() - this.mPopupAboveMargin) - this.mPopupBelowMargin));
                    y = this.mViewPortOnScreen.top;
                    this.mOverflowDirection = 1;
                }
            } else if (availableHeightAboveContent >= getToolbarHeightWithVerticalMargin() + this.mPopupBelowMargin) {
                y = (contentRectOnScreen.top - getToolbarHeightWithVerticalMargin()) - this.mPopupBelowMargin;
            } else if (availableHeightBelowContent >= getToolbarHeightWithVerticalMargin() + this.mPopupAboveMargin) {
                y = contentRectOnScreen.bottom + this.mPopupAboveMargin;
            } else if (availableHeightBelowContent >= FloatingToolbar.getEstimatedToolbarHeight(this.mContext, this.mUseSamsungToolbar)) {
                y = contentRectOnScreen.bottom - this.mMarginVertical;
            } else {
                y = Math.max(this.mViewPortOnScreen.top, contentRectOnScreen.top - getToolbarHeightWithVerticalMargin());
            }
            this.mParent.getRootView().getLocationOnScreen(this.mTmpCoords);
            int rootViewLeftOnScreen = this.mTmpCoords[0];
            int rootViewTopOnScreen = this.mTmpCoords[1];
            this.mParent.getRootView().getLocationInWindow(this.mTmpCoords);
            this.mCoordsOnWindow.set(x - (rootViewLeftOnScreen - this.mTmpCoords[0]), y - (rootViewTopOnScreen - this.mTmpCoords[1]));
        }

        private int getToolbarHeightWithVerticalMargin() {
            return ((FloatingToolbar.getEstimatedToolbarHeight(this.mContext, this.mUseSamsungToolbar) + (this.mMarginVertical * 2)) + this.mPopupBgPaddingTop) + this.mPopupBgPaddingBottom;
        }

        private void runShowAnimation() {
            FloatingToolbar.createEnterAnimation(this.mContentContainer).start();
        }

        private void runDismissAnimation() {
            this.mPopupWindow.dismiss();
            this.mContentContainer.removeAllViews();
        }

        private void runHideAnimation() {
            this.mHideAnimation.start();
        }

        private void cancelDismissAndHideAnimations() {
            this.mDismissAnimation.cancel();
            this.mHideAnimation.cancel();
        }

        private void cancelOverflowAnimations() {
            if (this.mOpenOverflowAnimation.hasStarted() && !this.mOpenOverflowAnimation.hasEnded()) {
                this.mOpenOverflowAnimation.setAnimationListener(null);
                this.mContentContainer.clearAnimation();
                this.mOnOverflowOpened.onAnimationEnd(null);
            }
            if (this.mCloseOverflowAnimation.hasStarted() && !this.mCloseOverflowAnimation.hasEnded()) {
                this.mCloseOverflowAnimation.setAnimationListener(null);
                this.mContentContainer.clearAnimation();
                this.mOnOverflowClosed.onAnimationEnd(null);
            }
        }

        private void openOverflow() {
            Preconditions.checkState(this.mMainPanel != null);
            Preconditions.checkState(this.mOverflowPanel != null);
            this.mMainPanel.fadeOut(true);
            Size overflowPanelSize = this.mOverflowPanel.measure();
            final int targetWidth = overflowPanelSize.getWidth();
            final int targetHeight = overflowPanelSize.getHeight();
            final boolean morphUpwards = this.mOverflowDirection == 0;
            final int startWidth = this.mContentContainer.getWidth();
            final int startHeight = this.mContentContainer.getHeight();
            final float startY = this.mContentContainer.getY();
            final float left = this.mContentContainer.getX();
            final float right = left + ((float) this.mContentContainer.getWidth());
            Animation widthAnimation = new Animation() {
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    LayoutParams params = FloatingToolbarPopup.this.mContentContainer.getLayoutParams();
                    params.width = startWidth + ((int) (((float) (targetWidth - startWidth)) * interpolatedTime));
                    FloatingToolbarPopup.this.mContentContainer.setLayoutParams(params);
                    if (FloatingToolbarPopup.this.isRTL()) {
                        FloatingToolbarPopup.this.mContentContainer.setX(left);
                    } else {
                        FloatingToolbarPopup.this.mContentContainer.setX(right - ((float) FloatingToolbarPopup.this.mContentContainer.getWidth()));
                    }
                }
            };
            Animation heightAnimation = new Animation() {
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    LayoutParams params = FloatingToolbarPopup.this.mContentContainer.getLayoutParams();
                    params.height = startHeight + ((int) (((float) (targetHeight - startHeight)) * interpolatedTime));
                    FloatingToolbarPopup.this.mContentContainer.setLayoutParams(params);
                    if (morphUpwards) {
                        FloatingToolbarPopup.this.mContentContainer.setY(startY - ((float) (FloatingToolbarPopup.this.mContentContainer.getHeight() - startHeight)));
                    }
                }
            };
            widthAnimation.setDuration(240);
            heightAnimation.setDuration(180);
            heightAnimation.setStartOffset(60);
            this.mOpenOverflowAnimation.getAnimations().clear();
            this.mOpenOverflowAnimation.setAnimationListener(this.mOnOverflowOpened);
            this.mOpenOverflowAnimation.addAnimation(widthAnimation);
            this.mOpenOverflowAnimation.addAnimation(heightAnimation);
            this.mContentContainer.startAnimation(this.mOpenOverflowAnimation);
        }

        private void closeOverflow() {
            Preconditions.checkState(this.mMainPanel != null);
            Preconditions.checkState(this.mOverflowPanel != null);
            this.mOverflowPanel.fadeOut(true);
            Size mainPanelSize = this.mMainPanel.measure();
            final int targetWidth = mainPanelSize.getWidth();
            final int targetHeight = mainPanelSize.getHeight();
            final int startWidth = this.mContentContainer.getWidth();
            final int startHeight = this.mContentContainer.getHeight();
            final float bottom = this.mContentContainer.getY() + ((float) this.mContentContainer.getHeight());
            final boolean morphedUpwards = this.mOverflowDirection == 0;
            final float left = this.mContentContainer.getX();
            final float right = left + ((float) this.mContentContainer.getWidth());
            Animation widthAnimation = new Animation() {
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    LayoutParams params = FloatingToolbarPopup.this.mContentContainer.getLayoutParams();
                    params.width = startWidth + ((int) (((float) (targetWidth - startWidth)) * interpolatedTime));
                    FloatingToolbarPopup.this.mContentContainer.setLayoutParams(params);
                    if (FloatingToolbarPopup.this.isRTL()) {
                        FloatingToolbarPopup.this.mContentContainer.setX(left);
                    } else {
                        FloatingToolbarPopup.this.mContentContainer.setX(right - ((float) FloatingToolbarPopup.this.mContentContainer.getWidth()));
                    }
                }
            };
            Animation heightAnimation = new Animation() {
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    LayoutParams params = FloatingToolbarPopup.this.mContentContainer.getLayoutParams();
                    params.height = startHeight + ((int) (((float) (targetHeight - startHeight)) * interpolatedTime));
                    FloatingToolbarPopup.this.mContentContainer.setLayoutParams(params);
                    if (morphedUpwards) {
                        FloatingToolbarPopup.this.mContentContainer.setY(bottom - ((float) FloatingToolbarPopup.this.mContentContainer.getHeight()));
                    }
                }
            };
            widthAnimation.setDuration(150);
            widthAnimation.setStartOffset(150);
            heightAnimation.setDuration(210);
            this.mCloseOverflowAnimation.getAnimations().clear();
            this.mCloseOverflowAnimation.setAnimationListener(this.mOnOverflowClosed);
            this.mCloseOverflowAnimation.addAnimation(widthAnimation);
            this.mCloseOverflowAnimation.addAnimation(heightAnimation);
            this.mContentContainer.startAnimation(this.mCloseOverflowAnimation);
        }

        private void openSamsungflow() {
            Preconditions.checkState(this.mMainPanel != null);
            final int startHeight = this.mContentContainer.getHeight();
            final int targetHeight = (Math.min(this.mMainPanel.getPopupMaxHeight(), this.mMainPanel.measureSamsung().getHeight()) + this.mPopupBgPaddingBottom) + this.mPopupBgPaddingTop;
            final boolean morphUpwards = this.mOverflowDirection == 0;
            final float startY = this.mContentContainer.getY();
            float right = this.mContentContainer.getX() + ((float) this.mContentContainer.getWidth());
            ValueAnimator heightAnimator = ValueAnimator.ofInt(new int[]{startHeight, targetHeight});
            heightAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    int val = ((Integer) animation.getAnimatedValue()).intValue();
                    LayoutParams params = FloatingToolbarPopup.this.mContentContainer.getLayoutParams();
                    params.height = val;
                    FloatingToolbarPopup.this.mContentContainer.setLayoutParams(params);
                    if (morphUpwards) {
                        FloatingToolbarPopup.this.mContentContainer.setY(startY - ((float) (FloatingToolbarPopup.this.mContentContainer.getHeight() - startHeight)));
                    }
                    FloatingToolbarPopup.this.mContentContainer.requestLayout();
                }
            });
            heightAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    LayoutParams params = FloatingToolbarPopup.this.mContentContainer.getLayoutParams();
                    params.height = targetHeight;
                    FloatingToolbarPopup.this.mContentContainer.setLayoutParams(params);
                    if (morphUpwards) {
                        FloatingToolbarPopup.this.mContentContainer.setY(startY - ((float) (targetHeight - startHeight)));
                    }
                    FloatingToolbarPopup.this.mContentContainer.requestLayout();
                }
            });
            heightAnimator.setDuration(166);
            heightAnimator.setInterpolator(new SineInOut33());
            ValueAnimator AlphaAnimator1 = ValueAnimator.ofFloat(new float[]{1.0f, 0.0f});
            AlphaAnimator1.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    FloatingToolbarPopup.this.mMainPanel.mOpenButton.setAlpha(((Float) animation.getAnimatedValue()).floatValue());
                }
            });
            AlphaAnimator1.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    FloatingToolbarPopup.this.mMainPanel.mOpenButton.setVisibility(8);
                    FloatingToolbarPopup.this.mMainPanel.mCloseButton.setVisibility(0);
                }
            });
            AlphaAnimator1.setDuration(100);
            AlphaAnimator1.setInterpolator(new SineInOut33());
            this.mOpenOverflowAnimatior1 = new AnimatorSet();
            this.mOpenOverflowAnimatior1.setStartDelay(0);
            this.mOpenOverflowAnimatior1.playTogether(new Animator[]{heightAnimator, AlphaAnimator1});
            ValueAnimator AlphaAnimator2 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            AlphaAnimator2.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    FloatingToolbarPopup.this.mMainPanel.mCloseButton.setAlpha(((Float) animation.getAnimatedValue()).floatValue());
                }
            });
            AlphaAnimator2.setDuration(166);
            AlphaAnimator2.setInterpolator(new SineInOut33());
            ValueAnimator AlphaAnimator3 = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
            AlphaAnimator3.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float val = ((Float) animation.getAnimatedValue()).floatValue();
                    for (int i = 1; i < FloatingToolbarPopup.this.mMainPanel.mVerticalLinearLayout.getChildCount(); i++) {
                        FloatingToolbarPopup.this.mMainPanel.mVerticalLinearLayout.getChildAt(i).setAlpha(val);
                    }
                }
            });
            AlphaAnimator3.addListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animation) {
                    for (int i = 1; i < FloatingToolbarPopup.this.mMainPanel.mVerticalLinearLayout.getChildCount(); i++) {
                        FloatingToolbarPopup.this.mMainPanel.mVerticalLinearLayout.getChildAt(i).setAlpha(0.0f);
                    }
                }

                public void onAnimationEnd(Animator animation) {
                    FloatingToolbarPopup.this.setMainPanelAsContent();
                }
            });
            AlphaAnimator3.setDuration(166);
            AlphaAnimator3.setInterpolator(new SineInOut33());
            this.mOpenOverflowAnimatior2 = new AnimatorSet();
            this.mOpenOverflowAnimatior2.setStartDelay(200);
            this.mOpenOverflowAnimatior2.playTogether(new Animator[]{AlphaAnimator2, AlphaAnimator3});
            this.mOpenOverflowAnimatior1.start();
            this.mOpenOverflowAnimatior2.start();
        }

        private void preparePopupContent() {
            if (this.mMainPanel != null) {
                this.mMainPanel.fadeIn(false);
            }
            if (this.mOverflowPanel != null) {
                this.mOverflowPanel.fadeIn(false);
            }
            if (isMainPanelContent()) {
                positionMainPanel();
            }
            if (isOverflowPanelContent()) {
                positionOverflowPanel();
            }
        }

        private boolean isMainPanelContent() {
            return this.mMainPanel != null && this.mContentContainer.getChildAt(0) == this.mMainPanel.getView();
        }

        private boolean isOverflowPanelContent() {
            return this.mOverflowPanel != null && this.mContentContainer.getChildAt(0) == this.mOverflowPanel.getView();
        }

        private void setMainPanelAsContent() {
            Preconditions.checkNotNull(this.mMainPanel);
            this.mContentContainer.removeAllViews();
            Size mainPanelSize = this.mMainPanel.measure();
            LayoutParams params = this.mContentContainer.getLayoutParams();
            params.width = (mainPanelSize.getWidth() + this.mPopupBgPaddingLeft) + this.mPopupBgPaddingRight;
            params.height = (mainPanelSize.getHeight() + this.mPopupBgPaddingBottom) + this.mPopupBgPaddingTop;
            this.mContentContainer.setLayoutParams(params);
            this.mContentContainer.addView(this.mMainPanel.getView());
            setContentAreaAsTouchableSurface();
        }

        private void setOverflowPanelAsContent() {
            Preconditions.checkNotNull(this.mOverflowPanel);
            this.mContentContainer.removeAllViews();
            Size overflowPanelSize = this.mOverflowPanel.measure();
            LayoutParams params = this.mContentContainer.getLayoutParams();
            params.width = overflowPanelSize.getWidth();
            params.height = overflowPanelSize.getHeight();
            this.mContentContainer.setLayoutParams(params);
            this.mContentContainer.addView(this.mOverflowPanel.getView());
            setContentAreaAsTouchableSurface();
        }

        private void positionMainPanel() {
            Preconditions.checkNotNull(this.mMainPanel);
            this.mContentContainer.setX((float) this.mMarginHorizontal);
            float y = (float) this.mMarginVertical;
            if (this.mOverflowDirection == 0) {
                y = (float) (getHeight() - (((this.mMainPanel.getView().getMeasuredHeight() + this.mMarginVertical) + this.mPopupBgPaddingTop) + this.mPopupBgPaddingBottom));
            }
            this.mContentContainer.setY(y);
            setContentAreaAsTouchableSurface();
        }

        private void positionOverflowPanel() {
            float x;
            Preconditions.checkNotNull(this.mOverflowPanel);
            if (isRTL()) {
                x = (float) this.mMarginHorizontal;
            } else {
                x = (float) (this.mPopupWindow.getWidth() - (this.mOverflowPanel.getView().getMeasuredWidth() + this.mMarginHorizontal));
            }
            this.mContentContainer.setX(x);
            this.mContentContainer.setY((float) this.mMarginVertical);
            setContentAreaAsTouchableSurface();
        }

        private void updateOverflowHeight(int height) {
            if (this.mOverflowPanel != null) {
                this.mOverflowPanel.setSuggestedHeight(height);
                boolean mainPanelContent = isMainPanelContent();
                boolean overflowPanelContent = isOverflowPanelContent();
                this.mContentContainer.removeAllViews();
                updatePopupSize();
                if (mainPanelContent) {
                    setMainPanelAsContent();
                }
                if (overflowPanelContent) {
                    setOverflowPanelAsContent();
                }
            }
        }

        private void updateNewPopup(int height) {
            this.mMainPanel.mOpenedPopupHeight = height;
            int mWidth = this.mMainPanel.measureSamsung().getWidth();
            int mHeight = this.mMainPanel.mOpenedPopupHeight;
            this.mPopupWindow.setWidth((((this.mMarginHorizontal * 2) + mWidth) + this.mPopupBgPaddingLeft) + this.mPopupBgPaddingRight);
            this.mPopupWindow.setHeight((((this.mMarginVertical * 2) + mHeight) + this.mPopupBgPaddingTop) + this.mPopupBgPaddingBottom);
        }

        private void updatePopupSize() {
            int width = 0;
            int height = 0;
            if (this.mMainPanel != null) {
                Size mainPanelSize = this.mMainPanel.measure();
                width = mainPanelSize.getWidth();
                height = mainPanelSize.getHeight();
            }
            if (this.mOverflowPanel != null) {
                Size overflowPanelSize = this.mOverflowPanel.measure();
                width = Math.max(width, overflowPanelSize.getWidth());
                height = Math.max(height, overflowPanelSize.getHeight());
            }
            this.mPopupWindow.setWidth((((this.mMarginHorizontal * 2) + width) + this.mPopupBgPaddingLeft) + this.mPopupBgPaddingRight);
            this.mPopupWindow.setHeight((((this.mMarginVertical * 2) + height) + this.mPopupBgPaddingTop) + this.mPopupBgPaddingBottom);
        }

        private void refreshViewPort() {
            this.mParent.getWindowVisibleDisplayFrame(this.mViewPortOnScreen);
            this.mOrientation = this.mContext.getResources().getConfiguration().orientation;
        }

        private boolean viewPortHasChanged() {
            this.mParent.getWindowVisibleDisplayFrame(this.mTmpRect);
            return !this.mTmpRect.equals(this.mViewPortOnScreen);
        }

        private boolean viewOrientationHasChanged(int orientation) {
            return this.mOrientation != orientation;
        }

        private int getToolbarWidth(int suggestedWidth) {
            int width = suggestedWidth;
            refreshViewPort();
            int maximumWidth = this.mViewPortOnScreen.width() - (this.mParent.getResources().getDimensionPixelSize(R.dimen.floating_toolbar_horizontal_margin) * 2);
            if (width <= 0) {
                width = this.mParent.getResources().getDimensionPixelSize(R.dimen.floating_toolbar_preferred_width);
            }
            return Math.min(width, maximumWidth);
        }

        private void setZeroTouchableSurface() {
            this.mTouchableRegion.setEmpty();
        }

        private void setContentAreaAsTouchableSurface() {
            if (!this.mPopupWindow.isShowing()) {
                this.mContentContainer.measure(0, 0);
            }
            this.mTouchableRegion.set((int) this.mContentContainer.getX(), (int) this.mContentContainer.getY(), ((int) this.mContentContainer.getX()) + this.mContentContainer.getMeasuredWidth(), ((int) this.mContentContainer.getY()) + this.mContentContainer.getMeasuredHeight());
        }

        private void setTouchableSurfaceInsetsComputer() {
            ViewTreeObserver viewTreeObserver = this.mPopupWindow.getContentView().getRootView().getViewTreeObserver();
            viewTreeObserver.removeOnComputeInternalInsetsListener(this.mInsetsComputer);
            viewTreeObserver.addOnComputeInternalInsetsListener(this.mInsetsComputer);
        }

        private boolean isRTL() {
            return this.mContentContainer.getLayoutDirection() == 1;
        }
    }

    private static final class ViewFader {
        private static final int FADE_IN_DURATION = 150;
        private static final int FADE_OUT_DURATION = 250;
        private final ObjectAnimator mFadeInAnimation;
        private final ObjectAnimator mFadeOutAnimation;
        private final View mView;

        private ViewFader(View view) {
            this.mView = (View) Preconditions.checkNotNull(view);
            this.mFadeOutAnimation = ObjectAnimator.ofFloat(view, View.ALPHA, new float[]{1.0f, 0.0f}).setDuration(250);
            this.mFadeInAnimation = ObjectAnimator.ofFloat(view, View.ALPHA, new float[]{0.0f, 1.0f}).setDuration(150);
        }

        public void fadeIn(boolean animate) {
            cancelFadeAnimations();
            if (animate) {
                this.mFadeInAnimation.start();
            } else {
                this.mView.setAlpha(1.0f);
            }
        }

        public void fadeOut(boolean animate) {
            cancelFadeAnimations();
            if (animate) {
                this.mFadeOutAnimation.start();
            } else {
                this.mView.setAlpha(0.0f);
            }
        }

        private void cancelFadeAnimations() {
            this.mFadeInAnimation.cancel();
            this.mFadeOutAnimation.cancel();
        }
    }

    public FloatingToolbar(Context context, Window window) {
        this(context, window, false);
    }

    public FloatingToolbar(Context context, Window window, boolean useSamsungToolbar) {
        this.mContentRect = new Rect();
        this.mPreviousContentRect = new Rect();
        this.mShowingMenuItems = new ArrayList();
        this.mMenuItemClickListener = NO_OP_MENUITEM_CLICK_LISTENER;
        this.mWidthChanged = true;
        this.mUseSamsungToolbar = false;
        this.mUseNewSamsungToolbar = false;
        this.mOrientationChangeHandler = new ComponentCallbacks() {
            public void onConfigurationChanged(Configuration newConfig) {
                if ((FloatingToolbar.this.mPopup.isShowing() && FloatingToolbar.this.mPopup.viewPortHasChanged()) || FloatingToolbar.this.mPopup.viewOrientationHasChanged(newConfig.orientation)) {
                    FloatingToolbar.this.mWidthChanged = true;
                }
            }

            public void onLowMemory() {
            }
        };
        Preconditions.checkNotNull(context);
        Preconditions.checkNotNull(window);
        this.mContext = applyDefaultTheme(context, useSamsungToolbar);
        this.mUseSamsungToolbar = useSamsungToolbar;
        this.mUseNewSamsungToolbar = this.mContext.getResources().getBoolean(R.bool.tw_edit_text_new_concept);
        this.mPopup = new FloatingToolbarPopup(this.mContext, window.getDecorView(), this.mUseSamsungToolbar);
    }

    public FloatingToolbar setMenu(Menu menu) {
        this.mMenu = (Menu) Preconditions.checkNotNull(menu);
        return this;
    }

    public FloatingToolbar setOnMenuItemClickListener(OnMenuItemClickListener menuItemClickListener) {
        if (menuItemClickListener != null) {
            this.mMenuItemClickListener = menuItemClickListener;
        } else {
            this.mMenuItemClickListener = NO_OP_MENUITEM_CLICK_LISTENER;
        }
        return this;
    }

    public FloatingToolbar setContentRect(Rect rect) {
        this.mContentRect.set((Rect) Preconditions.checkNotNull(rect));
        return this;
    }

    public FloatingToolbar setSuggestedWidth(int suggestedWidth) {
        this.mWidthChanged = ((double) Math.abs(suggestedWidth - this.mSuggestedWidth)) > ((double) this.mSuggestedWidth) * 0.2d;
        this.mSuggestedWidth = suggestedWidth;
        return this;
    }

    public FloatingToolbar show() {
        this.mContext.unregisterComponentCallbacks(this.mOrientationChangeHandler);
        this.mContext.registerComponentCallbacks(this.mOrientationChangeHandler);
        List<MenuItem> menuItems = getVisibleAndEnabledMenuItems(this.mMenu);
        if (!isCurrentlyShowing(menuItems) || this.mWidthChanged) {
            this.mPopup.dismiss();
            this.mPopup.layoutMenuItems(menuItems, this.mMenuItemClickListener, this.mSuggestedWidth);
            this.mShowingMenuItems = getShowingMenuItemsReferences(menuItems);
        }
        if (!this.mPopup.isShowing()) {
            this.mPopup.show(this.mContentRect);
        } else if (!this.mPreviousContentRect.equals(this.mContentRect)) {
            this.mPopup.updateCoordinates(this.mContentRect);
        }
        this.mWidthChanged = false;
        this.mPreviousContentRect.set(this.mContentRect);
        return this;
    }

    public FloatingToolbar updateLayout() {
        if (this.mPopup.isShowing()) {
            show();
        }
        return this;
    }

    public void dismiss() {
        this.mContext.unregisterComponentCallbacks(this.mOrientationChangeHandler);
        this.mPopup.dismiss();
    }

    public void hide() {
        this.mPopup.hide();
    }

    public boolean isShowing() {
        return this.mPopup.isShowing();
    }

    public boolean isHidden() {
        return this.mPopup.isHidden();
    }

    private boolean isCurrentlyShowing(List<MenuItem> menuItems) {
        return this.mShowingMenuItems.equals(getShowingMenuItemsReferences(menuItems));
    }

    private List<MenuItem> getVisibleAndEnabledMenuItems(Menu menu) {
        List<MenuItem> menuItems = new ArrayList();
        int i = 0;
        while (menu != null && i < menu.size()) {
            MenuItem menuItem = menu.getItem(i);
            if (menuItem.isVisible() && menuItem.isEnabled()) {
                Menu subMenu = menuItem.getSubMenu();
                if (subMenu != null) {
                    menuItems.addAll(getVisibleAndEnabledMenuItems(subMenu));
                } else {
                    menuItems.add(menuItem);
                }
            }
            i++;
        }
        return menuItems;
    }

    private List<Object> getShowingMenuItemsReferences(List<MenuItem> menuItems) {
        List<Object> references = new ArrayList();
        for (MenuItem menuItem : menuItems) {
            if (hasIconAndTitleMenuItem(menuItem)) {
                references.add(menuItem.getIcon());
                references.add(menuItem.getTitle());
            } else if (isIconOnlyMenuItem(menuItem)) {
                references.add(menuItem.getIcon());
            } else {
                references.add(menuItem.getTitle());
            }
        }
        return references;
    }

    private static boolean isIconOnlyMenuItem(MenuItem menuItem) {
        if (!TextUtils.isEmpty(menuItem.getTitle()) || menuItem.getIcon() == null) {
            return false;
        }
        return true;
    }

    private static boolean hasIconAndTitleMenuItem(MenuItem menuItem) {
        if (TextUtils.isEmpty(menuItem.getTitle()) || menuItem.getIcon() == null) {
            return false;
        }
        return true;
    }

    private static View createMenuItemButton(Context context, MenuItem menuItem) {
        return createMenuItemButton(context, menuItem, false);
    }

    private static View createMenuItemButton(Context context, MenuItem menuItem, boolean useSamsungToolbar) {
        boolean hasBounds = false;
        if (useSamsungToolbar) {
            TextView SamsungMenuItemButton = (TextView) LayoutInflater.from(context).inflate((int) R.layout.tw_text_edit_action_popup_text, null);
            SamsungMenuItemButton.setText(menuItem.getTitle());
            SamsungMenuItemButton.setContentDescription(menuItem.getTitle());
            Rect mBounds = menuItem.getIcon().getBounds();
            if (!(mBounds.left == 0 && mBounds.right == 0 && mBounds.top == 0 && mBounds.bottom == 0)) {
                hasBounds = true;
            }
            if (hasBounds) {
                SamsungMenuItemButton.setCompoundDrawables(null, menuItem.getIcon(), null, null);
                return SamsungMenuItemButton;
            }
            SamsungMenuItemButton.setCompoundDrawablesWithIntrinsicBounds(null, menuItem.getIcon(), null, null);
            return SamsungMenuItemButton;
        } else if (isIconOnlyMenuItem(menuItem)) {
            View imageMenuItemButton = LayoutInflater.from(context).inflate((int) R.layout.floating_popup_menu_image_button, null);
            ((ImageButton) imageMenuItemButton.findViewById(R.id.floating_toolbar_menu_item_image_button)).setImageDrawable(menuItem.getIcon());
            imageMenuItemButton.setHoverPopupType(0);
            return imageMenuItemButton;
        } else {
            TextView menuItemButton = (Button) LayoutInflater.from(context).inflate((int) R.layout.floating_popup_menu_button, null);
            menuItemButton.setText(menuItem.getTitle());
            menuItemButton.setContentDescription(menuItem.getTitle());
            menuItemButton.setHoverPopupType(0);
            return menuItemButton;
        }
    }

    private static TextView createOverflowMenuItemButton(Context context) {
        return (TextView) LayoutInflater.from(context).inflate((int) R.layout.floating_popup_overflow_list_item, null);
    }

    private static ViewGroup createContentContainer(Context context) {
        return createContentContainer(context, false);
    }

    private static ViewGroup createContentContainer(Context context, boolean useSamsungToolbar) {
        if (useSamsungToolbar) {
            return (ViewGroup) LayoutInflater.from(context).inflate((int) R.layout.tw_floating_popup_container, null);
        }
        return (ViewGroup) LayoutInflater.from(context).inflate((int) R.layout.floating_popup_container, null);
    }

    private static PopupWindow createPopupWindow(View content) {
        return createPopupWindow(content, false);
    }

    private static PopupWindow createPopupWindow(View content, boolean useSamsungToolbar) {
        View popupContentHolder = new LinearLayout(content.getContext());
        PopupWindow popupWindow = new PopupWindow(popupContentHolder);
        popupWindow.setClippingEnabled(false);
        popupWindow.setWindowLayoutType(1005);
        popupWindow.setAnimationStyle(0);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        content.setLayoutParams(new LayoutParams(-2, -2));
        popupContentHolder.addView(content);
        return popupWindow;
    }

    private static AnimatorSet createEnterAnimation(View view) {
        AnimatorSet animation = new AnimatorSet();
        Animator[] animatorArr = new Animator[2];
        animatorArr[0] = ObjectAnimator.ofFloat(view, View.ALPHA, new float[]{0.0f, 1.0f}).setDuration(150);
        animatorArr[1] = ObjectAnimator.ofFloat(view, View.X, new float[]{view.getX(), view.getX()});
        animation.playTogether(animatorArr);
        return animation;
    }

    private static AnimatorSet createExitAnimation(View view, int startDelay, AnimatorListener listener) {
        AnimatorSet animation = new AnimatorSet();
        animation.playTogether(new Animator[]{ObjectAnimator.ofFloat(view, View.ALPHA, new float[]{1.0f, 0.0f}).setDuration(100)});
        animation.setStartDelay((long) startDelay);
        animation.addListener(listener);
        return animation;
    }

    private static Context applyDefaultTheme(Context originalContext) {
        return applyDefaultTheme(originalContext, false);
    }

    private static Context applyDefaultTheme(Context originalContext, boolean useSamsungToolbar) {
        if (useSamsungToolbar) {
            TypedArray a = originalContext.obtainStyledAttributes(new int[]{R.attr.parentIsDeviceDefaultDark});
            int themeId = a.getBoolean(0, false) ? R.style.Theme_DeviceDefault_Light : R.style.Theme_DeviceDefault;
            a.recycle();
            return new ContextThemeWrapper(originalContext, themeId);
        }
        a = originalContext.obtainStyledAttributes(new int[]{R.attr.isLightTheme});
        themeId = a.getBoolean(0, true) ? R.style.Theme_Material_Light : R.style.Theme_Material;
        a.recycle();
        return new ContextThemeWrapper(originalContext, themeId);
    }

    private static int getEstimatedToolbarHeight(Context context) {
        return getEstimatedToolbarHeight(context, false);
    }

    private static int getEstimatedToolbarHeight(Context context, boolean useSamsungToolbar) {
        if (useSamsungToolbar) {
            return context.getResources().getDimensionPixelSize(R.dimen.tw_text_edit_action_popup_item_height);
        }
        return context.getResources().getDimensionPixelSize(R.dimen.floating_toolbar_height);
    }

    private static int getEstimatedOpenOverflowButtonWidth(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.floating_toolbar_menu_button_minimum_width);
    }
}
