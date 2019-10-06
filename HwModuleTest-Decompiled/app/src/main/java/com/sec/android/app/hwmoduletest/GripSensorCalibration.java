package com.sec.android.app.hwmoduletest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import egis.client.api.EgisFingerprint;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class GripSensorCalibration extends Activity implements OnClickListener {
    private static final String CLASS_NAME = "GripSensorCalibration";
    private static final byte MSG_UPDATE_UI = 0;
    private final String CALDATA_SYSFS_PATH = "/sys/class/sensors/grip_sensor/calibration";
    private final byte CALIBRATION = 49;
    private final byte CALIBRATION_ERASE = 48;
    private final String CAL_SYSFS_PATH = "/sys/class/sensors/grip_sensor/calibration";
    private final String CSPERSENT_SYSFS_PATH = "/sys/class/sensors/grip_sensor/raw_data";
    private final byte GRIP_OFF = 48;
    private final byte GRIP_ON = 49;
    private final String ONOFF_SYSFS_PATH = "/sys/class/sensors/grip_sensor/onoff";
    private final String PROXIMITYPERCENT_SYSFS_PATH = "/sys/class/sensors/grip_sensor/threshold";
    /* access modifiers changed from: private */
    public boolean isTimerFinish = false;
    private boolean isgrip_On = true;
    /* access modifiers changed from: private */
    public boolean ispass = true;
    private Button mButton_Calibration;
    private Button mButton_CalibrationErase;
    private Button mButton_Skip;
    private Button mButton_onoff;
    /* access modifiers changed from: private */
    public String mCaldata_data = "";
    private String mCspercent_data = "";
    /* access modifiers changed from: private */
    public String mProxpercent_data = "";
    /* access modifiers changed from: private */
    public TextView mText_Caldata;
    private TextView mText_RAW_Count;
    private TextView mText_RAW_Count_crcount;
    /* access modifiers changed from: private */
    public TextView mText_Result;
    /* access modifiers changed from: private */
    public TextView mText_SPEC;
    /* access modifiers changed from: private */
    public Handler mTimerHandler;
    private Handler mUIHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                TextView access$400 = GripSensorCalibration.this.mText_SPEC;
                StringBuilder sb = new StringBuilder();
                sb.append(" : ");
                sb.append(GripSensorCalibration.this.mProxpercent_data);
                access$400.setText(sb.toString());
                if (!GripSensorCalibration.this.mCaldata_data.equals("exception")) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("mCaldata_data : ");
                    sb2.append(GripSensorCalibration.this.mCaldata_data);
                    LtUtil.log_d(GripSensorCalibration.CLASS_NAME, "MSG_UPDATE_UI", sb2.toString());
                    String[] caldatas = GripSensorCalibration.this.mCaldata_data.split(",");
                    if (caldatas[0] != null) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("caldatas[0] : ");
                        sb3.append(caldatas[0]);
                        LtUtil.log_d(GripSensorCalibration.CLASS_NAME, "MSG_UPDATE_UI", sb3.toString());
                        if (caldatas[0].equals(EgisFingerprint.MAJOR_VERSION)) {
                            TextView access$600 = GripSensorCalibration.this.mText_Caldata;
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append(" :  Y (");
                            sb4.append(GripSensorCalibration.this.mCaldata_data.substring(2));
                            sb4.append(")");
                            access$600.setText(sb4.toString());
                        } else {
                            TextView access$6002 = GripSensorCalibration.this.mText_Caldata;
                            StringBuilder sb5 = new StringBuilder();
                            sb5.append(" :  N (");
                            sb5.append(GripSensorCalibration.this.mCaldata_data.substring(3));
                            sb5.append(")");
                            access$6002.setText(sb5.toString());
                        }
                    }
                } else {
                    TextView access$6003 = GripSensorCalibration.this.mText_Caldata;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(" : ");
                    sb6.append(GripSensorCalibration.this.mCaldata_data);
                    access$6003.setText(sb6.toString());
                }
                if (!GripSensorCalibration.this.ispass) {
                    GripSensorCalibration.this.mText_Result.setText("FAIL");
                    GripSensorCalibration.this.mText_Result.setTextColor(-65536);
                    return;
                }
                GripSensorCalibration.this.mText_Result.setText("PASS");
                GripSensorCalibration.this.mText_Result.setTextColor(-16776961);
            }
        }
    };

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        LtUtil.log_d(CLASS_NAME, "onCreate", "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gripsensor_cal);
        this.mText_RAW_Count = (TextView) findViewById(C0268R.C0269id.gripsensor_cspercent);
        this.mText_RAW_Count_crcount = (TextView) findViewById(C0268R.C0269id.gripsensor_crcount);
        this.mText_SPEC = (TextView) findViewById(C0268R.C0269id.gripsensor_proxpercent);
        this.mText_Caldata = (TextView) findViewById(C0268R.C0269id.gripsensor_caldata);
        this.mText_Result = (TextView) findViewById(C0268R.C0269id.result);
        this.mButton_Calibration = (Button) findViewById(C0268R.C0269id.gripsensor_cal_btn);
        this.mButton_Calibration.setOnClickListener(this);
        this.mButton_CalibrationErase = (Button) findViewById(C0268R.C0269id.gripsensor_calerase_btn);
        this.mButton_CalibrationErase.setOnClickListener(this);
        this.mButton_Skip = (Button) findViewById(C0268R.C0269id.gripsensor_skip_btn);
        this.mButton_Skip.setOnClickListener(this);
        this.mButton_onoff = (Button) findViewById(C0268R.C0269id.gripsensor_onoff_btn);
        this.mButton_onoff.setOnClickListener(this);
        this.mText_RAW_Count.setText(" : wait");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        LtUtil.log_d(CLASS_NAME, "onResume", "onResume");
        super.onResume();
        if (gripOnOff(49)) {
            this.isgrip_On = true;
            this.mButton_onoff.setText("Off");
            LtUtil.log_d(CLASS_NAME, "onResume", "grip on success");
        } else {
            this.isgrip_On = false;
            this.mButton_onoff.setText("On");
            LtUtil.log_d(CLASS_NAME, "onResume", "grip on fail");
        }
        UpdateGripdata();
        this.mTimerHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (!GripSensorCalibration.this.isTimerFinish) {
                    GripSensorCalibration.this.UpdateCSPercent();
                    GripSensorCalibration.this.mTimerHandler.sendEmptyMessageDelayed(0, 500);
                }
            }
        };
        this.isTimerFinish = false;
        this.mTimerHandler.sendEmptyMessage(0);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        LtUtil.log_d(CLASS_NAME, "onPause", "onPause");
        super.onPause();
        this.isgrip_On = false;
        this.isTimerFinish = true;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LtUtil.log_d(CLASS_NAME, "onDestroy", "onDestroy");
        super.onDestroy();
    }

    public void onClick(View view) {
        Intent intent;
        if (view.getId() == C0268R.C0269id.gripsensor_cal_btn) {
            LtUtil.log_d(CLASS_NAME, "onClick", "gripsensor_cal_btn");
            if (!calibration(49)) {
                LtUtil.log_d(CLASS_NAME, "onClick", "gripsensor_cal_fail");
            }
            UpdateGripdata();
        } else if (view.getId() == C0268R.C0269id.gripsensor_calerase_btn) {
            LtUtil.log_d(CLASS_NAME, "onClick", "gripsensor_calerase_btn");
            if (!calibration(48)) {
                LtUtil.log_d(CLASS_NAME, "onClick", "gripsensor_cal_erase_fail");
            }
            UpdateGripdata();
        } else if (view.getId() == C0268R.C0269id.gripsensor_skip_btn) {
            LtUtil.log_d(CLASS_NAME, "onClick", "gripsensor_skip_btn");
            if (Feature.getString(Feature.GRIPSENSOR_TYPE).equalsIgnoreCase("AP")) {
                intent = new Intent(this, GripSensorTest2.class);
            } else {
                intent = new Intent(this, GripSensorTest.class);
            }
            startActivity(intent);
            finish();
        } else if (view.getId() == C0268R.C0269id.gripsensor_onoff_btn) {
            LtUtil.log_d(CLASS_NAME, "onClick", "gripsensor_onoff_btn");
            if (this.isgrip_On) {
                if (gripOnOff(48)) {
                    this.isgrip_On = false;
                    this.mButton_onoff.setText("On");
                    LtUtil.log_d(CLASS_NAME, "onClick", "grip off");
                }
            } else if (!this.isgrip_On && gripOnOff(49)) {
                this.isgrip_On = true;
                this.mButton_onoff.setText("Off");
                LtUtil.log_d(CLASS_NAME, "onClick", "grip on");
            }
        }
    }

    private void UpdateGripdata() {
        this.mText_SPEC.setText(" : wait");
        this.mText_Caldata.setText(" : wait");
        this.mText_Result.setText("wait");
        this.mText_Result.setTextColor(-16777216);
        this.mProxpercent_data = readsysfs("/sys/class/sensors/grip_sensor/threshold");
        this.mCaldata_data = readsysfs("/sys/class/sensors/grip_sensor/calibration");
        this.mUIHandler.sendMessageDelayed(this.mUIHandler.obtainMessage(0), 0);
    }

    /* access modifiers changed from: private */
    public void UpdateCSPercent() {
        String[] RAWCount = null;
        String StrCS_Percent = null;
        String StrCR_Count = null;
        this.mCspercent_data = readsysfs("/sys/class/sensors/grip_sensor/raw_data");
        if (this.mCspercent_data != null) {
            RAWCount = this.mCspercent_data.split(",");
        }
        if (RAWCount != null && RAWCount.length > 7) {
            StringBuilder sb = new StringBuilder();
            sb.append("CS Percent(");
            sb.append(RAWCount[0]);
            sb.append(",");
            sb.append(RAWCount[1]);
            sb.append(",");
            sb.append(RAWCount[2]);
            sb.append(",");
            sb.append(RAWCount[3]);
            sb.append(")");
            StrCS_Percent = sb.toString();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("CR Percent (");
            sb2.append(RAWCount[4]);
            sb2.append(",");
            sb2.append(RAWCount[5]);
            sb2.append(",");
            sb2.append(RAWCount[6]);
            sb2.append(",");
            sb2.append(RAWCount[7]);
            sb2.append(")");
            StrCR_Count = sb2.toString();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("CS Percent(");
        sb3.append(StrCS_Percent);
        sb3.append(")\nCR Percent (");
        sb3.append(StrCR_Count);
        sb3.append(")");
        String temp = sb3.toString();
        if (this.mCspercent_data == "exception" || this.mCspercent_data == null) {
            LtUtil.log_d(CLASS_NAME, "UpdateCSPercent", "mCspercent_data null");
            return;
        }
        TextView textView = this.mText_RAW_Count;
        StringBuilder sb4 = new StringBuilder();
        sb4.append(" : ");
        sb4.append(StrCS_Percent);
        textView.setText(sb4.toString());
        TextView textView2 = this.mText_RAW_Count_crcount;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("   ");
        sb5.append(StrCR_Count);
        textView2.setText(sb5.toString());
        StringBuilder sb6 = new StringBuilder();
        sb6.append("mCspercent_data : ");
        sb6.append(this.mCspercent_data);
        LtUtil.log_d(CLASS_NAME, "UpdateCSPercent", sb6.toString());
    }

    private boolean calibration(byte data) {
        String str;
        String str2;
        StringBuilder sb;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("calibration data : ");
        sb2.append(data);
        LtUtil.log_d(CLASS_NAME, "calibration", sb2.toString());
        boolean result = false;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("/sys/class/sensors/grip_sensor/calibration");
            out.write(data);
            result = true;
            try {
                out.close();
            } catch (IOException e) {
                e = e;
                str = CLASS_NAME;
                str2 = "calibration";
                sb = new StringBuilder();
            }
        } catch (IOException e2) {
            String str3 = CLASS_NAME;
            String str4 = "calibration";
            StringBuilder sb3 = new StringBuilder();
            sb3.append("IOException : ");
            sb3.append(e2.getMessage());
            LtUtil.log_d(str3, str4, sb3.toString());
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e3) {
                    e = e3;
                    str = CLASS_NAME;
                    str2 = "calibration";
                    sb = new StringBuilder();
                }
            }
        } catch (Throwable th) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e4) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("IOException : ");
                    sb4.append(e4.getMessage());
                    LtUtil.log_d(CLASS_NAME, "calibration", sb4.toString());
                }
            }
            throw th;
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append("calibration result : ");
        sb5.append(result);
        LtUtil.log_d(CLASS_NAME, "calibration", sb5.toString());
        return result;
        sb.append("IOException : ");
        sb.append(e.getMessage());
        LtUtil.log_d(str, str2, sb.toString());
        StringBuilder sb52 = new StringBuilder();
        sb52.append("calibration result : ");
        sb52.append(result);
        LtUtil.log_d(CLASS_NAME, "calibration", sb52.toString());
        return result;
    }

    private boolean gripOnOff(byte data) {
        String str;
        String str2;
        StringBuilder sb;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("gripOnOff data : ");
        sb2.append(data);
        LtUtil.log_d(CLASS_NAME, "gripOnOff", sb2.toString());
        boolean result = false;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream("/sys/class/sensors/grip_sensor/onoff");
            out.write(data);
            result = true;
            try {
                out.close();
            } catch (IOException e) {
                e = e;
                str = CLASS_NAME;
                str2 = "gripOnOff";
                sb = new StringBuilder();
            }
        } catch (IOException e2) {
            String str3 = CLASS_NAME;
            String str4 = "gripOnOff";
            StringBuilder sb3 = new StringBuilder();
            sb3.append("IOException : ");
            sb3.append(e2.getMessage());
            LtUtil.log_d(str3, str4, sb3.toString());
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e3) {
                    e = e3;
                    str = CLASS_NAME;
                    str2 = "gripOnOff";
                    sb = new StringBuilder();
                }
            }
        } catch (Throwable th) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e4) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("IOException : ");
                    sb4.append(e4.getMessage());
                    LtUtil.log_d(CLASS_NAME, "gripOnOff", sb4.toString());
                }
            }
            throw th;
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append("gripOnOff result : ");
        sb5.append(result);
        LtUtil.log_d(CLASS_NAME, "gripOnOff", sb5.toString());
        return result;
        sb.append("IOException : ");
        sb.append(e.getMessage());
        LtUtil.log_d(str, str2, sb.toString());
        StringBuilder sb52 = new StringBuilder();
        sb52.append("gripOnOff result : ");
        sb52.append(result);
        LtUtil.log_d(CLASS_NAME, "gripOnOff", sb52.toString());
        return result;
    }

    private String readsysfs(String file_path) {
        StringBuilder sb;
        String str;
        String str2;
        String readData;
        String readData2 = null;
        String str3 = "";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file_path));
            readData2 = reader.readLine();
            if (readData2 != null) {
                readData = readData2.trim();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("sysfs Data : ");
            sb2.append(readData);
            LtUtil.log_d(CLASS_NAME, "readsysfs", sb2.toString());
            try {
                reader.close();
                return readData;
            } catch (IOException e) {
                e = e;
                str2 = CLASS_NAME;
                str = "readsysfs";
                sb = new StringBuilder();
            }
            sb.append("IOException : ");
            sb.append(e.getMessage());
            LtUtil.log_d(str2, str, sb.toString());
            String readData3 = "exception";
            this.ispass = false;
            return readData3;
        } catch (IOException e2) {
            String str4 = CLASS_NAME;
            String str5 = "readsysfs";
            StringBuilder sb3 = new StringBuilder();
            sb3.append("IOException : ");
            sb3.append(e2.getMessage());
            LtUtil.log_d(str4, str5, sb3.toString());
            String readData4 = "exception";
            this.ispass = false;
            if (readData2 == null) {
                return readData4;
            }
            try {
                readData2.close();
                return readData4;
            } catch (IOException e3) {
                e = e3;
                str2 = CLASS_NAME;
                str = "readsysfs";
                sb = new StringBuilder();
            }
        } finally {
            if (readData2 != null) {
                try {
                    readData2.close();
                } catch (IOException e4) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("IOException : ");
                    sb4.append(e4.getMessage());
                    LtUtil.log_d(CLASS_NAME, "readsysfs", sb4.toString());
                    String readData5 = "exception";
                    this.ispass = false;
                }
            }
        }
    }
}
