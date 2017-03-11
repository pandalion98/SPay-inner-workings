package com.samsung.android.spayfw.fraud.p011a;

import android.content.ContentValues;

/* renamed from: com.samsung.android.spayfw.fraud.a.c */
public class FCounterRecord extends FBaseRecord {
    private String nV;
    private long time;

    public FCounterRecord(String str, long j) {
        super("fcounter");
        this.nV = str;
        this.time = j;
    }

    public ContentValues bC() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("enroll_id", this.nV);
        contentValues.put("time", Long.valueOf(this.time));
        return contentValues;
    }
}
