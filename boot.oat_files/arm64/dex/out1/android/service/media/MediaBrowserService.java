package android.service.media;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ParceledListSlice;
import android.media.browse.MediaBrowser.MediaItem;
import android.media.session.MediaSession.Token;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.service.media.IMediaBrowserService.Stub;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

public abstract class MediaBrowserService extends Service {
    private static final boolean DBG = false;
    public static final String KEY_MEDIA_ITEM = "media_item";
    public static final String SERVICE_INTERFACE = "android.media.browse.MediaBrowserService";
    private static final String TAG = "MediaBrowserService";
    private ServiceBinder mBinder;
    private final ArrayMap<IBinder, ConnectionRecord> mConnections = new ArrayMap();
    private final Handler mHandler = new Handler();
    Token mSession;

    public class Result<T> {
        private Object mDebug;
        private boolean mDetachCalled;
        private boolean mSendResultCalled;

        Result(Object debug) {
            this.mDebug = debug;
        }

        public void sendResult(T result) {
            if (this.mSendResultCalled) {
                throw new IllegalStateException("sendResult() called twice for: " + this.mDebug);
            }
            this.mSendResultCalled = true;
            onResultSent(result);
        }

        public void detach() {
            if (this.mDetachCalled) {
                throw new IllegalStateException("detach() called when detach() had already been called for: " + this.mDebug);
            } else if (this.mSendResultCalled) {
                throw new IllegalStateException("detach() called when sendResult() had already been called for: " + this.mDebug);
            } else {
                this.mDetachCalled = true;
            }
        }

        boolean isDone() {
            return this.mDetachCalled || this.mSendResultCalled;
        }

        void onResultSent(T t) {
        }
    }

    public static final class BrowserRoot {
        private final Bundle mExtras;
        private final String mRootId;

        public BrowserRoot(String rootId, Bundle extras) {
            if (rootId == null) {
                throw new IllegalArgumentException("The root id in BrowserRoot cannot be null. Use null for BrowserRoot instead.");
            }
            this.mRootId = rootId;
            this.mExtras = extras;
        }

        public String getRootId() {
            return this.mRootId;
        }

        public Bundle getExtras() {
            return this.mExtras;
        }
    }

    private class ConnectionRecord {
        IMediaBrowserServiceCallbacks callbacks;
        String pkg;
        BrowserRoot root;
        Bundle rootHints;
        HashSet<String> subscriptions;

        private ConnectionRecord() {
            this.subscriptions = new HashSet();
        }
    }

    private class ServiceBinder extends Stub {
        private ServiceBinder() {
        }

        public void connect(String pkg, Bundle rootHints, IMediaBrowserServiceCallbacks callbacks) {
            final int uid = Binder.getCallingUid();
            if (MediaBrowserService.this.isValidPackage(pkg, uid)) {
                final IMediaBrowserServiceCallbacks iMediaBrowserServiceCallbacks = callbacks;
                final String str = pkg;
                final Bundle bundle = rootHints;
                MediaBrowserService.this.mHandler.post(new Runnable() {
                    public void run() {
                        IBinder b = iMediaBrowserServiceCallbacks.asBinder();
                        MediaBrowserService.this.mConnections.remove(b);
                        ConnectionRecord connection = new ConnectionRecord();
                        connection.pkg = str;
                        connection.rootHints = bundle;
                        connection.callbacks = iMediaBrowserServiceCallbacks;
                        connection.root = MediaBrowserService.this.onGetRoot(str, uid, bundle);
                        if (connection.root == null) {
                            Log.i(MediaBrowserService.TAG, "No root for client " + str + " from service " + getClass().getName());
                            try {
                                iMediaBrowserServiceCallbacks.onConnectFailed();
                                return;
                            } catch (RemoteException e) {
                                Log.w(MediaBrowserService.TAG, "Calling onConnectFailed() failed. Ignoring. pkg=" + str);
                                return;
                            }
                        }
                        try {
                            MediaBrowserService.this.mConnections.put(b, connection);
                            if (MediaBrowserService.this.mSession != null) {
                                iMediaBrowserServiceCallbacks.onConnect(connection.root.getRootId(), MediaBrowserService.this.mSession, connection.root.getExtras());
                            }
                        } catch (RemoteException e2) {
                            Log.w(MediaBrowserService.TAG, "Calling onConnect() failed. Dropping client. pkg=" + str);
                            MediaBrowserService.this.mConnections.remove(b);
                        }
                    }
                });
                return;
            }
            throw new IllegalArgumentException("Package/uid mismatch: uid=" + uid + " package=" + pkg);
        }

        public void disconnect(final IMediaBrowserServiceCallbacks callbacks) {
            MediaBrowserService.this.mHandler.post(new Runnable() {
                public void run() {
                    if (((ConnectionRecord) MediaBrowserService.this.mConnections.remove(callbacks.asBinder())) == null) {
                    }
                }
            });
        }

        public void addSubscription(final String id, final IMediaBrowserServiceCallbacks callbacks) {
            MediaBrowserService.this.mHandler.post(new Runnable() {
                public void run() {
                    ConnectionRecord connection = (ConnectionRecord) MediaBrowserService.this.mConnections.get(callbacks.asBinder());
                    if (connection == null) {
                        Log.w(MediaBrowserService.TAG, "addSubscription for callback that isn't registered id=" + id);
                    } else {
                        MediaBrowserService.this.addSubscription(id, connection);
                    }
                }
            });
        }

        public void removeSubscription(final String id, final IMediaBrowserServiceCallbacks callbacks) {
            MediaBrowserService.this.mHandler.post(new Runnable() {
                public void run() {
                    ConnectionRecord connection = (ConnectionRecord) MediaBrowserService.this.mConnections.get(callbacks.asBinder());
                    if (connection == null) {
                        Log.w(MediaBrowserService.TAG, "removeSubscription for callback that isn't registered id=" + id);
                    } else if (!connection.subscriptions.remove(id)) {
                        Log.w(MediaBrowserService.TAG, "removeSubscription called for " + id + " which is not subscribed");
                    }
                }
            });
        }

        public void getMediaItem(final String mediaId, final ResultReceiver receiver) {
            if (!TextUtils.isEmpty(mediaId) && receiver != null) {
                MediaBrowserService.this.mHandler.post(new Runnable() {
                    public void run() {
                        MediaBrowserService.this.performLoadItem(mediaId, receiver);
                    }
                });
            }
        }
    }

    public abstract BrowserRoot onGetRoot(String str, int i, Bundle bundle);

    public abstract void onLoadChildren(String str, Result<List<MediaItem>> result);

    public void onCreate() {
        super.onCreate();
        this.mBinder = new ServiceBinder();
    }

    public IBinder onBind(Intent intent) {
        if (SERVICE_INTERFACE.equals(intent.getAction())) {
            return this.mBinder;
        }
        return null;
    }

    public void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
    }

    public void onLoadItem(String itemId, Result<MediaItem> result) {
        result.sendResult(null);
    }

    public void setSessionToken(final Token token) {
        if (token == null) {
            throw new IllegalArgumentException("Session token may not be null.");
        } else if (this.mSession != null) {
            throw new IllegalStateException("The session token has already been set.");
        } else {
            this.mSession = token;
            this.mHandler.post(new Runnable() {
                public void run() {
                    for (IBinder key : MediaBrowserService.this.mConnections.keySet()) {
                        ConnectionRecord connection = (ConnectionRecord) MediaBrowserService.this.mConnections.get(key);
                        try {
                            connection.callbacks.onConnect(connection.root.getRootId(), token, connection.root.getExtras());
                        } catch (RemoteException e) {
                            Log.w(MediaBrowserService.TAG, "Connection for " + connection.pkg + " is no longer valid.");
                            MediaBrowserService.this.mConnections.remove(key);
                        }
                    }
                }
            });
        }
    }

    public Token getSessionToken() {
        return this.mSession;
    }

    public void notifyChildrenChanged(final String parentId) {
        if (parentId == null) {
            throw new IllegalArgumentException("parentId cannot be null in notifyChildrenChanged");
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                for (IBinder binder : MediaBrowserService.this.mConnections.keySet()) {
                    ConnectionRecord connection = (ConnectionRecord) MediaBrowserService.this.mConnections.get(binder);
                    if (connection.subscriptions.contains(parentId)) {
                        MediaBrowserService.this.performLoadChildren(parentId, connection);
                    }
                }
            }
        });
    }

    private boolean isValidPackage(String pkg, int uid) {
        if (pkg == null) {
            return false;
        }
        for (String equals : getPackageManager().getPackagesForUid(uid)) {
            if (equals.equals(pkg)) {
                return true;
            }
        }
        return false;
    }

    private void addSubscription(String id, ConnectionRecord connection) {
        connection.subscriptions.add(id);
        performLoadChildren(id, connection);
    }

    private void performLoadChildren(final String parentId, final ConnectionRecord connection) {
        Result<List<MediaItem>> result = new Result<List<MediaItem>>(parentId) {
            void onResultSent(List<MediaItem> list) {
                if (list == null) {
                    throw new IllegalStateException("onLoadChildren sent null list for id " + parentId);
                } else if (MediaBrowserService.this.mConnections.get(connection.callbacks.asBinder()) == connection) {
                    try {
                        connection.callbacks.onLoadChildren(parentId, new ParceledListSlice(list));
                    } catch (RemoteException e) {
                        Log.w(MediaBrowserService.TAG, "Calling onLoadChildren() failed for id=" + parentId + " package=" + connection.pkg);
                    }
                }
            }
        };
        onLoadChildren(parentId, result);
        if (!result.isDone()) {
            throw new IllegalStateException("onLoadChildren must call detach() or sendResult() before returning for package=" + connection.pkg + " id=" + parentId);
        }
    }

    private void performLoadItem(String itemId, final ResultReceiver receiver) {
        Result<MediaItem> result = new Result<MediaItem>(itemId) {
            void onResultSent(MediaItem item) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(MediaBrowserService.KEY_MEDIA_ITEM, item);
                receiver.send(0, bundle);
            }
        };
        onLoadItem(itemId, result);
        if (!result.isDone()) {
            throw new IllegalStateException("onLoadItem must call detach() or sendResult() before returning for id=" + itemId);
        }
    }
}
