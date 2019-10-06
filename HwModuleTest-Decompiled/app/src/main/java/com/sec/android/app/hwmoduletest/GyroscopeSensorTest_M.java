package com.sec.android.app.hwmoduletest;

import android.app.Activity;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.widget.TableLayout;
import android.widget.TextView;
import egis.client.api.EgisFingerprint;
import java.io.File;

public class GyroscopeSensorTest_M extends Activity {
    private static final String CLASS_NAME = "GyroscopeSensorTest";
    private boolean IS_Q1_MODEL = false;
    /* access modifiers changed from: private */
    public TableLayout mGyroSelfTestLayout;
    private TextView mGyroZeroRateText;
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public String[] mSelfTestResults;
    private SensorManager mSensorManager;
    private boolean mZeroRatePass = false;
    private TextView txt_X;
    private TextView txt_Y;
    private TextView txt_Z;
    private TextView txt_diff_x;
    private TextView txt_diff_y;
    private TextView txt_diff_z;
    private TextView txt_fifo_result;
    private TextView txt_fifo_subject;
    private TextView txt_prime_x;
    private TextView txt_prime_y;
    private TextView txt_prime_z;
    private TextView txt_sub1;
    private TextView txt_sub2;
    private TextView txt_sub3;
    private TextView txt_xyz_add;
    private TextView txt_xyz_x;
    private TextView txt_xyz_y;
    private TextView txt_xyz_z;
    private TextView txt_xyzprime_add;
    private TextView txtresult;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gyroscope_sensor_test_m);
        init();
    }

    private void init() {
        this.txtresult = (TextView) findViewById(C0268R.C0269id.result);
        this.txt_xyz_add = (TextView) findViewById(C0268R.C0269id.xyz_add_str);
        this.txt_xyzprime_add = (TextView) findViewById(C0268R.C0269id.xyzprime_add_str);
        this.txt_X = (TextView) findViewById(C0268R.C0269id.strX);
        this.txt_Y = (TextView) findViewById(C0268R.C0269id.strY);
        this.txt_Z = (TextView) findViewById(C0268R.C0269id.strZ);
        this.txt_sub1 = (TextView) findViewById(C0268R.C0269id.sub1_str);
        this.txt_sub2 = (TextView) findViewById(C0268R.C0269id.sub2_str);
        this.txt_sub3 = (TextView) findViewById(C0268R.C0269id.sub3_str);
        this.txt_xyz_x = (TextView) findViewById(C0268R.C0269id.xyz_x_str);
        this.txt_xyz_y = (TextView) findViewById(C0268R.C0269id.xyz_y_str);
        this.txt_xyz_z = (TextView) findViewById(C0268R.C0269id.xyz_z_str);
        this.txt_prime_x = (TextView) findViewById(C0268R.C0269id.prime_x_str);
        this.txt_prime_y = (TextView) findViewById(C0268R.C0269id.prime_y_str);
        this.txt_prime_z = (TextView) findViewById(C0268R.C0269id.prime_z_str);
        this.txt_diff_x = (TextView) findViewById(C0268R.C0269id.diff_x_str);
        this.txt_diff_y = (TextView) findViewById(C0268R.C0269id.diff_y_str);
        this.txt_diff_z = (TextView) findViewById(C0268R.C0269id.diff_z_str);
        this.txt_fifo_subject = (TextView) findViewById(C0268R.C0269id.magnetic3);
        this.txt_fifo_result = (TextView) findViewById(C0268R.C0269id.magnetic4);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mGyroZeroRateText = (TextView) findViewById(C0268R.C0269id.gyro_zero_rate);
        this.mGyroSelfTestLayout = (TableLayout) findViewById(C0268R.C0269id.menu);
        this.mGyroZeroRateText.setVisibility(0);
        this.mGyroSelfTestLayout.setVisibility(8);
        this.txtresult.setVisibility(8);
        this.txtresult.setText("");
        try {
            this.mSelfTestResults = FileUtils.readTextFile(new File("/sys/class/sensors/gyro_sensor/selftest"), 8192, null).split(",");
            if (this.mSelfTestResults[7] != null) {
                this.mZeroRatePass = this.mSelfTestResults[7].contains(EgisFingerprint.MAJOR_VERSION);
            }
            if (this.mZeroRatePass) {
                this.mGyroZeroRateText.setText("Zero Rate Level Check:  PASS");
                this.mGyroZeroRateText.setTextColor(-16776961);
                this.txtresult.setVisibility(0);
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        GyroscopeSensorTest_M.this.mGyroSelfTestLayout.setVisibility(0);
                        GyroscopeSensorTest_M.this.showTestResults(GyroscopeSensorTest_M.this.mSelfTestResults, GyroscopeSensorTest_M.this.mSelfTestResults[6]);
                    }
                }, 1000);
                return;
            }
            this.mGyroZeroRateText.setText("Zero Rate Level Check:  FAIL");
            this.mGyroZeroRateText.setTextColor(-65536);
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    GyroscopeSensorTest_M.this.finish();
                }
            }, 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void showTestResults(String[] results, String resultValue) {
        this.txt_xyz_x.setText(results[0]);
        this.txt_xyz_y.setText(results[1]);
        this.txt_xyz_z.setText(results[2]);
        this.txt_prime_x.setText(results[3]);
        this.txt_prime_y.setText(results[4]);
        this.txt_prime_z.setText(results[5]);
        this.txt_diff_x.setText(Integer.toString(Integer.parseInt(results[3]) - Integer.parseInt(results[0])));
        this.txt_diff_y.setText(Integer.toString(Integer.parseInt(results[4]) - Integer.parseInt(results[1])));
        this.txt_diff_z.setText(Integer.toString(Integer.parseInt(results[5]) - Integer.parseInt(results[2])));
        if (resultValue == null) {
            return;
        }
        if (this.mZeroRatePass && resultValue.contains(EgisFingerprint.MAJOR_VERSION)) {
            this.txtresult.setText(C0268R.string.PASS);
            this.txtresult.setTextColor(-16776961);
            setResult(-1);
        } else if (resultValue.contains("0")) {
            this.txtresult.setText(C0268R.string.FAIL);
            this.txtresult.setTextColor(-65536);
            setResult(0);
        } else {
            this.txtresult.setText(C0268R.string.RETRY);
            this.txtresult.setTextColor(-65536);
            setResult(0);
        }
    }
}
