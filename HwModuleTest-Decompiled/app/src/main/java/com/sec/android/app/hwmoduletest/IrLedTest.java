package com.sec.android.app.hwmoduletest;

import android.hardware.ConsumerIrManager;
import android.hardware.ConsumerIrManager.CarrierFrequencyRange;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class IrLedTest extends BaseActivity implements OnClickListener {
    private static final String CLASS_NAME = "IrEDTest";
    private static final int INTERVAL = 800;
    private static final String IrED_SYSFS_PATH = Kernel.getFilePath(Kernel.IR_LED_SEND);
    private static final String IrED_SYSFS_PATH_FOR_ON_OFF = Kernel.getFilePath(Kernel.IR_LED_SEND_TEST);
    private String CHANNEL_DOWN;
    /* access modifiers changed from: private */
    public String CHANNEL_UP;
    private String OFF;

    /* renamed from: ON */
    private String f16ON;
    private String ON_OFF_REPEAT;
    private String POWER_CMD;
    private String REPEAT_VOLUME_UP_START;
    private String REPEAT_VOLUME_UP_STOP;
    private String START_CMD;
    private String UNKNOWN_CMD;
    private String VOLUME_DOWN;
    private String VOLUME_UP;
    private CarrierFrequencyRange[] freqRange;
    private ConsumerIrManager irManager = null;
    private String mConcept = null;
    /* access modifiers changed from: private */
    public int mCounter = 0;
    /* access modifiers changed from: private */
    public TextView mCounterView = null;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    private boolean mRemoteCall = false;
    /* access modifiers changed from: private */
    public Runnable mRunnable = null;
    private Timer mTimer = null;

    public IrLedTest() {
        super("IrLedTest");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.irled_test);
        LtUtil.log_i(CLASS_NAME, "onCreate", "IrLedTest onCreate");
        this.mRemoteCall = getIntent().getBooleanExtra("REMOTE_CALL", false);
        View channelUp = findViewById(C0268R.C0269id.channel_up);
        channelUp.setOnClickListener(this);
        View channelDown = findViewById(C0268R.C0269id.channel_down);
        channelDown.setOnClickListener(this);
        View volumeUp = findViewById(C0268R.C0269id.volume_up);
        volumeUp.setOnClickListener(this);
        View volumeDown = findViewById(C0268R.C0269id.volume_down);
        volumeDown.setOnClickListener(this);
        View iRLedOn = findViewById(C0268R.C0269id.button_on);
        iRLedOn.setOnClickListener(this);
        View iRLedOff = findViewById(C0268R.C0269id.button_off);
        iRLedOff.setOnClickListener(this);
        View iRLedRepeat = findViewById(C0268R.C0269id.on_off_repeat);
        iRLedRepeat.setOnClickListener(this);
        iRLedRepeat.setVisibility(8);
        this.mConcept = Feature.getString(Feature.IRLED_CONCEPT);
        if ("PEEL".equals(this.mConcept) || "NEWTIME".equals(this.mConcept)) {
            this.f16ON = "38000,4499,4499,578,1683,578,1683,578,1683,578,552,578,552,578,552,578,552,578,552,578,1683,578,1683,578,1683,578,552,578,552,578,552,578,552,578,552,578,1683,578,552,578,552,578,1683,578,1683,578,552,578,552,578,1683,578,552,578,1683,578,1683,578,552,578,552,578,1683,578,1683,578,552,578,23047";
            this.OFF = "38000,4499,4499,578,1683,578,1683,578,1683,578,552,578,552,578,552,578,552,578,552,578,1683,578,1683,578,1683,578,552,578,552,578,552,578,552,578,552,578,552,578,552,578,552,578,1683,578,1683,578,552,578,552,578,1683,578,1683,578,1683,578,1683,578,552,578,552,578,1683,578,1683,578,552,578,23047";
            this.VOLUME_DOWN = "38000,4499,4499,578,1683,578,1683,578,1683,578,552,578,552,578,552,578,552,578,552,578,1683,578,1683,578,1683,578,552,578,552,578,552,578,552,578,552,578,1683,578,1683,578,552,578,1683,578,552,578,552,578,552,578,552,578,552,578,552,578,1683,578,552,578,1683,578,1683,578,1683,578,1683,578,23047";
            this.VOLUME_UP = "38000,4499,4499,578,1683,578,1683,578,1683,578,552,578,552,578,552,578,552,578,552,578,1683,578,1683,578,1683,578,552,578,552,578,552,578,552,578,552,578,1683,578,1683,578,1683,578,552,578,552,578,552,578,552,578,552,578,552,578,552,578,552,578,1683,578,1683,578,1683,578,1683,578,1683,578,23047";
            this.CHANNEL_DOWN = "38000,4499,4499,578,1683,578,1683,578,1683,578,552,578,552,578,552,578,552,578,552,578,1683,578,1683,578,1683,578,552,578,552,578,552,578,552,578,552,578,552,578,552,578,552,578,552,578,1683,578,552,578,552,578,552,578,1683,578,1683,578,1683,578,1683,578,552,578,1683,578,1683,578,1683,578,23047";
            this.CHANNEL_UP = "38000,4499,4499,578,1683,578,1683,578,1683,578,552,578,552,578,552,578,552,578,552,578,1683,578,1683,578,1683,578,552,578,552,578,552,578,552,578,552,578,552,578,1683,578,552,578,552,578,1683,578,552,578,552,578,552,578,1683,578,552,578,1683,578,1683,578,552,578,1683,578,1683,578,1683,578,23047";
        } else if ("HESTIA".equals(this.mConcept)) {
            this.f16ON = "38400,4531,4479,625,1588,625,1614,625,1588,625,442,651,442,625,442,625,442,625,468,625,1588,625,1614,625,1588,625,468,625,442,625,442,625,468,625,442,625,442,625,1614,625,442,625,442,625,468,625,442,625,442,625,468,625,1588,625,442,625,1614,625,1588,651,1588,625,1614,625,1588,625,1614,625,48932";
            this.OFF = "38400,4531,4479,625,1588,625,1614,625,1588,625,442,651,442,625,442,625,442,625,468,625,1588,625,1614,625,1588,625,468,625,442,625,442,625,468,625,442,625,442,625,1614,625,442,625,442,625,468,625,442,625,442,625,468,625,1588,625,442,625,1614,625,1588,651,1588,625,1614,625,1588,625,1614,625,48932";
            this.VOLUME_DOWN = "38000,4526,4447,578,1631,578,1631,578,1631,578,526,578,526,578,526,578,526,578,526,578,1631,578,1631,578,1631,578,526,578,526,578,526,578,526,578,526,578,1631,578,1631,578,526,578,1631,578,526,578,526,578,526,578,526,578,526,578,526,578,1631,578,526,578,1631,578,1631,578,1631,578,1631,552,46447";
            this.VOLUME_UP = "38000,4526,4447,578,1631,578,1631,578,1631,578,500,578,500,578,500,578,500,578,500,578,1631,578,1631,578,1631,578,500,578,500,578,500,578,500,578,500,578,1631,578,1631,578,1631,578,500,578,500,578,500,578,500,578,500,578,500,578,500,578,500,578,1631,578,1631,578,1631,578,1631,578,1631,578,46447";
            this.CHANNEL_DOWN = "38000,4526,4526,578,1657,578,1657,578,1657,578,526,578,526,578,526,578,526,578,526,578,1657,578,1657,578,1657,578,526,578,526,578,526,578,526,578,526,578,526,578,526,578,526,578,526,578,1657,578,526,578,526,578,526,578,1657,578,1657,578,1657,578,1657,578,526,578,1657,578,1657,578,1657,578,46736";
            this.CHANNEL_UP = "38000,4526,4447,578,1657,578,1657,578,1657,552,500,552,500,552,500,552,500,552,500,578,1657,578,1657,578,1657,552,500,552,500,552,500,552,500,552,500,552,500,578,1657,552,500,552,500,578,1657,552,500,552,500,552,500,578,1657,552,500,578,1657,578,1657,552,500,578,1657,578,1657,578,1657,578,46447";
        } else {
            this.f16ON = "38400,10";
            this.OFF = "38400,5";
            this.ON_OFF_REPEAT = "38400,10,5,10,5,10,5,10,5,10,5,10,5,10,5,10,5,10,5,10,5";
            this.VOLUME_DOWN = "38400,173,171,24,62,24,61,24,62,24,17,24,17,24,18,24,17,24,18,23,62,24,61,24,62,24,18,23,17,25,17,24,17,24,17,24,62,24,61,25,17,24,61,24,18,24,17,24,17,24,18,24,17,24,17,24,62,24,17,24,62,24,61,24,62,24,61,24,1880,0";
            this.VOLUME_UP = "38400,173,171,24,62,24,61,24,62,24,17,24,17,24,18,24,17,24,19,22,62,24,61,24,62,24,19,22,17,25,17,24,17,24,17,24,62,24,61,25,61,24,17,24,19,23,17,24,17,24,20,22,17,24,17,24,17,25,61,24,62,24,61,24,62,24,61,24,1880,0";
            this.CHANNEL_DOWN = "38400,173,171,24,62,24,61,24,62,24,17,24,17,24,18,24,17,24,18,23,62,24,61,24,62,24,18,23,17,25,17,24,17,24,17,24,19,23,17,24,17,24,18,24,61,24,17,25,17,24,17,24,62,24,61,24,62,24,61,24,19,23,61,24,62,24,61,24,1880,0";
            this.CHANNEL_UP = "38400,173,171,24,62,24,61,24,62,24,17,24,17,24,18,24,17,24,18,23,62,24,61,24,62,24,17,24,17,25,17,24,17,24,17,24,19,23,61,24,18,24,17,24,61,24,19,23,17,24,17,24,62,24,17,24,62,24,61,24,19,23,61,24,62,24,61,24,1880,0";
            this.START_CMD = "38400";
            this.POWER_CMD = ",174,172,24,61,24,62,24,61,24,17,25,17,24,17,24,17,24,18,24,61,24,62,24,61,24,18,24,17,24,17,24,18,24,17,24,17,24,62,24,17,24,17,24,18,24,17,24,17,24,18,24,61,24,17,24,62,24,61,25,61,24,62,24,61,24,62,24,1879";
        }
        if ("PEEL".equals(this.mConcept) || "NEWTIME".equals(this.mConcept)) {
            this.irManager = (ConsumerIrManager) getSystemService("consumer_ir");
            this.freqRange = this.irManager.getCarrierFrequencies();
        }
        if (this.mRemoteCall) {
            LtUtil.log_i(CLASS_NAME, "onCreate", "mRemoteCall == true");
            findViewById(C0268R.C0269id.layout2).setVisibility(0);
            View clrButton = findViewById(C0268R.C0269id.clr_button);
            clrButton.setOnClickListener(this);
            clrButton.setVisibility(0);
            this.mCounterView = (TextView) findViewById(C0268R.C0269id.counter);
            this.mCounterView.setVisibility(0);
            channelUp.setVisibility(8);
            channelDown.setVisibility(8);
            volumeUp.setVisibility(8);
            volumeDown.setVisibility(8);
            iRLedOn.setVisibility(8);
            iRLedOff.setVisibility(8);
            iRLedRepeat.setVisibility(8);
            findViewById(C0268R.C0269id.channel_title).setVisibility(8);
            findViewById(C0268R.C0269id.volume_title).setVisibility(8);
            findViewById(C0268R.C0269id.onoff_title).setVisibility(8);
            this.mTimer = new Timer();
            this.mTimer.schedule(new TimerTask() {
                public void run() {
                    IrLedTest.this.mHandler.post(IrLedTest.this.mRunnable = new Runnable() {
                        public void run() {
                            try {
                                IrLedTest.this.controlIrED(IrLedTest.this.CHANNEL_UP);
                                IrLedTest.this.mCounter = IrLedTest.this.mCounter + 1;
                                IrLedTest.this.mCounterView.setText(Integer.toString(IrLedTest.this.mCounter));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }, 0, 800);
        }
    }

    public void finishOperation() {
        if (!(this.mRunnable == null || this.mHandler == null)) {
            this.mHandler.removeCallbacks(this.mRunnable);
            this.mRunnable = null;
        }
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0268R.C0269id.clr_button /*2131296468*/:
                LtUtil.log_i(CLASS_NAME, "onClick", "Finished IR LED Test in 15 mode");
                setResult(-1);
                finish();
                return;
            case C0268R.C0269id.channel_up /*2131296471*/:
                LtUtil.log_i(CLASS_NAME, "onClick", "channel_up");
                try {
                    controlIrED(this.CHANNEL_UP);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            case C0268R.C0269id.channel_down /*2131296472*/:
                LtUtil.log_i(CLASS_NAME, "onClick", "channel_down");
                try {
                    controlIrED(this.CHANNEL_DOWN);
                    return;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return;
                }
            case C0268R.C0269id.volume_up /*2131296474*/:
                LtUtil.log_i(CLASS_NAME, "onClick", "volume_up");
                try {
                    controlIrED(this.VOLUME_UP);
                    return;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    return;
                }
            case C0268R.C0269id.volume_down /*2131296475*/:
                LtUtil.log_i(CLASS_NAME, "onClick", "volume_down");
                try {
                    controlIrED(this.VOLUME_DOWN);
                    return;
                } catch (IOException e4) {
                    e4.printStackTrace();
                    return;
                }
            case C0268R.C0269id.button_on /*2131296477*/:
                LtUtil.log_i(CLASS_NAME, "onClick", "button_on");
                StringBuilder sb = new StringBuilder();
                sb.append("IRLED_CONCEPT : ");
                sb.append(this.mConcept);
                LtUtil.log_i(CLASS_NAME, "onClick", sb.toString());
                try {
                    if ("NEW".equals(this.mConcept)) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(this.START_CMD);
                        sb2.append(this.POWER_CMD);
                        controlIrED(sb2.toString());
                        return;
                    }
                    if (!"PEEL".equals(this.mConcept) && !"NEWTIME".equals(this.mConcept)) {
                        if (!"HESTIA".equals(this.mConcept)) {
                            onoffIrED(this.f16ON);
                            return;
                        }
                    }
                    controlIrED(this.f16ON);
                    return;
                } catch (IOException e5) {
                    e5.printStackTrace();
                    return;
                }
            case C0268R.C0269id.button_off /*2131296478*/:
                LtUtil.log_i(CLASS_NAME, "onClick", "button_off");
                try {
                    if ("NEW".equals(this.mConcept)) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(this.START_CMD);
                        sb3.append(this.POWER_CMD);
                        controlIrED(sb3.toString());
                        return;
                    }
                    if (!"PEEL".equals(this.mConcept) && !"NEWTIME".equals(this.mConcept)) {
                        if (!"HESTIA".equals(this.mConcept)) {
                            onoffIrED(this.OFF);
                            return;
                        }
                    }
                    controlIrED(this.OFF);
                    return;
                } catch (IOException e6) {
                    e6.printStackTrace();
                    return;
                }
            case C0268R.C0269id.on_off_repeat /*2131296479*/:
                LtUtil.log_i(CLASS_NAME, "onClick", "on_off_repeat");
                try {
                    if ("NEW".equals(this.mConcept)) {
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(this.START_CMD);
                        sb4.append(this.POWER_CMD);
                        sb4.append(this.POWER_CMD);
                        sb4.append(this.POWER_CMD);
                        sb4.append(this.POWER_CMD);
                        sb4.append(this.POWER_CMD);
                        sb4.append(this.POWER_CMD);
                        sb4.append(this.POWER_CMD);
                        sb4.append(this.POWER_CMD);
                        sb4.append(this.POWER_CMD);
                        sb4.append(this.POWER_CMD);
                        controlIrED(sb4.toString());
                        return;
                    }
                    onoffIrED(this.ON_OFF_REPEAT);
                    return;
                } catch (IOException e7) {
                    e7.printStackTrace();
                    return;
                }
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        finishOperation();
    }

    public void onDestroy() {
        finishOperation();
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void controlIrED(String control) throws IOException {
        if ("PEEL".equals(this.mConcept) || "NEWTIME".equals(this.mConcept)) {
            this.irManager.transmit(38000, convertPattern(control));
        } else {
            FileOutputStream out = new FileOutputStream(IrED_SYSFS_PATH);
            try {
                out.write(control.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            } catch (Throwable th) {
                out.close();
                throw th;
            }
            out.close();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("controlIrED - write bytes : ");
        sb.append(control);
        LtUtil.log_i(CLASS_NAME, "controlIrED", sb.toString());
    }

    private void onoffIrED(String control) throws IOException {
        byte[] data = control.getBytes();
        StringBuilder sb = new StringBuilder();
        sb.append("onoffIrED - write bytes : ");
        sb.append(control);
        LtUtil.log_i(CLASS_NAME, "onoffIrED", sb.toString());
        FileOutputStream out = new FileOutputStream(IrED_SYSFS_PATH_FOR_ON_OFF);
        try {
            out.write(data);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            out.close();
            throw th;
        }
        out.close();
    }

    private int[] convertPattern(String pattern) {
        String[] arr = pattern.split(",");
        int[] converted = new int[(arr.length - 1)];
        for (int i = 0; i < arr.length - 1; i++) {
            converted[i] = Integer.parseInt(arr[i + 1]);
        }
        return converted;
    }
}
