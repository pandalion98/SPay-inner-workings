package com.sec.android.app.hwmoduletest.sensors;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.hardware.fingerprint.FingerprintManager.SemRequestCallback;
import com.qualcomm.qti.biometrics.fingerprint.QFSEventListener;
import com.qualcomm.qti.biometrics.fingerprint.QFSManager;
import com.qualcomm.qti.biometrics.fingerprint.QFSScanListener;
import com.sec.android.app.hwmoduletest.C0268R;
import com.sec.android.app.hwmoduletest.support.LtUtil;
import com.sec.android.app.hwmoduletest.support.Status;
import com.sec.xmldata.support.Support.Kernel;
import com.sec.xmldata.support.Support.Properties;
import com.sec.xmldata.support.Support.Spec;
import egis.client.api.EgisFingerprint;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class CommonFingerprint_qbt2000 {
    public static final int PRIORITY_FP_CONTROLLER = 20;
    public static final int PRIORITY_FP_SCAN = 10;
    private static QFSManager mFingerprint = null;
    /* access modifiers changed from: private */
    public String CLASS_NAME;
    public final int FINGERPRINT_ACQUIRED_FACTORY_TEST_SNSR_TEST_SCRIPT_END;
    public final int FINGERPRINT_ACQUIRED_FACTORY_TEST_SNSR_TEST_SCRIPT_START;
    public final String MSG_CAL_FINISH_FAIL;
    public final String MSG_CAL_FINISH_PASS;
    public final String MSG_CAPTURE_FAIL;
    public final String MSG_CAPTURE_READY;
    public final String MSG_CAPTURE_SUCCESS;
    public final String MSG_CONTAMINATION_FAIL;
    public final String MSG_CONTAMINATION_PASS;
    public final String MSG_INT1_FAIL;
    public final String MSG_INT1_SUCCESS;
    public final String MSG_NORMAL_SCAN_FAIL;
    public final String MSG_NORMAL_SCAN_FINISH_TZ;
    public final String MSG_NORMAL_SCAN_START_TZ;
    public final String MSG_NORMAL_SCAN_SUCCESS;
    public final String MSG_SNR_SCAN_FAIL;
    public final String MSG_SNR_SCAN_READY;
    public final String MSG_SNR_SCAN_SUCCESS;
    public final String MSG_SNR_SCAN_TIMEOUT;
    public final String MSG_TIME_OUT;
    private Thread checkTimeOutThread;
    /* access modifiers changed from: private */
    public boolean isEnabledListeners;
    /* access modifiers changed from: private */
    public Context mContext;
    private FingerPrintEventListener mFingerPrintEventListener;
    private FingerPrintScanListener mFingerPrintScanListener;
    private FingerprintManager mFingerprintManager;
    private NormalScanEventListener_TZ mNormalScanEventListener_TZ;
    private boolean mTzEnabled;
    private TestType testType;

    public enum CalDataCopyType {
        BACKUP,
        RESTORE
    }

    private class FingerPrintEventListener implements QFSEventListener {
        private final String TAG;

        private FingerPrintEventListener() {
            this.TAG = "FingerPrintEventListener-";
        }

        public void onFingerprintEvent(int eventId, Object eventData) {
            String str;
            String access$100 = CommonFingerprint_qbt2000.this.CLASS_NAME;
            String str2 = "FingerPrintEventListener-onFingerprintEvent";
            StringBuilder sb = new StringBuilder();
            sb.append("eventId : ");
            sb.append(eventId);
            sb.append(", eventData : ");
            sb.append(eventData);
            if (eventData == null) {
                str = "";
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" (Class: ");
                sb2.append(eventData.getClass().getName());
                sb2.append(")");
                str = sb2.toString();
            }
            sb.append(str);
            LtUtil.log_d(access$100, str2, sb.toString());
            if (!CommonFingerprint_qbt2000.this.isEnabledListeners) {
                LtUtil.log_d(CommonFingerprint_qbt2000.this.CLASS_NAME, "FingerPrintEventListener-onFingerprintEvent", "Disabled Listener - Events Ignored");
                return;
            }
            Intent i = new Intent();
            TestType testType = CommonFingerprint_qbt2000.this.getTestType();
            if (testType == TestType.CONTAMINATION) {
                if (eventId == 9999 && (eventData instanceof Integer)) {
                    int result = Integer.parseInt(eventData.toString());
                    if (result == 0) {
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CONTAMINATION_PASS");
                    } else if (result == 1) {
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CONTAMINATION_FAIL");
                    } else {
                        LtUtil.log_e(CommonFingerprint_qbt2000.this.CLASS_NAME, "FingerPrintEventListener-onFingerprintEvent", "Unknown CONTAMINATION CHECK Result");
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CONTAMINATION_FAIL");
                    }
                }
            } else if (testType == TestType.BGECAL) {
                if (eventId == 9999 && (eventData instanceof Integer)) {
                    int result2 = Integer.parseInt(eventData.toString());
                    if (result2 == 0) {
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CAL_FINISH_PASS");
                    } else if (result2 == 1) {
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CAL_FINISH_FAIL");
                    } else {
                        LtUtil.log_e(CommonFingerprint_qbt2000.this.CLASS_NAME, "FingerPrintEventListener-onFingerprintEvent", "Unknown BGE Cal Result");
                    }
                }
            } else if (testType == TestType.NORMALSCAN || testType == TestType.SNRSCAN) {
                if (eventId == 9999 && (eventData instanceof Integer) && Integer.parseInt(eventData.toString()) == 1) {
                    LtUtil.log_d(CommonFingerprint_qbt2000.this.CLASS_NAME, "FingerPrintEventListener-onFingerprintEvent", "One of QFS tests FAIL");
                }
            } else if (testType == TestType.INT1) {
                if (eventId == 9999 && (eventData instanceof Integer)) {
                    if (Integer.parseInt(eventData.toString()) == 0) {
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_INT1_SUCCESS");
                    } else {
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_INT1_FAIL");
                    }
                }
            } else if (testType == TestType.INT2) {
                switch (eventId) {
                    case 4001:
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CAPTURE_READY");
                        break;
                    case 4002:
                    case 4003:
                    case 4006:
                    case 4007:
                        break;
                    case 4004:
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CAPTURE_SUCCESS");
                        break;
                    case 4005:
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CAPTURE_FAIL");
                        break;
                    default:
                        switch (eventId) {
                        }
                }
            }
            if (i.getAction() != null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Send Broadcast: ");
                sb3.append(i.getAction());
                LtUtil.log_d(CommonFingerprint_qbt2000.this.CLASS_NAME, "FingerPrintEventListener-onFingerprintEvent", sb3.toString());
                CommonFingerprint_qbt2000.this.mContext.sendBroadcast(i);
            }
        }
    }

    private class FingerPrintScanListener implements QFSScanListener {
        private final String TAG;

        private FingerPrintScanListener() {
            this.TAG = "FingerPrintScanListener-";
        }

        public void onFingerprintEvent(int eventId, Object eventData) {
            String str;
            String access$100 = CommonFingerprint_qbt2000.this.CLASS_NAME;
            String str2 = "FingerPrintScanListener-onFingerprintEvent";
            StringBuilder sb = new StringBuilder();
            sb.append("eventId : ");
            sb.append(eventId);
            sb.append(", eventData : ");
            sb.append(eventData);
            if (eventData == null) {
                str = "";
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" (Class: ");
                sb2.append(eventData.getClass().getName());
                sb2.append(")");
                str = sb2.toString();
            }
            sb.append(str);
            LtUtil.log_d(access$100, str2, sb.toString());
            if (!CommonFingerprint_qbt2000.this.isEnabledListeners) {
                LtUtil.log_d(CommonFingerprint_qbt2000.this.CLASS_NAME, "FingerPrintScanListener-onFingerprintEvent", "Disabled Listener - Events Ignored");
                return;
            }
            Intent i = new Intent();
            TestType testType = CommonFingerprint_qbt2000.this.getTestType();
            if (testType == TestType.NORMALSCAN) {
                if (eventId == 8888 && (eventData instanceof Integer)) {
                    int result = Integer.parseInt(eventData.toString());
                    if (result == 0) {
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_SUCCESS");
                    } else if (result == 1) {
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_FAIL");
                    }
                }
            } else if (testType == TestType.SNRSCAN && eventId == 8888 && (eventData instanceof Integer)) {
                int result2 = Integer.parseInt(eventData.toString());
                if (result2 == 0) {
                    i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNRc_SCAN_SUCCESS");
                } else if (result2 == 1) {
                    i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNR_SCAN_FAIL");
                } else if (result2 == 2) {
                    i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNR_SCAN_READY");
                } else if (result2 == 3) {
                    i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNR_SCAN_TIMEOUT");
                }
            }
            if (i.getAction() != null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Send Broadcast: ");
                sb3.append(i.getAction());
                LtUtil.log_d(CommonFingerprint_qbt2000.this.CLASS_NAME, "FingerPrintScanListener-onFingerprintEvent", sb3.toString());
                CommonFingerprint_qbt2000.this.mContext.sendBroadcast(i);
            }
        }
    }

    private class NormalScanEventListener_TZ extends SemRequestCallback {
        private final String TAG;

        private NormalScanEventListener_TZ() {
            this.TAG = "NormalScanEventListener_TZ-";
        }

        public void onRequested(int msgId) {
            StringBuilder sb = new StringBuilder();
            sb.append("msgId : ");
            sb.append(msgId);
            LtUtil.log_d(CommonFingerprint_qbt2000.this.CLASS_NAME, "NormalScanEventListener_TZ-onRequested", sb.toString());
            if (!CommonFingerprint_qbt2000.this.isEnabledListeners) {
                LtUtil.log_d(CommonFingerprint_qbt2000.this.CLASS_NAME, "NormalScanEventListener_TZ-onRequested", "Disabled Listener - msg Ignored");
                return;
            }
            Intent i = new Intent();
            if (CommonFingerprint_qbt2000.this.getTestType() == TestType.NORMALSCAN) {
                switch (msgId) {
                    case 10008:
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_START_TZ");
                        break;
                    case 10009:
                        i.setAction("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_FINISH_TZ");
                        break;
                }
                if (i.getAction() != null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Send Broadcast: ");
                    sb2.append(i.getAction());
                    LtUtil.log_d(CommonFingerprint_qbt2000.this.CLASS_NAME, "NormalScanEventListener_TZ-onRequested", sb2.toString());
                    CommonFingerprint_qbt2000.this.mContext.sendBroadcast(i);
                }
            }
        }
    }

    public class ScanData {
        private String mCategory;
        private String mName;
        private Status mResult = Status.FAIL;
        private String mSpecMax;
        private String mSpecMin;
        private String mValue;

        public ScanData() {
        }

        public void decideResult() {
            if (this.mValue.equalsIgnoreCase("TBD") || this.mValue.equalsIgnoreCase("NA") || this.mValue.contains("NA")) {
                setResult(Status.PASS);
            } else if (!this.mSpecMin.equals("9999") || !this.mSpecMax.equals("-9999")) {
                try {
                    float value = Float.parseFloat(this.mValue);
                    if (!this.mSpecMin.equalsIgnoreCase(Properties.PROPERTIES_DEFAULT_STRING) || !this.mSpecMax.equalsIgnoreCase(Properties.PROPERTIES_DEFAULT_STRING)) {
                        try {
                            float min = Float.parseFloat(this.mSpecMin);
                            float max = Float.parseFloat(this.mSpecMax);
                            if (value < min || value > max) {
                                setResult(Status.FAIL);
                            } else {
                                setResult(Status.PASS);
                            }
                        } catch (NumberFormatException e) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(" spec parsing fail - mName:");
                            sb.append(this.mName);
                            sb.append(" mValue:");
                            sb.append(this.mValue);
                            sb.append(" mSpecMin:");
                            sb.append(this.mSpecMin);
                            sb.append(" mSpecMax:");
                            sb.append(this.mSpecMax);
                            LtUtil.log_e(CommonFingerprint_qbt2000.this.CLASS_NAME, "decideResult", sb.toString());
                            setResult(Status.ERROR);
                        }
                    } else {
                        setResult(Status.PASS);
                    }
                } catch (NumberFormatException e2) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(" value parsing fail - mName:");
                    sb2.append(this.mName);
                    sb2.append(" mValue:");
                    sb2.append(this.mValue);
                    sb2.append(" mSpecMin:");
                    sb2.append(this.mSpecMin);
                    sb2.append(" mSpecMax:");
                    sb2.append(this.mSpecMax);
                    LtUtil.log_e(CommonFingerprint_qbt2000.this.CLASS_NAME, "decideResult", sb2.toString());
                    setResult(Status.ERROR);
                }
            } else {
                setResult(Status.PASS);
            }
        }

        public void setCategory(String mCategory2) {
            this.mCategory = mCategory2;
        }

        public void setName(String mName2) {
            this.mName = mName2;
        }

        public void setSpecMin(String mSpecMin2) {
            this.mSpecMin = mSpecMin2;
        }

        public void setSpecMax(String mSpecMax2) {
            this.mSpecMax = mSpecMax2;
        }

        public void setValue(String mValue2) {
            this.mValue = mValue2;
        }

        public void setResult(Status mResult2) {
            this.mResult = mResult2;
        }

        public String getCategory() {
            return this.mCategory;
        }

        public String getName() {
            return this.mName;
        }

        public String getSpecMin() {
            return this.mSpecMin;
        }

        public String getSpecMax() {
            return this.mSpecMax;
        }

        public String getValue() {
            return this.mValue;
        }

        public Status getResult() {
            return this.mResult;
        }
    }

    public enum TestType {
        NORMALSCAN,
        SNRSCAN,
        BGECAL,
        INT1,
        INT2,
        CONTAMINATION,
        NOTEST
    }

    public enum TestWay {
        ATCMD,
        MANUAL
    }

    public CommonFingerprint_qbt2000(Context context, String callerName) {
        this(context, callerName, false);
    }

    public CommonFingerprint_qbt2000(Context context, String callerName, boolean isTzEnabled) {
        this.CLASS_NAME = "CommonFingerprint_qbt2000";
        this.mFingerPrintEventListener = null;
        this.mFingerPrintScanListener = null;
        this.MSG_NORMAL_SCAN_SUCCESS = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_SUCCESS";
        this.MSG_NORMAL_SCAN_FAIL = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_FAIL";
        this.MSG_SNR_SCAN_SUCCESS = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNRc_SCAN_SUCCESS";
        this.MSG_SNR_SCAN_FAIL = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNR_SCAN_FAIL";
        this.MSG_SNR_SCAN_READY = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNR_SCAN_READY";
        this.MSG_SNR_SCAN_TIMEOUT = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_SNR_SCAN_TIMEOUT";
        this.MSG_CONTAMINATION_PASS = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CONTAMINATION_PASS";
        this.MSG_CONTAMINATION_FAIL = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CONTAMINATION_FAIL";
        this.MSG_CAL_FINISH_PASS = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CAL_FINISH_PASS";
        this.MSG_CAL_FINISH_FAIL = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CAL_FINISH_FAIL";
        this.MSG_INT1_SUCCESS = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_INT1_SUCCESS";
        this.MSG_INT1_FAIL = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_INT1_FAIL";
        this.MSG_CAPTURE_READY = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CAPTURE_READY";
        this.MSG_CAPTURE_SUCCESS = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CAPTURE_SUCCESS";
        this.MSG_CAPTURE_FAIL = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_CAPTURE_FAIL";
        this.MSG_TIME_OUT = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_TIME_OUT";
        this.isEnabledListeners = false;
        this.testType = TestType.NOTEST;
        this.FINGERPRINT_ACQUIRED_FACTORY_TEST_SNSR_TEST_SCRIPT_START = 10008;
        this.FINGERPRINT_ACQUIRED_FACTORY_TEST_SNSR_TEST_SCRIPT_END = 10009;
        this.MSG_NORMAL_SCAN_START_TZ = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_START_TZ";
        this.MSG_NORMAL_SCAN_FINISH_TZ = "com.sec.factory.common.CommonFingerprint_qbt2000.MSG_NORMAL_SCAN_FINISH_TZ";
        this.mTzEnabled = false;
        StringBuilder sb = new StringBuilder();
        sb.append(callerName);
        sb.append("(");
        sb.append(this.CLASS_NAME);
        sb.append(")");
        this.CLASS_NAME = sb.toString();
        this.mContext = context;
        this.mTzEnabled = isTzEnabled;
        enableListeners();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" mTzEnabled:");
        sb2.append(this.mTzEnabled);
        LtUtil.log_d(this.CLASS_NAME, "CommonFingerprint_qbt2000", sb2.toString());
        if (this.mTzEnabled) {
            this.mFingerprintManager = (FingerprintManager) this.mContext.getSystemService("fingerprint");
            if (this.mFingerprintManager == null) {
                LtUtil.log_e(this.CLASS_NAME, "CommonFingerprint_qbt2000", "FAIL to get FingerprintManager instance");
                return;
            }
            return;
        }
        mFingerprint = getQFSManagerInstance();
        if (mFingerprint == null) {
            LtUtil.log_e(this.CLASS_NAME, "CommonFingerprint_qbt2000", "FAIL to get getQFSManagerInstance instance");
        }
        this.mFingerPrintEventListener = new FingerPrintEventListener();
    }

    private QFSManager getQFSManagerInstance() {
        if (mFingerprint == null) {
            synchronized (QFSManager.class) {
                if (mFingerprint == null) {
                    mFingerprint = new QFSManager();
                    LtUtil.log_d(this.CLASS_NAME, "getQFSManagerInstance", "get new object");
                }
            }
        } else {
            LtUtil.log_d(this.CLASS_NAME, "getQFSManagerInstance", "get existent instance");
        }
        return mFingerprint;
    }

    private byte[] getNormalScanResult_TZ() {
        byte[] outBuffer = new byte[30720];
        Arrays.fill(outBuffer, 0);
        if (this.mFingerprintManager.semGetSensorTestResult(outBuffer) >= 0) {
            return outBuffer;
        }
        LtUtil.log_e(this.CLASS_NAME, "getNormalScanResult_TZ", "FAIL - FingerprintManager.semGetSensorTestResult()");
        return null;
    }

    private FingerPrintScanListener getScanListener() {
        LtUtil.log_d(this.CLASS_NAME, "getScanListener", "getScanListener");
        if (this.mFingerPrintScanListener == null) {
            this.mFingerPrintScanListener = new FingerPrintScanListener();
        }
        return this.mFingerPrintScanListener;
    }

    public void enableListeners() {
        LtUtil.log_d(this.CLASS_NAME, "enableListeners", "enableListeners");
        this.isEnabledListeners = true;
    }

    public void disableListeners() {
        LtUtil.log_d(this.CLASS_NAME, "disableListeners", "disableListeners");
        this.isEnabledListeners = false;
    }

    private void setTestType(TestType testType2) {
        StringBuilder sb = new StringBuilder();
        sb.append(" Before:");
        sb.append(this.testType.name());
        sb.append(" After:");
        sb.append(testType2);
        LtUtil.log_d(this.CLASS_NAME, "setTestType", sb.toString());
        this.testType = testType2;
    }

    public TestType getTestType() {
        StringBuilder sb = new StringBuilder();
        sb.append(" Current:");
        sb.append(this.testType);
        LtUtil.log_d(this.CLASS_NAME, "getTestType", sb.toString());
        return this.testType;
    }

    public long startCheckTimeOut(final long timeout) {
        StringBuilder sb = new StringBuilder();
        sb.append(" timeout:");
        sb.append(timeout);
        LtUtil.log_d(this.CLASS_NAME, "startCheckTimeOut", sb.toString());
        final String TAG = "startCheckTimeOut-";
        this.checkTimeOutThread = new Thread(new Runnable() {
            public void run() {
                String access$100 = CommonFingerprint_qbt2000.this.CLASS_NAME;
                StringBuilder sb = new StringBuilder();
                sb.append(TAG);
                sb.append("thread_run");
                LtUtil.log_d(access$100, sb.toString(), "checkTimeOut thread start");
                try {
                    Thread.sleep(timeout);
                    Intent i = new Intent("com.sec.factory.common.CommonFingerprint_qbt2000.MSG_TIME_OUT");
                    Long threadId = Long.valueOf(Thread.currentThread().getId());
                    i.putExtra("THREAD_ID", threadId);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Send Broadcast: ");
                    sb2.append(i.getAction());
                    sb2.append(" threadId:");
                    sb2.append(threadId);
                    LtUtil.log_d(CommonFingerprint_qbt2000.this.CLASS_NAME, "startCheckTimeOut", sb2.toString());
                    CommonFingerprint_qbt2000.this.mContext.sendBroadcast(i);
                } catch (InterruptedException e) {
                    String access$1002 = CommonFingerprint_qbt2000.this.CLASS_NAME;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(TAG);
                    sb3.append("thread_run");
                    LtUtil.log_d(access$1002, sb3.toString(), "checkTimeOut thread Interrupted");
                }
            }
        });
        this.checkTimeOutThread.start();
        return this.checkTimeOutThread.getId();
    }

    public void endCheckTimeOut() {
        if (this.checkTimeOutThread != null) {
            this.checkTimeOutThread.interrupt();
            LtUtil.log_d(this.CLASS_NAME, "endCheckTimeOut", "endCheckTimeOut");
        }
    }

    public void killRunningService(String packageName, String fullClassName) {
        StringBuilder sb = new StringBuilder();
        sb.append(" packageName:");
        sb.append(packageName);
        sb.append(" ,fullClassName:");
        sb.append(fullClassName);
        LtUtil.log_d(this.CLASS_NAME, "killRunningService", sb.toString());
        ComponentName mComponentName = new ComponentName(packageName, fullClassName);
        Intent i = new Intent();
        i.setComponent(mComponentName);
        Boolean result = Boolean.valueOf(this.mContext.stopService(i));
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" kill result:");
        sb2.append(result);
        LtUtil.log_d(this.CLASS_NAME, "killRunningService", sb2.toString());
    }

    public void checkContamination() {
        LtUtil.log_d(this.CLASS_NAME, "checkContamination", "checkContamination");
        setTestType(TestType.CONTAMINATION);
        enableListeners();
        mFingerprint.setEventListener(this.mFingerPrintEventListener);
        mFingerprint.runQFSTest(1030);
    }

    public void runBGEcal() {
        String bgeData_path = Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_RESULT_PATH);
        StringBuilder sb = new StringBuilder();
        sb.append(" path:");
        sb.append(bgeData_path);
        LtUtil.log_d(this.CLASS_NAME, "runBGEcal", sb.toString());
        setTestType(TestType.BGECAL);
        enableListeners();
        mFingerprint.setEventListener(this.mFingerPrintEventListener);
        mFingerprint.runBGECalibration(bgeData_path);
    }

    public boolean checkBGE() {
        boolean result = mFingerprint.checkBGE();
        StringBuilder sb = new StringBuilder();
        sb.append(" result");
        sb.append(String.valueOf(result));
        LtUtil.log_d(this.CLASS_NAME, "checkBGE", sb.toString());
        return result;
    }

    public boolean delBGE() {
        File bgeDataFile1 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_RESULT_FILE_1));
        File bgeDataFile2 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_RESULT_FILE_2));
        File bgeDataFile3 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_RESULT_FILE_3));
        if (bgeDataFile1.exists() || bgeDataFile2.exists() || bgeDataFile3.exists()) {
            String bgeData_path = Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_RESULT_PATH);
            boolean result = mFingerprint.delBGE(bgeData_path);
            StringBuilder sb = new StringBuilder();
            sb.append(" path:");
            sb.append(bgeData_path);
            sb.append(" delete result:");
            sb.append(String.valueOf(result));
            LtUtil.log_d(this.CLASS_NAME, "delBGE", sb.toString());
            return result;
        }
        LtUtil.log_d(this.CLASS_NAME, "delBGE", " bge data not exist");
        return true;
    }

    public boolean saveSensorID() {
        boolean result = false;
        String sensorID = getSensorInfoValue(getRawSensorInfo(), "Sensor ID");
        if (!sensorID.equals("N/A")) {
            result = Kernel.writeNsync(Kernel.ULTRASONIC_SENSOR_ID_SAVE_PATH, sensorID);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" result:");
        sb.append(result);
        LtUtil.log_d(this.CLASS_NAME, "saveSensorIDinEFS", sb.toString());
        return result;
    }

    public String readSavedSensorID() {
        String savedSensorID = Kernel.read(Kernel.ULTRASONIC_SENSOR_ID_SAVE_PATH);
        StringBuilder sb = new StringBuilder();
        sb.append(" savedSensorID:");
        sb.append(savedSensorID);
        LtUtil.log_d(this.CLASS_NAME, "readSavedSensorID", sb.toString());
        if (savedSensorID == null) {
            return "NONE";
        }
        return savedSensorID;
    }

    public boolean deleteSavedSensorID() {
        if (new File(Kernel.getFilePath(Kernel.ULTRASONIC_SENSOR_ID_SAVE_PATH)).exists()) {
            boolean result = Kernel.deleteFile(Kernel.ULTRASONIC_SENSOR_ID_SAVE_PATH);
            StringBuilder sb = new StringBuilder();
            sb.append(" result:");
            sb.append(result);
            LtUtil.log_d(this.CLASS_NAME, "deleteSavedSensorID", sb.toString());
            return result;
        }
        LtUtil.log_d(this.CLASS_NAME, "deleteSavedSensorID", " sensorID File not exist");
        return true;
    }

    public boolean isExistBgeCalData() {
        File bgeDataFile1 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_RESULT_FILE_1));
        File bgeDataFile2 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_RESULT_FILE_2));
        File bgeDataFile3 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_RESULT_FILE_3));
        if (!bgeDataFile1.exists() || !bgeDataFile2.exists() || !bgeDataFile3.exists()) {
            LtUtil.log_d(this.CLASS_NAME, "isExistBgeCalData", " Not Exist BGE Data");
            return false;
        }
        LtUtil.log_d(this.CLASS_NAME, "isExistBgeCalData", " Exist BGE Data");
        return true;
    }

    public boolean isExistBackUpBgeCalData() {
        File bgeDataFile1 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_BACKUP_FILE_1));
        File bgeDataFile2 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_BACKUP_FILE_2));
        File bgeDataFile3 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_BACKUP_FILE_3));
        if (!bgeDataFile1.exists() || !bgeDataFile2.exists() || !bgeDataFile3.exists()) {
            LtUtil.log_d(this.CLASS_NAME, "isExistBackUpBgeCalData", " Not Exist BackUp BGE Data");
            return false;
        }
        LtUtil.log_d(this.CLASS_NAME, "isExistBackUpBgeCalData", " Exist BackUp BGE Data");
        return true;
    }

    public boolean copyBgeCalData(CalDataCopyType copyType) {
        StringBuilder sb = new StringBuilder();
        sb.append(" copyType:");
        sb.append(copyType.name());
        LtUtil.log_d(this.CLASS_NAME, "copyBgeCalData", sb.toString());
        File bgeDataFile1 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_RESULT_FILE_1));
        File bgeDataFile2 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_RESULT_FILE_2));
        File bgeDataFile3 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_RESULT_FILE_3));
        File bak_bgeDataFile1 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_BACKUP_FILE_1));
        File bak_bgeDataFile2 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_BACKUP_FILE_2));
        File bak_bgeDataFile3 = new File(Kernel.getFilePath(Kernel.ULTRASONIC_BGE_CAL_BACKUP_FILE_3));
        switch (copyType) {
            case BACKUP:
                if (copyFile(bgeDataFile1.getPath(), bak_bgeDataFile1.getPath()) && copyFile(bgeDataFile2.getPath(), bak_bgeDataFile2.getPath()) && copyFile(bgeDataFile3.getPath(), bak_bgeDataFile3.getPath())) {
                    return true;
                }
            case RESTORE:
                if (copyFile(bak_bgeDataFile1.getPath(), bgeDataFile1.getPath()) && copyFile(bak_bgeDataFile2.getPath(), bgeDataFile2.getPath()) && copyFile(bak_bgeDataFile3.getPath(), bgeDataFile3.getPath())) {
                    return true;
                }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("FAIL - ");
        sb2.append(copyType.name());
        LtUtil.log_e(this.CLASS_NAME, "copyBgeCalData", sb2.toString());
        return false;
    }

    private boolean copyFile(String src, String dst) {
        boolean res = false;
        FileInputStream in = null;
        FileOutputStream out = null;
        StringBuilder sb = new StringBuilder();
        sb.append(" sourcePath:");
        sb.append(src);
        sb.append(" targetPath:");
        sb.append(dst);
        LtUtil.log_d(this.CLASS_NAME, "copyFile", sb.toString());
        if (src == null || dst == null) {
            LtUtil.log_e(this.CLASS_NAME, "copyFile", " copy fil path wrong");
        } else {
            File dstDirectoryPath = new File(dst).getParentFile();
            if (dstDirectoryPath == null) {
                LtUtil.log_e(this.CLASS_NAME, "copyFile", " dst directory path is null, return false");
                return false;
            }
            if (!dstDirectoryPath.exists()) {
                dstDirectoryPath.mkdirs();
                LtUtil.log_d(this.CLASS_NAME, "copyFile", " dst directory not exist -> exist");
            }
            try {
                FileInputStream in2 = new FileInputStream(src);
                FileOutputStream out2 = new FileOutputStream(dst);
                byte[] buf = new byte[8192];
                while (true) {
                    int read = in2.read(buf);
                    int data = read;
                    if (read <= 0) {
                        break;
                    }
                    out2.write(buf, 0, data);
                }
                LtUtil.log_d(this.CLASS_NAME, "copyFile", " copy file success");
                res = true;
                try {
                    in2.close();
                    out2.flush();
                    out2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                LtUtil.log_e(this.CLASS_NAME, "copyFile", " copy file fail");
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.flush();
                    out.close();
                }
            } catch (Throwable th) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        throw th;
                    }
                }
                if (out != null) {
                    out.flush();
                    out.close();
                }
                throw th;
            }
        }
        return res;
    }

    public String[] getScanItemList(TestType testType2) {
        String[] scanItemList = null;
        switch (testType2) {
            case NORMALSCAN:
                if (!this.mTzEnabled) {
                    scanItemList = this.mContext.getResources().getStringArray(C0268R.array.fingerprint_qbt2000_normal_scan_item_list_shown);
                    break;
                } else {
                    scanItemList = this.mContext.getResources().getStringArray(C0268R.array.fingerprint_qbt2000_normal_scan_item_list_shown_user_bin);
                    break;
                }
            case SNRSCAN:
                scanItemList = this.mContext.getResources().getStringArray(C0268R.array.fingerprint_qbt2000_snr_scan_item_list_shown);
                break;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" mTzEnabled:");
        sb.append(this.mTzEnabled);
        sb.append(" testType:");
        sb.append(testType2.name());
        sb.append(" scan test item count:");
        sb.append(scanItemList.length);
        LtUtil.log_d(this.CLASS_NAME, "getScanItemList", sb.toString());
        return scanItemList;
    }

    public String getScanResultFilePath(TestType testType2) {
        String path = null;
        switch (testType2) {
            case NORMALSCAN:
                path = Kernel.getFilePath(Kernel.ULTRASONIC_FP_NRSC_RESULT);
                break;
            case SNRSCAN:
                path = Kernel.getFilePath(Kernel.ULTRASONIC_FP_SNR_RESULT);
                break;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" path:");
        sb.append(path);
        LtUtil.log_d(this.CLASS_NAME, "getScanResultFilePath", sb.toString());
        return path;
    }

    public String getValueAllfromScanData(String[] itemNameList, HashMap<String, ScanData> scanDataHashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(" Test Item Total Cnt:");
        sb.append(itemNameList.length);
        LtUtil.log_d(this.CLASS_NAME, "getValueAllfromScanData", sb.toString());
        String data = "";
        for (String itemName : itemNameList) {
            if (scanDataHashMap.containsKey(itemName)) {
                ScanData scanData = (ScanData) scanDataHashMap.get(itemName);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(data);
                sb2.append(scanData.getValue());
                sb2.append(",");
                data = sb2.toString();
            } else {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(" Cannot Find Item:");
                sb3.append(itemName);
                LtUtil.log_e(this.CLASS_NAME, "getValueAllfromScanData", sb3.toString());
                StringBuilder sb4 = new StringBuilder();
                sb4.append(data);
                sb4.append("NA,");
                data = sb4.toString();
            }
        }
        String data2 = data.substring(0, data.length() - 1);
        StringBuilder sb5 = new StringBuilder();
        sb5.append(" data:");
        sb5.append(data2);
        LtUtil.log_d(this.CLASS_NAME, "getValueAllfromScanData", sb5.toString());
        return data2;
    }

    public boolean getFinalResultfromScanData(String[] itemNameList, HashMap<String, ScanData> scanDataHashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append(" Test Item Total Cnt:");
        sb.append(itemNameList.length);
        LtUtil.log_d(this.CLASS_NAME, "getFinalResultfromScanData", sb.toString());
        boolean result = false;
        int failCnt = 0;
        int passCnt = 0;
        for (String itemName : itemNameList) {
            if (scanDataHashMap.containsKey(itemName)) {
                ScanData scanData = (ScanData) scanDataHashMap.get(itemName);
                if (scanData.getResult() == Status.PASS || scanData.getResult() == null || scanData.getResult() == Status.NOTEST) {
                    passCnt++;
                } else {
                    failCnt++;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(scanData.getSpecMin());
                    sb2.append(" ~ ");
                    sb2.append(scanData.getSpecMax());
                    String specStr = sb2.toString();
                    if (specStr.contains(Properties.PROPERTIES_DEFAULT_STRING)) {
                        specStr = "-";
                    } else if (specStr.contains("9999 ~ -9999")) {
                        specStr = "TBD";
                    }
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(" FAIL - ItemName: ");
                    sb3.append(itemName);
                    sb3.append(" ,Value: ");
                    sb3.append(scanData.getValue());
                    sb3.append(" ,Spec: ");
                    sb3.append(specStr);
                    LtUtil.log_e(this.CLASS_NAME, "getFinalResultfromScanData", sb3.toString());
                }
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(" Cannot Find Item:");
                sb4.append(itemName);
                LtUtil.log_e(this.CLASS_NAME, "getFinalResultfromScanData", sb4.toString());
            }
        }
        if (passCnt == itemNameList.length || failCnt == 0) {
            result = true;
        }
        boolean result2 = result;
        StringBuilder sb5 = new StringBuilder();
        sb5.append(" Result:");
        sb5.append(result2);
        sb5.append(" ,Total Item Cnt:");
        sb5.append(itemNameList.length);
        sb5.append(" ,Pass Cnt:");
        sb5.append(passCnt);
        sb5.append(" ,Fail Cnt:");
        sb5.append(failCnt);
        LtUtil.log_d(this.CLASS_NAME, "getFinalResultfromScanData", sb5.toString());
        return result2;
    }

    public void startNormalScan() {
        StringBuilder sb = new StringBuilder();
        sb.append(" mTzEnabled:");
        sb.append(this.mTzEnabled);
        LtUtil.log_d(this.CLASS_NAME, "startNormalScan", sb.toString());
        setTestType(TestType.NORMALSCAN);
        if (this.mTzEnabled) {
            this.mNormalScanEventListener_TZ = new NormalScanEventListener_TZ();
            this.mFingerprintManager.semRunSensorTest(0, 0, this.mNormalScanEventListener_TZ);
            return;
        }
        enableListeners();
        mFingerprint.setEventListener(this.mFingerPrintEventListener);
        mFingerprint.startNormalScan(getScanListener());
    }

    public void startSNRPrepare() {
        LtUtil.log_d(this.CLASS_NAME, "startSNRPrepare", "startSNRPrepare");
        setTestType(TestType.SNRSCAN);
        enableListeners();
        mFingerprint.setEventListener(this.mFingerPrintEventListener);
        mFingerprint.startSNRPrepare(getScanListener());
    }

    public void startSNR() {
        LtUtil.log_d(this.CLASS_NAME, "startSNR", "startSNR");
        mFingerprint.startSNR();
    }

    public boolean copySnrImg() {
        File snrImgFile = new File(Kernel.getFilePath(Kernel.ULTRASONIC_FP_SNR_IMAGE));
        File snrImgFile_copy = new File(Kernel.getFilePath(Kernel.ULTRASONIC_FP_SNR_IMAGE_COPY_PATH));
        String time = getTimeToString();
        if (snrImgFile.exists()) {
            String path = snrImgFile.getPath();
            StringBuilder sb = new StringBuilder();
            sb.append(snrImgFile_copy.getPath());
            sb.append("/SNR_Fail_Image_");
            sb.append(time);
            sb.append(".png");
            if (copyFile(path, sb.toString())) {
                return true;
            }
        }
        return false;
    }

    private String getTimeToString() {
        Calendar cal = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00", DecimalFormatSymbols.getInstance(Locale.US));
        String month = df.format((long) (cal.get(2) + 1));
        String day = df.format((long) cal.get(5));
        String hour = df.format((long) cal.get(11));
        String min = df.format((long) cal.get(12));
        StringBuilder sb = new StringBuilder();
        sb.append(cal.get(1));
        sb.append(month);
        sb.append(day);
        sb.append(hour);
        sb.append(min);
        String rtnData = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" rtnData:");
        sb2.append(rtnData);
        LtUtil.log_d(this.CLASS_NAME, "getTimeToString", sb2.toString());
        return rtnData;
    }

    public void endFpManger() {
        if (!this.mTzEnabled && mFingerprint != null) {
            LtUtil.log_d(this.CLASS_NAME, "endFpManger", "cancelTest() called");
            mFingerprint.cancelTest();
        }
    }

    private HashMap<String, String> getNameHashMap(TestType testType2) {
        HashMap<String, String> nameHashMap = new HashMap<>();
        String[] itemListShown = null;
        String[] itemListRead = null;
        StringBuilder sb = new StringBuilder();
        sb.append(" mTzEnabled:");
        sb.append(this.mTzEnabled);
        LtUtil.log_d(this.CLASS_NAME, "getNameHashMap", sb.toString());
        switch (testType2) {
            case NORMALSCAN:
                if (!this.mTzEnabled) {
                    itemListShown = this.mContext.getResources().getStringArray(C0268R.array.fingerprint_qbt2000_normal_scan_item_list_shown);
                    itemListRead = this.mContext.getResources().getStringArray(C0268R.array.fingerprint_qbt2000_normal_scan_item_list_read);
                    break;
                } else {
                    itemListShown = this.mContext.getResources().getStringArray(C0268R.array.fingerprint_qbt2000_normal_scan_item_list_shown_user_bin);
                    itemListRead = this.mContext.getResources().getStringArray(C0268R.array.fingerprint_qbt2000_normal_scan_item_list_read_user_bin);
                    break;
                }
            case SNRSCAN:
                itemListShown = this.mContext.getResources().getStringArray(C0268R.array.fingerprint_qbt2000_snr_scan_item_list_shown);
                itemListRead = this.mContext.getResources().getStringArray(C0268R.array.fingerprint_qbt2000_snr_scan_item_list_read);
                break;
        }
        if (itemListShown.length != itemListRead.length) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" FAIL - Not Same itemListShown cnt:");
            sb2.append(itemListShown.length);
            sb2.append(" itemListRead cnt:");
            sb2.append(itemListRead.length);
            LtUtil.log_e(this.CLASS_NAME, "getNameHashMap", sb2.toString());
        }
        for (int i = 0; i < itemListShown.length; i++) {
            nameHashMap.put(itemListRead[i], itemListShown[i]);
        }
        return nameHashMap;
    }

    private BufferedReader getBufferedReader(TestType testType2) {
        StringBuilder sb = new StringBuilder();
        sb.append(" testType:");
        sb.append(testType2.name());
        sb.append(" mTzEnabled:");
        sb.append(this.mTzEnabled);
        LtUtil.log_d(this.CLASS_NAME, "getBufferedReader", sb.toString());
        if (this.mTzEnabled) {
            byte[] result = getNormalScanResult_TZ();
            if (result != null) {
                return new BufferedReader(new InputStreamReader(new ByteArrayInputStream(result)));
            }
            return null;
        }
        String path = getScanResultFilePath(testType2);
        try {
            return new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" path:");
            sb2.append(path);
            sb2.append(" FAIL - Scan Result File Not Exist");
            LtUtil.log_e(this.CLASS_NAME, "getBufferedReader", sb2.toString());
            return null;
        }
    }

    public HashMap<String, ScanData> getScanDataMap(TestType testType2) {
        LtUtil.log_d(this.CLASS_NAME, "getScanDataMap", "Read start");
        HashMap<String, ScanData> scanDataHashMap = new HashMap<>();
        HashMap<String, String> nameHashMap = getNameHashMap(testType2);
        if (testType2 == TestType.NORMALSCAN) {
            LtUtil.log_d(this.CLASS_NAME, "getScanDataMap", "set COMP_TEMP_REAL data manually");
            ScanData mScanData = new ScanData();
            mScanData.setCategory("TEMP");
            mScanData.setName("COMP_TEMP");
            String value = Kernel.read(Kernel.ULTRASONIC_COMP_TEMP_REAL);
            if (value != null) {
                mScanData.setValue(value.trim());
            }
            mScanData.setSpecMin(Properties.PROPERTIES_DEFAULT_STRING);
            mScanData.setSpecMax(Properties.PROPERTIES_DEFAULT_STRING);
            mScanData.decideResult();
            scanDataHashMap.put(mScanData.getName(), mScanData);
        }
        BufferedReader bufferedReader = getBufferedReader(testType2);
        if (bufferedReader != null) {
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    String readStr = readLine;
                    if (readLine != null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("File line read: ");
                        sb.append(readStr);
                        LtUtil.log_d(this.CLASS_NAME, "getScanDataMap", sb.toString());
                        if (!readStr.contains("Begin") && !readStr.contains("End")) {
                            if (!readStr.contains("Date:")) {
                                ScanData mScanData2 = new ScanData();
                                String[] splitStr = readStr.split(":", 2);
                                if (splitStr.length > 1) {
                                    mScanData2.setValue(splitStr[1].trim());
                                } else {
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("No Value - ");
                                    sb2.append(splitStr[0]);
                                    LtUtil.log_e(this.CLASS_NAME, "getScanDataMap", sb2.toString());
                                }
                                String[] splitStr2 = splitStr[0].split("-", 2);
                                if (splitStr2.length > 1) {
                                    mScanData2.setCategory(splitStr2[0].trim().replace(" ", "_").toUpperCase());
                                    String nameForRead = (String) nameHashMap.get(splitStr2[1].trim());
                                    if (nameForRead != null) {
                                        mScanData2.setName(nameForRead);
                                    }
                                } else {
                                    mScanData2.setCategory("-");
                                    String nameForRead2 = (String) nameHashMap.get(splitStr2[0].trim());
                                    if (nameForRead2 != null) {
                                        mScanData2.setName(nameForRead2);
                                    }
                                }
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("FINGERPRINT_QBT_");
                                sb3.append(mScanData2.getName());
                                sb3.append("_MIN");
                                mScanData2.setSpecMin(Spec.getString(sb3.toString()));
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("FINGERPRINT_QBT_");
                                sb4.append(mScanData2.getName());
                                sb4.append("_MAX");
                                mScanData2.setSpecMax(Spec.getString(sb4.toString()));
                                mScanData2.decideResult();
                                if (this.mTzEnabled && mScanData2.getName() != null && mScanData2.getName().equals("BGE_CAL_CHECK")) {
                                    mScanData2.setSpecMin(Spec.getString(Properties.PROPERTIES_DEFAULT_STRING));
                                    mScanData2.setSpecMax(Spec.getString(Properties.PROPERTIES_DEFAULT_STRING));
                                    mScanData2.setResult(Status.NOTEST);
                                }
                                scanDataHashMap.put(mScanData2.getName(), mScanData2);
                            }
                        }
                        LtUtil.log_d(this.CLASS_NAME, "getScanDataMap", "dummy data skip");
                    } else {
                        try {
                            break;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e2) {
                    LtUtil.log_e(this.CLASS_NAME, "getScanDataMap", " FAIL - bufferedReader.readLine()");
                    e2.printStackTrace();
                } finally {
                    try {
                        bufferedReader.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            }
        }
        return scanDataHashMap;
    }

    public void startInt1() {
        LtUtil.log_d(this.CLASS_NAME, "startInt1", "startInt1");
        setTestType(TestType.INT1);
        enableListeners();
        mFingerprint.setEventListener(this.mFingerPrintEventListener);
        mFingerprint.runQFSTest(1010);
    }

    private int setActiveStorage(String testID) {
        String path = Kernel.getFilePath(Kernel.ULTRASONIC_INT_CHECK_PATH);
        StringBuilder sb = new StringBuilder();
        sb.append(" ID:");
        sb.append(testID);
        sb.append(" path:");
        sb.append(path);
        LtUtil.log_d(this.CLASS_NAME, "setActiveStorage", sb.toString());
        int result = mFingerprint.setActiveStorage(testID, path);
        if (result == 0) {
            LtUtil.log_d(this.CLASS_NAME, "setActiveStorage", " setActiveStorage result: OK");
        } else {
            LtUtil.log_d(this.CLASS_NAME, "setActiveStorage", " setActiveStorage result: FAIL");
        }
        return result;
    }

    public boolean startInt2() {
        String testID = "test";
        StringBuilder sb = new StringBuilder();
        sb.append(" ID:");
        sb.append(testID);
        LtUtil.log_d(this.CLASS_NAME, "startInt2", sb.toString());
        setTestType(TestType.INT2);
        enableListeners();
        mFingerprint.setEventListener(this.mFingerPrintEventListener);
        if (setActiveStorage(testID) == 0 && mFingerprint.authenticate(testID) == 0) {
            LtUtil.log_d(this.CLASS_NAME, "startInt2", " result: OK");
            return true;
        }
        LtUtil.log_d(this.CLASS_NAME, "startInt2", " result: FAIL");
        return false;
    }

    public boolean setWuhbMode() {
        boolean result = Kernel.write(Kernel.ULTRASONIC_WUHB_INT_CMD, "wuhb");
        StringBuilder sb = new StringBuilder();
        sb.append(" result:");
        sb.append(result);
        LtUtil.log_d(this.CLASS_NAME, "setWuhbMode", sb.toString());
        return result;
    }

    public boolean getWuhbResult() {
        String result = Kernel.read(Kernel.ULTRASONIC_WUHB_INT_RESULT);
        StringBuilder sb = new StringBuilder();
        sb.append(" result:");
        sb.append(result);
        LtUtil.log_d(this.CLASS_NAME, "getWuhbResult", sb.toString());
        if (EgisFingerprint.MAJOR_VERSION.equals(result)) {
            return true;
        }
        return false;
    }

    public boolean controlTspInt(boolean isOn) {
        String cmd;
        if (isOn) {
            cmd = "fp_int_control,1";
        } else {
            cmd = "fp_int_control,0";
        }
        Kernel.write(Kernel.TSP_COMMAND_CMD, cmd);
        String result = Kernel.read(Kernel.TSP_COMMAND_RESULT);
        StringBuilder sb = new StringBuilder();
        sb.append(" cmd:");
        sb.append(cmd);
        sb.append(" result:");
        sb.append(result);
        LtUtil.log_d(this.CLASS_NAME, "controlTspInt", sb.toString());
        if (result == null) {
            LtUtil.log_d(this.CLASS_NAME, "controlTspInt", "tsp cmd result value is null");
            return false;
        } else if (result.contains("OK")) {
            return true;
        } else {
            return false;
        }
    }

    public void cancelIntCheckOpeation() {
        if (mFingerprint == null) {
            return;
        }
        if (mFingerprint.cancel() == 0) {
            LtUtil.log_d(this.CLASS_NAME, "cancelIntCheckOpeation", " result: OK");
        } else {
            LtUtil.log_e(this.CLASS_NAME, "cancelIntCheckOpeation", " result: FAIL");
        }
    }

    public String getVendorName() {
        String vendorName = Kernel.read(Kernel.FINGERPRINT_VENDOR);
        StringBuilder sb = new StringBuilder();
        sb.append(" vendorName:");
        sb.append(vendorName);
        LtUtil.log_d(this.CLASS_NAME, "getVendorName", sb.toString());
        return vendorName;
    }

    public String getChipName() {
        String chipName = Kernel.read(Kernel.FINGERPRINT_CHIP_NAME);
        StringBuilder sb = new StringBuilder();
        sb.append(" chipName:");
        sb.append(chipName);
        LtUtil.log_d(this.CLASS_NAME, "getChipName", sb.toString());
        return chipName;
    }

    public int getChipTypeIndex() {
        return 9;
    }

    public String getVersion() {
        String versionStr = mFingerprint.getVersion();
        StringBuilder sb = new StringBuilder();
        sb.append(" version:");
        sb.append(versionStr);
        LtUtil.log_d(this.CLASS_NAME, "getVersion", sb.toString());
        if (versionStr == null) {
            return "";
        }
        return versionStr;
    }

    public String getFpVersion() {
        String fpVersionStr = mFingerprint.getFpVersion();
        StringBuilder sb = new StringBuilder();
        sb.append(" fpVersionStr:");
        sb.append(fpVersionStr);
        LtUtil.log_d(this.CLASS_NAME, "getFpVersion", sb.toString());
        if (fpVersionStr == null) {
            return "";
        }
        return fpVersionStr;
    }

    public String getRawSensorInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(" mTzEnabled: ");
        sb.append(this.mTzEnabled);
        LtUtil.log_d(this.CLASS_NAME, "getRawSensorInfo", sb.toString());
        String sensorInfoAll = null;
        if (this.mTzEnabled) {
            sensorInfoAll = this.mFingerprintManager.semGetSensorInfo();
        } else {
            if (mFingerprint.getSensorStatus() == 3001) {
                sensorInfoAll = mFingerprint.getSensorInfo();
            } else {
                LtUtil.log_e(this.CLASS_NAME, "getRawSensorInfo", " Fail - SensorStatus is Not SENSOR_OK");
            }
            endFpManger();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(" sensorInfoAll: ");
        sb2.append(sensorInfoAll);
        LtUtil.log_d(this.CLASS_NAME, "getRawSensorInfo", sb2.toString());
        return sensorInfoAll;
    }

    public String getSensorInfoValue(String rawSensorInfo, String sensorName) {
        if (rawSensorInfo == null) {
            LtUtil.log_d(this.CLASS_NAME, "getSensorInfoValue", " Fail - rawSensorInfo: N/A");
            return "N/A";
        }
        String[] infoArray = rawSensorInfo.split("\n");
        String[] strArr = infoArray;
        for (String info : infoArray) {
            if (info.contains(sensorName)) {
                String[] infoArray2 = info.split(":");
                if (infoArray2.length >= 2) {
                    return infoArray2[1].trim();
                }
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" Fail - name:");
        sb.append(sensorName);
        sb.append(" value: N/A");
        LtUtil.log_d(this.CLASS_NAME, "getSensorInfoValue", sb.toString());
        return "N/A";
    }

    public boolean hasUserIdList() {
        String[] userIdArr = this.mFingerprintManager.semGetUserIdList();
        StringBuilder sb = new StringBuilder();
        sb.append(" userIdArr:");
        sb.append(Arrays.toString(userIdArr));
        LtUtil.log_d(this.CLASS_NAME, "hasUserIdList", sb.toString());
        if (userIdArr == null || userIdArr.length == 0) {
            return false;
        }
        return true;
    }
}
