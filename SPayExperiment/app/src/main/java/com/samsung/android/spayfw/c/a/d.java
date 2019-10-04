/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.database.SQLException
 *  android.database.sqlite.SQLiteDatabase
 *  android.database.sqlite.SQLiteDatabase$CursorFactory
 *  android.util.Log
 *  java.lang.Class
 *  java.lang.Exception
 *  java.lang.IllegalStateException
 *  java.lang.String
 */
package com.samsung.android.spayfw.c.a;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.samsung.android.spayfw.c.a.a;

public class d
extends a {
    private static d Bt;

    public d(Context context) {
        super(context, "dc_provider.db", null, 1);
    }

    public static d S(Context context) {
        Class<d> class_ = d.class;
        synchronized (d.class) {
            Log.i((String)"SdlDcDBHelper", (String)"getInstance");
            if (Bt == null) {
                Bt = new d(context);
            }
            d d2 = Bt;
            // ** MonitorExit[var4_1] (shouldn't be in output)
            return d2;
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        super.onConfigure(sQLiteDatabase);
        try {
            sQLiteDatabase.setForeignKeyConstraintsEnabled(true);
            return;
        }
        catch (IllegalStateException illegalStateException) {
            Log.e((String)"SdlDcDBHelper", (String)("onConfigure: IllegalStateException: " + illegalStateException.getMessage()));
            illegalStateException.printStackTrace();
            return;
        }
        catch (Exception exception) {
            Log.e((String)"SdlDcDBHelper", (String)("onConfigure: Other Exception: " + exception.getMessage()));
            exception.printStackTrace();
            return;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        Log.i((String)"SdlDcDBHelper", (String)"Creating Database : dc_provider.db");
        try {
            Log.d((String)"SdlDcDBHelper", (String)"Creating TABLE : CREATE TABLE CardMaster (_id INTEGER PRIMARY KEY AUTOINCREMENT,tokenId TEXT,status TEXT,dpanSuffix TEXT,fpanSuffix TEXT,tokenProvisionStatus INTEGER NOT NULL,sessionKeys BLOB,paginationTS INTEGER,remainingOtpkCount INTEGER,replenishmentThreshold INTEGER,number1 INTEGER,number2 INTEGER,data1 TEXT,data2 TEXT,data3 TEXT,blob1 BLOB,blob2 BLOB,blob3 BLOB )");
            sQLiteDatabase.execSQL("CREATE TABLE CardMaster (_id INTEGER PRIMARY KEY AUTOINCREMENT,tokenId TEXT,status TEXT,dpanSuffix TEXT,fpanSuffix TEXT,tokenProvisionStatus INTEGER NOT NULL,sessionKeys BLOB,paginationTS INTEGER,remainingOtpkCount INTEGER,replenishmentThreshold INTEGER,number1 INTEGER,number2 INTEGER,data1 TEXT,data2 TEXT,data3 TEXT,blob1 BLOB,blob2 BLOB,blob3 BLOB )");
            Log.d((String)"SdlDcDBHelper", (String)"Creating TABLE : CREATE TABLE CardDetails (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,dataId INTEGER NOT NULL,data BLOB NOT NULL, FOREIGN KEY(cardMasterId) REFERENCES CardMaster(_id) ON DELETE CASCADE,UNIQUE (cardMasterId, dataId)  )");
            sQLiteDatabase.execSQL("CREATE TABLE CardDetails (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,dataId INTEGER NOT NULL,data BLOB NOT NULL, FOREIGN KEY(cardMasterId) REFERENCES CardMaster(_id) ON DELETE CASCADE,UNIQUE (cardMasterId, dataId)  )");
            Log.d((String)"SdlDcDBHelper", (String)"Creating TABLE : CREATE TABLE PaymentProfiles (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,payProfileId INTEGER NOT NULL,ctq BLOB,auc BLOB,pru BLOB,aip BLOB,afl BLOB,cpr BLOB,arm BLOB,cvm BLOB,cl BLOB,data1 TEXT,data2 TEXT,blob1 BLOB,blob2 BLOB, FOREIGN KEY(cardMasterId) REFERENCES CardMaster(_id) ON DELETE CASCADE,UNIQUE (cardMasterId, payProfileId)  )");
            sQLiteDatabase.execSQL("CREATE TABLE PaymentProfiles (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,payProfileId INTEGER NOT NULL,ctq BLOB,auc BLOB,pru BLOB,aip BLOB,afl BLOB,cpr BLOB,arm BLOB,cvm BLOB,cl BLOB,data1 TEXT,data2 TEXT,blob1 BLOB,blob2 BLOB, FOREIGN KEY(cardMasterId) REFERENCES CardMaster(_id) ON DELETE CASCADE,UNIQUE (cardMasterId, payProfileId)  )");
            Log.d((String)"SdlDcDBHelper", (String)"Creating TABLE : CREATE TABLE MetaData (_id INTEGER PRIMARY KEY AUTOINCREMENT,dataId INTEGER NOT NULL,data1 TEXT,data2 TEXT,data3 TEXT,num1 INTEGER,num2 INTEGER,num3 INTEGER,blob1 BLOB,blob2 BLOB,blob3 BLOB )");
            sQLiteDatabase.execSQL("CREATE TABLE MetaData (_id INTEGER PRIMARY KEY AUTOINCREMENT,dataId INTEGER NOT NULL,data1 TEXT,data2 TEXT,data3 TEXT,num1 INTEGER,num2 INTEGER,num3 INTEGER,blob1 BLOB,blob2 BLOB,blob3 BLOB )");
            return;
        }
        catch (SQLException sQLException) {
            Log.e((String)"SdlDcDBHelper", (String)("onCreate: SqlException: " + sQLException.getMessage()));
            sQLException.printStackTrace();
            return;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
        Log.i((String)"SdlDcDBHelper", (String)"onUpgrade");
    }
}

