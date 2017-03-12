package android.database.sqlite;

import android.database.SQLException;
import android.net.ProxyInfo;

public class SQLiteException extends SQLException {
    private static final int errCode = -1;
    private static final String[][] errString;
    private static final String[] errString2 = new String[]{"SQLITE_ROW", "SQLITE_DONE"};

    static {
        String[][] strArr = new String[29][];
        strArr[0] = new String[]{"SQLITE_OK", ProxyInfo.LOCAL_EXCL_LIST};
        strArr[1] = new String[]{"SQLITE_ERROR", "SQL(query) error or missing database."};
        strArr[2] = new String[]{"SQLITE_INTERNAL", ProxyInfo.LOCAL_EXCL_LIST};
        strArr[3] = new String[]{"SQLITE_PERM", "Access permission denied."};
        strArr[4] = new String[]{"SQLITE_ABORT", "Callback routine requested an abort."};
        strArr[5] = new String[]{"SQLITE_BUSY", "The database file is locked."};
        strArr[6] = new String[]{"SQLITE_LOCKED", "A table in the database is locked."};
        strArr[7] = new String[]{"SQLITE_NOMEM", "There is no enough heap memory for application."};
        strArr[8] = new String[]{"SQLITE_READONLY", "Attempt to write a readonly database. (database may be deleted by other process)"};
        strArr[9] = new String[]{"SQLITE_INTERRUPT", "Operation terminated by interrupt function."};
        strArr[10] = new String[]{"SQLITE_IOERR", "disk I/O error occurred."};
        strArr[11] = new String[]{"SQLITE_CORRUPT", "The database disk image is malformed."};
        strArr[12] = new String[]{"SQLITE_NOTFOUND", "Unknown operation code."};
        strArr[13] = new String[]{"SQLITE_FULL", "No available space in disk."};
        strArr[14] = new String[]{"SQLITE_CANTOPEN", "Unable to open the database file."};
        strArr[15] = new String[]{"SQLITE_PROTOCOL", "Database lock protocol error."};
        strArr[16] = new String[]{"SQLITE_EMPTY", "Database is empty."};
        strArr[17] = new String[]{"SQLITE_SCHEMA", "The database schema changed."};
        strArr[18] = new String[]{"SQLITE_TOOBIG", "String or BLOB exceeds size limits."};
        strArr[19] = new String[]{"SQLITE_CONSTRAINT", "Abort due to constraint violation."};
        strArr[20] = new String[]{"SQLITE_MISMATCH", "Data type mismatch."};
        strArr[21] = new String[]{"SQLITE_MISUSE", "Library used incorrectly."};
        strArr[22] = new String[]{"SQLITE_NOLFS", "Uses OS features not supported on host."};
        strArr[23] = new String[]{"SQLITE_AUTH", "Authorization denied."};
        strArr[24] = new String[]{"SQLITE_FORMAT", "Auxiliary database format error."};
        strArr[25] = new String[]{"SQLITE_RANGE", ProxyInfo.LOCAL_EXCL_LIST};
        strArr[26] = new String[]{"SQLITE_NOTADB", "File opened that is not a database file or encrypted."};
        strArr[27] = new String[]{"SQLITE_NOTICE", ProxyInfo.LOCAL_EXCL_LIST};
        strArr[28] = new String[]{"SQLITE_WARNING", ProxyInfo.LOCAL_EXCL_LIST};
        errString = strArr;
    }

    public SQLiteException(String error) {
        super(error + makeSQLiteExceptionLog(error));
    }

    public SQLiteException(String error, Throwable cause) {
        super(error + makeSQLiteExceptionLog(error), cause);
    }

    private static String makeSQLiteExceptionLog(String error) {
        SQLiteExceptionLog logAdder = new SQLiteExceptionLog(-1, error);
        int majorCode = logAdder.getMajorCode();
        if (majorCode >= 100 && majorCode <= 101) {
            logAdder.setErrString(errString2[majorCode - 100]);
            return logAdder.makeSQLiteExceptionLog(ProxyInfo.LOCAL_EXCL_LIST);
        } else if (majorCode < 0 || majorCode >= errString.length) {
            return ProxyInfo.LOCAL_EXCL_LIST;
        } else {
            int minorCode = logAdder.getMinorCode();
            logAdder.setErrString(getErrString(majorCode, minorCode));
            return logAdder.makeSQLiteExceptionLog(getCausedBy(majorCode, minorCode));
        }
    }

    private static String getCausedBy(int majorCode, int minorCode) {
        switch (majorCode) {
            case 8:
                if (minorCode > 0 && minorCode < SQLiteReadOnlyDatabaseException.errString.length) {
                    return SQLiteReadOnlyDatabaseException.errString[minorCode][1];
                }
            case 10:
                if (minorCode > 0 && minorCode < SQLiteDiskIOException.errString.length) {
                    return SQLiteDiskIOException.makeCausedBy(minorCode);
                }
            case 14:
                if (minorCode > 4 && minorCode < SQLiteCantOpenDatabaseException.errString.length) {
                    return SQLiteCantOpenDatabaseException.errString[minorCode][1];
                }
        }
        return errString[majorCode][1];
    }

    private static String getErrString(int majorCode, int minorCode) {
        switch (majorCode) {
            case 8:
                if (minorCode > 0 && minorCode < SQLiteReadOnlyDatabaseException.errString.length) {
                    return SQLiteReadOnlyDatabaseException.errString[minorCode][0];
                }
            case 10:
                if (minorCode > 0 && minorCode < SQLiteDiskIOException.errString.length) {
                    return SQLiteDiskIOException.errString[minorCode][0];
                }
            case 14:
                if (minorCode > 0 && minorCode < SQLiteCantOpenDatabaseException.errString.length) {
                    return SQLiteCantOpenDatabaseException.errString[minorCode][0];
                }
            case 19:
                if (minorCode > 0 && minorCode < SQLiteConstraintException.errString.length) {
                    return SQLiteConstraintException.errString[minorCode];
                }
        }
        return errString[majorCode][0];
    }
}
