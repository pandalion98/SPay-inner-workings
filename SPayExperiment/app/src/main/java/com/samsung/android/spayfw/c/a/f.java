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

public class f
extends a {
    private static f Bv;
    private static SQLiteDatabase Bw;
    private static final String TAG;

    static {
        TAG = f.class.getSimpleName();
    }

    private f(Context context) {
        super(context, "mc_enc.db", null, 1);
    }

    public static f U(Context context) {
        Class<f> class_ = f.class;
        synchronized (f.class) {
            if (Bv == null) {
                Bv = new f(context);
            }
            f f2 = Bv;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return f2;
        }
    }

    public void close() {
        f f2 = this;
        synchronized (f2) {
            super.close();
            if (Bw != null && Bw.isOpen()) {
                Bw.close();
            }
            return;
        }
    }

    @Override
    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        super.onConfigure(sQLiteDatabase);
        Log.d((String)TAG, (String)"onConfigure: FOREIGN KEY ENFORCEMENT = true");
        try {
            sQLiteDatabase.setForeignKeyConstraintsEnabled(true);
            return;
        }
        catch (IllegalStateException illegalStateException) {
            Log.e((String)TAG, (String)("onConfigure: IllegalStateException: " + illegalStateException.getMessage()));
            illegalStateException.printStackTrace();
            return;
        }
        catch (Exception exception) {
            Log.e((String)TAG, (String)("onConfigure: Exception: " + exception.getMessage()));
            exception.printStackTrace();
            return;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        Log.d((String)TAG, (String)"Creating Database");
        try {
            Log.d((String)TAG, (String)"Creating TABLE=CREATE TABLE CARD_MASTER (_id INTEGER PRIMARY KEY AUTOINCREMENT,tokenUniqueReference TEXT,mpaInstanceId TEXT,status TEXT,suspendedBy TEXT,tokenPanSuffix TEXT,accountPanSuffix TEXT,isProvisioned INTEGER NOT NULL,rgkDerivedKeys TEXT,tokenExpiry TEXT )");
            sQLiteDatabase.execSQL("CREATE TABLE CARD_MASTER (_id INTEGER PRIMARY KEY AUTOINCREMENT,tokenUniqueReference TEXT,mpaInstanceId TEXT,status TEXT,suspendedBy TEXT,tokenPanSuffix TEXT,accountPanSuffix TEXT,isProvisioned INTEGER NOT NULL,rgkDerivedKeys TEXT,tokenExpiry TEXT )");
            Log.d((String)TAG, (String)"Creating TABLE=CREATE TABLE CARD_PROVISION_DATA (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,dataId INTEGER NOT NULL,data BLOB NOT NULL, FOREIGN KEY(cardMasterId) REFERENCES CARD_MASTER(_id) ON DELETE CASCADE )");
            sQLiteDatabase.execSQL("CREATE TABLE CARD_PROVISION_DATA (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,dataId INTEGER NOT NULL,data BLOB NOT NULL, FOREIGN KEY(cardMasterId) REFERENCES CARD_MASTER(_id) ON DELETE CASCADE )");
            Log.d((String)TAG, (String)"Creating TABLE=CREATE TABLE TDS_METADATA (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,tdsUrl TEXT,tdsRegisterUrl TEXT,paymentAppInstanceId TEXT,hash TEXT,authCode TEXT,lastUpdateTag, FOREIGN KEY(cardMasterId) REFERENCES CARD_MASTER(_id) ON DELETE CASCADE )");
            sQLiteDatabase.execSQL("CREATE TABLE TDS_METADATA (_id INTEGER PRIMARY KEY AUTOINCREMENT,cardMasterId INTEGER NOT NULL,tdsUrl TEXT,tdsRegisterUrl TEXT,paymentAppInstanceId TEXT,hash TEXT,authCode TEXT,lastUpdateTag, FOREIGN KEY(cardMasterId) REFERENCES CARD_MASTER(_id) ON DELETE CASCADE )");
            Log.d((String)TAG, (String)"Creating TABLE=CREATE TABLE CERTIFICATES (_id INTEGER PRIMARY KEY,publicCertPem TEXT NOT NULL,publicCertAlias TEXT NOT NULL,cardInfoCertPem TEXT NOT NULL,cardInfoCertAlias TEXT NOT NULL )");
            sQLiteDatabase.execSQL("CREATE TABLE CERTIFICATES (_id INTEGER PRIMARY KEY,publicCertPem TEXT NOT NULL,publicCertAlias TEXT NOT NULL,cardInfoCertPem TEXT NOT NULL,cardInfoCertAlias TEXT NOT NULL )");
            return;
        }
        catch (SQLException sQLException) {
            Log.e((String)TAG, (String)("onCreate: SqlException: " + sQLException.getMessage()));
            sQLException.printStackTrace();
            return;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int n2, int n3) {
        Log.d((String)TAG, (String)"Database Upgrade not supported at this point");
    }
}

