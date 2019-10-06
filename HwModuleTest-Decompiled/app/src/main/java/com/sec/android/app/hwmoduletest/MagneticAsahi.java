package com.sec.android.app.hwmoduletest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TableRow;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.modules.ModuleSensor;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Kernel;
import egis.client.api.EgisFingerprint;

public class MagneticAsahi extends BaseActivity {
    /* access modifiers changed from: private */
    public int WHAT_UPDATE = 1;
    private String mFeature;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MagneticAsahi.this.WHAT_UPDATE) {
                MagneticAsahi.this.update();
            }
        }
    };
    private ModuleSensor mModuleSensor;
    private int[] mSenserID = null;
    private int mSensorID_ADC = this.mSensorID_None;
    private int mSensorID_DAC = this.mSensorID_None;
    private int mSensorID_Initialized = this.mSensorID_None;
    private int mSensorID_None = (ModuleSensor.ID_SCOPE_MIN - 1);
    private int mSensorID_Self = this.mSensorID_None;
    private int mSensorID_Temperature = this.mSensorID_None;
    private TableRow mTableRow_ADC;
    private TableRow mTableRow_DAC;
    private TableRow mTableRow_HX;
    private TableRow mTableRow_HY;
    private TableRow mTableRow_HZ;
    private TableRow mTableRow_Initialized;
    private TableRow mTableRow_Temp;
    private TextView mTextResult;
    private TextView mText_ADC_X;
    private TextView mText_ADC_Y;
    private TextView mText_ADC_Z;
    private TextView mText_DAC_X;
    private TextView mText_DAC_Y;
    private TextView mText_DAC_Z;
    private TextView mText_HX;
    private TextView mText_HY;
    private TextView mText_HZ;
    private TextView mText_Initialized;
    private TextView mText_Temp;

    public MagneticAsahi() {
        super("MagneticAsahi");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.magnetic_asahi);
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
        if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8963) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8963C)) {
            this.mSensorID_Initialized = ModuleSensor.ID_FILE____MAGNETIC_STATUS;
            this.mSensorID_ADC = ModuleSensor.ID_FILE____MAGNETIC_ADC;
            this.mSensorID_DAC = ModuleSensor.ID_FILE____MAGNETIC_DAC;
            this.mSensorID_Self = ModuleSensor.ID_FILE____MAGNETIC_SELF;
        } else if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911C) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09916C) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09918C)) {
            this.mSensorID_Initialized = ModuleSensor.ID_FILE____MAGNETIC_SELF;
            this.mSensorID_ADC = ModuleSensor.ID_FILE____MAGNETIC_SELF;
            this.mSensorID_DAC = ModuleSensor.ID_FILE____MAGNETIC_SELF;
            this.mSensorID_Self = ModuleSensor.ID_FILE____MAGNETIC_SELF;
        } else if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8975) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8963C_MANAGER)) {
            this.mSensorID_Initialized = ModuleSensor.ID_MANAGER_MAGNETIC_POWER_ON;
            this.mSensorID_DAC = ModuleSensor.ID_MANAGER_MAGNETIC_DAC;
            this.mSensorID_ADC = ModuleSensor.ID_MANAGER_MAGNETIC_ADC;
            this.mSensorID_Self = ModuleSensor.ID_MANAGER_MAGNETIC_SELF;
        } else {
            this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK8973);
        }
        this.mSenserID = new int[]{this.mSensorID_Initialized, this.mSensorID_Temperature, this.mSensorID_DAC, this.mSensorID_ADC, this.mSensorID_Self};
        this.mModuleSensor = ModuleSensor.instance(this);
        this.mModuleSensor.SensorOn(this.mSenserID);
        this.mHandler.sendEmptyMessageDelayed(this.WHAT_UPDATE, 500);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mModuleSensor.SensorOff();
    }

    private void init() {
        this.mTableRow_Initialized = (TableRow) findViewById(C0268R.C0269id.tablerow_initialized);
        this.mText_Initialized = (TextView) findViewById(C0268R.C0269id.initialized);
        this.mTableRow_Temp = (TableRow) findViewById(C0268R.C0269id.tablerow_temp);
        this.mText_Temp = (TextView) findViewById(C0268R.C0269id.temp);
        this.mTableRow_HX = (TableRow) findViewById(C0268R.C0269id.tablerow_hx);
        this.mText_HX = (TextView) findViewById(C0268R.C0269id.f18hx);
        this.mTableRow_HY = (TableRow) findViewById(C0268R.C0269id.tablerow_hy);
        this.mText_HY = (TextView) findViewById(C0268R.C0269id.f19hy);
        this.mTableRow_HZ = (TableRow) findViewById(C0268R.C0269id.tablerow_hz);
        this.mText_HZ = (TextView) findViewById(C0268R.C0269id.f20hz);
        this.mTableRow_DAC = (TableRow) findViewById(C0268R.C0269id.tablerow_dac);
        this.mText_DAC_X = (TextView) findViewById(C0268R.C0269id.dac_x);
        this.mText_DAC_Y = (TextView) findViewById(C0268R.C0269id.dac_y);
        this.mText_DAC_Z = (TextView) findViewById(C0268R.C0269id.dac_z);
        this.mTableRow_ADC = (TableRow) findViewById(C0268R.C0269id.tablerow_adc);
        this.mText_ADC_X = (TextView) findViewById(C0268R.C0269id.adc_x);
        this.mText_ADC_Y = (TextView) findViewById(C0268R.C0269id.adc_y);
        this.mText_ADC_Z = (TextView) findViewById(C0268R.C0269id.adc_z);
        this.mTextResult = (TextView) findViewById(C0268R.C0269id.result);
    }

    /* access modifiers changed from: private */
    public void update() {
        String retData;
        String retData2;
        LtUtil.log_e(this.CLASS_NAME, "update", null);
        String[] data = null;
        boolean isPass = true;
        if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911C) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09916C) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09918C)) {
            String tempdata = Kernel.read(Kernel.GEOMAGNETIC_SENSOR_SELFTEST);
            if (tempdata != null) {
                data = tempdata.split(",");
            }
        }
        if (this.mSensorID_None < this.mSensorID_Initialized) {
            this.mTableRow_Initialized.setVisibility(0);
            if (!this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911) && !this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911C) && !this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09916C) && !this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09918C)) {
                data = this.mModuleSensor.getData(this.mSensorID_Initialized);
            }
            if (data == null) {
                isPass = false;
                this.mText_Initialized.setText("NONE");
                LtUtil.log_e(this.CLASS_NAME, "update", "Initialized - Fail : null");
            } else if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911C) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09916C) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09918C)) {
                if (data[0].equals("0")) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "Initialized - Pass");
                    retData2 = EgisFingerprint.MAJOR_VERSION;
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "Initialized - Fail");
                    isPass = false;
                    retData2 = "-1";
                }
                this.mText_Initialized.setText(retData2);
                StringBuilder sb = new StringBuilder();
                sb.append("Initialized Return : ");
                sb.append(data[0]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb.toString());
            } else {
                if (data[2].equals(EgisFingerprint.MAJOR_VERSION)) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "Initialized - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "Initialized - Fail");
                    isPass = false;
                }
                this.mText_Initialized.setText(data[2]);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Initialized Return : ");
                sb2.append(data[2]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb2.toString());
            }
        }
        if (this.mSensorID_None < this.mSensorID_Temperature) {
            this.mTableRow_Temp.setVisibility(0);
            data = this.mModuleSensor.getData(this.mSensorID_Temperature);
            if (data != null) {
                if (data[1].equals("OK")) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "Temperature - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "Temperature - Fail");
                    isPass = false;
                }
                this.mText_Temp.setText(data[2]);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Retuen : ");
                sb3.append(data[1]);
                sb3.append(", Temperature : ");
                sb3.append(data[2]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb3.toString());
            } else {
                isPass = false;
                this.mText_Temp.setText("NONE");
                LtUtil.log_e(this.CLASS_NAME, "update", "Temperature - Fail : null");
            }
        }
        if (this.mSensorID_None < this.mSensorID_Self) {
            this.mTableRow_HX.setVisibility(0);
            this.mTableRow_HY.setVisibility(0);
            this.mTableRow_HZ.setVisibility(0);
            if (!this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911) && !this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911C) && !this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09916C) && !this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09918C)) {
                data = this.mModuleSensor.getData(this.mSensorID_Self);
            }
            if (data == null) {
                isPass = false;
                this.mText_HX.setText("NONE");
                this.mText_HY.setText("NONE");
                this.mText_HZ.setText("NONE");
                LtUtil.log_e(this.CLASS_NAME, "update", "HX/HY/HZ - Fail : null");
            } else if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911C) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09916C) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09918C)) {
                try {
                    if (data[1].equals("0")) {
                        LtUtil.log_i(this.CLASS_NAME, "update", "HX/HY/HZ - Pass");
                    } else {
                        LtUtil.log_e(this.CLASS_NAME, "update", "HX/HY/HZ - Fail");
                        isPass = false;
                    }
                    this.mText_HX.setText(data[2]);
                    this.mText_HY.setText(data[3]);
                    this.mText_HZ.setText(data[4]);
                } catch (NullPointerException ne) {
                    LtUtil.log_e(ne);
                }
            } else {
                if (data[1].equals("OK")) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "HX/HY/HZ - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "HX/HY/HZ - Fail");
                    isPass = false;
                }
                this.mText_HX.setText(data[2]);
                this.mText_HY.setText(data[3]);
                this.mText_HZ.setText(data[4]);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Retuen : ");
                sb4.append(data[1]);
                sb4.append(", HX:");
                sb4.append(data[2]);
                sb4.append(", HY:");
                sb4.append(data[3]);
                sb4.append(", HZ:");
                sb4.append(data[4]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb4.toString());
            }
        }
        if (this.mSensorID_None < this.mSensorID_DAC) {
            this.mTableRow_DAC.setVisibility(0);
            if (!this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911) && !this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911C) && !this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09916C) && !this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09918C)) {
                data = this.mModuleSensor.getData(this.mSensorID_DAC);
            }
            if (data == null) {
                isPass = false;
                this.mText_DAC_X.setText("NONE");
                this.mText_DAC_Y.setText("NONE");
                this.mText_DAC_Z.setText("NONE");
                LtUtil.log_e(this.CLASS_NAME, "update", "DAC - Fail : null");
            } else if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911C) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09916C) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09918C)) {
                try {
                    if (data[5].equals("0")) {
                        LtUtil.log_i(this.CLASS_NAME, "update", "DAC - Pass");
                        retData = EgisFingerprint.MAJOR_VERSION;
                    } else {
                        LtUtil.log_e(this.CLASS_NAME, "update", "DAC - Fail");
                        retData = "-1";
                        isPass = false;
                    }
                    this.mText_DAC_X.setText(retData);
                    this.mText_DAC_Y.setText("0");
                    this.mText_DAC_Z.setText("0");
                } catch (NullPointerException ne2) {
                    LtUtil.log_e(ne2);
                }
            } else {
                if (data[1].equals("OK")) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "DAC - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "DAC - Fail");
                    isPass = false;
                }
                this.mText_DAC_X.setText(data[2]);
                this.mText_DAC_Y.setText(data[3]);
                this.mText_DAC_Z.setText(data[4]);
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Retuen : ");
                sb5.append(data[1]);
                sb5.append(", [DAC]X:");
                sb5.append(data[2]);
                sb5.append(", Y:");
                sb5.append(data[3]);
                sb5.append(", Z:");
                sb5.append(data[4]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb5.toString());
            }
        }
        if (this.mSensorID_None < this.mSensorID_ADC) {
            this.mTableRow_ADC.setVisibility(0);
            if (!this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911) && !this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911C) && !this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09916C) && !this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09918C)) {
                data = this.mModuleSensor.getData(this.mSensorID_ADC);
            }
            if (data == null) {
                isPass = false;
                this.mText_ADC_X.setText("NONE");
                this.mText_ADC_Y.setText("NONE");
                this.mText_ADC_Z.setText("NONE");
                LtUtil.log_e(this.CLASS_NAME, "update", "ADC - Fail : null");
            } else if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09911C) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09916C) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_AK09918C)) {
                try {
                    if (data[6].equals("0")) {
                        LtUtil.log_i(this.CLASS_NAME, "update", "ADC - Pass");
                    } else {
                        LtUtil.log_e(this.CLASS_NAME, "update", "ADC - Fail");
                        isPass = false;
                    }
                    this.mText_ADC_X.setText(data[7]);
                    this.mText_ADC_Y.setText(data[8]);
                    this.mText_ADC_Z.setText(data[9]);
                } catch (NullPointerException ne3) {
                    LtUtil.log_e(ne3);
                }
            } else {
                if (data[1].equals("OK")) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "ADC - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "ADC - Fail");
                    isPass = false;
                }
                this.mText_ADC_X.setText(data[2]);
                this.mText_ADC_Y.setText(data[3]);
                this.mText_ADC_Z.setText(data[4]);
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Retuen : ");
                sb6.append(data[1]);
                sb6.append(", [ADC]X:");
                sb6.append(data[2]);
                sb6.append(", Y:");
                sb6.append(data[3]);
                sb6.append(", Z:");
                sb6.append(data[4]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb6.toString());
            }
        }
        this.mTextResult.setText(isPass ? "PASS" : "FAIL");
        this.mTextResult.setTextColor(isPass ? -16776961 : -65536);
        String str = this.CLASS_NAME;
        String str2 = "update";
        StringBuilder sb7 = new StringBuilder();
        sb7.append("Result:");
        sb7.append(isPass ? "PASS" : "FAIL");
        LtUtil.log_i(str, str2, sb7.toString());
    }
}
