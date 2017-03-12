package android.database.sqlite;

import android.net.ProxyInfo;

public class SQLiteReadOnlyDatabaseException extends SQLiteException {
    public static final int errCode = 8;
    public static final String[][] errString;

    static {
        String[][] strArr = new String[5][];
        strArr[0] = new String[]{"SQLITE_READONLY", "Attempt to write a readonly database."};
        strArr[1] = new String[]{"SQLITE_READONLY_RECOVERY", "Failed to reconstruct index for WAL file due to existing SHM file was Read-Only."};
        strArr[2] = new String[]{"SQLITE_READONLY_CANTLOCK", "Failed to get lock due to SHM file was Read-Only"};
        strArr[3] = new String[]{"SQLITE_READONLY_ROLLBACK", "Failed to execute ROLLBACK due to Database file was Read-Only"};
        strArr[4] = new String[]{"SQLITE_READONLY_DBMOVED", "Database or Journal file have been removed."};
        errString = strArr;
    }

    public SQLiteReadOnlyDatabaseException(String error) {
        super(error + addErrCode(error));
    }

    private static String addErrCode(String errMessage) {
        if (errMessage == null || errMessage.indexOf("(code ") <= 0) {
            return " (code 8)";
        }
        return ProxyInfo.LOCAL_EXCL_LIST;
    }
}
