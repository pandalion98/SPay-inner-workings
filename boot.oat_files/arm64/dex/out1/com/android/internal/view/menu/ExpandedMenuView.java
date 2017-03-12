package com.android.internal.view.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.android.internal.R;
import com.android.internal.view.menu.MenuBuilder.ItemInvoker;

public final class ExpandedMenuView extends ListView implements ItemInvoker, MenuView, OnItemClickListener {
    private int isDeviceDefault;
    private int mAnimations;
    private MenuBuilder mMenu;
    private int mVisibleMaxHeight;

    public ExpandedMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MenuView, 0, 0);
        this.mAnimations = a.getResourceId(0, 0);
        a.recycle();
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(R.attr.parentIsDeviceDefault, outValue, true);
        this.isDeviceDefault = outValue.data;
        setOnItemClickListener(this);
    }

    public void initialize(MenuBuilder menu) {
        this.mMenu = menu;
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Adapter mAdapter = getAdapter();
        if (mAdapter != null && (mAdapter instanceof BaseAdapter)) {
            ((BaseAdapter) mAdapter).notifyDataSetChanged();
        }
        setSelection(0);
    }

    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setChildrenDrawingCacheEnabled(false);
    }

    public boolean invokeItem(MenuItemImpl item) {
        return this.mMenu.performItemAction(item, 0);
    }

    public void onItemClick(AdapterView parent, View v, int position, long id) {
        invokeItem((MenuItemImpl) getAdapter().getItem(position));
    }

    public int getWindowAnimations() {
        return this.mAnimations;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mItemCount = getCount();
        if (this.isDeviceDefault != 0) {
            int mItemMaxCount = this.mContext.getResources().getInteger(R.integer.tw_option_menu_max_line_count);
            int mMeasuredHeight = getMeasuredHeight();
            int mVisibleMaxHeight = mItemCount > mItemMaxCount ? this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_option_menu_maxheight) : this.mContext.getResources().getDimensionPixelSize(R.dimen.tw_option_menu_height);
            if (mMeasuredHeight > mVisibleMaxHeight && mVisibleMaxHeight != 0) {
                setMeasuredDimension(getMeasuredWidth(), mVisibleMaxHeight);
            }
        }
    }
}
