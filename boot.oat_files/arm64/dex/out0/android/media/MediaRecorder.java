package android.media;

import android.app.ActivityThread;
import android.hardware.Camera;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.UserHandle;
import android.sec.enterprise.EnterpriseDeviceManager;
import android.sec.enterprise.auditlog.AuditLog;
import android.util.Log;
import android.view.Surface;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;

public class MediaRecorder {
    public static final int MEDIA_ERROR_SERVER_DIED = 100;
    public static final int MEDIA_RECORDER_ERROR_UNKNOWN = 1;
    public static final int MEDIA_RECORDER_INFO_MAX_DURATION_REACHED = 800;
    public static final int MEDIA_RECORDER_INFO_MAX_FILESIZE_REACHED = 801;
    public static final int MEDIA_RECORDER_INFO_UNKNOWN = 1;
    public static final int MEDIA_RECORDER_TRACK_INFO_COMPLETION_STATUS = 1000;
    public static final int MEDIA_RECORDER_TRACK_INFO_DATA_KBYTES = 1009;
    public static final int MEDIA_RECORDER_TRACK_INFO_DURATION_MS = 1003;
    public static final int MEDIA_RECORDER_TRACK_INFO_ENCODED_FRAMES = 1005;
    public static final int MEDIA_RECORDER_TRACK_INFO_INITIAL_DELAY_MS = 1007;
    public static final int MEDIA_RECORDER_TRACK_INFO_LIST_END = 2000;
    public static final int MEDIA_RECORDER_TRACK_INFO_LIST_START = 1000;
    public static final int MEDIA_RECORDER_TRACK_INFO_MAX_CHUNK_DUR_MS = 1004;
    public static final int MEDIA_RECORDER_TRACK_INFO_PROGRESS_IN_TIME = 1001;
    public static final int MEDIA_RECORDER_TRACK_INFO_START_OFFSET_MS = 1008;
    public static final int MEDIA_RECORDER_TRACK_INFO_TYPE = 1002;
    public static final int MEDIA_RECORDER_TRACK_INTER_CHUNK_TIME_MS = 1006;
    private static final String TAG = "MediaRecorder";
    private int mAudioSource = -1;
    private EventHandler mEventHandler;
    private FileDescriptor mFd;
    private boolean mIsAudioEnabled = false;
    private boolean mIsVideoRecordEnabled = false;
    private long mNativeContext;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private String mPath;
    private Surface mSurface;

    public final class AppStyle {
        public static final int AUDIO_APP_ASR = 3;
        public static final int AUDIO_APP_CAMCORDER = 2;
        public static final int AUDIO_APP_DEFAULT = 0;
        public static final int AUDIO_APP_VOICE_RECORDER = 1;
        public static final int AUDIO_APP_VOIP_RECORDER = 4;

        private AppStyle() {
        }
    }

    public final class AudioEncoder {
        public static final int AAC = 3;
        public static final int AAC_ELD = 5;
        public static final int AMR_NB = 1;
        public static final int AMR_WB = 2;
        public static final int DEFAULT = 0;
        public static final int EVRC = 10;
        public static final int HE_AAC = 4;
        public static final int LPCM = 12;
        public static final int QCELP = 11;
        public static final int VORBIS = 6;

        private AudioEncoder() {
        }
    }

    public final class AudioSource {
        public static final int AUDIO_SOURCE_INVALID = -1;
        public static final int CAMCORDER = 5;
        public static final int DEFAULT = 0;
        public static final int FM_RX = 9;
        public static final int FM_RX_A2DP = 10;
        public static final int HOTWORD = 1999;
        public static final int MIC = 1;
        public static final int RADIO_TUNER = 1998;
        public static final int REMOTE_SUBMIX = 8;
        public static final int SEC_2MIC_SVOICE_DRIVING = 17;
        public static final int SEC_2MIC_SVOICE_NORMAL = 18;
        public static final int SEC_AUDIOSOURCE_MAX = 23;
        public static final int SEC_BARGEIN_DRIVING = 19;
        public static final int SEC_BEAMFORMING = 21;
        public static final int SEC_CAMCORDER = 20;
        public static final int SEC_HIGH_GAIN_MIC = 13;
        public static final int SEC_LOW_GAIN_MIC = 14;
        public static final int SEC_MULTI_RECORD_FRONT = 15;
        public static final int SEC_MULTI_RECORD_REAR = 16;
        public static final int SEC_PLAYBACK_RECORD = 1001;
        public static final int SEC_VOICE_CALL_FORWARDING = 23;
        public static final int SEC_VOICE_COMMUNICATION = 12;
        public static final int SEC_VOICE_COMMUNICATION_NB = 22;
        public static final int SEC_VOICE_RECOGNITION = 11;
        public static final int VOICE_CALL = 4;
        public static final int VOICE_COMMUNICATION = 7;
        public static final int VOICE_DOWNLINK = 3;
        public static final int VOICE_RECOGNITION = 6;
        public static final int VOICE_UPLINK = 2;

        private AudioSource() {
        }
    }

    private class EventHandler extends Handler {
        private static final int MEDIA_RECORDER_EVENT_ERROR = 1;
        private static final int MEDIA_RECORDER_EVENT_INFO = 2;
        private static final int MEDIA_RECORDER_EVENT_LIST_END = 99;
        private static final int MEDIA_RECORDER_EVENT_LIST_START = 1;
        private static final int MEDIA_RECORDER_TRACK_EVENT_ERROR = 100;
        private static final int MEDIA_RECORDER_TRACK_EVENT_INFO = 101;
        private static final int MEDIA_RECORDER_TRACK_EVENT_LIST_END = 1000;
        private static final int MEDIA_RECORDER_TRACK_EVENT_LIST_START = 100;
        private MediaRecorder mMediaRecorder;

        public EventHandler(MediaRecorder mr, Looper looper) {
            super(looper);
            this.mMediaRecorder = mr;
        }

        public void handleMessage(Message msg) {
            if (this.mMediaRecorder.mNativeContext == 0) {
                Log.w(MediaRecorder.TAG, "mediarecorder went away with unhandled events");
                return;
            }
            switch (msg.what) {
                case 1:
                case 100:
                    if (MediaRecorder.this.mOnErrorListener != null) {
                        MediaRecorder.this.mOnErrorListener.onError(this.mMediaRecorder, msg.arg1, msg.arg2);
                        return;
                    }
                    return;
                case 2:
                case 101:
                    if (MediaRecorder.this.mOnInfoListener != null) {
                        MediaRecorder.this.mOnInfoListener.onInfo(this.mMediaRecorder, msg.arg1, msg.arg2);
                        return;
                    }
                    return;
                default:
                    Log.e(MediaRecorder.TAG, "Unknown message type " + msg.what);
                    return;
            }
        }
    }

    public interface OnErrorListener {
        void onError(MediaRecorder mediaRecorder, int i, int i2);
    }

    public interface OnInfoListener {
        void onInfo(MediaRecorder mediaRecorder, int i, int i2);
    }

    public final class OutputFormat {
        public static final int AAC_ADIF = 5;
        public static final int AAC_ADTS = 6;
        public static final int AMR_NB = 3;
        public static final int AMR_WB = 4;
        public static final int DEFAULT = 0;
        public static final int MPEG_4 = 2;
        public static final int OUTPUT_FORMAT_MPEG2TS = 8;
        public static final int OUTPUT_FORMAT_RTP_AVP = 7;
        public static final int QCP = 20;
        public static final int RAW_AMR = 3;
        public static final int THREE_GPP = 1;
        public static final int WAVE = 21;
        public static final int WEBM = 9;

        private OutputFormat() {
        }
    }

    public final class VideoEncoder {
        public static final int DEFAULT = 0;
        public static final int H263 = 1;
        public static final int H264 = 2;
        public static final int H265 = 5;
        public static final int MPEG_4_SP = 3;
        public static final int VP8 = 4;

        private VideoEncoder() {
        }
    }

    public final class VideoSource {
        public static final int CAMERA = 1;
        public static final int DEFAULT = 0;
        public static final int SURFACE = 2;

        private VideoSource() {
        }
    }

    private native void _prepare() throws IllegalStateException, IOException;

    private native void _setAudioSource(int i) throws IllegalStateException;

    private native void _setOutputFile(FileDescriptor fileDescriptor, long j, long j2) throws IllegalStateException, IOException;

    private final native void native_finalize();

    private static final native void native_init();

    private native void native_reset();

    private final native void native_setInputSurface(Surface surface);

    private final native void native_setup(Object obj, String str, String str2) throws IllegalStateException;

    private native void setParameter(String str);

    public native void _setVideoSource(int i) throws IllegalStateException;

    public native void _start() throws IllegalStateException;

    public native void _stop() throws IllegalStateException;

    public native int getMaxAmplitude() throws IllegalStateException;

    public native Surface getSurface();

    public native void release();

    public native void setAudioEncoder(int i) throws IllegalStateException;

    @Deprecated
    public native void setCamera(Camera camera);

    public native void setMaxDuration(int i) throws IllegalArgumentException;

    public native void setMaxFileSize(long j) throws IllegalArgumentException;

    public native void setOutputFormat(int i) throws IllegalStateException;

    public native void setVideoEncoder(int i) throws IllegalStateException;

    public native void setVideoFrameRate(int i) throws IllegalStateException;

    public native void setVideoSize(int i, int i2) throws IllegalStateException;

    static {
        System.loadLibrary("media_jni");
        native_init();
    }

    public MediaRecorder() {
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
        native_setup(new WeakReference(this), ActivityThread.currentPackageName(), ActivityThread.currentOpPackageName());
    }

    public void setInputSurface(Surface surface) {
        if (surface instanceof PersistentSurface) {
            native_setInputSurface(surface);
            return;
        }
        throw new IllegalArgumentException("not a PersistentSurface");
    }

    public void setPreviewDisplay(Surface sv) {
        this.mSurface = sv;
    }

    public void setAudioSource(int audio_source) throws IllegalStateException {
        if (!(audio_source == 4 || audio_source == 3 || audio_source == 2)) {
            if (checkMicrophoneEnabled()) {
                this.mIsAudioEnabled = true;
            } else {
                Log.d(TAG, "setAudioSource(): checkMicrophoneEnabled = false, Return Directly");
                return;
            }
        }
        this.mAudioSource = audio_source;
        _setAudioSource(audio_source);
    }

    public static final int getAudioSourceMax() {
        return 23;
    }

    public void setVideoSource(int video_source) throws IllegalStateException {
        this.mIsVideoRecordEnabled = true;
        _setVideoSource(video_source);
    }

    public void setProfile(CamcorderProfile profile) {
        setOutputFormat(profile.fileFormat);
        setVideoFrameRate(profile.videoFrameRate);
        setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
        setVideoEncodingBitRate(profile.videoBitRate);
        setVideoEncoder(profile.videoCodec);
        if (profile.quality < 1000 || profile.quality > 1008) {
            setAudioEncodingBitRate(profile.audioBitRate);
            setAudioChannels(profile.audioChannels);
            setAudioSamplingRate(profile.audioSampleRate);
            setAudioEncoder(profile.audioCodec);
        }
    }

    public void setCaptureRate(double fps) {
        setParameter("time-lapse-enable=1");
        setParameter("time-lapse-fps=" + fps);
    }

    public void setOrientationHint(int degrees) {
        if (degrees == 0 || degrees == 90 || degrees == 180 || degrees == 270) {
            setParameter("video-param-rotation-angle-degrees=" + degrees);
            return;
        }
        throw new IllegalArgumentException("Unsupported angle: " + degrees);
    }

    public void setLocation(float latitude, float longitude) {
        int latitudex10000 = (int) (((double) (latitude * SensorManager.LIGHT_OVERCAST)) + 0.5d);
        int longitudex10000 = (int) (((double) (longitude * SensorManager.LIGHT_OVERCAST)) + 0.5d);
        if (latitudex10000 > 900000 || latitudex10000 < -900000) {
            throw new IllegalArgumentException("Latitude: " + latitude + " out of range.");
        } else if (longitudex10000 > 1800000 || longitudex10000 < -1800000) {
            throw new IllegalArgumentException("Longitude: " + longitude + " out of range");
        } else {
            setParameter("param-geotag-latitude=" + latitudex10000);
            setParameter("param-geotag-longitude=" + longitudex10000);
        }
    }

    public void setAudioSamplingRate(int samplingRate) {
        if (samplingRate <= 0) {
            throw new IllegalArgumentException("Audio sampling rate is not positive");
        }
        setParameter("audio-param-sampling-rate=" + samplingRate);
    }

    public void setAudioChannels(int numChannels) {
        if (numChannels <= 0) {
            throw new IllegalArgumentException("Number of channels is not positive");
        }
        setParameter("audio-param-number-of-channels=" + numChannels);
    }

    public void setAudioEncodingBitRate(int bitRate) {
        if (bitRate <= 0) {
            throw new IllegalArgumentException("Audio encoding bit rate is not positive");
        }
        setParameter("audio-param-encoding-bitrate=" + bitRate);
    }

    public void setVideoEncodingBitRate(int bitRate) {
        if (bitRate <= 0) {
            throw new IllegalArgumentException("Video encoding bit rate is not positive");
        }
        setParameter("video-param-encoding-bitrate=" + bitRate);
    }

    public void setAuxiliaryOutputFile(FileDescriptor fd) {
        Log.w(TAG, "setAuxiliaryOutputFile(FileDescriptor) is no longer supported.");
    }

    public void setAuxiliaryOutputFile(String path) {
        Log.w(TAG, "setAuxiliaryOutputFile(String) is no longer supported.");
    }

    public void setOutputFile(FileDescriptor fd) throws IllegalStateException {
        this.mPath = null;
        this.mFd = fd;
    }

    public void setOutputFile(String path) throws IllegalStateException {
        this.mFd = null;
        this.mPath = path;
    }

    public void prepare() throws IllegalStateException, IOException {
        if (this.mIsVideoRecordEnabled) {
            boolean videoRecordEnabled;
            try {
                videoRecordEnabled = EnterpriseDeviceManager.getInstance().getRestrictionPolicy().isVideoRecordAllowed(true);
            } catch (Exception e) {
                videoRecordEnabled = true;
            }
            if (!videoRecordEnabled) {
                throw new IOException("MDM is blocking video recording");
            }
        }
        if (this.mAudioSource == 1 || this.mAudioSource == 5 || this.mAudioSource == 9 || this.mAudioSource == 10) {
            boolean audioRecordEnabled;
            try {
                audioRecordEnabled = EnterpriseDeviceManager.getInstance().getRestrictionPolicy().isAudioRecordAllowed(true);
            } catch (Exception e2) {
                audioRecordEnabled = true;
            }
            if (!audioRecordEnabled) {
                throw new IOException("MDM is blocking audio recording");
            }
        }
        if (this.mPath != null) {
            RandomAccessFile file = new RandomAccessFile(this.mPath, "rws");
            try {
                _setOutputFile(file.getFD(), 0, 0);
            } finally {
                file.close();
            }
        } else if (this.mFd != null) {
            _setOutputFile(this.mFd, 0, 0);
        } else {
            throw new IOException("No valid output file");
        }
        _prepare();
    }

    public void start() throws IllegalStateException {
        if (this.mAudioSource != -1) {
            try {
                AuditLog.logAsUser(5, 5, true, Process.myUid(), getClass().getSimpleName(), "Microphone enabled.", UserHandle.getUserId(Process.myUid()));
            } catch (Exception e) {
                Log.v(TAG, "could not log to auditlog due to lack of permission");
            }
        }
        _start();
    }

    public void stop() throws IllegalStateException {
        if (this.mAudioSource != -1) {
            try {
                AuditLog.logAsUser(5, 5, true, Process.myUid(), getClass().getSimpleName(), "Microphone disabled.", UserHandle.getUserId(Process.myUid()));
            } catch (Exception e) {
                Log.v(TAG, "could not log to auditlog due to lack of permission");
            }
        }
        _stop();
    }

    public void reset() {
        native_reset();
        this.mEventHandler.removeCallbacksAndMessages(null);
        this.mIsAudioEnabled = false;
        this.mIsVideoRecordEnabled = false;
        this.mAudioSource = -1;
    }

    public void setOnErrorListener(OnErrorListener l) {
        this.mOnErrorListener = l;
    }

    public void setOnInfoListener(OnInfoListener listener) {
        this.mOnInfoListener = listener;
    }

    private static void postEventFromNative(Object mediarecorder_ref, int what, int arg1, int arg2, Object obj) {
        MediaRecorder mr = (MediaRecorder) ((WeakReference) mediarecorder_ref).get();
        if (mr != null) {
            if (mr.mIsAudioEnabled) {
                checkMicrophoneEnabled();
            }
            if ((mr.mAudioSource != 1 && mr.mAudioSource != 5 && mr.mAudioSource != 9 && mr.mAudioSource != 10) || checkAudioRecordEnabled()) {
                if ((!mr.mIsVideoRecordEnabled || checkVideoRecordEnabled()) && mr.mEventHandler != null) {
                    mr.mEventHandler.sendMessage(mr.mEventHandler.obtainMessage(what, arg1, arg2, obj));
                }
            }
        }
    }

    private static boolean checkMicrophoneEnabled() {
        boolean enabled;
        try {
            enabled = EnterpriseDeviceManager.getInstance().getRestrictionPolicy().isMicrophoneEnabled(true);
        } catch (Exception e) {
            enabled = true;
        }
        if (!enabled) {
            Log.i(TAG, "MICROPHONE IS DISABLED");
            int appid = UserHandle.getAppId(Process.myUid());
            if (appid >= 10000 && appid <= Process.LAST_APPLICATION_UID) {
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
        }
        return enabled;
    }

    private static boolean checkAudioRecordEnabled() {
        boolean enabled;
        try {
            enabled = EnterpriseDeviceManager.getInstance().getRestrictionPolicy().isAudioRecordAllowed(true);
        } catch (Exception e) {
            enabled = true;
        }
        if (!enabled) {
            Log.i(TAG, "AUDIO RECORD IS DISABLED");
            int appid = UserHandle.getAppId(Process.myUid());
            if (appid >= 10000 && appid <= Process.LAST_APPLICATION_UID) {
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
        }
        return enabled;
    }

    private static boolean checkVideoRecordEnabled() {
        boolean enabled;
        try {
            enabled = EnterpriseDeviceManager.getInstance().getRestrictionPolicy().isVideoRecordAllowed(true);
        } catch (Exception e) {
            enabled = true;
        }
        if (!enabled) {
            Log.i(TAG, "VIDEO RECORD IS DISABLED");
            int appid = UserHandle.getAppId(Process.myUid());
            if (appid >= 10000 && appid <= Process.LAST_APPLICATION_UID) {
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
        }
        return enabled;
    }

    protected void finalize() {
        native_finalize();
    }
}
