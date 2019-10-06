package com.sec.android.app.hwmoduletest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TableRow;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.modules.ModuleSensor;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import egis.client.api.EgisFingerprint;

public class MagneticSTMicro extends BaseActivity {
    /* access modifiers changed from: private */
    public int WHAT_UPDATE = 1;
    private String mFeature;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MagneticSTMicro.this.WHAT_UPDATE) {
                MagneticSTMicro.this.update();
            }
        }
    };
    private ModuleSensor mModuleSensor;
    private int[] mSenserID = null;
    private int mSensorID_ADC = this.mSensorID_None;
    private int mSensorID_DAC = this.mSensorID_None;
    private int mSensorID_Initialized = this.mSensorID_None;
    private int mSensorID_None = (ModuleSensor.ID_SCOPE_MIN - 1);
    private int mSensorID_Released = this.mSensorID_None;
    private int mSensorID_Self = this.mSensorID_None;
    private int mSensorID_Status = this.mSensorID_None;
    private TableRow mTableRow_ADC;
    private TableRow mTableRow_DAC;
    private TableRow mTableRow_Initialized;
    private TableRow mTableRow_Offset_H;
    private TableRow mTableRow_SX;
    private TableRow mTableRow_SY;
    private TableRow mTableRow_SZ;
    private TableRow mTableRow_Status;
    private TableRow mTableRow_Temp;
    private TextView mTextResult;
    private TextView mText_ADC_X;
    private TextView mText_ADC_Y;
    private TextView mText_ADC_Z;
    private TextView mText_DAC_X;
    private TextView mText_DAC_Y;
    private TextView mText_DAC_Z;
    private TextView mText_Initialized;
    private TextView mText_Offset_H_X;
    private TextView mText_Offset_H_Y;
    private TextView mText_Offset_H_Z;
    private TextView mText_SX;
    private TextView mText_SY;
    private TextView mText_SZ;
    private TextView mText_Status;
    private TextView mText_Temp;

    public MagneticSTMicro() {
        super("MagneticSTMicro");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.magnetic_stmicro);
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
        if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_STMICRO) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_STMICRO_LSM303AH)) {
            this.mSensorID_Initialized = ModuleSensor.ID_MANAGER_MAGNETIC_POWER_ON;
            this.mSensorID_Status = ModuleSensor.ID_FILE____MAGNETIC_STATUS;
            this.mSensorID_ADC = ModuleSensor.ID_FILE____MAGNETIC_ADC;
            this.mSensorID_Self = ModuleSensor.ID_FILE____MAGNETIC_SELF;
        }
        this.mSenserID = new int[]{this.mSensorID_Initialized, this.mSensorID_Status, this.mSensorID_ADC, this.mSensorID_Self};
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
        this.mTableRow_Status = (TableRow) findViewById(C0268R.C0269id.tablerow_status);
        this.mText_Status = (TextView) findViewById(C0268R.C0269id.status);
        this.mTableRow_SX = (TableRow) findViewById(C0268R.C0269id.tablerow_sx);
        this.mText_SX = (TextView) findViewById(C0268R.C0269id.f21sx);
        this.mTableRow_SY = (TableRow) findViewById(C0268R.C0269id.tablerow_sy);
        this.mText_SY = (TextView) findViewById(C0268R.C0269id.f22sy);
        this.mTableRow_SZ = (TableRow) findViewById(C0268R.C0269id.tablerow_sz);
        this.mText_SZ = (TextView) findViewById(C0268R.C0269id.f23sz);
        this.mTableRow_DAC = (TableRow) findViewById(C0268R.C0269id.tablerow_dac);
        this.mText_DAC_X = (TextView) findViewById(C0268R.C0269id.dac_x);
        this.mText_DAC_Y = (TextView) findViewById(C0268R.C0269id.dac_y);
        this.mText_DAC_Z = (TextView) findViewById(C0268R.C0269id.dac_z);
        this.mTableRow_ADC = (TableRow) findViewById(C0268R.C0269id.tablerow_adc);
        this.mText_ADC_X = (TextView) findViewById(C0268R.C0269id.adc_x);
        this.mText_ADC_Y = (TextView) findViewById(C0268R.C0269id.adc_y);
        this.mText_ADC_Z = (TextView) findViewById(C0268R.C0269id.adc_z);
        this.mTableRow_Offset_H = (TableRow) findViewById(C0268R.C0269id.tablerow_offset_h);
        this.mText_Offset_H_X = (TextView) findViewById(C0268R.C0269id.offset_h_x);
        this.mText_Offset_H_Y = (TextView) findViewById(C0268R.C0269id.offset_h_y);
        this.mText_Offset_H_Z = (TextView) findViewById(C0268R.C0269id.offset_h_z);
        this.mTextResult = (TextView) findViewById(C0268R.C0269id.result);
    }

    /* access modifiers changed from: private */
    public void update() {
        LtUtil.log_e(this.CLASS_NAME, "update", null);
        boolean isPass = true;
        if (this.mSensorID_None < this.mSensorID_Initialized) {
            this.mTableRow_Initialized.setVisibility(0);
            String[] data = this.mModuleSensor.getData(this.mSensorID_Initialized);
            if (data != null) {
                if (data[2].equals(EgisFingerprint.MAJOR_VERSION)) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "Initialized - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "Initialized - Fail");
                    isPass = false;
                }
                this.mText_Initialized.setText(data[2]);
                StringBuilder sb = new StringBuilder();
                sb.append("Initialized Return : ");
                sb.append(data[2]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb.toString());
            } else {
                isPass = false;
                this.mText_Initialized.setText("NONE");
                LtUtil.log_e(this.CLASS_NAME, "update", "Initialized - Fail : null");
            }
        }
        if (this.mSensorID_None < this.mSensorID_Status) {
            this.mTableRow_Status.setVisibility(0);
            String[] data2 = this.mModuleSensor.getData(this.mSensorID_Status);
            if (data2 != null) {
                if (data2[1].equals("OK")) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "Status - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "Status - Fail");
                    isPass = false;
                }
                this.mText_Status.setText(data2[2]);
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Retuen : ");
                sb2.append(data2[1]);
                sb2.append(", Status : ");
                sb2.append(data2[2]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb2.toString());
            } else {
                isPass = false;
                this.mText_Status.setText("NONE");
                LtUtil.log_e(this.CLASS_NAME, "update", "Status - Fail : null");
            }
        }
        if (this.mSensorID_None < this.mSensorID_DAC) {
            this.mTableRow_DAC.setVisibility(0);
            String[] data3 = this.mModuleSensor.getData(this.mSensorID_DAC);
            if (data3 != null) {
                if (data3[1].equals("OK")) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "DAC - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "DAC - Fail");
                    isPass = false;
                }
                this.mText_DAC_X.setText(data3[2]);
                this.mText_DAC_Y.setText(data3[3]);
                this.mText_DAC_Z.setText(data3[4]);
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Retuen : ");
                sb3.append(data3[1]);
                sb3.append(", [DAC]X:");
                sb3.append(data3[2]);
                sb3.append(", Y:");
                sb3.append(data3[3]);
                sb3.append(", Z:");
                sb3.append(data3[4]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb3.toString());
            } else {
                isPass = false;
                this.mText_DAC_X.setText("NONE");
                this.mText_DAC_Y.setText("NONE");
                this.mText_DAC_Z.setText("NONE");
                LtUtil.log_e(this.CLASS_NAME, "update", "DAC - Fail : null");
            }
        }
        if (this.mSensorID_None < this.mSensorID_ADC) {
            this.mTableRow_ADC.setVisibility(0);
            String[] data4 = this.mModuleSensor.getData(this.mSensorID_ADC);
            if (data4 != null) {
                if (data4[1].equals("OK")) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "ADC - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "ADC - Fail");
                    isPass = false;
                }
                this.mText_ADC_X.setText(data4[2]);
                this.mText_ADC_Y.setText(data4[3]);
                this.mText_ADC_Z.setText(data4[4]);
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Retuen : ");
                sb4.append(data4[1]);
                sb4.append(", [ADC]X:");
                sb4.append(data4[2]);
                sb4.append(", Y:");
                sb4.append(data4[3]);
                sb4.append(", Z:");
                sb4.append(data4[4]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb4.toString());
            } else {
                isPass = false;
                this.mText_ADC_X.setText("NONE");
                this.mText_ADC_Y.setText("NONE");
                this.mText_ADC_Z.setText("NONE");
                LtUtil.log_e(this.CLASS_NAME, "update", "ADC - Fail : null");
            }
        }
        if (this.mSensorID_None < this.mSensorID_Self) {
            this.mTableRow_SX.setVisibility(0);
            this.mTableRow_SY.setVisibility(0);
            this.mTableRow_SZ.setVisibility(0);
            String[] data5 = this.mModuleSensor.getData(this.mSensorID_Self);
            if (data5 != null) {
                if (data5[1].equals("OK")) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Self - Pass (data[0] : ");
                    sb5.append(data5[1]);
                    sb5.append(")");
                    LtUtil.log_i(this.CLASS_NAME, "update", sb5.toString());
                } else {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Self - Fail (data[0] : ");
                    sb6.append(data5[1]);
                    sb6.append(")");
                    LtUtil.log_e(this.CLASS_NAME, "update", sb6.toString());
                    isPass = false;
                }
                this.mText_SX.setText(data5[2]);
                this.mText_SY.setText(data5[3]);
                this.mText_SZ.setText(data5[4]);
                StringBuilder sb7 = new StringBuilder();
                sb7.append("Retuen : ");
                sb7.append(data5[1]);
                sb7.append(", SX:");
                sb7.append(data5[2]);
                sb7.append(", SY:");
                sb7.append(data5[3]);
                sb7.append(", SZ:");
                sb7.append(data5[4]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb7.toString());
            } else {
                isPass = false;
                this.mText_SX.setText("NONE");
                this.mText_SY.setText("NONE");
                this.mText_SZ.setText("NONE");
                LtUtil.log_e(this.CLASS_NAME, "update", "Self - Fail : null");
            }
        }
        if (this.mSensorID_None < this.mSensorID_Released) {
            String[] data6 = this.mModuleSensor.getData(this.mSensorID_Released);
            if (data6 != null) {
                if (data6[2].equals(EgisFingerprint.MAJOR_VERSION)) {
                    LtUtil.log_i(this.CLASS_NAME, "update", "Released - Pass");
                } else {
                    LtUtil.log_e(this.CLASS_NAME, "update", "Released - Fail");
                    isPass = false;
                }
                StringBuilder sb8 = new StringBuilder();
                sb8.append("Released Return : ");
                sb8.append(data6[2]);
                LtUtil.log_i(this.CLASS_NAME, "update", sb8.toString());
            } else {
                isPass = false;
                LtUtil.log_e(this.CLASS_NAME, "update", "Released - Fail : null");
            }
        }
        this.mTextResult.setText(isPass ? "PASS" : "FAIL");
        this.mTextResult.setTextColor(isPass ? -16776961 : -65536);
        String str = this.CLASS_NAME;
        String str2 = "update";
        StringBuilder sb9 = new StringBuilder();
        sb9.append("Result:");
        sb9.append(isPass ? "PASS" : "FAIL");
        LtUtil.log_i(str, str2, sb9.toString());
    }
}
