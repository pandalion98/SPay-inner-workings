/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.text.TextUtils
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  javax.net.ssl.SSLSocketFactory
 */
package com.samsung.android.spayfw.payprovider;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.e;
import com.samsung.android.spayfw.remoteservice.e.b;
import com.samsung.android.spayfw.utils.GLDManager;
import com.samsung.android.spayfw.utils.g;
import com.samsung.android.spayfw.utils.h;

public class MerchantServerRequester {
    private static MerchantServerRequester oN = null;

    private MerchantServerRequester() {
    }

    public static MerchantServerRequester cc() {
        Class<MerchantServerRequester> class_ = MerchantServerRequester.class;
        synchronized (MerchantServerRequester.class) {
            if (oN == null) {
                oN = new MerchantServerRequester();
            }
            MerchantServerRequester merchantServerRequester = oN;
            // ** MonitorExit[var2] (shouldn't be in output)
            return merchantServerRequester;
        }
    }

    public void a(MerchantInfo merchantInfo) {
        if (merchantInfo == null || merchantInfo.getResultCode() == null) {
            Log.e("MerchantServerRequester", " while parsing response, result code not found!");
            throw new PaymentProviderException(-205);
        }
        String string = merchantInfo.getResultCode();
        if (!"0".equals((Object)string)) {
            Log.e("MerchantServerRequester", "Error msg: " + merchantInfo.getResultMessage());
            Log.e("MerchantServerRequester", "resultcode: " + string);
            throw new PaymentProviderException(-205);
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public MerchantInfo c(Context var1_1, String var2_2) {
        if (var1_1 == null || TextUtils.isEmpty((CharSequence)var2_2)) {
            Log.e("MerchantServerRequester", "Invalid input");
            throw new PaymentProviderException(-5);
        }
        Log.d("MerchantServerRequester", "getMerchantInfo: start ");
        var3_3 = h.fP();
        var4_4 = e.h(var1_1).getConfig("CONFIG_WALLET_ID");
        var5_5 = h.ah(var1_1);
        var6_6 = GLDManager.af(var1_1).by("PROD");
        if (TextUtils.isEmpty((CharSequence)var6_6)) {
            Log.e("MerchantServerRequester", "failed to get url from GLDManager!");
            throw new PaymentProviderException(-36);
        }
        var7_7 = new StringBuilder();
        var7_7.append(var6_6);
        var7_7.append("/payment/v1.0/profile/products/productId/certs".replace((CharSequence)"productId", (CharSequence)var2_2));
        var10_8 = new g();
        var10_8.addHeader("x-smps-cc2", var3_3);
        var10_8.addHeader("x-smps-dmid", var4_4);
        var10_8.addHeader("x-smps-did", var5_5);
        var10_8.addHeader("x-smps-dt", "Mobile");
        var11_9 = e.h(var1_1).getConfig("CONFIG_JWT_TOKEN");
        if (var11_9 == null) ** GOTO lbl31
        var10_8.addHeader("Authorization", "Bearer " + var11_9);
        var12_10 = b.a(com.samsung.android.spayfw.remoteservice.e.c.M(var1_1).getSocketFactory());
        var10_8.b(20000, 20000, true);
        var10_8.setSSLSocketFactory(var12_10);
        var13_11 = var10_8.bz(var7_7.toString());
        Log.d("MerchantServerRequester", "Merchant status code response is " + var13_11.statusCode);
        switch (var13_11.statusCode) {
            default: {
                throw new PaymentProviderException(-205);
            }
lbl31: // 1 sources:
            Log.e("MerchantServerRequester", "JWT Token is EMPTY!");
            throw new PaymentProviderException(-36);
            case 200: 
            case 201: 
            case 202: {
                if (var13_11.Dd != null) {
                    var14_12 = new String(var13_11.Dd);
                    var15_13 = new GsonBuilder().disableHtmlEscaping().create();
                    var16_14 = (MerchantInfo)var15_13.fromJson(var14_12, MerchantInfo.class);
                    this.a(var16_14);
                    Log.d("MerchantServerRequester", "Merchant raw response is: " + var14_12);
                    Log.d("MerchantServerRequester", "Merchant response is: " + var15_13.toJson((Object)var16_14));
                    return var16_14;
                }
                Log.e("MerchantServerRequester", "Merchant response is null!");
                throw new PaymentProviderException(-205);
            }
            case 408: 
            case 503: {
                Log.d("MerchantServerRequester", "Server No Response");
                throw new PaymentProviderException(-201);
            }
            case 0: {
                Log.d("MerchantServerRequester", "Fail No Network");
                throw new PaymentProviderException(-9);
            }
            case 401: 
            case 403: {
                throw new PaymentProviderException(-4);
            }
            case 406: 
            case 410: {
                throw new PaymentProviderException(-202);
            }
            case 400: {
                throw new PaymentProviderException(-5);
            }
            case 421: {
                throw new PaymentProviderException(-1);
            }
            case 404: 
        }
        throw new PaymentProviderException(-6);
    }

    public static class Certificate {
        private String alias;
        private String content;
        private String usage;

        public Certificate(String string, String string2, String string3) {
            this.usage = string;
            this.alias = string2;
            this.content = string3;
        }

        public String getAlias() {
            return this.alias;
        }

        public String getContent() {
            return this.content;
        }

        public String getUsage() {
            return this.usage;
        }
    }

    public static class MerchantInfo {
        private String payloadType;
        private PgInfo pgInfo;
        private ProductInfo productInfo;
        private String resultCode;
        private String resultMessage;
        private Certificate signingInfo;

        public String getMerchantCertificateChain() {
            if (this.productInfo == null || this.productInfo.getCertificate() == null) {
                Log.e("MerchantServerRequester", "Invalid input productInfo");
                return null;
            }
            String string = this.productInfo.getCertificate().getContent();
            if (string == null || string.isEmpty()) {
                Log.e("MerchantServerRequester", "Invalid input merchantCert");
                return null;
            }
            String string2 = this.signingInfo.getContent();
            if (string2 == null || string2.isEmpty()) {
                Log.e("MerchantServerRequester", "Invalid input caCert");
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(string);
            stringBuilder.append(string2);
            String string3 = stringBuilder.toString();
            Log.d("MerchantServerRequester", "cert chain: " + string3);
            return string3;
        }

        public String getPayloadType() {
            return this.payloadType;
        }

        public PgInfo getPgInfo() {
            return this.pgInfo;
        }

        public ProductInfo getProductInfo() {
            return this.productInfo;
        }

        public String getResultCode() {
            return this.resultCode;
        }

        public String getResultMessage() {
            return this.resultMessage;
        }

        public Certificate getSigningInfo() {
            return this.signingInfo;
        }

        public boolean isIndirect() {
            if (this.pgInfo == null || this.pgInfo.getIntegrationType() == null) {
                return false;
            }
            return "INDIRECT".equals((Object)this.pgInfo.getIntegrationType());
        }

        public boolean isJWEJWSRequired() {
            if (this.payloadType != null) {
                return this.payloadType.equals((Object)"JWE/JWS");
            }
            if (this.pgInfo == null || this.pgInfo.getIntegrationType() == null) {
                Log.e("MerchantServerRequester", "pgInfo not exist");
                return false;
            }
            return "INDIRECT".equals((Object)this.pgInfo.getIntegrationType());
        }
    }

    public static class PgInfo {
        public static final String INTEGRATION_TYPE_DIRECT = "DIRECT";
        public static final String INTEGRATION_TYPE_INDIRECT = "INDIRECT";
        private String integrationType;
        private String name;

        public PgInfo(String string, String string2) {
            this.integrationType = string;
            this.name = string2;
        }

        public String getIntegrationType() {
            return this.integrationType;
        }

        public String getName() {
            return this.name;
        }
    }

    public static class ProductInfo {
        private Certificate certificate;
        private String productId;
        private String timeStamp;

        public ProductInfo(String string, String string2, Certificate certificate) {
            this.productId = string;
            this.timeStamp = string2;
            this.certificate = certificate;
        }

        public Certificate getCertificate() {
            return this.certificate;
        }

        public String getProductId() {
            return this.productId;
        }

        public String getTimeStamp() {
            return this.timeStamp;
        }
    }

}

