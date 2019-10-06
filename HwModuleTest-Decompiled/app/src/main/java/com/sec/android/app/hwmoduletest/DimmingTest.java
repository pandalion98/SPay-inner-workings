package com.sec.android.app.hwmoduletest;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings.System;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.samsung.android.sdk.dualscreen.SDualScreenActivity;
import com.samsung.android.sdk.dualscreen.SDualScreenActivity.DualScreen;
import com.sec.android.app.hwmoduletest.modules.ModulePower;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;

public class DimmingTest extends BaseActivity {
    private boolean isDimming = false;
    private PowerManager mPowerManager = null;
    Resources resources = null;

    public DimmingTest() {
        super("DimmingTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.resources = getApplicationContext().getResources();
        View view = new View(this);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        view.setBackgroundResource(C0268R.drawable.rgb_pattern);
        setContentView(view);
        if (mIsDualScreenFeatureEnabled && !getIntent().getBooleanExtra(BaseActivity.SECOND_ACTIVITY, false)) {
            LtUtil.log_d(this.CLASS_NAME, "onCreate", "start Second Activity");
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setClass(getBaseContext(), getClass());
            intent.putExtra(BaseActivity.SECOND_ACTIVITY, true);
            SDualScreenActivity.makeIntent(this, intent, DualScreen.SUB, SDualScreenActivity.FLAG_COUPLED_TASK_EXPAND_MODE);
            startActivityForResult(intent, 1);
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        int brightness;
        if (event.getAction() == 0) {
            if (this.isDimming) {
                brightness = System.getInt(getContentResolver(), "screen_brightness", 255);
            } else {
                brightness = this.resources.getInteger(17694900);
            }
            this.isDimming = !this.isDimming;
            setBrightness(brightness);
            LtUtil.log_d(this.CLASS_NAME, "onTouchEvent", this.isDimming ? "DIM" : "NO DIM");
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        setBrightness(System.getInt(getContentResolver(), "screen_brightness", 255));
        super.onPause();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            return super.onKeyDown(keyCode, event);
        }
        if (keyCode == 24) {
            finish();
        }
        return true;
    }

    private void setBrightness(int brightness) {
        ModulePower.instance(getApplicationContext()).setBrightness(brightness);
    }
}
