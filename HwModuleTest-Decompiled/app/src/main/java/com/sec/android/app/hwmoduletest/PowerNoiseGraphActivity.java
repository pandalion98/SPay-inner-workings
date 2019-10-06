package com.sec.android.app.hwmoduletest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.sec.android.app.hwmoduletest.modules.ModuleSensor;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.android.app.hwmoduletest.view.PowerNoiseGraph;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.TestCase;
import egis.client.api.EgisFingerprint;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class PowerNoiseGraphActivity extends BaseActivity {
    private static int period = 10000;
    /* access modifiers changed from: private */
    public static int sampling = 130;
    private final int fail = 0;
    private String mChipsetName = "";
    private String mGeoMagneticSensorName = "";
    private Sensor mMagneticSensor = null;
    private float[] mOriginalData_Magnetic = null;
    /* access modifiers changed from: private */
    public PowerNoiseGraph mPowerNoiseGraph;
    private SensorListener mSensorListener = null;
    private SensorManager mSensorManager = null;
    private final int pass = 1;
    /* access modifiers changed from: private */
    public String[] power_noise = new String[3];
    private double power_noise_spec_x = 7.0d;
    private double power_noise_spec_y = 7.0d;
    private double power_noise_spec_z = 7.0d;
    /* access modifiers changed from: private */
    public float[] raw_data = new float[3];
    private float[][] raw_datas = null;
    /* access modifiers changed from: private */
    public int retSelftest = 2;
    /* access modifiers changed from: private */
    public int run_num;
    private Timer sptimer;
    private TimerTask task;

    private static class SensorListener implements SensorEventListener {
        private SensorListener() {
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            event.sensor.getType();
        }
    }

    public PowerNoiseGraphActivity() {
        super("PowerNoiseGraphActivity");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.power_noise_graph_activity);
        this.mPowerNoiseGraph = (PowerNoiseGraph) findViewById(C0268R.C0269id.power_noise_graph);
        this.mGeoMagneticSensorName = Kernel.getGeoMagneticSensorName();
        this.mChipsetName = Feature.getString(Feature.CHIPSET_NAME);
        if (this.mGeoMagneticSensorName.equalsIgnoreCase(ModuleSensor.FEATURE_MAGENTIC_AK8963C) || this.mGeoMagneticSensorName.equalsIgnoreCase(ModuleSensor.FEATURE_MAGENTIC_AK8963) || this.mGeoMagneticSensorName.equalsIgnoreCase(ModuleSensor.FEATURE_MAGENTIC_HSCDTD008A)) {
            this.power_noise_spec_x = 14.0d;
            this.power_noise_spec_y = 14.0d;
            this.power_noise_spec_z = 14.0d;
        } else if (this.mGeoMagneticSensorName.equalsIgnoreCase(ModuleSensor.FEATURE_MAGENTIC_YAS537) || this.mGeoMagneticSensorName.equalsIgnoreCase(ModuleSensor.FEATURE_MAGENTIC_YAS539)) {
            this.power_noise_spec_x = 7.0d;
            this.power_noise_spec_y = 7.0d;
            this.power_noise_spec_z = 7.0d;
        } else if (this.mGeoMagneticSensorName.contains("YAS")) {
            this.power_noise_spec_x = 14.0d;
            this.power_noise_spec_y = 26.0d;
            this.power_noise_spec_z = 11.0d;
        } else if (this.mGeoMagneticSensorName.contains("BMC150")) {
            this.power_noise_spec_x = 34.0d;
            this.power_noise_spec_y = 34.0d;
            this.power_noise_spec_z = 34.0d;
        } else if (this.mGeoMagneticSensorName.contains(ModuleSensor.FEATURE_MAGENTIC_AK09911C)) {
            this.power_noise_spec_x = 3.5d;
            this.power_noise_spec_y = 3.5d;
            this.power_noise_spec_z = 3.5d;
        } else if (this.mGeoMagneticSensorName.contains(ModuleSensor.FEATURE_MAGENTIC_STMICRO)) {
            this.power_noise_spec_x = 36.0d;
            this.power_noise_spec_y = 36.0d;
            this.power_noise_spec_z = 36.0d;
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mChipsetName.contains("SDM845")) {
            sensorRegister();
            Kernel.write(Kernel.MAGNT_SENSOR_RAW, EgisFingerprint.MAJOR_VERSION);
        } else if (this.mGeoMagneticSensorName.contains("YAS") || TestCase.getEnabled(TestCase.IS_ASAHI_USING_SYSFS)) {
            Kernel.write(Kernel.MAGNT_SENSOR_RAW, EgisFingerprint.MAJOR_VERSION);
        } else if (TestCase.getEnabled(TestCase.IS_USING_SENSOR_MANAGER_IN_POWER_NOISE)) {
            sensorRegister();
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                PowerNoiseGraphActivity.this.mPowerNoiseGraph.addValueWithPowerNoise(PowerNoiseGraphActivity.this.raw_data[0], PowerNoiseGraphActivity.this.raw_data[1], PowerNoiseGraphActivity.this.raw_data[2], PowerNoiseGraphActivity.this.power_noise, PowerNoiseGraphActivity.this.raw_data, PowerNoiseGraphActivity.this.retSelftest);
            }
        };
        this.run_num = 0;
        this.raw_datas = (float[][]) Array.newInstance(float.class, new int[]{sampling, 3});
        this.sptimer = new Timer();
        this.task = new TimerTask() {
            public void run() {
                if (PowerNoiseGraphActivity.this.run_num > PowerNoiseGraphActivity.sampling) {
                    PowerNoiseGraphActivity.this.getRawData(false);
                } else {
                    PowerNoiseGraphActivity.this.getRawData(true);
                }
                handler.sendMessage(handler.obtainMessage());
            }
        };
        this.sptimer.schedule(this.task, 0, (long) (period / sampling));
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (this.mChipsetName.contains("SDM845")) {
            Kernel.write(Kernel.MAGNT_SENSOR_RAW, "0");
            sensorUnregister();
        } else if (this.mGeoMagneticSensorName.contains("YAS") || TestCase.getEnabled(TestCase.IS_ASAHI_USING_SYSFS)) {
            Kernel.write(Kernel.MAGNT_SENSOR_RAW, "0");
        } else if (TestCase.getEnabled(TestCase.IS_USING_SENSOR_MANAGER_IN_POWER_NOISE)) {
            sensorUnregister();
        }
        if (this.sptimer != null) {
            this.sptimer.cancel();
        }
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    private void sensorRegister() {
        LtUtil.log_i(this.CLASS_NAME, "sensorRegister", "MAGNETIC");
        this.mSensorListener = new SensorListener();
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mMagneticSensor = this.mSensorManager.getDefaultSensor(2);
        this.mSensorManager.registerListener(this.mSensorListener, this.mMagneticSensor, 2);
    }

    private void sensorUnregister() {
        LtUtil.log_i(this.CLASS_NAME, "sensorUnregister", "MAGNETIC");
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this.mSensorListener);
        }
        this.mSensorManager = null;
        this.mSensorListener = null;
        this.mMagneticSensor = null;
    }

    /* access modifiers changed from: private */
    public void getRawData(boolean isSampling) {
        String str = Kernel.read(Kernel.MAGNT_SENSOR_RAW);
        boolean validRawData = false;
        if (str != null) {
            String[] RawDatas = str.split(",");
            if (RawDatas[0] == null) {
                RawDatas[0] = "0.0";
            }
            if (RawDatas[1] == null) {
                RawDatas[1] = "0.0";
            }
            if (RawDatas[2] == null) {
                RawDatas[2] = "0.0";
            }
            this.raw_data[0] = Float.valueOf(RawDatas[0]).floatValue();
            this.raw_data[1] = Float.valueOf(RawDatas[1]).floatValue();
            this.raw_data[2] = Float.valueOf(RawDatas[2]).floatValue();
            if (isSampling) {
                if (this.run_num >= sampling) {
                    this.retSelftest = standardDeviation(this.raw_datas);
                } else if (!(this.raw_data[0] == 0.0f || this.raw_data[1] == 0.0f || this.raw_data[2] == 0.0f)) {
                    this.raw_datas[this.run_num][0] = this.raw_data[0];
                    this.raw_datas[this.run_num][1] = this.raw_data[1];
                    this.raw_datas[this.run_num][2] = this.raw_data[2];
                    validRawData = true;
                }
            }
            if (validRawData) {
                this.run_num++;
            }
        }
    }

    private int standardDeviation(float[][] value) {
        float[][] fArr = value;
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.ENGLISH);
        df.applyPattern("0.000");
        float[] x = new float[fArr.length];
        float[] y = new float[fArr.length];
        float[] z = new float[fArr.length];
        int index = 0;
        for (int i = 0; i < fArr.length; i++) {
            if (!(fArr[i][0] == 0.0f || fArr[i][1] == 0.0f || fArr[i][2] == 0.0f)) {
                x[index] = fArr[i][0];
                y[index] = fArr[i][1];
                z[index] = fArr[i][2];
                index++;
            }
        }
        double dx = standardDeviation(x);
        double dy = standardDeviation(y);
        double dz = standardDeviation(z);
        this.power_noise[0] = df.format(dx);
        this.power_noise[1] = df.format(dy);
        this.power_noise[2] = df.format(dz);
        StringBuilder sb = new StringBuilder();
        sb.append("power noise x=");
        sb.append(this.power_noise[0]);
        sb.append(", y=");
        sb.append(this.power_noise[1]);
        sb.append(", z=");
        sb.append(this.power_noise[2]);
        LtUtil.log_d(this.CLASS_NAME, "standardDeviation", sb.toString());
        float[] fArr2 = x;
        if (dx > this.power_noise_spec_x || dy > this.power_noise_spec_y || dz > this.power_noise_spec_z) {
            return 0;
        }
        return 1;
    }

    public static double mean(float[] array) {
        double sum = 0.0d;
        for (float f : array) {
            sum += (double) f;
        }
        return sum / ((double) array.length);
    }

    public static double standardDeviation(float[] array) {
        if (array.length < 2) {
            return Double.NaN;
        }
        double sum = 0.0d;
        double meanValue = mean(array);
        for (float f : array) {
            double diff = ((double) f) - meanValue;
            sum += diff * diff;
        }
        return Math.sqrt(sum / ((double) array.length));
    }
}
