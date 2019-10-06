package com.sec.android.app.hwmoduletest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import com.samsung.android.sdk.dualscreen.SDualScreenActivity;
import com.samsung.android.sdk.dualscreen.SDualScreenActivity.DualScreen;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;

public class ColorTest extends BaseActivity {
    private static final String HWMODULE_COLORTEST_END = "android.intent.action.HWMODULE_COLORTEST_END";
    public static boolean mTestEnd;
    private BroadcastReceiver testEndReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ColorTest.HWMODULE_COLORTEST_END)) {
                ColorTest.this.finish();
            }
        }
    };

    public ColorTest() {
        super("ColorTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mIsDualScreenFeatureEnabled) {
            mTestEnd = false;
            registerReceiver(this.testEndReceiver, new IntentFilter(HWMODULE_COLORTEST_END));
        }
        int color = getIntent().getIntExtra("color", 0);
        StringBuilder sb = new StringBuilder();
        sb.append("Color=");
        sb.append(color);
        LtUtil.log_d(this.CLASS_NAME, "onCreate", sb.toString());
        View view = new View(this);
        view.setBackgroundColor(color);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        setContentView(view);
        if (mIsDualScreenFeatureEnabled && !getIntent().getBooleanExtra(BaseActivity.SECOND_ACTIVITY, false)) {
            LtUtil.log_d(this.CLASS_NAME, "onCreate", "start Second Activity");
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setClass(getBaseContext(), getClass());
            intent.putExtra(BaseActivity.SECOND_ACTIVITY, true);
            intent.putExtra("color", color);
            SDualScreenActivity.makeIntent(this, intent, DualScreen.SUB, SDualScreenActivity.FLAG_COUPLED_TASK_EXPAND_MODE);
            startActivityForResult(intent, color);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (!mIsDualScreenFeatureEnabled) {
            return;
        }
        if (!mTestEnd) {
            mTestEnd = true;
            sendBroadcast(new Intent(HWMODULE_COLORTEST_END));
            return;
        }
        unregisterReceiver(this.testEndReceiver);
    }

    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
