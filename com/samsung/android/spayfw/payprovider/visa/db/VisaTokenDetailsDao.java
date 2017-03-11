package com.samsung.android.spayfw.payprovider.visa.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.storage.DbAdapter;
import com.samsung.android.spayfw.storage.DbAdapter.ColumnType;
import java.util.ArrayList;
import java.util.List;

public class VisaTokenDetailsDao extends DbAdapter {
    private final String nG;

    public static class VisaTokenGroup {
        public static final String TABLE_NAME = "visa_token_group";
        public static final String TOKEN_GROUP_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS visa_token_group(_id INTEGER PRIMARY KEY AUTOINCREMENT, tr_token_id TEXT NOT NULL UNIQUE, max_pmts INTEGER, replenish_pmts INTEGER, key_exp_ts INTEGER, key_replenish_ts INTEGER, provider_token_key TEXT NOT NULL UNIQUE, data_1 TEXT, data_2 INTEGER DEFAULT 0, data_3 INTEGER, data_4 TEXT, data_5 BLOB, data_6 BLOB )";

        public enum TokenColumn {
            ID("ID", 0, "_id", ColumnType.INTEGER),
            TR_TOKEN_ID("TR_TOKEN_ID", 1, "tr_token_id", ColumnType.TEXT),
            MAX_PMTS("MAX_PMTS", 2, "max_pmts", ColumnType.INTEGER),
            REPLENISH_PMTS("REPLENISH_PMTS", 3, "replenish_pmts", ColumnType.INTEGER),
            KEY_EXP_TS("KEY_EXP_TS", 4, "key_exp_ts", ColumnType.LONG),
            KEY_REPLENISH_TS("KEY_REPLENISH_TS", 5, "key_replenish_ts", ColumnType.LONG),
            PROVIDER_TOKEN_KEY("PROVIDER_TOKEN_KEY", 6, "provider_token_key", ColumnType.TEXT),
            LAST_TRANSACTION_FETCH("data_2", 8, "data_2", ColumnType.LONG);
            
            private final int columnIndex;
            private final String columnName;
            private final ColumnType columnType;
            private final String columnValue;

            private TokenColumn(String str, int i, String str2, ColumnType columnType) {
                this.columnName = str;
                this.columnIndex = i;
                this.columnValue = str2;
                this.columnType = columnType;
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

            public ColumnType getColumnTypeFormat() {
                return this.columnType;
            }
        }
    }

    private static ContentValues m1116b(VisaTokenDetails visaTokenDetails) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TokenColumn.TR_TOKEN_ID.getColumn(), visaTokenDetails.getTrTokenId());
        contentValues.put(TokenColumn.PROVIDER_TOKEN_KEY.getColumn(), visaTokenDetails.eH());
        contentValues.put(TokenColumn.MAX_PMTS.getColumn(), Integer.valueOf(visaTokenDetails.getMaxPmts()));
        contentValues.put(TokenColumn.REPLENISH_PMTS.getColumn(), Integer.valueOf(visaTokenDetails.eI()));
        contentValues.put(TokenColumn.KEY_EXP_TS.getColumn(), Long.valueOf(visaTokenDetails.eJ()));
        contentValues.put(TokenColumn.KEY_REPLENISH_TS.getColumn(), Long.valueOf(visaTokenDetails.eK()));
        contentValues.put(TokenColumn.LAST_TRANSACTION_FETCH.getColumn(), Long.valueOf(visaTokenDetails.eL()));
        return contentValues;
    }

    public VisaTokenDetailsDao(Context context) {
        super(context);
        this.nG = VisaTokenGroup.TABLE_NAME;
        execSQL(VisaTokenGroup.TOKEN_GROUP_TABLE_CREATE);
    }

    public List<String> eM() {
        return m1111a(VisaTokenGroup.TABLE_NAME, TokenColumn.PROVIDER_TOKEN_KEY.getColumn(), null, null);
    }

    public int m1117a(TokenColumn tokenColumn, Object obj, String str) {
        if (tokenColumn == null) {
            return -1;
        }
        return m1109a(VisaTokenGroup.TABLE_NAME, tokenColumn.getColumn(), obj, tokenColumn.getColumnTypeFormat(), TokenColumn.PROVIDER_TOKEN_KEY.getColumn(), str);
    }

    public VisaTokenDetails aZ(String str) {
        List a = m1115a(TokenColumn.TR_TOKEN_ID, str);
        if (a != null) {
            return (VisaTokenDetails) a.get(0);
        }
        Log.m286e("VisaTokenDetailsDao", "cannot find token by tr token id");
        return null;
    }

    public VisaTokenDetails ba(String str) {
        List a = m1115a(TokenColumn.PROVIDER_TOKEN_KEY, str);
        if (a != null) {
            return (VisaTokenDetails) a.get(0);
        }
        Log.m286e("VisaTokenDetailsDao", "cannot find token by provider token id");
        return null;
    }

    public int m1118c(VisaTokenDetails visaTokenDetails) {
        int i = -1;
        if (!(visaTokenDetails == null || visaTokenDetails.getTrTokenId() == null)) {
            ContentValues b = m1116b(visaTokenDetails);
            b.put(TokenColumn.TR_TOKEN_ID.getColumn(), visaTokenDetails.getTrTokenId());
            try {
                i = m1107a(VisaTokenGroup.TABLE_NAME, b);
            } catch (Throwable e) {
                Log.m286e("VisaTokenDetailsDao", "cannot add token");
                Log.m284c("VisaTokenDetailsDao", e.getMessage(), e);
            }
        }
        return i;
    }

    public int m1119d(VisaTokenDetails visaTokenDetails) {
        if (visaTokenDetails == null) {
            return -1;
        }
        return m1108a(VisaTokenGroup.TABLE_NAME, m1116b(visaTokenDetails), TokenColumn.TR_TOKEN_ID.getColumn(), visaTokenDetails.getTrTokenId());
    }

    public int bb(String str) {
        return m1113e(VisaTokenGroup.TABLE_NAME, TokenColumn.PROVIDER_TOKEN_KEY.getColumn(), str);
    }

    private List<VisaTokenDetails> m1115a(TokenColumn tokenColumn, String str) {
        Throwable th;
        List<VisaTokenDetails> list = null;
        if (tokenColumn != null) {
            Cursor f;
            try {
                f = m1114f(VisaTokenGroup.TABLE_NAME, tokenColumn.getColumn(), str);
                if (f != null) {
                    try {
                        if (f.getCount() > 0) {
                            list = new ArrayList(f.getCount());
                            while (f.moveToNext()) {
                                list.add(new VisaTokenDetails(f));
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        DbAdapter.m1106a(f);
                        throw th;
                    }
                }
                DbAdapter.m1106a(f);
            } catch (Throwable th3) {
                Throwable th4 = th3;
                f = null;
                th = th4;
                DbAdapter.m1106a(f);
                throw th;
            }
        }
        return list;
    }
}
