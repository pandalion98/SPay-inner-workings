package android.view.accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.pm.ServiceInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.util.Log;
import android.view.IWindow;
import android.view.MagnificationSpec;
import android.view.accessibility.IAccessibilityManagerClient.Stub;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class AccessibilityManager {
    public static final int DALTONIZER_CORRECT_DEUTERANOMALY = 12;
    public static final int DALTONIZER_DISABLED = -1;
    public static final int DALTONIZER_SIMULATE_MONOCHROMACY = 0;
    private static final boolean DEBUG = false;
    private static final String LOG_TAG = "AccessibilityManager";
    public static final int STATE_FLAG_ACCESSIBILITY_ENABLED = 1;
    public static final int STATE_FLAG_HIGH_TEXT_CONTRAST_ENABLED = 4;
    public static final int STATE_FLAG_TOUCH_EXPLORATION_ENABLED = 2;
    private static AccessibilityManager sInstance;
    static final Object sInstanceSync = new Object();
    private final CopyOnWriteArrayList<AccessibilityStateChangeListener> mAccessibilityStateChangeListeners = new CopyOnWriteArrayList();
    private final Stub mClient = new Stub() {
        public void setState(int state) {
            AccessibilityManager.this.mHandler.obtainMessage(4, state, 0).sendToTarget();
        }
    };
    final Handler mHandler;
    private final CopyOnWriteArrayList<HighTextContrastChangeListener> mHighTextContrastStateChangeListeners = new CopyOnWriteArrayList();
    private final Object mIndexLock = new Object();
    boolean mIsEnabled;
    boolean mIsHighTextContrastEnabled;
    boolean mIsTouchExplorationEnabled;
    private final Object mLock = new Object();
    private IAccessibilityManager mService;
    private final CopyOnWriteArrayList<TouchExplorationStateChangeListener> mTouchExplorationStateChangeListeners = new CopyOnWriteArrayList();
    final int mUserId;

    public interface AccessibilityStateChangeListener {
        void onAccessibilityStateChanged(boolean z);
    }

    public interface HighTextContrastChangeListener {
        void onHighTextContrastStateChanged(boolean z);
    }

    private final class MyHandler extends Handler {
        public static final int MSG_NOTIFY_ACCESSIBILITY_STATE_CHANGED = 1;
        public static final int MSG_NOTIFY_EXPLORATION_STATE_CHANGED = 2;
        public static final int MSG_NOTIFY_HIGH_TEXT_CONTRAST_STATE_CHANGED = 3;
        public static final int MSG_SET_STATE = 4;

        public MyHandler(Looper looper) {
            super(looper, null, false);
        }

        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    AccessibilityManager.this.handleNotifyAccessibilityStateChanged();
                    return;
                case 2:
                    AccessibilityManager.this.handleNotifyTouchExplorationStateChanged();
                    return;
                case 3:
                    AccessibilityManager.this.handleNotifyHighTextContrastStateChanged();
                    return;
                case 4:
                    int state = message.arg1;
                    synchronized (AccessibilityManager.this.mLock) {
                        AccessibilityManager.this.setStateLocked(state);
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public interface TouchExplorationStateChangeListener {
        void onTouchExplorationStateChanged(boolean z);
    }

    public void sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent r10) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(Unknown Source)
	at java.util.HashMap$KeyIterator.next(Unknown Source)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:80)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:199)
*/
        /*
        r9 = this;
        r7 = r9.mLock;
        monitor-enter(r7);
        r4 = r9.getServiceLocked();	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        if (r4 != 0) goto L_0x000b;	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
    L_0x0009:
        monitor-exit(r7);	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
    L_0x000a:
        return;	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
    L_0x000b:
        r6 = r9.mIsEnabled;	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        if (r6 != 0) goto L_0x001a;	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
    L_0x000f:
        r6 = new java.lang.IllegalStateException;	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r8 = "Accessibility off. Did you forget to check that?";	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r6.<init>(r8);	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        throw r6;	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
    L_0x0017:
        r6 = move-exception;
        monitor-exit(r7);	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        throw r6;
    L_0x001a:
        r5 = r9.mUserId;	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        monitor-exit(r7);	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r0 = 0;
        r6 = android.os.SystemClock.uptimeMillis();	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r10.setEventTime(r6);	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r2 = android.os.Binder.clearCallingIdentity();	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r0 = r4.sendAccessibilityEvent(r10, r5);	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        android.os.Binder.restoreCallingIdentity(r2);	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        if (r0 == 0) goto L_0x000a;
    L_0x0032:
        r10.recycle();
        goto L_0x000a;
    L_0x0036:
        r1 = move-exception;
        r6 = "AccessibilityManager";	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r7 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r7.<init>();	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r8 = "Error during sending ";	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r7 = r7.append(r8);	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r7 = r7.append(r10);	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r8 = " ";	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r7 = r7.append(r8);	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        r7 = r7.toString();	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        android.util.Log.e(r6, r7, r1);	 Catch:{ RemoteException -> 0x0036, all -> 0x005b }
        if (r0 == 0) goto L_0x000a;
    L_0x0057:
        r10.recycle();
        goto L_0x000a;
    L_0x005b:
        r6 = move-exception;
        if (r0 == 0) goto L_0x0061;
    L_0x005e:
        r10.recycle();
    L_0x0061:
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent):void");
    }

    public static AccessibilityManager getInstance(Context context) {
        synchronized (sInstanceSync) {
            if (sInstance == null) {
                int userId;
                if (Binder.getCallingUid() == 1000 || context.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS") == 0 || context.checkCallingOrSelfPermission("android.permission.INTERACT_ACROSS_USERS_FULL") == 0) {
                    userId = -2;
                } else {
                    userId = UserHandle.myUserId();
                }
                IBinder iBinder = ServiceManager.getService("accessibility");
                sInstance = new AccessibilityManager(context, iBinder == null ? null : IAccessibilityManager.Stub.asInterface(iBinder), userId);
            }
        }
        return sInstance;
    }

    public AccessibilityManager(Context context, IAccessibilityManager service, int userId) {
        this.mHandler = new MyHandler(context.getMainLooper());
        this.mService = service;
        this.mUserId = userId;
        synchronized (this.mLock) {
            tryConnectToServiceLocked();
        }
    }

    public IAccessibilityManagerClient getClient() {
        return this.mClient;
    }

    public boolean isEnabled() {
        boolean z;
        synchronized (this.mLock) {
            if (getServiceLocked() == null) {
                z = false;
            } else {
                z = this.mIsEnabled;
            }
        }
        return z;
    }

    public boolean isTouchExplorationEnabled() {
        boolean z;
        synchronized (this.mLock) {
            if (getServiceLocked() == null) {
                z = false;
            } else {
                z = this.mIsTouchExplorationEnabled;
            }
        }
        return z;
    }

    public boolean isHighTextContrastEnabled() {
        boolean z;
        synchronized (this.mLock) {
            if (getServiceLocked() == null) {
                z = false;
            } else {
                z = this.mIsHighTextContrastEnabled;
            }
        }
        return z;
    }

    public void interrupt() {
        synchronized (this.mLock) {
            IAccessibilityManager service = getServiceLocked();
            if (service == null) {
            } else if (this.mIsEnabled) {
                int userId = this.mUserId;
                try {
                    service.interrupt(userId);
                } catch (RemoteException re) {
                    Log.e(LOG_TAG, "Error while requesting interrupt from all services. ", re);
                }
            } else {
                throw new IllegalStateException("Accessibility off. Did you forget to check that?");
            }
        }
    }

    @Deprecated
    public List<ServiceInfo> getAccessibilityServiceList() {
        List<AccessibilityServiceInfo> infos = getInstalledAccessibilityServiceList();
        List<ServiceInfo> services = new ArrayList();
        int infoCount = infos.size();
        for (int i = 0; i < infoCount; i++) {
            services.add(((AccessibilityServiceInfo) infos.get(i)).getResolveInfo().serviceInfo);
        }
        return Collections.unmodifiableList(services);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getInstalledAccessibilityServiceList() {
        /*
        r6 = this;
        r5 = r6.mLock;
        monitor-enter(r5);
        r1 = r6.getServiceLocked();	 Catch:{ all -> 0x001e }
        if (r1 != 0) goto L_0x000f;
    L_0x0009:
        r4 = java.util.Collections.emptyList();	 Catch:{ all -> 0x001e }
        monitor-exit(r5);	 Catch:{ all -> 0x001e }
    L_0x000e:
        return r4;
    L_0x000f:
        r3 = r6.mUserId;	 Catch:{ all -> 0x001e }
        monitor-exit(r5);	 Catch:{ all -> 0x001e }
        r2 = 0;
        r2 = r1.getInstalledAccessibilityServiceList(r3);	 Catch:{ RemoteException -> 0x0021 }
    L_0x0017:
        if (r2 == 0) goto L_0x002a;
    L_0x0019:
        r4 = java.util.Collections.unmodifiableList(r2);
        goto L_0x000e;
    L_0x001e:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x001e }
        throw r4;
    L_0x0021:
        r0 = move-exception;
        r4 = "AccessibilityManager";
        r5 = "Error while obtaining the installed AccessibilityServices. ";
        android.util.Log.e(r4, r5, r0);
        goto L_0x0017;
    L_0x002a:
        r4 = java.util.Collections.emptyList();
        goto L_0x000e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.getInstalledAccessibilityServiceList():java.util.List<android.accessibilityservice.AccessibilityServiceInfo>");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<android.accessibilityservice.AccessibilityServiceInfo> getEnabledAccessibilityServiceList(int r7) {
        /*
        r6 = this;
        r5 = r6.mLock;
        monitor-enter(r5);
        r1 = r6.getServiceLocked();	 Catch:{ all -> 0x001e }
        if (r1 != 0) goto L_0x000f;
    L_0x0009:
        r4 = java.util.Collections.emptyList();	 Catch:{ all -> 0x001e }
        monitor-exit(r5);	 Catch:{ all -> 0x001e }
    L_0x000e:
        return r4;
    L_0x000f:
        r3 = r6.mUserId;	 Catch:{ all -> 0x001e }
        monitor-exit(r5);	 Catch:{ all -> 0x001e }
        r2 = 0;
        r2 = r1.getEnabledAccessibilityServiceList(r7, r3);	 Catch:{ RemoteException -> 0x0021 }
    L_0x0017:
        if (r2 == 0) goto L_0x002a;
    L_0x0019:
        r4 = java.util.Collections.unmodifiableList(r2);
        goto L_0x000e;
    L_0x001e:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x001e }
        throw r4;
    L_0x0021:
        r0 = move-exception;
        r4 = "AccessibilityManager";
        r5 = "Error while obtaining the installed AccessibilityServices. ";
        android.util.Log.e(r4, r5, r0);
        goto L_0x0017;
    L_0x002a:
        r4 = java.util.Collections.emptyList();
        goto L_0x000e;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.accessibility.AccessibilityManager.getEnabledAccessibilityServiceList(int):java.util.List<android.accessibilityservice.AccessibilityServiceInfo>");
    }

    public boolean addAccessibilityStateChangeListener(AccessibilityStateChangeListener listener) {
        return this.mAccessibilityStateChangeListeners.add(listener);
    }

    public boolean removeAccessibilityStateChangeListener(AccessibilityStateChangeListener listener) {
        return this.mAccessibilityStateChangeListeners.remove(listener);
    }

    public boolean addTouchExplorationStateChangeListener(TouchExplorationStateChangeListener listener) {
        return this.mTouchExplorationStateChangeListeners.add(listener);
    }

    public boolean removeTouchExplorationStateChangeListener(TouchExplorationStateChangeListener listener) {
        return this.mTouchExplorationStateChangeListeners.remove(listener);
    }

    public boolean addHighTextContrastStateChangeListener(HighTextContrastChangeListener listener) {
        return this.mHighTextContrastStateChangeListeners.add(listener);
    }

    public boolean removeHighTextContrastStateChangeListener(HighTextContrastChangeListener listener) {
        return this.mHighTextContrastStateChangeListeners.remove(listener);
    }

    private void setStateLocked(int stateFlags) {
        boolean enabled;
        boolean touchExplorationEnabled;
        boolean highTextContrastEnabled = false;
        if ((stateFlags & 1) != 0) {
            enabled = true;
        } else {
            enabled = false;
        }
        if ((stateFlags & 2) != 0) {
            touchExplorationEnabled = true;
        } else {
            touchExplorationEnabled = false;
        }
        if ((stateFlags & 4) != 0) {
            highTextContrastEnabled = true;
        }
        boolean wasEnabled = this.mIsEnabled;
        boolean wasTouchExplorationEnabled = this.mIsTouchExplorationEnabled;
        boolean wasHighTextContrastEnabled = this.mIsHighTextContrastEnabled;
        this.mIsEnabled = enabled;
        this.mIsTouchExplorationEnabled = touchExplorationEnabled;
        this.mIsHighTextContrastEnabled = highTextContrastEnabled;
        if (wasEnabled != enabled) {
            this.mHandler.sendEmptyMessage(1);
        }
        if (wasTouchExplorationEnabled != touchExplorationEnabled) {
            this.mHandler.sendEmptyMessage(2);
        }
        if (wasHighTextContrastEnabled != highTextContrastEnabled) {
            this.mHandler.sendEmptyMessage(3);
        }
    }

    public int addAccessibilityInteractionConnection(IWindow windowToken, IAccessibilityInteractionConnection connection) {
        int i = -1;
        synchronized (this.mLock) {
            IAccessibilityManager service = getServiceLocked();
            if (service == null) {
            } else {
                int userId = this.mUserId;
                try {
                    i = service.addAccessibilityInteractionConnection(windowToken, connection, userId);
                } catch (RemoteException re) {
                    Log.e(LOG_TAG, "Error while adding an accessibility interaction connection. ", re);
                }
            }
        }
        return i;
    }

    public void removeAccessibilityInteractionConnection(IWindow windowToken) {
        synchronized (this.mLock) {
            IAccessibilityManager service = getServiceLocked();
            if (service == null) {
                return;
            }
            try {
                service.removeAccessibilityInteractionConnection(windowToken);
            } catch (RemoteException re) {
                Log.e(LOG_TAG, "Error while removing an accessibility interaction connection. ", re);
            }
        }
    }

    private IAccessibilityManager getServiceLocked() {
        if (this.mService == null) {
            tryConnectToServiceLocked();
        }
        return this.mService;
    }

    private void tryConnectToServiceLocked() {
        IBinder iBinder = ServiceManager.getService("accessibility");
        if (iBinder != null) {
            IAccessibilityManager service = IAccessibilityManager.Stub.asInterface(iBinder);
            try {
                setStateLocked(service.addClient(this.mClient, this.mUserId));
                this.mService = service;
            } catch (RemoteException re) {
                Log.e(LOG_TAG, "AccessibilityManagerService is dead", re);
            }
        }
    }

    private void handleNotifyAccessibilityStateChanged() {
        synchronized (this.mLock) {
            boolean isEnabled = this.mIsEnabled;
        }
        Iterator i$ = this.mAccessibilityStateChangeListeners.iterator();
        while (i$.hasNext()) {
            ((AccessibilityStateChangeListener) i$.next()).onAccessibilityStateChanged(isEnabled);
        }
    }

    private void handleNotifyTouchExplorationStateChanged() {
        synchronized (this.mLock) {
            boolean isTouchExplorationEnabled = this.mIsTouchExplorationEnabled;
        }
        Iterator i$ = this.mTouchExplorationStateChangeListeners.iterator();
        while (i$.hasNext()) {
            ((TouchExplorationStateChangeListener) i$.next()).onTouchExplorationStateChanged(isTouchExplorationEnabled);
        }
    }

    private void handleNotifyHighTextContrastStateChanged() {
        synchronized (this.mLock) {
            boolean isHighTextContrastEnabled = this.mIsHighTextContrastEnabled;
        }
        Iterator i$ = this.mHighTextContrastStateChangeListeners.iterator();
        while (i$.hasNext()) {
            ((HighTextContrastChangeListener) i$.next()).onHighTextContrastStateChanged(isHighTextContrastEnabled);
        }
    }

    public boolean setColorBlind(boolean enable, float userParameter) {
        boolean retVal = false;
        try {
            retVal = this.mService.setColorBlind(enable, userParameter);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "setColorBlind Exception:", re);
        }
        return retVal;
    }

    public boolean isColorBlind(int[] nums) {
        boolean retVal = false;
        try {
            retVal = this.mService.isColorBlind(nums);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "isColorBlind Exception:", re);
        }
        return retVal;
    }

    public boolean setmDNIeColorBlind(boolean enable, int[] result) {
        boolean retVal = false;
        try {
            retVal = this.mService.setmDNIeColorBlind(enable, result);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "setmDNIeColorBlind Exception:", re);
        }
        return retVal;
    }

    public boolean setmDNIeNegative(boolean enable) {
        boolean retVal = false;
        try {
            retVal = this.mService.setmDNIeNegative(enable);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "setmDNIeNegative Exception:", re);
        }
        return retVal;
    }

    public boolean setmDNIeAccessibilityMode(int mode, boolean enable) {
        boolean retVal = false;
        try {
            retVal = this.mService.setmDNIeAccessibilityMode(mode, enable);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "setmDNIeAccessibilityMode Exception:", re);
        }
        return retVal;
    }

    public void assistantMenuRegister(IBinder iBinder) {
        try {
            Log.e(LOG_TAG, "assistantMenuRegister invoking from manager start:");
            this.mService.assistantMenuRegister(iBinder);
            Log.e(LOG_TAG, "assistantMenuRegister invoking from manager end:");
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "assistantMenuRegister Exception:", re);
        }
    }

    public void assistantMenuUpdate(Bundle bundle) {
        try {
            this.mService.assistantMenuUpdate(bundle);
            Log.e(LOG_TAG, "assistantMenuUpdate invoking from manager:");
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "assistantMenuUpdate Exception:", re);
        }
    }

    public void shutdown(boolean isConfirm) {
        try {
            this.mService.shutdown(isConfirm);
            Log.e(LOG_TAG, "shutdown from manager:");
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "shutdown Exception:", re);
        }
    }

    public void reboot(boolean isConfirm) {
        try {
            this.mService.reboot(isConfirm);
            Log.e(LOG_TAG, "reboot from manager:");
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "reboot Exception:", re);
        }
    }

    public void openGlobalActions() {
        try {
            this.mService.openGlobalActions();
            Log.e(LOG_TAG, "global actions from manager:");
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "global actions Exception:", re);
        }
    }

    public void enableMagnifier(int width, int height, float scale) {
        try {
            this.mService.enableMagnifier(width, height, scale);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "enableMagnifier Exception:", re);
        }
    }

    public void enableMagnifierByDisplayID(int width, int height, float scale, int displayId) {
        try {
            this.mService.enableMagnifierByDisplayID(width, height, scale, displayId);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "enableMagnifier Exception:", re);
        }
    }

    public void disableMagnifier() {
        try {
            this.mService.disableMagnifier();
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "enableMagnifier Exception:", re);
        }
    }

    public void showMagnifier(float x, float y) {
        try {
            MagnificationSpec spec = MagnificationSpec.obtain();
            spec.offsetX = x;
            spec.offsetY = y;
            this.mService.setMagnificationSpec(spec);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "setMagnificationSpec Exception:", re);
        }
    }

    public void hideMagnifier() {
        try {
            this.mService.setMagnificationSpec(null);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "setMagnificationSpec Exception:", re);
        }
    }

    public void setTwoFingerGestureRecognitionEnabled(boolean enable) {
        if (this.mIsEnabled) {
            try {
                Log.i(LOG_TAG, "AccessibilityManager - setTwoFingerGestureRecognitionEnabled: " + enable);
                this.mService.setTwoFingerGestureRecognitionEnabled(enable);
                return;
            } catch (RemoteException re) {
                Log.e(LOG_TAG, "setTwoFingerGestureRecognitionEnabled Exception:", re);
                return;
            }
        }
        throw new IllegalStateException("Accessibility off. Did you forget to check that?");
    }

    public boolean isTwoFingerGestureRecognitionEnabled() {
        boolean value = false;
        try {
            value = this.mService.isTwoFingerGestureRecognitionEnabled();
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "isTwoFingerGestureRecognitionEnabled Exception:", re);
        }
        return value;
    }

    public boolean isUniversalSwitchEnabled() {
        boolean result = false;
        try {
            result = this.mService.isUniversalSwitchEnabled();
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "isUniversalSwitchEnabled Exception:", re);
        }
        return result;
    }

    public boolean registerGestureListenerForLauncher(IBinder iBinder) {
        try {
            return this.mService.registerGestureListenerForLauncher(iBinder);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "registerGestureListenerForLauncher Exception:", re);
            return false;
        }
    }

    public void unregisterGestureListenerForLauncher() {
        try {
            this.mService.unregisterGestureListenerForLauncher();
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "unregisterGestureListenerForLauncher Exception:", re);
        }
    }

    public boolean OnStartGestureWakeup() {
        boolean result = false;
        try {
            result = this.mService.OnStartGestureWakeup();
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "OnStartGestureWakeup Exception:", re);
        }
        return result;
    }

    public boolean OnStopGestureWakeup() {
        boolean result = false;
        try {
            result = this.mService.OnStopGestureWakeup();
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "OnStopGestureWakeup Exception:", re);
        }
        return result;
    }

    public boolean isScreenCurtainRunning() {
        boolean result = false;
        try {
            result = this.mService.isScreenCurtainRunning();
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "isScreenCurtainRunning Exception:", re);
        }
        return result;
    }

    public void setScreenCurtain() {
        try {
            this.mService.setScreenCurtain();
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "setScreenCurtain Exception:", re);
        }
    }

    public int getClearableActiveNotificationsCount(String callingPkg) {
        int countable = 0;
        try {
            countable = this.mService.getClearableActiveNotificationsCount(callingPkg);
        } catch (RemoteException re) {
            Log.e(LOG_TAG, "getClearableActiveNotificationsCount Exception:", re);
        }
        return countable;
    }
}
