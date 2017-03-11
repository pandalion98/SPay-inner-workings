package com.samsung.android.spayfw.fraud.p011a.p012a;

import android.content.ContentValues;
import com.samsung.android.spayfw.fraud.p011a.FBaseRecord;

/* renamed from: com.samsung.android.spayfw.fraud.a.a.a */
public class FDeviceRecord extends FBaseRecord {
    private long id;
    private String oc;
    private String od;
    private long oe;
    private String reason;
    private long time;

    public FDeviceRecord(long j, long j2, String str, String str2, String str3, long j3) {
        super("fdevice_info");
        this.id = j;
        this.time = j2;
        this.reason = str;
        this.oc = str2;
        this.od = str3;
        this.oe = j3;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id=");
        stringBuilder.append(this.id);
        stringBuilder.append(" ");
        stringBuilder.append("time=");
        stringBuilder.append(this.time);
        stringBuilder.append(" ");
        stringBuilder.append("reason=");
        stringBuilder.append(this.reason);
        stringBuilder.append(" ");
        stringBuilder.append("external_id=");
        stringBuilder.append(this.oc);
        stringBuilder.append(" ");
        stringBuilder.append("extras=");
        stringBuilder.append(this.od);
        stringBuilder.append(" ");
        stringBuilder.append("overflow=");
        stringBuilder.append(this.oe);
        return stringBuilder.toString();
    }

    public ContentValues bC() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("time", Long.valueOf(this.time));
        contentValues.put("reason", this.reason);
        contentValues.put("external_id", this.oc);
        contentValues.put("extras", this.od);
        contentValues.put("overflow", Long.valueOf(this.oe));
        return contentValues;
    }
}
