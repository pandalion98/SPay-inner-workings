/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteException
 *  android.text.TextUtils
 *  java.lang.CharSequence
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.storage.DbAdapter;
import com.samsung.android.spayfw.storage.models.a;
import java.util.ArrayList;
import java.util.List;

public class TokenRecordStorage
extends DbAdapter {
    private static TokenRecordStorage Cl;

    private TokenRecordStorage(Context context) {
        super(context);
        this.execSQL("CREATE TABLE IF NOT EXISTS token_group ( _id INTEGER PRIMARY KEY AUTOINCREMENT, enrollment_id TEXT NOT NULL UNIQUE, tr_token_id TEXT UNIQUE, user_id TEXT, app_id TEXT, provider_token_key TEXT, token_status TEXT, token_status_reason TEXT, card_brand TEXT, card_type TEXT, card_product TEXT, card_present_mode INTEGER, tnc_acceptance_time INTEGER, transaction_count INTEGER, transaction_url TEXT, data_1 TEXT, data_2 INTEGER, data_3 TEXT,  data_4 TEXT, data_5 TEXT, data_6 TEXT, data_7 BLOB, data_8 BLOB )");
        c.i("TokenRecordStorage", "Create TokenGroup Table If Not Exists");
    }

    public static final TokenRecordStorage ae(Context context) {
        Class<TokenRecordStorage> class_ = TokenRecordStorage.class;
        synchronized (TokenRecordStorage.class) {
            if (Cl == null) {
                Cl = new TokenRecordStorage(context);
            }
            TokenRecordStorage tokenRecordStorage = Cl;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return tokenRecordStorage;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private static ContentValues b(a a2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TokenGroup.TokenColumn.Co.getColumn(), a2.getTrTokenId());
        contentValues.put(TokenGroup.TokenColumn.Cp.getColumn(), a2.getUserId());
        contentValues.put(TokenGroup.TokenColumn.Cq.getColumn(), a2.getAppId());
        contentValues.put(TokenGroup.TokenColumn.Cr.getColumn(), a2.getTokenRefId());
        contentValues.put(TokenGroup.TokenColumn.Cs.getColumn(), a2.getTokenStatus());
        contentValues.put(TokenGroup.TokenColumn.Ct.getColumn(), a2.fy());
        contentValues.put(TokenGroup.TokenColumn.Cu.getColumn(), a2.getCardBrand());
        contentValues.put(TokenGroup.TokenColumn.Cv.getColumn(), a2.getCardType());
        contentValues.put(TokenGroup.TokenColumn.Cw.getColumn(), a2.fz());
        contentValues.put(TokenGroup.TokenColumn.Cx.getColumn(), Integer.valueOf((int)a2.ab()));
        contentValues.put(TokenGroup.TokenColumn.Cy.getColumn(), Long.valueOf((long)a2.fA()));
        contentValues.put(TokenGroup.TokenColumn.Cz.getColumn(), Long.valueOf((long)a2.fB()));
        contentValues.put(TokenGroup.TokenColumn.CA.getColumn(), a2.getTransactionUrl());
        contentValues.put(TokenGroup.TokenColumn.CB.getColumn(), a2.fD());
        String string = TokenGroup.TokenColumn.CC.getColumn();
        int n2 = a2.fw() ? 1 : 0;
        contentValues.put(string, Integer.valueOf((int)n2));
        contentValues.put(TokenGroup.TokenColumn.CD.getColumn(), a2.fx());
        contentValues.put(TokenGroup.TokenColumn.CE.getColumn(), a2.fE());
        return contentValues;
    }

    /*
     * Enabled aggressive block sorting
     */
    private static a i(Cursor cursor) {
        int n2 = 1;
        a a2 = new a(cursor.getString(TokenGroup.TokenColumn.Cn.getColumnIndex()));
        a2.setTrTokenId(cursor.getString(TokenGroup.TokenColumn.Co.getColumnIndex()));
        a2.setUserId(cursor.getString(TokenGroup.TokenColumn.Cp.getColumnIndex()));
        a2.bt(cursor.getString(TokenGroup.TokenColumn.Cq.getColumnIndex()));
        a2.setTokenRefId(cursor.getString(TokenGroup.TokenColumn.Cr.getColumnIndex()));
        a2.setTokenStatus(cursor.getString(TokenGroup.TokenColumn.Cs.getColumnIndex()));
        a2.H(cursor.getString(TokenGroup.TokenColumn.Ct.getColumnIndex()));
        a2.setCardBrand(cursor.getString(TokenGroup.TokenColumn.Cu.getColumnIndex()));
        a2.setCardType(cursor.getString(TokenGroup.TokenColumn.Cv.getColumnIndex()));
        a2.bu(cursor.getString(TokenGroup.TokenColumn.Cw.getColumnIndex()));
        a2.j(cursor.getInt(TokenGroup.TokenColumn.Cx.getColumnIndex()));
        a2.b(cursor.getLong(TokenGroup.TokenColumn.Cy.getColumnIndex()));
        a2.B(cursor.getLong(TokenGroup.TokenColumn.Cz.getColumnIndex()));
        a2.bv(cursor.getString(TokenGroup.TokenColumn.CA.getColumnIndex()));
        a2.bw(cursor.getString(TokenGroup.TokenColumn.CB.getColumnIndex()));
        if (cursor.getInt(TokenGroup.TokenColumn.CC.getColumnIndex()) != n2) {
            n2 = 0;
        }
        a2.i((boolean)n2);
        a2.bs(cursor.getString(TokenGroup.TokenColumn.CD.getColumnIndex()));
        a2.m(cursor.getBlob(TokenGroup.TokenColumn.CE.getColumnIndex()));
        return a2;
    }

    public int a(TokenGroup.TokenColumn tokenColumn, Object object, TokenGroup.TokenColumn tokenColumn2, String string) {
        if (tokenColumn == null || tokenColumn2 == null) {
            return -1;
        }
        return this.a("token_group", tokenColumn.getColumn(), object, tokenColumn.getColumnTypeFormat(), tokenColumn2.getColumn(), string);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public int a(TokenGroup.TokenColumn tokenColumn, String string) {
        String string2;
        if (tokenColumn == null) {
            string2 = null;
            do {
                return this.d("token_group", string2, string);
                break;
            } while (true);
        }
        string2 = tokenColumn.getColumn();
        return this.d("token_group", string2, string);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public List<String> a(TokenGroup.TokenColumn tokenColumn, TokenGroup.TokenColumn tokenColumn2, String string) {
        if (tokenColumn == null) {
            return null;
        }
        String string2 = null;
        if (tokenColumn2 == null) {
            do {
                return this.a("token_group", tokenColumn.getColumn(), string2, string);
                break;
            } while (true);
        }
        string2 = tokenColumn2.getColumn();
        return this.a("token_group", tokenColumn.getColumn(), string2, string);
    }

    public String b(TokenGroup.TokenColumn tokenColumn, String string) {
        if (string == null) {
            return null;
        }
        List<String> list = this.a(tokenColumn, TokenGroup.TokenColumn.Co, string);
        if (list != null) {
            return (String)list.get(0);
        }
        c.e("TokenRecordStorage", "getColomn: list is null");
        return null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public a bp(String string) {
        List<a> list;
        if (string == null || (list = this.c(TokenGroup.TokenColumn.Cn, string)) == null) {
            return null;
        }
        return (a)list.get(0);
    }

    public a bq(String string) {
        List<a> list = this.c(TokenGroup.TokenColumn.Co, string);
        if (list != null) {
            return (a)list.get(0);
        }
        c.e("TokenRecordStorage", "getTokenRecordByTrTokenId: cannot find token by tr token id");
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public byte[] br(String string) {
        if (TextUtils.isEmpty((CharSequence)string)) {
            return null;
        }
        List<a> list = this.c(TokenGroup.TokenColumn.Co, string);
        if (list == null) return null;
        if (list.size() <= 0) return null;
        a a2 = (a)list.get(0);
        if (a2 == null) return null;
        return a2.fE();
    }

    public int c(a a2) {
        if (a2 == null || a2.getEnrollmentId() == null) {
            return -1;
        }
        ContentValues contentValues = TokenRecordStorage.b(a2);
        contentValues.put(TokenGroup.TokenColumn.Cn.getColumn(), a2.getEnrollmentId());
        try {
            this.a("token_group", contentValues);
            return -1;
        }
        catch (SQLiteException sQLiteException) {
            c.e("TokenRecordStorage", "addTokenRecord: cannot add a token");
            c.c("TokenRecordStorage", sQLiteException.getMessage(), sQLiteException);
            return -1;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public List<a> c(TokenGroup.TokenColumn var1_1, String var2_2) {
        block6 : {
            if (var1_1 == null) {
                return null;
            }
            var5_4 = var6_3 = this.f("token_group", var1_1.getColumn(), var2_2);
            var7_5 = null;
            if (var5_4 == null) break block6;
            try {
                var8_6 = var5_4.getCount();
                var7_5 = null;
                if (var8_6 <= 0) break block6;
                var7_5 = new ArrayList(var5_4.getCount());
                while (var5_4.moveToNext()) {
                    var7_5.add((Object)TokenRecordStorage.i(var5_4));
                }
                break block6;
            }
            catch (Throwable var4_7) {}
            ** GOTO lbl-1000
        }
        TokenRecordStorage.a(var5_4);
        return var7_5;
        catch (Throwable var3_9) {
            var4_8 = var3_9;
            var5_4 = null;
        }
lbl-1000: // 2 sources:
        {
            TokenRecordStorage.a(var5_4);
            throw var4_8;
        }
    }

    public int d(TokenGroup.TokenColumn tokenColumn, String string) {
        if (tokenColumn == null) {
            return -1;
        }
        int n2 = this.e("token_group", tokenColumn.getColumn(), string);
        c.i("TokenRecordStorage", "deleteTokenRecord" + n2);
        return n2;
    }

    public int d(a a2) {
        if (a2 == null) {
            return -1;
        }
        return this.a("token_group", TokenRecordStorage.b(a2), TokenGroup.TokenColumn.Cn.getColumn(), a2.getEnrollmentId());
    }

    public int d(String string, byte[] arrby) {
        if (arrby == null || TextUtils.isEmpty((CharSequence)string)) {
            return -1;
        }
        return this.a(TokenGroup.TokenColumn.CE, arrby, TokenGroup.TokenColumn.Co, string);
    }

    public int fu() {
        return this.a(null, null);
    }

    public List<String> fv() {
        return this.a(TokenGroup.TokenColumn.Cn, null, null);
    }

    public static class TokenGroup {

        public static final class TokenColumn
        extends Enum<TokenColumn> {
            public static final /* enum */ TokenColumn CA;
            public static final /* enum */ TokenColumn CB;
            public static final /* enum */ TokenColumn CC;
            public static final /* enum */ TokenColumn CD;
            public static final /* enum */ TokenColumn CE;
            private static final /* synthetic */ TokenColumn[] CF;
            public static final /* enum */ TokenColumn Cm;
            public static final /* enum */ TokenColumn Cn;
            public static final /* enum */ TokenColumn Co;
            public static final /* enum */ TokenColumn Cp;
            public static final /* enum */ TokenColumn Cq;
            public static final /* enum */ TokenColumn Cr;
            public static final /* enum */ TokenColumn Cs;
            public static final /* enum */ TokenColumn Ct;
            public static final /* enum */ TokenColumn Cu;
            public static final /* enum */ TokenColumn Cv;
            public static final /* enum */ TokenColumn Cw;
            public static final /* enum */ TokenColumn Cx;
            public static final /* enum */ TokenColumn Cy;
            public static final /* enum */ TokenColumn Cz;
            private final int columnIndex;
            private final String columnName;
            private final DbAdapter.ColumnType columnType;
            private final String columnValue;

            static {
                Cm = new TokenColumn("ID", 0, "_id", DbAdapter.ColumnType.BV);
                Cn = new TokenColumn("ENROLLMENT_ID", 1, "enrollment_id", DbAdapter.ColumnType.BY);
                Co = new TokenColumn("TR_TOKEN_ID", 2, "tr_token_id", DbAdapter.ColumnType.BY);
                Cp = new TokenColumn("USER_ID", 3, "user_id", DbAdapter.ColumnType.BY);
                Cq = new TokenColumn("APP_ID", 4, "app_id", DbAdapter.ColumnType.BY);
                Cr = new TokenColumn("TOKEN_REF_ID", 5, "provider_token_key", DbAdapter.ColumnType.BY);
                Cs = new TokenColumn("TOKEN_STATUS", 6, "token_status", DbAdapter.ColumnType.BY);
                Ct = new TokenColumn("TOKEN_STATUS_REASON", 7, "token_status_reason", DbAdapter.ColumnType.BY);
                Cu = new TokenColumn("CARD_BRAND", 8, "card_brand", DbAdapter.ColumnType.BY);
                Cv = new TokenColumn("CARD_TYPE", 9, "card_type", DbAdapter.ColumnType.BY);
                Cw = new TokenColumn("CARD_PRODUCT", 10, "card_product", DbAdapter.ColumnType.BY);
                Cx = new TokenColumn("CARD_PRESENT_MODE", 11, "card_present_mode", DbAdapter.ColumnType.BV);
                Cy = new TokenColumn("TNC_ACCEPTANCE_TIME", 12, "tnc_acceptance_time", DbAdapter.ColumnType.BV);
                Cz = new TokenColumn("TRANSACTION_COUNT", 13, "transaction_count", DbAdapter.ColumnType.BV);
                CA = new TokenColumn("TRANSACTION_URL", 14, "transaction_url", DbAdapter.ColumnType.BY);
                CB = new TokenColumn("TRANSACTION_ID_LIST", 15, "data_1", DbAdapter.ColumnType.BY);
                CC = new TokenColumn("TRANSACTION_RETRY_ALLOWED", 16, "data_2", DbAdapter.ColumnType.BV);
                CD = new TokenColumn("CASH_CARD_ID", 17, "data_3", DbAdapter.ColumnType.BY);
                CE = new TokenColumn("USER_SIGNATURE_DATA", 21, "data_7", DbAdapter.ColumnType.BZ);
                TokenColumn[] arrtokenColumn = new TokenColumn[]{Cm, Cn, Co, Cp, Cq, Cr, Cs, Ct, Cu, Cv, Cw, Cx, Cy, Cz, CA, CB, CC, CD, CE};
                CF = arrtokenColumn;
            }

            private TokenColumn(String string2, int n3, String string3, DbAdapter.ColumnType columnType) {
                this.columnName = string2;
                this.columnIndex = n3;
                this.columnValue = string3;
                this.columnType = columnType;
            }

            public static TokenColumn valueOf(String string) {
                return (TokenColumn)Enum.valueOf(TokenColumn.class, (String)string);
            }

            public static TokenColumn[] values() {
                return (TokenColumn[])CF.clone();
            }

            public String getColumn() {
                return this.columnValue;
            }

            public int getColumnIndex() {
                return this.columnIndex;
            }

            protected DbAdapter.ColumnType getColumnTypeFormat() {
                return this.columnType;
            }
        }

    }

}

