package android.content;

import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.ICancellationSignal;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import com.kddi.android.internal.pdg.PdgContactsAccessChecker;
import com.sec.android.app.CscFeature;
import dalvik.system.CloseGuard;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ContentProviderClient {
    private static final String TAG = "ContentProviderClient";
    @GuardedBy("ContentProviderClient.class")
    private static Handler sAnrHandler;
    private NotRespondingRunnable mAnrRunnable;
    private long mAnrTimeout;
    private final IContentProvider mContentProvider;
    private final ContentResolver mContentResolver;
    private final CloseGuard mGuard = CloseGuard.get();
    private final String mPackageName;
    private boolean mReleased;
    private final boolean mStable;

    private class NotRespondingRunnable implements Runnable {
        private NotRespondingRunnable() {
        }

        public void run() {
            Log.w(ContentProviderClient.TAG, "Detected provider not responding: " + ContentProviderClient.this.mContentProvider);
            ContentProviderClient.this.mContentResolver.appNotRespondingViaProvider(ContentProviderClient.this.mContentProvider);
        }
    }

    ContentProviderClient(ContentResolver contentResolver, IContentProvider contentProvider, boolean stable) {
        this.mContentResolver = contentResolver;
        this.mContentProvider = contentProvider;
        this.mPackageName = contentResolver.mPackageName;
        this.mStable = stable;
        this.mGuard.open("release");
    }

    public void setDetectNotResponding(long timeoutMillis) {
        synchronized (ContentProviderClient.class) {
            this.mAnrTimeout = timeoutMillis;
            if (timeoutMillis > 0) {
                if (this.mAnrRunnable == null) {
                    this.mAnrRunnable = new NotRespondingRunnable();
                }
                if (sAnrHandler == null) {
                    sAnrHandler = new Handler(Looper.getMainLooper(), null, true);
                }
            } else {
                this.mAnrRunnable = null;
            }
        }
    }

    private void beforeRemote() {
        if (this.mAnrRunnable != null) {
            sAnrHandler.postDelayed(this.mAnrRunnable, this.mAnrTimeout);
        }
    }

    private void afterRemote() {
        if (this.mAnrRunnable != null) {
            sAnrHandler.removeCallbacks(this.mAnrRunnable);
        }
    }

    public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sortOrder) throws RemoteException {
        return query(url, projection, selection, selectionArgs, sortOrder, null);
    }

    public Cursor query(Uri url, String[] projection, String selection, String[] selectionArgs, String sortOrder, CancellationSignal cancellationSignal) throws RemoteException {
        Cursor query;
        Preconditions.checkNotNull(url, "url");
        beforeRemote();
        ICancellationSignal remoteCancellationSignal = null;
        if (cancellationSignal != null) {
            try {
                cancellationSignal.throwIfCanceled();
                remoteCancellationSignal = this.mContentProvider.createCancellationSignal();
                cancellationSignal.setRemote(remoteCancellationSignal);
            } catch (DeadObjectException e) {
                if (!this.mStable) {
                    this.mContentResolver.unstableProviderDied(this.mContentProvider);
                }
                throw e;
            } catch (Throwable th) {
                afterRemote();
            }
        }
        if (CscFeature.getInstance().getEnableStatus("CscFeature_Common_EnablePrivacyDataGuard") && !PdgContactsAccessChecker.checkPrivacy(this.mContentResolver.getContext(), url)) {
            query = this.mContentProvider.query(this.mPackageName, url, projection, selection, selectionArgs, sortOrder, remoteCancellationSignal);
            if (query != null) {
                String name = query.getColumnName(0);
                selection = name + " NOT NULL AND " + name + "='fill_long_long_character_here_abcdefghijklmnopqrstuvwxyz'";
                selectionArgs = null;
                query.close();
            } else {
                afterRemote();
                return query;
            }
        }
        query = this.mContentProvider.query(this.mPackageName, url, projection, selection, selectionArgs, sortOrder, remoteCancellationSignal);
        afterRemote();
        return query;
    }

    public String getType(Uri url) throws RemoteException {
        Preconditions.checkNotNull(url, "url");
        beforeRemote();
        try {
            String type = this.mContentProvider.getType(url);
            afterRemote();
            return type;
        } catch (DeadObjectException e) {
            if (!this.mStable) {
                this.mContentResolver.unstableProviderDied(this.mContentProvider);
            }
            throw e;
        } catch (Throwable th) {
            afterRemote();
        }
    }

    public String[] getStreamTypes(Uri url, String mimeTypeFilter) throws RemoteException {
        Preconditions.checkNotNull(url, "url");
        Preconditions.checkNotNull(mimeTypeFilter, "mimeTypeFilter");
        beforeRemote();
        try {
            String[] streamTypes = this.mContentProvider.getStreamTypes(url, mimeTypeFilter);
            afterRemote();
            return streamTypes;
        } catch (DeadObjectException e) {
            if (!this.mStable) {
                this.mContentResolver.unstableProviderDied(this.mContentProvider);
            }
            throw e;
        } catch (Throwable th) {
            afterRemote();
        }
    }

    public final Uri canonicalize(Uri url) throws RemoteException {
        Preconditions.checkNotNull(url, "url");
        beforeRemote();
        try {
            Uri canonicalize = this.mContentProvider.canonicalize(this.mPackageName, url);
            afterRemote();
            return canonicalize;
        } catch (DeadObjectException e) {
            if (!this.mStable) {
                this.mContentResolver.unstableProviderDied(this.mContentProvider);
            }
            throw e;
        } catch (Throwable th) {
            afterRemote();
        }
    }

    public final Uri uncanonicalize(Uri url) throws RemoteException {
        Preconditions.checkNotNull(url, "url");
        beforeRemote();
        try {
            Uri uncanonicalize = this.mContentProvider.uncanonicalize(this.mPackageName, url);
            afterRemote();
            return uncanonicalize;
        } catch (DeadObjectException e) {
            if (!this.mStable) {
                this.mContentResolver.unstableProviderDied(this.mContentProvider);
            }
            throw e;
        } catch (Throwable th) {
            afterRemote();
        }
    }

    public Uri insert(Uri url, ContentValues initialValues) throws RemoteException {
        Preconditions.checkNotNull(url, "url");
        beforeRemote();
        try {
            Uri insert = this.mContentProvider.insert(this.mPackageName, url, initialValues);
            afterRemote();
            return insert;
        } catch (DeadObjectException e) {
            if (!this.mStable) {
                this.mContentResolver.unstableProviderDied(this.mContentProvider);
            }
            throw e;
        } catch (Throwable th) {
            afterRemote();
        }
    }

    public int bulkInsert(Uri url, ContentValues[] initialValues) throws RemoteException {
        Preconditions.checkNotNull(url, "url");
        Preconditions.checkNotNull(initialValues, "initialValues");
        beforeRemote();
        try {
            int bulkInsert = this.mContentProvider.bulkInsert(this.mPackageName, url, initialValues);
            afterRemote();
            return bulkInsert;
        } catch (DeadObjectException e) {
            if (!this.mStable) {
                this.mContentResolver.unstableProviderDied(this.mContentProvider);
            }
            throw e;
        } catch (Throwable th) {
            afterRemote();
        }
    }

    public int delete(Uri url, String selection, String[] selectionArgs) throws RemoteException {
        Preconditions.checkNotNull(url, "url");
        beforeRemote();
        try {
            int delete = this.mContentProvider.delete(this.mPackageName, url, selection, selectionArgs);
            afterRemote();
            return delete;
        } catch (DeadObjectException e) {
            if (!this.mStable) {
                this.mContentResolver.unstableProviderDied(this.mContentProvider);
            }
            throw e;
        } catch (Throwable th) {
            afterRemote();
        }
    }

    public int update(Uri url, ContentValues values, String selection, String[] selectionArgs) throws RemoteException {
        Preconditions.checkNotNull(url, "url");
        beforeRemote();
        try {
            int update = this.mContentProvider.update(this.mPackageName, url, values, selection, selectionArgs);
            afterRemote();
            return update;
        } catch (DeadObjectException e) {
            if (!this.mStable) {
                this.mContentResolver.unstableProviderDied(this.mContentProvider);
            }
            throw e;
        } catch (Throwable th) {
            afterRemote();
        }
    }

    public ParcelFileDescriptor openFile(Uri url, String mode) throws RemoteException, FileNotFoundException {
        return openFile(url, mode, null);
    }

    public ParcelFileDescriptor openFile(Uri url, String mode, CancellationSignal signal) throws RemoteException, FileNotFoundException {
        Preconditions.checkNotNull(url, "url");
        Preconditions.checkNotNull(mode, "mode");
        beforeRemote();
        ICancellationSignal remoteSignal = null;
        if (signal != null) {
            try {
                signal.throwIfCanceled();
                remoteSignal = this.mContentProvider.createCancellationSignal();
                signal.setRemote(remoteSignal);
            } catch (DeadObjectException e) {
                if (!this.mStable) {
                    this.mContentResolver.unstableProviderDied(this.mContentProvider);
                }
                throw e;
            } catch (Throwable th) {
                afterRemote();
            }
        }
        ParcelFileDescriptor openFile = this.mContentProvider.openFile(this.mPackageName, url, mode, remoteSignal, null);
        afterRemote();
        return openFile;
    }

    public AssetFileDescriptor openAssetFile(Uri url, String mode) throws RemoteException, FileNotFoundException {
        return openAssetFile(url, mode, null);
    }

    public AssetFileDescriptor openAssetFile(Uri url, String mode, CancellationSignal signal) throws RemoteException, FileNotFoundException {
        Preconditions.checkNotNull(url, "url");
        Preconditions.checkNotNull(mode, "mode");
        beforeRemote();
        ICancellationSignal remoteSignal = null;
        if (signal != null) {
            try {
                signal.throwIfCanceled();
                remoteSignal = this.mContentProvider.createCancellationSignal();
                signal.setRemote(remoteSignal);
            } catch (DeadObjectException e) {
                if (!this.mStable) {
                    this.mContentResolver.unstableProviderDied(this.mContentProvider);
                }
                throw e;
            } catch (Throwable th) {
                afterRemote();
            }
        }
        AssetFileDescriptor openAssetFile = this.mContentProvider.openAssetFile(this.mPackageName, url, mode, remoteSignal);
        afterRemote();
        return openAssetFile;
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri uri, String mimeType, Bundle opts) throws RemoteException, FileNotFoundException {
        return openTypedAssetFileDescriptor(uri, mimeType, opts, null);
    }

    public final AssetFileDescriptor openTypedAssetFileDescriptor(Uri uri, String mimeType, Bundle opts, CancellationSignal signal) throws RemoteException, FileNotFoundException {
        Preconditions.checkNotNull(uri, "uri");
        Preconditions.checkNotNull(mimeType, "mimeType");
        beforeRemote();
        ICancellationSignal remoteSignal = null;
        if (signal != null) {
            try {
                signal.throwIfCanceled();
                remoteSignal = this.mContentProvider.createCancellationSignal();
                signal.setRemote(remoteSignal);
            } catch (DeadObjectException e) {
                if (!this.mStable) {
                    this.mContentResolver.unstableProviderDied(this.mContentProvider);
                }
                throw e;
            } catch (Throwable th) {
                afterRemote();
            }
        }
        AssetFileDescriptor openTypedAssetFile = this.mContentProvider.openTypedAssetFile(this.mPackageName, uri, mimeType, opts, remoteSignal);
        afterRemote();
        return openTypedAssetFile;
    }

    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws RemoteException, OperationApplicationException {
        Preconditions.checkNotNull(operations, "operations");
        beforeRemote();
        try {
            ContentProviderResult[] applyBatch = this.mContentProvider.applyBatch(this.mPackageName, operations);
            afterRemote();
            return applyBatch;
        } catch (DeadObjectException e) {
            if (!this.mStable) {
                this.mContentResolver.unstableProviderDied(this.mContentProvider);
            }
            throw e;
        } catch (Throwable th) {
            afterRemote();
        }
    }

    public Bundle call(String method, String arg, Bundle extras) throws RemoteException {
        Preconditions.checkNotNull(method, RemindersColumns.METHOD);
        beforeRemote();
        try {
            Bundle call = this.mContentProvider.call(this.mPackageName, method, arg, extras);
            afterRemote();
            return call;
        } catch (DeadObjectException e) {
            if (!this.mStable) {
                this.mContentResolver.unstableProviderDied(this.mContentProvider);
            }
            throw e;
        } catch (Throwable th) {
            afterRemote();
        }
    }

    public boolean release() {
        boolean releaseProvider;
        synchronized (this) {
            if (this.mReleased) {
                throw new IllegalStateException("Already released");
            }
            this.mReleased = true;
            this.mGuard.close();
            if (this.mStable) {
                releaseProvider = this.mContentResolver.releaseProvider(this.mContentProvider);
            } else {
                releaseProvider = this.mContentResolver.releaseUnstableProvider(this.mContentProvider);
            }
        }
        return releaseProvider;
    }

    protected void finalize() throws Throwable {
        if (this.mGuard != null) {
            this.mGuard.warnIfOpen();
        }
    }

    public ContentProvider getLocalContentProvider() {
        return ContentProvider.coerceToLocalContentProvider(this.mContentProvider);
    }

    public static void releaseQuietly(ContentProviderClient client) {
        if (client != null) {
            try {
                client.release();
            } catch (Exception e) {
            }
        }
    }
}
