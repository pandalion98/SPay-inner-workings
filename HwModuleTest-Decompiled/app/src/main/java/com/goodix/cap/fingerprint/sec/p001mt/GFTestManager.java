package com.goodix.cap.fingerprint.sec.p001mt;

import android.content.Context;
import com.goodix.cap.fingerprint.sec.GoodixFpCaptureInfo;
import com.goodix.cap.fingerprint.sec.GoodixFpEnrollStatus;
import com.goodix.cap.fingerprint.sec.GoodixFpIdentifyResult;

/* renamed from: com.goodix.cap.fingerprint.sec.mt.GFTestManager */
public class GFTestManager implements TestStatusListener {
    private static final String TAG = "GFTestManager";
    public static final int TEST_FINGER_INDEX = 1;
    public static final String TEST_USER_ID = "Test";
    private Context mContext;
    private TestResultNotify mResultNotify;
    private TestTask mTestTask;

    public GFTestManager(Context context) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("GFTestManager init ,thread Id = ");
        sb.append(Thread.currentThread().getId());
        Common.LOG_D(str, sb.toString());
        this.mContext = context;
        this.mTestTask = new TestTask(context);
        this.mTestTask.setTestStatusListener(this);
    }

    public String getVersion() {
        Common.LOG_D(TAG, "getVersion");
        String version = this.mTestTask.getSoftwareVersion();
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("getVersion : ");
        sb.append(version);
        Common.LOG_D(str, sb.toString());
        return version;
    }

    public void cancelTest() {
        Common.LOG_D(TAG, "cancelTest");
        this.mTestTask.removeTestStatusListener();
    }

    public void cancelCMD() {
        Common.LOG_D(TAG, "cancelTest");
        this.mTestTask.cancel();
    }

    public void resumeTest() {
        Common.LOG_D(TAG, "resumeTest");
        this.mTestTask = new TestTask(this.mContext);
        this.mTestTask.setTestStatusListener(this);
    }

    public void registerResultNotify(TestResultNotify notify) {
        Common.LOG_D(TAG, "registerResultNotify");
        this.mResultNotify = notify;
    }

    public void startUnTouchTest() {
        Common.LOG_D(TAG, "startUnTouchTest");
        this.mTestTask.startUnTouchTest();
    }

    public void startTouchIntRstTest() {
        Common.LOG_D(TAG, "startTouchIntRstTest");
        this.mTestTask.startTouchIntRstTest();
    }

    public void startTouchTest() {
        Common.LOG_D(TAG, "startTouchTest");
        this.mTestTask.startTouchTest();
    }

    public void startGetSensorInfo() {
        Common.LOG_D(TAG, "startGetSensorInfo");
        this.mTestTask.startGetSensorInfoTest();
    }

    public void enroll() {
        Common.LOG_D(TAG, "enroll");
        this.mTestTask.startEnrollTest("Test", 1);
    }

    public void identify() {
        Common.LOG_D(TAG, "identify");
        this.mTestTask.startAuthenticateTest("Test");
    }

    public void startOTPCheckTest() {
        Common.LOG_D(TAG, "startOTPCheckTest");
        this.mTestTask.startOtpCheckTest();
    }

    public void onUnTouchTestStatus(int cmdId, Object result) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onUnTouchTestStatus mResultNotify = ");
        sb.append(this.mResultNotify);
        sb.append(" cmdId = ");
        sb.append(cmdId);
        Common.LOG_D(str, sb.toString());
        if (this.mResultNotify != null) {
            this.mResultNotify.onUnTouchResultNotify(cmdId, result);
        }
    }

    public void onTouchTestStatus(int cmdId, Object result) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onTouchTestStatus mResultNotify = ");
        sb.append(this.mResultNotify);
        sb.append(" cmdId = ");
        sb.append(cmdId);
        Common.LOG_D(str, sb.toString());
        if (this.mResultNotify != null) {
            this.mResultNotify.onTouchResultNotify(cmdId, result);
        }
    }

    public void onCaptureStatus(int cmdId, Object result) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onCaptureStatus mResultNotify = ");
        sb.append(this.mResultNotify);
        sb.append(" cmdId = ");
        sb.append(cmdId);
        Common.LOG_D(str, sb.toString());
        if (this.mResultNotify != null) {
            this.mResultNotify.onCaptureNotify(cmdId, (GoodixFpCaptureInfo) result);
        }
    }

    public void onCaptureFingerLeaved(int cmdId, Object result) {
        if (this.mResultNotify != null) {
            this.mResultNotify.onCaptureFingerLeaved(cmdId, result);
        }
    }

    public void onEnrollTestStatus(int cmdId, Object result) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onEnrollTestStatus mResultNotify = ");
        sb.append(this.mResultNotify);
        sb.append(" cmdId = ");
        sb.append(cmdId);
        Common.LOG_D(str, sb.toString());
        if (this.mResultNotify != null) {
            this.mResultNotify.onEnrollStatus(cmdId, (GoodixFpEnrollStatus) result);
        }
    }

    public void onAuthenticateStatus(int cmdId, Object result) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onAuthenticateStatus mResultNotify = ");
        sb.append(this.mResultNotify);
        sb.append(" cmdId = ");
        sb.append(cmdId);
        Common.LOG_D(str, sb.toString());
        if (this.mResultNotify != null) {
            this.mResultNotify.onIdentifyStatus(cmdId, (GoodixFpIdentifyResult) result);
        }
    }

    public void onOTPCheckTestStatus(int cmdId, Object result) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onOTPCheckTestStatus mResultNotify = ");
        sb.append(this.mResultNotify);
        sb.append(" cmdId = ");
        sb.append(cmdId);
        Common.LOG_D(str, sb.toString());
        if (this.mResultNotify != null) {
            this.mResultNotify.onOTPCheckNotify(cmdId, result);
        }
    }

    public void onGetSensorInfoStatus(int cmdId, Object result) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onGetSensorInfoStatus mResultNotify = ");
        sb.append(this.mResultNotify);
        sb.append(" cmdId = ");
        sb.append(cmdId);
        Common.LOG_D(str, sb.toString());
        if (this.mResultNotify != null) {
            this.mResultNotify.onGetSensorInfoNotify(cmdId, result);
        }
    }
}
