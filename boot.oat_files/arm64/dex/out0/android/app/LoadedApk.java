package android.app;

import android.app.im.InjectionManager;
import android.content.BroadcastReceiver;
import android.content.BroadcastReceiver.PendingResult;
import android.content.ComponentName;
import android.content.Context;
import android.content.IIntentReceiver;
import android.content.IIntentReceiver.Stub;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.content.res.CompatibilityInfo;
import android.content.res.Resources;
import android.net.ProxyInfo;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Process;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.view.DisplayAdjustments;
import dalvik.system.VMRuntime;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

public final class LoadedApk {
    static final /* synthetic */ boolean $assertionsDisabled = (!LoadedApk.class.desiredAssertionStatus());
    private static final String TAG = "LoadedApk";
    private final boolean DEBUG_ELASTIC = true;
    private final boolean isElasticEnabled = true;
    private final ActivityThread mActivityThread;
    private final String mAppDir;
    private Application mApplication;
    private ApplicationInfo mApplicationInfo;
    private final ClassLoader mBaseClassLoader;
    private ClassLoader mClassLoader;
    int mClientCount = 0;
    private final String mDataDir;
    private final File mDataDirFile;
    private final DisplayAdjustments mDisplayAdjustments = new DisplayAdjustments();
    private final boolean mIncludeCode;
    private final String mLibDir;
    private String[] mOverlayDirs;
    final String mPackageName;
    private final ArrayMap<Context, ArrayMap<BroadcastReceiver, ReceiverDispatcher>> mReceivers = new ArrayMap();
    private final boolean mRegisterPackage;
    private final String mResDir;
    Resources mResources;
    private final boolean mSecurityViolation;
    private final ArrayMap<Context, ArrayMap<ServiceConnection, ServiceDispatcher>> mServices = new ArrayMap();
    private final String[] mSharedLibraries;
    private final String[] mSplitAppDirs;
    private final String[] mSplitResDirs;
    private final ArrayMap<Context, ArrayMap<ServiceConnection, ServiceDispatcher>> mUnboundServices = new ArrayMap();
    private final ArrayMap<Context, ArrayMap<BroadcastReceiver, ReceiverDispatcher>> mUnregisteredReceivers = new ArrayMap();

    static final class ReceiverDispatcher {
        final Handler mActivityThread;
        final Context mContext;
        boolean mForgotten;
        final Stub mIIntentReceiver;
        final Instrumentation mInstrumentation;
        final IntentReceiverLeaked mLocation;
        final BroadcastReceiver mReceiver;
        final boolean mRegistered;
        RuntimeException mUnregisterLocation;

        final class Args extends PendingResult implements Runnable {
            private Intent mCurIntent;
            private final boolean mOrdered;

            public Args(Intent intent, int resultCode, String resultData, Bundle resultExtras, boolean ordered, boolean sticky, int sendingUser) {
                super(resultCode, resultData, resultExtras, ReceiverDispatcher.this.mRegistered ? 1 : 2, ordered, sticky, ReceiverDispatcher.this.mIIntentReceiver.asBinder(), sendingUser, intent.getBroadcastQueueHint());
                this.mCurIntent = intent;
                this.mOrdered = ordered;
            }

            public void run() {
                BroadcastReceiver receiver = ReceiverDispatcher.this.mReceiver;
                boolean ordered = this.mOrdered;
                IActivityManager mgr = ActivityManagerNative.getDefault();
                Intent intent = this.mCurIntent;
                this.mCurIntent = null;
                if (receiver != null && !ReceiverDispatcher.this.mForgotten) {
                    Trace.traceBegin(64, "broadcastReceiveReg");
                    try {
                        ClassLoader cl = ReceiverDispatcher.this.mReceiver.getClass().getClassLoader();
                        intent.setExtrasClassLoader(cl);
                        setExtrasClassLoader(cl);
                        receiver.setPendingResult(this);
                        receiver.onReceive(ReceiverDispatcher.this.mContext, intent);
                    } catch (Exception e) {
                        if (ReceiverDispatcher.this.mRegistered && ordered) {
                            sendFinished(mgr);
                        }
                        if (ReceiverDispatcher.this.mInstrumentation == null || !ReceiverDispatcher.this.mInstrumentation.onException(ReceiverDispatcher.this.mReceiver, e)) {
                            Trace.traceEnd(64);
                            throw new RuntimeException("Error receiving broadcast " + intent + " in " + ReceiverDispatcher.this.mReceiver, e);
                        }
                    }
                    if (receiver.getPendingResult() != null) {
                        finish();
                    }
                    Trace.traceEnd(64);
                } else if (ReceiverDispatcher.this.mRegistered && ordered) {
                    sendFinished(mgr);
                }
            }
        }

        static final class InnerReceiver extends Stub {
            final WeakReference<ReceiverDispatcher> mDispatcher;
            final ReceiverDispatcher mStrongRef;

            InnerReceiver(ReceiverDispatcher rd, boolean strong) {
                this.mDispatcher = new WeakReference(rd);
                if (!strong) {
                    rd = null;
                }
                this.mStrongRef = rd;
            }

            public void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
                ReceiverDispatcher rd = (ReceiverDispatcher) this.mDispatcher.get();
                if (rd != null) {
                    rd.performReceive(intent, resultCode, data, extras, ordered, sticky, sendingUser);
                    return;
                }
                IActivityManager mgr = ActivityManagerNative.getDefault();
                if (extras != null) {
                    try {
                        extras.setAllowFds(false);
                    } catch (RemoteException e) {
                        Slog.w(ActivityThread.TAG, "Couldn't finish broadcast to unregistered receiver");
                        return;
                    }
                }
                mgr.finishReceiver(this, resultCode, data, extras, false, intent.getBroadcastQueueHint());
            }

            public String toString() {
                return dump();
            }

            public String dump() {
                StringBuilder sb = new StringBuilder();
                ReceiverDispatcher rd = (ReceiverDispatcher) this.mDispatcher.get();
                if (rd != null) {
                    sb.append(rd.getIntentReceiver());
                }
                return sb.toString();
            }
        }

        ReceiverDispatcher(BroadcastReceiver receiver, Context context, Handler activityThread, Instrumentation instrumentation, boolean registered) {
            if (activityThread == null) {
                throw new NullPointerException("Handler must not be null");
            }
            this.mIIntentReceiver = new InnerReceiver(this, !registered);
            this.mReceiver = receiver;
            this.mContext = context;
            this.mActivityThread = activityThread;
            this.mInstrumentation = instrumentation;
            this.mRegistered = registered;
            this.mLocation = new IntentReceiverLeaked(null);
            this.mLocation.fillInStackTrace();
        }

        void validate(Context context, Handler activityThread) {
            if (this.mContext != context) {
                throw new IllegalStateException("Receiver " + this.mReceiver + " registered with differing Context (was " + this.mContext + " now " + context + ")");
            } else if (this.mActivityThread != activityThread) {
                throw new IllegalStateException("Receiver " + this.mReceiver + " registered with differing handler (was " + this.mActivityThread + " now " + activityThread + ")");
            }
        }

        IntentReceiverLeaked getLocation() {
            return this.mLocation;
        }

        BroadcastReceiver getIntentReceiver() {
            return this.mReceiver;
        }

        IIntentReceiver getIIntentReceiver() {
            return this.mIIntentReceiver;
        }

        void setUnregisterLocation(RuntimeException ex) {
            this.mUnregisterLocation = ex;
        }

        RuntimeException getUnregisterLocation() {
            return this.mUnregisterLocation;
        }

        public void performReceive(Intent intent, int resultCode, String data, Bundle extras, boolean ordered, boolean sticky, int sendingUser) {
            Args args = new Args(intent, resultCode, data, extras, ordered, sticky, sendingUser);
            if (!this.mActivityThread.post(args) && this.mRegistered && ordered) {
                args.sendFinished(ActivityManagerNative.getDefault());
            }
        }
    }

    static final class ServiceDispatcher {
        private final ArrayMap<ComponentName, ConnectionInfo> mActiveConnections = new ArrayMap();
        private final Handler mActivityThread;
        private final ServiceConnection mConnection;
        private final Context mContext;
        private boolean mDied;
        private final int mFlags;
        private boolean mForgotten;
        private final InnerConnection mIServiceConnection = new InnerConnection(this);
        private final ServiceConnectionLeaked mLocation;
        private RuntimeException mUnbindLocation;

        private static class ConnectionInfo {
            IBinder binder;
            DeathRecipient deathMonitor;

            private ConnectionInfo() {
            }
        }

        private final class DeathMonitor implements DeathRecipient {
            final ComponentName mName;
            final IBinder mService;

            DeathMonitor(ComponentName name, IBinder service) {
                this.mName = name;
                this.mService = service;
            }

            public void binderDied() {
                ServiceDispatcher.this.death(this.mName, this.mService);
            }
        }

        private static class InnerConnection extends IServiceConnection.Stub {
            final WeakReference<ServiceDispatcher> mDispatcher;

            InnerConnection(ServiceDispatcher sd) {
                this.mDispatcher = new WeakReference(sd);
            }

            public void connected(ComponentName name, IBinder service) throws RemoteException {
                ServiceDispatcher sd = (ServiceDispatcher) this.mDispatcher.get();
                if (sd != null) {
                    sd.connected(name, service);
                }
            }
        }

        private final class RunConnection implements Runnable {
            final int mCommand;
            final ComponentName mName;
            final IBinder mService;

            RunConnection(ComponentName name, IBinder service, int command) {
                this.mName = name;
                this.mService = service;
                this.mCommand = command;
            }

            public void run() {
                if (this.mCommand == 0) {
                    ServiceDispatcher.this.doConnected(this.mName, this.mService);
                } else if (this.mCommand == 1) {
                    ServiceDispatcher.this.doDeath(this.mName, this.mService);
                }
            }
        }

        ServiceDispatcher(ServiceConnection conn, Context context, Handler activityThread, int flags) {
            this.mConnection = conn;
            this.mContext = context;
            this.mActivityThread = activityThread;
            this.mLocation = new ServiceConnectionLeaked(null);
            this.mLocation.fillInStackTrace();
            this.mFlags = flags;
        }

        void validate(Context context, Handler activityThread) {
            if (this.mContext != context) {
                throw new RuntimeException("ServiceConnection " + this.mConnection + " registered with differing Context (was " + this.mContext + " now " + context + ")");
            } else if (this.mActivityThread != activityThread) {
                throw new RuntimeException("ServiceConnection " + this.mConnection + " registered with differing handler (was " + this.mActivityThread + " now " + activityThread + ")");
            }
        }

        void doForget() {
            synchronized (this) {
                for (int i = 0; i < this.mActiveConnections.size(); i++) {
                    ConnectionInfo ci = (ConnectionInfo) this.mActiveConnections.valueAt(i);
                    ci.binder.unlinkToDeath(ci.deathMonitor, 0);
                }
                this.mActiveConnections.clear();
                this.mForgotten = true;
            }
        }

        ServiceConnectionLeaked getLocation() {
            return this.mLocation;
        }

        ServiceConnection getServiceConnection() {
            return this.mConnection;
        }

        IServiceConnection getIServiceConnection() {
            return this.mIServiceConnection;
        }

        int getFlags() {
            return this.mFlags;
        }

        void setUnbindLocation(RuntimeException ex) {
            this.mUnbindLocation = ex;
        }

        RuntimeException getUnbindLocation() {
            return this.mUnbindLocation;
        }

        public void connected(ComponentName name, IBinder service) {
            if (this.mActivityThread != null) {
                this.mActivityThread.post(new RunConnection(name, service, 0));
            } else {
                doConnected(name, service);
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void death(android.content.ComponentName r6, android.os.IBinder r7) {
            /*
            r5 = this;
            r4 = 1;
            monitor-enter(r5);
            r1 = 1;
            r5.mDied = r1;	 Catch:{ all -> 0x002d }
            r1 = r5.mActiveConnections;	 Catch:{ all -> 0x002d }
            r0 = r1.remove(r6);	 Catch:{ all -> 0x002d }
            r0 = (android.app.LoadedApk.ServiceDispatcher.ConnectionInfo) r0;	 Catch:{ all -> 0x002d }
            if (r0 == 0) goto L_0x0013;
        L_0x000f:
            r1 = r0.binder;	 Catch:{ all -> 0x002d }
            if (r1 == r7) goto L_0x0015;
        L_0x0013:
            monitor-exit(r5);	 Catch:{ all -> 0x002d }
        L_0x0014:
            return;
        L_0x0015:
            r1 = r0.binder;	 Catch:{ all -> 0x002d }
            r2 = r0.deathMonitor;	 Catch:{ all -> 0x002d }
            r3 = 0;
            r1.unlinkToDeath(r2, r3);	 Catch:{ all -> 0x002d }
            monitor-exit(r5);	 Catch:{ all -> 0x002d }
            r1 = r5.mActivityThread;
            if (r1 == 0) goto L_0x0030;
        L_0x0022:
            r1 = r5.mActivityThread;
            r2 = new android.app.LoadedApk$ServiceDispatcher$RunConnection;
            r2.<init>(r6, r7, r4);
            r1.post(r2);
            goto L_0x0014;
        L_0x002d:
            r1 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x002d }
            throw r1;
        L_0x0030:
            r5.doDeath(r6, r7);
            goto L_0x0014;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.LoadedApk.ServiceDispatcher.death(android.content.ComponentName, android.os.IBinder):void");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void doConnected(android.content.ComponentName r7, android.os.IBinder r8) {
            /*
            r6 = this;
            monitor-enter(r6);
            r3 = r6.mForgotten;	 Catch:{ all -> 0x0017 }
            if (r3 == 0) goto L_0x0007;
        L_0x0005:
            monitor-exit(r6);	 Catch:{ all -> 0x0017 }
        L_0x0006:
            return;
        L_0x0007:
            r3 = r6.mActiveConnections;	 Catch:{ all -> 0x0017 }
            r2 = r3.get(r7);	 Catch:{ all -> 0x0017 }
            r2 = (android.app.LoadedApk.ServiceDispatcher.ConnectionInfo) r2;	 Catch:{ all -> 0x0017 }
            if (r2 == 0) goto L_0x001a;
        L_0x0011:
            r3 = r2.binder;	 Catch:{ all -> 0x0017 }
            if (r3 != r8) goto L_0x001a;
        L_0x0015:
            monitor-exit(r6);	 Catch:{ all -> 0x0017 }
            goto L_0x0006;
        L_0x0017:
            r3 = move-exception;
            monitor-exit(r6);	 Catch:{ all -> 0x0017 }
            throw r3;
        L_0x001a:
            if (r8 == 0) goto L_0x005b;
        L_0x001c:
            r3 = 0;
            r6.mDied = r3;	 Catch:{ all -> 0x0017 }
            r1 = new android.app.LoadedApk$ServiceDispatcher$ConnectionInfo;	 Catch:{ all -> 0x0017 }
            r3 = 0;
            r1.<init>();	 Catch:{ all -> 0x0017 }
            r1.binder = r8;	 Catch:{ all -> 0x0017 }
            r3 = new android.app.LoadedApk$ServiceDispatcher$DeathMonitor;	 Catch:{ all -> 0x0017 }
            r3.<init>(r7, r8);	 Catch:{ all -> 0x0017 }
            r1.deathMonitor = r3;	 Catch:{ all -> 0x0017 }
            r3 = r1.deathMonitor;	 Catch:{ RemoteException -> 0x0053 }
            r4 = 0;
            r8.linkToDeath(r3, r4);	 Catch:{ RemoteException -> 0x0053 }
            r3 = r6.mActiveConnections;	 Catch:{ RemoteException -> 0x0053 }
            r3.put(r7, r1);	 Catch:{ RemoteException -> 0x0053 }
        L_0x0039:
            if (r2 == 0) goto L_0x0043;
        L_0x003b:
            r3 = r2.binder;	 Catch:{ all -> 0x0017 }
            r4 = r2.deathMonitor;	 Catch:{ all -> 0x0017 }
            r5 = 0;
            r3.unlinkToDeath(r4, r5);	 Catch:{ all -> 0x0017 }
        L_0x0043:
            monitor-exit(r6);	 Catch:{ all -> 0x0017 }
            if (r2 == 0) goto L_0x004b;
        L_0x0046:
            r3 = r6.mConnection;
            r3.onServiceDisconnected(r7);
        L_0x004b:
            if (r8 == 0) goto L_0x0006;
        L_0x004d:
            r3 = r6.mConnection;
            r3.onServiceConnected(r7, r8);
            goto L_0x0006;
        L_0x0053:
            r0 = move-exception;
            r3 = r6.mActiveConnections;	 Catch:{ all -> 0x0017 }
            r3.remove(r7);	 Catch:{ all -> 0x0017 }
            monitor-exit(r6);	 Catch:{ all -> 0x0017 }
            goto L_0x0006;
        L_0x005b:
            r3 = r6.mActiveConnections;	 Catch:{ all -> 0x0017 }
            r3.remove(r7);	 Catch:{ all -> 0x0017 }
            goto L_0x0039;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.LoadedApk.ServiceDispatcher.doConnected(android.content.ComponentName, android.os.IBinder):void");
        }

        public void doDeath(ComponentName name, IBinder service) {
            this.mConnection.onServiceDisconnected(name);
        }
    }

    private static class WarningContextClassLoader extends ClassLoader {
        private static boolean warned = false;

        private WarningContextClassLoader() {
        }

        private void warn(String methodName) {
            if (!warned) {
                warned = true;
                Thread.currentThread().setContextClassLoader(getParent());
                Slog.w(ActivityThread.TAG, "ClassLoader." + methodName + ": " + "The class loader returned by " + "Thread.getContextClassLoader() may fail for processes " + "that host multiple applications. You should explicitly " + "specify a context class loader. For example: " + "Thread.setContextClassLoader(getClass().getClassLoader());");
            }
        }

        public URL getResource(String resName) {
            warn("getResource");
            return getParent().getResource(resName);
        }

        public Enumeration<URL> getResources(String resName) throws IOException {
            warn("getResources");
            return getParent().getResources(resName);
        }

        public InputStream getResourceAsStream(String resName) {
            warn("getResourceAsStream");
            return getParent().getResourceAsStream(resName);
        }

        public Class<?> loadClass(String className) throws ClassNotFoundException {
            warn("loadClass");
            return getParent().loadClass(className);
        }

        public void setClassAssertionStatus(String cname, boolean enable) {
            warn("setClassAssertionStatus");
            getParent().setClassAssertionStatus(cname, enable);
        }

        public void setPackageAssertionStatus(String pname, boolean enable) {
            warn("setPackageAssertionStatus");
            getParent().setPackageAssertionStatus(pname, enable);
        }

        public void setDefaultAssertionStatus(boolean enable) {
            warn("setDefaultAssertionStatus");
            getParent().setDefaultAssertionStatus(enable);
        }

        public void clearAssertionStatus() {
            warn("clearAssertionStatus");
            getParent().clearAssertionStatus();
        }
    }

    Application getApplication() {
        return this.mApplication;
    }

    public LoadedApk(ActivityThread activityThread, ApplicationInfo aInfo, CompatibilityInfo compatInfo, ClassLoader baseLoader, boolean securityViolation, boolean includeCode, boolean registerPackage) {
        int myUid = Process.myUid();
        aInfo = adjustNativeLibraryPaths(aInfo);
        this.mActivityThread = activityThread;
        this.mApplicationInfo = aInfo;
        this.mPackageName = aInfo.packageName;
        this.mAppDir = aInfo.sourceDir;
        this.mResDir = aInfo.uid == myUid ? aInfo.sourceDir : aInfo.publicSourceDir;
        this.mSplitAppDirs = aInfo.splitSourceDirs;
        this.mSplitResDirs = aInfo.uid == myUid ? aInfo.splitSourceDirs : aInfo.splitPublicSourceDirs;
        this.mOverlayDirs = aInfo.resourceDirs;
        this.mSharedLibraries = aInfo.sharedLibraryFiles;
        this.mDataDir = aInfo.dataDir;
        this.mDataDirFile = this.mDataDir != null ? new File(this.mDataDir) : null;
        this.mLibDir = aInfo.nativeLibraryDir;
        this.mBaseClassLoader = baseLoader;
        this.mSecurityViolation = securityViolation;
        this.mIncludeCode = includeCode;
        this.mRegisterPackage = registerPackage;
        this.mDisplayAdjustments.setCompatibilityInfo(compatInfo);
    }

    private static ApplicationInfo adjustNativeLibraryPaths(ApplicationInfo info) {
        if (!(info.primaryCpuAbi == null || info.secondaryCpuAbi == null)) {
            String runtimeIsa = VMRuntime.getRuntime().vmInstructionSet();
            String secondaryIsa = VMRuntime.getInstructionSet(info.secondaryCpuAbi);
            String secondaryDexCodeIsa = SystemProperties.get("ro.dalvik.vm.isa." + secondaryIsa);
            if (!secondaryDexCodeIsa.isEmpty()) {
                secondaryIsa = secondaryDexCodeIsa;
            }
            if (runtimeIsa.equals(secondaryIsa)) {
                ApplicationInfo modified = new ApplicationInfo(info);
                modified.nativeLibraryDir = modified.secondaryNativeLibraryDir;
                modified.primaryCpuAbi = modified.secondaryCpuAbi;
                return modified;
            }
        }
        return info;
    }

    LoadedApk(ActivityThread activityThread) {
        this.mActivityThread = activityThread;
        this.mApplicationInfo = new ApplicationInfo();
        this.mApplicationInfo.packageName = "android";
        this.mPackageName = "android";
        this.mAppDir = null;
        this.mResDir = null;
        this.mSplitAppDirs = null;
        this.mSplitResDirs = null;
        this.mOverlayDirs = null;
        this.mSharedLibraries = null;
        this.mDataDir = null;
        this.mDataDirFile = null;
        this.mLibDir = null;
        this.mBaseClassLoader = null;
        this.mSecurityViolation = false;
        this.mIncludeCode = true;
        this.mRegisterPackage = false;
        this.mClassLoader = ClassLoader.getSystemClassLoader();
        this.mResources = Resources.getSystem();
    }

    void installSystemApplicationInfo(ApplicationInfo info, ClassLoader classLoader) {
        if ($assertionsDisabled || info.packageName.equals("android")) {
            this.mApplicationInfo = info;
            this.mClassLoader = classLoader;
            return;
        }
        throw new AssertionError();
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public ApplicationInfo getApplicationInfo() {
        return this.mApplicationInfo;
    }

    public boolean isSecurityViolation() {
        return this.mSecurityViolation;
    }

    public CompatibilityInfo getCompatibilityInfo() {
        return this.mDisplayAdjustments.getCompatibilityInfo();
    }

    public void setCompatibilityInfo(CompatibilityInfo compatInfo) {
        this.mDisplayAdjustments.setCompatibilityInfo(compatInfo);
    }

    public void setOverlayDirs(String[] overlayDirs) {
        this.mOverlayDirs = overlayDirs;
    }

    private static String[] getLibrariesFor(String packageName) {
        try {
            ApplicationInfo ai = ActivityThread.getPackageManager().getApplicationInfo(packageName, 1024, UserHandle.myUserId());
            if (ai == null) {
                return null;
            }
            return ai.sharedLibraryFiles;
        } catch (RemoteException e) {
            throw new AssertionError(e);
        }
    }

    public ClassLoader getClassLoader() {
        ClassLoader classLoader;
        synchronized (this) {
            if (this.mClassLoader != null) {
                classLoader = this.mClassLoader;
            } else {
                String classLibPath = InjectionManager.getClassLibPath(1);
                if (this.mActivityThread.mCoreFeatureClassLoader == null || classLibPath == null || classLibPath.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
                    if (this.mIncludeCode && !this.mPackageName.equals("android")) {
                        if (!Objects.equals(this.mPackageName, ActivityThread.currentPackageName())) {
                            try {
                                ActivityThread.getPackageManager().performDexOptIfNeeded(this.mPackageName, VMRuntime.getRuntime().vmInstructionSet());
                            } catch (RemoteException e) {
                            }
                        }
                        List<String> zipPaths = new ArrayList();
                        List<String> apkPaths = new ArrayList();
                        List<String> libPaths = new ArrayList();
                        if (this.mRegisterPackage) {
                            try {
                                ActivityManagerNative.getDefault().addPackageDependency(this.mPackageName);
                            } catch (RemoteException e2) {
                            }
                        }
                        zipPaths.add(this.mAppDir);
                        if (this.mSplitAppDirs != null) {
                            Collections.addAll(zipPaths, this.mSplitAppDirs);
                        }
                        libPaths.add(this.mLibDir);
                        String instrumentationPackageName = this.mActivityThread.mInstrumentationPackageName;
                        String instrumentationAppDir = this.mActivityThread.mInstrumentationAppDir;
                        String[] instrumentationSplitAppDirs = this.mActivityThread.mInstrumentationSplitAppDirs;
                        String instrumentationLibDir = this.mActivityThread.mInstrumentationLibDir;
                        String instrumentedAppDir = this.mActivityThread.mInstrumentedAppDir;
                        String[] instrumentedSplitAppDirs = this.mActivityThread.mInstrumentedSplitAppDirs;
                        String instrumentedLibDir = this.mActivityThread.mInstrumentedLibDir;
                        String[] instrumentationLibs = null;
                        if (this.mAppDir.equals(instrumentationAppDir) || this.mAppDir.equals(instrumentedAppDir)) {
                            zipPaths.clear();
                            zipPaths.add(instrumentationAppDir);
                            if (instrumentationSplitAppDirs != null) {
                                Collections.addAll(zipPaths, instrumentationSplitAppDirs);
                            }
                            zipPaths.add(instrumentedAppDir);
                            if (instrumentedSplitAppDirs != null) {
                                Collections.addAll(zipPaths, instrumentedSplitAppDirs);
                            }
                            libPaths.clear();
                            libPaths.add(instrumentationLibDir);
                            libPaths.add(instrumentedLibDir);
                            if (!instrumentedAppDir.equals(instrumentationAppDir)) {
                                instrumentationLibs = getLibrariesFor(instrumentationPackageName);
                            }
                        }
                        apkPaths.addAll(zipPaths);
                        if (this.mSharedLibraries != null) {
                            for (String lib : this.mSharedLibraries) {
                                if (!zipPaths.contains(lib)) {
                                    zipPaths.add(0, lib);
                                }
                            }
                        }
                        if (instrumentationLibs != null) {
                            for (String lib2 : instrumentationLibs) {
                                if (!zipPaths.contains(lib2)) {
                                    zipPaths.add(0, lib2);
                                }
                            }
                        }
                        String zip = TextUtils.join(File.pathSeparator, zipPaths);
                        if (this.mApplicationInfo.primaryCpuAbi != null) {
                            for (String apk : apkPaths) {
                                libPaths.add(apk + "!/lib/" + this.mApplicationInfo.primaryCpuAbi);
                            }
                        }
                        String lib22 = TextUtils.join(File.pathSeparator, libPaths);
                        ThreadPolicy oldPolicy = StrictMode.allowThreadDiskReads();
                        if (classLibPath == null || classLibPath.equals(ProxyInfo.LOCAL_EXCL_LIST)) {
                            this.mClassLoader = ApplicationLoaders.getDefault().getClassLoader(zip, lib22, this.mBaseClassLoader);
                        } else {
                            String[] paths = classLibPath.split("#");
                            this.mActivityThread.mCoreFeatureClassLoader = ApplicationLoaders.getDefault().getClassLoader(zip + ":" + paths[0], lib22 + ":" + paths[1], this.mBaseClassLoader, true);
                            this.mClassLoader = this.mActivityThread.mCoreFeatureClassLoader;
                        }
                        StrictMode.setThreadPolicy(oldPolicy);
                    } else if (this.mBaseClassLoader == null) {
                        this.mClassLoader = ClassLoader.getSystemClassLoader();
                    } else {
                        this.mClassLoader = this.mBaseClassLoader;
                    }
                    classLoader = this.mClassLoader;
                } else {
                    this.mClassLoader = this.mActivityThread.mCoreFeatureClassLoader;
                    initializeJavaContextClassLoader();
                    classLoader = this.mClassLoader;
                }
            }
        }
        return classLoader;
    }

    private void initializeJavaContextClassLoader() {
        try {
            PackageInfo pi = ActivityThread.getPackageManager().getPackageInfo(this.mPackageName, 0, UserHandle.myUserId());
            if (pi == null) {
                throw new IllegalStateException("Unable to get package info for " + this.mPackageName + "; is package not installed?");
            }
            boolean sharedUserIdSet;
            boolean sharable;
            if (pi.sharedUserId != null) {
                sharedUserIdSet = true;
            } else {
                sharedUserIdSet = false;
            }
            boolean processNameNotDefault;
            if (pi.applicationInfo == null || this.mPackageName.equals(pi.applicationInfo.processName)) {
                processNameNotDefault = false;
            } else {
                processNameNotDefault = true;
            }
            if (sharedUserIdSet || processNameNotDefault) {
                sharable = true;
            } else {
                sharable = false;
            }
            Thread.currentThread().setContextClassLoader(sharable ? new WarningContextClassLoader() : this.mClassLoader);
        } catch (RemoteException e) {
            throw new IllegalStateException("Unable to get package info for " + this.mPackageName + "; is system dying?", e);
        }
    }

    public String getAppDir() {
        return this.mAppDir;
    }

    public String getLibDir() {
        return this.mLibDir;
    }

    public String getResDir() {
        return this.mResDir;
    }

    public String[] getSplitAppDirs() {
        return this.mSplitAppDirs;
    }

    public String[] getSplitResDirs() {
        return this.mSplitResDirs;
    }

    public String[] getOverlayDirs() {
        return this.mOverlayDirs;
    }

    public String getDataDir() {
        return this.mDataDir;
    }

    public File getDataDirFile() {
        return this.mDataDirFile;
    }

    public AssetManager getAssets(ActivityThread mainThread) {
        return getResources(mainThread).getAssets();
    }

    public Resources getResources(ActivityThread mainThread) {
        return getResources(mainThread, UserHandle.CURRENT);
    }

    public Resources getResources(ActivityThread mainThread, UserHandle userHandle) {
        return getResources(mainThread, 0, userHandle);
    }

    public Resources getResources(ActivityThread mainThread, int displayId) {
        return getResources(mainThread, displayId, UserHandle.CURRENT);
    }

    public Resources getResources(ActivityThread mainThread, int displayId, UserHandle userHandle) {
        if (this.mResources == null) {
            this.mResources = mainThread.getTopLevelResources(this.mResDir, this.mSplitResDirs, this.mOverlayDirs, this.mApplicationInfo.sharedLibraryFiles, displayId, null, this, userHandle.getIdentifier());
        }
        return this.mResources;
    }

    public Application makeApplication(boolean forceDefaultAppClass, Instrumentation instrumentation) {
        if (ActivityThread.DEBUG_DUALSCREEN) {
            Slog.i(TAG, "makeApplication() LoadedApk=" + this + " appName=" + (this.mApplicationInfo != null ? this.mApplicationInfo.className : ProxyInfo.LOCAL_EXCL_LIST) + " caller=" + Debug.getCallers(4));
        }
        if (this.mApplication != null) {
            return this.mApplication;
        }
        Application app = null;
        String appClass = this.mApplicationInfo.className;
        if (forceDefaultAppClass || appClass == null) {
            appClass = "android.app.Application";
        }
        try {
            ClassLoader cl = getClassLoader();
            if (!this.mPackageName.equals("android")) {
                initializeJavaContextClassLoader();
            }
            ContextImpl appContext = ContextImpl.createAppContext(this.mActivityThread, this, 0);
            app = this.mActivityThread.mInstrumentation.newApplication(cl, appClass, appContext);
            appContext.setOuterContext(app);
        } catch (Exception e) {
            if (!this.mActivityThread.mInstrumentation.onException(null, e)) {
                throw new RuntimeException("Unable to instantiate application " + appClass + ": " + e.toString(), e);
            }
        }
        this.mActivityThread.mAllApplications.add(app);
        this.mApplication = app;
        if (instrumentation != null) {
            try {
                instrumentation.callApplicationOnCreate(app);
            } catch (Exception e2) {
                if (!instrumentation.onException(app, e2)) {
                    throw new RuntimeException("Unable to create application " + app.getClass().getName() + ": " + e2.toString(), e2);
                }
            }
        }
        SparseArray<String> packageIdentifiers = getAssets(this.mActivityThread).getAssignedPackageIdentifiers();
        int N = packageIdentifiers.size();
        for (int i = 0; i < N; i++) {
            int id = packageIdentifiers.keyAt(i);
            if (!(id == 1 || id == 127 || id == 2)) {
                rewriteRValues(getClassLoader(), (String) packageIdentifiers.valueAt(i), id);
            }
        }
        return app;
    }

    private void rewriteRValues(ClassLoader cl, String packageName, int id) {
        try {
            try {
                Throwable cause;
                try {
                    cl.loadClass(packageName + ".R").getMethod("onResourcesLoaded", new Class[]{Integer.TYPE}).invoke(null, new Object[]{Integer.valueOf(id)});
                    return;
                } catch (Throwable e) {
                    cause = e;
                } catch (InvocationTargetException e2) {
                    cause = e2.getCause();
                }
                throw new RuntimeException("Failed to rewrite resource references for " + packageName, cause);
            } catch (NoSuchMethodException e3) {
            }
        } catch (ClassNotFoundException e4) {
            Log.i(TAG, "No resource references to update in package " + packageName);
        }
    }

    public void removeContextRegistrations(Context context, String who, String what) {
        boolean reportRegistrationLeaks = StrictMode.vmRegistrationLeaksEnabled();
        synchronized (this.mReceivers) {
            int i;
            IntentReceiverLeaked leak;
            ArrayMap<BroadcastReceiver, ReceiverDispatcher> rmap = (ArrayMap) this.mReceivers.remove(context);
            if (rmap != null) {
                for (i = 0; i < rmap.size(); i++) {
                    ReceiverDispatcher rd = (ReceiverDispatcher) rmap.valueAt(i);
                    leak = new IntentReceiverLeaked(what + " " + who + " has leaked IntentReceiver " + rd.getIntentReceiver() + " that was " + "originally registered here. Are you missing a " + "call to unregisterReceiver()?");
                    leak.setStackTrace(rd.getLocation().getStackTrace());
                    Slog.e(ActivityThread.TAG, leak.getMessage(), leak);
                    if (reportRegistrationLeaks) {
                        StrictMode.onIntentReceiverLeaked(leak);
                    }
                    try {
                        ActivityManagerNative.getDefault().unregisterReceiver(rd.getIIntentReceiver());
                    } catch (RemoteException e) {
                    }
                }
            }
            this.mUnregisteredReceivers.remove(context);
        }
        synchronized (this.mServices) {
            ArrayMap<ServiceConnection, ServiceDispatcher> smap = (ArrayMap) this.mServices.remove(context);
            if (smap != null) {
                for (i = 0; i < smap.size(); i++) {
                    ServiceDispatcher sd = (ServiceDispatcher) smap.valueAt(i);
                    leak = new ServiceConnectionLeaked(what + " " + who + " has leaked ServiceConnection " + sd.getServiceConnection() + " that was originally bound here");
                    leak.setStackTrace(sd.getLocation().getStackTrace());
                    Slog.e(ActivityThread.TAG, leak.getMessage(), leak);
                    if (reportRegistrationLeaks) {
                        StrictMode.onServiceConnectionLeaked(leak);
                    }
                    try {
                        ActivityManagerNative.getDefault().unbindService(sd.getIServiceConnection());
                    } catch (RemoteException e2) {
                    }
                    sd.doForget();
                }
            }
            this.mUnboundServices.remove(context);
        }
    }

    public IIntentReceiver getReceiverDispatcher(BroadcastReceiver r, Context context, Handler handler, Instrumentation instrumentation, boolean registered) {
        Throwable th;
        synchronized (this.mReceivers) {
            ArrayMap<BroadcastReceiver, ReceiverDispatcher> map;
            ReceiverDispatcher rd;
            ReceiverDispatcher rd2;
            IIntentReceiver iIntentReceiver;
            ArrayMap<BroadcastReceiver, ReceiverDispatcher> map2 = null;
            if (registered) {
                try {
                    map2 = (ArrayMap) this.mReceivers.get(context);
                    if (map2 != null) {
                        map = map2;
                        rd = (ReceiverDispatcher) map2.get(r);
                        if (rd != null) {
                            try {
                                rd2 = new ReceiverDispatcher(r, context, handler, instrumentation, registered);
                                if (registered) {
                                } else {
                                    if (map != null) {
                                        try {
                                            map2 = new ArrayMap();
                                            this.mReceivers.put(context, map2);
                                        } catch (Throwable th2) {
                                            th = th2;
                                            map2 = map;
                                            throw th;
                                        }
                                    }
                                    map2 = map;
                                    map2.put(r, rd2);
                                }
                            } catch (Throwable th3) {
                                th = th3;
                                map2 = map;
                                rd2 = rd;
                                throw th;
                            }
                        }
                        rd.validate(context, handler);
                        map2 = map;
                        rd2 = rd;
                        rd2.mForgotten = false;
                        iIntentReceiver = rd2.getIIntentReceiver();
                        return iIntentReceiver;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    throw th;
                }
            }
            map = map2;
            rd = null;
            if (rd != null) {
                rd.validate(context, handler);
                map2 = map;
                rd2 = rd;
            } else {
                rd2 = new ReceiverDispatcher(r, context, handler, instrumentation, registered);
                if (registered) {
                } else {
                    if (map != null) {
                        map2 = map;
                    } else {
                        map2 = new ArrayMap();
                        this.mReceivers.put(context, map2);
                    }
                    map2.put(r, rd2);
                }
            }
            rd2.mForgotten = false;
            iIntentReceiver = rd2.getIIntentReceiver();
            return iIntentReceiver;
        }
    }

    public IIntentReceiver forgetReceiverDispatcher(Context context, BroadcastReceiver r) {
        IIntentReceiver iIntentReceiver;
        synchronized (this.mReceivers) {
            ReceiverDispatcher rd;
            ArrayMap<BroadcastReceiver, ReceiverDispatcher> holder;
            ArrayMap<BroadcastReceiver, ReceiverDispatcher> map = (ArrayMap) this.mReceivers.get(context);
            if (map != null) {
                rd = (ReceiverDispatcher) map.get(r);
                if (rd != null) {
                    map.remove(r);
                    if (map.size() == 0) {
                        this.mReceivers.remove(context);
                    }
                    if (r.getDebugUnregister()) {
                        holder = (ArrayMap) this.mUnregisteredReceivers.get(context);
                        if (holder == null) {
                            holder = new ArrayMap();
                            this.mUnregisteredReceivers.put(context, holder);
                        }
                        RuntimeException ex = new IllegalArgumentException("Originally unregistered here:");
                        ex.fillInStackTrace();
                        rd.setUnregisterLocation(ex);
                        holder.put(r, rd);
                    }
                    rd.mForgotten = true;
                    iIntentReceiver = rd.getIIntentReceiver();
                }
            }
            holder = (ArrayMap) this.mUnregisteredReceivers.get(context);
            if (holder != null) {
                rd = (ReceiverDispatcher) holder.get(r);
                if (rd != null) {
                    throw new IllegalArgumentException("Unregistering Receiver " + r + " that was already unregistered", rd.getUnregisterLocation());
                }
            }
            if (context == null) {
                throw new IllegalStateException("Unbinding Receiver " + r + " from Context that is no longer in use: " + context);
            }
            throw new IllegalArgumentException("Receiver not registered: " + r);
        }
        return iIntentReceiver;
    }

    public final IServiceConnection getServiceDispatcher(ServiceConnection c, Context context, Handler handler, int flags) {
        Throwable th;
        synchronized (this.mServices) {
            try {
                ServiceDispatcher sd;
                ServiceDispatcher sd2;
                ArrayMap<ServiceConnection, ServiceDispatcher> map = (ArrayMap) this.mServices.get(context);
                if (map != null) {
                    sd = (ServiceDispatcher) map.get(c);
                } else {
                    sd = null;
                }
                if (sd == null) {
                    try {
                        sd2 = new ServiceDispatcher(c, context, handler, flags);
                        if (map == null) {
                            map = new ArrayMap();
                            this.mServices.put(context, map);
                        }
                        map.put(c, sd2);
                    } catch (Throwable th2) {
                        th = th2;
                        sd2 = sd;
                        throw th;
                    }
                }
                sd.validate(context, handler);
                sd2 = sd;
                IServiceConnection iServiceConnection = sd2.getIServiceConnection();
                return iServiceConnection;
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
    }

    public final IServiceConnection forgetServiceDispatcher(Context context, ServiceConnection c) {
        IServiceConnection iServiceConnection;
        synchronized (this.mServices) {
            ServiceDispatcher sd;
            ArrayMap<ServiceConnection, ServiceDispatcher> holder;
            ArrayMap<ServiceConnection, ServiceDispatcher> map = (ArrayMap) this.mServices.get(context);
            if (map != null) {
                sd = (ServiceDispatcher) map.get(c);
                if (sd != null) {
                    map.remove(c);
                    sd.doForget();
                    if (map.size() == 0) {
                        this.mServices.remove(context);
                    }
                    if ((sd.getFlags() & 2) != 0) {
                        holder = (ArrayMap) this.mUnboundServices.get(context);
                        if (holder == null) {
                            holder = new ArrayMap();
                            this.mUnboundServices.put(context, holder);
                        }
                        RuntimeException ex = new IllegalArgumentException("Originally unbound here:");
                        ex.fillInStackTrace();
                        sd.setUnbindLocation(ex);
                        holder.put(c, sd);
                    }
                    iServiceConnection = sd.getIServiceConnection();
                }
            }
            holder = (ArrayMap) this.mUnboundServices.get(context);
            if (holder != null) {
                sd = (ServiceDispatcher) holder.get(c);
                if (sd != null) {
                    throw new IllegalArgumentException("Unbinding Service " + c + " that was already unbound", sd.getUnbindLocation());
                }
            }
            if (context == null) {
                throw new IllegalStateException("Unbinding Service " + c + " from Context that is no longer in use: " + context);
            }
            throw new IllegalArgumentException("Service not registered: " + c);
        }
        return iServiceConnection;
    }
}
