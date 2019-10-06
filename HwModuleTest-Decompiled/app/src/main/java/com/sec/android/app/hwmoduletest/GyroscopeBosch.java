package com.sec.android.app.hwmoduletest;

import android.os.Bundle;
import android.os.Handler;
import android.widget.TableLayout;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.xmldata.support.Support.Kernel;
import egis.client.api.EgisFingerprint;

public class GyroscopeBosch extends BaseActivity {
    /* access modifiers changed from: private */
    public TableLayout mGyroSelfTestLayout;
    private final Handler mHandler = new Handler();
    private boolean mPass = false;
    /* access modifiers changed from: private */
    public String[] mSelfTestResults;
    /* access modifiers changed from: private */
    public String mSubType;
    private TextView txt_bist;
    private TextView txt_build_in_selftest;
    private TextView txt_zerox;
    private TextView txt_zeroy;
    private TextView txt_zeroz;
    private TextView txtresult;

    public GyroscopeBosch() {
        super("GyroscopeBosch");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gyroscope_bosch);
        init();
    }

    private void init() {
        this.txt_build_in_selftest = (TextView) findViewById(C0268R.C0269id.sub2_str);
        this.txtresult = (TextView) findViewById(C0268R.C0269id.result);
        this.txt_bist = (TextView) findViewById(C0268R.C0269id.bist_str);
        this.txt_zerox = (TextView) findViewById(C0268R.C0269id.x_value);
        this.txt_zeroy = (TextView) findViewById(C0268R.C0269id.y_value);
        this.txt_zeroz = (TextView) findViewById(C0268R.C0269id.z_value);
        this.mSubType = getIntent().getExtras().getString("subtype");
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mGyroSelfTestLayout = (TableLayout) findViewById(C0268R.C0269id.menu);
        this.mGyroSelfTestLayout.setVisibility(8);
        this.txtresult.setVisibility(8);
        this.txtresult.setText("");
        String selfTestResult = Kernel.read(Kernel.GYRO_SENSOR_SELFTEST);
        if (selfTestResult == null) {
            selfTestResult = "0,0,0,0,0";
        }
        this.mSelfTestResults = selfTestResult.split(",");
        this.txtresult.setVisibility(0);
        this.txt_build_in_selftest.setText("(Build in selftest)");
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                GyroscopeBosch.this.mGyroSelfTestLayout.setVisibility(0);
                if ("BMG160".equalsIgnoreCase(GyroscopeBosch.this.mSubType) || "BMI055".equalsIgnoreCase(GyroscopeBosch.this.mSubType) || "BMI160".equalsIgnoreCase(GyroscopeBosch.this.mSubType) || "BMI168".equalsIgnoreCase(GyroscopeBosch.this.mSubType)) {
                    GyroscopeBosch.this.showTestResults_BMG160(GyroscopeBosch.this.mSelfTestResults);
                }
            }
        }, 1000);
    }

    /* access modifiers changed from: private */
    public void showTestResults_BMG160(String[] results) {
        String result = results[0].trim();
        String bist = results[1].trim();
        String zerorate_x = results[2].trim();
        String zerorate_y = results[3].trim();
        String zerorate_z = results[4].trim();
        if (EgisFingerprint.MAJOR_VERSION.equals(bist)) {
            this.txt_bist.setText(C0268R.string.PASS);
            this.txt_bist.setTextColor(-16776961);
        } else {
            this.txt_bist.setText(C0268R.string.FAIL);
            this.txt_bist.setTextColor(-65536);
        }
        this.txt_zerox.setText(zerorate_x);
        this.txt_zeroy.setText(zerorate_y);
        this.txt_zeroz.setText(zerorate_z);
        this.mPass = EgisFingerprint.MAJOR_VERSION.equals(result);
        if (this.mPass) {
            this.txtresult.setText(C0268R.string.PASS);
            this.txtresult.setTextColor(-16776961);
            return;
        }
        this.txtresult.setText(C0268R.string.FAIL);
        this.txtresult.setTextColor(-65536);
    }
}
