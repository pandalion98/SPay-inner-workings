package com.absolute.android.persistservice;

import android.os.FileObserver;

class z extends FileObserver {
    final /* synthetic */ y a;
    private String b;

    public z(y yVar, String str) {
        this.a = yVar;
        super(str, 1732);
        this.b = str;
    }

    public void onEvent(int i, String str) {
        String str2 = str == null ? this.b : this.b + str;
        switch (65535 & i) {
            case 4:
                this.a.c(str2);
                return;
            case 64:
            case 128:
            case 512:
            case 1024:
                this.a.a(str2, false);
                return;
            default:
                return;
        }
    }
}
