/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.visa.inapp;

import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.payprovider.visa.e;
import com.samsung.android.spayfw.payprovider.visa.inapp.models.GenCryptogramResponseData;
import com.samsung.android.spayfw.payprovider.visa.inapp.models.InAppData;
import com.samsung.android.spayfw.remoteservice.c;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Data;
import com.samsung.android.spayfw.utils.h;
import com.samsung.android.visasdk.facade.data.PaymentDataRequest;

public class InAppPayment {
    public static final String ECOM_TRANSACTION_TYPE = "ECOM";
    public static final String RECURRING_TRANSACTION_TYPE = "RECURRING";

    private static c<Data> a(Context context, String string, PaymentDataRequest paymentDataRequest) {
        if (context == null || string == null || paymentDataRequest == null) {
            Log.e("InAppPayment", "tokenId or requestData null");
            return null;
        }
        Log.d("InAppPayment", "tokenId :" + string);
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        Data data = new Data();
        data.setData(gson.toJsonTree((Object)paymentDataRequest).getAsJsonObject());
        return e.G(context).a(string, data).eS();
    }

    public static InAppData buildInAppPaymentData(Context context, PaymentNetworkProvider.InAppDetailedTransactionInfo inAppDetailedTransactionInfo, GenCryptogramResponseData genCryptogramResponseData) {
        if (context == null || genCryptogramResponseData == null || inAppDetailedTransactionInfo == null) {
            Log.e("InAppPayment", " buildInAppPaymentData: input are null");
            return null;
        }
        InAppData inAppData = new InAppData();
        inAppData.setAmount(inAppDetailedTransactionInfo.getAmount());
        inAppData.setCurrency_code(inAppDetailedTransactionInfo.getCurrencyCode());
        inAppData.setUtc(String.valueOf((long)h.am(context)));
        GenCryptogramResponseData.CryptogramInfo cryptogramInfo = genCryptogramResponseData.getCryptogramInfo();
        if (cryptogramInfo == null || cryptogramInfo.getCryptogram() == null) {
            Log.e("InAppPayment", " cryptogram: empty");
            return null;
        }
        inAppData.setCryptogram(cryptogramInfo.getCryptogram());
        inAppData.setEci_indicator(cryptogramInfo.getEci());
        GenCryptogramResponseData.TokenInfo tokenInfo = genCryptogramResponseData.getTokenInfo();
        if (tokenInfo == null || tokenInfo.getEncTokenInfo() == null || tokenInfo.getTokenExpirationDate() == null) {
            Log.e("InAppPayment", " tokenInfo: empty");
            return null;
        }
        inAppData.setTokenPANExpiration(tokenInfo.getTokenExpirationDate());
        return inAppData;
    }

    /*
     * Exception decompiling
     */
    public static GenCryptogramResponseData getCryptogramInfo(Context var0, String var1_1, PaymentDataRequest var2_2) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Started 2 blocks at once
        // org.benf.cfr.reader.b.a.a.j.b(Op04StructuredStatement.java:409)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:487)
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
}

