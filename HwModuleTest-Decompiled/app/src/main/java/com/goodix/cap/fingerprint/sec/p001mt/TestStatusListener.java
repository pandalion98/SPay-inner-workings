package com.goodix.cap.fingerprint.sec.p001mt;

/* renamed from: com.goodix.cap.fingerprint.sec.mt.TestStatusListener */
public interface TestStatusListener {
    void onAuthenticateStatus(int i, Object obj);

    void onCaptureFingerLeaved(int i, Object obj);

    void onCaptureStatus(int i, Object obj);

    void onEnrollTestStatus(int i, Object obj);

    void onGetSensorInfoStatus(int i, Object obj);

    void onOTPCheckTestStatus(int i, Object obj);

    void onTouchTestStatus(int i, Object obj);

    void onUnTouchTestStatus(int i, Object obj);
}
