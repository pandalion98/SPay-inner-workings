package com.samsung.android.spayfw.payprovider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
import com.google.android.gms.common.ConnectionResult;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.ExtractLoyaltyCardDetailRequest;
import com.samsung.android.spayfw.appinterface.GiftCardDetail;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.GiftCardRegisterResponseData;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardDetail;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterRequestData;
import com.samsung.android.spayfw.appinterface.GlobalMembershipCardRegisterResponseData;
import com.samsung.android.spayfw.appinterface.ICommonCallback;
import com.samsung.android.spayfw.appinterface.IInAppPayCallback;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.InAppTransactionInfo;
import com.samsung.android.spayfw.appinterface.MstPayConfig;
import com.samsung.android.spayfw.appinterface.MstPayConfigEntry;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.appinterface.VerifyIdvInfo;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.MstConfigurationManager;
import com.samsung.android.spayfw.core.MstPayConfigHelper;
import com.samsung.android.spayfw.core.PayConfigurator;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.PaymentFrameworkRequester;
import com.samsung.android.spayfw.core.State;
import com.samsung.android.spayfw.core.hce.SPayHCEReceiver;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.MerchantServerRequester.MerchantInfo;
import com.samsung.android.spayfw.payprovider.TACounter.TACounter;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.ExtractCardDetailResult;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.payprovider.plcc.util.PlccConstants;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.commerce.PaymentRequest;
import com.samsung.android.spayfw.remoteservice.commerce.PaymentServiceClient;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentRequestData;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentRequestData.Payment;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentResponseData;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytui.AuthNonce;
import com.samsung.android.spaytui.AuthResult;
import com.samsung.android.spaytui.SpayTuiTAController;
import com.samsung.android.spaytzsvc.api.TAController;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.TimerTask;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;

public abstract class PaymentNetworkProvider {
    public static final String AUTHTYPE_BACKUPPASSWORD = "BACKUP PASSWORD";
    public static final String AUTHTYPE_FP = "FP";
    public static final String AUTHTYPE_IRIS = "IRIS";
    public static final String AUTHTYPE_NONE = "None";
    public static final String AUTHTYPE_TRUSTED_PIN = "PIN";
    private static final int CMD_ABORT_MST = 3;
    private static final int CMD_MOVE_SEC_OS_CORE4 = 5;
    private static final int CMD_MST_OFF = 2;
    private static final int CMD_MST_ON = 1;
    private static final int CMD_RESET_MST = 4;
    public static final int CREATE_TOKEN_SRC_IDV_RESPONSE = 3;
    public static final int CREATE_TOKEN_SRC_PROV_PUSH = 2;
    public static final int CREATE_TOKEN_SRC_PROV_RESPONSE = 1;
    public static final boolean DEBUG;
    private static final String FILE_MIGRATE_SEC_OS = "/sys/devices/system/sec_os_ctrl/migrate_os";
    private static final String FILE_MST_INTERRUPT = "/dev/mst_ctrl";
    private static final String FILE_MST_POWER_ON_OFF = "/sys/class/mstldo/mst_drv/transmit";
    private static final String LOG_TAG = "PaymentNetworkProvider";
    public static final long NFC_WAIT_TIME = 30000;
    private static final boolean TOAST_DEBUG = false;
    private static final String TUIPINSECUREOBJECTFILE = "mpt.dat";
    private static final String TUI_DATA_DIR;
    private static final long UNLOAD_TIMER_EXPIRY_TIME = 60000;
    private static SpayTuiTAController mAuthTAController;
    private static ProviderTokenKey mAuthTASelectedCard;
    private static Thread mMstPayThread;
    private static ConditionVariable mNfcWait;
    private static PayConfig mPayConfig;
    private static ProviderTokenKey mPayTASelectedCard;
    private static Object mPaymentModeObj;
    private static boolean mRetryMode;
    private static boolean mShouldInterrupt;
    private static Object mSwitchObj;
    private static TACounter mTACounterFactory;
    private boolean forceQuit;
    private String mAuthType;
    private String mCardBrand;
    protected Context mContext;
    protected String mEnrollmentId;
    private boolean mJwtRetryTriggered;
    private String mMstSequenceId;
    protected String mPFTokenStatus;
    private PayResponse mPayCallback;
    protected ProviderTokenKey mProviderTokenKey;
    private ICommonCallback mStopPayCallback;
    private String mStopPaySelectedCard;
    protected TAController mTAController;
    private TACounter mTACounter;
    private UnloadTimer mUnloadTimer;

    /* renamed from: com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.1 */
    class C04151 extends Thread {
        final /* synthetic */ PaymentNetworkProvider oO;

        C04151(PaymentNetworkProvider paymentNetworkProvider) {
            this.oO = paymentNetworkProvider;
        }

        public void run() {
            Log.m285d(PaymentNetworkProvider.LOG_TAG, "Wait for NFC.");
            PaymentNetworkProvider.mNfcWait = new ConditionVariable();
            if (!(PaymentNetworkProvider.mNfcWait.block(PaymentNetworkProvider.NFC_WAIT_TIME) || this.oO.mPayCallback == null)) {
                this.oO.mPayCallback.m447a(null, -46, -46, this.oO.mAuthType, null);
            }
            PaymentNetworkProvider.mNfcWait = null;
        }
    }

    public static class InAppDetailedTransactionInfo extends InAppTransactionInfo {
        private String merchantCertificate;
        private byte[] nonce;

        public String cd() {
            return this.merchantCertificate;
        }

        public void aq(String str) {
            this.merchantCertificate = str;
        }

        public byte[] getNonce() {
            return this.nonce;
        }

        public InAppDetailedTransactionInfo(InAppTransactionInfo inAppTransactionInfo, byte[] bArr) {
            this.nonce = null;
            setAmount(inAppTransactionInfo.getAmount());
            setCurrencyCode(inAppTransactionInfo.getCurrencyCode());
            setPid(inAppTransactionInfo.getPid());
            setContextId(inAppTransactionInfo.getContextId());
            this.merchantCertificate = null;
            this.nonce = bArr;
        }
    }

    /* renamed from: com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.a */
    private class C0416a implements Runnable {
        final /* synthetic */ PaymentNetworkProvider oO;
        int oP;
        int oQ;

        private C0416a(PaymentNetworkProvider paymentNetworkProvider) {
            this.oO = paymentNetworkProvider;
            this.oP = -36;
            this.oQ = 0;
        }

        private boolean ce() {
            synchronized (PaymentNetworkProvider.mSwitchObj) {
                if (State.m656p(32)) {
                    try {
                        PaymentNetworkProvider.mSwitchObj.wait();
                    } catch (Throwable e) {
                        Log.m285d(PaymentNetworkProvider.LOG_TAG, "switch obj wait interrupt exception");
                        Log.m284c(PaymentNetworkProvider.LOG_TAG, e.getMessage(), e);
                    }
                    if (State.m656p(8)) {
                        Log.m285d(PaymentNetworkProvider.LOG_TAG, "continue to transimit MST");
                        return true;
                    }
                }
                return PaymentNetworkProvider.TOAST_DEBUG;
            }
        }

        private boolean cf() {
            if (SPayHCEReceiver.aR()) {
                SPayHCEReceiver.aS();
                Log.m290w(PaymentNetworkProvider.LOG_TAG, "RF is detected");
            }
            return PaymentNetworkProvider.TOAST_DEBUG;
        }

        public void run() {
            if (cf()) {
                Log.m285d(PaymentNetworkProvider.LOG_TAG, "MST thread exits, not in payment state");
            } else if (this.oO.prepareMstPayInternal()) {
                int i = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
                try {
                    if (PaymentNetworkProvider.mPayConfig != null) {
                        i = PaymentNetworkProvider.mPayConfig.getPayIdleTime();
                    } else {
                        Log.m286e(PaymentNetworkProvider.LOG_TAG, "pay config is null");
                    }
                    PaymentNetworkProvider.mShouldInterrupt = true;
                    Log.m285d(PaymentNetworkProvider.LOG_TAG, "mst idle time=" + i);
                    Thread.sleep((long) i);
                    PaymentNetworkProvider.mShouldInterrupt = PaymentNetworkProvider.TOAST_DEBUG;
                } catch (InterruptedException e) {
                    PaymentNetworkProvider.mShouldInterrupt = PaymentNetworkProvider.TOAST_DEBUG;
                    Log.m287i(PaymentNetworkProvider.LOG_TAG, "interrupt MST because nfc is detected");
                }
                if (!(State.m657q(64) || State.m656p(72))) {
                    Log.m285d(PaymentNetworkProvider.LOG_TAG, "cannot start mst");
                    if (!ce()) {
                        Log.m285d(PaymentNetworkProvider.LOG_TAG, "MST thread exits");
                        return;
                    }
                }
                if (PaymentNetworkProvider.mPayConfig != null && PaymentNetworkProvider.mPayConfig.getPayType() == PaymentNetworkProvider.CREATE_TOKEN_SRC_PROV_PUSH && PaymentNetworkProvider.mPayConfig.getMstPayConfig() != null) {
                    MstPayConfig mstPayConfig;
                    MstPayConfig mstPayConfig2 = PaymentNetworkProvider.mPayConfig.getMstPayConfig();
                    if (PaymentNetworkProvider.mAuthTASelectedCard != null && PlccTAController.CARD_BRAND_GIFT.equals(PaymentNetworkProvider.mAuthTASelectedCard.cn())) {
                        Log.m285d(PaymentNetworkProvider.LOG_TAG, "Gift Card - Use Config from App");
                        mstPayConfig = mstPayConfig2;
                    } else if (PaymentNetworkProvider.mRetryMode) {
                        r1 = MstConfigurationManager.m604j(this.oO.mContext).al();
                        if (r1 == null || r1.getMstPayConfig() == null) {
                            r1 = this.oO.getPayConfig();
                            if (r1 == null || r1.getMstPayConfig() == null || r1.getMstPayConfig().getMstPayConfigEntry() == null || r1.getMstPayConfig().getMstPayConfigEntry().size() <= 0) {
                                mstPayConfig = mstPayConfig2;
                            } else {
                                Log.m285d(PaymentNetworkProvider.LOG_TAG, "SDK provides new pay configuration in Retry Mode");
                                mstPayConfig = r1.getMstPayConfig();
                            }
                        } else {
                            Log.m285d(PaymentNetworkProvider.LOG_TAG, "MstConfigurationManager provides new pay configuration in Retry Mode");
                            mstPayConfig2 = r1.getMstPayConfig();
                            this.oO.mMstSequenceId = MstConfigurationManager.m604j(this.oO.mContext).an();
                            Log.m285d(PaymentNetworkProvider.LOG_TAG, "Mst Sequence Id from RSC= " + this.oO.mMstSequenceId);
                            mstPayConfig = mstPayConfig2;
                        }
                    } else {
                        r1 = MstConfigurationManager.m604j(this.oO.mContext).ak();
                        if (r1 == null || r1.getMstPayConfig() == null) {
                            r1 = this.oO.getPayConfig();
                            if (!(r1 == null || r1.getMstPayConfig() == null || r1.getMstPayConfig().getMstPayConfigEntry() == null || r1.getMstPayConfig().getMstPayConfigEntry().size() <= 0)) {
                                Log.m285d(PaymentNetworkProvider.LOG_TAG, "SDK provides new pay configuration in Main Mode");
                                mstPayConfig2 = r1.getMstPayConfig();
                            }
                        } else {
                            Log.m285d(PaymentNetworkProvider.LOG_TAG, "MstConfigurationManager provides new pay configuration in Main Mode");
                            mstPayConfig2 = r1.getMstPayConfig();
                            this.oO.mMstSequenceId = MstConfigurationManager.m604j(this.oO.mContext).am();
                            Log.m285d(PaymentNetworkProvider.LOG_TAG, "Mst Sequence Id from RSC = " + this.oO.mMstSequenceId);
                        }
                        mstPayConfig = mstPayConfig2;
                    }
                    if (mstPayConfig.getMstPayConfigEntry() == null || mstPayConfig.getMstPayConfigEntry().size() <= 0) {
                        Log.m286e(PaymentNetworkProvider.LOG_TAG, "startPay:configuration is missing ");
                        if (this.oO.mPayCallback != null) {
                            this.oO.mPayCallback.m448a(null, -36, this.oO.mAuthType);
                            return;
                        } else {
                            Log.m286e(PaymentNetworkProvider.LOG_TAG, "pay callback is null");
                            return;
                        }
                    }
                    if (!State.m656p(72)) {
                        Log.m287i(PaymentNetworkProvider.LOG_TAG, " Mst stopped");
                        if (!ce()) {
                            Log.m285d(PaymentNetworkProvider.LOG_TAG, "MST thread exits before preparing MST");
                            return;
                        }
                    }
                    this.oQ = mstPayConfig.getMstPayConfigEntry().size();
                    if (cf()) {
                        Log.m285d(PaymentNetworkProvider.LOG_TAG, "MST thread exits, rf is detected");
                    } else if (this.oO.mTAController.makeSystemCall(PaymentNetworkProvider.CREATE_TOKEN_SRC_PROV_RESPONSE)) {
                        this.oO.mTAController.moveSecOsToCore4();
                        Object obj = null;
                        int i2 = PaymentNetworkProvider.CREATE_TOKEN_SRC_PROV_RESPONSE;
                        int i3 = 0;
                        Object obj2 = PaymentNetworkProvider.CREATE_TOKEN_SRC_PROV_RESPONSE;
                        while (i2 <= this.oQ) {
                            Object obj3;
                            Log.m287i(PaymentNetworkProvider.LOG_TAG, "MstPayloop: start: " + System.currentTimeMillis());
                            if (State.m656p(72)) {
                                Log.m287i(PaymentNetworkProvider.LOG_TAG, "startPayMst: count: " + i2);
                                MstPayConfigEntry mstPayConfigEntry = (MstPayConfigEntry) mstPayConfig.getMstPayConfigEntry().get(i3);
                                Log.m287i(PaymentNetworkProvider.LOG_TAG, "startPayMst: config: " + mstPayConfigEntry.toString());
                                byte[] a = MstPayConfigHelper.m607a(mstPayConfigEntry);
                                if (State.m656p(72)) {
                                    Log.m285d(PaymentNetworkProvider.LOG_TAG, "transmitMstPay: start: " + System.currentTimeMillis());
                                    boolean startMstPay = this.oO.startMstPay(mstPayConfigEntry.getBaudRate(), a);
                                    Log.m285d(PaymentNetworkProvider.LOG_TAG, "transmitMstPay: end: " + System.currentTimeMillis());
                                    if (!State.m656p(72)) {
                                        Log.m285d(PaymentNetworkProvider.LOG_TAG, " MstPayloop: end: after transmitMst: " + System.currentTimeMillis());
                                        if (ce()) {
                                            i2 = 0;
                                            i3 = -1;
                                            obj3 = obj2;
                                        } else {
                                            Log.m285d(PaymentNetworkProvider.LOG_TAG, "MST thread exits after transimiting MST");
                                            if (!this.oO.forceQuit) {
                                                this.oO.stopMstPayLocked(true);
                                                return;
                                            }
                                            return;
                                        }
                                    } else if (startMstPay) {
                                        Object obj4;
                                        Log.m285d(PaymentNetworkProvider.LOG_TAG, "startMstPay: success: count: " + i2);
                                        if (this.oO.mPayCallback != null) {
                                            Log.m285d(PaymentNetworkProvider.LOG_TAG, "onPay: start: " + System.currentTimeMillis());
                                            if (i2 == this.oQ && !PaymentNetworkProvider.mRetryMode && this.oO.allowPaymentRetry()) {
                                                Log.m285d(PaymentNetworkProvider.LOG_TAG, "Send Retry Callback");
                                                this.oO.mPayCallback.m449g(null);
                                                obj4 = PaymentNetworkProvider.CREATE_TOKEN_SRC_PROV_RESPONSE;
                                            } else {
                                                if (i2 == this.oQ) {
                                                    Log.m285d(PaymentNetworkProvider.LOG_TAG, "Send Finish Callback");
                                                    obj4 = null;
                                                } else {
                                                    obj4 = obj;
                                                }
                                                this.oO.mPayCallback.m447a(null, i2, this.oQ, this.oO.mAuthType, this.oO.mMstSequenceId);
                                            }
                                            Log.m285d(PaymentNetworkProvider.LOG_TAG, "onPay: end: " + System.currentTimeMillis());
                                        } else {
                                            Log.m286e(PaymentNetworkProvider.LOG_TAG, "pay callback is null");
                                            obj4 = obj;
                                        }
                                        if (i2 < this.oQ) {
                                            if (State.m656p(72)) {
                                                try {
                                                    Log.m285d(PaymentNetworkProvider.LOG_TAG, "next transmission will happen after :" + ((MstPayConfigEntry) mstPayConfig.getMstPayConfigEntry().get(i3)).getDelayBetweenRepeat() + "Ms");
                                                    Log.m285d(PaymentNetworkProvider.LOG_TAG, "current time before sleep in ms: " + System.currentTimeMillis());
                                                    Thread.sleep((long) ((MstPayConfigEntry) mstPayConfig.getMstPayConfigEntry().get(i3)).getDelayBetweenRepeat());
                                                    if (obj2 == null || !State.m656p(8)) {
                                                        Log.m285d(PaymentNetworkProvider.LOG_TAG, "current time after wake up in ms: " + System.currentTimeMillis() + "\n next transmission started after delay:");
                                                    } else {
                                                        i2 = 0;
                                                        i3 = -1;
                                                        obj = obj4;
                                                        obj3 = null;
                                                    }
                                                } catch (InterruptedException e2) {
                                                    Log.m290w(PaymentNetworkProvider.LOG_TAG, "premature wake up from sleep: currentTime " + System.currentTimeMillis());
                                                }
                                            } else if (ce()) {
                                                Log.m285d(PaymentNetworkProvider.LOG_TAG, "restart MST when sleeping");
                                                i2 = 0;
                                                i3 = -1;
                                                obj = obj4;
                                                obj3 = obj2;
                                            } else {
                                                Log.m285d(PaymentNetworkProvider.LOG_TAG, "MST thread exits when sleeping");
                                                if (!this.oO.forceQuit) {
                                                    this.oO.stopMstPayLocked(true);
                                                    return;
                                                }
                                                return;
                                            }
                                        }
                                        Log.m285d(PaymentNetworkProvider.LOG_TAG, "MstPayloop: end: " + System.currentTimeMillis());
                                        obj = obj4;
                                        obj3 = obj2;
                                    } else {
                                        Log.m286e(PaymentNetworkProvider.LOG_TAG, "startPayMst: transmission error ");
                                        Log.m285d(PaymentNetworkProvider.LOG_TAG, "stopMstPay start:  currentTime " + System.currentTimeMillis());
                                        if (!this.oO.forceQuit) {
                                            this.oO.stopMstPayLocked(startMstPay);
                                        }
                                        Log.m285d(PaymentNetworkProvider.LOG_TAG, "stopMstPay end:  currentTime " + System.currentTimeMillis());
                                        if (this.oO.mPayCallback != null) {
                                            this.oO.mPayCallback.m448a(null, -37, this.oO.mAuthType);
                                            return;
                                        } else {
                                            Log.m286e(PaymentNetworkProvider.LOG_TAG, "pay callback is null");
                                            return;
                                        }
                                    }
                                }
                                Log.m287i(PaymentNetworkProvider.LOG_TAG, " MstPayloop end: before transmitMst: " + System.currentTimeMillis());
                                if (ce()) {
                                    Log.m285d(PaymentNetworkProvider.LOG_TAG, "restart MST before transimiting MST");
                                    i2 = 0;
                                    i3 = -1;
                                    obj3 = obj2;
                                } else {
                                    Log.m285d(PaymentNetworkProvider.LOG_TAG, "MST thread exits before transimiting MST");
                                    if (!this.oO.forceQuit) {
                                        this.oO.stopMstPayLocked(true);
                                        return;
                                    }
                                    return;
                                }
                            }
                            Log.m287i(PaymentNetworkProvider.LOG_TAG, " MstPayloop: end: at start of loop: " + System.currentTimeMillis());
                            if (ce()) {
                                Log.m285d(PaymentNetworkProvider.LOG_TAG, "restart MST");
                                i2 = 0;
                                i3 = -1;
                                obj3 = obj2;
                            } else {
                                Log.m285d(PaymentNetworkProvider.LOG_TAG, "MST thread exits after preparing MST");
                                if (!this.oO.forceQuit) {
                                    this.oO.stopMstPayLocked(true);
                                    return;
                                }
                                return;
                            }
                            i2 += PaymentNetworkProvider.CREATE_TOKEN_SRC_PROV_RESPONSE;
                            i3 += PaymentNetworkProvider.CREATE_TOKEN_SRC_PROV_RESPONSE;
                            obj2 = obj3;
                        }
                        Log.m285d(PaymentNetworkProvider.LOG_TAG, "stopMstPay start:  currentTime " + System.currentTimeMillis());
                        if (obj != null) {
                            Log.m285d(PaymentNetworkProvider.LOG_TAG, "Enable Retry Mode");
                            PaymentNetworkProvider.mRetryMode = true;
                            Log.m285d(PaymentNetworkProvider.LOG_TAG, "Set Pay Type to Default - MST");
                        } else {
                            Log.m285d(PaymentNetworkProvider.LOG_TAG, "Disable Retry Mode");
                            PaymentNetworkProvider.mRetryMode = PaymentNetworkProvider.TOAST_DEBUG;
                            if (!this.oO.forceQuit) {
                                this.oO.stopMstPayLocked(true);
                            }
                        }
                        Log.m285d(PaymentNetworkProvider.LOG_TAG, "stopMstPay end:  currentTime " + System.currentTimeMillis());
                    } else {
                        Log.m286e(PaymentNetworkProvider.LOG_TAG, "Error: Failed to turn MST Driver on");
                        if (this.oO.mPayCallback != null) {
                            this.oO.mPayCallback.m448a(null, this.oP, this.oO.mAuthType);
                        } else {
                            Log.m286e(PaymentNetworkProvider.LOG_TAG, "pay callback is null");
                        }
                    }
                }
            } else {
                Log.m286e(PaymentNetworkProvider.LOG_TAG, "startPay:prepareMstPay() failed ");
                if (this.oO.mPayCallback != null) {
                    this.oO.mPayCallback.m448a(null, this.oP, this.oO.mAuthType);
                } else {
                    Log.m286e(PaymentNetworkProvider.LOG_TAG, "pay callback is null");
                }
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.b */
    private class C0417b extends TimerTask {
        final /* synthetic */ PaymentNetworkProvider oO;

        private C0417b(PaymentNetworkProvider paymentNetworkProvider) {
            this.oO = paymentNetworkProvider;
        }

        public void run() {
            Log.m285d(PaymentNetworkProvider.LOG_TAG, "TimerExpired::run: unloading TA");
            this.oO.unloadTA();
            this.oO.mUnloadTimer.cancel();
        }
    }

    protected abstract boolean authenticateTransaction(SecuredObject securedObject);

    protected abstract void clearCard();

    protected abstract ProviderResponseData createToken(String str, ProviderRequestData providerRequestData, int i);

    public abstract void delete();

    protected abstract byte[] generateInAppPaymentPayload(InAppDetailedTransactionInfo inAppDetailedTransactionInfo);

    protected abstract CertificateInfo[] getDeviceCertificates();

    protected abstract ProviderRequestData getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo);

    public abstract boolean getPayReadyState();

    protected abstract ProviderRequestData getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo);

    protected abstract ProviderRequestData getReplenishmentRequestData();

    protected abstract int getTransactionData(Bundle bundle, TransactionResponse transactionResponse);

    protected abstract byte[] handleApdu(byte[] bArr, Bundle bundle);

    protected abstract void init();

    protected abstract void interruptMstPay();

    protected abstract void loadTA();

    protected abstract boolean prepareMstPay();

    protected abstract boolean prepareNfcPay();

    protected abstract TransactionDetails processTransactionData(Object obj);

    protected abstract ProviderResponseData replenishToken(JsonObject jsonObject, TokenStatus tokenStatus);

    protected abstract SelectCardResult selectCard();

    public abstract boolean setServerCertificates(CertificateInfo[] certificateInfoArr);

    public abstract boolean startMstPay(int i, byte[] bArr);

    protected abstract void stopMstPay(boolean z);

    protected abstract Bundle stopNfcPay(int i);

    protected abstract void unloadTA();

    protected abstract ProviderResponseData updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus);

    static {
        TUI_DATA_DIR = TAController.getEfsDirectory();
        DEBUG = Utils.DEBUG;
        mSwitchObj = new Object();
        mPaymentModeObj = new Object();
        mShouldInterrupt = TOAST_DEBUG;
        mAuthTASelectedCard = null;
        mPayTASelectedCard = null;
        mPayConfig = null;
        mMstPayThread = null;
        mTACounterFactory = new TACounter();
        mAuthTAController = null;
        mRetryMode = TOAST_DEBUG;
        mNfcWait = null;
    }

    public GiftCardRegisterResponseData getGiftCardRegisterData(GiftCardRegisterRequestData giftCardRegisterRequestData) {
        return null;
    }

    public GiftCardRegisterResponseData getGiftCardTzEncData(GiftCardRegisterRequestData giftCardRegisterRequestData) {
        return null;
    }

    public void setCardTzEncData(byte[] bArr) {
    }

    public GiftCardRegisterResponseData getGiftCardRegisterDataTA(GiftCardRegisterRequestData giftCardRegisterRequestData) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            GiftCardRegisterResponseData giftCardRegisterData = getGiftCardRegisterData(giftCardRegisterRequestData);
            return giftCardRegisterData;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    public GiftCardRegisterResponseData getGiftCardTzEncDataTA(GiftCardRegisterRequestData giftCardRegisterRequestData) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            GiftCardRegisterResponseData giftCardTzEncData = getGiftCardTzEncData(giftCardRegisterRequestData);
            return giftCardTzEncData;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    private void loadTAwithCounter(boolean z) {
        Log.m285d(LOG_TAG, "load payment TA request : asyncTAloadUnload " + z);
        this.mUnloadTimer.cancel();
        loadTA();
        if (z) {
            this.mTACounter.co();
            Log.m287i(LOG_TAG, "load payment TA request with asyncTAloadUnload: count  " + this.mTACounter.getCount());
        }
    }

    protected String getAuthType() {
        return this.mAuthType;
    }

    private void unloadTAwithCounter(boolean z) {
        if (z) {
            this.mTACounter.cp();
            Log.m287i(LOG_TAG, "unload payment TA request with  asyncTAloadUnload. decerment asyncloadUnload count ");
        }
        Log.m287i(LOG_TAG, "unload payment TA request: asyncLoadUnloadCount: " + this.mTACounter.getCount());
        if (this.mTACounter.getCount() <= 0) {
            this.mUnloadTimer.schedule(new C0417b(), UNLOAD_TIMER_EXPIRY_TIME);
            this.mTACounter.reset();
            return;
        }
        Log.m287i(LOG_TAG, "unload payment TA is delayed becasue of asyncLoadUnload : ");
    }

    protected PaymentNetworkProvider(Context context, String str) {
        this.mAuthType = AUTHTYPE_NONE;
        this.mMstSequenceId = null;
        this.mPayCallback = null;
        this.mStopPayCallback = null;
        this.mStopPaySelectedCard = null;
        this.mTAController = null;
        this.forceQuit = TOAST_DEBUG;
        this.mJwtRetryTriggered = TOAST_DEBUG;
        this.mCardBrand = str;
        this.mContext = context;
        if (str == null || !(str.equals(PlccConstants.BRAND) || str.equals(PaymentFramework.CARD_BRAND_GIFT) || str.equals(PaymentFramework.CARD_BRAND_LOYALTY))) {
            Log.m285d(LOG_TAG, "cardType =  " + str + " Use card specific unload timer/TA " + "Counter");
            this.mTACounter = mTACounterFactory.at(str);
            this.mUnloadTimer = new UnloadTimer(this.mCardBrand);
        } else {
            Log.m285d(LOG_TAG, "cardType =  " + str + " Plcc TA is used: Use Plcc unload " + "timer/TA Counter");
            this.mTACounter = mTACounterFactory.at(PlccConstants.BRAND);
            this.mUnloadTimer = new UnloadTimer(PlccConstants.BRAND);
        }
        File file = new File(TUI_DATA_DIR, TUIPINSECUREOBJECTFILE);
        if (!file.exists() || file.isDirectory()) {
            Log.m287i(LOG_TAG, "Creating Pin Random files");
            try {
                mAuthTAController = SpayTuiTAController.createOnlyInstance(this.mContext);
                if (mAuthTAController != null) {
                    mAuthTAController.loadTA();
                    mAuthTAController.unloadTA();
                }
            } catch (Throwable e) {
                Log.m284c(LOG_TAG, e.getMessage(), e);
            }
        }
    }

    public final void setProviderTokenKey(ProviderTokenKey providerTokenKey) {
        this.mProviderTokenKey = providerTokenKey;
    }

    public final ProviderTokenKey getProviderTokenKey() {
        return this.mProviderTokenKey;
    }

    public final void setEnrollmentId(String str) {
        this.mEnrollmentId = str;
    }

    public String getPFTokenStatus() {
        return this.mPFTokenStatus;
    }

    public void setPFTokenStatus(String str) {
        this.mPFTokenStatus = str;
    }

    public final void providerInit() {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            init();
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    public final CertificateInfo[] getDeviceCertificatesTA() {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            CertificateInfo[] deviceCertificates = getDeviceCertificates();
            return deviceCertificates;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    public final ProviderRequestData getEnrollmentRequestDataTA(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            ProviderRequestData enrollmentRequestData = getEnrollmentRequestData(enrollCardInfo, billingInfo);
            return enrollmentRequestData;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    public final ProviderRequestData getProvisionRequestDataTA(ProvisionTokenInfo provisionTokenInfo) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            ProviderRequestData provisionRequestData = getProvisionRequestData(provisionTokenInfo);
            return provisionRequestData;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    public final ProviderResponseData createTokenTA(String str, ProviderRequestData providerRequestData, int i) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            ProviderResponseData createToken = createToken(str, providerRequestData, i);
            if (!(createToken == null || createToken.getProviderTokenKey() == null)) {
                createToken.getProviderTokenKey().setTrTokenId(str);
                this.mProviderTokenKey = createToken.getProviderTokenKey();
            }
            unloadTAwithCounter(TOAST_DEBUG);
            return createToken;
        } catch (Throwable th) {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    public final ProviderRequestData getReplenishmentRequestDataTA() {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            ProviderRequestData replenishmentRequestData = getReplenishmentRequestData();
            return replenishmentRequestData;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    public final ProviderResponseData replenishTokenTA(JsonObject jsonObject, TokenStatus tokenStatus) {
        synchronized (mPaymentModeObj) {
            if (PaymentFrameworkApp.az().aK()) {
                try {
                    Log.m287i(LOG_TAG, "Paymentmode is ON: putting replenish thread to sleep");
                    mPaymentModeObj.wait();
                } catch (Throwable e) {
                    Log.m285d(LOG_TAG, "switch obj wait interrupt exception");
                    Log.m284c(LOG_TAG, e.getMessage(), e);
                }
                Log.m287i(LOG_TAG, "Paymentmode is OFF: replenish thread to awake from sleep");
            }
        }
        try {
            loadTAwithCounter(TOAST_DEBUG);
            ProviderResponseData replenishToken = replenishToken(jsonObject, tokenStatus);
            return replenishToken;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    public final ProviderResponseData updateTokenStatusTA(JsonObject jsonObject, TokenStatus tokenStatus) {
        if (tokenStatus != null) {
            try {
                if (tokenStatus.getCode() != null) {
                    Log.m285d(LOG_TAG, "Token Status = " + tokenStatus.getCode());
                    if (!(!tokenStatus.getCode().equals(TokenStatus.DISPOSED) || this.mProviderTokenKey == null || mAuthTASelectedCard == null)) {
                        Log.m285d(LOG_TAG, "providerTokenKey = " + this.mProviderTokenKey.cn());
                        Log.m285d(LOG_TAG, "mAuthTASelectedCard = " + mAuthTASelectedCard.cn());
                        if (Objects.equals(this.mProviderTokenKey.cn(), mAuthTASelectedCard.cn())) {
                            Log.m285d(LOG_TAG, "Clearing Secure Object Input for Payment");
                            clearSecureObjectInputForPayment();
                        }
                    }
                }
            } catch (Throwable th) {
                unloadTAwithCounter(TOAST_DEBUG);
            }
        }
        loadTAwithCounter(TOAST_DEBUG);
        ProviderResponseData updateTokenStatus = updateTokenStatus(jsonObject, tokenStatus);
        unloadTAwithCounter(TOAST_DEBUG);
        return updateTokenStatus;
    }

    public void checkIfReplenishmentNeeded(TransactionData transactionData) {
    }

    public void setupReplenishAlarm() {
    }

    public final GiftCardDetail extractGiftCardDetailTA(byte[] bArr, byte[] bArr2, SecuredObject securedObject, boolean z) {
        Log.m287i(LOG_TAG, "extractGiftCardDetailTA:");
        GiftCardDetail giftCardDetail = new GiftCardDetail();
        giftCardDetail.setErrorCode(-1);
        if (securedObject == null || bArr2 == null) {
            Log.m286e(LOG_TAG, "extractGiftCardDetailTA:invalid input");
            giftCardDetail.setErrorCode(-5);
        } else if (!State.m656p(CREATE_TOKEN_SRC_PROV_PUSH)) {
            Log.m286e(LOG_TAG, "extractGiftCardDetailTA: invalid state");
        } else if (mAuthTAController == null) {
            Log.m286e(LOG_TAG, "extractGiftCardDetailTA:auth TA controller instance is null ");
        } else {
            mPayTASelectedCard = mAuthTASelectedCard;
            if (mPayTASelectedCard == null) {
                Log.m286e(LOG_TAG, "extractGiftCardDetailTA:selectCard is not called ");
            } else {
                SelectCardResult selectCardForPayment = selectCardForPayment();
                if (selectCardForPayment == null || selectCardForPayment.getNonce() == null || selectCardForPayment.getTaid() == null) {
                    Log.m286e(LOG_TAG, "extractGiftCardDetailTA:selectCard result from pay provider is not valid ");
                    clearCardState();
                } else {
                    AuthResult authResult = mAuthTAController.getAuthResult(selectCardForPayment.getNonce(), selectCardForPayment.getTaid(), securedObject.getSecureObjectData());
                    byte[] bArr3 = null;
                    if (authResult != null) {
                        bArr3 = authResult.getSecObjData();
                        this.mAuthType = authResult.getAuthType();
                        Log.m287i(LOG_TAG, "extractGiftCardDetailTA:auth type =  " + this.mAuthType);
                    }
                    if (bArr3 == null) {
                        Log.m286e(LOG_TAG, "extractGiftCardDetailTA:authentication failed from auth TA ");
                        giftCardDetail.setErrorCode(-35);
                        clearCardState();
                    } else {
                        SecuredObject securedObject2 = new SecuredObject();
                        securedObject2.setSecureObjectData(bArr3);
                        if (!authenticateTransaction(securedObject2)) {
                            Log.m286e(LOG_TAG, "extractGiftCardDetailTA:authentication failed ");
                            clearCardState();
                        } else if (!State.m657q(16)) {
                            Log.m286e(LOG_TAG, "extractGiftCardDetailTA: cannot go to pay IDLE state");
                            clearCardState();
                        } else if (State.m657q(PKIFailureInfo.certRevoked)) {
                            PaymentFrameworkApp.az().m619c(true);
                            giftCardDetail = extractGiftCardDetail(bArr, bArr2);
                            PaymentFrameworkApp.az().m619c(TOAST_DEBUG);
                            if (!z) {
                                Log.m285d(LOG_TAG, "extractGiftCardDetailTA: No MST Pay Requested. Clear Card State");
                                clearCardState();
                            } else if (State.m657q(CREATE_TOKEN_SRC_PROV_PUSH)) {
                                Log.m285d(LOG_TAG, "extractGiftCardDetailTA: Start MST Pay Requested. State changed to NPAY_SELECTED");
                            } else {
                                Log.m286e(LOG_TAG, "extractGiftCardDetailTA: cannot go to pay Selected state");
                                clearCardState();
                            }
                        } else {
                            Log.m286e(LOG_TAG, "extractGiftCardDetailTA: cannot go to pay PAY_EXTRACT_CARDDETAIL state");
                            clearCardState();
                        }
                    }
                }
            }
        }
        return giftCardDetail;
    }

    private void clearCardState() {
        Log.m285d(LOG_TAG, "clearCardState:");
        clearSecureObjectInputForPayment();
        this.mJwtRetryTriggered = TOAST_DEBUG;
        try {
            clearPayState();
        } finally {
            clearCard();
            unloadTAwithCounter(true);
            State.m657q(CREATE_TOKEN_SRC_PROV_RESPONSE);
        }
    }

    protected GiftCardDetail extractGiftCardDetail(byte[] bArr, byte[] bArr2) {
        return null;
    }

    public final String updateLoyaltyCardTA(JsonObject jsonObject) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            String updateLoyaltyCard = updateLoyaltyCard(jsonObject);
            return updateLoyaltyCard;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    protected String updateLoyaltyCard(JsonObject jsonObject) {
        return null;
    }

    public final ExtractCardDetailResult extractLoyaltyCardDetailTA(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            ExtractCardDetailResult extractLoyaltyCardDetail = extractLoyaltyCardDetail(extractLoyaltyCardDetailRequest);
            return extractLoyaltyCardDetail;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    protected ExtractCardDetailResult extractLoyaltyCardDetail(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest) {
        return null;
    }

    public final String prepareLoyaltyDataForServerTA(String str) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            String prepareLoyaltyDataForServer = prepareLoyaltyDataForServer(str);
            return prepareLoyaltyDataForServer;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    protected String prepareLoyaltyDataForServer(String str) {
        return null;
    }

    public final SelectCardResult getSecureObjectInputForPayment(boolean z) {
        int i = CREATE_TOKEN_SRC_PROV_RESPONSE;
        int i2 = 0;
        if (this.mProviderTokenKey == null) {
            Log.m286e(LOG_TAG, "providerTokenKey is null. can't get secObj input");
            return null;
        } else if (z) {
            Log.m285d(LOG_TAG, "skipping loading auth TA");
            r0 = new SelectCardResult();
            r0.setStatus(0);
            mAuthTASelectedCard = this.mProviderTokenKey;
            return r0;
        } else {
            Log.m285d(LOG_TAG, "getSecureObjectInputForPayment: ");
            mAuthTAController = SpayTuiTAController.createOnlyInstance(this.mContext);
            if (mAuthTAController == null) {
                return null;
            }
            mAuthTAController.loadTA();
            AuthNonce cachedNonce = mAuthTAController.getCachedNonce(32, true);
            String tAId = mAuthTAController.getTAInfo().getTAId();
            if (!(cachedNonce == null || tAId == null)) {
                byte[] nonce = cachedNonce.getNonce();
                if (nonce != null && nonce.length > 0) {
                    Log.m285d(LOG_TAG, "nonce: " + Arrays.toString(nonce));
                    Log.m285d(LOG_TAG, "taid: " + tAId);
                    if (!cachedNonce.isFromCache()) {
                        i = 0;
                    }
                    SelectCardResult selectCardResult = new SelectCardResult();
                    selectCardResult.setNonce(nonce);
                    selectCardResult.setTaid(tAId);
                    selectCardResult.setStatus(i);
                    mAuthTASelectedCard = this.mProviderTokenKey;
                    r0 = selectCardResult;
                    if (i2 != 0) {
                        return r0;
                    }
                    Log.m286e(LOG_TAG, "Error occurred unloading auth TA");
                    mAuthTAController.unloadTA();
                    return r0;
                }
            }
            i2 = CREATE_TOKEN_SRC_PROV_RESPONSE;
            r0 = null;
            if (i2 != 0) {
                return r0;
            }
            Log.m286e(LOG_TAG, "Error occurred unloading auth TA");
            mAuthTAController.unloadTA();
            return r0;
        }
    }

    public final void clearSecureObjectInputForPayment() {
        Log.m285d(LOG_TAG, "clearSecureObjectInputForPayment: mAuthTASelectedCard " + mAuthTASelectedCard);
        if (mAuthTAController != null) {
            mAuthTAController.unloadTA();
        }
        mAuthTASelectedCard = null;
    }

    private SelectCardResult selectCardForPayment() {
        Log.m285d(LOG_TAG, "selectCardForPayment: mPayTASelectedCard " + mPayTASelectedCard);
        loadTAwithCounter(true);
        return selectCard();
    }

    public final void clearPay() {
        Log.m285d(LOG_TAG, "clearPay:");
        if (!this.mTAController.makeSystemCall(CREATE_TOKEN_SRC_PROV_PUSH)) {
            Log.m286e(LOG_TAG, "Error: Failed to turn OFF MST");
        }
        this.mTAController.moveSecOsToDefaultCore();
        providerResetMstPayIfPossible();
        clearSecureObjectInputForPayment();
        if (mNfcWait != null) {
            mNfcWait.open();
        }
        if (mPayTASelectedCard == null) {
            Log.m290w(LOG_TAG, "clearPay: mPayTASelectedCard " + mPayTASelectedCard);
            return;
        }
        synchronized (mPaymentModeObj) {
            mPaymentModeObj.notifyAll();
        }
        try {
            clearPayState();
        } finally {
            clearCard();
            unloadTAwithCounter(true);
        }
    }

    public final void clearRetryPay() {
        Log.m285d(LOG_TAG, "clearRetryPay:");
        mRetryMode = true;
        stopMstPayInternal(true);
        if (!this.mTAController.makeSystemCall(CREATE_TOKEN_SRC_PROV_PUSH)) {
            Log.m286e(LOG_TAG, "Error: Failed to turn OFF MST");
        }
        this.mTAController.moveSecOsToDefaultCore();
        providerResetMstPayIfPossible();
        clearSecureObjectInputForPayment();
        if (mNfcWait != null) {
            mNfcWait.open();
        }
        if (mPayTASelectedCard == null) {
            Log.m290w(LOG_TAG, "clearPay: mPayTASelectedCard " + mPayTASelectedCard);
            clearPayState();
            return;
        }
        synchronized (mPaymentModeObj) {
            mPaymentModeObj.notifyAll();
        }
        try {
            clearPayState();
        } finally {
            clearCard();
            unloadTAwithCounter(true);
        }
    }

    public final byte[] processApdu(byte[] bArr, Bundle bundle) {
        if (mNfcWait != null) {
            mNfcWait.open();
        }
        if (State.m658r(32)) {
            if (State.m656p(82)) {
                if (prepareNfcPayInternal()) {
                    boolean p = State.m656p(64);
                    if (State.m657q(32)) {
                        providerInterruptMstPayIfPossible();
                        if (p) {
                            Log.m287i(LOG_TAG, "onPaySwitch (MST -> NFC): start: " + System.currentTimeMillis());
                            onPaySwitch(CREATE_TOKEN_SRC_PROV_PUSH, CREATE_TOKEN_SRC_PROV_RESPONSE);
                            if (this.mPayCallback != null) {
                                this.mPayCallback.m446a(null, CREATE_TOKEN_SRC_PROV_PUSH, CREATE_TOKEN_SRC_PROV_RESPONSE, this.mAuthType);
                            }
                            Log.m285d(LOG_TAG, "onPaySwitch end= " + System.currentTimeMillis());
                        }
                    } else if (this.mPayCallback == null) {
                        return null;
                    } else {
                        this.mPayCallback.m448a(null, -36, this.mAuthType);
                        return null;
                    }
                }
                Log.m286e(LOG_TAG, "prepareNfcPay: setup error ");
                if (this.mPayCallback == null) {
                    return null;
                }
                this.mPayCallback.m448a(null, -11, this.mAuthType);
                return null;
            }
            Log.m287i(LOG_TAG, "handleApdu SDK start: currentTime" + System.currentTimeMillis());
            byte[] handleApdu = handleApdu(bArr, bundle);
            Log.m287i(LOG_TAG, "handleApdu SDK end:  currentTime" + System.currentTimeMillis());
            return handleApdu;
        }
        Log.m285d(LOG_TAG, "cannot continue the payment, state has changed");
        return null;
    }

    public void setPaymentFrameworkRequester(PaymentFrameworkRequester paymentFrameworkRequester) {
    }

    public final void setupReplenishAlarm(Card card) {
        if (card != null) {
            try {
                if (card.ac() != null && card.ac().getTokenId() != null) {
                    setupReplenishAlarm();
                }
            } catch (Throwable e) {
                Log.m284c(LOG_TAG, e.getMessage(), e);
            }
        }
    }

    public final void startPay(PayConfig payConfig, SecuredObject securedObject, PayResponse payResponse, boolean z) {
        boolean z2 = true;
        Log.m287i(LOG_TAG, "startPay:");
        if ((z || securedObject != null) && payResponse != null) {
            this.mPayCallback = payResponse;
            if (mRetryMode) {
                Log.m286e(LOG_TAG, "startPay: In Retry Mode. Do not call Start Pay, call Retry Pay or Stop Pay.");
                this.mPayCallback.m448a(null, -4, this.mAuthType);
                return;
            } else if (State.m656p(CREATE_TOKEN_SRC_PROV_PUSH)) {
                mPayConfig = payConfig;
                SelectCardResult selectCardForPayment;
                if (z) {
                    Log.m285d(LOG_TAG, "startPay: auth alive: skip authentication process");
                    if (mPayTASelectedCard == null) {
                        mPayTASelectedCard = mAuthTASelectedCard;
                        if (mPayTASelectedCard == null) {
                            Log.m286e(LOG_TAG, "startPay:selectCard is not called ");
                            this.mPayCallback.m448a(null, -36, this.mAuthType);
                            return;
                        }
                        selectCardForPayment = selectCardForPayment();
                        if (selectCardForPayment == null || selectCardForPayment.getNonce() == null || selectCardForPayment.getTaid() == null) {
                            Log.m286e(LOG_TAG, "startPay:selectCard result from pay provider is not valid ");
                            this.mPayCallback.m448a(null, -36, this.mAuthType);
                            return;
                        }
                    }
                }
                Log.m285d(LOG_TAG, "startPay: perform authentication");
                mPayTASelectedCard = mAuthTASelectedCard;
                if (mPayTASelectedCard == null) {
                    Log.m286e(LOG_TAG, "startPay:selectCard is not called ");
                    this.mPayCallback.m448a(null, -36, this.mAuthType);
                    return;
                } else if (mAuthTAController == null) {
                    Log.m286e(LOG_TAG, "startPay:auth TA controller instance is null ");
                    this.mPayCallback.m448a(null, -36, this.mAuthType);
                    return;
                } else {
                    selectCardForPayment = selectCardForPayment();
                    if (selectCardForPayment == null || selectCardForPayment.getNonce() == null || selectCardForPayment.getTaid() == null) {
                        Log.m286e(LOG_TAG, "startPay:selectCard result from pay provider is not valid ");
                        this.mPayCallback.m448a(null, -36, this.mAuthType);
                        return;
                    }
                    byte[] secObjData;
                    AuthResult authResult = mAuthTAController.getAuthResult(selectCardForPayment.getNonce(), selectCardForPayment.getTaid(), securedObject.getSecureObjectData());
                    if (authResult != null) {
                        secObjData = authResult.getSecObjData();
                        this.mAuthType = authResult.getAuthType();
                        Log.m287i(LOG_TAG, "startPay:auth type =  " + this.mAuthType);
                    } else {
                        secObjData = null;
                    }
                    if (secObjData == null) {
                        Log.m286e(LOG_TAG, "startPay:authentication failed from auth TA ");
                        this.mPayCallback.m448a(null, -35, this.mAuthType);
                        return;
                    }
                    SecuredObject securedObject2 = new SecuredObject();
                    securedObject2.setSecureObjectData(secObjData);
                    if (!authenticateTransaction(securedObject2)) {
                        Log.m286e(LOG_TAG, "startPay:authentication failed ");
                        this.mPayCallback.m448a(null, -35, this.mAuthType);
                        return;
                    }
                }
                if (State.m657q(16)) {
                    boolean z3 = (payConfig == null || payConfig.getPayType() != CREATE_TOKEN_SRC_PROV_RESPONSE) ? TOAST_DEBUG : true;
                    Log.m285d(LOG_TAG, "isNFCPaymentOnly= " + z3);
                    setPayAuthenticationMode(this.mAuthType);
                    if (getAuthType().equalsIgnoreCase(AUTHTYPE_NONE)) {
                        z2 = TOAST_DEBUG;
                    }
                    beginPay(z2, Utils.ap(this.mContext));
                    if (!z3 && Utils.ao(this.mContext) && isPayAllowedForPresentationMode(CREATE_TOKEN_SRC_PROV_PUSH)) {
                        this.mMstSequenceId = PayConfigurator.m615f("default", this.mCardBrand);
                        mMstPayThread = new Thread(new C0416a());
                        mMstPayThread.start();
                        Log.m287i(LOG_TAG, "started MST pay thread");
                        return;
                    }
                    Log.m285d(LOG_TAG, "Utils.isMstAvailable(mContext) " + Utils.ao(this.mContext));
                    Log.m285d(LOG_TAG, "isPayAllowedForPresentationMode(PaymentFramework.CARD_PRESENT_MODE_MST) " + isPayAllowedForPresentationMode(CREATE_TOKEN_SRC_PROV_PUSH));
                    Log.m285d(LOG_TAG, "MST not supported.");
                    new C04151(this).start();
                    return;
                }
                Log.m286e(LOG_TAG, "startPay: cannot go to pay ilde");
                this.mPayCallback.m448a(null, -4, this.mAuthType);
                return;
            } else {
                Log.m286e(LOG_TAG, "startPay: already pay in progress.can't start one more startPay");
                this.mPayCallback.m448a(null, -4, this.mAuthType);
                return;
            }
        }
        Log.m286e(LOG_TAG, "startPay:invalid input");
        if (payResponse != null) {
            payResponse.m448a(null, -36, this.mAuthType);
        }
    }

    public final int retryPay(PayConfig payConfig) {
        Log.m287i(LOG_TAG, "retryPay:");
        if (this.mPayCallback == null) {
            Log.m286e(LOG_TAG, "retryPay : invalid input");
            return -44;
        } else if (!State.m656p(CREATE_TOKEN_SRC_PROV_PUSH)) {
            Log.m286e(LOG_TAG, " retryPay: already pay in progress.can't start one more retryPay");
            this.mPayCallback.m448a(null, -4, this.mAuthType);
            return -4;
        } else if (mAuthTAController == null) {
            Log.m286e(LOG_TAG, "retryPay : auth TA controller instance is null ");
            this.mPayCallback.m448a(null, -36, this.mAuthType);
            return -36;
        } else {
            mPayTASelectedCard = mAuthTASelectedCard;
            mPayConfig = payConfig;
            if (mPayTASelectedCard == null) {
                Log.m286e(LOG_TAG, "retryPay : selectCard is not called ");
                this.mPayCallback.m448a(null, -36, this.mAuthType);
                return -36;
            } else if (!State.m657q(16)) {
                Log.m286e(LOG_TAG, "retryPay : cannot go to pay ilde");
                this.mPayCallback.m448a(null, -4, this.mAuthType);
                return -4;
            } else if (payConfig == null || payConfig.getPayType() != CREATE_TOKEN_SRC_PROV_RESPONSE) {
                setPayAuthenticationMode(this.mAuthType);
                if (payConfig != null && payConfig.getPayType() == CREATE_TOKEN_SRC_PROV_PUSH) {
                    this.mMstSequenceId = PayConfigurator.m615f("retry1", this.mCardBrand);
                    if (mMstPayThread != null && mMstPayThread.isAlive()) {
                        Log.m287i(LOG_TAG, "wait for MST pay thread");
                        try {
                            mMstPayThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mRetryMode = true;
                    mMstPayThread = new Thread(new C0416a());
                    mMstPayThread.start();
                    Log.m287i(LOG_TAG, "started MST pay thread");
                }
                return 0;
            } else {
                Log.m286e(LOG_TAG, "retryPay : payType should not be NFC");
                this.mPayCallback.m448a(null, -4, this.mAuthType);
                return -4;
            }
        }
    }

    public final boolean shouldTransitBackToMst() {
        boolean z = TOAST_DEBUG;
        if (State.m656p(8)) {
            Log.m285d(LOG_TAG, "contine to transit mst but stopping nfc");
            if (this.mPayCallback != null) {
                Log.m287i(LOG_TAG, "onPaySwitch (NFC -> MST): start: " + System.currentTimeMillis());
                this.mPayCallback.m446a(null, CREATE_TOKEN_SRC_PROV_RESPONSE, CREATE_TOKEN_SRC_PROV_PUSH, this.mAuthType);
            }
            if (!makeSysCallInternal(CMD_RESET_MST)) {
                Log.m286e(LOG_TAG, "Error: cannot reset mst transimission");
            }
            if (!this.mTAController.makeSystemCall(CREATE_TOKEN_SRC_PROV_RESPONSE)) {
                Log.m286e(LOG_TAG, "Error: Failed to turn MST Driver on");
            }
            z = true;
        } else {
            Log.m285d(LOG_TAG, "nfc is done, no need to transit mst again");
        }
        synchronized (mSwitchObj) {
            mSwitchObj.notifyAll();
        }
        return z;
    }

    public final Bundle processTransacionComplete(int i) {
        Log.m285d(LOG_TAG, "processTransacionComplete: stop nfc flag " + i);
        return stopNfcPayInternal(i);
    }

    protected void onPaySwitch(int i, int i2) {
        if (i == CREATE_TOKEN_SRC_PROV_PUSH && i2 == CREATE_TOKEN_SRC_PROV_RESPONSE) {
            Log.m287i(LOG_TAG, "switch payment method from MST to NFC");
        }
    }

    public final void stopInAppPay(String str, ICommonCallback iCommonCallback) {
        Log.m285d(LOG_TAG, "stopInAppPay");
        Log.m285d(LOG_TAG, "State : " + State.getState());
        if (iCommonCallback != null) {
            try {
                this.mStopPayCallback = iCommonCallback;
                this.mStopPaySelectedCard = str;
            } catch (Exception e) {
                Log.m286e(LOG_TAG, "Exception in stop in-app pay");
                return;
            }
        }
        if (!State.m657q(PKIFailureInfo.certConfirmed)) {
            Log.m286e(LOG_TAG, "state change prohibited");
        }
    }

    public final void stopPay() {
        Log.m285d(LOG_TAG, "stopPay");
        this.forceQuit = TOAST_DEBUG;
        SPayHCEReceiver.aS();
        if (State.m656p(72)) {
            if (!State.m657q(24320)) {
                Log.m286e(LOG_TAG, "state cannot be changed from MST");
            }
            Log.m287i(LOG_TAG, "stopped MST pay thread");
            if (this.mPayCallback != null) {
                this.mPayCallback.m448a(null, -7, this.mAuthType);
                this.mPayCallback = null;
                return;
            }
            Log.m285d(LOG_TAG, "stopPay callback is null");
        } else if (State.m656p(32)) {
            if (!State.m657q(24320)) {
                Log.m286e(LOG_TAG, "state cannot be changed from NFC");
            }
            Log.m287i(LOG_TAG, "stopped NFC pay ");
        } else {
            Log.m285d(LOG_TAG, "State : " + State.getState());
            Log.m285d(LOG_TAG, "Retry Mode : " + mRetryMode);
            if (mRetryMode) {
                Log.m285d(LOG_TAG, "stopMstPay in Retry Mode");
                stopMstPayLocked(true);
            }
            if (!State.m657q(24320)) {
                Log.m286e(LOG_TAG, "state cannot be changed from IDLE");
            }
            if (this.mPayCallback != null) {
                this.mPayCallback.m448a(null, -7, this.mAuthType);
                this.mPayCallback = null;
                return;
            }
            Log.m285d(LOG_TAG, "stopPay callback is null");
        }
    }

    public final TransactionDetails processTransactionDataTA(Object obj) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            TransactionDetails processTransactionData = processTransactionData(obj);
            return processTransactionData;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    public final void startInAppPay(com.samsung.android.spayfw.appinterface.SecuredObject r12, com.samsung.android.spayfw.appinterface.InAppTransactionInfo r13, com.samsung.android.spayfw.appinterface.IInAppPayCallback r14) {
        /* JADX: method processing error */
/*
        Error: jadx.core.utils.exceptions.JadxRuntimeException: Exception block dominator not found, method:com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.startInAppPay(com.samsung.android.spayfw.appinterface.SecuredObject, com.samsung.android.spayfw.appinterface.InAppTransactionInfo, com.samsung.android.spayfw.appinterface.IInAppPayCallback):void. bs: [B:46:0x017a, B:67:0x01fa]
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
        r11 = this;
        r4 = -4;
        r7 = 0;
        r10 = 0;
        r2 = -36;
        if (r12 == 0) goto L_0x000b;
    L_0x0007:
        if (r13 == 0) goto L_0x000b;
    L_0x0009:
        if (r14 != 0) goto L_0x0021;
    L_0x000b:
        r0 = "PaymentNetworkProvider";
        r1 = "startPay:invalid input";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        if (r14 == 0) goto L_0x001d;
    L_0x0014:
        if (r13 == 0) goto L_0x001d;
    L_0x0016:
        r0 = r13.getContextId();
        r14.onFail(r0, r2);
    L_0x001d:
        r11.clearSecureObjectInputForPayment();
    L_0x0020:
        return;
    L_0x0021:
        r0 = mAuthTAController;
        if (r0 != 0) goto L_0x0037;
    L_0x0025:
        r0 = "PaymentNetworkProvider";
        r1 = "startInAppPay:auth TA controller instance is null";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r13.getContextId();
        r14.onFail(r0, r2);
        r11.clearSecureObjectInputForPayment();
        goto L_0x0020;
    L_0x0037:
        r0 = r13.getCurrencyCode();
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 != 0) goto L_0x004b;
    L_0x0041:
        r0 = r13.getAmount();
        r0 = android.text.TextUtils.isEmpty(r0);
        if (r0 == 0) goto L_0x005d;
    L_0x004b:
        r0 = "PaymentNetworkProvider";
        r1 = "startInAppPay: either transaction info is null or missing some required fields";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r13.getContextId();
        r14.onFail(r0, r2);
        r11.clearSecureObjectInputForPayment();
        goto L_0x0020;
    L_0x005d:
        r0 = 2;
        r0 = com.samsung.android.spayfw.core.State.m656p(r0);
        if (r0 != 0) goto L_0x0076;
    L_0x0064:
        r0 = "PaymentNetworkProvider";
        r1 = "startInAppPay: already pay in progress.can't start one more startPay";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r13.getContextId();
        r14.onFail(r0, r4);
        r11.clearSecureObjectInputForPayment();
        goto L_0x0020;
    L_0x0076:
        r0 = "PaymentNetworkProvider";
        r1 = "startInAppPay: start";
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);
        r0 = mAuthTASelectedCard;
        mPayTASelectedCard = r0;
        r0 = r11.selectCardForPayment();
        if (r0 == 0) goto L_0x0093;
    L_0x0087:
        r1 = r0.getNonce();
        if (r1 == 0) goto L_0x0093;
    L_0x008d:
        r1 = r0.getTaid();
        if (r1 != 0) goto L_0x00a6;
    L_0x0093:
        r0 = "PaymentNetworkProvider";
        r1 = "startInAppPay:selectCard result from pay provider is not valid";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r13.getContextId();
        r14.onFail(r0, r2);
        r11.clearCardState();
        goto L_0x0020;
    L_0x00a6:
        r1 = mAuthTAController;
        r2 = r0.getNonce();
        r0 = r0.getTaid();
        r3 = r12.getSecureObjectData();
        r1 = r1.getAuthResult(r2, r0, r3);
        if (r1 == 0) goto L_0x0446;
    L_0x00ba:
        r0 = r1.getSecObjData();
        r1 = r1.getAuthType();
        r11.mAuthType = r1;
        r1 = "PaymentNetworkProvider";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "startInAppPay:auth type =  ";
        r2 = r2.append(r3);
        r3 = r11.mAuthType;
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m287i(r1, r2);
    L_0x00de:
        if (r0 != 0) goto L_0x00f5;
    L_0x00e0:
        r0 = "PaymentNetworkProvider";
        r1 = "startInAppPay:authentication failed from auth TA ";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r13.getContextId();
        r1 = -35;
        r14.onFail(r0, r1);
        r11.clearCardState();
        goto L_0x0020;
    L_0x00f5:
        r1 = new com.samsung.android.spayfw.appinterface.SecuredObject;
        r1.<init>();
        r1.setSecureObjectData(r0);
        r0 = r11.authenticateTransaction(r1);
        if (r0 != 0) goto L_0x0118;
    L_0x0103:
        r0 = "PaymentNetworkProvider";
        r1 = "startInAppPay:authentication failed";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r13.getContextId();
        r1 = -35;
        r14.onFail(r0, r1);
        r11.clearCardState();
        goto L_0x0020;
    L_0x0118:
        r1 = 16;
        r1 = com.samsung.android.spayfw.core.State.m657q(r1);
        if (r1 != 0) goto L_0x0133;
    L_0x0120:
        r0 = "PaymentNetworkProvider";
        r1 = "startInAppPay: cannot go to pay IDLE state";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r13.getContextId();
        r14.onFail(r0, r4);
        r11.clearCardState();
        goto L_0x0020;
    L_0x0133:
        r1 = 128; // 0x80 float:1.794E-43 double:6.32E-322;
        r1 = com.samsung.android.spayfw.core.State.m657q(r1);
        if (r1 != 0) goto L_0x014e;
    L_0x013b:
        r0 = "PaymentNetworkProvider";
        r1 = "startInAppPay: cannot go to pay PAY_ECOMMERCE state";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);
        r0 = r13.getContextId();
        r14.onFail(r0, r4);
        r11.clearCardState();
        goto L_0x0020;
    L_0x014e:
        r1 = "PaymentNetworkProvider";
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "txinfo:";
        r2 = r2.append(r3);
        r3 = r13.toString();
        r2 = r2.append(r3);
        r2 = r2.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);
        r1 = com.samsung.android.spayfw.payprovider.MerchantServerRequester.cc();	 Catch:{ PaymentProviderException -> 0x0439 }
        r2 = r11.mContext;	 Catch:{ PaymentProviderException -> 0x0439 }
        r3 = r13.getPid();	 Catch:{ PaymentProviderException -> 0x0439 }
        r8 = r1.m749c(r2, r3);	 Catch:{ PaymentProviderException -> 0x0439 }
        if (r8 != 0) goto L_0x01c8;
    L_0x017a:
        r0 = "PaymentNetworkProvider";	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = "merchant info returned null";	 Catch:{ PaymentProviderException -> 0x0189 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ PaymentProviderException -> 0x0189 }
        r0 = new com.samsung.android.spayfw.payprovider.PaymentProviderException;	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = -36;	 Catch:{ PaymentProviderException -> 0x0189 }
        r0.<init>(r1);	 Catch:{ PaymentProviderException -> 0x0189 }
        throw r0;	 Catch:{ PaymentProviderException -> 0x0189 }
    L_0x0189:
        r0 = move-exception;
        r1 = r8;
    L_0x018b:
        r0 = r0.getErrorCode();	 Catch:{ all -> 0x024a }
        r2 = "PaymentNetworkProvider";	 Catch:{ all -> 0x024a }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x024a }
        r3.<init>();	 Catch:{ all -> 0x024a }
        r4 = "startInAppPay: PaymentProviderException: ";	 Catch:{ all -> 0x024a }
        r3 = r3.append(r4);	 Catch:{ all -> 0x024a }
        r3 = r3.append(r0);	 Catch:{ all -> 0x024a }
        r3 = r3.toString();	 Catch:{ all -> 0x024a }
        com.samsung.android.spayfw.p002b.Log.m286e(r2, r3);	 Catch:{ all -> 0x024a }
        r2 = r13.getContextId();	 Catch:{ all -> 0x024a }
        r2 = r11.checkIfStopInAppPayInvoked(r14, r2);	 Catch:{ all -> 0x024a }
        if (r2 == 0) goto L_0x03f1;	 Catch:{ all -> 0x024a }
    L_0x01b1:
        r0 = "PaymentNetworkProvider";	 Catch:{ all -> 0x024a }
        r1 = "Stop Pay invoked";	 Catch:{ all -> 0x024a }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ all -> 0x024a }
        r0 = r11.mJwtRetryTriggered;
        if (r0 != 0) goto L_0x0020;
    L_0x01bc:
        r0 = com.samsung.android.spayfw.core.PaymentFrameworkApp.az();
        r0.m619c(r10);
        r11.clearCardState();
        goto L_0x0020;
    L_0x01c8:
        r1 = r13.getContextId();	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = r11.checkIfStopInAppPayInvoked(r14, r1);	 Catch:{ PaymentProviderException -> 0x0189 }
        if (r1 == 0) goto L_0x01e9;	 Catch:{ PaymentProviderException -> 0x0189 }
    L_0x01d2:
        r0 = "PaymentNetworkProvider";	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = "Stop Pay invoked";	 Catch:{ PaymentProviderException -> 0x0189 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ PaymentProviderException -> 0x0189 }
        r0 = r11.mJwtRetryTriggered;
        if (r0 != 0) goto L_0x0020;
    L_0x01dd:
        r0 = com.samsung.android.spayfw.core.PaymentFrameworkApp.az();
        r0.m619c(r10);
        r11.clearCardState();
        goto L_0x0020;
    L_0x01e9:
        r1 = com.samsung.android.spayfw.core.PaymentFrameworkApp.az();	 Catch:{ PaymentProviderException -> 0x0189 }
        r2 = 1;	 Catch:{ PaymentProviderException -> 0x0189 }
        r1.m619c(r2);	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = r11.mContext;	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = com.samsung.android.spayfw.utils.Utils.ap(r1);	 Catch:{ PaymentProviderException -> 0x0189 }
        r11.beginPay(r0, r1);	 Catch:{ PaymentProviderException -> 0x0189 }
        r0 = r13.getContextId();	 Catch:{ TAException -> 0x023e }
        r0 = r11.checkIfStopInAppPayInvoked(r14, r0);	 Catch:{ TAException -> 0x023e }
        if (r0 == 0) goto L_0x021b;	 Catch:{ TAException -> 0x023e }
    L_0x0204:
        r0 = "PaymentNetworkProvider";	 Catch:{ TAException -> 0x023e }
        r1 = "Stop Pay invoked";	 Catch:{ TAException -> 0x023e }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ TAException -> 0x023e }
        r0 = r11.mJwtRetryTriggered;
        if (r0 != 0) goto L_0x0020;
    L_0x020f:
        r0 = com.samsung.android.spayfw.core.PaymentFrameworkApp.az();
        r0.m619c(r10);
        r11.clearCardState();
        goto L_0x0020;
    L_0x021b:
        r0 = r11.mContext;	 Catch:{ TAException -> 0x023e }
        r3 = com.samsung.android.spayfw.cncc.CNCCTAController.isSupported(r0);	 Catch:{ TAException -> 0x023e }
        if (r3 == 0) goto L_0x0443;	 Catch:{ TAException -> 0x023e }
    L_0x0223:
        r0 = com.samsung.android.spayfw.cncc.CNCCTAController.getInstance();	 Catch:{ TAException -> 0x023e }
        r1 = 32;	 Catch:{ TAException -> 0x023e }
        r0 = r0.getNonce(r1);	 Catch:{ TAException -> 0x023e }
        if (r0 != 0) goto L_0x025a;	 Catch:{ TAException -> 0x023e }
    L_0x022f:
        r0 = "PaymentNetworkProvider";	 Catch:{ TAException -> 0x023e }
        r1 = "startInAppPay: cannot get nonce";	 Catch:{ TAException -> 0x023e }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ TAException -> 0x023e }
        r0 = new com.samsung.android.spayfw.payprovider.PaymentProviderException;	 Catch:{ TAException -> 0x023e }
        r1 = -36;	 Catch:{ TAException -> 0x023e }
        r0.<init>(r1);	 Catch:{ TAException -> 0x023e }
        throw r0;	 Catch:{ TAException -> 0x023e }
    L_0x023e:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ PaymentProviderException -> 0x0189 }
        r0 = new com.samsung.android.spayfw.payprovider.PaymentProviderException;	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = -36;	 Catch:{ PaymentProviderException -> 0x0189 }
        r0.<init>(r1);	 Catch:{ PaymentProviderException -> 0x0189 }
        throw r0;	 Catch:{ PaymentProviderException -> 0x0189 }
    L_0x024a:
        r0 = move-exception;
        r1 = r11.mJwtRetryTriggered;
        if (r1 != 0) goto L_0x0259;
    L_0x024f:
        r1 = com.samsung.android.spayfw.core.PaymentFrameworkApp.az();
        r1.m619c(r10);
        r11.clearCardState();
    L_0x0259:
        throw r0;
    L_0x025a:
        r1 = r0;
    L_0x025b:
        r0 = r8.isJWEJWSRequired();	 Catch:{ TAException -> 0x023e }
        r2 = new com.samsung.android.spayfw.payprovider.PaymentNetworkProvider$InAppDetailedTransactionInfo;	 Catch:{ TAException -> 0x023e }
        r2.<init>(r13, r1);	 Catch:{ TAException -> 0x023e }
        if (r3 != 0) goto L_0x0440;	 Catch:{ TAException -> 0x023e }
    L_0x0266:
        r0 = "PaymentNetworkProvider";	 Catch:{ TAException -> 0x023e }
        r1 = "CNCC TA not supported, use Payment TA";	 Catch:{ TAException -> 0x023e }
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);	 Catch:{ TAException -> 0x023e }
        r0 = r8.getMerchantCertificateChain();	 Catch:{ TAException -> 0x023e }
        r2.aq(r0);	 Catch:{ TAException -> 0x023e }
        r9 = r10;	 Catch:{ TAException -> 0x023e }
    L_0x0275:
        r2 = r11.generateInAppPaymentPayload(r2);	 Catch:{ TAException -> 0x023e }
        if (r2 != 0) goto L_0x028a;	 Catch:{ TAException -> 0x023e }
    L_0x027b:
        r0 = "PaymentNetworkProvider";	 Catch:{ TAException -> 0x023e }
        r1 = "generate paymentPayload Null";	 Catch:{ TAException -> 0x023e }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ TAException -> 0x023e }
        r0 = new com.samsung.android.spayfw.payprovider.PaymentProviderException;	 Catch:{ TAException -> 0x023e }
        r1 = -36;	 Catch:{ TAException -> 0x023e }
        r0.<init>(r1);	 Catch:{ TAException -> 0x023e }
        throw r0;	 Catch:{ TAException -> 0x023e }
    L_0x028a:
        r0 = r8.getMerchantCertificateChain();	 Catch:{ TAException -> 0x023e }
        r1 = com.samsung.android.spayfw.utils.Utils.bE(r0);	 Catch:{ TAException -> 0x023e }
        if (r1 == 0) goto L_0x029a;	 Catch:{ TAException -> 0x023e }
    L_0x0294:
        r0 = r1.isEmpty();	 Catch:{ TAException -> 0x023e }
        if (r0 == 0) goto L_0x02a9;	 Catch:{ TAException -> 0x023e }
    L_0x029a:
        r0 = "PaymentNetworkProvider";	 Catch:{ TAException -> 0x023e }
        r1 = "startInAppPay: cannot get certificate";	 Catch:{ TAException -> 0x023e }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ TAException -> 0x023e }
        r0 = new com.samsung.android.spayfw.payprovider.PaymentProviderException;	 Catch:{ TAException -> 0x023e }
        r1 = -36;	 Catch:{ TAException -> 0x023e }
        r0.<init>(r1);	 Catch:{ TAException -> 0x023e }
        throw r0;	 Catch:{ TAException -> 0x023e }
    L_0x02a9:
        if (r3 == 0) goto L_0x02da;	 Catch:{ TAException -> 0x023e }
    L_0x02ab:
        r0 = "PaymentNetworkProvider";	 Catch:{ TAException -> 0x023e }
        r3 = "Calling CNCC TA to do JWE/JWS";	 Catch:{ TAException -> 0x023e }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r3);	 Catch:{ TAException -> 0x023e }
        r0 = r8.isJWEJWSRequired();	 Catch:{ TAException -> 0x023e }
        if (r0 == 0) goto L_0x02d7;	 Catch:{ TAException -> 0x023e }
    L_0x02b8:
        r4 = com.samsung.android.spayfw.cncc.CNCCTAController.ProcessingOption.OPTION_UNWRAP_FROM_TA_AND_JWEJWS_ENCRYPT_SIGN;	 Catch:{ TAException -> 0x023e }
    L_0x02ba:
        r0 = com.samsung.android.spayfw.cncc.CNCCTAController.getInstance();	 Catch:{ TAException -> 0x023e }
        r3 = com.samsung.android.spayfw.cncc.CNCCTAController.DataType.DATATYPE_RAW_DATA;	 Catch:{ TAException -> 0x023e }
        r5 = 0;	 Catch:{ TAException -> 0x023e }
        r6 = 0;	 Catch:{ TAException -> 0x023e }
        r2 = r0.processData(r1, r2, r3, r4, r5, r6);	 Catch:{ TAException -> 0x023e }
    L_0x02c6:
        if (r2 != 0) goto L_0x02e2;	 Catch:{ TAException -> 0x023e }
    L_0x02c8:
        r0 = "PaymentNetworkProvider";	 Catch:{ TAException -> 0x023e }
        r1 = "get encPaymentPayload failed";	 Catch:{ TAException -> 0x023e }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ TAException -> 0x023e }
        r0 = new com.samsung.android.spayfw.payprovider.PaymentProviderException;	 Catch:{ TAException -> 0x023e }
        r1 = -36;	 Catch:{ TAException -> 0x023e }
        r0.<init>(r1);	 Catch:{ TAException -> 0x023e }
        throw r0;	 Catch:{ TAException -> 0x023e }
    L_0x02d7:
        r4 = com.samsung.android.spayfw.cncc.CNCCTAController.ProcessingOption.OPTION_UNWRAP_FROM_TA_AND_JWE_ENCRYPT;	 Catch:{ TAException -> 0x023e }
        goto L_0x02ba;	 Catch:{ TAException -> 0x023e }
    L_0x02da:
        r0 = "PaymentNetworkProvider";	 Catch:{ TAException -> 0x023e }
        r1 = "CNCC TA not supported, use the paymentPayload";	 Catch:{ TAException -> 0x023e }
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);	 Catch:{ TAException -> 0x023e }
        goto L_0x02c6;	 Catch:{ TAException -> 0x023e }
    L_0x02e2:
        if (r9 == 0) goto L_0x043d;	 Catch:{ TAException -> 0x023e }
    L_0x02e4:
        r0 = "PaymentNetworkProvider";	 Catch:{ TAException -> 0x023e }
        r1 = "Calling CNCC TA to get certificate";	 Catch:{ TAException -> 0x023e }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ TAException -> 0x023e }
        r0 = com.samsung.android.spayfw.cncc.CNCCTAController.getInstance();	 Catch:{ TAException -> 0x023e }
        r1 = r0.getDeviceCertificates();	 Catch:{ TAException -> 0x023e }
        r0 = 2;	 Catch:{ TAException -> 0x023e }
        r0 = new com.samsung.android.spayfw.remoteservice.models.CertificateInfo[r0];	 Catch:{ TAException -> 0x023e }
        r3 = new com.samsung.android.spayfw.remoteservice.models.CertificateInfo;	 Catch:{ TAException -> 0x023e }
        r3.<init>();	 Catch:{ TAException -> 0x023e }
        r4 = "verification_cert";	 Catch:{ TAException -> 0x023e }
        r3.setAlias(r4);	 Catch:{ TAException -> 0x023e }
        r4 = r1.deviceSigningCertificate;	 Catch:{ TAException -> 0x023e }
        r5 = "PaymentNetworkProvider";	 Catch:{ TAException -> 0x023e }
        r6 = new java.lang.StringBuilder;	 Catch:{ TAException -> 0x023e }
        r6.<init>();	 Catch:{ TAException -> 0x023e }
        r9 = "getCertificates: SignCert = ";	 Catch:{ TAException -> 0x023e }
        r6 = r6.append(r9);	 Catch:{ TAException -> 0x023e }
        r6 = r6.append(r4);	 Catch:{ TAException -> 0x023e }
        r6 = r6.toString();	 Catch:{ TAException -> 0x023e }
        com.samsung.android.spayfw.p002b.Log.m285d(r5, r6);	 Catch:{ TAException -> 0x023e }
        r3.setContent(r4);	 Catch:{ TAException -> 0x023e }
        r4 = "VER";	 Catch:{ TAException -> 0x023e }
        r3.setUsage(r4);	 Catch:{ TAException -> 0x023e }
        r4 = 0;	 Catch:{ TAException -> 0x023e }
        r0[r4] = r3;	 Catch:{ TAException -> 0x023e }
        r3 = new com.samsung.android.spayfw.remoteservice.models.CertificateInfo;	 Catch:{ TAException -> 0x023e }
        r3.<init>();	 Catch:{ TAException -> 0x023e }
        r4 = "device_root_cert";	 Catch:{ TAException -> 0x023e }
        r3.setAlias(r4);	 Catch:{ TAException -> 0x023e }
        r1 = r1.deviceCertificate;	 Catch:{ TAException -> 0x023e }
        r4 = "PaymentNetworkProvider";	 Catch:{ TAException -> 0x023e }
        r5 = new java.lang.StringBuilder;	 Catch:{ TAException -> 0x023e }
        r5.<init>();	 Catch:{ TAException -> 0x023e }
        r6 = "getCertificates: drc = ";	 Catch:{ TAException -> 0x023e }
        r5 = r5.append(r6);	 Catch:{ TAException -> 0x023e }
        r5 = r5.append(r1);	 Catch:{ TAException -> 0x023e }
        r5 = r5.toString();	 Catch:{ TAException -> 0x023e }
        com.samsung.android.spayfw.p002b.Log.m285d(r4, r5);	 Catch:{ TAException -> 0x023e }
        r3.setContent(r1);	 Catch:{ TAException -> 0x023e }
        r1 = "CA";	 Catch:{ TAException -> 0x023e }
        r3.setUsage(r1);	 Catch:{ TAException -> 0x023e }
        r1 = 1;	 Catch:{ TAException -> 0x023e }
        r0[r1] = r3;	 Catch:{ TAException -> 0x023e }
    L_0x0354:
        r1 = r13.getContextId();	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = r11.checkIfStopInAppPayInvoked(r14, r1);	 Catch:{ PaymentProviderException -> 0x0189 }
        if (r1 == 0) goto L_0x0375;	 Catch:{ PaymentProviderException -> 0x0189 }
    L_0x035e:
        r0 = "PaymentNetworkProvider";	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = "Stop Pay invoked";	 Catch:{ PaymentProviderException -> 0x0189 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ PaymentProviderException -> 0x0189 }
        r0 = r11.mJwtRetryTriggered;
        if (r0 != 0) goto L_0x0020;
    L_0x0369:
        r0 = com.samsung.android.spayfw.core.PaymentFrameworkApp.az();
        r0.m619c(r10);
        r11.clearCardState();
        goto L_0x0020;
    L_0x0375:
        r1 = new com.samsung.android.spayfw.payprovider.InAppPayload;	 Catch:{ PaymentProviderException -> 0x0189 }
        r1.<init>(r13, r2, r0);	 Catch:{ PaymentProviderException -> 0x0189 }
        r7 = r1.toJsonString();	 Catch:{ PaymentProviderException -> 0x0189 }
        if (r7 != 0) goto L_0x038f;	 Catch:{ PaymentProviderException -> 0x0189 }
    L_0x0380:
        r0 = "PaymentNetworkProvider";	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = "Payment Payload JSON null ";	 Catch:{ PaymentProviderException -> 0x0189 }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ PaymentProviderException -> 0x0189 }
        r0 = new com.samsung.android.spayfw.payprovider.PaymentProviderException;	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = -36;	 Catch:{ PaymentProviderException -> 0x0189 }
        r0.<init>(r1);	 Catch:{ PaymentProviderException -> 0x0189 }
        throw r0;	 Catch:{ PaymentProviderException -> 0x0189 }
    L_0x038f:
        r0 = "PaymentNetworkProvider";	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = new java.lang.StringBuilder;	 Catch:{ PaymentProviderException -> 0x0189 }
        r1.<init>();	 Catch:{ PaymentProviderException -> 0x0189 }
        r2 = "Payment Payload JSON = ";	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = r1.append(r2);	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = r1.append(r7);	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = r1.toString();	 Catch:{ PaymentProviderException -> 0x0189 }
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);	 Catch:{ PaymentProviderException -> 0x0189 }
        r0 = r8.isIndirect();	 Catch:{ PaymentProviderException -> 0x0189 }
        if (r0 == 0) goto L_0x03ec;	 Catch:{ PaymentProviderException -> 0x0189 }
    L_0x03ad:
        r0 = r11.sendIndirectPaymentRequest(r8, r7);	 Catch:{ PaymentProviderException -> 0x0189 }
    L_0x03b1:
        r1 = "PaymentNetworkProvider";	 Catch:{ PaymentProviderException -> 0x0189 }
        r2 = new java.lang.StringBuilder;	 Catch:{ PaymentProviderException -> 0x0189 }
        r2.<init>();	 Catch:{ PaymentProviderException -> 0x0189 }
        r3 = "paymentPayloadJsonBytes ";	 Catch:{ PaymentProviderException -> 0x0189 }
        r2 = r2.append(r3);	 Catch:{ PaymentProviderException -> 0x0189 }
        r3 = new java.lang.String;	 Catch:{ PaymentProviderException -> 0x0189 }
        r3.<init>(r0);	 Catch:{ PaymentProviderException -> 0x0189 }
        r2 = r2.append(r3);	 Catch:{ PaymentProviderException -> 0x0189 }
        r2 = r2.toString();	 Catch:{ PaymentProviderException -> 0x0189 }
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r2);	 Catch:{ PaymentProviderException -> 0x0189 }
        r1 = r13.getContextId();	 Catch:{ PaymentProviderException -> 0x0189 }
        r14.onSuccess(r1, r0);	 Catch:{ PaymentProviderException -> 0x0189 }
        r0 = r11.mJwtRetryTriggered;
        if (r0 != 0) goto L_0x03e3;
    L_0x03d9:
        r0 = com.samsung.android.spayfw.core.PaymentFrameworkApp.az();
        r0.m619c(r10);
        r11.clearCardState();
    L_0x03e3:
        r0 = "PaymentNetworkProvider";
        r1 = "startInAppPay: end";
        com.samsung.android.spayfw.p002b.Log.m287i(r0, r1);
        goto L_0x0020;
    L_0x03ec:
        r0 = r7.getBytes();	 Catch:{ PaymentProviderException -> 0x0189 }
        goto L_0x03b1;
    L_0x03f1:
        r2 = -206; // 0xffffffffffffff32 float:NaN double:NaN;
        if (r0 != r2) goto L_0x0423;
    L_0x03f5:
        r0 = "PaymentNetworkProvider";	 Catch:{ all -> 0x024a }
        r2 = "Jwt Token Invalid";	 Catch:{ all -> 0x024a }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r2);	 Catch:{ all -> 0x024a }
        r0 = 45;	 Catch:{ all -> 0x024a }
        r2 = r13.getContextId();	 Catch:{ all -> 0x024a }
        r0 = com.samsung.android.spayfw.core.PaymentFrameworkMessage.m622a(r0, r2, r1, r7, r14);	 Catch:{ all -> 0x024a }
        r1 = com.samsung.android.spayfw.core.retry.JwtRetryRequester.bj();	 Catch:{ all -> 0x024a }
        r1.m664b(r0);	 Catch:{ all -> 0x024a }
        r11.updateJwtToken();	 Catch:{ all -> 0x024a }
        r0 = 1;	 Catch:{ all -> 0x024a }
        r11.mJwtRetryTriggered = r0;	 Catch:{ all -> 0x024a }
        r0 = r11.mJwtRetryTriggered;
        if (r0 != 0) goto L_0x0020;
    L_0x0417:
        r0 = com.samsung.android.spayfw.core.PaymentFrameworkApp.az();
        r0.m619c(r10);
        r11.clearCardState();
        goto L_0x0020;
    L_0x0423:
        r1 = r13.getContextId();	 Catch:{ all -> 0x024a }
        r14.onFail(r1, r0);	 Catch:{ all -> 0x024a }
        r0 = r11.mJwtRetryTriggered;
        if (r0 != 0) goto L_0x03e3;
    L_0x042e:
        r0 = com.samsung.android.spayfw.core.PaymentFrameworkApp.az();
        r0.m619c(r10);
        r11.clearCardState();
        goto L_0x03e3;
    L_0x0439:
        r0 = move-exception;
        r1 = r7;
        goto L_0x018b;
    L_0x043d:
        r0 = r7;
        goto L_0x0354;
    L_0x0440:
        r9 = r0;
        goto L_0x0275;
    L_0x0443:
        r1 = r7;
        goto L_0x025b;
    L_0x0446:
        r0 = r7;
        goto L_0x00de;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.startInAppPay(com.samsung.android.spayfw.appinterface.SecuredObject, com.samsung.android.spayfw.appinterface.InAppTransactionInfo, com.samsung.android.spayfw.appinterface.IInAppPayCallback):void");
    }

    public void getInAppToken(String str, MerchantInfo merchantInfo, String str2, IInAppPayCallback iInAppPayCallback) {
        Log.m287i(LOG_TAG, "getInAppToken: start");
        try {
            byte[] sendIndirectPaymentRequest = sendIndirectPaymentRequest(merchantInfo, str2);
            Log.m285d(LOG_TAG, "paymentPayloadJsonBytes " + new String(sendIndirectPaymentRequest));
            iInAppPayCallback.onSuccess(str, sendIndirectPaymentRequest);
        } catch (PaymentProviderException e) {
            int errorCode = e.getErrorCode();
            Log.m286e(LOG_TAG, "startInAppPay: PaymentProviderException: " + errorCode);
            if (checkIfStopInAppPayInvoked(iInAppPayCallback, str)) {
                Log.m285d(LOG_TAG, "Stop Pay invoked");
                return;
            }
            iInAppPayCallback.onFail(str, errorCode);
            PaymentFrameworkApp.az().m619c(TOAST_DEBUG);
            clearCardState();
        } finally {
            PaymentFrameworkApp.az().m619c(TOAST_DEBUG);
            clearCardState();
        }
        Log.m287i(LOG_TAG, "getInAppToken: end");
    }

    public void updateJwtToken() {
        Log.m287i(LOG_TAG, "updateJwtToken: start");
        Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
        intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_UPDATE_JWT_TOKEN);
        PaymentFrameworkApp.m315a(intent);
    }

    private byte[] sendIndirectPaymentRequest(MerchantInfo merchantInfo, String str) {
        Log.m285d(LOG_TAG, "Indirect payment");
        if (merchantInfo.getPgInfo() == null || merchantInfo.getPgInfo().getName() == null) {
            Log.m286e(LOG_TAG, "Payment pgInfo Name null ");
            throw new PaymentProviderException(-36);
        }
        PaymentRequest a = PaymentServiceClient.m1183J(this.mContext).m1184a(new PaymentRequestData(new PaymentRequestData.Card(null, str), new Payment(merchantInfo.getPgInfo().getName())));
        if (a == null) {
            Log.m286e(LOG_TAG, "paymentRequest null ");
            throw new PaymentProviderException(-36);
        }
        Response eS = a.eS();
        if (eS == null) {
            Log.m286e(LOG_TAG, "response null ");
            throw new PaymentProviderException(-36);
        }
        int statusCode = eS.getStatusCode();
        switch (statusCode) {
            case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                Log.m286e(LOG_TAG, "error Jwt token invalid " + statusCode);
                throw new PaymentProviderException(PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID);
            case ECCurve.COORD_AFFINE /*0*/:
                Log.m286e(LOG_TAG, "error unable to connect " + statusCode);
                throw new PaymentProviderException(-9);
            case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
            case 201:
                PaymentResponseData paymentResponseData = (PaymentResponseData) eS.getResult();
                if (paymentResponseData == null || paymentResponseData.getCard() == null) {
                    Log.m286e(LOG_TAG, "responseData or getCard null ");
                    throw new PaymentProviderException(-36);
                }
                String toJson = paymentResponseData.getCard().toJson();
                if (toJson == null) {
                    Log.m286e(LOG_TAG, "serverCardReference null ");
                    throw new PaymentProviderException(-36);
                }
                byte[] bytes = toJson.getBytes();
                Log.m285d(LOG_TAG, "paymentPayloadJsonBytes " + toJson);
                return bytes;
            default:
                Log.m286e(LOG_TAG, "error make payment " + statusCode);
                throw new PaymentProviderException(PaymentFramework.RESULT_CODE_FAIL_SERVER_REJECT);
        }
    }

    private boolean checkIfStopInAppPayInvoked(IInAppPayCallback iInAppPayCallback, String str) {
        try {
            if (State.m656p(PKIFailureInfo.certConfirmed)) {
                clearCardState();
                if (iInAppPayCallback != null) {
                    iInAppPayCallback.onFail(str, -7);
                }
                if (this.mStopPayCallback != null) {
                    this.mStopPayCallback.onSuccess(this.mStopPaySelectedCard);
                }
                this.mStopPayCallback = null;
                this.mStopPaySelectedCard = null;
                return true;
            }
        } catch (Exception e) {
            Log.m286e(LOG_TAG, "Exception in checkIfStopInAppPayInvoked");
        }
        return TOAST_DEBUG;
    }

    private void clearPayState() {
        Log.m285d(LOG_TAG, "clearPayState");
        this.mPayCallback = null;
        mMstPayThread = null;
        mPayTASelectedCard = null;
        mPayConfig = null;
        mRetryMode = TOAST_DEBUG;
        this.mAuthType = AUTHTYPE_NONE;
        this.forceQuit = TOAST_DEBUG;
        SPayHCEReceiver.aS();
    }

    public CasdParameters getCasdParameters() {
        return null;
    }

    public boolean setCasdCertificate(String str) {
        return true;
    }

    public void setPayAuthenticationMode(String str) {
    }

    public void updateRequestStatus(ProviderRequestStatus providerRequestStatus) {
    }

    public boolean isReplenishDataAvailable(JsonObject jsonObject) {
        return true;
    }

    private static void providerInterruptMstPayIfPossible() {
        if (mShouldInterrupt && mMstPayThread != null) {
            mMstPayThread.interrupt();
        }
        if (makeSysCallInternal(CREATE_TOKEN_SRC_IDV_RESPONSE)) {
            Log.m285d(LOG_TAG, "stop mst transimission");
        } else {
            Log.m285d(LOG_TAG, "cannot stop mst transimission");
        }
    }

    private static void providerResetMstPayIfPossible() {
        if (mMstPayThread != null) {
            mMstPayThread.interrupt();
        }
        if (makeSysCallInternal(CMD_RESET_MST)) {
            Log.m285d(LOG_TAG, "reset mst transimission");
        } else {
            Log.m285d(LOG_TAG, "cannot reset mst transimission");
        }
    }

    private static boolean makeSysCallInternal(int i) {
        String str;
        String str2;
        BufferedWriter bufferedWriter;
        Throwable e;
        Throwable e2;
        Writer writer;
        FileWriter fileWriter = null;
        switch (i) {
            case CREATE_TOKEN_SRC_PROV_RESPONSE /*1*/:
                str = FILE_MST_POWER_ON_OFF;
                str2 = TransactionInfo.VISA_TRANSACTIONTYPE_REFUND;
                break;
            case CREATE_TOKEN_SRC_PROV_PUSH /*2*/:
                str = FILE_MST_POWER_ON_OFF;
                str2 = TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE;
                break;
            case CREATE_TOKEN_SRC_IDV_RESPONSE /*3*/:
                str = FILE_MST_INTERRUPT;
                str2 = TransactionInfo.VISA_TRANSACTIONTYPE_REFUND;
                break;
            case CMD_RESET_MST /*4*/:
                str = FILE_MST_INTERRUPT;
                str2 = TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE;
                break;
            case CMD_MOVE_SEC_OS_CORE4 /*5*/:
                str = FILE_MIGRATE_SEC_OS;
                str2 = "b0";
                break;
            default:
                Log.m286e(LOG_TAG, "UNKNOWN Command ID: " + i);
                return TOAST_DEBUG;
        }
        if (DEBUG) {
            Log.m285d(LOG_TAG, "Writting \"" + str2 + "\" to -> " + str);
        }
        try {
            Writer fileWriter2 = new FileWriter(new File(str).getAbsoluteFile());
            try {
                bufferedWriter = new BufferedWriter(fileWriter2);
                try {
                    bufferedWriter.write(str2);
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.close();
                        } catch (Throwable e3) {
                            Log.m284c(LOG_TAG, e3.getMessage(), e3);
                        }
                    }
                    if (fileWriter2 != null) {
                        fileWriter2.close();
                    }
                    return true;
                } catch (IOException e4) {
                    e2 = e4;
                    writer = fileWriter2;
                    try {
                        Log.m286e(LOG_TAG, "Error writting \"" + str2 + "\" to file -> " + str);
                        Log.m284c(LOG_TAG, e2.getMessage(), e2);
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (Throwable e32) {
                                Log.m284c(LOG_TAG, e32.getMessage(), e32);
                                return TOAST_DEBUG;
                            }
                        }
                        if (fileWriter != null) {
                            fileWriter.close();
                        }
                        return TOAST_DEBUG;
                    } catch (Throwable th) {
                        e32 = th;
                        if (bufferedWriter != null) {
                            try {
                                bufferedWriter.close();
                            } catch (Throwable e5) {
                                Log.m284c(LOG_TAG, e5.getMessage(), e5);
                                throw e32;
                            }
                        }
                        if (fileWriter != null) {
                            fileWriter.close();
                        }
                        throw e32;
                    }
                } catch (Throwable th2) {
                    e32 = th2;
                    writer = fileWriter2;
                    if (bufferedWriter != null) {
                        bufferedWriter.close();
                    }
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                    throw e32;
                }
            } catch (IOException e6) {
                e2 = e6;
                bufferedWriter = null;
                writer = fileWriter2;
                Log.m286e(LOG_TAG, "Error writting \"" + str2 + "\" to file -> " + str);
                Log.m284c(LOG_TAG, e2.getMessage(), e2);
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }
                return TOAST_DEBUG;
            } catch (Throwable th3) {
                e32 = th3;
                bufferedWriter = null;
                writer = fileWriter2;
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }
                throw e32;
            }
        } catch (IOException e7) {
            e2 = e7;
            bufferedWriter = null;
            Log.m286e(LOG_TAG, "Error writting \"" + str2 + "\" to file -> " + str);
            Log.m284c(LOG_TAG, e2.getMessage(), e2);
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (fileWriter != null) {
                fileWriter.close();
            }
            return TOAST_DEBUG;
        } catch (Throwable th4) {
            e32 = th4;
            bufferedWriter = null;
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (fileWriter != null) {
                fileWriter.close();
            }
            throw e32;
        }
    }

    public final ProviderRequestData getVerifyIdvRequestDataTA(VerifyIdvInfo verifyIdvInfo) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            ProviderRequestData verifyIdvRequestData = getVerifyIdvRequestData(verifyIdvInfo);
            return verifyIdvRequestData;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    protected ProviderRequestData getVerifyIdvRequestData(VerifyIdvInfo verifyIdvInfo) {
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        return providerRequestData;
    }

    protected ProviderResponseData processIdvOptionsData(IdvMethod idvMethod) {
        ProviderResponseData providerResponseData = new ProviderResponseData();
        providerResponseData.setErrorCode(0);
        return providerResponseData;
    }

    public final ProviderResponseData processIdvOptionsDataTA(IdvMethod idvMethod) {
        Log.m285d(LOG_TAG, "processIdvOptionsDataTA");
        return processIdvOptionsData(idvMethod);
    }

    public final int getTransactionDataTA(Bundle bundle, TransactionResponse transactionResponse) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            int transactionData = getTransactionData(bundle, transactionResponse);
            return transactionData;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    public final ProviderRequestData getDeleteRequestDataTA(Bundle bundle) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            ProviderRequestData deleteRequestData = getDeleteRequestData(bundle);
            return deleteRequestData;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    protected ProviderRequestData getDeleteRequestData(Bundle bundle) {
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        return providerRequestData;
    }

    private boolean prepareMstPayInternal() {
        Log.m285d(LOG_TAG, "prepareMstPayInternal : Retry Mode : " + mRetryMode);
        if (mRetryMode) {
            return true;
        }
        return prepareMstPay();
    }

    private void stopMstPayInternal(boolean z) {
        if (mRetryMode) {
            stopMstPayLocked(z);
        }
        Log.m285d(LOG_TAG, "stopMstPayInternal : Retry Mode : " + mRetryMode);
    }

    private synchronized void stopMstPayLocked(boolean z) {
        stopMstPay(z);
    }

    private boolean prepareNfcPayInternal() {
        Log.m285d(LOG_TAG, "prepareNfcPayInternal : " + mRetryMode);
        return prepareNfcPay();
    }

    private Bundle stopNfcPayInternal(int i) {
        Log.m285d(LOG_TAG, "stopNfcPayInternal : " + mRetryMode);
        return stopNfcPay(i);
    }

    protected boolean allowPaymentRetry() {
        return true;
    }

    public boolean isPayAllowedForPresentationMode(int i) {
        return true;
    }

    public PayConfig getPayConfig() {
        return null;
    }

    public int getPayConfigTransmitTime(boolean z) {
        if (z) {
            return PayConfigurator.m613c("retry1", this.mCardBrand);
        }
        return PayConfigurator.m613c("default", this.mCardBrand);
    }

    public String getMerchantId() {
        return null;
    }

    public boolean isDsrpBlobMissing() {
        return TOAST_DEBUG;
    }

    public void reconstructMissingDsrpBlob() {
    }

    public void beginPay(boolean z, boolean z2) {
    }

    public void endPay() {
    }

    public final boolean isMstThreadStarted() {
        if (mMstPayThread == null || !mMstPayThread.isAlive()) {
            Log.m285d(LOG_TAG, "isMstThreadStarted false ");
            return TOAST_DEBUG;
        }
        Log.m285d(LOG_TAG, "isMstThreadStarted true ");
        return true;
    }

    public final byte[] decryptUserSignatureTA(byte[] bArr) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            byte[] decryptUserSignature = decryptUserSignature(new String(bArr));
            return decryptUserSignature;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    public final String encryptUserSignatureTA(byte[] bArr) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            String encryptUserSignature = encryptUserSignature(bArr);
            return encryptUserSignature;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    protected byte[] decryptUserSignature(String str) {
        return null;
    }

    protected String encryptUserSignature(byte[] bArr) {
        return null;
    }

    protected void replenishAlarmExpired() {
    }

    public void forceQuitMst() {
        if (mMstPayThread != null && mMstPayThread.isAlive()) {
            this.forceQuit = true;
            synchronized (mSwitchObj) {
                mSwitchObj.notifyAll();
            }
        }
    }

    public Bundle getTokenMetaData() {
        return null;
    }

    protected void updateTokenMetaData(JsonObject jsonObject, Token token) {
    }

    public void updateTokenMetaDataTA(JsonObject jsonObject, Token token) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            updateTokenMetaData(jsonObject, token);
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    public Bundle isTransactionComplete() {
        return null;
    }

    protected GlobalMembershipCardRegisterResponseData getGlobalMembershipCardRegisterData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData) {
        return null;
    }

    public GlobalMembershipCardRegisterResponseData getGlobalMembershipCardRegisterDataTA(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterData = getGlobalMembershipCardRegisterData(globalMembershipCardRegisterRequestData);
            return globalMembershipCardRegisterData;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    protected GlobalMembershipCardRegisterResponseData getGlobalMembershipCardTzEncData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData) {
        return null;
    }

    public GlobalMembershipCardRegisterResponseData getGlobalMembershipCardTzEncDataTA(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            GlobalMembershipCardRegisterResponseData globalMembershipCardTzEncData = getGlobalMembershipCardTzEncData(globalMembershipCardRegisterRequestData);
            return globalMembershipCardTzEncData;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }

    protected List<GlobalMembershipCardDetail> extractGlobalMembershipCardDetail(String[] strArr, byte[][] bArr) {
        return null;
    }

    public List<GlobalMembershipCardDetail> extractGlobalMembershipCardDetailTA(String[] strArr, byte[][] bArr) {
        try {
            loadTAwithCounter(TOAST_DEBUG);
            List<GlobalMembershipCardDetail> extractGlobalMembershipCardDetail = extractGlobalMembershipCardDetail(strArr, bArr);
            return extractGlobalMembershipCardDetail;
        } finally {
            unloadTAwithCounter(TOAST_DEBUG);
        }
    }
}
