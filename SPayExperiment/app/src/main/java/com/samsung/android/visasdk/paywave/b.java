/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  java.lang.Class
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.visasdk.paywave;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.visasdk.facade.data.ApduResponse;
import com.samsung.android.visasdk.facade.data.CvmMode;
import com.samsung.android.visasdk.facade.data.PaymentDataRequest;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.data.TransactionError;
import com.samsung.android.visasdk.facade.data.TransactionStatus;
import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.samsung.android.visasdk.paywave.Constant;
import com.samsung.android.visasdk.paywave.a;
import com.samsung.android.visasdk.paywave.data.ApduError;

public class b {
    private static final boolean DEBUG = a.Ej;
    private static com.samsung.android.visasdk.b.b Dl;
    private static a Er;
    private static b mVcpcsProcessor;
    private Constant.State Gp;
    private TokenKey Gq;

    static {
        mVcpcsProcessor = null;
        Er = null;
        Dl = null;
    }

    private b(Context context, Bundle bundle) {
        Dl = com.samsung.android.visasdk.b.b.fV();
        if (Dl == null) {
            throw new InitializationException("VcpcsProcessor(): cryptomanager is null ");
        }
        Er = a.a(context, Dl, bundle);
        if (Er == null) {
            throw new InitializationException("VcpcsProcessor(): vcpcsManager is null ");
        }
    }

    public static b a(Context context, Bundle bundle) {
        Class<b> class_ = b.class;
        synchronized (b.class) {
            if (mVcpcsProcessor == null) {
                mVcpcsProcessor = new b(context, bundle);
            }
            if (DEBUG) {
                com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "UPDATE_TEST_PARAMS is false");
            }
            b b2 = mVcpcsProcessor;
            // ** MonitorExit[var4_2] (shouldn't be in output)
            return b2;
        }
    }

    private boolean a(Constant.State state) {
        return this.Gp == state;
    }

    private void gr() {
        if (Er != null) {
            Er.gl();
        }
    }

    boolean b(Constant.State state) {
        boolean bl = true;
        switch (1.Gr[state.ordinal()]) {
            default: {
                if (DEBUG) {
                    com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "unknown state");
                }
                this.Gp = Constant.State.DP;
                bl = false;
            }
            case 6: 
            case 7: {
                return bl;
            }
            case 1: {
                this.Gp = Constant.State.DP;
                return bl;
            }
            case 2: {
                this.Gp = Constant.State.DQ;
                this.gr();
                return bl;
            }
            case 3: {
                if (this.Gp == Constant.State.DQ || this.Gp == Constant.State.DR) {
                    this.Gp = Constant.State.DR;
                    return bl;
                }
                this.gr();
                this.Gp = Constant.State.DR;
                return bl;
            }
            case 4: {
                if (this.Gp != Constant.State.DR) break;
                this.Gp = Constant.State.DS;
                return bl;
            }
            case 5: {
                if (this.Gp != Constant.State.DS && this.Gp != Constant.State.DU) break;
                this.Gp = Constant.State.DU;
                return bl;
            }
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    public ApduResponse c(byte[] arrby, boolean bl) {
        ApduResponse apduResponse;
        com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "processCommandApdu()");
        Constant.State state = Constant.State.DP;
        if (arrby == null) {
            apduResponse = new ApduResponse(-12);
            if (!DEBUG) return apduResponse;
            {
                com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "processApdu: " + "NoCommand");
                return apduResponse;
            }
        } else {
            int n2 = arrby.length;
            if (n2 > 256 || n2 <= 0) {
                if (DEBUG) {
                    com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "APDU length " + n2 + " exceeds the maximum");
                }
                apduResponse = new ApduResponse(-12);
                if (!DEBUG) return apduResponse;
                {
                    com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "processApdu: " + "NoCommand");
                    return apduResponse;
                }
            } else {
                ApduResponse apduResponse2;
                String string;
                if (DEBUG) {
                    com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "cAPDU=" + com.samsung.android.visasdk.a.b.o(arrby));
                }
                short s2 = com.samsung.android.visasdk.a.b.getShort(arrby, 0);
                switch (s2) {
                    default: {
                        string = "Unhandled APDU: ";
                        if (DEBUG) {
                            com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "error APDU: " + s2);
                        }
                        state = Constant.State.DP;
                        apduResponse2 = new ApduResponse(-12);
                        break;
                    }
                    case 164: {
                        if (com.samsung.android.visasdk.a.b.g(arrby, Constant.iO)) {
                            string = "SelectPPSE";
                            state = Constant.State.DQ;
                            apduResponse2 = null;
                            break;
                        }
                        if (this.a(Constant.State.DQ) || this.a(Constant.State.DR)) {
                            string = "SelectAID";
                            state = Constant.State.DR;
                            apduResponse2 = null;
                            break;
                        }
                        string = "SelectAID without PPSE";
                        state = Constant.State.DR;
                        apduResponse2 = null;
                        break;
                    }
                    case -32600: {
                        string = "GPO";
                        if (this.a(Constant.State.DR)) {
                            state = Constant.State.DS;
                            apduResponse2 = null;
                            break;
                        }
                        if (DEBUG) {
                            com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "should not go to GPO");
                        }
                        state = Constant.State.DP;
                        apduResponse2 = new ApduResponse(-5);
                        break;
                    }
                    case 178: {
                        string = "Read Record";
                        if (this.a(Constant.State.DS) || this.a(Constant.State.DU)) {
                            state = Constant.State.DU;
                            apduResponse2 = null;
                            break;
                        }
                        if (DEBUG) {
                            com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "should not go to Read Record");
                        }
                        state = Constant.State.DP;
                        apduResponse2 = new ApduResponse(-5);
                        break;
                    }
                    case -32529: {
                        string = "Update Test Parameters";
                        apduResponse2 = null;
                        break;
                    }
                    case -32566: {
                        string = "Get Data";
                        apduResponse2 = null;
                    }
                }
                if (!this.b(state)) {
                    state = Constant.State.DP;
                    this.b(Constant.State.DP);
                    apduResponse2 = new ApduResponse(-5);
                }
                switch (1.Gr[state.ordinal()]) {
                    default: {
                        apduResponse = apduResponse2;
                        break;
                    }
                    case 2: {
                        apduResponse = Er.C(arrby);
                        break;
                    }
                    case 3: {
                        apduResponse = Er.D(arrby);
                        break;
                    }
                    case 4: {
                        apduResponse = Er.E(arrby);
                        break;
                    }
                    case 5: {
                        apduResponse = Er.F(arrby);
                        break;
                    }
                    case 6: {
                        apduResponse = Er.B(arrby);
                        break;
                    }
                    case 7: {
                        apduResponse = Er.u(arrby);
                    }
                }
                if (apduResponse == null || apduResponse.getApduData() == null || apduResponse.getApduError() == null) {
                    com.samsung.android.visasdk.c.a.e("VcpcsProcessor", "response apdu length < 0, something is wrong in SDK");
                    apduResponse = new ApduResponse(-5);
                }
                if (!DEBUG) return apduResponse;
                {
                    com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "processApdu " + string + ", rAPDU=" + com.samsung.android.visasdk.a.b.o(apduResponse.getApduData()));
                    return apduResponse;
                }
            }
        }
    }

    public boolean c(TokenKey tokenKey) {
        com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "selectCard()");
        this.b(Constant.State.DP);
        this.Gq = tokenKey;
        if (Er.b(tokenKey) >= 0L) {
            return true;
        }
        this.Gq = null;
        return false;
    }

    public PaymentDataRequest constructPaymentDataRequest(String string, TokenKey tokenKey, String string2, String string3) {
        return Er.constructPaymentDataRequest(string, tokenKey, string2, string3);
    }

    public boolean isCvmVerified() {
        return Er.fW();
    }

    public boolean l(boolean bl) {
        com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "processMstTransactionComplete()");
        if (this.Gq != null) {
            Er.a(this.Gq, false);
        }
        return false;
    }

    public boolean prepareMstData() {
        if (this.Gq == null) {
            com.samsung.android.visasdk.c.a.e("VcpcsProcessor", "no card is selected for MST transaction");
            return false;
        }
        return Er.prepareMstData();
    }

    public void processInAppTransactionComplete(TokenKey tokenKey, String string, boolean bl) {
        Er.processInAppTransactionComplete(tokenKey, string, bl);
    }

    public TransactionStatus processTransactionComplete(TokenKey tokenKey) {
        com.samsung.android.visasdk.c.a.d("VcpcsProcessor", "processTransactionComplete()");
        if (this.Gq != null) {
            TransactionStatus transactionStatus = Er.a(this.Gq, true);
            if (transactionStatus.getError() == TransactionError.NO_ERROR) {
                this.Gq = null;
            }
            return transactionStatus;
        }
        return new TransactionStatus(TransactionError.OTHER_ERROR, false);
    }

    public void setCvmVerificationMode(CvmMode cvmMode) {
        Er.setCvmVerificationMode(cvmMode);
    }

    public void setCvmVerified(boolean bl) {
        Er.j(bl);
    }

    public boolean shouldTapAndGo(TokenKey tokenKey) {
        return Er.shouldTapAndGo(tokenKey);
    }

}

