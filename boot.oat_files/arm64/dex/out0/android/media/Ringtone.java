package android.media;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources.NotFoundException;
import android.database.Cursor;
import android.media.AudioAttributes.Builder;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.ProxyInfo;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Binder;
import android.os.Environment;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.storage.StorageEventListener;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;

public class Ringtone {
    private static final boolean LOGD = true;
    private static final String[] MEDIA_COLUMNS = new String[]{"_id", "_data", "title"};
    private static final String MEDIA_SELECTION = "mime_type LIKE 'audio/%' OR mime_type IN ('application/ogg', 'application/x-flac')";
    private static final String TAG = "Ringtone";
    private static StorageEventListener mStorageListener = new StorageEventListener() {
        public void onStorageStateChanged(String path, String oldStage, String newState) {
            if (Environment.MEDIA_MOUNTED.equals(newState)) {
                Ringtone.setExtSDCardPath();
            }
        }
    };
    private static StorageManager mStorageManager = null;
    private static final ArrayList<Ringtone> sActiveRingtones = new ArrayList();
    private Uri mActualUri = null;
    private final boolean mAllowRemote;
    private AudioAttributes mAudioAttributes = new Builder().setUsage(6).setContentType(4).build();
    private final AudioManager mAudioManager;
    private final MyOnCompletionListener mCompletionListener = new MyOnCompletionListener();
    private final Context mContext;
    private boolean mIsDecryptMode = false;
    private boolean mIsLooping = false;
    private boolean mIsTelecomPkg;
    private MediaPlayer mLocalPlayer;
    private final Object mPlaybackSettingsLock = new Object();
    private final IRingtonePlayer mRemotePlayer;
    private final Binder mRemoteToken;
    private Object mRingtoneLock = new Object();
    private int mSecForSeek = 0;
    private String mTitle;
    private Uri mUri;
    private boolean mUriStatus;
    private float mVolume = 1.0f;

    class MyOnCompletionListener implements OnCompletionListener {
        MyOnCompletionListener() {
        }

        public void onCompletion(MediaPlayer mp) {
            synchronized (Ringtone.sActiveRingtones) {
                Ringtone.sActiveRingtones.remove(Ringtone.this);
            }
        }
    }

    public Ringtone(Context context, boolean allowRemote) {
        Binder binder = null;
        this.mContext = context;
        this.mAudioManager = (AudioManager) this.mContext.getSystemService(Context.AUDIO_SERVICE);
        this.mAllowRemote = allowRemote;
        this.mRemotePlayer = allowRemote ? this.mAudioManager.getRingtonePlayer() : null;
        if (allowRemote) {
            binder = new Binder();
        }
        this.mRemoteToken = binder;
        this.mIsTelecomPkg = this.mContext.getPackageName().equals("com.android.server.telecom");
        if (this.mIsTelecomPkg) {
            initStorageManager(context);
        }
        this.mUriStatus = false;
        RingtoneManager.clearCachedUri();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void initStorageManager(android.content.Context r3) {
        /*
        r1 = mStorageListener;
        monitor-enter(r1);
        r0 = mStorageManager;	 Catch:{ all -> 0x002b }
        if (r0 != 0) goto L_0x0029;
    L_0x0007:
        r0 = "storage";
        r0 = r3.getSystemService(r0);	 Catch:{ all -> 0x002b }
        r0 = (android.os.storage.StorageManager) r0;	 Catch:{ all -> 0x002b }
        mStorageManager = r0;	 Catch:{ all -> 0x002b }
        r0 = mStorageManager;	 Catch:{ all -> 0x002b }
        if (r0 != 0) goto L_0x001f;
    L_0x0016:
        r0 = "Ringtone";
        r2 = "Can't not get storagemager";
        android.util.Log.e(r0, r2);	 Catch:{ all -> 0x002b }
        monitor-exit(r1);	 Catch:{ all -> 0x002b }
    L_0x001e:
        return;
    L_0x001f:
        r0 = mStorageManager;	 Catch:{ all -> 0x002b }
        r2 = mStorageListener;	 Catch:{ all -> 0x002b }
        r0.registerListener(r2);	 Catch:{ all -> 0x002b }
        setExtSDCardPath();	 Catch:{ all -> 0x002b }
    L_0x0029:
        monitor-exit(r1);	 Catch:{ all -> 0x002b }
        goto L_0x001e;
    L_0x002b:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x002b }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.Ringtone.initStorageManager(android.content.Context):void");
    }

    private static void setExtSDCardPath() {
        if (mStorageManager == null) {
            Log.e(TAG, "StorageManager is null");
            return;
        }
        for (StorageVolume volume : mStorageManager.getVolumeList()) {
            if ("sd".equals(volume.getSubSystem()) && volume.isRemovable()) {
                Log.d(TAG, "Ext Sd card path is " + volume.getPath());
                RingtoneManager.EXTERNAL_PATH = volume.getPath();
                return;
            }
        }
        Log.d(TAG, "Can't get external sdcard path");
    }

    @Deprecated
    public void setStreamType(int streamType) {
        setAudioAttributes(new Builder().setInternalLegacyStreamType(streamType).build());
    }

    public void setRepeat(boolean repeate) {
        if (this.mLocalPlayer != null) {
            this.mLocalPlayer.setLooping(repeate);
        }
    }

    public void setSecForSeek(int mSec) {
        this.mSecForSeek = mSec;
    }

    @Deprecated
    public int getStreamType() {
        return AudioAttributes.toLegacyStreamType(this.mAudioAttributes);
    }

    public void setAudioAttributes(AudioAttributes attributes) throws IllegalArgumentException {
        if (attributes == null) {
            throw new IllegalArgumentException("Invalid null AudioAttributes for Ringtone");
        }
        this.mAudioAttributes = attributes;
        setUri(this.mUri);
    }

    public AudioAttributes getAudioAttributes() {
        return this.mAudioAttributes;
    }

    public void setLooping(boolean looping) {
        synchronized (this.mPlaybackSettingsLock) {
            this.mIsLooping = looping;
            applyPlaybackProperties_sync();
        }
    }

    public void setVolume(float volume) {
        synchronized (this.mPlaybackSettingsLock) {
            if (volume < 0.0f) {
                volume = 0.0f;
            }
            if (volume > 1.0f) {
                volume = 1.0f;
            }
            this.mVolume = volume;
            applyPlaybackProperties_sync();
        }
    }

    private void applyPlaybackProperties_sync() {
        if (this.mLocalPlayer != null) {
            this.mLocalPlayer.setVolume(this.mVolume);
            this.mLocalPlayer.setLooping(this.mIsLooping);
        } else if (!this.mAllowRemote || this.mRemotePlayer == null) {
            Log.w(TAG, "Neither local nor remote player available when applying playback properties");
        } else {
            try {
                this.mRemotePlayer.setPlaybackProperties(this.mRemoteToken, this.mVolume, this.mIsLooping);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem setting playback properties: ", e);
            }
        }
    }

    public String getTitle(Context context) {
        if (this.mTitle != null) {
            return this.mTitle;
        }
        String title = getTitle(context, this.mUri, true, this.mAllowRemote);
        this.mTitle = title;
        return title;
    }

    public static String getTitle(Context context, Uri uri, boolean followSettingsUri, boolean allowRemote) {
        ContentResolver res = context.getContentResolver();
        String title = null;
        if (uri != null) {
            String authority = uri.getAuthority();
            if (!"settings".equals(authority)) {
                Cursor cursor = null;
                try {
                    if ("media".equals(authority)) {
                        cursor = res.query(uri, MEDIA_COLUMNS, allowRemote ? null : MEDIA_SELECTION, null, null);
                        if (cursor != null && cursor.getCount() == 1) {
                            cursor.moveToFirst();
                            String string = cursor.getString(2);
                            if (cursor != null) {
                                cursor.close();
                            }
                            return string;
                        }
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (SecurityException e) {
                    IRingtonePlayer mRemotePlayer = null;
                    if (allowRemote) {
                        mRemotePlayer = ((AudioManager) context.getSystemService(Context.AUDIO_SERVICE)).getRingtonePlayer();
                    }
                    if (mRemotePlayer != null) {
                        try {
                            title = mRemotePlayer.getTitle(uri);
                        } catch (RemoteException e2) {
                        }
                    }
                    if (cursor != null) {
                        cursor.close();
                    }
                } catch (Throwable th) {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
                if (title == null) {
                    title = uri.getLastPathSegment();
                }
            } else if (followSettingsUri) {
                title = context.getString(17040291, getTitle(context, RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.getDefaultType(uri)), false, allowRemote));
            }
        }
        if (title == null) {
            title = context.getString(17040294);
            if (title == null) {
                title = ProxyInfo.LOCAL_EXCL_LIST;
            }
        }
        return title;
    }

    private boolean isExtSDCardUri(Uri uri) {
        if (uri == null || !uri.toString().startsWith(Media.EXTERNAL_CONTENT_URI.toString()) || RingtoneManager.EXTERNAL_PATH == null) {
            return false;
        }
        Cursor cursor = null;
        try {
            Boolean cache = RingtoneManager.getCachedUri(uri);
            if (cache != null) {
                Log.d(TAG, "get cachedUri : " + cache);
                boolean booleanValue = cache.booleanValue();
                if (cursor == null) {
                    return booleanValue;
                }
                cursor.close();
                return booleanValue;
            }
            cursor = this.mContext.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
            if (cursor == null || cursor.getCount() != 1) {
                Log.d(TAG, "Invalid ringtone uri");
            } else {
                cursor.moveToFirst();
                String path = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                RingtoneManager.setCacheUri(uri, path);
                if (path != null && path.startsWith(RingtoneManager.EXTERNAL_PATH)) {
                    Log.d(TAG, "ringtone is in extSdcard ");
                    if (cursor != null) {
                        cursor.close();
                    }
                    return true;
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            return false;
        } catch (Exception ex) {
            Log.e(TAG, "can't check extSdcard", ex);
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public boolean setUri(Uri uri) {
        Exception e;
        boolean ret = true;
        destroyLocalPlayer();
        this.mUri = uri;
        if (this.mUri == null) {
            return true;
        }
        if (this.mActualUri == null) {
            this.mActualUri = ActualURIfromDefaultURI(this.mContext, this.mUri);
        }
        try {
            String highlightOffset = this.mActualUri.getQueryParameter("highlight_offset");
            Log.d(TAG, "highlightoffset is :" + highlightOffset);
            if (highlightOffset == null || highlightOffset.isEmpty()) {
                if (this.mActualUri.toString().startsWith(Media.INTERNAL_CONTENT_URI.toString())) {
                    this.mSecForSeek = 0;
                }
                this.mLocalPlayer = new MediaPlayer();
                if (this.mIsTelecomPkg) {
                    this.mAudioAttributes = new Builder().setUsage(this.mAudioAttributes.getUsage()).setContentType(this.mAudioAttributes.getContentType()).addTag("AUDIO_STREAM_RING").build();
                }
                try {
                    if (!isExtSDCardUri(this.mActualUri)) {
                        throw new SecurityException("External Storage");
                    }
                    this.mLocalPlayer.setDataSource(this.mContext, this.mActualUri);
                    if (this.mAudioAttributes != null) {
                        this.mLocalPlayer.setAudioAttributes(this.mAudioAttributes);
                    }
                    synchronized (this.mPlaybackSettingsLock) {
                        applyPlaybackProperties_sync();
                    }
                    this.mLocalPlayer.prepare();
                    this.mUriStatus = true;
                    if (this.mLocalPlayer != null) {
                        Log.d(TAG, "Successfully created local player");
                    } else {
                        Log.d(TAG, "Problem opening; delegating to remote player, return false : " + this.mActualUri);
                    }
                    return ret;
                } catch (SecurityException e2) {
                    e = e2;
                } catch (IOException e3) {
                    e = e3;
                }
            } else {
                this.mSecForSeek = Integer.parseInt(highlightOffset);
                this.mLocalPlayer = new MediaPlayer();
                if (this.mIsTelecomPkg) {
                    this.mAudioAttributes = new Builder().setUsage(this.mAudioAttributes.getUsage()).setContentType(this.mAudioAttributes.getContentType()).addTag("AUDIO_STREAM_RING").build();
                }
                if (!isExtSDCardUri(this.mActualUri)) {
                    this.mLocalPlayer.setDataSource(this.mContext, this.mActualUri);
                    if (this.mAudioAttributes != null) {
                        this.mLocalPlayer.setAudioAttributes(this.mAudioAttributes);
                    }
                    synchronized (this.mPlaybackSettingsLock) {
                        applyPlaybackProperties_sync();
                    }
                    this.mLocalPlayer.prepare();
                    this.mUriStatus = true;
                    if (this.mLocalPlayer != null) {
                        Log.d(TAG, "Problem opening; delegating to remote player, return false : " + this.mActualUri);
                    } else {
                        Log.d(TAG, "Successfully created local player");
                    }
                    return ret;
                }
                throw new SecurityException("External Storage");
            }
        } catch (Exception ex) {
            Log.e(TAG, "Failed get highlight offset", ex);
            this.mSecForSeek = 0;
        }
        destroyLocalPlayer();
        if (!this.mAllowRemote) {
            Log.w(TAG, "Remote playback not allowed: " + e);
        }
        if (!this.mIsDecryptMode) {
            Log.i(TAG, "Inside Japan Flag for Decrypt Mode");
            int ringtoneType = RingtoneManager.getDefaultType(this.mUri);
            if ("trigger_restart_min_framework".equals(SystemProperties.get("vold.decrypt")) || WifiEnterpriseConfig.ENGINE_ENABLE.equals(SystemProperties.get("vold.decrypt"))) {
                Log.i(TAG, "Inside Decrypt Mode : Re-initializing the MediaPlayer with the new Uri, type :" + ringtoneType);
                if (ringtoneType == 1 || ringtoneType == 128) {
                    this.mUri = Uri.parse("file:///system/media/audio/ringtones/" + SystemProperties.get("ro.config.ringtone"));
                } else {
                    this.mUri = Uri.parse("file:///system/media/audio/notifications/" + SystemProperties.get("ro.config.notification_sound"));
                }
                this.mIsDecryptMode = true;
                return setUri(this.mUri);
            }
        }
        if (this.mIsTelecomPkg && this.mAllowRemote && this.mRemotePlayer != null && isExtSDCardUri(this.mActualUri)) {
            this.mUriStatus = true;
        } else {
            this.mUriStatus = false;
            ret = false;
        }
        if (this.mLocalPlayer != null) {
            Log.d(TAG, "Successfully created local player");
        } else {
            Log.d(TAG, "Problem opening; delegating to remote player, return false : " + this.mActualUri);
        }
        return ret;
    }

    public Uri getUri() {
        return this.mUri;
    }

    public void play() {
        if (this.mLocalPlayer != null) {
            if (this.mAudioManager.getStreamVolume(AudioAttributes.toLegacyStreamType(this.mAudioAttributes)) != 0 || getStreamType() == 3 || (getStreamType() == 3 && this.mIsTelecomPkg)) {
                if (this.mSecForSeek > 0) {
                    Log.d(TAG, "play()_jump to " + this.mSecForSeek + "mSec");
                    this.mLocalPlayer.seekTo(this.mSecForSeek);
                    this.mSecForSeek = 0;
                }
                startLocalPlayer();
            }
        } else if (this.mAllowRemote && this.mRemotePlayer != null && this.mUri != null) {
            boolean looping;
            float volume;
            Uri canonicalUri = this.mUri.getCanonicalUri();
            synchronized (this.mPlaybackSettingsLock) {
                looping = this.mIsLooping;
                volume = this.mVolume;
            }
            try {
                this.mRemotePlayer.play(this.mRemoteToken, canonicalUri, this.mAudioAttributes, volume, looping);
            } catch (IllegalStateException e) {
                Log.e(TAG, "IlligalStateException Occured in MediaPlayer");
            } catch (RemoteException e2) {
                if (!playFallbackRingtone()) {
                    Log.w(TAG, "Problem playing ringtone: " + e2);
                }
            }
        } else if (!playFallbackRingtone()) {
            Log.w(TAG, "Neither local nor remote playback available");
        }
    }

    public void stop() {
        if (this.mLocalPlayer != null) {
            destroyLocalPlayer();
        } else if (this.mAllowRemote && this.mRemotePlayer != null) {
            try {
                this.mRemotePlayer.stop(this.mRemoteToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem stopping ringtone: " + e);
            }
        }
    }

    private void destroyLocalPlayer() {
        synchronized (this.mRingtoneLock) {
            if (this.mLocalPlayer != null) {
                this.mLocalPlayer.setVolume(0.0f, 0.0f);
                this.mLocalPlayer.release();
                this.mLocalPlayer = null;
                synchronized (sActiveRingtones) {
                    sActiveRingtones.remove(this);
                }
            }
        }
    }

    private void startLocalPlayer() {
        if (this.mLocalPlayer != null) {
            synchronized (sActiveRingtones) {
                sActiveRingtones.add(this);
            }
            this.mLocalPlayer.setOnCompletionListener(this.mCompletionListener);
            this.mLocalPlayer.start();
        }
    }

    public boolean isPlaying() {
        boolean z = false;
        if (this.mLocalPlayer != null) {
            try {
                z = this.mLocalPlayer.isPlaying();
            } catch (IllegalStateException ie) {
                Log.d(TAG, "Problem isPlaying ringtone: " + ie);
            }
        } else if (!this.mAllowRemote || this.mRemotePlayer == null) {
            Log.w(TAG, "Neither local nor remote playback available");
        } else {
            try {
                z = this.mRemotePlayer.isPlaying(this.mRemoteToken);
            } catch (RemoteException e) {
                Log.w(TAG, "Problem checking ringtone: " + e);
            }
        }
        return z;
    }

    private boolean playFallbackRingtone() {
        if (this.mAudioManager.getStreamVolume(AudioAttributes.toLegacyStreamType(this.mAudioAttributes)) != 0) {
            int ringtoneType = RingtoneManager.getDefaultType(this.mUri);
            if (ringtoneType == -1 || RingtoneManager.getActualDefaultRingtoneUri(this.mContext, ringtoneType) != null) {
                AssetFileDescriptor afd;
                if (ringtoneType == 1 || ringtoneType == 128) {
                    try {
                        Log.d(TAG, "play fallbackring");
                        afd = this.mContext.getResources().openRawResourceFd(17825823);
                    } catch (IOException e) {
                        destroyLocalPlayer();
                        Log.e(TAG, "Failed to open fallback ringtone");
                    } catch (NotFoundException e2) {
                        Log.e(TAG, "Fallback ringtone does not exist");
                    }
                } else {
                    Log.d(TAG, "play fallbacknoti");
                    afd = this.mContext.getResources().openRawResourceFd(17825822);
                }
                if (afd != null) {
                    this.mLocalPlayer = new MediaPlayer();
                    if (afd.getDeclaredLength() < 0) {
                        this.mLocalPlayer.setDataSource(afd.getFileDescriptor());
                    } else {
                        this.mLocalPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getDeclaredLength());
                    }
                    this.mLocalPlayer.setAudioAttributes(this.mAudioAttributes);
                    synchronized (this.mPlaybackSettingsLock) {
                        applyPlaybackProperties_sync();
                    }
                    this.mLocalPlayer.prepare();
                    this.mLocalPlayer.start();
                    afd.close();
                    return true;
                }
                Log.e(TAG, "Could not load fallback ringtone");
            } else {
                Log.w(TAG, "not playing fallback for " + this.mUri);
            }
        }
        return false;
    }

    void setTitle(String title) {
        this.mTitle = title;
    }

    protected void finalize() {
        if (this.mLocalPlayer != null) {
            this.mLocalPlayer.release();
        }
    }

    public void setVolume(float rightVol, float leftVol) {
        if (this.mLocalPlayer != null) {
            this.mLocalPlayer.setVolume(rightVol, leftVol);
        }
    }

    public boolean isUriTrue() {
        return this.mUriStatus;
    }

    private Uri ActualURIfromDefaultURI(Context context, Uri uri) {
        int ringtoneType = RingtoneManager.getDefaultType(uri);
        if (ringtoneType != -1) {
            Log.i(TAG, "ActualURIfromDefaultURI Type : " + ringtoneType);
            Uri ActualUri = RingtoneManager.getActualDefaultRingtoneUri(this.mContext, ringtoneType);
            Log.i(TAG, "Change URI : " + uri + " to " + ActualUri);
            if (ActualUri == null) {
                return uri;
            }
            return ActualUri;
        }
        Log.i(TAG, "This is not default URI");
        return uri;
    }
}
