package com.samsung.android.contextaware.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.samsung.android.contextaware.ContextList;
import com.samsung.android.contextaware.ContextList.ContextType;
import com.samsung.android.contextaware.InterruptModeContextList;
import com.samsung.android.contextaware.manager.IContextAwareService.Stub;
import com.samsung.android.contextaware.manager.fault.CmdProcessResultManager;
import com.samsung.android.contextaware.manager.fault.FaultDetectionManager;
import com.samsung.android.contextaware.manager.fault.RestoreManager;
import com.samsung.android.contextaware.utilbundle.autotest.CaAutoTestScenarioManager;
import com.samsung.android.contextaware.utilbundle.logger.CaLogger;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class ContextAwareService extends Stub implements IContextObserver {
    public static final int BINDER_DIED_OPERATION = 3;
    public static final int NORMAL_OPERATION = 1;
    private static final int NOTIFY_WATING_TIME = 6;
    public static final int RESTORE_OPERATION = 2;
    private volatile boolean isVersionSetting = false;
    private CaAutoTestScenarioManager mAutoTest;
    private boolean mCmdProcessResultNotifyCompletion = true;
    private boolean mContextCollectionResultNotifyCompletion = true;
    private ContextManager mContextManager;
    private ReentrantLock mMutex;
    private ServiceHandler mServiceHandler;
    private int mVersion = 1;

    public final class Listener implements DeathRecipient {
        private CmdProcessResultManager mCmdProcessResultManager;
        private final ConcurrentHashMap<Integer, Integer> mServices = new ConcurrentHashMap();
        private final IBinder mToken;

        Listener(IBinder token) {
            this.mToken = token;
        }

        public void binderDied() {
            ContextAwareService.this.mMutex.lock();
            try {
                CaLogger.warning("[binderDied 01] Mutex is locked for binderDied");
                Iterator<Integer> iter = this.mServices.keySet().iterator();
                Iterator<?> j = iter;
                while (iter.hasNext()) {
                    int service = ((Integer) j.next()).intValue();
                    this.mServices.remove(Integer.valueOf(service));
                    ContextAwareService.this.mContextManager.stop(this, ContextList.getInstance().getServiceCode(service), null, this.mCmdProcessResultManager, 3);
                }
                ListenerListManager.getInstance().removeListener(this);
                this.mToken.unlinkToDeath(this, 0);
                CaLogger.warning("[binderDied 02] Mutex is unlocked for binderDied");
            } finally {
                ContextAwareService.this.mMutex.unlock();
            }
        }

        private synchronized void callback(int type, Bundle context) {
            try {
                IContextAwareCallback callback = IContextAwareCallback.Stub.asInterface(this.mToken);
                if (callback != null) {
                    callback.caCallback(type, context);
                    ContextAwareService.this.mContextCollectionResultNotifyCompletion = true;
                }
                notifyAll();
            } catch (DeadObjectException de) {
                CaLogger.exception(de);
                notifyAll();
            } catch (RemoteException e) {
                CaLogger.exception(e);
                notifyAll();
            } catch (Throwable th) {
                notifyAll();
            }
        }

        public ConcurrentHashMap<Integer, Integer> getServices() {
            return this.mServices;
        }

        public void increaseServiceCount(int service) {
            if (this.mServices.containsKey(Integer.valueOf(service))) {
                this.mServices.put(Integer.valueOf(service), Integer.valueOf(((Integer) this.mServices.get(Integer.valueOf(service))).intValue() + 1));
                return;
            }
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_COUNT_FAULT.getCode()));
        }

        public void decreaseServiceCount(int service) {
            if (this.mServices.containsKey(Integer.valueOf(service))) {
                this.mServices.put(Integer.valueOf(service), Integer.valueOf(((Integer) this.mServices.get(Integer.valueOf(service))).intValue() - 1));
                return;
            }
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_COUNT_FAULT.getCode()));
        }

        public IBinder getToken() {
            return this.mToken;
        }

        public final void setContextCollectionResultNotifyCompletion(boolean completion) {
            ContextAwareService.this.mContextCollectionResultNotifyCompletion = completion;
        }
    }

    @SuppressLint({"HandlerLeak"})
    public final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        public synchronized void handleMessage(Message msg) {
            int type = msg.what;
            Bundle context = (Bundle) ((Bundle) msg.obj).clone();
            if (notifyOperationCheckResult(type, context)) {
                ContextAwareService.this.mCmdProcessResultNotifyCompletion = true;
            } else {
                callback(type, context);
            }
            notifyAll();
        }

        private void callback(int type, Bundle context) {
            Iterator<?> i = ListenerListManager.getInstance().getListenerList().iterator();
            while (i.hasNext()) {
                Listener next = (Listener) i.next();
                if (next != null && next.mServices.containsKey(Integer.valueOf(type))) {
                    next.callback(type, context);
                }
            }
            i = ListenerListManager.getInstance().getWatcherList().iterator();
            while (i.hasNext()) {
                Watcher next2 = (Watcher) i.next();
                if (next2 != null && next2.mServices.containsKey(Integer.valueOf(type))) {
                    next2.callback(type, context);
                }
            }
        }

        private boolean notifyOperationCheckResult(int type, Bundle context) {
            if (type != ContextType.CMD_PROCESS_FAULT_DETECTION.ordinal()) {
                return false;
            }
            if (context == null) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_CONTEXT_NULL_EXCEPTION.getMessage());
                return false;
            }
            Bundle bundle = context.getBundle("Listener");
            if (bundle == null) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_BUNDLE_NULL_EXCEPTION.getMessage());
                return true;
            }
            IBinder binder = bundle.getIBinder("Binder");
            int service = bundle.getInt("Service");
            if (binder == null) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_BINDER_NULL_EXCEPTION.getMessage());
                return true;
            }
            Listener listener = ListenerListManager.getInstance().getListener(binder);
            if (listener == null) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_LISTENER_NULL_EXCEPTION.getMessage());
                if (context.getInt("CheckResult") != 0) {
                    CaLogger.info("This cmd proccess was stopped and that's because the fault detection result is not success");
                    ContextAwareService.this.mContextCollectionResultNotifyCompletion = true;
                }
            } else if (context.getInt("Service") == service) {
                context.remove("Listener");
                listener.callback(type, context);
            } else {
                CaLogger.error(ContextAwareServiceErrors.ERROR_SERVICE_FAULT.getMessage());
            }
            return true;
        }
    }

    public final class Watcher implements DeathRecipient {
        private final ConcurrentHashMap<Integer, Integer> mServices = new ConcurrentHashMap();
        private final IBinder mToken;

        Watcher(IBinder token) {
            this.mToken = token;
        }

        public void binderDied() {
            ContextAwareService.this.mMutex.lock();
            try {
                CaLogger.warning("[binderDied 01] Mutex is locked for binderDied");
                Iterator<Integer> iter = this.mServices.keySet().iterator();
                Iterator<?> j = iter;
                while (iter.hasNext()) {
                    this.mServices.remove(Integer.valueOf(((Integer) j.next()).intValue()));
                }
                ListenerListManager.getInstance().removeWatcher(this);
                this.mToken.unlinkToDeath(this, 0);
                CaLogger.warning("[binderDied 02] Mutex is unlocked for binderDied");
            } finally {
                ContextAwareService.this.mMutex.unlock();
            }
        }

        private synchronized void callback(int type, Bundle context) {
            try {
                IContextAwareCallback callback = IContextAwareCallback.Stub.asInterface(this.mToken);
                if (callback != null) {
                    callback.caCallback(type, context);
                }
                notifyAll();
            } catch (DeadObjectException de) {
                CaLogger.exception(de);
                notifyAll();
            } catch (RemoteException e) {
                CaLogger.exception(e);
                notifyAll();
            } catch (Throwable th) {
                notifyAll();
            }
        }

        public ConcurrentHashMap<Integer, Integer> getServices() {
            return this.mServices;
        }

        public void increaseServiceCount(int service) {
            if (this.mServices.containsKey(Integer.valueOf(service))) {
                this.mServices.put(Integer.valueOf(service), Integer.valueOf(((Integer) this.mServices.get(Integer.valueOf(service))).intValue() + 1));
                return;
            }
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_COUNT_FAULT.getCode()));
        }

        public void decreaseServiceCount(int service) {
            if (this.mServices.containsKey(Integer.valueOf(service))) {
                this.mServices.put(Integer.valueOf(service), Integer.valueOf(((Integer) this.mServices.get(Integer.valueOf(service))).intValue() - 1));
                return;
            }
            CaLogger.error(ContextAwareServiceErrors.getMessage(ContextAwareServiceErrors.ERROR_SERVICE_COUNT_FAULT.getCode()));
        }
    }

    public ContextAwareService(Context context) {
        HandlerThread handlerThread = new HandlerThread("context_aware");
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        if (looper == null) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_LOOPER_NULL_EXCEPTION.getMessage());
            return;
        }
        this.mServiceHandler = new ServiceHandler(looper);
        this.mContextManager = new ContextManager(context, looper, this.mVersion);
        this.mAutoTest = new CaAutoTestScenarioManager(context);
        this.mMutex = new ReentrantLock(true);
        ListenerListManager.getInstance().setCreator(this.mContextManager.getCreator());
        FaultDetectionManager.getInstance().initializeManager(this.mContextManager);
    }

    public final void registerCallback(IBinder binder, int service) throws RemoteException {
        this.mMutex.lock();
        try {
            CaLogger.warning("[regi 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(service));
            FaultDetectionManager.getInstance().initializeRestoreManager();
            this.mCmdProcessResultNotifyCompletion = false;
            this.mContextCollectionResultNotifyCompletion = true;
            boolean isExistListener = false;
            Iterator<?> i = ListenerListManager.getInstance().getListenerList().iterator();
            while (i.hasNext()) {
                Listener next = (Listener) i.next();
                if (binder.equals(next.mToken)) {
                    doCommendProcess(RestoreManager.REGISTER_CMD_RESTORE, next, service);
                    isExistListener = true;
                    break;
                }
            }
            if (!isExistListener) {
                Listener listener = new Listener(binder);
                listener.mCmdProcessResultManager = new CmdProcessResultManager(listener.mToken, this.mServiceHandler);
                binder.linkToDeath(listener, 0);
                ListenerListManager.getInstance().addListener(listener);
                doCommendProcess(RestoreManager.REGISTER_CMD_RESTORE, listener, service);
            }
            displayUsedCountForService(service);
            showListenerList();
            FaultDetectionManager.getInstance().unregisterCmdProcessResultManager();
            CaLogger.warning("[regi 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(service));
        } finally {
            this.mMutex.unlock();
        }
    }

    public final boolean unregisterCallback(IBinder binder, int service) throws RemoteException {
        boolean isEmptyListener = true;
        this.mMutex.lock();
        try {
            CaLogger.warning("[unregi 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(service));
            FaultDetectionManager.getInstance().initializeRestoreManager();
            this.mCmdProcessResultNotifyCompletion = false;
            this.mContextCollectionResultNotifyCompletion = true;
            Listener listener = null;
            Iterator<?> i = ListenerListManager.getInstance().getListenerList().iterator();
            while (i.hasNext()) {
                Listener next = (Listener) i.next();
                if (binder.equals(next.mToken)) {
                    listener = next;
                    doCommendProcess(RestoreManager.UNREGISTER_CMD_RESTORE, next, service);
                    break;
                }
            }
            if (listener == null || !listener.mServices.isEmpty()) {
                isEmptyListener = false;
            }
            if (isEmptyListener) {
                binder.unlinkToDeath(listener, 0);
                ListenerListManager.getInstance().removeListener(listener);
            }
            displayUsedCountForService(service);
            showListenerList();
            FaultDetectionManager.getInstance().unregisterCmdProcessResultManager();
            CaLogger.warning("[unregi 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(service));
            return isEmptyListener;
        } finally {
            this.mMutex.unlock();
        }
    }

    public final void registerWatcher(IBinder binder, int service) throws RemoteException {
        this.mMutex.lock();
        try {
            Watcher watcher;
            CaLogger.warning("[regi 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(service));
            boolean isExistWatcher = false;
            Iterator<?> i = ListenerListManager.getInstance().getWatcherList().iterator();
            while (i.hasNext()) {
                Watcher next = (Watcher) i.next();
                if (binder.equals(next.mToken)) {
                    isExistWatcher = true;
                    if (!next.mServices.containsKey(Integer.valueOf(service))) {
                        next.mServices.put(Integer.valueOf(service), Integer.valueOf(1));
                    }
                    if (!isExistWatcher) {
                        watcher = new Watcher(binder);
                        binder.linkToDeath(watcher, 0);
                        ListenerListManager.getInstance().addWatcher(watcher);
                        if (!watcher.mServices.containsKey(Integer.valueOf(service))) {
                            watcher.mServices.put(Integer.valueOf(service), Integer.valueOf(1));
                        }
                    }
                    CaLogger.warning("[regi 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(service));
                }
            }
            if (isExistWatcher) {
                watcher = new Watcher(binder);
                binder.linkToDeath(watcher, 0);
                ListenerListManager.getInstance().addWatcher(watcher);
                if (watcher.mServices.containsKey(Integer.valueOf(service))) {
                    watcher.mServices.put(Integer.valueOf(service), Integer.valueOf(1));
                }
            }
            CaLogger.warning("[regi 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(service));
        } finally {
            this.mMutex.unlock();
        }
    }

    public final boolean unregisterWatcher(IBinder binder, int service) throws RemoteException {
        boolean isEmptyWatcher = true;
        this.mMutex.lock();
        try {
            CaLogger.warning("[unregi 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(service));
            Watcher watcher = null;
            Iterator<?> i = ListenerListManager.getInstance().getWatcherList().iterator();
            while (i.hasNext()) {
                Watcher next = (Watcher) i.next();
                if (binder.equals(next.mToken)) {
                    watcher = next;
                    if (next.mServices.containsKey(Integer.valueOf(service))) {
                        next.decreaseServiceCount(service);
                        if (((Integer) next.mServices.get(Integer.valueOf(service))).intValue() <= 0) {
                            next.mServices.remove(Integer.valueOf(service));
                        }
                    }
                    if (watcher == null || !watcher.mServices.isEmpty()) {
                        isEmptyWatcher = false;
                    }
                    if (isEmptyWatcher) {
                        binder.unlinkToDeath(watcher, 0);
                        ListenerListManager.getInstance().removeWatcher(watcher);
                    }
                    CaLogger.warning("[unregi 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(service));
                    return isEmptyWatcher;
                }
            }
            isEmptyWatcher = false;
            if (isEmptyWatcher) {
                binder.unlinkToDeath(watcher, 0);
                ListenerListManager.getInstance().removeWatcher(watcher);
            }
            CaLogger.warning("[unregi 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(service));
            return isEmptyWatcher;
        } finally {
            this.mMutex.unlock();
        }
    }

    private void showListenerList() {
        CaLogger.debug("===== Context Aware Service List =====");
        Iterator<?> i = ListenerListManager.getInstance().getListenerList().iterator();
        while (i.hasNext()) {
            Listener next = (Listener) i.next();
            Iterator<Integer> iter = next.mServices.keySet().iterator();
            Iterator<?> j = iter;
            while (iter.hasNext()) {
                int service = ((Integer) j.next()).intValue();
                CaLogger.info("Listener : " + next.toString() + ", Service : " + ContextList.getInstance().getServiceCode(service) + "(" + ((Integer) next.mServices.get(Integer.valueOf(service))).intValue() + ")");
            }
        }
    }

    private void doCommendProcess(String operation, Listener listener, int service) {
        if (!InterruptModeContextList.getInstance().isInterruptModeType(service)) {
            if (operation.equals(RestoreManager.REGISTER_CMD_RESTORE) && !isUsableService(listener, service)) {
                return;
            }
            if (operation.equals(RestoreManager.UNREGISTER_CMD_RESTORE) && isUsableService(listener, service)) {
                return;
            }
        }
        FaultDetectionManager.getInstance().registerCmdProcessResultManager(listener.mCmdProcessResultManager);
        if (operation.equals(RestoreManager.REGISTER_CMD_RESTORE)) {
            if (!listener.mServices.containsKey(Integer.valueOf(service))) {
                listener.mServices.put(Integer.valueOf(service), Integer.valueOf(0));
            }
            listener.increaseServiceCount(service);
            this.mContextManager.start(listener, ContextList.getInstance().getServiceCode(service), this, 1);
        } else if (operation.equals(RestoreManager.UNREGISTER_CMD_RESTORE)) {
            if (listener.mServices.containsKey(Integer.valueOf(service))) {
                listener.decreaseServiceCount(service);
                if (((Integer) listener.mServices.get(Integer.valueOf(service))).intValue() <= 0) {
                    listener.mServices.remove(Integer.valueOf(service));
                }
            }
            this.mContextManager.stop(listener, ContextList.getInstance().getServiceCode(service), this, null, 1);
        }
        if (!waitForNotifyOperationCheckResult()) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_TIME_OUT.getMessage());
            FaultDetectionManager.getInstance().setRestoreEnable();
        }
        CaLogger.debug("complete notify the operation result.");
        if (FaultDetectionManager.getInstance().isRestoreEnable()) {
            FaultDetectionManager.getInstance().runRestore(operation, listener, service, this);
        } else if (operation.equals(RestoreManager.REGISTER_CMD_RESTORE)) {
            this.mContextManager.notifyInitContext(ContextList.getInstance().getServiceCode(service));
        }
    }

    public final void getContextInfo(IBinder binder, int service) {
        this.mMutex.lock();
        try {
            CaLogger.warning("[getContext 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(service));
            boolean isListener = false;
            Listener next = null;
            this.mCmdProcessResultNotifyCompletion = true;
            this.mContextCollectionResultNotifyCompletion = false;
            Iterator<?> i = ListenerListManager.getInstance().getListenerList().iterator();
            while (i.hasNext()) {
                next = (Listener) i.next();
                if (binder.equals(next.mToken)) {
                    if (!next.mServices.containsKey(Integer.valueOf(service))) {
                        next.mServices.put(Integer.valueOf(service), Integer.valueOf(1));
                    }
                    FaultDetectionManager.getInstance().registerCmdProcessResultManager(new CmdProcessResultManager(next.mToken, this.mServiceHandler));
                    ListenerListManager.getInstance().addListener(next);
                    this.mContextManager.getContextInfo(next, ContextList.getInstance().getServiceCode(service), this);
                    isListener = true;
                    if (!isListener) {
                        CaLogger.error(ContextAwareServiceErrors.ERROR_LISTENER_NOT_REGISTERED.getMessage());
                    } else if (!waitForNotifyOperationCheckResult()) {
                        CaLogger.error(ContextAwareServiceErrors.ERROR_TIME_OUT.getMessage());
                    }
                    this.mContextManager.unregisterObservers(ContextList.getInstance().getServiceCode(service), this);
                    if (next != null) {
                        if (next.mServices.size() != 1 && next.mServices.contains(Integer.valueOf(service))) {
                            ListenerListManager.getInstance().removeListener(next);
                        } else if (next.mServices.contains(Integer.valueOf(service))) {
                            next.mServices.remove(Integer.valueOf(service));
                        }
                    }
                    FaultDetectionManager.getInstance().unregisterCmdProcessResultManager();
                    CaLogger.warning("[getContext 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(service));
                }
            }
            if (!isListener) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_LISTENER_NOT_REGISTERED.getMessage());
            } else if (waitForNotifyOperationCheckResult()) {
                CaLogger.error(ContextAwareServiceErrors.ERROR_TIME_OUT.getMessage());
            }
            this.mContextManager.unregisterObservers(ContextList.getInstance().getServiceCode(service), this);
            if (next != null) {
                if (next.mServices.size() != 1) {
                }
                if (next.mServices.contains(Integer.valueOf(service))) {
                    next.mServices.remove(Integer.valueOf(service));
                }
            }
            FaultDetectionManager.getInstance().unregisterCmdProcessResultManager();
            CaLogger.warning("[getContext 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(service));
        } finally {
            this.mMutex.unlock();
        }
    }

    private void displayUsedCountForService(int service) {
        CaLogger.info("totalCnt = " + Integer.toString(ListenerListManager.getInstance().getUsedTotalCount(ContextList.getInstance().getServiceCode(service))) + ", serviceCount = " + Integer.toString(ListenerListManager.getInstance().getUsedServiceCount(ContextList.getInstance().getServiceCode(service))) + ", subCollectionCount = " + Integer.toString(ListenerListManager.getInstance().getUsedSubCollectionCount(ContextList.getInstance().getServiceCode(service))));
    }

    private boolean waitForNotifyOperationCheckResult() {
        int i = 0;
        while (i < 600) {
            try {
                Thread.sleep(10);
                if (this.mCmdProcessResultNotifyCompletion && this.mContextCollectionResultNotifyCompletion) {
                    return true;
                }
                i++;
            } catch (InterruptedException e) {
                CaLogger.exception(e);
            }
        }
        return false;
    }

    public final boolean setCAProperty(int service, int property, ContextAwarePropertyBundle value) {
        boolean result = false;
        this.mMutex.lock();
        try {
            CaLogger.warning("[setProperty 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(service));
            result = this.mContextManager.setProperty(ContextList.getInstance().getServiceCode(service), property, value);
            CaLogger.info("result : " + Boolean.toString(result));
            CaLogger.warning("[setProperty 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(service));
            return result;
        } finally {
            this.mMutex.unlock();
        }
    }

    public final void resetCAService(int service) {
        this.mMutex.lock();
        try {
            CaLogger.warning("[reset 01] Mutex is locked for " + ContextList.getInstance().getServiceCode(service));
            CaLogger.info("reset service : " + ContextList.getInstance().getServiceCode(service));
            this.mContextManager.reset(ContextList.getInstance().getServiceCode(service));
            CaLogger.warning("[reset 02] Mutex is unlocked for " + ContextList.getInstance().getServiceCode(service));
        } finally {
            this.mMutex.unlock();
        }
    }

    public final void updateContext(String type, Bundle context) {
        Message msg = Message.obtain();
        msg.what = ContextList.getInstance().getServiceOrdinal(type);
        msg.obj = context;
        this.mServiceHandler.sendMessage(msg);
    }

    private boolean isUsableService(Listener listener, int service) {
        return !listener.mServices.containsKey(Integer.valueOf(service));
    }

    public final void setCALogger(boolean isConsole, boolean isFile, int level, boolean isCaller) {
        CaLogger.setConsoleLoggingEnable(isConsole);
        CaLogger.setFileLoggingEnable(isFile);
        CaLogger.setLogOption(level, isCaller);
    }

    public final void initializeAutoTest() {
        this.mAutoTest.initilizeAutoTest();
    }

    public final void startAutoTest() {
        this.mAutoTest.startAutoTest();
    }

    public final void stopAutoTest() {
        this.mAutoTest.stopAutoTest();
    }

    public final void setCmdProcessResultNotifyCompletion(boolean completion) {
        this.mCmdProcessResultNotifyCompletion = completion;
    }

    public final boolean setScenarioForTest(int testType, int delayTime) {
        return this.mAutoTest.setScenarioForTest(testType, delayTime);
    }

    public final boolean setScenarioForDebugging(int testType, int delayTime, byte[] packet) {
        return this.mAutoTest.setScenarioForDebugging(testType, delayTime, packet);
    }

    public final void setVersion(int version) {
        if (this.isVersionSetting) {
            CaLogger.error(ContextAwareServiceErrors.ERROR_VERSION_SETTING.getMessage());
            return;
        }
        CaLogger.info("Version : " + Integer.toString(version));
        this.mVersion = version;
        this.mContextManager.setVersion(version);
        this.isVersionSetting = true;
    }

    public final int getVersion() {
        return this.mVersion;
    }
}
