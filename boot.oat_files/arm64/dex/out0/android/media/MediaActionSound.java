package android.media;

import android.media.SoundPool.OnLoadCompleteListener;
import android.util.Log;

public class MediaActionSound {
    public static final int FOCUS_COMPLETE = 1;
    private static final int NUM_MEDIA_SOUND_STREAMS = 1;
    public static final int SHUTTER_CLICK = 0;
    private static final String[] SOUND_FILES = new String[]{"/system/media/audio/ui/camera_click.ogg", "/system/media/audio/ui/camera_focus.ogg", "/system/media/audio/ui/VideoRecord.ogg", "/system/media/audio/ui/VideoStop.ogg"};
    private static final int SOUND_NOT_LOADED = -1;
    public static final int START_VIDEO_RECORDING = 2;
    public static final int STOP_VIDEO_RECORDING = 3;
    private static final String TAG = "MediaActionSound";
    private OnLoadCompleteListener mLoadCompleteListener = new OnLoadCompleteListener() {
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            if (status != 0) {
                Log.e(MediaActionSound.TAG, "Unable to load sound for playback (status: " + status + ")");
            } else if (MediaActionSound.this.mSoundIdToPlay == sampleId) {
                soundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);
                MediaActionSound.this.mSoundIdToPlay = -1;
            }
        }
    };
    private int mSoundIdToPlay;
    private int[] mSoundIds;
    private SoundPool mSoundPool = new SoundPool(1, 7, 0);

    public MediaActionSound() {
        this.mSoundPool.setOnLoadCompleteListener(this.mLoadCompleteListener);
        this.mSoundIds = new int[SOUND_FILES.length];
        for (int i = 0; i < this.mSoundIds.length; i++) {
            this.mSoundIds[i] = -1;
        }
        this.mSoundIdToPlay = -1;
    }

    public synchronized void load(int soundName) {
        if (soundName >= 0) {
            if (soundName < SOUND_FILES.length) {
                if (this.mSoundIds[soundName] == -1) {
                    this.mSoundIds[soundName] = this.mSoundPool.load(SOUND_FILES[soundName], 1);
                }
            }
        }
        throw new RuntimeException("Unknown sound requested: " + soundName);
    }

    public synchronized void play(int soundName) {
        if (soundName >= 0) {
            if (soundName < SOUND_FILES.length) {
                if (this.mSoundIds[soundName] == -1) {
                    this.mSoundIdToPlay = this.mSoundPool.load(SOUND_FILES[soundName], 1);
                    this.mSoundIds[soundName] = this.mSoundIdToPlay;
                } else {
                    this.mSoundPool.play(this.mSoundIds[soundName], 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        }
        throw new RuntimeException("Unknown sound requested: " + soundName);
    }

    public void release() {
        if (this.mSoundPool != null) {
            this.mSoundPool.release();
            this.mSoundPool = null;
        }
    }
}
