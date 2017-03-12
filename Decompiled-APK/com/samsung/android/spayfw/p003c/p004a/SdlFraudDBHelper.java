package com.samsung.android.spayfw.p003c.p004a;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/* renamed from: com.samsung.android.spayfw.c.a.e */
public class SdlFraudDBHelper extends SdlBaseDBHelper {
    private static SdlFraudDBHelper Bu;

    public static SdlFraudDBHelper m297T(Context context) {
        if (Bu == null) {
            Bu = new SdlFraudDBHelper(context.getApplicationContext());
        }
        return Bu;
    }

    protected SdlFraudDBHelper(Context context) {
        super(context, "collector_enc.db", null, 3);
    }

    public void onConfigure(SQLiteDatabase sQLiteDatabase) {
        if (!sQLiteDatabase.isReadOnly()) {
            sQLiteDatabase.execSQL("PRAGMA foreign_keys = ON;");
            Log.i("SdlFraudDBHelper", "FOREIGN KEY constraint enabled");
        }
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            if (sQLiteDatabase.isOpen()) {
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS fcard (id integer NOT NULL  PRIMARY KEY AUTOINCREMENT,account_id varchar(64) NOT NULL,account_creation_time integer,account_ondevice_time integer,four_digits varchar(64) NOT NULL,brand integer NOT NULL,first_name varchar(2),last_name varchar(2),last_name_hash varchar(128),avsaddr text,avszip varchar(10),country varchar(255),attempt_time integer NOT NULL,result integer NOT NULL,geo_x double,geo_y double,device_name varchar(255),token_id integer)");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ftoken (id integer NOT NULL  PRIMARY KEY AUTOINCREMENT,token_ref_key varchar(64) NOT NULL,status integer NOT NULL,card_id integer NOT NULL,CONSTRAINT token_ak_1 UNIQUE (token_ref_key,card_id),FOREIGN KEY (card_id) REFERENCES fcard (id) ON DELETE RESTRICT  ON UPDATE RESTRICT)");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ftoken_transaction_details (id integer NOT NULL  PRIMARY KEY AUTOINCREMENT,time integer NOT NULL,method varchar(30),amount varchar(30),currency varchar(5),transaction_id varchar(255),transaction_result integer NOT NULL DEFAULT 0,token_id integer NOT NULL,FOREIGN KEY (token_id) REFERENCES ftoken (id) ON DELETE CASCADE  ON UPDATE CASCADE)");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ftoken_transaction_statistics (token_id integer NOT NULL  PRIMARY KEY,approve_count integer NOT NULL DEFAULT 0,decline_count integer NOT NULL DEFAULT 0,refund_count integer NOT NULL DEFAULT 0,FOREIGN KEY (token_id) REFERENCES ftoken (id))");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ftoken_status_history (id integer NOT NULL  PRIMARY KEY AUTOINCREMENT,token_id integer NOT NULL,status integer NOT NULL,time integer NOT NULL,FOREIGN KEY (token_id) REFERENCES ftoken (id))");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS fcounter (id integer NOT NULL  PRIMARY KEY AUTOINCREMENT,enroll_id TEXT,time integer NOT NULL)");
                sQLiteDatabase.execSQL("  create table knownmachines(    machineid text,    primary key(machineid)  );");
                sQLiteDatabase.execSQL("  create table modelinfo(    modelid text,    modelbase text,    modelparams blob,    lastaccesstime real default current_timestamp,    primary key(modelid)  );");
                sQLiteDatabase.execSQL("  create table activemodels(    machineid text,    modelid text,    modelIndex integer,    foreign key(machineid) references knownmachines(machineid)  );");
                Log.i("SdlFraudDBHelper", "second database is created");
            }
        } catch (Throwable e) {
            Log.e("SdlFraudDBHelper", "Database is not fully created, any schema error?");
            Log.e("SdlFraudDBHelper", e.getMessage(), e);
        }
    }

    public void onDowngrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Log.d("SdlFraudDBHelper", "cannot downgrade the db");
    }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        Log.d("SdlFraudDBHelper", "upgrade second database from version " + i + " to version " + i2);
        if (i == i2) {
            Log.i("SdlFraudDBHelper", "no need to upgrade second db");
        } else if (i > i2) {
            Log.e("SdlFraudDBHelper", "database cannot be downgraded, please update to the latest version");
        } else {
            if (i <= 2) {
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS ftoken_transaction_statistics");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS ftoken_status_history");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS ftoken_transaction_details");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS ftoken");
                sQLiteDatabase.execSQL("DROP TABLE IF EXISTS fcard");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS fcard (id integer NOT NULL  PRIMARY KEY AUTOINCREMENT,account_id varchar(64) NOT NULL,account_creation_time integer,account_ondevice_time integer,four_digits varchar(64) NOT NULL,brand integer NOT NULL,first_name varchar(2),last_name varchar(2),last_name_hash varchar(128),avsaddr text,avszip varchar(10),country varchar(255),attempt_time integer NOT NULL,result integer NOT NULL,geo_x double,geo_y double,device_name varchar(255),token_id integer)");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ftoken (id integer NOT NULL  PRIMARY KEY AUTOINCREMENT,token_ref_key varchar(64) NOT NULL,status integer NOT NULL,card_id integer NOT NULL,CONSTRAINT token_ak_1 UNIQUE (token_ref_key,card_id),FOREIGN KEY (card_id) REFERENCES fcard (id) ON DELETE RESTRICT  ON UPDATE RESTRICT)");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ftoken_transaction_details (id integer NOT NULL  PRIMARY KEY AUTOINCREMENT,time integer NOT NULL,method varchar(30),amount varchar(30),currency varchar(5),transaction_id varchar(255),transaction_result integer NOT NULL DEFAULT 0,token_id integer NOT NULL,FOREIGN KEY (token_id) REFERENCES ftoken (id) ON DELETE CASCADE  ON UPDATE CASCADE)");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ftoken_transaction_statistics (token_id integer NOT NULL  PRIMARY KEY,approve_count integer NOT NULL DEFAULT 0,decline_count integer NOT NULL DEFAULT 0,refund_count integer NOT NULL DEFAULT 0,FOREIGN KEY (token_id) REFERENCES ftoken (id))");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS ftoken_status_history (id integer NOT NULL  PRIMARY KEY AUTOINCREMENT,token_id integer NOT NULL,status integer NOT NULL,time integer NOT NULL,FOREIGN KEY (token_id) REFERENCES ftoken (id))");
                sQLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS fcounter (id integer NOT NULL  PRIMARY KEY AUTOINCREMENT,enroll_id TEXT,time integer NOT NULL)");
                i = 3;
            }
            if (i == 3) {
                Log.d("SdlFraudDBHelper", "This is the current version");
            }
        }
    }
}
