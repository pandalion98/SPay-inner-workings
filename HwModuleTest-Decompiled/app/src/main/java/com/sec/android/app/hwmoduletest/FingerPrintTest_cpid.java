package com.sec.android.app.hwmoduletest;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;
import com.sec.android.app.hwmoduletest.support.BaseActivity;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.synaptics.bpd.fingerprint.DeviceInfo;
import com.synaptics.bpd.fingerprint.Fingerprint;
import com.synaptics.bpd.fingerprint.FingerprintCore.EventListener;
import com.synaptics.bpd.fingerprint.FingerprintEvent;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class FingerPrintTest_cpid extends BaseActivity implements EventListener {
    private static final String CLASS_NAME = "FingerPrintTest_cpid";
    private final int MSG2 = 1;
    private final int OPERATION_NOTIFY_PLACED_AND_WAIT = 11;
    private final int OPERATION_REQUEST_DEVICEINFO = 8;
    private final int OPERATION_REQUEST_NORMALSCAN_DATA = 101;
    private final int OPERATION_REQUEST_SNR_DATA = 103;
    private final int RESULT = 2;
    private final int STATE_NORMALSCAN = 1014;
    private final int STATE_NORMALSCAN_FINISH = 1015;
    private final int STATE_SNR_NOTERM_SCAN = 1016;
    private final int STATE_SNR_SCAN_FINISH = 1019;
    private final int STATE_SNR_TERM_SCAN_PROCESSING = 1018;
    private final int STATE_SNR_TERM_SCAN_READY = 1017;
    private DeviceInfo mDeviceInfo;
    /* access modifiers changed from: private */
    public Fingerprint mFingerprint;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            StringBuilder sb = new StringBuilder();
            sb.append("what : ");
            sb.append(msg.what);
            LtUtil.log_i(FingerPrintTest_cpid.CLASS_NAME, "handleMessage", sb.toString());
            switch (msg.what) {
                case 1:
                    String data = msg.obj.toString();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("MSG2 - data : ");
                    sb2.append(data);
                    LtUtil.log_i(FingerPrintTest_cpid.CLASS_NAME, "handleMessage", sb2.toString());
                    FingerPrintTest_cpid.this.mTvMsg2.setText(data);
                    return;
                case 2:
                    String data2 = msg.obj.toString();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("RESULT - data : ");
                    sb3.append(data2);
                    LtUtil.log_i(FingerPrintTest_cpid.CLASS_NAME, "handleMessage", sb3.toString());
                    FingerPrintTest_cpid.this.mTvResult.setVisibility(0);
                    FingerPrintTest_cpid.this.mTvResult.setText(data2);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public Semaphore mLock;
    private FingerPrintResultView_touch mResultView;
    private FingerPrintResultView_touch mSNRZoneView;
    private String mStrSdkVersion;
    private String mStrTestAction;
    private TextView mTvMsg1;
    /* access modifiers changed from: private */
    public TextView mTvMsg2;
    /* access modifiers changed from: private */
    public TextView mTvResult;
    private int mnCurrentOperation = 0;
    private int mnScreenHeight = 0;
    private int mnScreenWidth = 0;
    /* access modifiers changed from: private */
    public int mnSensorStateResult = 0;

    public FingerPrintTest_cpid() {
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
        this.mLock = new Semaphore(1);
        this.mFingerprint = new Fingerprint(this);
        this.mFingerprint.registerListener(this);
        LtUtil.log_d(CLASS_NAME, "onCreate", "register");
        this.mStrSdkVersion = this.mFingerprint.getVersion();
        StringBuilder sb = new StringBuilder();
        sb.append("Version : ");
        sb.append(this.mStrSdkVersion);
        LtUtil.log_d(CLASS_NAME, "onCreate", sb.toString());
        this.mStrTestAction = getIntent().getStringExtra("Action");
        if (this.mFingerprint.getSensorStatus() == 0) {
            LtUtil.log_i(CLASS_NAME, "onCreate", "getSensorStatus : STATUS_OK");
            if ("NORMALDATA".equals(this.mStrTestAction)) {
                this.mnCurrentOperation = 1014;
                excuteOperation(101);
            } else if ("SNR".equals(this.mStrTestAction)) {
                this.mnCurrentOperation = 1016;
                excuteOperation(103);
            } else if ("SENSORINFO".equals(this.mStrTestAction)) {
                excuteOperation(8);
            }
        } else {
            LtUtil.log_i(CLASS_NAME, "onCreate", "getSensorStatus : STATUS_FAIL");
            updateMsg(1, "Fingerprint initializing Fail", 0);
        }
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

    /* access modifiers changed from: protected */
    public void onPause() {
        LtUtil.log_i(CLASS_NAME, "onPause", "");
        super.onPause();
        if (this.mFingerprint != null) {
            LtUtil.log_i(CLASS_NAME, "onPause", "mFingerprint.notify(");
            this.mnSensorStateResult = this.mFingerprint.notify(2, null);
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (FingerPrintTest_cpid.this.mFingerprint != null) {
                        LtUtil.log_i(FingerPrintTest_cpid.CLASS_NAME, "onPause()", "mFingerprint.cleanUp()");
                        try {
                            FingerPrintTest_cpid.this.mLock.acquire();
                            FingerPrintTest_cpid.this.mFingerprint.cancel();
                            FingerPrintTest_cpid.this.mFingerprint.cleanUp();
                            FingerPrintTest_cpid.this.mFingerprint = null;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        FingerPrintTest_cpid.this.mLock.release();
                    }
                }
            }, 60);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }

    public void onEvent(FingerprintEvent event) {
        if (event == null) {
            LtUtil.log_i(CLASS_NAME, "onEvent", "Event is null");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Event Id = ");
        sb.append(event.eventId);
        LtUtil.log_i(CLASS_NAME, "onEvent", sb.toString());
        if (event.eventId != 405) {
            LtUtil.log_d(CLASS_NAME, "onEvent", "event is : UNKNOWN_EVT_MESSAGE");
        } else {
            LtUtil.log_d(CLASS_NAME, "onEvent", "Event is : EVT_DEVICE_INFO");
            this.mDeviceInfo = (DeviceInfo) event.eventData;
            if (!"SENSORINFO".equals(this.mStrTestAction) || this.mDeviceInfo == null) {
                LtUtil.log_d(CLASS_NAME, "onEvent", "deviceInfo is null");
            } else {
                StringBuilder strResult = new StringBuilder();
                strResult.append("FwVersion : ");
                strResult.append(this.mDeviceInfo.fwVersion);
                strResult.append("\nExtVersion : ");
                strResult.append(this.mDeviceInfo.fwExtVersion);
                strResult.append("\nFlex ID : ");
                strResult.append(this.mDeviceInfo.flexId);
                strResult.append("\nProduct ID : ");
                strResult.append(this.mDeviceInfo.productId);
                strResult.append("\nUID : ");
                strResult.append(byteArrayToHex(this.mDeviceInfo.serialNumber));
                updateMsg(1, "Sensor Infomation", 0);
                updateMsg(2, strResult.toString(), 0);
            }
        }
    }

    private void excuteOperation(final int _nOperation) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    if (!FingerPrintTest_cpid.this.mLock.tryAcquire(500, TimeUnit.MILLISECONDS)) {
                        LtUtil.log_e(FingerPrintTest_cpid.CLASS_NAME, "excuteOperation", "lock tryAcquire fail!");
                        return;
                    }
                    if (_nOperation == 8) {
                        LtUtil.log_i(FingerPrintTest_cpid.CLASS_NAME, "excuteOperation", "OPERATION_REQUEST_DEVICEINFO");
                        FingerPrintTest_cpid.this.mnSensorStateResult = FingerPrintTest_cpid.this.mFingerprint.request(12, null);
                    }
                    FingerPrintTest_cpid.this.mLock.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void updateMsg(int _nType, String _strMsg, int _nDelayMs) {
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(_nType, _strMsg), (long) _nDelayMs);
    }

    private String byteArrayToHex(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }
        StringBuffer sb = new StringBuffer(ba.length * 2);
        for (int x = 0; x < ba.length; x++) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Integer.toHexString(0xff & ba[x] : ");
            sb2.append(Integer.toHexString(ba[x] & 255));
            LtUtil.log_i(CLASS_NAME, "byteArrayToHex", sb2.toString());
            StringBuilder sb3 = new StringBuilder();
            sb3.append("0");
            sb3.append(Integer.toHexString(ba[x] & 255));
            String hexNumber = sb3.toString();
            StringBuilder sb4 = new StringBuilder();
            sb4.append("hexNumber : ");
            sb4.append(hexNumber);
            LtUtil.log_i(CLASS_NAME, "byteArrayToHex", sb4.toString());
            sb.append(hexNumber.substring(hexNumber.length() - 2));
            sb.append(" ");
        }
        return sb.toString();
    }
}
