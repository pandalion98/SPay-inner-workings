package com.samsung.android.spayfw.p006d.p007a;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/* renamed from: com.samsung.android.spayfw.d.a.d */
public class SeDcDBHelper extends SeBaseDBHelper {
    private static SeDcDBHelper BD;

    public static synchronized SeDcDBHelper m684W(Context context) {
        SeDcDBHelper seDcDBHelper;
        synchronized (SeDcDBHelper.class) {
            Log.i("SeDcDBHelper", "getInstance");
            if (BD == null) {
                BD = new SeDcDBHelper(context);
            }
            seDcDBHelper = BD;
        }
        return seDcDBHelper;
    }

    public SeDcDBHelper(Context context) {
        super(context, "dc_provider.db", null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        Log.i("SeDcDBHelper", "Creating Database : dc_provider.db");
        try {
            Log.d("SeDcDBHelper", "Creating TABLE : CREATE TABLE CardMaster (_id INTEGER PRIMARY KEY AUTOINCREMENT,tokenId TEXT,status TEXT,dpanSuffix TEXT,fpanSuffix TEXT,tokenProvisionStatus INTEGER NOT NULL,sessionKeys BLOB,paginationTS INTEGER,remainingOtpkCount INTEGER,replenishmentThreshold INTEGER,number1 INTEGER,number2 INTEGER,data1 TEXT,data2 TEXT,data3 TEXT,blob1 BLOB,blob2 BLOB,blob3 BLOB )");
            sQLiteDatabase.execSQL("CREATE TABLE CardMaster (_id INTEGER PRIMARY KEY AUTOINCREMENT,tokenId TEXT,status TEXT,dpanSuffix TEXT,fpanSuffix TEXT,tokenProvisionStatus INTEGER NOT NULL,sessionKeys BLOB,paginationTS INTEGER,remainingOtpkCount INTEGER,replenishmentThreshold INTEGER,number1 INTEGER,number2 INTEGER,data1 TEXT,data2 TEXT,data3 TEXT,blob1 BLOB,blob2 BLOB,blob3 BLOB )");
            Log.d("SeDcDBHelper", "Creating TABLE : CREATE TABLE CardDetails (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,dataId INTEGER NOT NULL,data BLOB NOT NULL, FOREIGN KEY(cardMasterId) REFERENCES CardMaster(_id) ON DELETE CASCADE,UNIQUE (cardMasterId, dataId)  )");
            sQLiteDatabase.execSQL("CREATE TABLE CardDetails (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,dataId INTEGER NOT NULL,data BLOB NOT NULL, FOREIGN KEY(cardMasterId) REFERENCES CardMaster(_id) ON DELETE CASCADE,UNIQUE (cardMasterId, dataId)  )");
            Log.d("SeDcDBHelper", "Creating TABLE : CREATE TABLE PaymentProfiles (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,payProfileId INTEGER NOT NULL,ctq BLOB,auc BLOB,pru BLOB,aip BLOB,afl BLOB,cpr BLOB,arm BLOB,cvm BLOB,cl BLOB,data1 TEXT,data2 TEXT,blob1 BLOB,blob2 BLOB, FOREIGN KEY(cardMasterId) REFERENCES CardMaster(_id) ON DELETE CASCADE,UNIQUE (cardMasterId, payProfileId)  )");
            sQLiteDatabase.execSQL("CREATE TABLE PaymentProfiles (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,payProfileId INTEGER NOT NULL,ctq BLOB,auc BLOB,pru BLOB,aip BLOB,afl BLOB,cpr BLOB,arm BLOB,cvm BLOB,cl BLOB,data1 TEXT,data2 TEXT,blob1 BLOB,blob2 BLOB, FOREIGN KEY(cardMasterId) REFERENCES CardMaster(_id) ON DELETE CASCADE,UNIQUE (cardMasterId, payProfileId)  )");
            Log.d("SeDcDBHelper", "Creating TABLE : CREATE TABLE MetaData (_id INTEGER PRIMARY KEY AUTOINCREMENT,dataId INTEGER NOT NULL,data1 TEXT,data2 TEXT,data3 TEXT,num1 INTEGER,num2 INTEGER,num3 INTEGER,blob1 BLOB,blob2 BLOB,blob3 BLOB )");
            sQLiteDatabase.execSQL("CREATE TABLE MetaData (_id INTEGER PRIMARY KEY AUTOINCREMENT,dataId INTEGER NOT NULL,data1 TEXT,data2 TEXT,data3 TEXT,num1 INTEGER,num2 INTEGER,num3 INTEGER,blob1 BLOB,blob2 BLOB,blob3 BLOB )");
        } catch (SQLException e) {
            Log.e("SeDcDBHelper", "onCreate: SqlException: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Log.i("SeDcDBHelper", "onUpgrade");
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        super.onConfigure(sQLiteDatabase);
        try {
            sQLiteDatabase.setForeignKeyConstraintsEnabled(true);
        } catch (IllegalStateException e) {
            Log.e("SeDcDBHelper", "onConfigure: IllegalStateException: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e2) {
            Log.e("SeDcDBHelper", "onConfigure: Other Exception: " + e2.getMessage());
            e2.printStackTrace();
        }
    }
}
