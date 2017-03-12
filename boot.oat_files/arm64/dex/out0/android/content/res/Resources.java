package android.content.res;

import android.R;
import android.accounts.AuthenticatorDescription;
import android.animation.Animator;
import android.animation.StateListAnimator;
import android.app.ActivityThread;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory.Options;
import android.graphics.Movie;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.Trace;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pools.SynchronizedPool;
import android.util.Slog;
import android.util.TypedValue;
import android.view.Display;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewHierarchyEncoder;
import com.android.internal.util.GrowingArrayUtils;
import com.android.internal.util.XmlUtils;
import com.samsung.android.multidisplay.common.UnRestrictedArrayList;
import com.samsung.android.multiwindow.MultiWindowFeatures;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Pattern;
import libcore.icu.NativePluralRules;
import org.xmlpull.v1.XmlPullParserException;

public class Resources {
    private static final String CACHE_NOT_THEMED = "";
    private static final String CACHE_NULL_THEME = "null_theme";
    private static int CONCURRENCY = (Runtime.getRuntime().availableProcessors() * 1);
    private static final boolean DEBUG_CONFIG = false;
    private static final boolean DEBUG_LOAD = false;
    private static final boolean DEBUG_RC = false;
    private static final int ID_OTHER = 16777220;
    private static final int LAYOUT_DIR_CONFIG = ActivityInfo.activityInfoConfigToNative(8192);
    private static final String NULL_THEME_KEY = "[2131623959, 2131623959, 2131623959, 2131623959]";
    static final String TAG = "Resources";
    private static final int THREAD_PRIORITY = -11;
    private static final boolean TRACE_FOR_MISS_PRELOAD = false;
    private static final boolean TRACE_FOR_PRELOAD = false;
    private static final String TYPE_APP_NAME = "AppName";
    private static final String TYPE_DEFAULT_VALUE = "Def=";
    private static final String TYPE_FORMATTED_STRING = "Formatted";
    private static final String TYPE_INT_ARRAY_POSITION = "IntPoz=";
    private static final String TYPE_MENU_ITEM_TITLE = "Menu";
    private static final String TYPE_PREFERENCES = "Pref";
    private static final String TYPE_QUANTITY = "Quantity=";
    private static final String TYPE_QUANTITY_ID_OTHER = "QuantityIdOther=";
    private static final String TYPE_STRING_ARRAY_POSITION = "StringPoz=";
    private static final String TYPE_TEXT = "Text";
    private static final String TYPE_TEXT_ARRAY_POSITION = "TextPoz=";
    private static Date date = new Date();
    private static int mStartStoringRL = 0;
    static Resources mSystem = null;
    static UnRestrictedArrayList<Resources> mSystems = null;
    private static final String mTAG = ("ResourceCaching " + date.toString());
    private static Map<CharSequence, List<CharSequence>> notificationStringsOriginMap;
    private static boolean sPreloaded;
    private static final LongSparseArray<ConstantState> sPreloadedColorDrawables = new LongSparseArray();
    private static final LongSparseArray<ConstantState<ColorStateList>> sPreloadedColorStateLists = new LongSparseArray();
    private static int sPreloadedDensity;
    private static final LongSparseArray<ConstantState>[] sPreloadedDrawables = new LongSparseArray[2];
    private static final Object sSync = new Object();
    private Map<CharSequence, List<CharSequence>> appAndWidgetStringNamesMap;
    private ExecutorService ecs;
    private volatile File file;
    private final ArrayMap<String, LongSparseArray<Future<ConstantState>>> mALDC;
    private final Object mAccessLock;
    private final ConfigurationBoundResourceCache<Animator> mAnimatorCache;
    public int mAppIconResId;
    final AssetManager mAssets;
    private final int[] mCachedXmlBlockIds;
    private final XmlBlock[] mCachedXmlBlocks;
    private final DrawableCache mColorDrawableCache;
    private final ConfigurationBoundResourceCache<ColorStateList> mColorStateListCache;
    private CompatibilityInfo mCompatibilityInfo;
    private final Configuration mConfiguration;
    private final DrawableCache mDrawableCache;
    private int mLastCachedXmlBlockIndex;
    final DisplayMetrics mMetrics;
    public String mPackageName;
    private NativePluralRules mPluralRule;
    private boolean mPreloading;
    public String mResPackageName;
    public int mResUserId;
    private final ConfigurationBoundResourceCache<StateListAnimator> mStateListAnimatorCache;
    private final Configuration mTmpConfig;
    private TypedValue mTmpValue;
    final SynchronizedPool<TypedArray> mTypedArrayPool;
    private List<TypedValue> mTypedValue;
    public int mUserId;
    private String refThemeKey;
    private Map<CharSequence, List<Integer>> resourcesMap;
    private Map<String, String> resourcesMapType;

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String name) {
            super(name);
        }
    }

    private static final class ProcessPriorityThreadFactory implements ThreadFactory {
        private final int threadPriority;

        public ProcessPriorityThreadFactory(int threadPriority) {
            this.threadPriority = threadPriority;
        }

        public Thread newThread(Runnable r) {
            return new Thread(r) {
                public void run() {
                    Process.setThreadPriority(ProcessPriorityThreadFactory.this.threadPriority);
                    super.run();
                }
            };
        }
    }

    private class RCCallable implements Callable<ConstantState> {
        String file;
        final long key;
        Resources res;
        TypedValue value;

        public RCCallable(TypedValue v, String f, Resources r, long k) {
            this.value = v;
            this.file = f;
            this.res = r;
            this.key = k;
        }

        public ConstantState call() throws Exception {
            Trace.traceBegin(8192, this.file);
            try {
                InputStream is = Resources.this.mAssets.openNonAsset(this.value.assetCookie, this.file, 2);
                Drawable dr = Drawable.createFromResourceStream(this.res, this.value, is, this.file, null);
                is.close();
                Trace.traceEnd(8192);
                if (dr == null) {
                    return null;
                }
                dr.setChangingConfigurations(this.value.changingConfigurations);
                return dr.getConstantState();
            } catch (Exception e) {
                Trace.traceEnd(8192);
                NotFoundException rnf = new NotFoundException("File " + this.file + " from drawable resource ID #0x" + Integer.toHexString(this.value.resourceId));
                rnf.initCause(e);
                throw rnf;
            }
        }
    }

    public final class Theme {
        private final AssetManager mAssets;
        private final ThemeKey mKey = new ThemeKey();
        private final long mTheme;
        private int mThemeResId = 0;

        public void applyStyle(int resId, boolean force) {
            AssetManager.applyThemeStyle(this.mTheme, resId, force);
            this.mThemeResId = resId;
            this.mKey.append(resId, force);
        }

        public void setTo(Theme other) {
            AssetManager.copyTheme(this.mTheme, other.mTheme);
            this.mThemeResId = other.mThemeResId;
            this.mKey.setTo(other.getKey());
        }

        public TypedArray obtainStyledAttributes(int[] attrs) {
            TypedArray array = TypedArray.obtain(Resources.this, attrs.length);
            array.mTheme = this;
            AssetManager.applyStyle(this.mTheme, 0, 0, 0, attrs, array.mData, array.mIndices);
            return array;
        }

        public TypedArray obtainStyledAttributes(int resid, int[] attrs) throws NotFoundException {
            TypedArray array = TypedArray.obtain(Resources.this, attrs.length);
            array.mTheme = this;
            AssetManager.applyStyle(this.mTheme, 0, resid, 0, attrs, array.mData, array.mIndices);
            return array;
        }

        public TypedArray obtainStyledAttributes(AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes) {
            TypedArray array = TypedArray.obtain(Resources.this, attrs.length);
            Parser parser = (Parser) set;
            AssetManager.applyStyle(this.mTheme, defStyleAttr, defStyleRes, parser != null ? parser.mParseState : 0, attrs, array.mData, array.mIndices);
            array.mTheme = this;
            array.mXml = parser;
            return array;
        }

        public TypedArray resolveAttributes(int[] values, int[] attrs) {
            int len = attrs.length;
            if (values == null || len != values.length) {
                throw new IllegalArgumentException("Base attribute values must the same length as attrs");
            }
            TypedArray array = TypedArray.obtain(Resources.this, len);
            AssetManager.resolveAttrs(this.mTheme, 0, 0, values, attrs, array.mData, array.mIndices);
            array.mTheme = this;
            array.mXml = null;
            return array;
        }

        public boolean resolveAttribute(int resid, TypedValue outValue, boolean resolveRefs) {
            return this.mAssets.getThemeValue(this.mTheme, resid, outValue, resolveRefs);
        }

        public int[] getAllAttributes() {
            return this.mAssets.getStyleAttributes(getAppliedStyleResId());
        }

        public Resources getResources() {
            return Resources.this;
        }

        public Drawable getDrawable(int id) throws NotFoundException {
            return Resources.this.getDrawable(id, this);
        }

        public int getChangingConfigurations() {
            return ActivityInfo.activityInfoConfigNativeToJava(AssetManager.getThemeChangingConfigurations(this.mTheme));
        }

        public void dump(int priority, String tag, String prefix) {
            AssetManager.dumpTheme(this.mTheme, priority, tag, prefix);
        }

        protected void finalize() throws Throwable {
            super.finalize();
            this.mAssets.releaseTheme(this.mTheme);
        }

        Theme() {
            this.mAssets = Resources.this.mAssets;
            this.mTheme = this.mAssets.createTheme();
        }

        long getNativeTheme() {
            return this.mTheme;
        }

        int getAppliedStyleResId() {
            return this.mThemeResId;
        }

        ThemeKey getKey() {
            return this.mKey;
        }

        private String getResourceNameFromHexString(String hexString) {
            return Resources.this.getResourceName(Integer.parseInt(hexString, 16));
        }

        @ExportedProperty(category = "theme", hasAdjacentMapping = true)
        public String[] getTheme() {
            int N = this.mKey.mCount;
            String[] themes = new String[(N * 2)];
            int i = 0;
            int j = N - 1;
            while (i < themes.length) {
                String str;
                int resId = this.mKey.mResId[j];
                boolean forced = this.mKey.mForce[j];
                try {
                    themes[i] = Resources.this.getResourceName(resId);
                } catch (NotFoundException e) {
                    themes[i] = Integer.toHexString(i);
                }
                int i2 = i + 1;
                if (forced) {
                    str = "forced";
                } else {
                    str = "not forced";
                }
                themes[i2] = str;
                i += 2;
                j--;
            }
            return themes;
        }

        public void encode(ViewHierarchyEncoder encoder) {
            encoder.beginObject(this);
            String[] properties = getTheme();
            for (int i = 0; i < properties.length; i += 2) {
                encoder.addProperty(properties[i], properties[i + 1]);
            }
            encoder.endObject();
        }

        public void rebase() {
            AssetManager.clearTheme(this.mTheme);
            for (int i = 0; i < this.mKey.mCount; i++) {
                AssetManager.applyThemeStyle(this.mTheme, this.mKey.mResId[i], this.mKey.mForce[i]);
            }
        }
    }

    static class ThemeKey implements Cloneable {
        int mCount;
        boolean[] mForce;
        private int mHashCode = 0;
        int[] mResId;

        ThemeKey() {
        }

        public void append(int resId, boolean force) {
            if (this.mResId == null) {
                this.mResId = new int[4];
            }
            if (this.mForce == null) {
                this.mForce = new boolean[4];
            }
            this.mResId = GrowingArrayUtils.append(this.mResId, this.mCount, resId);
            this.mForce = GrowingArrayUtils.append(this.mForce, this.mCount, force);
            this.mCount++;
            this.mHashCode = (force ? 1 : 0) + (((this.mHashCode * 31) + resId) * 31);
        }

        public void setTo(ThemeKey other) {
            boolean[] zArr = null;
            this.mResId = other.mResId == null ? null : (int[]) other.mResId.clone();
            if (other.mForce != null) {
                zArr = (boolean[]) other.mForce.clone();
            }
            this.mForce = zArr;
            this.mCount = other.mCount;
        }

        public int hashCode() {
            return this.mHashCode;
        }

        public String toString() {
            if (this.mResId == null || this.mResId.length <= 0) {
                return Resources.NULL_THEME_KEY;
            }
            return Arrays.toString(this.mResId) + "";
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass() || hashCode() != o.hashCode()) {
                return false;
            }
            ThemeKey t = (ThemeKey) o;
            if (this.mCount != t.mCount) {
                return false;
            }
            int N = this.mCount;
            int i = 0;
            while (i < N) {
                if (this.mResId[i] != t.mResId[i] || this.mForce[i] != t.mForce[i]) {
                    return false;
                }
                i++;
            }
            return true;
        }

        public ThemeKey clone() {
            ThemeKey other = new ThemeKey();
            other.mResId = this.mResId;
            other.mForce = this.mForce;
            other.mCount = this.mCount;
            other.mHashCode = this.mHashCode;
            return other;
        }
    }

    static {
        Map map = null;
        sPreloadedDrawables[0] = new LongSparseArray();
        sPreloadedDrawables[1] = new LongSparseArray();
        if (Build.IS_SYSTEM_SECURE) {
            map = new HashMap();
        }
        notificationStringsOriginMap = map;
    }

    private void addToMap(CharSequence value, int resid, String type) {
        synchronized (this.resourcesMap) {
            List<Integer> list = (List) this.resourcesMap.get(value);
            if (list != null) {
                for (Integer intValue : list) {
                    if (resid == intValue.intValue()) {
                        return;
                    }
                }
            }
            list = new ArrayList();
            String typKey = value.toString() + resid;
            list.add(Integer.valueOf(resid));
            this.resourcesMap.put(value, list);
            this.resourcesMapType.put(typKey, type);
        }
    }

    private void addToAppNamesMap(CharSequence value, CharSequence stringName) {
        synchronized (this.appAndWidgetStringNamesMap) {
            List<CharSequence> list = (List) this.appAndWidgetStringNamesMap.get(value);
            if (list != null) {
                for (CharSequence temp : list) {
                    try {
                        if (temp.equals(stringName)) {
                            return;
                        }
                    } catch (NullPointerException e) {
                    }
                }
            } else {
                list = new ArrayList();
            }
            list.add(stringName);
            this.appAndWidgetStringNamesMap.put(value, list);
        }
    }

    private void addToNotiMap(CharSequence value, CharSequence stringName) {
        synchronized (notificationStringsOriginMap) {
            List<CharSequence> list = (List) notificationStringsOriginMap.get(value);
            if (list != null) {
                for (CharSequence temp : list) {
                    if (temp.equals(stringName)) {
                        return;
                    }
                }
            }
            list = new ArrayList();
            list.add(stringName);
            notificationStringsOriginMap.put(value, list);
        }
    }

    private CharSequence searchFor(CharSequence search) {
        CharSequence stringNames = new StringBuilder();
        synchronized (this.resourcesMap) {
            List<Integer> list = (List) this.resourcesMap.get(search);
        }
        if (list != null) {
            for (Integer intValue : list) {
                int resid = intValue.intValue();
                StringBuilder names = new StringBuilder();
                names.append(getResourceName(resid));
                names.append(".");
                names.append((String) this.resourcesMapType.get(search.toString() + resid));
                names.append(";");
                stringNames.append(names);
            }
            searchAppAndWidgetStringNames(stringNames, search);
            searchNotificationStringName(stringNames, search);
        } else {
            synchronized (this.resourcesMap) {
                for (CharSequence key : this.resourcesMap.keySet()) {
                    if (key == null || !matchFormattedStringToKey(key.toString(), search.toString())) {
                        if (key != null && key.toString().equalsIgnoreCase(search.toString())) {
                            stringNames = searchFor(key);
                            break;
                        }
                    } else {
                        stringNames = searchFor(key);
                        break;
                    }
                }
                searchAppAndWidgetStringNames(stringNames, search);
                searchNotificationStringName(stringNames, search);
            }
        }
        return stringNames;
    }

    private void searchAppAndWidgetStringNames(StringBuilder stringNames, CharSequence search) {
        synchronized (this.appAndWidgetStringNamesMap) {
            List<CharSequence> list = (List) this.appAndWidgetStringNamesMap.get(search);
        }
        if (list != null) {
            for (CharSequence temp : list) {
                if (!(temp == null || stringNames.toString().contains(temp))) {
                    StringBuilder names = new StringBuilder();
                    names.append(temp);
                    names.append(".");
                    names.append(TYPE_APP_NAME);
                    names.append(";");
                    stringNames.append(names);
                }
            }
        }
    }

    private void searchNotificationStringName(StringBuilder stringNames, CharSequence search) {
        synchronized (notificationStringsOriginMap) {
            List<CharSequence> list = (List) notificationStringsOriginMap.get(search);
        }
        if (list != null) {
            for (CharSequence temp : list) {
                if (!(temp == null || stringNames.toString().contains(temp))) {
                    StringBuilder names = new StringBuilder();
                    names.append(temp);
                    names.append(";");
                    stringNames.append(names);
                }
            }
        }
    }

    private void loadAppNames(PackageManager pm) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        pm.queryIntentActivities(intent, 0);
        pm.queryIntentServices(new Intent("android.view.InputMethod"), 0);
    }

    private void loadNamesFromMetaData(String key, ResolveInfo ri, Context context) {
        Context c = null;
        try {
            c = context.createPackageContext(ri.activityInfo.packageName, 3);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (c != null) {
            Resources contextRes = c.getResources();
            XmlResourceParser parser = ri.activityInfo.loadXmlMetaData(c.getPackageManager(), key);
            if (parser != null) {
                int type;
                do {
                    try {
                        type = parser.next();
                        if (type == 2) {
                            break;
                        }
                    } catch (XmlPullParserException e2) {
                        e2.printStackTrace();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                } while (type != 1);
                int id = parser.getAttributeResourceValue(null, "description", 0);
                if (id != 0) {
                    addToAppNamesMap(contextRes.getString(id), contextRes.getResourceName(id));
                }
            }
        }
    }

    private CharSequence getStringName(ResolveInfo ri, PackageManager pm) {
        CharSequence stringName = null;
        if (ri.nonLocalizedLabel != null && ri.resolvePackageName != null) {
            return ri.resolvePackageName + ":noResource/";
        }
        if (!(ri.labelRes == 0 || ri.resolvePackageName == null)) {
            stringName = getStringName(ri.resolvePackageName, ri.labelRes, pm);
            if (stringName != null) {
                return stringName;
            }
        }
        ComponentInfo ci = ri.activityInfo != null ? ri.activityInfo : ri.serviceInfo;
        ApplicationInfo ai = ci.applicationInfo;
        if (ri.labelRes != 0) {
            stringName = getStringName(ci.packageName, ri.labelRes, pm);
            if (stringName != null) {
                return stringName;
            }
        }
        if (ci.nonLocalizedLabel != null && ci.packageName != null) {
            return ci.packageName + ":noResource/";
        }
        if (ci.labelRes != 0) {
            stringName = getStringName(ci.packageName, ci.labelRes, pm);
            if (stringName != null) {
                return stringName;
            }
        }
        if (ai.nonLocalizedLabel != null && ci.packageName != null) {
            return ci.packageName + ":noResource/";
        }
        if (!(ai.labelRes == 0 || ci.packageName == null)) {
            stringName = getStringName(ci.packageName, ai.labelRes, pm);
            if (stringName != null) {
                return stringName;
            }
        }
        return stringName;
    }

    private CharSequence getStringName(String packageName, int labelRes, PackageManager pm) {
        CharSequence stringName = null;
        if (!(packageName == null || labelRes == 0)) {
            try {
                stringName = pm.getResourcesForApplication(packageName).getResourceName(labelRes);
            } catch (NameNotFoundException e) {
                return null;
            }
        }
        return stringName;
    }

    private void resetStringNames(Configuration config) {
        if (Build.IS_SYSTEM_SECURE && this.mConfiguration != null && this.mConfiguration.locale != null && config != null && config.locale != null && !config.locale.equals(this.mConfiguration.locale)) {
            this.resourcesMap.clear();
            this.resourcesMapType.clear();
            this.appAndWidgetStringNamesMap.clear();
        }
    }

    private boolean matchFormattedStringToKey(String key, String formattedString) {
        if (key.isEmpty() || !key.contains("%")) {
            return false;
        }
        String[] splited = key.split("%d|%s|%[0-9]?[0-9]?" + Pattern.quote("$") + "[s,d]?");
        if (splited.length == 0) {
            return false;
        }
        for (String element : splited) {
            if (!formattedString.contains(element)) {
                return false;
            }
        }
        return true;
    }

    public CharSequence getStringNames(CharSequence text) {
        if (!Build.IS_SYSTEM_SECURE || text == null) {
            return null;
        }
        return searchFor(text);
    }

    public void addPreferenceString(CharSequence value, int resid) {
        if (Build.IS_SYSTEM_SECURE && resid != 0) {
            addToMap(value, resid, TYPE_PREFERENCES);
        }
    }

    public void addMenuItemTitle(TypedArray array) {
        if (Build.IS_SYSTEM_SECURE) {
            TypedValue itemTitle = array.peekValue(7);
            if (itemTitle != null && itemTitle.type == 3 && itemTitle.resourceId > 0) {
                addToMap(itemTitle.string, itemTitle.resourceId, TYPE_MENU_ITEM_TITLE);
            }
        }
    }

    public void addAppsNames(Context context, PackageManager pm, List<ResolveInfo> items) {
        if (Build.IS_SYSTEM_SECURE) {
            synchronized (this.appAndWidgetStringNamesMap) {
                boolean firstAdd = false;
                if (this.appAndWidgetStringNamesMap.isEmpty()) {
                    firstAdd = true;
                }
                for (ResolveInfo ri : items) {
                    addToAppNamesMap(ri.loadLabel(pm), getStringName(ri, pm));
                    if (ri.activityInfo != null) {
                        Bundle bundle = ri.activityInfo.metaData;
                        if (bundle != null) {
                            for (String loadNamesFromMetaData : bundle.keySet()) {
                                loadNamesFromMetaData(loadNamesFromMetaData, ri, context);
                            }
                        }
                    }
                }
                if (firstAdd) {
                    loadAppNames(pm);
                }
            }
        }
    }

    public void addAppsNames(Context context, List<AppWidgetProviderInfo> items) {
        if (Build.IS_SYSTEM_SECURE) {
            synchronized (this.appAndWidgetStringNamesMap) {
                boolean firstAdd = false;
                if (this.appAndWidgetStringNamesMap.isEmpty()) {
                    firstAdd = true;
                }
                PackageManager pm = context.getPackageManager();
                for (AppWidgetProviderInfo item : items) {
                    addToAppNamesMap(item.label, getStringName(item.provider.getPackageName(), item.labelRes, pm));
                }
                if (firstAdd) {
                    loadAppNames(pm);
                }
            }
        }
    }

    public void addAccountsNames(Context context, AuthenticatorDescription[] items) {
        if (Build.IS_SYSTEM_SECURE) {
            synchronized (this.appAndWidgetStringNamesMap) {
                boolean firstAdd = false;
                if (this.appAndWidgetStringNamesMap.isEmpty()) {
                    firstAdd = true;
                }
                PackageManager pm = context.getPackageManager();
                for (AuthenticatorDescription ad : items) {
                    int labelId = ad.labelId;
                    Resources res = null;
                    try {
                        res = pm.getResourcesForApplication(ad.packageName);
                    } catch (NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (!(res == null || labelId == 0)) {
                        addToAppNamesMap(res.getString(labelId), res.getResourceName(labelId));
                    }
                }
                if (firstAdd) {
                    loadAppNames(pm);
                }
            }
        }
    }

    public void addNotificationString(CharSequence title, CharSequence stringNames) {
        if (Build.IS_SYSTEM_SECURE) {
            CharSequence stringName = stringNames;
            if (stringName == null) {
                stringName = getStringNames(title);
            }
            if (stringName != null) {
                addToNotiMap(title, stringName);
            }
        }
    }

    private void addFormattedString(int id, String formattedString, Object... formatArgs) {
        addToMap(formattedString, id, TYPE_FORMATTED_STRING);
        for (Object arg : formatArgs) {
            if (arg != null) {
                List<Integer> resourceIdList = (List) this.resourcesMap.get(arg.toString());
                if (resourceIdList != null) {
                    for (Integer resId : resourceIdList) {
                        addToMap(formattedString, resId.intValue(), (String) this.resourcesMapType.get(arg.toString() + resId));
                    }
                }
            }
        }
    }

    public void startRC(Context context, String activityName, File rFile) {
        InterruptedException e;
        ExecutionException e2;
        RuntimeException e3;
        FileNotFoundException e4;
        IOException e5;
        Throwable th;
        this.ecs = Executors.newFixedThreadPool(CONCURRENCY, new ProcessPriorityThreadFactory(-11));
        BufferedReader in = null;
        this.file = rFile;
        try {
            if (this.file == null && context.getFilesDir() != null) {
                this.file = new File(context.getFilesDir(), "rList-" + activityName);
            }
            if (this.file != null && this.file.canRead() && mStartStoringRL == 0) {
                BufferedReader in2 = new BufferedReader(new InputStreamReader(new FileInputStream(this.file), "UTF-8"));
                while (true) {
                    try {
                        String line = in2.readLine();
                        if (line == null) {
                            break;
                        }
                        String[] arrayString = line.split("/");
                        int resourceId = Integer.parseInt(arrayString[0]);
                        if (arrayString.length == 1) {
                            this.refThemeKey = NULL_THEME_KEY;
                        } else {
                            this.refThemeKey = arrayString[1];
                        }
                        if (resourceId != 0) {
                            TypedValue mTmp = new TypedValue();
                            getValue(resourceId, mTmp, true);
                            if (mTmp.string != null) {
                                String file;
                                long key = (((long) mTmp.assetCookie) << 32) | ((long) mTmp.data);
                                ConstantState cs = (ConstantState) sPreloadedDrawables[this.mConfiguration.getLayoutDirection()].get(key);
                                if (cs != null) {
                                    file = mTmp.string.toString();
                                } else {
                                    file = mTmp.string.toString();
                                }
                                if (!(key < 0 || file == null || file.endsWith(".jpg") || file.endsWith(".xml"))) {
                                    LongSparseArray<Future<ConstantState>> mRCFutureDrawableCache = (LongSparseArray) this.mALDC.get(this.refThemeKey);
                                    if (mRCFutureDrawableCache == null) {
                                        LongSparseArray<Future<ConstantState>> mRCFutureDrawableCache2 = new LongSparseArray();
                                        try {
                                            this.mALDC.put(this.refThemeKey, mRCFutureDrawableCache2);
                                            mRCFutureDrawableCache = mRCFutureDrawableCache2;
                                        } catch (InterruptedException e6) {
                                            e = e6;
                                            mRCFutureDrawableCache = mRCFutureDrawableCache2;
                                            in = in2;
                                        } catch (ExecutionException e7) {
                                            e2 = e7;
                                            mRCFutureDrawableCache = mRCFutureDrawableCache2;
                                            in = in2;
                                        } catch (RuntimeException e8) {
                                            e3 = e8;
                                            mRCFutureDrawableCache = mRCFutureDrawableCache2;
                                            in = in2;
                                        } catch (FileNotFoundException e9) {
                                            e4 = e9;
                                            mRCFutureDrawableCache = mRCFutureDrawableCache2;
                                            in = in2;
                                        } catch (IOException e10) {
                                            e5 = e10;
                                            mRCFutureDrawableCache = mRCFutureDrawableCache2;
                                            in = in2;
                                        } catch (Throwable th2) {
                                            th = th2;
                                            mRCFutureDrawableCache = mRCFutureDrawableCache2;
                                            in = in2;
                                        }
                                    }
                                    Future<ConstantState> future = (Future) mRCFutureDrawableCache.get(key);
                                    if ((future == null || future.get() == null) && cs == null) {
                                        mRCFutureDrawableCache.append(key, this.ecs.submit(new RCCallable(mTmp, file, this, key)));
                                    }
                                }
                            } else {
                                continue;
                            }
                        }
                    } catch (InterruptedException e11) {
                        e = e11;
                        in = in2;
                    } catch (ExecutionException e12) {
                        e2 = e12;
                        in = in2;
                    } catch (RuntimeException e13) {
                        e3 = e13;
                        in = in2;
                    } catch (FileNotFoundException e14) {
                        e4 = e14;
                        in = in2;
                    } catch (IOException e15) {
                        e5 = e15;
                        in = in2;
                    } catch (Throwable th3) {
                        th = th3;
                        in = in2;
                    }
                }
                in = in2;
            }
            if (in != null) {
                try {
                    this.ecs.shutdown();
                    in.close();
                } catch (IOException e52) {
                    Slog.e(TAG, "IOException", e52);
                } catch (RuntimeException e32) {
                    Slog.e(TAG, "RuntimeException", e32);
                }
            }
        } catch (InterruptedException e16) {
            e = e16;
            try {
                Slog.e(TAG, "InterruptedException", e);
                if (in != null) {
                    try {
                        this.ecs.shutdown();
                        in.close();
                    } catch (IOException e522) {
                        Slog.e(TAG, "IOException", e522);
                    } catch (RuntimeException e322) {
                        Slog.e(TAG, "RuntimeException", e322);
                    }
                }
            } catch (Throwable th4) {
                th = th4;
                if (in != null) {
                    try {
                        this.ecs.shutdown();
                        in.close();
                    } catch (IOException e5222) {
                        Slog.e(TAG, "IOException", e5222);
                    } catch (RuntimeException e3222) {
                        Slog.e(TAG, "RuntimeException", e3222);
                    }
                }
                throw th;
            }
        } catch (ExecutionException e17) {
            e2 = e17;
            Slog.e(TAG, "ExecutionException", e2);
            if (in != null) {
                try {
                    this.ecs.shutdown();
                    in.close();
                } catch (IOException e52222) {
                    Slog.e(TAG, "IOException", e52222);
                } catch (RuntimeException e32222) {
                    Slog.e(TAG, "RuntimeException", e32222);
                }
            }
        } catch (RuntimeException e18) {
            e32222 = e18;
            Slog.e(TAG, "RunTimeException", e32222);
            if (in != null) {
                try {
                    this.ecs.shutdown();
                    in.close();
                } catch (IOException e522222) {
                    Slog.e(TAG, "IOException", e522222);
                } catch (RuntimeException e322222) {
                    Slog.e(TAG, "RuntimeException", e322222);
                }
            }
        } catch (FileNotFoundException e19) {
            e4 = e19;
            Slog.e(TAG, "FileNotFoundException" + activityName, e4);
            if (in != null) {
                try {
                    this.ecs.shutdown();
                    in.close();
                } catch (IOException e5222222) {
                    Slog.e(TAG, "IOException", e5222222);
                } catch (RuntimeException e3222222) {
                    Slog.e(TAG, "RuntimeException", e3222222);
                }
            }
        } catch (IOException e20) {
            e5222222 = e20;
            Slog.e(TAG, "IOException", e5222222);
            if (in != null) {
                try {
                    this.ecs.shutdown();
                    in.close();
                } catch (IOException e52222222) {
                    Slog.e(TAG, "IOException", e52222222);
                } catch (RuntimeException e32222222) {
                    Slog.e(TAG, "RuntimeException", e32222222);
                }
            }
        }
    }

    public void setRLRable(String activityName) {
        Boolean bool = Boolean.valueOf(false);
        if (activityName != null) {
            if (this.file == null && ActivityThread.currentApplication().getFilesDir() != null) {
                this.file = new File(ActivityThread.currentApplication().getFilesDir(), "rList-" + activityName);
            }
            if (this.file != null && this.file.exists() && this.file.canWrite()) {
                if (!Boolean.valueOf(this.file.setReadOnly()).booleanValue()) {
                    Log.e(TAG, "Read-only is not set");
                }
                mStartStoringRL = 0;
            }
        }
    }

    public static int selectDefaultTheme(int curTheme, int targetSdkVersion) {
        return selectSystemTheme(curTheme, targetSdkVersion, R.style.Theme, R.style.Theme_Holo, R.style.Theme_DeviceDefault, R.style.Theme_DeviceDefault_Light_DarkActionBar);
    }

    public static int selectSystemTheme(int curTheme, int targetSdkVersion, int orig, int holo, int dark, int deviceDefault) {
        if (curTheme != 0) {
            return curTheme;
        }
        if (targetSdkVersion < 11) {
            return orig;
        }
        if (targetSdkVersion < 14) {
            return holo;
        }
        if (targetSdkVersion < 10000) {
            return dark;
        }
        return deviceDefault;
    }

    public ConfigurationBoundResourceCache<Animator> getAnimatorCache() {
        return this.mAnimatorCache;
    }

    public ConfigurationBoundResourceCache<StateListAnimator> getStateListAnimatorCache() {
        return this.mStateListAnimatorCache;
    }

    public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        this(assets, metrics, config, CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO);
    }

    public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config, CompatibilityInfo compatInfo) {
        Map hashMap;
        this.refThemeKey = null;
        this.mALDC = new ArrayMap();
        this.mTypedValue = new ArrayList();
        this.file = null;
        this.mTypedArrayPool = new SynchronizedPool(5);
        this.mAccessLock = new Object();
        this.mTmpConfig = new Configuration();
        this.mDrawableCache = new DrawableCache(this);
        this.mColorDrawableCache = new DrawableCache(this);
        this.mColorStateListCache = new ConfigurationBoundResourceCache(this);
        this.mAnimatorCache = new ConfigurationBoundResourceCache(this);
        this.mStateListAnimatorCache = new ConfigurationBoundResourceCache(this);
        this.mTmpValue = new TypedValue();
        this.mLastCachedXmlBlockIndex = -1;
        this.mCachedXmlBlockIds = new int[]{0, 0, 0, 0};
        this.mCachedXmlBlocks = new XmlBlock[4];
        this.mMetrics = new DisplayMetrics();
        this.mConfiguration = new Configuration();
        this.mCompatibilityInfo = CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
        if (Build.IS_SYSTEM_SECURE) {
            hashMap = new HashMap();
        } else {
            hashMap = null;
        }
        this.resourcesMap = hashMap;
        if (Build.IS_SYSTEM_SECURE) {
            hashMap = new HashMap();
        } else {
            hashMap = null;
        }
        this.resourcesMapType = hashMap;
        if (Build.IS_SYSTEM_SECURE) {
            hashMap = new HashMap();
        } else {
            hashMap = null;
        }
        this.appAndWidgetStringNamesMap = hashMap;
        this.mPackageName = null;
        this.mResPackageName = null;
        this.mAppIconResId = 0;
        this.mUserId = 0;
        this.mAssets = assets;
        this.mMetrics.setToDefaults();
        if (compatInfo != null) {
            this.mCompatibilityInfo = compatInfo;
        }
        updateConfiguration(config, metrics);
        assets.ensureStringBlocks();
    }

    public static Resources getSystem() {
        Resources ret;
        synchronized (sSync) {
            ret = mSystem;
            if (ret == null) {
                ret = new Resources();
                mSystem = ret;
            }
        }
        return ret;
    }

    public static Resources getSystem(int displayId) {
        Resources ret;
        synchronized (sSync) {
            if (mSystems == null) {
                mSystems = new UnRestrictedArrayList();
            }
            ret = (Resources) mSystems.get(displayId);
            if (ret == null) {
                ret = new Resources();
                mSystems.set(displayId, ret);
            }
        }
        return ret;
    }

    public int isStoringRL() {
        return mStartStoringRL;
    }

    public void setRCable(int value, File fileName) {
        mStartStoringRL = value;
        this.file = fileName;
    }

    public CharSequence getText(int id) throws NotFoundException {
        CharSequence res = this.mAssets.getResourceText(id);
        if (res != null) {
            if (Build.IS_SYSTEM_SECURE) {
                addToMap(res, id, TYPE_TEXT);
            }
            return res;
        }
        throw new NotFoundException("String resource ID #0x" + Integer.toHexString(id));
    }

    public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {
        NativePluralRules rule = getPluralRule();
        CharSequence res = this.mAssets.getResourceBagText(id, attrForQuantityCode(rule.quantityForInt(quantity)));
        if (res != null) {
            if (Build.IS_SYSTEM_SECURE) {
                addToMap(res, id, TYPE_QUANTITY + stringForQuantityCode(quantity));
            }
            return res;
        }
        res = this.mAssets.getResourceBagText(id, ID_OTHER);
        if (res != null) {
            if (Build.IS_SYSTEM_SECURE) {
                addToMap(res, id, TYPE_QUANTITY_ID_OTHER + stringForQuantityCode(ID_OTHER));
            }
            return res;
        }
        throw new NotFoundException("Plural resource ID #0x" + Integer.toHexString(id) + " quantity=" + quantity + " item=" + stringForQuantityCode(rule.quantityForInt(quantity)));
    }

    private NativePluralRules getPluralRule() {
        NativePluralRules nativePluralRules;
        synchronized (sSync) {
            if (this.mPluralRule == null) {
                this.mPluralRule = NativePluralRules.forLocale(this.mConfiguration.locale);
            }
            nativePluralRules = this.mPluralRule;
        }
        return nativePluralRules;
    }

    private static int attrForQuantityCode(int quantityCode) {
        switch (quantityCode) {
            case 0:
                return 16777221;
            case 1:
                return 16777222;
            case 2:
                return 16777223;
            case 3:
                return 16777224;
            case 4:
                return 16777225;
            default:
                return ID_OTHER;
        }
    }

    private static String stringForQuantityCode(int quantityCode) {
        switch (quantityCode) {
            case 0:
                return "zero";
            case 1:
                return "one";
            case 2:
                return "two";
            case 3:
                return "few";
            case 4:
                return "many";
            default:
                return "other";
        }
    }

    public String getString(int id) throws NotFoundException {
        CharSequence res = getText(id);
        if (res != null) {
            return res.toString();
        }
        throw new NotFoundException("String resource ID #0x" + Integer.toHexString(id));
    }

    public String getString(int id, Object... formatArgs) throws NotFoundException {
        String formatted = String.format(this.mConfiguration.locale, getString(id), formatArgs);
        if (Build.IS_SYSTEM_SECURE) {
            addFormattedString(id, formatted, formatArgs);
        }
        return formatted;
    }

    public String getQuantityString(int id, int quantity, Object... formatArgs) throws NotFoundException {
        String formatted = String.format(this.mConfiguration.locale, getQuantityText(id, quantity).toString(), formatArgs);
        if (Build.IS_SYSTEM_SECURE) {
            addFormattedString(id, formatted, formatArgs);
        }
        return formatted;
    }

    public String getQuantityString(int id, int quantity) throws NotFoundException {
        return getQuantityText(id, quantity).toString();
    }

    public CharSequence getText(int id, CharSequence def) {
        CharSequence res = id != 0 ? this.mAssets.getResourceText(id) : null;
        if (!(!Build.IS_SYSTEM_SECURE || id == 0 || res == null)) {
            addToMap(res, id, TYPE_DEFAULT_VALUE + def);
        }
        if (res != null) {
            return res;
        }
        return def;
    }

    public CharSequence[] getTextArray(int id) throws NotFoundException {
        CharSequence[] res = this.mAssets.getResourceTextArray(id);
        if (res != null) {
            if (Build.IS_SYSTEM_SECURE && res.length > 0) {
                for (int i = 0; i < res.length; i++) {
                    addToMap(res[i], id, TYPE_TEXT_ARRAY_POSITION + i);
                }
            }
            return res;
        }
        throw new NotFoundException("Text array resource ID #0x" + Integer.toHexString(id));
    }

    public String[] getStringArray(int id) throws NotFoundException {
        String[] res = this.mAssets.getResourceStringArray(id);
        if (res != null) {
            if (Build.IS_SYSTEM_SECURE && res.length > 0) {
                for (int i = 0; i < res.length; i++) {
                    addToMap(res[i], id, TYPE_STRING_ARRAY_POSITION + i);
                }
            }
            return res;
        }
        throw new NotFoundException("String array resource ID #0x" + Integer.toHexString(id));
    }

    public int[] getIntArray(int id) throws NotFoundException {
        int[] res = this.mAssets.getArrayIntResource(id);
        if (res != null) {
            if (Build.IS_SYSTEM_SECURE && res.length > 0) {
                for (int i = 0; i < res.length; i++) {
                    addToMap(Integer.toString(res[i]), id, TYPE_INT_ARRAY_POSITION + i);
                }
            }
            return res;
        }
        throw new NotFoundException("Int array resource ID #0x" + Integer.toHexString(id));
    }

    public TypedArray obtainTypedArray(int id) throws NotFoundException {
        int len = this.mAssets.getArraySize(id);
        if (len < 0) {
            throw new NotFoundException("Array resource ID #0x" + Integer.toHexString(id));
        }
        TypedArray array = TypedArray.obtain(this, len);
        array.mLength = this.mAssets.retrieveArray(id, array.mData);
        array.mIndices[0] = 0;
        return array;
    }

    public float getDimension(int id) throws NotFoundException {
        float complexToDimension;
        synchronized (this.mAccessLock) {
            TypedValue value = this.mTmpValue;
            if (value == null) {
                value = new TypedValue();
                this.mTmpValue = value;
            }
            getValue(id, value, true);
            if (value.type == 5) {
                complexToDimension = TypedValue.complexToDimension(value.data, this.mMetrics);
            } else {
                throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id) + " type #0x" + Integer.toHexString(value.type) + " is not valid");
            }
        }
        return complexToDimension;
    }

    public int getDimensionPixelOffset(int id) throws NotFoundException {
        int complexToDimensionPixelOffset;
        synchronized (this.mAccessLock) {
            TypedValue value = this.mTmpValue;
            if (value == null) {
                value = new TypedValue();
                this.mTmpValue = value;
            }
            getValue(id, value, true);
            if (value.type == 5) {
                complexToDimensionPixelOffset = TypedValue.complexToDimensionPixelOffset(value.data, this.mMetrics);
            } else {
                throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id) + " type #0x" + Integer.toHexString(value.type) + " is not valid");
            }
        }
        return complexToDimensionPixelOffset;
    }

    public int getDimensionPixelSize(int id) throws NotFoundException {
        int complexToDimensionPixelSize;
        synchronized (this.mAccessLock) {
            TypedValue value = this.mTmpValue;
            if (value == null) {
                value = new TypedValue();
                this.mTmpValue = value;
            }
            getValue(id, value, true);
            if (value.type == 5) {
                complexToDimensionPixelSize = TypedValue.complexToDimensionPixelSize(value.data, this.mMetrics);
            } else {
                throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id) + " type #0x" + Integer.toHexString(value.type) + " is not valid");
            }
        }
        return complexToDimensionPixelSize;
    }

    public float getFraction(int id, int base, int pbase) {
        float complexToFraction;
        synchronized (this.mAccessLock) {
            TypedValue value = this.mTmpValue;
            if (value == null) {
                value = new TypedValue();
                this.mTmpValue = value;
            }
            getValue(id, value, true);
            if (value.type == 6) {
                complexToFraction = TypedValue.complexToFraction(value.data, (float) base, (float) pbase);
            } else {
                throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id) + " type #0x" + Integer.toHexString(value.type) + " is not valid");
            }
        }
        return complexToFraction;
    }

    @Deprecated
    public Drawable getDrawable(int id) throws NotFoundException {
        Drawable d = getDrawable(id, null);
        if (d != null && d.canApplyTheme()) {
            Log.w(TAG, "Drawable " + getResourceName(id) + " has unresolved theme " + "attributes! Consider using Resources.getDrawable(int, Theme) or " + "Context.getDrawable(int).", new RuntimeException());
        }
        return d;
    }

    public Drawable getDrawable(int id, Theme theme) throws NotFoundException {
        return getDrawable(id, theme, false);
    }

    public Drawable getDrawable(int id, Theme theme, boolean force) throws NotFoundException {
        TypedValue value;
        synchronized (this.mAccessLock) {
            value = this.mTmpValue;
            if (value == null) {
                value = new TypedValue();
            } else {
                this.mTmpValue = null;
            }
            getValue(id, value, true);
        }
        Drawable res = loadDrawable(value, id, theme, force);
        if (res != null) {
            res.setImagePath(value);
        }
        synchronized (this.mAccessLock) {
            if (this.mTmpValue == null) {
                this.mTmpValue = value;
            }
        }
        return res;
    }

    @Deprecated
    public Drawable getDrawableForDensity(int id, int density) throws NotFoundException {
        return getDrawableForDensity(id, density, null);
    }

    public Drawable getDrawableForDensity(int id, int density, Theme theme) {
        TypedValue value;
        synchronized (this.mAccessLock) {
            DisplayMetrics dm = this.mMetrics;
            value = this.mTmpValue;
            if (value == null) {
                value = new TypedValue();
            } else {
                this.mTmpValue = null;
            }
            getValueForDensity(id, density, value, true);
            if (value.density > 0 && value.density != 65535) {
                if (value.density == density) {
                    value.density = dm.densityDpi;
                } else {
                    value.density = (value.density * dm.densityDpi) / density;
                }
            }
        }
        Drawable res = loadDrawable(value, id, theme);
        if (res != null) {
            res.setImagePath(value);
        }
        synchronized (this.mAccessLock) {
            if (this.mTmpValue == null) {
                this.mTmpValue = value;
            }
        }
        return res;
    }

    public Movie getMovie(int id) throws NotFoundException {
        InputStream is = openRawResource(id);
        Movie movie = Movie.decodeStream(is);
        try {
            is.close();
        } catch (IOException e) {
        }
        return movie;
    }

    @Deprecated
    public int getColor(int id) throws NotFoundException {
        return getColor(id, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int getColor(int r7, android.content.res.Resources.Theme r8) throws android.content.res.Resources.NotFoundException {
        /*
        r6 = this;
        r3 = r6.mAccessLock;
        monitor-enter(r3);
        r1 = r6.mTmpValue;	 Catch:{ all -> 0x005a }
        if (r1 != 0) goto L_0x000c;
    L_0x0007:
        r1 = new android.util.TypedValue;	 Catch:{ all -> 0x005a }
        r1.<init>();	 Catch:{ all -> 0x005a }
    L_0x000c:
        r2 = 1;
        r6.getValue(r7, r1, r2);	 Catch:{ all -> 0x005a }
        r2 = r1.type;	 Catch:{ all -> 0x005a }
        r4 = 16;
        if (r2 < r4) goto L_0x0022;
    L_0x0016:
        r2 = r1.type;	 Catch:{ all -> 0x005a }
        r4 = 31;
        if (r2 > r4) goto L_0x0022;
    L_0x001c:
        r6.mTmpValue = r1;	 Catch:{ all -> 0x005a }
        r2 = r1.data;	 Catch:{ all -> 0x005a }
        monitor-exit(r3);	 Catch:{ all -> 0x005a }
    L_0x0021:
        return r2;
    L_0x0022:
        r2 = r1.type;	 Catch:{ all -> 0x005a }
        r4 = 3;
        if (r2 == r4) goto L_0x005d;
    L_0x0027:
        r2 = new android.content.res.Resources$NotFoundException;	 Catch:{ all -> 0x005a }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x005a }
        r4.<init>();	 Catch:{ all -> 0x005a }
        r5 = "Resource ID #0x";
        r4 = r4.append(r5);	 Catch:{ all -> 0x005a }
        r5 = java.lang.Integer.toHexString(r7);	 Catch:{ all -> 0x005a }
        r4 = r4.append(r5);	 Catch:{ all -> 0x005a }
        r5 = " type #0x";
        r4 = r4.append(r5);	 Catch:{ all -> 0x005a }
        r5 = r1.type;	 Catch:{ all -> 0x005a }
        r5 = java.lang.Integer.toHexString(r5);	 Catch:{ all -> 0x005a }
        r4 = r4.append(r5);	 Catch:{ all -> 0x005a }
        r5 = " is not valid";
        r4 = r4.append(r5);	 Catch:{ all -> 0x005a }
        r4 = r4.toString();	 Catch:{ all -> 0x005a }
        r2.<init>(r4);	 Catch:{ all -> 0x005a }
        throw r2;	 Catch:{ all -> 0x005a }
    L_0x005a:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x005a }
        throw r2;
    L_0x005d:
        r2 = 0;
        r6.mTmpValue = r2;	 Catch:{ all -> 0x005a }
        monitor-exit(r3);	 Catch:{ all -> 0x005a }
        r0 = r6.loadColorStateList(r1, r7, r8);
        r3 = r6.mAccessLock;
        monitor-enter(r3);
        r2 = r6.mTmpValue;	 Catch:{ all -> 0x0074 }
        if (r2 != 0) goto L_0x006e;
    L_0x006c:
        r6.mTmpValue = r1;	 Catch:{ all -> 0x0074 }
    L_0x006e:
        monitor-exit(r3);	 Catch:{ all -> 0x0074 }
        r2 = r0.getDefaultColor();
        goto L_0x0021;
    L_0x0074:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0074 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.Resources.getColor(int, android.content.res.Resources$Theme):int");
    }

    @Deprecated
    public ColorStateList getColorStateList(int id) throws NotFoundException {
        ColorStateList csl = getColorStateList(id, null);
        if (csl != null && csl.canApplyTheme()) {
            Log.w(TAG, "ColorStateList " + getResourceName(id) + " has " + "unresolved theme attributes! Consider using " + "Resources.getColorStateList(int, Theme) or " + "Context.getColorStateList(int).", new RuntimeException());
        }
        return csl;
    }

    public ColorStateList getColorStateList(int id, Theme theme) throws NotFoundException {
        TypedValue value;
        synchronized (this.mAccessLock) {
            value = this.mTmpValue;
            if (value == null) {
                value = new TypedValue();
            } else {
                this.mTmpValue = null;
            }
            getValue(id, value, true);
        }
        ColorStateList res = loadColorStateList(value, id, theme);
        synchronized (this.mAccessLock) {
            if (this.mTmpValue == null) {
                this.mTmpValue = value;
            }
        }
        return res;
    }

    public boolean getBoolean(int id) throws NotFoundException {
        boolean z = true;
        synchronized (this.mAccessLock) {
            TypedValue value = this.mTmpValue;
            if (value == null) {
                value = new TypedValue();
                this.mTmpValue = value;
            }
            getValue(id, value, true);
            if (value.type < 16 || value.type > 31) {
                throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id) + " type #0x" + Integer.toHexString(value.type) + " is not valid");
            }
            if (value.data == 0) {
                z = false;
            }
        }
        return z;
    }

    public int getInteger(int id) throws NotFoundException {
        int i;
        synchronized (this.mAccessLock) {
            TypedValue value = this.mTmpValue;
            if (value == null) {
                value = new TypedValue();
                this.mTmpValue = value;
            }
            getValue(id, value, true);
            if (value.type < 16 || value.type > 31) {
                throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id) + " type #0x" + Integer.toHexString(value.type) + " is not valid");
            }
            i = value.data;
        }
        return i;
    }

    public float getFloat(int id) {
        float f;
        synchronized (this.mAccessLock) {
            TypedValue value = this.mTmpValue;
            if (value == null) {
                value = new TypedValue();
                this.mTmpValue = value;
            }
            getValue(id, value, true);
            if (value.type == 4) {
                f = value.getFloat();
            } else {
                throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id) + " type #0x" + Integer.toHexString(value.type) + " is not valid");
            }
        }
        return f;
    }

    public XmlResourceParser getLayout(int id) throws NotFoundException {
        return loadXmlResourceParser(id, TtmlUtils.TAG_LAYOUT);
    }

    public XmlResourceParser getAnimation(int id) throws NotFoundException {
        return loadXmlResourceParser(id, "anim");
    }

    public XmlResourceParser getXml(int id) throws NotFoundException {
        return loadXmlResourceParser(id, "xml");
    }

    public InputStream openRawResource(int id) throws NotFoundException {
        TypedValue value;
        synchronized (this.mAccessLock) {
            value = this.mTmpValue;
            if (value == null) {
                value = new TypedValue();
            } else {
                this.mTmpValue = null;
            }
        }
        InputStream res = openRawResource(id, value);
        synchronized (this.mAccessLock) {
            if (this.mTmpValue == null) {
                this.mTmpValue = value;
            }
        }
        return res;
    }

    public InputStream openRawResource(int id, TypedValue value) throws NotFoundException {
        getValue(id, value, true);
        try {
            return this.mAssets.openNonAsset(value.assetCookie, value.string.toString(), 2);
        } catch (Exception e) {
            NotFoundException rnf = new NotFoundException("File " + value.string.toString() + " from drawable resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(e);
            throw rnf;
        }
    }

    public AssetFileDescriptor openRawResourceFd(int id) throws NotFoundException {
        synchronized (this.mAccessLock) {
            TypedValue value = this.mTmpValue;
            if (value == null) {
                value = new TypedValue();
            } else {
                this.mTmpValue = null;
            }
            getValue(id, value, true);
        }
        try {
            AssetFileDescriptor openNonAssetFd = this.mAssets.openNonAssetFd(value.assetCookie, value.string.toString());
            synchronized (this.mAccessLock) {
                if (this.mTmpValue == null) {
                    this.mTmpValue = value;
                }
            }
            return openNonAssetFd;
        } catch (Exception e) {
            NotFoundException rnf = new NotFoundException("File " + value.string.toString() + " from drawable resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(e);
            throw rnf;
        } catch (Throwable th) {
            synchronized (this.mAccessLock) {
                if (this.mTmpValue == null) {
                    this.mTmpValue = value;
                }
            }
        }
    }

    public void getValue(int id, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        if (!this.mAssets.getResourceValue(id, 0, outValue, resolveRefs)) {
            throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id));
        }
    }

    public void getValueForDensity(int id, int density, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        if (!this.mAssets.getResourceValue(id, density, outValue, resolveRefs)) {
            throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id));
        }
    }

    public void getValue(String name, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        int id = getIdentifier(name, "string", null);
        if (id != 0) {
            getValue(id, outValue, resolveRefs);
            return;
        }
        throw new NotFoundException("String resource name " + name);
    }

    public final Theme newTheme() {
        return new Theme();
    }

    public TypedArray obtainAttributes(AttributeSet set, int[] attrs) {
        TypedArray array = TypedArray.obtain(this, attrs.length);
        Parser parser = (Parser) set;
        this.mAssets.retrieveAttributes(parser.mParseState, attrs, array.mData, array.mIndices);
        array.mXml = parser;
        return array;
    }

    public void updateConfiguration(Configuration config, DisplayMetrics metrics) {
        updateConfiguration(config, metrics, null);
    }

    public void updateConfiguration(Configuration config, DisplayMetrics metrics, CompatibilityInfo compat) {
        synchronized (this.mAccessLock) {
            int width;
            int height;
            int keyboardHidden;
            resetStringNames(config);
            if (compat != null) {
                this.mCompatibilityInfo = compat;
            }
            if (metrics != null) {
                this.mMetrics.setTo(metrics);
            }
            this.mCompatibilityInfo.applyToDisplayMetrics(this.mMetrics);
            int configChanges = calcConfigChanges(config);
            if (this.mConfiguration.locale == null) {
                this.mConfiguration.locale = Locale.getDefault();
                this.mConfiguration.setLayoutDirection(this.mConfiguration.locale);
            }
            if (this.mConfiguration.densityDpi != 0) {
                this.mMetrics.densityDpi = this.mConfiguration.densityDpi;
                this.mMetrics.density = ((float) this.mConfiguration.densityDpi) * 0.00625f;
            }
            this.mMetrics.scaledDensity = this.mMetrics.density * this.mConfiguration.fontScale;
            String locale = null;
            if (this.mConfiguration.locale != null) {
                locale = adjustLanguageTag(this.mConfiguration.locale.toLanguageTag());
            }
            if (this.mMetrics.widthPixels >= this.mMetrics.heightPixels) {
                width = this.mMetrics.widthPixels;
                height = this.mMetrics.heightPixels;
            } else {
                width = this.mMetrics.heightPixels;
                height = this.mMetrics.widthPixels;
            }
            if (this.mConfiguration.keyboardHidden == 1 && this.mConfiguration.hardKeyboardHidden == 2) {
                keyboardHidden = 3;
            } else {
                keyboardHidden = this.mConfiguration.keyboardHidden;
            }
            this.mAssets.setConfiguration(this.mConfiguration.mcc, this.mConfiguration.mnc, locale, this.mConfiguration.orientation, this.mConfiguration.touchscreen, this.mConfiguration.densityDpi, this.mConfiguration.keyboard, keyboardHidden, this.mConfiguration.navigation, width, height, this.mConfiguration.smallestScreenWidthDp, this.mConfiguration.screenWidthDp, this.mConfiguration.screenHeightDp, this.mConfiguration.screenLayout, this.mConfiguration.uiMode, VERSION.RESOURCES_SDK_INT);
            this.mDrawableCache.onConfigurationChange(configChanges);
            this.mColorDrawableCache.onConfigurationChange(configChanges);
            this.mColorStateListCache.onConfigurationChange(configChanges);
            this.mAnimatorCache.onConfigurationChange(configChanges);
            this.mStateListAnimatorCache.onConfigurationChange(configChanges);
            mClearALDCLocked(this.mALDC, configChanges);
            flushLayoutCache();
        }
        synchronized (sSync) {
            if (this.mPluralRule != null) {
                this.mPluralRule = NativePluralRules.forLocale(config.locale);
            }
        }
    }

    public void updateConfiguration(Display display) {
        int displayId = display.getDisplayId();
        if (displayId >= 0 && displayId < 4) {
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            Configuration config = getConfiguration();
            config.displayId = displayId;
            config.densityDpi = metrics.densityDpi;
            config.screenWidthDp = (int) (((float) metrics.widthPixels) / metrics.density);
            config.screenHeightDp = (int) (((float) metrics.heightPixels) / metrics.density);
            updateConfiguration(config, metrics);
        }
    }

    private int calcConfigChanges(Configuration config) {
        if (config == null) {
            return 268435455;
        }
        this.mTmpConfig.setTo(config);
        int density = config.densityDpi;
        if (density == 0) {
            density = this.mMetrics.noncompatDensityDpi;
        }
        this.mCompatibilityInfo.applyToConfiguration(density, this.mTmpConfig);
        if (this.mTmpConfig.locale == null) {
            this.mTmpConfig.locale = Locale.getDefault();
            this.mTmpConfig.setLayoutDirection(this.mTmpConfig.locale);
        } else {
            String country = this.mTmpConfig.locale.getCountry();
            if ("ZG".equals(country)) {
                if (!"ZG".equals(Locale.getDefault().getCountry())) {
                    this.mTmpConfig.locale = Locale.MYANMAR_UNICODE;
                    this.mTmpConfig.setLayoutDirection(this.mTmpConfig.locale);
                }
            } else if ("MM".equals(country) && "ZG".equals(Locale.getDefault().getCountry())) {
                this.mTmpConfig.locale = Locale.MYANMAR_ZAWGYI;
                this.mTmpConfig.setLayoutDirection(this.mTmpConfig.locale);
            }
        }
        return ActivityInfo.activityInfoConfigToNative(this.mConfiguration.updateFrom(this.mTmpConfig));
    }

    private void mClearALDCLocked(ArrayMap<String, LongSparseArray<Future<ConstantState>>> caches, int configChanges) {
        if (caches != null) {
            int N = caches.size();
            for (int i = 0; i < N; i++) {
                if (caches.valueAt(i) != null) {
                    clearRCCachedDrawableLocked((LongSparseArray) caches.valueAt(i), configChanges);
                }
            }
        }
    }

    private void clearRCCachedDrawableLocked(LongSparseArray<Future<ConstantState>> futureCache, int configChanges) {
        int i = futureCache.size() - 1;
        while (i >= 0) {
            try {
                if (futureCache.valueAt(i) == null || !((Future) futureCache.valueAt(i)).isDone()) {
                    if (futureCache.valueAt(i) != null) {
                        futureCache.removeAt(i);
                    }
                    i--;
                } else {
                    ConstantState cs = (ConstantState) ((Future) futureCache.valueAt(i)).get();
                    if (cs != null && Configuration.needNewResources(configChanges, cs.getChangingConfigurations())) {
                        futureCache.removeAt(i);
                    }
                    i--;
                }
            } catch (InterruptedException e) {
                Slog.e(TAG, "InterruptedException", e);
            } catch (ExecutionException e2) {
                Slog.e(TAG, "ExecutionException", e2);
            }
        }
    }

    public void updateDisplayMetrics(DisplayMetrics metrics) {
        synchronized (this.mAccessLock) {
            if (metrics != null) {
                this.mMetrics.setTo(metrics);
            }
            this.mCompatibilityInfo.applyToDisplayMetrics(this.mMetrics);
        }
    }

    private void clearDrawableCachesLocked(ArrayMap<String, LongSparseArray<WeakReference<ConstantState>>> caches, int configChanges) {
        int N = caches.size();
        for (int i = 0; i < N; i++) {
            clearDrawableCacheLocked((LongSparseArray) caches.valueAt(i), configChanges);
        }
    }

    private void clearDrawableCacheLocked(LongSparseArray<WeakReference<ConstantState>> cache, int configChanges) {
        int N = cache.size();
        for (int i = 0; i < N; i++) {
            WeakReference<ConstantState> ref = (WeakReference) cache.valueAt(i);
            if (ref != null) {
                ConstantState cs = (ConstantState) ref.get();
                if (cs != null && Configuration.needNewResources(configChanges, cs.getChangingConfigurations())) {
                    cache.setValueAt(i, null);
                }
            }
        }
    }

    private static String adjustLanguageTag(String languageTag) {
        String language;
        String remainder;
        int separator = languageTag.indexOf(45);
        if (separator == -1) {
            language = languageTag;
            remainder = "";
        } else {
            language = languageTag.substring(0, separator);
            remainder = languageTag.substring(separator);
        }
        return Locale.adjustLanguageCode(language) + remainder;
    }

    public static void updateSystemConfiguration(Configuration config, DisplayMetrics metrics, CompatibilityInfo compat) {
        if (mSystem != null) {
            mSystem.updateConfiguration(config, metrics, compat);
        }
    }

    public DisplayMetrics getDisplayMetrics() {
        return this.mMetrics;
    }

    public Configuration getConfiguration() {
        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED) {
            try {
                ActivityThread am = ActivityThread.currentActivityThread();
                IBinder token = am != null ? am.getSuspectActivityToken() : null;
                if (am != null && am.isFixedOrientationCascade(token)) {
                    Configuration overrideConfig = am.getOverrideConfiguration(token);
                    if (overrideConfig != null) {
                        Configuration copyConfig = new Configuration(this.mConfiguration);
                        if (overrideConfig.orientation != 0) {
                            copyConfig.orientation = overrideConfig.orientation;
                        }
                        if (overrideConfig.screenWidthDp != 0) {
                            copyConfig.screenWidthDp = overrideConfig.screenWidthDp;
                        }
                        if (overrideConfig.screenHeightDp != 0) {
                            copyConfig.screenHeightDp = overrideConfig.screenHeightDp;
                        }
                        if (overrideConfig.smallestScreenWidthDp == 0) {
                            return copyConfig;
                        }
                        copyConfig.smallestScreenWidthDp = overrideConfig.smallestScreenWidthDp;
                        return copyConfig;
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            } catch (ClassCastException e2) {
            }
        }
        return this.mConfiguration;
    }

    public CompatibilityInfo getCompatibilityInfo() {
        return this.mCompatibilityInfo;
    }

    public void setCompatibilityInfo(CompatibilityInfo ci) {
        if (ci != null) {
            this.mCompatibilityInfo = ci;
            updateConfiguration(this.mConfiguration, this.mMetrics);
        }
    }

    public int getIdentifier(String name, String defType, String defPackage) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        try {
            return Integer.parseInt(name);
        } catch (Exception e) {
            return this.mAssets.getResourceIdentifier(name, defType, defPackage);
        }
    }

    public static boolean resourceHasPackage(int resid) {
        return (resid >>> 24) != 0;
    }

    public String getResourceName(int resid) throws NotFoundException {
        String str = this.mAssets.getResourceName(resid);
        if (str != null) {
            return str;
        }
        throw new NotFoundException("Unable to find resource ID #0x" + Integer.toHexString(resid));
    }

    public String getResourcePackageName(int resid) throws NotFoundException {
        String str = this.mAssets.getResourcePackageName(resid);
        if (str != null) {
            return str;
        }
        throw new NotFoundException("Unable to find resource ID #0x" + Integer.toHexString(resid));
    }

    public String getResourceTypeName(int resid) throws NotFoundException {
        String str = this.mAssets.getResourceTypeName(resid);
        if (str != null) {
            return str;
        }
        throw new NotFoundException("Unable to find resource ID #0x" + Integer.toHexString(resid));
    }

    public String getResourceEntryName(int resid) throws NotFoundException {
        String str = this.mAssets.getResourceEntryName(resid);
        if (str != null) {
            return str;
        }
        throw new NotFoundException("Unable to find resource ID #0x" + Integer.toHexString(resid));
    }

    public void parseBundleExtras(XmlResourceParser parser, Bundle outBundle) throws XmlPullParserException, IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int type = parser.next();
            if (type == 1) {
                return;
            }
            if (type == 3 && parser.getDepth() <= outerDepth) {
                return;
            }
            if (!(type == 3 || type == 4)) {
                if (parser.getName().equals("extra")) {
                    parseBundleExtra("extra", parser, outBundle);
                    XmlUtils.skipCurrentTag(parser);
                } else {
                    XmlUtils.skipCurrentTag(parser);
                }
            }
        }
    }

    public void parseBundleExtra(String tagName, AttributeSet attrs, Bundle outBundle) throws XmlPullParserException {
        boolean z = true;
        TypedArray sa = obtainAttributes(attrs, com.android.internal.R.styleable.Extra);
        String name = sa.getString(0);
        if (name == null) {
            sa.recycle();
            throw new XmlPullParserException("<" + tagName + "> requires an android:name attribute at " + attrs.getPositionDescription());
        }
        TypedValue v = sa.peekValue(1);
        if (v != null) {
            if (v.type == 3) {
                outBundle.putCharSequence(name, v.coerceToString());
            } else if (v.type == 18) {
                if (v.data == 0) {
                    z = false;
                }
                outBundle.putBoolean(name, z);
            } else if (v.type >= 16 && v.type <= 31) {
                outBundle.putInt(name, v.data);
            } else if (v.type == 4) {
                outBundle.putFloat(name, v.getFloat());
            } else {
                sa.recycle();
                throw new XmlPullParserException("<" + tagName + "> only supports string, integer, float, color, and boolean at " + attrs.getPositionDescription());
            }
            sa.recycle();
            return;
        }
        sa.recycle();
        throw new XmlPullParserException("<" + tagName + "> requires an android:value or android:resource attribute at " + attrs.getPositionDescription());
    }

    public final AssetManager getAssets() {
        return this.mAssets;
    }

    public final void flushLayoutCache() {
        synchronized (this.mCachedXmlBlockIds) {
            int num = this.mCachedXmlBlockIds.length;
            for (int i = 0; i < num; i++) {
                this.mCachedXmlBlockIds[i] = 0;
                XmlBlock oldBlock = this.mCachedXmlBlocks[i];
                if (oldBlock != null) {
                    oldBlock.close();
                }
                this.mCachedXmlBlocks[i] = null;
            }
        }
    }

    public final void startPreloading() {
        synchronized (sSync) {
            if (sPreloaded) {
                throw new IllegalStateException("Resources already preloaded");
            }
            sPreloaded = true;
            this.mPreloading = true;
            sPreloadedDensity = DisplayMetrics.DENSITY_DEVICE;
            this.mConfiguration.densityDpi = sPreloadedDensity;
            updateConfiguration(null, null);
        }
    }

    public final void finishPreloading() {
        if (this.mPreloading) {
            this.mPreloading = false;
            flushLayoutCache();
        }
    }

    public LongSparseArray<ConstantState> getPreloadedDrawables() {
        return sPreloadedDrawables[0];
    }

    private boolean verifyPreloadConfig(int changingConfigurations, int allowVarying, int resourceId, String name) {
        if (((-1073745921 & changingConfigurations) & (allowVarying ^ -1)) == 0) {
            return true;
        }
        String resName;
        try {
            resName = getResourceName(resourceId);
        } catch (NotFoundException e) {
            resName = "?";
        }
        Log.w(TAG, "Preloaded " + name + " resource #0x" + Integer.toHexString(resourceId) + " (" + resName + ") that varies with configuration!!");
        return false;
    }

    Drawable loadDrawable(TypedValue value, int id, Theme theme) throws NotFoundException {
        return loadDrawable(value, id, theme, false);
    }

    Drawable loadDrawable(TypedValue value, int id, Theme theme, boolean force) throws NotFoundException {
        boolean isColorDrawable;
        DrawableCache caches;
        long key;
        ConstantState cs;
        Drawable dr;
        boolean canApplyTheme;
        Throwable th;
        if (id == this.mAppIconResId && this.mPackageName != null) {
            try {
                byte[] imageData = EnterpriseDeviceManager.getInstance().getApplicationPolicy().getApplicationIconFromDb(this.mPackageName, this.mUserId);
                if (imageData != null) {
                    InputStream byteArrayInputStream = new ByteArrayInputStream(imageData);
                    TypedValue typedValue = new TypedValue();
                    typedValue.density = 0;
                    Options opts = new Options();
                    opts.inTargetDensity = getDisplayMetrics().densityDpi;
                    Drawable drw = Drawable.createFromResourceStream(this, typedValue, byteArrayInputStream, null, opts);
                    Log.i(TAG, "EDM:ApplicationIcon got from EDM database ");
                    return drw;
                }
            } catch (Exception e) {
                Log.w(TAG, "EDM: Get Icon EX: " + e);
            }
        }
        if (value.type < 28 || value.type > 31) {
            isColorDrawable = false;
            caches = this.mDrawableCache;
            key = (((long) value.assetCookie) << 32) | ((long) value.data);
        } else {
            isColorDrawable = true;
            caches = this.mColorDrawableCache;
            key = (long) value.data;
        }
        if (!this.mPreloading) {
            Drawable cachedDrawable = caches.getInstance(key, theme);
            if (cachedDrawable != null) {
                return cachedDrawable;
            }
            Drawable mDR = getRCCachedDrawable(this.mALDC, key, theme);
            if (mDR != null) {
                return mDR;
            }
            String mThemeKey = theme != null ? theme.getKey().toString() : NULL_THEME_KEY;
            if (mThemeKey.equals(NULL_THEME_KEY) && value != null && value.string == null) {
            }
            if (!(mStartStoringRL != 1 || value.string == null || value.string.toString().endsWith(".xml") || value.string.toString().endsWith("jpg") || ActivityThread.getCurrActivityFromThread() == null)) {
                FileWriter out = null;
                try {
                    if (this.file == null && ActivityThread.currentApplication().getFilesDir() != null) {
                        this.file = new File(ActivityThread.currentApplication().getFilesDir(), "rList-" + ActivityThread.getCurrActivityFromThread());
                    }
                    if (this.file != null && this.file.exists() && this.file.canWrite()) {
                        FileWriter fileWriter = new FileWriter(this.file, true);
                        try {
                            fileWriter.append(value.resourceId + "/" + mThemeKey + "\n");
                            fileWriter.close();
                            out = fileWriter;
                        } catch (IOException e2) {
                            out = fileWriter;
                            try {
                                Slog.e(TAG, "IOException");
                                if (out != null) {
                                    try {
                                        out.close();
                                    } catch (IOException e3) {
                                        Slog.e(TAG, " IOException" + e3);
                                    }
                                }
                                if (isColorDrawable) {
                                    cs = (ConstantState) sPreloadedColorDrawables.get(key);
                                } else {
                                    cs = (ConstantState) sPreloadedDrawables[this.mConfiguration.getLayoutDirection()].get(key);
                                }
                                if (cs == null) {
                                }
                                if (isColorDrawable) {
                                    dr = new ColorDrawable(value.data);
                                } else {
                                    dr = loadDrawableForCookie(value, id, null);
                                }
                                if (dr == null) {
                                }
                                dr = dr.mutate();
                                dr.applyTheme(theme);
                                dr.clearMutated();
                                if (dr != null) {
                                    dr.setChangingConfigurations(value.changingConfigurations);
                                    cacheDrawable(value, isColorDrawable, caches, theme, canApplyTheme, key, dr);
                                }
                                return dr;
                            } catch (Throwable th2) {
                                th = th2;
                                if (out != null) {
                                    try {
                                        out.close();
                                    } catch (IOException e32) {
                                        Slog.e(TAG, " IOException" + e32);
                                    }
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            out = fileWriter;
                            if (out != null) {
                                out.close();
                            }
                            throw th;
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e322) {
                            Slog.e(TAG, " IOException" + e322);
                        }
                    }
                } catch (IOException e4) {
                    Slog.e(TAG, "IOException");
                    if (out != null) {
                        out.close();
                    }
                    if (isColorDrawable) {
                        cs = (ConstantState) sPreloadedDrawables[this.mConfiguration.getLayoutDirection()].get(key);
                    } else {
                        cs = (ConstantState) sPreloadedColorDrawables.get(key);
                    }
                    if (cs == null) {
                    }
                    if (isColorDrawable) {
                        dr = loadDrawableForCookie(value, id, null);
                    } else {
                        dr = new ColorDrawable(value.data);
                    }
                    if (dr == null) {
                    }
                    dr = dr.mutate();
                    dr.applyTheme(theme);
                    dr.clearMutated();
                    if (dr != null) {
                        dr.setChangingConfigurations(value.changingConfigurations);
                        cacheDrawable(value, isColorDrawable, caches, theme, canApplyTheme, key, dr);
                    }
                    return dr;
                }
            }
        }
        if (isColorDrawable) {
            cs = (ConstantState) sPreloadedColorDrawables.get(key);
        } else {
            cs = (ConstantState) sPreloadedDrawables[this.mConfiguration.getLayoutDirection()].get(key);
        }
        if (cs == null && !force) {
            dr = cs.newDrawable(this);
        } else if (isColorDrawable) {
            dr = new ColorDrawable(value.data);
        } else {
            dr = loadDrawableForCookie(value, id, null);
        }
        canApplyTheme = dr == null && dr.canApplyTheme();
        if (canApplyTheme && theme != null) {
            dr = dr.mutate();
            dr.applyTheme(theme);
            dr.clearMutated();
        }
        if (dr != null) {
            dr.setChangingConfigurations(value.changingConfigurations);
            cacheDrawable(value, isColorDrawable, caches, theme, canApplyTheme, key, dr);
        }
        return dr;
    }

    private Drawable getRCCachedDrawable(ArrayMap<String, LongSparseArray<Future<ConstantState>>> drawableCache, long key, Theme theme) {
        String mThemeKey = theme != null ? theme.getKey().toString() : NULL_THEME_KEY;
        if (mThemeKey == null) {
            return null;
        }
        LongSparseArray<Future<ConstantState>> futureDrawableCache = (LongSparseArray) drawableCache.get(mThemeKey);
        if (futureDrawableCache != null) {
            try {
                Future<ConstantState> future = (Future) futureDrawableCache.get(key);
                if (future != null) {
                    ConstantState entry = (ConstantState) future.get();
                    if (entry != null) {
                        return entry.newDrawable(this);
                    }
                    futureDrawableCache.delete(key);
                }
            } catch (InterruptedException e) {
                Slog.e(TAG, "InterruptedException", e);
            } catch (ExecutionException e2) {
                Slog.e(TAG, "ExecutionException", e2);
            }
        }
        return null;
    }

    private void cacheDrawable(TypedValue value, boolean isColorDrawable, DrawableCache caches, Theme theme, boolean usesTheme, long key, Drawable dr) {
        ConstantState cs = dr.getConstantState();
        if (cs != null) {
            if (this.mPreloading) {
                int changingConfigs = cs.getChangingConfigurations();
                if (isColorDrawable) {
                    if (verifyPreloadConfig(changingConfigs, 0, value.resourceId, "drawable")) {
                        sPreloadedColorDrawables.put(key, cs);
                        return;
                    }
                    return;
                } else if (!verifyPreloadConfig(changingConfigs, LAYOUT_DIR_CONFIG, value.resourceId, "drawable")) {
                    return;
                } else {
                    if ((LAYOUT_DIR_CONFIG & changingConfigs) == 0) {
                        sPreloadedDrawables[0].put(key, cs);
                        sPreloadedDrawables[1].put(key, cs);
                        return;
                    }
                    sPreloadedDrawables[this.mConfiguration.getLayoutDirection()].put(key, cs);
                    return;
                }
            }
            synchronized (this.mAccessLock) {
                caches.put(key, theme, cs, usesTheme);
            }
        }
    }

    private Drawable loadDrawableForCookie(TypedValue value, int id, Theme theme) {
        if (value.string == null) {
            throw new NotFoundException("Resource \"" + getResourceName(id) + "\" (" + Integer.toHexString(id) + ") is not a Drawable (color or path): " + value);
        }
        String file = value.string.toString();
        Trace.traceBegin(8192, file);
        try {
            Drawable dr;
            if (file.endsWith(".xml")) {
                XmlResourceParser rp = loadXmlResourceParser(file, id, value.assetCookie, "drawable");
                dr = Drawable.createFromXml(this, rp, theme);
                rp.close();
            } else {
                InputStream is = this.mAssets.openNonAsset(value.assetCookie, file, 2);
                dr = Drawable.createFromResourceStream(this, value, is, file, null);
                is.close();
            }
            Trace.traceEnd(8192);
            return dr;
        } catch (Exception e) {
            Trace.traceEnd(8192);
            NotFoundException rnf = new NotFoundException("File " + file + " from drawable resource ID #0x" + Integer.toHexString(id));
            rnf.initCause(e);
            ArrayList<String> overlays = this.mAssets.getOverlays();
            if (overlays != null) {
                Iterator i$ = overlays.iterator();
                while (i$.hasNext()) {
                    Log.e(TAG, "Resources overlayPath = " + ((String) i$.next()));
                }
            } else {
                Log.e(TAG, "Resources overlays is NULL");
            }
            throw rnf;
        }
    }

    ColorStateList loadColorStateList(TypedValue value, int id, Theme theme) throws NotFoundException {
        long key = (((long) value.assetCookie) << 32) | ((long) value.data);
        ColorStateList csl;
        ConstantState<ColorStateList> factory;
        if (value.type < 28 || value.type > 31) {
            ConfigurationBoundResourceCache<ColorStateList> cache = this.mColorStateListCache;
            csl = (ColorStateList) cache.getInstance(key, theme);
            if (csl != null) {
                return csl;
            }
            factory = (ConstantState) sPreloadedColorStateLists.get(key);
            if (factory != null) {
                csl = (ColorStateList) factory.newInstance(this, theme);
            }
            if (csl == null) {
                csl = loadColorStateListForCookie(value, id, theme);
            }
            if (csl == null) {
                return csl;
            }
            if (!this.mPreloading) {
                cache.put(key, theme, csl.getConstantState());
                return csl;
            } else if (!verifyPreloadConfig(value.changingConfigurations, 0, value.resourceId, ColorsColumns.COLOR)) {
                return csl;
            } else {
                sPreloadedColorStateLists.put(key, csl.getConstantState());
                return csl;
            }
        }
        factory = (ConstantState) sPreloadedColorStateLists.get(key);
        if (factory != null) {
            return (ColorStateList) factory.newInstance();
        }
        csl = ColorStateList.valueOf(value.data);
        if (!this.mPreloading || !verifyPreloadConfig(value.changingConfigurations, 0, value.resourceId, ColorsColumns.COLOR)) {
            return csl;
        }
        sPreloadedColorStateLists.put(key, csl.getConstantState());
        return csl;
    }

    private ColorStateList loadColorStateListForCookie(TypedValue value, int id, Theme theme) {
        if (value.string == null) {
            throw new UnsupportedOperationException("Can't convert to color state list: type=0x" + value.type);
        }
        String file = value.string.toString();
        Trace.traceBegin(8192, file);
        if (file.endsWith(".xml")) {
            try {
                XmlResourceParser rp = loadXmlResourceParser(file, id, value.assetCookie, "colorstatelist");
                ColorStateList csl = ColorStateList.createFromXml(this, rp, theme);
                rp.close();
                Trace.traceEnd(8192);
                return csl;
            } catch (Exception e) {
                Trace.traceEnd(8192);
                NotFoundException rnf = new NotFoundException("File " + file + " from color state list resource ID #0x" + Integer.toHexString(id));
                rnf.initCause(e);
                throw rnf;
            }
        }
        Trace.traceEnd(8192);
        throw new NotFoundException("File " + file + " from drawable resource ID #0x" + Integer.toHexString(id) + ": .xml extension required");
    }

    XmlResourceParser loadXmlResourceParser(int id, String type) throws NotFoundException {
        XmlResourceParser loadXmlResourceParser;
        synchronized (this.mAccessLock) {
            TypedValue value = this.mTmpValue;
            if (value == null) {
                value = new TypedValue();
                this.mTmpValue = value;
            }
            getValue(id, value, true);
            if (value.type == 3) {
                loadXmlResourceParser = loadXmlResourceParser(value.string.toString(), id, value.assetCookie, type);
            } else {
                throw new NotFoundException("Resource ID #0x" + Integer.toHexString(id) + " type #0x" + Integer.toHexString(value.type) + " is not valid");
            }
        }
        return loadXmlResourceParser;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    android.content.res.XmlResourceParser loadXmlResourceParser(java.lang.String r12, int r13, int r14, java.lang.String r15) throws android.content.res.Resources.NotFoundException {
        /*
        r11 = this;
        if (r13 == 0) goto L_0x0089;
    L_0x0002:
        r9 = r11.mCachedXmlBlockIds;	 Catch:{ Exception -> 0x0053 }
        monitor-enter(r9);	 Catch:{ Exception -> 0x0053 }
        r8 = r11.mCachedXmlBlockIds;	 Catch:{ all -> 0x0050 }
        r3 = r8.length;	 Catch:{ all -> 0x0050 }
        r2 = 0;
    L_0x0009:
        if (r2 >= r3) goto L_0x0023;
    L_0x000b:
        r8 = r11.mCachedXmlBlockIds;	 Catch:{ all -> 0x0050 }
        r8 = r8[r2];	 Catch:{ all -> 0x0050 }
        if (r8 != r13) goto L_0x0020;
    L_0x0011:
        r8 = r11.mCachedXmlBlocks;	 Catch:{ all -> 0x0050 }
        r8 = r8[r2];	 Catch:{ all -> 0x0050 }
        r6 = r8.newParser();	 Catch:{ all -> 0x0050 }
        if (r6 == 0) goto L_0x001e;
    L_0x001b:
        r6.setFilePath(r12);	 Catch:{ all -> 0x0050 }
    L_0x001e:
        monitor-exit(r9);	 Catch:{ all -> 0x0050 }
    L_0x001f:
        return r6;
    L_0x0020:
        r2 = r2 + 1;
        goto L_0x0009;
    L_0x0023:
        r8 = r11.mAssets;	 Catch:{ all -> 0x0050 }
        r0 = r8.openXmlBlockAsset(r14, r12);	 Catch:{ all -> 0x0050 }
        if (r0 == 0) goto L_0x0088;
    L_0x002b:
        r8 = r11.mLastCachedXmlBlockIndex;	 Catch:{ all -> 0x0050 }
        r5 = r8 + 1;
        if (r5 < r3) goto L_0x0032;
    L_0x0031:
        r5 = 0;
    L_0x0032:
        r11.mLastCachedXmlBlockIndex = r5;	 Catch:{ all -> 0x0050 }
        r8 = r11.mCachedXmlBlocks;	 Catch:{ all -> 0x0050 }
        r4 = r8[r5];	 Catch:{ all -> 0x0050 }
        if (r4 == 0) goto L_0x003d;
    L_0x003a:
        r4.close();	 Catch:{ all -> 0x0050 }
    L_0x003d:
        r8 = r11.mCachedXmlBlockIds;	 Catch:{ all -> 0x0050 }
        r8[r5] = r13;	 Catch:{ all -> 0x0050 }
        r8 = r11.mCachedXmlBlocks;	 Catch:{ all -> 0x0050 }
        r8[r5] = r0;	 Catch:{ all -> 0x0050 }
        r6 = r0.newParser();	 Catch:{ all -> 0x0050 }
        if (r6 == 0) goto L_0x004e;
    L_0x004b:
        r6.setFilePath(r12);	 Catch:{ all -> 0x0050 }
    L_0x004e:
        monitor-exit(r9);	 Catch:{ all -> 0x0050 }
        goto L_0x001f;
    L_0x0050:
        r8 = move-exception;
        monitor-exit(r9);	 Catch:{ all -> 0x0050 }
        throw r8;	 Catch:{ Exception -> 0x0053 }
    L_0x0053:
        r1 = move-exception;
        r7 = new android.content.res.Resources$NotFoundException;
        r8 = new java.lang.StringBuilder;
        r8.<init>();
        r9 = "File ";
        r8 = r8.append(r9);
        r8 = r8.append(r12);
        r9 = " from xml type ";
        r8 = r8.append(r9);
        r8 = r8.append(r15);
        r9 = " resource ID #0x";
        r8 = r8.append(r9);
        r9 = java.lang.Integer.toHexString(r13);
        r8 = r8.append(r9);
        r8 = r8.toString();
        r7.<init>(r8);
        r7.initCause(r1);
        throw r7;
    L_0x0088:
        monitor-exit(r9);	 Catch:{ all -> 0x0050 }
    L_0x0089:
        r8 = new android.content.res.Resources$NotFoundException;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "File ";
        r9 = r9.append(r10);
        r9 = r9.append(r12);
        r10 = " from xml type ";
        r9 = r9.append(r10);
        r9 = r9.append(r15);
        r10 = " resource ID #0x";
        r9 = r9.append(r10);
        r10 = java.lang.Integer.toHexString(r13);
        r9 = r9.append(r10);
        r9 = r9.toString();
        r8.<init>(r9);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.Resources.loadXmlResourceParser(java.lang.String, int, int, java.lang.String):android.content.res.XmlResourceParser");
    }

    public static TypedArray obtainAttributes(Resources res, Theme theme, AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    public static void updateSystemDisplayMetrics(DisplayMetrics metrics) {
        if (mSystem != null) {
            mSystem.updateDisplayMetrics(metrics);
        }
    }

    private Resources() {
        Map hashMap;
        this.refThemeKey = null;
        this.mALDC = new ArrayMap();
        this.mTypedValue = new ArrayList();
        this.file = null;
        this.mTypedArrayPool = new SynchronizedPool(5);
        this.mAccessLock = new Object();
        this.mTmpConfig = new Configuration();
        this.mDrawableCache = new DrawableCache(this);
        this.mColorDrawableCache = new DrawableCache(this);
        this.mColorStateListCache = new ConfigurationBoundResourceCache(this);
        this.mAnimatorCache = new ConfigurationBoundResourceCache(this);
        this.mStateListAnimatorCache = new ConfigurationBoundResourceCache(this);
        this.mTmpValue = new TypedValue();
        this.mLastCachedXmlBlockIndex = -1;
        this.mCachedXmlBlockIds = new int[]{0, 0, 0, 0};
        this.mCachedXmlBlocks = new XmlBlock[4];
        this.mMetrics = new DisplayMetrics();
        this.mConfiguration = new Configuration();
        this.mCompatibilityInfo = CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
        if (Build.IS_SYSTEM_SECURE) {
            hashMap = new HashMap();
        } else {
            hashMap = null;
        }
        this.resourcesMap = hashMap;
        if (Build.IS_SYSTEM_SECURE) {
            hashMap = new HashMap();
        } else {
            hashMap = null;
        }
        this.resourcesMapType = hashMap;
        if (Build.IS_SYSTEM_SECURE) {
            hashMap = new HashMap();
        } else {
            hashMap = null;
        }
        this.appAndWidgetStringNamesMap = hashMap;
        this.mPackageName = null;
        this.mResPackageName = null;
        this.mAppIconResId = 0;
        this.mUserId = 0;
        this.mAssets = AssetManager.getSystem();
        this.mConfiguration.setToDefaults();
        this.mMetrics.setToDefaults();
        updateConfiguration(null, null);
        this.mAssets.ensureStringBlocks();
    }
}
