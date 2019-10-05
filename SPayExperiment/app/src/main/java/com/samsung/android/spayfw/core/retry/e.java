/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.Intent
 *  android.os.IBinder
 *  android.os.Message
 *  android.os.Parcelable
 *  java.lang.Class
 *  java.lang.Object
 *  java.lang.String
 *  java.util.HashMap
 *  java.util.Map
 *  java.util.Timer
 *  java.util.TimerTask
 */
package com.samsung.android.spayfw.core.retry;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcelable;
import com.samsung.android.spayfw.appinterface.ITransactionDetailsCallback;
import com.samsung.android.spayfw.appinterface.TransactionDetails;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.j;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class e {
    private static e mW;
    private Timer mTimer = new Timer();
    private Map<String, a> mX = new HashMap();
    private TokenRecordStorage mY;

    private e(Context context) {
        this.mY = TokenRecordStorage.ae(context);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void T(String string) {
        e e2 = this;
        // MONITORENTER : e2
        Log.d("TransactionDetailsRetryRequester", "retry: adding token in queue = " + string);
        if (this.mX != null) {
            long l2 = ((a)((Object)this.mX.get((Object)string))).time;
            a a2 = new a(string, 0L);
            if (l2 == 0L) {
                a2.time = 30000L;
                this.mX.put((Object)string, (Object)a2);
            } else if (l2 == 30000L) {
                a2.time = 120000L;
                this.mX.put((Object)string, (Object)a2);
            } else if (l2 == 120000L) {
                a2.time = 240000L;
                this.mX.put((Object)string, (Object)a2);
            } else if (l2 == 240000L) {
                this.mX.remove((Object)string);
                Log.d("TransactionDetailsRetryRequester", "Retry Limit Reached. Stop Retry.");
                return;
            }
            Log.i("TransactionDetailsRetryRequester", "retry time = " + a2.time);
            this.mTimer.schedule((TimerTask)a2, a2.time);
        }
        // MONITOREXIT : e2
    }

    public static e w(Context context) {
        Class<e> class_ = e.class;
        synchronized (e.class) {
            if (mW == null) {
                mW = new e(context);
            }
            e e2 = mW;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return e2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void add(String string) {
        e e2 = this;
        synchronized (e2) {
            Log.d("TransactionDetailsRetryRequester", "add :  " + string);
            if (this.mY == null) {
                Log.e("TransactionDetailsRetryRequester", "Token Storage is null");
            } else {
                com.samsung.android.spayfw.storage.models.a a2 = this.mY.bq(string);
                if (a2 == null) {
                    Log.e("TransactionDetailsRetryRequester", "Token Record is null");
                } else if (!a2.fw()) {
                    Log.i("TransactionDetailsRetryRequester", "Transaction Retry NOT Allowed");
                } else {
                    Log.i("TransactionDetailsRetryRequester", "Transaction Retry Allowed");
                    a a3 = (a)((Object)this.mX.get((Object)string));
                    if (a3 == null) {
                        this.mX.put((Object)string, (Object)new a(string, 0L));
                    } else {
                        a3.cancel();
                        this.mTimer.purge();
                        this.mX.put((Object)string, (Object)new a(string, 0L));
                    }
                    this.T(string);
                }
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void remove(String string) {
        e e2 = this;
        synchronized (e2) {
            Log.d("TransactionDetailsRetryRequester", "remove :  " + string);
            a a2 = (a)((Object)this.mX.get((Object)string));
            if (a2 != null) {
                a2.cancel();
                this.mTimer.purge();
            }
            this.mX.remove((Object)string);
            return;
        }
    }

    private class a
    extends TimerTask {
        private long time;
        private String tokenId;

        public a(String string, long l2) {
            this.tokenId = string;
            this.time = l2;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        public void run() {
            e e2;
            Log.i("TransactionDetailsRetryRequester", "run : RetryTimerTask");
            Log.d("TransactionDetailsRetryRequester", "run : RetryTimerTask : " + this.tokenId);
            e e3 = e2 = e.this;
            synchronized (e3) {
                if ((a)((Object)e.this.mX.get((Object)this.tokenId)) == null) {
                    Log.i("TransactionDetailsRetryRequester", "No Need to Run.");
                    return;
                }
            }
            Message message = j.a(37, this.tokenId, -1L, -1L, -1, new ITransactionDetailsCallback(){

                public IBinder asBinder() {
                    return null;
                }

                @Override
                public void onFail(String string, int n2) {
                    Log.e("TransactionDetailsRetryRequester", "onFail : " + n2);
                    if (n2 == -3) {
                        e.this.remove(string);
                        return;
                    }
                    e.this.T(string);
                }

                @Override
                public void onTransactionUpdate(String string, TransactionDetails transactionDetails) {
                    Log.d("TransactionDetailsRetryRequester", "onTransactionUpdate : tokenId " + string);
                    Log.d("TransactionDetailsRetryRequester", "onTransactionUpdate : transactionDetails " + transactionDetails);
                    Intent intent = new Intent("com.samsung.android.spayfw.action.notification");
                    intent.putExtra("notiType", "transactionDetailsReceived");
                    intent.putExtra("tokenId", string);
                    intent.putExtra("transactionDetails", (Parcelable)transactionDetails);
                    PaymentFrameworkApp.a(intent);
                    e.this.remove(string);
                }
            });
            PaymentFrameworkApp.az().a(message);
        }

    }

}

