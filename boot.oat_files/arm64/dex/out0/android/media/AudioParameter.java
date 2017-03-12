package android.media;

import android.net.ProxyInfo;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.StringTokenizer;

public class AudioParameter {
    public static final String AUDIO_PARAMETER_KEY_BT_AOBLE_MODE = "bt_aoble_mode";
    public static final String AUDIO_PARAMETER_KEY_FINEVOLUME = "fine_volume";
    public static final String AUDIO_PARAMETER_KEY_GET_ACTIVE_ADDRESS = "active_addr";
    public static final String AUDIO_PARAMETER_KEY_GET_DEVICE_MASK_FOR_QUICK_SOUND_PATH = "device_mask_QSP";
    public static final String AUDIO_PARAMETER_KEY_HMT_STATE = "HMTstate";
    public static final String AUDIO_PARAMETER_KEY_IS_SAFE_MEDIA_VOLUME_DEVICE = "SafeMediaVolumeDevice";
    public static final String AUDIO_PARAMETER_KEY_IS_USE_AUDIO = "isUseAudio";
    public static final String AUDIO_PARAMETER_KEY_SCREEN_MIRRORING_STATE = "ScreenMirroringState";
    public static final String AUDIO_PARAMETER_KEY_SET_FORCE_USE_FOR_MEDIA = "setForceUseForMedia";
    public static final String AUDIO_PARAMETER_KEY_SPLITSOUND = "SplitSound";
    public static final String AUDIO_PARAMETER_KEY_UHQ_BT = "UHQ_BT";
    public static final String AUDIO_PARAMETER_VALUE_false = "false";
    public static final String AUDIO_PARAMETER_VALUE_off = "off";
    public static final String AUDIO_PARAMETER_VALUE_on = "on";
    public static final String AUDIO_PARAMETER_VALUE_true = "true";
    public static final int BAD_VALUE = -1;
    public static final int NO_ERROR = 0;
    private Hashtable<String, String> mAudioParams = new Hashtable();

    public AudioParameter(String audioParams) {
        if (audioParams != null) {
            StringTokenizer st1 = new StringTokenizer(audioParams, ";");
            while (st1.hasMoreTokens()) {
                String key;
                String value;
                StringTokenizer st2 = new StringTokenizer(st1.nextToken(), "=");
                if (st2.hasMoreTokens()) {
                    key = st2.nextToken();
                } else {
                    key = null;
                }
                if (st2.hasMoreTokens()) {
                    value = st2.nextToken();
                } else {
                    value = null;
                }
                if (!(key == null || value == null)) {
                    this.mAudioParams.put(key, value);
                }
            }
        }
    }

    public String get(String strKey) {
        if (strKey == null || this.mAudioParams == null || this.mAudioParams.isEmpty()) {
            return null;
        }
        return (String) this.mAudioParams.get(strKey);
    }

    public String toString() {
        String strReturn = ProxyInfo.LOCAL_EXCL_LIST;
        if (!(this.mAudioParams == null || this.mAudioParams.isEmpty())) {
            Iterator i = this.mAudioParams.entrySet().iterator();
            while (i.hasNext()) {
                Entry m = (Entry) i.next();
                strReturn = ((strReturn + ((String) m.getKey())) + "=") + ((String) m.getValue());
                if (i.hasNext()) {
                    strReturn = strReturn + ";";
                }
            }
        }
        return strReturn;
    }
}
