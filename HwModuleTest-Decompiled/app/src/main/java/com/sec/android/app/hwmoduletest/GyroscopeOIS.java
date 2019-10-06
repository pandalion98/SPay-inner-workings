package com.sec.android.app.hwmoduletest;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.android.app.hwmoduletest.support.LtUtil.Sleep;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Spec;
import egis.client.api.EgisFingerprint;

public class GyroscopeOIS extends BaseActivity {
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public String[] mIntialTestResults;
    private boolean mPass = false;
    /* access modifiers changed from: private */
    public String[] mSelfTestResults;
    private TextView txt_diff_x;
    private TextView txt_diff_y;
    private TextView txt_dps_x;
    private TextView txt_dps_y;
    private TextView txt_intial_x;
    private TextView txt_intial_y;
    private TextView txt_self_result;
    private TextView txtresult;

    public class EmptyListener implements OnDismissListener {
        public EmptyListener() {
        }

        public void onDismiss(DialogInterface dialog) {
            GyroscopeOIS.this.finish();
        }
    }

    public GyroscopeOIS() {
        super("GyroscopeOIS");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gyroscope_ois);
        init();
    }

    private void init() {
        this.txtresult = (TextView) findViewById(C0268R.C0269id.ois_result);
        this.txt_dps_x = (TextView) findViewById(C0268R.C0269id.dps_X);
        this.txt_dps_y = (TextView) findViewById(C0268R.C0269id.dps_Y);
        this.txt_intial_x = (TextView) findViewById(C0268R.C0269id.intial_X);
        this.txt_intial_y = (TextView) findViewById(C0268R.C0269id.intial_Y);
        this.txt_diff_x = (TextView) findViewById(C0268R.C0269id.diff_X);
        this.txt_diff_y = (TextView) findViewById(C0268R.C0269id.diff_Y);
        this.txt_self_result = (TextView) findViewById(C0268R.C0269id.self_check_result);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Kernel.write(Kernel.GYRO_OIS_POWER, "0");
        Sleep.sleep(100);
        Kernel.write(Kernel.GYRO_OIS_POWER, EgisFingerprint.MAJOR_VERSION);
        String intialData = Kernel.read(Kernel.GYRO_OIS_RAWDATA);
        this.txtresult.setVisibility(8);
        this.txtresult.setText("");
        try {
            String selfTestResult = Kernel.read(Kernel.GYRO_OIS_SELFTEST);
            if (selfTestResult == null || intialData == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("selfTestResult : ");
                sb.append(selfTestResult);
                sb.append(", intialData : ");
                sb.append(intialData);
                LtUtil.log_d(this.CLASS_NAME, "onResume", sb.toString());
                return;
            }
            this.mSelfTestResults = selfTestResult.split(",");
            this.mIntialTestResults = intialData.split(",");
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    GyroscopeOIS.this.showTestResults(GyroscopeOIS.this.mSelfTestResults, GyroscopeOIS.this.mIntialTestResults);
                }
            }, 1000);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mHandler.removeCallbacksAndMessages(null);
        Kernel.write(Kernel.GYRO_OIS_POWER, "0");
    }

    /* access modifiers changed from: private */
    public void showTestResults(String[] results, String[] intial_results) {
        String dps_x = results[1].trim();
        String dps_y = results[2].trim();
        String intial_x = intial_results[0].trim();
        String intial_y = intial_results[1].trim();
        this.txt_dps_x.setText(dps_x);
        this.txt_dps_y.setText(dps_y);
        this.txt_intial_x.setText(intial_x);
        this.txt_intial_y.setText(intial_y);
        Double diff_x_value = Double.valueOf(Double.parseDouble(intial_x) - Double.parseDouble(dps_x));
        Double diff_y_value = Double.valueOf(Double.parseDouble(intial_y) - Double.parseDouble(dps_y));
        this.txt_diff_x.setText(String.format("%.3f", new Object[]{diff_x_value}));
        this.txt_diff_y.setText(String.format("%.3f", new Object[]{diff_y_value}));
        if (results[0].equals("0")) {
            this.txt_self_result.setText("PASS");
            this.txt_self_result.setTextColor(-16776961);
            this.mPass = true;
        } else {
            this.txt_self_result.setText("FAIL");
            this.txt_self_result.setTextColor(-65536);
        }
        this.txt_self_result.setVisibility(0);
        if (!this.mPass || !CheckSpecin(intial_results)) {
            this.txtresult.setText("FAIL");
            this.txtresult.setTextColor(-65536);
            GyroTestFail();
        } else {
            this.txtresult.setText("PASS");
            this.txtresult.setTextColor(-16776961);
        }
        this.txtresult.setVisibility(0);
    }

    private boolean CheckSpecin(String[] data) {
        Double mSpec_Max = Double.valueOf(Spec.getDouble(Spec.OIS_INITIAL_SPEC_MAX));
        Double mSpec_Min = Double.valueOf(Spec.getDouble(Spec.OIS_INITIAL_SPEC_MIN));
        boolean isSpecin = true;
        if (data == null) {
            return false;
        }
        Double[] measure = new Double[data.length];
        for (int i = 0; i < data.length; i++) {
            measure[i] = Double.valueOf(Double.parseDouble(data[i]));
        }
        for (int i2 = 0; i2 < measure.length; i2++) {
            if (measure[i2].doubleValue() < mSpec_Min.doubleValue() || measure[i2].doubleValue() > mSpec_Max.doubleValue()) {
                isSpecin = false;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("result : ");
        sb.append(isSpecin);
        LtUtil.log_d(this.CLASS_NAME, "CheckSpecin", sb.toString());
        return isSpecin;
    }

    private void GyroTestFail() {
        Builder builder = new Builder(this);
        builder.setTitle("Gyro Test Fail");
        builder.setMessage("Do not move & Re –test");
        OnDismissListener pl = new EmptyListener();
        AlertDialog ad = builder.create();
        ad.setOnDismissListener(pl);
        ad.show();
    }
}
