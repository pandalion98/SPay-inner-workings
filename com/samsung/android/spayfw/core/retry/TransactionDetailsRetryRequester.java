package com.samsung.android.spayfw.core.retry;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback;
import com.samsung.android.spayfw.appinterface.PaymentFramework;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.PaymentFrameworkMessage;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.payprovider.PaymentNetworkProvider;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/* renamed from: com.samsung.android.spayfw.core.retry.e */
public class TransactionDetailsRetryRequester {
    private static TransactionDetailsRetryRequester mW;
    private Timer mTimer;
    private Map<String, TransactionDetailsRetryRequester> mX;
    private TokenRecordStorage mY;

    /* renamed from: com.samsung.android.spayfw.core.retry.e.a */
    private class TransactionDetailsRetryRequester extends TimerTask {
        final /* synthetic */ TransactionDetailsRetryRequester mZ;
        private long time;
        private String tokenId;

        /* renamed from: com.samsung.android.spayfw.core.retry.e.a.1 */
        class TransactionDetailsRetryRequester implements ITransactionDetailsCallback {
            final /* synthetic */ TransactionDetailsRetryRequester na;

            TransactionDetailsRetryRequester(TransactionDetailsRetryRequester transactionDetailsRetryRequester) {
                this.na = transactionDetailsRetryRequester;
            }

            public void onTransactionUpdate(String str, TransactionDetails transactionDetails) {
                Log.m285d("TransactionDetailsRetryRequester", "onTransactionUpdate : tokenId " + str);
                Log.m285d("TransactionDetailsRetryRequester", "onTransactionUpdate : transactionDetails " + transactionDetails);
                Intent intent = new Intent(PaymentFramework.ACTION_PF_NOTIFICATION);
                intent.putExtra(PaymentFramework.EXTRA_NOTIFICATION_TYPE, PaymentFramework.NOTIFICATION_TYPE_TRANSACTION_DETAILS_RECEIVED);
                intent.putExtra(PaymentFramework.EXTRA_TOKEN_ID, str);
                intent.putExtra(PaymentFramework.EXTRA_TRANSACTION_DETAILS, transactionDetails);
                PaymentFrameworkApp.m315a(intent);
                this.na.mZ.remove(str);
            }

            public void onFail(String str, int i) {
                Log.m286e("TransactionDetailsRetryRequester", "onFail : " + i);
                if (i == -3) {
                    this.na.mZ.remove(str);
                } else {
                    this.na.mZ.m676T(str);
                }
            }

            public IBinder asBinder() {
                return null;
            }
        }

        public TransactionDetailsRetryRequester(TransactionDetailsRetryRequester transactionDetailsRetryRequester, String str, long j) {
            this.mZ = transactionDetailsRetryRequester;
            this.tokenId = str;
            this.time = j;
        }

        public void run() {
            Log.m287i("TransactionDetailsRetryRequester", "run : RetryTimerTask");
            Log.m285d("TransactionDetailsRetryRequester", "run : RetryTimerTask : " + this.tokenId);
            synchronized (this.mZ) {
                if (((TransactionDetailsRetryRequester) this.mZ.mX.get(this.tokenId)) == null) {
                    Log.m287i("TransactionDetailsRetryRequester", "No Need to Run.");
                    return;
                }
                PaymentFrameworkApp.az().m618a(PaymentFrameworkMessage.m623a(37, this.tokenId, Long.valueOf(-1), Long.valueOf(-1), Integer.valueOf(-1), new TransactionDetailsRetryRequester(this)));
            }
        }
    }

    public static synchronized TransactionDetailsRetryRequester m679w(Context context) {
        TransactionDetailsRetryRequester transactionDetailsRetryRequester;
        synchronized (TransactionDetailsRetryRequester.class) {
            if (mW == null) {
                mW = new TransactionDetailsRetryRequester(context);
            }
            transactionDetailsRetryRequester = mW;
        }
        return transactionDetailsRetryRequester;
    }

    private TransactionDetailsRetryRequester(Context context) {
        this.mX = new HashMap();
        this.mTimer = new Timer();
        this.mY = TokenRecordStorage.ae(context);
    }

    public synchronized void remove(String str) {
        Log.m285d("TransactionDetailsRetryRequester", "remove :  " + str);
        TransactionDetailsRetryRequester transactionDetailsRetryRequester = (TransactionDetailsRetryRequester) this.mX.get(str);
        if (transactionDetailsRetryRequester != null) {
            transactionDetailsRetryRequester.cancel();
            this.mTimer.purge();
        }
        this.mX.remove(str);
    }

    public synchronized void add(String str) {
        Log.m285d("TransactionDetailsRetryRequester", "add :  " + str);
        if (this.mY == null) {
            Log.m286e("TransactionDetailsRetryRequester", "Token Storage is null");
        } else {
            TokenRecord bq = this.mY.bq(str);
            if (bq == null) {
                Log.m286e("TransactionDetailsRetryRequester", "Token Record is null");
            } else if (bq.fw()) {
                Log.m287i("TransactionDetailsRetryRequester", "Transaction Retry Allowed");
                TransactionDetailsRetryRequester transactionDetailsRetryRequester = (TransactionDetailsRetryRequester) this.mX.get(str);
                if (transactionDetailsRetryRequester == null) {
                    this.mX.put(str, new TransactionDetailsRetryRequester(this, str, 0));
                } else {
                    transactionDetailsRetryRequester.cancel();
                    this.mTimer.purge();
                    this.mX.put(str, new TransactionDetailsRetryRequester(this, str, 0));
                }
                m676T(str);
            } else {
                Log.m287i("TransactionDetailsRetryRequester", "Transaction Retry NOT Allowed");
            }
        }
    }

    private synchronized void m676T(String str) {
        Log.m285d("TransactionDetailsRetryRequester", "retry: adding token in queue = " + str);
        if (this.mX != null) {
            long a = ((TransactionDetailsRetryRequester) this.mX.get(str)).time;
            TimerTask transactionDetailsRetryRequester = new TransactionDetailsRetryRequester(this, str, 0);
            if (a == 0) {
                transactionDetailsRetryRequester.time = PaymentNetworkProvider.NFC_WAIT_TIME;
                this.mX.put(str, transactionDetailsRetryRequester);
            } else if (a == PaymentNetworkProvider.NFC_WAIT_TIME) {
                transactionDetailsRetryRequester.time = 120000;
                this.mX.put(str, transactionDetailsRetryRequester);
            } else if (a == 120000) {
                transactionDetailsRetryRequester.time = 240000;
                this.mX.put(str, transactionDetailsRetryRequester);
            } else if (a == 240000) {
                this.mX.remove(str);
                Log.m285d("TransactionDetailsRetryRequester", "Retry Limit Reached. Stop Retry.");
            }
            Log.m287i("TransactionDetailsRetryRequester", "retry time = " + transactionDetailsRetryRequester.time);
            this.mTimer.schedule(transactionDetailsRetryRequester, transactionDetailsRetryRequester.time);
        }
    }
}
