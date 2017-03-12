package android.preference;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PreferenceGroupAdapter extends BaseAdapter implements OnPreferenceChangeInternalListener {
    private static final String TAG = "PreferenceGroupAdapter";
    private static LayoutParams sWrapperLayoutParams = new LayoutParams(-1, -2);
    private int mCntPreferenceCategory = 0;
    private Handler mHandler = new Handler();
    private boolean mHasReturnedViewTypeCount = false;
    private Drawable mHighlightedDrawable;
    private int mHighlightedPosition = -1;
    private volatile boolean mIsSyncing = false;
    private boolean mIsTwForceRecycleList = false;
    private Preference mLastPreference = null;
    private PreferenceGroup mPreferenceGroup;
    private ArrayList<PreferenceLayout> mPreferenceLayouts;
    private List<Preference> mPreferenceList;
    private Runnable mSyncRunnable = new Runnable() {
        public void run() {
            PreferenceGroupAdapter.this.syncMyPreferences();
        }
    };
    private PreferenceLayout mTempPreferenceLayout = new PreferenceLayout();
    public int mTwNeedPaddingTop = 0;

    private static class PreferenceLayout implements Comparable<PreferenceLayout> {
        private boolean mTwForceRecycleLayout;
        private String name;
        private int resId;
        private int widgetResId;

        private PreferenceLayout() {
        }

        public int compareTo(PreferenceLayout other) {
            int compareNames = this.name.compareTo(other.name);
            if (compareNames != 0) {
                return compareNames;
            }
            if (this.resId != other.resId) {
                return this.resId - other.resId;
            }
            if (this.widgetResId == other.widgetResId) {
                return 0;
            }
            return this.widgetResId - other.widgetResId;
        }
    }

    public PreferenceGroupAdapter(PreferenceGroup preferenceGroup) {
        this.mPreferenceGroup = preferenceGroup;
        this.mPreferenceGroup.setOnPreferenceChangeInternalListener(this);
        this.mPreferenceList = new ArrayList();
        this.mPreferenceLayouts = new ArrayList();
        syncMyPreferences();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void syncMyPreferences() {
        /*
        r3 = this;
        r1 = 0;
        monitor-enter(r3);
        r2 = r3.mIsSyncing;	 Catch:{ all -> 0x0040 }
        if (r2 == 0) goto L_0x0008;
    L_0x0006:
        monitor-exit(r3);	 Catch:{ all -> 0x0040 }
    L_0x0007:
        return;
    L_0x0008:
        r2 = 1;
        r3.mIsSyncing = r2;	 Catch:{ all -> 0x0040 }
        monitor-exit(r3);	 Catch:{ all -> 0x0040 }
        r3.mCntPreferenceCategory = r1;
        r2 = 0;
        r3.mLastPreference = r2;
        r2 = android.view.View.TW_SCAFE_AMERICANO;
        if (r2 != 0) goto L_0x0019;
    L_0x0015:
        r2 = android.view.View.TW_SCAFE_MOCHA;
        if (r2 == 0) goto L_0x005e;
    L_0x0019:
        r2 = r3.mPreferenceGroup;
        if (r2 != 0) goto L_0x0043;
    L_0x001d:
        r3.mTwNeedPaddingTop = r1;
    L_0x001f:
        r0 = new java.util.ArrayList;
        r1 = r3.mPreferenceList;
        r1 = r1.size();
        r0.<init>(r1);
        r1 = r3.mPreferenceGroup;
        r3.flattenPreferenceGroup(r0, r1);
        r3.mPreferenceList = r0;
        r3.notifyDataSetChanged();
        monitor-enter(r3);
        r1 = 0;
        r3.mIsSyncing = r1;	 Catch:{ all -> 0x003d }
        r3.notifyAll();	 Catch:{ all -> 0x003d }
        monitor-exit(r3);	 Catch:{ all -> 0x003d }
        goto L_0x0007;
    L_0x003d:
        r1 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x003d }
        throw r1;
    L_0x0040:
        r1 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0040 }
        throw r1;
    L_0x0043:
        r2 = r3.mPreferenceGroup;
        r2 = r2.mIsDeviceDefault;
        if (r2 == 0) goto L_0x001d;
    L_0x0049:
        r1 = 1090519040; // 0x41000000 float:8.0 double:5.38787994E-315;
        r2 = r3.mPreferenceGroup;
        r2 = r2.twGetContext();
        r2 = r2.getResources();
        r2 = r2.getDisplayMetrics();
        r2 = r2.density;
        r1 = r1 * r2;
        r1 = (int) r1;
        goto L_0x001d;
    L_0x005e:
        r3.mTwNeedPaddingTop = r1;
        goto L_0x001f;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.preference.PreferenceGroupAdapter.syncMyPreferences():void");
    }

    private void flattenPreferenceGroup(List<Preference> preferences, PreferenceGroup group) {
        group.sortPreferences();
        int groupSize = group.getPreferenceCount();
        for (int i = 0; i < groupSize; i++) {
            Preference preference = group.getPreference(i);
            if (preference != null) {
                preferences.add(preference);
                if (preference.mTwForceRecycleLayout) {
                    this.mIsTwForceRecycleList = true;
                    if (i == 0) {
                        this.mHasReturnedViewTypeCount = false;
                    }
                }
                if (preference.mIsDeviceDefault) {
                    if (preference instanceof PreferenceCategory) {
                        this.mCntPreferenceCategory++;
                        preference.mIsPreferenceCategory = true;
                        if (this.mLastPreference != null) {
                            this.mLastPreference.mNeedPaddingBottom = true;
                        }
                        if (i == 0 && this.mLastPreference == null) {
                            this.mTwNeedPaddingTop = 0;
                            if (preference.getLayoutResource() == 17367341) {
                                preference.setLayoutResourceInternal(17367342);
                            }
                        }
                        if (TextUtils.isEmpty(preference.getTitle())) {
                            preference.setLayoutResourceInternal(17367343);
                        }
                    } else {
                        preference.mIsPreferenceCategory = false;
                        preference.mNeedPaddingBottom = false;
                    }
                    this.mLastPreference = preference;
                }
                if (!(this.mHasReturnedViewTypeCount || preference == null || !preference.canRecycleLayout())) {
                    addPreferenceClassName(preference);
                }
                if (preference instanceof PreferenceGroup) {
                    PreferenceGroup preferenceAsGroup = (PreferenceGroup) preference;
                    if (preferenceAsGroup.isOnSameScreenAsChildren()) {
                        flattenPreferenceGroup(preferences, preferenceAsGroup);
                    }
                }
                preference.setOnPreferenceChangeInternalListener(this);
            }
        }
    }

    private PreferenceLayout createPreferenceLayout(Preference preference, PreferenceLayout in) {
        PreferenceLayout pl = in != null ? in : new PreferenceLayout();
        pl.name = preference.getClass().getName();
        pl.resId = preference.getLayoutResource();
        pl.widgetResId = preference.getWidgetLayoutResource();
        pl.mTwForceRecycleLayout = preference.mTwForceRecycleLayout;
        return pl;
    }

    private void addPreferenceClassName(Preference preference) {
        PreferenceLayout pl = createPreferenceLayout(preference, null);
        int insertPos = Collections.binarySearch(this.mPreferenceLayouts, pl);
        if (insertPos < 0) {
            this.mPreferenceLayouts.add((insertPos * -1) - 1, pl);
        }
    }

    public int getCount() {
        return this.mPreferenceList.size();
    }

    public Preference getItem(int position) {
        if (position < 0 || position >= getCount()) {
            return null;
        }
        return (Preference) this.mPreferenceList.get(position);
    }

    public long getItemId(int position) {
        if (position < 0 || position >= getCount()) {
            return Long.MIN_VALUE;
        }
        return getItem(position).getId();
    }

    public void setHighlighted(int position) {
        this.mHighlightedPosition = position;
    }

    public void setHighlightedDrawable(Drawable drawable) {
        this.mHighlightedDrawable = drawable;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Preference preference = getItem(position);
        if (preference == null) {
            return convertView;
        }
        this.mTempPreferenceLayout = createPreferenceLayout(preference, this.mTempPreferenceLayout);
        if (Collections.binarySearch(this.mPreferenceLayouts, this.mTempPreferenceLayout) < 0 || getItemViewType(position) == getHighlightItemViewType()) {
            convertView = null;
        }
        if (preference.mIsDeviceDefault && View.TW_SCAFE_2016A) {
            preference.setTitleLargerTextSize(this.mPreferenceGroup.mLargerFontLevel, this.mPreferenceGroup.mTitleLargerTextSize);
        }
        View result = preference.getView(convertView, parent);
        if (position != this.mHighlightedPosition || this.mHighlightedDrawable == null) {
            return result;
        }
        View wrapper = new FrameLayout(parent.getContext());
        wrapper.setLayoutParams(sWrapperLayoutParams);
        wrapper.setBackgroundDrawable(this.mHighlightedDrawable);
        wrapper.addView(result);
        return wrapper;
    }

    public boolean isEnabled(int position) {
        if (position < 0 || position >= getCount()) {
            return true;
        }
        return getItem(position).isSelectable();
    }

    public int getPreferenceCategoryCnt() {
        return this.mCntPreferenceCategory;
    }

    public boolean isPreferenceCategory(int position) {
        if (position < 0 || position >= getCount()) {
            return false;
        }
        return getItem(position) instanceof PreferenceCategory;
    }

    public boolean areAllItemsEnabled() {
        return false;
    }

    public void onPreferenceChange(Preference preference) {
        notifyDataSetChanged();
    }

    public void onPreferenceHierarchyChange(Preference preference) {
        this.mHandler.removeCallbacks(this.mSyncRunnable);
        this.mHandler.post(this.mSyncRunnable);
    }

    public boolean hasStableIds() {
        return true;
    }

    private int getHighlightItemViewType() {
        return getViewTypeCount() - 1;
    }

    public int getItemViewType(int position) {
        if (position == this.mHighlightedPosition) {
            return getHighlightItemViewType();
        }
        if (!this.mHasReturnedViewTypeCount) {
            this.mHasReturnedViewTypeCount = true;
        }
        Preference preference = getItem(position);
        if (preference == null || !preference.canRecycleLayout()) {
            return -1;
        }
        this.mTempPreferenceLayout = createPreferenceLayout(preference, this.mTempPreferenceLayout);
        int viewType = Collections.binarySearch(this.mPreferenceLayouts, this.mTempPreferenceLayout);
        if (viewType < 0) {
            return -1;
        }
        return viewType;
    }

    public int getViewTypeCount() {
        if (!(this.mIsTwForceRecycleList || this.mHasReturnedViewTypeCount)) {
            this.mHasReturnedViewTypeCount = true;
        }
        return Math.max(1, this.mPreferenceLayouts.size()) + 1;
    }
}
