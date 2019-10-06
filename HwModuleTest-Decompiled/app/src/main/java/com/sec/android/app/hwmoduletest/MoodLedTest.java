package com.sec.android.app.hwmoduletest;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.modules.ModuleDevice;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;

public class MoodLedTest extends BaseActivity {
    private final int COLORSTATE_COLOR_1 = 0;
    private final int COLORSTATE_COLOR_2 = 1;
    private final int COLORSTATE_COLOR_3 = 2;
    private final int COLORSTATE_END = 3;
    private final int COLORSTATE_FINISH = 4;
    private final int COLORSTATE_INIT = -1;
    private View mBackground;
    private final int mColor1 = 6;
    private final int mColor2 = 7;
    private final int mColor3 = 8;
    private ModuleDevice mModuleDevice;
    private final int mOff = 0;
    private int nColorState = -1;
    private TextView txtcolorstate;

    public MoodLedTest() {
        super("MoodLedTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.mood_led_test);
        this.mModuleDevice = ModuleDevice.instance(this);
        this.txtcolorstate = (TextView) findViewById(C0268R.C0269id.colorstate);
        this.mBackground = new View(this);
        this.mBackground = this.txtcolorstate.getRootView();
        this.mBackground.setBackgroundColor(-16777216);
        setcolorstate(0);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        setcolorstate(3);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    private void setcolorstate(int colorstate) {
        StringBuilder sb = new StringBuilder();
        sb.append("setcolorstate : ");
        sb.append(colorstate);
        LtUtil.log_i(this.CLASS_NAME, "setcolorState", sb.toString());
        this.nColorState = colorstate;
        switch (this.nColorState) {
            case 0:
                this.txtcolorstate.setText("COLOR 1");
                this.txtcolorstate.setTextColor(-65536);
                this.mModuleDevice.setMoodLEDlamp(6);
                LtUtil.log_i(this.CLASS_NAME, "setcolorState", "MOODLED_UX5 Color 1 On!!");
                return;
            case 1:
                this.txtcolorstate.setText("COLOR 2");
                this.txtcolorstate.setTextColor(-16711936);
                this.mModuleDevice.setMoodLEDlamp(7);
                LtUtil.log_i(this.CLASS_NAME, "setcolorState", "MOODLED_UX5 Color 2 On!!");
                return;
            case 2:
                this.txtcolorstate.setText("COLOR 3");
                this.txtcolorstate.setTextColor(-16776961);
                this.mModuleDevice.setMoodLEDlamp(8);
                LtUtil.log_i(this.CLASS_NAME, "setcolorState", "MOODLED_UX5 Color 3 On!!");
                return;
            case 3:
                this.txtcolorstate.setText("END");
                this.txtcolorstate.setTextColor(-1);
                this.mModuleDevice.setMoodLEDlamp(0);
                LtUtil.log_i(this.CLASS_NAME, "setcolorState", "LED Off!!");
                return;
            case 4:
                LtUtil.log_i(this.CLASS_NAME, "setcolorState", "FINISH!!");
                return;
            default:
                return;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == 1) {
            setcolorstate(this.nColorState + 1);
            if (this.nColorState == 4) {
                finish();
            }
        }
        return true;
    }
}
