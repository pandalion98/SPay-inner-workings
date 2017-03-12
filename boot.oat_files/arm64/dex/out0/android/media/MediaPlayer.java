package android.media;

import android.app.ActivityManagerNative;
import android.app.ActivityThread;
import android.app.IActivityManager;
import android.app.backup.FullBackup;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.IPackageManager;
import android.content.pm.PackageInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioAttributes.Builder;
import android.media.MediaTimeProvider.OnMediaTimeListener;
import android.media.SubtitleController.Anchor;
import android.media.SubtitleController.Listener;
import android.media.SubtitleTrack.RenderingWidget;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.util.Log;
import android.util.Pair;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.android.internal.app.IAppOpsService;
import com.android.internal.app.IAppOpsService.Stub;
import com.sec.android.app.CscFeature;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.BitSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import libcore.io.IoBridge;
import libcore.io.Libcore;

public class MediaPlayer implements Listener {
    public static final boolean APPLY_METADATA_FILTER = true;
    public static final boolean BYPASS_METADATA_FILTER = false;
    public static final int ERROR_MEDIA_RESOURCE_OVERSPEC = -5001;
    private static final String IMEDIA_CONTEXT_AWARE = "com.samsung.CONTEXT_AWARE_MUSIC_INFO";
    private static final String IMEDIA_PLAYER = "android.media.IMediaPlayer";
    private static final String IMEDIA_PLAYER_VIDEO_EXIST = "android.media.IMediaPlayer.videoexist";
    private static final String IMEDIA_SMART_PAUSE = "android.intent.action.SMART_PAUSE";
    private static final int INVOKE_ID_ADD_EXTERNAL_SOURCE = 2;
    private static final int INVOKE_ID_ADD_EXTERNAL_SOURCE_FD = 3;
    private static final int INVOKE_ID_DESELECT_TRACK = 5;
    private static final int INVOKE_ID_GET_FULL_SUBTITLE = 11;
    private static final int INVOKE_ID_GET_INBAND_TRACK_INFO = 9;
    private static final int INVOKE_ID_GET_OUTBAND_TIMEDTEXT_TRACK_INFO = 10;
    private static final int INVOKE_ID_GET_SELECTED_TRACK = 7;
    private static final int INVOKE_ID_GET_TRACK_INFO = 1;
    private static final int INVOKE_ID_REMOVE_EXTERNAL_SOURCE = 8;
    private static final int INVOKE_ID_SELECT_TRACK = 4;
    private static final int INVOKE_ID_SET_VIDEO_SCALE_MODE = 6;
    public static final int KEY_PARAMETER_360VIDEO_XMP = 31951;
    private static final int KEY_PARAMETER_AUDIO_ATTRIBUTES = 1400;
    public static final int KEY_PARAMETER_HOVERING_TYPE = 31950;
    public static final int KEY_PARAMETER_META_AUTHORIZATION = 31600;
    public static final int KEY_PARAMETER_META_VIDEOSNAPSHOT = 31602;
    public static final int KEY_PARAMETER_META_WEATHER = 31601;
    public static final int KEY_PARAMETER_MULTIVISION_TYPE = 31605;
    public static final int KEY_PARAMETER_TIMED_TEXT_TRACK_MULTI = 31502;
    public static final int KEY_PARAMETER_TIMED_TEXT_TRACK_TIME_SYNC = 31501;
    public static final int KEY_PARAMETER_USE_SW_DECODER = 33000;
    public static final int KEY_PARAMETER_VIDEO_FPS = 31505;
    public static final int KEY_PARAMETER_VIDEO_TRUEBLUE = 31506;
    public static final int KEY_PARAMETER_WFD_TCP_DISABLE = 2500;
    private static final int MEDIA_BUFFERING_UPDATE = 3;
    private static final int MEDIA_CONTEXT_AWARE = 300;
    private static final int MEDIA_ERROR = 100;
    public static final int MEDIA_ERROR_ACCESS_TOKEN_EXPIRED = -1020;
    public static final int MEDIA_ERROR_CONNECTION_LOST = -1005;
    public static final int MEDIA_ERROR_IO = -1004;
    public static final int MEDIA_ERROR_MALFORMED = -1007;
    public static final int MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK = 200;
    public static final int MEDIA_ERROR_SERVER_DIED = 100;
    public static final int MEDIA_ERROR_SYSTEM = Integer.MIN_VALUE;
    public static final int MEDIA_ERROR_TIMED_OUT = -110;
    public static final int MEDIA_ERROR_TRANSCODING_CODEC_ALLOCATION_ERROR = -6011;
    public static final int MEDIA_ERROR_TRANSCODING_DRM_CONTENTS_IS_ALREADY_PLAYING = -6012;
    public static final int MEDIA_ERROR_TRANSCODING_LACK_OF_RESOURCE = -6013;
    public static final int MEDIA_ERROR_TRANSCODING_UNSPECIFIED_ERROR = -6100;
    public static final int MEDIA_ERROR_UNKNOWN = 1;
    public static final int MEDIA_ERROR_UNSUPPORTED = -1010;
    public static final int MEDIA_ErrDrmDevCertRevoked = -59;
    public static final int MEDIA_ErrDrmLicenseExpired = 301;
    public static final int MEDIA_ErrDrmLicenseNotFound = 300;
    public static final int MEDIA_ErrDrmLicenseNotValidYet = 302;
    public static final int MEDIA_ErrDrmRightsAcquisitionFailed = -49;
    public static final int MEDIA_ErrDrmServerDeviceLimitReached = -64;
    public static final int MEDIA_ErrDrmServerDomainRequired = -60;
    public static final int MEDIA_ErrDrmServerInternalError = -58;
    public static final int MEDIA_ErrDrmServerNotAMember = -61;
    public static final int MEDIA_ErrDrmServerProtocolVersionMismatch = -63;
    public static final int MEDIA_ErrDrmServerUnknownAccountId = -62;
    private static final int MEDIA_INFO = 200;
    public static final int MEDIA_INFO_BAD_INTERLEAVING = 800;
    public static final int MEDIA_INFO_BUFFERING_END = 702;
    public static final int MEDIA_INFO_BUFFERING_START = 701;
    public static final int MEDIA_INFO_BUFFERING_TOAST = 777;
    public static final int MEDIA_INFO_CODEC_TYPE_HEVC = 10970;
    public static final int MEDIA_INFO_EXTERNAL_METADATA_UPDATE = 803;
    public static final int MEDIA_INFO_METADATA_UPDATE = 802;
    public static final int MEDIA_INFO_NETWORK_BANDWIDTH = 703;
    public static final int MEDIA_INFO_NOT_SEEKABLE = 801;
    public static final int MEDIA_INFO_NO_AUDIO = 10972;
    public static final int MEDIA_INFO_NO_VIDEO = 10973;
    public static final int MEDIA_INFO_STARTED_AS_NEXT = 2;
    public static final int MEDIA_INFO_SUBTITLE_TIMED_OUT = 902;
    public static final int MEDIA_INFO_TIMED_TEXT_ERROR = 900;
    public static final int MEDIA_INFO_UNKNOWN = 1;
    public static final int MEDIA_INFO_UNSUPPORTED_AUDIO = 10950;
    public static final int MEDIA_INFO_UNSUPPORTED_SUBTITLE = 901;
    public static final int MEDIA_INFO_UNSUPPORTED_TICKPLAY = 10953;
    public static final int MEDIA_INFO_UNSUPPORTED_VIDEO = 10951;
    public static final int MEDIA_INFO_VIDEO_RENDERING_START = 3;
    public static final int MEDIA_INFO_VIDEO_TRACK_LAGGING = 700;
    private static final int MEDIA_META_DATA = 202;
    public static final String MEDIA_MIMETYPE_TEXT_CEA_608 = "text/cea-608";
    public static final String MEDIA_MIMETYPE_TEXT_SUBRIP = "application/x-subrip";
    public static final String MEDIA_MIMETYPE_TEXT_VTT = "text/vtt";
    private static final int MEDIA_NOP = 0;
    private static final int MEDIA_PAUSED = 7;
    private static final int MEDIA_PLAYBACK_COMPLETE = 2;
    private static final int MEDIA_PREPARED = 1;
    public static final int MEDIA_PREPARED_MIRACAST_SINK = 711;
    private static final int MEDIA_SEEK_COMPLETE = 4;
    private static final int MEDIA_SET_VIDEO_SIZE = 5;
    private static final int MEDIA_SKIPPED = 9;
    private static final int MEDIA_STARTED = 6;
    private static final int MEDIA_STOPPED = 8;
    private static final int MEDIA_SUBTITLE_DATA = 201;
    private static final int MEDIA_TIMED_TEXT = 99;
    public static final boolean METADATA_ALL = false;
    public static final boolean METADATA_UPDATE_ONLY = true;
    private static boolean MMFWContextAware = false;
    private static final boolean MMFWSmartPause = false;
    private static final int PENDING_PAUSE = 1;
    private static final int PENDING_STOP = 2;
    public static final int PLAYBACK_RATE_AUDIO_MODE_DEFAULT = 0;
    public static final int PLAYBACK_RATE_AUDIO_MODE_RESAMPLE = 2;
    public static final int PLAYBACK_RATE_AUDIO_MODE_STRETCH = 1;
    private static final String TAG = "MediaPlayer";
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT = 1;
    public static final int VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING = 2;
    private static boolean mPmExceptionForContextAware = true;
    private boolean mAIAContext = false;
    private final IAppOpsService mAppOps;
    private boolean mBypassInterruptionPolicy;
    private int mContextAwareId = 0;
    private boolean mContextAwareSend = false;
    private String mContextAwareUri = ProxyInfo.LOCAL_EXCL_LIST;
    private EventHandler mEventHandler;
    private BitSet mInbandTrackIndices = new BitSet();
    private Vector<Pair<Integer, SubtitleTrack>> mIndexTrackPairs = new Vector();
    private boolean mIsStart = false;
    private boolean mIsVideo = false;
    private Object mItsOnInstance;
    private Method mItsOnMethod;
    private int mListenerContext;
    private long mNativeContext;
    private long mNativeSurfaceTexture;
    private OnBufferingUpdateListener mOnBufferingUpdateListener;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnPlayReadyErrorListener mOnPlayReadyErrorListener;
    private OnPreparedListener mOnPreparedListener;
    private OnSeekCompleteListener mOnSeekCompleteListener;
    private OnSubtitleDataListener mOnSubtitleDataListener;
    private OnTimedMetaDataAvailableListener mOnTimedMetaDataAvailableListener;
    private OnTimedTextListener mOnTimedTextListener;
    private OnVideoSizeChangedListener mOnVideoSizeChangedListener;
    private Vector<InputStream> mOpenSubtitleSources;
    private int mPendingCommand = 0;
    private int mPid = 0;
    private boolean mScreenOnWhilePlaying;
    private int mSelectedSubtitleTrackIndex = -1;
    private boolean mStayAwake;
    private int mStreamType = Integer.MIN_VALUE;
    private SubtitleController mSubtitleController;
    private OnSubtitleDataListener mSubtitleDataListener = new OnSubtitleDataListener() {
        public void onSubtitleData(MediaPlayer mp, SubtitleData data) {
            int index = data.getTrackIndex();
            synchronized (MediaPlayer.this.mIndexTrackPairs) {
                Iterator i$ = MediaPlayer.this.mIndexTrackPairs.iterator();
                while (i$.hasNext()) {
                    Pair<Integer, SubtitleTrack> p = (Pair) i$.next();
                    if (!(p.first == null || ((Integer) p.first).intValue() != index || p.second == null)) {
                        p.second.onData(data);
                    }
                }
            }
        }
    };
    private SurfaceHolder mSurfaceHolder;
    private TimeProvider mTimeProvider;
    private int mUsage = -1;
    private WakeLock mWakeLock = null;

    public interface OnVideoSizeChangedListener {
        void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2);
    }

    public interface OnPreparedListener {
        void onPrepared(MediaPlayer mediaPlayer);
    }

    public interface OnCompletionListener {
        void onCompletion(MediaPlayer mediaPlayer);
    }

    public interface OnSubtitleDataListener {
        void onSubtitleData(MediaPlayer mediaPlayer, SubtitleData subtitleData);
    }

    private class EventHandler extends Handler {
        private MediaPlayer mMediaPlayer;

        public EventHandler(MediaPlayer mp, Looper looper) {
            super(looper);
            this.mMediaPlayer = mp;
        }

        public void handleMessage(Message msg) {
            if (this.mMediaPlayer.mNativeContext == 0) {
                Log.w(MediaPlayer.TAG, "mediaplayer went away with unhandled events");
                return;
            }
            TimeProvider timeProvider;
            Parcel parcel;
            switch (msg.what) {
                case 0:
                    return;
                case 1:
                    if (msg.arg1 != MediaPlayer.MEDIA_PREPARED_MIRACAST_SINK) {
                        try {
                            MediaPlayer.this.scanInternalSubtitleTracks();
                        } catch (RuntimeException e) {
                            sendMessage(obtainMessage(100, 1, -1010, null));
                        }
                    }
                    if (MediaPlayer.this.mOnPreparedListener != null) {
                        MediaPlayer.this.mOnPreparedListener.onPrepared(this.mMediaPlayer);
                        return;
                    }
                    return;
                case 2:
                    if (MediaPlayer.this.mOnCompletionListener != null) {
                        MediaPlayer.this.mOnCompletionListener.onCompletion(this.mMediaPlayer);
                    }
                    MediaPlayer.this.stayAwake(false);
                    if (MediaPlayer.MMFWContextAware && MediaPlayer.this.mContextAwareSend) {
                        MediaPlayer.this.sendBroadcastingIntent(MediaPlayer.IMEDIA_CONTEXT_AWARE, "TYPE", "complete", "URI", MediaPlayer.this.mContextAwareUri, "ID", Integer.valueOf(MediaPlayer.this.mContextAwareId), "PID", Integer.valueOf(MediaPlayer.this.mPid));
                        Log.i(MediaPlayer.TAG, "sendBroadcast CONTEXT_AWARE_MUSIC_INFO - type(complete) - id(" + MediaPlayer.this.mContextAwareId + ")");
                        return;
                    }
                    return;
                case 3:
                    if (MediaPlayer.this.mOnBufferingUpdateListener != null) {
                        MediaPlayer.this.mOnBufferingUpdateListener.onBufferingUpdate(this.mMediaPlayer, msg.arg1);
                        return;
                    }
                    return;
                case 4:
                    if (MediaPlayer.this.mOnSeekCompleteListener != null) {
                        MediaPlayer.this.mOnSeekCompleteListener.onSeekComplete(this.mMediaPlayer);
                        break;
                    }
                    break;
                case 5:
                    if (MediaPlayer.this.mOnVideoSizeChangedListener != null) {
                        MediaPlayer.this.mOnVideoSizeChangedListener.onVideoSizeChanged(this.mMediaPlayer, msg.arg1, msg.arg2);
                    }
                    if (msg.arg1 == 0 || msg.arg2 == 0) {
                        Log.i(MediaPlayer.TAG, "Don't send intent. msg.arg1 = " + msg.arg1 + ", msg.arg2 = " + msg.arg2);
                        return;
                    }
                    MediaPlayer.this.mIsVideo = true;
                    if (!MediaPlayer.this.mIsStart) {
                        return;
                    }
                    if (MediaPlayer.this.mAIAContext) {
                        Log.e(MediaPlayer.TAG, "context is 1, don't send BroadcastIntent!!!!!!");
                        return;
                    }
                    MediaPlayer.this.sendBroadcastingIntent(MediaPlayer.IMEDIA_PLAYER_VIDEO_EXIST, new Object[0]);
                    Log.i(MediaPlayer.TAG, "sendBroadcast android.media.IMediaPlayer.videoexist");
                    return;
                case 6:
                case 7:
                    try {
                        timeProvider = MediaPlayer.this.mTimeProvider;
                        if (timeProvider != null) {
                            timeProvider.onPaused(msg.what == 7);
                            return;
                        }
                        return;
                    } catch (NullPointerException e2) {
                        Log.d(MediaPlayer.TAG, "handleMessage MEDIA_STARTED or MEDIA_PAUSED e : ", e2);
                        return;
                    }
                case 8:
                    try {
                        timeProvider = MediaPlayer.this.mTimeProvider;
                        if (timeProvider != null) {
                            timeProvider.onStopped();
                            return;
                        }
                        return;
                    } catch (NullPointerException e22) {
                        Log.d(MediaPlayer.TAG, "handleMessage MEDIA_STOPPED e : ", e22);
                        return;
                    }
                case 9:
                    break;
                case 99:
                    if (MediaPlayer.this.mOnTimedTextListener == null) {
                        return;
                    }
                    if (msg.obj == null) {
                        MediaPlayer.this.mOnTimedTextListener.onTimedText(this.mMediaPlayer, null);
                        return;
                    } else if (msg.obj instanceof Parcel) {
                        parcel = msg.obj;
                        TimedText text = new TimedText(parcel);
                        parcel.recycle();
                        MediaPlayer.this.mOnTimedTextListener.onTimedText(this.mMediaPlayer, text);
                        return;
                    } else {
                        return;
                    }
                case 100:
                    Log.e(MediaPlayer.TAG, "Error (" + msg.arg1 + "," + msg.arg2 + ")");
                    boolean error_was_handled = false;
                    if (MediaPlayer.this.mOnErrorListener != null) {
                        if ((msg.arg2 == -49 || msg.arg2 == -60 || msg.arg2 == -61 || msg.arg2 == -64) && MediaPlayer.this.mOnPlayReadyErrorListener != null) {
                            if (msg.obj != null) {
                                Log.e(MediaPlayer.TAG, "PlayReadyAcquistion Failed \n sending onPlayReadyError " + ((String) msg.obj));
                                error_was_handled = MediaPlayer.this.mOnPlayReadyErrorListener.onPlayReadyError(this.mMediaPlayer, msg.arg1, msg.arg2, (String) msg.obj);
                            } else {
                                Log.e(MediaPlayer.TAG, "PlayReadyAcquistion Failed \n sending onPlayReadyError NULL");
                                error_was_handled = MediaPlayer.this.mOnErrorListener.onError(this.mMediaPlayer, msg.arg1, msg.arg2);
                            }
                        } else if (msg.arg2 == 300) {
                            Log.e(MediaPlayer.TAG, "License Not Found, propagate error to MoviePlaybackService.java");
                            error_was_handled = MediaPlayer.this.mOnErrorListener.onError(this.mMediaPlayer, msg.arg1, msg.arg2);
                        } else {
                            error_was_handled = MediaPlayer.this.mOnErrorListener.onError(this.mMediaPlayer, msg.arg1, msg.arg2);
                        }
                    }
                    if (!(MediaPlayer.this.mOnCompletionListener == null || error_was_handled)) {
                        MediaPlayer.this.mOnCompletionListener.onCompletion(this.mMediaPlayer);
                    }
                    MediaPlayer.this.stayAwake(false);
                    return;
                case 200:
                    switch (msg.arg1) {
                        case 700:
                            Log.i(MediaPlayer.TAG, "Info (" + msg.arg1 + "," + msg.arg2 + ")");
                            break;
                        case MediaPlayer.MEDIA_INFO_BUFFERING_START /*701*/:
                        case MediaPlayer.MEDIA_INFO_BUFFERING_END /*702*/:
                            timeProvider = MediaPlayer.this.mTimeProvider;
                            if (timeProvider != null) {
                                timeProvider.onBuffering(msg.arg1 == MediaPlayer.MEDIA_INFO_BUFFERING_START);
                                break;
                            }
                            break;
                        case MediaPlayer.MEDIA_INFO_METADATA_UPDATE /*802*/:
                            try {
                                MediaPlayer.this.scanInternalSubtitleTracks();
                                break;
                            } catch (RuntimeException e3) {
                                sendMessage(obtainMessage(100, 1, -1010, null));
                                break;
                            }
                        case MediaPlayer.MEDIA_INFO_EXTERNAL_METADATA_UPDATE /*803*/:
                            break;
                    }
                    msg.arg1 = MediaPlayer.MEDIA_INFO_METADATA_UPDATE;
                    if (MediaPlayer.this.mSubtitleController != null) {
                        MediaPlayer.this.mSubtitleController.selectDefaultTrack();
                    }
                    if (MediaPlayer.this.mOnInfoListener != null) {
                        MediaPlayer.this.mOnInfoListener.onInfo(this.mMediaPlayer, msg.arg1, msg.arg2);
                        return;
                    }
                    return;
                case 201:
                    if (MediaPlayer.this.mOnSubtitleDataListener != null && (msg.obj instanceof Parcel)) {
                        parcel = (Parcel) msg.obj;
                        SubtitleData data = new SubtitleData(parcel);
                        parcel.recycle();
                        MediaPlayer.this.mOnSubtitleDataListener.onSubtitleData(this.mMediaPlayer, data);
                        return;
                    }
                    return;
                case 202:
                    if (MediaPlayer.this.mOnTimedMetaDataAvailableListener != null && (msg.obj instanceof Parcel)) {
                        parcel = (Parcel) msg.obj;
                        TimedMetaData data2 = TimedMetaData.createTimedMetaDataFromParcel(parcel);
                        parcel.recycle();
                        MediaPlayer.this.mOnTimedMetaDataAvailableListener.onTimedMetaDataAvailable(this.mMediaPlayer, data2);
                        return;
                    }
                    return;
                case 300:
                    if (MediaPlayer.MMFWContextAware && (msg.obj instanceof Parcel)) {
                        Log.i(MediaPlayer.TAG, "send context aware event");
                        parcel = (Parcel) msg.obj;
                        String type = parcel.readString();
                        String uri = parcel.readString();
                        int id = parcel.readInt();
                        if (type.compareTo("start") == 0) {
                            MediaPlayer.this.mContextAwareSend = true;
                            MediaPlayer.this.mContextAwareId = id;
                            MediaPlayer.this.mContextAwareUri = uri;
                        }
                        MediaPlayer.this.sendBroadcastingIntent(MediaPlayer.IMEDIA_CONTEXT_AWARE, "TYPE", type, "URI", uri, "ID", Integer.valueOf(id), "PID", Integer.valueOf(MediaPlayer.this.mPid));
                        parcel.recycle();
                        Log.i(MediaPlayer.TAG, "sendBroadcast CONTEXT_AWARE_MUSIC_INFO - type(" + type + ") - id (" + id + ")");
                    }
                    if (MediaPlayer.this.mPendingCommand != 0) {
                        if (MediaPlayer.this.mPendingCommand == 1) {
                            MediaPlayer.this.sendBroadcastingIntent(MediaPlayer.IMEDIA_CONTEXT_AWARE, "TYPE", "pause", "URI", MediaPlayer.this.mContextAwareUri, "ID", Integer.valueOf(MediaPlayer.this.mContextAwareId), "PID", Integer.valueOf(MediaPlayer.this.mPid));
                            Log.i(MediaPlayer.TAG, "sendBroadcast CONTEXT_AWARE_MUSIC_INFO - type(pause) - id(" + MediaPlayer.this.mContextAwareId + ")");
                        } else {
                            MediaPlayer.this.sendBroadcastingIntent(MediaPlayer.IMEDIA_CONTEXT_AWARE, "TYPE", "stop", "URI", MediaPlayer.this.mContextAwareUri, "ID", Integer.valueOf(MediaPlayer.this.mContextAwareId), "PID", Integer.valueOf(MediaPlayer.this.mPid));
                            Log.i(MediaPlayer.TAG, "sendBroadcast CONTEXT_AWARE_MUSIC_INFO - type(stop) - id(" + MediaPlayer.this.mContextAwareId + ")");
                        }
                        MediaPlayer.this.mPendingCommand = 0;
                        MediaPlayer.this.mContextAwareSend = false;
                        return;
                    }
                    return;
                default:
                    Log.e(MediaPlayer.TAG, "Unknown message type " + msg.what);
                    return;
            }
            try {
                timeProvider = MediaPlayer.this.mTimeProvider;
                if (timeProvider != null) {
                    timeProvider.onSeekComplete(this.mMediaPlayer);
                }
            } catch (NullPointerException e222) {
                Log.d(MediaPlayer.TAG, "handleMessage MEDIA_SKIPPED e : ", e222);
            }
        }
    }

    public interface OnBufferingUpdateListener {
        void onBufferingUpdate(MediaPlayer mediaPlayer, int i);
    }

    public interface OnErrorListener {
        boolean onError(MediaPlayer mediaPlayer, int i, int i2);
    }

    public interface OnInfoListener {
        boolean onInfo(MediaPlayer mediaPlayer, int i, int i2);
    }

    public interface OnPlayReadyErrorListener {
        boolean onPlayReadyError(MediaPlayer mediaPlayer, int i, int i2, String str);
    }

    public interface OnSeekCompleteListener {
        void onSeekComplete(MediaPlayer mediaPlayer);
    }

    public interface OnTimedMetaDataAvailableListener {
        void onTimedMetaDataAvailable(MediaPlayer mediaPlayer, TimedMetaData timedMetaData);
    }

    public interface OnTimedTextListener {
        void onTimedText(MediaPlayer mediaPlayer, TimedText timedText);
    }

    static class TimeProvider implements OnSeekCompleteListener, MediaTimeProvider {
        private static final long MAX_EARLY_CALLBACK_US = 1000;
        private static final long MAX_NS_WITHOUT_POSITION_CHECK = 5000000000L;
        private static final int NOTIFY = 1;
        private static final int NOTIFY_SEEK = 3;
        private static final int NOTIFY_STOP = 2;
        private static final int NOTIFY_TIME = 0;
        private static final int REFRESH_AND_NOTIFY_TIME = 1;
        private static final String TAG = "MTP";
        private static final long TIME_ADJUSTMENT_RATE = 2;
        public boolean DEBUG = false;
        private boolean mBuffering;
        private Handler mEventHandler;
        private HandlerThread mHandlerThread;
        private long mLastNanoTime;
        private long mLastReportedTime;
        private long mLastTimeUs = 0;
        private OnMediaTimeListener[] mListeners;
        private boolean mPaused = true;
        private boolean mPausing = false;
        private MediaPlayer mPlayer;
        private boolean mRefresh = false;
        private boolean mSeeking = false;
        private boolean mStopped = true;
        private long mTimeAdjustment;
        private long[] mTimes;

        private class EventHandler extends Handler {
            public EventHandler(Looper looper) {
                super(looper);
            }

            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    switch (msg.arg1) {
                        case 0:
                            TimeProvider.this.notifyTimedEvent(false);
                            return;
                        case 1:
                            TimeProvider.this.notifyTimedEvent(true);
                            return;
                        case 2:
                            TimeProvider.this.notifyStop();
                            return;
                        case 3:
                            TimeProvider.this.notifySeek();
                            return;
                        default:
                            return;
                    }
                }
            }
        }

        public TimeProvider(MediaPlayer mp) {
            this.mPlayer = mp;
            try {
                getCurrentTimeUs(true, false);
            } catch (IllegalStateException e) {
                this.mRefresh = true;
            }
            Looper looper = Looper.myLooper();
            if (looper == null) {
                looper = Looper.getMainLooper();
                if (looper == null) {
                    this.mHandlerThread = new HandlerThread("MediaPlayerMTPEventThread", -2);
                    this.mHandlerThread.start();
                    looper = this.mHandlerThread.getLooper();
                }
            }
            this.mEventHandler = new EventHandler(looper);
            this.mListeners = new OnMediaTimeListener[0];
            this.mTimes = new long[0];
            this.mLastTimeUs = 0;
            this.mTimeAdjustment = 0;
        }

        private void scheduleNotification(int type, long delayUs) {
            if (!this.mSeeking || (type != 0 && type != 1)) {
                if (this.DEBUG) {
                    Log.v(TAG, "scheduleNotification " + type + " in " + delayUs);
                }
                this.mEventHandler.removeMessages(1);
                this.mEventHandler.sendMessageDelayed(this.mEventHandler.obtainMessage(1, type, 0), (long) ((int) (delayUs / MAX_EARLY_CALLBACK_US)));
            }
        }

        public void close() {
            this.mEventHandler.removeMessages(1);
            if (this.mHandlerThread != null) {
                this.mHandlerThread.quitSafely();
                this.mHandlerThread = null;
            }
        }

        protected void finalize() {
            if (this.mHandlerThread != null) {
                this.mHandlerThread.quitSafely();
            }
        }

        public void onPaused(boolean paused) {
            synchronized (this) {
                if (this.DEBUG) {
                    Log.d(TAG, "onPaused: " + paused);
                }
                if (this.mStopped) {
                    this.mStopped = false;
                    this.mSeeking = true;
                    scheduleNotification(3, 0);
                } else {
                    this.mPausing = paused;
                    this.mSeeking = false;
                    scheduleNotification(1, 0);
                }
            }
        }

        public void onBuffering(boolean buffering) {
            synchronized (this) {
                if (this.DEBUG) {
                    Log.d(TAG, "onBuffering: " + buffering);
                }
                this.mBuffering = buffering;
                scheduleNotification(1, 0);
            }
        }

        public void onStopped() {
            synchronized (this) {
                if (this.DEBUG) {
                    Log.d(TAG, "onStopped");
                }
                this.mPaused = true;
                this.mStopped = true;
                this.mSeeking = false;
                this.mBuffering = false;
                scheduleNotification(2, 0);
            }
        }

        public void onSeekComplete(MediaPlayer mp) {
            synchronized (this) {
                this.mStopped = false;
                this.mSeeking = true;
                scheduleNotification(3, 0);
            }
        }

        public void onNewPlayer() {
            if (this.mRefresh) {
                synchronized (this) {
                    this.mStopped = false;
                    this.mSeeking = true;
                    this.mBuffering = false;
                    scheduleNotification(3, 0);
                }
            }
        }

        private synchronized void notifySeek() {
            this.mSeeking = false;
            try {
                long timeUs = getCurrentTimeUs(true, false);
                if (this.DEBUG) {
                    Log.d(TAG, "onSeekComplete at " + timeUs);
                }
                for (OnMediaTimeListener listener : this.mListeners) {
                    if (listener == null) {
                        break;
                    }
                    listener.onSeek(timeUs);
                }
            } catch (IllegalStateException e) {
                if (this.DEBUG) {
                    Log.d(TAG, "onSeekComplete but no player");
                }
                this.mPausing = true;
                notifyTimedEvent(false);
            }
        }

        private synchronized void notifyStop() {
            for (OnMediaTimeListener listener : this.mListeners) {
                if (listener == null) {
                    break;
                }
                listener.onStop();
            }
        }

        private int registerListener(OnMediaTimeListener listener) {
            int i = 0;
            while (i < this.mListeners.length && this.mListeners[i] != listener && this.mListeners[i] != null) {
                i++;
            }
            if (i >= this.mListeners.length) {
                OnMediaTimeListener[] newListeners = new OnMediaTimeListener[(i + 1)];
                long[] newTimes = new long[(i + 1)];
                System.arraycopy(this.mListeners, 0, newListeners, 0, this.mListeners.length);
                System.arraycopy(this.mTimes, 0, newTimes, 0, this.mTimes.length);
                this.mListeners = newListeners;
                this.mTimes = newTimes;
            }
            if (this.mListeners[i] == null) {
                this.mListeners[i] = listener;
                this.mTimes[i] = -1;
            }
            return i;
        }

        public void notifyAt(long timeUs, OnMediaTimeListener listener) {
            synchronized (this) {
                if (this.DEBUG) {
                    Log.d(TAG, "notifyAt " + timeUs);
                }
                this.mTimes[registerListener(listener)] = timeUs;
                scheduleNotification(0, 0);
            }
        }

        public void scheduleUpdate(OnMediaTimeListener listener) {
            synchronized (this) {
                if (this.DEBUG) {
                    Log.d(TAG, "scheduleUpdate");
                }
                int i = registerListener(listener);
                if (!this.mStopped) {
                    this.mTimes[i] = 0;
                    scheduleNotification(0, 0);
                }
            }
        }

        public void cancelNotifications(OnMediaTimeListener listener) {
            synchronized (this) {
                int i = 0;
                while (i < this.mListeners.length) {
                    if (this.mListeners[i] != listener) {
                        if (this.mListeners[i] == null) {
                            break;
                        }
                        i++;
                    } else {
                        System.arraycopy(this.mListeners, i + 1, this.mListeners, i, (this.mListeners.length - i) - 1);
                        System.arraycopy(this.mTimes, i + 1, this.mTimes, i, (this.mTimes.length - i) - 1);
                        this.mListeners[this.mListeners.length - 1] = null;
                        this.mTimes[this.mTimes.length - 1] = -1;
                        break;
                    }
                }
                scheduleNotification(0, 0);
            }
        }

        private synchronized void notifyTimedEvent(boolean refreshTime) {
            long nowUs;
            try {
                nowUs = getCurrentTimeUs(refreshTime, true);
            } catch (IllegalStateException e) {
                this.mRefresh = true;
                this.mPausing = true;
                nowUs = getCurrentTimeUs(refreshTime, true);
            }
            long nextTimeUs = nowUs;
            if (!this.mSeeking) {
                if (this.DEBUG) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("notifyTimedEvent(").append(this.mLastTimeUs).append(" -> ").append(nowUs).append(") from {");
                    boolean first = true;
                    for (long time : this.mTimes) {
                        if (time != -1) {
                            if (!first) {
                                sb.append(", ");
                            }
                            sb.append(time);
                            first = false;
                        }
                    }
                    sb.append("}");
                    Log.d(TAG, sb.toString());
                }
                Vector<OnMediaTimeListener> activatedListeners = new Vector();
                int ix = 0;
                while (ix < this.mTimes.length && this.mListeners[ix] != null) {
                    if (this.mTimes[ix] > -1) {
                        if (this.mTimes[ix] <= MAX_EARLY_CALLBACK_US + nowUs) {
                            activatedListeners.add(this.mListeners[ix]);
                            if (this.DEBUG) {
                                Log.d(TAG, Environment.MEDIA_REMOVED);
                            }
                            this.mTimes[ix] = -1;
                        } else if (nextTimeUs == nowUs || this.mTimes[ix] < nextTimeUs) {
                            nextTimeUs = this.mTimes[ix];
                        }
                    }
                    ix++;
                }
                if (nextTimeUs <= nowUs || this.mPaused) {
                    this.mEventHandler.removeMessages(1);
                } else {
                    if (this.DEBUG) {
                        Log.d(TAG, "scheduling for " + nextTimeUs + " and " + nowUs);
                    }
                    scheduleNotification(0, nextTimeUs - nowUs);
                }
                Iterator i$ = activatedListeners.iterator();
                while (i$.hasNext()) {
                    ((OnMediaTimeListener) i$.next()).onTimedEvent(nowUs);
                }
            }
        }

        private long getEstimatedTime(long nanoTime, boolean monotonic) {
            if (this.mPaused) {
                this.mLastReportedTime = this.mLastTimeUs + this.mTimeAdjustment;
            } else {
                long timeSinceRead = (nanoTime - this.mLastNanoTime) / MAX_EARLY_CALLBACK_US;
                this.mLastReportedTime = this.mLastTimeUs + timeSinceRead;
                if (this.mTimeAdjustment > 0) {
                    long adjustment = this.mTimeAdjustment - (timeSinceRead / 2);
                    if (adjustment <= 0) {
                        this.mTimeAdjustment = 0;
                    } else {
                        this.mLastReportedTime += adjustment;
                    }
                }
            }
            return this.mLastReportedTime;
        }

        public long getCurrentTimeUs(boolean refreshTime, boolean monotonic) throws IllegalStateException {
            long j;
            boolean z = false;
            synchronized (this) {
                if (!this.mPaused || refreshTime) {
                    long nanoTime = System.nanoTime();
                    if (refreshTime || nanoTime >= this.mLastNanoTime + MAX_NS_WITHOUT_POSITION_CHECK) {
                        try {
                            this.mLastTimeUs = ((long) this.mPlayer.getCurrentPosition()) * MAX_EARLY_CALLBACK_US;
                            if (!this.mPlayer.isPlaying() || this.mBuffering) {
                                z = true;
                            }
                            this.mPaused = z;
                            if (this.DEBUG) {
                                String str;
                                String str2 = TAG;
                                StringBuilder stringBuilder = new StringBuilder();
                                if (this.mPaused) {
                                    str = "paused";
                                } else {
                                    str = "playing";
                                }
                                Log.v(str2, stringBuilder.append(str).append(" at ").append(this.mLastTimeUs).toString());
                            }
                            this.mLastNanoTime = nanoTime;
                            if (!monotonic || this.mLastTimeUs >= this.mLastReportedTime) {
                                this.mTimeAdjustment = 0;
                            } else {
                                this.mTimeAdjustment = this.mLastReportedTime - this.mLastTimeUs;
                                if (this.mTimeAdjustment > 1000000) {
                                    this.mStopped = false;
                                    this.mSeeking = true;
                                    scheduleNotification(3, 0);
                                }
                            }
                        } catch (IllegalStateException e) {
                            if (this.mPausing) {
                                this.mPausing = false;
                                getEstimatedTime(nanoTime, monotonic);
                                this.mPaused = true;
                                if (this.DEBUG) {
                                    Log.d(TAG, "illegal state, but pausing: estimating at " + this.mLastReportedTime);
                                }
                                j = this.mLastReportedTime;
                            } else {
                                throw e;
                            }
                        }
                    }
                    j = getEstimatedTime(nanoTime, monotonic);
                } else {
                    j = this.mLastReportedTime;
                }
            }
            return j;
        }
    }

    public static class TrackInfo implements Parcelable {
        static final Creator<TrackInfo> CREATOR = new Creator<TrackInfo>() {
            public TrackInfo createFromParcel(Parcel in) {
                return new TrackInfo(in);
            }

            public TrackInfo[] newArray(int size) {
                return new TrackInfo[size];
            }
        };
        public static final int MEDIA_TRACK_TYPE_AUDIO = 2;
        public static final int MEDIA_TRACK_TYPE_METADATA = 5;
        public static final int MEDIA_TRACK_TYPE_SMPTE_TT = 107;
        public static final int MEDIA_TRACK_TYPE_SUBTITLE = 4;
        public static final int MEDIA_TRACK_TYPE_TIMEDTEXT = 3;
        public static final int MEDIA_TRACK_TYPE_UNKNOWN = 0;
        public static final int MEDIA_TRACK_TYPE_VIDEO = 1;
        final MediaFormat mFormat;
        final int mTrackType;

        public int getTrackType() {
            return this.mTrackType;
        }

        public String getLanguage() {
            String language = this.mFormat.getString("language");
            return language == null ? "und" : language;
        }

        public MediaFormat getFormat() {
            if (this.mTrackType == 3 || this.mTrackType == 4) {
                return this.mFormat;
            }
            return null;
        }

        TrackInfo(Parcel in) {
            this.mTrackType = in.readInt();
            this.mFormat = MediaFormat.createSubtitleFormat(in.readString(), in.readString());
            if (this.mTrackType == 4) {
                this.mFormat.setInteger(MediaFormat.KEY_IS_AUTOSELECT, in.readInt());
                this.mFormat.setInteger(MediaFormat.KEY_IS_DEFAULT, in.readInt());
                this.mFormat.setInteger(MediaFormat.KEY_IS_FORCED_SUBTITLE, in.readInt());
            }
        }

        TrackInfo(int type, MediaFormat format) {
            this.mTrackType = type;
            this.mFormat = format;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.mTrackType);
            dest.writeString(getLanguage());
            if (this.mTrackType == 4) {
                dest.writeString(this.mFormat.getString(MediaFormat.KEY_MIME));
                dest.writeInt(this.mFormat.getInteger(MediaFormat.KEY_IS_AUTOSELECT));
                dest.writeInt(this.mFormat.getInteger(MediaFormat.KEY_IS_DEFAULT));
                dest.writeInt(this.mFormat.getInteger(MediaFormat.KEY_IS_FORCED_SUBTITLE));
            }
        }

        public String toString() {
            StringBuilder out = new StringBuilder(128);
            out.append(getClass().getName());
            out.append('{');
            switch (this.mTrackType) {
                case 1:
                    out.append("VIDEO");
                    break;
                case 2:
                    out.append("AUDIO");
                    break;
                case 3:
                    out.append("TIMEDTEXT");
                    break;
                case 4:
                    out.append("SUBTITLE");
                    break;
                default:
                    out.append("UNKNOWN");
                    break;
            }
            out.append(", " + this.mFormat.toString());
            out.append("}");
            return out.toString();
        }
    }

    private native int _getAudioStreamType() throws IllegalStateException;

    private native void _pause() throws IllegalStateException;

    private native void _prepare() throws IOException, IllegalStateException;

    private native void _release();

    private native void _reset();

    private native void _setAudioStreamType(int i);

    private native void _setAuxEffectSendLevel(float f);

    private native void _setDataSource(MediaDataSource mediaDataSource) throws IllegalArgumentException, IllegalStateException;

    private native void _setDataSource(FileDescriptor fileDescriptor, long j, long j2) throws IOException, IllegalArgumentException, IllegalStateException;

    private native void _setVideoSurface(Surface surface);

    private native void _setVolume(float f, float f2);

    private native void _start() throws IllegalStateException;

    private native void _stop() throws IllegalStateException;

    public static native int getClientMediaInfo(int i, Parcel parcel);

    private native void getParameter(int i, Parcel parcel);

    private native void nativeSetDataSource(IBinder iBinder, String str, String[] strArr, String[] strArr2) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException;

    private final native void native_finalize();

    private final native boolean native_getMetadata(boolean z, boolean z2, Parcel parcel);

    private static final native void native_init();

    private final native int native_invoke(Parcel parcel, Parcel parcel2);

    public static native int native_pullBatteryData(Parcel parcel);

    private final native int native_setMetadataFilter(Parcel parcel);

    private final native int native_setRetransmitEndpoint(String str, int i);

    private final native void native_setup(Object obj);

    private void setDataSource(java.lang.String r9, java.lang.String[] r10, java.lang.String[] r11) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.SecurityException, java.lang.IllegalStateException {
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
        r8 = this;
        r5 = android.net.Uri.parse(r9);
        r4 = r5.getScheme();
        r6 = "file";
        r6 = r6.equals(r4);
        if (r6 == 0) goto L_0x0047;
    L_0x0010:
        r9 = r5.getPath();
    L_0x0014:
        r6 = "file://";
        r6 = r9.startsWith(r6);
        if (r6 == 0) goto L_0x0024;
    L_0x001c:
        r6 = "file://";
        r7 = "";
        r9 = r9.replaceFirst(r6, r7);
    L_0x0024:
        r6 = ".sdp";
        r6 = r9.endsWith(r6);
        if (r6 != 0) goto L_0x0034;
    L_0x002c:
        r6 = ".SDP";
        r6 = r9.endsWith(r6);
        if (r6 == 0) goto L_0x0054;
    L_0x0034:
        r6 = "/storage/emulated/";
        r7 = "/mnt/shell/emulated/";
        r3 = r9.replaceFirst(r6, r7);
        r8.registerMediaMapping(r3);
        r6 = android.media.MediaHTTPService.createHttpServiceBinderIfNecessary(r9);
        r8.nativeSetDataSource(r6, r3, r10, r11);
    L_0x0046:
        return;
    L_0x0047:
        if (r4 == 0) goto L_0x0014;
    L_0x0049:
        r8.registerMediaMapping(r9);
        r6 = android.media.MediaHTTPService.createHttpServiceBinderIfNecessary(r9);
        r8.nativeSetDataSource(r6, r9, r10, r11);
        goto L_0x0046;
    L_0x0054:
        r1 = new java.io.File;
        r1.<init>(r9);
        r6 = r1.exists();
        if (r6 == 0) goto L_0x0078;
    L_0x005f:
        r2 = new java.io.FileInputStream;
        r2.<init>(r1);
        r0 = r2.getFD();	 Catch:{ all -> 0x0071 }
        r8.setDataSource(r0);	 Catch:{ all -> 0x0071 }
        if (r2 == 0) goto L_0x0046;
    L_0x006d:
        r2.close();
        goto L_0x0046;
    L_0x0071:
        r6 = move-exception;
        if (r2 == 0) goto L_0x0077;
    L_0x0074:
        r2.close();
    L_0x0077:
        throw r6;
    L_0x0078:
        r6 = new java.io.IOException;
        r7 = "setDataSource failed.";
        r6.<init>(r7);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer.setDataSource(java.lang.String, java.lang.String[], java.lang.String[]):void");
    }

    public void addTimedTextSource(android.content.Context r7, android.net.Uri r8, java.lang.String r9) throws java.io.IOException, java.lang.IllegalArgumentException, java.lang.IllegalStateException {
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
        r6 = this;
        r3 = r8.getScheme();
        if (r3 == 0) goto L_0x000e;
    L_0x0006:
        r4 = "file";
        r4 = r3.equals(r4);
        if (r4 == 0) goto L_0x0016;
    L_0x000e:
        r4 = r8.getPath();
        r6.addTimedTextSource(r4, r9);
    L_0x0015:
        return;
    L_0x0016:
        r1 = 0;
        r2 = r7.getContentResolver();	 Catch:{ SecurityException -> 0x0037, IOException -> 0x0045, all -> 0x0053 }
        r4 = "r";	 Catch:{ SecurityException -> 0x0037, IOException -> 0x0045, all -> 0x0053 }
        r1 = r2.openAssetFileDescriptor(r8, r4);	 Catch:{ SecurityException -> 0x0037, IOException -> 0x0045, all -> 0x0053 }
        if (r1 != 0) goto L_0x002a;
    L_0x0024:
        if (r1 == 0) goto L_0x0015;
    L_0x0026:
        r1.close();
        goto L_0x0015;
    L_0x002a:
        r4 = r1.getFileDescriptor();	 Catch:{ SecurityException -> 0x0037, IOException -> 0x0045, all -> 0x0053 }
        r6.addTimedTextSource(r4, r9);	 Catch:{ SecurityException -> 0x0037, IOException -> 0x0045, all -> 0x0053 }
        if (r1 == 0) goto L_0x0015;
    L_0x0033:
        r1.close();
        goto L_0x0015;
    L_0x0037:
        r0 = move-exception;
        r4 = "MediaPlayer";	 Catch:{ SecurityException -> 0x0037, IOException -> 0x0045, all -> 0x0053 }
        r5 = "addTimedTextSource SecurityException happend : ";	 Catch:{ SecurityException -> 0x0037, IOException -> 0x0045, all -> 0x0053 }
        android.util.Log.d(r4, r5, r0);	 Catch:{ SecurityException -> 0x0037, IOException -> 0x0045, all -> 0x0053 }
        if (r1 == 0) goto L_0x0015;
    L_0x0041:
        r1.close();
        goto L_0x0015;
    L_0x0045:
        r0 = move-exception;
        r4 = "MediaPlayer";	 Catch:{ SecurityException -> 0x0037, IOException -> 0x0045, all -> 0x0053 }
        r5 = "addTimedTextSource IOException happend : ";	 Catch:{ SecurityException -> 0x0037, IOException -> 0x0045, all -> 0x0053 }
        android.util.Log.d(r4, r5, r0);	 Catch:{ SecurityException -> 0x0037, IOException -> 0x0045, all -> 0x0053 }
        if (r1 == 0) goto L_0x0015;
    L_0x004f:
        r1.close();
        goto L_0x0015;
    L_0x0053:
        r4 = move-exception;
        if (r1 == 0) goto L_0x0059;
    L_0x0056:
        r1.close();
    L_0x0059:
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer.addTimedTextSource(android.content.Context, android.net.Uri, java.lang.String):void");
    }

    public native void attachAuxEffect(int i);

    public native int getAudioSessionId();

    public native Bitmap getCurrentFrame() throws IllegalStateException;

    public native int getCurrentPosition();

    public native int getDuration();

    public native PlaybackParams getPlaybackParams();

    public native SyncParams getSyncParams();

    public native int getVideoHeight();

    public native int getVideoWidth();

    public native void hovering(int i) throws IllegalStateException;

    public native boolean isLooping();

    public native boolean isPlaying();

    public native void prepareAsync() throws IllegalStateException;

    public native void seekTo(int i) throws IllegalStateException;

    public native void seekTo(int i, int i2) throws IllegalStateException;

    public native void setAudioSessionId(int i) throws IllegalArgumentException, IllegalStateException;

    public native void setLooping(boolean z);

    public native void setNextMediaPlayer(MediaPlayer mediaPlayer);

    public native boolean setParameter(int i, Parcel parcel);

    public native void setPlaybackParams(PlaybackParams playbackParams);

    public native void setSyncParams(SyncParams syncParams);

    static {
        System.loadLibrary("media_jni");
        native_init();
    }

    public MediaPlayer() {
        Looper looper = Looper.myLooper();
        if (looper != null) {
            this.mEventHandler = new EventHandler(this, looper);
        } else {
            looper = Looper.getMainLooper();
            if (looper != null) {
                this.mEventHandler = new EventHandler(this, looper);
            } else {
                this.mEventHandler = null;
            }
        }
        this.mTimeProvider = new TimeProvider(this);
        this.mOpenSubtitleSources = new Vector();
        this.mAppOps = Stub.asInterface(ServiceManager.getService(Context.APP_OPS_SERVICE));
        if (mPmExceptionForContextAware) {
            MMFWContextAware = isNeedContextAwareInfoBroadCast();
        }
        this.mPid = Process.myPid();
        native_setup(new WeakReference(this));
    }

    public Parcel newRequest() {
        Parcel parcel = Parcel.obtain();
        parcel.writeInterfaceToken(IMEDIA_PLAYER);
        return parcel;
    }

    public void invoke(Parcel request, Parcel reply) {
        int retcode = native_invoke(request, reply);
        reply.setDataPosition(0);
        if (retcode != 0) {
            throw new RuntimeException("failure code: " + retcode);
        }
    }

    public void setDisplay(SurfaceHolder sh) {
        Surface surface;
        this.mSurfaceHolder = sh;
        if (sh != null) {
            surface = sh.getSurface();
        } else {
            surface = null;
        }
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    public void setSurface(Surface surface) {
        if (this.mScreenOnWhilePlaying && surface != null) {
            Log.w(TAG, "setScreenOnWhilePlaying(true) is ineffective for Surface");
        }
        this.mSurfaceHolder = null;
        _setVideoSurface(surface);
        updateSurfaceScreenOn();
    }

    public void setVideoScalingMode(int mode) {
        if (isVideoScalingModeSupported(mode)) {
            Parcel request = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                request.writeInterfaceToken(IMEDIA_PLAYER);
                request.writeInt(6);
                request.writeInt(mode);
                invoke(request, reply);
            } finally {
                request.recycle();
                reply.recycle();
            }
        } else {
            throw new IllegalArgumentException("Scaling mode " + mode + " is not supported");
        }
    }

    public static MediaPlayer create(Context context, Uri uri) {
        return create(context, uri, null);
    }

    public static MediaPlayer create(Context context, Uri uri, SurfaceHolder holder) {
        int s = AudioSystem.newAudioSessionId();
        if (s <= 0) {
            s = 0;
        }
        return create(context, uri, holder, null, s);
    }

    public static MediaPlayer create(Context context, Uri uri, SurfaceHolder holder, AudioAttributes audioAttributes, int audioSessionId) {
        try {
            MediaPlayer mp = new MediaPlayer();
            mp.setAudioAttributes(audioAttributes != null ? audioAttributes : new Builder().build());
            mp.setAudioSessionId(audioSessionId);
            mp.setDataSource(context, uri);
            if (holder != null) {
                mp.setDisplay(holder);
            }
            mp.prepare();
            return mp;
        } catch (IOException ex) {
            Log.d(TAG, "create failed:", ex);
            return null;
        } catch (IllegalArgumentException ex2) {
            Log.d(TAG, "create failed:", ex2);
            return null;
        } catch (SecurityException ex3) {
            Log.d(TAG, "create failed:", ex3);
            return null;
        }
    }

    public static MediaPlayer create(Context context, int resid) {
        int s = AudioSystem.newAudioSessionId();
        if (s <= 0) {
            s = 0;
        }
        return create(context, resid, null, s);
    }

    public static MediaPlayer create(Context context, int resid, AudioAttributes audioAttributes, int audioSessionId) {
        try {
            AssetFileDescriptor afd = context.getResources().openRawResourceFd(resid);
            if (afd == null) {
                return null;
            }
            AudioAttributes aa;
            MediaPlayer mp = new MediaPlayer();
            if (audioAttributes != null) {
                aa = audioAttributes;
            } else {
                aa = new Builder().build();
            }
            mp.setAudioAttributes(aa);
            mp.setAudioSessionId(audioSessionId);
            mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mp.prepare();
            return mp;
        } catch (IOException ex) {
            Log.d(TAG, "create failed:", ex);
            return null;
        } catch (IllegalArgumentException ex2) {
            Log.d(TAG, "create failed:", ex2);
            return null;
        } catch (SecurityException ex3) {
            Log.d(TAG, "create failed:", ex3);
            return null;
        }
    }

    public void setAIAContext(boolean AIAContext) {
        Log.i(TAG, "setAIAFlag = " + AIAContext);
        this.mAIAContext = AIAContext;
    }

    private boolean isNeedContextAwareInfoBroadCast() {
        try {
            PackageInfo pi = IPackageManager.Stub.asInterface(ServiceManager.checkService("package")).getPackageInfo("com.samsung.android.providers.context", 0, Process.myUserHandle().getIdentifier());
            mPmExceptionForContextAware = false;
            if (pi == null || pi.versionCode < 3) {
                return false;
            }
            Log.i(TAG, "Need to enable context aware info");
            return true;
        } catch (RemoteException e) {
            Log.w(TAG, "isNeedContextAwareInfoBroadCast RemoteException");
            mPmExceptionForContextAware = true;
            return false;
        }
    }

    public void setDataSource(Context context, Uri uri) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        setDataSource(context, uri, null);
    }

    public void setDataSource(Context context, Uri uri, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        Exception ex;
        String scheme = uri.getScheme();
        if (ContentResolver.SCHEME_FILE.equals(scheme) || scheme == null) {
            setDataSource(uri.getPath());
            return;
        }
        if ("content".equals(scheme) && "settings".equals(uri.getAuthority())) {
            uri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.getDefaultType(uri));
            if (uri == null) {
                throw new FileNotFoundException("Failed to resolve default ringtone");
            }
        }
        AssetFileDescriptor fd = null;
        try {
            fd = RingtoneManager.getCurrentProfileContentResolver(context).openAssetFileDescriptor(uri, FullBackup.ROOT_TREE_TOKEN);
            if (fd != null) {
                if (fd.getDeclaredLength() < 0) {
                    setDataSource(fd.getFileDescriptor());
                } else {
                    setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getDeclaredLength());
                }
                if (fd != null) {
                    fd.close();
                }
            } else if (fd != null) {
                fd.close();
            }
        } catch (Exception e) {
            ex = e;
            try {
                Log.w(TAG, "Couldn't open file on client side; trying server side: " + ex);
                Log.d(TAG, "setDataSource IOException | SecurityException happend : ", ex);
                setDataSource(uri.toString(), (Map) headers);
            } finally {
                if (fd != null) {
                    fd.close();
                }
            }
        } catch (Exception e2) {
            ex = e2;
            Log.w(TAG, "Couldn't open file on client side; trying server side: " + ex);
            Log.d(TAG, "setDataSource IOException | SecurityException happend : ", ex);
            setDataSource(uri.toString(), (Map) headers);
        }
    }

    public void setDataSource(String path) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        setDataSource(path, null, null);
    }

    public void setDataSource(String path, Map<String, String> headers) throws IOException, IllegalArgumentException, SecurityException, IllegalStateException {
        String[] keys = null;
        String[] values = null;
        if (headers != null) {
            keys = new String[headers.size()];
            values = new String[headers.size()];
            int i = 0;
            for (Entry<String, String> entry : headers.entrySet()) {
                keys[i] = (String) entry.getKey();
                values[i] = (String) entry.getValue();
                i++;
            }
        }
        setDataSource(path, keys, values);
    }

    private void registerMediaMapping(String path) {
        if (CscFeature.getInstance().getEnableStatus("CscFeature_Common_EnableItsOn")) {
            if (this.mItsOnInstance == null || this.mItsOnMethod == null) {
                try {
                    Class<?> itsOnOemApiClass = Class.forName("com.itsoninc.android.ItsOnOemApi");
                    this.mItsOnInstance = itsOnOemApiClass.getMethod("getInstance", new Class[0]).invoke(null, new Object[0]);
                    this.mItsOnMethod = itsOnOemApiClass.getMethod("registerMediaMapping", new Class[]{String.class});
                } catch (Exception e) {
                    Log.e(TAG, "Cannot initiate Itson Instance and/or Method", e);
                }
            }
            try {
                this.mItsOnMethod.invoke(this.mItsOnInstance, new Object[]{path});
            } catch (Exception e2) {
                Log.e(TAG, "Cannot invoke registerMediaMapping", e2);
            }
        }
    }

    public int setSoundAlive(Parcel request, Parcel reply) {
        invoke(request, reply);
        return 0;
    }

    public void setDataSource(FileDescriptor fd) throws IOException, IllegalArgumentException, IllegalStateException {
        setDataSource(fd, 0, 576460752303423487L);
    }

    public void setDataSource(FileDescriptor fd, long offset, long length) throws IOException, IllegalArgumentException, IllegalStateException {
        _setDataSource(fd, offset, length);
    }

    public void setDataSource(MediaDataSource dataSource) throws IllegalArgumentException, IllegalStateException {
        _setDataSource(dataSource);
    }

    public void prepare() throws IOException, IllegalStateException {
        _prepare();
        scanInternalSubtitleTracks();
    }

    public void start() throws IllegalStateException {
        if (this.mIsVideo) {
            if (this.mAIAContext) {
                Log.e(TAG, "context is 1, don't send IMEDIA_PLAYER_VIDEO_EXIST!");
            } else {
                sendBroadcastingIntent(IMEDIA_PLAYER_VIDEO_EXIST, new Object[0]);
                Log.i(TAG, "sendBroadcast android.media.IMediaPlayer.videoexist");
            }
        }
        this.mIsStart = true;
        if (MMFWContextAware) {
            this.mContextAwareSend = false;
            this.mPendingCommand = 0;
        }
        if (isRestricted()) {
            _setVolume(0.0f, 0.0f);
        }
        stayAwake(true);
        _start();
    }

    private boolean isRestricted() {
        if (this.mBypassInterruptionPolicy) {
            return false;
        }
        try {
            int mode = this.mAppOps.checkAudioOperation(28, this.mUsage != -1 ? this.mUsage : AudioAttributes.usageForLegacyStreamType(getAudioStreamType()), Process.myUid(), ActivityThread.currentPackageName());
            if (mode != 0) {
                Log.e(TAG, "It is mode (" + mode + "), Uid " + Process.myUid() + ", Pid " + Process.myPid() + ", Name : " + ActivityThread.currentPackageName());
            }
            if (mode != 0) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            return false;
        }
    }

    private int getAudioStreamType() {
        if (this.mStreamType == Integer.MIN_VALUE) {
            this.mStreamType = _getAudioStreamType();
        }
        return this.mStreamType;
    }

    public void stop() throws IllegalStateException {
        stayAwake(false);
        if (MMFWContextAware) {
            if (this.mContextAwareSend) {
                sendBroadcastingIntent(IMEDIA_CONTEXT_AWARE, "TYPE", "stop", "URI", this.mContextAwareUri, "ID", Integer.valueOf(this.mContextAwareId), "PID", Integer.valueOf(this.mPid));
                Log.i(TAG, "sendBroadcast CONTEXT_AWARE_MUSIC_INFO - type(stop) - id(" + this.mContextAwareId + ")");
                this.mContextAwareSend = false;
            } else {
                this.mPendingCommand = 2;
            }
        }
        _stop();
    }

    public void pause() throws IllegalStateException {
        stayAwake(false);
        if (MMFWContextAware) {
            if (this.mContextAwareSend) {
                sendBroadcastingIntent(IMEDIA_CONTEXT_AWARE, "TYPE", "pause", "URI", this.mContextAwareUri, "ID", Integer.valueOf(this.mContextAwareId), "PID", Integer.valueOf(this.mPid));
                Log.i(TAG, "sendBroadcast CONTEXT_AWARE_MUSIC_INFO - type(pause) - id(" + this.mContextAwareId + ")");
            } else {
                this.mPendingCommand = 1;
            }
        }
        _pause();
    }

    public void setWakeMode(Context context, int mode) {
        boolean washeld = false;
        if (this.mWakeLock != null) {
            if (this.mWakeLock.isHeld()) {
                washeld = true;
                this.mWakeLock.release();
            }
            this.mWakeLock = null;
        }
        this.mWakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(mode, MediaPlayer.class.getName());
        this.mWakeLock.setReferenceCounted(false);
        if (washeld) {
            this.mWakeLock.acquire();
        }
    }

    public void setScreenOnWhilePlaying(boolean screenOn) {
        if (this.mScreenOnWhilePlaying != screenOn) {
            if (screenOn && this.mSurfaceHolder == null) {
                Log.w(TAG, "setScreenOnWhilePlaying(true) is ineffective without a SurfaceHolder");
            }
            this.mScreenOnWhilePlaying = screenOn;
            updateSurfaceScreenOn();
        }
    }

    private void stayAwake(boolean awake) {
        if (this.mWakeLock != null) {
            if (awake && !this.mWakeLock.isHeld()) {
                this.mWakeLock.acquire();
            } else if (!awake && this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
        }
        this.mStayAwake = awake;
        updateSurfaceScreenOn();
    }

    private void updateSurfaceScreenOn() {
        if (this.mSurfaceHolder != null) {
            SurfaceHolder surfaceHolder = this.mSurfaceHolder;
            boolean z = this.mScreenOnWhilePlaying && this.mStayAwake;
            surfaceHolder.setKeepScreenOn(z);
        }
    }

    public PlaybackParams easyPlaybackParams(float rate, int audioMode) {
        PlaybackParams params = new PlaybackParams();
        params.allowDefaults();
        switch (audioMode) {
            case 0:
                params.setSpeed(rate).setPitch(1.0f);
                break;
            case 1:
                params.setSpeed(rate).setPitch(1.0f).setAudioFallbackMode(2);
                break;
            case 2:
                params.setSpeed(rate).setPitch(rate);
                break;
            default:
                throw new IllegalArgumentException("Audio playback mode " + audioMode + " is not supported");
        }
        return params;
    }

    public MediaTimestamp getTimestamp() {
        try {
            return new MediaTimestamp(((long) getCurrentPosition()) * 1000, System.nanoTime(), isPlaying() ? getPlaybackParams().getSpeed() : 0.0f);
        } catch (IllegalStateException e) {
            return null;
        }
    }

    public Metadata getMetadata(boolean update_only, boolean apply_filter) {
        Parcel reply = Parcel.obtain();
        Metadata data = new Metadata();
        if (!native_getMetadata(update_only, apply_filter, reply)) {
            reply.recycle();
            return null;
        } else if (data.parse(reply)) {
            return data;
        } else {
            reply.recycle();
            return null;
        }
    }

    public int setMetadataFilter(Set<Integer> allow, Set<Integer> block) {
        Parcel request = newRequest();
        int capacity = request.dataSize() + ((((allow.size() + 1) + 1) + block.size()) * 4);
        if (request.dataCapacity() < capacity) {
            request.setDataCapacity(capacity);
        }
        request.writeInt(allow.size());
        for (Integer t : allow) {
            request.writeInt(t.intValue());
        }
        request.writeInt(block.size());
        for (Integer t2 : block) {
            request.writeInt(t2.intValue());
        }
        return native_setMetadataFilter(request);
    }

    public void release() {
        stayAwake(false);
        if (MMFWContextAware) {
            if (this.mContextAwareSend) {
                sendBroadcastingIntent(IMEDIA_CONTEXT_AWARE, "TYPE", "stop", "URI", this.mContextAwareUri, "ID", Integer.valueOf(this.mContextAwareId), "PID", Integer.valueOf(this.mPid));
                Log.i(TAG, "sendBroadcast CONTEXT_AWARE_MUSIC_INFO - type(release) - id(" + this.mContextAwareId + ")");
                this.mContextAwareSend = false;
            } else {
                this.mPendingCommand = 2;
            }
        }
        updateSurfaceScreenOn();
        this.mOnPreparedListener = null;
        this.mOnBufferingUpdateListener = null;
        this.mOnCompletionListener = null;
        this.mOnSeekCompleteListener = null;
        this.mOnErrorListener = null;
        this.mOnInfoListener = null;
        this.mOnVideoSizeChangedListener = null;
        this.mOnTimedTextListener = null;
        this.mIsStart = false;
        if (this.mTimeProvider != null) {
            this.mTimeProvider.close();
            this.mTimeProvider = null;
        }
        this.mOnSubtitleDataListener = null;
        _release();
    }

    public void reset() {
        this.mSelectedSubtitleTrackIndex = -1;
        synchronized (this.mOpenSubtitleSources) {
            Iterator i$ = this.mOpenSubtitleSources.iterator();
            while (i$.hasNext()) {
                try {
                    ((InputStream) i$.next()).close();
                } catch (IOException e) {
                }
            }
            this.mOpenSubtitleSources.clear();
        }
        if (this.mSubtitleController != null) {
            this.mSubtitleController.reset();
        }
        if (this.mTimeProvider != null) {
            this.mTimeProvider.close();
            this.mTimeProvider = null;
        }
        stayAwake(false);
        if (MMFWContextAware) {
            if (this.mContextAwareSend) {
                sendBroadcastingIntent(IMEDIA_CONTEXT_AWARE, "TYPE", "stop", "URI", this.mContextAwareUri, "ID", Integer.valueOf(this.mContextAwareId), "PID", Integer.valueOf(this.mPid));
                Log.i(TAG, "sendBroadcast CONTEXT_AWARE_MUSIC_INFO - type(reset) - id(" + this.mContextAwareId + ")");
                this.mContextAwareSend = false;
            } else {
                this.mPendingCommand = 2;
            }
        }
        this.mIsStart = false;
        _reset();
        if (this.mEventHandler != null) {
            this.mEventHandler.removeCallbacksAndMessages(null);
        }
        synchronized (this.mIndexTrackPairs) {
            this.mIndexTrackPairs.clear();
            this.mInbandTrackIndices.clear();
        }
    }

    public void setAudioStreamType(int streamtype) {
        _setAudioStreamType(streamtype);
        this.mStreamType = streamtype;
    }

    public boolean setParameter(int key, String value) {
        Parcel p = Parcel.obtain();
        p.writeString(value);
        boolean ret = setParameter(key, p);
        p.recycle();
        return ret;
    }

    public boolean setParameter(int key, int value) {
        Parcel p = Parcel.obtain();
        p.writeInt(value);
        boolean ret = setParameter(key, p);
        p.recycle();
        return ret;
    }

    public Parcel getParcelParameter(int key) {
        Parcel p = Parcel.obtain();
        getParameter(key, p);
        return p;
    }

    public String getStringParameter(int key) {
        Parcel p = Parcel.obtain();
        getParameter(key, p);
        String ret = p.readString();
        p.recycle();
        return ret;
    }

    public int getIntParameter(int key) {
        Parcel p = Parcel.obtain();
        getParameter(key, p);
        int ret = p.readInt();
        p.recycle();
        return ret;
    }

    public void setAudioAttributes(AudioAttributes attributes) throws IllegalArgumentException {
        if (attributes == null) {
            String msg = "Cannot set AudioAttributes to null";
            throw new IllegalArgumentException("Cannot set AudioAttributes to null");
        }
        this.mUsage = attributes.getUsage();
        this.mBypassInterruptionPolicy = (attributes.getAllFlags() & 64) != 0;
        Parcel pattributes = Parcel.obtain();
        attributes.writeToParcel(pattributes, 1);
        setParameter((int) KEY_PARAMETER_AUDIO_ATTRIBUTES, pattributes);
        pattributes.recycle();
        setAudioStreamType(AudioAttributes.toLegacyStreamType(attributes));
    }

    public void setVolume(float leftVolume, float rightVolume) {
        if (!isRestricted()) {
            _setVolume(leftVolume, rightVolume);
        }
    }

    public void setVolume(float volume) {
        setVolume(volume, volume);
    }

    public void setAuxEffectSendLevel(float level) {
        if (!isRestricted()) {
            _setAuxEffectSendLevel(level);
        }
    }

    public TrackInfo[] getTrackInfo() throws IllegalStateException {
        TrackInfo[] allTrackInfo;
        TrackInfo[] trackInfo = getInbandTrackInfo();
        synchronized (this.mIndexTrackPairs) {
            allTrackInfo = new TrackInfo[this.mIndexTrackPairs.size()];
            for (int i = 0; i < allTrackInfo.length; i++) {
                Pair<Integer, SubtitleTrack> p = (Pair) this.mIndexTrackPairs.get(i);
                if (p.first != null) {
                    allTrackInfo[i] = trackInfo[((Integer) p.first).intValue()];
                } else {
                    SubtitleTrack track = p.second;
                    allTrackInfo[i] = new TrackInfo(track.getTrackType(), track.getFormat());
                }
            }
        }
        return allTrackInfo;
    }

    private TrackInfo[] getInbandTrackInfo() throws IllegalStateException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            request.writeInterfaceToken(IMEDIA_PLAYER);
            request.writeInt(1);
            invoke(request, reply);
            TrackInfo[] trackInfo = (TrackInfo[]) reply.createTypedArray(TrackInfo.CREATOR);
            return trackInfo;
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    private static boolean availableMimeTypeForExternalSource(String mimeType) {
        if (MEDIA_MIMETYPE_TEXT_SUBRIP.equals(mimeType)) {
            return true;
        }
        return false;
    }

    public void setSubtitleAnchor(SubtitleController controller, Anchor anchor) {
        this.mSubtitleController = controller;
        this.mSubtitleController.setAnchor(anchor);
    }

    private synchronized void setSubtitleAnchor() {
        if (this.mSubtitleController == null) {
            final HandlerThread thread = new HandlerThread("SetSubtitleAnchorThread");
            thread.start();
            new Handler(thread.getLooper()).post(new Runnable() {
                public void run() {
                    MediaPlayer.this.mSubtitleController = new SubtitleController(ActivityThread.currentApplication(), MediaPlayer.this.mTimeProvider, MediaPlayer.this);
                    MediaPlayer.this.mSubtitleController.setAnchor(new Anchor() {
                        public void setSubtitleWidget(RenderingWidget subtitleWidget) {
                        }

                        public Looper getSubtitleLooper() {
                            return Looper.getMainLooper();
                        }
                    });
                    thread.getLooper().quitSafely();
                }
            });
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Log.w(TAG, "failed to join SetSubtitleAnchorThread");
            }
        }
    }

    public void onSubtitleTrackSelected(SubtitleTrack track) {
        if (this.mSelectedSubtitleTrackIndex >= 0) {
            try {
                selectOrDeselectInbandTrack(this.mSelectedSubtitleTrackIndex, false);
            } catch (IllegalStateException e) {
            }
            this.mSelectedSubtitleTrackIndex = -1;
        }
        setOnSubtitleDataListener(null);
        if (track != null) {
            synchronized (this.mIndexTrackPairs) {
                Iterator i$ = this.mIndexTrackPairs.iterator();
                while (i$.hasNext()) {
                    Pair<Integer, SubtitleTrack> p = (Pair) i$.next();
                    if (p.first != null && p.second == track) {
                        this.mSelectedSubtitleTrackIndex = ((Integer) p.first).intValue();
                        break;
                    }
                }
            }
            if (this.mSelectedSubtitleTrackIndex >= 0) {
                try {
                    selectOrDeselectInbandTrack(this.mSelectedSubtitleTrackIndex, true);
                } catch (IllegalStateException e2) {
                }
                setOnSubtitleDataListener(this.mSubtitleDataListener);
            }
        }
    }

    public void addSubtitleSource(InputStream is, MediaFormat format) throws IllegalStateException {
        final InputStream fIs = is;
        final MediaFormat fFormat = format;
        if (is != null) {
            synchronized (this.mOpenSubtitleSources) {
                this.mOpenSubtitleSources.add(is);
            }
        } else {
            Log.w(TAG, "addSubtitleSource called with null InputStream");
        }
        final HandlerThread thread = new HandlerThread("SubtitleReadThread", -5);
        thread.start();
        new Handler(thread.getLooper()).post(new Runnable() {
            private int addTrack() {
                Exception e;
                Throwable th;
                if (fIs == null || MediaPlayer.this.mSubtitleController == null) {
                    return MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE;
                }
                SubtitleTrack track = MediaPlayer.this.mSubtitleController.addTrack(fFormat);
                if (track == null) {
                    return MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE;
                }
                Scanner scanner = null;
                try {
                    Scanner scanner2 = new Scanner(fIs, "UTF-8");
                    try {
                        String contents = scanner2.useDelimiter("\\A").next();
                        synchronized (MediaPlayer.this.mOpenSubtitleSources) {
                            MediaPlayer.this.mOpenSubtitleSources.remove(fIs);
                        }
                        if (scanner2 != null) {
                            scanner2.close();
                        }
                        synchronized (MediaPlayer.this.mIndexTrackPairs) {
                            MediaPlayer.this.mIndexTrackPairs.add(Pair.create(null, track));
                        }
                        track.onData(contents.getBytes(), true, -1);
                        return MediaPlayer.MEDIA_INFO_EXTERNAL_METADATA_UPDATE;
                    } catch (Exception e2) {
                        e = e2;
                        scanner = scanner2;
                        try {
                            Log.e(MediaPlayer.TAG, e.getMessage(), e);
                            synchronized (MediaPlayer.this.mOpenSubtitleSources) {
                                MediaPlayer.this.mOpenSubtitleSources.remove(fIs);
                            }
                            if (scanner != null) {
                                return MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE;
                            }
                            scanner.close();
                            return MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE;
                        } catch (Throwable th2) {
                            th = th2;
                            synchronized (MediaPlayer.this.mOpenSubtitleSources) {
                                MediaPlayer.this.mOpenSubtitleSources.remove(fIs);
                            }
                            if (scanner != null) {
                                scanner.close();
                            }
                            throw th;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        scanner = scanner2;
                        synchronized (MediaPlayer.this.mOpenSubtitleSources) {
                            MediaPlayer.this.mOpenSubtitleSources.remove(fIs);
                        }
                        if (scanner != null) {
                            scanner.close();
                        }
                        throw th;
                    }
                } catch (Exception e3) {
                    e = e3;
                    Log.e(MediaPlayer.TAG, e.getMessage(), e);
                    synchronized (MediaPlayer.this.mOpenSubtitleSources) {
                        MediaPlayer.this.mOpenSubtitleSources.remove(fIs);
                    }
                    if (scanner != null) {
                        return MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE;
                    }
                    scanner.close();
                    return MediaPlayer.MEDIA_INFO_UNSUPPORTED_SUBTITLE;
                }
            }

            public void run() {
                int res = addTrack();
                if (MediaPlayer.this.mEventHandler != null) {
                    MediaPlayer.this.mEventHandler.sendMessage(MediaPlayer.this.mEventHandler.obtainMessage(200, res, 0, null));
                }
                thread.getLooper().quitSafely();
            }
        });
    }

    private void scanInternalSubtitleTracks() {
        if (this.mSubtitleController == null) {
            Log.d(TAG, "setSubtitleAnchor in MediaPlayer");
            setSubtitleAnchor();
        }
        populateInbandTracks();
        if (this.mSubtitleController != null) {
            this.mSubtitleController.selectDefaultTrack();
        }
    }

    private void populateInbandTracks() {
        TrackInfo[] tracks = getInbandTrackInfo();
        synchronized (this.mIndexTrackPairs) {
            for (int i = 0; i < tracks.length; i++) {
                if (!this.mInbandTrackIndices.get(i)) {
                    this.mInbandTrackIndices.set(i);
                    if (tracks[i].getTrackType() == 4) {
                        this.mIndexTrackPairs.add(Pair.create(Integer.valueOf(i), this.mSubtitleController.addTrack(tracks[i].getFormat())));
                    } else {
                        this.mIndexTrackPairs.add(Pair.create(Integer.valueOf(i), null));
                    }
                }
            }
        }
    }

    public void addTimedTextSource(String path, String mimeType) throws IOException, IllegalArgumentException, IllegalStateException {
        if (availableMimeTypeForExternalSource(mimeType)) {
            File file = new File(path);
            if (file.exists()) {
                FileInputStream is = new FileInputStream(file);
                try {
                    addTimedTextSource(is.getFD(), mimeType);
                    return;
                } finally {
                    if (is != null) {
                        is.close();
                    }
                }
            } else {
                throw new IOException(path);
            }
        }
        throw new IllegalArgumentException("Illegal mimeType for timed text source: " + mimeType);
    }

    public void addTimedTextSource(FileDescriptor fd, String mimeType) throws IllegalArgumentException, IllegalStateException {
        addTimedTextSource(fd, 0, 576460752303423487L, mimeType);
    }

    public void addTimedTextSource(FileDescriptor fd, long offset, long length, String mime) throws IllegalArgumentException, IllegalStateException {
        if (availableMimeTypeForExternalSource(mime)) {
            try {
                FileDescriptor fd2 = Libcore.os.dup(fd);
                MediaFormat fFormat = new MediaFormat();
                fFormat.setString(MediaFormat.KEY_MIME, mime);
                fFormat.setInteger(MediaFormat.KEY_IS_TIMED_TEXT, 1);
                if (this.mSubtitleController == null) {
                    setSubtitleAnchor();
                }
                if (!this.mSubtitleController.hasRendererFor(fFormat)) {
                    this.mSubtitleController.registerRenderer(new SRTRenderer(ActivityThread.currentApplication(), this.mEventHandler));
                }
                final SubtitleTrack track = this.mSubtitleController.addTrack(fFormat);
                synchronized (this.mIndexTrackPairs) {
                    this.mIndexTrackPairs.add(Pair.create(null, track));
                }
                final FileDescriptor fd3 = fd2;
                final long offset2 = offset;
                final long length2 = length;
                final HandlerThread thread = new HandlerThread("TimedTextReadThread", 9);
                thread.start();
                new Handler(thread.getLooper()).post(new Runnable() {
                    private int addTrack() {
                        int i;
                        InputStream is = null;
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        try {
                            Libcore.os.lseek(fd3, offset2, OsConstants.SEEK_SET);
                            byte[] buffer = new byte[4096];
                            long total = 0;
                            while (total < length2) {
                                int bytes = IoBridge.read(fd3, buffer, 0, (int) Math.min((long) buffer.length, length2 - total));
                                if (bytes < 0) {
                                    break;
                                }
                                bos.write(buffer, 0, bytes);
                                total += (long) bytes;
                            }
                            track.onData(bos.toByteArray(), true, -1);
                            i = MediaPlayer.MEDIA_INFO_EXTERNAL_METADATA_UPDATE;
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (IOException e) {
                                    Log.e(MediaPlayer.TAG, e.getMessage(), e);
                                }
                            }
                        } catch (Exception e2) {
                            Log.e(MediaPlayer.TAG, e2.getMessage(), e2);
                            i = MediaPlayer.MEDIA_INFO_TIMED_TEXT_ERROR;
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (IOException e3) {
                                    Log.e(MediaPlayer.TAG, e3.getMessage(), e3);
                                }
                            }
                        } catch (Throwable th) {
                            if (is != null) {
                                try {
                                    is.close();
                                } catch (IOException e32) {
                                    Log.e(MediaPlayer.TAG, e32.getMessage(), e32);
                                }
                            }
                        }
                        return i;
                    }

                    public void run() {
                        int res = addTrack();
                        if (MediaPlayer.this.mEventHandler != null) {
                            MediaPlayer.this.mEventHandler.sendMessage(MediaPlayer.this.mEventHandler.obtainMessage(200, res, 0, null));
                        }
                        thread.getLooper().quitSafely();
                    }
                });
                return;
            } catch (ErrnoException ex) {
                Log.e(TAG, ex.getMessage(), ex);
                throw new RuntimeException(ex);
            }
        }
        throw new IllegalArgumentException("Illegal mimeType for timed text source: " + mime);
    }

    public void addTimedTextSourceSEC(String path, String mimeType) throws IOException, IllegalArgumentException, IllegalStateException {
        Parcel request;
        Parcel reply;
        if (availableMimeTypeForExternalSource(mimeType)) {
            File file = new File(path);
            if (file.exists()) {
                FileInputStream is = new FileInputStream(file);
                try {
                    FileDescriptor fd = is.getFD();
                    request = Parcel.obtain();
                    reply = Parcel.obtain();
                    request.writeInterfaceToken(IMEDIA_PLAYER);
                    request.writeInt(3);
                    request.writeFileDescriptor(fd);
                    request.writeLong(0);
                    request.writeLong(576460752303423487L);
                    request.writeString(mimeType);
                    invoke(request, reply);
                    request.recycle();
                    reply.recycle();
                    if (is != null) {
                        is.close();
                    }
                    populateInbandTracks();
                    return;
                } catch (Throwable th) {
                    if (is != null) {
                        is.close();
                    }
                }
            } else {
                throw new IOException(path);
            }
        }
        throw new IllegalArgumentException("Illegal mimeType for timed text source: " + mimeType);
    }

    public byte[] getFullDumpSubtitle(FileDescriptor fd, String mimeType) throws IllegalArgumentException, IllegalStateException {
        if (availableMimeTypeForExternalSource(mimeType)) {
            Parcel request = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            byte[] ret = null;
            try {
                request.writeInterfaceToken(IMEDIA_PLAYER);
                request.writeInt(11);
                request.writeFileDescriptor(fd);
                request.writeLong(0);
                request.writeLong(576460752303423487L);
                request.writeString(mimeType);
                invoke(request, reply);
                reply.setDataPosition(0);
                Log.v(TAG, "parcel size = " + reply.dataSize());
                ret = reply.marshall();
                return ret;
            } finally {
                request.recycle();
                reply.recycle();
            }
        } else {
            throw new IllegalArgumentException("Illegal mimeType for timed text source: " + mimeType);
        }
    }

    public TrackInfo[] getInbandTracksInfo() throws IllegalStateException {
        Log.e(TAG, "getInbandSubtilteTrackInfo");
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            request.writeInterfaceToken(IMEDIA_PLAYER);
            request.writeInt(9);
            invoke(request, reply);
            TrackInfo[] trackInfo = (TrackInfo[]) reply.createTypedArray(TrackInfo.CREATOR);
            return trackInfo;
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    public TrackInfo[] getOutbandTimedTextTrackInfo() throws IllegalStateException {
        Log.e(TAG, "getOutbandTimedTextTrackInfo");
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            request.writeInterfaceToken(IMEDIA_PLAYER);
            request.writeInt(10);
            invoke(request, reply);
            TrackInfo[] trackInfo = (TrackInfo[]) reply.createTypedArray(TrackInfo.CREATOR);
            return trackInfo;
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.media.MediaPlayer.TrackInfo[] getOutbandSubtilteTrackInfo() throws java.lang.IllegalStateException {
        /*
        r12 = this;
        r8 = "MediaPlayer";
        r9 = "getOutbandSubtilteTrackInfo";
        android.util.Log.d(r8, r9);
        r7 = r12.getInbandTrackInfo();
        r8 = r12.mIndexTrackPairs;
        r8 = r8.size();
        r9 = r7.length;
        r3 = r8 - r9;
        if (r3 > 0) goto L_0x0022;
    L_0x0016:
        r8 = "MediaPlayer";
        r9 = "no outband subtitle track";
        android.util.Log.d(r8, r9);
        r8 = 0;
        r4 = new android.media.MediaPlayer.TrackInfo[r8];
    L_0x0021:
        return r4;
    L_0x0022:
        r1 = 0;
        r4 = new android.media.MediaPlayer.TrackInfo[r3];
        r9 = r12.mIndexTrackPairs;
        monitor-enter(r9);
        r0 = r7.length;	 Catch:{ all -> 0x0085 }
        r2 = r1;
    L_0x002a:
        r8 = r12.mIndexTrackPairs;	 Catch:{ all -> 0x0088 }
        r8 = r8.size();	 Catch:{ all -> 0x0088 }
        if (r0 >= r8) goto L_0x005e;
    L_0x0032:
        r8 = r12.mIndexTrackPairs;	 Catch:{ all -> 0x0088 }
        r5 = r8.get(r0);	 Catch:{ all -> 0x0088 }
        r5 = (android.util.Pair) r5;	 Catch:{ all -> 0x0088 }
        r8 = r5.first;	 Catch:{ all -> 0x0088 }
        if (r8 != 0) goto L_0x008b;
    L_0x003e:
        r6 = r5.second;	 Catch:{ all -> 0x0088 }
        r6 = (android.media.SubtitleTrack) r6;	 Catch:{ all -> 0x0088 }
        r8 = r6.getTrackType();	 Catch:{ all -> 0x0088 }
        r10 = 4;
        if (r8 != r10) goto L_0x008b;
    L_0x0049:
        r1 = r2 + 1;
        r8 = new android.media.MediaPlayer$TrackInfo;	 Catch:{ all -> 0x0085 }
        r10 = r6.getTrackType();	 Catch:{ all -> 0x0085 }
        r11 = r6.getFormat();	 Catch:{ all -> 0x0085 }
        r8.<init>(r10, r11);	 Catch:{ all -> 0x0085 }
        r4[r2] = r8;	 Catch:{ all -> 0x0085 }
    L_0x005a:
        r0 = r0 + 1;
        r2 = r1;
        goto L_0x002a;
    L_0x005e:
        monitor-exit(r9);	 Catch:{ all -> 0x0088 }
        if (r2 == r3) goto L_0x0021;
    L_0x0061:
        r8 = "MediaPlayer";
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "invalid outband subtitle track number - outOfBandSubtitleTrackNum = ";
        r9 = r9.append(r10);
        r9 = r9.append(r3);
        r10 = " , index = ";
        r9 = r9.append(r10);
        r9 = r9.append(r2);
        r9 = r9.toString();
        android.util.Log.e(r8, r9);
        goto L_0x0021;
    L_0x0085:
        r8 = move-exception;
    L_0x0086:
        monitor-exit(r9);	 Catch:{ all -> 0x0085 }
        throw r8;
    L_0x0088:
        r8 = move-exception;
        r1 = r2;
        goto L_0x0086;
    L_0x008b:
        r1 = r2;
        goto L_0x005a;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.MediaPlayer.getOutbandSubtilteTrackInfo():android.media.MediaPlayer$TrackInfo[]");
    }

    public void removeOutbandTimedTextSource() throws IllegalStateException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            request.writeInterfaceToken(IMEDIA_PLAYER);
            request.writeInt(8);
            invoke(request, reply);
            int outOfBandSubtitleTrackNum = this.mIndexTrackPairs.size() - getInbandTrackInfo().length;
            synchronized (this.mIndexTrackPairs) {
                this.mIndexTrackPairs.clear();
                this.mInbandTrackIndices.clear();
            }
            populateInbandTracks();
            synchronized (this.mIndexTrackPairs) {
                SubtitleTrack[] subtitiletracks = this.mSubtitleController.getTracks();
                for (int i = subtitiletracks.length - outOfBandSubtitleTrackNum; i < subtitiletracks.length; i++) {
                    this.mIndexTrackPairs.add(Pair.create(null, subtitiletracks[i]));
                }
            }
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    public void removeOutbandSubtitleSource() throws IllegalStateException {
        Log.d(TAG, "removeOutbandSubtitleSource");
        if (this.mSubtitleController == null) {
            Log.e(TAG, "Should have subtitle controller already set");
            return;
        }
        this.mSelectedSubtitleTrackIndex = -1;
        if (!this.mOpenSubtitleSources.isEmpty()) {
            synchronized (this.mOpenSubtitleSources) {
                Iterator i$ = this.mOpenSubtitleSources.iterator();
                while (i$.hasNext()) {
                    try {
                        ((InputStream) i$.next()).close();
                    } catch (IOException e) {
                    }
                }
                this.mOpenSubtitleSources.clear();
            }
        }
        if (this.mSubtitleController != null) {
            this.mSubtitleController.resetTracks();
        }
        synchronized (this.mIndexTrackPairs) {
            this.mIndexTrackPairs.clear();
            this.mInbandTrackIndices.clear();
        }
        populateInbandTracks();
    }

    public int getSelectedTrack(int trackType) throws IllegalStateException {
        int i;
        if (this.mSubtitleController != null && (trackType == 4 || trackType == 3)) {
            SubtitleTrack subtitleTrack = this.mSubtitleController.getSelectedTrack();
            if (subtitleTrack != null) {
                synchronized (this.mIndexTrackPairs) {
                    for (i = 0; i < this.mIndexTrackPairs.size(); i++) {
                        if (((Pair) this.mIndexTrackPairs.get(i)).second == subtitleTrack && subtitleTrack.getTrackType() == trackType) {
                            return i;
                        }
                    }
                }
            }
        }
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            request.writeInterfaceToken(IMEDIA_PLAYER);
            request.writeInt(7);
            request.writeInt(trackType);
            invoke(request, reply);
            int inbandTrackIndex = reply.readInt();
            synchronized (this.mIndexTrackPairs) {
                i = 0;
                while (i < this.mIndexTrackPairs.size()) {
                    Pair<Integer, SubtitleTrack> p = (Pair) this.mIndexTrackPairs.get(i);
                    if (p.first == null || ((Integer) p.first).intValue() != inbandTrackIndex) {
                        i++;
                    } else {
                        request.recycle();
                        reply.recycle();
                        return i;
                    }
                }
                request.recycle();
                reply.recycle();
                return -1;
            }
        } catch (Throwable th) {
            request.recycle();
            reply.recycle();
        }
    }

    public void selectTrack(int index) throws IllegalStateException {
        selectOrDeselectTrack(index, true);
    }

    public void deselectTrack(int index) throws IllegalStateException {
        selectOrDeselectTrack(index, false);
    }

    private void selectOrDeselectTrack(int index, boolean select) throws IllegalStateException {
        populateInbandTracks();
        try {
            Pair<Integer, SubtitleTrack> p = (Pair) this.mIndexTrackPairs.get(index);
            SubtitleTrack track = p.second;
            if (track == null) {
                selectOrDeselectInbandTrack(((Integer) p.first).intValue(), select);
            } else if (this.mSubtitleController == null) {
            } else {
                if (select) {
                    if (track.getTrackType() == 3) {
                        int ttIndex = getSelectedTrack(3);
                        synchronized (this.mIndexTrackPairs) {
                            if (ttIndex >= 0) {
                                if (ttIndex < this.mIndexTrackPairs.size()) {
                                    Pair<Integer, SubtitleTrack> p2 = (Pair) this.mIndexTrackPairs.get(ttIndex);
                                    if (p2.first != null && p2.second == null) {
                                        selectOrDeselectInbandTrack(((Integer) p2.first).intValue(), false);
                                    }
                                }
                            }
                        }
                    }
                    this.mSubtitleController.selectTrack(track);
                } else if (this.mSubtitleController.getSelectedTrack() == track) {
                    this.mSubtitleController.selectTrack(null);
                } else {
                    Log.w(TAG, "trying to deselect track that was not selected");
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    private void selectOrDeselectInbandTrack(int index, boolean select) throws IllegalStateException {
        Parcel request = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        try {
            request.writeInterfaceToken(IMEDIA_PLAYER);
            request.writeInt(select ? 4 : 5);
            request.writeInt(index);
            invoke(request, reply);
        } finally {
            request.recycle();
            reply.recycle();
        }
    }

    public void setRetransmitEndpoint(InetSocketAddress endpoint) throws IllegalStateException, IllegalArgumentException {
        String addrString = null;
        int port = 0;
        if (endpoint != null) {
            addrString = endpoint.getAddress().getHostAddress();
            port = endpoint.getPort();
        }
        int ret = native_setRetransmitEndpoint(addrString, port);
        if (ret != 0) {
            throw new IllegalArgumentException("Illegal re-transmit endpoint; native ret " + ret);
        }
    }

    protected void finalize() {
        native_finalize();
    }

    public MediaTimeProvider getMediaTimeProvider() {
        if (this.mTimeProvider == null) {
            this.mTimeProvider = new TimeProvider(this);
        }
        return this.mTimeProvider;
    }

    private static void postEventFromNative(Object mediaplayer_ref, int what, int arg1, int arg2, Object obj) {
        if ((arg2 == -49 || arg2 == -60 || arg2 == -61 || arg2 == -64) && obj != null) {
            Log.d(TAG, "postEventFromNative Error String is" + ((String) obj));
        }
        MediaPlayer mp = (MediaPlayer) ((WeakReference) mediaplayer_ref).get();
        if (mp != null) {
            if (what == 200 && arg1 == 2) {
                mp.start();
            }
            if (mp.mEventHandler != null) {
                mp.mEventHandler.sendMessage(mp.mEventHandler.obtainMessage(what, arg1, arg2, obj));
            }
        }
    }

    private boolean sendBroadcastingIntent(String intentName, Object... extraData) {
        if (extraData.length % 2 != 0) {
            Log.w(TAG, "sendBroadcastingIntent invailed param - " + intentName);
            return false;
        }
        Intent intent = new Intent(intentName);
        intent.addFlags(67108864);
        if (intentName.equals(IMEDIA_PLAYER_VIDEO_EXIST)) {
            Log.w(TAG, "this is IMEDIA_PLAYER_VIDEO_EXIST");
            intent.setAllowFds(false);
        }
        int i = 0;
        while (i < extraData.length) {
            try {
                if (extraData[i + 1] instanceof String) {
                    intent.putExtra(extraData[i].toString(), extraData[i + 1].toString());
                } else if (extraData[i + 1] instanceof Integer) {
                    intent.putExtra(extraData[i].toString(), (Integer) extraData[i + 1]);
                } else {
                    Log.w(TAG, "Invailed Type. Add " + extraData[i + 1].getClass().getName());
                }
                i += 2;
            } catch (ClassCastException e) {
                Log.i(TAG, "sendBroadcast(ClassCastException) fail");
                return false;
            }
        }
        try {
            IActivityManager iActivityManager = ActivityManagerNative.getDefault();
            Binder.getCallingUserHandle();
            iActivityManager.broadcastIntent(null, intent, null, null, -1, null, null, null, -1, null, false, false, UserHandle.myUserId());
            return true;
        } catch (RemoteException e2) {
            Log.i(TAG, "sendBroadcast fail");
            return false;
        }
    }

    public void setOnPreparedListener(OnPreparedListener listener) {
        this.mOnPreparedListener = listener;
    }

    public void setOnCompletionListener(OnCompletionListener listener) {
        this.mOnCompletionListener = listener;
    }

    public void setOnBufferingUpdateListener(OnBufferingUpdateListener listener) {
        this.mOnBufferingUpdateListener = listener;
    }

    public void setOnSeekCompleteListener(OnSeekCompleteListener listener) {
        this.mOnSeekCompleteListener = listener;
    }

    public void setOnVideoSizeChangedListener(OnVideoSizeChangedListener listener) {
        this.mOnVideoSizeChangedListener = listener;
    }

    public void setOnTimedTextListener(OnTimedTextListener listener) {
        this.mOnTimedTextListener = listener;
    }

    public void setOnSubtitleDataListener(OnSubtitleDataListener listener) {
        this.mOnSubtitleDataListener = listener;
    }

    public void setOnTimedMetaDataAvailableListener(OnTimedMetaDataAvailableListener listener) {
        this.mOnTimedMetaDataAvailableListener = listener;
    }

    public void setOnPlayReadyErrorListener(OnPlayReadyErrorListener listener) {
        this.mOnPlayReadyErrorListener = listener;
    }

    public void setOnErrorListener(OnErrorListener listener) {
        this.mOnErrorListener = listener;
    }

    public void setOnInfoListener(OnInfoListener listener) {
        this.mOnInfoListener = listener;
    }

    private boolean isVideoScalingModeSupported(int mode) {
        return mode == 1 || mode == 2;
    }
}
