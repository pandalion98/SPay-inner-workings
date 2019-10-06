package egistec.optical.csa.client.api;

import egistec.optical.csa.client.api.Fingerprint.FingerprintEventListener;
import java.io.UnsupportedEncodingException;

public interface IFingerprint {
    void calibration();

    int cancel();

    int cleanup();

    int eepromTest(int i, int i2, int i3);

    int enableSensorDevice(boolean z);

    int enroll(String str, int i);

    int getEnrollRepeatCount();

    byte[] getFingerprintId(String str, int i);

    int[] getFingerprintIndexList(String str);

    String getSensorInfo();

    int getSensorStatus();

    String[] getUserIdList();

    String getVersion();

    int identify(String str);

    int remove(String str, int i);

    int request(int i, Object obj);

    int setAccuracyLevel(int i);

    int setEnrollSession(boolean z);

    void setEventListener(FingerprintEventListener fingerprintEventListener);

    void setForCaptureFrame(int i, int i2, int i3);

    int setPassword(String str, byte[] bArr) throws UnsupportedEncodingException;

    int swipeEnroll(String str, int i);

    void triggerTouchEvent(int i);

    int verifyPassword(String str, byte[] bArr) throws UnsupportedEncodingException;

    int verifySensorState(int i, int i2, int i3, int i4, int i5);
}
