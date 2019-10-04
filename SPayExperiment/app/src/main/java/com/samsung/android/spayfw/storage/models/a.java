/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.reflect.TypeToken
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.reflect.Type
 *  java.util.List
 */
package com.samsung.android.spayfw.storage.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.samsung.android.spayfw.storage.TokenRecordStorage;
import java.lang.reflect.Type;
import java.util.List;

public class a {
    private static final Gson sGson = new GsonBuilder().disableHtmlEscaping().create();
    private String CG;
    private String CH;
    private String CI;
    private long CJ;
    private String CK;
    private String CL;
    private boolean CM;
    private String CO;
    private byte[] CP;
    private String bN;
    private String iU;
    private int iV;
    private long jb = -1L;
    private String kd;
    private String mCardBrand;
    private String mEnrollmentId;
    private String mF;
    private String oZ;

    public a(String string) {
        this.mEnrollmentId = string;
    }

    public void B(long l2) {
        this.CJ = l2;
    }

    public void H(String string) {
        this.CH = string;
    }

    public int ab() {
        return this.iV;
    }

    public void b(long l2) {
        this.jb = l2;
    }

    public void bs(String string) {
        this.CO = string;
    }

    public void bt(String string) {
        this.CG = string;
    }

    public void bu(String string) {
        this.CI = string;
    }

    public void bv(String string) {
        this.CK = string;
    }

    public void bw(String string) {
        this.CL = string;
    }

    public void c(List<String> list) {
        this.CL = sGson.toJson(list, new TypeToken<List<String>>(){}.getType());
    }

    public String dump() {
        return "TokenRecord { (" + TokenRecordStorage.TokenGroup.TokenColumn.Cn.getColumn() + ", \"" + this.mEnrollmentId + "\"), (" + TokenRecordStorage.TokenGroup.TokenColumn.Co.getColumn() + ", \"" + this.mF + "\"), (" + TokenRecordStorage.TokenGroup.TokenColumn.Cq.getColumn() + ", \"" + this.CG + "\"), (" + TokenRecordStorage.TokenGroup.TokenColumn.Cr.getColumn() + ", \"" + this.oZ + "\"), (" + TokenRecordStorage.TokenGroup.TokenColumn.Cv.getColumn() + ", \"" + this.iU + "\"), (" + TokenRecordStorage.TokenGroup.TokenColumn.Cx.getColumn() + ", \"" + this.iV + "\"), (" + TokenRecordStorage.TokenGroup.TokenColumn.Cs.getColumn() + ", \"" + this.kd + "\"), (" + TokenRecordStorage.TokenGroup.TokenColumn.Ct.getColumn() + ", \"" + this.CH + "\"), (" + TokenRecordStorage.TokenGroup.TokenColumn.Cy.getColumn() + ", \"" + this.jb + "\"), (" + TokenRecordStorage.TokenGroup.TokenColumn.Cz.getColumn() + ", \"" + this.CJ + "\"), (" + TokenRecordStorage.TokenGroup.TokenColumn.CA.getColumn() + ", \"" + this.CK + "\")}";
    }

    public long fA() {
        return this.jb;
    }

    public long fB() {
        return this.CJ;
    }

    public List<String> fC() {
        return (List)sGson.fromJson(this.CL, new TypeToken<List<String>>(){}.getType());
    }

    public String fD() {
        return this.CL;
    }

    public byte[] fE() {
        return this.CP;
    }

    public boolean fw() {
        return this.CM;
    }

    public String fx() {
        return this.CO;
    }

    public String fy() {
        return this.CH;
    }

    public String fz() {
        return this.CI;
    }

    public String getAppId() {
        return this.CG;
    }

    public String getCardBrand() {
        return this.mCardBrand;
    }

    public String getCardType() {
        return this.iU;
    }

    public String getEnrollmentId() {
        return this.mEnrollmentId;
    }

    public String getTokenRefId() {
        return this.oZ;
    }

    public String getTokenStatus() {
        return this.kd;
    }

    public String getTrTokenId() {
        return this.mF;
    }

    public String getTransactionUrl() {
        return this.CK;
    }

    public String getUserId() {
        return this.bN;
    }

    public void i(boolean bl) {
        this.CM = bl;
    }

    public void j(int n2) {
        this.iV = n2;
    }

    public void m(byte[] arrby) {
        this.CP = arrby;
    }

    public void setCardBrand(String string) {
        this.mCardBrand = string;
    }

    public void setCardType(String string) {
        this.iU = string;
    }

    public void setTokenRefId(String string) {
        this.oZ = string;
    }

    public void setTokenStatus(String string) {
        this.kd = string;
    }

    public void setTrTokenId(String string) {
        this.mF = string;
    }

    public void setUserId(String string) {
        this.bN = string;
    }

}

