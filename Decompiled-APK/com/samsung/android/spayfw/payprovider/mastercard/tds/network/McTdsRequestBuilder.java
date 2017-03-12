package com.samsung.android.spayfw.payprovider.mastercard.tds.network;

import com.google.gson.JsonObject;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.utils.AsyncNetworkHttpClient.AsyncNetworkHttpClient;
import java.io.UnsupportedEncodingException;

public class McTdsRequestBuilder {
    private static final String AUTH_CODE = "authenticationCode";
    private static final String LATEST_TAG = "lastUpdatedTag";
    private static final int REGISTRATION_HASH_LENGTH = 64;
    private static final String REG_HASH = "registrationHash";
    private static final String SCHEME = "https://";
    private static final String TAG = "McTdsRequestBuilder";
    private static final String TOKEN_ID_UNIQUE = "tokenUniqueReference";
    private static final int TOKEN_ID_UNIQUE_MAX_LENGTH = 64;
    private static final String URL_SUFFIX = "/tds/1/0/";

    /* renamed from: com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.1 */
    static /* synthetic */ class C05671 {
        static final /* synthetic */ int[] f14x33ec7f3c;

        static {
            f14x33ec7f3c = new int[RequestType.values().length];
            try {
                f14x33ec7f3c[RequestType.REGISTRATION_CODE.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                f14x33ec7f3c[RequestType.REGISTER.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                f14x33ec7f3c[RequestType.GET_TRANSACTIONS.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                f14x33ec7f3c[RequestType.UNREGISTER.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    public enum RequestMethod {
        GET,
        POST
    }

    public enum RequestType {
        REGISTRATION_CODE("/getRegistrationCode"),
        REGISTER("/register"),
        GET_TRANSACTIONS("/getTransactions"),
        UNREGISTER("/unregister");
        
        private String mType;

        private RequestType(String str) {
            this.mType = str;
        }

        public String getRequestType() {
            return this.mType;
        }
    }

    public static class TdsRequest {
        private AsyncNetworkHttpClient mCallbck;
        private JsonObject mData;
        private String mUrl;

        public TdsRequest(String str) {
            this.mUrl = str;
        }

        public String getUrl() {
            return this.mUrl;
        }

        public void setUrl(String str) {
            this.mUrl = str;
        }

        public JsonObject getData() {
            return this.mData;
        }

        public void setData(JsonObject jsonObject) {
            this.mData = jsonObject;
        }

        public AsyncNetworkHttpClient getCallbck() {
            return this.mCallbck;
        }

        public void setCallbck(AsyncNetworkHttpClient asyncNetworkHttpClient) {
            this.mCallbck = asyncNetworkHttpClient;
        }

        public byte[] getDataBytes() {
            byte[] bArr = null;
            if (this.mData != null) {
                try {
                    bArr = this.mData.toString().getBytes("UTF8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    Log.m286e(McTdsRequestBuilder.TAG, "UnsupportedEncodingException..");
                }
            }
            return bArr;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.TdsRequest build(com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.RequestType r10, com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsMetaData r11, java.lang.String r12, com.samsung.android.spayfw.payprovider.TransactionResponse r13, java.lang.String r14) {
        /*
        r4 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.class;
        monitor-enter(r4);
        r0 = new com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder$TdsRequest;	 Catch:{ all -> 0x0080 }
        r0.<init>();	 Catch:{ all -> 0x0080 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0080 }
        r1 = "https://";
        r5.<init>(r1);	 Catch:{ all -> 0x0080 }
        r6 = new com.google.gson.JsonObject;	 Catch:{ all -> 0x0080 }
        r6.<init>();	 Catch:{ all -> 0x0080 }
        r3 = 0;
        r2 = 0;
        r1 = 0;
        r7 = r11.getPaymentAppInstanceId();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        if (r7 != 0) goto L_0x0027;
    L_0x001d:
        r0 = "McTdsRequestBuilder";
        r1 = "build: PaymentAppInstanceId is null...";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r0 = 0;
    L_0x0025:
        monitor-exit(r4);
        return r0;
    L_0x0027:
        r7 = r12.length();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r8 = 64;
        if (r7 < r8) goto L_0x0038;
    L_0x002f:
        r0 = "McTdsRequestBuilder";
        r1 = "build: Token unique reference exceeded max length...";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r0 = 0;
        goto L_0x0025;
    L_0x0038:
        r7 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r7.<init>();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r8 = "/tds/1/0/";
        r7 = r7.append(r8);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r8 = r11.getPaymentAppInstanceId();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r7 = r7.append(r8);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r7 = r7.toString();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r8 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.C05671.f14x33ec7f3c;	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r9 = r10.ordinal();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r8 = r8[r9];	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        switch(r8) {
            case 1: goto L_0x0083;
            case 2: goto L_0x00b2;
            case 3: goto L_0x012f;
            case 4: goto L_0x0191;
            default: goto L_0x005a;
        };	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
    L_0x005a:
        r8 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r8.<init>();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r2 = r8.append(r2);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r2 = r2.append(r7);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r1 = r2.append(r1);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r1 = r1.toString();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r5.append(r1);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r1 = r5.toString();	 Catch:{ all -> 0x0080 }
        r0.setUrl(r1);	 Catch:{ all -> 0x0080 }
        r0.setData(r6);	 Catch:{ all -> 0x0080 }
        r0.setCallbck(r3);	 Catch:{ all -> 0x0080 }
        goto L_0x0025;
    L_0x0080:
        r0 = move-exception;
        monitor-exit(r4);
        throw r0;
    L_0x0083:
        r1 = android.text.TextUtils.isEmpty(r14);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        if (r1 == 0) goto L_0x00b0;
    L_0x0089:
        r2 = r11.getTdsRegisterUrl();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
    L_0x008d:
        r1 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.RequestType.REGISTRATION_CODE;	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r1 = r1.getRequestType();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = "tokenUniqueReference";
        r6.addProperty(r3, r12);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = new com.samsung.android.spayfw.payprovider.mastercard.tds.network.callback.McTdsRegistrationCode1Callbck;	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r8 = r11.getCardMasterId();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3.<init>(r8);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        goto L_0x005a;
    L_0x00a2:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0080 }
        r0 = "McTdsRequestBuilder";
        r1 = "build: NPE during TDS request building..";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ all -> 0x0080 }
        r0 = 0;
        goto L_0x0025;
    L_0x00b0:
        r2 = r14;
        goto L_0x008d;
    L_0x00b2:
        r1 = android.text.TextUtils.isEmpty(r14);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        if (r1 == 0) goto L_0x00f1;
    L_0x00b8:
        r2 = r11.getTdsRegisterUrl();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
    L_0x00bc:
        r1 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.RequestType.REGISTER;	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r1 = r1.getRequestType();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = r11.getHash();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = r3.length();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r8 = 64;
        if (r3 == r8) goto L_0x00f3;
    L_0x00ce:
        r0 = "McTdsRequestBuilder";
        r1 = new java.lang.StringBuilder;	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r1.<init>();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r2 = "build: Invalid Hash Length: ";
        r1 = r1.append(r2);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r2 = r11.getHash();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r2 = r2.length();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r1 = r1.append(r2);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r1 = r1.toString();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r0 = 0;
        goto L_0x0025;
    L_0x00f1:
        r2 = r14;
        goto L_0x00bc;
    L_0x00f3:
        r3 = "tokenUniqueReference";
        r6.addProperty(r3, r12);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = "registrationHash";
        r8 = r11.getHash();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r6.addProperty(r3, r8);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = new com.samsung.android.spayfw.payprovider.mastercard.tds.network.callback.McTdsRegisterCallbck;	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r8 = r11.getCardMasterId();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3.<init>(r8);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        goto L_0x005a;
    L_0x010c:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0080 }
        r1 = "McTdsRequestBuilder";
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0080 }
        r2.<init>();	 Catch:{ all -> 0x0080 }
        r3 = "build: Exception during TDS request building :";
        r2 = r2.append(r3);	 Catch:{ all -> 0x0080 }
        r0 = r0.getMessage();	 Catch:{ all -> 0x0080 }
        r0 = r2.append(r0);	 Catch:{ all -> 0x0080 }
        r0 = r0.toString();	 Catch:{ all -> 0x0080 }
        com.samsung.android.spayfw.p002b.Log.m286e(r1, r0);	 Catch:{ all -> 0x0080 }
        r0 = 0;
        goto L_0x0025;
    L_0x012f:
        r1 = android.text.TextUtils.isEmpty(r14);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        if (r1 == 0) goto L_0x0149;
    L_0x0135:
        r2 = r11.getTdsUrl();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
    L_0x0139:
        r1 = android.text.TextUtils.isEmpty(r2);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        if (r1 == 0) goto L_0x014b;
    L_0x013f:
        r0 = "McTdsRequestBuilder";
        r1 = "build: Missing urlBase Err: ";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r0 = 0;
        goto L_0x0025;
    L_0x0149:
        r2 = r14;
        goto L_0x0139;
    L_0x014b:
        r1 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.RequestType.GET_TRANSACTIONS;	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r1 = r1.getRequestType();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = r11.getAuthCode();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = android.text.TextUtils.isEmpty(r3);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        if (r3 == 0) goto L_0x0165;
    L_0x015b:
        r0 = "McTdsRequestBuilder";
        r1 = "build: Missing auth Code Cannot fetch tds: ";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r0 = 0;
        goto L_0x0025;
    L_0x0165:
        r3 = r11.getLastUpdateTag();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = android.text.TextUtils.isEmpty(r3);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        if (r3 != 0) goto L_0x0178;
    L_0x016f:
        r3 = "lastUpdatedTag";
        r8 = r11.getLastUpdateTag();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r6.addProperty(r3, r8);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
    L_0x0178:
        r3 = "tokenUniqueReference";
        r6.addProperty(r3, r12);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = "authenticationCode";
        r8 = r11.getAuthCode();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r6.addProperty(r3, r8);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = new com.samsung.android.spayfw.payprovider.mastercard.tds.network.callback.McTdsTransactionsCallbck;	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r8 = r11.getCardMasterId();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3.<init>(r8, r13);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        goto L_0x005a;
    L_0x0191:
        r1 = android.text.TextUtils.isEmpty(r14);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        if (r1 == 0) goto L_0x01b5;
    L_0x0197:
        r2 = r11.getTdsUrl();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
    L_0x019b:
        r1 = com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.RequestType.UNREGISTER;	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r1 = r1.getRequestType();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = r11.getAuthCode();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = android.text.TextUtils.isEmpty(r3);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        if (r3 == 0) goto L_0x01b7;
    L_0x01ab:
        r0 = "McTdsRequestBuilder";
        r1 = "build: Missing auth Code Cannot unRegister: ";
        com.samsung.android.spayfw.p002b.Log.m286e(r0, r1);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r0 = 0;
        goto L_0x0025;
    L_0x01b5:
        r2 = r14;
        goto L_0x019b;
    L_0x01b7:
        r3 = "tokenUniqueReference";
        r6.addProperty(r3, r12);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = "authenticationCode";
        r8 = r11.getAuthCode();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r6.addProperty(r3, r8);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3 = new com.samsung.android.spayfw.payprovider.mastercard.tds.network.callback.McTdsUnRegisterCallbck;	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r8 = r11.getCardMasterId();	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        r3.<init>(r8);	 Catch:{ NullPointerException -> 0x00a2, Exception -> 0x010c }
        goto L_0x005a;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder.build(com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder$RequestType, com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsMetaData, java.lang.String, com.samsung.android.spayfw.payprovider.i, java.lang.String):com.samsung.android.spayfw.payprovider.mastercard.tds.network.McTdsRequestBuilder$TdsRequest");
    }
}
