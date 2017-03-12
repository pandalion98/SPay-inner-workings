package android.preference;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.preference.GenericInflater.Parent;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;

public class PreferenceManager {
    public static final String KEY_HAS_SET_DEFAULT_VALUES = "_has_set_default_values";
    public static final String METADATA_KEY_PREFERENCES = "android.preference";
    private static final String TAG = "PreferenceManager";
    private Activity mActivity;
    private List<OnActivityDestroyListener> mActivityDestroyListeners;
    private List<OnActivityResultListener> mActivityResultListeners;
    private List<OnActivityStopListener> mActivityStopListeners;
    private Context mContext;
    private Editor mEditor;
    private PreferenceFragment mFragment;
    private long mNextId = 0;
    private int mNextRequestCode;
    private boolean mNoCommit;
    private OnPreferenceTreeClickListener mOnPreferenceTreeClickListener;
    private PreferenceScreen mPreferenceScreen;
    private List<DialogInterface> mPreferencesScreens;
    private SharedPreferences mSharedPreferences;
    private int mSharedPreferencesMode;
    private String mSharedPreferencesName;

    public interface OnActivityDestroyListener {
        void onActivityDestroy();
    }

    public interface OnPreferenceTreeClickListener {
        boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference);
    }

    public interface OnActivityResultListener {
        boolean onActivityResult(int i, int i2, Intent intent);
    }

    public interface OnActivityStopListener {
        void onActivityStop();
    }

    public PreferenceManager(Activity activity, int firstRequestCode) {
        this.mActivity = activity;
        this.mNextRequestCode = firstRequestCode;
        init(activity);
    }

    PreferenceManager(Context context) {
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setSharedPreferencesName(getDefaultSharedPreferencesName(context));
    }

    void setFragment(PreferenceFragment fragment) {
        this.mFragment = fragment;
    }

    PreferenceFragment getFragment() {
        return this.mFragment;
    }

    private List<ResolveInfo> queryIntentActivities(Intent queryIntent) {
        return this.mContext.getPackageManager().queryIntentActivities(queryIntent, 128);
    }

    PreferenceScreen inflateFromIntent(Intent queryIntent, PreferenceScreen rootPreferences) {
        List<ResolveInfo> activities = queryIntentActivities(queryIntent);
        HashSet<String> inflatedRes = new HashSet();
        for (int i = activities.size() - 1; i >= 0; i--) {
            ActivityInfo activityInfo = ((ResolveInfo) activities.get(i)).activityInfo;
            Bundle metaData = activityInfo.metaData;
            if (metaData != null && metaData.containsKey(METADATA_KEY_PREFERENCES)) {
                String uniqueResId = activityInfo.packageName + ":" + activityInfo.metaData.getInt(METADATA_KEY_PREFERENCES);
                if (!inflatedRes.contains(uniqueResId)) {
                    inflatedRes.add(uniqueResId);
                    try {
                        Context context = this.mContext.createPackageContext(activityInfo.packageName, 0);
                        PreferenceInflater inflater = new PreferenceInflater(context, this);
                        XmlResourceParser parser = activityInfo.loadXmlMetaData(context.getPackageManager(), METADATA_KEY_PREFERENCES);
                        rootPreferences = (PreferenceScreen) inflater.inflate((XmlPullParser) parser, (Parent) rootPreferences, true);
                        parser.close();
                    } catch (NameNotFoundException e) {
                        Log.w(TAG, "Could not create context for " + activityInfo.packageName + ": " + Log.getStackTraceString(e));
                    }
                }
            }
        }
        rootPreferences.onAttachedToHierarchy(this);
        return rootPreferences;
    }

    public PreferenceScreen inflateFromResource(Context context, int resId, PreferenceScreen rootPreferences) {
        setNoCommit(true);
        rootPreferences = (PreferenceScreen) new PreferenceInflater(context, this).inflate(resId, (Parent) rootPreferences, true);
        rootPreferences.onAttachedToHierarchy(this);
        setNoCommit(false);
        return rootPreferences;
    }

    public PreferenceScreen createPreferenceScreen(Context context) {
        PreferenceScreen preferenceScreen = new PreferenceScreen(context, null);
        preferenceScreen.onAttachedToHierarchy(this);
        return preferenceScreen;
    }

    long getNextId() {
        long j;
        synchronized (this) {
            j = this.mNextId;
            this.mNextId = 1 + j;
        }
        return j;
    }

    public String getSharedPreferencesName() {
        return this.mSharedPreferencesName;
    }

    public void setSharedPreferencesName(String sharedPreferencesName) {
        this.mSharedPreferencesName = sharedPreferencesName;
        this.mSharedPreferences = null;
    }

    public int getSharedPreferencesMode() {
        return this.mSharedPreferencesMode;
    }

    public void setSharedPreferencesMode(int sharedPreferencesMode) {
        this.mSharedPreferencesMode = sharedPreferencesMode;
        this.mSharedPreferences = null;
    }

    public SharedPreferences getSharedPreferences() {
        if (this.mSharedPreferences == null) {
            this.mSharedPreferences = this.mContext.getSharedPreferences(this.mSharedPreferencesName, this.mSharedPreferencesMode);
        }
        return this.mSharedPreferences;
    }

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return context.getSharedPreferences(getDefaultSharedPreferencesName(context), getDefaultSharedPreferencesMode());
    }

    private static String getDefaultSharedPreferencesName(Context context) {
        return context.getPackageName() + "_preferences";
    }

    private static int getDefaultSharedPreferencesMode() {
        return 0;
    }

    PreferenceScreen getPreferenceScreen() {
        return this.mPreferenceScreen;
    }

    boolean setPreferences(PreferenceScreen preferenceScreen) {
        if (preferenceScreen == this.mPreferenceScreen) {
            return false;
        }
        this.mPreferenceScreen = preferenceScreen;
        return true;
    }

    public Preference findPreference(CharSequence key) {
        if (this.mPreferenceScreen == null) {
            return null;
        }
        return this.mPreferenceScreen.findPreference(key);
    }

    public static void setDefaultValues(Context context, int resId, boolean readAgain) {
        setDefaultValues(context, getDefaultSharedPreferencesName(context), getDefaultSharedPreferencesMode(), resId, readAgain);
    }

    public static void setDefaultValues(Context context, String sharedPreferencesName, int sharedPreferencesMode, int resId, boolean readAgain) {
        SharedPreferences defaultValueSp = context.getSharedPreferences(KEY_HAS_SET_DEFAULT_VALUES, 0);
        if (readAgain || !defaultValueSp.getBoolean(KEY_HAS_SET_DEFAULT_VALUES, false)) {
            PreferenceManager pm = new PreferenceManager(context);
            pm.setSharedPreferencesName(sharedPreferencesName);
            pm.setSharedPreferencesMode(sharedPreferencesMode);
            pm.inflateFromResource(context, resId, null);
            Editor editor = defaultValueSp.edit().putBoolean(KEY_HAS_SET_DEFAULT_VALUES, true);
            try {
                editor.apply();
            } catch (AbstractMethodError e) {
                editor.commit();
            }
        }
    }

    Editor getEditor() {
        if (!this.mNoCommit) {
            return getSharedPreferences().edit();
        }
        if (this.mEditor == null) {
            this.mEditor = getSharedPreferences().edit();
        }
        return this.mEditor;
    }

    boolean shouldCommit() {
        return !this.mNoCommit;
    }

    private void setNoCommit(boolean noCommit) {
        if (!(noCommit || this.mEditor == null)) {
            try {
                this.mEditor.apply();
            } catch (AbstractMethodError e) {
                this.mEditor.commit();
            }
        }
        this.mNoCommit = noCommit;
    }

    Activity getActivity() {
        return this.mActivity;
    }

    Context getContext() {
        return this.mContext;
    }

    void registerOnActivityResultListener(OnActivityResultListener listener) {
        synchronized (this) {
            if (this.mActivityResultListeners == null) {
                this.mActivityResultListeners = new ArrayList();
            }
            if (!this.mActivityResultListeners.contains(listener)) {
                this.mActivityResultListeners.add(listener);
            }
        }
    }

    void unregisterOnActivityResultListener(OnActivityResultListener listener) {
        synchronized (this) {
            if (this.mActivityResultListeners != null) {
                this.mActivityResultListeners.remove(listener);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void dispatchActivityResult(int r5, int r6, android.content.Intent r7) {
        /*
        r4 = this;
        monitor-enter(r4);
        r3 = r4.mActivityResultListeners;	 Catch:{ all -> 0x0025 }
        if (r3 != 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r4);	 Catch:{ all -> 0x0025 }
    L_0x0006:
        return;
    L_0x0007:
        r2 = new java.util.ArrayList;	 Catch:{ all -> 0x0025 }
        r3 = r4.mActivityResultListeners;	 Catch:{ all -> 0x0025 }
        r2.<init>(r3);	 Catch:{ all -> 0x0025 }
        monitor-exit(r4);	 Catch:{ all -> 0x0025 }
        r0 = r2.size();
        r1 = 0;
    L_0x0014:
        if (r1 >= r0) goto L_0x0006;
    L_0x0016:
        r3 = r2.get(r1);
        r3 = (android.preference.PreferenceManager.OnActivityResultListener) r3;
        r3 = r3.onActivityResult(r5, r6, r7);
        if (r3 != 0) goto L_0x0006;
    L_0x0022:
        r1 = r1 + 1;
        goto L_0x0014;
    L_0x0025:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0025 }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.preference.PreferenceManager.dispatchActivityResult(int, int, android.content.Intent):void");
    }

    public void registerOnActivityStopListener(OnActivityStopListener listener) {
        synchronized (this) {
            if (this.mActivityStopListeners == null) {
                this.mActivityStopListeners = new ArrayList();
            }
            if (!this.mActivityStopListeners.contains(listener)) {
                this.mActivityStopListeners.add(listener);
            }
        }
    }

    public void unregisterOnActivityStopListener(OnActivityStopListener listener) {
        synchronized (this) {
            if (this.mActivityStopListeners != null) {
                this.mActivityStopListeners.remove(listener);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    void dispatchActivityStop() {
        /*
        r4 = this;
        monitor-enter(r4);
        r3 = r4.mActivityStopListeners;	 Catch:{ all -> 0x0022 }
        if (r3 != 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r4);	 Catch:{ all -> 0x0022 }
    L_0x0006:
        return;
    L_0x0007:
        r2 = new java.util.ArrayList;	 Catch:{ all -> 0x0022 }
        r3 = r4.mActivityStopListeners;	 Catch:{ all -> 0x0022 }
        r2.<init>(r3);	 Catch:{ all -> 0x0022 }
        monitor-exit(r4);	 Catch:{ all -> 0x0022 }
        r0 = r2.size();
        r1 = 0;
    L_0x0014:
        if (r1 >= r0) goto L_0x0006;
    L_0x0016:
        r3 = r2.get(r1);
        r3 = (android.preference.PreferenceManager.OnActivityStopListener) r3;
        r3.onActivityStop();
        r1 = r1 + 1;
        goto L_0x0014;
    L_0x0022:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0022 }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.preference.PreferenceManager.dispatchActivityStop():void");
    }

    void registerOnActivityDestroyListener(OnActivityDestroyListener listener) {
        synchronized (this) {
            if (this.mActivityDestroyListeners == null) {
                this.mActivityDestroyListeners = new ArrayList();
            }
            if (!this.mActivityDestroyListeners.contains(listener)) {
                this.mActivityDestroyListeners.add(listener);
            }
        }
    }

    void unregisterOnActivityDestroyListener(OnActivityDestroyListener listener) {
        synchronized (this) {
            if (this.mActivityDestroyListeners != null) {
                this.mActivityDestroyListeners.remove(listener);
            }
        }
    }

    void dispatchActivityDestroy() {
        List<OnActivityDestroyListener> list = null;
        synchronized (this) {
            if (this.mActivityDestroyListeners != null) {
                list = new ArrayList(this.mActivityDestroyListeners);
            }
        }
        if (list != null) {
            int N = list.size();
            for (int i = 0; i < N; i++) {
                ((OnActivityDestroyListener) list.get(i)).onActivityDestroy();
            }
        }
        if (View.TW_SCAFE_2016A && this.mPreferenceScreen != null) {
            this.mPreferenceScreen.unregisterReceiver();
        }
        dismissAllScreens();
    }

    int getNextRequestCode() {
        int i;
        synchronized (this) {
            i = this.mNextRequestCode;
            this.mNextRequestCode = i + 1;
        }
        return i;
    }

    void addPreferencesScreen(DialogInterface screen) {
        synchronized (this) {
            if (this.mPreferencesScreens == null) {
                this.mPreferencesScreens = new ArrayList();
            }
            this.mPreferencesScreens.add(screen);
        }
    }

    void removePreferencesScreen(DialogInterface screen) {
        synchronized (this) {
            if (this.mPreferencesScreens == null) {
                return;
            }
            this.mPreferencesScreens.remove(screen);
        }
    }

    void dispatchNewIntent(Intent intent) {
        dismissAllScreens();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void dismissAllScreens() {
        /*
        r3 = this;
        monitor-enter(r3);
        r2 = r3.mPreferencesScreens;	 Catch:{ all -> 0x0028 }
        if (r2 != 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r3);	 Catch:{ all -> 0x0028 }
    L_0x0006:
        return;
    L_0x0007:
        r1 = new java.util.ArrayList;	 Catch:{ all -> 0x0028 }
        r2 = r3.mPreferencesScreens;	 Catch:{ all -> 0x0028 }
        r1.<init>(r2);	 Catch:{ all -> 0x0028 }
        r2 = r3.mPreferencesScreens;	 Catch:{ all -> 0x0028 }
        r2.clear();	 Catch:{ all -> 0x0028 }
        monitor-exit(r3);	 Catch:{ all -> 0x0028 }
        r2 = r1.size();
        r0 = r2 + -1;
    L_0x001a:
        if (r0 < 0) goto L_0x0006;
    L_0x001c:
        r2 = r1.get(r0);
        r2 = (android.content.DialogInterface) r2;
        r2.dismiss();
        r0 = r0 + -1;
        goto L_0x001a;
    L_0x0028:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0028 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.preference.PreferenceManager.dismissAllScreens():void");
    }

    void setOnPreferenceTreeClickListener(OnPreferenceTreeClickListener listener) {
        this.mOnPreferenceTreeClickListener = listener;
    }

    OnPreferenceTreeClickListener getOnPreferenceTreeClickListener() {
        return this.mOnPreferenceTreeClickListener;
    }
}
