/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteException
 *  java.lang.Enum
 *  java.lang.Integer
 *  java.lang.Long
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.payprovider.visa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import com.samsung.android.spayfw.b.c;
import com.samsung.android.spayfw.payprovider.visa.db.a;
import com.samsung.android.spayfw.storage.DbAdapter;
import java.util.ArrayList;
import java.util.List;

public class VisaTokenDetailsDao
extends DbAdapter {
    private final String nG = "visa_token_group";

    public VisaTokenDetailsDao(Context context) {
        super(context);
        this.execSQL("CREATE TABLE IF NOT EXISTS visa_token_group(_id INTEGER PRIMARY KEY AUTOINCREMENT, tr_token_id TEXT NOT NULL UNIQUE, max_pmts INTEGER, replenish_pmts INTEGER, key_exp_ts INTEGER, key_replenish_ts INTEGER, provider_token_key TEXT NOT NULL UNIQUE, data_1 TEXT, data_2 INTEGER DEFAULT 0, data_3 INTEGER, data_4 TEXT, data_5 BLOB, data_6 BLOB )");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private List<a> a(VisaTokenGroup.TokenColumn var1_1, String var2_2) {
        block6 : {
            if (var1_1 == null) {
                return null;
            }
            var5_4 = var6_3 = this.f("visa_token_group", var1_1.getColumn(), var2_2);
            var7_5 = null;
            if (var5_4 == null) break block6;
            try {
                var8_6 = var5_4.getCount();
                var7_5 = null;
                if (var8_6 <= 0) break block6;
                var7_5 = new ArrayList(var5_4.getCount());
                while (var5_4.moveToNext()) {
                    var7_5.add((Object)new a(var5_4));
                }
                break block6;
            }
            catch (Throwable var4_7) {}
            ** GOTO lbl-1000
        }
        VisaTokenDetailsDao.a(var5_4);
        return var7_5;
        catch (Throwable var3_9) {
            var4_8 = var3_9;
            var5_4 = null;
        }
lbl-1000: // 2 sources:
        {
            VisaTokenDetailsDao.a(var5_4);
            throw var4_8;
        }
    }

    private static ContentValues b(a a2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(VisaTokenGroup.TokenColumn.TR_TOKEN_ID.getColumn(), a2.getTrTokenId());
        contentValues.put(VisaTokenGroup.TokenColumn.PROVIDER_TOKEN_KEY.getColumn(), a2.eH());
        contentValues.put(VisaTokenGroup.TokenColumn.MAX_PMTS.getColumn(), Integer.valueOf((int)a2.getMaxPmts()));
        contentValues.put(VisaTokenGroup.TokenColumn.REPLENISH_PMTS.getColumn(), Integer.valueOf((int)a2.eI()));
        contentValues.put(VisaTokenGroup.TokenColumn.KEY_EXP_TS.getColumn(), Long.valueOf((long)a2.eJ()));
        contentValues.put(VisaTokenGroup.TokenColumn.KEY_REPLENISH_TS.getColumn(), Long.valueOf((long)a2.eK()));
        contentValues.put(VisaTokenGroup.TokenColumn.LAST_TRANSACTION_FETCH.getColumn(), Long.valueOf((long)a2.eL()));
        return contentValues;
    }

    public int a(VisaTokenGroup.TokenColumn tokenColumn, Object object, String string) {
        if (tokenColumn == null) {
            return -1;
        }
        return this.a("visa_token_group", tokenColumn.getColumn(), object, tokenColumn.getColumnTypeFormat(), VisaTokenGroup.TokenColumn.PROVIDER_TOKEN_KEY.getColumn(), string);
    }

    public a aZ(String string) {
        List<a> list = this.a(VisaTokenGroup.TokenColumn.TR_TOKEN_ID, string);
        if (list != null) {
            return (a)list.get(0);
        }
        c.e("VisaTokenDetailsDao", "cannot find token by tr token id");
        return null;
    }

    public a ba(String string) {
        List<a> list = this.a(VisaTokenGroup.TokenColumn.PROVIDER_TOKEN_KEY, string);
        if (list != null) {
            return (a)list.get(0);
        }
        c.e("VisaTokenDetailsDao", "cannot find token by provider token id");
        return null;
    }

    public int bb(String string) {
        return this.e("visa_token_group", VisaTokenGroup.TokenColumn.PROVIDER_TOKEN_KEY.getColumn(), string);
    }

    public int c(a a2) {
        if (a2 == null || a2.getTrTokenId() == null) {
            return -1;
        }
        ContentValues contentValues = VisaTokenDetailsDao.b(a2);
        contentValues.put(VisaTokenGroup.TokenColumn.TR_TOKEN_ID.getColumn(), a2.getTrTokenId());
        try {
            int n2 = this.a("visa_token_group", contentValues);
            return n2;
        }
        catch (SQLiteException sQLiteException) {
            c.e("VisaTokenDetailsDao", "cannot add token");
            c.c("VisaTokenDetailsDao", sQLiteException.getMessage(), sQLiteException);
            return -1;
        }
    }

    public int d(a a2) {
        if (a2 == null) {
            return -1;
        }
        return this.a("visa_token_group", VisaTokenDetailsDao.b(a2), VisaTokenGroup.TokenColumn.TR_TOKEN_ID.getColumn(), a2.getTrTokenId());
    }

    public List<String> eM() {
        return this.a("visa_token_group", VisaTokenGroup.TokenColumn.PROVIDER_TOKEN_KEY.getColumn(), null, null);
    }

    public static class VisaTokenGroup {
        public static final String TABLE_NAME = "visa_token_group";
        public static final String TOKEN_GROUP_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS visa_token_group(_id INTEGER PRIMARY KEY AUTOINCREMENT, tr_token_id TEXT NOT NULL UNIQUE, max_pmts INTEGER, replenish_pmts INTEGER, key_exp_ts INTEGER, key_replenish_ts INTEGER, provider_token_key TEXT NOT NULL UNIQUE, data_1 TEXT, data_2 INTEGER DEFAULT 0, data_3 INTEGER, data_4 TEXT, data_5 BLOB, data_6 BLOB )";

        public static final class TokenColumn
        extends Enum<TokenColumn> {
            public static final /* enum */ TokenColumn ID = new TokenColumn("ID", 0, "_id", DbAdapter.ColumnType.BV);
            public static final /* enum */ TokenColumn KEY_EXP_TS;
            public static final /* enum */ TokenColumn KEY_REPLENISH_TS;
            public static final /* enum */ TokenColumn LAST_TRANSACTION_FETCH;
            public static final /* enum */ TokenColumn MAX_PMTS;
            public static final /* enum */ TokenColumn PROVIDER_TOKEN_KEY;
            public static final /* enum */ TokenColumn REPLENISH_PMTS;
            public static final /* enum */ TokenColumn TR_TOKEN_ID;
            private static final /* synthetic */ TokenColumn[] zW;
            private final int columnIndex;
            private final String columnName;
            private final DbAdapter.ColumnType columnType;
            private final String columnValue;

            static {
                TR_TOKEN_ID = new TokenColumn("TR_TOKEN_ID", 1, "tr_token_id", DbAdapter.ColumnType.BY);
                MAX_PMTS = new TokenColumn("MAX_PMTS", 2, "max_pmts", DbAdapter.ColumnType.BV);
                REPLENISH_PMTS = new TokenColumn("REPLENISH_PMTS", 3, "replenish_pmts", DbAdapter.ColumnType.BV);
                KEY_EXP_TS = new TokenColumn("KEY_EXP_TS", 4, "key_exp_ts", DbAdapter.ColumnType.BW);
                KEY_REPLENISH_TS = new TokenColumn("KEY_REPLENISH_TS", 5, "key_replenish_ts", DbAdapter.ColumnType.BW);
                PROVIDER_TOKEN_KEY = new TokenColumn("PROVIDER_TOKEN_KEY", 6, "provider_token_key", DbAdapter.ColumnType.BY);
                LAST_TRANSACTION_FETCH = new TokenColumn("data_2", 8, "data_2", DbAdapter.ColumnType.BW);
                TokenColumn[] arrtokenColumn = new TokenColumn[]{ID, TR_TOKEN_ID, MAX_PMTS, REPLENISH_PMTS, KEY_EXP_TS, KEY_REPLENISH_TS, PROVIDER_TOKEN_KEY, LAST_TRANSACTION_FETCH};
                zW = arrtokenColumn;
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
                return (TokenColumn[])zW.clone();
            }

            public String getColumn() {
                return this.columnValue;
            }

            public int getColumnIndex() {
                return this.columnIndex;
            }

            public String getColumnType() {
                return this.columnType.fs();
            }

            public DbAdapter.ColumnType getColumnTypeFormat() {
                return this.columnType;
            }
        }

    }

}

