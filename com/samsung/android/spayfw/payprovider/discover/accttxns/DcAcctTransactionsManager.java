package com.samsung.android.spayfw.payprovider.discover.accttxns;

import android.content.Context;
import android.os.Bundle;
import com.google.gson.Gson;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.discover.accttxns.models.AcctTransactionDetail;
import com.samsung.android.spayfw.payprovider.discover.accttxns.models.AcctTransactionDetailContextContainer;
import com.samsung.android.spayfw.payprovider.discover.accttxns.models.AcctTransactionRecord;
import com.samsung.android.spayfw.payprovider.discover.accttxns.models.AcctTransactionsResponseData;
import com.samsung.android.spayfw.payprovider.discover.accttxns.models.Element;
import com.samsung.android.spayfw.payprovider.discover.accttxns.models.PullTransactionContext;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAController;
import com.samsung.android.spayfw.payprovider.discover.tzsvc.DcTAException;
import com.samsung.android.spayfw.payprovider.discover.util.ISO8601Utils;
import com.samsung.android.spayfw.payprovider.discover.util.ISO8601Utils.Format;
import com.samsung.android.spayfw.payprovider.plcc.db.PlccCardSchema;
import com.samsung.android.spayfw.remoteservice.Request.C0413a;
import com.samsung.android.spayfw.remoteservice.Response;
import com.samsung.android.spayfw.remoteservice.tokenrequester.TokenRequesterClient;
import com.samsung.android.spayfw.remoteservice.tokenrequester.TokenRequesterRequest;
import com.samsung.android.spayfw.utils.Utils;
import com.samsung.android.spaytui.SpayTuiTAController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bouncycastle.asn1.x509.DisplayText;
import org.bouncycastle.math.ec.ECCurve;

/* renamed from: com.samsung.android.spayfw.payprovider.discover.accttxns.b */
public class DcAcctTransactionsManager {
    private static final Map<String, String> sq;
    private static final Map<String, String> ss;
    private final Context mContext;
    private String mF;
    private long mTokenId;
    private TransactionResponse sp;

    /* renamed from: com.samsung.android.spayfw.payprovider.discover.accttxns.b.a */
    private class DcAcctTransactionsManager extends C0413a<Response<AcctTransactionsResponseData>, AcctTransactionsRequester> {
        final /* synthetic */ DcAcctTransactionsManager su;

        private DcAcctTransactionsManager(DcAcctTransactionsManager dcAcctTransactionsManager) {
            this.su = dcAcctTransactionsManager;
        }

        public void m846a(int i, Response<AcctTransactionsResponseData> response) {
            int i2 = -7;
            Log.m287i("DCSDK_DcAcctTransactionsManager", "AcctTransactionsRequesterCB: onRequestComplete: status " + i);
            if (this.su.sp == null) {
                Log.m286e("DCSDK_DcAcctTransactionsManager", "AcctTransactionsRequesterCB: onRequestComplete: mAcctTxnRespCb object is null ");
                return;
            }
            ProviderResponseData providerResponseData = new ProviderResponseData();
            ProviderTokenKey providerTokenKey = new ProviderTokenKey(this.su.mTokenId);
            providerTokenKey.setTrTokenId(this.su.mF);
            if (i != DisplayText.DISPLAY_TEXT_MAXIMUM_SIZE) {
                switch (i) {
                    case SpayTuiTAController.ERROR_EXECUTE_FAIL /*-2*/:
                        i2 = -10;
                        break;
                    case ECCurve.COORD_AFFINE /*0*/:
                    case 503:
                        break;
                    case 401:
                    case 403:
                        i2 = -8;
                        break;
                    case 404:
                        i2 = -5;
                        break;
                    default:
                        i2 = -2;
                        break;
                }
                providerResponseData.setErrorCode(i2);
                this.su.sp.m344a(providerTokenKey, i2, null, providerResponseData);
            } else if (response == null || response.getResult() == null) {
                Log.m286e("DCSDK_DcAcctTransactionsManager", "AcctTransactionsRequesterCB: onRequestComplete: response is null");
                providerResponseData.setErrorCode(-7);
                this.su.sp.m344a(providerTokenKey, -7, null, providerResponseData);
            } else {
                this.su.sp.m344a(providerTokenKey, 0, (AcctTransactionsResponseData) response.getResult(), null);
            }
        }
    }

    public DcAcctTransactionsManager(Context context) {
        this.mContext = context;
    }

    public int m852a(long j, Bundle bundle, TransactionResponse transactionResponse) {
        this.sp = transactionResponse;
        this.mTokenId = j;
        String string = bundle.getString(PlccCardSchema.COLUMN_NAME_TR_TOKEN_ID);
        this.mF = string;
        String valueOf = String.valueOf(Utils.am(this.mContext) - 10000);
        TokenRequesterClient Q = TokenRequesterClient.m1126Q(this.mContext);
        TokenRequesterRequest acctTransactionsRequester = new AcctTransactionsRequester(Q, string, valueOf);
        Q.m1138a(acctTransactionsRequester, "credit/ds");
        acctTransactionsRequester.m836a(new DcAcctTransactionsManager());
        return 0;
    }

    public TransactionDetails m853a(long j, Object obj, List<byte[]> list) {
        try {
            PullTransactionContext pullTxnContext = ((Element) ((AcctTransactionsResponseData) obj).getElements().get(0)).getPullTxnContext();
            if ("true".equals(pullTxnContext.getTransactionsAvailable())) {
                String encryptedPayload = pullTxnContext.getSecureTransactionsList().getEncryptedPayload();
                Log.m285d("DCSDK_DcAcctTransactionsManager", "processTransactionData: " + pullTxnContext.toString() + ", Enc Payload (50): " + encryptedPayload.substring(0, 50));
                if (list == null) {
                    Log.m286e("DCSDK_DcAcctTransactionsManager", "processTransactionData: serverCertChain is null");
                    return null;
                }
                try {
                    TransactionDetails transactionDetails;
                    encryptedPayload = DcTAController.eu().m1044a(encryptedPayload, (List) list);
                    AcctTransactionDetailContextContainer acctTransactionDetailContextContainer = (AcctTransactionDetailContextContainer) new Gson().fromJson(encryptedPayload, AcctTransactionDetailContextContainer.class);
                    Log.m285d("DCSDK_DcAcctTransactionsManager", "Decrypted Data: " + encryptedPayload);
                    if (acctTransactionDetailContextContainer != null) {
                        List a = m849a(acctTransactionDetailContextContainer.getTransactionsList());
                        transactionDetails = new TransactionDetails();
                        transactionDetails.setTransactionData(a);
                    } else {
                        transactionDetails = null;
                    }
                    if (transactionDetails == null) {
                        return transactionDetails;
                    }
                    Log.m287i("DCSDK_DcAcctTransactionsManager", "TRec Count: " + transactionDetails.getTransactionData().size());
                    Log.m285d("DCSDK_DcAcctTransactionsManager", "processTransactionData: tdRecords - " + transactionDetails.toString());
                    return transactionDetails;
                } catch (DcTAException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            Log.m286e("DCSDK_DcAcctTransactionsManager", "processTransactionData: No new transactions");
            return null;
        } catch (NullPointerException e2) {
            Log.m286e("DCSDK_DcAcctTransactionsManager", "processTransactionData: NPE - " + e2.getMessage());
            e2.printStackTrace();
            Log.m286e("DCSDK_DcAcctTransactionsManager", "processTransactionData: failed");
            return null;
        }
    }

    private List<TransactionData> m849a(AcctTransactionRecord[] acctTransactionRecordArr) {
        List<TransactionData> arrayList = new ArrayList(acctTransactionRecordArr.length);
        for (AcctTransactionRecord acctTransactionRecord : acctTransactionRecordArr) {
            TransactionData transactionData = new TransactionData();
            AcctTransactionDetail transactionDetail = acctTransactionRecord.getTransactionDetail();
            transactionData.setAmount(transactionDetail.getAmount());
            transactionData.setCurrencyCode(transactionDetail.getCurrencyCode());
            transactionData.setMechantName(transactionDetail.getMerchantName());
            transactionData.setTransactionDate(ISO8601Utils.m1055a(Format.UTCMS_WithZ, Format.UTCS_WithZ, transactionDetail.getTransactionTimestamp()));
            transactionData.setTransactionType((String) sq.get(transactionDetail.getTransactionType().toLowerCase()));
            transactionData.setTransactionStatus((String) ss.get(transactionDetail.getAuthorizationStatus().toLowerCase()));
            transactionData.setTransactionId(acctTransactionRecord.getTransactionIdentifier());
            arrayList.add(transactionData);
        }
        return arrayList;
    }

    static {
        sq = new HashMap();
        ss = new HashMap();
        sq.put("purchase", TransactionData.TRANSACTION_TYPE_PURCHASE);
        sq.put("refund", TransactionData.TRANSACTION_TYPE_REFUND);
        ss.put("pending", TransactionData.TRANSACTION_STATUS_PENDING);
        ss.put("approved", TransactionData.TRANSACTION_STATUS_APPROVED);
        ss.put("refunded", TransactionData.TRANSACTION_STATUS_REFUNDED);
        ss.put("declined", TransactionData.TRANSACTION_STATUS_DECLINED);
    }
}
