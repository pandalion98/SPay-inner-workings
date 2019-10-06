package com.sec.android.app.hwmoduletest;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.FactoryTest;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.modules.ModuleDevice;
import com.sec.android.app.hwmoduletest.modules.ModulePower;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Kernel;

public class SensorHubTest extends BaseActivity {
    private static final String ACTION_WAKE_UP = "com.sec.factory.WakeUp";
    public static final int FINISH_TEST = 3;
    private static final String RTC_OFF = "android.intent.action.START_FACTORY_TEST";
    private static final String RTC_ON = "android.intent.action.STOP_FACTORY_TEST";
    public static final int SENSORHUB_START = 1;
    public static final int SENSORHUB_WAKEUP = 2;
    public static final int WAKEUP_DELAY = 5000;
    protected static ModulePower mModulePower = null;
    private TextView BinFwVersion;
    private final int FAIL = 2;
    /* access modifiers changed from: private */
    public TextView IntCheckResult;
    /* access modifiers changed from: private */
    public TextView McuChipName;
    private TextView McuFwVersion;
    private final int PASS = 1;
    private TextView PassFail;
    boolean isFactoryMode = false;
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    LtUtil.log_d(SensorHubTest.this.CLASS_NAME, "mHandler()", "Display SensorHub Info()");
                    SensorHubTest.this.getMcuFirmware();
                    SensorHubTest.this.getBinFirmware();
                    LtUtil.log_d(SensorHubTest.this.CLASS_NAME, "mHandler()", "Start SensorHub SelfTest");
                    if (SensorHubTest.this.isFactoryMode) {
                        SensorHubTest.this.getApplicationContext().sendBroadcast(new Intent(SensorHubTest.RTC_ON));
                        LtUtil.log_i(SensorHubTest.this.CLASS_NAME, "handleCommand", "AlarmManager Enable");
                    }
                    SensorHubTest.this.mModuleDevice.startSensrohubTest();
                    Intent intent = new Intent(SensorHubTest.this.getApplicationContext(), WakeUpService.class);
                    intent.setAction(SensorHubTest.ACTION_WAKE_UP);
                    intent.putExtra("isAcquireWakelock", false);
                    ((AlarmManager) SensorHubTest.this.getApplicationContext().getSystemService("alarm")).set(0, System.currentTimeMillis() + 5000, PendingIntent.getBroadcast(SensorHubTest.this.getApplicationContext(), 0, intent, 0));
                    SensorHubTest.this.mPM.goToSleep(SystemClock.uptimeMillis());
                    return;
                case 2:
                    LtUtil.log_d(SensorHubTest.this.CLASS_NAME, "mHandler()", "WakeUp device");
                    if (SensorHubTest.this.isFactoryMode) {
                        SensorHubTest.this.getApplicationContext().sendBroadcast(new Intent(SensorHubTest.RTC_OFF));
                        LtUtil.log_i(SensorHubTest.this.CLASS_NAME, "handleCommand", "AlarmManager disable");
                    }
                    try {
                        SensorHubTest.this.result = SensorHubTest.this.mModuleDevice.readSensrohubTest().split(",");
                    } catch (Exception e) {
                        SensorHubTest.this.result = new String[3];
                        SensorHubTest.this.result[0] = "NG";
                        SensorHubTest.this.result[1] = "NG";
                        SensorHubTest.this.result[2] = "NG";
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("MCU name: ");
                    sb.append(SensorHubTest.this.result[0]);
                    sb.append("  /  Selftest: ");
                    sb.append(SensorHubTest.this.result[1]);
                    sb.append("  /  INT pin: ");
                    sb.append(SensorHubTest.this.result[2]);
                    LtUtil.log_i(SensorHubTest.this.CLASS_NAME, "Result - ", sb.toString());
                    TextView access$1100 = SensorHubTest.this.IntCheckResult;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("INT Check : ");
                    sb2.append(SensorHubTest.this.result[2]);
                    access$1100.setText(sb2.toString());
                    TextView access$1200 = SensorHubTest.this.McuChipName;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("MCU name : ");
                    sb3.append(SensorHubTest.this.result[0]);
                    access$1200.setText(sb3.toString());
                    do {
                    } while (!SensorHubTest.this.mPM.isScreenOn());
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e2) {
                        e2.printStackTrace();
                    }
                    ModulePower.instance(SensorHubTest.this.getApplicationContext()).setBrightness(255);
                    LtUtil.log_d(SensorHubTest.this.CLASS_NAME, "mHandler()", "checkPassResult()");
                    SensorHubTest.this.checkPassFail();
                    return;
                case 3:
                    SensorHubTest.this.finish();
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public ModuleDevice mModuleDevice;
    /* access modifiers changed from: private */
    public PowerManager mPM;
    /* access modifiers changed from: private */
    public String[] result;

    public SensorHubTest() {
        super("SensorHubTest");
    }

    private void init() {
        LtUtil.log_d(this.CLASS_NAME, "init()", "init_start");
        this.IntCheckResult = (TextView) findViewById(C0268R.C0269id.intcheck);
        this.McuFwVersion = (TextView) findViewById(C0268R.C0269id.mcufirmversion);
        this.BinFwVersion = (TextView) findViewById(C0268R.C0269id.binfirmversion);
        this.McuChipName = (TextView) findViewById(C0268R.C0269id.MCUchip);
        this.PassFail = (TextView) findViewById(C0268R.C0269id.sensorHubpassfail);
        this.mModuleDevice = ModuleDevice.instance(this);
        this.mPM = (PowerManager) getApplicationContext().getSystemService("power");
        mModulePower = mModulePower == null ? ModulePower.instance(this) : mModulePower;
        this.isFactoryMode = FactoryTest.isFactoryMode();
        StringBuilder sb = new StringBuilder();
        sb.append("isFactoryMode: ");
        sb.append(this.isFactoryMode);
        LtUtil.log_i(this.CLASS_NAME, "handleCommand", sb.toString());
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle arg0) {
        LtUtil.log_d(this.CLASS_NAME, "onCreate()", "startOnCreate()");
        super.onCreate(arg0);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        setContentView(C0268R.layout.sensorhub_test);
        init();
        new Thread(new Runnable() {
            public void run() {
                SensorHubTest.this.startSensorHubTest();
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public void startSensorHubTest() {
        LtUtil.log_d(this.CLASS_NAME, "startSensorHubTest", null);
        this.mHandler.sendEmptyMessage(1);
        this.mHandler.sendEmptyMessageDelayed(2, 5000);
    }

    /* access modifiers changed from: private */
    public void getMcuFirmware() {
        LtUtil.log_d(this.CLASS_NAME, "getMcuFirmware", null);
        String temp = Kernel.read(Kernel.SENSORHUB_FIRMWARE_VERSION);
        if (temp != null) {
            String[] McuFirmware = temp.split(",");
            TextView textView = this.McuFwVersion;
            StringBuilder sb = new StringBuilder();
            sb.append("MCU Firm Version : ");
            sb.append(McuFirmware[0]);
            textView.setText(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("McuFirmware = ");
            sb2.append(McuFirmware[0]);
            LtUtil.log_d(this.CLASS_NAME, "getMcuFirmware", sb2.toString());
        }
    }

    /* access modifiers changed from: private */
    public void getBinFirmware() {
        LtUtil.log_d(this.CLASS_NAME, "getBinFirmware", null);
        String tempResult = Kernel.read(Kernel.SENSORHUB_FIRMWARE_VERSION);
        if (tempResult != null) {
            String[] BinFirmware = tempResult.split(",");
            StringBuilder sb = new StringBuilder();
            sb.append("BinFirmware = ");
            sb.append(BinFirmware[1]);
            LtUtil.log_d(this.CLASS_NAME, "getBinFirmware", sb.toString());
            TextView textView = this.BinFwVersion;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Bin Firm Version : ");
            sb2.append(BinFirmware[1]);
            textView.setText(sb2.toString());
            return;
        }
        LtUtil.log_d(this.CLASS_NAME, "getBinFirmware", "BinFirmware is NULL");
    }

    /* access modifiers changed from: private */
    public void checkPassFail() {
        boolean passfail = false;
        if ("OK".equals(this.result[1]) && "OK".equals(this.result[2])) {
            passfail = true;
        }
        if (passfail) {
            this.PassFail.setText("PASS");
            this.PassFail.setTextColor(-16776961);
            return;
        }
        this.PassFail.setText("FAIL");
        this.PassFail.setTextColor(-65536);
    }

    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
