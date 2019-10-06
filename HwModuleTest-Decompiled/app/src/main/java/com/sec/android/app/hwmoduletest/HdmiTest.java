package com.sec.android.app.hwmoduletest;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Toast;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;

public class HdmiTest extends BaseActivity {
    private final String HDMI_MSG = "HDMI Pattern Display On";
    private MediaPlayer mMediaPlayer;

    public HdmiTest() {
        super("HdmiTest");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.hdmitest);
        LtUtil.setRemoveSystemUI(getWindow(), true);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        playMedia(C0268R.raw.mp3_1khz, true);
        Toast.makeText(this, "HDMI Pattern Display On", 0).show();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        stopMedia();
        super.onPause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 24) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void playMedia(int resId, boolean isLoop) {
        release();
        this.mMediaPlayer = MediaPlayer.create(getApplicationContext(), resId);
        this.mMediaPlayer.setLooping(isLoop);
        this.mMediaPlayer.start();
    }

    private void stopMedia() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            release();
        }
    }

    private void release() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }
}
