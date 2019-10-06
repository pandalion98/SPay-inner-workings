package com.sec.android.app.hwmoduletest.support;

import android.app.Activity;
import android.os.Bundle;
import android.os.FactoryTest;
import android.view.KeyEvent;
import com.sec.android.app.hwmoduletest.modules.ModulePower;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.XMLDataStorage;

public class BaseActivity extends Activity {
    private static int KEYCODE_USER = 228;
    public static final int NV_O_ACSENSOR = 410;
    public static final int NV_O_FINGERPRINT = 409;
    public static final int NV_O_HALL_IC = 404;
    public static final int NV_O_KEY_TEST = 406;
    public static final int NV_O_LIGHT = 408;
    public static final int NV_O_MOTOR = 405;
    public static final int NV_O_MST = 417;
    public static final int NV_O_NFC = 416;
    public static final int NV_O_PROXIMITY = 407;
    public static final int NV_O_SPEN_DETECT = 403;
    public static final int NV_O_SPEN_DRAWING = 402;
    public static final int NV_O_TSP_DRAWING = 401;
    public static final String SECOND_ACTIVITY = "SECOND_ACTIVITY";
    public static boolean isOqcsbftt = false;
    private static int mBaseActivityUsingCount = 0;
    public static boolean mIsDualScreenFeatureEnabled = false;
    public static ModulePower mModulePower = null;
    private static int mSystemScreenBrightness;
    private static int mSystemScreenBrightnessMode;
    private final long BACK_KEY_EVENT_TIMELAG = 2000;
    /* access modifiers changed from: protected */
    public String CLASS_NAME = "BaseActivity";
    private boolean mIsLongPress = false;
    private long mPrevBackKeyEventTime = -1;

    public BaseActivity(String className) {
        this.CLASS_NAME = className;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        StringBuilder sb = new StringBuilder();
        sb.append("Create ");
        sb.append(this.CLASS_NAME);
        LtUtil.log_i(this.CLASS_NAME, "onCreate", sb.toString());
        super.onCreate(savedInstanceState);
        if (!XMLDataStorage.instance().wasCompletedParsing()) {
            XMLDataStorage.instance().parseXML(this);
        }
        LtUtil.setSystemKeyBlock(getComponentName(), 3);
        LtUtil.setSystemKeyBlock(getComponentName(), 26);
        LtUtil.setSystemKeyBlock(getComponentName(), 6);
        LtUtil.setSystemKeyBlock(getComponentName(), 1082);
        LtUtil.setSystemKeyBlock(getComponentName(), 187);
        LtUtil.setSystemKeyBlock(getComponentName(), KEYCODE_USER);
        LtUtil.setRemoveStatusBar(getWindow());
        LtUtil.disableDisplayCutOutMode(getWindow(), true);
        mModulePower = mModulePower == null ? ModulePower.instance(this) : mModulePower;
        if (Feature.getBoolean(Feature.FORCE_LANDSCAPE_MODE, false)) {
            setRequestedOrientation(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        StringBuilder sb = new StringBuilder();
        sb.append("RestoreInstanceState ");
        sb.append(this.CLASS_NAME);
        LtUtil.log_i(this.CLASS_NAME, "onRestoreInstanceState", sb.toString());
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        LtUtil.log_i(this.CLASS_NAME, "onStart", "onStart");
        if (mBaseActivityUsingCount == 0) {
            mSystemScreenBrightness = mModulePower.getBrightness();
            mSystemScreenBrightnessMode = mModulePower.getScreenBrightnessMode();
            if (FactoryTest.setRunningFactoryApp(this, true)) {
                LtUtil.log_i(this.CLASS_NAME, "onStart", "setRunningFactoryApp true");
            }
        }
        mBaseActivityUsingCount++;
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        StringBuilder sb = new StringBuilder();
        sb.append("Resume ");
        sb.append(this.CLASS_NAME);
        LtUtil.log_i(this.CLASS_NAME, "onResume", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("isRunningFactoryApp = ");
        sb2.append(FactoryTest.isRunningFactoryApp());
        LtUtil.log_i(this.CLASS_NAME, "onResume", sb2.toString());
        setBrightnessMode();
        setBrightness();
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pause ");
        sb.append(this.CLASS_NAME);
        LtUtil.log_i(this.CLASS_NAME, "onPause", sb.toString());
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        LtUtil.log_i(this.CLASS_NAME, "onStop", "onStop");
        mBaseActivityUsingCount--;
        if (mBaseActivityUsingCount == 0) {
            mModulePower.setBrightness(mSystemScreenBrightness);
            mModulePower.setScreenBrightnessMode(mSystemScreenBrightnessMode);
            if ((!FactoryTest.isFactoryBinary() || !Feature.getBoolean(Feature.SUPPORT_3X4_KEY)) && FactoryTest.setRunningFactoryApp(this, false)) {
                LtUtil.log_i(this.CLASS_NAME, "onStop", "setRunningFactoryApp false");
            }
        }
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        StringBuilder sb = new StringBuilder();
        sb.append("Destroy ");
        sb.append(this.CLASS_NAME);
        LtUtil.log_i(this.CLASS_NAME, "onDestroy", sb.toString());
        super.onDestroy();
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        this.mIsLongPress = false;
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!this.mIsLongPress) {
            this.mIsLongPress = event.isLongPress();
        }
        if (!this.mIsLongPress) {
            if (keyCode != 4) {
                this.mPrevBackKeyEventTime = -1;
            } else {
                if (this.mPrevBackKeyEventTime == -1) {
                    this.mPrevBackKeyEventTime = event.getEventTime();
                } else if (event.getEventTime() - this.mPrevBackKeyEventTime < 2000) {
                    finish();
                } else {
                    this.mPrevBackKeyEventTime = event.getEventTime();
                }
                return true;
            }
        }
        return true;
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    public void setBrightness() {
        mModulePower.setBrightness(255);
    }

    public void setBrightnessMode() {
        mModulePower.setScreenBrightnessMode(0);
    }

    public void unSetBrightnessMode() {
        mModulePower.setScreenBrightnessMode(1);
    }
}
