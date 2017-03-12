package com.samsung.android.visasdk.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.spayfw.interfacelibrary.db.DBHelperCallback;
import com.samsung.android.visasdk.p025c.Log;
import com.samsung.android.visasdk.p026d.TokenProcessor;
import com.samsung.android.visasdk.paywave.model.AidInfo;
import com.samsung.android.visasdk.paywave.model.StaticParams;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/* renamed from: com.samsung.android.visasdk.storage.d */
public class DbOpenHelper implements DBHelperCallback {
    private static String Gv;
    private Context mContext;

    public DbOpenHelper(Context context) {
        this.mContext = context;
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        if (!sQLiteDatabase.isReadOnly()) {
            sQLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
            Log.m1302i("DbOpenHelper", "FOREIGN KEY constraint enabled");
        }
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            if (sQLiteDatabase.isOpen()) {
                sQLiteDatabase.execSQL("CREATE TABLE tbl_enhanced_token_info (_id INTEGER PRIMARY KEY AUTOINCREMENT, vPanEnrollmentID TEXT, vProvisionedTokenID TEXT, token_requester_id TEXT, encryption_metadata TEXT, tokenStatus TEXT, payment_instrument_last4 TEXT, payment_instrument_expiration_month TEXT, payment_instrument_expiration_year TEXT, token_expirationDate_month TEXT, token_expirationDate_year TEXT, appPrgrmID TEXT, static_params TEXT, dynamic_key BLOB, mac_left_key BLOB, mac_right_key BLOB, enc_token_info BLOB, dynamic_dki TEXT, token_last4 TEXT, mst_cvv TEXT, mst_svc_code TEXT, nic INTEGER, locate_sad_offset INTEGER, sdad_sfi INTEGER, sdad_rec INTEGER, sdad_offset INTEGER, sdad_length INTEGER, car_data_offset INTEGER, decimalized_crypto_data BLOB, bouncy_submarine BLOB, sugar BLOB, UNIQUE (vProvisionedTokenID) ON CONFLICT FAIL)");
                sQLiteDatabase.execSQL("CREATE TABLE tbl_data_group(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, token_key INTEGER, dgi TEXT NOT NULL, dgi_data BLOB NOT NULL, FOREIGN KEY(token_key) REFERENCES tbl_enhanced_token_info (_id) ON DELETE CASCADE, UNIQUE (token_key,dgi) ON CONFLICT REPLACE)");
                sQLiteDatabase.execSQL("CREATE TABLE tbl_settings (_id INTEGER PRIMARY KEY, access_token_adv_warning_percent INTEGER, access_token_adv_warning_time INTEGER, check_status_period INTEGER, select_card INTEGER, cvm_entity TEXT, cvm_type TEXT, cvm_verified INTEGER, end_point TEXT, environment TEXT, thm_enabled INTEGER, bouncy_airplane BLOB, max_tvl INTEGER DEFAULT 100)");
                sQLiteDatabase.execSQL("CREATE TABLE tbl_tvl (_id INTEGER PRIMARY KEY AUTOINCREMENT, token_key INTEGER, timestamp NUMERIC, unpredictable_number TEXT, atc INTEGER, transaction_type TEXT, api TEXT, cryptogram TEXT, FOREIGN KEY(token_key) REFERENCES tbl_enhanced_token_info (_id) ON DELETE CASCADE )");
                Log.m1302i("DbOpenHelper", "Database is created");
            }
        } catch (Exception e) {
            Log.m1301e("DbOpenHelper", "paywave database is not fully created, any schema error?");
            e.printStackTrace();
        }
        DbMigrationHelper.m1371a(this.mContext, sQLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Log.m1300d("DbOpenHelper", "paywave database downgrade");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Log.m1302i("DbOpenHelper", "onUpgrade database from version " + i + " to version " + i2);
        if (i == i2) {
            Log.m1302i("DbOpenHelper", "oldVersion == newVersion, no need to upgrade!");
        } else if (i > i2) {
            Log.m1301e("DbOpenHelper", "database cannot be downgraded, please update to the latest version");
            onDowngrade(sQLiteDatabase, i, i2);
        } else {
            if (i < 2) {
                Log.m1302i("DbOpenHelper", "migrate DB to ver 2");
                DbOpenHelper.m1352a(sQLiteDatabase);
            }
            if (i < 3) {
                Log.m1302i("DbOpenHelper", "migrate DB to ver 3");
                DbOpenHelper.m1353b(sQLiteDatabase);
            }
        }
    }

    public static void m1352a(SQLiteDatabase sQLiteDatabase) {
        Throwable th;
        Cursor cursor = null;
        Map hashMap = new HashMap();
        Log.m1300d("DbOpenHelper", "CAP value migration");
        try {
            Cursor rawQuery = sQLiteDatabase.rawQuery("SELECT * FROM tbl_enhanced_token_info", null);
            if (rawQuery != null) {
                try {
                    if (rawQuery.getCount() > 0) {
                        String string;
                        String toJson;
                        while (rawQuery.moveToNext()) {
                            StaticParams fromJson = StaticParams.fromJson(rawQuery.getString(rawQuery.getColumnIndex("static_params")));
                            if (!(fromJson == null || fromJson.getQVSDCData() == null || fromJson.getQVSDCData().getCountryCode() == null || !fromJson.getQVSDCData().getCountryCode().equals("0840"))) {
                                int i;
                                List<AidInfo> aidInfo = fromJson.getAidInfo();
                                if (aidInfo != null) {
                                    i = 0;
                                    for (AidInfo aidInfo2 : aidInfo) {
                                        int i2;
                                        if (aidInfo2.getAid().equals("A0000000031010") && aidInfo2.getCap().equals("0000D800")) {
                                            Log.m1300d("DbOpenHelper", "CAP modified for aid " + aidInfo2.getAid());
                                            aidInfo2.setCap("00001800");
                                            i2 = 1;
                                        } else {
                                            i2 = i;
                                        }
                                        i = i2;
                                    }
                                } else {
                                    i = 0;
                                }
                                if (i != 0) {
                                    string = rawQuery.getString(rawQuery.getColumnIndex("_id"));
                                    Log.m1303w("DbOpenHelper", "Found Cards need to be modified for " + rawQuery.getString(rawQuery.getColumnIndex("vProvisionedTokenID")));
                                    Log.m1300d("DbOpenHelper", "Found Cards need to be modified for id " + string);
                                    toJson = fromJson.toJson();
                                    Log.m1300d("DbOpenHelper", "static param " + toJson);
                                    hashMap.put(string, toJson);
                                } else {
                                    continue;
                                }
                            }
                        }
                        if (!(rawQuery == null || rawQuery.isClosed())) {
                            rawQuery.close();
                        }
                        if (hashMap != null && !hashMap.isEmpty()) {
                            for (Entry entry : hashMap.entrySet()) {
                                toJson = (String) entry.getKey();
                                string = (String) entry.getValue();
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("static_params", string);
                                String str = "tbl_enhanced_token_info";
                                Log.m1302i("DbOpenHelper", "update return value " + sQLiteDatabase.update(str, contentValues, "_id = ?", new String[]{toJson}) + " for " + toJson);
                            }
                            return;
                        }
                        return;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    cursor = rawQuery;
                }
            }
            Log.m1302i("DbOpenHelper", "No cards to be modified");
            if (rawQuery != null && !rawQuery.isClosed()) {
                rawQuery.close();
            }
        } catch (Throwable th3) {
            th = th3;
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
            throw th;
        }
    }

    public static void m1353b(SQLiteDatabase sQLiteDatabase) {
        DbOpenHelper.m1354c(sQLiteDatabase);
        DbOpenHelper.m1355d(sQLiteDatabase);
    }

    static {
        Gv = "DELETE FROM tbl_tvl WHERE _id NOT IN (SELECT MIN(_id) FROM tbl_tvl GROUP BY token_key,atc)";
    }

    private static void m1354c(SQLiteDatabase sQLiteDatabase) {
        Log.m1300d("DbOpenHelper", "removeDuplicateTVL");
        sQLiteDatabase.execSQL(Gv);
    }

    private static void m1355d(SQLiteDatabase sQLiteDatabase) {
        Cursor cursor = null;
        Log.m1300d("DbOpenHelper", "ODA data exp date format migration");
        try {
            cursor = sQLiteDatabase.rawQuery("SELECT * FROM tbl_enhanced_token_info", null);
            if (cursor == null || cursor.getCount() <= 0) {
                Log.m1302i("DbOpenHelper", "No cards to be modified");
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                    return;
                }
                return;
            }
            while (cursor.moveToNext()) {
                String string = cursor.getString(cursor.getColumnIndex("static_params"));
                String string2 = cursor.getString(cursor.getColumnIndex("_id"));
                Log.m1300d("DbOpenHelper", "static param " + string2 + " with data: " + string);
                StaticParams fromJson = StaticParams.fromJson(string);
                if (!(fromJson == null || fromJson.getQVSDCData() == null || fromJson.getQVSDCData().getqVSDCWithODA() == null || fromJson.getQVSDCData().getqVSDCWithODA().getAppExpDate() == null)) {
                    String A = TokenProcessor.m1304A(cursor.getString(cursor.getColumnIndex("token_expirationDate_year")), cursor.getString(cursor.getColumnIndex("token_expirationDate_month")));
                    if (!(A == null || fromJson.getQVSDCData().getqVSDCWithODA().getAppExpDate().equals(A))) {
                        Log.m1303w("DbOpenHelper", "App expiration date not the same for " + string2);
                        Log.m1300d("DbOpenHelper", "App expiration date before " + fromJson.getQVSDCData().getqVSDCWithODA().getAppExpDate());
                        Log.m1300d("DbOpenHelper", "App expiration date after: " + A);
                        fromJson.getQVSDCData().getqVSDCWithODA().setAppExpDate(A);
                        Log.m1303w("DbOpenHelper", "Found Cards need to be modified for " + cursor.getString(cursor.getColumnIndex("vProvisionedTokenID")));
                        string = fromJson.toJson();
                        Log.m1300d("DbOpenHelper", "static param " + string);
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("static_params", string);
                        String str = "tbl_enhanced_token_info";
                        Log.m1302i("DbOpenHelper", "update return value " + sQLiteDatabase.update(str, contentValues, "_id = ?", new String[]{string2}) + " for " + string2);
                    }
                }
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (!(cursor == null || cursor.isClosed())) {
                cursor.close();
            }
        }
    }
}
