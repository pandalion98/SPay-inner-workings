package com.sec.android.app.hwmoduletest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.FactoryTest;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.android.app.hwmoduletest.view.AccGraph;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.TestCase;
import egis.client.api.EgisFingerprint;
import java.util.Timer;
import java.util.TimerTask;

public class AccGraphActivity extends BaseActivity {
    /* access modifiers changed from: private */
    public static final boolean mIsProxMotorTest = (TestCase.getEnabled(TestCase.IS_PROXIMITY_TEST_MOTOR_FEEDBACK) || TestCase.getEnabled(TestCase.IS_ACCGRAPH_PROXIMITY_MOTOR_FEEDBACK));
    /* access modifiers changed from: private */
    public final long[] FEED_BACK_PATTERN = {0, 5000};
    OnClickListener LPFButtonListener = new OnClickListener() {
        public void onClick(View arg0) {
            if ("LPF Status : OFF".equals((String) ((Button) arg0).getText())) {
                if (AccGraphActivity.this.mIsSubAcc) {
                    Kernel.write(Kernel.SUB_MOTOR_LPF, EgisFingerprint.MAJOR_VERSION);
                } else {
                    Kernel.write(Kernel.MOTOR_LPF, EgisFingerprint.MAJOR_VERSION);
                }
                AccGraphActivity.this.btnLPFOnOff.setText("LPF Status : ON");
                return;
            }
            if (AccGraphActivity.this.mIsSubAcc) {
                Kernel.write(Kernel.SUB_MOTOR_LPF, "0");
            } else {
                Kernel.write(Kernel.MOTOR_LPF, "0");
            }
            AccGraphActivity.this.btnLPFOnOff.setText("LPF Status : OFF");
        }
    };
    private final byte MSG_UPDATE_SENSOR_VALUE = 10;
    private final byte MSG_VIBRATE_FEED_BACK_END = HwModuleTest.ID_SUB_KEY;
    private final byte MSG_VIBRATE_FEED_BACK_START = HwModuleTest.ID_SLEEP;
    /* access modifiers changed from: private */
    public Button btnLPFOnOff;
    /* access modifiers changed from: private */
    public AccGraph mAccGraph;
    private Sensor mAcceSensor;
    /* access modifiers changed from: private */
    public float[] mAcceSensorValues = new float[3];
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    AccGraphActivity.this.mAccGraph.addValue((float) AccGraphActivity.this.mRawData[0], (float) AccGraphActivity.this.mRawData[1], (float) AccGraphActivity.this.mRawData[2]);
                    return;
                case 11:
                    if (AccGraphActivity.mIsProxMotorTest) {
                        AccGraphActivity.this.mVibrator.vibrate(AccGraphActivity.this.FEED_BACK_PATTERN, 0);
                        return;
                    }
                    return;
                case 12:
                    if (AccGraphActivity.mIsProxMotorTest) {
                        AccGraphActivity.this.mVibrator.cancel();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mIsDownKeyVib = false;
    /* access modifiers changed from: private */
    public boolean mIsSubAcc = false;
    private boolean mIsVibrate = false;
    private int mLogCount = 0;
    /* access modifiers changed from: private */
    public float mProxSensorValue = 5.0f;
    private int mProxStatus = -1;
    private Sensor mProximitySensor;
    private int[] mRate = new int[3];
    /* access modifiers changed from: private */
    public int[] mRawData = new int[3];
    private SensorTestListener mSensorListener;
    private SensorManager mSensorManager;
    private Sensor mSubAcceSensor;
    /* access modifiers changed from: private */
    public float[] mSubAcceSensorValues = new float[3];
    private int[] mSubRawData = new int[3];
    private Timer mTimer;
    /* access modifiers changed from: private */
    public Vibrator mVibrator;
    private TimerTask task;

    private class SensorTestListener implements SensorEventListener {
        private SensorTestListener() {
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            int type = event.sensor.getType();
            if (type == 1) {
                AccGraphActivity.this.mAcceSensorValues = (float[]) event.values.clone();
            } else if (type == 8 || type == 65592) {
                AccGraphActivity.this.mProxSensorValue = event.values[0];
            } else if (type == 65687) {
                AccGraphActivity.this.mSubAcceSensorValues = (float[]) event.values.clone();
            }
        }
    }

    public AccGraphActivity() {
        super("AccGraphActivity");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.acc_graph_activity);
        this.mAccGraph = (AccGraph) findViewById(C0268R.C0269id.acc_sensor_graph);
        this.btnLPFOnOff = (Button) findViewById(C0268R.C0269id.acc_lpf_onoff);
        this.mIsSubAcc = getIntent().getBooleanExtra("sub_acc", false);
        StringBuilder sb = new StringBuilder();
        sb.append("mIsSubAcc : ");
        sb.append(this.mIsSubAcc);
        LtUtil.log_d(this.CLASS_NAME, "onCreate", sb.toString());
        if (FactoryTest.isFactoryBinary()) {
            this.btnLPFOnOff.setVisibility(0);
            this.btnLPFOnOff.setText("LPF Status : ON");
            this.btnLPFOnOff.setOnClickListener(this.LPFButtonListener);
            return;
        }
        this.btnLPFOnOff.setVisibility(4);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        StringBuilder sb = new StringBuilder();
        sb.append("focused : ");
        sb.append(hasFocus);
        LtUtil.log_d(this.CLASS_NAME, "onWindowFocusChanged", sb.toString());
        if (hasFocus) {
            sensorRegister();
            if ("LPF Status : OFF".equals(this.btnLPFOnOff.getText().toString())) {
                if (this.mIsSubAcc) {
                    Kernel.write(Kernel.SUB_MOTOR_LPF, "0");
                } else {
                    Kernel.write(Kernel.MOTOR_LPF, "0");
                }
            }
            this.mIsVibrate = false;
            if (mIsProxMotorTest) {
                this.mVibrator.cancel();
            }
            this.mTimer = new Timer();
            this.task = new TimerTask() {
                public void run() {
                    AccGraphActivity.this.readToAccelerometerSensor();
                    AccGraphActivity.this.updateUI();
                }
            };
            this.mTimer.schedule(this.task, 0, 50);
            return;
        }
        if ("LPF Status : OFF".equals(this.btnLPFOnOff.getText().toString())) {
            if (this.mIsSubAcc) {
                Kernel.write(Kernel.SUB_MOTOR_LPF, EgisFingerprint.MAJOR_VERSION);
            } else {
                Kernel.write(Kernel.MOTOR_LPF, EgisFingerprint.MAJOR_VERSION);
            }
        }
        sensorUnregister();
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
        if (this.mHandler.hasMessages(10)) {
            this.mHandler.removeMessages(10);
        }
        if (this.mHandler.hasMessages(12)) {
            this.mHandler.removeMessages(12);
        }
        if (this.mHandler.hasMessages(11)) {
            this.mHandler.removeMessages(11);
        }
        this.mVibrator.cancel();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mVibrator != null) {
            this.mVibrator.cancel();
        }
        if (this.mTimer != null) {
            this.mTimer.cancel();
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode != 25) {
            return super.onKeyUp(keyCode, event);
        }
        LtUtil.log_i(this.CLASS_NAME, "onKeyUp", "KEYCODE_VOLUME_DOWN");
        if (this.mIsDownKeyVib || this.mIsVibrate) {
            this.mIsDownKeyVib = false;
        } else {
            this.mIsDownKeyVib = true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("mIsDownKeyVib: ");
        sb.append(this.mIsDownKeyVib);
        LtUtil.log_i(this.CLASS_NAME, "onKeyUp", sb.toString());
        return true;
    }

    private void sensorRegister() {
        LtUtil.log_i(this.CLASS_NAME, "sensorRegister", "ACC_SENSOR");
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mAcceSensor = this.mSensorManager.getDefaultSensor(1);
        this.mSubAcceSensor = this.mSensorManager.getDefaultSensor(65687);
        this.mProximitySensor = this.mSensorManager.getDefaultSensor(65592);
        if (this.mProximitySensor == null) {
            this.mProximitySensor = this.mSensorManager.getDefaultSensor(8);
        }
        this.mSensorListener = new SensorTestListener();
        this.mSensorManager.registerListener(this.mSensorListener, this.mAcceSensor, 1);
        if (this.mSubAcceSensor != null) {
            this.mSensorManager.registerListener(this.mSensorListener, this.mSubAcceSensor, 1);
        }
        this.mSensorManager.registerListener(this.mSensorListener, this.mProximitySensor, 2);
    }

    private void sensorUnregister() {
        LtUtil.log_i(this.CLASS_NAME, "sensorUnregister", "ACC_SENSOR");
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this.mSensorListener);
        }
        this.mSensorManager = null;
        this.mSensorListener = null;
        this.mAcceSensor = null;
        this.mProximitySensor = null;
    }

    /* access modifiers changed from: private */
    public void readToAccelerometerSensor() {
        String[] rawDatas;
        try {
            if (this.mIsSubAcc) {
                LtUtil.log_d(this.CLASS_NAME, "readToAccelerometerSensor", "read sub accel raw data");
                rawDatas = Kernel.read(Kernel.SUB_ACCEL_SENSOR_RAW, 0).trim().split(",");
            } else {
                LtUtil.log_d(this.CLASS_NAME, "readToAccelerometerSensor", "read main accel raw data");
                rawDatas = Kernel.read(Kernel.ACCEL_SENSOR_RAW, 0).trim().split(",");
            }
            this.mRawData[0] = Integer.parseInt(rawDatas[0].trim());
            this.mRawData[1] = Integer.parseInt(rawDatas[1].trim());
            this.mRawData[2] = Integer.parseInt(rawDatas[2].trim());
        } catch (Exception e) {
            if (this.mIsSubAcc) {
                this.mRawData[0] = (int) ((((double) this.mSubAcceSensorValues[0]) * 1024.0d) / 9.81d);
                this.mRawData[1] = (int) ((((double) this.mSubAcceSensorValues[1]) * 1024.0d) / 9.81d);
                this.mRawData[2] = (int) ((((double) this.mSubAcceSensorValues[2]) * 1024.0d) / 9.81d);
                return;
            }
            this.mRawData[0] = (int) ((((double) this.mAcceSensorValues[0]) * 1024.0d) / 9.81d);
            this.mRawData[1] = (int) ((((double) this.mAcceSensorValues[1]) * 1024.0d) / 9.81d);
            this.mRawData[2] = (int) ((((double) this.mAcceSensorValues[2]) * 1024.0d) / 9.81d);
        }
    }

    /* access modifiers changed from: private */
    public synchronized void updateUI() {
        this.mProxStatus = (int) this.mProxSensorValue;
        this.mLogCount++;
        if (this.mLogCount == 10) {
            StringBuilder sb = new StringBuilder();
            sb.append("mProxStatus : ");
            sb.append(this.mProxStatus);
            LtUtil.log_d(this.CLASS_NAME, " updateProx ", sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("x: ");
            sb2.append(this.mRawData[0]);
            sb2.append(" y: ");
            sb2.append(this.mRawData[1]);
            sb2.append(" z: ");
            sb2.append(this.mRawData[2]);
            LtUtil.log_d(this.CLASS_NAME, " updateAcc ", sb2.toString());
            this.mLogCount = 0;
        }
        if (!this.mIsVibrate) {
            if (this.mProxStatus == 0 || this.mIsDownKeyVib) {
                this.mHandler.sendEmptyMessage(11);
                this.mIsVibrate = true;
            }
        } else if (this.mIsVibrate && this.mProxStatus != 0 && !this.mIsDownKeyVib) {
            this.mHandler.sendEmptyMessage(12);
            this.mIsVibrate = false;
        }
        this.mHandler.sendEmptyMessage(10);
    }
}
