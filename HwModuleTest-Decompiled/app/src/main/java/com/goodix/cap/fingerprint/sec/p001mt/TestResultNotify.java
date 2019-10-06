package com.goodix.cap.fingerprint.sec.p001mt;

/* renamed from: com.goodix.cap.fingerprint.sec.mt.TestResultNotify */
public interface TestResultNotify {
    void onCaptureFingerLeaved(int i, Object obj);

    void onCaptureNotify(int i, Object obj);

    void onEnrollStatus(int i, Object obj);

    void onGetSensorInfoNotify(int i, Object obj);

    void onIdentifyStatus(int i, Object obj);

    void onOTPCheckNotify(int i, Object obj);

    void onTouchResultNotify(int i, Object obj);

    void onUnTouchResultNotify(int i, Object obj);
}
