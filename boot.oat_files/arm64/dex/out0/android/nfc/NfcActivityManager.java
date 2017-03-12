package android.nfc;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.ComponentName;
import android.net.Uri;
import android.nfc.IAppCallback.Stub;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.LedCoverNotificationCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcAdapter.ReaderCallback;
import android.os.Binder;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public final class NfcActivityManager extends Stub implements ActivityLifecycleCallbacks {
    static final Boolean DBG = Boolean.valueOf(false);
    private static final String NFC_PERM = "android.permission.NFC";
    static final String TAG = "NFC";
    private static LedCoverNotificationCallback mLedCallback = null;
    final List<NfcActivityState> mActivities = new LinkedList();
    final NfcAdapter mAdapter;
    final List<NfcApplicationState> mApps = new ArrayList(1);

    class NfcActivityState {
        Activity activity;
        int flags = 0;
        int listenModeFlags = -1;
        NdefMessage ndefMessage = null;
        CreateNdefMessageCallback ndefMessageCallback = null;
        OnNdefPushCompleteCallback onNdefPushCompleteCallback = null;
        String proto = null;
        ReaderCallback readerCallback = null;
        Bundle readerModeExtras = null;
        int readerModeFlags = 0;
        boolean resumed = false;
        List<ComponentName> services = null;
        String tech = null;
        Binder token;
        CreateBeamUrisCallback uriCallback = null;
        Uri[] uris = null;
        int userId = -1;

        public NfcActivityState(Activity activity) {
            if (activity.getWindow().isDestroyed()) {
                throw new IllegalStateException("activity is already destroyed");
            }
            activity.enforceCallingOrSelfPermission("android.permission.NFC", "NFC permission required.");
            this.resumed = activity.isResumed();
            this.activity = activity;
            this.token = new Binder();
            NfcActivityManager.this.registerApplication(activity.getApplication());
        }

        public void destroy() {
            NfcActivityManager.this.unregisterApplication(this.activity.getApplication());
            this.resumed = false;
            this.activity = null;
            this.ndefMessage = null;
            this.ndefMessageCallback = null;
            this.onNdefPushCompleteCallback = null;
            this.uriCallback = null;
            this.uris = null;
            this.readerModeFlags = 0;
            this.token = null;
            this.listenModeFlags = -1;
            this.tech = null;
            this.proto = null;
            this.services = null;
            this.userId = -1;
        }

        public String toString() {
            StringBuilder s = new StringBuilder("[").append(" ");
            s.append(this.ndefMessage).append(" ").append(this.ndefMessageCallback).append(" ");
            s.append(this.uriCallback).append(" ");
            if (this.uris != null) {
                for (Uri uri : this.uris) {
                    s.append(this.onNdefPushCompleteCallback).append(" ").append(uri).append("]");
                }
            }
            return s.toString();
        }
    }

    class NfcApplicationState {
        final Application app;
        int refCount = 0;

        public NfcApplicationState(Application app) {
            this.app = app;
        }

        public void register() {
            this.refCount++;
            if (this.refCount == 1) {
                this.app.registerActivityLifecycleCallbacks(NfcActivityManager.this);
            }
        }

        public void unregister() {
            this.refCount--;
            if (this.refCount == 0) {
                this.app.unregisterActivityLifecycleCallbacks(NfcActivityManager.this);
            } else if (this.refCount < 0) {
                Log.e(NfcActivityManager.TAG, "-ve refcount for " + this.app);
            }
        }
    }

    NfcApplicationState findAppState(Application app) {
        for (NfcApplicationState appState : this.mApps) {
            if (appState.app == app) {
                return appState;
            }
        }
        return null;
    }

    void registerApplication(Application app) {
        NfcApplicationState appState = findAppState(app);
        if (appState == null) {
            appState = new NfcApplicationState(app);
            this.mApps.add(appState);
        }
        appState.register();
    }

    void unregisterApplication(Application app) {
        NfcApplicationState appState = findAppState(app);
        if (appState == null) {
            Log.e(TAG, "app was not registered " + app);
        } else {
            appState.unregister();
        }
    }

    synchronized NfcActivityState findActivityState(Activity activity) {
        NfcActivityState state;
        for (NfcActivityState state2 : this.mActivities) {
            if (state2.activity == activity) {
                break;
            }
        }
        state2 = null;
        return state2;
    }

    synchronized NfcActivityState getActivityState(Activity activity) {
        NfcActivityState state;
        state = findActivityState(activity);
        if (state == null) {
            state = new NfcActivityState(activity);
            this.mActivities.add(state);
        }
        return state;
    }

    synchronized NfcActivityState findResumedActivityState() {
        NfcActivityState state;
        for (NfcActivityState state2 : this.mActivities) {
            if (state2.resumed) {
                break;
            }
        }
        state2 = null;
        return state2;
    }

    synchronized void destroyActivityState(Activity activity) {
        NfcActivityState activityState = findActivityState(activity);
        if (activityState != null) {
            activityState.destroy();
            this.mActivities.remove(activityState);
        }
    }

    public NfcActivityManager(NfcAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void enableReaderMode(Activity activity, ReaderCallback callback, int flags, Bundle extras) {
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.readerCallback = callback;
            state.readerModeFlags = flags;
            state.readerModeExtras = extras;
            Binder token = state.token;
            boolean isResumed = state.resumed;
        }
        if (isResumed) {
            setReaderMode(token, flags, extras);
        }
    }

    public void disableReaderMode(Activity activity) {
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.readerCallback = null;
            state.readerModeFlags = 0;
            state.readerModeExtras = null;
            Binder token = state.token;
            boolean isResumed = state.resumed;
        }
        if (isResumed) {
            setReaderMode(token, 0, null);
        }
    }

    public void setReaderMode(Binder token, int flags, Bundle extras) {
        if (DBG.booleanValue()) {
            Log.d(TAG, "Setting reader mode");
        }
        try {
            NfcAdapter.sService.setReaderMode(token, this, flags, extras);
        } catch (RemoteException e) {
            this.mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    public void enableListenMode(Activity activity, int flags) {
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.listenModeFlags = flags;
            Binder token = state.token;
            boolean isResumed = state.resumed;
        }
        if (isResumed) {
            setListenMode(token, flags);
        }
    }

    public void disableListenMode(Activity activity) {
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.listenModeFlags = -1;
            Binder token = state.token;
            boolean isResumed = state.resumed;
        }
        if (isResumed) {
            setListenMode(token, 255);
        }
    }

    public void changeRouting(int userHandle, Activity activity, String proto, String tech, List<ComponentName> services) {
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            Binder token = state.token;
            state.userId = userHandle;
            state.proto = proto;
            state.tech = tech;
            state.services = services;
            boolean isResumed = state.resumed;
        }
        if (isResumed) {
            changeRoutingTable(token, userHandle, proto, tech, services);
        } else {
            Log.d(TAG, "Activity must be resumed.");
        }
    }

    public void setListenMode(Binder token, int flags) {
        if (DBG.booleanValue()) {
            Log.d(TAG, "Setting Listen mode");
        }
        try {
            NfcAdapter.sService.setListenMode2(token, flags);
        } catch (RemoteException e) {
            this.mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    public void changeRoutingTable(Binder token, int userHandle, String proto, String tech, List<ComponentName> services) {
        try {
            NfcAdapter.sService.changeRoutingTable(token, userHandle, proto, tech, services);
        } catch (RemoteException e) {
            this.mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    public void setNdefPushContentUri(Activity activity, Uri[] uris) {
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.uris = uris;
            boolean isResumed = state.resumed;
        }
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    public void setNdefPushContentUriCallback(Activity activity, CreateBeamUrisCallback callback) {
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.uriCallback = callback;
            boolean isResumed = state.resumed;
        }
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    public void setNdefPushMessage(Activity activity, NdefMessage message, int flags) {
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.ndefMessage = message;
            state.flags = flags;
            boolean isResumed = state.resumed;
        }
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    public void setNdefPushMessageCallback(Activity activity, CreateNdefMessageCallback callback, int flags) {
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.ndefMessageCallback = callback;
            state.flags = flags;
            boolean isResumed = state.resumed;
        }
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    public void setOnNdefPushCompleteCallback(Activity activity, OnNdefPushCompleteCallback callback) {
        synchronized (this) {
            NfcActivityState state = getActivityState(activity);
            state.onNdefPushCompleteCallback = callback;
            boolean isResumed = state.resumed;
        }
        if (isResumed) {
            requestNfcServiceCallback();
        } else {
            verifyNfcPermission();
        }
    }

    public void setLedCoverNtfCallback(LedCoverNotificationCallback callback) {
        mLedCallback = callback;
        try {
            NfcAdapter.sService.setLedCoverCallback(this);
        } catch (RemoteException e) {
            this.mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    public void LedCoverNotification() {
        if (mLedCallback == null) {
            Log.i(TAG, "LedCover Callback is not registed");
        } else {
            mLedCallback.LedCoverNotification();
        }
    }

    void requestNfcServiceCallback() {
        try {
            NfcAdapter.sService.setAppCallback(this);
        } catch (RemoteException e) {
            this.mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    void verifyNfcPermission() {
        try {
            NfcAdapter.sService.verifyNfcPermission();
        } catch (RemoteException e) {
            this.mAdapter.attemptDeadServiceRecovery(e);
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.nfc.BeamShareData createBeamShareData(byte r19) {
        /*
        r18 = this;
        r3 = new android.nfc.NfcEvent;
        r0 = r18;
        r15 = r0.mAdapter;
        r0 = r19;
        r3.<init>(r15, r0);
        r15 = "NFC";
        r16 = "createBeamShareData start new";
        android.util.Log.i(r15, r16);
        monitor-enter(r18);
        r10 = r18.findResumedActivityState();	 Catch:{ all -> 0x004f }
        if (r10 != 0) goto L_0x001c;
    L_0x0019:
        r15 = 0;
        monitor-exit(r18);	 Catch:{ all -> 0x004f }
    L_0x001b:
        return r15;
    L_0x001c:
        r8 = r10.ndefMessageCallback;	 Catch:{ all -> 0x004f }
        r13 = r10.uriCallback;	 Catch:{ all -> 0x004f }
        r7 = r10.ndefMessage;	 Catch:{ all -> 0x004f }
        r12 = r10.uris;	 Catch:{ all -> 0x004f }
        r4 = r10.flags;	 Catch:{ all -> 0x004f }
        r1 = r10.activity;	 Catch:{ all -> 0x004f }
        monitor-exit(r18);	 Catch:{ all -> 0x004f }
        if (r8 == 0) goto L_0x002f;
    L_0x002b:
        r7 = r8.createNdefMessage(r3);
    L_0x002f:
        if (r13 == 0) goto L_0x00db;
    L_0x0031:
        r12 = r13.createBeamUris(r3);
        if (r12 == 0) goto L_0x00db;
    L_0x0037:
        r14 = new java.util.ArrayList;
        r14.<init>();
        r2 = r12;
        r6 = r2.length;
        r5 = 0;
    L_0x003f:
        if (r5 >= r6) goto L_0x00cf;
    L_0x0041:
        r11 = r2[r5];
        if (r11 != 0) goto L_0x0052;
    L_0x0045:
        r15 = "NFC";
        r16 = "Uri not allowed to be null.";
        android.util.Log.e(r15, r16);
    L_0x004c:
        r5 = r5 + 1;
        goto L_0x003f;
    L_0x004f:
        r15 = move-exception;
        monitor-exit(r18);	 Catch:{ all -> 0x004f }
        throw r15;
    L_0x0052:
        r9 = r11.getScheme();
        if (r9 == 0) goto L_0x0068;
    L_0x0058:
        r15 = "file";
        r15 = r9.equalsIgnoreCase(r15);
        if (r15 != 0) goto L_0x0070;
    L_0x0060:
        r15 = "content";
        r15 = r9.equalsIgnoreCase(r15);
        if (r15 != 0) goto L_0x0070;
    L_0x0068:
        r15 = "NFC";
        r16 = "Uri needs to have either scheme file or scheme content";
        android.util.Log.e(r15, r16);
        goto L_0x004c;
    L_0x0070:
        r15 = "NFC";
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = "pre Uri = ";
        r16 = r16.append(r17);
        r0 = r16;
        r16 = r0.append(r11);
        r17 = " UserHandle.myUserId() = ";
        r16 = r16.append(r17);
        r17 = android.os.UserHandle.myUserId();
        r16 = r16.append(r17);
        r16 = r16.toString();
        android.util.Log.i(r15, r16);
        r15 = android.os.UserHandle.myUserId();
        r11 = android.content.ContentProvider.maybeAddUserId(r11, r15);
        r14.add(r11);
        r15 = "NFC";
        r16 = new java.lang.StringBuilder;
        r16.<init>();
        r17 = "post Uri = ";
        r16 = r16.append(r17);
        r0 = r16;
        r16 = r0.append(r11);
        r17 = " UserHandle.myUserId() = ";
        r16 = r16.append(r17);
        r17 = android.os.UserHandle.myUserId();
        r16 = r16.append(r17);
        r16 = r16.toString();
        android.util.Log.i(r15, r16);
        goto L_0x004c;
    L_0x00cf:
        r15 = r14.size();
        r15 = new android.net.Uri[r15];
        r12 = r14.toArray(r15);
        r12 = (android.net.Uri[]) r12;
    L_0x00db:
        if (r12 == 0) goto L_0x00f3;
    L_0x00dd:
        r15 = r12.length;
        if (r15 <= 0) goto L_0x00f3;
    L_0x00e0:
        r2 = r12;
        r6 = r2.length;
        r5 = 0;
    L_0x00e3:
        if (r5 >= r6) goto L_0x00f3;
    L_0x00e5:
        r11 = r2[r5];
        r15 = "com.android.nfc";
        r16 = 1;
        r0 = r16;
        r1.grantUriPermission(r15, r11, r0);
        r5 = r5 + 1;
        goto L_0x00e3;
    L_0x00f3:
        r15 = new android.nfc.BeamShareData;
        r16 = new android.os.UserHandle;
        r17 = android.os.UserHandle.myUserId();
        r16.<init>(r17);
        r0 = r16;
        r15.<init>(r7, r12, r0, r4);
        goto L_0x001b;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcActivityManager.createBeamShareData(byte):android.nfc.BeamShareData");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onNdefPushComplete(byte r5) {
        /*
        r4 = this;
        monitor-enter(r4);
        r2 = r4.findResumedActivityState();	 Catch:{ all -> 0x0019 }
        if (r2 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r4);	 Catch:{ all -> 0x0019 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r2.onNdefPushCompleteCallback;	 Catch:{ all -> 0x0019 }
        monitor-exit(r4);	 Catch:{ all -> 0x0019 }
        r1 = new android.nfc.NfcEvent;
        r3 = r4.mAdapter;
        r1.<init>(r3, r5);
        if (r0 == 0) goto L_0x0008;
    L_0x0015:
        r0.onNdefPushComplete(r1);
        goto L_0x0008;
    L_0x0019:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0019 }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcActivityManager.onNdefPushComplete(byte):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onTagDiscovered(android.nfc.Tag r4) throws android.os.RemoteException {
        /*
        r3 = this;
        monitor-enter(r3);
        r1 = r3.findResumedActivityState();	 Catch:{ all -> 0x0012 }
        if (r1 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r3);	 Catch:{ all -> 0x0012 }
    L_0x0008:
        return;
    L_0x0009:
        r0 = r1.readerCallback;	 Catch:{ all -> 0x0012 }
        monitor-exit(r3);	 Catch:{ all -> 0x0012 }
        if (r0 == 0) goto L_0x0008;
    L_0x000e:
        r0.onTagDiscovered(r4);
        goto L_0x0008;
    L_0x0012:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0012 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcActivityManager.onTagDiscovered(android.nfc.Tag):void");
    }

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    public void onActivityStarted(Activity activity) {
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActivityResumed(android.app.Activity r13) {
        /*
        r12 = this;
        r8 = 0;
        r6 = -1;
        r3 = 0;
        r4 = 0;
        r2 = -1;
        r5 = 0;
        r7 = 0;
        monitor-enter(r12);
        r9 = r12.findActivityState(r13);	 Catch:{ all -> 0x0068 }
        r0 = DBG;	 Catch:{ all -> 0x0068 }
        r0 = r0.booleanValue();	 Catch:{ all -> 0x0068 }
        if (r0 == 0) goto L_0x0037;
    L_0x0014:
        r0 = "NFC";
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0068 }
        r10.<init>();	 Catch:{ all -> 0x0068 }
        r11 = "onResume() for ";
        r10 = r10.append(r11);	 Catch:{ all -> 0x0068 }
        r10 = r10.append(r13);	 Catch:{ all -> 0x0068 }
        r11 = " ";
        r10 = r10.append(r11);	 Catch:{ all -> 0x0068 }
        r10 = r10.append(r9);	 Catch:{ all -> 0x0068 }
        r10 = r10.toString();	 Catch:{ all -> 0x0068 }
        android.util.Log.d(r0, r10);	 Catch:{ all -> 0x0068 }
    L_0x0037:
        if (r9 != 0) goto L_0x003b;
    L_0x0039:
        monitor-exit(r12);	 Catch:{ all -> 0x0068 }
    L_0x003a:
        return;
    L_0x003b:
        r0 = 1;
        r9.resumed = r0;	 Catch:{ all -> 0x0068 }
        r1 = r9.token;	 Catch:{ all -> 0x0068 }
        r8 = r9.readerModeFlags;	 Catch:{ all -> 0x0068 }
        r7 = r9.readerModeExtras;	 Catch:{ all -> 0x0068 }
        r6 = r9.listenModeFlags;	 Catch:{ all -> 0x0068 }
        r3 = r9.proto;	 Catch:{ all -> 0x0068 }
        r4 = r9.tech;	 Catch:{ all -> 0x0068 }
        r5 = r9.services;	 Catch:{ all -> 0x0068 }
        r2 = r9.userId;	 Catch:{ all -> 0x0068 }
        monitor-exit(r12);	 Catch:{ all -> 0x0068 }
        if (r8 == 0) goto L_0x0054;
    L_0x0051:
        r12.setReaderMode(r1, r8, r7);
    L_0x0054:
        r0 = -1;
        if (r6 == r0) goto L_0x005a;
    L_0x0057:
        r12.setListenMode(r1, r6);
    L_0x005a:
        if (r3 != 0) goto L_0x0060;
    L_0x005c:
        if (r4 != 0) goto L_0x0060;
    L_0x005e:
        if (r5 == 0) goto L_0x0064;
    L_0x0060:
        r0 = r12;
        r0.changeRoutingTable(r1, r2, r3, r4, r5);
    L_0x0064:
        r12.requestNfcServiceCallback();
        goto L_0x003a;
    L_0x0068:
        r0 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x0068 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcActivityManager.onActivityResumed(android.app.Activity):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onActivityPaused(android.app.Activity r12) {
        /*
        r11 = this;
        r7 = 1;
        r3 = 0;
        r0 = 0;
        monitor-enter(r11);
        r9 = r11.findActivityState(r12);	 Catch:{ all -> 0x0071 }
        r4 = DBG;	 Catch:{ all -> 0x0071 }
        r4 = r4.booleanValue();	 Catch:{ all -> 0x0071 }
        if (r4 == 0) goto L_0x0033;
    L_0x0010:
        r4 = "NFC";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0071 }
        r5.<init>();	 Catch:{ all -> 0x0071 }
        r10 = "onPause() for ";
        r5 = r5.append(r10);	 Catch:{ all -> 0x0071 }
        r5 = r5.append(r12);	 Catch:{ all -> 0x0071 }
        r10 = " ";
        r5 = r5.append(r10);	 Catch:{ all -> 0x0071 }
        r5 = r5.append(r9);	 Catch:{ all -> 0x0071 }
        r5 = r5.toString();	 Catch:{ all -> 0x0071 }
        android.util.Log.d(r4, r5);	 Catch:{ all -> 0x0071 }
    L_0x0033:
        if (r9 != 0) goto L_0x0037;
    L_0x0035:
        monitor-exit(r11);	 Catch:{ all -> 0x0071 }
    L_0x0036:
        return;
    L_0x0037:
        r4 = 0;
        r9.resumed = r4;	 Catch:{ all -> 0x0071 }
        r1 = r9.token;	 Catch:{ all -> 0x0071 }
        r4 = r9.readerModeFlags;	 Catch:{ all -> 0x0071 }
        if (r4 == 0) goto L_0x006b;
    L_0x0040:
        r8 = r7;
    L_0x0041:
        r4 = r9.listenModeFlags;	 Catch:{ all -> 0x0071 }
        r5 = -1;
        if (r4 == r5) goto L_0x006d;
    L_0x0046:
        r6 = r7;
    L_0x0047:
        r4 = r9.proto;	 Catch:{ all -> 0x0071 }
        if (r4 != 0) goto L_0x0053;
    L_0x004b:
        r4 = r9.tech;	 Catch:{ all -> 0x0071 }
        if (r4 != 0) goto L_0x0053;
    L_0x004f:
        r4 = r9.services;	 Catch:{ all -> 0x0071 }
        if (r4 == 0) goto L_0x006f;
    L_0x0053:
        r2 = r9.userId;	 Catch:{ all -> 0x0071 }
        monitor-exit(r11);	 Catch:{ all -> 0x0071 }
        if (r8 == 0) goto L_0x005b;
    L_0x0058:
        r11.setReaderMode(r1, r0, r3);
    L_0x005b:
        if (r6 == 0) goto L_0x0062;
    L_0x005d:
        r0 = 255; // 0xff float:3.57E-43 double:1.26E-321;
        r11.setListenMode(r1, r0);
    L_0x0062:
        if (r7 == 0) goto L_0x0036;
    L_0x0064:
        r0 = r11;
        r4 = r3;
        r5 = r3;
        r0.changeRoutingTable(r1, r2, r3, r4, r5);
        goto L_0x0036;
    L_0x006b:
        r8 = r0;
        goto L_0x0041;
    L_0x006d:
        r6 = r0;
        goto L_0x0047;
    L_0x006f:
        r7 = r0;
        goto L_0x0053;
    L_0x0071:
        r0 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0071 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.nfc.NfcActivityManager.onActivityPaused(android.app.Activity):void");
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityDestroyed(Activity activity) {
        synchronized (this) {
            NfcActivityState state = findActivityState(activity);
            if (DBG.booleanValue()) {
                Log.d(TAG, "onDestroy() for " + activity + " " + state);
            }
            if (state != null) {
                destroyActivityState(activity);
            }
        }
    }
}
