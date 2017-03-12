package com.android.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.SamsungAudioManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.provider.Settings$System;
import android.provider.Settings.Global;
import android.security.KeyChain;
import android.security.keymaster.KeymasterDefs;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.ims.ImsConferenceState;
import com.android.internal.os.SMProviderContract;
import com.android.internal.telephony.PhoneConstants;
import com.samsung.android.cocktailbar.AbsCocktailLoadablePanel;
import com.samsung.android.feature.FloatingFeature;
import com.samsung.android.fingerprint.FingerprintManager;
import com.samsung.android.magazinecard.Constants.MagazineIntents;
import com.samsung.android.motion.MREvent;
import com.samsung.android.motion.MRListener;
import com.samsung.android.motion.MotionRecognitionManager;
import com.samsung.android.smartface.SmartFaceManager;
import com.samsung.media.fmradio.internal.IFMEventListener;
import com.samsung.media.fmradio.internal.IFMPlayer.Stub;
import com.sec.android.app.CscFeature;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class FMRadioService extends Stub {
    private static final String ACTINON_ALARM_PLAY = "com.sec.android.app.voicecommand";
    private static final String ACTION_ALL_SOUND_OFF = "android.settings.ALL_SOUND_MUTE";
    private static final String ACTION_BACKGROUND_PLAY = "com.android.backgroung.playing";
    private static final String ACTION_DNS_PAUSE_INTERNET_STREAM = "com.sec.android.app.dns.action.pause_internet_stream";
    private static final String ACTION_DNS_RESUME_INTERNET_STREAM = "com.sec.android.app.dns.action.resume_internet_stream";
    private static final String ACTION_DNS_STOP_INTERNET_STREAM = "com.sec.android.app.dns.action.stop_internet_stream";
    private static final String ACTION_MUSIC_COMMAND = "com.android.music.musicservicecommand";
    private static final String ACTION_RECORDING_SAVE = "com.samsung.media.fmradio.rec_save";
    private static final String ACTION_SAVE_FMRECORDING_ONLY = "com.samsung.media.save_fmrecording_only";
    private static final String ACTION_VOLUME_LOCK = "com.sec.android.fm.volume_lock";
    private static final String ACTION_VOLUME_UNLOCK = "com.sec.android.fm.volume_unlock";
    private static final String APP_NAME = "com.sec.android.app.fm";
    public static final int BAND_76000_108000_kHz = 2;
    public static final int BAND_76000_90000_kHz = 3;
    public static final int BAND_87500_108000_kHz = 1;
    public static final int CHAN_SPACING_100_kHz = 10;
    public static final int CHAN_SPACING_200_kHz = 20;
    public static final int CHAN_SPACING_50_kHz = 5;
    private static final int CODE_SCAN_PROGRESS = 1;
    public static final boolean DEBUG = true;
    private static final boolean DEBUGGABLE;
    private static final int DELAY_WAITING_STREAM_STOPPED = 150;
    public static final int DE_TIME_CONSTANT_50 = 1;
    public static final int DE_TIME_CONSTANT_75 = 0;
    static final int EVENT_AF_RECEIVED = 14;
    static final int EVENT_AF_STARTED = 13;
    private static final int EVENT_CHANNEL_FOUND = 1;
    private static final int EVENT_EAR_PHONE_CONNECT = 8;
    private static final int EVENT_EAR_PHONE_DISCONNECT = 9;
    private static final int EVENT_OFF = 6;
    private static final int EVENT_ON = 5;
    static final int EVENT_PIECC_EVENT = 18;
    private static final int EVENT_RDS_DISABLED = 12;
    private static final int EVENT_RDS_ENABLED = 11;
    static final int EVENT_RDS_EVENT = 10;
    static final int EVENT_REC_FINISH = 17;
    static final int EVENT_RTPLUS_EVENT = 16;
    private static final int EVENT_SCAN_FINISHED = 3;
    private static final int EVENT_SCAN_STARTED = 2;
    private static final int EVENT_SCAN_STOPPED = 4;
    private static final int EVENT_TUNE = 7;
    public static final int EVENT_VOLUME_LOCK = 15;
    private static final String KEY_RETURNBACK_VOLUME = "com.sec.android.fm.return_back_volume";
    private static final String KNOX_MODE_USER_SWITCH = "android.intent.action.USER_SWITCHED";
    private static final String NEXTRADIO_NAME = "com.nextradioapp.nextradio";
    public static final int OFF_AIRPLANE_MODE_SET = 3;
    public static final int OFF_BATTERY_LOW = 7;
    public static final int OFF_CALL_ACTIVE = 1;
    public static final int OFF_DEVICE_SHUTDOWN = 6;
    public static final int OFF_EAR_PHONE_DISCONNECT = 2;
    public static final int OFF_MOTION_LISTENER = 21;
    public static final int OFF_NORMAL = 0;
    public static final int OFF_PAUSE_COMMAND = 5;
    public static final int OFF_STOP_COMMAND = 4;
    public static final int OFF_TV_OUT = 10;
    private static final String PACKAGE_NAME_TALKBACK = "com.google.android.marvin.talkback";
    public static final int PAUSED = 11;
    static final int VOLUME_FADEIN = 200;
    static final int VOLUME_FADEIN_DELAYTIME = 100;
    private static final String VOLUME_UP_DOWN = "114,115";
    private static final String audioMute = "fm_radio_mute=1";
    private static final String audioUnMute = "fm_radio_mute=0";
    private static boolean mIsSetWakeKey = false;
    private static boolean mIsTransientPaused = false;
    private static MotionRecognitionManager mMotionSensorManager = null;
    private String SetPropertyPermission = "com.sec.android.app.fm.permission.setproperty";
    private String VolumePropertyname = "service.brcm.fm.volumetable";
    private boolean alarmTTSPlay = false;
    private FileOutputStream fos;
    private boolean isFMBackGroundPlaying = true;
    private boolean mAFEnable;
    private int mAfRmssisampleCnt_th = 0;
    private int mAfRmssith_th = 0;
    private boolean mAirPlaneEnabled;
    private final BroadcastReceiver mAlarmReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            FMRadioService.log("Alarm onReceive");
            String cmdStr = intent.getStringExtra(MagazineIntents.COMMAND);
            if ("TTSstart".equals(cmdStr)) {
                FMRadioService.log("TTSstart play");
                FMRadioService.this.alarmTTSPlay = true;
            }
            if ("TTSstop".equals(cmdStr)) {
                FMRadioService.log("TTSstop play");
                FMRadioService.this.alarmTTSPlay = false;
            }
        }
    };
    private int mAlgo_type = 1;
    private final BroadcastReceiver mAllSoundOffReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            int AllSoundOff = intent.getIntExtra("mute", 0);
            FMRadioService.log("mAllSoundOffReceiver :: " + AllSoundOff);
            if (AllSoundOff == 1) {
                FMRadioService.log("FM chip mute");
                FMRadioService.this.mute(true);
                FMRadioService.this.notifyRecFinish();
                return;
            }
            FMRadioService.log("FM chip unmute");
            FMRadioService.this.mute(false);
        }
    };
    final Handler mAudioFocusHandler = new Handler() {
        public static final int EVENT_AUDIOFOCUS_GAIN = 1;
        public static final int EVENT_AUDIOFOCUS_LOSS = -1;
        public static final int EVENT_AUDIOFOCUS_LOSS_TRANSIENT = -2;
        public static final int EVENT_AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3;
        public static final int INIT_TIRED = 10;
        private static final String TAG = "mAudioFocusHandler:";
        private int TIREDTIME = 300;
        private boolean isTired = false;

        public void handleMessage(Message msg) {
            FMRadioService.log("mAudioFocusHandler:mHandler(g.what=" + msg.what + ") is called + isTired = " + this.isTired);
            switch (msg.what) {
                case -3:
                case -2:
                case -1:
                case 1:
                    if (!this.isTired) {
                        FMRadioService.this.responedFocusEvent(msg.what);
                        FMRadioService.this.respondAudioFocusChangeForDns(msg.what);
                        removeMessages(10);
                        sendEmptyMessageDelayed(10, (long) this.TIREDTIME);
                        this.isTired = true;
                        FMRadioService.log("mAudioFocusHandler:Fired  TIME = " + (SystemClock.uptimeMillis() / 1000));
                        break;
                    }
                    FMRadioService.log("mAudioFocusHandler:TIME = " + (SystemClock.uptimeMillis() / 1000));
                    sendEmptyMessageDelayed(msg.what, (long) this.TIREDTIME);
                    break;
                case 10:
                    this.isTired = false;
                    break;
            }
            FMRadioService.log("=============TIREDTIME = ==== ||| " + this.TIREDTIME + " ||||| ====" + (SystemClock.uptimeMillis() / 1000));
        }
    };
    private OnAudioFocusChangeListener mAudioFocusListener = new OnAudioFocusChangeListener() {
        public void onAudioFocusChange(int focusChange) {
            FMRadioService.log("-------------------------------------------------------------------------");
            FMRadioService.this.clearMessageQueue();
            FMRadioService.this.mAudioFocusHandler.sendEmptyMessage(focusChange);
            FMRadioService.log("----------------------------^_^|||^_^^_^^_^^_^^_^^_^^_^^_^^_^^_^^_^^_^^_^^_^^_^^_^");
            if (FMRadioService.DEBUGGABLE) {
                FMRadioService.log("OnAudioFocusChangeListener switch off mAudioFocusListener :" + focusChange + " stored freq:" + FMRadioService.this.mNeedResumeToFreq);
            }
        }
    };
    private AudioManager mAudioManager;
    public int mBand = 1;
    private BroadcastReceiver mButtonReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.media.AUDIO_BECOMING_NOISY")) {
                boolean isFromBT = intent.getBooleanExtra("android.bluetooth.a2dp.extra.DISCONNECT_A2DP", false);
                FMRadioService.log("ACTION_AUDIO_BECOMING_NOISE , Its from BT :" + isFromBT);
                boolean isFromDock = intent.getBooleanExtra("DISCONNECT_DOCK", false);
                FMRadioService.log("ACTION_AUDIO_BECOMING_NOISE , Its from Dock :" + isFromDock);
                if (FMRadioService.this.mIsOn && !FMRadioService.this.mIsTestMode && !isFromBT && !isFromDock) {
                    FMRadioService.this.notifyEvent(9, null);
                    FMRadioService.this.stopInternetStreaming();
                    FMRadioService.this.cancelSeek();
                    FMRadioService.this.offInternal(true, 2, true);
                }
            }
        }
    };
    private int mCf0_th12 = 0;
    public int mChannelSpacing = 10;
    private int mChipVendor = 0;
    private int mCnt_th = 0;
    private int mCnt_th_2 = 0;
    private Context mContext;
    private long mCurrentFoundFreq = 0;
    private long mCurrentResumeVol;
    public int mDEConstant = 1;
    private boolean mDNSEnable;
    private final BroadcastReceiver mEmergencyReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            FMRadioService.log("mEmergencyReceiver onReceive");
            int reason = intent.getIntExtra("reason", 0);
            if (FMRadioService.this.mIsOn && reason == 2) {
                FMRadioService.log("mReceiver: EMERGENCY_STATE_CHANGED - fmradio off");
                FMRadioService.this.stopInternetStreaming();
                FMRadioService.this.cancelSeek();
                FMRadioService.this.offInternal(true, 4, true);
            } else if (!FMRadioService.this.mIsOn && reason == 2) {
                FMRadioService.this.mIsForcestop = true;
                FMRadioService.this.mIsAudioFocusAlive = false;
                FMRadioService.log("force stop : remove audiofocus");
                FMRadioService.this.mAudioManager.abandonAudioFocus(FMRadioService.this.mAudioFocusListener);
            }
        }
    };
    private String mFmOff = "102,116,172";
    private String mFmOn = "102,114,115,116,172";
    final Handler mHandler = new Handler() {
        long currentVolume = 0;

        public void handleMessage(Message msg) {
            FMRadioService.log("mHandler(g.what=" + msg.what + ") is called");
            if (msg.what != 200) {
                return;
            }
            if (!FMRadioService.this.mIsOn) {
                this.currentVolume = 0;
            } else if (this.currentVolume < FMRadioService.this.mCurrentResumeVol) {
                this.currentVolume++;
                FMRadioService.this.setVolume(this.currentVolume);
                FMRadioService.this.queueUpdate(200, 100);
            } else {
                this.currentVolume = FMRadioService.this.mResumeVol;
                FMRadioService.this.setVolume(this.currentVolume);
                this.currentVolume = 0;
            }
        }
    };
    private boolean mInternetStreamingMode = false;
    public boolean mIsAudioFocusAlive = false;
    private boolean mIsBatteryLow;
    private BroadcastReceiver mIsFMBackGrondPlaying = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(FMRadioService.ACTION_BACKGROUND_PLAY)) {
                FMRadioService.this.isFMBackGroundPlaying = intent.getBooleanExtra("isbackgroundplaying", true);
            }
            FMRadioService.log("mIsFMBackGrondPlaying :" + FMRadioService.this.isFMBackGroundPlaying);
        }
    };
    private boolean mIsForcestop = false;
    private boolean mIsHeadsetPlugged = false;
    private boolean mIsKeepRecoding = true;
    private boolean mIsMute;
    private boolean mIsOn;
    private boolean mIsSeeking;
    private boolean mIsSkipTunigVal;
    private boolean mIsTestMode;
    private boolean mIsTvOutPlugged = false;
    private boolean mIsphoneCall = false;
    private boolean mIsphoneCallRinging = false;
    private Vector<ListenerRecord> mListeners;
    private final BroadcastReceiver mLowBatteryReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            FMRadioService.log("FMRadioService:mLowBatteryReceiver " + action);
            FMRadioService.log("Low batteryWarning Level :" + 1);
            if (action.equals("android.intent.action.BATTERY_CHANGED")) {
                int battStatus = intent.getIntExtra(ImsConferenceState.STATUS, 1);
                int battScale = intent.getIntExtra("scale", 100);
                int battLevel = intent.getIntExtra("level", battScale);
                FMRadioService.log("Level = " + battLevel + "/" + battScale);
                FMRadioService.log("Status = " + battStatus);
                if (battLevel > 1 || battStatus == 2) {
                    FMRadioService.this.mIsBatteryLow = false;
                    return;
                }
                FMRadioService.this.mIsBatteryLow = true;
                if (FMRadioService.this.mIsOn) {
                    FMRadioService.this.stopInternetStreaming();
                    FMRadioService.this.offInternal(true, 7, true);
                }
            }
        }
    };
    private int mMarvell_cmi = 0;
    private int mMarvell_rssi = 0;
    private MRListener mMotionListener = new MRListener() {
        public void onMotionListener(MREvent motionEvent) {
            switch (motionEvent.getMotion()) {
                case 10:
                    if (!FMRadioService.this.mIsOn || !FMRadioService.this.mAudioManager.isRadioSpeakerOn()) {
                        return;
                    }
                    if (!FMRadioService.this.mIsMute || FMRadioService.this.mChipVendor == 3) {
                        FMRadioService.log("FLIP_TOP_TO_BOTTOM : mute");
                        FMRadioService.this.mAudioManager.setStreamVolume(SamsungAudioManager.stream(1), 0, 0);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private BroadcastReceiver mMusicCommandRec = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String cmdStr = intent.getStringExtra(MagazineIntents.COMMAND);
            String appName = intent.getStringExtra("from");
            if (FMRadioService.DEBUGGABLE) {
                FMRadioService.log("Got Music command :" + cmdStr + " from:" + appName);
            }
            if (!FMRadioService.APP_NAME.equals(appName)) {
                if ("stop".equals(cmdStr) || "pause".equals(cmdStr)) {
                    FMRadioService.this.offInternal(true, 11, true);
                }
            }
        }
    };
    private long mNeedResumeToFreq = -2;
    private boolean mOnProgress = false;
    private PhoneStateListener mPhoneListener = new PhoneStateListener() {
        public void onCallStateChanged(int state, String incomingNumber) {
            FMRadioService.log("phone state : " + state);
            switch (state) {
                case 0:
                    FMRadioService.log("CALL_STATE_IDLE - mIsphoneCallRinging: " + FMRadioService.this.mIsphoneCallRinging + "  mIsphoneCall : " + FMRadioService.this.mIsphoneCall);
                    if (!FMRadioService.this.isOn() && FMRadioService.this.mNeedResumeToFreq != -2 && !FMRadioService.this.mIsForcestop && FMRadioService.this.mIsphoneCallRinging && FMRadioService.this.mIsphoneCall && FMRadioService.this.mIsKeepRecoding) {
                        if (FMRadioService.this.on(false)) {
                            FMRadioService.log("CALL_STATE_IDLE " + FMRadioService.this.mNeedResumeToFreq);
                            FMRadioService.this.mAudioManager.setRadioSpeakerOn(FMRadioService.this.mAudioManager.isRadioSpeakerOn());
                            if (FMRadioService.mIsTransientPaused) {
                                FMRadioService.this.mResumeVol = (long) FMRadioService.this.mAudioManager.getStreamVolume(SamsungAudioManager.stream(1));
                                FMRadioService.log("slowly increase the volume till :" + FMRadioService.this.mResumeVol);
                                if (FMRadioService.this.mResumeVol != 0) {
                                    FMRadioService.this.mCurrentResumeVol = FMRadioService.this.mResumeVol;
                                    FMRadioService.this.setVolume(1);
                                    FMRadioService.this.mHandler.removeMessages(200);
                                    FMRadioService.this.mHandler.sendEmptyMessageDelayed(200, 100);
                                } else {
                                    FMRadioService.this.mAudioManager.setStreamVolume(SamsungAudioManager.stream(1), (int) FMRadioService.this.mResumeVol, 0);
                                }
                                FMRadioService.mIsTransientPaused = false;
                            } else {
                                FMRadioService.this.mAudioManager.setStreamVolume(SamsungAudioManager.stream(1), FMRadioService.this.mAudioManager.getStreamVolume(SamsungAudioManager.stream(1)), 0);
                            }
                            if (FMRadioService.this.mNeedResumeToFreq <= 0) {
                                FMRadioService.this.mNeedResumeToFreq = 87500;
                            }
                            FMRadioService.this.mPlayerNative.tune(FMRadioService.this.mNeedResumeToFreq);
                            FMRadioService.this.notifyEvent(7, Long.valueOf(FMRadioService.this.mNeedResumeToFreq));
                            Intent intent = new Intent("com.app.fm.auto.on");
                            intent.setFlags(268435456);
                            intent.putExtra("freq", (((float) FMRadioService.this.mNeedResumeToFreq) / 1000.0f) + "");
                            FMRadioService.this.mContext.sendBroadcast(intent);
                            FMRadioService.this.mNeedResumeToFreq = -2;
                        } else {
                            FMRadioService.log("Not able to resume FM player");
                        }
                    }
                    FMRadioService.this.mIsphoneCallRinging = false;
                    FMRadioService.this.mIsphoneCall = false;
                    return;
                case 1:
                    if (FMRadioService.this.mIsOn && FMRadioService.this.volumeLock) {
                        FMRadioService.this.mIsphoneCallRinging = true;
                        if (!FMRadioService.this.mIsKeepRecoding) {
                            if (FMRadioService.this.mScanProgress) {
                                FMRadioService.this.mNeedResumeToFreq = FMRadioService.this.mScanFreq;
                            } else {
                                FMRadioService.this.mNeedResumeToFreq = FMRadioService.this.getCurrentChannel();
                            }
                            FMRadioService.this.notifyEvent(17, null);
                            FMRadioService.this.offInternal(true, 11, false);
                            FMRadioService.this.mIsphoneCall = true;
                            return;
                        }
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private FMPlayerNative mPlayerNative;
    private PowerManager mPowerManager;
    private long mPreviousFoundFreq = 0;
    private int mQualcomm_af_rmssisamplecnt = 80;
    private int mQualcomm_af_rmssith = 53;
    private int mQualcomm_cfoth12 = 15000;
    private int mQualcomm_offchannel = 115;
    private int mQualcomm_onchannel = 109;
    private int mQualcomm_rmssi_firststate = -113;
    private int mQualcomm_sinr_samplecnt = 10;
    private boolean mRDSEnable;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            boolean z = false;
            boolean z2 = true;
            if (intent.getAction().equals("android.intent.action.HEADSET_PLUG")) {
                FMRadioService.log("mReceiver: ACTION_HEADSET_PLUG");
                if (FMRadioService.DEBUGGABLE) {
                    FMRadioService.log("==> intent: " + intent);
                }
                FMRadioService.log("   state: " + intent.getIntExtra("state", 0));
                FMRadioService.log("    name: " + intent.getStringExtra(KeyChain.EXTRA_NAME));
                FMRadioService.this.mIsHeadsetPlugged = intent.getIntExtra("state", 0) != 0;
                if (FMRadioService.this.mIsTestMode) {
                    boolean z3;
                    AudioManager access$100 = FMRadioService.this.mAudioManager;
                    if (FMRadioService.this.mIsHeadsetPlugged) {
                        z3 = false;
                    } else {
                        z3 = true;
                    }
                    access$100.setRadioSpeakerOn(z3);
                    StringBuilder append = new StringBuilder().append("TestMode :- making setRadioSpeakerOn:");
                    if (FMRadioService.this.mIsHeadsetPlugged) {
                        z2 = false;
                    }
                    FMRadioService.log(append.append(z2).toString());
                } else if (FMRadioService.this.mIsHeadsetPlugged) {
                    FMRadioService.this.notifyEvent(8, null);
                } else {
                    int tvstatus = Settings$System.getInt(FMRadioService.this.mContext.getContentResolver(), "tv_out", 0);
                    FMRadioService.log("TV out setting value :" + tvstatus);
                    if (tvstatus != 1) {
                        FMRadioService.this.notifyEvent(9, null);
                        if (FMRadioService.this.mIsOn) {
                            FMRadioService.this.stopInternetStreaming();
                            FMRadioService.this.cancelSeek();
                            FMRadioService.this.offInternal(true, 2, true);
                            return;
                        }
                        FMRadioService.this.mAudioManager.abandonAudioFocus(FMRadioService.this.mAudioFocusListener);
                    }
                }
            } else if (intent.getAction().equals("android.intent.action.AIRPLANE_MODE")) {
                FMRadioService.log("mReceiver: ACTION_AIRPLANE_MODE_CHANGED");
                r1 = FMRadioService.this;
                if (Global.getInt(FMRadioService.this.mContext.getContentResolver(), Settings$System.AIRPLANE_MODE_ON, 0) != 0) {
                    z = true;
                }
                r1.mAirPlaneEnabled = z;
                System.out.println("mAirPlaneEnabled flag :" + FMRadioService.this.mAirPlaneEnabled);
                if (FMRadioService.this.mAirPlaneEnabled && FMRadioService.this.mIsOn) {
                    FMRadioService.this.stopInternetStreaming();
                    FMRadioService.this.offInternal(true, 3, true);
                }
            } else if (intent.getAction().equals("android.intent.action.TVOUT_PLUG")) {
                FMRadioService.log("mReceiver: ACTION_TVOUT_PLUG");
                if (FMRadioService.DEBUGGABLE) {
                    FMRadioService.log("==> intent: " + intent);
                }
                FMRadioService.log("   state: " + intent.getIntExtra("state", 0));
                FMRadioService.log("    name: " + intent.getStringExtra(KeyChain.EXTRA_NAME));
                if (!FMRadioService.this.mIsTestMode) {
                    r1 = FMRadioService.this;
                    if (intent.getIntExtra("state", 0) != 0) {
                        z = true;
                    }
                    r1.mIsTvOutPlugged = z;
                    if (FMRadioService.this.mIsTvOutPlugged && FMRadioService.this.mIsOn) {
                        FMRadioService.this.stopInternetStreaming();
                        FMRadioService.this.cancelSeek();
                        FMRadioService.this.offInternal(true, 10, true);
                    }
                }
            } else if (intent.getAction().equals(FMRadioService.ACTION_SAVE_FMRECORDING_ONLY)) {
                if (FMRadioService.this.mIsOn) {
                    FMRadioService.log("mReceiver:Stop FM for Camera -");
                    if (FMRadioService.this.volumeLock) {
                        FMRadioService.log("mReceiver:Stop recording for Camera ");
                        FMRadioService.this.notifyEvent(17, null);
                    }
                    FMRadioService.this.offInternal(true, 4, true);
                }
            } else if (intent.getAction().equals(FMRadioService.ACTION_RECORDING_SAVE)) {
                if (FMRadioService.this.mIsOn && FMRadioService.this.mIsphoneCallRinging) {
                    FMRadioService.log("mReceiver: ACTION_RECORDING_SAVE - fmradio off");
                    if (FMRadioService.this.mScanProgress) {
                        FMRadioService.this.mNeedResumeToFreq = FMRadioService.this.mScanFreq;
                    } else {
                        FMRadioService.this.mNeedResumeToFreq = FMRadioService.this.getCurrentChannel();
                    }
                    FMRadioService.this.notifyEvent(17, null);
                    FMRadioService.this.offInternal(true, 11, false);
                    FMRadioService.this.mIsphoneCall = false;
                    FMRadioService.this.mContext.sendBroadcast(new Intent(FMRadioService.ACTION_DNS_PAUSE_INTERNET_STREAM));
                }
            } else if (intent.getAction().equals(FMRadioService.KNOX_MODE_USER_SWITCH)) {
                FMRadioService.log("mReceiver: KNOX_MODE_USER_SWITCH - fmradio off");
                if (FMRadioService.this.mIsOn) {
                    FMRadioService.this.stopInternetStreaming();
                    FMRadioService.this.cancelSeek();
                    FMRadioService.this.offInternal(true, 4, true);
                }
            }
        }
    };
    private long mResumeVol;
    private int mReturnBackVolume = -1;
    private int mRichwave_rssi = 12;
    private int mRssi_th = 0;
    private int mRssi_th_2 = 0;
    private ArrayList<Long> mScanChannelList;
    private long mScanFreq;
    private boolean mScanProgress;
    private Thread mScanThread = null;
    private int mScanVolume;
    private final BroadcastReceiver mSetPropertyReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            FMRadioService.log("mSetPropertyReceiver : action is " + action);
            String key;
            if ("com.sec.android.app.fm.set_property".equals(action)) {
                key = intent.getStringExtra("key");
                int value = intent.getIntExtra("value", 0);
                if (FMRadioService.DEBUGGABLE) {
                    FMRadioService.log("mSetPropertyReceiver :: " + key + "=" + value);
                }
                if (key.startsWith("service.brcm.fm") || key.startsWith("service.mrvl.fm")) {
                    SystemProperties.set(key, String.valueOf(value));
                }
            } else if ("com.sec.android.app.fm.set_volume".equals(action)) {
                key = intent.getStringExtra("key");
                String volumetable = intent.getStringExtra("volumetable");
                if (FMRadioService.DEBUGGABLE) {
                    FMRadioService.log("mSetPropertyReceiver :: " + key + "=" + volumetable);
                }
                if (FMRadioService.this.VolumePropertyname.equals(key)) {
                    SystemProperties.set(key, volumetable);
                }
            }
        }
    };
    private int mSilicon_cnt = 1;
    private int mSilicon_rssi = 0;
    private int mSilicon_snr = 2;
    private int mSnr_th = 0;
    private int mSnr_th_2 = 0;
    private final BroadcastReceiver mSystemReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")) {
                off();
            }
        }

        private void off() {
            if (FMRadioService.this.mIsOn) {
                FMRadioService.log("Powering off : stop FM");
                FMRadioService.this.stopInternetStreaming();
                FMRadioService.this.cancelSeek();
                FMRadioService.this.offInternal(true, 6, true);
                return;
            }
            FMRadioService.log("Powering off : remove audiofocus: FM");
            FMRadioService.this.mAudioManager.abandonAudioFocus(FMRadioService.this.mAudioFocusListener);
        }
    };
    private final BroadcastReceiver mSystemReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.PACKAGE_REMOVED") || action.equals("android.intent.action.PACKAGE_RESTARTED")) {
                String packageName = intent.getData().getSchemeSpecificPart();
                if (FMRadioService.APP_NAME.equals(packageName) || (CscFeature.getInstance().getEnableStatus("CscFeature_Common_SupportNextRadio") && FMRadioService.NEXTRADIO_NAME.equals(packageName))) {
                    FMRadioService.this.mIsForcestop = true;
                    FMRadioService.this.mIsAudioFocusAlive = false;
                    off();
                }
            }
        }

        private void off() {
            if (FMRadioService.this.mIsOn) {
                FMRadioService.log("force stop : making off FM");
                FMRadioService.this.stopInternetStreaming();
                FMRadioService.this.cancelSeek();
                FMRadioService.this.offInternal(true, 6, true);
                return;
            }
            FMRadioService.log("force stop : remove audiofocus");
            FMRadioService.this.mAudioManager.abandonAudioFocus(FMRadioService.this.mAudioFocusListener);
        }
    };
    private TelephonyManager mTelephonyManager;
    private BroadcastReceiver mVolumeEventReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if ("android.media.VOLUME_CHANGED_ACTION".equals(intent.getAction())) {
                if (FMRadioService.this.volumeLock) {
                    FMRadioService.this.notifyEvent(15, null);
                } else {
                    int stream = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", 10);
                    int volume = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_VALUE", 0);
                    FMRadioService.log("*** mReceiver: SAMSUNG_VOLUME_CHANGED_ACTION");
                    FMRadioService.log("   stream: " + stream);
                    FMRadioService.log("   volume: " + volume);
                    int current_stream_volume = FMRadioService.this.mAudioManager.getStreamVolume(SamsungAudioManager.stream(1));
                    FMRadioService.log("   current_stream_volume: " + current_stream_volume);
                    if ((stream == SamsungAudioManager.stream(1) || (stream == 3 && FMRadioService.this.mIsOn)) && volume == current_stream_volume) {
                        if (FMRadioService.this.mHandler.hasMessages(200)) {
                            FMRadioService.this.mHandler.removeMessages(200);
                        }
                        FMRadioService.this.setVolume((long) volume);
                    }
                }
            }
            if (FMRadioService.ACTION_VOLUME_LOCK.equals(intent.getAction())) {
                if (FMRadioService.this.mChipVendor == 2 || FMRadioService.this.mChipVendor == 3) {
                    FMRadioService.this.mReturnBackVolume = intent.getIntExtra(FMRadioService.KEY_RETURNBACK_VOLUME, -1);
                }
                FMRadioService.log("Volume Locked...");
                FMRadioService.this.volumeLock = true;
            } else if (FMRadioService.ACTION_VOLUME_UNLOCK.equals(intent.getAction())) {
                FMRadioService.log("Volume Unlocked...");
                FMRadioService.this.mReturnBackVolume = -1;
                FMRadioService.this.volumeLock = false;
            }
        }
    };
    private boolean mWaitPidDuringScanning = false;
    private WakeLock mWakeLock;
    private String mWakeUpKeyFileName = "wakeup_keys";
    private String mWakeUpKeyFilePath = "/sys/class/sec/sec_key/";
    private int mgoodChrmssi_th = 0;
    private String platform = null;
    private boolean volumeLock = false;

    private static class ListenerRecord {
        IBinder mBinder;
        IFMEventListener mListener;

        public ListenerRecord(IFMEventListener listener, IBinder binder) {
            this.mBinder = binder;
            this.mListener = listener;
        }
    }

    class ScanThread extends Thread {
        private int scanCount;

        ScanThread() {
        }

        private void doScan() throws InterruptedException {
            if (FMRadioService.this.mBand != 1) {
                FMRadioService.this.mPlayerNative.tune(76000);
            } else if (FMRadioService.this.mChipVendor == 2) {
                FMRadioService.this.mPlayerNative.tune(108000);
            } else {
                FMRadioService.this.mPlayerNative.tune(87500);
            }
            this.scanCount++;
            if (FMRadioService.this.mChipVendor == 4) {
                FMRadioService.this.mPreviousFoundFreq = 0;
                FMRadioService.this.mCurrentFoundFreq = 0;
            }
            if (!FMRadioServiceFeature.FEATURE_DISABLEDNS && FMRadioService.this.mWaitPidDuringScanning) {
                FMRadioService.this.mPlayerNative.setScanning(true);
            }
            while (FMRadioService.this.mScanProgress) {
                long freq = FMRadioService.this.searchAll();
                if (FMRadioService.DEBUGGABLE) {
                    FMRadioService.log("Found channel :" + freq);
                }
                if (FMRadioService.this.mChipVendor == 4 || !FMRadioService.this.mScanChannelList.contains(Long.valueOf(freq))) {
                    if (freq <= 0) {
                        FMRadioService.log("Testmode Skipp value : " + FMRadioService.this.mIsSkipTunigVal);
                        FMRadioService.this.notifyEvent(3, FMRadioService.this.mScanChannelList.toArray(new Long[0]));
                        Thread.sleep(20);
                        break;
                    }
                    if (FMRadioService.this.mScanFreq <= 0) {
                        FMRadioService.this.mScanFreq = freq;
                    }
                    if (FMRadioService.this.mChipVendor != 4) {
                        FMRadioService.this.mScanChannelList.add(Long.valueOf(freq));
                        FMRadioService.this.notifyEvent(1, Long.valueOf(freq));
                        if (!(FMRadioServiceFeature.FEATURE_DISABLEDNS || !FMRadioService.this.mWaitPidDuringScanning || FMRadioService.this.mScanThread == null)) {
                            synchronized (FMRadioService.this.mScanThread) {
                                FMRadioService.this.mScanThread.wait(250);
                            }
                        }
                        if (((FMRadioService.this.mBand == 1 || FMRadioService.this.mBand == 2) && freq == 108000) || (FMRadioService.this.mBand == 3 && freq == 90000)) {
                            FMRadioService.this.notifyEvent(3, FMRadioService.this.mScanChannelList.toArray(new Long[0]));
                            Thread.sleep(20);
                            break;
                        }
                    }
                    FMRadioService.this.mCurrentFoundFreq = freq;
                    if (FMRadioService.this.mPreviousFoundFreq >= FMRadioService.this.mCurrentFoundFreq) {
                        FMRadioService.this.notifyEvent(3, FMRadioService.this.mScanChannelList.toArray(new Long[0]));
                        Thread.sleep(20);
                        break;
                    }
                    FMRadioService.this.mPreviousFoundFreq = FMRadioService.this.mCurrentFoundFreq;
                    FMRadioService.this.mScanChannelList.add(Long.valueOf(freq));
                    FMRadioService.this.notifyEvent(1, Long.valueOf(freq));
                    if (!(FMRadioServiceFeature.FEATURE_DISABLEDNS || !FMRadioService.this.mWaitPidDuringScanning || FMRadioService.this.mScanThread == null)) {
                        synchronized (FMRadioService.this.mScanThread) {
                            FMRadioService.this.mScanThread.wait(250);
                        }
                    }
                } else if (FMRadioService.DEBUGGABLE) {
                    FMRadioService.log("Duplicate channel :" + freq);
                }
            }
            if (!FMRadioServiceFeature.FEATURE_DISABLEDNS && FMRadioService.this.mWaitPidDuringScanning) {
                FMRadioService.this.mPlayerNative.setScanning(false);
            }
        }

        public void run() {
            WakeLock wakelock = FMRadioService.this.mPowerManager.newWakeLock(KeymasterDefs.KM_TAG_PURPOSE, "FMRadio Service Scan Thread");
            wakelock.acquire();
            FMRadioService.log("Scan thread gets the dimmed screen lock");
            try {
                FMRadioService.log("Scanning Thread started...");
                FMRadioService.this.notifyEvent(2, null);
                FMRadioService.this.mAudioManager.setParameters("fmradio_turnon=false");
                FMRadioService.log("Scanning Thread started... - Turning off FM");
                FMRadioService.this.mScanFreq = FMRadioService.this.getCurrentChannel();
                if (FMRadioService.this.mScanChannelList == null) {
                    FMRadioService.this.mScanChannelList = new ArrayList();
                } else {
                    FMRadioService.this.mScanChannelList.clear();
                }
                if (FMRadioService.this.mIsSkipTunigVal) {
                    FMRadioService.this.setSignalSetting(FMRadioService.this.mRssi_th, FMRadioService.this.mSnr_th, FMRadioService.this.mCnt_th);
                    FMRadioService.log("first scan no block channel with " + FMRadioService.this.mRssi_th + FMRadioService.this.mSnr_th + FMRadioService.this.mCnt_th);
                }
                doScan();
                FMRadioService.this.mScanProgress = false;
                FMRadioService.this.mScanThread = null;
                FMRadioService.this.mScanVolume = FMRadioService.this.mAudioManager.getStreamVolume(SamsungAudioManager.stream(1));
                FMRadioService.log("Scan Thread resuming volume :" + FMRadioService.this.mScanVolume);
                FMRadioService.this.mAudioManager.setStreamVolume(SamsungAudioManager.stream(1), FMRadioService.this.mScanVolume, 0);
                if (wakelock.isHeld()) {
                    wakelock.release();
                    FMRadioService.log("Scan thread released the dimmed screen lock");
                }
            } catch (Exception e) {
                e.printStackTrace();
                FMRadioService.this.mScanProgress = false;
                FMRadioService.this.mScanThread = null;
                FMRadioService.this.mScanVolume = FMRadioService.this.mAudioManager.getStreamVolume(SamsungAudioManager.stream(1));
                FMRadioService.log("Scan Thread resuming volume :" + FMRadioService.this.mScanVolume);
                FMRadioService.this.mAudioManager.setStreamVolume(SamsungAudioManager.stream(1), FMRadioService.this.mScanVolume, 0);
                if (wakelock.isHeld()) {
                    wakelock.release();
                    FMRadioService.log("Scan thread released the dimmed screen lock");
                }
            } catch (Throwable th) {
                FMRadioService.this.mScanProgress = false;
                FMRadioService.this.mScanThread = null;
                FMRadioService.this.mScanVolume = FMRadioService.this.mAudioManager.getStreamVolume(SamsungAudioManager.stream(1));
                FMRadioService.log("Scan Thread resuming volume :" + FMRadioService.this.mScanVolume);
                FMRadioService.this.mAudioManager.setStreamVolume(SamsungAudioManager.stream(1), FMRadioService.this.mScanVolume, 0);
                if (wakelock.isHeld()) {
                    wakelock.release();
                    FMRadioService.log("Scan thread released the dimmed screen lock");
                }
            }
            FMRadioService.log("Scanning Thread work is done...");
        }
    }

    static {
        boolean z = true;
        if (SystemProperties.getInt("ro.debuggable", 0) != 1) {
            z = false;
        }
        DEBUGGABLE = z;
    }

    private void clearMessageQueue() {
        this.mAudioFocusHandler.removeMessages(-1);
        this.mAudioFocusHandler.removeMessages(-2);
        this.mAudioFocusHandler.removeMessages(-3);
        this.mAudioFocusHandler.removeMessages(1);
    }

    private void stopInternetStreaming() {
        log("stopInternetStreaming() - streamingMode:" + this.mInternetStreamingMode);
        if (!FMRadioServiceFeature.FEATURE_DISABLEDNS) {
            try {
                this.mContext.sendBroadcast(new Intent(ACTION_DNS_STOP_INTERNET_STREAM));
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void respondAudioFocusChangeForDns(int focusChange) {
        log("respondAudioFocusChangeForDns()-focusChange:" + focusChange + " streamingMode:" + this.mInternetStreamingMode);
        if (this.mInternetStreamingMode) {
            String action = null;
            switch (focusChange) {
                case -3:
                case -2:
                    action = ACTION_DNS_PAUSE_INTERNET_STREAM;
                    break;
                case -1:
                    action = ACTION_DNS_STOP_INTERNET_STREAM;
                    break;
                case 1:
                    action = ACTION_DNS_RESUME_INTERNET_STREAM;
                    break;
            }
            this.mContext.sendBroadcast(new Intent(action));
        }
    }

    private void responedFocusEvent(int focusEvent) {
        switch (focusEvent) {
            case -3:
                if (!isOn()) {
                    return;
                }
                if (this.volumeLock) {
                    log("AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK - recoding O");
                    return;
                }
                log("AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK - recoding X");
                if (this.mScanProgress) {
                    this.mNeedResumeToFreq = this.mScanFreq;
                } else {
                    this.mNeedResumeToFreq = getCurrentChannel();
                }
                offInternal(true, 11, false);
                respondAudioFocusChangeForDns(focusEvent);
                return;
            case -2:
                if (isOn()) {
                    if (this.mScanProgress) {
                        this.mNeedResumeToFreq = this.mScanFreq;
                    } else {
                        this.mNeedResumeToFreq = getCurrentChannel();
                    }
                    if (!this.volumeLock) {
                        log("AUDIOFOCUS_LOSS_TRANSIENT - recoding X");
                        offInternal(true, 11, false);
                        respondAudioFocusChangeForDns(focusEvent);
                        return;
                    } else if (this.mIsphoneCallRinging && this.mIsKeepRecoding) {
                        log("AUDIOFOCUS_LOSS_TRANSIENT - recoding O - call Ringing");
                        return;
                    } else {
                        log("AUDIOFOCUS_LOSS_TRANSIENT - recoding O");
                        respondAudioFocusChangeForDns(focusEvent);
                        offInternal(true, 11, false);
                        return;
                    }
                } else if (this.mOnProgress) {
                    this.mAudioFocusHandler.removeMessages(focusEvent);
                    this.mAudioFocusHandler.sendEmptyMessage(focusEvent);
                    return;
                } else {
                    return;
                }
            case -1:
                if (isOn()) {
                    if (this.mScanProgress) {
                        this.mNeedResumeToFreq = this.mScanFreq;
                    } else {
                        this.mNeedResumeToFreq = getCurrentChannel();
                    }
                    if (this.volumeLock) {
                        log("AUDIOFOCUS_LOSS - recoding O");
                        notifyEvent(17, null);
                    } else {
                        log("AUDIOFOCUS_LOSS - recoding X");
                    }
                    respondAudioFocusChangeForDns(focusEvent);
                    offInternal(true, 11, true);
                    return;
                } else if (this.mOnProgress) {
                    log("still in progress");
                    this.mAudioFocusHandler.removeMessages(focusEvent);
                    this.mAudioFocusHandler.sendEmptyMessage(focusEvent);
                    return;
                } else {
                    return;
                }
            case 1:
                if (!isOn() && this.mNeedResumeToFreq != -2 && !this.mIsForcestop) {
                    respondAudioFocusChangeForDns(focusEvent);
                    if (on(false)) {
                        log("OnAudioFocusChangeListener switch on mNeedResumeToFreq:" + this.mNeedResumeToFreq);
                        this.mAudioManager.setRadioSpeakerOn(this.mAudioManager.isRadioSpeakerOn());
                        if (mIsTransientPaused) {
                            this.mResumeVol = (long) this.mAudioManager.getStreamVolume(SamsungAudioManager.stream(1));
                            log("slowly increase the volume till :" + this.mResumeVol);
                            if (this.mResumeVol != 0) {
                                this.mCurrentResumeVol = this.mResumeVol;
                                setVolume(1);
                                this.mHandler.removeMessages(200);
                                this.mHandler.sendEmptyMessageDelayed(200, 100);
                            } else {
                                this.mAudioManager.setStreamVolume(SamsungAudioManager.stream(1), (int) this.mResumeVol, 0);
                            }
                            mIsTransientPaused = false;
                        } else {
                            this.mAudioManager.setStreamVolume(SamsungAudioManager.stream(1), this.mAudioManager.getStreamVolume(SamsungAudioManager.stream(1)), 0);
                        }
                        if (this.mNeedResumeToFreq <= 0) {
                            this.mNeedResumeToFreq = 87500;
                        }
                        this.mPlayerNative.tune(this.mNeedResumeToFreq);
                        notifyEvent(7, Long.valueOf(this.mNeedResumeToFreq));
                        Intent intent = new Intent("com.app.fm.auto.on");
                        intent.putExtra("freq", (((float) this.mNeedResumeToFreq) / 1000.0f) + "");
                        this.mContext.sendBroadcast(intent);
                        this.mNeedResumeToFreq = -2;
                        return;
                    }
                    log("Not able to resume FM player");
                    return;
                }
                return;
            default:
                return;
        }
    }

    void registerMotionListener() {
        log("[FMRadioService] registerMotionListener ");
        synchronized (this.mMotionListener) {
            if (mMotionSensorManager == null) {
                mMotionSensorManager = (MotionRecognitionManager) this.mContext.getSystemService("motion_recognition");
            }
            mMotionSensorManager.registerListenerEvent(this.mMotionListener, 1);
        }
    }

    void unregisterMotionListener() {
        if (mMotionSensorManager != null && this.mMotionListener != null) {
            synchronized (this.mMotionListener) {
                if (mMotionSensorManager != null) {
                    mMotionSensorManager.unregisterListener(this.mMotionListener);
                }
            }
        }
    }

    private void registerAllSoundOffListener() {
        IntentFilter intentAllSoundOffFilter = new IntentFilter();
        intentAllSoundOffFilter.addAction(ACTION_ALL_SOUND_OFF);
        this.mContext.registerReceiver(this.mAllSoundOffReceiver, intentAllSoundOffFilter);
        log("registering AllSoundOff listener");
    }

    private void unregisterAllSoundOffListener() {
        log("Unregistering AllSoundOff listener");
        this.mContext.unregisterReceiver(this.mAllSoundOffReceiver);
    }

    private void registerAlarmListener() {
        IntentFilter intentAlarmFilter = new IntentFilter();
        intentAlarmFilter.addAction(ACTINON_ALARM_PLAY);
        this.mContext.registerReceiver(this.mAlarmReceiver, intentAlarmFilter);
        log("registering Alarm play listener");
    }

    private void unregisterAlarmListener() {
        log("Unregistering Alarm play listener");
        this.mContext.unregisterReceiver(this.mAlarmReceiver);
    }

    private void registerEmergencyStateChangedListener() {
        IntentFilter intentEmergencyFilter = new IntentFilter();
        intentEmergencyFilter.addAction("com.samsung.intent.action.EMERGENCY_STATE_CHANGED");
        this.mContext.registerReceiver(this.mEmergencyReceiver, intentEmergencyFilter);
        log("registering Emergency State Changed Listener");
    }

    private void unregisterEmegencyStateChangedListener() {
        log("Unregistering Emergency State Changed Listener");
        this.mContext.unregisterReceiver(this.mEmergencyReceiver);
    }

    private void readChipVendor() {
        this.mChipVendor = Integer.parseInt(SmartFaceManager.PAGE_MIDDLE);
        log("ChipVendor : " + this.mChipVendor);
    }

    private void readTuningParameters() {
        if (this.mChipVendor == 2) {
            String productName = getPropertyProductName();
            if (productName.startsWith("zerolte")) {
                SystemProperties.set("service.brcm.fm.start_snr", Integer.toString(34));
                SystemProperties.set("service.brcm.fm.set_blndmute", SmartFaceManager.PAGE_BOTTOM);
            } else if (productName.startsWith("j2lte")) {
                SystemProperties.set("service.brcm.fm.start_mute", Integer.toString(4));
            } else if (productName.startsWith("j1xlte")) {
                SystemProperties.set("service.brcm.fm.start_snr", Integer.toString(1));
                SystemProperties.set("service.brcm.fm.set_blndmute", SmartFaceManager.PAGE_BOTTOM);
            }
        }
        if (!"".equals(FMRadioServiceFeature.FEATURE_SETLOCALTUNNING)) {
            String[] Local_Tunning_vals = FMRadioServiceFeature.FEATURE_SETLOCALTUNNING.split(FingerprintManager.FINGER_PERMISSION_DELIMITER);
            log("Tuning value size: " + Local_Tunning_vals.length);
            switch (Local_Tunning_vals.length) {
                case 1:
                    if (this.mChipVendor == 2) {
                        SystemProperties.set("service.brcm.fm.start_mute", Local_Tunning_vals[0]);
                        SystemProperties.set("service.brcm.fm.set_blndmute", SmartFaceManager.PAGE_BOTTOM);
                        this.mRssi_th = Integer.parseInt(SmartFaceManager.PAGE_MIDDLE);
                        this.mSnr_th = Integer.parseInt(SmartFaceManager.PAGE_MIDDLE);
                        this.mCnt_th = Integer.parseInt(SmartFaceManager.PAGE_MIDDLE);
                        return;
                    } else if (this.mChipVendor == 4) {
                        this.mSnr_th = Integer.parseInt(Local_Tunning_vals[0]);
                        this.mAlgo_type = Integer.parseInt(SmartFaceManager.PAGE_BOTTOM);
                        this.mSnr_th_2 = Integer.parseInt("-2");
                        this.mRssi_th = this.mQualcomm_rmssi_firststate;
                        this.mCnt_th = this.mQualcomm_onchannel;
                        this.mCnt_th_2 = this.mQualcomm_offchannel;
                        this.mRssi_th_2 = this.mQualcomm_sinr_samplecnt;
                        this.mCf0_th12 = this.mQualcomm_cfoth12;
                        this.mAfRmssith_th = this.mQualcomm_af_rmssith;
                        this.mAfRmssisampleCnt_th = this.mQualcomm_af_rmssisamplecnt;
                        this.mgoodChrmssi_th = Integer.parseInt("-110");
                        return;
                    } else {
                        this.mRssi_th = Integer.parseInt(Local_Tunning_vals[0]);
                        this.mSnr_th = 0;
                        this.mCnt_th = 0;
                        return;
                    }
                case 2:
                    if (this.mChipVendor == 4) {
                        this.mCnt_th = Integer.parseInt(Local_Tunning_vals[0]);
                        this.mSnr_th = Integer.parseInt(Local_Tunning_vals[1]);
                        this.mAlgo_type = Integer.parseInt(SmartFaceManager.PAGE_BOTTOM);
                        this.mSnr_th_2 = Integer.parseInt("-2");
                        this.mRssi_th = this.mQualcomm_rmssi_firststate;
                        this.mCnt_th_2 = this.mQualcomm_offchannel;
                        this.mRssi_th_2 = this.mQualcomm_sinr_samplecnt;
                        this.mCf0_th12 = this.mQualcomm_cfoth12;
                        this.mAfRmssith_th = this.mQualcomm_af_rmssith;
                        this.mAfRmssisampleCnt_th = this.mQualcomm_af_rmssisamplecnt;
                        this.mgoodChrmssi_th = Integer.parseInt("-110");
                        return;
                    }
                    return;
                case 3:
                    this.mRssi_th = Integer.parseInt(Local_Tunning_vals[0]);
                    this.mSnr_th = Integer.parseInt(Local_Tunning_vals[1]);
                    this.mCnt_th = Integer.parseInt(Local_Tunning_vals[2]);
                    return;
                case 4:
                    if (this.mChipVendor == 4) {
                        this.mRssi_th = Integer.parseInt(Local_Tunning_vals[0]);
                        this.mSnr_th_2 = Integer.parseInt(Local_Tunning_vals[1]);
                        this.mSnr_th = Integer.parseInt(Local_Tunning_vals[2]);
                        this.mAlgo_type = Integer.parseInt(Local_Tunning_vals[3]);
                        this.mCnt_th = this.mQualcomm_onchannel;
                        this.mCnt_th_2 = this.mQualcomm_offchannel;
                        this.mRssi_th_2 = this.mQualcomm_sinr_samplecnt;
                        this.mCf0_th12 = this.mQualcomm_cfoth12;
                        this.mAfRmssith_th = this.mQualcomm_af_rmssith;
                        this.mAfRmssisampleCnt_th = this.mQualcomm_af_rmssisamplecnt;
                        this.mgoodChrmssi_th = Integer.parseInt("-110");
                        return;
                    }
                    this.mRssi_th = Integer.parseInt(Local_Tunning_vals[0]);
                    this.mSnr_th = Integer.parseInt(Local_Tunning_vals[1]);
                    this.mCnt_th = Integer.parseInt(Local_Tunning_vals[2]);
                    if (this.mChipVendor == 2) {
                        SystemProperties.set("service.brcm.fm.start_mute", Local_Tunning_vals[3]);
                        SystemProperties.set("service.brcm.fm.set_blndmute", SmartFaceManager.PAGE_BOTTOM);
                        return;
                    }
                    return;
                case 5:
                    if (this.mChipVendor == 4) {
                        this.mRssi_th = Integer.parseInt(Local_Tunning_vals[0]);
                        this.mSnr_th_2 = Integer.parseInt(Local_Tunning_vals[1]);
                        this.mSnr_th = Integer.parseInt(Local_Tunning_vals[2]);
                        this.mAlgo_type = Integer.parseInt(Local_Tunning_vals[3]);
                        this.mgoodChrmssi_th = Integer.parseInt(Local_Tunning_vals[4]);
                        this.mCnt_th = this.mQualcomm_onchannel;
                        this.mCnt_th_2 = this.mQualcomm_offchannel;
                        this.mRssi_th_2 = this.mQualcomm_sinr_samplecnt;
                        this.mCf0_th12 = this.mQualcomm_cfoth12;
                        this.mAfRmssith_th = this.mQualcomm_af_rmssith;
                        this.mAfRmssisampleCnt_th = this.mQualcomm_af_rmssisamplecnt;
                        return;
                    }
                    this.mRssi_th = Integer.parseInt(Local_Tunning_vals[0]);
                    this.mSnr_th = Integer.parseInt(Local_Tunning_vals[1]);
                    this.mCnt_th = Integer.parseInt(Local_Tunning_vals[2]);
                    if (this.mChipVendor == 2) {
                        SystemProperties.set("service.brcm.fm.start_snr", Local_Tunning_vals[3]);
                        SystemProperties.set("service.brcm.fm.stop_snr", Local_Tunning_vals[4]);
                        SystemProperties.set("service.brcm.fm.set_blndmute", SmartFaceManager.PAGE_BOTTOM);
                        return;
                    }
                    return;
                case 6:
                    this.mRssi_th = Integer.parseInt(Local_Tunning_vals[0]);
                    this.mSnr_th = Integer.parseInt(Local_Tunning_vals[1]);
                    this.mCnt_th = Integer.parseInt(Local_Tunning_vals[2]);
                    this.mRssi_th_2 = Integer.parseInt(Local_Tunning_vals[3]);
                    this.mSnr_th_2 = Integer.parseInt(Local_Tunning_vals[4]);
                    this.mCnt_th_2 = Integer.parseInt(Local_Tunning_vals[5]);
                    return;
                default:
                    log("Tuning value size: " + Local_Tunning_vals.length);
                    return;
            }
        } else if (this.mChipVendor == 1) {
            this.mRssi_th = this.mSilicon_rssi;
            this.mSnr_th = this.mSilicon_snr;
            this.mCnt_th = this.mSilicon_cnt;
        } else if (this.mChipVendor == 2) {
            this.mRssi_th = Integer.parseInt(SmartFaceManager.PAGE_MIDDLE);
            this.mSnr_th = Integer.parseInt(SmartFaceManager.PAGE_MIDDLE);
            this.mCnt_th = Integer.parseInt(SmartFaceManager.PAGE_MIDDLE);
        } else if (this.mChipVendor == 3) {
            this.mRssi_th = Integer.parseInt(SmartFaceManager.PAGE_MIDDLE);
            this.mSnr_th = 0;
            this.mCnt_th = Integer.parseInt(SmartFaceManager.PAGE_MIDDLE);
        } else if (this.mChipVendor == 4) {
            this.mSnr_th = Integer.parseInt("3");
            this.mAlgo_type = Integer.parseInt(SmartFaceManager.PAGE_BOTTOM);
            this.mSnr_th_2 = Integer.parseInt("-2");
            this.mRssi_th = this.mQualcomm_rmssi_firststate;
            this.mCnt_th = this.mQualcomm_onchannel;
            this.mCnt_th_2 = this.mQualcomm_offchannel;
            this.mRssi_th_2 = this.mQualcomm_sinr_samplecnt;
            this.mCf0_th12 = this.mQualcomm_cfoth12;
            this.mAfRmssith_th = this.mQualcomm_af_rmssith;
            this.mAfRmssisampleCnt_th = this.mQualcomm_af_rmssisamplecnt;
            this.mgoodChrmssi_th = Integer.parseInt("-110");
        } else if (this.mChipVendor == 5) {
            this.mRssi_th = this.mRichwave_rssi;
        }
    }

    private void readParametersForCurrentRegion() {
        try {
            if (FMRadioServiceFeature.BANDWIDTHAS_87500_108000.equals(FMRadioServiceFeature.FEATURE_BANDWIDTH)) {
                this.mBand = 1;
            } else if (FMRadioServiceFeature.BANDWIDTHAS_76000_108000.equals(FMRadioServiceFeature.FEATURE_BANDWIDTH)) {
                this.mBand = 2;
            } else if (FMRadioServiceFeature.BANDWIDTHAS_76000_90000.equals(FMRadioServiceFeature.FEATURE_BANDWIDTH)) {
                this.mBand = 3;
            } else {
                this.mBand = 1;
            }
            switch (FMRadioServiceFeature.FEATURE_FREQUENCYSPACE) {
                case 50:
                    this.mChannelSpacing = 5;
                    break;
                case 100:
                    this.mChannelSpacing = 10;
                    break;
                default:
                    this.mChannelSpacing = 10;
                    break;
            }
            switch (FMRadioServiceFeature.FEATURE_DECONSTANT) {
                case 50:
                    this.mDEConstant = 1;
                    return;
                case 75:
                    this.mDEConstant = 0;
                    return;
                default:
                    this.mDEConstant = 1;
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.mBand = 1;
            this.mChannelSpacing = 10;
            this.mDEConstant = 1;
        }
    }

    private void queueUpdate(int what, long delay) {
        log("queueUpdate(" + what + FingerprintManager.FINGER_PERMISSION_DELIMITER + delay + ") is called");
        if (what == 200) {
            log("queueUpdate ## VOLUME_FADEIN");
            this.mHandler.removeMessages(what);
        }
        this.mHandler.sendEmptyMessageDelayed(what, delay);
    }

    public static void log(String str) {
        Log.i("FMRadioService", str);
    }

    public Context getContext() {
        return this.mContext;
    }

    public FMRadioService(Context context) {
        this.mContext = context;
        this.mPlayerNative = new FMPlayerNative(this);
        this.mPowerManager = (PowerManager) context.getSystemService(SMProviderContract.KEY_POWER);
        this.mTelephonyManager = (TelephonyManager) context.getSystemService(PhoneConstants.PHONE_KEY);
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        this.mWakeLock = this.mPowerManager.newWakeLock(1, "FMRadio Service");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        intentFilter.addAction(KNOX_MODE_USER_SWITCH);
        intentFilter.addAction("android.intent.action.TVOUT_PLUG");
        context.registerReceiver(this.mReceiver, intentFilter);
        context.registerReceiver(this.mVolumeEventReceiver, new IntentFilter("android.media.VOLUME_CHANGED_ACTION"));
        context.registerReceiver(this.mVolumeEventReceiver, new IntentFilter(ACTION_VOLUME_LOCK));
        context.registerReceiver(this.mVolumeEventReceiver, new IntentFilter(ACTION_VOLUME_UNLOCK));
        this.mAirPlaneEnabled = Global.getInt(this.mContext.getContentResolver(), Settings$System.AIRPLANE_MODE_ON, 0) != 0;
        System.out.println("mAirPlaneEnabled flag :" + this.mAirPlaneEnabled);
        context.registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.AIRPLANE_MODE"));
        context.registerReceiver(this.mButtonReceiver, new IntentFilter("android.media.AUDIO_BECOMING_NOISY"));
        context.registerReceiver(this.mReceiver, new IntentFilter(ACTION_RECORDING_SAVE));
        context.registerReceiver(this.mReceiver, new IntentFilter(ACTION_SAVE_FMRECORDING_ONLY));
        registerSystemListener();
        registerTelephonyListener();
        registerSetPropertyListener();
        readChipVendor();
        readTuningParameters();
        readParametersForCurrentRegion();
        this.platform = SystemProperties.get("ro.board.platform");
        log("platform : " + this.platform);
        if (this.platform != null) {
            if ("exynos4".equals(this.platform) || "msm8960".equals(this.platform)) {
                this.mWakeUpKeyFilePath = "/sys/class/sec/sec_key/";
                this.mWakeUpKeyFileName = "wakeup_keys";
                this.mFmOn = "102,114,115,116,172";
                this.mFmOff = "102,116,172";
            } else if ("msm7627a".equals(this.platform) || "msm7k".equals(this.platform)) {
                this.mWakeUpKeyFilePath = "/sys/devices/platform/gpio-event/";
                this.mWakeUpKeyFileName = "wakeup_keys";
                this.mFmOn = SmartFaceManager.PAGE_BOTTOM;
                this.mFmOff = SmartFaceManager.PAGE_MIDDLE;
            } else if ("montblanc".equals(this.platform)) {
                this.mWakeUpKeyFilePath = "/sys/devices/platform/gpio-keys.0/";
                this.mWakeUpKeyFileName = "wakeup_keys";
                this.mFmOn = "114,115,172";
                this.mFmOff = "172";
            } else if ("mrvl".equals(this.platform)) {
                this.mWakeUpKeyFilePath = "/sys/devices/platform/pxa27x-keypad/power/";
                this.mWakeUpKeyFileName = "wakeup";
                this.mFmOn = "enabled";
                this.mFmOff = "disabled";
            }
        }
        if (this.platform == null) {
            return;
        }
        if ("rhea".equals(this.platform) || "capri".equals(this.platform) || "hawaii".equals(this.platform) || "java".equals(this.platform) || "sc8810".equals(this.platform) || "sc8825".equals(this.platform) || "sc6820i".equals(this.platform) || "sc8830".equals(this.platform) || "scx15".equals(this.platform) || "u2".equals(this.platform)) {
            this.mIsKeepRecoding = false;
        }
    }

    private void registerBatteryListener() {
        IntentFilter intentFilterBattery = new IntentFilter();
        intentFilterBattery.addAction("android.intent.action.BATTERY_CHANGED");
        this.mContext.registerReceiver(this.mLowBatteryReceiver, intentFilterBattery);
        log("registering low battery listener");
    }

    private void unRegisterBatteryListener() {
        this.mContext.unregisterReceiver(this.mLowBatteryReceiver);
        log("unregistering low battery listener");
    }

    private void registerSystemListener() {
        IntentFilter intentSystemFilter = new IntentFilter();
        intentSystemFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        this.mContext.registerReceiver(this.mSystemReceiver, intentSystemFilter);
        intentSystemFilter = new IntentFilter();
        intentSystemFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentSystemFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentSystemFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        intentSystemFilter.addDataScheme(AbsCocktailLoadablePanel.PACKAGE_NAME);
        this.mContext.registerReceiver(this.mSystemReceiver1, intentSystemFilter);
    }

    private void unregisterSystemListener() {
        this.mContext.unregisterReceiver(this.mSystemReceiver);
    }

    private void registerSetPropertyListener() {
        IntentFilter intentFilterSetProperty = new IntentFilter();
        intentFilterSetProperty.addAction("com.sec.android.app.fm.set_property");
        intentFilterSetProperty.addAction("com.sec.android.app.fm.set_volume");
        this.mContext.registerReceiver(this.mSetPropertyReceiver, intentFilterSetProperty, this.SetPropertyPermission, null);
        log("registering set property listener");
    }

    private void unRegisterSetPropertyListener() {
        this.mContext.unregisterReceiver(this.mSetPropertyReceiver);
        log("unregistering set property listener");
    }

    public void tune(long freq) {
        if (this.mChipVendor != 3) {
            log("tune mute - ");
            this.mAudioManager.setParameters(audioMute);
        } else {
            this.mAudioManager.setParameters("fmradio_turnon=false");
        }
        this.mPlayerNative.tune(freq);
        if (this.mAudioManager.getStreamVolume(SamsungAudioManager.stream(1)) > 0 && Settings$System.getInt(this.mContext.getContentResolver(), "all_sound_off", 0) != 1) {
            log("tune unmute - ");
            this.mAudioManager.setParameters(audioUnMute);
            if (this.mChipVendor == 3) {
                this.mPlayerNative.muteOff();
            }
        }
        notifyEvent(7, Long.valueOf(freq));
    }

    public void mute(boolean value) {
        log("mute - " + value);
        if (value) {
            this.mAudioManager.setParameters(audioMute);
            this.mPlayerNative.muteOn();
            this.mIsMute = true;
            return;
        }
        this.mPlayerNative.muteOff();
        this.mAudioManager.setParameters(audioUnMute);
        this.mIsMute = false;
    }

    public long[] getLastScanResult() {
        if (this.mScanChannelList != null) {
            return convertToPrimitives((Long[]) this.mScanChannelList.toArray(new Long[0]));
        }
        log("getLastScanResult - mScanChannelList null");
        return null;
    }

    public long seekUp() {
        this.mIsSeeking = true;
        if (this.mChipVendor != 3) {
            this.mAudioManager.setParameters(audioMute);
        } else {
            this.mAudioManager.setParameters("fmradio_turnon=false");
        }
        long freq = this.mPlayerNative.seekUp();
        if (this.mChipVendor != 3 && this.mAudioManager.getStreamVolume(SamsungAudioManager.stream(1)) > 0) {
            this.mAudioManager.setParameters(audioUnMute);
        }
        this.mIsSeeking = false;
        notifyEvent(7, Long.valueOf(freq));
        return freq;
    }

    public long seekDown() {
        this.mIsSeeking = true;
        if (this.mChipVendor != 3) {
            this.mAudioManager.setParameters(audioMute);
        } else {
            this.mAudioManager.setParameters("fmradio_turnon=false");
        }
        long freq = this.mPlayerNative.seekDown();
        if (this.mChipVendor != 3 && this.mAudioManager.getStreamVolume(SamsungAudioManager.stream(1)) > 0) {
            this.mAudioManager.setParameters(audioUnMute);
        }
        this.mIsSeeking = false;
        notifyEvent(7, Long.valueOf(freq));
        return freq;
    }

    public void cancelSeek() {
        this.mPlayerNative.cancelSeek();
    }

    public int isBusy() {
        if (this.mScanProgress) {
            return 1;
        }
        return -1;
    }

    public boolean isHeadsetPlugged() {
        return this.mIsHeadsetPlugged;
    }

    public boolean isTvOutPlugged() {
        return this.mIsTvOutPlugged;
    }

    public boolean isBatteryLow() {
        return this.mIsBatteryLow;
    }

    public boolean isAirPlaneMode() {
        return this.mAirPlaneEnabled;
    }

    public long getCurrentChannel() {
        return this.mPlayerNative.getCurrentChannel();
    }

    public void setListener(IFMEventListener listener) {
        log("[FMRadioService] setListener :" + listener);
        if (listener != null) {
            synchronized (listener) {
                if (this.mListeners == null) {
                    this.mListeners = new Vector();
                }
                this.mListeners.addElement(new ListenerRecord(listener, listener.asBinder()));
                log("no of listener:" + this.mListeners.size());
            }
        }
    }

    public void removeListener(IFMEventListener listener) {
        log("[FMRadioService] (removeListener) :" + listener);
        if (listener != null) {
            remove(listener);
        }
    }

    public synchronized boolean on_in_testmode() {
        boolean z = false;
        synchronized (this) {
            if (!this.mAirPlaneEnabled) {
                if (!(this.mTelephonyManager.getCallState() == 1 || this.mTelephonyManager.getCallState() == 2)) {
                    if (this.mIsOn) {
                        z = true;
                    } else {
                        try {
                            if (this.mPlayerNative.on() > 0) {
                                setSoftmute(false);
                                this.mIsOn = true;
                                this.mIsTestMode = true;
                                notifyEvent(5, null);
                                mute(false);
                                this.mAudioManager.setParameters("fmradio_turnon=true");
                                log("on_in_testmode Turning on FM radio");
                                z = true;
                            } else {
                                releaseWakeLock();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            releaseWakeLock();
                        }
                    }
                }
            }
        }
        return z;
    }

    public boolean on() {
        return on(true);
    }

    private synchronized boolean on(boolean isAudioFocusNeeded) {
        boolean z = false;
        synchronized (this) {
            if (this.mIsHeadsetPlugged) {
                if (!(this.mIsTvOutPlugged || this.mAirPlaneEnabled)) {
                    registerBatteryListener();
                    if (!(this.mIsBatteryLow || this.mTelephonyManager.getCallState() == 1 || this.mTelephonyManager.getCallState() == 2 || this.alarmTTSPlay)) {
                        if (this.mIsOn) {
                            z = true;
                        } else {
                            try {
                                String mPackageName = getContext().getPackageManager().getPackagesForUid(Binder.getCallingUid())[0];
                                if (isAudioFocusNeeded || !mIsTransientPaused) {
                                    log("AudioFocusListener registered");
                                    if (!(CscFeature.getInstance().getEnableStatus("CscFeature_Common_SupportNextRadio") && APP_NAME.equals(mPackageName))) {
                                        this.mAudioManager.requestAudioFocus(this.mAudioFocusListener, SamsungAudioManager.stream(1), 1);
                                    }
                                } else {
                                    log("AudioFocusListener : skip the requestAudioFocus");
                                }
                                this.mOnProgress = true;
                                if (this.mPlayerNative.on() > 0) {
                                    log("on returned from native");
                                    this.mOnProgress = false;
                                    this.mIsOn = true;
                                    mute(true);
                                    if (this.mIsHeadsetPlugged) {
                                        if (this.mChipVendor == 3) {
                                            this.mAudioManager.setRadioSpeakerOn(this.mAudioManager.isRadioSpeakerOn());
                                            log("setRadioSpeakerOn() is called.");
                                        }
                                        notifyEvent(5, null);
                                        if (this.mChipVendor == 4) {
                                            setSINRThreshold(this.mSnr_th);
                                            setSearchAlgoType(this.mAlgo_type);
                                            setSINRFirstStage(this.mSnr_th_2);
                                            setRMSSIFirstStage(this.mRssi_th);
                                            setOnChannelThreshold(this.mCnt_th);
                                            setOffChannelThreshold(this.mCnt_th_2);
                                            setSINRSamples(this.mRssi_th_2);
                                            setCFOTh12(this.mCf0_th12);
                                            setAFRMSSIThreshold(this.mAfRmssith_th);
                                            setAFRMSSISamples(this.mAfRmssisampleCnt_th);
                                            setGoodChannelRMSSIThreshold(this.mgoodChrmssi_th);
                                        } else {
                                            setSignalSetting(this.mRssi_th, this.mSnr_th, this.mCnt_th);
                                        }
                                        setBand(this.mBand);
                                        setChannelSpacing(this.mChannelSpacing);
                                        setDEConstant((long) this.mDEConstant);
                                        registerMusicCommandRec();
                                        registerAlarmListener();
                                        registerAllSoundOffListener();
                                        if (FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_SETTINGS_SUPPORT_MOTION")) {
                                            registerMotionListener();
                                        }
                                        this.mContext.registerReceiver(this.mIsFMBackGrondPlaying, new IntentFilter(ACTION_BACKGROUND_PLAY));
                                        registerEmergencyStateChangedListener();
                                        if (this.platform == null || !this.platform.equals("mrvl")) {
                                            InputManager inputManager = (InputManager) this.mContext.getSystemService("input");
                                            if (!(this.mIsOn == mIsSetWakeKey || inputManager == null)) {
                                                try {
                                                    inputManager.setWakeKeyDynamically(APP_NAME, this.mIsOn, VOLUME_UP_DOWN);
                                                } catch (SecurityException se) {
                                                    log("Exception in setWakeKeyDynamically(): " + se.toString());
                                                }
                                                mIsSetWakeKey = this.mIsOn;
                                            }
                                        } else {
                                            openFile();
                                            writeFile(this.mIsOn);
                                            closeFile();
                                        }
                                        this.mIsForcestop = false;
                                        z = true;
                                    } else {
                                        offInternal(true, 2, true);
                                    }
                                } else {
                                    this.mOnProgress = false;
                                    log("on is failed :: remove audiofocus ");
                                    this.mAudioManager.abandonAudioFocus(this.mAudioFocusListener);
                                    releaseWakeLock();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                this.mIsOn = false;
                                this.mOnProgress = false;
                                log("on is failed by exception :: remove audiofocus ");
                                this.mAudioManager.abandonAudioFocus(this.mAudioFocusListener);
                                releaseWakeLock();
                                unRegisterBatteryListener();
                            }
                        }
                    }
                }
            }
        }
        return z;
    }

    private void registerMusicCommandRec() {
        this.mContext.registerReceiver(this.mMusicCommandRec, new IntentFilter(ACTION_MUSIC_COMMAND));
        log("music command reciever registered");
    }

    private void sendStopMusicCommandBroadcast() {
        Intent intent = new Intent(ACTION_MUSIC_COMMAND);
        intent.putExtra(MagazineIntents.COMMAND, "stop");
        intent.putExtra("from", APP_NAME);
        this.mContext.sendBroadcast(intent);
        log("music command stop sent ..");
    }

    private void registerTelephonyListener() {
        long id = Binder.clearCallingIdentity();
        try {
            this.mTelephonyManager.listen(this.mPhoneListener, 32);
            log("registering telephony listener..");
        } finally {
            Binder.restoreCallingIdentity(id);
        }
    }

    private void unRegisterTelephonyListener() {
        long id = Binder.clearCallingIdentity();
        try {
            this.mTelephonyManager.listen(this.mPhoneListener, 0);
            log("unregistering telephony listener");
        } finally {
            Binder.restoreCallingIdentity(id);
        }
    }

    public void setVolume(long val) {
        if (!this.mIsOn) {
            return;
        }
        if (this.mScanProgress) {
            log("setVolume :: unset on ScanProgress");
            return;
        }
        try {
            if (!this.mInternetStreamingMode) {
                this.mPlayerNative.setVolume(val);
                this.mResumeVol = val;
                if (Settings$System.getInt(this.mContext.getContentResolver(), "all_sound_off", 0) == 1) {
                    log("setVolume :: AllSoundOff is enabled. So FMRadio is muted.");
                    if (!this.mIsMute) {
                        mute(true);
                    }
                } else if (this.mChipVendor == 3) {
                } else {
                    if (val <= 0) {
                        mute(true);
                    } else if (this.mIsMute) {
                        mute(false);
                        this.mPlayerNative.setVolume(val);
                    }
                }
            } else if (!this.mIsMute) {
                this.mPlayerNative.setVolume(0);
                mute(true);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public long getVolume() {
        return this.mPlayerNative.getVolume();
    }

    public void setSpeakerOn(boolean bSpeakerOn) {
        this.mPlayerNative.setSpeakerOn(bSpeakerOn);
    }

    public void setRecordMode(int is_record) {
        this.mPlayerNative.setRecordMode(is_record);
    }

    public long getMaxVolume() {
        return this.mPlayerNative.getMaxVolume();
    }

    private void releaseWakeLock() {
        if (this.mWakeLock.isHeld()) {
            this.mWakeLock.release();
            log("Lock is released");
        }
    }

    private void acquireWakeLock() {
        if (!this.mWakeLock.isHeld()) {
            this.mWakeLock.acquire();
            log("Lock is held");
        }
    }

    public boolean isOn() {
        return this.mIsOn;
    }

    public void SetIsOn(boolean IsOn) {
        this.mIsOn = IsOn;
    }

    private synchronized boolean offInternal(boolean isModeToSet, int reasonCode, boolean needToRemoveFocusListener) {
        boolean z = true;
        synchronized (this) {
            log("offInternal :: reasonCode=" + reasonCode);
            if (this.mIsOn) {
                try {
                    boolean z2;
                    mIsTransientPaused = !needToRemoveFocusListener;
                    this.mIsOn = false;
                    if (isModeToSet) {
                        if (this.mReturnBackVolume != -1 && this.mAudioManager.isRadioSpeakerOn()) {
                            log("offInternal :: mReturnBackVolume=" + this.mReturnBackVolume);
                            this.mAudioManager.setStreamVolume(SamsungAudioManager.stream(1), this.mReturnBackVolume, 0);
                            this.mReturnBackVolume = -1;
                        }
                        this.mAudioManager.setParameters("fmradio_turnon=false");
                        log("offInternal Turning off FM radio");
                    } else {
                        log("offInternal NOT Turning off FM radio");
                    }
                    if (needToRemoveFocusListener) {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                    this.mIsAudioFocusAlive = z2;
                    if (this.mChipVendor == 2 && reasonCode == 3) {
                        this.mPlayerNative.offFMService();
                    } else {
                        this.mPlayerNative.off();
                    }
                    log("off returned from native");
                    this.mRDSEnable = false;
                    this.mAFEnable = false;
                    this.mDNSEnable = false;
                    this.mIsMute = false;
                    this.mIsSkipTunigVal = false;
                    if (!this.mIsTestMode) {
                        unRegisterMusicCommandRec();
                        unRegisterBatteryListener();
                        if (FloatingFeature.getInstance().getEnableStatus("SEC_FLOATING_FEATURE_SETTINGS_SUPPORT_MOTION")) {
                            unregisterMotionListener();
                        }
                        if (!this.alarmTTSPlay) {
                            unregisterAlarmListener();
                        }
                        unregisterAllSoundOffListener();
                        this.mContext.unregisterReceiver(this.mIsFMBackGrondPlaying);
                        if (needToRemoveFocusListener) {
                            unregisterEmegencyStateChangedListener();
                        }
                    }
                    this.mIsTestMode = false;
                    if (needToRemoveFocusListener) {
                        this.mAudioManager.abandonAudioFocus(this.mAudioFocusListener);
                    }
                    notifyEvent(6, Integer.valueOf(reasonCode));
                    sendFMOFFBroadcast();
                    if (this.platform == null || !this.platform.equals("mrvl")) {
                        InputManager inputManager = (InputManager) this.mContext.getSystemService("input");
                        if (!(this.mIsOn == mIsSetWakeKey || inputManager == null)) {
                            try {
                                inputManager.setWakeKeyDynamically(APP_NAME, this.mIsOn, VOLUME_UP_DOWN);
                            } catch (SecurityException se) {
                                log("Exception in setWakeKeyDynamically(): " + se.toString());
                            }
                            mIsSetWakeKey = this.mIsOn;
                        }
                    } else {
                        openFile();
                        writeFile(this.mIsOn);
                        closeFile();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    z = false;
                    return z;
                } finally {
                    releaseWakeLock();
                }
            } else if (needToRemoveFocusListener) {
                log("offInternal :: remove audiofocus ");
                this.mAudioManager.abandonAudioFocus(this.mAudioFocusListener);
            }
        }
        return z;
    }

    private void sendFMOFFBroadcast() {
        log("Sending broadcast FM is in OFF state");
        Intent intent = new Intent("com.sec.android.fm.player_lock.status.off");
        intent.setFlags(268435456);
        this.mContext.sendBroadcast(intent);
    }

    void sendFMStatusBroadcast(Long freq) {
        float currentFreq = ((float) freq.longValue()) / 1000.0f;
        if (currentFreq == 0.0f) {
            currentFreq = 87.5f;
        }
        if (DEBUGGABLE) {
            log("Sending broadcast tune currentFreq = " + currentFreq);
        }
        Intent intent = new Intent("com.sec.android.fm.player_lock.tune.channel");
        intent.setFlags(268435456);
        intent.putExtra("freq", currentFreq + "");
        this.mContext.sendBroadcast(intent);
    }

    public boolean off() {
        return offInternal(true, 0, true);
    }

    private void unRegisterMusicCommandRec() {
        this.mContext.unregisterReceiver(this.mMusicCommandRec);
        log("music command reciever un-registered");
    }

    private void remove(IFMEventListener listener) {
        if (this.mListeners != null && this.mListeners.size() != 0) {
            synchronized (this.mListeners) {
                for (int i = 0; i < this.mListeners.size(); i++) {
                    if (((ListenerRecord) this.mListeners.get(i)).mBinder == listener.asBinder()) {
                        log("[FMRadioService] deleted Listener :" + ((ListenerRecord) this.mListeners.remove(i)));
                        return;
                    }
                }
            }
        }
    }

    public void scan() {
        if (!this.mScanProgress) {
            this.mScanProgress = true;
            this.mScanThread = new ScanThread();
            this.mScanThread.start();
        }
    }

    public boolean isScanning() {
        return this.mScanProgress;
    }

    public boolean isSeeking() {
        return this.mIsSeeking;
    }

    public long getCurrentRSSI() {
        return this.mPlayerNative.getCurrentRSSI();
    }

    public long getCurrentSNR() {
        return this.mPlayerNative.getCurrentSNR();
    }

    public boolean cancelScan() {
        try {
            if (!this.mScanProgress) {
                return false;
            }
            this.mScanProgress = false;
            this.mPlayerNative.cancelSeek();
            if (this.mScanChannelList != null) {
                notifyEvent(4, this.mScanChannelList.toArray(new Long[0]));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public long searchUp() {
        if (this.mIsOn) {
            return this.mPlayerNative.searchUp();
        }
        return -1;
    }

    public long searchAll() {
        if (this.mIsOn) {
            return this.mPlayerNative.searchAll();
        }
        return -1;
    }

    public long searchDown() {
        return this.mPlayerNative.searchDown();
    }

    public void enableRDS() {
        if (this.mRDSEnable) {
            log("RDS is already enabled");
            return;
        }
        this.mPlayerNative.enableRDS();
        this.mRDSEnable = true;
        notifyEvent(11, null);
        acquireWakeLock();
    }

    public void disableRDS() {
        this.mRDSEnable = false;
        this.mPlayerNative.disableRDS();
        notifyEvent(12, null);
        checkForWakeLockRelease();
    }

    public void enableDNS() {
        if (this.mDNSEnable) {
            log("DNS is already enabled");
            return;
        }
        this.mPlayerNative.enableDNS();
        this.mDNSEnable = true;
        acquireWakeLock();
    }

    public void disableDNS() {
        this.mDNSEnable = false;
        this.mPlayerNative.disableDNS();
        checkForWakeLockRelease();
    }

    public boolean isDNSEnable() {
        return this.mDNSEnable;
    }

    public void enableAF() {
        if (this.mAFEnable) {
            log("AF is already enabled");
            return;
        }
        this.mPlayerNative.enableAF();
        this.mAFEnable = true;
        acquireWakeLock();
    }

    public void disableAF() {
        this.mAFEnable = false;
        this.mPlayerNative.disableAF();
        checkForWakeLockRelease();
    }

    private void checkForWakeLockRelease() {
        if (!this.mAFEnable && !this.mRDSEnable && !this.mDNSEnable) {
            log("AF and RDS is off. release the wake lock");
            releaseWakeLock();
        }
    }

    public void setBand(int band) {
        this.mPlayerNative.setBand(band);
        this.mBand = band;
    }

    public void setChannelSpacing(int spacing) {
        this.mPlayerNative.setChannelSpacing(spacing);
    }

    public boolean isRDSEnable() {
        return this.mRDSEnable;
    }

    public boolean isAFEnable() {
        return this.mAFEnable;
    }

    public void cancelAFSwitching() {
        this.mPlayerNative.cancelAFSwitching();
    }

    public void setStereo() {
        this.mPlayerNative.setStereo();
    }

    public void setMono() {
        this.mPlayerNative.setMono();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void notifyEvent(int r23, java.lang.Object r24) {
        /*
        r22 = this;
        r0 = r22;
        r2 = r0.mIsOn;
        if (r2 == 0) goto L_0x0025;
    L_0x0006:
        r2 = 7;
        r0 = r23;
        if (r0 != r2) goto L_0x0025;
    L_0x000b:
        r15 = "fmradio_turnon=true";
        r0 = r22;
        r2 = r0.mAudioManager;
        r2.setParameters(r15);
        r2 = "notifyEvent Turning on FM radio";
        log(r2);
        if (r24 == 0) goto L_0x0025;
    L_0x001c:
        r2 = r24;
        r2 = (java.lang.Long) r2;
        r0 = r22;
        r0.sendFMStatusBroadcast(r2);
    L_0x0025:
        r0 = r22;
        r2 = r0.mListeners;
        if (r2 == 0) goto L_0x0035;
    L_0x002b:
        r0 = r22;
        r2 = r0.mListeners;
        r2 = r2.size();
        if (r2 != 0) goto L_0x0036;
    L_0x0035:
        return;
    L_0x0036:
        r0 = r22;
        r0 = r0.mListeners;
        r21 = r0;
        monitor-enter(r21);
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0120 }
        r2.<init>();	 Catch:{ all -> 0x0120 }
        r3 = "Total listener:";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0120 }
        r0 = r22;
        r3 = r0.mListeners;	 Catch:{ all -> 0x0120 }
        r3 = r3.size();	 Catch:{ all -> 0x0120 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0120 }
        r2 = r2.toString();	 Catch:{ all -> 0x0120 }
        log(r2);	 Catch:{ all -> 0x0120 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ all -> 0x0120 }
        r20 = r2.size();	 Catch:{ all -> 0x0120 }
        r14 = r20 + -1;
    L_0x0065:
        if (r14 < 0) goto L_0x0676;
    L_0x0067:
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0120 }
        r2.<init>();	 Catch:{ all -> 0x0120 }
        r3 = "Notifying listener:";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0120 }
        r2 = r2.append(r14);	 Catch:{ all -> 0x0120 }
        r2 = r2.toString();	 Catch:{ all -> 0x0120 }
        log(r2);	 Catch:{ all -> 0x0120 }
        switch(r23) {
            case 1: goto L_0x0174;
            case 2: goto L_0x01d1;
            case 3: goto L_0x0279;
            case 4: goto L_0x0213;
            case 5: goto L_0x0083;
            case 6: goto L_0x0123;
            case 7: goto L_0x02df;
            case 8: goto L_0x0342;
            case 9: goto L_0x0384;
            case 10: goto L_0x03c6;
            case 11: goto L_0x04d7;
            case 12: goto L_0x0519;
            case 13: goto L_0x055b;
            case 14: goto L_0x059d;
            case 15: goto L_0x05f2;
            case 16: goto L_0x0424;
            case 17: goto L_0x0634;
            case 18: goto L_0x048e;
            default: goto L_0x0080;
        };
    L_0x0080:
        r14 = r14 + -1;
        goto L_0x0065;
    L_0x0083:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_POWER_ON to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = "-->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.onOn();	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x00c4:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ all -> 0x0120 }
        r3 = "FMRadioService";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0120 }
        r2.<init>();	 Catch:{ all -> 0x0120 }
        r4 = "we loose ";
        r2 = r2.append(r4);	 Catch:{ all -> 0x0120 }
        r2 = r2.append(r14);	 Catch:{ all -> 0x0120 }
        r4 = " listener--ignore it :";
        r4 = r2.append(r4);	 Catch:{ all -> 0x0120 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ all -> 0x0120 }
        r2 = r2.get(r14);	 Catch:{ all -> 0x0120 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ all -> 0x0120 }
        r2 = r2.mListener;	 Catch:{ all -> 0x0120 }
        r2 = r4.append(r2);	 Catch:{ all -> 0x0120 }
        r2 = r2.toString();	 Catch:{ all -> 0x0120 }
        android.util.Log.e(r3, r2);	 Catch:{ all -> 0x0120 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ all -> 0x0120 }
        r2 = r2.get(r14);	 Catch:{ all -> 0x0120 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ all -> 0x0120 }
        r2 = r2.mListener;	 Catch:{ all -> 0x0120 }
        r0 = r22;
        r0.remove(r2);	 Catch:{ all -> 0x0120 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0120 }
        r2.<init>();	 Catch:{ all -> 0x0120 }
        r3 = "Remove done go for next i's value:";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0120 }
        r2 = r2.append(r14);	 Catch:{ all -> 0x0120 }
        r2 = r2.toString();	 Catch:{ all -> 0x0120 }
        log(r2);	 Catch:{ all -> 0x0120 }
        goto L_0x0080;
    L_0x0120:
        r2 = move-exception;
        monitor-exit(r21);	 Catch:{ all -> 0x0120 }
        throw r2;
    L_0x0123:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_POWER_OFF to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = "-->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        r18 = -1;
        if (r24 == 0) goto L_0x0161;
    L_0x0158:
        r0 = r24;
        r0 = (java.lang.Integer) r0;	 Catch:{ Exception -> 0x00c4 }
        r2 = r0;
        r18 = r2.intValue();	 Catch:{ Exception -> 0x00c4 }
    L_0x0161:
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r0 = r18;
        r2.onOff(r0);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x0174:
        r12 = 0;
        if (r24 == 0) goto L_0x0181;
    L_0x0178:
        r0 = r24;
        r0 = (java.lang.Long) r0;	 Catch:{ Exception -> 0x00c4 }
        r2 = r0;
        r12 = r2.longValue();	 Catch:{ Exception -> 0x00c4 }
    L_0x0181:
        r2 = DEBUGGABLE;	 Catch:{ Exception -> 0x00c4 }
        if (r2 == 0) goto L_0x01c0;
    L_0x0185:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_CHANNEL_FOUND to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " : with freq:";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r12);	 Catch:{ Exception -> 0x00c4 }
        r3 = "-->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
    L_0x01c0:
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.onChannelFound(r12);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x01d1:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_SCAN_STARTED to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " :";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.onScanStarted();	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x0213:
        if (r24 == 0) goto L_0x0271;
    L_0x0215:
        r0 = r24;
        r0 = (java.lang.Long[]) r0;	 Catch:{ Exception -> 0x00c4 }
        r2 = r0;
        r0 = r2;
        r0 = (java.lang.Long[]) r0;	 Catch:{ Exception -> 0x00c4 }
        r9 = r0;
        r0 = r22;
        r11 = r0.convertToPrimitives(r9);	 Catch:{ Exception -> 0x00c4 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_SCAN_STOPPED to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " : with data array:";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r3 = r11.length;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r3 = "-->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.onScanStopped(r11);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x0271:
        r2 = "notifying : EVENT_SCAN_STOPPED : data is null !!!";
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x0279:
        if (r24 == 0) goto L_0x02d7;
    L_0x027b:
        r0 = r24;
        r0 = (java.lang.Long[]) r0;	 Catch:{ Exception -> 0x00c4 }
        r2 = r0;
        r0 = r2;
        r0 = (java.lang.Long[]) r0;	 Catch:{ Exception -> 0x00c4 }
        r9 = r0;
        r0 = r22;
        r11 = r0.convertToPrimitives(r9);	 Catch:{ Exception -> 0x00c4 }
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_SCAN_FINISHED to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " : with data array:";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r3 = r11.length;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r3 = "-->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.onScanFinished(r11);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x02d7:
        r2 = "notifying : EVENT_SCAN_FINISHED : data is null !!!";
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x02df:
        if (r24 == 0) goto L_0x033a;
    L_0x02e1:
        r0 = r24;
        r0 = (java.lang.Long) r0;	 Catch:{ Exception -> 0x00c4 }
        r2 = r0;
        r12 = r2.longValue();	 Catch:{ Exception -> 0x00c4 }
        r2 = DEBUGGABLE;	 Catch:{ Exception -> 0x00c4 }
        if (r2 == 0) goto L_0x0329;
    L_0x02ee:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_TUNE to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " : with data array:";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r12);	 Catch:{ Exception -> 0x00c4 }
        r3 = "-->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
    L_0x0329:
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.onTune(r12);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x033a:
        r2 = "notifying : EVENT_TUNE : data is null !!!";
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x0342:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_EAR_PHONE_CONNECT to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = ": -->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.earPhoneConnected();	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x0384:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_EAR_PHONE_DISCONNECT to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " : ->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.earPhoneDisconnected();	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x03c6:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying : EVENT_RDS_EVENT : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " : ->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        if (r24 == 0) goto L_0x041c;
    L_0x03f9:
        r0 = r24;
        r0 = (com.android.server.FMPlayerNative.RDSData) r0;	 Catch:{ Exception -> 0x00c4 }
        r17 = r0;
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r0 = r17;
        r4 = r0.mFreq;	 Catch:{ Exception -> 0x00c4 }
        r0 = r17;
        r3 = r0.mChannelName;	 Catch:{ Exception -> 0x00c4 }
        r0 = r17;
        r6 = r0.mRadioText;	 Catch:{ Exception -> 0x00c4 }
        r2.onRDSReceived(r4, r3, r6);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x041c:
        r2 = "notifying : EVENT_RDS_EVENT : data is null !!!";
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x0424:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_RTPLUS_EVENT to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " : ->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        if (r24 == 0) goto L_0x0486;
    L_0x0457:
        r0 = r24;
        r0 = (com.android.server.FMPlayerNative.RTPlusData) r0;	 Catch:{ Exception -> 0x00c4 }
        r19 = r0;
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r0 = r19;
        r3 = r0.mContentType1;	 Catch:{ Exception -> 0x00c4 }
        r0 = r19;
        r4 = r0.mStartPos1;	 Catch:{ Exception -> 0x00c4 }
        r0 = r19;
        r5 = r0.mAdditionalLen1;	 Catch:{ Exception -> 0x00c4 }
        r0 = r19;
        r6 = r0.mContentType2;	 Catch:{ Exception -> 0x00c4 }
        r0 = r19;
        r7 = r0.mStartPos2;	 Catch:{ Exception -> 0x00c4 }
        r0 = r19;
        r8 = r0.mAdditionalLen2;	 Catch:{ Exception -> 0x00c4 }
        r2.onRTPlusReceived(r3, r4, r5, r6, r7, r8);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x0486:
        r2 = "notifying : EVENT_RTPLUS_EVENT : data is null !!!";
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x048e:
        if (r24 == 0) goto L_0x04cf;
    L_0x0490:
        r0 = r24;
        r0 = (com.android.server.FMPlayerNative.PIECCData) r0;	 Catch:{ Exception -> 0x00c4 }
        r16 = r0;
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r0 = r16;
        r3 = r0.mPI;	 Catch:{ Exception -> 0x00c4 }
        r0 = r16;
        r4 = r0.mECC;	 Catch:{ Exception -> 0x00c4 }
        r2.onPIECCReceived(r3, r4);	 Catch:{ Exception -> 0x00c4 }
        r2 = com.android.server.FMRadioServiceFeature.FEATURE_DISABLEDNS;	 Catch:{ Exception -> 0x00c4 }
        if (r2 != 0) goto L_0x0080;
    L_0x04b1:
        r0 = r22;
        r2 = r0.mWaitPidDuringScanning;	 Catch:{ Exception -> 0x00c4 }
        if (r2 == 0) goto L_0x0080;
    L_0x04b7:
        r0 = r22;
        r2 = r0.mScanThread;	 Catch:{ Exception -> 0x00c4 }
        if (r2 == 0) goto L_0x0080;
    L_0x04bd:
        r0 = r22;
        r3 = r0.mScanThread;	 Catch:{ Exception -> 0x00c4 }
        monitor-enter(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mScanThread;	 Catch:{ all -> 0x04cc }
        r2.notify();	 Catch:{ all -> 0x04cc }
        monitor-exit(r3);	 Catch:{ all -> 0x04cc }
        goto L_0x0080;
    L_0x04cc:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x04cc }
        throw r2;	 Catch:{ Exception -> 0x00c4 }
    L_0x04cf:
        r2 = "notifying : EVENT_PIECC_EVENT : data is null !!!";
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x04d7:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_RDS_ENABLED to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " : ->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.onRDSEnabled();	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x0519:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_RDS_DISABLED to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " : ->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.onRDSDisabled();	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x055b:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_AF_STARTED to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " : ->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.onAFStarted();	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x059d:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_AF_RECEIVED to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " : ->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        if (r24 == 0) goto L_0x05ea;
    L_0x05d0:
        r0 = r24;
        r0 = (java.lang.Long) r0;	 Catch:{ Exception -> 0x00c4 }
        r2 = r0;
        r12 = r2.longValue();	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.onAFReceived(r12);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x05ea:
        r2 = "notifying : EVENT_AF_RECEIVED : data is null !!!";
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x05f2:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_VOLUME_LOCK to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " : ->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.volumeLock();	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x0634:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00c4 }
        r2.<init>();	 Catch:{ Exception -> 0x00c4 }
        r3 = "notifying :EVENT_REC_FINISH to : listener -->";
        r2 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.append(r14);	 Catch:{ Exception -> 0x00c4 }
        r3 = " : ->";
        r3 = r2.append(r3);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.asBinder();	 Catch:{ Exception -> 0x00c4 }
        r2 = r3.append(r2);	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x00c4 }
        log(r2);	 Catch:{ Exception -> 0x00c4 }
        r0 = r22;
        r2 = r0.mListeners;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.get(r14);	 Catch:{ Exception -> 0x00c4 }
        r2 = (com.android.server.FMRadioService.ListenerRecord) r2;	 Catch:{ Exception -> 0x00c4 }
        r2 = r2.mListener;	 Catch:{ Exception -> 0x00c4 }
        r2.recFinish();	 Catch:{ Exception -> 0x00c4 }
        goto L_0x0080;
    L_0x0676:
        monitor-exit(r21);	 Catch:{ all -> 0x0120 }
        goto L_0x0035;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.FMRadioService.notifyEvent(int, java.lang.Object):void");
    }

    private long[] convertToPrimitives(Long[] longObArray) {
        if (longObArray == null) {
            return null;
        }
        long[] jArr = new long[longObArray.length];
        for (int i = 0; i < jArr.length; i++) {
            jArr[i] = longObArray[i].longValue();
        }
        return jArr;
    }

    public void setDEConstant(long value) {
        this.mPlayerNative.setDEConstant(value);
    }

    public void setSeekRSSI(long value) {
        this.mPlayerNative.setSeekRSSI(value);
    }

    public void setSeekSNR(long value) {
        this.mPlayerNative.setSeekSNR(value);
    }

    public void setRSSI_th(int value) {
        this.mRssi_th = value;
    }

    public void setSNR_th(int value) {
        this.mSnr_th = value;
    }

    public void setCnt_th(int value) {
        this.mCnt_th = value;
    }

    public void setRSSI_th_2(int value) {
        this.mRssi_th_2 = value;
    }

    public void setSNR_th_2(int value) {
        this.mSnr_th_2 = value;
    }

    public void setCnt_th_2(int value) {
        this.mCnt_th_2 = value;
    }

    public void SkipTuning_Value() {
        this.mIsSkipTunigVal = true;
        Log.e("FMRadioService", "SkipTuning_Value");
    }

    public int getRSSI_th() {
        return this.mRssi_th;
    }

    public int getSNR_th() {
        return this.mSnr_th;
    }

    public int getCnt_th() {
        return this.mCnt_th;
    }

    public int getRSSI_th_2() {
        return this.mRssi_th_2;
    }

    public int getSNR_th_2() {
        return this.mSnr_th_2;
    }

    public int getCnt_th_2() {
        return this.mCnt_th_2;
    }

    public void setAF_th(int value) {
        this.mPlayerNative.setAF_th(value);
    }

    public int getAF_th() {
        return this.mPlayerNative.getAF_th();
    }

    public void setAFValid_th(int value) {
        this.mPlayerNative.setAFValid_th(value);
    }

    public int getAFValid_th() {
        return this.mPlayerNative.getAFValid_th();
    }

    public void setFMIntenna(boolean setFMIntenna) {
        this.mPlayerNative.setFMIntenna(setFMIntenna);
    }

    public void setSoftmute(boolean setSoftmute) {
        this.mPlayerNative.setSoftmute(setSoftmute);
    }

    public boolean getSoftMuteMode() {
        return this.mPlayerNative.getSoftMuteMode();
    }

    public void setSoftMuteControl(int min_RSSI, int max_RSSI, int max_attenuation) {
        this.mPlayerNative.setSoftMuteControl(min_RSSI, max_RSSI, max_attenuation);
    }

    public void setSearchAlgoType(int value) {
        this.mPlayerNative.setSearchAlgoType(value);
    }

    public int getSearchAlgoType() {
        return this.mPlayerNative.getSearchAlgoType();
    }

    public void setSINRSamples(int value) {
        this.mPlayerNative.setSINRSamples(value);
    }

    public int getSINRSamples() {
        return this.mPlayerNative.getSINRSamples();
    }

    public void setOnChannelThreshold(int value) {
        this.mPlayerNative.setOnChannelThreshold(value);
    }

    public int getOnChannelThreshold() {
        return this.mPlayerNative.getOnChannelThreshold();
    }

    public void setOffChannelThreshold(int value) {
        this.mPlayerNative.setOffChannelThreshold(value);
    }

    public int getOffChannelThreshold() {
        return this.mPlayerNative.getOffChannelThreshold();
    }

    public void setSINRThreshold(int value) {
        this.mPlayerNative.setSINRThreshold(value);
    }

    public int getSINRThreshold() {
        return this.mPlayerNative.getSINRThreshold();
    }

    public void setCFOTh12(int value) {
        this.mPlayerNative.setCFOTh12(value);
    }

    public int getCFOTh12() {
        return this.mPlayerNative.getCFOTh12();
    }

    public void setRMSSIFirstStage(int value) {
        this.mPlayerNative.setRMSSIFirstStage(value);
    }

    public int getRMSSIFirstStage() {
        return this.mPlayerNative.getRMSSIFirstStage();
    }

    public void setSINRFirstStage(int value) {
        this.mPlayerNative.setSINRFirstStage(value);
    }

    public int getSINRFirstStage() {
        return this.mPlayerNative.getSINRFirstStage();
    }

    public void setAFRMSSIThreshold(int value) {
        this.mPlayerNative.setAFRMSSIThreshold(value);
    }

    public int getAFRMSSIThreshold() {
        return this.mPlayerNative.getAFRMSSIThreshold();
    }

    public void setAFRMSSISamples(int value) {
        this.mPlayerNative.setAFRMSSISamples(value);
    }

    public int getAFRMSSISamples() {
        return this.mPlayerNative.getAFRMSSISamples();
    }

    public void setGoodChannelRMSSIThreshold(int value) {
        this.mPlayerNative.setGoodChannelRMSSIThreshold(value);
    }

    public int getGoodChannelRMSSIThreshold() {
        return this.mPlayerNative.getGoodChannelRMSSIThreshold();
    }

    public void setHybridSearch(String value) {
        this.mPlayerNative.setHybridSearch(value);
    }

    public String getHybridSearch() {
        return this.mPlayerNative.getHybridSearch();
    }

    public void setSeekDC(int value) {
        this.mPlayerNative.setSeekDC(value);
    }

    public int getSeekDC() {
        return this.mPlayerNative.getSeekDC();
    }

    public void setSeekQA(int value) {
        this.mPlayerNative.setSeekQA(value);
    }

    public int getSeekQA() {
        return this.mPlayerNative.getSeekQA();
    }

    private void openFile() {
        File fileFMRadio = new File(this.mWakeUpKeyFilePath + this.mWakeUpKeyFileName);
        if (!fileFMRadio.exists()) {
            try {
                fileFMRadio.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            this.fos = new FileOutputStream(fileFMRadio);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
    }

    private void closeFile() {
        try {
            if (this.fos != null) {
                this.fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFile(boolean isFmRadioOn) {
        String value = isFmRadioOn ? this.mFmOn : this.mFmOff;
        if (DEBUGGABLE) {
            log("writeFile: value = " + value);
        }
        try {
            if (this.fos != null) {
                log("writeFile: data " + value);
                this.fos.write(value.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void finalize() throws Throwable {
        super.finalize();
        try {
            if (this.mWakeLock != null && this.mWakeLock.isHeld()) {
                this.mWakeLock.release();
            }
            unregisterSystemListener();
            unRegisterSetPropertyListener();
            this.mScanProgress = false;
            this.mWakeLock = null;
            this.mPowerManager = null;
            this.mListeners = null;
            this.mPlayerNative = null;
            this.mScanChannelList = null;
            this.mAudioManager = null;
        } catch (Error e) {
        }
    }

    private void setSignalSetting(int rssi, int snr, int cnt) {
        if (this.mIsOn) {
            this.mPlayerNative.setRSSI_th(rssi);
            this.mPlayerNative.setSNR_th(snr);
            this.mPlayerNative.setCnt_th(cnt);
        }
    }

    public void setInternetStreamingMode(boolean mode) {
        log("setInternetStreamingMode :: mode:" + mode);
        this.mInternetStreamingMode = mode;
        if (mode) {
            setVolume(0);
        } else {
            setVolume((long) this.mAudioManager.getStreamVolume(SamsungAudioManager.stream(1)));
        }
    }

    public void notifyRecFinish() {
        log("notifyRecFinish EVENT_REC_FINISH");
        notifyEvent(17, null);
        this.mAudioManager.setParameters("fmradio_recoding=off");
    }

    private String getPropertyProductName() {
        return SystemProperties.get("ro.product.name");
    }
}
