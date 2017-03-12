package com.samsung.android.spayfw.core.p005a;

import android.content.Context;
import android.os.Bundle;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.TokenStatus;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.core.Card;
import com.samsung.android.spayfw.core.retry.TransactionDetailsRetryRequester;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.ProviderResponseData;
import com.samsung.android.spayfw.payprovider.ProviderTokenKey;
import com.samsung.android.spayfw.payprovider.TransactionResponse;
import com.samsung.android.spayfw.payprovider.plcc.db.PlccCardSchema;
import com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: TransactionDetailsRetriever */
/* renamed from: com.samsung.android.spayfw.core.a.aa */
public class aa extends Processor {
    protected PushMessage kU;
    protected IPushMessageCallback kV;
    protected int mCount;
    protected ITransactionDetailsCallback mG;
    protected long mH;
    protected long mI;
    protected String mTokenId;

    /* renamed from: com.samsung.android.spayfw.core.a.aa.1 */
    class TransactionDetailsRetriever implements Comparator<TransactionData> {
        final /* synthetic */ SimpleDateFormat mJ;
        final /* synthetic */ aa mK;

        TransactionDetailsRetriever(aa aaVar, SimpleDateFormat simpleDateFormat) {
            this.mK = aaVar;
            this.mJ = simpleDateFormat;
        }

        public int compare(TransactionData transactionData, TransactionData transactionData2) {
            try {
                return this.mJ.parse(transactionData.getTransactionDate()).compareTo(this.mJ.parse(transactionData2.getTransactionDate()));
            } catch (Throwable e) {
                Log.m284c("TransactionDetailsRetriever", e.getMessage(), e);
                return 0;
            }
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.a.aa.a */
    private class TransactionDetailsRetriever implements TransactionResponse {
        final /* synthetic */ aa mK;

        private TransactionDetailsRetriever(aa aaVar) {
            this.mK = aaVar;
        }

        public void m345a(ProviderTokenKey providerTokenKey, int i, Object obj, ProviderResponseData providerResponseData) {
            ProviderResponseData providerResponseData2 = null;
            int i2 = -3;
            Log.m285d("TransactionDetailsRetriever", "PayProviderTransactionResponse onComplete: status: " + i);
            Card r = this.mK.iJ.m559r(this.mK.mTokenId);
            if (r == null) {
                Log.m286e("TransactionDetailsRetriever", "processTransaction : unable to get Card object :" + this.mK.mTokenId);
                this.mK.notifyError(-5);
            } else if (i != 0 || obj == null) {
                Log.m286e("TransactionDetailsRetriever", " pay provider returns error. can't get transaction");
                if (i == -5) {
                    i2 = -6;
                } else if (i == -7) {
                    i2 = -9;
                } else if (i == -8) {
                    i2 = -4;
                    TokenRecord bq = this.mK.jJ.bq(this.mK.mTokenId);
                    if (bq != null) {
                        bq.m1254i(false);
                        this.mK.jJ.m1230d(bq);
                    }
                } else if (i == -10) {
                    i2 = PaymentFramework.RESULT_CODE_JWT_TOKEN_INVALID;
                } else if (obj != null) {
                    i2 = -1;
                }
                this.mK.notifyError(i2);
                if (this.mK.kU != null) {
                    this.mK.m335b(this.mK.kU.getNotificationId(), this.mK.mTokenId, r.ac().getTokenStatus(), PushMessage.TYPE_TRANSACTION, Card.m574y(r.getCardBrand()), providerResponseData, false);
                }
            } else {
                TransactionDetails processTransactionDataTA = r.ad().processTransactionDataTA(obj);
                r.ad().checkIfReplenishmentNeeded(null);
                if (processTransactionDataTA != null) {
                    this.mK.m353a(processTransactionDataTA);
                } else {
                    this.mK.notifyError(-3);
                }
                if (this.mK.kU != null) {
                    JsonArray jsonArray = new JsonArray();
                    String a = this.mK.bi();
                    if (!(processTransactionDataTA == null || processTransactionDataTA.getTransactionData() == null || a == null)) {
                        for (TransactionData transactionData : processTransactionDataTA.getTransactionData()) {
                            if (transactionData != null) {
                                JsonElement jsonObject = new JsonObject();
                                if (a.contains("txnTimestamp")) {
                                    jsonObject.addProperty("txnTimestamp", transactionData.getTransactionDate());
                                }
                                if (a.contains("merchantName")) {
                                    jsonObject.addProperty("merchantName", transactionData.getMechantName());
                                }
                                if (a.contains(IdvMethod.EXTRA_AMOUNT)) {
                                    jsonObject.addProperty(IdvMethod.EXTRA_AMOUNT, transactionData.getAmount());
                                }
                                if (a.contains("currency")) {
                                    jsonObject.addProperty("currency", transactionData.getCurrencyCode());
                                }
                                if (a.contains(PaymentFramework.EXTRA_TRANSACTION_TYPE)) {
                                    jsonObject.addProperty(PaymentFramework.EXTRA_TRANSACTION_TYPE, transactionData.getTransactionType());
                                }
                                if (a.contains("txnReceiptId")) {
                                    jsonObject.addProperty("txnReceiptId", transactionData.getTransactionId());
                                }
                                jsonObject.addProperty("updatedTransaction", Boolean.valueOf(this.mK.m349a(transactionData)));
                                jsonArray.add(jsonObject);
                            }
                        }
                    }
                    JsonObject a2 = this.mK.m346a(jsonArray);
                    if (a2 != null) {
                        providerResponseData2 = new ProviderResponseData();
                        providerResponseData2.m1057b(a2);
                    }
                }
                this.mK.m352b(processTransactionDataTA);
                if (this.mK.kU != null) {
                    this.mK.m331a(this.mK.kU.getNotificationId(), this.mK.mTokenId, TokenStatus.ACTIVE, PushMessage.TYPE_TRANSACTION, Card.m574y(r.getCardBrand()), providerResponseData2, false);
                }
            }
        }
    }

    public aa(Context context, String str, long j, long j2, int i, ITransactionDetailsCallback iTransactionDetailsCallback) {
        super(context);
        this.mTokenId = str;
        this.mG = iTransactionDetailsCallback;
        this.mH = j;
        this.mI = j2;
        this.mCount = i;
    }

    aa(Context context, String str, PushMessage pushMessage, IPushMessageCallback iPushMessageCallback) {
        super(context);
        this.mTokenId = str;
        this.kV = iPushMessageCallback;
        this.kU = pushMessage;
    }

    public void process() {
        Log.m285d("TransactionDetailsRetriever", "process : tokenId " + this.mTokenId);
        Card r = this.iJ.m559r(this.mTokenId);
        if (r == null) {
            Log.m286e("TransactionDetailsRetriever", "Unable to get card based on tokenId. ignore request");
            notifyError(-5);
            return;
        }
        String transactionUrl;
        if (this.kU != null) {
            transactionUrl = this.kU.getTransactionUrl();
        } else {
            transactionUrl = null;
        }
        String b = this.jJ.m1226b(TokenColumn.TRANSACTION_URL, r.ac().getTokenId());
        if (transactionUrl != null) {
            if (b == null) {
                Log.m287i("TransactionDetailsRetriever", "Update Transact URL in DB");
                TokenRecord bq = this.jJ.bq(r.ac().getTokenId());
                if (bq != null) {
                    bq.bv(transactionUrl);
                    this.jJ.m1230d(bq);
                }
            }
            b = transactionUrl;
        } else if (b != null) {
            transactionUrl = b;
        } else {
            String str = b;
            b = transactionUrl;
            transactionUrl = str;
        }
        Bundle bundle = new Bundle();
        bundle.putString(PlccCardSchema.COLUMN_NAME_TR_TOKEN_ID, this.mTokenId);
        bundle.putString("transactionUrl", transactionUrl);
        bundle.putString("pushtransactionUrl", b);
        bundle.putLong("startTimestamp", this.mH);
        bundle.putLong("endTimestamp", this.mI);
        bundle.putInt("count", this.mCount);
        if (this.kU != null) {
            bundle.putString("authorizationCode", this.kU.getTransactionCredentials());
        }
        int transactionDataTA = r.ad().getTransactionDataTA(bundle, new TransactionDetailsRetriever());
        if (transactionDataTA != 0) {
            Log.m286e("TransactionDetailsRetriever", " pay provider returns error. can't get transaction");
            ProviderResponseData providerResponseData = new ProviderResponseData();
            providerResponseData.setErrorCode(transactionDataTA);
            notifyError(-1);
            if (this.kU != null) {
                m335b(this.kU.getNotificationId(), this.mTokenId, TokenStatus.ACTIVE, PushMessage.TYPE_TRANSACTION, Card.m574y(r.getCardBrand()), providerResponseData, false);
            }
        }
    }

    protected void notifyError(int i) {
        try {
            if (this.mG != null) {
                Log.m287i("TransactionDetailsRetriever", "Invoking Error Transaction Callback " + i);
                this.mG.onFail(this.mTokenId, i);
            } else if (this.kV != null) {
                Log.m287i("TransactionDetailsRetriever", "Invoking Error Push Callback " + i);
                this.kV.onFail(this.kU.getNotificationId(), i);
            } else {
                Log.m286e("TransactionDetailsRetriever", "No Callbacks");
            }
        } catch (Throwable e) {
            Log.m284c("TransactionDetailsRetriever", e.getMessage(), e);
        }
    }

    protected void m353a(TransactionDetails transactionDetails) {
        try {
            if (this.mG != null) {
                Log.m287i("TransactionDetailsRetriever", "Invoking Success Transaction Callback");
                this.mG.onTransactionUpdate(this.mTokenId, transactionDetails);
            } else if (this.kV != null) {
                Log.m285d("TransactionDetailsRetriever", "Oob value = " + this.kU.getOob());
                Log.m287i("TransactionDetailsRetriever", "Invoking Success Push Callback");
                this.kV.onTransactionUpdate(this.kU.getNotificationId(), this.mTokenId, transactionDetails, this.kU.getOob());
            } else {
                Log.m286e("TransactionDetailsRetriever", "No Callbacks");
            }
            TransactionDetailsRetryRequester.m679w(this.mContext).remove(this.mTokenId);
        } catch (Throwable e) {
            Log.m284c("TransactionDetailsRetriever", e.getMessage(), e);
        }
    }

    private boolean m349a(TransactionData transactionData) {
        TokenRecord bq = this.jJ.bq(this.kU.getTokenId());
        if (bq == null) {
            Log.m286e("TransactionDetailsRetriever", "Token Record is NULL");
            return false;
        }
        List fC = bq.fC();
        Log.m285d("TransactionDetailsRetriever", "TIDs : " + fC);
        if (fC == null || !fC.contains(transactionData.getTransactionId())) {
            return false;
        }
        return true;
    }

    private boolean m352b(TransactionDetails transactionDetails) {
        if (transactionDetails != null) {
            TokenRecord bq = this.jJ.bq(this.mTokenId);
            if (bq == null) {
                Log.m286e("TransactionDetailsRetriever", "Token Record is NULL");
            } else {
                List arrayList;
                List fC = bq.fC();
                if (fC == null) {
                    arrayList = new ArrayList();
                } else {
                    arrayList = fC;
                }
                Log.m285d("TransactionDetailsRetriever", "TIDs : " + arrayList);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                Collections.sort(transactionDetails.getTransactionData(), new TransactionDetailsRetriever(this, simpleDateFormat));
                Log.m285d("TransactionDetailsRetriever", "Transaction Details : " + transactionDetails.getTransactionData());
                for (TransactionData transactionData : transactionDetails.getTransactionData()) {
                    if (arrayList.contains(transactionData.getTransactionId())) {
                        arrayList.remove(transactionData.getTransactionId());
                        arrayList.add(transactionData.getTransactionId());
                    } else {
                        arrayList.add(transactionData.getTransactionId());
                    }
                }
                if (arrayList.size() >= 10) {
                    arrayList = arrayList.subList(arrayList.size() - 10, arrayList.size());
                }
                Log.m285d("TransactionDetailsRetriever", "Final TIDs : " + arrayList);
                bq.m1253c(arrayList);
                bq.m1254i(true);
                this.jJ.m1230d(bq);
            }
        }
        return false;
    }

    private String bi() {
        String str = null;
        try {
            str = new JSONObject(this.kU.getMessage()).getJSONObject(PushMessage.JSON_KEY_TRANSACTION).getString("log");
        } catch (JSONException e) {
        }
        return str;
    }

    private JsonObject m346a(JsonArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        JsonObject jsonObject = new JsonObject();
        JsonElement jsonObject2 = new JsonObject();
        jsonObject2.add("elements", jsonArray);
        jsonObject.add("txn", jsonObject2);
        Log.m285d("TransactionDetailsRetriever", "reportData = " + jsonObject);
        return jsonObject;
    }
}
