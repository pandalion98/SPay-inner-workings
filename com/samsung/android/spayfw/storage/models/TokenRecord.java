package com.samsung.android.spayfw.storage.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.samsung.android.spayfw.storage.TokenRecordStorage.TokenGroup.TokenColumn;
import java.util.List;

/* renamed from: com.samsung.android.spayfw.storage.models.a */
public class TokenRecord {
    private static final Gson sGson;
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
    private long jb;
    private String kd;
    private String mCardBrand;
    private String mEnrollmentId;
    private String mF;
    private String oZ;

    /* renamed from: com.samsung.android.spayfw.storage.models.a.1 */
    class TokenRecord extends TypeToken<List<String>> {
        final /* synthetic */ TokenRecord CQ;

        TokenRecord(TokenRecord tokenRecord) {
            this.CQ = tokenRecord;
        }
    }

    /* renamed from: com.samsung.android.spayfw.storage.models.a.2 */
    class TokenRecord extends TypeToken<List<String>> {
        final /* synthetic */ TokenRecord CQ;

        TokenRecord(TokenRecord tokenRecord) {
            this.CQ = tokenRecord;
        }
    }

    static {
        sGson = new GsonBuilder().disableHtmlEscaping().create();
    }

    public TokenRecord(String str) {
        this.jb = -1;
        this.mEnrollmentId = str;
    }

    public boolean fw() {
        return this.CM;
    }

    public void m1254i(boolean z) {
        this.CM = z;
    }

    public String fx() {
        return this.CO;
    }

    public void bs(String str) {
        this.CO = str;
    }

    public String getEnrollmentId() {
        return this.mEnrollmentId;
    }

    public String getTrTokenId() {
        return this.mF;
    }

    public void setTrTokenId(String str) {
        this.mF = str;
    }

    public String getUserId() {
        return this.bN;
    }

    public void setUserId(String str) {
        this.bN = str;
    }

    public String getAppId() {
        return this.CG;
    }

    public void bt(String str) {
        this.CG = str;
    }

    public String getTokenRefId() {
        return this.oZ;
    }

    public void setTokenRefId(String str) {
        this.oZ = str;
    }

    public String getTokenStatus() {
        return this.kd;
    }

    public void setTokenStatus(String str) {
        this.kd = str;
    }

    public String fy() {
        return this.CH;
    }

    public void m1251H(String str) {
        this.CH = str;
    }

    public String getCardBrand() {
        return this.mCardBrand;
    }

    public void setCardBrand(String str) {
        this.mCardBrand = str;
    }

    public String getCardType() {
        return this.iU;
    }

    public void setCardType(String str) {
        this.iU = str;
    }

    public String fz() {
        return this.CI;
    }

    public void bu(String str) {
        this.CI = str;
    }

    public int ab() {
        return this.iV;
    }

    public void m1255j(int i) {
        this.iV = i;
    }

    public long fA() {
        return this.jb;
    }

    public void m1252b(long j) {
        this.jb = j;
    }

    public long fB() {
        return this.CJ;
    }

    public void m1250B(long j) {
        this.CJ = j;
    }

    public String getTransactionUrl() {
        return this.CK;
    }

    public void bv(String str) {
        this.CK = str;
    }

    public List<String> fC() {
        return (List) sGson.fromJson(this.CL, new TokenRecord(this).getType());
    }

    public void m1253c(List<String> list) {
        this.CL = sGson.toJson((Object) list, new TokenRecord(this).getType());
    }

    public String fD() {
        return this.CL;
    }

    public void bw(String str) {
        this.CL = str;
    }

    public byte[] fE() {
        return this.CP;
    }

    public void m1256m(byte[] bArr) {
        this.CP = bArr;
    }

    public String dump() {
        return "TokenRecord { (" + TokenColumn.ENROLLMENT_ID.getColumn() + ", \"" + this.mEnrollmentId + "\"), (" + TokenColumn.TR_TOKEN_ID.getColumn() + ", \"" + this.mF + "\"), (" + TokenColumn.APP_ID.getColumn() + ", \"" + this.CG + "\"), (" + TokenColumn.TOKEN_REF_ID.getColumn() + ", \"" + this.oZ + "\"), (" + TokenColumn.CARD_TYPE.getColumn() + ", \"" + this.iU + "\"), (" + TokenColumn.CARD_PRESENT_MODE.getColumn() + ", \"" + this.iV + "\"), (" + TokenColumn.TOKEN_STATUS.getColumn() + ", \"" + this.kd + "\"), (" + TokenColumn.TOKEN_STATUS_REASON.getColumn() + ", \"" + this.CH + "\"), (" + TokenColumn.TNC_ACCEPTANCE_TIME.getColumn() + ", \"" + this.jb + "\"), (" + TokenColumn.TRANSACTION_COUNT.getColumn() + ", \"" + this.CJ + "\"), (" + TokenColumn.TRANSACTION_URL.getColumn() + ", \"" + this.CK + "\")}";
    }
}
