package com.sec.android.app.hwmoduletest;

import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.System;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.HwTestMenu;

public class SPenKeyTest extends BaseActivity {
    private static AudioManager mAudioManger = null;
    private static int preVolume = 0;
    private boolean backPressPass = false;
    private boolean backReleasePass = false;
    private boolean isBackKeyPressed = false;
    private boolean isMenuKeyPressed = false;
    protected OnClickListener mExitButtonAction = new OnClickListener() {
        public void onClick(View view) {
            SPenKeyTest.this.setResult(0);
            SPenKeyTest.this.finish();
        }
    };
    private Handler mHandler = new Handler();
    private boolean mIsPassed = false;
    private MediaPlayer mMediaPlayer;
    private int mMultiWindowEnabled = 0;
    private TextView mPressBack;
    private TextView mPressMenu;
    private TextView mReleaseBack;
    private TextView mReleaseMenu;
    private boolean menuPressPass = false;
    private boolean menuReleasePass = false;

    public SPenKeyTest() {
        super("SPenKeyTest");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LtUtil.log_i(this.CLASS_NAME, "onCreate", "");
        mAudioManger = (AudioManager) getSystemService("audio");
        this.mMultiWindowEnabled = System.getInt(getContentResolver(), "multi_window_enabled", 0);
        if (this.mMultiWindowEnabled == 1) {
            System.putInt(getContentResolver(), "multi_window_enabled", 0);
        }
        try {
            if (HwTestMenu.getTestCase(String.valueOf(35)).contains("VERTICAL")) {
                setContentView(C0268R.layout.ui_spenkeytest_vertical);
            } else {
                setContentView(C0268R.layout.ui_spenkeytest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();
    }

    public void onResume() {
        super.onResume();
        LtUtil.log_i(this.CLASS_NAME, "onResume", "");
        preVolume = mAudioManger.getStreamVolume(3);
        StringBuilder sb = new StringBuilder();
        sb.append("preVolume = ");
        sb.append(preVolume);
        LtUtil.log_i(this.CLASS_NAME, "onResume", sb.toString());
        mAudioManger.setStreamVolume(3, mAudioManger.getStreamMaxVolume(3), 0);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mMultiWindowEnabled == 1) {
            System.putInt(getContentResolver(), "multi_window_enabled", 1);
        }
    }

    public void onPause() {
        super.onPause();
        mAudioManger.setStreamVolume(3, preVolume, 0);
        if (this.mMultiWindowEnabled == 1) {
            System.putInt(getContentResolver(), "multi_window_enabled", 1);
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("passed=");
        sb.append(this.mIsPassed);
        sb.append(", keyCode=");
        sb.append(keyCode);
        LtUtil.log_i(this.CLASS_NAME, "onKeyUp", sb.toString());
        if (keyCode == 82 || keyCode == 187) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("setMenuRelease: isMenuKeyPressed : ");
            sb2.append(this.isMenuKeyPressed);
            LtUtil.log_i(this.CLASS_NAME, "onKeyUp", sb2.toString());
            if (isTouchPenKeyEvent(event) && !this.mIsPassed) {
                setMenuRelease();
                this.isMenuKeyPressed = false;
                LtUtil.log_i(this.CLASS_NAME, "onKeyUp", "setMenuRelease: isMenuKeyPressed = false");
            }
        } else if (keyCode == 4) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("setBackRelease: isBackKeyPressed : ");
            sb3.append(this.isBackKeyPressed);
            LtUtil.log_i(this.CLASS_NAME, "onKeyUp", sb3.toString());
            if (isTouchPenKeyEvent(event) && !this.mIsPassed) {
                setBackRelease();
                this.isBackKeyPressed = false;
                LtUtil.log_i(this.CLASS_NAME, "onKeyUp", "setBackRelease: isBackKeyPressed = false");
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("keyCode=");
        sb.append(keyCode);
        LtUtil.log_i(this.CLASS_NAME, "onKeyDown", sb.toString());
        if (keyCode == 25 || keyCode == 24) {
            finish();
            return super.onKeyDown(keyCode, event);
        } else if (keyCode == 3) {
            return true;
        } else {
            if (keyCode == 4) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("setBackPress: isBackKeyPressed : ");
                sb2.append(this.isBackKeyPressed);
                LtUtil.log_i(this.CLASS_NAME, "onKeyDown", sb2.toString());
                if (isTouchPenKeyEvent(event) && !this.mIsPassed && !this.isBackKeyPressed) {
                    this.isBackKeyPressed = true;
                    setBackPress();
                    LtUtil.log_i(this.CLASS_NAME, "onKeyDown", "setBackPress: isBackKeyPressed = true");
                }
                return true;
            } else if (keyCode != 82 && keyCode != 187) {
                return super.onKeyDown(keyCode, event);
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("setMenuPress: isMenuKeyPressed : ");
                sb3.append(this.isMenuKeyPressed);
                LtUtil.log_i(this.CLASS_NAME, "onKeyDown", sb3.toString());
                if (isTouchPenKeyEvent(event) && !this.mIsPassed && !this.isMenuKeyPressed) {
                    this.isMenuKeyPressed = true;
                    setMenuPress();
                    LtUtil.log_i(this.CLASS_NAME, "onKeyDown", "setMenuPress: setMenuPress = true");
                }
                return true;
            }
        }
    }

    private void setMenuPress() {
        playMedia(C0268R.raw.button, false);
        if (!this.menuPressPass) {
            this.mPressMenu.setTextColor(-16776961);
            this.menuPressPass = true;
        } else if (this.menuPressPass) {
            this.mPressMenu.setTextColor(-65536);
            this.menuPressPass = false;
        }
        checkingAllPassItems();
    }

    private void setBackPress() {
        playMedia(C0268R.raw.button, false);
        if (!this.backPressPass) {
            this.mPressBack.setTextColor(-16776961);
            this.backPressPass = true;
        } else if (this.backPressPass) {
            this.mPressBack.setTextColor(-65536);
            this.backPressPass = false;
        }
        checkingAllPassItems();
    }

    private void setMenuRelease() {
        playMedia(C0268R.raw.button, false);
        if (!this.menuReleasePass) {
            this.mReleaseMenu.setTextColor(-16776961);
            this.menuReleasePass = true;
        } else if (this.menuReleasePass) {
            this.mReleaseMenu.setTextColor(-65536);
            this.menuReleasePass = false;
        }
        checkingAllPassItems();
    }

    private void setBackRelease() {
        playMedia(C0268R.raw.button, false);
        if (!this.backReleasePass) {
            this.mReleaseBack.setTextColor(-16776961);
            this.backReleasePass = true;
        } else if (this.backReleasePass) {
            this.mReleaseBack.setTextColor(-65536);
            this.backReleasePass = false;
        }
        checkingAllPassItems();
    }

    private void initialize() {
        ((Button) findViewById(C0268R.C0269id.exit)).setOnClickListener(this.mExitButtonAction);
        this.mPressMenu = (TextView) findViewById(C0268R.C0269id.spen_menu_press);
        this.mPressBack = (TextView) findViewById(C0268R.C0269id.spen_back_press);
        this.mReleaseMenu = (TextView) findViewById(C0268R.C0269id.spen_menu_release);
        this.mReleaseBack = (TextView) findViewById(C0268R.C0269id.spen_back_release);
    }

    public void checkingAllPassItems() {
        if (this.menuPressPass && this.backPressPass && this.menuReleasePass && this.backReleasePass) {
            LtUtil.log_i(this.CLASS_NAME, "checkingAllPassItems", "Pass the S-Pen KeyTest");
            this.mIsPassed = true;
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    SPenKeyTest.this.finish();
                }
            }, 1000);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isTouchPenKeyEvent(KeyEvent event) {
        int deviceId = event.getDeviceId();
        InputDevice inputDevice = InputManager.getInstance().getInputDevice(deviceId);
        StringBuilder sb = new StringBuilder();
        sb.append("isTouchPenKeyEvent2: deviceId = ");
        sb.append(deviceId);
        sb.append(", inputDevice= ");
        sb.append(inputDevice);
        LtUtil.log_i(this.CLASS_NAME, "onKeyEvent", sb.toString());
        if (inputDevice == null || !inputDevice.getName().contains("sec_e-pen")) {
            return false;
        }
        return true;
    }

    public void playMedia(int resId, boolean isLoop) {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        this.mMediaPlayer = MediaPlayer.create(this, resId);
        this.mMediaPlayer.setLooping(isLoop);
        this.mMediaPlayer.start();
    }
}
