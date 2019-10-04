/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteDatabase
 *  java.lang.Exception
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 */
package com.samsung.android.visasdk.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.samsung.android.visasdk.c.a;
import com.samsung.android.visasdk.paywave.model.QVSDCData;
import com.samsung.android.visasdk.paywave.model.QVSDCWithODA;
import com.samsung.android.visasdk.paywave.model.StaticParams;
import com.samsung.android.visasdk.storage.c;

public class d
implements com.samsung.android.spayfw.interfacelibrary.db.a {
    private static String Gv = "DELETE FROM tbl_tvl WHERE _id NOT IN (SELECT MIN(_id) FROM tbl_tvl GROUP BY token_key,atc)";
    private Context mContext;

    public d(Context context) {
        this.mContext = context;
    }

    /*
     * Exception decompiling
     */
    public static void a(SQLiteDatabase var0) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.ConfusedCFRException: Tried to end blocks [5[DOLOOP]], but top level block is 9[UNCONDITIONALDOLOOP]
        // org.benf.cfr.reader.b.a.a.j.a(Op04StructuredStatement.java:432)
        // org.benf.cfr.reader.b.a.a.j.d(Op04StructuredStatement.java:484)
        // org.benf.cfr.reader.b.a.a.i.a(Op03SimpleStatement.java:607)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:692)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:182)
        // org.benf.cfr.reader.b.f.a(CodeAnalyser.java:127)
        // org.benf.cfr.reader.entities.attributes.f.c(AttributeCode.java:96)
        // org.benf.cfr.reader.entities.g.p(Method.java:396)
        // org.benf.cfr.reader.entities.d.e(ClassFile.java:890)
        // org.benf.cfr.reader.entities.d.b(ClassFile.java:792)
        // org.benf.cfr.reader.b.a(Driver.java:128)
        // org.benf.cfr.reader.a.a(CfrDriverImpl.java:63)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.decompileWithCFR(JavaExtractionWorker.kt:61)
        // com.njlabs.showjava.decompilers.JavaExtractionWorker.doWork(JavaExtractionWorker.kt:130)
        // com.njlabs.showjava.decompilers.BaseDecompiler.withAttempt(BaseDecompiler.kt:108)
        // com.njlabs.showjava.workers.DecompilerWorker$b.run(DecompilerWorker.kt:118)
        // java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1167)
        // java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:641)
        // java.lang.Thread.run(Thread.java:764)
        throw new IllegalStateException("Decompilation failed");
    }

    public static void b(SQLiteDatabase sQLiteDatabase) {
        d.c(sQLiteDatabase);
        d.d(sQLiteDatabase);
    }

    private static void c(SQLiteDatabase sQLiteDatabase) {
        a.d("DbOpenHelper", "removeDuplicateTVL");
        sQLiteDatabase.execSQL(Gv);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private static void d(SQLiteDatabase sQLiteDatabase) {
        Cursor cursor;
        block7 : {
            cursor = null;
            a.d("DbOpenHelper", "ODA data exp date format migration");
            cursor = sQLiteDatabase.rawQuery("SELECT * FROM tbl_enhanced_token_info", null);
            if (cursor != null && cursor.getCount() > 0) break block7;
            a.i("DbOpenHelper", "No cards to be modified");
            if (cursor == null || cursor.isClosed()) return;
            {
                cursor.close();
                return;
            }
        }
        try {
            while (cursor.moveToNext()) {
                String string = cursor.getString(cursor.getColumnIndex("static_params"));
                String string2 = cursor.getString(cursor.getColumnIndex("_id"));
                a.d("DbOpenHelper", "static param " + string2 + " with data: " + string);
                StaticParams staticParams = StaticParams.fromJson(string);
                if (staticParams == null || staticParams.getQVSDCData() == null || staticParams.getQVSDCData().getqVSDCWithODA() == null || staticParams.getQVSDCData().getqVSDCWithODA().getAppExpDate() == null) continue;
                String string3 = cursor.getString(cursor.getColumnIndex("token_expirationDate_month"));
                String string4 = com.samsung.android.visasdk.d.a.A(cursor.getString(cursor.getColumnIndex("token_expirationDate_year")), string3);
                if (string4 == null || staticParams.getQVSDCData().getqVSDCWithODA().getAppExpDate().equals((Object)string4)) continue;
                a.w("DbOpenHelper", "App expiration date not the same for " + string2);
                a.d("DbOpenHelper", "App expiration date before " + staticParams.getQVSDCData().getqVSDCWithODA().getAppExpDate());
                a.d("DbOpenHelper", "App expiration date after: " + string4);
                staticParams.getQVSDCData().getqVSDCWithODA().setAppExpDate(string4);
                String string5 = cursor.getString(cursor.getColumnIndex("vProvisionedTokenID"));
                a.w("DbOpenHelper", "Found Cards need to be modified for " + string5);
                String string6 = staticParams.toJson();
                a.d("DbOpenHelper", "static param " + string6);
                ContentValues contentValues = new ContentValues();
                contentValues.put("static_params", string6);
                int n2 = sQLiteDatabase.update("tbl_enhanced_token_info", contentValues, "_id = ?", new String[]{string2});
                a.i("DbOpenHelper", "update return value " + n2 + " for " + string2);
            }
        }
        catch (Throwable throwable) {
            if (cursor == null || cursor.isClosed()) throw throwable;
            {
                cursor.close();
            }
            throw throwable;
        }
        if (cursor == null || cursor.isClosed()) return;
        {
            cursor.close();
            return;
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        if (!sQLiteDatabase.isReadOnly()) {
            sQLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
            a.i("DbOpenHelper", "FOREIGN KEY constraint enabled");
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            if (sQLiteDatabase.isOpen()) {
                sQLiteDatabase.execSQL("CREATE TABLE tbl_enhanced_token_info (_id INTEGER PRIMARY KEY AUTOINCREMENT, vPanEnrollmentID TEXT, vProvisionedTokenID TEXT, token_requester_id TEXT, encryption_metadata TEXT, tokenStatus TEXT, payment_instrument_last4 TEXT, payment_instrument_expiration_month TEXT, payment_instrument_expiration_year TEXT, token_expirationDate_month TEXT, token_expirationDate_year TEXT, appPrgrmID TEXT, static_params TEXT, dynamic_key BLOB, mac_left_key BLOB, mac_right_key BLOB, enc_token_info BLOB, dynamic_dki TEXT, token_last4 TEXT, mst_cvv TEXT, mst_svc_code TEXT, nic INTEGER, locate_sad_offset INTEGER, sdad_sfi INTEGER, sdad_rec INTEGER, sdad_offset INTEGER, sdad_length INTEGER, car_data_offset INTEGER, decimalized_crypto_data BLOB, bouncy_submarine BLOB, sugar BLOB, UNIQUE (vProvisionedTokenID) ON CONFLICT FAIL)");
                sQLiteDatabase.execSQL("CREATE TABLE tbl_data_group(_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, token_key INTEGER, dgi TEXT NOT NULL, dgi_data BLOB NOT NULL, FOREIGN KEY(token_key) REFERENCES tbl_enhanced_token_info (_id) ON DELETE CASCADE, UNIQUE (token_key,dgi) ON CONFLICT REPLACE)");
                sQLiteDatabase.execSQL("CREATE TABLE tbl_settings (_id INTEGER PRIMARY KEY, access_token_adv_warning_percent INTEGER, access_token_adv_warning_time INTEGER, check_status_period INTEGER, select_card INTEGER, cvm_entity TEXT, cvm_type TEXT, cvm_verified INTEGER, end_point TEXT, environment TEXT, thm_enabled INTEGER, bouncy_airplane BLOB, max_tvl INTEGER DEFAULT 100)");
                sQLiteDatabase.execSQL("CREATE TABLE tbl_tvl (_id INTEGER PRIMARY KEY AUTOINCREMENT, token_key INTEGER, timestamp NUMERIC, unpredictable_number TEXT, atc INTEGER, transaction_type TEXT, api TEXT, cryptogram TEXT, FOREIGN KEY(token_key) REFERENCES tbl_enhanced_token_info (_id) ON DELETE CASCADE )");
                a.i("DbOpenHelper", "Database is created");
            }
        }
        catch (Exception exception) {
            a.e("DbOpenHelper", "paywave database is not fully created, any schema error?");
            exception.printStackTrace();
        }
        c.a(this.mContext, sQLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
        a.d("DbOpenHelper", "paywave database downgrade");
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
        a.i("DbOpenHelper", "onUpgrade database from version " + n2 + " to version " + n3);
        if (n2 == n3) {
            a.i("DbOpenHelper", "oldVersion == newVersion, no need to upgrade!");
            return;
        } else {
            if (n2 > n3) {
                a.e("DbOpenHelper", "database cannot be downgraded, please update to the latest version");
                this.onDowngrade(sQLiteDatabase, n2, n3);
                return;
            }
            if (n2 < 2) {
                a.i("DbOpenHelper", "migrate DB to ver 2");
                d.a(sQLiteDatabase);
            }
            if (n2 >= 3) return;
            {
                a.i("DbOpenHelper", "migrate DB to ver 3");
                d.b(sQLiteDatabase);
                return;
            }
        }
    }
}

