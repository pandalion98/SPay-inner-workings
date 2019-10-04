/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  java.lang.Double
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.String
 */
package com.samsung.android.spayfw.fraud.a;

import android.content.ContentValues;
import com.samsung.android.spayfw.fraud.a.a;
import com.samsung.android.spayfw.fraud.a.e;

public class b
extends a {
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

    public b(boolean bl, long l2, String string, long l3, long l4, String string2, int n2, String string3, String string4, String string5, String string6, String string7, String string8) {
        super("fcard");
        if (bl) {
            this.id = l2;
            this.nH = e.b(string, e.salt);
            this.nI = l3;
            this.nJ = l4;
            this.nK = e.b(string2, e.salt);
            this.nL = n2;
            this.nM = string3;
            this.nN = string4;
            this.lastNameHash = e.b(string5, e.salt);
            this.nO = e.b(string6, e.salt);
            this.nP = e.b(string7, e.salt);
            this.country = e.b(string8, e.salt);
            return;
        }
        this.id = l2;
        this.nH = string;
        this.nI = l3;
        this.nJ = l4;
        this.nK = string2;
        this.nL = n2;
        this.nM = string3;
        this.nN = string4;
        this.lastNameHash = string5;
        this.nO = string6;
        this.nP = string7;
        this.country = string8;
    }

    public void a(long l2, int n2, double d2, double d3, String string, long l3) {
        this.nQ = l2;
        this.result = n2;
        this.nR = d2;
        this.nS = d3;
        this.nT = string;
        this.nU = l3;
    }

    @Override
    public ContentValues bC() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_id", this.nH);
        contentValues.put("account_creation_time", Long.valueOf((long)this.nI));
        contentValues.put("account_ondevice_time", Long.valueOf((long)this.nJ));
        contentValues.put("four_digits", this.nK);
        contentValues.put("brand", Integer.valueOf((int)this.nL));
        contentValues.put("first_name", this.nM);
        contentValues.put("last_name", this.nN);
        contentValues.put("last_name_hash", this.lastNameHash);
        contentValues.put("avsaddr", this.nO);
        contentValues.put("avszip", this.nP);
        contentValues.put("country", this.country);
        contentValues.put("attempt_time", Long.valueOf((long)this.nQ));
        contentValues.put("result", Integer.valueOf((int)this.result));
        contentValues.put("geo_x", Double.valueOf((double)this.nR));
        contentValues.put("geo_y", Double.valueOf((double)this.nS));
        contentValues.put("device_name", this.nT);
        if (this.result == 0 || this.result == 3) {
            contentValues.put("token_id", Long.valueOf((long)this.nU));
        }
        return contentValues;
    }

    public String bD() {
        return this.nK;
    }

    public String bE() {
        return this.nO;
    }

    public String getAccountId() {
        return this.nH;
    }

    public String getFirstName() {
        return this.nM;
    }

    public String getLastName() {
        return this.nN;
    }

    public String getZip() {
        return this.nP;
    }

    public void reset() {
        this.id = 0L;
        this.nH = null;
        this.nI = 0L;
        this.nJ = 0L;
        this.nK = null;
        this.nL = 0;
        this.nM = null;
        this.nN = null;
        this.lastNameHash = null;
        this.nO = null;
        this.nP = null;
        this.country = null;
        this.nQ = 0L;
        this.result = 0;
        this.nR = 0.0;
        this.nS = 0.0;
        this.nT = null;
        this.nU = 0L;
    }

    public String toString() {
        ContentValues contentValues = this.bC();
        contentValues.put("id", Long.valueOf((long)this.id));
        return contentValues.toString();
    }
}

