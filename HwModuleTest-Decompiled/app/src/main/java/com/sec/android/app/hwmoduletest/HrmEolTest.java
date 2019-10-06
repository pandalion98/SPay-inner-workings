package com.sec.android.app.hwmoduletest;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.modules.ModuleDvfs;
import com.sec.android.app.hwmoduletest.modules.ModuleSensor;
import com.sec.android.app.hwmoduletest.sensors.SensorListener;
import com.sec.android.app.hwmoduletest.sensors.SensorUV;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Spec;
import egis.client.api.EgisFingerprint;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class HrmEolTest extends BaseActivity implements OnClickListener, SensorEventListener, SensorListener {
    public static boolean isDualSensorHRM = false;
    private final String CLASS_NAME = "HrmEolTest";
    private String[] HRM_spec_data;
    private final int IR_RED = 1;
    private String[] Measure;
    private String[] Result;
    private final String SENSOR_NAME_MAX86915 = "MAX86915";
    private final String SENSOR_NAME_MAX86917 = "MAX86917";

    /* renamed from: UV */
    private final int f14UV = 0;
    private final int WHAT_UPDATE_CLOUD_TEST = 2;
    private final int WHAT_UPDATE_STATUS = 1;
    private int[] dataCloud;
    /* access modifiers changed from: private */
    public boolean isCloudTest = false;
    private boolean isEolOn;
    private boolean isUvOn;
    private String[] itemName;
    private final String[] itemName_ADI = {"Frequency", "Red Low DC", "IR Low DC", "Red MID DC", "IR MID DC", "Red MID Squared Noise", "IR MID Squared Noise", "Red High DC", "IR High DC"};
    private final String[] itemName_ADI_OLD = {"IR Frequency", "IR AC Level", "IR DC Level", "IR Noise Level", "RED DC Level"};
    private final String[] itemName_MAXIM = {"P_IR AC Level", "P_IR DC Level", "P_IR AC/DC Ratio", "F_IR Frequency", "F_IR DC Level", "F_IR Noise Level", "P_RED AC Level", "P_RED DC Level", "P_RED AC/DC Ratio", "F_RED Frequency", "F_RED DC Level", "F_RED Noise Level"};
    private final String[] itemName_MAXIM_Cloud = {"Cloud UV Ratio", "IR/RED R Ratio"};
    private final String[] itemName_MAXIM_NEW = {"System Noise", "IR Low DC Level", "RED Low DC Level", "IR Mid DC Level", "RED Mid DC Level", "IR High DC Level", "RED High DC Level", "IR Noise Level", "RED Noise Level", "Frequency"};
    private final String[] itemName_MAXIM_SPO2 = {"SPO2"};
    private final String[] itemName_MAXIM_XTC = {"System Noise", "IR Low DC Level", "RED Low DC Level", "Green Low DC Level", "Blue Low DC Level", "IR Mid DC Level", "RED Mid DC Level", "Green Mid DC Level", "Blue Mid DC Level", "IR High DC Level", "RED High DC Level", "Green High DC Level", "Blue High DC Level", "IR X-talk Cancell DC Level", "Red X-talk Cancell DC Level", "Green X-talk Cancell DC Level", "Blue X-talk Cancell DC Level", "IR Noise Level", "RED Noise Level", "Green Noise Level", "Blue Noise Level", "Frequency"};
    private final String[] itemName_PARTRON = {"IR Frequency", "IR Low DC", "IR HI DC", "IR AC", "IR AC/DC Ratio", "IR Noise", "RED Low DC", "RED HI DC", "RED AC", "RED AC/DC Ratio", "RED Noise", "IR/RED R Ratio"};
    /* access modifiers changed from: private */
    public final boolean[] itemUsable_MAXIM_Cloud = {false, false};
    private Sensor mBIOSensor;
    private String[] mBufferCloud;
    private Button mButton_Exit;
    /* access modifiers changed from: private */
    public Button mButton_Start;
    /* access modifiers changed from: private */
    public boolean mCloud_Status = false;
    private ModuleDvfs mDvfs = null;
    private DecimalFormat mFormat;
    private Sensor mHRMSensor;
    /* access modifiers changed from: private */
    public final Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (!HrmEolTest.this.mhtm_Status) {
                    HrmEolTest.this.checkStatus();
                    HrmEolTest.this.mHandler.sendEmptyMessageDelayed(1, 100);
                    return;
                }
                HrmEolTest.this.mhtm_Status = false;
                HrmEolTest.this.registerSysfs(false);
                if (!"PARTRON".equals(HrmEolTest.this.mHrm_Vendor)) {
                    HrmEolTest.this.HRM_OnOff(false);
                }
                if (!HrmEolTest.this.isCloudTest || !HrmEolTest.this.itemUsable_MAXIM_Cloud[0]) {
                    HrmEolTest.this.mButton_Start.setEnabled(true);
                    HrmEolTest.this.updateUI();
                    return;
                }
                HrmEolTest.this.runCloudTest();
            } else if (msg.what != 2) {
            } else {
                if (!HrmEolTest.this.mCloud_Status) {
                    HrmEolTest.this.checkCloudValue();
                    HrmEolTest.this.mHandler.sendEmptyMessageDelayed(2, 100);
                    return;
                }
                HrmEolTest.this.registerSysfs_UV(false);
                HrmEolTest.this.UV_OnOff(false);
                HrmEolTest.this.mCloud_Status = false;
                HrmEolTest.this.StoreCloudValue();
                HrmEolTest.this.mButton_Start.setEnabled(true);
                HrmEolTest.this.updateUI();
            }
        }
    };
    private String mHrm_Name;
    /* access modifiers changed from: private */
    public String mHrm_Vendor;
    private SensorManager mSensorManager;
    private boolean mSpecResult = true;
    private TableRow mTableRow_Initialized;
    private TextView mTextResult;
    private TextView mText_Frequency_DC_IR;
    private TextView mText_Frequency_DC_RED;
    private TextView mText_Frequency_DC_Result;
    private TextView mText_Frequency_Noise_IR;
    private TextView mText_Frequency_Noise_RED;
    private TextView mText_Frequency_Noise_Result;
    private TextView mText_Frequency_Result;
    private TextView mText_Frequency_Sample_IR;
    private TextView mText_Frequency_Sample_RED;
    private TextView mText_Hr_Spo2;
    private TextView mText_Initialized;
    private TextView mText_Peak_DC_IR;
    private TextView mText_Peak_DC_RED;
    private TextView mText_Peak_DC_Result;
    private TextView mText_Peak_Peak_IR;
    private TextView mText_Peak_Peak_RED;
    private TextView mText_Peak_Ratio_IR;
    private TextView mText_Peak_Ratio_RED;
    private TextView mText_Peak_Ratio_Result;
    private TextView mText_Peak_result;
    private TextView mText_Temperature;
    private DecimalFormat mThreeFormat;
    private SensorUV mUVSensor;
    /* access modifiers changed from: private */
    public boolean mhtm_Status = false;
    private String partType = EgisFingerprint.MAJOR_VERSION;
    private int partTypeInt = 100;
    private TextView[] txt_itemName;
    private TextView[] txt_measure;
    private TextView[] txt_result;
    private final ProgressDialog waitPopup = null;

    public HrmEolTest() {
        super("UIHeartRateMeasure");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.hrm_eoltest);
        DecimalFormatSymbols paramDecimalFormatSymbols = new DecimalFormatSymbols();
        paramDecimalFormatSymbols.setDecimalSeparator('.');
        this.mFormat = new DecimalFormat("#.##", paramDecimalFormatSymbols);
        this.mThreeFormat = new DecimalFormat("#.###", paramDecimalFormatSymbols);
        try {
            this.partType = Kernel.read(Kernel.HRM_PART_TYPE);
            if (this.partType != null) {
                this.partTypeInt = Integer.parseInt(this.partType);
                isDualSensorHRM = Feature.getBoolean(Feature.USE_DUAL_SENSOR_HRM) && this.partTypeInt < 4;
            }
            this.mHrm_Vendor = Kernel.read(Kernel.HRM_VENDOR);
            if (this.mHrm_Vendor != null) {
                this.mHrm_Vendor = this.mHrm_Vendor.toUpperCase(Locale.ENGLISH);
            }
            StringBuilder sb = new StringBuilder();
            sb.append("VENDOR : ");
            sb.append(this.mHrm_Vendor);
            LtUtil.log_i("HrmEolTest", "onCreate", sb.toString());
            this.mHrm_Name = Kernel.read(Kernel.HRM_SENSOR_NAME);
            if (this.mHrm_Name != null) {
                this.mHrm_Name = this.mHrm_Name.toUpperCase(Locale.ENGLISH);
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("NAME : ");
            sb2.append(this.mHrm_Name);
            LtUtil.log_i("HrmEolTest", "onCreate", sb2.toString());
            if ("OSRAM".equalsIgnoreCase(this.mHrm_Vendor)) {
                this.mHrm_Vendor = ModuleSensor.FEATURE_GYROSCOP_MAXIM;
                LtUtil.log_i("HrmEolTest", "onCreate", "Change the HRM vendor name(OSRAM) to MAXIM temporary");
            }
            if ("SFH7832".equalsIgnoreCase(this.mHrm_Name)) {
                this.mHrm_Name = "MAX86915";
                LtUtil.log_i("HrmEolTest", "onCreate", "Change the HRM chip name(SFH7832) to MAX86915 temporary");
            }
        } catch (NullPointerException ne) {
            LtUtil.log_e(ne);
        }
        setPreCondition();
        readTestSpec();
        initUI();
        this.mDvfs = new ModuleDvfs(this);
        this.mDvfs.initCpuBoost();
        this.isUvOn = false;
        this.isEolOn = false;
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LtUtil.log_i("HrmEolTest", "onResume", "CPU LOCK acquire ready");
        this.mDvfs.setCpuBoost();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        if (this.isEolOn) {
            Kernel.write(Kernel.HRM_EOL_TEST, "0");
            this.isEolOn = false;
        }
        if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this);
        }
        this.mHandler.removeMessages(1);
        if (this.isCloudTest && this.itemUsable_MAXIM_Cloud[0]) {
            if (this.mUVSensor != null && this.mUVSensor.isSensorOn()) {
                this.mUVSensor.SensorOff();
            }
            this.mHandler.removeMessages(2);
            if (this.isUvOn) {
                Kernel.write(Kernel.UV_SENSOR_EOL_TEST, "0");
                this.isUvOn = false;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        this.mDvfs.disableCpuBoost();
    }

    private void setPreCondition() {
        ArrayList<String> testList = new ArrayList<>();
        int i = 0;
        if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equals(this.mHrm_Vendor)) {
            this.isCloudTest = true;
            String subStrHRMSensorName = null;
            if (this.mHrm_Name != null) {
                subStrHRMSensorName = this.mHrm_Name.substring(0, 7);
            }
            if ((!"MAX8690".equalsIgnoreCase(subStrHRMSensorName) && !"MAX8691".equalsIgnoreCase(subStrHRMSensorName)) || Feature.getBoolean(Feature.FORCE_HRM_CLOUD_UV_IR_RED_TEST)) {
                this.itemUsable_MAXIM_Cloud[0] = true;
                this.itemUsable_MAXIM_Cloud[1] = true;
            } else if (Feature.getBoolean(Feature.FORCE_HRM_IR_RED_TEST)) {
                this.itemUsable_MAXIM_Cloud[0] = false;
                this.itemUsable_MAXIM_Cloud[1] = true;
            } else {
                this.isCloudTest = false;
            }
            if (this.isCloudTest) {
                LtUtil.log_i("HrmEolTest", "setPreCondition", "Enable to Cloud Detection Test");
            }
        }
        if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equals(this.mHrm_Vendor)) {
            if (this.mHrm_Name.contains("MAX86915") || this.mHrm_Name.contains("MAX86917")) {
                for (String add : this.itemName_MAXIM_XTC) {
                    testList.add(add);
                }
            } else if (Feature.getBoolean(Feature.HRM_NEW_EOL_TEST)) {
                for (String add2 : this.itemName_MAXIM_NEW) {
                    testList.add(add2);
                }
            } else {
                for (String add3 : this.itemName_MAXIM) {
                    testList.add(add3);
                }
            }
            if (Feature.getBoolean(Feature.SUPPORT_HRM_SPO2)) {
                for (String add4 : this.itemName_MAXIM_SPO2) {
                    testList.add(add4);
                }
            }
            if (this.isCloudTest != 0) {
                if (this.itemUsable_MAXIM_Cloud[0]) {
                    testList.add(this.itemName_MAXIM_Cloud[0]);
                }
                if (this.itemUsable_MAXIM_Cloud[1] && !Feature.getBoolean(Feature.SUPPORT_HRM_SPO2)) {
                    testList.add(this.itemName_MAXIM_Cloud[1]);
                }
            }
            this.itemName = new String[testList.size()];
            while (true) {
                int i2 = i;
                if (i2 < testList.size()) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("testItem : ");
                    sb.append((String) testList.get(i2));
                    LtUtil.log_i("HrmEolTest", "initUI", sb.toString());
                    this.itemName[i2] = (String) testList.get(i2);
                    i = i2 + 1;
                } else {
                    return;
                }
            }
        } else if ("PARTRON".equals(this.mHrm_Vendor)) {
            this.itemName = new String[this.itemName_PARTRON.length];
            while (true) {
                int i3 = i;
                if (i3 < this.itemName_PARTRON.length) {
                    this.itemName[i3] = this.itemName_PARTRON[i3];
                    i = i3 + 1;
                } else {
                    return;
                }
            }
        } else if (!"ADI".equals(this.mHrm_Vendor)) {
        } else {
            if ("ADPD143RI".equals(this.mHrm_Name)) {
                for (String add5 : this.itemName_ADI) {
                    testList.add(add5);
                }
                this.itemName = new String[testList.size()];
                while (true) {
                    int i4 = i;
                    if (i4 < testList.size()) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("testItem : ");
                        sb2.append((String) testList.get(i4));
                        LtUtil.log_i("HrmEolTest", "initUI", sb2.toString());
                        this.itemName[i4] = (String) testList.get(i4);
                        i = i4 + 1;
                    } else {
                        return;
                    }
                }
            } else {
                this.itemName = new String[this.itemName_ADI_OLD.length];
                while (true) {
                    int i5 = i;
                    if (i5 < this.itemName_ADI_OLD.length) {
                        this.itemName[i5] = this.itemName_ADI_OLD[i5];
                        i = i5 + 1;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void runCloudTest() {
        LtUtil.log_i("HrmEolTest", "runCloudTest", "start cloud detection test");
        UV_OnOff(true);
        registerSysfs_UV(true);
        this.mBufferCloud = null;
        this.mHandler.sendEmptyMessageDelayed(2, 50);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case C0268R.C0269id.start_button /*2131296460*/:
                LtUtil.log_i("HrmEolTest", "onClick", "start_button Click");
                if (!"PARTRON".equals(this.mHrm_Vendor)) {
                    HRM_OnOff(true);
                }
                registerSysfs(true);
                for (int i = 0; i < this.itemName.length; i++) {
                    this.txt_measure[i].setText("");
                    this.txt_result[i].setText("");
                }
                this.mTextResult.setText("Do not Move & please wait a moment");
                this.mTextResult.setTextColor(-16777216);
                this.mButton_Start.setEnabled(false);
                this.mSpecResult = true;
                this.mHandler.sendEmptyMessageDelayed(1, 3000);
                return;
            case C0268R.C0269id.exit_button /*2131296461*/:
                LtUtil.log_i("HrmEolTest", "onClick", "exit_button Click");
                finish();
                return;
            default:
                return;
        }
    }

    private void initUI() {
        int Font_Size = 20;
        this.mTextResult = (TextView) findViewById(C0268R.C0269id.final_result);
        if ("ADI".equals(this.mHrm_Vendor)) {
            if ("ADPD143RI".equals(this.mHrm_Name)) {
                Font_Size = 15;
                this.mTextResult.setTextSize(1, 20.0f);
            }
        } else if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equals(this.mHrm_Vendor)) {
            Font_Size = 11;
            this.mTextResult.setTextSize(1, 15.0f);
            if (this.mHrm_Name.contains("MAX86915") || this.mHrm_Name.contains("MAX86917")) {
                Font_Size = 9;
                this.mTextResult.setTextSize(1, 30.0f);
            }
        }
        LayoutParams layoutParams_upper = new LayoutParams(-1, -2);
        LayoutParams layoutParams = new LayoutParams(-1, -2);
        TableRow.LayoutParams layoutParams_txt = new TableRow.LayoutParams(-1, -2);
        TableRow.LayoutParams layoutParams_MatchParent = new TableRow.LayoutParams(-1, -1);
        this.txt_itemName = new TextView[this.itemName.length];
        this.txt_measure = new TextView[this.itemName.length];
        this.txt_result = new TextView[this.itemName.length];
        TableLayout tableLayout = (TableLayout) findViewById(C0268R.C0269id.table);
        TextView classification1 = new TextView(this);
        TextView classification2 = new TextView(this);
        TextView classification3 = new TextView(this);
        TableRow tableRow_classification = new TableRow(this);
        int i = 2;
        layoutParams_upper.setMargins(2, 2, 2, 2);
        tableRow_classification.setLayoutParams(layoutParams_upper);
        classification1.setText("Item");
        classification2.setText("Measure");
        classification3.setText("Result");
        classification1.setTextSize(1, (float) Font_Size);
        classification2.setTextSize(1, (float) Font_Size);
        classification3.setTextSize(1, (float) Font_Size);
        classification1.setGravity(1);
        classification2.setGravity(1);
        classification3.setGravity(1);
        classification1.setBackgroundColor(-1);
        classification2.setBackgroundColor(-1);
        classification3.setBackgroundColor(-1);
        classification1.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, 1));
        classification2.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, 1));
        classification3.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, 1));
        classification1.setTextColor(-16777216);
        classification2.setTextColor(-16777216);
        classification3.setTextColor(-16777216);
        classification1.setBackgroundColor(-256);
        classification2.setBackgroundColor(-256);
        classification3.setBackgroundColor(-256);
        int i2 = 0;
        layoutParams_txt.setMargins(1, 0, 1, 0);
        layoutParams_MatchParent.setMargins(1, 0, 1, 0);
        classification1.setLayoutParams(layoutParams_txt);
        classification2.setLayoutParams(layoutParams_txt);
        classification3.setLayoutParams(layoutParams_txt);
        tableRow_classification.addView(classification1);
        tableRow_classification.addView(classification2);
        tableRow_classification.addView(classification3);
        tableLayout.addView(tableRow_classification);
        int i3 = 0;
        while (true) {
            int i4 = i3;
            if (i4 < this.itemName.length) {
                layoutParams.setMargins(i, i2, i, i);
                TableRow tableRow = new TableRow(this);
                tableRow.setLayoutParams(layoutParams);
                this.txt_itemName[i4] = new TextView(this);
                TextView textView = this.txt_itemName[i4];
                StringBuilder sb = new StringBuilder();
                sb.append(this.itemName[i4]);
                sb.append(addSpec(i4));
                textView.setText(sb.toString());
                this.txt_itemName[i4].setTextSize(1, (float) Font_Size);
                this.txt_itemName[i4].setGravity(17);
                this.txt_itemName[i4].setBackgroundColor(-1);
                this.txt_itemName[i4].setTextColor(-16777216);
                this.txt_itemName[i4].setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, 1));
                this.txt_itemName[i4].setLayoutParams(layoutParams_txt);
                tableRow.addView(this.txt_itemName[i4]);
                this.txt_measure[i4] = new TextView(this);
                this.txt_measure[i4].setTextSize(1, (float) Font_Size);
                this.txt_measure[i4].setGravity(17);
                this.txt_measure[i4].setBackgroundColor(-1);
                this.txt_measure[i4].setTextColor(-16777216);
                this.txt_measure[i4].setLayoutParams(layoutParams_MatchParent);
                tableRow.addView(this.txt_measure[i4]);
                this.txt_result[i4] = new TextView(this);
                this.txt_result[i4].setTextSize(1, (float) Font_Size);
                this.txt_result[i4].setGravity(17);
                this.txt_result[i4].setBackgroundColor(-1);
                this.txt_result[i4].setTextColor(-16776961);
                this.txt_result[i4].setLayoutParams(layoutParams_MatchParent);
                tableRow.addView(this.txt_result[i4]);
                tableLayout.addView(tableRow);
                i3 = i4 + 1;
                i = 2;
                i2 = 0;
            } else {
                this.mButton_Start = (Button) findViewById(C0268R.C0269id.start_button);
                this.mButton_Exit = (Button) findViewById(C0268R.C0269id.exit_button);
                this.mButton_Start.setOnClickListener(this);
                this.mButton_Exit.setOnClickListener(this);
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void registerSysfs(boolean mStatus) {
        LtUtil.log_i("HrmEolTest", "registerSysfs", "registerSysfs Node");
        if (mStatus) {
            if ("ADI".equals(this.mHrm_Vendor)) {
                if (this.mHrm_Name != null && this.mHrm_Name.contains("142")) {
                    Kernel.write(Kernel.HRM_LED_CURRENT, "119");
                    Kernel.write(Kernel.HRM_MODE_IR, "0");
                    Kernel.write(Kernel.HRM_MODE_R, "0");
                }
                Kernel.write(Kernel.HRM_EOL_TEST, EgisFingerprint.MAJOR_VERSION);
            } else if ("PARTRON".equals(this.mHrm_Vendor)) {
                LtUtil.log_i("HrmEolTest", "registerSysfs", "PARTRON");
                Kernel.write(Kernel.HRM_EOL_TEST, EgisFingerprint.MAJOR_VERSION);
                return;
            } else {
                if (Feature.getBoolean(Feature.NEED_CURRENT_HRM_EOL)) {
                    Kernel.write(Kernel.HRM_HR_RANGE2, "48");
                    Kernel.write(Kernel.HRM_LED_CURRENT, "119");
                    Kernel.write(Kernel.HRM_MODE_IR, "0");
                    Kernel.write(Kernel.HRM_MODE_R, "0");
                }
                Kernel.write(Kernel.HRM_EOL_TEST, EgisFingerprint.MAJOR_VERSION);
            }
            this.isEolOn = true;
        } else {
            Kernel.write(Kernel.HRM_EOL_TEST, "0");
            this.isEolOn = false;
        }
    }

    /* access modifiers changed from: private */
    public void registerSysfs_UV(boolean mStatus) {
        StringBuilder sb = new StringBuilder();
        sb.append("registerSysfs UV node - mStatus:");
        sb.append(mStatus);
        LtUtil.log_i("HrmEolTest", "registerSysfs_UV", sb.toString());
        if (mStatus) {
            Kernel.write(Kernel.HRM_LED_CURRENT2, "255");
            Kernel.write(Kernel.UV_SENSOR_EOL_TEST, EgisFingerprint.MAJOR_VERSION);
            this.isUvOn = true;
            return;
        }
        Kernel.write(Kernel.UV_SENSOR_EOL_TEST, "0");
        this.isUvOn = false;
    }

    /* access modifiers changed from: private */
    public void checkCloudValue() {
        if (this.mBufferCloud == null || this.mBufferCloud.length != 6) {
            LtUtil.log_i("HrmEolTest", "checkCloudValue", "Cloud test values are not ready");
            return;
        }
        LtUtil.log_i("HrmEolTest", "checkCloudValue", "Cloud test values are ready");
        this.mCloud_Status = true;
    }

    /* access modifiers changed from: private */
    public void HRM_OnOff(boolean OnOff) {
        StringBuilder sb = new StringBuilder();
        sb.append("HRM Sensor OnOff : ");
        sb.append(OnOff);
        LtUtil.log_i("HrmEolTest", "HRM_OnOff", sb.toString());
        if (OnOff) {
            if ("ADI".equalsIgnoreCase(this.mHrm_Vendor)) {
                this.mSensorManager = (SensorManager) getSystemService("sensor");
                this.mBIOSensor = this.mSensorManager.getDefaultSensor(65561);
                this.mSensorManager.registerListener(this, this.mBIOSensor, 2);
                return;
            }
            this.mSensorManager = (SensorManager) getSystemService("sensor");
            this.mBIOSensor = this.mSensorManager.getDefaultSensor(65561);
            this.mHRMSensor = this.mSensorManager.getDefaultSensor(65562);
            this.mSensorManager.registerListener(this, this.mBIOSensor, 2);
            this.mSensorManager.registerListener(this, this.mHRMSensor, 2);
        } else if (this.mSensorManager != null) {
            this.mSensorManager.unregisterListener(this);
        }
    }

    /* access modifiers changed from: private */
    public void UV_OnOff(boolean OnOff) {
        StringBuilder sb = new StringBuilder();
        sb.append("UV Sensor OnOff : ");
        sb.append(OnOff);
        LtUtil.log_i("HrmEolTest", "UV_OnOff", sb.toString());
        if (OnOff) {
            this.mUVSensor = new SensorUV(this, getBaseContext());
            this.mUVSensor.SensorOn();
        } else if (this.mUVSensor != null) {
            this.mUVSensor.SensorOff();
            this.mUVSensor = null;
        }
    }

    /* access modifiers changed from: private */
    public void StoreCloudValue() {
        LtUtil.log_i("HrmEolTest", "StoreCloudValue", "");
        if (this.mBufferCloud != null && this.mBufferCloud.length > 0) {
            this.dataCloud = new int[this.mBufferCloud.length];
            for (int i = 0; i < this.mBufferCloud.length; i++) {
                this.dataCloud[i] = Integer.parseInt(this.mBufferCloud[i]);
            }
        }
    }

    /* access modifiers changed from: private */
    public void checkStatus() {
        String mStatus = Kernel.read(Kernel.HRM_EOL_TEST_STATUS);
        if (mStatus == null || !mStatus.equals(EgisFingerprint.MAJOR_VERSION)) {
            LtUtil.log_i("HrmEolTest", "checkStatus", "Sysfs is not ready");
            return;
        }
        LtUtil.log_i("HrmEolTest", "checkStatus", "Sysfs is ready");
        this.mhtm_Status = true;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x069d  */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x06ae  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateUI() {
        /*
            r18 = this;
            r1 = r18
            r0 = 0
            java.lang.String r2 = ""
            java.lang.String r3 = "HRM_EOL_TEST_RESULT"
            java.lang.String r2 = com.sec.xmldata.support.Support.Kernel.read(r3)
            if (r2 != 0) goto L_0x0017
            java.lang.String r3 = "HrmEolTest"
            java.lang.String r4 = "updateUI"
            java.lang.String r5 = "Sysfs is not ready"
            com.sec.android.app.hwmoduletest.support.LtUtil.log_i(r3, r4, r5)
            return
        L_0x0017:
            java.lang.String r3 = r2.trim()
            java.lang.String r4 = ","
            java.lang.String[] r3 = r3.split(r4)
            r0 = 100
            float[] r0 = new float[r0]
            r4 = r0
            r5 = 0
            r0 = r5
        L_0x0028:
            int r6 = r3.length     // Catch:{ NumberFormatException -> 0x0037 }
            if (r0 >= r6) goto L_0x0036
            r6 = r3[r0]     // Catch:{ NumberFormatException -> 0x0037 }
            float r6 = java.lang.Float.parseFloat(r6)     // Catch:{ NumberFormatException -> 0x0037 }
            r4[r0] = r6     // Catch:{ NumberFormatException -> 0x0037 }
            int r0 = r0 + 1
            goto L_0x0028
        L_0x0036:
            goto L_0x003b
        L_0x0037:
            r0 = move-exception
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r0)
        L_0x003b:
            java.lang.String r0 = "MAXIM"
            java.lang.String r6 = r1.mHrm_Vendor
            boolean r0 = r0.equals(r6)
            r7 = 9
            r10 = 6
            r14 = 4
            r15 = -65536(0xffffffffffff0000, float:NaN)
            r6 = -16776961(0xffffffffff0000ff, float:-1.7014636E38)
            r8 = 1
            if (r0 == 0) goto L_0x0606
            java.lang.String r0 = r1.mHrm_Name
            java.lang.String r9 = "MAX86915"
            boolean r0 = r0.contains(r9)
            if (r0 != 0) goto L_0x05b4
            java.lang.String r0 = r1.mHrm_Name
            java.lang.String r9 = "MAX86917"
            boolean r0 = r0.contains(r9)
            if (r0 == 0) goto L_0x0065
            goto L_0x05b4
        L_0x0065:
            java.lang.String r0 = "HRM_NEW_EOL_TEST"
            boolean r0 = com.sec.xmldata.support.Support.Feature.getBoolean(r0)
            r9 = 7
            r11 = 8
            if (r0 == 0) goto L_0x00dc
            r0 = r5
        L_0x0071:
            java.lang.String[] r7 = r1.itemName
            int r7 = r7.length
            if (r0 >= r7) goto L_0x08ce
            if (r0 == r9) goto L_0x008a
            if (r0 != r11) goto L_0x007b
            goto L_0x008a
        L_0x007b:
            android.widget.TextView[] r7 = r1.txt_measure
            r7 = r7[r0]
            r8 = r4[r0]
            int r8 = (int) r8
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r7.setText(r8)
            goto L_0x009e
        L_0x008a:
            android.widget.TextView[] r7 = r1.txt_measure
            r7 = r7[r0]
            java.text.DecimalFormat r8 = r1.mFormat
            r10 = r4[r0]
            double r12 = (double) r10
            java.lang.String r8 = r8.format(r12)
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r7.setText(r8)
        L_0x009e:
            r7 = r3[r0]
            java.lang.String[] r8 = r1.HRM_spec_data
            r8 = r8[r0]
            android.widget.TextView[] r10 = r1.txt_measure
            r10 = r10[r0]
            java.lang.CharSequence r10 = r10.getText()
            java.lang.String r10 = r10.toString()
            boolean r7 = r1.checkSpec(r7, r8, r10)
            if (r7 == 0) goto L_0x00c7
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            java.lang.String r8 = "PASS"
            r7.setText(r8)
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            r7.setTextColor(r6)
            goto L_0x00d9
        L_0x00c7:
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            java.lang.String r8 = "FAIL"
            r7.setText(r8)
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            r7.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x00d9:
            int r0 = r0 + 1
            goto L_0x0071
        L_0x00dc:
            android.widget.TextView[] r0 = r1.txt_measure
            r0 = r0[r5]
            java.text.DecimalFormat r11 = r1.mFormat
            r9 = r4[r5]
            double r12 = (double) r9
            java.lang.String r9 = r11.format(r12)
            java.lang.String r9 = java.lang.String.valueOf(r9)
            r0.setText(r9)
            r0 = r3[r5]
            java.lang.String[] r9 = r1.HRM_spec_data
            r9 = r9[r5]
            android.widget.TextView[] r11 = r1.txt_itemName
            r11 = r11[r5]
            java.lang.CharSequence r11 = r11.getText()
            java.lang.String r11 = r11.toString()
            boolean r0 = r1.checkSpec(r0, r9, r11)
            if (r0 == 0) goto L_0x0119
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r5]
            java.lang.String r9 = "PASS"
            r0.setText(r9)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r5]
            r0.setTextColor(r6)
            goto L_0x012b
        L_0x0119:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r5]
            java.lang.String r9 = "FAIL"
            r0.setText(r9)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r5]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x012b:
            android.widget.TextView[] r0 = r1.txt_measure
            r0 = r0[r8]
            r9 = 2
            r11 = r4[r9]
            int r11 = (int) r11
            java.lang.String r11 = java.lang.String.valueOf(r11)
            r0.setText(r11)
            r0 = r3[r9]
            java.lang.String[] r9 = r1.HRM_spec_data
            r9 = r9[r8]
            android.widget.TextView[] r11 = r1.txt_itemName
            r11 = r11[r8]
            java.lang.CharSequence r11 = r11.getText()
            java.lang.String r11 = r11.toString()
            boolean r0 = r1.checkSpec(r0, r9, r11)
            if (r0 == 0) goto L_0x0163
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r8]
            java.lang.String r9 = "PASS"
            r0.setText(r9)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r8]
            r0.setTextColor(r6)
            goto L_0x0175
        L_0x0163:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r8]
            java.lang.String r9 = "FAIL"
            r0.setText(r9)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r8]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x0175:
            android.widget.TextView[] r0 = r1.txt_measure
            r9 = 2
            r0 = r0[r9]
            java.text.DecimalFormat r11 = r1.mFormat
            r12 = r4[r14]
            double r12 = (double) r12
            java.lang.String r11 = r11.format(r12)
            java.lang.String r11 = java.lang.String.valueOf(r11)
            r0.setText(r11)
            r0 = r3[r14]
            java.lang.String[] r11 = r1.HRM_spec_data
            r11 = r11[r9]
            android.widget.TextView[] r12 = r1.txt_itemName
            r12 = r12[r9]
            java.lang.CharSequence r12 = r12.getText()
            java.lang.String r12 = r12.toString()
            boolean r0 = r1.checkSpec(r0, r11, r12)
            if (r0 == 0) goto L_0x01b3
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r9]
            java.lang.String r11 = "PASS"
            r0.setText(r11)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r9]
            r0.setTextColor(r6)
            goto L_0x01c5
        L_0x01b3:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r9]
            java.lang.String r11 = "FAIL"
            r0.setText(r11)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r9]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x01c5:
            android.widget.TextView[] r0 = r1.txt_measure
            r9 = 3
            r0 = r0[r9]
            r9 = r4[r10]
            int r9 = (int) r9
            java.lang.String r9 = java.lang.String.valueOf(r9)
            r0.setText(r9)
            android.widget.TextView[] r0 = r1.txt_measure
            r0 = r0[r7]
            r9 = 7
            r11 = r4[r9]
            int r9 = (int) r11
            java.lang.String r9 = java.lang.String.valueOf(r9)
            r0.setText(r9)
            r0 = r3[r10]
            java.lang.String[] r9 = r1.HRM_spec_data
            r11 = 3
            r9 = r9[r11]
            android.widget.TextView[] r12 = r1.txt_itemName
            r12 = r12[r11]
            java.lang.CharSequence r12 = r12.getText()
            java.lang.String r12 = r12.toString()
            boolean r0 = r1.checkSpec(r0, r9, r12)
            if (r0 == 0) goto L_0x021d
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r11]
            java.lang.String r9 = "PASS"
            r0.setText(r9)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r9 = "PASS"
            r0.setText(r9)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r11]
            r0.setTextColor(r6)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r6)
            goto L_0x023f
        L_0x021d:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r11]
            java.lang.String r9 = "FAIL"
            r0.setText(r9)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r9 = "FAIL"
            r0.setText(r9)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r11]
            r0.setTextColor(r15)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x023f:
            android.widget.TextView[] r0 = r1.txt_measure
            r0 = r0[r14]
            r9 = 8
            r11 = r4[r9]
            int r11 = (int) r11
            java.lang.String r11 = java.lang.String.valueOf(r11)
            r0.setText(r11)
            r0 = r3[r9]
            java.lang.String[] r9 = r1.HRM_spec_data
            r9 = r9[r14]
            android.widget.TextView[] r11 = r1.txt_itemName
            r11 = r11[r14]
            java.lang.CharSequence r11 = r11.getText()
            java.lang.String r11 = r11.toString()
            boolean r0 = r1.checkSpec(r0, r9, r11)
            if (r0 == 0) goto L_0x0278
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r14]
            java.lang.String r9 = "PASS"
            r0.setText(r9)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r14]
            r0.setTextColor(r6)
            goto L_0x028a
        L_0x0278:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r14]
            java.lang.String r9 = "FAIL"
            r0.setText(r9)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r14]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x028a:
            android.widget.TextView[] r0 = r1.txt_measure
            r9 = 5
            r0 = r0[r9]
            java.text.DecimalFormat r11 = r1.mFormat
            r12 = 10
            r13 = r4[r12]
            double r7 = (double) r13
            java.lang.String r7 = r11.format(r7)
            java.lang.String r7 = java.lang.String.valueOf(r7)
            r0.setText(r7)
            r0 = r3[r12]
            java.lang.String[] r7 = r1.HRM_spec_data
            r7 = r7[r9]
            android.widget.TextView[] r8 = r1.txt_itemName
            r8 = r8[r9]
            java.lang.CharSequence r8 = r8.getText()
            java.lang.String r8 = r8.toString()
            boolean r0 = r1.checkSpec(r0, r7, r8)
            if (r0 == 0) goto L_0x02ca
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r9]
            java.lang.String r7 = "PASS"
            r0.setText(r7)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r9]
            r0.setTextColor(r6)
            goto L_0x02dc
        L_0x02ca:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r9]
            java.lang.String r7 = "FAIL"
            r0.setText(r7)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r9]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x02dc:
            android.widget.TextView[] r0 = r1.txt_measure
            r0 = r0[r10]
            r7 = 1
            r8 = r4[r7]
            int r8 = (int) r8
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r0.setText(r8)
            r0 = r3[r7]
            java.lang.String[] r7 = r1.HRM_spec_data
            r7 = r7[r10]
            android.widget.TextView[] r8 = r1.txt_itemName
            r8 = r8[r10]
            java.lang.CharSequence r8 = r8.getText()
            java.lang.String r8 = r8.toString()
            boolean r0 = r1.checkSpec(r0, r7, r8)
            if (r0 == 0) goto L_0x0314
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r10]
            java.lang.String r7 = "PASS"
            r0.setText(r7)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r10]
            r0.setTextColor(r6)
            goto L_0x0326
        L_0x0314:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r10]
            java.lang.String r7 = "FAIL"
            r0.setText(r7)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r10]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x0326:
            android.widget.TextView[] r0 = r1.txt_measure
            r7 = 7
            r0 = r0[r7]
            r8 = 3
            r9 = r4[r8]
            int r9 = (int) r9
            java.lang.String r9 = java.lang.String.valueOf(r9)
            r0.setText(r9)
            r0 = r3[r8]
            java.lang.String[] r8 = r1.HRM_spec_data
            r8 = r8[r7]
            android.widget.TextView[] r9 = r1.txt_itemName
            r9 = r9[r7]
            java.lang.CharSequence r9 = r9.getText()
            java.lang.String r9 = r9.toString()
            boolean r0 = r1.checkSpec(r0, r8, r9)
            if (r0 == 0) goto L_0x035f
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "PASS"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r6)
            goto L_0x0371
        L_0x035f:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "FAIL"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x0371:
            android.widget.TextView[] r0 = r1.txt_measure
            r7 = 8
            r0 = r0[r7]
            java.text.DecimalFormat r8 = r1.mFormat
            r9 = 5
            r10 = r4[r9]
            double r10 = (double) r10
            java.lang.String r8 = r8.format(r10)
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r0.setText(r8)
            r0 = r3[r9]
            java.lang.String[] r8 = r1.HRM_spec_data
            r8 = r8[r7]
            android.widget.TextView[] r9 = r1.txt_itemName
            r9 = r9[r7]
            java.lang.CharSequence r9 = r9.getText()
            java.lang.String r9 = r9.toString()
            boolean r0 = r1.checkSpec(r0, r8, r9)
            if (r0 == 0) goto L_0x03b1
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "PASS"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r6)
            goto L_0x03c3
        L_0x03b1:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "FAIL"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x03c3:
            android.widget.TextView[] r0 = r1.txt_measure
            r7 = 10
            r0 = r0[r7]
            r8 = 9
            r9 = r4[r8]
            int r9 = (int) r9
            java.lang.String r9 = java.lang.String.valueOf(r9)
            r0.setText(r9)
            r0 = r3[r8]
            java.lang.String[] r8 = r1.HRM_spec_data
            r8 = r8[r7]
            android.widget.TextView[] r9 = r1.txt_itemName
            r9 = r9[r7]
            java.lang.CharSequence r9 = r9.getText()
            java.lang.String r9 = r9.toString()
            boolean r0 = r1.checkSpec(r0, r8, r9)
            if (r0 == 0) goto L_0x03fe
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "PASS"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r6)
            goto L_0x0410
        L_0x03fe:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "FAIL"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x0410:
            android.widget.TextView[] r0 = r1.txt_measure
            r7 = 11
            r0 = r0[r7]
            java.text.DecimalFormat r8 = r1.mFormat
            r9 = r4[r7]
            double r9 = (double) r9
            java.lang.String r8 = r8.format(r9)
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r0.setText(r8)
            r0 = r3[r7]
            java.lang.String[] r8 = r1.HRM_spec_data
            r8 = r8[r7]
            android.widget.TextView[] r9 = r1.txt_itemName
            r9 = r9[r7]
            java.lang.CharSequence r9 = r9.getText()
            java.lang.String r9 = r9.toString()
            boolean r0 = r1.checkSpec(r0, r8, r9)
            if (r0 == 0) goto L_0x044f
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "PASS"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r6)
            goto L_0x0461
        L_0x044f:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "FAIL"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x0461:
            r0 = 12
            r7 = 12
            java.lang.String r8 = "SUPPORT_HRM_SPO2"
            boolean r8 = com.sec.xmldata.support.Support.Feature.getBoolean(r8)
            if (r8 == 0) goto L_0x04c3
            android.widget.TextView[] r8 = r1.txt_measure
            r8 = r8[r0]
            java.text.DecimalFormat r9 = r1.mFormat
            r10 = 12
            r11 = r4[r10]
            double r11 = (double) r11
            java.lang.String r9 = r9.format(r11)
            java.lang.String r9 = java.lang.String.valueOf(r9)
            r8.setText(r9)
            r8 = r3[r10]
            java.lang.String[] r9 = r1.HRM_spec_data
            int r10 = r7 + 1
            r7 = r9[r7]
            android.widget.TextView[] r9 = r1.txt_itemName
            r9 = r9[r0]
            java.lang.CharSequence r9 = r9.getText()
            java.lang.String r9 = r9.toString()
            boolean r7 = r1.checkSpec(r8, r7, r9)
            if (r7 == 0) goto L_0x04ae
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            java.lang.String r8 = "PASS"
            r7.setText(r8)
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            r7.setTextColor(r6)
            goto L_0x04c0
        L_0x04ae:
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            java.lang.String r8 = "FAIL"
            r7.setText(r8)
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            r7.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x04c0:
            int r0 = r0 + 1
            r7 = r10
        L_0x04c3:
            boolean r8 = r1.isCloudTest
            if (r8 == 0) goto L_0x05b2
            boolean[] r8 = r1.itemUsable_MAXIM_Cloud
            boolean r8 = r8[r5]
            if (r8 == 0) goto L_0x054e
            int[] r8 = r1.dataCloud
            r8 = r8[r14]
            float r8 = (float) r8
            r9 = 1120403456(0x42c80000, float:100.0)
            float r8 = r8 * r9
            int[] r9 = r1.dataCloud
            r10 = 5
            r9 = r9[r10]
            float r9 = (float) r9
            float r8 = r8 / r9
            android.widget.TextView[] r9 = r1.txt_measure
            r9 = r9[r0]
            java.text.DecimalFormat r10 = r1.mFormat
            double r11 = (double) r8
            java.lang.String r10 = r10.format(r11)
            java.lang.String r10 = java.lang.String.valueOf(r10)
            r9.setText(r10)
            java.lang.String r9 = java.lang.String.valueOf(r8)
            java.lang.String[] r10 = r1.HRM_spec_data
            int r11 = r7 + 1
            r7 = r10[r7]
            android.widget.TextView[] r10 = r1.txt_itemName
            r10 = r10[r0]
            java.lang.CharSequence r10 = r10.getText()
            java.lang.String r10 = r10.toString()
            boolean r7 = r1.checkSpec(r9, r7, r10)
            if (r7 == 0) goto L_0x051b
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            java.lang.String r9 = "PASS"
            r7.setText(r9)
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            r7.setTextColor(r6)
            goto L_0x052d
        L_0x051b:
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            java.lang.String r9 = "FAIL"
            r7.setText(r9)
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            r7.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x052d:
            int r7 = r0 + 1
            java.lang.String r0 = "HrmEolTest"
            java.lang.String r9 = "updateUI"
            java.lang.StringBuilder r10 = new java.lang.StringBuilder     // Catch:{ NumberFormatException -> 0x0548 }
            r10.<init>()     // Catch:{ NumberFormatException -> 0x0548 }
            java.lang.String r12 = "cloud uv ratio:"
            r10.append(r12)     // Catch:{ NumberFormatException -> 0x0548 }
            r10.append(r8)     // Catch:{ NumberFormatException -> 0x0548 }
            java.lang.String r10 = r10.toString()     // Catch:{ NumberFormatException -> 0x0548 }
            com.sec.android.app.hwmoduletest.support.LtUtil.log_i(r0, r9, r10)     // Catch:{ NumberFormatException -> 0x0548 }
            goto L_0x054c
        L_0x0548:
            r0 = move-exception
            com.sec.android.app.hwmoduletest.support.LtUtil.log_e(r0)
        L_0x054c:
            r0 = r7
            r7 = r11
        L_0x054e:
            boolean[] r8 = r1.itemUsable_MAXIM_Cloud
            r9 = 1
            boolean r8 = r8[r9]
            if (r8 == 0) goto L_0x05b2
            java.lang.String r8 = "SUPPORT_HRM_SPO2"
            boolean r8 = com.sec.xmldata.support.Support.Feature.getBoolean(r8)
            if (r8 != 0) goto L_0x05b2
            android.widget.TextView[] r8 = r1.txt_measure
            r8 = r8[r0]
            java.text.DecimalFormat r9 = r1.mThreeFormat
            r10 = 12
            r11 = r4[r10]
            double r11 = (double) r11
            java.lang.String r9 = r9.format(r11)
            java.lang.String r9 = java.lang.String.valueOf(r9)
            r8.setText(r9)
            r8 = r3[r10]
            java.lang.String[] r9 = r1.HRM_spec_data
            int r10 = r7 + 1
            r7 = r9[r7]
            android.widget.TextView[] r9 = r1.txt_itemName
            r9 = r9[r0]
            java.lang.CharSequence r9 = r9.getText()
            java.lang.String r9 = r9.toString()
            boolean r7 = r1.checkSpec(r8, r7, r9)
            if (r7 == 0) goto L_0x059e
            android.widget.TextView[] r5 = r1.txt_result
            r5 = r5[r0]
            java.lang.String r7 = "PASS"
            r5.setText(r7)
            android.widget.TextView[] r5 = r1.txt_result
            r5 = r5[r0]
            r5.setTextColor(r6)
            goto L_0x05b0
        L_0x059e:
            android.widget.TextView[] r6 = r1.txt_result
            r6 = r6[r0]
            java.lang.String r7 = "FAIL"
            r6.setText(r7)
            android.widget.TextView[] r6 = r1.txt_result
            r6 = r6[r0]
            r6.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x05b0:
            r7 = r10
        L_0x05b2:
            goto L_0x08ce
        L_0x05b4:
            r0 = r5
        L_0x05b5:
            java.lang.String[] r7 = r1.itemName
            int r7 = r7.length
            if (r0 >= r7) goto L_0x08ce
            android.widget.TextView[] r7 = r1.txt_measure
            r7 = r7[r0]
            r8 = r4[r0]
            int r8 = (int) r8
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r7.setText(r8)
            r7 = r3[r0]
            java.lang.String[] r8 = r1.HRM_spec_data
            r8 = r8[r0]
            android.widget.TextView[] r9 = r1.txt_measure
            r9 = r9[r0]
            java.lang.CharSequence r9 = r9.getText()
            java.lang.String r9 = r9.toString()
            boolean r7 = r1.checkSpec(r7, r8, r9)
            if (r7 == 0) goto L_0x05f1
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            java.lang.String r8 = "PASS"
            r7.setText(r8)
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            r7.setTextColor(r6)
            goto L_0x0603
        L_0x05f1:
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            java.lang.String r8 = "FAIL"
            r7.setText(r8)
            android.widget.TextView[] r7 = r1.txt_result
            r7 = r7[r0]
            r7.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x0603:
            int r0 = r0 + 1
            goto L_0x05b5
        L_0x0606:
            java.lang.String r0 = "PARTRON"
            java.lang.String r7 = r1.mHrm_Vendor
            boolean r0 = r0.equals(r7)
            if (r0 == 0) goto L_0x06c6
            r7 = 12
            java.lang.String[] r0 = new java.lang.String[r7]
            r7 = r5
        L_0x0615:
            android.widget.TextView[] r8 = r1.txt_measure
            int r8 = r8.length
            if (r7 >= r8) goto L_0x06c4
            if (r7 == r14) goto L_0x0660
            r8 = 5
            if (r7 == r8) goto L_0x0660
            r8 = 9
            if (r7 == r8) goto L_0x0662
            r9 = 10
            if (r7 != r9) goto L_0x0628
            goto L_0x0664
        L_0x0628:
            r10 = 11
            if (r7 != r10) goto L_0x0651
            java.lang.String r11 = "%.3f"
            r12 = 1
            java.lang.Object[] r13 = new java.lang.Object[r12]
            r12 = r4[r7]
            r16 = 1148846080(0x447a0000, float:1000.0)
            float r12 = r12 / r16
            java.lang.Float r12 = java.lang.Float.valueOf(r12)
            r13[r5] = r12
            java.lang.String r11 = java.lang.String.format(r11, r13)
            r3[r7] = r11
            android.widget.TextView[] r11 = r1.txt_measure
            r11 = r11[r7]
            r12 = r3[r7]
            java.lang.String r12 = java.lang.String.valueOf(r12)
            r11.setText(r12)
            goto L_0x0687
        L_0x0651:
            android.widget.TextView[] r11 = r1.txt_measure
            r11 = r11[r7]
            r12 = r4[r7]
            int r12 = (int) r12
            java.lang.String r12 = java.lang.String.valueOf(r12)
            r11.setText(r12)
            goto L_0x0687
        L_0x0660:
            r8 = 9
        L_0x0662:
            r9 = 10
        L_0x0664:
            r10 = 11
            java.lang.String r11 = "%.2f"
            r12 = 1
            java.lang.Object[] r13 = new java.lang.Object[r12]
            r12 = r4[r7]
            r16 = 1259902592(0x4b189680, float:1.0E7)
            float r12 = r12 / r16
            java.lang.Float r12 = java.lang.Float.valueOf(r12)
            r13[r5] = r12
            java.lang.String r11 = java.lang.String.format(r11, r13)
            r3[r7] = r11
            android.widget.TextView[] r11 = r1.txt_measure
            r11 = r11[r7]
            r12 = r3[r7]
            r11.setText(r12)
        L_0x0687:
            r11 = r3[r7]
            r12 = r0[r7]
            android.widget.TextView[] r13 = r1.txt_itemName
            r13 = r13[r7]
            java.lang.CharSequence r13 = r13.getText()
            java.lang.String r13 = r13.toString()
            boolean r11 = r1.checkSpec(r11, r12, r13)
            if (r11 == 0) goto L_0x06ae
            android.widget.TextView[] r11 = r1.txt_result
            r11 = r11[r7]
            java.lang.String r12 = "PASS"
            r11.setText(r12)
            android.widget.TextView[] r11 = r1.txt_result
            r11 = r11[r7]
            r11.setTextColor(r6)
            goto L_0x06c0
        L_0x06ae:
            android.widget.TextView[] r11 = r1.txt_result
            r11 = r11[r7]
            java.lang.String r12 = "FAIL"
            r11.setText(r12)
            android.widget.TextView[] r11 = r1.txt_result
            r11 = r11[r7]
            r11.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x06c0:
            int r7 = r7 + 1
            goto L_0x0615
        L_0x06c4:
            goto L_0x08ce
        L_0x06c6:
            java.lang.String r0 = "ADI"
            java.lang.String r7 = r1.mHrm_Vendor
            boolean r0 = r0.equalsIgnoreCase(r7)
            if (r0 == 0) goto L_0x08ce
            java.lang.String r0 = "ADPD143RI"
            java.lang.String r7 = r1.mHrm_Name
            boolean r0 = r0.equals(r7)
            if (r0 == 0) goto L_0x074e
            r0 = r5
        L_0x06db:
            android.widget.TextView[] r7 = r1.txt_measure
            int r7 = r7.length
            if (r0 >= r7) goto L_0x08ce
            r7 = 5
            if (r0 == r7) goto L_0x06f5
            if (r0 != r10) goto L_0x06e6
            goto L_0x06f5
        L_0x06e6:
            android.widget.TextView[] r8 = r1.txt_measure
            r8 = r8[r0]
            r9 = r4[r0]
            int r9 = (int) r9
            java.lang.String r9 = java.lang.String.valueOf(r9)
            r8.setText(r9)
            goto L_0x0710
        L_0x06f5:
            r8 = r4[r0]
            double r8 = (double) r8
            double r8 = java.lang.Math.sqrt(r8)
            long r8 = java.lang.Math.round(r8)
            int r8 = (int) r8
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r3[r0] = r8
            android.widget.TextView[] r8 = r1.txt_measure
            r8 = r8[r0]
            r9 = r3[r0]
            r8.setText(r9)
        L_0x0710:
            r8 = r3[r0]
            java.lang.String[] r9 = r1.HRM_spec_data
            r9 = r9[r0]
            android.widget.TextView[] r11 = r1.txt_itemName
            r11 = r11[r0]
            java.lang.CharSequence r11 = r11.getText()
            java.lang.String r11 = r11.toString()
            boolean r8 = r1.checkSpec(r8, r9, r11)
            if (r8 == 0) goto L_0x0739
            android.widget.TextView[] r8 = r1.txt_result
            r8 = r8[r0]
            java.lang.String r9 = "PASS"
            r8.setText(r9)
            android.widget.TextView[] r8 = r1.txt_result
            r8 = r8[r0]
            r8.setTextColor(r6)
            goto L_0x074b
        L_0x0739:
            android.widget.TextView[] r8 = r1.txt_result
            r8 = r8[r0]
            java.lang.String r9 = "FAIL"
            r8.setText(r9)
            android.widget.TextView[] r8 = r1.txt_result
            r8 = r8[r0]
            r8.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x074b:
            int r0 = r0 + 1
            goto L_0x06db
        L_0x074e:
            android.widget.TextView[] r0 = r1.txt_measure
            r0 = r0[r5]
            r7 = r4[r5]
            int r7 = (int) r7
            java.lang.String r7 = java.lang.String.valueOf(r7)
            r0.setText(r7)
            r0 = r3[r5]
            java.lang.String r7 = "HRM_FREQUENCY_SAMPLE_NO"
            java.lang.String r7 = com.sec.xmldata.support.Support.Spec.getString(r7)
            android.widget.TextView[] r8 = r1.txt_itemName
            r8 = r8[r5]
            java.lang.CharSequence r8 = r8.getText()
            java.lang.String r8 = r8.toString()
            boolean r0 = r1.checkSpec(r0, r7, r8)
            if (r0 == 0) goto L_0x0787
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r5]
            java.lang.String r7 = "PASS"
            r0.setText(r7)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r5]
            r0.setTextColor(r6)
            goto L_0x0799
        L_0x0787:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r5]
            java.lang.String r7 = "FAIL"
            r0.setText(r7)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r5]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x0799:
            android.widget.TextView[] r0 = r1.txt_measure
            r7 = 1
            r0 = r0[r7]
            r8 = r4[r7]
            int r8 = (int) r8
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r0.setText(r8)
            r0 = r3[r7]
            java.lang.String r8 = "HRM_PEAK_TO_PEAK_PEAK_IR"
            java.lang.String r8 = com.sec.xmldata.support.Support.Spec.getString(r8)
            android.widget.TextView[] r9 = r1.txt_itemName
            r9 = r9[r7]
            java.lang.CharSequence r9 = r9.getText()
            java.lang.String r9 = r9.toString()
            boolean r0 = r1.checkSpec(r0, r8, r9)
            if (r0 == 0) goto L_0x07d3
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "PASS"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r6)
            goto L_0x07e5
        L_0x07d3:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "FAIL"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x07e5:
            android.widget.TextView[] r0 = r1.txt_measure
            r7 = 2
            r0 = r0[r7]
            r8 = r4[r7]
            int r8 = (int) r8
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r0.setText(r8)
            r0 = r3[r7]
            java.lang.String r8 = "HRM_PEAK_TO_PEAK_DC_IR"
            java.lang.String r8 = com.sec.xmldata.support.Support.Spec.getString(r8)
            android.widget.TextView[] r9 = r1.txt_itemName
            r9 = r9[r7]
            java.lang.CharSequence r9 = r9.getText()
            java.lang.String r9 = r9.toString()
            boolean r0 = r1.checkSpec(r0, r8, r9)
            if (r0 == 0) goto L_0x081f
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "PASS"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r6)
            goto L_0x0831
        L_0x081f:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "FAIL"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x0831:
            android.widget.TextView[] r0 = r1.txt_measure
            r7 = 3
            r0 = r0[r7]
            java.text.DecimalFormat r8 = r1.mFormat
            r9 = r4[r7]
            double r9 = (double) r9
            java.lang.String r8 = r8.format(r9)
            java.lang.String r8 = java.lang.String.valueOf(r8)
            r0.setText(r8)
            r0 = r3[r7]
            java.lang.String r8 = "HRM_FREQUENCY_NOISE_IR"
            java.lang.String r8 = com.sec.xmldata.support.Support.Spec.getString(r8)
            android.widget.TextView[] r9 = r1.txt_itemName
            r9 = r9[r7]
            java.lang.CharSequence r9 = r9.getText()
            java.lang.String r9 = r9.toString()
            boolean r0 = r1.checkSpec(r0, r8, r9)
            if (r0 == 0) goto L_0x0871
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "PASS"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r6)
            goto L_0x0883
        L_0x0871:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            java.lang.String r8 = "FAIL"
            r0.setText(r8)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r7]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x0883:
            android.widget.TextView[] r0 = r1.txt_measure
            r0 = r0[r14]
            r7 = r4[r14]
            int r7 = (int) r7
            java.lang.String r7 = java.lang.String.valueOf(r7)
            r0.setText(r7)
            r0 = r3[r14]
            java.lang.String r7 = "HRM_PEAK_TO_PEAK_DC_RED"
            java.lang.String r7 = com.sec.xmldata.support.Support.Spec.getString(r7)
            android.widget.TextView[] r8 = r1.txt_itemName
            r8 = r8[r14]
            java.lang.CharSequence r8 = r8.getText()
            java.lang.String r8 = r8.toString()
            boolean r0 = r1.checkSpec(r0, r7, r8)
            if (r0 == 0) goto L_0x08bc
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r14]
            java.lang.String r5 = "PASS"
            r0.setText(r5)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r14]
            r0.setTextColor(r6)
            goto L_0x08ce
        L_0x08bc:
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r14]
            java.lang.String r6 = "FAIL"
            r0.setText(r6)
            android.widget.TextView[] r0 = r1.txt_result
            r0 = r0[r14]
            r0.setTextColor(r15)
            r1.mSpecResult = r5
        L_0x08ce:
            r18.checkPassFail()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sec.android.app.hwmoduletest.HrmEolTest.updateUI():void");
    }

    private boolean checkSpec(String mOriginalVal, String mSysfsNode, String itemName2) {
        String[] mSpecVal = mSysfsNode.split(",");
        String itemName3 = itemName2.replace("\n", "");
        try {
            float mTempVal = Float.parseFloat(mOriginalVal);
            if (mSpecVal.length != 2 || mTempVal < Float.parseFloat(mSpecVal[0]) || mTempVal > Float.parseFloat(mSpecVal[1])) {
                StringBuilder sb = new StringBuilder();
                sb.append("Fail! ");
                sb.append(itemName3);
                sb.append(" Value : ");
                sb.append(mTempVal);
                sb.append(" ");
                sb.append(mSysfsNode);
                LtUtil.log_i("HrmEolTest", "checkSpec", sb.toString());
                return false;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Pass! ");
            sb2.append(itemName3);
            sb2.append(" Value : ");
            sb2.append(mTempVal);
            sb2.append(" ");
            sb2.append(mSysfsNode);
            LtUtil.log_i("HrmEolTest", "checkSpec", sb2.toString());
            return true;
        } catch (NumberFormatException ne) {
            LtUtil.log_e(ne);
        }
    }

    private void checkPassFail() {
        if (this.mSpecResult) {
            this.mTextResult.setText("PASS");
            this.mTextResult.setTextColor(-16776961);
            return;
        }
        this.mTextResult.setText("FAIL");
        this.mTextResult.setTextColor(-65536);
    }

    public void onSensorChanged(SensorEvent event) {
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorValueReceived(int mSensor, String value) {
        if (mSensor == 14) {
            ReadValueFromListener(value);
            StringBuilder sb = new StringBuilder();
            sb.append("ID_MANAGER_UV_CLOUD : ");
            sb.append(value);
            LtUtil.log_d("HrmEolTest", "onSensorValueReceived", sb.toString());
        }
    }

    private void ReadValueFromListener(String value) {
        if (value != null && value.contains(",")) {
            this.mBufferCloud = value.split(",");
        }
    }

    private String addSpec(int Index) {
        String Spec = "";
        if ("ADI".equals(this.mHrm_Vendor) && "ADPD143RI".equals(this.mHrm_Name)) {
            StringBuilder sb = new StringBuilder();
            sb.append(Spec);
            sb.append("\n");
            String Spec2 = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(Spec2);
            sb2.append("(");
            sb2.append(replaceComma(this.HRM_spec_data[Index]));
            sb2.append(")");
            return sb2.toString();
        } else if (!ModuleSensor.FEATURE_GYROSCOP_MAXIM.equals(this.mHrm_Vendor) && !"PARTRON".equals(this.mHrm_Vendor)) {
            return Spec;
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(Spec);
            sb3.append("\n");
            String Spec3 = sb3.toString();
            StringBuilder sb4 = new StringBuilder();
            sb4.append("");
            sb4.append(Index);
            LtUtil.log_d("HrmEolTest", "addSpec", sb4.toString());
            StringBuilder sb5 = new StringBuilder();
            sb5.append(Spec3);
            sb5.append("(");
            sb5.append(replaceComma(this.HRM_spec_data[Index]));
            sb5.append(")");
            return sb5.toString();
        }
    }

    private String replaceComma(String Spec) {
        if (Spec == null) {
            return "";
        }
        return Spec.replaceAll("\\s", "").replace(",", "~");
    }

    private void readTestSpec() {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        int Index;
        this.HRM_spec_data = new String[this.itemName.length];
        if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equals(this.mHrm_Vendor)) {
            if (this.mHrm_Name.contains("MAX86915") || this.mHrm_Name.contains("MAX86917")) {
                int Index2 = 0 + 1;
                this.HRM_spec_data[0] = Spec.getString(Spec.HRM_SYSTEM_NOISE);
                int Index3 = Index2 + 1;
                this.HRM_spec_data[Index2] = Spec.getString(Spec.HRM_IR_LOW_DC);
                int Index4 = Index3 + 1;
                this.HRM_spec_data[Index3] = Spec.getString(Spec.HRM_RED_LOW_DC);
                int Index5 = Index4 + 1;
                this.HRM_spec_data[Index4] = Spec.getString(Spec.HRM_GREEN_LOW_DC);
                int Index6 = Index5 + 1;
                this.HRM_spec_data[Index5] = Spec.getString(Spec.HRM_BLUE_LOW_DC);
                int Index7 = Index6 + 1;
                this.HRM_spec_data[Index6] = Spec.getString(Spec.HRM_IR_MEDIUM_DC);
                int Index8 = Index7 + 1;
                this.HRM_spec_data[Index7] = Spec.getString(Spec.HRM_RED_MEDIUM_DC);
                int Index9 = Index8 + 1;
                this.HRM_spec_data[Index8] = Spec.getString(Spec.HRM_GREEN_MEDIUM_DC);
                int Index10 = Index9 + 1;
                this.HRM_spec_data[Index9] = Spec.getString(Spec.HRM_BLUE_MEDIUM_DC);
                int Index11 = Index10 + 1;
                this.HRM_spec_data[Index10] = Spec.getString(Spec.HRM_IR_HIGH_DC);
                int Index12 = Index11 + 1;
                this.HRM_spec_data[Index11] = Spec.getString(Spec.HRM_RED_HIGH_DC);
                int Index13 = Index12 + 1;
                this.HRM_spec_data[Index12] = Spec.getString(Spec.HRM_GREEN_HIGH_DC);
                int Index14 = Index13 + 1;
                this.HRM_spec_data[Index13] = Spec.getString(Spec.HRM_BLUE_HIGH_DC);
                int Index15 = Index14 + 1;
                this.HRM_spec_data[Index14] = Spec.getString(Spec.HRM_IR_XTC_DC);
                int Index16 = Index15 + 1;
                this.HRM_spec_data[Index15] = Spec.getString(Spec.HRM_RED_XTC_DC);
                int Index17 = Index16 + 1;
                this.HRM_spec_data[Index16] = Spec.getString(Spec.HRM_GREEN_XTC_DC);
                int Index18 = Index17 + 1;
                this.HRM_spec_data[Index17] = Spec.getString(Spec.HRM_BLUE_XTC_DC);
                int Index19 = Index18 + 1;
                this.HRM_spec_data[Index18] = Spec.getString(Spec.HRM_IR_NOISE);
                int Index20 = Index19 + 1;
                this.HRM_spec_data[Index19] = Spec.getString(Spec.HRM_RED_NOISE);
                int Index21 = Index20 + 1;
                this.HRM_spec_data[Index20] = Spec.getString(Spec.HRM_GREEN_NOISE);
                int Index22 = Index21 + 1;
                this.HRM_spec_data[Index21] = Spec.getString(Spec.HRM_BLUE_NOISE);
                int i = Index22 + 1;
                this.HRM_spec_data[Index22] = Spec.getString(Spec.HRM_FREQUENCY);
            } else if (Feature.getBoolean(Feature.HRM_NEW_EOL_TEST)) {
                int Index23 = 0 + 1;
                this.HRM_spec_data[0] = Spec.getString(Spec.HRM_SYSTEM_NOISE);
                int Index24 = Index23 + 1;
                this.HRM_spec_data[Index23] = Spec.getString(Spec.HRM_IR_LOW_DC);
                int Index25 = Index24 + 1;
                this.HRM_spec_data[Index24] = Spec.getString(Spec.HRM_RED_LOW_DC);
                int Index26 = Index25 + 1;
                this.HRM_spec_data[Index25] = Spec.getString(Spec.HRM_IR_MEDIUM_DC);
                int Index27 = Index26 + 1;
                this.HRM_spec_data[Index26] = Spec.getString(Spec.HRM_RED_MEDIUM_DC);
                int Index28 = Index27 + 1;
                this.HRM_spec_data[Index27] = Spec.getString(Spec.HRM_IR_HIGH_DC);
                int Index29 = Index28 + 1;
                this.HRM_spec_data[Index28] = Spec.getString(Spec.HRM_RED_HIGH_DC);
                int Index30 = Index29 + 1;
                this.HRM_spec_data[Index29] = Spec.getString(Spec.HRM_IR_NOISE);
                int Index31 = Index30 + 1;
                this.HRM_spec_data[Index30] = Spec.getString(Spec.HRM_RED_NOISE);
                int i2 = Index31 + 1;
                this.HRM_spec_data[Index31] = Spec.getString(Spec.HRM_FREQUENCY);
            } else {
                String[] strArr = this.HRM_spec_data;
                int Index32 = 0 + 1;
                if (isDualSensorHRM) {
                    str = Spec.getString(Spec.HRM_86900A_PEAK_TO_PEAK_PEAK_IR);
                } else {
                    str = Spec.getString(Spec.HRM_PEAK_TO_PEAK_PEAK_IR);
                }
                strArr[0] = str;
                String[] strArr2 = this.HRM_spec_data;
                int Index33 = Index32 + 1;
                if (isDualSensorHRM) {
                    str2 = Spec.getString(Spec.HRM_86900A_PEAK_TO_PEAK_DC_IR);
                } else {
                    str2 = Spec.getString(Spec.HRM_PEAK_TO_PEAK_DC_IR);
                }
                strArr2[Index32] = str2;
                int Index34 = Index33 + 1;
                this.HRM_spec_data[Index33] = Spec.getString(Spec.HRM_PEAK_TO_PEAK_RATIO_IR);
                int Index35 = Index34 + 1;
                this.HRM_spec_data[Index34] = Spec.getString(Spec.HRM_FREQUENCY_SAMPLE_NO);
                String[] strArr3 = this.HRM_spec_data;
                int Index36 = Index35 + 1;
                if (isDualSensorHRM) {
                    str3 = Spec.getString(Spec.HRM_86900A_FREQUENCY_DC_IR);
                } else {
                    str3 = Spec.getString(Spec.HRM_FREQUENCY_DC_IR);
                }
                strArr3[Index35] = str3;
                int Index37 = Index36 + 1;
                this.HRM_spec_data[Index36] = Spec.getString(Spec.HRM_FREQUENCY_NOISE_IR);
                String[] strArr4 = this.HRM_spec_data;
                int Index38 = Index37 + 1;
                if (isDualSensorHRM) {
                    str4 = Spec.getString(Spec.HRM_86900A_PEAK_TO_PEAK_PEAK_RED);
                } else {
                    str4 = Spec.getString(Spec.HRM_PEAK_TO_PEAK_PEAK_RED);
                }
                strArr4[Index37] = str4;
                int Index39 = Index38 + 1;
                this.HRM_spec_data[Index38] = Spec.getString(Spec.HRM_PEAK_TO_PEAK_DC_RED);
                int Index40 = Index39 + 1;
                this.HRM_spec_data[Index39] = Spec.getString(Spec.HRM_PEAK_TO_PEAK_RATIO_RED);
                int Index41 = Index40 + 1;
                this.HRM_spec_data[Index40] = Spec.getString(Spec.HRM_FREQUENCY_SAMPLE_NO);
                String[] strArr5 = this.HRM_spec_data;
                int Index42 = Index41 + 1;
                if (isDualSensorHRM) {
                    str5 = Spec.getString(Spec.HRM_86900A_FREQUENCY_DC_RED);
                } else {
                    str5 = Spec.getString(Spec.HRM_FREQUENCY_DC_RED);
                }
                strArr5[Index41] = str5;
                int Index43 = Index42 + 1;
                this.HRM_spec_data[Index42] = Spec.getString(Spec.HRM_FREQUENCY_NOISE_RED);
                if (Feature.getBoolean(Feature.SUPPORT_HRM_SPO2)) {
                    int Index44 = Index43 + 1;
                    this.HRM_spec_data[Index43] = Spec.getString(Spec.HRM_HR_SPO2);
                    Index43 = Index44;
                }
                if (this.isCloudTest) {
                    if (this.itemUsable_MAXIM_Cloud[0]) {
                        Index = Index43 + 1;
                        this.HRM_spec_data[Index43] = Spec.getString(Spec.HRM_CLOUD_UV_RATIO);
                    } else {
                        Index = Index43;
                    }
                    if (this.itemUsable_MAXIM_Cloud[1]) {
                        int i3 = Index + 1;
                        this.HRM_spec_data[Index] = Spec.getString(Spec.HRM_IR_RED_R_RATIO);
                    }
                }
            }
        } else if ("PARTRON".equals(this.mHrm_Vendor)) {
            this.HRM_spec_data[0] = Spec.getString(Spec.HRM_IR_FREQUENCY);
            this.HRM_spec_data[1] = Spec.getString(Spec.HRM_IR_LOW_DC);
            this.HRM_spec_data[2] = Spec.getString(Spec.HRM_IR_HI_DC);
            this.HRM_spec_data[3] = Spec.getString(Spec.HRM_IR_AC);
            this.HRM_spec_data[4] = Spec.getString(Spec.HRM_IR_AC_DC_RATIO);
            this.HRM_spec_data[5] = Spec.getString(Spec.HRM_IR_NOISE);
            this.HRM_spec_data[6] = Spec.getString(Spec.HRM_RED_LOW_NOISE);
            this.HRM_spec_data[7] = Spec.getString(Spec.HRM_RED_HI_DC);
            this.HRM_spec_data[8] = Spec.getString(Spec.HRM_RED_AC);
            this.HRM_spec_data[9] = Spec.getString(Spec.HRM_RED_AC_DC_RATIO);
            this.HRM_spec_data[10] = Spec.getString(Spec.HRM_RED_NOISE);
            this.HRM_spec_data[11] = Spec.getString(Spec.HRM_IR_RED_R_RATIO);
        } else if ("ADI".equals(this.mHrm_Vendor) && "ADPD143RI".equals(this.mHrm_Name)) {
            this.HRM_spec_data[0] = Spec.getString(Spec.HRM_FREQUENCY);
            this.HRM_spec_data[1] = Spec.getString(Spec.HRM_RED_LOW_DC);
            this.HRM_spec_data[2] = Spec.getString(Spec.HRM_IR_LOW_DC);
            this.HRM_spec_data[3] = Spec.getString(Spec.HRM_RED_MEDIUM_DC);
            this.HRM_spec_data[4] = Spec.getString(Spec.HRM_IR_MEDIUM_DC);
            this.HRM_spec_data[5] = Spec.getString(Spec.HRM_RED_MEDIUM_SQUARED_NOISE);
            this.HRM_spec_data[6] = Spec.getString(Spec.HRM_IR_MEDIUM_SQUARED_NOISE);
            this.HRM_spec_data[7] = Spec.getString(Spec.HRM_RED_HIGH_DC);
            this.HRM_spec_data[8] = Spec.getString(Spec.HRM_IR_HIGH_DC);
        }
    }
}
