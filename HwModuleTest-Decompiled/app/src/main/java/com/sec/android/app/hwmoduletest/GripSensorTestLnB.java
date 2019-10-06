package com.sec.android.app.hwmoduletest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.HwTestMenu;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.TestCase;
import java.util.Timer;
import java.util.TimerTask;

public class GripSensorTestLnB extends BaseActivity {
    private static final int BODY = 0;
    private static int GRIP_CH_COUNT = 2;
    private static int IS_GRIP_COUNT = 1;
    protected static final int KEY_TIMEOUT = 2;
    protected static final int KEY_TIMER_EXPIRED = 1;
    private static final int LIMB = 10;
    protected static final int MILLIS_IN_SEC = 1000;
    private static final int RELEASE = 5;
    private static final int VIB_STRONG = 1;
    private static final int VIB_WEAK = 0;
    private final String CLASS_NAME = "GripSensorTestLnB";
    private String Grip2ndType = null;
    /* access modifiers changed from: private */
    public TextView info1;
    private TextView info2;
    /* access modifiers changed from: private */
    public LinearLayout mBackgroundLayout1;
    private LinearLayout mBackgroundLayout2;
    private Sensor mGripSensor;
    private SensorTestListener mSensorListener;
    private SensorManager mSensorManager;
    private Vibrator mVibrator;
    WakeLock mWl;
    private TextView txtgripsensor1;
    private TextView txtgripsensor2;

    private class SensorTestListener implements SensorEventListener {
        private boolean Grip_Working;

        private SensorTestListener() {
            this.Grip_Working = false;
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            StringBuilder sb = new StringBuilder();
            sb.append("sensor1 : ");
            sb.append(event.values[0]);
            LtUtil.log_d("GripSensorTestLnB", "onSensorChanged", sb.toString());
            if (event.values[0] == 10.0f || event.values[0] == 0.0f) {
                GripSensorTestLnB.this.setBackgroundColor(GripSensorTestLnB.this.mBackgroundLayout1, C0268R.color.working);
                if (event.values[0] == 10.0f) {
                    GripSensorTestLnB.this.info1.setText(C0268R.string.limb_working);
                } else if (event.values[0] == 0.0f) {
                    GripSensorTestLnB.this.info1.setText(C0268R.string.body_working);
                } else {
                    GripSensorTestLnB.this.info1.setText(C0268R.string.working);
                }
                if (!this.Grip_Working) {
                    GripSensorTestLnB.this.ActiveVibrate(0);
                }
                this.Grip_Working = true;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("============================ status Grip1:DETECT ");
                sb2.append(event.values[0]);
                LtUtil.log_i("GripSensorTestLnB", "onSensorChanged", sb2.toString());
            } else if (event.values[0] == 5.0f && this.Grip_Working) {
                this.Grip_Working = false;
                GripSensorTestLnB.this.stopVibration();
                GripSensorTestLnB.this.setBackgroundColor(GripSensorTestLnB.this.mBackgroundLayout1, C0268R.color.black);
                GripSensorTestLnB.this.info1.setText(C0268R.string.release);
                LtUtil.log_i("GripSensorTestLnB", "onSensorChanged", "============================ status Grip1:RELEASE");
            }
        }

        public boolean getWorking() {
            return this.Grip_Working;
        }
    }

    public GripSensorTestLnB() {
        super("GripSensorTestLnB");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gripsensor);
        init();
        this.Grip2ndType = Feature.getString(Feature.GRIPSENSOR_2ND_TYPE);
        LtUtil.log_d("GripSensorTestLnB", "onCreate", "onCreate");
    }

    public void onResume() {
        super.onResume();
        LtUtil.log_d("GripSensorTestLnB", "onResume", "onResume");
        this.mWl.acquire();
        sensorOn();
    }

    public void onPause() {
        this.mWl.release();
        this.mVibrator.cancel();
        sensorOff();
        super.onPause();
        LtUtil.log_d("GripSensorTestLnB", "onPause", "onPause");
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        String gripChCount = HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_GRIP);
        this.mWl = ((PowerManager) getSystemService("power")).newWakeLock(26, "My Lock Tag");
        this.txtgripsensor1 = (TextView) findViewById(C0268R.C0269id.grip_str_sen1);
        this.txtgripsensor2 = (TextView) findViewById(C0268R.C0269id.grip_str_sen2);
        this.info1 = (TextView) findViewById(C0268R.C0269id.info1);
        this.info2 = (TextView) findViewById(C0268R.C0269id.info2);
        this.info1.setText(C0268R.string.release);
        this.info2.setText(C0268R.string.release);
        this.info1.setTextSize(50.0f);
        this.info2.setTextSize(50.0f);
        this.mBackgroundLayout1 = (LinearLayout) findViewById(C0268R.C0269id.background_layout_side);
        this.mBackgroundLayout2 = (LinearLayout) findViewById(C0268R.C0269id.background_layout_back);
        IS_GRIP_COUNT = TestCase.getInt(TestCase.GRIPSENSOR_GRIP_COUNT);
        if (gripChCount.length() > 0) {
            GRIP_CH_COUNT = Integer.valueOf(gripChCount).intValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("GRIP_CH_COUNT : ");
        sb.append(GRIP_CH_COUNT);
        LtUtil.log_i("GripSensorTestLnB", "init", sb.toString());
        if (GRIP_CH_COUNT == 1) {
            this.mBackgroundLayout2.setVisibility(8);
            this.txtgripsensor1.setVisibility(8);
            this.txtgripsensor2.setVisibility(8);
            this.info2.setVisibility(8);
        }
        this.mVibrator = (Vibrator) getSystemService("vibrator");
    }

    private void sensorOn() {
        this.mSensorListener = new SensorTestListener();
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        if (this.mGripSensor == null) {
            if ("WIFI".equals(Feature.getString(Feature.GRIPSENSOR_TYPE))) {
                this.mGripSensor = this.mSensorManager.getDefaultSensor(65575);
            } else {
                this.mGripSensor = this.mSensorManager.getDefaultSensor(65560);
            }
        }
        if (this.mSensorListener == null || this.mGripSensor == null) {
            LtUtil.log_e("GripSensorTestLnB", "sensorOn", "SensorListener or GripSensor is null");
        }
        this.mSensorManager.registerListener(this.mSensorListener, this.mGripSensor, 2);
        LtUtil.log_d("GripSensorTestLnB", "sensorOn", "SensorListener Type 1 : GRIPSENSOR_TYPE");
        int i = IS_GRIP_COUNT;
        if (Feature.getBoolean(Feature.SUPPORT_GRIPSENS_ALWAYS_ON)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Sensor Reset : ");
            sb.append(Kernel.read(Kernel.GRIP_SENSOR_RESET));
            LtUtil.log_d("GripSensorTestLnB", "sensorOn", sb.toString());
            if (IS_GRIP_COUNT == 2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Sensor Reset Type 2 : ");
                sb2.append(Kernel.read(Kernel.GRIP_SENSOR_RESET_2));
                LtUtil.log_d("GripSensorTestLnB", "sensorOn", sb2.toString());
            }
        }
    }

    private void sensorOff() {
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this.mSensorListener);
            int i = IS_GRIP_COUNT;
        }
        this.mSensorListener = null;
        this.mSensorManager = null;
        this.mGripSensor = null;
    }

    public void setBackgroundColor(LinearLayout layout, int id) {
        layout.setBackgroundColor(getResources().getColor(id));
    }

    public void onFinish() {
        LtUtil.log_d("GripSensorTestLnB", "onFinish", "GripSensor Test finish");
        this.mVibrator.cancel();
        finish();
    }

    public void onExit() {
        LtUtil.log_d("GripSensorTestLnB", "onExit", "finish");
        this.mVibrator.cancel();
        setResult(0);
        finish();
    }

    private void startVibration(int intensity) {
        LtUtil.log_i("GripSensorTestLnB", "startVibration", "Vibration start");
        long[] pattern = {30, 100};
        if (intensity == 0) {
            pattern[0] = 30;
            pattern[1] = 100;
        } else if (intensity == 1) {
            pattern[0] = 30;
            pattern[1] = 400;
        }
        this.mVibrator.vibrate(pattern, 0);
    }

    /* access modifiers changed from: private */
    public void stopVibration() {
        if (this.mSensorListener.getWorking()) {
            LtUtil.log_i("GripSensorTestLnB", "stopVibration", "Vibration doesn't stop beacuse of grip sensor is working");
            return;
        }
        this.mVibrator.cancel();
        LtUtil.log_i("GripSensorTestLnB", "stopVibration", "Vibration stop");
    }

    /* access modifiers changed from: private */
    public void ActiveVibrate(int intensity) {
        if (this.mSensorListener.getWorking()) {
            LtUtil.log_i("GripSensorTestLnB", "ActiveVibrate", "Vibration doesn't restart beacuse of grip sensor is working");
            return;
        }
        startVibration(intensity);
        new Timer().schedule(new TimerTask() {
            public void run() {
            }
        }, 500);
    }
}
