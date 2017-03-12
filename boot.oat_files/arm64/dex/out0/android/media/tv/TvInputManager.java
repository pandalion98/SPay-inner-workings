package android.media.tv;

import android.graphics.Rect;
import android.media.PlaybackParams;
import android.media.tv.ITvInputClient.Stub;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pools.Pool;
import android.util.Pools.SimplePool;
import android.util.SparseArray;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventSender;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.View;
import com.android.internal.util.Preconditions;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class TvInputManager {
    public static final String ACTION_BLOCKED_RATINGS_CHANGED = "android.media.tv.action.BLOCKED_RATINGS_CHANGED";
    public static final String ACTION_PARENTAL_CONTROLS_ENABLED_CHANGED = "android.media.tv.action.PARENTAL_CONTROLS_ENABLED_CHANGED";
    public static final String ACTION_QUERY_CONTENT_RATING_SYSTEMS = "android.media.tv.action.QUERY_CONTENT_RATING_SYSTEMS";
    public static final int DVB_DEVICE_DEMUX = 0;
    public static final int DVB_DEVICE_DVR = 1;
    static final int DVB_DEVICE_END = 2;
    public static final int DVB_DEVICE_FRONTEND = 2;
    static final int DVB_DEVICE_START = 0;
    public static final int INPUT_STATE_CONNECTED = 0;
    public static final int INPUT_STATE_CONNECTED_STANDBY = 1;
    public static final int INPUT_STATE_DISCONNECTED = 2;
    public static final String META_DATA_CONTENT_RATING_SYSTEMS = "android.media.tv.metadata.CONTENT_RATING_SYSTEMS";
    private static final String TAG = "TvInputManager";
    public static final long TIME_SHIFT_INVALID_TIME = Long.MIN_VALUE;
    public static final int TIME_SHIFT_STATUS_AVAILABLE = 3;
    public static final int TIME_SHIFT_STATUS_UNAVAILABLE = 2;
    public static final int TIME_SHIFT_STATUS_UNKNOWN = 0;
    public static final int TIME_SHIFT_STATUS_UNSUPPORTED = 1;
    public static final int VIDEO_UNAVAILABLE_REASON_AUDIO_ONLY = 4;
    public static final int VIDEO_UNAVAILABLE_REASON_BUFFERING = 3;
    static final int VIDEO_UNAVAILABLE_REASON_END = 4;
    static final int VIDEO_UNAVAILABLE_REASON_START = 0;
    public static final int VIDEO_UNAVAILABLE_REASON_TUNING = 1;
    public static final int VIDEO_UNAVAILABLE_REASON_UNKNOWN = 0;
    public static final int VIDEO_UNAVAILABLE_REASON_WEAK_SIGNAL = 2;
    private final List<TvInputCallbackRecord> mCallbackRecords = new LinkedList();
    private final ITvInputClient mClient;
    private final Object mLock = new Object();
    private int mNextSeq;
    private final ITvInputManager mService;
    private final SparseArray<SessionCallbackRecord> mSessionCallbackRecordMap = new SparseArray();
    private final Map<String, Integer> mStateMap = new ArrayMap();
    private final int mUserId;

    public static final class Hardware {
        private final ITvInputHardware mInterface;

        private Hardware(ITvInputHardware hardwareInterface) {
            this.mInterface = hardwareInterface;
        }

        private ITvInputHardware getInterface() {
            return this.mInterface;
        }

        public boolean setSurface(Surface surface, TvStreamConfig config) {
            try {
                return this.mInterface.setSurface(surface, config);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void setStreamVolume(float volume) {
            try {
                this.mInterface.setStreamVolume(volume);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public boolean dispatchKeyEventToHdmi(KeyEvent event) {
            try {
                return this.mInterface.dispatchKeyEventToHdmi(event);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void overrideAudioSink(int audioType, String audioAddress, int samplingRate, int channelMask, int format) {
            try {
                this.mInterface.overrideAudioSink(audioType, audioAddress, samplingRate, channelMask, format);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static abstract class HardwareCallback {
        public abstract void onReleased();

        public abstract void onStreamConfigChanged(TvStreamConfig[] tvStreamConfigArr);
    }

    public static final class Session {
        static final int DISPATCH_HANDLED = 1;
        static final int DISPATCH_IN_PROGRESS = -1;
        static final int DISPATCH_NOT_HANDLED = 0;
        private static final long INPUT_SESSION_NOT_RESPONDING_TIMEOUT = 2500;
        private final List<TvTrackInfo> mAudioTracks;
        private InputChannel mChannel;
        private final InputEventHandler mHandler;
        private final Object mMetadataLock;
        private final Pool<PendingEvent> mPendingEventPool;
        private final SparseArray<PendingEvent> mPendingEvents;
        private String mSelectedAudioTrackId;
        private String mSelectedSubtitleTrackId;
        private String mSelectedVideoTrackId;
        private TvInputEventSender mSender;
        private final int mSeq;
        private final ITvInputManager mService;
        private final SparseArray<SessionCallbackRecord> mSessionCallbackRecordMap;
        private final List<TvTrackInfo> mSubtitleTracks;
        private IBinder mToken;
        private final int mUserId;
        private int mVideoHeight;
        private final List<TvTrackInfo> mVideoTracks;
        private int mVideoWidth;

        public interface FinishedInputEventCallback {
            void onFinishedInputEvent(Object obj, boolean z);
        }

        private final class InputEventHandler extends Handler {
            public static final int MSG_FLUSH_INPUT_EVENT = 3;
            public static final int MSG_SEND_INPUT_EVENT = 1;
            public static final int MSG_TIMEOUT_INPUT_EVENT = 2;

            InputEventHandler(Looper looper) {
                super(looper, null, true);
            }

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        Session.this.sendInputEventAndReportResultOnMainLooper((PendingEvent) msg.obj);
                        return;
                    case 2:
                        Session.this.finishedInputEvent(msg.arg1, false, true);
                        return;
                    case 3:
                        Session.this.finishedInputEvent(msg.arg1, false, false);
                        return;
                    default:
                        return;
                }
            }
        }

        private final class PendingEvent implements Runnable {
            public FinishedInputEventCallback mCallback;
            public InputEvent mEvent;
            public Handler mEventHandler;
            public Object mEventToken;
            public boolean mHandled;

            private PendingEvent() {
            }

            public void recycle() {
                this.mEvent = null;
                this.mEventToken = null;
                this.mCallback = null;
                this.mEventHandler = null;
                this.mHandled = false;
            }

            public void run() {
                this.mCallback.onFinishedInputEvent(this.mEventToken, this.mHandled);
                synchronized (this.mEventHandler) {
                    Session.this.recyclePendingEventLocked(this);
                }
            }
        }

        private final class TvInputEventSender extends InputEventSender {
            public TvInputEventSender(InputChannel inputChannel, Looper looper) {
                super(inputChannel, looper);
            }

            public void onInputEventFinished(int seq, boolean handled) {
                Session.this.finishedInputEvent(seq, handled, false);
            }
        }

        private Session(IBinder token, InputChannel channel, ITvInputManager service, int userId, int seq, SparseArray<SessionCallbackRecord> sessionCallbackRecordMap) {
            this.mHandler = new InputEventHandler(Looper.getMainLooper());
            this.mPendingEventPool = new SimplePool(20);
            this.mPendingEvents = new SparseArray(20);
            this.mMetadataLock = new Object();
            this.mAudioTracks = new ArrayList();
            this.mVideoTracks = new ArrayList();
            this.mSubtitleTracks = new ArrayList();
            this.mToken = token;
            this.mChannel = channel;
            this.mService = service;
            this.mUserId = userId;
            this.mSeq = seq;
            this.mSessionCallbackRecordMap = sessionCallbackRecordMap;
        }

        public void release() {
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.releaseSession(this.mToken, this.mUserId);
                releaseInternal();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        void setMain() {
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.setMainSession(this.mToken, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void setSurface(Surface surface) {
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.setSurface(this.mToken, surface, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void dispatchSurfaceChanged(int format, int width, int height) {
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.dispatchSurfaceChanged(this.mToken, format, width, height, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void setStreamVolume(float volume) {
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
            } else if (volume < 0.0f || volume > 1.0f) {
                try {
                    throw new IllegalArgumentException("volume should be between 0.0f and 1.0f");
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                this.mService.setVolume(this.mToken, volume, this.mUserId);
            }
        }

        public void tune(Uri channelUri) {
            tune(channelUri, null);
        }

        public void tune(Uri channelUri, Bundle params) {
            Preconditions.checkNotNull(channelUri);
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            synchronized (this.mMetadataLock) {
                this.mAudioTracks.clear();
                this.mVideoTracks.clear();
                this.mSubtitleTracks.clear();
                this.mSelectedAudioTrackId = null;
                this.mSelectedVideoTrackId = null;
                this.mSelectedSubtitleTrackId = null;
                this.mVideoWidth = 0;
                this.mVideoHeight = 0;
            }
            try {
                this.mService.tune(this.mToken, channelUri, params, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void setCaptionEnabled(boolean enabled) {
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.setCaptionEnabled(this.mToken, enabled, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void selectTrack(int r6, java.lang.String r7) {
            /*
            r5 = this;
            r2 = r5.mMetadataLock;
            monitor-enter(r2);
            if (r6 != 0) goto L_0x0029;
        L_0x0005:
            if (r7 == 0) goto L_0x0094;
        L_0x0007:
            r1 = r5.mAudioTracks;	 Catch:{ all -> 0x0050 }
            r1 = r5.containsTrack(r1, r7);	 Catch:{ all -> 0x0050 }
            if (r1 != 0) goto L_0x0094;
        L_0x000f:
            r1 = "TvInputManager";
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0050 }
            r3.<init>();	 Catch:{ all -> 0x0050 }
            r4 = "Invalid audio trackId: ";
            r3 = r3.append(r4);	 Catch:{ all -> 0x0050 }
            r3 = r3.append(r7);	 Catch:{ all -> 0x0050 }
            r3 = r3.toString();	 Catch:{ all -> 0x0050 }
            android.util.Log.w(r1, r3);	 Catch:{ all -> 0x0050 }
            monitor-exit(r2);	 Catch:{ all -> 0x0050 }
        L_0x0028:
            return;
        L_0x0029:
            r1 = 1;
            if (r6 != r1) goto L_0x0053;
        L_0x002c:
            if (r7 == 0) goto L_0x0094;
        L_0x002e:
            r1 = r5.mVideoTracks;	 Catch:{ all -> 0x0050 }
            r1 = r5.containsTrack(r1, r7);	 Catch:{ all -> 0x0050 }
            if (r1 != 0) goto L_0x0094;
        L_0x0036:
            r1 = "TvInputManager";
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0050 }
            r3.<init>();	 Catch:{ all -> 0x0050 }
            r4 = "Invalid video trackId: ";
            r3 = r3.append(r4);	 Catch:{ all -> 0x0050 }
            r3 = r3.append(r7);	 Catch:{ all -> 0x0050 }
            r3 = r3.toString();	 Catch:{ all -> 0x0050 }
            android.util.Log.w(r1, r3);	 Catch:{ all -> 0x0050 }
            monitor-exit(r2);	 Catch:{ all -> 0x0050 }
            goto L_0x0028;
        L_0x0050:
            r1 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x0050 }
            throw r1;
        L_0x0053:
            r1 = 2;
            if (r6 != r1) goto L_0x007a;
        L_0x0056:
            if (r7 == 0) goto L_0x0094;
        L_0x0058:
            r1 = r5.mSubtitleTracks;	 Catch:{ all -> 0x0050 }
            r1 = r5.containsTrack(r1, r7);	 Catch:{ all -> 0x0050 }
            if (r1 != 0) goto L_0x0094;
        L_0x0060:
            r1 = "TvInputManager";
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0050 }
            r3.<init>();	 Catch:{ all -> 0x0050 }
            r4 = "Invalid subtitle trackId: ";
            r3 = r3.append(r4);	 Catch:{ all -> 0x0050 }
            r3 = r3.append(r7);	 Catch:{ all -> 0x0050 }
            r3 = r3.toString();	 Catch:{ all -> 0x0050 }
            android.util.Log.w(r1, r3);	 Catch:{ all -> 0x0050 }
            monitor-exit(r2);	 Catch:{ all -> 0x0050 }
            goto L_0x0028;
        L_0x007a:
            r1 = new java.lang.IllegalArgumentException;	 Catch:{ all -> 0x0050 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0050 }
            r3.<init>();	 Catch:{ all -> 0x0050 }
            r4 = "invalid type: ";
            r3 = r3.append(r4);	 Catch:{ all -> 0x0050 }
            r3 = r3.append(r6);	 Catch:{ all -> 0x0050 }
            r3 = r3.toString();	 Catch:{ all -> 0x0050 }
            r1.<init>(r3);	 Catch:{ all -> 0x0050 }
            throw r1;	 Catch:{ all -> 0x0050 }
        L_0x0094:
            monitor-exit(r2);	 Catch:{ all -> 0x0050 }
            r1 = r5.mToken;
            if (r1 != 0) goto L_0x00a1;
        L_0x0099:
            r1 = "TvInputManager";
            r2 = "The session has been already released";
            android.util.Log.w(r1, r2);
            goto L_0x0028;
        L_0x00a1:
            r1 = r5.mService;	 Catch:{ RemoteException -> 0x00ac }
            r2 = r5.mToken;	 Catch:{ RemoteException -> 0x00ac }
            r3 = r5.mUserId;	 Catch:{ RemoteException -> 0x00ac }
            r1.selectTrack(r2, r6, r7, r3);	 Catch:{ RemoteException -> 0x00ac }
            goto L_0x0028;
        L_0x00ac:
            r0 = move-exception;
            r1 = new java.lang.RuntimeException;
            r1.<init>(r0);
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputManager.Session.selectTrack(int, java.lang.String):void");
        }

        private boolean containsTrack(List<TvTrackInfo> tracks, String trackId) {
            for (TvTrackInfo track : tracks) {
                if (track.getId().equals(trackId)) {
                    return true;
                }
            }
            return false;
        }

        public List<TvTrackInfo> getTracks(int type) {
            List<TvTrackInfo> list = null;
            synchronized (this.mMetadataLock) {
                if (type == 0) {
                    if (this.mAudioTracks == null) {
                    } else {
                        list = new ArrayList(this.mAudioTracks);
                    }
                } else if (type == 1) {
                    if (this.mVideoTracks == null) {
                    } else {
                        list = new ArrayList(this.mVideoTracks);
                    }
                } else if (type != 2) {
                    throw new IllegalArgumentException("invalid type: " + type);
                } else if (this.mSubtitleTracks == null) {
                } else {
                    list = new ArrayList(this.mSubtitleTracks);
                }
                return list;
            }
        }

        public String getSelectedTrack(int type) {
            synchronized (this.mMetadataLock) {
                String str;
                if (type == 0) {
                    str = this.mSelectedAudioTrackId;
                } else if (type == 1) {
                    str = this.mSelectedVideoTrackId;
                } else if (type == 2) {
                    str = this.mSelectedSubtitleTrackId;
                } else {
                    throw new IllegalArgumentException("invalid type: " + type);
                }
                return str;
            }
        }

        boolean updateTracks(List<TvTrackInfo> tracks) {
            boolean z = true;
            synchronized (this.mMetadataLock) {
                this.mAudioTracks.clear();
                this.mVideoTracks.clear();
                this.mSubtitleTracks.clear();
                for (TvTrackInfo track : tracks) {
                    if (track.getType() == 0) {
                        this.mAudioTracks.add(track);
                    } else if (track.getType() == 1) {
                        this.mVideoTracks.add(track);
                    } else if (track.getType() == 2) {
                        this.mSubtitleTracks.add(track);
                    }
                }
                if (this.mAudioTracks.isEmpty() && this.mVideoTracks.isEmpty() && this.mSubtitleTracks.isEmpty()) {
                    z = false;
                }
            }
            return z;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        boolean updateTrackSelection(int r4, java.lang.String r5) {
            /*
            r3 = this;
            r0 = 1;
            r1 = r3.mMetadataLock;
            monitor-enter(r1);
            if (r4 != 0) goto L_0x0012;
        L_0x0006:
            r2 = r3.mSelectedAudioTrackId;	 Catch:{ all -> 0x0020 }
            r2 = android.text.TextUtils.equals(r5, r2);	 Catch:{ all -> 0x0020 }
            if (r2 != 0) goto L_0x0012;
        L_0x000e:
            r3.mSelectedAudioTrackId = r5;	 Catch:{ all -> 0x0020 }
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
        L_0x0011:
            return r0;
        L_0x0012:
            if (r4 != r0) goto L_0x0023;
        L_0x0014:
            r2 = r3.mSelectedVideoTrackId;	 Catch:{ all -> 0x0020 }
            r2 = android.text.TextUtils.equals(r5, r2);	 Catch:{ all -> 0x0020 }
            if (r2 != 0) goto L_0x0023;
        L_0x001c:
            r3.mSelectedVideoTrackId = r5;	 Catch:{ all -> 0x0020 }
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            goto L_0x0011;
        L_0x0020:
            r0 = move-exception;
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            throw r0;
        L_0x0023:
            r2 = 2;
            if (r4 != r2) goto L_0x0032;
        L_0x0026:
            r2 = r3.mSelectedSubtitleTrackId;	 Catch:{ all -> 0x0020 }
            r2 = android.text.TextUtils.equals(r5, r2);	 Catch:{ all -> 0x0020 }
            if (r2 != 0) goto L_0x0032;
        L_0x002e:
            r3.mSelectedSubtitleTrackId = r5;	 Catch:{ all -> 0x0020 }
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            goto L_0x0011;
        L_0x0032:
            monitor-exit(r1);	 Catch:{ all -> 0x0020 }
            r0 = 0;
            goto L_0x0011;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputManager.Session.updateTrackSelection(int, java.lang.String):boolean");
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        android.media.tv.TvTrackInfo getVideoTrackToNotify() {
            /*
            r7 = this;
            r5 = r7.mMetadataLock;
            monitor-enter(r5);
            r4 = r7.mVideoTracks;	 Catch:{ all -> 0x0046 }
            r4 = r4.isEmpty();	 Catch:{ all -> 0x0046 }
            if (r4 != 0) goto L_0x0043;
        L_0x000b:
            r4 = r7.mSelectedVideoTrackId;	 Catch:{ all -> 0x0046 }
            if (r4 == 0) goto L_0x0043;
        L_0x000f:
            r4 = r7.mVideoTracks;	 Catch:{ all -> 0x0046 }
            r0 = r4.iterator();	 Catch:{ all -> 0x0046 }
        L_0x0015:
            r4 = r0.hasNext();	 Catch:{ all -> 0x0046 }
            if (r4 == 0) goto L_0x0043;
        L_0x001b:
            r1 = r0.next();	 Catch:{ all -> 0x0046 }
            r1 = (android.media.tv.TvTrackInfo) r1;	 Catch:{ all -> 0x0046 }
            r4 = r1.getId();	 Catch:{ all -> 0x0046 }
            r6 = r7.mSelectedVideoTrackId;	 Catch:{ all -> 0x0046 }
            r4 = r4.equals(r6);	 Catch:{ all -> 0x0046 }
            if (r4 == 0) goto L_0x0015;
        L_0x002d:
            r3 = r1.getVideoWidth();	 Catch:{ all -> 0x0046 }
            r2 = r1.getVideoHeight();	 Catch:{ all -> 0x0046 }
            r4 = r7.mVideoWidth;	 Catch:{ all -> 0x0046 }
            if (r4 != r3) goto L_0x003d;
        L_0x0039:
            r4 = r7.mVideoHeight;	 Catch:{ all -> 0x0046 }
            if (r4 == r2) goto L_0x0015;
        L_0x003d:
            r7.mVideoWidth = r3;	 Catch:{ all -> 0x0046 }
            r7.mVideoHeight = r2;	 Catch:{ all -> 0x0046 }
            monitor-exit(r5);	 Catch:{ all -> 0x0046 }
        L_0x0042:
            return r1;
        L_0x0043:
            monitor-exit(r5);	 Catch:{ all -> 0x0046 }
            r1 = 0;
            goto L_0x0042;
        L_0x0046:
            r4 = move-exception;
            monitor-exit(r5);	 Catch:{ all -> 0x0046 }
            throw r4;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputManager.Session.getVideoTrackToNotify():android.media.tv.TvTrackInfo");
        }

        void timeShiftPause() {
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.timeShiftPause(this.mToken, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        void timeShiftResume() {
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.timeShiftResume(this.mToken, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        void timeShiftSeekTo(long timeMs) {
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.timeShiftSeekTo(this.mToken, timeMs, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        void timeShiftSetPlaybackParams(PlaybackParams params) {
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.timeShiftSetPlaybackParams(this.mToken, params, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        void timeShiftEnablePositionTracking(boolean enable) {
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.timeShiftEnablePositionTracking(this.mToken, enable, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public void sendAppPrivateCommand(String action, Bundle data) {
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.sendAppPrivateCommand(this.mToken, action, data, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        void createOverlayView(View view, Rect frame) {
            Preconditions.checkNotNull(view);
            Preconditions.checkNotNull(frame);
            if (view.getWindowToken() == null) {
                throw new IllegalStateException("view must be attached to a window");
            } else if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
            } else {
                try {
                    this.mService.createOverlayView(this.mToken, view.getWindowToken(), frame, this.mUserId);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        void relayoutOverlayView(Rect frame) {
            Preconditions.checkNotNull(frame);
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.relayoutOverlayView(this.mToken, frame, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        void removeOverlayView() {
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.removeOverlayView(this.mToken, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        void unblockContent(TvContentRating unblockedRating) {
            Preconditions.checkNotNull(unblockedRating);
            if (this.mToken == null) {
                Log.w(TvInputManager.TAG, "The session has been already released");
                return;
            }
            try {
                this.mService.unblockContent(this.mToken, unblockedRating.flattenToString(), this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        public int dispatchInputEvent(InputEvent event, Object token, FinishedInputEventCallback callback, Handler handler) {
            int i;
            Preconditions.checkNotNull(event);
            Preconditions.checkNotNull(callback);
            Preconditions.checkNotNull(handler);
            synchronized (this.mHandler) {
                if (this.mChannel == null) {
                    i = 0;
                } else {
                    PendingEvent p = obtainPendingEventLocked(event, token, callback, handler);
                    if (Looper.myLooper() == Looper.getMainLooper()) {
                        i = sendInputEventOnMainLooperLocked(p);
                    } else {
                        Message msg = this.mHandler.obtainMessage(1, p);
                        msg.setAsynchronous(true);
                        this.mHandler.sendMessage(msg);
                        i = -1;
                    }
                }
            }
            return i;
        }

        private void sendInputEventAndReportResultOnMainLooper(PendingEvent p) {
            synchronized (this.mHandler) {
                if (sendInputEventOnMainLooperLocked(p) == -1) {
                    return;
                }
                invokeFinishedInputEventCallback(p, false);
            }
        }

        private int sendInputEventOnMainLooperLocked(PendingEvent p) {
            if (this.mChannel != null) {
                if (this.mSender == null) {
                    this.mSender = new TvInputEventSender(this.mChannel, this.mHandler.getLooper());
                }
                InputEvent event = p.mEvent;
                int seq = event.getSequenceNumber();
                if (this.mSender.sendInputEvent(seq, event)) {
                    this.mPendingEvents.put(seq, p);
                    Message msg = this.mHandler.obtainMessage(2, p);
                    msg.setAsynchronous(true);
                    this.mHandler.sendMessageDelayed(msg, INPUT_SESSION_NOT_RESPONDING_TIMEOUT);
                    return -1;
                }
                Log.w(TvInputManager.TAG, "Unable to send input event to session: " + this.mToken + " dropping:" + event);
            }
            return 0;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        void finishedInputEvent(int r7, boolean r8, boolean r9) {
            /*
            r6 = this;
            r3 = r6.mHandler;
            monitor-enter(r3);
            r2 = r6.mPendingEvents;	 Catch:{ all -> 0x0042 }
            r0 = r2.indexOfKey(r7);	 Catch:{ all -> 0x0042 }
            if (r0 >= 0) goto L_0x000d;
        L_0x000b:
            monitor-exit(r3);	 Catch:{ all -> 0x0042 }
        L_0x000c:
            return;
        L_0x000d:
            r2 = r6.mPendingEvents;	 Catch:{ all -> 0x0042 }
            r1 = r2.valueAt(r0);	 Catch:{ all -> 0x0042 }
            r1 = (android.media.tv.TvInputManager.Session.PendingEvent) r1;	 Catch:{ all -> 0x0042 }
            r2 = r6.mPendingEvents;	 Catch:{ all -> 0x0042 }
            r2.removeAt(r0);	 Catch:{ all -> 0x0042 }
            if (r9 == 0) goto L_0x003b;
        L_0x001c:
            r2 = "TvInputManager";
            r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0042 }
            r4.<init>();	 Catch:{ all -> 0x0042 }
            r5 = "Timeout waiting for session to handle input event after 2500 ms: ";
            r4 = r4.append(r5);	 Catch:{ all -> 0x0042 }
            r5 = r6.mToken;	 Catch:{ all -> 0x0042 }
            r4 = r4.append(r5);	 Catch:{ all -> 0x0042 }
            r4 = r4.toString();	 Catch:{ all -> 0x0042 }
            android.util.Log.w(r2, r4);	 Catch:{ all -> 0x0042 }
        L_0x0036:
            monitor-exit(r3);	 Catch:{ all -> 0x0042 }
            r6.invokeFinishedInputEventCallback(r1, r8);
            goto L_0x000c;
        L_0x003b:
            r2 = r6.mHandler;	 Catch:{ all -> 0x0042 }
            r4 = 2;
            r2.removeMessages(r4, r1);	 Catch:{ all -> 0x0042 }
            goto L_0x0036;
        L_0x0042:
            r2 = move-exception;
            monitor-exit(r3);	 Catch:{ all -> 0x0042 }
            throw r2;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputManager.Session.finishedInputEvent(int, boolean, boolean):void");
        }

        void invokeFinishedInputEventCallback(PendingEvent p, boolean handled) {
            p.mHandled = handled;
            if (p.mEventHandler.getLooper().isCurrentThread()) {
                p.run();
                return;
            }
            Message msg = Message.obtain(p.mEventHandler, (Runnable) p);
            msg.setAsynchronous(true);
            msg.sendToTarget();
        }

        private void flushPendingEventsLocked() {
            this.mHandler.removeMessages(3);
            int count = this.mPendingEvents.size();
            for (int i = 0; i < count; i++) {
                Message msg = this.mHandler.obtainMessage(3, this.mPendingEvents.keyAt(i), 0);
                msg.setAsynchronous(true);
                msg.sendToTarget();
            }
        }

        private PendingEvent obtainPendingEventLocked(InputEvent event, Object token, FinishedInputEventCallback callback, Handler handler) {
            PendingEvent p = (PendingEvent) this.mPendingEventPool.acquire();
            if (p == null) {
                p = new PendingEvent();
            }
            p.mEvent = event;
            p.mEventToken = token;
            p.mCallback = callback;
            p.mEventHandler = handler;
            return p;
        }

        private void recyclePendingEventLocked(PendingEvent p) {
            p.recycle();
            this.mPendingEventPool.release(p);
        }

        IBinder getToken() {
            return this.mToken;
        }

        private void releaseInternal() {
            this.mToken = null;
            synchronized (this.mHandler) {
                if (this.mChannel != null) {
                    if (this.mSender != null) {
                        flushPendingEventsLocked();
                        this.mSender.dispose();
                        this.mSender = null;
                    }
                    this.mChannel.dispose();
                    this.mChannel = null;
                }
            }
            synchronized (this.mSessionCallbackRecordMap) {
                this.mSessionCallbackRecordMap.remove(this.mSeq);
            }
        }
    }

    public static abstract class SessionCallback {
        public void onSessionCreated(Session session) {
        }

        public void onSessionReleased(Session session) {
        }

        public void onChannelRetuned(Session session, Uri channelUri) {
        }

        public void onTracksChanged(Session session, List<TvTrackInfo> list) {
        }

        public void onTrackSelected(Session session, int type, String trackId) {
        }

        public void onVideoSizeChanged(Session session, int width, int height) {
        }

        public void onVideoAvailable(Session session) {
        }

        public void onVideoUnavailable(Session session, int reason) {
        }

        public void onContentAllowed(Session session) {
        }

        public void onContentBlocked(Session session, TvContentRating rating) {
        }

        public void onLayoutSurface(Session session, int left, int top, int right, int bottom) {
        }

        public void onSessionEvent(Session session, String eventType, Bundle eventArgs) {
        }

        public void onTimeShiftStatusChanged(Session session, int status) {
        }

        public void onTimeShiftStartPositionChanged(Session session, long timeMs) {
        }

        public void onTimeShiftCurrentPositionChanged(Session session, long timeMs) {
        }
    }

    private static final class SessionCallbackRecord {
        private final Handler mHandler;
        private Session mSession;
        private final SessionCallback mSessionCallback;

        SessionCallbackRecord(SessionCallback sessionCallback, Handler handler) {
            this.mSessionCallback = sessionCallback;
            this.mHandler = handler;
        }

        void postSessionCreated(final Session session) {
            this.mSession = session;
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onSessionCreated(session);
                }
            });
        }

        void postSessionReleased() {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onSessionReleased(SessionCallbackRecord.this.mSession);
                }
            });
        }

        void postChannelRetuned(final Uri channelUri) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onChannelRetuned(SessionCallbackRecord.this.mSession, channelUri);
                }
            });
        }

        void postTracksChanged(final List<TvTrackInfo> tracks) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onTracksChanged(SessionCallbackRecord.this.mSession, tracks);
                }
            });
        }

        void postTrackSelected(final int type, final String trackId) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onTrackSelected(SessionCallbackRecord.this.mSession, type, trackId);
                }
            });
        }

        void postVideoSizeChanged(final int width, final int height) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onVideoSizeChanged(SessionCallbackRecord.this.mSession, width, height);
                }
            });
        }

        void postVideoAvailable() {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onVideoAvailable(SessionCallbackRecord.this.mSession);
                }
            });
        }

        void postVideoUnavailable(final int reason) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onVideoUnavailable(SessionCallbackRecord.this.mSession, reason);
                }
            });
        }

        void postContentAllowed() {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onContentAllowed(SessionCallbackRecord.this.mSession);
                }
            });
        }

        void postContentBlocked(final TvContentRating rating) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onContentBlocked(SessionCallbackRecord.this.mSession, rating);
                }
            });
        }

        void postLayoutSurface(int left, int top, int right, int bottom) {
            final int i = left;
            final int i2 = top;
            final int i3 = right;
            final int i4 = bottom;
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onLayoutSurface(SessionCallbackRecord.this.mSession, i, i2, i3, i4);
                }
            });
        }

        void postSessionEvent(final String eventType, final Bundle eventArgs) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onSessionEvent(SessionCallbackRecord.this.mSession, eventType, eventArgs);
                }
            });
        }

        void postTimeShiftStatusChanged(final int status) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onTimeShiftStatusChanged(SessionCallbackRecord.this.mSession, status);
                }
            });
        }

        void postTimeShiftStartPositionChanged(final long timeMs) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onTimeShiftStartPositionChanged(SessionCallbackRecord.this.mSession, timeMs);
                }
            });
        }

        void postTimeShiftCurrentPositionChanged(final long timeMs) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SessionCallbackRecord.this.mSessionCallback.onTimeShiftCurrentPositionChanged(SessionCallbackRecord.this.mSession, timeMs);
                }
            });
        }
    }

    public static abstract class TvInputCallback {
        public void onInputStateChanged(String inputId, int state) {
        }

        public void onInputAdded(String inputId) {
        }

        public void onInputRemoved(String inputId) {
        }

        public void onInputUpdated(String inputId) {
        }
    }

    private static final class TvInputCallbackRecord {
        private final TvInputCallback mCallback;
        private final Handler mHandler;

        public TvInputCallbackRecord(TvInputCallback callback, Handler handler) {
            this.mCallback = callback;
            this.mHandler = handler;
        }

        public TvInputCallback getCallback() {
            return this.mCallback;
        }

        public void postInputStateChanged(final String inputId, final int state) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    TvInputCallbackRecord.this.mCallback.onInputStateChanged(inputId, state);
                }
            });
        }

        public void postInputAdded(final String inputId) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    TvInputCallbackRecord.this.mCallback.onInputAdded(inputId);
                }
            });
        }

        public void postInputRemoved(final String inputId) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    TvInputCallbackRecord.this.mCallback.onInputRemoved(inputId);
                }
            });
        }

        public void postInputUpdated(final String inputId) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    TvInputCallbackRecord.this.mCallback.onInputUpdated(inputId);
                }
            });
        }
    }

    public TvInputManager(ITvInputManager service, int userId) {
        this.mService = service;
        this.mUserId = userId;
        this.mClient = new Stub() {
            public void onSessionCreated(String inputId, IBinder token, InputChannel channel, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        Log.e(TvInputManager.TAG, "Callback not found for " + token);
                        return;
                    }
                    Session session = null;
                    if (token != null) {
                        session = new Session(token, channel, TvInputManager.this.mService, TvInputManager.this.mUserId, seq, TvInputManager.this.mSessionCallbackRecordMap);
                    }
                    record.postSessionCreated(session);
                }
            }

            public void onSessionReleased(int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    TvInputManager.this.mSessionCallbackRecordMap.delete(seq);
                    if (record == null) {
                        Log.e(TvInputManager.TAG, "Callback not found for seq:" + seq);
                        return;
                    }
                    record.mSession.releaseInternal();
                    record.postSessionReleased();
                }
            }

            public void onChannelRetuned(Uri channelUri, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        Log.e(TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postChannelRetuned(channelUri);
                }
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onTracksChanged(java.util.List<android.media.tv.TvTrackInfo> r6, int r7) {
                /*
                r5 = this;
                r1 = android.media.tv.TvInputManager.this;
                r2 = r1.mSessionCallbackRecordMap;
                monitor-enter(r2);
                r1 = android.media.tv.TvInputManager.this;	 Catch:{ all -> 0x0041 }
                r1 = r1.mSessionCallbackRecordMap;	 Catch:{ all -> 0x0041 }
                r0 = r1.get(r7);	 Catch:{ all -> 0x0041 }
                r0 = (android.media.tv.TvInputManager.SessionCallbackRecord) r0;	 Catch:{ all -> 0x0041 }
                if (r0 != 0) goto L_0x002f;
            L_0x0015:
                r1 = "TvInputManager";
                r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0041 }
                r3.<init>();	 Catch:{ all -> 0x0041 }
                r4 = "Callback not found for seq ";
                r3 = r3.append(r4);	 Catch:{ all -> 0x0041 }
                r3 = r3.append(r7);	 Catch:{ all -> 0x0041 }
                r3 = r3.toString();	 Catch:{ all -> 0x0041 }
                android.util.Log.e(r1, r3);	 Catch:{ all -> 0x0041 }
                monitor-exit(r2);	 Catch:{ all -> 0x0041 }
            L_0x002e:
                return;
            L_0x002f:
                r1 = r0.mSession;	 Catch:{ all -> 0x0041 }
                r1 = r1.updateTracks(r6);	 Catch:{ all -> 0x0041 }
                if (r1 == 0) goto L_0x003f;
            L_0x0039:
                r0.postTracksChanged(r6);	 Catch:{ all -> 0x0041 }
                r5.postVideoSizeChangedIfNeededLocked(r0);	 Catch:{ all -> 0x0041 }
            L_0x003f:
                monitor-exit(r2);	 Catch:{ all -> 0x0041 }
                goto L_0x002e;
            L_0x0041:
                r1 = move-exception;
                monitor-exit(r2);	 Catch:{ all -> 0x0041 }
                throw r1;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputManager.1.onTracksChanged(java.util.List, int):void");
            }

            /* JADX WARNING: inconsistent code. */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void onTrackSelected(int r6, java.lang.String r7, int r8) {
                /*
                r5 = this;
                r1 = android.media.tv.TvInputManager.this;
                r2 = r1.mSessionCallbackRecordMap;
                monitor-enter(r2);
                r1 = android.media.tv.TvInputManager.this;	 Catch:{ all -> 0x0041 }
                r1 = r1.mSessionCallbackRecordMap;	 Catch:{ all -> 0x0041 }
                r0 = r1.get(r8);	 Catch:{ all -> 0x0041 }
                r0 = (android.media.tv.TvInputManager.SessionCallbackRecord) r0;	 Catch:{ all -> 0x0041 }
                if (r0 != 0) goto L_0x002f;
            L_0x0015:
                r1 = "TvInputManager";
                r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0041 }
                r3.<init>();	 Catch:{ all -> 0x0041 }
                r4 = "Callback not found for seq ";
                r3 = r3.append(r4);	 Catch:{ all -> 0x0041 }
                r3 = r3.append(r8);	 Catch:{ all -> 0x0041 }
                r3 = r3.toString();	 Catch:{ all -> 0x0041 }
                android.util.Log.e(r1, r3);	 Catch:{ all -> 0x0041 }
                monitor-exit(r2);	 Catch:{ all -> 0x0041 }
            L_0x002e:
                return;
            L_0x002f:
                r1 = r0.mSession;	 Catch:{ all -> 0x0041 }
                r1 = r1.updateTrackSelection(r6, r7);	 Catch:{ all -> 0x0041 }
                if (r1 == 0) goto L_0x003f;
            L_0x0039:
                r0.postTrackSelected(r6, r7);	 Catch:{ all -> 0x0041 }
                r5.postVideoSizeChangedIfNeededLocked(r0);	 Catch:{ all -> 0x0041 }
            L_0x003f:
                monitor-exit(r2);	 Catch:{ all -> 0x0041 }
                goto L_0x002e;
            L_0x0041:
                r1 = move-exception;
                monitor-exit(r2);	 Catch:{ all -> 0x0041 }
                throw r1;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.media.tv.TvInputManager.1.onTrackSelected(int, java.lang.String, int):void");
            }

            private void postVideoSizeChangedIfNeededLocked(SessionCallbackRecord record) {
                TvTrackInfo track = record.mSession.getVideoTrackToNotify();
                if (track != null) {
                    record.postVideoSizeChanged(track.getVideoWidth(), track.getVideoHeight());
                }
            }

            public void onVideoAvailable(int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        Log.e(TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postVideoAvailable();
                }
            }

            public void onVideoUnavailable(int reason, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        Log.e(TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postVideoUnavailable(reason);
                }
            }

            public void onContentAllowed(int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        Log.e(TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postContentAllowed();
                }
            }

            public void onContentBlocked(String rating, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        Log.e(TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postContentBlocked(TvContentRating.unflattenFromString(rating));
                }
            }

            public void onLayoutSurface(int left, int top, int right, int bottom, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        Log.e(TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postLayoutSurface(left, top, right, bottom);
                }
            }

            public void onSessionEvent(String eventType, Bundle eventArgs, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        Log.e(TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postSessionEvent(eventType, eventArgs);
                }
            }

            public void onTimeShiftStatusChanged(int status, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        Log.e(TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postTimeShiftStatusChanged(status);
                }
            }

            public void onTimeShiftStartPositionChanged(long timeMs, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        Log.e(TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postTimeShiftStartPositionChanged(timeMs);
                }
            }

            public void onTimeShiftCurrentPositionChanged(long timeMs, int seq) {
                synchronized (TvInputManager.this.mSessionCallbackRecordMap) {
                    SessionCallbackRecord record = (SessionCallbackRecord) TvInputManager.this.mSessionCallbackRecordMap.get(seq);
                    if (record == null) {
                        Log.e(TvInputManager.TAG, "Callback not found for seq " + seq);
                        return;
                    }
                    record.postTimeShiftCurrentPositionChanged(timeMs);
                }
            }
        };
        ITvInputManagerCallback managerCallback = new ITvInputManagerCallback.Stub() {
            public void onInputStateChanged(String inputId, int state) {
                synchronized (TvInputManager.this.mLock) {
                    TvInputManager.this.mStateMap.put(inputId, Integer.valueOf(state));
                    for (TvInputCallbackRecord record : TvInputManager.this.mCallbackRecords) {
                        record.postInputStateChanged(inputId, state);
                    }
                }
            }

            public void onInputAdded(String inputId) {
                synchronized (TvInputManager.this.mLock) {
                    TvInputManager.this.mStateMap.put(inputId, Integer.valueOf(0));
                    for (TvInputCallbackRecord record : TvInputManager.this.mCallbackRecords) {
                        record.postInputAdded(inputId);
                    }
                }
            }

            public void onInputRemoved(String inputId) {
                synchronized (TvInputManager.this.mLock) {
                    TvInputManager.this.mStateMap.remove(inputId);
                    for (TvInputCallbackRecord record : TvInputManager.this.mCallbackRecords) {
                        record.postInputRemoved(inputId);
                    }
                }
            }

            public void onInputUpdated(String inputId) {
                synchronized (TvInputManager.this.mLock) {
                    for (TvInputCallbackRecord record : TvInputManager.this.mCallbackRecords) {
                        record.postInputUpdated(inputId);
                    }
                }
            }
        };
        try {
            if (this.mService != null) {
                this.mService.registerCallback(managerCallback, this.mUserId);
                List<TvInputInfo> infos = this.mService.getTvInputList(this.mUserId);
                synchronized (this.mLock) {
                    for (TvInputInfo info : infos) {
                        String inputId = info.getId();
                        this.mStateMap.put(inputId, Integer.valueOf(this.mService.getTvInputState(inputId, this.mUserId)));
                    }
                }
            }
        } catch (RemoteException e) {
            Log.e(TAG, "TvInputManager initialization failed", e);
        }
    }

    public List<TvInputInfo> getTvInputList() {
        try {
            return this.mService.getTvInputList(this.mUserId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public TvInputInfo getTvInputInfo(String inputId) {
        Preconditions.checkNotNull(inputId);
        try {
            return this.mService.getTvInputInfo(inputId, this.mUserId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public int getInputState(String inputId) {
        int i;
        Preconditions.checkNotNull(inputId);
        synchronized (this.mLock) {
            Integer state = (Integer) this.mStateMap.get(inputId);
            if (state == null) {
                Log.w(TAG, "Unrecognized input ID: " + inputId);
                i = 2;
            } else {
                i = state.intValue();
            }
        }
        return i;
    }

    public void registerCallback(TvInputCallback callback, Handler handler) {
        Preconditions.checkNotNull(callback);
        Preconditions.checkNotNull(handler);
        synchronized (this.mLock) {
            this.mCallbackRecords.add(new TvInputCallbackRecord(callback, handler));
        }
    }

    public void unregisterCallback(TvInputCallback callback) {
        Preconditions.checkNotNull(callback);
        synchronized (this.mLock) {
            Iterator<TvInputCallbackRecord> it = this.mCallbackRecords.iterator();
            while (it.hasNext()) {
                if (((TvInputCallbackRecord) it.next()).getCallback() == callback) {
                    it.remove();
                    break;
                }
            }
        }
    }

    public boolean isParentalControlsEnabled() {
        try {
            return this.mService.isParentalControlsEnabled(this.mUserId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void setParentalControlsEnabled(boolean enabled) {
        try {
            this.mService.setParentalControlsEnabled(enabled, this.mUserId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isRatingBlocked(TvContentRating rating) {
        Preconditions.checkNotNull(rating);
        try {
            return this.mService.isRatingBlocked(rating.flattenToString(), this.mUserId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TvContentRating> getBlockedRatings() {
        try {
            List<TvContentRating> ratings = new ArrayList();
            for (String rating : this.mService.getBlockedRatings(this.mUserId)) {
                ratings.add(TvContentRating.unflattenFromString(rating));
            }
            return ratings;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void addBlockedRating(TvContentRating rating) {
        Preconditions.checkNotNull(rating);
        try {
            this.mService.addBlockedRating(rating.flattenToString(), this.mUserId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeBlockedRating(TvContentRating rating) {
        Preconditions.checkNotNull(rating);
        try {
            this.mService.removeBlockedRating(rating.flattenToString(), this.mUserId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TvContentRatingSystemInfo> getTvContentRatingSystemList() {
        try {
            return this.mService.getTvContentRatingSystemList(this.mUserId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void createSession(String inputId, SessionCallback callback, Handler handler) {
        Preconditions.checkNotNull(inputId);
        Preconditions.checkNotNull(callback);
        Preconditions.checkNotNull(handler);
        SessionCallbackRecord record = new SessionCallbackRecord(callback, handler);
        synchronized (this.mSessionCallbackRecordMap) {
            int seq = this.mNextSeq;
            this.mNextSeq = seq + 1;
            this.mSessionCallbackRecordMap.put(seq, record);
            try {
                this.mService.createSession(this.mClient, inputId, seq, this.mUserId);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public List<TvStreamConfig> getAvailableTvStreamConfigList(String inputId) {
        try {
            return this.mService.getAvailableTvStreamConfigList(inputId, this.mUserId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean captureFrame(String inputId, Surface surface, TvStreamConfig config) {
        try {
            return this.mService.captureFrame(inputId, surface, config, this.mUserId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isSingleSessionActive() {
        try {
            return this.mService.isSingleSessionActive(this.mUserId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<TvInputHardwareInfo> getHardwareList() {
        try {
            return this.mService.getHardwareList();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Hardware acquireTvInputHardware(int deviceId, final HardwareCallback callback, TvInputInfo info) {
        try {
            return new Hardware(this.mService.acquireTvInputHardware(deviceId, new ITvInputHardwareCallback.Stub() {
                public void onReleased() {
                    callback.onReleased();
                }

                public void onStreamConfigChanged(TvStreamConfig[] configs) {
                    callback.onStreamConfigChanged(configs);
                }
            }, info, this.mUserId));
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void releaseTvInputHardware(int deviceId, Hardware hardware) {
        try {
            this.mService.releaseTvInputHardware(deviceId, hardware.getInterface(), this.mUserId);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<DvbDeviceInfo> getDvbDeviceList() {
        try {
            return this.mService.getDvbDeviceList();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public ParcelFileDescriptor openDvbDevice(DvbDeviceInfo info, int device) {
        if (device >= 0 && 2 >= device) {
            return this.mService.openDvbDevice(info, device);
        }
        try {
            throw new IllegalArgumentException("Invalid DVB device: " + device);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
