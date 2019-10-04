/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonObject
 *  java.io.UnsupportedEncodingException
 *  java.lang.Enum
 *  java.lang.NoSuchFieldError
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.mastercard.tds.network;

import com.google.gson.JsonObject;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.payprovider.mastercard.tds.network.models.McTdsMetaData;
import com.samsung.android.spayfw.utils.a;
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

    /*
     * Exception decompiling
     */
    public static TdsRequest build(RequestType var0, McTdsMetaData var1_1, String var2_2, i var3_3, String var4_4) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [13[CASE]], but top level block is 7[TRYBLOCK]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    public static final class RequestMethod
    extends Enum<RequestMethod> {
        private static final /* synthetic */ RequestMethod[] $VALUES;
        public static final /* enum */ RequestMethod GET = new RequestMethod();
        public static final /* enum */ RequestMethod POST = new RequestMethod();

        static {
            RequestMethod[] arrrequestMethod = new RequestMethod[]{GET, POST};
            $VALUES = arrrequestMethod;
        }

        public static RequestMethod valueOf(String string) {
            return (RequestMethod)Enum.valueOf(RequestMethod.class, (String)string);
        }

        public static RequestMethod[] values() {
            return (RequestMethod[])$VALUES.clone();
        }
    }

    public static final class RequestType
    extends Enum<RequestType> {
        private static final /* synthetic */ RequestType[] $VALUES;
        public static final /* enum */ RequestType GET_TRANSACTIONS;
        public static final /* enum */ RequestType REGISTER;
        public static final /* enum */ RequestType REGISTRATION_CODE;
        public static final /* enum */ RequestType UNREGISTER;
        private String mType;

        static {
            REGISTRATION_CODE = new RequestType("/getRegistrationCode");
            REGISTER = new RequestType("/register");
            GET_TRANSACTIONS = new RequestType("/getTransactions");
            UNREGISTER = new RequestType("/unregister");
            RequestType[] arrrequestType = new RequestType[]{REGISTRATION_CODE, REGISTER, GET_TRANSACTIONS, UNREGISTER};
            $VALUES = arrrequestType;
        }

        private RequestType(String string2) {
            this.mType = string2;
        }

        public static RequestType valueOf(String string) {
            return (RequestType)Enum.valueOf(RequestType.class, (String)string);
        }

        public static RequestType[] values() {
            return (RequestType[])$VALUES.clone();
        }

        public String getRequestType() {
            return this.mType;
        }
    }

    public static class TdsRequest {
        private a.a mCallbck;
        private JsonObject mData;
        private String mUrl;

        public TdsRequest() {
        }

        public TdsRequest(String string) {
            this.mUrl = string;
        }

        public a.a getCallbck() {
            return this.mCallbck;
        }

        public JsonObject getData() {
            return this.mData;
        }

        public byte[] getDataBytes() {
            if (this.mData == null) {
                return null;
            }
            try {
                byte[] arrby = this.mData.toString().getBytes("UTF8");
                return arrby;
            }
            catch (UnsupportedEncodingException unsupportedEncodingException) {
                unsupportedEncodingException.printStackTrace();
                c.e(McTdsRequestBuilder.TAG, "UnsupportedEncodingException..");
                return null;
            }
        }

        public String getUrl() {
            return this.mUrl;
        }

        public void setCallbck(a.a a2) {
            this.mCallbck = a2;
        }

        public void setData(JsonObject jsonObject) {
            this.mData = jsonObject;
        }

        public void setUrl(String string) {
            this.mUrl = string;
        }
    }

}

