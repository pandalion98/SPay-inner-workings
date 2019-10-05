/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.os.RemoteException
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  java.lang.Boolean
 *  java.lang.CharSequence
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.text.SimpleDateFormat
 *  java.util.ArrayList
 *  java.util.Collections
 *  java.util.Comparator
 *  java.util.Date
 *  java.util.List
 *  java.util.Locale
 *  java.util.TimeZone
 *  org.json.JSONException
 *  org.json.JSONObject
 */
package com.samsung.android.spayfw.core.a;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.samsung.android.spayfw.appinterface.IPushMessageCallback;
import com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.appinterface.TransactionData;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.c;
import com.samsung.android.spayfw.core.retry.e;
import com.samsung.android.spayfw.payprovider.f;
import com.samsung.android.spayfw.payprovider.i;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.JSONObject;

public class aa
extends o {
    protected PushMessage kU;
    protected IPushMessageCallback kV;
    protected int mCount;
    protected ITransactionDetailsCallback mG;
    protected long mH;
    protected long mI;
    protected String mTokenId;

    public aa(Context context, String string, long l2, long l3, int n2, ITransactionDetailsCallback iTransactionDetailsCallback) {
        super(context);
        this.mTokenId = string;
        this.mG = iTransactionDetailsCallback;
        this.mH = l2;
        this.mI = l3;
        this.mCount = n2;
    }

    aa(Context context, String string, PushMessage pushMessage, IPushMessageCallback iPushMessageCallback) {
        super(context);
        this.mTokenId = string;
        this.kV = iPushMessageCallback;
        this.kU = pushMessage;
    }

    private JsonObject a(JsonArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject2 = new JsonObject();
        jsonObject2.add("elements", (JsonElement)jsonArray);
        jsonObject.add("txn", (JsonElement)jsonObject2);
        Log.d("TransactionDetailsRetriever", "reportData = " + (Object)jsonObject);
        return jsonObject;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean a(TransactionData transactionData) {
        String string = this.kU.getTokenId();
        com.samsung.android.spayfw.storage.models.a a2 = this.jJ.bq(string);
        if (a2 == null) {
            Log.e("TransactionDetailsRetriever", "Token Record is NULL");
            return false;
        } else {
            List<String> list = a2.fC();
            Log.d("TransactionDetailsRetriever", "TIDs : " + list);
            if (list == null || !list.contains((Object)transactionData.getTransactionId())) return false;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean b(TransactionDetails transactionDetails) {
        if (transactionDetails == null) {
            return false;
        }
        com.samsung.android.spayfw.storage.models.a a2 = this.jJ.bq(this.mTokenId);
        if (a2 == null) {
            Log.e("TransactionDetailsRetriever", "Token Record is NULL");
            return false;
        }
        ArrayList arrayList = a2.fC();
        ArrayList arrayList2 = arrayList == null ? new ArrayList() : arrayList;
        Log.d("TransactionDetailsRetriever", "TIDs : " + (Object)arrayList2);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone((String)"GMT"));
        Collections.sort(transactionDetails.getTransactionData(), (Comparator)new Comparator<TransactionData>(){

            public int compare(TransactionData transactionData, TransactionData transactionData2) {
                try {
                    int n2 = simpleDateFormat.parse(transactionData.getTransactionDate()).compareTo(simpleDateFormat.parse(transactionData2.getTransactionDate()));
                    return n2;
                }
                catch (Exception exception) {
                    Log.c("TransactionDetailsRetriever", exception.getMessage(), exception);
                    return 0;
                }
            }
        });
        Log.d("TransactionDetailsRetriever", "Transaction Details : " + transactionDetails.getTransactionData());
        for (TransactionData transactionData : transactionDetails.getTransactionData()) {
            if (arrayList2.contains((Object)transactionData.getTransactionId())) {
                arrayList2.remove((Object)transactionData.getTransactionId());
                arrayList2.add((Object)transactionData.getTransactionId());
                continue;
            }
            arrayList2.add((Object)transactionData.getTransactionId());
        }
        if (arrayList2.size() >= 10) {
            arrayList2 = arrayList2.subList(-10 + arrayList2.size(), arrayList2.size());
        }
        Log.d("TransactionDetailsRetriever", "Final TIDs : " + (Object)arrayList2);
        a2.c((List<String>)arrayList2);
        a2.i(true);
        this.jJ.d(a2);
        return false;
    }

    private String bi() {
        String string = this.kU.getMessage();
        try {
            String string2 = new JSONObject(string).getJSONObject("transaction").getString("log");
            return string2;
        }
        catch (JSONException jSONException) {
            return null;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void a(TransactionDetails transactionDetails) {
        try {
            if (this.mG != null) {
                Log.i("TransactionDetailsRetriever", "Invoking Success Transaction Callback");
                this.mG.onTransactionUpdate(this.mTokenId, transactionDetails);
            } else if (this.kV != null) {
                Log.d("TransactionDetailsRetriever", "Oob value = " + this.kU.getOob());
                Log.i("TransactionDetailsRetriever", "Invoking Success Push Callback");
                this.kV.onTransactionUpdate(this.kU.getNotificationId(), this.mTokenId, transactionDetails, this.kU.getOob());
            } else {
                Log.e("TransactionDetailsRetriever", "No Callbacks");
            }
            e.w(this.mContext).remove(this.mTokenId);
            return;
        }
        catch (RemoteException remoteException) {
            Log.c("TransactionDetailsRetriever", remoteException.getMessage(), remoteException);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void notifyError(int n2) {
        try {
            if (this.mG != null) {
                Log.i("TransactionDetailsRetriever", "Invoking Error Transaction Callback " + n2);
                this.mG.onFail(this.mTokenId, n2);
                return;
            }
            if (this.kV != null) {
                Log.i("TransactionDetailsRetriever", "Invoking Error Push Callback " + n2);
                this.kV.onFail(this.kU.getNotificationId(), n2);
                return;
            }
            Log.e("TransactionDetailsRetriever", "No Callbacks");
            return;
        }
        catch (RemoteException remoteException) {
            Log.c("TransactionDetailsRetriever", remoteException.getMessage(), remoteException);
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public void process() {
        Log.d("TransactionDetailsRetriever", "process : tokenId " + this.mTokenId);
        c c2 = this.iJ.r(this.mTokenId);
        if (c2 == null) {
            Log.e("TransactionDetailsRetriever", "Unable to get card based on tokenId. ignore request");
            this.notifyError(-5);
            return;
        } else {
            int n2;
            String string = this.kU != null ? this.kU.getTransactionUrl() : null;
            String string2 = this.jJ.b(TokenRecordStorage.TokenGroup.TokenColumn.CA, c2.ac().getTokenId());
            if (string != null) {
                if (string2 == null) {
                    Log.i("TransactionDetailsRetriever", "Update Transact URL in DB");
                    com.samsung.android.spayfw.storage.models.a a2 = this.jJ.bq(c2.ac().getTokenId());
                    if (a2 != null) {
                        a2.bv(string);
                        this.jJ.d(a2);
                    }
                }
                string2 = string;
            } else if (string2 != null) {
                string = string2;
            } else {
                String string3 = string2;
                string2 = string;
                string = string3;
            }
            Bundle bundle = new Bundle();
            bundle.putString("trTokenId", this.mTokenId);
            bundle.putString("transactionUrl", string);
            bundle.putString("pushtransactionUrl", string2);
            bundle.putLong("startTimestamp", this.mH);
            bundle.putLong("endTimestamp", this.mI);
            bundle.putInt("count", this.mCount);
            if (this.kU != null) {
                bundle.putString("authorizationCode", this.kU.getTransactionCredentials());
            }
            if ((n2 = c2.ad().getTransactionDataTA(bundle, new a())) == 0) return;
            {
                Log.e("TransactionDetailsRetriever", " pay provider returns error. can't get transaction");
                com.samsung.android.spayfw.payprovider.e e2 = new com.samsung.android.spayfw.payprovider.e();
                e2.setErrorCode(n2);
                this.notifyError(-1);
                if (this.kU == null) return;
                {
                    this.b(this.kU.getNotificationId(), this.mTokenId, "ACTIVE", "TRANSACTION", c.y(c2.getCardBrand()), e2, false);
                    return;
                }
            }
        }
    }

    private class a
    implements i {
        private a() {
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void a(f f2, int n2, Object object, com.samsung.android.spayfw.payprovider.e e2) {
            int n3 = -3;
            Log.d("TransactionDetailsRetriever", "PayProviderTransactionResponse onComplete: status: " + n2);
            c c2 = aa.this.iJ.r(aa.this.mTokenId);
            if (c2 == null) {
                Log.e("TransactionDetailsRetriever", "processTransaction : unable to get Card object :" + aa.this.mTokenId);
                aa.this.notifyError(-5);
                return;
            } else if (n2 != 0 || object == null) {
                Log.e("TransactionDetailsRetriever", " pay provider returns error. can't get transaction");
                if (n2 == -5) {
                    n3 = -6;
                } else if (n2 == -7) {
                    n3 = -9;
                } else if (n2 == -8) {
                    n3 = -4;
                    com.samsung.android.spayfw.storage.models.a a2 = aa.this.jJ.bq(aa.this.mTokenId);
                    if (a2 != null) {
                        a2.i(false);
                        aa.this.jJ.d(a2);
                    }
                } else if (n2 == -10) {
                    n3 = -206;
                } else if (object != null) {
                    n3 = -1;
                }
                aa.this.notifyError(n3);
                if (aa.this.kU == null) return;
                {
                    aa.this.b(aa.this.kU.getNotificationId(), aa.this.mTokenId, c2.ac().getTokenStatus(), "TRANSACTION", c.y(c2.getCardBrand()), e2, false);
                    return;
                }
            } else {
                TransactionDetails transactionDetails = c2.ad().processTransactionDataTA(object);
                c2.ad().checkIfReplenishmentNeeded(null);
                if (transactionDetails != null) {
                    aa.this.a(transactionDetails);
                } else {
                    aa.this.notifyError(n3);
                }
                PushMessage pushMessage = aa.this.kU;
                com.samsung.android.spayfw.payprovider.e e3 = null;
                if (pushMessage != null) {
                    JsonArray jsonArray = new JsonArray();
                    String string = aa.this.bi();
                    if (transactionDetails != null && transactionDetails.getTransactionData() != null && string != null) {
                        for (TransactionData transactionData : transactionDetails.getTransactionData()) {
                            if (transactionData == null) continue;
                            JsonObject jsonObject = new JsonObject();
                            if (string.contains((CharSequence)"txnTimestamp")) {
                                jsonObject.addProperty("txnTimestamp", transactionData.getTransactionDate());
                            }
                            if (string.contains((CharSequence)"merchantName")) {
                                jsonObject.addProperty("merchantName", transactionData.getMechantName());
                            }
                            if (string.contains((CharSequence)"amount")) {
                                jsonObject.addProperty("amount", transactionData.getAmount());
                            }
                            if (string.contains((CharSequence)"currency")) {
                                jsonObject.addProperty("currency", transactionData.getCurrencyCode());
                            }
                            if (string.contains((CharSequence)"transactionType")) {
                                jsonObject.addProperty("transactionType", transactionData.getTransactionType());
                            }
                            if (string.contains((CharSequence)"txnReceiptId")) {
                                jsonObject.addProperty("txnReceiptId", transactionData.getTransactionId());
                            }
                            jsonObject.addProperty("updatedTransaction", Boolean.valueOf((boolean)aa.this.a(transactionData)));
                            jsonArray.add((JsonElement)jsonObject);
                        }
                    }
                    JsonObject jsonObject = aa.this.a(jsonArray);
                    e3 = null;
                    if (jsonObject != null) {
                        e3 = new com.samsung.android.spayfw.payprovider.e();
                        e3.b(jsonObject);
                    }
                }
                aa.this.b(transactionDetails);
                if (aa.this.kU == null) return;
                {
                    aa.this.a(aa.this.kU.getNotificationId(), aa.this.mTokenId, "ACTIVE", "TRANSACTION", c.y(c2.getCardBrand()), e3, false);
                    return;
                }
            }
        }
    }

}

