package android.media;

import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes.Builder;
import android.media.IAudioFocusDispatcher.Stub;
import android.media.SamsungAudioManager.AudioLog;
import android.media.audiopolicy.AudioPolicy;
import android.media.audiopolicy.IAudioPolicyCallback;
import android.media.session.MediaSessionLegacyHelper;
import android.net.ProxyInfo;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Settings.System;
import android.util.ArrayMap;
import android.util.Log;
import android.view.KeyEvent;
import com.sec.android.app.CscFeature;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class AudioManager {
    public static final String A2DPCONNECTED = "audioParam;a2dpconnected";
    public static final String A2DPPLAYING = "audioParam;a2dpplaying";
    public static final String ACTION_AUDIO_BECOMING_NOISY = "android.media.AUDIO_BECOMING_NOISY";
    public static final String ACTION_AUDIO_BECOMING_NOISY_SEC = "android.media.AUDIO_BECOMING_NOISY_SEC";
    public static final String ACTION_HDMI_AUDIO_PLUG = "android.media.action.HDMI_AUDIO_PLUG";
    public static final String ACTION_HEADSET_PLUG = "android.intent.action.HEADSET_PLUG";
    @Deprecated
    public static final String ACTION_SCO_AUDIO_STATE_CHANGED = "android.media.SCO_AUDIO_STATE_CHANGED";
    public static final String ACTION_SCO_AUDIO_STATE_UPDATED = "android.media.ACTION_SCO_AUDIO_STATE_UPDATED";
    public static final int ADJUST_LOWER = -1;
    public static final int ADJUST_MUTE = -100;
    public static final int ADJUST_RAISE = 1;
    public static final int ADJUST_SAME = 0;
    public static final int ADJUST_TOGGLE_MUTE = 101;
    public static final int ADJUST_UNMUTE = 100;
    public static final String AF = "situation=4";
    public static final int AUDIOFOCUS_FLAGS_APPS = 3;
    public static final int AUDIOFOCUS_FLAGS_SYSTEM = 7;
    public static final int AUDIOFOCUS_FLAG_DELAY_OK = 1;
    public static final int AUDIOFOCUS_FLAG_LOCK = 4;
    public static final int AUDIOFOCUS_FLAG_PAUSES_ON_DUCKABLE_LOSS = 2;
    public static final int AUDIOFOCUS_GAIN = 1;
    public static final int AUDIOFOCUS_GAIN_TRANSIENT = 2;
    public static final int AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE = 4;
    public static final int AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK = 3;
    public static final int AUDIOFOCUS_LOSS = -1;
    public static final int AUDIOFOCUS_LOSS_TRANSIENT = -2;
    public static final int AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK = -3;
    public static final int AUDIOFOCUS_NONE = 0;
    public static final int AUDIOFOCUS_REQUEST_DELAYED = 2;
    public static final int AUDIOFOCUS_REQUEST_FAILED = 0;
    public static final int AUDIOFOCUS_REQUEST_GRANTED = 1;
    static final int AUDIOPORT_GENERATION_INIT = 0;
    public static final int AUDIO_SESSION_ID_GENERATE = 0;
    public static final String BOOTSOUND = "situation=8";
    public static final int CALL_ACTIVE = 2;
    public static final String CALL_CONNECTION = "situation=14";
    public static final int CALL_HOLD = 3;
    public static final int CALL_INACTIVE = 1;
    public static final int CALL_LOCAL_HOLD = 4;
    public static final String CALL_STATE_KEY = "call_state";
    public static final String CAMCORDING_START = "situation=5";
    public static final String CAMCORDING_STOP = "situation=5";
    public static final int DEVICE_IN_ANLG_DOCK_HEADSET = -2147483136;
    public static final int DEVICE_IN_BACK_MIC = -2147483520;
    public static final int DEVICE_IN_BLUETOOTH_SCO_HEADSET = -2147483640;
    public static final int DEVICE_IN_BUILTIN_MIC = -2147483644;
    public static final int DEVICE_IN_DGTL_DOCK_HEADSET = -2147482624;
    public static final int DEVICE_IN_EXT_MIC = -2145386496;
    public static final int DEVICE_IN_FM_TUNER = -2147475456;
    public static final int DEVICE_IN_HDMI = -2147483616;
    public static final int DEVICE_IN_LINE = -2147450880;
    public static final int DEVICE_IN_LOOPBACK = -2147221504;
    public static final int DEVICE_IN_SPDIF = -2147418112;
    public static final int DEVICE_IN_TELEPHONY_RX = -2147483584;
    public static final int DEVICE_IN_TV_TUNER = -2147467264;
    public static final int DEVICE_IN_USB_ACCESSORY = -2147481600;
    public static final int DEVICE_IN_USB_DEVICE = -2147479552;
    public static final int DEVICE_IN_WIRED_HEADSET = -2147483632;
    public static final int DEVICE_NONE = 0;
    public static final int DEVICE_OUT_ANLG_DOCK_HEADSET = 2048;
    public static final int DEVICE_OUT_AUX_DIGITAL = 1024;
    public static final int DEVICE_OUT_BLUETOOTH_A2DP = 128;
    public static final int DEVICE_OUT_BLUETOOTH_A2DP_HEADPHONES = 256;
    public static final int DEVICE_OUT_BLUETOOTH_A2DP_SPEAKER = 512;
    public static final int DEVICE_OUT_BLUETOOTH_SCO = 16;
    public static final int DEVICE_OUT_BLUETOOTH_SCO_CARKIT = 64;
    public static final int DEVICE_OUT_BLUETOOTH_SCO_HEADSET = 32;
    public static final int DEVICE_OUT_DEFAULT = 1073741824;
    public static final int DEVICE_OUT_DGTL_DOCK_HEADSET = 4096;
    public static final int DEVICE_OUT_EARPIECE = 1;
    public static final int DEVICE_OUT_FM = 1048576;
    public static final int DEVICE_OUT_HDMI = 1024;
    public static final int DEVICE_OUT_HDMI_ARC = 262144;
    public static final int DEVICE_OUT_LINE = 131072;
    public static final int DEVICE_OUT_REMOTE_SUBMIX = 32768;
    public static final int DEVICE_OUT_SPDIF = 524288;
    public static final int DEVICE_OUT_SPEAKER = 2;
    public static final int DEVICE_OUT_TELEPHONY_TX = 65536;
    public static final int DEVICE_OUT_USB_ACCESSORY = 8192;
    public static final int DEVICE_OUT_USB_DEVICE = 16384;
    public static final int DEVICE_OUT_WIRED_HEADPHONE = 8;
    public static final int DEVICE_OUT_WIRED_HEADSET = 4;
    public static final int EAR_PROTECT_DEFAULT_INDEX = 9;
    public static final int EAR_PROTECT_DEFAULT_INDEX_DOUBLE = 21;
    public static int EAR_PROTECT_LIMIT_INDEX_DISABLE = 16;
    public static int EAR_PROTECT_LIMIT_INDEX_DISABLE_DOUBLE = 31;
    public static int EAR_PROTECT_LIMIT_INDEX_NORMAL = 10;
    public static int EAR_PROTECT_LIMIT_INDEX_NORMAL_DOUBLE = 22;
    public static final int ERROR = -1;
    public static final int ERROR_BAD_VALUE = -2;
    public static final int ERROR_DEAD_OBJECT = -6;
    public static final int ERROR_INVALID_OPERATION = -3;
    public static final int ERROR_NO_INIT = -5;
    public static final int ERROR_PERMISSION_DENIED = -4;
    public static final String EXTRA_AUDIO_PLUG_STATE = "android.media.extra.AUDIO_PLUG_STATE";
    public static final String EXTRA_AVAILABLITY_CHANGED_VALUE = "org.codeaurora.bluetooth.EXTRA_AVAILABLITY_CHANGED_VALUE";
    public static final String EXTRA_CALLING_PACKAGE_NAME = "org.codeaurora.bluetooth.EXTRA_CALLING_PACKAGE_NAME";
    public static final String EXTRA_ENCODINGS = "android.media.extra.ENCODINGS";
    public static final String EXTRA_FOCUS_CHANGED_VALUE = "org.codeaurora.bluetooth.EXTRA_FOCUS_CHANGED_VALUE";
    public static final String EXTRA_MASTER_VOLUME_MUTED = "android.media.EXTRA_MASTER_VOLUME_MUTED";
    public static final String EXTRA_MAX_CHANNEL_COUNT = "android.media.extra.MAX_CHANNEL_COUNT";
    public static final String EXTRA_PREV_VOLUME_STREAM_DEVICES = "android.media.EXTRA_PREV_VOLUME_STREAM_DEVICES";
    public static final String EXTRA_PREV_VOLUME_STREAM_VALUE = "android.media.EXTRA_PREV_VOLUME_STREAM_VALUE";
    public static final String EXTRA_RINGER_MODE = "android.media.EXTRA_RINGER_MODE";
    public static final String EXTRA_SCO_AUDIO_PREVIOUS_STATE = "android.media.extra.SCO_AUDIO_PREVIOUS_STATE";
    public static final String EXTRA_SCO_AUDIO_STATE = "android.media.extra.SCO_AUDIO_STATE";
    public static final String EXTRA_STREAM_VOLUME_MUTED = "android.media.EXTRA_STREAM_VOLUME_MUTED";
    public static final String EXTRA_VIBRATE_SETTING = "android.media.EXTRA_VIBRATE_SETTING";
    public static final String EXTRA_VIBRATE_TYPE = "android.media.EXTRA_VIBRATE_TYPE";
    public static final String EXTRA_VOLUME_STREAM_DEVICES = "android.media.EXTRA_VOLUME_STREAM_DEVICES";
    public static final String EXTRA_VOLUME_STREAM_MUSIC_ADDRESS = "android.media.EXTRA_VOLUME_STREAM_MUSIC_ADDRESS";
    public static final String EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
    public static final String EXTRA_VOLUME_STREAM_TYPE_ALIAS = "android.media.EXTRA_VOLUME_STREAM_TYPE_ALIAS";
    public static final String EXTRA_VOLUME_STREAM_VALUE = "android.media.EXTRA_VOLUME_STREAM_VALUE";
    public static final int FLAG_ACTIVE_MEDIA_ONLY = 512;
    public static final int FLAG_ALLOW_RINGER_MODES = 2;
    public static final int FLAG_BLUETOOTH_ABS_VOLUME = 64;
    public static final int FLAG_DISMISS_UI_WARNINGS = 524288;
    public static final int FLAG_FIXED_VOLUME = 32;
    public static final int FLAG_FORCE_SHOW_UI = 131072;
    public static final int FLAG_FROM_KEY = 4096;
    public static final int FLAG_HDMI_SYSTEM_AUDIO_VOLUME = 256;
    private static final String[] FLAG_NAMES = new String[]{"FLAG_SHOW_UI", "FLAG_ALLOW_RINGER_MODES", "FLAG_PLAY_SOUND", "FLAG_REMOVE_SOUND_AND_VIBRATE", "FLAG_VIBRATE", "FLAG_FIXED_VOLUME", "FLAG_BLUETOOTH_ABS_VOLUME", "FLAG_SHOW_SILENT_HINT", "FLAG_HDMI_SYSTEM_AUDIO_VOLUME", "FLAG_ACTIVE_MEDIA_ONLY", "FLAG_SHOW_UI_WARNINGS", "FLAG_SHOW_VIBRATE_HINT", "FLAG_FROM_KEY"};
    public static final int FLAG_PLAY_SOUND = 4;
    public static final int FLAG_REMOVE_SOUND_AND_VIBRATE = 8;
    public static final int FLAG_SHOW_SILENT_HINT = 128;
    public static final int FLAG_SHOW_UI = 1;
    public static final int FLAG_SHOW_UI_WARNINGS = 1024;
    public static final int FLAG_SHOW_VIBRATE_HINT = 2048;
    public static final int FLAG_SILENT_MODE_STATE = 16384;
    public static final int FLAG_SKIP_PENDING = 262144;
    public static final int FLAG_SKIP_RINGER_MODES = 32768;
    public static final int FLAG_SOUND_EFFECT = 100;
    public static final int FLAG_UDATE_STATE = 8192;
    public static final int FLAG_UI_EXPANDED = 65536;
    public static final int FLAG_VIBRATE = 16;
    public static final int FX_FOCUS_NAVIGATION_DOWN = 2;
    public static final int FX_FOCUS_NAVIGATION_LEFT = 3;
    public static final int FX_FOCUS_NAVIGATION_RIGHT = 4;
    public static final int FX_FOCUS_NAVIGATION_UP = 1;
    public static final int FX_KEYPRESS_DELETE = 7;
    public static final int FX_KEYPRESS_INVALID = 9;
    public static final int FX_KEYPRESS_RETURN = 8;
    public static final int FX_KEYPRESS_SPACEBAR = 6;
    public static final int FX_KEYPRESS_STANDARD = 5;
    public static final int FX_KEY_CLICK = 0;
    public static final int GET_DEVICES_ALL = 3;
    public static final int GET_DEVICES_INPUTS = 1;
    public static final int GET_DEVICES_OUTPUTS = 2;
    public static final String HEADSET_VOLUME = ";device=2";
    public static final String IMPLICIT = ";device=0";
    public static final long IMS_VSID = 281026560;
    public static final String INTERNAL_RINGER_MODE_CHANGED_ACTION = "android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION";
    public static final String KEYBOARD = "situation=2";
    public static final String KEY_TONE = "situation=0";
    public static final String MASTER_MUTE_CHANGED_ACTION = "android.media.MASTER_MUTE_CHANGED_ACTION";
    public static final int MODE_CURRENT = -1;
    public static final int MODE_INVALID = -2;
    public static final int MODE_IN_CALL = 2;
    public static final int MODE_IN_COMMUNICATION = 3;
    public static final int MODE_IN_VIDEOCALL = 4;
    public static final int MODE_NORMAL = 0;
    public static final int MODE_RINGTONE = 1;
    private static final int MSG_DEVICES_CALLBACK_REGISTERED = 0;
    private static final int MSG_DEVICES_DEVICES_ADDED = 1;
    private static final int MSG_DEVICES_DEVICES_REMOVED = 2;
    public static final int NUM_SOUNDALIVE_PRESET_NUSIC = 6;
    public static final int NUM_SOUNDALIVE_PRESET_VIDEO = 5;
    public static final int NUM_SOUND_EFFECTS = 16;
    public static final int NUM_SOUND_EFFECTS_ORIG = 10;
    @Deprecated
    public static final int NUM_STREAMS = 5;
    public static final String OUTDEVICE = "audioParam;outDevice";
    public static final String PROPERTY_OUTPUT_FRAMES_PER_BUFFER = "android.media.property.OUTPUT_FRAMES_PER_BUFFER";
    public static final String PROPERTY_OUTPUT_SAMPLE_RATE = "android.media.property.OUTPUT_SAMPLE_RATE";
    public static final String PROPERTY_SUPPORT_MIC_NEAR_ULTRASOUND = "android.media.property.SUPPORT_MIC_NEAR_ULTRASOUND";
    public static final String PROPERTY_SUPPORT_SPEAKER_NEAR_ULTRASOUND = "android.media.property.SUPPORT_SPEAKER_NEAR_ULTRASOUND";
    public static final String RCC_CHANGED_ACTION = "org.codeaurora.bluetooth.RCC_CHANGED_ACTION";
    public static final String RECORD_ACTIVE = "isRecordActive";
    public static final String RINGER_MODE_CHANGED_ACTION = "android.media.RINGER_MODE_CHANGED";
    public static final int RINGER_MODE_MAX = 2;
    public static final int RINGER_MODE_NORMAL = 2;
    public static final int RINGER_MODE_SILENT = 0;
    public static final int RINGER_MODE_VIBRATE = 1;
    @Deprecated
    public static final int ROUTE_ALL = -1;
    @Deprecated
    public static final int ROUTE_BLUETOOTH = 4;
    @Deprecated
    public static final int ROUTE_BLUETOOTH_A2DP = 16;
    @Deprecated
    public static final int ROUTE_BLUETOOTH_SCO = 4;
    @Deprecated
    public static final int ROUTE_EARPIECE = 1;
    @Deprecated
    public static final int ROUTE_HEADSET = 8;
    @Deprecated
    public static final int ROUTE_SPEAKER = 2;
    public static final int SA_DUMP_OFF = 8192;
    public static final int SA_DUMP_ON = 16384;
    public static final int SA_GET_PUMP_DATA = 512;
    public static final int SA_GET_VISUALIZATION_DATA = 128;
    public static final int SA_INVOKE_ID_TUNNEL_CONTROL = 100000;
    public static final int SA_SET_EXTENDED_PARAM = 2048;
    public static final int SA_SET_PRESET = 16;
    public static final int SA_SET_PUMP = 256;
    public static final int SA_SET_SPEED = 1024;
    public static final int SA_SET_USEREQ_DATA = 32;
    public static final int SA_SET_VISUALIZATION = 64;
    public static final int SA_ULP_DUMP_OFF = 4096;
    public static final int SCO_AUDIO_STATE_CONNECTED = 1;
    public static final int SCO_AUDIO_STATE_CONNECTING = 2;
    public static final int SCO_AUDIO_STATE_DISCONNECTED = 0;
    public static final int SCO_AUDIO_STATE_ERROR = -1;
    public static final String SHUTTER_1 = "situation=3";
    public static final String SHUTTER_2 = "situation=9";
    public static final String SHUTTER_3 = "situation=10";
    public static final String SITUATION_MIDI = "situation=6";
    public static final int SOUNDALIVE_PRESET_BASS_BOOST = 7;
    public static final int SOUNDALIVE_PRESET_CAFE = 11;
    public static final int SOUNDALIVE_PRESET_CLASSIC = 5;
    public static final int SOUNDALIVE_PRESET_CLUB = 4;
    public static final int SOUNDALIVE_PRESET_CONCERTHALL = 5;
    public static final int SOUNDALIVE_PRESET_CONCERT_HALL = 12;
    public static final int SOUNDALIVE_PRESET_DANCE = 3;
    public static final int SOUNDALIVE_PRESET_EXTERNALIZATION = 10;
    public static final int SOUNDALIVE_PRESET_JAZZ = 4;
    public static final int SOUNDALIVE_PRESET_MOVIE = 15;
    public static final int SOUNDALIVE_PRESET_MTHEATER = 9;
    public static final int SOUNDALIVE_PRESET_NORMAL = 0;
    public static final int SOUNDALIVE_PRESET_POP = 1;
    public static final int SOUNDALIVE_PRESET_ROCK = 2;
    public static final int SOUNDALIVE_PRESET_SAMSUNG_TUBE_SOUND = 17;
    public static final int SOUNDALIVE_PRESET_SRS_SURROUND_MUSIC = 18;
    public static final int SOUNDALIVE_PRESET_SRS_SURROUND_VIDEO = 20;
    public static final int SOUNDALIVE_PRESET_SRS_WOWHD = 19;
    public static final int SOUNDALIVE_PRESET_STUDIO = 3;
    public static final int SOUNDALIVE_PRESET_TREBLE_BOOST = 8;
    public static final int SOUNDALIVE_PRESET_TUBE = 1;
    public static final int SOUNDALIVE_PRESET_USER = 13;
    public static final int SOUNDALIVE_PRESET_VIDEO_AUTO = 14;
    public static final int SOUNDALIVE_PRESET_VIDEO_MUSIC = 12;
    public static final int SOUNDALIVE_PRESET_VIDEO_NORMAL = 10;
    public static final int SOUNDALIVE_PRESET_VIDEO_VIRT71 = 13;
    public static final int SOUNDALIVE_PRESET_VIDEO_VOICE = 11;
    public static final int SOUNDALIVE_PRESET_VIRT51 = 16;
    public static final int SOUNDALIVE_PRESET_VIRT71 = 2;
    public static final int SOUNDALIVE_PRESET_VOCAL = 6;
    public static final int SOUNDALIVE_PRESET_VOICE = 14;
    public static final int SOUND_HW_TOUCH = 102;
    public static final int SOUND_SILENT_MODE_OFF = 101;
    public static final int SOUND_TIME_PICKER = 104;
    public static final int SOUND_TIME_PICKER_SCROLL = 105;
    public static final int SOUND_TOUCH = 100;
    public static final int SOUND_TW_HIGHLIGHT = 103;
    public static final String SPEAKER_VOLUME = ";device=1";
    public static final int STREAM_ALARM = 4;
    public static final int STREAM_BLUETOOTH_SCO = 6;
    public static final String STREAM_DEVICES_CHANGED_ACTION = "android.media.STREAM_DEVICES_CHANGED_ACTION";
    public static final int STREAM_DTMF = 8;
    public static final int STREAM_FM_RADIO = 10;
    public static final int STREAM_MUSIC = 3;
    public static final String STREAM_MUTE_CHANGED_ACTION = "android.media.STREAM_MUTE_CHANGED_ACTION";
    public static final int STREAM_NOTIFICATION = 5;
    public static final int STREAM_RING = 2;
    public static final int STREAM_SEC_VOICE_COMMUNICATION = 12;
    public static final int STREAM_SYSTEM = 1;
    public static final int STREAM_SYSTEM_ENFORCED = 7;
    public static final int STREAM_TTS = 9;
    public static final int STREAM_VIDEO_CALL = 11;
    public static final int STREAM_VOICENOTE = 13;
    public static final int STREAM_VOICE_CALL = 0;
    public static final int SUCCESS = 0;
    private static String TAG = "AudioManager";
    public static final String TONE_HIPRI = "situation=16";
    public static final String TONE_LOPRI = "situation=15";
    public static final String TOUCH_TONE = "situation=1";
    public static final int USE_DEFAULT_STREAM_TYPE = Integer.MIN_VALUE;
    public static final String VIBRATE_SETTING_CHANGED_ACTION = "android.media.VIBRATE_SETTING_CHANGED";
    private static final int VIBRATE_SETTING_MAX = 2;
    public static final int VIBRATE_SETTING_OFF = 0;
    public static final int VIBRATE_SETTING_ON = 1;
    public static final int VIBRATE_SETTING_ONLY_SILENT = 2;
    private static final int VIBRATE_TYPE_MAX = 1;
    public static final int VIBRATE_TYPE_NOTIFICATION = 1;
    public static final int VIBRATE_TYPE_RINGER = 0;
    public static final String VIDEO = "situation=7";
    public static final long VOICE2_VSID = 282857472;
    public static final long VOICE_VSID = 281022464;
    public static final String VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    public static final String VSID_KEY = "vsid";
    static ArrayList<AudioPatch> sAudioPatchesCached = new ArrayList();
    private static final AudioPortEventHandler sAudioPortEventHandler = new AudioPortEventHandler();
    static Integer sAudioPortGeneration = new Integer(0);
    static ArrayList<AudioPort> sAudioPortsCached = new ArrayList();
    static ArrayList<AudioPort> sPreviousAudioPortsCached = new ArrayList();
    private static IAudioService sService;
    static Object sSetDeviceForceLock = new Object();
    private Context mApplicationContext;
    private final IAudioFocusDispatcher mAudioFocusDispatcher = new Stub() {
        public void dispatchAudioFocusChange(int focusChange, String id) {
            AudioManager.this.mAudioFocusEventHandlerDelegate.getHandler().sendMessage(AudioManager.this.mAudioFocusEventHandlerDelegate.getHandler().obtainMessage(focusChange, id));
        }
    };
    private final FocusEventHandlerDelegate mAudioFocusEventHandlerDelegate = new FocusEventHandlerDelegate();
    private final HashMap<String, OnAudioFocusChangeListener> mAudioFocusIdListenerMap = new HashMap();
    private ArrayMap<AudioDeviceCallback, NativeEventHandlerDelegate> mDeviceCallbacks = new ArrayMap();
    private final Object mFocusListenerLock = new Object();
    private final IBinder mICallBack = new Binder();
    private KeyguardManager mKeyguardManager;
    private Context mOriginalContext;
    private OnAmPortUpdateListener mPortListener = null;
    private ArrayList<AudioDevicePort> mPreviousPorts = new ArrayList();
    private final boolean mUseFixedVolume;
    private final boolean mUseVolumeKeySounds;
    private long mVolumeKeyUpTime;

    private class FocusEventHandlerDelegate {
        private final Handler mHandler;

        FocusEventHandlerDelegate() {
            Looper looper = Looper.myLooper();
            if (looper == null) {
                looper = Looper.getMainLooper();
            }
            if (looper != null) {
                this.mHandler = new Handler(looper, AudioManager.this) {
                    public void handleMessage(Message msg) {
                        synchronized (AudioManager.this.mFocusListenerLock) {
                            OnAudioFocusChangeListener listener = AudioManager.this.findFocusListener((String) msg.obj);
                        }
                        if (listener != null) {
                            Log.d(AudioManager.TAG, "AudioManager dispatching onAudioFocusChange(" + msg.what + ") for " + msg.obj);
                            listener.onAudioFocusChange(msg.what);
                        }
                    }
                };
            } else {
                this.mHandler = null;
            }
        }

        Handler getHandler() {
            return this.mHandler;
        }
    }

    private class NativeEventHandlerDelegate {
        private final Handler mHandler;

        NativeEventHandlerDelegate(final AudioDeviceCallback callback, Handler handler) {
            Looper looper;
            if (handler != null) {
                looper = handler.getLooper();
            } else {
                looper = Looper.getMainLooper();
            }
            if (looper != null) {
                this.mHandler = new Handler(looper, AudioManager.this) {
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case 0:
                            case 1:
                                if (callback != null) {
                                    callback.onAudioDevicesAdded((AudioDeviceInfo[]) msg.obj);
                                    return;
                                }
                                return;
                            case 2:
                                if (callback != null) {
                                    callback.onAudioDevicesRemoved((AudioDeviceInfo[]) msg.obj);
                                    return;
                                }
                                return;
                            default:
                                Log.e(AudioManager.TAG, "Unknown native event type: " + msg.what);
                                return;
                        }
                    }
                };
            } else {
                this.mHandler = null;
            }
        }

        Handler getHandler() {
            return this.mHandler;
        }
    }

    public interface OnAudioPortUpdateListener {
        void onAudioPatchListUpdate(AudioPatch[] audioPatchArr);

        void onAudioPortListUpdate(AudioPort[] audioPortArr);

        void onServiceDied();
    }

    private class OnAmPortUpdateListener implements OnAudioPortUpdateListener {
        static final String TAG = "OnAmPortUpdateListener";

        private OnAmPortUpdateListener() {
        }

        public void onAudioPortListUpdate(AudioPort[] portList) {
            AudioManager.this.broadcastDeviceListChange(null);
        }

        public void onAudioPatchListUpdate(AudioPatch[] patchList) {
        }

        public void onServiceDied() {
            AudioManager.this.broadcastDeviceListChange(null);
        }
    }

    public interface OnAudioFocusChangeListener {
        void onAudioFocusChange(int i);
    }

    public static String flagsToString(int flags) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < FLAG_NAMES.length; i++) {
            int flag = 1 << i;
            if ((flags & flag) != 0) {
                if (sb.length() > 0) {
                    sb.append(',');
                }
                sb.append(FLAG_NAMES[i]);
                flags &= flag ^ -1;
            }
        }
        if (flags != 0) {
            if (sb.length() > 0) {
                sb.append(',');
            }
            sb.append(flags);
        }
        return sb.toString();
    }

    public AudioManager(Context context) {
        setContext(context);
        this.mUseVolumeKeySounds = getContext().getResources().getBoolean(17956885);
        this.mUseFixedVolume = getContext().getResources().getBoolean(17956995);
        this.mKeyguardManager = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
    }

    private Context getContext() {
        if (this.mApplicationContext == null) {
            setContext(this.mOriginalContext);
        }
        if (this.mApplicationContext != null) {
            return this.mApplicationContext;
        }
        return this.mOriginalContext;
    }

    private void setContext(Context context) {
        this.mApplicationContext = context.getApplicationContext();
        if (this.mApplicationContext != null) {
            this.mOriginalContext = null;
        } else {
            this.mOriginalContext = context;
        }
    }

    private static IAudioService getService() {
        if (sService != null) {
            return sService;
        }
        sService = IAudioService.Stub.asInterface(ServiceManager.getService(Context.AUDIO_SERVICE));
        return sService;
    }

    public void dispatchMediaKeyEvent(KeyEvent keyEvent) {
        MediaSessionLegacyHelper.getHelper(getContext()).sendMediaButtonEvent(keyEvent, false);
    }

    public void preDispatchKeyEvent(KeyEvent event, int stream) {
        int keyCode = event.getKeyCode();
        if (keyCode != 25 && keyCode != 24 && keyCode != 164 && this.mVolumeKeyUpTime + 0 > SystemClock.uptimeMillis()) {
            adjustSuggestedStreamVolume(0, stream, 8);
        }
    }

    public void handleKeyDown(KeyEvent event, int stream) {
        int i;
        boolean z = true;
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case 24:
            case 25:
                break;
            case 91:
            case 164:
            case 1021:
                if (event.getRepeatCount() <= 0) {
                    boolean isLocked;
                    int flags2 = 1;
                    int streamType = getActiveStreamType();
                    if (this.mKeyguardManager == null || !this.mKeyguardManager.isKeyguardLocked()) {
                        isLocked = false;
                    } else {
                        isLocked = true;
                    }
                    if (streamType == 3) {
                        if (isMediaSilentMode()) {
                            z = false;
                        }
                        setMediaSilentMode(z);
                    } else if (!isLocked) {
                        if (getRingerMode() != 2) {
                            setRingerMode(2);
                        } else {
                            setRingerMode(1);
                        }
                    }
                    if (isLocked) {
                        flags2 = 1 & -2;
                    }
                    adjustSuggestedStreamVolume(0, stream, flags2);
                    return;
                }
                return;
            case 168:
            case 169:
            case 1019:
            case 1020:
                int scanCode = event.getScanCode();
                if (scanCode == 533 || scanCode == 534) {
                    return;
                }
            default:
                return;
        }
        if (!(keyCode == 24 || keyCode == 168 || keyCode == 1020)) {
            i = -1;
        }
        adjustSuggestedStreamVolume(i, stream, 17);
    }

    public void handleKeyUp(KeyEvent event, int stream) {
        switch (event.getKeyCode()) {
            case 24:
            case 25:
                break;
            case 164:
                MediaSessionLegacyHelper.getHelper(getContext()).sendVolumeKeyEvent(event, false);
                return;
            case 168:
            case 169:
            case 1019:
            case 1020:
                int scanCode = event.getScanCode();
                if (scanCode == 533 || scanCode == 534) {
                    return;
                }
            default:
                return;
        }
        if (this.mUseVolumeKeySounds) {
            adjustSuggestedStreamVolume(0, stream, 4);
        }
        this.mVolumeKeyUpTime = SystemClock.uptimeMillis();
    }

    public boolean isVolumeFixed() {
        return this.mUseFixedVolume;
    }

    public void adjustStreamVolume(int streamType, int direction, int flags) {
        try {
            getService().adjustStreamVolume(streamType, direction, flags, getContext().getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in adjustStreamVolume", e);
        }
    }

    public void adjustVolume(int direction, int flags) {
        MediaSessionLegacyHelper.getHelper(getContext()).sendAdjustVolumeBy(Integer.MIN_VALUE, direction, flags);
    }

    public void adjustSuggestedStreamVolume(int direction, int suggestedStreamType, int flags) {
        MediaSessionLegacyHelper.getHelper(getContext()).sendAdjustVolumeBy(suggestedStreamType, direction, flags);
    }

    public void setMasterMute(boolean mute, int flags) {
        try {
            getService().setMasterMute(mute, flags, getContext().getOpPackageName(), UserHandle.getCallingUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setMasterMute", e);
        }
    }

    public int getRingerMode() {
        try {
            return getService().getRingerModeExternal();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in getRingerMode", e);
            return 2;
        }
    }

    public static boolean isValidRingerMode(int ringerMode) {
        boolean z = false;
        if (ringerMode >= 0 && ringerMode <= 2) {
            try {
                z = getService().isValidRingerMode(ringerMode);
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in isValidRingerMode", e);
            }
        }
        return z;
    }

    public int getStreamMaxVolume(int streamType) {
        try {
            return getService().getStreamMaxVolume(streamType);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in getStreamMaxVolume", e);
            return 0;
        }
    }

    public int getStreamMinVolume(int streamType) {
        try {
            return getService().getStreamMinVolume(streamType);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in getStreamMinVolume", e);
            return 0;
        }
    }

    public int getStreamVolume(int streamType) {
        try {
            return getService().getStreamVolume(streamType);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in getStreamVolume", e);
            return 0;
        }
    }

    public int getLastAudibleStreamVolume(int streamType) {
        try {
            return getService().getLastAudibleStreamVolume(streamType);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in getLastAudibleStreamVolume", e);
            return 0;
        }
    }

    public int getUiSoundsStreamType() {
        try {
            return getService().getUiSoundsStreamType();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in getUiSoundsStreamType", e);
            return 2;
        }
    }

    public void setRingerMode(int ringerMode) {
        if (isValidRingerMode(ringerMode)) {
            try {
                getService().setRingerModeExternal(ringerMode, getContext().getOpPackageName());
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in setRingerMode", e);
            }
        }
    }

    public void setStreamVolume(int streamType, int index, int flags) {
        try {
            getService().setStreamVolume(streamType, index, flags, getContext().getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setStreamVolume", e);
        }
    }

    @Deprecated
    public void setStreamSolo(int streamType, boolean state) {
        Log.w(TAG, "setStreamSolo has been deprecated. Do not use.");
    }

    @Deprecated
    public void setStreamMute(int streamType, boolean state) {
        Log.w(TAG, "setStreamMute is deprecated. adjustStreamVolume should be used instead.");
        int direction = state ? -100 : 100;
        if (streamType == Integer.MIN_VALUE) {
            adjustSuggestedStreamVolume(direction, streamType, 0);
        } else {
            adjustStreamVolume(streamType, direction, 0);
        }
    }

    public boolean isStreamMute(int streamType) {
        try {
            return getService().isStreamMute(streamType);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in isStreamMute", e);
            return false;
        }
    }

    public boolean isMasterMute() {
        try {
            return getService().isMasterMute();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in isMasterMute", e);
            return false;
        }
    }

    public void forceVolumeControlStream(int streamType) {
        try {
            getService().forceVolumeControlStream(streamType, this.mICallBack);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in forceVolumeControlStream", e);
        }
    }

    public boolean shouldVibrate(int vibrateType) {
        try {
            return getService().shouldVibrate(vibrateType);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in shouldVibrate", e);
            return false;
        }
    }

    public int getVibrateSetting(int vibrateType) {
        try {
            return getService().getVibrateSetting(vibrateType);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in getVibrateSetting", e);
            return 0;
        }
    }

    public void setVibrateSetting(int vibrateType, int vibrateSetting) {
        try {
            getService().setVibrateSetting(vibrateType, vibrateSetting);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setVibrateSetting", e);
        }
    }

    public void setSpeakerphoneOn(boolean on) {
        IAudioService service = getService();
        try {
            if (AudioLog.isLoggable(TAG, AudioLog.HIGH)) {
                RuntimeException here = new RuntimeException("here");
                here.fillInStackTrace();
                Log.d(TAG, "setSpeakerphoneOn call stack", here);
            }
            setParameters("audioParam;FORCE_SPEAKER=" + on);
            service.setSpeakerphoneOn(on);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setSpeakerphoneOn", e);
        }
    }

    public boolean isSpeakerphoneOn() {
        try {
            return getService().isSpeakerphoneOn();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in isSpeakerphoneOn", e);
            return false;
        }
    }

    public void setForceSpeakerOn(boolean on) {
        try {
            getService().setForceSpeakerOn(on);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setForceSpeakerOn", e);
        }
    }

    public boolean isForceSpeakerOn() {
        try {
            return getService().isForceSpeakerOn();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in isForceSpeakerOn", e);
            return false;
        }
    }

    public void setRadioSpeakerOn(boolean on) {
        try {
            getService().setRadioSpeakerOn(on);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setRadioSpeakerOn", e);
        }
    }

    public boolean isRadioSpeakerOn() {
        try {
            return getService().isRadioSpeakerOn();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in isRadioSpeakerOn", e);
            return false;
        }
    }

    public void setMediaSpeakerOn(boolean on) {
    }

    public boolean isMediaSpeakerOn() {
        return false;
    }

    public static int getActiveStreamType() {
        int nReturn = Integer.MIN_VALUE;
        try {
            nReturn = getService().getActiveStreamType(Integer.MIN_VALUE);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in getActiveStreamType", e);
        }
        return nReturn;
    }

    public static void setMediaSilentMode(boolean on) {
        try {
            getService().setMediaSilentMode(on);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setMediaSilentMode", e);
        }
    }

    public static boolean isMediaSilentMode() {
        boolean nReturn = false;
        try {
            nReturn = getService().isMediaSilentMode();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in isMediaSilentMode", e);
        }
        return nReturn;
    }

    public static void setSmartVoumeEnable(boolean on) {
    }

    public boolean isBluetoothScoAvailableOffCall() {
        return getContext().getResources().getBoolean(17956951);
    }

    public void startBluetoothSco() {
        try {
            getService().startBluetoothSco(this.mICallBack, getContext().getApplicationInfo().targetSdkVersion);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in startBluetoothSco", e);
        }
    }

    public void startBluetoothScoVirtualCall() {
        try {
            getService().startBluetoothScoVirtualCall(this.mICallBack);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in startBluetoothScoVirtualCall", e);
        }
    }

    public void stopBluetoothSco() {
        try {
            getService().stopBluetoothSco(this.mICallBack);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in stopBluetoothSco", e);
        }
    }

    public void setBluetoothScoOn(boolean on) {
        IAudioService service = getService();
        try {
            if (AudioLog.isLoggable(TAG, AudioLog.HIGH)) {
                RuntimeException here = new RuntimeException("here");
                here.fillInStackTrace();
                Log.d(TAG, "setBluetoothScoOn call stack", here);
            }
            setParameters("audioParam;FORCE_BT_SCO=" + on);
            service.setBluetoothScoOn(on);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setBluetoothScoOn", e);
        }
    }

    public boolean isBluetoothScoOn() {
        try {
            return getService().isBluetoothScoOn();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in isBluetoothScoOn", e);
            return false;
        }
    }

    @Deprecated
    public void setBluetoothA2dpOn(boolean on) {
    }

    public boolean isBluetoothA2dpOn() {
        if (AudioSystem.getDeviceConnectionState(128, ProxyInfo.LOCAL_EXCL_LIST) == 0) {
            return false;
        }
        return true;
    }

    @Deprecated
    public void setWiredHeadsetOn(boolean on) {
    }

    public boolean isWiredHeadsetOn() {
        if (AudioSystem.getDeviceConnectionState(4, ProxyInfo.LOCAL_EXCL_LIST) == 0 && AudioSystem.getDeviceConnectionState(8, ProxyInfo.LOCAL_EXCL_LIST) == 0) {
            return false;
        }
        return true;
    }

    public boolean isExtraSpeakerDockOn() {
        if (AudioSystem.getDeviceConnectionState(2048, ProxyInfo.LOCAL_EXCL_LIST) == 0 && AudioSystem.getDeviceConnectionState(4096, ProxyInfo.LOCAL_EXCL_LIST) == 0) {
            return false;
        }
        return true;
    }

    public boolean isHdmiConnected() {
        if (AudioSystem.getDeviceConnectionState(1024, ProxyInfo.LOCAL_EXCL_LIST) == 1) {
            return true;
        }
        return false;
    }

    public void setMicrophoneMute(boolean on) {
        try {
            getService().setMicrophoneMute(on, getContext().getOpPackageName(), UserHandle.getCallingUserId());
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setMicrophoneMute", e);
        }
    }

    public boolean isMicrophoneMute() {
        return AudioSystem.isMicrophoneMuted();
    }

    public void setMode(int mode) {
        try {
            getService().setMode(mode, this.mICallBack, this.mApplicationContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setMode", e);
        }
    }

    public int getMode() {
        try {
            return getService().getMode();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in getMode", e);
            return -2;
        }
    }

    @Deprecated
    public void setRouting(int mode, int routes, int mask) {
    }

    @Deprecated
    public int getRouting(int mode) {
        return -1;
    }

    public boolean isMusicActive() {
        return AudioSystem.isStreamActive(3, 0);
    }

    public boolean isMusicActiveRemotely() {
        return AudioSystem.isStreamActiveRemotely(3, 0);
    }

    public boolean isAudioFocusExclusive() {
        try {
            if (getService().getCurrentAudioFocus() == 4) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in isAudioFocusExclusive()", e);
            return false;
        }
    }

    public int generateAudioSessionId() {
        int session = AudioSystem.newAudioSessionId();
        if (session > 0) {
            return session;
        }
        Log.e(TAG, "Failure to generate a new audio session ID");
        return -1;
    }

    public boolean isFMActive() {
        return AudioSystem.isStreamActive(10, 0);
    }

    public boolean isRecordActive() {
        if (getParameters(RECORD_ACTIVE).equals("TRUE")) {
            return true;
        }
        return false;
    }

    public boolean isVoiceCallActive() {
        return AudioSystem.isStreamActive(0, 0) || AudioSystem.isStreamActive(12, 0);
    }

    @Deprecated
    public void setParameter(String key, String value) {
        setParameters(key + "=" + value);
    }

    public void setParameters(String keyValuePairs) {
        AudioSystem.setParameters(keyValuePairs);
        if (keyValuePairs.startsWith(AudioParameter.AUDIO_PARAMETER_KEY_BT_AOBLE_MODE)) {
            setAudioServiceConfig(keyValuePairs);
        }
    }

    public String getParameters(String keys) {
        return AudioSystem.getParameters(keys);
    }

    public void playSoundEffect(int effectType) {
        if (effectType >= 100) {
            effectType = (effectType - 100) + 10;
        }
        if (effectType >= 0 && effectType < 16 && querySoundEffectsEnabled(Process.myUserHandle().getIdentifier()) && !isSilentMode()) {
            try {
                getService().playSoundEffect(effectType);
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in playSoundEffect" + e);
            }
        }
    }

    public void playSoundEffect(int effectType, int userId) {
        if (effectType >= 100) {
            effectType = (effectType - 100) + 10;
        }
        if (effectType >= 0 && effectType < 16 && querySoundEffectsEnabled(userId) && !isSilentMode()) {
            try {
                getService().playSoundEffect(effectType);
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in playSoundEffect" + e);
            }
        }
    }

    public void playSoundEffect(int effectType, float volume) {
        if (effectType >= 100) {
            effectType = (effectType - 100) + 10;
        }
        if (effectType >= 0 && effectType < 16) {
            try {
                getService().playSoundEffectVolume(effectType, volume);
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in playSoundEffect" + e);
            }
        }
    }

    private boolean querySoundEffectsEnabled(int user) {
        return System.getIntForUser(getContext().getContentResolver(), "sound_effects_enabled", 0, user) != 0;
    }

    public void loadSoundEffects() {
        try {
            getService().loadSoundEffects();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in loadSoundEffects" + e);
        }
    }

    public void unloadSoundEffects() {
        try {
            getService().unloadSoundEffects();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in unloadSoundEffects" + e);
        }
    }

    private OnAudioFocusChangeListener findFocusListener(String id) {
        return (OnAudioFocusChangeListener) this.mAudioFocusIdListenerMap.get(id);
    }

    private String getIdForAudioFocusListener(OnAudioFocusChangeListener l) {
        if (l == null) {
            return new String(toString()).replace('@', '$');
        }
        return new String(toString() + l.toString()).replace('@', '$');
    }

    public void registerAudioFocusListener(OnAudioFocusChangeListener l) {
        synchronized (this.mFocusListenerLock) {
            if (this.mAudioFocusIdListenerMap.containsKey(getIdForAudioFocusListener(l))) {
                return;
            }
            this.mAudioFocusIdListenerMap.put(getIdForAudioFocusListener(l), l);
        }
    }

    public void unregisterAudioFocusListener(OnAudioFocusChangeListener l) {
        synchronized (this.mFocusListenerLock) {
            this.mAudioFocusIdListenerMap.remove(getIdForAudioFocusListener(l));
        }
    }

    public int requestAudioFocus(OnAudioFocusChangeListener l, int streamType, int durationHint) {
        AudioAttributes build;
        if (streamType == 10) {
            try {
                build = new Builder().setInternalLegacyStreamType(streamType).addTag(SamsungAudioManager.FM_RADIO).build();
            } catch (IllegalArgumentException e) {
                Log.e(TAG, "Audio focus request denied due to ", e);
                return 0;
            }
        }
        build = new Builder().setInternalLegacyStreamType(streamType).build();
        return requestAudioFocus(l, build, durationHint, 0);
    }

    public int requestAudioFocus(OnAudioFocusChangeListener l, AudioAttributes requestAttributes, int durationHint, int flags) throws IllegalArgumentException {
        if (flags != (flags & 3)) {
            throw new IllegalArgumentException("Invalid flags 0x" + Integer.toHexString(flags).toUpperCase());
        }
        return requestAudioFocus(l, requestAttributes, durationHint, flags & 3, null);
    }

    public int requestAudioFocus(OnAudioFocusChangeListener l, AudioAttributes requestAttributes, int durationHint, int flags, AudioPolicy ap) throws IllegalArgumentException {
        if (requestAttributes == null) {
            throw new IllegalArgumentException("Illegal null AudioAttributes argument");
        } else if (durationHint < 1 || durationHint > 4) {
            throw new IllegalArgumentException("Invalid duration hint");
        } else if (flags != (flags & 7)) {
            throw new IllegalArgumentException("Illegal flags 0x" + Integer.toHexString(flags).toUpperCase());
        } else if ((flags & 1) == 1 && l == null) {
            throw new IllegalArgumentException("Illegal null focus listener when flagged as accepting delayed focus grant");
        } else if ((flags & 4) == 4 && ap == null) {
            throw new IllegalArgumentException("Illegal null audio policy when locking audio focus");
        } else {
            registerAudioFocusListener(l);
            IAudioService service = getService();
            try {
                if (requestAttributes.getTags().contains(SamsungAudioManager.FM_RADIO)) {
                    IAudioPolicyCallback cb;
                    String packageName = "com.sec.android.app.fm";
                    if (CscFeature.getInstance().getEnableStatus("CscFeature_Common_SupportNextRadio")) {
                        packageName = "com.nextradioapp.nextradio";
                    }
                    IBinder iBinder = this.mICallBack;
                    IAudioFocusDispatcher iAudioFocusDispatcher = this.mAudioFocusDispatcher;
                    String idForAudioFocusListener = getIdForAudioFocusListener(l);
                    if (ap != null) {
                        cb = ap.cb();
                    } else {
                        cb = null;
                    }
                    return service.requestAudioFocus(requestAttributes, durationHint, iBinder, iAudioFocusDispatcher, idForAudioFocusListener, packageName, flags, cb);
                }
                return service.requestAudioFocus(requestAttributes, durationHint, this.mICallBack, this.mAudioFocusDispatcher, getIdForAudioFocusListener(l), getContext().getOpPackageName(), flags, ap != null ? ap.cb() : null);
            } catch (Throwable e) {
                Log.e(TAG, "Can't call requestAudioFocus() on AudioService:", e);
                return 0;
            }
        }
    }

    public void requestAudioFocusForCall(int streamType, int durationHint) {
        IAudioService service = getService();
        try {
            service.requestAudioFocus(new Builder().setInternalLegacyStreamType(streamType).build(), durationHint, this.mICallBack, null, AudioSystem.IN_VOICE_COMM_FOCUS_ID, getContext().getOpPackageName(), 4, null);
        } catch (RemoteException e) {
            Log.e(TAG, "Can't call requestAudioFocusForCall() on AudioService:", e);
        }
    }

    public void abandonAudioFocusForCall() {
        try {
            getService().abandonAudioFocus(null, AudioSystem.IN_VOICE_COMM_FOCUS_ID, null);
        } catch (RemoteException e) {
            Log.e(TAG, "Can't call abandonAudioFocusForCall() on AudioService:", e);
        }
    }

    public int abandonAudioFocus(OnAudioFocusChangeListener l) {
        return abandonAudioFocus(l, null);
    }

    public int abandonAudioFocus(OnAudioFocusChangeListener l, AudioAttributes aa) {
        int status = 0;
        unregisterAudioFocusListener(l);
        try {
            status = getService().abandonAudioFocus(this.mAudioFocusDispatcher, getIdForAudioFocusListener(l), aa);
        } catch (RemoteException e) {
            Log.e(TAG, "Can't call abandonAudioFocus() on AudioService:", e);
        }
        return status;
    }

    @Deprecated
    public void registerMediaButtonEventReceiver(ComponentName eventReceiver) {
        if (eventReceiver != null) {
            if (eventReceiver.getPackageName().equals(getContext().getPackageName())) {
                IAudioService service = getService();
                try {
                    if (getContext().getOpPackageName().equals("com.sec.android.app.together")) {
                        service.setTogether(true);
                    }
                } catch (RemoteException e) {
                    Log.e(TAG, "Dead object in unregisterMediaButtonEventReceiver", e);
                }
                Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
                mediaButtonIntent.addFlags(268435456);
                mediaButtonIntent.setComponent(eventReceiver);
                registerMediaButtonIntent(PendingIntent.getBroadcast(getContext(), 0, mediaButtonIntent, 0), eventReceiver);
                return;
            }
            Log.e(TAG, "registerMediaButtonEventReceiver() error: receiver and context package names don't match");
        }
    }

    @Deprecated
    public void registerMediaButtonEventReceiver(PendingIntent eventReceiver) {
        if (eventReceiver != null) {
            registerMediaButtonIntent(eventReceiver, null);
        }
    }

    public void registerMediaButtonIntent(PendingIntent pi, ComponentName eventReceiver) {
        if (pi == null) {
            Log.e(TAG, "Cannot call registerMediaButtonIntent() with a null parameter");
        } else {
            MediaSessionLegacyHelper.getHelper(getContext()).addMediaButtonListener(pi, eventReceiver, getContext());
        }
    }

    @Deprecated
    public void unregisterMediaButtonEventReceiver(ComponentName eventReceiver) {
        if (eventReceiver != null) {
            IAudioService service = getService();
            try {
                if (getContext().getOpPackageName().equals("com.sec.android.app.together")) {
                    service.setTogether(false);
                }
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in unregisterMediaButtonEventReceiver", e);
            }
            Intent mediaButtonIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
            mediaButtonIntent.setComponent(eventReceiver);
            unregisterMediaButtonIntent(PendingIntent.getBroadcast(getContext(), 0, mediaButtonIntent, 0));
        }
    }

    @Deprecated
    public void unregisterMediaButtonEventReceiver(PendingIntent eventReceiver) {
        if (eventReceiver != null) {
            unregisterMediaButtonIntent(eventReceiver);
        }
    }

    public void unregisterMediaButtonIntent(PendingIntent pi) {
        MediaSessionLegacyHelper.getHelper(getContext()).removeMediaButtonListener(pi);
    }

    @Deprecated
    public void registerRemoteControlClient(RemoteControlClient rcClient) {
        if (rcClient != null && rcClient.getRcMediaIntent() != null) {
            rcClient.registerWithSession(MediaSessionLegacyHelper.getHelper(getContext()));
        }
    }

    @Deprecated
    public void unregisterRemoteControlClient(RemoteControlClient rcClient) {
        if (rcClient != null && rcClient.getRcMediaIntent() != null) {
            rcClient.unregisterWithSession(MediaSessionLegacyHelper.getHelper(getContext()));
        }
    }

    @Deprecated
    public boolean registerRemoteController(RemoteController rctlr) {
        if (rctlr == null) {
            return false;
        }
        rctlr.startListeningToSessions();
        return true;
    }

    @Deprecated
    public void unregisterRemoteController(RemoteController rctlr) {
        if (rctlr != null) {
            rctlr.stopListeningToSessions();
        }
    }

    public void registerRemoteControlDisplay(IRemoteControlDisplay rcd) {
        registerRemoteControlDisplay(rcd, -1, -1);
    }

    public void registerRemoteControlDisplay(IRemoteControlDisplay rcd, int w, int h) {
        if (rcd != null) {
            try {
                getService().registerRemoteControlDisplay(rcd, w, h);
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in registerRemoteControlDisplay " + e);
            }
        }
    }

    public void unregisterRemoteControlDisplay(IRemoteControlDisplay rcd) {
        if (rcd != null) {
            try {
                getService().unregisterRemoteControlDisplay(rcd);
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in unregisterRemoteControlDisplay " + e);
            }
        }
    }

    public void remoteControlDisplayUsesBitmapSize(IRemoteControlDisplay rcd, int w, int h) {
        if (rcd != null) {
            try {
                getService().remoteControlDisplayUsesBitmapSize(rcd, w, h);
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in remoteControlDisplayUsesBitmapSize " + e);
            }
        }
    }

    public void remoteControlDisplayWantsPlaybackPositionSync(IRemoteControlDisplay rcd, boolean wantsSync) {
        if (rcd != null) {
            try {
                getService().remoteControlDisplayWantsPlaybackPositionSync(rcd, wantsSync);
            } catch (RemoteException e) {
                Log.e(TAG, "Dead object in remoteControlDisplayWantsPlaybackPositionSync " + e);
            }
        }
    }

    public int registerAudioPolicy(AudioPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Illegal null AudioPolicy argument");
        }
        try {
            String regId = getService().registerAudioPolicy(policy.getConfig(), policy.cb(), policy.hasFocusListener());
            if (regId == null) {
                return -1;
            }
            policy.setRegistration(regId);
            return 0;
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in registerAudioPolicyAsync()", e);
            return -1;
        }
    }

    public void unregisterAudioPolicyAsync(AudioPolicy policy) {
        if (policy == null) {
            throw new IllegalArgumentException("Illegal null AudioPolicy argument");
        }
        try {
            getService().unregisterAudioPolicyAsync(policy.cb());
            policy.setRegistration(null);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in unregisterAudioPolicyAsync()", e);
        }
    }

    public void reloadAudioSettings() {
        try {
            getService().reloadAudioSettings();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in reloadAudioSettings" + e);
        }
    }

    public void avrcpSupportsAbsoluteVolume(String address, boolean support) {
        try {
            getService().avrcpSupportsAbsoluteVolume(address, support);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in avrcpSupportsAbsoluteVolume", e);
        }
    }

    public boolean isSilentMode() {
        int ringerMode = getRingerMode();
        if (ringerMode == 0 || ringerMode == 1) {
            return true;
        }
        return false;
    }

    public static boolean isOutputDevice(int device) {
        return (Integer.MIN_VALUE & device) == 0;
    }

    public static boolean isInputDevice(int device) {
        return (device & Integer.MIN_VALUE) == Integer.MIN_VALUE;
    }

    public int getDevicesForStream(int streamType) {
        switch (streamType) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 8:
                return AudioSystem.getDevicesForStream(streamType);
            default:
                return 0;
        }
    }

    public void setWiredDeviceConnectionState(int type, int state, String address, String name) {
        IAudioService service = getService();
        try {
            service.setWiredDeviceConnectionState(type, state, address, name, this.mApplicationContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setWiredDeviceConnectionState " + e);
        }
    }

    public void setHdmiDeviceConnectionState(int state, String name) {
        IAudioService service = getService();
        try {
            service.setWiredDeviceConnectionState(1024, state, ProxyInfo.LOCAL_EXCL_LIST, name, this.mApplicationContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setHdmiDeviceConnectionState " + e);
        }
    }

    public int setBluetoothA2dpDeviceConnectionState(BluetoothDevice device, int state, int profile) {
        int delay = 0;
        try {
            delay = getService().setBluetoothA2dpDeviceConnectionState(device, state, profile);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setBluetoothA2dpDeviceConnectionState " + e);
        }
        return delay;
    }

    public IRingtonePlayer getRingtonePlayer() {
        try {
            return getService().getRingtonePlayer();
        } catch (RemoteException e) {
            return null;
        }
    }

    public String getProperty(String key) {
        if (PROPERTY_OUTPUT_SAMPLE_RATE.equals(key)) {
            int outputSampleRate = AudioSystem.getPrimaryOutputSamplingRate();
            if (outputSampleRate > 0) {
                return Integer.toString(outputSampleRate);
            }
            return null;
        } else if (PROPERTY_OUTPUT_FRAMES_PER_BUFFER.equals(key)) {
            int outputFramesPerBuffer = AudioSystem.getPrimaryOutputFrameCount();
            if (outputFramesPerBuffer == 1440) {
                outputFramesPerBuffer = 960;
            }
            if (outputFramesPerBuffer > 0) {
                return Integer.toString(outputFramesPerBuffer);
            }
            return null;
        } else if (PROPERTY_SUPPORT_MIC_NEAR_ULTRASOUND.equals(key)) {
            return String.valueOf(getContext().getResources().getBoolean(17957052));
        } else {
            if (PROPERTY_SUPPORT_SPEAKER_NEAR_ULTRASOUND.equals(key)) {
                return String.valueOf(getContext().getResources().getBoolean(17957053));
            }
            return null;
        }
    }

    public int getOutputLatency(int streamType) {
        return AudioSystem.getOutputLatency(streamType);
    }

    public static int getEarProtectLimitIndex() {
        return EAR_PROTECT_LIMIT_INDEX_NORMAL;
    }

    public void dismissVolumePanel() {
        try {
            getService().dismissVolumePanel();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in dismissVolumePanel", e);
        }
    }

    public void setStatusbarHasVolumeSlider(boolean hasVolumeSlider) {
    }

    public String getCurrentAudioFocusPackageName() {
        try {
            return getService().getCurrentAudioFocusPackageName();
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in getCurrentAudioFocusPackageName", e);
            return null;
        }
    }

    public void setAudioServiceConfig(String keyValuePairs) {
        try {
            getService().setAudioServiceConfig(keyValuePairs);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in setAudioServiceConfig", e);
        }
    }

    public String getAudioServiceConfig(String keys) {
        try {
            return getService().getAudioServiceConfig(keys);
        } catch (RemoteException e) {
            Log.e(TAG, "Dead object in getAudioServiceConfig", e);
            return null;
        }
    }

    public void setVolumeController(IVolumeController controller) {
        try {
            getService().setVolumeController(controller);
        } catch (RemoteException e) {
            Log.w(TAG, "Error setting volume controller", e);
        }
    }

    public void notifyVolumeControllerVisible(IVolumeController controller, boolean visible) {
        try {
            getService().notifyVolumeControllerVisible(controller, visible);
        } catch (RemoteException e) {
            Log.w(TAG, "Error notifying about volume controller visibility", e);
        }
    }

    public boolean isStreamAffectedByRingerMode(int streamType) {
        try {
            return getService().isStreamAffectedByRingerMode(streamType);
        } catch (RemoteException e) {
            Log.w(TAG, "Error calling isStreamAffectedByRingerMode", e);
            return false;
        }
    }

    public boolean isStreamAffectedByMute(int streamType) {
        try {
            return getService().isStreamAffectedByMute(streamType);
        } catch (RemoteException e) {
            Log.w(TAG, "Error calling isStreamAffectedByMute", e);
            return false;
        }
    }

    public void disableSafeMediaVolume() {
        try {
            getService().disableSafeMediaVolume(this.mApplicationContext.getOpPackageName());
        } catch (RemoteException e) {
            Log.w(TAG, "Error disabling safe media volume", e);
        }
    }

    public void setRingerModeInternal(int ringerMode) {
        try {
            getService().setRingerModeInternal(ringerMode, getContext().getOpPackageName());
        } catch (RemoteException e) {
            Log.w(TAG, "Error calling setRingerModeInternal", e);
        }
    }

    public int getRingerModeInternal() {
        try {
            return getService().getRingerModeInternal();
        } catch (RemoteException e) {
            Log.w(TAG, "Error calling getRingerModeInternal", e);
            return 2;
        }
    }

    public void setVolumePolicy(VolumePolicy policy) {
        try {
            getService().setVolumePolicy(policy);
        } catch (RemoteException e) {
            Log.w(TAG, "Error calling setVolumePolicy", e);
        }
    }

    public int setHdmiSystemAudioSupported(boolean on) {
        try {
            return getService().setHdmiSystemAudioSupported(on);
        } catch (RemoteException e) {
            Log.w(TAG, "Error setting system audio mode", e);
            return 0;
        }
    }

    public boolean isHdmiSystemAudioSupported() {
        try {
            return getService().isHdmiSystemAudioSupported();
        } catch (RemoteException e) {
            Log.w(TAG, "Error querying system audio mode", e);
            return false;
        }
    }

    public static int listAudioPorts(ArrayList<AudioPort> ports) {
        return updateAudioPortCache(ports, null, null);
    }

    public static int listPreviousAudioPorts(ArrayList<AudioPort> ports) {
        return updateAudioPortCache(null, null, ports);
    }

    public static int listAudioDevicePorts(ArrayList<AudioDevicePort> devices) {
        if (devices == null) {
            return -2;
        }
        ArrayList<AudioPort> ports = new ArrayList();
        int status = updateAudioPortCache(ports, null, null);
        if (status != 0) {
            return status;
        }
        filterDevicePorts(ports, devices);
        return status;
    }

    public static int listPreviousAudioDevicePorts(ArrayList<AudioDevicePort> devices) {
        if (devices == null) {
            return -2;
        }
        ArrayList<AudioPort> ports = new ArrayList();
        int status = updateAudioPortCache(null, null, ports);
        if (status != 0) {
            return status;
        }
        filterDevicePorts(ports, devices);
        return status;
    }

    private static void filterDevicePorts(ArrayList<AudioPort> ports, ArrayList<AudioDevicePort> devices) {
        devices.clear();
        for (int i = 0; i < ports.size(); i++) {
            if (ports.get(i) instanceof AudioDevicePort) {
                devices.add((AudioDevicePort) ports.get(i));
            }
        }
    }

    public static int createAudioPatch(AudioPatch[] patch, AudioPortConfig[] sources, AudioPortConfig[] sinks) {
        return AudioSystem.createAudioPatch(patch, sources, sinks);
    }

    public static int releaseAudioPatch(AudioPatch patch) {
        return AudioSystem.releaseAudioPatch(patch);
    }

    public static int listAudioPatches(ArrayList<AudioPatch> patches) {
        return updateAudioPortCache(null, patches, null);
    }

    public static int setAudioPortGain(AudioPort port, AudioGainConfig gain) {
        if (port == null || gain == null) {
            return -2;
        }
        AudioPortConfig activeConfig = port.activeConfig();
        AudioPortConfig config = new AudioPortConfig(port, activeConfig.samplingRate(), activeConfig.channelMask(), activeConfig.format(), gain);
        config.mConfigMask = 8;
        return AudioSystem.setAudioPortConfig(config);
    }

    public void registerAudioPortUpdateListener(OnAudioPortUpdateListener l) {
        sAudioPortEventHandler.init();
        sAudioPortEventHandler.registerListener(l);
    }

    public void unregisterAudioPortUpdateListener(OnAudioPortUpdateListener l) {
        sAudioPortEventHandler.unregisterListener(l);
    }

    static int resetAudioPortGeneration() {
        int generation;
        synchronized (sAudioPortGeneration) {
            generation = sAudioPortGeneration.intValue();
            sAudioPortGeneration = Integer.valueOf(0);
        }
        return generation;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static int updateAudioPortCache(java.util.ArrayList<android.media.AudioPort> r18, java.util.ArrayList<android.media.AudioPatch> r19, java.util.ArrayList<android.media.AudioPort> r20) {
        /*
        r15 = sAudioPortEventHandler;
        r15.init();
        r16 = sAudioPortGeneration;
        monitor-enter(r16);
        r15 = sAudioPortGeneration;	 Catch:{ all -> 0x004a }
        r15 = r15.intValue();	 Catch:{ all -> 0x004a }
        if (r15 != 0) goto L_0x0104;
    L_0x0010:
        r15 = 1;
        r11 = new int[r15];	 Catch:{ all -> 0x004a }
        r15 = 1;
        r13 = new int[r15];	 Catch:{ all -> 0x004a }
        r10 = new java.util.ArrayList;	 Catch:{ all -> 0x004a }
        r10.<init>();	 Catch:{ all -> 0x004a }
        r9 = new java.util.ArrayList;	 Catch:{ all -> 0x004a }
        r9.<init>();	 Catch:{ all -> 0x004a }
    L_0x0020:
        r10.clear();	 Catch:{ all -> 0x004a }
        r14 = android.media.AudioSystem.listAudioPorts(r10, r13);	 Catch:{ all -> 0x004a }
        if (r14 == 0) goto L_0x0035;
    L_0x0029:
        r15 = TAG;	 Catch:{ all -> 0x004a }
        r17 = "updateAudioPortCache: listAudioPorts failed";
        r0 = r17;
        android.util.Log.w(r15, r0);	 Catch:{ all -> 0x004a }
        monitor-exit(r16);	 Catch:{ all -> 0x004a }
    L_0x0034:
        return r14;
    L_0x0035:
        r9.clear();	 Catch:{ all -> 0x004a }
        r14 = android.media.AudioSystem.listAudioPatches(r9, r11);	 Catch:{ all -> 0x004a }
        if (r14 == 0) goto L_0x004d;
    L_0x003e:
        r15 = TAG;	 Catch:{ all -> 0x004a }
        r17 = "updateAudioPortCache: listAudioPatches failed";
        r0 = r17;
        android.util.Log.w(r15, r0);	 Catch:{ all -> 0x004a }
        monitor-exit(r16);	 Catch:{ all -> 0x004a }
        goto L_0x0034;
    L_0x004a:
        r15 = move-exception;
        monitor-exit(r16);	 Catch:{ all -> 0x004a }
        throw r15;
    L_0x004d:
        r15 = 0;
        r15 = r11[r15];	 Catch:{ all -> 0x004a }
        r17 = 0;
        r17 = r13[r17];	 Catch:{ all -> 0x004a }
        r0 = r17;
        if (r15 != r0) goto L_0x0020;
    L_0x0058:
        r3 = 0;
    L_0x0059:
        r15 = r9.size();	 Catch:{ all -> 0x004a }
        if (r3 >= r15) goto L_0x00bc;
    L_0x005f:
        r6 = 0;
    L_0x0060:
        r15 = r9.get(r3);	 Catch:{ all -> 0x004a }
        r15 = (android.media.AudioPatch) r15;	 Catch:{ all -> 0x004a }
        r15 = r15.sources();	 Catch:{ all -> 0x004a }
        r15 = r15.length;	 Catch:{ all -> 0x004a }
        if (r6 >= r15) goto L_0x008c;
    L_0x006d:
        r15 = r9.get(r3);	 Catch:{ all -> 0x004a }
        r15 = (android.media.AudioPatch) r15;	 Catch:{ all -> 0x004a }
        r15 = r15.sources();	 Catch:{ all -> 0x004a }
        r15 = r15[r6];	 Catch:{ all -> 0x004a }
        r12 = updatePortConfig(r15, r10);	 Catch:{ all -> 0x004a }
        r15 = r9.get(r3);	 Catch:{ all -> 0x004a }
        r15 = (android.media.AudioPatch) r15;	 Catch:{ all -> 0x004a }
        r15 = r15.sources();	 Catch:{ all -> 0x004a }
        r15[r6] = r12;	 Catch:{ all -> 0x004a }
        r6 = r6 + 1;
        goto L_0x0060;
    L_0x008c:
        r6 = 0;
    L_0x008d:
        r15 = r9.get(r3);	 Catch:{ all -> 0x004a }
        r15 = (android.media.AudioPatch) r15;	 Catch:{ all -> 0x004a }
        r15 = r15.sinks();	 Catch:{ all -> 0x004a }
        r15 = r15.length;	 Catch:{ all -> 0x004a }
        if (r6 >= r15) goto L_0x00b9;
    L_0x009a:
        r15 = r9.get(r3);	 Catch:{ all -> 0x004a }
        r15 = (android.media.AudioPatch) r15;	 Catch:{ all -> 0x004a }
        r15 = r15.sinks();	 Catch:{ all -> 0x004a }
        r15 = r15[r6];	 Catch:{ all -> 0x004a }
        r12 = updatePortConfig(r15, r10);	 Catch:{ all -> 0x004a }
        r15 = r9.get(r3);	 Catch:{ all -> 0x004a }
        r15 = (android.media.AudioPatch) r15;	 Catch:{ all -> 0x004a }
        r15 = r15.sinks();	 Catch:{ all -> 0x004a }
        r15[r6] = r12;	 Catch:{ all -> 0x004a }
        r6 = r6 + 1;
        goto L_0x008d;
    L_0x00b9:
        r3 = r3 + 1;
        goto L_0x0059;
    L_0x00bc:
        r4 = r9.iterator();	 Catch:{ all -> 0x004a }
    L_0x00c0:
        r15 = r4.hasNext();	 Catch:{ all -> 0x004a }
        if (r15 == 0) goto L_0x00f3;
    L_0x00c6:
        r8 = r4.next();	 Catch:{ all -> 0x004a }
        r8 = (android.media.AudioPatch) r8;	 Catch:{ all -> 0x004a }
        r2 = 0;
        r1 = r8.sources();	 Catch:{ all -> 0x004a }
        r7 = r1.length;	 Catch:{ all -> 0x004a }
        r5 = 0;
    L_0x00d3:
        if (r5 >= r7) goto L_0x00da;
    L_0x00d5:
        r12 = r1[r5];	 Catch:{ all -> 0x004a }
        if (r12 != 0) goto L_0x00ed;
    L_0x00d9:
        r2 = 1;
    L_0x00da:
        r1 = r8.sinks();	 Catch:{ all -> 0x004a }
        r7 = r1.length;	 Catch:{ all -> 0x004a }
        r5 = 0;
    L_0x00e0:
        if (r5 >= r7) goto L_0x00e7;
    L_0x00e2:
        r12 = r1[r5];	 Catch:{ all -> 0x004a }
        if (r12 != 0) goto L_0x00f0;
    L_0x00e6:
        r2 = 1;
    L_0x00e7:
        if (r2 == 0) goto L_0x00c0;
    L_0x00e9:
        r4.remove();	 Catch:{ all -> 0x004a }
        goto L_0x00c0;
    L_0x00ed:
        r5 = r5 + 1;
        goto L_0x00d3;
    L_0x00f0:
        r5 = r5 + 1;
        goto L_0x00e0;
    L_0x00f3:
        r15 = sAudioPortsCached;	 Catch:{ all -> 0x004a }
        sPreviousAudioPortsCached = r15;	 Catch:{ all -> 0x004a }
        sAudioPortsCached = r10;	 Catch:{ all -> 0x004a }
        sAudioPatchesCached = r9;	 Catch:{ all -> 0x004a }
        r15 = 0;
        r15 = r13[r15];	 Catch:{ all -> 0x004a }
        r15 = java.lang.Integer.valueOf(r15);	 Catch:{ all -> 0x004a }
        sAudioPortGeneration = r15;	 Catch:{ all -> 0x004a }
    L_0x0104:
        if (r18 == 0) goto L_0x0110;
    L_0x0106:
        r18.clear();	 Catch:{ all -> 0x004a }
        r15 = sAudioPortsCached;	 Catch:{ all -> 0x004a }
        r0 = r18;
        r0.addAll(r15);	 Catch:{ all -> 0x004a }
    L_0x0110:
        if (r19 == 0) goto L_0x011c;
    L_0x0112:
        r19.clear();	 Catch:{ all -> 0x004a }
        r15 = sAudioPatchesCached;	 Catch:{ all -> 0x004a }
        r0 = r19;
        r0.addAll(r15);	 Catch:{ all -> 0x004a }
    L_0x011c:
        if (r20 == 0) goto L_0x0128;
    L_0x011e:
        r20.clear();	 Catch:{ all -> 0x004a }
        r15 = sPreviousAudioPortsCached;	 Catch:{ all -> 0x004a }
        r0 = r20;
        r0.addAll(r15);	 Catch:{ all -> 0x004a }
    L_0x0128:
        monitor-exit(r16);	 Catch:{ all -> 0x004a }
        r14 = 0;
        goto L_0x0034;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.media.AudioManager.updateAudioPortCache(java.util.ArrayList, java.util.ArrayList, java.util.ArrayList):int");
    }

    static AudioPortConfig updatePortConfig(AudioPortConfig portCfg, ArrayList<AudioPort> ports) {
        AudioPort port = portCfg.port();
        int k = 0;
        while (k < ports.size()) {
            if (((AudioPort) ports.get(k)).handle().equals(port.handle())) {
                port = (AudioPort) ports.get(k);
                break;
            }
            k++;
        }
        if (k == ports.size()) {
            Log.e(TAG, "updatePortConfig port not found for handle: " + port.handle().id());
            return null;
        }
        AudioGainConfig gainCfg = portCfg.gain();
        if (gainCfg != null) {
            gainCfg = port.gain(gainCfg.index()).buildConfig(gainCfg.mode(), gainCfg.channelMask(), gainCfg.values(), gainCfg.rampDurationMs());
        }
        return port.buildConfig(portCfg.samplingRate(), portCfg.channelMask(), portCfg.format(), gainCfg);
    }

    private static boolean checkFlags(AudioDevicePort port, int flags) {
        if (port.role() != 2 || (flags & 2) == 0) {
            return port.role() == 1 && (flags & 1) != 0;
        } else {
            return true;
        }
    }

    private static boolean checkTypes(AudioDevicePort port) {
        return (AudioDeviceInfo.convertInternalDeviceToDeviceType(port.type()) == 0 || port.type() == -2147483520) ? false : true;
    }

    public AudioDeviceInfo[] getDevices(int flags) {
        return getDevicesStatic(flags);
    }

    private static AudioDeviceInfo[] infoListFromPortList(ArrayList<AudioDevicePort> ports, int flags) {
        int numRecs = 0;
        Iterator i$ = ports.iterator();
        while (i$.hasNext()) {
            AudioDevicePort port = (AudioDevicePort) i$.next();
            if (checkTypes(port) && checkFlags(port, flags)) {
                numRecs++;
            }
        }
        AudioDeviceInfo[] deviceList = new AudioDeviceInfo[numRecs];
        int slot = 0;
        i$ = ports.iterator();
        while (i$.hasNext()) {
            port = (AudioDevicePort) i$.next();
            if (checkTypes(port) && checkFlags(port, flags)) {
                int slot2 = slot + 1;
                deviceList[slot] = new AudioDeviceInfo(port);
                slot = slot2;
            }
        }
        return deviceList;
    }

    private static AudioDeviceInfo[] calcListDeltas(ArrayList<AudioDevicePort> ports_A, ArrayList<AudioDevicePort> ports_B, int flags) {
        ArrayList<AudioDevicePort> delta_ports = new ArrayList();
        for (int cur_index = 0; cur_index < ports_B.size(); cur_index++) {
            boolean cur_port_found = false;
            AudioDevicePort cur_port = (AudioDevicePort) ports_B.get(cur_index);
            for (int prev_index = 0; prev_index < ports_A.size() && !cur_port_found; prev_index++) {
                cur_port_found = cur_port.id() == ((AudioDevicePort) ports_A.get(prev_index)).id();
            }
            if (!cur_port_found) {
                delta_ports.add(cur_port);
            }
        }
        return infoListFromPortList(delta_ports, flags);
    }

    public static AudioDeviceInfo[] getDevicesStatic(int flags) {
        ArrayList<AudioDevicePort> ports = new ArrayList();
        if (listAudioDevicePorts(ports) != 0) {
            return new AudioDeviceInfo[0];
        }
        return infoListFromPortList(ports, flags);
    }

    public void registerAudioDeviceCallback(AudioDeviceCallback callback, Handler handler) {
        synchronized (this.mDeviceCallbacks) {
            if (callback != null) {
                if (!this.mDeviceCallbacks.containsKey(callback)) {
                    if (this.mDeviceCallbacks.size() == 0) {
                        if (this.mPortListener == null) {
                            this.mPortListener = new OnAmPortUpdateListener();
                        }
                        registerAudioPortUpdateListener(this.mPortListener);
                    }
                    NativeEventHandlerDelegate delegate = new NativeEventHandlerDelegate(callback, handler);
                    this.mDeviceCallbacks.put(callback, delegate);
                    broadcastDeviceListChange(delegate.getHandler());
                }
            }
        }
    }

    public void unregisterAudioDeviceCallback(AudioDeviceCallback callback) {
        synchronized (this.mDeviceCallbacks) {
            if (this.mDeviceCallbacks.containsKey(callback)) {
                this.mDeviceCallbacks.remove(callback);
                if (this.mDeviceCallbacks.size() == 0) {
                    unregisterAudioPortUpdateListener(this.mPortListener);
                }
            }
        }
    }

    private void broadcastDeviceListChange(Handler handler) {
        ArrayList<AudioDevicePort> current_ports = new ArrayList();
        if (listAudioDevicePorts(current_ports) == 0) {
            if (handler != null) {
                handler.sendMessage(Message.obtain(handler, 0, infoListFromPortList(current_ports, 3)));
            } else {
                AudioDeviceInfo[] added_devices = calcListDeltas(this.mPreviousPorts, current_ports, 3);
                AudioDeviceInfo[] removed_devices = calcListDeltas(current_ports, this.mPreviousPorts, 3);
                if (!(added_devices.length == 0 && removed_devices.length == 0)) {
                    synchronized (this.mDeviceCallbacks) {
                        for (int i = 0; i < this.mDeviceCallbacks.size(); i++) {
                            handler = ((NativeEventHandlerDelegate) this.mDeviceCallbacks.valueAt(i)).getHandler();
                            if (handler != null) {
                                if (added_devices.length != 0) {
                                    handler.sendMessage(Message.obtain(handler, 1, added_devices));
                                }
                                if (removed_devices.length != 0) {
                                    handler.sendMessage(Message.obtain(handler, 2, removed_devices));
                                }
                            }
                        }
                    }
                }
            }
            this.mPreviousPorts = current_ports;
        }
    }

    public int setDeviceToForceByUser(int device, String address) {
        int deviceToForceByUser;
        synchronized (sSetDeviceForceLock) {
            AudioDeviceInfo[] outDevicesInfo = getDevices(2);
            int size = outDevicesInfo.length;
            int i = 0;
            while (i < size) {
                if (outDevicesInfo[i].getDeviceId() == device && outDevicesInfo[i].getAddress().equals(address)) {
                    Log.d(TAG, "Device 0x" + device + " is available to set by force");
                    try {
                        deviceToForceByUser = getService().setDeviceToForceByUser(device, address);
                        break;
                    } catch (RemoteException e) {
                        Log.e(TAG, "Dead object in setDeviceToForceByUser", e);
                        deviceToForceByUser = 0;
                    }
                } else {
                    i++;
                }
            }
            deviceToForceByUser = -1;
        }
        return deviceToForceByUser;
    }
}
