/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.util.Base64
 *  com.google.gson.JsonObject
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.discover;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.k;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.d;
import com.samsung.android.spayfw.payprovider.discover.accttxns.b;
import com.samsung.android.spayfw.payprovider.discover.db.DcDbException;
import com.samsung.android.spayfw.payprovider.discover.db.dao.c;
import com.samsung.android.spayfw.payprovider.discover.db.models.DcCardMaster;
import com.samsung.android.spayfw.payprovider.discover.payment.data.DiscoverCDCVM;
import com.samsung.android.spayfw.payprovider.discover.payment.g;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.h;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;

import java.util.ArrayList;
import java.util.List;

public class a
extends PaymentNetworkProvider {
    private k ps;
    private long se = 120000L;
    private com.samsung.android.spayfw.payprovider.discover.tokenmanager.b sf = null;
    private g sg = null;
    private b sh = null;
    private com.samsung.android.spayfw.payprovider.discover.tzsvc.b sj = null;
    private f sk = null;
    private CertificateInfo[] sl = null;
    private boolean sm = false;

    /*
     * Enabled aggressive block sorting
     */
    public a(Context context, String string, f f2) {
        super(context, string);
        this.sf = new com.samsung.android.spayfw.payprovider.discover.tokenmanager.b(context);
        this.sg = new g(context);
        this.sh = new b(context);
        this.sj = com.samsung.android.spayfw.payprovider.discover.tzsvc.b.E(context);
        this.mTAController = this.sj;
        this.sk = this.mProviderTokenKey;
        String string2 = f2 == null ? "true" : "false";
        Log.d("DCSDK_DiscoverPayProvider", "Key is null? " + string2);
        String string3 = this.mProviderTokenKey == null ? "true" : "false";
        Log.d("DCSDK_DiscoverPayProvider", "Key is null? " + string3);
    }

    private boolean aE(String string) {
        Log.i("DCSDK_DiscoverPayProvider", "startReplenishAlarm: " + this.sm);
        if (!this.sm) {
            f f2 = new f(string);
            f2.setTrTokenId(string);
            h.a(this.mContext, this.se + com.samsung.android.spayfw.utils.h.am(this.mContext), f2);
            this.ps.a(this.mProviderTokenKey);
            this.sm = true;
            Log.d("DCSDK_DiscoverPayProvider", "startReplenishAlarm: started alarm - " + this.se);
        }
        return this.sm;
    }

    private byte[] aF(String string) {
        Log.d("DCSDK_DiscoverPayProvider", "getDiscoverCert with usage type : " + string);
        for (CertificateInfo certificateInfo : this.sl) {
            if (certificateInfo.getContent() == null || !string.equals((Object)certificateInfo.getUsage())) continue;
            return Base64.decode((byte[])certificateInfo.getContent().replace((CharSequence)"-----BEGIN CERTIFICATE-----", (CharSequence)"").replace((CharSequence)"-----END CERTIFICATE-----", (CharSequence)"").getBytes(), (int)0);
        }
        Log.e("DCSDK_DiscoverPayProvider", "getDiscoverCert: No Cert Found for usage type " + string);
        return null;
    }

    public static Context cC() {
        Class<a> class_ = a.class;
        synchronized (a.class) {
            PaymentFrameworkApp paymentFrameworkApp = PaymentFrameworkApp.aB();
            // ** MonitorExit[var2] (shouldn't be in output)
            return paymentFrameworkApp;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private boolean cD() {
        block6 : {
            block5 : {
                Log.i("DCSDK_DiscoverPayProvider", "checkAndTriggerReplenishment...");
                try {
                    var3_1 = (DcCardMaster)new c(this.mContext).getData(this.mProviderTokenKey.cm());
                }
                catch (DcDbException var2_6) {
                    var2_6.printStackTrace();
                    var3_1 = null;
                }
lbl8: // 2 sources:
                if (var3_1 == null) break block5;
                var4_2 = var3_1.getRemainingOtpkCount();
                var7_3 = var3_1.getReplenishmentThreshold();
                Log.d("DCSDK_DiscoverPayProvider", "checkAndTriggerReplenishment: otpkBal - " + var4_2 + ", Threshold - " + var7_3);
                if (var4_2 <= var7_3) {
                    var9_4 = var3_1.getTokenId();
                    Log.d("DCSDK_DiscoverPayProvider", "Requesting Replenishment: " + var9_4);
                    this.aE(var9_4);
                } else {
                    Log.d("DCSDK_DiscoverPayProvider", "Requesting Replenishment: No need for replenishment");
                    this.cE();
                }
                break block6;
                catch (NullPointerException var1_7) {
                    var1_7.printStackTrace();
                    Log.e("DCSDK_DiscoverPayProvider", "Nullpointer Exception in checkAndTriggerReplenishment");
                    return false;
                }
                ** GOTO lbl8
            }
            Log.d("DCSDK_DiscoverPayProvider", "Requesting Replenishment: cardMaster is null");
            this.cE();
            var4_2 = 0L;
        }
        if (var4_2 <= 0L) return false;
        return true;
    }

    private void cE() {
        Log.i("DCSDK_DiscoverPayProvider", "cancelReplenishmentAlarm: " + this.sm);
        if (this.sm) {
            h.a(this.mContext, this.mProviderTokenKey);
            this.sm = false;
        }
    }

    private void cF() {
        this.se = 120000L + this.se;
        if (this.se > 1200000L) {
            this.se = 1200000L;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public List<byte[]> J(int n2) {
        if (n2 != 1 && n2 != 2) {
            Log.e("DCSDK_DiscoverPayProvider", "getDiscoverCertChain: Invalid usage");
            return null;
        }
        byte[] arrby = this.aF("ENC");
        byte[] arrby2 = this.aF("CA");
        byte[] arrby3 = this.aF("SIG");
        if (arrby == null || arrby2 == null || arrby3 == null) {
            Log.e("DCSDK_DiscoverPayProvider", "getDiscoverCertChain: One of the Server cert is null");
            return null;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(0, (Object)arrby2);
        if (1 == n2) {
            arrayList.add(1, (Object)arrby);
            do {
                return arrayList;
                break;
            } while (true);
        }
        if (2 == n2) {
            arrayList.add(1, (Object)arrby3);
            return arrayList;
        }
        Log.e("DCSDK_DiscoverPayProvider", "getDiscoverCertChain: Invalid usage");
        return null;
    }

    @Override
    protected boolean authenticateTransaction(SecuredObject securedObject) {
        Log.i("DCSDK_DiscoverPayProvider", "authenticateTransaction: start , timestamp " + System.currentTimeMillis());
        if (securedObject == null) {
            Log.e("DCSDK_DiscoverPayProvider", "authenticateTransaction: can't authenticate transaction, secure object is null, returned false.");
            return false;
        }
        boolean bl = this.sg.a(securedObject, DiscoverCDCVM.aG(this.getAuthType()));
        if (!bl) {
            Log.e("DCSDK_DiscoverPayProvider", "authenticateTransaction: authenticateTransaction failed.");
        }
        Log.i("DCSDK_DiscoverPayProvider", "authenticateTransaction:  end, result " + bl + ", timestamp " + System.currentTimeMillis());
        return bl;
    }

    @Override
    public void beginPay(boolean bl, boolean bl2) {
        Log.i("DCSDK_DiscoverPayProvider", "prepareNfcPay: start, timestamp " + System.currentTimeMillis());
        boolean bl3 = this.sg.prepareNfcPay();
        if (!bl3) {
            Log.e("DCSDK_DiscoverPayProvider", "prepareNfcPay: init nfc transaction failed.");
        }
        Log.i("DCSDK_DiscoverPayProvider", "prepareNfcPay:  end, result " + bl3 + ", timestamp " + System.currentTimeMillis());
    }

    @Override
    public void checkIfReplenishmentNeeded(TransactionData transactionData) {
        Log.i("DCSDK_DiscoverPayProvider", "checkIfReplenishmentNeeded");
        this.sk = this.mProviderTokenKey;
        this.cD();
    }

    @Override
    protected void clearCard() {
        Log.i("DCSDK_DiscoverPayProvider", "clearCard: start, timestamp " + System.currentTimeMillis());
        this.sg.clearCard();
        this.cD();
        Log.i("DCSDK_DiscoverPayProvider", "clearCard:  end, timestamp " + System.currentTimeMillis());
    }

    @Override
    protected e createToken(String string, com.samsung.android.spayfw.payprovider.c c2, int n2) {
        Log.i("DCSDK_DiscoverPayProvider", "createToken");
        return this.sf.a(string, c2.ch(), n2, this.J(2));
    }

    @Override
    public byte[] decryptUserSignature(String string) {
        if (string == null) {
            Log.e("DCSDK_DiscoverPayProvider", "decryptUserSignature : input data null");
            return null;
        }
        return this.sf.aO(string);
    }

    @Override
    public void delete() {
        Log.i("DCSDK_DiscoverPayProvider", "delete");
        this.sf.d(this.mProviderTokenKey);
    }

    @Override
    public String encryptUserSignature(byte[] arrby) {
        if (arrby == null) {
            Log.e("DCSDK_DiscoverPayProvider", "encryptUserSignature : input data null");
            return null;
        }
        return this.sf.j(arrby);
    }

    @Override
    protected byte[] generateInAppPaymentPayload(PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        Log.i("DCSDK_DiscoverPayProvider", "generateInAppPaymentPayload: start, timestamp " + System.currentTimeMillis());
        byte[] arrby = this.sg.generateInAppPaymentPayload(inAppDetailedTransactionInfo);
        Log.i("DCSDK_DiscoverPayProvider", "generateInAppPaymentPayload: end, timestamp " + System.currentTimeMillis());
        return arrby;
    }

    @Override
    protected CertificateInfo[] getDeviceCertificates() {
        Log.i("DCSDK_DiscoverPayProvider", "getDeviceCertificates");
        return null;
    }

    @Override
    protected com.samsung.android.spayfw.payprovider.c getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        Log.i("DCSDK_DiscoverPayProvider", "getEnrollmentRequestData");
        if (enrollCardInfo instanceof EnrollCardPanInfo) {
            Log.i("DCSDK_DiscoverPayProvider", "Regular Enrollment");
            return this.sf.a((EnrollCardPanInfo)enrollCardInfo, billingInfo, this.J(1));
        }
        if (enrollCardInfo instanceof EnrollCardReferenceInfo) {
            Log.i("DCSDK_DiscoverPayProvider", "Push Enrollemnt");
            com.samsung.android.spayfw.payprovider.c c2 = this.sf.b((EnrollCardReferenceInfo)enrollCardInfo);
            Log.i("DCSDK_DiscoverPayProvider", "Provider Req Data: " + c2.ch().toString());
            return c2;
        }
        Log.e("DCSDK_DiscoverPayProvider", "EnrollCardInfo is of Invalid type" + enrollCardInfo.getEnrollType());
        com.samsung.android.spayfw.payprovider.c c3 = new com.samsung.android.spayfw.payprovider.c();
        c3.a(null);
        c3.setErrorCode(-4);
        return c3;
    }

    @Override
    public boolean getPayReadyState() {
        this.sk = this.mProviderTokenKey;
        boolean bl = this.sg.s(this.sk.cm());
        Log.i("DCSDK_DiscoverPayProvider", "getPayReadyState: " + bl);
        this.cD();
        return bl;
    }

    @Override
    protected com.samsung.android.spayfw.payprovider.c getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        Log.i("DCSDK_DiscoverPayProvider", "getProvisionRequestData");
        return this.sf.c(provisionTokenInfo);
    }

    @Override
    protected com.samsung.android.spayfw.payprovider.c getReplenishmentRequestData() {
        Log.i("DCSDK_DiscoverPayProvider", "getReplenishmentRequestData");
        return this.sf.a(this.mProviderTokenKey, this.J(1));
    }

    @Override
    protected int getTransactionData(Bundle bundle, i i2) {
        Log.i("DCSDK_DiscoverPayProvider", "getTransactionData");
        return this.sh.a(this.mProviderTokenKey.cm(), bundle, i2);
    }

    @Override
    protected byte[] handleApdu(byte[] arrby, Bundle bundle) {
        Log.i("DCSDK_DiscoverPayProvider", "handleApdu: start, timestamp " + System.currentTimeMillis());
        if (arrby == null) {
            Log.e("DCSDK_DiscoverPayProvider", "handleApdu: can't process apdu, apdu buffer is null, return null.");
            return null;
        }
        if (arrby.length == 0) {
            Log.e("DCSDK_DiscoverPayProvider", "handleApdu: can't process apdu, apdu buffer is empty, return null.");
            return null;
        }
        byte[] arrby2 = this.sg.h(arrby);
        Log.i("DCSDK_DiscoverPayProvider", "handleApdu: end, timestamp " + System.currentTimeMillis());
        return arrby2;
    }

    @Override
    protected void init() {
        Log.i("DCSDK_DiscoverPayProvider", "init");
    }

    @Override
    protected void interruptMstPay() {
        Log.i("DCSDK_DiscoverPayProvider", "interruptMstPay: start, timestamp " + System.currentTimeMillis());
        this.sg.interruptMstPay();
        Log.i("DCSDK_DiscoverPayProvider", "interruptMstPay: end, timestamp " + System.currentTimeMillis());
    }

    @Override
    public boolean isReplenishDataAvailable(JsonObject jsonObject) {
        Log.i("DCSDK_DiscoverPayProvider", "isReplenishDataAvailable");
        return this.sf.isReplenishDataAvailable(jsonObject);
    }

    @Override
    public Bundle isTransactionComplete() {
        return this.sg.isTransactionComplete();
    }

    @Override
    protected void loadTA() {
        Log.i("DCSDK_DiscoverPayProvider", "loadTA: start, timestamp " + System.currentTimeMillis());
        boolean bl = this.sj.loadTA();
        Log.i("DCSDK_DiscoverPayProvider", "loadTA: end, result " + bl + ", timestamp " + System.currentTimeMillis());
    }

    @Override
    protected boolean prepareMstPay() {
        Log.i("DCSDK_DiscoverPayProvider", "prepareMstPay: start, timestamp " + System.currentTimeMillis());
        boolean bl = this.sg.prepareMstPay();
        if (!bl) {
            Log.e("DCSDK_DiscoverPayProvider", "prepareMstPay: mst tracks data preparation failed ");
        }
        Log.i("DCSDK_DiscoverPayProvider", "prepareMstPay: end, result " + bl + ", timestamp " + System.currentTimeMillis());
        return bl;
    }

    @Override
    protected boolean prepareNfcPay() {
        return true;
    }

    @Override
    protected TransactionDetails processTransactionData(Object object) {
        Log.i("DCSDK_DiscoverPayProvider", "processTransactionData");
        return this.sh.a(this.mProviderTokenKey.cm(), object, this.J(2));
    }

    @Override
    protected void replenishAlarmExpired() {
        Log.i("DCSDK_DiscoverPayProvider", "replenishAlarmExpired");
        this.sk = this.mProviderTokenKey;
        this.cE();
        this.cF();
        this.cD();
    }

    @Override
    protected e replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        Log.i("DCSDK_DiscoverPayProvider", "replenishToken");
        e e2 = this.sf.a(this.mProviderTokenKey.getTrTokenId(), this.mProviderTokenKey, jsonObject, tokenStatus, this.J(2));
        if (e2.getErrorCode() == 0) {
            this.cE();
        }
        return e2;
    }

    @Override
    protected SelectCardResult selectCard() {
        Log.i("DCSDK_DiscoverPayProvider", "selectCard: start, timestamp " + System.currentTimeMillis());
        if (this.mProviderTokenKey == null) {
            Log.e("DCSDK_DiscoverPayProvider", "selectCard: wrong token - null.");
            return null;
        }
        long l2 = this.mProviderTokenKey.cm();
        Log.d("DCSDK_DiscoverPayProvider", "selectCard:  load card by token key, key = " + l2);
        SelectCardResult selectCardResult = this.sg.q(l2);
        if (selectCardResult == null) {
            Log.e("DCSDK_DiscoverPayProvider", "selectCard: cannot select card for token " + l2);
        }
        Log.i("DCSDK_DiscoverPayProvider", "selectCard: end , timestamp " + System.currentTimeMillis());
        return selectCardResult;
    }

    @Override
    public void setPaymentFrameworkRequester(k k2) {
        this.ps = k2;
    }

    @Override
    public boolean setServerCertificates(CertificateInfo[] arrcertificateInfo) {
        Log.i("DCSDK_DiscoverPayProvider", "setServerCertificates");
        Log.d("DCSDK_DiscoverPayProvider", "setServerCertificates: size: " + arrcertificateInfo.length);
        int n2 = arrcertificateInfo.length;
        for (int i2 = 0; i2 < n2; ++i2) {
            CertificateInfo certificateInfo = arrcertificateInfo[i2];
            Log.d("DCSDK_DiscoverPayProvider", "Usage: " + certificateInfo.getUsage() + ", Alias: " + certificateInfo.getAlias());
        }
        this.sl = arrcertificateInfo;
        return true;
    }

    @Override
    public void setupReplenishAlarm() {
        Log.i("DCSDK_DiscoverPayProvider", "setupReplenishAlarm");
        this.sk = this.mProviderTokenKey;
        this.cD();
    }

    @Override
    public boolean startMstPay(int n2, byte[] arrby) {
        Log.i("DCSDK_DiscoverPayProvider", "startMstPay: start, timestamp " + System.currentTimeMillis());
        boolean bl = this.sg.startMstPay(n2, arrby);
        if (!bl) {
            Log.e("DCSDK_DiscoverPayProvider", "prepareMstPay: mst transmission failed ");
        }
        Log.i("DCSDK_DiscoverPayProvider", "startMstPay: end, timestamp" + System.currentTimeMillis() + ", result " + bl);
        return bl;
    }

    @Override
    protected void stopMstPay(boolean bl) {
        Log.i("DCSDK_DiscoverPayProvider", "stopMstPay: start, timestamp " + System.currentTimeMillis() + "mstTransmissionStatus " + bl);
        this.sg.stopMstPay(bl);
        Log.i("DCSDK_DiscoverPayProvider", "stopMstPay:  end, timestamp " + System.currentTimeMillis());
    }

    @Override
    protected Bundle stopNfcPay(int n2) {
        Log.i("DCSDK_DiscoverPayProvider", "stopNfcPay: start, reason " + n2 + ", timestamp " + System.currentTimeMillis());
        short s2 = this.sg.K(n2);
        Log.i("DCSDK_DiscoverPayProvider", "stopNfcPay: end, result " + s2 + ", timestamp " + System.currentTimeMillis());
        Bundle bundle = new Bundle();
        bundle.putShort("nfcApduErrorCode", s2);
        return bundle;
    }

    @Override
    protected void unloadTA() {
        Log.i("DCSDK_DiscoverPayProvider", "unloadTA: start, timestamp " + System.currentTimeMillis());
        this.sj.unloadTA();
        Log.i("DCSDK_DiscoverPayProvider", "unloadTA: unloadTA end, timestamp " + System.currentTimeMillis());
    }

    @Override
    public void updateRequestStatus(d d2) {
        Log.i("DCSDK_DiscoverPayProvider", "updateRequestStatus: Req Type - " + d2.getRequestType() + ", RequestStatus - " + d2.ci());
        switch (d2.getRequestType()) {
            default: {
                return;
            }
            case 11: 
        }
        this.cE();
        if (d2.ci() != 0) {
            Log.e("DCSDK_DiscoverPayProvider", "Replenishment Request Failed");
            this.cF();
            this.aE(d2.ck().cn());
            return;
        }
        this.se = 120000L;
        Log.i("DCSDK_DiscoverPayProvider", "Replenishment successful. Set mReplenishmentRetryWait to DEFAULT_REPLENISH_RETRY_DELTA");
        this.ps.b(d2.ck());
    }

    @Override
    protected e updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        Log.i("DCSDK_DiscoverPayProvider", "updateTokenStatus");
        return this.sf.a(this.mProviderTokenKey, jsonObject, tokenStatus);
    }
}

