package com.samsung.android.spayfw.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import com.samsung.android.spayfw.p002b.Log;
import com.samsung.android.spayfw.remoteservice.models.CertificateInfo;
import com.samsung.android.spayfw.storage.DbAdapter.ColumnType;
import java.util.ArrayList;
import java.util.List;

public class ServerCertsStorage extends DbAdapter {
    private static ServerCertsStorage Ce;

    public static class ServerCertsDb {

        public enum ServerCertsColumn {
            ID("ID", 0, "_id", ColumnType.INTEGER),
            CARD_TYPE("CARD_TYPE", 1, "card_type", ColumnType.TEXT),
            CONTENT("CONTENT", 2, "content", ColumnType.TEXT),
            USAGE("USAGE", 3, "usage", ColumnType.TEXT),
            ALIAS("ALIAS", 4, "alias", ColumnType.TEXT);
            
            private final int columnIndex;
            private final String columnName;
            private final ColumnType columnType;
            private final String columnValue;

            private ServerCertsColumn(String str, int i, String str2, ColumnType columnType) {
                this.columnName = str;
                this.columnIndex = i;
                this.columnValue = str2;
                this.columnType = columnType;
            }

            public String getColumn() {
                return this.columnValue;
            }

            public int getColumnIndex() {
                return this.columnIndex;
            }
        }
    }

    public static final synchronized ServerCertsStorage ad(Context context) {
        ServerCertsStorage serverCertsStorage;
        synchronized (ServerCertsStorage.class) {
            if (Ce == null) {
                Ce = new ServerCertsStorage(context);
            }
            serverCertsStorage = Ce;
        }
        return serverCertsStorage;
    }

    private static CertificateInfo m1218h(Cursor cursor) {
        CertificateInfo certificateInfo = new CertificateInfo();
        certificateInfo.setContent(cursor.getString(ServerCertsColumn.CONTENT.getColumnIndex()));
        certificateInfo.setUsage(cursor.getString(ServerCertsColumn.USAGE.getColumnIndex()));
        certificateInfo.setAlias(cursor.getString(ServerCertsColumn.ALIAS.getColumnIndex()));
        return certificateInfo;
    }

    private ServerCertsStorage(Context context) {
        super(context);
        execSQL("CREATE TABLE IF NOT EXISTS server_certs_group(_id INTEGER, card_type TEXT NOT NULL, content TEXT NOT NULL, usage TEXT NOT NULL, alias TEXT NOT NULL, PRIMARY KEY (card_type, usage, alias) )");
        Log.m287i("ServerCertsStorage", "Create ServerCerts Table If Not Exists");
    }

    public List<CertificateInfo> m1220a(ServerCertsColumn serverCertsColumn, String str) {
        Throwable th;
        List<CertificateInfo> list = null;
        if (serverCertsColumn == null) {
            Log.m286e("ServerCertsStorage", "getCertificates: column is null");
        } else {
            Cursor f;
            try {
                f = m1114f("server_certs_group", serverCertsColumn.getColumn(), str);
                if (f != null) {
                    try {
                        if (f.getCount() > 0) {
                            list = new ArrayList(f.getCount());
                            while (f.moveToNext()) {
                                list.add(m1218h(f));
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        DbAdapter.m1106a(f);
                        throw th;
                    }
                }
                DbAdapter.m1106a(f);
            } catch (Throwable th3) {
                Throwable th4 = th3;
                f = null;
                th = th4;
                DbAdapter.m1106a(f);
                throw th;
            }
        }
        return list;
    }

    public int m1219a(String str, CertificateInfo certificateInfo) {
        if (str == null || certificateInfo == null || certificateInfo.getContent() == null || certificateInfo.getUsage() == null) {
            Log.m286e("ServerCertsStorage", "addServerCertsRecord: cardType/cert/cert.content/cert.usage is null");
            return -1;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(ServerCertsColumn.CARD_TYPE.getColumn(), str);
        contentValues.put(ServerCertsColumn.CONTENT.getColumn(), certificateInfo.getContent());
        contentValues.put(ServerCertsColumn.USAGE.getColumn(), certificateInfo.getUsage());
        contentValues.put(ServerCertsColumn.ALIAS.getColumn(), certificateInfo.getAlias());
        try {
            int a = m1107a("server_certs_group", contentValues);
            Log.m285d("ServerCertsStorage", "RowId : " + a);
            return a;
        } catch (Throwable e) {
            Log.m286e("ServerCertsStorage", "addTokenRecord: cannot add server certs record");
            Log.m284c("ServerCertsStorage", e.getMessage(), e);
            return -1;
        }
    }
}
