package com.sec.android.app.hwmoduletest;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.sensors.CommonFingerprint_qbt2000;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import egis.client.api.EgisFingerprint;

public class FingerPrintSensorInfo_qbt2000 extends BaseActivity {
    private final String CLASS_NAME = "FingerPrintTest_qbt2000_SensorInfo";
    private CommonFingerprint_qbt2000 mCommonFingerprint_qbt2000;
    private final Handler mHandler = new Handler();
    private TextView mTvSensorInfo;

    public FingerPrintSensorInfo_qbt2000() {
        super("FingerPrintTest_qbt2000_SensorInfo");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.fingerprint_sensor_info_qbt2000);
        this.mTvSensorInfo = (TextView) findViewById(C0268R.C0269id.sensorInfo);
        boolean mTzEnabled = getIntent().getBooleanExtra("tz_enabled", false);
        StringBuilder sb = new StringBuilder();
        sb.append(" tz_enabled:");
        sb.append(mTzEnabled);
        LtUtil.log_d("FingerPrintTest_qbt2000_SensorInfo", "onCreate", sb.toString());
        this.mCommonFingerprint_qbt2000 = new CommonFingerprint_qbt2000(this, "FingerPrintTest_qbt2000_SensorInfo", mTzEnabled);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                FingerPrintSensorInfo_qbt2000.this.showSensorInfo();
            }
        }, 100);
    }

    /* access modifiers changed from: private */
    public void showSensorInfo() {
        String rawSensorInfo = this.mCommonFingerprint_qbt2000.getRawSensorInfo();
        StringBuilder sb = new StringBuilder();
        sb.append(" sensorInfo: ");
        sb.append(rawSensorInfo);
        LtUtil.log_d("FingerPrintTest_qbt2000_SensorInfo", "showSensorInfo", sb.toString());
        if (rawSensorInfo != null) {
            String[] dataArray = rawSensorInfo.split("\n");
            StringBuilder strBuilder = new StringBuilder();
            for (String data : dataArray) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" data:");
                sb2.append(data);
                LtUtil.log_d("FingerPrintTest_qbt2000_SensorInfo", "showSensorInfo", sb2.toString());
                if (data.contains("Sensor ID") || data.contains("Product ID") || data.contains("Chip SN") || data.contains("Module Test") || data.contains("Firmware Version") || data.contains("Sensor Temp") || data.contains("bin ")) {
                    strBuilder.append(data);
                    strBuilder.append("\n");
                }
            }
            strBuilder.append(getBgeCalDataInfo());
            this.mTvSensorInfo.setText(strBuilder.toString().trim());
            this.mTvSensorInfo.setVisibility(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mHandler.removeCallbacksAndMessages(null);
        finish();
    }

    private String getBgeCalDataInfo() {
        String data;
        String data2 = "BGE: ";
        boolean isExistBgeCalData = this.mCommonFingerprint_qbt2000.isExistBgeCalData();
        if (isExistBgeCalData) {
            StringBuilder sb = new StringBuilder();
            sb.append(data2);
            sb.append("0 (");
            sb.append(this.mCommonFingerprint_qbt2000.readSavedSensorID());
            sb.append(")");
            data = sb.toString();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(data2);
            sb2.append(EgisFingerprint.MAJOR_VERSION);
            data = sb2.toString();
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append(" isExistBgeCalData: ");
        sb3.append(isExistBgeCalData);
        sb3.append(" Data:");
        sb3.append(data);
        LtUtil.log_d("FingerPrintTest_qbt2000_SensorInfo", "getBgeCalDataInfo", sb3.toString());
        return data;
    }
}
