package com.samsung.android.spayfw.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.americanexpress.mobilepayments.hceclient.context.MetaDataManager;
import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.storage.DbAdapter.ColumnType;
import com.samsung.android.spayfw.storage.models.TokenRecord;
import java.util.ArrayList;
import java.util.List;

public class TokenRecordStorage extends DbAdapter {
    private static TokenRecordStorage Cl;

    public static class TokenGroup {

        public enum TokenColumn {
            ID("ID", 0, "_id", ColumnType.INTEGER),
            ENROLLMENT_ID("ENROLLMENT_ID", 1, "enrollment_id", ColumnType.TEXT),
            TR_TOKEN_ID("TR_TOKEN_ID", 2, "tr_token_id", ColumnType.TEXT),
            USER_ID("USER_ID", 3, "user_id", ColumnType.TEXT),
            APP_ID("APP_ID", 4, "app_id", ColumnType.TEXT),
            TOKEN_REF_ID(HCEClientConstants.TOKEN_REF_ID, 5, "provider_token_key", ColumnType.TEXT),
            TOKEN_STATUS(MetaDataManager.TOKEN_STATUS, 6, "token_status", ColumnType.TEXT),
            TOKEN_STATUS_REASON("TOKEN_STATUS_REASON", 7, "token_status_reason", ColumnType.TEXT),
            CARD_BRAND("CARD_BRAND", 8, "card_brand", ColumnType.TEXT),
            CARD_TYPE("CARD_TYPE", 9, "card_type", ColumnType.TEXT),
            CARD_PRODUCT("CARD_PRODUCT", 10, "card_product", ColumnType.TEXT),
            CARD_PRESENT_MODE("CARD_PRESENT_MODE", 11, "card_present_mode", ColumnType.INTEGER),
            TNC_ACCEPTANCE_TIME("TNC_ACCEPTANCE_TIME", 12, "tnc_acceptance_time", ColumnType.INTEGER),
            TRANSACTION_COUNT("TRANSACTION_COUNT", 13, "transaction_count", ColumnType.INTEGER),
            TRANSACTION_URL("TRANSACTION_URL", 14, "transaction_url", ColumnType.TEXT),
            TRANSACTION_ID_LIST("TRANSACTION_ID_LIST", 15, "data_1", ColumnType.TEXT),
            TRANSACTION_RETRY_ALLOWED("TRANSACTION_RETRY_ALLOWED", 16, "data_2", ColumnType.INTEGER),
            CASH_CARD_ID("CASH_CARD_ID", 17, "data_3", ColumnType.TEXT),
            USER_SIGNATURE_DATA("USER_SIGNATURE_DATA", 21, "data_7", ColumnType.BLOB);
            
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

            protected ColumnType getColumnTypeFormat() {
                return this.columnType;
            }
        }
    }

    public static final synchronized TokenRecordStorage ae(Context context) {
        TokenRecordStorage tokenRecordStorage;
        synchronized (TokenRecordStorage.class) {
            if (Cl == null) {
                Cl = new TokenRecordStorage(context);
            }
            tokenRecordStorage = Cl;
        }
        return tokenRecordStorage;
    }

    private static TokenRecord m1222i(Cursor cursor) {
        boolean z = true;
        TokenRecord tokenRecord = new TokenRecord(cursor.getString(TokenColumn.ENROLLMENT_ID.getColumnIndex()));
        tokenRecord.setTrTokenId(cursor.getString(TokenColumn.TR_TOKEN_ID.getColumnIndex()));
        tokenRecord.setUserId(cursor.getString(TokenColumn.USER_ID.getColumnIndex()));
        tokenRecord.bt(cursor.getString(TokenColumn.APP_ID.getColumnIndex()));
        tokenRecord.setTokenRefId(cursor.getString(TokenColumn.TOKEN_REF_ID.getColumnIndex()));
        tokenRecord.setTokenStatus(cursor.getString(TokenColumn.TOKEN_STATUS.getColumnIndex()));
        tokenRecord.m1251H(cursor.getString(TokenColumn.TOKEN_STATUS_REASON.getColumnIndex()));
        tokenRecord.setCardBrand(cursor.getString(TokenColumn.CARD_BRAND.getColumnIndex()));
        tokenRecord.setCardType(cursor.getString(TokenColumn.CARD_TYPE.getColumnIndex()));
        tokenRecord.bu(cursor.getString(TokenColumn.CARD_PRODUCT.getColumnIndex()));
        tokenRecord.m1255j(cursor.getInt(TokenColumn.CARD_PRESENT_MODE.getColumnIndex()));
        tokenRecord.m1252b(cursor.getLong(TokenColumn.TNC_ACCEPTANCE_TIME.getColumnIndex()));
        tokenRecord.m1250B(cursor.getLong(TokenColumn.TRANSACTION_COUNT.getColumnIndex()));
        tokenRecord.bv(cursor.getString(TokenColumn.TRANSACTION_URL.getColumnIndex()));
        tokenRecord.bw(cursor.getString(TokenColumn.TRANSACTION_ID_LIST.getColumnIndex()));
        if (cursor.getInt(TokenColumn.TRANSACTION_RETRY_ALLOWED.getColumnIndex()) != 1) {
            z = false;
        }
        tokenRecord.m1254i(z);
        tokenRecord.bs(cursor.getString(TokenColumn.CASH_CARD_ID.getColumnIndex()));
        tokenRecord.m1256m(cursor.getBlob(TokenColumn.USER_SIGNATURE_DATA.getColumnIndex()));
        return tokenRecord;
    }

    private static ContentValues m1221b(TokenRecord tokenRecord) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TokenColumn.TR_TOKEN_ID.getColumn(), tokenRecord.getTrTokenId());
        contentValues.put(TokenColumn.USER_ID.getColumn(), tokenRecord.getUserId());
        contentValues.put(TokenColumn.APP_ID.getColumn(), tokenRecord.getAppId());
        contentValues.put(TokenColumn.TOKEN_REF_ID.getColumn(), tokenRecord.getTokenRefId());
        contentValues.put(TokenColumn.TOKEN_STATUS.getColumn(), tokenRecord.getTokenStatus());
        contentValues.put(TokenColumn.TOKEN_STATUS_REASON.getColumn(), tokenRecord.fy());
        contentValues.put(TokenColumn.CARD_BRAND.getColumn(), tokenRecord.getCardBrand());
        contentValues.put(TokenColumn.CARD_TYPE.getColumn(), tokenRecord.getCardType());
        contentValues.put(TokenColumn.CARD_PRODUCT.getColumn(), tokenRecord.fz());
        contentValues.put(TokenColumn.CARD_PRESENT_MODE.getColumn(), Integer.valueOf(tokenRecord.ab()));
        contentValues.put(TokenColumn.TNC_ACCEPTANCE_TIME.getColumn(), Long.valueOf(tokenRecord.fA()));
        contentValues.put(TokenColumn.TRANSACTION_COUNT.getColumn(), Long.valueOf(tokenRecord.fB()));
        contentValues.put(TokenColumn.TRANSACTION_URL.getColumn(), tokenRecord.getTransactionUrl());
        contentValues.put(TokenColumn.TRANSACTION_ID_LIST.getColumn(), tokenRecord.fD());
        contentValues.put(TokenColumn.TRANSACTION_RETRY_ALLOWED.getColumn(), Integer.valueOf(tokenRecord.fw() ? 1 : 0));
        contentValues.put(TokenColumn.CASH_CARD_ID.getColumn(), tokenRecord.fx());
        contentValues.put(TokenColumn.USER_SIGNATURE_DATA.getColumn(), tokenRecord.fE());
        return contentValues;
    }

    private TokenRecordStorage(Context context) {
        super(context);
        execSQL("CREATE TABLE IF NOT EXISTS token_group ( _id INTEGER PRIMARY KEY AUTOINCREMENT, enrollment_id TEXT NOT NULL UNIQUE, tr_token_id TEXT UNIQUE, user_id TEXT, app_id TEXT, provider_token_key TEXT, token_status TEXT, token_status_reason TEXT, card_brand TEXT, card_type TEXT, card_product TEXT, card_present_mode INTEGER, tnc_acceptance_time INTEGER, transaction_count INTEGER, transaction_url TEXT, data_1 TEXT, data_2 INTEGER, data_3 TEXT,  data_4 TEXT, data_5 TEXT, data_6 TEXT, data_7 BLOB, data_8 BLOB )");
        Log.m287i("TokenRecordStorage", "Create TokenGroup Table If Not Exists");
    }

    public int m1224a(TokenColumn tokenColumn, String str) {
        return m1112d("token_group", tokenColumn == null ? null : tokenColumn.getColumn(), str);
    }

    public int fu() {
        return m1224a(null, null);
    }

    public List<String> fv() {
        return m1225a(TokenColumn.ENROLLMENT_ID, null, null);
    }

    public List<String> m1225a(TokenColumn tokenColumn, TokenColumn tokenColumn2, String str) {
        String str2 = null;
        if (tokenColumn == null) {
            return null;
        }
        if (tokenColumn2 != null) {
            str2 = tokenColumn2.getColumn();
        }
        return m1111a("token_group", tokenColumn.getColumn(), str2, str);
    }

    public String m1226b(TokenColumn tokenColumn, String str) {
        if (str == null) {
            return null;
        }
        List a = m1225a(tokenColumn, TokenColumn.TR_TOKEN_ID, str);
        if (a != null) {
            return (String) a.get(0);
        }
        Log.m286e("TokenRecordStorage", "getColomn: list is null");
        return null;
    }

    public int m1223a(TokenColumn tokenColumn, Object obj, TokenColumn tokenColumn2, String str) {
        if (tokenColumn == null || tokenColumn2 == null) {
            return -1;
        }
        return m1109a("token_group", tokenColumn.getColumn(), obj, tokenColumn.getColumnTypeFormat(), tokenColumn2.getColumn(), str);
    }

    public List<TokenRecord> m1228c(TokenColumn tokenColumn, String str) {
        Cursor f;
        Throwable th;
        List<TokenRecord> list = null;
        if (tokenColumn != null) {
            try {
                f = m1114f("token_group", tokenColumn.getColumn(), str);
                if (f != null) {
                    try {
                        if (f.getCount() > 0) {
                            list = new ArrayList(f.getCount());
                            while (f.moveToNext()) {
                                list.add(m1222i(f));
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

    public TokenRecord bp(String str) {
        if (str == null) {
            return null;
        }
        List c = m1228c(TokenColumn.ENROLLMENT_ID, str);
        if (c != null) {
            return (TokenRecord) c.get(0);
        }
        return null;
    }

    public TokenRecord bq(String str) {
        List c = m1228c(TokenColumn.TR_TOKEN_ID, str);
        if (c != null) {
            return (TokenRecord) c.get(0);
        }
        Log.m286e("TokenRecordStorage", "getTokenRecordByTrTokenId: cannot find token by tr token id");
        return null;
    }

    public int m1227c(TokenRecord tokenRecord) {
        if (!(tokenRecord == null || tokenRecord.getEnrollmentId() == null)) {
            ContentValues b = m1221b(tokenRecord);
            b.put(TokenColumn.ENROLLMENT_ID.getColumn(), tokenRecord.getEnrollmentId());
            try {
                m1107a("token_group", b);
            } catch (Throwable e) {
                Log.m286e("TokenRecordStorage", "addTokenRecord: cannot add a token");
                Log.m284c("TokenRecordStorage", e.getMessage(), e);
            }
        }
        return -1;
    }

    public int m1230d(TokenRecord tokenRecord) {
        if (tokenRecord == null) {
            return -1;
        }
        return m1108a("token_group", m1221b(tokenRecord), TokenColumn.ENROLLMENT_ID.getColumn(), tokenRecord.getEnrollmentId());
    }

    public int m1229d(TokenColumn tokenColumn, String str) {
        if (tokenColumn == null) {
            return -1;
        }
        int e = m1113e("token_group", tokenColumn.getColumn(), str);
        Log.m287i("TokenRecordStorage", "deleteTokenRecord" + e);
        return e;
    }

    public int m1231d(String str, byte[] bArr) {
        if (bArr == null || TextUtils.isEmpty(str)) {
            return -1;
        }
        return m1223a(TokenColumn.USER_SIGNATURE_DATA, bArr, TokenColumn.TR_TOKEN_ID, str);
    }

    public byte[] br(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        byte[] fE;
        List c = m1228c(TokenColumn.TR_TOKEN_ID, str);
        if (c != null && c.size() > 0) {
            TokenRecord tokenRecord = (TokenRecord) c.get(0);
            if (tokenRecord != null) {
                fE = tokenRecord.fE();
                return fE;
            }
        }
        fE = null;
        return fE;
    }
}
