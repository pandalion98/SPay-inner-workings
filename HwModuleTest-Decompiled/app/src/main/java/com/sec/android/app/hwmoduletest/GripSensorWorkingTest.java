package com.sec.android.app.hwmoduletest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.TestCase;
import egis.client.api.EgisFingerprint;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class GripSensorWorkingTest extends BaseActivity {
    private static final int GRIP_DETECT = 1;
    private static final int GRIP_NONE = -1;
    private static final int GRIP_RELEASE = 0;
    private static String[] READ_CH_COUNT = {Kernel.GRIP_SENSOR_CH_COUNT, Kernel.GRIP_SENSOR_CH_COUNT_2, Kernel.GRIP_SENSOR_CH_COUNT_3};
    private static String[] READ_CH_STATE = {Kernel.GRIP_SENSOR_CH_STATE, Kernel.GRIP_SENSOR_CH_STATE_2, Kernel.GRIP_SENSOR_CH_STATE_3};
    private static String[] RESET_SYSFS_PATH = {Kernel.GRIP_SENSOR_RESET, Kernel.GRIP_SENSOR_RESET_2ND, Kernel.GRIP_SENSOR_RESET_3RD};
    private static final int VIB_STRONG = 1;
    private static final int VIB_WEAK = 0;
    private static final int WHAT_SENSOR_DETECT = 2;
    private static final int WHAT_SENSOR_RELEASE = 1;
    private static final int WHAT_SENSOR_UNKNOWN = 0;
    private static final int WHAT_SENSOR_UPDATE = 3;
    private final String CLASS_NAME = "GripSensorWorkingTest";
    private final int GRIP_ITEM1 = 0;
    private final int GRIP_ITEM2 = 1;
    private final int GRIP_ITEM3 = 2;
    private final int MAX_GRIP = 3;
    private final LinearLayout[] mBackgroundLayout = new LinearLayout[3];
    /* access modifiers changed from: private */
    public int mGripCount = 1;
    /* access modifiers changed from: private */
    public Thread mGripDataThread = null;
    /* access modifiers changed from: private */
    public int[] mGripState;
    private String[] mGripType = null;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Handler: Grip ");
                    sb.append(msg.arg1);
                    sb.append(" State Unknown");
                    LtUtil.log_e("GripSensorWorkingTest", "handleMessage", sb.toString());
                    return;
                case 1:
                    GripSensorWorkingTest.this.releaseGripSensor(msg.arg1);
                    return;
                case 2:
                    GripSensorWorkingTest.this.workingGripSensor(msg.arg1);
                    return;
                case 3:
                    int item = msg.arg1;
                    if (GripSensorWorkingTest.this.mIsWorking[item] && GripSensorWorkingTest.this.mGripState[item] == 0) {
                        GripSensorWorkingTest.this.releaseGripSensor(item);
                        GripSensorWorkingTest.this.mIsWorking[item] = false;
                        return;
                    } else if (!GripSensorWorkingTest.this.mIsWorking[item] && GripSensorWorkingTest.this.mGripState[item] == 1) {
                        GripSensorWorkingTest.this.workingGripSensor(item);
                        GripSensorWorkingTest.this.mIsWorking[item] = true;
                        return;
                    } else if (GripSensorWorkingTest.this.mGripState[item] == -1) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Grip #");
                        sb2.append(item + 1);
                        sb2.append(" State Unknown");
                        LtUtil.log_e("GripSensorWorkingTest", "handleMessage", sb2.toString());
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean[] mIsWorking;
    private SensorInfo[] mSensorInfo;
    private SensorTestListener[] mSensorListeners = null;
    private SensorManager mSensorManager = null;
    private final TextView[] mTextGripSensor = new TextView[3];
    private final TextView[] mTextInfo = new TextView[3];
    private Vibrator mVibrator;
    WakeLock mWl;
    private boolean misWifiSub = false;
    private List<Sensor> sensors = null;

    static class SensorInfo {
        int ch_count;
        int[] state;

        SensorInfo() {
            this.ch_count = 1;
            this.state = new int[this.ch_count];
            this.state[0] = -1;
        }

        SensorInfo(int ch) {
            this.ch_count = ch;
            this.state = new int[this.ch_count];
            for (int i = 0; i < ch; i++) {
                this.state[i] = -1;
            }
        }
    }

    private class SensorTestListener implements SensorEventListener {
        private int gripData;
        private final int gripInfo;
        private Sensor gripSensor = null;
        private final String gripType;

        public SensorTestListener(String grip_type, int grip_sequence) {
            this.gripType = grip_type;
            this.gripInfo = grip_sequence;
        }

        public void setGripSensor(Sensor sensor) {
            this.gripSensor = sensor;
        }

        public Sensor getGripSensor() {
            return this.gripSensor;
        }

        public String getGripType() {
            return this.gripType;
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            StringBuilder sb = new StringBuilder();
            sb.append("sensor : ");
            sb.append(event.values[0]);
            sb.append(",");
            sb.append(event.values[1]);
            sb.append(",");
            sb.append(event.values[2]);
            LtUtil.log_d("GripSensorWorkingTest", "onSensorChanged", sb.toString());
            this.gripData = (int) event.values[0];
            GripSensorWorkingTest.this.setSensorData(this.gripInfo, this.gripData);
        }
    }

    public GripSensorWorkingTest() {
        super("GripSensorWorkingTest");
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LtUtil.log_d("GripSensorWorkingTest", "onCreate", "onCreate");
        LtUtil.setSystemKeyBlock(getComponentName(), 3);
        LtUtil.setSystemKeyBlock(getComponentName(), 187);
        setContentView(C0268R.layout.gripsensor_working);
        this.mVibrator = (Vibrator) getSystemService("vibrator");
        this.mGripType = new String[3];
        this.mGripCount = TestCase.getInt(TestCase.GRIPSENSOR_GRIP_COUNT);
        if (Feature.getBoolean(Feature.GRIP_TEST_UI_NEW_CONCEPT)) {
            String[] items = Feature.getString(Feature.GRIPSENSOR_TYPE).split(",");
            if (items != null) {
                for (int i = 0; i < items.length; i++) {
                    this.mGripType[i] = items[i];
                    if ("WIFI_SUB".equals(items[i])) {
                        this.misWifiSub = true;
                    }
                }
            }
        } else {
            this.mGripType[0] = Feature.getString(Feature.GRIPSENSOR_TYPE);
            this.mGripType[1] = Feature.getString(Feature.GRIPSENSOR_2ND_TYPE);
        }
        setGripInfo();
        initUI();
        initGripData();
        getSensorData();
    }

    public void onResume() {
        LtUtil.log_d("GripSensorWorkingTest", "onResume", "onResume");
        super.onResume();
        sensorOn();
        startGetGripData();
    }

    public void onPause() {
        LtUtil.log_d("GripSensorWorkingTest", "onPause", "onPause");
        stopGetGripData();
        stopVibration();
        sensorOff();
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LtUtil.log_d("GripSensorWorkingTest", "onDestroy", "onDestroy");
        super.onDestroy();
    }

    private void initUI() {
        StringBuilder sb = new StringBuilder();
        sb.append("mGripCount : ");
        sb.append(this.mGripCount);
        LtUtil.log_i("GripSensorWorkingTest", "initUI", sb.toString());
        getSystemService("power");
        this.mBackgroundLayout[0] = (LinearLayout) findViewById(C0268R.C0269id.background_layout_grip1);
        this.mBackgroundLayout[1] = (LinearLayout) findViewById(C0268R.C0269id.background_layout_grip2);
        this.mBackgroundLayout[2] = (LinearLayout) findViewById(C0268R.C0269id.background_layout_grip3);
        this.mTextGripSensor[0] = (TextView) findViewById(C0268R.C0269id.grip_str_sen1);
        this.mTextGripSensor[1] = (TextView) findViewById(C0268R.C0269id.grip_str_sen2);
        this.mTextGripSensor[2] = (TextView) findViewById(C0268R.C0269id.grip_str_sen3);
        this.mTextInfo[0] = (TextView) findViewById(C0268R.C0269id.info1);
        this.mTextInfo[1] = (TextView) findViewById(C0268R.C0269id.info2);
        this.mTextInfo[2] = (TextView) findViewById(C0268R.C0269id.info3);
        if (this.mGripCount < 3) {
            this.mBackgroundLayout[2].setVisibility(8);
        }
        if (this.mGripCount < 2) {
            this.mTextGripSensor[0].setVisibility(8);
            this.mBackgroundLayout[1].setVisibility(8);
        }
    }

    private void setGripInfo() {
        int cnt = 0;
        this.mSensorInfo = new SensorInfo[this.mGripCount];
        for (int i = 0; i < this.mSensorInfo.length; i++) {
            String ch_count = Kernel.read(READ_CH_COUNT[i]);
            if (ch_count != null) {
                this.mSensorInfo[i] = new SensorInfo(Integer.parseInt(ch_count));
            } else {
                this.mSensorInfo[i] = new SensorInfo();
            }
            cnt += this.mSensorInfo[i].ch_count;
        }
        if (cnt > this.mGripCount) {
            this.mGripCount = cnt;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("SensorInfo=");
        sb.append(this.mSensorInfo.length);
        sb.append(", Grip items=");
        sb.append(this.mGripCount);
        LtUtil.log_d("GripSensorWorkingTest", "setGripInfo", sb.toString());
    }

    private void initGripData() {
        StringBuilder sb = new StringBuilder();
        sb.append("SensorInfo=");
        sb.append(this.mSensorInfo.length);
        sb.append(", Grip items=");
        sb.append(this.mGripCount);
        LtUtil.log_d("GripSensorWorkingTest", "setGripPath", sb.toString());
        this.mIsWorking = new boolean[this.mGripCount];
        this.mGripState = new int[this.mGripCount];
        for (int i = 0; i < this.mGripCount; i++) {
            this.mIsWorking[i] = false;
            this.mGripState[i] = -1;
        }
    }

    /* access modifiers changed from: private */
    public void setSensorData(int info, int event) {
        StringBuilder sb = new StringBuilder();
        sb.append("info=");
        sb.append(info);
        sb.append(", event=");
        sb.append(event);
        LtUtil.log_d("GripSensorWorkingTest", "setSensorData", sb.toString());
        int i = 0;
        if (this.mSensorInfo[info].ch_count > 1) {
            String data = Kernel.read(READ_CH_STATE[info], false);
            while (true) {
                int i2 = i;
                if (i2 < this.mSensorInfo[info].ch_count) {
                    if (data != null) {
                        this.mSensorInfo[info].state[i2] = Integer.valueOf(data.split(",")[i2]).intValue();
                    } else {
                        this.mSensorInfo[info].state[i2] = -1;
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("state[");
                    sb2.append(i2);
                    sb2.append("]=");
                    sb2.append(this.mSensorInfo[info].state[i2]);
                    LtUtil.log_d("GripSensorWorkingTest", "setSensorData", sb2.toString());
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else {
            if (event == 5) {
                this.mSensorInfo[info].state[0] = 0;
            } else if (event == 0) {
                this.mSensorInfo[info].state[0] = 1;
            } else {
                this.mSensorInfo[info].state[0] = -1;
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("state[0]=");
            sb3.append(this.mSensorInfo[info].state[0]);
            LtUtil.log_d("GripSensorWorkingTest", "setSensorData", sb3.toString());
        }
    }

    /* access modifiers changed from: private */
    public void getSensorData() {
        int index;
        int index2 = 0;
        for (int i = 0; i < this.mSensorInfo.length; i++) {
            if (this.mSensorInfo[i].ch_count > 1) {
                String data = Kernel.read(READ_CH_STATE[i], false);
                index = index2;
                int j = 0;
                while (j < this.mSensorInfo[i].ch_count) {
                    if (data != null) {
                        this.mSensorInfo[i].state[j] = Integer.valueOf(data.split(",")[j]).intValue();
                    }
                    int index3 = index + 1;
                    this.mGripState[index] = this.mSensorInfo[i].state[j];
                    j++;
                    index = index3;
                }
            } else {
                index = index2 + 1;
                this.mGripState[index2] = this.mSensorInfo[i].state[0];
            }
            index2 = index;
        }
    }

    private boolean sensorOn() {
        LtUtil.log_i("GripSensorWorkingTest", "sensorOn", "Grip sensor ON");
        if ("ABOV".equals(Kernel.read(Kernel.GRIP_SENSOR_VENDOR))) {
            gripReset();
        } else if (Feature.getBoolean(Feature.SUPPORT_GRIPSENS_ALWAYS_ON)) {
            for (int i = 0; i < this.mSensorInfo.length; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append("Sensor Reset[");
                sb.append(i);
                sb.append("] : ");
                sb.append(Kernel.read(RESET_SYSFS_PATH[i]));
                LtUtil.log_d("GripSensorWorkingTest", "sensorOn", sb.toString());
            }
        }
        if (this.mSensorManager == null) {
            this.mSensorManager = (SensorManager) getSystemService("sensor");
        }
        if (this.mGripCount >= 2) {
            if (this.misWifiSub) {
                this.sensors = this.mSensorManager.getSensorList(65575);
            } else {
                this.sensors = this.mSensorManager.getSensorList(65560);
            }
        }
        this.mSensorListeners = new SensorTestListener[this.mSensorInfo.length];
        for (int i2 = 0; i2 < this.mSensorInfo.length; i2++) {
            this.mSensorListeners[i2] = new SensorTestListener(this.mGripType[i2], i2);
            mappingGripSensor(i2);
            if (this.mSensorManager == null || this.mSensorListeners[i2] == null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Register Grip Sensor Listener [");
                sb2.append(i2);
                sb2.append("] FAIL");
                LtUtil.log_e("GripSensorWorkingTest", "sensorOn", sb2.toString());
                return false;
            }
            this.mSensorManager.registerListener(this.mSensorListeners[i2], this.mSensorListeners[i2].getGripSensor(), 2);
        }
        return true;
    }

    private void sensorOff() {
        LtUtil.log_i("GripSensorWorkingTest", "sensorOff", "Grip sensor OFF");
        if (this.mSensorManager != null) {
            for (int i = 0; i < this.mSensorInfo.length; i++) {
                if (this.mSensorListeners[i] != null) {
                    this.mSensorManager.unregisterListener(this.mSensorListeners[i]);
                    this.mSensorListeners[i] = null;
                }
            }
        }
        this.mSensorListeners = null;
        this.mSensorManager = null;
    }

    private void gripReset() {
        LtUtil.log_d("GripSensorWorkingTest", "gripReset", "gripReset");
        if (this.mGripCount >= 1) {
            Kernel.write(Kernel.GRIP_TOUCH_SENSOR_RESET, EgisFingerprint.MAJOR_VERSION);
        }
        if (this.mGripCount >= 2) {
            Kernel.write(Kernel.GRIP_TOUCH_SENSOR_RESET_2, EgisFingerprint.MAJOR_VERSION);
        }
        if (this.mGripCount >= 3) {
            Kernel.write(Kernel.GRIP_TOUCH_SENSOR_RESET_3, EgisFingerprint.MAJOR_VERSION);
        }
        Kernel.read(Kernel.GRIP_RESET_READY);
    }

    private void startGetGripData() {
        StringBuilder sb = new StringBuilder();
        sb.append("Thread state = ");
        sb.append(this.mGripDataThread);
        LtUtil.log_d("GripSensorWorkingTest", "startGetGripData", sb.toString());
        this.mGripDataThread = new Thread(new Runnable() {
            public void run() {
                while (!GripSensorWorkingTest.this.mGripDataThread.isInterrupted()) {
                    try {
                        int[] preState = new int[GripSensorWorkingTest.this.mGripCount];
                        for (int i = 0; i < GripSensorWorkingTest.this.mGripCount; i++) {
                            preState[i] = GripSensorWorkingTest.this.mGripState[i];
                        }
                        GripSensorWorkingTest.this.getSensorData();
                        for (int i2 = 0; i2 < GripSensorWorkingTest.this.mGripCount; i2++) {
                            if (preState[i2] != GripSensorWorkingTest.this.mGripState[i2]) {
                                Message msg = new Message();
                                msg.arg1 = i2;
                                msg.what = 3;
                                GripSensorWorkingTest.this.mHandler.sendMessage(msg);
                            }
                        }
                    } catch (NumberFormatException e) {
                        LtUtil.log_e(e);
                        return;
                    }
                }
            }
        });
        this.mGripDataThread.start();
    }

    private void stopGetGripData() {
        StringBuilder sb = new StringBuilder();
        sb.append("Thread state = ");
        sb.append(this.mGripDataThread);
        LtUtil.log_d("GripSensorWorkingTest", "stopGetGripData", sb.toString());
        while (this.mGripDataThread != null && this.mGripDataThread.isAlive()) {
            this.mGripDataThread.interrupt();
        }
        this.mGripDataThread = null;
        if (this.mHandler != null && this.mHandler.hasMessages(2)) {
            this.mHandler.removeMessages(2);
        }
        if (this.mHandler != null && this.mHandler.hasMessages(1)) {
            this.mHandler.removeMessages(1);
        }
        if (this.mHandler != null && this.mHandler.hasMessages(0)) {
            this.mHandler.removeMessages(0);
        }
        if (this.mHandler != null && this.mHandler.hasMessages(3)) {
            this.mHandler.removeMessages(3);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Thread state = ");
        sb2.append(this.mGripDataThread);
        LtUtil.log_d("GripSensorWorkingTest", "stopGetGripData", sb2.toString());
    }

    public void setBackgroundColor(LinearLayout layout, int id) {
        layout.setBackgroundColor(getResources().getColor(id));
    }

    public void onExit() {
        LtUtil.log_d("GripSensorWorkingTest", "onExit", "finish");
        this.mVibrator.cancel();
        setResult(0);
        finish();
    }

    private void ActiveVibrate(int intensity) {
        startVibration(intensity);
        new Timer().schedule(new TimerTask() {
            public void run() {
            }
        }, 500);
    }

    private void startVibration(int intensity) {
        LtUtil.log_i("GripSensorWorkingTest", "startVibration", "Vibration start");
        if (this.mVibrator != null && this.mVibrator.hasVibrator()) {
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
    }

    private void stopVibration() {
        LtUtil.log_i("GripSensorWorkingTest", "stopVibration", "Vibration stop");
        if (this.mVibrator != null && this.mVibrator.hasVibrator()) {
            this.mVibrator.cancel();
        }
    }

    /* access modifiers changed from: private */
    public void workingGripSensor(int index) {
        if (!this.mIsWorking[index]) {
            this.mTextInfo[index].setText("Working");
            this.mBackgroundLayout[index].setBackgroundColor(getResources().getColor(C0268R.color.working));
            this.mIsWorking[index] = true;
            boolean allWorking = true;
            for (int i = 0; i < this.mGripCount; i++) {
                if (!this.mIsWorking[i]) {
                    allWorking = false;
                }
            }
            if (allWorking) {
                ActiveVibrate(1);
            } else {
                ActiveVibrate(0);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Grip #");
            sb.append(index + 1);
            sb.append(" Working");
            LtUtil.log_i("GripSensorWorkingTest", "workingGripSensor", sb.toString());
        }
    }

    /* access modifiers changed from: private */
    public void releaseGripSensor(int index) {
        if (this.mIsWorking[index]) {
            this.mIsWorking[index] = false;
            this.mBackgroundLayout[index].setBackgroundColor(getResources().getColor(C0268R.color.black));
            this.mTextInfo[index].setText("Release");
            boolean isStopVib = true;
            for (int i = 0; i < this.mGripCount; i++) {
                if (this.mIsWorking[i]) {
                    isStopVib = false;
                }
            }
            if (isStopVib) {
                stopVibration();
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Grip #");
            sb.append(index + 1);
            sb.append(" Release");
            LtUtil.log_i("GripSensorWorkingTest", "releaseGripSensor", sb.toString());
        }
    }

    private void mappingGripSensor(int index) {
        Sensor gripSensor = null;
        if ("WIFI".equals(this.mSensorListeners[index].getGripType())) {
            gripSensor = this.mSensorManager.getDefaultSensor(65575);
            this.mSensorListeners[index].setGripSensor(gripSensor);
        } else if ("AP".equals(this.mSensorListeners[index].getGripType()) || "TOUCH".equals(this.mSensorListeners[index].getGripType())) {
            if (this.mGripCount < 2 || this.misWifiSub) {
                gripSensor = this.mSensorManager.getDefaultSensor(65560);
                this.mSensorListeners[index].setGripSensor(gripSensor);
            } else if (this.sensors != null && this.sensors.size() >= 1) {
                gripSensor = (Sensor) this.sensors.get(0);
                this.mSensorListeners[index].setGripSensor(gripSensor);
            }
        } else if (("SUB".equals(this.mSensorListeners[index].getGripType()) || "WIFI_SUB".equals(this.mSensorListeners[index].getGripType())) && this.sensors != null && this.sensors.size() >= 2) {
            gripSensor = (Sensor) this.sensors.get(1);
            this.mSensorListeners[index].setGripSensor(gripSensor);
        }
        if (gripSensor == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("GripSensor");
            sb.append(index);
            sb.append(" is NULL");
            LtUtil.log_e("GripSensorWorkingTest", "mappingGripSensor", sb.toString());
        }
    }
}
