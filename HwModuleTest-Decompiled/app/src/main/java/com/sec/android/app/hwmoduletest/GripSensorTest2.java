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
import java.util.Timer;
import java.util.TimerTask;

public class GripSensorTest2 extends BaseActivity {
    private static int IS_GRIP_COUNT = 0;
    protected static final int KEY_TIMEOUT = 2;
    protected static final int KEY_TIMER_EXPIRED = 1;
    private static final String MASK_SENSING_GRIP_OFF = "000000000000";
    private static final String MASK_SENSING_GRIP_SIDE_ON = "000001000000";
    private static final String MASK_SENSING_GRIP_TOP_ON = "010000000000";
    private static final String MASK_SENSING_GRIP_TOP_SIDE_ON = "010001000000";
    protected static final int MILLIS_IN_SEC = 1000;
    private static final int VIB_STRONG = 1;
    private static final int VIB_WEAK = 0;
    private final String CLASS_NAME = "GripSensorTest2";
    private String Grip2ndType = null;
    private boolean Grip_status_ON_1 = false;
    private boolean Grip_status_ON_2 = false;
    private final int VIBRATE_TIME = 65535;
    byte WHAT_EXIT = 1;
    byte WHAT_NOTI_SENSOR_UPDATAE = 0;
    /* access modifiers changed from: private */
    public TextView info1;
    /* access modifiers changed from: private */
    public TextView info2;
    /* access modifiers changed from: private */
    public LinearLayout mBackgroudLayout1;
    /* access modifiers changed from: private */
    public LinearLayout mBackgroudLayout2;
    private long mCurrentTime = 0;
    private Sensor mGripSensor;
    private Sensor mGripSensor2;
    private boolean mIsPressedBackkey = false;
    private SensorTestListener mSensorListener;
    private SensorTestListener2 mSensorListener2;
    private SensorManager mSensorManager;
    private Vibrator mVibrator;
    WakeLock mWl;
    private boolean pass1 = false;
    private boolean pass2 = false;
    float[] temp;
    private TextView txtgripsensor1;
    private TextView txtgripsensor2;
    private boolean working = false;

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
            LtUtil.log_d("GripSensorTest2", "onSensorChanged", sb.toString());
            if (event.values[0] == 0.0f && !this.Grip_Working) {
                GripSensorTest2.this.setBackgroundColor(GripSensorTest2.this.mBackgroudLayout1, C0268R.color.working);
                GripSensorTest2.this.info1.setText(C0268R.string.working);
                GripSensorTest2.this.ActiveVibrate(0);
                this.Grip_Working = true;
                LtUtil.log_i("GripSensorTest2", "onSensorChanged", "============================ status Grip1:ON");
            } else if (event.values[0] == 5.0f && this.Grip_Working) {
                this.Grip_Working = false;
                GripSensorTest2.this.stopVibration();
                GripSensorTest2.this.setBackgroundColor(GripSensorTest2.this.mBackgroudLayout1, C0268R.color.black);
                GripSensorTest2.this.info1.setText(C0268R.string.release);
                LtUtil.log_i("GripSensorTest2", "onSensorChanged", "============================ status Grip1:OFF");
            }
        }

        public boolean getWorking() {
            return this.Grip_Working;
        }
    }

    private class SensorTestListener2 implements SensorEventListener {
        private boolean Grip_Working;

        private SensorTestListener2() {
            this.Grip_Working = false;
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            StringBuilder sb = new StringBuilder();
            sb.append("sensor2 : ");
            sb.append(event.values[0]);
            LtUtil.log_d("GripSensorTest2", "onSensorChanged", sb.toString());
            if (event.values[0] == 0.0f && !this.Grip_Working) {
                GripSensorTest2.this.setBackgroundColor(GripSensorTest2.this.mBackgroudLayout2, C0268R.color.working);
                GripSensorTest2.this.info2.setText(C0268R.string.working);
                GripSensorTest2.this.ActiveVibrate(0);
                this.Grip_Working = true;
                LtUtil.log_i("GripSensorTest2", "onSensorChanged", "============================ status Grip2:ON");
            } else if (event.values[0] == 5.0f && this.Grip_Working) {
                GripSensorTest2.this.setBackgroundColor(GripSensorTest2.this.mBackgroudLayout2, C0268R.color.black);
                GripSensorTest2.this.info2.setText(C0268R.string.release);
                this.Grip_Working = false;
                GripSensorTest2.this.stopVibration();
                LtUtil.log_i("GripSensorTest2", "onSensorChanged", "============================ status Grip2:OFF");
            }
        }

        public boolean getWorking() {
            return this.Grip_Working;
        }
    }

    public GripSensorTest2() {
        super("GripSensorTest2");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gripsensor);
        init();
        this.Grip2ndType = Feature.getString(Feature.GRIPSENSOR_2ND_TYPE);
        LtUtil.log_d("GripSensorTest2", "onCreate", "onCreate");
    }

    public void onResume() {
        super.onResume();
        LtUtil.log_d("GripSensorTest2", "onResume", "onResume");
        this.mWl.acquire();
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
            LtUtil.log_e("GripSensorTest2", "onResume", "SensorListener or GripSensor is null");
        }
        this.mSensorManager.registerListener(this.mSensorListener, this.mGripSensor, 2);
        LtUtil.log_d("GripSensorTest2", "onResume", "SensorListener Type 1 : GRIPSENSOR_TYPE");
        if (IS_GRIP_COUNT == 2) {
            this.mSensorListener2 = new SensorTestListener2();
            if ("WIFI".equals(this.Grip2ndType)) {
                this.mGripSensor2 = this.mSensorManager.getDefaultSensor(65575);
            } else if ("AP".equals(this.Grip2ndType)) {
                this.mGripSensor2 = this.mSensorManager.getDefaultSensor(65560);
            }
            if (this.mSensorListener2 == null || this.mGripSensor2 == null) {
                LtUtil.log_e("GripSensorTest2", "onResume", "SensorListener2 or GripSensor2 is null");
            }
            this.mSensorManager.registerListener(this.mSensorListener2, this.mGripSensor2, 2);
            StringBuilder sb = new StringBuilder();
            sb.append("SensorListener Type 2 : ");
            sb.append(this.Grip2ndType);
            LtUtil.log_d("GripSensorTest2", "onResume", sb.toString());
        }
        if (Feature.getBoolean(Feature.SUPPORT_GRIPSENS_ALWAYS_ON)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Sensor Reset : ");
            sb2.append(Kernel.read(Kernel.GRIP_SENSOR_RESET));
            LtUtil.log_d("GripSensorTest2", "onResume", sb2.toString());
            if (IS_GRIP_COUNT == 2) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Sensor Reset Type 2 : ");
                sb3.append(Kernel.read(Kernel.GRIP_SENSOR_RESET_2));
                LtUtil.log_d("GripSensorTest2", "onResume", sb3.toString());
            }
        }
    }

    public void onPause() {
        this.mWl.release();
        this.mVibrator.cancel();
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this.mSensorListener);
            if (IS_GRIP_COUNT == 2) {
                this.mSensorManager.unregisterListener(this.mSensorListener2);
                this.mSensorListener2 = null;
                this.mGripSensor2 = null;
            }
        }
        this.mSensorListener = null;
        this.mSensorManager = null;
        this.mGripSensor = null;
        super.onPause();
        LtUtil.log_d("GripSensorTest2", "onPause", "onPause");
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        String gripCount = HwTestMenu.getTestCase(HwTestMenu.LCD_TEST_GRIP);
        this.mWl = ((PowerManager) getSystemService("power")).newWakeLock(26, "My Lock Tag");
        this.txtgripsensor1 = (TextView) findViewById(C0268R.C0269id.grip_str_sen1);
        this.txtgripsensor2 = (TextView) findViewById(C0268R.C0269id.grip_str_sen2);
        this.info1 = (TextView) findViewById(C0268R.C0269id.info1);
        this.info2 = (TextView) findViewById(C0268R.C0269id.info2);
        this.info1.setText(C0268R.string.release);
        this.info2.setText(C0268R.string.release);
        this.mBackgroudLayout1 = (LinearLayout) findViewById(C0268R.C0269id.background_layout_side);
        this.mBackgroudLayout2 = (LinearLayout) findViewById(C0268R.C0269id.background_layout_back);
        if (gripCount.length() > 0) {
            IS_GRIP_COUNT = Integer.valueOf(gripCount).intValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("IS_GRIP_COUNT : ");
        sb.append(IS_GRIP_COUNT);
        LtUtil.log_i("GripSensorTest2", "init", sb.toString());
        if (IS_GRIP_COUNT == 1) {
            this.mBackgroudLayout2.setVisibility(8);
            this.txtgripsensor1.setVisibility(8);
            this.txtgripsensor2.setVisibility(8);
            this.info2.setVisibility(8);
        }
        this.mVibrator = (Vibrator) getSystemService("vibrator");
    }

    public void setBackgroundColor(LinearLayout layout, int id) {
        layout.setBackgroundColor(getResources().getColor(id));
    }

    public void onFinish() {
        LtUtil.log_d("GripSensorTest2", "onFinish", "GripSensor Test finish");
        this.mVibrator.cancel();
        finish();
    }

    public void onExit() {
        LtUtil.log_d("GripSensorTest2", "onExit", "finish");
        this.mVibrator.cancel();
        setResult(0);
        finish();
    }

    private void startVibration(int intensity) {
        LtUtil.log_i("GripSensorTest2", "startVibration", "Vibration start");
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
        if (IS_GRIP_COUNT == 2 && (this.mSensorListener2.getWorking() || this.mSensorListener.getWorking())) {
            LtUtil.log_i("GripSensorTest2", "stopVibration", "Vibration doesn't stop beacuse of grip sensor is working");
            return;
        }
        this.mVibrator.cancel();
        LtUtil.log_i("GripSensorTest2", "stopVibration", "Vibration stop");
    }

    /* access modifiers changed from: private */
    public void ActiveVibrate(int intensity) {
        startVibration(intensity);
        new Timer().schedule(new TimerTask() {
            public void run() {
            }
        }, 500);
    }

    private void startTest() {
        if (IS_GRIP_COUNT == 1) {
            LtUtil.log_i("GripSensorTest2", "onReceive", "IS_GRIP_COUNT == 1");
            if (this.temp[0] >= 0.0f) {
                setBackgroundColor(this.mBackgroudLayout1, C0268R.color.working);
                this.info1.setText(C0268R.string.working);
                ActiveVibrate(0);
                LtUtil.log_i("GripSensorTest2", "onReceive", "________ 0000 ________");
                return;
            }
            stopVibration();
            setBackgroundColor(this.mBackgroudLayout1, C0268R.color.black);
            setBackgroundColor(this.mBackgroudLayout2, C0268R.color.black);
            this.info1.setText(C0268R.string.release);
            this.info2.setText(C0268R.string.release);
            LtUtil.log_i("GripSensorTest2", "onReceive", "MASK_SENSING_GRIP_OFF");
        } else if (this.temp[0] >= 0.0f && this.temp[1] >= 0.0f) {
            setBackgroundColor(this.mBackgroudLayout1, C0268R.color.working);
            setBackgroundColor(this.mBackgroudLayout2, C0268R.color.working);
            this.info1.setText(C0268R.string.working);
            this.info2.setText(C0268R.string.working);
            ActiveVibrate(1);
            LtUtil.log_i("GripSensorTest2", "onReceive", "MASK_SENSING_GRIP_SIDE_BACK_ON");
        } else if (this.temp[0] >= 0.0f) {
            setBackgroundColor(this.mBackgroudLayout1, C0268R.color.working);
            setBackgroundColor(this.mBackgroudLayout2, C0268R.color.black);
            this.info1.setText(C0268R.string.working);
            this.info2.setText(C0268R.string.release);
            ActiveVibrate(0);
            LtUtil.log_i("GripSensorTest2", "onReceive", "MASK_SENSING_GRIP_SIDE_ON");
        } else if (this.temp[1] >= 0.0f) {
            setBackgroundColor(this.mBackgroudLayout1, C0268R.color.black);
            setBackgroundColor(this.mBackgroudLayout2, C0268R.color.working);
            this.info1.setText(C0268R.string.release);
            this.info2.setText(C0268R.string.working);
            ActiveVibrate(0);
            LtUtil.log_i("GripSensorTest2", "onReceive", "MASK_SENSING_GRIP_BACK_ON");
        } else {
            stopVibration();
            setBackgroundColor(this.mBackgroudLayout1, C0268R.color.black);
            setBackgroundColor(this.mBackgroudLayout2, C0268R.color.black);
            this.info1.setText(C0268R.string.release);
            this.info2.setText(C0268R.string.release);
            LtUtil.log_i("GripSensorTest2", "onReceive", "MASK_SENSING_GRIP_OFF else");
        }
    }
}
