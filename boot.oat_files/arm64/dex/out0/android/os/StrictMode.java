package android.os;

import android.animation.ValueAnimator;
import android.app.ActivityManagerNative;
import android.app.ActivityThread;
import android.app.ApplicationErrorReport.CrashInfo;
import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.MessageQueue.IdleHandler;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Printer;
import android.util.Singleton;
import android.util.Slog;
import android.view.IWindowManager;
import android.view.IWindowManager.Stub;
import com.android.internal.os.RuntimeInit;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.HexDump;
import dalvik.system.BlockGuard;
import dalvik.system.BlockGuard.BlockGuardPolicyException;
import dalvik.system.BlockGuard.Policy;
import dalvik.system.CloseGuard;
import dalvik.system.CloseGuard.Reporter;
import dalvik.system.VMDebug;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

public final class StrictMode {
    private static final int ALL_THREAD_DETECT_BITS = 31;
    private static final int ALL_VM_DETECT_BITS = 32512;
    private static final String CLEARTEXT_PROPERTY = "persist.sys.strictmode.clear";
    public static final int DETECT_CUSTOM = 8;
    public static final int DETECT_DISK_READ = 2;
    public static final int DETECT_DISK_WRITE = 1;
    public static final int DETECT_NETWORK = 4;
    public static final int DETECT_RESOURCE_MISMATCH = 16;
    public static final int DETECT_VM_ACTIVITY_LEAKS = 1024;
    private static final int DETECT_VM_CLEARTEXT_NETWORK = 16384;
    public static final int DETECT_VM_CLOSABLE_LEAKS = 512;
    public static final int DETECT_VM_CURSOR_LEAKS = 256;
    private static final int DETECT_VM_FILE_URI_EXPOSURE = 8192;
    private static final int DETECT_VM_INSTANCE_LEAKS = 2048;
    public static final int DETECT_VM_REGISTRATION_LEAKS = 4096;
    public static final String DISABLE_PROPERTY = "persist.sys.strictmode.disable";
    private static final HashMap<Class, Integer> EMPTY_CLASS_LIMIT_MAP = new HashMap();
    private static final boolean IS_ENG_BUILD = "eng".equals(Build.TYPE);
    private static final boolean IS_USER_BUILD = Context.USER_SERVICE.equals(Build.TYPE);
    private static final boolean LOG_V = Log.isLoggable(TAG, 2);
    private static final int MAX_OFFENSES_PER_LOOP = 10;
    private static final int MAX_SPAN_TAGS = 20;
    private static final long MIN_DIALOG_INTERVAL_MS = 30000;
    private static final long MIN_LOG_INTERVAL_MS = 1000;
    public static final int NETWORK_POLICY_ACCEPT = 0;
    public static final int NETWORK_POLICY_LOG = 1;
    public static final int NETWORK_POLICY_REJECT = 2;
    private static final Span NO_OP_SPAN = new Span() {
        public void finish() {
        }
    };
    public static final int PENALTY_DEATH = 262144;
    public static final int PENALTY_DEATH_ON_CLEARTEXT_NETWORK = 8388608;
    public static final int PENALTY_DEATH_ON_NETWORK = 524288;
    public static final int PENALTY_DIALOG = 131072;
    public static final int PENALTY_DROPBOX = 2097152;
    public static final int PENALTY_FLASH = 1048576;
    public static final int PENALTY_GATHER = 4194304;
    public static final int PENALTY_LOG = 65536;
    private static final String TAG = "StrictMode";
    private static final int THREAD_PENALTY_MASK = 8323072;
    public static final String VISUAL_PROPERTY = "persist.sys.strictmode.visual";
    private static final int VM_PENALTY_MASK = 10813440;
    private static final ThreadLocal<ArrayList<ViolationInfo>> gatheredViolations = new ThreadLocal<ArrayList<ViolationInfo>>() {
        protected ArrayList<ViolationInfo> initialValue() {
            return null;
        }
    };
    private static final AtomicInteger sDropboxCallsInFlight = new AtomicInteger(0);
    private static final HashMap<Class, Integer> sExpectedActivityInstanceCount = new HashMap();
    private static boolean sIsIdlerRegistered = false;
    private static long sLastInstanceCountCheckMillis = 0;
    private static final HashMap<Integer, Long> sLastVmViolationTime = new HashMap();
    private static final IdleHandler sProcessIdleHandler = new IdleHandler() {
        public boolean queueIdle() {
            long now = SystemClock.uptimeMillis();
            if (now - StrictMode.sLastInstanceCountCheckMillis > 30000) {
                StrictMode.sLastInstanceCountCheckMillis = now;
                StrictMode.conditionallyCheckInstanceCounts();
            }
            return true;
        }
    };
    private static final ThreadLocal<ThreadSpanState> sThisThreadSpanState = new ThreadLocal<ThreadSpanState>() {
        protected ThreadSpanState initialValue() {
            return new ThreadSpanState();
        }
    };
    private static volatile VmPolicy sVmPolicy = VmPolicy.LAX;
    private static volatile int sVmPolicyMask = 0;
    private static Singleton<IWindowManager> sWindowManager = new Singleton<IWindowManager>() {
        protected IWindowManager create() {
            return Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
        }
    };
    private static final ThreadLocal<AndroidBlockGuardPolicy> threadAndroidPolicy = new ThreadLocal<AndroidBlockGuardPolicy>() {
        protected AndroidBlockGuardPolicy initialValue() {
            return new AndroidBlockGuardPolicy(0);
        }
    };
    private static final ThreadLocal<Handler> threadHandler = new ThreadLocal<Handler>() {
        protected Handler initialValue() {
            return new Handler();
        }
    };
    private static final ThreadLocal<ArrayList<ViolationInfo>> violationsBeingTimed = new ThreadLocal<ArrayList<ViolationInfo>>() {
        protected ArrayList<ViolationInfo> initialValue() {
            return new ArrayList();
        }
    };

    public static class Span {
        private final ThreadSpanState mContainerState;
        private long mCreateMillis;
        private String mName;
        private Span mNext;
        private Span mPrev;

        Span(ThreadSpanState threadState) {
            this.mContainerState = threadState;
        }

        protected Span() {
            this.mContainerState = null;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void finish() {
            /*
            r4 = this;
            r0 = r4.mContainerState;
            monitor-enter(r0);
            r1 = r4.mName;	 Catch:{ all -> 0x0077 }
            if (r1 != 0) goto L_0x0009;
        L_0x0007:
            monitor-exit(r0);	 Catch:{ all -> 0x0077 }
        L_0x0008:
            return;
        L_0x0009:
            r1 = r4.mPrev;	 Catch:{ all -> 0x0077 }
            if (r1 == 0) goto L_0x0013;
        L_0x000d:
            r1 = r4.mPrev;	 Catch:{ all -> 0x0077 }
            r2 = r4.mNext;	 Catch:{ all -> 0x0077 }
            r1.mNext = r2;	 Catch:{ all -> 0x0077 }
        L_0x0013:
            r1 = r4.mNext;	 Catch:{ all -> 0x0077 }
            if (r1 == 0) goto L_0x001d;
        L_0x0017:
            r1 = r4.mNext;	 Catch:{ all -> 0x0077 }
            r2 = r4.mPrev;	 Catch:{ all -> 0x0077 }
            r1.mPrev = r2;	 Catch:{ all -> 0x0077 }
        L_0x001d:
            r1 = r0.mActiveHead;	 Catch:{ all -> 0x0077 }
            if (r1 != r4) goto L_0x0025;
        L_0x0021:
            r1 = r4.mNext;	 Catch:{ all -> 0x0077 }
            r0.mActiveHead = r1;	 Catch:{ all -> 0x0077 }
        L_0x0025:
            r1 = r0.mActiveSize;	 Catch:{ all -> 0x0077 }
            r1 = r1 + -1;
            r0.mActiveSize = r1;	 Catch:{ all -> 0x0077 }
            r1 = android.os.StrictMode.LOG_V;	 Catch:{ all -> 0x0077 }
            if (r1 == 0) goto L_0x0057;
        L_0x0031:
            r1 = "StrictMode";
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0077 }
            r2.<init>();	 Catch:{ all -> 0x0077 }
            r3 = "Span finished=";
            r2 = r2.append(r3);	 Catch:{ all -> 0x0077 }
            r3 = r4.mName;	 Catch:{ all -> 0x0077 }
            r2 = r2.append(r3);	 Catch:{ all -> 0x0077 }
            r3 = "; size=";
            r2 = r2.append(r3);	 Catch:{ all -> 0x0077 }
            r3 = r0.mActiveSize;	 Catch:{ all -> 0x0077 }
            r2 = r2.append(r3);	 Catch:{ all -> 0x0077 }
            r2 = r2.toString();	 Catch:{ all -> 0x0077 }
            android.util.Log.d(r1, r2);	 Catch:{ all -> 0x0077 }
        L_0x0057:
            r2 = -1;
            r4.mCreateMillis = r2;	 Catch:{ all -> 0x0077 }
            r1 = 0;
            r4.mName = r1;	 Catch:{ all -> 0x0077 }
            r1 = 0;
            r4.mPrev = r1;	 Catch:{ all -> 0x0077 }
            r1 = 0;
            r4.mNext = r1;	 Catch:{ all -> 0x0077 }
            r1 = r0.mFreeListSize;	 Catch:{ all -> 0x0077 }
            r2 = 5;
            if (r1 >= r2) goto L_0x0075;
        L_0x0069:
            r1 = r0.mFreeListHead;	 Catch:{ all -> 0x0077 }
            r4.mNext = r1;	 Catch:{ all -> 0x0077 }
            r0.mFreeListHead = r4;	 Catch:{ all -> 0x0077 }
            r1 = r0.mFreeListSize;	 Catch:{ all -> 0x0077 }
            r1 = r1 + 1;
            r0.mFreeListSize = r1;	 Catch:{ all -> 0x0077 }
        L_0x0075:
            monitor-exit(r0);	 Catch:{ all -> 0x0077 }
            goto L_0x0008;
        L_0x0077:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0077 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.os.StrictMode.Span.finish():void");
        }
    }

    private static class AndroidBlockGuardPolicy implements Policy {
        private ArrayMap<Integer, Long> mLastViolationTime;
        private int mPolicyMask;

        public AndroidBlockGuardPolicy(int policyMask) {
            this.mPolicyMask = policyMask;
        }

        public String toString() {
            return "AndroidBlockGuardPolicy; mPolicyMask=" + this.mPolicyMask;
        }

        public int getPolicyMask() {
            return this.mPolicyMask;
        }

        public void onWriteToDisk() {
            if ((this.mPolicyMask & 1) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                BlockGuardPolicyException e = new StrictModeDiskWriteViolation(this.mPolicyMask);
                e.fillInStackTrace();
                startHandlingViolationException(e);
            }
        }

        void onCustomSlowCall(String name) {
            if ((this.mPolicyMask & 8) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                BlockGuardPolicyException e = new StrictModeCustomViolation(this.mPolicyMask, name);
                e.fillInStackTrace();
                startHandlingViolationException(e);
            }
        }

        void onResourceMismatch(Object tag) {
            if ((this.mPolicyMask & 16) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                BlockGuardPolicyException e = new StrictModeResourceMismatchViolation(this.mPolicyMask, tag);
                e.fillInStackTrace();
                startHandlingViolationException(e);
            }
        }

        public void onReadFromDisk() {
            if ((this.mPolicyMask & 2) != 0 && !StrictMode.tooManyViolationsThisLoop()) {
                BlockGuardPolicyException e = new StrictModeDiskReadViolation(this.mPolicyMask);
                e.fillInStackTrace();
                startHandlingViolationException(e);
            }
        }

        public void onNetwork() {
            if ((this.mPolicyMask & 4) != 0) {
                if ((this.mPolicyMask & 524288) != 0) {
                    throw new NetworkOnMainThreadException();
                } else if (!StrictMode.tooManyViolationsThisLoop()) {
                    BlockGuardPolicyException e = new StrictModeNetworkViolation(this.mPolicyMask);
                    e.fillInStackTrace();
                    startHandlingViolationException(e);
                }
            }
        }

        public void setPolicyMask(int policyMask) {
            this.mPolicyMask = policyMask;
        }

        void startHandlingViolationException(BlockGuardPolicyException e) {
            ViolationInfo info = new ViolationInfo((Throwable) e, e.getPolicy());
            info.violationUptimeMillis = SystemClock.uptimeMillis();
            handleViolationWithTimingAttempt(info);
        }

        void handleViolationWithTimingAttempt(ViolationInfo info) {
            if (Looper.myLooper() == null || (info.policy & StrictMode.THREAD_PENALTY_MASK) == 262144) {
                info.durationMillis = -1;
                handleViolation(info);
                return;
            }
            final ArrayList<ViolationInfo> records = (ArrayList) StrictMode.violationsBeingTimed.get();
            if (records.size() < 10) {
                records.add(info);
                if (records.size() <= 1) {
                    final IWindowManager windowManager = (info.policy & 1048576) != 0 ? (IWindowManager) StrictMode.sWindowManager.get() : null;
                    if (windowManager != null) {
                        try {
                            windowManager.showStrictModeViolation(true);
                        } catch (RemoteException e) {
                        }
                    }
                    ((Handler) StrictMode.threadHandler.get()).postAtFrontOfQueue(new Runnable() {
                        public void run() {
                            long loopFinishTime = SystemClock.uptimeMillis();
                            if (windowManager != null) {
                                try {
                                    windowManager.showStrictModeViolation(false);
                                } catch (RemoteException e) {
                                }
                            }
                            for (int n = 0; n < records.size(); n++) {
                                ViolationInfo v = (ViolationInfo) records.get(n);
                                v.violationNumThisLoop = n + 1;
                                v.durationMillis = (int) (loopFinishTime - v.violationUptimeMillis);
                                AndroidBlockGuardPolicy.this.handleViolation(v);
                            }
                            records.clear();
                        }
                    });
                }
            }
        }

        void handleViolation(ViolationInfo info) {
            if (info == null || info.crashInfo == null || info.crashInfo.stackTrace == null) {
                Log.wtf(StrictMode.TAG, "unexpected null stacktrace");
                return;
            }
            if (StrictMode.LOG_V) {
                Log.d(StrictMode.TAG, "handleViolation; policy=" + info.policy);
            }
            if ((info.policy & 4194304) != 0) {
                ArrayList<ViolationInfo> violations = (ArrayList) StrictMode.gatheredViolations.get();
                if (violations == null) {
                    ArrayList<ViolationInfo> arrayList = new ArrayList(1);
                    StrictMode.gatheredViolations.set(arrayList);
                } else if (violations.size() >= 5) {
                    return;
                }
                Iterator i$ = violations.iterator();
                while (i$.hasNext()) {
                    if (info.crashInfo.stackTrace.equals(((ViolationInfo) i$.next()).crashInfo.stackTrace)) {
                        return;
                    }
                }
                violations.add(info);
                return;
            }
            Integer crashFingerprint = Integer.valueOf(info.hashCode());
            long lastViolationTime = 0;
            if (this.mLastViolationTime != null) {
                Long vtime = (Long) this.mLastViolationTime.get(crashFingerprint);
                if (vtime != null) {
                    lastViolationTime = vtime.longValue();
                }
            } else {
                this.mLastViolationTime = new ArrayMap(1);
            }
            long now = SystemClock.uptimeMillis();
            this.mLastViolationTime.put(crashFingerprint, Long.valueOf(now));
            long timeSinceLastViolationMillis = lastViolationTime == 0 ? Long.MAX_VALUE : now - lastViolationTime;
            if ((info.policy & 65536) != 0 && timeSinceLastViolationMillis > StrictMode.MIN_LOG_INTERVAL_MS) {
                if (info.durationMillis != -1) {
                    Log.d(StrictMode.TAG, "StrictMode policy violation; ~duration=" + info.durationMillis + " ms: " + info.crashInfo.stackTrace);
                } else {
                    Log.d(StrictMode.TAG, "StrictMode policy violation: " + info.crashInfo.stackTrace);
                }
            }
            int violationMaskSubset = 0;
            if ((info.policy & 131072) != 0 && timeSinceLastViolationMillis > 30000) {
                violationMaskSubset = 0 | 131072;
            }
            if ((info.policy & 2097152) != 0 && lastViolationTime == 0) {
                violationMaskSubset |= 2097152;
            }
            if (violationMaskSubset != 0) {
                violationMaskSubset |= StrictMode.parseViolationFromMessage(info.crashInfo.exceptionMessage);
                int savedPolicyMask = StrictMode.getThreadPolicyMask();
                if ((info.policy & StrictMode.THREAD_PENALTY_MASK) == 2097152) {
                    StrictMode.dropboxViolationAsync(violationMaskSubset, info);
                    return;
                }
                try {
                    StrictMode.setThreadPolicyMask(0);
                    ActivityManagerNative.getDefault().handleApplicationStrictModeViolation(RuntimeInit.getApplicationObject(), violationMaskSubset, info);
                } catch (RemoteException e) {
                    Log.e(StrictMode.TAG, "RemoteException trying to handle StrictMode violation", e);
                } finally {
                    StrictMode.setThreadPolicyMask(savedPolicyMask);
                }
            }
            if ((info.policy & 262144) != 0) {
                StrictMode.executeDeathPenalty(info);
            }
        }
    }

    private static class AndroidCloseGuardReporter implements Reporter {
        private AndroidCloseGuardReporter() {
        }

        public void report(String message, Throwable allocationSite) {
            StrictMode.onVmPolicyViolation(message, allocationSite);
        }
    }

    private static class InstanceCountViolation extends Throwable {
        private static final StackTraceElement[] FAKE_STACK = new StackTraceElement[]{new StackTraceElement("android.os.StrictMode", "setClassInstanceLimit", "StrictMode.java", 1)};
        final Class mClass;
        final long mInstances;
        final int mLimit;

        public InstanceCountViolation(Class klass, long instances, int limit) {
            super(klass.toString() + "; instances=" + instances + "; limit=" + limit);
            setStackTrace(FAKE_STACK);
            this.mClass = klass;
            this.mInstances = instances;
            this.mLimit = limit;
        }
    }

    private static final class InstanceTracker {
        private static final HashMap<Class<?>, Integer> sInstanceCounts = new HashMap();
        private final Class<?> mKlass;

        public InstanceTracker(Object instance) {
            this.mKlass = instance.getClass();
            synchronized (sInstanceCounts) {
                Integer value = (Integer) sInstanceCounts.get(this.mKlass);
                sInstanceCounts.put(this.mKlass, Integer.valueOf(value != null ? value.intValue() + 1 : 1));
            }
        }

        protected void finalize() throws Throwable {
            try {
                synchronized (sInstanceCounts) {
                    Integer value = (Integer) sInstanceCounts.get(this.mKlass);
                    if (value != null) {
                        int newValue = value.intValue() - 1;
                        if (newValue > 0) {
                            sInstanceCounts.put(this.mKlass, Integer.valueOf(newValue));
                        } else {
                            sInstanceCounts.remove(this.mKlass);
                        }
                    }
                }
                super.finalize();
            } catch (Throwable th) {
                super.finalize();
            }
        }

        public static int getInstanceCount(Class<?> klass) {
            int intValue;
            synchronized (sInstanceCounts) {
                Integer value = (Integer) sInstanceCounts.get(klass);
                intValue = value != null ? value.intValue() : 0;
            }
            return intValue;
        }
    }

    private static class LogStackTrace extends Exception {
        private LogStackTrace() {
        }
    }

    public static class StrictModeViolation extends BlockGuardPolicyException {
        public StrictModeViolation(int policyState, int policyViolated, String message) {
            super(policyState, policyViolated, message);
        }
    }

    private static class StrictModeCustomViolation extends StrictModeViolation {
        public StrictModeCustomViolation(int policyMask, String name) {
            super(policyMask, 8, name);
        }
    }

    private static class StrictModeDiskReadViolation extends StrictModeViolation {
        public StrictModeDiskReadViolation(int policyMask) {
            super(policyMask, 2, null);
        }
    }

    private static class StrictModeDiskWriteViolation extends StrictModeViolation {
        public StrictModeDiskWriteViolation(int policyMask) {
            super(policyMask, 1, null);
        }
    }

    public static class StrictModeNetworkViolation extends StrictModeViolation {
        public StrictModeNetworkViolation(int policyMask) {
            super(policyMask, 4, null);
        }
    }

    private static class StrictModeResourceMismatchViolation extends StrictModeViolation {
        public StrictModeResourceMismatchViolation(int policyMask, Object tag) {
            super(policyMask, 16, tag != null ? tag.toString() : null);
        }
    }

    public static final class ThreadPolicy {
        public static final ThreadPolicy LAX = new ThreadPolicy(0);
        final int mask;

        public static final class Builder {
            private int mMask;

            public Builder() {
                this.mMask = 0;
                this.mMask = 0;
            }

            public Builder(ThreadPolicy policy) {
                this.mMask = 0;
                this.mMask = policy.mask;
            }

            public Builder detectAll() {
                return enable(31);
            }

            public Builder permitAll() {
                return disable(31);
            }

            public Builder detectNetwork() {
                return enable(4);
            }

            public Builder permitNetwork() {
                return disable(4);
            }

            public Builder detectDiskReads() {
                return enable(2);
            }

            public Builder permitDiskReads() {
                return disable(2);
            }

            public Builder detectCustomSlowCalls() {
                return enable(8);
            }

            public Builder permitCustomSlowCalls() {
                return disable(8);
            }

            public Builder permitResourceMismatches() {
                return disable(16);
            }

            public Builder detectResourceMismatches() {
                return enable(16);
            }

            public Builder detectDiskWrites() {
                return enable(1);
            }

            public Builder permitDiskWrites() {
                return disable(1);
            }

            public Builder penaltyDialog() {
                return enable(131072);
            }

            public Builder penaltyDeath() {
                return enable(262144);
            }

            public Builder penaltyDeathOnNetwork() {
                return enable(524288);
            }

            public Builder penaltyFlashScreen() {
                return enable(1048576);
            }

            public Builder penaltyLog() {
                return enable(65536);
            }

            public Builder penaltyDropBox() {
                return enable(2097152);
            }

            private Builder enable(int bit) {
                this.mMask |= bit;
                return this;
            }

            private Builder disable(int bit) {
                this.mMask &= bit ^ -1;
                return this;
            }

            public ThreadPolicy build() {
                if (this.mMask != 0 && (this.mMask & 2555904) == 0) {
                    penaltyLog();
                }
                return new ThreadPolicy(this.mMask);
            }
        }

        private ThreadPolicy(int mask) {
            this.mask = mask;
        }

        public String toString() {
            return "[StrictMode.ThreadPolicy; mask=" + this.mask + "]";
        }
    }

    private static class ThreadSpanState {
        public Span mActiveHead;
        public int mActiveSize;
        public Span mFreeListHead;
        public int mFreeListSize;

        private ThreadSpanState() {
        }
    }

    public static class ViolationInfo {
        public String broadcastIntentAction;
        public final CrashInfo crashInfo;
        public int durationMillis;
        public String message;
        public int numAnimationsRunning;
        public long numInstances;
        public final int policy;
        public String[] tags;
        public int violationNumThisLoop;
        public long violationUptimeMillis;

        public ViolationInfo() {
            this.durationMillis = -1;
            this.numAnimationsRunning = 0;
            this.numInstances = -1;
            this.crashInfo = null;
            this.policy = 0;
        }

        public ViolationInfo(Throwable tr, int policy) {
            this(null, tr, policy);
        }

        public ViolationInfo(String message, Throwable tr, int policy) {
            this.durationMillis = -1;
            this.numAnimationsRunning = 0;
            this.numInstances = -1;
            this.message = message;
            this.crashInfo = new CrashInfo(tr);
            this.violationUptimeMillis = SystemClock.uptimeMillis();
            this.policy = policy;
            this.numAnimationsRunning = ValueAnimator.getCurrentAnimationsCount();
            Intent broadcastIntent = ActivityThread.getIntentBeingBroadcast();
            if (broadcastIntent != null) {
                this.broadcastIntentAction = broadcastIntent.getAction();
            }
            ThreadSpanState state = (ThreadSpanState) StrictMode.sThisThreadSpanState.get();
            if (tr instanceof InstanceCountViolation) {
                this.numInstances = ((InstanceCountViolation) tr).mInstances;
            }
            synchronized (state) {
                int spanActiveCount = state.mActiveSize;
                if (spanActiveCount > 20) {
                    spanActiveCount = 20;
                }
                if (spanActiveCount != 0) {
                    this.tags = new String[spanActiveCount];
                    int index = 0;
                    for (Span iter = state.mActiveHead; iter != null && index < spanActiveCount; iter = iter.mNext) {
                        this.tags[index] = iter.mName;
                        index++;
                    }
                }
            }
        }

        public int hashCode() {
            int result = this.crashInfo.stackTrace.hashCode() + 629;
            if (this.numAnimationsRunning != 0) {
                result *= 37;
            }
            if (this.broadcastIntentAction != null) {
                result = (result * 37) + this.broadcastIntentAction.hashCode();
            }
            if (this.tags != null) {
                for (String tag : this.tags) {
                    result = (result * 37) + tag.hashCode();
                }
            }
            return result;
        }

        public ViolationInfo(Parcel in) {
            this(in, false);
        }

        public ViolationInfo(Parcel in, boolean unsetGatheringBit) {
            this.durationMillis = -1;
            this.numAnimationsRunning = 0;
            this.numInstances = -1;
            this.message = in.readString();
            this.crashInfo = new CrashInfo(in);
            int rawPolicy = in.readInt();
            if (unsetGatheringBit) {
                this.policy = -4194305 & rawPolicy;
            } else {
                this.policy = rawPolicy;
            }
            this.durationMillis = in.readInt();
            this.violationNumThisLoop = in.readInt();
            this.numAnimationsRunning = in.readInt();
            this.violationUptimeMillis = in.readLong();
            this.numInstances = in.readLong();
            this.broadcastIntentAction = in.readString();
            this.tags = in.readStringArray();
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.message);
            this.crashInfo.writeToParcel(dest, flags);
            int start = dest.dataPosition();
            dest.writeInt(this.policy);
            dest.writeInt(this.durationMillis);
            dest.writeInt(this.violationNumThisLoop);
            dest.writeInt(this.numAnimationsRunning);
            dest.writeLong(this.violationUptimeMillis);
            dest.writeLong(this.numInstances);
            dest.writeString(this.broadcastIntentAction);
            dest.writeStringArray(this.tags);
            if (dest.dataPosition() - start > 10240) {
                Slog.d(StrictMode.TAG, "VIO: policy=" + this.policy + " dur=" + this.durationMillis + " numLoop=" + this.violationNumThisLoop + " anim=" + this.numAnimationsRunning + " uptime=" + this.violationUptimeMillis + " numInst=" + this.numInstances);
                Slog.d(StrictMode.TAG, "VIO: action=" + this.broadcastIntentAction);
                Slog.d(StrictMode.TAG, "VIO: tags=" + Arrays.toString(this.tags));
                Slog.d(StrictMode.TAG, "VIO: TOTAL BYTES WRITTEN: " + (dest.dataPosition() - start));
            }
        }

        public void dump(Printer pw, String prefix) {
            this.crashInfo.dump(pw, prefix);
            pw.println(prefix + "policy: " + this.policy);
            if (this.durationMillis != -1) {
                pw.println(prefix + "durationMillis: " + this.durationMillis);
            }
            if (this.numInstances != -1) {
                pw.println(prefix + "numInstances: " + this.numInstances);
            }
            if (this.violationNumThisLoop != 0) {
                pw.println(prefix + "violationNumThisLoop: " + this.violationNumThisLoop);
            }
            if (this.numAnimationsRunning != 0) {
                pw.println(prefix + "numAnimationsRunning: " + this.numAnimationsRunning);
            }
            pw.println(prefix + "violationUptimeMillis: " + this.violationUptimeMillis);
            if (this.broadcastIntentAction != null) {
                pw.println(prefix + "broadcastIntentAction: " + this.broadcastIntentAction);
            }
            if (this.tags != null) {
                String[] arr$ = this.tags;
                int len$ = arr$.length;
                int i$ = 0;
                int index = 0;
                while (i$ < len$) {
                    int index2 = index + 1;
                    pw.println(prefix + "tag[" + index + "]: " + arr$[i$]);
                    i$++;
                    index = index2;
                }
            }
        }
    }

    public static final class VmPolicy {
        public static final VmPolicy LAX = new VmPolicy(0, StrictMode.EMPTY_CLASS_LIMIT_MAP);
        final HashMap<Class, Integer> classInstanceLimit;
        final int mask;

        public static final class Builder {
            private HashMap<Class, Integer> mClassInstanceLimit;
            private boolean mClassInstanceLimitNeedCow;
            private int mMask;

            public Builder() {
                this.mClassInstanceLimitNeedCow = false;
                this.mMask = 0;
            }

            public Builder(VmPolicy base) {
                this.mClassInstanceLimitNeedCow = false;
                this.mMask = base.mask;
                this.mClassInstanceLimitNeedCow = true;
                this.mClassInstanceLimit = base.classInstanceLimit;
            }

            public Builder setClassInstanceLimit(Class klass, int instanceLimit) {
                if (klass == null) {
                    throw new NullPointerException("klass == null");
                }
                if (!this.mClassInstanceLimitNeedCow) {
                    if (this.mClassInstanceLimit == null) {
                        this.mClassInstanceLimit = new HashMap();
                    }
                    this.mMask |= 2048;
                    this.mClassInstanceLimit.put(klass, Integer.valueOf(instanceLimit));
                } else if (!(this.mClassInstanceLimit.containsKey(klass) && ((Integer) this.mClassInstanceLimit.get(klass)).intValue() == instanceLimit)) {
                    this.mClassInstanceLimitNeedCow = false;
                    this.mClassInstanceLimit = (HashMap) this.mClassInstanceLimit.clone();
                    this.mMask |= 2048;
                    this.mClassInstanceLimit.put(klass, Integer.valueOf(instanceLimit));
                }
                return this;
            }

            public Builder detectActivityLeaks() {
                return enable(1024);
            }

            public Builder detectAll() {
                int flags = 14080;
                if (SystemProperties.getBoolean(StrictMode.CLEARTEXT_PROPERTY, false)) {
                    flags = 14080 | 16384;
                }
                return enable(flags);
            }

            public Builder detectLeakedSqlLiteObjects() {
                return enable(256);
            }

            public Builder detectLeakedClosableObjects() {
                return enable(512);
            }

            public Builder detectLeakedRegistrationObjects() {
                return enable(4096);
            }

            public Builder detectFileUriExposure() {
                return enable(8192);
            }

            public Builder detectCleartextNetwork() {
                return enable(16384);
            }

            public Builder penaltyDeath() {
                return enable(262144);
            }

            public Builder penaltyDeathOnCleartextNetwork() {
                return enable(8388608);
            }

            public Builder penaltyLog() {
                return enable(65536);
            }

            public Builder penaltyDropBox() {
                return enable(2097152);
            }

            private Builder enable(int bit) {
                this.mMask |= bit;
                return this;
            }

            public VmPolicy build() {
                if (this.mMask != 0 && (this.mMask & 2555904) == 0) {
                    penaltyLog();
                }
                return new VmPolicy(this.mMask, this.mClassInstanceLimit != null ? this.mClassInstanceLimit : StrictMode.EMPTY_CLASS_LIMIT_MAP);
            }
        }

        private VmPolicy(int mask, HashMap<Class, Integer> classInstanceLimit) {
            if (classInstanceLimit == null) {
                throw new NullPointerException("classInstanceLimit == null");
            }
            this.mask = mask;
            this.classInstanceLimit = classInstanceLimit;
        }

        public String toString() {
            return "[StrictMode.VmPolicy; mask=" + this.mask + "]";
        }
    }

    private StrictMode() {
    }

    public static void setThreadPolicy(ThreadPolicy policy) {
        setThreadPolicyMask(policy.mask);
    }

    private static void setThreadPolicyMask(int policyMask) {
        setBlockGuardPolicy(policyMask);
        Binder.setThreadStrictModePolicy(policyMask);
    }

    private static void setBlockGuardPolicy(int policyMask) {
        if (policyMask == 0) {
            BlockGuard.setThreadPolicy(BlockGuard.LAX_POLICY);
            return;
        }
        AndroidBlockGuardPolicy androidPolicy;
        Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            androidPolicy = (AndroidBlockGuardPolicy) policy;
        } else {
            androidPolicy = (AndroidBlockGuardPolicy) threadAndroidPolicy.get();
            BlockGuard.setThreadPolicy(androidPolicy);
        }
        androidPolicy.setPolicyMask(policyMask);
    }

    private static void setCloseGuardEnabled(boolean enabled) {
        if (!(CloseGuard.getReporter() instanceof AndroidCloseGuardReporter)) {
            CloseGuard.setReporter(new AndroidCloseGuardReporter());
        }
        CloseGuard.setEnabled(enabled);
    }

    public static int getThreadPolicyMask() {
        return BlockGuard.getThreadPolicy().getPolicyMask();
    }

    public static ThreadPolicy getThreadPolicy() {
        return new ThreadPolicy(getThreadPolicyMask());
    }

    public static ThreadPolicy allowThreadDiskWrites() {
        int oldPolicyMask = getThreadPolicyMask();
        int newPolicyMask = oldPolicyMask & -4;
        if (newPolicyMask != oldPolicyMask) {
            setThreadPolicyMask(newPolicyMask);
        }
        return new ThreadPolicy(oldPolicyMask);
    }

    public static ThreadPolicy allowThreadDiskReads() {
        int oldPolicyMask = getThreadPolicyMask();
        int newPolicyMask = oldPolicyMask & -3;
        if (newPolicyMask != oldPolicyMask) {
            setThreadPolicyMask(newPolicyMask);
        }
        return new ThreadPolicy(oldPolicyMask);
    }

    private static boolean amTheSystemServerProcess() {
        if (Process.myUid() != 1000) {
            return false;
        }
        Throwable stack = new Throwable();
        stack.fillInStackTrace();
        for (StackTraceElement ste : stack.getStackTrace()) {
            String clsName = ste.getClassName();
            if (clsName != null && clsName.startsWith("com.android.server.")) {
                return true;
            }
        }
        return false;
    }

    public static boolean conditionallyEnableDebugLogging() {
        boolean doFlashes;
        if (!SystemProperties.getBoolean(VISUAL_PROPERTY, false) || amTheSystemServerProcess()) {
            doFlashes = false;
        } else {
            doFlashes = true;
        }
        boolean suppress = SystemProperties.getBoolean(DISABLE_PROPERTY, false);
        if (doFlashes || !(IS_USER_BUILD || suppress)) {
            int threadPolicyMask = 7;
            if (!IS_USER_BUILD) {
                threadPolicyMask = 7 | 2097152;
            }
            if (doFlashes) {
                threadPolicyMask |= 1048576;
            }
            setThreadPolicyMask(threadPolicyMask);
            if (IS_USER_BUILD) {
                setCloseGuardEnabled(false);
            } else {
                Builder policyBuilder = new Builder().detectAll().penaltyDropBox();
                if (IS_ENG_BUILD) {
                    policyBuilder.penaltyLog();
                }
                setVmPolicy(policyBuilder.build());
                setCloseGuardEnabled(vmClosableObjectLeaksEnabled());
            }
            return true;
        }
        setCloseGuardEnabled(false);
        return false;
    }

    public static void enableDeathOnNetwork() {
        setThreadPolicyMask((getThreadPolicyMask() | 4) | 524288);
    }

    private static int parsePolicyFromMessage(String message) {
        int i = 0;
        if (message != null && message.startsWith("policy=")) {
            int spaceIndex = message.indexOf(32);
            if (spaceIndex != -1) {
                try {
                    i = Integer.valueOf(message.substring(7, spaceIndex)).intValue();
                } catch (NumberFormatException e) {
                }
            }
        }
        return i;
    }

    private static int parseViolationFromMessage(String message) {
        int i = 0;
        if (message != null) {
            int violationIndex = message.indexOf("violation=");
            if (violationIndex != -1) {
                int numberStartIndex = violationIndex + "violation=".length();
                int numberEndIndex = message.indexOf(32, numberStartIndex);
                if (numberEndIndex == -1) {
                    numberEndIndex = message.length();
                }
                try {
                    i = Integer.valueOf(message.substring(numberStartIndex, numberEndIndex)).intValue();
                } catch (NumberFormatException e) {
                }
            }
        }
        return i;
    }

    private static boolean tooManyViolationsThisLoop() {
        return ((ArrayList) violationsBeingTimed.get()).size() >= 10;
    }

    private static void executeDeathPenalty(ViolationInfo info) {
        throw new StrictModeViolation(info.policy, parseViolationFromMessage(info.crashInfo.exceptionMessage), null);
    }

    private static void dropboxViolationAsync(final int violationMaskSubset, final ViolationInfo info) {
        int outstanding = sDropboxCallsInFlight.incrementAndGet();
        if (outstanding > 20) {
            sDropboxCallsInFlight.decrementAndGet();
            return;
        }
        if (LOG_V) {
            Log.d(TAG, "Dropboxing async; in-flight=" + outstanding);
        }
        new Thread("callActivityManagerForStrictModeDropbox") {
            public void run() {
                Process.setThreadPriority(10);
                try {
                    IActivityManager am = ActivityManagerNative.getDefault();
                    if (am == null) {
                        Log.d(StrictMode.TAG, "No activity manager; failed to Dropbox violation.");
                    } else {
                        am.handleApplicationStrictModeViolation(RuntimeInit.getApplicationObject(), violationMaskSubset, info);
                    }
                } catch (RemoteException e) {
                    Log.e(StrictMode.TAG, "RemoteException handling StrictMode violation", e);
                }
                int outstanding = StrictMode.sDropboxCallsInFlight.decrementAndGet();
                if (StrictMode.LOG_V) {
                    Log.d(StrictMode.TAG, "Dropbox complete; in-flight=" + outstanding);
                }
            }
        }.start();
    }

    static boolean hasGatheredViolations() {
        return gatheredViolations.get() != null;
    }

    static void clearGatheredViolations() {
        gatheredViolations.set(null);
    }

    public static void conditionallyCheckInstanceCounts() {
        VmPolicy policy = getVmPolicy();
        if (policy.classInstanceLimit.size() != 0) {
            System.gc();
            System.runFinalization();
            System.gc();
            for (Entry<Class, Integer> entry : policy.classInstanceLimit.entrySet()) {
                Class klass = (Class) entry.getKey();
                int limit = ((Integer) entry.getValue()).intValue();
                long instances = VMDebug.countInstancesOfClass(klass, false);
                if (instances > ((long) limit)) {
                    Throwable tr = new InstanceCountViolation(klass, instances, limit);
                    onVmPolicyViolation(tr.getMessage(), tr);
                }
            }
        }
    }

    public static void setVmPolicy(VmPolicy policy) {
        synchronized (StrictMode.class) {
            sVmPolicy = policy;
            sVmPolicyMask = policy.mask;
            setCloseGuardEnabled(vmClosableObjectLeaksEnabled());
            Looper looper = Looper.getMainLooper();
            if (looper != null) {
                MessageQueue mq = looper.mQueue;
                if (policy.classInstanceLimit.size() == 0 || (sVmPolicyMask & VM_PENALTY_MASK) == 0) {
                    mq.removeIdleHandler(sProcessIdleHandler);
                    sIsIdlerRegistered = false;
                } else if (!sIsIdlerRegistered) {
                    mq.addIdleHandler(sProcessIdleHandler);
                    sIsIdlerRegistered = true;
                }
            }
            int networkPolicy = 0;
            if ((sVmPolicyMask & 16384) != 0) {
                if ((sVmPolicyMask & 262144) == 0 && (sVmPolicyMask & 8388608) == 0) {
                    networkPolicy = 1;
                } else {
                    networkPolicy = 2;
                }
            }
            INetworkManagementService netd = INetworkManagementService.Stub.asInterface(ServiceManager.getService(Context.NETWORKMANAGEMENT_SERVICE));
            if (netd != null) {
                try {
                    netd.setUidCleartextNetworkPolicy(Process.myUid(), networkPolicy);
                } catch (RemoteException e) {
                }
            } else if (networkPolicy != 0) {
                Log.w(TAG, "Dropping requested network policy due to missing service!");
            }
        }
    }

    public static VmPolicy getVmPolicy() {
        VmPolicy vmPolicy;
        synchronized (StrictMode.class) {
            vmPolicy = sVmPolicy;
        }
        return vmPolicy;
    }

    public static void enableDefaults() {
        setThreadPolicy(new Builder().detectAll().penaltyLog().build());
        setVmPolicy(new Builder().detectAll().penaltyLog().build());
    }

    public static boolean vmSqliteObjectLeaksEnabled() {
        return (sVmPolicyMask & 256) != 0;
    }

    public static boolean vmClosableObjectLeaksEnabled() {
        return (sVmPolicyMask & 512) != 0;
    }

    public static boolean vmRegistrationLeaksEnabled() {
        return (sVmPolicyMask & 4096) != 0;
    }

    public static boolean vmFileUriExposureEnabled() {
        return (sVmPolicyMask & 8192) != 0;
    }

    public static boolean vmCleartextNetworkEnabled() {
        return (sVmPolicyMask & 16384) != 0;
    }

    public static void onSqliteObjectLeaked(String message, Throwable originStack) {
        onVmPolicyViolation(message, originStack);
    }

    public static void onWebViewMethodCalledOnWrongThread(Throwable originStack) {
        onVmPolicyViolation(null, originStack);
    }

    public static void onIntentReceiverLeaked(Throwable originStack) {
        onVmPolicyViolation(null, originStack);
    }

    public static void onServiceConnectionLeaked(Throwable originStack) {
        onVmPolicyViolation(null, originStack);
    }

    public static void onFileUriExposed(String location) {
        onVmPolicyViolation(null, new Throwable("file:// Uri exposed through " + location));
    }

    public static void onCleartextNetworkDetected(byte[] firstPacket) {
        boolean forceDeath = false;
        byte[] rawAddr = null;
        if (firstPacket != null) {
            if (firstPacket.length >= 20 && (firstPacket[0] & 240) == 64) {
                rawAddr = new byte[4];
                System.arraycopy(firstPacket, 16, rawAddr, 0, 4);
            } else if (firstPacket.length >= 40 && (firstPacket[0] & 240) == 96) {
                rawAddr = new byte[16];
                System.arraycopy(firstPacket, 24, rawAddr, 0, 16);
            }
        }
        int uid = Process.myUid();
        String msg = "Detected cleartext network traffic from UID " + uid;
        if (rawAddr != null) {
            try {
                msg = "Detected cleartext network traffic from UID " + uid + " to " + InetAddress.getByAddress(rawAddr);
            } catch (UnknownHostException e) {
            }
        }
        if ((sVmPolicyMask & 8388608) != 0) {
            forceDeath = true;
        }
        onVmPolicyViolation(HexDump.dumpHexString(firstPacket).trim(), new Throwable(msg), forceDeath);
    }

    public static void onVmPolicyViolation(String message, Throwable originStack) {
        onVmPolicyViolation(message, originStack, false);
    }

    public static void onVmPolicyViolation(String message, Throwable originStack, boolean forceDeath) {
        boolean penaltyDropbox = (sVmPolicyMask & 2097152) != 0;
        boolean penaltyDeath = (sVmPolicyMask & 262144) != 0 || forceDeath;
        boolean penaltyLog = (sVmPolicyMask & 65536) != 0;
        ViolationInfo info = new ViolationInfo(message, originStack, sVmPolicyMask);
        info.numAnimationsRunning = 0;
        info.tags = null;
        info.broadcastIntentAction = null;
        Integer fingerprint = Integer.valueOf(info.hashCode());
        long now = SystemClock.uptimeMillis();
        long lastViolationTime = 0;
        long timeSinceLastViolationMillis = Long.MAX_VALUE;
        synchronized (sLastVmViolationTime) {
            if (sLastVmViolationTime.containsKey(fingerprint)) {
                lastViolationTime = ((Long) sLastVmViolationTime.get(fingerprint)).longValue();
                timeSinceLastViolationMillis = now - lastViolationTime;
            }
            if (timeSinceLastViolationMillis > MIN_LOG_INTERVAL_MS) {
                sLastVmViolationTime.put(fingerprint, Long.valueOf(now));
            }
        }
        if (penaltyLog && timeSinceLastViolationMillis > MIN_LOG_INTERVAL_MS) {
            Log.e(TAG, message, originStack);
        }
        int violationMaskSubset = 2097152 | (sVmPolicyMask & ALL_VM_DETECT_BITS);
        if (!penaltyDropbox || penaltyDeath) {
            if (penaltyDropbox && lastViolationTime == 0) {
                int savedPolicyMask = getThreadPolicyMask();
                try {
                    setThreadPolicyMask(0);
                    ActivityManagerNative.getDefault().handleApplicationStrictModeViolation(RuntimeInit.getApplicationObject(), violationMaskSubset, info);
                } catch (RemoteException e) {
                    Log.e(TAG, "RemoteException trying to handle StrictMode violation", e);
                } finally {
                    setThreadPolicyMask(savedPolicyMask);
                }
            }
            if (penaltyDeath) {
                System.err.println("StrictMode VmPolicy violation with POLICY_DEATH; shutting down.");
                Process.killProcess(Process.myPid());
                System.exit(10);
                return;
            }
            return;
        }
        dropboxViolationAsync(violationMaskSubset, info);
    }

    static void writeGatheredViolationsToParcel(Parcel p) {
        ArrayList<ViolationInfo> violations = (ArrayList) gatheredViolations.get();
        if (violations == null) {
            p.writeInt(0);
        } else {
            p.writeInt(violations.size());
            for (int i = 0; i < violations.size(); i++) {
                int start = p.dataPosition();
                ((ViolationInfo) violations.get(i)).writeToParcel(p, 0);
                if (p.dataPosition() - start > 10240) {
                    Slog.d(TAG, "Wrote violation #" + i + " of " + violations.size() + ": " + (p.dataPosition() - start) + " bytes");
                }
            }
            if (LOG_V) {
                Log.d(TAG, "wrote violations to response parcel; num=" + violations.size());
            }
            violations.clear();
        }
        gatheredViolations.set(null);
    }

    static void readAndHandleBinderCallViolations(Parcel p) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new FastPrintWriter(sw, false, 256);
        new LogStackTrace().printStackTrace(pw);
        pw.flush();
        String ourStack = sw.toString();
        boolean currentlyGathering = (4194304 & getThreadPolicyMask()) != 0;
        int numViolations = p.readInt();
        int i = 0;
        while (i < numViolations) {
            if (LOG_V) {
                Log.d(TAG, "strict mode violation stacks read from binder call.  i=" + i);
            }
            ViolationInfo info = new ViolationInfo(p, !currentlyGathering);
            if (info.crashInfo.stackTrace == null || info.crashInfo.stackTrace.length() <= 30000) {
                StringBuilder stringBuilder = new StringBuilder();
                CrashInfo crashInfo = info.crashInfo;
                crashInfo.stackTrace = stringBuilder.append(crashInfo.stackTrace).append("# via Binder call with stack:\n").append(ourStack).toString();
                Policy policy = BlockGuard.getThreadPolicy();
                if (policy instanceof AndroidBlockGuardPolicy) {
                    ((AndroidBlockGuardPolicy) policy).handleViolationWithTimingAttempt(info);
                }
                i++;
            } else {
                String front = info.crashInfo.stackTrace.substring(256);
                while (i + 1 < numViolations) {
                    info = new ViolationInfo(p, !currentlyGathering);
                    i++;
                }
                clearGatheredViolations();
                return;
            }
        }
    }

    private static void onBinderStrictModePolicyChange(int newPolicy) {
        setBlockGuardPolicy(newPolicy);
    }

    public static Span enterCriticalSpan(String name) {
        if (IS_USER_BUILD) {
            return NO_OP_SPAN;
        }
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name must be non-null and non-empty");
        }
        Span span;
        ThreadSpanState state = (ThreadSpanState) sThisThreadSpanState.get();
        synchronized (state) {
            if (state.mFreeListHead != null) {
                span = state.mFreeListHead;
                state.mFreeListHead = span.mNext;
                state.mFreeListSize--;
            } else {
                span = new Span(state);
            }
            span.mName = name;
            span.mCreateMillis = SystemClock.uptimeMillis();
            span.mNext = state.mActiveHead;
            span.mPrev = null;
            state.mActiveHead = span;
            state.mActiveSize++;
            if (span.mNext != null) {
                span.mNext.mPrev = span;
            }
            if (LOG_V) {
                Log.d(TAG, "Span enter=" + name + "; size=" + state.mActiveSize);
            }
        }
        return span;
    }

    public static void noteSlowCall(String name) {
        Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            ((AndroidBlockGuardPolicy) policy).onCustomSlowCall(name);
        }
    }

    public static void noteResourceMismatch(Object tag) {
        Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            ((AndroidBlockGuardPolicy) policy).onResourceMismatch(tag);
        }
    }

    public static void noteDiskRead() {
        Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            ((AndroidBlockGuardPolicy) policy).onReadFromDisk();
        }
    }

    public static void noteDiskWrite() {
        Policy policy = BlockGuard.getThreadPolicy();
        if (policy instanceof AndroidBlockGuardPolicy) {
            ((AndroidBlockGuardPolicy) policy).onWriteToDisk();
        }
    }

    public static Object trackActivity(Object instance) {
        return new InstanceTracker(instance);
    }

    public static void incrementExpectedActivityCount(Class klass) {
        if (klass != null) {
            synchronized (StrictMode.class) {
                if ((sVmPolicy.mask & 1024) == 0) {
                    return;
                }
                Integer expected = (Integer) sExpectedActivityInstanceCount.get(klass);
                sExpectedActivityInstanceCount.put(klass, Integer.valueOf(expected == null ? 1 : expected.intValue() + 1));
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void decrementExpectedActivityCount(java.lang.Class r11) {
        /*
        r7 = 0;
        if (r11 != 0) goto L_0x0004;
    L_0x0003:
        return;
    L_0x0004:
        r8 = android.os.StrictMode.class;
        monitor-enter(r8);
        r9 = sVmPolicy;	 Catch:{ all -> 0x0011 }
        r9 = r9.mask;	 Catch:{ all -> 0x0011 }
        r9 = r9 & 1024;
        if (r9 != 0) goto L_0x0014;
    L_0x000f:
        monitor-exit(r8);	 Catch:{ all -> 0x0011 }
        goto L_0x0003;
    L_0x0011:
        r7 = move-exception;
        monitor-exit(r8);	 Catch:{ all -> 0x0011 }
        throw r7;
    L_0x0014:
        r9 = sExpectedActivityInstanceCount;	 Catch:{ all -> 0x0011 }
        r1 = r9.get(r11);	 Catch:{ all -> 0x0011 }
        r1 = (java.lang.Integer) r1;	 Catch:{ all -> 0x0011 }
        if (r1 == 0) goto L_0x0024;
    L_0x001e:
        r9 = r1.intValue();	 Catch:{ all -> 0x0011 }
        if (r9 != 0) goto L_0x0054;
    L_0x0024:
        r5 = r7;
    L_0x0025:
        if (r5 != 0) goto L_0x005b;
    L_0x0027:
        r9 = sExpectedActivityInstanceCount;	 Catch:{ all -> 0x0011 }
        r9.remove(r11);	 Catch:{ all -> 0x0011 }
    L_0x002c:
        r4 = r5 + 1;
        monitor-exit(r8);	 Catch:{ all -> 0x0011 }
        r0 = android.os.StrictMode.InstanceTracker.getInstanceCount(r11);
        if (r0 <= r4) goto L_0x0003;
    L_0x0035:
        java.lang.System.gc();
        java.lang.System.runFinalization();
        java.lang.System.gc();
        r2 = dalvik.system.VMDebug.countInstancesOfClass(r11, r7);
        r8 = (long) r4;
        r7 = (r2 > r8 ? 1 : (r2 == r8 ? 0 : -1));
        if (r7 <= 0) goto L_0x0003;
    L_0x0047:
        r6 = new android.os.StrictMode$InstanceCountViolation;
        r6.<init>(r11, r2, r4);
        r7 = r6.getMessage();
        onVmPolicyViolation(r7, r6);
        goto L_0x0003;
    L_0x0054:
        r9 = r1.intValue();	 Catch:{ all -> 0x0011 }
        r5 = r9 + -1;
        goto L_0x0025;
    L_0x005b:
        r9 = sExpectedActivityInstanceCount;	 Catch:{ all -> 0x0011 }
        r10 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x0011 }
        r9.put(r11, r10);	 Catch:{ all -> 0x0011 }
        goto L_0x002c;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.StrictMode.decrementExpectedActivityCount(java.lang.Class):void");
    }
}
