/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Bundle
 *  android.util.Base64
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  java.io.UnsupportedEncodingException
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.Throwable
 *  java.security.MessageDigest
 *  java.security.NoSuchAlgorithmException
 *  java.text.SimpleDateFormat
 *  java.util.ArrayList
 *  java.util.Date
 *  java.util.List
 *  java.util.Locale
 *  java.util.Map
 *  java.util.TimeZone
 *  org.apache.http.entity.StringEntity
 */
package com.samsung.android.spayfw.payprovider.amexv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.payprovider.amex.models.AmexTransactionData;
import com.samsung.android.spayfw.payprovider.amex.models.AmexTransactionRequest;
import com.samsung.android.spayfw.payprovider.amex.models.AmexTransactionResponse;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.AmexTAException;
import com.samsung.android.spayfw.payprovider.amexv2.tzsvc.c;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.g;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.DeviceInfo;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenResponseData;
import com.samsung.android.spayfw.utils.a;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.apache.http.entity.StringEntity;

public class b {
    private static b qJ = null;
    private Context mContext;
    private CertificateInfo pl;
    private SharedPreferences pp;
    private c qA;

    private b(Context context, c c2, CertificateInfo certificateInfo, SharedPreferences sharedPreferences) {
        this.mContext = context;
        this.qA = c2;
        this.pl = certificateInfo;
        this.pp = sharedPreferences;
    }

    private int a(f f2, i i2) {
        c.c c2;
        com.samsung.android.spayfw.b.c.d("AmexTransactionManager", "startRegistration");
        String string = f2.cn();
        e e2 = new e();
        JsonObject jsonObject = this.cs();
        if (jsonObject == null) {
            return -6;
        }
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("secureDeviceData", (JsonElement)jsonObject);
        try {
            c2 = this.qA.c(this.pl.getContent(), null, string);
        }
        catch (AmexTAException amexTAException) {
            com.samsung.android.spayfw.b.c.c("AmexTransactionManager", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            return -6;
        }
        jsonObject2.addProperty("tokenRefIdSignature", c2.requestDataSignature);
        e2.b(jsonObject2);
        e2.setErrorCode(-8);
        e2.as("PAYFW_ERROR_PAY_PROVIDER_NETWORK_AUTH");
        i2.a(f2, -8, null, e2);
        return 0;
    }

    private int a(final f f2, final i i2, final String string) {
        com.samsung.android.spayfw.b.c.d("AmexTransactionManager", "getRegistrationData");
        String string2 = f2.cn();
        l.Q((Context)PaymentFrameworkApp.aB()).x("credit/ax", string2).a(new Request.a<com.samsung.android.spayfw.remoteservice.c<TokenResponseData>, g>(){

            /*
             * Enabled aggressive block sorting
             */
            @Override
            public void a(int n2, com.samsung.android.spayfw.remoteservice.c<TokenResponseData> c2) {
                String string3;
                String string2;
                com.samsung.android.spayfw.b.c.d("AmexTransactionManager", "onRequestComplete: Get Token Data for Transaction: status code " + n2);
                switch (n2) {
                    default: {
                        i2.a(f2, -7, null, null);
                        return;
                    }
                    case 200: 
                }
                TokenResponseData tokenResponseData = c2.getResult();
                if (tokenResponseData == null) {
                    com.samsung.android.spayfw.b.c.e("AmexTransactionManager", "TokenResponseData is null");
                    return;
                }
                JsonObject jsonObject = tokenResponseData.getData();
                if (jsonObject != null) {
                    string2 = jsonObject.get("encryptionParametersForTransactionRegistration").getAsString();
                    string3 = jsonObject.get("encryptedDataForTransactionRegistration").getAsString();
                } else {
                    string2 = null;
                    string3 = null;
                }
                if (string3 != null && string2 != null) {
                    if (b.this.a(f2, i2, string3, string2) != 0) {
                        com.samsung.android.spayfw.b.c.e("AmexTransactionManager", "saveTransactionData failed");
                        return;
                    }
                    b.this.b(f2, i2, string);
                    return;
                }
                b.this.a(f2, i2);
            }
        });
        return 0;
    }

    private int a(f f2, i i2, String string, String string2) {
        c.c c2;
        String string3;
        String string4;
        String string5;
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String string6 = f2.cn();
        try {
            string5 = this.qA.o(string, string2);
        }
        catch (AmexTAException amexTAException) {
            com.samsung.android.spayfw.b.c.c("AmexTransactionManager", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            com.samsung.android.spayfw.b.c.e("AmexTransactionManager", "encryptedData is invalid");
            i2.a(f2, -6, null, null);
            return -6;
        }
        String string7 = new String(Base64.decode((String)string5, (int)2));
        try {
            c.c c3 = this.qA.c(this.pl.getContent(), null, string7);
            string3 = c3.requestDataSignature;
        }
        catch (AmexTAException amexTAException) {
            com.samsung.android.spayfw.b.c.c("AmexTransactionManager", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            return -6;
        }
        try {
            c2 = this.qA.c(this.pl.getContent(), null, string6);
        }
        catch (AmexTAException amexTAException) {
            com.samsung.android.spayfw.b.c.c("AmexTransactionManager", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            return -6;
        }
        String string8 = gson.toJson((Object)new a(string7, string3, c2.requestDataSignature));
        try {
            string4 = this.qA.a(string8, true);
        }
        catch (AmexTAException amexTAException) {
            com.samsung.android.spayfw.b.c.c("AmexTransactionManager", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            i2.a(f2, -6, null, null);
            return -6;
        }
        this.pp.edit().putString(string6 + "_transaction_json_data", string4).apply();
        return 0;
    }

    public static b a(Context context, c c2, CertificateInfo certificateInfo, SharedPreferences sharedPreferences) {
        Class<b> class_ = b.class;
        synchronized (b.class) {
            if (qJ == null) {
                qJ = new b(context, c2, certificateInfo, sharedPreferences);
            }
            b b2 = qJ;
            // ** MonitorExit[var6_4] (shouldn't be in output)
            return b2;
        }
    }

    private String a(AmexTransactionData amexTransactionData) {
        if (amexTransactionData == null) {
            return "";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(amexTransactionData.getTransactionTimestamp()).append(amexTransactionData.getMerchantName()).append(amexTransactionData.getTransactionAmount());
        return b.ay(stringBuilder.toString());
    }

    public static final String ay(String string) {
        if (string == null) {
            return null;
        }
        try {
            com.samsung.android.spayfw.b.c.d("Data", string);
            MessageDigest messageDigest = MessageDigest.getInstance((String)"SHA256");
            messageDigest.update(string.getBytes());
            String string2 = Base64.encodeToString((byte[])messageDigest.digest(), (int)2);
            com.samsung.android.spayfw.b.c.d("Hash", string2);
            return string2;
        }
        catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            com.samsung.android.spayfw.b.c.c("AmexTransactionManager", noSuchAlgorithmException.getMessage(), noSuchAlgorithmException);
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private int b(final f f2, final i i2, String string) {
        String string2;
        final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        final String string3 = f2.cn();
        String string4 = this.pp.getString(string3 + "_transaction_json_data", null);
        if (string4 == null) {
            com.samsung.android.spayfw.b.c.e("AmexTransactionManager", "Error: Transaction Data in Preference file is empty");
            i2.a(f2, -6, null, null);
            return -6;
        }
        try {
            string2 = this.qA.a(string4, false);
        }
        catch (AmexTAException amexTAException) {
            com.samsung.android.spayfw.b.c.c("AmexTransactionManager", amexTAException.getMessage(), (Throwable)((Object)amexTAException));
            i2.a(f2, -6, null, null);
            return -6;
        }
        final a a2 = (a)gson.fromJson(string2, a.class);
        final JsonObject jsonObject = this.cs();
        if (jsonObject == null) return -6;
        AmexTransactionRequest amexTransactionRequest = new AmexTransactionRequest();
        amexTransactionRequest.setAccessKey(a2.accessKey);
        amexTransactionRequest.setTokenRefId(string3);
        amexTransactionRequest.setSecureDeviceData(jsonObject);
        amexTransactionRequest.setAccessKeySignature(a2.accessKeySignature);
        String string5 = gson.toJson((Object)amexTransactionRequest);
        com.samsung.android.spayfw.b.c.d("AmexTransactionManager", "Request Data : " + string5);
        com.samsung.android.spayfw.utils.a a3 = new com.samsung.android.spayfw.utils.a();
        try {
            new StringEntity(string5);
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            com.samsung.android.spayfw.b.c.c("AmexTransactionManager", unsupportedEncodingException.getMessage(), unsupportedEncodingException);
        }
        a3.addHeader("Content-Type", "application/json");
        a3.addHeader("tokenRequesterId", "30000000025");
        a3.addHeader("walletId", com.samsung.android.spayfw.core.e.h(this.mContext).getConfig("CONFIG_WALLET_ID"));
        a3.addHeader("deviceId", DeviceInfo.getDeviceId(this.mContext));
        a3.a(string, string5.getBytes(), "application/json", new a.a(){

            /*
             * Loose catch block
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Lifted jumps to return sites
             */
            @Override
            public void onComplete(int n2, Map<String, List<String>> map, byte[] arrby) {
                e e2;
                block10 : {
                    com.samsung.android.spayfw.b.c.e("AmexTransactionManager", "Status Code : " + n2);
                    switch (n2) {
                        default: {
                            e2 = new e();
                            e2.setErrorCode(-7);
                            if (arrby != null) {
                                if (arrby.length > 0) {
                                    String string = new String(arrby);
                                    com.samsung.android.spayfw.b.c.e("AmexTransactionManager", string);
                                    e2.as(n2 + " : " + string);
                                }
                            }
                            break block10;
                        }
                        case 200: {
                            if (arrby != null && arrby.length > 0) {
                                String string = new String(arrby);
                                com.samsung.android.spayfw.b.c.d("AmexTransactionManager", "AmexTransactionResponse : " + string);
                                AmexTransactionResponse amexTransactionResponse = (AmexTransactionResponse)gson.fromJson(string, AmexTransactionResponse.class);
                                com.samsung.android.spayfw.b.c.d("AmexTransactionManager", "AmexTransactionResponse : " + amexTransactionResponse);
                                if (amexTransactionResponse != null && amexTransactionResponse.getTransactionDetail() != null && amexTransactionResponse.getTransactionDetail().length > 0) {
                                    i2.a(f2, 0, amexTransactionResponse.getTransactionDetail(), null);
                                    return;
                                }
                                i2.a(f2, -9, null, null);
                                return;
                            }
                            com.samsung.android.spayfw.b.c.e("AmexTransactionManager", "Transaction Data is empty");
                            i2.a(f2, -7, null, null);
                            return;
                        }
                        case 401: {
                            b.this.pp.edit().remove(string3 + "_transaction_json_data").apply();
                            e e3 = new e();
                            JsonObject jsonObject2 = new JsonObject();
                            jsonObject2.add("secureDeviceData", (JsonElement)jsonObject);
                            jsonObject2.addProperty("tokenRefIdSignature", a2.pA);
                            e3.b(jsonObject2);
                            e3.setErrorCode(-8);
                            e3.as("PAYFW_ERROR_PAY_PROVIDER_NETWORK_AUTH");
                            i2.a(f2, -8, null, e3);
                            return;
                        }
                    }
                    catch (Exception exception) {
                        com.samsung.android.spayfw.b.c.c("AmexTransactionManager", exception.getMessage(), exception);
                        e2.as(n2 + " : " + exception.getMessage());
                    }
                }
                i2.a(f2, -7, null, e2);
            }
        });
        return 0;
    }

    private JsonObject cs() {
        JsonObject jsonObject = new JsonObject();
        try {
            c.a a2 = this.qA.cA();
            if (a2.deviceCertificate != null) {
                jsonObject.addProperty("devicePublicKeyCert", a2.deviceCertificate);
            }
            if (a2.deviceEncryptionCertificate != null) {
                jsonObject.addProperty("deviceEncryptionPublicKeyCert", a2.deviceEncryptionCertificate);
            }
            if (a2.deviceSigningCertificate != null) {
                jsonObject.addProperty("deviceSigningPublicKeyCert", a2.deviceSigningCertificate);
            }
            return jsonObject;
        }
        catch (Exception exception) {
            com.samsung.android.spayfw.b.c.c("AmexTransactionManager", exception.getMessage(), exception);
            return null;
        }
    }

    int a(f f2, Bundle bundle, i i2) {
        if (f2 == null || bundle == null || i2 == null) {
            com.samsung.android.spayfw.b.c.e("AmexTransactionManager", "getTransactionData : invalid input ");
            return -4;
        }
        String string = bundle.getString("transactionUrl");
        if (string == null) {
            com.samsung.android.spayfw.b.c.d("AmexTransactionManager", "transactionUrl is empty, start registration");
            return this.a(f2, i2);
        }
        String string2 = f2.cn();
        if (this.pp.getString(string2 + "_transaction_json_data", null) == null) {
            com.samsung.android.spayfw.b.c.d("AmexTransactionManager", "accessKey is empty, get registration data");
            return this.a(f2, i2, string);
        }
        com.samsung.android.spayfw.b.c.d("AmexTransactionManager", "get transaction data");
        return this.b(f2, i2, string);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    TransactionDetails a(f f2, Object object) {
        AmexTransactionData[] arramexTransactionData = (AmexTransactionData[])object;
        if (arramexTransactionData == null || arramexTransactionData.length <= 0) {
            com.samsung.android.spayfw.b.c.e("AmexTransactionManager", "Raw Transaction Data is empty");
            return null;
        }
        TransactionDetails transactionDetails = new TransactionDetails();
        ArrayList arrayList = new ArrayList();
        int n2 = arramexTransactionData.length;
        int n3 = 0;
        do {
            if (n3 >= n2) {
                transactionDetails.setTransactionData((List<TransactionData>)arrayList);
                return transactionDetails;
            }
            AmexTransactionData amexTransactionData = arramexTransactionData[n3];
            com.samsung.android.spayfw.b.c.d("AmexTransactionManager", amexTransactionData.toString());
            TransactionData transactionData = new TransactionData();
            transactionData.setAmount(amexTransactionData.getTransactionAmount());
            transactionData.setCurrencyCode(amexTransactionData.getTransactionCurrency());
            transactionData.setMechantName(amexTransactionData.getMerchantName());
            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.US).parse(amexTransactionData.getTransactionTimestamp());
                com.samsung.android.spayfw.b.c.d("AmexTransactionManager", date.toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone((String)"GMT"));
                String string = simpleDateFormat.format(date);
                com.samsung.android.spayfw.b.c.d("AmexTransactionManager", string);
                transactionData.setTransactionDate(string);
            }
            catch (Exception exception) {
                com.samsung.android.spayfw.b.c.e("AmexTransactionManager", exception.getMessage());
            }
            if (amexTransactionData.getTransactionIdentifier() != null && !amexTransactionData.getTransactionIdentifier().isEmpty()) {
                transactionData.setTransactionId(amexTransactionData.getTransactionIdentifier());
            } else {
                transactionData.setTransactionId(this.a(amexTransactionData));
            }
            transactionData.setTransactionStatus(amexTransactionData.getTransactionStatus());
            transactionData.setTransactionType(amexTransactionData.getTransactionType());
            Bundle bundle = new Bundle();
            bundle.putString("displayUntil", amexTransactionData.getDisplayUntil());
            bundle.putString("merchantZipCode", amexTransactionData.getMerchantZipCode());
            bundle.putString("trnasactionDetailUrl", amexTransactionData.getTransactionDetailUrl());
            transactionData.setCustomData(bundle);
            arrayList.add((Object)transactionData);
            ++n3;
        } while (true);
    }

    public class a {
        public String accessKey;
        public String accessKeySignature;
        public String pA;

        a(String string, String string2, String string3) {
            this.accessKey = string;
            this.accessKeySignature = string2;
            this.pA = string3;
        }
    }

}

