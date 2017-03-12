package android.app;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.input.InputManager;
import android.net.NetworkPolicyManager;
import android.os.Bundle;
import android.os.Debug;
import android.os.IBinder;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.MessageQueue.IdleHandler;
import android.os.PerformanceCollector;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import android.view.IWindowManager.Stub;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.android.internal.content.ReferrerIntent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Instrumentation {
    public static final String REPORT_KEY_IDENTIFIER = "id";
    public static final String REPORT_KEY_STREAMRESULT = "stream";
    private static final String TAG = "Instrumentation";
    private final boolean isElasticEnabled = true;
    private List<ActivityMonitor> mActivityMonitors;
    private Context mAppContext;
    private boolean mAutomaticPerformanceSnapshots = false;
    private ComponentName mComponent;
    private Context mInstrContext;
    private MessageQueue mMessageQueue = null;
    private Bundle mPerfMetrics = new Bundle();
    private PerformanceCollector mPerformanceCollector;
    private Thread mRunner;
    private final Object mSync = new Object();
    private ActivityThread mThread = null;
    private UiAutomation mUiAutomation;
    private IUiAutomationConnection mUiAutomationConnection;
    private List<ActivityWaiter> mWaitingActivities;
    private IInstrumentationWatcher mWatcher;

    class AnonymousClass1ContextMenuRunnable implements Runnable {
        private final Activity activity;
        private final int flags;
        private final int identifier;
        boolean returnValue;

        public AnonymousClass1ContextMenuRunnable(Activity _activity, int _identifier, int _flags) {
            this.activity = _activity;
            this.identifier = _identifier;
            this.flags = _flags;
        }

        public void run() {
            this.returnValue = this.activity.getWindow().performContextMenuIdentifierAction(this.identifier, this.flags);
        }
    }

    class AnonymousClass1MenuRunnable implements Runnable {
        private final Activity activity;
        private final int flags;
        private final int identifier;
        boolean returnValue;

        public AnonymousClass1MenuRunnable(Activity _activity, int _identifier, int _flags) {
            this.activity = _activity;
            this.identifier = _identifier;
            this.flags = _flags;
        }

        public void run() {
            this.returnValue = this.activity.getWindow().performPanelIdentifierAction(0, this.identifier, this.flags);
        }
    }

    private final class ActivityGoing implements IdleHandler {
        private final ActivityWaiter mWaiter;

        public ActivityGoing(ActivityWaiter waiter) {
            this.mWaiter = waiter;
        }

        public final boolean queueIdle() {
            synchronized (Instrumentation.this.mSync) {
                Instrumentation.this.mWaitingActivities.remove(this.mWaiter);
                Instrumentation.this.mSync.notifyAll();
            }
            return false;
        }
    }

    public static class ActivityMonitor {
        private final boolean mBlock;
        private final String mClass;
        int mHits;
        Activity mLastActivity;
        private final ActivityResult mResult;
        private final IntentFilter mWhich;

        public ActivityMonitor(IntentFilter which, ActivityResult result, boolean block) {
            this.mHits = 0;
            this.mLastActivity = null;
            this.mWhich = which;
            this.mClass = null;
            this.mResult = result;
            this.mBlock = block;
        }

        public ActivityMonitor(String cls, ActivityResult result, boolean block) {
            this.mHits = 0;
            this.mLastActivity = null;
            this.mWhich = null;
            this.mClass = cls;
            this.mResult = result;
            this.mBlock = block;
        }

        public final IntentFilter getFilter() {
            return this.mWhich;
        }

        public final ActivityResult getResult() {
            return this.mResult;
        }

        public final boolean isBlocking() {
            return this.mBlock;
        }

        public final int getHits() {
            return this.mHits;
        }

        public final Activity getLastActivity() {
            return this.mLastActivity;
        }

        public final Activity waitForActivity() {
            Activity res;
            synchronized (this) {
                while (this.mLastActivity == null) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
                res = this.mLastActivity;
                this.mLastActivity = null;
            }
            return res;
        }

        public final Activity waitForActivityWithTimeout(long timeOut) {
            Activity activity = null;
            synchronized (this) {
                if (this.mLastActivity == null) {
                    try {
                        wait(timeOut);
                    } catch (InterruptedException e) {
                    }
                }
                if (this.mLastActivity == null) {
                } else {
                    activity = this.mLastActivity;
                    this.mLastActivity = null;
                }
            }
            return activity;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        final boolean match(android.content.Context r8, android.app.Activity r9, android.content.Intent r10) {
            /*
            r7 = this;
            r2 = 1;
            r1 = 0;
            monitor-enter(r7);
            r3 = r7.mWhich;	 Catch:{ all -> 0x0033 }
            if (r3 == 0) goto L_0x0018;
        L_0x0007:
            r3 = r7.mWhich;	 Catch:{ all -> 0x0033 }
            r4 = r8.getContentResolver();	 Catch:{ all -> 0x0033 }
            r5 = 1;
            r6 = "Instrumentation";
            r3 = r3.match(r4, r10, r5, r6);	 Catch:{ all -> 0x0033 }
            if (r3 >= 0) goto L_0x0018;
        L_0x0016:
            monitor-exit(r7);	 Catch:{ all -> 0x0033 }
        L_0x0017:
            return r1;
        L_0x0018:
            r3 = r7.mClass;	 Catch:{ all -> 0x0033 }
            if (r3 == 0) goto L_0x0045;
        L_0x001c:
            r0 = 0;
            if (r9 == 0) goto L_0x0036;
        L_0x001f:
            r3 = r9.getClass();	 Catch:{ all -> 0x0033 }
            r0 = r3.getName();	 Catch:{ all -> 0x0033 }
        L_0x0027:
            if (r0 == 0) goto L_0x0031;
        L_0x0029:
            r3 = r7.mClass;	 Catch:{ all -> 0x0033 }
            r3 = r3.equals(r0);	 Catch:{ all -> 0x0033 }
            if (r3 != 0) goto L_0x0045;
        L_0x0031:
            monitor-exit(r7);	 Catch:{ all -> 0x0033 }
            goto L_0x0017;
        L_0x0033:
            r1 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x0033 }
            throw r1;
        L_0x0036:
            r3 = r10.getComponent();	 Catch:{ all -> 0x0033 }
            if (r3 == 0) goto L_0x0027;
        L_0x003c:
            r3 = r10.getComponent();	 Catch:{ all -> 0x0033 }
            r0 = r3.getClassName();	 Catch:{ all -> 0x0033 }
            goto L_0x0027;
        L_0x0045:
            if (r9 == 0) goto L_0x004c;
        L_0x0047:
            r7.mLastActivity = r9;	 Catch:{ all -> 0x0033 }
            r7.notifyAll();	 Catch:{ all -> 0x0033 }
        L_0x004c:
            monitor-exit(r7);	 Catch:{ all -> 0x0033 }
            r1 = r2;
            goto L_0x0017;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.Instrumentation.ActivityMonitor.match(android.content.Context, android.app.Activity, android.content.Intent):boolean");
        }
    }

    public static final class ActivityResult {
        private final int mResultCode;
        private final Intent mResultData;

        public ActivityResult(int resultCode, Intent resultData) {
            this.mResultCode = resultCode;
            this.mResultData = resultData;
        }

        public int getResultCode() {
            return this.mResultCode;
        }

        public Intent getResultData() {
            return this.mResultData;
        }
    }

    private static final class ActivityWaiter {
        public Activity activity;
        public final Intent intent;

        public ActivityWaiter(Intent _intent) {
            this.intent = _intent;
        }
    }

    private static final class EmptyRunnable implements Runnable {
        private EmptyRunnable() {
        }

        public void run() {
        }
    }

    private static final class Idler implements IdleHandler {
        private final Runnable mCallback;
        private boolean mIdle = false;

        public Idler(Runnable callback) {
            this.mCallback = callback;
        }

        public final boolean queueIdle() {
            if (this.mCallback != null) {
                this.mCallback.run();
            }
            synchronized (this) {
                this.mIdle = true;
                notifyAll();
            }
            return false;
        }

        public void waitForIdle() {
            synchronized (this) {
                while (!this.mIdle) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    private final class InstrumentationThread extends Thread {
        public InstrumentationThread(String name) {
            super(name);
        }

        public void run() {
            try {
                Process.setThreadPriority(-8);
            } catch (RuntimeException e) {
                Log.w(Instrumentation.TAG, "Exception setting priority of instrumentation thread " + Process.myTid(), e);
            }
            if (Instrumentation.this.mAutomaticPerformanceSnapshots) {
                Instrumentation.this.startPerformanceSnapshot();
            }
            Instrumentation.this.onStart();
        }
    }

    private static final class SyncRunnable implements Runnable {
        private boolean mComplete;
        private final Runnable mTarget;

        public SyncRunnable(Runnable target) {
            this.mTarget = target;
        }

        public void run() {
            this.mTarget.run();
            synchronized (this) {
                this.mComplete = true;
                notifyAll();
            }
        }

        public void waitForComplete() {
            synchronized (this) {
                while (!this.mComplete) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }

    public void onCreate(Bundle arguments) {
    }

    public void start() {
        if (this.mRunner != null) {
            throw new RuntimeException("Instrumentation already started");
        }
        this.mRunner = new InstrumentationThread("Instr: " + getClass().getName());
        this.mRunner.start();
    }

    public void onStart() {
    }

    public boolean onException(Object obj, Throwable e) {
        return false;
    }

    public void sendStatus(int resultCode, Bundle results) {
        if (this.mWatcher != null) {
            try {
                this.mWatcher.instrumentationStatus(this.mComponent, resultCode, results);
            } catch (RemoteException e) {
                this.mWatcher = null;
            }
        }
    }

    public void finish(int resultCode, Bundle results) {
        if (this.mAutomaticPerformanceSnapshots) {
            endPerformanceSnapshot();
        }
        if (this.mPerfMetrics != null) {
            if (results == null) {
                results = new Bundle();
            }
            results.putAll(this.mPerfMetrics);
        }
        if (this.mUiAutomation != null) {
            this.mUiAutomation.disconnect();
            this.mUiAutomation = null;
        }
        this.mThread.finishInstrumentation(resultCode, results);
    }

    public void setAutomaticPerformanceSnapshots() {
        this.mAutomaticPerformanceSnapshots = true;
        this.mPerformanceCollector = new PerformanceCollector();
    }

    public void startPerformanceSnapshot() {
        if (!isProfiling()) {
            this.mPerformanceCollector.beginSnapshot(null);
        }
    }

    public void endPerformanceSnapshot() {
        if (!isProfiling()) {
            this.mPerfMetrics = this.mPerformanceCollector.endSnapshot();
        }
    }

    public void onDestroy() {
    }

    public Context getContext() {
        return this.mInstrContext;
    }

    public ComponentName getComponentName() {
        return this.mComponent;
    }

    public Context getTargetContext() {
        return this.mAppContext;
    }

    public boolean isProfiling() {
        return this.mThread.isProfiling();
    }

    public void startProfiling() {
        if (this.mThread.isProfiling()) {
            File file = new File(this.mThread.getProfileFilePath());
            file.getParentFile().mkdirs();
            Debug.startMethodTracing(file.toString(), 8388608);
        }
    }

    public void stopProfiling() {
        if (this.mThread.isProfiling()) {
            Debug.stopMethodTracing();
        }
    }

    public void setInTouchMode(boolean inTouch) {
        try {
            Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE)).setInTouchMode(inTouch);
        } catch (RemoteException e) {
        }
    }

    public void waitForIdle(Runnable recipient) {
        this.mMessageQueue.addIdleHandler(new Idler(recipient));
        this.mThread.getHandler().post(new EmptyRunnable());
    }

    public void waitForIdleSync() {
        validateNotAppThread();
        Idler idler = new Idler(null);
        this.mMessageQueue.addIdleHandler(idler);
        this.mThread.getHandler().post(new EmptyRunnable());
        idler.waitForIdle();
    }

    public void runOnMainSync(Runnable runner) {
        validateNotAppThread();
        SyncRunnable sr = new SyncRunnable(runner);
        this.mThread.getHandler().post(sr);
        sr.waitForComplete();
    }

    public Activity startActivitySync(Intent intent) {
        Throwable th;
        validateNotAppThread();
        synchronized (this.mSync) {
            try {
                Intent intent2 = new Intent(intent);
                try {
                    ActivityInfo ai = intent2.resolveActivityInfo(getTargetContext().getPackageManager(), 0);
                    if (ai == null) {
                        throw new RuntimeException("Unable to resolve activity for: " + intent2);
                    }
                    String myProc = this.mThread.getProcessName();
                    if (ai.processName.equals(myProc)) {
                        intent2.setComponent(new ComponentName(ai.applicationInfo.packageName, ai.name));
                        ActivityWaiter aw = new ActivityWaiter(intent2);
                        if (this.mWaitingActivities == null) {
                            this.mWaitingActivities = new ArrayList();
                        }
                        this.mWaitingActivities.add(aw);
                        getTargetContext().startActivity(intent2);
                        do {
                            try {
                                this.mSync.wait();
                            } catch (InterruptedException e) {
                            }
                        } while (this.mWaitingActivities.contains(aw));
                        Activity activity = aw.activity;
                        return activity;
                    }
                    throw new RuntimeException("Intent in process " + myProc + " resolved to different process " + ai.processName + ": " + intent2);
                } catch (Throwable th2) {
                    th = th2;
                    intent = intent2;
                }
            } catch (Throwable th3) {
                th = th3;
                throw th;
            }
        }
    }

    public void addMonitor(ActivityMonitor monitor) {
        synchronized (this.mSync) {
            if (this.mActivityMonitors == null) {
                this.mActivityMonitors = new ArrayList();
            }
            this.mActivityMonitors.add(monitor);
        }
    }

    public ActivityMonitor addMonitor(IntentFilter filter, ActivityResult result, boolean block) {
        ActivityMonitor am = new ActivityMonitor(filter, result, block);
        addMonitor(am);
        return am;
    }

    public ActivityMonitor addMonitor(String cls, ActivityResult result, boolean block) {
        ActivityMonitor am = new ActivityMonitor(cls, result, block);
        addMonitor(am);
        return am;
    }

    public boolean checkMonitorHit(ActivityMonitor monitor, int minHits) {
        waitForIdleSync();
        synchronized (this.mSync) {
            if (monitor.getHits() < minHits) {
                return false;
            }
            this.mActivityMonitors.remove(monitor);
            return true;
        }
    }

    public Activity waitForMonitor(ActivityMonitor monitor) {
        Activity activity = monitor.waitForActivity();
        synchronized (this.mSync) {
            this.mActivityMonitors.remove(monitor);
        }
        return activity;
    }

    public Activity waitForMonitorWithTimeout(ActivityMonitor monitor, long timeOut) {
        Activity activity = monitor.waitForActivityWithTimeout(timeOut);
        synchronized (this.mSync) {
            this.mActivityMonitors.remove(monitor);
        }
        return activity;
    }

    public void removeMonitor(ActivityMonitor monitor) {
        synchronized (this.mSync) {
            this.mActivityMonitors.remove(monitor);
        }
    }

    public boolean invokeMenuActionSync(Activity targetActivity, int id, int flag) {
        AnonymousClass1MenuRunnable mr = new AnonymousClass1MenuRunnable(targetActivity, id, flag);
        runOnMainSync(mr);
        return mr.returnValue;
    }

    public boolean invokeContextMenuAction(Activity targetActivity, int id, int flag) {
        validateNotAppThread();
        sendKeySync(new KeyEvent(0, 23));
        waitForIdleSync();
        try {
            Thread.sleep((long) ViewConfiguration.getLongPressTimeout());
            sendKeySync(new KeyEvent(1, 23));
            waitForIdleSync();
            AnonymousClass1ContextMenuRunnable cmr = new AnonymousClass1ContextMenuRunnable(targetActivity, id, flag);
            runOnMainSync(cmr);
            return cmr.returnValue;
        } catch (InterruptedException e) {
            Log.e(TAG, "Could not sleep for long press timeout", e);
            return false;
        }
    }

    public void sendStringSync(String text) {
        if (text != null) {
            KeyEvent[] events = KeyCharacterMap.load(-1).getEvents(text.toCharArray());
            if (events != null) {
                for (KeyEvent changeTimeRepeat : events) {
                    sendKeySync(KeyEvent.changeTimeRepeat(changeTimeRepeat, SystemClock.uptimeMillis(), 0));
                }
            }
        }
    }

    public void sendKeySync(KeyEvent event) {
        validateNotAppThread();
        long downTime = event.getDownTime();
        long eventTime = event.getEventTime();
        int action = event.getAction();
        int code = event.getKeyCode();
        int repeatCount = event.getRepeatCount();
        int metaState = event.getMetaState();
        int deviceId = event.getDeviceId();
        int scancode = event.getScanCode();
        int source = event.getSource();
        int flags = event.getFlags();
        int displayId = event.getDisplayId();
        if (source == 0) {
            source = 257;
        }
        if (eventTime == 0) {
            eventTime = SystemClock.uptimeMillis();
        }
        if (downTime == 0) {
            downTime = eventTime;
        }
        InputManager.getInstance().injectInputEvent(new KeyEvent(downTime, eventTime, action, code, repeatCount, metaState, deviceId, scancode, flags | 8, source, displayId), 2);
    }

    public void sendKeyDownUpSync(int key) {
        sendKeySync(new KeyEvent(0, key));
        sendKeySync(new KeyEvent(1, key));
    }

    public void sendCharacterSync(int keyCode) {
        sendKeySync(new KeyEvent(0, keyCode));
        sendKeySync(new KeyEvent(1, keyCode));
    }

    public void sendPointerSync(MotionEvent event) {
        validateNotAppThread();
        if ((event.getSource() & 2) == 0) {
            event.setSource(NetworkPolicyManager.POLICY_ALLOW_WHITELIST_IN_ROAMING);
        }
        InputManager.getInstance().injectInputEvent(event, 2);
    }

    public void sendTrackballEventSync(MotionEvent event) {
        validateNotAppThread();
        if ((event.getSource() & 4) == 0) {
            event.setSource(65540);
        }
        InputManager.getInstance().injectInputEvent(event, 2);
    }

    public Application newApplication(ClassLoader cl, String className, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return newApplication(cl.loadClass(className), context);
    }

    public static Application newApplication(Class<?> clazz, Context context) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        Application app = (Application) clazz.newInstance();
        app.attach(context);
        return app;
    }

    public void callApplicationOnCreate(Application app) {
        app.onCreate();
        app.dispatchInjectionManagerGetInstance();
    }

    public Activity newActivity(Class<?> clazz, Context context, IBinder token, Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id, Object lastNonConfigurationInstance) throws InstantiationException, IllegalAccessException {
        Activity activity = (Activity) clazz.newInstance();
        activity.attach(context, null, this, token, 0, application, intent, info, title, parent, id, (NonConfigurationInstances) lastNonConfigurationInstance, new Configuration(), null, null);
        return activity;
    }

    public Activity newActivity(ClassLoader cl, String className, Intent intent) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return (Activity) cl.loadClass(className).newInstance();
    }

    private void prePerformCreate(Activity activity) {
        if (this.mWaitingActivities != null) {
            synchronized (this.mSync) {
                int N = this.mWaitingActivities.size();
                for (int i = 0; i < N; i++) {
                    ActivityWaiter aw = (ActivityWaiter) this.mWaitingActivities.get(i);
                    if (aw.intent.filterEquals(activity.getIntent())) {
                        aw.activity = activity;
                        this.mMessageQueue.addIdleHandler(new ActivityGoing(aw));
                    }
                }
            }
        }
    }

    private void postPerformCreate(Activity activity) {
        if (this.mActivityMonitors != null) {
            synchronized (this.mSync) {
                int N = this.mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    ((ActivityMonitor) this.mActivityMonitors.get(i)).match(activity, activity, activity.getIntent());
                }
            }
        }
    }

    public void callActivityOnCreate(Activity activity, Bundle icicle) {
        prePerformCreate(activity);
        activity.performCreate(icicle);
        postPerformCreate(activity);
    }

    public void callActivityOnCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        prePerformCreate(activity);
        activity.performCreate(icicle, persistentState);
        postPerformCreate(activity);
    }

    public void callActivityOnDestroy(Activity activity) {
        activity.performDestroy();
        if (this.mActivityMonitors != null) {
            synchronized (this.mSync) {
                int N = this.mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    ((ActivityMonitor) this.mActivityMonitors.get(i)).match(activity, activity, activity.getIntent());
                }
            }
        }
    }

    public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState) {
        activity.performRestoreInstanceState(savedInstanceState);
    }

    public void callActivityOnRestoreInstanceState(Activity activity, Bundle savedInstanceState, PersistableBundle persistentState) {
        activity.performRestoreInstanceState(savedInstanceState, persistentState);
    }

    public void callActivityOnPostCreate(Activity activity, Bundle icicle) {
        activity.onPostCreate(icicle);
    }

    public void callActivityOnPostCreate(Activity activity, Bundle icicle, PersistableBundle persistentState) {
        activity.onPostCreate(icicle, persistentState);
    }

    public void callActivityOnNewIntent(Activity activity, Intent intent) {
        activity.onNewIntent(intent);
    }

    public void callActivityOnNewIntent(Activity activity, ReferrerIntent intent) {
        String oldReferrer = activity.mReferrer;
        if (intent != null) {
            try {
                activity.mReferrer = intent.mReferrer;
            } catch (Throwable th) {
                activity.mReferrer = oldReferrer;
            }
        }
        callActivityOnNewIntent(activity, intent != null ? new Intent((Intent) intent) : null);
        activity.mReferrer = oldReferrer;
    }

    public void callActivityOnStart(Activity activity) {
        activity.onStart();
    }

    public void callActivityOnRestart(Activity activity) {
        activity.onRestart();
    }

    public void callActivityOnResume(Activity activity) {
        activity.mResumed = true;
        activity.onResume();
        if (this.mActivityMonitors != null) {
            synchronized (this.mSync) {
                int N = this.mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    ((ActivityMonitor) this.mActivityMonitors.get(i)).match(activity, activity, activity.getIntent());
                }
            }
        }
    }

    public void callActivityOnStop(Activity activity) {
        activity.onStop();
    }

    public void callActivityOnSaveInstanceState(Activity activity, Bundle outState) {
        activity.performSaveInstanceState(outState);
    }

    public void callActivityOnSaveInstanceState(Activity activity, Bundle outState, PersistableBundle outPersistentState) {
        activity.performSaveInstanceState(outState, outPersistentState);
    }

    public void callActivityOnPause(Activity activity) {
        activity.performPause();
    }

    public void callActivityOnUserLeaving(Activity activity) {
        activity.performUserLeaving();
    }

    @Deprecated
    public void startAllocCounting() {
        Runtime.getRuntime().gc();
        Runtime.getRuntime().runFinalization();
        Runtime.getRuntime().gc();
        Debug.resetAllCounts();
        Debug.startAllocCounting();
    }

    @Deprecated
    public void stopAllocCounting() {
        Runtime.getRuntime().gc();
        Runtime.getRuntime().runFinalization();
        Runtime.getRuntime().gc();
        Debug.stopAllocCounting();
    }

    private void addValue(String key, int value, Bundle results) {
        if (results.containsKey(key)) {
            List<Integer> list = results.getIntegerArrayList(key);
            if (list != null) {
                list.add(Integer.valueOf(value));
                return;
            }
            return;
        }
        ArrayList<Integer> list2 = new ArrayList();
        list2.add(Integer.valueOf(value));
        results.putIntegerArrayList(key, list2);
    }

    public Bundle getAllocCounts() {
        Bundle results = new Bundle();
        results.putLong(PerformanceCollector.METRIC_KEY_GLOBAL_ALLOC_COUNT, (long) Debug.getGlobalAllocCount());
        results.putLong(PerformanceCollector.METRIC_KEY_GLOBAL_ALLOC_SIZE, (long) Debug.getGlobalAllocSize());
        results.putLong(PerformanceCollector.METRIC_KEY_GLOBAL_FREED_COUNT, (long) Debug.getGlobalFreedCount());
        results.putLong(PerformanceCollector.METRIC_KEY_GLOBAL_FREED_SIZE, (long) Debug.getGlobalFreedSize());
        results.putLong(PerformanceCollector.METRIC_KEY_GC_INVOCATION_COUNT, (long) Debug.getGlobalGcInvocationCount());
        return results;
    }

    public Bundle getBinderCounts() {
        Bundle results = new Bundle();
        results.putLong(PerformanceCollector.METRIC_KEY_SENT_TRANSACTIONS, (long) Debug.getBinderSentTransactions());
        results.putLong(PerformanceCollector.METRIC_KEY_RECEIVED_TRANSACTIONS, (long) Debug.getBinderReceivedTransactions());
        return results;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.app.Instrumentation.ActivityResult execStartActivity(android.content.Context r20, android.os.IBinder r21, android.os.IBinder r22, android.app.Activity r23, android.content.Intent r24, int r25, android.os.Bundle r26) {
        /*
        r19 = this;
        r3 = r21;
        r3 = (android.app.IApplicationThread) r3;
        if (r23 == 0) goto L_0x0059;
    L_0x0006:
        r17 = r23.onProvideReferrer();
    L_0x000a:
        if (r17 == 0) goto L_0x0015;
    L_0x000c:
        r2 = "android.intent.extra.REFERRER";
        r0 = r24;
        r1 = r17;
        r0.putExtra(r2, r1);
    L_0x0015:
        r0 = r19;
        r2 = r0.mActivityMonitors;
        if (r2 == 0) goto L_0x0062;
    L_0x001b:
        r0 = r19;
        r4 = r0.mSync;
        monitor-enter(r4);
        r0 = r19;
        r2 = r0.mActivityMonitors;	 Catch:{ all -> 0x0097 }
        r13 = r2.size();	 Catch:{ all -> 0x0097 }
        r16 = 0;
    L_0x002a:
        r0 = r16;
        if (r0 >= r13) goto L_0x0061;
    L_0x002e:
        r0 = r19;
        r2 = r0.mActivityMonitors;	 Catch:{ all -> 0x0097 }
        r0 = r16;
        r14 = r2.get(r0);	 Catch:{ all -> 0x0097 }
        r14 = (android.app.Instrumentation.ActivityMonitor) r14;	 Catch:{ all -> 0x0097 }
        r2 = 0;
        r0 = r20;
        r1 = r24;
        r2 = r14.match(r0, r2, r1);	 Catch:{ all -> 0x0097 }
        if (r2 == 0) goto L_0x005e;
    L_0x0045:
        r2 = r14.mHits;	 Catch:{ all -> 0x0097 }
        r2 = r2 + 1;
        r14.mHits = r2;	 Catch:{ all -> 0x0097 }
        r2 = r14.isBlocking();	 Catch:{ all -> 0x0097 }
        if (r2 == 0) goto L_0x0061;
    L_0x0051:
        if (r25 < 0) goto L_0x005c;
    L_0x0053:
        r2 = r14.getResult();	 Catch:{ all -> 0x0097 }
    L_0x0057:
        monitor-exit(r4);	 Catch:{ all -> 0x0097 }
    L_0x0058:
        return r2;
    L_0x0059:
        r17 = 0;
        goto L_0x000a;
    L_0x005c:
        r2 = 0;
        goto L_0x0057;
    L_0x005e:
        r16 = r16 + 1;
        goto L_0x002a;
    L_0x0061:
        monitor-exit(r4);	 Catch:{ all -> 0x0097 }
    L_0x0062:
        r24.migrateExtraStreamToClipData();	 Catch:{ RemoteException -> 0x009c }
        r24.prepareToLeaveProcess();	 Catch:{ RemoteException -> 0x009c }
        r2 = android.app.ActivityManagerNative.getDefault();	 Catch:{ RemoteException -> 0x009c }
        r4 = r20.getBasePackageName();	 Catch:{ RemoteException -> 0x009c }
        r5 = r20.getContentResolver();	 Catch:{ RemoteException -> 0x009c }
        r0 = r24;
        r6 = r0.resolveTypeIfNeeded(r5);	 Catch:{ RemoteException -> 0x009c }
        if (r23 == 0) goto L_0x009a;
    L_0x007c:
        r0 = r23;
        r8 = r0.mEmbeddedID;	 Catch:{ RemoteException -> 0x009c }
    L_0x0080:
        r10 = 0;
        r11 = 0;
        r5 = r24;
        r7 = r22;
        r9 = r25;
        r12 = r26;
        r18 = r2.startActivity(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12);	 Catch:{ RemoteException -> 0x009c }
        r0 = r18;
        r1 = r24;
        checkStartActivityResult(r0, r1);	 Catch:{ RemoteException -> 0x009c }
        r2 = 0;
        goto L_0x0058;
    L_0x0097:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0097 }
        throw r2;
    L_0x009a:
        r8 = 0;
        goto L_0x0080;
    L_0x009c:
        r15 = move-exception;
        r2 = new java.lang.RuntimeException;
        r4 = "Failure from system";
        r2.<init>(r4, r15);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Instrumentation.execStartActivity(android.content.Context, android.os.IBinder, android.os.IBinder, android.app.Activity, android.content.Intent, int, android.os.Bundle):android.app.Instrumentation$ActivityResult");
    }

    public void execStartActivities(Context who, IBinder contextThread, IBinder token, Activity target, Intent[] intents, Bundle options) {
        execStartActivitiesAsUser(who, contextThread, token, target, intents, options, UserHandle.myUserId());
    }

    public void execStartActivitiesAsUser(Context who, IBinder contextThread, IBinder token, Activity target, Intent[] intents, Bundle options, int userId) {
        int i;
        IApplicationThread whoThread = (IApplicationThread) contextThread;
        if (this.mActivityMonitors != null) {
            synchronized (this.mSync) {
                int N = this.mActivityMonitors.size();
                for (i = 0; i < N; i++) {
                    ActivityMonitor am = (ActivityMonitor) this.mActivityMonitors.get(i);
                    if (am.match(who, null, intents[0])) {
                        am.mHits++;
                        if (am.isBlocking()) {
                            return;
                        }
                    }
                }
            }
        }
        try {
            String[] resolvedTypes = new String[intents.length];
            for (i = 0; i < intents.length; i++) {
                intents[i].migrateExtraStreamToClipData();
                intents[i].prepareToLeaveProcess();
                resolvedTypes[i] = intents[i].resolveTypeIfNeeded(who.getContentResolver());
            }
            checkStartActivityResult(ActivityManagerNative.getDefault().startActivities(whoThread, who.getBasePackageName(), intents, resolvedTypes, token, options, userId), intents[0]);
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.app.Instrumentation.ActivityResult execStartActivity(android.content.Context r19, android.os.IBinder r20, android.os.IBinder r21, java.lang.String r22, android.content.Intent r23, int r24, android.os.Bundle r25) {
        /*
        r18 = this;
        r3 = r20;
        r3 = (android.app.IApplicationThread) r3;
        r0 = r18;
        r2 = r0.mActivityMonitors;
        if (r2 == 0) goto L_0x004e;
    L_0x000a:
        r0 = r18;
        r4 = r0.mSync;
        monitor-enter(r4);
        r0 = r18;
        r2 = r0.mActivityMonitors;	 Catch:{ all -> 0x007f }
        r13 = r2.size();	 Catch:{ all -> 0x007f }
        r16 = 0;
    L_0x0019:
        r0 = r16;
        if (r0 >= r13) goto L_0x004d;
    L_0x001d:
        r0 = r18;
        r2 = r0.mActivityMonitors;	 Catch:{ all -> 0x007f }
        r0 = r16;
        r14 = r2.get(r0);	 Catch:{ all -> 0x007f }
        r14 = (android.app.Instrumentation.ActivityMonitor) r14;	 Catch:{ all -> 0x007f }
        r2 = 0;
        r0 = r19;
        r1 = r23;
        r2 = r14.match(r0, r2, r1);	 Catch:{ all -> 0x007f }
        if (r2 == 0) goto L_0x004a;
    L_0x0034:
        r2 = r14.mHits;	 Catch:{ all -> 0x007f }
        r2 = r2 + 1;
        r14.mHits = r2;	 Catch:{ all -> 0x007f }
        r2 = r14.isBlocking();	 Catch:{ all -> 0x007f }
        if (r2 == 0) goto L_0x004d;
    L_0x0040:
        if (r24 < 0) goto L_0x0048;
    L_0x0042:
        r2 = r14.getResult();	 Catch:{ all -> 0x007f }
    L_0x0046:
        monitor-exit(r4);	 Catch:{ all -> 0x007f }
    L_0x0047:
        return r2;
    L_0x0048:
        r2 = 0;
        goto L_0x0046;
    L_0x004a:
        r16 = r16 + 1;
        goto L_0x0019;
    L_0x004d:
        monitor-exit(r4);	 Catch:{ all -> 0x007f }
    L_0x004e:
        r23.migrateExtraStreamToClipData();	 Catch:{ RemoteException -> 0x0082 }
        r23.prepareToLeaveProcess();	 Catch:{ RemoteException -> 0x0082 }
        r2 = android.app.ActivityManagerNative.getDefault();	 Catch:{ RemoteException -> 0x0082 }
        r4 = r19.getBasePackageName();	 Catch:{ RemoteException -> 0x0082 }
        r5 = r19.getContentResolver();	 Catch:{ RemoteException -> 0x0082 }
        r0 = r23;
        r6 = r0.resolveTypeIfNeeded(r5);	 Catch:{ RemoteException -> 0x0082 }
        r10 = 0;
        r11 = 0;
        r5 = r23;
        r7 = r21;
        r8 = r22;
        r9 = r24;
        r12 = r25;
        r17 = r2.startActivity(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12);	 Catch:{ RemoteException -> 0x0082 }
        r0 = r17;
        r1 = r23;
        checkStartActivityResult(r0, r1);	 Catch:{ RemoteException -> 0x0082 }
        r2 = 0;
        goto L_0x0047;
    L_0x007f:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x007f }
        throw r2;
    L_0x0082:
        r15 = move-exception;
        r2 = new java.lang.RuntimeException;
        r4 = "Failure from system";
        r2.<init>(r4, r15);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Instrumentation.execStartActivity(android.content.Context, android.os.IBinder, android.os.IBinder, java.lang.String, android.content.Intent, int, android.os.Bundle):android.app.Instrumentation$ActivityResult");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.app.Instrumentation.ActivityResult execStartActivity(android.content.Context r20, android.os.IBinder r21, android.os.IBinder r22, android.app.Activity r23, android.content.Intent r24, int r25, android.os.Bundle r26, android.os.UserHandle r27) {
        /*
        r19 = this;
        r3 = r21;
        r3 = (android.app.IApplicationThread) r3;
        r0 = r19;
        r2 = r0.mActivityMonitors;
        if (r2 == 0) goto L_0x004e;
    L_0x000a:
        r0 = r19;
        r4 = r0.mSync;
        monitor-enter(r4);
        r0 = r19;
        r2 = r0.mActivityMonitors;	 Catch:{ all -> 0x0087 }
        r14 = r2.size();	 Catch:{ all -> 0x0087 }
        r17 = 0;
    L_0x0019:
        r0 = r17;
        if (r0 >= r14) goto L_0x004d;
    L_0x001d:
        r0 = r19;
        r2 = r0.mActivityMonitors;	 Catch:{ all -> 0x0087 }
        r0 = r17;
        r15 = r2.get(r0);	 Catch:{ all -> 0x0087 }
        r15 = (android.app.Instrumentation.ActivityMonitor) r15;	 Catch:{ all -> 0x0087 }
        r2 = 0;
        r0 = r20;
        r1 = r24;
        r2 = r15.match(r0, r2, r1);	 Catch:{ all -> 0x0087 }
        if (r2 == 0) goto L_0x004a;
    L_0x0034:
        r2 = r15.mHits;	 Catch:{ all -> 0x0087 }
        r2 = r2 + 1;
        r15.mHits = r2;	 Catch:{ all -> 0x0087 }
        r2 = r15.isBlocking();	 Catch:{ all -> 0x0087 }
        if (r2 == 0) goto L_0x004d;
    L_0x0040:
        if (r25 < 0) goto L_0x0048;
    L_0x0042:
        r2 = r15.getResult();	 Catch:{ all -> 0x0087 }
    L_0x0046:
        monitor-exit(r4);	 Catch:{ all -> 0x0087 }
    L_0x0047:
        return r2;
    L_0x0048:
        r2 = 0;
        goto L_0x0046;
    L_0x004a:
        r17 = r17 + 1;
        goto L_0x0019;
    L_0x004d:
        monitor-exit(r4);	 Catch:{ all -> 0x0087 }
    L_0x004e:
        r24.migrateExtraStreamToClipData();	 Catch:{ RemoteException -> 0x008c }
        r24.prepareToLeaveProcess();	 Catch:{ RemoteException -> 0x008c }
        r2 = android.app.ActivityManagerNative.getDefault();	 Catch:{ RemoteException -> 0x008c }
        r4 = r20.getBasePackageName();	 Catch:{ RemoteException -> 0x008c }
        r5 = r20.getContentResolver();	 Catch:{ RemoteException -> 0x008c }
        r0 = r24;
        r6 = r0.resolveTypeIfNeeded(r5);	 Catch:{ RemoteException -> 0x008c }
        if (r23 == 0) goto L_0x008a;
    L_0x0068:
        r0 = r23;
        r8 = r0.mEmbeddedID;	 Catch:{ RemoteException -> 0x008c }
    L_0x006c:
        r10 = 0;
        r11 = 0;
        r13 = r27.getIdentifier();	 Catch:{ RemoteException -> 0x008c }
        r5 = r24;
        r7 = r22;
        r9 = r25;
        r12 = r26;
        r18 = r2.startActivityAsUser(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13);	 Catch:{ RemoteException -> 0x008c }
        r0 = r18;
        r1 = r24;
        checkStartActivityResult(r0, r1);	 Catch:{ RemoteException -> 0x008c }
        r2 = 0;
        goto L_0x0047;
    L_0x0087:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0087 }
        throw r2;
    L_0x008a:
        r8 = 0;
        goto L_0x006c;
    L_0x008c:
        r16 = move-exception;
        r2 = new java.lang.RuntimeException;
        r4 = "Failure from system";
        r0 = r16;
        r2.<init>(r4, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Instrumentation.execStartActivity(android.content.Context, android.os.IBinder, android.os.IBinder, android.app.Activity, android.content.Intent, int, android.os.Bundle, android.os.UserHandle):android.app.Instrumentation$ActivityResult");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.app.Instrumentation.ActivityResult execStartActivityAsCaller(android.content.Context r22, android.os.IBinder r23, android.os.IBinder r24, android.app.Activity r25, android.content.Intent r26, int r27, android.os.Bundle r28, boolean r29, int r30) {
        /*
        r21 = this;
        r4 = r23;
        r4 = (android.app.IApplicationThread) r4;
        r0 = r21;
        r3 = r0.mActivityMonitors;
        if (r3 == 0) goto L_0x0056;
    L_0x000a:
        r0 = r21;
        r5 = r0.mSync;
        monitor-enter(r5);
        r0 = r21;
        r3 = r0.mActivityMonitors;	 Catch:{ all -> 0x008f }
        r16 = r3.size();	 Catch:{ all -> 0x008f }
        r19 = 0;
    L_0x0019:
        r0 = r19;
        r1 = r16;
        if (r0 >= r1) goto L_0x0055;
    L_0x001f:
        r0 = r21;
        r3 = r0.mActivityMonitors;	 Catch:{ all -> 0x008f }
        r0 = r19;
        r17 = r3.get(r0);	 Catch:{ all -> 0x008f }
        r17 = (android.app.Instrumentation.ActivityMonitor) r17;	 Catch:{ all -> 0x008f }
        r3 = 0;
        r0 = r17;
        r1 = r22;
        r2 = r26;
        r3 = r0.match(r1, r3, r2);	 Catch:{ all -> 0x008f }
        if (r3 == 0) goto L_0x0052;
    L_0x0038:
        r0 = r17;
        r3 = r0.mHits;	 Catch:{ all -> 0x008f }
        r3 = r3 + 1;
        r0 = r17;
        r0.mHits = r3;	 Catch:{ all -> 0x008f }
        r3 = r17.isBlocking();	 Catch:{ all -> 0x008f }
        if (r3 == 0) goto L_0x0055;
    L_0x0048:
        if (r27 < 0) goto L_0x0050;
    L_0x004a:
        r3 = r17.getResult();	 Catch:{ all -> 0x008f }
    L_0x004e:
        monitor-exit(r5);	 Catch:{ all -> 0x008f }
    L_0x004f:
        return r3;
    L_0x0050:
        r3 = 0;
        goto L_0x004e;
    L_0x0052:
        r19 = r19 + 1;
        goto L_0x0019;
    L_0x0055:
        monitor-exit(r5);	 Catch:{ all -> 0x008f }
    L_0x0056:
        r26.migrateExtraStreamToClipData();	 Catch:{ RemoteException -> 0x0094 }
        r26.prepareToLeaveProcess();	 Catch:{ RemoteException -> 0x0094 }
        r3 = android.app.ActivityManagerNative.getDefault();	 Catch:{ RemoteException -> 0x0094 }
        r5 = r22.getBasePackageName();	 Catch:{ RemoteException -> 0x0094 }
        r6 = r22.getContentResolver();	 Catch:{ RemoteException -> 0x0094 }
        r0 = r26;
        r7 = r0.resolveTypeIfNeeded(r6);	 Catch:{ RemoteException -> 0x0094 }
        if (r25 == 0) goto L_0x0092;
    L_0x0070:
        r0 = r25;
        r9 = r0.mEmbeddedID;	 Catch:{ RemoteException -> 0x0094 }
    L_0x0074:
        r11 = 0;
        r12 = 0;
        r6 = r26;
        r8 = r24;
        r10 = r27;
        r13 = r28;
        r14 = r29;
        r15 = r30;
        r20 = r3.startActivityAsCaller(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15);	 Catch:{ RemoteException -> 0x0094 }
        r0 = r20;
        r1 = r26;
        checkStartActivityResult(r0, r1);	 Catch:{ RemoteException -> 0x0094 }
        r3 = 0;
        goto L_0x004f;
    L_0x008f:
        r3 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x008f }
        throw r3;
    L_0x0092:
        r9 = 0;
        goto L_0x0074;
    L_0x0094:
        r18 = move-exception;
        r3 = new java.lang.RuntimeException;
        r5 = "Failure from system";
        r0 = r18;
        r3.<init>(r5, r0);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Instrumentation.execStartActivityAsCaller(android.content.Context, android.os.IBinder, android.os.IBinder, android.app.Activity, android.content.Intent, int, android.os.Bundle, boolean, int):android.app.Instrumentation$ActivityResult");
    }

    public void execStartActivityFromAppTask(Context who, IBinder contextThread, IAppTask appTask, Intent intent, Bundle options) {
        IApplicationThread whoThread = (IApplicationThread) contextThread;
        if (this.mActivityMonitors != null) {
            synchronized (this.mSync) {
                int N = this.mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    ActivityMonitor am = (ActivityMonitor) this.mActivityMonitors.get(i);
                    if (am.match(who, null, intent)) {
                        am.mHits++;
                        if (am.isBlocking()) {
                            return;
                        }
                    }
                }
            }
        }
        try {
            intent.migrateExtraStreamToClipData();
            intent.prepareToLeaveProcess();
            checkStartActivityResult(appTask.startActivity(whoThread.asBinder(), who.getBasePackageName(), intent, intent.resolveTypeIfNeeded(who.getContentResolver()), options), intent);
        } catch (RemoteException e) {
            throw new RuntimeException("Failure from system", e);
        }
    }

    final void init(ActivityThread thread, Context instrContext, Context appContext, ComponentName component, IInstrumentationWatcher watcher, IUiAutomationConnection uiAutomationConnection) {
        this.mThread = thread;
        this.mThread.getLooper();
        this.mMessageQueue = Looper.myQueue();
        this.mInstrContext = instrContext;
        this.mAppContext = appContext;
        this.mComponent = component;
        this.mWatcher = watcher;
        this.mUiAutomationConnection = uiAutomationConnection;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void checkStartActivityResult(int r6, java.lang.Object r7) {
        /*
        if (r6 < 0) goto L_0x0003;
    L_0x0002:
        return;
    L_0x0003:
        switch(r6) {
            case -9: goto L_0x0002;
            case -8: goto L_0x00f6;
            case -7: goto L_0x00dd;
            case -6: goto L_0x0006;
            case -5: goto L_0x00d5;
            case -4: goto L_0x00b4;
            case -3: goto L_0x00cd;
            case -2: goto L_0x0029;
            case -1: goto L_0x0029;
            default: goto L_0x0006;
        };
    L_0x0006:
        r3 = new android.util.AndroidRuntimeException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Unknown error code ";
        r4 = r4.append(r5);
        r4 = r4.append(r6);
        r5 = " when starting ";
        r4 = r4.append(r5);
        r4 = r4.append(r7);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
    L_0x0029:
        r3 = r7 instanceof android.content.Intent;
        if (r3 == 0) goto L_0x005f;
    L_0x002d:
        r3 = r7;
        r3 = (android.content.Intent) r3;
        r3 = r3.getComponent();
        if (r3 == 0) goto L_0x005f;
    L_0x0036:
        r3 = new android.content.ActivityNotFoundException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Unable to find explicit activity class ";
        r4 = r4.append(r5);
        r7 = (android.content.Intent) r7;
        r5 = r7.getComponent();
        r5 = r5.toShortString();
        r4 = r4.append(r5);
        r5 = "; have you declared this activity in your AndroidManifest.xml?";
        r4 = r4.append(r5);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
    L_0x005f:
        r3 = "Instrumentation";
        r4 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00af }
        r4.<init>();	 Catch:{ Exception -> 0x00af }
        r5 = "checkStartActivityResult() : ";
        r4 = r4.append(r5);	 Catch:{ Exception -> 0x00af }
        r4 = r4.append(r7);	 Catch:{ Exception -> 0x00af }
        r4 = r4.toString();	 Catch:{ Exception -> 0x00af }
        android.util.Log.d(r3, r4);	 Catch:{ Exception -> 0x00af }
        r3 = r7 instanceof android.content.Intent;	 Catch:{ Exception -> 0x00af }
        if (r3 == 0) goto L_0x0096;
    L_0x007b:
        r3 = "Instrumentation";
        r4 = "checkStartActivityResult() : intent is instance of [Intent].";
        android.util.Log.d(r3, r4);	 Catch:{ Exception -> 0x00af }
        r3 = android.sec.enterprise.EnterpriseDeviceManager.getInstance();	 Catch:{ Exception -> 0x00af }
        r1 = r3.getApplicationPolicy();	 Catch:{ Exception -> 0x00af }
        if (r1 == 0) goto L_0x0096;
    L_0x008c:
        r0 = r7;
        r0 = (android.content.Intent) r0;	 Catch:{ Exception -> 0x00af }
        r3 = r0;
        r3 = r1.isIntentDisabled(r3);	 Catch:{ Exception -> 0x00af }
        if (r3 == 0) goto L_0x0096;
    L_0x0096:
        r3 = new android.content.ActivityNotFoundException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "No Activity found to handle ";
        r4 = r4.append(r5);
        r4 = r4.append(r7);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
    L_0x00af:
        r2 = move-exception;
        r2.printStackTrace();
        goto L_0x0096;
    L_0x00b4:
        r3 = new java.lang.SecurityException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Not allowed to start activity ";
        r4 = r4.append(r5);
        r4 = r4.append(r7);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
    L_0x00cd:
        r3 = new android.util.AndroidRuntimeException;
        r4 = "FORWARD_RESULT_FLAG used while also requesting a result";
        r3.<init>(r4);
        throw r3;
    L_0x00d5:
        r3 = new java.lang.IllegalArgumentException;
        r4 = "PendingIntent is not an activity";
        r3.<init>(r4);
        throw r3;
    L_0x00dd:
        r3 = new java.lang.SecurityException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Starting under voice control not allowed for: ";
        r4 = r4.append(r5);
        r4 = r4.append(r7);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
    L_0x00f6:
        r3 = "Instrumentation";
        r4 = "Not allowed to start background user activity that shouldn't be displayed for all users. Failing silently...";
        android.util.Log.e(r3, r4);
        goto L_0x0002;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Instrumentation.checkStartActivityResult(int, java.lang.Object):void");
    }

    private final void validateNotAppThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("This method can not be called from the main application thread");
        }
    }

    public UiAutomation getUiAutomation() {
        if (this.mUiAutomationConnection == null) {
            return null;
        }
        if (this.mUiAutomation == null) {
            this.mUiAutomation = new UiAutomation(getTargetContext().getMainLooper(), this.mUiAutomationConnection);
            this.mUiAutomation.connect();
        }
        return this.mUiAutomation;
    }
}
