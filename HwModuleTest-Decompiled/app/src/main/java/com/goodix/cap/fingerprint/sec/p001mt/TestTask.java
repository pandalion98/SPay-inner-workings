package com.goodix.cap.fingerprint.sec.p001mt;

import android.content.Context;
import com.goodix.cap.fingerprint.ext.ExtGoodixFpManager;
import com.goodix.cap.fingerprint.ext.GoodixEventListener;
import com.goodix.cap.fingerprint.sec.GoodixFpEnrollStatus;
import com.goodix.cap.fingerprint.sec.GoodixFpIdentifyResult;
import com.goodix.cap.fingerprint.service.GoodixFingerprintManagerExt;
import com.goodix.cap.fingerprint.service.GoodixFingerprintManagerExt.TestCmdCallback;
import com.goodix.cap.fingerprint.utils.TestResultParser;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.HashMap;

/* renamed from: com.goodix.cap.fingerprint.sec.mt.TestTask */
public class TestTask implements TestCmdCallback {
    private static final int DISABLE_DEVICE_FLAG = 0;
    private static final int ENABLE_DEVICE_FLAG = 1;
    private static final String TAG = "TestTask";
    private static final int TASK_TIME_OUT = 10000;
    private static final int[] UNTOUCH_TEST_ITEMS_1 = {8, 2};
    private static final int[] UNTOUCH_TEST_ITEMS_2 = {24, 23};
    private static final String mAuthenticateResultFile = "/data/log/gf_authenticate_test.txt";
    private static final String mEnrollResultFile = "/data/log/gf_enroll_test.txt";
    private static final String mGetSensorInfoFile = "/data/log/gf_get_sensor_info_test.txt";
    private static final String mOtpCheckResultFile = "/data/log/gf_otp_check_test.txt";
    private static final String mResultBaseFile = "/data/log";
    private static final String mTimeFormat = "yyyy/MM/dd HH:mm:ss SSS";
    private static final String mTouchResultFile = "/data/log/gf_touch_test.txt";
    private static final String mUnTouchResultFile = "/data/log/gf_un_touch_test.txt";
    private static TestTask sTestTask;
    private GoodixEventListener listener = new GoodixEventListener() {
        public void onEvent(int eventId, Object eventData) {
            switch (eventId) {
                case 4004:
                    StringBuffer access$300 = TestTask.this.mEnrollResult;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Capture Status:");
                    sb.append(TestTask.this.parseResult(true));
                    sb.append("\r\n");
                    access$300.append(sb.toString());
                    String str = TestTask.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Capture Status:");
                    sb2.append(TestTask.this.parseResult(true));
                    Common.LOG_D(str, sb2.toString());
                    if (TestTask.this.mTestStatusListener != null) {
                        TestTask.this.mTestStatusListener.onCaptureStatus(4004, eventData);
                        return;
                    } else {
                        Common.LOG_D(TestTask.TAG, "mTestStatusListener is not initialized.");
                        return;
                    }
                case 4005:
                    StringBuffer access$3002 = TestTask.this.mEnrollResult;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Capture Status:");
                    sb3.append(TestTask.this.parseResult(false));
                    sb3.append("\r\n");
                    access$3002.append(sb3.toString());
                    String str2 = TestTask.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Capture Status:");
                    sb4.append(TestTask.this.parseResult(false));
                    Common.LOG_D(str2, sb4.toString());
                    if (TestTask.this.mTestStatusListener != null) {
                        TestTask.this.mTestStatusListener.onCaptureStatus(4005, eventData);
                    } else {
                        Common.LOG_D(TestTask.TAG, "mTestStatusListener is not initialized.");
                    }
                    synchronized (TestTask.this.mTaskThread) {
                        TestTask.this.mTaskThread.notify();
                    }
                    return;
                case 4006:
                    if (TestTask.this.mTestStatusListener != null) {
                        TestTask.this.mTestStatusListener.onCaptureFingerLeaved(4006, null);
                        TestTask.this.mGoodixFpManager.cancel();
                        return;
                    }
                    Common.LOG_D(TestTask.TAG, "mTestStatusListener is not initialized.");
                    return;
                case 5001:
                    GoodixFpEnrollStatus status = (GoodixFpEnrollStatus) eventData;
                    String str3 = TestTask.TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Enroll Image:");
                    sb5.append(status.coverageBitmap);
                    Common.LOG_D(str3, sb5.toString());
                    if (TestTask.this.mTestStatusListener != null) {
                        TestTask.this.mTestStatusListener.onEnrollTestStatus(5001, eventData);
                        return;
                    } else {
                        Common.LOG_D(TestTask.TAG, "mTestStatusListener is not initialized.");
                        return;
                    }
                case 5002:
                    StringBuffer access$3003 = TestTask.this.mEnrollResult;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Enroll Result:");
                    sb6.append(TestTask.this.parseResult(true));
                    sb6.append("\r\n");
                    access$3003.append(sb6.toString());
                    String str4 = TestTask.TAG;
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append("Enroll Result:");
                    sb7.append(TestTask.this.parseResult(true));
                    Common.LOG_D(str4, sb7.toString());
                    if (TestTask.this.mTestStatusListener != null) {
                        TestTask.this.mTestStatusListener.onEnrollTestStatus(5002, eventData);
                    } else {
                        Common.LOG_D(TestTask.TAG, "mTestStatusListener is not initialized.");
                    }
                    synchronized (TestTask.this.mTaskThread) {
                        TestTask.this.mTaskThread.notify();
                    }
                    return;
                case 6001:
                    StringBuffer access$600 = TestTask.this.mAuthenticateResult;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("Authenticate Result:");
                    sb8.append(TestTask.this.parseResult(true));
                    sb8.append("\r\n");
                    access$600.append(sb8.toString());
                    String str5 = TestTask.TAG;
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append("Authenticate Result:");
                    sb9.append(TestTask.this.parseResult(true));
                    Common.LOG_D(str5, sb9.toString());
                    if (TestTask.this.mTestStatusListener != null) {
                        TestTask.this.mTestStatusListener.onAuthenticateStatus(6001, eventData);
                    } else {
                        Common.LOG_D(TestTask.TAG, "mTestStatusListener is not initialized.");
                    }
                    synchronized (TestTask.this.mTaskThread) {
                        TestTask.this.mGoodixFpManager.remove("Test", 1);
                        TestTask.this.mTaskThread.notify();
                    }
                    return;
                case 6002:
                    if (((GoodixFpIdentifyResult) eventData).result == 6101) {
                        synchronized (TestTask.this.mTaskThread) {
                            TestTask.this.mTaskThread.notify();
                        }
                        return;
                    }
                    if (TestTask.this.mTestStatusListener != null) {
                        TestTask.this.mTestStatusListener.onAuthenticateStatus(6002, eventData);
                    } else {
                        Common.LOG_D(TestTask.TAG, "mTestStatusListener is not initialized.");
                    }
                    StringBuffer access$6002 = TestTask.this.mAuthenticateResult;
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append("Authenticate Result:");
                    sb10.append(TestTask.this.parseResult(false));
                    sb10.append("\r\n");
                    access$6002.append(sb10.toString());
                    String str6 = TestTask.TAG;
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append("Authenticate Result:");
                    sb11.append(TestTask.this.parseResult(false));
                    Common.LOG_D(str6, sb11.toString());
                    TestTask.this.mGoodixFpManager.remove("Test", 1);
                    synchronized (TestTask.this.mTaskThread) {
                        TestTask.this.mTaskThread.notify();
                    }
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public StringBuffer mAuthenticateResult;
    /* access modifiers changed from: private */
    public StringBuffer mEnrollResult;
    private int mFalseTrigger = 1;
    /* access modifiers changed from: private */
    public GoodixFingerprintManagerExt mGoodixFingerprintManager;
    /* access modifiers changed from: private */
    public ExtGoodixFpManager mGoodixFpManager;
    private HashMap<Integer, Object> mIntResult;
    private int mOtpResult = 1;
    private HashMap<Integer, Object> mRstResult;
    /* access modifiers changed from: private */
    public TaskThread mTaskThread;
    /* access modifiers changed from: private */
    public TestStatusListener mTestStatusListener;
    private Runnable mTimeout = new Runnable() {
        public void run() {
            synchronized (TestTask.this.mTaskThread) {
                TestTask.this.mGoodixFingerprintManager.testCmd(25);
                TestTask.this.mTaskThread.notifyAll();
            }
        }
    };

    public TestTask(Context context) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("TestTask create,thread Id = ");
        sb.append(Thread.currentThread().getId());
        Common.LOG_D(str, sb.toString());
        this.mGoodixFingerprintManager = new GoodixFingerprintManagerExt(context);
        this.mGoodixFingerprintManager.registerTestCmdCallback(this);
        this.mEnrollResult = new StringBuffer();
        this.mAuthenticateResult = new StringBuffer();
        this.mGoodixFpManager = new ExtGoodixFpManager(context);
        this.mGoodixFpManager.setEventListener(this.listener);
        this.mTaskThread = new TaskThread();
    }

    public synchronized void cancel() {
        Common.LOG_D(TAG, "cancel.");
        this.mGoodixFingerprintManager.testCmd(25);
    }

    public static synchronized TestTask getTestTask(Context context) {
        TestTask testTask;
        synchronized (TestTask.class) {
            if (sTestTask == null) {
                synchronized (TestTask.class) {
                    if (sTestTask == null) {
                        sTestTask = new TestTask(context);
                    }
                }
            }
            testTask = sTestTask;
        }
        return testTask;
    }

    public synchronized void setTestStatusListener(TestStatusListener listener2) {
        Common.LOG_D(TAG, "setTestStatusListener");
        this.mTestStatusListener = listener2;
        this.mGoodixFingerprintManager.registerTestCmdCallback(this);
    }

    public synchronized void removeTestStatusListener() {
        Common.LOG_D(TAG, "removeTestStatusListener");
        stopTest();
    }

    private void resumeTask() {
        Common.LOG_D(TAG, "TaskThread.isShutdown ");
        this.mTaskThread.resumeThreadPool();
    }

    public synchronized void startUnTouchTest() {
        Common.LOG_D(TAG, "startUnTouchTest ");
        this.mGoodixFingerprintManager.registerTestCmdCallback(this);
        if (this.mTaskThread.isShutdown()) {
            resumeTask();
        }
        this.mTaskThread.runTestThread(new TaskCommon() {
            public void onStart() {
                Common.LOG_D(TestTask.TAG, "startUnTouchTest onStart ");
                TestTask.this.mGoodixFingerprintManager.startUntouchTest();
            }

            public void onDone() {
                Common.LOG_D(TestTask.TAG, "startUnTouchTest onDone ");
                TestTask.this.stopTask();
            }
        });
    }

    public synchronized void testCmd(int cmd) {
        this.mGoodixFingerprintManager.testCmd(cmd);
    }

    public synchronized void startTouchIntRstTest() {
        Common.LOG_D(TAG, "startTouchIntRstTest ");
        this.mGoodixFingerprintManager.registerTestCmdCallback(this);
        if (this.mTaskThread.isShutdown()) {
            resumeTask();
        }
        this.mTaskThread.runTestThread(new TaskCommon() {
            public void onStart() {
                Common.LOG_D(TestTask.TAG, "startTouchIntRstTest onStart ");
                Common.LOG_D(TestTask.TAG, "startTouchTest CMD_TEST_SENSOR_TOUCH_PREPARE start ");
                TestTask.this.mGoodixFingerprintManager.startTouchPrepareTest();
            }

            public void onDone() {
                Common.LOG_D(TestTask.TAG, "startTouchIntRstTest  done ");
            }
        });
    }

    public synchronized void startTouchTest() {
        Common.LOG_D(TAG, "startTouchTest ");
        this.mGoodixFingerprintManager.registerTestCmdCallback(this);
        if (this.mTaskThread.isShutdown()) {
            resumeTask();
        }
        this.mTaskThread.runTestThread(new TaskCommon() {
            public void onStart() {
                Common.LOG_D(TestTask.TAG, "startTouchTest onStart ");
                TestTask.this.mGoodixFingerprintManager.startTouchTest();
            }

            public void onDone() {
                Common.LOG_D(TestTask.TAG, "startTouchTest onDone ");
            }
        });
    }

    public synchronized void startGetSensorInfoTest() {
        Common.LOG_D(TAG, "startGetSensorInfoTest ");
        this.mGoodixFingerprintManager.registerTestCmdCallback(this);
        if (this.mTaskThread.isShutdown()) {
            resumeTask();
        }
        this.mTaskThread.runTestThread(new TaskCommon() {
            public void onStart() {
                Common.LOG_D(TestTask.TAG, "startGetSensorInfoTest onStart ");
                TestTask.this.mGoodixFingerprintManager.startSensorInfoTest();
                Common.LOG_D(TestTask.TAG, "startGetSensorInfoTest CMD_TEST_GET_SENSOR_INFO ");
            }

            public void onDone() {
                Common.LOG_D(TestTask.TAG, "startGetSensorInfoTest onDone ");
                TestTask.this.stopTask();
            }
        });
    }

    public synchronized void startEnrollTest(final String userId, final int index) {
        Common.LOG_D(TAG, "startEnrollTest ");
        if (this.mTaskThread.isShutdown()) {
            resumeTask();
        }
        this.mTaskThread.runTestThread(new TaskCommon() {
            public void onStart() {
                Common.LOG_D(TestTask.TAG, "enroll onStart ");
                TestTask.this.mEnrollResult.append("Enroll Test start\r\n");
                StringBuffer access$300 = TestTask.this.mEnrollResult;
                StringBuilder sb = new StringBuilder();
                sb.append("Time       : ");
                sb.append(Common.longToString(System.currentTimeMillis(), TestTask.mTimeFormat));
                sb.append("\r\n");
                access$300.append(sb.toString());
                synchronized (TestTask.this.mTaskThread) {
                    try {
                        TestTask.this.mGoodixFpManager.enableMT(true);
                        TestTask.this.mGoodixFpManager.remove("Test", 1);
                        TestTask.this.mGoodixFpManager.enroll(userId, index);
                        TestTask.this.mTaskThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onDone() {
                Common.LOG_D(TestTask.TAG, "enroll end ");
                TestTask.this.mEnrollResult.append("Enroll Test end\r\n");
                TestTask.this.logResult(TestTask.mEnrollResultFile, TestTask.this.mEnrollResult.toString());
                TestTask.this.mEnrollResult.delete(0, TestTask.this.mEnrollResult.length());
                TestTask.this.stopTask();
            }
        });
    }

    public synchronized void startAuthenticateTest(final String userId) {
        Common.LOG_D(TAG, "startAuthenticateTest");
        if (this.mTaskThread.isShutdown()) {
            resumeTask();
        }
        this.mTaskThread.runTestThread(new TaskCommon() {
            public void onStart() {
                Common.LOG_D(TestTask.TAG, "identify onStart ");
                TestTask.this.mAuthenticateResult.append("Authenticate Test start\r\n");
                StringBuffer access$600 = TestTask.this.mAuthenticateResult;
                StringBuilder sb = new StringBuilder();
                sb.append("Time       : ");
                sb.append(Common.longToString(System.currentTimeMillis(), TestTask.mTimeFormat));
                sb.append("\r\n");
                access$600.append(sb.toString());
                synchronized (TestTask.this.mTaskThread) {
                    try {
                        TestTask.this.mGoodixFpManager.enableMT(true);
                        TestTask.this.mGoodixFpManager.identify(userId);
                        TestTask.this.mTaskThread.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void onDone() {
                Common.LOG_D(TestTask.TAG, "identify end ");
                TestTask.this.mAuthenticateResult.append("Authenticate Test end\r\n");
                TestTask.this.logResult(TestTask.mAuthenticateResultFile, TestTask.this.mAuthenticateResult.toString());
                TestTask.this.mAuthenticateResult.delete(0, TestTask.this.mAuthenticateResult.length());
                TestTask.this.stopTask();
            }
        });
    }

    public synchronized void startOtpCheckTest() {
        Common.LOG_D(TAG, "startOtpCheckTest");
        this.mGoodixFingerprintManager.registerTestCmdCallback(this);
        if (this.mTaskThread.isShutdown()) {
            resumeTask();
        }
        this.mTaskThread.runTestThread(new TaskCommon() {
            public void onStart() {
                Common.LOG_D(TestTask.TAG, "startOtpCheckTest onStart");
                TestTask.this.mGoodixFingerprintManager.startOtpCheckTest();
            }

            public void onDone() {
                Common.LOG_D(TestTask.TAG, "startOtpCheckTest onDone");
                TestTask.this.stopTask();
            }
        });
    }

    public synchronized void stopTest() {
        Common.LOG_D(TAG, "stopTest");
        synchronized (this.mTaskThread) {
            this.mTaskThread.notifyAll();
        }
        cancel();
        this.mTaskThread.stopTask();
    }

    /* access modifiers changed from: private */
    public void stopTask() {
        Common.LOG_D(TAG, "stopTask");
        synchronized (this.mTaskThread) {
            this.mTaskThread.notifyAll();
        }
        this.mTaskThread.stopTask();
    }

    private static String toHexString(byte[] byteArray) {
        if (byteArray == null || byteArray.length < 1) {
            throw new IllegalArgumentException("this byteArray must not be null or empty");
        }
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < byteArray.length; i++) {
            if ((byteArray[i] & 255) < 16) {
                hexString.append("0");
            }
            hexString.append(Integer.toHexString(byteArray[i] & 255));
        }
        return hexString.toString().toLowerCase();
    }

    public String bytes2Hex(byte[] bytes) {
        return new BigInteger(1, bytes).toString(16);
    }

    public static String bytes2hex02(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String tmp = Integer.toHexString(255 & b);
            if (tmp.length() == 1) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("0");
                sb2.append(tmp);
                tmp = sb2.toString();
            }
            sb.append(tmp);
        }
        return sb.toString();
    }

    public void onTestCmd(int cmd, HashMap<Integer, Object> result) {
        byte[] productId;
        int i = cmd;
        HashMap<Integer, Object> hashMap = result;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onTestCmd cmd =  ");
        sb.append(i);
        Common.LOG_D(str, sb.toString());
        if (hashMap != null) {
            if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_UNTOUCH_TEST_START))) {
                Common.LOG_D(TAG, "TEST_TOKEN_UNTOUCH_TEST_START");
                if (this.mTestStatusListener != null) {
                    this.mTestStatusListener.onUnTouchTestStatus(917, Integer.valueOf(1));
                } else {
                    Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                }
            } else if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_UNTOUCH_TEST_END))) {
                Common.LOG_D(TAG, "TEST_TOKEN_UNTOUCH_TEST_END");
                if (this.mTestStatusListener != null) {
                    this.mTestStatusListener.onUnTouchTestStatus(918, Integer.valueOf(1));
                } else {
                    Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                }
            } else if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_TOUCH_TEST_START))) {
                Common.LOG_D(TAG, "TEST_TOKEN_TOUCH_TEST_START");
                if (this.mTestStatusListener != null) {
                    this.mTestStatusListener.onTouchTestStatus(919, Integer.valueOf(1));
                } else {
                    Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                }
            } else if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_TOUCH_TEST_END))) {
                Common.LOG_D(TAG, "TEST_TOKEN_TOUCH_TEST_END");
                if (this.mTestStatusListener != null) {
                    this.mTestStatusListener.onTouchTestStatus(920, Integer.valueOf(1));
                    stopTask();
                } else {
                    Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                }
            } else if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_OTPCHECK_TEST_START))) {
                Common.LOG_D(TAG, "TEST_TOKEN_OTPCHECK_TEST_START");
                if (this.mTestStatusListener != null) {
                    this.mTestStatusListener.onOTPCheckTestStatus(921, Integer.valueOf(1));
                } else {
                    Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                }
            } else if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_OTPCHECK_TEST_END))) {
                Common.LOG_D(TAG, "TEST_TOKEN_OTPCHECK_TEST_END");
                if (this.mTestStatusListener != null) {
                    this.mTestStatusListener.onOTPCheckTestStatus(922, Integer.valueOf(1));
                } else {
                    Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                }
            } else if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_GET_SENSOR_INFO_TEST_START))) {
                Common.LOG_D(TAG, "TEST_TOKEN_GET_SENSOR_INFO_TEST_START");
                if (this.mTestStatusListener != null) {
                    this.mTestStatusListener.onGetSensorInfoStatus(923, Integer.valueOf(1));
                } else {
                    Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                }
            } else if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_GET_SENSOR_INFO_TEST_END))) {
                Common.LOG_D(TAG, "TEST_TOKEN_GET_SENSOR_INFO_TEST_END");
                if (this.mTestStatusListener != null) {
                    this.mTestStatusListener.onGetSensorInfoStatus(924, Integer.valueOf(1));
                } else {
                    Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                }
            } else if (i == 8) {
                onTestSpi(hashMap);
            } else if (i == 24) {
                this.mIntResult = hashMap;
            } else if (i == 23) {
                this.mRstResult = hashMap;
            } else if (i == 2) {
                onTestPixel(hashMap);
            } else if (i == 72) {
                if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_PIXEL_SHORT_NUM))) {
                    int pixelShort = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_PIXEL_SHORT_NUM))).intValue();
                    if (this.mTestStatusListener != null) {
                        this.mTestStatusListener.onUnTouchTestStatus(72, Integer.valueOf(pixelShort));
                    } else {
                        Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                    }
                }
            } else if (i == 32) {
                if (hashMap.containsKey(Integer.valueOf(900))) {
                    this.mOtpResult = ((Integer) hashMap.get(Integer.valueOf(900))).intValue();
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("sensorValidity = ");
                    sb2.append(this.mOtpResult);
                    Common.LOG_D(str2, sb2.toString());
                    if (this.mTestStatusListener != null) {
                        this.mTestStatusListener.onUnTouchTestStatus(32, Integer.valueOf(this.mOtpResult));
                    } else {
                        Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                    }
                }
            } else if (i != 63 || !hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_FALSE_TRIGGER_STATUS))) {
                if (i == 60) {
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SENSOR_AVAILABLE_STATUS))) {
                        int sensorAvailable = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SENSOR_AVAILABLE_STATUS))).intValue();
                        String str3 = TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("sensorAvailable = ");
                        sb3.append(sensorAvailable);
                        Common.LOG_D(str3, sb3.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(933, Integer.valueOf(sensorAvailable));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    int isPrepare = -1;
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_IS_TOUCH_TEST_PREPARE))) {
                        isPrepare = ((Short) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_IS_TOUCH_TEST_PREPARE))).shortValue();
                    }
                    String str4 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("isPrepare = ");
                    sb4.append(isPrepare);
                    Common.LOG_D(str4, sb4.toString());
                    if (isPrepare == 1) {
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(902, parseResult(true));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                        return;
                    }
                    if (this.mTestStatusListener != null) {
                        this.mTestStatusListener.onTouchTestStatus(903, parseResult(false));
                    } else {
                        Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                    }
                } else if (i == 59) {
                    Common.LOG_D(TAG, "CMD_TEST_SENSOR_TOUCH   ");
                    if (this.mIntResult != null) {
                        onTestInt(this.mIntResult);
                    }
                    if (this.mRstResult != null) {
                        onTestReset(this.mRstResult);
                    }
                    int sensorAvailable2 = 1;
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SENSOR_AVAILABLE_STATUS))) {
                        sensorAvailable2 = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SENSOR_AVAILABLE_STATUS))).intValue();
                        String str5 = TAG;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append("sensorAvailable = ");
                        sb5.append(sensorAvailable2);
                        Common.LOG_D(str5, sb5.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(933, Integer.valueOf(sensorAvailable2));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    if (this.mTestStatusListener != null) {
                        this.mTestStatusListener.onTouchTestStatus(32, Integer.valueOf(this.mOtpResult));
                    } else {
                        Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                    }
                    int errorCode = 1000;
                    if (hashMap.containsKey(Integer.valueOf(100))) {
                        errorCode = ((Integer) hashMap.get(Integer.valueOf(100))).intValue();
                        String str6 = TAG;
                        StringBuilder sb6 = new StringBuilder();
                        sb6.append("errorCode = ");
                        sb6.append(errorCode);
                        Common.LOG_D(str6, sb6.toString());
                    }
                    if (hashMap.containsKey(Integer.valueOf(301))) {
                        float noise = ((Float) hashMap.get(Integer.valueOf(301))).floatValue();
                        String str7 = TAG;
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("noise = ");
                        sb7.append(noise);
                        Common.LOG_D(str7, sb7.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(904, Float.valueOf(noise));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    if (hashMap.containsKey(Integer.valueOf(303))) {
                        float snr = ((Float) hashMap.get(Integer.valueOf(303))).floatValue();
                        String str8 = TAG;
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("snr = ");
                        sb8.append(snr);
                        Common.LOG_D(str8, sb8.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(905, Float.valueOf(snr));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    if (hashMap.containsKey(Integer.valueOf(305))) {
                        int totalPixelNum = ((Integer) hashMap.get(Integer.valueOf(305))).intValue();
                        String str9 = TAG;
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("totalPixelNum = ");
                        sb9.append(totalPixelNum);
                        Common.LOG_D(str9, sb9.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(906, Integer.valueOf(totalPixelNum));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_LOCAL_BAD_PIXEL_NUM))) {
                        int localPixelNum = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_LOCAL_BAD_PIXEL_NUM))).intValue();
                        String str10 = TAG;
                        StringBuilder sb10 = new StringBuilder();
                        sb10.append("localPixelNum = ");
                        sb10.append(localPixelNum);
                        Common.LOG_D(str10, sb10.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(907, Integer.valueOf(localPixelNum));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_DATA_DIFFERENCE))) {
                        int dataDifference = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_DATA_DIFFERENCE))).intValue();
                        String str11 = TAG;
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append("dataDifference = ");
                        sb11.append(dataDifference);
                        Common.LOG_D(str11, sb11.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(908, Integer.valueOf(dataDifference));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SNR_TEST_IS_TOUCH_STABLE))) {
                        float rubberPlacement = ((Float) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SNR_TEST_IS_TOUCH_STABLE))).floatValue();
                        String str12 = TAG;
                        StringBuilder sb12 = new StringBuilder();
                        sb12.append("rubberPlacement = ");
                        sb12.append(rubberPlacement);
                        Common.LOG_D(str12, sb12.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(912, Float.valueOf(rubberPlacement));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SENSOR_PERFORMANCE))) {
                        int performance = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SENSOR_PERFORMANCE))).intValue();
                        String str13 = TAG;
                        StringBuilder sb13 = new StringBuilder();
                        sb13.append("performance = ");
                        sb13.append(performance);
                        Common.LOG_D(str13, sb13.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(913, Integer.valueOf(performance));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_IS_PIXEL_DOWN_PASS))) {
                        int pixelDown = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_IS_PIXEL_DOWN_PASS))).intValue();
                        String str14 = TAG;
                        StringBuilder sb14 = new StringBuilder();
                        sb14.append("pixelDown = ");
                        sb14.append(pixelDown);
                        Common.LOG_D(str14, sb14.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(909, Integer.valueOf(pixelDown));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_IS_RECV_FALSE_IRQ))) {
                        int falseIrq = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_IS_RECV_FALSE_IRQ))).intValue();
                        String str15 = TAG;
                        StringBuilder sb15 = new StringBuilder();
                        int i2 = errorCode;
                        sb15.append("falseIrq = ");
                        sb15.append(falseIrq);
                        Common.LOG_D(str15, sb15.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(911, Integer.valueOf(falseIrq));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    int saturateMin = 0;
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_DOWN_VALUE))) {
                        saturateMin = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_DOWN_VALUE))).intValue();
                        String str16 = TAG;
                        StringBuilder sb16 = new StringBuilder();
                        int i3 = sensorAvailable2;
                        sb16.append("saturateMin = ");
                        sb16.append(saturateMin);
                        Common.LOG_D(str16, sb16.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(931, Integer.valueOf(saturateMin));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    int saturateMax = 0;
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_UP_VALUE))) {
                        saturateMax = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_UP_VALUE))).intValue();
                        String str17 = TAG;
                        StringBuilder sb17 = new StringBuilder();
                        int i4 = saturateMin;
                        sb17.append("saturateMax = ");
                        sb17.append(saturateMax);
                        Common.LOG_D(str17, sb17.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(930, Integer.valueOf(saturateMax));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    this.mIntResult = null;
                    this.mRstResult = null;
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_IS_PIXEL_UP_PASS))) {
                        int pixelUp = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_IS_PIXEL_UP_PASS))).intValue();
                        String str18 = TAG;
                        StringBuilder sb18 = new StringBuilder();
                        int i5 = saturateMax;
                        sb18.append("pixelUp = ");
                        sb18.append(pixelUp);
                        Common.LOG_D(str18, sb18.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onTouchTestStatus(910, Integer.valueOf(pixelUp));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                } else if (i == 61) {
                    int sensorAvailable3 = 1;
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SENSOR_AVAILABLE_STATUS))) {
                        sensorAvailable3 = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SENSOR_AVAILABLE_STATUS))).intValue();
                        String str19 = TAG;
                        StringBuilder sb19 = new StringBuilder();
                        sb19.append("sensorAvailable = ");
                        sb19.append(sensorAvailable3);
                        Common.LOG_D(str19, sb19.toString());
                    }
                    if (this.mTestStatusListener != null) {
                        this.mTestStatusListener.onGetSensorInfoStatus(933, Integer.valueOf(sensorAvailable3));
                    } else {
                        Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                    }
                    byte[] bArr = new byte[0];
                    if (hashMap.containsKey(Integer.valueOf(207))) {
                        String sensorIdStr = toHexString((byte[]) hashMap.get(Integer.valueOf(207)));
                        String str20 = TAG;
                        StringBuilder sb20 = new StringBuilder();
                        sb20.append("sensorId = ");
                        sb20.append(sensorIdStr);
                        Common.LOG_D(str20, sb20.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onGetSensorInfoStatus(928, sensorIdStr);
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    byte[] bArr2 = new byte[0];
                    if (hashMap.containsKey(Integer.valueOf(211))) {
                        String productIdStr = "";
                        for (byte ch : (byte[]) hashMap.get(Integer.valueOf(211))) {
                            StringBuilder sb21 = new StringBuilder();
                            sb21.append(productIdStr);
                            sb21.append((char) ch);
                            productIdStr = sb21.toString();
                        }
                        String str21 = TAG;
                        StringBuilder sb22 = new StringBuilder();
                        sb22.append("productId = ");
                        sb22.append(productIdStr);
                        Common.LOG_D(str21, sb22.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onGetSensorInfoStatus(927, productIdStr);
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_IS_MODULE_TEST_PASS))) {
                        int isModuleTestPass = ((Short) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_IS_MODULE_TEST_PASS))).shortValue();
                        String str22 = TAG;
                        StringBuilder sb23 = new StringBuilder();
                        sb23.append("isModuleTestPass = ");
                        sb23.append(isModuleTestPass);
                        Common.LOG_D(str22, sb23.toString());
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onGetSensorInfoStatus(914, Integer.valueOf(isModuleTestPass));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                    if (hashMap.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_CALIBRATION_STATUS))) {
                        int calibration = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_CALIBRATION_STATUS))).intValue();
                        if (this.mTestStatusListener != null) {
                            this.mTestStatusListener.onGetSensorInfoStatus(929, Integer.valueOf(calibration));
                        } else {
                            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                        }
                    }
                }
            } else {
                this.mFalseTrigger = ((Integer) hashMap.get(Integer.valueOf(TestResultParser.TEST_TOKEN_FALSE_TRIGGER_STATUS))).intValue();
                String str23 = TAG;
                StringBuilder sb24 = new StringBuilder();
                sb24.append("falseTrigger = ");
                sb24.append(this.mFalseTrigger);
                Common.LOG_D(str23, sb24.toString());
                if (this.mTestStatusListener != null) {
                    this.mTestStatusListener.onTouchTestStatus(932, Integer.valueOf(this.mFalseTrigger));
                } else {
                    Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
                }
            }
        }
    }

    private void onTestSpi(HashMap<Integer, Object> result) {
        int errorCode = 1000;
        int chipID = 0;
        if (result.containsKey(Integer.valueOf(100))) {
            errorCode = ((Integer) result.get(Integer.valueOf(100))).intValue();
        }
        int sensorAvailable = 1;
        if (result.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SENSOR_AVAILABLE_STATUS))) {
            sensorAvailable = ((Integer) result.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SENSOR_AVAILABLE_STATUS))).intValue();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("sensorAvailable = ");
            sb.append(sensorAvailable);
            Common.LOG_D(str, sb.toString());
        }
        if (this.mTestStatusListener != null) {
            this.mTestStatusListener.onUnTouchTestStatus(933, Integer.valueOf(sensorAvailable));
        } else {
            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
        }
        if (result.containsKey(Integer.valueOf(205))) {
            byte[] chip = (byte[]) result.get(Integer.valueOf(205));
            if (chip != null && chip.length >= 4) {
                chipID = TestResultParser.decodeInt32(chip, 0) >> 8;
            }
        }
        String str2 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("onTestSpi errorCode = ");
        sb2.append(errorCode);
        sb2.append(" , chipID = 0x");
        sb2.append(Integer.toHexString(chipID));
        Common.LOG_D(str2, sb2.toString());
        int spiPass = 1;
        if (result.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_SPI_PASS_STATUS))) {
            spiPass = ((Integer) result.get(Integer.valueOf(TestResultParser.TEST_TOKEN_SPI_PASS_STATUS))).intValue();
        }
        if (this.mTestStatusListener != null) {
            this.mTestStatusListener.onUnTouchTestStatus(8, Integer.valueOf(spiPass));
        } else {
            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
        }
    }

    private void onTestInt(HashMap<Integer, Object> result) {
        int errorCode = 1000;
        if (result.containsKey(Integer.valueOf(100))) {
            errorCode = ((Integer) result.get(Integer.valueOf(100))).intValue();
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onTestInt errorCode = ");
        sb.append(errorCode);
        Common.LOG_D(str, sb.toString());
        int intPass = 1;
        if (result.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_INT_PASS_STATUS))) {
            intPass = ((Integer) result.get(Integer.valueOf(TestResultParser.TEST_TOKEN_INT_PASS_STATUS))).intValue();
        }
        if (this.mTestStatusListener != null) {
            this.mTestStatusListener.onTouchTestStatus(24, Integer.valueOf(intPass));
        } else {
            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
        }
    }

    private void onTestReset(HashMap<Integer, Object> result) {
        int errorCode = 1000;
        if (result.containsKey(Integer.valueOf(100))) {
            errorCode = ((Integer) result.get(Integer.valueOf(100))).intValue();
        }
        int resetFlag = 0;
        if (result.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_RESET_FLAG))) {
            resetFlag = ((Integer) result.get(Integer.valueOf(TestResultParser.TEST_TOKEN_RESET_FLAG))).intValue();
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onTestReset errorCode = ");
        sb.append(errorCode);
        sb.append(" , resetFlag =");
        sb.append(resetFlag);
        Common.LOG_D(str, sb.toString());
        int rstPass = 1;
        if (result.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_RESET_PASS_STATUS))) {
            rstPass = ((Integer) result.get(Integer.valueOf(TestResultParser.TEST_TOKEN_RESET_PASS_STATUS))).intValue();
        }
        if (this.mTestStatusListener != null) {
            this.mTestStatusListener.onTouchTestStatus(23, Integer.valueOf(rstPass));
        } else {
            Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
        }
    }

    private void onTestPixel(HashMap<Integer, Object> result) {
        int errorCode = 1000;
        if (result.containsKey(Integer.valueOf(100))) {
            errorCode = ((Integer) result.get(Integer.valueOf(100))).intValue();
        }
        int badPixelNum = 0;
        if (result.containsKey(Integer.valueOf(305))) {
            badPixelNum = ((Integer) result.get(Integer.valueOf(305))).intValue();
        }
        int localBadPixelNum = 0;
        if (result.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_LOCAL_BAD_PIXEL_NUM))) {
            localBadPixelNum = ((Integer) result.get(Integer.valueOf(TestResultParser.TEST_TOKEN_LOCAL_BAD_PIXEL_NUM))).intValue();
        }
        int fdtZone8x1 = 0;
        if (result.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_FDT_USED_OPEN_PIXEL_NUM))) {
            fdtZone8x1 = ((Integer) result.get(Integer.valueOf(TestResultParser.TEST_TOKEN_FDT_USED_OPEN_PIXEL_NUM))).intValue();
        }
        int fdtZone8x7 = 0;
        if (result.containsKey(Integer.valueOf(TestResultParser.TEST_TOKEN_FDT_EXTRA_OPEN_PIXEL_NUM))) {
            fdtZone8x7 = ((Integer) result.get(Integer.valueOf(TestResultParser.TEST_TOKEN_FDT_EXTRA_OPEN_PIXEL_NUM))).intValue();
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onTestPixel errorCode = ");
        sb.append(errorCode);
        sb.append(" , badPixelNum =");
        sb.append(badPixelNum);
        sb.append(", localBadPixelNum = ");
        sb.append(localBadPixelNum);
        sb.append(", fdtZone8x1 = ");
        sb.append(fdtZone8x1);
        sb.append(", fdtZone8x7 = ");
        sb.append(fdtZone8x7);
        Common.LOG_D(str, sb.toString());
        if (this.mTestStatusListener != null) {
            this.mTestStatusListener.onUnTouchTestStatus(900, Integer.valueOf(badPixelNum));
            this.mTestStatusListener.onUnTouchTestStatus(901, Integer.valueOf(localBadPixelNum));
            this.mTestStatusListener.onUnTouchTestStatus(925, Integer.valueOf(fdtZone8x1));
            this.mTestStatusListener.onUnTouchTestStatus(926, Integer.valueOf(fdtZone8x7));
            return;
        }
        Common.LOG_D(TAG, "mTestStatusListener is not initialized.");
    }

    /* access modifiers changed from: private */
    public void logResult(String file, String content) {
        boolean out = null;
        try {
            File parentDir = new File(mResultBaseFile);
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }
            File logFile = new File(file);
            out = logFile.exists();
            if (out) {
                logFile.delete();
            }
            if (logFile.createNewFile()) {
                Common.LOG_D(TAG, "Test log file created.");
            }
            BufferedWriter out2 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true)));
            out2.newLine();
            out2.write(content);
            try {
                out2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            if (out != null) {
                out.close();
            }
        } catch (IOException e3) {
            e3.printStackTrace();
            if (out != null) {
                out.close();
            }
        } catch (Exception e4) {
            e4.printStackTrace();
            if (out != null) {
                out.close();
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public Integer parseResult(boolean result) {
        int i = 1;
        if (result) {
            i = 0;
        }
        return Integer.valueOf(i);
    }

    public synchronized String getSoftwareVersion() {
        return this.mGoodixFingerprintManager.startGetVersion();
    }
}
