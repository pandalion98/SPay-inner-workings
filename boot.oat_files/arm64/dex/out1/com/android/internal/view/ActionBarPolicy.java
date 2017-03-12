package com.android.internal.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.ViewConfiguration;
import com.android.internal.R;

public class ActionBarPolicy {
    private Context mContext;

    public static ActionBarPolicy get(Context context) {
        return new ActionBarPolicy(context);
    }

    private ActionBarPolicy(Context context) {
        this.mContext = context;
    }

    public int getMaxActionButtons() {
        return this.mContext.getResources().getInteger(R.integer.max_action_buttons);
    }

    public boolean showsOverflowMenuButton() {
        return !ViewConfiguration.get(this.mContext).hasPermanentMenuKey();
    }

    public int getEmbeddedMenuWidthLimit() {
        return this.mContext.getResources().getDisplayMetrics().widthPixels / 2;
    }

    public boolean hasEmbeddedTabs() {
        if (this.mContext.getApplicationInfo().targetSdkVersion >= 16) {
            return this.mContext.getResources().getBoolean(R.bool.action_bar_embed_tabs);
        }
        return this.mContext.getResources().getBoolean(R.bool.action_bar_embed_tabs_pre_jb);
    }

    public int getTabContainerHeight() {
        boolean useCustomHeight = false;
        TypedArray a = this.mContext.obtainStyledAttributes(null, R.styleable.ActionBar, R.attr.actionBarStyle, 0);
        int height = a.getLayoutDimension(4, 0);
        if (this.mContext instanceof Activity) {
            Activity activity = this.mContext;
            if (activity == null) {
                throw new IllegalStateException("activity is null!");
            }
            int customHeight = activity.getActionBarTabSize();
            if (activity.checkNoEmbeddedTabs() && customHeight > -1) {
                useCustomHeight = true;
            }
            Resources r = this.mContext.getResources();
            if (useCustomHeight) {
                height = customHeight;
            } else if (!hasEmbeddedTabs()) {
                height = Math.min(height, r.getDimensionPixelSize(R.dimen.action_bar_stacked_max_height));
            }
        }
        a.recycle();
        return height;
    }

    public boolean enableHomeButtonByDefault() {
        return this.mContext.getApplicationInfo().targetSdkVersion < 14;
    }

    public int getStackedTabMaxWidth() {
        return this.mContext.getResources().getDimensionPixelSize(R.dimen.action_bar_stacked_tab_max_width);
    }
}
