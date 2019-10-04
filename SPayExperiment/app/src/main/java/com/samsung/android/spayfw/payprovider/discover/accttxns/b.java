/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.HashMap
 *  java.util.List
 *  java.util.Map
 */
package com.samsung.android.spayfw.payprovider.discover.accttxns;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.discover.accttxns.models.AcctTransactionDetail;
import com.samsung.android.spayfw.payprovider.discover.accttxns.models.AcctTransactionRecord;
import com.samsung.android.spayfw.payprovider.discover.accttxns.models.AcctTransactionsResponseData;
import com.samsung.android.spayfw.payprovider.discover.util.ISO8601Utils;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.m;
import com.samsung.android.spayfw.utils.h;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class b {
    private static final Map<String, String> sq = new HashMap();
    private static final Map<String, String> ss = new HashMap();
    private final Context mContext;
    private String mF;
    private long mTokenId;
    private i sp;

    static {
        sq.put((Object)"purchase", (Object)"Purchase");
        sq.put((Object)"refund", (Object)"Refund");
        ss.put((Object)"pending", (Object)"Pending");
        ss.put((Object)"approved", (Object)"Approved");
        ss.put((Object)"refunded", (Object)"Refunded");
        ss.put((Object)"declined", (Object)"Declined");
    }

    public b(Context context) {
        this.mContext = context;
    }

    private List<TransactionData> a(AcctTransactionRecord[] arracctTransactionRecord) {
        ArrayList arrayList = new ArrayList(arracctTransactionRecord.length);
        for (AcctTransactionRecord acctTransactionRecord : arracctTransactionRecord) {
            TransactionData transactionData = new TransactionData();
            AcctTransactionDetail acctTransactionDetail = acctTransactionRecord.getTransactionDetail();
            transactionData.setAmount(acctTransactionDetail.getAmount());
            transactionData.setCurrencyCode(acctTransactionDetail.getCurrencyCode());
            transactionData.setMechantName(acctTransactionDetail.getMerchantName());
            transactionData.setTransactionDate(ISO8601Utils.a(ISO8601Utils.Format.zb, ISO8601Utils.Format.zc, acctTransactionDetail.getTransactionTimestamp()));
            transactionData.setTransactionType((String)sq.get((Object)acctTransactionDetail.getTransactionType().toLowerCase()));
            transactionData.setTransactionStatus((String)ss.get((Object)acctTransactionDetail.getAuthorizationStatus().toLowerCase()));
            transactionData.setTransactionId(acctTransactionRecord.getTransactionIdentifier());
            arrayList.add((Object)transactionData);
        }
        return arrayList;
    }

    public int a(long l2, Bundle bundle, i i2) {
        String string;
        this.sp = i2;
        this.mTokenId = l2;
        this.mF = string = bundle.getString("trTokenId");
        String string2 = String.valueOf((long)(h.am(this.mContext) - 10000L));
        l l3 = l.Q(this.mContext);
        com.samsung.android.spayfw.payprovider.discover.accttxns.a a2 = new com.samsung.android.spayfw.payprovider.discover.accttxns.a(l3, string, string2);
        l3.a(a2, "credit/ds");
        a2.a(new a());
        return 0;
    }

    /*
     * Exception decompiling
     */
    public TransactionDetails a(long var1_1, Object var3_2, List<byte[]> var4_3) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [0[TRYBLOCK]], but top level block is 4[CATCHBLOCK]
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

    private class a
    extends Request.a<com.samsung.android.spayfw.remoteservice.c<AcctTransactionsResponseData>, com.samsung.android.spayfw.payprovider.discover.accttxns.a> {
        private a() {
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void a(int n2, com.samsung.android.spayfw.remoteservice.c<AcctTransactionsResponseData> c2) {
            int n3 = -7;
            c.i("DCSDK_DcAcctTransactionsManager", "AcctTransactionsRequesterCB: onRequestComplete: status " + n2);
            if (b.this.sp == null) {
                c.e("DCSDK_DcAcctTransactionsManager", "AcctTransactionsRequesterCB: onRequestComplete: mAcctTxnRespCb object is null ");
                return;
            }
            e e2 = new e();
            f f2 = new f(b.this.mTokenId);
            f2.setTrTokenId(b.this.mF);
            if (n2 != 200) {
                switch (n2) {
                    default: {
                        n3 = -2;
                        break;
                    }
                    case 404: {
                        n3 = -5;
                        break;
                    }
                    case 401: 
                    case 403: {
                        n3 = -8;
                    }
                    case 0: 
                    case 503: {
                        break;
                    }
                    case -2: {
                        n3 = -10;
                    }
                }
                e2.setErrorCode(n3);
                b.this.sp.a(f2, n3, null, e2);
                return;
            }
            if (c2 != null && c2.getResult() != null) {
                AcctTransactionsResponseData acctTransactionsResponseData = c2.getResult();
                b.this.sp.a(f2, 0, acctTransactionsResponseData, null);
                return;
            }
            c.e("DCSDK_DcAcctTransactionsManager", "AcctTransactionsRequesterCB: onRequestComplete: response is null");
            e2.setErrorCode(n3);
            b.this.sp.a(f2, n3, null, e2);
        }
    }

}

