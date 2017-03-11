package com.samsung.android.spayfw.fraud.p011a;

import android.content.ContentValues;
import com.samsung.android.spayfw.appinterface.IdvMethod;
import com.samsung.android.spayfw.appinterface.PushMessage;
import com.samsung.android.spayfw.payprovider.mastercard.db.McDbContract.CardMaster;

/* renamed from: com.samsung.android.spayfw.fraud.a.d */
public class FTokenRecord extends FBaseRecord {
    private long id;
    private String nW;
    private long nX;
    private int status;

    /* renamed from: com.samsung.android.spayfw.fraud.a.d.a */
    public static class FTokenRecord extends FBaseRecord {
        private long id;
        private long nU;
        private int status;
        private long time;

        FTokenRecord(long j, long j2, int i, long j3) {
            super("ftoken_status_history");
            this.id = j;
            this.nU = j2;
            this.status = i;
            this.time = j3;
        }

        public ContentValues bC() {
            ContentValues contentValues = new ContentValues();
            contentValues.put("token_id", Long.valueOf(this.nU));
            contentValues.put(CardMaster.COL_STATUS, Integer.valueOf(this.status));
            contentValues.put("time", Long.valueOf(this.time));
            return contentValues;
        }

        public String toString() {
            ContentValues bC = bC();
            bC.put(PushMessage.JSON_KEY_ID, Long.valueOf(this.id));
            return bC.toString();
        }
    }

    /* renamed from: com.samsung.android.spayfw.fraud.a.d.b */
    public static class FTokenRecord extends FBaseRecord {
        private String amount;
        private String currency;
        private long id;
        private String method;
        private int nY;
        private long time;
        private long tokenId;
        private String transactionId;

        public FTokenRecord(long j, long j2, String str, String str2, String str3, String str4, int i, long j3) {
            super("ftoken_transaction_details");
            this.id = j;
            this.time = j2;
            this.method = str;
            this.amount = str2;
            this.currency = str3;
            this.transactionId = str4;
            this.nY = i;
            this.tokenId = j3;
        }

        public ContentValues bC() {
            ContentValues contentValues = new ContentValues();
            contentValues.put("time", Long.valueOf(this.time));
            contentValues.put("method", this.method);
            contentValues.put(IdvMethod.EXTRA_AMOUNT, this.amount);
            contentValues.put("currency", this.currency);
            contentValues.put("transaction_id", this.transactionId);
            contentValues.put("transaction_result", Integer.valueOf(this.nY));
            contentValues.put("token_id", Long.valueOf(this.tokenId));
            return contentValues;
        }

        public String toString() {
            ContentValues bC = bC();
            bC.put(PushMessage.JSON_KEY_ID, Long.valueOf(this.id));
            return bC.toString();
        }
    }

    public FTokenRecord(boolean z, long j, String str, int i, long j2) {
        super("ftoken");
        this.id = j;
        if (z) {
            str = FraudConstant.m697b(str, FraudConstant.salt);
        }
        this.nW = str;
        this.status = i;
        this.nX = j2;
    }

    public ContentValues bC() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("token_ref_key", this.nW);
        contentValues.put(CardMaster.COL_STATUS, Integer.valueOf(this.status));
        contentValues.put("card_id", Long.valueOf(this.nX));
        return contentValues;
    }

    public String toString() {
        ContentValues bC = bC();
        bC.put(PushMessage.JSON_KEY_ID, Long.valueOf(this.id));
        return bC.toString();
    }

    public long getId() {
        return this.id;
    }

    public String bF() {
        return this.nW;
    }

    public int getStatus() {
        return this.status;
    }

    public long bG() {
        return this.nX;
    }
}
