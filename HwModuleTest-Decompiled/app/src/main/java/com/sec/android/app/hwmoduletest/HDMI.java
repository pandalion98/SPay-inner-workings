package com.sec.android.app.hwmoduletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.SystemProperties;
import android.widget.Toast;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;

public class HDMI extends BaseActivity {
    /* access modifiers changed from: private */
    public String ACTION_HDMI_AUDIO_PLUG;
    private final String HDMI_MSG;
    private float MAX_VOLUME;
    /* access modifiers changed from: private */
    public AudioManager mAudioManager;
    private BroadcastReceiver mBroadcastReceiver;
    /* access modifiers changed from: private */
    public boolean mIsTurnOffAudioPath;
    private MediaPlayer mMediaPlayer;

    public HDMI() {
        this("UIHDMI");
    }

    public HDMI(String className) {
        super(className);
        this.HDMI_MSG = "HDMI Pattern Display On";
        this.mIsTurnOffAudioPath = false;
        this.MAX_VOLUME = 100.0f;
        this.mBroadcastReceiver = new BroadcastReceiver() {
            public synchronized void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                try {
                    if ("com.android.samsungtest.HDMITEST_STOP".equals(action)) {
                        HDMI.this.finish();
                    } else if (action.equals(HDMI.this.ACTION_HDMI_AUDIO_PLUG)) {
                        int state = intent.getIntExtra("state", -1);
                        StringBuilder sb = new StringBuilder();
                        sb.append("AudioManager.ACTION_HDMI_AUDIO_PLUG - state=");
                        sb.append(state);
                        LtUtil.log_i(HDMI.this.CLASS_NAME, "onReceive", sb.toString());
                        if (HDMI.this.isFinishing()) {
                            LtUtil.log_i(HDMI.this.CLASS_NAME, "onReceive", "Already UIHDMI finished");
                        }
                        if (state == 1) {
                            HDMI.this.stopMedia();
                            HDMI.this.mAudioManager.setParameters("factory_test_route=hdmi");
                            HDMI.this.playMedia(C0268R.raw.mp3_1khz, true);
                        } else if (state == 0) {
                            HDMI.this.stopMedia();
                            Thread.sleep(300);
                            HDMI.this.mIsTurnOffAudioPath = false;
                            HDMI.this.turnOffAudioPath();
                            Thread.sleep(300);
                            if (!HDMI.this.isFinishing()) {
                                HDMI.this.playMedia(C0268R.raw.mp3_1khz, true);
                            }
                        }
                    }
                } catch (Exception e) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Exception : ");
                    sb2.append(e);
                    LtUtil.log_i(HDMI.this.CLASS_NAME, "mBroadcastReceiver.onReceive", sb2.toString());
                }
                return;
            }
        };
        if (VERSION.SDK_INT <= 19) {
            this.ACTION_HDMI_AUDIO_PLUG = "android.intent.action.HDMI_AUDIO_PLUG";
        } else {
            this.ACTION_HDMI_AUDIO_PLUG = "android.media.action.HDMI_AUDIO_PLUG";
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String platform = SystemProperties.get("ro.board.platform");
        this.mAudioManager = (AudioManager) getSystemService("audio");
        if (Feature.getBoolean(Feature.SUPPORT_HDMI_43_RATIO)) {
            setContentView(C0268R.layout.hdmi_43ratio);
        } else if (platform == null || !platform.equals("capri")) {
            setContentView(C0268R.layout.hdmi);
        } else {
            setContentView(C0268R.layout.hdmi_capri);
        }
        LtUtil.log_i(this.CLASS_NAME, "onCreate", "mMediaPlayer.setVolume => MAX_VOLUME");
    }

    public void onResume() {
        super.onResume();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("com.android.samsungtest.HDMITEST_STOP");
        mIntentFilter.addAction(this.ACTION_HDMI_AUDIO_PLUG);
        registerReceiver(this.mBroadcastReceiver, mIntentFilter);
        this.mIsTurnOffAudioPath = false;
        StringBuilder sb = new StringBuilder();
        sb.append("mIsTurnOffAudioPath = ");
        sb.append(this.mIsTurnOffAudioPath);
        LtUtil.log_i(this.CLASS_NAME, "onResume()", sb.toString());
        playMedia(C0268R.raw.mp3_1khz, true);
        Toast.makeText(getApplicationContext(), "HDMI Pattern Display On", 0).show();
    }

    public void onPause() {
        super.onPause();
        unregisterReceiver(this.mBroadcastReceiver);
        LtUtil.log_i(this.CLASS_NAME, "onPause", "stopMedia() ...");
        stopMedia();
    }

    /* access modifiers changed from: private */
    public synchronized void turnOffAudioPath() {
        if (isFinishing()) {
            LtUtil.log_i(this.CLASS_NAME, "turnOffAudioPath", "Already UIHDMI finished");
            this.mAudioManager.setParameters("factory_test_route=off");
        } else if (!this.mIsTurnOffAudioPath) {
            this.mIsTurnOffAudioPath = true;
            this.mAudioManager.setParameters("factory_test_route=off");
            StringBuilder sb = new StringBuilder();
            sb.append("mIsTurnOffAudioPath = ");
            sb.append(this.mIsTurnOffAudioPath);
            LtUtil.log_i(this.CLASS_NAME, "turnOffAudioPath()", sb.toString());
        }
    }

    public void playMedia(int resId, boolean isLoop) {
        release();
        this.mMediaPlayer = MediaPlayer.create(getApplicationContext(), resId);
        this.mMediaPlayer.setLooping(isLoop);
        this.mMediaPlayer.start();
    }

    /* access modifiers changed from: private */
    public void stopMedia() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            release();
            turnOffAudioPath();
        }
    }

    private void release() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }
}
