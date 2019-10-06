package com.sec.android.app.hwmoduletest;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.FileUtils;
import android.os.Handler;
import android.widget.TableLayout;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import java.io.File;

public class GyroscopeIcSTMicroTablet extends BaseActivity {
    private static final String CLASS_NAME = "GyroscopeIcSTMicroTablet";
    private static final int RET_IDX_CALIBRATION = 15;
    private static final int RET_IDX_FIFO_NUM = 9;
    private static final int RET_IDX_FIFO_X = 10;
    private static final int RET_IDX_FIFO_Y = 11;
    private static final int RET_IDX_FIFO_Z = 12;
    private static final int RET_IDX_MAX = 15;
    private static final int RET_IDX_NOMAL_X = 0;
    private static final int RET_IDX_NOMAL_Y = 1;
    private static final int RET_IDX_NOMAL_Z = 2;
    private static final int RET_IDX_SELFTEST = 13;
    private static final int RET_IDX_SELFTEST_X = 3;
    private static final int RET_IDX_SELFTEST_Y = 4;
    private static final int RET_IDX_SELFTEST_Z = 5;
    private static final int RET_IDX_ZERORATE = 14;
    private static final int RET_IDX_ZERORATE_X = 6;
    private static final int RET_IDX_ZERORATE_Y = 7;
    private static final int RET_IDX_ZERORATE_Z = 8;
    private static final String TEST_FAIL = "0";
    private static final String TEST_SUCCESS = "1";
    private boolean mCalibrationPass = false;
    /* access modifiers changed from: private */
    public TableLayout mGyroSelfTestLayout;
    private TextView mGyroZeroRateText;
    private Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public String[] mSelfTestResults;
    private SensorManager mSensorManager;
    private boolean mZeroRatePass = false;
    private TextView txt_calibration;
    private TextView txt_diff_x;
    private TextView txt_diff_y;
    private TextView txt_diff_z;
    private TextView txt_self_x;
    private TextView txt_self_y;
    private TextView txt_self_z;
    private TextView txt_x;
    private TextView txt_xyz_x;
    private TextView txt_xyz_y;
    private TextView txt_xyz_z;
    private TextView txt_y;
    private TextView txt_z;
    private TextView txt_zerorate_x;
    private TextView txt_zerorate_y;
    private TextView txt_zerorate_z;
    private TextView txtresult;

    public GyroscopeIcSTMicroTablet() {
        super(CLASS_NAME);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.gyroscope_icstmicro_tablet);
        init();
    }

    private void init() {
        this.txtresult = (TextView) findViewById(C0268R.C0269id.result);
        this.txt_x = (TextView) findViewById(C0268R.C0269id.strX);
        this.txt_y = (TextView) findViewById(C0268R.C0269id.strY);
        this.txt_z = (TextView) findViewById(C0268R.C0269id.strZ);
        this.txt_xyz_x = (TextView) findViewById(C0268R.C0269id.xyz_x_str);
        this.txt_xyz_y = (TextView) findViewById(C0268R.C0269id.xyz_y_str);
        this.txt_xyz_z = (TextView) findViewById(C0268R.C0269id.xyz_z_str);
        this.txt_self_x = (TextView) findViewById(C0268R.C0269id.prime_x_str);
        this.txt_self_y = (TextView) findViewById(C0268R.C0269id.prime_y_str);
        this.txt_self_z = (TextView) findViewById(C0268R.C0269id.prime_z_str);
        this.txt_diff_x = (TextView) findViewById(C0268R.C0269id.diff_x_str);
        this.txt_diff_y = (TextView) findViewById(C0268R.C0269id.diff_y_str);
        this.txt_diff_z = (TextView) findViewById(C0268R.C0269id.diff_z_str);
        this.txt_zerorate_x = (TextView) findViewById(C0268R.C0269id.zero_x_str);
        this.txt_zerorate_y = (TextView) findViewById(C0268R.C0269id.zero_y_str);
        this.txt_zerorate_z = (TextView) findViewById(C0268R.C0269id.zero_z_str);
        this.txt_calibration = (TextView) findViewById(C0268R.C0269id.calibration_str);
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
            String selfTestResult = FileUtils.readTextFile(new File("/sys/class/sensors/gyro_sensor/selftest"), 8192, null);
            this.mSelfTestResults = selfTestResult.split(",");
            if (this.mSelfTestResults[15] != null) {
                boolean z = true;
                this.mZeroRatePass = this.mSelfTestResults[14].contains("1");
                if (!this.mSelfTestResults[15].contains("1")) {
                    z = false;
                }
                this.mCalibrationPass = z;
                if (this.mZeroRatePass) {
                    TextView textView = this.mGyroZeroRateText;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Zero Rate Level Check:  PASS \n(Index of FIFO : ");
                    sb.append(this.mSelfTestResults[9]);
                    sb.append(" / Data : ");
                    sb.append(this.mSelfTestResults[10]);
                    sb.append(", ");
                    sb.append(this.mSelfTestResults[11]);
                    sb.append(", ");
                    sb.append(this.mSelfTestResults[12]);
                    sb.append(")");
                    textView.setText(sb.toString());
                    this.mGyroZeroRateText.setTextColor(-16776961);
                    this.txtresult.setVisibility(0);
                    this.mHandler.postDelayed(new Runnable() {
                        public void run() {
                            GyroscopeIcSTMicroTablet.this.mGyroSelfTestLayout.setVisibility(0);
                            GyroscopeIcSTMicroTablet.this.showTestResults(GyroscopeIcSTMicroTablet.this.mSelfTestResults, GyroscopeIcSTMicroTablet.this.mSelfTestResults[13]);
                        }
                    }, 1000);
                } else {
                    TextView textView2 = this.mGyroZeroRateText;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Zero Rate Level Check:  FAIL \n(Index of FIFO : ");
                    sb2.append(this.mSelfTestResults[9]);
                    sb2.append(" / Data : ");
                    sb2.append(this.mSelfTestResults[10]);
                    sb2.append(", ");
                    sb2.append(this.mSelfTestResults[11]);
                    sb2.append(", ");
                    sb2.append(this.mSelfTestResults[12]);
                    sb2.append(")");
                    textView2.setText(sb2.toString());
                    this.mGyroZeroRateText.setTextColor(-65536);
                }
                return;
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append(" -- parse error: Gyro self-test -- ret selftest : ");
            sb3.append(selfTestResult);
            LtUtil.log_d(CLASS_NAME, "onResume", sb3.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void showTestResults(String[] results, String resultValue) {
        this.txt_xyz_x.setText(results[0]);
        this.txt_xyz_y.setText(results[1]);
        this.txt_xyz_z.setText(results[2]);
        this.txt_self_x.setText(results[3]);
        this.txt_self_y.setText(results[4]);
        this.txt_self_z.setText(results[5]);
        this.txt_zerorate_x.setText(results[6]);
        this.txt_zerorate_y.setText(results[7]);
        this.txt_zerorate_z.setText(results[8]);
        this.txt_diff_x.setText(Integer.toString(Integer.parseInt(results[3]) - Integer.parseInt(results[0])));
        this.txt_diff_y.setText(Integer.toString(Integer.parseInt(results[4]) - Integer.parseInt(results[1])));
        this.txt_diff_z.setText(Integer.toString(Integer.parseInt(results[5]) - Integer.parseInt(results[2])));
        if (this.mCalibrationPass) {
            this.txt_calibration.setText(C0268R.string.PASS);
            this.txt_calibration.setTextColor(-16776961);
        } else {
            this.txt_calibration.setText(C0268R.string.FAIL);
            this.txt_calibration.setTextColor(-65536);
        }
        if (resultValue == null) {
            return;
        }
        if (this.mZeroRatePass && resultValue.contains("1")) {
            this.txtresult.setText(C0268R.string.PASS);
            this.txtresult.setTextColor(-16776961);
        } else if (resultValue.contains(TEST_FAIL)) {
            this.txtresult.setText(C0268R.string.FAIL);
            this.txtresult.setTextColor(-65536);
        } else {
            this.txtresult.setText(C0268R.string.RETRY);
            this.txtresult.setTextColor(-65536);
        }
    }
}
