package com.sec.android.app.hwmoduletest;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.modules.ModuleSensor;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.android.app.hwmoduletest.view.HrmGraph;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class HrmTest extends BaseActivity {
    private int ITEM_NUM = 4;
    private final byte MSG_UPDATE_BIO_SENSOR_VALUE = 10;
    private final byte MSG_UPDATE_HRM_SENSOR_VALUE = HwModuleTest.ID_SLEEP;
    private final byte MSG_UPDATE_HRM_THD_VALUE = HwModuleTest.ID_SUB_KEY;
    public final int MSG_UPDATE_UI = 0;
    private final int UpdateBio = 0;
    private final int UpdateHrm = 1;
    private final int UpdateHrmThd = 2;
    private String hrm_thd;
    /* access modifiers changed from: private */
    public String hrm_vendor;
    private LinearLayout linearLayout;
    private Sensor mBIOSensor;
    private BIOSensorTask mBIOSensorTask;
    /* access modifiers changed from: private */
    public DecimalFormat mFormat;
    /* access modifiers changed from: private */
    public float mHRMHRAvg = 0.0f;
    /* access modifiers changed from: private */
    public int mHRMHRCount = 1;
    /* access modifiers changed from: private */
    public float mHRMHRMax = 0.0f;
    /* access modifiers changed from: private */
    public float mHRMHRMin = 100.0f;
    /* access modifiers changed from: private */
    public float mHRMHRTotal = 0.0f;
    /* access modifiers changed from: private */
    public float mHRMHeartRate;
    /* access modifiers changed from: private */
    public float mHRMIRLevel;
    /* access modifiers changed from: private */
    public float mHRMREDLevel;
    /* access modifiers changed from: private */
    public float mHRMRvalue;
    private Sensor mHRMSensor;
    private HRMSensorTask mHRMSensorTask;
    /* access modifiers changed from: private */
    public float mHRMSignalQtyAvg = 0.0f;
    /* access modifiers changed from: private */
    public int mHRMSignalQtyCount = 1;
    /* access modifiers changed from: private */
    public float mHRMSignalQtyMax = 0.0f;
    /* access modifiers changed from: private */
    public float mHRMSignalQtyMin = 100.0f;
    /* access modifiers changed from: private */
    public float mHRMSignalQtyTotal = 0.0f;
    /* access modifiers changed from: private */
    public float mHRMSignalQuality;
    /* access modifiers changed from: private */
    public float mHRMSpO2;
    /* access modifiers changed from: private */
    public float mHRMSpO2Avg = 0.0f;
    /* access modifiers changed from: private */
    public int mHRMSpO2Count = 1;
    /* access modifiers changed from: private */
    public float mHRMSpO2Max = 0.0f;
    /* access modifiers changed from: private */
    public float mHRMSpO2Min = 100.0f;
    /* access modifiers changed from: private */
    public float mHRMSpO2Total = 0.0f;
    /* access modifiers changed from: private */
    public float mHRMTemp;
    /* access modifiers changed from: private */
    public float mHRM_RRI;
    /* access modifiers changed from: private */
    public float mHRM_RRI_Avg = 0.0f;
    /* access modifiers changed from: private */
    public int mHRM_RRI_Count = 1;
    /* access modifiers changed from: private */
    public float mHRM_RRI_Max = 0.0f;
    /* access modifiers changed from: private */
    public float mHRM_RRI_Min = 100.0f;
    /* access modifiers changed from: private */
    public float mHRM_RRI_Total = 0.0f;
    /* access modifiers changed from: private */
    public float mHRM_SNR;
    /* access modifiers changed from: private */
    public float mHRM_SNR_Avg = 0.0f;
    /* access modifiers changed from: private */
    public int mHRM_SNR_Count = 1;
    /* access modifiers changed from: private */
    public float mHRM_SNR_Max = 0.0f;
    /* access modifiers changed from: private */
    public float mHRM_SNR_Min = 100.0f;
    /* access modifiers changed from: private */
    public float mHRM_SNR_Total = 0.0f;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10:
                    HrmTest.this.updateUI(0);
                    return;
                case 11:
                    HrmTest.this.updateUI(1);
                    return;
                case 12:
                    HrmTest.this.updateUI(2);
                    return;
                default:
                    return;
            }
        }
    };
    private HrmGraph mHrmGraph;
    private SensorManager mSensorManager;
    /* access modifiers changed from: private */
    public DecimalFormat mThreeFormat;
    private Timer mTimer;
    private TextView[] txt_item;

    private class BIOSensorTask extends TimerTask implements SensorEventListener {
        private float[] mBIOSensorValues;
        private boolean mIsRunningTask;

        private BIOSensorTask() {
            this.mIsRunningTask = false;
            this.mBIOSensorValues = new float[4];
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == 65561) {
                this.mBIOSensorValues = (float[]) event.values.clone();
            }
        }

        public void run() {
            if (this.mIsRunningTask) {
                readToBIOSensor();
                HrmTest.this.mHandler.sendEmptyMessage(10);
            }
        }

        /* access modifiers changed from: private */
        public void resume() {
            this.mIsRunningTask = true;
        }

        /* access modifiers changed from: private */
        public void pause() {
            this.mIsRunningTask = false;
            if (HrmTest.this.mHandler.hasMessages(10)) {
                HrmTest.this.mHandler.removeMessages(10);
            }
        }

        private void readToBIOSensor() {
            StringBuilder sb = new StringBuilder();
            sb.append("mBIOSensorValues :");
            sb.append(this.mBIOSensorValues[0]);
            sb.append(", ");
            sb.append(this.mBIOSensorValues[1]);
            sb.append(", ");
            sb.append(this.mBIOSensorValues[2]);
            LtUtil.log_d(HrmTest.this.CLASS_NAME, "readToBIOSensor", sb.toString());
            HrmTest.this.mHRMIRLevel = this.mBIOSensorValues[0];
            HrmTest.this.mHRMREDLevel = this.mBIOSensorValues[1];
            if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equals(HrmTest.this.hrm_vendor) || "PARTRON".equals(HrmTest.this.hrm_vendor)) {
                HrmTest.this.mHRMTemp = this.mBIOSensorValues[2];
            }
        }

        /* access modifiers changed from: private */
        public String getTempValueString() {
            if (HrmTest.this.mHRMTemp == 0.0f) {
                return "Temp('C) : --";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Temp('C) : ");
            sb.append((int) (HrmTest.this.mHRMTemp / 16.0f));
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getIRValueString() {
            if (HrmTest.this.mHRMIRLevel == 0.0f) {
                return "IR(ADC) : --";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("IR(ADC) : ");
            sb.append((int) HrmTest.this.mHRMIRLevel);
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getREDValueString() {
            if (HrmTest.this.mHRMREDLevel == 0.0f) {
                return "RED(ADC) : --";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("RED(ADC) : ");
            sb.append((int) HrmTest.this.mHRMREDLevel);
            return sb.toString();
        }
    }

    private class HRMSensorTask extends TimerTask implements SensorEventListener {
        private float[] mHRMSensorValues;
        private boolean mIsRunningTask;

        private HRMSensorTask() {
            this.mIsRunningTask = false;
            this.mHRMSensorValues = new float[5];
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == 65562) {
                this.mHRMSensorValues = (float[]) event.values.clone();
            }
        }

        public void run() {
            if (this.mIsRunningTask) {
                readToHRMSensor();
                HrmTest.this.mHandler.sendEmptyMessage(11);
            }
        }

        /* access modifiers changed from: private */
        public void resume() {
            this.mIsRunningTask = true;
        }

        /* access modifiers changed from: private */
        public void pause() {
            this.mIsRunningTask = false;
            if (HrmTest.this.mHandler.hasMessages(11)) {
                HrmTest.this.mHandler.removeMessages(11);
            }
        }

        private void readToHRMSensor() {
            StringBuilder sb = new StringBuilder();
            sb.append("mHRMSensorValues :");
            sb.append(this.mHRMSensorValues[0]);
            sb.append(", ");
            sb.append(this.mHRMSensorValues[1]);
            sb.append(", ");
            sb.append(this.mHRMSensorValues[2]);
            LtUtil.log_d(HrmTest.this.CLASS_NAME, "readToHRMSensor", sb.toString());
            HrmTest.this.mHRMHeartRate = this.mHRMSensorValues[0];
            HrmTest.this.mHRMRvalue = StoreHRMSensorValue(4);
            if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equals(HrmTest.this.hrm_vendor)) {
                HrmTest.this.mHRMSignalQuality = this.mHRMSensorValues[2];
            } else if ("ADI".equals(HrmTest.this.hrm_vendor) || "PARTRON".equals(HrmTest.this.hrm_vendor)) {
                HrmTest.this.mHRM_RRI = this.mHRMSensorValues[1];
                HrmTest.this.mHRM_SNR = this.mHRMSensorValues[2];
            }
        }

        /* access modifiers changed from: private */
        public String getHeartRateValueString() {
            if (HrmTest.this.mHRMHeartRate == 0.0f && HrmTest.this.mHRMSpO2 == 0.0f && HrmTest.this.mHRMRvalue == 0.0f) {
                return "HR(Bpm) : --";
            }
            if (HrmTest.this.mHRMHRMin > HrmTest.this.mHRMHeartRate) {
                HrmTest.this.mHRMHRMin = HrmTest.this.mHRMHeartRate;
            }
            if (HrmTest.this.mHRMHRMax < HrmTest.this.mHRMHeartRate) {
                HrmTest.this.mHRMHRMax = HrmTest.this.mHRMHeartRate;
            }
            HrmTest.access$2816(HrmTest.this, HrmTest.this.mHRMHeartRate);
            HrmTest.this.mHRMHRAvg = HrmTest.this.mHRMHRTotal / ((float) HrmTest.this.mHRMHRCount);
            HrmTest.this.mHRMHRCount = HrmTest.this.mHRMHRCount + 1;
            if (!Feature.getBoolean(Feature.SUPPORT_HRM_R, true)) {
                StringBuilder sb = new StringBuilder();
                sb.append("HR(Bpm) : ");
                sb.append((int) HrmTest.this.mHRMHeartRate);
                sb.append(" ( ");
                sb.append((int) HrmTest.this.mHRMHRMin);
                sb.append(", ");
                sb.append((int) HrmTest.this.mHRMHRAvg);
                sb.append(", ");
                sb.append((int) HrmTest.this.mHRMHRMax);
                sb.append(" )");
                return sb.toString();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("HR(Bpm) : ");
            sb2.append((int) HrmTest.this.mHRMHeartRate);
            sb2.append(" ( ");
            sb2.append((int) HrmTest.this.mHRMHRMin);
            sb2.append(", ");
            sb2.append((int) HrmTest.this.mHRMHRAvg);
            sb2.append(", ");
            sb2.append((int) HrmTest.this.mHRMHRMax);
            sb2.append(", ");
            sb2.append(HrmTest.this.mThreeFormat.format((double) HrmTest.this.mHRMRvalue));
            sb2.append(" )");
            return sb2.toString();
        }

        private String getSpO2ValueString() {
            if (HrmTest.this.mHRMSpO2 == 0.0f) {
                return "SpO2(%) : --";
            }
            if (HrmTest.this.mHRMSpO2Min > HrmTest.this.mHRMSpO2) {
                HrmTest.this.mHRMSpO2Min = HrmTest.this.mHRMSpO2;
            }
            if (HrmTest.this.mHRMSpO2Max < HrmTest.this.mHRMSpO2) {
                HrmTest.this.mHRMSpO2Max = HrmTest.this.mHRMSpO2;
            }
            HrmTest.access$3416(HrmTest.this, HrmTest.this.mHRMSpO2);
            HrmTest.this.mHRMSpO2Avg = HrmTest.this.mHRMSpO2Total / ((float) HrmTest.this.mHRMSpO2Count);
            HrmTest.this.mHRMSpO2Count = HrmTest.this.mHRMSpO2Count + 1;
            StringBuilder sb = new StringBuilder();
            sb.append("SpO2(%) : ");
            sb.append((int) HrmTest.this.mHRMSpO2);
            sb.append(" ( ");
            sb.append((int) HrmTest.this.mHRMSpO2Min);
            sb.append(", ");
            sb.append((int) HrmTest.this.mHRMSpO2Avg);
            sb.append(", ");
            sb.append((int) HrmTest.this.mHRMSpO2Max);
            sb.append(" )");
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getSNRValueString() {
            if (HrmTest.this.mHRM_SNR == 0.0f) {
                return "SNR(db) : --";
            }
            if (HrmTest.this.mHRM_SNR_Min > HrmTest.this.mHRM_SNR) {
                HrmTest.this.mHRM_SNR_Min = HrmTest.this.mHRM_SNR;
            }
            if (HrmTest.this.mHRM_SNR_Max < HrmTest.this.mHRM_SNR) {
                HrmTest.this.mHRM_SNR_Max = HrmTest.this.mHRM_SNR;
            }
            HrmTest.access$3916(HrmTest.this, HrmTest.this.mHRM_SNR);
            HrmTest.this.mHRM_SNR_Avg = HrmTest.this.mHRM_SNR_Total / ((float) HrmTest.this.mHRM_SNR_Count);
            HrmTest.this.mHRM_SNR_Count = HrmTest.this.mHRM_SNR_Count + 1;
            StringBuilder sb = new StringBuilder();
            sb.append("SNR(db) : ");
            sb.append(HrmTest.this.mFormat.format((double) HrmTest.this.mHRM_SNR));
            sb.append(" ( ");
            sb.append(HrmTest.this.mFormat.format((double) HrmTest.this.mHRM_SNR_Min));
            sb.append(", ");
            sb.append(HrmTest.this.mFormat.format((double) HrmTest.this.mHRM_SNR_Avg));
            sb.append(", ");
            sb.append(HrmTest.this.mFormat.format((double) HrmTest.this.mHRM_SNR_Max));
            sb.append(" )");
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getRRIValueString() {
            if (HrmTest.this.mHRM_RRI == 0.0f) {
                return "RRI(ms) : --";
            }
            if (HrmTest.this.mHRM_RRI_Min > HrmTest.this.mHRM_RRI) {
                HrmTest.this.mHRM_RRI_Min = HrmTest.this.mHRM_RRI;
            }
            if (HrmTest.this.mHRM_RRI_Max < HrmTest.this.mHRM_RRI) {
                HrmTest.this.mHRM_RRI_Max = HrmTest.this.mHRM_RRI;
            }
            HrmTest.access$4516(HrmTest.this, HrmTest.this.mHRM_RRI);
            HrmTest.this.mHRM_RRI_Avg = HrmTest.this.mHRM_RRI_Total / ((float) HrmTest.this.mHRM_RRI_Count);
            HrmTest.this.mHRM_RRI_Count = HrmTest.this.mHRM_RRI_Count + 1;
            StringBuilder sb = new StringBuilder();
            sb.append("RRI(ms) : ");
            sb.append(HrmTest.this.mFormat.format((double) HrmTest.this.mHRM_RRI));
            sb.append(" ( ");
            sb.append(HrmTest.this.mFormat.format((double) HrmTest.this.mHRM_RRI_Min));
            sb.append(", ");
            sb.append(HrmTest.this.mFormat.format((double) HrmTest.this.mHRM_RRI_Avg));
            sb.append(", ");
            sb.append(HrmTest.this.mFormat.format((double) HrmTest.this.mHRM_RRI_Max));
            sb.append(" )");
            return sb.toString();
        }

        /* access modifiers changed from: private */
        public String getSignalQulityValueString() {
            if (HrmTest.this.mHRMSignalQuality == 0.0f) {
                return "Signal Qty(%) : --";
            }
            if (HrmTest.this.mHRMSignalQtyMin > HrmTest.this.mHRMSignalQuality) {
                HrmTest.this.mHRMSignalQtyMin = HrmTest.this.mHRMSignalQuality;
            }
            if (HrmTest.this.mHRMSignalQtyMax < HrmTest.this.mHRMSignalQuality) {
                HrmTest.this.mHRMSignalQtyMax = HrmTest.this.mHRMSignalQuality;
            }
            HrmTest.access$5016(HrmTest.this, HrmTest.this.mHRMSignalQuality);
            HrmTest.this.mHRMSignalQtyAvg = HrmTest.this.mHRMSignalQtyTotal / ((float) HrmTest.this.mHRMSignalQtyCount);
            HrmTest.this.mHRMSignalQtyCount = HrmTest.this.mHRMSignalQtyCount + 1;
            StringBuilder sb = new StringBuilder();
            sb.append("Signal Qty(%) : ");
            sb.append(HrmTest.this.mFormat.format((double) HrmTest.this.mHRMSignalQuality));
            sb.append(" ( ");
            sb.append(HrmTest.this.mFormat.format((double) HrmTest.this.mHRMSignalQtyMin));
            sb.append(", ");
            sb.append(HrmTest.this.mFormat.format((double) HrmTest.this.mHRMSignalQtyAvg));
            sb.append(", ");
            sb.append(HrmTest.this.mFormat.format((double) HrmTest.this.mHRMSignalQtyMax));
            sb.append(" )");
            return sb.toString();
        }

        private float StoreHRMSensorValue(int ArrayIndex) {
            if (this.mHRMSensorValues.length > ArrayIndex) {
                return this.mHRMSensorValues[ArrayIndex];
            }
            return 0.0f;
        }
    }

    static /* synthetic */ float access$2816(HrmTest x0, float x1) {
        float f = x0.mHRMHRTotal + x1;
        x0.mHRMHRTotal = f;
        return f;
    }

    static /* synthetic */ float access$3416(HrmTest x0, float x1) {
        float f = x0.mHRMSpO2Total + x1;
        x0.mHRMSpO2Total = f;
        return f;
    }

    static /* synthetic */ float access$3916(HrmTest x0, float x1) {
        float f = x0.mHRM_SNR_Total + x1;
        x0.mHRM_SNR_Total = f;
        return f;
    }

    static /* synthetic */ float access$4516(HrmTest x0, float x1) {
        float f = x0.mHRM_RRI_Total + x1;
        x0.mHRM_RRI_Total = f;
        return f;
    }

    static /* synthetic */ float access$5016(HrmTest x0, float x1) {
        float f = x0.mHRMSignalQtyTotal + x1;
        x0.mHRMSignalQtyTotal = f;
        return f;
    }

    public HrmTest() {
        super("HrmTest");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.hrmtest);
        this.mHrmGraph = (HrmGraph) findViewById(C0268R.C0269id.hrm_sensor_graph);
        init();
        DecimalFormatSymbols paramDecimalFormatSymbols = new DecimalFormatSymbols();
        paramDecimalFormatSymbols.setDecimalSeparator('.');
        this.mFormat = new DecimalFormat("#.##", paramDecimalFormatSymbols);
        this.mThreeFormat = new DecimalFormat("#.###", paramDecimalFormatSymbols);
        if (this.mTimer == null) {
            this.mTimer = new Timer();
        }
        this.mTimer.schedule(this.mBIOSensorTask, 0, 10);
        this.mTimer.schedule(this.mHRMSensorTask, 0, 1000);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mSensorManager.registerListener(this.mBIOSensorTask, this.mBIOSensor, 2);
        this.mSensorManager.registerListener(this.mHRMSensorTask, this.mHRMSensor, 2);
        this.mBIOSensorTask.resume();
        this.mHRMSensorTask.resume();
        if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equals(this.hrm_vendor)) {
            if (this.ITEM_NUM == 4) {
                this.mHandler.sendEmptyMessageDelayed(12, 300);
            }
        } else if (("ADI".equals(this.hrm_vendor) || "PARTRON".equals(this.hrm_vendor)) && this.ITEM_NUM == 6) {
            this.mHandler.sendEmptyMessageDelayed(12, 300);
        }
    }

    public void updateUI(int type) {
        if (type == 0) {
            if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equals(this.hrm_vendor)) {
                this.txt_item[2].setText(this.mBIOSensorTask.getTempValueString());
            } else if ("ADI".equals(this.hrm_vendor) || "PARTRON".equals(this.hrm_vendor)) {
                this.txt_item[3].setText(this.mBIOSensorTask.getIRValueString());
                this.txt_item[4].setText(this.mBIOSensorTask.getREDValueString());
            }
            this.mHrmGraph.addValueIR(this.mHRMIRLevel);
            this.mHrmGraph.addValueRED(this.mHRMREDLevel);
        } else if (type == 1) {
            this.txt_item[0].setText(this.mHRMSensorTask.getHeartRateValueString());
            if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equals(this.hrm_vendor)) {
                this.txt_item[1].setText(this.mHRMSensorTask.getSignalQulityValueString());
            } else if ("ADI".equals(this.hrm_vendor) || "PARTRON".equals(this.hrm_vendor)) {
                this.txt_item[1].setText(this.mHRMSensorTask.getSNRValueString());
                this.txt_item[2].setText(this.mHRMSensorTask.getRRIValueString());
            }
        } else if (type == 2) {
            this.hrm_thd = Kernel.read(Kernel.HRM_THD);
            if ("ADI".equals(this.hrm_vendor) || "PARTRON".equals(this.hrm_vendor)) {
                TextView textView = this.txt_item[5];
                StringBuilder sb = new StringBuilder();
                sb.append("THD : ");
                sb.append(this.hrm_thd);
                textView.setText(sb.toString());
                return;
            }
            TextView textView2 = this.txt_item[3];
            StringBuilder sb2 = new StringBuilder();
            sb2.append("THD : ");
            sb2.append(this.hrm_thd);
            textView2.setText(sb2.toString());
        } else {
            LtUtil.log_d(this.CLASS_NAME, "updateUI", "update type is invalid");
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mBIOSensorTask.pause();
        this.mHRMSensorTask.pause();
        this.mSensorManager.unregisterListener(this.mBIOSensorTask);
        this.mSensorManager.unregisterListener(this.mHRMSensorTask);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mBIOSensorTask.cancel();
        this.mHRMSensorTask.cancel();
        this.mTimer.purge();
        this.mTimer.cancel();
        this.mBIOSensorTask = null;
        this.mHRMSensorTask = null;
        this.mTimer = null;
        LtUtil.log_d(this.CLASS_NAME, "onDestroy", "bio hrm task, hrm timer - purge cancel");
        super.onDestroy();
    }

    private void init() {
        this.hrm_vendor = Kernel.read(Kernel.HRM_VENDOR);
        if (this.hrm_vendor != null) {
            this.hrm_vendor = this.hrm_vendor.toUpperCase(Locale.ENGLISH);
        }
        if ("OSRAM".equalsIgnoreCase(this.hrm_vendor)) {
            this.hrm_vendor = ModuleSensor.FEATURE_GYROSCOP_MAXIM;
            LtUtil.log_i(this.CLASS_NAME, "init", "Change the HRM vendor name(OSRAM) to MAXIM temporary");
        }
        if (ModuleSensor.FEATURE_GYROSCOP_MAXIM.equals(this.hrm_vendor)) {
            this.ITEM_NUM = 3;
            if (Kernel.isExistFile(Kernel.HRM_THD)) {
                this.ITEM_NUM = 4;
            }
        } else if ("ADI".equals(this.hrm_vendor)) {
            this.ITEM_NUM = 5;
            if (Kernel.isExistFile(Kernel.HRM_THD)) {
                this.ITEM_NUM = 6;
            }
        } else if ("PARTRON".equals(this.hrm_vendor)) {
            this.ITEM_NUM = 6;
        }
        this.linearLayout = (LinearLayout) findViewById(C0268R.C0269id.linear);
        this.txt_item = new TextView[this.ITEM_NUM];
        for (int i = 0; i < this.ITEM_NUM; i++) {
            this.txt_item[i] = new TextView(this);
            this.txt_item[i].setTextColor(-16777216);
            this.txt_item[i].setTextSize(1, 20.0f);
            this.txt_item[i].setTextColor(-16777216);
            this.linearLayout.addView(this.txt_item[i]);
        }
        this.mBIOSensorTask = new BIOSensorTask();
        this.mHRMSensorTask = new HRMSensorTask();
        this.mSensorManager = (SensorManager) getSystemService("sensor");
        this.mBIOSensor = this.mSensorManager.getDefaultSensor(65561);
        this.mHRMSensor = this.mSensorManager.getDefaultSensor(65562);
    }
}
