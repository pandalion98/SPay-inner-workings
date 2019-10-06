package com.sec.android.app.hwmoduletest;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Message;
import android.widget.TableLayout;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.xmldata.support.Support.Feature;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Spec;
import egis.client.api.EgisFingerprint;
import java.io.File;
import java.io.IOException;

public class GyroscopeStandard extends BaseActivity {
    private static final String CLASS_NAME = "GyroscopeSensorTest";
    private final int TEST_FAIL = 1000;
    private Handler UIUpdateHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1000:
                    GyroscopeStandard.this.mGyroZeroRateText.setText(String.format("Zero Rate Level Check:  FAIL(%s,%s,%s)", new Object[]{GyroscopeStandard.this.mSelfTestResults[0], GyroscopeStandard.this.mSelfTestResults[1], GyroscopeStandard.this.mSelfTestResults[2]}));
                    GyroscopeStandard.this.mGyroZeroRateText.setTextColor(-65536);
                    return;
                case 1001:
                    GyroscopeStandard.this.mGyroZeroRateText.setText("Zero Rate Level Check:  PASS");
                    GyroscopeStandard.this.mGyroZeroRateText.setTextColor(-16776961);
                    return;
                default:
                    return;
            }
        }
    };
    private final int ZERO_RATE_PASS = 1001;
    private boolean isRevised = false;
    /* access modifiers changed from: private */
    public TableLayout mGyroSelfTestLayout;
    /* access modifiers changed from: private */
    public TextView mGyroTitle;
    /* access modifiers changed from: private */
    public TextView mGyroZeroRateText;
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public boolean mIsSubGyro = false;
    /* access modifiers changed from: private */
    public String[] mSelfTestResults;
    private SensorManager mSensorManager;
    /* access modifiers changed from: private */
    public boolean mZeroRatePass = false;
    private TextView txt_bias_result;
    private TextView txt_bias_x;
    private TextView txt_bias_y;
    private TextView txt_bias_z;
    private TextView txt_diff_selftest_result;
    private TextView txt_diff_x;
    private TextView txt_diff_y;
    private TextView txt_diff_z;
    private TextView txt_fifo_zro_result;
    private TextView txt_fifo_zro_x;
    private TextView txt_fifo_zro_y;
    private TextView txt_fifo_zro_z;
    private TextView txt_gyro_test;
    private TextView txt_gyro_test_result;
    private TextView txt_result;
    private TextView txt_zro_selftest_result;
    private TextView txt_zro_x;
    private TextView txt_zro_y;
    private TextView txt_zro_z;

    public GyroscopeStandard() {
        super("GyroscopeStandard");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gyroscope_standard);
        this.mIsSubGyro = getIntent().getBooleanExtra("sub_gyro", false);
        StringBuilder sb = new StringBuilder();
        sb.append(" mIsSubGyro : ");
        sb.append(this.mIsSubGyro);
        LtUtil.log_d(CLASS_NAME, "onCreate", sb.toString());
        init();
    }

    private void init() {
        this.isRevised = EgisFingerprint.MAJOR_VERSION.equals(Kernel.read(Kernel.GYRO_SENSOR_REVISED));
        this.mGyroTitle = (TextView) findViewById(C0268R.C0269id.gyro_title);
        this.mGyroZeroRateText = (TextView) findViewById(C0268R.C0269id.gyro_zero_rate);
        this.mGyroSelfTestLayout = (TableLayout) findViewById(C0268R.C0269id.menu);
        this.txt_result = (TextView) findViewById(C0268R.C0269id.strResult);
        this.txt_fifo_zro_x = (TextView) findViewById(C0268R.C0269id.fifo_zro_x);
        this.txt_fifo_zro_y = (TextView) findViewById(C0268R.C0269id.fifo_zro_y);
        this.txt_fifo_zro_z = (TextView) findViewById(C0268R.C0269id.fifo_zro_z);
        this.txt_fifo_zro_result = (TextView) findViewById(C0268R.C0269id.fifo_zro_result);
        this.txt_zro_x = (TextView) findViewById(C0268R.C0269id.zro_x_str);
        this.txt_zro_y = (TextView) findViewById(C0268R.C0269id.zro_y_str);
        this.txt_zro_z = (TextView) findViewById(C0268R.C0269id.zro_z_str);
        this.txt_zro_selftest_result = (TextView) findViewById(C0268R.C0269id.zro_selftest_result);
        this.txt_bias_x = (TextView) findViewById(C0268R.C0269id.bias_x_str);
        this.txt_bias_y = (TextView) findViewById(C0268R.C0269id.bias_y_str);
        this.txt_bias_z = (TextView) findViewById(C0268R.C0269id.bias_z_str);
        this.txt_bias_result = (TextView) findViewById(C0268R.C0269id.bias_result);
        this.txt_diff_x = (TextView) findViewById(C0268R.C0269id.diff_x_str);
        this.txt_diff_y = (TextView) findViewById(C0268R.C0269id.diff_y_str);
        this.txt_diff_z = (TextView) findViewById(C0268R.C0269id.diff_z_str);
        this.txt_diff_selftest_result = (TextView) findViewById(C0268R.C0269id.diff_selftest_result);
        this.txt_gyro_test = (TextView) findViewById(C0268R.C0269id.gyro_test);
        this.txt_gyro_test_result = (TextView) findViewById(C0268R.C0269id.gyro_test_result);
        if (!this.isRevised) {
            this.txt_result.setVisibility(8);
            this.txt_fifo_zro_result.setVisibility(8);
            this.txt_zro_selftest_result.setVisibility(8);
            this.txt_bias_result.setVisibility(8);
            this.txt_diff_selftest_result.setVisibility(8);
        }
        if (this.mIsSubGyro) {
            this.txt_result.setVisibility(8);
            this.txt_fifo_zro_result.setVisibility(8);
            this.txt_zro_selftest_result.setVisibility(8);
            this.txt_bias_result.setVisibility(8);
            this.txt_diff_selftest_result.setVisibility(8);
            this.txt_gyro_test.setVisibility(8);
            this.txt_gyro_test_result.setVisibility(8);
        }
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mGyroSelfTestLayout.setVisibility(0);
        new Thread() {
            public void run() {
                String selfTestResult;
                String str = "";
                try {
                    if (GyroscopeStandard.this.mIsSubGyro) {
                        GyroscopeStandard.this.mGyroTitle.setText("SUB GYROSCOPE SENSOR");
                        selfTestResult = FileUtils.readTextFile(new File("/sys/class/sensors/sub_gyro_sensor/selftest"), 8192, null);
                    } else {
                        selfTestResult = FileUtils.readTextFile(new File("/sys/class/sensors/gyro_sensor/selftest"), 8192, null);
                    }
                    GyroscopeStandard.this.mSelfTestResults = selfTestResult.split(",");
                    if (GyroscopeStandard.this.mSelfTestResults.length > 4) {
                        GyroscopeStandard.this.mZeroRatePass = true;
                    } else {
                        GyroscopeStandard.this.mZeroRatePass = false;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(" ZeroRatePass : ");
                    sb.append(GyroscopeStandard.this.mZeroRatePass);
                    sb.append(", length : ");
                    sb.append(GyroscopeStandard.this.mSelfTestResults.length);
                    LtUtil.log_d(GyroscopeStandard.CLASS_NAME, "SelfTest", sb.toString());
                    GyroscopeStandard.this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            GyroscopeStandard.this.mGyroSelfTestLayout.setVisibility(0);
                            GyroscopeStandard.this.showTestResults(GyroscopeStandard.this.mSelfTestResults);
                        }
                    }, 1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /* access modifiers changed from: private */
    public void showTestResults(String[] data) {
        boolean mResult = true;
        boolean SelfDiffResult = false;
        boolean SelfZroResult = false;
        this.txt_gyro_test.setVisibility(0);
        this.txt_gyro_test_result.setVisibility(0);
        this.txt_fifo_zro_x.setText(data[0]);
        this.txt_fifo_zro_y.setText(data[1]);
        this.txt_fifo_zro_z.setText(data[2]);
        if (this.mZeroRatePass) {
            this.txt_fifo_zro_result.setText(C0268R.string.PASS);
            this.txt_fifo_zro_result.setTextColor(-16776961);
            this.txt_zro_x.setText(data[3]);
            this.txt_zro_y.setText(data[4]);
            this.txt_zro_z.setText(data[5]);
            this.txt_bias_x.setText(data[6]);
            this.txt_bias_y.setText(data[7]);
            this.txt_bias_z.setText(data[8]);
            int i = 9;
            this.txt_diff_x.setText(data[9]);
            this.txt_diff_y.setText(data[10]);
            this.txt_diff_z.setText(data[11]);
            SelfDiffResult = data[12].contains(EgisFingerprint.MAJOR_VERSION);
            SelfZroResult = data[13].contains(EgisFingerprint.MAJOR_VERSION);
            if (!this.isRevised) {
                int mGyroSpec_Min = Spec.getInt(Spec.GYROSCOPE_SELFTEST_MIN);
                int mGyroSpec_Max = Spec.getInt(Spec.GYROSCOPE_SELFTEST_MAX);
                if (Feature.getBoolean(Feature.USE_DUAL_SENSOR_ACC)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("GYROSCOPE_SELFTEST_");
                    sb.append(Kernel.getGyroSensorVendor());
                    sb.append("_MIN");
                    mGyroSpec_Min = Spec.getInt(sb.toString());
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("GYROSCOPE_SELFTEST_");
                    sb2.append(Kernel.getGyroSensorVendor());
                    sb2.append("_MAX");
                    mGyroSpec_Max = Spec.getInt(sb2.toString());
                }
                while (true) {
                    int i2 = i;
                    if (i2 >= 12) {
                        break;
                    }
                    if (mGyroSpec_Min > Integer.parseInt(data[i2]) || Integer.parseInt(data[i2]) > mGyroSpec_Max) {
                        mResult = false;
                    }
                    i = i2 + 1;
                }
            } else {
                if (SelfZroResult) {
                    this.txt_zro_selftest_result.setText(C0268R.string.PASS);
                    this.txt_zro_selftest_result.setTextColor(-16776961);
                } else {
                    this.txt_zro_selftest_result.setText(C0268R.string.FAIL);
                    this.txt_zro_selftest_result.setTextColor(-65536);
                }
                if (SelfDiffResult) {
                    this.txt_diff_selftest_result.setText(C0268R.string.PASS);
                    this.txt_diff_selftest_result.setTextColor(-16776961);
                } else {
                    this.txt_diff_selftest_result.setText(C0268R.string.FAIL);
                    this.txt_diff_selftest_result.setTextColor(-65536);
                }
            }
        } else {
            this.txt_fifo_zro_result.setText(C0268R.string.FAIL);
            this.txt_fifo_zro_result.setTextColor(-65536);
        }
        if (this.isRevised) {
            if (!this.mZeroRatePass || !SelfZroResult || !SelfDiffResult) {
                displayResult(false);
            } else {
                displayResult(true);
            }
        } else if (!mResult || !this.mZeroRatePass) {
            displayResult(false);
        } else {
            displayResult(true);
        }
        if (this.mIsSubGyro) {
            this.txt_gyro_test.setVisibility(8);
            this.txt_gyro_test_result.setVisibility(8);
        }
    }

    private void displayResult(boolean status) {
        this.txt_gyro_test_result.setVisibility(0);
        if (status) {
            this.txt_gyro_test_result.setText(C0268R.string.PASS);
            this.txt_gyro_test_result.setTextColor(-16776961);
            return;
        }
        this.txt_gyro_test_result.setText(C0268R.string.FAIL);
        this.txt_gyro_test_result.setTextColor(-65536);
    }
}
