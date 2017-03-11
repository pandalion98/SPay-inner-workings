package com.samsung.android.spayfw.payprovider.visa;

import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.ActivationData;
import com.samsung.android.spayfw.appinterface.BillingInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardPanInfo;
import com.samsung.android.spayfw.appinterface.EnrollCardReferenceInfo;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.core.PaymentFrameworkRequester;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.ProviderRequestData;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.TokenReplenishAlarm;
import com.samsung.android.spayfw.payprovider.visa.db.VisaTokenDetails;
import com.samsung.android.spayfw.payprovider.visa.db.VisaTokenDetailsDao;
import com.samsung.android.spayfw.payprovider.visa.db.VisaTokenDetailsDao.VisaTokenGroup.TokenColumn;
import com.samsung.android.spayfw.payprovider.visa.inapp.InAppPayment;
import com.samsung.android.spayfw.utils.DBUtils;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.android.visasdk.facade.VisaPaymentSDK;
import com.samsung.android.visasdk.facade.VisaPaymentSDKImpl;
import com.samsung.android.visasdk.facade.data.ApduResponse;
import com.samsung.android.visasdk.facade.data.CvmMode;
import com.samsung.android.visasdk.facade.data.EnrollPanRequest;
import com.samsung.android.visasdk.facade.data.PaymentDataRequest;
import com.samsung.android.visasdk.facade.data.ProvisionResponse;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.data.TokenMetaData;
import com.samsung.android.visasdk.facade.data.TransactionError;
import com.samsung.android.visasdk.facade.data.TransactionStatus;
import com.samsung.android.visasdk.facade.data.UpdateReason;
import com.samsung.android.visasdk.facade.data.VerifyingEntity;
import com.samsung.android.visasdk.facade.data.VerifyingType;
import com.samsung.android.visasdk.facade.exception.TokenKeyInvalidException;
import com.samsung.android.visasdk.paywave.model.TokenInfo;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/* renamed from: com.samsung.android.spayfw.payprovider.visa.c */
public class VisaPayProviderSdk {
    private static Map<String, VisaTokenDetails> zO;
    private Context mContext;
    private VisaTokenDetailsDao zG;
    private VisaPaymentSDK zN;

    static {
        zO = null;
    }

    public VisaPayProviderSdk(Context context, VisaTokenDetailsDao visaTokenDetailsDao) {
        this.mContext = context;
        this.zG = visaTokenDetailsDao;
        VisaPaymentSDKImpl.initialize(context, DBUtils.getDbPassword());
        eE();
        init();
    }

    private void eE() {
        try {
            if (this.zN == null) {
                this.zN = VisaPaymentSDKImpl.getInstance(VisaPayProviderSdk.eG());
            }
        } catch (Throwable e) {
            Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
        }
    }

    String m1099f(ProviderTokenKey providerTokenKey) {
        eE();
        if (providerTokenKey == null || providerTokenKey.cn() == null) {
            Log.m286e("VisaPayProviderSdk", "getTokenStatus :  token key is null");
            return null;
        }
        return this.zN.getTokenStatus(new TokenKey(providerTokenKey.cm()));
    }

    public ProviderRequestData m1097b(EnrollCardInfo enrollCardInfo, BillingInfo billingInfo) {
        eE();
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        Log.m285d("VisaPayProviderSdk", "getEnrollRequestData: enter ");
        if (enrollCardInfo == null || this.zN == null) {
            Log.m286e("VisaPayProviderSdk", "getEnrollRequestData: input is invalid ");
            providerRequestData.setErrorCode(-4);
            return providerRequestData;
        }
        Bundle extraEnrollData;
        Log.m288m("VisaPayProviderSdk", "EnrollCardInfo:" + enrollCardInfo.toString());
        this.zN.getEnrollPANTemplate();
        JsonObject jsonObject = new JsonObject();
        Object bytes;
        Object obj;
        EnrollPanRequest constructEnrollRequest;
        if (enrollCardInfo instanceof EnrollCardPanInfo) {
            try {
                EnrollCardPanInfo enrollCardPanInfo = (EnrollCardPanInfo) enrollCardInfo;
                JsonObject jsonObject2 = new JsonObject();
                JsonElement jsonObject3 = new JsonObject();
                JsonElement jsonObject4 = new JsonObject();
                if (billingInfo != null) {
                    Log.m288m("VisaPayProviderSdk", "BillingInfo:" + billingInfo.toString());
                    if (!(billingInfo.getStreet1() == null || billingInfo.getStreet1().equals(BuildConfig.FLAVOR))) {
                        Log.m285d("VisaPayProviderSdk", "street1:" + billingInfo.getStreet1());
                        jsonObject3.addProperty(ActivationData.CARD_INFO_BILLING_LINE1, billingInfo.getStreet1());
                    }
                    if (!(billingInfo.getStreet2() == null || billingInfo.getStreet2().equals(BuildConfig.FLAVOR))) {
                        Log.m285d("VisaPayProviderSdk", "street2:" + billingInfo.getStreet2());
                        jsonObject3.addProperty("line2", billingInfo.getStreet2());
                    }
                    if (!(billingInfo.getCity() == null || billingInfo.getCity().equals(BuildConfig.FLAVOR))) {
                        Log.m285d("VisaPayProviderSdk", "city:" + billingInfo.getCity());
                        jsonObject3.addProperty("city", billingInfo.getCity());
                    }
                    if (!(billingInfo.getState() == null || billingInfo.getState().equals(BuildConfig.FLAVOR))) {
                        Log.m285d("VisaPayProviderSdk", "state:" + billingInfo.getState());
                        jsonObject3.addProperty("state", billingInfo.getState());
                    }
                    if (!(billingInfo.getCountry() == null || billingInfo.getCountry().equals(BuildConfig.FLAVOR))) {
                        Log.m285d("VisaPayProviderSdk", "country:" + billingInfo.getCountry());
                        jsonObject3.addProperty("country", billingInfo.getCountry());
                    }
                    if (!(billingInfo.getZip() == null || billingInfo.getZip().equals(BuildConfig.FLAVOR))) {
                        Log.m285d("VisaPayProviderSdk", "zip:" + billingInfo.getZip());
                        jsonObject3.addProperty("postalCode", billingInfo.getZip());
                    }
                    jsonObject2.add("billingAddress", jsonObject3);
                }
                if (!(enrollCardPanInfo.getExpMonth() == null || enrollCardPanInfo.getExpMonth().isEmpty())) {
                    jsonObject4.addProperty(ActivationData.EXPIRATION_DATE_MONTH, enrollCardPanInfo.getExpMonth());
                }
                if (!(enrollCardPanInfo.getExpYear() == null || enrollCardPanInfo.getExpYear().isEmpty())) {
                    jsonObject4.addProperty(ActivationData.EXPIRATION_DATE_YEAR, "20" + enrollCardPanInfo.getExpYear());
                }
                jsonObject2.addProperty("accountNumber", enrollCardPanInfo.getPAN());
                jsonObject2.addProperty(ActivationData.CARD_INFO_CVV, enrollCardPanInfo.getCVV());
                jsonObject2.addProperty("name", enrollCardPanInfo.getName());
                if (jsonObject4.has(ActivationData.EXPIRATION_DATE_MONTH) && jsonObject4.has(ActivationData.EXPIRATION_DATE_YEAR)) {
                    jsonObject2.add("expirationDate", jsonObject4);
                }
                Log.m288m("VisaPayProviderSdk", "paymentInstrument: " + jsonObject2.toString());
                bytes = jsonObject2.toString().getBytes("utf-8");
                obj = new byte[(bytes.length + 1)];
                obj[0] = (byte) 21;
                System.arraycopy(bytes, 0, obj, 1, bytes.length);
                constructEnrollRequest = this.zN.constructEnrollRequest(obj);
                if (constructEnrollRequest == null) {
                    Log.m286e("VisaPayProviderSdk", "constructEnrollRequest null");
                    providerRequestData.setErrorCode(-2);
                } else {
                    jsonObject.addProperty("encPaymentInstrument", constructEnrollRequest.getEncPaymentInstrument());
                    Log.m285d("VisaPayProviderSdk", "getEnrollRequestData: " + constructEnrollRequest.getEncPaymentInstrument());
                    providerRequestData.m822a(jsonObject);
                }
            } catch (Throwable e) {
                providerRequestData.setErrorCode(-2);
                Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
            } catch (Exception e2) {
                providerRequestData.setErrorCode(-2);
                e2.printStackTrace();
            }
        } else if (enrollCardInfo instanceof EnrollCardReferenceInfo) {
            EnrollCardReferenceInfo enrollCardReferenceInfo = (EnrollCardReferenceInfo) enrollCardInfo;
            if (enrollCardReferenceInfo.getReferenceType() == null || enrollCardReferenceInfo.getExtraEnrollData() == null) {
                Log.m286e("VisaPayProviderSdk", "enrollCardReferenceInfo reference type or extra data is null");
            } else {
                Log.m285d("VisaPayProviderSdk", "Card Type : " + enrollCardReferenceInfo.getReferenceType());
                if (enrollCardReferenceInfo.getReferenceType().equals(EnrollCardReferenceInfo.CARD_REF_TYPE_APP2APP)) {
                    jsonObject.addProperty("encPaymentInstrument", new String(enrollCardReferenceInfo.getExtraEnrollData().getByteArray(EnrollCardReferenceInfo.ENROLL_PAYLOAD)));
                    providerRequestData.m822a(jsonObject);
                } else if (enrollCardReferenceInfo.getReferenceType().equals(EnrollCardReferenceInfo.CARD_REF_TYPE_ID)) {
                    Log.m285d("VisaPayProviderSdk", "Card Import enrollment");
                    try {
                        extraEnrollData = ((EnrollCardReferenceInfo) enrollCardInfo).getExtraEnrollData();
                        if (extraEnrollData != null) {
                            String string = extraEnrollData.getString(EnrollCardReferenceInfo.CARD_INFO_CVV);
                            String string2 = extraEnrollData.getString(EnrollCardReferenceInfo.CARD_INFO_EXP_MM);
                            String string3 = extraEnrollData.getString(EnrollCardReferenceInfo.CARD_INFO_EXP_YY);
                            String string4 = extraEnrollData.getString(EnrollCardReferenceInfo.CARD_INFO_ZIP);
                            JsonObject jsonObject5 = new JsonObject();
                            JsonElement jsonObject6 = new JsonObject();
                            JsonElement jsonObject7 = new JsonObject();
                            if (!(string4 == null || string4.isEmpty())) {
                                Log.m288m("VisaPayProviderSdk", "BillingInfo:" + string4);
                                jsonObject6.addProperty("postalCode", string4);
                                jsonObject5.add("billingAddress", jsonObject6);
                            }
                            if (!(string2 == null || string2.isEmpty())) {
                                jsonObject7.addProperty(ActivationData.EXPIRATION_DATE_MONTH, string2);
                            }
                            if (!(string3 == null || string3.isEmpty())) {
                                jsonObject7.addProperty(ActivationData.EXPIRATION_DATE_YEAR, "20" + string3);
                            }
                            jsonObject5.addProperty(ActivationData.CARD_INFO_CVV, string);
                            if (jsonObject7.has(ActivationData.EXPIRATION_DATE_MONTH) && jsonObject7.has(ActivationData.EXPIRATION_DATE_YEAR)) {
                                jsonObject5.add("expirationDate", jsonObject7);
                            }
                            Log.m288m("VisaPayProviderSdk", "paymentInstrument: " + jsonObject5.toString());
                            bytes = jsonObject5.toString().getBytes("utf-8");
                            obj = new byte[(bytes.length + 1)];
                            obj[0] = (byte) 21;
                            System.arraycopy(bytes, 0, obj, 1, bytes.length);
                            constructEnrollRequest = this.zN.constructEnrollRequest(obj);
                            if (constructEnrollRequest == null) {
                                Log.m286e("VisaPayProviderSdk", "constructEnrollRequest null");
                                providerRequestData.setErrorCode(-2);
                            } else {
                                jsonObject.addProperty("encPaymentInstrument", constructEnrollRequest.getEncPaymentInstrument());
                                Log.m285d("VisaPayProviderSdk", "getEnrollRequestData: " + constructEnrollRequest.getEncPaymentInstrument());
                                providerRequestData.m822a(jsonObject);
                            }
                        } else {
                            providerRequestData.setErrorCode(-2);
                        }
                    } catch (Throwable e3) {
                        providerRequestData.setErrorCode(-2);
                        Log.m284c("VisaPayProviderSdk", e3.getMessage(), e3);
                    } catch (Exception e22) {
                        providerRequestData.setErrorCode(-2);
                        e22.printStackTrace();
                    }
                }
            }
        }
        extraEnrollData = new Bundle();
        if (enrollCardInfo.getUserEmail() != null) {
            extraEnrollData.putString("emailHash", getEmailAddressHash(enrollCardInfo.getUserEmail()));
        }
        providerRequestData.m823e(extraEnrollData);
        return providerRequestData;
    }

    public void selectCard(TokenKey tokenKey) {
        eE();
        if (this.zN == null) {
            Log.m286e("VisaPayProviderSdk", "selectCard:Sdk instance is null ");
            return;
        }
        try {
            this.zN.selectCard(tokenKey);
        } catch (Throwable e) {
            Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public ProviderResponseData m1090a(String str, JsonObject jsonObject) {
        eE();
        ProviderResponseData providerResponseData = new ProviderResponseData();
        providerResponseData.setErrorCode(0);
        if (this.zN == null || str == null || this.zG == null) {
            Log.m286e("VisaPayProviderSdk", "setProvisionResponse invalid input");
            providerResponseData.setErrorCode(-4);
            return providerResponseData;
        }
        ProvisionResponse provisionResponse = new ProvisionResponse();
        Gson create = new GsonBuilder().disableHtmlEscaping().create();
        try {
            Object obj = (ProvisionResponse) create.fromJson((JsonElement) jsonObject, ProvisionResponse.class);
            Log.m285d("VisaPayProviderSdk", "ProvisionToken: " + create.toJson(obj));
            TokenKey storeProvisionedToken = this.zN.storeProvisionedToken(obj, null);
            if (storeProvisionedToken != null) {
                Log.m285d("VisaPayProviderSdk", "setProvisionResponse : trTokenId: " + str + " tokenKey: " + storeProvisionedToken.getTokenId());
                ProviderTokenKey providerTokenKey = new ProviderTokenKey(storeProvisionedToken.getTokenId());
                providerResponseData.setProviderTokenKey(providerTokenKey);
                Object constructProvisionAck = this.zN.constructProvisionAck(storeProvisionedToken);
                if (constructProvisionAck != null) {
                    Gson gson = new Gson();
                    String toJson = gson.toJson(constructProvisionAck);
                    Log.m285d("VisaPayProviderSdk", "constructProvisionAck : " + toJson);
                    providerResponseData.m1057b((JsonObject) gson.fromJson(toJson, JsonObject.class));
                } else {
                    providerResponseData.setErrorCode(-2);
                    Log.m290w("VisaPayProviderSdk", "constructProvisionAck returns null");
                }
                if (!(obj == null || obj.getTokenInfo().getHceData() == null || obj.getTokenInfo().getHceData().getDynParams() == null)) {
                    int intValue = obj.getTokenInfo().getHceData().getDynParams().getMaxPmts().intValue();
                    long longValue = obj.getTokenInfo().getHceData().getDynParams().getKeyExpTS().longValue();
                    Log.m285d("VisaPayProviderSdk", "maxPmts: " + intValue);
                    VisaTokenDetails visaTokenDetails = new VisaTokenDetails(str, providerTokenKey.cn(), intValue, m1084Q(intValue), m1085w(longValue), m1086x(longValue), Utils.am(this.mContext));
                    this.zG.m1118c(visaTokenDetails);
                    m1093a(visaTokenDetails);
                }
            } else {
                Log.m286e("VisaPayProviderSdk", "storeProvisionedToken returns null");
                providerResponseData.setErrorCode(-2);
            }
        } catch (Throwable e) {
            providerResponseData.setErrorCode(-2);
            Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
        } catch (Exception e2) {
            providerResponseData.setErrorCode(-2);
            e2.printStackTrace();
        }
        return providerResponseData;
    }

    public boolean prepareMstPay() {
        eE();
        boolean z = false;
        if (this.zN == null) {
            Log.m286e("VisaPayProviderSdk", "prepareMstPay:Sdk instance is null");
        } else {
            try {
                z = this.zN.prepareMstData();
            } catch (Exception e) {
                Log.m286e("VisaPayProviderSdk", "prepareMstData returns exception");
                e.printStackTrace();
            }
        }
        return z;
    }

    public ProviderRequestData m1089a(TokenKey tokenKey) {
        eE();
        ProviderRequestData providerRequestData = new ProviderRequestData();
        providerRequestData.setErrorCode(0);
        if (this.zN == null || tokenKey == null) {
            if (this.zN == null) {
                Log.m286e("VisaPayProviderSdk", "getReplenishmentRequestData:Sdk instance is null");
            } else {
                Log.m286e("VisaPayProviderSdk", "getReplenishmentRequestData:tokenId is null");
            }
            providerRequestData.setErrorCode(-4);
            return providerRequestData;
        }
        Log.m285d("VisaPayProviderSdk", "getReplenishmentRequestData:tokenId: " + tokenKey);
        try {
            Object constructReplenishRequest = this.zN.constructReplenishRequest(tokenKey);
            if (constructReplenishRequest != null) {
                JsonObject jsonObject = new JsonObject();
                Gson gson = new Gson();
                String toJson = gson.toJson(constructReplenishRequest);
                JsonObject jsonObject2 = (JsonObject) gson.fromJson(toJson, JsonObject.class);
                Log.m285d("VisaPayProviderSdk", "getReplenishmentRequestData: jsonData: " + toJson);
                providerRequestData.m822a(jsonObject2);
                Log.m285d("VisaPayProviderSdk", "getReplenishmentRequestData:data: " + providerRequestData.ch());
                providerRequestData.setErrorCode(0);
            }
        } catch (Throwable e) {
            Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
            Log.m286e("VisaPayProviderSdk", "constructReplenishRequest returns exception");
            providerRequestData.setErrorCode(-2);
        } catch (Exception e2) {
            e2.printStackTrace();
            providerRequestData.setErrorCode(-2);
        }
        return providerRequestData;
    }

    public byte[] processApdu(byte[] bArr, Bundle bundle) {
        eE();
        if (this.zN == null) {
            Log.m286e("VisaPayProviderSdk", "processApdu:Sdk instance is null");
            return null;
        }
        Log.m285d("VisaPayProviderSdk", "processApdu = " + new String(bArr));
        boolean isCvmVerified = this.zN.isCvmVerified();
        long currentTimeMillis = System.currentTimeMillis();
        Log.m285d("VisaPayProviderSdk", "visa start processApdu measuretime=" + currentTimeMillis);
        try {
            ApduResponse processCommandApdu = this.zN.processCommandApdu(bArr, bundle, isCvmVerified);
            long currentTimeMillis2 = System.currentTimeMillis();
            Log.m285d("VisaPayProviderSdk", "visa end processApdu measuretime=" + currentTimeMillis2);
            Log.m285d("VisaPayProviderSdk", "visa total processApdu measuretime=" + (currentTimeMillis2 - currentTimeMillis));
            Log.m285d("VisaPayProviderSdk", "apduResponse = " + new String(processCommandApdu.getApduData()));
            return processCommandApdu.getApduData();
        } catch (Throwable e) {
            Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
            return new byte[]{(byte) 105, (byte) -123};
        }
    }

    public ProviderResponseData m1091a(String str, ProviderTokenKey providerTokenKey, JsonObject jsonObject, TokenStatus tokenStatus, PaymentFrameworkRequester paymentFrameworkRequester) {
        eE();
        ProviderResponseData providerResponseData = new ProviderResponseData();
        providerResponseData.setErrorCode(0);
        if (this.zN == null || str == null || providerTokenKey == null) {
            Log.m286e("VisaPayProviderSdk", "replenishToken:Sdk instance or tokenId is null");
            providerResponseData.setErrorCode(-4);
            return providerResponseData;
        } else if (jsonObject != null) {
            ProvisionResponse provisionResponse = new ProvisionResponse();
            Gson gson = new Gson();
            try {
                Object obj = (ProvisionResponse) gson.fromJson((JsonElement) jsonObject, ProvisionResponse.class);
                Log.m285d("VisaPayProviderSdk", "replenishToken:ProvisionResponse " + gson.toJson(obj));
                if (obj == null || obj.getTokenInfo() == null) {
                    Log.m286e("VisaPayProviderSdk", "replenishToken:incoming data is invalid");
                    providerResponseData.setErrorCode(-2);
                    return providerResponseData;
                }
                obj = obj.getTokenInfo();
                if (obj == null || obj.getHceData() == null || obj.getHceData().getDynParams() == null || obj.getHceData().getDynParams().getEncKeyInfo() == null) {
                    Log.m286e("VisaPayProviderSdk", "replenishToken:incoming data(tokenInfo) is invalid");
                    providerResponseData.setErrorCode(-2);
                    return providerResponseData;
                }
                Log.m285d("VisaPayProviderSdk", "replenishToken:TokenInfo " + gson.toJson(obj));
                int intValue = obj.getHceData().getDynParams().getMaxPmts().intValue();
                long longValue = obj.getHceData().getDynParams().getKeyExpTS().longValue();
                Log.m285d("VisaPayProviderSdk", "maxPmts " + intValue);
                Log.m285d("VisaPayProviderSdk", "keyExpTs " + longValue);
                TokenKey tokenKey = new TokenKey(providerTokenKey.cm());
                if (this.zN.processReplenishmentResponse(tokenKey, obj)) {
                    Log.m287i("VisaPayProviderSdk", "constructReplenishAcknowledgementRequest: ");
                    obj = this.zN.constructReplenishAcknowledgementRequest(tokenKey);
                    if (obj != null) {
                        Gson gson2 = new Gson();
                        String toJson = gson2.toJson(obj);
                        Log.m285d("VisaPayProviderSdk", "constructReplenishAcknowledgementRequest : " + toJson);
                        providerResponseData.m1057b((JsonObject) gson2.fromJson(toJson, JsonObject.class));
                        longValue = m1085w(longValue);
                        int Q = m1084Q(intValue);
                        long x = m1086x(longValue);
                        VisaTokenDetails aZ = this.zG.aZ(str);
                        if (aZ == null) {
                            Log.m286e("VisaPayProviderSdk", "constructReplenishAcknowledgementRequest returns null");
                            providerResponseData.setErrorCode(-5);
                            return providerResponseData;
                        }
                        aZ.setMaxPmts(intValue);
                        aZ.m1121R(Q);
                        aZ.m1122y(longValue);
                        aZ.m1123z(x);
                        this.zG.m1119d(aZ);
                        m1093a(aZ);
                        Log.m285d("VisaPayProviderSdk", "updateReplenishStatus : maxPmts:  " + intValue + " replenishPmts: " + Q + "keyExpTs: " + longValue + " replenishExpTs: " + x);
                        VisaTokenDetails aZ2 = this.zG.aZ(str);
                        if (aZ2 != null) {
                            Log.m285d("VisaPayProviderSdk", "updateReplenishStatus from db : " + aZ2.dump());
                        }
                        m1098b(str, providerTokenKey, paymentFrameworkRequester);
                        providerTokenKey.setTrTokenId(str);
                        paymentFrameworkRequester.m312b(providerTokenKey);
                    } else {
                        Log.m286e("VisaPayProviderSdk", "constructReplenishAcknowledgementRequest returns null");
                        providerResponseData.setErrorCode(-5);
                    }
                    return providerResponseData;
                }
                Log.m286e("VisaPayProviderSdk", "processReplenishmentResponse: ret false");
                providerResponseData.setErrorCode(-2);
                return providerResponseData;
            } catch (Throwable e) {
                Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
                Log.m286e("VisaPayProviderSdk", "replenishToken = JsonSyntaxException");
                providerResponseData.setErrorCode(-4);
                return providerResponseData;
            } catch (Throwable e2) {
                Log.m284c("VisaPayProviderSdk", e2.getMessage(), e2);
                Log.m286e("VisaPayProviderSdk", "replenishToken = TokenInvalidException");
                providerResponseData.setErrorCode(-5);
                return providerResponseData;
            } catch (Exception e3) {
                e3.printStackTrace();
                providerResponseData.setErrorCode(-2);
            }
        } else {
            Log.m286e("VisaPayProviderSdk", "replenishToken:data is null");
            providerResponseData.setErrorCode(-4);
            return providerResponseData;
        }
    }

    public int m1087a(ProviderTokenKey providerTokenKey, JsonObject jsonObject, TokenStatus tokenStatus, PaymentFrameworkRequester paymentFrameworkRequester) {
        int i = -5;
        eE();
        if (this.zN == null) {
            Log.m286e("VisaPayProviderSdk", "updateTokenStatus:Sdk instance is null");
            return -4;
        } else if (providerTokenKey == null || providerTokenKey.cn() == null) {
            Log.m286e("VisaPayProviderSdk", "updateTokenStatus:provider token key is null");
            return -4;
        } else {
            String str = null;
            if (tokenStatus != null) {
                str = tokenStatus.getCode();
            }
            if (str == null) {
                Log.m286e("VisaPayProviderSdk", "updateTokenStatus:status code is null");
                return -4;
            }
            String cn = providerTokenKey.cn();
            TokenKey tokenKey = new TokenKey(providerTokenKey.cm());
            VisaTokenDetails ba = this.zG.ba(cn);
            if (ba == null) {
                Log.m287i("VisaPayProviderSdk", "updateTokenStatus:record is null for providerTokenKey: " + cn);
                return -4;
            }
            providerTokenKey.setTrTokenId(ba.getTrTokenId());
            try {
                String tokenStatus2 = this.zN.getTokenStatus(tokenKey);
                Log.m285d("VisaPayProviderSdk", "VisaTokenStatus: " + tokenStatus2);
                UpdateReason updateReason = new UpdateReason();
                if (str.equals(TokenStatus.SUSPENDED)) {
                    this.zN.suspendToken(tokenKey, updateReason);
                    TokenReplenishAlarm.m1071a(this.mContext, providerTokenKey);
                    ba.m1122y(-1);
                    ba.m1123z(-1);
                    ba.setMaxPmts(-1);
                    ba.m1121R(-1);
                    this.zG.m1119d(ba);
                    m1093a(ba);
                    Log.m285d("VisaPayProviderSdk", "updateTokenStatus:token suspended so replenish values set to zero ");
                } else if (str.equals(TokenStatus.ACTIVE)) {
                    this.zN.resumeToken(tokenKey);
                    if (paymentFrameworkRequester != null && TokenStatus.SUSPENDED.equals(tokenStatus2)) {
                        Log.m285d("VisaPayProviderSdk", "token resumed so triggering replenish: trTokenId: " + ba.getTrTokenId() + " providerKey: " + cn);
                        paymentFrameworkRequester.m311a(providerTokenKey);
                    }
                    if (paymentFrameworkRequester != null && "INACTIVE".equals(tokenStatus2)) {
                        Log.m285d("VisaPayProviderSdk", "token resumed so setup replenish alarm: trTokenId: " + ba.getTrTokenId() + " providerKey: " + cn);
                        m1098b(ba.getTrTokenId(), providerTokenKey, paymentFrameworkRequester);
                    }
                } else if (str.equals(TokenStatus.DISPOSED)) {
                    Log.m285d("VisaPayProviderSdk", "updateTokenStatus:Sdk: deleting token from sdk:tokenId " + ba.getTrTokenId());
                    TokenReplenishAlarm.m1071a(this.mContext, providerTokenKey);
                    this.zN.deleteToken(tokenKey);
                    this.zG.bb(cn);
                    deleteEntry(cn);
                } else {
                    Log.m285d("VisaPayProviderSdk", "updateTokenStatus:Sdk: statusCode " + str);
                }
                i = 0;
            } catch (Throwable e) {
                Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
            } catch (TokenKeyInvalidException e2) {
                e2.printStackTrace();
            } catch (Exception e3) {
                e3.printStackTrace();
                i = -4;
            }
            return i;
        }
    }

    public Bundle m1088a(int i, ProviderTokenKey providerTokenKey) {
        eE();
        Bundle bundle = new Bundle();
        if (providerTokenKey == null) {
            Log.m286e("VisaPayProviderSdk", "stopNfcPay ProviderTokenKey is null");
            bundle.putShort("nfcApduErrorCode", (short) 1);
            return bundle;
        }
        short s;
        TransactionStatus processTransactionComplete = this.zN.processTransactionComplete(new TokenKey(providerTokenKey.cm()));
        Log.m285d("VisaPayProviderSdk", "stopNfcPay: reason : " + i + ", sdk ret = " + processTransactionComplete.getError());
        if (processTransactionComplete.getError() != TransactionError.NO_ERROR) {
            s = (short) 3;
        } else {
            s = (short) 2;
        }
        bundle.putShort("nfcApduErrorCode", s);
        if (processTransactionComplete.getError() == TransactionError.NO_ERROR && processTransactionComplete.isTapNGoAllowed()) {
            bundle.putInt("tapNGotransactionErrorCode", 0);
        } else if (processTransactionComplete.getError() == TransactionError.NO_AUTH_AMOUNT_REQ_NOT_SATISFIED) {
            bundle.putInt("tapNGotransactionErrorCode", PaymentFramework.RESULT_CODE_FAIL_INVALID_TRANSACTION_AMOUNT);
        } else if (processTransactionComplete.getError() == TransactionError.NO_AUTH_TRANSACTION_TYPE_REQ_NOT_SATISFIED) {
            bundle.putInt("tapNGotransactionErrorCode", PaymentFramework.RESULT_CODE_FAIL_PAY_INVALID_TRANSACTION_TYPE);
        } else if (processTransactionComplete.getError() == TransactionError.NO_AUTH_CURRENCY_REQ_NOT_SATISFIED) {
            bundle.putInt("tapNGotransactionErrorCode", PaymentFramework.RESULT_CODE_FAIL_PAY_INVALID_TRANSACTION_CURRENCY);
        }
        Map pdolValues = processTransactionComplete.getPdolValues();
        if (!(pdolValues == null || pdolValues.isEmpty())) {
            Bundle bundle2 = new Bundle();
            for (Entry entry : pdolValues.entrySet()) {
                bundle2.putString((String) entry.getKey(), (String) entry.getValue());
            }
            bundle.putBundle(TransactionStatus.EXTRA_PDOL_VALUES, bundle2);
        }
        return bundle;
    }

    public void stopMstPay(boolean z) {
        eE();
        Log.m287i("VisaPayProviderSdk", "stopMstPay: status: " + z);
        if (this.zN == null) {
            Log.m286e("VisaPayProviderSdk", "stopMstPay:Sdk instance is null");
            return;
        }
        try {
            this.zN.transactionComplete(z);
        } catch (Throwable e) {
            Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
        }
    }

    public void deleteToken(TokenKey tokenKey) {
        eE();
        try {
            this.zN.deleteToken(tokenKey);
        } catch (Throwable e) {
            Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public void m1095a(String str, ProviderTokenKey providerTokenKey, PaymentFrameworkRequester paymentFrameworkRequester) {
        String f = m1099f(providerTokenKey);
        if (f == null) {
            Log.m286e("VisaPayProviderSdk", "setupReplenishAlarm :  tokenStatus is null");
            return;
        }
        Log.m285d("VisaPayProviderSdk", "setupReplenishAlarm :  tokenStatus " + f);
        if (!f.equals(TokenStatus.ACTIVE)) {
            Log.m286e("VisaPayProviderSdk", "Not Replenishing as Token is Suspended or Pending.");
        } else if (str != null) {
            VisaTokenDetails ba = this.zG.ba(str);
            if (ba == null) {
                Log.m286e("VisaPayProviderSdk", "no token record. ignore updateAndCheckReplenishStatus ");
                return;
            }
            int maxPmts = ba.getMaxPmts();
            int eI = ba.eI();
            Log.m285d("VisaPayProviderSdk", "current: maxPmts " + maxPmts + "replenishPmts: " + eI);
            maxPmts--;
            if (maxPmts < 0) {
                maxPmts = 0;
            }
            this.zG.m1117a(TokenColumn.MAX_PMTS, Integer.valueOf(maxPmts), str);
            Log.m285d("VisaPayProviderSdk", " New current: maxPmts " + maxPmts + " replenishPmts: " + eI);
            VisaTokenDetails aX = aX(str);
            if (aX != null) {
                aX.setMaxPmts(maxPmts);
                m1093a(aX);
            }
            if (maxPmts <= eI) {
                Log.m285d("VisaPayProviderSdk", "updateAndCheckReplenishStatus: triggering Replenish request ");
                providerTokenKey.setTrTokenId(ba.getTrTokenId());
                paymentFrameworkRequester.m311a(providerTokenKey);
            }
        }
    }

    public boolean aW(String str) {
        return true;
    }

    public boolean isReplenishDataAvailable(JsonObject jsonObject) {
        boolean z;
        if (jsonObject != null) {
            ProvisionResponse provisionResponse = new ProvisionResponse();
            provisionResponse = (ProvisionResponse) new Gson().fromJson((JsonElement) jsonObject, ProvisionResponse.class);
            if (provisionResponse == null || provisionResponse.getTokenInfo() == null) {
                Log.m287i("VisaPayProviderSdk", "isReplenishDataAvailable:incoming data is invalid");
                return false;
            }
            TokenInfo tokenInfo = provisionResponse.getTokenInfo();
            if (tokenInfo == null || tokenInfo.getHceData() == null || tokenInfo.getHceData().getDynParams() == null || tokenInfo.getHceData().getDynParams().getEncKeyInfo() == null) {
                Log.m287i("VisaPayProviderSdk", "isReplenishDataAvailable:incoming data(tokenInfo) is invalid");
                return false;
            }
            z = true;
        } else {
            z = false;
        }
        Log.m287i("VisaPayProviderSdk", "isReplenishDataAvailable:ret :" + z);
        return z;
    }

    public void m1098b(String str, ProviderTokenKey providerTokenKey, PaymentFrameworkRequester paymentFrameworkRequester) {
        Log.m287i("VisaPayProviderSdk", "Entered setup Replenish Alarm");
        VisaTokenDetails visaTokenDetails = null;
        if (str != null) {
            Log.m286e("VisaPayProviderSdk", "trtokenid not null");
            visaTokenDetails = this.zG.aZ(str);
        }
        String f = m1099f(providerTokenKey);
        if (f == null) {
            Log.m286e("VisaPayProviderSdk", "setupReplenishAlarm :  tokenStatus is null");
            return;
        }
        Log.m285d("VisaPayProviderSdk", "setupReplenishAlarm :  tokenStatus " + f);
        if (!f.equals(TokenStatus.ACTIVE)) {
            Log.m286e("VisaPayProviderSdk", "Not Replenishing as Token is Suspended or Pending.");
        } else if (visaTokenDetails != null) {
            Log.m285d("VisaPayProviderSdk", "visa token details not null");
            Log.m285d("VisaPayProviderSdk", "visa token details replenishts = " + visaTokenDetails.eK());
            Log.m285d("VisaPayProviderSdk", "current time  = " + Utils.am(this.mContext));
            if (str == null) {
                return;
            }
            if (visaTokenDetails.eK() - Utils.am(this.mContext) <= 0 || visaTokenDetails.getMaxPmts() <= visaTokenDetails.eI()) {
                Log.m287i("VisaPayProviderSdk", "Run Replenish Rightaway: trTokenId " + str);
                Log.m285d("VisaPayProviderSdk", "Visa token record: " + visaTokenDetails.dump());
                paymentFrameworkRequester.m311a(providerTokenKey);
                return;
            }
            Log.m285d("VisaPayProviderSdk", "Setting up Replenish Alarm");
            TokenReplenishAlarm.m1070a(this.mContext, visaTokenDetails.eK(), providerTokenKey);
        }
    }

    public void setPayAuthenticationMode(String str) {
        eE();
        Log.m285d("VisaPayProviderSdk", "setPayAuthenticationMode: " + str);
        VerifyingType verifyingType = VerifyingType.PASSCODE;
        boolean z = true;
        if (PaymentNetworkProvider.AUTHTYPE_FP.equalsIgnoreCase(str)) {
            verifyingType = VerifyingType.OTHER_CD_CVM;
        } else if ("NONE".equalsIgnoreCase(str)) {
            verifyingType = VerifyingType.NO_CD_CVM;
            z = false;
        }
        try {
            this.zN.setCvmVerificationMode(new CvmMode(VerifyingEntity.MOBILE_APP, verifyingType));
            this.zN.setCvmVerified(z);
            Log.m285d("VisaPayProviderSdk", "setPayAuthenticationMode: type " + verifyingType);
        } catch (Throwable e) {
            Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
        }
    }

    private int m1084Q(int i) {
        if (i <= 0) {
            return (int) 4607182418800017408;
        }
        if (i == 1) {
            return 0;
        }
        double d = 0.25d * ((double) i);
        if (i <= 15) {
            d += 1.0d;
        }
        Log.m285d("VisaPayProviderSdk", "calculatereplenishpmts - maxPmts= " + i + " replenishPmts = " + ((int) d));
        return (int) d;
    }

    private long m1085w(long j) {
        Log.m285d("VisaPayProviderSdk", "getReplenishTsinMs - keyExpTs = " + j);
        if (j <= 0) {
            Log.m286e("VisaPayProviderSdk", "getReplenishTsinMs - keyExpTs  value is negative. setting default interval ");
            return Utils.am(this.mContext) + 600000;
        }
        if (Long.toString(j).length() <= 10) {
            j *= 1000;
        }
        Log.m285d("VisaPayProviderSdk", "getReplenishTsinMs - keyExpTs in milliseconds= " + j);
        return j;
    }

    private long m1086x(long j) {
        long am = Utils.am(this.mContext);
        Log.m285d("VisaPayProviderSdk", "calculatereplenishts - keyExpTs = " + j + " CurrnetTime: " + am);
        if (j <= 0) {
            Log.m286e("VisaPayProviderSdk", "calculatereplenishts - keyExpTs  value is negative. setting default interval ");
            return am + 600000;
        }
        if (Long.toString(j).length() <= 10) {
            j *= 1000;
            Log.m285d("VisaPayProviderSdk", "calculatereplenishts - keyExpTs in milliseconds= " + j);
        }
        long j2 = (((j - am) * 3) / 4) + am;
        if (j2 <= am) {
            Log.m287i("VisaPayProviderSdk", "Replenish time is <= currnetTime. Setting default Replenish Time600000");
            j2 = am + 600000;
        }
        Log.m285d("VisaPayProviderSdk", "calculatereplenishts - replenishTs = " + j2);
        return j2;
    }

    private String getEmailAddressHash(String str) {
        String encodeToString;
        Throwable e;
        int i = 0;
        try {
            Object obj;
            String toLowerCase = str.toLowerCase();
            MessageDigest instance = MessageDigest.getInstance("SHA-256");
            byte[] bytes = toLowerCase.getBytes();
            Object obj2 = null;
            while (i < LocationStatusCodes.GEOFENCE_NOT_AVAILABLE) {
                instance.reset();
                instance.update(bytes);
                obj2 = instance.digest();
                i++;
                obj = obj2;
            }
            instance.reset();
            instance.update(toLowerCase.getBytes());
            Object digest = instance.digest();
            obj = new byte[(digest.length + obj2.length)];
            System.arraycopy(digest, 0, obj, 0, digest.length);
            System.arraycopy(obj2, 0, obj, digest.length, obj2.length);
            instance.reset();
            instance.update(obj);
            encodeToString = Base64.encodeToString(instance.digest(), 11);
            try {
                Log.m285d("VisaPayProviderSdk", " emailHash: " + encodeToString);
            } catch (Exception e2) {
                e = e2;
                Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
                return encodeToString;
            }
        } catch (Throwable e3) {
            Throwable th = e3;
            encodeToString = null;
            e = th;
            Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
            return encodeToString;
        }
        return encodeToString;
    }

    private void init() {
        if (eF() == -1) {
            List eM = this.zG.eM();
            Log.m285d("VisaPayProviderSdk", " init: " + eM);
            if (eM != null && !eM.isEmpty()) {
                for (int i = 0; i < eM.size(); i++) {
                    m1093a(this.zG.ba((String) eM.get(i)));
                }
            }
        }
    }

    public synchronized void m1093a(VisaTokenDetails visaTokenDetails) {
        if (!(zO == null || visaTokenDetails == null || visaTokenDetails.eH() == null)) {
            zO.put(visaTokenDetails.eH(), visaTokenDetails);
            Log.m285d("VisaPayProviderSdk", " addEntry: " + visaTokenDetails.dump());
        }
    }

    private synchronized void deleteEntry(String str) {
        if (!(zO == null || zO.get(str) == null)) {
            zO.remove(str);
        }
    }

    public synchronized VisaTokenDetails aX(String str) {
        VisaTokenDetails visaTokenDetails;
        if (zO == null || zO.isEmpty()) {
            visaTokenDetails = null;
        } else {
            visaTokenDetails = (VisaTokenDetails) zO.get(str);
        }
        return visaTokenDetails;
    }

    private synchronized int eF() {
        int i;
        if (zO == null) {
            zO = new HashMap();
            i = -1;
        } else {
            i = zO.size();
        }
        return i;
    }

    public PaymentDataRequest m1092a(TokenKey tokenKey, String str, boolean z) {
        Log.m285d("VisaPayProviderSdk", "getInAppRequestData");
        eE();
        if (str == null || tokenKey == null) {
            Log.m286e("VisaPayProviderSdk", "getInAppRequestData, input is null");
            return null;
        } else if (this.zN == null) {
            Log.m286e("VisaPayProviderSdk", "getInAppRequestData, sdk is null");
            return null;
        } else {
            try {
                return this.zN.constructPaymentDataRequest(null, tokenKey, str, z ? InAppPayment.RECURRING_TRANSACTION_TYPE : InAppPayment.ECOM_TRANSACTION_TYPE);
            } catch (Throwable e) {
                Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
                return null;
            }
        }
    }

    void m1094a(TokenKey tokenKey, boolean z, boolean z2) {
        Log.m285d("VisaPayProviderSdk", "processInAppTransactionComplete " + z2);
        eE();
        if (tokenKey == null) {
            Log.m286e("VisaPayProviderSdk", "tokenKey is null ");
        } else if (this.zN == null) {
            Log.m286e("VisaPayProviderSdk", "sdk is null");
        } else {
            try {
                this.zN.processInAppTransactionComplete(tokenKey, z ? InAppPayment.RECURRING_TRANSACTION_TYPE : InAppPayment.ECOM_TRANSACTION_TYPE, z2);
            } catch (Throwable e) {
                Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
            }
        }
    }

    boolean m1096a(TokenKey tokenKey, int i) {
        boolean z = false;
        Log.m285d("VisaPayProviderSdk", "isPresentationModeSupported " + i);
        if (i != 2) {
            return true;
        }
        eE();
        if (tokenKey == null) {
            Log.m286e("VisaPayProviderSdk", "tokenKey is null ");
            return z;
        } else if (this.zN == null) {
            Log.m286e("VisaPayProviderSdk", "sdk is null");
            return z;
        } else {
            try {
                return this.zN.isMstSupported(tokenKey);
            } catch (Throwable e) {
                Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
                return z;
            }
        }
    }

    Bundle getTokenMetaData(TokenKey tokenKey) {
        if (this.zN == null) {
            Log.m286e("VisaPayProviderSdk", "sdk is null");
            return null;
        } else if (tokenKey == null) {
            Log.m286e("VisaPayProviderSdk", "tokenKey is null");
            return null;
        } else {
            try {
                Bundle tokenMetaData = this.zN.getTokenMetaData(tokenKey);
                if (tokenMetaData == null) {
                    Log.m286e("VisaPayProviderSdk", "metaData is null");
                    return null;
                }
                String string = tokenMetaData.getString(TokenMetaData.QVSDC_ISSUER_COUNTRY_CODE);
                if (string == null) {
                    Log.m286e("VisaPayProviderSdk", "issuerCountryCode is null");
                    return null;
                }
                tokenMetaData = new Bundle();
                tokenMetaData.putString(PaymentFramework.EXTRA_TOKEN_METADATA_ISSUER_COUNTRY_CODE, string);
                return tokenMetaData;
            } catch (Throwable e) {
                Log.m284c("VisaPayProviderSdk", e.getMessage(), e);
                return null;
            }
        }
    }

    private static Bundle eG() {
        if (!"GB".equals(Utils.fP())) {
            return null;
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean(TransactionStatus.EXTRA_PDOL_VALUES, true);
        return bundle;
    }
}
