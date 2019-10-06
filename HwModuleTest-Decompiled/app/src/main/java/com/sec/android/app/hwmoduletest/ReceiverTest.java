package com.sec.android.app.hwmoduletest;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.goodix.cap.fingerprint.Constants;
import com.samsung.android.displaysolution.SemDisplaySolutionManager;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.android.app.hwmoduletest.support.LtUtil.Sleep;
import com.sec.xmldata.support.Support.Feature;

public class ReceiverTest extends BaseActivity {
    public static final int AUDIO_PATH_EAR = 2;
    public static final int AUDIO_PATH_HDMI = 3;
    public static final int AUDIO_PATH_OFF = 4;
    public static final int AUDIO_PATH_RCV = 1;
    public static final int AUDIO_PATH_SPK = 0;
    private static IBinder mBinder = new Binder();
    public static int preVolume = 0;
    private final String[] AUDIO_PATH = {"spk", "rcv", "ear", "hdmi", "off"};
    private AudioManager mAudioManager;
    private MediaPlayer mMediaPlayer;
    private SemDisplaySolutionManager mSemDisplaySolutionManager;

    public ReceiverTest() {
        super("ReceiverTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = new View(this);
        view.setBackgroundColor(-1);
        setContentView(view);
        this.mAudioManager = (AudioManager) getSystemService("audio");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (Feature.getBoolean(Feature.IS_DUAL_CLOCK_AUDIO)) {
            this.mMediaPlayer = MediaPlayer.create(this, C0268R.raw.mp3_1khz_48);
        } else {
            this.mMediaPlayer = MediaPlayer.create(this, C0268R.raw.mp3_1khz);
        }
        this.mMediaPlayer.setLooping(true);
        if (VERSION.SDK_INT > 23) {
            setACL(true);
        }
        preVolume = this.mAudioManager.getStreamVolume(3);
        this.mAudioManager.setStreamVolume(3, this.mAudioManager.getStreamMaxVolume(3), 0);
        StringBuilder sb = new StringBuilder();
        sb.append("index : ");
        sb.append(this.mAudioManager.getStreamMaxVolume(3));
        LtUtil.log_d(this.CLASS_NAME, "onResume", sb.toString());
        setLoopbackOff();
        setAudioPath(1);
        this.mAudioManager.setMode(0);
        if (Feature.getBoolean(Feature.ENABLE_RCV_PLAY_START_DELAY)) {
            Sleep.sleep(100);
        }
        this.mMediaPlayer.start();
        LtUtil.log_d(this.CLASS_NAME, "onResume", "Media start");
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mMediaPlayer.stop();
        LtUtil.log_d(this.CLASS_NAME, "onPause", "Media stop");
        if (VERSION.SDK_INT > 23) {
            setACL(false);
        }
        this.mMediaPlayer.release();
        try {
            LtUtil.log_d(this.CLASS_NAME, "onDestroy", "wait 200ms... release media player");
            Thread.sleep(200);
        } catch (InterruptedException ie) {
            LtUtil.log_e(ie);
        }
        this.mAudioManager.setStreamVolume(3, preVolume, 0);
        setAudioPath(4);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 24:
                LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "VOLUME_UP");
                this.mMediaPlayer.start();
                break;
            case Constants.CMD_TEST_CANCEL /*25*/:
                LtUtil.log_d(this.CLASS_NAME, "onKeyDown", "VOLUME_DOWN");
                this.mMediaPlayer.pause();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    private void setLoopbackOff() {
        LtUtil.log_i(this.CLASS_NAME, "setLoopbackOff", "Loopback Off");
        this.mAudioManager.setParameters("factory_test_loopback=off");
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

    private void setACL(boolean set) {
        StringBuilder sb = new StringBuilder();
        sb.append("set :");
        sb.append(set);
        LtUtil.log_d(this.CLASS_NAME, "setACL", sb.toString());
        if (VERSION.SDK_INT >= 28) {
            try {
                SemDisplaySolutionManager mSemDisplaySolutionManager2 = (SemDisplaySolutionManager) getSystemService("DisplaySolution");
                mSemDisplaySolutionManager2.getClass().getMethod("onAutoCurrentLimitStateChanged", new Class[]{Boolean.TYPE}).invoke(mSemDisplaySolutionManager2, new Object[]{Boolean.valueOf(set)});
            } catch (Exception e) {
                LtUtil.log_i(this.CLASS_NAME, "onAutoCurrentLimitStateChanged", e.toString());
            }
        } else {
            try {
                PowerManager pm = (PowerManager) getSystemService("power");
                pm.getClass().getMethod("setClearViewBrightnessMode", new Class[]{Boolean.TYPE, Integer.TYPE, IBinder.class}).invoke(pm, new Object[]{Boolean.valueOf(set), Integer.valueOf(0), mBinder});
            } catch (Exception e2) {
                LtUtil.log_i(this.CLASS_NAME, "setClearViewBrightnessMode", e2.toString());
            }
        }
    }
}
