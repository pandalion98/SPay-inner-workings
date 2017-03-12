package android.database.sqlite;

import android.net.ProxyInfo;

public class SQLiteConstraintException extends SQLiteException {
    public static final int errCode = 19;
    public static final String[] errString = new String[]{"SQLITE_CONSTRAINT", "SQLITE_CONSTRAINT_CHECK", "SQLITE_CONSTRAINT_COMMITHOOK", "SQLITE_CONSTRAINT_FOEIGNKEY", "SQLITE_CONSTRAINT_FUNCTION", "SQLITE_CONSTRAINT_NOTNULL", "SQLITE_CONSTRAINT_PRIMARYKEY", "SQLITE_CONSTRAINT_TRIGGER", "SQLITE_CONSTRAINT_UNIQUE", "SQLITE_CONSTRAINT_VTAB", "SQLITE_CONSTRAINT_ROWID"};

    public SQLiteConstraintException(String error) {
        super(error + addErrCode(error));
    }

    private static String addErrCode(String errMessage) {
        if (errMessage == null || errMessage.indexOf("(code ") <= 0) {
            return " (code 19)";
        }
        return ProxyInfo.LOCAL_EXCL_LIST;
    }
}
