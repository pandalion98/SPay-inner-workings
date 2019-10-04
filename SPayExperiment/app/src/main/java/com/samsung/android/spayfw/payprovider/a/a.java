/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.text.ParsePosition
 *  java.text.SimpleDateFormat
 *  java.util.ArrayList
 *  java.util.Arrays
 *  java.util.Collection
 *  java.util.Comparator
 *  java.util.Date
 *  java.util.HashSet
 *  java.util.Iterator
 *  java.util.List
 *  java.util.Locale
 *  java.util.TimeZone
 */
package com.samsung.android.spayfw.payprovider.a;

import android.content.Context;
import android.os.Bundle;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.d;
import com.samsung.android.spayfw.payprovider.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.payprovider.visa.b;
import com.samsung.android.spayfw.remoteservice.Request;
import com.samsung.android.spayfw.remoteservice.cashcard.models.CashCardInfo;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.j;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class a
extends b {
    private static Comparator<CashCardInfo.TransactionInfo> rW = new Comparator<CashCardInfo.TransactionInfo>(){

        public int a(CashCardInfo.TransactionInfo transactionInfo, CashCardInfo.TransactionInfo transactionInfo2) {
            long l2 = transactionInfo.getTime() - transactionInfo2.getTime();
            if (l2 > 0L) {
                return 1;
            }
            if (l2 < 0L) {
                return -1;
            }
            return transactionInfo.getType().compareTo(transactionInfo2.getType());
        }

        public /* synthetic */ int compare(Object object, Object object2) {
            return this.a((CashCardInfo.TransactionInfo)object, (CashCardInfo.TransactionInfo)object2);
        }
    };
    protected TokenRecordStorage jJ;
    protected com.samsung.android.spayfw.remoteservice.cashcard.a kT;
    protected String rX;
    protected String rY;
    protected CashCardInfo.TransactionInfo[] rZ;

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public a(Context context, String string, f f2) {
        super(context, "VI", f2);
        c.e("CashCardPayProvider", "CashCardPayProvider()");
        try {
            this.jJ = TokenRecordStorage.ae(this.mContext);
        }
        catch (Exception exception) {
            c.c("Processor", exception.getMessage(), exception);
            c.e("Processor", "Exception when initializing Dao");
            this.jJ = null;
        }
        this.kT = com.samsung.android.spayfw.remoteservice.cashcard.a.I(this.mContext);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void a(List<TransactionData> list, CashCardInfo.TransactionInfo[] arrtransactionInfo) {
        int n2 = arrtransactionInfo.length;
        int n3 = 0;
        while (n3 < n2) {
            CashCardInfo.TransactionInfo transactionInfo = arrtransactionInfo[n3];
            c.d("CashCardPayProvider", transactionInfo.toString());
            TransactionData transactionData = new TransactionData();
            transactionData.setAmount(transactionInfo.getAmount() + "");
            transactionData.setCurrencyCode(transactionInfo.getCurrency());
            transactionData.setMechantName(transactionInfo.getDetails());
            try {
                Date date = new Date();
                date.setTime(transactionInfo.getTime());
                c.d("CashCardPayProvider", date.toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone((String)"GMT"));
                String string = simpleDateFormat.format(date);
                c.d("CashCardPayProvider", string);
                transactionData.setTransactionDate(string);
            }
            catch (Exception exception) {
                c.e("CashCardPayProvider", exception.getMessage());
            }
            transactionData.setTransactionId(transactionInfo.getId());
            if (transactionInfo.getType().equalsIgnoreCase("Purchase")) {
                transactionData.setTransactionType("Purchase");
                transactionData.setTransactionStatus("Approved");
            } else if (transactionInfo.getType().equalsIgnoreCase("Refund")) {
                transactionData.setTransactionType("Refund");
                transactionData.setTransactionStatus("Refunded");
            } else if (transactionInfo.getType().equalsIgnoreCase("Reward")) {
                transactionData.setTransactionType("Reward");
                transactionData.setTransactionStatus("Approved");
            }
            list.add((Object)transactionData);
            ++n3;
        }
        return;
    }

    /*
     * Enabled aggressive block sorting
     */
    private void a(CashCardInfo.TransactionInfo[] arrtransactionInfo) {
        Arrays.sort((Object[])arrtransactionInfo, rW);
        HashSet hashSet = new HashSet();
        int n2 = arrtransactionInfo.length;
        int n3 = 0;
        while (n3 < n2) {
            CashCardInfo.TransactionInfo transactionInfo = arrtransactionInfo[n3];
            if (!hashSet.contains((Object)transactionInfo.getId())) {
                hashSet.add((Object)transactionInfo.getId());
            } else {
                String string = transactionInfo.getId();
                c.d("CashCardPayProvider", "Old Tid " + string);
                c.d("CashCardPayProvider", "Type " + transactionInfo.getType());
                while (hashSet.contains((Object)string)) {
                    string = Long.toString((long)(1L + Long.parseLong((String)string)));
                }
                c.d("CashCardPayProvider", "New Tid " + string);
                transactionInfo.setId(string);
                hashSet.add((Object)string);
            }
            ++n3;
        }
        return;
    }

    @Override
    public int getTransactionData(final Bundle bundle, final i i2) {
        com.samsung.android.spayfw.storage.models.a a2 = this.jJ.bq(this.mProviderTokenKey.getTrTokenId());
        if (a2 == null || a2.fx() == null || a2.fx().isEmpty()) {
            c.e("CashCardPayProvider", "Unable to find Token Id OR Cash Card Id from db " + this.mProviderTokenKey.getTrTokenId());
            return -4;
        }
        this.kT.b(a2.fx(), false).a(new Request.a<com.samsung.android.spayfw.remoteservice.c<CashCardInfo>, com.samsung.android.spayfw.remoteservice.cashcard.c>(){

            /*
             * Enabled aggressive block sorting
             */
            @Override
            public void a(int n2, com.samsung.android.spayfw.remoteservice.c<CashCardInfo> c2) {
                ErrorResponseData errorResponseData;
                c.d("CashCardPayProvider", "process : onRequestComplete: code: " + n2);
                e e2 = new e();
                switch (n2) {
                    default: {
                        errorResponseData = c2 != null ? c2.fa() : null;
                    }
                    case 200: {
                        int n3;
                        com.samsung.android.spayfw.remoteservice.tokenrequester.models.Collection<CashCardInfo.TransactionInfo> collection;
                        CashCardInfo.TransactionInfo[] arrtransactionInfo;
                        CashCardInfo cashCardInfo = c2 != null ? c2.getResult() : null;
                        if (cashCardInfo != null) {
                            a.this.rX = cashCardInfo.getFunds().getAmount() + "";
                            a.this.rY = cashCardInfo.getFunds().getCurrency();
                        }
                        if ((arrtransactionInfo = (collection = cashCardInfo != null ? cashCardInfo.getTransactions() : null) != null ? collection.getElements() : null) != null && arrtransactionInfo.length > 0) {
                            e2.setErrorCode(0);
                            a.this.a(arrtransactionInfo);
                            a.this.rZ = arrtransactionInfo;
                            c.i("CashCardPayProvider", "TransactionInfo is Same Fetch from Card Network");
                            int n4 = a.super.getTransactionData(bundle, i2);
                            if (n4 == 0) {
                                c.i("CashCardPayProvider", "Success in fetching TransactionInfo from Card Network");
                                return;
                            }
                            c.i("CashCardPayProvider", "Error in fetching TransactionInfo from Card Network");
                            e2.setErrorCode(n4);
                            e2.as("Error in fetching TransactionInfo from Card Network");
                            n3 = 0;
                        } else {
                            c.i("CashCardPayProvider", "TransactionInfo is NULL or EMPTY");
                            n3 = -5;
                            e2.setErrorCode(n3);
                            e2.as("CashCardInfo.TransactionInfo is NULL or EMPTY");
                        }
                        i2.a(a.this.mProviderTokenKey, n3, cashCardInfo, e2);
                        return;
                    }
                }
                e2.setErrorCode(-7);
                if (errorResponseData != null && errorResponseData.getMessage() != null) {
                    c.e("CashCardPayProvider", errorResponseData.getMessage());
                    e2.as(n2 + " : " + errorResponseData.getMessage());
                }
                i2.a(a.this.mProviderTokenKey, 0, null, e2);
            }
        });
        return 0;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    protected TransactionDetails processTransactionData(Object object) {
        if (object == null) {
            c.e("CashCardPayProvider", "Raw Transaction Data is empty");
            return null;
        }
        if (object instanceof CashCardInfo) {
            CashCardInfo cashCardInfo = (CashCardInfo)object;
            com.samsung.android.spayfw.remoteservice.tokenrequester.models.Collection<CashCardInfo.TransactionInfo> collection = cashCardInfo.getTransactions();
            CashCardInfo.TransactionInfo[] arrtransactionInfo = collection != null ? collection.getElements() : null;
            if (arrtransactionInfo != null && arrtransactionInfo.length > 0) {
                TransactionDetails transactionDetails = new TransactionDetails();
                ArrayList arrayList = new ArrayList();
                this.a((List<TransactionData>)arrayList, arrtransactionInfo);
                transactionDetails.setTransactionData((List<TransactionData>)arrayList);
                transactionDetails.setBalanceAmount(cashCardInfo.getFunds().getAmount() + "");
                transactionDetails.setBalanceCurrencyCode(cashCardInfo.getFunds().getCurrency());
                return transactionDetails;
            }
            c.e("CashCardPayProvider", "Raw Transaction Data is empty");
            return null;
        }
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setBalanceAmount(this.rX);
        transactionDetails.setBalanceCurrencyCode(this.rY);
        ArrayList arrayList = new ArrayList();
        this.a((List<TransactionData>)arrayList, this.rZ);
        transactionDetails.setTransactionData((List<TransactionData>)arrayList);
        TransactionDetails transactionDetails2 = super.processTransactionData(object);
        if (transactionDetails2 == null) {
            c.e("CashCardPayProvider", "Transaction Details is null from Card Network Provider");
            return transactionDetails;
        }
        List<TransactionData> list = transactionDetails2.getTransactionData();
        if (list == null) {
            c.e("CashCardPayProvider", "Transaction Data List is null from Card Network Provider");
            return transactionDetails;
        }
        Iterator iterator = list.iterator();
        do {
            if (!iterator.hasNext()) {
                c.d("CashCardPayProvider", "Converted List " + list.toString());
                transactionDetails.getTransactionData().addAll(list);
                return transactionDetails;
            }
            TransactionData transactionData = (TransactionData)iterator.next();
            c.d("CashCardPayProvider", transactionData.toString());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone((String)"GMT"));
            transactionData.setTransactionId(Long.toString((long)simpleDateFormat.parse(transactionData.getTransactionDate(), new ParsePosition(0)).getTime()));
            if (transactionData.getTransactionType().equalsIgnoreCase("Purchase")) {
                if (transactionData.getTransactionStatus().equalsIgnoreCase("Refunded")) {
                    transactionData.setAmount(transactionData.getAmount().replace((CharSequence)"-", (CharSequence)""));
                } else {
                    String string = transactionData.getAmount();
                    transactionData.setAmount("-" + string);
                }
            } else if (transactionData.getTransactionType().equalsIgnoreCase("Refund")) {
                transactionData.setAmount(transactionData.getAmount().replace((CharSequence)"-", (CharSequence)""));
            }
            c.d("CashCardPayProvider", transactionData.toString());
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void updateRequestStatus(d d2) {
        try {
            c.d("CashCardPayProvider", "updateRequestStatus : " + d2.getRequestType() + " " + d2.ci());
            if (d2.ck() != null) {
                c.d("CashCardPayProvider", "updateRequestStatus : " + d2.ck().cn());
            }
            switch (d2.ci()) {
                default: {
                    c.i("CashCardPayProvider", "Ignore status");
                    return;
                }
                case 0: 
            }
            if (d2.getRequestType() != 22) return;
            {
                TokenReport tokenReport = new TokenReport(null, d2.ck().getTrTokenId(), "DISPOSED");
                tokenReport.setEvent("STATUS_CHANGE");
                com.samsung.android.spayfw.storage.models.a a2 = this.jJ.bq(this.mProviderTokenKey.getTrTokenId());
                if (a2 != null) {
                    this.kT.a(a2.fx(), tokenReport).fe();
                    return;
                }
                c.e("CashCardPayProvider", "Token Record is null");
                return;
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return;
        }
    }

}

