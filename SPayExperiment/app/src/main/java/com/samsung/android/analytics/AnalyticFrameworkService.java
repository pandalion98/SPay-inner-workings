/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.app.Service
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.IBinder$DeathRecipient
 *  android.os.RemoteException
 *  java.lang.Object
 *  java.lang.String
 *  java.util.List
 */
package com.samsung.android.analytics;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import com.samsung.android.analytics.sdk.AnalyticContext;
import com.samsung.android.analytics.sdk.AnalyticEvent;
import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.core.BinAttribute;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.a.n;
import com.samsung.android.spayfw.utils.h;
import java.util.List;

public class AnalyticFrameworkService
extends Service {
    static String bE = null;
    private a bD = null;
    private final a.a bF = new a.a(){

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int a(AnalyticEvent analyticEvent, AnalyticContext analyticContext, boolean bl) {
            Log.d("AnalyticFrameworkService", "reportAnalyticEvent: AnalyticEvent:" + analyticEvent + " , AnalyticContext: " + analyticContext);
            AnalyticFrameworkService.this.a(analyticContext);
            AnalyticFrameworkService.this.a(analyticEvent);
            try {
                com.samsung.android.spayfw.core.a.a.l(AnalyticFrameworkService.this.mContext).a(analyticEvent, analyticContext);
                do {
                    return 0;
                    break;
                } while (true);
            }
            catch (RemoteException remoteException) {
                remoteException.printStackTrace();
                Log.e("AnalyticFrameworkService", "Exception while sending the analytic event to processor");
                return 0;
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public int a(List<AnalyticEvent> list, AnalyticContext analyticContext, boolean bl) {
            Log.d("AnalyticFrameworkService", "reportAnalyticEvents");
            try {
                com.samsung.android.spayfw.core.a.a.l(AnalyticFrameworkService.this.mContext).a(list, analyticContext);
                do {
                    return 0;
                    break;
                } while (true);
            }
            catch (RemoteException remoteException) {
                remoteException.printStackTrace();
                Log.e("AnalyticFrameworkService", "Exception while sending the analytic event to processor");
                return 0;
            }
        }
    };
    private Context mContext;
    private Handler mHandler;

    private void a(AnalyticContext analyticContext) {
        Log.d("AnalyticFrameworkService", "updatePFVersion");
        if (bE == null) {
            bE = h.getPackageVersion(this.mContext, this.mContext.getPackageName());
        }
        Log.d("AnalyticFrameworkService", "PF version :" + bE);
        analyticContext.l(bE);
    }

    private void a(AnalyticEvent analyticEvent) {
        Log.d("AnalyticFrameworkService", "updateBin -");
        boolean bl = analyticEvent.L().equals((Object)AnalyticEvent.Type.he);
        String string = null;
        if (bl) {
            String string2 = analyticEvent.getValue(AnalyticEvent.Field.fq.getString());
            Log.d("AnalyticFrameworkService", "updateBin-- :" + string2);
            string = null;
            if (string2 != null) {
                boolean bl2 = string2.equals((Object)AnalyticEvent.Data.bZ.getString());
                string = null;
                if (bl2) {
                    Log.d("AnalyticFrameworkService", "filling Brand info");
                    String string3 = analyticEvent.getValue(AnalyticEvent.Field.fr.getString());
                    Log.d("AnalyticFrameworkService", "given bin:" + string3);
                    BinAttribute binAttribute = BinAttribute.getBinAttribute(string3);
                    string = null;
                    if (binAttribute != null) {
                        string = binAttribute.getCardBrand();
                    }
                    analyticEvent.a(AnalyticEvent.Field.fr, string);
                }
            }
        }
        Log.d("AnalyticFrameworkService", "cardBrand:" + string);
    }

    public static final void enable() {
        PaymentFrameworkApp.b(AnalyticFrameworkService.class);
        Log.d("AnalyticFrameworkService", "AnalyticFrameworkService is enabled");
    }

    public IBinder onBind(Intent intent) {
        Log.d("AnalyticFrameworkService", "onBind...");
        Log.i("AnalyticFrameworkService", "return auth binder");
        return this.bF;
    }

    public void onCreate() {
        Log.d("AnalyticFrameworkService", "onCreate...");
        super.onCreate();
        this.bD = new a();
        this.mHandler = PaymentFrameworkApp.az();
        this.mContext = this.getApplicationContext();
    }

    class a
    implements IBinder.DeathRecipient {
        a() {
        }

        public void binderDied() {
            Log.e("AnalyticFrameworkService", "DeathRecipient: Error: Wallet App died, handle clean up");
            n.q(AnalyticFrameworkService.this.mContext).clearCard();
        }
    }

}

