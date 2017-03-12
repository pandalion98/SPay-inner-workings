package android.media.session;

import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaMetadataEditor;
import android.media.Rating;
import android.media.SamsungAudioManager.AudioLog;
import android.media.session.MediaSession.Callback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import com.samsung.android.cover.CoverState;
import com.samsung.android.cover.ICoverManager;
import com.samsung.android.cover.ICoverManager.Stub;
import com.samsung.android.feature.FloatingFeature;

public class MediaSessionLegacyHelper {
    private static final boolean DEBUG = AudioLog.isLoggable(TAG, 0);
    private static final String TAG = "MediaSessionHelper";
    private static boolean mAudioCoreVolumekeyLoggingEnabled = false;
    private static MediaSessionLegacyHelper sInstance;
    private static final Object sLock = new Object();
    private Context mContext;
    private ICoverManager mCoverManager;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private KeyguardManager mKeyguardManager;
    private PowerManager mPowerManager;
    private MediaSessionManager mSessionManager;
    private ArrayMap<PendingIntent, SessionHolder> mSessions = new ArrayMap();

    private static final class MediaButtonListener extends Callback {
        private final Context mContext;
        private final PendingIntent mPendingIntent;

        public MediaButtonListener(PendingIntent pi, Context context) {
            this.mPendingIntent = pi;
            this.mContext = context;
        }

        public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
            MediaSessionLegacyHelper.sendKeyEvent(this.mPendingIntent, this.mContext, mediaButtonIntent);
            return true;
        }

        public void onPlay() {
            sendKeyEvent(126);
        }

        public void onPause() {
            sendKeyEvent(127);
        }

        public void onSkipToNext() {
            sendKeyEvent(87);
        }

        public void onSkipToPrevious() {
            sendKeyEvent(88);
        }

        public void onFastForward() {
            sendKeyEvent(90);
        }

        public void onRewind() {
            sendKeyEvent(89);
        }

        public void onStop() {
            sendKeyEvent(86);
        }

        private void sendKeyEvent(int keyCode) {
            Parcelable ke = new KeyEvent(0, keyCode);
            Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            intent.addFlags(268435456);
            intent.putExtra(Intent.EXTRA_KEY_EVENT, ke);
            MediaSessionLegacyHelper.sendKeyEvent(this.mPendingIntent, this.mContext, intent);
            intent.putExtra(Intent.EXTRA_KEY_EVENT, new KeyEvent(1, keyCode));
            MediaSessionLegacyHelper.sendKeyEvent(this.mPendingIntent, this.mContext, intent);
            if (MediaSessionLegacyHelper.DEBUG) {
                Log.d(MediaSessionLegacyHelper.TAG, "Sent " + keyCode + " to pending intent " + this.mPendingIntent);
            }
        }
    }

    private class SessionHolder {
        public SessionCallback mCb;
        public int mFlags;
        public MediaButtonListener mMediaButtonListener;
        public final PendingIntent mPi;
        public Callback mRccListener;
        public final MediaSession mSession;

        private class SessionCallback extends Callback {
            private SessionCallback() {
            }

            public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onMediaButtonEvent(mediaButtonIntent);
                }
                return true;
            }

            public void onPlay() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onPlay();
                }
            }

            public void onPause() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onPause();
                }
            }

            public void onSkipToNext() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onSkipToNext();
                }
            }

            public void onSkipToPrevious() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onSkipToPrevious();
                }
            }

            public void onFastForward() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onFastForward();
                }
            }

            public void onRewind() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onRewind();
                }
            }

            public void onStop() {
                if (SessionHolder.this.mMediaButtonListener != null) {
                    SessionHolder.this.mMediaButtonListener.onStop();
                }
            }

            public void onSeekTo(long pos) {
                if (SessionHolder.this.mRccListener != null) {
                    SessionHolder.this.mRccListener.onSeekTo(pos);
                }
            }

            public void onSetRating(Rating rating) {
                if (SessionHolder.this.mRccListener != null) {
                    SessionHolder.this.mRccListener.onSetRating(rating);
                }
            }
        }

        public SessionHolder(MediaSession session, PendingIntent pi) {
            this.mSession = session;
            this.mPi = pi;
        }

        public void update() {
            if (this.mMediaButtonListener == null && this.mRccListener == null) {
                this.mSession.setCallback(null);
                this.mSession.release();
                this.mCb = null;
                MediaSessionLegacyHelper.this.mSessions.remove(this.mPi);
            } else if (this.mCb == null) {
                this.mCb = new SessionCallback();
                this.mSession.setCallback(this.mCb, new Handler(Looper.getMainLooper()));
            }
        }
    }

    private MediaSessionLegacyHelper(Context context) {
        this.mContext = context;
        this.mSessionManager = (MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE);
        this.mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        this.mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        this.mCoverManager = Stub.asInterface(ServiceManager.getService("cover"));
        mAudioCoreVolumekeyLoggingEnabled = FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_CONTEXTSERVICE_ENABLE_SURVEY_MODE");
    }

    public static MediaSessionLegacyHelper getHelper(Context context) {
        synchronized (sLock) {
            if (sInstance == null) {
                sInstance = new MediaSessionLegacyHelper(context.getApplicationContext());
            }
        }
        return sInstance;
    }

    public static Bundle getOldMetadata(MediaMetadata metadata, int artworkWidth, int artworkHeight) {
        boolean includeArtwork;
        if (artworkWidth == -1 || artworkHeight == -1) {
            includeArtwork = false;
        } else {
            includeArtwork = true;
        }
        Bundle oldMetadata = new Bundle();
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_ALBUM)) {
            oldMetadata.putString(String.valueOf(1), metadata.getString(MediaMetadata.METADATA_KEY_ALBUM));
        }
        if (includeArtwork && metadata.containsKey(MediaMetadata.METADATA_KEY_ART)) {
            oldMetadata.putParcelable(String.valueOf(100), scaleBitmapIfTooBig(metadata.getBitmap(MediaMetadata.METADATA_KEY_ART), artworkWidth, artworkHeight));
        } else if (includeArtwork && metadata.containsKey(MediaMetadata.METADATA_KEY_ALBUM_ART)) {
            oldMetadata.putParcelable(String.valueOf(100), scaleBitmapIfTooBig(metadata.getBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART), artworkWidth, artworkHeight));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_ALBUM_ARTIST)) {
            oldMetadata.putString(String.valueOf(13), metadata.getString(MediaMetadata.METADATA_KEY_ALBUM_ARTIST));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_ARTIST)) {
            oldMetadata.putString(String.valueOf(2), metadata.getString(MediaMetadata.METADATA_KEY_ARTIST));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_AUTHOR)) {
            oldMetadata.putString(String.valueOf(3), metadata.getString(MediaMetadata.METADATA_KEY_AUTHOR));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_COMPILATION)) {
            oldMetadata.putString(String.valueOf(15), metadata.getString(MediaMetadata.METADATA_KEY_COMPILATION));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_COMPOSER)) {
            oldMetadata.putString(String.valueOf(4), metadata.getString(MediaMetadata.METADATA_KEY_COMPOSER));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_DATE)) {
            oldMetadata.putString(String.valueOf(5), metadata.getString(MediaMetadata.METADATA_KEY_DATE));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_DISC_NUMBER)) {
            oldMetadata.putLong(String.valueOf(14), metadata.getLong(MediaMetadata.METADATA_KEY_DISC_NUMBER));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_DURATION)) {
            oldMetadata.putLong(String.valueOf(9), metadata.getLong(MediaMetadata.METADATA_KEY_DURATION));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_GENRE)) {
            oldMetadata.putString(String.valueOf(6), metadata.getString(MediaMetadata.METADATA_KEY_GENRE));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_NUM_TRACKS)) {
            oldMetadata.putLong(String.valueOf(10), metadata.getLong(MediaMetadata.METADATA_KEY_NUM_TRACKS));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_RATING)) {
            oldMetadata.putParcelable(String.valueOf(101), metadata.getRating(MediaMetadata.METADATA_KEY_RATING));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_USER_RATING)) {
            oldMetadata.putParcelable(String.valueOf(MediaMetadataEditor.RATING_KEY_BY_USER), metadata.getRating(MediaMetadata.METADATA_KEY_USER_RATING));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_TITLE)) {
            oldMetadata.putString(String.valueOf(7), metadata.getString(MediaMetadata.METADATA_KEY_TITLE));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_TRACK_NUMBER)) {
            oldMetadata.putLong(String.valueOf(0), metadata.getLong(MediaMetadata.METADATA_KEY_TRACK_NUMBER));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_WRITER)) {
            oldMetadata.putString(String.valueOf(11), metadata.getString(MediaMetadata.METADATA_KEY_WRITER));
        }
        if (metadata.containsKey(MediaMetadata.METADATA_KEY_YEAR)) {
            oldMetadata.putString(String.valueOf(8), metadata.getString(MediaMetadata.METADATA_KEY_YEAR));
        }
        return oldMetadata;
    }

    public MediaSession getSession(PendingIntent pi) {
        SessionHolder holder = (SessionHolder) this.mSessions.get(pi);
        return holder == null ? null : holder.mSession;
    }

    public void sendMediaButtonEvent(KeyEvent keyEvent, boolean needWakeLock) {
        if (keyEvent == null) {
            Log.w(TAG, "Tried to send a null key event. Ignoring.");
            return;
        }
        this.mSessionManager.dispatchMediaKeyEvent(keyEvent, needWakeLock);
        if (DEBUG) {
            Log.d(TAG, "dispatched media key " + keyEvent);
        }
    }

    private boolean isCoverOpen() {
        if (this.mCoverManager == null) {
            return true;
        }
        try {
            CoverState state = this.mCoverManager.getCoverState();
            if (state != null) {
                return state.getSwitchState();
            }
            return true;
        } catch (RemoteException e) {
            Log.e(TAG, "Error while get SView coverstate", e);
            return true;
        }
    }

    public void sendVolumeKeyEvent(KeyEvent keyEvent, boolean musicOnly) {
        if (keyEvent == null) {
            Log.w(TAG, "Tried to send a null key event. Ignoring.");
            return;
        }
        boolean down;
        boolean up;
        if (keyEvent.getAction() == 0) {
            down = true;
        } else {
            down = false;
        }
        if (keyEvent.getAction() == 1) {
            up = true;
        } else {
            up = false;
        }
        int direction = 0;
        boolean isMute = false;
        Log.d(TAG, "VolumeKeyEvent down:" + down + ", musicOnly:" + musicOnly);
        if (mAudioCoreVolumekeyLoggingEnabled && up) {
            volumeKeyLogging();
        }
        switch (keyEvent.getKeyCode()) {
            case 24:
                direction = 1;
                break;
            case 25:
                direction = -1;
                break;
            case 164:
                isMute = true;
                break;
        }
        if (down || up) {
            int flags = 4096;
            if (down) {
                flags = 4096 | 536870912;
            }
            if (musicOnly || (this.mKeyguardManager != null && this.mKeyguardManager.isKeyguardLocked() && isCoverOpen())) {
                flags |= 512;
            } else if (up) {
                flags |= 20;
            } else {
                flags |= 17;
            }
            if (direction != 0) {
                if (up) {
                    direction = 0;
                }
                this.mSessionManager.dispatchAdjustVolume(Integer.MIN_VALUE, direction, flags);
            } else if (isMute && down && keyEvent.getRepeatCount() == 0) {
                this.mSessionManager.dispatchAdjustVolume(Integer.MIN_VALUE, 101, flags);
            }
        }
    }

    public void sendAdjustVolumeBy(int suggestedStream, int delta, int flags) {
        this.mSessionManager.dispatchAdjustVolume(suggestedStream, delta, flags);
        if (mAudioCoreVolumekeyLoggingEnabled && flags == 4116) {
            volumeKeyLogging();
        }
        if (DEBUG) {
            Log.d(TAG, "dispatched volume adjustment");
        }
    }

    public boolean isGlobalPriorityActive() {
        return this.mSessionManager.isGlobalPriorityActive();
    }

    public void addRccListener(PendingIntent pi, Callback listener) {
        if (pi == null) {
            Log.w(TAG, "Pending intent was null, can't add rcc listener.");
            return;
        }
        SessionHolder holder = getHolder(pi, true);
        if (holder == null) {
            return;
        }
        if (holder.mRccListener == null || holder.mRccListener != listener) {
            holder.mRccListener = listener;
            holder.mFlags |= 2;
            holder.mSession.setFlags(holder.mFlags);
            holder.update();
            if (DEBUG) {
                Log.d(TAG, "Added rcc listener for " + pi + ".");
            }
        } else if (DEBUG) {
            Log.d(TAG, "addRccListener listener already added.");
        }
    }

    public void removeRccListener(PendingIntent pi) {
        if (pi != null) {
            SessionHolder holder = getHolder(pi, false);
            if (holder != null && holder.mRccListener != null) {
                holder.mRccListener = null;
                holder.mFlags &= -3;
                holder.mSession.setFlags(holder.mFlags);
                holder.update();
                if (DEBUG) {
                    Log.d(TAG, "Removed rcc listener for " + pi + ".");
                }
            }
        }
    }

    public void addMediaButtonListener(PendingIntent pi, ComponentName mbrComponent, Context context) {
        if (pi == null) {
            Log.w(TAG, "Pending intent was null, can't addMediaButtonListener.");
            return;
        }
        SessionHolder holder = getHolder(pi, true);
        if (holder != null) {
            if (holder.mMediaButtonListener != null && DEBUG) {
                Log.d(TAG, "addMediaButtonListener already added " + pi);
            }
            holder.mMediaButtonListener = new MediaButtonListener(pi, context);
            holder.mFlags |= 1;
            holder.mSession.setFlags(holder.mFlags);
            holder.mSession.setMediaButtonReceiver(pi);
            holder.update();
            if (DEBUG) {
                Log.d(TAG, "addMediaButtonListener added " + pi);
            }
        }
    }

    public void removeMediaButtonListener(PendingIntent pi) {
        if (pi != null) {
            SessionHolder holder = getHolder(pi, false);
            if (holder != null && holder.mMediaButtonListener != null) {
                holder.mFlags &= -2;
                holder.mSession.setFlags(holder.mFlags);
                holder.mMediaButtonListener = null;
                holder.update();
                if (DEBUG) {
                    Log.d(TAG, "removeMediaButtonListener removed " + pi);
                }
            }
        }
    }

    private void volumeKeyLogging() {
        String extra;
        String feature;
        int stream = AudioManager.getActiveStreamType();
        if (stream == 0) {
            extra = "Call volume";
        } else if (stream == 3) {
            extra = "Music volume";
        } else {
            return;
        }
        if (!this.mPowerManager.isScreenOn()) {
            feature = "MPVL";
        } else if (stream == 0) {
            feature = "UIFW";
            extra = "Media";
        } else {
            return;
        }
        sendLogAudioCoreVolumekeyLoggingEnabled(feature, extra);
    }

    private void sendLogAudioCoreVolumekeyLoggingEnabled(String feature, String extra) {
        Parcelable cv = new ContentValues();
        String appId = "android.media.session";
        cv.put("app_id", appId);
        cv.put("feature", feature);
        cv.put("extra", extra);
        cv.put("value", Long.valueOf(1000));
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("com.samsung.android.providers.context.log.action.USE_APP_FEATURE_SURVEY");
        broadcastIntent.putExtra("data", cv);
        broadcastIntent.setPackage("com.samsung.android.providers.context");
        this.mContext.sendBroadcast(broadcastIntent);
        if (DEBUG) {
            Log.d(TAG, "AudioCoreVolumekeyLoggingEnabled success appId=" + appId + "feature=" + feature + "extra=" + extra + "value=" + 1000);
        }
    }

    private static Bitmap scaleBitmapIfTooBig(Bitmap bitmap, int maxWidth, int maxHeight) {
        if (bitmap == null) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width <= maxWidth && height <= maxHeight) {
            return bitmap;
        }
        float scale = Math.min(((float) maxWidth) / ((float) width), ((float) maxHeight) / ((float) height));
        int newWidth = Math.round(((float) width) * scale);
        int newHeight = Math.round(((float) height) * scale);
        Config newConfig = bitmap.getConfig();
        if (newConfig == null) {
            newConfig = Config.ARGB_8888;
        }
        Bitmap outBitmap = Bitmap.createBitmap(newWidth, newHeight, newConfig);
        Canvas canvas = new Canvas(outBitmap);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, null, new RectF(0.0f, 0.0f, (float) outBitmap.getWidth(), (float) outBitmap.getHeight()), paint);
        return outBitmap;
    }

    private SessionHolder getHolder(PendingIntent pi, boolean createIfMissing) {
        SessionHolder holder = (SessionHolder) this.mSessions.get(pi);
        if (holder != null || !createIfMissing) {
            return holder;
        }
        MediaSession session = new MediaSession(this.mContext, "MediaSessionHelper-" + pi.getCreatorPackage());
        session.setActive(true);
        holder = new SessionHolder(session, pi);
        this.mSessions.put(pi, holder);
        return holder;
    }

    private static void sendKeyEvent(PendingIntent pi, Context context, Intent intent) {
        try {
            pi.send(context, 0, intent);
        } catch (CanceledException e) {
            Log.e(TAG, "Error sending media key down event:", e);
        }
    }
}
