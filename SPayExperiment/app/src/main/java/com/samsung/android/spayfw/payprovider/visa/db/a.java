/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.database.Cursor
 *  java.lang.Object
 *  java.lang.String
 */
package com.samsung.android.spayfw.payprovider.visa.db;

import android.database.Cursor;
import com.samsung.android.spayfw.payprovider.visa.db.VisaTokenDetailsDao;

public class a {
    private int id;
    private int maxPmts;
    private String trTokenId;
    private String zR;
    private int zS;
    private long zT;
    private long zU;
    private long zV;

    public a(Cursor cursor) {
        this.id = cursor.getInt(VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.ID.getColumnIndex());
        this.trTokenId = cursor.getString(VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.TR_TOKEN_ID.getColumnIndex());
        this.zR = cursor.getString(VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.PROVIDER_TOKEN_KEY.getColumnIndex());
        this.maxPmts = cursor.getInt(VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.MAX_PMTS.getColumnIndex());
        this.zS = cursor.getInt(VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.REPLENISH_PMTS.getColumnIndex());
        this.zT = cursor.getLong(VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.KEY_EXP_TS.getColumnIndex());
        this.zU = cursor.getLong(VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.KEY_REPLENISH_TS.getColumnIndex());
        this.zV = cursor.getLong(VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.LAST_TRANSACTION_FETCH.getColumnIndex());
    }

    public a(String string, String string2, int n2, int n3, long l2, long l3, long l4) {
        this.trTokenId = string;
        this.zR = string2;
        this.maxPmts = n2;
        this.zS = n3;
        this.zT = l2;
        this.zU = l3;
        this.zV = l4;
    }

    public void A(long l2) {
        this.zV = l2;
    }

    public void R(int n2) {
        this.zS = n2;
    }

    public String dump() {
        return "VisaTokenDetails { (" + VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.ID.getColumn() + ", \"" + this.id + "\"), (" + VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.TR_TOKEN_ID.getColumn() + ", \"" + this.trTokenId + "\"), (" + VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.MAX_PMTS.getColumn() + ", \"" + this.maxPmts + "\"), (" + VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.PROVIDER_TOKEN_KEY.getColumn() + ", \"" + this.zR + "\"), (" + VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.REPLENISH_PMTS.getColumn() + ", \"" + this.zS + "\"), (" + VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.KEY_EXP_TS.getColumn() + ", \"" + this.zT + "\"), (" + VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.KEY_REPLENISH_TS.getColumn() + ", \"" + this.zU + "\"), (" + VisaTokenDetailsDao.VisaTokenGroup.TokenColumn.LAST_TRANSACTION_FETCH.getColumn() + ", \"" + this.zV + "\")}";
    }

    public String eH() {
        return this.zR;
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

    public int getMaxPmts() {
        return this.maxPmts;
    }

    public String getTrTokenId() {
        return this.trTokenId;
    }

    public void setMaxPmts(int n2) {
        this.maxPmts = n2;
    }

    public void y(long l2) {
        this.zT = l2;
    }

    public void z(long l2) {
        this.zU = l2;
    }
}

