package android.os.storage.sensitive;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.net.ProxyInfo;
import android.util.Log;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class SensitiveDataProvider {
    public static final int DB_READ = 1;
    public static final int DB_WRITE = 2;
    private static final boolean DEBUG = false;
    private static final int FLAG_SIZE = 4;
    private String TAG = "LSManager.SensitiveDataProvider";
    private String client_columns = "name";
    private SQLiteDatabase database;
    private DBOpenHelper dbHelper = null;
    private String insert_columns = "uuid, flags, client_id, dek";
    private Context mCtx = null;
    private String read_columns = "flags, dek";

    private void debugLog(String LogString) {
    }

    public SensitiveDataProvider(Context context) {
        debugLog("Constructing SensitiveDataProvider...");
        if (context == null) {
            debugLog("context is null in SensitiveDataProvider!!!... =/");
        }
        this.dbHelper = new DBOpenHelper(context);
        if (this.dbHelper == null) {
            debugLog("dbHelper is NULL.");
        }
        this.mCtx = context;
        if (this.mCtx == null) {
            debugLog("mCtx is NULL!!!... =/");
        }
    }

    public void open(int mode) throws SQLException {
        debugLog("open with mode: " + mode);
        switch (mode) {
            case 1:
                this.database = this.dbHelper.getReadableDatabase();
                break;
            case 2:
                this.database = this.dbHelper.getWritableDatabase();
                break;
        }
        if (!this.database.isOpen()) {
            debugLog("database is not opened...");
            debugLog("BOOM is expected, so humbly leaving...");
        } else if (!this.database.isReadOnly()) {
            this.database.execSQL("PRAGMA foreign_keys = ON;");
        }
    }

    public void close() {
        debugLog("close");
        this.dbHelper.close();
    }

    public byte[] get_keys_by_flag(long flags) {
        debugLog("get_keys_by_flag");
        if (this.database.isOpen()) {
            Cursor cursor = this.database.rawQuery("SELECT uuid,dek FROM data WHERE flags=" + flags, null);
            if (cursor.moveToFirst()) {
                ByteBuffer bb = ByteBuffer.allocate((cursor.getCount() * ((cursor.getString(0).length() + cursor.getBlob(1).length) + 12)) + 32);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                bb.putInt(cursor.getCount());
                do {
                    bb.putInt(cursor.getString(0).getBytes().length);
                    bb.put(cursor.getString(0).getBytes());
                    bb.putInt(cursor.getBlob(1).length - 4);
                    bb.put(cursor.getBlob(1));
                } while (cursor.moveToNext());
                cursor.close();
                return bb.array();
            }
            debugLog("cursor error");
            cursor.close();
            return new byte[0];
        }
        debugLog("database is not opened...");
        return null;
    }

    public byte[] get_keys_by_flag_and_uuid(long flags, String id) {
        debugLog("get_keys_by_flag_and_uuid");
        if (this.database.isOpen()) {
            Cursor cursor = this.database.rawQuery("SELECT uuid,dek FROM data WHERE flags=" + flags + " AND " + DBOpenHelper.COLUMN_UUID + "=\"" + id + "\"", null);
            if (cursor.moveToFirst()) {
                ByteBuffer bb = ByteBuffer.allocate((cursor.getCount() * ((cursor.getString(0).length() + cursor.getBlob(1).length) + 12)) + 32);
                bb.order(ByteOrder.LITTLE_ENDIAN);
                bb.putInt(cursor.getCount());
                do {
                    bb.putInt(cursor.getString(0).getBytes().length);
                    bb.put(cursor.getString(0).getBytes());
                    bb.putInt(cursor.getBlob(1).length - 4);
                    bb.put(cursor.getBlob(1));
                } while (cursor.moveToNext());
                cursor.close();
                return bb.array();
            }
            debugLog("cursor error");
            cursor.close();
            return new byte[0];
        }
        debugLog("database is not opened...");
        return null;
    }

    public void update_keys(byte[] keys) {
        debugLog("update_keys");
        if (this.database.isOpen()) {
            ByteBuffer bb = ByteBuffer.wrap(keys);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            int cntr = bb.getInt();
            debugLog("Count of keys: " + Integer.toString(cntr));
            do {
                byte[] is = new byte[bb.getInt()];
                debugLog("ID len: " + Integer.toString(is.length));
                bb.get(is);
                byte[] kd = new byte[(bb.getInt() + 4)];
                debugLog("Key len: " + Integer.toString(kd.length));
                bb.get(kd);
                String stmt_str = "UPDATE data SET dek=?, flags=0 WHERE uuid=\"" + new String(is) + "\"";
                debugLog("Update statement " + stmt_str);
                SQLiteStatement stmt = this.database.compileStatement(stmt_str);
                stmt.bindBlob(1, kd);
                if (stmt.executeUpdateDelete() <= 0) {
                    Log.e(this.TAG, "Update failed.");
                }
                cntr--;
            } while (cntr > 0);
            return;
        }
        debugLog("database is not opened...");
    }

    public byte[] read(String client, String id) {
        debugLog("read");
        if (this.database.isOpen()) {
            byte[] key = null;
            Cursor cursor = this.database.rawQuery("SELECT _id FROM clients WHERE name=\"" + client.replace(".", ProxyInfo.LOCAL_EXCL_LIST) + "\"", null);
            if (cursor.moveToFirst()) {
                long row = (long) cursor.getInt(0);
                cursor.close();
                cursor = this.database.rawQuery("SELECT " + this.read_columns + " FROM " + "data" + " WHERE " + DBOpenHelper.COLUMN_UUID + "=\"" + id + "\" AND " + DBOpenHelper.COLUMN_CLIENT + "=" + Long.toString(row), null);
                if (cursor.moveToFirst()) {
                    long flags = cursor.getLong(0);
                    key = cursor.getBlob(1);
                } else {
                    Log.e(this.TAG, "Record not found.");
                }
                cursor.close();
                return key;
            }
            Log.w(this.TAG, "Client not found.");
            cursor.close();
            return null;
        }
        debugLog("database is not opened...");
        return null;
    }

    public boolean write(String client, String id, long flags, byte[] key) {
        debugLog("write");
        if (!this.database.isOpen()) {
            debugLog("database is not opened...");
            return false;
        } else if (this.database.isReadOnly()) {
            debugLog("database is read only...");
            return false;
        } else {
            SQLiteStatement insStm;
            long row = 0;
            Cursor cursor = this.database.rawQuery("SELECT _id FROM clients WHERE name=\"" + client.replace(".", ProxyInfo.LOCAL_EXCL_LIST) + "\"", null);
            if (cursor.moveToFirst()) {
                row = (long) cursor.getInt(0);
                debugLog("Client already exists.");
                cursor.close();
            } else {
                cursor.close();
                insStm = this.database.compileStatement("INSERT into clients(" + this.client_columns + ") VALUES(?);");
                insStm.bindString(1, client.replace(".", ProxyInfo.LOCAL_EXCL_LIST));
                try {
                    row = insStm.executeInsert();
                    insStm.clearBindings();
                    insStm.close();
                } catch (SQLiteException e) {
                    e.printStackTrace();
                    Log.e(this.TAG, "Client add error.");
                }
                if (row < 0) {
                    debugLog("row error");
                    return false;
                }
            }
            insStm = this.database.compileStatement("INSERT into data(" + this.insert_columns + ") VALUES(?,?,?,?);");
            insStm.bindString(1, id);
            insStm.bindLong(2, flags);
            insStm.bindLong(3, row);
            insStm.bindBlob(4, key);
            try {
                if (insStm.executeInsert() >= 0) {
                    return true;
                }
                Log.e(this.TAG, "Client add error.");
                return false;
            } catch (SQLiteException e2) {
                Log.e(this.TAG, "Record add error.");
                e2.printStackTrace();
                return false;
            }
        }
    }

    public boolean delete(String client, String id) {
        debugLog("delete");
        if (!this.database.isOpen() || this.database.isReadOnly()) {
            return false;
        }
        Cursor cursor = this.database.rawQuery("SELECT _id FROM clients WHERE name=\"" + client.replace(".", ProxyInfo.LOCAL_EXCL_LIST) + "\"", null);
        if (cursor.moveToFirst()) {
            long row = (long) cursor.getInt(0);
            cursor.close();
            row = (long) this.database.compileStatement("DELETE FROM data WHERE uuid=\"" + id + "\" AND " + DBOpenHelper.COLUMN_CLIENT + "=" + Long.toString(row) + ";").executeUpdateDelete();
            debugLog("Affected " + Long.toString(row));
            if (row > 0) {
                return true;
            }
            return false;
        }
        Log.w(this.TAG, "Client not found.");
        cursor.close();
        return false;
    }

    public void drop() {
        debugLog("drop");
        this.mCtx.deleteDatabase(this.dbHelper.getDatabaseName());
    }
}
