package android.preference;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.media.AudioAttributes.Builder;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SamsungAudioManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.Vibrator;
import android.preference.VolumePreference.VolumeStore;
import android.provider.Settings.System;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.android.internal.telephony.ITelephony;
import com.android.internal.telephony.ITelephony.Stub;
import com.samsung.android.telephony.MultiSimManager;
import com.sec.android.app.CscFeature;

public class SeekBarVolumizer implements OnSeekBarChangeListener, android.os.Handler.Callback {
    private static final int CHECK_RINGTONE_PLAYBACK_DELAY_MS = 1000;
    private static final boolean DEBUG = true;
    private static final int DISPLAY_PROFILE_EDIT = 1;
    private static final int DISPLAY_SOUND_SETTING = 0;
    private static final boolean DUAL_SIM = (MultiSimManager.getSimSlotCount() == 2);
    private static final int FINEVOLUME_MAX_INDEX = 150;
    private static final int MSG_INIT_SAMPLE = 3;
    private static final int MSG_SET_STREAM_VOLUME = 0;
    private static final int MSG_START_SAMPLE = 1;
    private static final int MSG_STOP_SAMPLE = 2;
    private static final boolean SUPPORT_FINEVOLUME = true;
    private static final String TAG = "SeekBarVolumizer";
    private final String ACTION_DND_OFF = "com.android.systemui.action.dnd_off";
    private final String SIM_CHANGED_ACTION = "com.samsung.intent.action.DEFAULT_CS_SIM_CHANGED";
    private final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    private Uri mActualRingtoneUri;
    private boolean mAffectedByRingerMode;
    private final AudioManager mAudioManager;
    private final Callback mCallback;
    private final Context mContext;
    private Uri mDefaultUri;
    private int mDisplayType;
    private int mEditMode;
    private Handler mHandler;
    private int mLastAudibleStreamVolume;
    private int mLastProgress = -1;
    private int mLastWaitingToneVolume = -1;
    private final int mMaxStreamVolume;
    private boolean mMuted;
    private final NotificationManager mNotificationManager;
    private boolean mNotificationOrRing;
    private int mOriginalLastAudibleStreamVolume;
    private int mOriginalNotificationVolume;
    private int mOriginalRingerMode;
    private int mOriginalStreamVolume;
    private int mOriginalSystemVolume;
    private int mProfileMode;
    private final Receiver mReceiver = new Receiver();
    private int mRingerMode;
    private Ringtone mRingtone;
    private ContentObserver mRingtoneChangeObserver = new ContentObserver(new Handler()) {
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            SeekBarVolumizer.this.onInitSample();
        }
    };
    private final SamsungAudioManager mSamsungAudioManager;
    private SeekBar mSeekBar;
    private final int mStreamType;
    private boolean mSystemSampleStarted;
    private ToneGenerator mToneGenerator = null;
    private final H mUiHandler = new H();
    private Vibrator mVibrator;
    private boolean mVoiceCapable;
    private int mVolumeBeforeMute = -1;
    private Observer mVolumeObserver;
    private int mZenMode;

    public interface Callback {
        void onMuted(boolean z, boolean z2);

        void onProgressChanged(SeekBar seekBar, int i, boolean z);

        void onSampleStarting(SeekBarVolumizer seekBarVolumizer);
    }

    private final class H extends Handler {
        private static final int UPDATE_SLIDER = 1;

        private H() {
        }

        public void handleMessage(Message msg) {
            boolean muted = true;
            if (msg.what == 1) {
                if (SeekBarVolumizer.this.mSeekBar != null) {
                    SeekBarVolumizer.this.mLastProgress = msg.arg1;
                    SeekBarVolumizer.this.mLastAudibleStreamVolume = Math.abs(msg.arg2);
                    if (msg.arg2 >= 0) {
                        muted = false;
                    }
                    if (muted != SeekBarVolumizer.this.mMuted) {
                        SeekBarVolumizer.this.mMuted = muted;
                        if (SeekBarVolumizer.this.mCallback != null) {
                            SeekBarVolumizer.this.mCallback.onMuted(SeekBarVolumizer.this.mMuted, SeekBarVolumizer.this.isZenMuted());
                        }
                    }
                }
                SeekBarVolumizer.this.updateSeekBar();
            }
        }

        public void postUpdateSlider(int volume, int lastAudibleVolume, boolean mute) {
            int i;
            if (mute) {
                i = -1;
            } else {
                i = 1;
            }
            obtainMessage(1, volume, lastAudibleVolume * i).sendToTarget();
        }
    }

    private final class Observer extends ContentObserver {
        public Observer(Handler handler) {
            super(handler);
        }

        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            if (SeekBarVolumizer.this.mStreamType != 8) {
                SeekBarVolumizer.this.updateSlider();
            }
        }
    }

    private final class Receiver extends BroadcastReceiver {
        private boolean mListening;

        private Receiver() {
        }

        public void setListening(boolean listening) {
            if (this.mListening != listening) {
                this.mListening = listening;
                if (listening) {
                    IntentFilter filter = new IntentFilter();
                    filter.addAction(AudioManager.INTERNAL_RINGER_MODE_CHANGED_ACTION);
                    filter.addAction(NotificationManager.ACTION_INTERRUPTION_FILTER_CHANGED);
                    filter.addAction(AudioManager.STREAM_DEVICES_CHANGED_ACTION);
                    filter.addAction("android.media.VOLUME_CHANGED_ACTION");
                    filter.addAction("com.samsung.intent.action.DEFAULT_CS_SIM_CHANGED");
                    filter.addAction("com.android.systemui.action.dnd_off");
                    SeekBarVolumizer.this.mContext.registerReceiver(this, filter);
                    return;
                }
                SeekBarVolumizer.this.mContext.unregisterReceiver(this);
            }
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.media.VOLUME_CHANGED_ACTION".equals(action)) {
                if (SeekBarVolumizer.this.mStreamType == intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1)) {
                    SeekBarVolumizer.this.updateSlider();
                }
            } else if (AudioManager.INTERNAL_RINGER_MODE_CHANGED_ACTION.equals(action)) {
                SeekBarVolumizer.this.mRingerMode = SeekBarVolumizer.this.mAudioManager.getRingerModeInternal();
                if (SeekBarVolumizer.this.mStreamType != 8) {
                    SeekBarVolumizer.this.updateSlider();
                }
            } else if ("com.samsung.intent.action.DEFAULT_CS_SIM_CHANGED".equals(action)) {
                if (!SeekBarVolumizer.DUAL_SIM) {
                    return;
                }
                if (SubscriptionManager.getSlotId(SubscriptionManager.getDefaultSubId()) == 1) {
                    SeekBarVolumizer.this.mActualRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(SeekBarVolumizer.this.mContext, 128);
                } else {
                    SeekBarVolumizer.this.mActualRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(SeekBarVolumizer.this.mContext, 1);
                }
            } else if ("com.android.systemui.action.dnd_off".equals(action)) {
                if (SeekBarVolumizer.this.isSamplePlaying()) {
                    SeekBarVolumizer.this.stopSample();
                }
            } else if (AudioManager.STREAM_DEVICES_CHANGED_ACTION.equals(action)) {
                if (SeekBarVolumizer.this.mStreamType == intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", -1)) {
                    SeekBarVolumizer.this.updateSlider();
                }
            } else if (NotificationManager.ACTION_INTERRUPTION_FILTER_CHANGED.equals(action)) {
                SeekBarVolumizer.this.mZenMode = SeekBarVolumizer.this.mNotificationManager.getZenMode();
                SeekBarVolumizer.this.updateSlider();
            }
        }

        private void updateVolumeSlider(int streamType, int streamValue) {
            boolean streamMatch = SeekBarVolumizer.this.mNotificationOrRing ? SeekBarVolumizer.this.isNotificationOrRing(streamType) : streamType == SeekBarVolumizer.this.mStreamType;
            if (SeekBarVolumizer.this.mSeekBar != null && streamMatch && streamValue != -1) {
                boolean muted;
                if (SeekBarVolumizer.this.mAudioManager.isStreamMute(SeekBarVolumizer.this.mStreamType) || streamValue == 0) {
                    muted = true;
                } else {
                    muted = false;
                }
                SeekBarVolumizer.this.mUiHandler.postUpdateSlider(streamValue, SeekBarVolumizer.this.mLastAudibleStreamVolume, muted);
            }
        }
    }

    public SeekBarVolumizer(Context context, int streamType, Uri defaultUri, Callback callback) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService(AudioManager.class);
        this.mNotificationManager = (NotificationManager) context.getSystemService(NotificationManager.class);
        this.mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        this.mSamsungAudioManager = new SamsungAudioManager(this.mContext);
        this.mVoiceCapable = context.getResources().getBoolean(17956955);
        this.mStreamType = streamType;
        int i = this.mStreamType;
        AudioManager audioManager = this.mAudioManager;
        if (i == 3) {
            this.mMaxStreamVolume = 1500;
        } else {
            this.mMaxStreamVolume = this.mAudioManager.getStreamMaxVolume(this.mStreamType) * 100;
        }
        this.mAffectedByRingerMode = this.mAudioManager.isStreamAffectedByRingerMode(this.mStreamType);
        this.mNotificationOrRing = isNotificationOrRing(this.mStreamType);
        if (this.mNotificationOrRing) {
            this.mRingerMode = this.mAudioManager.getRingerModeInternal();
        }
        this.mZenMode = this.mNotificationManager.getZenMode();
        this.mCallback = callback;
        i = this.mStreamType;
        audioManager = this.mAudioManager;
        if (i == 3) {
            this.mOriginalStreamVolume = (this.mAudioManager.getStreamVolume(this.mStreamType) * 10) + this.mSamsungAudioManager.getFineMediaVolume();
        } else {
            this.mOriginalStreamVolume = this.mAudioManager.getStreamVolume(this.mStreamType);
        }
        this.mLastWaitingToneVolume = System.getInt(this.mContext.getContentResolver(), "volume_waiting_tone", 2);
        this.mOriginalRingerMode = this.mAudioManager.getRingerMode();
        this.mOriginalStreamVolume = this.mAudioManager.getStreamVolume(this.mStreamType);
        this.mLastAudibleStreamVolume = this.mAudioManager.getLastAudibleStreamVolume(this.mStreamType);
        this.mMuted = this.mAudioManager.isStreamMute(this.mStreamType);
        if (this.mCallback != null) {
            this.mCallback.onMuted(this.mMuted, isZenMuted());
        }
        if (defaultUri == null) {
            if (this.mStreamType == 2) {
                defaultUri = getDefaultRingtoneUri();
            } else if (this.mStreamType == 5) {
                defaultUri = getDefaultNotificationUri();
            } else {
                defaultUri = System.DEFAULT_ALARM_ALERT_URI;
            }
        }
        this.mDefaultUri = defaultUri;
        if (this.mStreamType == 5 && this.mMuted) {
            this.mOriginalNotificationVolume = this.mAudioManager.getLastAudibleStreamVolume(this.mStreamType);
        } else if (this.mStreamType == 1 && this.mMuted) {
            this.mOriginalSystemVolume = this.mAudioManager.getLastAudibleStreamVolume(this.mStreamType);
        } else if (this.mStreamType == 2 && this.mMuted) {
            this.mOriginalLastAudibleStreamVolume = this.mAudioManager.getLastAudibleStreamVolume(this.mStreamType);
        }
        this.mContext.getContentResolver().registerContentObserver(System.DEFAULT_RINGTONE_URI, true, this.mRingtoneChangeObserver);
    }

    private Uri getDefaultRingtoneUri() {
        int currentSimSlot = SubscriptionManager.getSlotId(SubscriptionManager.getDefaultSubId());
        if (!DUAL_SIM) {
            this.mActualRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 1);
            return System.DEFAULT_RINGTONE_URI;
        } else if (currentSimSlot == 1) {
            this.mActualRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 128);
            return System.DEFAULT_RINGTONE_URI_2;
        } else {
            this.mActualRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 1);
            return System.DEFAULT_RINGTONE_URI;
        }
    }

    private Uri getDefaultNotificationUri() {
        int currentSimSlot = SubscriptionManager.getSlotId(SubscriptionManager.getDefaultSubId());
        if (!DUAL_SIM) {
            return System.DEFAULT_NOTIFICATION_URI;
        }
        if (currentSimSlot == 1) {
            return System.DEFAULT_NOTIFICATION_URI_2;
        }
        return System.DEFAULT_NOTIFICATION_URI;
    }

    private boolean isNotificationOrRing(int stream) {
        if (this.mVoiceCapable) {
            if (stream == 2) {
                return true;
            }
            return false;
        } else if (stream != 5) {
            return false;
        } else {
            return true;
        }
    }

    public void setSeekBar(SeekBar seekBar) {
        this.mSeekBar = seekBar;
        if (this.mStreamType == 8) {
            seekBar.setMax(4);
            this.mSeekBar.setProgress(this.mLastProgress > -1 ? this.mLastProgress : this.mLastWaitingToneVolume);
        } else {
            this.mSeekBar.setMax(this.mMaxStreamVolume);
            updateSeekBar();
        }
        this.mSeekBar.setOnSeekBarChangeListener(this);
    }

    private boolean isZenMuted() {
        return (this.mNotificationOrRing && this.mZenMode == 3) || this.mZenMode == 2;
    }

    protected void updateSeekBar() {
        this.mMuted = this.mAudioManager.isStreamMute(this.mStreamType);
        this.mRingerMode = this.mAudioManager.getRingerModeInternal();
        if (!this.mNotificationOrRing || this.mRingerMode == 2) {
            int i;
            AudioManager audioManager;
            if (this.mMuted && !this.mNotificationOrRing) {
                i = this.mStreamType;
                audioManager = this.mAudioManager;
                if (!(i == 3 || this.mRingerMode == 2)) {
                    this.mSeekBar.setEnabled(false);
                    this.mSeekBar.setProgress(0);
                    return;
                }
            }
            i = this.mStreamType;
            audioManager = this.mAudioManager;
            if (i == 3) {
                int fineVolumeIndex = this.mSamsungAudioManager.getFineMediaVolume();
                int streamVolumeIndex = this.mAudioManager.getStreamVolume(this.mStreamType);
                this.mLastProgress = getImpliedMediaVolumeLevel(this.mSeekBar.getProgress());
                if (this.mLastProgress != (streamVolumeIndex * 10) + fineVolumeIndex) {
                    this.mSeekBar.setProgress(((streamVolumeIndex * 10) + fineVolumeIndex) * 10);
                    this.mLastProgress = getImpliedMediaVolumeLevel(this.mSeekBar.getProgress());
                    return;
                }
                return;
            }
            if (!this.mSeekBar.isEnabled()) {
                this.mSeekBar.setEnabled(true);
            }
            this.mLastProgress = getImpliedLevel(this.mSeekBar.getProgress());
            if (this.mLastProgress != this.mAudioManager.getStreamVolume(this.mStreamType)) {
                this.mSeekBar.setProgress(this.mAudioManager.getStreamVolume(this.mStreamType) * 100);
                this.mLastProgress = getImpliedLevel(this.mSeekBar.getProgress());
                return;
            }
            return;
        }
        this.mSeekBar.setProgress(0);
    }

    public void setSeekBarVolume(int volume) {
        postSetVolume(volume);
    }

    public void setDisplayType(int displayType) {
        Log.d(TAG, "setDisplayType : " + displayType);
        this.mDisplayType = displayType;
    }

    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 0:
                this.mAudioManager.setStreamVolume(this.mStreamType, this.mLastProgress, 1024);
                if (this.mLastProgress == 0 && !this.mVibrator.hasVibrator()) {
                    if (this.mStreamType != 2) {
                        if (!this.mVoiceCapable && this.mStreamType == 5) {
                            this.mAudioManager.setRingerMode(0);
                            break;
                        }
                    }
                    this.mAudioManager.setRingerMode(0);
                    break;
                }
            case 1:
                onStartSample();
                break;
            case 2:
                onStopSample();
                break;
            case 3:
                onInitSample();
                break;
            default:
                Log.e(TAG, "invalid SeekBarVolumizer message: " + msg.what);
                break;
        }
        return true;
    }

    private void onInitSample() {
        try {
            this.mRingtone = RingtoneManager.getRingtone(this.mContext, this.mDefaultUri);
            if (this.mRingtone != null) {
                this.mRingtone.setStreamType(this.mStreamType);
            }
        } catch (Exception e) {
            Log.d(TAG, "Exception happens in onInitSample() : " + e.toString());
        }
    }

    private void postStartSample() {
        if (this.mHandler != null && !isUserInCall(this.mContext)) {
            this.mHandler.removeMessages(1);
            this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1), isSamplePlaying() ? 1000 : 0);
        }
    }

    private void onStartSample() {
        if (!isSamplePlaying()) {
            if (this.mCallback != null) {
                this.mCallback.onSampleStarting(this);
            }
            if (this.mRingtone != null) {
                try {
                    this.mRingtone.setAudioAttributes(new Builder(this.mRingtone.getAudioAttributes()).setFlags(192).build());
                    ITelephony telephonyService = getTelephonyService();
                    String opPackageName = this.mContext.getOpPackageName();
                    if (telephonyService != null) {
                        Log.d(TAG, "isRinging : " + telephonyService.isRinging(opPackageName));
                        Log.d(TAG, "isOffhook : " + telephonyService.isOffhook(opPackageName));
                        if (telephonyService.isRinging(opPackageName) || telephonyService.isOffhook(opPackageName)) {
                            Log.d(TAG, "don't play ringtone in volumepreference: return called");
                            return;
                        }
                    }
                } catch (RemoteException ex) {
                    Log.w(TAG, "ITelephony threw RemoteException", ex);
                } catch (Throwable e) {
                    Log.w(TAG, "Error playing ringtone, stream " + this.mStreamType, e);
                    return;
                }
                if (this.mStreamType != 1 && this.mStreamType != 8 && !this.mAudioManager.isFMActive()) {
                    Log.d(TAG, "sample : mRingtone.play()");
                    if (this.mStreamType == 2 && this.mActualRingtoneUri != null) {
                        checkDefaultRingtoneUri();
                    } else if (this.mStreamType == 5) {
                        checkDefaultNotificationUri();
                    }
                    this.mRingtone.setUri(this.mDefaultUri);
                    if (this.mRingtone != null) {
                        this.mRingtone.setStreamType(this.mStreamType);
                    }
                    this.mRingtone.play();
                }
            }
        }
    }

    private void checkDefaultRingtoneUri() {
        int currentSimSlot = SubscriptionManager.getSlotId(SubscriptionManager.getDefaultSubId());
        if (!DUAL_SIM) {
            if (!this.mActualRingtoneUri.toString().equals(RingtoneManager.getActualDefaultRingtoneUri(this.mContext, 1).toString())) {
                this.mDefaultUri = System.DEFAULT_RINGTONE_URI;
            }
        } else if (currentSimSlot == 1) {
            this.mDefaultUri = System.DEFAULT_RINGTONE_URI_2;
        } else {
            this.mDefaultUri = System.DEFAULT_RINGTONE_URI;
        }
    }

    private void checkDefaultNotificationUri() {
        int currentSimSlot = SubscriptionManager.getSlotId(SubscriptionManager.getDefaultSubId());
        if (!DUAL_SIM) {
            this.mDefaultUri = System.DEFAULT_NOTIFICATION_URI;
        } else if (currentSimSlot == 1) {
            this.mDefaultUri = System.DEFAULT_NOTIFICATION_URI_2;
        } else {
            this.mDefaultUri = System.DEFAULT_NOTIFICATION_URI;
        }
    }

    private void postStopSample() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(2));
        }
    }

    private void onStopSample() {
        if (this.mRingtone != null) {
            this.mRingtone.stop();
        }
    }

    public void stop() {
        if (this.mHandler != null) {
            postStopSample();
            stopToneGenerator();
            if (this.mToneGenerator != null) {
                this.mToneGenerator.release();
                this.mToneGenerator = null;
            }
            this.mContext.getContentResolver().unregisterContentObserver(this.mVolumeObserver);
            this.mReceiver.setListening(false);
            this.mSeekBar.setOnSeekBarChangeListener(null);
            this.mHandler.getLooper().quitSafely();
            this.mHandler = null;
            this.mVolumeObserver = null;
        }
    }

    public void start() {
        if (this.mHandler == null) {
            HandlerThread thread = new HandlerThread("SeekBarVolumizer.CallbackHandler");
            thread.start();
            this.mHandler = new Handler(thread.getLooper(), (android.os.Handler.Callback) this);
            this.mHandler.sendEmptyMessage(3);
            this.mVolumeObserver = new Observer(this.mHandler);
            this.mContext.getContentResolver().registerContentObserver(System.getUriFor(System.VOLUME_SETTINGS[this.mStreamType]), false, this.mVolumeObserver);
            this.mReceiver.setListening(true);
        }
    }

    public void revertVolume() {
        if (this.mStreamType == 8) {
            System.putInt(this.mContext.getContentResolver(), "volume_waiting_tone", this.mLastWaitingToneVolume);
        } else if (this.mStreamType == 5 && this.mAudioManager.isStreamMute(this.mStreamType)) {
            if (this.mVoiceCapable || this.mOriginalStreamVolume != 0 || this.mOriginalRingerMode == 2) {
                this.mAudioManager.setStreamVolume(this.mStreamType, this.mOriginalNotificationVolume, 0);
                return;
            }
            if (this.mAudioManager.getRingerMode() != this.mOriginalRingerMode) {
                this.mAudioManager.setRingerMode(this.mOriginalRingerMode);
            }
            this.mAudioManager.setStreamVolume(this.mStreamType, this.mOriginalNotificationVolume, 67108864);
        } else if (this.mStreamType == 1 && this.mAudioManager.isStreamMute(this.mStreamType)) {
            this.mAudioManager.setStreamVolume(this.mStreamType, this.mOriginalSystemVolume, 0);
        } else if (this.mOriginalStreamVolume != 0 || this.mOriginalRingerMode == 2) {
            this.mAudioManager.setStreamVolume(this.mStreamType, this.mOriginalStreamVolume, 0);
        } else {
            if (this.mAudioManager.getRingerMode() != this.mOriginalRingerMode) {
                this.mAudioManager.setRingerMode(this.mOriginalRingerMode);
            }
            this.mAudioManager.setStreamVolume(this.mStreamType, this.mOriginalLastAudibleStreamVolume, 67108864);
        }
    }

    private int getImpliedLevel(int progress) {
        int n = (this.mMaxStreamVolume / 100) - 1;
        if (progress == 0) {
            return 0;
        }
        return progress == this.mMaxStreamVolume ? this.mMaxStreamVolume / 100 : ((int) ((((float) progress) / ((float) this.mMaxStreamVolume)) * ((float) n))) + 1;
    }

    private int getImpliedMediaVolumeLevel(int progress) {
        return progress / 10;
    }

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
        if (fromTouch) {
            if (this.mStreamType == 3) {
                progress = getImpliedMediaVolumeLevel(progress);
            } else {
                progress = getImpliedLevel(progress);
            }
            Log.d(TAG, "onProgressChanged : stream = " + this.mStreamType + ", progress = " + progress);
            if (!(this.mStreamType == 1 || this.mStreamType == 8 || isSamplePlaying())) {
                startSample();
            }
            stopToneGenerator();
            if (this.mStreamType == 2) {
                if (CscFeature.getInstance().getEnableStatus("CscFeature_Common_SupportPhoneProfile") && this.mDisplayType == 1 && progress < 1) {
                    progress = 1;
                }
                setSeekBarVolume(progress);
            } else if (this.mStreamType == 5) {
                if (progress == 0) {
                    stopSample();
                }
                postSetVolume(progress);
            } else if (this.mStreamType == 1) {
                Log.d(TAG, "******onProgressChanged*****");
                if (this.mCallback != null) {
                    this.mCallback.onSampleStarting(this);
                }
                this.mAudioManager.setStreamVolume(1, progress, 0);
                ITelephony telephonyService = getTelephonyService();
                String opPackageName = this.mContext.getOpPackageName();
                if (telephonyService != null) {
                    if (telephonyService.isRinging(opPackageName) || telephonyService.isOffhook(opPackageName)) {
                        Log.d(TAG, "isRinging : " + telephonyService.isRinging(opPackageName));
                        Log.d(TAG, "isOffhook : " + telephonyService.isOffhook(opPackageName));
                        Log.d(TAG, "don't play STREAM_SYSTEM in volumepreference.");
                        Log.d(TAG, "onProgressChanged : AudioManager.STREAM_SYSTEM[" + this.mAudioManager.getStreamVolume(1) + "]");
                    }
                }
                try {
                    if (!this.mAudioManager.isFMActive() && this.mLastProgress != progress) {
                        this.mAudioManager.playSoundEffect(100, (float) progress);
                        this.mSystemSampleStarted = true;
                        Log.d(TAG, "sample : playSoundEffect()");
                    } else if (this.mLastProgress == progress) {
                        this.mSystemSampleStarted = false;
                    }
                    this.mLastProgress = progress;
                } catch (RemoteException ex) {
                    Log.w(TAG, "ITelephony threw RemoteException", ex);
                }
                Log.d(TAG, "onProgressChanged : AudioManager.STREAM_SYSTEM[" + this.mAudioManager.getStreamVolume(1) + "]");
            } else if (this.mStreamType == 8) {
                System.putInt(this.mContext.getContentResolver(), "volume_waiting_tone", progress);
                if (this.mToneGenerator == null) {
                    this.mToneGenerator = new ToneGenerator(8, 0);
                }
                this.mToneGenerator.setVolume(Float.parseFloat(this.mAudioManager.getParameters("situation=15;device=0")) * ((float) Math.pow(2.0d, (double) (progress - 2))));
                this.mToneGenerator.startTone(22, 300);
                this.mLastProgress = progress;
            } else if (this.mStreamType != 3) {
                postSetVolume(progress);
            } else if (this.mStreamType == 3) {
                int progressRemainder = progress % 10;
                this.mAudioManager.setStreamVolume(this.mStreamType, progress / 10, 0);
                this.mSamsungAudioManager.setFineMediaVolume(progressRemainder);
                this.mLastProgress = progress;
            } else {
                postSetVolume(progress);
            }
            if (!(this.mStreamType == 1 || this.mStreamType == 8 || isSamplePlaying())) {
                startSample();
            }
        }
        if (this.mCallback != null) {
            this.mCallback.onProgressChanged(seekBar, progress, fromTouch);
        }
    }

    private void stopToneGenerator() {
        if (this.mToneGenerator != null) {
            this.mToneGenerator.setVolume(0.0f);
            this.mToneGenerator.stopTone();
        }
    }

    private void postSetVolume(int progress) {
        if (this.mHandler != null) {
            this.mLastProgress = progress;
            this.mHandler.removeMessages(0);
            this.mHandler.sendMessage(this.mHandler.obtainMessage(0));
        }
    }

    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    public void onStopTrackingTouch(SeekBar seekBar) {
        if (this.mStreamType == 1 && !this.mSystemSampleStarted) {
            ITelephony telephonyService = getTelephonyService();
            String opPackageName = this.mContext.getOpPackageName();
            int progress = getImpliedLevel(seekBar.getProgress());
            if (telephonyService != null) {
                try {
                    if (telephonyService.isRinging(opPackageName) || telephonyService.isOffhook(opPackageName)) {
                        Log.d(TAG, "[onStopTrackingTouch]isRinging : " + telephonyService.isRinging(opPackageName));
                        Log.d(TAG, "[onStopTrackingTouch]isOffhook : " + telephonyService.isOffhook(opPackageName));
                        Log.d(TAG, "[onStopTrackingTouch]don't play STREAM_SYSTEM in volumepreference.");
                        return;
                    }
                } catch (RemoteException ex) {
                    Log.w(TAG, "ITelephony threw RemoteException", ex);
                    return;
                }
            }
            if (!this.mAudioManager.isFMActive()) {
                this.mAudioManager.playSoundEffect(100, (float) progress);
                Log.d(TAG, "[onStopTrackingTouch]sample : playSoundEffect() : " + progress);
            }
        }
    }

    private boolean isUserInCall(Context context) {
        boolean isVoiceCall;
        if (((TelephonyManager) context.getSystemService("phone")).getCallState() != 0) {
            isVoiceCall = true;
        } else {
            isVoiceCall = false;
        }
        int audioMode = this.mAudioManager.getMode();
        boolean isVoIP;
        if (audioMode == 3 || audioMode == 2) {
            isVoIP = true;
        } else {
            isVoIP = false;
        }
        if (isVoiceCall || isVoIP) {
            return true;
        }
        return false;
    }

    public boolean isSamplePlaying() {
        return this.mRingtone != null && this.mRingtone.isPlaying();
    }

    public void startSample() {
        ITelephony telephonyService = getTelephonyService();
        String opPackageName = this.mContext.getOpPackageName();
        if (telephonyService != null) {
            try {
                Log.d(TAG, "isRinging : " + telephonyService.isRinging(opPackageName));
                Log.d(TAG, "isOffhook : " + telephonyService.isOffhook(opPackageName));
                if (telephonyService.isRinging(opPackageName) || telephonyService.isOffhook(opPackageName)) {
                    Log.d(TAG, "don't play ringtone in volumepreference: return called");
                    return;
                }
            } catch (RemoteException ex) {
                Log.w(TAG, "ITelephony threw RemoteException", ex);
            }
        }
        postStartSample();
    }

    private ITelephony getTelephonyService() {
        ITelephony telephonyService = Stub.asInterface(ServiceManager.checkService("phone"));
        if (telephonyService == null) {
            Log.w(TAG, "Unable to find ITelephony interface.");
        }
        return telephonyService;
    }

    public void stopSample() {
        postStopSample();
    }

    public SeekBar getSeekBar() {
        return this.mSeekBar;
    }

    public void changeVolumeBy(int amount) {
        this.mSeekBar.incrementProgressBy(amount);
        postSetVolume(this.mSeekBar.getProgress());
        postStartSample();
        this.mVolumeBeforeMute = -1;
    }

    public void muteVolume() {
        if (this.mVolumeBeforeMute != -1) {
            this.mSeekBar.setProgress(this.mVolumeBeforeMute);
            postSetVolume(this.mVolumeBeforeMute);
            postStartSample();
            this.mVolumeBeforeMute = -1;
            return;
        }
        this.mVolumeBeforeMute = this.mSeekBar.getProgress();
        this.mSeekBar.setProgress(0);
        postStopSample();
        postSetVolume(0);
    }

    public void onSaveInstanceState(VolumeStore volumeStore) {
        if (this.mLastProgress >= 0) {
            volumeStore.volume = this.mLastProgress;
            volumeStore.originalVolume = this.mOriginalStreamVolume;
        }
    }

    public void onRestoreInstanceState(VolumeStore volumeStore) {
        if (volumeStore.volume != -1) {
            this.mOriginalStreamVolume = volumeStore.originalVolume;
            this.mLastProgress = volumeStore.volume;
            postSetVolume(this.mLastProgress);
        }
    }

    private void updateSlider() {
        if (this.mSeekBar != null && this.mAudioManager != null) {
            this.mUiHandler.postUpdateSlider(this.mAudioManager.getStreamVolume(this.mStreamType), this.mAudioManager.getLastAudibleStreamVolume(this.mStreamType), this.mAudioManager.isStreamMute(this.mStreamType));
        }
    }
}
