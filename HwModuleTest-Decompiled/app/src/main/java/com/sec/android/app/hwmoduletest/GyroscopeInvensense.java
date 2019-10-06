package com.sec.android.app.hwmoduletest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TableRow;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.modules.ModuleSensor;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Spec;
import egis.client.api.EgisFingerprint;

public class GyroscopeInvensense extends BaseActivity {
    private String CLASS_NAME = "GyroscopeInvensense";
    /* access modifiers changed from: private */
    public int WHAT_UPDATE = 1;
    private String mFeature;
    private int mGyroSpec_Max;
    private int mGyroSpec_Min;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == GyroscopeInvensense.this.WHAT_UPDATE) {
                GyroscopeInvensense.this.update();
            }
        }
    };
    private ModuleSensor mModuleSensor;
    private int[] mSenserID = null;
    private TableRow mTableRow_Initialized;
    private TextView mTextResult;
    private TextView mText_HW_Self_X;
    private TextView mText_HW_Self_Y;
    private TextView mText_HW_Self_Z;
    private TextView mText_Initialized;
    private TextView mText_Noise_Bias_X;
    private TextView mText_Noise_Bias_Y;
    private TextView mText_Noise_Bias_Z;
    private TextView mText_Noise_Power_X;
    private TextView mText_Noise_Power_Y;
    private TextView mText_Noise_Power_Z;
    private TextView mText_Temperature;

    public GyroscopeInvensense() {
        super("GyroscopeInvensense");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gyroscope_invensense);
        this.mFeature = ModuleSensor.instance(this).mFeature_Gyroscope;
        StringBuilder sb = new StringBuilder();
        sb.append("mFeature : ");
        sb.append(this.mFeature);
        LtUtil.log_i(this.CLASS_NAME, "onCreate", sb.toString());
        init();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mModuleSensor = ModuleSensor.instance(this);
        this.mSenserID = new int[]{ModuleSensor.ID_FILE____GYRO_TEMPERATURE, ModuleSensor.ID_MANAGER_GYRO_SELF};
        this.mModuleSensor.SensorOn(this.mSenserID);
        this.mHandler.sendEmptyMessageDelayed(this.WHAT_UPDATE, 500);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mModuleSensor.SensorOff();
    }

    private void init() {
        this.mText_Initialized = (TextView) findViewById(C0268R.C0269id.initialized);
        this.mTableRow_Initialized = (TableRow) findViewById(C0268R.C0269id.tablerow_initialized);
        this.mText_Temperature = (TextView) findViewById(C0268R.C0269id.temperature);
        this.mText_Noise_Bias_X = (TextView) findViewById(C0268R.C0269id.noise_bias_x);
        this.mText_Noise_Bias_Y = (TextView) findViewById(C0268R.C0269id.noise_bias_y);
        this.mText_Noise_Bias_Z = (TextView) findViewById(C0268R.C0269id.noise_bias_z);
        this.mText_Noise_Power_X = (TextView) findViewById(C0268R.C0269id.noise_power_x);
        this.mText_Noise_Power_Y = (TextView) findViewById(C0268R.C0269id.noise_power_y);
        this.mText_Noise_Power_Z = (TextView) findViewById(C0268R.C0269id.noise_power_z);
        this.mText_HW_Self_X = (TextView) findViewById(C0268R.C0269id.hw_self_x);
        this.mText_HW_Self_Y = (TextView) findViewById(C0268R.C0269id.hw_self_y);
        this.mText_HW_Self_Z = (TextView) findViewById(C0268R.C0269id.hw_self_z);
        this.mTextResult = (TextView) findViewById(C0268R.C0269id.result);
        this.mTableRow_Initialized.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void update() {
        String feature = this.mModuleSensor.mFeature_Gyroscope;
        StringBuilder sb = new StringBuilder();
        sb.append("feature : ");
        sb.append(feature);
        LtUtil.log_i(this.CLASS_NAME, "update", sb.toString());
        String str = "";
        boolean isPass = true;
        if (feature.equals(ModuleSensor.FEATURE_GYROSCOP_INVENSENSE_MPU6050) || feature.equals(ModuleSensor.FEATURE_GYROSCOP_INVENSENSE_MPU6051) || feature.equals(ModuleSensor.FEATURE_GYROSCOP_INVENSENSE_MPU6051M)) {
            this.mText_Initialized.setText(EgisFingerprint.MAJOR_VERSION);
        }
        String[] data = this.mModuleSensor.getData(ModuleSensor.ID_FILE____GYRO_TEMPERATURE);
        if (data != null) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Temperature : ");
            sb2.append(data[2]);
            LtUtil.log_i(this.CLASS_NAME, "update", sb2.toString());
            this.mText_Temperature.setText(data[2]);
        } else {
            this.mText_Temperature.setText("NONE");
        }
        String tempdata = Kernel.read(Kernel.GYRO_SENSOR_SELFTEST);
        if (tempdata != null) {
            data = tempdata.split(",");
        }
        if (data != null) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Noise Bias : ");
            sb3.append(data[1]);
            sb3.append(" , ");
            sb3.append(data[2]);
            sb3.append(" , ");
            sb3.append(data[3]);
            LtUtil.log_i(this.CLASS_NAME, "update", sb3.toString());
            this.mText_Noise_Bias_X.setText(data[1]);
            this.mText_Noise_Bias_Y.setText(data[2]);
            this.mText_Noise_Bias_Z.setText(data[3]);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Noise Power(RMS) : ");
            sb4.append(data[4]);
            sb4.append(" , ");
            sb4.append(data[5]);
            sb4.append(" , ");
            sb4.append(data[6]);
            LtUtil.log_i(this.CLASS_NAME, "update", sb4.toString());
            this.mText_Noise_Power_X.setText(data[4]);
            this.mText_Noise_Power_Y.setText(data[5]);
            this.mText_Noise_Power_Z.setText(data[6]);
            StringBuilder sb5 = new StringBuilder();
            sb5.append("HW Self Test(%) : ");
            sb5.append(data[7]);
            sb5.append(" , ");
            sb5.append(data[8]);
            sb5.append(" , ");
            sb5.append(data[9]);
            LtUtil.log_i(this.CLASS_NAME, "update", sb5.toString());
            this.mText_HW_Self_X.setText(data[7]);
            this.mText_HW_Self_Y.setText(data[8]);
            this.mText_HW_Self_Z.setText(data[9]);
            StringBuilder sb6 = new StringBuilder();
            sb6.append("ReturnValue : ");
            sb6.append(data[0]);
            LtUtil.log_i(this.CLASS_NAME, "update", sb6.toString());
            if (!data[0].equals("0")) {
                isPass = false;
            }
            this.mGyroSpec_Min = Spec.getInt(Spec.GYROSCOPE_SELFTEST_MIN);
            this.mGyroSpec_Max = Spec.getInt(Spec.GYROSCOPE_SELFTEST_MAX);
            if (Feature.getBoolean(Feature.USE_DUAL_SENSOR_ACC)) {
                StringBuilder sb7 = new StringBuilder();
                sb7.append("GYROSCOPE_SELFTEST_");
                sb7.append(Kernel.getGyroSensorVendor());
                sb7.append("_MIN");
                this.mGyroSpec_Min = Spec.getInt(sb7.toString());
                StringBuilder sb8 = new StringBuilder();
                sb8.append("GYROSCOPE_SELFTEST_");
                sb8.append(Kernel.getGyroSensorVendor());
                sb8.append("_MAX");
                this.mGyroSpec_Max = Spec.getInt(sb8.toString());
            }
            if (!isPass || Float.parseFloat(data[1]) > ((float) this.mGyroSpec_Max) || Float.parseFloat(data[2]) > ((float) this.mGyroSpec_Max) || Float.parseFloat(data[3]) > ((float) this.mGyroSpec_Max) || Float.parseFloat(data[1]) < ((float) this.mGyroSpec_Min) || Float.parseFloat(data[2]) < ((float) this.mGyroSpec_Min) || Float.parseFloat(data[3]) < ((float) this.mGyroSpec_Min)) {
                isPass = false;
            }
            if (!isPass || Float.parseFloat(data[4]) > 5.0f || Float.parseFloat(data[5]) > 5.0f || Float.parseFloat(data[6]) > 5.0f || Float.parseFloat(data[4]) < -5.0f || Float.parseFloat(data[5]) < -5.0f || Float.parseFloat(data[6]) < -5.0f) {
                isPass = false;
            }
            if (isPass && (((float) 50) <= Float.parseFloat(data[7]) || ((float) 50) <= Float.parseFloat(data[8]) || ((float) 50) <= Float.parseFloat(data[9]))) {
                StringBuilder sb9 = new StringBuilder();
                sb9.append("HW Self Test : ");
                sb9.append(data[7]);
                sb9.append(" , ");
                sb9.append(data[8]);
                sb9.append(" , ");
                sb9.append(data[9]);
                LtUtil.log_i(this.CLASS_NAME, "spec out", sb9.toString());
                isPass = false;
            }
            this.mTextResult.setText(isPass ? "PASS" : "FAIL");
            this.mTextResult.setTextColor(isPass ? -16776961 : -65536);
            return;
        }
        this.mText_Noise_Bias_X.setText("NONE");
        this.mText_Noise_Bias_Y.setText("NONE");
        this.mText_Noise_Bias_Z.setText("NONE");
        this.mText_Noise_Power_X.setText("NONE");
        this.mText_Noise_Power_Y.setText("NONE");
        this.mText_Noise_Power_Z.setText("NONE");
        this.mTextResult.setText("FAIL");
        this.mTextResult.setTextColor(-65536);
    }
}
