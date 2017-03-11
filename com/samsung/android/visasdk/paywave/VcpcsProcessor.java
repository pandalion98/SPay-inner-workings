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
import com.samsung.android.visasdk.p023a.Utils;
import com.samsung.android.visasdk.p024b.CryptoManager;
import com.samsung.android.visasdk.p025c.Log;
import com.samsung.android.visasdk.paywave.Constant.State;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

/* renamed from: com.samsung.android.visasdk.paywave.b */
public class VcpcsProcessor {
    private static final boolean DEBUG;
    private static CryptoManager Dl;
    private static VcpcsManager Er;
    private static VcpcsProcessor mVcpcsProcessor;
    private State Gp;
    private TokenKey Gq;

    /* renamed from: com.samsung.android.visasdk.paywave.b.1 */
    static /* synthetic */ class VcpcsProcessor {
        static final /* synthetic */ int[] Gr;

        static {
            Gr = new int[State.values().length];
            try {
                Gr[State.READY.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                Gr[State.SELECT_PPSE.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                Gr[State.SELECT_AID.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                Gr[State.GPO.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                Gr[State.READ_RECORD.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                Gr[State.GET_DATA.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
            try {
                Gr[State.UPDATE_PARAMS.ordinal()] = 7;
            } catch (NoSuchFieldError e7) {
            }
        }
    }

    static {
        DEBUG = VcpcsManager.Ej;
        mVcpcsProcessor = null;
        Er = null;
        Dl = null;
    }

    private VcpcsProcessor(Context context, Bundle bundle) {
        Dl = CryptoManager.fV();
        if (Dl == null) {
            throw new InitializationException("VcpcsProcessor(): cryptomanager is null ");
        }
        Er = VcpcsManager.m1317a(context, Dl, bundle);
        if (Er == null) {
            throw new InitializationException("VcpcsProcessor(): vcpcsManager is null ");
        }
    }

    public static synchronized VcpcsProcessor m1346a(Context context, Bundle bundle) {
        VcpcsProcessor vcpcsProcessor;
        synchronized (VcpcsProcessor.class) {
            if (mVcpcsProcessor == null) {
                mVcpcsProcessor = new VcpcsProcessor(context, bundle);
            }
            if (DEBUG) {
                Log.m1300d("VcpcsProcessor", "UPDATE_TEST_PARAMS is false");
            }
            vcpcsProcessor = mVcpcsProcessor;
        }
        return vcpcsProcessor;
    }

    private void gr() {
        if (Er != null) {
            Er.gl();
        }
    }

    private boolean m1347a(State state) {
        if (this.Gp == state) {
            return true;
        }
        return false;
    }

    boolean m1348b(State state) {
        switch (VcpcsProcessor.Gr[state.ordinal()]) {
            case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                this.Gp = State.READY;
                return true;
            case CipherSpiExt.DECRYPT_MODE /*2*/:
                this.Gp = State.SELECT_PPSE;
                gr();
                return true;
            case F2m.PPB /*3*/:
                if (this.Gp == State.SELECT_PPSE || this.Gp == State.SELECT_AID) {
                    this.Gp = State.SELECT_AID;
                    return true;
                }
                gr();
                this.Gp = State.SELECT_AID;
                return true;
            case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                if (this.Gp == State.SELECT_AID) {
                    this.Gp = State.GPO;
                    return true;
                }
                break;
            case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                if (this.Gp == State.GPO || this.Gp == State.READ_RECORD) {
                    this.Gp = State.READ_RECORD;
                    return true;
                }
            case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
            case ECCurve.COORD_SKEWED /*7*/:
                return true;
            default:
                if (DEBUG) {
                    Log.m1300d("VcpcsProcessor", "unknown state");
                }
                this.Gp = State.READY;
                return false;
        }
        return false;
    }

    public boolean m1350c(TokenKey tokenKey) {
        Log.m1300d("VcpcsProcessor", "selectCard()");
        m1348b(State.READY);
        this.Gq = tokenKey;
        if (Er.m1343b(tokenKey) >= 0) {
            return true;
        }
        this.Gq = null;
        return false;
    }

    public ApduResponse m1349c(byte[] bArr, boolean z) {
        ApduResponse apduResponse;
        Log.m1300d("VcpcsProcessor", "processCommandApdu()");
        String str = "NoCommand";
        ApduResponse apduResponse2 = null;
        State state = State.READY;
        if (bArr == null) {
            apduResponse = new ApduResponse(-12);
            if (DEBUG) {
                Log.m1300d("VcpcsProcessor", "processApdu: " + str);
            }
        } else {
            int length = bArr.length;
            if (length > SkeinMac.SKEIN_256 || length <= 0) {
                if (DEBUG) {
                    Log.m1300d("VcpcsProcessor", "APDU length " + length + " exceeds the maximum");
                }
                apduResponse = new ApduResponse(-12);
                if (DEBUG) {
                    Log.m1300d("VcpcsProcessor", "processApdu: " + str);
                }
            } else {
                if (DEBUG) {
                    Log.m1300d("VcpcsProcessor", "cAPDU=" + Utils.m1285o(bArr));
                }
                short s = Utils.getShort(bArr, 0);
                switch (s) {
                    case (short) -32600:
                        str = "GPO";
                        if (!m1347a(State.SELECT_AID)) {
                            if (DEBUG) {
                                Log.m1300d("VcpcsProcessor", "should not go to GPO");
                            }
                            state = State.READY;
                            apduResponse2 = new ApduResponse(-5);
                            break;
                        }
                        state = State.GPO;
                        break;
                    case (short) -32566:
                        str = "Get Data";
                        break;
                    case (short) -32529:
                        str = "Update Test Parameters";
                        break;
                    case CipherSuite.TLS_DH_DSS_WITH_AES_128_GCM_SHA256 /*164*/:
                        String str2 = "Select";
                        if (!Utils.m1284g(bArr, Constant.iO)) {
                            if (!m1347a(State.SELECT_PPSE) && !m1347a(State.SELECT_AID)) {
                                str = "SelectAID without PPSE";
                                state = State.SELECT_AID;
                                break;
                            }
                            str = "SelectAID";
                            state = State.SELECT_AID;
                            break;
                        }
                        str = "SelectPPSE";
                        state = State.SELECT_PPSE;
                        break;
                        break;
                    case CipherSuite.TLS_DHE_PSK_WITH_AES_128_CBC_SHA256 /*178*/:
                        str = "Read Record";
                        if (!m1347a(State.GPO) && !m1347a(State.READ_RECORD)) {
                            if (DEBUG) {
                                Log.m1300d("VcpcsProcessor", "should not go to Read Record");
                            }
                            state = State.READY;
                            apduResponse2 = new ApduResponse(-5);
                            break;
                        }
                        state = State.READ_RECORD;
                        break;
                    default:
                        str = "Unhandled APDU: ";
                        if (DEBUG) {
                            Log.m1300d("VcpcsProcessor", "error APDU: " + s);
                        }
                        state = State.READY;
                        apduResponse2 = new ApduResponse(-12);
                        break;
                }
                if (!m1348b(state)) {
                    state = State.READY;
                    m1348b(State.READY);
                    apduResponse2 = new ApduResponse(-5);
                }
                switch (VcpcsProcessor.Gr[state.ordinal()]) {
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                        apduResponse = Er.m1338C(bArr);
                        break;
                    case F2m.PPB /*3*/:
                        apduResponse = Er.m1339D(bArr);
                        break;
                    case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                        apduResponse = Er.m1340E(bArr);
                        break;
                    case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                        apduResponse = Er.m1341F(bArr);
                        break;
                    case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                        apduResponse = Er.m1337B(bArr);
                        break;
                    case ECCurve.COORD_SKEWED /*7*/:
                        apduResponse = Er.m1345u(bArr);
                        break;
                    default:
                        apduResponse = apduResponse2;
                        break;
                }
                if (apduResponse == null || apduResponse.getApduData() == null || apduResponse.getApduError() == null) {
                    Log.m1301e("VcpcsProcessor", "response apdu length < 0, something is wrong in SDK");
                    apduResponse = new ApduResponse(-5);
                }
                if (DEBUG) {
                    Log.m1300d("VcpcsProcessor", "processApdu " + str + ", rAPDU=" + Utils.m1285o(apduResponse.getApduData()));
                }
            }
        }
        return apduResponse;
    }

    public TransactionStatus processTransactionComplete(TokenKey tokenKey) {
        Log.m1300d("VcpcsProcessor", "processTransactionComplete()");
        if (this.Gq == null) {
            return new TransactionStatus(TransactionError.OTHER_ERROR, false);
        }
        TransactionStatus a = Er.m1342a(this.Gq, true);
        if (a.getError() != TransactionError.NO_ERROR) {
            return a;
        }
        this.Gq = null;
        return a;
    }

    public boolean isCvmVerified() {
        return Er.fW();
    }

    public void setCvmVerified(boolean z) {
        Er.m1344j(z);
    }

    public void setCvmVerificationMode(CvmMode cvmMode) {
        Er.setCvmVerificationMode(cvmMode);
    }

    public boolean m1351l(boolean z) {
        Log.m1300d("VcpcsProcessor", "processMstTransactionComplete()");
        if (this.Gq != null) {
            Er.m1342a(this.Gq, false);
        }
        return false;
    }

    public boolean prepareMstData() {
        if (this.Gq != null) {
            return Er.prepareMstData();
        }
        Log.m1301e("VcpcsProcessor", "no card is selected for MST transaction");
        return false;
    }

    public boolean shouldTapAndGo(TokenKey tokenKey) {
        return Er.shouldTapAndGo(tokenKey);
    }

    public PaymentDataRequest constructPaymentDataRequest(String str, TokenKey tokenKey, String str2, String str3) {
        return Er.constructPaymentDataRequest(str, tokenKey, str2, str3);
    }

    public void processInAppTransactionComplete(TokenKey tokenKey, String str, boolean z) {
        Er.processInAppTransactionComplete(tokenKey, str, z);
    }
}
