package com.samsung.android.spayfw.payprovider.p014a;

import android.content.Context;
import android.os.Bundle;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderRequestStatus;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.visa.VisaPayProvider;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.cashcard.CashCardClient;
import com.samsung.android.spayfw.remoteservice.cashcard.QueryCashCardRequest;
import com.samsung.android.spayfw.remoteservice.cashcard.models.CashCardInfo;
import com.samsung.android.spayfw.remoteservice.cashcard.models.CashCardInfo.TransactionInfo;
import com.samsung.android.spayfw.remoteservice.models.ErrorResponseData;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.Collection;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.TokenReport;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.payprovider.a.a */
public class CashCardPayProvider extends VisaPayProvider {
    private static Comparator<TransactionInfo> rW;
    protected TokenRecordStorage jJ;
    protected CashCardClient kT;
    protected String rX;
    protected String rY;
    protected TransactionInfo[] rZ;

    /* renamed from: com.samsung.android.spayfw.payprovider.a.a.1 */
    static class CashCardPayProvider implements Comparator<TransactionInfo> {
        CashCardPayProvider() {
        }

        public /* synthetic */ int compare(Object obj, Object obj2) {
            return m750a((TransactionInfo) obj, (TransactionInfo) obj2);
        }

        public int m750a(TransactionInfo transactionInfo, TransactionInfo transactionInfo2) {
            long time = transactionInfo.getTime() - transactionInfo2.getTime();
            if (time > 0) {
                return 1;
            }
            if (time < 0) {
                return -1;
            }
            return transactionInfo.getType().compareTo(transactionInfo2.getType());
        }
    }

    /* renamed from: com.samsung.android.spayfw.payprovider.a.a.2 */
    class CashCardPayProvider extends C0413a<Response<CashCardInfo>, QueryCashCardRequest> {
        final /* synthetic */ TransactionResponse pu;
        final /* synthetic */ Bundle sa;
        final /* synthetic */ CashCardPayProvider sd;

        CashCardPayProvider(CashCardPayProvider cashCardPayProvider, Bundle bundle, TransactionResponse transactionResponse) {
            this.sd = cashCardPayProvider;
            this.sa = bundle;
            this.pu = transactionResponse;
        }

        public void m751a(int i, Response<CashCardInfo> response) {
            Log.m285d("CashCardPayProvider", "process : onRequestComplete: code: " + i);
            ProviderResponseData providerResponseData = new ProviderResponseData();
            switch (i) {
                case DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE /*200*/:
                    Collection transactions;
                    TransactionInfo[] transactionInfoArr;
                    int i2;
                    CashCardInfo cashCardInfo = response != null ? (CashCardInfo) response.getResult() : null;
                    if (cashCardInfo != null) {
                        this.sd.rX = cashCardInfo.getFunds().getAmount() + BuildConfig.FLAVOR;
                        this.sd.rY = cashCardInfo.getFunds().getCurrency();
                    }
                    if (cashCardInfo != null) {
                        transactions = cashCardInfo.getTransactions();
                    } else {
                        transactions = null;
                    }
                    if (transactions != null) {
                        transactionInfoArr = (TransactionInfo[]) transactions.getElements();
                    } else {
                        transactionInfoArr = null;
                    }
                    if (transactionInfoArr == null || transactionInfoArr.length <= 0) {
                        Log.m287i("CashCardPayProvider", "TransactionInfo is NULL or EMPTY");
                        i2 = -5;
                        providerResponseData.setErrorCode(-5);
                        providerResponseData.as("CashCardInfo.TransactionInfo is NULL or EMPTY");
                    } else {
                        providerResponseData.setErrorCode(0);
                        this.sd.m758a(transactionInfoArr);
                        this.sd.rZ = transactionInfoArr;
                        Log.m287i("CashCardPayProvider", "TransactionInfo is Same Fetch from Card Network");
                        i2 = super.getTransactionData(this.sa, this.pu);
                        if (i2 == 0) {
                            Log.m287i("CashCardPayProvider", "Success in fetching TransactionInfo from Card Network");
                            return;
                        }
                        Log.m287i("CashCardPayProvider", "Error in fetching TransactionInfo from Card Network");
                        providerResponseData.setErrorCode(i2);
                        providerResponseData.as("Error in fetching TransactionInfo from Card Network");
                        i2 = 0;
                    }
                    this.pu.m344a(this.sd.mProviderTokenKey, i2, cashCardInfo, providerResponseData);
                default:
                    ErrorResponseData fa;
                    if (response != null) {
                        fa = response.fa();
                    } else {
                        fa = null;
                    }
                    providerResponseData.setErrorCode(-7);
                    if (!(fa == null || fa.getMessage() == null)) {
                        Log.m286e("CashCardPayProvider", fa.getMessage());
                        providerResponseData.as(i + " : " + fa.getMessage());
                    }
                    this.pu.m344a(this.sd.mProviderTokenKey, 0, null, providerResponseData);
            }
        }
    }

    static {
        rW = new CashCardPayProvider();
    }

    public CashCardPayProvider(Context context, String str, ProviderTokenKey providerTokenKey) {
        super(context, PaymentFramework.CARD_BRAND_VISA, providerTokenKey);
        Log.m286e("CashCardPayProvider", "CashCardPayProvider()");
        try {
            this.jJ = TokenRecordStorage.ae(this.mContext);
        } catch (Throwable e) {
            Log.m284c("Processor", e.getMessage(), e);
            Log.m286e("Processor", "Exception when initializing Dao");
            this.jJ = null;
        }
        this.kT = CashCardClient.m1172I(this.mContext);
    }

    public int getTransactionData(Bundle bundle, TransactionResponse transactionResponse) {
        TokenRecord bq = this.jJ.bq(this.mProviderTokenKey.getTrTokenId());
        if (bq == null || bq.fx() == null || bq.fx().isEmpty()) {
            Log.m286e("CashCardPayProvider", "Unable to find Token Id OR Cash Card Id from db " + this.mProviderTokenKey.getTrTokenId());
            return -4;
        }
        this.kT.m1177b(bq.fx(), false).m836a(new CashCardPayProvider(this, bundle, transactionResponse));
        return 0;
    }

    protected TransactionDetails processTransactionData(Object obj) {
        if (obj == null) {
            Log.m286e("CashCardPayProvider", "Raw Transaction Data is empty");
            return null;
        } else if (obj instanceof CashCardInfo) {
            CashCardInfo cashCardInfo = (CashCardInfo) obj;
            Collection transactions = cashCardInfo.getTransactions();
            TransactionInfo[] transactionInfoArr = transactions != null ? (TransactionInfo[]) transactions.getElements() : null;
            if (transactionInfoArr == null || transactionInfoArr.length <= 0) {
                Log.m286e("CashCardPayProvider", "Raw Transaction Data is empty");
                return null;
            }
            r0 = new TransactionDetails();
            List arrayList = new ArrayList();
            m757a(arrayList, transactionInfoArr);
            r0.setTransactionData(arrayList);
            r0.setBalanceAmount(cashCardInfo.getFunds().getAmount() + BuildConfig.FLAVOR);
            r0.setBalanceCurrencyCode(cashCardInfo.getFunds().getCurrency());
            return r0;
        } else {
            TransactionDetails transactionDetails = new TransactionDetails();
            transactionDetails.setBalanceAmount(this.rX);
            transactionDetails.setBalanceCurrencyCode(this.rY);
            List arrayList2 = new ArrayList();
            m757a(arrayList2, this.rZ);
            transactionDetails.setTransactionData(arrayList2);
            r0 = super.processTransactionData(obj);
            if (r0 == null) {
                Log.m286e("CashCardPayProvider", "Transaction Details is null from Card Network Provider");
                return transactionDetails;
            }
            java.util.Collection<TransactionData> transactionData = r0.getTransactionData();
            if (transactionData == null) {
                Log.m286e("CashCardPayProvider", "Transaction Data List is null from Card Network Provider");
                return transactionDetails;
            }
            for (TransactionData transactionData2 : transactionData) {
                Log.m285d("CashCardPayProvider", transactionData2.toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                transactionData2.setTransactionId(Long.toString(simpleDateFormat.parse(transactionData2.getTransactionDate(), new ParsePosition(0)).getTime()));
                if (transactionData2.getTransactionType().equalsIgnoreCase(TransactionData.TRANSACTION_TYPE_PURCHASE)) {
                    if (transactionData2.getTransactionStatus().equalsIgnoreCase(TransactionData.TRANSACTION_STATUS_REFUNDED)) {
                        transactionData2.setAmount(transactionData2.getAmount().replace(HCEClientConstants.TAG_KEY_SEPARATOR, BuildConfig.FLAVOR));
                    } else {
                        transactionData2.setAmount(HCEClientConstants.TAG_KEY_SEPARATOR + transactionData2.getAmount());
                    }
                } else if (transactionData2.getTransactionType().equalsIgnoreCase(TransactionData.TRANSACTION_TYPE_REFUND)) {
                    transactionData2.setAmount(transactionData2.getAmount().replace(HCEClientConstants.TAG_KEY_SEPARATOR, BuildConfig.FLAVOR));
                }
                Log.m285d("CashCardPayProvider", transactionData2.toString());
            }
            Log.m285d("CashCardPayProvider", "Converted List " + transactionData.toString());
            transactionDetails.getTransactionData().addAll(transactionData);
            return transactionDetails;
        }
    }

    public void updateRequestStatus(ProviderRequestStatus providerRequestStatus) {
        try {
            Log.m285d("CashCardPayProvider", "updateRequestStatus : " + providerRequestStatus.getRequestType() + " " + providerRequestStatus.ci());
            if (providerRequestStatus.ck() != null) {
                Log.m285d("CashCardPayProvider", "updateRequestStatus : " + providerRequestStatus.ck().cn());
            }
            switch (providerRequestStatus.ci()) {
                case ECCurve.COORD_AFFINE /*0*/:
                    if (providerRequestStatus.getRequestType() == 22) {
                        TokenReport tokenReport = new TokenReport(null, providerRequestStatus.ck().getTrTokenId(), TokenStatus.DISPOSED);
                        tokenReport.setEvent(PushMessage.TYPE_STATUS_CHANGE);
                        TokenRecord bq = this.jJ.bq(this.mProviderTokenKey.getTrTokenId());
                        if (bq != null) {
                            this.kT.m1175a(bq.fx(), tokenReport).fe();
                            return;
                        } else {
                            Log.m286e("CashCardPayProvider", "Token Record is null");
                            return;
                        }
                    }
                    return;
                default:
                    Log.m287i("CashCardPayProvider", "Ignore status");
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        e.printStackTrace();
    }

    private void m757a(List<TransactionData> list, TransactionInfo[] transactionInfoArr) {
        for (TransactionInfo transactionInfo : transactionInfoArr) {
            Log.m285d("CashCardPayProvider", transactionInfo.toString());
            TransactionData transactionData = new TransactionData();
            transactionData.setAmount(transactionInfo.getAmount() + BuildConfig.FLAVOR);
            transactionData.setCurrencyCode(transactionInfo.getCurrency());
            transactionData.setMechantName(transactionInfo.getDetails());
            try {
                Date date = new Date();
                date.setTime(transactionInfo.getTime());
                Log.m285d("CashCardPayProvider", date.toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                String format = simpleDateFormat.format(date);
                Log.m285d("CashCardPayProvider", format);
                transactionData.setTransactionDate(format);
            } catch (Exception e) {
                Log.m286e("CashCardPayProvider", e.getMessage());
            }
            transactionData.setTransactionId(transactionInfo.getId());
            if (transactionInfo.getType().equalsIgnoreCase(TransactionData.TRANSACTION_TYPE_PURCHASE)) {
                transactionData.setTransactionType(TransactionData.TRANSACTION_TYPE_PURCHASE);
                transactionData.setTransactionStatus(TransactionData.TRANSACTION_STATUS_APPROVED);
            } else if (transactionInfo.getType().equalsIgnoreCase(TransactionData.TRANSACTION_TYPE_REFUND)) {
                transactionData.setTransactionType(TransactionData.TRANSACTION_TYPE_REFUND);
                transactionData.setTransactionStatus(TransactionData.TRANSACTION_STATUS_REFUNDED);
            } else if (transactionInfo.getType().equalsIgnoreCase(TransactionData.TRANSACTION_TYPE_REWARD)) {
                transactionData.setTransactionType(TransactionData.TRANSACTION_TYPE_REWARD);
                transactionData.setTransactionStatus(TransactionData.TRANSACTION_STATUS_APPROVED);
            }
            list.add(transactionData);
        }
    }

    private void m758a(TransactionInfo[] transactionInfoArr) {
        Arrays.sort(transactionInfoArr, rW);
        HashSet hashSet = new HashSet();
        for (TransactionInfo transactionInfo : transactionInfoArr) {
            if (hashSet.contains(transactionInfo.getId())) {
                String id = transactionInfo.getId();
                Log.m285d("CashCardPayProvider", "Old Tid " + id);
                Log.m285d("CashCardPayProvider", "Type " + transactionInfo.getType());
                while (hashSet.contains(id)) {
                    id = Long.toString(Long.parseLong(id) + 1);
                }
                Log.m285d("CashCardPayProvider", "New Tid " + id);
                transactionInfo.setId(id);
                hashSet.add(id);
            } else {
                hashSet.add(transactionInfo.getId());
            }
        }
    }
}
