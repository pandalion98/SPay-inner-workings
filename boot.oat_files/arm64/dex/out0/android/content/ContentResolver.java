package android.content;

import android.accounts.Account;
import android.app.ActivityManagerNative;
import android.app.ActivityThread;
import android.app.backup.FullBackup;
import android.content.ISyncStatusObserver.Stub;
import android.content.SyncRequest.Builder;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.database.ContentObserver;
import android.database.CrossProcessCursorWrapper;
import android.database.Cursor;
import android.database.IContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.DeadObjectException;
import android.os.ICancellationSignal;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.ContactsContract.Directory;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import dalvik.system.CloseGuard;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class ContentResolver {
    public static final Intent ACTION_SYNC_CONN_STATUS_CHANGED = new Intent("com.android.sync.SYNC_CONN_STATUS_CHANGED");
    public static final String ANY_CURSOR_ITEM_TYPE = "vnd.android.cursor.item/*";
    public static final String CONTENT_SERVICE_NAME = "content";
    public static final String CURSOR_DIR_BASE_TYPE = "vnd.android.cursor.dir";
    public static final String CURSOR_ITEM_BASE_TYPE = "vnd.android.cursor.item";
    private static final boolean ENABLE_CONTENT_SAMPLE = false;
    public static final String EXTRA_SIZE = "android.content.extra.SIZE";
    public static final String SCHEME_ANDROID_RESOURCE = "android.resource";
    public static final String SCHEME_CONTENT = "content";
    public static final String SCHEME_FILE = "file";
    private static final int SLOW_THRESHOLD_MILLIS = 500;
    public static final int SYNC_ERROR_AUTHENTICATION = 2;
    public static final int SYNC_ERROR_CONFLICT = 5;
    public static final int SYNC_ERROR_INTERNAL = 8;
    public static final int SYNC_ERROR_IO = 3;
    private static final String[] SYNC_ERROR_NAMES = new String[]{"already-in-progress", "authentication-error", "io-error", "parse-error", "conflict", "too-many-deletions", "too-many-retries", "internal-error"};
    public static final int SYNC_ERROR_PARSE = 4;
    public static final int SYNC_ERROR_SYNC_ALREADY_IN_PROGRESS = 1;
    public static final int SYNC_ERROR_TOO_MANY_DELETIONS = 6;
    public static final int SYNC_ERROR_TOO_MANY_RETRIES = 7;
    @Deprecated
    public static final String SYNC_EXTRAS_ACCOUNT = "account";
    public static final String SYNC_EXTRAS_DISALLOW_METERED = "allow_metered";
    public static final String SYNC_EXTRAS_DISCARD_LOCAL_DELETIONS = "discard_deletions";
    public static final String SYNC_EXTRAS_DO_NOT_RETRY = "do_not_retry";
    public static final String SYNC_EXTRAS_EXPECTED_DOWNLOAD = "expected_download";
    public static final String SYNC_EXTRAS_EXPECTED_UPLOAD = "expected_upload";
    public static final String SYNC_EXTRAS_EXPEDITED = "expedited";
    @Deprecated
    public static final String SYNC_EXTRAS_FORCE = "force";
    public static final String SYNC_EXTRAS_IGNORE_BACKOFF = "ignore_backoff";
    public static final String SYNC_EXTRAS_IGNORE_SETTINGS = "ignore_settings";
    public static final String SYNC_EXTRAS_INITIALIZE = "initialize";
    public static final String SYNC_EXTRAS_MANUAL = "force";
    public static final String SYNC_EXTRAS_OVERRIDE_TOO_MANY_DELETIONS = "deletions_override";
    public static final String SYNC_EXTRAS_PRIORITY = "sync_priority";
    public static final String SYNC_EXTRAS_UPLOAD = "upload";
    public static final int SYNC_OBSERVER_TYPE_ACTIVE = 4;
    public static final int SYNC_OBSERVER_TYPE_ALL = Integer.MAX_VALUE;
    public static final int SYNC_OBSERVER_TYPE_PENDING = 2;
    public static final int SYNC_OBSERVER_TYPE_SETTINGS = 1;
    public static final int SYNC_OBSERVER_TYPE_STATUS = 8;
    private static final String TAG = "ContentResolver";
    private static IContentService sContentService;
    private final Context mContext;
    final String mPackageName;
    private final Random mRandom = new Random();

    private final class CursorWrapperInner extends CrossProcessCursorWrapper {
        public static final String TAG = "CursorWrapperInner";
        private final CloseGuard mCloseGuard = CloseGuard.get();
        private final IContentProvider mContentProvider;
        private boolean mProviderReleased;

        CursorWrapperInner(Cursor cursor, IContentProvider icp) {
            super(cursor);
            this.mContentProvider = icp;
            this.mCloseGuard.open("close");
        }

        public void close() {
            super.close();
            ContentResolver.this.releaseProvider(this.mContentProvider);
            this.mProviderReleased = true;
            if (this.mCloseGuard != null) {
                this.mCloseGuard.close();
            }
        }

        protected void finalize() throws Throwable {
            try {
                if (this.mCloseGuard != null) {
                    this.mCloseGuard.warnIfOpen();
                }
                if (!(this.mProviderReleased || this.mContentProvider == null)) {
                    Log.w(TAG, "Cursor finalized without prior close()");
                    ContentResolver.this.releaseProvider(this.mContentProvider);
                }
                super.finalize();
            } catch (Throwable th) {
                super.finalize();
            }
        }
    }

    public class OpenResourceIdResult {
        public int id;
        public Resources r;
    }

    private final class ParcelFileDescriptorInner extends ParcelFileDescriptor {
        private final IContentProvider mContentProvider;
        private boolean mProviderReleased;

        ParcelFileDescriptorInner(ParcelFileDescriptor pfd, IContentProvider icp) {
            super(pfd);
            this.mContentProvider = icp;
        }

        public void releaseResources() {
            if (!this.mProviderReleased) {
                ContentResolver.this.releaseProvider(this.mContentProvider);
                this.mProviderReleased = true;
            }
        }
    }

    protected abstract IContentProvider acquireProvider(Context context, String str);

    protected abstract IContentProvider acquireUnstableProvider(Context context, String str);

    public final android.database.Cursor query(android.net.Uri r31, java.lang.String[] r32, java.lang.String r33, java.lang.String[] r34, java.lang.String r35, android.os.CancellationSignal r36) {
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
        r30 = this;
        r5 = "uri";
        r0 = r31;
        com.android.internal.util.Preconditions.checkNotNull(r0, r5);
        r4 = r30.acquireUnstableProvider(r31);
        if (r4 != 0) goto L_0x0011;
    L_0x000e:
        r27 = 0;
    L_0x0010:
        return r27;
    L_0x0011:
        r12 = 0;
        r26 = 0;
        r28 = android.os.SystemClock.uptimeMillis();	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r11 = 0;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        if (r36 == 0) goto L_0x0027;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
    L_0x001b:
        r36.throwIfCanceled();	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r11 = r4.createCancellationSignal();	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r0 = r36;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r0.setRemote(r11);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
    L_0x0027:
        r0 = r30;	 Catch:{ DeadObjectException -> 0x0059 }
        r5 = r0.mPackageName;	 Catch:{ DeadObjectException -> 0x0059 }
        r6 = r31;	 Catch:{ DeadObjectException -> 0x0059 }
        r7 = r32;	 Catch:{ DeadObjectException -> 0x0059 }
        r8 = r33;	 Catch:{ DeadObjectException -> 0x0059 }
        r9 = r34;	 Catch:{ DeadObjectException -> 0x0059 }
        r10 = r35;	 Catch:{ DeadObjectException -> 0x0059 }
        r26 = r4.query(r5, r6, r7, r8, r9, r10, r11);	 Catch:{ DeadObjectException -> 0x0059 }
    L_0x0039:
        if (r26 != 0) goto L_0x0098;
    L_0x003b:
        r27 = 0;
        if (r26 == 0) goto L_0x0042;
    L_0x003f:
        r26.close();
    L_0x0042:
        if (r36 == 0) goto L_0x004a;
    L_0x0044:
        r5 = 0;
        r0 = r36;
        r0.setRemote(r5);
    L_0x004a:
        if (r4 == 0) goto L_0x0051;
    L_0x004c:
        r0 = r30;
        r0.releaseUnstableProvider(r4);
    L_0x0051:
        if (r12 == 0) goto L_0x0010;
    L_0x0053:
        r0 = r30;
        r0.releaseProvider(r12);
        goto L_0x0010;
    L_0x0059:
        r24 = move-exception;
        r0 = r30;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r0.unstableProviderDied(r4);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r12 = r30.acquireProvider(r31);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        if (r12 != 0) goto L_0x0083;
    L_0x0065:
        r27 = 0;
        if (r26 == 0) goto L_0x006c;
    L_0x0069:
        r26.close();
    L_0x006c:
        if (r36 == 0) goto L_0x0074;
    L_0x006e:
        r5 = 0;
        r0 = r36;
        r0.setRemote(r5);
    L_0x0074:
        if (r4 == 0) goto L_0x007b;
    L_0x0076:
        r0 = r30;
        r0.releaseUnstableProvider(r4);
    L_0x007b:
        if (r12 == 0) goto L_0x0010;
    L_0x007d:
        r0 = r30;
        r0.releaseProvider(r12);
        goto L_0x0010;
    L_0x0083:
        r0 = r30;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r13 = r0.mPackageName;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r14 = r31;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r15 = r32;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r16 = r33;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r17 = r34;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r18 = r35;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r19 = r11;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r26 = r12.query(r13, r14, r15, r16, r17, r18, r19);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        goto L_0x0039;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
    L_0x0098:
        r26.getCount();	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r6 = android.os.SystemClock.uptimeMillis();	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r14 = r6 - r28;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r13 = r30;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r16 = r31;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r17 = r32;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r18 = r33;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r19 = r35;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r13.maybeLogQueryToEventLog(r14, r16, r17, r18, r19);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r5 = com.sec.android.app.CscFeature.getInstance();	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r6 = "CscFeature_Common_EnablePrivacyDataGuard";	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r5 = r5.getEnableStatus(r6);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        if (r5 == 0) goto L_0x016e;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
    L_0x00ba:
        r0 = r30;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r5 = r0.mContext;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r0 = r31;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r5 = com.kddi.android.internal.pdg.PdgContactsAccessChecker.checkPrivacy(r5, r0);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        if (r5 != 0) goto L_0x016e;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
    L_0x00c6:
        r5 = 0;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r0 = r26;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r25 = r0.getColumnName(r5);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r5 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r5.<init>();	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r0 = r25;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r5 = r5.append(r0);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r6 = " NOT NULL AND ";	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r5 = r5.append(r6);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r0 = r25;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r5 = r5.append(r0);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r6 = "='fill_long_long_character_here_abcdefghijklmnopqrstuvwxyz'";	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r5 = r5.append(r6);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r33 = r5.toString();	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r34 = 0;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        if (r26 == 0) goto L_0x00f7;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
    L_0x00f2:
        r26.close();	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r26 = 0;
    L_0x00f7:
        r0 = r30;	 Catch:{ DeadObjectException -> 0x012a }
        r5 = r0.mPackageName;	 Catch:{ DeadObjectException -> 0x012a }
        r6 = r31;	 Catch:{ DeadObjectException -> 0x012a }
        r7 = r32;	 Catch:{ DeadObjectException -> 0x012a }
        r8 = r33;	 Catch:{ DeadObjectException -> 0x012a }
        r9 = r34;	 Catch:{ DeadObjectException -> 0x012a }
        r10 = r35;	 Catch:{ DeadObjectException -> 0x012a }
        r26 = r4.query(r5, r6, r7, r8, r9, r10, r11);	 Catch:{ DeadObjectException -> 0x012a }
    L_0x0109:
        if (r26 != 0) goto L_0x016e;
    L_0x010b:
        r27 = 0;
        if (r26 == 0) goto L_0x0112;
    L_0x010f:
        r26.close();
    L_0x0112:
        if (r36 == 0) goto L_0x011a;
    L_0x0114:
        r5 = 0;
        r0 = r36;
        r0.setRemote(r5);
    L_0x011a:
        if (r4 == 0) goto L_0x0121;
    L_0x011c:
        r0 = r30;
        r0.releaseUnstableProvider(r4);
    L_0x0121:
        if (r12 == 0) goto L_0x0010;
    L_0x0123:
        r0 = r30;
        r0.releaseProvider(r12);
        goto L_0x0010;
    L_0x012a:
        r24 = move-exception;
        r0 = r30;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r0.unstableProviderDied(r4);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r12 = r30.acquireProvider(r31);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        if (r12 != 0) goto L_0x0155;
    L_0x0136:
        r27 = 0;
        if (r26 == 0) goto L_0x013d;
    L_0x013a:
        r26.close();
    L_0x013d:
        if (r36 == 0) goto L_0x0145;
    L_0x013f:
        r5 = 0;
        r0 = r36;
        r0.setRemote(r5);
    L_0x0145:
        if (r4 == 0) goto L_0x014c;
    L_0x0147:
        r0 = r30;
        r0.releaseUnstableProvider(r4);
    L_0x014c:
        if (r12 == 0) goto L_0x0010;
    L_0x014e:
        r0 = r30;
        r0.releaseProvider(r12);
        goto L_0x0010;
    L_0x0155:
        r0 = r30;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r0 = r0.mPackageName;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r17 = r0;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r16 = r12;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r18 = r31;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r19 = r32;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r20 = r33;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r21 = r34;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r22 = r35;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r23 = r11;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r26 = r16.query(r17, r18, r19, r20, r21, r22, r23);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        goto L_0x0109;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
    L_0x016e:
        r27 = new android.content.ContentResolver$CursorWrapperInner;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        if (r12 == 0) goto L_0x019c;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
    L_0x0172:
        r5 = r12;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
    L_0x0173:
        r0 = r27;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r1 = r30;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r2 = r26;	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r0.<init>(r2, r5);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        r12 = 0;
        r26 = 0;
        if (r26 == 0) goto L_0x0184;
    L_0x0181:
        r26.close();
    L_0x0184:
        if (r36 == 0) goto L_0x018c;
    L_0x0186:
        r5 = 0;
        r0 = r36;
        r0.setRemote(r5);
    L_0x018c:
        if (r4 == 0) goto L_0x0193;
    L_0x018e:
        r0 = r30;
        r0.releaseUnstableProvider(r4);
    L_0x0193:
        if (r12 == 0) goto L_0x0010;
    L_0x0195:
        r0 = r30;
        r0.releaseProvider(r12);
        goto L_0x0010;
    L_0x019c:
        r5 = r30.acquireProvider(r31);	 Catch:{ RemoteException -> 0x01a1, all -> 0x01c1 }
        goto L_0x0173;
    L_0x01a1:
        r24 = move-exception;
        r27 = 0;
        if (r26 == 0) goto L_0x01a9;
    L_0x01a6:
        r26.close();
    L_0x01a9:
        if (r36 == 0) goto L_0x01b1;
    L_0x01ab:
        r5 = 0;
        r0 = r36;
        r0.setRemote(r5);
    L_0x01b1:
        if (r4 == 0) goto L_0x01b8;
    L_0x01b3:
        r0 = r30;
        r0.releaseUnstableProvider(r4);
    L_0x01b8:
        if (r12 == 0) goto L_0x0010;
    L_0x01ba:
        r0 = r30;
        r0.releaseProvider(r12);
        goto L_0x0010;
    L_0x01c1:
        r5 = move-exception;
        if (r26 == 0) goto L_0x01c7;
    L_0x01c4:
        r26.close();
    L_0x01c7:
        if (r36 == 0) goto L_0x01cf;
    L_0x01c9:
        r6 = 0;
        r0 = r36;
        r0.setRemote(r6);
    L_0x01cf:
        if (r4 == 0) goto L_0x01d6;
    L_0x01d1:
        r0 = r30;
        r0.releaseUnstableProvider(r4);
    L_0x01d6:
        if (r12 == 0) goto L_0x01dd;
    L_0x01d8:
        r0 = r30;
        r0.releaseProvider(r12);
    L_0x01dd:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.ContentResolver.query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String, android.os.CancellationSignal):android.database.Cursor");
    }

    public abstract boolean releaseProvider(IContentProvider iContentProvider);

    public abstract boolean releaseUnstableProvider(IContentProvider iContentProvider);

    public abstract void unstableProviderDied(IContentProvider iContentProvider);

    public static String syncErrorToString(int error) {
        if (error < 1 || error > SYNC_ERROR_NAMES.length) {
            return String.valueOf(error);
        }
        return SYNC_ERROR_NAMES[error - 1];
    }

    public static int syncErrorStringToInt(String error) {
        int n = SYNC_ERROR_NAMES.length;
        for (int i = 0; i < n; i++) {
            if (SYNC_ERROR_NAMES[i].equals(error)) {
                return i + 1;
            }
        }
        if (error != null) {
            try {
                return Integer.parseInt(error);
            } catch (NumberFormatException e) {
                Log.d(TAG, "error parsing sync error: " + error);
            }
        }
        return 0;
    }

    public ContentResolver(Context context) {
        if (context == null) {
            context = ActivityThread.currentApplication();
        }
        this.mContext = context;
        this.mPackageName = this.mContext.getOpPackageName();
    }

    protected IContentProvider acquireExistingProvider(Context c, String name) {
        return acquireProvider(c, name);
    }

    public void appNotRespondingViaProvider(IContentProvider icp) {
        throw new UnsupportedOperationException("appNotRespondingViaProvider");
    }

    public final String getType(Uri url) {
        String str = null;
        Preconditions.checkNotNull(url, "url");
        IContentProvider provider = acquireExistingProvider(url);
        if (provider != null) {
            try {
                str = provider.getType(url);
            } catch (RemoteException e) {
            } catch (Exception e2) {
                Log.w(TAG, "Failed to get type for: " + url + " (" + e2.getMessage() + ")");
            } finally {
                releaseProvider(provider);
            }
        } else if ("content".equals(url.getScheme())) {
            try {
                str = ActivityManagerNative.getDefault().getProviderMimeType(ContentProvider.getUriWithoutUserId(url), resolveUserId(url));
            } catch (RemoteException e3) {
            } catch (Exception e22) {
                Log.w(TAG, "Failed to get type for: " + url + " (" + e22.getMessage() + ")");
            }
        }
        return str;
    }

    public String[] getStreamTypes(Uri url, String mimeTypeFilter) {
        String[] strArr = null;
        Preconditions.checkNotNull(url, "url");
        Preconditions.checkNotNull(mimeTypeFilter, "mimeTypeFilter");
        IContentProvider provider = acquireProvider(url);
        if (provider != null) {
            try {
                strArr = provider.getStreamTypes(url, mimeTypeFilter);
            } catch (RemoteException e) {
            } finally {
                releaseProvider(provider);
            }
        }
        return strArr;
    }

    public final Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return query(uri, projection, selection, selectionArgs, sortOrder, null);
    }

    public final Uri canonicalize(Uri url) {
        Uri uri = null;
        Preconditions.checkNotNull(url, "url");
        IContentProvider provider = acquireProvider(url);
        if (provider != null) {
            try {
                uri = provider.canonicalize(this.mPackageName, url);
            } catch (RemoteException e) {
            } finally {
                releaseProvider(provider);
            }
        }
        return uri;
    }

    public final Uri uncanonicalize(Uri url) {
        Uri uri = null;
        Preconditions.checkNotNull(url, "url");
        IContentProvider provider = acquireProvider(url);
        if (provider != null) {
            try {
                uri = provider.uncanonicalize(this.mPackageName, url);
            } catch (RemoteException e) {
            } finally {
                releaseProvider(provider);
            }
        }
        return uri;
    }

    public final InputStream openInputStream(Uri uri) throws FileNotFoundException {
        CancellationSignal cancellationSignal = null;
        Preconditions.checkNotNull(uri, "uri");
        String scheme = uri.getScheme();
        if (SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            OpenResourceIdResult r = getResourceId(uri);
            try {
                return r.r.openRawResource(r.id);
            } catch (NotFoundException e) {
                throw new FileNotFoundException("Resource does not exist: " + uri);
            }
        } else if (SCHEME_FILE.equals(scheme)) {
            return new FileInputStream(uri.getPath());
        } else {
            AssetFileDescriptor fd = openAssetFileDescriptor(uri, FullBackup.ROOT_TREE_TOKEN, null);
            if (fd != null) {
                try {
                    cancellationSignal = fd.createInputStream();
                } catch (IOException e2) {
                    throw new FileNotFoundException("Unable to create stream");
                }
            }
            return cancellationSignal;
        }
    }

    public final OutputStream openOutputStream(Uri uri) throws FileNotFoundException {
        return openOutputStream(uri, "w");
    }

    public final OutputStream openOutputStream(Uri uri, String mode) throws FileNotFoundException {
        OutputStream outputStream = null;
        AssetFileDescriptor fd = openAssetFileDescriptor(uri, mode, null);
        if (fd != null) {
            try {
                outputStream = fd.createOutputStream();
            } catch (IOException e) {
                throw new FileNotFoundException("Unable to create stream");
            }
        }
        return outputStream;
    }

    public final ParcelFileDescriptor openFileDescriptor(Uri uri, String mode) throws FileNotFoundException {
        return openFileDescriptor(uri, mode, null);
    }

    public final ParcelFileDescriptor openFileDescriptor(Uri uri, String mode, CancellationSignal cancellationSignal) throws FileNotFoundException {
        AssetFileDescriptor afd = openAssetFileDescriptor(uri, mode, cancellationSignal);
        if (afd == null) {
            return null;
        }
        if (afd.getDeclaredLength() < 0) {
            return afd.getParcelFileDescriptor();
        }
        try {
            afd.close();
        } catch (IOException e) {
        }
        throw new FileNotFoundException("Not a whole file");
    }

    public final AssetFileDescriptor openAssetFileDescriptor(Uri uri, String mode) throws FileNotFoundException {
        return openAssetFileDescriptor(uri, mode, null);
    }

    public final AssetFileDescriptor openAssetFileDescriptor(Uri uri, String mode, CancellationSignal cancellationSignal) throws FileNotFoundException {
        AssetFileDescriptor fd;
        Preconditions.checkNotNull(uri, "uri");
        Preconditions.checkNotNull(mode, "mode");
        String scheme = uri.getScheme();
        if (SCHEME_ANDROID_RESOURCE.equals(scheme)) {
            if (FullBackup.ROOT_TREE_TOKEN.equals(mode)) {
                OpenResourceIdResult r = getResourceId(uri);
                try {
                    return r.r.openRawResourceFd(r.id);
                } catch (NotFoundException e) {
                    throw new FileNotFoundException("Resource does not exist: " + uri);
                }
            }
            throw new FileNotFoundException("Can't write resources: " + uri);
        } else if (SCHEME_FILE.equals(scheme)) {
            return new AssetFileDescriptor(ParcelFileDescriptor.open(new File(uri.getPath()), ParcelFileDescriptor.parseMode(mode)), 0, -1);
        } else {
            if (FullBackup.ROOT_TREE_TOKEN.equals(mode)) {
                return openTypedAssetFileDescriptor(uri, "*/*", null, cancellationSignal);
            }
            IContentProvider unstableProvider = acquireUnstableProvider(uri);
            if (unstableProvider == null) {
                throw new FileNotFoundException("No content provider: " + uri);
            }
            IContentProvider stableProvider = null;
            ICancellationSignal remoteCancellationSignal = null;
            if (cancellationSignal != null) {
                try {
                    cancellationSignal.throwIfCanceled();
                    remoteCancellationSignal = unstableProvider.createCancellationSignal();
                    cancellationSignal.setRemote(remoteCancellationSignal);
                } catch (DeadObjectException e2) {
                    unstableProviderDied(unstableProvider);
                    stableProvider = acquireProvider(uri);
                    if (stableProvider == null) {
                        throw new FileNotFoundException("No content provider: " + uri);
                    }
                    fd = stableProvider.openAssetFile(this.mPackageName, uri, mode, remoteCancellationSignal);
                    if (fd == null) {
                        if (cancellationSignal != null) {
                            cancellationSignal.setRemote(null);
                        }
                        if (stableProvider != null) {
                            releaseProvider(stableProvider);
                        }
                        if (unstableProvider == null) {
                            return null;
                        }
                        releaseUnstableProvider(unstableProvider);
                        return null;
                    }
                } catch (RemoteException e3) {
                    try {
                        throw new FileNotFoundException("Failed opening content provider: " + uri);
                    } catch (Throwable th) {
                        if (cancellationSignal != null) {
                            cancellationSignal.setRemote(null);
                        }
                        if (stableProvider != null) {
                            releaseProvider(stableProvider);
                        }
                        if (unstableProvider != null) {
                            releaseUnstableProvider(unstableProvider);
                        }
                    }
                } catch (FileNotFoundException e4) {
                    throw e4;
                }
            }
            fd = unstableProvider.openAssetFile(this.mPackageName, uri, mode, remoteCancellationSignal);
            if (fd == null) {
                if (cancellationSignal != null) {
                    cancellationSignal.setRemote(null);
                }
                if (null != null) {
                    releaseProvider(null);
                }
                if (unstableProvider == null) {
                    return null;
                }
                releaseUnstableProvider(unstableProvider);
                return null;
            }
            if (stableProvider == null) {
                stableProvider = acquireProvider(uri);
            }
            AssetFileDescriptor assetFileDescriptor = new AssetFileDescriptor(new ParcelFileDescriptorInner(fd.getParcelFileDescriptor(), stableProvider), fd.getStartOffset(), fd.getDeclaredLength());
            if (cancellationSignal != null) {
                cancellationSignal.setRemote(null);
            }
            if (null != null) {
                releaseProvider(null);
            }
            if (unstableProvider == null) {
                return assetFileDescriptor;
            }
            releaseUnstableProvider(unstableProvider);
            return assetFileDescriptor;
        }
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri uri, String mimeType, Bundle opts) throws FileNotFoundException {
        return openTypedAssetFileDescriptor(uri, mimeType, opts, null);
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri uri, String mimeType, Bundle opts, CancellationSignal cancellationSignal) throws FileNotFoundException {
        Preconditions.checkNotNull(uri, "uri");
        Preconditions.checkNotNull(mimeType, "mimeType");
        IContentProvider unstableProvider = acquireUnstableProvider(uri);
        if (unstableProvider == null) {
            throw new FileNotFoundException("No content provider: " + uri);
        }
        AssetFileDescriptor fd;
        AssetFileDescriptor assetFileDescriptor;
        IContentProvider stableProvider = null;
        ICancellationSignal remoteCancellationSignal = null;
        if (cancellationSignal != null) {
            try {
                cancellationSignal.throwIfCanceled();
                remoteCancellationSignal = unstableProvider.createCancellationSignal();
                cancellationSignal.setRemote(remoteCancellationSignal);
            } catch (DeadObjectException e) {
                unstableProviderDied(unstableProvider);
                stableProvider = acquireProvider(uri);
                if (stableProvider == null) {
                    throw new FileNotFoundException("No content provider: " + uri);
                }
                fd = stableProvider.openTypedAssetFile(this.mPackageName, uri, mimeType, opts, remoteCancellationSignal);
                if (fd == null) {
                    assetFileDescriptor = null;
                    if (cancellationSignal != null) {
                        cancellationSignal.setRemote(null);
                    }
                    if (stableProvider != null) {
                        releaseProvider(stableProvider);
                    }
                    if (unstableProvider != null) {
                        releaseUnstableProvider(unstableProvider);
                    }
                }
            } catch (RemoteException e2) {
                try {
                    throw new FileNotFoundException("Failed opening content provider: " + uri);
                } catch (Throwable th) {
                    if (cancellationSignal != null) {
                        cancellationSignal.setRemote(null);
                    }
                    if (stableProvider != null) {
                        releaseProvider(stableProvider);
                    }
                    if (unstableProvider != null) {
                        releaseUnstableProvider(unstableProvider);
                    }
                }
            } catch (FileNotFoundException e3) {
                throw e3;
            }
        }
        fd = unstableProvider.openTypedAssetFile(this.mPackageName, uri, mimeType, opts, remoteCancellationSignal);
        if (fd == null) {
            assetFileDescriptor = null;
            if (cancellationSignal != null) {
                cancellationSignal.setRemote(null);
            }
            if (null != null) {
                releaseProvider(null);
            }
            if (unstableProvider != null) {
                releaseUnstableProvider(unstableProvider);
            }
            return assetFileDescriptor;
        }
        if (stableProvider == null) {
            stableProvider = acquireProvider(uri);
        }
        assetFileDescriptor = new AssetFileDescriptor(new ParcelFileDescriptorInner(fd.getParcelFileDescriptor(), stableProvider), fd.getStartOffset(), fd.getDeclaredLength());
        if (cancellationSignal != null) {
            cancellationSignal.setRemote(null);
        }
        if (null != null) {
            releaseProvider(null);
        }
        if (unstableProvider != null) {
            releaseUnstableProvider(unstableProvider);
        }
        return assetFileDescriptor;
    }

    public OpenResourceIdResult getResourceId(Uri uri) throws FileNotFoundException {
        String authority = uri.getAuthority();
        if (TextUtils.isEmpty(authority)) {
            throw new FileNotFoundException("No authority: " + uri);
        }
        try {
            Resources r = this.mContext.getPackageManager().getResourcesForApplication(authority);
            List<String> path = uri.getPathSegments();
            if (path == null) {
                throw new FileNotFoundException("No path: " + uri);
            }
            int id;
            int len = path.size();
            if (len == 1) {
                try {
                    id = Integer.parseInt((String) path.get(0));
                } catch (NumberFormatException e) {
                    throw new FileNotFoundException("Single path segment is not a resource ID: " + uri);
                }
            } else if (len == 2) {
                id = r.getIdentifier((String) path.get(1), (String) path.get(0), authority);
            } else {
                throw new FileNotFoundException("More than two path segments: " + uri);
            }
            if (id == 0) {
                throw new FileNotFoundException("No resource found for: " + uri);
            }
            OpenResourceIdResult res = new OpenResourceIdResult();
            res.r = r;
            res.id = id;
            return res;
        } catch (NameNotFoundException e2) {
            throw new FileNotFoundException("No package found for authority: " + uri);
        }
    }

    public final Uri insert(Uri url, ContentValues values) {
        Preconditions.checkNotNull(url, "url");
        IContentProvider provider = acquireProvider(url);
        if (provider == null) {
            throw new IllegalArgumentException("Unknown URL " + url);
        }
        try {
            long startTime = SystemClock.uptimeMillis();
            Uri createdRow = provider.insert(this.mPackageName, url, values);
            maybeLogUpdateToEventLog(SystemClock.uptimeMillis() - startTime, url, "insert", null);
            return createdRow;
        } catch (RemoteException e) {
            return null;
        } finally {
            releaseProvider(provider);
        }
    }

    public ContentProviderResult[] applyBatch(String authority, ArrayList<ContentProviderOperation> operations) throws RemoteException, OperationApplicationException {
        Preconditions.checkNotNull(authority, Directory.DIRECTORY_AUTHORITY);
        Preconditions.checkNotNull(operations, "operations");
        ContentProviderClient provider = acquireContentProviderClient(authority);
        if (provider == null) {
            throw new IllegalArgumentException("Unknown authority " + authority);
        }
        try {
            ContentProviderResult[] applyBatch = provider.applyBatch(operations);
            return applyBatch;
        } finally {
            provider.release();
        }
    }

    public final int bulkInsert(Uri url, ContentValues[] values) {
        Preconditions.checkNotNull(url, "url");
        Preconditions.checkNotNull(values, "values");
        IContentProvider provider = acquireProvider(url);
        if (provider == null) {
            throw new IllegalArgumentException("Unknown URL " + url);
        }
        int bulkInsert;
        try {
            long startTime = SystemClock.uptimeMillis();
            bulkInsert = provider.bulkInsert(this.mPackageName, url, values);
            maybeLogUpdateToEventLog(SystemClock.uptimeMillis() - startTime, url, "bulkinsert", null);
        } catch (RemoteException e) {
            bulkInsert = 0;
        } finally {
            releaseProvider(provider);
        }
        return bulkInsert;
    }

    public final int delete(Uri url, String where, String[] selectionArgs) {
        Preconditions.checkNotNull(url, "url");
        IContentProvider provider = acquireProvider(url);
        if (provider == null) {
            throw new IllegalArgumentException("Unknown URL " + url);
        }
        int delete;
        try {
            long startTime = SystemClock.uptimeMillis();
            delete = provider.delete(this.mPackageName, url, where, selectionArgs);
            maybeLogUpdateToEventLog(SystemClock.uptimeMillis() - startTime, url, "delete", where);
        } catch (RemoteException e) {
            delete = -1;
        } finally {
            releaseProvider(provider);
        }
        return delete;
    }

    public final int update(Uri uri, ContentValues values, String where, String[] selectionArgs) {
        Preconditions.checkNotNull(uri, "uri");
        IContentProvider provider = acquireProvider(uri);
        if (provider == null) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        int update;
        try {
            long startTime = SystemClock.uptimeMillis();
            update = provider.update(this.mPackageName, uri, values, where, selectionArgs);
            maybeLogUpdateToEventLog(SystemClock.uptimeMillis() - startTime, uri, "update", where);
        } catch (RemoteException e) {
            update = -1;
        } finally {
            releaseProvider(provider);
        }
        return update;
    }

    public final Bundle call(Uri uri, String method, String arg, Bundle extras) {
        Preconditions.checkNotNull(uri, "uri");
        Preconditions.checkNotNull(method, RemindersColumns.METHOD);
        IContentProvider provider = acquireProvider(uri);
        if (provider == null) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        Bundle call;
        try {
            call = provider.call(this.mPackageName, method, arg, extras);
        } catch (RemoteException e) {
            call = null;
        } finally {
            releaseProvider(provider);
        }
        return call;
    }

    public final IContentProvider acquireProvider(Uri uri) {
        if (!"content".equals(uri.getScheme())) {
            return null;
        }
        String auth = uri.getAuthority();
        if (auth != null) {
            return acquireProvider(this.mContext, auth);
        }
        return null;
    }

    public final IContentProvider acquireExistingProvider(Uri uri) {
        if (!"content".equals(uri.getScheme())) {
            return null;
        }
        String auth = uri.getAuthority();
        if (auth != null) {
            return acquireExistingProvider(this.mContext, auth);
        }
        return null;
    }

    public final IContentProvider acquireProvider(String name) {
        if (name == null) {
            return null;
        }
        return acquireProvider(this.mContext, name);
    }

    public final IContentProvider acquireUnstableProvider(Uri uri) {
        if ("content".equals(uri.getScheme()) && uri.getAuthority() != null) {
            return acquireUnstableProvider(this.mContext, uri.getAuthority());
        }
        return null;
    }

    public final IContentProvider acquireUnstableProvider(String name) {
        if (name == null) {
            return null;
        }
        return acquireUnstableProvider(this.mContext, name);
    }

    public final ContentProviderClient acquireContentProviderClient(Uri uri) {
        Preconditions.checkNotNull(uri, "uri");
        IContentProvider provider = acquireProvider(uri);
        if (provider != null) {
            return new ContentProviderClient(this, provider, true);
        }
        return null;
    }

    public final ContentProviderClient acquireContentProviderClient(String name) {
        Preconditions.checkNotNull(name, "name");
        IContentProvider provider = acquireProvider(name);
        if (provider != null) {
            return new ContentProviderClient(this, provider, true);
        }
        return null;
    }

    public final ContentProviderClient acquireUnstableContentProviderClient(Uri uri) {
        Preconditions.checkNotNull(uri, "uri");
        IContentProvider provider = acquireUnstableProvider(uri);
        if (provider != null) {
            return new ContentProviderClient(this, provider, false);
        }
        return null;
    }

    public final ContentProviderClient acquireUnstableContentProviderClient(String name) {
        Preconditions.checkNotNull(name, "name");
        IContentProvider provider = acquireUnstableProvider(name);
        if (provider != null) {
            return new ContentProviderClient(this, provider, false);
        }
        return null;
    }

    public final void registerContentObserver(Uri uri, boolean notifyForDescendents, ContentObserver observer) {
        Preconditions.checkNotNull(uri, "uri");
        Preconditions.checkNotNull(observer, "observer");
        registerContentObserver(ContentProvider.getUriWithoutUserId(uri), notifyForDescendents, observer, ContentProvider.getUserIdFromUri(uri, UserHandle.myUserId()));
    }

    public final void registerContentObserver(Uri uri, boolean notifyForDescendents, ContentObserver observer, int userHandle) {
        try {
            getContentService().registerContentObserver(uri, notifyForDescendents, observer.getContentObserver(), userHandle);
        } catch (RemoteException e) {
        }
    }

    public final void unregisterContentObserver(ContentObserver observer) {
        Preconditions.checkNotNull(observer, "observer");
        try {
            IContentObserver contentObserver = observer.releaseContentObserver();
            if (contentObserver != null) {
                getContentService().unregisterContentObserver(contentObserver);
            }
        } catch (RemoteException e) {
        }
    }

    public void notifyChange(Uri uri, ContentObserver observer) {
        notifyChange(uri, observer, true);
    }

    public void notifyChange(Uri uri, ContentObserver observer, boolean syncToNetwork) {
        Preconditions.checkNotNull(uri, "uri");
        notifyChange(ContentProvider.getUriWithoutUserId(uri), observer, syncToNetwork, ContentProvider.getUserIdFromUri(uri, UserHandle.myUserId()));
    }

    public void notifyChange(Uri uri, ContentObserver observer, boolean syncToNetwork, int userHandle) {
        try {
            IContentService contentService = getContentService();
            IContentObserver contentObserver = observer == null ? null : observer.getContentObserver();
            boolean z = observer != null && observer.deliverSelfNotifications();
            contentService.notifyChange(uri, contentObserver, z, syncToNetwork, userHandle);
        } catch (RemoteException e) {
        }
    }

    public void takePersistableUriPermission(Uri uri, int modeFlags) {
        Preconditions.checkNotNull(uri, "uri");
        try {
            ActivityManagerNative.getDefault().takePersistableUriPermission(ContentProvider.getUriWithoutUserId(uri), modeFlags, resolveUserId(uri));
        } catch (RemoteException e) {
        }
    }

    public void releasePersistableUriPermission(Uri uri, int modeFlags) {
        Preconditions.checkNotNull(uri, "uri");
        try {
            ActivityManagerNative.getDefault().releasePersistableUriPermission(ContentProvider.getUriWithoutUserId(uri), modeFlags, resolveUserId(uri));
        } catch (RemoteException e) {
        }
    }

    public List<UriPermission> getPersistedUriPermissions() {
        try {
            return ActivityManagerNative.getDefault().getPersistedUriPermissions(this.mPackageName, true).getList();
        } catch (RemoteException e) {
            throw new RuntimeException("Activity manager has died", e);
        }
    }

    public List<UriPermission> getOutgoingPersistedUriPermissions() {
        try {
            return ActivityManagerNative.getDefault().getPersistedUriPermissions(this.mPackageName, false).getList();
        } catch (RemoteException e) {
            throw new RuntimeException("Activity manager has died", e);
        }
    }

    @Deprecated
    public void startSync(Uri uri, Bundle extras) {
        Account account = null;
        if (extras != null) {
            String accountName = extras.getString("account");
            if (!TextUtils.isEmpty(accountName)) {
                account = new Account(accountName, "com.google");
            }
            extras.remove("account");
        }
        requestSync(account, uri != null ? uri.getAuthority() : null, extras);
    }

    public static void requestSync(Account account, String authority, Bundle extras) {
        requestSyncAsUser(account, authority, UserHandle.myUserId(), extras);
    }

    public static void requestSyncAsUser(Account account, String authority, int userId, Bundle extras) {
        if (extras == null) {
            throw new IllegalArgumentException("Must specify extras.");
        }
        try {
            getContentService().syncAsUser(new Builder().setSyncAdapter(account, authority).setExtras(extras).syncOnce().build(), userId);
        } catch (RemoteException e) {
        }
    }

    public static void requestSync(SyncRequest request) {
        try {
            getContentService().sync(request);
        } catch (RemoteException e) {
        }
    }

    public static void validateSyncExtrasBundle(Bundle extras) {
        try {
            for (String key : extras.keySet()) {
                Object value = extras.get(key);
                if (value != null && !(value instanceof Long) && !(value instanceof Integer) && !(value instanceof Boolean) && !(value instanceof Float) && !(value instanceof Double) && !(value instanceof String) && !(value instanceof Account)) {
                    throw new IllegalArgumentException("unexpected value type: " + value.getClass().getName());
                }
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (RuntimeException exc) {
            throw new IllegalArgumentException("error unparceling Bundle", exc);
        }
    }

    @Deprecated
    public void cancelSync(Uri uri) {
        String authority;
        if (uri != null) {
            authority = uri.getAuthority();
        } else {
            authority = null;
        }
        cancelSync(null, authority);
    }

    public static void cancelSync(Account account, String authority) {
        try {
            getContentService().cancelSync(account, authority, null);
        } catch (RemoteException e) {
        }
    }

    public static void cancelSyncAsUser(Account account, String authority, int userId) {
        try {
            getContentService().cancelSyncAsUser(account, authority, null, userId);
        } catch (RemoteException e) {
        }
    }

    public static SyncAdapterType[] getSyncAdapterTypes() {
        try {
            return getContentService().getSyncAdapterTypes();
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static SyncAdapterType[] getSyncAdapterTypesAsUser(int userId) {
        try {
            return getContentService().getSyncAdapterTypesAsUser(userId);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static String[] getSyncAdapterPackagesForAuthorityAsUser(String authority, int userId) {
        try {
            return getContentService().getSyncAdapterPackagesForAuthorityAsUser(authority, userId);
        } catch (RemoteException e) {
            return (String[]) ArrayUtils.emptyArray(String.class);
        }
    }

    public static boolean getSyncAutomatically(Account account, String authority) {
        try {
            return getContentService().getSyncAutomatically(account, authority);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static boolean getSyncAutomaticallyAsUser(Account account, String authority, int userId) {
        try {
            return getContentService().getSyncAutomaticallyAsUser(account, authority, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static void setSyncAutomatically(Account account, String authority, boolean sync) {
        setSyncAutomaticallyAsUser(account, authority, sync, UserHandle.myUserId());
    }

    public static void setSyncAutomaticallyAsUser(Account account, String authority, boolean sync, int userId) {
        try {
            getContentService().setSyncAutomaticallyAsUser(account, authority, sync, userId);
        } catch (RemoteException e) {
        }
    }

    public static void addPeriodicSync(Account account, String authority, Bundle extras, long pollFrequency) {
        validateSyncExtrasBundle(extras);
        if (extras.getBoolean("force", false) || extras.getBoolean(SYNC_EXTRAS_DO_NOT_RETRY, false) || extras.getBoolean(SYNC_EXTRAS_IGNORE_BACKOFF, false) || extras.getBoolean(SYNC_EXTRAS_IGNORE_SETTINGS, false) || extras.getBoolean(SYNC_EXTRAS_INITIALIZE, false) || extras.getBoolean("force", false) || extras.getBoolean(SYNC_EXTRAS_EXPEDITED, false)) {
            throw new IllegalArgumentException("illegal extras were set");
        }
        try {
            getContentService().addPeriodicSync(account, authority, extras, pollFrequency);
        } catch (RemoteException e) {
        }
    }

    public static boolean invalidPeriodicExtras(Bundle extras) {
        if (extras.getBoolean("force", false) || extras.getBoolean(SYNC_EXTRAS_DO_NOT_RETRY, false) || extras.getBoolean(SYNC_EXTRAS_IGNORE_BACKOFF, false) || extras.getBoolean(SYNC_EXTRAS_IGNORE_SETTINGS, false) || extras.getBoolean(SYNC_EXTRAS_INITIALIZE, false) || extras.getBoolean("force", false) || extras.getBoolean(SYNC_EXTRAS_EXPEDITED, false)) {
            return true;
        }
        return false;
    }

    public static void removePeriodicSync(Account account, String authority, Bundle extras) {
        validateSyncExtrasBundle(extras);
        try {
            getContentService().removePeriodicSync(account, authority, extras);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static void cancelSync(SyncRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request cannot be null");
        }
        try {
            getContentService().cancelRequest(request);
        } catch (RemoteException e) {
        }
    }

    public static List<PeriodicSync> getPeriodicSyncs(Account account, String authority) {
        try {
            return getContentService().getPeriodicSyncs(account, authority, null);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static int getIsSyncable(Account account, String authority) {
        try {
            return getContentService().getIsSyncable(account, authority);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static int getIsSyncableAsUser(Account account, String authority, int userId) {
        try {
            return getContentService().getIsSyncableAsUser(account, authority, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static void setIsSyncable(Account account, String authority, int syncable) {
        try {
            getContentService().setIsSyncable(account, authority, syncable);
        } catch (RemoteException e) {
        }
    }

    public static boolean getMasterSyncAutomatically() {
        try {
            return getContentService().getMasterSyncAutomatically();
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static boolean getMasterSyncAutomaticallyAsUser(int userId) {
        try {
            return getContentService().getMasterSyncAutomaticallyAsUser(userId);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static void setMasterSyncAutomatically(boolean sync) {
        setMasterSyncAutomaticallyAsUser(sync, UserHandle.myUserId());
    }

    public static void setMasterSyncAutomaticallyAsUser(boolean sync, int userId) {
        try {
            getContentService().setMasterSyncAutomaticallyAsUser(sync, userId);
        } catch (RemoteException e) {
        }
    }

    public static boolean isSyncActive(Account account, String authority) {
        if (account == null) {
            throw new IllegalArgumentException("account must not be null");
        } else if (authority == null) {
            throw new IllegalArgumentException("authority must not be null");
        } else {
            try {
                return getContentService().isSyncActive(account, authority, null);
            } catch (RemoteException e) {
                throw new RuntimeException("the ContentService should always be reachable", e);
            }
        }
    }

    @Deprecated
    public static SyncInfo getCurrentSync() {
        try {
            List<SyncInfo> syncs = getContentService().getCurrentSyncs();
            if (syncs.isEmpty()) {
                return null;
            }
            return (SyncInfo) syncs.get(0);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static List<SyncInfo> getCurrentSyncs() {
        try {
            return getContentService().getCurrentSyncs();
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static List<SyncInfo> getCurrentSyncsAsUser(int userId) {
        try {
            return getContentService().getCurrentSyncsAsUser(userId);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static SyncStatusInfo getSyncStatus(Account account, String authority) {
        try {
            return getContentService().getSyncStatus(account, authority, null);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static SyncStatusInfo getSyncStatusAsUser(Account account, String authority, int userId) {
        try {
            return getContentService().getSyncStatusAsUser(account, authority, null, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static boolean isSyncPending(Account account, String authority) {
        return isSyncPendingAsUser(account, authority, UserHandle.myUserId());
    }

    public static boolean isSyncPendingAsUser(Account account, String authority, int userId) {
        try {
            return getContentService().isSyncPendingAsUser(account, authority, null, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static Object addStatusChangeListener(int mask, final SyncStatusObserver callback) {
        if (callback == null) {
            throw new IllegalArgumentException("you passed in a null callback");
        }
        try {
            Stub observer = new Stub() {
                public void onStatusChanged(int which) throws RemoteException {
                    callback.onStatusChanged(which);
                }
            };
            getContentService().addStatusChangeListener(mask, observer);
            return observer;
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    public static void removeStatusChangeListener(Object handle) {
        if (handle == null) {
            throw new IllegalArgumentException("you passed in a null handle");
        }
        try {
            getContentService().removeStatusChangeListener((Stub) handle);
        } catch (RemoteException e) {
        }
    }

    public static void setIsSyncable(Account account, String authority, int syncable, int userID) {
        try {
            getContentService().setIsSyncableAsUser(account, authority, syncable, userID);
        } catch (RemoteException e) {
        }
    }

    public static void setSyncAutomatically(Account account, String authority, boolean sync, int userID) {
        try {
            getContentService().setSyncAutomaticallyAsUser(account, authority, sync, userID);
        } catch (RemoteException e) {
        }
    }

    public static boolean getSyncAutomatically(Account account, String authority, int userID) {
        try {
            return getContentService().getSyncAutomaticallyAsUser(account, authority, userID);
        } catch (RemoteException e) {
            throw new RuntimeException("the ContentService should always be reachable", e);
        }
    }

    private int samplePercentForDuration(long durationMillis) {
        if (durationMillis >= 500) {
            return 100;
        }
        return ((int) ((100 * durationMillis) / 500)) + 1;
    }

    private void maybeLogQueryToEventLog(long durationMillis, Uri uri, String[] projection, String selection, String sortOrder) {
    }

    private void maybeLogUpdateToEventLog(long durationMillis, Uri uri, String operation, String selection) {
    }

    public static IContentService getContentService() {
        if (sContentService != null) {
            return sContentService;
        }
        sContentService = IContentService.Stub.asInterface(ServiceManager.getService("content"));
        return sContentService;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    Context getContext() {
        return this.mContext;
    }

    public int resolveUserId(Uri uri) {
        return ContentProvider.getUserIdFromUri(uri, this.mContext.getUserId());
    }
}
