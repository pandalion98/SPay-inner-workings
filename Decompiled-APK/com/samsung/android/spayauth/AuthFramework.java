package com.samsung.android.spayauth;

import android.content.Context;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytui.SpayTuiTAController;
import com.samsung.android.spaytui.SpayTuiTAInfo;
import java.util.Arrays;
import org.bouncycastle.crypto.macs.SkeinMac;

/* renamed from: com.samsung.android.spayauth.a */
public class AuthFramework {
    private static AuthFramework iA;
    private static SpayTuiTAController iC;
    private int iB;
    private Context mContext;

    static {
        iA = null;
    }

    public AuthFramework(Context context) {
        this.mContext = null;
        this.iB = SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        this.mContext = context;
        try {
            iC = SpayTuiTAController.createOnlyInstance(this.mContext);
            this.iB = 0;
        } catch (Throwable e) {
            if (e.getErrorCode() == 2) {
                Log.m285d("SPAY:AuthFramework", "ERR_GENERIC_INTEGRITY_FAIL");
                this.iB = SkeinMac.SKEIN_256;
            } else {
                Log.m285d("SPAY:AuthFramework", "ERR_GENERIC_INTEGRITY_VERIFYERROR");
                this.iB = SpayTuiTAInfo.SPAY_TA_TYPE_TEE_TUI;
            }
            Log.m284c("SPAY:AuthFramework", e.getMessage(), e);
        }
    }

    public static AuthFramework m232b(Context context) {
        if (iA == null) {
            iA = new AuthFramework(context);
        }
        return iA;
    }

    public int m233N() {
        Log.m285d("SPAY:AuthFramework", "tppLoad(): ");
        int i = SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        if (iC.loadTA()) {
            return iC.pinExist() ? 458753 : 0;
        } else {
            if (iC.isResetPeding()) {
                i = 1;
            }
            Log.m286e("SPAY:AuthFramework", "Failure Load TPP: " + i);
            return i;
        }
    }

    public int m234O() {
        Log.m285d("SPAY:AuthFramework", "tppUnLoad(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        iC.unloadTA();
        return 0;
    }

    public int m249a(String[] strArr) {
        Log.m285d("SPAY:AuthFramework", "tppSetSecureModeText(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        if (strArr != null) {
            return iC.setSecureModeText(strArr);
        }
        Log.m286e("SPAY:AuthFramework", "SecureModeText: Null input");
        return 4;
    }

    public int m254b(String[] strArr) {
        Log.m285d("SPAY:AuthFramework", "tppSetActionBarText(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        if (strArr == null) {
            Log.m286e("SPAY:AuthFramework", "ActionBarText: Null input");
            return 4;
        }
        boolean isRTL = Utils.isRTL();
        boolean aq = Utils.aq(this.mContext);
        Log.m285d("SPAY:AuthFramework", "isRTL()=" + isRTL + " isForceRTL()=" + aq);
        isRTL = isRTL || aq;
        int b = m251b(isRTL);
        Log.m285d("SPAY:AuthFramework", "tppSetRtl=" + b);
        if (b == 0) {
            return iC.setActionBarText(strArr);
        }
        return b;
    }

    public int m248a(byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, int i5) {
        Log.m285d("SPAY:AuthFramework", "tppSetPinBox(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.setPinBox(bArr, bArr2, i, i2, i3, i4, i5);
    }

    public int m244a(byte[] bArr, int i, int i2) {
        Log.m285d("SPAY:AuthFramework", "tppSetPrompt(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.setPrompt(bArr, i, i2);
    }

    public int m252b(byte[] bArr, int i, int i2) {
        Log.m285d("SPAY:AuthFramework", "tppSetBkImg(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.setBackgroundImg(bArr, i, i2);
    }

    public int m247a(byte[] bArr, byte[] bArr2, int i, int i2) {
        Log.m285d("SPAY:AuthFramework", "tppSetCancelBtnImg(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.setCancelBtnImg(bArr, bArr2, i, i2);
    }

    public int m253b(byte[] bArr, byte[] bArr2) {
        Log.m285d("SPAY:AuthFramework", "tppSetupBIO(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.setupBio(bArr, bArr2);
    }

    public int m255c(byte[] bArr) {
        Log.m285d("SPAY:AuthFramework", "tppSetupPIN(so): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.setupPin(bArr);
    }

    public int m243a(boolean z, boolean z2) {
        Log.m285d("SPAY:AuthFramework", "tppVerifyPIN(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.verify(z, z2);
    }

    public int m242a(boolean z) {
        Log.m285d("SPAY:AuthFramework", "tppResume(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.resume(z);
    }

    public byte[] m250a(String str, byte[] bArr) {
        Log.m285d("SPAY:AuthFramework", "tppGetSecureResult(): ");
        Log.m285d("SPAY:AuthFramework", "taName = " + str + " nonce = " + Arrays.toString(bArr));
        if (this.iB == 0 && iC != null) {
            return iC.getSecureResult(bArr, str);
        }
        return null;
    }

    public int m256d(byte[] bArr) {
        Log.m285d("SPAY:AuthFramework", "tppPreLoadFpSecureResult(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.preloadFpSecureResult(bArr);
    }

    public int m235P() {
        if (iC.deletePin()) {
            return 0;
        }
        return 458754;
    }

    public int m236Q() {
        Log.m285d("SPAY:AuthFramework", "tppCheckTuiSession(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.checkTuiSession();
    }

    public byte[] m237R() {
        Log.m285d("SPAY:AuthFramework", "tppGetNonce(): ");
        if (this.iB == 0 && iC != null) {
            return iC.getNonce(32);
        }
        return null;
    }

    public int m238S() {
        Log.m285d("SPAY:AuthFramework", "tppClearState");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.clearTuiState();
    }

    public int m257e(byte[] bArr) {
        Log.m285d("SPAY:AuthFramework", "tppUpdateFP");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.updateFP(bArr);
    }

    public int m258f(byte[] bArr) {
        Log.m285d("SPAY:AuthFramework", "tppUpdateFP");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.updateFP(bArr);
    }

    public int m241a(String str, String str2, String str3, String str4, String str5, String str6, byte[] bArr) {
        Log.m285d("SPAY:AuthFramework", "tppInAppConfirm");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        return iC.inAppConfirm(str, str2, str3, str4, str5, str6, bArr);
    }

    public int m246a(byte[] bArr, int i, int i2, int[] iArr) {
        Log.m285d("SPAY:AuthFramework", "tppMerchantSecureDisplay");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        int merchantSecureDisplay = iC.merchantSecureDisplay(bArr, i, i2, iArr);
        Log.m285d("SPAY:AuthFramework", "tppMerchantSecureDisplay ret[1]=" + merchantSecureDisplay);
        return merchantSecureDisplay;
    }

    public int[] m239T() {
        Log.m285d("SPAY:AuthFramework", "tppMerchantSecureTouch");
        int[] iArr = new int[]{0, 0};
        if (this.iB != 0) {
            iArr[0] = this.iB;
            return iArr;
        } else if (iC == null) {
            iArr[0] = SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
            return iArr;
        } else {
            iArr = iC.merchantSecureTouch();
            Log.m285d("SPAY:AuthFramework", "tppMerchantSecureTouch ret[1]=" + iArr[1]);
            return iArr;
        }
    }

    public int m251b(boolean z) {
        Log.m285d("SPAY:AuthFramework", "tppSetRtl");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        int rtl = iC.setRtl(z);
        Log.m285d("SPAY:AuthFramework", "tppSetRtl ret=" + rtl);
        return rtl;
    }

    public int m240a(int i, int[] iArr) {
        Log.m285d("SPAY:AuthFramework", "tppMerchantSecureInit");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        int merchantSecureInit = iC.merchantSecureInit(i, iArr);
        Log.m285d("SPAY:AuthFramework", "tppMerchantSecureDisplay ret=" + merchantSecureInit);
        return merchantSecureInit;
    }

    public int m245a(byte[] bArr, int i, int i2, int i3, int i4) {
        Log.m285d("SPAY:AuthFramework", "tppMerchantSendSecureImg");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return SpayTuiTAController.SPAY_TPP_ERROR_GENERIC;
        }
        int merchantSendSecureImg = iC.merchantSendSecureImg(bArr, i, i2, i3, i4);
        Log.m285d("SPAY:AuthFramework", "tppMerchantSendSecureImg ret=" + merchantSendSecureImg);
        return merchantSendSecureImg;
    }
}
