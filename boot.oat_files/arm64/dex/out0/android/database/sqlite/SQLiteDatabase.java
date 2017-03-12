package android.database.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.DefaultDatabaseErrorHandler;
import android.database.DefaultSecureDatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDebug.DbStats;
import android.net.ProxyInfo;
import android.os.CancellationSignal;
import android.os.Looper;
import android.text.TextUtils;
import android.util.EventLog;
import android.util.Log;
import android.util.Pair;
import android.util.Printer;
import dalvik.system.CloseGuard;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

public final class SQLiteDatabase extends SQLiteClosable {
    static final /* synthetic */ boolean $assertionsDisabled = (!SQLiteDatabase.class.desiredAssertionStatus());
    public static final int CONFLICT_ABORT = 2;
    public static final int CONFLICT_FAIL = 3;
    public static final int CONFLICT_IGNORE = 4;
    public static final int CONFLICT_NONE = 0;
    public static final int CONFLICT_REPLACE = 5;
    public static final int CONFLICT_ROLLBACK = 1;
    private static final String[] CONFLICT_VALUES = new String[]{ProxyInfo.LOCAL_EXCL_LIST, " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};
    public static final int CREATE_IF_NECESSARY = 268435456;
    public static final int ENABLE_WRITE_AHEAD_LOGGING = 536870912;
    private static final int EVENT_DB_CORRUPT = 75004;
    public static final int MAX_SQL_CACHE_SIZE = 100;
    public static final int NO_LOCALIZED_COLLATORS = 16;
    public static final int OPEN_READONLY = 1;
    public static final int OPEN_READWRITE = 0;
    private static final int OPEN_READ_MASK = 1;
    public static final int SQLITE_MAX_LIKE_PATTERN_LENGTH = 50000;
    private static final String TAG = "SQLiteDatabase";
    private static WeakHashMap<SQLiteDatabase, Object> sActiveDatabases = new WeakHashMap();
    private final CloseGuard mCloseGuardLocked = CloseGuard.get();
    private final SQLiteDatabaseConfiguration mConfigurationLocked;
    private SQLiteConnectionPool mConnectionPoolLocked;
    private int mCorrupt_code = 0;
    private final CursorFactory mCursorFactory;
    private final DatabaseErrorHandler mErrorHandler;
    private boolean mHasAttachedDbsLocked;
    private String mIntegrityErrorString = null;
    private final Object mLock = new Object();
    private final ThreadLocal<SQLiteSession> mThreadSession = new ThreadLocal<SQLiteSession>() {
        protected SQLiteSession initialValue() {
            return SQLiteDatabase.this.createSession();
        }
    };

    public interface CursorFactory {
        Cursor newCursor(SQLiteDatabase sQLiteDatabase, SQLiteCursorDriver sQLiteCursorDriver, String str, SQLiteQuery sQLiteQuery);
    }

    public interface CustomFunction {
        void callback(String[] strArr);
    }

    private SQLiteDatabase(String path, int openFlags, CursorFactory cursorFactory, DatabaseErrorHandler errorHandler) {
        this.mCursorFactory = cursorFactory;
        if (errorHandler == null) {
            errorHandler = new DefaultDatabaseErrorHandler();
        }
        this.mErrorHandler = errorHandler;
        this.mConfigurationLocked = new SQLiteDatabaseConfiguration(path, openFlags);
    }

    protected void finalize() throws Throwable {
        try {
            dispose(true);
        } finally {
            super.finalize();
        }
    }

    protected void onAllReferencesReleased() {
        dispose(false);
    }

    private void dispose(boolean finalized) {
        synchronized (this.mLock) {
            if (this.mCloseGuardLocked != null) {
                if (finalized) {
                    this.mCloseGuardLocked.warnIfOpen();
                }
                this.mCloseGuardLocked.close();
            }
            SQLiteConnectionPool pool = this.mConnectionPoolLocked;
            this.mConnectionPoolLocked = null;
        }
        if (!finalized) {
            synchronized (sActiveDatabases) {
                sActiveDatabases.remove(this);
            }
            if (pool != null) {
                pool.close();
            }
        }
    }

    public static int releaseMemory() {
        return SQLiteGlobal.releaseMemory();
    }

    @Deprecated
    public void setLockingEnabled(boolean lockingEnabled) {
    }

    String getLabel() {
        String str;
        synchronized (this.mLock) {
            str = this.mConfigurationLocked.label;
        }
        return str;
    }

    void onCorruption() {
        EventLog.writeEvent(EVENT_DB_CORRUPT, getLabel());
        this.mErrorHandler.onCorruption(this);
    }

    SQLiteSession getThreadSession() {
        return (SQLiteSession) this.mThreadSession.get();
    }

    SQLiteSession createSession() {
        SQLiteConnectionPool pool;
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            pool = this.mConnectionPoolLocked;
        }
        return new SQLiteSession(pool);
    }

    int getThreadDefaultConnectionFlags(boolean readOnly) {
        int flags = readOnly ? 1 : 2;
        if (isMainThread()) {
            return flags | 4;
        }
        return flags;
    }

    private static boolean isMainThread() {
        Looper looper = Looper.myLooper();
        return looper != null && looper == Looper.getMainLooper();
    }

    public void beginTransaction() {
        beginTransaction(null, true);
    }

    public void beginTransactionNonExclusive() {
        beginTransaction(null, false);
    }

    public void beginTransactionWithListener(SQLiteTransactionListener transactionListener) {
        beginTransaction(transactionListener, true);
    }

    public void beginTransactionWithListenerNonExclusive(SQLiteTransactionListener transactionListener) {
        beginTransaction(transactionListener, false);
    }

    private void beginTransaction(SQLiteTransactionListener transactionListener, boolean exclusive) {
        acquireReference();
        try {
            getThreadSession().beginTransaction(exclusive ? 2 : 1, transactionListener, getThreadDefaultConnectionFlags(false), null);
        } finally {
            releaseReference();
        }
    }

    public void endTransaction() {
        acquireReference();
        try {
            getThreadSession().endTransaction(null);
        } finally {
            releaseReference();
        }
    }

    public void setTransactionSuccessful() {
        acquireReference();
        try {
            getThreadSession().setTransactionSuccessful();
        } finally {
            releaseReference();
        }
    }

    public boolean inTransaction() {
        acquireReference();
        try {
            boolean hasTransaction = getThreadSession().hasTransaction();
            return hasTransaction;
        } finally {
            releaseReference();
        }
    }

    public boolean isDbLockedByCurrentThread() {
        acquireReference();
        try {
            boolean hasConnection = getThreadSession().hasConnection();
            return hasConnection;
        } finally {
            releaseReference();
        }
    }

    @Deprecated
    public boolean isDbLockedByOtherThreads() {
        return false;
    }

    @Deprecated
    public boolean yieldIfContended() {
        return yieldIfContendedHelper(false, -1);
    }

    public boolean yieldIfContendedSafely() {
        return yieldIfContendedHelper(true, -1);
    }

    public boolean yieldIfContendedSafely(long sleepAfterYieldDelay) {
        return yieldIfContendedHelper(true, sleepAfterYieldDelay);
    }

    private boolean yieldIfContendedHelper(boolean throwIfUnsafe, long sleepAfterYieldDelay) {
        acquireReference();
        try {
            boolean yieldTransaction = getThreadSession().yieldTransaction(sleepAfterYieldDelay, throwIfUnsafe, null);
            return yieldTransaction;
        } finally {
            releaseReference();
        }
    }

    @Deprecated
    public Map<String, String> getSyncedTables() {
        return new HashMap(0);
    }

    public static SQLiteDatabase openDatabase(String path, CursorFactory factory, int flags) {
        return openDatabase(path, factory, flags, null);
    }

    public static SQLiteDatabase openDatabase(String path, CursorFactory factory, int flags, DatabaseErrorHandler errorHandler) {
        SQLiteDatabase db = new SQLiteDatabase(path, flags, factory, errorHandler);
        db.open();
        return db;
    }

    public static SQLiteDatabase openOrCreateDatabase(File file, CursorFactory factory) {
        return openOrCreateDatabase(file.getPath(), factory);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, CursorFactory factory) {
        return openDatabase(path, factory, 268435456, null);
    }

    public static SQLiteDatabase openOrCreateDatabase(String path, CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return openDatabase(path, factory, 268435456, errorHandler);
    }

    public static SQLiteDatabase openSecureDatabase(String path, CursorFactory factory, int flags, DatabaseErrorHandler errorHandler, byte[] password) {
        SQLiteDatabase db = new SQLiteDatabase(path, flags, factory, errorHandler);
        db.openSecureDatabase(password);
        return db;
    }

    private static String convertByte2HexString(byte[] password) {
        String rst = ProxyInfo.LOCAL_EXCL_LIST;
        if (password != null) {
            int i = 0;
            while (i < password.length) {
                if (password[i] >= (byte) 0 && password[i] < (byte) 16) {
                    rst = rst + "0";
                }
                rst = rst + Integer.toHexString(password[i] & 255);
                i++;
            }
        }
        return rst;
    }

    public static void convert2PlainDB(File sourceDbFile, byte[] password, File destDbFile) throws Exception {
        SQLiteDatabase sourceDb = openSecureDatabase(sourceDbFile.getAbsolutePath(), null, 0, new DefaultDatabaseErrorHandler(), password);
        if (sourceDb != null) {
            int sourceDbVer = sourceDb.getVersion();
            sourceDb.close();
            SQLiteDatabase destDb = openDatabase(destDbFile.getAbsolutePath(), null, 268435456, new DefaultDatabaseErrorHandler());
            destDb.execSQL(String.format("attach database '%s' as secureDb key x'%s'", new Object[]{sourceDbFile.getAbsolutePath(), convertByte2HexString(password)}));
            destDb.exportDB("secureDb");
            destDb.execSQL("detach database secureDb");
            destDb.setVersion(sourceDbVer);
            destDb.close();
            return;
        }
        throw new IllegalStateException("Source DB not found");
    }

    public static void convert2SecureDB(File sourceDbFile, File destDbFile, byte[] password) throws Exception {
        SQLiteDatabase sourceDb = openDatabase(sourceDbFile.getAbsolutePath(), null, 0, new DefaultDatabaseErrorHandler());
        if (sourceDb != null) {
            int sourceDbVer = sourceDb.getVersion();
            sourceDb.close();
            SQLiteDatabase destDb = openSecureDatabase(destDbFile.getAbsolutePath(), null, 268435456, new DefaultDatabaseErrorHandler(), password);
            destDb.execSQL(String.format("attach database '%s' as plainDb key ''", new Object[]{sourceDbFile.getAbsolutePath()}));
            destDb.exportDB("plainDb");
            destDb.execSQL("detach database plainDb");
            destDb.setVersion(sourceDbVer);
            destDb.close();
            return;
        }
        throw new IllegalStateException("Source DB not found");
    }

    public int changeDBPassword(byte[] newPassword) {
        if (isOpen()) {
            getThreadSession().changePassword(newPassword);
            return 0;
        }
        throw new IllegalStateException("DB is not open");
    }

    public static boolean deleteDatabase(File file) {
        if (file == null) {
            throw new IllegalArgumentException("file must not be null");
        }
        boolean deleted = (((false | file.delete()) | new File(file.getPath() + "-journal").delete()) | new File(file.getPath() + "-shm").delete()) | new File(file.getPath() + "-wal").delete();
        File dir = file.getParentFile();
        if (dir != null) {
            final String prefix = file.getName() + "-mj";
            File[] files = dir.listFiles(new FileFilter() {
                public boolean accept(File candidate) {
                    return candidate.getName().startsWith(prefix);
                }
            });
            if (files != null) {
                for (File masterJournal : files) {
                    deleted |= masterJournal.delete();
                }
            }
        }
        return deleted;
    }

    public void reopenReadWrite() {
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            if (isReadOnlyLocked()) {
                int oldOpenFlags = this.mConfigurationLocked.openFlags;
                this.mConfigurationLocked.openFlags = (this.mConfigurationLocked.openFlags & -2) | 0;
                try {
                    this.mConnectionPoolLocked.reconfigure(this.mConfigurationLocked);
                    return;
                } catch (RuntimeException ex) {
                    this.mConfigurationLocked.openFlags = oldOpenFlags;
                    throw ex;
                }
            }
        }
    }

    private void open() {
        try {
            openInner();
        } catch (SQLiteDatabaseCorruptException ex) {
            try {
                onCorruption(ex.getCorruptCode());
                openInner();
            } catch (SQLiteException ex2) {
                Log.e(TAG, "Failed to open database '" + getLabel() + "'.", ex2);
                close();
                throw ex2;
            }
        }
    }

    private void openSecureDatabase(byte[] password) {
        try {
            openInnerSecureDatabase(password);
        } catch (SQLiteDatabaseCorruptException ex) {
            try {
                onCorruption(ex.getCorruptCode());
                openInnerSecureDatabase(password);
            } catch (SQLiteException ex2) {
                Log.e(TAG, "Failed to open database '" + getLabel() + "'.", ex2);
                close();
                throw ex2;
            }
        }
    }

    private void openInner() {
        synchronized (this.mLock) {
            if ($assertionsDisabled || this.mConnectionPoolLocked == null) {
                this.mConnectionPoolLocked = SQLiteConnectionPool.open(this.mConfigurationLocked);
                this.mCloseGuardLocked.open("close");
            } else {
                throw new AssertionError();
            }
        }
        synchronized (sActiveDatabases) {
            sActiveDatabases.put(this, null);
        }
    }

    private void openInnerSecureDatabase(byte[] password) {
        synchronized (this.mLock) {
            if ($assertionsDisabled || this.mConnectionPoolLocked == null) {
                this.mConnectionPoolLocked = SQLiteConnectionPool.openSecure(this.mConfigurationLocked, password);
                this.mCloseGuardLocked.open("close");
            } else {
                throw new AssertionError();
            }
        }
        synchronized (sActiveDatabases) {
            sActiveDatabases.put(this, null);
        }
    }

    private void exportDB(String attachedDB) {
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            this.mConnectionPoolLocked.exportDB(attachedDB);
        }
    }

    public static SQLiteDatabase create(CursorFactory factory) {
        return openDatabase(SQLiteDatabaseConfiguration.MEMORY_DB_PATH, factory, 268435456);
    }

    public static SQLiteDatabase createSecureDatabase(CursorFactory factory, byte[] password) {
        return openSecureDatabase(SQLiteDatabaseConfiguration.MEMORY_DB_PATH, factory, 268435456, new DefaultSecureDatabaseErrorHandler(), password);
    }

    public void addCustomFunction(String name, int numArgs, CustomFunction function) {
        SQLiteCustomFunction wrapper = new SQLiteCustomFunction(name, numArgs, function);
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            this.mConfigurationLocked.customFunctions.add(wrapper);
            try {
                this.mConnectionPoolLocked.reconfigure(this.mConfigurationLocked);
            } catch (RuntimeException ex) {
                this.mConfigurationLocked.customFunctions.remove(wrapper);
                throw ex;
            }
        }
    }

    public int getVersion() {
        return Long.valueOf(DatabaseUtils.longForQuery(this, "PRAGMA user_version;", null)).intValue();
    }

    public void setVersion(int version) {
        execSQL("PRAGMA user_version = " + version);
    }

    public long getMaximumSize() {
        return getPageSize() * DatabaseUtils.longForQuery(this, "PRAGMA max_page_count;", null);
    }

    public long setMaximumSize(long numBytes) {
        long pageSize = getPageSize();
        long numPages = numBytes / pageSize;
        if (numBytes % pageSize != 0) {
            numPages++;
        }
        return DatabaseUtils.longForQuery(this, "PRAGMA max_page_count = " + numPages, null) * pageSize;
    }

    public long getPageSize() {
        return DatabaseUtils.longForQuery(this, "PRAGMA page_size;", null);
    }

    public void setPageSize(long numBytes) {
        execSQL("PRAGMA page_size = " + numBytes);
    }

    @Deprecated
    public void markTableSyncable(String table, String deletedTable) {
    }

    @Deprecated
    public void markTableSyncable(String table, String foreignKey, String updateTable) {
    }

    public static String findEditTable(String tables) {
        if (TextUtils.isEmpty(tables)) {
            throw new IllegalStateException("Invalid tables");
        }
        int spacepos = tables.indexOf(32);
        int commapos = tables.indexOf(44);
        if (spacepos > 0 && (spacepos < commapos || commapos < 0)) {
            return tables.substring(0, spacepos);
        }
        if (commapos <= 0) {
            return tables;
        }
        if (commapos < spacepos || spacepos < 0) {
            return tables.substring(0, commapos);
        }
        return tables;
    }

    public SQLiteStatement compileStatement(String sql) throws SQLException {
        acquireReference();
        try {
            SQLiteStatement sQLiteStatement = new SQLiteStatement(this, sql, null);
            return sQLiteStatement;
        } finally {
            releaseReference();
        }
    }

    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return queryWithFactory(null, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, null);
    }

    public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, CancellationSignal cancellationSignal) {
        return queryWithFactory(null, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, cancellationSignal);
    }

    public Cursor queryWithFactory(CursorFactory cursorFactory, boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return queryWithFactory(cursorFactory, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit, null);
    }

    public Cursor queryWithFactory(CursorFactory cursorFactory, boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit, CancellationSignal cancellationSignal) {
        acquireReference();
        try {
            Cursor rawQueryWithFactory = rawQueryWithFactory(cursorFactory, SQLiteQueryBuilder.buildQueryString(distinct, table, columns, selection, groupBy, having, orderBy, limit), selectionArgs, findEditTable(table), cancellationSignal);
            return rawQueryWithFactory;
        } finally {
            releaseReference();
        }
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return query(false, table, columns, selection, selectionArgs, groupBy, having, orderBy, null);
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
        return query(false, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return rawQueryWithFactory(null, sql, selectionArgs, null, null);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs, CancellationSignal cancellationSignal) {
        return rawQueryWithFactory(null, sql, selectionArgs, null, cancellationSignal);
    }

    public Cursor rawQueryWithFactory(CursorFactory cursorFactory, String sql, String[] selectionArgs, String editTable) {
        return rawQueryWithFactory(cursorFactory, sql, selectionArgs, editTable, null);
    }

    public Cursor rawQueryWithFactory(CursorFactory cursorFactory, String sql, String[] selectionArgs, String editTable, CancellationSignal cancellationSignal) {
        acquireReference();
        try {
            SQLiteCursorDriver driver = new SQLiteDirectCursorDriver(this, sql, editTable, cancellationSignal);
            if (cursorFactory == null) {
                cursorFactory = this.mCursorFactory;
            }
            Cursor query = driver.query(cursorFactory, selectionArgs);
            return query;
        } finally {
            releaseReference();
        }
    }

    public long insert(String table, String nullColumnHack, ContentValues values) {
        try {
            return insertWithOnConflict(table, nullColumnHack, values, 0);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + values, e);
            return -1;
        }
    }

    public long insertOrThrow(String table, String nullColumnHack, ContentValues values) throws SQLException {
        return insertWithOnConflict(table, nullColumnHack, values, 0);
    }

    public long replace(String table, String nullColumnHack, ContentValues initialValues) {
        try {
            return insertWithOnConflict(table, nullColumnHack, initialValues, 5);
        } catch (SQLException e) {
            Log.e(TAG, "Error inserting " + initialValues, e);
            return -1;
        }
    }

    public long replaceOrThrow(String table, String nullColumnHack, ContentValues initialValues) throws SQLException {
        return insertWithOnConflict(table, nullColumnHack, initialValues, 5);
    }

    public long insertWithOnConflict(String table, String nullColumnHack, ContentValues initialValues, int conflictAlgorithm) {
        SQLiteStatement statement;
        acquireReference();
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT");
            sql.append(CONFLICT_VALUES[conflictAlgorithm]);
            sql.append(" INTO ");
            sql.append(table);
            sql.append('(');
            Object[] bindArgs = null;
            int size = (initialValues == null || initialValues.size() <= 0) ? 0 : initialValues.size();
            if (size > 0) {
                int i;
                bindArgs = new Object[size];
                int i2 = 0;
                for (String colName : initialValues.keySet()) {
                    sql.append(i2 > 0 ? "," : ProxyInfo.LOCAL_EXCL_LIST);
                    sql.append(colName);
                    i = i2 + 1;
                    bindArgs[i2] = initialValues.get(colName);
                    i2 = i;
                }
                sql.append(')');
                sql.append(" VALUES (");
                i = 0;
                while (i < size) {
                    sql.append(i > 0 ? ",?" : "?");
                    i++;
                }
            } else {
                sql.append(nullColumnHack + ") VALUES (NULL");
            }
            sql.append(')');
            statement = new SQLiteStatement(this, sql.toString(), bindArgs);
            long executeInsert = statement.executeInsert();
            statement.close();
            releaseReference();
            return executeInsert;
        } catch (Throwable th) {
            releaseReference();
        }
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        acquireReference();
        SQLiteStatement statement;
        try {
            statement = new SQLiteStatement(this, "DELETE FROM " + table + (!TextUtils.isEmpty(whereClause) ? " WHERE " + whereClause : ProxyInfo.LOCAL_EXCL_LIST), whereArgs);
            int executeUpdateDelete = statement.executeUpdateDelete();
            statement.close();
            releaseReference();
            return executeUpdateDelete;
        } catch (Throwable th) {
            releaseReference();
        }
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return updateWithOnConflict(table, values, whereClause, whereArgs, 0);
    }

    public int updateWithOnConflict(String table, ContentValues values, String whereClause, String[] whereArgs, int conflictAlgorithm) {
        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("Empty values");
        }
        acquireReference();
        SQLiteStatement statement;
        try {
            int i;
            StringBuilder sql = new StringBuilder(120);
            sql.append("UPDATE ");
            sql.append(CONFLICT_VALUES[conflictAlgorithm]);
            sql.append(table);
            sql.append(" SET ");
            int setValuesSize = values.size();
            int bindArgsSize = whereArgs == null ? setValuesSize : setValuesSize + whereArgs.length;
            Object[] bindArgs = new Object[bindArgsSize];
            int i2 = 0;
            for (String colName : values.keySet()) {
                sql.append(i2 > 0 ? "," : ProxyInfo.LOCAL_EXCL_LIST);
                sql.append(colName);
                i = i2 + 1;
                bindArgs[i2] = values.get(colName);
                sql.append("=?");
                i2 = i;
            }
            if (whereArgs != null) {
                for (i = setValuesSize; i < bindArgsSize; i++) {
                    bindArgs[i] = whereArgs[i - setValuesSize];
                }
            }
            if (!TextUtils.isEmpty(whereClause)) {
                sql.append(" WHERE ");
                sql.append(whereClause);
            }
            statement = new SQLiteStatement(this, sql.toString(), bindArgs);
            int executeUpdateDelete = statement.executeUpdateDelete();
            statement.close();
            releaseReference();
            return executeUpdateDelete;
        } catch (Throwable th) {
            releaseReference();
        }
    }

    public void execSQL(String sql) throws SQLException {
        executeSql(sql, null);
    }

    public void execSQL(String sql, Object[] bindArgs) throws SQLException {
        if (bindArgs == null) {
            throw new IllegalArgumentException("Empty bindArgs");
        }
        executeSql(sql, bindArgs);
    }

    private int executeSql(String sql, Object[] bindArgs) throws SQLException {
        acquireReference();
        SQLiteStatement statement;
        try {
            if (DatabaseUtils.getSqlStatementType(sql) == 3) {
                boolean disableWal = false;
                synchronized (this.mLock) {
                    if (!this.mHasAttachedDbsLocked) {
                        this.mHasAttachedDbsLocked = true;
                        disableWal = true;
                    }
                }
                if (disableWal) {
                    disableWriteAheadLogging();
                }
            }
            statement = new SQLiteStatement(this, sql, bindArgs);
            int executeUpdateDelete = statement.executeUpdateDelete();
            statement.close();
            releaseReference();
            return executeUpdateDelete;
        } catch (Throwable th) {
            releaseReference();
        }
    }

    public boolean isReadOnly() {
        boolean isReadOnlyLocked;
        synchronized (this.mLock) {
            isReadOnlyLocked = isReadOnlyLocked();
        }
        return isReadOnlyLocked;
    }

    private boolean isReadOnlyLocked() {
        return (this.mConfigurationLocked.openFlags & 1) == 1;
    }

    public boolean isInMemoryDatabase() {
        boolean isInMemoryDb;
        synchronized (this.mLock) {
            isInMemoryDb = this.mConfigurationLocked.isInMemoryDb();
        }
        return isInMemoryDb;
    }

    public boolean isOpen() {
        boolean z;
        synchronized (this.mLock) {
            z = this.mConnectionPoolLocked != null;
        }
        return z;
    }

    public boolean needUpgrade(int newVersion) {
        return newVersion > getVersion();
    }

    public final String getPath() {
        String str;
        synchronized (this.mLock) {
            str = this.mConfigurationLocked.path;
        }
        return str;
    }

    public void setLocale(Locale locale) {
        if (locale == null) {
            throw new IllegalArgumentException("locale must not be null.");
        }
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            Locale oldLocale = this.mConfigurationLocked.locale;
            this.mConfigurationLocked.locale = locale;
            try {
                this.mConnectionPoolLocked.reconfigure(this.mConfigurationLocked);
            } catch (RuntimeException ex) {
                this.mConfigurationLocked.locale = oldLocale;
                throw ex;
            }
        }
    }

    public void setMaxSqlCacheSize(int cacheSize) {
        if (cacheSize > 100 || cacheSize < 0) {
            throw new IllegalStateException("expected value between 0 and 100");
        }
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            int oldMaxSqlCacheSize = this.mConfigurationLocked.maxSqlCacheSize;
            this.mConfigurationLocked.maxSqlCacheSize = cacheSize;
            try {
                this.mConnectionPoolLocked.reconfigure(this.mConfigurationLocked);
            } catch (RuntimeException ex) {
                this.mConfigurationLocked.maxSqlCacheSize = oldMaxSqlCacheSize;
                throw ex;
            }
        }
    }

    public void setForeignKeyConstraintsEnabled(boolean enable) {
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            if (this.mConfigurationLocked.foreignKeyConstraintsEnabled == enable) {
                return;
            }
            this.mConfigurationLocked.foreignKeyConstraintsEnabled = enable;
            try {
                this.mConnectionPoolLocked.reconfigure(this.mConfigurationLocked);
            } catch (RuntimeException ex) {
                this.mConfigurationLocked.foreignKeyConstraintsEnabled = !enable;
                throw ex;
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean enableWriteAheadLogging() {
        /*
        r6 = this;
        r5 = 536870912; // 0x20000000 float:1.0842022E-19 double:2.652494739E-315;
        r1 = 1;
        r2 = 0;
        r3 = r6.mLock;
        monitor-enter(r3);
        r6.throwIfNotOpenLocked();	 Catch:{ all -> 0x0071 }
        r4 = r6.mConfigurationLocked;	 Catch:{ all -> 0x0071 }
        r4 = r4.openFlags;	 Catch:{ all -> 0x0071 }
        r4 = r4 & r5;
        if (r4 == 0) goto L_0x0013;
    L_0x0011:
        monitor-exit(r3);	 Catch:{ all -> 0x0071 }
    L_0x0012:
        return r1;
    L_0x0013:
        r4 = r6.isReadOnlyLocked();	 Catch:{ all -> 0x0071 }
        if (r4 == 0) goto L_0x001c;
    L_0x0019:
        monitor-exit(r3);	 Catch:{ all -> 0x0071 }
        r1 = r2;
        goto L_0x0012;
    L_0x001c:
        r4 = r6.mConfigurationLocked;	 Catch:{ all -> 0x0071 }
        r4 = r4.isInMemoryDb();	 Catch:{ all -> 0x0071 }
        if (r4 == 0) goto L_0x002e;
    L_0x0024:
        r1 = "SQLiteDatabase";
        r4 = "can't enable WAL for memory databases.";
        android.util.Log.i(r1, r4);	 Catch:{ all -> 0x0071 }
        monitor-exit(r3);	 Catch:{ all -> 0x0071 }
        r1 = r2;
        goto L_0x0012;
    L_0x002e:
        r4 = r6.mHasAttachedDbsLocked;	 Catch:{ all -> 0x0071 }
        if (r4 == 0) goto L_0x0061;
    L_0x0032:
        r1 = "SQLiteDatabase";
        r4 = 3;
        r1 = android.util.Log.isLoggable(r1, r4);	 Catch:{ all -> 0x0071 }
        if (r1 == 0) goto L_0x005e;
    L_0x003b:
        r1 = "SQLiteDatabase";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0071 }
        r4.<init>();	 Catch:{ all -> 0x0071 }
        r5 = "this database: ";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0071 }
        r5 = r6.mConfigurationLocked;	 Catch:{ all -> 0x0071 }
        r5 = r5.label;	 Catch:{ all -> 0x0071 }
        r4 = r4.append(r5);	 Catch:{ all -> 0x0071 }
        r5 = " has attached databases. can't  enable WAL.";
        r4 = r4.append(r5);	 Catch:{ all -> 0x0071 }
        r4 = r4.toString();	 Catch:{ all -> 0x0071 }
        android.util.Log.d(r1, r4);	 Catch:{ all -> 0x0071 }
    L_0x005e:
        monitor-exit(r3);	 Catch:{ all -> 0x0071 }
        r1 = r2;
        goto L_0x0012;
    L_0x0061:
        r2 = r6.mConfigurationLocked;	 Catch:{ all -> 0x0071 }
        r4 = r2.openFlags;	 Catch:{ all -> 0x0071 }
        r4 = r4 | r5;
        r2.openFlags = r4;	 Catch:{ all -> 0x0071 }
        r2 = r6.mConnectionPoolLocked;	 Catch:{ RuntimeException -> 0x0074 }
        r4 = r6.mConfigurationLocked;	 Catch:{ RuntimeException -> 0x0074 }
        r2.reconfigure(r4);	 Catch:{ RuntimeException -> 0x0074 }
        monitor-exit(r3);	 Catch:{ all -> 0x0071 }
        goto L_0x0012;
    L_0x0071:
        r1 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0071 }
        throw r1;
    L_0x0074:
        r0 = move-exception;
        r1 = r6.mConfigurationLocked;	 Catch:{ all -> 0x0071 }
        r2 = r1.openFlags;	 Catch:{ all -> 0x0071 }
        r4 = -536870913; // 0xffffffffdfffffff float:-3.6893486E19 double:NaN;
        r2 = r2 & r4;
        r1.openFlags = r2;	 Catch:{ all -> 0x0071 }
        throw r0;	 Catch:{ all -> 0x0071 }
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.sqlite.SQLiteDatabase.enableWriteAheadLogging():boolean");
    }

    public void disableWriteAheadLogging() {
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            if ((this.mConfigurationLocked.openFlags & 536870912) == 0) {
                return;
            }
            SQLiteDatabaseConfiguration sQLiteDatabaseConfiguration = this.mConfigurationLocked;
            sQLiteDatabaseConfiguration.openFlags &= -536870913;
            try {
                this.mConnectionPoolLocked.reconfigure(this.mConfigurationLocked);
            } catch (RuntimeException ex) {
                sQLiteDatabaseConfiguration = this.mConfigurationLocked;
                sQLiteDatabaseConfiguration.openFlags |= 536870912;
                throw ex;
            }
        }
    }

    public boolean isWriteAheadLoggingEnabled() {
        boolean z;
        synchronized (this.mLock) {
            throwIfNotOpenLocked();
            z = (this.mConfigurationLocked.openFlags & 536870912) != 0;
        }
        return z;
    }

    public static int backupDatabase(String srcPath, String dstPath) {
        return SQLiteConnection.native_backup(srcPath, dstPath);
    }

    public int getCorruptCode() {
        return this.mCorrupt_code;
    }

    void onCorruption(int error_code) {
        this.mCorrupt_code = error_code;
        EventLog.writeEvent(EVENT_DB_CORRUPT, getLabel());
        this.mErrorHandler.onCorruption(this);
    }

    static ArrayList<DbStats> getDbStats() {
        ArrayList<DbStats> dbStatsList = new ArrayList();
        Iterator i$ = getActiveDatabases().iterator();
        while (i$.hasNext()) {
            ((SQLiteDatabase) i$.next()).collectDbStats(dbStatsList);
        }
        return dbStatsList;
    }

    private void collectDbStats(ArrayList<DbStats> dbStatsList) {
        synchronized (this.mLock) {
            if (this.mConnectionPoolLocked != null) {
                this.mConnectionPoolLocked.collectDbStats(dbStatsList);
            }
        }
    }

    private static ArrayList<SQLiteDatabase> getActiveDatabases() {
        ArrayList<SQLiteDatabase> databases = new ArrayList();
        synchronized (sActiveDatabases) {
            databases.addAll(sActiveDatabases.keySet());
        }
        return databases;
    }

    static void dumpAll(Printer printer, boolean verbose) {
        Iterator i$ = getActiveDatabases().iterator();
        while (i$.hasNext()) {
            ((SQLiteDatabase) i$.next()).dump(printer, verbose);
        }
    }

    private void dump(Printer printer, boolean verbose) {
        synchronized (this.mLock) {
            if (this.mConnectionPoolLocked != null) {
                printer.println(ProxyInfo.LOCAL_EXCL_LIST);
                this.mConnectionPoolLocked.dump(printer, verbose);
            }
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<android.util.Pair<java.lang.String, java.lang.String>> getAttachedDbs() {
        /*
        r6 = this;
        r2 = 0;
        r0 = new java.util.ArrayList;
        r0.<init>();
        r3 = r6.mLock;
        monitor-enter(r3);
        r4 = r6.mConnectionPoolLocked;	 Catch:{ all -> 0x0025 }
        if (r4 != 0) goto L_0x0010;
    L_0x000d:
        monitor-exit(r3);	 Catch:{ all -> 0x0025 }
        r0 = r2;
    L_0x000f:
        return r0;
    L_0x0010:
        r2 = r6.mHasAttachedDbsLocked;	 Catch:{ all -> 0x0025 }
        if (r2 != 0) goto L_0x0028;
    L_0x0014:
        r2 = new android.util.Pair;	 Catch:{ all -> 0x0025 }
        r4 = "main";
        r5 = r6.mConfigurationLocked;	 Catch:{ all -> 0x0025 }
        r5 = r5.path;	 Catch:{ all -> 0x0025 }
        r2.<init>(r4, r5);	 Catch:{ all -> 0x0025 }
        r0.add(r2);	 Catch:{ all -> 0x0025 }
        monitor-exit(r3);	 Catch:{ all -> 0x0025 }
        goto L_0x000f;
    L_0x0025:
        r2 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0025 }
        throw r2;
    L_0x0028:
        r6.acquireReference();	 Catch:{ all -> 0x0025 }
        monitor-exit(r3);	 Catch:{ all -> 0x0025 }
        r1 = 0;
        r2 = "pragma database_list;";
        r3 = 0;
        r1 = r6.rawQuery(r2, r3);	 Catch:{ all -> 0x004e }
    L_0x0035:
        r2 = r1.moveToNext();	 Catch:{ all -> 0x004e }
        if (r2 == 0) goto L_0x005a;
    L_0x003b:
        r2 = new android.util.Pair;	 Catch:{ all -> 0x004e }
        r3 = 1;
        r3 = r1.getString(r3);	 Catch:{ all -> 0x004e }
        r4 = 2;
        r4 = r1.getString(r4);	 Catch:{ all -> 0x004e }
        r2.<init>(r3, r4);	 Catch:{ all -> 0x004e }
        r0.add(r2);	 Catch:{ all -> 0x004e }
        goto L_0x0035;
    L_0x004e:
        r2 = move-exception;
        if (r1 == 0) goto L_0x0054;
    L_0x0051:
        r1.close();	 Catch:{ all -> 0x0055 }
    L_0x0054:
        throw r2;	 Catch:{ all -> 0x0055 }
    L_0x0055:
        r2 = move-exception;
        r6.releaseReference();
        throw r2;
    L_0x005a:
        if (r1 == 0) goto L_0x005f;
    L_0x005c:
        r1.close();	 Catch:{ all -> 0x0055 }
    L_0x005f:
        r6.releaseReference();
        goto L_0x000f;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.database.sqlite.SQLiteDatabase.getAttachedDbs():java.util.List<android.util.Pair<java.lang.String, java.lang.String>>");
    }

    public boolean isDatabaseIntegrityOk() {
        List<Pair<String, String>> attachedDbs;
        SQLiteStatement prog;
        List<Pair<String, String>> attachedDbs2;
        Throwable th;
        acquireReference();
        try {
            attachedDbs = getAttachedDbs();
            if (attachedDbs == null) {
                throw new IllegalStateException("databaselist for: " + getPath() + " couldn't " + "be retrieved. probably because the database is closed");
            }
        } catch (SQLiteException e) {
            attachedDbs2 = null;
            attachedDbs = new ArrayList();
            attachedDbs.add(new Pair("main", getPath()));
        } catch (Throwable th2) {
            th = th2;
        }
        int i = 0;
        while (i < attachedDbs.size()) {
            Pair<String, String> p = (Pair) attachedDbs.get(i);
            prog = null;
            prog = compileStatement("PRAGMA " + ((String) p.first) + ".integrity_check(1);");
            String rslt = prog.simpleQueryForIntegrityCheck();
            this.mIntegrityErrorString = null;
            if (rslt == null || rslt.equalsIgnoreCase("ok")) {
                if (prog != null) {
                    prog.close();
                }
                i++;
            } else {
                this.mIntegrityErrorString = rslt;
                Log.e(TAG, "PRAGMA integrity_check on " + ((String) p.second) + " returned: " + rslt);
                if (prog != null) {
                    prog.close();
                }
                releaseReference();
                return false;
            }
        }
        releaseReference();
        return true;
        releaseReference();
        throw th;
    }

    public String getIntegrityErrorInfo() {
        return this.mIntegrityErrorString;
    }

    public String toString() {
        return "SQLiteDatabase: " + getPath();
    }

    private void throwIfNotOpenLocked() {
        if (this.mConnectionPoolLocked == null) {
            throw new IllegalStateException("The database '" + this.mConfigurationLocked.label + "' is not open.");
        }
    }

    public static void renameDatabaseFile(String from, String to) {
        new File(from).renameTo(new File(to));
        File fi = new File(from + "-wal");
        if (fi.exists()) {
            fi.renameTo(new File(to + "-wal"));
        }
        fi = new File(from + "-shm");
        if (fi.exists()) {
            fi.renameTo(new File(to + "-shm"));
        }
    }

    public static boolean deleteDatabaseFile(String file) {
        boolean ret = new File(file).delete();
        if (ret) {
            File fi = new File(file + "-journal");
            if (fi.exists()) {
                fi.delete();
            }
            fi = new File(file + "-wal");
            if (fi.exists()) {
                fi.delete();
            }
            fi = new File(file + "-shm");
            if (fi.exists()) {
                fi.delete();
            }
        }
        return ret;
    }
}
