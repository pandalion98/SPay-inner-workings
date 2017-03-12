package com.absolute.android.persistservice;

import android.os.FileObserver;

class ab extends FileObserver {
    final /* synthetic */ y a;
    private ac b;
    private boolean c;

    public ab(y yVar, ac acVar) {
        this.a = yVar;
        super(acVar.f, 2);
        this.b = acVar;
    }

    public void onEvent(int i, String str) {
        String str2 = str == null ? this.b.f : this.b.f + str;
        switch (i) {
            case 2:
                if (!this.c) {
                    this.a.a(str2, true);
                    return;
                }
                return;
            default:
                return;
        }
    }
}
