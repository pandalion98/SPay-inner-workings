package com.sec.android.app.hwmoduletest;

import android.graphics.Point;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.SemRequestCallback;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;

public class FingerPrintTest_cpidTZ extends BaseActivity {
    private static final String CLASS_NAME = "FingerPrintTest_cpidTZ";
    private int IDENTIFY_FAILURE_SENSOR_CHANGED = 59;
    private final int MSG2 = 1;
    private final int RESULT = 2;
    private FingerprintManager mFingerprintManager;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            StringBuilder sb = new StringBuilder();
            sb.append("what : ");
            sb.append(msg.what);
            LtUtil.log_i(FingerPrintTest_cpidTZ.CLASS_NAME, "handleMessage", sb.toString());
            switch (msg.what) {
                case 1:
                    String data = msg.obj.toString();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("MSG2 - data : ");
                    sb2.append(data);
                    LtUtil.log_i(FingerPrintTest_cpidTZ.CLASS_NAME, "handleMessage", sb2.toString());
                    FingerPrintTest_cpidTZ.this.mTvMsg2.setText(data);
                    return;
                case 2:
                    String data2 = msg.obj.toString();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("RESULT - data : ");
                    sb3.append(data2);
                    LtUtil.log_i(FingerPrintTest_cpidTZ.CLASS_NAME, "handleMessage", sb3.toString());
                    FingerPrintTest_cpidTZ.this.mTvResult.setVisibility(0);
                    FingerPrintTest_cpidTZ.this.mTvResult.setText(data2);
                    return;
                default:
                    return;
            }
        }
    };
    private FingerPrintResultView_touch mResultView;
    private FingerPrintResultView_touch mSNRZoneView;
    private String mStrTestAction;
    private IBinder mToken;
    private TextView mTvMsg1;
    /* access modifiers changed from: private */
    public TextView mTvMsg2;
    /* access modifiers changed from: private */
    public TextView mTvResult;
    private int mnCurrentOperation = 0;
    private int mnScreenHeight = 0;
    private int mnScreenWidth = 0;
    private int mnSensorStateResult = 0;

    public FingerPrintTest_cpidTZ() {
        super(CLASS_NAME);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(C0268R.layout.fingerprint_test_touch);
        LtUtil.log_i(CLASS_NAME, "onCreate", "");
        initLayout();
        this.mStrTestAction = getIntent().getStringExtra("Action");
        if (Integer.parseInt(VERSION.RELEASE.replace("ver:", "").trim().split("")[1]) >= 6) {
            this.IDENTIFY_FAILURE_SENSOR_CHANGED = 64;
        }
    }

    public void onResume() {
        super.onResume();
        if (!registerClient()) {
            LtUtil.log_d(CLASS_NAME, "onResume", "sensorStatus has been failed ");
            updateMsg(1, "Fingerprint initializing Fail", 0);
        } else if ("SENSORINFO".equals(this.mStrTestAction)) {
            String strSensorInfo = this.mFingerprintManager.semGetSensorInfo();
            StringBuilder sb = new StringBuilder();
            sb.append("semGetSensorInfo : ");
            sb.append(strSensorInfo);
            LtUtil.log_d(CLASS_NAME, "onResume", sb.toString());
            updateMsg(1, "Sensor Infomation", 0);
            updateMsg(2, strSensorInfo, 0);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        LtUtil.log_d(CLASS_NAME, "onPause", "");
        super.onPause();
        unregisterClient();
    }

    private boolean registerClient() {
        this.mFingerprintManager = (FingerprintManager) getSystemService("fingerprint");
        if (this.mFingerprintManager == null) {
            return false;
        }
        return true;
    }

    public void unregisterClient() {
    }

    private void initLayout() {
        Display display = ((WindowManager) getApplicationContext().getSystemService("window")).getDefaultDisplay();
        Point outpoint = new Point();
        display.getRealSize(outpoint);
        this.mnScreenHeight = outpoint.x;
        this.mnScreenWidth = outpoint.y;
        this.mResultView = (FingerPrintResultView_touch) findViewById(C0268R.C0269id.fingerprint_resultview);
        this.mSNRZoneView = (FingerPrintResultView_touch) findViewById(C0268R.C0269id.fingerprint_snrzoneview);
        this.mResultView.setVisibility(8);
        this.mSNRZoneView.setVisibility(8);
        this.mTvMsg1 = (TextView) findViewById(C0268R.C0269id.fingerprint_message1);
        this.mTvMsg2 = (TextView) findViewById(C0268R.C0269id.fingerprint_message2);
        this.mTvResult = (TextView) findViewById(C0268R.C0269id.result_text);
        updateMsg(1, "Ready", 0);
    }

    public void updateMsg(int _nType, String _strMsg, int _nDelayMs) {
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(_nType, _strMsg), (long) _nDelayMs);
    }

    private int runSensorTest(int commandId) {
        return this.mFingerprintManager.semRunSensorTest(commandId, 0, new SemRequestCallback() {
            public void onRequested(int msgId) {
                LtUtil.log_d(FingerPrintTest_cpidTZ.CLASS_NAME, "runSensorTest", "Event is : UNKNOWN_EVT_MESSAGE");
            }
        });
    }
}
