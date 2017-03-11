package com.samsung.android.spayfw.fraud.p011a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.fraud.p011a.FTokenRecord.FTokenRecord;
import com.samsung.android.spayfw.interfacelibrary.db.DBName;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.p008e.p009a.DBHelperWrapper;
import com.samsung.android.spayfw.utils.DBUtils;
import com.samsung.android.spayfw.utils.Utils;

/* renamed from: com.samsung.android.spayfw.fraud.a.g */
public class FraudDbAdapter {
    private static DBHelperWrapper ns;
    private static FraudDbAdapter oa;
    private SQLiteDatabase ob;

    private FraudDbAdapter(Context context) {
        ns = DBHelperWrapper.m689b(context, null, 0, DBName.collector_enc);
        if (ns == null) {
            throw new Exception("dbHelper is null");
        }
        this.ob = ns.getWritableDatabase(DBUtils.getDbPassword());
        if (this.ob == null) {
            throw new Exception("db is null");
        }
    }

    public static FraudDbAdapter m709z(Context context) {
        try {
            if (oa == null) {
                oa = new FraudDbAdapter(context);
            }
        } catch (Exception e) {
            Log.m286e("FraudDbAdapter", "FraudDbAdapter: cannot get db adapter");
            oa = null;
        }
        return oa;
    }

    public long m712a(FTokenRecord fTokenRecord) {
        try {
            long insert = this.ob.insert(fTokenRecord.bB(), null, fTokenRecord.bC());
            Log.m285d("FraudDbAdapter", "addTransactionDetail: rowId = " + insert);
            return insert;
        } catch (Throwable e) {
            Log.m284c("FraudDbAdapter", e.getMessage(), e);
            return -1;
        }
    }

    public Cursor rawQuery(String str, String[] strArr) {
        try {
            return this.ob.rawQuery(str, strArr);
        } catch (Throwable e) {
            Utils.m1274a(new RuntimeException(e));
            return null;
        }
    }

    public long m711a(FBaseRecord fBaseRecord) {
        long j = -1;
        if (fBaseRecord != null) {
            try {
                j = this.ob.replaceOrThrow(fBaseRecord.bB(), null, fBaseRecord.bC());
            } catch (Throwable e) {
                Log.m284c("FraudDbAdapter", e.getMessage(), e);
            }
        }
        return j;
    }

    public Cursor m713a(String str, String str2, String[] strArr) {
        if (str == null) {
            return null;
        }
        try {
            return this.ob.query(str, null, str2, strArr, null, null, null);
        } catch (Throwable e) {
            Log.m284c("FraudDbAdapter", e.getMessage(), e);
            return null;
        }
    }

    public int m710a(String str, ContentValues contentValues, String str2, String[] strArr) {
        int i = -1;
        if (!(str == null || contentValues == null)) {
            try {
                i = this.ob.update(str, contentValues, str2, strArr);
            } catch (Throwable e) {
                Log.m284c("FraudDbAdapter", e.getMessage(), e);
            }
        }
        return i;
    }
}
