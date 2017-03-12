package com.absolute.android.persistservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.io.File;

class c extends BroadcastReceiver {
    final /* synthetic */ ABTPersistenceService a;

    private c(ABTPersistenceService aBTPersistenceService) {
        this.a = aBTPersistenceService;
    }

    private void a() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        this.a.g.registerReceiver(this, intentFilter);
    }

    public void onReceive(Context context, Intent intent) {
        int i = 0;
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            File file = new File(ABTPersistenceService.a());
            if (!(file.exists() && file.canRead() && file.canWrite())) {
                this.a.i.a("Directory " + ABTPersistenceService.a() + " is not mounted or not accessible.", null);
            }
            this.a.a(true);
            if (this.a.a) {
                this.a.b(true);
            }
            if (this.a.j.b() == 0) {
                try {
                    File file2 = new File(ABTPersistenceService.c);
                    if (file2.exists()) {
                        String[] list = file2.list(new b(this.a));
                        if (list != null && list.length > 0) {
                            int length = list.length;
                            while (i < length) {
                                this.a.i.c("Absolute pre-install App: " + list[i]);
                                i++;
                            }
                            i = 86400;
                        }
                        this.a.i.c("Total duration for sending PS_READY set to : " + i);
                        if (i > 0) {
                            this.a.a(i);
                        }
                    }
                } catch (Throwable th) {
                    this.a.i.b("Exception checking for pre-install apps: " + th.getMessage());
                }
                i = 600;
                this.a.i.c("Total duration for sending PS_READY set to : " + i);
                if (i > 0) {
                    this.a.a(i);
                }
            }
        }
    }
}
