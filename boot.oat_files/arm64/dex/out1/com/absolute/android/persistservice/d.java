package com.absolute.android.persistservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.Iterator;

class d extends BroadcastReceiver {
    final /* synthetic */ ABTPersistenceService a;
    private boolean b;

    private d(ABTPersistenceService aBTPersistenceService) {
        this.a = aBTPersistenceService;
    }

    private void a() {
        if (!this.b) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            this.a.g.registerReceiver(this, intentFilter);
            this.b = true;
        }
    }

    private void b() {
        if (this.b) {
            this.a.g.unregisterReceiver(this);
            this.b = false;
        }
    }

    public void onReceive(Context context, Intent intent) {
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                synchronized (this.a.s) {
                    Iterator it = this.a.s.iterator();
                    while (it.hasNext()) {
                        this.a.a((String) it.next(), null, 20);
                    }
                    this.a.s.clear();
                    b();
                }
            }
        }
    }
}
