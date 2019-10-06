package com.sec.android.app.hwmoduletest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TableRow;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.modules.ModuleSensor;
import com.sec.android.app.hwmoduletest.modules.SensorCalculator;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Kernel;
import egis.client.api.EgisFingerprint;

public class MagneticYamaha extends BaseActivity {
    private final int WHAT_UPDATE = 1;
    private String mFeature;
    private final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                MagneticYamaha.this.update();
            }
        }
    };
    private ModuleSensor mModuleSensor;
    private int[] mSenserID = null;
    private int mSensorID_ADC = this.mSensorID_None;
    private int mSensorID_DAC = this.mSensorID_None;
    private int mSensorID_Initialized = this.mSensorID_None;
    private int mSensorID_None = (ModuleSensor.ID_SCOPE_MIN - 1);
    private int mSensorID_Offset_H = this.mSensorID_None;
    private int mSensorID_Released = this.mSensorID_None;
    private int mSensorID_Self = this.mSensorID_None;
    private int mSensorID_Status = this.mSensorID_None;
    private TableRow mTableRow_ADC;
    private TableRow mTableRow_DAC;
    private TableRow mTableRow_Initialized;
    private TableRow mTableRow_Offset_H;
    private TableRow mTableRow_SX;
    private TableRow mTableRow_SY;
    private TableRow mTableRow_SY2;
    private TableRow mTableRow_Temp;
    private TextView mTextResult;
    private TextView mText_ADC_X;
    private TextView mText_ADC_Y;
    private TextView mText_ADC_Z;
    private TextView mText_DAC_X;
    private TextView mText_DAC_Y;
    private TextView mText_DAC_Z;
    private TextView mText_Initialized;
    private TextView mText_Offset_H_Title;
    private TextView mText_Offset_H_X;
    private TextView mText_Offset_H_Y;
    private TextView mText_Offset_H_Z;
    private TextView mText_SX;
    private TextView mText_SY;
    private TextView mText_SY2;
    private TextView mText_Temp;

    public MagneticYamaha() {
        super("MagneticYamaha");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.magnetic_yamaha);
        this.mFeature = ModuleSensor.instance(this).mFeature_Magnetic;
        StringBuilder sb = new StringBuilder();
        sb.append("mFeature : ");
        sb.append(this.mFeature);
        LtUtil.log_i(this.CLASS_NAME, "onCreate", sb.toString());
        init();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mSenserID = new int[]{this.mSensorID_Initialized, this.mSensorID_Status, this.mSensorID_DAC, this.mSensorID_ADC, this.mSensorID_Self, this.mSensorID_Offset_H, this.mSensorID_Released};
        this.mModuleSensor = ModuleSensor.instance(this);
        this.mModuleSensor.SensorOn(this.mSenserID);
        this.mHandler.sendEmptyMessageDelayed(1, 500);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mModuleSensor.SensorOff();
    }

    private void init() {
        if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS530C) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS532B) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS532) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS537) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS539)) {
            this.mSensorID_Initialized = ModuleSensor.ID_MANAGER_MAGNETIC_POWER_ON;
            this.mSensorID_Status = ModuleSensor.ID_MANAGER_MAGNETIC_STATUS;
            this.mSensorID_DAC = ModuleSensor.ID_MANAGER_MAGNETIC_DAC;
            this.mSensorID_ADC = ModuleSensor.ID_MANAGER_MAGNETIC_ADC;
            this.mSensorID_Self = ModuleSensor.ID_MANAGER_MAGNETIC_SELF;
            this.mSensorID_Offset_H = ModuleSensor.ID_MANAGER_MAGNETIC_OFFSETH;
            this.mSensorID_Released = ModuleSensor.ID_MANAGER_MAGNETIC_POWER_OFF;
        }
        this.mTableRow_Initialized = (TableRow) findViewById(C0268R.C0269id.tablerow_initialized);
        this.mText_Initialized = (TextView) findViewById(C0268R.C0269id.initialized);
        this.mTableRow_SX = (TableRow) findViewById(C0268R.C0269id.tablerow_sx);
        this.mText_SX = (TextView) findViewById(C0268R.C0269id.f21sx);
        this.mTableRow_SY = (TableRow) findViewById(C0268R.C0269id.tablerow_sy);
        this.mText_SY = (TextView) findViewById(C0268R.C0269id.f22sy);
        this.mTableRow_SY2 = (TableRow) findViewById(C0268R.C0269id.tablerow_sy2);
        this.mText_SY2 = (TextView) findViewById(C0268R.C0269id.sy2);
        this.mTableRow_DAC = (TableRow) findViewById(C0268R.C0269id.tablerow_dac);
        this.mText_DAC_X = (TextView) findViewById(C0268R.C0269id.dac_x);
        this.mText_DAC_Y = (TextView) findViewById(C0268R.C0269id.dac_y);
        this.mText_DAC_Z = (TextView) findViewById(C0268R.C0269id.dac_z);
        this.mTableRow_ADC = (TableRow) findViewById(C0268R.C0269id.tablerow_adc);
        this.mText_ADC_X = (TextView) findViewById(C0268R.C0269id.adc_x);
        this.mText_ADC_Y = (TextView) findViewById(C0268R.C0269id.adc_y);
        this.mText_ADC_Z = (TextView) findViewById(C0268R.C0269id.adc_z);
        this.mTableRow_Offset_H = (TableRow) findViewById(C0268R.C0269id.tablerow_offset_h);
        this.mText_Offset_H_Title = (TextView) findViewById(C0268R.C0269id.offset_h_title);
        this.mText_Offset_H_X = (TextView) findViewById(C0268R.C0269id.offset_h_x);
        this.mText_Offset_H_Y = (TextView) findViewById(C0268R.C0269id.offset_h_y);
        this.mText_Offset_H_Z = (TextView) findViewById(C0268R.C0269id.offset_h_z);
        this.mTextResult = (TextView) findViewById(C0268R.C0269id.result);
    }

    /* access modifiers changed from: private */
    public void update() {
        String PowerOnResult;
        LtUtil.log_d(this.CLASS_NAME, "update", null);
        int isYamaha539 = this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS539);
        String[] data = null;
        boolean isPass = true;
        String tempdata = Kernel.read(Kernel.GEOMAGNETIC_SENSOR_SELFTEST);
        if (tempdata != null) {
            data = tempdata.split(",");
        }
        if (data != null) {
            this.mTableRow_Initialized.setVisibility(0);
            if (data[0].equals("0")) {
                LtUtil.log_i(this.CLASS_NAME, "update", "Initialized - Pass");
                PowerOnResult = EgisFingerprint.MAJOR_VERSION;
            } else {
                LtUtil.log_e(this.CLASS_NAME, "update", "Initialized - Fail");
                isPass = false;
                PowerOnResult = "0";
            }
            this.mText_Initialized.setText(PowerOnResult);
            StringBuilder sb = new StringBuilder();
            sb.append("Initialized Return : ");
            sb.append(PowerOnResult);
            LtUtil.log_i(this.CLASS_NAME, "update", sb.toString());
            if (data[2].equals("0")) {
                LtUtil.log_i(this.CLASS_NAME, "update", "Status - Pass");
            } else {
                LtUtil.log_e(this.CLASS_NAME, "update", "Status - Fail");
                isPass = false;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Return : ");
            sb2.append(data[2]);
            LtUtil.log_i(this.CLASS_NAME, "update", sb2.toString());
            if (SensorCalculator.getmRangeMagneticDAC() != null) {
                this.mTableRow_DAC.setVisibility(0);
                if (data[3].equals("0")) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "DAC - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "DAC - Fail");
                    isPass = false;
                }
                this.mText_DAC_X.setText(data[4]);
                this.mText_DAC_Y.setText(data[5]);
                this.mText_DAC_Z.setText(data[6]);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Return : ");
                sb3.append(data[3]);
                sb3.append(", [DAC]X:");
                sb3.append(data[4]);
                sb3.append(", Y:");
                sb3.append(data[5]);
                sb3.append(", Z:");
                sb3.append(data[6]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb3.toString());
            }
            if (SensorCalculator.getmRangeMagneticADC() != null) {
                this.mTableRow_ADC.setVisibility(0);
                if (data[7].equals("0")) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "ADC - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "ADC - Fail");
                    isPass = false;
                }
                this.mText_ADC_X.setText(data[8]);
                this.mText_ADC_Y.setText("0");
                this.mText_ADC_Z.setText("0");
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Return : ");
                sb4.append(data[7]);
                sb4.append(", [ADC]X:");
                sb4.append(data[8]);
                sb4.append(", Y:");
                sb4.append(0);
                sb4.append(", Z:");
                sb4.append(0);
                LtUtil.log_i(this.CLASS_NAME, "update", sb4.toString());
            }
            if (SensorCalculator.getmRangeMagneticSelf() != null) {
                this.mTableRow_SX.setVisibility(0);
                this.mTableRow_SY.setVisibility(0);
                if (data[9].equals("0")) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "Self - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "Self - Fail");
                    isPass = false;
                }
                this.mText_SX.setText(data[10]);
                this.mText_SY.setText(data[11]);
                if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS539)) {
                    this.mTableRow_SY2.setVisibility(0);
                    this.mText_SY2.setText(data[12]);
                }
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Return : ");
                sb5.append(data[9]);
                sb5.append(", SX:");
                sb5.append(data[10]);
                sb5.append(", SY:");
                sb5.append(data[11]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb5.toString());
            }
            if (SensorCalculator.getmRangeMagneticADC2() != null) {
                this.mTableRow_Offset_H.setVisibility(0);
                if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS537) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_YAS539)) {
                    this.mText_Offset_H_Title.setText("ADC2");
                }
                if (data[12 + isYamaha539].equals("0")) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "Offset - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "Offset - Fail");
                    isPass = false;
                }
                this.mText_Offset_H_X.setText(data[13 + isYamaha539]);
                this.mText_Offset_H_Y.setText(data[14 + isYamaha539]);
                this.mText_Offset_H_Z.setText(data[15 + isYamaha539]);
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Return : ");
                sb6.append(data[12 + isYamaha539]);
                sb6.append(", [Offset H]X:");
                sb6.append(data[13 + isYamaha539]);
                sb6.append(", Y:");
                sb6.append(data[14 + isYamaha539]);
                sb6.append(", Z:");
                sb6.append(data[15 + isYamaha539]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb6.toString());
            }
            if (data[true + ((int) isYamaha539)].equals("0")) {
                LtUtil.log_i(this.CLASS_NAME, "update", "Released - Pass");
            } else {
                LtUtil.log_e(this.CLASS_NAME, "update", "Released - Fail");
                isPass = false;
            }
            StringBuilder sb7 = new StringBuilder();
            sb7.append("Released Return : ");
            sb7.append(data[16]);
            LtUtil.log_i(this.CLASS_NAME, "update", sb7.toString());
        } else {
            isPass = false;
            this.mText_Initialized.setText("NONE");
            LtUtil.log_e(this.CLASS_NAME, "update", "Initialized - Fail : null");
            this.mText_DAC_X.setText("NONE");
            this.mText_DAC_Y.setText("NONE");
            this.mText_DAC_Z.setText("NONE");
            LtUtil.log_e(this.CLASS_NAME, "update", "DAC - Fail : null");
            this.mText_ADC_X.setText("NONE");
            this.mText_ADC_Y.setText("NONE");
            this.mText_ADC_Z.setText("NONE");
            LtUtil.log_e(this.CLASS_NAME, "update", "ADC - Fail : null");
            this.mText_SX.setText("NONE");
            this.mText_SY.setText("NONE");
            LtUtil.log_e(this.CLASS_NAME, "update", "Self - Fail : null");
            this.mText_Offset_H_X.setText("NONE");
            this.mText_Offset_H_Y.setText("NONE");
            this.mText_Offset_H_Z.setText("NONE");
            LtUtil.log_i(this.CLASS_NAME, "update", "Offset - Fail : null");
            LtUtil.log_e(this.CLASS_NAME, "update", "Released - Fail : null");
        }
        this.mTextResult.setText(isPass ? "PASS" : "FAIL");
        this.mTextResult.setTextColor(isPass ? -16776961 : -65536);
        String str = this.CLASS_NAME;
        String str2 = "update";
        StringBuilder sb8 = new StringBuilder();
        sb8.append("Result:");
        sb8.append(isPass ? "PASS" : "FAIL");
        LtUtil.log_i(str, str2, sb8.toString());
    }
}
