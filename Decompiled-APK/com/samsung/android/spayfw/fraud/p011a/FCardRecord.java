package com.samsung.android.spayfw.fraud.p011a;

import android.content.ContentValues;
import com.samsung.android.spayfw.appinterface.PushMessage;

/* renamed from: com.samsung.android.spayfw.fraud.a.b */
public class FCardRecord extends FBaseRecord {
    private String country;
    private long id;
    private String lastNameHash;
    private String nH;
    private long nI;
    private long nJ;
    private String nK;
    private int nL;
    private String nM;
    private String nN;
    private String nO;
    private String nP;
    private long nQ;
    private double nR;
    private double nS;
    private String nT;
    private long nU;
    private int result;

    public String getAccountId() {
        return this.nH;
    }

    public String bD() {
        return this.nK;
    }

    public String getFirstName() {
        return this.nM;
    }

    public String getLastName() {
        return this.nN;
    }

    public String bE() {
        return this.nO;
    }

    public String getZip() {
        return this.nP;
    }

    public FCardRecord(boolean z, long j, String str, long j2, long j3, String str2, int i, String str3, String str4, String str5, String str6, String str7, String str8) {
        super("fcard");
        if (z) {
            this.id = j;
            this.nH = FraudConstant.m697b(str, FraudConstant.salt);
            this.nI = j2;
            this.nJ = j3;
            this.nK = FraudConstant.m697b(str2, FraudConstant.salt);
            this.nL = i;
            this.nM = str3;
            this.nN = str4;
            this.lastNameHash = FraudConstant.m697b(str5, FraudConstant.salt);
            this.nO = FraudConstant.m697b(str6, FraudConstant.salt);
            this.nP = FraudConstant.m697b(str7, FraudConstant.salt);
            this.country = FraudConstant.m697b(str8, FraudConstant.salt);
            return;
        }
        this.id = j;
        this.nH = str;
        this.nI = j2;
        this.nJ = j3;
        this.nK = str2;
        this.nL = i;
        this.nM = str3;
        this.nN = str4;
        this.lastNameHash = str5;
        this.nO = str6;
        this.nP = str7;
        this.country = str8;
    }

    public void reset() {
        this.id = 0;
        this.nH = null;
        this.nI = 0;
        this.nJ = 0;
        this.nK = null;
        this.nL = 0;
        this.nM = null;
        this.nN = null;
        this.lastNameHash = null;
        this.nO = null;
        this.nP = null;
        this.country = null;
        this.nQ = 0;
        this.result = 0;
        this.nR = 0.0d;
        this.nS = 0.0d;
        this.nT = null;
        this.nU = 0;
    }

    public void m696a(long j, int i, double d, double d2, String str, long j2) {
        this.nQ = j;
        this.result = i;
        this.nR = d;
        this.nS = d2;
        this.nT = str;
        this.nU = j2;
    }

    public ContentValues bC() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_id", this.nH);
        contentValues.put("account_creation_time", Long.valueOf(this.nI));
        contentValues.put("account_ondevice_time", Long.valueOf(this.nJ));
        contentValues.put("four_digits", this.nK);
        contentValues.put("brand", Integer.valueOf(this.nL));
        contentValues.put("first_name", this.nM);
        contentValues.put("last_name", this.nN);
        contentValues.put("last_name_hash", this.lastNameHash);
        contentValues.put("avsaddr", this.nO);
        contentValues.put("avszip", this.nP);
        contentValues.put("country", this.country);
        contentValues.put("attempt_time", Long.valueOf(this.nQ));
        contentValues.put("result", Integer.valueOf(this.result));
        contentValues.put("geo_x", Double.valueOf(this.nR));
        contentValues.put("geo_y", Double.valueOf(this.nS));
        contentValues.put("device_name", this.nT);
        if (this.result == 0 || this.result == 3) {
            contentValues.put("token_id", Long.valueOf(this.nU));
        }
        return contentValues;
    }

    public String toString() {
        ContentValues bC = bC();
        bC.put(PushMessage.JSON_KEY_ID, Long.valueOf(this.id));
        return bC.toString();
    }
}
