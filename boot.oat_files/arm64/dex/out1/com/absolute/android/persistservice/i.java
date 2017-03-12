package com.absolute.android.persistservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.absolute.android.persistence.AppProfile;
import com.samsung.android.cocktailbar.AbsCocktailLoadablePanel;

class i extends BroadcastReceiver {
    final /* synthetic */ ABTPersistenceService a;

    private i(ABTPersistenceService aBTPersistenceService) {
        this.a = aBTPersistenceService;
    }

    private void a() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REPLACED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addDataScheme(AbsCocktailLoadablePanel.PACKAGE_NAME);
        this.a.g.registerReceiver(this, intentFilter);
    }

    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.PACKAGE_REMOVED".equals(intent.getAction())) {
            String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
            PersistedAppInfo h = this.a.j.h(schemeSpecificPart);
            if (h != null) {
                AppProfile a = h.a();
                int d = h.d();
                if (a != null && this.a.e(schemeSpecificPart) != a.getVersion() && (d & 1) == 0) {
                    this.a.i.c(intent.getAction() + " for package: " + schemeSpecificPart);
                    if (a.getIsMonitored()) {
                        this.a.t.a(schemeSpecificPart);
                    }
                    if (this.a.f.a() == 1 && a.getIsPersisted() && (d & 2) == 0) {
                        this.a.a(schemeSpecificPart, false);
                    } else if (!a.getIsPersisted()) {
                        this.a.i.c("Cleaning up to remove AppProfile of non-persisted package: " + schemeSpecificPart);
                        this.a.g(schemeSpecificPart);
                    }
                }
            }
        } else if ("android.intent.action.PACKAGE_ADDED".equals(intent.getAction()) || "android.intent.action.PACKAGE_REPLACED".equals(intent.getAction())) {
            String schemeSpecificPart2 = intent.getData().getSchemeSpecificPart();
            AppProfile b = this.a.k.b(schemeSpecificPart2);
            if (b != null && (this.a.j.g(schemeSpecificPart2) & 16) != 0) {
                this.a.i.c(intent.getAction() + " for package: " + schemeSpecificPart2 + " for install attempt which previously timed out. Completing post install ops.");
                this.a.a(schemeSpecificPart2, b.getVersion(), b, this.a.k.d(schemeSpecificPart2), this.a.k.e(schemeSpecificPart2), null);
            }
        }
    }
}
