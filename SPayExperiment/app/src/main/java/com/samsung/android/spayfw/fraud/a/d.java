/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.String
 */
package com.samsung.android.spayfw.fraud.a;

import android.content.ContentValues;
import com.samsung.android.spayfw.fraud.a.e;

public class d
extends com.samsung.android.spayfw.fraud.a.a {
    private long id;
    private String nW;
    private long nX;
    private int status;

    public d(boolean bl, long l2, String string, int n2, long l3) {
        super("ftoken");
        this.id = l2;
        if (bl) {
            string = e.b(string, e.salt);
        }
        this.nW = string;
        this.status = n2;
        this.nX = l3;
    }

    @Override
    public ContentValues bC() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("token_ref_key", this.nW);
        contentValues.put("status", Integer.valueOf((int)this.status));
        contentValues.put("card_id", Long.valueOf((long)this.nX));
        return contentValues;
    }

    public String bF() {
        return this.nW;
    }

    public long bG() {
        return this.nX;
    }

    public long getId() {
        return this.id;
    }

    public int getStatus() {
        return this.status;
    }

    public String toString() {
        ContentValues contentValues = this.bC();
        contentValues.put("id", Long.valueOf((long)this.id));
        return contentValues.toString();
    }

    public static class a
    extends com.samsung.android.spayfw.fraud.a.a {
        private long id;
        private long nU;
        private int status;
        private long time;

        a(long l2, long l3, int n2, long l4) {
            super("ftoken_status_history");
            this.id = l2;
            this.nU = l3;
            this.status = n2;
            this.time = l4;
        }

        @Override
        public ContentValues bC() {
            ContentValues contentValues = new ContentValues();
            contentValues.put("token_id", Long.valueOf((long)this.nU));
            contentValues.put("status", Integer.valueOf((int)this.status));
            contentValues.put("time", Long.valueOf((long)this.time));
            return contentValues;
        }

        public String toString() {
            ContentValues contentValues = this.bC();
            contentValues.put("id", Long.valueOf((long)this.id));
            return contentValues.toString();
        }
    }

    public static class b
    extends com.samsung.android.spayfw.fraud.a.a {
        private String amount;
        private String currency;
        private long id;
        private String method;
        private int nY;
        private long time;
        private long tokenId;
        private String transactionId;

        public b(long l2, long l3, String string, String string2, String string3, String string4, int n2, long l4) {
            super("ftoken_transaction_details");
            this.id = l2;
            this.time = l3;
            this.method = string;
            this.amount = string2;
            this.currency = string3;
            this.transactionId = string4;
            this.nY = n2;
            this.tokenId = l4;
        }

        @Override
        public ContentValues bC() {
            ContentValues contentValues = new ContentValues();
            contentValues.put("time", Long.valueOf((long)this.time));
            contentValues.put("method", this.method);
            contentValues.put("amount", this.amount);
            contentValues.put("currency", this.currency);
            contentValues.put("transaction_id", this.transactionId);
            contentValues.put("transaction_result", Integer.valueOf((int)this.nY));
            contentValues.put("token_id", Long.valueOf((long)this.tokenId));
            return contentValues;
        }

        public String toString() {
            ContentValues contentValues = this.bC();
            contentValues.put("id", Long.valueOf((long)this.id));
            return contentValues.toString();
        }
    }

}

