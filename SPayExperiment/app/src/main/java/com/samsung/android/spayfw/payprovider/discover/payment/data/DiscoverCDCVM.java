/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.discover.payment.data;

import android.text.TextUtils;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.discover.payment.utils.ByteBuffer;

public class DiscoverCDCVM {
    private final DiscoverCDCVMType tH;
    private final long tI;

    public DiscoverCDCVM(DiscoverCDCVMType discoverCDCVMType, long l2) {
        long l3 = 0L;
        this.tH = discoverCDCVMType;
        if (l2 == l3) {
            l3 = 1L;
        }
        this.tI = l3;
    }

    public static DiscoverCDCVMType aG(String string) {
        if (string == null || TextUtils.isEmpty((CharSequence)string)) {
            Log.d("DCSDK_DiscoverCDCVM", "getCDCVMTypeByFWAuthType, PF auth type is empty, return CVM_TYPE_NO_VALIDATION");
            return DiscoverCDCVMType.tJ;
        }
        if ("BACKUP PASSWORD".equals((Object)string)) {
            Log.d("DCSDK_DiscoverCDCVM", "getCDCVMTypeByFWAuthType, PF auth type is AUTHTYPE_BACKUPPASSWORD, return CVM_TYPE_PASSWORD");
            return DiscoverCDCVMType.tK;
        }
        if ("FP".equals((Object)string)) {
            Log.d("DCSDK_DiscoverCDCVM", "getCDCVMTypeByFWAuthType, PF auth type is AUTHTYPE_FP, return CVM_TYPE_FINGERPRINT");
            return DiscoverCDCVMType.tL;
        }
        if ("PIN".equals((Object)string)) {
            Log.d("DCSDK_DiscoverCDCVM", "getCDCVMTypeByFWAuthType, PF auth type is AUTHTYPE_TRUSTED_PIN, return CVM_TYPE_LOCAL_PASSCODE");
            return DiscoverCDCVMType.tQ;
        }
        if ("IRIS".equals((Object)string)) {
            Log.d("DCSDK_DiscoverCDCVM", "getCDCVMTypeByFWAuthType, PF auth type is AUTHTYPE_IRIS, return CVM_TYPE_RETINA_SCAN");
            return DiscoverCDCVMType.tO;
        }
        return DiscoverCDCVMType.tJ;
    }

    public DiscoverCDCVMType dE() {
        return this.tH;
    }

    public long dF() {
        return this.tI;
    }

    public static final class DiscoverCDCVMType
    extends Enum<DiscoverCDCVMType> {
        public static final /* enum */ DiscoverCDCVMType tJ = new DiscoverCDCVMType();
        public static final /* enum */ DiscoverCDCVMType tK = new DiscoverCDCVMType();
        public static final /* enum */ DiscoverCDCVMType tL = new DiscoverCDCVMType();
        public static final /* enum */ DiscoverCDCVMType tM = new DiscoverCDCVMType();
        public static final /* enum */ DiscoverCDCVMType tN = new DiscoverCDCVMType();
        public static final /* enum */ DiscoverCDCVMType tO = new DiscoverCDCVMType();
        public static final /* enum */ DiscoverCDCVMType tP = new DiscoverCDCVMType();
        public static final /* enum */ DiscoverCDCVMType tQ = new DiscoverCDCVMType();
        public static final /* enum */ DiscoverCDCVMType tR = new DiscoverCDCVMType();
        private static final /* synthetic */ DiscoverCDCVMType[] tS;

        static {
            DiscoverCDCVMType[] arrdiscoverCDCVMType = new DiscoverCDCVMType[]{tJ, tK, tL, tM, tN, tO, tP, tQ, tR};
            tS = arrdiscoverCDCVMType;
        }

        public static DiscoverCDCVMType valueOf(String string) {
            return (DiscoverCDCVMType)Enum.valueOf(DiscoverCDCVMType.class, (String)string);
        }

        public static DiscoverCDCVMType[] values() {
            return (DiscoverCDCVMType[])tS.clone();
        }

        public ByteBuffer dG() {
            ByteBuffer byteBuffer = new ByteBuffer(1);
            byteBuffer.setByte(0, (byte)this.ordinal());
            return byteBuffer;
        }
    }

}

