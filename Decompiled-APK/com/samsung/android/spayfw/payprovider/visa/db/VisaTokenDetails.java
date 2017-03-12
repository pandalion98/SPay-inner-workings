package com.samsung.android.spayfw.payprovider.visa.db;

import android.database.Cursor;
import com.samsung.android.spayfw.payprovider.visa.db.VisaTokenDetailsDao.VisaTokenGroup.TokenColumn;

/* renamed from: com.samsung.android.spayfw.payprovider.visa.db.a */
public class VisaTokenDetails {
    private int id;
    private int maxPmts;
    private String trTokenId;
    private String zR;
    private int zS;
    private long zT;
    private long zU;
    private long zV;

    public VisaTokenDetails(String str, String str2, int i, int i2, long j, long j2, long j3) {
        this.trTokenId = str;
        this.zR = str2;
        this.maxPmts = i;
        this.zS = i2;
        this.zT = j;
        this.zU = j2;
        this.zV = j3;
    }

    public VisaTokenDetails(Cursor cursor) {
        this.id = cursor.getInt(TokenColumn.ID.getColumnIndex());
        this.trTokenId = cursor.getString(TokenColumn.TR_TOKEN_ID.getColumnIndex());
        this.zR = cursor.getString(TokenColumn.PROVIDER_TOKEN_KEY.getColumnIndex());
        this.maxPmts = cursor.getInt(TokenColumn.MAX_PMTS.getColumnIndex());
        this.zS = cursor.getInt(TokenColumn.REPLENISH_PMTS.getColumnIndex());
        this.zT = cursor.getLong(TokenColumn.KEY_EXP_TS.getColumnIndex());
        this.zU = cursor.getLong(TokenColumn.KEY_REPLENISH_TS.getColumnIndex());
        this.zV = cursor.getLong(TokenColumn.LAST_TRANSACTION_FETCH.getColumnIndex());
    }

    public String getTrTokenId() {
        return this.trTokenId;
    }

    public String eH() {
        return this.zR;
    }

    public int getMaxPmts() {
        return this.maxPmts;
    }

    public int eI() {
        return this.zS;
    }

    public long eJ() {
        return this.zT;
    }

    public long eK() {
        return this.zU;
    }

    public long eL() {
        return this.zV;
    }

    public void setMaxPmts(int i) {
        this.maxPmts = i;
    }

    public void m1121R(int i) {
        this.zS = i;
    }

    public void m1122y(long j) {
        this.zT = j;
    }

    public void m1123z(long j) {
        this.zU = j;
    }

    public void m1120A(long j) {
        this.zV = j;
    }

    public String dump() {
        return "VisaTokenDetails { (" + TokenColumn.ID.getColumn() + ", \"" + this.id + "\"), (" + TokenColumn.TR_TOKEN_ID.getColumn() + ", \"" + this.trTokenId + "\"), (" + TokenColumn.MAX_PMTS.getColumn() + ", \"" + this.maxPmts + "\"), (" + TokenColumn.PROVIDER_TOKEN_KEY.getColumn() + ", \"" + this.zR + "\"), (" + TokenColumn.REPLENISH_PMTS.getColumn() + ", \"" + this.zS + "\"), (" + TokenColumn.KEY_EXP_TS.getColumn() + ", \"" + this.zT + "\"), (" + TokenColumn.KEY_REPLENISH_TS.getColumn() + ", \"" + this.zU + "\"), (" + TokenColumn.LAST_TRANSACTION_FETCH.getColumn() + ", \"" + this.zV + "\")}";
    }
}
