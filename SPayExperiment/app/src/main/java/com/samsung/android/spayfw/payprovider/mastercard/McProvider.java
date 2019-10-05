/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.util.Base64
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Integer
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.samsung.android.spayfw.payprovider.mastercard;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.PaymentProviderException;
import com.samsung.android.spayfw.payprovider.a;
import com.samsung.android.spayfw.payprovider.c;
import com.samsung.android.spayfw.payprovider.d;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.payprovider.mastercard.pce.MCTransactionService;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPInputData;
import com.samsung.android.spayfw.payprovider.mastercard.pce.data.DSRPOutputData;
import com.samsung.android.spayfw.payprovider.mastercard.tds.McTdsManager;
import com.samsung.android.spayfw.payprovider.mastercard.tokenmanagement.McTokenManager;
import com.samsung.android.spayfw.payprovider.mastercard.tzsvc.McTAController;
import com.samsung.android.spayfw.payprovider.mastercard.utils.CryptoUtils;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;

public class McProvider
extends PaymentNetworkProvider {
    private static final String JSON_KEY_ACTION = "action";
    private static final String JSON_KEY_EXTRA_TEXT = "extraTextValue";
    private static final String JSON_KEY_PKGNAME = "packageName";
    private static final String TAG = "McProvider";
    public static final String TDS_TAG_ERROR = "e_";
    public static final String TDS_TAG_INFO = "i_";
    private static boolean initCleanupDone;
    private static String mEmailHash;
    private McTokenManager mMcTokenManager;
    private MCTransactionService mTransactionService;

    public McProvider(Context context, String string) {
        super(context, string);
        McTAController.createOnlyInstance(context);
        this.mTAController = McTAController.getInstance();
        this.mMcTokenManager = new McTokenManager(context);
        this.mTransactionService = new MCTransactionService(context);
    }

    public McProvider(Context context, String string, f f2) {
        this(context, string);
        McProvider.initCleanup(context);
        if (f2 != null) {
            McTdsManager.getInstance(f2.cm());
        }
    }

    private String getAppId() {
        return "SAMSUNGPAY1";
    }

    public static Context getContext() {
        Class<McProvider> class_ = McProvider.class;
        synchronized (McProvider.class) {
            PaymentFrameworkApp paymentFrameworkApp = PaymentFrameworkApp.aB();
            // ** MonitorExit[var2] (shouldn't be in output)
            return paymentFrameworkApp;
        }
    }

    private String getEmailAddressHash(String string) {
        return CryptoUtils.convertbyteToHexString(CryptoUtils.getAccountIdHash(string));
    }

    public static String getEmailHash() {
        return mEmailHash;
    }

    private static void initCleanup(Context context) {
        Class<McProvider> class_ = McProvider.class;
        synchronized (McProvider.class) {
            if (!initCleanupDone) {
                Log.i(TAG, "initCleanup: performing one time cleanup operations");
                long l2 = McTokenManager.deleteStaleEnrollmentData(context);
                if (l2 > 0L) {
                    Log.i(TAG, "initCleanup: deleted entries: " + l2);
                }
                initCleanupDone = true;
            }
            return;
        }
    }

    private void unloadMcTAController(McTAController mcTAController) {
        try {
            mcTAController.unloadTA();
            return;
        }
        catch (Exception exception) {
            Log.e(TAG, "unloadMcTAController : McTAController is null");
            return;
        }
    }

    @Override
    public boolean authenticateTransaction(SecuredObject securedObject) {
        return this.mTransactionService.authenticateTransaction(securedObject);
    }

    @Override
    public void beginPay(boolean bl, boolean bl2) {
        Log.d(TAG, "beginPay authenticated " + bl);
        this.mTransactionService.initTransaction(bl);
    }

    @Override
    public void clearCard() {
        Log.i(TAG, "clearCard");
        this.mTransactionService.clearCard();
    }

    @Override
    public e createToken(String string, c c2, int n2) {
        return this.mMcTokenManager.provisionToken(c2.ch(), n2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public byte[] decryptUserSignature(String string) {
        if (string == null) {
            Log.d(TAG, "decryptUserSignature : input data null");
            return null;
        } else {
            byte[] arrby = this.mMcTokenManager.processSignatureData(Base64.decode((String)string, (int)2), 2);
            if (arrby == null) return null;
            return arrby;
        }
    }

    @Override
    public void delete() {
        this.mMcTokenManager.deleteToken(this.mProviderTokenKey);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public String encryptUserSignature(byte[] arrby) {
        if (arrby == null) {
            Log.d(TAG, "encryptUserSignature : input data null");
            return null;
        } else {
            byte[] arrby2 = this.mMcTokenManager.processSignatureData(arrby, 1);
            if (arrby2 == null) return null;
            return Base64.encodeToString((byte[])arrby2, (int)2);
        }
    }

    @Override
    public void endPay() {
        Log.d(TAG, "endTransaction");
    }

    @Override
    protected byte[] generateInAppPaymentPayload(PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        McPayDataHandler mcPayDataHandler = new McPayDataHandler();
        DSRPInputData dSRPInputData = mcPayDataHandler.convertToDsrpInput(inAppDetailedTransactionInfo);
        DSRPOutputData dSRPOutputData = this.mTransactionService.getPayInfoData(dSRPInputData);
        if (dSRPOutputData == null) {
            Log.e(TAG, "Failed to create dsrpOutput !!!");
            throw new PaymentProviderException(-36);
        }
        return mcPayDataHandler.convertToPaymentGatewayFormat(inAppDetailedTransactionInfo.cd(), inAppDetailedTransactionInfo.getNonce(), dSRPOutputData);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public a getCasdParameters() {
        block11 : {
            block10 : {
                var1_1 = null;
                this.mTAController = McTAController.getInstance();
                var5_2 = (McTAController)this.mTAController;
                var5_2.loadTA();
                var9_3 = var5_2.getCasdParameters();
                if (var9_3 == null) break block10;
                var4_4 = new a();
                var4_4.ao(Base64.encodeToString((byte[])var9_3.hpk, (int)2));
                var4_4.ap(Base64.encodeToString((byte[])var9_3.huid, (int)2));
                break block11;
                {
                    catch (Throwable var7_9) {
                        var1_1 = var5_2;
                        var2_8 = var7_9;
                        ** GOTO lbl-1000
                    }
                }
                catch (Exception var3_5) {
                    block12 : {
                        var4_4 = null;
                        break block12;
                        catch (Exception var6_10) {
                            var1_1 = var5_2;
                            var3_6 = var6_10;
                            var4_4 = null;
                            break block12;
                        }
                        catch (Exception var10_11) {
                            var1_1 = var5_2;
                            var3_6 = var10_11;
                        }
                    }
                    try {
                        var3_6.printStackTrace();
                    }
                    catch (Throwable var2_7) lbl-1000: // 2 sources:
                    {
                        this.unloadMcTAController(var1_1);
                        throw var2_8;
                    }
                    this.unloadMcTAController(var1_1);
                    return var4_4;
                }
            }
            var4_4 = null;
        }
        this.unloadMcTAController(var5_2);
        return var4_4;
    }

    @Override
    public CertificateInfo[] getDeviceCertificates() {
        return null;
    }

    @Override
    public c getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        c c2 = this.mMcTokenManager.getEnrollmentMcData(enrollCardInfo, billingInfo);
        if (c2.getErrorCode() != 0) {
            Log.e(TAG, "Failed to create Enrollment Request" + c2.getErrorCode());
            return c2;
        }
        Bundle bundle = new Bundle();
        if (enrollCardInfo.getUserEmail() != null) {
            mEmailHash = this.getEmailAddressHash(enrollCardInfo.getUserEmail());
            bundle.putString("emailHash", mEmailHash);
        }
        bundle.putString("appId", this.getAppId());
        c2.e(bundle);
        return c2;
    }

    @Override
    public boolean getPayReadyState() {
        return true;
    }

    @Override
    protected c getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        return this.mMcTokenManager.getProvisionRequestData(provisionTokenInfo);
    }

    @Override
    public c getReplenishmentRequestData() {
        return null;
    }

    @Override
    public Bundle getTokenMetaData() {
        int n2 = this.mTransactionService.getIssuerCountryCode(this.mProviderTokenKey);
        if (n2 == -2) {
            Log.e(TAG, "Error obtaining CRM Country !!!");
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putString("tokenMetadataIssuerCountryCode", Integer.toHexString((int)n2));
        Log.d(TAG, "crmCountryCode = " + bundle.toString());
        return bundle;
    }

    @Override
    public int getTransactionData(Bundle bundle, i i2) {
        if (this.mProviderTokenKey == null) {
            Log.e("mctds_", "providerTokenKey is null..");
            return -2;
        }
        try {
            int n2 = McTdsManager.getInstance(this.mProviderTokenKey.cm()).getTransactionData(bundle, i2);
            return n2;
        }
        catch (NullPointerException nullPointerException) {
            Log.e("mctds_", "getTransactionData: NPE occured: " + nullPointerException.getMessage());
            nullPointerException.printStackTrace();
            return -2;
        }
    }

    @Override
    public byte[] handleApdu(byte[] arrby, Bundle bundle) {
        return this.mTransactionService.processApdu(arrby, bundle);
    }

    @Override
    protected void init() {
    }

    @Override
    protected void interruptMstPay() {
        Log.d(TAG, "MCProvider: interruptMstPay");
        this.mTransactionService.interruptMstPay();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean isDsrpBlobMissing() {
        boolean bl = !this.mTransactionService.isDSRPSupportedByProfile(this.mProviderTokenKey);
        Log.d(TAG, "isDsrpBlobMissing: mcprovider: ret = " + bl);
        return bl;
    }

    public boolean isMstSupported(f f2) {
        return this.mTransactionService.isMSTSupportedByProfile(f2);
    }

    @Override
    public boolean isPayAllowedForPresentationMode(int n2) {
        if (n2 == 2) {
            return this.isMstSupported(this.mProviderTokenKey);
        }
        return true;
    }

    @Override
    protected void loadTA() {
        this.mTransactionService.loadTA();
    }

    @Override
    public boolean prepareMstPay() {
        return this.mTransactionService.prepareMstPay();
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
        Log.d(TAG, "processIdvOptionsData: ");
        e e2 = new e();
        e2.setErrorCode(0);
        if (idvMethod != null && idvMethod.getData() != null && "APP".equals((Object)idvMethod.getType())) {
            String string;
            String string2;
            JsonObject jsonObject = new JsonParser().parse(idvMethod.getData()).getAsJsonObject();
            if (jsonObject.get(JSON_KEY_ACTION) != null) {
                string = jsonObject.get(JSON_KEY_ACTION).getAsString();
                Log.d(TAG, "intentAction : " + string);
            } else {
                Log.w(TAG, "intentAction : is null ");
                string = null;
            }
            if (jsonObject.get(JSON_KEY_PKGNAME) != null) {
                string2 = jsonObject.get(JSON_KEY_PKGNAME).getAsString();
                Log.d(TAG, "Package Name : " + string2);
            } else {
                Log.w(TAG, "intentAction : is null ");
                string2 = null;
            }
            JsonElement jsonElement = jsonObject.get(JSON_KEY_EXTRA_TEXT);
            String string3 = null;
            if (jsonElement != null) {
                String string4 = jsonObject.get(JSON_KEY_EXTRA_TEXT).getAsString();
                Log.d(TAG, "Old Payload URL : " + string4);
                string3 = Base64.encodeToString((byte[])Base64.decode((String)string4, (int)2), (int)2);
                Log.d(TAG, "New Payload URL : " + string3);
            }
            Bundle bundle = new Bundle();
            bundle.putString("intentAction", string);
            bundle.putString(JSON_KEY_PKGNAME, string2);
            bundle.putString("payloadData", string3);
            e2.e(bundle);
        }
        return e2;
    }

    @Override
    protected TransactionDetails processTransactionData(Object object) {
        Log.i("mctds_", "processTransactionData: ");
        if (object != null && object instanceof TransactionDetails) {
            return (TransactionDetails)object;
        }
        Log.e("mctds_", "processTransactionData: Invalid data pased");
        return null;
    }

    @Override
    public void reconstructMissingDsrpBlob() {
        Log.d(TAG, "reconstructMissingDsrpBlob");
        this.mTransactionService.validateDSRPProfile(this.mProviderTokenKey);
        Log.d(TAG, "reconstructAlternateProfile");
        this.mTransactionService.validateAlternateProfile(this.mProviderTokenKey);
    }

    @Override
    public e replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }

    @Override
    public SelectCardResult selectCard() {
        return this.mTransactionService.selectCard(this.mProviderTokenKey);
    }

    @Override
    public boolean setCasdCertificate(String string) {
        McTAController mcTAController;
        block5 : {
            mcTAController = null;
            try {
                mcTAController = McTAController.getInstance();
                mcTAController.loadTA();
                int n2 = mcTAController.writeCasdCert(Base64.decode((String)string, (int)2));
                if (n2 != 0) break block5;
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return false;
            }
            finally {
                this.unloadMcTAController(mcTAController);
            }
            this.unloadMcTAController(mcTAController);
            return true;
        }
        this.unloadMcTAController(mcTAController);
        return false;
    }

    @Override
    public boolean setServerCertificates(CertificateInfo[] arrcertificateInfo) {
        return this.mMcTokenManager.updateMcCerts(arrcertificateInfo);
    }

    @Override
    public boolean startMstPay(int n2, byte[] arrby) {
        return this.mTransactionService.startMstPay(n2, arrby);
    }

    @Override
    public void stopMstPay(boolean bl) {
        this.mTransactionService.stopMstPay();
    }

    @Override
    public Bundle stopNfcPay(int n2) {
        Log.i(TAG, "stopNfcPay : " + n2);
        return this.mTransactionService.stopNfcPay(n2);
    }

    @Override
    protected void unloadTA() {
        this.mTransactionService.unloadTA();
    }

    @Override
    public void updateRequestStatus(d d2) {
        if (d2 != null) {
            Log.i(TAG, "updateRequestStatus Status :" + d2.ci() + " Type:" + d2.getRequestType());
            if (d2.ci() == 0 && d2.getRequestType() == 3 && d2.ck() != null && d2.ck().cn() != null) {
                Log.i(TAG, "updateRequestStatus Provision Complete");
                this.mMcTokenManager.setUniqueTokenReference(d2.ck().cn());
            }
            return;
        }
        Log.e(TAG, "ProviderRequestStatus is null");
    }

    @Override
    public e updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        return this.mMcTokenManager.updateTokenData(this.mProviderTokenKey, jsonObject, tokenStatus);
    }
}

