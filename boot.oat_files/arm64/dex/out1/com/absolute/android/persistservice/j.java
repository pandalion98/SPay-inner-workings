package com.absolute.android.persistservice;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

class j extends Handler {
    final /* synthetic */ ABTPersistenceService a;

    public j(ABTPersistenceService aBTPersistenceService, Looper looper) {
        this.a = aBTPersistenceService;
        super(looper);
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                this.a.a((h) message.obj);
                return;
            case 2:
                this.a.b((h) message.obj);
                return;
            case 3:
                this.a.c((h) message.obj);
                return;
            case 4:
                this.a.a((g) message.obj);
                return;
            case 5:
                this.a.a((f) message.obj);
                return;
            case 6:
                this.a.a((e) message.obj);
                return;
            default:
                return;
        }
    }
}
