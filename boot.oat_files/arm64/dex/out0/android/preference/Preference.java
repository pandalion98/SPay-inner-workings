package android.preference;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.preference.PreferenceManager.OnPreferenceTreeClickListener;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.AbsSavedState;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.util.CharSequences;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Preference implements Comparable<Preference> {
    public static final int DEFAULT_ORDER = Integer.MAX_VALUE;
    private boolean mBaseMethodCalled;
    private boolean mCanRecycleLayout;
    private ColorStateList mColorPrimaryDark;
    private Context mContext;
    private Object mDefaultValue;
    private String mDependencyKey;
    private boolean mDependencyMet;
    private List<Preference> mDependents;
    private boolean mEnabled;
    private Bundle mExtras;
    private String mFragment;
    private Drawable mIcon;
    private int mIconResId;
    private long mId;
    private Intent mIntent;
    boolean mIsDeviceDefault;
    boolean mIsPreferenceCategory;
    private boolean mIsSummaryColorPrimaryDark;
    private String mKey;
    private int mLargerFontLevel;
    private int mLayoutResId;
    private OnPreferenceChangeInternalListener mListener;
    boolean mNeedPaddingBottom;
    private OnPreferenceChangeListener mOnChangeListener;
    private OnPreferenceClickListener mOnClickListener;
    private int mOrder;
    private boolean mParentDependencyMet;
    private boolean mPersistent;
    private PreferenceManager mPreferenceManager;
    private boolean mRequiresKey;
    private boolean mSelectable;
    private boolean mShouldDisableView;
    private CharSequence mSummary;
    private CharSequence mSummaryDescription;
    private ColorStateList mTextColorSecondary;
    private CharSequence mTitle;
    private CharSequence mTitleDescription;
    private int mTitleDescriptionRes;
    private int mTitleLargerTextSize;
    private int mTitleRes;
    public int mTwExtraPaddingBottom;
    boolean mTwForceRecycleLayout;
    private int mWidgetLayoutResId;

    public static class BaseSavedState extends AbsSavedState {
        public static final Creator<BaseSavedState> CREATOR = new Creator<BaseSavedState>() {
            public BaseSavedState createFromParcel(Parcel in) {
                return new BaseSavedState(in);
            }

            public BaseSavedState[] newArray(int size) {
                return new BaseSavedState[size];
            }
        };

        public BaseSavedState(Parcel source) {
            super(source);
        }

        public BaseSavedState(Parcelable superState) {
            super(superState);
        }
    }

    interface OnPreferenceChangeInternalListener {
        void onPreferenceChange(Preference preference);

        void onPreferenceHierarchyChange(Preference preference);
    }

    public interface OnPreferenceChangeListener {
        boolean onPreferenceChange(Preference preference, Object obj);
    }

    public interface OnPreferenceClickListener {
        boolean onPreferenceClick(Preference preference);
    }

    public Preference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.mOrder = Integer.MAX_VALUE;
        this.mEnabled = true;
        this.mSelectable = true;
        this.mPersistent = true;
        this.mDependencyMet = true;
        this.mParentDependencyMet = true;
        this.mNeedPaddingBottom = false;
        this.mIsPreferenceCategory = false;
        this.mTwExtraPaddingBottom = 0;
        this.mIsDeviceDefault = false;
        this.mShouldDisableView = true;
        this.mLayoutResId = 17367227;
        this.mCanRecycleLayout = true;
        this.mIsSummaryColorPrimaryDark = false;
        this.mTwForceRecycleLayout = false;
        this.mContext = context;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Preference, defStyleAttr, defStyleRes);
        for (int i = a.getIndexCount() - 1; i >= 0; i--) {
            int attr = a.getIndex(i);
            switch (attr) {
                case 0:
                    this.mIconResId = a.getResourceId(attr, 0);
                    break;
                case 1:
                    this.mPersistent = a.getBoolean(attr, this.mPersistent);
                    break;
                case 2:
                    this.mEnabled = a.getBoolean(attr, true);
                    break;
                case 3:
                    this.mLayoutResId = a.getResourceId(attr, this.mLayoutResId);
                    break;
                case 4:
                    this.mTitleRes = a.getResourceId(attr, 0);
                    this.mTitle = a.getString(attr);
                    if (!Build.IS_SYSTEM_SECURE) {
                        break;
                    }
                    this.mContext.getResources().addPreferenceString(this.mTitle, this.mTitleRes);
                    break;
                case 5:
                    this.mSelectable = a.getBoolean(attr, true);
                    break;
                case 6:
                    this.mKey = a.getString(attr);
                    break;
                case 7:
                    this.mSummary = a.getString(attr);
                    if (!Build.IS_SYSTEM_SECURE) {
                        break;
                    }
                    this.mContext.getResources().addPreferenceString(this.mSummary, a.getResourceId(attr, 0));
                    break;
                case 8:
                    this.mOrder = a.getInt(attr, this.mOrder);
                    break;
                case 9:
                    this.mWidgetLayoutResId = a.getResourceId(attr, this.mWidgetLayoutResId);
                    break;
                case 10:
                    this.mDependencyKey = a.getString(attr);
                    break;
                case 11:
                    this.mDefaultValue = onGetDefaultValue(a, attr);
                    break;
                case 12:
                    this.mShouldDisableView = a.getBoolean(attr, this.mShouldDisableView);
                    break;
                case 13:
                    this.mFragment = a.getString(attr);
                    break;
                case 14:
                    this.mTitleDescriptionRes = a.getResourceId(attr, 0);
                    this.mTitleDescription = a.getString(attr);
                    break;
                case 15:
                    this.mSummaryDescription = a.getString(attr);
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(18219197, outValue, true);
        this.mIsDeviceDefault = outValue.data != 0;
        if (this.mIsDeviceDefault) {
            try {
                this.mTwExtraPaddingBottom = (int) (8.0f * context.getResources().getDisplayMetrics().density);
            } catch (Exception e) {
                this.mTwExtraPaddingBottom = 0;
            }
            context.getTheme().resolveAttribute(android.R.attr.textColorSecondary, outValue, true);
            if (outValue.resourceId > 0) {
                this.mTextColorSecondary = context.getResources().getColorStateList(outValue.resourceId);
            }
            this.mColorPrimaryDark = context.getResources().getColorStateList(17170956, context.getTheme());
        }
        if (!getClass().getName().startsWith(PreferenceManager.METADATA_KEY_PREFERENCES) && !getClass().getName().startsWith("com.android")) {
            this.mCanRecycleLayout = false;
        }
    }

    public Preference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Preference(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.preferenceStyle);
    }

    public Preference(Context context) {
        this(context, null);
    }

    protected Object onGetDefaultValue(TypedArray a, int index) {
        return null;
    }

    public void setIntent(Intent intent) {
        this.mIntent = intent;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public void setFragment(String fragment) {
        this.mFragment = fragment;
    }

    public String getFragment() {
        return this.mFragment;
    }

    public Bundle getExtras() {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        return this.mExtras;
    }

    public Bundle peekExtras() {
        return this.mExtras;
    }

    public void setLayoutResource(int layoutResId) {
        if (layoutResId != this.mLayoutResId) {
            this.mCanRecycleLayout = false;
        }
        this.mLayoutResId = layoutResId;
    }

    void setLayoutResourceInternal(int layoutResId) {
        this.mLayoutResId = layoutResId;
    }

    public int getLayoutResource() {
        return this.mLayoutResId;
    }

    public void setWidgetLayoutResource(int widgetLayoutResId) {
        if (widgetLayoutResId != this.mWidgetLayoutResId) {
            this.mCanRecycleLayout = false;
        }
        this.mWidgetLayoutResId = widgetLayoutResId;
    }

    public int getWidgetLayoutResource() {
        return this.mWidgetLayoutResId;
    }

    public View getView(View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = onCreateView(parent);
        }
        onBindView(convertView);
        if (this.mIsDeviceDefault && (this instanceof PreferenceCategory) && convertView != null && (convertView instanceof TextView)) {
            convertView.setContentDescription(((TextView) convertView).getText().toString() + ", " + this.mContext.getString(17041990));
        }
        if (this.mIsDeviceDefault && ((View.TW_SCAFE_AMERICANO || View.TW_SCAFE_MOCHA) && convertView != null)) {
            if (this.mNeedPaddingBottom && convertView.getPaddingBottom() == 0) {
                convertView.setPaddingRelative(convertView.getPaddingStart(), convertView.getPaddingTop(), convertView.getPaddingEnd(), this.mTwExtraPaddingBottom);
                convertView.mTwExtraPaddingBottomForPreference = this.mTwExtraPaddingBottom;
            } else if (!this.mNeedPaddingBottom && convertView.mTwExtraPaddingBottomForPreference > 0) {
                convertView.setPaddingRelative(convertView.getPaddingStart(), convertView.getPaddingTop(), convertView.getPaddingEnd(), convertView.getPaddingBottom() - convertView.mTwExtraPaddingBottomForPreference);
                convertView.mTwExtraPaddingBottomForPreference = 0;
            }
        }
        return convertView;
    }

    protected View onCreateView(ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(this.mLayoutResId, parent, false);
        ViewGroup widgetFrame = (ViewGroup) layout.findViewById(android.R.id.widget_frame);
        if (widgetFrame != null) {
            if (this.mWidgetLayoutResId != 0) {
                layoutInflater.inflate(this.mWidgetLayoutResId, widgetFrame);
            } else {
                widgetFrame.setVisibility(8);
            }
        }
        return layout;
    }

    protected void onBindView(View view) {
        int i = 0;
        TextView titleView = (TextView) view.findViewById(android.R.id.title);
        if (titleView != null) {
            CharSequence title = getTitle();
            CharSequence titleDescription = getTitleDescription();
            if (!TextUtils.isEmpty(title)) {
                titleView.setText(title);
                if (View.TW_SCAFE_2016A && this.mLargerFontLevel > 5 && !(this instanceof PreferenceCategory)) {
                    titleView.setTextSize(0, (float) this.mTitleLargerTextSize);
                }
                if (TextUtils.isEmpty(titleDescription)) {
                    titleView.setContentDescription(null);
                } else {
                    titleView.setContentDescription(titleDescription);
                }
                titleView.setVisibility(0);
            } else if (TextUtils.isEmpty(title) && (this instanceof PreferenceCategory)) {
                titleView.setVisibility(0);
            } else {
                titleView.setVisibility(8);
            }
        }
        TextView summaryView = (TextView) view.findViewById(android.R.id.summary);
        if (summaryView != null) {
            CharSequence summary = getSummary();
            CharSequence summaryDescription = getSummaryDescription();
            if (TextUtils.isEmpty(summary)) {
                summaryView.setVisibility(8);
            } else {
                summaryView.setText(summary);
                if (TextUtils.isEmpty(summaryDescription)) {
                    summaryView.setContentDescription(null);
                } else {
                    summaryView.setContentDescription(summaryDescription);
                }
                if (this.mIsDeviceDefault && View.TW_SCAFE_2016A) {
                    if (getTwSummaryColorToColorPrimaryDark() || (this instanceof ListPreference)) {
                        summaryView.setTextColor(this.mColorPrimaryDark);
                    } else if (this.mTextColorSecondary != null) {
                        summaryView.setTextColor(this.mTextColorSecondary);
                    }
                }
                summaryView.setVisibility(0);
            }
        }
        ImageView imageView = (ImageView) view.findViewById(android.R.id.icon);
        if (imageView != null) {
            int i2;
            if (!(this.mIconResId == 0 && this.mIcon == null)) {
                if (this.mIcon == null) {
                    this.mIcon = getContext().getDrawable(this.mIconResId);
                }
                if (this.mIcon != null) {
                    imageView.setImageDrawable(this.mIcon);
                }
            }
            if (this.mIcon != null) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            imageView.setVisibility(i2);
        }
        View imageFrame = view.findViewById(16909388);
        if (imageFrame != null) {
            if (this.mIcon == null) {
                i = 8;
            }
            imageFrame.setVisibility(i);
        }
        if (this.mShouldDisableView) {
            setEnabledStateOnViews(view, isEnabled());
        }
    }

    Context twGetContext() {
        return this.mContext;
    }

    private void setEnabledStateOnViews(View v, boolean enabled) {
        v.setEnabled(enabled);
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            for (int i = vg.getChildCount() - 1; i >= 0; i--) {
                setEnabledStateOnViews(vg.getChildAt(i), enabled);
            }
        }
    }

    public void setOrder(int order) {
        if (order != this.mOrder) {
            this.mOrder = order;
            notifyHierarchyChanged();
        }
    }

    public int getOrder() {
        return this.mOrder;
    }

    public void setTitle(CharSequence title) {
        if ((title == null && this.mTitle != null) || (title != null && !title.equals(this.mTitle))) {
            this.mTitleRes = 0;
            this.mTitle = title;
            notifyChanged();
        }
    }

    public void setTitle(int titleResId) {
        setTitle(this.mContext.getString(titleResId));
        this.mTitleRes = titleResId;
    }

    public void setTitleDescription(CharSequence titleDescription) {
        if ((titleDescription == null && this.mTitleDescription != null) || (titleDescription != null && !titleDescription.equals(this.mTitle))) {
            this.mTitleDescriptionRes = 0;
            this.mTitleDescription = titleDescription;
            notifyChanged();
        }
    }

    public void setTitleDescription(int titleDescriptionResId) {
        setTitleDescription(this.mContext.getString(titleDescriptionResId));
        this.mTitleDescriptionRes = titleDescriptionResId;
    }

    public int getTitleRes() {
        return this.mTitleRes;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getTitleDescription() {
        return this.mTitleDescription;
    }

    void setTitleLargerTextSize(int fontLevel, int textSize) {
        this.mLargerFontLevel = fontLevel;
        this.mTitleLargerTextSize = textSize;
    }

    public void setIcon(Drawable icon) {
        if ((icon == null && this.mIcon != null) || (icon != null && this.mIcon != icon)) {
            this.mIcon = icon;
            notifyChanged();
        }
    }

    public void setIcon(int iconResId) {
        if (this.mIconResId != iconResId) {
            this.mIconResId = iconResId;
            setIcon(this.mContext.getDrawable(iconResId));
        }
    }

    public Drawable getIcon() {
        return this.mIcon;
    }

    public CharSequence getSummary() {
        return this.mSummary;
    }

    public CharSequence getSummaryDescription() {
        return this.mSummaryDescription;
    }

    public void setSummary(CharSequence summary) {
        if ((summary == null && this.mSummary != null) || (summary != null && !summary.equals(this.mSummary))) {
            this.mSummary = summary;
            notifyChanged();
        }
    }

    public void setSummary(int summaryResId) {
        setSummary(this.mContext.getString(summaryResId));
    }

    public void setSummaryDescription(CharSequence summaryDescription) {
        if ((summaryDescription == null && this.mSummaryDescription != null) || (summaryDescription != null && !summaryDescription.equals(this.mSummary))) {
            this.mSummaryDescription = summaryDescription;
            notifyChanged();
        }
    }

    public void setSummaryDescription(int summaryDescriptionResId) {
        setSummaryDescription(this.mContext.getString(summaryDescriptionResId));
    }

    public void setTwSummaryColorToColorPrimaryDark(boolean color) {
        this.mIsSummaryColorPrimaryDark = color;
    }

    private boolean getTwSummaryColorToColorPrimaryDark() {
        return this.mIsSummaryColorPrimaryDark;
    }

    public void setEnabled(boolean enabled) {
        if (this.mEnabled != enabled) {
            this.mEnabled = enabled;
            notifyDependencyChange(shouldDisableDependents());
            notifyChanged();
        }
    }

    public boolean isEnabled() {
        return this.mEnabled && this.mDependencyMet && this.mParentDependencyMet;
    }

    public void setSelectable(boolean selectable) {
        if (this.mSelectable != selectable) {
            this.mSelectable = selectable;
            notifyChanged();
        }
    }

    public boolean isSelectable() {
        return this.mSelectable;
    }

    public void setShouldDisableView(boolean shouldDisableView) {
        this.mShouldDisableView = shouldDisableView;
        notifyChanged();
    }

    public boolean getShouldDisableView() {
        return this.mShouldDisableView;
    }

    long getId() {
        return this.mId;
    }

    protected void onClick() {
    }

    public void setKey(String key) {
        this.mKey = key;
        if (this.mRequiresKey && !hasKey()) {
            requireKey();
        }
    }

    public String getKey() {
        return this.mKey;
    }

    void requireKey() {
        if (this.mKey == null) {
            throw new IllegalStateException("Preference does not have a key assigned.");
        }
        this.mRequiresKey = true;
    }

    public boolean hasKey() {
        return !TextUtils.isEmpty(this.mKey);
    }

    public boolean isPersistent() {
        return this.mPersistent;
    }

    protected boolean shouldPersist() {
        return this.mPreferenceManager != null && isPersistent() && hasKey();
    }

    public void setPersistent(boolean persistent) {
        this.mPersistent = persistent;
    }

    protected boolean callChangeListener(Object newValue) {
        return this.mOnChangeListener == null ? true : this.mOnChangeListener.onPreferenceChange(this, newValue);
    }

    public void setOnPreferenceChangeListener(OnPreferenceChangeListener onPreferenceChangeListener) {
        this.mOnChangeListener = onPreferenceChangeListener;
    }

    public OnPreferenceChangeListener getOnPreferenceChangeListener() {
        return this.mOnChangeListener;
    }

    public void setOnPreferenceClickListener(OnPreferenceClickListener onPreferenceClickListener) {
        this.mOnClickListener = onPreferenceClickListener;
    }

    public OnPreferenceClickListener getOnPreferenceClickListener() {
        return this.mOnClickListener;
    }

    public void performClick(PreferenceScreen preferenceScreen) {
        if (isEnabled()) {
            onClick();
            if (this.mOnClickListener == null || !this.mOnClickListener.onPreferenceClick(this)) {
                PreferenceManager preferenceManager = getPreferenceManager();
                if (preferenceManager != null) {
                    OnPreferenceTreeClickListener listener = preferenceManager.getOnPreferenceTreeClickListener();
                    if (!(preferenceScreen == null || listener == null || !listener.onPreferenceTreeClick(preferenceScreen, this))) {
                        return;
                    }
                }
                if (this.mIntent != null) {
                    getContext().startActivity(this.mIntent);
                }
            }
        }
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    public Context getContext() {
        return this.mContext;
    }

    public SharedPreferences getSharedPreferences() {
        if (this.mPreferenceManager == null) {
            return null;
        }
        return this.mPreferenceManager.getSharedPreferences();
    }

    public Editor getEditor() {
        if (this.mPreferenceManager == null) {
            return null;
        }
        return this.mPreferenceManager.getEditor();
    }

    public boolean shouldCommit() {
        if (this.mPreferenceManager == null) {
            return false;
        }
        return this.mPreferenceManager.shouldCommit();
    }

    public int compareTo(Preference another) {
        if (this.mOrder != another.mOrder) {
            return this.mOrder - another.mOrder;
        }
        if (this.mTitle == another.mTitle) {
            return 0;
        }
        if (this.mTitle == null) {
            return 1;
        }
        if (another.mTitle == null) {
            return -1;
        }
        return CharSequences.compareToIgnoreCase(this.mTitle, another.mTitle);
    }

    final void setOnPreferenceChangeInternalListener(OnPreferenceChangeInternalListener listener) {
        this.mListener = listener;
    }

    protected void notifyChanged() {
        if (this.mListener != null) {
            this.mListener.onPreferenceChange(this);
        }
    }

    protected void notifyHierarchyChanged() {
        if (this.mListener != null) {
            this.mListener.onPreferenceHierarchyChange(this);
        }
    }

    public PreferenceManager getPreferenceManager() {
        return this.mPreferenceManager;
    }

    protected void onAttachedToHierarchy(PreferenceManager preferenceManager) {
        this.mPreferenceManager = preferenceManager;
        this.mId = preferenceManager.getNextId();
        dispatchSetInitialValue();
    }

    protected void onAttachedToActivity() {
        registerDependency();
    }

    private void registerDependency() {
        if (!TextUtils.isEmpty(this.mDependencyKey)) {
            Preference preference = findPreferenceInHierarchy(this.mDependencyKey);
            if (preference != null) {
                preference.registerDependent(this);
                return;
            }
            throw new IllegalStateException("Dependency \"" + this.mDependencyKey + "\" not found for preference \"" + this.mKey + "\" (title: \"" + this.mTitle + "\"");
        }
    }

    private void unregisterDependency() {
        if (this.mDependencyKey != null) {
            Preference oldDependency = findPreferenceInHierarchy(this.mDependencyKey);
            if (oldDependency != null) {
                oldDependency.unregisterDependent(this);
            }
        }
    }

    protected Preference findPreferenceInHierarchy(String key) {
        if (TextUtils.isEmpty(key) || this.mPreferenceManager == null) {
            return null;
        }
        return this.mPreferenceManager.findPreference(key);
    }

    private void registerDependent(Preference dependent) {
        if (this.mDependents == null) {
            this.mDependents = new ArrayList();
        }
        this.mDependents.add(dependent);
        dependent.onDependencyChanged(this, shouldDisableDependents());
    }

    private void unregisterDependent(Preference dependent) {
        if (this.mDependents != null) {
            this.mDependents.remove(dependent);
        }
    }

    public void notifyDependencyChange(boolean disableDependents) {
        List<Preference> dependents = this.mDependents;
        if (dependents != null) {
            int dependentsCount = dependents.size();
            for (int i = 0; i < dependentsCount; i++) {
                ((Preference) dependents.get(i)).onDependencyChanged(this, disableDependents);
            }
        }
    }

    public void onDependencyChanged(Preference dependency, boolean disableDependent) {
        if (this.mDependencyMet == disableDependent) {
            this.mDependencyMet = !disableDependent;
            notifyDependencyChange(shouldDisableDependents());
            notifyChanged();
        }
    }

    public void onParentChanged(Preference parent, boolean disableChild) {
        if (this.mParentDependencyMet == disableChild) {
            this.mParentDependencyMet = !disableChild;
            notifyDependencyChange(shouldDisableDependents());
            notifyChanged();
        }
    }

    public boolean shouldDisableDependents() {
        return !isEnabled();
    }

    public void setDependency(String dependencyKey) {
        unregisterDependency();
        this.mDependencyKey = dependencyKey;
        registerDependency();
    }

    public String getDependency() {
        return this.mDependencyKey;
    }

    protected void onPrepareForRemoval() {
        unregisterDependency();
    }

    public void setDefaultValue(Object defaultValue) {
        this.mDefaultValue = defaultValue;
    }

    private void dispatchSetInitialValue() {
        if (shouldPersist() && getSharedPreferences().contains(this.mKey)) {
            onSetInitialValue(true, null);
        } else if (this.mDefaultValue != null) {
            onSetInitialValue(false, this.mDefaultValue);
        }
    }

    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
    }

    private void tryCommit(Editor editor) {
        if (this.mPreferenceManager.shouldCommit()) {
            try {
                editor.apply();
            } catch (AbstractMethodError e) {
                editor.commit();
            }
        }
    }

    protected boolean persistString(String value) {
        if (!shouldPersist()) {
            return false;
        }
        if (TextUtils.equals(value, getPersistedString(null))) {
            return true;
        }
        Editor editor = this.mPreferenceManager.getEditor();
        editor.putString(this.mKey, value);
        tryCommit(editor);
        return true;
    }

    protected String getPersistedString(String defaultReturnValue) {
        return !shouldPersist() ? defaultReturnValue : this.mPreferenceManager.getSharedPreferences().getString(this.mKey, defaultReturnValue);
    }

    protected boolean persistStringSet(Set<String> values) {
        if (!shouldPersist()) {
            return false;
        }
        if (values.equals(getPersistedStringSet(null))) {
            return true;
        }
        Editor editor = this.mPreferenceManager.getEditor();
        editor.putStringSet(this.mKey, values);
        tryCommit(editor);
        return true;
    }

    protected Set<String> getPersistedStringSet(Set<String> defaultReturnValue) {
        return !shouldPersist() ? defaultReturnValue : this.mPreferenceManager.getSharedPreferences().getStringSet(this.mKey, defaultReturnValue);
    }

    protected boolean persistInt(int value) {
        if (!shouldPersist()) {
            return false;
        }
        if (value == getPersistedInt(value ^ -1)) {
            return true;
        }
        Editor editor = this.mPreferenceManager.getEditor();
        editor.putInt(this.mKey, value);
        tryCommit(editor);
        return true;
    }

    protected int getPersistedInt(int defaultReturnValue) {
        return !shouldPersist() ? defaultReturnValue : this.mPreferenceManager.getSharedPreferences().getInt(this.mKey, defaultReturnValue);
    }

    protected boolean persistFloat(float value) {
        if (!shouldPersist()) {
            return false;
        }
        if (value == getPersistedFloat(Float.NaN)) {
            return true;
        }
        Editor editor = this.mPreferenceManager.getEditor();
        editor.putFloat(this.mKey, value);
        tryCommit(editor);
        return true;
    }

    protected float getPersistedFloat(float defaultReturnValue) {
        return !shouldPersist() ? defaultReturnValue : this.mPreferenceManager.getSharedPreferences().getFloat(this.mKey, defaultReturnValue);
    }

    protected boolean persistLong(long value) {
        if (!shouldPersist()) {
            return false;
        }
        if (value == getPersistedLong(-1 ^ value)) {
            return true;
        }
        Editor editor = this.mPreferenceManager.getEditor();
        editor.putLong(this.mKey, value);
        tryCommit(editor);
        return true;
    }

    protected long getPersistedLong(long defaultReturnValue) {
        return !shouldPersist() ? defaultReturnValue : this.mPreferenceManager.getSharedPreferences().getLong(this.mKey, defaultReturnValue);
    }

    protected boolean persistBoolean(boolean value) {
        boolean z = false;
        if (!shouldPersist()) {
            return false;
        }
        if (!value) {
            z = true;
        }
        if (value == getPersistedBoolean(z)) {
            return true;
        }
        Editor editor = this.mPreferenceManager.getEditor();
        editor.putBoolean(this.mKey, value);
        tryCommit(editor);
        return true;
    }

    protected boolean getPersistedBoolean(boolean defaultReturnValue) {
        return !shouldPersist() ? defaultReturnValue : this.mPreferenceManager.getSharedPreferences().getBoolean(this.mKey, defaultReturnValue);
    }

    boolean canRecycleLayout() {
        return this.mCanRecycleLayout || this.mTwForceRecycleLayout;
    }

    public void setForceRecycleLayout(boolean recycle) {
        this.mTwForceRecycleLayout = recycle;
    }

    public String toString() {
        return getFilterableStringBuilder().toString();
    }

    StringBuilder getFilterableStringBuilder() {
        StringBuilder sb = new StringBuilder();
        CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            sb.append(title).append(' ');
        }
        CharSequence summary = getSummary();
        if (!TextUtils.isEmpty(summary)) {
            sb.append(summary).append(' ');
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb;
    }

    public void saveHierarchyState(Bundle container) {
        dispatchSaveInstanceState(container);
    }

    void dispatchSaveInstanceState(Bundle container) {
        if (hasKey()) {
            this.mBaseMethodCalled = false;
            Parcelable state = onSaveInstanceState();
            if (!this.mBaseMethodCalled) {
                throw new IllegalStateException("Derived class did not call super.onSaveInstanceState()");
            } else if (state != null) {
                container.putParcelable(this.mKey, state);
            }
        }
    }

    protected Parcelable onSaveInstanceState() {
        this.mBaseMethodCalled = true;
        return BaseSavedState.EMPTY_STATE;
    }

    public void restoreHierarchyState(Bundle container) {
        dispatchRestoreInstanceState(container);
    }

    void dispatchRestoreInstanceState(Bundle container) {
        if (hasKey()) {
            Parcelable state = container.getParcelable(this.mKey);
            if (state != null) {
                this.mBaseMethodCalled = false;
                onRestoreInstanceState(state);
                if (!this.mBaseMethodCalled) {
                    throw new IllegalStateException("Derived class did not call super.onRestoreInstanceState()");
                }
            }
        }
    }

    protected void onRestoreInstanceState(Parcelable state) {
        this.mBaseMethodCalled = true;
        if (state != BaseSavedState.EMPTY_STATE && state != null) {
            throw new IllegalArgumentException("Wrong state class -- expecting Preference State");
        }
    }

    boolean hasRTL() {
        return this.mContext.getApplicationInfo().hasRtlSupport();
    }

    boolean isRTL() {
        if (this.mContext.getResources().getConfiguration().getLayoutDirection() == 1) {
            return true;
        }
        return false;
    }
}
