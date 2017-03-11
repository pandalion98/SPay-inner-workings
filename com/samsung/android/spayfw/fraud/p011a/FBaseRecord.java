package com.samsung.android.spayfw.fraud.p011a;

import android.content.ContentValues;

/* renamed from: com.samsung.android.spayfw.fraud.a.a */
public abstract class FBaseRecord {
    private String nG;

    public abstract ContentValues bC();

    public FBaseRecord(String str) {
        this.nG = str;
    }

    public final String bB() {
        return this.nG;
    }
}
