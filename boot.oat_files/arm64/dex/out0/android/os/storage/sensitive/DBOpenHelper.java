package android.os.storage.sensitive;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    public static final String COLUMN_CLIENT = "client_id";
    public static final String COLUMN_EDEK = "dek";
    public static final String COLUMN_FLAGS = "flags";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_UUID = "uuid";
    private static final String CREATE_CLIENTS = "CREATE TABLE IF NOT EXISTS clients(_id  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name TEXT NOT NULL UNIQUE);";
    private static final String CREATE_DATA = "CREATE TABLE IF NOT EXISTS data(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, uuid TEXT NOT NULL UNIQUE, flags INTEGER NOT NULL, client_id INTEGER NOT NULL REFERENCES clients (_id), dek BLOB NOT NULL);";
    private static final String DATABASE_NAME = "ls_data.db";
    private static final int DATABASE_VERSION = 1;
    private static final boolean DEBUG = false;
    public static final String TABLE_CLIENTS = "clients";
    public static final String TABLE_DATA = "data";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase arg0) {
        arg0.execSQL(CREATE_CLIENTS);
        arg0.execSQL(CREATE_DATA);
    }

    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }
}
