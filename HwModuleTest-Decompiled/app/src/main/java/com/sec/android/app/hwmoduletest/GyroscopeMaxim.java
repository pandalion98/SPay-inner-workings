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

public class GyroscopeMaxim extends BaseActivity {
    private String CLASS_NAME = "GyroscopeMaxim";
    /* access modifiers changed from: private */
    public int WHAT_UPDATE = 1;
    private String mFeature;
    private int mGyroSpec_Max;
    private int mGyroSpec_Min;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == GyroscopeMaxim.this.WHAT_UPDATE) {
                GyroscopeMaxim.this.update();
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
    private TextView mText_Temperature;

    public GyroscopeMaxim() {
        super("GyroscopeMaxim");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gyroscope_maxim);
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
        int i = -65536;
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
            sb4.append("HW Self Test(%) : ");
            sb4.append(data[7]);
            sb4.append(" , ");
            sb4.append(data[8]);
            sb4.append(" , ");
            sb4.append(data[9]);
            LtUtil.log_i(this.CLASS_NAME, "update", sb4.toString());
            this.mText_HW_Self_X.setText(data[7]);
            this.mText_HW_Self_Y.setText(data[8]);
            this.mText_HW_Self_Z.setText(data[9]);
            StringBuilder sb5 = new StringBuilder();
            sb5.append("ReturnValue : ");
            sb5.append(data[0]);
            LtUtil.log_i(this.CLASS_NAME, "update", sb5.toString());
            this.mGyroSpec_Min = Spec.getInt(Spec.GYROSCOPE_SELFTEST_MIN);
            this.mGyroSpec_Max = Spec.getInt(Spec.GYROSCOPE_SELFTEST_MAX);
            if (Feature.getBoolean(Feature.USE_DUAL_SENSOR_ACC)) {
                StringBuilder sb6 = new StringBuilder();
                sb6.append("GYROSCOPE_SELFTEST_");
                sb6.append(Kernel.getGyroSensorVendor());
                sb6.append("_MIN");
                this.mGyroSpec_Min = Spec.getInt(sb6.toString());
                StringBuilder sb7 = new StringBuilder();
                sb7.append("GYROSCOPE_SELFTEST_");
                sb7.append(Kernel.getGyroSensorVendor());
                sb7.append("_MAX");
                this.mGyroSpec_Max = Spec.getInt(sb7.toString());
            }
            if (1 != 1 || Float.parseFloat(data[1]) > ((float) this.mGyroSpec_Max) || Float.parseFloat(data[2]) > ((float) this.mGyroSpec_Max) || Float.parseFloat(data[3]) > ((float) this.mGyroSpec_Max) || Float.parseFloat(data[1]) < ((float) this.mGyroSpec_Min) || Float.parseFloat(data[2]) < ((float) this.mGyroSpec_Min) || Float.parseFloat(data[3]) < ((float) this.mGyroSpec_Min)) {
                StringBuilder sb8 = new StringBuilder();
                sb8.append("Noise Bias : ");
                sb8.append(data[1]);
                sb8.append(" , ");
                sb8.append(data[2]);
                sb8.append(" , ");
                sb8.append(data[3]);
                LtUtil.log_i(this.CLASS_NAME, "spec out", sb8.toString());
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
            TextView textView = this.mTextResult;
            if (isPass) {
                i = -16776961;
            }
            textView.setTextColor(i);
            return;
        }
        this.mText_Noise_Bias_X.setText("NONE");
        this.mText_Noise_Bias_Y.setText("NONE");
        this.mText_Noise_Bias_Z.setText("NONE");
        this.mTextResult.setText("FAIL");
        this.mTextResult.setTextColor(-65536);
    }
}
