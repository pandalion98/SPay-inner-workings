package com.samsung.cpp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.util.LongSparseArray;
import com.samsung.cpp.CPPositioningService.RequestCPGeoFenceRegister;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.GregorianCalendar;

public class CPPDbAdapter {
    private static final String DATABASE_GEO_CREATE = "CREATE TABLE Geo_Table (_geo_id integer primary key autoincrement,geo_lat REAL,geo_lon REAL,geo_mode INTEGER,geo_radius INTEGER,geo_period INTEGER);";
    private static final String DATABASE_NAME = "cellDB.db";
    private static final String DATABASE_PATCH_CREATE = "CREATE TABLE Patch_Table (_patch_id integer primary key autoincrement,req_id INTEGER,version INTEGER,latIndex INTEGER, lonIndex INTEGER,patch_fcn INTEGER);";
    private static final String DATABASE_REQ_CREATE = "CREATE TABLE Request_Table (_req_id integer primary key autoincrement,RAT INTEGER,MODE INTEGER,LATENCY INTEGER,db_req_sn INTEGER,req_type INTEGER,db_type INTEGER,patch_class INTEGER,mcc INTEGER,mnc INTEGER,tac INTEGER,gci INTEGER,pci INTEGER,fcn INTEGER,encryption_key INTEGER,is_sending INTEGER);";
    private static final String DATABASE_RESP_PATCH_CREATE = "CREATE TABLE Resp_Patch_Table (_d_patch_id integer primary key autoincrement,rat_d INTEGER,sn_d INTEGER,up_d INTEGER,patch_class_d INTEGER,patch_ver_d INTEGER,patch_lat_index_d INTEGER,patch_lon_index_d INTEGER,fcn_d INTEGER,req_id_d INTEGER,numcell_d INTEGER,patch_length_d INTEGER,cell_data_d BLOB);";
    private static final int DATABASE_VERSION = 1;
    private static final String GEO_DATABASE_NAME = "geoDB.db";
    private static final String GEO_TABLE = "Geo_Table";
    public static final String KEY_CELL_DATA_D = "cell_data_d";
    public static final String KEY_DB_TYPE = "db_type";
    public static final String KEY_ENC_KEY = "encryption_key";
    public static final String KEY_FCN = "fcn";
    public static final String KEY_FCN_D = "fcn_d";
    public static final String KEY_GCI = "gci";
    public static final String KEY_GEO_LAT = "geo_lat";
    public static final String KEY_GEO_LON = "geo_lon";
    public static final String KEY_GEO_MODE = "geo_mode";
    public static final String KEY_GEO_PERIOD = "geo_period";
    public static final String KEY_GEO_RADIUS = "geo_radius";
    public static final String KEY_ID_GEO = "_geo_id";
    public static final String KEY_ID_PATCH = "_patch_id";
    public static final String KEY_ID_PATCH_D = "_d_patch_id";
    public static final String KEY_ID_REQ = "_req_id";
    public static final String KEY_IS_SENDING = "is_sending";
    public static final String KEY_LATENCY = "LATENCY";
    public static final String KEY_LATINDEX = "latIndex";
    public static final String KEY_LONINDEX = "lonIndex";
    public static final String KEY_MCC = "mcc";
    public static final String KEY_MNC = "mnc";
    public static final String KEY_MODE = "MODE";
    public static final String KEY_NUMCELL_D = "numcell_d";
    public static final String KEY_PATCH_CLASS = "patch_class";
    public static final String KEY_PATCH_CLASS_D = "patch_class_d";
    public static final String KEY_PATCH_FCN = "patch_fcn";
    public static final String KEY_PATCH_LAT_INDEX_D = "patch_lat_index_d";
    public static final String KEY_PATCH_LENGTH_D = "patch_length_d";
    public static final String KEY_PATCH_LON_INDEX_D = "patch_lon_index_d";
    public static final String KEY_PATCH_VER_D = "patch_ver_d";
    public static final String KEY_PCI = "pci";
    public static final String KEY_RAT = "RAT";
    public static final String KEY_RAT_D = "rat_d";
    public static final String KEY_REQ_ID = "req_id";
    public static final String KEY_REQ_ID_D = "req_id_d";
    public static final String KEY_REQ_TYPE = "req_type";
    public static final String KEY_SN = "db_req_sn";
    public static final String KEY_SN_D = "sn_d";
    public static final String KEY_TAC = "tac";
    public static final String KEY_UP_D = "up_d";
    public static final String KEY_VERSION = "version";
    private static final String PATCH_TABLE = "Patch_Table";
    private static final String REQ_TABLE = "Request_Table";
    private static final String RESP_PATCH_TABLE = "Resp_Patch_Table";
    private static final String TAG = "CPPDbAdapter";
    public LongSparseArray<Long> arrayUtc = new LongSparseArray();
    public LongSparseArray<Integer> arrayUtcKey = new LongSparseArray();
    private final Context context;
    private DbHelper mDbHelper = new DbHelper(this.context);
    private GeoDbHelper mGeoDbHelper = new GeoDbHelper(this.context);
    private SQLiteDatabase mdb;
    private SQLiteDatabase mgdb;

    private class DbHelper extends SQLiteOpenHelper {
        public DbHelper(Context context) {
            super(context, CPPDbAdapter.DATABASE_NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase _db) {
            Log.d(CPPDbAdapter.TAG, "DbAdapter.DbHelper.onCreate()");
            _db.execSQL(CPPDbAdapter.DATABASE_REQ_CREATE);
            _db.execSQL(CPPDbAdapter.DATABASE_PATCH_CREATE);
            _db.execSQL(CPPDbAdapter.DATABASE_RESP_PATCH_CREATE);
        }

        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.d(CPPDbAdapter.TAG, "Upgrading db from version" + oldVersion + " to" + newVersion + ", which will destroy all old data");
            _db.execSQL("DROP TABLE IF EXISTS dataRequest_Table");
            _db.execSQL("DROP TABLE IF EXISTS dataPatch_Table");
            _db.execSQL("DROP TABLE IF EXISTS dataResp_Patch_Table");
            onCreate(_db);
        }
    }

    private class GeoDbHelper extends SQLiteOpenHelper {
        public GeoDbHelper(Context context) {
            super(context, CPPDbAdapter.GEO_DATABASE_NAME, null, 1);
        }

        public void onCreate(SQLiteDatabase _db) {
            Log.d(CPPDbAdapter.TAG, "DbAdapter.GeoDbHelper.onCreate()");
            _db.execSQL(CPPDbAdapter.DATABASE_GEO_CREATE);
        }

        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.d(CPPDbAdapter.TAG, "Upgrading db from version" + oldVersion + " to" + newVersion + ", which will destroy all old data");
            _db.execSQL("DROP TABLE IF EXISTS dataGeo_Table");
            onCreate(_db);
        }
    }

    public CPPDbAdapter(Context _context) {
        this.context = _context;
    }

    public CPPDbAdapter open() throws SQLException {
        Log.d(TAG, "DbAdapter.open()");
        this.mdb = this.mDbHelper.getWritableDatabase();
        this.mgdb = this.mGeoDbHelper.getWritableDatabase();
        Log.d(TAG, "DbAdapter.open() -- completed");
        return this;
    }

    public void close() {
        Log.d(TAG, "Close mdb!!");
        this.mdb.close();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public long insertReq(byte[] r43) {
        /*
        r42 = this;
        r4 = "CPPDbAdapter";
        r10 = "insertReq() Start";
        android.util.Log.d(r4, r10);
        r32 = 0;
        r21 = 0;
        r20 = 0;
        r15 = 0;
        r14 = r15;
        r13 = r15;
        r12 = r15;
        r35 = new android.content.ContentValues;
        r35.<init>();
        r31 = new android.content.ContentValues;
        r31.<init>();
        r4 = java.nio.ByteBuffer.wrap(r43);	 Catch:{ Exception -> 0x0256 }
        r10 = java.nio.ByteOrder.LITTLE_ENDIAN;	 Catch:{ Exception -> 0x0256 }
        r17 = r4.order(r10);	 Catch:{ Exception -> 0x0256 }
        r5 = r17.get();	 Catch:{ Exception -> 0x0256 }
        r4 = "RAT";
        r10 = java.lang.Integer.valueOf(r5);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r28 = r17.get();	 Catch:{ Exception -> 0x0256 }
        r4 = "MODE";
        r10 = java.lang.Integer.valueOf(r28);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r24 = r17.get();	 Catch:{ Exception -> 0x0256 }
        r4 = "LATENCY";
        r10 = java.lang.Integer.valueOf(r24);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r36 = r17.get();	 Catch:{ Exception -> 0x0256 }
        r4 = "db_req_sn";
        r10 = java.lang.Integer.valueOf(r36);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r34 = r17.get();	 Catch:{ Exception -> 0x0256 }
        r4 = "req_type";
        r10 = java.lang.Integer.valueOf(r34);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r18 = r17.get();	 Catch:{ Exception -> 0x0256 }
        r4 = "db_type";
        r10 = java.lang.Integer.valueOf(r18);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r30 = r17.get();	 Catch:{ Exception -> 0x0256 }
        r4 = "patch_class";
        r10 = java.lang.Integer.valueOf(r30);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r26 = r17.getShort();	 Catch:{ Exception -> 0x0256 }
        r4 = "mcc";
        r10 = java.lang.Integer.valueOf(r26);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r27 = r17.getShort();	 Catch:{ Exception -> 0x0256 }
        r4 = "mnc";
        r10 = java.lang.Integer.valueOf(r27);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = r17.getShort();	 Catch:{ Exception -> 0x0256 }
        r10 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r37 = r4 & r10;
        r4 = "tac";
        r10 = java.lang.Integer.valueOf(r37);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = r17.getInt();	 Catch:{ Exception -> 0x0256 }
        r10 = (long) r4;	 Catch:{ Exception -> 0x0256 }
        r40 = 4294967295; // 0xffffffff float:NaN double:2.1219957905E-314;
        r6 = r10 & r40;
        r4 = "gci";
        r10 = java.lang.Long.valueOf(r6);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r8 = r17.getShort();	 Catch:{ Exception -> 0x0256 }
        r4 = "pci";
        r10 = java.lang.Integer.valueOf(r8);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = r17.getShort();	 Catch:{ Exception -> 0x0256 }
        r10 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r9 = r4 & r10;
        r4 = "fcn";
        r10 = java.lang.Integer.valueOf(r9);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r29 = r17.get();	 Catch:{ Exception -> 0x0256 }
        r4 = "is_sending";
        r10 = 0;
        r10 = java.lang.Integer.valueOf(r10);	 Catch:{ Exception -> 0x0256 }
        r0 = r35;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = "CPPDbAdapter";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0256 }
        r10.<init>();	 Catch:{ Exception -> 0x0256 }
        r11 = "insertReq() rat : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.append(r5);	 Catch:{ Exception -> 0x0256 }
        r11 = ", mode : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r28;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", latency : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r24;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0256 }
        android.util.Log.d(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = "CPPDbAdapter";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0256 }
        r10.<init>();	 Catch:{ Exception -> 0x0256 }
        r11 = "insertReq() sN : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r36;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", reqType : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r34;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", dbType : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r18;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", patchClass : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r30;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", mcc : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r26;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", mnc : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r27;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", tac : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r37;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", gci : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.append(r6);	 Catch:{ Exception -> 0x0256 }
        r11 = ", pci : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.append(r8);	 Catch:{ Exception -> 0x0256 }
        r11 = ", fcn : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.append(r9);	 Catch:{ Exception -> 0x0256 }
        r11 = ", numPatch : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r29;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0256 }
        android.util.Log.d(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r10 = 0;
        r4 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1));
        if (r4 <= 0) goto L_0x01f7;
    L_0x01cc:
        r4 = r42;
        r4 = r4.checkExistedGciReq(r5, r6, r8, r9);	 Catch:{ Exception -> 0x0256 }
        if (r4 == 0) goto L_0x01f7;
    L_0x01d4:
        r4 = "CPPDbAdapter";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0256 }
        r10.<init>();	 Catch:{ Exception -> 0x0256 }
        r11 = "insertReq() : [GCI-request] checkExistedGciReq = already in req_table, send it to server : (reqId = ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r32;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ")";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0256 }
        android.util.Log.d(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r10 = 0;
    L_0x01f6:
        return r10;
    L_0x01f7:
        if (r28 != 0) goto L_0x0202;
    L_0x01f9:
        r4 = "MODE";
        r10 = "0";
        r0 = r42;
        r0.deleteReqByValue(r4, r10);	 Catch:{ Exception -> 0x0256 }
    L_0x0202:
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ Exception -> 0x0256 }
        r4.beginTransaction();	 Catch:{ Exception -> 0x0256 }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ all -> 0x024d }
        r10 = "Request_Table";
        r11 = 0;
        r0 = r35;
        r32 = r4.insert(r10, r11, r0);	 Catch:{ all -> 0x024d }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ all -> 0x024d }
        r4.setTransactionSuccessful();	 Catch:{ all -> 0x024d }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ Exception -> 0x0256 }
        r4.endTransaction();	 Catch:{ Exception -> 0x0256 }
        r4 = "CPPDbAdapter";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0256 }
        r10.<init>();	 Catch:{ Exception -> 0x0256 }
        r11 = "insertReq() reqId : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r32;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0256 }
        android.util.Log.d(r4, r10);	 Catch:{ Exception -> 0x0256 }
        if (r29 <= 0) goto L_0x0243;
    L_0x0240:
        switch(r28) {
            case 0: goto L_0x025b;
            case 1: goto L_0x040d;
            case 2: goto L_0x025b;
            case 3: goto L_0x040d;
            default: goto L_0x0243;
        };
    L_0x0243:
        r4 = "CPPDbAdapter";
        r10 = "insertReq() End";
        android.util.Log.d(r4, r10);
        r10 = r32;
        goto L_0x01f6;
    L_0x024d:
        r4 = move-exception;
        r0 = r42;
        r10 = r0.mdb;	 Catch:{ Exception -> 0x0256 }
        r10.endTransaction();	 Catch:{ Exception -> 0x0256 }
        throw r4;	 Catch:{ Exception -> 0x0256 }
    L_0x0256:
        r19 = move-exception;
        r19.printStackTrace();
        goto L_0x0243;
    L_0x025b:
        r22 = 0;
    L_0x025d:
        r0 = r22;
        r1 = r29;
        if (r0 >= r1) goto L_0x0243;
    L_0x0263:
        r4 = r17.getInt();	 Catch:{ Exception -> 0x0256 }
        r0 = (long) r4;	 Catch:{ Exception -> 0x0256 }
        r38 = r0;
        r4 = r22 % 2;
        if (r4 != 0) goto L_0x03d0;
    L_0x026e:
        r12 = r17.getShort();	 Catch:{ Exception -> 0x0256 }
        r14 = r17.getShort();	 Catch:{ Exception -> 0x0256 }
    L_0x0276:
        r4 = r17.getShort();	 Catch:{ Exception -> 0x0256 }
        r10 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r16 = r4 & r10;
        r4 = 1;
        r10 = r22 % 2;
        if (r4 != r10) goto L_0x03ec;
    L_0x0284:
        r10 = r42;
        r11 = r5;
        r4 = r10.checkExistedLargeResp(r11, r12, r13, r14, r15, r16);	 Catch:{ Exception -> 0x0256 }
        if (r4 != 0) goto L_0x03ec;
    L_0x028d:
        r4 = "req_id";
        r10 = java.lang.Long.valueOf(r32);	 Catch:{ Exception -> 0x0256 }
        r0 = r31;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = "version";
        r10 = java.lang.Long.valueOf(r38);	 Catch:{ Exception -> 0x0256 }
        r0 = r31;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = "latIndex";
        r10 = java.lang.Integer.valueOf(r12);	 Catch:{ Exception -> 0x0256 }
        r0 = r31;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = "lonIndex";
        r10 = java.lang.Integer.valueOf(r14);	 Catch:{ Exception -> 0x0256 }
        r0 = r31;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = "patch_fcn";
        r10 = java.lang.Integer.valueOf(r16);	 Catch:{ Exception -> 0x0256 }
        r0 = r31;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ Exception -> 0x0256 }
        r4.beginTransaction();	 Catch:{ Exception -> 0x0256 }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ all -> 0x03da }
        r10 = "Patch_Table";
        r11 = 0;
        r0 = r31;
        r4.insert(r10, r11, r0);	 Catch:{ all -> 0x03da }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ all -> 0x03da }
        r4.setTransactionSuccessful();	 Catch:{ all -> 0x03da }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ Exception -> 0x0256 }
        r4.endTransaction();	 Catch:{ Exception -> 0x0256 }
        r4 = "CPPDbAdapter";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0256 }
        r10.<init>();	 Catch:{ Exception -> 0x0256 }
        r11 = "insertReq() req_id : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r32;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", patch[";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r11 = r22 + -1;
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r11 = "]";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r11 = " version : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r38;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", lat1 : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.append(r12);	 Catch:{ Exception -> 0x0256 }
        r11 = ", lon1 : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.append(r14);	 Catch:{ Exception -> 0x0256 }
        r11 = ", patch_fcn : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r16;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0256 }
        android.util.Log.d(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = "latIndex";
        r10 = java.lang.Integer.valueOf(r13);	 Catch:{ Exception -> 0x0256 }
        r0 = r31;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = "lonIndex";
        r10 = java.lang.Integer.valueOf(r15);	 Catch:{ Exception -> 0x0256 }
        r0 = r31;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ Exception -> 0x0256 }
        r4.beginTransaction();	 Catch:{ Exception -> 0x0256 }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ all -> 0x03e3 }
        r10 = "Patch_Table";
        r11 = 0;
        r0 = r31;
        r4.insert(r10, r11, r0);	 Catch:{ all -> 0x03e3 }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ all -> 0x03e3 }
        r4.setTransactionSuccessful();	 Catch:{ all -> 0x03e3 }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ Exception -> 0x0256 }
        r4.endTransaction();	 Catch:{ Exception -> 0x0256 }
        r4 = "CPPDbAdapter";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0256 }
        r10.<init>();	 Catch:{ Exception -> 0x0256 }
        r11 = "insertReq() req_id : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r32;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", patch[";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r22;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = "]";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r11 = " version : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r38;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", lat2 : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.append(r13);	 Catch:{ Exception -> 0x0256 }
        r11 = ", lon2 : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.append(r15);	 Catch:{ Exception -> 0x0256 }
        r11 = ", patch_fcn : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r16;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0256 }
        android.util.Log.d(r4, r10);	 Catch:{ Exception -> 0x0256 }
    L_0x03cc:
        r22 = r22 + 1;
        goto L_0x025d;
    L_0x03d0:
        r13 = r17.getShort();	 Catch:{ Exception -> 0x0256 }
        r15 = r17.getShort();	 Catch:{ Exception -> 0x0256 }
        goto L_0x0276;
    L_0x03da:
        r4 = move-exception;
        r0 = r42;
        r10 = r0.mdb;	 Catch:{ Exception -> 0x0256 }
        r10.endTransaction();	 Catch:{ Exception -> 0x0256 }
        throw r4;	 Catch:{ Exception -> 0x0256 }
    L_0x03e3:
        r4 = move-exception;
        r0 = r42;
        r10 = r0.mdb;	 Catch:{ Exception -> 0x0256 }
        r10.endTransaction();	 Catch:{ Exception -> 0x0256 }
        throw r4;	 Catch:{ Exception -> 0x0256 }
    L_0x03ec:
        r4 = 1;
        r10 = r22 % 2;
        if (r4 != r10) goto L_0x03cc;
    L_0x03f1:
        r10 = r42;
        r11 = r5;
        r4 = r10.checkExistedLargeResp(r11, r12, r13, r14, r15, r16);	 Catch:{ Exception -> 0x0256 }
        if (r4 == 0) goto L_0x03cc;
    L_0x03fa:
        r20 = r20 + 1;
        r4 = r29 / 2;
        r0 = r20;
        if (r4 != r0) goto L_0x03cc;
    L_0x0402:
        r0 = r42;
        r1 = r32;
        r0.deleteReqById(r1);	 Catch:{ Exception -> 0x0256 }
        r10 = 0;
        goto L_0x01f6;
    L_0x040d:
        r22 = 0;
    L_0x040f:
        r0 = r22;
        r1 = r29;
        if (r0 >= r1) goto L_0x0243;
    L_0x0415:
        r4 = r17.getInt();	 Catch:{ Exception -> 0x0256 }
        r0 = (long) r4;	 Catch:{ Exception -> 0x0256 }
        r38 = r0;
        r23 = r17.getShort();	 Catch:{ Exception -> 0x0256 }
        r25 = r17.getShort();	 Catch:{ Exception -> 0x0256 }
        r4 = r17.getShort();	 Catch:{ Exception -> 0x0256 }
        r10 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        r16 = r4 & r10;
        r0 = r42;
        r1 = r23;
        r2 = r25;
        r3 = r16;
        r4 = r0.checkExistedPatchReq(r5, r1, r2, r3);	 Catch:{ Exception -> 0x0256 }
        if (r4 != 0) goto L_0x04fc;
    L_0x043b:
        r4 = "req_id";
        r10 = java.lang.Long.valueOf(r32);	 Catch:{ Exception -> 0x0256 }
        r0 = r31;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = "version";
        r10 = java.lang.Long.valueOf(r38);	 Catch:{ Exception -> 0x0256 }
        r0 = r31;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = "latIndex";
        r10 = java.lang.Integer.valueOf(r23);	 Catch:{ Exception -> 0x0256 }
        r0 = r31;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = "lonIndex";
        r10 = java.lang.Integer.valueOf(r25);	 Catch:{ Exception -> 0x0256 }
        r0 = r31;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r4 = "patch_fcn";
        r10 = java.lang.Integer.valueOf(r16);	 Catch:{ Exception -> 0x0256 }
        r0 = r31;
        r0.put(r4, r10);	 Catch:{ Exception -> 0x0256 }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ Exception -> 0x0256 }
        r4.beginTransaction();	 Catch:{ Exception -> 0x0256 }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ all -> 0x04f3 }
        r10 = "Patch_Table";
        r11 = 0;
        r0 = r31;
        r4.insert(r10, r11, r0);	 Catch:{ all -> 0x04f3 }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ all -> 0x04f3 }
        r4.setTransactionSuccessful();	 Catch:{ all -> 0x04f3 }
        r0 = r42;
        r4 = r0.mdb;	 Catch:{ Exception -> 0x0256 }
        r4.endTransaction();	 Catch:{ Exception -> 0x0256 }
        r4 = "CPPDbAdapter";
        r10 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0256 }
        r10.<init>();	 Catch:{ Exception -> 0x0256 }
        r11 = "insertReq() req_id : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r32;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", patch[";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r22;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = "]";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r11 = " version : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r38;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", latIndex : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r23;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", lonIndex : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r25;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r11 = ", patch_fcn : ";
        r10 = r10.append(r11);	 Catch:{ Exception -> 0x0256 }
        r0 = r16;
        r10 = r10.append(r0);	 Catch:{ Exception -> 0x0256 }
        r10 = r10.toString();	 Catch:{ Exception -> 0x0256 }
        android.util.Log.d(r4, r10);	 Catch:{ Exception -> 0x0256 }
    L_0x04ef:
        r22 = r22 + 1;
        goto L_0x040f;
    L_0x04f3:
        r4 = move-exception;
        r0 = r42;
        r10 = r0.mdb;	 Catch:{ Exception -> 0x0256 }
        r10.endTransaction();	 Catch:{ Exception -> 0x0256 }
        throw r4;	 Catch:{ Exception -> 0x0256 }
    L_0x04fc:
        r21 = r21 + 1;
        r0 = r29;
        r1 = r21;
        if (r0 != r1) goto L_0x04ef;
    L_0x0504:
        r0 = r42;
        r1 = r32;
        r0.deleteReqById(r1);	 Catch:{ Exception -> 0x0256 }
        r10 = 0;
        goto L_0x01f6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.samsung.cpp.CPPDbAdapter.insertReq(byte[]):long");
    }

    public Cursor getReq(String key, String value) throws SQLException {
        Log.d(TAG, "getReq() key : " + key + ", value : " + value);
        String _selection = key + "=" + value;
        this.mdb.beginTransaction();
        try {
            Cursor mCursor = this.mdb.query(REQ_TABLE, null, _selection, null, null, null, null);
            this.mdb.setTransactionSuccessful();
            return mCursor;
        } finally {
            this.mdb.endTransaction();
        }
    }

    public Cursor getPatch(long reqId) throws SQLException {
        Log.d(TAG, "getPatch() reqId : " + reqId);
        String _selection = "req_id=" + reqId;
        this.mdb.beginTransaction();
        try {
            Cursor mCursor = this.mdb.query(PATCH_TABLE, null, _selection, null, null, null, null);
            this.mdb.setTransactionSuccessful();
            return mCursor;
        } finally {
            this.mdb.endTransaction();
        }
    }

    public boolean isLargeReq(long reqId) {
        Log.d(TAG, "isLargeReq() reqId : " + reqId);
        int reqType = 0;
        String _selection = "_req_id=" + reqId;
        this.mdb.beginTransaction();
        try {
            Cursor mCursor = this.mdb.query(REQ_TABLE, null, _selection, null, null, null, null);
            this.mdb.setTransactionSuccessful();
            if (!(mCursor == null || !mCursor.moveToFirst() || mCursor.isClosed())) {
                reqType = mCursor.getInt(mCursor.getColumnIndex(KEY_REQ_TYPE));
            }
            if (!(mCursor == null || mCursor.isClosed())) {
                mCursor.close();
            }
            Log.d(TAG, "isLargeReq() reqType : " + reqType);
            if (reqType > 0) {
                return true;
            }
            return false;
        } finally {
            this.mdb.endTransaction();
        }
    }

    public void insertResp(byte[] data, int size, int rat, long reqId) {
        Log.d(TAG, "insertResp() data : " + Arrays.toString(data) + ", size : " + size + ", rat : " + rat + ", reqId : " + reqId);
        Long objUtc = (Long) this.arrayUtc.get(reqId);
        Integer objUtcKey = (Integer) this.arrayUtcKey.get(reqId);
        ContentValues patchValues = new ContentValues();
        Log.d(TAG, "insertResp() Start");
        try {
            Log.d(TAG, "insertResp() UTC : " + objUtc.longValue() + ", UTC Key : " + objUtcKey.intValue());
        } catch (NullPointerException e) {
            if (objUtcKey == null) {
                Log.d(TAG, "insertResp() utcKey = null, end insert");
                return;
            }
        }
        try {
            ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            int sN = bb.get();
            bb.getInt();
            long uP = (long) bb.getInt();
            int numPatch = bb.getShort();
            Log.d(TAG, "insertResp() reqId : " + reqId + ", sN : " + sN + ", uP : " + uP + ", numPatch : " + numPatch);
            if (numPatch == 0) {
                Log.d(TAG, "insertResp() numPatch == 0, end insert");
                return;
            }
            int i = 0;
            while (i < numPatch) {
                Log.d(TAG, "insertResp() insert patch[" + i + "]");
                patchValues.put(KEY_RAT_D, Integer.valueOf(rat));
                patchValues.put(KEY_SN_D, Integer.valueOf(sN));
                patchValues.put(KEY_UP_D, Long.valueOf(uP));
                patchValues.put(KEY_REQ_ID_D, Long.valueOf(reqId));
                patchValues.put(KEY_PATCH_CLASS_D, Byte.valueOf(bb.get()));
                patchValues.put(KEY_PATCH_VER_D, Integer.valueOf(bb.getInt()));
                int latIndex = (bb.getShort() & 65535) ^ objUtcKey.intValue();
                patchValues.put(KEY_PATCH_LAT_INDEX_D, Integer.valueOf(latIndex));
                int lonIndex = (bb.getShort() & 65535) ^ objUtcKey.intValue();
                patchValues.put(KEY_PATCH_LON_INDEX_D, Integer.valueOf(lonIndex));
                int fcn = bb.getShort() & 65535;
                patchValues.put(KEY_FCN_D, Integer.valueOf(fcn));
                int numCells = bb.getShort();
                patchValues.put(KEY_NUMCELL_D, Integer.valueOf(numCells));
                int patchLength = bb.getInt();
                patchValues.put(KEY_PATCH_LENGTH_D, Integer.valueOf(patchLength));
                Log.d(TAG, "insertResp() latIndex : " + latIndex + ", lonIndex : " + lonIndex + ", fcn : " + fcn + ", numCells : " + numCells + ", patchLength : " + patchLength);
                if (patchLength <= 80000) {
                    byte[] cellData = new byte[patchLength];
                    bb.get(cellData, 0, patchLength);
                    patchValues.put(KEY_CELL_DATA_D, cellData);
                    deleteExistedDb(rat, latIndex, lonIndex, fcn);
                    this.mdb.beginTransaction();
                    this.mdb.insert(RESP_PATCH_TABLE, null, patchValues);
                    this.mdb.setTransactionSuccessful();
                    this.mdb.endTransaction();
                    i++;
                } else {
                    Log.d(TAG, "insertResp() patchLength is over 80000");
                    Log.d(TAG, "insertResp() Finish");
                    return;
                }
            }
            Log.d(TAG, "insertResp() End Position : " + bb.position());
            Log.d(TAG, "insertResp() Finish");
        } catch (Exception e2) {
            e2.printStackTrace();
        } catch (Throwable th) {
            this.mdb.endTransaction();
        }
    }

    public byte[] getRespForCp(long reqId) throws IOException {
        Log.d(TAG, "getRespForCp() reqId : " + reqId);
        byte[] init = new byte[]{(byte) 34, (byte) 86, (byte) 97, (byte) 108, (byte) 117, (byte) 101, (byte) 34, (byte) 58};
        byte[] bArr = new byte[2];
        bArr = new byte[]{(byte) 0, (byte) 0};
        byte[] bArr2 = new byte[4];
        bArr2 = new byte[]{(byte) 0, (byte) 0, (byte) 0, (byte) 0};
        byte[] result = null;
        Cursor mC1 = null;
        String q1 = "req_id_d=" + reqId;
        this.mdb.beginTransaction();
        try {
            mC1 = this.mdb.query(RESP_PATCH_TABLE, null, q1, null, null, null, null);
            this.mdb.setTransactionSuccessful();
        } catch (SQLException sq) {
            sq.printStackTrace();
        } finally {
            this.mdb.endTransaction();
        }
        if (mC1 == null || !mC1.moveToFirst() || mC1.isClosed()) {
            if (mC1 != null) {
                mC1.close();
            }
            return null;
        }
        try {
            ByteBuffer bb = ByteBuffer.allocate(524288).order(ByteOrder.LITTLE_ENDIAN);
            Log.d(TAG, "getRespForCp() mC1.getCount : " + mC1.getCount());
            bb.put(init);
            bb.put((byte) 1);
            bb.put(bArr);
            bb.put((byte) mC1.getInt(mC1.getColumnIndex(KEY_RAT_D)));
            bb.put((byte) mC1.getInt(mC1.getColumnIndex(KEY_SN_D)));
            bb.put(bArr2);
            bb.putInt(mC1.getInt(mC1.getColumnIndex(KEY_UP_D)));
            int numPatch = mC1.getCount();
            bb.putShort((short) numPatch);
            for (int i = 0; i < numPatch; i++) {
                Log.d(TAG, "Get Patch Table Start");
                bb.put((byte) mC1.getInt(mC1.getColumnIndex(KEY_PATCH_CLASS_D)));
                bb.putInt(mC1.getInt(mC1.getColumnIndex(KEY_PATCH_VER_D)));
                bb.putShort(mC1.getShort(mC1.getColumnIndex(KEY_PATCH_LAT_INDEX_D)));
                bb.putShort(mC1.getShort(mC1.getColumnIndex(KEY_PATCH_LON_INDEX_D)));
                bb.putShort(mC1.getShort(mC1.getColumnIndex(KEY_FCN_D)));
                bb.putShort(mC1.getShort(mC1.getColumnIndex(KEY_NUMCELL_D)));
                bb.putInt(mC1.getInt(mC1.getColumnIndex(KEY_PATCH_LENGTH_D)));
                bb.put(mC1.getBlob(mC1.getColumnIndex(KEY_CELL_DATA_D)));
                if (!mC1.moveToNext()) {
                    break;
                }
            }
            Log.d(TAG, "getRespForCp() size : " + bb.position());
            int size = bb.position();
            result = new byte[size];
            bb.position(0);
            bb.get(result, 0, size);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mC1.close();
        Log.d(TAG, "getRespForCp() Finish");
        return result;
    }

    public boolean checkExistedGciReq(int rat, long gci, int pci, int fcn) {
        boolean exist = false;
        String _selection = "RAT=" + rat + " AND " + KEY_FCN + "=" + fcn + " AND " + KEY_GCI + "=" + gci + " AND " + KEY_PCI + "=" + pci;
        this.mdb.beginTransaction();
        try {
            Cursor mCursor = this.mdb.query(REQ_TABLE, null, _selection, null, null, null, null);
            this.mdb.setTransactionSuccessful();
            if (!(mCursor == null || !mCursor.moveToFirst() || mCursor.isClosed())) {
                exist = true;
            }
            if (!(mCursor == null || mCursor.isClosed())) {
                mCursor.close();
            }
            Log.d(TAG, "checkExistedGciReq() " + exist);
            return exist;
        } finally {
            this.mdb.endTransaction();
        }
    }

    public boolean checkExistedPatchReq(int rat, int lat, int lon, int patch_fcn) {
        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        joinReqTables(sqb);
        boolean exist = false;
        String query = sqb.buildQuery(null, "Request_Table.RAT=" + rat + " AND " + PATCH_TABLE + "." + KEY_LATINDEX + "=" + lat + " AND " + PATCH_TABLE + "." + KEY_LONINDEX + "=" + lon + " AND " + PATCH_TABLE + "." + KEY_PATCH_FCN + "=" + patch_fcn, null, null, null, null);
        this.mdb.beginTransaction();
        try {
            Cursor mCursor = this.mdb.rawQuery(query, null);
            this.mdb.setTransactionSuccessful();
            if (!(mCursor == null || !mCursor.moveToFirst() || mCursor.isClosed())) {
                exist = true;
            }
            if (!(mCursor == null || mCursor.isClosed())) {
                mCursor.close();
            }
            Log.d(TAG, "checkExistedPatchReq() " + exist);
            return exist;
        } finally {
            this.mdb.endTransaction();
        }
    }

    public boolean checkExistedLargeResp(int rat, int lat1, int lat2, int lon1, int lon2, int fcn) {
        boolean exist = false;
        String _selection = "rat_d=" + rat + " AND " + KEY_FCN_D + "=" + fcn + " AND " + KEY_PATCH_LAT_INDEX_D + ">=" + lat1 + " AND " + KEY_PATCH_LAT_INDEX_D + "<=" + lat2 + " AND " + KEY_PATCH_LON_INDEX_D + ">=" + lon1 + " AND " + KEY_PATCH_LON_INDEX_D + "<=" + lon2;
        this.mdb.beginTransaction();
        try {
            Cursor mCursor = this.mdb.query(RESP_PATCH_TABLE, null, _selection, null, null, null, null);
            this.mdb.setTransactionSuccessful();
            if (mCursor != null && mCursor.moveToFirst() && !mCursor.isClosed() && mCursor.getCount() > 8) {
                exist = true;
            }
            if (!(mCursor == null || mCursor.isClosed())) {
                mCursor.close();
            }
            if (!exist) {
                this.mdb.beginTransaction();
                try {
                    this.mdb.delete(RESP_PATCH_TABLE, _selection, null);
                    this.mdb.setTransactionSuccessful();
                } finally {
                    this.mdb.endTransaction();
                }
            }
            Log.d(TAG, "checkExistedLargeResp() " + exist);
            return exist;
        } finally {
            this.mdb.endTransaction();
        }
    }

    public long checkExistedPatchResp(long reqId, int rat, int sN, int lat, int lon, int fcn, long version) {
        long value = version;
        GregorianCalendar gc = new GregorianCalendar();
        long today = Long.parseLong(String.format("%d%02d%02d", new Object[]{Integer.valueOf(gc.get(1)), Integer.valueOf(gc.get(2) + 1), Integer.valueOf(gc.get(5))}));
        String _selection = "Resp_Patch_Table.rat_d=" + rat + " AND " + RESP_PATCH_TABLE + "." + KEY_PATCH_LAT_INDEX_D + "=" + lat + " AND " + RESP_PATCH_TABLE + "." + KEY_PATCH_LON_INDEX_D + "=" + lon + " AND " + RESP_PATCH_TABLE + "." + KEY_FCN_D + "=" + fcn;
        this.mdb.beginTransaction();
        try {
            Cursor mCursor = this.mdb.query(RESP_PATCH_TABLE, null, _selection, null, null, null, null);
            this.mdb.setTransactionSuccessful();
            if (mCursor != null && mCursor.moveToFirst() && !mCursor.isClosed()) {
                loop0:
                for (int i = 0; i < mCursor.getCount(); i++) {
                    long patchVer = (long) mCursor.getInt(mCursor.getColumnIndex(KEY_PATCH_VER_D));
                    if (today <= ((long) mCursor.getInt(mCursor.getColumnIndex(KEY_UP_D)))) {
                        long patchId = mCursor.getLong(mCursor.getColumnIndex(KEY_ID_PATCH_D));
                        ContentValues updateInfo = new ContentValues();
                        updateInfo.put(KEY_REQ_ID_D, Long.valueOf(reqId));
                        updateInfo.put(KEY_SN_D, Integer.valueOf(sN));
                        this.mdb.beginTransaction();
                        try {
                            ContentValues contentValues = updateInfo;
                            Log.d(TAG, "checkExistedPatchResp() updated : " + this.mdb.update(RESP_PATCH_TABLE, contentValues, "_d_patch_id= ?", new String[]{String.valueOf(patchId)}));
                            this.mdb.setTransactionSuccessful();
                            value = 0;
                        } finally {
                            this.mdb.endTransaction();
                        }
                    } else if (version > patchVer) {
                        value = version;
                        Log.d(TAG, "checkExistedPatchResp() Send request to server");
                    } else {
                        value = patchVer;
                        Log.d(TAG, "checkExistedPatchResp() Change version and send request to server");
                    }
                    if (!mCursor.moveToNext()) {
                        break loop0;
                    }
                }
            }
            if (!(mCursor == null || mCursor.isClosed())) {
                mCursor.close();
            }
            return value;
        } finally {
            this.mdb.endTransaction();
        }
    }

    public void updateSending(long reqId, boolean isSending) {
        Log.d(TAG, "updateSending() reqId : " + reqId + ", isSending : " + isSending);
        ContentValues updateInfo = new ContentValues();
        String _selection = "_req_id=" + String.valueOf(reqId);
        this.mdb.beginTransaction();
        try {
            Cursor mCursor = this.mdb.query(REQ_TABLE, null, _selection, null, null, null, null);
            this.mdb.setTransactionSuccessful();
            if (!(mCursor == null || !mCursor.moveToFirst() || mCursor.isClosed())) {
                if (isSending) {
                    updateInfo.put(KEY_IS_SENDING, Integer.valueOf(1));
                } else {
                    updateInfo.put(KEY_IS_SENDING, Integer.valueOf(0));
                }
                this.mdb.beginTransaction();
                try {
                    this.mdb.update(REQ_TABLE, updateInfo, "_req_id= ?", new String[]{String.valueOf(reqId)});
                    this.mdb.setTransactionSuccessful();
                } finally {
                    this.mdb.endTransaction();
                }
            }
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
        } finally {
            this.mdb.endTransaction();
        }
    }

    public void deleteAllReq() {
        this.mdb.beginTransaction();
        try {
            this.mdb.delete(REQ_TABLE, null, null);
            this.mdb.delete(PATCH_TABLE, null, null);
            Log.d(TAG, "deleteAllReq()");
        } finally {
            this.mdb.endTransaction();
        }
    }

    public void deleteReqById(long reqId) {
        this.mdb.beginTransaction();
        try {
            int _tempPatch = this.mdb.delete(PATCH_TABLE, "req_id=" + reqId, null);
            int _tempRequest = this.mdb.delete(REQ_TABLE, "_req_id=" + reqId, null);
            this.mdb.setTransactionSuccessful();
            Log.d(TAG, "deleteReqById() reqId : " + reqId + ", deleted row in Patch_Table : " + _tempPatch + " / in Request_Table : " + _tempRequest);
        } finally {
            this.mdb.endTransaction();
        }
    }

    public void deleteReqByValue(String key, String value) {
        String selection = key + "=" + value;
        this.mdb.beginTransaction();
        try {
            Cursor mCursor = this.mdb.query(REQ_TABLE, null, selection, null, null, null, null);
            this.mdb.setTransactionSuccessful();
            if (mCursor == null || !mCursor.moveToFirst() || mCursor.isClosed()) {
                Log.d(TAG, "deleteReqByValue() reqId : " + 0 + ", delete nothing");
            } else {
                long reqId = mCursor.getLong(mCursor.getColumnIndex("req_id"));
                this.mdb.beginTransaction();
                try {
                    int _tempPatch = this.mdb.delete(PATCH_TABLE, "req_id=" + reqId, null);
                    Log.d(TAG, "deleteReqByValue() reqId : " + reqId + ", deleted patch : " + _tempPatch + ", deleted req : " + this.mdb.delete(REQ_TABLE, "_req_id=" + reqId, null));
                    this.mdb.setTransactionSuccessful();
                } finally {
                    this.mdb.endTransaction();
                }
            }
            if (mCursor != null && !mCursor.isClosed()) {
                mCursor.close();
            }
        } finally {
            this.mdb.endTransaction();
        }
    }

    public void deleteReqId(long reqId) {
        int deleted = 0;
        String selection = "req_id_d=" + reqId;
        this.mdb.beginTransaction();
        try {
            Cursor mCursor = this.mdb.query(RESP_PATCH_TABLE, null, selection, null, null, null, null);
            this.mdb.setTransactionSuccessful();
            if (!(mCursor == null || !mCursor.moveToFirst() || mCursor.isClosed())) {
                ContentValues updateInfo = new ContentValues();
                updateInfo.put(KEY_REQ_ID_D, Integer.valueOf(-1));
                updateInfo.put(KEY_SN_D, Integer.valueOf(-1));
                this.mdb.beginTransaction();
                int i = 0;
                while (i < mCursor.getCount()) {
                    try {
                        deleted = this.mdb.update(RESP_PATCH_TABLE, updateInfo, null, null);
                        if (!mCursor.moveToNext()) {
                            break;
                        }
                        i++;
                    } finally {
                        this.mdb.endTransaction();
                    }
                }
                this.mdb.setTransactionSuccessful();
            }
            if (!(mCursor == null || mCursor.isClosed())) {
                mCursor.close();
            }
            Log.d(TAG, "deleteReqById() reqId : " + reqId + ", deleted patches in Resp_Patch_Table : " + deleted);
        } finally {
            this.mdb.endTransaction();
        }
    }

    public void deleteExistedDb(int rat, int lat, int lon, int fcn) {
        String _selection = "Resp_Patch_Table.rat_d=" + rat + " AND " + RESP_PATCH_TABLE + "." + KEY_PATCH_LAT_INDEX_D + "=" + lat + " AND " + RESP_PATCH_TABLE + "." + KEY_PATCH_LON_INDEX_D + "=" + lon + " AND " + RESP_PATCH_TABLE + "." + KEY_FCN_D + "=" + fcn;
        this.mdb.beginTransaction();
        try {
            Log.d(TAG, "deleteExistedDb() deleted : " + this.mdb.delete(RESP_PATCH_TABLE, _selection, null));
            this.mdb.setTransactionSuccessful();
        } finally {
            this.mdb.endTransaction();
        }
    }

    private void joinReqTables(SQLiteQueryBuilder sqb) {
        Log.d(TAG, "joinReqTables()");
        sqb.setTables("Request_Table LEFT OUTER JOIN Patch_Table ON (Request_Table._req_id = Patch_Table.req_id)");
    }

    public static byte[] intToReverseByte(int iData) {
        return Arrays.copyOfRange(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(iData).array(), 0, 2);
    }

    public int insertGeoReq(RequestCPGeoFenceRegister input) {
        long geoId = 0;
        ContentValues geoValues = new ContentValues();
        geoValues.put(KEY_GEO_LAT, Double.valueOf(input.mLatitude));
        geoValues.put(KEY_GEO_LON, Double.valueOf(input.mLongitude));
        geoValues.put(KEY_GEO_MODE, Integer.valueOf(input.mGeoMode));
        geoValues.put(KEY_GEO_RADIUS, Integer.valueOf(input.mRadius));
        geoValues.put(KEY_GEO_PERIOD, Integer.valueOf(input.mPeriod));
        this.mgdb.beginTransaction();
        try {
            geoId = this.mgdb.insert(GEO_TABLE, null, geoValues);
            this.mgdb.setTransactionSuccessful();
            return (int) geoId;
        } finally {
            this.mgdb.endTransaction();
        }
    }

    public Double getGeoLat(long geoId) {
        String _selection = "_geo_id=" + geoId;
        this.mgdb.beginTransaction();
        try {
            Cursor mCursor = this.mgdb.query(GEO_TABLE, null, _selection, null, null, null, null);
            this.mgdb.setTransactionSuccessful();
            Double lat = Double.valueOf(0.0d);
            if (!(mCursor == null || !mCursor.moveToFirst() || mCursor.isClosed())) {
                lat = Double.valueOf(mCursor.getDouble(mCursor.getColumnIndex(KEY_GEO_LAT)));
            }
            if (!(mCursor == null || mCursor.isClosed())) {
                mCursor.close();
            }
            Log.d(TAG, "getGeoLat() geoId : " + geoId + ", lat : " + lat);
            return Double.valueOf(lat.doubleValue() * 20000.0d);
        } finally {
            this.mgdb.endTransaction();
        }
    }

    public Double getGeoLon(long geoId) {
        String _selection = "_geo_id=" + geoId;
        this.mgdb.beginTransaction();
        try {
            Cursor mCursor = this.mgdb.query(GEO_TABLE, null, _selection, null, null, null, null);
            this.mgdb.setTransactionSuccessful();
            Double lon = Double.valueOf(0.0d);
            if (!(mCursor == null || !mCursor.moveToFirst() || mCursor.isClosed())) {
                lon = Double.valueOf(mCursor.getDouble(mCursor.getColumnIndex(KEY_GEO_LON)));
            }
            if (!(mCursor == null || mCursor.isClosed())) {
                mCursor.close();
            }
            Log.d(TAG, "getGeoLat() geoId : " + geoId + ", lon : " + lon);
            return Double.valueOf(lon.doubleValue() * 20000.0d);
        } finally {
            this.mgdb.endTransaction();
        }
    }

    public void deleteGeoById(int geoId) {
        this.mgdb.beginTransaction();
        try {
            int _tempGeo = this.mgdb.delete(GEO_TABLE, "_geo_id=" + geoId, null);
            this.mgdb.setTransactionSuccessful();
            Log.d(TAG, "deleteGeoById() geoId : " + geoId + ", deleted row in Geo_Table : " + _tempGeo);
        } finally {
            this.mgdb.endTransaction();
        }
    }
}
