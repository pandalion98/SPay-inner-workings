package com.samsung.android.visasdk.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.samsung.android.visasdk.facade.data.TokenKey;
import com.samsung.android.visasdk.facade.exception.InitializationException;
import com.samsung.android.visasdk.p025c.Log;
import com.samsung.android.visasdk.paywave.data.TVL;
import java.util.ArrayList;
import java.util.List;

/* renamed from: com.samsung.android.visasdk.storage.e */
public class DbTvlDao {
    private final DbAdapter Gu;

    public DbTvlDao(Context context) {
        if (context == null) {
            Log.m1301e("DbTvlDao", "ctx is null");
        }
        this.Gu = DbAdapter.as(context);
        if (this.Gu == null) {
            throw new InitializationException("cannot get db adapter");
        }
    }

    public synchronized void m1374a(TVL tvl) {
        if (tvl == null) {
            Log.m1301e("DbTvlDao", "tvl is null");
        } else if (m1375b(tvl)) {
            Log.m1301e("DbTvlDao", "tvl entry already exist tokenkey " + tvl.getTokenKey().getTokenId() + " atc: " + tvl.getAtc());
        } else {
            ContentValues contentValues = new ContentValues();
            try {
                contentValues.put("token_key", Long.valueOf(tvl.getTokenKey().getTokenId()));
                contentValues.put("atc", Integer.valueOf(tvl.getAtc()));
                contentValues.put("api", tvl.getApi());
                contentValues.put("timestamp", Long.valueOf(tvl.getTimeStamp()));
                contentValues.put("transaction_type", tvl.getTransactionType());
                contentValues.put("unpredictable_number", tvl.getUnpredictableNumber());
                this.Gu.m1360b("tbl_tvl", contentValues);
            } catch (Exception e) {
                e.printStackTrace();
                throw new SQLException("cannot store tvl");
            }
        }
    }

    public List<String> m1373a(TokenKey tokenKey, String str) {
        Throwable th;
        Cursor cursor = null;
        try {
            String[] strArr = new String[]{"timestamp", "unpredictable_number", "atc", "transaction_type"};
            String[] strArr2 = new String[]{Long.toString(tokenKey.getTokenId()), str};
            Cursor a = this.Gu.m1358a("tbl_tvl", strArr, "token_key = ? AND api = ? ", strArr2, "atc ASC");
            if (a != null) {
                try {
                    if (a.getCount() > 0) {
                        List<String> arrayList = new ArrayList(a.getCount());
                        int columnIndex = a.getColumnIndex("timestamp");
                        int columnIndex2 = a.getColumnIndex("unpredictable_number");
                        int columnIndex3 = a.getColumnIndex("atc");
                        int columnIndex4 = a.getColumnIndex("transaction_type");
                        while (a.moveToNext()) {
                            String string = a.getString(columnIndex2);
                            String string2 = a.getString(columnIndex4);
                            if (string == null || "S".equals(string2) || "I".equals(string2) || "M".equals(string2)) {
                                string = BuildConfig.FLAVOR;
                            }
                            arrayList.add(a.getLong(columnIndex) + "|" + string + "|" + a.getInt(columnIndex3) + "|" + string2);
                        }
                        DbAdapter.m1356a(a);
                        return arrayList;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor = a;
                    DbAdapter.m1356a(cursor);
                    throw th;
                }
            }
            DbAdapter.m1356a(a);
            return null;
        } catch (Throwable th3) {
            th = th3;
            DbAdapter.m1356a(cursor);
            throw th;
        }
    }

    public boolean m1375b(TVL tvl) {
        Throwable th;
        Cursor a;
        try {
            String[] strArr = new String[]{"timestamp", "unpredictable_number", "atc", "transaction_type"};
            String[] strArr2 = new String[]{Long.toString(tvl.getTokenKey().getTokenId()), Integer.toString(tvl.getAtc())};
            a = this.Gu.m1358a("tbl_tvl", strArr, "token_key = ? AND atc = ? ", strArr2, "atc DESC");
            if (a != null) {
                try {
                    if (a.getCount() > 0) {
                        DbAdapter.m1356a(a);
                        return true;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    DbAdapter.m1356a(a);
                    throw th;
                }
            }
            DbAdapter.m1356a(a);
            return false;
        } catch (Throwable th3) {
            th = th3;
            a = null;
            DbAdapter.m1356a(a);
            throw th;
        }
    }
}
