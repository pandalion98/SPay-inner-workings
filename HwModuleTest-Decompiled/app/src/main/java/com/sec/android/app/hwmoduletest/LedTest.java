package com.sec.android.app.hwmoduletest;

import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Spec;
import egis.client.api.EgisFingerprint;

public class LedTest extends BaseActivity {
    private final int COLORSTATE_BLUE = 2;
    private final int COLORSTATE_END = 3;
    private final int COLORSTATE_FINISH = 4;
    private final int COLORSTATE_GREEN = 1;
    private final int COLORSTATE_INIT = -1;
    private final int COLORSTATE_RED = 0;
    private final String LED_LOW_POWER_MODE_OFF = "0";
    private final String LED_LOW_POWER_MODE_ON = EgisFingerprint.MAJOR_VERSION;
    private int LED_ON_BRIGHTNESS = Spec.getInt(Spec.LED_POWER_MAX);
    private int nColorState = -1;
    private TextView txtcolorstate;

    public LedTest() {
        super("LedTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.led_test);
        this.txtcolorstate = (TextView) findViewById(C0268R.C0269id.colorstate);
        Kernel.write(Kernel.LED_LOWPOWER, "0");
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
        Kernel.write(Kernel.LED_LOWPOWER, EgisFingerprint.MAJOR_VERSION);
    }

    private void setcolorstate(int colorstate) {
        StringBuilder sb = new StringBuilder();
        sb.append("setcolorstate : ");
        sb.append(colorstate);
        LtUtil.log_i(this.CLASS_NAME, "setcolorState", sb.toString());
        this.nColorState = colorstate;
        switch (this.nColorState) {
            case 0:
                this.txtcolorstate.setText("RED");
                this.txtcolorstate.setTextColor(-65536);
                setLED(this.LED_ON_BRIGHTNESS, 0, 0);
                LtUtil.log_i(this.CLASS_NAME, "setcolorState", "LED_RED On!!");
                return;
            case 1:
                this.txtcolorstate.setText("GREEN");
                this.txtcolorstate.setTextColor(-16711936);
                setLED(0, this.LED_ON_BRIGHTNESS, 0);
                LtUtil.log_i(this.CLASS_NAME, "setcolorState", "LED_GREEN On!!");
                return;
            case 2:
                this.txtcolorstate.setText("BLUE");
                this.txtcolorstate.setTextColor(-16776961);
                setLED(0, 0, this.LED_ON_BRIGHTNESS);
                LtUtil.log_i(this.CLASS_NAME, "setcolorState", "LED_BLUE On!!");
                return;
            case 3:
                this.txtcolorstate.setText("END");
                this.txtcolorstate.setTextColor(-16777216);
                setLED(0, 0, 0);
                LtUtil.log_i(this.CLASS_NAME, "setcolorState", "LED Off!!");
                return;
            case 4:
                setLED(0, 0, 0);
                LtUtil.log_i(this.CLASS_NAME, "setcolorState", "FINISH!!");
                return;
            default:
                return;
        }
    }

    private void setLED(int setR, int setG, int setB) {
        StringBuilder sb = new StringBuilder();
        sb.append(" setMix : ");
        sb.append(String.format("%02X", new Object[]{Integer.valueOf(setR)}));
        sb.append(String.format("%02X", new Object[]{Integer.valueOf(setG)}));
        sb.append(String.format("%02X", new Object[]{Integer.valueOf(setB)}));
        LtUtil.log_i(this.CLASS_NAME, "setLED()", sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(String.format("%02X", new Object[]{Integer.valueOf(setR)}));
        sb2.append(String.format("%02X", new Object[]{Integer.valueOf(setG)}));
        sb2.append(String.format("%02X", new Object[]{Integer.valueOf(setB)}));
        String setMix = sb2.toString();
        if (VERSION.SDK_INT > 19) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(setMix);
            sb3.append(" 60000 0");
            setMix = sb3.toString();
            LtUtil.log_i(this.CLASS_NAME, "setLED", "VERSION.SDK_INT is higher than KITKAT");
        }
        LtUtil.log_i(this.CLASS_NAME, "setLED", setMix);
        String str = Kernel.LED_BLINK;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("0x");
        sb4.append(setMix);
        Kernel.write(str, sb4.toString());
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
