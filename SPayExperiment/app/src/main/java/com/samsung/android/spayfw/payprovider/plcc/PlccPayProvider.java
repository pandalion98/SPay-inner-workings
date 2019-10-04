/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.text.TextUtils
 *  android.util.Base64
 *  com.google.gson.JsonObject
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.security.MessageDigest
 *  java.security.NoSuchAlgorithmException
 *  java.util.Arrays
 *  java.util.HashMap
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Objects
 */
package com.samsung.android.spayfw.payprovider.plcc;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.MstPayConfig;
import com.samsung.android.spayfw.appinterface.PayConfig;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.c;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.payprovider.plcc.PlccPayProviderSdk;
import com.samsung.android.spayfw.payprovider.plcc.domain.PlccCard;
import com.samsung.android.spayfw.payprovider.plcc.exception.PlccException;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.payprovider.plcc.util.SequenceUtils;
import com.samsung.android.spayfw.payprovider.plcc.util.Util;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spaytzsvc.api.TAController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class PlccPayProvider
extends PaymentNetworkProvider {
    private static final String TAG = "PlccPayProvider";
    private static PlccTAController mPlccTac = null;
    private Context mContext;
    private PlccPayProviderSdk mSdk;

    public PlccPayProvider(Context context, String string, f f2) {
        super(context, string);
        this.mContext = context;
        this.mTAController = PlccTAController.createOnlyInstance(this.mContext);
        mPlccTac = (PlccTAController)this.mTAController;
        this.mSdk = new PlccPayProviderSdk(context);
    }

    private JsonObject buildJsonFromCard(EnrollCardPanInfo enrollCardPanInfo, BillingInfo billingInfo) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("pan", enrollCardPanInfo.getPAN());
        jsonObject.addProperty("cvv2", enrollCardPanInfo.getCVV());
        com.samsung.android.spayfw.b.c.d(TAG, "debug cvv = " + enrollCardPanInfo.getCVV());
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.addProperty("encrypted", this.mSdk.utilityEnc4ServerTransport(jsonObject.toString()));
        return jsonObject2;
    }

    private String getEmailAddressHash(String string) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance((String)"SHA-256");
            messageDigest.update(string.getBytes());
            byte[] arrby = messageDigest.digest();
            com.samsung.android.spayfw.b.c.d(TAG, "getEmailAddressHash: digest " + Arrays.toString((byte[])arrby));
            String string2 = Base64.encodeToString((byte[])arrby, (int)11);
            com.samsung.android.spayfw.b.c.d(TAG, "getEmailAddressHash: emailAddress " + string + " Hash: " + string2);
            return string2;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            com.samsung.android.spayfw.b.c.c(TAG, noSuchAlgorithmException.getMessage(), noSuchAlgorithmException);
            return null;
        }
    }

    @Override
    public boolean authenticateTransaction(SecuredObject securedObject) {
        return this.mSdk.authenticateTransaction(securedObject.getSecureObjectData());
    }

    @Override
    public void clearCard() {
        this.mSdk.clearCard();
    }

    @Override
    public e createToken(String string, c c2, int n2) {
        e e2 = new e();
        e2.setProviderTokenKey(new f(string));
        this.mSdk.addCard(string, string, c2.ch());
        return e2;
    }

    @Override
    public byte[] decryptUserSignature(String string) {
        if (TextUtils.isEmpty((CharSequence)string)) {
            return null;
        }
        try {
            byte[] arrby = mPlccTac.retrieveFromStorage(Base64.decode((String)string, (int)2));
            return arrby;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.e(TAG, "decryptUserSignature Error occured while gettting decrypted data from TA");
            com.samsung.android.spayfw.b.c.c(TAG, exception.getMessage(), exception);
            return null;
        }
    }

    @Override
    public void delete() {
        if (this.mProviderTokenKey != null) {
            this.mSdk.removeCard(this.mProviderTokenKey.cn());
            return;
        }
        com.samsung.android.spayfw.b.c.d(TAG, "Provider Key null");
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
            arrby2 = mPlccTac.storeData(arrby);
            if (arrby2 == null) return null;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.e(TAG, "encryptUserSignature Error occured while gettting encypted data from TA");
            com.samsung.android.spayfw.b.c.c(TAG, exception.getMessage(), exception);
            return null;
        }
        return Base64.encodeToString((byte[])arrby2, (int)2);
    }

    @Override
    protected byte[] generateInAppPaymentPayload(PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        return null;
    }

    @Override
    public CertificateInfo[] getDeviceCertificates() {
        HashMap<String, byte[]> hashMap = this.mSdk.getDeviceCertificates();
        if (hashMap == null) {
            return null;
        }
        String string = Util.convertToPem((byte[])hashMap.get((Object)"cert_enc"));
        String string2 = Util.convertToPem((byte[])hashMap.get((Object)"cert_sign"));
        String string3 = Util.convertToPem((byte[])hashMap.get((Object)"cert_ca"));
        if (string == null || string2 == null || string3 == null) {
            return null;
        }
        CertificateInfo[] arrcertificateInfo = new CertificateInfo[3];
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setAlias("ENC");
        certificateInfo.setContent(string);
        certificateInfo.setUsage("ENC");
        arrcertificateInfo[0] = certificateInfo;
        CertificateInfo certificateInfo2 = new CertificateInfo();
        certificateInfo2.setAlias("SIG");
        certificateInfo2.setContent(string2);
        certificateInfo2.setUsage("SIG");
        arrcertificateInfo[1] = certificateInfo2;
        CertificateInfo certificateInfo3 = new CertificateInfo();
        certificateInfo3.setAlias("CA");
        certificateInfo3.setContent(string3);
        certificateInfo3.setUsage("CA");
        arrcertificateInfo[2] = certificateInfo3;
        return arrcertificateInfo;
    }

    @Override
    public c getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        c c2 = new c();
        c2.setErrorCode(0);
        if (enrollCardInfo == null || billingInfo == null) {
            c2.setErrorCode(-4);
            com.samsung.android.spayfw.b.c.e(TAG, "getEnrollmentRequestData: invalid input ");
            return c2;
        }
        if (enrollCardInfo instanceof EnrollCardPanInfo) {
            c2.a(this.buildJsonFromCard((EnrollCardPanInfo)enrollCardInfo, billingInfo));
        }
        Bundle bundle = new Bundle();
        if (enrollCardInfo.getUserEmail() != null) {
            bundle.putString("emailHash", this.getEmailAddressHash(enrollCardInfo.getUserEmail()));
        }
        c2.e(bundle);
        return c2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public String getMerchantId() {
        Iterator iterator;
        String string = null;
        try {
            iterator = this.mSdk.listCard().iterator();
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.c(TAG, exception.getMessage(), exception);
            return string;
        }
        while (iterator.hasNext()) {
            String string2;
            PlccCard plccCard = (PlccCard)iterator.next();
            String string3 = Objects.equals((Object)plccCard.getProviderKey(), (Object)this.mProviderTokenKey.cn()) ? (string2 = plccCard.getMerchantId()) : string;
            string = string3;
        }
        return string;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public PayConfig getPayConfig() {
        MstPayConfig mstPayConfig;
        if (this.mProviderTokenKey == null) return null;
        String string = this.mProviderTokenKey.cn();
        PayConfig payConfig = new PayConfig();
        try {
            mstPayConfig = SequenceUtils.buildSequenceFromString(this.mSdk.getPayConfig(string));
        }
        catch (PlccException plccException) {
            com.samsung.android.spayfw.b.c.c(TAG, plccException.getMessage(), (Throwable)((Object)plccException));
            return null;
        }
        payConfig.setMstPayConfig(mstPayConfig);
        return payConfig;
    }

    @Override
    public int getPayConfigTransmitTime(boolean bl) {
        return 23;
    }

    @Override
    public boolean getPayReadyState() {
        return true;
    }

    @Override
    protected c getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        return null;
    }

    @Override
    public c getReplenishmentRequestData() {
        return null;
    }

    @Override
    public int getTransactionData(Bundle bundle, i i2) {
        return 0;
    }

    @Override
    public byte[] handleApdu(byte[] arrby, Bundle bundle) {
        return null;
    }

    @Override
    protected void init() {
    }

    @Override
    protected void interruptMstPay() {
    }

    @Override
    protected void loadTA() {
        this.mSdk.loadTA();
    }

    @Override
    public boolean prepareMstPay() {
        return true;
    }

    @Override
    public boolean prepareNfcPay() {
        return false;
    }

    @Override
    protected TransactionDetails processTransactionData(Object object) {
        return null;
    }

    @Override
    public e replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }

    @Override
    public SelectCardResult selectCard() {
        if (this.mProviderTokenKey != null) {
            String string = this.mProviderTokenKey.cn();
            SelectCardResult selectCardResult = new SelectCardResult(this.mSdk.getTaid(string), this.mSdk.getNonce(32));
            this.mSdk.selectCard(string);
            return selectCardResult;
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean setServerCertificates(CertificateInfo[] arrcertificateInfo) {
        byte[] arrby = null;
        if (arrcertificateInfo == null) return false;
        if (arrcertificateInfo.length == 0) {
            return false;
        }
        int n2 = arrcertificateInfo.length;
        byte[] arrby2 = null;
        byte[] arrby3 = null;
        for (int i2 = 0; i2 < n2; ++i2) {
            CertificateInfo certificateInfo = arrcertificateInfo[i2];
            String string = certificateInfo.getContent().replace((CharSequence)"-----BEGIN CERTIFICATE-----", (CharSequence)"").replace((CharSequence)"-----END CERTIFICATE-----", (CharSequence)"");
            if ("ENC".equals((Object)certificateInfo.getUsage())) {
                arrby3 = Base64.decode((String)string, (int)0);
                continue;
            }
            if ("VER".equals((Object)certificateInfo.getUsage())) {
                arrby2 = Base64.decode((String)string, (int)0);
                continue;
            }
            if (!"CA".equals((Object)certificateInfo.getUsage())) continue;
            arrby = Base64.decode((String)string, (int)0);
        }
        if (arrby3 != null && arrby2 != null && arrby != null) {
            this.mSdk.setPlccServerCerts(arrby2, arrby3, arrby);
            return true;
        }
        return false;
    }

    @Override
    public boolean startMstPay(int n2, byte[] arrby) {
        try {
            com.samsung.android.spayfw.b.c.i(TAG, "Mstpayconfig length = " + arrby.length);
            boolean bl = this.mSdk.startMstTransmit(n2, arrby);
            return bl;
        }
        catch (PlccException plccException) {
            com.samsung.android.spayfw.b.c.c(TAG, plccException.getMessage(), (Throwable)((Object)plccException));
            return false;
        }
    }

    @Override
    public void stopMstPay(boolean bl) {
        this.mSdk.stopMstTransmit();
    }

    @Override
    public Bundle stopNfcPay(int n2) {
        Bundle bundle = new Bundle();
        bundle.putShort("nfcApduErrorCode", (short)1);
        return bundle;
    }

    @Override
    protected void unloadTA() {
        this.mSdk.unloadTA();
    }

    public void updateSequenceConfig(HashMap<String, String> hashMap) {
        this.mSdk.updateSequenceConfig(hashMap);
    }

    @Override
    public e updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        e e2 = new e();
        if (this.mSdk != null && this.mProviderTokenKey != null) {
            this.mSdk.updateCard(this.mProviderTokenKey.cn(), tokenStatus.getCode());
            e2.setProviderTokenKey(this.mProviderTokenKey);
            return e2;
        }
        return null;
    }
}

