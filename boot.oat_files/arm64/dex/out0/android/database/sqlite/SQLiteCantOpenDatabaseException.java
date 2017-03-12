package android.database.sqlite;

import android.net.ProxyInfo;

public class SQLiteCantOpenDatabaseException extends SQLiteException {
    public static final int errCode = 14;
    public static final String[][] errString;

    static {
        r0 = new String[11][];
        r0[0] = new String[]{"SQLITE_CANTOPEN", "Unable to open the database file."};
        r0[1] = new String[]{"SQLITE_CANTOPEN_NOTEMPDIR", ProxyInfo.LOCAL_EXCL_LIST};
        r0[2] = new String[]{"SQLITE_CANTOPEN_ISDIR", ProxyInfo.LOCAL_EXCL_LIST};
        r0[3] = new String[]{"SQLITE_CANTOPEN_FULLPATH", ProxyInfo.LOCAL_EXCL_LIST};
        r0[4] = new String[]{"SQLITE_CANTOPEN_CONVPATH", ProxyInfo.LOCAL_EXCL_LIST};
        r0[5] = new String[]{"SQLITE_CANTOPEN_ENOENT", "Specified directory or database file does not exist."};
        r0[6] = new String[]{"SQLITE_CANTOPEN_ENOMEM", "No available memory space."};
        r0[7] = new String[]{"SQLITE_CANTOPEN_EACCES", "Application has no permission to open the specified database file."};
        r0[8] = new String[]{"SQLITE_CANTOPEN_EMFILE", "Application has opened two many files. Maximum of available file descriptors in one process is 1024 in default."};
        r0[9] = new String[]{"SQLITE_CANTOPEN_ENOSPC", "No available space in disk."};
        r0[10] = new String[]{"SQLITE_CANTOPEN_EROFS", "Current partition has been mounted with read-only mode."};
        errString = r0;
    }

    public SQLiteCantOpenDatabaseException(String error) {
        super(error + addErrCode(error));
    }

    private static String addErrCode(String errMessage) {
        if (errMessage == null || errMessage.indexOf("(code ") <= 0) {
            return " (code 14)";
        }
        return ProxyInfo.LOCAL_EXCL_LIST;
    }
}
