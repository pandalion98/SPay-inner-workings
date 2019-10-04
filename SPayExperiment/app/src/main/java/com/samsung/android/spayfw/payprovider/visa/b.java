/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.text.TextUtils
 *  android.util.Base64
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  java.io.Serializable
 *  java.lang.CharSequence
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Throwable
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.visa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.appinterface.VerifyIdvInfo;
import com.samsung.android.spayfw.core.k;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.PaymentProviderException;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.payprovider.visa.a;
import com.samsung.android.spayfw.payprovider.visa.c;
import com.samsung.android.spayfw.payprovider.visa.d;
import com.samsung.android.spayfw.payprovider.visa.db.VisaTokenDetailsDao;
import com.samsung.android.spayfw.payprovider.visa.inapp.InAppPayment;
import com.samsung.android.spayfw.payprovider.visa.inapp.models.GenCryptogramResponseData;
import com.samsung.android.spayfw.payprovider.visa.inapp.models.InAppData;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.h;
import com.samsung.android.spaytzsvc.api.TAController;
import com.samsung.android.visasdk.facade.data.PaymentDataRequest;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.exception.TokenInvalidException;
import com.visa.tainterface.VisaTAController;
import com.visa.tainterface.VisaTAException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class b
extends PaymentNetworkProvider {
    private static f pj;
    private static k ps;
    private static VisaTokenDetailsDao zG;
    private static VisaTAController zH;
    private static boolean zK;
    private d zE;
    private c zF;
    private boolean zI = false;
    private boolean zJ = false;
    private EnrollCardInfo zL = null;
    private long zM = -1L;

    static {
        zG = null;
        zK = false;
        pj = null;
    }

    public b(Context context, String string, f f2) {
        super(context, string);
        this.mContext = context;
        this.mTAController = VisaTAController.bv(this.mContext);
        zH = (VisaTAController)this.mTAController;
        if (zG == null) {
            zG = new VisaTokenDetailsDao(this.mContext);
        }
    }

    private TokenKey e(f f2) {
        TokenKey tokenKey = null;
        if (f2 != null) {
            tokenKey = new TokenKey(f2.cm());
        }
        return tokenKey;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void eD() {
        if (pj == null) {
            com.samsung.android.spayfw.b.c.i("VisaPayProvider", "no provider Token key. ignore updateAndCheckReplenishStatus ");
            return;
        } else {
            String string = pj.cn();
            com.samsung.android.spayfw.b.c.d("VisaPayProvider", "updateAndCheckReplenishStatus: provideKey: " + pj.cm());
            if (string == null) return;
            {
                this.zF.a(string, pj, ps);
                return;
            }
        }
    }

    @Override
    public boolean authenticateTransaction(SecuredObject securedObject) {
        boolean bl = this.zE.authenticateTransaction(securedObject);
        if (!bl) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", "authenticateTransaction:failed");
        }
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void checkIfReplenishmentNeeded(TransactionData transactionData) {
        if (this.mProviderTokenKey == null || this.mProviderTokenKey.cn() == null) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", "checkIfReplenishmentNeeded :  token key is null");
            return;
        } else {
            String string = this.mProviderTokenKey.getTrTokenId();
            com.samsung.android.spayfw.b.c.d("VisaPayProvider", "checkIfReplenishmentNeeded: id " + string);
            if (string == null) return;
            {
                com.samsung.android.spayfw.payprovider.visa.db.a a2 = zG.aZ(string);
                String string2 = this.zF.f(this.mProviderTokenKey);
                if (string2 == null) {
                    com.samsung.android.spayfw.b.c.e("VisaPayProvider", "checkIfReplenishmentNeeded :  tokenStatus is null");
                    return;
                }
                com.samsung.android.spayfw.b.c.d("VisaPayProvider", "checkIfReplenishmentNeeded :  tokenStatus " + string2);
                if (!string2.equals((Object)"ACTIVE")) {
                    com.samsung.android.spayfw.b.c.e("VisaPayProvider", "Not Replenishing as Token is Suspended or Pending: " + string2);
                    return;
                }
                if (a2 == null) return;
                {
                    com.samsung.android.spayfw.b.c.d("VisaPayProvider", "checkIfReplenishmentNeeded: repplenish ts = " + a2.eK() + " current ts = " + System.currentTimeMillis() + " maxPmts = " + a2.getMaxPmts() + " replPmts = " + a2.eI());
                    if (a2.eK() - System.currentTimeMillis() > 0L && a2.getMaxPmts() > a2.eI()) return;
                    {
                        ps.a(this.mProviderTokenKey);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void clearCard() {
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    @Override
    public e createToken(String var1_1, com.samsung.android.spayfw.payprovider.c var2_2, int var3_3) {
        try {
            try {
                var7_5 = var8_4 = this.zF.a(var1_1, var2_2.ch());
                if (var7_5 == null) ** GOTO lbl20
            }
            catch (TokenInvalidException var5_6) {
                var6_7 = var5_6;
                var7_5 = null;
lbl8: // 2 sources:
                do {
                    com.samsung.android.spayfw.b.c.c("VisaPayProvider", var6_7.getMessage(), (Throwable)var6_7);
                    if (this.zL == null) ** continue;
                    this.zL.decrementRefCount();
                    this.zL = null;
                    return var7_5;
                    break;
                } while (true);
            }
            if (var7_5.getErrorCode() == 0) {
                this.mProviderTokenKey = var7_5.getProviderTokenKey();
            }
lbl20: // 4 sources:
            this.mProviderTokenKey.setTrTokenId(var1_1);
            this.setupReplenishAlarm();
            do {
                return var7_5;
                break;
            } while (true);
        }
        finally {
            if (this.zL != null) {
                this.zL.decrementRefCount();
                this.zL = null;
            }
        }
        {
            catch (TokenInvalidException var6_8) {
                ** continue;
            }
        }
    }

    @Override
    public byte[] decryptUserSignature(String string) {
        if (TextUtils.isEmpty((CharSequence)string)) {
            return null;
        }
        try {
            byte[] arrby = zH.retrieveFromStorage(Base64.decode((String)string, (int)2));
            return arrby;
        }
        catch (VisaTAException visaTAException) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", "decryptUserSignature Error occured while gettting decrypted data from TA");
            com.samsung.android.spayfw.b.c.c("VisaPayProvider", visaTAException.getMessage(), (Throwable)((Object)visaTAException));
            return null;
        }
    }

    @Override
    public void delete() {
        com.samsung.android.spayfw.b.c.d("VisaPayProvider", "delete: tokenKey = " + this.mProviderTokenKey);
        if (this.zL != null) {
            this.zL.decrementRefCount();
            this.zL = null;
        }
        if (this.mProviderTokenKey == null) {
            return;
        }
        String string = this.mProviderTokenKey.cn();
        if (string == null || zG.ba(string) == null) {
            com.samsung.android.spayfw.b.c.d("VisaPayProvider", "delete: token already deleted from VisaPayProvider database : " + string);
            return;
        }
        this.zF.deleteToken(new TokenKey(this.mProviderTokenKey.cm()));
        zG.bb(string);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public String encryptUserSignature(byte[] arrby) {
        byte[] arrby2;
        if (arrby == null) {
            return null;
        }
        try {
            arrby2 = zH.storeData(arrby);
            if (arrby2 == null) return null;
        }
        catch (VisaTAException visaTAException) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", "encryptUserSignature Error occured while gettting encypted data from TA");
            com.samsung.android.spayfw.b.c.c("VisaPayProvider", visaTAException.getMessage(), (Throwable)((Object)visaTAException));
            return null;
        }
        return Base64.encodeToString((byte[])arrby2, (int)2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected byte[] generateInAppPaymentPayload(PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        byte[] arrby;
        if (inAppDetailedTransactionInfo == null || pj == null) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", " generateInAppPaymentPayload: input is null or mSelectedTokenKey is null");
            throw new PaymentProviderException(-36);
        }
        long l2 = System.currentTimeMillis();
        com.samsung.android.spayfw.b.c.d("VisaPayProvider", " generateInAppPaymentPayload: start " + l2);
        com.samsung.android.spayfw.payprovider.visa.db.a a2 = zG.ba(pj.cn());
        if (a2 == null) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", " generateInAppPaymentPayload: tokenDetails is null");
            throw new PaymentProviderException(-36);
        }
        TokenKey tokenKey = this.e(pj);
        PaymentDataRequest paymentDataRequest = this.zF.a(tokenKey, inAppDetailedTransactionInfo.getContextId(), inAppDetailedTransactionInfo.isRecurring());
        GenCryptogramResponseData genCryptogramResponseData = InAppPayment.getCryptogramInfo(this.mContext, a2.getTrTokenId(), paymentDataRequest);
        if (genCryptogramResponseData == null) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", " getCryptogramInfo: error");
            throw new PaymentProviderException(-36);
        }
        if (genCryptogramResponseData.getTokenInfo() == null) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", " getTokenInfo: null");
            throw new PaymentProviderException(-36);
        }
        InAppData inAppData = InAppPayment.buildInAppPaymentData(this.mContext, inAppDetailedTransactionInfo, genCryptogramResponseData);
        if (inAppData == null) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", " buildInAppPaymentData: error");
            throw new PaymentProviderException(-36);
        }
        String string = new GsonBuilder().disableHtmlEscaping().create().toJson((Object)inAppData);
        if (string == null) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", " buildInAppPaymentData: json error");
            throw new PaymentProviderException(-36);
        }
        com.samsung.android.spayfw.b.c.d("VisaPayProvider", "json string: " + string);
        String string2 = genCryptogramResponseData.getTokenInfo().getEncTokenInfo();
        if (string2 == null) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", " encTokenInfo: null");
            throw new PaymentProviderException(-36);
        }
        if (inAppDetailedTransactionInfo.cd() == null) {
            arrby = this.zE.b(string, string2, inAppDetailedTransactionInfo.getNonce());
        } else {
            List<byte[]> list = h.bE(inAppDetailedTransactionInfo.cd());
            if (list == null || list.isEmpty()) {
                com.samsung.android.spayfw.b.c.e("VisaPayProvider", "buildInAppPaymentData: cannot get certificate");
                throw new PaymentProviderException(-36);
            }
            arrby = this.zE.a(string, string2, inAppDetailedTransactionInfo.getNonce(), (byte[])list.get(0), (byte[])list.get(1));
        }
        if (arrby != null && arrby.length > 0) {
            com.samsung.android.spayfw.b.c.d("VisaPayProvider", "generateInAppPaymentPayload: payload length: " + arrby.length);
            this.zF.a(tokenKey, inAppDetailedTransactionInfo.isRecurring(), true);
            this.eD();
            long l3 = System.currentTimeMillis();
            com.samsung.android.spayfw.b.c.d("VisaPayProvider", "generateInAppPaymentPayload: end: " + l3);
            return arrby;
        }
        com.samsung.android.spayfw.b.c.e("VisaPayProvider", "payload empty");
        throw new PaymentProviderException(-36);
    }

    @Override
    public CertificateInfo[] getDeviceCertificates() {
        return this.zE.getCertificates();
    }

    @Override
    public com.samsung.android.spayfw.payprovider.c getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        if (!zK) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", "server cert is not set. so return empty data");
            com.samsung.android.spayfw.payprovider.c c2 = new com.samsung.android.spayfw.payprovider.c();
            c2.setErrorCode(0);
            c2.a(new JsonObject());
            return c2;
        }
        com.samsung.android.spayfw.payprovider.c c3 = this.zF.b(enrollCardInfo, billingInfo);
        this.zL = enrollCardInfo;
        this.zL.incrementRefCount();
        return c3;
    }

    @Override
    public boolean getPayReadyState() {
        f f2 = this.mProviderTokenKey;
        boolean bl = false;
        if (f2 != null) {
            String string = this.mProviderTokenKey.cn();
            bl = false;
            if (string != null) {
                bl = this.zF.aW(this.mProviderTokenKey.cn());
            }
        }
        com.samsung.android.spayfw.b.c.d("VisaPayProvider", " getPayReadyState: key" + this.mProviderTokenKey + "ret: " + bl);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected com.samsung.android.spayfw.payprovider.c getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        if (provisionTokenInfo != null && provisionTokenInfo.getActivationParams() != null) {
            (String)provisionTokenInfo.getActivationParams().get((Object)"cvv2");
            if (this.zL != null && this.zL instanceof EnrollCardPanInfo) {
                EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo)this.zL;
                if (enrollCardPanInfo.getCVV() != null) {
                    provisionTokenInfo.getActivationParams().put((Object)"cvv2", (Object)enrollCardPanInfo.getCVV());
                }
            } else if (this.zL != null && this.zL instanceof EnrollCardReferenceInfo) {
                com.samsung.android.spayfw.b.c.i("VisaPayProvider", "Provisioning by cardReferenceInfo !!!");
                Bundle bundle = ((EnrollCardReferenceInfo)this.zL).getExtraEnrollData();
                if (bundle != null) {
                    String string = bundle.getString("cardCvv");
                    String string2 = bundle.getString("cardExpMM");
                    String string3 = bundle.getString("cardExpYY");
                    String string4 = bundle.getString("cardZip");
                    com.samsung.android.spayfw.b.c.i("VisaPayProvider", "Provisioning by cardReferenceInfo: ZIP " + string4 + ", exp " + string2 + "/" + string3 + ", cvv " + string);
                    if (string != null) {
                        provisionTokenInfo.getActivationParams().put((Object)"cvv2", (Object)string);
                    }
                    if (string4 != null) {
                        provisionTokenInfo.getActivationParams().put((Object)"billingZip", (Object)string4);
                    }
                    if (string2 != null) {
                        provisionTokenInfo.getActivationParams().put((Object)"month", (Object)string2);
                    }
                    if (string3 != null) {
                        provisionTokenInfo.getActivationParams().put((Object)"year", (Object)string3);
                    }
                }
            }
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("riskData", a.a(this.mContext, provisionTokenInfo, this.zE));
        com.samsung.android.spayfw.payprovider.c c2 = new com.samsung.android.spayfw.payprovider.c();
        c2.e(bundle);
        return c2;
    }

    @Override
    public com.samsung.android.spayfw.payprovider.c getReplenishmentRequestData() {
        return this.zF.a(this.e(this.mProviderTokenKey));
    }

    @Override
    public Bundle getTokenMetaData() {
        com.samsung.android.spayfw.b.c.d("VisaPayProvider", "getTokenMetaData ");
        if (this.zF == null) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", "mSdk is null");
            return null;
        }
        if (this.mProviderTokenKey == null) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", "tokenKey is null");
            return null;
        }
        return this.zF.getTokenMetaData(this.e(this.mProviderTokenKey));
    }

    @Override
    public int getTransactionData(Bundle bundle, i i2) {
        if (this.mProviderTokenKey == null || bundle == null || i2 == null) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", "getTransactionData : invalid input ");
            return -4;
        }
        com.samsung.android.spayfw.payprovider.visa.db.a a2 = this.zF.aX(this.mProviderTokenKey.cn());
        String string = null;
        if (a2 != null) {
            long l2 = a2.eL() LCMP 0L;
            string = null;
            if (l2 > 0) {
                string = String.valueOf((long)a2.eL());
            }
        }
        this.zM = h.am(this.mContext);
        new com.samsung.android.spayfw.payprovider.visa.transaction.b().a(this.mContext, this.mProviderTokenKey, string, bundle, i2);
        return 0;
    }

    @Override
    protected com.samsung.android.spayfw.payprovider.c getVerifyIdvRequestData(VerifyIdvInfo verifyIdvInfo) {
        com.samsung.android.spayfw.payprovider.c c2 = new com.samsung.android.spayfw.payprovider.c();
        c2.setErrorCode(0);
        if (verifyIdvInfo.getIntent() != null) {
            String string = verifyIdvInfo.getIntent().getStringExtra("authenticationCodeResponse");
            Bundle bundle = new Bundle();
            bundle.putString("tac", string);
            c2.e(bundle);
            return c2;
        }
        com.samsung.android.spayfw.b.c.d("VisaPayProvider", "No Intent Data");
        return c2;
    }

    @Override
    public byte[] handleApdu(byte[] arrby, Bundle bundle) {
        return this.zF.processApdu(arrby, bundle);
    }

    @Override
    protected void init() {
        com.samsung.android.spayfw.b.c.d("VisaPayProvider", "init ");
        this.zE = new d(this.mContext, zH);
        this.zF = new c(this.mContext, zG);
    }

    @Override
    protected void interruptMstPay() {
        this.zE.interruptMstPay();
    }

    @Override
    public boolean isPayAllowedForPresentationMode(int n2) {
        if (this.mProviderTokenKey == null) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", "ProviderTokenKey is null");
            return false;
        }
        return this.zF.a(this.e(this.mProviderTokenKey), n2);
    }

    @Override
    public boolean isReplenishDataAvailable(JsonObject jsonObject) {
        return this.zF.isReplenishDataAvailable(jsonObject);
    }

    @Override
    protected void loadTA() {
        zH.loadTA();
        com.samsung.android.spayfw.b.c.i("VisaPayProvider", "load real TA");
    }

    @Override
    protected void onPaySwitch(int n2, int n3) {
        com.samsung.android.spayfw.b.c.i("VisaPayProvider", "switch payment method from MST to NFC");
        this.zI = true;
    }

    @Override
    public boolean prepareMstPay() {
        boolean bl = this.zF.prepareMstPay();
        if (!bl) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", "prepareMstPay: unable to prepare mst data in TA");
        }
        return bl;
    }

    @Override
    public boolean prepareNfcPay() {
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public e processIdvOptionsData(IdvMethod idvMethod) {
        String string;
        String string2;
        com.samsung.android.spayfw.b.c.d("VisaPayProvider", "processIdvOptionsData: ");
        e e2 = new e();
        e2.setErrorCode(0);
        if (idvMethod == null || idvMethod.getData() == null) return e2;
        if ("APP".equals((Object)idvMethod.getType())) {
            String string3;
            String string4;
            String string5;
            JsonObject jsonObject = new JsonParser().parse(idvMethod.getData()).getAsJsonObject();
            if (jsonObject.get("source") != null) {
                string5 = jsonObject.get("source").getAsString();
                if (string5 != null) {
                    string4 = string5.substring(0, string5.indexOf(124));
                    string5 = string5.replaceAll("\\|", "\\.");
                    com.samsung.android.spayfw.b.c.d("VisaPayProvider", "Source: " + string5 + " Package Name : " + string4);
                } else {
                    com.samsung.android.spayfw.b.c.w("VisaPayProvider", "Source is null after converting as string");
                    string4 = null;
                }
            } else {
                com.samsung.android.spayfw.b.c.w("VisaPayProvider", "Source is missing in App2App IDV data");
                string4 = null;
                string5 = null;
            }
            if (jsonObject.get("requestPayload") != null) {
                String string6 = jsonObject.get("requestPayload").getAsString();
                com.samsung.android.spayfw.b.c.d("VisaPayProvider", "Old Payload URL : " + string6);
                string3 = Base64.encodeToString((byte[])Base64.decode((String)string6, (int)2), (int)2);
                com.samsung.android.spayfw.b.c.d("VisaPayProvider", "New Payload URL : " + string3);
            } else {
                com.samsung.android.spayfw.b.c.w("VisaPayProvider", "Bank app payload is missing in App2App IDV data");
                string3 = null;
            }
            Bundle bundle = new Bundle();
            bundle.putString("intentAction", string5);
            bundle.putString("packageName", string4);
            bundle.putString("payloadData", string3);
            e2.e(bundle);
            return e2;
        }
        if (!"CODE_ONLINEBANKING".equals((Object)idvMethod.getType())) return e2;
        JsonObject jsonObject = new JsonParser().parse(idvMethod.getData()).getAsJsonObject();
        if (jsonObject.get("source") != null) {
            String string7 = jsonObject.get("source").getAsString();
            if (string7 != null) {
                string2 = string7.substring(0, string7.indexOf(124));
                string = string7.substring(1 + string7.indexOf(124));
                com.samsung.android.spayfw.b.c.d("VisaPayProvider", "amount: " + string2 + " currenyCode : " + string);
            } else {
                com.samsung.android.spayfw.b.c.w("VisaPayProvider", "Source is null after converting as string");
                string2 = null;
                string = null;
            }
        } else {
            com.samsung.android.spayfw.b.c.w("VisaPayProvider", "Source is missing in CODE_ONLINE_BANKING IDV data");
            string2 = null;
            string = null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("amount", string2);
        bundle.putString("currencyCode", string);
        e2.e(bundle);
        return e2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected TransactionDetails processTransactionData(Object object) {
        com.samsung.android.spayfw.payprovider.visa.db.a a2;
        TransactionDetails transactionDetails = this.zE.a(this.mProviderTokenKey, object);
        if (transactionDetails != null && (a2 = this.zF.aX(this.mProviderTokenKey.cn())) != null) {
            if (this.zM == -1L) {
                com.samsung.android.spayfw.b.c.e("VisaPayProvider", "unable to update transaction fetch time ");
            } else {
                a2.A(this.zM);
            }
            zG.d(a2);
            this.zF.a(a2);
            com.samsung.android.spayfw.b.c.d("VisaPayProvider", "processTransactionData : updated transaction fetch time " + this.zM);
        }
        this.zM = -1L;
        return transactionDetails;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected void replenishAlarmExpired() {
        if (this.mProviderTokenKey == null) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", "cannot fire replenishment, providerTokenKey is null");
            return;
        } else {
            if (ps == null) return;
            {
                ps.a(this.mProviderTokenKey);
                return;
            }
        }
    }

    @Override
    protected e replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        String string = this.mProviderTokenKey.getTrTokenId();
        com.samsung.android.spayfw.b.c.d("VisaPayProvider", "replenishToken: trTokenId = " + string);
        return this.zF.a(string, this.mProviderTokenKey, jsonObject, tokenStatus, ps);
    }

    @Override
    public SelectCardResult selectCard() {
        boolean bl = true;
        this.zF.selectCard(this.e(this.mProviderTokenKey));
        String string = this.zE.i(null);
        SelectCardResult selectCardResult = null;
        if (string != null) {
            byte[] arrby = this.zE.getNonce();
            selectCardResult = null;
            if (arrby != null) {
                selectCardResult = new SelectCardResult(string, arrby);
                pj = this.mProviderTokenKey;
                bl = false;
            }
        }
        if (bl) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", "selectCard error occured");
        }
        return selectCardResult;
    }

    @Override
    public void setPayAuthenticationMode(String string) {
        this.zF.setPayAuthenticationMode(string);
    }

    @Override
    public void setPaymentFrameworkRequester(k k2) {
        ps = k2;
    }

    @Override
    public boolean setServerCertificates(CertificateInfo[] arrcertificateInfo) {
        zK = this.zE.a(arrcertificateInfo);
        return zK;
    }

    @Override
    public void setupReplenishAlarm() {
        String string = this.mProviderTokenKey.getTrTokenId();
        this.zF.b(string, this.mProviderTokenKey, ps);
    }

    @Override
    public boolean startMstPay(int n2, byte[] arrby) {
        boolean bl = this.zE.startMstPay(n2, arrby);
        if (!bl) {
            com.samsung.android.spayfw.b.c.e("VisaPayProvider", "startPayMst: failed to transmit mst data");
        }
        return bl;
    }

    @Override
    public void stopMstPay(boolean bl) {
        this.zI = false;
        if (this.zJ) {
            com.samsung.android.spayfw.b.c.i("VisaPayProvider", "mIgnoreToInformVisaSdk flag is set so no need to inform sdk abt stopMst");
            this.zJ = false;
            this.zE.stopMstPay();
            return;
        }
        com.samsung.android.spayfw.b.c.d("VisaPayProvider", "stopMstPay: stop visa mst pay");
        com.samsung.android.spayfw.b.c.i("VisaPayProvider", "stopMstPay: SDK start: " + System.currentTimeMillis());
        this.zF.stopMstPay(bl);
        com.samsung.android.spayfw.b.c.i("VisaPayProvider", "stopMstPay: SDK end: " + System.currentTimeMillis());
        this.zE.stopMstPay();
        if (bl) {
            this.eD();
        }
        pj = null;
    }

    @Override
    public Bundle stopNfcPay(int n2) {
        Bundle bundle = this.zF.a(n2, pj);
        if (bundle.getShort("nfcApduErrorCode") == 2) {
            this.eD();
            pj = null;
            if (this.zI) {
                this.zJ = true;
            }
        }
        return bundle;
    }

    @Override
    protected void unloadTA() {
        zH.unloadTA();
        com.samsung.android.spayfw.b.c.i("VisaPayProvider", "unload real TA");
    }

    @Override
    protected e updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        int n2 = this.zF.a(this.mProviderTokenKey, jsonObject, tokenStatus, ps);
        e e2 = new e();
        e2.setErrorCode(n2);
        e2.setProviderTokenKey(this.mProviderTokenKey);
        return e2;
    }
}

