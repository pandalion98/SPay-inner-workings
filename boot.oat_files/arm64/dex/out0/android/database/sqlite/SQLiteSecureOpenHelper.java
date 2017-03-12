package android.database.sqlite;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultSecureDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;
import java.io.File;

public abstract class SQLiteSecureOpenHelper {
    private static final boolean DEBUG_STRICT_READONLY = false;
    private static final String TAG = SQLiteSecureOpenHelper.class.getSimpleName();
    private final Context mContext;
    private SQLiteDatabase mDatabase;
    private boolean mEnableWriteAheadLogging;
    private final DatabaseErrorHandler mErrorHandler;
    private final CursorFactory mFactory;
    private boolean mIsInitializing;
    private final String mName;
    private final int mNewVersion;

    public abstract void onCreate(SQLiteDatabase sQLiteDatabase);

    public abstract void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2);

    public SQLiteSecureOpenHelper(Context context, String name, CursorFactory factory, int version) {
        this(context, name, factory, version, new DefaultSecureDatabaseErrorHandler());
    }

    public SQLiteSecureOpenHelper(Context context, String name, CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        if (version < 1) {
            throw new IllegalArgumentException("Version must be >= 1, was " + version);
        }
        this.mContext = context;
        this.mName = name;
        this.mFactory = factory;
        this.mNewVersion = version;
        this.mErrorHandler = errorHandler;
    }

    public String getDatabaseName() {
        return this.mName;
    }

    public void setWriteAheadLoggingEnabled(boolean enabled) {
        synchronized (this) {
            if (this.mEnableWriteAheadLogging != enabled) {
                if (!(this.mDatabase == null || !this.mDatabase.isOpen() || this.mDatabase.isReadOnly())) {
                    if (enabled) {
                        this.mDatabase.enableWriteAheadLogging();
                    } else {
                        this.mDatabase.disableWriteAheadLogging();
                    }
                }
                this.mEnableWriteAheadLogging = enabled;
            }
        }
    }

    public SQLiteDatabase getWritableDatabase() {
        SQLiteDatabase databaseLocked;
        synchronized (this) {
            databaseLocked = getDatabaseLocked(true);
        }
        return databaseLocked;
    }

    public SQLiteDatabase getReadableDatabase() {
        SQLiteDatabase databaseLocked;
        synchronized (this) {
            databaseLocked = getDatabaseLocked(false);
        }
        return databaseLocked;
    }

    private SQLiteDatabase getDatabaseLocked(boolean writable) {
        if (this.mDatabase != null) {
            if (!this.mDatabase.isOpen()) {
                this.mDatabase = null;
            } else if (!(writable && this.mDatabase.isReadOnly())) {
                return this.mDatabase;
            }
        }
        if (this.mIsInitializing) {
            throw new IllegalStateException("getDatabase called recursively");
        }
        SQLiteDatabase db = this.mDatabase;
        try {
            this.mIsInitializing = true;
            if (db != null) {
                if (writable && db.isReadOnly()) {
                    db.reopenReadWrite();
                }
            } else if (this.mName == null) {
                db = SQLiteDatabase.create(null);
            } else {
                db = this.mContext.openOrCreateDatabase(this.mName, this.mEnableWriteAheadLogging ? 8 : 0, this.mFactory, this.mErrorHandler);
            }
        } catch (SQLiteException ex) {
            if (writable) {
                throw ex;
            }
            Log.e(TAG, "Couldn't open " + this.mName + " for writing (will try read-only):", ex);
            db = SQLiteDatabase.openDatabase(this.mContext.getDatabasePath(this.mName).getPath(), this.mFactory, 1, this.mErrorHandler);
        } catch (Throwable th) {
            this.mIsInitializing = false;
            if (!(db == null || db == this.mDatabase)) {
                db.close();
            }
        }
        onConfigure(db);
        if (db != null) {
            int version = db.getVersion();
            if (version != this.mNewVersion) {
                if (db.isReadOnly()) {
                    throw new SQLiteException("Can't upgrade read-only database from version " + db.getVersion() + " to " + this.mNewVersion + ": " + this.mName);
                }
                db.beginTransaction();
                if (version == 0) {
                    onCreate(db);
                } else if (version > this.mNewVersion) {
                    onDowngrade(db, version, this.mNewVersion);
                } else {
                    onUpgrade(db, version, this.mNewVersion);
                }
                db.setVersion(this.mNewVersion);
                db.setTransactionSuccessful();
                db.endTransaction();
            }
            onOpen(db);
            if (db.isReadOnly()) {
                Log.w(TAG, "Opened " + this.mName + " in read-only mode");
            }
        }
        this.mDatabase = db;
        this.mIsInitializing = false;
        if (db == null || db == this.mDatabase) {
            return db;
        }
        db.close();
        return db;
    }

    public SQLiteDatabase getWritableDatabase(byte[] password) {
        SQLiteDatabase databaseLocked;
        synchronized (this) {
            databaseLocked = getDatabaseLocked(true, true, password);
        }
        return databaseLocked;
    }

    public SQLiteDatabase getReadableDatabase(byte[] password) {
        SQLiteDatabase databaseLocked;
        synchronized (this) {
            databaseLocked = getDatabaseLocked(false, true, password);
        }
        return databaseLocked;
    }

    private SQLiteDatabase getDatabaseLocked(boolean writable, boolean secure, byte[] password) {
        int flags = 8;
        if (this.mDatabase != null) {
            if (!this.mDatabase.isOpen()) {
                this.mDatabase = null;
            } else if (!(writable && this.mDatabase.isReadOnly())) {
                return this.mDatabase;
            }
        }
        if (this.mIsInitializing) {
            throw new IllegalStateException("getDatabase called recursively");
        }
        SQLiteDatabase db = this.mDatabase;
        try {
            this.mIsInitializing = true;
            if (db != null) {
                if (writable && db.isReadOnly()) {
                    if (secure) {
                        db.reopenReadWrite();
                        Log.i(TAG, "TODO: Opening  " + this.mName + " in read-write mode");
                    } else {
                        db.reopenReadWrite();
                    }
                }
            } else if (this.mName == null) {
                db = !secure ? SQLiteDatabase.create(null) : SQLiteDatabase.createSecureDatabase(null, password);
            } else if (secure) {
                String path = this.mContext.getDatabasePath(this.mName).getPath();
                if (!this.mEnableWriteAheadLogging) {
                    flags = 0;
                }
                File dbFile = new File(path);
                if (!dbFile.exists()) {
                    Log.i(TAG, "DB Directory does not exist");
                    dbFile.getParentFile().mkdirs();
                }
                db = SQLiteDatabase.openSecureDatabase(path, this.mFactory, 268435456 | flags, this.mErrorHandler, password);
            } else {
                Context context = this.mContext;
                String str = this.mName;
                if (!this.mEnableWriteAheadLogging) {
                    flags = 0;
                }
                db = context.openOrCreateDatabase(str, flags, this.mFactory, this.mErrorHandler);
            }
            onConfigure(db);
            if (db != null) {
                int version = db.getVersion();
                if (version != this.mNewVersion) {
                    if (db.isReadOnly()) {
                        throw new SQLiteException("Can't upgrade read-only database from version " + db.getVersion() + " to " + this.mNewVersion + ": " + this.mName);
                    }
                    db.beginTransaction();
                    if (version == 0) {
                        onCreate(db);
                    } else if (version > this.mNewVersion) {
                        onDowngrade(db, version, this.mNewVersion);
                    } else {
                        onUpgrade(db, version, this.mNewVersion);
                    }
                    db.setVersion(this.mNewVersion);
                    db.setTransactionSuccessful();
                    db.endTransaction();
                }
                if (db.isReadOnly()) {
                    Log.w(TAG, "Opened " + this.mName + " in read-only mode");
                }
            }
            this.mDatabase = db;
            this.mIsInitializing = false;
            if (db == null || db == this.mDatabase) {
                return db;
            }
            db.close();
            return db;
        } catch (SQLiteException ex) {
            throw ex;
        } catch (Throwable th) {
            this.mIsInitializing = false;
            if (!(db == null || db == this.mDatabase)) {
                db.close();
            }
        }
    }

    public synchronized void close() {
        if (this.mIsInitializing) {
            throw new IllegalStateException("Closed during initialization");
        } else if (this.mDatabase != null && this.mDatabase.isOpen()) {
            this.mDatabase.close();
            this.mDatabase = null;
        }
    }

    public void onConfigure(SQLiteDatabase db) {
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        throw new SQLiteException("Can't downgrade database from version " + oldVersion + " to " + newVersion);
    }

    public void onOpen(SQLiteDatabase db) {
    }

    public static final void convert2PlainDB(File sourceDbFile, byte[] password, File destDbFile) throws Exception {
        SQLiteDatabase.convert2PlainDB(sourceDbFile, password, destDbFile);
    }

    public static final void convert2SecureDB(File sourceDbFile, File destDbFile, byte[] password) throws Exception {
        SQLiteDatabase.convert2SecureDB(sourceDbFile, destDbFile, password);
    }

    public static final int changeDBPassword(SQLiteDatabase db, byte[] newPassword) {
        return db.changeDBPassword(newPassword);
    }
}
