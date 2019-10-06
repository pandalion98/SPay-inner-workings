package com.sec.android.app.hwmoduletest;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Values;
import java.io.File;

public class SpeakerTest extends BaseActivity implements OnClickListener {
    public static int AUDIO_PATH_DUMMY = 0;
    public static final int AUDIO_PATH_OFF;
    public static final int AUDIO_PATH_SPK;
    public static final int AUDIO_PATH_SPK1;
    public static final int AUDIO_PATH_SPK2;
    public static final int AUDIO_PATH_SPK3;
    public static final int AUDIO_PATH_SPK4;
    protected static final String CLASS_NAME = "Speaker4Test";
    private static int preVolume = 0;
    private int DUMMY = 0;
    /* access modifiers changed from: private */
    public final int FINISH_TEST;
    private final int LOWER_L;
    private final int LOWER_R;
    /* access modifiers changed from: private */
    public final int TEST_FAIL;
    private int TEST_ID = 0;
    private final int UPPER_L;
    private final int UPPER_R;
    private Button button_fail;
    private Button button_lowerSPKL;
    private Button button_lowerSPKR;
    private Button button_upperSPKL;
    private Button button_upperSPKR;
    private AudioManager mAudioManager;
    private String[] mAudioPath;
    private final Handler mHandler;
    private boolean mIsReverse;
    private MediaPlayer mMediaPlayer;
    private TextView txt_lowerL;
    private TextView txt_lowerR;
    private TextView txt_msg;
    private TextView txt_title;
    private TextView txt_upperL;
    private TextView txt_upperR;

    static {
        AUDIO_PATH_DUMMY = -1;
        int i = AUDIO_PATH_DUMMY + 1;
        AUDIO_PATH_DUMMY = i;
        AUDIO_PATH_SPK1 = i;
        int i2 = AUDIO_PATH_DUMMY + 1;
        AUDIO_PATH_DUMMY = i2;
        AUDIO_PATH_SPK2 = i2;
        int i3 = AUDIO_PATH_DUMMY + 1;
        AUDIO_PATH_DUMMY = i3;
        AUDIO_PATH_SPK3 = i3;
        int i4 = AUDIO_PATH_DUMMY + 1;
        AUDIO_PATH_DUMMY = i4;
        AUDIO_PATH_SPK4 = i4;
        int i5 = AUDIO_PATH_DUMMY + 1;
        AUDIO_PATH_DUMMY = i5;
        AUDIO_PATH_SPK = i5;
        int i6 = AUDIO_PATH_DUMMY + 1;
        AUDIO_PATH_DUMMY = i6;
        AUDIO_PATH_OFF = i6;
    }

    public SpeakerTest() {
        super("SpeakerTest");
        int i = this.DUMMY;
        this.DUMMY = i + 1;
        this.UPPER_L = i;
        int i2 = this.DUMMY;
        this.DUMMY = i2 + 1;
        this.UPPER_R = i2;
        int i3 = this.DUMMY;
        this.DUMMY = i3 + 1;
        this.LOWER_R = i3;
        int i4 = this.DUMMY;
        this.DUMMY = i4 + 1;
        this.LOWER_L = i4;
        int i5 = this.DUMMY;
        this.DUMMY = i5 + 1;
        this.FINISH_TEST = i5;
        int i6 = this.DUMMY;
        this.DUMMY = i6 + 1;
        this.TEST_FAIL = i6;
        this.mIsReverse = false;
        this.mAudioPath = new String[]{"spk1", "spk2", "spk3", "spk4", "spk", "off"};
        this.mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == SpeakerTest.this.FINISH_TEST) {
                    SpeakerTest.this.finish();
                } else if (msg.what == SpeakerTest.this.TEST_FAIL) {
                    LtUtil.log_d(SpeakerTest.CLASS_NAME, "handleMessage", "Fail - User CLICK fail");
                    SpeakerTest.this.finish();
                } else {
                    SpeakerTest.this.ChangeBtn(true, msg.what);
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        LtUtil.log_i(CLASS_NAME, "onCreate", null);
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.speaker4_test);
        this.mAudioManager = (AudioManager) getSystemService("audio");
        preVolume = this.mAudioManager.getStreamVolume(3);
        initUI();
        LtUtil.log_d(CLASS_NAME, "onCreate", "Fix Rotation for SPK4 model");
        this.mAudioManager.setParameters("display_rotation=0");
    }

    private void initUI() {
        LtUtil.log_i(CLASS_NAME, "initUI", null);
        this.txt_msg = (TextView) findViewById(C0268R.C0269id.test_msg);
        this.txt_title = (TextView) findViewById(C0268R.C0269id.title);
        this.txt_upperL = (TextView) findViewById(C0268R.C0269id.txt_upperL);
        this.txt_upperR = (TextView) findViewById(C0268R.C0269id.txt_upperR);
        this.txt_lowerL = (TextView) findViewById(C0268R.C0269id.txt_lowerL);
        this.txt_lowerR = (TextView) findViewById(C0268R.C0269id.txt_lowerR);
        this.button_upperSPKL = (Button) findViewById(C0268R.C0269id.button_upperSPKL);
        this.button_upperSPKR = (Button) findViewById(C0268R.C0269id.button_upperSPKR);
        this.button_lowerSPKL = (Button) findViewById(C0268R.C0269id.button_lowerSPKL);
        this.button_lowerSPKR = (Button) findViewById(C0268R.C0269id.button_lowerSPKR);
        this.button_fail = (Button) findViewById(C0268R.C0269id.button_fail);
        this.button_upperSPKL.setOnClickListener(this);
        this.button_upperSPKR.setOnClickListener(this);
        this.button_lowerSPKL.setOnClickListener(this);
        this.button_lowerSPKR.setOnClickListener(this);
        this.button_fail.setOnClickListener(this);
        this.txt_title.setText("Speaker Test");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        setLoopbackOff();
        setAudioPath(AUDIO_PATH_SPK1);
        setStreamMusicVolumeMax();
        ChangeBtn(false, 0);
        TestStart();
        this.mHandler.sendEmptyMessageDelayed(this.UPPER_L, 2000);
    }

    private void TestStart() {
        LtUtil.log_i(CLASS_NAME, "TestStart", "Play music");
        playSound(Values.OverTheHorizonPath);
        this.txt_msg.setText("Top SPK L Playing...");
    }

    public void onPause() {
        super.onPause();
        stopSound();
        setAudioPath(AUDIO_PATH_OFF);
        this.mAudioManager.setStreamVolume(3, preVolume, 0);
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onClick(View v) {
        stopSound();
        ChangeBtn(false, 0);
        switch (v.getId()) {
            case C0268R.C0269id.button_upperSPKL /*2131296635*/:
                LtUtil.log_i(CLASS_NAME, "onClick", "upperSPKL pass");
                this.button_upperSPKL.setTextColor(-1);
                this.button_upperSPKL.setBackgroundColor(-16776961);
                setAudioPath(AUDIO_PATH_SPK2);
                playSound(Values.OverTheHorizonPath);
                this.txt_msg.setText("Top SPK R Playing...");
                this.mHandler.sendEmptyMessageDelayed(this.UPPER_R, 2000);
                return;
            case C0268R.C0269id.button_upperSPKR /*2131296637*/:
                LtUtil.log_i(CLASS_NAME, "onClick", "upperSPKR pass");
                this.button_upperSPKR.setTextColor(-1);
                this.button_upperSPKR.setBackgroundColor(-16776961);
                setAudioPath(AUDIO_PATH_SPK3);
                playSound(Values.OverTheHorizonPath);
                this.txt_msg.setText("Bottom SPK R Playing...");
                this.mHandler.sendEmptyMessageDelayed(this.LOWER_R, 2000);
                return;
            case C0268R.C0269id.button_fail /*2131296640*/:
                LtUtil.log_i(CLASS_NAME, "onClick", "Test fail");
                this.txt_msg.setText("FAIL");
                this.txt_msg.setTextColor(-65536);
                this.mHandler.sendEmptyMessageDelayed(this.TEST_FAIL, 1000);
                return;
            case C0268R.C0269id.button_lowerSPKL /*2131296643*/:
                LtUtil.log_i(CLASS_NAME, "onClick", "lowerSPKL pass");
                this.button_lowerSPKL.setTextColor(-1);
                this.button_lowerSPKL.setBackgroundColor(-16776961);
                this.txt_msg.setText("PASS");
                this.txt_msg.setTextColor(-16776961);
                this.mHandler.sendEmptyMessageDelayed(this.FINISH_TEST, 1000);
                return;
            case C0268R.C0269id.button_lowerSPKR /*2131296645*/:
                LtUtil.log_i(CLASS_NAME, "onClick", "lowerSPKR pass");
                this.button_lowerSPKR.setTextColor(-1);
                this.button_lowerSPKR.setBackgroundColor(-16776961);
                setAudioPath(AUDIO_PATH_SPK4);
                playSound(Values.OverTheHorizonPath);
                this.txt_msg.setText("Bottom SPK L Playing...");
                this.mHandler.sendEmptyMessageDelayed(this.LOWER_L, 2000);
                return;
            default:
                LtUtil.log_i(CLASS_NAME, "onClick", "Default");
                return;
        }
    }

    private void playSound(String path) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        if (new File(path).exists()) {
            this.mMediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(path));
        } else {
            this.mMediaPlayer = MediaPlayer.create(getApplicationContext(), C0268R.raw.over_the_horizon);
        }
        this.mMediaPlayer.setLooping(true);
        this.mMediaPlayer.start();
    }

    private void stopSound() {
        if (this.mMediaPlayer != null && this.mMediaPlayer.isPlaying()) {
            this.mMediaPlayer.stop();
            try {
                LtUtil.log_d(CLASS_NAME, "stopMedia : delay", " : 50ms");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    /* access modifiers changed from: private */
    public void ChangeBtn(boolean enable, int step) {
        if (enable) {
            this.button_fail.setEnabled(true);
            this.button_fail.setTextColor(-65536);
            if (step == this.UPPER_L) {
                this.button_upperSPKL.setEnabled(true);
                this.button_upperSPKL.setTextColor(-16776961);
            } else if (step == this.UPPER_R) {
                this.button_upperSPKR.setEnabled(true);
                this.button_upperSPKR.setTextColor(-16776961);
            } else if (step == this.LOWER_R) {
                this.button_lowerSPKR.setEnabled(true);
                this.button_lowerSPKR.setTextColor(-16776961);
            } else if (step == this.LOWER_L) {
                this.button_lowerSPKL.setEnabled(true);
                this.button_lowerSPKL.setTextColor(-16776961);
            }
        } else {
            this.button_upperSPKL.setEnabled(false);
            this.button_upperSPKR.setEnabled(false);
            this.button_lowerSPKL.setEnabled(false);
            this.button_lowerSPKR.setEnabled(false);
            this.button_fail.setEnabled(false);
            this.button_fail.setTextColor(-16777216);
        }
    }

    private void setLoopbackOff() {
        LtUtil.log_i(CLASS_NAME, "setLoopbackOff", "Loopback Off");
        this.mAudioManager.setParameters("factory_test_loopback=off");
    }

    private void setAudioPath(int path) {
        int routePath = path < AUDIO_PATH_SPK ? AUDIO_PATH_SPK : path;
        StringBuilder sb = new StringBuilder();
        sb.append("setAudioPath : factory_test_route=");
        sb.append(this.mAudioPath[routePath]);
        sb.append(" factory_test_spkpath=");
        sb.append(this.mAudioPath[path]);
        LtUtil.log_i(CLASS_NAME, "setMode", sb.toString());
        AudioManager audioManager = this.mAudioManager;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("factory_test_route=");
        sb2.append(this.mAudioPath[routePath]);
        sb2.append(";factory_test_spkpath=");
        sb2.append(this.mAudioPath[path]);
        sb2.append(";");
        audioManager.setParameters(sb2.toString());
    }

    private void setStreamMusicVolumeMax() {
        LtUtil.log_i(CLASS_NAME, "setStreamMusicVolumeMax", "set volume max");
        this.mAudioManager.setStreamVolume(3, this.mAudioManager.getStreamMaxVolume(3), 0);
    }
}
