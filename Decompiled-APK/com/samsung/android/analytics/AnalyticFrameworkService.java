package com.samsung.android.analytics;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;
import com.samsung.android.analytics.sdk.AnalyticContext;
import com.samsung.android.analytics.sdk.AnalyticEvent;
import com.samsung.android.analytics.sdk.AnalyticEvent.Data;
import com.samsung.android.analytics.sdk.AnalyticEvent.Field;
import com.samsung.android.analytics.sdk.AnalyticEvent.Type;
import com.samsung.android.analytics.sdk.IAnalyticsFramework.IAnalyticsFramework;
import com.samsung.android.spayfw.core.BinAttribute;
import com.samsung.android.spayfw.core.PaymentFrameworkApp;
import com.samsung.android.spayfw.core.p005a.AnalyticsFrameworkProcessor;
import com.samsung.android.spayfw.core.p005a.PaymentProcessor;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.utils.Utils;
import java.util.List;

public class AnalyticFrameworkService extends Service {
    static String bE;
    private C0322a bD;
    private final IAnalyticsFramework bF;
    private Context mContext;
    private Handler mHandler;

    /* renamed from: com.samsung.android.analytics.AnalyticFrameworkService.1 */
    class C03211 extends IAnalyticsFramework {
        final /* synthetic */ AnalyticFrameworkService bG;

        C03211(AnalyticFrameworkService analyticFrameworkService) {
            this.bG = analyticFrameworkService;
        }

        public int m142a(AnalyticEvent analyticEvent, AnalyticContext analyticContext, boolean z) {
            Log.m285d("AnalyticFrameworkService", "reportAnalyticEvent: AnalyticEvent:" + analyticEvent + " , AnalyticContext: " + analyticContext);
            this.bG.m147a(analyticContext);
            this.bG.m148a(analyticEvent);
            try {
                AnalyticsFrameworkProcessor.m340l(this.bG.mContext).m341a(analyticEvent, analyticContext);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.m286e("AnalyticFrameworkService", "Exception while sending the analytic event to processor");
            }
            return 0;
        }

        public int m143a(List<AnalyticEvent> list, AnalyticContext analyticContext, boolean z) {
            Log.m285d("AnalyticFrameworkService", "reportAnalyticEvents");
            try {
                AnalyticsFrameworkProcessor.m340l(this.bG.mContext).m343a((List) list, analyticContext);
            } catch (RemoteException e) {
                e.printStackTrace();
                Log.m286e("AnalyticFrameworkService", "Exception while sending the analytic event to processor");
            }
            return 0;
        }
    }

    /* renamed from: com.samsung.android.analytics.AnalyticFrameworkService.a */
    class C0322a implements DeathRecipient {
        final /* synthetic */ AnalyticFrameworkService bG;

        C0322a(AnalyticFrameworkService analyticFrameworkService) {
            this.bG = analyticFrameworkService;
        }

        public void binderDied() {
            Log.m286e("AnalyticFrameworkService", "DeathRecipient: Error: Wallet App died, handle clean up");
            PaymentProcessor.m470q(this.bG.mContext).clearCard();
        }
    }

    public AnalyticFrameworkService() {
        this.bD = null;
        this.bF = new C03211(this);
    }

    static {
        bE = null;
    }

    public void onCreate() {
        Log.m285d("AnalyticFrameworkService", "onCreate...");
        super.onCreate();
        this.bD = new C0322a(this);
        this.mHandler = PaymentFrameworkApp.az();
        this.mContext = getApplicationContext();
    }

    public IBinder onBind(Intent intent) {
        Log.m285d("AnalyticFrameworkService", "onBind...");
        Log.m287i("AnalyticFrameworkService", "return auth binder");
        return this.bF;
    }

    public static final void enable() {
        PaymentFrameworkApp.m318b(AnalyticFrameworkService.class);
        Log.m285d("AnalyticFrameworkService", "AnalyticFrameworkService is enabled");
    }

    private void m147a(AnalyticContext analyticContext) {
        Log.m285d("AnalyticFrameworkService", "updatePFVersion");
        if (bE == null) {
            bE = Utils.getPackageVersion(this.mContext, this.mContext.getPackageName());
        }
        Log.m285d("AnalyticFrameworkService", "PF version :" + bE);
        analyticContext.m160l(bE);
    }

    private void m148a(AnalyticEvent analyticEvent) {
        String str = null;
        Log.m285d("AnalyticFrameworkService", "updateBin -");
        if (analyticEvent.m166L().equals(Type.ATTEMPT_ADD_CARD)) {
            String value = analyticEvent.getValue(Field.CARD_CATEGORY.getString());
            Log.m285d("AnalyticFrameworkService", "updateBin-- :" + value);
            if (value != null && value.equals(Data.CARD_CATEGORY_CREDIT_OR_DEBIT_CARD.getString())) {
                Log.m285d("AnalyticFrameworkService", "filling Brand info");
                value = analyticEvent.getValue(Field.CARD_SUB_CATEGORY.getString());
                Log.m285d("AnalyticFrameworkService", "given bin:" + value);
                BinAttribute binAttribute = BinAttribute.getBinAttribute(value);
                if (binAttribute != null) {
                    str = binAttribute.getCardBrand();
                }
                analyticEvent.m168a(Field.CARD_SUB_CATEGORY, str);
            }
        }
        Log.m285d("AnalyticFrameworkService", "cardBrand:" + str);
    }
}
