package android.media;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.ProxyInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.SystemProperties;
import android.util.Log;

public class SamsungAudioManager {
    public static final String ACTION_SPLIT_SOUND = "com.sec.android.intent.action.SPLIT_SOUND";
    public static final int BACKGROUND_MUSIC_ID_WIZARD = 0;
    public static final int FLAG_DISMISS_UI_WARNINGS = 268435456;
    public static final int FLAG_KEY_ACTION_DOWN = 536870912;
    public static final int FLAG_SKIP_RINGER_MODES = 67108864;
    public static final int FLAG_UDATE_STATE = 33554432;
    public static final int FLAG_UI_EXPANDED = 134217728;
    public static final String FM_RADIO = "FM_RADIO";
    public static final int FORCE_EARPIECE = 14;
    public static final int FORCE_NONE = 0;
    private static final float GAIN_MAX = 1.0f;
    private static final float GAIN_MIN = 0.0f;
    public static final int MYSPACE_EFFECT_CENTER_TO_RIGHT = 1;
    public static final int MYSPACE_EFFECT_DELAY = 50;
    public static final int MYSPACE_EFFECT_DIRECT_LEFT_TO_CENTER = 2;
    public static final int MYSPACE_EFFECT_LEFT_TO_CENTER = 0;
    public static final int MYSPACE_EFFECT_MAX_TIMED_OUT = 1500;
    public static final int MYSPACE_EFFECT_TIMED_OUT = 1000;
    public static final int MYSPACE_FADEIN_FOR_MUSIC = 2;
    public static final int MYSPACE_FADEIN_FOR_RINGTONE = 3;
    public static final int MYSPACE_FADEOUT_FOR_NOTIFICATION = 0;
    public static final int MYSPACE_FADEOUT_FOR_RINGTONE = 1;
    public static final String SAMSUNG_EXTRA_VOLUME_STREAM_TYPE = "android.media.EXTRA_VOLUME_STREAM_TYPE";
    public static final String SAMSUNG_EXTRA_VOLUME_STREAM_VALUE = "android.media.EXTRA_VOLUME_STREAM_VALUE";
    public static final String SAMSUNG_VOLUME_CHANGED_ACTION = "android.media.VOLUME_CHANGED_ACTION";
    public static final int STREAM_BLUETOOTH_SCO = 4;
    public static final int STREAM_FM_RADIO = 1;
    public static final int STREAM_SYSTEM_ENFORCED = 5;
    public static final int STREAM_VIDEO_CALL = 2;
    public static final int STREAM_VOICENOTE = 3;
    public static final String SUPPORT_AOBLE = "android.bluetooth.aoble.extra.SUPPORT_AOBLE";
    private static String TAG = "SamsungAudioManager";
    private final AudioManager mAudioManager;
    private final Context mContext;
    MySpaceManager mMySpaceManager = new MySpaceManager();

    public static final class AudioLog {
        public static final int HIGH = 18760;
        public static final int LOW = 20300;
        public static final int MID = 18765;
        public static final int SLOG = 0;
        private static int debugLevel;
        public static boolean isSecLogEnable;

        static {
            isSecLogEnable = false;
            debugLevel = LOW;
            isSecLogEnable = WifiEnterpriseConfig.ENGINE_ENABLE.equals(SystemProperties.get("persist.log.seclevel", "0"));
            try {
                debugLevel = Integer.parseInt(SystemProperties.get("ro.debug_level", "0x4f4c").substring(2), 16);
            } catch (NumberFormatException e) {
                debugLevel = LOW;
            }
        }

        public static boolean isLoggable(String tag, int level) {
            if (level == 0) {
                return isSecLogEnable;
            }
            if ((level == LOW || level == MID || level == HIGH) && level == debugLevel) {
                return true;
            }
            return false;
        }
    }

    public SamsungAudioManager(Context context) {
        this.mContext = context;
        this.mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    public int playMySpaceEffect(int effectType) {
        if (!(!true || isSplitSoundOn() || isHMTmounted() || this.mMySpaceManager.checkEnableCondition())) {
            this.mMySpaceManager.playMySpaceEffect(this.mMySpaceManager.mRingAttributes, 3);
            Log.v(TAG, "playMySpaceEffect RING FADE OUT");
        }
        return 0;
    }

    public boolean isSplitSoundOn() {
        String strReturn = this.mAudioManager.getAudioServiceConfig(AudioParameter.AUDIO_PARAMETER_KEY_SPLITSOUND);
        if (strReturn == null || !AudioParameter.AUDIO_PARAMETER_VALUE_true.equals(strReturn)) {
            return false;
        }
        return true;
    }

    public boolean isSplitSoundRunning() {
        if (AudioParameter.AUDIO_PARAMETER_VALUE_true.equals(AudioSystem.getParameters("audioParam;split_sound_for_call"))) {
            return true;
        }
        return false;
    }

    public boolean isRemoteSubmixOn() {
        if (AudioParameter.AUDIO_PARAMETER_VALUE_true.equals(AudioSystem.getParameters("audioParam;split_sound_for_call"))) {
            return true;
        }
        return false;
    }

    public boolean isHMTmounted() {
        String strReturn = this.mAudioManager.getAudioServiceConfig(AudioParameter.AUDIO_PARAMETER_KEY_HMT_STATE);
        if (strReturn == null || !AudioParameter.AUDIO_PARAMETER_VALUE_true.equals(strReturn)) {
            return false;
        }
        return true;
    }

    public void setFineMediaVolume(int volume) {
        this.mAudioManager.setAudioServiceConfig("fine_volume=" + volume);
    }

    public int getFineMediaVolume() {
        try {
            return Integer.parseInt(this.mAudioManager.getAudioServiceConfig(AudioParameter.AUDIO_PARAMETER_KEY_FINEVOLUME));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void updateBluetoothDevice(BluetoothDevice btDevice, int sampleRate) {
        this.mAudioManager.setAudioServiceConfig("UHQ_BT=" + sampleRate);
    }

    public void playHighampereGame(int state) {
        if (state != 0) {
            AudioSystem.setParameters("audioParam;High_ampere_game=true");
        } else {
            AudioSystem.setParameters("audioParam;High_ampere_game=false");
        }
    }

    public int getCurrentDeviceType() {
        String path = this.mAudioManager.getParameters(AudioManager.OUTDEVICE);
        if (path != null && !ProxyInfo.LOCAL_EXCL_LIST.equals(path)) {
            return AudioDeviceInfo.convertInternalDeviceToDeviceType(Integer.valueOf(path).intValue());
        }
        Log.i(TAG, "isDeviceTypeActive : Can't get outDevice");
        return 0;
    }

    public boolean isUsingAudio(String packageName) {
        if (packageName == null || ProxyInfo.LOCAL_EXCL_LIST.equals(packageName)) {
            return false;
        }
        if (AudioParameter.AUDIO_PARAMETER_VALUE_true.equals(AudioSystem.getParameters("isUseAudio=" + packageName))) {
            return true;
        }
        return false;
    }

    public boolean isSafeMediaVolumeDeviceOn() {
        String strReturn = this.mAudioManager.getAudioServiceConfig(AudioParameter.AUDIO_PARAMETER_KEY_IS_SAFE_MEDIA_VOLUME_DEVICE);
        if (strReturn == null || !AudioParameter.AUDIO_PARAMETER_VALUE_true.equals(strReturn)) {
            return false;
        }
        return true;
    }

    public int getAvailableDeviceMaskForQuickSoundPath() {
        String strReturn = this.mAudioManager.getAudioServiceConfig(AudioParameter.AUDIO_PARAMETER_KEY_GET_DEVICE_MASK_FOR_QUICK_SOUND_PATH);
        if (strReturn != null) {
            return Integer.parseInt(strReturn, 16);
        }
        return Integer.MAX_VALUE;
    }

    public void setForceUseForMedia(int forced_config) {
        this.mAudioManager.setAudioServiceConfig("setForceUseForMedia=" + forced_config);
    }

    public static int stream(int samsung_stream) {
        switch (samsung_stream) {
            case 1:
                return 10;
            case 2:
                return 11;
            case 3:
                return 13;
            case 4:
                return 6;
            case 5:
                return 7;
            default:
                return -1;
        }
    }

    public static int getVideoCallMode() {
        return 4;
    }

    public static int getDeviceOut(int typeDevice) {
        return AudioDeviceInfo.convertDeviceTypeToInternalDevice(typeDevice);
    }

    public void setAppVolume(int pid, float trackVolume) {
        AudioSystem.setParameters("appVolume_pid=" + pid + ";appVolume_volume=" + clampGainOrLevel(trackVolume));
    }

    public void setAppVolume(String packageNames, float trackVolume) {
        if (packageNames == null || ProxyInfo.LOCAL_EXCL_LIST.equals(packageNames)) {
            Log.e(TAG, "packageNames is null");
            return;
        }
        AudioSystem.setParameters("appVolume_packagenames=" + packageNames + ";appVolume_volume=" + clampGainOrLevel(trackVolume));
    }

    private static float clampGainOrLevel(float gainOrLevel) {
        if (Float.isNaN(gainOrLevel)) {
            throw new IllegalArgumentException();
        } else if (gainOrLevel < 0.0f) {
            return 0.0f;
        } else {
            if (gainOrLevel > 1.0f) {
                return 1.0f;
            }
            return gainOrLevel;
        }
    }
}
