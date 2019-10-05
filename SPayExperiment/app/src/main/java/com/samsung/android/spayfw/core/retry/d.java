/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Message
 *  com.google.gson.Gson
 *  com.google.gson.reflect.TypeToken
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Type
 *  java.util.Collection
 *  java.util.Timer
 *  java.util.TimerTask
 *  java.util.concurrent.ConcurrentHashMap
 */
package com.samsung.android.spayfw.core.retry;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.j;
import com.samsung.android.spayfw.remoteservice.tokenrequester.l;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReportData;

import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class d {
    private static d mS;
    private static ConcurrentHashMap<ReportData, RetryRequestData> mT;

    static {
        mT = new ConcurrentHashMap();
    }

    private d() {
    }

    public static void a(RetryRequestData retryRequestData) {
        Log.d("RetryRequester", "executeRequest: rObj = " + retryRequestData);
        if (retryRequestData == null) {
            return;
        }
        if (retryRequestData.getNumRetryAttempts() >= 3) {
            d.a(retryRequestData.getReportData());
            return;
        }
        retryRequestData.setNumRetryAttempts(1 + retryRequestData.getNumRetryAttempts());
        d.bp();
        l.Q((Context)PaymentFrameworkApp.aB()).a(retryRequestData.getCardType(), retryRequestData.getReportData()).fe();
    }

    public static void a(RetryRequestData retryRequestData, long l2) {
        Log.d("RetryRequester", "scheduleRetryTimer: scheduling timer at " + l2);
        Timer timer = new Timer();
        d d2 = d.bn();
        d2.getClass();
        timer.schedule((TimerTask)d2.new a(retryRequestData), l2);
    }

    public static void a(ReportData reportData) {
        Class<d> class_ = d.class;
        synchronized (d.class) {
            mT.remove((Object)reportData);
            d.bp();
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return;
        }
    }

    public static void a(ReportData reportData, RetryRequestData retryRequestData) {
        Class<d> class_ = d.class;
        synchronized (d.class) {
            mT.put((Object)reportData, (Object)retryRequestData);
            d.bp();
            // ** MonitorExit[var4_2] (shouldn't be in output)
            return;
        }
    }

    public static RetryRequestData b(ReportData reportData) {
        Class<d> class_ = d.class;
        synchronized (d.class) {
            RetryRequestData retryRequestData = (RetryRequestData)mT.get((Object)reportData);
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return retryRequestData;
        }
    }

    public static void b(RetryRequestData retryRequestData) {
        long l2 = retryRequestData.getNextRetryTimeoutValue();
        if (l2 == -1L) {
            l2 = 300000L;
        }
        retryRequestData.setNextRetryTimeoutValue(20L * l2);
        d.bp();
        d.a(retryRequestData, l2);
    }

    public static d bn() {
        Class<d> class_ = d.class;
        synchronized (d.class) {
            if (mS == null) {
                mS = new d();
            }
            d d2 = mS;
            // ** MonitorExit[var2] (shouldn't be in output)
            return d2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void bo() {
        Class<d> class_ = d.class;
        synchronized (d.class) {
            Log.d("RetryRequester", "In executeAllReqInQueue");
            boolean bl = mT.isEmpty();
            if (!bl) {
                for (RetryRequestData retryRequestData : mT.values()) {
                    Log.d("RetryRequester", "rObj = " + retryRequestData.getReportData() + "; rObj.retryNum = " + retryRequestData.getNumRetryAttempts());
                    d.a(retryRequestData);
                }
            }
            // ** MonitorExit[var4] (shouldn't be in output)
            return;
        }
    }

    private static void bp() {
        Class<d> class_ = d.class;
        synchronized (d.class) {
            Log.d("RetryRequester", "persistData - mRetryReqQueue = " + mT);
            SharedPreferences.Editor editor = PaymentFrameworkApp.aB().getSharedPreferences("RetryRequestQueue", 0).edit();
            editor.putString("RequestList", new Gson().toJson((Object)mT.values()));
            editor.commit();
            // ** MonitorExit[var4] (shouldn't be in output)
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void bq() {
        Class<d> class_ = d.class;
        synchronized (d.class) {
            SharedPreferences sharedPreferences = PaymentFrameworkApp.aB().getSharedPreferences("RetryRequestQueue", 0);
            try {
                String string = sharedPreferences.getString("RequestList", "");
                boolean bl = "".equals((Object)string);
                if (!bl) {
                    Collection collection = (Collection)new Gson().fromJson(string, new TypeToken<Collection<RetryRequestData>>(){}.getType());
                    Log.d("RetryRequester", "readPersistedData - reqList = " + (Object)collection);
                    if (collection != null) {
                        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                        for (RetryRequestData retryRequestData : collection) {
                            if (retryRequestData == null) continue;
                            Log.d("RetryRequester", "rObj.reportData = " + retryRequestData.getReportData() + "; rObj.numRetry = " + retryRequestData.getNumRetryAttempts());
                            concurrentHashMap.put((Object)retryRequestData.getReportData(), (Object)retryRequestData);
                        }
                        mT = concurrentHashMap;
                    }
                }
            }
            catch (Exception exception) {
                Log.c("RetryRequester", exception.getMessage(), exception);
                Log.e("RetryRequester", "readPersistedData: Data corrupted, clear the shared prefs values!");
                sharedPreferences.edit().clear();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void e(boolean bl) {
        Class<d> class_ = d.class;
        synchronized (d.class) {
            Log.d("RetryRequester", "flushRetryRequestQueue: fromPersistedStorage = " + bl);
            if (bl) {
                d.bq();
            }
            if (mT.isEmpty()) {
                Log.d("RetryRequester", "flushRetryRequestQueue: Request Queue empty!");
            } else {
                Message message = j.a(15, null, null);
                PaymentFrameworkApp.az().sendMessage(message);
            }
            // ** MonitorExit[var4_1] (shouldn't be in output)
            return;
        }
    }

    private class a
    extends TimerTask {
        private RetryRequestData mU;

        public a(RetryRequestData retryRequestData) {
            this.mU = retryRequestData;
        }

        public void run() {
            Log.d("RetryRequester", "run : RetryTimerExpired - rObj.numRetry = " + this.mU.getNumRetryAttempts());
            Message message = j.a(15, this.mU, null);
            PaymentFrameworkApp.az().sendMessage(message);
        }
    }

}

