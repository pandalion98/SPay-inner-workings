/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  android.content.ContentValues
 *  android.content.Context
 *  android.database.Cursor
 *  android.database.sqlite.SQLiteException
 *  java.lang.Class
 *  java.lang.Enum
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.ArrayList
 *  java.util.List
 */
package com.samsung.android.spayfw.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.samsung.android.spayfw.b.Log;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;

import java.util.ArrayList;
import java.util.List;

public class ServerCertsStorage
extends DbAdapter {
    private static ServerCertsStorage Ce;

    private ServerCertsStorage(Context context) {
        super(context);
        this.execSQL("CREATE TABLE IF NOT EXISTS server_certs_group(_id INTEGER, card_type TEXT NOT NULL, content TEXT NOT NULL, usage TEXT NOT NULL, alias TEXT NOT NULL, PRIMARY KEY (card_type, usage, alias) )");
        Log.i("ServerCertsStorage", "Create ServerCerts Table If Not Exists");
    }

    public static final ServerCertsStorage ad(Context context) {
        Class<ServerCertsStorage> class_ = ServerCertsStorage.class;
        synchronized (ServerCertsStorage.class) {
            if (Ce == null) {
                Ce = new ServerCertsStorage(context);
            }
            ServerCertsStorage serverCertsStorage = Ce;
            // ** MonitorExit[var3_1] (shouldn't be in output)
            return serverCertsStorage;
        }
    }

    private static CertificateInfo h(Cursor cursor) {
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setContent(cursor.getString(ServerCertsDb.ServerCertsColumn.Ch.getColumnIndex()));
        certificateInfo.setUsage(cursor.getString(ServerCertsDb.ServerCertsColumn.Ci.getColumnIndex()));
        certificateInfo.setAlias(cursor.getString(ServerCertsDb.ServerCertsColumn.Cj.getColumnIndex()));
        return certificateInfo;
    }

    public int a(String string, CertificateInfo certificateInfo) {
        if (string == null || certificateInfo == null || certificateInfo.getContent() == null || certificateInfo.getUsage() == null) {
            Log.e("ServerCertsStorage", "addServerCertsRecord: cardType/cert/cert.content/cert.usage is null");
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ServerCertsDb.ServerCertsColumn.Cg.getColumn(), string);
        contentValues.put(ServerCertsDb.ServerCertsColumn.Ch.getColumn(), certificateInfo.getContent());
        contentValues.put(ServerCertsDb.ServerCertsColumn.Ci.getColumn(), certificateInfo.getUsage());
        contentValues.put(ServerCertsDb.ServerCertsColumn.Cj.getColumn(), certificateInfo.getAlias());
        try {
            int n2 = this.a("server_certs_group", contentValues);
            Log.d("ServerCertsStorage", "RowId : " + n2);
            return n2;
        }
        catch (SQLiteException sQLiteException) {
            Log.e("ServerCertsStorage", "addTokenRecord: cannot add server certs record");
            Log.c("ServerCertsStorage", sQLiteException.getMessage(), sQLiteException);
            return -1;
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public List<CertificateInfo> a(ServerCertsDb.ServerCertsColumn var1_1, String var2_2) {
        block6 : {
            if (var1_1 == null) {
                Log.e("ServerCertsStorage", "getCertificates: column is null");
                return null;
            }
            var5_4 = var6_3 = this.f("server_certs_group", var1_1.getColumn(), var2_2);
            var7_5 = null;
            if (var5_4 == null) break block6;
            try {
                var8_6 = var5_4.getCount();
                var7_5 = null;
                if (var8_6 <= 0) break block6;
                var7_5 = new ArrayList(var5_4.getCount());
                while (var5_4.moveToNext()) {
                    var7_5.add((Object)ServerCertsStorage.h(var5_4));
                }
                break block6;
            }
            catch (Throwable var4_7) {}
            ** GOTO lbl-1000
        }
        ServerCertsStorage.a(var5_4);
        return var7_5;
        catch (Throwable var3_9) {
            var4_8 = var3_9;
            var5_4 = null;
        }
lbl-1000: // 2 sources:
        {
            ServerCertsStorage.a(var5_4);
            throw var4_8;
        }
    }

    public static class ServerCertsDb {

        public static final class ServerCertsColumn
        extends Enum<ServerCertsColumn> {
            public static final /* enum */ ServerCertsColumn Cf = new ServerCertsColumn("ID", 0, "_id", DbAdapter.ColumnType.BV);
            public static final /* enum */ ServerCertsColumn Cg = new ServerCertsColumn("CARD_TYPE", 1, "card_type", DbAdapter.ColumnType.BY);
            public static final /* enum */ ServerCertsColumn Ch = new ServerCertsColumn("CONTENT", 2, "content", DbAdapter.ColumnType.BY);
            public static final /* enum */ ServerCertsColumn Ci = new ServerCertsColumn("USAGE", 3, "usage", DbAdapter.ColumnType.BY);
            public static final /* enum */ ServerCertsColumn Cj = new ServerCertsColumn("ALIAS", 4, "alias", DbAdapter.ColumnType.BY);
            private static final /* synthetic */ ServerCertsColumn[] Ck;
            private final int columnIndex;
            private final String columnName;
            private final DbAdapter.ColumnType columnType;
            private final String columnValue;

            static {
                ServerCertsColumn[] arrserverCertsColumn = new ServerCertsColumn[]{Cf, Cg, Ch, Ci, Cj};
                Ck = arrserverCertsColumn;
            }

            private ServerCertsColumn(String string2, int n3, String string3, DbAdapter.ColumnType columnType) {
                this.columnName = string2;
                this.columnIndex = n3;
                this.columnValue = string3;
                this.columnType = columnType;
            }

            public static ServerCertsColumn valueOf(String string) {
                return (ServerCertsColumn)Enum.valueOf(ServerCertsColumn.class, (String)string);
            }

            public static ServerCertsColumn[] values() {
                return (ServerCertsColumn[])Ck.clone();
            }

            public String getColumn() {
                return this.columnValue;
            }

            public int getColumnIndex() {
                return this.columnIndex;
            }
        }

    }

}

