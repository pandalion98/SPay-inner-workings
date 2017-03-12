package android.content;

import android.accounts.Account;
import android.content.ISyncAdapter.Stub;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.os.Trace;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractThreadedSyncAdapter {
    @Deprecated
    public static final int LOG_SYNC_DETAILS = 2743;
    private boolean mAllowParallelSyncs;
    private final boolean mAutoInitialize;
    private final Context mContext;
    private final ISyncAdapterImpl mISyncAdapterImpl;
    private final AtomicInteger mNumSyncStarts;
    private final Object mSyncThreadLock;
    private final HashMap<Account, SyncThread> mSyncThreads;

    private class ISyncAdapterImpl extends Stub {
        private ISyncAdapterImpl() {
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void startSync(android.content.ISyncContext r12, java.lang.String r13, android.accounts.Account r14, android.os.Bundle r15) {
            /*
            r11 = this;
            r3 = new android.content.SyncContext;
            r3.<init>(r12);
            r1 = android.content.AbstractThreadedSyncAdapter.this;
            r9 = r1.toSyncKey(r14);
            r1 = android.content.AbstractThreadedSyncAdapter.this;
            r10 = r1.mSyncThreadLock;
            monitor-enter(r10);
            r1 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x0050 }
            r1 = r1.mSyncThreads;	 Catch:{ all -> 0x0050 }
            r1 = r1.containsKey(r9);	 Catch:{ all -> 0x0050 }
            if (r1 != 0) goto L_0x0091;
        L_0x001e:
            r1 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x0050 }
            r1 = r1.mAutoInitialize;	 Catch:{ all -> 0x0050 }
            if (r1 == 0) goto L_0x0053;
        L_0x0026:
            if (r15 == 0) goto L_0x0053;
        L_0x0028:
            r1 = "initialize";
            r2 = 0;
            r1 = r15.getBoolean(r1, r2);	 Catch:{ all -> 0x0050 }
            if (r1 == 0) goto L_0x0053;
        L_0x0032:
            r1 = android.content.ContentResolver.getIsSyncable(r14, r13);	 Catch:{ all -> 0x0046 }
            if (r1 >= 0) goto L_0x003c;
        L_0x0038:
            r1 = 1;
            android.content.ContentResolver.setIsSyncable(r14, r13, r1);	 Catch:{ all -> 0x0046 }
        L_0x003c:
            r1 = new android.content.SyncResult;	 Catch:{ all -> 0x0050 }
            r1.<init>();	 Catch:{ all -> 0x0050 }
            r3.onFinished(r1);	 Catch:{ all -> 0x0050 }
            monitor-exit(r10);	 Catch:{ all -> 0x0050 }
        L_0x0045:
            return;
        L_0x0046:
            r1 = move-exception;
            r2 = new android.content.SyncResult;	 Catch:{ all -> 0x0050 }
            r2.<init>();	 Catch:{ all -> 0x0050 }
            r3.onFinished(r2);	 Catch:{ all -> 0x0050 }
            throw r1;	 Catch:{ all -> 0x0050 }
        L_0x0050:
            r1 = move-exception;
            monitor-exit(r10);	 Catch:{ all -> 0x0050 }
            throw r1;
        L_0x0053:
            r0 = new android.content.AbstractThreadedSyncAdapter$SyncThread;	 Catch:{ all -> 0x0050 }
            r1 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x0050 }
            r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0050 }
            r2.<init>();	 Catch:{ all -> 0x0050 }
            r4 = "SyncAdapterThread-";
            r2 = r2.append(r4);	 Catch:{ all -> 0x0050 }
            r4 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x0050 }
            r4 = r4.mNumSyncStarts;	 Catch:{ all -> 0x0050 }
            r4 = r4.incrementAndGet();	 Catch:{ all -> 0x0050 }
            r2 = r2.append(r4);	 Catch:{ all -> 0x0050 }
            r2 = r2.toString();	 Catch:{ all -> 0x0050 }
            r7 = 0;
            r4 = r13;
            r5 = r14;
            r6 = r15;
            r0.<init>(r2, r3, r4, r5, r6);	 Catch:{ all -> 0x0050 }
            r1 = android.content.AbstractThreadedSyncAdapter.this;	 Catch:{ all -> 0x0050 }
            r1 = r1.mSyncThreads;	 Catch:{ all -> 0x0050 }
            r1.put(r9, r0);	 Catch:{ all -> 0x0050 }
            r0.start();	 Catch:{ all -> 0x0050 }
            r8 = 0;
        L_0x0088:
            monitor-exit(r10);	 Catch:{ all -> 0x0050 }
            if (r8 == 0) goto L_0x0045;
        L_0x008b:
            r1 = android.content.SyncResult.ALREADY_IN_PROGRESS;
            r3.onFinished(r1);
            goto L_0x0045;
        L_0x0091:
            r8 = 1;
            goto L_0x0088;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.content.AbstractThreadedSyncAdapter.ISyncAdapterImpl.startSync(android.content.ISyncContext, java.lang.String, android.accounts.Account, android.os.Bundle):void");
        }

        public void cancelSync(ISyncContext syncContext) {
            SyncThread info = null;
            synchronized (AbstractThreadedSyncAdapter.this.mSyncThreadLock) {
                for (SyncThread current : AbstractThreadedSyncAdapter.this.mSyncThreads.values()) {
                    if (current.mSyncContext.getSyncContextBinder() == syncContext.asBinder()) {
                        info = current;
                        break;
                    }
                }
            }
            if (info == null) {
                return;
            }
            if (AbstractThreadedSyncAdapter.this.mAllowParallelSyncs) {
                AbstractThreadedSyncAdapter.this.onSyncCanceled(info);
            } else {
                AbstractThreadedSyncAdapter.this.onSyncCanceled();
            }
        }

        public void initialize(Account account, String authority) throws RemoteException {
            Bundle extras = new Bundle();
            extras.putBoolean(ContentResolver.SYNC_EXTRAS_INITIALIZE, true);
            startSync(null, authority, account, extras);
        }
    }

    private class SyncThread extends Thread {
        private final Account mAccount;
        private final String mAuthority;
        private final Bundle mExtras;
        private final SyncContext mSyncContext;
        private final Account mThreadsKey;

        private SyncThread(String name, SyncContext syncContext, String authority, Account account, Bundle extras) {
            super(name);
            this.mSyncContext = syncContext;
            this.mAuthority = authority;
            this.mAccount = account;
            this.mExtras = extras;
            this.mThreadsKey = AbstractThreadedSyncAdapter.this.toSyncKey(account);
        }

        public void run() {
            Process.setThreadPriority(10);
            Trace.traceBegin(128, this.mAuthority);
            SyncResult syncResult = new SyncResult();
            ContentProviderClient provider = null;
            try {
                if (isCanceled()) {
                    Trace.traceEnd(128);
                    if (provider != null) {
                        provider.release();
                    }
                    if (!isCanceled()) {
                        this.mSyncContext.onFinished(syncResult);
                    }
                    synchronized (AbstractThreadedSyncAdapter.this.mSyncThreadLock) {
                        AbstractThreadedSyncAdapter.this.mSyncThreads.remove(this.mThreadsKey);
                    }
                    return;
                }
                provider = AbstractThreadedSyncAdapter.this.mContext.getContentResolver().acquireContentProviderClient(this.mAuthority);
                if (provider != null) {
                    AbstractThreadedSyncAdapter.this.onPerformSync(this.mAccount, this.mExtras, this.mAuthority, provider, syncResult);
                } else {
                    syncResult.databaseError = true;
                }
                Trace.traceEnd(128);
                if (provider != null) {
                    provider.release();
                }
                if (!isCanceled()) {
                    this.mSyncContext.onFinished(syncResult);
                }
                synchronized (AbstractThreadedSyncAdapter.this.mSyncThreadLock) {
                    AbstractThreadedSyncAdapter.this.mSyncThreads.remove(this.mThreadsKey);
                }
            } catch (SecurityException e) {
                AbstractThreadedSyncAdapter.this.onSecurityException(this.mAccount, this.mExtras, this.mAuthority, syncResult);
                syncResult.databaseError = true;
                Trace.traceEnd(128);
                if (provider != null) {
                    provider.release();
                }
                if (!isCanceled()) {
                    this.mSyncContext.onFinished(syncResult);
                }
                synchronized (AbstractThreadedSyncAdapter.this.mSyncThreadLock) {
                    AbstractThreadedSyncAdapter.this.mSyncThreads.remove(this.mThreadsKey);
                }
            } catch (Throwable th) {
                Trace.traceEnd(128);
                if (provider != null) {
                    provider.release();
                }
                if (!isCanceled()) {
                    this.mSyncContext.onFinished(syncResult);
                }
                synchronized (AbstractThreadedSyncAdapter.this.mSyncThreadLock) {
                    AbstractThreadedSyncAdapter.this.mSyncThreads.remove(this.mThreadsKey);
                }
            }
        }

        private boolean isCanceled() {
            return Thread.currentThread().isInterrupted();
        }
    }

    public abstract void onPerformSync(Account account, Bundle bundle, String str, ContentProviderClient contentProviderClient, SyncResult syncResult);

    public AbstractThreadedSyncAdapter(Context context, boolean autoInitialize) {
        this(context, autoInitialize, false);
    }

    public AbstractThreadedSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        this.mSyncThreads = new HashMap();
        this.mSyncThreadLock = new Object();
        this.mContext = context;
        this.mISyncAdapterImpl = new ISyncAdapterImpl();
        this.mNumSyncStarts = new AtomicInteger(0);
        this.mAutoInitialize = autoInitialize;
        this.mAllowParallelSyncs = allowParallelSyncs;
    }

    public Context getContext() {
        return this.mContext;
    }

    private Account toSyncKey(Account account) {
        return this.mAllowParallelSyncs ? account : null;
    }

    public final IBinder getSyncAdapterBinder() {
        return this.mISyncAdapterImpl.asBinder();
    }

    public void onSecurityException(Account account, Bundle extras, String authority, SyncResult syncResult) {
    }

    public void onSyncCanceled() {
        synchronized (this.mSyncThreadLock) {
            SyncThread syncThread = (SyncThread) this.mSyncThreads.get(null);
        }
        if (syncThread != null) {
            syncThread.interrupt();
        }
    }

    public void onSyncCanceled(Thread thread) {
        thread.interrupt();
    }
}
