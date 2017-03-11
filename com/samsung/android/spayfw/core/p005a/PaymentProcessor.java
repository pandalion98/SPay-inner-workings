package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.RemoteException;
import android.text.TextUtils;
import com.google.android.gms.location.LocationStatusCodes;
import com.samsung.android.spayfw.appinterface.ApduReasonCode;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IInAppPayCallback;
import com.samsung.android.spayfw.appinterface.IPayCallback;
import com.samsung.android.spayfw.appinterface.ISO7816;
import com.samsung.android.spayfw.appinterface.ISelectCardCallback;
import com.samsung.android.spayfw.appinterface.InAppTransactionInfo;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.Account;
import com.samsung.android.spayfw.core.ApduHelper;
import com.samsung.android.spayfw.core.BinAttribute;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.ConfigurationManager;
import com.samsung.android.spayfw.core.MstConfigurationManager;
import com.samsung.android.spayfw.core.PayConfigurator;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.PaymentFrameworkMessage;
import com.samsung.android.spayfw.core.State;
import com.samsung.android.spayfw.core.retry.TransactionDetailsRetryRequester;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.p008e.NfcControllerWrapper;
import com.samsung.android.spayfw.payprovider.MerchantServerRequester.MerchantInfo;
import com.samsung.android.spayfw.payprovider.PayResponse;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardMaster;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.MCFCITemplate;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.storage.models.PaymentDetailsRecord;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytui.SpayTuiTAController;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.android.visasdk.facade.data.TransactionStatus;
import java.util.Arrays;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.asn1.isismtt.ocsp.RequestedCertificate;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

/* renamed from: com.samsung.android.spayfw.core.a.n */
public class PaymentProcessor extends Processor implements IPaymentProcessor {
    private static boolean lA;
    private static PaymentProcessor lB;
    private static CountDownTimer lC;
    static IPayCallback lz;
    private Timer lD;
    private Timer lE;
    private String lF;
    private Bundle lG;
    private Timer lH;
    private IPayCallback lI;
    private boolean lJ;
    private PayConfig lK;
    PaymentDetailsRecord lf;
    long startTime;

    /* renamed from: com.samsung.android.spayfw.core.a.n.1 */
    class PaymentProcessor extends TimerTask {
        final /* synthetic */ PaymentProcessor lL;
        final /* synthetic */ int val$reason;

        PaymentProcessor(PaymentProcessor paymentProcessor, int i) {
            this.lL = paymentProcessor;
            this.val$reason = i;
        }

        public void run() {
            Log.m285d("PaymentProcessor", "nfc usage timer expired");
            try {
                this.lL.m476u(this.val$reason);
            } catch (Throwable e) {
                Log.m284c("PaymentProcessor", e.getMessage(), e);
            }
            this.lL.lD = null;
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.n.a */
    private class PaymentProcessor extends TimerTask {
        final /* synthetic */ PaymentProcessor lL;
        private int reason;

        public PaymentProcessor(PaymentProcessor paymentProcessor, int i) {
            this.lL = paymentProcessor;
            this.reason = -99;
            this.reason = i;
        }

        public void run() {
            Log.m287i("PaymentProcessor", "NFC apdu TimerExpired::invoke onDeactivated. Timestamp = " + System.currentTimeMillis() + ", reason: " + this.reason);
            try {
                this.lL.m476u(this.reason);
            } catch (Throwable e) {
                Log.m284c("PaymentProcessor", e.getMessage(), e);
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.n.b */
    private class PaymentProcessor implements PayResponse {
        final /* synthetic */ PaymentProcessor lL;

        /* renamed from: com.samsung.android.spayfw.core.a.n.b.1 */
        class PaymentProcessor implements Runnable {
            final /* synthetic */ Card jL;
            final /* synthetic */ PaymentProcessor lM;

            PaymentProcessor(PaymentProcessor paymentProcessor, Card card) {
                this.lM = paymentProcessor;
                this.jL = card;
            }

            public void run() {
                PaymentProcessor.lC = new PaymentProcessor(this.lM.lL, (long) this.lM.lL.m455a(this.jL, this.lM.lL.lK), 5000);
                PaymentProcessor.lC.start();
            }
        }

        public PaymentProcessor(PaymentProcessor paymentProcessor, IPayCallback iPayCallback) {
            this.lL = paymentProcessor;
            PaymentProcessor.lz = iPayCallback;
        }

        public void m451a(Object obj, int i, int i2, String str, String str2) {
            try {
                if (this.lL.lf != null) {
                    if (i != -46) {
                        this.lL.lf.setMstSequenceId(str2);
                        if (this.lL.lJ) {
                            this.lL.lf.setMstRetryAttempted("ATTEMPTED");
                            this.lL.lf.setMstRetryLoopcount(i);
                        } else {
                            this.lL.lf.setMstAttempted("ATTEMPTED");
                            if (this.lL.lf.getAuthenticationMode() == null) {
                                this.lL.lf.setAuthenticationMode(str);
                            }
                            this.lL.lf.setMstLoopcount(i);
                        }
                    } else {
                        Log.m285d("PaymentProcessor", "NFC Timeout Expired");
                        this.lL.lf.setNfcRetryAttempted("ATTEMPTED");
                    }
                }
                if (i == i2) {
                    this.lL.clearRetryPay();
                }
                if (PaymentProcessor.lz == null) {
                    Log.m285d("PaymentProcessor", "invoking app callback onPay, callback is null");
                    return;
                }
                if (i != i2) {
                    Log.m287i("PaymentProcessor", "invoking app callback onPay");
                    PaymentProcessor.lz.onPay(this.lL.lF, i, i2);
                } else if (i == -46) {
                    Log.m287i("PaymentProcessor", "invoking app callback onFinish after NFC timeout");
                    PaymentProcessor.lz.onFinish(this.lL.lF, 1, null);
                }
                Log.m285d("PaymentProcessor", "onPay: CurrentLoop = " + i);
            } catch (Throwable e) {
                Log.m284c("PaymentProcessor", e.getMessage(), e);
            } finally {
                Log.m285d("PaymentProcessor", "onPay: CurrentLoop = " + i);
            }
        }

        public void m450a(Object obj, int i, int i2, String str) {
            if (PaymentProcessor.lz == null) {
                Log.m285d("PaymentProcessor", "invoking app callback onPaySwitch, callback is null");
                return;
            }
            try {
                Log.m285d("PaymentProcessor", "invoking app callback onPaySwitch");
                PaymentProcessor.lz.onPaySwitch(this.lL.lF, i, i2);
                if (this.lL.lf != null) {
                    Log.m285d("PaymentProcessor", "onPaySwitch");
                    this.lL.lf.setAuthenticationMode(str);
                }
                if (i == 1 && i2 == 2) {
                    if (PaymentProcessor.lC != null) {
                        Log.m285d("PaymentProcessor", "Cancel CountDown Timer");
                        PaymentProcessor.lC.cancel();
                    }
                    Card r = this.lL.iJ.m559r(this.lL.lF);
                    if (r == null) {
                        Log.m286e("PaymentProcessor", "onPaySwitch card is null");
                    } else if (this.lL.lJ) {
                        PaymentFrameworkApp.az().postAtFrontOfQueue(new PaymentProcessor(this.lL, r, this.lL.lK));
                    } else {
                        PaymentFrameworkApp.az().postAtFrontOfQueue(new PaymentProcessor(this, r));
                    }
                }
            } catch (Throwable e) {
                Log.m284c("PaymentProcessor", e.getMessage(), e);
            }
        }

        public void m452a(Object obj, int i, String str) {
            try {
                if (this.lL.lf != null) {
                    Log.m286e("PaymentProcessor", "onFail");
                    if (!this.lL.lJ) {
                        if ((this.lL.lf.getMstAttempted() == null || !this.lL.lf.getMstAttempted().equals("ATTEMPTED")) && i != -11) {
                            this.lL.lf.setMstAttempted("FAILED");
                        }
                        this.lL.lf.setAuthenticationMode(str);
                    } else if ((this.lL.lf.getMstRetryAttempted() == null || !this.lL.lf.getMstRetryAttempted().equals("ATTEMPTED")) && i != -11) {
                        this.lL.lf.setMstRetryAttempted("FAILED");
                    }
                }
                this.lL.clearRetryPay();
                if (PaymentProcessor.lz == null) {
                    Log.m286e("PaymentProcessor", "invoking app callback onFail, callback is null");
                    return;
                }
                int i2;
                Log.m285d("PaymentProcessor", "invoking app callback onFail: errorCode: " + i);
                if (i == -11) {
                    i2 = -2;
                } else {
                    i2 = i;
                }
                switch (i2) {
                    case SpayTuiTAController.ERROR_INVALID_INPUT_PARAMS /*-3*/:
                        i2 = -37;
                        break;
                    case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                        i2 = -36;
                        break;
                    case RequestedCertificate.certificate /*-1*/:
                        i2 = -35;
                        break;
                }
                PaymentProcessor.lz.onFail(this.lL.lF, i2);
                PaymentProcessor.lz = null;
            } catch (Throwable e) {
                Log.m284c("PaymentProcessor", e.getMessage(), e);
            }
        }

        public void m453g(Object obj) {
            Log.m285d("PaymentProcessor", "onRetry()");
            if (PaymentProcessor.lz == null) {
                Log.m285d("PaymentProcessor", "Callback is null");
                this.lL.clearRetryPay();
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.n.c */
    private class PaymentProcessor implements Runnable {
        private PayConfig js;
        final /* synthetic */ PaymentProcessor lL;
        private Card lN;

        /* renamed from: com.samsung.android.spayfw.core.a.n.c.1 */
        class PaymentProcessor extends CountDownTimer {
            final /* synthetic */ PaymentProcessor lO;

            PaymentProcessor(PaymentProcessor paymentProcessor, long j, long j2) {
                this.lO = paymentProcessor;
                super(j, j2);
            }

            public void onTick(long j) {
                Log.m285d("PaymentProcessor", "CDT Retry : remaining time : " + (((int) j) / LocationStatusCodes.GEOFENCE_NOT_AVAILABLE));
            }

            public void onFinish() {
                Log.m285d("PaymentProcessor", "CDT Retry : onFinish - sMstRetryType : " + PayConfigurator.aw());
                synchronized (this.lO.lL) {
                    if (PaymentProcessor.lz != null) {
                        try {
                            Log.m287i("PaymentProcessor", "invoking app callback onFinish");
                            PaymentProcessor.lz.onFinish(this.lO.lL.lF, 2, null);
                            PaymentProcessor.lz = null;
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.m287i("PaymentProcessor", "Callback is null");
                    }
                }
                this.lO.lL.clearRetryPay();
            }
        }

        PaymentProcessor(PaymentProcessor paymentProcessor, Card card, PayConfig payConfig) {
            this.lL = paymentProcessor;
            this.lN = card;
            this.js = payConfig;
        }

        public void run() {
            int payConfigTransmitTime;
            if (this.js == null || this.js.getMstTransmitTime() == 0) {
                payConfigTransmitTime = this.lN.ad().getPayConfigTransmitTime(true) * LocationStatusCodes.GEOFENCE_NOT_AVAILABLE;
            } else {
                payConfigTransmitTime = this.js.getMstTransmitTime();
            }
            Log.m285d("PaymentProcessor", "Retry mstTransmitTime in ms: " + payConfigTransmitTime);
            PaymentProcessor.lC = new PaymentProcessor(this, (long) payConfigTransmitTime, 5000);
            PaymentProcessor.lC.start();
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.n.d */
    private class PaymentProcessor extends CountDownTimer {
        final /* synthetic */ PaymentProcessor lL;

        /* renamed from: com.samsung.android.spayfw.core.a.n.d.1 */
        class PaymentProcessor extends TimerTask {
            final /* synthetic */ PaymentProcessor lP;

            PaymentProcessor(PaymentProcessor paymentProcessor) {
                this.lP = paymentProcessor;
            }

            public void run() {
                Log.m285d("PaymentProcessor", "CDT : Retry Timeout Expired");
                this.lP.lL.clearRetryPay();
            }
        }

        PaymentProcessor(PaymentProcessor paymentProcessor, long j, long j2) {
            this.lL = paymentProcessor;
            super(j, j2);
        }

        public void onTick(long j) {
            int i = ((int) j) / LocationStatusCodes.GEOFENCE_NOT_AVAILABLE;
            Log.m285d("PaymentProcessor", "CDT : remaining time : " + i);
            if (i <= 10 && PayConfigurator.aw() == 2) {
                if (this.lL.iJ == null || this.lL.lF == null) {
                    Log.m286e("PaymentProcessor", "No account or selected token id");
                    return;
                }
                Card r = this.lL.iJ.m559r(this.lL.lF);
                if (r == null) {
                    Log.m286e("PaymentProcessor", "card is null during timer tick");
                    return;
                }
                int payConfigTransmitTime = r.ad().getPayConfigTransmitTime(true);
                if (this.lL.lK != null) {
                    int mstTransmitTime = this.lL.lK.getMstTransmitTime();
                    if (mstTransmitTime != 0) {
                        payConfigTransmitTime = mstTransmitTime / LocationStatusCodes.GEOFENCE_NOT_AVAILABLE;
                    }
                }
                Log.m285d("PaymentProcessor", "Mst Transmit Time = " + payConfigTransmitTime);
                Log.m285d("PaymentProcessor", "Retry Timeout = " + i);
                if (!(this.lL.lf == null || this.lL.lJ)) {
                    this.lL.lf.setMstLoopcount(this.lL.lf.getMstLoopcount() + 1);
                }
                synchronized (this.lL) {
                    if (PaymentProcessor.lz != null) {
                        try {
                            Log.m287i("PaymentProcessor", "invoking app callback onRetry");
                            PaymentProcessor.lz.onRetry(this.lL.lF, i, payConfigTransmitTime);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Log.m287i("PaymentProcessor", "Callback is null");
                    }
                }
            }
        }

        public void onFinish() {
            Log.m285d("PaymentProcessor", "CDT : onFinish - sMstRetryType : " + PayConfigurator.aw());
            if (PayConfigurator.aw() == 1) {
                if (PaymentProcessor.lz == null) {
                    Log.m285d("PaymentProcessor", "Callback is null");
                    this.lL.clearRetryPay();
                    return;
                }
                int av = PayConfigurator.av();
                try {
                    Log.m285d("PaymentProcessor", "CDT : invoking app callback onRetry");
                    if (!State.m657q(1)) {
                        Log.m286e("PaymentProcessor", "CDT : Cannot goto Npay ready");
                    }
                    if (!State.m657q(2)) {
                        Log.m286e("PaymentProcessor", "CDT : Cannot goto Selected State");
                    }
                    Card r = this.lL.iJ.m559r(this.lL.lF);
                    if (r == null || r.ad() == null) {
                        PaymentProcessor.lz.onFail(this.lL.lF, -10);
                        this.lL.clearRetryPay();
                        return;
                    }
                    int payConfigTransmitTime = r.ad().getPayConfigTransmitTime(true);
                    if (this.lL.lK != null) {
                        int mstTransmitTime = this.lL.lK.getMstTransmitTime();
                        if (mstTransmitTime != 0) {
                            payConfigTransmitTime = mstTransmitTime / LocationStatusCodes.GEOFENCE_NOT_AVAILABLE;
                        }
                    }
                    if (!(Utils.ao(this.lL.mContext) && r.m578k(2))) {
                        payConfigTransmitTime = 30;
                    }
                    Log.m285d("PaymentProcessor", "CDT : Mst Transmit Time = " + payConfigTransmitTime);
                    Log.m285d("PaymentProcessor", "CDT : Retry Timeout = " + av);
                    if (!(this.lL.lf == null || this.lL.lJ)) {
                        this.lL.lf.setMstLoopcount(this.lL.lf.getMstLoopcount() + 1);
                    }
                    Log.m287i("PaymentProcessor", "invoking app callback onRetry");
                    r.ad().forceQuitMst();
                    PaymentProcessor.lz.onRetry(this.lL.lF, av, payConfigTransmitTime);
                    TimerTask paymentProcessor = new PaymentProcessor(this);
                    Timer timer = new Timer();
                    av += 5;
                    if (this.lL.lH == null) {
                        this.lL.lH = timer;
                        this.lL.lH.schedule(paymentProcessor, (long) (av * LocationStatusCodes.GEOFENCE_NOT_AVAILABLE));
                        return;
                    }
                    Log.m286e("PaymentProcessor", "CDT : Retry Timer Running. Restart.");
                    this.lL.lH.cancel();
                    this.lL.lH = timer;
                    this.lL.lH.schedule(paymentProcessor, (long) (av * LocationStatusCodes.GEOFENCE_NOT_AVAILABLE));
                } catch (Throwable e) {
                    Log.m284c("PaymentProcessor", e.getMessage(), e);
                }
            } else if (PayConfigurator.aw() == 2) {
                this.lL.clearRetryPay();
            }
        }
    }

    static {
        lA = false;
    }

    public static final synchronized IPaymentProcessor m470q(Context context) {
        IPaymentProcessor a;
        synchronized (PaymentProcessor.class) {
            if (lB == null) {
                lB = new PaymentProcessor(context);
            }
            if (ConfigurationManager.m582i(context)) {
                a = TapAndGoDecorator.m513a(context, lB);
            } else {
                a = lB;
            }
        }
        return a;
    }

    public static final short m460a(byte[] bArr, short s) {
        return (short) ((((short) bArr[s]) << 8) + (((short) bArr[(short) (s + 1)]) & GF2Field.MASK));
    }

    private PaymentProcessor(Context context) {
        super(context);
        this.lD = null;
        this.lE = null;
        this.lG = null;
        this.lJ = false;
        this.lK = null;
    }

    public void clearCard() {
        bb();
        Log.m285d("PaymentProcessor", "clearCard(): mSelectCardTokenId = " + this.lF);
        State.m657q(1);
        if (!(this.lF == null || this.iJ == null)) {
            Card r = this.iJ.m559r(this.lF);
            if (r == null) {
                Log.m286e("PaymentProcessor", "clearCard: card object does not exist");
                this.lF = null;
                return;
            }
            r.ad().clearSecureObjectInputForPayment();
        }
        this.lF = null;
    }

    public synchronized ApduReasonCode m476u(int i) {
        ApduReasonCode apduReasonCode = null;
        synchronized (this) {
            if (this.lE != null) {
                Log.m285d("PaymentProcessor", "onDeactivated called. mNFCTimer is not null. Cancel mNFCTimer. Timestamp =  " + System.currentTimeMillis());
                this.lE.cancel();
                this.lE = null;
            }
            bb();
            Log.m285d("PaymentProcessor", "onDeactivated(): " + i);
            if (!State.aO()) {
                Log.m285d("PaymentProcessor", "onDeactivated -no need to deactivate");
            } else if (this.lF == null) {
                Log.m286e("PaymentProcessor", "onDeactivated- mSelectCardTokenId is null");
            } else if (this.iJ == null) {
                Log.m286e("PaymentProcessor", "onDeactivated- Account is null");
            } else {
                Card r = this.iJ.m559r(this.lF);
                if (r == null) {
                    Log.m286e("PaymentProcessor", "onDeactivated- unable to get card object");
                } else if (!r.m578k(1)) {
                    Log.m286e("PaymentProcessor", "NFC is not supported");
                } else if (ApduHelper.m569a(r, i) || i == -99 || ApduHelper.hasError() || i == -98 || i == -96) {
                    if (this.lD != null) {
                        Log.m285d("PaymentProcessor", "Cancel timer");
                        this.lD.cancel();
                        this.lD = null;
                    }
                    apduReasonCode = m457a(i, r);
                } else {
                    Log.m285d("PaymentProcessor", "no need to cancel, continue NFC session ");
                }
            }
        }
        return apduReasonCode;
    }

    public synchronized byte[] processApdu(byte[] bArr, Bundle bundle) {
        byte[] bArr2;
        bb();
        if (PlccTAController.CARD_BRAND_GIFT.equals(this.lF)) {
            Log.m290w("PaymentProcessor", "Current selected card is a Gift card, hence ignore processApdu!");
            bArr2 = ApduHelper.iR;
        } else {
            if (this.lJ) {
                if (this.lf != null) {
                    Log.m285d("PaymentProcessor", "processApdu - set nfc retry attempted");
                    this.lf.setNfcRetryAttempted("ATTEMPTED");
                }
            } else if (this.lf != null) {
                Log.m285d("PaymentProcessor", "processApdu - set nfc attempted");
                this.lf.setNfcAttempted("ATTEMPTED");
            }
            if (bArr == null) {
                Log.m286e("PaymentProcessor", "command apdu is null");
                bArr2 = ApduHelper.iR;
            } else {
                if (this.lE != null) {
                    Log.m285d("PaymentProcessor", "processApdu called, cancel mNfcTimer");
                    this.lE.cancel();
                    this.lE = null;
                }
                if (State.m658r(32)) {
                    short s;
                    Card r;
                    short a = PaymentProcessor.m460a(bArr, (short) 0);
                    if (a == ISO7816.SELECT) {
                        if (Arrays.equals(ApduHelper.iO, bArr)) {
                            if (ApduHelper.aa() >= 5) {
                                Log.m286e("PaymentProcessor", "processApdu - reach max PPSE count");
                                m461a(-99, 1000);
                                bArr2 = ApduHelper.iS;
                            } else {
                                s = MCFCITemplate.TAG_FCI_PROPRIETARY_TEMPLATE;
                                ApduHelper.m570b(s);
                                if (this.lF != null) {
                                    Log.m286e("PaymentProcessor", "processApdu- mSelectCardTokenId is null");
                                    if (lz == null) {
                                        lz.onFail(this.lF, -5);
                                    } else {
                                        Log.m285d("PaymentProcessor", "pay callback is null");
                                    }
                                    bArr2 = ApduHelper.iR;
                                } else {
                                    r = this.iJ.m559r(this.lF);
                                    if (r == null) {
                                        Log.m286e("PaymentProcessor", "processApdu- unable to get card object");
                                        if (lz != null) {
                                            lz.onFail(this.lF, -1);
                                        }
                                        bArr2 = ApduHelper.iR;
                                    } else if (r.m578k(1)) {
                                        Log.m286e("PaymentProcessor", "this card cannot be used for nfc payment");
                                        bArr2 = ApduHelper.iR;
                                    } else {
                                        bArr2 = r.ad().processApdu(bArr, bundle);
                                        if (bArr2 != null) {
                                            bArr2 = ApduHelper.iR;
                                        } else {
                                            m465b(r, -98);
                                            Log.m285d("APDU_LOG", ApduHelper.m567a(s, bArr2));
                                            if (ApduHelper.hasError()) {
                                                Log.m285d("PaymentProcessor", "apdu processing failed run timer");
                                                m461a(-99, 1000);
                                            } else if (ApduHelper.m569a(r, ApduHelper.m563Y().getCode())) {
                                                Log.m285d("PaymentProcessor", "Transaction sequence completed, run timer also");
                                                m461a(-97, 100);
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (ApduHelper.m571g(bArr)) {
                            bArr2 = ApduHelper.iR;
                        }
                    }
                    s = a;
                    ApduHelper.m570b(s);
                    if (this.lF != null) {
                        r = this.iJ.m559r(this.lF);
                        if (r == null) {
                            Log.m286e("PaymentProcessor", "processApdu- unable to get card object");
                            if (lz != null) {
                                lz.onFail(this.lF, -1);
                            }
                            bArr2 = ApduHelper.iR;
                        } else if (r.m578k(1)) {
                            bArr2 = r.ad().processApdu(bArr, bundle);
                            if (bArr2 != null) {
                                m465b(r, -98);
                                Log.m285d("APDU_LOG", ApduHelper.m567a(s, bArr2));
                                if (ApduHelper.hasError()) {
                                    Log.m285d("PaymentProcessor", "apdu processing failed run timer");
                                    m461a(-99, 1000);
                                } else if (ApduHelper.m569a(r, ApduHelper.m563Y().getCode())) {
                                    Log.m285d("PaymentProcessor", "Transaction sequence completed, run timer also");
                                    m461a(-97, 100);
                                }
                            } else {
                                bArr2 = ApduHelper.iR;
                            }
                        } else {
                            Log.m286e("PaymentProcessor", "this card cannot be used for nfc payment");
                            bArr2 = ApduHelper.iR;
                        }
                    } else {
                        Log.m286e("PaymentProcessor", "processApdu- mSelectCardTokenId is null");
                        if (lz == null) {
                            Log.m285d("PaymentProcessor", "pay callback is null");
                        } else {
                            lz.onFail(this.lF, -5);
                        }
                        bArr2 = ApduHelper.iR;
                    }
                } else if (State.m656p(24320) || ApduHelper.m571g(bArr)) {
                    Log.m285d("PaymentProcessor", "should stop nfc payment");
                    bArr2 = ApduHelper.iS;
                } else {
                    Log.m285d("PaymentProcessor", "cannot make nfc payment, status not satisfied");
                    Log.m290w("PaymentProcessor", "Abort NFC payment, not authenticated yet, notify app");
                    Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
                    intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_TAP_N_GO_STATE);
                    if (this.lF != null) {
                        intent.putExtra(PaymentFramework.EXTRA_TOKEN_ID, this.lF);
                    }
                    intent.putExtra(CardMaster.COL_STATUS, -35);
                    PaymentFrameworkApp.m315a(intent);
                    bArr2 = ApduHelper.iS;
                }
            }
        }
        return bArr2;
    }

    private void m461a(int i, long j) {
        if (this.lE == null) {
            Log.m285d("PaymentProcessor", "mNFCTimer is null. Scheduling NFCTimerTask. Timestamp =  " + System.currentTimeMillis() + " reason: " + i);
        } else {
            Log.m285d("PaymentProcessor", "mNFCTimer is not null. Cancel and reschedule. Timestamp = " + System.currentTimeMillis() + " reason: " + i);
            this.lE.cancel();
        }
        TimerTask paymentProcessor = new PaymentProcessor(this, i);
        if (j > 0) {
            this.lE = new Timer();
            this.lE.schedule(paymentProcessor, j);
            return;
        }
        paymentProcessor.run();
    }

    private void m465b(Card card, int i) {
        if (card == null || card.ad() == null) {
            Log.m286e("PaymentProcessor", "start Nfc timer, card is null ");
        } else if (this.lD != null) {
            Log.m286e("PaymentProcessor", "NFC session timer already started ");
        } else {
            int a = m455a(card, this.lK);
            Log.m285d("PaymentProcessor", "startNFCSessionTimer mstTransmitTime " + a);
            Log.m285d("PaymentProcessor", "startTime " + this.startTime);
            long currentTimeMillis = ((long) a) - (System.currentTimeMillis() - this.startTime);
            Log.m285d("PaymentProcessor", "period " + currentTimeMillis);
            if (currentTimeMillis > 0) {
                TimerTask paymentProcessor = new PaymentProcessor(this, i);
                if (this.lD == null) {
                    Log.m285d("PaymentProcessor", "NFC session timer starts");
                } else {
                    Log.m285d("PaymentProcessor", "NFC session timer restarts");
                    this.lD.cancel();
                }
                this.lD = new Timer();
                this.lD.schedule(paymentProcessor, currentTimeMillis);
            }
        }
    }

    public synchronized void m474a(String str, ISelectCardCallback iSelectCardCallback, boolean z) {
        int i = -1;
        synchronized (this) {
            SelectCardResult secureObjectInputForPayment;
            Log.m285d("PaymentProcessor", "selectCard() - tokenId = " + str);
            if (State.m658r(2)) {
                bb();
                if (str == null || iSelectCardCallback == null || this.iJ == null) {
                    if (this.iJ == null) {
                        Log.m286e("PaymentProcessor", "selectCard  - Failed to initialize account");
                    } else if (str == null) {
                        Log.m286e("PaymentProcessor", "selectCard- invalid inputs: tokenId is null");
                        i = -5;
                    } else {
                        Log.m286e("PaymentProcessor", "selectCard- invalid inputs: callback object is null");
                        i = -5;
                    }
                    if (iSelectCardCallback != null) {
                        iSelectCardCallback.onFail(str, i);
                    } else {
                        Log.m286e("PaymentProcessor", "selectCard callback is null");
                    }
                    clearCard();
                } else {
                    Card M;
                    Card r = this.iJ.m559r(str);
                    if ((r == null || r.ac() == null || r.ac().aQ() == null) && Objects.equals(str, PlccTAController.CARD_BRAND_GIFT)) {
                        Log.m286e("PaymentProcessor", "Gift Card record in DB was null. Recreate");
                        M = GiftCardProcessor.m402n(this.mContext).m403M(null);
                    } else {
                        M = r;
                    }
                    if (M == null || M.ac() == null || M.ac().aQ() == null) {
                        i = -6;
                        if (M == null) {
                            Log.m286e("PaymentProcessor", "selectCard- unable to get card Object : " + str);
                        } else if (M.ac() == null) {
                            Log.m286e("PaymentProcessor", "selectCard- unable to get token: " + str);
                        } else if (M.ac().aQ() == null) {
                            Log.m286e("PaymentProcessor", "selectCard- unable to get token providerKey: " + str);
                            i = -4;
                        } else if (!TokenStatus.ACTIVE.equals(M.ac().getTokenStatus())) {
                            Log.m286e("PaymentProcessor", "selectCard- token is not active : " + M.ac().getTokenStatus());
                            i = -4;
                        }
                        iSelectCardCallback.onFail(str, i);
                        clearCard();
                    } else {
                        Log.m285d("PaymentProcessor", "selectCard- selectCard() - call Base PayProvider :" + str);
                        try {
                            secureObjectInputForPayment = M.ad().getSecureObjectInputForPayment(z);
                        } catch (Throwable e) {
                            Log.m284c("PaymentProcessor", e.getMessage(), e);
                            if (e.getErrorCode() == 2) {
                                iSelectCardCallback.onFail(str, -42);
                                secureObjectInputForPayment = null;
                            } else {
                                iSelectCardCallback.onFail(str, -41);
                                secureObjectInputForPayment = null;
                            }
                        } catch (Throwable th) {
                            clearCard();
                        }
                        this.lF = str;
                        if (secureObjectInputForPayment == null || !(secureObjectInputForPayment.getStatus() == 0 || secureObjectInputForPayment.getStatus() == 1)) {
                            iSelectCardCallback.onFail(str, -1);
                            Log.m286e("PaymentProcessor", "getSecureObjectInputForPayment-unable to get proper response");
                            clearCard();
                        } else {
                            int payConfigTransmitTime = M.ad().getPayConfigTransmitTime(false);
                            Log.m285d("PaymentProcessor", "mstTransmitTime : " + payConfigTransmitTime);
                            secureObjectInputForPayment.setMstTransmitTime(payConfigTransmitTime);
                            if (!(Utils.ao(this.mContext) && M.m578k(2))) {
                                secureObjectInputForPayment.setMstTransmitTime(30);
                            }
                            State.m657q(2);
                            iSelectCardCallback.onSuccess(str, secureObjectInputForPayment);
                        }
                    }
                }
            } else {
                Log.m286e("PaymentProcessor", "selectCard  - cannot change state");
                if (iSelectCardCallback != null) {
                    iSelectCardCallback.onFail(str, -4);
                } else {
                    Log.m286e("PaymentProcessor", "selectCard callback is null");
                }
            }
        }
    }

    public synchronized void m473a(com.samsung.android.spayfw.appinterface.SecuredObject r7, com.samsung.android.spayfw.appinterface.PayConfig r8, com.samsung.android.spayfw.appinterface.IPayCallback r9, boolean r10) {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Exception block dominator not found, method:com.samsung.android.spayfw.core.a.n.a(com.samsung.android.spayfw.appinterface.SecuredObject, com.samsung.android.spayfw.appinterface.PayConfig, com.samsung.android.spayfw.appinterface.IPayCallback, boolean):void. bs: [B:11:0x0055, B:38:0x00a3, B:49:0x00ef]
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:86)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:57)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:52)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:281)
	at jadx.api.JavaClass.decompile(JavaClass.java:59)
	at jadx.api.JadxDecompiler$1.run(JadxDecompiler.java:161)
*/
        /*
        r6 = this;
        r4 = 2;
        r5 = 1;
        monitor-enter(r6);
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r1 = "startPay()";	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ all -> 0x0078 }
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0078 }
        r1.<init>();	 Catch:{ all -> 0x0078 }
        r2 = "startPay(): authAlive = ";	 Catch:{ all -> 0x0078 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0078 }
        r1 = r1.append(r10);	 Catch:{ all -> 0x0078 }
        r1 = r1.toString();	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ all -> 0x0078 }
        r6.bb();	 Catch:{ all -> 0x0078 }
        r0 = java.lang.System.currentTimeMillis();	 Catch:{ all -> 0x0078 }
        r6.startTime = r0;	 Catch:{ all -> 0x0078 }
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0078 }
        r1.<init>();	 Catch:{ all -> 0x0078 }
        r2 = "Start time = ";	 Catch:{ all -> 0x0078 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0078 }
        r2 = r6.startTime;	 Catch:{ all -> 0x0078 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0078 }
        r1 = r1.toString();	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);	 Catch:{ all -> 0x0078 }
        r0 = r6.lF;	 Catch:{ all -> 0x0078 }
        if (r0 == 0) goto L_0x0053;	 Catch:{ all -> 0x0078 }
    L_0x0049:
        if (r10 != 0) goto L_0x004d;	 Catch:{ all -> 0x0078 }
    L_0x004b:
        if (r7 == 0) goto L_0x0053;	 Catch:{ all -> 0x0078 }
    L_0x004d:
        if (r9 == 0) goto L_0x0053;	 Catch:{ all -> 0x0078 }
    L_0x004f:
        r0 = r6.iJ;	 Catch:{ all -> 0x0078 }
        if (r0 != 0) goto L_0x0099;
    L_0x0053:
        if (r9 == 0) goto L_0x006b;
    L_0x0055:
        r0 = r6.lF;	 Catch:{ all -> 0x0073 }
        r1 = -5;	 Catch:{ all -> 0x0073 }
        r9.onFail(r0, r1);	 Catch:{ all -> 0x0073 }
    L_0x005b:
        r0 = r6.lF;	 Catch:{ all -> 0x0073 }
        if (r0 != 0) goto L_0x007b;	 Catch:{ all -> 0x0073 }
    L_0x005f:
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0073 }
        r1 = "startPay- invalid inputs: mSelectCardTokenId is null";	 Catch:{ all -> 0x0073 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x0073 }
    L_0x0066:
        r6.clearCard();	 Catch:{ all -> 0x0078 }
    L_0x0069:
        monitor-exit(r6);
        return;
    L_0x006b:
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0073 }
        r1 = "pay callback is null";	 Catch:{ all -> 0x0073 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x0073 }
        goto L_0x005b;
    L_0x0073:
        r0 = move-exception;
        r6.clearCard();	 Catch:{ all -> 0x0078 }
        throw r0;	 Catch:{ all -> 0x0078 }
    L_0x0078:
        r0 = move-exception;
        monitor-exit(r6);
        throw r0;
    L_0x007b:
        if (r7 != 0) goto L_0x0085;
    L_0x007d:
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0073 }
        r1 = "startPay- invalid inputs: secObj is null";	 Catch:{ all -> 0x0073 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x0073 }
        goto L_0x0066;	 Catch:{ all -> 0x0073 }
    L_0x0085:
        r0 = r6.iJ;	 Catch:{ all -> 0x0073 }
        if (r0 != 0) goto L_0x0091;	 Catch:{ all -> 0x0073 }
    L_0x0089:
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0073 }
        r1 = "startPay- invalid inputs: Account is null";	 Catch:{ all -> 0x0073 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x0073 }
        goto L_0x0066;	 Catch:{ all -> 0x0073 }
    L_0x0091:
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0073 }
        r1 = "startPay- invalid inputs";	 Catch:{ all -> 0x0073 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x0073 }
        goto L_0x0066;
    L_0x0099:
        r0 = r6.iJ;	 Catch:{ all -> 0x0078 }
        r1 = r6.lF;	 Catch:{ all -> 0x0078 }
        r1 = r0.m559r(r1);	 Catch:{ all -> 0x0078 }
        if (r1 != 0) goto L_0x00b9;
    L_0x00a3:
        r0 = r6.lF;	 Catch:{ all -> 0x00b4 }
        r1 = -6;	 Catch:{ all -> 0x00b4 }
        r9.onFail(r0, r1);	 Catch:{ all -> 0x00b4 }
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x00b4 }
        r1 = "startPay- unable to get card Object";	 Catch:{ all -> 0x00b4 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x00b4 }
        r6.clearCard();	 Catch:{ all -> 0x0078 }
        goto L_0x0069;	 Catch:{ all -> 0x0078 }
    L_0x00b4:
        r0 = move-exception;	 Catch:{ all -> 0x0078 }
        r6.clearCard();	 Catch:{ all -> 0x0078 }
        throw r0;	 Catch:{ all -> 0x0078 }
    L_0x00b9:
        r0 = r1.ac();	 Catch:{ all -> 0x0078 }
        if (r0 == 0) goto L_0x0106;	 Catch:{ all -> 0x0078 }
    L_0x00bf:
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0078 }
        r2.<init>();	 Catch:{ all -> 0x0078 }
        r3 = "Token Status = ";	 Catch:{ all -> 0x0078 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0078 }
        r3 = r1.ac();	 Catch:{ all -> 0x0078 }
        r3 = r3.getTokenStatus();	 Catch:{ all -> 0x0078 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0078 }
        r2 = r2.toString();	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r2);	 Catch:{ all -> 0x0078 }
        r0 = r1.ac();	 Catch:{ all -> 0x0078 }
        r0 = r0.getTokenStatus();	 Catch:{ all -> 0x0078 }
        r2 = "ACTIVE";	 Catch:{ all -> 0x0078 }
        r0 = java.util.Objects.equals(r0, r2);	 Catch:{ all -> 0x0078 }
        if (r0 != 0) goto L_0x0106;
    L_0x00ef:
        r0 = r6.lF;	 Catch:{ all -> 0x0101 }
        r1 = -4;	 Catch:{ all -> 0x0101 }
        r9.onFail(r0, r1);	 Catch:{ all -> 0x0101 }
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0101 }
        r1 = "startPay- Token Status is not Active";	 Catch:{ all -> 0x0101 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x0101 }
        r6.clearCard();	 Catch:{ all -> 0x0078 }
        goto L_0x0069;	 Catch:{ all -> 0x0078 }
    L_0x0101:
        r0 = move-exception;	 Catch:{ all -> 0x0078 }
        r6.clearCard();	 Catch:{ all -> 0x0078 }
        throw r0;	 Catch:{ all -> 0x0078 }
    L_0x0106:
        r0 = com.samsung.android.spayfw.core.PaymentFrameworkApp.az();	 Catch:{ all -> 0x0078 }
        r2 = 1;	 Catch:{ all -> 0x0078 }
        r0.m619c(r2);	 Catch:{ all -> 0x0078 }
        r0 = r6.lF;	 Catch:{ all -> 0x0078 }
        r2 = "GIFT";	 Catch:{ all -> 0x0078 }
        r0 = java.util.Objects.equals(r0, r2);	 Catch:{ all -> 0x0078 }
        if (r0 != 0) goto L_0x012a;	 Catch:{ all -> 0x0078 }
    L_0x0118:
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r2 = "Not Gift Card";	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r2);	 Catch:{ all -> 0x0078 }
        r0 = r6.mContext;	 Catch:{ Exception -> 0x01ae }
        r0 = com.samsung.android.spayfw.core.MstConfigurationManager.m604j(r0);	 Catch:{ Exception -> 0x01ae }
        r2 = r6.lF;	 Catch:{ Exception -> 0x01ae }
        r0.m606B(r2);	 Catch:{ Exception -> 0x01ae }
    L_0x012a:
        r6.lK = r8;	 Catch:{ all -> 0x0078 }
        if (r8 != 0) goto L_0x01ba;	 Catch:{ all -> 0x0078 }
    L_0x012e:
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r2 = "startPay: using default payConfig from PF";	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r2);	 Catch:{ all -> 0x0078 }
        r0 = "default";	 Catch:{ all -> 0x0078 }
        r2 = r1.getCardBrand();	 Catch:{ all -> 0x0078 }
        r0 = com.samsung.android.spayfw.core.PayConfigurator.m612b(r0, r2);	 Catch:{ all -> 0x0078 }
    L_0x013f:
        r2 = new com.samsung.android.spayfw.storage.models.PaymentDetailsRecord;	 Catch:{ all -> 0x0078 }
        r2.<init>();	 Catch:{ all -> 0x0078 }
        r6.lf = r2;	 Catch:{ all -> 0x0078 }
        if (r10 == 0) goto L_0x0153;	 Catch:{ all -> 0x0078 }
    L_0x0148:
        r2 = r6.lf;	 Catch:{ all -> 0x0078 }
        if (r2 == 0) goto L_0x0153;	 Catch:{ all -> 0x0078 }
    L_0x014c:
        r2 = r6.lf;	 Catch:{ all -> 0x0078 }
        r3 = "ATTEMPTED";	 Catch:{ all -> 0x0078 }
        r2.setBarcodeAttempted(r3);	 Catch:{ all -> 0x0078 }
    L_0x0153:
        r6.lI = r9;	 Catch:{ all -> 0x0078 }
        r2 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0078 }
        r3.<init>();	 Catch:{ all -> 0x0078 }
        r4 = "Card presentation mode ";	 Catch:{ all -> 0x0078 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0078 }
        r4 = r1.ab();	 Catch:{ all -> 0x0078 }
        r3 = r3.append(r4);	 Catch:{ all -> 0x0078 }
        r3 = r3.toString();	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m285d(r2, r3);	 Catch:{ all -> 0x0078 }
        r2 = r6.mContext;	 Catch:{ all -> 0x0078 }
        r2 = com.samsung.android.spayfw.utils.Utils.ao(r2);	 Catch:{ all -> 0x0078 }
        if (r2 == 0) goto L_0x0180;	 Catch:{ all -> 0x0078 }
    L_0x0179:
        r2 = 2;	 Catch:{ all -> 0x0078 }
        r2 = r1.m578k(r2);	 Catch:{ all -> 0x0078 }
        if (r2 != 0) goto L_0x0190;	 Catch:{ all -> 0x0078 }
    L_0x0180:
        r0 = new com.samsung.android.spayfw.appinterface.PayConfig;	 Catch:{ all -> 0x0078 }
        r0.<init>();	 Catch:{ all -> 0x0078 }
        r2 = 1;	 Catch:{ all -> 0x0078 }
        r0.setPayType(r2);	 Catch:{ all -> 0x0078 }
        r2 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r3 = "MST is not supported";	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m289v(r2, r3);	 Catch:{ all -> 0x0078 }
    L_0x0190:
        r2 = r1.ad();	 Catch:{ all -> 0x0078 }
        r3 = new com.samsung.android.spayfw.core.a.n$b;	 Catch:{ all -> 0x0078 }
        r3.<init>(r6, r9);	 Catch:{ all -> 0x0078 }
        r2.startPay(r0, r7, r3, r10);	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.core.ApduHelper.m564Z();	 Catch:{ all -> 0x0078 }
        r0 = r0.getPayType();	 Catch:{ all -> 0x0078 }
        if (r0 != r5) goto L_0x01e3;	 Catch:{ all -> 0x0078 }
    L_0x01a5:
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r1 = "NFC only Payment";	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m289v(r0, r1);	 Catch:{ all -> 0x0078 }
        goto L_0x0069;	 Catch:{ all -> 0x0078 }
    L_0x01ae:
        r0 = move-exception;	 Catch:{ all -> 0x0078 }
        r2 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r3 = r0.getMessage();	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m284c(r2, r3, r0);	 Catch:{ all -> 0x0078 }
        goto L_0x012a;	 Catch:{ all -> 0x0078 }
    L_0x01ba:
        r0 = r8.getPayType();	 Catch:{ all -> 0x0078 }
        if (r0 != r4) goto L_0x01d9;	 Catch:{ all -> 0x0078 }
    L_0x01c0:
        r0 = r8.getMstPayConfig();	 Catch:{ all -> 0x0078 }
        if (r0 != 0) goto L_0x01d9;	 Catch:{ all -> 0x0078 }
    L_0x01c6:
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r2 = "startPay: type MST but mstPayConfig is NULL, using default payConfig from PF";	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r2);	 Catch:{ all -> 0x0078 }
        r0 = "default";	 Catch:{ all -> 0x0078 }
        r2 = r1.getCardBrand();	 Catch:{ all -> 0x0078 }
        r0 = com.samsung.android.spayfw.core.PayConfigurator.m612b(r0, r2);	 Catch:{ all -> 0x0078 }
        goto L_0x013f;	 Catch:{ all -> 0x0078 }
    L_0x01d9:
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r2 = "startPay: using payConfig from APP";	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r2);	 Catch:{ all -> 0x0078 }
        r0 = r8;	 Catch:{ all -> 0x0078 }
        goto L_0x013f;	 Catch:{ all -> 0x0078 }
    L_0x01e3:
        r0 = lC;	 Catch:{ all -> 0x0078 }
        if (r0 == 0) goto L_0x01f6;	 Catch:{ all -> 0x0078 }
    L_0x01e7:
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r2 = "Cancel CountDown Timer";	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r2);	 Catch:{ all -> 0x0078 }
        r0 = lC;	 Catch:{ all -> 0x0078 }
        r0.cancel();	 Catch:{ all -> 0x0078 }
        r0 = 0;	 Catch:{ all -> 0x0078 }
        lC = r0;	 Catch:{ all -> 0x0078 }
    L_0x01f6:
        r0 = lC;	 Catch:{ all -> 0x0078 }
        if (r0 != 0) goto L_0x0069;	 Catch:{ all -> 0x0078 }
    L_0x01fa:
        r0 = r6.lK;	 Catch:{ all -> 0x0078 }
        r1 = r6.m455a(r1, r0);	 Catch:{ all -> 0x0078 }
        r0 = "PaymentProcessor";	 Catch:{ all -> 0x0078 }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0078 }
        r2.<init>();	 Catch:{ all -> 0x0078 }
        r3 = "StartPayCountDownTimer : ";	 Catch:{ all -> 0x0078 }
        r2 = r2.append(r3);	 Catch:{ all -> 0x0078 }
        r2 = r2.append(r1);	 Catch:{ all -> 0x0078 }
        r2 = r2.toString();	 Catch:{ all -> 0x0078 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r2);	 Catch:{ all -> 0x0078 }
        r0 = new com.samsung.android.spayfw.core.a.n$d;	 Catch:{ all -> 0x0078 }
        r2 = (long) r1;	 Catch:{ all -> 0x0078 }
        r4 = 5000; // 0x1388 float:7.006E-42 double:2.4703E-320;	 Catch:{ all -> 0x0078 }
        r1 = r6;	 Catch:{ all -> 0x0078 }
        r0.<init>(r1, r2, r4);	 Catch:{ all -> 0x0078 }
        lC = r0;	 Catch:{ all -> 0x0078 }
        r0 = lC;	 Catch:{ all -> 0x0078 }
        r0.start();	 Catch:{ all -> 0x0078 }
        goto L_0x0069;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.core.a.n.a(com.samsung.android.spayfw.appinterface.SecuredObject, com.samsung.android.spayfw.appinterface.PayConfig, com.samsung.android.spayfw.appinterface.IPayCallback, boolean):void");
    }

    private int m455a(Card card, PayConfig payConfig) {
        int payConfigTransmitTime;
        if (payConfig == null || (payConfig.getPayType() == 2 && payConfig.getMstTransmitTime() == 0)) {
            payConfigTransmitTime = card.ad().getPayConfigTransmitTime(false);
            if (!(Utils.ao(this.mContext) && card.m578k(2))) {
                payConfigTransmitTime = 30;
            }
            payConfigTransmitTime *= LocationStatusCodes.GEOFENCE_NOT_AVAILABLE;
        } else if (payConfig.getPayType() == 1 && payConfig.getMstTransmitTime() == 0) {
            payConfigTransmitTime = 30000;
        } else {
            payConfigTransmitTime = payConfig.getMstTransmitTime();
        }
        Log.m285d("PaymentProcessor", "mstTransmitTime : " + payConfigTransmitTime);
        return payConfigTransmitTime;
    }

    public synchronized int retryPay(PayConfig payConfig) {
        int i;
        Log.m285d("PaymentProcessor", "retryPay()");
        this.lJ = true;
        if (this.lH != null) {
            Log.m285d("PaymentProcessor", "Cancel Retry Timer");
            this.lH.cancel();
            this.lH = null;
        }
        if (lC != null) {
            Log.m285d("PaymentProcessor", "Cancel CountDown Timer");
            lC.cancel();
            lC = null;
        }
        bb();
        if (this.iJ == null) {
            Log.m286e("PaymentProcessor", "retryPay- Account is null");
            i = -4;
        } else {
            this.startTime = System.currentTimeMillis();
            Log.m287i("PaymentProcessor", "Start time = " + this.startTime);
            if (this.lF == null) {
                Log.m286e("PaymentProcessor", "retryPay- invalid inputs");
                i = -44;
            } else {
                Card r = this.iJ.m559r(this.lF);
                if (r == null) {
                    try {
                        Log.m286e("PaymentProcessor", "retryPay- unable to get card Object");
                        i = -6;
                    } finally {
                        clearCard();
                    }
                } else {
                    PayConfig b;
                    PaymentFrameworkApp.az().m619c(true);
                    this.lK = payConfig;
                    if (payConfig == null) {
                        Log.m285d("PaymentProcessor", "retryPay: using default retry seq from PF");
                        b = PayConfigurator.m612b("retry1", r.getCardBrand());
                    } else if (payConfig.getPayType() == 2 && payConfig.getMstPayConfig() == null) {
                        Log.m285d("PaymentProcessor", "retryPay: type MST but mstPayConfig is NULL, using default payConfig from PF");
                        b = PayConfigurator.m612b("retry1", r.getCardBrand());
                    } else {
                        b = payConfig;
                    }
                    Log.m285d("PaymentProcessor", "Card presentation mode " + r.ab());
                    if (!r.m578k(2)) {
                        b = new PayConfig();
                        b.setPayType(1);
                        Log.m289v("PaymentProcessor", "MST is not supported");
                    }
                    if (PayConfigurator.aw() == 2 && State.aN()) {
                        r.ad().forceQuitMst();
                        if (!State.m657q(1)) {
                            Log.m286e("PaymentProcessor", "Cannot goto Npay ready");
                        }
                        if (!State.m657q(2)) {
                            Log.m286e("PaymentProcessor", "Cannot goto Selected State");
                        }
                    }
                    i = r.ad().retryPay(b);
                    ApduHelper.m564Z();
                    PaymentFrameworkApp.az().postAtFrontOfQueue(new PaymentProcessor(this, r, this.lK));
                }
            }
        }
        return i;
    }

    public synchronized void m472a(ICommonCallback iCommonCallback) {
        Log.m285d("PaymentProcessor", "stopPay()");
        bb();
        if (iCommonCallback == null) {
            Log.m286e("PaymentProcessor", "stopPay- invalid inputs: callback is null");
        } else {
            if (this.lH != null) {
                Log.m285d("PaymentProcessor", "Cancel Retry Timer");
                this.lH.cancel();
                this.lH = null;
            }
            if (this.lD != null) {
                Log.m285d("PaymentProcessor", "Cancel mNFCSessionTimer Timer");
                this.lD.cancel();
                this.lD = null;
            }
            if (this.lE != null) {
                this.lE.cancel();
                this.lE = null;
            }
            if (lC != null) {
                Log.m285d("PaymentProcessor", "Cancel CountDown Timer");
                lC.cancel();
                lC = null;
            }
            if (State.m656p(1)) {
                Log.m286e("PaymentProcessor", "Nothing to Stop");
                if (this.lI != null) {
                    this.lI.onFail(this.lF, -7);
                    this.lI = null;
                }
                iCommonCallback.onSuccess(this.lF);
            } else if (!State.m658r(24320) || !State.m658r(PKIFailureInfo.certConfirmed)) {
                Log.m286e("PaymentProcessor", "state cannot be changed");
                iCommonCallback.onFail(this.lF, -4);
            } else if (this.lF == null) {
                iCommonCallback.onFail(this.lF, -4);
                Log.m286e("PaymentProcessor", "stopPay- mSelectCardTokenId is null");
            } else if (this.iJ == null) {
                iCommonCallback.onFail(this.lF, -4);
                Log.m286e("PaymentProcessor", "stopPay- mAccount is null");
            } else {
                Card r = this.iJ.m559r(this.lF);
                if (r == null) {
                    iCommonCallback.onFail(this.lF, -6);
                    Log.m286e("PaymentProcessor", "stopPay- unable to get card Object");
                    this.lF = null;
                } else {
                    String str = this.lF;
                    if (State.m656p(X509KeyUsage.digitalSignature)) {
                        r.ad().stopInAppPay(this.lF, iCommonCallback);
                    } else {
                        if (this.lJ) {
                            if (this.lf != null && State.m656p(72)) {
                                this.lf.setMstRetryCancelled("CANCELLED");
                            }
                        } else if (this.lf != null && State.m656p(72)) {
                            this.lf.setMstCancelled("CANCELLED");
                        }
                        r.ad().stopPay();
                        if (State.m656p(SkeinMac.SKEIN_512)) {
                            Log.m285d("PaymentProcessor", "NFC_USER_CANCELLED");
                            m476u(-98);
                        }
                        iCommonCallback.onSuccess(str);
                    }
                }
            }
        }
    }

    public synchronized void clearPay() {
        bb();
        if (lA) {
            NfcControllerWrapper.ar(this.mContext);
            NfcControllerWrapper.fi();
            lA = false;
        }
        if (this.lD != null) {
            this.lD.cancel();
            this.lD = null;
        }
        if (this.lE != null) {
            this.lE.cancel();
            this.lE = null;
        }
        ApduHelper.m564Z();
        PaymentFrameworkApp.az().m619c(false);
        State.m657q(1);
        this.lJ = false;
        if (!(this.lF == null || this.iJ == null)) {
            Card r = this.iJ.m559r(this.lF);
            if (r == null) {
                Log.m286e("PaymentProcessor", "clearPay: unable to get card Object");
            } else {
                Log.m285d("PaymentProcessor", "clearPay(): mSelectCardTokenId = " + this.lF);
                r.ad().clearPay();
                if (this.lf != null) {
                    this.lf.setPaymentType(Card.m574y(r.getCardBrand()));
                }
            }
        }
        if (this.lf != null) {
            this.lf.setTrTokenId(this.lF);
            this.lf.setRscAttemptRequestId(MstConfigurationManager.m604j(this.mContext).getRscAttemptRequestId());
            m469d(this.lf);
        }
        this.lF = null;
        this.lK = null;
    }

    public synchronized void clearRetryPay() {
        this.lH = null;
        bb();
        if (lA) {
            NfcControllerWrapper.ar(this.mContext);
            NfcControllerWrapper.fi();
            lA = false;
        }
        if (this.lD != null) {
            this.lD.cancel();
            this.lD = null;
        }
        if (this.lE != null) {
            this.lE.cancel();
            this.lE = null;
        }
        if (lC != null) {
            lC.cancel();
            lC = null;
        }
        PaymentFrameworkApp.az().m619c(false);
        State.m657q(1);
        this.lJ = false;
        if (!(this.lF == null || this.iJ == null)) {
            Card r = this.iJ.m559r(this.lF);
            if (r == null) {
                Log.m286e("PaymentProcessor", "clearRetryPay: unable to get card Object");
            } else {
                Log.m285d("PaymentProcessor", "clearRetryPay(): mSelectCardTokenId = " + this.lF);
                r.ad().clearRetryPay();
                if (this.lf != null) {
                    this.lf.setPaymentType(Card.m574y(r.getCardBrand()));
                }
            }
        }
        if (this.lf != null) {
            this.lf.setTrTokenId(this.lF);
            this.lf.setRscAttemptRequestId(MstConfigurationManager.m604j(this.mContext).getRscAttemptRequestId());
            m469d(this.lf);
        }
        this.lF = null;
        this.lK = null;
    }

    public void startInAppPay(SecuredObject securedObject, InAppTransactionInfo inAppTransactionInfo, IInAppPayCallback iInAppPayCallback) {
        Log.m285d("PaymentProcessor", "startInAppPay()");
        if (this.lF == null || securedObject == null || iInAppPayCallback == null || inAppTransactionInfo == null) {
            if (!(iInAppPayCallback == null || inAppTransactionInfo == null)) {
                try {
                    iInAppPayCallback.onFail(inAppTransactionInfo.getContextId(), -5);
                } catch (Throwable th) {
                    clearCard();
                }
            }
            if (this.lF == null) {
                Log.m286e("PaymentProcessor", "startInAppPay- invalid inputs: mSelectCardTokenId is null");
            } else if (securedObject == null) {
                Log.m286e("PaymentProcessor", "startInAppPay- invalid inputs: secObj is null");
            } else if (inAppTransactionInfo == null) {
                Log.m286e("PaymentProcessor", "startInAppPay- invalid inputs: txnInfo is null");
            } else {
                Log.m286e("PaymentProcessor", "startInAppPay- invalid inputs: callback is null");
            }
            clearCard();
            return;
        }
        Card r = this.iJ.m559r(this.lF);
        if (r == null) {
            try {
                iInAppPayCallback.onFail(inAppTransactionInfo.getContextId(), -6);
                Log.m286e("PaymentProcessor", "startInAppPay- unable to get card Object");
            } finally {
                clearCard();
            }
        } else {
            this.lf = new PaymentDetailsRecord();
            if (r.ac() != null) {
                if (this.lf != null) {
                    this.lf.setTrTokenId(this.lF);
                    this.lf.setInAppTransactionInfo(m458a(inAppTransactionInfo));
                    m469d(this.lf);
                }
                Log.m285d("PaymentProcessor", "Token Status = " + r.ac().getTokenStatus());
                if (!Objects.equals(r.ac().getTokenStatus(), TokenStatus.ACTIVE)) {
                    try {
                        iInAppPayCallback.onFail(inAppTransactionInfo.getContextId(), -4);
                        Log.m286e("PaymentProcessor", "startInAppPay- Token Status is not Active");
                        return;
                    } finally {
                        clearCard();
                    }
                }
            }
            r.ad().startInAppPay(securedObject, inAppTransactionInfo, iInAppPayCallback);
        }
    }

    private void bb() {
        if (this.iJ == null) {
            this.iJ = Account.m551a(this.mContext, null);
        }
    }

    private synchronized ApduReasonCode m457a(int i, Card card) {
        ApduReasonCode Y;
        int i2 = 1;
        synchronized (this) {
            try {
                Log.m285d("PaymentProcessor", "onDeactivatedInternal: reason is " + i);
                ApduReasonCode Y2 = ApduHelper.m563Y();
                Log.m285d("PaymentProcessor", "onDeactivatedInternal: arc error is " + Y2.getCode());
                if (Y2.getCode() != -28672) {
                    i2 = 4;
                }
                Bundle processTransacionComplete = card.ad().processTransacionComplete(i2);
                short s = processTransacionComplete.getShort("nfcApduErrorCode");
                this.lG = processTransacionComplete;
                ApduHelper.m568a(s);
                Y = ApduHelper.m563Y();
                Y.setExtraData(m467d(processTransacionComplete));
                Log.m285d("PaymentProcessor", "onDeactivated: error command is " + s);
                if (i == -98) {
                    Log.m285d("PaymentProcessor", "reason = NFC_USER_CANCELLED");
                    if (lz != null) {
                        lz.onFail(this.lF, -7);
                        lz = null;
                    } else {
                        Log.m286e("PaymentProcessor", "onDeactivated: pay call back is null");
                    }
                } else if (i == -96) {
                    Log.m285d("PaymentProcessor", "reason = NFC_ERROR_USAGE_TIMEOUT");
                    if (lz != null) {
                        lz.onFinish(this.lF, 1, Y);
                        lz = null;
                    } else {
                        Log.m286e("PaymentProcessor", "onDeactivated: pay call back is null");
                    }
                } else if (Y.getCommand() != 2) {
                    Log.m285d("PaymentProcessor", "going back to mst");
                    if (!Utils.ao(this.mContext) || !card.m578k(2) || !card.ad().isMstThreadStarted()) {
                        Log.m285d("PaymentProcessor", "Do not switch to MST");
                    } else if (State.m657q(8)) {
                        NfcControllerWrapper.ar(this.mContext);
                        NfcControllerWrapper.fj();
                        lA = true;
                    } else {
                        Log.m285d("PaymentProcessor", "NFC -> MST fails, is stopping pay called?");
                    }
                } else if (lz != null) {
                    lz.onFinish(this.lF, 1, Y);
                    lz = null;
                } else {
                    Log.m286e("PaymentProcessor", "onDeactivated: pay call back is null");
                }
                if (this.lJ) {
                    if (this.lf != null) {
                        if (Y.getCommand() == 2) {
                            this.lf.setNfcRetryCompleted("SUCCESSFUL");
                        } else {
                            this.lf.setNfcRetryCompleted("FAILED");
                        }
                    }
                } else if (this.lf != null) {
                    if (Y.getCommand() == 2) {
                        this.lf.setNfcCompleted("SUCCESSFUL");
                    } else {
                        this.lf.setNfcCompleted("FAILED");
                    }
                }
                card.ad().shouldTransitBackToMst();
                if (!State.m656p(8)) {
                    if (lz != null) {
                        Log.m285d("PaymentProcessor", "No MST, done");
                        lz.onFinish(this.lF, 1, ApduHelper.m563Y());
                        lz = null;
                    } else {
                        Log.m286e("PaymentProcessor", "onDeactivated: pay call back is null");
                    }
                    clearPay();
                }
            } catch (Throwable th) {
                card.ad().shouldTransitBackToMst();
                if (!State.m656p(8)) {
                    if (lz != null) {
                        Log.m285d("PaymentProcessor", "No MST, done");
                        lz.onFinish(this.lF, 1, ApduHelper.m563Y());
                        lz = null;
                    } else {
                        Log.m286e("PaymentProcessor", "onDeactivated: pay call back is null");
                    }
                    clearPay();
                }
            }
        }
        return Y;
    }

    private void m469d(PaymentDetailsRecord paymentDetailsRecord) {
        if (paymentDetailsRecord == null) {
            Log.m290w("PaymentProcessor", "mPaymentDetails is null, returning!");
            return;
        }
        Handler az = PaymentFrameworkApp.az();
        if (paymentDetailsRecord.getInAppTransactionInfo() == null) {
            paymentDetailsRecord.setElapsedTime(System.currentTimeMillis() - this.startTime);
        }
        if (az != null) {
            Log.m285d("PaymentProcessor", "Post PAYFW_OPT_ANALYTICS_REPORT request");
            az.sendMessage(PaymentFrameworkMessage.m620a(21, paymentDetailsRecord, null));
        } else {
            Log.m286e("PaymentProcessor", "HANDLER IS NOT INITIAILIZED");
        }
        TransactionDetailsRetryRequester.m679w(this.mContext).add(paymentDetailsRecord.getTrTokenId());
    }

    private InAppTransactionInfo m458a(InAppTransactionInfo inAppTransactionInfo) {
        InAppTransactionInfo inAppTransactionInfo2 = new InAppTransactionInfo();
        inAppTransactionInfo2.setContextId(inAppTransactionInfo.getContextId());
        inAppTransactionInfo2.setAmount(inAppTransactionInfo.getAmount());
        inAppTransactionInfo2.setCurrencyCode(inAppTransactionInfo.getCurrencyCode());
        inAppTransactionInfo2.setPid(inAppTransactionInfo.getPid());
        inAppTransactionInfo2.setMerchantRefId(inAppTransactionInfo.getMerchantRefId());
        inAppTransactionInfo2.setRecurring(inAppTransactionInfo.isRecurring());
        inAppTransactionInfo2.setFPANLast4Digits(inAppTransactionInfo.getFPANLast4Digits());
        return inAppTransactionInfo2;
    }

    public Card m471X() {
        bb();
        return this.iJ != null ? this.iJ.m554X() : null;
    }

    public void m475a(String str, String str2, ICommonCallback iCommonCallback) {
        Log.m285d("PaymentProcessor", "updateBinAttribute version [" + str + "], url [" + str2 + "]");
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2) && iCommonCallback != null) {
            BinAttribute.setServerBinVersion(str);
            iCommonCallback.onSuccess(BuildConfig.FLAVOR);
        } else if (iCommonCallback != null) {
            iCommonCallback.onFail(BuildConfig.FLAVOR, -5);
        }
    }

    private Bundle m467d(Bundle bundle) {
        Bundle bundle2 = null;
        if (bundle != null) {
            if (bundle.containsKey("tapNGotransactionErrorCode")) {
                int i = bundle.getInt("tapNGotransactionErrorCode");
                Log.m285d("PaymentProcessor", "bundle transactionErrorCode = " + i);
                if (null == null) {
                    bundle2 = new Bundle();
                }
                bundle2.putInt(CardMaster.COL_STATUS, i);
                bundle2.putString(PaymentFramework.EXTRA_TRANSACTION_TYPE, PaymentFramework.NOTIFICATION_TYPE_TAP_N_GO_STATE);
            }
            if (bundle.containsKey(TransactionStatus.EXTRA_PDOL_VALUES)) {
                Bundle bundle3 = bundle.getBundle(TransactionStatus.EXTRA_PDOL_VALUES);
                Log.m285d("PaymentProcessor", "bundle pdolValues = " + bundle3.toString());
                if (bundle2 == null) {
                    bundle2 = new Bundle();
                }
                bundle2.putBundle(TransactionStatus.EXTRA_PDOL_VALUES, bundle3);
            }
            if (bundle2 != null) {
                Log.m285d("PaymentProcessor", "extraData = " + bundle2.toString());
            }
        }
        return bundle2;
    }

    public void getInAppToken(String str, MerchantInfo merchantInfo, String str2, IInAppPayCallback iInAppPayCallback) {
        Log.m285d("PaymentProcessor", "getInAppToken()");
        if (this.lF == null || merchantInfo == null || iInAppPayCallback == null || str2 == null) {
            if (iInAppPayCallback != null) {
                try {
                    iInAppPayCallback.onFail(str, -5);
                } catch (Throwable th) {
                    clearCard();
                }
            }
            if (this.lF == null) {
                Log.m286e("PaymentProcessor", "getInAppToken- invalid inputs: mSelectCardTokenId is null");
            } else if (merchantInfo == null) {
                Log.m286e("PaymentProcessor", "getInAppToken- invalid inputs: merchantInfo is null");
            } else if (str2 == null) {
                Log.m286e("PaymentProcessor", "getInAppToken- invalid inputs: paymentPayloadJson is null");
            } else {
                Log.m286e("PaymentProcessor", "getInAppToken- invalid inputs: callback is null");
            }
            clearCard();
            return;
        }
        Card r = this.iJ.m559r(this.lF);
        if (r == null) {
            try {
                iInAppPayCallback.onFail(str, -6);
                Log.m286e("PaymentProcessor", "getInAppToken- unable to get card Object");
            } finally {
                clearCard();
            }
        } else if (r.ad() != null) {
            r.ad().getInAppToken(str, merchantInfo, str2, iInAppPayCallback);
        }
    }
}
