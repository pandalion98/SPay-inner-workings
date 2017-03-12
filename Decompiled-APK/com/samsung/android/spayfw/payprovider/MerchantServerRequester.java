package com.samsung.android.spayfw.payprovider;

import android.content.Context;
import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.core.ConfigurationManager;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.visa.transaction.TransactionInfo;
import com.samsung.android.spayfw.remoteservice.p022e.SpayFwSSLSocketFactory;
import com.samsung.android.spayfw.remoteservice.p022e.SslUtils;
import com.samsung.android.spayfw.utils.GLDManager;
import com.samsung.android.spayfw.utils.SyncNetworkHttpClient;
import com.samsung.android.spayfw.utils.Utils;
import javax.net.ssl.SSLSocketFactory;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;

public class MerchantServerRequester {
    private static MerchantServerRequester oN;

    public static class Certificate {
        private String alias;
        private String content;
        private String usage;

        public Certificate(String str, String str2, String str3) {
            this.usage = str;
            this.alias = str2;
            this.content = str3;
        }

        public String getUsage() {
            return this.usage;
        }

        public String getAlias() {
            return this.alias;
        }

        public String getContent() {
            return this.content;
        }
    }

    public static class MerchantInfo {
        private String payloadType;
        private PgInfo pgInfo;
        private ProductInfo productInfo;
        private String resultCode;
        private String resultMessage;
        private Certificate signingInfo;

        public Certificate getSigningInfo() {
            return this.signingInfo;
        }

        public String getResultCode() {
            return this.resultCode;
        }

        public String getResultMessage() {
            return this.resultMessage;
        }

        public PgInfo getPgInfo() {
            return this.pgInfo;
        }

        public ProductInfo getProductInfo() {
            return this.productInfo;
        }

        public String getPayloadType() {
            return this.payloadType;
        }

        public String getMerchantCertificateChain() {
            if (this.productInfo == null || this.productInfo.getCertificate() == null) {
                Log.m286e("MerchantServerRequester", "Invalid input productInfo");
                return null;
            }
            String content = this.productInfo.getCertificate().getContent();
            if (content == null || content.isEmpty()) {
                Log.m286e("MerchantServerRequester", "Invalid input merchantCert");
                return null;
            }
            String content2 = this.signingInfo.getContent();
            if (content2 == null || content2.isEmpty()) {
                Log.m286e("MerchantServerRequester", "Invalid input caCert");
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(content);
            stringBuilder.append(content2);
            String stringBuilder2 = stringBuilder.toString();
            Log.m285d("MerchantServerRequester", "cert chain: " + stringBuilder2);
            return stringBuilder2;
        }

        public boolean isJWEJWSRequired() {
            if (this.payloadType != null) {
                return this.payloadType.equals("JWE/JWS");
            }
            if (this.pgInfo != null && this.pgInfo.getIntegrationType() != null) {
                return PgInfo.INTEGRATION_TYPE_INDIRECT.equals(this.pgInfo.getIntegrationType());
            }
            Log.m286e("MerchantServerRequester", "pgInfo not exist");
            return false;
        }

        public boolean isIndirect() {
            if (this.pgInfo == null || this.pgInfo.getIntegrationType() == null) {
                return false;
            }
            return PgInfo.INTEGRATION_TYPE_INDIRECT.equals(this.pgInfo.getIntegrationType());
        }
    }

    public static class PgInfo {
        public static final String INTEGRATION_TYPE_DIRECT = "DIRECT";
        public static final String INTEGRATION_TYPE_INDIRECT = "INDIRECT";
        private String integrationType;
        private String name;

        public PgInfo(String str, String str2) {
            this.integrationType = str;
            this.name = str2;
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

        public ProductInfo(String str, String str2, Certificate certificate) {
            this.productId = str;
            this.timeStamp = str2;
            this.certificate = certificate;
        }

        public String getProductId() {
            return this.productId;
        }

        public String getTimeStamp() {
            return this.timeStamp;
        }

        public Certificate getCertificate() {
            return this.certificate;
        }
    }

    static {
        oN = null;
    }

    public static synchronized MerchantServerRequester cc() {
        MerchantServerRequester merchantServerRequester;
        synchronized (MerchantServerRequester.class) {
            if (oN == null) {
                oN = new MerchantServerRequester();
            }
            merchantServerRequester = oN;
        }
        return merchantServerRequester;
    }

    private MerchantServerRequester() {
    }

    public MerchantInfo m749c(Context context, String str) {
        if (context == null || TextUtils.isEmpty(str)) {
            Log.m286e("MerchantServerRequester", "Invalid input");
            throw new PaymentProviderException(-5);
        }
        Log.m285d("MerchantServerRequester", "getMerchantInfo: start ");
        String fP = Utils.fP();
        String config = ConfigurationManager.m581h(context).getConfig(PaymentFramework.CONFIG_WALLET_ID);
        String ah = Utils.ah(context);
        Object by = GLDManager.af(context).by("PROD");
        if (TextUtils.isEmpty(by)) {
            Log.m286e("MerchantServerRequester", "failed to get url from GLDManager!");
            throw new PaymentProviderException(-36);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(by);
        stringBuilder.append("/payment/v1.0/profile/products/productId/certs".replace("productId", str));
        SyncNetworkHttpClient syncNetworkHttpClient = new SyncNetworkHttpClient();
        syncNetworkHttpClient.addHeader("x-smps-cc2", fP);
        syncNetworkHttpClient.addHeader("x-smps-dmid", config);
        syncNetworkHttpClient.addHeader("x-smps-did", ah);
        syncNetworkHttpClient.addHeader("x-smps-dt", "Mobile");
        fP = ConfigurationManager.m581h(context).getConfig(PaymentFramework.CONFIG_JWT_TOKEN);
        if (fP != null) {
            syncNetworkHttpClient.addHeader("Authorization", "Bearer " + fP);
            SSLSocketFactory a = SpayFwSSLSocketFactory.m1187a(SslUtils.m1190M(context).getSocketFactory());
            syncNetworkHttpClient.m1272b(20000, 20000, true);
            syncNetworkHttpClient.setSSLSocketFactory(a);
            SyncNetworkHttpClient.SyncNetworkHttpClient bz = syncNetworkHttpClient.bz(stringBuilder.toString());
            Log.m285d("MerchantServerRequester", "Merchant status code response is " + bz.statusCode);
            switch (bz.statusCode) {
                case ECCurve.COORD_AFFINE /*0*/:
                    Log.m285d("MerchantServerRequester", "Fail No Network");
                    throw new PaymentProviderException(-9);
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                case 201:
                case 202:
                    if (bz.Dd != null) {
                        config = new String(bz.Dd);
                        Gson create = new GsonBuilder().disableHtmlEscaping().create();
                        Object obj = (MerchantInfo) create.fromJson(config, MerchantInfo.class);
                        m748a(obj);
                        Log.m285d("MerchantServerRequester", "Merchant raw response is: " + config);
                        Log.m285d("MerchantServerRequester", "Merchant response is: " + create.toJson(obj));
                        return obj;
                    }
                    Log.m286e("MerchantServerRequester", "Merchant response is null!");
                    throw new PaymentProviderException(PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL);
                case 400:
                    throw new PaymentProviderException(-5);
                case 401:
                case 403:
                    throw new PaymentProviderException(-4);
                case 404:
                    throw new PaymentProviderException(-6);
                case 406:
                case 410:
                    throw new PaymentProviderException(PaymentFramework.RESULT_CODE_FAIL_SERVER_REJECT);
                case 408:
                case 503:
                    Log.m285d("MerchantServerRequester", "Server No Response");
                    throw new PaymentProviderException(PaymentFramework.RESULT_CODE_FAIL_SERVER_NO_RESPONSE);
                case 421:
                    throw new PaymentProviderException(-1);
                default:
                    throw new PaymentProviderException(PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL);
            }
        }
        Log.m286e("MerchantServerRequester", "JWT Token is EMPTY!");
        throw new PaymentProviderException(-36);
    }

    public void m748a(MerchantInfo merchantInfo) {
        if (merchantInfo == null || merchantInfo.getResultCode() == null) {
            Log.m286e("MerchantServerRequester", " while parsing response, result code not found!");
            throw new PaymentProviderException(PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL);
        }
        String resultCode = merchantInfo.getResultCode();
        if (!TransactionInfo.VISA_TRANSACTIONTYPE_PURCHASE.equals(resultCode)) {
            Log.m286e("MerchantServerRequester", "Error msg: " + merchantInfo.getResultMessage());
            Log.m286e("MerchantServerRequester", "resultcode: " + resultCode);
            throw new PaymentProviderException(PaymentFramework.RESULT_CODE_FAIL_SERVER_INTERNAL);
        }
    }
}
