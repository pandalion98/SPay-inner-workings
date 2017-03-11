package com.samsung.android.spayfw.core.retry;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.PaymentFrameworkMessage;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.tokenrequester.TokenRequesterClient;
import com.samsung.android.spayfw.remoteservice.tokenrequester.models.ReportData;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/* renamed from: com.samsung.android.spayfw.core.retry.d */
public class RetryRequester {
    private static RetryRequester mS;
    private static ConcurrentHashMap<ReportData, RetryRequestData> mT;

    /* renamed from: com.samsung.android.spayfw.core.retry.d.1 */
    static class RetryRequester extends TypeToken<Collection<RetryRequestData>> {
        RetryRequester() {
        }
    }

    /* renamed from: com.samsung.android.spayfw.core.retry.d.a */
    private class RetryRequester extends TimerTask {
        private RetryRequestData mU;
        final /* synthetic */ RetryRequester mV;

        public RetryRequester(RetryRequester retryRequester, RetryRequestData retryRequestData) {
            this.mV = retryRequester;
            this.mU = retryRequestData;
        }

        public void run() {
            Log.m285d("RetryRequester", "run : RetryTimerExpired - rObj.numRetry = " + this.mU.getNumRetryAttempts());
            PaymentFrameworkApp.az().sendMessage(PaymentFrameworkMessage.m620a(15, this.mU, null));
        }
    }

    static {
        mT = new ConcurrentHashMap();
    }

    public static synchronized RetryRequester bn() {
        RetryRequester retryRequester;
        synchronized (RetryRequester.class) {
            if (mS == null) {
                mS = new RetryRequester();
            }
            retryRequester = mS;
        }
        return retryRequester;
    }

    public static synchronized void m670a(ReportData reportData, RetryRequestData retryRequestData) {
        synchronized (RetryRequester.class) {
            mT.put(reportData, retryRequestData);
            RetryRequester.bp();
        }
    }

    public static synchronized void m669a(ReportData reportData) {
        synchronized (RetryRequester.class) {
            mT.remove(reportData);
            RetryRequester.bp();
        }
    }

    public static synchronized RetryRequestData m671b(ReportData reportData) {
        RetryRequestData retryRequestData;
        synchronized (RetryRequester.class) {
            retryRequestData = (RetryRequestData) mT.get(reportData);
        }
        return retryRequestData;
    }

    public static void m667a(RetryRequestData retryRequestData) {
        Log.m285d("RetryRequester", "executeRequest: rObj = " + retryRequestData);
        if (retryRequestData != null) {
            if (retryRequestData.getNumRetryAttempts() >= 3) {
                RetryRequester.m669a(retryRequestData.getReportData());
                return;
            }
            retryRequestData.setNumRetryAttempts(retryRequestData.getNumRetryAttempts() + 1);
            RetryRequester.bp();
            TokenRequesterClient.m1126Q(PaymentFrameworkApp.aB()).m1134a(retryRequestData.getCardType(), retryRequestData.getReportData()).fe();
        }
    }

    public static synchronized void bo() {
        synchronized (RetryRequester.class) {
            Log.m285d("RetryRequester", "In executeAllReqInQueue");
            if (!mT.isEmpty()) {
                for (RetryRequestData retryRequestData : mT.values()) {
                    Log.m285d("RetryRequester", "rObj = " + retryRequestData.getReportData() + "; rObj.retryNum = " + retryRequestData.getNumRetryAttempts());
                    RetryRequester.m667a(retryRequestData);
                }
            }
        }
    }

    public static synchronized void m673e(boolean z) {
        synchronized (RetryRequester.class) {
            Log.m285d("RetryRequester", "flushRetryRequestQueue: fromPersistedStorage = " + z);
            if (z) {
                RetryRequester.bq();
            }
            if (mT.isEmpty()) {
                Log.m285d("RetryRequester", "flushRetryRequestQueue: Request Queue empty!");
            } else {
                PaymentFrameworkApp.az().sendMessage(PaymentFrameworkMessage.m620a(15, null, null));
            }
        }
    }

    public static void m668a(RetryRequestData retryRequestData, long j) {
        Log.m285d("RetryRequester", "scheduleRetryTimer: scheduling timer at " + j);
        Timer timer = new Timer();
        RetryRequester bn = RetryRequester.bn();
        bn.getClass();
        timer.schedule(new RetryRequester(bn, retryRequestData), j);
    }

    public static void m672b(RetryRequestData retryRequestData) {
        long nextRetryTimeoutValue = retryRequestData.getNextRetryTimeoutValue();
        if (nextRetryTimeoutValue == -1) {
            nextRetryTimeoutValue = 300000;
        }
        retryRequestData.setNextRetryTimeoutValue(20 * nextRetryTimeoutValue);
        RetryRequester.bp();
        RetryRequester.m668a(retryRequestData, nextRetryTimeoutValue);
    }

    private static synchronized void bp() {
        synchronized (RetryRequester.class) {
            Log.m285d("RetryRequester", "persistData - mRetryReqQueue = " + mT);
            Editor edit = PaymentFrameworkApp.aB().getSharedPreferences("RetryRequestQueue", 0).edit();
            edit.putString("RequestList", new Gson().toJson(mT.values()));
            edit.commit();
        }
    }

    private static synchronized void bq() {
        synchronized (RetryRequester.class) {
            SharedPreferences sharedPreferences = PaymentFrameworkApp.aB().getSharedPreferences("RetryRequestQueue", 0);
            try {
                String string = sharedPreferences.getString("RequestList", BuildConfig.FLAVOR);
                if (!BuildConfig.FLAVOR.equals(string)) {
                    Collection<RetryRequestData> collection = (Collection) new Gson().fromJson(string, new RetryRequester().getType());
                    Log.m285d("RetryRequester", "readPersistedData - reqList = " + collection);
                    if (collection != null) {
                        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
                        for (RetryRequestData retryRequestData : collection) {
                            if (retryRequestData != null) {
                                Log.m285d("RetryRequester", "rObj.reportData = " + retryRequestData.getReportData() + "; rObj.numRetry = " + retryRequestData.getNumRetryAttempts());
                                concurrentHashMap.put(retryRequestData.getReportData(), retryRequestData);
                            }
                        }
                        mT = concurrentHashMap;
                    }
                }
            } catch (Throwable e) {
                Log.m284c("RetryRequester", e.getMessage(), e);
                Log.m286e("RetryRequester", "readPersistedData: Data corrupted, clear the shared prefs values!");
                sharedPreferences.edit().clear();
            }
        }
    }

    private RetryRequester() {
    }
}
