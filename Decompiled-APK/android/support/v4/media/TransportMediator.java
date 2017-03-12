package android.support.v4.media;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Build.VERSION;
import android.support.v4.view.KeyEventCompat;
import android.view.KeyEvent;
import android.view.KeyEvent.Callback;
import android.view.View;
import com.google.android.gms.location.places.Place;
import java.util.ArrayList;
import org.bouncycastle.asn1.eac.EACTags;

public class TransportMediator extends TransportController {
    public static final int FLAG_KEY_MEDIA_FAST_FORWARD = 64;
    public static final int FLAG_KEY_MEDIA_NEXT = 128;
    public static final int FLAG_KEY_MEDIA_PAUSE = 16;
    public static final int FLAG_KEY_MEDIA_PLAY = 4;
    public static final int FLAG_KEY_MEDIA_PLAY_PAUSE = 8;
    public static final int FLAG_KEY_MEDIA_PREVIOUS = 1;
    public static final int FLAG_KEY_MEDIA_REWIND = 2;
    public static final int FLAG_KEY_MEDIA_STOP = 32;
    public static final int KEYCODE_MEDIA_PAUSE = 127;
    public static final int KEYCODE_MEDIA_PLAY = 126;
    public static final int KEYCODE_MEDIA_RECORD = 130;
    final AudioManager mAudioManager;
    final TransportPerformer mCallbacks;
    final Context mContext;
    final TransportMediatorJellybeanMR2 mController;
    final Object mDispatcherState;
    final Callback mKeyEventCallback;
    final ArrayList<TransportStateListener> mListeners;
    final TransportMediatorCallback mTransportKeyCallback;
    final View mView;

    /* renamed from: android.support.v4.media.TransportMediator.1 */
    class C00151 implements TransportMediatorCallback {
        C00151() {
        }

        public void handleKey(KeyEvent keyEvent) {
            keyEvent.dispatch(TransportMediator.this.mKeyEventCallback);
        }

        public void handleAudioFocusChange(int i) {
            TransportMediator.this.mCallbacks.onAudioFocusChange(i);
        }

        public long getPlaybackPosition() {
            return TransportMediator.this.mCallbacks.onGetCurrentPosition();
        }

        public void playbackPositionUpdate(long j) {
            TransportMediator.this.mCallbacks.onSeekTo(j);
        }
    }

    /* renamed from: android.support.v4.media.TransportMediator.2 */
    class C00162 implements Callback {
        C00162() {
        }

        public boolean onKeyDown(int i, KeyEvent keyEvent) {
            return TransportMediator.isMediaKey(i) ? TransportMediator.this.mCallbacks.onMediaButtonDown(i, keyEvent) : false;
        }

        public boolean onKeyLongPress(int i, KeyEvent keyEvent) {
            return false;
        }

        public boolean onKeyUp(int i, KeyEvent keyEvent) {
            return TransportMediator.isMediaKey(i) ? TransportMediator.this.mCallbacks.onMediaButtonUp(i, keyEvent) : false;
        }

        public boolean onKeyMultiple(int i, int i2, KeyEvent keyEvent) {
            return false;
        }
    }

    static boolean isMediaKey(int i) {
        switch (i) {
            case EACTags.APPLICATION_IDENTIFIER /*79*/:
            case Place.TYPE_SPA /*85*/:
            case EACTags.TRACK1_APPLICATION /*86*/:
            case EACTags.TRACK2_APPLICATION /*87*/:
            case EACTags.TRACK3_APPLICATION /*88*/:
            case EACTags.CARD_EXPIRATION_DATA /*89*/:
            case EACTags.PRIMARY_ACCOUNT_NUMBER /*90*/:
            case EACTags.NAME /*91*/:
            case KEYCODE_MEDIA_PLAY /*126*/:
            case KEYCODE_MEDIA_PAUSE /*127*/:
            case KEYCODE_MEDIA_RECORD /*130*/:
                return true;
            default:
                return false;
        }
    }

    public TransportMediator(Activity activity, TransportPerformer transportPerformer) {
        this(activity, null, transportPerformer);
    }

    public TransportMediator(View view, TransportPerformer transportPerformer) {
        this(null, view, transportPerformer);
    }

    private TransportMediator(Activity activity, View view, TransportPerformer transportPerformer) {
        this.mListeners = new ArrayList();
        this.mTransportKeyCallback = new C00151();
        this.mKeyEventCallback = new C00162();
        this.mContext = activity != null ? activity : view.getContext();
        this.mCallbacks = transportPerformer;
        this.mAudioManager = (AudioManager) this.mContext.getSystemService("audio");
        if (activity != null) {
            view = activity.getWindow().getDecorView();
        }
        this.mView = view;
        this.mDispatcherState = KeyEventCompat.getKeyDispatcherState(this.mView);
        if (VERSION.SDK_INT >= 18) {
            this.mController = new TransportMediatorJellybeanMR2(this.mContext, this.mAudioManager, this.mView, this.mTransportKeyCallback);
        } else {
            this.mController = null;
        }
    }

    public Object getRemoteControlClient() {
        return this.mController != null ? this.mController.getRemoteControlClient() : null;
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        return KeyEventCompat.dispatch(keyEvent, this.mKeyEventCallback, this.mDispatcherState, this);
    }

    public void registerStateListener(TransportStateListener transportStateListener) {
        this.mListeners.add(transportStateListener);
    }

    public void unregisterStateListener(TransportStateListener transportStateListener) {
        this.mListeners.remove(transportStateListener);
    }

    private TransportStateListener[] getListeners() {
        if (this.mListeners.size() <= 0) {
            return null;
        }
        TransportStateListener[] transportStateListenerArr = new TransportStateListener[this.mListeners.size()];
        this.mListeners.toArray(transportStateListenerArr);
        return transportStateListenerArr;
    }

    private void reportPlayingChanged() {
        TransportStateListener[] listeners = getListeners();
        if (listeners != null) {
            int length = listeners.length;
            for (int i = 0; i < length; i += FLAG_KEY_MEDIA_PREVIOUS) {
                listeners[i].onPlayingChanged(this);
            }
        }
    }

    private void reportTransportControlsChanged() {
        TransportStateListener[] listeners = getListeners();
        if (listeners != null) {
            int length = listeners.length;
            for (int i = 0; i < length; i += FLAG_KEY_MEDIA_PREVIOUS) {
                listeners[i].onTransportControlsChanged(this);
            }
        }
    }

    private void pushControllerState() {
        if (this.mController != null) {
            this.mController.refreshState(this.mCallbacks.onIsPlaying(), this.mCallbacks.onGetCurrentPosition(), this.mCallbacks.onGetTransportControlFlags());
        }
    }

    public void refreshState() {
        pushControllerState();
        reportPlayingChanged();
        reportTransportControlsChanged();
    }

    public void startPlaying() {
        if (this.mController != null) {
            this.mController.startPlaying();
        }
        this.mCallbacks.onStart();
        pushControllerState();
        reportPlayingChanged();
    }

    public void pausePlaying() {
        if (this.mController != null) {
            this.mController.pausePlaying();
        }
        this.mCallbacks.onPause();
        pushControllerState();
        reportPlayingChanged();
    }

    public void stopPlaying() {
        if (this.mController != null) {
            this.mController.stopPlaying();
        }
        this.mCallbacks.onStop();
        pushControllerState();
        reportPlayingChanged();
    }

    public long getDuration() {
        return this.mCallbacks.onGetDuration();
    }

    public long getCurrentPosition() {
        return this.mCallbacks.onGetCurrentPosition();
    }

    public void seekTo(long j) {
        this.mCallbacks.onSeekTo(j);
    }

    public boolean isPlaying() {
        return this.mCallbacks.onIsPlaying();
    }

    public int getBufferPercentage() {
        return this.mCallbacks.onGetBufferPercentage();
    }

    public int getTransportControlFlags() {
        return this.mCallbacks.onGetTransportControlFlags();
    }

    public void destroy() {
        this.mController.destroy();
    }
}
