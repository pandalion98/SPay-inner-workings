package com.sec.android.app.hwmoduletest;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.xmldata.support.Support.Kernel;
import egis.client.api.EgisFingerprint;

public class SvcLedTest extends BaseActivity implements OnClickListener {
    private final int LED_BLUE = 2;
    private final String LED_LOW_POWER_MODE_OFF = "0";
    private final String LED_LOW_POWER_MODE_ON = EgisFingerprint.MAJOR_VERSION;
    private final int LED_MAGENTA = 3;
    private final int LED_RED = 1;
    private Button _buttonBlue;
    private Button _buttonMagenta;
    private Button _buttonOff;
    private Button _buttonRed;

    public SvcLedTest() {
        super("SvcLedTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.svc_led_test);
        this._buttonOff = (Button) findViewById(C0268R.C0269id.svc_led_off);
        this._buttonRed = (Button) findViewById(C0268R.C0269id.svc_led_red);
        this._buttonBlue = (Button) findViewById(C0268R.C0269id.svc_led_blue);
        this._buttonMagenta = (Button) findViewById(C0268R.C0269id.svc_led_magenta);
        Kernel.write(Kernel.LED_LOWPOWER, "0");
        this._buttonOff.setOnClickListener(this);
        this._buttonRed.setOnClickListener(this);
        this._buttonBlue.setOnClickListener(this);
        this._buttonMagenta.setOnClickListener(this);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        turnOffLed();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        Kernel.write(Kernel.LED_LOWPOWER, EgisFingerprint.MAJOR_VERSION);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case C0268R.C0269id.svc_led_off /*2131296652*/:
                turnOffLed();
                return;
            case C0268R.C0269id.svc_led_red /*2131296653*/:
                turnOnLed(1);
                return;
            case C0268R.C0269id.svc_led_blue /*2131296654*/:
                turnOnLed(2);
                return;
            case C0268R.C0269id.svc_led_magenta /*2131296655*/:
                turnOnLed(3);
                return;
            default:
                return;
        }
    }

    private void turnOnLed(int color) {
        Kernel.write(Kernel.LED_RED, (color & 1) != 0 ? EgisFingerprint.MAJOR_VERSION : "0");
        Kernel.write(Kernel.LED_BLUE, (color & 2) != 0 ? EgisFingerprint.MAJOR_VERSION : "0");
    }

    private void turnOffLed() {
        Kernel.write(Kernel.LED_RED, "0");
        Kernel.write(Kernel.LED_BLUE, "0");
    }
}
