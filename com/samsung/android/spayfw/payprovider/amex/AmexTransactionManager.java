package com.samsung.android.spayfw.payprovider.amex;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.core.ConfigurationManager;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.amex.models.AmexTransactionData;
import com.samsung.android.spayfw.payprovider.amex.models.AmexTransactionRequest;
import com.samsung.android.spayfw.payprovider.amex.models.AmexTransactionResponse;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController;
import com.samsung.android.spayfw.payprovider.amex.tzsvc.AmexTAController.DevicePublicCerts;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.QueryTokenRequest;
import com.samsung.android.spayfw.remoteservice.tokenrequester.TokenRequesterClient;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.utils.AsyncNetworkHttpClient.AsyncNetworkHttpClient;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.http.entity.StringEntity;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.pqc.jcajce.spec.McElieceCCA2ParameterSpec;

/* renamed from: com.samsung.android.spayfw.payprovider.amex.b */
public class AmexTransactionManager {
    private static AmexTransactionManager pt;
    private Context mContext;
    private AmexTAController pg;
    private CertificateInfo pl;
    private SharedPreferences pp;

    /* renamed from: com.samsung.android.spayfw.payprovider.amex.b.1 */
    class AmexTransactionManager extends C0413a<Response<TokenResponseData>, QueryTokenRequest> {
        final /* synthetic */ ProviderTokenKey pd;
        final /* synthetic */ TransactionResponse pu;
        final /* synthetic */ String pv;
        final /* synthetic */ AmexTransactionManager pw;

        AmexTransactionManager(AmexTransactionManager amexTransactionManager, ProviderTokenKey providerTokenKey, TransactionResponse transactionResponse, String str) {
            this.pw = amexTransactionManager;
            this.pd = providerTokenKey;
            this.pu = transactionResponse;
            this.pv = str;
        }

        public void m766a(int i, Response<TokenResponseData> response) {
            String str = null;
            Log.m285d("AmexTransactionManager", "onRequestComplete: Get Token Data for Transaction: status code " + i);
            switch (i) {
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    TokenResponseData tokenResponseData = (TokenResponseData) response.getResult();
                    if (tokenResponseData == null) {
                        Log.m286e("AmexTransactionManager", "TokenResponseData is null");
                        return;
                    }
                    String asString;
                    JsonObject data = tokenResponseData.getData();
                    if (data != null) {
                        asString = data.get("encryptionParametersForTransactionRegistration").getAsString();
                        str = data.get("encryptedDataForTransactionRegistration").getAsString();
                    } else {
                        asString = null;
                    }
                    if (str == null || asString == null) {
                        this.pw.m771a(this.pd, this.pu);
                    } else if (this.pw.m773a(this.pd, this.pu, str, asString) != 0) {
                        Log.m286e("AmexTransactionManager", "saveTransactionData failed");
                    } else {
                        this.pw.m777b(this.pd, this.pu, this.pv);
                    }
                default:
                    this.pu.m344a(this.pd, -7, null, null);
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.payprovider.amex.b.2 */
    class AmexTransactionManager implements AsyncNetworkHttpClient {
        final /* synthetic */ ProviderTokenKey pd;
        final /* synthetic */ TransactionResponse pu;
        final /* synthetic */ AmexTransactionManager pw;
        final /* synthetic */ String px;
        final /* synthetic */ JsonObject py;
        final /* synthetic */ AmexTransactionManager pz;
        final /* synthetic */ Gson val$gson;

        AmexTransactionManager(AmexTransactionManager amexTransactionManager, Gson gson, TransactionResponse transactionResponse, ProviderTokenKey providerTokenKey, String str, JsonObject jsonObject, AmexTransactionManager amexTransactionManager2) {
            this.pw = amexTransactionManager;
            this.val$gson = gson;
            this.pu = transactionResponse;
            this.pd = providerTokenKey;
            this.px = str;
            this.py = jsonObject;
            this.pz = amexTransactionManager2;
        }

        public void onComplete(int i, Map<String, List<String>> map, byte[] bArr) {
            Log.m286e("AmexTransactionManager", "Status Code : " + i);
            String str;
            switch (i) {
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    if (bArr == null || bArr.length <= 0) {
                        Log.m286e("AmexTransactionManager", "Transaction Data is empty");
                        this.pu.m344a(this.pd, -7, null, null);
                        return;
                    }
                    str = new String(bArr);
                    Log.m285d("AmexTransactionManager", "AmexTransactionResponse : " + str);
                    AmexTransactionResponse amexTransactionResponse = (AmexTransactionResponse) this.val$gson.fromJson(str, AmexTransactionResponse.class);
                    Log.m285d("AmexTransactionManager", "AmexTransactionResponse : " + amexTransactionResponse);
                    if (amexTransactionResponse == null || amexTransactionResponse.getTransactionDetail() == null || amexTransactionResponse.getTransactionDetail().length <= 0) {
                        this.pu.m344a(this.pd, -9, null, null);
                    } else {
                        this.pu.m344a(this.pd, 0, amexTransactionResponse.getTransactionDetail(), null);
                    }
                case 401:
                    this.pw.pp.edit().remove(this.px + "_transaction_json_data").apply();
                    ProviderResponseData providerResponseData = new ProviderResponseData();
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.add("secureDeviceData", this.py);
                    jsonObject.addProperty("tokenRefIdSignature", this.pz.pA);
                    providerResponseData.m1057b(jsonObject);
                    providerResponseData.setErrorCode(-8);
                    providerResponseData.as("PAYFW_ERROR_PAY_PROVIDER_NETWORK_AUTH");
                    this.pu.m344a(this.pd, -8, null, providerResponseData);
                default:
                    ProviderResponseData providerResponseData2 = new ProviderResponseData();
                    providerResponseData2.setErrorCode(-7);
                    if (bArr != null) {
                        try {
                            if (bArr.length > 0) {
                                str = new String(bArr);
                                Log.m286e("AmexTransactionManager", str);
                                providerResponseData2.as(i + " : " + str);
                            }
                        } catch (Throwable e) {
                            Log.m284c("AmexTransactionManager", e.getMessage(), e);
                            providerResponseData2.as(i + " : " + e.getMessage());
                        }
                    }
                    this.pu.m344a(this.pd, -7, null, providerResponseData2);
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.payprovider.amex.b.a */
    public class AmexTransactionManager {
        public String accessKey;
        public String accessKeySignature;
        public String pA;
        final /* synthetic */ AmexTransactionManager pw;

        AmexTransactionManager(AmexTransactionManager amexTransactionManager, String str, String str2, String str3) {
            this.pw = amexTransactionManager;
            this.accessKey = str;
            this.accessKeySignature = str2;
            this.pA = str3;
        }
    }

    static {
        pt = null;
    }

    public static final String ay(String str) {
        if (str == null) {
            return null;
        }
        try {
            Log.m285d("Data", str);
            MessageDigest instance = MessageDigest.getInstance(McElieceCCA2ParameterSpec.DEFAULT_MD);
            instance.update(str.getBytes());
            String encodeToString = Base64.encodeToString(instance.digest(), 2);
            Log.m285d("Hash", encodeToString);
            return encodeToString;
        } catch (Throwable e) {
            Log.m284c("AmexTransactionManager", e.getMessage(), e);
            return null;
        }
    }

    public static synchronized AmexTransactionManager m775a(Context context, AmexTAController amexTAController, CertificateInfo certificateInfo, SharedPreferences sharedPreferences) {
        AmexTransactionManager amexTransactionManager;
        synchronized (AmexTransactionManager.class) {
            if (pt == null) {
                pt = new AmexTransactionManager(context, amexTAController, certificateInfo, sharedPreferences);
            }
            amexTransactionManager = pt;
        }
        return amexTransactionManager;
    }

    private AmexTransactionManager(Context context, AmexTAController amexTAController, CertificateInfo certificateInfo, SharedPreferences sharedPreferences) {
        this.mContext = context;
        this.pg = amexTAController;
        this.pl = certificateInfo;
        this.pp = sharedPreferences;
    }

    int m778a(ProviderTokenKey providerTokenKey, Bundle bundle, TransactionResponse transactionResponse) {
        if (providerTokenKey == null || bundle == null || transactionResponse == null) {
            Log.m286e("AmexTransactionManager", "getTransactionData : invalid input ");
            return -4;
        }
        String string = bundle.getString("transactionUrl");
        if (string == null) {
            Log.m285d("AmexTransactionManager", "transactionUrl is empty, start registration");
            return m771a(providerTokenKey, transactionResponse);
        }
        if (this.pp.getString(providerTokenKey.cn() + "_transaction_json_data", null) == null) {
            Log.m285d("AmexTransactionManager", "accessKey is empty, get registration data");
            return m772a(providerTokenKey, transactionResponse, string);
        }
        Log.m285d("AmexTransactionManager", "get transaction data");
        return m777b(providerTokenKey, transactionResponse, string);
    }

    TransactionDetails m779a(ProviderTokenKey providerTokenKey, Object obj) {
        AmexTransactionData[] amexTransactionDataArr = (AmexTransactionData[]) obj;
        if (amexTransactionDataArr == null || amexTransactionDataArr.length <= 0) {
            Log.m286e("AmexTransactionManager", "Raw Transaction Data is empty");
            return null;
        }
        TransactionDetails transactionDetails = new TransactionDetails();
        List arrayList = new ArrayList();
        for (AmexTransactionData amexTransactionData : amexTransactionDataArr) {
            Log.m285d("AmexTransactionManager", amexTransactionData.toString());
            TransactionData transactionData = new TransactionData();
            transactionData.setAmount(amexTransactionData.getTransactionAmount());
            transactionData.setCurrencyCode(amexTransactionData.getTransactionCurrency());
            transactionData.setMechantName(amexTransactionData.getMerchantName());
            try {
                Date parse = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US).parse(amexTransactionData.getTransactionTimestamp());
                Log.m285d("AmexTransactionManager", parse.toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                String format = simpleDateFormat.format(parse);
                Log.m285d("AmexTransactionManager", format);
                transactionData.setTransactionDate(format);
            } catch (Exception e) {
                Log.m286e("AmexTransactionManager", e.getMessage());
            }
            if (amexTransactionData.getTransactionIdentifier() == null || amexTransactionData.getTransactionIdentifier().isEmpty()) {
                transactionData.setTransactionId(m776a(amexTransactionData));
            } else {
                transactionData.setTransactionId(amexTransactionData.getTransactionIdentifier());
            }
            transactionData.setTransactionStatus(amexTransactionData.getTransactionStatus());
            transactionData.setTransactionType(amexTransactionData.getTransactionType());
            Bundle bundle = new Bundle();
            bundle.putString(TransactionData.DISPLAYUNTIL, amexTransactionData.getDisplayUntil());
            bundle.putString(TransactionData.MERCHANTZIPCODE, amexTransactionData.getMerchantZipCode());
            bundle.putString(TransactionData.TRANSACTIONDETAILURL, amexTransactionData.getTransactionDetailUrl());
            transactionData.setCustomData(bundle);
            arrayList.add(transactionData);
        }
        transactionDetails.setTransactionData(arrayList);
        return transactionDetails;
    }

    private String m776a(AmexTransactionData amexTransactionData) {
        if (amexTransactionData == null) {
            return BuildConfig.FLAVOR;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(amexTransactionData.getTransactionTimestamp()).append(amexTransactionData.getMerchantName()).append(amexTransactionData.getTransactionAmount());
        return AmexTransactionManager.ay(stringBuilder.toString());
    }

    private int m772a(ProviderTokenKey providerTokenKey, TransactionResponse transactionResponse, String str) {
        Log.m285d("AmexTransactionManager", "getRegistrationData");
        TokenRequesterClient.m1126Q(PaymentFrameworkApp.aB()).m1141x("credit/ax", providerTokenKey.cn()).m836a(new AmexTransactionManager(this, providerTokenKey, transactionResponse, str));
        return 0;
    }

    private JsonObject cs() {
        JsonObject jsonObject = new JsonObject();
        try {
            DevicePublicCerts cu = this.pg.cu();
            if (cu.deviceCertificate != null) {
                jsonObject.addProperty("devicePublicKeyCert", cu.deviceCertificate);
            }
            if (cu.deviceEncryptionCertificate != null) {
                jsonObject.addProperty("deviceEncryptionPublicKeyCert", cu.deviceEncryptionCertificate);
            }
            if (cu.deviceSigningCertificate == null) {
                return jsonObject;
            }
            jsonObject.addProperty("deviceSigningPublicKeyCert", cu.deviceSigningCertificate);
            return jsonObject;
        } catch (Throwable e) {
            Log.m284c("AmexTransactionManager", e.getMessage(), e);
            return null;
        }
    }

    private int m773a(ProviderTokenKey providerTokenKey, TransactionResponse transactionResponse, String str, String str2) {
        int i = -6;
        Gson create = new GsonBuilder().disableHtmlEscaping().create();
        String cn = providerTokenKey.cn();
        try {
            String str3 = new String(Base64.decode(this.pg.m788o(str, str2), 2));
            try {
                try {
                    try {
                        this.pp.edit().putString(cn + "_transaction_json_data", this.pg.m785a(create.toJson(new AmexTransactionManager(this, str3, this.pg.m787b(this.pl.getContent(), null, str3).requestDataSignature, this.pg.m787b(this.pl.getContent(), null, cn).requestDataSignature)), true)).apply();
                        return 0;
                    } catch (Throwable e) {
                        Log.m284c("AmexTransactionManager", e.getMessage(), e);
                        transactionResponse.m344a(providerTokenKey, i, null, null);
                        return i;
                    }
                } catch (Throwable e2) {
                    Log.m284c("AmexTransactionManager", e2.getMessage(), e2);
                    return i;
                }
            } catch (Throwable e22) {
                Log.m284c("AmexTransactionManager", e22.getMessage(), e22);
                return i;
            }
        } catch (Throwable e222) {
            Log.m284c("AmexTransactionManager", e222.getMessage(), e222);
            Log.m286e("AmexTransactionManager", "encryptedData is invalid");
            transactionResponse.m344a(providerTokenKey, i, null, null);
            return i;
        }
    }

    private int m777b(ProviderTokenKey providerTokenKey, TransactionResponse transactionResponse, String str) {
        Gson create = new GsonBuilder().disableHtmlEscaping().create();
        String cn = providerTokenKey.cn();
        String string = this.pp.getString(cn + "_transaction_json_data", null);
        if (string == null) {
            Log.m286e("AmexTransactionManager", "Error: Transaction Data in Preference file is empty");
            transactionResponse.m344a(providerTokenKey, -6, null, null);
            return -6;
        }
        try {
            AmexTransactionManager amexTransactionManager = (AmexTransactionManager) create.fromJson(this.pg.m785a(string, false), AmexTransactionManager.class);
            JsonObject cs = cs();
            if (cs == null) {
                return -6;
            }
            Object amexTransactionRequest = new AmexTransactionRequest();
            amexTransactionRequest.setAccessKey(amexTransactionManager.accessKey);
            amexTransactionRequest.setTokenRefId(cn);
            amexTransactionRequest.setSecureDeviceData(cs);
            amexTransactionRequest.setAccessKeySignature(amexTransactionManager.accessKeySignature);
            string = create.toJson(amexTransactionRequest);
            Log.m285d("AmexTransactionManager", "Request Data : " + string);
            com.samsung.android.spayfw.utils.AsyncNetworkHttpClient asyncNetworkHttpClient = new com.samsung.android.spayfw.utils.AsyncNetworkHttpClient();
            try {
                StringEntity stringEntity = new StringEntity(string);
            } catch (Throwable e) {
                Log.m284c("AmexTransactionManager", e.getMessage(), e);
            }
            asyncNetworkHttpClient.addHeader("Content-Type", "application/json");
            asyncNetworkHttpClient.addHeader("tokenRequesterId", "30000000025");
            asyncNetworkHttpClient.addHeader("walletId", ConfigurationManager.m581h(this.mContext).getConfig(PaymentFramework.CONFIG_WALLET_ID));
            asyncNetworkHttpClient.addHeader("deviceId", DeviceInfo.getDeviceId(this.mContext));
            asyncNetworkHttpClient.m1263a(str, string.getBytes(), "application/json", new AmexTransactionManager(this, create, transactionResponse, providerTokenKey, cn, cs, amexTransactionManager));
            return 0;
        } catch (Throwable e2) {
            Log.m284c("AmexTransactionManager", e2.getMessage(), e2);
            transactionResponse.m344a(providerTokenKey, -6, null, null);
            return -6;
        }
    }

    private int m771a(ProviderTokenKey providerTokenKey, TransactionResponse transactionResponse) {
        int i = -6;
        Log.m285d("AmexTransactionManager", "startRegistration");
        String cn = providerTokenKey.cn();
        ProviderResponseData providerResponseData = new ProviderResponseData();
        JsonElement cs = cs();
        if (cs == null) {
            return i;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("secureDeviceData", cs);
        try {
            jsonObject.addProperty("tokenRefIdSignature", this.pg.m787b(this.pl.getContent(), null, cn).requestDataSignature);
            providerResponseData.m1057b(jsonObject);
            providerResponseData.setErrorCode(-8);
            providerResponseData.as("PAYFW_ERROR_PAY_PROVIDER_NETWORK_AUTH");
            transactionResponse.m344a(providerTokenKey, -8, null, providerResponseData);
            return 0;
        } catch (Throwable e) {
            Log.m284c("AmexTransactionManager", e.getMessage(), e);
            return i;
        }
    }
}
