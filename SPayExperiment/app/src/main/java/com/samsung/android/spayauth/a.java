/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.Arrays
 */
package com.samsung.android.spayauth;

import android.content.Context;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.utils.h;
import com.samsung.android.spaytui.SpayTuiTAController;
import com.samsung.android.spaytzsvc.api.TAException;
import java.util.Arrays;

public class a {
    private static a iA = null;
    private static SpayTuiTAController iC;
    private int iB = 401408;
    private Context mContext = null;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public a(Context context) {
        this.mContext = context;
        try {
            iC = SpayTuiTAController.createOnlyInstance(this.mContext);
            this.iB = 0;
            return;
        }
        catch (TAException tAException) {
            if (tAException.getErrorCode() == 2) {
                Log.d("SPAY:AuthFramework", "ERR_GENERIC_INTEGRITY_FAIL");
                this.iB = 256;
            } else {
                Log.d("SPAY:AuthFramework", "ERR_GENERIC_INTEGRITY_VERIFYERROR");
                this.iB = 257;
            }
            Log.c("SPAY:AuthFramework", tAException.getMessage(), (Throwable)tAException);
            return;
        }
    }

    public static a b(Context context) {
        if (iA == null) {
            iA = new a(context);
        }
        return iA;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int N() {
        Log.d("SPAY:AuthFramework", "tppLoad(): ");
        int n2 = 401408;
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) return n2;
        if (iC.loadTA()) {
            if (!iC.pinExist()) return 0;
            return 458753;
        }
        if (iC.isResetPeding()) {
            n2 = 1;
        }
        Log.e("SPAY:AuthFramework", "Failure Load TPP: " + n2);
        return n2;
    }

    public int O() {
        Log.d("SPAY:AuthFramework", "tppUnLoad(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        iC.unloadTA();
        return 0;
    }

    public int P() {
        if (iC.deletePin()) {
            return 0;
        }
        return 458754;
    }

    public int Q() {
        Log.d("SPAY:AuthFramework", "tppCheckTuiSession(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.checkTuiSession();
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[] R() {
        Log.d("SPAY:AuthFramework", "tppGetNonce(): ");
        if (this.iB != 0 || iC == null) {
            return null;
        }
        return iC.getNonce(32);
    }

    public int S() {
        Log.d("SPAY:AuthFramework", "tppClearState");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.clearTuiState();
    }

    public int[] T() {
        Log.d("SPAY:AuthFramework", "tppMerchantSecureTouch");
        int[] arrn = new int[]{0, 0};
        if (this.iB != 0) {
            arrn[0] = this.iB;
            return arrn;
        }
        if (iC == null) {
            arrn[0] = 401408;
            return arrn;
        }
        int[] arrn2 = iC.merchantSecureTouch();
        Log.d("SPAY:AuthFramework", "tppMerchantSecureTouch ret[1]=" + arrn2[1]);
        return arrn2;
    }

    public int a(int n2, int[] arrn) {
        Log.d("SPAY:AuthFramework", "tppMerchantSecureInit");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        int n3 = iC.merchantSecureInit(n2, arrn);
        Log.d("SPAY:AuthFramework", "tppMerchantSecureDisplay ret=" + n3);
        return n3;
    }

    public int a(String string, String string2, String string3, String string4, String string5, String string6, byte[] arrby) {
        Log.d("SPAY:AuthFramework", "tppInAppConfirm");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.inAppConfirm(string, string2, string3, string4, string5, string6, arrby);
    }

    public int a(boolean bl) {
        Log.d("SPAY:AuthFramework", "tppResume(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.resume(bl);
    }

    public int a(boolean bl, boolean bl2) {
        Log.d("SPAY:AuthFramework", "tppVerifyPIN(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.verify(bl, bl2);
    }

    public int a(byte[] arrby, int n2, int n3) {
        Log.d("SPAY:AuthFramework", "tppSetPrompt(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.setPrompt(arrby, n2, n3);
    }

    public int a(byte[] arrby, int n2, int n3, int n4, int n5) {
        Log.d("SPAY:AuthFramework", "tppMerchantSendSecureImg");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        int n6 = iC.merchantSendSecureImg(arrby, n2, n3, n4, n5);
        Log.d("SPAY:AuthFramework", "tppMerchantSendSecureImg ret=" + n6);
        return n6;
    }

    public int a(byte[] arrby, int n2, int n3, int[] arrn) {
        Log.d("SPAY:AuthFramework", "tppMerchantSecureDisplay");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        int n4 = iC.merchantSecureDisplay(arrby, n2, n3, arrn);
        Log.d("SPAY:AuthFramework", "tppMerchantSecureDisplay ret[1]=" + n4);
        return n4;
    }

    public int a(byte[] arrby, byte[] arrby2, int n2, int n3) {
        Log.d("SPAY:AuthFramework", "tppSetCancelBtnImg(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.setCancelBtnImg(arrby, arrby2, n2, n3);
    }

    public int a(byte[] arrby, byte[] arrby2, int n2, int n3, int n4, int n5, int n6) {
        Log.d("SPAY:AuthFramework", "tppSetPinBox(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.setPinBox(arrby, arrby2, n2, n3, n4, n5, n6);
    }

    public int a(String[] arrstring) {
        Log.d("SPAY:AuthFramework", "tppSetSecureModeText(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        if (arrstring == null) {
            Log.e("SPAY:AuthFramework", "SecureModeText: Null input");
            return 4;
        }
        return iC.setSecureModeText(arrstring);
    }

    /*
     * Enabled aggressive block sorting
     */
    public byte[] a(String string, byte[] arrby) {
        Log.d("SPAY:AuthFramework", "tppGetSecureResult(): ");
        Log.d("SPAY:AuthFramework", "taName = " + string + " nonce = " + Arrays.toString((byte[])arrby));
        if (this.iB != 0 || iC == null) {
            return null;
        }
        return iC.getSecureResult(arrby, string);
    }

    public int b(boolean bl) {
        Log.d("SPAY:AuthFramework", "tppSetRtl");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        int n2 = iC.setRtl(bl);
        Log.d("SPAY:AuthFramework", "tppSetRtl ret=" + n2);
        return n2;
    }

    public int b(byte[] arrby, int n2, int n3) {
        Log.d("SPAY:AuthFramework", "tppSetBkImg(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.setBackgroundImg(arrby, n2, n3);
    }

    public int b(byte[] arrby, byte[] arrby2) {
        Log.d("SPAY:AuthFramework", "tppSetupBIO(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.setupBio(arrby, arrby2);
    }

    /*
     * Enabled aggressive block sorting
     */
    public int b(String[] arrstring) {
        Log.d("SPAY:AuthFramework", "tppSetActionBarText(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        if (arrstring == null) {
            Log.e("SPAY:AuthFramework", "ActionBarText: Null input");
            return 4;
        }
        boolean bl = h.isRTL();
        boolean bl2 = h.aq(this.mContext);
        Log.d("SPAY:AuthFramework", "isRTL()=" + bl + " isForceRTL()=" + bl2);
        boolean bl3 = bl || bl2;
        int n2 = this.b(bl3);
        Log.d("SPAY:AuthFramework", "tppSetRtl=" + n2);
        if (n2 != 0) return n2;
        return iC.setActionBarText(arrstring);
    }

    public int c(byte[] arrby) {
        Log.d("SPAY:AuthFramework", "tppSetupPIN(so): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.setupPin(arrby);
    }

    public int d(byte[] arrby) {
        Log.d("SPAY:AuthFramework", "tppPreLoadFpSecureResult(): ");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.preloadFpSecureResult(arrby);
    }

    public int e(byte[] arrby) {
        Log.d("SPAY:AuthFramework", "tppUpdateFP");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.updateFP(arrby);
    }

    public int f(byte[] arrby) {
        Log.d("SPAY:AuthFramework", "tppUpdateFP");
        if (this.iB != 0) {
            return this.iB;
        }
        if (iC == null) {
            return 401408;
        }
        return iC.updateFP(arrby);
    }
}

