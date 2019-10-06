package com.goodix.cap.fingerprint.service;

import android.graphics.Bitmap;

public interface GoodixFingerprintCallbackExt {
    void onAcquired(int i);

    void onAuthenticateFido(int i, byte[] bArr);

    void onAuthenticationFailed();

    void onAuthenticationSucceeded(int i);

    void onCaptureInfo(long j, Bitmap bitmap, byte[] bArr, int i, int i2, Bitmap bitmap2, byte[] bArr2, int i3, int i4);

    void onEnrollResult(int i, int i2);

    void onEnrollSecResult(int i, int i2, int i3, int i4, Bitmap bitmap, int i5);

    void onError(int i);

    void onEventInfo(int i, byte[] bArr);

    void onHbdData(int i, int i2, int[] iArr, int[] iArr2);

    void onRemoved(int i);

    void onTestCmd(int i, byte[] bArr);
}
