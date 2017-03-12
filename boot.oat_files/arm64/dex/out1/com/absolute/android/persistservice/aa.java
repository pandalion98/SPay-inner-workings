package com.absolute.android.persistservice;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

class aa extends Handler {
    final /* synthetic */ y a;

    public aa(y yVar, Looper looper) {
        this.a = yVar;
        super(looper);
    }

    public void handleMessage(Message message) {
        switch (message.what) {
            case 1:
                this.a.b((ab) message.obj);
                return;
            case 2:
                this.a.c();
                return;
            case 3:
                this.a.b((String) message.obj);
                return;
            case 4:
                this.a.d((String) message.obj);
                return;
            default:
                return;
        }
    }
}
