package com.sec.android.app.hwmoduletest;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;

public class LowFrequencyTest extends BaseActivity implements OnClickListener {
    public static final int AUDIO_PATH_EAR = 2;
    public static final int AUDIO_PATH_HDMI = 3;
    public static final int AUDIO_PATH_OFF = 4;
    public static final int AUDIO_PATH_RCV = 1;
    public static final int AUDIO_PATH_SPK = 0;
    private final String[] AUDIO_PATH = {"spk", "rcv", "ear", "hdmi", "off"};
    private ToggleButton m100Hz;
    private ToggleButton m200Hz;
    private ToggleButton m300Hz;
    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;

    public LowFrequencyTest() {
        super("LowFrequencyTest");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.low_frequency_test);
        this.mAudioManager = (AudioManager) getSystemService("audio");
        this.m100Hz = (ToggleButton) findViewById(C0268R.C0269id.freq_100);
        this.m200Hz = (ToggleButton) findViewById(C0268R.C0269id.freq_200);
        this.m300Hz = (ToggleButton) findViewById(C0268R.C0269id.freq_300);
        this.m100Hz.setOnClickListener(this);
        this.m200Hz.setOnClickListener(this);
        this.m300Hz.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mAudioManager.setStreamVolume(3, this.mAudioManager.getStreamMaxVolume(3), 0);
        setAudioPath(1);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        stopMedia();
        try {
            LtUtil.log_d(this.CLASS_NAME, "onPause", "wait 200ms... release media player");
            Thread.sleep(200);
        } catch (InterruptedException ie) {
            LtUtil.log_e(ie);
        }
        setAudioPath(4);
    }

    public void onClick(View v) {
        updateToggle(v.getId());
        updateMedia(v);
    }

    private void updateToggle(int resID) {
        boolean z = false;
        this.m100Hz.setChecked(resID == C0268R.C0269id.freq_100 ? this.m100Hz.isChecked() : false);
        this.m200Hz.setChecked(resID == C0268R.C0269id.freq_200 ? this.m200Hz.isChecked() : false);
        ToggleButton toggleButton = this.m300Hz;
        if (resID == C0268R.C0269id.freq_300) {
            z = this.m300Hz.isChecked();
        }
        toggleButton.setChecked(z);
    }

    private void updateMedia(View v) {
        ToggleButton toggle = (ToggleButton) v;
        stopMedia();
        if (toggle.isChecked()) {
            int resID = 0;
            switch (v.getId()) {
                case C0268R.C0269id.freq_100 /*2131296488*/:
                    resID = C0268R.raw.lf_100hz;
                    break;
                case C0268R.C0269id.freq_200 /*2131296489*/:
                    resID = C0268R.raw.lf_200hz;
                    break;
                case C0268R.C0269id.freq_300 /*2131296490*/:
                    resID = C0268R.raw.lf_300hz;
                    break;
            }
            startMedia(resID);
        }
    }

    private void startMedia(int resID) {
        setAudioPath(1);
        release();
        this.mMediaPlayer = MediaPlayer.create(this, resID);
        this.mMediaPlayer.setLooping(true);
        this.mAudioManager.setMode(0);
        this.mMediaPlayer.start();
    }

    private void stopMedia() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            try {
                LtUtil.log_d(this.CLASS_NAME, "stopMedia", "wait 100ms... before release media player");
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                LtUtil.log_e(ie);
            }
            release();
        }
    }

    private void release() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    public void setAudioPath(int path) {
        StringBuilder sb = new StringBuilder();
        sb.append("setAudioPath : factory_test_route=");
        sb.append(this.AUDIO_PATH[path]);
        LtUtil.log_i(this.CLASS_NAME, "setMode", sb.toString());
        AudioManager audioManager = this.mAudioManager;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("factory_test_route=");
        sb2.append(this.AUDIO_PATH[path]);
        audioManager.setParameters(sb2.toString());
    }
}
