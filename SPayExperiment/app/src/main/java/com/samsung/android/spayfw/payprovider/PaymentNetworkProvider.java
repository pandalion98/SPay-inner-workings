/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.ConditionVariable
 *  com.google.gson.JsonObject
 *  java.io.BufferedWriter
 *  java.io.File
 *  java.io.FileWriter
 *  java.io.IOException
 *  java.io.Writer
 *  java.lang.Exception
 *  java.lang.InterruptedException
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.util.Arrays
 *  java.util.List
 *  java.util.Objects
 *  java.util.TimerTask
 */
package com.samsung.android.spayfw.payprovider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ConditionVariable;
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
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.appinterface.VerifyIdvInfo;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.hce.SPayHCEReceiver;
import com.samsung.android.spayfw.core.k;
import com.samsung.android.spayfw.core.o;
import com.samsung.android.spayfw.core.q;
import com.samsung.android.spayfw.payprovider.MerchantServerRequester;
import com.samsung.android.spayfw.payprovider.PaymentProviderException;
import com.samsung.android.spayfw.payprovider.c;
import com.samsung.android.spayfw.payprovider.d;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.g;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.payprovider.j;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.ExtractCardDetailResult;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentRequestData;
import com.samsung.android.spayfw.remoteservice.commerce.models.PaymentResponseData;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.h;
import com.samsung.android.spaytui.AuthNonce;
import com.samsung.android.spaytui.AuthResult;
import com.samsung.android.spaytui.SpayTuiTAController;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.spaytzsvc.api.TAException;
import com.samsung.android.spaytzsvc.api.TAInfo;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.TimerTask;

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
    public static final boolean DEBUG = false;
    private static final String FILE_MIGRATE_SEC_OS = "/sys/devices/system/sec_os_ctrl/migrate_os";
    private static final String FILE_MST_INTERRUPT = "/dev/mst_ctrl";
    private static final String FILE_MST_POWER_ON_OFF = "/sys/class/mstldo/mst_drv/transmit";
    private static final String LOG_TAG = "PaymentNetworkProvider";
    public static final long NFC_WAIT_TIME = 30000L;
    private static final boolean TOAST_DEBUG = false;
    private static final String TUIPINSECUREOBJECTFILE = "mpt.dat";
    private static final String TUI_DATA_DIR = TAController.getEfsDirectory();
    private static final long UNLOAD_TIMER_EXPIRY_TIME = 60000L;
    private static SpayTuiTAController mAuthTAController;
    private static f mAuthTASelectedCard;
    private static Thread mMstPayThread;
    private static ConditionVariable mNfcWait;
    private static PayConfig mPayConfig;
    private static f mPayTASelectedCard;
    private static Object mPaymentModeObj;
    private static boolean mRetryMode;
    private static boolean mShouldInterrupt;
    private static Object mSwitchObj;
    private static g mTACounterFactory;
    private boolean forceQuit = false;
    private String mAuthType = "None";
    private String mCardBrand;
    protected Context mContext;
    protected String mEnrollmentId;
    private boolean mJwtRetryTriggered = false;
    private String mMstSequenceId = null;
    protected String mPFTokenStatus;
    private com.samsung.android.spayfw.payprovider.b mPayCallback = null;
    protected f mProviderTokenKey;
    private ICommonCallback mStopPayCallback = null;
    private String mStopPaySelectedCard = null;
    protected TAController mTAController = null;
    private g.a mTACounter;
    private j mUnloadTimer;

    static {
        DEBUG = h.DEBUG;
        mSwitchObj = new Object();
        mPaymentModeObj = new Object();
        mShouldInterrupt = false;
        mAuthTASelectedCard = null;
        mPayTASelectedCard = null;
        mPayConfig = null;
        mMstPayThread = null;
        mTACounterFactory = new g();
        mAuthTAController = null;
        mRetryMode = false;
        mNfcWait = null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    protected PaymentNetworkProvider(Context context, String string) {
        File file;
        this.mCardBrand = string;
        this.mContext = context;
        if (string != null && (string.equals((Object)"PL") || string.equals((Object)"GI") || string.equals((Object)"LO"))) {
            com.samsung.android.spayfw.b.c.d(LOG_TAG, "cardType =  " + string + " Plcc TA is used: Use Plcc unload " + "timer/TA Counter");
            this.mTACounter = mTACounterFactory.at("PL");
            this.mUnloadTimer = new j("PL");
        } else {
            com.samsung.android.spayfw.b.c.d(LOG_TAG, "cardType =  " + string + " Use card specific unload timer/TA " + "Counter");
            this.mTACounter = mTACounterFactory.at(string);
            this.mUnloadTimer = new j(this.mCardBrand);
        }
        if ((file = new File(TUI_DATA_DIR, TUIPINSECUREOBJECTFILE)).exists()) {
            if (!file.isDirectory()) return;
        }
        com.samsung.android.spayfw.b.c.i(LOG_TAG, "Creating Pin Random files");
        try {
            mAuthTAController = SpayTuiTAController.createOnlyInstance(this.mContext);
            if (mAuthTAController == null) return;
            mAuthTAController.loadTA();
            mAuthTAController.unloadTA();
            return;
        }
        catch (TAException tAException) {
            com.samsung.android.spayfw.b.c.c(LOG_TAG, tAException.getMessage(), (Throwable)((Object)tAException));
            return;
        }
    }

    static /* synthetic */ boolean access$600(int n2) {
        return PaymentNetworkProvider.makeSysCallInternal(n2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean checkIfStopInAppPayInvoked(IInAppPayCallback iInAppPayCallback, String string) {
        try {
            if (!o.p(4096)) return false;
            {
                this.clearCardState();
                if (iInAppPayCallback != null) {
                    iInAppPayCallback.onFail(string, -7);
                }
                if (this.mStopPayCallback != null) {
                    this.mStopPayCallback.onSuccess(this.mStopPaySelectedCard);
                }
                this.mStopPayCallback = null;
                this.mStopPaySelectedCard = null;
                return true;
            }
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.e(LOG_TAG, "Exception in checkIfStopInAppPayInvoked");
        }
        return false;
    }

    private void clearCardState() {
        com.samsung.android.spayfw.b.c.d(LOG_TAG, "clearCardState:");
        this.clearSecureObjectInputForPayment();
        this.mJwtRetryTriggered = false;
        try {
            this.clearPayState();
            return;
        }
        finally {
            this.clearCard();
            this.unloadTAwithCounter(true);
            o.q(1);
        }
    }

    private void clearPayState() {
        com.samsung.android.spayfw.b.c.d(LOG_TAG, "clearPayState");
        this.mPayCallback = null;
        mMstPayThread = null;
        mPayTASelectedCard = null;
        mPayConfig = null;
        mRetryMode = false;
        this.mAuthType = AUTHTYPE_NONE;
        this.forceQuit = false;
        SPayHCEReceiver.aS();
    }

    private void loadTAwithCounter(boolean bl) {
        com.samsung.android.spayfw.b.c.d(LOG_TAG, "load payment TA request : asyncTAloadUnload " + bl);
        this.mUnloadTimer.cancel();
        this.loadTA();
        if (bl) {
            this.mTACounter.co();
            com.samsung.android.spayfw.b.c.i(LOG_TAG, "load payment TA request with asyncTAloadUnload: count  " + this.mTACounter.getCount());
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    private static boolean makeSysCallInternal(int var0) {
        var1_1 = null;
        switch (var0) {
            default: {
                com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "UNKNOWN Command ID: " + var0);
                return false;
            }
            case 1: {
                var2_2 = "/sys/class/mstldo/mst_drv/transmit";
                var3_3 = "1";
lbl9: // 5 sources:
                do {
                    if (PaymentNetworkProvider.DEBUG) {
                        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "Writting \"" + var3_3 + "\" to -> " + var2_2);
                    }
                    var4_4 = new FileWriter(new File(var2_2).getAbsoluteFile());
                    var5_5 = new BufferedWriter((Writer)var4_4);
                    var5_5.write(var3_3);
                    if (var5_5 == null) ** GOTO lbl21
                    var5_5.close();
lbl21: // 2 sources:
                    if (var4_4 != null) {
                        var4_4.close();
                    }
lbl23: // 4 sources:
                    do {
                        return true;
                        break;
                    } while (true);
                    break;
                } while (true);
            }
            case 2: {
                var2_2 = "/sys/class/mstldo/mst_drv/transmit";
                var3_3 = "0";
                ** GOTO lbl9
            }
            case 3: {
                var2_2 = "/dev/mst_ctrl";
                var3_3 = "1";
                ** GOTO lbl9
            }
            case 4: {
                var2_2 = "/dev/mst_ctrl";
                var3_3 = "0";
                ** GOTO lbl9
            }
            case 5: 
        }
        var2_2 = "/sys/devices/system/sec_os_ctrl/migrate_os";
        var3_3 = "b0";
        ** while (true)
        catch (IOException var10_6) {
            com.samsung.android.spayfw.b.c.c("PaymentNetworkProvider", var10_6.getMessage(), var10_6);
            ** continue;
        }
        catch (IOException var6_7) {
            var5_5 = null;
lbl46: // 3 sources:
            do {
                com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "Error writting \"" + var3_3 + "\" to file -> " + var2_2);
                com.samsung.android.spayfw.b.c.c("PaymentNetworkProvider", var6_8.getMessage(), (Throwable)var6_8);
                if (var5_5 == null) ** GOTO lbl53
                try {
                    var5_5.close();
lbl53: // 2 sources:
                    if (var1_1 != null) {
                        var1_1.close();
                    }
lbl55: // 4 sources:
                    do {
                        return false;
                        break;
                    } while (true);
                }
                catch (IOException var9_11) {
                    com.samsung.android.spayfw.b.c.c("PaymentNetworkProvider", var9_11.getMessage(), var9_11);
                    ** continue;
                }
                break;
            } while (true);
        }
        catch (Throwable var7_12) {
            var5_5 = null;
lbl62: // 4 sources:
            do {
                block33 : {
                    if (var5_5 == null) ** GOTO lbl66
                    try {
                        var5_5.close();
lbl66: // 2 sources:
                        if (var1_1 == null) break block33;
                        var1_1.close();
                    }
                    catch (IOException var8_17) {
                        com.samsung.android.spayfw.b.c.c("PaymentNetworkProvider", var8_17.getMessage(), var8_17);
                        ** continue;
                    }
                }
lbl72: // 2 sources:
                do {
                    throw var7_13;
                    break;
                } while (true);
                break;
            } while (true);
        }
        catch (Throwable var7_14) {
            var1_1 = var4_4;
            var5_5 = null;
            ** GOTO lbl62
        }
        catch (Throwable var7_15) {
            var1_1 = var4_4;
            ** GOTO lbl62
        }
        {
            catch (Throwable var7_16) {
                ** continue;
            }
        }
        catch (IOException var6_9) {
            var1_1 = var4_4;
            var5_5 = null;
            ** GOTO lbl46
        }
        catch (IOException var6_10) {
            var1_1 = var4_4;
            ** continue;
        }
    }

    private boolean prepareMstPayInternal() {
        com.samsung.android.spayfw.b.c.d(LOG_TAG, "prepareMstPayInternal : Retry Mode : " + mRetryMode);
        if (!mRetryMode) {
            return this.prepareMstPay();
        }
        return true;
    }

    private boolean prepareNfcPayInternal() {
        com.samsung.android.spayfw.b.c.d(LOG_TAG, "prepareNfcPayInternal : " + mRetryMode);
        return this.prepareNfcPay();
    }

    private static void providerInterruptMstPayIfPossible() {
        if (mShouldInterrupt && mMstPayThread != null) {
            mMstPayThread.interrupt();
        }
        if (PaymentNetworkProvider.makeSysCallInternal(3)) {
            com.samsung.android.spayfw.b.c.d(LOG_TAG, "stop mst transimission");
            return;
        }
        com.samsung.android.spayfw.b.c.d(LOG_TAG, "cannot stop mst transimission");
    }

    private static void providerResetMstPayIfPossible() {
        if (mMstPayThread != null) {
            mMstPayThread.interrupt();
        }
        if (PaymentNetworkProvider.makeSysCallInternal(4)) {
            com.samsung.android.spayfw.b.c.d(LOG_TAG, "reset mst transimission");
            return;
        }
        com.samsung.android.spayfw.b.c.d(LOG_TAG, "cannot reset mst transimission");
    }

    private SelectCardResult selectCardForPayment() {
        com.samsung.android.spayfw.b.c.d(LOG_TAG, "selectCardForPayment: mPayTASelectedCard " + mPayTASelectedCard);
        this.loadTAwithCounter(true);
        return this.selectCard();
    }

    private byte[] sendIndirectPaymentRequest(MerchantServerRequester.MerchantInfo merchantInfo, String string) {
        com.samsung.android.spayfw.b.c.d(LOG_TAG, "Indirect payment");
        if (merchantInfo.getPgInfo() == null || merchantInfo.getPgInfo().getName() == null) {
            com.samsung.android.spayfw.b.c.e(LOG_TAG, "Payment pgInfo Name null ");
            throw new PaymentProviderException(-36);
        }
        com.samsung.android.spayfw.remoteservice.commerce.a a2 = com.samsung.android.spayfw.remoteservice.commerce.b.J(this.mContext).a(new PaymentRequestData(new PaymentRequestData.Card(null, string), new PaymentRequestData.Payment(merchantInfo.getPgInfo().getName())));
        if (a2 == null) {
            com.samsung.android.spayfw.b.c.e(LOG_TAG, "paymentRequest null ");
            throw new PaymentProviderException(-36);
        }
        com.samsung.android.spayfw.remoteservice.c c2 = a2.eS();
        if (c2 == null) {
            com.samsung.android.spayfw.b.c.e(LOG_TAG, "response null ");
            throw new PaymentProviderException(-36);
        }
        int n2 = c2.getStatusCode();
        switch (n2) {
            default: {
                com.samsung.android.spayfw.b.c.e(LOG_TAG, "error make payment " + n2);
                throw new PaymentProviderException(-202);
            }
            case 200: 
            case 201: {
                PaymentResponseData paymentResponseData = (PaymentResponseData)c2.getResult();
                if (paymentResponseData == null || paymentResponseData.getCard() == null) {
                    com.samsung.android.spayfw.b.c.e(LOG_TAG, "responseData or getCard null ");
                    throw new PaymentProviderException(-36);
                }
                String string2 = paymentResponseData.getCard().toJson();
                if (string2 == null) {
                    com.samsung.android.spayfw.b.c.e(LOG_TAG, "serverCardReference null ");
                    throw new PaymentProviderException(-36);
                }
                byte[] arrby = string2.getBytes();
                com.samsung.android.spayfw.b.c.d(LOG_TAG, "paymentPayloadJsonBytes " + string2);
                return arrby;
            }
            case 0: {
                com.samsung.android.spayfw.b.c.e(LOG_TAG, "error unable to connect " + n2);
                throw new PaymentProviderException(-9);
            }
            case -2: 
        }
        com.samsung.android.spayfw.b.c.e(LOG_TAG, "error Jwt token invalid " + n2);
        throw new PaymentProviderException(-206);
    }

    private void stopMstPayInternal(boolean bl) {
        if (mRetryMode) {
            this.stopMstPayLocked(bl);
        }
        com.samsung.android.spayfw.b.c.d(LOG_TAG, "stopMstPayInternal : Retry Mode : " + mRetryMode);
    }

    private void stopMstPayLocked(boolean bl) {
        PaymentNetworkProvider paymentNetworkProvider = this;
        synchronized (paymentNetworkProvider) {
            this.stopMstPay(bl);
            return;
        }
    }

    private Bundle stopNfcPayInternal(int n2) {
        com.samsung.android.spayfw.b.c.d(LOG_TAG, "stopNfcPayInternal : " + mRetryMode);
        return this.stopNfcPay(n2);
    }

    private void unloadTAwithCounter(boolean bl) {
        if (bl) {
            this.mTACounter.cp();
            com.samsung.android.spayfw.b.c.i(LOG_TAG, "unload payment TA request with  asyncTAloadUnload. decerment asyncloadUnload count ");
        }
        com.samsung.android.spayfw.b.c.i(LOG_TAG, "unload payment TA request: asyncLoadUnloadCount: " + this.mTACounter.getCount());
        if (this.mTACounter.getCount() <= 0) {
            this.mUnloadTimer.schedule(new b(), 60000L);
            this.mTACounter.reset();
            return;
        }
        com.samsung.android.spayfw.b.c.i(LOG_TAG, "unload payment TA is delayed becasue of asyncLoadUnload : ");
    }

    protected boolean allowPaymentRetry() {
        return true;
    }

    protected abstract boolean authenticateTransaction(SecuredObject var1);

    public void beginPay(boolean bl, boolean bl2) {
    }

    public void checkIfReplenishmentNeeded(TransactionData transactionData) {
    }

    protected abstract void clearCard();

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void clearPay() {
        Object object;
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "clearPay:");
        if (!this.mTAController.makeSystemCall(2)) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "Error: Failed to turn OFF MST");
        }
        this.mTAController.moveSecOsToDefaultCore();
        PaymentNetworkProvider.providerResetMstPayIfPossible();
        this.clearSecureObjectInputForPayment();
        if (mNfcWait != null) {
            mNfcWait.open();
        }
        if (mPayTASelectedCard == null) {
            com.samsung.android.spayfw.b.c.w("PaymentNetworkProvider", "clearPay: mPayTASelectedCard " + mPayTASelectedCard);
            return;
        }
        Object object2 = object = mPaymentModeObj;
        synchronized (object2) {
            mPaymentModeObj.notifyAll();
        }
        try {
            this.clearPayState();
            return;
        }
        finally {
            this.clearCard();
            this.unloadTAwithCounter(true);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final void clearRetryPay() {
        Object object;
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "clearRetryPay:");
        mRetryMode = true;
        this.stopMstPayInternal(true);
        if (!this.mTAController.makeSystemCall(2)) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "Error: Failed to turn OFF MST");
        }
        this.mTAController.moveSecOsToDefaultCore();
        PaymentNetworkProvider.providerResetMstPayIfPossible();
        this.clearSecureObjectInputForPayment();
        if (mNfcWait != null) {
            mNfcWait.open();
        }
        if (mPayTASelectedCard == null) {
            com.samsung.android.spayfw.b.c.w("PaymentNetworkProvider", "clearPay: mPayTASelectedCard " + mPayTASelectedCard);
            this.clearPayState();
            return;
        }
        Object object2 = object = mPaymentModeObj;
        synchronized (object2) {
            mPaymentModeObj.notifyAll();
        }
        try {
            this.clearPayState();
            return;
        }
        finally {
            this.clearCard();
            this.unloadTAwithCounter(true);
        }
    }

    public final void clearSecureObjectInputForPayment() {
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "clearSecureObjectInputForPayment: mAuthTASelectedCard " + mAuthTASelectedCard);
        if (mAuthTAController != null) {
            mAuthTAController.unloadTA();
        }
        mAuthTASelectedCard = null;
    }

    protected abstract e createToken(String var1, c var2, int var3);

    public final e createTokenTA(String string, c c2, int n2) {
        e e2;
        block4 : {
            this.loadTAwithCounter(false);
            e2 = this.createToken(string, c2, n2);
            if (e2 == null) break block4;
            if (e2.getProviderTokenKey() == null) break block4;
            e2.getProviderTokenKey().setTrTokenId(string);
            this.mProviderTokenKey = e2.getProviderTokenKey();
        }
        return e2;
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    protected byte[] decryptUserSignature(String string) {
        return null;
    }

    public final byte[] decryptUserSignatureTA(byte[] arrby) {
        try {
            this.loadTAwithCounter(false);
            byte[] arrby2 = this.decryptUserSignature(new String(arrby));
            return arrby2;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    public abstract void delete();

    protected String encryptUserSignature(byte[] arrby) {
        return null;
    }

    public final String encryptUserSignatureTA(byte[] arrby) {
        try {
            this.loadTAwithCounter(false);
            String string = this.encryptUserSignature(arrby);
            return string;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    public void endPay() {
    }

    protected GiftCardDetail extractGiftCardDetail(byte[] arrby, byte[] arrby2) {
        return null;
    }

    public final GiftCardDetail extractGiftCardDetailTA(byte[] arrby, byte[] arrby2, SecuredObject securedObject, boolean bl) {
        com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "extractGiftCardDetailTA:");
        GiftCardDetail giftCardDetail = new GiftCardDetail();
        giftCardDetail.setErrorCode(-1);
        if (securedObject == null || arrby2 == null) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "extractGiftCardDetailTA:invalid input");
            giftCardDetail.setErrorCode(-5);
            return giftCardDetail;
        }
        if (!o.p(2)) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "extractGiftCardDetailTA: invalid state");
            return giftCardDetail;
        }
        if (mAuthTAController == null) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "extractGiftCardDetailTA:auth TA controller instance is null ");
            return giftCardDetail;
        }
        mPayTASelectedCard = mAuthTASelectedCard;
        if (mPayTASelectedCard == null) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "extractGiftCardDetailTA:selectCard is not called ");
            return giftCardDetail;
        }
        SelectCardResult selectCardResult = this.selectCardForPayment();
        if (selectCardResult == null || selectCardResult.getNonce() == null || selectCardResult.getTaid() == null) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "extractGiftCardDetailTA:selectCard result from pay provider is not valid ");
            this.clearCardState();
            return giftCardDetail;
        }
        AuthResult authResult = mAuthTAController.getAuthResult(selectCardResult.getNonce(), selectCardResult.getTaid(), securedObject.getSecureObjectData());
        byte[] arrby3 = null;
        if (authResult != null) {
            arrby3 = authResult.getSecObjData();
            this.mAuthType = authResult.getAuthType();
            com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "extractGiftCardDetailTA:auth type =  " + this.mAuthType);
        }
        if (arrby3 == null) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "extractGiftCardDetailTA:authentication failed from auth TA ");
            giftCardDetail.setErrorCode(-35);
            this.clearCardState();
            return giftCardDetail;
        }
        SecuredObject securedObject2 = new SecuredObject();
        securedObject2.setSecureObjectData(arrby3);
        if (!this.authenticateTransaction(securedObject2)) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "extractGiftCardDetailTA:authentication failed ");
            this.clearCardState();
            return giftCardDetail;
        }
        if (!o.q(16)) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "extractGiftCardDetailTA: cannot go to pay IDLE state");
            this.clearCardState();
            return giftCardDetail;
        }
        if (!o.q(8192)) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "extractGiftCardDetailTA: cannot go to pay PAY_EXTRACT_CARDDETAIL state");
            this.clearCardState();
            return giftCardDetail;
        }
        PaymentFrameworkApp.az().c(true);
        GiftCardDetail giftCardDetail2 = this.extractGiftCardDetail(arrby, arrby2);
        PaymentFrameworkApp.az().c(false);
        if (bl) {
            if (!o.q(2)) {
                com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "extractGiftCardDetailTA: cannot go to pay Selected state");
                this.clearCardState();
                return giftCardDetail2;
            }
            com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "extractGiftCardDetailTA: Start MST Pay Requested. State changed to NPAY_SELECTED");
            return giftCardDetail2;
        }
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "extractGiftCardDetailTA: No MST Pay Requested. Clear Card State");
        this.clearCardState();
        return giftCardDetail2;
    }

    protected List<GlobalMembershipCardDetail> extractGlobalMembershipCardDetail(String[] arrstring, byte[][] arrby) {
        return null;
    }

    public List<GlobalMembershipCardDetail> extractGlobalMembershipCardDetailTA(String[] arrstring, byte[][] arrby) {
        try {
            this.loadTAwithCounter(false);
            List<GlobalMembershipCardDetail> list = this.extractGlobalMembershipCardDetail(arrstring, arrby);
            return list;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    protected ExtractCardDetailResult extractLoyaltyCardDetail(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest) {
        return null;
    }

    public final ExtractCardDetailResult extractLoyaltyCardDetailTA(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest) {
        try {
            this.loadTAwithCounter(false);
            ExtractCardDetailResult extractCardDetailResult = this.extractLoyaltyCardDetail(extractLoyaltyCardDetailRequest);
            return extractCardDetailResult;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void forceQuitMst() {
        if (mMstPayThread != null && mMstPayThread.isAlive()) {
            Object object;
            this.forceQuit = true;
            Object object2 = object = mSwitchObj;
            synchronized (object2) {
                mSwitchObj.notifyAll();
                return;
            }
        }
    }

    protected abstract byte[] generateInAppPaymentPayload(InAppDetailedTransactionInfo var1);

    protected String getAuthType() {
        return this.mAuthType;
    }

    public com.samsung.android.spayfw.payprovider.a getCasdParameters() {
        return null;
    }

    protected c getDeleteRequestData(Bundle bundle) {
        c c2 = new c();
        c2.setErrorCode(0);
        return c2;
    }

    public final c getDeleteRequestDataTA(Bundle bundle) {
        try {
            this.loadTAwithCounter(false);
            c c2 = this.getDeleteRequestData(bundle);
            return c2;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    protected abstract CertificateInfo[] getDeviceCertificates();

    public final CertificateInfo[] getDeviceCertificatesTA() {
        try {
            this.loadTAwithCounter(false);
            CertificateInfo[] arrcertificateInfo = this.getDeviceCertificates();
            return arrcertificateInfo;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    protected abstract c getEnrollmentRequestData(EnrollCardInfo var1, BillingInfo var2);

    public final c getEnrollmentRequestDataTA(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        try {
            this.loadTAwithCounter(false);
            c c2 = this.getEnrollmentRequestData(enrollCardInfo, billingInfo);
            return c2;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    public GiftCardRegisterResponseData getGiftCardRegisterData(GiftCardRegisterRequestData giftCardRegisterRequestData) {
        return null;
    }

    public GiftCardRegisterResponseData getGiftCardRegisterDataTA(GiftCardRegisterRequestData giftCardRegisterRequestData) {
        try {
            this.loadTAwithCounter(false);
            GiftCardRegisterResponseData giftCardRegisterResponseData = this.getGiftCardRegisterData(giftCardRegisterRequestData);
            return giftCardRegisterResponseData;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    public GiftCardRegisterResponseData getGiftCardTzEncData(GiftCardRegisterRequestData giftCardRegisterRequestData) {
        return null;
    }

    public GiftCardRegisterResponseData getGiftCardTzEncDataTA(GiftCardRegisterRequestData giftCardRegisterRequestData) {
        try {
            this.loadTAwithCounter(false);
            GiftCardRegisterResponseData giftCardRegisterResponseData = this.getGiftCardTzEncData(giftCardRegisterRequestData);
            return giftCardRegisterResponseData;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    protected GlobalMembershipCardRegisterResponseData getGlobalMembershipCardRegisterData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData) {
        return null;
    }

    public GlobalMembershipCardRegisterResponseData getGlobalMembershipCardRegisterDataTA(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData) {
        try {
            this.loadTAwithCounter(false);
            GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData = this.getGlobalMembershipCardRegisterData(globalMembershipCardRegisterRequestData);
            return globalMembershipCardRegisterResponseData;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    protected GlobalMembershipCardRegisterResponseData getGlobalMembershipCardTzEncData(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData) {
        return null;
    }

    public GlobalMembershipCardRegisterResponseData getGlobalMembershipCardTzEncDataTA(GlobalMembershipCardRegisterRequestData globalMembershipCardRegisterRequestData) {
        try {
            this.loadTAwithCounter(false);
            GlobalMembershipCardRegisterResponseData globalMembershipCardRegisterResponseData = this.getGlobalMembershipCardTzEncData(globalMembershipCardRegisterRequestData);
            return globalMembershipCardRegisterResponseData;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void getInAppToken(String string, MerchantServerRequester.MerchantInfo merchantInfo, String string2, IInAppPayCallback iInAppPayCallback) {
        com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "getInAppToken: start");
        try {
            byte[] arrby = this.sendIndirectPaymentRequest(merchantInfo, string2);
            com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "paymentPayloadJsonBytes " + new String(arrby));
            iInAppPayCallback.onSuccess(string, arrby);
        }
        catch (PaymentProviderException paymentProviderException) {
            int n2 = paymentProviderException.getErrorCode();
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "startInAppPay: PaymentProviderException: " + n2);
            if (this.checkIfStopInAppPayInvoked(iInAppPayCallback, string)) {
                com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "Stop Pay invoked");
                return;
            }
            iInAppPayCallback.onFail(string, n2);
        }
        finally {
            PaymentFrameworkApp.az().c(false);
            this.clearCardState();
        }
        com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "getInAppToken: end");
    }

    public String getMerchantId() {
        return null;
    }

    public String getPFTokenStatus() {
        return this.mPFTokenStatus;
    }

    public PayConfig getPayConfig() {
        return null;
    }

    public int getPayConfigTransmitTime(boolean bl) {
        if (bl) {
            return com.samsung.android.spayfw.core.h.c("retry1", this.mCardBrand);
        }
        return com.samsung.android.spayfw.core.h.c("default", this.mCardBrand);
    }

    public abstract boolean getPayReadyState();

    public final f getProviderTokenKey() {
        return this.mProviderTokenKey;
    }

    protected abstract c getProvisionRequestData(ProvisionTokenInfo var1);

    public final c getProvisionRequestDataTA(ProvisionTokenInfo provisionTokenInfo) {
        try {
            this.loadTAwithCounter(false);
            c c2 = this.getProvisionRequestData(provisionTokenInfo);
            return c2;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    protected abstract c getReplenishmentRequestData();

    public final c getReplenishmentRequestDataTA() {
        try {
            this.loadTAwithCounter(false);
            c c2 = this.getReplenishmentRequestData();
            return c2;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public final SelectCardResult getSecureObjectInputForPayment(boolean bl) {
        byte[] arrby;
        SelectCardResult selectCardResult;
        int n2 = 1;
        int n3 = 0;
        if (this.mProviderTokenKey == null) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "providerTokenKey is null. can't get secObj input");
            return null;
        }
        if (bl) {
            com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "skipping loading auth TA");
            SelectCardResult selectCardResult2 = new SelectCardResult();
            selectCardResult2.setStatus(0);
            mAuthTASelectedCard = this.mProviderTokenKey;
            return selectCardResult2;
        }
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "getSecureObjectInputForPayment: ");
        mAuthTAController = SpayTuiTAController.createOnlyInstance(this.mContext);
        if (mAuthTAController == null) {
            return null;
        }
        mAuthTAController.loadTA();
        AuthNonce authNonce = mAuthTAController.getCachedNonce(32, (boolean)n2);
        String string = mAuthTAController.getTAInfo().getTAId();
        if (authNonce != null && string != null && (arrby = authNonce.getNonce()) != null && arrby.length > 0) {
            com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "nonce: " + Arrays.toString((byte[])arrby));
            com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "taid: " + string);
            if (!authNonce.isFromCache()) {
                n2 = 0;
            }
            SelectCardResult selectCardResult3 = new SelectCardResult();
            selectCardResult3.setNonce(arrby);
            selectCardResult3.setTaid(string);
            selectCardResult3.setStatus(n2);
            mAuthTASelectedCard = this.mProviderTokenKey;
            selectCardResult = selectCardResult3;
        } else {
            n3 = n2;
            selectCardResult = null;
        }
        if (n3 == 0) return selectCardResult;
        com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "Error occurred unloading auth TA");
        mAuthTAController.unloadTA();
        return selectCardResult;
    }

    public Bundle getTokenMetaData() {
        return null;
    }

    protected abstract int getTransactionData(Bundle var1, i var2);

    public final int getTransactionDataTA(Bundle bundle, i i2) {
        try {
            this.loadTAwithCounter(false);
            int n2 = this.getTransactionData(bundle, i2);
            return n2;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    protected c getVerifyIdvRequestData(VerifyIdvInfo verifyIdvInfo) {
        c c2 = new c();
        c2.setErrorCode(0);
        return c2;
    }

    public final c getVerifyIdvRequestDataTA(VerifyIdvInfo verifyIdvInfo) {
        try {
            this.loadTAwithCounter(false);
            c c2 = this.getVerifyIdvRequestData(verifyIdvInfo);
            return c2;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    protected abstract byte[] handleApdu(byte[] var1, Bundle var2);

    protected abstract void init();

    protected abstract void interruptMstPay();

    public boolean isDsrpBlobMissing() {
        return false;
    }

    public final boolean isMstThreadStarted() {
        if (mMstPayThread != null && mMstPayThread.isAlive()) {
            com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "isMstThreadStarted true ");
            return true;
        }
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "isMstThreadStarted false ");
        return false;
    }

    public boolean isPayAllowedForPresentationMode(int n2) {
        return true;
    }

    public boolean isReplenishDataAvailable(JsonObject jsonObject) {
        return true;
    }

    public Bundle isTransactionComplete() {
        return null;
    }

    protected abstract void loadTA();

    protected void onPaySwitch(int n2, int n3) {
        if (n2 == 2 && n3 == 1) {
            com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "switch payment method from MST to NFC");
        }
    }

    protected String prepareLoyaltyDataForServer(String string) {
        return null;
    }

    public final String prepareLoyaltyDataForServerTA(String string) {
        try {
            this.loadTAwithCounter(false);
            String string2 = this.prepareLoyaltyDataForServer(string);
            return string2;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    protected abstract boolean prepareMstPay();

    protected abstract boolean prepareNfcPay();

    /*
     * Enabled aggressive block sorting
     */
    public final byte[] processApdu(byte[] arrby, Bundle bundle) {
        if (mNfcWait != null) {
            mNfcWait.open();
        }
        if (!o.r(32)) {
            com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "cannot continue the payment, state has changed");
            return null;
        }
        if (o.p(82)) {
            if (!this.prepareNfcPayInternal()) {
                com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "prepareNfcPay: setup error ");
                if (this.mPayCallback == null) return null;
                {
                    this.mPayCallback.a(null, -11, this.mAuthType);
                    return null;
                }
            }
            boolean bl = o.p(64);
            if (!o.q(32)) {
                if (this.mPayCallback == null) return null;
                {
                    this.mPayCallback.a(null, -36, this.mAuthType);
                    return null;
                }
            }
            PaymentNetworkProvider.providerInterruptMstPayIfPossible();
            if (bl) {
                com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "onPaySwitch (MST -> NFC): start: " + System.currentTimeMillis());
                this.onPaySwitch(2, 1);
                if (this.mPayCallback != null) {
                    this.mPayCallback.a(null, 2, 1, this.mAuthType);
                }
                com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "onPaySwitch end= " + System.currentTimeMillis());
            }
        }
        com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "handleApdu SDK start: currentTime" + System.currentTimeMillis());
        byte[] arrby2 = this.handleApdu(arrby, bundle);
        com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "handleApdu SDK end:  currentTime" + System.currentTimeMillis());
        return arrby2;
    }

    protected e processIdvOptionsData(IdvMethod idvMethod) {
        e e2 = new e();
        e2.setErrorCode(0);
        return e2;
    }

    public final e processIdvOptionsDataTA(IdvMethod idvMethod) {
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "processIdvOptionsDataTA");
        return this.processIdvOptionsData(idvMethod);
    }

    public final Bundle processTransacionComplete(int n2) {
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "processTransacionComplete: stop nfc flag " + n2);
        return this.stopNfcPayInternal(n2);
    }

    protected abstract TransactionDetails processTransactionData(Object var1);

    public final TransactionDetails processTransactionDataTA(Object object) {
        try {
            this.loadTAwithCounter(false);
            TransactionDetails transactionDetails = this.processTransactionData(object);
            return transactionDetails;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    public final void providerInit() {
        try {
            this.loadTAwithCounter(false);
            this.init();
            return;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    public void reconstructMissingDsrpBlob() {
    }

    protected void replenishAlarmExpired() {
    }

    protected abstract e replenishToken(JsonObject var1, TokenStatus var2);

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final e replenishTokenTA(JsonObject jsonObject, TokenStatus tokenStatus) {
        Object object;
        Object object2 = object = mPaymentModeObj;
        synchronized (object2) {
            boolean bl = PaymentFrameworkApp.az().aK();
            if (bl) {
                try {
                    com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "Paymentmode is ON: putting replenish thread to sleep");
                    mPaymentModeObj.wait();
                }
                catch (InterruptedException interruptedException) {
                    com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "switch obj wait interrupt exception");
                    com.samsung.android.spayfw.b.c.c("PaymentNetworkProvider", interruptedException.getMessage(), interruptedException);
                }
                com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "Paymentmode is OFF: replenish thread to awake from sleep");
            }
        }
        try {
            this.loadTAwithCounter(false);
            e e2 = this.replenishToken(jsonObject, tokenStatus);
            return e2;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final int retryPay(PayConfig payConfig) {
        com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "retryPay:");
        if (this.mPayCallback == null) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "retryPay : invalid input");
            return -44;
        }
        if (!o.p(2)) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", " retryPay: already pay in progress.can't start one more retryPay");
            this.mPayCallback.a(null, -4, this.mAuthType);
            return -4;
        }
        if (mAuthTAController == null) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "retryPay : auth TA controller instance is null ");
            this.mPayCallback.a(null, -36, this.mAuthType);
            return -36;
        }
        mPayTASelectedCard = mAuthTASelectedCard;
        mPayConfig = payConfig;
        if (mPayTASelectedCard == null) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "retryPay : selectCard is not called ");
            this.mPayCallback.a(null, -36, this.mAuthType);
            return -36;
        }
        if (!o.q(16)) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "retryPay : cannot go to pay ilde");
            this.mPayCallback.a(null, -4, this.mAuthType);
            return -4;
        }
        if (payConfig != null && payConfig.getPayType() == 1) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "retryPay : payType should not be NFC");
            this.mPayCallback.a(null, -4, this.mAuthType);
            return -4;
        }
        this.setPayAuthenticationMode(this.mAuthType);
        if (payConfig != null && payConfig.getPayType() == 2) {
            this.mMstSequenceId = com.samsung.android.spayfw.core.h.f("retry1", this.mCardBrand);
            if (mMstPayThread != null && mMstPayThread.isAlive()) {
                com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "wait for MST pay thread");
                try {
                    mMstPayThread.join();
                }
                catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
            mRetryMode = true;
            mMstPayThread = new Thread((Runnable)new a());
            mMstPayThread.start();
            com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "started MST pay thread");
        }
        return 0;
    }

    protected abstract SelectCardResult selectCard();

    public void setCardTzEncData(byte[] arrby) {
    }

    public boolean setCasdCertificate(String string) {
        return true;
    }

    public final void setEnrollmentId(String string) {
        this.mEnrollmentId = string;
    }

    public void setPFTokenStatus(String string) {
        this.mPFTokenStatus = string;
    }

    public void setPayAuthenticationMode(String string) {
    }

    public void setPaymentFrameworkRequester(k k2) {
    }

    public final void setProviderTokenKey(f f2) {
        this.mProviderTokenKey = f2;
    }

    public abstract boolean setServerCertificates(CertificateInfo[] var1);

    public void setupReplenishAlarm() {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final void setupReplenishAlarm(com.samsung.android.spayfw.core.c c2) {
        if (c2 == null) return;
        try {
            if (c2.ac() == null || c2.ac().getTokenId() == null) return;
            this.setupReplenishAlarm();
            return;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.c("PaymentNetworkProvider", exception.getMessage(), exception);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public final boolean shouldTransitBackToMst() {
        Object object;
        boolean bl = false;
        if (!o.p(8)) {
            com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "nfc is done, no need to transit mst again");
        } else {
            com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "contine to transit mst but stopping nfc");
            if (this.mPayCallback != null) {
                com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "onPaySwitch (NFC -> MST): start: " + System.currentTimeMillis());
                this.mPayCallback.a(null, 1, 2, this.mAuthType);
            }
            if (!PaymentNetworkProvider.makeSysCallInternal(4)) {
                com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "Error: cannot reset mst transimission");
            }
            if (!this.mTAController.makeSystemCall(1)) {
                com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "Error: Failed to turn MST Driver on");
            }
            bl = true;
        }
        Object object2 = object = mSwitchObj;
        synchronized (object2) {
            mSwitchObj.notifyAll();
            return bl;
        }
    }

    /*
     * Exception decompiling
     */
    public final void startInAppPay(SecuredObject var1_1, InAppTransactionInfo var2_2, IInAppPayCallback var3_3) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
        // org.benf.cfr.reader.b.a.a.j.b(Op04StructuredStatement.java:409)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:487)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    public abstract boolean startMstPay(int var1, byte[] var2);

    /*
     * Enabled aggressive block sorting
     */
    public final void startPay(PayConfig payConfig, SecuredObject securedObject, com.samsung.android.spayfw.payprovider.b b2, boolean bl) {
        int n2 = 1;
        com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "startPay:");
        if (!bl && securedObject == null || b2 == null) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "startPay:invalid input");
            if (b2 != null) {
                b2.a(null, -36, this.mAuthType);
            }
            return;
        }
        this.mPayCallback = b2;
        if (mRetryMode) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "startPay: In Retry Mode. Do not call Start Pay, call Retry Pay or Stop Pay.");
            this.mPayCallback.a(null, -4, this.mAuthType);
            return;
        }
        if (!o.p(2)) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "startPay: already pay in progress.can't start one more startPay");
            this.mPayCallback.a(null, -4, this.mAuthType);
            return;
        }
        mPayConfig = payConfig;
        if (bl) {
            com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "startPay: auth alive: skip authentication process");
            if (mPayTASelectedCard == null) {
                mPayTASelectedCard = mAuthTASelectedCard;
                if (mPayTASelectedCard == null) {
                    com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "startPay:selectCard is not called ");
                    this.mPayCallback.a(null, -36, this.mAuthType);
                    return;
                }
                SelectCardResult selectCardResult = this.selectCardForPayment();
                if (selectCardResult == null || selectCardResult.getNonce() == null || selectCardResult.getTaid() == null) {
                    com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "startPay:selectCard result from pay provider is not valid ");
                    this.mPayCallback.a(null, -36, this.mAuthType);
                    return;
                }
            }
        } else {
            byte[] arrby;
            com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "startPay: perform authentication");
            mPayTASelectedCard = mAuthTASelectedCard;
            if (mPayTASelectedCard == null) {
                com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "startPay:selectCard is not called ");
                this.mPayCallback.a(null, -36, this.mAuthType);
                return;
            }
            if (mAuthTAController == null) {
                com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "startPay:auth TA controller instance is null ");
                this.mPayCallback.a(null, -36, this.mAuthType);
                return;
            }
            SelectCardResult selectCardResult = this.selectCardForPayment();
            if (selectCardResult == null || selectCardResult.getNonce() == null || selectCardResult.getTaid() == null) {
                com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "startPay:selectCard result from pay provider is not valid ");
                this.mPayCallback.a(null, -36, this.mAuthType);
                return;
            }
            AuthResult authResult = mAuthTAController.getAuthResult(selectCardResult.getNonce(), selectCardResult.getTaid(), securedObject.getSecureObjectData());
            if (authResult != null) {
                arrby = authResult.getSecObjData();
                this.mAuthType = authResult.getAuthType();
                com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "startPay:auth type =  " + this.mAuthType);
            } else {
                arrby = null;
            }
            if (arrby == null) {
                com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "startPay:authentication failed from auth TA ");
                this.mPayCallback.a(null, -35, this.mAuthType);
                return;
            }
            SecuredObject securedObject2 = new SecuredObject();
            securedObject2.setSecureObjectData(arrby);
            if (!this.authenticateTransaction(securedObject2)) {
                com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "startPay:authentication failed ");
                this.mPayCallback.a(null, -35, this.mAuthType);
                return;
            }
        }
        if (!o.q(16)) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "startPay: cannot go to pay ilde");
            this.mPayCallback.a(null, -4, this.mAuthType);
            return;
        }
        int n3 = payConfig != null && payConfig.getPayType() == n2 ? n2 : 0;
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "isNFCPaymentOnly= " + (boolean)n3);
        this.setPayAuthenticationMode(this.mAuthType);
        if (this.getAuthType().equalsIgnoreCase("None")) {
            n2 = 0;
        }
        this.beginPay((boolean)n2, h.ap(this.mContext));
        if (n3 == 0 && h.ao(this.mContext) && this.isPayAllowedForPresentationMode(2)) {
            this.mMstSequenceId = com.samsung.android.spayfw.core.h.f("default", this.mCardBrand);
            mMstPayThread = new Thread((Runnable)new a());
            mMstPayThread.start();
            com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "started MST pay thread");
            return;
        }
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "Utils.isMstAvailable(mContext) " + h.ao(this.mContext));
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "isPayAllowedForPresentationMode(PaymentFramework.CARD_PRESENT_MODE_MST) " + this.isPayAllowedForPresentationMode(2));
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "MST not supported.");
        new Thread(){

            public void run() {
                com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "Wait for NFC.");
                mNfcWait = new ConditionVariable();
                if (!mNfcWait.block(30000L) && PaymentNetworkProvider.this.mPayCallback != null) {
                    PaymentNetworkProvider.this.mPayCallback.a(null, -46, -46, PaymentNetworkProvider.this.mAuthType, null);
                }
                mNfcWait = null;
            }
        }.start();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public final void stopInAppPay(String var1_1, ICommonCallback var2_2) {
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "stopInAppPay");
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "State : " + o.getState());
        if (var2_2 == null) ** GOTO lbl7
        try {
            this.mStopPayCallback = var2_2;
            this.mStopPaySelectedCard = var1_1;
lbl7: // 2 sources:
            if (o.q(4096) != false) return;
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "state change prohibited");
            return;
        }
        catch (Exception var3_3) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "Exception in stop in-app pay");
            return;
        }
    }

    protected abstract void stopMstPay(boolean var1);

    protected abstract Bundle stopNfcPay(int var1);

    public final void stopPay() {
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "stopPay");
        this.forceQuit = false;
        SPayHCEReceiver.aS();
        if (o.p(72)) {
            if (!o.q(24320)) {
                com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "state cannot be changed from MST");
            }
            com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "stopped MST pay thread");
            if (this.mPayCallback != null) {
                this.mPayCallback.a(null, -7, this.mAuthType);
                this.mPayCallback = null;
                return;
            }
            com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "stopPay callback is null");
            return;
        }
        if (o.p(32)) {
            if (!o.q(24320)) {
                com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "state cannot be changed from NFC");
            }
            com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "stopped NFC pay ");
            return;
        }
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "State : " + o.getState());
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "Retry Mode : " + mRetryMode);
        if (mRetryMode) {
            com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "stopMstPay in Retry Mode");
            this.stopMstPayLocked(true);
        }
        if (!o.q(24320)) {
            com.samsung.android.spayfw.b.c.e("PaymentNetworkProvider", "state cannot be changed from IDLE");
        }
        if (this.mPayCallback != null) {
            this.mPayCallback.a(null, -7, this.mAuthType);
            this.mPayCallback = null;
            return;
        }
        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "stopPay callback is null");
    }

    protected abstract void unloadTA();

    public void updateJwtToken() {
        com.samsung.android.spayfw.b.c.i("PaymentNetworkProvider", "updateJwtToken: start");
        Intent intent = new Intent("com.samsung.android.spayfw.action.notification");
        intent.putExtra("notiType", "updateJwtToken");
        PaymentFrameworkApp.a(intent);
    }

    protected String updateLoyaltyCard(JsonObject jsonObject) {
        return null;
    }

    public final String updateLoyaltyCardTA(JsonObject jsonObject) {
        try {
            this.loadTAwithCounter(false);
            String string = this.updateLoyaltyCard(jsonObject);
            return string;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    public void updateRequestStatus(d d2) {
    }

    protected void updateTokenMetaData(JsonObject jsonObject, Token token) {
    }

    public void updateTokenMetaDataTA(JsonObject jsonObject, Token token) {
        try {
            this.loadTAwithCounter(false);
            this.updateTokenMetaData(jsonObject, token);
            return;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    protected abstract e updateTokenStatus(JsonObject var1, TokenStatus var2);

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public final e updateTokenStatusTA(JsonObject var1_1, TokenStatus var2_2) {
        if (var2_2 == null) ** GOTO lbl11
        try {
            if (var2_2.getCode() != null) {
                com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "Token Status = " + var2_2.getCode());
                if (var2_2.getCode().equals((Object)"DISPOSED") && this.mProviderTokenKey != null && PaymentNetworkProvider.mAuthTASelectedCard != null) {
                    com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "providerTokenKey = " + this.mProviderTokenKey.cn());
                    com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "mAuthTASelectedCard = " + PaymentNetworkProvider.mAuthTASelectedCard.cn());
                    if (Objects.equals((Object)this.mProviderTokenKey.cn(), (Object)PaymentNetworkProvider.mAuthTASelectedCard.cn())) {
                        com.samsung.android.spayfw.b.c.d("PaymentNetworkProvider", "Clearing Secure Object Input for Payment");
                        this.clearSecureObjectInputForPayment();
                    }
                }
            }
lbl11: // 8 sources:
            this.loadTAwithCounter(false);
            var4_3 = this.updateTokenStatus(var1_1, var2_2);
            return var4_3;
        }
        finally {
            this.unloadTAwithCounter(false);
        }
    }

    public static class InAppDetailedTransactionInfo
    extends InAppTransactionInfo {
        private String merchantCertificate;
        private byte[] nonce = null;

        public InAppDetailedTransactionInfo(InAppTransactionInfo inAppTransactionInfo, byte[] arrby) {
            this.setAmount(inAppTransactionInfo.getAmount());
            this.setCurrencyCode(inAppTransactionInfo.getCurrencyCode());
            this.setPid(inAppTransactionInfo.getPid());
            this.setContextId(inAppTransactionInfo.getContextId());
            this.merchantCertificate = null;
            this.nonce = arrby;
        }

        public void aq(String string) {
            this.merchantCertificate = string;
        }

        public String cd() {
            return this.merchantCertificate;
        }

        public byte[] getNonce() {
            return this.nonce;
        }
    }

    private class a
    implements Runnable {
        int oP = -36;
        int oQ = 0;

        private a() {
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private boolean ce() {
            Object object;
            Object object2 = object = mSwitchObj;
            synchronized (object2) {
                boolean bl = o.p(32);
                if (bl) {
                    try {
                        mSwitchObj.wait();
                    }
                    catch (InterruptedException interruptedException) {
                        com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "switch obj wait interrupt exception");
                        com.samsung.android.spayfw.b.c.c(PaymentNetworkProvider.LOG_TAG, interruptedException.getMessage(), interruptedException);
                    }
                    if (o.p(8)) {
                        com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "continue to transimit MST");
                        return true;
                    }
                }
                return false;
            }
        }

        private boolean cf() {
            if (SPayHCEReceiver.aR()) {
                SPayHCEReceiver.aS();
                com.samsung.android.spayfw.b.c.w(PaymentNetworkProvider.LOG_TAG, "RF is detected");
            }
            return false;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        public void run() {
            MstPayConfig mstPayConfig;
            block46 : {
                MstPayConfig mstPayConfig2;
                block45 : {
                    block44 : {
                        if (this.cf()) {
                            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "MST thread exits, not in payment state");
                            return;
                        }
                        if (!PaymentNetworkProvider.this.prepareMstPayInternal()) {
                            com.samsung.android.spayfw.b.c.e(PaymentNetworkProvider.LOG_TAG, "startPay:prepareMstPay() failed ");
                            if (PaymentNetworkProvider.this.mPayCallback != null) {
                                PaymentNetworkProvider.this.mPayCallback.a(null, this.oP, PaymentNetworkProvider.this.mAuthType);
                                return;
                            }
                            com.samsung.android.spayfw.b.c.e(PaymentNetworkProvider.LOG_TAG, "pay callback is null");
                            return;
                        }
                        int n2 = 1500;
                        try {
                            if (mPayConfig != null) {
                                n2 = mPayConfig.getPayIdleTime();
                            } else {
                                com.samsung.android.spayfw.b.c.e(PaymentNetworkProvider.LOG_TAG, "pay config is null");
                            }
                            mShouldInterrupt = true;
                            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "mst idle time=" + n2);
                            Thread.sleep((long)n2);
                            mShouldInterrupt = false;
                        }
                        catch (InterruptedException interruptedException) {
                            mShouldInterrupt = false;
                            com.samsung.android.spayfw.b.c.i(PaymentNetworkProvider.LOG_TAG, "interrupt MST because nfc is detected");
                        }
                        if (!o.q(64) && !o.p(72)) {
                            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "cannot start mst");
                            if (!this.ce()) {
                                com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "MST thread exits");
                                return;
                            }
                        }
                        if (mPayConfig == null) return;
                        if (mPayConfig.getPayType() != 2) return;
                        if (mPayConfig.getMstPayConfig() == null) return;
                        mstPayConfig2 = mPayConfig.getMstPayConfig();
                        if (mAuthTASelectedCard != null && "GIFT".equals((Object)mAuthTASelectedCard.cn())) break block44;
                        if (mRetryMode) break block45;
                        PayConfig payConfig = com.samsung.android.spayfw.core.f.j(PaymentNetworkProvider.this.mContext).ak();
                        if (payConfig != null && payConfig.getMstPayConfig() != null) {
                            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "MstConfigurationManager provides new pay configuration in Main Mode");
                            mstPayConfig2 = payConfig.getMstPayConfig();
                            PaymentNetworkProvider.this.mMstSequenceId = com.samsung.android.spayfw.core.f.j(PaymentNetworkProvider.this.mContext).am();
                            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "Mst Sequence Id from RSC = " + PaymentNetworkProvider.this.mMstSequenceId);
                        } else {
                            PayConfig payConfig2 = PaymentNetworkProvider.this.getPayConfig();
                            if (payConfig2 != null && payConfig2.getMstPayConfig() != null && payConfig2.getMstPayConfig().getMstPayConfigEntry() != null && payConfig2.getMstPayConfig().getMstPayConfigEntry().size() > 0) {
                                com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "SDK provides new pay configuration in Main Mode");
                                mstPayConfig2 = payConfig2.getMstPayConfig();
                            }
                        }
                        mstPayConfig = mstPayConfig2;
                        break block46;
                    }
                    com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "Gift Card - Use Config from App");
                    mstPayConfig = mstPayConfig2;
                    break block46;
                }
                PayConfig payConfig = com.samsung.android.spayfw.core.f.j(PaymentNetworkProvider.this.mContext).al();
                if (payConfig != null && payConfig.getMstPayConfig() != null) {
                    com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "MstConfigurationManager provides new pay configuration in Retry Mode");
                    MstPayConfig mstPayConfig3 = payConfig.getMstPayConfig();
                    PaymentNetworkProvider.this.mMstSequenceId = com.samsung.android.spayfw.core.f.j(PaymentNetworkProvider.this.mContext).an();
                    com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "Mst Sequence Id from RSC= " + PaymentNetworkProvider.this.mMstSequenceId);
                    mstPayConfig = mstPayConfig3;
                } else {
                    PayConfig payConfig3 = PaymentNetworkProvider.this.getPayConfig();
                    if (payConfig3 != null && payConfig3.getMstPayConfig() != null && payConfig3.getMstPayConfig().getMstPayConfigEntry() != null && payConfig3.getMstPayConfig().getMstPayConfigEntry().size() > 0) {
                        com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "SDK provides new pay configuration in Retry Mode");
                        mstPayConfig = payConfig3.getMstPayConfig();
                    } else {
                        mstPayConfig = mstPayConfig2;
                    }
                }
            }
            if (mstPayConfig.getMstPayConfigEntry() == null || mstPayConfig.getMstPayConfigEntry().size() <= 0) {
                com.samsung.android.spayfw.b.c.e(PaymentNetworkProvider.LOG_TAG, "startPay:configuration is missing ");
                if (PaymentNetworkProvider.this.mPayCallback != null) {
                    PaymentNetworkProvider.this.mPayCallback.a(null, -36, PaymentNetworkProvider.this.mAuthType);
                    return;
                }
                com.samsung.android.spayfw.b.c.e(PaymentNetworkProvider.LOG_TAG, "pay callback is null");
                return;
            }
            if (!o.p(72)) {
                com.samsung.android.spayfw.b.c.i(PaymentNetworkProvider.LOG_TAG, " Mst stopped");
                if (!this.ce()) {
                    com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "MST thread exits before preparing MST");
                    return;
                }
            }
            this.oQ = mstPayConfig.getMstPayConfigEntry().size();
            if (this.cf()) {
                com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "MST thread exits, rf is detected");
                return;
            }
            if (!PaymentNetworkProvider.this.mTAController.makeSystemCall(1)) {
                com.samsung.android.spayfw.b.c.e(PaymentNetworkProvider.LOG_TAG, "Error: Failed to turn MST Driver on");
                if (PaymentNetworkProvider.this.mPayCallback != null) {
                    PaymentNetworkProvider.this.mPayCallback.a(null, this.oP, PaymentNetworkProvider.this.mAuthType);
                    return;
                }
                com.samsung.android.spayfw.b.c.e(PaymentNetworkProvider.LOG_TAG, "pay callback is null");
                return;
            }
            PaymentNetworkProvider.this.mTAController.moveSecOsToCore4();
            boolean bl = false;
            int n3 = 0;
            boolean bl2 = true;
            for (int i2 = 1; i2 <= this.oQ; ++i2, ++n3) {
                boolean bl3;
                block43 : {
                    boolean bl4;
                    block51 : {
                        block52 : {
                            block53 : {
                                boolean bl5;
                                block50 : {
                                    block49 : {
                                        byte[] arrby;
                                        MstPayConfigEntry mstPayConfigEntry;
                                        block48 : {
                                            block47 : {
                                                com.samsung.android.spayfw.b.c.i(PaymentNetworkProvider.LOG_TAG, "MstPayloop: start: " + System.currentTimeMillis());
                                                if (o.p(72)) break block47;
                                                com.samsung.android.spayfw.b.c.i(PaymentNetworkProvider.LOG_TAG, " MstPayloop: end: at start of loop: " + System.currentTimeMillis());
                                                if (!this.ce()) {
                                                    com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "MST thread exits after preparing MST");
                                                    if (PaymentNetworkProvider.this.forceQuit) return;
                                                    PaymentNetworkProvider.this.stopMstPayLocked(true);
                                                    return;
                                                }
                                                com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "restart MST");
                                                i2 = 0;
                                                n3 = -1;
                                                bl3 = bl2;
                                                break block43;
                                            }
                                            com.samsung.android.spayfw.b.c.i(PaymentNetworkProvider.LOG_TAG, "startPayMst: count: " + i2);
                                            mstPayConfigEntry = (MstPayConfigEntry)mstPayConfig.getMstPayConfigEntry().get(n3);
                                            com.samsung.android.spayfw.b.c.i(PaymentNetworkProvider.LOG_TAG, "startPayMst: config: " + mstPayConfigEntry.toString());
                                            arrby = com.samsung.android.spayfw.core.g.a(mstPayConfigEntry);
                                            if (o.p(72)) break block48;
                                            com.samsung.android.spayfw.b.c.i(PaymentNetworkProvider.LOG_TAG, " MstPayloop end: before transmitMst: " + System.currentTimeMillis());
                                            if (!this.ce()) {
                                                com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "MST thread exits before transimiting MST");
                                                if (PaymentNetworkProvider.this.forceQuit) return;
                                                PaymentNetworkProvider.this.stopMstPayLocked(true);
                                                return;
                                            }
                                            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "restart MST before transimiting MST");
                                            n3 = -1;
                                            bl3 = bl2;
                                            i2 = 0;
                                            break block43;
                                        }
                                        com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "transmitMstPay: start: " + System.currentTimeMillis());
                                        bl5 = PaymentNetworkProvider.this.startMstPay(mstPayConfigEntry.getBaudRate(), arrby);
                                        com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "transmitMstPay: end: " + System.currentTimeMillis());
                                        if (o.p(72)) break block49;
                                        com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, " MstPayloop: end: after transmitMst: " + System.currentTimeMillis());
                                        if (!this.ce()) {
                                            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "MST thread exits after transimiting MST");
                                            if (PaymentNetworkProvider.this.forceQuit) return;
                                            PaymentNetworkProvider.this.stopMstPayLocked(true);
                                            return;
                                        }
                                        n3 = -1;
                                        bl3 = bl2;
                                        i2 = 0;
                                        break block43;
                                    }
                                    if (!bl5) break block50;
                                    com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "startMstPay: success: count: " + i2);
                                    if (PaymentNetworkProvider.this.mPayCallback != null) {
                                        com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "onPay: start: " + System.currentTimeMillis());
                                        if (i2 == this.oQ && !mRetryMode && PaymentNetworkProvider.this.allowPaymentRetry()) {
                                            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "Send Retry Callback");
                                            PaymentNetworkProvider.this.mPayCallback.g(null);
                                            bl4 = true;
                                        } else {
                                            if (i2 == this.oQ) {
                                                com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "Send Finish Callback");
                                                bl4 = false;
                                            } else {
                                                bl4 = bl;
                                            }
                                            PaymentNetworkProvider.this.mPayCallback.a(null, i2, this.oQ, PaymentNetworkProvider.this.mAuthType, PaymentNetworkProvider.this.mMstSequenceId);
                                        }
                                        com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "onPay: end: " + System.currentTimeMillis());
                                    } else {
                                        com.samsung.android.spayfw.b.c.e(PaymentNetworkProvider.LOG_TAG, "pay callback is null");
                                        bl4 = bl;
                                    }
                                    if (i2 >= this.oQ) break block51;
                                    if (o.p(72)) break block52;
                                    if (!this.ce()) {
                                        com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "MST thread exits when sleeping");
                                        if (PaymentNetworkProvider.this.forceQuit) return;
                                        PaymentNetworkProvider.this.stopMstPayLocked(true);
                                        return;
                                    }
                                    break block53;
                                }
                                com.samsung.android.spayfw.b.c.e(PaymentNetworkProvider.LOG_TAG, "startPayMst: transmission error ");
                                com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "stopMstPay start:  currentTime " + System.currentTimeMillis());
                                if (!PaymentNetworkProvider.this.forceQuit) {
                                    PaymentNetworkProvider.this.stopMstPayLocked(bl5);
                                }
                                com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "stopMstPay end:  currentTime " + System.currentTimeMillis());
                                if (PaymentNetworkProvider.this.mPayCallback != null) {
                                    PaymentNetworkProvider.this.mPayCallback.a(null, -37, PaymentNetworkProvider.this.mAuthType);
                                    return;
                                }
                                com.samsung.android.spayfw.b.c.e(PaymentNetworkProvider.LOG_TAG, "pay callback is null");
                                return;
                            }
                            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "restart MST when sleeping");
                            n3 = -1;
                            bl = bl4;
                            bl3 = bl2;
                            i2 = 0;
                            break block43;
                        }
                        try {
                            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "next transmission will happen after :" + ((MstPayConfigEntry)mstPayConfig.getMstPayConfigEntry().get(n3)).getDelayBetweenRepeat() + "Ms");
                            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "current time before sleep in ms: " + System.currentTimeMillis());
                            Thread.sleep((long)((MstPayConfigEntry)mstPayConfig.getMstPayConfigEntry().get(n3)).getDelayBetweenRepeat());
                            if (bl2 && o.p(8)) {
                                n3 = -1;
                                bl = bl4;
                                i2 = 0;
                                bl3 = false;
                                break block43;
                            }
                            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "current time after wake up in ms: " + System.currentTimeMillis() + "\n next transmission started after delay:");
                        }
                        catch (InterruptedException interruptedException) {
                            com.samsung.android.spayfw.b.c.w(PaymentNetworkProvider.LOG_TAG, "premature wake up from sleep: currentTime " + System.currentTimeMillis());
                        }
                    }
                    com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "MstPayloop: end: " + System.currentTimeMillis());
                    bl = bl4;
                    bl3 = bl2;
                }
                bl2 = bl3;
            }
            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "stopMstPay start:  currentTime " + System.currentTimeMillis());
            if (bl) {
                com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "Enable Retry Mode");
                mRetryMode = true;
                com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "Set Pay Type to Default - MST");
            } else {
                com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "Disable Retry Mode");
                mRetryMode = false;
                if (!PaymentNetworkProvider.this.forceQuit) {
                    PaymentNetworkProvider.this.stopMstPayLocked(true);
                }
            }
            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "stopMstPay end:  currentTime " + System.currentTimeMillis());
        }
    }

    private class b
    extends TimerTask {
        private b() {
        }

        public void run() {
            com.samsung.android.spayfw.b.c.d(PaymentNetworkProvider.LOG_TAG, "TimerExpired::run: unloading TA");
            PaymentNetworkProvider.this.unloadTA();
            PaymentNetworkProvider.this.mUnloadTimer.cancel();
        }
    }

}

