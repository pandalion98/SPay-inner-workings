package android.media;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.IRemoteControlDisplay.Stub;
import android.media.session.MediaController;
import android.media.session.MediaController.Callback;
import android.media.session.MediaSessionLegacyHelper;
import android.media.session.MediaSessionManager;
import android.media.session.MediaSessionManager.OnActiveSessionsChangedListener;
import android.media.session.PlaybackState;
import android.net.ProxyInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.List;

@Deprecated
public final class RemoteController {
    private static final boolean DEBUG = false;
    private static final int MAX_BITMAP_DIMENSION = 512;
    private static final int MSG_CLIENT_CHANGE = 4;
    private static final int MSG_DISPLAY_ENABLE = 5;
    private static final int MSG_NEW_MEDIA_METADATA = 7;
    private static final int MSG_NEW_METADATA = 3;
    private static final int MSG_NEW_PENDING_INTENT = 0;
    private static final int MSG_NEW_PLAYBACK_INFO = 1;
    private static final int MSG_NEW_PLAYBACK_STATE = 6;
    private static final int MSG_NEW_TRANSPORT_INFO = 2;
    public static final int POSITION_SYNCHRONIZATION_CHECK = 1;
    public static final int POSITION_SYNCHRONIZATION_NONE = 0;
    private static final int SENDMSG_NOOP = 1;
    private static final int SENDMSG_QUEUE = 2;
    private static final int SENDMSG_REPLACE = 0;
    private static final String TAG = "RemoteController";
    private static final int TRANSPORT_UNKNOWN = 0;
    private static final boolean USE_SESSIONS = true;
    private static final Object mGenLock = new Object();
    private static final Object mInfoLock = new Object();
    private int mArtworkHeight;
    private int mArtworkWidth;
    private final AudioManager mAudioManager;
    private int mClientGenerationIdCurrent;
    private PendingIntent mClientPendingIntentCurrent;
    private final Context mContext;
    private MediaController mCurrentSession;
    private boolean mEnabled;
    private final EventHandler mEventHandler;
    private boolean mIsRegistered;
    private PlaybackInfo mLastPlaybackInfo;
    private final int mMaxBitmapDimension;
    private MetadataEditor mMetadataEditor;
    private OnClientUpdateListener mOnClientUpdateListener;
    private final RcDisplay mRcd;
    private Callback mSessionCb;
    private OnActiveSessionsChangedListener mSessionListener;
    private MediaSessionManager mSessionManager;

    private class EventHandler extends Handler {
        public EventHandler(RemoteController rc, Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            boolean z = true;
            RemoteController remoteController;
            switch (msg.what) {
                case 0:
                    RemoteController.this.onNewPendingIntent(msg.arg1, (PendingIntent) msg.obj);
                    return;
                case 1:
                    RemoteController.this.onNewPlaybackInfo(msg.arg1, (PlaybackInfo) msg.obj);
                    return;
                case 2:
                    RemoteController.this.onNewTransportInfo(msg.arg1, msg.arg2);
                    return;
                case 3:
                    RemoteController.this.onNewMetadata(msg.arg1, (Bundle) msg.obj);
                    return;
                case 4:
                    remoteController = RemoteController.this;
                    int i = msg.arg1;
                    if (msg.arg2 != 1) {
                        z = false;
                    }
                    remoteController.onClientChange(i, z);
                    return;
                case 5:
                    remoteController = RemoteController.this;
                    if (msg.arg1 != 1) {
                        z = false;
                    }
                    remoteController.onDisplayEnable(z);
                    return;
                case 6:
                    RemoteController.this.onNewPlaybackState((PlaybackState) msg.obj);
                    return;
                case 7:
                    RemoteController.this.onNewMediaMetadata((MediaMetadata) msg.obj);
                    return;
                default:
                    Log.e(RemoteController.TAG, "unknown event " + msg.what);
                    return;
            }
        }
    }

    private class MediaControllerCallback extends Callback {
        private MediaControllerCallback() {
        }

        public void onPlaybackStateChanged(PlaybackState state) {
            RemoteController.this.onNewPlaybackState(state);
        }

        public void onMetadataChanged(MediaMetadata metadata) {
            RemoteController.this.onNewMediaMetadata(metadata);
        }
    }

    public class MetadataEditor extends MediaMetadataEditor {
        protected MetadataEditor() {
        }

        protected MetadataEditor(Bundle metadata, long editableKeys) {
            this.mEditorMetadata = metadata;
            this.mEditableKeys = editableKeys;
            this.mEditorArtwork = (Bitmap) metadata.getParcelable(String.valueOf(100));
            if (this.mEditorArtwork != null) {
                cleanupBitmapFromBundle(100);
            }
            this.mMetadataChanged = true;
            this.mArtworkChanged = true;
            this.mApplied = false;
        }

        private void cleanupBitmapFromBundle(int key) {
            if (METADATA_KEYS_TYPE.get(key, -1) == 2) {
                this.mEditorMetadata.remove(String.valueOf(key));
            }
        }

        public synchronized void apply() {
            if (this.mMetadataChanged) {
                synchronized (RemoteController.mInfoLock) {
                    if (RemoteController.this.mCurrentSession != null && this.mEditorMetadata.containsKey(String.valueOf(MediaMetadataEditor.RATING_KEY_BY_USER))) {
                        Rating rating = (Rating) getObject(MediaMetadataEditor.RATING_KEY_BY_USER, null);
                        if (rating != null) {
                            RemoteController.this.mCurrentSession.getTransportControls().setRating(rating);
                        }
                    }
                }
                this.mApplied = false;
            }
        }
    }

    public interface OnClientUpdateListener {
        void onClientChange(boolean z);

        void onClientMetadataUpdate(MetadataEditor metadataEditor);

        void onClientPlaybackStateUpdate(int i);

        void onClientPlaybackStateUpdate(int i, long j, long j2, float f);

        void onClientTransportControlUpdate(int i);
    }

    private static class PlaybackInfo {
        long mCurrentPosMs;
        float mSpeed;
        int mState;
        long mStateChangeTimeMs;

        PlaybackInfo(int state, long stateChangeTimeMs, long currentPosMs, float speed) {
            this.mState = state;
            this.mStateChangeTimeMs = stateChangeTimeMs;
            this.mCurrentPosMs = currentPosMs;
            this.mSpeed = speed;
        }
    }

    private static class RcDisplay extends Stub {
        private final WeakReference<RemoteController> mController;

        RcDisplay(RemoteController rc) {
            this.mController = new WeakReference(rc);
        }

        public void setCurrentClientId(int genId, PendingIntent clientMediaIntent, boolean clearing) {
            RemoteController rc = (RemoteController) this.mController.get();
            if (rc != null) {
                boolean isNew = false;
                synchronized (RemoteController.mGenLock) {
                    if (rc.mClientGenerationIdCurrent != genId) {
                        rc.mClientGenerationIdCurrent = genId;
                        isNew = true;
                    }
                }
                if (clientMediaIntent != null) {
                    RemoteController.sendMsg(rc.mEventHandler, 0, 0, genId, 0, clientMediaIntent, 0);
                }
                if (isNew || clearing) {
                    RemoteController.sendMsg(rc.mEventHandler, 4, 0, genId, clearing ? 1 : 0, null, 0);
                }
            }
        }

        public void setEnabled(boolean enabled) {
            RemoteController rc = (RemoteController) this.mController.get();
            if (rc != null) {
                RemoteController.sendMsg(rc.mEventHandler, 5, 0, enabled ? 1 : 0, 0, null, 0);
            }
        }

        public void setPlaybackState(int genId, int state, long stateChangeTimeMs, long currentPosMs, float speed) {
            RemoteController rc = (RemoteController) this.mController.get();
            if (rc != null) {
                synchronized (RemoteController.mGenLock) {
                    if (rc.mClientGenerationIdCurrent != genId) {
                        return;
                    }
                    RemoteController.sendMsg(rc.mEventHandler, 1, 0, genId, 0, new PlaybackInfo(state, stateChangeTimeMs, currentPosMs, speed), 0);
                }
            }
        }

        public void setTransportControlInfo(int genId, int transportControlFlags, int posCapabilities) {
            RemoteController rc = (RemoteController) this.mController.get();
            if (rc != null) {
                synchronized (RemoteController.mGenLock) {
                    if (rc.mClientGenerationIdCurrent != genId) {
                        return;
                    }
                    RemoteController.sendMsg(rc.mEventHandler, 2, 0, genId, transportControlFlags, null, 0);
                }
            }
        }

        public void setMetadata(int genId, Bundle metadata) {
            RemoteController rc = (RemoteController) this.mController.get();
            if (rc != null && metadata != null) {
                synchronized (RemoteController.mGenLock) {
                    if (rc.mClientGenerationIdCurrent != genId) {
                        return;
                    }
                    RemoteController.sendMsg(rc.mEventHandler, 3, 2, genId, 0, metadata, 0);
                }
            }
        }

        public void setArtwork(int genId, Bitmap artwork) {
            RemoteController rc = (RemoteController) this.mController.get();
            if (rc != null) {
                synchronized (RemoteController.mGenLock) {
                    if (rc.mClientGenerationIdCurrent != genId) {
                        return;
                    }
                    Bundle metadata = new Bundle(1);
                    metadata.putParcelable(String.valueOf(100), artwork);
                    RemoteController.sendMsg(rc.mEventHandler, 3, 2, genId, 0, metadata, 0);
                }
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void setAllMetadata(int r9, android.os.Bundle r10, android.graphics.Bitmap r11) {
            /*
            r8 = this;
            r4 = 0;
            r0 = r8.mController;
            r7 = r0.get();
            r7 = (android.media.RemoteController) r7;
            if (r7 != 0) goto L_0x000c;
        L_0x000b:
            return;
        L_0x000c:
            if (r10 != 0) goto L_0x0010;
        L_0x000e:
            if (r11 == 0) goto L_0x000b;
        L_0x0010:
            r1 = android.media.RemoteController.mGenLock;
            monitor-enter(r1);
            r0 = r7.mClientGenerationIdCurrent;	 Catch:{ all -> 0x001d }
            if (r0 == r9) goto L_0x0020;
        L_0x001b:
            monitor-exit(r1);	 Catch:{ all -> 0x001d }
            goto L_0x000b;
        L_0x001d:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x001d }
            throw r0;
        L_0x0020:
            monitor-exit(r1);	 Catch:{ all -> 0x001d }
            if (r10 != 0) goto L_0x0029;
        L_0x0023:
            r10 = new android.os.Bundle;
            r0 = 1;
            r10.<init>(r0);
        L_0x0029:
            if (r11 == 0) goto L_0x0034;
        L_0x002b:
            r0 = 100;
            r0 = java.lang.String.valueOf(r0);
            r10.putParcelable(r0, r11);
        L_0x0034:
            r0 = r7.mEventHandler;
            r1 = 3;
            r2 = 2;
            r3 = r9;
            r5 = r10;
            r6 = r4;
            android.media.RemoteController.sendMsg(r0, r1, r2, r3, r4, r5, r6);
            goto L_0x000b;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.RemoteController.RcDisplay.setAllMetadata(int, android.os.Bundle, android.graphics.Bitmap):void");
        }
    }

    private class TopTransportSessionListener implements OnActiveSessionsChangedListener {
        private TopTransportSessionListener() {
        }

        public void onActiveSessionsChanged(List<MediaController> controllers) {
            int size = controllers.size();
            for (int i = 0; i < size; i++) {
                MediaController controller = (MediaController) controllers.get(i);
                if ((2 & controller.getFlags()) != 0) {
                    RemoteController.this.updateController(controller);
                    return;
                }
            }
            RemoteController.this.updateController(null);
        }
    }

    public RemoteController(Context context, OnClientUpdateListener updateListener) throws IllegalArgumentException {
        this(context, updateListener, null);
    }

    public RemoteController(Context context, OnClientUpdateListener updateListener, Looper looper) throws IllegalArgumentException {
        this.mSessionCb = new MediaControllerCallback();
        this.mClientGenerationIdCurrent = 0;
        this.mIsRegistered = false;
        this.mArtworkWidth = -1;
        this.mArtworkHeight = -1;
        this.mEnabled = true;
        if (context == null) {
            throw new IllegalArgumentException("Invalid null Context");
        } else if (updateListener == null) {
            throw new IllegalArgumentException("Invalid null OnClientUpdateListener");
        } else {
            if (looper != null) {
                this.mEventHandler = new EventHandler(this, looper);
            } else {
                Looper l = Looper.myLooper();
                if (l != null) {
                    this.mEventHandler = new EventHandler(this, l);
                } else {
                    throw new IllegalArgumentException("Calling thread not associated with a looper");
                }
            }
            this.mOnClientUpdateListener = updateListener;
            this.mContext = context;
            this.mRcd = new RcDisplay(this);
            this.mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            this.mSessionManager = (MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE);
            this.mSessionListener = new TopTransportSessionListener();
            if (ActivityManager.isLowRamDeviceStatic()) {
                this.mMaxBitmapDimension = 512;
                return;
            }
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            this.mMaxBitmapDimension = Math.max(dm.widthPixels, dm.heightPixels);
        }
    }

    public String getRemoteControlClientPackageName() {
        String packageName;
        synchronized (mInfoLock) {
            packageName = this.mCurrentSession != null ? this.mCurrentSession.getPackageName() : null;
        }
        return packageName;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long getEstimatedMediaPosition() {
        /*
        r5 = this;
        r4 = mInfoLock;
        monitor-enter(r4);
        r1 = r5.mCurrentSession;	 Catch:{ all -> 0x0019 }
        if (r1 == 0) goto L_0x0015;
    L_0x0007:
        r1 = r5.mCurrentSession;	 Catch:{ all -> 0x0019 }
        r0 = r1.getPlaybackState();	 Catch:{ all -> 0x0019 }
        if (r0 == 0) goto L_0x0015;
    L_0x000f:
        r2 = r0.getPosition();	 Catch:{ all -> 0x0019 }
        monitor-exit(r4);	 Catch:{ all -> 0x0019 }
    L_0x0014:
        return r2;
    L_0x0015:
        monitor-exit(r4);	 Catch:{ all -> 0x0019 }
        r2 = -1;
        goto L_0x0014;
    L_0x0019:
        r1 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0019 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.RemoteController.getEstimatedMediaPosition():long");
    }

    public boolean sendMediaKeyEvent(KeyEvent keyEvent) throws IllegalArgumentException {
        if (KeyEvent.isMediaKey(keyEvent.getKeyCode())) {
            boolean dispatchMediaButtonEvent;
            synchronized (mInfoLock) {
                if (this.mCurrentSession != null) {
                    dispatchMediaButtonEvent = this.mCurrentSession.dispatchMediaButtonEvent(keyEvent);
                } else {
                    dispatchMediaButtonEvent = false;
                }
            }
            return dispatchMediaButtonEvent;
        }
        throw new IllegalArgumentException("not a media key event");
    }

    public boolean seekTo(long timeMs) throws IllegalArgumentException {
        Log.e(TAG, "seekTo() in RemoteController");
        if (!this.mEnabled) {
            Log.e(TAG, "Cannot use seekTo() from a disabled RemoteController");
            return false;
        } else if (timeMs < 0) {
            throw new IllegalArgumentException("illegal negative time value");
        } else {
            synchronized (mInfoLock) {
                if (this.mCurrentSession != null) {
                    this.mCurrentSession.getTransportControls().seekTo(timeMs);
                }
            }
            return true;
        }
    }

    public boolean sendCustomAction(String action, Bundle args) {
        if (this.mEnabled) {
            synchronized (mInfoLock) {
                if (this.mCurrentSession != null) {
                    this.mCurrentSession.getTransportControls().sendCustomAction(action, args);
                }
            }
            return true;
        }
        Log.e(TAG, "Cannot use sendCustomAction() from a disabled RemoteController");
        return false;
    }

    public boolean setArtworkConfiguration(boolean wantBitmap, int width, int height) throws IllegalArgumentException {
        synchronized (mInfoLock) {
            if (!wantBitmap) {
                this.mArtworkWidth = -1;
                this.mArtworkHeight = -1;
            } else if (width <= 0 || height <= 0) {
                throw new IllegalArgumentException("Invalid dimensions");
            } else {
                if (width > this.mMaxBitmapDimension) {
                    width = this.mMaxBitmapDimension;
                }
                if (height > this.mMaxBitmapDimension) {
                    height = this.mMaxBitmapDimension;
                }
                this.mArtworkWidth = width;
                this.mArtworkHeight = height;
            }
        }
        return true;
    }

    public boolean setArtworkConfiguration(int width, int height) throws IllegalArgumentException {
        return setArtworkConfiguration(true, width, height);
    }

    public boolean clearArtworkConfiguration() {
        return setArtworkConfiguration(false, -1, -1);
    }

    public boolean setSynchronizationMode(int sync) throws IllegalArgumentException {
        boolean z = false;
        if (sync != 0 && sync != 1) {
            throw new IllegalArgumentException("Unknown synchronization mode " + sync);
        } else if (this.mIsRegistered) {
            AudioManager audioManager = this.mAudioManager;
            IRemoteControlDisplay iRemoteControlDisplay = this.mRcd;
            if (1 == sync) {
                z = true;
            }
            audioManager.remoteControlDisplayWantsPlaybackPositionSync(iRemoteControlDisplay, z);
            return true;
        } else {
            Log.e(TAG, "Cannot set synchronization mode on an unregistered RemoteController");
            return false;
        }
    }

    public MetadataEditor editMetadata() {
        MetadataEditor editor = new MetadataEditor();
        editor.mEditorMetadata = new Bundle();
        editor.mEditorArtwork = null;
        editor.mMetadataChanged = true;
        editor.mArtworkChanged = true;
        editor.mEditableKeys = 0;
        return editor;
    }

    void startListeningToSessions() {
        ComponentName listenerComponent = new ComponentName(this.mContext, this.mOnClientUpdateListener.getClass());
        Handler handler = null;
        if (Looper.myLooper() == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        this.mSessionManager.addOnActiveSessionsChangedListener(this.mSessionListener, listenerComponent, UserHandle.myUserId(), handler);
        this.mSessionListener.onActiveSessionsChanged(this.mSessionManager.getActiveSessions(listenerComponent));
    }

    void stopListeningToSessions() {
        this.mSessionManager.removeOnActiveSessionsChangedListener(this.mSessionListener);
    }

    private static void sendMsg(Handler handler, int msg, int existingMsgPolicy, int arg1, int arg2, Object obj, int delayMs) {
        if (handler == null) {
            Log.e(TAG, "null event handler, will not deliver message " + msg);
            return;
        }
        if (existingMsgPolicy == 0) {
            handler.removeMessages(msg);
        } else if (existingMsgPolicy == 1 && handler.hasMessages(msg)) {
            return;
        }
        handler.sendMessageDelayed(handler.obtainMessage(msg, arg1, arg2, obj), (long) delayMs);
    }

    private void onNewPendingIntent(int genId, PendingIntent pi) {
        synchronized (mGenLock) {
            if (this.mClientGenerationIdCurrent != genId) {
                return;
            }
            synchronized (mInfoLock) {
                this.mClientPendingIntentCurrent = pi;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onNewPlaybackInfo(int r8, android.media.RemoteController.PlaybackInfo r9) {
        /*
        r7 = this;
        r2 = mGenLock;
        monitor-enter(r2);
        r1 = r7.mClientGenerationIdCurrent;	 Catch:{ all -> 0x0025 }
        if (r1 == r8) goto L_0x0009;
    L_0x0007:
        monitor-exit(r2);	 Catch:{ all -> 0x0025 }
    L_0x0008:
        return;
    L_0x0009:
        monitor-exit(r2);	 Catch:{ all -> 0x0025 }
        r2 = mInfoLock;
        monitor-enter(r2);
        r0 = r7.mOnClientUpdateListener;	 Catch:{ all -> 0x0028 }
        r7.mLastPlaybackInfo = r9;	 Catch:{ all -> 0x0028 }
        monitor-exit(r2);	 Catch:{ all -> 0x0028 }
        if (r0 == 0) goto L_0x0008;
    L_0x0014:
        r2 = r9.mCurrentPosMs;
        r4 = -9216204211029966080; // 0x8019771980198300 float:-2.342881E-39 double:-3.541376495412184E-308;
        r1 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1));
        if (r1 != 0) goto L_0x002b;
    L_0x001f:
        r1 = r9.mState;
        r0.onClientPlaybackStateUpdate(r1);
        goto L_0x0008;
    L_0x0025:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0025 }
        throw r1;
    L_0x0028:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0028 }
        throw r1;
    L_0x002b:
        r1 = r9.mState;
        r2 = r9.mStateChangeTimeMs;
        r4 = r9.mCurrentPosMs;
        r6 = r9.mSpeed;
        r0.onClientPlaybackStateUpdate(r1, r2, r4, r6);
        goto L_0x0008;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.RemoteController.onNewPlaybackInfo(int, android.media.RemoteController$PlaybackInfo):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onNewTransportInfo(int r4, int r5) {
        /*
        r3 = this;
        r2 = mGenLock;
        monitor-enter(r2);
        r1 = r3.mClientGenerationIdCurrent;	 Catch:{ all -> 0x0016 }
        if (r1 == r4) goto L_0x0009;
    L_0x0007:
        monitor-exit(r2);	 Catch:{ all -> 0x0016 }
    L_0x0008:
        return;
    L_0x0009:
        monitor-exit(r2);	 Catch:{ all -> 0x0016 }
        r2 = mInfoLock;
        monitor-enter(r2);
        r0 = r3.mOnClientUpdateListener;	 Catch:{ all -> 0x0019 }
        monitor-exit(r2);	 Catch:{ all -> 0x0019 }
        if (r0 == 0) goto L_0x0008;
    L_0x0012:
        r0.onClientTransportControlUpdate(r5);
        goto L_0x0008;
    L_0x0016:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0016 }
        throw r1;
    L_0x0019:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0019 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.RemoteController.onNewTransportInfo(int, int):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onNewMetadata(int r11, android.os.Bundle r12) {
        /*
        r10 = this;
        r8 = 0;
        r6 = 536870911; // 0x1fffffff float:1.0842021E-19 double:2.652494734E-315;
        r5 = mGenLock;
        monitor-enter(r5);
        r4 = r10.mClientGenerationIdCurrent;	 Catch:{ all -> 0x0061 }
        if (r4 == r11) goto L_0x000e;
    L_0x000c:
        monitor-exit(r5);	 Catch:{ all -> 0x0061 }
    L_0x000d:
        return;
    L_0x000e:
        monitor-exit(r5);	 Catch:{ all -> 0x0061 }
        r4 = java.lang.String.valueOf(r6);
        r0 = r12.getLong(r4, r8);
        r4 = (r0 > r8 ? 1 : (r0 == r8 ? 0 : -1));
        if (r4 == 0) goto L_0x0022;
    L_0x001b:
        r4 = java.lang.String.valueOf(r6);
        r12.remove(r4);
    L_0x0022:
        r5 = mInfoLock;
        monitor-enter(r5);
        r2 = r10.mOnClientUpdateListener;	 Catch:{ all -> 0x006c }
        r4 = r10.mMetadataEditor;	 Catch:{ all -> 0x006c }
        if (r4 == 0) goto L_0x0064;
    L_0x002b:
        r4 = r10.mMetadataEditor;	 Catch:{ all -> 0x006c }
        r4 = r4.mEditorMetadata;	 Catch:{ all -> 0x006c }
        if (r4 == 0) goto L_0x0064;
    L_0x0031:
        r4 = r10.mMetadataEditor;	 Catch:{ all -> 0x006c }
        r4 = r4.mEditorMetadata;	 Catch:{ all -> 0x006c }
        if (r4 == r12) goto L_0x003e;
    L_0x0037:
        r4 = r10.mMetadataEditor;	 Catch:{ all -> 0x006c }
        r4 = r4.mEditorMetadata;	 Catch:{ all -> 0x006c }
        r4.putAll(r12);	 Catch:{ all -> 0x006c }
    L_0x003e:
        r6 = r10.mMetadataEditor;	 Catch:{ all -> 0x006c }
        r7 = 100;
        r4 = 100;
        r4 = java.lang.String.valueOf(r4);	 Catch:{ all -> 0x006c }
        r4 = r12.getParcelable(r4);	 Catch:{ all -> 0x006c }
        r4 = (android.graphics.Bitmap) r4;	 Catch:{ all -> 0x006c }
        r6.putBitmap(r7, r4);	 Catch:{ all -> 0x006c }
        r4 = r10.mMetadataEditor;	 Catch:{ all -> 0x006c }
        r6 = 100;
        r4.cleanupBitmapFromBundle(r6);	 Catch:{ all -> 0x006c }
    L_0x0058:
        r3 = r10.mMetadataEditor;	 Catch:{ all -> 0x006c }
        monitor-exit(r5);	 Catch:{ all -> 0x006c }
        if (r2 == 0) goto L_0x000d;
    L_0x005d:
        r2.onClientMetadataUpdate(r3);
        goto L_0x000d;
    L_0x0061:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x0061 }
        throw r4;
    L_0x0064:
        r4 = new android.media.RemoteController$MetadataEditor;	 Catch:{ all -> 0x006c }
        r4.<init>(r12, r0);	 Catch:{ all -> 0x006c }
        r10.mMetadataEditor = r4;	 Catch:{ all -> 0x006c }
        goto L_0x0058;
    L_0x006c:
        r4 = move-exception;
        monitor-exit(r5);	 Catch:{ all -> 0x006c }
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.RemoteController.onNewMetadata(int, android.os.Bundle):void");
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void onClientChange(int r4, boolean r5) {
        /*
        r3 = this;
        r2 = mGenLock;
        monitor-enter(r2);
        r1 = r3.mClientGenerationIdCurrent;	 Catch:{ all -> 0x0019 }
        if (r1 == r4) goto L_0x0009;
    L_0x0007:
        monitor-exit(r2);	 Catch:{ all -> 0x0019 }
    L_0x0008:
        return;
    L_0x0009:
        monitor-exit(r2);	 Catch:{ all -> 0x0019 }
        r2 = mInfoLock;
        monitor-enter(r2);
        r0 = r3.mOnClientUpdateListener;	 Catch:{ all -> 0x001c }
        r1 = 0;
        r3.mMetadataEditor = r1;	 Catch:{ all -> 0x001c }
        monitor-exit(r2);	 Catch:{ all -> 0x001c }
        if (r0 == 0) goto L_0x0008;
    L_0x0015:
        r0.onClientChange(r5);
        goto L_0x0008;
    L_0x0019:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0019 }
        throw r1;
    L_0x001c:
        r1 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x001c }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.RemoteController.onClientChange(int, boolean):void");
    }

    private void onDisplayEnable(boolean enabled) {
        synchronized (mInfoLock) {
            this.mEnabled = enabled;
            OnClientUpdateListener l = this.mOnClientUpdateListener;
        }
        if (!enabled) {
            int genId;
            synchronized (mGenLock) {
                genId = this.mClientGenerationIdCurrent;
            }
            sendMsg(this.mEventHandler, 1, 0, genId, 0, new PlaybackInfo(1, SystemClock.elapsedRealtime(), 0, 0.0f), 0);
            sendMsg(this.mEventHandler, 2, 0, genId, 0, null, 0);
            Bundle metadata = new Bundle(3);
            metadata.putString(String.valueOf(7), ProxyInfo.LOCAL_EXCL_LIST);
            metadata.putString(String.valueOf(2), ProxyInfo.LOCAL_EXCL_LIST);
            metadata.putLong(String.valueOf(9), 0);
            sendMsg(this.mEventHandler, 3, 2, genId, 0, metadata, 0);
        }
    }

    private void updateController(MediaController controller) {
        synchronized (mInfoLock) {
            if (controller == null) {
                if (this.mCurrentSession != null) {
                    this.mCurrentSession.unregisterCallback(this.mSessionCb);
                    this.mCurrentSession = null;
                    sendMsg(this.mEventHandler, 4, 0, 0, 1, null, 0);
                }
            } else if (this.mCurrentSession == null || !controller.getSessionToken().equals(this.mCurrentSession.getSessionToken())) {
                if (this.mCurrentSession != null) {
                    this.mCurrentSession.unregisterCallback(this.mSessionCb);
                }
                sendMsg(this.mEventHandler, 4, 0, 0, 0, null, 0);
                this.mCurrentSession = controller;
                this.mCurrentSession.registerCallback(this.mSessionCb, this.mEventHandler);
                sendMsg(this.mEventHandler, 6, 0, 0, 0, controller.getPlaybackState(), 0);
                sendMsg(this.mEventHandler, 7, 0, 0, 0, controller.getMetadata(), 0);
            }
        }
    }

    private void onNewPlaybackState(PlaybackState state) {
        synchronized (mInfoLock) {
            OnClientUpdateListener l = this.mOnClientUpdateListener;
        }
        if (l != null) {
            int playstate = state == null ? 0 : PlaybackState.getRccStateFromState(state.getState());
            if (state == null || state.getPosition() == -1) {
                l.onClientPlaybackStateUpdate(playstate);
            } else {
                l.onClientPlaybackStateUpdate(playstate, state.getLastPositionUpdateTime(), state.getPosition(), state.getPlaybackSpeed());
            }
            if (state != null) {
                l.onClientTransportControlUpdate(PlaybackState.getRccControlFlagsFromActions(state.getActions()));
            }
        }
    }

    private void onNewMediaMetadata(MediaMetadata metadata) {
        if (metadata != null) {
            OnClientUpdateListener l;
            MetadataEditor metadataEditor;
            synchronized (mInfoLock) {
                l = this.mOnClientUpdateListener;
                boolean canRate = (this.mCurrentSession == null || this.mCurrentSession.getRatingType() == 0) ? false : true;
                this.mMetadataEditor = new MetadataEditor(MediaSessionLegacyHelper.getOldMetadata(metadata, this.mArtworkWidth, this.mArtworkHeight), canRate ? 268435457 : 0);
                metadataEditor = this.mMetadataEditor;
            }
            if (l != null) {
                l.onClientMetadataUpdate(metadataEditor);
            }
        }
    }

    void setIsRegistered(boolean registered) {
        synchronized (mInfoLock) {
            this.mIsRegistered = registered;
        }
    }

    RcDisplay getRcDisplay() {
        return this.mRcd;
    }

    int[] getArtworkSize() {
        int[] size;
        synchronized (mInfoLock) {
            size = new int[]{this.mArtworkWidth, this.mArtworkHeight};
        }
        return size;
    }

    OnClientUpdateListener getUpdateListener() {
        return this.mOnClientUpdateListener;
    }
}
