package android.app;

import android.R;
import android.app.IActivityManager.ContentProviderHolder;
import android.app.assist.AssistContent;
import android.app.assist.AssistStructure;
import android.app.backup.BackupAgent;
import android.app.im.InjectionConstants.DispatchParentCall;
import android.app.im.InjectionManager;
import android.content.BroadcastReceiver;
import android.content.BroadcastReceiver.PendingResult;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.Context;
import android.content.IContentProvider;
import android.content.IIntentReceiver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.content.pm.IPackageManager.Stub;
import android.content.pm.InstrumentationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.content.res.AssetManager;
import android.content.res.CompatibilityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDebug;
import android.database.sqlite.SQLiteDebug.DbStats;
import android.database.sqlite.SQLiteDebug.PagerStats;
import android.ddm.DdmHandleAppName;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.hardware.SensorManager;
import android.hardware.display.DisplayManagerGlobal;
import android.inputmethodservice.InputMethodService;
import android.net.ConnectivityManager;
import android.net.IConnectivityManager;
import android.net.Proxy;
import android.net.ProxyInfo;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.opengl.GLUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Debug;
import android.os.Debug.MemoryInfo;
import android.os.DropBoxManager;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.MessageQueue.IdleHandler;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.PersonaManager;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.provider.SearchIndexablesContract.BaseColumns;
import android.provider.Settings.Global;
import android.renderscript.RenderScriptCacheDir;
import android.security.NetworkSecurityPolicy;
import android.security.keystore.AndroidKeyStoreProvider;
import android.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;
import android.util.PrintWriterPrinter;
import android.util.Slog;
import android.util.SuperNotCalledException;
import android.util.Xml;
import android.view.DisplayInfo;
import android.view.HardwareRenderer;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewManager;
import android.view.ViewRootImpl;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerGlobal;
import com.android.internal.app.AppLockPolicy;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.content.ReferrerIntent;
import com.android.internal.os.BinderInternal;
import com.android.internal.os.RuntimeInit;
import com.android.internal.os.SamplingProfilerIntegration;
import com.android.internal.util.FastPrintWriter;
import com.android.org.conscrypt.OpenSSLSocketImpl;
import com.android.org.conscrypt.TrustedCertificateStore;
import com.google.android.collect.Lists;
import com.samsung.android.dualscreen.DualScreen;
import com.samsung.android.dualscreen.DualScreenManager;
import com.samsung.android.feature.FloatingFeature;
import com.samsung.android.multidisplay.common.ContextRelationManager;
import com.samsung.android.multidisplay.common.MultiDisplayFeatures;
import com.samsung.android.multidisplay.common.UnRestrictedArrayList;
import com.samsung.android.multiwindow.MultiWindowFeatures;
import com.samsung.android.multiwindow.MultiWindowStyle;
import com.sec.android.app.CscFeature;
import dalvik.system.CloseGuard;
import dalvik.system.VMDebug;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.security.Provider;
import java.security.Security;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.TimeZone;
import libcore.io.DropBox;
import libcore.io.DropBox.Reporter;
import libcore.io.EventLogger;
import libcore.io.IoUtils;
import libcore.net.event.NetworkEventDispatcher;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public final class ActivityThread {
    private static final int ACTIVITY_THREAD_CHECKIN_VERSION = 3;
    private static final boolean DEBUG_BACKUP = false;
    public static final boolean DEBUG_BROADCAST = false;
    public static final boolean DEBUG_CONFIGURATION = false;
    static final boolean DEBUG_DUALSCREEN = DualScreenManager.DEBUG;
    static final boolean DEBUG_DUALSCREEN_VERBOSE = false;
    private static final boolean DEBUG_MEMORY_TRIM = false;
    static final boolean DEBUG_MESSAGES = false;
    private static final boolean DEBUG_PROVIDER = false;
    private static final boolean DEBUG_RC = false;
    private static final boolean DEBUG_RESULTS = false;
    private static final boolean DEBUG_SERVICE = false;
    public static final int DUAL_SCREEN_TYPE_MAX = 4;
    private static final String HEAP_COLUMN = "%13s %8s %8s %8s %8s %8s %8s %8s";
    private static final String HEAP_FULL_COLUMN = "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s";
    private static final int LOG_AM_ON_PAUSE_CALLED = 30021;
    private static final int LOG_AM_ON_RESUME_CALLED = 30022;
    private static final int LOOPER_SLOW_LOOP_WARNING_TIMEOUT = SystemProperties.getInt("sysprof.activitythread.slowloop", -1);
    private static final long MIN_TIME_BETWEEN_GCS = 5000;
    private static final String ONE_COUNT_COLUMN = "%21s %8d";
    private static final String ONE_COUNT_COLUMN_HEADER = "%21s %8s";
    static final boolean SAFE_DEBUG;
    public static final int SERVICE_DONE_EXECUTING_ANON = 0;
    public static final int SERVICE_DONE_EXECUTING_START = 1;
    public static final int SERVICE_DONE_EXECUTING_STOP = 2;
    private static final int SQLITE_MEM_RELEASED_EVENT_LOG_TAG = 75003;
    public static final String TAG = "ActivityThread";
    private static final int THREAD_PRIORITY = -11;
    private static final Config THUMBNAIL_FORMAT = Config.RGB_565;
    private static final String TWO_COUNT_COLUMNS = "%21s %8d %21s %8d";
    private static final boolean isElasticEnabled = true;
    static final boolean localLOGV = false;
    private static final boolean mIsWearableHMTSupported = true;
    private static String rCActivShort = null;
    private static final Object sCSCSync = new Object();
    public static CscFeature sCscFeature = CscFeature.getInstance();
    private static ActivityThread sCurrentActivityThread;
    private static final ThreadLocal<Intent> sCurrentBroadcastIntent = new ThreadLocal();
    public static FloatingFeature sFloatingFeature = FloatingFeature.getInstance();
    static Handler sMainThreadHandler;
    static IPackageManager sPackageManager;
    private static final Object sThemeSync = new Object();
    private float defaultHeapUtil;
    final ArrayMap<IBinder, ActivityClientRecord> mActivities = new ArrayMap();
    final ArrayList<Application> mAllApplications = new ArrayList();
    final ApplicationThread mAppThread = new ApplicationThread();
    private Bitmap mAvailThumbnailBitmap = null;
    final ArrayMap<String, BackupAgent> mBackupAgents = new ArrayMap();
    AppBindData mBoundApplication;
    ArrayMap<String, Integer> mCSCIconMap = null;
    ArrayMap<String, Integer> mCSCStringMap = null;
    Configuration mCompatConfiguration;
    UnRestrictedArrayList<Configuration> mCompatConfigurations = new UnRestrictedArrayList();
    Configuration mConfiguration;
    UnRestrictedArrayList<Configuration> mConfigurations = new UnRestrictedArrayList();
    public ClassLoader mCoreFeatureClassLoader;
    Bundle mCoreSettings = null;
    int mCurDefaultDisplayDpi;
    UnRestrictedArrayList<Integer> mCurDefaultDisplayDpis = new UnRestrictedArrayList();
    boolean mDensityCompatMode;
    final GcIdler mGcIdler = new GcIdler();
    boolean mGcIdlerScheduled = false;
    final H mH = new H();
    Application mInitialApplication;
    Application[] mInitialApplications = new Application[4];
    Instrumentation mInstrumentation;
    String mInstrumentationAppDir = null;
    String mInstrumentationLibDir = null;
    String mInstrumentationPackageName = null;
    String[] mInstrumentationSplitAppDirs = null;
    String mInstrumentedAppDir = null;
    String mInstrumentedLibDir = null;
    String[] mInstrumentedSplitAppDirs = null;
    boolean mJitEnabled = false;
    WeakReference<AssistStructure> mLastAssistStructure;
    IBinder mLastIntendedActivityToken = null;
    final ArrayMap<IBinder, ProviderClientRecord> mLocalProviders = new ArrayMap();
    final ArrayMap<ComponentName, ProviderClientRecord> mLocalProvidersByName = new ArrayMap();
    final Looper mLooper = Looper.myLooper();
    private Configuration mMainThreadConfig = new Configuration();
    ActivityClientRecord mNewActivities = null;
    int mNumVisibleActivities = 0;
    final ArrayMap<Activity, ArrayList<OnActivityPausedListener>> mOnPauseListeners = new ArrayMap();
    final ArrayMap<String, WeakReference<LoadedApk>> mPackages = new ArrayMap();
    Configuration mPendingConfiguration = null;
    UnRestrictedArrayList<Configuration> mPendingConfigurations = new UnRestrictedArrayList();
    private boolean mPrevScreenOrientationDefined = false;
    Profiler mProfiler;
    final ArrayMap<ProviderKey, ProviderClientRecord> mProviderMap = new ArrayMap();
    final ArrayMap<IBinder, ProviderRefCount> mProviderRefCountMap = new ArrayMap();
    final ArrayList<ActivityClientRecord> mRelaunchingActivities = new ArrayList();
    final ArrayMap<String, WeakReference<LoadedApk>> mResourcePackages = new ArrayMap();
    private final ResourcesManager mResourcesManager = ResourcesManager.getInstance();
    final ArrayMap<IBinder, Service> mServices = new ArrayMap();
    boolean mShrinkRequested = false;
    boolean mSomeActivitiesChanged = false;
    private ContextImpl mSystemContext;
    boolean mSystemThread = false;
    Configuration mTempConfigurationForSelectiveOrientation = null;
    HashMap<String, String> mThemeAppIconMap = null;
    private Canvas mThumbnailCanvas = null;
    private int mThumbnailHeight = -1;
    private int mThumbnailWidth = -1;

    static final class ActivityClientRecord {
        Activity activity;
        ActivityInfo activityInfo;
        CompatibilityInfo compatInfo;
        Configuration createdConfig;
        int displayId = 0;
        String embeddedID = null;
        boolean hideForNow = false;
        int ident;
        Intent intent;
        boolean isForward;
        NonConfigurationInstances lastNonConfigurationInstances;
        View mPendingRemoveWindow;
        WindowManager mPendingRemoveWindowManager;
        MultiWindowStyle multiWindowStyle = new MultiWindowStyle();
        Configuration newConfig;
        ActivityClientRecord nextIdle = null;
        boolean onlyLocalRequest;
        Configuration overrideConfig;
        LoadedApk packageInfo;
        Activity parent = null;
        boolean paused = false;
        int pendingConfigChanges;
        List<ReferrerIntent> pendingIntents;
        List<ResultInfo> pendingResults;
        PersistableBundle persistentState;
        ProfilerInfo profilerInfo;
        String referrer;
        boolean startsNotResumed;
        Bundle state;
        boolean stopped = false;
        private Configuration tmpConfig = new Configuration();
        IBinder token;
        IVoiceInteractor voiceInteractor;
        Window window;

        ActivityClientRecord() {
        }

        public boolean isPreHoneycomb() {
            if (this.activity == null || this.activity.getApplicationInfo().targetSdkVersion >= 11) {
                return false;
            }
            return true;
        }

        public boolean isPersistable() {
            return this.activityInfo.persistableMode == 2;
        }

        public String toString() {
            String str;
            ComponentName componentName = this.intent != null ? this.intent.getComponent() : null;
            StringBuilder append = new StringBuilder().append("ActivityRecord{").append(Integer.toHexString(System.identityHashCode(this))).append(" token=").append(this.token).append(" ");
            if (componentName == null) {
                str = "no component name";
            } else {
                str = componentName.toShortString();
            }
            return append.append(str).append("}").toString();
        }
    }

    static final class ActivityConfigChangeData {
        final IBinder activityToken;
        final Configuration overrideConfig;

        public ActivityConfigChangeData(IBinder token, Configuration config) {
            this.activityToken = token;
            this.overrideConfig = config;
        }
    }

    static final class AppBindData {
        ApplicationInfo appInfo;
        CompatibilityInfo compatInfo;
        Configuration config;
        UnRestrictedArrayList<Configuration> configs = new UnRestrictedArrayList();
        int debugMode;
        int displayId;
        boolean enableOpenGlTrace;
        LoadedApk info;
        ProfilerInfo initProfilerInfo;
        Bundle instrumentationArgs;
        ComponentName instrumentationName;
        IUiAutomationConnection instrumentationUiAutomationConnection;
        IInstrumentationWatcher instrumentationWatcher;
        boolean persistent;
        String processName;
        List<ProviderInfo> providers;
        boolean restrictedBackupMode;

        AppBindData() {
        }

        public String toString() {
            return "AppBindData{appInfo=" + this.appInfo + "}";
        }
    }

    private class ApplicationThread extends ApplicationThreadNative {
        private static final String DB_INFO_FORMAT = "  %8s %8s %14s %14s  %s";
        private int mLastProcessState;

        private ApplicationThread() {
            this.mLastProcessState = -1;
        }

        private void updatePendingConfiguration(Configuration config) {
            synchronized (ActivityThread.this.mResourcesManager) {
                if (ActivityThread.this.mPendingConfiguration == null || ActivityThread.this.mPendingConfiguration.isOtherSeqNewer(config)) {
                    ActivityThread.this.mPendingConfiguration = config;
                }
            }
        }

        private void updatePendingConfigurations(UnRestrictedArrayList<Configuration> configs) {
            synchronized (ActivityThread.this.mResourcesManager) {
                Iterator i$ = configs.iterator();
                while (i$.hasNext()) {
                    Configuration config = (Configuration) i$.next();
                    if (config != null && (ActivityThread.this.mPendingConfigurations.get(config.displayId) == null || ((Configuration) ActivityThread.this.mPendingConfigurations.get(config.displayId)).isOtherSeqNewer(config))) {
                        ActivityThread.this.mPendingConfigurations.set(config.displayId, config);
                    }
                }
            }
        }

        public final void updateOverlayPath(String path, String packageName, int isDisable) {
            Slog.d(ActivityThread.TAG, "updating overlay path for " + packageName + ", isDisable =" + isDisable);
            Message msg = Message.obtain(null, 150, isDisable, 0);
            ActivityThread.this.sendMessage(150, new Pair(path, packageName), isDisable);
        }

        public final void schedulePauseActivity(IBinder token, boolean finished, boolean userLeaving, int configChanges, boolean dontReport) {
            int i = 0;
            ActivityThread activityThread = ActivityThread.this;
            int i2 = finished ? 102 : 101;
            int i3 = userLeaving ? 1 : 0;
            if (dontReport) {
                i = 2;
            }
            activityThread.sendMessage(i2, token, i | i3, configChanges);
        }

        public final void scheduleStopActivity(IBinder token, boolean showWindow, int configChanges) {
            ActivityThread.this.sendMessage(showWindow ? 103 : 104, token, 0, configChanges);
        }

        public final void scheduleWindowVisibility(IBinder token, boolean showWindow) {
            ActivityThread.this.sendMessage(showWindow ? 105 : 106, token);
        }

        public final void scheduleSleeping(IBinder token, boolean sleeping) {
            ActivityThread.this.sendMessage(137, token, sleeping ? 1 : 0);
        }

        public final void scheduleResumeActivity(IBinder token, int processState, boolean isForward, Bundle resumeArgs) {
            int i = 0;
            updateProcessState(processState, false);
            ActivityThread activityThread = ActivityThread.this;
            if (isForward) {
                i = 1;
            }
            activityThread.sendMessage(107, token, i);
        }

        public final void forceCallResumeActivity(IBinder token) {
            ActivityThread.this.sendMessage(151, token);
        }

        public final void scheduleSendResult(IBinder token, List<ResultInfo> results) {
            ResultData res = new ResultData();
            res.token = token;
            res.results = results;
            ActivityThread.this.sendMessage(108, res);
        }

        public final void scheduleLaunchActivity(Intent intent, IBinder token, int ident, ActivityInfo info, Configuration curConfig, Configuration overrideConfig, CompatibilityInfo compatInfo, String referrer, IVoiceInteractor voiceInteractor, int procState, Bundle state, PersistableBundle persistentState, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingNewIntents, boolean notResumed, boolean isForward, ProfilerInfo profilerInfo, MultiWindowStyle multiWindowStyle, int displayId) {
            updateProcessState(procState, false);
            ActivityClientRecord r = new ActivityClientRecord();
            r.token = token;
            r.ident = ident;
            r.intent = intent;
            r.referrer = referrer;
            r.voiceInteractor = voiceInteractor;
            r.activityInfo = info;
            r.compatInfo = compatInfo;
            r.state = state;
            r.persistentState = persistentState;
            r.pendingResults = pendingResults;
            r.pendingIntents = pendingNewIntents;
            r.startsNotResumed = notResumed;
            r.isForward = isForward;
            r.profilerInfo = profilerInfo;
            r.overrideConfig = overrideConfig;
            r.multiWindowStyle.setTo(multiWindowStyle, true);
            r.displayId = displayId;
            updatePendingConfiguration(curConfig);
            ActivityThread.this.sendMessage(100, r);
        }

        public final void scheduleRelaunchActivity(IBinder token, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingNewIntents, int configChanges, boolean notResumed, Configuration config, Configuration overrideConfig, MultiWindowStyle multiWindowStyle, int displayId) {
            ActivityThread.this.requestRelaunchActivity(token, pendingResults, pendingNewIntents, configChanges, notResumed, config, overrideConfig, true, multiWindowStyle, displayId);
        }

        public final void scheduleNewIntent(List<ReferrerIntent> intents, IBinder token) {
            NewIntentData data = new NewIntentData();
            data.intents = intents;
            data.token = token;
            ActivityThread.this.sendMessage(112, data);
        }

        public final void scheduleDestroyActivity(IBinder token, boolean finishing, int configChanges) {
            ActivityThread.this.sendMessage(109, token, finishing ? 1 : 0, configChanges);
        }

        public final void scheduleReceiver(Intent intent, ActivityInfo info, CompatibilityInfo compatInfo, int resultCode, String data, Bundle extras, boolean sync, int sendingUser, int processState) {
            updateProcessState(processState, false);
            ReceiverData r = new ReceiverData(intent, resultCode, data, extras, sync, false, ActivityThread.this.mAppThread.asBinder(), sendingUser);
            r.info = info;
            r.compatInfo = compatInfo;
            ActivityThread.this.sendMessage(113, r);
        }

        public final void scheduleCreateBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo, int backupMode) {
            CreateBackupAgentData d = new CreateBackupAgentData();
            d.appInfo = app;
            d.compatInfo = compatInfo;
            d.backupMode = backupMode;
            ActivityThread.this.sendMessage(128, d);
        }

        public final void scheduleDestroyBackupAgent(ApplicationInfo app, CompatibilityInfo compatInfo) {
            CreateBackupAgentData d = new CreateBackupAgentData();
            d.appInfo = app;
            d.compatInfo = compatInfo;
            ActivityThread.this.sendMessage(129, d);
        }

        public final void scheduleCreateService(IBinder token, ServiceInfo info, CompatibilityInfo compatInfo, int processState) {
            scheduleCreateService(token, info, compatInfo, processState, 0, null);
        }

        public final void scheduleCreateService(IBinder token, ServiceInfo info, CompatibilityInfo compatInfo, int processState, int displayId, IBinder callerActivityToken) {
            updateProcessState(processState, false);
            CreateServiceData s = new CreateServiceData();
            s.token = token;
            s.info = info;
            s.compatInfo = compatInfo;
            s.displayId = displayId;
            s.callerActivityToken = callerActivityToken;
            ActivityThread.this.sendMessage(114, s);
        }

        public final void scheduleBindService(IBinder token, Intent intent, boolean rebind, int processState) {
            updateProcessState(processState, false);
            BindServiceData s = new BindServiceData();
            s.token = token;
            s.intent = intent;
            s.rebind = rebind;
            ActivityThread.this.sendMessage(121, s);
        }

        public final void scheduleUnbindService(IBinder token, Intent intent) {
            BindServiceData s = new BindServiceData();
            s.token = token;
            s.intent = intent;
            ActivityThread.this.sendMessage(122, s);
        }

        public final void scheduleServiceArgs(IBinder token, boolean taskRemoved, int startId, int flags, Intent args) {
            ServiceArgsData s = new ServiceArgsData();
            s.token = token;
            s.taskRemoved = taskRemoved;
            s.startId = startId;
            s.flags = flags;
            s.args = args;
            ActivityThread.this.sendMessage(115, s);
        }

        public final void scheduleStopService(IBinder token) {
            ActivityThread.this.sendMessage(116, token);
        }

        public final void bindApplication(String processName, ApplicationInfo appInfo, List<ProviderInfo> providers, ComponentName instrumentationName, ProfilerInfo profilerInfo, Bundle instrumentationArgs, IInstrumentationWatcher instrumentationWatcher, IUiAutomationConnection instrumentationUiConnection, int debugMode, boolean enableOpenGlTrace, boolean isRestrictedBackupMode, boolean persistent, UnRestrictedArrayList<Configuration> configs, CompatibilityInfo compatInfo, Map<String, IBinder> services, Bundle coreSettings, int displayId) {
            if (services != null) {
                ServiceManager.initServiceCache(services);
            }
            setCoreSettings(coreSettings);
            PackageInfo pi = null;
            try {
                pi = ActivityThread.getPackageManager().getPackageInfo(appInfo.packageName, 0, UserHandle.myUserId());
            } catch (RemoteException e) {
            }
            if (pi != null) {
                boolean sharedUserIdSet = pi.sharedUserId != null;
                boolean processNameNotDefault = (pi.applicationInfo == null || appInfo.packageName.equals(pi.applicationInfo.processName)) ? false : true;
                boolean sharable = sharedUserIdSet || processNameNotDefault;
                if (!sharable) {
                    VMRuntime.registerAppInfo(appInfo.packageName, appInfo.dataDir, appInfo.processName);
                }
            }
            AppBindData data = new AppBindData();
            data.processName = processName;
            data.appInfo = appInfo;
            data.providers = providers;
            data.instrumentationName = instrumentationName;
            data.instrumentationArgs = instrumentationArgs;
            data.instrumentationWatcher = instrumentationWatcher;
            data.instrumentationUiAutomationConnection = instrumentationUiConnection;
            data.debugMode = debugMode;
            data.enableOpenGlTrace = enableOpenGlTrace;
            data.restrictedBackupMode = isRestrictedBackupMode;
            data.persistent = persistent;
            data.config = (Configuration) configs.get(0);
            data.compatInfo = compatInfo;
            data.initProfilerInfo = profilerInfo;
            data.displayId = displayId;
            data.configs.set(0, configs.get(0));
            ActivityThread.this.sendMessage(110, data);
        }

        public final void scheduleExit() {
            ActivityThread.this.sendMessage(111, null);
        }

        public final void scheduleSuicide() {
            ActivityThread.this.sendMessage(130, null);
        }

        public void scheduleConfigurationChanged(Configuration config) {
            updatePendingConfiguration(config);
            ActivityThread.this.sendMessage(118, config);
        }

        public void updateTimeZone() {
            TimeZone.setDefault(null);
        }

        public void clearDnsCache() {
            InetAddress.clearDnsCache();
            NetworkEventDispatcher.getInstance().onNetworkConfigurationChanged();
        }

        public void setHttpProxy(String host, String port, String exclList, Uri pacFileUrl) {
            ConnectivityManager cm = ConnectivityManager.from(ActivityThread.this.getSystemContext());
            if (cm.getBoundNetworkForProcess() != null) {
                Proxy.setHttpProxySystemProperty(cm.getDefaultProxy());
            } else {
                Proxy.setHttpProxySystemProperty(host, port, exclList, pacFileUrl);
            }
        }

        public void setHttpProxy(ProxyInfo proxyInfo) {
            Proxy.setHttpProxySystemProperty(proxyInfo);
        }

        public void setHttpProxy(String host, String port, String username, String password, String exclList, Uri pacFileUrl) {
            Proxy.setHttpProxySystemProperty(host, port, username, password, exclList, pacFileUrl);
        }

        public void processInBackground() {
            ActivityThread.this.mH.removeMessages(120);
            ActivityThread.this.mH.sendMessage(ActivityThread.this.mH.obtainMessage(120));
        }

        public void dumpService(FileDescriptor fd, IBinder servicetoken, String[] args) {
            DumpComponentInfo data = new DumpComponentInfo();
            try {
                data.fd = ParcelFileDescriptor.dup(fd);
                data.token = servicetoken;
                data.args = args;
                ActivityThread.this.sendMessage(123, data, 0, 0, true);
            } catch (IOException e) {
                Slog.w(ActivityThread.TAG, "dumpService failed", e);
            }
        }

        public void scheduleRegisteredReceiver(IIntentReceiver receiver, Intent intent, int resultCode, String dataStr, Bundle extras, boolean ordered, boolean sticky, int sendingUser, int processState) throws RemoteException {
            updateProcessState(processState, false);
            receiver.performReceive(intent, resultCode, dataStr, extras, ordered, sticky, sendingUser);
        }

        public void scheduleLowMemory() {
            ActivityThread.this.sendMessage(124, null);
        }

        public void scheduleActivityConfigurationChanged(IBinder token, Configuration overrideConfig) {
            ActivityThread.this.sendMessage(125, new ActivityConfigChangeData(token, overrideConfig));
        }

        public void scheduleMultiWindowStyleChanged(IBinder token, MultiWindowStyle style, int notifyReason) {
            ActivityThread.this.sendMessage(1000, new Pair(token, style), notifyReason);
        }

        public void scheduleMultiWindowFocusChanged(IBinder activityToken, int notifyReason, boolean focus, boolean keepInputMethod) {
            ActivityThread.this.sendMessage(1002, new MultiWindowFocusChangeData(activityToken, notifyReason, focus, keepInputMethod));
        }

        public void scheduleMultiWindowExitByCloseBtn(IBinder activityToken) {
            ActivityThread.this.sendMessage(1003, activityToken);
        }

        public void scheduleActivityDisplayIdChanged(IBinder token, int displayId) {
            if (MultiDisplayFeatures.DEBUG_MULTIDISPLAY) {
                Slog.d(ActivityThread.TAG, "scheduleActivityDisplayIdChanged() : r=" + ((ActivityClientRecord) ActivityThread.this.mActivities.get(token)) + " d" + displayId);
            }
            ActivityThread.this.sendMessage(2000, token, displayId);
        }

        public void scheduleApplicationDisplayIdChanged(int displayId) {
            if (ActivityThread.DEBUG_DUALSCREEN) {
                Slog.d(ActivityThread.TAG, "scheduleApplicationDisplayIdChanged() : d" + displayId);
            }
            ActivityThread.this.sendMessage(2003, null, displayId);
        }

        public void scheduleConfigurationsChanged(UnRestrictedArrayList<Configuration> configs) {
            updatePendingConfigurations(configs);
            ActivityThread.this.sendMessage(2002, configs);
        }

        public void setShrinkRequestedState(boolean shrinkRequested) {
            if (ActivityThread.DEBUG_DUALSCREEN) {
                Slog.d(ActivityThread.TAG, "setShrinkRequestedState() : shrinkRequested=" + shrinkRequested);
            }
            ActivityThread.this.updateShrinkRequestedState(shrinkRequested);
        }

        public void scheduleSendExpandRequest(IBinder token, int notifyReason) {
            if (ActivityThread.DEBUG_DUALSCREEN) {
                Slog.d(ActivityThread.TAG, "scheduleSendExpandRequest() : r=" + ((ActivityClientRecord) ActivityThread.this.mActivities.get(token)) + " notifyReason=" + notifyReason);
            }
            ActivityThread.this.sendMessage(2004, new Pair(token, Integer.valueOf(notifyReason)));
        }

        public void scheduleSendShrinkRequest(IBinder token, int toDisplayId, int notifyReason) {
            DualScreen targetScreen = DualScreen.displayIdToScreen(toDisplayId);
            if (ActivityThread.DEBUG_DUALSCREEN) {
                Slog.d(ActivityThread.TAG, "scheduleSendShrinkRequest() : r=" + ((ActivityClientRecord) ActivityThread.this.mActivities.get(token)) + " notifyReason=" + notifyReason + " d" + toDisplayId);
            }
            ActivityThread.this.sendMessage(2005, new Pair(token, targetScreen), notifyReason);
        }

        public void profilerControl(boolean start, ProfilerInfo profilerInfo, int profileType) {
            ActivityThread.this.sendMessage(127, profilerInfo, start ? 1 : 0, profileType);
        }

        public void dumpHeap(boolean managed, String path, ParcelFileDescriptor fd) {
            DumpHeapData dhd = new DumpHeapData();
            dhd.path = path;
            dhd.fd = fd;
            ActivityThread.handleDumpHeap(managed, dhd);
        }

        public void setSchedulingGroup(int group) {
            try {
                Process.setProcessGroup(Process.myPid(), group);
            } catch (Exception e) {
                Slog.w(ActivityThread.TAG, "Failed setting process group to " + group, e);
            }
        }

        public void dispatchPackageBroadcast(int cmd, String[] packages) {
            ActivityThread.this.sendMessage(133, packages, cmd);
        }

        public void scheduleCrash(String msg) {
            ActivityThread.this.sendMessage(134, msg);
        }

        public void scheduleResetTargetHeapUtilization(IBinder activityToken, String currentComponentName) {
            ActivityThread.this.sendMessage(246, activityToken);
        }

        public void dumpActivity(FileDescriptor fd, IBinder activitytoken, String prefix, String[] args) {
            DumpComponentInfo data = new DumpComponentInfo();
            try {
                data.fd = ParcelFileDescriptor.dup(fd);
                data.token = activitytoken;
                data.prefix = prefix;
                data.args = args;
                ActivityThread.this.sendMessage(136, data, 0, 0, true);
            } catch (IOException e) {
                Slog.w(ActivityThread.TAG, "dumpActivity failed", e);
            }
        }

        public void dumpContextRelationInfo(FileDescriptor fd, String prefix, String[] args) {
            DumpComponentInfo data = new DumpComponentInfo();
            try {
                data.fd = ParcelFileDescriptor.dup(fd);
                data.prefix = prefix;
                data.args = args;
                ActivityThread.this.sendMessage(2001, data, 0, 0, true);
            } catch (IOException e) {
                Slog.w(ActivityThread.TAG, "dumpContextRelationInfo failed", e);
            }
        }

        public void dumpProvider(FileDescriptor fd, IBinder providertoken, String[] args) {
            DumpComponentInfo data = new DumpComponentInfo();
            try {
                data.fd = ParcelFileDescriptor.dup(fd);
                data.token = providertoken;
                data.args = args;
                ActivityThread.this.sendMessage(141, data, 0, 0, true);
            } catch (IOException e) {
                Slog.w(ActivityThread.TAG, "dumpProvider failed", e);
            }
        }

        public void dumpMemInfo(FileDescriptor fd, MemoryInfo mem, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, String[] args) {
            PrintWriter pw = new FastPrintWriter(new FileOutputStream(fd));
            try {
                dumpMemInfo(pw, mem, checkin, dumpFullInfo, dumpDalvik, dumpSummaryOnly);
            } finally {
                pw.flush();
            }
        }

        private void dumpMemInfo(PrintWriter pw, MemoryInfo memInfo, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly) {
            long nativeMax = Debug.getNativeHeapSize() / 1024;
            long nativeAllocated = Debug.getNativeHeapAllocatedSize() / 1024;
            long nativeFree = Debug.getNativeHeapFreeSize() / 1024;
            Runtime runtime = Runtime.getRuntime();
            long dalvikMax = runtime.totalMemory() / 1024;
            long dalvikFree = runtime.freeMemory() / 1024;
            long dalvikAllocated = dalvikMax - dalvikFree;
            long viewInstanceCount = ViewDebug.getViewInstanceCount();
            long viewRootInstanceCount = ViewDebug.getViewRootImplCount();
            long appContextInstanceCount = Debug.countInstancesOfClass(ContextImpl.class);
            long activityInstanceCount = Debug.countInstancesOfClass(Activity.class);
            int globalAssetCount = AssetManager.getGlobalAssetCount();
            int globalAssetManagerCount = AssetManager.getGlobalAssetManagerCount();
            int binderLocalObjectCount = Debug.getBinderLocalObjectCount();
            int binderProxyObjectCount = Debug.getBinderProxyObjectCount();
            int binderDeathObjectCount = Debug.getBinderDeathObjectCount();
            long parcelSize = Parcel.getGlobalAllocSize();
            long parcelCount = Parcel.getGlobalAllocCount();
            long openSslSocketCount = Debug.countInstancesOfClass(OpenSSLSocketImpl.class);
            PagerStats stats = SQLiteDebug.getDatabaseInfo();
            ActivityThread.dumpMemInfoTable(pw, memInfo, checkin, dumpFullInfo, dumpDalvik, dumpSummaryOnly, Process.myPid(), ActivityThread.this.mBoundApplication != null ? ActivityThread.this.mBoundApplication.processName : "unknown", nativeMax, nativeAllocated, nativeFree, dalvikMax, dalvikAllocated, dalvikFree);
            int i;
            if (checkin) {
                pw.print(viewInstanceCount);
                pw.print(',');
                pw.print(viewRootInstanceCount);
                pw.print(',');
                pw.print(appContextInstanceCount);
                pw.print(',');
                pw.print(activityInstanceCount);
                pw.print(',');
                pw.print(globalAssetCount);
                pw.print(',');
                pw.print(globalAssetManagerCount);
                pw.print(',');
                pw.print(binderLocalObjectCount);
                pw.print(',');
                pw.print(binderProxyObjectCount);
                pw.print(',');
                pw.print(binderDeathObjectCount);
                pw.print(',');
                pw.print(openSslSocketCount);
                pw.print(',');
                pw.print(stats.memoryUsed / 1024);
                pw.print(',');
                pw.print(stats.memoryUsed / 1024);
                pw.print(',');
                pw.print(stats.pageCacheOverflow / 1024);
                pw.print(',');
                pw.print(stats.largestMemAlloc / 1024);
                for (i = 0; i < stats.dbStats.size(); i++) {
                    DbStats dbStats = (DbStats) stats.dbStats.get(i);
                    pw.print(',');
                    pw.print(dbStats.dbName);
                    pw.print(',');
                    pw.print(dbStats.pageSize);
                    pw.print(',');
                    pw.print(dbStats.dbSize);
                    pw.print(',');
                    pw.print(dbStats.lookaside);
                    pw.print(',');
                    pw.print(dbStats.cache);
                    pw.print(',');
                    pw.print(dbStats.cache);
                }
                pw.println();
                return;
            }
            pw.println(" ");
            pw.println(" Objects");
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Views:", Long.valueOf(viewInstanceCount), "ViewRootImpl:", Long.valueOf(viewRootInstanceCount));
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "AppContexts:", Long.valueOf(appContextInstanceCount), "Activities:", Long.valueOf(activityInstanceCount));
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Assets:", Integer.valueOf(globalAssetCount), "AssetManagers:", Integer.valueOf(globalAssetManagerCount));
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Local Binders:", Integer.valueOf(binderLocalObjectCount), "Proxy Binders:", Integer.valueOf(binderProxyObjectCount));
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Parcel memory:", Long.valueOf(parcelSize / 1024), "Parcel count:", Long.valueOf(parcelCount));
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "Death Recipients:", Integer.valueOf(binderDeathObjectCount), "OpenSSL Sockets:", Long.valueOf(openSslSocketCount));
            pw.println(" ");
            pw.println(" SQL");
            ActivityThread.printRow(pw, ActivityThread.ONE_COUNT_COLUMN, "MEMORY_USED:", Integer.valueOf(stats.memoryUsed / 1024));
            ActivityThread.printRow(pw, ActivityThread.TWO_COUNT_COLUMNS, "PAGECACHE_OVERFLOW:", Integer.valueOf(stats.pageCacheOverflow / 1024), "MALLOC_SIZE:", Integer.valueOf(stats.largestMemAlloc / 1024));
            pw.println(" ");
            int N = stats.dbStats.size();
            if (N > 0) {
                pw.println(" DATABASES");
                ActivityThread.printRow(pw, DB_INFO_FORMAT, "pgsz", "dbsz", "Lookaside(b)", "cache", "Dbname");
                for (i = 0; i < N; i++) {
                    dbStats = (DbStats) stats.dbStats.get(i);
                    String str = DB_INFO_FORMAT;
                    Object[] objArr = new Object[5];
                    objArr[0] = dbStats.pageSize > 0 ? String.valueOf(dbStats.pageSize) : " ";
                    objArr[1] = dbStats.dbSize > 0 ? String.valueOf(dbStats.dbSize) : " ";
                    objArr[2] = dbStats.lookaside > 0 ? String.valueOf(dbStats.lookaside) : " ";
                    objArr[3] = dbStats.cache;
                    objArr[4] = dbStats.dbName;
                    ActivityThread.printRow(pw, str, objArr);
                }
            }
            String assetAlloc = AssetManager.getAssetAllocations();
            if (assetAlloc != null) {
                pw.println(" ");
                pw.println(" Asset Allocations");
                pw.print(assetAlloc);
            }
        }

        public void dumpGfxInfo(FileDescriptor fd, String[] args) {
            ActivityThread.this.dumpGraphicsInfo(fd);
            WindowManagerGlobal.getInstance().dumpGfxInfo(fd, args);
        }

        private void dumpDatabaseInfo(FileDescriptor fd, String[] args) {
            PrintWriter pw = new FastPrintWriter(new FileOutputStream(fd));
            SQLiteDebug.dump(new PrintWriterPrinter(pw), args);
            pw.flush();
        }

        public void dumpDbInfo(final FileDescriptor fd, final String[] args) {
            if (ActivityThread.this.mSystemThread) {
                AsyncTask.THREAD_POOL_EXECUTOR.execute(new Runnable() {
                    public void run() {
                        ApplicationThread.this.dumpDatabaseInfo(fd, args);
                    }
                });
            } else {
                dumpDatabaseInfo(fd, args);
            }
        }

        public void unstableProviderDied(IBinder provider) {
            ActivityThread.this.sendMessage(142, provider);
        }

        public void requestAssistContextExtras(IBinder activityToken, IBinder requestToken, int requestType) {
            RequestAssistContextExtras cmd = new RequestAssistContextExtras();
            cmd.activityToken = activityToken;
            cmd.requestToken = requestToken;
            cmd.requestType = requestType;
            ActivityThread.this.sendMessage(143, cmd);
        }

        public void setCoreSettings(Bundle coreSettings) {
            ActivityThread.this.sendMessage(138, coreSettings);
        }

        public void updatePackageCompatibilityInfo(String pkg, CompatibilityInfo info) {
            UpdateCompatibilityData ucd = new UpdateCompatibilityData();
            ucd.pkg = pkg;
            ucd.info = info;
            ActivityThread.this.sendMessage(139, ucd);
        }

        public void scheduleTrimMemory(int level) {
            ActivityThread.this.sendMessage(140, null, level);
        }

        public void scheduleTranslucentConversionComplete(IBinder token, boolean drawComplete) {
            ActivityThread.this.sendMessage(144, token, drawComplete ? 1 : 0);
        }

        public void scheduleOnNewActivityOptions(IBinder token, ActivityOptions options) {
            ActivityThread.this.sendMessage(146, new Pair(token, options));
        }

        public void setProcessState(int state) {
            updateProcessState(state, true);
        }

        public void updateProcessState(int processState, boolean fromIpc) {
            synchronized (this) {
                if (this.mLastProcessState != processState) {
                    this.mLastProcessState = processState;
                    int dalvikProcessState = 1;
                    if (processState <= 6) {
                        dalvikProcessState = 0;
                    }
                    VMRuntime.getRuntime().updateProcessState(dalvikProcessState);
                }
            }
        }

        public void scheduleInstallProvider(ProviderInfo provider) {
            ActivityThread.this.sendMessage(145, provider);
        }

        public void scheduleDisplayMetricsChanged(DisplayInfo dInfo) {
            ActivityThread.this.sendMessage(1100, dInfo);
        }

        public final void updateTimePrefs(boolean is24Hour) {
            DateFormat.set24HourTimePref(is24Hour);
        }

        public void scheduleCancelVisibleBehind(IBinder token) {
            ActivityThread.this.sendMessage(147, token);
        }

        public void scheduleBackgroundVisibleBehindChanged(IBinder token, boolean visible) {
            ActivityThread.this.sendMessage(148, token, visible ? 1 : 0);
        }

        public void scheduleEnterAnimationComplete(IBinder token) {
            ActivityThread.this.sendMessage(149, token);
        }

        public void notifyCleartextNetwork(byte[] firstPacket) {
            if (StrictMode.vmCleartextNetworkEnabled()) {
                StrictMode.onCleartextNetworkDetected(firstPacket);
            }
        }
    }

    static final class BindServiceData {
        Intent intent;
        boolean rebind;
        IBinder token;

        BindServiceData() {
        }

        public String toString() {
            return "BindServiceData{token=" + this.token + " intent=" + this.intent + "}";
        }
    }

    static final class ContextCleanupInfo {
        ContextImpl context;
        String what;
        String who;

        ContextCleanupInfo() {
        }
    }

    static final class CreateBackupAgentData {
        ApplicationInfo appInfo;
        int backupMode;
        CompatibilityInfo compatInfo;

        CreateBackupAgentData() {
        }

        public String toString() {
            return "CreateBackupAgentData{appInfo=" + this.appInfo + " backupAgent=" + this.appInfo.backupAgentName + " mode=" + this.backupMode + "}";
        }
    }

    static final class CreateServiceData {
        IBinder callerActivityToken;
        CompatibilityInfo compatInfo;
        int displayId;
        ServiceInfo info;
        Intent intent;
        IBinder token;

        CreateServiceData() {
        }

        public String toString() {
            return "CreateServiceData{token=" + this.token + " className=" + this.info.name + " packageName=" + this.info.packageName + " intent=" + this.intent + "}";
        }
    }

    private class DropBoxReporter implements Reporter {
        private DropBoxManager dropBox;

        public void addData(String tag, byte[] data, int flags) {
            ensureInitialized();
            this.dropBox.addData(tag, data, flags);
        }

        public void addText(String tag, String data) {
            ensureInitialized();
            this.dropBox.addText(tag, data);
        }

        private synchronized void ensureInitialized() {
            if (this.dropBox == null) {
                this.dropBox = (DropBoxManager) ActivityThread.this.getSystemContext().getSystemService(Context.DROPBOX_SERVICE);
            }
        }
    }

    static final class DumpComponentInfo {
        String[] args;
        ParcelFileDescriptor fd;
        String prefix;
        IBinder token;

        DumpComponentInfo() {
        }
    }

    static final class DumpHeapData {
        ParcelFileDescriptor fd;
        String path;

        DumpHeapData() {
        }
    }

    private static class EventLoggingReporter implements EventLogger.Reporter {
        private EventLoggingReporter() {
        }

        public void report(int code, Object... list) {
            EventLog.writeEvent(code, list);
        }
    }

    final class GcIdler implements IdleHandler {
        GcIdler() {
        }

        public final boolean queueIdle() {
            ActivityThread.this.doGcIfNeeded();
            return false;
        }
    }

    private class H extends Handler {
        public static final int ACTIVITY_CONFIGURATION_CHANGED = 125;
        public static final int ACTIVITY_DISPLAY_ID_CHANGED = 2000;
        public static final int APPLICATION_DISPLAY_ID_CHANGED = 2003;
        public static final int APPLOCK_START_CHECK_ACTIVITY = 3000;
        public static final int BACKGROUND_VISIBLE_BEHIND_CHANGED = 148;
        public static final int BIND_APPLICATION = 110;
        public static final int BIND_SERVICE = 121;
        public static final int CANCEL_VISIBLE_BEHIND = 147;
        public static final int CLEAN_UP_CONTEXT = 119;
        public static final int CONFIGURATIONS_CHANGED = 2002;
        public static final int CONFIGURATION_CHANGED = 118;
        public static final int CREATE_BACKUP_AGENT = 128;
        public static final int CREATE_SERVICE = 114;
        public static final int DESTROY_ACTIVITY = 109;
        public static final int DESTROY_BACKUP_AGENT = 129;
        public static final int DISPATCH_PACKAGE_BROADCAST = 133;
        public static final int DISPLAYMETRICS_CHANGED = 1100;
        public static final int DUMP_ACTIVITY = 136;
        public static final int DUMP_CONTEXT_RELATION_INFO = 2001;
        public static final int DUMP_HEAP = 135;
        public static final int DUMP_PROVIDER = 141;
        public static final int DUMP_SERVICE = 123;
        public static final int ENABLE_JIT = 132;
        public static final int ENTER_ANIMATION_COMPLETE = 149;
        public static final int EXIT_APPLICATION = 111;
        public static final int FORCE_RESUME_ACTIVITY = 151;
        public static final int GC_WHEN_IDLE = 120;
        public static final int HIDE_WINDOW = 106;
        public static final int INSTALL_PROVIDER = 145;
        public static final int LAUNCH_ACTIVITY = 100;
        public static final int LOW_MEMORY = 124;
        public static final int MULTI_WINDOW_CONFIGURATION_CHANGED = 1001;
        public static final int MULTI_WINDOW_EXIT_BY_CLOSE_BTN = 1003;
        public static final int MULTI_WINDOW_FOCUS_CHANGED = 1002;
        public static final int MULTI_WINDOW_STYLE_CHANGED = 1000;
        public static final int NEW_INTENT = 112;
        public static final int ON_NEW_ACTIVITY_OPTIONS = 146;
        public static final int PAUSE_ACTIVITY = 101;
        public static final int PAUSE_ACTIVITY_FINISHING = 102;
        public static final int PROFILER_CONTROL = 127;
        public static final int RECEIVER = 113;
        public static final int RELAUNCH_ACTIVITY = 126;
        public static final int REMOVE_PROVIDER = 131;
        public static final int REQUEST_ASSIST_CONTEXT_EXTRAS = 143;
        public static final int RESET_TARGET_HEAP_UTILIZATION = 246;
        public static final int RESUME_ACTIVITY = 107;
        public static final int SCHEDULE_CRASH = 134;
        public static final int SEND_EXPAND_REQUEST = 2004;
        public static final int SEND_RESULT = 108;
        public static final int SEND_SHRINK_REQUEST = 2005;
        public static final int SERVICE_ARGS = 115;
        public static final int SET_CORE_SETTINGS = 138;
        public static final int SHOW_WINDOW = 105;
        public static final int SLEEPING = 137;
        public static final int STOP_ACTIVITY_HIDE = 104;
        public static final int STOP_ACTIVITY_SHOW = 103;
        public static final int STOP_SERVICE = 116;
        public static final int SUICIDE = 130;
        public static final int TRANSLUCENT_CONVERSION_COMPLETE = 144;
        public static final int TRIM_MEMORY = 140;
        public static final int UNBIND_SERVICE = 122;
        public static final int UNSTABLE_PROVIDER_DIED = 142;
        public static final int UPDATE_OVERLAY_PATH = 150;
        public static final int UPDATE_PACKAGE_COMPATIBILITY_INFO = 139;

        private H() {
        }

        String codeToString(int code) {
            return Integer.toString(code);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    Trace.traceBegin(64, "activityStart");
                    ActivityClientRecord r = msg.obj;
                    r.packageInfo = ActivityThread.this.getPackageInfoNoCheck(r.activityInfo.applicationInfo, r.compatInfo);
                    ActivityThread.this.handleLaunchActivity(r, null);
                    Trace.traceEnd(64);
                    return;
                case 101:
                    Trace.traceBegin(64, "activityPause");
                    ActivityThread.this.handlePauseActivity((IBinder) msg.obj, false, (msg.arg1 & 1) != 0, msg.arg2, (msg.arg1 & 2) != 0);
                    maybeSnapshot();
                    Trace.traceEnd(64);
                    return;
                case 102:
                    Trace.traceBegin(64, "activityPause");
                    ActivityThread.this.handlePauseActivity((IBinder) msg.obj, true, (msg.arg1 & 1) != 0, msg.arg2, (msg.arg1 & 1) != 0);
                    Trace.traceEnd(64);
                    return;
                case 103:
                    Trace.traceBegin(64, "activityStop");
                    ActivityThread.this.handleStopActivity((IBinder) msg.obj, true, msg.arg2);
                    Trace.traceEnd(64);
                    return;
                case 104:
                    Trace.traceBegin(64, "activityStop");
                    ActivityThread.this.handleStopActivity((IBinder) msg.obj, false, msg.arg2);
                    Trace.traceEnd(64);
                    return;
                case 105:
                    Trace.traceBegin(64, "activityShowWindow");
                    ActivityThread.this.handleWindowVisibility((IBinder) msg.obj, true);
                    Trace.traceEnd(64);
                    return;
                case 106:
                    Trace.traceBegin(64, "activityHideWindow");
                    ActivityThread.this.handleWindowVisibility((IBinder) msg.obj, false);
                    Trace.traceEnd(64);
                    return;
                case 107:
                    Trace.traceBegin(64, "activityResume");
                    ActivityThread.this.handleResumeActivity((IBinder) msg.obj, true, msg.arg1 != 0, true);
                    Trace.traceEnd(64);
                    return;
                case 108:
                    Trace.traceBegin(64, "activityDeliverResult");
                    ActivityThread.this.handleSendResult((ResultData) msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 109:
                    Trace.traceBegin(64, "activityDestroy");
                    ActivityThread.this.handleDestroyActivity((IBinder) msg.obj, msg.arg1 != 0, msg.arg2, false);
                    Trace.traceEnd(64);
                    return;
                case 110:
                    Trace.traceBegin(64, "bindApplication");
                    ActivityThread.this.handleBindApplication(msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 111:
                    if (ActivityThread.this.mInitialApplication != null) {
                        ActivityThread.this.mInitialApplication.onTerminate();
                    }
                    Looper.myLooper().quit();
                    return;
                case 112:
                    Trace.traceBegin(64, "activityNewIntent");
                    ActivityThread.this.handleNewIntent((NewIntentData) msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 113:
                    Trace.traceBegin(64, "broadcastReceiveComp");
                    ActivityThread.this.handleReceiver((ReceiverData) msg.obj);
                    maybeSnapshot();
                    Trace.traceEnd(64);
                    return;
                case 114:
                    Trace.traceBegin(64, "serviceCreate");
                    ActivityThread.this.handleCreateService((CreateServiceData) msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 115:
                    Trace.traceBegin(64, "serviceStart");
                    ActivityThread.this.handleServiceArgs((ServiceArgsData) msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 116:
                    Trace.traceBegin(64, "serviceStop");
                    ActivityThread.this.handleStopService((IBinder) msg.obj);
                    maybeSnapshot();
                    Trace.traceEnd(64);
                    return;
                case 118:
                    Trace.traceBegin(64, "configChanged");
                    ActivityThread.this.mCurDefaultDisplayDpi = ((Configuration) msg.obj).densityDpi;
                    ActivityThread.this.handleConfigurationChanged((Configuration) msg.obj, null);
                    Trace.traceEnd(64);
                    return;
                case 119:
                    ContextCleanupInfo cci = msg.obj;
                    cci.context.performFinalCleanup(cci.who, cci.what);
                    return;
                case 120:
                    ActivityThread.this.scheduleGcIdler();
                    return;
                case 121:
                    Trace.traceBegin(64, "serviceBind");
                    ActivityThread.this.handleBindService((BindServiceData) msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 122:
                    Trace.traceBegin(64, "serviceUnbind");
                    ActivityThread.this.handleUnbindService((BindServiceData) msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 123:
                    ActivityThread.this.handleDumpService((DumpComponentInfo) msg.obj);
                    return;
                case 124:
                    Trace.traceBegin(64, "lowMemory");
                    ActivityThread.this.handleLowMemory();
                    Trace.traceEnd(64);
                    return;
                case 125:
                    Trace.traceBegin(64, "activityConfigChanged");
                    ActivityThread.this.handleActivityConfigurationChanged((ActivityConfigChangeData) msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 126:
                    Trace.traceBegin(64, "activityRestart");
                    ActivityThread.this.handleRelaunchActivity((ActivityClientRecord) msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 127:
                    ActivityThread.this.handleProfilerControl(msg.arg1 != 0, (ProfilerInfo) msg.obj, msg.arg2);
                    return;
                case 128:
                    Trace.traceBegin(64, "backupCreateAgent");
                    ActivityThread.this.handleCreateBackupAgent((CreateBackupAgentData) msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 129:
                    Trace.traceBegin(64, "backupDestroyAgent");
                    ActivityThread.this.handleDestroyBackupAgent((CreateBackupAgentData) msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 130:
                    Process.killProcess(Process.myPid());
                    return;
                case 131:
                    Trace.traceBegin(64, "providerRemove");
                    ActivityThread.this.completeRemoveProvider((ProviderRefCount) msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 132:
                    ActivityThread.this.ensureJitEnabled();
                    return;
                case 133:
                    Trace.traceBegin(64, "broadcastPackage");
                    ActivityThread.this.handleDispatchPackageBroadcast(msg.arg1, (String[]) msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 134:
                    throw new RemoteServiceException((String) msg.obj);
                case 135:
                    ActivityThread.handleDumpHeap(msg.arg1 != 0, (DumpHeapData) msg.obj);
                    return;
                case 136:
                    ActivityThread.this.handleDumpActivity((DumpComponentInfo) msg.obj);
                    return;
                case 137:
                    Trace.traceBegin(64, "sleeping");
                    ActivityThread.this.handleSleeping((IBinder) msg.obj, msg.arg1 != 0);
                    Trace.traceEnd(64);
                    return;
                case 138:
                    Trace.traceBegin(64, "setCoreSettings");
                    ActivityThread.this.handleSetCoreSettings((Bundle) msg.obj);
                    Trace.traceEnd(64);
                    return;
                case 139:
                    ActivityThread.this.handleUpdatePackageCompatibilityInfo((UpdateCompatibilityData) msg.obj);
                    return;
                case 140:
                    Trace.traceBegin(64, "trimMemory");
                    ActivityThread.this.handleTrimMemory(msg.arg1);
                    Trace.traceEnd(64);
                    return;
                case 141:
                    ActivityThread.this.handleDumpProvider((DumpComponentInfo) msg.obj);
                    return;
                case 142:
                    ActivityThread.this.handleUnstableProviderDied((IBinder) msg.obj, false);
                    return;
                case 143:
                    ActivityThread.this.handleRequestAssistContextExtras((RequestAssistContextExtras) msg.obj);
                    return;
                case 144:
                    ActivityThread.this.handleTranslucentConversionComplete((IBinder) msg.obj, msg.arg1 == 1);
                    return;
                case 145:
                    ActivityThread.this.handleInstallProvider((ProviderInfo) msg.obj);
                    return;
                case 146:
                    Pair<IBinder, ActivityOptions> pair = msg.obj;
                    ActivityThread.this.onNewActivityOptions((IBinder) pair.first, (ActivityOptions) pair.second);
                    return;
                case 147:
                    ActivityThread.this.handleCancelVisibleBehind((IBinder) msg.obj);
                    return;
                case 148:
                    ActivityThread.this.handleOnBackgroundVisibleBehindChanged((IBinder) msg.obj, msg.arg1 > 0);
                    return;
                case 149:
                    ActivityThread.this.handleEnterAnimationComplete((IBinder) msg.obj);
                    return;
                case 150:
                    Pair<String, String> pathPackagePair = msg.obj;
                    ActivityThread.this.handleUpdateOverlayPath(pathPackagePair.first, pathPackagePair.second, msg.arg1);
                    return;
                case 151:
                    Trace.traceBegin(64, "activityForceResume");
                    ActivityThread.this.handleForceCallResumeActivity((IBinder) msg.obj, true);
                    Trace.traceEnd(64);
                    return;
                case 246:
                    ActivityThread.this.handleResetTargetHeapUtilization((IBinder) msg.obj);
                    return;
                case 1000:
                    Pair<IBinder, MultiWindowStyle> pair2 = msg.obj;
                    handleMultiWindowStyleChanged((IBinder) pair2.first, (MultiWindowStyle) pair2.second, msg.arg1);
                    return;
                case 1001:
                    handleMultiWindowConfigurationChanaged(msg.arg1);
                    return;
                case 1002:
                    MultiWindowFocusChangeData param = msg.obj;
                    handleMultiWindowFocusChanged(param.activityToken, param.notifyReason, param.focus, param.keepInputMethod);
                    return;
                case 1003:
                    handleMultiWindowExitByCloseBtn((IBinder) msg.obj);
                    return;
                case 1100:
                    ActivityThread.this.handleDisplayMetricsChanged((DisplayInfo) msg.obj);
                    return;
                case 2000:
                    ActivityThread.this.handleActivityDisplayIdChanged((IBinder) msg.obj, msg.arg1);
                    return;
                case 2001:
                    ActivityThread.this.handleDumpContextRelationInfo((DumpComponentInfo) msg.obj);
                    return;
                case 2002:
                    UnRestrictedArrayList<Configuration> configs = (UnRestrictedArrayList) msg.obj;
                    Iterator i$ = configs.iterator();
                    while (i$.hasNext()) {
                        Configuration config = (Configuration) i$.next();
                        if (config != null) {
                            ActivityThread.this.mCurDefaultDisplayDpis.set(config.displayId, Integer.valueOf(config.densityDpi));
                        }
                    }
                    ActivityThread.this.handleConfigurationsChanged(configs, null);
                    return;
                case 2003:
                    ActivityThread.this.handleApplicationDisplayIdChanged(msg.arg1);
                    return;
                case 2004:
                    Pair<IBinder, Integer> pair3 = msg.obj;
                    ActivityThread.this.handleSendExpandRequest((IBinder) pair3.first, ((Integer) pair3.second).intValue());
                    return;
                case 2005:
                    Pair<IBinder, DualScreen> pair4 = msg.obj;
                    ActivityThread.this.handleSendShrinkRequest((IBinder) pair4.first, (DualScreen) pair4.second, msg.arg1);
                    return;
                case 3000:
                    ActivityThread.this.checkAppLockState((ActivityClientRecord) msg.obj);
                    return;
                default:
                    return;
            }
        }

        private void handleMultiWindowStyleChanged(IBinder token, MultiWindowStyle style, int notifyReason) {
            ActivityClientRecord r = (ActivityClientRecord) ActivityThread.this.mActivities.get(token);
            if (r != null && r.activity != null) {
                switch (notifyReason) {
                    case 1:
                        if (style.isEnabled(4) && !r.activity.isFinishing()) {
                            Window window = r.activity.getWindow();
                            if (!(window == null || (window.getAttributes().privateFlags & 16) == 0)) {
                                r.activity.finish();
                                return;
                            }
                        }
                    case 4:
                        if (style.isEnabled(262144)) {
                            r.activity.finish();
                            return;
                        }
                        break;
                }
                r.multiWindowStyle.setTo(style, true);
                r.activity.mMultiWindowStyle.setTo(style, true);
                r.activity.mMultiWindowStyle.setAppRequestOrientation(style.getAppRequestOrientation(), true);
                r.activity.onMultiWindowStyleChanged(style, notifyReason);
            }
        }

        private void handleMultiWindowConfigurationChanaged(int configDiff) {
            if ((configDiff & 384) != 0) {
                for (Object key : ActivityThread.this.mActivities.keySet().toArray()) {
                    ActivityClientRecord r = (ActivityClientRecord) ActivityThread.this.mActivities.get(key);
                    if (!(r.activity == null || r.activity.getMultiWindowStyle() == null)) {
                        r.activity.onMultiWindowConfigurationChanged(configDiff);
                    }
                }
            }
        }

        private void handleMultiWindowFocusChanged(IBinder token, int notifyReason, boolean focus, boolean keepInputMethod) {
            ActivityClientRecord r = (ActivityClientRecord) ActivityThread.this.mActivities.get(token);
            if (r != null && r.activity != null) {
                r.activity.onMultiWindowFocusChanged(notifyReason, focus, keepInputMethod);
            }
        }

        private void handleMultiWindowExitByCloseBtn(IBinder token) {
            ActivityClientRecord r = (ActivityClientRecord) ActivityThread.this.mActivities.get(token);
            if (r != null && r.activity != null) {
                r.activity.exitByCloseBtn();
            }
        }

        private void maybeSnapshot() {
            if (ActivityThread.this.mBoundApplication != null && SamplingProfilerIntegration.isEnabled()) {
                String packageName = ActivityThread.this.mBoundApplication.info.mPackageName;
                PackageInfo packageInfo = null;
                try {
                    Context context = ActivityThread.this.getSystemContext();
                    if (context == null) {
                        Log.e(ActivityThread.TAG, "cannot get a valid context");
                        return;
                    }
                    PackageManager pm = context.getPackageManager();
                    if (pm == null) {
                        Log.e(ActivityThread.TAG, "cannot get a valid PackageManager");
                        return;
                    }
                    packageInfo = pm.getPackageInfo(packageName, 1);
                    SamplingProfilerIntegration.writeSnapshot(ActivityThread.this.mBoundApplication.processName, packageInfo);
                } catch (NameNotFoundException e) {
                    Log.e(ActivityThread.TAG, "cannot get package info for " + packageName, e);
                }
            }
        }
    }

    private class Idler implements IdleHandler {
        private Idler() {
        }

        public final boolean queueIdle() {
            ActivityThread.niceDown();
            ActivityClientRecord a = ActivityThread.this.mNewActivities;
            boolean stopProfiling = false;
            if (!(ActivityThread.this.mBoundApplication == null || ActivityThread.this.mProfiler.profileFd == null || !ActivityThread.this.mProfiler.autoStopProfiler)) {
                stopProfiling = true;
            }
            if (a != null) {
                ActivityThread.this.mNewActivities = null;
                IActivityManager am = ActivityManagerNative.getDefault();
                do {
                    if (!(a.activity == null || a.activity.mFinished)) {
                        try {
                            if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && ActivityThread.this.isSelectiveOrientationStateInner(a)) {
                                Configuration copyConfig = null;
                                if (!(a.createdConfig == null || a.overrideConfig == null)) {
                                    copyConfig = new Configuration(a.createdConfig);
                                    copyConfig.orientation = a.overrideConfig.orientation;
                                }
                                am.activityIdle(a.token, copyConfig, stopProfiling);
                            } else {
                                am.activityIdle(a.token, a.createdConfig, stopProfiling);
                            }
                            a.createdConfig = null;
                        } catch (RemoteException e) {
                        }
                    }
                    ActivityClientRecord prev = a;
                    a = a.nextIdle;
                    prev.nextIdle = null;
                } while (a != null);
            }
            if (stopProfiling) {
                ActivityThread.this.mProfiler.stopProfiling();
            }
            ActivityThread.this.ensureJitEnabled();
            return false;
        }
    }

    static final class MultiWindowFocusChangeData {
        final IBinder activityToken;
        boolean focus;
        boolean keepInputMethod;
        int notifyReason;

        public MultiWindowFocusChangeData(IBinder token, int _notifyReason, boolean _focus, boolean _keepInputMethod) {
            this.activityToken = token;
            this.notifyReason = _notifyReason;
            this.focus = _focus;
            this.keepInputMethod = _keepInputMethod;
        }
    }

    static final class NewIntentData {
        List<ReferrerIntent> intents;
        IBinder token;

        NewIntentData() {
        }

        public String toString() {
            return "NewIntentData{intents=" + this.intents + " token=" + this.token + "}";
        }
    }

    static final class Profiler {
        boolean autoStopProfiler;
        boolean handlingProfiling;
        ParcelFileDescriptor profileFd;
        String profileFile;
        boolean profiling;
        int samplingInterval;

        Profiler() {
        }

        public void setProfiler(ProfilerInfo profilerInfo) {
            ParcelFileDescriptor fd = profilerInfo.profileFd;
            if (!this.profiling) {
                if (this.profileFd != null) {
                    try {
                        this.profileFd.close();
                    } catch (IOException e) {
                    }
                }
                this.profileFile = profilerInfo.profileFile;
                this.profileFd = fd;
                this.samplingInterval = profilerInfo.samplingInterval;
                this.autoStopProfiler = profilerInfo.autoStopProfiler;
            } else if (fd != null) {
                try {
                    fd.close();
                } catch (IOException e2) {
                }
            }
        }

        public void startProfiling() {
            boolean z = true;
            if (this.profileFd != null && !this.profiling) {
                try {
                    String str = this.profileFile;
                    FileDescriptor fileDescriptor = this.profileFd.getFileDescriptor();
                    if (this.samplingInterval == 0) {
                        z = false;
                    }
                    VMDebug.startMethodTracing(str, fileDescriptor, 8388608, 0, z, this.samplingInterval);
                    this.profiling = true;
                } catch (RuntimeException e) {
                    Slog.w(ActivityThread.TAG, "Profiling failed on path " + this.profileFile);
                    try {
                        this.profileFd.close();
                        this.profileFd = null;
                    } catch (IOException e2) {
                        Slog.w(ActivityThread.TAG, "Failure closing profile fd", e2);
                    }
                }
            }
        }

        public void stopProfiling() {
            if (this.profiling) {
                this.profiling = false;
                Debug.stopMethodTracing();
                if (this.profileFd != null) {
                    try {
                        this.profileFd.close();
                    } catch (IOException e) {
                    }
                }
                this.profileFd = null;
                this.profileFile = null;
            }
        }
    }

    final class ProviderClientRecord {
        final ContentProviderHolder mHolder;
        final ContentProvider mLocalProvider;
        final String[] mNames;
        final IContentProvider mProvider;

        ProviderClientRecord(String[] names, IContentProvider provider, ContentProvider localProvider, ContentProviderHolder holder) {
            this.mNames = names;
            this.mProvider = provider;
            this.mLocalProvider = localProvider;
            this.mHolder = holder;
        }
    }

    private static final class ProviderKey {
        final String authority;
        final int userId;

        public ProviderKey(String authority, int userId) {
            this.authority = authority;
            this.userId = userId;
        }

        public boolean equals(Object o) {
            if (!(o instanceof ProviderKey)) {
                return false;
            }
            ProviderKey other = (ProviderKey) o;
            if (Objects.equals(this.authority, other.authority) && this.userId == other.userId) {
                return true;
            }
            return false;
        }

        public int hashCode() {
            return (this.authority != null ? this.authority.hashCode() : 0) ^ this.userId;
        }
    }

    private static final class ProviderRefCount {
        public final ProviderClientRecord client;
        public final ContentProviderHolder holder;
        public boolean removePending;
        public int stableCount;
        public int unstableCount;

        ProviderRefCount(ContentProviderHolder inHolder, ProviderClientRecord inClient, int sCount, int uCount) {
            this.holder = inHolder;
            this.client = inClient;
            this.stableCount = sCount;
            this.unstableCount = uCount;
        }
    }

    static final class ReceiverData extends PendingResult {
        CompatibilityInfo compatInfo;
        ActivityInfo info;
        Intent intent;

        public ReceiverData(Intent intent, int resultCode, String resultData, Bundle resultExtras, boolean ordered, boolean sticky, IBinder token, int sendingUser) {
            super(resultCode, resultData, resultExtras, 0, ordered, sticky, token, sendingUser, intent.getBroadcastQueueHint());
            this.intent = intent;
        }

        public String toString() {
            return "ReceiverData{intent=" + this.intent + " packageName=" + this.info.packageName + " resultCode=" + getResultCode() + " resultData=" + getResultData() + " resultExtras=" + getResultExtras(false) + "}";
        }
    }

    static final class RequestAssistContextExtras {
        IBinder activityToken;
        IBinder requestToken;
        int requestType;

        RequestAssistContextExtras() {
        }
    }

    static final class ResultData {
        List<ResultInfo> results;
        IBinder token;

        ResultData() {
        }

        public String toString() {
            return "ResultData{token=" + this.token + " results" + this.results + "}";
        }
    }

    static final class ServiceArgsData {
        Intent args;
        int flags;
        int startId;
        boolean taskRemoved;
        IBinder token;

        ServiceArgsData() {
        }

        public String toString() {
            return "ServiceArgsData{token=" + this.token + " startId=" + this.startId + " args=" + this.args + "}";
        }
    }

    private static class StopInfo implements Runnable {
        ActivityClientRecord activity;
        CharSequence description;
        PersistableBundle persistentState;
        Bundle state;

        private StopInfo() {
        }

        public void run() {
            try {
                ActivityManagerNative.getDefault().activityStopped(this.activity.token, this.state, this.persistentState, this.description);
            } catch (RemoteException e) {
            }
        }
    }

    static final class UpdateCompatibilityData {
        CompatibilityInfo info;
        String pkg;

        UpdateCompatibilityData() {
        }
    }

    private class mRunnable implements Runnable {
        String actname;
        private Context c;
        File filename;

        mRunnable(Context context, String activityName, File rFile) {
            this.c = context;
            this.actname = activityName;
            this.filename = rFile;
        }

        public void run() {
            Process.setThreadPriority(-11);
            this.c.getResources().startRC(this.c, this.actname, this.filename);
        }
    }

    private native void dumpGraphicsInfo(FileDescriptor fileDescriptor);

    static {
        boolean z = false;
        if (DualScreenManager.DEBUG) {
        }
        if (Debug.isProductShip() != 1) {
            z = true;
        }
        SAFE_DEBUG = z;
    }

    public static ActivityThread currentActivityThread() {
        return sCurrentActivityThread;
    }

    public static boolean isSystem() {
        return sCurrentActivityThread != null ? sCurrentActivityThread.mSystemThread : false;
    }

    public static String currentOpPackageName() {
        ActivityThread am = currentActivityThread();
        return (am == null || am.getApplication() == null) ? null : am.getApplication().getOpPackageName();
    }

    public static String currentPackageName() {
        ActivityThread am = currentActivityThread();
        return (am == null || am.mBoundApplication == null) ? null : am.mBoundApplication.appInfo.packageName;
    }

    public static String currentProcessName() {
        ActivityThread am = currentActivityThread();
        return (am == null || am.mBoundApplication == null) ? null : am.mBoundApplication.processName;
    }

    public static Application currentApplication() {
        ActivityThread am = currentActivityThread();
        return am != null ? am.mInitialApplication : null;
    }

    public static IPackageManager getPackageManager() {
        if (sPackageManager != null) {
            return sPackageManager;
        }
        sPackageManager = Stub.asInterface(ServiceManager.getService("package"));
        return sPackageManager;
    }

    Configuration applyConfigCompatMainThread(int displayDensity, Configuration config, CompatibilityInfo compat) {
        if (config == null) {
            return null;
        }
        if (!compat.supportsScreen()) {
            this.mMainThreadConfig.setTo(config);
            config = this.mMainThreadConfig;
            compat.applyToConfiguration(displayDensity, config);
        }
        return config;
    }

    public Configuration getDisplayConfiguration(int displayId) {
        return (Configuration) this.mConfigurations.get(displayId);
    }

    Resources getTopLevelResources(String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, Configuration overrideConfiguration, LoadedApk pkgInfo) {
        return getTopLevelResources(resDir, splitResDirs, overlayDirs, libDirs, displayId, overrideConfiguration, pkgInfo, UserHandle.myUserId());
    }

    Resources getTopLevelResources(String resDir, String[] splitResDirs, String[] overlayDirs, String[] libDirs, int displayId, Configuration overrideConfiguration, LoadedApk pkgInfo, int userId) {
        return this.mResourcesManager.getTopLevelResources(resDir, splitResDirs, overlayDirs, libDirs, displayId, overrideConfiguration, pkgInfo.getCompatibilityInfo(), pkgInfo.mPackageName, userId);
    }

    final Handler getHandler() {
        return this.mH;
    }

    public final LoadedApk getPackageInfo(String packageName, CompatibilityInfo compatInfo, int flags) {
        return getPackageInfo(packageName, compatInfo, flags, UserHandle.myUserId());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.app.LoadedApk getPackageInfo(java.lang.String r9, android.content.res.CompatibilityInfo r10, int r11, int r12) {
        /*
        r8 = this;
        r5 = 0;
        r4 = android.os.UserHandle.myUserId();
        if (r4 == r12) goto L_0x006d;
    L_0x0007:
        r1 = 1;
    L_0x0008:
        r6 = r8.mResourcesManager;
        monitor-enter(r6);
        if (r1 == 0) goto L_0x006f;
    L_0x000d:
        r3 = 0;
    L_0x000e:
        if (r3 == 0) goto L_0x0085;
    L_0x0010:
        r4 = r3.get();	 Catch:{ all -> 0x006a }
        r4 = (android.app.LoadedApk) r4;	 Catch:{ all -> 0x006a }
        r2 = r4;
    L_0x0017:
        if (r2 == 0) goto L_0x0089;
    L_0x0019:
        r4 = r2.mResources;	 Catch:{ all -> 0x006a }
        if (r4 == 0) goto L_0x0029;
    L_0x001d:
        r4 = r2.mResources;	 Catch:{ all -> 0x006a }
        r4 = r4.getAssets();	 Catch:{ all -> 0x006a }
        r4 = r4.isUpToDate();	 Catch:{ all -> 0x006a }
        if (r4 == 0) goto L_0x0089;
    L_0x0029:
        r4 = r2.isSecurityViolation();	 Catch:{ all -> 0x006a }
        if (r4 == 0) goto L_0x0087;
    L_0x002f:
        r4 = r11 & 2;
        if (r4 != 0) goto L_0x0087;
    L_0x0033:
        r4 = new java.lang.SecurityException;	 Catch:{ all -> 0x006a }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x006a }
        r5.<init>();	 Catch:{ all -> 0x006a }
        r7 = "Requesting code from ";
        r5 = r5.append(r7);	 Catch:{ all -> 0x006a }
        r5 = r5.append(r9);	 Catch:{ all -> 0x006a }
        r7 = " to be run in process ";
        r5 = r5.append(r7);	 Catch:{ all -> 0x006a }
        r7 = r8.mBoundApplication;	 Catch:{ all -> 0x006a }
        r7 = r7.processName;	 Catch:{ all -> 0x006a }
        r5 = r5.append(r7);	 Catch:{ all -> 0x006a }
        r7 = "/";
        r5 = r5.append(r7);	 Catch:{ all -> 0x006a }
        r7 = r8.mBoundApplication;	 Catch:{ all -> 0x006a }
        r7 = r7.appInfo;	 Catch:{ all -> 0x006a }
        r7 = r7.uid;	 Catch:{ all -> 0x006a }
        r5 = r5.append(r7);	 Catch:{ all -> 0x006a }
        r5 = r5.toString();	 Catch:{ all -> 0x006a }
        r4.<init>(r5);	 Catch:{ all -> 0x006a }
        throw r4;	 Catch:{ all -> 0x006a }
    L_0x006a:
        r4 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x006a }
        throw r4;
    L_0x006d:
        r1 = 0;
        goto L_0x0008;
    L_0x006f:
        r4 = r11 & 1;
        if (r4 == 0) goto L_0x007c;
    L_0x0073:
        r4 = r8.mPackages;	 Catch:{ all -> 0x006a }
        r3 = r4.get(r9);	 Catch:{ all -> 0x006a }
        r3 = (java.lang.ref.WeakReference) r3;	 Catch:{ all -> 0x006a }
        goto L_0x000e;
    L_0x007c:
        r4 = r8.mResourcePackages;	 Catch:{ all -> 0x006a }
        r3 = r4.get(r9);	 Catch:{ all -> 0x006a }
        r3 = (java.lang.ref.WeakReference) r3;	 Catch:{ all -> 0x006a }
        goto L_0x000e;
    L_0x0085:
        r2 = r5;
        goto L_0x0017;
    L_0x0087:
        monitor-exit(r6);	 Catch:{ all -> 0x006a }
    L_0x0088:
        return r2;
    L_0x0089:
        monitor-exit(r6);	 Catch:{ all -> 0x006a }
        r0 = 0;
        r4 = getPackageManager();	 Catch:{ RemoteException -> 0x009e }
        r6 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = r4.getApplicationInfo(r9, r6, r12);	 Catch:{ RemoteException -> 0x009e }
    L_0x0095:
        if (r0 == 0) goto L_0x009c;
    L_0x0097:
        r2 = r8.getPackageInfo(r0, r10, r11);
        goto L_0x0088;
    L_0x009c:
        r2 = r5;
        goto L_0x0088;
    L_0x009e:
        r4 = move-exception;
        goto L_0x0095;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.getPackageInfo(java.lang.String, android.content.res.CompatibilityInfo, int, int):android.app.LoadedApk");
    }

    public final LoadedApk getPackageInfo(ApplicationInfo ai, CompatibilityInfo compatInfo, int flags) {
        boolean includeCode;
        boolean securityViolation;
        boolean registerPackage = false;
        if ((flags & 1) != 0) {
            includeCode = true;
        } else {
            includeCode = false;
        }
        if (!includeCode || ai.uid == 0 || ai.uid == 1000 || (this.mBoundApplication != null && UserHandle.isSameApp(ai.uid, this.mBoundApplication.appInfo.uid))) {
            securityViolation = false;
        } else {
            securityViolation = true;
        }
        if (includeCode && (1073741824 & flags) != 0) {
            registerPackage = true;
        }
        if ((flags & 3) != 1 || !securityViolation) {
            return getPackageInfo(ai, compatInfo, null, securityViolation, includeCode, registerPackage);
        }
        String msg = "Requesting code from " + ai.packageName + " (with uid " + ai.uid + ")";
        if (this.mBoundApplication != null) {
            msg = msg + " to be run in process " + this.mBoundApplication.processName + " (with uid " + this.mBoundApplication.appInfo.uid + ")";
        }
        throw new SecurityException(msg);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.app.LoadedApk getPackageInfo(java.lang.String r8, android.content.res.CompatibilityInfo r9, java.lang.ClassLoader r10, int r11, int r12) {
        /*
        r7 = this;
        r4 = 0;
        r5 = r7.mResourcesManager;
        monitor-enter(r5);
        r3 = r11 & 1;
        if (r3 == 0) goto L_0x006f;
    L_0x0008:
        r3 = r7.mPackages;	 Catch:{ all -> 0x006c }
        r2 = r3.get(r8);	 Catch:{ all -> 0x006c }
        r2 = (java.lang.ref.WeakReference) r2;	 Catch:{ all -> 0x006c }
    L_0x0010:
        if (r2 == 0) goto L_0x0078;
    L_0x0012:
        r3 = r2.get();	 Catch:{ all -> 0x006c }
        r3 = (android.app.LoadedApk) r3;	 Catch:{ all -> 0x006c }
        r1 = r3;
    L_0x0019:
        if (r1 == 0) goto L_0x007c;
    L_0x001b:
        r3 = r1.mResources;	 Catch:{ all -> 0x006c }
        if (r3 == 0) goto L_0x002b;
    L_0x001f:
        r3 = r1.mResources;	 Catch:{ all -> 0x006c }
        r3 = r3.getAssets();	 Catch:{ all -> 0x006c }
        r3 = r3.isUpToDate();	 Catch:{ all -> 0x006c }
        if (r3 == 0) goto L_0x007c;
    L_0x002b:
        r3 = r1.isSecurityViolation();	 Catch:{ all -> 0x006c }
        if (r3 == 0) goto L_0x007a;
    L_0x0031:
        r3 = r11 & 2;
        if (r3 != 0) goto L_0x007a;
    L_0x0035:
        r3 = new java.lang.SecurityException;	 Catch:{ all -> 0x006c }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x006c }
        r4.<init>();	 Catch:{ all -> 0x006c }
        r6 = "Requesting code from ";
        r4 = r4.append(r6);	 Catch:{ all -> 0x006c }
        r4 = r4.append(r8);	 Catch:{ all -> 0x006c }
        r6 = " to be run in process ";
        r4 = r4.append(r6);	 Catch:{ all -> 0x006c }
        r6 = r7.mBoundApplication;	 Catch:{ all -> 0x006c }
        r6 = r6.processName;	 Catch:{ all -> 0x006c }
        r4 = r4.append(r6);	 Catch:{ all -> 0x006c }
        r6 = "/";
        r4 = r4.append(r6);	 Catch:{ all -> 0x006c }
        r6 = r7.mBoundApplication;	 Catch:{ all -> 0x006c }
        r6 = r6.appInfo;	 Catch:{ all -> 0x006c }
        r6 = r6.uid;	 Catch:{ all -> 0x006c }
        r4 = r4.append(r6);	 Catch:{ all -> 0x006c }
        r4 = r4.toString();	 Catch:{ all -> 0x006c }
        r3.<init>(r4);	 Catch:{ all -> 0x006c }
        throw r3;	 Catch:{ all -> 0x006c }
    L_0x006c:
        r3 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x006c }
        throw r3;
    L_0x006f:
        r3 = r7.mResourcePackages;	 Catch:{ all -> 0x006c }
        r2 = r3.get(r8);	 Catch:{ all -> 0x006c }
        r2 = (java.lang.ref.WeakReference) r2;	 Catch:{ all -> 0x006c }
        goto L_0x0010;
    L_0x0078:
        r1 = r4;
        goto L_0x0019;
    L_0x007a:
        monitor-exit(r5);	 Catch:{ all -> 0x006c }
    L_0x007b:
        return r1;
    L_0x007c:
        monitor-exit(r5);	 Catch:{ all -> 0x006c }
        r0 = 0;
        r3 = getPackageManager();	 Catch:{ RemoteException -> 0x0091 }
        r5 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = r3.getApplicationInfo(r8, r5, r12);	 Catch:{ RemoteException -> 0x0091 }
    L_0x0088:
        if (r0 == 0) goto L_0x008f;
    L_0x008a:
        r1 = r7.getPackageInfo(r0, r9, r10, r11);
        goto L_0x007b;
    L_0x008f:
        r1 = r4;
        goto L_0x007b;
    L_0x0091:
        r3 = move-exception;
        goto L_0x0088;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.getPackageInfo(java.lang.String, android.content.res.CompatibilityInfo, java.lang.ClassLoader, int, int):android.app.LoadedApk");
    }

    public final LoadedApk getPackageInfo(ApplicationInfo ai, CompatibilityInfo compatInfo, ClassLoader baseLoader, int flags) {
        boolean includeCode;
        boolean securityViolation;
        boolean registerPackage = false;
        if ((flags & 1) != 0) {
            includeCode = true;
        } else {
            includeCode = false;
        }
        if (!includeCode || ai.uid == 0 || ai.uid == 1000 || (this.mBoundApplication != null && UserHandle.isSameApp(ai.uid, this.mBoundApplication.appInfo.uid))) {
            securityViolation = false;
        } else {
            securityViolation = true;
        }
        if (includeCode && (1073741824 & flags) != 0) {
            registerPackage = true;
        }
        if ((flags & 3) != 1 || !securityViolation) {
            return getPackageInfo(ai, compatInfo, baseLoader, securityViolation, includeCode, registerPackage);
        }
        String msg = "Requesting code from " + ai.packageName + " (with uid " + ai.uid + ")";
        if (this.mBoundApplication != null) {
            msg = msg + " to be run in process " + this.mBoundApplication.processName + " (with uid " + this.mBoundApplication.appInfo.uid + ")";
        }
        throw new SecurityException(msg);
    }

    public final LoadedApk getPackageInfoNoCheck(ApplicationInfo ai, CompatibilityInfo compatInfo) {
        return getPackageInfo(ai, compatInfo, null, false, true, false);
    }

    public final LoadedApk peekPackageInfo(String packageName, boolean includeCode) {
        LoadedApk loadedApk;
        synchronized (this.mResourcesManager) {
            WeakReference<LoadedApk> ref;
            if (includeCode) {
                ref = (WeakReference) this.mPackages.get(packageName);
            } else {
                ref = (WeakReference) this.mResourcePackages.get(packageName);
            }
            loadedApk = ref != null ? (LoadedApk) ref.get() : null;
        }
        return loadedApk;
    }

    private LoadedApk getPackageInfo(ApplicationInfo aInfo, CompatibilityInfo compatInfo, ClassLoader baseLoader, boolean securityViolation, boolean includeCode, boolean registerPackage) {
        LoadedApk packageInfo;
        boolean differentUser = UserHandle.myUserId() != UserHandle.getUserId(aInfo.uid);
        synchronized (this.mResourcesManager) {
            WeakReference<LoadedApk> ref;
            if (differentUser) {
                ref = null;
            } else if (includeCode) {
                ref = (WeakReference) this.mPackages.get(aInfo.packageName);
            } else {
                ref = (WeakReference) this.mResourcePackages.get(aInfo.packageName);
            }
            packageInfo = ref != null ? (LoadedApk) ref.get() : null;
            if (packageInfo == null || !(packageInfo.mResources == null || packageInfo.mResources.getAssets().isUpToDate())) {
                boolean z = includeCode && (aInfo.flags & 4) != 0;
                packageInfo = new LoadedApk(this, aInfo, compatInfo, baseLoader, securityViolation, z, registerPackage);
                if (this.mSystemThread && "android".equals(aInfo.packageName)) {
                    packageInfo.installSystemApplicationInfo(aInfo, getSystemContext().mPackageInfo.getClassLoader());
                }
                if (!differentUser) {
                    if (includeCode) {
                        this.mPackages.put(aInfo.packageName, new WeakReference(packageInfo));
                    } else {
                        this.mResourcePackages.put(aInfo.packageName, new WeakReference(packageInfo));
                    }
                }
            }
        }
        return packageInfo;
    }

    ActivityThread() {
    }

    public ApplicationThread getApplicationThread() {
        return this.mAppThread;
    }

    public Instrumentation getInstrumentation() {
        return this.mInstrumentation;
    }

    public boolean isProfiling() {
        return (this.mProfiler == null || this.mProfiler.profileFile == null || this.mProfiler.profileFd != null) ? false : true;
    }

    public String getProfileFilePath() {
        return this.mProfiler.profileFile;
    }

    public Looper getLooper() {
        return this.mLooper;
    }

    public Application getApplication() {
        return this.mInitialApplication;
    }

    public String getProcessName() {
        return this.mBoundApplication.processName;
    }

    public ContextImpl getSystemContext() {
        ContextImpl contextImpl;
        synchronized (this) {
            if (this.mSystemContext == null) {
                this.mSystemContext = ContextImpl.createSystemContext(this);
            }
            contextImpl = this.mSystemContext;
        }
        return contextImpl;
    }

    public void installSystemApplicationInfo(ApplicationInfo info, ClassLoader classLoader) {
        synchronized (this) {
            getSystemContext().installSystemApplicationInfo(info, classLoader);
            this.mProfiler = new Profiler();
        }
    }

    void ensureJitEnabled() {
        if (!this.mJitEnabled) {
            this.mJitEnabled = true;
            VMRuntime.getRuntime().startJitCompilation();
        }
    }

    void scheduleGcIdler() {
        if (!this.mGcIdlerScheduled) {
            this.mGcIdlerScheduled = true;
            Looper.myQueue().addIdleHandler(this.mGcIdler);
        }
        this.mH.removeMessages(120);
    }

    void unscheduleGcIdler() {
        if (this.mGcIdlerScheduled) {
            this.mGcIdlerScheduled = false;
            Looper.myQueue().removeIdleHandler(this.mGcIdler);
        }
        this.mH.removeMessages(120);
    }

    void doGcIfNeeded() {
        this.mGcIdlerScheduled = false;
        if (BinderInternal.getLastGcTime() + MIN_TIME_BETWEEN_GCS < SystemClock.uptimeMillis()) {
            BinderInternal.forceGc("bg");
        }
    }

    static void printRow(PrintWriter pw, String format, Object... objs) {
        pw.println(String.format(format, objs));
    }

    public static void dumpMemInfoTable(PrintWriter pw, MemoryInfo memInfo, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, int pid, String processName, long nativeMax, long nativeAllocated, long nativeFree, long dalvikMax, long dalvikAllocated, long dalvikFree) {
        int i;
        if (checkin) {
            pw.print(3);
            pw.print(',');
            pw.print(pid);
            pw.print(',');
            pw.print(processName);
            pw.print(',');
            pw.print(nativeMax);
            pw.print(',');
            pw.print(dalvikMax);
            pw.print(',');
            pw.print("N/A,");
            pw.print(nativeMax + dalvikMax);
            pw.print(',');
            pw.print(nativeAllocated);
            pw.print(',');
            pw.print(dalvikAllocated);
            pw.print(',');
            pw.print("N/A,");
            pw.print(nativeAllocated + dalvikAllocated);
            pw.print(',');
            pw.print(nativeFree);
            pw.print(',');
            pw.print(dalvikFree);
            pw.print(',');
            pw.print("N/A,");
            pw.print(nativeFree + dalvikFree);
            pw.print(',');
            pw.print(memInfo.nativePss);
            pw.print(',');
            pw.print(memInfo.dalvikPss);
            pw.print(',');
            pw.print(memInfo.otherPss);
            pw.print(',');
            pw.print(memInfo.getTotalPss());
            pw.print(',');
            pw.print(memInfo.nativeSwappablePss);
            pw.print(',');
            pw.print(memInfo.dalvikSwappablePss);
            pw.print(',');
            pw.print(memInfo.otherSwappablePss);
            pw.print(',');
            pw.print(memInfo.getTotalSwappablePss());
            pw.print(',');
            pw.print(memInfo.nativeSharedDirty);
            pw.print(',');
            pw.print(memInfo.dalvikSharedDirty);
            pw.print(',');
            pw.print(memInfo.otherSharedDirty);
            pw.print(',');
            pw.print(memInfo.getTotalSharedDirty());
            pw.print(',');
            pw.print(memInfo.nativeSharedClean);
            pw.print(',');
            pw.print(memInfo.dalvikSharedClean);
            pw.print(',');
            pw.print(memInfo.otherSharedClean);
            pw.print(',');
            pw.print(memInfo.getTotalSharedClean());
            pw.print(',');
            pw.print(memInfo.nativePrivateDirty);
            pw.print(',');
            pw.print(memInfo.dalvikPrivateDirty);
            pw.print(',');
            pw.print(memInfo.otherPrivateDirty);
            pw.print(',');
            pw.print(memInfo.getTotalPrivateDirty());
            pw.print(',');
            pw.print(memInfo.nativePrivateClean);
            pw.print(',');
            pw.print(memInfo.dalvikPrivateClean);
            pw.print(',');
            pw.print(memInfo.otherPrivateClean);
            pw.print(',');
            pw.print(memInfo.getTotalPrivateClean());
            pw.print(',');
            for (i = 0; i < 17; i++) {
                pw.print(MemoryInfo.getOtherLabel(i));
                pw.print(',');
                pw.print(memInfo.getOtherPss(i));
                pw.print(',');
                pw.print(memInfo.getOtherSwappablePss(i));
                pw.print(',');
                pw.print(memInfo.getOtherSharedDirty(i));
                pw.print(',');
                pw.print(memInfo.getOtherSharedClean(i));
                pw.print(',');
                pw.print(memInfo.getOtherPrivateDirty(i));
                pw.print(',');
                pw.print(memInfo.getOtherPrivateClean(i));
                pw.print(',');
            }
            return;
        }
        if (!dumpSummaryOnly) {
            int myPss;
            int mySwappablePss;
            int mySharedDirty;
            int myPrivateDirty;
            int mySharedClean;
            int myPrivateClean;
            int mySwappedOut;
            if (dumpFullInfo) {
                printRow(pw, HEAP_FULL_COLUMN, ProxyInfo.LOCAL_EXCL_LIST, "Pss", "Pss", "Shared", "Private", "Shared", "Private", "Swapped", "Heap", "Heap", "Heap");
                printRow(pw, HEAP_FULL_COLUMN, ProxyInfo.LOCAL_EXCL_LIST, "Total", "Clean", "Dirty", "Dirty", "Clean", "Clean", "Dirty", "Size", "Alloc", "Free");
                printRow(pw, HEAP_FULL_COLUMN, ProxyInfo.LOCAL_EXCL_LIST, "------", "------", "------", "------", "------", "------", "------", "------", "------", "------");
                printRow(pw, HEAP_FULL_COLUMN, "Native Heap", Integer.valueOf(memInfo.nativePss), Integer.valueOf(memInfo.nativeSwappablePss), Integer.valueOf(memInfo.nativeSharedDirty), Integer.valueOf(memInfo.nativePrivateDirty), Integer.valueOf(memInfo.nativeSharedClean), Integer.valueOf(memInfo.nativePrivateClean), Integer.valueOf(memInfo.nativeSwappedOut), Long.valueOf(nativeMax), Long.valueOf(nativeAllocated), Long.valueOf(nativeFree));
                printRow(pw, HEAP_FULL_COLUMN, "Dalvik Heap", Integer.valueOf(memInfo.dalvikPss), Integer.valueOf(memInfo.dalvikSwappablePss), Integer.valueOf(memInfo.dalvikSharedDirty), Integer.valueOf(memInfo.dalvikPrivateDirty), Integer.valueOf(memInfo.dalvikSharedClean), Integer.valueOf(memInfo.dalvikPrivateClean), Integer.valueOf(memInfo.dalvikSwappedOut), Long.valueOf(dalvikMax), Long.valueOf(dalvikAllocated), Long.valueOf(dalvikFree));
            } else {
                printRow(pw, HEAP_COLUMN, ProxyInfo.LOCAL_EXCL_LIST, "Pss", "Private", "Private", "Swapped", "Heap", "Heap", "Heap");
                printRow(pw, HEAP_COLUMN, ProxyInfo.LOCAL_EXCL_LIST, "Total", "Dirty", "Clean", "Dirty", "Size", "Alloc", "Free");
                printRow(pw, HEAP_COLUMN, ProxyInfo.LOCAL_EXCL_LIST, "------", "------", "------", "------", "------", "------", "------", "------");
                printRow(pw, HEAP_COLUMN, "Native Heap", Integer.valueOf(memInfo.nativePss), Integer.valueOf(memInfo.nativePrivateDirty), Integer.valueOf(memInfo.nativePrivateClean), Integer.valueOf(memInfo.nativeSwappedOut), Long.valueOf(nativeMax), Long.valueOf(nativeAllocated), Long.valueOf(nativeFree));
                printRow(pw, HEAP_COLUMN, "Dalvik Heap", Integer.valueOf(memInfo.dalvikPss), Integer.valueOf(memInfo.dalvikPrivateDirty), Integer.valueOf(memInfo.dalvikPrivateClean), Integer.valueOf(memInfo.dalvikSwappedOut), Long.valueOf(dalvikMax), Long.valueOf(dalvikAllocated), Long.valueOf(dalvikFree));
            }
            int otherPss = memInfo.otherPss;
            int otherSwappablePss = memInfo.otherSwappablePss;
            int otherSharedDirty = memInfo.otherSharedDirty;
            int otherPrivateDirty = memInfo.otherPrivateDirty;
            int otherSharedClean = memInfo.otherSharedClean;
            int otherPrivateClean = memInfo.otherPrivateClean;
            int otherSwappedOut = memInfo.otherSwappedOut;
            for (i = 0; i < 17; i++) {
                myPss = memInfo.getOtherPss(i);
                mySwappablePss = memInfo.getOtherSwappablePss(i);
                mySharedDirty = memInfo.getOtherSharedDirty(i);
                myPrivateDirty = memInfo.getOtherPrivateDirty(i);
                mySharedClean = memInfo.getOtherSharedClean(i);
                myPrivateClean = memInfo.getOtherPrivateClean(i);
                mySwappedOut = memInfo.getOtherSwappedOut(i);
                if (myPss != 0 || mySharedDirty != 0 || myPrivateDirty != 0 || mySharedClean != 0 || myPrivateClean != 0 || mySwappedOut != 0) {
                    if (dumpFullInfo) {
                        printRow(pw, HEAP_FULL_COLUMN, MemoryInfo.getOtherLabel(i), Integer.valueOf(myPss), Integer.valueOf(mySwappablePss), Integer.valueOf(mySharedDirty), Integer.valueOf(myPrivateDirty), Integer.valueOf(mySharedClean), Integer.valueOf(myPrivateClean), Integer.valueOf(mySwappedOut), ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST);
                    } else {
                        printRow(pw, HEAP_COLUMN, MemoryInfo.getOtherLabel(i), Integer.valueOf(myPss), Integer.valueOf(myPrivateDirty), Integer.valueOf(myPrivateClean), Integer.valueOf(mySwappedOut), ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST);
                    }
                    otherPss -= myPss;
                    otherSwappablePss -= mySwappablePss;
                    otherSharedDirty -= mySharedDirty;
                    otherPrivateDirty -= myPrivateDirty;
                    otherSharedClean -= mySharedClean;
                    otherPrivateClean -= myPrivateClean;
                    otherSwappedOut -= mySwappedOut;
                }
            }
            if (dumpFullInfo) {
                printRow(pw, HEAP_FULL_COLUMN, "Unknown", Integer.valueOf(otherPss), Integer.valueOf(otherSwappablePss), Integer.valueOf(otherSharedDirty), Integer.valueOf(otherPrivateDirty), Integer.valueOf(otherSharedClean), Integer.valueOf(otherPrivateClean), Integer.valueOf(otherSwappedOut), ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST);
                printRow(pw, HEAP_FULL_COLUMN, "TOTAL", Integer.valueOf(memInfo.getTotalPss()), Integer.valueOf(memInfo.getTotalSwappablePss()), Integer.valueOf(memInfo.getTotalSharedDirty()), Integer.valueOf(memInfo.getTotalPrivateDirty()), Integer.valueOf(memInfo.getTotalSharedClean()), Integer.valueOf(memInfo.getTotalPrivateClean()), Integer.valueOf(memInfo.getTotalSwappedOut()), Long.valueOf(nativeMax + dalvikMax), Long.valueOf(nativeAllocated + dalvikAllocated), Long.valueOf(nativeFree + dalvikFree));
            } else {
                printRow(pw, HEAP_COLUMN, "Unknown", Integer.valueOf(otherPss), Integer.valueOf(otherPrivateDirty), Integer.valueOf(otherPrivateClean), Integer.valueOf(otherSwappedOut), ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST);
                printRow(pw, HEAP_COLUMN, "TOTAL", Integer.valueOf(memInfo.getTotalPss()), Integer.valueOf(memInfo.getTotalPrivateDirty()), Integer.valueOf(memInfo.getTotalPrivateClean()), Integer.valueOf(memInfo.getTotalSwappedOut()), Long.valueOf(nativeMax + dalvikMax), Long.valueOf(nativeAllocated + dalvikAllocated), Long.valueOf(nativeFree + dalvikFree));
            }
            if (dumpDalvik) {
                pw.println(" ");
                pw.println(" Dalvik Details");
                for (i = 17; i < 25; i++) {
                    myPss = memInfo.getOtherPss(i);
                    mySwappablePss = memInfo.getOtherSwappablePss(i);
                    mySharedDirty = memInfo.getOtherSharedDirty(i);
                    myPrivateDirty = memInfo.getOtherPrivateDirty(i);
                    mySharedClean = memInfo.getOtherSharedClean(i);
                    myPrivateClean = memInfo.getOtherPrivateClean(i);
                    mySwappedOut = memInfo.getOtherSwappedOut(i);
                    if (myPss != 0 || mySharedDirty != 0 || myPrivateDirty != 0 || mySharedClean != 0 || myPrivateClean != 0) {
                        if (dumpFullInfo) {
                            printRow(pw, HEAP_FULL_COLUMN, MemoryInfo.getOtherLabel(i), Integer.valueOf(myPss), Integer.valueOf(mySwappablePss), Integer.valueOf(mySharedDirty), Integer.valueOf(myPrivateDirty), Integer.valueOf(mySharedClean), Integer.valueOf(myPrivateClean), Integer.valueOf(mySwappedOut), ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST);
                        } else {
                            printRow(pw, HEAP_COLUMN, MemoryInfo.getOtherLabel(i), Integer.valueOf(myPss), Integer.valueOf(myPrivateDirty), Integer.valueOf(myPrivateClean), Integer.valueOf(mySwappedOut), ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST, ProxyInfo.LOCAL_EXCL_LIST);
                        }
                    }
                }
            }
        }
        pw.println(" ");
        pw.println(" App Summary");
        printRow(pw, ONE_COUNT_COLUMN_HEADER, ProxyInfo.LOCAL_EXCL_LIST, "Pss(KB)");
        printRow(pw, ONE_COUNT_COLUMN_HEADER, ProxyInfo.LOCAL_EXCL_LIST, "------");
        printRow(pw, ONE_COUNT_COLUMN, "Java Heap:", Integer.valueOf(memInfo.getSummaryJavaHeap()));
        printRow(pw, ONE_COUNT_COLUMN, "Native Heap:", Integer.valueOf(memInfo.getSummaryNativeHeap()));
        printRow(pw, ONE_COUNT_COLUMN, "Code:", Integer.valueOf(memInfo.getSummaryCode()));
        printRow(pw, ONE_COUNT_COLUMN, "Stack:", Integer.valueOf(memInfo.getSummaryStack()));
        printRow(pw, ONE_COUNT_COLUMN, "Graphics:", Integer.valueOf(memInfo.getSummaryGraphics()));
        printRow(pw, ONE_COUNT_COLUMN, "Private Other:", Integer.valueOf(memInfo.getSummaryPrivateOther()));
        printRow(pw, ONE_COUNT_COLUMN, "System:", Integer.valueOf(memInfo.getSummarySystem()));
        pw.println(" ");
        printRow(pw, TWO_COUNT_COLUMNS, "TOTAL:", Integer.valueOf(memInfo.getSummaryTotalPss()), "TOTAL SWAP (KB):", Integer.valueOf(memInfo.getSummaryTotalSwap()));
    }

    public void registerOnActivityPausedListener(Activity activity, OnActivityPausedListener listener) {
        synchronized (this.mOnPauseListeners) {
            ArrayList<OnActivityPausedListener> list = (ArrayList) this.mOnPauseListeners.get(activity);
            if (list == null) {
                list = new ArrayList();
                this.mOnPauseListeners.put(activity, list);
            }
            list.add(listener);
        }
    }

    public void unregisterOnActivityPausedListener(Activity activity, OnActivityPausedListener listener) {
        synchronized (this.mOnPauseListeners) {
            ArrayList<OnActivityPausedListener> list = (ArrayList) this.mOnPauseListeners.get(activity);
            if (list != null) {
                list.remove(listener);
            }
        }
    }

    public final ActivityInfo resolveActivityInfo(Intent intent) {
        ActivityInfo aInfo = intent.resolveActivityInfo(this.mInitialApplication.getPackageManager(), 1024);
        if (aInfo == null) {
            Instrumentation.checkStartActivityResult(-2, intent);
        }
        return aInfo;
    }

    public final Activity startActivityNow(Activity parent, String id, Intent intent, ActivityInfo activityInfo, IBinder token, Bundle state, NonConfigurationInstances lastNonConfigurationInstances) {
        ActivityClientRecord r = new ActivityClientRecord();
        r.token = token;
        r.ident = 0;
        r.intent = intent;
        r.state = state;
        r.parent = parent;
        r.embeddedID = id;
        r.activityInfo = activityInfo;
        r.lastNonConfigurationInstances = lastNonConfigurationInstances;
        return performLaunchActivity(r, null);
    }

    public boolean isValidToken(IBinder token) {
        return this.mActivities.containsKey(token);
    }

    public final Activity getActivity(IBinder token) {
        return ((ActivityClientRecord) this.mActivities.get(token)).activity;
    }

    public final void sendActivityResult(IBinder token, String id, int requestCode, int resultCode, Intent data) {
        ArrayList<ResultInfo> list = new ArrayList();
        list.add(new ResultInfo(id, requestCode, resultCode, data));
        this.mAppThread.scheduleSendResult(token, list);
    }

    private void sendMessage(int what, Object obj) {
        sendMessage(what, obj, 0, 0, false);
    }

    private void sendMessage(int what, Object obj, int arg1) {
        sendMessage(what, obj, arg1, 0, false);
    }

    private void sendMessage(int what, Object obj, int arg1, int arg2) {
        sendMessage(what, obj, arg1, arg2, false);
    }

    private void sendMessage(int what, Object obj, int arg1, int arg2, boolean async) {
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = obj;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        if (async) {
            msg.setAsynchronous(true);
        }
        this.mH.sendMessage(msg);
    }

    final void scheduleContextCleanup(ContextImpl context, String who, String what) {
        ContextCleanupInfo cci = new ContextCleanupInfo();
        cci.context = context;
        cci.who = who;
        cci.what = what;
        sendMessage(119, cci);
    }

    public static String getCurrActivityFromThread() {
        if (currentActivityThread() == null || rCActivShort == null) {
            return null;
        }
        return rCActivShort;
    }

    private Activity performLaunchActivity(ActivityClientRecord r, Intent customIntent) {
        ActivityInfo aInfo = r.activityInfo;
        if (r.packageInfo == null) {
            r.packageInfo = getPackageInfo(aInfo.applicationInfo, r.compatInfo, 1);
        }
        ComponentName component = r.intent.getComponent();
        if (component == null) {
            component = r.intent.resolveActivity(this.mInitialApplication.getPackageManager());
            r.intent.setComponent(component);
        }
        if (r.activityInfo.targetActivity != null) {
            ComponentName componentName = new ComponentName(r.activityInfo.packageName, r.activityInfo.targetActivity);
        }
        Activity activity = null;
        try {
            ClassLoader cl = r.packageInfo.getClassLoader();
            activity = this.mInstrumentation.newActivity(cl, component.getClassName(), r.intent);
            StrictMode.incrementExpectedActivityCount(activity.getClass());
            r.intent.setExtrasClassLoader(cl);
            r.intent.prepareToEnterProcess();
            if (r.state != null) {
                r.state.setClassLoader(cl);
            }
        } catch (Throwable e) {
            if (!this.mInstrumentation.onException(null, e)) {
                throw new RuntimeException("Unable to instantiate activity " + component + ": " + e.toString(), e);
            }
        }
        try {
            Application app = r.packageInfo.makeApplication(false, this.mInstrumentation);
            if (activity != null) {
                Context appContext = createBaseContextForActivity(r, activity, r.displayId);
                r.activityInfo.setResIdOffset(this.mResourcesManager.getResIdOffset(r.activityInfo.packageName));
                CharSequence title = r.activityInfo.loadLabel(appContext.getPackageManager());
                Configuration configuration = new Configuration(this.mCompatConfiguration);
                this.defaultHeapUtil = VMRuntime.getRuntime().getTargetHeapUtilization();
                VMRuntime.getRuntime().setTargetHeapUtilization(SensorManager.LIGHT_FULLMOON);
                String activShortName = r.intent.getComponent().getClassName();
                rCActivShort = activShortName;
                File file = null;
                if (!(appContext == null || appContext.getFilesDir() == null || activShortName == null)) {
                    File file2 = new File(appContext.getFilesDir(), "rList-" + activShortName);
                }
                if (file != null && file.exists() && file.canWrite()) {
                    file.setReadOnly();
                }
                if (file != null && file.exists() && file.length() > 600 && appContext != null) {
                    Thread thread = new Thread(new mRunnable(appContext, activShortName, file));
                    if (thread != null) {
                        thread.start();
                    }
                }
                if (!(file == null || file.exists() || appContext == null)) {
                    try {
                        if (!Boolean.valueOf(file.createNewFile()).booleanValue()) {
                            Slog.e(TAG, "File already exists");
                        }
                        appContext.getResources().setRCable(1, file);
                    } catch (Throwable e2) {
                        Slog.e(TAG, "IOException - to create new rList-" + activShortName + " file for pkg : " + app.getPackageName() + " and file : " + file + " path : " + appContext.getFilesDir(), e2);
                    }
                }
                activity.mMultiWindowStyle.setTo(r.multiWindowStyle, true);
                activity.mPreferredOrientation = r.multiWindowStyle.getAppRequestOrientation();
                activity.attach(appContext, this, getInstrumentation(), r.token, r.ident, app, r.intent, r.activityInfo, title, r.parent, r.embeddedID, r.lastNonConfigurationInstances, configuration, r.referrer, r.voiceInteractor);
                if (customIntent != null) {
                    activity.mIntent = customIntent;
                }
                r.lastNonConfigurationInstances = null;
                activity.mStartedActivity = false;
                int theme = r.activityInfo.getThemeResource();
                if (theme != 0) {
                    activity.setTheme(theme);
                }
                activity.mCalled = false;
                if (r.isPersistable()) {
                    this.mInstrumentation.callActivityOnCreate(activity, r.state, r.persistentState);
                } else {
                    this.mInstrumentation.callActivityOnCreate(activity, r.state);
                }
                if (activity.mCalled) {
                    r.activity = activity;
                    r.stopped = true;
                    if (!r.activity.mFinished) {
                        activity.performStart();
                        r.stopped = false;
                    }
                    if (!r.activity.mFinished) {
                        if (r.isPersistable()) {
                            if (!(r.state == null && r.persistentState == null)) {
                                this.mInstrumentation.callActivityOnRestoreInstanceState(activity, r.state, r.persistentState);
                            }
                        } else if (r.state != null) {
                            this.mInstrumentation.callActivityOnRestoreInstanceState(activity, r.state);
                        }
                    }
                    if (!r.activity.mFinished) {
                        activity.mCalled = false;
                        if (r.isPersistable()) {
                            this.mInstrumentation.callActivityOnPostCreate(activity, r.state, r.persistentState);
                        } else {
                            this.mInstrumentation.callActivityOnPostCreate(activity, r.state);
                        }
                        if (!activity.mCalled) {
                            throw new SuperNotCalledException("Activity " + r.intent.getComponent().toShortString() + " did not call through to super.onPostCreate()");
                        }
                    }
                }
                throw new SuperNotCalledException("Activity " + r.intent.getComponent().toShortString() + " did not call through to super.onCreate()");
            }
            r.paused = true;
            this.mActivities.put(r.token, r);
        } catch (SuperNotCalledException e3) {
            throw e3;
        } catch (Throwable e22) {
            if (!this.mInstrumentation.onException(activity, e22)) {
                throw new RuntimeException("Unable to start activity " + component + ": " + e22.toString(), e22);
            }
        }
        return activity;
    }

    private Context createBaseContextForActivity(ActivityClientRecord r, Activity activity) {
        return createBaseContextForActivity(r, activity, -1);
    }

    private Context createBaseContextForActivity(ActivityClientRecord r, Activity activity, int activityDisplayId) {
        DisplayManagerGlobal dm = DisplayManagerGlobal.getInstance();
        int displayId = activityDisplayId;
        if (displayId == -1) {
            try {
                displayId = ActivityManagerNative.getDefault().getActivityDisplayId(r.token);
            } catch (RemoteException e) {
            }
        }
        Context appContext = ContextImpl.createActivityContext(this, r.packageInfo, displayId, r.overrideConfig, r.token);
        appContext.setOuterContext(activity);
        Context baseContext = appContext;
        String pkgName = SystemProperties.get("debug.second-display.pkg");
        if (pkgName == null || pkgName.isEmpty() || !r.packageInfo.mPackageName.contains(pkgName)) {
            return baseContext;
        }
        for (int id : dm.getDisplayIds()) {
            if (id != 0) {
                return appContext.createDisplayContext(dm.getCompatibleDisplay(id, appContext.getDisplayAdjustments(id)));
            }
        }
        return baseContext;
    }

    private void handleLaunchActivity(ActivityClientRecord r, Intent customIntent) {
        unscheduleGcIdler();
        niceUp();
        this.mSomeActivitiesChanged = true;
        if (r.profilerInfo != null) {
            this.mProfiler.setProfiler(r.profilerInfo);
            this.mProfiler.startProfiling();
        }
        handleConfigurationChanged(null, null);
        WindowManagerGlobal.initialize();
        if (performLaunchActivity(r, customIntent) != null) {
            boolean z;
            r.createdConfig = new Configuration(this.mConfiguration);
            Bundle oldState = r.state;
            IBinder iBinder = r.token;
            boolean z2 = r.isForward;
            if (r.activity.mFinished || r.startsNotResumed) {
                z = false;
            } else {
                z = true;
            }
            handleResumeActivity(iBinder, false, z2, z);
            if (!r.activity.mFinished && r.startsNotResumed) {
                try {
                    r.activity.mCalled = false;
                    this.mInstrumentation.callActivityOnPause(r.activity);
                    if (r.isPreHoneycomb()) {
                        r.state = oldState;
                    }
                    if (!r.activity.mCalled) {
                        throw new SuperNotCalledException("Activity " + r.intent.getComponent().toShortString() + " did not call through to super.onPause()");
                    }
                } catch (SuperNotCalledException e) {
                    throw e;
                } catch (Exception e2) {
                    if (!this.mInstrumentation.onException(r.activity, e2)) {
                        throw new RuntimeException("Unable to pause activity " + r.intent.getComponent().toShortString() + ": " + e2.toString(), e2);
                    }
                }
                r.paused = true;
                return;
            }
            return;
        }
        try {
            ActivityManagerNative.getDefault().finishActivity(r.token, 0, null, false);
        } catch (RemoteException e3) {
        }
    }

    private void deliverNewIntents(ActivityClientRecord r, List<ReferrerIntent> intents) {
        int N = intents.size();
        for (int i = 0; i < N; i++) {
            ReferrerIntent intent = (ReferrerIntent) intents.get(i);
            intent.setExtrasClassLoader(r.activity.getClassLoader());
            intent.prepareToEnterProcess();
            r.activity.mFragments.noteStateNotSaved();
            this.mInstrumentation.callActivityOnNewIntent(r.activity, intent);
        }
    }

    public final void performNewIntents(IBinder token, List<ReferrerIntent> intents) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            boolean resumed = !r.paused;
            if (resumed) {
                r.activity.mTemporaryPause = true;
                this.mInstrumentation.callActivityOnPause(r.activity);
            }
            deliverNewIntents(r, intents);
            if (resumed) {
                r.activity.performResume();
                r.activity.mTemporaryPause = false;
            }
        }
    }

    private void handleNewIntent(NewIntentData data) {
        performNewIntents(data.token, data.intents);
    }

    public void handleRequestAssistContextExtras(RequestAssistContextExtras cmd) {
        AssistStructure structure;
        if (this.mLastAssistStructure != null) {
            structure = (AssistStructure) this.mLastAssistStructure.get();
            if (structure != null) {
                structure.clearSendChannel();
            }
        }
        Bundle data = new Bundle();
        structure = null;
        AssistContent content = new AssistContent();
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(cmd.activityToken);
        Uri referrer = null;
        if (r != null) {
            r.activity.getApplication().dispatchOnProvideAssistData(r.activity, data);
            r.activity.onProvideAssistData(data);
            referrer = r.activity.onProvideReferrer();
            if (cmd.requestType == 1) {
                structure = new AssistStructure(r.activity);
                Intent activityIntent = r.activity.getIntent();
                if (activityIntent == null || !(r.window == null || (r.window.getAttributes().flags & 8192) == 0)) {
                    content.setDefaultIntent(new Intent());
                } else {
                    Intent intent = new Intent(activityIntent);
                    intent.setFlags(intent.getFlags() & -67);
                    intent.removeUnsafeExtras();
                    content.setDefaultIntent(intent);
                }
                r.activity.onProvideAssistContent(content);
            }
        }
        if (structure == null) {
            structure = new AssistStructure();
        }
        this.mLastAssistStructure = new WeakReference(structure);
        try {
            ActivityManagerNative.getDefault().reportAssistContextExtras(cmd.requestToken, data, structure, content, referrer);
        } catch (RemoteException e) {
        }
    }

    public void handleTranslucentConversionComplete(IBinder token, boolean drawComplete) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            r.activity.onTranslucentConversionComplete(drawComplete);
        }
    }

    public void onNewActivityOptions(IBinder token, ActivityOptions options) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            r.activity.onNewActivityOptions(options);
        }
    }

    public void handleCancelVisibleBehind(IBinder token) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            this.mSomeActivitiesChanged = true;
            Activity activity = r.activity;
            if (activity.mVisibleBehind) {
                activity.mCalled = false;
                activity.onVisibleBehindCanceled();
                if (activity.mCalled) {
                    activity.mVisibleBehind = false;
                } else {
                    throw new SuperNotCalledException("Activity " + activity.getLocalClassName() + " did not call through to super.onVisibleBehindCanceled()");
                }
            }
        }
        try {
            ActivityManagerNative.getDefault().backgroundResourcesReleased(token);
        } catch (RemoteException e) {
        }
    }

    public void handleOnBackgroundVisibleBehindChanged(IBinder token, boolean visible) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            r.activity.onBackgroundVisibleBehindChanged(visible);
        }
    }

    public void handleInstallProvider(ProviderInfo info) {
        ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            installContentProviders(this.mInitialApplication, Lists.newArrayList(new ProviderInfo[]{info}));
        } finally {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleDisplayMetricsChanged(DisplayInfo dInfo) {
        synchronized (this.mResourcesManager) {
        }
    }

    private void handleEnterAnimationComplete(IBinder token) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            r.activity.dispatchEnterAnimationComplete();
        }
    }

    private void handleUpdateOverlayPath(String path, String packageName, int isDisable) {
        if (this.mBoundApplication == null || this.mBoundApplication.info == null || this.mBoundApplication.info.mResources == null || this.mBoundApplication.info.mResources.getAssets() == null) {
            Slog.e(TAG, "Bound aplication is null .. for " + packageName + " . Not updating overlay path");
        } else if (isDisable != 0) {
            Slog.d(TAG, "handleUpdateOverlayPath removeOverlayPath path =" + path + ", isDisable =" + isDisable);
            this.mBoundApplication.info.mResources.getAssets().removeOverlayPath(path);
        } else {
            Slog.d(TAG, "handleUpdateOverlayPath addOverlayPath path =" + path + ", isDisable =" + isDisable);
            this.mBoundApplication.info.mResources.getAssets().addOverlayPath(path);
        }
    }

    public static Intent getIntentBeingBroadcast() {
        return (Intent) sCurrentBroadcastIntent.get();
    }

    private void handleReceiver(ReceiverData data) {
        unscheduleGcIdler();
        String component = data.intent.getComponent().getClassName();
        LoadedApk packageInfo = getPackageInfoNoCheck(data.info.applicationInfo, data.compatInfo);
        IActivityManager mgr = ActivityManagerNative.getDefault();
        try {
            ClassLoader cl = packageInfo.getClassLoader();
            data.intent.setExtrasClassLoader(cl);
            data.intent.prepareToEnterProcess();
            data.setExtrasClassLoader(cl);
            BroadcastReceiver receiver = (BroadcastReceiver) cl.loadClass(component).newInstance();
            try {
                ContextImpl context = (ContextImpl) packageInfo.makeApplication(false, this.mInstrumentation).getBaseContext();
                sCurrentBroadcastIntent.set(data.intent);
                receiver.setPendingResult(data);
                receiver.onReceive(context.getReceiverRestrictedContext(), data.intent);
            } catch (Exception e) {
                data.sendFinished(mgr);
                if (!this.mInstrumentation.onException(receiver, e)) {
                    throw new RuntimeException("Unable to start receiver " + component + ": " + e.toString(), e);
                }
            } finally {
                sCurrentBroadcastIntent.set(null);
            }
            if (receiver.getPendingResult() != null) {
                data.finish();
            }
        } catch (Exception e2) {
            data.sendFinished(mgr);
            throw new RuntimeException("Unable to instantiate receiver " + component + ": " + e2.toString(), e2);
        }
    }

    private void handleCreateBackupAgent(CreateBackupAgentData data) {
        try {
            if (getPackageManager().getPackageInfo(data.appInfo.packageName, 0, UserHandle.myUserId()).applicationInfo.uid != Process.myUid()) {
                Slog.w(TAG, "Asked to instantiate non-matching package " + data.appInfo.packageName);
                return;
            }
            unscheduleGcIdler();
            LoadedApk packageInfo = getPackageInfoNoCheck(data.appInfo, data.compatInfo);
            String packageName = packageInfo.mPackageName;
            if (packageName == null) {
                Slog.d(TAG, "Asked to create backup agent for nonexistent package");
                return;
            }
            String classname = data.appInfo.backupAgentName;
            if (classname == null && (data.backupMode == 1 || data.backupMode == 3)) {
                classname = "android.app.backup.FullBackupAgent";
            }
            IBinder binder = null;
            try {
                BackupAgent agent = (BackupAgent) this.mBackupAgents.get(packageName);
                if (agent != null) {
                    binder = agent.onBind();
                } else {
                    agent = (BackupAgent) packageInfo.getClassLoader().loadClass(classname).newInstance();
                    ContextImpl context = ContextImpl.createAppContext(this, packageInfo);
                    context.setOuterContext(agent);
                    agent.attach(context);
                    agent.onCreate();
                    binder = agent.onBind();
                    this.mBackupAgents.put(packageName, agent);
                }
            } catch (Exception e) {
                Slog.e(TAG, "Agent threw during creation: " + e);
                if (!(data.backupMode == 2 || data.backupMode == 3)) {
                    throw e;
                }
            } catch (Exception e2) {
                throw new RuntimeException("Unable to create BackupAgent " + classname + ": " + e2.toString(), e2);
            }
            try {
                ActivityManagerNative.getDefault().backupAgentCreated(packageName, binder);
            } catch (RemoteException e3) {
            }
        } catch (RemoteException e4) {
            Slog.e(TAG, "Can't reach package manager", e4);
        }
    }

    private void handleDestroyBackupAgent(CreateBackupAgentData data) {
        String packageName = getPackageInfoNoCheck(data.appInfo, data.compatInfo).mPackageName;
        BackupAgent agent = (BackupAgent) this.mBackupAgents.get(packageName);
        if (agent != null) {
            try {
                agent.onDestroy();
            } catch (Exception e) {
                Slog.w(TAG, "Exception thrown in onDestroy by backup agent of " + data.appInfo);
                e.printStackTrace();
            }
            this.mBackupAgents.remove(packageName);
            return;
        }
        Slog.w(TAG, "Attempt to destroy unknown backup agent " + data);
    }

    private void handleCreateService(CreateServiceData data) {
        unscheduleGcIdler();
        LoadedApk packageInfo = getPackageInfoNoCheck(data.info.applicationInfo, data.compatInfo);
        Service service = null;
        try {
            service = (Service) packageInfo.getClassLoader().loadClass(data.info.name).newInstance();
        } catch (Exception e) {
            if (!this.mInstrumentation.onException(service, e)) {
                throw new RuntimeException("Unable to instantiate service " + data.info.name + ": " + e.toString(), e);
            }
        }
        try {
            ContextImpl context = ContextImpl.createAppContext(this, packageInfo, data.displayId);
            context.setOuterContext(service);
            service.attach(context, this, data.info.name, data.token, packageInfo.makeApplication(false, this.mInstrumentation), ActivityManagerNative.getDefault());
            service.onCreate();
            this.mServices.put(data.token, service);
            try {
                ActivityManagerNative.getDefault().serviceDoneExecuting(data.token, 0, 0, 0);
            } catch (RemoteException e2) {
            }
        } catch (Exception e3) {
            if (!this.mInstrumentation.onException(service, e3)) {
                throw new RuntimeException("Unable to create service " + data.info.name + ": " + e3.toString(), e3);
            }
        }
    }

    private void handleBindService(BindServiceData data) {
        Service s = (Service) this.mServices.get(data.token);
        if (s != null) {
            try {
                data.intent.setExtrasClassLoader(s.getClassLoader());
                data.intent.prepareToEnterProcess();
                try {
                    if (data.rebind) {
                        s.onRebind(data.intent);
                        ActivityManagerNative.getDefault().serviceDoneExecuting(data.token, 0, 0, 0);
                    } else {
                        ActivityManagerNative.getDefault().publishService(data.token, data.intent, s.onBind(data.intent));
                    }
                    ensureJitEnabled();
                } catch (RemoteException e) {
                }
            } catch (Exception e2) {
                if (!this.mInstrumentation.onException(s, e2)) {
                    throw new RuntimeException("Unable to bind to service " + s + " with " + data.intent + ": " + e2.toString(), e2);
                }
            }
        }
    }

    private void handleUnbindService(BindServiceData data) {
        Service s = (Service) this.mServices.get(data.token);
        if (s != null) {
            try {
                data.intent.setExtrasClassLoader(s.getClassLoader());
                data.intent.prepareToEnterProcess();
                boolean doRebind = s.onUnbind(data.intent);
                if (doRebind) {
                    try {
                        ActivityManagerNative.getDefault().unbindFinished(data.token, data.intent, doRebind);
                        return;
                    } catch (RemoteException e) {
                        return;
                    }
                }
                ActivityManagerNative.getDefault().serviceDoneExecuting(data.token, 0, 0, 0);
            } catch (Exception e2) {
                if (!this.mInstrumentation.onException(s, e2)) {
                    throw new RuntimeException("Unable to unbind to service " + s + " with " + data.intent + ": " + e2.toString(), e2);
                }
            }
        }
    }

    private void handleResetTargetHeapUtilization(IBinder token) {
    }

    private void handleDumpService(DumpComponentInfo info) {
        ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            Service s = (Service) this.mServices.get(info.token);
            if (s != null) {
                PrintWriter pw = new FastPrintWriter(new FileOutputStream(info.fd.getFileDescriptor()));
                s.dump(info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        } catch (Throwable th) {
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleDumpActivity(DumpComponentInfo info) {
        ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(info.token);
            if (!(r == null || r.activity == null)) {
                PrintWriter pw = new FastPrintWriter(new FileOutputStream(info.fd.getFileDescriptor()));
                r.activity.dump(info.prefix, info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        } catch (Throwable th) {
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleDumpContextRelationInfo(DumpComponentInfo info) {
        NullPointerException e;
        Throwable th;
        ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        PrintWriter pw = null;
        try {
            PrintWriter pw2 = new PrintWriter(new FileOutputStream(info.fd.getFileDescriptor()));
            try {
                String innerPrefix = info.prefix + "  ";
                boolean dumpDetail = false;
                boolean dumpCallStack = false;
                String[] args = info.args;
                int opti = 0;
                while (opti < args.length) {
                    String opt = args[opti];
                    if (opt == null || opt.length() <= 0 || opt.charAt(0) != '-') {
                        break;
                    }
                    opti++;
                }
                String cmd = null;
                if (opti < args.length) {
                    cmd = args[opti];
                    opti++;
                    if ("cc".equals(cmd)) {
                        dumpCallStack = true;
                    }
                }
                if (!(cmd == null || "cs".equals(cmd) || opti >= args.length)) {
                    dumpDetail = true;
                }
                if (dumpDetail) {
                    dumpThread(pw2, innerPrefix, dumpDetail, dumpCallStack);
                }
                ContextRelationManager.getInstance().dump(pw2, innerPrefix, dumpDetail, dumpCallStack);
                if (pw2 != null) {
                    pw2.flush();
                }
                IoUtils.closeQuietly(info.fd);
                StrictMode.setThreadPolicy(oldPolicy);
                pw = pw2;
            } catch (NullPointerException e2) {
                e = e2;
                pw = pw2;
                try {
                    Slog.w(TAG, "handleDumpContextRelationInfo failed", e);
                    if (pw != null) {
                        pw.flush();
                    }
                    IoUtils.closeQuietly(info.fd);
                    StrictMode.setThreadPolicy(oldPolicy);
                } catch (Throwable th2) {
                    th = th2;
                    if (pw != null) {
                        pw.flush();
                    }
                    IoUtils.closeQuietly(info.fd);
                    StrictMode.setThreadPolicy(oldPolicy);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                pw = pw2;
                if (pw != null) {
                    pw.flush();
                }
                IoUtils.closeQuietly(info.fd);
                StrictMode.setThreadPolicy(oldPolicy);
                throw th;
            }
        } catch (NullPointerException e3) {
            e = e3;
            Slog.w(TAG, "handleDumpContextRelationInfo failed", e);
            if (pw != null) {
                pw.flush();
            }
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    public void dumpThread(PrintWriter pw, String prefix, boolean dumpDetail, boolean dumpCallStack) {
        pw.print(prefix);
        pw.println(ActivityThread.class.getSimpleName() + " : ");
        String innerPrefix = prefix + "  ";
        dumpIntArrayList(this.mCurDefaultDisplayDpis, "mCurDefaultDisplayDpis", pw, innerPrefix);
        dumpObjArrayList(this.mConfigurations, "mConfigurations", pw, innerPrefix);
        dumpObjArrayList(this.mCompatConfigurations, "mCompatConfigurations", pw, innerPrefix);
        dumpObjArrayList(this.mPendingConfigurations, "mPendingConfigurations", pw, innerPrefix);
    }

    public void dumpObjArrayList(ArrayList list, String listName, PrintWriter pw, String prefix) {
        if (list == null) {
            pw.print(prefix);
            pw.println(listName + "=null");
            return;
        }
        pw.print(prefix);
        pw.println(listName + "=");
        int i = 0;
        Iterator i$ = list.iterator();
        while (i$.hasNext()) {
            Object o = i$.next();
            pw.print(prefix + "  [" + i + "]=");
            if (o == null) {
                pw.println("null");
            } else {
                pw.println(o.toString());
            }
            i++;
        }
    }

    public void dumpIntArrayList(ArrayList list, String arrayName, PrintWriter pw, String prefix) {
        if (list == null) {
            pw.print(prefix);
            pw.println(arrayName + " { }");
            return;
        }
        pw.print(prefix);
        pw.println(arrayName + " {");
        for (int i = 0; i < list.size(); i++) {
            pw.print(prefix + "  [" + i + "]=");
            pw.println(list.get(i));
        }
        pw.print(prefix);
        pw.println("}");
    }

    private void handleDumpProvider(DumpComponentInfo info) {
        ThreadPolicy oldPolicy = StrictMode.allowThreadDiskWrites();
        try {
            ProviderClientRecord r = (ProviderClientRecord) this.mLocalProviders.get(info.token);
            if (!(r == null || r.mLocalProvider == null)) {
                PrintWriter pw = new FastPrintWriter(new FileOutputStream(info.fd.getFileDescriptor()));
                r.mLocalProvider.dump(info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        } catch (Throwable th) {
            IoUtils.closeQuietly(info.fd);
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleServiceArgs(ServiceArgsData data) {
        Service s = (Service) this.mServices.get(data.token);
        if (s != null) {
            try {
                int res;
                if (data.args != null) {
                    data.args.setExtrasClassLoader(s.getClassLoader());
                    data.args.prepareToEnterProcess();
                }
                if (data.taskRemoved) {
                    s.onTaskRemoved(data.args);
                    res = 1000;
                } else {
                    res = s.onStartCommand(data.args, data.flags, data.startId);
                }
                QueuedWork.waitToFinish();
                try {
                    ActivityManagerNative.getDefault().serviceDoneExecuting(data.token, 1, data.startId, res);
                } catch (RemoteException e) {
                }
                ensureJitEnabled();
            } catch (Exception e2) {
                if (!this.mInstrumentation.onException(s, e2)) {
                    throw new RuntimeException("Unable to start service " + s + " with " + data.args + ": " + e2.toString(), e2);
                }
            }
        }
    }

    private void handleStopService(IBinder token) {
        Service s = (Service) this.mServices.remove(token);
        if (s != null) {
            try {
                s.onDestroy();
                Context context = s.getBaseContext();
                if (context instanceof ContextImpl) {
                    ((ContextImpl) context).scheduleFinalCleanup(s.getClassName(), "Service");
                }
                QueuedWork.waitToFinish();
                try {
                    ActivityManagerNative.getDefault().serviceDoneExecuting(token, 2, 0, 0);
                    return;
                } catch (RemoteException e) {
                    Slog.i(TAG, "handleStopService: unable to execute serviceDoneExecuting for " + token, e);
                    return;
                }
            } catch (Exception e2) {
                if (this.mInstrumentation.onException(s, e2)) {
                    Slog.i(TAG, "handleStopService: exception for " + token, e2);
                    return;
                }
                throw new RuntimeException("Unable to stop service " + s + ": " + e2.toString(), e2);
            }
        }
        Slog.i(TAG, "handleStopService: token=" + token + " not found.");
    }

    public final ActivityClientRecord performResumeActivity(IBinder token, boolean clearHide) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (!(r == null || r.activity.mFinished)) {
            if (clearHide) {
                r.hideForNow = false;
                r.activity.mStartedActivity = false;
            }
            try {
                r.activity.onStateNotSaved();
                r.activity.mFragments.noteStateNotSaved();
                if (r.pendingIntents != null) {
                    deliverNewIntents(r, r.pendingIntents);
                    r.pendingIntents = null;
                }
                if (r.pendingResults != null) {
                    deliverResults(r, r.pendingResults);
                    r.pendingResults = null;
                }
                r.activity.performResume();
                EventLog.writeEvent(LOG_AM_ON_RESUME_CALLED, new Object[]{Integer.valueOf(UserHandle.myUserId()), r.activity.getComponentName().getClassName()});
                r.paused = false;
                r.stopped = false;
                r.state = null;
                r.persistentState = null;
            } catch (Exception e) {
                if (!this.mInstrumentation.onException(r.activity, e)) {
                    throw new RuntimeException("Unable to resume activity " + r.intent.getComponent().toShortString() + ": " + e.toString(), e);
                }
            }
            if (AppLockPolicy.isSupportAppLock() && !PersonaManager.isKnoxId(UserHandle.getUserId(r.activityInfo.applicationInfo.uid))) {
                Message msg = Message.obtain();
                msg.what = 3000;
                msg.obj = r;
                this.mH.sendMessage(msg);
            }
        }
        return r;
    }

    private void checkAppLockState(ActivityClientRecord r) {
        ActivityManager am = (ActivityManager) getSystemContext().getSystemService(Context.ACTIVITY_SERVICE);
        String pkgName = currentPackageName();
        boolean isAppLocked = am.isAppLockedPackage(pkgName);
        Slog.w(TAG, "AppLock checkAppLockState isAppLocked = " + isAppLocked + " pkgName = " + pkgName);
        if (isAppLocked && !am.isAppLockedVerifying(pkgName)) {
            Parcelable appLockMultiWindowStyle;
            am.setAppLockedVerifying(pkgName, true);
            Intent intent = new Intent("android.intent.action.CHECK_APPLOCK_SERVICE");
            intent.setExtrasClassLoader(r.activity.getClassLoader());
            intent.putExtra("LAUNCH_FROM_RESUME", true);
            intent.putExtra("LOCKED_PACKAGE_NAME", pkgName);
            intent.putExtra("LOCKED_PACKAGE_INTENT", r.intent);
            if (r.parent != null) {
                appLockMultiWindowStyle = r.parent.getMultiWindowStyle();
            } else {
                appLockMultiWindowStyle = r.multiWindowStyle;
            }
            Slog.w(TAG, "AppLock verify multiwindow = " + appLockMultiWindowStyle);
            intent.putExtra("LOCKED_PACKAGE_MULTIWINDOWSTYLE", appLockMultiWindowStyle);
            getSystemContext().startService(intent);
        }
    }

    static final void cleanUpPendingRemoveWindows(ActivityClientRecord r) {
        if (r.mPendingRemoveWindow != null) {
            r.mPendingRemoveWindowManager.removeViewImmediate(r.mPendingRemoveWindow);
            IBinder wtoken = r.mPendingRemoveWindow.getWindowToken();
            if (wtoken != null) {
                WindowManagerGlobal.getInstance().closeAll(wtoken, r.activity.getClass().getName(), "Activity");
            }
        }
        r.mPendingRemoveWindow = null;
        r.mPendingRemoveWindowManager = null;
    }

    final void handleForceCallResumeActivity(IBinder token, boolean clearHide) {
        ActivityClientRecord r = performResumeActivity(token, clearHide);
        if (r != null) {
            Activity a = r.activity;
            boolean willBeVisible = !a.mStartedActivity;
            if (!r.activity.mFinished && willBeVisible && r.activity.mDecor != null && !r.hideForNow) {
                LayoutParams l = r.window.getAttributes();
                if (r.activity.mVisibleFromClient) {
                    a.getWindowManager().updateViewLayout(r.window.getDecorView(), l);
                }
            }
        }
    }

    final void handleResumeActivity(IBinder token, boolean clearHide, boolean isForward, boolean reallyResume) {
        unscheduleGcIdler();
        this.mSomeActivitiesChanged = true;
        ActivityClientRecord r = performResumeActivity(token, clearHide);
        if (r != null) {
            LayoutParams l;
            Activity a = r.activity;
            int forwardBit = isForward ? 256 : 0;
            boolean willBeVisible = !a.mStartedActivity;
            if (!willBeVisible) {
                try {
                    willBeVisible = ActivityManagerNative.getDefault().willActivityBeVisible(a.getActivityToken());
                } catch (RemoteException e) {
                }
            }
            if (r.window == null && !a.mFinished && willBeVisible) {
                boolean isVRActivity;
                r.window = r.activity.getWindow();
                View decor = r.window.getDecorView();
                decor.setVisibility(4);
                ViewManager wm = a.getWindowManager();
                l = r.window.getAttributes();
                a.mDecor = decor;
                l.type = 1;
                l.softInputMode |= forwardBit;
                Bundle applicationMetaData = r.activityInfo.applicationInfo != null ? r.activityInfo.applicationInfo.metaData : null;
                Bundle activityMetaData = r.activityInfo.metaData;
                if (applicationMetaData == null) {
                    isVRActivity = false;
                } else if ("vr_only".equals(applicationMetaData.getString("com.samsung.android.vr.application.mode"))) {
                    isVRActivity = true;
                } else if ("dual".equals(applicationMetaData.getString("com.samsung.android.vr.application.mode")) && activityMetaData != null && activityMetaData.getBoolean("com.samsung.android.vr.activity")) {
                    isVRActivity = true;
                } else {
                    isVRActivity = false;
                }
                if (isVRActivity && SystemProperties.getBoolean("sys.hmt.connected", false)) {
                    l.type = 97;
                    l.flags |= 4718592;
                }
                if (a.mVisibleFromClient) {
                    a.mWindowAdded = true;
                    wm.addView(decor, l);
                }
            } else if (!willBeVisible) {
                r.hideForNow = true;
            }
            cleanUpPendingRemoveWindows(r);
            if (!(r.activity.mFinished || !willBeVisible || r.activity.mDecor == null || r.hideForNow)) {
                if (r.newConfig != null) {
                    r.tmpConfig.setTo(r.newConfig);
                    if (!(r.overrideConfig == null || Configuration.EMPTY.equals(r.overrideConfig))) {
                        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && r.overrideConfig.isStackOverrideConfig()) {
                            r.tmpConfig.updateFromStackOverrideConfig(r.overrideConfig);
                        } else {
                            r.tmpConfig.updateFrom(r.overrideConfig);
                        }
                    }
                    performConfigurationChanged(r.activity, r.tmpConfig);
                    freeTextLayoutCachesIfNeeded(r.activity.mCurrentConfig.diff(r.tmpConfig));
                    r.newConfig = null;
                }
                l = r.window.getAttributes();
                if ((l.softInputMode & 256) != forwardBit) {
                    l.softInputMode = (l.softInputMode & -257) | forwardBit;
                    if (r.activity.mVisibleFromClient) {
                        a.getWindowManager().updateViewLayout(r.window.getDecorView(), l);
                    }
                }
                r.activity.mVisibleFromServer = true;
                this.mNumVisibleActivities++;
                if (r.activity.mVisibleFromClient) {
                    r.activity.makeVisible();
                }
                r.activity.getWindow().onVisibilityChanged(true);
            }
            if (!r.onlyLocalRequest) {
                r.nextIdle = this.mNewActivities;
                this.mNewActivities = r;
                Looper.myQueue().addIdleHandler(new Idler());
            }
            r.onlyLocalRequest = false;
            if (reallyResume) {
                try {
                    ActivityManagerNative.getDefault().activityResumed(token);
                    return;
                } catch (RemoteException e2) {
                    return;
                }
            }
            return;
        }
        try {
            ActivityManagerNative.getDefault().finishActivity(token, 0, null, false);
        } catch (RemoteException e3) {
        }
    }

    private Bitmap createThumbnailBitmap(ActivityClientRecord r) {
        Bitmap thumbnail = this.mAvailThumbnailBitmap;
        if (thumbnail == null) {
            try {
                int h;
                int w = this.mThumbnailWidth;
                if (w < 0) {
                    Resources res = r.activity.getResources();
                    w = res.getDimensionPixelSize(R.dimen.thumbnail_width);
                    this.mThumbnailWidth = w;
                    h = res.getDimensionPixelSize(R.dimen.thumbnail_height);
                    this.mThumbnailHeight = h;
                } else {
                    h = this.mThumbnailHeight;
                }
                if (w > 0 && h > 0) {
                    thumbnail = Bitmap.createBitmap(r.activity.getResources().getDisplayMetrics(), w, h, THUMBNAIL_FORMAT);
                    thumbnail.eraseColor(0);
                }
            } catch (Exception e) {
                if (this.mInstrumentation.onException(r.activity, e)) {
                    return null;
                }
                throw new RuntimeException("Unable to create thumbnail of " + r.intent.getComponent().toShortString() + ": " + e.toString(), e);
            }
        }
        if (thumbnail == null) {
            return thumbnail;
        }
        Canvas cv = this.mThumbnailCanvas;
        if (cv == null) {
            cv = new Canvas();
            this.mThumbnailCanvas = cv;
        }
        cv.setBitmap(thumbnail);
        if (!r.activity.onCreateThumbnail(thumbnail, cv)) {
            this.mAvailThumbnailBitmap = thumbnail;
            thumbnail = null;
        }
        cv.setBitmap(null);
        return thumbnail;
    }

    private void handlePauseActivity(IBinder token, boolean finished, boolean userLeaving, int configChanges, boolean dontReport) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r != null) {
            if (userLeaving) {
                performUserLeavingActivity(r);
            }
            Activity activity = r.activity;
            activity.mConfigChangeFlags |= configChanges;
            performPauseActivity(token, finished, r.isPreHoneycomb());
            if (r.isPreHoneycomb()) {
                QueuedWork.waitToFinish();
            }
            if (!dontReport) {
                try {
                    ActivityManagerNative.getDefault().activityPaused(token);
                } catch (RemoteException e) {
                }
            }
            this.mSomeActivitiesChanged = true;
        }
    }

    final void performUserLeavingActivity(ActivityClientRecord r) {
        this.mInstrumentation.callActivityOnUserLeaving(r.activity);
    }

    final Bundle performPauseActivity(IBinder token, boolean finished, boolean saveState) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        return r != null ? performPauseActivity(r, finished, saveState) : null;
    }

    final Bundle performPauseActivity(ActivityClientRecord r, boolean finished, boolean saveState) {
        int size = 0;
        if (r.paused) {
            if (r.activity.mFinished) {
                return null;
            }
            RuntimeException e = new RuntimeException("Performing pause of activity that is not resumed: " + r.intent.getComponent().toShortString());
            Slog.e(TAG, e.getMessage(), e);
        }
        if (finished) {
            r.activity.mFinished = true;
        }
        try {
            if (!r.activity.mFinished && saveState) {
                callCallActivityOnSaveInstanceState(r);
            }
            r.activity.mCalled = false;
            this.mInstrumentation.callActivityOnPause(r.activity);
            EventLog.writeEvent(LOG_AM_ON_PAUSE_CALLED, new Object[]{Integer.valueOf(UserHandle.myUserId()), r.activity.getComponentName().getClassName()});
            if (!r.activity.mCalled) {
                throw new SuperNotCalledException("Activity " + r.intent.getComponent().toShortString() + " did not call through to super.onPause()");
            }
        } catch (SuperNotCalledException e2) {
            throw e2;
        } catch (Exception e3) {
            if (!this.mInstrumentation.onException(r.activity, e3)) {
                throw new RuntimeException("Unable to pause activity " + r.intent.getComponent().toShortString() + ": " + e3.toString(), e3);
            }
        }
        r.paused = true;
        synchronized (this.mOnPauseListeners) {
            ArrayList<OnActivityPausedListener> listeners = (ArrayList) this.mOnPauseListeners.remove(r.activity);
        }
        if (listeners != null) {
            size = listeners.size();
        }
        for (int i = 0; i < size; i++) {
            ((OnActivityPausedListener) listeners.get(i)).onPaused(r.activity);
        }
        Bundle bundle = (r.activity.mFinished || !saveState) ? null : r.state;
        return bundle;
    }

    final void performStopActivity(IBinder token, boolean saveState) {
        performStopActivityInner((ActivityClientRecord) this.mActivities.get(token), null, false, saveState);
    }

    private void performStopActivityInner(ActivityClientRecord r, StopInfo info, boolean keepShown, boolean saveState) {
        if (r != null) {
            if (!keepShown && r.stopped) {
                if (!r.activity.mFinished) {
                    RuntimeException e = new RuntimeException("Performing stop of activity that is not resumed: " + r.intent.getComponent().toShortString());
                    Slog.e(TAG, e.getMessage(), e);
                } else {
                    return;
                }
            }
            if (info != null) {
                try {
                    info.description = r.activity.onCreateDescription();
                } catch (Exception e2) {
                    if (!this.mInstrumentation.onException(r.activity, e2)) {
                        throw new RuntimeException("Unable to save state of activity " + r.intent.getComponent().toShortString() + ": " + e2.toString(), e2);
                    }
                }
            }
            if (!r.activity.mFinished && saveState && r.state == null) {
                callCallActivityOnSaveInstanceState(r);
            }
            if (!keepShown) {
                try {
                    r.activity.performStop();
                } catch (Exception e22) {
                    if (!this.mInstrumentation.onException(r.activity, e22)) {
                        throw new RuntimeException("Unable to stop activity " + r.intent.getComponent().toShortString() + ": " + e22.toString(), e22);
                    }
                }
                r.stopped = true;
            }
            r.paused = true;
        }
    }

    private void updateVisibility(ActivityClientRecord r, boolean show) {
        View v = r.activity.mDecor;
        if (v != null) {
            if (show) {
                if (!r.activity.mVisibleFromServer) {
                    r.activity.mVisibleFromServer = true;
                    this.mNumVisibleActivities++;
                    if (r.activity.mVisibleFromClient) {
                        r.activity.makeVisible();
                    }
                }
                if (r.newConfig != null) {
                    r.tmpConfig.setTo(r.newConfig);
                    if (!(r.overrideConfig == null || Configuration.EMPTY.equals(r.overrideConfig))) {
                        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && r.overrideConfig.isStackOverrideConfig()) {
                            r.tmpConfig.updateFromStackOverrideConfig(r.overrideConfig);
                        } else {
                            r.tmpConfig.updateFrom(r.overrideConfig);
                        }
                    }
                    performConfigurationChanged(r.activity, r.tmpConfig);
                    freeTextLayoutCachesIfNeeded(r.activity.mCurrentConfig.diff(r.tmpConfig));
                    r.newConfig = null;
                }
            } else if (r.activity.mVisibleFromServer) {
                r.activity.mVisibleFromServer = false;
                this.mNumVisibleActivities--;
                v.setVisibility(4);
            }
            if (r != null && r.activity != null) {
                Slog.v(TAG, "updateVisibility : " + r + " show : " + show);
                r.activity.getWindow().onVisibilityChanged(show);
            }
        }
    }

    private void handleStopActivity(IBinder token, boolean show, int configChanges) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        Activity activity = r.activity;
        activity.mConfigChangeFlags |= configChanges;
        StopInfo info = new StopInfo();
        performStopActivityInner(r, info, show, true);
        updateVisibility(r, show);
        if (!r.isPreHoneycomb()) {
            QueuedWork.waitToFinish();
        }
        info.activity = r;
        info.state = r.state;
        info.persistentState = r.persistentState;
        this.mH.post(info);
        this.mSomeActivitiesChanged = true;
    }

    final void performRestartActivity(IBinder token) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r.stopped) {
            r.activity.performRestart();
            r.stopped = false;
        }
    }

    private void handleWindowVisibility(IBinder token, boolean show) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r == null) {
            Log.w(TAG, "handleWindowVisibility: no activity for token " + token);
            return;
        }
        if (!show && !r.stopped) {
            performStopActivityInner(r, null, show, false);
        } else if (show && r.stopped) {
            unscheduleGcIdler();
            r.activity.performRestart();
            r.stopped = false;
        }
        if (r.activity.mDecor != null) {
            updateVisibility(r, show);
        }
        this.mSomeActivitiesChanged = true;
    }

    private void handleSleeping(IBinder token, boolean sleeping) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (r == null) {
            Log.w(TAG, "handleSleeping: no activity for token " + token);
        } else if (sleeping) {
            if (!(r.stopped || r.isPreHoneycomb())) {
                try {
                    r.activity.performStop();
                } catch (Exception e) {
                    if (!this.mInstrumentation.onException(r.activity, e)) {
                        throw new RuntimeException("Unable to stop activity " + r.intent.getComponent().toShortString() + ": " + e.toString(), e);
                    }
                }
                r.stopped = true;
            }
            if (!r.isPreHoneycomb()) {
                QueuedWork.waitToFinish();
            }
            try {
                ActivityManagerNative.getDefault().activitySlept(r.token);
            } catch (RemoteException e2) {
            }
        } else if (r.stopped && r.activity.mVisibleFromServer) {
            r.activity.performRestart();
            r.stopped = false;
        }
    }

    private void handleSetCoreSettings(Bundle coreSettings) {
        synchronized (this.mResourcesManager) {
            this.mCoreSettings = coreSettings;
        }
        onCoreSettingsChange();
    }

    private void onCoreSettingsChange() {
        boolean debugViewAttributes;
        if (this.mCoreSettings.getInt(Global.DEBUG_VIEW_ATTRIBUTES, 0) != 0) {
            debugViewAttributes = true;
        } else {
            debugViewAttributes = false;
        }
        if (debugViewAttributes != View.mDebugViewAttributes) {
            View.mDebugViewAttributes = debugViewAttributes;
            for (Entry<IBinder, ActivityClientRecord> entry : this.mActivities.entrySet()) {
                requestRelaunchActivity((IBinder) entry.getKey(), null, null, 0, false, null, null, false, ((ActivityClientRecord) entry.getValue()).multiWindowStyle, ((ActivityClientRecord) entry.getValue()).displayId);
            }
        }
    }

    private void handleUpdatePackageCompatibilityInfo(UpdateCompatibilityData data) {
        LoadedApk apk = peekPackageInfo(data.pkg, false);
        if (apk != null) {
            apk.setCompatibilityInfo(data.info);
        }
        apk = peekPackageInfo(data.pkg, true);
        if (apk != null) {
            apk.setCompatibilityInfo(data.info);
        }
        handleConfigurationChanged(this.mConfiguration, data.info);
        WindowManagerGlobal.getInstance().reportNewConfiguration(this.mConfiguration);
    }

    private void deliverResults(ActivityClientRecord r, List<ResultInfo> results) {
        int N = results.size();
        for (int i = 0; i < N; i++) {
            ResultInfo ri = (ResultInfo) results.get(i);
            try {
                if (ri.mData != null) {
                    ri.mData.setExtrasClassLoader(r.activity.getClassLoader());
                    ri.mData.prepareToEnterProcess();
                }
                r.activity.dispatchActivityResult(ri.mResultWho, ri.mRequestCode, ri.mResultCode, ri.mData);
            } catch (Exception e) {
                if (!this.mInstrumentation.onException(r.activity, e)) {
                    throw new RuntimeException("Failure delivering result " + ri + " to activity " + r.intent.getComponent().toShortString() + ": " + e.toString(), e);
                }
            }
        }
    }

    private void handleSendResult(ResultData res) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(res.token);
        if (r != null) {
            boolean resumed;
            if (r.paused) {
                resumed = false;
            } else {
                resumed = true;
            }
            if (!r.activity.mFinished && r.activity.mDecor != null && r.hideForNow && resumed) {
                updateVisibility(r, true);
            }
            if (resumed) {
                try {
                    r.activity.mCalled = false;
                    r.activity.mTemporaryPause = true;
                    this.mInstrumentation.callActivityOnPause(r.activity);
                    if (!r.activity.mCalled) {
                        throw new SuperNotCalledException("Activity " + r.intent.getComponent().toShortString() + " did not call through to super.onPause()");
                    }
                } catch (SuperNotCalledException e) {
                    throw e;
                } catch (Exception e2) {
                    if (!this.mInstrumentation.onException(r.activity, e2)) {
                        throw new RuntimeException("Unable to pause activity " + r.intent.getComponent().toShortString() + ": " + e2.toString(), e2);
                    }
                }
            }
            deliverResults(r, res.results);
            if (resumed) {
                r.activity.performResume();
                r.activity.mTemporaryPause = false;
            }
        }
    }

    public final ActivityClientRecord performDestroyActivity(IBinder token, boolean finishing) {
        return performDestroyActivity(token, finishing, 0, false);
    }

    private ActivityClientRecord performDestroyActivity(IBinder token, boolean finishing, int configChanges, boolean getNonConfigInstance) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        Class<? extends Activity> activityClass = null;
        if (r != null) {
            activityClass = r.activity.getClass();
            Activity activity = r.activity;
            activity.mConfigChangeFlags |= configChanges;
            if (finishing) {
                r.activity.mFinished = true;
            }
            if (!r.paused) {
                try {
                    r.activity.mCalled = false;
                    this.mInstrumentation.callActivityOnPause(r.activity);
                    EventLog.writeEvent(LOG_AM_ON_PAUSE_CALLED, new Object[]{Integer.valueOf(UserHandle.myUserId()), r.activity.getComponentName().getClassName()});
                    if (!r.activity.mCalled) {
                        throw new SuperNotCalledException("Activity " + safeToComponentShortString(r.intent) + " did not call through to super.onPause()");
                    }
                } catch (SuperNotCalledException e) {
                    throw e;
                } catch (Exception e2) {
                    if (!this.mInstrumentation.onException(r.activity, e2)) {
                        throw new RuntimeException("Unable to pause activity " + safeToComponentShortString(r.intent) + ": " + e2.toString(), e2);
                    }
                }
                r.paused = true;
            }
            if (!r.stopped) {
                try {
                    r.activity.performStop();
                } catch (SuperNotCalledException e3) {
                    throw e3;
                } catch (Exception e22) {
                    if (!this.mInstrumentation.onException(r.activity, e22)) {
                        throw new RuntimeException("Unable to stop activity " + safeToComponentShortString(r.intent) + ": " + e22.toString(), e22);
                    }
                }
                r.stopped = true;
            }
            if (getNonConfigInstance) {
                try {
                    r.lastNonConfigurationInstances = r.activity.retainNonConfigurationInstances();
                } catch (Exception e222) {
                    if (!this.mInstrumentation.onException(r.activity, e222)) {
                        throw new RuntimeException("Unable to retain activity " + r.intent.getComponent().toShortString() + ": " + e222.toString(), e222);
                    }
                }
            }
            try {
                r.activity.mCalled = false;
                this.mInstrumentation.callActivityOnDestroy(r.activity);
                if (!r.activity.mCalled) {
                    throw new SuperNotCalledException("Activity " + safeToComponentShortString(r.intent) + " did not call through to super.onDestroy()");
                } else if (r.window != null) {
                    r.window.closeAllPanels();
                }
            } catch (SuperNotCalledException e32) {
                throw e32;
            } catch (Exception e2222) {
                if (!this.mInstrumentation.onException(r.activity, e2222)) {
                    throw new RuntimeException("Unable to destroy activity " + safeToComponentShortString(r.intent) + ": " + e2222.toString(), e2222);
                }
            }
        }
        this.mActivities.remove(token);
        StrictMode.decrementExpectedActivityCount(activityClass);
        return r;
    }

    private static String safeToComponentShortString(Intent intent) {
        ComponentName component = intent.getComponent();
        return component == null ? "[Unknown]" : component.toShortString();
    }

    private void handleDestroyActivity(IBinder token, boolean finishing, int configChanges, boolean getNonConfigInstance) {
        ActivityClientRecord r = performDestroyActivity(token, finishing, configChanges, getNonConfigInstance);
        if (r != null) {
            cleanUpPendingRemoveWindows(r);
            WindowManager wm = r.activity.getWindowManager();
            View v = r.activity.mDecor;
            if (v != null) {
                if (r.activity.mVisibleFromServer) {
                    this.mNumVisibleActivities--;
                }
                IBinder wtoken = v.getWindowToken();
                if (r.activity.mWindowAdded) {
                    if (r.onlyLocalRequest) {
                        r.mPendingRemoveWindow = v;
                        r.mPendingRemoveWindowManager = wm;
                    } else {
                        wm.removeViewImmediate(v);
                    }
                }
                if (wtoken != null && r.mPendingRemoveWindow == null) {
                    WindowManagerGlobal.getInstance().closeAll(wtoken, r.activity.getClass().getName(), "Activity");
                }
                r.activity.mDecor = null;
            }
            if (r.mPendingRemoveWindow == null) {
                WindowManagerGlobal.getInstance().closeAll(token, r.activity.getClass().getName(), "Activity");
            }
            Context c = r.activity.getBaseContext();
            if (c instanceof ContextImpl) {
                ((ContextImpl) c).scheduleFinalCleanup(r.activity.getClass().getName(), "Activity");
            }
        }
        if (finishing) {
            try {
                ActivityManagerNative.getDefault().activityDestroyed(token);
            } catch (RemoteException e) {
            }
        }
        this.mSomeActivitiesChanged = true;
    }

    public final void requestRelaunchActivity(IBinder token, List<ResultInfo> pendingResults, List<ReferrerIntent> pendingNewIntents, int configChanges, boolean notResumed, Configuration config, Configuration overrideConfig, boolean fromServer, MultiWindowStyle multiWindowStyle, int displayId) {
        ActivityClientRecord target;
        ActivityClientRecord target2 = null;
        synchronized (this.mResourcesManager) {
            int i = 0;
            while (i < this.mRelaunchingActivities.size()) {
                ActivityClientRecord existing;
                try {
                    ActivityClientRecord r = (ActivityClientRecord) this.mRelaunchingActivities.get(i);
                    if (r.token == token) {
                        target2 = r;
                        if (pendingResults != null) {
                            if (r.pendingResults != null) {
                                r.pendingResults.addAll(pendingResults);
                            } else {
                                r.pendingResults = pendingResults;
                            }
                        }
                        if (pendingNewIntents != null) {
                            if (r.pendingIntents != null) {
                                r.pendingIntents.addAll(pendingNewIntents);
                                target = target2;
                            } else {
                                r.pendingIntents = pendingNewIntents;
                                target = target2;
                            }
                            if (target == null) {
                                try {
                                    target2 = new ActivityClientRecord();
                                    target2.token = token;
                                    target2.pendingResults = pendingResults;
                                    target2.pendingIntents = pendingNewIntents;
                                    target2.multiWindowStyle.setTo(multiWindowStyle, true);
                                    target2.displayId = displayId;
                                    if (!fromServer) {
                                        existing = (ActivityClientRecord) this.mActivities.get(token);
                                        if (existing != null) {
                                            target2.startsNotResumed = existing.paused;
                                            target2.overrideConfig = existing.overrideConfig;
                                        }
                                        target2.onlyLocalRequest = true;
                                    }
                                    this.mRelaunchingActivities.add(target2);
                                    sendMessage(126, target2);
                                } catch (Throwable th) {
                                    Throwable th2 = th;
                                    target2 = target;
                                    throw th2;
                                }
                            }
                            target2 = target;
                            if (fromServer) {
                                target2.startsNotResumed = notResumed;
                                target2.onlyLocalRequest = false;
                            }
                            if (config != null) {
                                target2.createdConfig = config;
                            }
                            if (overrideConfig != null) {
                                target2.overrideConfig = overrideConfig;
                            }
                            target2.pendingConfigChanges |= configChanges;
                        }
                        target = target2;
                        if (target == null) {
                            target2 = target;
                        } else {
                            target2 = new ActivityClientRecord();
                            target2.token = token;
                            target2.pendingResults = pendingResults;
                            target2.pendingIntents = pendingNewIntents;
                            target2.multiWindowStyle.setTo(multiWindowStyle, true);
                            target2.displayId = displayId;
                            if (fromServer) {
                                existing = (ActivityClientRecord) this.mActivities.get(token);
                                if (existing != null) {
                                    target2.startsNotResumed = existing.paused;
                                    target2.overrideConfig = existing.overrideConfig;
                                }
                                target2.onlyLocalRequest = true;
                            }
                            this.mRelaunchingActivities.add(target2);
                            sendMessage(126, target2);
                        }
                        if (fromServer) {
                            target2.startsNotResumed = notResumed;
                            target2.onlyLocalRequest = false;
                        }
                        if (config != null) {
                            target2.createdConfig = config;
                        }
                        if (overrideConfig != null) {
                            target2.overrideConfig = overrideConfig;
                        }
                        target2.pendingConfigChanges |= configChanges;
                    }
                    i++;
                } catch (Throwable th3) {
                    th2 = th3;
                }
            }
            target = target2;
            if (target == null) {
                target2 = new ActivityClientRecord();
                target2.token = token;
                target2.pendingResults = pendingResults;
                target2.pendingIntents = pendingNewIntents;
                target2.multiWindowStyle.setTo(multiWindowStyle, true);
                target2.displayId = displayId;
                if (fromServer) {
                    existing = (ActivityClientRecord) this.mActivities.get(token);
                    if (existing != null) {
                        target2.startsNotResumed = existing.paused;
                        target2.overrideConfig = existing.overrideConfig;
                    }
                    target2.onlyLocalRequest = true;
                }
                this.mRelaunchingActivities.add(target2);
                sendMessage(126, target2);
            } else {
                target2 = target;
            }
            if (fromServer) {
                target2.startsNotResumed = notResumed;
                target2.onlyLocalRequest = false;
            }
            if (config != null) {
                target2.createdConfig = config;
            }
            if (overrideConfig != null) {
                target2.overrideConfig = overrideConfig;
            }
            target2.pendingConfigChanges |= configChanges;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void handleRelaunchActivity(android.app.ActivityThread.ActivityClientRecord r14) {
        /*
        r13 = this;
        r12 = 0;
        r11 = 1;
        r10 = 0;
        r13.unscheduleGcIdler();
        r13.mSomeActivitiesChanged = r11;
        r1 = 0;
        r2 = 0;
        r8 = r14.createdConfig;
        if (r8 == 0) goto L_0x003d;
    L_0x000e:
        r8 = r14.createdConfig;
        r4 = r8.displayId;
    L_0x0012:
        r9 = r13.mResourcesManager;
        monitor-enter(r9);
        r8 = r13.mRelaunchingActivities;	 Catch:{ all -> 0x0104 }
        r0 = r8.size();	 Catch:{ all -> 0x0104 }
        r7 = r14.token;	 Catch:{ all -> 0x0104 }
        r14 = 0;
        r5 = 0;
    L_0x001f:
        if (r5 >= r0) goto L_0x0040;
    L_0x0021:
        r8 = r13.mRelaunchingActivities;	 Catch:{ all -> 0x0104 }
        r6 = r8.get(r5);	 Catch:{ all -> 0x0104 }
        r6 = (android.app.ActivityThread.ActivityClientRecord) r6;	 Catch:{ all -> 0x0104 }
        r8 = r6.token;	 Catch:{ all -> 0x0104 }
        if (r8 != r7) goto L_0x003a;
    L_0x002d:
        r14 = r6;
        r8 = r14.pendingConfigChanges;	 Catch:{ all -> 0x0104 }
        r2 = r2 | r8;
        r8 = r13.mRelaunchingActivities;	 Catch:{ all -> 0x0104 }
        r8.remove(r5);	 Catch:{ all -> 0x0104 }
        r5 = r5 + -1;
        r0 = r0 + -1;
    L_0x003a:
        r5 = r5 + 1;
        goto L_0x001f;
    L_0x003d:
        r4 = r14.displayId;
        goto L_0x0012;
    L_0x0040:
        if (r14 != 0) goto L_0x0044;
    L_0x0042:
        monitor-exit(r9);	 Catch:{ all -> 0x0104 }
    L_0x0043:
        return;
    L_0x0044:
        r8 = r13.mPendingConfiguration;	 Catch:{ all -> 0x0104 }
        if (r8 == 0) goto L_0x004d;
    L_0x0048:
        r1 = r13.mPendingConfiguration;	 Catch:{ all -> 0x0104 }
        r8 = 0;
        r13.mPendingConfiguration = r8;	 Catch:{ all -> 0x0104 }
    L_0x004d:
        monitor-exit(r9);	 Catch:{ all -> 0x0104 }
        r8 = r14.createdConfig;
        if (r8 == 0) goto L_0x0076;
    L_0x0052:
        r8 = r13.mConfiguration;
        if (r8 == 0) goto L_0x006a;
    L_0x0056:
        r8 = r14.createdConfig;
        r9 = r13.mConfiguration;
        r8 = r8.isOtherSeqNewer(r9);
        if (r8 == 0) goto L_0x0076;
    L_0x0060:
        r8 = r13.mConfiguration;
        r9 = r14.createdConfig;
        r8 = r8.diff(r9);
        if (r8 == 0) goto L_0x0076;
    L_0x006a:
        if (r1 == 0) goto L_0x0074;
    L_0x006c:
        r8 = r14.createdConfig;
        r8 = r8.isOtherSeqNewer(r1);
        if (r8 == 0) goto L_0x0076;
    L_0x0074:
        r1 = r14.createdConfig;
    L_0x0076:
        if (r1 == 0) goto L_0x0082;
    L_0x0078:
        r8 = r1.densityDpi;
        r13.mCurDefaultDisplayDpi = r8;
        r13.updateDefaultDensity();
        r13.handleConfigurationChanged(r1, r10);
    L_0x0082:
        r8 = r13.mActivities;
        r9 = r14.token;
        r6 = r8.get(r9);
        r6 = (android.app.ActivityThread.ActivityClientRecord) r6;
        if (r6 == 0) goto L_0x0043;
    L_0x008e:
        r8 = r6.activity;
        r9 = r8.mConfigChangeFlags;
        r9 = r9 | r2;
        r8.mConfigChangeFlags = r9;
        r8 = r14.onlyLocalRequest;
        r6.onlyLocalRequest = r8;
        r8 = r6.activity;
        r3 = r8.mIntent;
        r8 = r6.activity;
        r8.mChangingConfigurations = r11;
        r8 = r6.onlyLocalRequest;
        if (r8 == 0) goto L_0x00a9;
    L_0x00a5:
        r8 = r6.paused;
        r14.startsNotResumed = r8;
    L_0x00a9:
        r8 = r6.paused;
        if (r8 != 0) goto L_0x00b6;
    L_0x00ad:
        r8 = r6.token;
        r9 = r6.isPreHoneycomb();
        r13.performPauseActivity(r8, r12, r9);
    L_0x00b6:
        r8 = r6.state;
        if (r8 != 0) goto L_0x00c7;
    L_0x00ba:
        r8 = r6.stopped;
        if (r8 != 0) goto L_0x00c7;
    L_0x00be:
        r8 = r6.isPreHoneycomb();
        if (r8 != 0) goto L_0x00c7;
    L_0x00c4:
        r13.callCallActivityOnSaveInstanceState(r6);
    L_0x00c7:
        r8 = r6.token;
        r13.handleDestroyActivity(r8, r12, r2, r11);
        r6.activity = r10;
        r6.window = r10;
        r6.hideForNow = r12;
        r6.nextIdle = r10;
        r8 = r14.pendingResults;
        if (r8 == 0) goto L_0x00e0;
    L_0x00d8:
        r8 = r6.pendingResults;
        if (r8 != 0) goto L_0x0107;
    L_0x00dc:
        r8 = r14.pendingResults;
        r6.pendingResults = r8;
    L_0x00e0:
        r8 = r14.pendingIntents;
        if (r8 == 0) goto L_0x00ec;
    L_0x00e4:
        r8 = r6.pendingIntents;
        if (r8 != 0) goto L_0x010f;
    L_0x00e8:
        r8 = r14.pendingIntents;
        r6.pendingIntents = r8;
    L_0x00ec:
        r8 = r14.startsNotResumed;
        r6.startsNotResumed = r8;
        r8 = r14.overrideConfig;
        r6.overrideConfig = r8;
        r8 = r14.multiWindowStyle;
        if (r8 == 0) goto L_0x00ff;
    L_0x00f8:
        r8 = r6.multiWindowStyle;
        r9 = r14.multiWindowStyle;
        r8.setTo(r9, r11);
    L_0x00ff:
        r13.handleLaunchActivity(r6, r3);
        goto L_0x0043;
    L_0x0104:
        r8 = move-exception;
        monitor-exit(r9);	 Catch:{ all -> 0x0104 }
        throw r8;
    L_0x0107:
        r8 = r6.pendingResults;
        r9 = r14.pendingResults;
        r8.addAll(r9);
        goto L_0x00e0;
    L_0x010f:
        r8 = r6.pendingIntents;
        r9 = r14.pendingIntents;
        r8.addAll(r9);
        goto L_0x00ec;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.handleRelaunchActivity(android.app.ActivityThread$ActivityClientRecord):void");
    }

    private void callCallActivityOnSaveInstanceState(ActivityClientRecord r) {
        r.state = new Bundle();
        r.state.setAllowFds(false);
        if (r.isPersistable()) {
            r.persistentState = new PersistableBundle();
            this.mInstrumentation.callActivityOnSaveInstanceState(r.activity, r.state, r.persistentState);
            return;
        }
        this.mInstrumentation.callActivityOnSaveInstanceState(r.activity, r.state);
    }

    ArrayList<ComponentCallbacks2> collectComponentCallbacks(boolean allActivities, Configuration newConfig) {
        ArrayList<ComponentCallbacks2> callbacks = new ArrayList();
        synchronized (this.mResourcesManager) {
            int i;
            int NAPP = this.mAllApplications.size();
            for (i = 0; i < NAPP; i++) {
                callbacks.add(this.mAllApplications.get(i));
            }
            int NACT = this.mActivities.size();
            for (i = 0; i < NACT; i++) {
                ActivityClientRecord ar = (ActivityClientRecord) this.mActivities.valueAt(i);
                int mCurDefaultDisplayDpi = this.mCurDefaultDisplayDpi;
                Activity a = ar.activity;
                if (a != null) {
                    Configuration thisConfig = applyConfigCompatMainThread(mCurDefaultDisplayDpi, newConfig, ar.packageInfo.getCompatibilityInfo());
                    if (!ar.activity.mFinished && (allActivities || !ar.paused)) {
                        callbacks.add(a);
                    } else if (thisConfig != null) {
                        ar.newConfig = thisConfig;
                    }
                }
            }
            int NSVC = this.mServices.size();
            for (i = 0; i < NSVC; i++) {
                callbacks.add(this.mServices.valueAt(i));
            }
        }
        synchronized (this.mProviderMap) {
            int NPRV = this.mLocalProviders.size();
            for (i = 0; i < NPRV; i++) {
                callbacks.add(((ProviderClientRecord) this.mLocalProviders.valueAt(i)).mLocalProvider);
            }
        }
        return callbacks;
    }

    private static void performConfigurationChanged(ComponentCallbacks2 cb, Configuration config) {
        Activity activity = cb instanceof Activity ? (Activity) cb : null;
        if (activity != null) {
            activity.mCalled = false;
        }
        boolean shouldChangeConfig = false;
        if (activity == null || activity.mCurrentConfig == null) {
            shouldChangeConfig = true;
        } else {
            int diff = activity.mCurrentConfig.diff(config);
            if (diff != 0 && ((activity.mActivityInfo.getRealConfigChanged() ^ -1) & diff) == 0) {
                shouldChangeConfig = true;
            }
        }
        if (shouldChangeConfig) {
            cb.onConfigurationChanged(config);
            if (activity != null) {
                if (InjectionManager.getInstance() != null) {
                    InjectionManager.getInstance().dispatchParentCallToFeature(DispatchParentCall.ONCONFIGURATIONCHANGED, (Object) cb, config);
                }
                if (activity.mCalled) {
                    activity.mConfigChangeFlags = 0;
                    activity.mCurrentConfig = new Configuration(config);
                    return;
                }
                throw new SuperNotCalledException("Activity " + activity.getLocalClassName() + " did not call through to super.onConfigurationChanged()");
            }
        }
    }

    public final void applyConfigurationToResources(Configuration config) {
        synchronized (this.mResourcesManager) {
            this.mResourcesManager.applyConfigurationToResourcesLocked(config, null);
        }
    }

    final Configuration applyCompatConfiguration(int displayDensity) {
        Configuration config = this.mConfiguration;
        if (this.mCompatConfiguration == null) {
            this.mCompatConfiguration = new Configuration();
        }
        this.mCompatConfiguration.setTo(this.mConfiguration);
        if (this.mResourcesManager.applyCompatConfiguration(displayDensity, this.mCompatConfiguration)) {
            return this.mCompatConfiguration;
        }
        return config;
    }

    final Configuration applyCompatConfiguration(int displayId, int displayDensity) {
        Configuration config = (Configuration) this.mConfigurations.get(displayId);
        if (this.mCompatConfigurations.get(displayId) == null) {
            this.mCompatConfigurations.set(displayId, new Configuration());
        }
        ((Configuration) this.mCompatConfigurations.get(displayId)).setTo((Configuration) this.mConfigurations.get(displayId));
        if (this.mResourcesManager.applyCompatConfiguration(displayDensity, (Configuration) this.mCompatConfigurations.get(displayId))) {
            return (Configuration) this.mCompatConfigurations.get(displayId);
        }
        return config;
    }

    final void handleConfigurationChanged(Configuration config, CompatibilityInfo compat) {
        handleConfigurationChanged(config, compat, -1, config == null ? 0 : config.displayId, false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    final void handleConfigurationChanged(android.content.res.Configuration r13, android.content.res.CompatibilityInfo r14, int r15, int r16, boolean r17) {
        /*
        r12 = this;
        if (r13 == 0) goto L_0x0024;
    L_0x0002:
        r5 = r13.displayId;
    L_0x0004:
        r3 = 0;
        r11 = r12.mResourcesManager;
        monitor-enter(r11);
        r10 = r12.mPendingConfiguration;	 Catch:{ all -> 0x005b }
        if (r10 == 0) goto L_0x0020;
    L_0x000c:
        r10 = r12.mPendingConfiguration;	 Catch:{ all -> 0x005b }
        r10 = r10.isOtherSeqNewer(r13);	 Catch:{ all -> 0x005b }
        if (r10 != 0) goto L_0x001d;
    L_0x0014:
        r13 = r12.mPendingConfiguration;	 Catch:{ all -> 0x005b }
        r10 = r13.densityDpi;	 Catch:{ all -> 0x005b }
        r12.mCurDefaultDisplayDpi = r10;	 Catch:{ all -> 0x005b }
        r12.updateDefaultDensity();	 Catch:{ all -> 0x005b }
    L_0x001d:
        r10 = 0;
        r12.mPendingConfiguration = r10;	 Catch:{ all -> 0x005b }
    L_0x0020:
        if (r13 != 0) goto L_0x0027;
    L_0x0022:
        monitor-exit(r11);	 Catch:{ all -> 0x005b }
    L_0x0023:
        return;
    L_0x0024:
        r5 = r16;
        goto L_0x0004;
    L_0x0027:
        r6 = 0;
        r10 = 1;
        if (r15 != r10) goto L_0x005e;
    L_0x002b:
        r10 = 1;
        r13.orientation = r10;	 Catch:{ all -> 0x005b }
        r10 = 1;
        r12.mPrevScreenOrientationDefined = r10;	 Catch:{ all -> 0x005b }
        r6 = 1;
    L_0x0032:
        r10 = com.samsung.android.multiwindow.MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED;	 Catch:{ all -> 0x005b }
        if (r10 == 0) goto L_0x0038;
    L_0x0036:
        r12.mTempConfigurationForSelectiveOrientation = r13;	 Catch:{ all -> 0x005b }
    L_0x0038:
        r10 = r12.mResourcesManager;	 Catch:{ all -> 0x005b }
        r10.applyConfigurationToResourcesLocked(r13, r14, r6);	 Catch:{ all -> 0x005b }
        r10 = com.samsung.android.multiwindow.MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED;	 Catch:{ all -> 0x005b }
        if (r10 == 0) goto L_0x0044;
    L_0x0041:
        r10 = 0;
        r12.mTempConfigurationForSelectiveOrientation = r10;	 Catch:{ all -> 0x005b }
    L_0x0044:
        r10 = r12.mConfiguration;	 Catch:{ all -> 0x005b }
        if (r10 != 0) goto L_0x004f;
    L_0x0048:
        r10 = new android.content.res.Configuration;	 Catch:{ all -> 0x005b }
        r10.<init>();	 Catch:{ all -> 0x005b }
        r12.mConfiguration = r10;	 Catch:{ all -> 0x005b }
    L_0x004f:
        r10 = r12.mConfiguration;	 Catch:{ all -> 0x005b }
        r10 = r10.isOtherSeqNewer(r13);	 Catch:{ all -> 0x005b }
        if (r10 != 0) goto L_0x007d;
    L_0x0057:
        if (r14 != 0) goto L_0x007d;
    L_0x0059:
        monitor-exit(r11);	 Catch:{ all -> 0x005b }
        goto L_0x0023;
    L_0x005b:
        r10 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x005b }
        throw r10;
    L_0x005e:
        if (r15 != 0) goto L_0x0068;
    L_0x0060:
        r10 = 2;
        r13.orientation = r10;	 Catch:{ all -> 0x005b }
        r10 = 1;
        r12.mPrevScreenOrientationDefined = r10;	 Catch:{ all -> 0x005b }
        r6 = 1;
        goto L_0x0032;
    L_0x0068:
        r10 = r12.mPrevScreenOrientationDefined;	 Catch:{ all -> 0x005b }
        if (r10 == 0) goto L_0x0032;
    L_0x006c:
        r10 = 0;
        r12.mPrevScreenOrientationDefined = r10;	 Catch:{ all -> 0x005b }
        r10 = android.app.ActivityManagerNative.getDefault();	 Catch:{ RemoteException -> 0x00fe }
        r10 = r10.getConfiguration();	 Catch:{ RemoteException -> 0x00fe }
        r10 = r10.orientation;	 Catch:{ RemoteException -> 0x00fe }
        r13.orientation = r10;	 Catch:{ RemoteException -> 0x00fe }
    L_0x007b:
        r6 = 1;
        goto L_0x0032;
    L_0x007d:
        r10 = r12.mConfiguration;	 Catch:{ all -> 0x005b }
        r3 = r10.updateFrom(r13);	 Catch:{ all -> 0x005b }
        r10 = r12.mCurDefaultDisplayDpi;	 Catch:{ all -> 0x005b }
        r13 = r12.applyCompatConfiguration(r10);	 Catch:{ all -> 0x005b }
        monitor-exit(r11);	 Catch:{ all -> 0x005b }
        r10 = 0;
        r2 = r12.collectComponentCallbacks(r10, r13);
        r10 = 1001; // 0x3e9 float:1.403E-42 double:4.946E-321;
        r11 = 0;
        r12.sendMessage(r10, r11, r3);
        freeTextLayoutCachesIfNeeded(r3);
        if (r2 == 0) goto L_0x0023;
    L_0x009a:
        r0 = r2.size();
        r7 = 0;
    L_0x009f:
        if (r7 >= r0) goto L_0x0023;
    L_0x00a1:
        r10 = com.samsung.android.multiwindow.MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED;
        if (r10 == 0) goto L_0x00f4;
    L_0x00a5:
        r10 = r2.get(r7);
        r10 = r10 instanceof android.app.Activity;
        if (r10 == 0) goto L_0x00f4;
    L_0x00ad:
        r1 = r2.get(r7);
        r1 = (android.app.Activity) r1;
        r9 = r1.getActivityToken();
        r10 = r12.isFixedOrientationCascade(r9);
        if (r10 == 0) goto L_0x00f4;
    L_0x00bd:
        r8 = r12.getOverrideConfiguration(r9);
        if (r8 == 0) goto L_0x00f4;
    L_0x00c3:
        r4 = new android.content.res.Configuration;
        r4.<init>(r13);
        r10 = r8.orientation;
        if (r10 == 0) goto L_0x00d0;
    L_0x00cc:
        r10 = r8.orientation;
        r4.orientation = r10;
    L_0x00d0:
        r10 = r8.screenWidthDp;
        if (r10 == 0) goto L_0x00d8;
    L_0x00d4:
        r10 = r8.screenWidthDp;
        r4.screenWidthDp = r10;
    L_0x00d8:
        r10 = r8.screenHeightDp;
        if (r10 == 0) goto L_0x00e0;
    L_0x00dc:
        r10 = r8.screenHeightDp;
        r4.screenHeightDp = r10;
    L_0x00e0:
        r10 = r8.smallestScreenWidthDp;
        if (r10 == 0) goto L_0x00e8;
    L_0x00e4:
        r10 = r8.smallestScreenWidthDp;
        r4.smallestScreenWidthDp = r10;
    L_0x00e8:
        r10 = r2.get(r7);
        r10 = (android.content.ComponentCallbacks2) r10;
        performConfigurationChanged(r10, r4);
    L_0x00f1:
        r7 = r7 + 1;
        goto L_0x009f;
    L_0x00f4:
        r10 = r2.get(r7);
        r10 = (android.content.ComponentCallbacks2) r10;
        performConfigurationChanged(r10, r13);
        goto L_0x00f1;
    L_0x00fe:
        r10 = move-exception;
        goto L_0x007b;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.handleConfigurationChanged(android.content.res.Configuration, android.content.res.CompatibilityInfo, int, int, boolean):void");
    }

    final void handleConfigurationsChanged(UnRestrictedArrayList<Configuration> configs, CompatibilityInfo compat) {
        int i;
        boolean hasIM = hasInputMethodService();
        for (i = 0; i < configs.size(); i++) {
            synchronized (this.mResourcesManager) {
                if (this.mPendingConfigurations.get(i) != null) {
                    if (!((Configuration) this.mPendingConfigurations.get(i)).isOtherSeqNewer((Configuration) configs.get(i))) {
                        configs.set(i, this.mPendingConfigurations.get(i));
                        this.mCurDefaultDisplayDpis.set(i, Integer.valueOf(((Configuration) configs.get(i)).densityDpi));
                        updateDefaultDensity();
                    }
                    this.mPendingConfigurations.set(i, null);
                }
                if (configs.get(i) == null) {
                } else {
                    if (!hasIM) {
                        this.mResourcesManager.applyConfigurationToResourcesLocked((Configuration) configs.get(i), compat, false);
                    }
                    if (this.mConfigurations.get(i) == null) {
                        this.mConfigurations.set(i, new Configuration());
                    }
                    if (((Configuration) this.mConfigurations.get(i)).isOtherSeqNewer((Configuration) configs.get(i)) || compat != null) {
                        int configDiff = ((Configuration) this.mConfigurations.get(i)).diff((Configuration) configs.get(i));
                        ((Configuration) this.mConfigurations.get(i)).updateFrom((Configuration) configs.get(i));
                        configs.set(i, applyCompatConfiguration(i, ((Integer) this.mCurDefaultDisplayDpis.get(i)).intValue()));
                        ArrayList<ComponentCallbacks2> callbacks = collectComponentCallbacks(false, (Configuration) configs.get(i));
                        sendMessage(1001, null, configDiff);
                        freeTextLayoutCachesIfNeeded(configDiff);
                        if (callbacks != null) {
                            int N = callbacks.size();
                            for (int j = 0; j < N; j++) {
                                performConfigurationChanged((ComponentCallbacks2) callbacks.get(j), (Configuration) configs.get(i));
                            }
                        }
                    }
                }
            }
        }
        if (hasIM) {
            int NSVC = this.mServices.size();
            Configuration[] arrayType = new Configuration[4];
            Iterator i$ = configs.iterator();
            while (i$.hasNext()) {
                Configuration config = (Configuration) i$.next();
                if (config != null) {
                    arrayType[config.displayId] = config;
                }
            }
            for (i = 0; i < NSVC; i++) {
                if (this.mServices.valueAt(i) instanceof InputMethodService) {
                    ((InputMethodService) this.mServices.valueAt(i)).onConfiguratinChangedForAllDisplays(arrayType);
                }
            }
        }
    }

    static void freeTextLayoutCachesIfNeeded(int configDiff) {
        if (configDiff != 0) {
            if ((configDiff & 4) != 0) {
                Canvas.freeTextLayoutCaches();
            }
        }
    }

    final void handleActivityConfigurationChanged(ActivityConfigChangeData data) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(data.activityToken);
        if (r != null && r.activity != null) {
            Configuration mCompatConfiguration = this.mCompatConfiguration;
            r.tmpConfig.setTo(mCompatConfiguration);
            if (data.overrideConfig != null) {
                int nextOverrideOr = data.overrideConfig.orientation;
                int prevOverrideOr = r.overrideConfig != null ? r.overrideConfig.orientation : 0;
                r.overrideConfig = data.overrideConfig;
                if (!Configuration.EMPTY.equals(data.overrideConfig)) {
                    if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED && data.overrideConfig.isStackOverrideConfig()) {
                        r.tmpConfig.updateFromStackOverrideConfig(data.overrideConfig);
                    } else {
                        r.tmpConfig.updateFrom(data.overrideConfig);
                    }
                }
                if (!(!MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED || prevOverrideOr == nextOverrideOr || r.packageInfo == null || r.activity == null)) {
                    Resources topResources = getTopLevelResources(r.packageInfo.getResDir(), r.packageInfo.getSplitResDirs(), r.packageInfo.getOverlayDirs(), r.packageInfo.getApplicationInfo().sharedLibraryFiles, r.displayId, r.overrideConfig, r.packageInfo);
                    Resources curResources = r.activity.getResources();
                    if (!(topResources == null || curResources == topResources)) {
                        if (SAFE_DEBUG) {
                            Slog.i(TAG, "Replace res, r=" + r + ", curRes=" + curResources + ", newRes=" + topResources + ", overConfig=" + r.overrideConfig);
                        }
                        r.activity.setResources(topResources);
                        WindowManagerGlobal.getInstance().setResources(r.token, topResources);
                    }
                    if (nextOverrideOr == 0 && r.multiWindowStyle.isNormal()) {
                        synchronized (this.mResourcesManager) {
                            if (this.mPendingConfiguration != null) {
                                r.tmpConfig.updateFrom(this.mPendingConfiguration);
                                Slog.v(TAG, "applied pendingConfig, tmp=" + r.tmpConfig);
                            }
                        }
                    }
                }
            }
            performConfigurationChanged(r.activity, r.tmpConfig);
            freeTextLayoutCachesIfNeeded(r.activity.mCurrentConfig.diff(mCompatConfiguration));
            this.mSomeActivitiesChanged = true;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    final void handleProfilerControl(boolean r5, android.app.ProfilerInfo r6, int r7) {
        /*
        r4 = this;
        if (r5 == 0) goto L_0x005b;
    L_0x0002:
        r1 = r4.mProfiler;	 Catch:{ RuntimeException -> 0x001b }
        r1.setProfiler(r6);	 Catch:{ RuntimeException -> 0x001b }
        r1 = r4.mProfiler;	 Catch:{ RuntimeException -> 0x001b }
        r1.startProfiling();	 Catch:{ RuntimeException -> 0x001b }
        r1 = r6.profileFd;	 Catch:{ IOException -> 0x0012 }
        r1.close();	 Catch:{ IOException -> 0x0012 }
    L_0x0011:
        return;
    L_0x0012:
        r0 = move-exception;
        r1 = "ActivityThread";
        r2 = "Failure closing profile fd";
        android.util.Slog.w(r1, r2, r0);
        goto L_0x0011;
    L_0x001b:
        r0 = move-exception;
        r1 = "ActivityThread";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004b }
        r2.<init>();	 Catch:{ all -> 0x004b }
        r3 = "Profiling failed on path ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x004b }
        r3 = r6.profileFile;	 Catch:{ all -> 0x004b }
        r2 = r2.append(r3);	 Catch:{ all -> 0x004b }
        r3 = " -- can the process access this path?";
        r2 = r2.append(r3);	 Catch:{ all -> 0x004b }
        r2 = r2.toString();	 Catch:{ all -> 0x004b }
        android.util.Slog.w(r1, r2);	 Catch:{ all -> 0x004b }
        r1 = r6.profileFd;	 Catch:{ IOException -> 0x0042 }
        r1.close();	 Catch:{ IOException -> 0x0042 }
        goto L_0x0011;
    L_0x0042:
        r0 = move-exception;
        r1 = "ActivityThread";
        r2 = "Failure closing profile fd";
        android.util.Slog.w(r1, r2, r0);
        goto L_0x0011;
    L_0x004b:
        r1 = move-exception;
        r2 = r6.profileFd;	 Catch:{ IOException -> 0x0052 }
        r2.close();	 Catch:{ IOException -> 0x0052 }
    L_0x0051:
        throw r1;
    L_0x0052:
        r0 = move-exception;
        r2 = "ActivityThread";
        r3 = "Failure closing profile fd";
        android.util.Slog.w(r2, r3, r0);
        goto L_0x0051;
    L_0x005b:
        r1 = r4.mProfiler;
        r1.stopProfiling();
        goto L_0x0011;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.handleProfilerControl(boolean, android.app.ProfilerInfo, int):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static final void handleDumpHeap(boolean r4, android.app.ActivityThread.DumpHeapData r5) {
        /*
        if (r4 == 0) goto L_0x0065;
    L_0x0002:
        r1 = r5.path;	 Catch:{ IOException -> 0x0025 }
        r2 = r5.fd;	 Catch:{ IOException -> 0x0025 }
        r2 = r2.getFileDescriptor();	 Catch:{ IOException -> 0x0025 }
        android.os.Debug.dumpHprofData(r1, r2);	 Catch:{ IOException -> 0x0025 }
        r1 = r5.fd;	 Catch:{ IOException -> 0x001c }
        r1.close();	 Catch:{ IOException -> 0x001c }
    L_0x0012:
        r1 = android.app.ActivityManagerNative.getDefault();	 Catch:{ RemoteException -> 0x006f }
        r2 = r5.path;	 Catch:{ RemoteException -> 0x006f }
        r1.dumpHeapFinished(r2);	 Catch:{ RemoteException -> 0x006f }
    L_0x001b:
        return;
    L_0x001c:
        r0 = move-exception;
        r1 = "ActivityThread";
        r2 = "Failure closing profile fd";
        android.util.Slog.w(r1, r2, r0);
        goto L_0x0012;
    L_0x0025:
        r0 = move-exception;
        r1 = "ActivityThread";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0055 }
        r2.<init>();	 Catch:{ all -> 0x0055 }
        r3 = "Managed heap dump failed on path ";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0055 }
        r3 = r5.path;	 Catch:{ all -> 0x0055 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0055 }
        r3 = " -- can the process access this path?";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0055 }
        r2 = r2.toString();	 Catch:{ all -> 0x0055 }
        android.util.Slog.w(r1, r2);	 Catch:{ all -> 0x0055 }
        r1 = r5.fd;	 Catch:{ IOException -> 0x004c }
        r1.close();	 Catch:{ IOException -> 0x004c }
        goto L_0x0012;
    L_0x004c:
        r0 = move-exception;
        r1 = "ActivityThread";
        r2 = "Failure closing profile fd";
        android.util.Slog.w(r1, r2, r0);
        goto L_0x0012;
    L_0x0055:
        r1 = move-exception;
        r2 = r5.fd;	 Catch:{ IOException -> 0x005c }
        r2.close();	 Catch:{ IOException -> 0x005c }
    L_0x005b:
        throw r1;
    L_0x005c:
        r0 = move-exception;
        r2 = "ActivityThread";
        r3 = "Failure closing profile fd";
        android.util.Slog.w(r2, r3, r0);
        goto L_0x005b;
    L_0x0065:
        r1 = r5.fd;
        r1 = r1.getFileDescriptor();
        android.os.Debug.dumpNativeHeap(r1);
        goto L_0x0012;
    L_0x006f:
        r1 = move-exception;
        goto L_0x001b;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.handleDumpHeap(boolean, android.app.ActivityThread$DumpHeapData):void");
    }

    final void handleDispatchPackageBroadcast(int cmd, String[] packages) {
        boolean hasPkgInfo = false;
        if (packages != null) {
            synchronized (this.mResourcesManager) {
                for (int i = packages.length - 1; i >= 0; i--) {
                    if (!hasPkgInfo) {
                        WeakReference<LoadedApk> ref = (WeakReference) this.mPackages.get(packages[i]);
                        if (ref == null || ref.get() == null) {
                            ref = (WeakReference) this.mResourcePackages.get(packages[i]);
                            if (!(ref == null || ref.get() == null)) {
                                hasPkgInfo = true;
                            }
                        } else {
                            hasPkgInfo = true;
                        }
                    }
                    this.mPackages.remove(packages[i]);
                    this.mResourcePackages.remove(packages[i]);
                }
            }
        }
        ApplicationPackageManager.handlePackageBroadcast(cmd, packages, hasPkgInfo);
    }

    final void handleLowMemory() {
        ArrayList<ComponentCallbacks2> callbacks = collectComponentCallbacks(true, null);
        int N = callbacks.size();
        for (int i = 0; i < N; i++) {
            ((ComponentCallbacks2) callbacks.get(i)).onLowMemory();
        }
        if (Process.myUid() != 1000) {
            EventLog.writeEvent(SQLITE_MEM_RELEASED_EVENT_LOG_TAG, SQLiteDatabase.releaseMemory());
        }
        Canvas.freeCaches();
        Canvas.freeTextLayoutCaches();
        BinderInternal.forceGc("mem");
    }

    final void handleTrimMemory(int level) {
        ArrayList<ComponentCallbacks2> callbacks = collectComponentCallbacks(true, null);
        int N = callbacks.size();
        for (int i = 0; i < N; i++) {
            ((ComponentCallbacks2) callbacks.get(i)).onTrimMemory(level);
        }
        WindowManagerGlobal.getInstance().trimMemory(level);
    }

    private void setupGraphicsSupport(LoadedApk info, File cacheDir) {
        if (!Process.isIsolated()) {
            try {
                String[] packages = getPackageManager().getPackagesForUid(Process.myUid());
                if (packages != null && packages.length == 1) {
                    HardwareRenderer.setupDiskCache(cacheDir);
                    RenderScriptCacheDir.setupDiskCache(cacheDir);
                }
            } catch (RemoteException e) {
            }
        }
    }

    private void updateDefaultDensity() {
        if (this.mCurDefaultDisplayDpi != 0 && this.mCurDefaultDisplayDpi != DisplayMetrics.DENSITY_DEVICE && !this.mDensityCompatMode) {
            Slog.i(TAG, "Switching default density from " + DisplayMetrics.DENSITY_DEVICE + " to " + this.mCurDefaultDisplayDpi);
            DisplayMetrics.DENSITY_DEVICE = this.mCurDefaultDisplayDpi;
            Bitmap.setDefaultDensity(DisplayMetrics.DENSITY_DEVICE);
        }
    }

    private void handleBindApplication(AppBindData data) {
        this.mBoundApplication = data;
        this.mConfiguration = new Configuration(data.config);
        this.mCompatConfiguration = new Configuration(data.config);
        this.mProfiler = new Profiler();
        if (data.initProfilerInfo != null) {
            this.mProfiler.profileFile = data.initProfilerInfo.profileFile;
            this.mProfiler.profileFd = data.initProfilerInfo.profileFd;
            this.mProfiler.samplingInterval = data.initProfilerInfo.samplingInterval;
            this.mProfiler.autoStopProfiler = data.initProfilerInfo.autoStopProfiler;
        }
        Process.setArgV0(data.processName);
        DdmHandleAppName.setAppName(data.processName, UserHandle.myUserId());
        if (!(!data.persistent || ActivityManager.isHighEndGfx() || "com.android.phone".equals(data.processName))) {
            HardwareRenderer.disable(false);
        }
        if (this.mProfiler.profileFd != null) {
            this.mProfiler.startProfiling();
        }
        if (data.appInfo.targetSdkVersion <= 12) {
            AsyncTask.setDefaultExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        Message.updateCheckRecycle(data.appInfo.targetSdkVersion);
        TimeZone.setDefault(null);
        Locale.setDefault(data.config.locale);
        this.mResourcesManager.applyConfigurationToResourcesLocked(data.config, data.compatInfo);
        this.mCurDefaultDisplayDpi = data.config.densityDpi;
        applyCompatConfiguration(this.mCurDefaultDisplayDpi);
        data.info = getPackageInfoNoCheck(data.appInfo, data.compatInfo);
        if ((data.appInfo.flags & 8192) == 0) {
            this.mDensityCompatMode = true;
            Bitmap.setDefaultDensity(160);
        }
        updateDefaultDensity();
        Context appContext = ContextImpl.createAppContext(this, data.info, data.displayId);
        if (!Process.isIsolated()) {
            File cacheDir = appContext.getCacheDir();
            if (cacheDir != null) {
                System.setProperty("java.io.tmpdir", cacheDir.getAbsolutePath());
            } else {
                Log.v(TAG, "Unable to initialize \"java.io.tmpdir\" property due to missing cache directory");
            }
            File codeCacheDir = appContext.getCodeCacheDir();
            if (codeCacheDir != null) {
                setupGraphicsSupport(data.info, codeCacheDir);
            } else {
                Log.e(TAG, "Unable to setupGraphicsSupport due to missing code-cache directory");
            }
        }
        HardwareRenderer.setLibDir(data.info.getLibDir());
        DateFormat.set24HourTimePref("24".equals(this.mCoreSettings.getString("time_12_24")));
        View.mDebugViewAttributes = this.mCoreSettings.getInt(Global.DEBUG_VIEW_ATTRIBUTES, 0) != 0;
        if ((data.appInfo.flags & 129) != 0) {
            StrictMode.conditionallyEnableDebugLogging();
        }
        if (data.appInfo.targetSdkVersion > 9) {
            StrictMode.enableDeathOnNetwork();
        }
        NetworkSecurityPolicy.getInstance().setCleartextTrafficPermitted((data.appInfo.flags & 134217728) != 0);
        if (data.debugMode != 0) {
            Debug.changeDebugPort(8100);
            if (data.debugMode == 2) {
                Slog.w(TAG, "Application " + data.info.getPackageName() + " is waiting for the debugger on port 8100...");
                IActivityManager mgr = ActivityManagerNative.getDefault();
                try {
                    mgr.showWaitingForDebugger(this.mAppThread, true);
                } catch (RemoteException e) {
                }
                Debug.waitForDebugger();
                try {
                    mgr.showWaitingForDebugger(this.mAppThread, false);
                } catch (RemoteException e2) {
                }
            } else {
                Slog.w(TAG, "Application " + data.info.getPackageName() + " can be debugged on port 8100...");
            }
        }
        if (data.enableOpenGlTrace) {
            GLUtils.setTracingLevel(1);
        }
        Trace.setAppTracingAllowed((data.appInfo.flags & 2) != 0);
        IBinder b = ServiceManager.getService(Context.CONNECTIVITY_SERVICE);
        if (b != null) {
            try {
                Proxy.setHttpProxySystemProperty(IConnectivityManager.Stub.asInterface(b).getProxyForNetwork(null));
            } catch (RemoteException e3) {
            }
        }
        if (data.instrumentationName != null) {
            InstrumentationInfo ii = null;
            try {
                ii = appContext.getPackageManager().getInstrumentationInfo(data.instrumentationName, 0);
            } catch (NameNotFoundException e4) {
            }
            if (ii == null) {
                throw new RuntimeException("Unable to find instrumentation info for: " + data.instrumentationName);
            }
            this.mInstrumentationPackageName = ii.packageName;
            this.mInstrumentationAppDir = ii.sourceDir;
            this.mInstrumentationSplitAppDirs = ii.splitSourceDirs;
            this.mInstrumentationLibDir = ii.nativeLibraryDir;
            this.mInstrumentedAppDir = data.info.getAppDir();
            this.mInstrumentedSplitAppDirs = data.info.getSplitAppDirs();
            this.mInstrumentedLibDir = data.info.getLibDir();
            ApplicationInfo instrApp = new ApplicationInfo();
            instrApp.packageName = ii.packageName;
            instrApp.sourceDir = ii.sourceDir;
            instrApp.publicSourceDir = ii.publicSourceDir;
            instrApp.splitSourceDirs = ii.splitSourceDirs;
            instrApp.splitPublicSourceDirs = ii.splitPublicSourceDirs;
            instrApp.dataDir = ii.dataDir;
            instrApp.nativeLibraryDir = ii.nativeLibraryDir;
            ContextImpl instrContext = ContextImpl.createAppContext(this, getPackageInfo(instrApp, data.compatInfo, appContext.getClassLoader(), false, true, false));
            try {
                this.mInstrumentation = (Instrumentation) instrContext.getClassLoader().loadClass(data.instrumentationName.getClassName()).newInstance();
                this.mInstrumentation.init(this, instrContext, appContext, new ComponentName(ii.packageName, ii.name), data.instrumentationWatcher, data.instrumentationUiAutomationConnection);
                if (!(this.mProfiler.profileFile == null || ii.handleProfiling || this.mProfiler.profileFd != null)) {
                    this.mProfiler.handlingProfiling = true;
                    File file = new File(this.mProfiler.profileFile);
                    file.getParentFile().mkdirs();
                    Debug.startMethodTracing(file.toString(), 8388608);
                }
            } catch (Throwable e5) {
                throw new RuntimeException("Unable to instantiate instrumentation " + data.instrumentationName + ": " + e5.toString(), e5);
            }
        }
        this.mInstrumentation = new Instrumentation();
        if ((data.appInfo.flags & 1048576) != 0) {
            VMRuntime.getRuntime().clearGrowthLimit();
        } else {
            VMRuntime.getRuntime().clampGrowthLimit();
        }
        ThreadPolicy savedPolicy = StrictMode.allowThreadDiskWrites();
        Application app;
        try {
            app = data.info.makeApplication(data.restrictedBackupMode, null);
            this.mInitialApplication = app;
            if (!data.restrictedBackupMode) {
                List<ProviderInfo> providers = data.providers;
                if (providers != null) {
                    installContentProviders(app, providers);
                    this.mH.sendEmptyMessageDelayed(132, 10000);
                }
            }
            this.mInstrumentation.onCreate(data.instrumentationArgs);
            this.mInstrumentation.callApplicationOnCreate(app);
        } catch (Throwable e52) {
            if (!this.mInstrumentation.onException(app, e52)) {
                throw new RuntimeException("Unable to create application " + app.getClass().getName() + ": " + e52.toString(), e52);
            }
        } catch (Throwable e522) {
            throw new RuntimeException("Exception thrown in onCreate() of " + data.instrumentationName + ": " + e522.toString(), e522);
        } catch (Throwable th) {
            StrictMode.setThreadPolicy(savedPolicy);
        }
        StrictMode.setThreadPolicy(savedPolicy);
    }

    final void finishInstrumentation(int resultCode, Bundle results) {
        IActivityManager am = ActivityManagerNative.getDefault();
        if (this.mProfiler.profileFile != null && this.mProfiler.handlingProfiling && this.mProfiler.profileFd == null) {
            Debug.stopMethodTracing();
        }
        try {
            am.finishInstrumentation(this.mAppThread, resultCode, results);
        } catch (RemoteException e) {
        }
    }

    private void installContentProviders(Context context, List<ProviderInfo> providers) {
        ArrayList<ContentProviderHolder> results = new ArrayList();
        for (ProviderInfo cpi : providers) {
            ContentProviderHolder cph = installProvider(context, null, cpi, false, true, true);
            if (cph != null) {
                cph.noReleaseNeeded = true;
                results.add(cph);
            }
        }
        try {
            ActivityManagerNative.getDefault().publishContentProviders(getApplicationThread(), results);
        } catch (RemoteException e) {
        }
    }

    public final IContentProvider acquireProvider(Context c, String auth, int userId, boolean stable) {
        IContentProvider provider = acquireExistingProvider(c, auth, userId, stable);
        if (provider != null) {
            return provider;
        }
        ContentProviderHolder holder = null;
        try {
            holder = ActivityManagerNative.getDefault().getContentProvider(getApplicationThread(), auth, userId, stable);
        } catch (RemoteException e) {
        }
        if (holder == null) {
            Slog.e(TAG, "Failed to find provider info for " + auth);
            return null;
        }
        return installProvider(c, holder, holder.info, true, holder.noReleaseNeeded, stable).provider;
    }

    private final void incProviderRefLocked(ProviderRefCount prc, boolean stable) {
        if (stable) {
            prc.stableCount++;
            if (prc.stableCount == 1) {
                int unstableDelta;
                if (prc.removePending) {
                    unstableDelta = -1;
                    prc.removePending = false;
                    this.mH.removeMessages(131, prc);
                } else {
                    unstableDelta = 0;
                }
                try {
                    ActivityManagerNative.getDefault().refContentProvider(prc.holder.connection, 1, unstableDelta);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            return;
        }
        prc.unstableCount++;
        if (prc.unstableCount != 1) {
            return;
        }
        if (prc.removePending) {
            prc.removePending = false;
            this.mH.removeMessages(131, prc);
            return;
        }
        try {
            ActivityManagerNative.getDefault().refContentProvider(prc.holder.connection, 0, 1);
        } catch (RemoteException e2) {
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final android.content.IContentProvider acquireExistingProvider(android.content.Context r11, java.lang.String r12, int r13, boolean r14) {
        /*
        r10 = this;
        r5 = 0;
        r6 = r10.mProviderMap;
        monitor-enter(r6);
        r1 = new android.app.ActivityThread$ProviderKey;	 Catch:{ all -> 0x0060 }
        r1.<init>(r12, r13);	 Catch:{ all -> 0x0060 }
        r7 = r10.mProviderMap;	 Catch:{ all -> 0x0060 }
        r2 = r7.get(r1);	 Catch:{ all -> 0x0060 }
        r2 = (android.app.ActivityThread.ProviderClientRecord) r2;	 Catch:{ all -> 0x0060 }
        if (r2 != 0) goto L_0x0016;
    L_0x0013:
        monitor-exit(r6);	 Catch:{ all -> 0x0060 }
        r4 = r5;
    L_0x0015:
        return r4;
    L_0x0016:
        r4 = r2.mProvider;	 Catch:{ all -> 0x0060 }
        r0 = r4.asBinder();	 Catch:{ all -> 0x0060 }
        r7 = r0.isBinderAlive();	 Catch:{ all -> 0x0060 }
        if (r7 != 0) goto L_0x0051;
    L_0x0022:
        r7 = "ActivityThread";
        r8 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0060 }
        r8.<init>();	 Catch:{ all -> 0x0060 }
        r9 = "Acquiring provider ";
        r8 = r8.append(r9);	 Catch:{ all -> 0x0060 }
        r8 = r8.append(r12);	 Catch:{ all -> 0x0060 }
        r9 = " for user ";
        r8 = r8.append(r9);	 Catch:{ all -> 0x0060 }
        r8 = r8.append(r13);	 Catch:{ all -> 0x0060 }
        r9 = ": existing object's process dead";
        r8 = r8.append(r9);	 Catch:{ all -> 0x0060 }
        r8 = r8.toString();	 Catch:{ all -> 0x0060 }
        android.util.Log.i(r7, r8);	 Catch:{ all -> 0x0060 }
        r7 = 1;
        r10.handleUnstableProviderDiedLocked(r0, r7);	 Catch:{ all -> 0x0060 }
        monitor-exit(r6);	 Catch:{ all -> 0x0060 }
        r4 = r5;
        goto L_0x0015;
    L_0x0051:
        r5 = r10.mProviderRefCountMap;	 Catch:{ all -> 0x0060 }
        r3 = r5.get(r0);	 Catch:{ all -> 0x0060 }
        r3 = (android.app.ActivityThread.ProviderRefCount) r3;	 Catch:{ all -> 0x0060 }
        if (r3 == 0) goto L_0x005e;
    L_0x005b:
        r10.incProviderRefLocked(r3, r14);	 Catch:{ all -> 0x0060 }
    L_0x005e:
        monitor-exit(r6);	 Catch:{ all -> 0x0060 }
        goto L_0x0015;
    L_0x0060:
        r5 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0060 }
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.acquireExistingProvider(android.content.Context, java.lang.String, int, boolean):android.content.IContentProvider");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final boolean releaseProvider(android.content.IContentProvider r11, boolean r12) {
        /*
        r10 = this;
        r4 = 1;
        r5 = 0;
        if (r11 != 0) goto L_0x0005;
    L_0x0004:
        return r5;
    L_0x0005:
        r0 = r11.asBinder();
        r6 = r10.mProviderMap;
        monitor-enter(r6);
        r7 = r10.mProviderRefCountMap;	 Catch:{ all -> 0x0018 }
        r3 = r7.get(r0);	 Catch:{ all -> 0x0018 }
        r3 = (android.app.ActivityThread.ProviderRefCount) r3;	 Catch:{ all -> 0x0018 }
        if (r3 != 0) goto L_0x001b;
    L_0x0016:
        monitor-exit(r6);	 Catch:{ all -> 0x0018 }
        goto L_0x0004;
    L_0x0018:
        r4 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x0018 }
        throw r4;
    L_0x001b:
        r1 = 0;
        if (r12 == 0) goto L_0x005d;
    L_0x001e:
        r7 = r3.stableCount;	 Catch:{ all -> 0x0018 }
        if (r7 != 0) goto L_0x0024;
    L_0x0022:
        monitor-exit(r6);	 Catch:{ all -> 0x0018 }
        goto L_0x0004;
    L_0x0024:
        r7 = r3.stableCount;	 Catch:{ all -> 0x0018 }
        r7 = r7 + -1;
        r3.stableCount = r7;	 Catch:{ all -> 0x0018 }
        r7 = r3.stableCount;	 Catch:{ all -> 0x0018 }
        if (r7 != 0) goto L_0x0042;
    L_0x002e:
        r7 = r3.unstableCount;	 Catch:{ all -> 0x0018 }
        if (r7 != 0) goto L_0x005b;
    L_0x0032:
        r1 = r4;
    L_0x0033:
        r7 = android.app.ActivityManagerNative.getDefault();	 Catch:{ RemoteException -> 0x00a5 }
        r8 = r3.holder;	 Catch:{ RemoteException -> 0x00a5 }
        r8 = r8.connection;	 Catch:{ RemoteException -> 0x00a5 }
        r9 = -1;
        if (r1 == 0) goto L_0x003f;
    L_0x003e:
        r5 = r4;
    L_0x003f:
        r7.refContentProvider(r8, r9, r5);	 Catch:{ RemoteException -> 0x00a5 }
    L_0x0042:
        if (r1 == 0) goto L_0x0058;
    L_0x0044:
        r5 = r3.removePending;	 Catch:{ all -> 0x0018 }
        if (r5 != 0) goto L_0x0086;
    L_0x0048:
        r5 = 1;
        r3.removePending = r5;	 Catch:{ all -> 0x0018 }
        r5 = r10.mH;	 Catch:{ all -> 0x0018 }
        r7 = 131; // 0x83 float:1.84E-43 double:6.47E-322;
        r2 = r5.obtainMessage(r7, r3);	 Catch:{ all -> 0x0018 }
        r5 = r10.mH;	 Catch:{ all -> 0x0018 }
        r5.sendMessage(r2);	 Catch:{ all -> 0x0018 }
    L_0x0058:
        monitor-exit(r6);	 Catch:{ all -> 0x0018 }
        r5 = r4;
        goto L_0x0004;
    L_0x005b:
        r1 = r5;
        goto L_0x0033;
    L_0x005d:
        r7 = r3.unstableCount;	 Catch:{ all -> 0x0018 }
        if (r7 != 0) goto L_0x0063;
    L_0x0061:
        monitor-exit(r6);	 Catch:{ all -> 0x0018 }
        goto L_0x0004;
    L_0x0063:
        r7 = r3.unstableCount;	 Catch:{ all -> 0x0018 }
        r7 = r7 + -1;
        r3.unstableCount = r7;	 Catch:{ all -> 0x0018 }
        r7 = r3.unstableCount;	 Catch:{ all -> 0x0018 }
        if (r7 != 0) goto L_0x0042;
    L_0x006d:
        r7 = r3.stableCount;	 Catch:{ all -> 0x0018 }
        if (r7 != 0) goto L_0x0084;
    L_0x0071:
        r1 = r4;
    L_0x0072:
        if (r1 != 0) goto L_0x0042;
    L_0x0074:
        r5 = android.app.ActivityManagerNative.getDefault();	 Catch:{ RemoteException -> 0x0082 }
        r7 = r3.holder;	 Catch:{ RemoteException -> 0x0082 }
        r7 = r7.connection;	 Catch:{ RemoteException -> 0x0082 }
        r8 = 0;
        r9 = -1;
        r5.refContentProvider(r7, r8, r9);	 Catch:{ RemoteException -> 0x0082 }
        goto L_0x0042;
    L_0x0082:
        r5 = move-exception;
        goto L_0x0042;
    L_0x0084:
        r1 = r5;
        goto L_0x0072;
    L_0x0086:
        r5 = "ActivityThread";
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0018 }
        r7.<init>();	 Catch:{ all -> 0x0018 }
        r8 = "Duplicate remove pending of provider ";
        r7 = r7.append(r8);	 Catch:{ all -> 0x0018 }
        r8 = r3.holder;	 Catch:{ all -> 0x0018 }
        r8 = r8.info;	 Catch:{ all -> 0x0018 }
        r8 = r8.name;	 Catch:{ all -> 0x0018 }
        r7 = r7.append(r8);	 Catch:{ all -> 0x0018 }
        r7 = r7.toString();	 Catch:{ all -> 0x0018 }
        android.util.Slog.w(r5, r7);	 Catch:{ all -> 0x0018 }
        goto L_0x0058;
    L_0x00a5:
        r5 = move-exception;
        goto L_0x0042;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.releaseProvider(android.content.IContentProvider, boolean):boolean");
    }

    final void completeRemoveProvider(ProviderRefCount prc) {
        synchronized (this.mProviderMap) {
            if (prc.removePending) {
                prc.removePending = false;
                IBinder jBinder = prc.holder.provider.asBinder();
                if (((ProviderRefCount) this.mProviderRefCountMap.get(jBinder)) == prc) {
                    this.mProviderRefCountMap.remove(jBinder);
                }
                for (int i = this.mProviderMap.size() - 1; i >= 0; i--) {
                    if (((ProviderClientRecord) this.mProviderMap.valueAt(i)).mProvider.asBinder() == jBinder) {
                        this.mProviderMap.removeAt(i);
                    }
                }
                try {
                    ActivityManagerNative.getDefault().removeContentProvider(prc.holder.connection, false);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
        }
    }

    final void handleUnstableProviderDied(IBinder provider, boolean fromClient) {
        synchronized (this.mProviderMap) {
            handleUnstableProviderDiedLocked(provider, fromClient);
        }
    }

    final void handleUnstableProviderDiedLocked(IBinder provider, boolean fromClient) {
        ProviderRefCount prc = (ProviderRefCount) this.mProviderRefCountMap.get(provider);
        if (prc != null) {
            this.mProviderRefCountMap.remove(provider);
            for (int i = this.mProviderMap.size() - 1; i >= 0; i--) {
                ProviderClientRecord pr = (ProviderClientRecord) this.mProviderMap.valueAt(i);
                if (pr != null && pr.mProvider.asBinder() == provider) {
                    Slog.i(TAG, "Removing dead content provider:" + pr.mProvider.toString());
                    this.mProviderMap.removeAt(i);
                }
            }
            if (fromClient) {
                try {
                    ActivityManagerNative.getDefault().unstableProviderDied(prc.holder.connection);
                } catch (RemoteException e) {
                }
            }
        }
    }

    final void appNotRespondingViaProvider(IBinder provider) {
        synchronized (this.mProviderMap) {
            ProviderRefCount prc = (ProviderRefCount) this.mProviderRefCountMap.get(provider);
            if (prc != null) {
                try {
                    ActivityManagerNative.getDefault().appNotRespondingViaProvider(prc.holder.connection);
                } catch (RemoteException e) {
                }
            }
        }
    }

    private ProviderClientRecord installProviderAuthoritiesLocked(IContentProvider provider, ContentProvider localProvider, ContentProviderHolder holder) {
        String[] auths = holder.info.authority.split(";");
        int userId = UserHandle.getUserId(holder.info.applicationInfo.uid);
        ProviderClientRecord pcr = new ProviderClientRecord(auths, provider, localProvider, holder);
        for (String auth : auths) {
            ProviderKey key = new ProviderKey(auth, userId);
            if (((ProviderClientRecord) this.mProviderMap.get(key)) != null) {
                Slog.w(TAG, "Content provider " + pcr.mHolder.info.name + " already published as " + auth);
            } else {
                this.mProviderMap.put(key, pcr);
            }
        }
        return pcr;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private android.app.IActivityManager.ContentProviderHolder installProvider(android.content.Context r19, android.app.IActivityManager.ContentProviderHolder r20, android.content.pm.ProviderInfo r21, boolean r22, boolean r23, boolean r24) {
        /*
        r18 = this;
        r10 = 0;
        if (r20 == 0) goto L_0x0009;
    L_0x0003:
        r0 = r20;
        r15 = r0.provider;
        if (r15 != 0) goto L_0x0172;
    L_0x0009:
        if (r22 == 0) goto L_0x0039;
    L_0x000b:
        r15 = "ActivityThread";
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = "Loading provider ";
        r16 = r16.append(r17);
        r0 = r21;
        r0 = r0.authority;
        r17 = r0;
        r16 = r16.append(r17);
        r17 = ": ";
        r16 = r16.append(r17);
        r0 = r21;
        r0 = r0.name;
        r17 = r0;
        r16 = r16.append(r17);
        r16 = r16.toString();
        android.util.Slog.d(r15, r16);
    L_0x0039:
        r3 = 0;
        r0 = r21;
        r2 = r0.applicationInfo;
        r15 = r19.getPackageName();
        r0 = r2.packageName;
        r16 = r0;
        r15 = r15.equals(r16);
        if (r15 == 0) goto L_0x007e;
    L_0x004c:
        r3 = r19;
    L_0x004e:
        if (r3 != 0) goto L_0x00a8;
    L_0x0050:
        r15 = "ActivityThread";
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = "Unable to get context for package ";
        r16 = r16.append(r17);
        r0 = r2.packageName;
        r17 = r0;
        r16 = r16.append(r17);
        r17 = " while loading content provider ";
        r16 = r16.append(r17);
        r0 = r21;
        r0 = r0.name;
        r17 = r0;
        r16 = r16.append(r17);
        r16 = r16.toString();
        android.util.Slog.w(r15, r16);
        r14 = 0;
    L_0x007d:
        return r14;
    L_0x007e:
        r0 = r18;
        r15 = r0.mInitialApplication;
        if (r15 == 0) goto L_0x009b;
    L_0x0084:
        r0 = r18;
        r15 = r0.mInitialApplication;
        r15 = r15.getPackageName();
        r0 = r2.packageName;
        r16 = r0;
        r15 = r15.equals(r16);
        if (r15 == 0) goto L_0x009b;
    L_0x0096:
        r0 = r18;
        r3 = r0.mInitialApplication;
        goto L_0x004e;
    L_0x009b:
        r15 = r2.packageName;	 Catch:{ NameNotFoundException -> 0x0206 }
        r16 = 1;
        r0 = r19;
        r1 = r16;
        r3 = r0.createPackageContext(r15, r1);	 Catch:{ NameNotFoundException -> 0x0206 }
        goto L_0x004e;
    L_0x00a8:
        r4 = r3.getClassLoader();	 Catch:{ Exception -> 0x0131 }
        r0 = r21;
        r15 = r0.name;	 Catch:{ Exception -> 0x0131 }
        r15 = r4.loadClass(r15);	 Catch:{ Exception -> 0x0131 }
        r15 = r15.newInstance();	 Catch:{ Exception -> 0x0131 }
        r0 = r15;
        r0 = (android.content.ContentProvider) r0;	 Catch:{ Exception -> 0x0131 }
        r10 = r0;
        r13 = r10.getIContentProvider();	 Catch:{ Exception -> 0x0131 }
        if (r13 != 0) goto L_0x00f8;
    L_0x00c2:
        r15 = "ActivityThread";
        r16 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0131 }
        r16.<init>();	 Catch:{ Exception -> 0x0131 }
        r17 = "Failed to instantiate class ";
        r16 = r16.append(r17);	 Catch:{ Exception -> 0x0131 }
        r0 = r21;
        r0 = r0.name;	 Catch:{ Exception -> 0x0131 }
        r17 = r0;
        r16 = r16.append(r17);	 Catch:{ Exception -> 0x0131 }
        r17 = " from sourceDir ";
        r16 = r16.append(r17);	 Catch:{ Exception -> 0x0131 }
        r0 = r21;
        r0 = r0.applicationInfo;	 Catch:{ Exception -> 0x0131 }
        r17 = r0;
        r0 = r17;
        r0 = r0.sourceDir;	 Catch:{ Exception -> 0x0131 }
        r17 = r0;
        r16 = r16.append(r17);	 Catch:{ Exception -> 0x0131 }
        r16 = r16.toString();	 Catch:{ Exception -> 0x0131 }
        android.util.Slog.e(r15, r16);	 Catch:{ Exception -> 0x0131 }
        r14 = 0;
        goto L_0x007d;
    L_0x00f8:
        r0 = r21;
        r10.attachInfo(r3, r0);	 Catch:{ Exception -> 0x0131 }
    L_0x00fd:
        r0 = r18;
        r0 = r0.mProviderMap;
        r16 = r0;
        monitor-enter(r16);
        r9 = r13.asBinder();	 Catch:{ all -> 0x012e }
        if (r10 == 0) goto L_0x019a;
    L_0x010a:
        r6 = new android.content.ComponentName;	 Catch:{ all -> 0x012e }
        r0 = r21;
        r15 = r0.packageName;	 Catch:{ all -> 0x012e }
        r0 = r21;
        r0 = r0.name;	 Catch:{ all -> 0x012e }
        r17 = r0;
        r0 = r17;
        r6.<init>(r15, r0);	 Catch:{ all -> 0x012e }
        r0 = r18;
        r15 = r0.mLocalProvidersByName;	 Catch:{ all -> 0x012e }
        r11 = r15.get(r6);	 Catch:{ all -> 0x012e }
        r11 = (android.app.ActivityThread.ProviderClientRecord) r11;	 Catch:{ all -> 0x012e }
        if (r11 == 0) goto L_0x0177;
    L_0x0127:
        r13 = r11.mProvider;	 Catch:{ all -> 0x012e }
    L_0x0129:
        r14 = r11.mHolder;	 Catch:{ all -> 0x012e }
    L_0x012b:
        monitor-exit(r16);	 Catch:{ all -> 0x012e }
        goto L_0x007d;
    L_0x012e:
        r15 = move-exception;
    L_0x012f:
        monitor-exit(r16);	 Catch:{ all -> 0x012e }
        throw r15;
    L_0x0131:
        r7 = move-exception;
        r0 = r18;
        r15 = r0.mInstrumentation;
        r16 = 0;
        r0 = r16;
        r15 = r15.onException(r0, r7);
        if (r15 != 0) goto L_0x016f;
    L_0x0140:
        r15 = new java.lang.RuntimeException;
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = "Unable to get provider ";
        r16 = r16.append(r17);
        r0 = r21;
        r0 = r0.name;
        r17 = r0;
        r16 = r16.append(r17);
        r17 = ": ";
        r16 = r16.append(r17);
        r17 = r7.toString();
        r16 = r16.append(r17);
        r16 = r16.toString();
        r0 = r16;
        r15.<init>(r0, r7);
        throw r15;
    L_0x016f:
        r14 = 0;
        goto L_0x007d;
    L_0x0172:
        r0 = r20;
        r13 = r0.provider;
        goto L_0x00fd;
    L_0x0177:
        r8 = new android.app.IActivityManager$ContentProviderHolder;	 Catch:{ all -> 0x012e }
        r0 = r21;
        r8.<init>(r0);	 Catch:{ all -> 0x012e }
        r8.provider = r13;	 Catch:{ all -> 0x01ff }
        r15 = 1;
        r8.noReleaseNeeded = r15;	 Catch:{ all -> 0x01ff }
        r0 = r18;
        r11 = r0.installProviderAuthoritiesLocked(r13, r10, r8);	 Catch:{ all -> 0x01ff }
        r0 = r18;
        r15 = r0.mLocalProviders;	 Catch:{ all -> 0x01ff }
        r15.put(r9, r11);	 Catch:{ all -> 0x01ff }
        r0 = r18;
        r15 = r0.mLocalProvidersByName;	 Catch:{ all -> 0x01ff }
        r15.put(r6, r11);	 Catch:{ all -> 0x01ff }
        r20 = r8;
        goto L_0x0129;
    L_0x019a:
        r0 = r18;
        r15 = r0.mProviderRefCountMap;	 Catch:{ all -> 0x012e }
        r12 = r15.get(r9);	 Catch:{ all -> 0x012e }
        r12 = (android.app.ActivityThread.ProviderRefCount) r12;	 Catch:{ all -> 0x012e }
        if (r12 == 0) goto L_0x01c4;
    L_0x01a6:
        if (r23 != 0) goto L_0x01c0;
    L_0x01a8:
        r0 = r18;
        r1 = r24;
        r0.incProviderRefLocked(r12, r1);	 Catch:{ all -> 0x012e }
        r15 = android.app.ActivityManagerNative.getDefault();	 Catch:{ RemoteException -> 0x0204 }
        r0 = r20;
        r0 = r0.connection;	 Catch:{ RemoteException -> 0x0204 }
        r17 = r0;
        r0 = r17;
        r1 = r24;
        r15.removeContentProvider(r0, r1);	 Catch:{ RemoteException -> 0x0204 }
    L_0x01c0:
        r14 = r12.holder;	 Catch:{ all -> 0x012e }
        goto L_0x012b;
    L_0x01c4:
        r0 = r18;
        r1 = r20;
        r5 = r0.installProviderAuthoritiesLocked(r13, r10, r1);	 Catch:{ all -> 0x012e }
        if (r23 == 0) goto L_0x01e3;
    L_0x01ce:
        r12 = new android.app.ActivityThread$ProviderRefCount;	 Catch:{ all -> 0x012e }
        r15 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r17 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r0 = r20;
        r1 = r17;
        r12.<init>(r0, r5, r15, r1);	 Catch:{ all -> 0x012e }
    L_0x01db:
        r0 = r18;
        r15 = r0.mProviderRefCountMap;	 Catch:{ all -> 0x012e }
        r15.put(r9, r12);	 Catch:{ all -> 0x012e }
        goto L_0x01c0;
    L_0x01e3:
        if (r24 == 0) goto L_0x01f2;
    L_0x01e5:
        r12 = new android.app.ActivityThread$ProviderRefCount;	 Catch:{ all -> 0x012e }
        r15 = 1;
        r17 = 0;
        r0 = r20;
        r1 = r17;
        r12.<init>(r0, r5, r15, r1);	 Catch:{ all -> 0x012e }
    L_0x01f1:
        goto L_0x01db;
    L_0x01f2:
        r12 = new android.app.ActivityThread$ProviderRefCount;	 Catch:{ all -> 0x012e }
        r15 = 0;
        r17 = 1;
        r0 = r20;
        r1 = r17;
        r12.<init>(r0, r5, r15, r1);	 Catch:{ all -> 0x012e }
        goto L_0x01f1;
    L_0x01ff:
        r15 = move-exception;
        r20 = r8;
        goto L_0x012f;
    L_0x0204:
        r15 = move-exception;
        goto L_0x01c0;
    L_0x0206:
        r15 = move-exception;
        goto L_0x004e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.installProvider(android.content.Context, android.app.IActivityManager$ContentProviderHolder, android.content.pm.ProviderInfo, boolean, boolean, boolean):android.app.IActivityManager$ContentProviderHolder");
    }

    private void attach(boolean system) {
        sCurrentActivityThread = this;
        this.mSystemThread = system;
        if (system) {
            DdmHandleAppName.setAppName("system_process", UserHandle.myUserId());
            try {
                this.mInstrumentation = new Instrumentation();
                this.mInitialApplication = ContextImpl.createAppContext(this, getSystemContext().mPackageInfo).mPackageInfo.makeApplication(true, null);
                this.mInitialApplication.onCreate();
            } catch (Exception e) {
                throw new RuntimeException("Unable to instantiate Application():" + e.toString(), e);
            }
        }
        ViewRootImpl.addFirstDrawHandler(new Runnable() {
            public void run() {
                ActivityThread.this.ensureJitEnabled();
            }
        });
        DdmHandleAppName.setAppName("<pre-initialized>", UserHandle.myUserId());
        RuntimeInit.setApplicationObject(this.mAppThread.asBinder());
        final IActivityManager mgr = ActivityManagerNative.getDefault();
        try {
            mgr.attachApplication(this.mAppThread);
        } catch (RemoteException e2) {
        }
        BinderInternal.addGcWatcher(new Runnable() {
            public void run() {
                if (ActivityThread.this.mSomeActivitiesChanged) {
                    Runtime runtime = Runtime.getRuntime();
                    if (runtime.totalMemory() - runtime.freeMemory() > (3 * runtime.maxMemory()) / 4) {
                        ActivityThread.this.mSomeActivitiesChanged = false;
                        try {
                            mgr.releaseSomeActivities(ActivityThread.this.mAppThread);
                        } catch (RemoteException e) {
                        }
                    }
                }
            }
        });
        DropBox.setReporter(new DropBoxReporter());
        ViewRootImpl.addConfigCallback(new ComponentCallbacks2() {
            public void onConfigurationChanged(Configuration newConfig) {
                synchronized (ActivityThread.this.mResourcesManager) {
                    if (ActivityThread.this.mResourcesManager.applyConfigurationToResourcesLocked(newConfig, null) && (ActivityThread.this.mPendingConfiguration == null || ActivityThread.this.mPendingConfiguration.isOtherSeqNewer(newConfig))) {
                        ActivityThread.this.mPendingConfiguration = newConfig;
                        ActivityThread.this.sendMessage(118, newConfig);
                    }
                }
            }

            public void onLowMemory() {
            }

            public void onTrimMemory(int level) {
            }
        });
    }

    public static ActivityThread systemMain() {
        if (ActivityManager.isHighEndGfx()) {
            HardwareRenderer.enableForegroundTrimming();
        } else {
            HardwareRenderer.disable(true);
        }
        ActivityThread thread = new ActivityThread();
        thread.attach(true);
        return thread;
    }

    public final void installSystemProviders(List<ProviderInfo> providers) {
        if (providers != null) {
            installContentProviders(this.mInitialApplication, providers);
        }
    }

    public int getIntCoreSetting(String key, int defaultValue) {
        synchronized (this.mResourcesManager) {
            if (this.mCoreSettings != null) {
                defaultValue = this.mCoreSettings.getInt(key, defaultValue);
            }
        }
        return defaultValue;
    }

    public ArrayMap<String, Integer> getCSCAppStringMap() {
        synchronized (sCSCSync) {
            if (this.mCSCStringMap == null) {
                parseCSCAppResource();
            }
        }
        return this.mCSCStringMap;
    }

    public ArrayMap<String, Integer> getCSCAppIconMap() {
        synchronized (sCSCSync) {
            if (this.mCSCIconMap == null) {
                parseCSCAppResource();
            }
        }
        return this.mCSCIconMap;
    }

    private void parseCSCAppResource() {
        NullPointerException e;
        NumberFormatException e2;
        XmlPullParserException e3;
        IOException e4;
        Exception e5;
        Throwable th;
        FileInputStream stream = null;
        try {
            this.mCSCIconMap = new ArrayMap();
            this.mCSCStringMap = new ArrayMap();
            File file = new File("/system/csc/appresource/CSCAppResource.xml");
            if (file.exists()) {
                FileInputStream stream2 = new FileInputStream(file);
                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setInput(stream2, null);
                    this.mCSCIconMap.clear();
                    this.mCSCStringMap.clear();
                    int type;
                    do {
                        type = parser.next();
                        if (type == 2 && parser.getName().compareTo("item") == 0) {
                            String strName = parser.getAttributeValue(null, "name");
                            String strIconId = parser.getAttributeValue(null, "iconid");
                            String strStringId = parser.getAttributeValue(null, "stringid");
                            if (!(strName == null || (strIconId == null && strStringId == null))) {
                                if (strIconId != null) {
                                    this.mCSCIconMap.put(strName, Integer.valueOf(Integer.parseInt(strIconId, 16)));
                                }
                                if (strStringId != null) {
                                    this.mCSCStringMap.put(strName, Integer.valueOf(Integer.parseInt(strStringId, 16)));
                                }
                            }
                        }
                    } while (type != 1);
                    stream = stream2;
                } catch (NullPointerException e6) {
                    e = e6;
                    stream = stream2;
                } catch (NumberFormatException e7) {
                    e2 = e7;
                    stream = stream2;
                } catch (XmlPullParserException e8) {
                    e3 = e8;
                    stream = stream2;
                } catch (IOException e9) {
                    e4 = e9;
                    stream = stream2;
                } catch (Exception e10) {
                    e5 = e10;
                    stream = stream2;
                } catch (Throwable th2) {
                    th = th2;
                    stream = stream2;
                }
            }
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e11) {
                }
            }
        } catch (NullPointerException e12) {
            e = e12;
            try {
                Log.w(TAG, "parseCSCAppResource: Got exception:", e);
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e13) {
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e14) {
                    }
                }
                throw th;
            }
        } catch (NumberFormatException e15) {
            e2 = e15;
            Log.w(TAG, "parseCSCAppResource: Got exception:", e2);
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e16) {
                }
            }
        } catch (XmlPullParserException e17) {
            e3 = e17;
            Log.w(TAG, "parseCSCAppResource: Got exception:", e3);
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e18) {
                }
            }
        } catch (IOException e19) {
            e4 = e19;
            Log.w(TAG, "parseCSCAppResource: Got exception:", e4);
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e20) {
                }
            }
        } catch (Exception e21) {
            e5 = e21;
            Log.w(TAG, "parseCSCAppResource: Got exception:", e5);
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e22) {
                }
            }
        }
    }

    public HashMap<String, String> getThemeAppIconMap() {
        synchronized (sThemeSync) {
            if (this.mThemeAppIconMap == null) {
                getThemeResourceFromMappingTable();
            }
        }
        return this.mThemeAppIconMap;
    }

    private void getThemeResourceFromMappingTable() {
        String TAG_THEME_APP = "ThemeApp";
        String TAG_ATTR_CLASSNAME = BaseColumns.COLUMN_CLASS_NAME;
        String TAG_ATTR_ICONID = "iconId";
        try {
            XmlResourceParser parser = getSystemContext().getResources().getXml(17891353);
            this.mThemeAppIconMap = new HashMap();
            this.mThemeAppIconMap.clear();
            if (parser != null) {
                int depth = parser.getDepth();
                while (true) {
                    int type = parser.next();
                    if ((type == 3 && parser.getDepth() <= depth) || type == 1) {
                        return;
                    }
                    if (type == 2) {
                        String className = null;
                        String iconId = null;
                        if (TAG_THEME_APP.equals(parser.getName())) {
                            int size = parser.getAttributeCount();
                            for (int i = 0; i < size; i++) {
                                String attrName = parser.getAttributeName(i);
                                String attrValue = parser.getAttributeValue(i);
                                if (attrName != null && attrName.equals(TAG_ATTR_CLASSNAME)) {
                                    className = attrValue;
                                }
                                if (attrName != null && attrName.equals(TAG_ATTR_ICONID)) {
                                    iconId = attrValue;
                                }
                            }
                            this.mThemeAppIconMap.put(className, iconId);
                        }
                    }
                }
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Exception during parsing theme app list XmlPullParserException" + e);
            e.printStackTrace();
        } catch (Exception e2) {
            Log.e(TAG, "Exception during parsing theme app list" + e2);
            e2.printStackTrace();
        }
    }

    private static void niceUp() {
        try {
            Process.setThreadPriority(-11);
        } catch (Exception e) {
            Slog.e(TAG, "Nice UP", e);
        }
    }

    private static void niceDown() {
        try {
            Process.setThreadPriority(0);
        } catch (Exception e) {
            Slog.e(TAG, "Nice Down", e);
        }
    }

    public static void main(String[] args) {
        Trace.traceBegin(64, "ActivityThreadMain");
        SamplingProfilerIntegration.start();
        CloseGuard.setEnabled(false);
        Environment.initForCurrentUser();
        EventLogger.setReporter(new EventLoggingReporter());
        AndroidKeyStoreProvider.install();
        try {
            if (WifiEnterpriseConfig.ENGINE_ENABLE.equals(SystemProperties.get("ro.config.tima", "0")) && "3.0".equals(SystemProperties.get("ro.config.timaversion", "0"))) {
                Security.addProvider((Provider) Class.forName("com.sec.tima.TimaKeyStoreProvider").newInstance());
                Log.d(TAG, "Added TimaKeyStore provider");
            }
        } catch (Exception e) {
            Log.d(TAG, "Unable to add TimaKeyStore provider");
            e.printStackTrace();
        }
        TrustedCertificateStore.setDefaultUserDirectory(Environment.getUserConfigDirectory(UserHandle.myUserId()));
        Process.setArgV0("<pre-initialized>");
        Looper.prepareMainLooper();
        ActivityThread thread = new ActivityThread();
        thread.attach(false);
        if (sMainThreadHandler == null) {
            sMainThreadHandler = thread.getHandler();
        }
        Looper.myLooper().setSlowLapTimeEvent(LOOPER_SLOW_LOOP_WARNING_TIMEOUT);
        try {
            Looper.myLooper().setPackageName(thread.mBoundApplication.appInfo.packageName);
        } catch (NullPointerException e2) {
        }
        Trace.traceEnd(64);
        Looper.loop();
        throw new RuntimeException("Main thread loop unexpectedly exited");
    }

    public MultiWindowStyle getAppMultiWindowStyle(IBinder token) {
        synchronized (this.mActivities) {
            if (this.mActivities.size() > 0) {
                ActivityClientRecord r;
                MultiWindowStyle multiWindowStyle;
                if (token != null) {
                    r = (ActivityClientRecord) this.mActivities.get(token);
                    if (r != null) {
                        if (r.parent != null) {
                            multiWindowStyle = r.parent.getMultiWindowStyle();
                            return multiWindowStyle;
                        }
                        multiWindowStyle = r.multiWindowStyle;
                        return multiWindowStyle;
                    }
                }
                try {
                    for (int i = this.mActivities.size() - 1; i >= 0; i--) {
                        r = (ActivityClientRecord) this.mActivities.valueAt(i);
                        if (r != null) {
                            multiWindowStyle = r.multiWindowStyle;
                            return multiWindowStyle;
                        }
                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }
        return MultiWindowStyle.sConstDefaultMultiWindowStyle;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isTouchBlocked(android.os.IBinder r6) {
        /*
        r5 = this;
        r4 = r5.mActivities;
        monitor-enter(r4);
        r3 = r5.mActivities;	 Catch:{ all -> 0x005b }
        r3 = r3.size();	 Catch:{ all -> 0x005b }
        if (r3 <= 0) goto L_0x0058;
    L_0x000b:
        r0 = 0;
        if (r6 == 0) goto L_0x002f;
    L_0x000e:
        r3 = r5.mActivities;	 Catch:{ all -> 0x005b }
        r2 = r3.get(r6);	 Catch:{ all -> 0x005b }
        r2 = (android.app.ActivityThread.ActivityClientRecord) r2;	 Catch:{ all -> 0x005b }
        if (r2 == 0) goto L_0x001d;
    L_0x0018:
        r3 = r2.paused;	 Catch:{ all -> 0x005b }
        if (r3 != 0) goto L_0x001d;
    L_0x001c:
        r0 = r2;
    L_0x001d:
        if (r0 == 0) goto L_0x0058;
    L_0x001f:
        r3 = r0.activity;	 Catch:{ all -> 0x005b }
        if (r3 == 0) goto L_0x0058;
    L_0x0023:
        r3 = r0.activity;	 Catch:{ all -> 0x005b }
        r3 = r3.getWindow();	 Catch:{ all -> 0x005b }
        r3 = r3.isTouchBlocked();	 Catch:{ all -> 0x005b }
        monitor-exit(r4);	 Catch:{ all -> 0x005b }
    L_0x002e:
        return r3;
    L_0x002f:
        r3 = r5.mActivities;	 Catch:{ all -> 0x005b }
        r3 = r3.size();	 Catch:{ all -> 0x005b }
        r1 = r3 + -1;
    L_0x0037:
        if (r1 < 0) goto L_0x001d;
    L_0x0039:
        r3 = r5.mActivities;	 Catch:{ all -> 0x005b }
        r2 = r3.valueAt(r1);	 Catch:{ all -> 0x005b }
        r2 = (android.app.ActivityThread.ActivityClientRecord) r2;	 Catch:{ all -> 0x005b }
        if (r2 == 0) goto L_0x0055;
    L_0x0043:
        r3 = r2.paused;	 Catch:{ all -> 0x005b }
        if (r3 != 0) goto L_0x0055;
    L_0x0047:
        r3 = r2.activity;	 Catch:{ all -> 0x005b }
        r3 = r3.getWindow();	 Catch:{ all -> 0x005b }
        r3 = r3.hasStackFocus();	 Catch:{ all -> 0x005b }
        if (r3 == 0) goto L_0x0055;
    L_0x0053:
        r0 = r2;
        goto L_0x001d;
    L_0x0055:
        r1 = r1 + -1;
        goto L_0x0037;
    L_0x0058:
        monitor-exit(r4);	 Catch:{ all -> 0x005b }
        r3 = 0;
        goto L_0x002e;
    L_0x005b:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x005b }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.isTouchBlocked(android.os.IBinder):boolean");
    }

    public IBinder getBaseActivityToken() {
        synchronized (this.mActivities) {
            for (int i = this.mActivities.size() - 1; i >= 0; i--) {
                ActivityClientRecord r = (ActivityClientRecord) this.mActivities.valueAt(i);
                if (r != null) {
                    IBinder iBinder = r.token;
                    return iBinder;
                }
            }
            return null;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.os.IBinder getSuspectActivityToken() {
        /*
        r4 = this;
        r3 = r4.mActivities;
        monitor-enter(r3);
        r2 = r4.mActivities;	 Catch:{ all -> 0x003c }
        r2 = r2.size();	 Catch:{ all -> 0x003c }
        r0 = r2 + -1;
    L_0x000b:
        if (r0 < 0) goto L_0x0034;
    L_0x000d:
        r2 = r4.mActivities;	 Catch:{ all -> 0x003c }
        r1 = r2.valueAt(r0);	 Catch:{ all -> 0x003c }
        r1 = (android.app.ActivityThread.ActivityClientRecord) r1;	 Catch:{ all -> 0x003c }
        if (r1 == 0) goto L_0x0031;
    L_0x0017:
        r2 = r1.activity;	 Catch:{ all -> 0x003c }
        if (r2 == 0) goto L_0x0031;
    L_0x001b:
        r2 = r1.activity;	 Catch:{ all -> 0x003c }
        r2 = r2.mResumed;	 Catch:{ all -> 0x003c }
        if (r2 == 0) goto L_0x0031;
    L_0x0021:
        r2 = r1.activity;	 Catch:{ all -> 0x003c }
        r2 = r2.getWindow();	 Catch:{ all -> 0x003c }
        r2 = r2.hasStackFocus();	 Catch:{ all -> 0x003c }
        if (r2 == 0) goto L_0x0031;
    L_0x002d:
        r2 = r1.token;	 Catch:{ all -> 0x003c }
        monitor-exit(r3);	 Catch:{ all -> 0x003c }
    L_0x0030:
        return r2;
    L_0x0031:
        r0 = r0 + -1;
        goto L_0x000b;
    L_0x0034:
        monitor-exit(r3);	 Catch:{ all -> 0x003c }
        r2 = r4.mLastIntendedActivityToken;
        if (r2 == 0) goto L_0x003f;
    L_0x0039:
        r2 = r4.mLastIntendedActivityToken;
        goto L_0x0030;
    L_0x003c:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x003c }
        throw r2;
    L_0x003f:
        r2 = r4.getBaseActivityToken();
        goto L_0x0030;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.ActivityThread.getSuspectActivityToken():android.os.IBinder");
    }

    public Configuration getOverrideConfiguration(IBinder token) {
        Configuration configuration = null;
        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED) {
            synchronized (this.mActivities) {
                if (token != null) {
                    ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
                    if (r != null) {
                        configuration = r.overrideConfig;
                    }
                }
            }
        }
        return configuration;
    }

    public boolean isFixedOrientationCascade(IBinder token) {
        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED) {
            synchronized (this.mActivities) {
                if (token != null) {
                    boolean isFixedOrientationCascadeInner = isFixedOrientationCascadeInner((ActivityClientRecord) this.mActivities.get(token));
                    return isFixedOrientationCascadeInner;
                }
            }
        }
        return false;
    }

    private boolean isFixedOrientationCascadeInner(ActivityClientRecord r) {
        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED) {
            if (r == null || r.activity == null) {
                return false;
            }
            if (r.multiWindowStyle.isCascade() && r.overrideConfig != null) {
                int overrideOr = r.overrideConfig.orientation;
                if (overrideOr == 2 || overrideOr == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isSelectiveOrientationState(IBinder token) {
        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED) {
            synchronized (this.mActivities) {
                if (token != null) {
                    boolean isSelectiveOrientationStateInner = isSelectiveOrientationStateInner((ActivityClientRecord) this.mActivities.get(token));
                    return isSelectiveOrientationStateInner;
                }
            }
        }
        return false;
    }

    private boolean isSelectiveOrientationStateInner(ActivityClientRecord r) {
        if (MultiWindowFeatures.SELECTIVE1ORIENTATION_ENABLED) {
            if (r == null || r.activity == null) {
                return false;
            }
            if (!(!r.multiWindowStyle.isCascade() || this.mConfiguration == null || r.overrideConfig == null || r.overrideConfig.equals(Configuration.EMPTY))) {
                int globalOr = this.mTempConfigurationForSelectiveOrientation != null ? this.mTempConfigurationForSelectiveOrientation.orientation : this.mConfiguration.orientation;
                int overrideOr = r.overrideConfig.orientation;
                if (globalOr == 1 && overrideOr == 2) {
                    return true;
                }
                if (globalOr == 2 && overrideOr == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    public void forceStopActivity(IBinder token) {
        synchronized (this.mActivities) {
            ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
            if (r == null) {
                Log.w(TAG, "forceStopActivity: no activity for token " + token);
                return;
            }
            if (!r.stopped && r.paused) {
                try {
                    r.activity.performStop();
                } catch (Exception e) {
                    if (!this.mInstrumentation.onException(r.activity, e)) {
                        throw new RuntimeException("Unable to stop activity " + r.intent.getComponent().toShortString() + ": " + e.toString(), e);
                    }
                }
                r.stopped = true;
            }
            QueuedWork.waitToFinish();
        }
    }

    public void forceRestartActivity(IBinder token) {
        if (token != null) {
            synchronized (this.mActivities) {
                performRestartActivity(token);
            }
        }
    }

    public IBinder getLastIntendedActivityToken() {
        return this.mLastIntendedActivityToken != null ? this.mLastIntendedActivityToken : getBaseActivityToken();
    }

    private boolean hasMatchedActivityDisplayId(int displayId) {
        for (int i = 0; i < this.mActivities.size(); i++) {
            if (((ActivityClientRecord) this.mActivities.valueAt(i)).displayId == displayId) {
                return true;
            }
        }
        return false;
    }

    private boolean hasInputMethodService() {
        int NSVC = this.mServices.size();
        for (int i = 0; i < NSVC; i++) {
            if (this.mServices.valueAt(i) instanceof InputMethodService) {
                return true;
            }
        }
        return false;
    }

    private void handleActivityDisplayIdChanged(IBinder token, int displayId) {
    }

    private void handleApplicationDisplayIdChanged(int displayId) {
    }

    private void updateShrinkRequestedState(boolean shrinkRequested) {
        if (DEBUG_DUALSCREEN) {
            Slog.d(TAG, "updateShrinkRequestedState() : shrinkRequested=" + shrinkRequested);
        }
        synchronized (this) {
            this.mShrinkRequested = shrinkRequested;
        }
    }

    private void handleSendExpandRequest(IBinder token, int notifyReason) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (DEBUG_DUALSCREEN) {
            Slog.d(TAG, "handleSendExpandRequest() : r=" + r + " notifyReason=" + notifyReason);
        }
        if (r != null && r.activity != null) {
            r.activity.onExpandRequested(notifyReason);
        }
    }

    private void handleSendShrinkRequest(IBinder token, DualScreen toScreen, int notifyReason) {
        ActivityClientRecord r = (ActivityClientRecord) this.mActivities.get(token);
        if (DEBUG_DUALSCREEN) {
            Slog.d(TAG, "handleSendShrinkRequest() : r=" + r + " toScreen=" + toScreen + " notifyReason=" + notifyReason);
        }
        if (r != null && r.activity != null) {
            r.activity.onShrinkRequested(toScreen, notifyReason);
        }
    }

    public boolean getShrinkRequested() {
        return this.mShrinkRequested;
    }

    public boolean isSystemThread() {
        return this.mSystemThread;
    }
}
