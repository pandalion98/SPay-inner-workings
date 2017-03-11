package com.samsung.android.spayfw.payprovider.discover.payment.data;

import android.text.TextUtils;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class DiscoverCDCVM {
    private final DiscoverCDCVMType tH;
    private final long tI;

    public enum DiscoverCDCVMType {
        CVM_TYPE_NO_VALIDATION,
        CVM_TYPE_PASSWORD,
        CVM_TYPE_FINGERPRINT,
        CVM_TYPE_PATTERN,
        CVM_TYPE_CRYPTO_PIN,
        CVM_TYPE_RETINA_SCAN,
        CVM_TYPE_FACIAL_RECOGNITION,
        CVM_TYPE_LOCAL_PASSCODE,
        CVM_TYPE_OTHER_BIOMETRIC;

        public ByteBuffer dG() {
            ByteBuffer byteBuffer = new ByteBuffer(1);
            byteBuffer.setByte(0, (byte) ordinal());
            return byteBuffer;
        }
    }

    public DiscoverCDCVM(DiscoverCDCVMType discoverCDCVMType, long j) {
        long j2 = 0;
        this.tH = discoverCDCVMType;
        if (j == 0) {
            j2 = 1;
        }
        this.tI = j2;
    }

    public DiscoverCDCVMType dE() {
        return this.tH;
    }

    public long dF() {
        return this.tI;
    }

    public static DiscoverCDCVMType aG(String str) {
        if (str == null || TextUtils.isEmpty(str)) {
            Log.m285d("DCSDK_DiscoverCDCVM", "getCDCVMTypeByFWAuthType, PF auth type is empty, return CVM_TYPE_NO_VALIDATION");
            return DiscoverCDCVMType.CVM_TYPE_NO_VALIDATION;
        } else if (PaymentNetworkProvider.AUTHTYPE_BACKUPPASSWORD.equals(str)) {
            Log.m285d("DCSDK_DiscoverCDCVM", "getCDCVMTypeByFWAuthType, PF auth type is AUTHTYPE_BACKUPPASSWORD, return CVM_TYPE_PASSWORD");
            return DiscoverCDCVMType.CVM_TYPE_PASSWORD;
        } else if (PaymentNetworkProvider.AUTHTYPE_FP.equals(str)) {
            Log.m285d("DCSDK_DiscoverCDCVM", "getCDCVMTypeByFWAuthType, PF auth type is AUTHTYPE_FP, return CVM_TYPE_FINGERPRINT");
            return DiscoverCDCVMType.CVM_TYPE_FINGERPRINT;
        } else if (PaymentNetworkProvider.AUTHTYPE_TRUSTED_PIN.equals(str)) {
            Log.m285d("DCSDK_DiscoverCDCVM", "getCDCVMTypeByFWAuthType, PF auth type is AUTHTYPE_TRUSTED_PIN, return CVM_TYPE_LOCAL_PASSCODE");
            return DiscoverCDCVMType.CVM_TYPE_LOCAL_PASSCODE;
        } else if (!PaymentNetworkProvider.AUTHTYPE_IRIS.equals(str)) {
            return DiscoverCDCVMType.CVM_TYPE_NO_VALIDATION;
        } else {
            Log.m285d("DCSDK_DiscoverCDCVM", "getCDCVMTypeByFWAuthType, PF auth type is AUTHTYPE_IRIS, return CVM_TYPE_RETINA_SCAN");
            return DiscoverCDCVMType.CVM_TYPE_RETINA_SCAN;
        }
    }
}
