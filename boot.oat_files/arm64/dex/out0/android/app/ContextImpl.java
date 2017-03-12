package android.app;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.IContentProvider;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.storage.IMountService.Stub;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.sec.enterprise.kioskmode.KioskMode;
import android.util.AndroidRuntimeException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.view.Display;
import android.view.DisplayAdjustments;
import android.view.IWindowManager;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import com.samsung.android.multiwindow.MultiWindowStyle;
import com.sec.android.app.CscFeature;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class ContextImpl extends Context {
    private static final boolean DEBUG = false;
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final String TAG = "ContextImpl";
    private static final boolean isElasticEnabled = true;
    private static ArrayMap<String, ArrayMap<String, SharedPreferencesImpl>> sSharedPrefs;
    private final IBinder mActivityToken;
    private final String mBasePackageName;
    @GuardedBy("mSync")
    private File mCacheDir;
    @GuardedBy("mSync")
    private File mCodeCacheDir;
    private Object mConnectionManager3G = null;
    private Object mConnectionManager4G = null;
    private final ApplicationContentResolver mContentResolver;
    @GuardedBy("mSync")
    private File mDatabasesDir;
    private Display mDisplay;
    private final DisplayAdjustments mDisplayAdjustments = new DisplayAdjustments();
    @GuardedBy("mSync")
    private File[] mExternalCacheDirs;
    @GuardedBy("mSync")
    private File[] mExternalFilesDirs;
    @GuardedBy("mSync")
    private File[] mExternalMediaDirs;
    @GuardedBy("mSync")
    private File[] mExternalObbDirs;
    @GuardedBy("mSync")
    private File mFilesDir;
    final ActivityThread mMainThread;
    @GuardedBy("mSync")
    private File mNoBackupFilesDir;
    private final String mOpPackageName;
    private Context mOuterContext = this;
    final LoadedApk mPackageInfo;
    private PackageManager mPackageManager;
    @GuardedBy("mSync")
    private File mPreferencesDir;
    private Context mReceiverRestrictedContext = null;
    private Resources mResources;
    private final ResourcesManager mResourcesManager;
    private final boolean mRestricted;
    final Object[] mServiceCache = SystemServiceRegistry.createServiceCache();
    private final Object mSync = new Object();
    private Theme mTheme = null;
    private int mThemeResource = 0;
    private final UserHandle mUser;

    private static final class ApplicationContentResolver extends ContentResolver {
        private final ActivityThread mMainThread;
        private final UserHandle mUser;

        public ApplicationContentResolver(Context context, ActivityThread mainThread, UserHandle user) {
            super(context);
            this.mMainThread = (ActivityThread) Preconditions.checkNotNull(mainThread);
            this.mUser = (UserHandle) Preconditions.checkNotNull(user);
        }

        protected IContentProvider acquireProvider(Context context, String auth) {
            return this.mMainThread.acquireProvider(context, ContentProvider.getAuthorityWithoutUserId(auth), resolveUserIdFromAuthority(auth), true);
        }

        protected IContentProvider acquireExistingProvider(Context context, String auth) {
            return this.mMainThread.acquireExistingProvider(context, ContentProvider.getAuthorityWithoutUserId(auth), resolveUserIdFromAuthority(auth), true);
        }

        public boolean releaseProvider(IContentProvider provider) {
            return this.mMainThread.releaseProvider(provider, true);
        }

        protected IContentProvider acquireUnstableProvider(Context c, String auth) {
            return this.mMainThread.acquireProvider(c, ContentProvider.getAuthorityWithoutUserId(auth), resolveUserIdFromAuthority(auth), false);
        }

        public boolean releaseUnstableProvider(IContentProvider icp) {
            return this.mMainThread.releaseProvider(icp, false);
        }

        public void unstableProviderDied(IContentProvider icp) {
            this.mMainThread.handleUnstableProviderDied(icp.asBinder(), true);
        }

        public void appNotRespondingViaProvider(IContentProvider icp) {
            this.mMainThread.appNotRespondingViaProvider(icp.asBinder());
        }

        protected int resolveUserIdFromAuthority(String auth) {
            return ContentProvider.getUserIdFromAuthority(auth, this.mUser.getIdentifier());
        }
    }

    static ContextImpl getImpl(Context context) {
        while (context instanceof ContextWrapper) {
            Context nextContext = ((ContextWrapper) context).getBaseContext();
            if (nextContext == null) {
                break;
            }
            context = nextContext;
        }
        return (ContextImpl) context;
    }

    public AssetManager getAssets() {
        return getResources().getAssets();
    }

    public Resources getResources() {
        return this.mResources;
    }

    public PackageManager getPackageManager() {
        if (this.mPackageManager != null) {
            return this.mPackageManager;
        }
        IPackageManager pm = ActivityThread.getPackageManager();
        if (pm == null) {
            return null;
        }
        PackageManager applicationPackageManager = new ApplicationPackageManager(this, pm);
        this.mPackageManager = applicationPackageManager;
        return applicationPackageManager;
    }

    public ContentResolver getContentResolver() {
        return this.mContentResolver;
    }

    public Looper getMainLooper() {
        return this.mMainThread.getLooper();
    }

    public Context getApplicationContext() {
        return this.mPackageInfo != null ? this.mPackageInfo.getApplication() : this.mMainThread.getApplication();
    }

    public void setTheme(int resId) {
        if (this.mThemeResource != resId) {
            this.mThemeResource = resId;
            initializeTheme();
        }
    }

    public int getThemeResId() {
        return this.mThemeResource;
    }

    public Theme getTheme() {
        if (this.mTheme != null) {
            return this.mTheme;
        }
        this.mThemeResource = Resources.selectDefaultTheme(this.mThemeResource, getOuterContext().getApplicationInfo().targetSdkVersion);
        initializeTheme();
        return this.mTheme;
    }

    private void initializeTheme() {
        if (this.mTheme == null) {
            this.mTheme = this.mResources.newTheme();
        }
        this.mTheme.applyStyle(this.mThemeResource, true);
    }

    public ClassLoader getClassLoader() {
        return this.mPackageInfo != null ? this.mPackageInfo.getClassLoader() : ClassLoader.getSystemClassLoader();
    }

    public String getPackageName() {
        if (this.mPackageInfo != null) {
            return this.mPackageInfo.getPackageName();
        }
        return "android";
    }

    public String getBasePackageName() {
        return this.mBasePackageName != null ? this.mBasePackageName : getPackageName();
    }

    public String getOpPackageName() {
        return this.mOpPackageName != null ? this.mOpPackageName : getBasePackageName();
    }

    public ApplicationInfo getApplicationInfo() {
        if (this.mPackageInfo != null) {
            return this.mPackageInfo.getApplicationInfo();
        }
        throw new RuntimeException("Not supported in system context");
    }

    public String getPackageResourcePath() {
        if (this.mPackageInfo != null) {
            return this.mPackageInfo.getResDir();
        }
        throw new RuntimeException("Not supported in system context");
    }

    public String getPackageCodePath() {
        if (this.mPackageInfo != null) {
            return this.mPackageInfo.getAppDir();
        }
        throw new RuntimeException("Not supported in system context");
    }

    public File getSharedPrefsFile(String name) {
        return makeFilename(getPreferencesDir(), name + ".xml");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.content.SharedPreferences getSharedPreferences(java.lang.String r9, int r10) {
        /*
        r8 = this;
        r6 = android.app.ContextImpl.class;
        monitor-enter(r6);
        r5 = sSharedPrefs;	 Catch:{ all -> 0x0062 }
        if (r5 != 0) goto L_0x000e;
    L_0x0007:
        r5 = new android.util.ArrayMap;	 Catch:{ all -> 0x0062 }
        r5.<init>();	 Catch:{ all -> 0x0062 }
        sSharedPrefs = r5;	 Catch:{ all -> 0x0062 }
    L_0x000e:
        r0 = r8.getPackageName();	 Catch:{ all -> 0x0062 }
        r5 = sSharedPrefs;	 Catch:{ all -> 0x0062 }
        r1 = r5.get(r0);	 Catch:{ all -> 0x0062 }
        r1 = (android.util.ArrayMap) r1;	 Catch:{ all -> 0x0062 }
        if (r1 != 0) goto L_0x0026;
    L_0x001c:
        r1 = new android.util.ArrayMap;	 Catch:{ all -> 0x0062 }
        r1.<init>();	 Catch:{ all -> 0x0062 }
        r5 = sSharedPrefs;	 Catch:{ all -> 0x0062 }
        r5.put(r0, r1);	 Catch:{ all -> 0x0062 }
    L_0x0026:
        r5 = r8.mPackageInfo;	 Catch:{ all -> 0x0062 }
        r5 = r5.getApplicationInfo();	 Catch:{ all -> 0x0062 }
        r5 = r5.targetSdkVersion;	 Catch:{ all -> 0x0062 }
        r7 = 19;
        if (r5 >= r7) goto L_0x0037;
    L_0x0032:
        if (r9 != 0) goto L_0x0037;
    L_0x0034:
        r9 = "null";
    L_0x0037:
        r3 = r1.get(r9);	 Catch:{ all -> 0x0062 }
        r3 = (android.app.SharedPreferencesImpl) r3;	 Catch:{ all -> 0x0062 }
        if (r3 != 0) goto L_0x004e;
    L_0x003f:
        r2 = r8.getSharedPrefsFile(r9);	 Catch:{ all -> 0x0062 }
        r3 = new android.app.SharedPreferencesImpl;	 Catch:{ all -> 0x0062 }
        r3.<init>(r2, r10);	 Catch:{ all -> 0x0062 }
        r1.put(r9, r3);	 Catch:{ all -> 0x0062 }
        monitor-exit(r6);	 Catch:{ all -> 0x0062 }
        r4 = r3;
    L_0x004d:
        return r4;
    L_0x004e:
        monitor-exit(r6);	 Catch:{ all -> 0x0062 }
        r5 = r10 & 4;
        if (r5 != 0) goto L_0x005d;
    L_0x0053:
        r5 = r8.getApplicationInfo();
        r5 = r5.targetSdkVersion;
        r6 = 11;
        if (r5 >= r6) goto L_0x0060;
    L_0x005d:
        r3.startReloadIfChangedUnexpectedly();
    L_0x0060:
        r4 = r3;
        goto L_0x004d;
    L_0x0062:
        r5 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0062 }
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ContextImpl.getSharedPreferences(java.lang.String, int):android.content.SharedPreferences");
    }

    private File getPreferencesDir() {
        File file;
        synchronized (this.mSync) {
            if (this.mPreferencesDir == null) {
                this.mPreferencesDir = new File(getDataDirFile(), "shared_prefs");
            }
            file = this.mPreferencesDir;
        }
        return file;
    }

    public FileInputStream openFileInput(String name) throws FileNotFoundException {
        return new FileInputStream(makeFilename(getFilesDir(), name));
    }

    public FileOutputStream openFileOutput(String name, int mode) throws FileNotFoundException {
        boolean append;
        FileOutputStream fos;
        if ((32768 & mode) != 0) {
            append = true;
        } else {
            append = false;
        }
        File f = makeFilename(getFilesDir(), name);
        try {
            fos = new FileOutputStream(f, append);
            setFilePermissionsFromMode(f.getPath(), mode, 0);
            return fos;
        } catch (FileNotFoundException e) {
            File parent = f.getParentFile();
            parent.mkdir();
            FileUtils.setPermissions(parent.getPath(), 505, -1, -1);
            fos = new FileOutputStream(f, append);
            setFilePermissionsFromMode(f.getPath(), mode, 0);
            return fos;
        }
    }

    public boolean deleteFile(String name) {
        return makeFilename(getFilesDir(), name).delete();
    }

    private static File createFilesDirLocked(File file) {
        if (file.exists()) {
            return file;
        }
        if (file.mkdirs()) {
            FileUtils.setPermissions(file.getPath(), 505, -1, -1);
            return file;
        } else if (file.exists()) {
            return file;
        } else {
            Log.w(TAG, "Unable to create files subdir " + file.getPath());
            return null;
        }
    }

    public File getFilesDir() {
        File createFilesDirLocked;
        synchronized (this.mSync) {
            if (this.mFilesDir == null) {
                this.mFilesDir = new File(getDataDirFile(), "files");
            }
            createFilesDirLocked = createFilesDirLocked(this.mFilesDir);
        }
        return createFilesDirLocked;
    }

    public File getNoBackupFilesDir() {
        File createFilesDirLocked;
        synchronized (this.mSync) {
            if (this.mNoBackupFilesDir == null) {
                this.mNoBackupFilesDir = new File(getDataDirFile(), "no_backup");
            }
            createFilesDirLocked = createFilesDirLocked(this.mNoBackupFilesDir);
        }
        return createFilesDirLocked;
    }

    public File getExternalFilesDir(String type) {
        return getExternalFilesDirs(type)[0];
    }

    public File[] getExternalFilesDirs(String type) {
        File[] ensureDirsExistOrFilter;
        synchronized (this.mSync) {
            if (this.mExternalFilesDirs == null) {
                this.mExternalFilesDirs = Environment.buildExternalStorageAppFilesDirs(getPackageName());
            }
            File[] dirs = this.mExternalFilesDirs;
            if (type != null) {
                dirs = Environment.buildPaths(dirs, type);
            }
            ensureDirsExistOrFilter = ensureDirsExistOrFilter(dirs);
        }
        return ensureDirsExistOrFilter;
    }

    public File getObbDir() {
        return getObbDirs()[0];
    }

    public File[] getObbDirs() {
        File[] ensureDirsExistOrFilter;
        synchronized (this.mSync) {
            if (this.mExternalObbDirs == null) {
                this.mExternalObbDirs = Environment.buildExternalStorageAppObbDirs(getPackageName());
            }
            ensureDirsExistOrFilter = ensureDirsExistOrFilter(this.mExternalObbDirs);
        }
        return ensureDirsExistOrFilter;
    }

    public File getCacheDir() {
        File createFilesDirLocked;
        synchronized (this.mSync) {
            if (this.mCacheDir == null) {
                this.mCacheDir = new File(getDataDirFile(), "cache");
            }
            createFilesDirLocked = createFilesDirLocked(this.mCacheDir);
        }
        return createFilesDirLocked;
    }

    public File getCodeCacheDir() {
        File createFilesDirLocked;
        synchronized (this.mSync) {
            if (this.mCodeCacheDir == null) {
                this.mCodeCacheDir = new File(getDataDirFile(), "code_cache");
            }
            createFilesDirLocked = createFilesDirLocked(this.mCodeCacheDir);
        }
        return createFilesDirLocked;
    }

    public File getExternalCacheDir() {
        return getExternalCacheDirs()[0];
    }

    public File[] getExternalCacheDirs() {
        File[] ensureDirsExistOrFilter;
        synchronized (this.mSync) {
            if (this.mExternalCacheDirs == null) {
                this.mExternalCacheDirs = Environment.buildExternalStorageAppCacheDirs(getPackageName());
            }
            ensureDirsExistOrFilter = ensureDirsExistOrFilter(this.mExternalCacheDirs);
        }
        return ensureDirsExistOrFilter;
    }

    public File[] getExternalMediaDirs() {
        File[] ensureDirsExistOrFilter;
        synchronized (this.mSync) {
            if (this.mExternalMediaDirs == null) {
                this.mExternalMediaDirs = Environment.buildExternalStorageAppMediaDirs(getPackageName());
            }
            ensureDirsExistOrFilter = ensureDirsExistOrFilter(this.mExternalMediaDirs);
        }
        return ensureDirsExistOrFilter;
    }

    public File getFileStreamPath(String name) {
        return makeFilename(getFilesDir(), name);
    }

    public String[] fileList() {
        String[] list = getFilesDir().list();
        return list != null ? list : EMPTY_STRING_ARRAY;
    }

    public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory) {
        return openOrCreateDatabase(name, mode, factory, null);
    }

    public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory, DatabaseErrorHandler errorHandler) {
        File f = validateFilePath(name, true);
        int flags = 268435456;
        if ((mode & 8) != 0) {
            flags = 268435456 | 536870912;
        }
        SQLiteDatabase db = SQLiteDatabase.openDatabase(f.getPath(), factory, flags, errorHandler);
        setFilePermissionsFromMode(f.getPath(), mode, 0);
        return db;
    }

    public boolean deleteDatabase(String name) {
        boolean z = false;
        try {
            z = SQLiteDatabase.deleteDatabase(validateFilePath(name, false));
        } catch (Exception e) {
        }
        return z;
    }

    public File getDatabasePath(String name) {
        return validateFilePath(name, false);
    }

    public String[] databaseList() {
        String[] list = getDatabasesDir().list();
        return list != null ? list : EMPTY_STRING_ARRAY;
    }

    private File getDatabasesDir() {
        File file;
        synchronized (this.mSync) {
            if (this.mDatabasesDir == null) {
                this.mDatabasesDir = new File(getDataDirFile(), "databases");
            }
            if (this.mDatabasesDir.getPath().equals("databases")) {
                this.mDatabasesDir = new File("/data/system");
            }
            file = this.mDatabasesDir;
        }
        return file;
    }

    @Deprecated
    public Drawable getWallpaper() {
        return getWallpaperManager().getDrawable();
    }

    @Deprecated
    public Drawable peekWallpaper() {
        return getWallpaperManager().peekDrawable();
    }

    @Deprecated
    public int getWallpaperDesiredMinimumWidth() {
        return getWallpaperManager().getDesiredMinimumWidth();
    }

    @Deprecated
    public int getWallpaperDesiredMinimumHeight() {
        return getWallpaperManager().getDesiredMinimumHeight();
    }

    @Deprecated
    public void setWallpaper(Bitmap bitmap) throws IOException {
        getWallpaperManager().setBitmap(bitmap);
    }

    @Deprecated
    public void setWallpaper(InputStream data) throws IOException {
        getWallpaperManager().setStream(data);
    }

    @Deprecated
    public void clearWallpaper() throws IOException {
        getWallpaperManager().clear();
    }

    private WallpaperManager getWallpaperManager() {
        return (WallpaperManager) getSystemService(WallpaperManager.class);
    }

    public void startActivity(Intent intent) {
        warnIfCallingFromSystemProcess();
        startActivity(intent, null);
    }

    public void startActivityAsUser(Intent intent, UserHandle user) {
        startActivityAsUser(intent, null, user);
    }

    public void startActivity(Intent intent, Bundle options) {
        warnIfCallingFromSystemProcess();
        if ((intent.getFlags() & 268435456) == 0) {
            throw new AndroidRuntimeException("Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?");
        }
        this.mMainThread.getInstrumentation().execStartActivity(getOuterContext(), this.mMainThread.getApplicationThread(), null, (Activity) null, intent, -1, options);
    }

    public void startActivityAsUser(Intent intent, Bundle options, UserHandle user) {
        try {
            ActivityManagerNative.getDefault().startActivityAsUser(this.mMainThread.getApplicationThread(), getBasePackageName(), intent, intent.resolveTypeIfNeeded(getContentResolver()), null, null, 0, 268435456, null, options, user.getIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public void startActivities(Intent[] intents) {
        warnIfCallingFromSystemProcess();
        startActivities(intents, null);
    }

    public void startActivitiesAsUser(Intent[] intents, Bundle options, UserHandle userHandle) {
        if ((intents[0].getFlags() & 268435456) == 0) {
            throw new AndroidRuntimeException("Calling startActivities() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag on first Intent. Is this really what you want?");
        }
        this.mMainThread.getInstrumentation().execStartActivitiesAsUser(getOuterContext(), this.mMainThread.getApplicationThread(), null, (Activity) null, intents, options, userHandle.getIdentifier());
    }

    public void startActivities(Intent[] intents, Bundle options) {
        warnIfCallingFromSystemProcess();
        if ((intents[0].getFlags() & 268435456) == 0) {
            throw new AndroidRuntimeException("Calling startActivities() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag on first Intent. Is this really what you want?");
        }
        this.mMainThread.getInstrumentation().execStartActivities(getOuterContext(), this.mMainThread.getApplicationThread(), null, (Activity) null, intents, options);
    }

    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws SendIntentException {
        startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, null);
    }

    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
        String resolvedType = null;
        if (fillInIntent != null) {
            try {
                fillInIntent.migrateExtraStreamToClipData();
                fillInIntent.prepareToLeaveProcess();
                resolvedType = fillInIntent.resolveTypeIfNeeded(getContentResolver());
            } catch (RemoteException e) {
                throw new RuntimeException("Failure from system", e);
            }
        }
        int result = ActivityManagerNative.getDefault().startActivityIntentSender(this.mMainThread.getApplicationThread(), intent, fillInIntent, resolvedType, null, null, 0, flagsMask, flagsValues, options);
        if (result == -6) {
            throw new SendIntentException();
        }
        Instrumentation.checkStartActivityResult(result, null);
    }

    public void sendBroadcast(Intent intent) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, null, false, false, getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public void sendBroadcast(Intent intent, String receiverPermission) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, null, false, false, getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public void sendBroadcastMultiplePermissions(Intent intent, String[] receiverPermissions) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, null, false, false, getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public void sendBroadcastMultiplePermissionsAsUser(Intent intent, String[] receiverPermissions, UserHandle user) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, null, false, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public void sendBroadcast(Intent intent, String receiverPermission, Bundle options) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, options, false, false, getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public void sendBroadcast(Intent intent, String receiverPermission, int appOp) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, appOp, null, false, false, getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public void sendOrderedBroadcast(Intent intent, String receiverPermission) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, -1, null, true, false, getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public void sendOrderedBroadcast(Intent intent, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcast(intent, receiverPermission, -1, resultReceiver, scheduler, initialCode, initialData, initialExtras, null);
    }

    public void sendOrderedBroadcast(Intent intent, String receiverPermission, Bundle options, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcast(intent, receiverPermission, -1, resultReceiver, scheduler, initialCode, initialData, initialExtras, options);
    }

    public void sendOrderedBroadcast(Intent intent, String receiverPermission, int appOp, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcast(intent, receiverPermission, appOp, resultReceiver, scheduler, initialCode, initialData, initialExtras, null);
    }

    void sendOrderedBroadcast(Intent intent, String receiverPermission, int appOp, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras, Bundle options) {
        warnIfCallingFromSystemProcess();
        IIntentReceiver rd = null;
        if (resultReceiver != null) {
            if (this.mPackageInfo != null) {
                if (scheduler == null) {
                    scheduler = this.mMainThread.getHandler();
                }
                rd = this.mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, this.mMainThread.getInstrumentation(), false);
            } else {
                if (scheduler == null) {
                    scheduler = this.mMainThread.getHandler();
                }
                rd = new ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, null, false).getIIntentReceiver();
            }
        }
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, rd, initialCode, initialData, initialExtras, receiverPermissions, appOp, options, true, false, getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public void sendBroadcastAsUser(Intent intent, UserHandle user) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, null, false, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission) {
        sendBroadcastAsUser(intent, user, receiverPermission, -1);
    }

    public void sendBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, int appOp) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, receiverPermissions, appOp, null, false, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcastAsUser(intent, user, receiverPermission, -1, null, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, int appOp, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        sendOrderedBroadcastAsUser(intent, user, receiverPermission, appOp, null, resultReceiver, scheduler, initialCode, initialData, initialExtras);
    }

    public void sendOrderedBroadcastAsUser(Intent intent, UserHandle user, String receiverPermission, int appOp, Bundle options, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        IIntentReceiver rd = null;
        if (resultReceiver != null) {
            if (this.mPackageInfo != null) {
                if (scheduler == null) {
                    scheduler = this.mMainThread.getHandler();
                }
                rd = this.mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, this.mMainThread.getInstrumentation(), false);
            } else {
                if (scheduler == null) {
                    scheduler = this.mMainThread.getHandler();
                }
                rd = new ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, null, false).getIIntentReceiver();
            }
        }
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        String[] receiverPermissions = receiverPermission == null ? null : new String[]{receiverPermission};
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, rd, initialCode, initialData, initialExtras, receiverPermissions, appOp, options, true, false, user.getIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    @Deprecated
    public void sendStickyBroadcast(Intent intent) {
        warnIfCallingFromSystemProcess();
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, null, false, true, getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    @Deprecated
    public void sendStickyOrderedBroadcast(Intent intent, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        warnIfCallingFromSystemProcess();
        IIntentReceiver rd = null;
        if (resultReceiver != null) {
            if (this.mPackageInfo != null) {
                if (scheduler == null) {
                    scheduler = this.mMainThread.getHandler();
                }
                rd = this.mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, this.mMainThread.getInstrumentation(), false);
            } else {
                if (scheduler == null) {
                    scheduler = this.mMainThread.getHandler();
                }
                rd = new ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, null, false).getIIntentReceiver();
            }
        }
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, rd, initialCode, initialData, initialExtras, null, -1, null, true, true, getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    @Deprecated
    public void removeStickyBroadcast(Intent intent) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        if (resolvedType != null) {
            Intent intent2 = new Intent(intent);
            intent2.setDataAndType(intent2.getData(), resolvedType);
            intent = intent2;
        }
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().unbroadcastIntent(this.mMainThread.getApplicationThread(), intent, getUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    @Deprecated
    public void sendStickyBroadcastAsUser(Intent intent, UserHandle user) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, null, -1, null, null, null, -1, null, false, true, user.getIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    @Deprecated
    public void sendStickyOrderedBroadcastAsUser(Intent intent, UserHandle user, BroadcastReceiver resultReceiver, Handler scheduler, int initialCode, String initialData, Bundle initialExtras) {
        IIntentReceiver rd = null;
        if (resultReceiver != null) {
            if (this.mPackageInfo != null) {
                if (scheduler == null) {
                    scheduler = this.mMainThread.getHandler();
                }
                rd = this.mPackageInfo.getReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, this.mMainThread.getInstrumentation(), false);
            } else {
                if (scheduler == null) {
                    scheduler = this.mMainThread.getHandler();
                }
                rd = new ReceiverDispatcher(resultReceiver, getOuterContext(), scheduler, null, false).getIIntentReceiver();
            }
        }
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().broadcastIntent(this.mMainThread.getApplicationThread(), intent, resolvedType, rd, initialCode, initialData, initialExtras, null, -1, null, true, true, user.getIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    @Deprecated
    public void removeStickyBroadcastAsUser(Intent intent, UserHandle user) {
        String resolvedType = intent.resolveTypeIfNeeded(getContentResolver());
        if (resolvedType != null) {
            Intent intent2 = new Intent(intent);
            intent2.setDataAndType(intent2.getData(), resolvedType);
            intent = intent2;
        }
        try {
            intent.prepareToLeaveProcess();
            ActivityManagerNative.getDefault().unbroadcastIntent(this.mMainThread.getApplicationThread(), intent, user.getIdentifier());
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        return registerReceiver(receiver, filter, null, null);
    }

    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter, String broadcastPermission, Handler scheduler) {
        return registerReceiverInternal(receiver, getUserId(), filter, broadcastPermission, scheduler, getOuterContext());
    }

    public Intent registerReceiverAsUser(BroadcastReceiver receiver, UserHandle user, IntentFilter filter, String broadcastPermission, Handler scheduler) {
        return registerReceiverInternal(receiver, user.getIdentifier(), filter, broadcastPermission, scheduler, getOuterContext());
    }

    private Intent registerReceiverInternal(BroadcastReceiver receiver, int userId, IntentFilter filter, String broadcastPermission, Handler scheduler, Context context) {
        IIntentReceiver rd = null;
        if (receiver != null) {
            if (this.mPackageInfo == null || context == null) {
                if (scheduler == null) {
                    scheduler = this.mMainThread.getHandler();
                }
                rd = new ReceiverDispatcher(receiver, context, scheduler, null, true).getIIntentReceiver();
            } else {
                if (scheduler == null) {
                    scheduler = this.mMainThread.getHandler();
                }
                rd = this.mPackageInfo.getReceiverDispatcher(receiver, context, scheduler, this.mMainThread.getInstrumentation(), true);
            }
        }
        try {
            return ActivityManagerNative.getDefault().registerReceiver(this.mMainThread.getApplicationThread(), this.mBasePackageName, rd, filter, broadcastPermission, userId);
        } catch (RemoteException e) {
            return null;
        }
    }

    public void unregisterReceiver(BroadcastReceiver receiver) {
        if (this.mPackageInfo != null) {
            try {
                ActivityManagerNative.getDefault().unregisterReceiver(this.mPackageInfo.forgetReceiverDispatcher(getOuterContext(), receiver));
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        throw new RuntimeException("Not supported in system context");
    }

    private void validateServiceIntent(Intent service) {
        if (service.getComponent() != null || service.getPackage() != null) {
            return;
        }
        if (getApplicationInfo().targetSdkVersion >= 21) {
            throw new IllegalArgumentException("Service Intent must be explicit: " + service);
        }
        Log.w(TAG, "Implicit intents with startService are not safe: " + service + " " + Debug.getCallers(2, 3));
    }

    public ComponentName startService(Intent service) {
        warnIfCallingFromSystemProcess();
        return startServiceCommon(service, this.mUser);
    }

    public boolean stopService(Intent service) {
        warnIfCallingFromSystemProcess();
        return stopServiceCommon(service, this.mUser);
    }

    public ComponentName startServiceAsUser(Intent service, UserHandle user) {
        return startServiceCommon(service, user);
    }

    private ComponentName startServiceCommon(Intent service, UserHandle user) {
        try {
            ComponentName aux = service.getComponent();
            if (aux != null && aux.getPackageName() != null && KioskMode.MINI_TASK_MANAGER_PKGNAME.equals(aux.getPackageName()) && !EnterpriseDeviceManager.getInstance().getKioskMode().isTaskManagerAllowed(true)) {
                return null;
            }
            validateServiceIntent(service);
            service.prepareToLeaveProcess();
            ComponentName cn = ActivityManagerNative.getDefault().startService(this.mMainThread.getApplicationThread(), service, service.resolveTypeIfNeeded(getContentResolver()), getOpPackageName(), user.getIdentifier());
            if (cn == null) {
                return cn;
            }
            if (cn.getPackageName().equals("!")) {
                throw new SecurityException("Not allowed to start service " + service + " without permission " + cn.getClassName());
            } else if (!cn.getPackageName().equals("!!")) {
                return cn;
            } else {
                throw new SecurityException("Unable to start service " + service + ": " + cn.getClassName());
            }
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public boolean stopServiceAsUser(Intent service, UserHandle user) {
        return stopServiceCommon(service, user);
    }

    private boolean stopServiceCommon(Intent service, UserHandle user) {
        try {
            validateServiceIntent(service);
            service.prepareToLeaveProcess();
            int res = ActivityManagerNative.getDefault().stopService(this.mMainThread.getApplicationThread(), service, service.resolveTypeIfNeeded(getContentResolver()), user.getIdentifier());
            if (res >= 0) {
                return res != 0;
            } else {
                throw new SecurityException("Not allowed to stop service " + service);
            }
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    public boolean bindService(Intent service, ServiceConnection conn, int flags) {
        warnIfCallingFromSystemProcess();
        return bindServiceCommon(service, conn, flags, Process.myUserHandle());
    }

    public boolean bindServiceAsUser(Intent service, ServiceConnection conn, int flags, UserHandle user) {
        return bindServiceCommon(service, conn, flags, user);
    }

    private boolean bindServiceCommon(Intent service, ServiceConnection conn, int flags, UserHandle user) {
        if (conn == null) {
            throw new IllegalArgumentException("connection is null");
        } else if (this.mPackageInfo != null) {
            IServiceConnection sd = this.mPackageInfo.getServiceDispatcher(conn, getOuterContext(), this.mMainThread.getHandler(), flags);
            validateServiceIntent(service);
            try {
                if (getActivityToken() == null && (flags & 1) == 0 && this.mPackageInfo != null && this.mPackageInfo.getApplicationInfo().targetSdkVersion < 14) {
                    flags |= 32;
                }
                service.prepareToLeaveProcess();
                int res = ActivityManagerNative.getDefault().bindService(this.mMainThread.getApplicationThread(), null, service, service.resolveTypeIfNeeded(getContentResolver()), sd, flags, getOpPackageName(), user.getIdentifier());
                if (res >= 0) {
                    return res != 0;
                } else {
                    throw new SecurityException("Not allowed to bind to service " + service);
                }
            } catch (RemoteException e) {
                throw new RuntimeException("Failure from system", e);
            }
        } else {
            throw new RuntimeException("Not supported in system context");
        }
    }

    public void unbindService(ServiceConnection conn) {
        if (conn == null) {
            throw new IllegalArgumentException("connection is null");
        } else if (this.mPackageInfo != null) {
            try {
                ActivityManagerNative.getDefault().unbindService(this.mPackageInfo.forgetServiceDispatcher(getOuterContext(), conn));
            } catch (RemoteException e) {
            }
        } else {
            throw new RuntimeException("Not supported in system context");
        }
    }

    public boolean startInstrumentation(ComponentName className, String profileFile, Bundle arguments) {
        if (arguments != null) {
            try {
                arguments.setAllowFds(false);
            } catch (RemoteException e) {
                throw new RuntimeException("Failure from system", e);
            }
        }
        return ActivityManagerNative.getDefault().startInstrumentation(className, profileFile, 0, arguments, null, null, getUserId(), null);
    }

    public Object getSystemService(String name) {
        if ("enterprise_policy".equals(name)) {
            return EnterpriseDeviceManager.create(this.mOuterContext, null);
        }
        if ("knox_enterprise_policy".equals(name)) {
            return EnterpriseDeviceManager.createKnox(this.mOuterContext, null);
        }
        Object registrySystemService = SystemServiceRegistry.getSystemService(this, name);
        if (registrySystemService != null || !CscFeature.getInstance().getEnableStatus("CscFeature_Common_EnableSprintExtension")) {
            return registrySystemService;
        }
        if ("lte".equals(name)) {
            Log.i(TAG, "getSystemService(): Sprint LTE");
            if (this.mConnectionManager4G == null) {
                Log.i(TAG, "Initializing Sprint LTE ConnectionManager");
                try {
                    this.mConnectionManager4G = Class.forName("com.sprint.net.lte.ConnectionManager").getConstructor(new Class[]{Context.class}).newInstance(new Object[]{this.mOuterContext});
                } catch (Exception e) {
                    Log.e(TAG, "Could not load Sprint LTE ConnectionManager", e);
                }
            }
            return this.mConnectionManager4G;
        } else if (!"cdma".equals(name)) {
            return registrySystemService;
        } else {
            if (this.mConnectionManager3G == null) {
                Log.i(TAG, "Initializing Sprint CDMA ConnectionManager");
                try {
                    this.mConnectionManager3G = Class.forName("com.sprint.net.cdma.ConnectionManager").getConstructor(new Class[]{Context.class}).newInstance(new Object[]{this.mOuterContext});
                } catch (Exception e2) {
                    Log.e(TAG, "Could not load Sprint CDMA ConnectionManager", e2);
                }
            }
            return this.mConnectionManager3G;
        }
    }

    public String getSystemServiceName(Class<?> serviceClass) {
        return SystemServiceRegistry.getSystemServiceName(serviceClass);
    }

    public int checkPermission(String permission, int pid, int uid) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }
        try {
            return ActivityManagerNative.getDefault().checkPermission(permission, pid, uid);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int checkPermission(String permission, int pid, int uid, IBinder callerToken) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }
        try {
            return ActivityManagerNative.getDefault().checkPermissionWithToken(permission, pid, uid, callerToken);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int checkCallingPermission(String permission) {
        if (permission == null) {
            throw new IllegalArgumentException("permission is null");
        }
        int pid = Binder.getCallingPid();
        if (pid != Process.myPid()) {
            return checkPermission(permission, pid, Binder.getCallingUid());
        }
        return -1;
    }

    public int checkCallingOrSelfPermission(String permission) {
        if (permission != null) {
            return checkPermission(permission, Binder.getCallingPid(), Binder.getCallingUid());
        }
        throw new IllegalArgumentException("permission is null");
    }

    public int checkSelfPermission(String permission) {
        if (permission != null) {
            return checkPermission(permission, Process.myPid(), Process.myUid());
        }
        throw new IllegalArgumentException("permission is null");
    }

    private void enforce(String permission, int resultOfCheck, boolean selfToo, int uid, String message) {
        if (resultOfCheck != 0) {
            throw new SecurityException((message != null ? message + ": " : ProxyInfo.LOCAL_EXCL_LIST) + (selfToo ? "Neither user " + uid + " nor current process has " : "uid " + uid + " does not have ") + permission + ".");
        }
    }

    public void enforcePermission(String permission, int pid, int uid, String message) {
        enforce(permission, checkPermission(permission, pid, uid), false, uid, message);
    }

    public void enforceCallingPermission(String permission, String message) {
        enforce(permission, checkCallingPermission(permission), false, Binder.getCallingUid(), message);
    }

    public void enforceCallingOrSelfPermission(String permission, String message) {
        enforce(permission, checkCallingOrSelfPermission(permission), true, Binder.getCallingUid(), message);
    }

    public void grantUriPermission(String toPackage, Uri uri, int modeFlags) {
        try {
            ActivityManagerNative.getDefault().grantUriPermission(this.mMainThread.getApplicationThread(), toPackage, ContentProvider.getUriWithoutUserId(uri), modeFlags, resolveUserId(uri));
        } catch (RemoteException e) {
        }
    }

    public void revokeUriPermission(Uri uri, int modeFlags) {
        try {
            ActivityManagerNative.getDefault().revokeUriPermission(this.mMainThread.getApplicationThread(), ContentProvider.getUriWithoutUserId(uri), modeFlags, resolveUserId(uri));
        } catch (RemoteException e) {
        }
    }

    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags) {
        try {
            return ActivityManagerNative.getDefault().checkUriPermission(ContentProvider.getUriWithoutUserId(uri), pid, uid, modeFlags, resolveUserId(uri), null);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int checkUriPermission(Uri uri, int pid, int uid, int modeFlags, IBinder callerToken) {
        try {
            return ActivityManagerNative.getDefault().checkUriPermission(ContentProvider.getUriWithoutUserId(uri), pid, uid, modeFlags, resolveUserId(uri), callerToken);
        } catch (RemoteException e) {
            return -1;
        }
    }

    private int resolveUserId(Uri uri) {
        return ContentProvider.getUserIdFromUri(uri, getUserId());
    }

    public int checkCallingUriPermission(Uri uri, int modeFlags) {
        int pid = Binder.getCallingPid();
        if (pid != Process.myPid()) {
            return checkUriPermission(uri, pid, Binder.getCallingUid(), modeFlags);
        }
        return -1;
    }

    public int checkCallingOrSelfUriPermission(Uri uri, int modeFlags) {
        return checkUriPermission(uri, Binder.getCallingPid(), Binder.getCallingUid(), modeFlags);
    }

    public int checkUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags) {
        if ((modeFlags & 1) != 0 && (readPermission == null || checkPermission(readPermission, pid, uid) == 0)) {
            return 0;
        }
        if ((modeFlags & 2) == 0 || (writePermission != null && checkPermission(writePermission, pid, uid) != 0)) {
            return uri != null ? checkUriPermission(uri, pid, uid, modeFlags) : -1;
        } else {
            return 0;
        }
    }

    private String uriModeFlagToString(int uriModeFlags) {
        StringBuilder builder = new StringBuilder();
        if ((uriModeFlags & 1) != 0) {
            builder.append("read and ");
        }
        if ((uriModeFlags & 2) != 0) {
            builder.append("write and ");
        }
        if ((uriModeFlags & 64) != 0) {
            builder.append("persistable and ");
        }
        if ((uriModeFlags & 128) != 0) {
            builder.append("prefix and ");
        }
        if (builder.length() > 5) {
            builder.setLength(builder.length() - 5);
            return builder.toString();
        }
        throw new IllegalArgumentException("Unknown permission mode flags: " + uriModeFlags);
    }

    private void enforceForUri(int modeFlags, int resultOfCheck, boolean selfToo, int uid, Uri uri, String message) {
        if (resultOfCheck != 0) {
            throw new SecurityException((message != null ? message + ": " : ProxyInfo.LOCAL_EXCL_LIST) + (selfToo ? "Neither user " + uid + " nor current process has " : "User " + uid + " does not have ") + uriModeFlagToString(modeFlags) + " permission on " + uri + ".");
        }
    }

    public void enforceUriPermission(Uri uri, int pid, int uid, int modeFlags, String message) {
        enforceForUri(modeFlags, checkUriPermission(uri, pid, uid, modeFlags), false, uid, uri, message);
    }

    public void enforceCallingUriPermission(Uri uri, int modeFlags, String message) {
        enforceForUri(modeFlags, checkCallingUriPermission(uri, modeFlags), false, Binder.getCallingUid(), uri, message);
    }

    public void enforceCallingOrSelfUriPermission(Uri uri, int modeFlags, String message) {
        enforceForUri(modeFlags, checkCallingOrSelfUriPermission(uri, modeFlags), true, Binder.getCallingUid(), uri, message);
    }

    public void enforceUriPermission(Uri uri, String readPermission, String writePermission, int pid, int uid, int modeFlags, String message) {
        enforceForUri(modeFlags, checkUriPermission(uri, readPermission, writePermission, pid, uid, modeFlags), false, uid, uri, message);
    }

    private void warnIfCallingFromSystemProcess() {
        if (Process.myUid() == 1000) {
            Slog.w(TAG, "Calling a method in the system process without a qualified user: " + Debug.getCallers(5));
        }
    }

    public Context createApplicationContext(ApplicationInfo application, int flags) throws NameNotFoundException {
        LoadedApk pi = this.mMainThread.getPackageInfo(application, this.mResources.getCompatibilityInfo(), 1073741824 | flags);
        if (pi != null) {
            ContextImpl c = new ContextImpl(this, this.mMainThread, pi, this.mActivityToken, new UserHandle(UserHandle.getUserId(application.uid)), (flags & 4) == 4, this.mDisplay, null, -1);
            if (c.mResources != null) {
                return c;
            }
        }
        throw new NameNotFoundException("Application package " + application.packageName + " not found");
    }

    public Context createPackageContext(String packageName, int flags) throws NameNotFoundException {
        return createPackageContextAsUser(packageName, flags, this.mUser != null ? this.mUser : Process.myUserHandle());
    }

    public Context createPackageContextAsUser(String packageName, int flags, UserHandle user) throws NameNotFoundException {
        boolean restricted = (flags & 4) == 4;
        if (packageName.equals("system") || packageName.equals("android")) {
            return new ContextImpl(this, this.mMainThread, this.mPackageInfo, this.mActivityToken, user, restricted, this.mDisplay, null, -1);
        }
        LoadedApk pi = this.mMainThread.getPackageInfo(packageName, this.mResources.getCompatibilityInfo(), 1073741824 | flags, user.getIdentifier());
        if (pi != null) {
            Context c = new ContextImpl(this, this.mMainThread, pi, this.mActivityToken, user, restricted, this.mDisplay, null, -1);
            if (c.mResources != null) {
                return c;
            }
        }
        throw new NameNotFoundException("Application package " + packageName + " not found");
    }

    public Context createPackageContext(String packageName, ClassLoader baseLoader, int flags) throws NameNotFoundException {
        return createPackageContextAsUser(packageName, baseLoader, flags, this.mUser != null ? this.mUser : Process.myUserHandle());
    }

    public Context createPackageContextAsUser(String packageName, ClassLoader baseLoader, int flags, UserHandle user) throws NameNotFoundException {
        boolean restricted = (flags & 4) == 4;
        if (packageName.equals("system") || packageName.equals("android")) {
            return new ContextImpl(this, this.mMainThread, this.mPackageInfo, this.mActivityToken, user, restricted, this.mDisplay, null, -1);
        }
        LoadedApk pi = this.mMainThread.getPackageInfo(packageName, this.mResources.getCompatibilityInfo(), baseLoader, flags | 1073741824, user.getIdentifier());
        if (pi != null) {
            Context c = new ContextImpl(this, this.mMainThread, pi, this.mActivityToken, user, restricted, this.mDisplay, null, -1);
            if (c.mResources != null) {
                return c;
            }
        }
        throw new NameNotFoundException("Application package " + packageName + " not found");
    }

    public Context createConfigurationContext(Configuration overrideConfiguration) {
        if (overrideConfiguration != null) {
            return new ContextImpl(this, this.mMainThread, this.mPackageInfo, this.mActivityToken, this.mUser, this.mRestricted, this.mDisplay, overrideConfiguration, -1);
        }
        throw new IllegalArgumentException("overrideConfiguration must not be null");
    }

    public Context createDisplayContext(Display display) {
        return createDisplayContext(display, null);
    }

    public Context createDisplayContext(Display display, IBinder activityToken) {
        if (display == null) {
            throw new IllegalArgumentException("display must not be null");
        }
        return new ContextImpl(this, this.mMainThread, this.mPackageInfo, activityToken, this.mUser, this.mRestricted, display, null, -1);
    }

    Display getDisplay() {
        if (this.mDisplay != null) {
            return this.mDisplay;
        }
        return ResourcesManager.getInstance().getAdjustedDisplay(0, this.mDisplayAdjustments);
    }

    public int getDisplayId() {
        return this.mDisplay != null ? this.mDisplay.getDisplayId() : 0;
    }

    public void setDisplay(Display display) {
    }

    public boolean getShrinkRequested() {
        return this.mMainThread.getShrinkRequested();
    }

    public boolean isRestricted() {
        return this.mRestricted;
    }

    public DisplayAdjustments getDisplayAdjustments(int displayId) {
        return this.mDisplayAdjustments;
    }

    private File getDataDirFile() {
        if (this.mPackageInfo != null) {
            return this.mPackageInfo.getDataDirFile();
        }
        throw new RuntimeException("Not supported in system context");
    }

    public File getDir(String name, int mode) {
        File file = makeFilename(getDataDirFile(), "app_" + name);
        if (!file.exists()) {
            file.mkdir();
            setFilePermissionsFromMode(file.getPath(), mode, 505);
        }
        return file;
    }

    public int getUserId() {
        return this.mUser.getIdentifier();
    }

    static ContextImpl createSystemContext(ActivityThread mainThread) {
        ContextImpl context = new ContextImpl(null, mainThread, new LoadedApk(mainThread), null, null, false, null, null, -1);
        context.mResources.updateConfiguration(context.mResourcesManager.getConfiguration(), context.mResourcesManager.getDisplayMetricsLocked());
        return context;
    }

    static ContextImpl createAppContext(ActivityThread mainThread, LoadedApk packageInfo) {
        return createAppContext(mainThread, packageInfo, 0);
    }

    static ContextImpl createAppContext(ActivityThread mainThread, LoadedApk packageInfo, int displayId) {
        if (packageInfo != null) {
            return new ContextImpl(null, mainThread, packageInfo, null, null, false, null, null, -1);
        }
        throw new IllegalArgumentException("packageInfo");
    }

    static ContextImpl createActivityContext(ActivityThread mainThread, LoadedApk packageInfo, int displayId, Configuration overrideConfiguration, IBinder activityToken) {
        if (packageInfo != null) {
            return new ContextImpl(null, mainThread, packageInfo, activityToken, null, false, null, overrideConfiguration, displayId);
        }
        throw new IllegalArgumentException("packageInfo");
    }

    private ContextImpl(ContextImpl container, ActivityThread mainThread, LoadedApk packageInfo, IBinder activityToken, UserHandle user, boolean restricted, Display display, Configuration overrideConfiguration, int createDisplayWithId) {
        this.mMainThread = mainThread;
        this.mActivityToken = activityToken;
        this.mRestricted = restricted;
        if (user == null) {
            user = Process.myUserHandle();
        }
        this.mUser = user;
        this.mPackageInfo = packageInfo;
        this.mResourcesManager = ResourcesManager.getInstance();
        int displayId = createDisplayWithId != -1 ? createDisplayWithId : display != null ? display.getDisplayId() : 0;
        CompatibilityInfo compatInfo = null;
        if (container != null) {
            compatInfo = container.getDisplayAdjustments(displayId).getCompatibilityInfo();
        }
        if (compatInfo == null) {
            compatInfo = displayId == 0 ? packageInfo.getCompatibilityInfo() : CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
        }
        this.mDisplayAdjustments.setCompatibilityInfo(compatInfo);
        this.mDisplayAdjustments.setConfiguration(overrideConfiguration);
        if (createDisplayWithId != -1) {
            display = ResourcesManager.getInstance().getAdjustedDisplay(displayId, this.mDisplayAdjustments);
        }
        this.mDisplay = display;
        Resources resources = packageInfo.getResources(mainThread, this.mUser);
        if (!(resources == null || (displayId == 0 && overrideConfiguration == null && (compatInfo == null || compatInfo.applicationScale == resources.getCompatibilityInfo().applicationScale)))) {
            resources = this.mResourcesManager.getTopLevelResources(packageInfo.getResDir(), packageInfo.getSplitResDirs(), packageInfo.getOverlayDirs(), packageInfo.getApplicationInfo().sharedLibraryFiles, displayId, overrideConfiguration, compatInfo, this.mPackageInfo.mPackageName);
        }
        this.mResources = resources;
        if (container != null) {
            this.mBasePackageName = container.mBasePackageName;
            this.mOpPackageName = container.mOpPackageName;
        } else {
            this.mBasePackageName = packageInfo.mPackageName;
            ApplicationInfo ainfo = packageInfo.getApplicationInfo();
            if (ainfo.uid != 1000 || ainfo.uid == Process.myUid()) {
                this.mOpPackageName = this.mBasePackageName;
            } else {
                this.mOpPackageName = ActivityThread.currentPackageName();
            }
        }
        this.mContentResolver = new ApplicationContentResolver(this, mainThread, user);
    }

    void installSystemApplicationInfo(ApplicationInfo info, ClassLoader classLoader) {
        this.mPackageInfo.installSystemApplicationInfo(info, classLoader);
    }

    final void scheduleFinalCleanup(String who, String what) {
        this.mMainThread.scheduleContextCleanup(this, who, what);
    }

    final void performFinalCleanup(String who, String what) {
        this.mPackageInfo.removeContextRegistrations(getOuterContext(), who, what);
    }

    final Context getReceiverRestrictedContext() {
        if (this.mReceiverRestrictedContext != null) {
            return this.mReceiverRestrictedContext;
        }
        Context receiverRestrictedContext = new ReceiverRestrictedContext(getOuterContext());
        this.mReceiverRestrictedContext = receiverRestrictedContext;
        return receiverRestrictedContext;
    }

    final void setOuterContext(Context context) {
        this.mOuterContext = context;
    }

    public final Context getOuterContext() {
        return this.mOuterContext;
    }

    final IBinder getActivityToken() {
        return this.mActivityToken;
    }

    static void setFilePermissionsFromMode(String name, int mode, int extraPermissions) {
        int perms = extraPermissions | 432;
        if ((mode & 1) != 0) {
            perms |= 4;
        }
        if ((mode & 2) != 0) {
            perms |= 2;
        }
        FileUtils.setPermissions(name, perms, -1, -1);
    }

    private File validateFilePath(String name, boolean createDirectory) {
        File dir;
        File f;
        if (name.charAt(0) == File.separatorChar) {
            dir = new File(name.substring(0, name.lastIndexOf(File.separatorChar)));
            f = new File(dir, name.substring(name.lastIndexOf(File.separatorChar)));
        } else {
            dir = getDatabasesDir();
            f = makeFilename(dir, name);
        }
        if (createDirectory && !dir.isDirectory() && dir.mkdir()) {
            FileUtils.setPermissions(dir.getPath(), 505, -1, -1);
        }
        return f;
    }

    private File makeFilename(File base, String name) {
        if (name.indexOf(File.separatorChar) < 0) {
            return new File(base, name);
        }
        throw new IllegalArgumentException("File " + name + " contains a path separator");
    }

    private File[] ensureDirsExistOrFilter(File[] dirs) {
        File[] result = new File[dirs.length];
        for (int i = 0; i < dirs.length; i++) {
            File dir = dirs[i];
            if (!(dir.exists() || dir.mkdirs() || dir.exists())) {
                try {
                    int res = Stub.asInterface(ServiceManager.getService("mount")).mkdirs(getPackageName(), dir.getAbsolutePath());
                    if (res != 0) {
                        Log.w(TAG, "Failed to ensure " + dir + ": " + res);
                        dir = null;
                    }
                } catch (Exception e) {
                    Log.w(TAG, "Failed to ensure " + dir + ": " + e);
                    dir = null;
                }
            }
            result[i] = dir;
        }
        return result;
    }

    public MultiWindowStyle getAppMultiWindowStyle() {
        return this.mMainThread.getAppMultiWindowStyle(this.mActivityToken);
    }

    public boolean isTouchBlocked() {
        return this.mMainThread.isTouchBlocked(this.mActivityToken);
    }

    public IBinder getBaseActivityToken() {
        if (this.mActivityToken != null) {
            return this.mActivityToken;
        }
        return this.mMainThread.getBaseActivityToken();
    }

    public void setResources(Resources resources) {
        if (resources != null) {
            this.mResources = resources;
            invalidateTheme();
        }
    }

    public void invalidateTheme() {
        this.mTheme = null;
        initializeTheme();
    }

    public String[] getOverlayDirs() {
        return this.mPackageInfo.getOverlayDirs();
    }

    public void setStartingWindowContentView(int resId) {
        try {
            IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE)).setStartingWindowContentView(this.mBasePackageName, resId);
        } catch (RemoteException e) {
            Log.e(TAG, "failed to set StartingWindowContentView");
        }
    }

    public void setReverseStartingWindowContentView(int resId) {
        try {
            IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE)).setReverseStartingWindowContentView(this.mBasePackageName, resId);
        } catch (RemoteException e) {
            Log.e(TAG, "failed to set ReverseStartingWindowContentView");
        }
    }

    public void setKeyguardPreview(int resId) {
        try {
            IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE)).setKeyguardPreview(this.mBasePackageName, resId);
        } catch (RemoteException e) {
            Log.e(TAG, "failed to set setKeyguardPreview");
        }
    }

    public void startActivityForKey(Intent intent) {
        startActivity(intent);
    }
}
