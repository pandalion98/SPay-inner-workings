package com.absolute.android.persistservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

class p extends BroadcastReceiver {
    final /* synthetic */ l a;
    private String b;

    p(l lVar, String str, String[] strArr) {
        this.a = lVar;
        this.b = str;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        if (strArr != null) {
            for (String str2 : strArr) {
                if (!str2.equals("android.intent.action.PACKAGE_RESTARTED") && str2.length() > 0) {
                    intentFilter.addAction(str2);
                }
            }
        }
        lVar.c.registerReceiver(this, intentFilter);
    }

    private void a() {
        this.a.c.unregisterReceiver(this);
    }

    public void onReceive(Context context, Intent intent) {
        boolean z = true;
        if (intent.getAction().equals("android.intent.action.PACKAGE_RESTARTED")) {
            if (intent.getDataString().toLowerCase().contains(this.b.toLowerCase())) {
                z = false;
            } else {
                return;
            }
        }
        this.a.a(this.b, z);
    }
}
