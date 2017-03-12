package com.samsung.android.spayfw.payprovider.p016c;

import android.content.Context;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EncryptedImage;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardLoyaltyInfo;
import com.samsung.android.spayfw.appinterface.ExtractLoyaltyCardDetailRequest;
import com.samsung.android.spayfw.appinterface.LoyaltyCardDetail;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.ProvisionTokenInfo;
import com.samsung.android.spayfw.appinterface.ProvisionTokenResult;
import com.samsung.android.spayfw.appinterface.SecuredObject;
import com.samsung.android.spayfw.appinterface.SelectCardResult;
import com.samsung.android.spayfw.appinterface.Token;
import com.samsung.android.spayfw.appinterface.TokenMetaData;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider.InAppDetailedTransactionInfo;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.ExtractCardDetailResult;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController;
import com.samsung.android.spayfw.payprovider.plcc.tzsvc.PlccTAController.TACerts;
import com.samsung.android.spayfw.payprovider.plcc.util.PlccConstants;
import com.samsung.android.spayfw.payprovider.plcc.util.Util;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/* renamed from: com.samsung.android.spayfw.payprovider.c.a */
public class LoyaltyCardPayProvider extends PaymentNetworkProvider {
    private static PlccTAController zg;
    private final Gson mGson;
    private byte[] zD;

    public LoyaltyCardPayProvider(Context context, String str, ProviderTokenKey providerTokenKey) {
        super(context, str);
        this.zD = null;
        this.mGson = new GsonBuilder().disableHtmlEscaping().create();
        this.mContext = context;
        this.mTAController = PlccTAController.createOnlyInstance(this.mContext);
        zg = (PlccTAController) this.mTAController;
    }

    protected ProviderRequestData getEnrollmentRequestData(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        Log.m285d("LoyaltyCardPayProvider", "Entered getEnrollmentRequestData");
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        if (enrollCardInfo == null) {
            Log.m286e("LoyaltyCardPayProvider", " getEnrollmentRequestData: Invalid input");
            providerRequestData.setErrorCode(-4);
            return providerRequestData;
        }
        if (enrollCardInfo instanceof EnrollCardLoyaltyInfo) {
            JsonObject b = m821b((EnrollCardLoyaltyInfo) enrollCardInfo);
            if (b != null) {
                providerRequestData.m822a(b);
                Log.m285d("LoyaltyCardPayProvider", "Set Encrypted result data");
            } else {
                providerRequestData.setErrorCode(-2);
                Log.m286e("LoyaltyCardPayProvider", " getEnrollmentRequestData: PAYFW_ERROR_PAY_PROVIDER");
                return providerRequestData;
            }
        }
        Bundle bundle = new Bundle();
        if (enrollCardInfo.getUserEmail() != null) {
            bundle.putString("emailHash", getEmailAddressHash(enrollCardInfo.getUserEmail()));
        }
        providerRequestData.m823e(bundle);
        return providerRequestData;
    }

    private JsonObject m821b(EnrollCardLoyaltyInfo enrollCardLoyaltyInfo) {
        Log.m285d("LoyaltyCardPayProvider", "Entered buildJsonFromCard");
        String loyaltyInfo = enrollCardLoyaltyInfo.getLoyaltyInfo();
        List<EncryptedImage> encryptedImages = enrollCardLoyaltyInfo.getEncryptedImages();
        Log.m285d("LoyaltyCardPayProvider", "loyaltyCardInfo = " + loyaltyInfo);
        JsonObject jsonObject = new JsonObject();
        try {
            byte[] bytes = loyaltyInfo.getBytes();
            long am = Utils.am(this.mContext);
            Log.m285d("LoyaltyCardPayProvider", "Network Time = " + am);
            PlccTAController plccTAController = zg;
            PlccTAController plccTAController2 = zg;
            loyaltyInfo = Base64.encodeToString(plccTAController.utility_enc4Server_Transport(PlccTAController.CARD_BRAND_LOYALTY, bytes, am), 2);
        } catch (Throwable e) {
            Log.m284c("LoyaltyCardPayProvider", e.getMessage(), e);
            loyaltyInfo = null;
        }
        if (loyaltyInfo == null || loyaltyInfo.equals(BuildConfig.FLAVOR)) {
            Log.m285d("LoyaltyCardPayProvider", "encrypted data is either null or empty = " + loyaltyInfo);
            return null;
        }
        Log.m285d("LoyaltyCardPayProvider", "encrypted data  = " + loyaltyInfo);
        jsonObject.addProperty("encryptedData", loyaltyInfo);
        if (encryptedImages != null) {
            JsonElement jsonArray = new JsonArray();
            for (EncryptedImage encryptedImage : encryptedImages) {
                Log.m285d("LoyaltyCardPayProvider", "encrypted image = " + encryptedImage);
                if (encryptedImage != null) {
                    JsonElement jsonObject2 = new JsonObject();
                    Log.m285d("LoyaltyCardPayProvider", "encrypted image: Usage = " + encryptedImage.getUsage());
                    jsonObject2.addProperty("usage", encryptedImage.getUsage());
                    Log.m285d("LoyaltyCardPayProvider", "encrypted image: Content = " + encryptedImage.getContent());
                    jsonObject2.addProperty("content", encryptedImage.getContent());
                    jsonArray.add(jsonObject2);
                }
            }
            jsonObject.add("encImages", jsonArray);
        } else {
            Log.m285d("LoyaltyCardPayProvider", "Encrypted images null");
        }
        return jsonObject;
    }

    private String getEmailAddressHash(String str) {
        try {
            Log.m285d("LoyaltyCardPayProvider", "Entered getEmailAddressHash");
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            instance.update(str.getBytes());
            byte[] digest = instance.digest();
            Log.m285d("LoyaltyCardPayProvider", "getEmailAddressHash: digest " + Arrays.toString(digest));
            String encodeToString = Base64.encodeToString(digest, 11);
            Log.m285d("LoyaltyCardPayProvider", "getEmailAddressHash: emailAddress " + str + " Hash: " + encodeToString);
            return encodeToString;
        } catch (Throwable e) {
            Log.m284c("LoyaltyCardPayProvider", e.getMessage(), e);
            return null;
        }
    }

    protected String prepareLoyaltyDataForServer(String str) {
        Log.m285d("LoyaltyCardPayProvider", "Entered prepareLoyaltyDataForServer");
        if (zg == null || str == null) {
            Log.m286e("LoyaltyCardPayProvider", "prepareLoyaltyDataForServer: Error: mPlccTAC = " + zg + "lData = " + str);
            return null;
        }
        String encodeToString;
        try {
            byte[] bytes = str.getBytes();
            long am = Utils.am(this.mContext);
            Log.m285d("LoyaltyCardPayProvider", "Network Time = " + am);
            PlccTAController plccTAController = zg;
            PlccTAController plccTAController2 = zg;
            encodeToString = Base64.encodeToString(plccTAController.utility_enc4Server_Transport(PlccTAController.CARD_BRAND_LOYALTY, bytes, am), 2);
        } catch (Throwable e) {
            Log.m284c("LoyaltyCardPayProvider", e.getMessage(), e);
            encodeToString = null;
        }
        if (encodeToString == null) {
            Log.m286e("LoyaltyCardPayProvider", "prepareLoyaltyDataForServer : encData is null");
            return null;
        }
        Log.m285d("LoyaltyCardPayProvider", "prepareLoyaltyDataForServer: encrypted data  = " + encodeToString);
        return encodeToString;
    }

    protected ProviderResponseData createToken(String str, ProviderRequestData providerRequestData, int i) {
        Log.m285d("LoyaltyCardPayProvider", "Entered createToken()");
        JsonElement ch = providerRequestData.ch();
        ProviderResponseData providerResponseData = new ProviderResponseData();
        providerResponseData.setErrorCode(-7);
        try {
            providerResponseData.setProviderTokenKey(new ProviderTokenKey(str));
            if (ch != null) {
                Log.m285d("LoyaltyCardPayProvider", "Response Data = " + ch.toString());
                JsonElement jsonElement = ch.get("encrypted");
                if (jsonElement != null) {
                    String asString = jsonElement.getAsString();
                    Log.m285d("LoyaltyCardPayProvider", "ac = " + asString.toString());
                    byte[] decode = Base64.decode(asString, 0);
                    PlccTAController plccTAController = zg;
                    PlccTAController plccTAController2 = zg;
                    decode = plccTAController.addCard(PlccTAController.CARD_BRAND_LOYALTY, decode);
                    String encodeToString = Base64.encodeToString(decode, 2);
                    Log.m285d("LoyaltyCardPayProvider", "tzEnc = " + encodeToString.toString());
                    ch.addProperty("encrypted", encodeToString);
                    String toJson = this.mGson.toJson(ch);
                    Log.m285d("LoyaltyCardPayProvider", "Response Data String = " + toJson);
                    Bundle bundle = new Bundle();
                    toJson = aT(toJson);
                    bundle.putString(ProvisionTokenResult.BUNDLE_KEY_RESPONSE_DATA_FILE_PATH, toJson);
                    bundle.putParcelable(ProvisionTokenResult.BUNDLE_KEY_RESPONSE_DATA_FD, aS(toJson));
                    if (providerRequestData.cg() == null || !providerRequestData.cg().containsKey("cardRefId")) {
                        Log.m287i("LoyaltyCardPayProvider", " CardRefId not part of request");
                    } else {
                        ExtractCardDetailResult extractLoyaltyCardDetail = zg.extractLoyaltyCardDetail(providerRequestData.cg().getString("cardRefId").getBytes(), decode);
                        if (extractLoyaltyCardDetail == null) {
                            Log.m287i("LoyaltyCardPayProvider", " ImgSessionKey not added part of Provision Response");
                        } else if (extractLoyaltyCardDetail.getExtraContent() != null && !extractLoyaltyCardDetail.getExtraContent().isEmpty()) {
                            bundle.putString(LoyaltyCardDetail.BUNDLE_KEY_AC_TOKEN_EXTRA, extractLoyaltyCardDetail.getExtraContent());
                            Log.m285d("LoyaltyCardPayProvider", "added extra as part of Provision Response:" + extractLoyaltyCardDetail.getExtraContent());
                        } else if (!(extractLoyaltyCardDetail.getImgSessionKey() == null || extractLoyaltyCardDetail.getImgSessionKey().isEmpty())) {
                            bundle.putString(LoyaltyCardDetail.BUNDLE_KEY_IMG_SESSION_KEY, extractLoyaltyCardDetail.getImgSessionKey());
                            Log.m285d("LoyaltyCardPayProvider", "added ImgSessionKey as part of Provision Response:" + extractLoyaltyCardDetail.getImgSessionKey());
                        }
                    }
                    providerResponseData.m1058e(bundle);
                    providerResponseData.setErrorCode(0);
                }
            }
        } catch (Throwable e) {
            Log.m284c("LoyaltyCardPayProvider", e.getMessage(), e);
            providerResponseData.setErrorCode(-2);
        }
        return providerResponseData;
    }

    private ParcelFileDescriptor aS(String str) {
        ParcelFileDescriptor parcelFileDescriptor = null;
        try {
            parcelFileDescriptor = ParcelFileDescriptor.open(new File(str), 805306368);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return parcelFileDescriptor;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected java.lang.String aT(java.lang.String r7) {
        /*
        r6 = this;
        r2 = 0;
        r0 = "LoyaltyCardPayProvider";
        r1 = "writeStringToFile";
        com.samsung.android.spayfw.p002b.Log.m285d(r0, r1);
        r0 = r6.eC();
        r1 = "LoyaltyCardPayProvider";
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "File = ";
        r3 = r3.append(r4);
        r3 = r3.append(r0);
        r3 = r3.toString();
        com.samsung.android.spayfw.p002b.Log.m285d(r1, r3);
        r3 = new java.io.BufferedWriter;	 Catch:{ Exception -> 0x005e }
        r1 = new java.io.FileWriter;	 Catch:{ Exception -> 0x005e }
        r1.<init>(r0);	 Catch:{ Exception -> 0x005e }
        r3.<init>(r1);	 Catch:{ Exception -> 0x005e }
        r1 = 0;
        r3.write(r7);	 Catch:{ Throwable -> 0x0050, all -> 0x006a }
        r0 = r0.getAbsolutePath();	 Catch:{ Throwable -> 0x0050, all -> 0x006a }
        if (r3 == 0) goto L_0x003d;
    L_0x0038:
        if (r2 == 0) goto L_0x004c;
    L_0x003a:
        r3.close();	 Catch:{ Throwable -> 0x003e }
    L_0x003d:
        return r0;
    L_0x003e:
        r2 = move-exception;
        r1.addSuppressed(r2);	 Catch:{ Exception -> 0x0043 }
        goto L_0x003d;
    L_0x0043:
        r1 = move-exception;
    L_0x0044:
        r1 = "LoyaltyCardPayProvider";
        r2 = "Exception when writing string to file";
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r2);
        goto L_0x003d;
    L_0x004c:
        r3.close();	 Catch:{ Exception -> 0x0043 }
        goto L_0x003d;
    L_0x0050:
        r0 = move-exception;
        throw r0;	 Catch:{ all -> 0x0052 }
    L_0x0052:
        r1 = move-exception;
        r5 = r1;
        r1 = r0;
        r0 = r5;
    L_0x0056:
        if (r3 == 0) goto L_0x005d;
    L_0x0058:
        if (r1 == 0) goto L_0x0066;
    L_0x005a:
        r3.close();	 Catch:{ Throwable -> 0x0061 }
    L_0x005d:
        throw r0;	 Catch:{ Exception -> 0x005e }
    L_0x005e:
        r0 = move-exception;
        r0 = r2;
        goto L_0x0044;
    L_0x0061:
        r3 = move-exception;
        r1.addSuppressed(r3);	 Catch:{ Exception -> 0x005e }
        goto L_0x005d;
    L_0x0066:
        r3.close();	 Catch:{ Exception -> 0x005e }
        goto L_0x005d;
    L_0x006a:
        r0 = move-exception;
        r1 = r2;
        goto L_0x0056;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.c.a.aT(java.lang.String):java.lang.String");
    }

    private File eC() {
        return new File(aU("LoyaltyBundle"), "serverResponse_" + System.currentTimeMillis() + String.valueOf(new Random().nextInt()));
    }

    public File aU(String str) {
        return this.mContext.getDir(str, 0);
    }

    public void setCardTzEncData(byte[] bArr) {
        Log.m285d("LoyaltyCardPayProvider", "Entered setCardTzEncData");
        this.zD = bArr;
    }

    public SelectCardResult selectCard() {
        Log.m285d("LoyaltyCardPayProvider", "Entered selectCard");
        if (zg == null) {
            Log.m286e("LoyaltyCardPayProvider", "TAController is null");
            return null;
        }
        SelectCardResult selectCardResult;
        try {
            selectCardResult = new SelectCardResult(PlccTAController.getInstance().getTAInfo().getTAId(), zg.getNonce(32));
        } catch (Throwable e) {
            Log.m284c("LoyaltyCardPayProvider", e.getMessage(), e);
            selectCardResult = null;
        }
        return selectCardResult;
    }

    protected void clearCard() {
        Log.m285d("LoyaltyCardPayProvider", "Entered clearCard");
        this.zD = null;
    }

    public boolean startMstPay(int i, byte[] bArr) {
        boolean z = false;
        Log.m285d("LoyaltyCardPayProvider", "Entered startMstPay");
        if (zg == null) {
            Log.m286e("LoyaltyCardPayProvider", "TAController is null");
        } else {
            try {
                z = zg.mstTransmit(this.zD, i, bArr);
            } catch (Throwable e) {
                Log.m284c("LoyaltyCardPayProvider", e.getMessage(), e);
            } catch (Throwable e2) {
                Log.m284c("LoyaltyCardPayProvider", e2.getMessage(), e2);
            }
        }
        return z;
    }

    protected void stopMstPay(boolean z) {
        Log.m287i("LoyaltyCardPayProvider", "stopMstPay: start ");
        if (zg == null) {
            Log.m286e("LoyaltyCardPayProvider", "TAController is null");
            return;
        }
        try {
            zg.clearMstData();
        } catch (Throwable e) {
            Log.m284c("LoyaltyCardPayProvider", e.getMessage(), e);
        }
        Log.m287i("LoyaltyCardPayProvider", "stopMstPay: end ");
    }

    protected String updateLoyaltyCard(JsonObject jsonObject) {
        Log.m285d("LoyaltyCardPayProvider", "Entered updateLoyaltyCard");
        String str = null;
        if (zg == null || jsonObject == null) {
            Log.m286e("LoyaltyCardPayProvider", "Error: mPlccTAC = " + zg + "; responseData = " + jsonObject);
        } else {
            try {
                Log.m285d("LoyaltyCardPayProvider", "updateLoyaltyCard: Response Data = " + jsonObject.toString());
                JsonElement jsonElement = jsonObject.get("encrypted");
                if (jsonElement != null) {
                    String asString = jsonElement.getAsString();
                    Log.m285d("LoyaltyCardPayProvider", "updateLoyaltyCard: ac = " + asString);
                    byte[] decode = Base64.decode(asString, 0);
                    PlccTAController plccTAController = zg;
                    PlccTAController plccTAController2 = zg;
                    asString = Base64.encodeToString(plccTAController.addCard(PlccTAController.CARD_BRAND_LOYALTY, decode), 2);
                    Log.m285d("LoyaltyCardPayProvider", "updateLoyaltyCard: tzEnc = " + asString);
                    jsonObject.addProperty("encrypted", asString);
                    str = this.mGson.toJson((JsonElement) jsonObject);
                    Log.m285d("LoyaltyCardPayProvider", "updateLoyaltyCard: Response Data String = " + str);
                }
            } catch (Throwable e) {
                Log.m284c("LoyaltyCardPayProvider", e.getMessage(), e);
            }
        }
        return str;
    }

    protected ExtractCardDetailResult extractLoyaltyCardDetail(ExtractLoyaltyCardDetailRequest extractLoyaltyCardDetailRequest) {
        Log.m285d("LoyaltyCardPayProvider", "Entered extractLoyaltyCardDetail");
        ExtractCardDetailResult extractCardDetailResult = null;
        if (zg == null || extractLoyaltyCardDetailRequest == null) {
            Log.m286e("LoyaltyCardPayProvider", "Error: mPlccTAC = " + zg + "request = " + extractLoyaltyCardDetailRequest);
        } else {
            try {
                extractCardDetailResult = zg.extractLoyaltyCardDetail(extractLoyaltyCardDetailRequest.getCardRefID(), extractLoyaltyCardDetailRequest.getTzEncData());
            } catch (Throwable e) {
                Log.m284c("LoyaltyCardPayProvider", e.getMessage(), e);
            } catch (Throwable e2) {
                Log.m284c("LoyaltyCardPayProvider", e2.getMessage(), e2);
            }
        }
        return extractCardDetailResult;
    }

    protected boolean authenticateTransaction(SecuredObject securedObject) {
        boolean z = false;
        Log.m285d("LoyaltyCardPayProvider", "Entered authenticateTransaction");
        if (zg == null) {
            Log.m286e("LoyaltyCardPayProvider", "TAController is null");
        } else {
            try {
                Log.m285d("LoyaltyCardPayProvider", "Calling Plcc TA Controller Authenticate Transaction");
                z = zg.authenticateTransaction(securedObject.getSecureObjectData());
            } catch (Throwable e) {
                Log.m284c("LoyaltyCardPayProvider", e.getMessage(), e);
            }
        }
        return z;
    }

    public boolean setServerCertificates(CertificateInfo[] certificateInfoArr) {
        byte[] bArr = null;
        Log.m285d("LoyaltyCardPayProvider", "Entered setServerCertificates");
        if (certificateInfoArr == null || certificateInfoArr.length == 0) {
            Log.m286e("LoyaltyCardPayProvider", "setServerCertificates : invalid input");
            return false;
        }
        byte[] bArr2 = null;
        byte[] bArr3 = null;
        for (CertificateInfo certificateInfo : certificateInfoArr) {
            String replace = certificateInfo.getContent().replace("-----BEGIN CERTIFICATE-----", BuildConfig.FLAVOR).replace("-----END CERTIFICATE-----", BuildConfig.FLAVOR);
            if (CertificateInfo.CERT_USAGE_ENC.equals(certificateInfo.getUsage())) {
                bArr3 = Base64.decode(replace, 0);
            } else if (CertificateInfo.CERT_USAGE_VER.equals(certificateInfo.getUsage())) {
                bArr2 = Base64.decode(replace, 0);
            } else if (CertificateInfo.CERT_USAGE_CA.equals(certificateInfo.getUsage())) {
                bArr = Base64.decode(replace, 0);
            }
        }
        if (bArr3 == null || bArr2 == null || bArr == null) {
            return false;
        }
        PlccTAController plccTAController = zg;
        PlccTAController plccTAController2 = zg;
        plccTAController.setPlccServerCerts(PlccTAController.CARD_BRAND_LOYALTY, bArr2, bArr3, bArr);
        return true;
    }

    protected CertificateInfo[] getDeviceCertificates() {
        CertificateInfo[] certificateInfoArr = new CertificateInfo[3];
        Log.m285d("LoyaltyCardPayProvider", "getDeviceCertificates");
        try {
            PlccTAController plccTAController = zg;
            PlccTAController plccTAController2 = zg;
            TACerts allCerts = plccTAController.getAllCerts(PlccTAController.CARD_BRAND_LOYALTY);
            if (allCerts == null || allCerts.encryptcert == null || allCerts.encryptcert.length <= 0 || allCerts.drk == null || allCerts.drk.length <= 0 || allCerts.signcert == null || allCerts.signcert.length <= 0) {
                Log.m286e("LoyaltyCardPayProvider", "getAllCerts failed for Loyalty card");
                return null;
            }
            HashMap hashMap = new HashMap();
            hashMap.put(PlccConstants.CERT_ENC, allCerts.encryptcert);
            hashMap.put(PlccConstants.CERT_SIGN, allCerts.signcert);
            hashMap.put(PlccConstants.CERT_CA, allCerts.drk);
            String convertToPem = Util.convertToPem((byte[]) hashMap.get(PlccConstants.CERT_ENC));
            String convertToPem2 = Util.convertToPem((byte[]) hashMap.get(PlccConstants.CERT_SIGN));
            String convertToPem3 = Util.convertToPem((byte[]) hashMap.get(PlccConstants.CERT_CA));
            if (convertToPem == null || convertToPem2 == null || convertToPem3 == null) {
                Log.m286e("LoyaltyCardPayProvider", "getDeviceCertificates failed");
                return null;
            }
            CertificateInfo certificateInfo = new CertificateInfo();
            certificateInfo.setAlias(CertificateInfo.CERT_USAGE_ENC);
            certificateInfo.setContent(convertToPem);
            certificateInfo.setUsage(CertificateInfo.CERT_USAGE_ENC);
            certificateInfoArr[0] = certificateInfo;
            certificateInfo = new CertificateInfo();
            certificateInfo.setAlias(CertificateInfo.CERT_USAGE_SIG);
            certificateInfo.setContent(convertToPem2);
            certificateInfo.setUsage(CertificateInfo.CERT_USAGE_SIG);
            certificateInfoArr[1] = certificateInfo;
            certificateInfo = new CertificateInfo();
            certificateInfo.setAlias(CertificateInfo.CERT_USAGE_CA);
            certificateInfo.setContent(convertToPem3);
            certificateInfo.setUsage(CertificateInfo.CERT_USAGE_CA);
            certificateInfoArr[2] = certificateInfo;
            Log.m285d("LoyaltyCardPayProvider", "getDeviceCertificates: success");
            return certificateInfoArr;
        } catch (Throwable e) {
            Log.m284c("LoyaltyCardPayProvider", e.getMessage(), e);
        }
    }

    protected void loadTA() {
        zg.loadTA();
        Log.m287i("LoyaltyCardPayProvider", "load real TA");
    }

    protected void unloadTA() {
        zg.unloadTA();
        Log.m287i("LoyaltyCardPayProvider", "unload real TA");
    }

    public boolean getPayReadyState() {
        return true;
    }

    protected boolean prepareMstPay() {
        return true;
    }

    protected void init() {
    }

    protected ProviderRequestData getProvisionRequestData(ProvisionTokenInfo provisionTokenInfo) {
        return null;
    }

    public void delete() {
    }

    protected void interruptMstPay() {
    }

    public boolean prepareNfcPay() {
        return false;
    }

    public byte[] handleApdu(byte[] bArr, Bundle bundle) {
        return new byte[0];
    }

    protected Bundle stopNfcPay(int i) {
        return null;
    }

    protected ProviderRequestData getReplenishmentRequestData() {
        return null;
    }

    protected ProviderResponseData replenishToken(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }

    protected ProviderResponseData updateTokenStatus(JsonObject jsonObject, TokenStatus tokenStatus) {
        return null;
    }

    protected int getTransactionData(Bundle bundle, TransactionResponse transactionResponse) {
        return 0;
    }

    protected TransactionDetails processTransactionData(Object obj) {
        return null;
    }

    protected byte[] generateInAppPaymentPayload(InAppDetailedTransactionInfo inAppDetailedTransactionInfo) {
        return null;
    }

    protected ProviderRequestData getDeleteRequestData(Bundle bundle) {
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        if (!(bundle == null || bundle.getString(PaymentFramework.EXTRA_DELETE_CARD_DATA) == null)) {
            String string = bundle.getString(PaymentFramework.EXTRA_DELETE_CARD_DATA);
            providerRequestData.m822a((JsonObject) new Gson().fromJson(string, JsonObject.class));
            Log.m285d("LoyaltyCardPayProvider", "getDeleteRequestData: " + string);
        }
        return providerRequestData;
    }

    protected void updateTokenMetaData(JsonObject jsonObject, Token token) {
        Log.m285d("LoyaltyCardPayProvider", "updateTokenMetaData");
        if (jsonObject != null) {
            try {
                Log.m285d("LoyaltyCardPayProvider", "updateTokenMetaData:Response Data = " + jsonObject.toString());
                JsonElement jsonElement = jsonObject.get("encrypted");
                if (jsonElement != null) {
                    String asString = jsonElement.getAsString();
                    Log.m285d("LoyaltyCardPayProvider", "ac = " + asString.toString());
                    byte[] decode = Base64.decode(asString, 0);
                    PlccTAController plccTAController = zg;
                    PlccTAController plccTAController2 = zg;
                    decode = plccTAController.addCard(PlccTAController.CARD_BRAND_LOYALTY, decode);
                    String encodeToString = Base64.encodeToString(decode, 2);
                    Log.m285d("LoyaltyCardPayProvider", "tzEnc = " + encodeToString.toString());
                    jsonObject.addProperty("encrypted", encodeToString);
                    encodeToString = this.mGson.toJson((JsonElement) jsonObject);
                    Log.m285d("LoyaltyCardPayProvider", "Response Data String = " + encodeToString);
                    Bundle bundle = new Bundle();
                    encodeToString = aT(encodeToString);
                    bundle.putString(TokenMetaData.BUNDLE_KEY_EXTRA_META_DATA_FILE_PATH, encodeToString);
                    bundle.putParcelable(TokenMetaData.BUNDLE_KEY_EXTRA_META_DATA_FD, aS(encodeToString));
                    if (token == null || token.getMetadata() == null || token.getMetadata().getCardRefernceId() == null) {
                        Log.m287i("LoyaltyCardPayProvider", " CardRefId not part of request");
                        return;
                    }
                    ExtractCardDetailResult extractLoyaltyCardDetail = zg.extractLoyaltyCardDetail(token.getMetadata().getCardRefernceId().getBytes(), decode);
                    if (extractLoyaltyCardDetail == null) {
                        Log.m287i("LoyaltyCardPayProvider", " ImgSessionKey not added part of Provision Response");
                    } else if (extractLoyaltyCardDetail.getExtraContent() != null && !extractLoyaltyCardDetail.getExtraContent().isEmpty()) {
                        bundle.putString(LoyaltyCardDetail.BUNDLE_KEY_AC_TOKEN_EXTRA, extractLoyaltyCardDetail.getExtraContent());
                        Log.m285d("LoyaltyCardPayProvider", "added extra as part of Provision Response:" + extractLoyaltyCardDetail.getExtraContent());
                    } else if (!(extractLoyaltyCardDetail.getImgSessionKey() == null || extractLoyaltyCardDetail.getImgSessionKey().isEmpty())) {
                        bundle.putString(LoyaltyCardDetail.BUNDLE_KEY_IMG_SESSION_KEY, extractLoyaltyCardDetail.getImgSessionKey());
                        Log.m285d("LoyaltyCardPayProvider", "added ImgSessionKey as part of Provision Response:" + extractLoyaltyCardDetail.getImgSessionKey());
                    }
                    token.getMetadata().setExtraMetaData(bundle);
                }
            } catch (Throwable e) {
                Log.m284c("LoyaltyCardPayProvider", e.getMessage(), e);
            }
        }
    }
}
