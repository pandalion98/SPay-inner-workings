package com.sec.android.app.hwmoduletest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TableRow;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.modules.ModuleSensor;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.TestCase;
import egis.client.api.EgisFingerprint;

public class MagneticBosch extends BaseActivity {
    /* access modifiers changed from: private */
    public int WHAT_UPDATE = 1;
    private String mFeature;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MagneticBosch.this.WHAT_UPDATE) {
                MagneticBosch.this.update();
            }
        }
    };
    private ModuleSensor mModuleSensor;
    private String mReadTarget;
    private int[] mSenserID = null;
    private int mSensorID_ADC = this.mSensorID_None;
    private int mSensorID_Initialized = this.mSensorID_None;
    private int mSensorID_None = (ModuleSensor.ID_SCOPE_MIN - 1);
    private int mSensorID_Self = this.mSensorID_None;
    private TableRow mTableRow_ADC;
    private TableRow mTableRow_BmZ;
    private TableRow mTableRow_Initialized;
    private TextView mTextResult;
    private TextView mText_ADC_X;
    private TextView mText_ADC_Y;
    private TextView mText_ADC_Z;
    private TextView mText_BmZ;
    private TextView mText_Initialized;

    public MagneticBosch() {
        super("MagneticBosch");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.magnetic_bosch);
        this.mFeature = ModuleSensor.instance(this).mFeature_Magnetic;
        StringBuilder sb = new StringBuilder();
        sb.append("mFeature : ");
        sb.append(this.mFeature);
        LtUtil.log_i(this.CLASS_NAME, "onCreate", sb.toString());
        this.mReadTarget = TestCase.getString(TestCase.READ_TARGET_GEOMAGNETIC);
        StringBuilder sb2 = new StringBuilder();
        sb2.append("readTarget : [");
        sb2.append(this.mReadTarget);
        sb2.append("]");
        LtUtil.log_i(this.CLASS_NAME, "onCreate", sb2.toString());
        init();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_POWER_NOISE) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_COMBINATION) || this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_NEWEST)) {
            if (this.mReadTarget.equals("")) {
                if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_NEWEST)) {
                    this.mSensorID_Initialized = ModuleSensor.ID_FILE____MAGNETIC_SELF;
                    this.mSensorID_Self = ModuleSensor.ID_FILE____MAGNETIC_SELF;
                    this.mSensorID_ADC = ModuleSensor.ID_FILE____MAGNETIC_SELF;
                } else {
                    this.mSensorID_Initialized = ModuleSensor.ID_MANAGER_MAGNETIC_POWER_ON;
                    this.mSensorID_Self = ModuleSensor.ID_MANAGER_MAGNETIC_SELF;
                    this.mSensorID_ADC = ModuleSensor.ID_MANAGER_MAGNETIC_ADC;
                }
            } else if (this.mReadTarget.equals("MANAGER")) {
                this.mSensorID_Initialized = ModuleSensor.ID_MANAGER_MAGNETIC_POWER_ON;
                this.mSensorID_Self = ModuleSensor.ID_MANAGER_MAGNETIC_SELF;
                this.mSensorID_ADC = ModuleSensor.ID_MANAGER_MAGNETIC_ADC;
            } else if (this.mReadTarget.equals("FILE")) {
                this.mSensorID_Initialized = ModuleSensor.ID_FILE____MAGNETIC_POWER_ON;
                this.mSensorID_Self = ModuleSensor.ID_FILE____MAGNETIC_SELF;
                this.mSensorID_ADC = ModuleSensor.ID_FILE____MAGNETIC_ADC;
            }
        }
        this.mSenserID = new int[]{this.mSensorID_Initialized, this.mSensorID_Self, this.mSensorID_ADC};
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
        this.mTableRow_BmZ = (TableRow) findViewById(C0268R.C0269id.tablerow_bmz);
        this.mText_BmZ = (TextView) findViewById(C0268R.C0269id.bmz);
        this.mTableRow_ADC = (TableRow) findViewById(C0268R.C0269id.tablerow_adc);
        this.mText_ADC_X = (TextView) findViewById(C0268R.C0269id.adc_x);
        this.mText_ADC_Y = (TextView) findViewById(C0268R.C0269id.adc_y);
        this.mText_ADC_Z = (TextView) findViewById(C0268R.C0269id.adc_z);
        this.mTextResult = (TextView) findViewById(C0268R.C0269id.result);
    }

    /* access modifiers changed from: private */
    public void update() {
        LtUtil.log_e(this.CLASS_NAME, "update", null);
        boolean isPass = true;
        if (this.mFeature.equals(ModuleSensor.FEATURE_MAGENTIC_BMC150_NEWEST)) {
            this.mTableRow_Initialized.setVisibility(0);
            this.mTableRow_BmZ.setVisibility(0);
            this.mTableRow_ADC.setVisibility(0);
            String[] data = this.mModuleSensor.getData(this.mSensorID_Self);
            if (data != null) {
                if (data[0].equals("0")) {
                    this.mText_Initialized.setText(EgisFingerprint.MAJOR_VERSION);
                } else {
                    isPass = false;
                    this.mText_Initialized.setText("-1");
                }
                if (!data[5].equals("0")) {
                    isPass = false;
                }
                this.mText_BmZ.setText(data[1]);
                this.mText_ADC_X.setText(data[2]);
                this.mText_ADC_Y.setText(data[3]);
                this.mText_ADC_Z.setText(data[4]);
            } else {
                isPass = false;
                this.mText_Initialized.setText("NONE");
                this.mText_BmZ.setText("NONE");
                this.mText_ADC_X.setText("NONE");
                this.mText_ADC_Y.setText("NONE");
                this.mText_ADC_Z.setText("NONE");
            }
        } else {
            if (this.mSensorID_None < this.mSensorID_Initialized) {
                this.mTableRow_Initialized.setVisibility(0);
                String[] data2 = this.mModuleSensor.getData(this.mSensorID_Initialized);
                if (data2 != null) {
                    if (data2[2].equals(EgisFingerprint.MAJOR_VERSION)) {
                        LtUtil.log_i(this.CLASS_NAME, "update", "Initialized - Pass");
                    } else {
                        LtUtil.log_e(this.CLASS_NAME, "update", "Initialized - Fail");
                        isPass = false;
                    }
                    this.mText_Initialized.setText(data2[2]);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Initialized Return : ");
                    sb.append(data2[2]);
                    LtUtil.log_i(this.CLASS_NAME, "update", sb.toString());
                } else {
                    isPass = false;
                    this.mText_Initialized.setText("NONE");
                    LtUtil.log_e(this.CLASS_NAME, "update", "Initialized - Fail : null");
                }
            }
            if (this.mSensorID_None < this.mSensorID_Self) {
                this.mTableRow_BmZ.setVisibility(0);
                String[] data3 = this.mModuleSensor.getData(this.mSensorID_Self);
                if (data3 != null) {
                    if (data3[1].equals("OK")) {
                        LtUtil.log_i(this.CLASS_NAME, "update", "Self - Pass");
                    } else {
                        LtUtil.log_e(this.CLASS_NAME, "update", "Self - Fail");
                        isPass = false;
                    }
                    this.mText_BmZ.setText(data3[2]);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Retuen : ");
                    sb2.append(data3[1]);
                    sb2.append(", [Self]BmZ : ");
                    sb2.append(data3[2]);
                    LtUtil.log_i(this.CLASS_NAME, "update", sb2.toString());
                } else {
                    isPass = false;
                    this.mText_BmZ.setText("NONE");
                    LtUtil.log_e(this.CLASS_NAME, "update", "Self - Fail : null");
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
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Retuen : ");
                    sb3.append(data4[1]);
                    sb3.append(", [ADC]X:");
                    sb3.append(data4[2]);
                    sb3.append(", Y:");
                    sb3.append(data4[3]);
                    sb3.append(", Z:");
                    sb3.append(data4[4]);
                    LtUtil.log_i(this.CLASS_NAME, "update", sb3.toString());
                } else {
                    isPass = false;
                    this.mText_ADC_X.setText("NONE");
                    this.mText_ADC_Y.setText("NONE");
                    this.mText_ADC_Z.setText("NONE");
                    LtUtil.log_e(this.CLASS_NAME, "update", "ADC - Fail : null");
                }
            }
        }
        this.mTextResult.setText(isPass ? "PASS" : "FAIL");
        this.mTextResult.setTextColor(isPass ? -16776961 : -65536);
        String str = this.CLASS_NAME;
        String str2 = "update";
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Result:");
        sb4.append(isPass ? "PASS" : "FAIL");
        LtUtil.log_i(str, str2, sb4.toString());
    }
}
