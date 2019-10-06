package com.sec.android.app.hwmoduletest;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.sensors.CommonFingerprint_qbt2000;
import com.sec.android.app.hwmoduletest.sensors.CommonFingerprint_qbt2000.TestType;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.android.app.hwmoduletest.support.LtUtil.Sleep;
import com.sec.android.app.hwmoduletest.support.Status;

public class FingerprintIntCheckTest_qbt2000 extends BaseActivity {
    private final String CLASS_NAME = "FingerprintIntCheckTest_qbt2000";
    /* access modifiers changed from: private */
    public Button mBtnExit;
    /* access modifiers changed from: private */
    public CommonFingerprint_qbt2000 mCommonFingerprint_qbt2000;
    private final Handler mHandler = new Handler();
    private Status mInt2Result = Status.NOTEST;
    private TextView mTvInt2Status;
    private Boolean mTzEnabled = Boolean.valueOf(false);

    /* renamed from: com.sec.android.app.hwmoduletest.FingerprintIntCheckTest_qbt2000$2 */
    static /* synthetic */ class C02032 {

        /* renamed from: $SwitchMap$com$sec$android$app$hwmoduletest$sensors$CommonFingerprint_qbt2000$TestType */
        static final /* synthetic */ int[] f8x535fbc69 = new int[TestType.values().length];

        static {
            $SwitchMap$com$sec$android$app$hwmoduletest$support$Status = new int[Status.values().length];
            try {
                $SwitchMap$com$sec$android$app$hwmoduletest$support$Status[Status.PASS.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$com$sec$android$app$hwmoduletest$support$Status[Status.FAIL.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$com$sec$android$app$hwmoduletest$support$Status[Status.RUNNING.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$com$sec$android$app$hwmoduletest$support$Status[Status.READY.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                f8x535fbc69[TestType.INT2.ordinal()] = 1;
            } catch (NoSuchFieldError e5) {
            }
        }
    }

    public FingerprintIntCheckTest_qbt2000() {
        super("FingerprintIntCheckTest_qbt2000");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0268R.layout.fingerprint_int_check_qbt2000);
        LtUtil.setRemoveSystemUI(getWindow(), true);
        this.mTvInt2Status = (TextView) findViewById(C0268R.C0269id.int2Status);
        this.mBtnExit = (Button) findViewById(C0268R.C0269id.exitButton);
        this.mTzEnabled = Boolean.valueOf(getIntent().getBooleanExtra("tz_enabled", false));
        StringBuilder sb = new StringBuilder();
        sb.append(" tz_enabled:");
        sb.append(this.mTzEnabled);
        LtUtil.log_d("FingerprintIntCheckTest_qbt2000", "onCreate", sb.toString());
        this.mCommonFingerprint_qbt2000 = new CommonFingerprint_qbt2000(this, "FingerprintIntCheckTest_qbt2000", this.mTzEnabled.booleanValue());
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        updateStatus(TestType.INT2, Status.RUNNING);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                FingerprintIntCheckTest_qbt2000.this.mCommonFingerprint_qbt2000.setWuhbMode();
                Sleep.sleep(50);
                FingerprintIntCheckTest_qbt2000.this.mCommonFingerprint_qbt2000.controlTspInt(true);
                Sleep.sleep(100);
                FingerprintIntCheckTest_qbt2000.this.mCommonFingerprint_qbt2000.controlTspInt(false);
                Sleep.sleep(50);
                if (FingerprintIntCheckTest_qbt2000.this.mCommonFingerprint_qbt2000.getWuhbResult()) {
                    FingerprintIntCheckTest_qbt2000.this.updateStatus(TestType.INT2, Status.PASS);
                    FingerprintIntCheckTest_qbt2000.this.setScanResult(TestType.INT2, Status.PASS);
                } else {
                    FingerprintIntCheckTest_qbt2000.this.setScanResult(TestType.INT2, Status.FAIL_INT2);
                }
                FingerprintIntCheckTest_qbt2000.this.mBtnExit.setVisibility(0);
            }
        }, 100);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        this.mHandler.removeCallbacksAndMessages(null);
        finish();
    }

    /* access modifiers changed from: private */
    public void setScanResult(TestType testType, Status status) {
        StringBuilder sb = new StringBuilder();
        sb.append(" testType:");
        sb.append(testType.name());
        sb.append(" result:");
        sb.append(status.name());
        LtUtil.log_d("FingerprintIntCheckTest_qbt2000", "setScanResult", sb.toString());
        if (C02032.f8x535fbc69[testType.ordinal()] == 1) {
            this.mInt2Result = status;
        }
    }

    /* access modifiers changed from: private */
    public void updateStatus(TestType testType, Status status) {
        StringBuilder sb = new StringBuilder();
        sb.append(" testType:");
        sb.append(testType.name());
        sb.append(" status:");
        sb.append(status.name());
        LtUtil.log_d("FingerprintIntCheckTest_qbt2000", "updateStatus", sb.toString());
        TextView tvStatus = this.mTvInt2Status;
        switch (status) {
            case PASS:
                tvStatus.setTextColor(-16776961);
                tvStatus.setText(C0268R.string.PASS);
                return;
            case FAIL:
                tvStatus.setTextColor(-65536);
                tvStatus.setText(C0268R.string.FAIL);
                return;
            case RUNNING:
                tvStatus.setTextColor(getResources().getColor(C0268R.color.forest_green));
                tvStatus.setText(C0268R.string.RUNNING);
                return;
            case READY:
                tvStatus.setTextColor(-16777216);
                tvStatus.setText(C0268R.string.READY);
                return;
            default:
                return;
        }
    }

    public void onExitButtonClicked(View v) {
        LtUtil.log_d("FingerprintIntCheckTest_qbt2000", "onExitButtonClicked", "onExitButtonClicked");
        finish();
    }
}
