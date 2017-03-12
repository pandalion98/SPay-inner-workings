package android.media;

import android.media.AudioAttributes.Builder;
import android.net.ProxyInfo;

public class MySpaceManager {
    public static final int MYSPACE_BYPASS = 5;
    public static final int MYSPACE_CLEAR_PRESET = 6;
    public static final int MYSPACE_EFFECT_MAX_TIMED_OUT = 1500;
    public static final int MYSPACE_EFFECT_TIMED_OUT = 1000;
    public static final int MYSPACE_MUSIC_FADEIN = 0;
    public static final int MYSPACE_MUSIC_FADEOUT = 1;
    public static final int MYSPACE_MUSIC_FADE_OUT_IN = 4;
    public static final int MYSPACE_RING_FADEIN = 2;
    public static final int MYSPACE_RING_FADEOUT = 3;
    private static final int STREAM_TYPE = 0;
    private static final String TAG = "MySpaceManager";
    public final AudioAttributes mMusicAttributes = new Builder().setUsage(1).setContentType(2).build();
    public final AudioAttributes mRingAttributes = new Builder().setUsage(6).setContentType(4).build();

    public void setParameter(int presetType) {
        AudioSystem.setParameters("audioEffectParam;setMySpaceEffectType=" + presetType);
    }

    public void playMySpaceEffect(AudioAttributes mAudioAttributes, int effectPreset) {
        if (effectPreset == 0) {
            if (mAudioAttributes.getUsage() == 1) {
                if (AudioSystem.isStreamActive(3, 0)) {
                    setParameter(4);
                    setParameter(0);
                    return;
                }
                setParameter(0);
            } else if (mAudioAttributes.getUsage() == 6) {
                setParameter(0);
            }
        } else if (effectPreset == 1) {
            if (mAudioAttributes.getUsage() == 1) {
                setParameter(1);
            }
        } else if (effectPreset == 2) {
            if (mAudioAttributes.getUsage() == 6) {
                setParameter(2);
            }
        } else if (effectPreset == 3 && mAudioAttributes.getUsage() == 6) {
            setParameter(3);
        }
    }

    public boolean checkEnableCondition() {
        String path = AudioSystem.getParameters(AudioManager.OUTDEVICE);
        if (path == null || ProxyInfo.LOCAL_EXCL_LIST.equals(path)) {
            return false;
        }
        return (Integer.valueOf(path).intValue() & 12) == 0;
    }

    public void playMySpaceEffect(int usage, int effectPreset) {
        if (effectPreset == 0) {
            if (usage == 1) {
                if (AudioSystem.isStreamActive(3, 0)) {
                    setParameter(4);
                    setParameter(0);
                    return;
                }
                setParameter(0);
            } else if (usage == 6) {
                setParameter(0);
            }
        } else if (effectPreset == 1) {
            if (usage == 1) {
                setParameter(1);
            }
        } else if (effectPreset == 2) {
            if (usage == 6) {
                setParameter(2);
            }
        } else if (effectPreset == 3 && usage == 6) {
            setParameter(3);
        }
    }

    public void MySpaceSet3DPosition(int enable, float mX, float mY, float mZ) {
        if (enable > 0) {
            AudioSystem.setParameters("audioEffectParam;MySpaceSet3DPosition=1," + mX + "," + mY + "," + mZ);
        } else {
            AudioSystem.setParameters("audioEffectParam;MySpaceSet3DPosition=0," + mX + "," + mY + "," + mZ);
        }
    }
}
